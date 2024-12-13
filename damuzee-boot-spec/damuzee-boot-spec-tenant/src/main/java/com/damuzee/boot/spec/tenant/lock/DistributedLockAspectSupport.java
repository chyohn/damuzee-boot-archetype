package com.damuzee.boot.spec.tenant.lock;

import com.damuzee.boot.spec.tenant.TenantEnvironment;
import com.damuzee.boot.spec.tenant.redis.RedisKeyProperties;
import com.damuzee.boot.spec.tenant.redis.RedisPrefixKeyUtil;
import com.damuzee.boot.spec.tenant.redis.RedisProxy;
import com.damuzee.boot.spec.util.DamuzeeArchContext;

import java.lang.reflect.Method;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.context.expression.AnnotatedElementKey;
import org.springframework.expression.EvaluationContext;


@Aspect
@Slf4j
public class DistributedLockAspectSupport extends DistributedLockValueParser {

    private final static DistributedLockExpressionEvaluator EVALUATOR = new DistributedLockExpressionEvaluator();

    private final RedissonClient redissonClient;
    private final RedisKeyProperties redisKeyProperties;

    public DistributedLockAspectSupport(RedissonClient redissonClient, RedisKeyProperties redisKeyProperties) {
        this.redissonClient = redissonClient;
        this.redisKeyProperties = redisKeyProperties;
    }

    @Pointcut("@annotation(com.damuzee.boot.spec.tenant.lock.DistributedLock)")
    public void dLockAspect() {
    }

    @Around("dLockAspect()")
    public Object arround(ProceedingJoinPoint pj) throws Throwable {
        Object object;
        RLock lock;
        log.info("dLockAspect start");

        DistributedLock dLock = getDLock(pj);
        String lockKey = getLockKey(pj, dLock);

        lockKey = generateTenantKey(dLock, lockKey);

        if (log.isDebugEnabled()) {
            log.debug("lockKey:{}", lockKey);
        }

        lock = redissonClient.getLock(lockKey);
        if (log.isDebugEnabled()) {
            log.debug("lock:{}", lock);
        }

        if (lock != null) {

            long waitTime = dLock.waitTime();
            boolean status;
            if (waitTime <= 0) {
                status = lock.tryLock();
            } else {
                status = lock.tryLock(waitTime, dLock.timeUnit());
            }

            if (!status) {
                log.warn("lock not acquired {} after trying {} {}.", lockKey, waitTime, dLock.timeUnit());
                throw new DistributedLockAcquireException(dLock.bizCode(), "当前业务有其它用户正在进行相同操作，请稍后再试");
            } else {
                if (log.isDebugEnabled()) {
                    log.debug("lock acquired: {}", lockKey);
                }

                try {
                    object = pj.proceed();
                } finally {
                    if (lock.isHeldByCurrentThread()) {
                        lock.unlock();

                        if (log.isDebugEnabled()) {
                            log.debug("lock unlocked: {}", lockKey);
                        }
                    }
                }
            }
        } else {
            log.warn("lock not acquired: {}", lockKey);
            throw new DistributedLockIOException(dLock.bizCode(), "分布式锁[" + lockKey + "]获取失败");
        }
        return object;
    }

    /**
     * todo 这里需要再抽象一下
     *
     * @param dLock
     * @param lockKey
     * @return
     */
    private String generateTenantKey(final DistributedLock dLock, String lockKey) {
        DamuzeeArchContext context = TenantEnvironment.isGroup() ? DamuzeeArchContext.GROUP_ID : DamuzeeArchContext.TENANT_ID;
        String tenantId = context.get();//DamuzeeArchContext.TENANT_ID.get();
        String prefix = this.redisKeyProperties.getPrefix();
        if (dLock.tenantPrefix() && StringUtils.isNotBlank(tenantId)) {
            if (RedisProxy.twemproxy.equals(this.redisKeyProperties.getProxy())) {
                lockKey = RedisPrefixKeyUtil.generate4Twemproxy(tenantId, prefix, lockKey);
            } else {
                lockKey = RedisPrefixKeyUtil.generate(tenantId, prefix, lockKey);
            }
        } else {
            lockKey = prefix + ":" + lockKey;
        }
        return lockKey;
    }


    private String getLockKey(ProceedingJoinPoint pj, DistributedLock dLock) {
        Method method = ((MethodSignature) pj.getSignature()).getMethod();
        Object[] params = pj.getArgs();
        Class<?> targetClz = AopProxyUtils.ultimateTargetClass(pj.getTarget());

        EvaluationContext evaluationContext = EVALUATOR.createEvaluationContext(method, params, targetClz,
            this.beanFactory);

        AnnotatedElementKey annotatedElementKey = new AnnotatedElementKey(method, targetClz);

        String exp = dLock.name();
        Object value = null;
        try {
            value = EVALUATOR.parseExpression(exp, annotatedElementKey, evaluationContext);
        } catch (Exception e) {
            log.error("lock key parse error", e);
        }

        if (value == null) {
            return exp;
        }

        if (value instanceof String) {
            return (String) value;
        } else if (value instanceof List) {
            List<?> valueList = (List<?>) value;
            StringBuilder str = new StringBuilder();
            valueList.forEach(v -> str.append(v.toString()));
            return str.toString();
        } else {
            log.warn("only support string, list type.");
            return exp;
        }
    }

    private DistributedLock getDLock(ProceedingJoinPoint pj) {
        MethodSignature methodSignature = (MethodSignature) pj.getSignature();
        return methodSignature.getMethod().getAnnotation(DistributedLock.class);
    }
}
