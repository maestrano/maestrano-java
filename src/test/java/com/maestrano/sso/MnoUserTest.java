package com.maestrano.sso;

import static org.junit.Assert.assertEquals;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;
import com.maestrano.exception.MnoException;
import com.maestrano.saml.Response;
import com.maestrano.testhelpers.SamlMnoRespStub;

public class MnoUserTest {
	private Properties props = new Properties();
	private Response samlResp;
	private MnoUser subject;

	@Before
	public void beforeEach() throws Exception {
		props.setProperty("app.environment", "production");
		props.setProperty("app.host", "https://mysuperapp.com");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		
		Maestrano.reloadConfiguration(props);

		samlResp = (Response) new SamlMnoRespStub();
		subject = new MnoUser(samlResp);
	}

	@Test
	public void constructor_itExtractsTheRightAttributesFromTheSamlResponse() throws ParseException
	{
		SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy-MM-dd'T'H:mm:ssZ");

		Map<String,String> att = samlResp.getAttributes();

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

	@Test
	public void toUid_whenReal_itReturnsTheRightUid() throws MnoException
	{
		props.setProperty("sso.creationMode", "real");
		
		Maestrano.reloadConfiguration(props);

		assertEquals(subject.getUid(),subject.toUid());
	}

	@Test
	public void toUid_whenVirtual_itReturnsTheRightUid() throws MnoException
	{
		props.setProperty("sso.creationMoMaestranode", "virtual");
		
		Maestrano.reloadConfiguration(props);
		
		assertEquals(subject.getVirtualUid(),subject.toUid());
	}

	@Test
	public void toEmail_whenReal_itReturnsTheRightEmail() throws MnoException
	{
		props.setProperty("sso.creationMode", "real");
		
		Maestrano.reloadConfiguration(props);

		assertEquals(subject.getEmail(),subject.toEmail());
	}

	@Test
	public void toEmail_whenVirtual_itReturnsTheRightEmail() throws MnoException
	{
		props.setProperty("sso.creationMode", "virtual");
		
		Maestrano.reloadConfiguration(props);

		assertEquals(subject.getVirtualEmail(),subject.toEmail());
	}


}
