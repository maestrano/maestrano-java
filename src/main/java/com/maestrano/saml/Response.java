package com.maestrano.saml;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;
import javax.xml.crypto.dsig.XMLSignature;
import javax.xml.crypto.dsig.XMLSignatureException;
import javax.xml.crypto.dsig.XMLSignatureFactory;
import javax.xml.crypto.dsig.dom.DOMValidateContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import com.google.common.base.Supplier;
import com.google.common.base.Suppliers;
import com.maestrano.SsoService;
import com.maestrano.exception.MnoException;

public class Response {

	private final Certificate certificate;
	private final String xml;
	private final Document xmlDoc;
	private final Supplier<Map<String, String>> cachedAttributes = Suppliers.memoize(getCachedAttributesSupplier());

	private static final Logger logger = LoggerFactory.getLogger(Response.class);

	/**
	 * Load the Response with the provided XML string (not base64 encoded)
	 * 
	 * @param ssoService
	 *            Maestrano ssOSsoService
	 * @param String
	 *            xml response provided by the SAML idp
	 */
	public static Response loadFromXML(SsoService ssoService, String xml) throws CertificateException, ParserConfigurationException, SAXException, IOException {
		return new Response(ssoService.getSamlSettings().getIdpCertificate(), xml);
	}

	/**
	 * Load the response with the provided base64 encoded XML string
	 * 
	 * @param String
	 *            base64 encoded xml response provided by the SAML idp
	 * @throws ParserConfigurationException
	 * @throws SAXException
	 * @throws IOException
	 */
	public static Response loadFromBase64XML(SsoService ssoService, String base64xml) throws CertificateException, ParserConfigurationException, SAXException, IOException {
		return loadFromBase64XML(ssoService.getSamlSettings().getIdpCertificate(), base64xml);
	}

	public static Response loadFromBase64XML(String idpCertificate, String base64xml) throws CertificateException, ParserConfigurationException, SAXException, IOException {
		byte[] decodedB = DatatypeConverter.parseBase64Binary(base64xml);
		String xml = new String(decodedB, "UTF-8");
		return new Response(idpCertificate, xml);
	}

	/**
	 * Constructor
	 * 
	 * @param String
	 *            preset
	 * @throws CertificateException
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 * @throws MnoException
	 */
	protected Response(String idCertificate, String xml) throws CertificateException, ParserConfigurationException, SAXException, IOException {
		Certificate certificate = new Certificate(idCertificate);
		this.xml = xml;
		this.xmlDoc = parseXml(xml);
		this.certificate = certificate;
	}

	/**
	 * Constructor only used for Stubbing
	 */
	protected Response() {
		this.certificate = null;
		this.xmlDoc = null;
		this.xml = null;
	}

	private static Document parseXml(String xml) throws ParserConfigurationException, SAXException, IOException {
		DocumentBuilderFactory fty = DocumentBuilderFactory.newInstance();
		fty.setNamespaceAware(true);
		DocumentBuilder builder = fty.newDocumentBuilder();
		ByteArrayInputStream bais = new ByteArrayInputStream(xml.getBytes());
		return builder.parse(bais);
	}

	/**
	 * Check if the XML response provided by the IDP is valid or not
	 * 
	 * @return Boolean whether the response is valid or not
	 * @throws Exception
	 */
	public boolean isValid() {
		NodeList nodes = xmlDoc.getElementsByTagNameNS(XMLSignature.XMLNS, "Signature");

		if (nodes == null || nodes.getLength() == 0) {
			logger.debug("Can't find signature in document");
			return false;
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
			logger.debug("Could not unmarshalXMLSignature", e);
			xmlSignature = null;
		}
		if (xmlSignature == null) {
			return false;
		}
		try {
			return xmlSignature.validate(ctx);
		} catch (XMLSignatureException e) {
			logger.debug("Could not validate signature", e);
		}
		return false;
	}

	/**
	 * Return the Response nameID (user identifier)
	 * 
	 * @return String user nameID
	 * @throws Exception
	 */
	public String getNameId() throws Exception {
		NodeList nodes = xmlDoc.getElementsByTagNameNS("urn:oasis:names:tc:SAML:2.0:assertion", "NameID");

		if (nodes.getLength() == 0) {
			throw new Exception("No name id found in document");
		}

		return nodes.item(0).getTextContent();
	}

	/**
	 * Return all the assertions describing the user and user context (group)
	 * 
	 * @return HashMap<String,String> map of user/context attributes
	 */
	public Map<String, String> getAttributes() {
		return cachedAttributes.get();
	}

	/**
	 * Return the certificate used to check the response signature
	 * 
	 * @return
	 */
	public Certificate getCertificate() {
		return certificate;
	}

	public Document getXmlDoc() {
		return xmlDoc;
	}

	private static void tagIdAttributes(Document xmlDoc) {
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

	private static boolean setIdAttributeExists() {
		for (Method method : Element.class.getDeclaredMethods()) {
			if (method.getName().equals("setIdAttribute")) {
				return true;
			}
		}
		return false;
	}

	private Supplier<Map<String, String>> getCachedAttributesSupplier() {
		return new Supplier<Map<String, String>>() {

			public Map<String, String> get() {
				HashMap<String, String> result = new HashMap<String, String>();

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

							result.put(nameAttr, valAttr);
						}
					}
				}

				return result;
			}
		};
	}

	@Override
	public String toString() {
		return "Response [" + xml + "]";
	}
}