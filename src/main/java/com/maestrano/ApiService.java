package com.maestrano;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class ApiService {
	private static ApiService instance;
	
	// Map of Preset Name => AppServiceProperties
    private Map<String, ApiServiceProperties> presetsProperties = new HashMap<String, ApiServiceProperties> ();
    
    /**
     * Properties wrapper for a given preset
     */
    public static class ApiServiceProperties {
        protected String preset;
        private String id;
    	private String key;
    	private Boolean verifySslCerts;
    	private String accountBase;
    	private String accountHost;
    	private String connecHost;
    	private String connecBase;
    	
    	private ApiServiceProperties(String preset) {
            this.preset = preset;
        }
    }
	
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
        this.configure("default");
    }
    
    /**
     * Configure the service using the maestrano.properties
     * file available in the class path
     */
    public void configure(String preset) {
        this.configure(preset, ConfigFile.getProperties(preset));
    }
	
	/**
	 * Configure the service using a list of properties
	 * @param preset
	 * @param props Properties object
	 */
	public void configure(String preset, Properties props) {
	    ApiServiceProperties apiServiceProperties = new ApiServiceProperties(preset);

	    apiServiceProperties.id = props.getProperty("api.id");
	    apiServiceProperties.key = props.getProperty("api.key");
	    apiServiceProperties.accountBase = props.getProperty("api.accountBase");
	    apiServiceProperties.accountHost = props.getProperty("api.accountHost");
	    apiServiceProperties.connecBase = props.getProperty("api.connecBase");
	    apiServiceProperties.connecHost = props.getProperty("api.connecHost");

		if (props.getProperty("api.verifySslCerts") != null) {
		    apiServiceProperties.verifySslCerts = props.getProperty("api.verifySslCerts").equalsIgnoreCase("true");
		}

		this.presetsProperties.put(preset, apiServiceProperties);
	}
	
	/**
     * Return the Maestrano Application ID
     * @return String application id
     */
    public String getId() {
        return getId("default");
    }
    
    /**
	 * Return the Maestrano Application ID
	 * @return String application id
	 */
	public String getId(String preset) {
	    ApiServiceProperties apiServiceProperties = this.presetsProperties.get(preset);
		return apiServiceProperties.id;
	}
	
	/**
     * Return the Maestrano API Key
     * @return String api key
     */
    public String getKey() {
        return getKey("default");
    }
    
    /**
	 * Return the Maestrano API Key
	 * @return String api key
	 */
	public String getKey(String preset) {
	    ApiServiceProperties apiServiceProperties = this.presetsProperties.get(preset);
        return apiServiceProperties.key;
	}
	
	/**
     * Return the host used to make API calls
     * @return String host
     */
    public String getAccountHost() {
        return getAccountHost("default");
    }
    
    /**
     * Return the host used to make API calls
     * @return String host
     */
    public String getAccountHost(String preset) {
        ApiServiceProperties apiServiceProperties = this.presetsProperties.get(preset);
        if (apiServiceProperties == null || apiServiceProperties.accountHost == null || apiServiceProperties.accountHost.isEmpty()) {
            if (Maestrano.appService().getEnvironment().equals("production")) {
                return "https://maestrano.com";
            } else {
                return "http://api-sandbox.maestrano.io";
            }
        }
        return apiServiceProperties.accountHost;
    }
	
    /**
     * Return the base of the API endpoint
     * @return String base
     */
    public String getAccountBase() {
        return getAccountBase("default");
    }
    
    /**
	 * Return the base of the API endpoint
	 * @return String base
	 */
	public String getAccountBase(String preset) {
        ApiServiceProperties apiServiceProperties = this.presetsProperties.get(preset);
		if (apiServiceProperties == null || apiServiceProperties.accountBase == null || apiServiceProperties.accountBase.isEmpty()) return "/api/v1/account";
		return apiServiceProperties.accountBase;
	}
	
	/**
     * Return the host used to make API calls on Connec!
     * @return String host
     */
    public String getConnecHost() {
        return getConnecHost("default");
    }
    
    /**
     * Return the host used to make API calls on Connec!
     * @return String host
     */
    public String getConnecHost(String preset) {
        ApiServiceProperties apiServiceProperties = this.presetsProperties.get(preset);
        if (apiServiceProperties == null || apiServiceProperties.connecHost == null || apiServiceProperties.connecHost.isEmpty()) {
            if (Maestrano.appService().getEnvironment().equals("production")) {
                return "https://api-connec.maestrano.com";
            } else {
                return "http://api-sandbox.maestrano.io";
            }
        }
        return apiServiceProperties.connecHost;
    }
	
    /**
     * Return the base of the API endpoint
     * @return String base
     */
    public String getConnecBase() {
        return getConnecBase("default");
    }
    
	/**
	 * Return the base of the API endpoint
	 * @return String base
	 */
	public String getConnecBase(String preset) {
	    ApiServiceProperties apiServiceProperties = this.presetsProperties.get(preset);
		if (apiServiceProperties == null || apiServiceProperties.connecBase == null || apiServiceProperties.connecBase.isEmpty()) {
			if (Maestrano.appService().getEnvironment().equals("production")) {
				return "/api/v2";
			} else {
				return "/connec/api/v2";
			}
		}
		
		return apiServiceProperties.connecBase;
	}
	
	/**
     * Check whether to verify the SSL certificate or not
     * during API calls. False by default.
     * @return Boolean whether to verify the certificate or not
     */
    public Boolean getVerifySslCerts() {
        return getVerifySslCerts("default");
    }
	
	/**
	 * Check whether to verify the SSL certificate or not
	 * during API calls. False by default.
	 * @return Boolean whether to verify the certificate or not
	 */
	public Boolean getVerifySslCerts(String preset) {
	    ApiServiceProperties apiServiceProperties = this.presetsProperties.get(preset);
		if (apiServiceProperties == null || apiServiceProperties.verifySslCerts == null) return false;
		return apiServiceProperties.verifySslCerts;
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
        return toMetadataHash("default");
    }
	
	public Map<String,String> toMetadataHash(String preset) {
        Map<String,String> hash = new HashMap<String,String>();
        hash.put("id", getId(preset));
        hash.put("lang", getLang());
        hash.put("version", getVersion());
        hash.put("lang_version", getLangVersion());
        
        return hash;
    }
}
