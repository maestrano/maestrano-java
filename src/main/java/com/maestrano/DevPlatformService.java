package com.maestrano;

import java.util.Properties;

import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoPropertiesHelper;

/**
 * Service related to the DevPlatform environment
 */
public class DevPlatformService {
	private final String host;
	private final String v1Path;
	private final String environmentName;
	private final String apiKey;
	private final String apiSecret;

	public DevPlatformService(Properties properties) throws MnoConfigurationException {
		host = MnoPropertiesHelper.getPropertyOrEnvironment(properties, "dev-platform.host", "DEVPL_HOST", "https://dev-platform.maestrano.com");
		v1Path = MnoPropertiesHelper.getPropertyOrEnvironment(properties, "dev-platform.v1Path", "DEVPL_V1_PATH", "/api/config/v1/marketplaces");
		environmentName = MnoPropertiesHelper.getPropertyOrEnvironment(properties, "environment.name", "ENVIRONMENT_NAME");
		apiKey = MnoPropertiesHelper.getPropertyOrEnvironment(properties, "environment.apiKey", "ENVIRONMENT_KEY");
		apiSecret = MnoPropertiesHelper.getPropertyOrEnvironment(properties, "environment.apiSecret", "ENVIRONMENT_SECRET");
	}

	/**
	 * @return The host of the Dev Platform
	 */
	public String getHost() {
		return host;
	}

	/**
	 * @return The path of the V1 API
	 */
	public String getV1Path() {
		return v1Path;
	}

	/**
	 * @return The name of your environment
	 */
	public String getEnvironmentName() {
		return environmentName;
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

}
