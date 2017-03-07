package com.maestrano;

import static org.junit.Assert.assertNotNull;

import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.exception.MnoException;

public class MaestranoTest {
	@Before
	public void loadDefaultConfiguration() throws Exception {
		Properties properties = new Properties();
		properties.setProperty("app.host", "http://localhost");
		properties.setProperty("api.id", "api_id");
		properties.setProperty("api.key", "api_key");
		properties.setProperty("api.base", "/api/v1");
		properties.setProperty("api.host", "http://localhost");
		properties.setProperty("sso.initPath", "/api/saml/init");
		properties.setProperty("sso.consumePath", "/api/saml/consume");
		properties.setProperty("sso.idp", "http://localhost");
		properties.setProperty("sso.x509Fingerprint", "31 96 DC BA");
		properties.setProperty("sso.x509Certificate", "-----BEGIN RSA Mnxdd== -----END RSA");
		properties.setProperty("connec.basePath", "/api/v2");
		properties.setProperty("connec.host", "http://localhost");
		properties.setProperty("webhooks.account.groupPath", "/api/v2/app_instances");
		properties.setProperty("webhooks.account.groupUserPath", "/api/v2/users");
		properties.setProperty("webhooks.connec.externalIds", "true");
		properties.setProperty("webhooks.connec.notificationPath", "/api/connec/notifications");

		Maestrano.reloadConfiguration("my-marketplace", properties);
	}

	@Test
	public void get_withValidPreset() throws MnoException {
		assertNotNull(Maestrano.get("my-marketplace"));
	}

	@Test(expected = MnoConfigurationException.class)
	public void get_withInvalidPreset() throws MnoException {
		Maestrano.get("invalid");
	}
}
