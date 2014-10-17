package com.maestrano.saml;

import java.io.UnsupportedEncodingException;
import java.security.cert.CertificateException;

import javax.xml.bind.DatatypeConverter;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import com.maestrano.saml.Certificate;
import com.maestrano.testhelpers.SamlCertHelper;

import static org.junit.Assert.assertEquals;

public class CertificateTest {
  private static Certificate certificate;
  private static String certStr;
  
  @BeforeClass
  public static void beforeAll() {
	  certStr = "MIIDezCCAuSgAwIBAgIJAOehBr+YIrhjMA0GCSqGSIb3DQEBBQUAMIGGMQswCQYDVQQGEwJBVTEMMAoGA1UECBMDTlNXMQ8wDQYDVQQHEwZTeWRuZXkxGjAYBgNVBAoTEU1hZXN0cmFubyBQdHkgTHRkMRYwFAYDVQQDEw1tYWVzdHJhbm8uY29tMSQwIgYJKoZIhvcNAQkBFhVzdXBwb3J0QG1hZXN0cmFuby5jb20wHhcNMTQwMTA0MDUyMjM5WhcNMzMxMjMwMDUyMjM5WjCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEPMA0GA1UEBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQGA1UEAxMNbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVzdHJhbm8uY29tMIGfMA0GCSqGSIb3DQEBAQUAA4GNADCBiQKBgQDVkIqo5t5PafluP2zbSbzxn29n6HxKnTcsubycLBEs0jkTkdG7seF1LPqnXl8jFM9NGPiBFkiaR15I5w482IW6mC7s8T2CbZEL3qqQEAzztEPnxQg0twswyIZWNyuHYzf9fw0AnohBhGu228EZWaezzT2F333FOVGSsTn1+u6tFwIDAQABo4HuMIHrMB0GA1UdDgQWBBSvrNxoeHDm9nhKnkdpe0lZjYD1GzCBuwYDVR0jBIGzMIGwgBSvrNxoeHDm9nhKnkdpe0lZjYD1G6GBjKSBiTCBhjELMAkGA1UEBhMCQVUxDDAKBgNVBAgTA05TVzEPMA0GA1UEBxMGU3lkbmV5MRowGAYDVQQKExFNYWVzdHJhbm8gUHR5IEx0ZDEWMBQGA1UEAxMNbWFlc3RyYW5vLmNvbTEkMCIGCSqGSIb3DQEJARYVc3VwcG9ydEBtYWVzdHJhbm8uY29tggkA56EGv5giuGMwDAYDVR0TBAUwAwEB/zANBgkqhkiG9w0BAQUFAAOBgQCcMPgV0CpumKRMulOeZwdpnyLQI/NTr3VVHhDDxxCzcB0zlZ2xyDACGnIG2cQJJxfc2GcsFnb0BMw48K6TEhAaV92Q7bt1/TYRvprvhxUNMX2N8PHaYELFG2nWfQ4vqxESRkjkjqy+H7vir/MOF3rlFjiv5twAbDKYHXDT7v1YCg==";
  }

  @Before
  public void beforeEach() 
  {
    certificate = new Certificate();
  }

  @Test
  public void itLoadsACertificateFromString() throws CertificateException
  {
	  certificate.loadCertificate(certStr);
	  assertEquals(certStr,DatatypeConverter.printBase64Binary(certificate.getX509Cert().getEncoded()));
  }

  @Test
  public void itLoadsACertificateFromByteArray() throws CertificateException, UnsupportedEncodingException
  {
	  certificate.loadCertificate(certStr.getBytes());
	  assertEquals(certStr,DatatypeConverter.printBase64Binary(certificate.getX509Cert().getEncoded()));
  }
  
  @Test
  public void itLoadsACertificateWithHeaders() throws CertificateException {
	  // no error raised
	  certificate.loadCertificate(SamlCertHelper.certificate1());
  }
}




