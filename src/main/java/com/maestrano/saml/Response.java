package com.maestrano.saml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;

import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import java.lang.reflect.Method;

import org.w3c.dom.Element;
import org.w3c.dom.Node;

import com.maestrano.Maestrano;

import java.util.HashMap;

public class Response {
	
	private Document xmlDoc;
	private Settings settings;
	private Certificate certificate;

	/**
	 * Constructor
	 * @throws CertificateException
	 */
	public Response() throws CertificateException {
		this.settings = Maestrano.ssoService().getSamlSettings();
		certificate = new Certificate();
		certificate.loadCertificate(this.settings.getIdpCertificate());
	}
	
	/**
	 * Load the Response with the provided XML string (not base64 encoded)
	 * @param String xml response provided by the SAML idp
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void loadXml(String xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory fty = DocumentBuilderFactory.newInstance();
		fty.setNamespaceAware(true);
		DocumentBuilder builder = fty.newDocumentBuilder();
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
		xmlDoc = builder.parse(bais);		
	}
	
	/**
	 * Load the response with the provided base64 encoded XML string
	 * @param String base64 encoded xml response provided by the SAML idp
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public void loadXmlFromBase64(String response) throws ParserConfigurationException, SAXException, IOException {		
		byte [] decodedB = DatatypeConverter.parseBase64Binary(response);
		String decodedS = new String(decodedB,"UTF-8");
		loadXml(decodedS);	
	}
	
	/**
	 * Check if the XML response provided by the IDP is valid or not
	 * @return Boolean whether the response is valid or not
	 * @throws Exception
	 */
	public boolean isValid() throws Exception {
		NodeList nodes = xmlDoc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");

		if (nodes == null || nodes.getLength() == 0) {
			throw new Exception("Can't find signature in document.");
		}

		X509Certificate cert = certificate.getX509Cert();
		DOMValidateContext ctx = new DOMValidateContext(cert.getPublicKey(), nodes.item(0));
		XMLSignatureFactory sigF = XMLSignatureFactory.getInstance("DOM");
		XMLSignature xmlSignature = sigF.unmarshalXMLSignature(ctx);

		return xmlSignature.validate(ctx);
	}
	
	/**
	 * Return the Response nameID (user identifier)
	 * @return String user nameID
	 * @throws Exception
	 */
	public String getNameId() throws Exception {
		NodeList nodes = xmlDoc.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "NameID");		

		if(nodes.getLength()==0){
			throw new Exception("No name id found in document");
		}		

		return nodes.item(0).getTextContent();
	}
	
	/**
	 * Return all the assertions describing the user and user context (group) 
	 * @return HashMap<String,String> map of user/context attributes
	 */
	public HashMap<String,String> getAttributes() {
		HashMap<String,String> attributes = new HashMap<String,String>();
    
		NodeList nodes = xmlDoc.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Attribute");
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			if (node.getNodeType() == Node.ATTRIBUTE_NODE) {
				attributes.put(node.getNodeName(),node.getNodeValue());
			}
		}
    
		return attributes;
	}
     
	/**
	 * Return the certificate used to check the response signature
	 * @return
	 */
	public Certificate getCertificate() {
		return certificate;
	}
}