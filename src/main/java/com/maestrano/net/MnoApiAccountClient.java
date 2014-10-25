package com.maestrano.net;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maestrano.Maestrano;
import com.maestrano.account.MnoBill;
import com.maestrano.testhelpers.MnoHttpClientStub;

public class MnoApiAccountClient {
	
	public MnoApiAccountClient() {}
	
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
	
	public static <T> List<T> all(Class<T> clazz) throws IOException {
		return all(clazz,null,MnoHttpClient.getAuthenticatedClient());
	}
	
	public static <T> List<T> all(Class<T> clazz, Map<String,String> params) throws IOException {
		return all(clazz,params,MnoHttpClient.getAuthenticatedClient());
	}
	
	public static <T> List<T> all(Class<T> clazz, Map<String,String> params, MnoHttpClient httpClient) throws IOException {
		Gson gson = new Gson();
		String jsonBody = httpClient.get(getCollectionUrl(clazz), params);
		
		Type parsingType = new TypeToken<MnoApiAccountResponse<List<MnoBill>>>(){}.getType();
		MnoApiAccountResponse<List<T>> resp = gson.fromJson(jsonBody, parsingType);
		
		return resp.getData();
	}
	
	public static <T> T create(Class<T> clazz, Map<String,Object> hash) throws IOException {
		return create(clazz,hash,MnoHttpClient.getAuthenticatedClient());
	}
	
	public static <T> T create(Class<T> clazz, Map<String,Object> hash, MnoHttpClient httpClient) throws IOException {
		Gson gson = new Gson();
		String jsonBody = httpClient.post(getCollectionUrl(clazz), gson.toJson(hash));
		
		Type parsingType = new TypeToken<MnoApiAccountResponse<MnoBill>>(){}.getType();
		MnoApiAccountResponse<T> resp = gson.fromJson(jsonBody, parsingType);
		
		return resp.getData();
	}
	
	public static <T> T retrieve(Class<T> clazz, String entityId) throws IOException {
		return retrieve(clazz,entityId,MnoHttpClient.getAuthenticatedClient());
	}
	
	public static <T> T retrieve(Class<T> clazz, String entityId, MnoHttpClient httpClient) throws IOException {
		Gson gson = new Gson();
		String jsonBody = httpClient.get(getInstanceUrl(clazz,entityId));
		
		Type parsingType = new TypeToken<MnoApiAccountResponse<MnoBill>>(){}.getType();
		MnoApiAccountResponse<T> resp = gson.fromJson(jsonBody, parsingType);
		
		return resp.getData();
	}
	
	public static <T> T update(Class<T> clazz, String entityId, Map<String,Object> hash) throws IOException {
		return update(clazz,entityId,hash,MnoHttpClient.getAuthenticatedClient());
	}
	
	public static <T> T update(Class<T> clazz, String entityId, Map<String,Object> hash, MnoHttpClient httpClient) throws IOException {
		Gson gson = new Gson();
		String jsonBody = httpClient.put(getInstanceUrl(clazz,entityId),gson.toJson(hash));
		
		Type parsingType = new TypeToken<MnoApiAccountResponse<MnoBill>>(){}.getType();
		MnoApiAccountResponse<T> resp = gson.fromJson(jsonBody, parsingType);
		
		return resp.getData();
	}
	
	public static <T> T delete(Class<T> clazz, String entityId) throws IOException {
		return delete(clazz,entityId,MnoHttpClient.getAuthenticatedClient());
	}
	
	public static <T> T delete(Class<T> clazz, String entityId, MnoHttpClient httpClient) throws IOException {
		Gson gson = new Gson();
		String jsonBody = httpClient.delete(getInstanceUrl(clazz,entityId));
		
		Type parsingType = new TypeToken<MnoApiAccountResponse<MnoBill>>(){}.getType();
		MnoApiAccountResponse<T> resp = gson.fromJson(jsonBody, parsingType);
		
		return resp.getData();
	}
}
