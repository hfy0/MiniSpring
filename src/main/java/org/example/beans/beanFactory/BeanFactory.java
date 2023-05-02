package org.example.beans.beanFactory;


import org.example.beans.BeansException;

public interface BeanFactory {
    Object getBean(String name) throws BeansException;

    boolean containsBean(String name);

    boolean isSingleton(String name);

    boolean isPrototype(String name);

    Class<?> getType(String name);

}
