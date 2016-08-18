package com.maestrano.saml;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletRequest;
import javax.xml.bind.DatatypeConverter;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;

import com.maestrano.Maestrano;
import com.maestrano.exception.MnoException;
import com.maestrano.helpers.MnoZipHelper;

public class AuthRequest {

	private final String id;
	private final String issueInstant;
	private final Settings settings;
	private final Map<String, String> parameters;

	/**
	 * Constructor
	 * 
	 * @param String
	 *            preset
	 * @param Map<String,String>
	 *            request parameters
	 * @throws MnoException
	 */
	public AuthRequest(Maestrano maestrano, Map<String, String> parameters) {
		this.settings = maestrano.ssoService().getSamlSettings();
		id = "_" + UUID.randomUUID().toString();
		SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy-MM-dd'T'H:mm:ssZ");
		issueInstant = simpleDf.format(new Date());
		this.parameters = parameters;
	}

	/**
	 * Constructor
	 * 
	 * @param String
	 *            preset
	 * @param request
	 * @throws MnoException
	 */
	public AuthRequest(Maestrano maestrano, ServletRequest request) {
		this(maestrano, new HashMap<String, String>());

		@SuppressWarnings("unchecked")
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
	 * @throws IOException
	 */
	public String getXmlBase64Request() throws XMLStreamException, IOException {
		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		XMLOutputFactory factory = XMLOutputFactory.newInstance();
		XMLStreamWriter writer = factory.createXMLStreamWriter(baos);

		// Prepare XML request
		writer.writeStartElement("samlp", "AuthnRequest", "urn:oasis:names:tc:SAML:2.0:protocol");
		writer.writeNamespace("samlp", "urn:oasis:names:tc:SAML:2.0:protocol");
		writer.writeAttribute("ID", id);
		writer.writeAttribute("Version", "2.0");
		writer.writeAttribute("IssueInstant", this.issueInstant);
		writer.writeAttribute("ProtocolBinding", "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST");
		writer.writeAttribute("AssertionConsumerServiceURL", this.settings.getAssertionConsumerServiceUrl());

		writer.writeStartElement("saml", "Issuer", "urn:oasis:names:tc:SAML:2.0:assertion");
		writer.writeNamespace("saml", "urn:oasis:names:tc:SAML:2.0:assertion");
		writer.writeCharacters(this.settings.getIssuer());
		writer.writeEndElement();

		writer.writeStartElement("samlp", "NameIDPolicy", "urn:oasis:names:tc:SAML:2.0:protocol");
		writer.writeAttribute("Format", "urn:oasis:names:tc:SAML:2.0:nameid-format:unspecified");
		writer.writeAttribute("AllowCreate", "true");
		writer.writeEndElement();

		writer.writeStartElement("samlp", "RequestedAuthnContext", "urn:oasis:names:tc:SAML:2.0:protocol");
		writer.writeAttribute("Comparison", "exact");
		writer.writeEndElement();

		writer.writeStartElement("saml", "AuthnContextClassRef", "urn:oasis:names:tc:SAML:2.0:assertion");
		writer.writeNamespace("saml", "urn:oasis:names:tc:SAML:2.0:assertion");
		writer.writeCharacters("urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport");
		writer.writeEndElement();
		writer.writeEndElement();
		writer.flush();

		// Deflate
		byte[] output = MnoZipHelper.deflate(baos.toByteArray());

		// Base64 encode the result
		String result = DatatypeConverter.printBase64Binary(output);

		return result;
	}

	/**
	 * Return the full IDP redirect url with encoded SAML request
	 * 
	 * @return String SAML request url
	 * @throws XMLStreamException
	 * @throws IOException
	 */
	public String getRedirectUrl() throws XMLStreamException, IOException {
		String url = this.settings.getIdpSsoTargetUrl();
		url += "?SAMLRequest=";

		url += URLEncoder.encode(this.getXmlBase64Request(), "UTF-8");

		if (this.parameters != null) {
			for (Map.Entry<String, String> param : this.parameters.entrySet()) {
				String key = URLEncoder.encode(param.getKey(), "UTF-8");
				String val = URLEncoder.encode(param.getValue(), "UTF-8");
				url += "&" + key + "=" + val;
			}
		}

		return url;
	}

}
