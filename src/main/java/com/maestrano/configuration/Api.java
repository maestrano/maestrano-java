package com.maestrano.configuration;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.maestrano.Maestrano;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoPropertiesHelper;

/**
 * Maestrano API Configuration, related to all Maestrano API
 */
public class Api {
	
	private final String id;
	private final String key;
	private final String base;
	private final String host;
	private final String langVersion;

	// package private Constructor
	Api(Properties props) throws MnoConfigurationException {
		this.id = MnoPropertiesHelper.getProperty(props, "api.id");
		this.key = MnoPropertiesHelper.getProperty(props, "api.key");
		this.base = MnoPropertiesHelper.getProperty(props, "api.base");
		this.host = MnoPropertiesHelper.getProperty(props, "api.host");
		this.langVersion = System.getProperty("java.version");
	}

	/**
	 * Return the Maestrano Application ID
	 * 
	 * @return String application id
	 */
	public String getId() {
		return id;
	}

	/**
	 * Return the Maestrano API Key
	 * 
	 * @return String api key
	 */
	public String getKey() {
		return key;
	}

	/**
	 * Return the host used to make API calls
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
	public String getBase() {
		return base;
	}

	public String getLang() {
		return "Java";
	}

	public String getLangVersion() {
		return this.langVersion;
	}

	public String getVersion() {
		return Maestrano.getVersion();
	}

	public Map<String, String> toMetadataHash() {
		Map<String, String> hash = new LinkedHashMap<String, String>();
		hash.put("id", getId());
		hash.put("version", getVersion());
		hash.put("lang", getLang());
		hash.put("lang_version", getLangVersion());
		hash.put("host", getHost());
		hash.put("base", getBase());
		return hash;
	}
}
