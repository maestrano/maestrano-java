package com.maestrano.configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoPropertiesHelper;

/**
 * Configuration related to the Application environment
 *
 */
public class App {
	private final String host;

	// package private Constructor
	App(Properties props) throws MnoConfigurationException {
		this.host = MnoPropertiesHelper.getProperty(props, "app.host");
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
