package com.maestrano.saml;

import static org.junit.Assert.assertEquals;

import java.io.StringReader;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;

import com.maestrano.configuration.Preset;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoZipHelper;
import com.maestrano.testhelpers.DefaultPropertiesHelper;

public class AuthRequestTest {
	private Properties properties;
	private AuthRequest subject;
	private Preset preset;

	@Before
	public void beforeEach() throws MnoConfigurationException {
		properties = DefaultPropertiesHelper.loadDefaultProperties();
		properties.setProperty("environment", "production");
		properties.setProperty("app.host", "https://mysuperapp.com");
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.key", "somekey");
		properties.setProperty("connec.host", "https://api-connec.maestrano.com");
		this.preset = new Preset("test", properties);
		subject = new AuthRequest(preset, new HashMap<String, String>());
	}

	@Test
	public void getXmlBase64Request_itReturnsABase64EncodedRequest() throws Exception {
		DatatypeConverter.parseBase64Binary(subject.getXmlBase64Request());
	}

	@Test
	public void getXmlBase64Request_itSetsTheRightSAMLParameters() throws Exception {
		String actual;
		byte[] encoded = DatatypeConverter.parseBase64Binary(subject.getXmlBase64Request());
		byte[] encodedDeflated = MnoZipHelper.inflate(encoded);
		String xmlReq = new String(encodedDeflated, "UTF-8");

		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = db.parse(new InputSource(new StringReader(xmlReq)));

		actual = dom.getElementsByTagName("saml:Issuer").item(0).getFirstChild().getTextContent();
		assertEquals(properties.getProperty("api.id"), actual);

		actual = dom.getElementsByTagName("samlp:AuthnRequest").item(0).getAttributes().getNamedItem("AssertionConsumerServiceURL").getNodeValue();
		assertEquals(properties.getProperty("sso.idm") + "/maestrano/auth/saml/consume", actual);
	}

	@Test
	public void getRedirectUrl_itGeneratesTheRightBaseUrl() throws Exception {
		String expected = "https://api-hub.maestrano.com/api/v1/auth/saml?SAMLRequest=";
		expected += URLEncoder.encode(subject.getXmlBase64Request(), "UTF-8");

		assertEquals(expected, subject.getRedirectUrl());
	}

	@Test
	public void getRedirectUrl_itAddsTheProvidedParameters() throws Exception {
		Map<String, String> params = new HashMap<String, String>();
		params.put("group_id", "cld-9");
		params.put("other", "value with spaces");
		subject = new AuthRequest(preset, params);

		String expected = "https://api-hub.maestrano.com/api/v1/auth/saml?SAMLRequest=";
		expected += URLEncoder.encode(subject.getXmlBase64Request(), "UTF-8");
		expected += "&other=value+with+spaces";
		expected += "&group_id=cld-9";

		assertEquals(expected, subject.getRedirectUrl());
	}
}
