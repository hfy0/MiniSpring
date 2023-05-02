package org.example.aop.advice.interceptor;

import org.example.aop.methodInvocation.MethodInvocation;

public interface MethodInterceptor extends Interceptor {
    Object invoke(MethodInvocation invocation) throws Throwable;
}
