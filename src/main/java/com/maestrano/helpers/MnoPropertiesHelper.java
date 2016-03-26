package com.maestrano.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Iterator;
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
		return trimProperties(properties);
	}
	/**
	 * @return a properties where all the property valuegetPropertys are trimmed
	 */
	public static Properties trimProperties(Properties input) {
		Properties output = new Properties();
		for (String key : input.stringPropertyNames()) {
			output.put(key, input.getProperty(key).trim());
		}
		return output;
	}

	/**
	 * @return the property with the specified key, if null tries the legacy keys
	 */
	public static String getProperty(Properties properties, String key, String... legacyKeys) {
		String property = properties.getProperty(key);
		Iterator<String> iterator = Arrays.asList(legacyKeys).iterator();
		while (isNullOrEmpty(property) && iterator.hasNext()) {
			property = properties.getProperty(iterator.next());
		}
		return property;
	}

	/**
	 * @return the property with the specified key, if null tries the legacy keys, if still null, take the DEFAULT_PROPERTIES value
	 */
	public static String getPropertyOrDefault(Properties properties, String key, String... legacyKeys) {
		String property = getProperty(properties, key, legacyKeys);
		if (isNullOrEmpty(property)) {
			property = DEFAULT_PROPERTIES.getProperty(key);
		}
		return property;
	}

	public static boolean isNullOrEmpty(String property) {
		return property == null || property.length() == 0;
	}

	public static boolean getBooleanProperty(Properties properties, String key, String... legacyKeys) {
		return "true".equalsIgnoreCase(getPropertyOrDefault(properties, key, legacyKeys));
	}
}
