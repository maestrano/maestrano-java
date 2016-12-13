package com.maestrano;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.exception.MnoException;
import com.maestrano.helpers.BuildInformationHelper;
import com.maestrano.helpers.MnoPropertiesHelper;
import com.maestrano.net.DevPlatformClient;

/**
 * <p>
 * Entry point for Maestrano API SDK Configuration.
 * </p>
 * 
 * You can configure maestrano using a properties file from the classpath or with an absolute path:</br>
 * 
 * <pre>
 * Maestrano.configure("myconfig.properties");
 * </pre>
 * 
 * <p>
 * 
 * This will configure Maestrano with the properties defined in the myconfig.properties file for the default configuration.
 * 
 * You can add configuration presets programmatically by adding sets of properties in your Maestrano configuration. These additional presets can then be specified when doing particular action, such as
 * initializing a Connec!™ client or triggering a SSO handshake. These presets are particularly useful if you are dealing with multiple Maestrano-style marketplaces (multi-enterprise integration).
 * </p>
 * <p>
 * If this is the first time you integrate with Maestrano, we recommend adopting a multi-tenant approach. All code samples in this documentation provide examples on how to handle multi-tenancy by
 * scoping method calls to a specific configuration preset. </br>
 * More information about multi-tenant integration can be found on our <a href="https://maestrano.atlassian.net/wiki/display/CONNECAPIV2/Multi-Tenant+Integration">Our Multi-Tenant Integration
 * Guide</a>
 * <p>
 * The {@link #configure()} methods needs to be called only once per preset. A {@link MnoConfigurationException} will be thrown if Maestrano was previously configured. If you need to reload the
 * configuration for a given preset, you may use the {@linkplain Maestrano#reloadConfiguration(Properties)} method.
 * 
 */
public final class Maestrano {
	private static final Logger logger = LoggerFactory.getLogger(Maestrano.class);
	public static final String DEFAULT = "default";
	private static final Map<String, Maestrano> instances = new LinkedHashMap<String, Maestrano>();

	private final String preset;
	private final AppService appService;
	private final ApiService apiService;
	private final SsoService ssoService;
	private final ConnecService connecService;
	private final WebhookService webhookService;

	// Private constructor
	private Maestrano(String preset, Properties props) {
		Properties trimmedProperties = MnoPropertiesHelper.trimProperties(props);
		this.preset = preset;
		this.appService = new AppService(trimmedProperties);
		this.apiService = new ApiService(trimmedProperties);
		this.connecService = new ConnecService(appService, props);
		this.ssoService = new SsoService(apiService, appService, trimmedProperties);
		this.webhookService = new WebhookService(trimmedProperties);
	}

	/**
	 * Return the current Maestrano API version
	 * 
	 * @return String version
	 */
	public static String getVersion() {
		return BuildInformationHelper.getVersion();
	}

	/**
	 * Configure Maestrano API using a "config.properties" file
	 * 
	 * @throws MnoConfigurationException
	 *             if an instance was already configured
	 */
	public static Maestrano configure() throws MnoConfigurationException {
		return configure(DEFAULT, "config.properties");
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
	public static Map<String, Maestrano> autoConfigure() throws MnoConfigurationException {
		Properties properties = new Properties();
		return autoConfigure(properties);
	}

	/**
	 * Method to fetch configuration from the dev-platform, using a properties file.
	 * 
	 * @throws MnoConfigurationException
	 */
	public static Map<String, Maestrano> autoConfigure(String devPlatformPropertiesFile) throws MnoConfigurationException {
		Properties properties = loadProperties(devPlatformPropertiesFile);
		Properties trimProperties = MnoPropertiesHelper.trimProperties(properties);
		return autoConfigure(trimProperties);
	}

	/**
	 * Method to fetch configuration from the dev-platform, using a properties.
	 * 
	 * @return a Map <Preset, MaestranoConfiguration> the preset being the marketplace name
	 * @throws MnoConfigurationException
	 */
	public static Map<String, Maestrano> autoConfigure(Properties properties) throws MnoConfigurationException {
		DevPlatformService devPlatformService = new DevPlatformService(properties);
		DevPlatformClient client = new DevPlatformClient(devPlatformService);
		List<MarketplaceConfiguration> marketplaceConfigurations = client.getMarketplaceConfigurations();
		Map<String, Maestrano> configurations = new LinkedHashMap<String, Maestrano>();
		for (MarketplaceConfiguration marketplaceConfiguration : marketplaceConfigurations) {
			String preset = marketplaceConfiguration.getName();
			Maestrano maestrano = reloadConfiguration(preset, marketplaceConfiguration.getProperties());
			configurations.put(preset, maestrano);
		}
		logger.debug("autoConfigure() found {}", configurations.keySet());
		return configurations;
	}

	/**
	 * Configure Maestrano API using a Properties file
	 * 
	 * @param String
	 *            filePath properties file path. In the classPath or absolute
	 * @throws MnoConfigurationException
	 *             if an instance was already configured
	 */
	public static Maestrano configure(String filePath) throws MnoConfigurationException {
		return configure(DEFAULT, filePath);
	}

	/**
	 * Configure Maestrano API using a Properties object
	 * 
	 * @param Properties
	 *            props
	 * @throws MnoConfigurationException
	 *             if an instance was already configured
	 */
	public static Maestrano configure(Properties props) throws MnoConfigurationException {
		return configure(DEFAULT, props);
	}

	/**
	 * Configure Maestrano API using a Properties file for the given preset
	 * 
	 * @param String
	 *            preset
	 * @param String
	 *            filePath properties file path. In the classPath or absolute
	 * @throws MnoConfigurationException
	 *             if an instance was already configured
	 */
	public static Maestrano configure(String preset, String filePath) throws MnoConfigurationException {
		Properties properties = loadProperties(filePath);
		return configure(preset, properties);
	}

	/**
	 * Configure Maestrano API using a Properties for the given preset
	 * 
	 * @param String
	 *            preset
	 * @param Properties
	 *            props
	 * @throws MnoConfigurationException
	 *             if an instance was already configured for this preset
	 */
	public static Maestrano configure(String preset, Properties props) throws MnoConfigurationException {
		Maestrano maestrano = new Maestrano(preset, props);
		Maestrano previous = instances.put(preset, maestrano);
		if (previous != null) {
			throw new MnoConfigurationException("An instance was already configured for preset: " + preset);
		}
		return maestrano;
	}

	/**
	 * reload the configuration for the given preset and overload the existing one if any
	 */
	public static Maestrano reloadConfiguration(String preset, Properties props) {
		Maestrano maestrano = new Maestrano(preset, props);
		instances.put(preset, maestrano);
		return maestrano;
	}

	/**
	 * reload the configuration for the given preset and overload the existing one if any
	 * 
	 * @param props
	 * @return
	 */
	public static Maestrano reloadConfiguration(Properties props) {
		return reloadConfiguration(DEFAULT, props);
	}

	/**
	 * return the Maestrano Instance for the given preset
	 * 
	 * @param preset
	 * @return
	 * @throws MnoConfigurationException
	 *             if no instance was not configured
	 */
	public static Maestrano get(String preset) throws MnoConfigurationException {
		Maestrano maestrano = instances.get(preset);
		if (maestrano == null) {
			throw new MnoConfigurationException("Mastrano was not configured for preset: " + preset + ". Maetrano.configure(" + preset + ") needs to have been called once.");
		}
		return maestrano;
	}

	public static Maestrano getDefault() {
		Maestrano maestrano = instances.get(DEFAULT);
		if (maestrano == null) {
			throw new RuntimeException("Mastrano was not configured for default preset. Maetrano.configure() needs to have been called once.");
		}
		return maestrano;
	}

	/**
	 * 
	 * @return the configured presets id
	 */
	public static Set<String> presets() {
		return instances.keySet();
	}

	/**
	 * 
	 * @return a unmodifiableMap of the Maestrano configuration indexed by preset Id.
	 */
	public static Map<String, Maestrano> getConfigurations() {
		return Collections.unmodifiableMap(instances);
	}

	/**
	 * Authenticate a Maestrano request using the appId and apiKey
	 * 
	 * @param preset
	 * 
	 * @param appId
	 * 
	 * @param apiKey
	 * 
	 * @return authenticated or not
	 * 
	 * @throws MnoException
	 */
	public boolean authenticate(String appId, String apiKey) throws MnoConfigurationException {
		return appId != null && apiKey != null && appId.equals(apiService.getId()) && apiKey.equals(apiService.getKey());
	}

	/**
	 * Authenticate a Maestrano request by reading the Authorization header
	 * 
	 * @param preset
	 * @param appId
	 * @return authenticated or not
	 * @throws UnsupportedEncodingException
	 * @throws MnoException
	 */
	public boolean authenticate(HttpServletRequest request) throws MnoException {
		String authHeader = request.getHeader("Authorization");
		if (authHeader == null || authHeader.isEmpty()) {
			return false;
		}
		String[] auth = authHeader.trim().split("\\s+");

		if (auth == null || auth.length != 2 || !auth[0].equalsIgnoreCase("basic")) {
			return false;
		}
		byte[] decodedStr = DatatypeConverter.parseBase64Binary(auth[1]);
		String[] creds;
		try {
			creds = (new String(decodedStr, "UTF-8")).split(":");
		} catch (UnsupportedEncodingException e) {
			throw new MnoException("Could not decode basic authentication" + Arrays.toString(auth), e);
		}

		if (creds.length == 2) {
			return authenticate(creds[0], creds[1]);
		} else {
			return false;
		}
	}

	/**
	 * Return the Maestrano App Service
	 * 
	 * @return AppService singleton
	 */
	public AppService appService() {
		return appService;
	}

	/**
	 * Return the Maestrano Sso Service
	 * 
	 * @return SsoService singleton
	 */
	public SsoService ssoService() {
		return ssoService;
	}

	/**
	 * Return the Maestrano Api Service
	 * 
	 * @return ApiService
	 */
	public ApiService apiService() {
		return apiService;
	}

	/**
	 * Return the Connec Service
	 * 
	 * @return ConnecService
	 */
	public ConnecService connecService() {
		return connecService;
	}

	/**
	 * Return the Maestrano Webhook Service
	 * 
	 * @return WebhookService
	 */
	public WebhookService webhookService() {
		return webhookService;
	}

	/**
	 * Return the preset of the Maestrano Configuration, or the marketplace if it is coming from autoconfiguration
	 * 
	 * @return
	 */
	public String getPreset() {
		return preset;
	}

	/**
	 * Return the Maestrano API configuration as a hash
	 * 
	 * @param preset
	 * @return metadata hash
	 */
	public Map<String, Object> toMetadataHash() {
		Map<String, Object> hash = new LinkedHashMap<String, Object>();
		hash.put("environment", appService.getEnvironment());
		hash.put("app", appService.toMetadataHash());
		hash.put("api", apiService.toMetadataHash());
		hash.put("sso", ssoService.toMetadataHash());
		hash.put("connec", connecService.toMetadataHash());
		hash.put("webhook", webhookService.toMetadataHash());

		return hash;
	}

	/**
	 * Return the Maestrano API configuration as a json hash
	 * 
	 * @return metadata hash
	 */
	public String toMetadata() {
		Gson gson = new Gson();
		return gson.toJson(toMetadataHash());
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

	@Override
	public String toString() {
		return toMetadata();
	}

}
