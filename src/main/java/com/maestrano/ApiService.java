package com.maestrano;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApiService {
	private static ApiService instance;
	
	private String id;
	private String key;
	private String base;
	private Boolean verifySslCerts;
	private String host;
	private String connecHost;
	private String connecBase;
	
	// Private Constructor
	private ApiService() {}
	
	/**
	 * Return the service singleton
	 * @return ApiService singleton
	 */
	public static ApiService getInstance() {
		if (instance == null) {
			instance = new ApiService();
			instance.configure();
		}
		return instance;
	}
	
	/**
	 * Configure the service using the maestrano.properties
	 * file available in the class path
	 */
	public void configure() {
		this.configure(ConfigFile.getProperties());
	}
	
	/**
	 * Configure the service using a list of properties
	 * @param props Properties object
	 */
	public void configure(Properties props) {
		this.id = props.getProperty("api.id");
		this.key = props.getProperty("api.key");
		this.base = props.getProperty("api.base");
		
		if (props.getProperty("api.verifySslCerts") != null) {
			this.verifySslCerts = props.getProperty("api.verifySslCerts").equalsIgnoreCase("true");
		}
		
		this.host = props.getProperty("api.host");
	}
	
	/**
	 * Return the Maestrano Application ID
	 * @return String application id
	 */
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	/**
	 * Return the Maestrano API Key
	 * @return String api key
	 */
	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}
	
	/**
	 * Return the host used to make API calls
	 * @return String host
	 */
	public String getHost() {
		if (host == null || host.isEmpty()) {
			if (Maestrano.appService().getEnvironment().equals("production")) {
				return "https://maestrano.com";
			} else {
				return "http://api-sandbox.maestrano.io";
			}
		}
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}
	
	/**
	 * Return the base of the API endpoint
	 * @return String base
	 */
	public String getBase() {
		if (base == null || base.isEmpty()) return "/api/v1";
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}
	
	/**
	 * Return the host used to make API calls on Connec!
	 * @return String host
	 */
	public String getConnecHost() {
		if (connecHost == null || connecHost.isEmpty()) {
			if (Maestrano.appService().getEnvironment().equals("production")) {
				return "https://connec.maestrano.com";
			} else {
				return "http://api-sandbox.maestrano.io";
			}
		}
		return connecHost;
	}

	public void setConnecHost(String connecHost) {
		this.connecHost = connecHost;
	}
	
	/**
	 * Return the base of the API endpoint
	 * @return String base
	 */
	public String getConnecBase() {
		if (connecBase == null || connecBase.isEmpty()) return "/api/v2";
		return connecBase;
	}

	public void setConnecBase(String connecBase) {
		this.connecBase = connecBase;
	}
	
	/**
	 * Return the base for the "Account" API
	 * @return String base
	 */
	public String getAccountBase() {
		return getBase() + "/account";
	}
	
	/**
	 * Check whether to verify the SSL certificate or not
	 * during API calls. False by default.
	 * @return Boolean whether to verify the certificate or not
	 */
	public Boolean getVerifySslCerts() {
		if (verifySslCerts == null) return false;
		return verifySslCerts;
	}

	public void setVerifySslCerts(Boolean verifySslCerts) {
		this.verifySslCerts = verifySslCerts;
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
	
	public Map<String,String> toMetadataHash() {
		Map<String,String> hash = new HashMap<String,String>();
		hash.put("id", getId());
		hash.put("lang", getLang());
		hash.put("version", getVersion());
		hash.put("lang_version", getLangVersion());
		
		return hash;
	}
	
	
}
