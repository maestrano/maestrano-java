package com.maestrano.testhelpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import com.maestrano.helpers.MnoPropertiesHelper;

public class DefaultPropertiesHelper {

	private static final String DEFAULT_CONFIG_PROPERTIES_PATH = "/com/maestrano/default-config.properties";

	public static Properties loadDefaultProperties() {
		Properties properties = new Properties();
		InputStream is = MnoPropertiesHelper.class.getResourceAsStream(DEFAULT_CONFIG_PROPERTIES_PATH);
		try {
			properties.load(is);
		} catch (IOException cantHappen) {
			throw new RuntimeException(cantHappen);
		}
		return properties;
	}
}
