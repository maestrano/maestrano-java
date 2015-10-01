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
import org.w3c.dom.Node;

import java.lang.reflect.Method;

import org.w3c.dom.Element;

import com.maestrano.Maestrano;

import java.util.HashMap;
import java.util.Map;

public class Response {
	
	private Document xmlDoc;
	private Settings settings;
	private Certificate certificate;
	protected Map<String,String> cachedAttributes = null;
	
	/**
	 * Constructor
	 * @param String preset
	 * @throws CertificateException
	 */
	public Response(String preset) throws CertificateException {
		this.settings = Maestrano.ssoService().getSamlSettings(preset);
		certificate = new Certificate();
		certificate.loadCertificate(this.settings.getIdpCertificate());
	}
	
	/**
     * Constructor
     * @throws CertificateException
     */
    public Response() throws CertificateException {
        this("default");
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
		
		if (setIdAttributeExists()) {
			tagIdAttributes(xmlDoc);
		}

		X509Certificate cert = certificate.getX509Cert();
		DOMValidateContext ctx = new DOMValidateContext(cert.getPublicKey(), nodes.item(0));
		XMLSignatureFactory sigF = XMLSignatureFactory.getInstance("DOM");
		
		XMLSignature xmlSignature;
		try {
			xmlSignature = sigF.unmarshalXMLSignature(ctx);
		} catch (Exception e) {
			xmlSignature = null;
		}
		
		if (xmlSignature != null) {
			return xmlSignature.validate(ctx);
		} else {
			return false;
		}
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
	public Map<String,String> getAttributes() {
		if (cachedAttributes != null)
        {
            return cachedAttributes;
        }
		
		cachedAttributes = new HashMap<String,String>();
		
		NodeList nodes = xmlDoc.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "Attribute");
		
		for (int i = 0; i < nodes.getLength(); i++) {
			Node node = nodes.item(i);
			
			if (node.getAttributes() != null && node.getAttributes().getNamedItem("Name") != null) {
				String nameAttr = node.getAttributes().getNamedItem("Name").getNodeValue();
				if (nameAttr != null && !nameAttr.isEmpty()) {
					String valAttr = null;
					
					for (int j = 0; j < node.getChildNodes().getLength(); j++) {
						if (node.getChildNodes().item(j).getNodeName().matches(".*AttributeValue")) {
							valAttr = node.getChildNodes().item(j).getTextContent();
						}
					}
					
					cachedAttributes.put(nameAttr,valAttr);
				}
			}
		}
    
		return cachedAttributes;
	}
     
	/**
	 * Return the certificate used to check the response signature
	 * @return
	 */
	public Certificate getCertificate() {
		return certificate;
	}
	
	private void tagIdAttributes(Document xmlDoc) {
		NodeList nodeList = xmlDoc.getElementsByTagName("*");
		for (int i = 0; i < nodeList.getLength(); i++) {
			Node node = nodeList.item(i);
			if (node.getNodeType() == Node.ELEMENT_NODE) {
				if (node.getAttributes().getNamedItem("ID") != null) {
					((Element) node).setIdAttribute("ID", true);
				}
			}
		}
	}

	private boolean setIdAttributeExists() {
		for (Method method : Element.class.getDeclaredMethods()) {
			if (method.getName().equals("setIdAttribute")) {
				return true;
			}
		}
		return false;
	}
}