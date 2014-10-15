//package com.maestrano.saml;
//
//import java.io.ByteArrayOutputStream;
//import java.nio.charset.Charset;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Map;
//import java.util.UUID;
//
//import javax.xml.stream.XMLOutputFactory;
//import javax.xml.stream.XMLStreamException;
//import javax.xml.stream.XMLStreamWriter;
//
//import org.apache.commons.codec.binary.Base64;
//
//import com.maestrano.lib.onelogin.AccountSettings;
//import com.maestrano.lib.onelogin.AppSettings;
//
//public class Request {
//	
//	private String id;
//	private String issueInstant;
//	//private AppSettings appSettings;
//	
//	public Request(Map<String, String> parameters)
//	{		
//		this.appSettings = appSettings;
//		id="_"+UUID.randomUUID().toString();		
//		SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy-MM-dd'T'H:mm:ss");
//		issueInstant = simpleDf.format(new Date());		
//	}
//	
//	public String getXmlBase64Request() throws XMLStreamException {
//		ByteArrayOutputStream baos = new ByteArrayOutputStream();		
//		XMLOutputFactory factory = XMLOutputFactory.newInstance();
//		XMLStreamWriter writer = factory.createXMLStreamWriter(baos);
//		
//		// Prepare XML request
//		writer.writeStartElement("samlp", "AuthnRequest", "urn:oasis:names:tc:SAML:2.0:protocol");
//		writer.writeNamespace("samlp","urn:oasis:names:tc:SAML:2.0:protocol");
//		
//		writer.writeAttribute("ID", id);
//		writer.writeAttribute("Version", "2.0");
//		writer.writeAttribute("IssueInstant", this.issueInstant);
//		writer.writeAttribute("ProtocolBinding", "urn:oasis:names:tc:SAML:2.0:bindings:HTTP-POST");
//		writer.writeAttribute("AssertionConsumerServiceURL", this.appSettings.getAssertionConsumerServiceUrl());
//		
//		writer.writeStartElement("saml","Issuer","urn:oasis:names:tc:SAML:2.0:assertion");
//		writer.writeNamespace("saml","urn:oasis:names:tc:SAML:2.0:assertion");
//		writer.writeCharacters(this.appSettings.getIssuer());
//		writer.writeEndElement();
//		
//		writer.writeStartElement("samlp", "NameIDPolicy", "urn:oasis:names:tc:SAML:2.0:protocol");
//		
//		writer.writeAttribute("Format", "urn:oasis:names:tc:SAML:2.0:nameid-format:unspecified");
//		writer.writeAttribute("AllowCreate", "true");
//		writer.writeEndElement();
//		
//		writer.writeStartElement("samlp","RequestedAuthnContext","urn:oasis:names:tc:SAML:2.0:protocol");
//		
//		writer.writeAttribute("Comparison", "exact");
//		writer.writeEndElement();
//		
//		writer.writeStartElement("saml","AuthnContextClassRef","urn:oasis:names:tc:SAML:2.0:assertion");
//		writer.writeNamespace("saml", "urn:oasis:names:tc:SAML:2.0:assertion");
//		writer.writeCharacters("urn:oasis:names:tc:SAML:2.0:ac:classes:PasswordProtectedTransport");
//		writer.writeEndElement();
//		
//		writer.writeEndElement();
//		writer.flush();		
//		
//		// Base64 encode the result
//		byte [] encoded = Base64.encodeBase64Chunked(baos.toByteArray());
//		String result = new String(encoded,Charset.forName("UTF-8"));
//						
//		return result;
//	}
//	
// 	public static String getRidOfCRLF(String what) {
//		String lf = "%0D";
//		String cr = "%0A";
//		String now = lf;
//
//		int index = what.indexOf(now);
//		StringBuffer r = new StringBuffer();
//
//		while (index!=-1) {
//			r.append(what.substring(0,index));
//			what = what.substring(index+3,what.length());
//			
//			if (now.equals(lf)) {
//				now = cr;
//			} else {
//				now = lf;
//			}
//			
//			index = what.indexOf(now);
//		}
//		return r.toString();
//	}		
//
//}
