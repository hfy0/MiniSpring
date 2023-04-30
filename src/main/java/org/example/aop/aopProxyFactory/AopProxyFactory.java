package org.example.aop.aopProxyFactory;

import org.example.aop.aopProxy.AopProxy;

public interface AopProxyFactory {
    AopProxy createAopProxy(Object target);

}
