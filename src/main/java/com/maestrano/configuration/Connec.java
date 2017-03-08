package com.maestrano.configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoPropertiesHelper;

/**
 * Maestrano Connec Configuration, related to all Connec API
 */
public class Connec {

	private final String host;
	private final String basePath;

	// package private Constructor
	Connec(App appService, Properties props) throws MnoConfigurationException {
		this.basePath = MnoPropertiesHelper.getProperty(props, "connec.basePath");
		this.host = MnoPropertiesHelper.getProperty(props, "connec.host");
	}

	/**
	 * Return the host used to make API calls on Connec!
	 * 
	 * @return String host
	 */
	public String getHost() {
		return host;
	}

	/**
	 * Return the base of the API endpoint
	 * 
	 * @return String base
	 */
	public String getBasePath() {
		return basePath;
	}

	public Map<String, String> toMetadataHash() {
		Map<String, String> hash = new LinkedHashMap<String, String>();
		hash.put("host", getHost());
		hash.put("base_path", getBasePath());
		return hash;
	}
}
