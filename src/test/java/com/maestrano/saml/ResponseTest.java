package com.maestrano.saml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.security.cert.CertificateException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;
import com.maestrano.exception.MnoException;
import com.maestrano.testhelpers.SamlCertHelper;
import com.maestrano.testhelpers.SamlRespHelper;

public class ResponseTest {
	private Properties props = new Properties();
	private Response subject;

	@Before
	public void beforeEach() throws CertificateException, MnoException {
		props.setProperty("environment", "production");
		props.setProperty("app.host", "https://mysuperapp.com");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		Maestrano maestrano = Maestrano.reloadConfiguration(props);
		subject = new Response(maestrano);
	}

	@Test
	public void ItShouldConsiderResponse1AsInvalid() throws Exception {
		subject.setCertificate(new Certificate(SamlCertHelper.certificate1()));
		subject.loadXmlFromBase64(SamlRespHelper.response1XmlBase64());

		assertFalse(subject.isValid());
	}

	@Test
	public void ItShouldLoadResponseWithSpecialNewlineCharacters() throws Exception {
		// Response2 contains \n and \r characters that should break base64.decode usually

		subject.setCertificate(new Certificate(SamlCertHelper.certificate1()));
		subject.loadXmlFromBase64(SamlRespHelper.response2XmlBase64());

		assertFalse(subject.isValid());
		assertEquals("wibble@wibble.com", subject.getNameId());
	}

	@Test
	public void ItShouldLoadResponse4Properly() throws Exception {

		subject.setCertificate(new Certificate(SamlCertHelper.certificate1()));
		subject.loadXmlFromBase64(SamlRespHelper.response4XmlBase64());

		assertTrue(subject.isValid());
		assertEquals("bogus@onelogin.com", subject.getNameId());
	}

	@Test
	public void ItShouldLoadTheResponseAttributesProperly() throws Exception {
		subject.setCertificate(new Certificate(SamlCertHelper.certificate1()));
		subject.loadXmlFromBase64(SamlRespHelper.response1XmlBase64());

		assertEquals("demo", subject.getAttributes().get("uid"));
		assertEquals("value", subject.getAttributes().get("another_value"));
	}
}
