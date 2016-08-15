package com.maestrano.helpers;

import java.io.IOException;
import java.io.InputStream;
import java.util.Date;
import java.util.Properties;

import org.apache.commons.io.IOUtils;

public class BuildInformationHelper {

	private static final Properties properties;

	/** Use a static initializer to read from file. */
	static {
		InputStream inputStream = BuildInformationHelper.class.getResourceAsStream("/com/maestrano/maestrano-build-info.properties");
		properties = new Properties();
		try {
			properties.load(inputStream);
		} catch (IOException e) {
			throw new RuntimeException("Failed to read properties file", e);
		} finally {
			IOUtils.closeQuietly(inputStream);
		}
	}

	/** Hide default constructor. */
	private BuildInformationHelper() {
	}

	/**
	 * Gets the Git SHA-1.
	 * 
	 * @return A {@code String} with the Git SHA-1.
	 */
	public static String getGitSha1() {
		return properties.getProperty("git-sha-1");
	}

	public static Date getBuildTimestamp() {
		String timeStamp = properties.getProperty("build-timestamp");
		long valueOf = Long.parseLong(timeStamp);
		Date date = new Date(valueOf);
		return date;
	}

	public static String getVersion() {
		return properties.getProperty("version");
	}

}
