package com.maestrano;

import java.util.Properties;

import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoPropertiesHelper;

/**
 * Service related to the DevPlatform environment
 */
public class DevPlatformService {
	private final String host;
	private final String apiPath;
	private final String environmentName;
	private final String apiKey;
	private final String apiSecret;

	public DevPlatformService(Properties properties) throws MnoConfigurationException {

		host = MnoPropertiesHelper.getPropertyOrEnvironment(properties, "dev-platform.host", "MNO_DEVPL_HOST", "https://developer.maestrano.com");
		apiPath = MnoPropertiesHelper.getPropertyOrEnvironment(properties, "dev-platform.apiPath", "MNO_DEVPL_API_PATH", "/api/config/v1");
		environmentName = MnoPropertiesHelper.getPropertyOrEnvironment(properties, "environment.name", "MNO_DEVPL_ENV_NAME");
		apiKey = MnoPropertiesHelper.getPropertyOrEnvironment(properties, "environment.apiKey", "MNO_DEVPL_ENV_KEY");
		apiSecret = MnoPropertiesHelper.getPropertyOrEnvironment(properties, "environment.apiSecret", "MNO_DEVPL_ENV_SECRET");
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

	@Override
	public String toString() {
		return "[host=" + host + ", apiPath=" + apiPath + ", environmentName=" + environmentName + ", apiKey=" + apiKey + ", apiSecret=" + apiSecret + "]";
	}

}
