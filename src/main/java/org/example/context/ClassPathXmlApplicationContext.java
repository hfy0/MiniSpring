package org.example.context;


import org.example.beans.BeanDefinition;
import org.example.beans.BeansException;
import org.example.beans.SimpleBeanFactory;
import org.example.beans.XmlBeanDefinitionReader;

import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ClassPathXmlApplicationContext {

    private SimpleBeanFactory beanFactory = new SimpleBeanFactory();

    public ClassPathXmlApplicationContext(String fileName) throws Throwable {
        this(fileName, true);
    }

    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) throws Throwable {
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(beanFactory);
        reader.loadBeanDefinitions(fileName);

        if (isRefresh) {
            this.beanFactory.refresh();
        }
    }
}
