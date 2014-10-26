package com.maestrano.testhelpers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maestrano.json.DateSerializer;
import com.maestrano.net.MnoHttpClient;

public class MnoHttpClientStub extends MnoHttpClient {
	public static final Gson GSON = new GsonBuilder()
		.registerTypeAdapter(Date.class, new DateSerializer())
		.create();
	
	private String defaultResponseStub;
	private Map<String,String> responseStubs;
	
	public MnoHttpClientStub() {
		responseStubs = new HashMap<String,String>();
		defaultResponseStub = null;
	}
	
	public String get(String url) {
		return getResponseStub(url,null,null);
	}
	
	public <V> String get(String url, Map<String,V> params) {
		return getResponseStub(url,params,null);
	}
	
	public String put(String url,String payload) {
		return getResponseStub(url,null,payload);
	}
	
	public String post(String url,String payload) {
		return getResponseStub(url,null,payload);
	}
	
	public String delete(String url) {
		return getResponseStub(url,null,null);
	}
	
	public String getResponseStub() {
		return defaultResponseStub;
	}
	
	public <V> String getResponseStub(String url, Map<String,V> params, String payload) {
		String keyStr = url;
		String paramsStr = stringifyHash(params);
		
		if (paramsStr != null) {
			keyStr += "?" + paramsStr;
		}
		
		if (payload !=null) {
			keyStr += "@@payload@@" + payload;
		}
		
		if (this.responseStubs.get(keyStr) != null) {
			return this.responseStubs.get(keyStr);
		}
		
		return this.defaultResponseStub;
	}

	

	public void setResponseStub(String responseStub) {
		this.defaultResponseStub = responseStub;
	}
	
	public void setResponseStub(String responseStub, String url) {
		setResponseStub(responseStub,url,null,null);
	}
	
	public void setResponseStub(String responseStub, String url, Map<String,String> params) {
		setResponseStub(responseStub,url,params,null);
	}
	
	public void setResponseStub(String responseStub, String url, Map<String,String> params, String payload) {
		String keyStr = url;
		String paramsStr = stringifyHash(params);
		
		if (paramsStr != null) {
			keyStr += "?" + paramsStr;
		}
		
		if (payload !=null) {
			keyStr += "@@payload@@" + payload;
		}
		
		this.responseStubs.put(keyStr, responseStub);
	}
	
	public void setResponseStub(Map<String,String> responseStub) {
		Gson gson = new Gson();
		this.defaultResponseStub = gson.toJson(responseStub);
	}
	
	private <V> String stringifyHash(Map<String,V> hash) {
		if (hash == null || hash.isEmpty()) return null;
		
		String stringified = "";
		for (Map.Entry<String, V> param : hash.entrySet())
		{
			String key = param.getKey();
			String val = GSON.toJson(param.getValue());
			stringified += "&" + key + "=" + val;
		}
		
		return stringified;
	}
}
