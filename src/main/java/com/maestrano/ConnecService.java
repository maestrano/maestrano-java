package com.maestrano;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

import com.maestrano.helpers.MnoPropertiesHelper;

/**
 * Maestrano Connec Service, related to all Connec API
 */
public class ConnecService {

	private static final String PROD_CONNEC_HOST = "https://api-connec.maestrano.com";
	private static final String TEST_CONNEC_HOST = "http://api-sandbox.maestrano.io";

	private final String host;
	private final String base;

	// package private Constructor
	ConnecService(AppService appService, Properties props) {
		this.base = getConnecBase(appService, props);
		this.host = getConnecHost(appService, props);
	}

	/**
	 * Return the host used to make API calls on Connec!
	 * 
	 * @return String host
	 */
	public String getHost() {
		return host;
	}

	private static String getConnecHost(AppService appService, Properties props) {
		String connecHost = MnoPropertiesHelper.getProperty(props, "connec.host", "api.connecHost");
		if (!MnoPropertiesHelper.isNullOrEmpty(connecHost)) {
			return connecHost;
		} else {
			if (appService.isProduction()) {
				return PROD_CONNEC_HOST;
			} else {
				return TEST_CONNEC_HOST;
			}
		}
	}

	/**
	 * Return the base of the API endpoint
	 * 
	 * @return String base
	 */
	public String getBase() {
		return base;
	}

	/**
	 * Return the base of the API endpoint
	 * 
	 * @return String base
	 */
	private static String getConnecBase(AppService appService, Properties props) {
		String connecBase = MnoPropertiesHelper.getProperty(props, "connec.base", "api.connecBase");
		if (!MnoPropertiesHelper.isNullOrEmpty(connecBase)) {
			return connecBase;
		} else {
			if (appService.isProduction()) {
				return "/api/v2";
			} else {
				return "/connec/api/v2";
			}
		}
	}

	public Map<String, String> toMetadataHash() {
		Map<String, String> hash = new LinkedHashMap<String, String>();
		hash.put("host", getHost());
		hash.put("base_path", getBase());
		return hash;
	}
}
