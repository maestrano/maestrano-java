package com.maestrano.net;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maestrano.Maestrano;
import com.maestrano.account.MnoBill;

public class MnoApiAccountResource {
	
	public MnoApiAccountResource() {}
	
	public static String getEntityName(Class<?> clazz) {
		return clazz.getSimpleName()
				.replaceAll("([a-z])([A-Z])","$1_$2")
				.toLowerCase()
				.replaceFirst("^mno_", "");
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

	public static <T> List<T> all(Class<T> clazz, Map<String,String> params, MnoHttpClient httpClient) throws IOException {
		Gson gson = new Gson();
		String jsonBody = httpClient.get(getCollectionUrl(clazz), params);
		
		Type parsingType = new TypeToken<MnoApiAccountResponse<List<MnoBill>>>(){}.getType();
		MnoApiAccountResponse<List<T>> resp = gson.fromJson(jsonBody, parsingType);
		
		return resp.getData();
	}
}
