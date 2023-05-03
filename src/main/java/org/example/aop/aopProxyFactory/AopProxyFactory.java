package org.example.aop.aopProxyFactory;

import org.example.aop.advisor.Advisor;
import org.example.aop.aopProxy.AopProxy;
import org.example.aop.pointcut.PointcutAdvisor;

public interface AopProxyFactory {
    AopProxy createAopProxy(Object target, PointcutAdvisor adviseor);

}
