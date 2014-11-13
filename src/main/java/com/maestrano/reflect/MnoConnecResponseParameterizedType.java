package com.maestrano.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.maestrano.net.MnoApiConnecResponse;

public class MnoConnecResponseParameterizedType implements ParameterizedType {
	private Type type;

    public MnoConnecResponseParameterizedType(Type type) {
        this.type = type;
    }

    public Type[] getActualTypeArguments() {
        return new Type[] {type};
    }

    public Type getRawType() {
        return MnoApiConnecResponse.class;
    }

    public Type getOwnerType() {
        return null;
    }
}
