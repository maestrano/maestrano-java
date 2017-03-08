package com.maestrano.configuration;

import java.util.Properties;

import org.junit.Test;

import com.maestrano.exception.MnoConfigurationException;

import junit.framework.Assert;

public class DevPlatformTest {
	@Test
	public void constructor_usingDefaultValues_itReturnTheRightValues() throws MnoConfigurationException {
		Properties properties = new Properties();
		properties.setProperty("environment.name", "name");
		properties.setProperty("environment.apiKey", "apiKey");
		properties.setProperty("environment.apiSecret", "apiSecret");

		DevPlatform subject = new DevPlatform(properties);
		Assert.assertEquals("https://developer.maestrano.com", subject.getHost());
		Assert.assertEquals("/api/config/v1", subject.getApiPath());
		Assert.assertEquals("apiKey", subject.getApiKey());
		Assert.assertEquals("apiSecret", subject.getApiSecret());
	}

	@Test
	public void constructor_itReturnTheRightValues() throws MnoConfigurationException {
		Properties properties = new Properties();
		properties.setProperty("dev-platform.host", "host");
		properties.setProperty("dev-platform.apiPath", "apiPath");
		properties.setProperty("environment.name", "name");
		properties.setProperty("environment.apiKey", "apiKey");
		properties.setProperty("environment.apiSecret", "apiSecret");

		DevPlatform subject = new DevPlatform(properties);
		Assert.assertEquals("host", subject.getHost());
		Assert.assertEquals("apiPath", subject.getApiPath());
		Assert.assertEquals("apiKey", subject.getApiKey());
		Assert.assertEquals("apiSecret", subject.getApiSecret());

	}

	@Test(expected = MnoConfigurationException.class)
	public void constructor_throwsAnErrorWhenPropertiesAreMissing() throws MnoConfigurationException {
		Properties properties = new Properties();
		properties.setProperty("dev-platform.host", "host");
		properties.setProperty("dev-platform.apiPath", "apiPath");
		properties.setProperty("environment.name", "name");
		properties.setProperty("environment.apiKey", "apiKey");
		new DevPlatform(properties);
	}

}
