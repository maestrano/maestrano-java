package com.maestrano;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import com.maestrano.helpers.MnoPropertiesHelper;

/**
 * Maestrano API Service, related to all Maestrano API
 */
public class ApiService {
	private static final String PROD_API_HOST = "https://maestrano.com";
	private static final String TEST_API_HOST = "http://api-sandbox.maestrano.io";

	private static final String PROD_CONNEC_HOST = "https://api-connec.maestrano.com";


	private final String id;
	private final String key;
	private final boolean verifySslCerts;
	private final String accountBase;
	private final String host;
	private final String connecHost;
	private final String connecBase;

	// package private Constructor
	ApiService(AppService appService, Properties props) {
		this.id = MnoPropertiesHelper.getPropertyOrDefault(props, "api.id");
		this.key = MnoPropertiesHelper.getPropertyOrDefault(props, "api.key");
		this.accountBase = MnoPropertiesHelper.getPropertyOrDefault(props, "api.accountBase");
		this.host = getApiHost(appService, props);
		this.connecBase = getConnecBase(appService, props);
		this.connecHost = getConnecHost(appService, props);
		this.verifySslCerts = MnoPropertiesHelper.getBooleanProperty(props, "api.verifySslCerts");
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

	private static String getApiHost(AppService appService, Properties props) {
		String apiHost = MnoPropertiesHelper.getProperty(props, "api.host", "api.accountHost");
		if (!MnoPropertiesHelper.isNullOrEmpty(apiHost)) {
			return apiHost;
		} else {
			if (appService.isProduction()) {
				return PROD_API_HOST;
			} else {
				return TEST_API_HOST;
			}
		}
	}

	/**
	 * Return the base of the API endpoint
	 * 
	 * @return String base
	 */
	public String getAccountBase() {
		return accountBase;
	}

	/**
	 * Return the host used to make API calls on Connec!
	 * 
	 * @return String host
	 */
	public String getConnecHost() {
		return connecHost;
	}

	private static String getConnecHost(AppService appService, Properties props) {
		String connecHost = MnoPropertiesHelper.getProperty(props, "connec.host", "api.connecHost");
		if (!MnoPropertiesHelper.isNullOrEmpty(connecHost)) {
			return connecHost;
		} else {
			if (appService.isProduction()) {
				return PROD_CONNEC_HOST;
			} else {
				return TEST_API_HOST;
			}
		}
	}

	/**
	 * Return the base of the API endpoint
	 * 
	 * @return String base
	 */
	public String getConnecBase() {
		return connecBase;
	}

	/**
	 * Return the base of the API endpoint
	 * 
	 * @return String base
	 */
	private static String getConnecBase(AppService appService, Properties props) {
		String connecBase = MnoPropertiesHelper.getProperty(props, "connec.base", "api.connecBase");
		if (!MnoPropertiesHelper.isNullOrEmpty(connecBase)) {
			return connecBase;
		} else {
			if (appService.isProduction()) {
				return "/api/v2";
			} else {
				return "/connec/api/v2";
			}
		}
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
		return System.getProperty("java.version");
	}

	public String getVersion() {
		return Maestrano.getVersion();
	}

	public Map<String, String> toMetadataHash() {
		Map<String, String> hash = new HashMap<String, String>();
		hash.put("id", getId());
		hash.put("lang", getLang());
		hash.put("version", getVersion());
		hash.put("lang_version", getLangVersion());
		
		hash.put("connec.host", getConnecHost());

		return hash;
	}
}
