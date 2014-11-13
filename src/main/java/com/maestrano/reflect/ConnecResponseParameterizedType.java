package com.maestrano.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.maestrano.net.ConnecResponse;

public class ConnecResponseParameterizedType implements ParameterizedType {
	private Type type;

    public ConnecResponseParameterizedType(Type type) {
        this.type = type;
    }

    public Type[] getActualTypeArguments() {
        return new Type[] {type};
    }

    public Type getRawType() {
        return ConnecResponse.class;
    }

    public Type getOwnerType() {
        return null;
    }
}
