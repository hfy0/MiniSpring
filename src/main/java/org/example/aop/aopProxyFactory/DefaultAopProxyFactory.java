package org.example.aop.aopProxyFactory;

import org.example.aop.aopProxy.AopProxy;
import org.example.aop.aopProxy.JdkDynamicAopProxy;

public class DefaultAopProxyFactory implements AopProxyFactory {

    @Override
    public AopProxy createAopProxy(Object target) {
        //if (targetClass.isInterface() || Proxy.isProxyClass(targetClass)) {
        return new JdkDynamicAopProxy(target);
        //}
        //return new CglibAopProxy(config);
    }
}
