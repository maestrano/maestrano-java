package com.maestrano.saml;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.maestrano.testhelpers.SamlCertHelper;
import com.maestrano.testhelpers.SamlRespHelper;

public class ResponseTest {

	@Test
	public void ItShouldConsiderResponse1AsInvalid() throws Exception {
		Response subject = Response.loadFromBase64XML(SamlCertHelper.certificate1(), SamlRespHelper.response1XmlBase64());

		assertFalse(subject.isValid());
	}

	@Test
	public void ItShouldLoadResponseWithSpecialNewlineCharacters() throws Exception {
		Response subject = Response.loadFromBase64XML(SamlCertHelper.certificate1(), SamlRespHelper.response2XmlBase64());

		assertFalse(subject.isValid());
		assertEquals("wibble@wibble.com", subject.getNameId());
	}

	@Test
	public void ItShouldLoadResponse4Properly() throws Exception {
		Response subject = Response.loadFromBase64XML(SamlCertHelper.certificate1(), SamlRespHelper.response4XmlBase64());

		assertTrue(subject.isValid());
		assertEquals("bogus@onelogin.com", subject.getNameId());
	}

	@Test
	public void ItShouldLoadTheResponseAttributesProperly() throws Exception {
		Response subject = Response.loadFromBase64XML(SamlCertHelper.certificate1(), SamlRespHelper.response1XmlBase64());

		assertEquals("demo", subject.getAttributes().get("uid"));
		assertEquals("value", subject.getAttributes().get("another_value"));
	}
}
