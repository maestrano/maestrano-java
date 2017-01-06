package com.maestrano;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.maestrano.helpers.MnoPropertiesHelper;

/**
 * Maestrano Connec Service, related to all Connec API
 */
public class ConnecService {

	private final String host;
	private final String basePath;

	// package private Constructor
	ConnecService(AppService appService, Properties props) {
		this.basePath = MnoPropertiesHelper.getPropertyOrDefault(props, "connec.basePath", "connec.base", "api.connecBase");
		this.host = MnoPropertiesHelper.getPropertyOrDefault(props, "connec.host");
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
