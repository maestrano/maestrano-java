package com.maestrano;

import static org.junit.Assert.assertEquals;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class ConnecServiceTest {
	private Properties props = new Properties();
	private ConnecService subject;

	@Before
	public void beforeEach() {
		props.setProperty("environment", "production");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		Maestrano maestrano = Maestrano.reloadConfiguration(props);
		subject = maestrano.connecService();
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
