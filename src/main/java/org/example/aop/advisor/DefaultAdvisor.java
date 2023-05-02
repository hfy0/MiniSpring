package org.example.aop.advisor;

import org.example.aop.advice.interceptor.MethodInterceptor;

import java.util.Objects;

public class DefaultAdvisor implements Advisor {
    private MethodInterceptor methodInterceptor;

    public DefaultAdvisor() {
    }

    public void setMethodInterceptor(MethodInterceptor methodInterceptor) {
        this.methodInterceptor = Objects.requireNonNull(methodInterceptor);
    }

    public MethodInterceptor getMethodInterceptor() {
        return this.methodInterceptor;
    }

}
