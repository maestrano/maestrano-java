package com.maestrano.net;

import com.maestrano.Maestrano;

public class MnoApiAccountResource {
	
	public MnoApiAccountResource() {}
	
	public static String getEntityName(Class<?> clazz) {
		return clazz.getSimpleName().replaceAll("([a-z])([A-Z])","$1_$2").toLowerCase();
	}
	
	public static String getEntitiesName(Class<?> clazz) {
		return getEntityName(clazz) + "s";
	}
	
	public static String getCollectionEndpoint(Class<?> clazz) {
		return Maestrano.apiService().getAccountBase() + "/" + getEntitiesName(clazz);
	}
	
	public static String getCollectionUrl(Class<?> clazz) {
		return Maestrano.apiService().getHost() + getCollectionEndpoint(clazz);
	}
	
	public static String getInstanceEndpoint(Class<?> clazz, String id) {
		return getCollectionEndpoint(clazz) + "/" + id;
	}
	
	public static String getInstanceUrl(Class<?> clazz, String id) {
		return Maestrano.apiService().getHost() + getInstanceEndpoint(clazz,id);
	}
}
