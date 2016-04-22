package com.maestrano;

import java.util.LinkedHashMap;
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
		this.environment = MnoPropertiesHelper.getPropertyOrDefault(props, "environment", "app.environment");
		this.host = MnoPropertiesHelper.getPropertyOrDefault(props, "app.host");
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
		return "production".equals(this.environment) || "uat".equals(this.environment);
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
		Map<String, String> hash = new LinkedHashMap<String, String>();
		hash.put("host", getHost());

		return hash;
	}
}
