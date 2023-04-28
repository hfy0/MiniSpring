package org.example.beans;

import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import org.example.beans.value.ArgumentValue;
import org.example.beans.value.ArgumentValues;
import org.example.beans.value.PropertyValue;
import org.example.beans.value.PropertyValues;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class XmlBeanDefinitionReader {

    SimpleBeanFactory beanFactory;

    public XmlBeanDefinitionReader(SimpleBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    public void loadBeanDefinitions(String fileName) throws Throwable {
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
                BeanDefinition beanDefinition = new BeanDefinition(beanID, beanClassName);

                // 使用构造器给对象的属性赋值
                List<Element> constructorElements = element.elements("constructor-arg");
                ArgumentValues AVS = new ArgumentValues();
                for (Element e : constructorElements) {
                    String pType = e.attributeValue("type");
                    String pName = e.attributeValue("name");
                    String pValue = e.attributeValue("value");
                    AVS.addArgumentValue(new ArgumentValue(pType, pName, pValue));
                }
                beanDefinition.setConstructorArgumentValues(AVS);
                //end of handle constructor

                //handle properties
                List<Element> propertyElements = element.elements("property");
                PropertyValues PVS = new PropertyValues();
                List<String> refs = new ArrayList<>();
                for (Element e : propertyElements) {
                    String pType = e.attributeValue("type");
                    String pName = e.attributeValue("name");
                    String pValue = e.attributeValue("value");
                    String pRef = e.attributeValue("ref");
                    String pV = "";
                    boolean isRef = false;
                    if (pValue != null && !pValue.equals("")) {
                        isRef = false;
                        pV = pValue;
                    } else if (pRef != null && !pRef.equals("")) {
                        isRef = true;
                        pV = pRef;
                        refs.add(pRef);
                    }
                    PVS.addPropertyValue(new PropertyValue(pType, pName, pV, isRef));
                }
                beanDefinition.setPropertyValues(PVS);
                String[] refArray = refs.toArray(new String[0]);
                beanDefinition.setDependsOn(refArray);
                //end of handle properties

                beanFactory.registerBeanDefinition(beanID, beanDefinition);
            }
        });
    }
}
