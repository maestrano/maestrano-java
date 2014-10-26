package com.maestrano;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

public class AppService {
	private static AppService instance;
	
	private String environment;
	private String host;
	
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
		this.configure(ConfigFile.getProperties());
	}
	
	/**
	 * Configure the service using a list of properties
	 * @param props Properties object
	 */
	public void configure(Properties props) {
		this.environment = props.getProperty("app.environment");
		this.host = props.getProperty("app.host");
	}
	
	/**
	 * The current application environment
	 * @return String either 'test' or 'production', 'test' by default
	 */
	public String getEnvironment() {
		if (environment == null || environment.isEmpty()) return "test";
		return environment;
	}
	public void setEnvironment(String environment) {
		this.environment = environment;
	}
	
	/**
	 * Return the application host. E.g: http://localhost:8080
	 * @return String the application host or 'locahost' by default
	 */
	public String getHost() {
		if (host == null || host.isEmpty()) return "localhost";
		return host;
	}
	public void setHost(String host) {
		this.host = host;
	}
	
	public Map<String,String> toMetadataHash() {
		Map<String,String> hash = new HashMap<String,String>();
		hash.put("host",getHost());
		
		return hash;
	}
}
