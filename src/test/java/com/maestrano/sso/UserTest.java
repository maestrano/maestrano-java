package com.maestrano.sso;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.saml.Response;
import com.maestrano.testhelpers.SamlMnoRespStub;

public class UserTest {
	private Response samlResp;
	private User subject;

	@Before
	public void beforeEach() throws Exception {

		samlResp = new SamlMnoRespStub();
		subject = new User(samlResp);
	}

	@Test
	public void constructor_itExtractsTheRightAttributesFromTheSamlResponse() throws ParseException {
		SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy-MM-dd'T'H:mm:ssZ");

		Map<String, String> att = samlResp.getAttributes();

		assertEquals(att.get("mno_session"), subject.getSsoSession());
		assertEquals(simpleDf.parse(att.get("mno_session_recheck")), subject.getSsoSessionRecheck());
		assertEquals(att.get("group_uid"), subject.getGroupUid());
		assertEquals(att.get("group_role"), subject.getGroupRole());
		assertEquals(att.get("uid"), subject.getUid());
		assertEquals(att.get("virtual_uid"), subject.getVirtualUid());
		assertEquals(att.get("email"), subject.getEmail());
		assertEquals(att.get("virtual_email"), subject.getVirtualEmail());
		assertEquals(att.get("name"), subject.getFirstName());
		assertEquals(att.get("surname"), subject.getLastName());
		assertEquals(att.get("country"), subject.getCountry());
		assertEquals(att.get("company_name"), subject.getCompanyName());
	}

}
