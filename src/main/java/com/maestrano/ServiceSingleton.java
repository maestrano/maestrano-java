package com.maestrano;

import java.util.Properties;

class ServiceSingleton {
	private static ServiceSingleton instance;
	
	public static ServiceSingleton getInstance() {
		if (instance == null) {
			instance = new ServiceSingleton();
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
		
	}
}
