package com.maestrano.saml;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.xml.bind.DatatypeConverter;

public class Certificate {

	private final X509Certificate x509Cert;

	/**
	 * load certificate from a base64 encoded string
	 * 
	 * @param certificate
	 *            an encoded base64 byte array.
	 * @throws CertificateException
	 *             In case it cannot load the certificate.
	 */
	public Certificate(String certificate) throws CertificateException {
		this.x509Cert = loadCertificate(certificate);
	}

	/**
	 * load a certificate from a encoded base64 byte array.
	 * 
	 * @param certificate
	 *            an encoded base64 byte array.
	 * @throws CertificateException
	 *             In case it cannot load the certificate.
	 * @throws UnsupportedEncodingException
	 */
	public Certificate(byte[] certificate) throws CertificateException, UnsupportedEncodingException {
		String certAsString = new String(certificate, "UTF-8");

		this.x509Cert = loadCertificate(certAsString);
	}

	private static X509Certificate loadCertificate(String certificate) throws CertificateException {
		String cleanCert = certificate.replaceAll("-----BEGIN CERTIFICATE-----(\n)*", "");
		cleanCert = cleanCert.replaceAll("(\n)*-----END CERTIFICATE-----(\n)*", "");

		CertificateFactory fty = CertificateFactory.getInstance("X.509");

		ByteArrayInputStream bais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(cleanCert));

		return (X509Certificate) fty.generateCertificate(bais);
	}

	public X509Certificate getX509Cert() {
		return x509Cert;
	}
}