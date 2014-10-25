package com.maestrano.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.maestrano.net.MnoApiAccountResponse;

public class MnoResponseParameterizedType implements ParameterizedType {
	private Type type;

    public MnoResponseParameterizedType(Type type) {
        this.type = type;
    }

    public Type[] getActualTypeArguments() {
        return new Type[] {type};
    }

    public Type getRawType() {
        return MnoApiAccountResponse.class;
    }

    public Type getOwnerType() {
        return null;
    }
}
