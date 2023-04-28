package org.example.beans;


import org.dom4j.Element;
import org.example.beans.value.ArgumentValues;
import org.example.beans.value.PropertyValue;
import org.example.beans.value.PropertyValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;

public class BeanDefinition {

    private static final String SCOPE_SINGLETON = "singleton";

    private static final String SCOPE_PROTOTYPE = "prototype";

    private String id;
    private String className;

    private ArgumentValues constructorArgumentValues;

    private PropertyValues propertyValues;

    private String[] dependsOn;

    private boolean lazyInit = true;

    public BeanDefinition(String id, String className) {
        this.id = Objects.requireNonNull(id);
        this.className = Objects.requireNonNull(className);
    }

    public String getId() {
        return id;
    }

    public String getClassName() {
        return className;
    }

    public ArgumentValues getConstructorArgumentValues() {
        return constructorArgumentValues;
    }

    public PropertyValues getPropertyValues() {
        return propertyValues;
    }

    public boolean isLazyInit() {
        return lazyInit;
    }

    public void setDependsOn(String[] dependsOn) {
        this.dependsOn = dependsOn;
    }

    public void setPropertyValues(PropertyValues propertyValues) {
        this.propertyValues = (propertyValues != null ? propertyValues : new PropertyValues());
    }

    public void setConstructorArgumentValues(ArgumentValues constructorArgumentValues) {
        this.constructorArgumentValues =
                (constructorArgumentValues != null ? constructorArgumentValues : new ArgumentValues());
    }


}
