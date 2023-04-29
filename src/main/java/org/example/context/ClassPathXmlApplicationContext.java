package org.example.context;


import org.example.beans.BeansException;
import org.example.beans.beanProcessor.AutowiredAnnotationBeanPostProcessor;
import org.example.beans.beanFactory.AutowireCapableBeanFactory;
import org.example.beans.XmlBeanDefinitionReader;

public class ClassPathXmlApplicationContext {

    private AutowireCapableBeanFactory beanFactory;

    public ClassPathXmlApplicationContext(String fileName) throws Throwable {
        this(fileName, true);
    }

    public Object getBean(String beanName) throws BeansException {
        return this.beanFactory.getBean(beanName);
    }

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh) throws Throwable {
        this.beanFactory = new AutowireCapableBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(this.beanFactory);
        reader.loadBeanDefinitions(fileName);

        if (isRefresh) {
            try {
                refresh();
            } catch (IllegalStateException e) {
                e.printStackTrace();
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    public void refresh() throws BeansException, IllegalStateException {
        // Register bean processors that intercept bean creation.
        // 将 BeanPostProcessor 处理器注册进 BeanFactory，这些处理器拦截 bean 的创建
        registerBeanPostProcessors(this.beanFactory);

        // Initialize other special beans in specific context subclasses.
        onRefresh();
    }

    private void registerBeanPostProcessors(AutowireCapableBeanFactory bf) {
        //if (supportAutowire) {
        bf.addBeanPostProcessor(new AutowiredAnnotationBeanPostProcessor());
        //}
    }

    private void onRefresh() {
        this.beanFactory.refresh();
    }
}
