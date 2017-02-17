package com.maestrano.configuration;

import java.util.Properties;

import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoPropertiesHelper;

/**
 * Configuration related to the DevPlatform environment
 */
public class DevPlatform {
	private final String host;
	private final String apiPath;
	private final String apiKey;
	private final String apiSecret;

	public DevPlatform(String host, String apiPath, String apiKey, String apiSecret) {
		this.host = host;
		this.apiPath = apiPath;
		this.apiKey = apiKey;
		this.apiSecret = apiSecret;
	}

	public DevPlatform(Properties properties) throws MnoConfigurationException {

		this.host = MnoPropertiesHelper.getPropertyOrDefault(properties, "dev-platform.host", "https://developer.maestrano.com");
		this.apiPath = MnoPropertiesHelper.getPropertyOrDefault(properties, "dev-platform.apiPath", "/api/config/v1");
		this.apiKey = MnoPropertiesHelper.getProperty(properties, "environment.apiKey");
		this.apiSecret = MnoPropertiesHelper.getProperty(properties, "environment.apiSecret");
	}

	/**
	 * @return The host of the Dev Platform
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return The path of the API
	 */
	public String getApiPath() {
		return apiPath;
	}

	/**
	 * @return The API key
	 */
	public String getApiKey() {
		return apiKey;
	}

	/**
	 * 
	 * @return the API Secret
	 */
	public String getApiSecret() {
		return apiSecret;
	}

	@Override
	public String toString() {
		return "DevPlatform [host=" + host + ", apiPath=" + apiPath + ", apiKey=" + apiKey + ", apiSecret=" + apiSecret + "]";
	}

}
