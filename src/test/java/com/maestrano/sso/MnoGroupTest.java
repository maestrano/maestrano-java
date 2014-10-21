package com.maestrano.sso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Map;
import java.util.Properties;
import java.util.TimeZone;

import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

import com.maestrano.Maestrano;
import com.maestrano.saml.Response;
import com.maestrano.testhelpers.SamlMnoRespStub;

public class MnoGroupTest {
	private Properties props = new Properties();
	private Response samlResp;
	private MnoGroup subject;

	@Before
	public void beforeEach() throws Exception {
		props.setProperty("app.environment", "production");
		props.setProperty("app.host", "https://mysuperapp.com");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		Maestrano.configure(props);

		samlResp = (Response) new SamlMnoRespStub();
		subject = new MnoGroup(samlResp);
	}

	@Test
	public void ItShouldExtractTheRightAttributesFromTheSamlResponse() throws ParseException
	{
		SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy-MM-dd'T'H:mm:ssZ");
		Map<String,String> att = samlResp.getAttributes();

		assertEquals(att.get("group_uid"), subject.getUid());
		assertEquals(att.get("group_name"), subject.getName());
		assertEquals(att.get("group_email"), subject.getEmail());
		assertEquals(simpleDf.parse(att.get("group_end_free_trial")), subject.getFreeTrialEndAt());
		assertEquals(att.get("company_name"), subject.getCompanyName());
		assertEquals(att.get("group_has_credit_card").equals("true"), subject.hasCreditCard());

		assertEquals(att.get("group_currency"), subject.getCurrency());
		assertEquals(TimeZone.getTimeZone(att.get("group_timezone")), subject.getTimezone());
		assertEquals(att.get("group_country"), subject.getCountry());
		assertEquals(att.get("group_city"), subject.getCity());
	}
}
