package org.example.test.aop;

import org.example.aop.advice.AfterReturningAdvice;

import java.lang.reflect.Method;

public class MyAfterAdvice implements AfterReturningAdvice {
    @Override
    public void afterReturning(Object returnValue, Method method, Object[] args, Object target) throws Throwable {
        System.out.println("my interceptor after method call");
    }
}
