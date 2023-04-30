package org.example.aop.factoryBean;


import org.example.beans.BeansException;
import org.example.beans.beanRegistry.DefaultSingletonBeanRegistry;

public abstract class FactoryBeanRegistrySupport extends DefaultSingletonBeanRegistry {

    protected Class<?> getTypeForFactoryBean(final FactoryBean<?> factoryBean) {
        return factoryBean.getObjectType();
    }

    /**
     * @param factoryBean
     * @param beanName
     * @return 代理对象
     */
    protected Object getObjectFromFactoryBean(FactoryBean<?> factoryBean, String beanName) {
        Object object = doGetObjectFromFactoryBean(factoryBean, beanName);
        try {
            object = postProcessObjectFromFactoryBean(object, beanName);
        } catch (BeansException e) {
            e.printStackTrace();
        }
        return object;
    }

    private Object doGetObjectFromFactoryBean(final FactoryBean<?> factoryBean, final String beanName) {

        Object object = null;
        try {
            object = factoryBean.getObject();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return object;
    }

    /**
     * 处理器：在创建代理对象后，对代理对象进行处理
     *
     * @param object
     * @param beanName
     * @return
     * @throws BeansException
     */
    protected Object postProcessObjectFromFactoryBean(Object object, String beanName) throws BeansException {
        System.out.println("处理器：在创建代理对象后，对代理对象进行处理");
        return object;
    }

    protected FactoryBean<?> getFactoryBean(String beanName, Object beanInstance) throws BeansException {
        if (!(beanInstance instanceof FactoryBean)) {
            throw new BeansException(
                    "Bean instance of type [" + beanInstance.getClass() + "] is not a FactoryBean");
        }
        return (FactoryBean<?>) beanInstance;
    }
}
