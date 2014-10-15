package com.maestrano.saml;

import java.io.ByteArrayInputStream;
import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;

import javax.xml.bind.DatatypeConverter;

public class Certificate {

  private X509Certificate x509Cert;

  /**
   * Loads certificate from a base64 encoded string
   * @param certificate an encoded base64 byte array.
   * @throws CertificateException In case it cannot load the certificate.
   */
   public void loadCertificate(String certificate) throws CertificateException {
    CertificateFactory fty = CertificateFactory.getInstance("X.509");
    
    ByteArrayInputStream bais = new ByteArrayInputStream(DatatypeConverter.parseBase64Binary(certificate));

    x509Cert = (X509Certificate)fty.generateCertificate(bais);
  }

  /**
   * Loads a certificate from a encoded base64 byte array.
   * @param certificate an encoded base64 byte array.
   * @throws CertificateException In case it cannot load the certificate.
   * @throws UnsupportedEncodingException 
   */
  public void loadCertificate(byte[] certificate) throws CertificateException, UnsupportedEncodingException {
    String certAsString = new String(certificate, "UTF-8");
    
    this.loadCertificate(certAsString);

  }

  public X509Certificate getX509Cert() {
    return x509Cert;
  }
}