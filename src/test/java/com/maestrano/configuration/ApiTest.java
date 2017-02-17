package com.maestrano.configuration;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.exception.MnoException;
import com.maestrano.testhelpers.DefaultPropertiesHelper;

public class ApiTest {
	private Api subject;
	private Properties properties;

	@Before
	public void beforeEach() throws MnoConfigurationException {
		properties = DefaultPropertiesHelper.loadDefaultProperties();
		properties.setProperty("environment", "production");
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.key", "somekey");
		Preset preset = new Preset("test", properties);
		subject = preset.getApi();
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
		Properties properties = DefaultPropertiesHelper.loadDefaultProperties();
		String apiId = "someApiId";
		String apiKey = "someApiKey";
		properties.setProperty("api.id", apiId);
		properties.setProperty("api.key", apiKey);
		Preset preset = new Preset("test", properties);
		Api api = preset.getApi();
		assertEquals(apiId, api.getId());
		assertEquals(apiKey, api.getKey());
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
		Properties properties = DefaultPropertiesHelper.loadDefaultProperties();
		String host = "https://mysuperapp.com";
		properties.setProperty("api.host", host);
		Preset preset = new Preset("test", properties);
		Api api = preset.getApi();
		assertEquals(host, api.getHost());
	}

	@Test
	public void toMetadataHash_itReturnsTheRightValue() {
		Map<String, String> hash = subject.toMetadataHash();
		assertEquals(properties.getProperty("api.id"), hash.get("id"));
		assertEquals(subject.getLang(), hash.get("lang"));
		assertEquals(subject.getVersion(), hash.get("version"));
		assertEquals(subject.getLangVersion(), hash.get("lang_version"));
		assertEquals(subject.getHost(), hash.get("host"));
		assertEquals(subject.getBase(), hash.get("base"));
	}
}
