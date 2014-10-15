package com.maestrano;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class AppServiceTest {
	private Properties props = new Properties();
	private AppService subject;

	@Before
	public void beforeEach() {
		props.setProperty("app.environment", "production");
		Maestrano.configure(props);
		subject = Maestrano.appService();
	}

	@Test
	public void getEnvironment_itReturnsTheRightValue() {
		assertEquals("production", subject.getEnvironment());
	}
	
	@Test
	public void getHost_itReturnsTheRightValue() {
		String host = "https://mysuperapp.com";
		props.setProperty("app.host", host);
		
		assertEquals(host, subject.getHost());
	}

}
