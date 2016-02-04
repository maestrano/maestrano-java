package com.maestrano.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class MnoPropertiesHelper {

	private static final String DEFAULT_CONFIG_PROPERTIES_PATH = "/com/maestrano/default.config.properties";
	private static final Properties DEFAULT_PROPERTIES = loadDefaultProperties();

	private static Properties loadDefaultProperties() {
		Properties properties = new Properties();
		InputStream is = MnoPropertiesHelper.class.getResourceAsStream(DEFAULT_CONFIG_PROPERTIES_PATH);
		try {
			properties.load(is);
		} catch (IOException cantHappen) {
			throw new RuntimeException(cantHappen);
		}
		return properties;
	}

	public static String getProperty(String key, Properties properties) {
		String property = properties.getProperty(key);
		if (isNullOrEmpty(property)) {
			property = DEFAULT_PROPERTIES.getProperty(key);
		}
		if (property == null) {
			return null;
		} else {
			return property.trim();
		}
	}

	public static boolean isNullOrEmpty(String property) {
		return property == null || property.trim().length() == 0;
	}

	public static boolean getBooleanProperty(String key, Properties properties) {
		return "true".equalsIgnoreCase(getProperty(key, properties));
	}
}
