package com.maestrano.saml;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import javax.xml.bind.DatatypeConverter;

import com.maestrano.Maestrano;

public class AuthRequest {
	
	private String id;
	private String issueInstant;
	private Settings settings;
	private Map<String,String> parameters;
	
	/**
	 * Constructor
	 */
	public AuthRequest() {
		this.settings = Maestrano.ssoService().getSamlSettings();
		id = "_" + UUID.randomUUID().toString();
		SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy-MM-dd'T'H:mm:ssZ");
		issueInstant = simpleDf.format(new Date());	
	}
	
	/**
	 * Constructor
	 * @param Map<String,String> request parameters
	 */
	public AuthRequest(Map<String, String> parameters)
	{	
		this();
		this.parameters = parameters;
	}
	
	/**
	 * Constructor
	 * @param request
	 */
	@SuppressWarnings("unchecked")
	public AuthRequest(ServletRequest request) {
		this();
		this.parameters = new HashMap<String,String>();
				
		Enumeration<String> parameterNames = request.getParameterNames();

        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            this.parameters.put(paramName, request.getParameter(paramName));
        }
	}
	
	
	/**
	 * 
	 * @return String base 64 encoded SAML request
	 * @throws XMLStreamException
	 */
	public String getXmlBase64Request() throws XMLStreamException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = factory.createXMLStreamWriter(baos);
		
		// Prepare XML request
		writer.writeStartElement("samlp", "AuthnRequest", "urn:oasis:names:tc:SAML:2.0:protocol");
			writer.writeNamespace("samlp","urn:oasis:names:tc:SAML:2.0:protocol");
			writer.writeAttribute("ID", id);
			writer.writeAttribute("Version", "2.0");
			writer.writeAttribute("IssueInstant", this.issueInstant);
			writer.writeAttribute("ProtocolBinding", "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST");
			writer.writeAttribute("AssertionConsumerServiceURL", this.settings.getAssertionConsumerServiceUrl());
		
			writer.writeStartElement("saml","Issuer","urn:oasis:names:tc:SAML:2.0:assertion");
				writer.writeNamespace("saml","urn:oasis:names:tc:SAML:2.0:assertion");
				writer.writeCharacters(this.settings.getIssuer());
			writer.writeEndElement();
		
			writer.writeStartElement("samlp", "NameIDPolicy", "urn:oasis:names:tc:SAML:2.0:protocol");
				writer.writeAttribute("Format", "urn:oasis:names:tc:SAML:2.0:nameid-format:unspecified");
				writer.writeAttribute("AllowCreate", "true");
			writer.writeEndElement();
		
			writer.writeStartElement("samlp","RequestedAuthnContext","urn:oasis:names:tc:SAML:2.0:protocol");
				writer.writeAttribute("Comparison", "exact");
			writer.writeEndElement();
		
			writer.writeStartElement("saml","AuthnContextClassRef","urn:oasis:names:tc:SAML:2.0:assertion");
				writer.writeNamespace("saml", "urn:oasis:names:tc:SAML:2.0:assertion");
				writer.writeCharacters("urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport");
			writer.writeEndElement();
		writer.writeEndElement();
		writer.flush();		
		
		// Base64 encode the result
		String result = DatatypeConverter.printBase64Binary(baos.toByteArray());
		
		return result;
	}
	
	/**
	 * Return the full IDP redirect url with encoded SAML request
	 * @return String SAML request url
	 * @throws XMLStreamException 
	 * @throws UnsupportedEncodingException 
	 */
	public String getRedirectUrl() throws UnsupportedEncodingException, XMLStreamException {
		String url = this.settings.getIdpSsoTargetUrl();
		url += "?SAMLRequest=";
		
		url += URLEncoder.encode(this.getXmlBase64Request(),"UTF-8");
		
		if (this.parameters != null) {
			for (Map.Entry<String, String> param : this.parameters.entrySet())
			{
				String key = URLEncoder.encode(param.getKey(),"UTF-8");
				String val = URLEncoder.encode(param.getValue(), "UTF-8");
				url += "&" + key + "=" + val;
			}
		}
		
		
		return url;
	}

}
