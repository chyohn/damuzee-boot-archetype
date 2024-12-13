package com.damuzee.boot.spec.tenant.lock;

import java.lang.reflect.Method;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.ParameterNameDiscoverer;

public class DistributedLockEvaluationContext extends MethodBasedEvaluationContext {

    public DistributedLockEvaluationContext(final Object rootObject, final Method method, final Object[] arguments,
        final ParameterNameDiscoverer parameterNameDiscoverer) {
        super(rootObject, method, arguments, parameterNameDiscoverer);
    }
}
