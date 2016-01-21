package com.maestrano;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.exception.MnoException;

/**
 * Entry point for Maestrano Configuration
 * 
 * You need to call it only once the {@link #configure() methods
 * 
 */
public final class Maestrano {
	public static final String DEFAULT = "default";

	private static final Map<String, Maestrano> instances = new HashMap<String, Maestrano>();

	private final AppService appService;
	private final ApiService apiService;
	private final SsoService ssoService;
	private final WebhookService webhookService;

	// Private constructor
	private Maestrano(Properties props) {
		this.appService = new AppService(props);
		this.apiService = new ApiService(appService, props);
		this.ssoService = new SsoService(apiService, appService, props);
		this.webhookService = new WebhookService(props);
	}

	/**
	 * Return the current Maestrano API version
	 * 
	 * @return String version
	 */
	public static String getVersion() {
		return Maestrano.class.getPackage().getImplementationVersion();
	}

	/**
	 * Configure Maestrano API using a Properties file and return an instance of Maestrano
	 * 
	 * @param String
	 *            filename
	 * @throws MnoConfigurationException
	 *             if an instance was already configured
	 */
	public static Maestrano configure() throws MnoConfigurationException {
		return configure(DEFAULT, "config.properties");
	}

	/**
	 * Configure Maestrano API using a Properties file and return an instance of Maestrano
	 * 
	 * @param String
	 *            filename
	 * @throws MnoConfigurationException
	 *             if an instance was already configured
	 */
	public static Maestrano configure(String filename) throws MnoConfigurationException {
		return configure(DEFAULT, filename);
	}

	/**
	 * Configure Maestrano API using a Properties object
	 * 
	 * @param Properties
	 *            props
	 * @throws MnoException
	 *             if an instance was already configured
	 */
	public static Maestrano configure(Properties props) throws MnoConfigurationException {
		return configure(DEFAULT, props);
	}

	/**
	 * Configure Maestrano API using a Properties file and preset
	 * 
	 * @param String
	 *            preset
	 * @param String
	 *            filename file in the classPath, or the absolute path of the file
	 * @throws MnoConfigurationException
	 */
	public static Maestrano configure(String preset, String filePath) throws MnoConfigurationException {
		Properties properties = loadProperties(filePath);
		return configure(preset, properties);
	}

	/**
	 * Configure Maestrano API using a Properties object and preset
	 * 
	 * @param String
	 *            preset
	 * @param Properties
	 *            props
	 * @throws MnoConfigurationException
	 *             if an instance was already configured for this preset
	 */
	public static Maestrano configure(String preset, Properties props) throws MnoConfigurationException {
		Maestrano maestrano = new Maestrano(props);
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
		Maestrano maestrano = new Maestrano(props);
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
	 * Authenticate a Maestrano request using the appId and apiKey
	 * 
	 * @param appId
	 * @param apiKey
	 * @return authenticated or not
	 * @throws MnoException
	 */
	public static boolean authenticate(String appId, String apiKey) throws MnoException {
		return authenticate(DEFAULT, appId, apiKey);
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
	public static boolean authenticate(String preset, String appId, String apiKey) throws MnoException {
		Maestrano maestrano = get(preset);
		ApiService apiService = maestrano.apiService();
		return appId != null && apiKey != null && appId.equals(apiService.getId()) && apiKey.equals(apiService.getKey());
	}

	/**
	 * Authenticate a Maestrano request by reading the Authorization header
	 * 
	 * @param appId
	 * @return authenticated or not
	 * @throws MnoException
	 * @throws UnsupportedEncodingException
	 */
	public static boolean authenticate(HttpServletRequest request) throws MnoException {
		return authenticate(DEFAULT, request);
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
	public static boolean authenticate(String preset, HttpServletRequest request) throws MnoException {
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
			throw new MnoException("Could not decode basic authentication" + Arrays.toString(auth));
		}

		if (creds.length == 2) {
			return authenticate(preset, creds[0], creds[1]);
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
	 * Return the Maestrano Webhook Service
	 * 
	 * @return WebhookService
	 */
	public WebhookService webhookService() {
		return webhookService;
	}

	/**
	 * Return the Maestrano API configuration as a hash
	 * 
	 * @param preset
	 * @return metadata hash
	 */
	public Map<String, Object> toMetadataHash() {
		Map<String, Object> hash = new HashMap<String, Object>();
		hash.put("environment", appService.getEnvironment());
		hash.put("app", appService.toMetadataHash());
		hash.put("api", apiService.toMetadataHash());
		hash.put("sso", ssoService.toMetadataHash());
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

	public static Properties loadProperties(String filePath) throws MnoConfigurationException {
		Properties properties = new Properties();
		InputStream input = getInputStreamFromClassPathOrFile(filePath);
		try {
			properties.load(input);
		} catch (IOException e) {
			throw new MnoConfigurationException("Could not load properties file: " + filePath, e);
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
