package com.maestrano;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.maestrano.configuration.Preset;
import com.maestrano.exception.MnoException;
import com.maestrano.testhelpers.DefaultPropertiesHelper;
import com.maestrano.testhelpers.HttpRequestStub;

public class PresetTest {

	private Properties properties;
	private Preset preset;

	@Before
	public void beforeEach() throws Exception {
		properties = DefaultPropertiesHelper.loadDefaultProperties();
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.key", "somekey");
		preset = new Preset("test", properties);
	}

	@Test
	public void authenticate_withCredentials_itReturnsTrueWithRightCredentials() throws MnoException {
		assertTrue(preset.authenticate(properties.getProperty("api.id"), properties.getProperty("api.key")));
	}

	@Test
	public void authenticate_withCredentials_itReturnsFalseWithWrongCredentials() throws MnoException {
		assertFalse(preset.authenticate(properties.getProperty("api.id"), properties.getProperty("api.key") + "aa"));
		assertFalse(preset.authenticate(properties.getProperty("api.id") + "aa", properties.getProperty("api.key")));
	}

	@Test
	public void authenticate_withRequest_itReturnsTrueWithRightBasicAuth() throws UnsupportedEncodingException, MnoException {
		HttpRequestStub request = new HttpRequestStub();
		String authStr = properties.getProperty("api.id") + ":" + properties.getProperty("api.key");
		authStr = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
		request.setHeader("Authorization", authStr);

		assertTrue(preset.authenticate(request));
	}

	@Test
	public void authenticate_withRequest_itReturnsFalseWithWrongBasicAuth() throws UnsupportedEncodingException, MnoException {
		HttpRequestStub request = new HttpRequestStub();
		String authStr = properties.getProperty("api.id") + ":" + properties.getProperty("api.key") + "aaa";
		authStr = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
		request.setHeader("Authorization", authStr);

		assertFalse(preset.authenticate(request));
	}

	@Test
	public void toMetadataHash_itReturnTheRightValue() {
		Map<String, Object> hash = new HashMap<String, Object>();
		hash.put("marketplace", preset.getMarketplace());
		hash.put("app", preset.getApp().toMetadataHash());
		hash.put("api", preset.getApi().toMetadataHash());
		hash.put("sso", preset.getSso().toMetadataHash());
		hash.put("connec", preset.getConnec().toMetadataHash());
		hash.put("webhook", preset.getWebhook().toMetadataHash());
		assertEquals(hash, preset.toMetadataHash());
	}

	@Test
	public void toMetadata_itReturnTheRightValue() throws IOException {
		InputStream inputStream = this.getClass().getResourceAsStream("/expected-metadata.json");
		String expected = IOUtils.toString(inputStream, StandardCharsets.UTF_8);
		expected = expected.replace("[JAVA_VERSION]", System.getProperty("java.version"));
		expected = expected.replace("[MAESTRANO_VERSION]", Maestrano.getVersion());
		assertEquals(expected, preset.toMetadata());
	}
}
