package org.example.aop.aopProxyFactory;

import org.example.aop.advisor.Advisor;
import org.example.aop.aopProxy.AopProxy;
import org.example.aop.aopProxy.JdkDynamicAopProxy;

public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createAopProxy(Object target, Advisor advisor) {
        //if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
        return new JdkDynamicAopProxy(target, advisor);
        //}
        //return new CglibAopProxy(config);
    }
}
