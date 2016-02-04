package com.maestrano;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.maestrano.helpers.MnoPropertiesHelper;

/**
 * Service related to the Application environment
 *
 */
public class AppService {
	private final String environment;
	private final String host;

	// package private Constructor
	AppService(Properties props) {
		this.environment = MnoPropertiesHelper.getProperty("app.environment", props);
		this.host = MnoPropertiesHelper.getProperty("app.host", props);
	}

	/**
	 * The current application environment
	 * 
	 * @return String either 'test' or 'production', 'test' by default
	 */
	public String getEnvironment() {
		return this.environment;
	}

	/**
	 * @return true if environment is production
	 */
	public boolean isProduction() {
		return "production".equals(this.environment);
	}

	/**
	 * Return the application host. E.g: http://localhost:8080
	 * 
	 * @return String the application host or 'localhost' by default
	 */
	public String getHost() {
		return this.host;
	}

	public Map<String, String> toMetadataHash() {
		Map<String, String> hash = new HashMap<String, String>();
		hash.put("host", getHost());

		return hash;
	}
}
