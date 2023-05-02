package org.example.aop.aopProxyFactory;

import org.example.aop.advisor.Advisor;
import org.example.aop.aopProxy.AopProxy;

public interface AopProxyFactory {
    AopProxy createAopProxy(Object target, Advisor adviseor);

}
