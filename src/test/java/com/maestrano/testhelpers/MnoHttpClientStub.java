package com.maestrano.testhelpers;

import java.io.IOException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.maestrano.net.MnoHttpClient;

public class MnoHttpClientStub extends MnoHttpClient {
	private String defaultResponseStub;
	private Map<String,String> responseStubs;
	
	public MnoHttpClientStub() {
		responseStubs = new HashMap<String,String>();
		defaultResponseStub = null;
	}
	
	public String get(String url) {
		return get(url,null);
	}
	
	public String get(String url, Map<String,String> params) {
		String keyStr = url;
		String paramsStr = stringifyHash(params);
		
		if (paramsStr != null) {
			keyStr += "?" + paramsStr;
		}
		
		if (this.responseStubs.get(keyStr) != null) {
			return this.responseStubs.get(keyStr);
		}
		
		return this.defaultResponseStub;
	}

	public String getResponseStub() {
		return defaultResponseStub;
	}

	public void setResponseStub(String responseStub) {
		this.defaultResponseStub = responseStub;
	}
	
	public void setResponseStub(String responseStub, String url) {
		setResponseStub(responseStub,url,null);
	}
	
	public void setResponseStub(String responseStub, String url, Map<String,String> params) {
		String keyStr = url;
		String paramsStr = stringifyHash(params);
		
		if (paramsStr != null) {
			keyStr += "?" + paramsStr;
		}
		
		this.responseStubs.put(keyStr, responseStub);
	}
	
	public void setResponseStub(Map<String,String> responseStub) {
		Gson gson = new Gson();
		this.defaultResponseStub = gson.toJson(responseStub);
	}
	
	private String stringifyHash(Map<String,String> hash) {
		if (hash == null || hash.isEmpty()) return null;
		
		String stringified = "";
		for (Map.Entry<String, String> param : hash.entrySet())
		{
			String key = param.getKey();
			String val = param.getValue();
			stringified += "&" + key + "=" + val;
		}
		
		return stringified;
	}
}
