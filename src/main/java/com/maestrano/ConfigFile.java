package com.maestrano;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public final class ConfigFile {
	private static Properties prop;
	
	// Private Constructor
	private ConfigFile() {}
	
	public static Properties getProperties() {
		if (prop == null) {
			loadFromFile();
		}
		
		return prop;
	}
	
	private static void loadFromFile() {
		String filename = "config.properties";
		InputStream input = ConfigFile.class.getClassLoader().getResourceAsStream(filename);
		prop = new Properties();
		
		if(input != null){
			try {
				prop.load(input);
			} catch (IOException e) {}
		}
	}
}
