package com.maestrano.saml;

import java.security.cert.CertificateException;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;
import com.maestrano.testhelpers.SamlCertHelper;
import com.maestrano.testhelpers.SamlRespHelper;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.assertFalse;

public class ResponseTest {
	private Properties props = new Properties();
	private Response subject;
	
	@Before
	public void beforeEach() throws CertificateException 
	{
		props.setProperty("app.environment", "production");
		props.setProperty("app.host", "https://mysuperapp.com");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		Maestrano.configure(props);
		subject = new Response();
	}
	
	@Test
	public void ItShouldConsiderResponse1AsInvalid() throws Exception
	{
		subject.getCertificate().loadCertificate(SamlCertHelper.certificate1());
		subject.loadXmlFromBase64(SamlRespHelper.response1XmlBase64());
		
		assertFalse(subject.isValid());
	}

	@Test
	public void ItShouldLoadResponseWithSpecialNewlineCharacters() throws Exception
	{
		// Response2 contains \n and \r characters that should break base64.decode usually
		subject.getCertificate().loadCertificate(SamlCertHelper.certificate1());
		subject.loadXmlFromBase64(SamlRespHelper.response2XmlBase64());

		assertFalse(subject.isValid());
		assertEquals("wibble@wibble.com", subject.getNameId());
	}

	@Test
	public void ItShouldLoadResponse4Properly() throws Exception
	{
		
		subject.getCertificate().loadCertificate(SamlCertHelper.certificate1());
		subject.loadXmlFromBase64(SamlRespHelper.response4XmlBase64());

		assertTrue(subject.isValid());
		assertEquals("bogus@onelogin.com", subject.getNameId());
	}


	@Test
	public void ItShouldLoadTheResponseAttributesProperly() throws Exception
	{
		subject.getCertificate().loadCertificate(SamlCertHelper.certificate1());
		subject.loadXmlFromBase64(SamlRespHelper.response1XmlBase64());
		
		assertEquals("demo", subject.getAttributes().get("uid"));
		assertEquals("value", subject.getAttributes().get("another_value"));
	}
}
