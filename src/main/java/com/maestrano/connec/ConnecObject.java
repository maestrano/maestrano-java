package com.maestrano.connec;

import java.lang.reflect.Field;

public class ConnecObject {
	
	/**
	 * Merge a provided object into the current object
	 * @param obj object to merge from
	 */
	protected void merge(Object obj) {
		Class<?> klass = this.getClass();
		
		while (klass != null) {
			Field[] fs = klass.getDeclaredFields();
			
			for (Field f : fs) {
				try {
					f.set(this,f.get(obj));
				} catch (Exception e) {}
			}
			
			klass = klass.getSuperclass();
		}
		
	}
}
