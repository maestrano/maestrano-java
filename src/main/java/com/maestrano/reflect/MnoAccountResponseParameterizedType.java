package com.maestrano.reflect;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

import com.maestrano.net.MnoAccountResponse;

public class MnoAccountResponseParameterizedType implements ParameterizedType {
	private Type type;

    public MnoAccountResponseParameterizedType(Type type) {
        this.type = type;
    }

    public Type[] getActualTypeArguments() {
        return new Type[] {type};
    }

    public Type getRawType() {
        return MnoAccountResponse.class;
    }

    public Type getOwnerType() {
        return null;
    }
}
