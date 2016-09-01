package com.maestrano.net;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.maestrano.DevPlatformService;
import com.maestrano.MarketplaceConfiguration;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.exception.MnoException;
import com.maestrano.helpers.MnoPropertiesHelper;
import com.maestrano.json.DateDeserializer;
import com.maestrano.json.DateSerializer;
import com.maestrano.json.TimeZoneDeserializer;
import com.maestrano.json.TimeZoneSerializer;

public class DevPlatformClient {

	public final Gson GSON = new GsonBuilder().setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES).registerTypeAdapter(Date.class, new DateSerializer())
			.registerTypeAdapter(Date.class, new DateDeserializer()).registerTypeAdapter(TimeZone.class, new TimeZoneSerializer()).registerTypeAdapter(TimeZone.class, new TimeZoneDeserializer())
			.create();
	private final DevPlatformService devPlatformService;

	public DevPlatformClient(DevPlatformService devPlatformService) {
		this.devPlatformService = devPlatformService;
	}

	public List<MarketplaceConfiguration> getMarketplaceConfigurations() throws MnoConfigurationException {
		return getMarketplaceConfigurations(getAuthenticatedClient());
	}

	
	
	public List<MarketplaceConfiguration> getMarketplaceConfigurations(MnoHttpClient client) throws MnoConfigurationException {
		String string;
		try {
			string = client.get(getInstanceUrl() + "/marketplaces");
		} catch (MnoException e) {
			throw new MnoConfigurationException("Could not retrieve the list of the marketplace: " + e.getMessage(), e);
		}
		Map<String, Object> fromJson = fromJson(string);

		if (fromJson.get("error") != null) {
			throw new MnoConfigurationException("An error occurred while retrieving the marketplaces. Body content: " + fromJson.get("error"));
		}
		@SuppressWarnings("unchecked")
		Collection<Object> marketplaces = (Collection<Object>) fromJson.get("marketplaces");
		if (marketplaces == null) {
			throw new MnoConfigurationException("An error occurred while retrieving the marketplaces. No marketplaces in the json answer");
		}

		List<MarketplaceConfiguration> result = new ArrayList<MarketplaceConfiguration>();

		for (Object marketplace : marketplaces) {
			@SuppressWarnings("unchecked")
			Map<String, Object> castedMarketplace = (Map<String, Object>) marketplace;
			String name = (String) castedMarketplace.get("marketplace");
			Properties properties = MnoPropertiesHelper.fromJson(marketplace);
			result.add(new MarketplaceConfiguration(name, properties));
		}
		return result;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> fromJson(String string) throws MnoConfigurationException {
		try {
			return GSON.fromJson(string, Map.class);
		} catch (JsonSyntaxException e) {
			throw new MnoConfigurationException("Could not retrieve the list of the marketplace. The Json is invalid.", e);
		}
	}

	private MnoHttpClient getAuthenticatedClient() {
		return MnoHttpClient.getAuthenticatedClient(devPlatformService.getApiKey(), devPlatformService.getApiSecret(), "application/vnd.api+json");
	}

	public String getInstanceUrl() {
		return devPlatformService.getHost() + devPlatformService.getApiPath();
	}
}
