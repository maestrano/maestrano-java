package com.maestrano.testhelpers;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maestrano.exception.ApiException;
import com.maestrano.json.DateSerializer;
import com.maestrano.net.MnoHttpClient;

public class MnoHttpClientStub extends MnoHttpClient {
	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).create();

	private String defaultResponseStub;
	private final Map<String, String> responseStubs;
	private final Map<String, ApiException> exceptionsStubs;

	public MnoHttpClientStub() {
		responseStubs = new HashMap<String, String>();
		exceptionsStubs = new HashMap<String, ApiException>();
		defaultResponseStub = null;
	}

	public String get(String url) throws ApiException {
		return getResponseStub(url, null, null);
	}

	public <V> String get(String url, Map<String, V> params) throws ApiException {
		return getResponseStub(url, params, null);
	}

	public String put(String url, String payload) throws ApiException {
		return getResponseStub(url, null, payload);
	}

	public String post(String url, String payload) throws ApiException {
		return getResponseStub(url, null, payload);
	}

	public String delete(String url) throws ApiException {
		return getResponseStub(url, null, null);
	}

	public String getResponseStub() {
		return defaultResponseStub;
	}

	public <V> String getResponseStub(String url, Map<String, V> params, String payload) throws ApiException {

		String keyStr = url;
		String paramsStr = stringifyHash(params);

		if (paramsStr != null) {
			keyStr += "?" + paramsStr;
		}

		if (payload != null) {
			keyStr += "@@payload@@" + payload;
		}

		if (this.exceptionsStubs.get(keyStr) != null) {
			throw exceptionsStubs.get(keyStr);
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
		setResponseStub(responseStub, url, null, null);
	}

	public void setResponseStub(String responseStub, String url, Map<String, String> params) {
		setResponseStub(responseStub, url, params, null);
	}

	public void setResponseStub(String responseStub, String url, Map<String, String> params, String payload) {
		String keyStr = url;
		String paramsStr = stringifyHash(params);

		if (paramsStr != null) {
			keyStr += "?" + paramsStr;
		}

		if (payload != null) {
			keyStr += "@@payload@@" + payload;
		}

		this.responseStubs.put(keyStr, responseStub);
	}

	public void setResponseStub(Map<String, String> responseStub) {
		Gson gson = new Gson();
		this.defaultResponseStub = gson.toJson(responseStub);
	}

	/**
	 * The MnoHttpClientStub will throw an exception when the url is being called
	 * 
	 * @param url
	 */
	public void setExceptionStub(ApiException exception, String url) {
		this.exceptionsStubs.put(url, exception);
	}

	private static <V> String stringifyHash(Map<String, V> hash) {
		if (hash == null || hash.isEmpty())
			return null;

		String stringified = "";
		for (Map.Entry<String, V> param : hash.entrySet()) {
			String key = param.getKey();
			String val = GSON.toJson(param.getValue());
			stringified += "&" + key + "=" + val;
		}

		return stringified;
	}
}
