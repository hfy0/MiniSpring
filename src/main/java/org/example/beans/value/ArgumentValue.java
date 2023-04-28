package org.example.beans.value;

import java.util.Objects;

public class ArgumentValue {
    private Object value;
    private String type;
    private String name;

    public ArgumentValue(String type, Object value) {
        this.value = Objects.requireNonNull(value);
        this.type = Objects.requireNonNull(type);
    }

    public ArgumentValue(String type, String name, Object value) {
        this.value = Objects.requireNonNull(value);
        this.type = Objects.requireNonNull(type);
        this.name = Objects.requireNonNull(name);
    }


    public Object getValue() {
        return this.value;
    }


    public String getType() {
        return this.type;
    }

    public String getName() {
        return this.name;
    }

}

