package com.maestrano;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AppService {
	private static AppService instance;
	
	// Map of Preset Name => AppServiceProperties
	private Map<String, AppServiceProperties> presetsProperties = new HashMap<String, AppServiceProperties> ();
	
	/**
	 * Properties wrapper for a given preset
	 */
	public static class AppServiceProperties {
	    protected String preset;
	    protected String environment;
    	protected String host;
    	
    	private AppServiceProperties(String preset) {
    	    this.preset = preset;
    	}
	}
	
	// Private Constructor
	private AppService() {}
	
	/**
	 * Return the service singleton
	 * @return AppService singleton
	 */
	public static AppService getInstance() {
		if (instance == null) {
			instance = new AppService();
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
     * Configure the service using the specified properties file and preset
     * @param preset
     * @param filename
     */
    public void configure(String preset, String filename) {
        this.configure(preset, ConfigFile.getProperties(preset, filename));
    }
	
	/**
	 * Configure the service using a list of properties
	 * @param preset Configuration preset name
	 * @param props Properties object
	 */
	public void configure(String preset, Properties props) {
	    AppServiceProperties appServiceProperties = new AppServiceProperties(preset);
	    appServiceProperties.environment = props.getProperty("app.environment");
	    appServiceProperties.host = props.getProperty("app.host");
	    this.presetsProperties.put(preset, appServiceProperties);
	}
	
	/**
     * The current application environment
     * @return String either 'test' or 'production', 'test' by default
     */
    public String getEnvironment() {
        return getEnvironment("default");
    }
    /**
	 * The current application environment
	 * @return String either 'test' or 'production', 'test' by default
	 */
	public String getEnvironment(String preset) {
	    AppServiceProperties appServiceProperties = this.presetsProperties.get(preset);
	    
		if (appServiceProperties == null || appServiceProperties.environment == null || appServiceProperties.environment.isEmpty()) return "test";
		return appServiceProperties.environment;
	}
	
	/**
     * Return the application host. E.g: http://localhost:8080
     * @return String the application host or 'localhost' by default
     */
    public String getHost() {
        return getHost("default");
    }
    
    /**
	 * Return the application host. E.g: http://localhost:8080
	 * @return String the application host or 'localhost' by default
	 */
	public String getHost(String preset) {
	    AppServiceProperties appServiceProperties = this.presetsProperties.get(preset);
	    
		if (appServiceProperties == null || appServiceProperties.host == null || appServiceProperties.host.isEmpty()) return "localhost";
		return appServiceProperties.host;
	}
	
	public Map<String,String> toMetadataHash() {       
        return toMetadataHash("default");
    }
	
	public Map<String,String> toMetadataHash(String preset) {
		Map<String,String> hash = new HashMap<String,String>();
		hash.put("host", getHost(preset));
		
		return hash;
	}
}
