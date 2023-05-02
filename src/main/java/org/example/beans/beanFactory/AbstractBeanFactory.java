package org.example.beans.beanFactory;

import org.example.aop.factoryBean.FactoryBean;
import org.example.aop.factoryBean.FactoryBeanRegistrySupport;
import org.example.beans.BeanDefinition;
import org.example.beans.BeansException;
import org.example.beans.beanRegistry.DefaultSingletonBeanRegistry;
import org.example.beans.value.ArgumentValue;
import org.example.beans.value.ArgumentValues;
import org.example.beans.value.PropertyValue;
import org.example.beans.value.PropertyValues;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

public abstract class AbstractBeanFactory extends FactoryBeanRegistrySupport implements ConfigurableBeanFactory {

    /**
     * BeanDefinition 相关
     */
    private Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>();
    private List<String> beanDefinitionNames = new ArrayList<>();

    // 存储毛胚 Bean
    private final Map<String, Object> earlySingletonObjects = new HashMap<>();

    public void refresh() {
        this.beanDefinitionNames.stream().forEach(new Consumer<String>() {
            @Override
            public void accept(String beanName) {
                try {
                    getBean(beanName);
                } catch (BeansException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
        this.beanDefinitionNames.add(beanName);
        if (!beanDefinition.isLazyInit()) {
            try {
                getBean(beanName);
            } catch (BeansException e) {
                e.printStackTrace();
            }
        }
    }

    public Object getBean(String beanName) throws BeansException {
        Object singleton = this.getSingleton(beanName);

        if (singleton == null) {
            singleton = this.earlySingletonObjects.get(beanName);
            if (singleton == null) {
                BeanDefinition bd = beanDefinitionMap.get(beanName);
                if (bd != null) {
                    // 利用反射机制，创建对象并给对象的属性赋值
                    singleton = createBean(bd);

                    // 将创建好的对象存储起来
                    this.registerBean(beanName, singleton);

                    if (singleton instanceof BeanFactoryAware) {
                        ((BeanFactoryAware) singleton).setBeanFactory(this);
                    }

                    // beanPostProcessor (Bean处理器)
                    // step 1 : postProcessBeforeInitialization
                    // 在调用 initMethodName 指定的方法进行 Bean 的初始化工作前，对 Bean 进行处理
                    applyBeanPostProcessorsBeforeInitialization(singleton, beanName);

                    // step 2 : init-method
                    // 调用 initMethodName 指定的方法进行 Bean 的初始化工作
                    if (bd.getInitMethodName() != null && !bd.getInitMethodName().equals("")) {
                        invokeInitMethod(bd, singleton);
                    }

                    // step 3 : postProcessAfterInitialization
                    // 在调用 initMethodName 指定的方法进行 Bean 的初始化工作后，对 Bean 进行处理
                    applyBeanPostProcessorsAfterInitialization(singleton, beanName);
                } else {
                    return null;
                }
            }
        }

        // process Factory Bean
        // 处理 Factory Bean
        if (singleton instanceof FactoryBean) {
            return this.getObjectForBeanInstance(singleton, beanName);
        }

        return singleton;
    }

    protected Object getObjectForBeanInstance(Object beanInstance, String beanName) {
        // 如果 beanInstance 不是 FactoryBean，则直接返回
        if (!(beanInstance instanceof FactoryBean)) {
            return beanInstance;
        }

        Object object = null;
        FactoryBean<?> factoryBean = (FactoryBean<?>) beanInstance;
        // object 为代理对象
        System.out.println("beanName：" + beanName + " 对象是 FactoryBean 类型。Object：" + factoryBean);
        System.out.println("下面获取 " + "beanName：" + beanName + " 对应的代理对象");
        object = getObjectFromFactoryBean(factoryBean, beanName);
        return object;
    }

    private void invokeInitMethod(BeanDefinition bd, Object obj) {
        Class<?> clz = obj.getClass();
        Method method = null;
        try {
            method = clz.getMethod(bd.getInitMethodName());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (SecurityException e) {
            e.printStackTrace();
        }
        try {
            method.invoke(obj);
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        }
    }


    public void registerBean(String beanName, Object obj) {
        this.registerSingleton(beanName, obj);

        // TODO beanpostprocessor
    }

    /**
     * 利用反射机制，创建对象并给对象的属性赋值
     *
     * @param beanDefinition
     * @return
     */
    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clazz = null;
        // 利用反射机制，使用构造器创建对象
        Object obj = doCreateBean(beanDefinition);

        this.earlySingletonObjects.put(beanDefinition.getId(), obj);

        try {
            clazz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        // 使用 setter() 方法给对象的属性赋值
        handleProperties(beanDefinition, clazz, obj);

        return obj;
    }

    /**
     * 利用反射机制，使用构造器创建对象
     *
     * @param beanDefinition
     * @return
     */
    private Object doCreateBean(BeanDefinition beanDefinition) {
        Class<?> clazz = null;
        Object obj = null;
        Constructor<?> constructor = null;

        try {
            clazz = Class.forName(beanDefinition.getClassName());

            // 使用构造器创建对象
            ArgumentValues argumentValues = beanDefinition.getConstructorArgumentValues();
            if (!argumentValues.isEmpty()) {
                Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
                Object[] paramValues = new Object[argumentValues.getArgumentCount()];
                for (int i = 0; i < argumentValues.getArgumentCount(); i++) {
                    ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
                    if ("String".equals(argumentValue.getType()) || "java.lang.String".equals(argumentValue.getType())) {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    } else if ("Integer".equals(argumentValue.getType()) || "java.lang.Integer".equals(argumentValue.getType())) {
                        paramTypes[i] = Integer.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue());
                    } else if ("int".equals(argumentValue.getType())) {
                        paramTypes[i] = int.class;
                        paramValues[i] = Integer.valueOf((String) argumentValue.getValue()).intValue();
                    } else {
                        paramTypes[i] = String.class;
                        paramValues[i] = argumentValue.getValue();
                    }
                }
                try {
                    constructor = clazz.getConstructor(paramTypes);
                    obj = constructor.newInstance(paramValues);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            } else {
                obj = clazz.newInstance();
            }

        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        System.out.println(beanDefinition.getId() + " 对象被创建" + beanDefinition.getClassName() + " : " + obj.toString());

        return obj;

    }

    /**
     * 利用反射机制，使用 setter() 方法为对象的属性赋值
     *
     * @param beanDefinition
     * @param clazz
     * @param obj
     */
    private void handleProperties(BeanDefinition beanDefinition, Class<?> clazz, Object obj) {

        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (!propertyValues.isEmpty()) {

            for (int i = 0; i < propertyValues.size(); i++) {
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);

                String pName = propertyValue.getName();
                String pType = propertyValue.getType();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();

                Class<?>[] paramTypes = new Class<?>[1];
                Object[] paramValues = new Object[1];

                if (!isRef) {
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                    } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                    } else if ("int".equals(pType)) {
                        paramTypes[0] = int.class;
                    } else {
                        paramTypes[0] = String.class;
                    }

                    paramValues[0] = pValue;
                } else { //is ref, create the dependent beans
                    try {
                        paramTypes[0] = Class.forName(pType);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                        paramValues[0] = getBean((String) pValue);
                    } catch (BeansException e) {
                        e.printStackTrace();
                    }
                }

                String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);

                Method method = null;
                try {
                    method = clazz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                } catch (SecurityException e) {
                    e.printStackTrace();
                }
                try {
                    method.invoke(obj, paramValues);
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                } catch (IllegalArgumentException e) {
                    e.printStackTrace();
                } catch (InvocationTargetException e) {
                    e.printStackTrace();
                }
            }
            System.out.println("利用反射机制，使用 setter() 方法为对象的属性赋值 : " + beanDefinition.getId());
        }
    }

    abstract public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException;

    abstract public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException;

    @Override
    public boolean containsBean(String name) {
        return containsSingleton(name);
    }

    @Override
    public boolean isSingleton(String name) {
        return this.beanDefinitionMap.get(name).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) {
        return this.beanDefinitionMap.get(name).isPrototype();
    }

    @Override
    public Class<?> getType(String name) {
        return this.beanDefinitionMap.get(name).getClass();
    }
}
