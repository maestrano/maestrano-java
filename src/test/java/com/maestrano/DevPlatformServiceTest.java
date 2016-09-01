package com.maestrano;

import java.util.Map;
import java.util.Properties;

import org.junit.Test;

import com.google.common.collect.ImmutableMap;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoPropertiesHelper;
import com.maestrano.helpers.MnoPropertiesHelper.EnvironmentReader;

import junit.framework.Assert;

public class DevPlatformServiceTest {
	@Test
	public void constructor_usingDefaultValues_itReturnTheRightValues() throws MnoConfigurationException {
		Properties properties = new Properties();
		properties.setProperty("environment.name", "name");
		properties.setProperty("environment.apiKey", "apiKey");
		properties.setProperty("environment.apiSecret", "apiSecret");

		DevPlatformService subject = new DevPlatformService(properties);
		Assert.assertEquals("https://dev-platform.maestrano.com", subject.getHost());
		Assert.assertEquals("/api/config/v1", subject.getApiPath());
		Assert.assertEquals("name", subject.getEnvironmentName());
		Assert.assertEquals("apiKey", subject.getApiKey());
		Assert.assertEquals("apiSecret", subject.getApiSecret());
	}

	@Test
	public void constructor_usingEnvironmentVariable_itReturnTheRightValues() throws MnoConfigurationException {
		Properties properties = new Properties();
		// This is to override the call to System.getEnv
		final Map<String, String> environmentValues = ImmutableMap.of("MNO_DEVPL_ENV_NAME", "theHost", "MNO_DEVPL_ENV_KEY", "theKey", "MNO_DEVPL_ENV_SECRET", "theSecret");
		MnoPropertiesHelper.setEnvironmentReader(new EnvironmentReader() {
			@Override
			public String getValue(String name) {
				return environmentValues.get(name);
			}
		});

		DevPlatformService subject = new DevPlatformService(properties);
		Assert.assertEquals("https://dev-platform.maestrano.com", subject.getHost());
		Assert.assertEquals("/api/config/v1", subject.getApiPath());
		Assert.assertEquals("theHost", subject.getEnvironmentName());
		Assert.assertEquals("theKey", subject.getApiKey());
		Assert.assertEquals("theSecret", subject.getApiSecret());

		// Reset environment Reader
		MnoPropertiesHelper.setEnvironmentReader(new EnvironmentReader());
	}

	@Test
	public void constructor_itReturnTheRightValues() throws MnoConfigurationException {
		Properties properties = new Properties();
		properties.setProperty("dev-platform.host", "host");
		properties.setProperty("dev-platform.apiPath", "apiPath");
		properties.setProperty("environment.name", "name");
		properties.setProperty("environment.apiKey", "apiKey");
		properties.setProperty("environment.apiSecret", "apiSecret");

		DevPlatformService subject = new DevPlatformService(properties);
		Assert.assertEquals("host", subject.getHost());
		Assert.assertEquals("apiPath", subject.getApiPath());
		Assert.assertEquals("name", subject.getEnvironmentName());
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
		new DevPlatformService(properties);
	}

}
