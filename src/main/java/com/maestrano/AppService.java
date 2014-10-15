package com.maestrano;

import java.util.Properties;

class AppService extends ServiceSingleton {
	private String environment;
	private String host;
	
	// Private Constructor
	private AppService() {}
	
	@Override
	public void configure(Properties props) {
		this.environment = props.getProperty("app.environment");
		this.host = props.getProperty("app.host");
	}
	
	/**
	 * The current application environment
	 * @return String either 'test' or 'production'
	 */
	public String getEnvironment() {
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
	
}
