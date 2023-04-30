package org.example.aop.aopProxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class JdkDynamicAopProxy implements AopProxy, InvocationHandler {
    Object target;

    public JdkDynamicAopProxy(Object target) {
        this.target = target;
    }


    @Override
    public Object getProxy() {
        Class<?>[] interfaces = target.getClass().getInterfaces();

        System.out.println("动态创建代理对象。for：" + target + "interfaces：" + interfaces);

        ClassLoader classLoader = JdkDynamicAopProxy.class.getClassLoader();
        Object obj = Proxy.newProxyInstance(classLoader, interfaces, this);
        return obj;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getName().equals("doAction")) {
            System.out.println("动态代理：在调用被代理对象的方法前调用的代码");
            Object result = method.invoke(target, args);
            System.out.println("动态代理：在调用被代理对象的方法后调用的代码");
            return result;
        } else {
            throw new RuntimeException("doAction() 方法不存在");
        }
    }
}
