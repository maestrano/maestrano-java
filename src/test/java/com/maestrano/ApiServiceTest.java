package com.maestrano;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.exception.MnoException;

public class ApiServiceTest {
	private Properties props = new Properties();
	private ApiService subject;

	@Before
	public void beforeEach() {
		props.setProperty("environment", "production");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		Maestrano maestrano = Maestrano.reloadConfiguration(props);
		subject = maestrano.apiService();
	}

	@Test
	public void getLang_itReturnsTheRightValue() {
		assertEquals("Java", subject.getLang());
	}

	@Test
	public void getLangVersion_itReturnsTheRightValue() {
		assertEquals(System.getProperty("java.version"), subject.getLangVersion());
	}

	@Test
	public void getVersion() {
		assertEquals(Maestrano.getVersion(), subject.getVersion());
	}

	@Test
	public void apiAuth_itReturnsTheRightCredentials() throws MnoException {
		String apiId = "someApiId";
		String apiKey = "someApiKey";
		props.setProperty("api.id", apiId);
		props.setProperty("api.key", apiKey);
		Maestrano maestrano = Maestrano.reloadConfiguration(props);
		ApiService apiService = maestrano.apiService();
		assertEquals(apiId, apiService.getId());
		assertEquals(apiKey, apiService.getKey());
	}

	@Test
	public void getHost_itReturnsTheRightProductionValue() {
		assertEquals("https://api-hub.maestrano.com", subject.getHost());
	}

	@Test
	public void getAccountBase_itReturnsTheRightValue() {
		assertEquals("/api/v1/", subject.getBase());
	}

	@Test
	public void getHost_itReturnsTheRightValue() throws MnoException {
		String host = "https://mysuperapp.com";
		props.setProperty("api.host", host);
		Maestrano maestrano = Maestrano.reloadConfiguration(props);
		ApiService apiService = maestrano.apiService();
		assertEquals(host, apiService.getHost());
	}

	@Test
	public void toMetadataHash_itReturnsTheRightValue() {
		Map<String, String> hash = subject.toMetadataHash();
		assertEquals(props.getProperty("api.id"), hash.get("id"));
		assertEquals(subject.getLang(), hash.get("lang"));
		assertEquals(subject.getVersion(), hash.get("version"));
		assertEquals(subject.getLangVersion(), hash.get("lang_version"));
		assertEquals(subject.getHost(), hash.get("host"));
		assertEquals(subject.getBase(), hash.get("base"));
	}
}
