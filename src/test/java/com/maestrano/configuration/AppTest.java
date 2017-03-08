package com.maestrano.configuration;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.testhelpers.DefaultPropertiesHelper;

public class AppTest {
	private App subject;

	@Before
	public void beforeEach() throws MnoConfigurationException {

		Properties properties = DefaultPropertiesHelper.loadDefaultProperties();
		properties.setProperty("app.host", "https://mysuperapp.com");
		Preset preset = new Preset("test", properties);

		subject = preset.getApp();
	}

	@Test
	public void getHost_itReturnsTheRightValue() {
		assertEquals("https://mysuperapp.com", subject.getHost());
	}

	@Test
	public void toMetadataHash_itReturnsTheRightValue() {
		Map<String, String> hash = subject.toMetadataHash();
		assertEquals("https://mysuperapp.com", hash.get("host"));
	}

}
