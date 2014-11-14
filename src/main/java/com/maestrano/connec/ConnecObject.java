package com.maestrano.connec;

import java.lang.reflect.Field;

public class ConnecObject {
	
	protected void merge(Object obj) {
		Field[] fs = this.getClass().getDeclaredFields();
		
		for (Field f : fs) {
			System.out.println(f.toString());
			try {
				f.set(this,f.get(obj));
			} catch (Exception e) {}
		}
	}
}
