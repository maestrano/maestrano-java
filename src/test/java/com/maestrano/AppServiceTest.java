package com.maestrano;

import static org.junit.Assert.assertEquals;

import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

public class AppServiceTest {
	private Properties props = new Properties();
	private AppService subject;

	@Before
	public void beforeEach() {
		props.setProperty("environment", "production");
		props.setProperty("app.host", "https://mysuperapp.com");
		
		Maestrano maestrano = Maestrano.reloadConfiguration(props);
		subject = maestrano.appService();
	}

	@Test
	public void getEnvironment_itReturnsTheRightValue() {
		assertEquals("production", subject.getEnvironment());
	}
	
	@Test
	public void getHost_itReturnsTheRightValue() {
		assertEquals("https://mysuperapp.com", subject.getHost());
	}
	
	@Test
	public void toMetadataHash_itReturnsTheRightValue() {
		Map<String,String> hash = subject.toMetadataHash();
		assertEquals(props.getProperty("app.host"), hash.get("host"));
	}

}
