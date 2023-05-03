package org.example.aop.advisor;

import org.example.aop.advice.Advice;
import org.example.aop.advice.interceptor.MethodInterceptor;

public interface Advisor {
    MethodInterceptor getMethodInterceptor();

    void setMethodInterceptor(MethodInterceptor methodInterceptor);

    Advice getAdvice();
}
