package org.example.context;


import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.beans.BeanDefinition;

import java.net.URL;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ClassPathXmlApplicationContext {
    private List<BeanDefinition> beanDefinitions = new ArrayList<>();
    private Map<String, Object> singletons = new HashMap<>();

    public ClassPathXmlApplicationContext(String fileName) throws Throwable {
        this.readXml(fileName);
        this.instanceBeans();
    }

    private void readXml(String fileName) throws Throwable {
        SAXReader saxReader = new SAXReader();

        URL xmlPath = this.getClass().getClassLoader().getResource(fileName);
        if (Objects.isNull(xmlPath)) {
            throw new Exception("文件读取失败:" + fileName);
        }

        Document document = saxReader.read(xmlPath);
        Element rootElement = document.getRootElement();

        // 对 XML 配置文件中的每一个<bean>，进行处理
        rootElement.elements().stream().forEach(new Consumer<Element>() {
            @Override
            public void accept(Element element) {
                // 根据 XML 文件中的信息生成 BeanDefinition
                String beanID = element.attributeValue("id");
                String beanClassName = element.attributeValue("class");
                BeanDefinition beanDefinition = new BeanDefinition(beanID,
                        beanClassName);

                // 将创建好的 BeanDefinition 存储在 beanDefinitions 变量中
                beanDefinitions.add(beanDefinition);
            }
        });
    }

    // 使用反射机制创建 Bean 的实例，并将创建好的 Bean 的实例存储在 singletons 变量中
    private void instanceBeans() {
        beanDefinitions.stream().forEach(new Consumer<BeanDefinition>() {
            @Override
            public void accept(BeanDefinition beanDefinition) {
                String id = beanDefinition.getId();
                Object newInstance = null;
                try {
                    newInstance = Class.forName(beanDefinition.getClassName()).newInstance();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e + "创建对象失败，无法定位类。");
                } catch (InstantiationException | IllegalAccessException e) {
                    throw new RuntimeException(e + "创建对象失败。");
                }

                singletons.put(id, newInstance);
            }
        });
    }

    //这是对外的一个方法，让外部程序从 Ioc 容器中获取对象
    public Object getBean(String beanName) throws Throwable {
        Object instance = singletons.get(beanName);
        return Optional.ofNullable(instance).orElseThrow(new Supplier<Throwable>() {
            @Override
            public Throwable get() {
                return new Exception("获取" + beanName + "对象失败");
            }
        });
    }
}
