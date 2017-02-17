package com.maestrano.configuration;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.testhelpers.DefaultPropertiesHelper;

public class ConnecService {
	private Connec subject;

	@Before
	public void beforeEach() throws MnoConfigurationException {
		Properties properties = DefaultPropertiesHelper.loadDefaultProperties();
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.key", "somekey");
		Preset preset = new Preset("test", properties);
		subject = preset.getConnec();
	}

	@Test
	public void getBase_itReturnsTheRightValue() {
		assertEquals("/api/v2", subject.getBasePath());
	}

	@Test
	public void getHost_itReturnsTheRightProductionValue() {
		assertEquals("https://api-connec.maestrano.com", subject.getHost());
	}
}
