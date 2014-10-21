package com.maestrano.testhelpers;

import java.util.Map;

import com.google.gson.Gson;
import com.maestrano.net.MnoHttpClient;

public class MnoHttpClientStub extends MnoHttpClient {
	private String responseStub;
	
	public String get(String url) {
		return this.responseStub;
	}

	public String getResponseStub() {
		return responseStub;
	}

	public void setResponseStub(String responseStub) {
		this.responseStub = responseStub;
	}
	
	public void setResponseStub(Map<String,String> responseStub) {
		Gson gson = new Gson();
		this.responseStub = gson.toJson(responseStub);
	}
}
