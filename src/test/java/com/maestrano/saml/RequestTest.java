package com.maestrano.saml;

import java.io.IOException;
import java.io.StringReader;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import javax.xml.bind.DatatypeConverter;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;

import org.junit.Before;
import org.junit.Test;
import org.w3c.dom.Document;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.maestrano.Maestrano;

import static org.junit.Assert.assertEquals;

public class RequestTest {
	private Properties props = new Properties();
	private Request subject;
	
	@Before
	public void beforeEach() {
		props.setProperty("app.environment", "production");
		props.setProperty("app.host", "https://mysuperapp.com");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		Maestrano.configure(props);
		subject = new Request();
	}
	
	@Test
	public void getXmlBase64Request_itReturnsABase64EncodedRequest() throws XMLStreamException {
		DatatypeConverter.parseBase64Binary(subject.getXmlBase64Request());
	}
	
	@Test
	public void getXmlBase64Request_itSetsTheRightSAMLParameters() throws XMLStreamException, ParserConfigurationException, SAXException, IOException {
		String actual;
		byte[] encoded = DatatypeConverter.parseBase64Binary(subject.getXmlBase64Request());
		String xmlReq = new String(encoded,"UTF-8");
		
		DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
		DocumentBuilder db = dbf.newDocumentBuilder();
		Document dom = db.parse(new InputSource(new StringReader(xmlReq)));
		
		actual = dom.getElementsByTagName("saml:Issuer").item(0).getFirstChild().getTextContent();
		assertEquals(props.getProperty("api.id"), actual);
		
		actual = dom.getElementsByTagName("samlp:AuthnRequest").item(0).getAttributes().getNamedItem("AssertionConsumerServiceURL").getNodeValue();
		assertEquals(props.getProperty("app.host") + "/maestrano/auth/saml/consume.jsp", actual);
	}
	
	@Test
	public void getRedirectUrl_itGeneratesTheRightBaseUrl() throws UnsupportedEncodingException, XMLStreamException {
		String expected = "https://maestrano.com/api/v1/auth/saml?SAMLRequest=";
		expected += URLEncoder.encode(subject.getXmlBase64Request(),"UTF-8");
		
		assertEquals(expected,subject.getRedirectUrl());
	}
	
	@Test
	public void getRedirectUrl_itAddsTheProvidedParameters() throws XMLStreamException, UnsupportedEncodingException {
		Map<String,String> params = new HashMap<String,String>();
		params.put("group_id", "cld-9");
		params.put("other", "value with spaces");
		subject = new Request(params);
		
		String expected = "https://maestrano.com/api/v1/auth/saml?SAMLRequest=";
		expected += URLEncoder.encode(subject.getXmlBase64Request(),"UTF-8");
		expected += "&other=value+with+spaces";
		expected += "&group_id=cld-9";
		
		assertEquals(expected,subject.getRedirectUrl());
	}
}
