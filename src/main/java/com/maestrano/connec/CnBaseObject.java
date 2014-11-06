package com.maestrano.connec;

import java.lang.reflect.Field;

public class CnBaseObject {
	
	protected void merge(Object obj) {
		Field[] fs = this.getClass().getDeclaredFields();
		
		for (Field f : fs) {
			try {
				f.set(this,f.get(obj));
			} catch (Exception e) {}
		}
	}
}
