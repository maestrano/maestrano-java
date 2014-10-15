package com.maestrano;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ApiServiceTest {
	private Properties props = new Properties();
	private ApiService subject;

	@Before
	public void beforeEach() {
		props.setProperty("app.environment", "production");
		Maestrano.configure(props);
		subject = Maestrano.apiService();
	}
	
	@Test
	public void apiAuth_itReturnsTheRightCredentials() {
		String apiId = "someApiId";
		String apiKey = "someApiKey";
		props.setProperty("api.id", apiId);
		props.setProperty("api.key", apiKey);
		Maestrano.configure(props);
		
		assertEquals(apiId, subject.getId());
		assertEquals(apiKey, subject.getKey());
	}
	
	@Test
	public void getHost_itReturnsTheRightProductionValue() {
		assertEquals("https://maestrano.com", subject.getHost());
	}
	
	@Test
	public void getBase_itReturnsTheRightValue() {
		assertEquals("/api/v1", subject.getBase());
	}
	
	@Test
	public void getHost_itReturnsTheRightValue() {
		String host = "https://mysuperapp.com";
		props.setProperty("api.host", host);
		Maestrano.configure(props);
		
		assertEquals(host, subject.getHost());
	}
}
