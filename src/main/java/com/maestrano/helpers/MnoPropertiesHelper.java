package com.maestrano.helpers;

import java.util.Collection;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Properties;
import java.util.Set;

import com.google.common.base.CaseFormat;
import com.maestrano.exception.MnoConfigurationException;

public class MnoPropertiesHelper {

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
	 * @return the property with the specified key, if null tries the legacy keys, if still null, take the DEFAULT_PROPERTIES value
	 */
	public static String getPropertyOrDefault(Properties properties, String key, String defaultValue) {
		String property = properties.getProperty(key);
		if (isNullOrEmpty(property)) {
			return defaultValue;
		}
		return property;
	}

	public static String getProperty(Properties properties, String key) throws MnoConfigurationException {
		String property = properties.getProperty(key);
		if (isNullOrEmpty(property)) {
			throw new MnoConfigurationException("Could not find properties: " + key);
		}
		return property;
	}

	/**
	 * Return the value from the properties, and if not found try to find in the System environment variable, if not found throws a MnoConfigurationException
	 */
	public static String readEnvironment(String environmentName) throws MnoConfigurationException {
		String property = System.getenv(environmentName);
		if (isNullOrEmpty(property)) {
			throw new MnoConfigurationException("Could not environment variable: " + environmentName);
		}
		return property;
	}

	/**
	 * Read from the System environment variable, if not found throws a MnoConfigurationException
	 */
	public static String readEnvironment(String environmentName, String defaultValue) {
		String property = System.getenv(environmentName);
		if (isNullOrEmpty(property)) {
			return defaultValue;
		}
		return property;
	}

	public static boolean isNullOrEmpty(String property) {
		return property == null || property.length() == 0;
	}

	public static boolean getBooleanProperty(Properties properties, String key) throws MnoConfigurationException {
		return "true".equalsIgnoreCase(getProperty(properties, key));
	}

	public static boolean getBooleanProperty(Properties properties, String key, String defaultValue) {
		return "true".equalsIgnoreCase(getPropertyOrDefault(properties, key, defaultValue));
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
			// Convert the a_property_value to aPropertyValue
			String propertyKey = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, prefix);
			String value = o == null ? "" : o.toString();
			properties.setProperty(propertyKey, value);
		}
	}

}
