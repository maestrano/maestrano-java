package com.maestrano.net;

import static org.junit.Assert.assertEquals;

import java.util.List;
import java.util.Properties;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.maestrano.DevPlatformService;
import com.maestrano.MarketplaceConfiguration;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.testhelpers.MnoHttpClientStub;

import junit.framework.Assert;

public class DevPlatformClientTest {

	private MnoHttpClientStub httpClient = new MnoHttpClientStub();
	private DevPlatformClient subject;

	@Before
	public void beforeEach() throws MnoConfigurationException {
		Properties properties = new Properties();
		properties.setProperty("environment.name", "name");
		properties.setProperty("environment.apiKey", "apiKey");
		properties.setProperty("environment.apiSecret", "apiSecret");

		DevPlatformService devPlatformService = new DevPlatformService(properties);
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

		Properties p1 = new Properties();
		p1.put("marketplace", "first_market_place");
		p1.put("environment", "uat");
		p1.put("app.host", "host");
		p1.put("sso.consumePath", "anotherValue");

		MarketplaceConfiguration first = new MarketplaceConfiguration("first_market_place", p1);
		Assert.assertEquals(first, marketplaceConfigurations.get(0));

		Properties p2 = new Properties();
		p2.put("marketplace", "second_market_place");
		p2.put("environment", "prod");
		p2.put("app.host", "host2");

		MarketplaceConfiguration second = new MarketplaceConfiguration("second_market_place", p2);
		Assert.assertEquals(second, marketplaceConfigurations.get(1));

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
