package com.maestrano.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.google.common.base.CaseFormat;
import com.maestrano.exception.MnoConfigurationException;

public class MnoPropertiesHelper {

	private static final String DEFAULT_CONFIG_PROPERTIES_PATH = "/com/maestrano/default.config.properties";
	private static final Properties DEFAULT_PROPERTIES = loadDefaultProperties();
	/**
	 * Class used to be stubbed for test cases
	 *
	 */
	public static class EnvironmentReader {
		public String getValue(String name) {
			return System.getenv(name);
		}
	}

	private static EnvironmentReader environmentReader = new EnvironmentReader();

	/**
	 * made public for testing only, allows the stubbing of System.getenv calls
	 */
	public static void setEnvironmentReader(EnvironmentReader environmentReader) {
		MnoPropertiesHelper.environmentReader = environmentReader;
	}

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
	 * @return a properties where all the property values are trimmed
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

	/**
	 * Return the value from the properties, and if not found try to find in the System environment variable, if not found throws a MnoConfigurationException
	 */
	public static String getPropertyOrEnvironment(Properties properties, String key, String environmentName) throws MnoConfigurationException {
		String property = getProperty(properties, key);
		if (isNullOrEmpty(property)) {
			property = environmentReader.getValue(environmentName);
		}
		if (isNullOrEmpty(property)) {
			throw new MnoConfigurationException("Could not find property: " + key + " or environment variable: " + environmentName);
		}
		return property;
	}

	/**
	 * Return the value from the properties, and if not found try to find in the System environment variable, if not found return the default value
	 */
	public static String getPropertyOrEnvironment(Properties properties, String key, String environmentName, String defaultValue) {
		String property = getProperty(properties, key);
		if (isNullOrEmpty(property)) {
			property = environmentReader.getValue(environmentName);
		}
		if (isNullOrEmpty(property)) {
			return defaultValue;
		}
		return property;
	}

	public static boolean isNullOrEmpty(String property) {
		return property == null || property.length() == 0;
	}

	public static boolean getBooleanProperty(Properties properties, String key, String... legacyKeys) {
		return "true".equalsIgnoreCase(getPropertyOrDefault(properties, key, legacyKeys));
	}

	public static Properties fromJson(Object object) {
		Properties properties = new Properties();
		loadJsonObject(properties, null, object);
		return properties;
	}

	private static void loadJsonObject(Properties properties, String prefix, Object o) {
		if (o instanceof Map) {
			@SuppressWarnings({ "unchecked" })
			Map<String, Object> map = (Map<String, Object>) o;
			Set<Entry<String, Object>> entrySet = map.entrySet();
			for (Entry<String, Object> entry : entrySet) {
				String key = (prefix == null ? "" : prefix + ".") + entry.getKey();
				loadJsonObject(properties, key, entry.getValue());
			}
		} else if (!(o instanceof Collection)) {
			//Convert the a_property_value to aPropertyValue
			String propertyKey = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, prefix);
			String value = o == null ? "" : o.toString();
			properties.setProperty(propertyKey, value);
		}
	}

}
