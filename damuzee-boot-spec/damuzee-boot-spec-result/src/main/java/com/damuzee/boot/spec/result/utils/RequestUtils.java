package com.damuzee.boot.spec.result.utils;

import com.damuzee.boot.spec.result.vo.HttpResult;
import com.damuzee.boot.spec.result.vo.IResult;
import com.damuzee.boot.spec.result.vo.RpcResult;
import com.damuzee.boot.spec.result.exception.KindlyException;
import com.damuzee.boot.spec.util.DamuzeeArchContextUtil;
import java.util.concurrent.Callable;
import lombok.SneakyThrows;

public class RequestUtils {

    public static String getTraceId() {
        return null;//Monitor.getTraceId();
    }

    /**
     * 使用指定的集团ID作为执行上下文的集团ID，来执行指定的{@link Callable#call()}方法
     * <p>
     * 如果结果类型为：{@link RpcResult}或者{@link HttpResult}，检查结果如果success=false，则抛出异常
     *
     * @throws KindlyException
     */
    public static <V extends IResult<?>> V callWithGroupId(Long newGroupId, Callable<V> callable)
        throws KindlyException {
        V v = DamuzeeArchContextUtil.callWithGroupId(newGroupId, callable);
        v.assertSuccess();
        return v;
    }

    /**
     * 使用指定的集团ID作为执行上下文的集团ID，来执行指定的{@link Callable#call()}方法
     * <p>
     * 如果结果类型为：{@link RpcResult}或者{@link HttpResult}，检查结果如果success=false，则抛出异常
     *
     * @throws KindlyException
     */
    public static <V extends IResult<?>> V callWithTenantId(Long newTenantId, Callable<V> callable)
        throws KindlyException {
        V v = DamuzeeArchContextUtil.callWithTenantId(newTenantId, callable);
        v.assertSuccess();
        return v;
    }

    /**
     * 执行指定的{@link Callable#call()}方法，如果结果类型为：{@link RpcResult}或者{@link HttpResult}，检查结果如果success=false，则抛出异常
     *
     * @throws KindlyException
     */
    @SneakyThrows
    public static <V extends IResult<?>> V call(Callable<V> callable) throws KindlyException {
        V v = callable.call();
        v.assertSuccess();
        return v;
    }

}
