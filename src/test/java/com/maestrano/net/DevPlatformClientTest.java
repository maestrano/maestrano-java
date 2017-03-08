package com.maestrano.net;

import static org.junit.Assert.*;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.maestrano.configuration.DevPlatform;
import com.maestrano.configuration.MarketplaceConfiguration;
import com.maestrano.configuration.Preset;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.testhelpers.MnoHttpClientStub;

public class DevPlatformClientTest {

	private MnoHttpClientStub httpClient = new MnoHttpClientStub();
	private DevPlatformClient subject;

	@Before
	public void beforeEach() throws MnoConfigurationException {
		Properties properties = new Properties();
		properties.setProperty("environment.name", "name");
		properties.setProperty("environment.apiKey", "apiKey");
		properties.setProperty("environment.apiSecret", "apiSecret");

		DevPlatform devPlatformService = new DevPlatform(properties);
		this.subject = new DevPlatformClient(devPlatformService);
	}

	@Test
	public void getInstanceUrl_ItReturnsTheRightInstanceUrl() {
		assertEquals("https://developer.maestrano.com/api/config/v1", subject.getInstanceUrl());
	}

	@Test
	public void getMarketplaceConfigurations_itReturnsTheRightConfigurations() throws Exception {

		// Prepare response
		httpClient = new MnoHttpClientStub();
		String json = IOUtils.toString(this.getClass().getResourceAsStream("dev-platform-marketplaces.json"));

		httpClient.setResponseStub(json, "https://developer.maestrano.com/api/config/v1/marketplaces");

		List<MarketplaceConfiguration> marketplaceConfigurations = subject.getMarketplaceConfigurations(httpClient);
		Map<String, Preset> presets = new LinkedHashMap<String, Preset>();
		for (MarketplaceConfiguration marketplaceConfiguration : marketplaceConfigurations) {
			String marketplace = marketplaceConfiguration.getName();
			Preset preset = new Preset(marketplace, marketplaceConfiguration.getProperties());
			presets.put(marketplace, preset);
		}
		assertFalse(presets.isEmpty());
		Preset preset = presets.get("maestrano-test");
		assertEquals("test", preset.getEnvironment());
		assertEquals("http://localhost:63705", preset.getApp().getHost());
		assertEquals("app-fasdfasd", preset.getApi().getId());
		assertEquals("/maestrano/consume/?marketplace=maestrano-test", preset.getSso().getConsumePath());
		assertEquals("8b:1e:2e:76:c4:67:80:68:6c:81:18:f7:d3:29:5d:77:f8:79:54:2f", preset.getSso().getX509Fingerprint());
		
		assertEquals("https://api-connec-uat.maestrano.io", preset.getConnec().getHost());
		
		assertEquals("/maestrano/account/groups/:group_id/users/:id/maestrano-uat", preset.getWebhook().getAccountGroupUserPath());
		
	}

	@Test(expected = MnoConfigurationException.class)
	public void getMarketplaceConfigurations_whenJsonContainsErrors_throwsAnError() throws Exception {
		// Prepare response
		httpClient = new MnoHttpClientStub();
		String json = "{'error': 'error'}";
		httpClient.setResponseStub(json, "https://developer.maestrano.com/api/config/v1/marketplaces");
		subject.getMarketplaceConfigurations(httpClient);
	}

	@Test(expected = MnoConfigurationException.class)
	public void getMarketplaceConfigurations_whenJsonIsInvalid_throwsAnError() throws Exception {
		// Prepare response
		httpClient = new MnoHttpClientStub();
		String json = "INVALID";
		httpClient.setResponseStub(json, "https://developer.maestrano.com/api/config/v1/marketplaces");
		subject.getMarketplaceConfigurations(httpClient);

	}

	@Test(expected = MnoConfigurationException.class)
	public void getMarketplaceConfigurations_whenApiExceptionIsRaised_throwsAnError() throws Exception {
		// Prepare response
		httpClient = new MnoHttpClientStub();
		httpClient.setExceptionStub(new ApiException("Error"), "https://developer.maestrano.com/api/config/v1/marketplaces");
		subject.getMarketplaceConfigurations(httpClient);

	}

}
