package com.maestrano;

import java.util.Map;
import java.util.Properties;

import org.junit.Ignore;
import org.junit.Test;

import com.maestrano.exception.MnoConfigurationException;

/**
 * Test to be used with a local development setup.
 *
 */
public class MaestranoIntegrationTest {

	@Ignore
	@Test
	public void autoconfigure() throws MnoConfigurationException {
		Properties properties = new Properties();
		properties.setProperty("dev-platform.host", "http://localhost:5000");
		properties.setProperty("environment.name", "MyEnvironment");
		properties.setProperty("environment.apiKey", "a349e248-b264-4a76-897d-8b6b1bee6458");
		properties.setProperty("environment.apiSecret", "iTErlebKaIA6TY6mOuG2Tw");

		Map<String, Maestrano> autoConfigure = Maestrano.autoConfigure(properties);
		System.out.println(autoConfigure);
	}
}
