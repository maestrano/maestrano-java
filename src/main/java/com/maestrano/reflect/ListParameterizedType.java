package com.maestrano.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class ListParameterizedType implements ParameterizedType {

    private Type type;

    public ListParameterizedType(Type type) {
        this.type = type;
    }

    public Type[] getActualTypeArguments() {
        return new Type[] {type};
    }

    public Type getRawType() {
        return ArrayList.class;
    }

    public Type getOwnerType() {
        return null;
    }
}
