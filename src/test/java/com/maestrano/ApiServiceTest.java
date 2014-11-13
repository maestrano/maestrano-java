package com.maestrano;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ApiServiceTest {
	private Properties props = new Properties();
	private ApiService subject;

	@Before
	public void beforeEach() {
		props.setProperty("app.environment", "production");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		Maestrano.configure(props);
		subject = Maestrano.apiService();
	}
	
	@Test
	public void getLang_itReturnsTheRightValue() {
		assertEquals("Java",subject.getLang());
	}
	
	@Test
	public void getLangVersion_itReturnsTheRightValue() {
		assertEquals(System.getProperty("java.version"),subject.getLangVersion());
	}
	
	@Test
	public void getVersion() {
		assertEquals(Maestrano.getVersion(),subject.getVersion());
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
	public void getAccountHost_itReturnsTheRightProductionValue() {
		assertEquals("https://maestrano.com", subject.getAccountHost());
	}
	
	@Test
	public void getConnecHost_itReturnsTheRightProductionValue() {
		assertEquals("https://connec.maestrano.com",subject.getConnecHost());
	}
	
	@Test
	public void getAccountBase_itReturnsTheRightValue() {
		assertEquals("/api/v1/account", subject.getAccountBase());
	}
	
	@Test
	public void getConnecBase_itReturnsTheRightValue() {
		assertEquals("/api/v2", subject.getConnecBase());
	}
	
	@Test
	public void getAccountHost_itReturnsTheRightValue() {
		String host = "https://mysuperapp.com";
		props.setProperty("api.accountHost", host);
		Maestrano.configure(props);
		
		assertEquals(host, subject.getAccountHost());
	}
	
	@Test
	public void toMetadataHash_itReturnsTheRightValue() {
		Map<String,String> hash = subject.toMetadataHash();
		assertEquals(props.getProperty("api.id"), hash.get("id"));
		assertEquals(subject.getLang(), hash.get("lang"));
		assertEquals(subject.getVersion(), hash.get("version"));
		assertEquals(subject.getLangVersion(), hash.get("lang_version"));
	}
}
