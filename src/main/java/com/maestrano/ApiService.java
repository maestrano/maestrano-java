package com.maestrano;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.maestrano.helpers.MnoPropertiesHelper;

/**
 * Maestrano API Service, related to all Maestrano API
 */
public class ApiService {

	private final String id;
	private final String key;
	private final boolean verifySslCerts;
	private final String base;
	private final String host;
	private final String langVersion;

	// package private Constructor
	ApiService(Properties props) {
		this.id = MnoPropertiesHelper.getPropertyOrDefault(props, "api.id");
		this.key = MnoPropertiesHelper.getPropertyOrDefault(props, "api.key");
		this.base = MnoPropertiesHelper.getPropertyOrDefault(props, "api.base", "api.accountBase");
		this.host = MnoPropertiesHelper.getPropertyOrDefault(props, "api.host");
		this.verifySslCerts = MnoPropertiesHelper.getBooleanProperty(props, "api.verifySslCerts");
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

	/**
	 * Check whether to verify the SSL certificate or not during API calls. False by default.
	 * 
	 * @return boolean whether to verify the certificate or not
	 */
	public boolean getVerifySslCerts() {
		return verifySslCerts;
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
		hash.put("verify_ssl_certs", Boolean.toString(getVerifySslCerts()));
		hash.put("lang", getLang());
		hash.put("lang_version", getLangVersion());
		hash.put("host", getHost());
		hash.put("base", getBase());
		return hash;
	}
}
