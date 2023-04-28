package org.example.beans.value;

import java.util.Objects;

public class PropertyValue {
    private final String type;
    private final String name;
    private final Object value;
    private final boolean isRef;

    public PropertyValue(String type, String name, Object value, boolean isRef) {
        this.type = Objects.requireNonNull(type);
        this.name = Objects.requireNonNull(name);
        this.value = Objects.requireNonNull(value);
        this.isRef = Objects.requireNonNull(isRef);
    }

    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

    public Object getValue() {
        return this.value;
    }

    public boolean getIsRef() {
        return isRef;
    }

}

