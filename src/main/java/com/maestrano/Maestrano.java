package com.maestrano;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.maestrano.configuration.DevPlatform;
import com.maestrano.configuration.MarketplaceConfiguration;
import com.maestrano.configuration.Preset;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.BuildInformationHelper;
import com.maestrano.helpers.MnoPropertiesHelper;
import com.maestrano.net.DevPlatformClient;

/**
 * <p>
 * Entry point for Maestrano API SDK Configuration.
 * </p>
 * 
 * You can autoconfigure using the developer platform.</br>
 * 
 * Either by using environment variable:
 * 
 * <pre>
 * {@code
 * MNO_DEVPL_ENV_KEY=<your environment key>
 * MNO_DEVPL_ENV_SECRET=<your environment secret>
 * Maestrano.autoconfigure();
 * }
 * </pre>
 * 
 * Or using a properties files
 * 
 * <pre>
 * {code
 * 	Properties properties = new Properties();
 * 	properties.setProperty("dev-platform.host", "https://developer.maestrano.com");
 * 	properties.setProperty("dev-platform.api_path", "/api/config/v1");
 * 	properties.setProperty("environment.name", "<your environment nid>");
 * 	properties.setProperty("environment.apiKey", "<your environment key>");
 * 	properties.setProperty("environment.apiSecret", "<your environment secret>");
 * 	Maestrano.autoConfigure(properties);
 * }
 * </pre>
 * 
 * This class contains the different configurations of the services used by the SDK to interact with Maestrano API
 * 
 * <p>
 * If this is the first time you integrate with Maestrano, we recommend adopting a multi-tenant approach. All code samples in this documentation provide examples on how to handle multi-tenancy by
 * scoping method calls to a specific configuration marketplace. </br>
 * More information about multi-tenant integration can be found on our <a href="https://maestrano.atlassian.net/wiki/display/CONNECAPIV2/Multi-Tenant+Integration">Our Multi-Tenant Integration
 * Guide</a>
 * <p>
 * 
 */
public final class Maestrano {
	private static final Logger logger = LoggerFactory.getLogger(Maestrano.class);
	private static final Map<String, Preset> configurations = new LinkedHashMap<String, Preset>();

	/**
	 * Return the current Maestrano API version
	 * 
	 * @return String version
	 */
	public static String getVersion() {
		return BuildInformationHelper.getVersion();
	}

	/**
	 * Method to fetch configuration from the dev-platform, using environment variable.
	 * 
	 * The following variable must be set in the environment.
	 * <ul>
	 * <li>MNO_DEVPL_ENV_NAME</li>
	 * <li>MNO_DEVPL_ENV_KEY</li>
	 * <li>MNO_DEVPL_ENV_SECRET</li>
	 * </ul>
	 * 
	 * @throws MnoConfigurationException
	 */
	public static Map<String, Preset> autoConfigure() throws MnoConfigurationException {
		String host = MnoPropertiesHelper.readEnvironment("MNO_DEVPL_HOST", "https://developer.maestrano.com");
		String apiPath = MnoPropertiesHelper.readEnvironment("MNO_DEVPL_API_PATH", "/api/config/v1");
		String apiKey = MnoPropertiesHelper.readEnvironment("MNO_DEVPL_ENV_KEY");
		String apiSecret = MnoPropertiesHelper.readEnvironment("MNO_DEVPL_ENV_SECRET");
		return autoConfigure(host, apiPath, apiKey, apiSecret);
	}

	public static Map<String, Preset> autoConfigure(String host, String apiPath, String apiKey, String apiSecret) throws MnoConfigurationException {
		DevPlatform devplatformConfiguration = new DevPlatform(host, apiPath, apiKey, apiSecret);
		return autoconfigure(devplatformConfiguration);
	}

	/**
	 * Method to fetch configuration from the dev-platform, using a properties file.
	 * 
	 * @throws MnoConfigurationException
	 */
	public static Map<String, Preset> autoConfigure(String devPlatformPropertiesFile) throws MnoConfigurationException {
		Properties properties = loadProperties(devPlatformPropertiesFile);
		Properties trimProperties = MnoPropertiesHelper.trimProperties(properties);
		return autoConfigure(trimProperties);
	}

	/**
	 * Method to fetch configuration from the dev-platform, using a properties.
	 * 
	 * @return a Map <String, Maestrano> the different Maestrano configuration indexed by marketplace name
	 * @throws MnoConfigurationException
	 */
	public static Map<String, Preset> autoConfigure(Properties properties) throws MnoConfigurationException {
		DevPlatform devplatformConfiguration = new DevPlatform(properties);
		return autoconfigure(devplatformConfiguration);
	}

	private static Map<String, Preset> autoconfigure(DevPlatform devplatformConfiguration) throws MnoConfigurationException {
		DevPlatformClient client = new DevPlatformClient(devplatformConfiguration);
		List<MarketplaceConfiguration> marketplaceConfigurations = client.getMarketplaceConfigurations();
		for (MarketplaceConfiguration marketplaceConfiguration : marketplaceConfigurations) {
			String marketplace = marketplaceConfiguration.getName();
			reloadConfiguration(marketplace, marketplaceConfiguration.getProperties());
		}
		logger.debug("autoConfigure() found {}", configurations.keySet());
		return configurations;
	}

	/**
	 * reload the configuration for the given preset and overload the existing one if any
	 * 
	 * @throws MnoConfigurationException
	 */
	public static Preset reloadConfiguration(String marketplace, Properties props) throws MnoConfigurationException {
		Preset preset = new Preset(marketplace, props);
		configurations.put(marketplace, preset);
		return preset;
	}

	/**
	 * return the Maestrano Instance for the given preset
	 * 
	 * @param marketplace
	 * @return
	 * @throws MnoConfigurationException
	 *             if no instance was not configured
	 */
	public static Preset get(String marketplace) throws MnoConfigurationException {
		Preset maestrano = configurations.get(marketplace);
		if (maestrano == null) {
			throw new MnoConfigurationException("Maestrano was not configured for marketplace: " + marketplace + ". Maestrano.configure(" + marketplace + ") needs to have been called once.");
		}
		return maestrano;
	}

	/**
	 * 
	 * @return the configured marketplaces id
	 */
	public static Set<String> marketplaces() {
		return configurations.keySet();
	}

	/**
	 * 
	 * @return a unmodifiableMap of the Maestrano configuration indexed by preset Id.
	 */
	public static Map<String, Preset> getConfigurations() {
		return Collections.unmodifiableMap(configurations);
	}

	/**
	 * load Properties from a filePath, in the classPath or absolute
	 * 
	 * @param filePath
	 * @return
	 * @throws MnoConfigurationException
	 */
	public static Properties loadProperties(String filePath) throws MnoConfigurationException {
		Properties properties = new Properties();
		InputStream input = getInputStreamFromClassPathOrFile(filePath);
		try {
			properties.load(input);
		} catch (IOException e) {
			throw new MnoConfigurationException("Could not load properties file: " + filePath, e);
		} finally {
			IOUtils.closeQuietly(input);
		}
		return properties;
	}

	private static InputStream getInputStreamFromClassPathOrFile(String filename) throws MnoConfigurationException {
		InputStream input = Maestrano.class.getClassLoader().getResourceAsStream(filename);
		if (input != null) {
			return input;
		}
		// the file was not found in the classpath, let's try an absolute path
		File file = new File(filename);
		try {
			return new FileInputStream(file);
		} catch (FileNotFoundException e) {
			throw new MnoConfigurationException("Could not find file: " + filename, e);
		}
	}

}
