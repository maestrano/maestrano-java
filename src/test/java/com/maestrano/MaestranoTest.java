package com.maestrano;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.io.InputStream;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;
import org.junit.Before;
import org.junit.Test;

import com.maestrano.exception.MnoException;
import com.maestrano.testhelpers.HttpRequestStub;

public class MaestranoTest {
	private Properties defaultProps = new Properties();
	private Properties otherProps = new Properties();
	private Maestrano maestrano;

	@Before
	public void beforeEach() {
		defaultProps.setProperty("environment", "production");
		defaultProps.setProperty("app.host", "https://mysuperapp.com");
		defaultProps.setProperty("api.id", "someid");
		defaultProps.setProperty("api.key", "somekey");
		maestrano = Maestrano.reloadConfiguration(defaultProps);

		otherProps.setProperty("environment", "production");
		otherProps.setProperty("app.host", "https://myotherapp.com");
		otherProps.setProperty("api.id", "otherid");
		otherProps.setProperty("api.key", "otherkey");
		Maestrano.reloadConfiguration("other", otherProps);
	}

	@Test
	public void authenticate_withCredentials_itReturnsTrueWithRightCredentials() throws MnoException {
		assertTrue(Maestrano.authenticate(defaultProps.getProperty("api.id"), defaultProps.getProperty("api.key")));
	}

	@Test
	public void authenticate_withCredentials_itReturnsFalseWithWrongCredentials() throws MnoException {
		assertFalse(Maestrano.authenticate(defaultProps.getProperty("api.id"), defaultProps.getProperty("api.key") + "aa"));
		assertFalse(Maestrano.authenticate(defaultProps.getProperty("api.id") + "aa", defaultProps.getProperty("api.key")));
	}

	@Test
	public void authenticate_withRequest_itReturnsTrueWithRightBasicAuth() throws UnsupportedEncodingException, MnoException {
		HttpRequestStub request = new HttpRequestStub();
		String authStr = defaultProps.getProperty("api.id") + ":" + defaultProps.getProperty("api.key");
		authStr = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
		request.setHeader("Authorization", authStr);

		assertTrue(Maestrano.authenticate(request));
	}

	@Test
	public void authenticate_withRequest_itReturnsFalseWithWrongBasicAuth() throws UnsupportedEncodingException, MnoException {
		HttpRequestStub request = new HttpRequestStub();
		String authStr = defaultProps.getProperty("api.id") + ":" + defaultProps.getProperty("api.key") + "aaa";
		authStr = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
		request.setHeader("Authorization", authStr);

		assertFalse(Maestrano.authenticate(request));
	}

	@Test
	public void authenticate_withRequestPreset_itReturnsTrueWithRightBasicAuth() throws UnsupportedEncodingException, MnoException {
		HttpRequestStub request = new HttpRequestStub();
		String authStr = otherProps.getProperty("api.id") + ":" + otherProps.getProperty("api.key");
		authStr = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
		request.setHeader("Authorization", authStr);

		assertTrue(Maestrano.authenticate("other", request));
	}

	@Test
	public void authenticate_withRequestPreset_itReturnsFalseWithWrongBasicAuth() throws UnsupportedEncodingException, MnoException {
		HttpRequestStub request = new HttpRequestStub();
		String authStr = otherProps.getProperty("api.id") + ":" + otherProps.getProperty("api.key") + "aaa";
		authStr = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
		request.setHeader("Authorization", authStr);

		assertFalse(Maestrano.authenticate("other", request));
	}

	@Test
	public void toMetadataHash_itReturnTheRightValue() {
		Map<String, Object> hash = new HashMap<String, Object>();
		hash.put("environment", maestrano.appService().getEnvironment());
		hash.put("app", maestrano.appService().toMetadataHash());
		hash.put("api", maestrano.apiService().toMetadataHash());
		hash.put("sso", maestrano.ssoService().toMetadataHash());
		hash.put("connec", maestrano.connecService().toMetadataHash());
		hash.put("webhook", maestrano.webhookService().toMetadataHash());

		assertEquals(hash, maestrano.toMetadataHash());
	}

	@Test
	public void toMetadata_itReturnTheRightValue() throws IOException {
		maestrano.apiService().setLangVersion("JAVA_VERSION");
		InputStream inputStream = this.getClass().getResourceAsStream("/expected-metadata.json");
		String expected = IOUtils.toString(inputStream , StandardCharsets.UTF_8); 
		assertEquals(expected, maestrano.toMetadata());
	}

	@Test
	public void configure_itConfiguresFromFileAndPreset() throws MnoException {
		Properties properties = Maestrano.loadProperties("myconfig.properties");

		Maestrano myConfigMaestrano = Maestrano.reloadConfiguration("myconfig", properties);

		assertEquals("blabla", myConfigMaestrano.apiService().getId());
		assertEquals("secret", myConfigMaestrano.apiService().getKey());
	}
}
