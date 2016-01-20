package com.maestrano.sso;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.junit.Before;
import org.junit.Test;

import com.maestrano.Maestrano;
import com.maestrano.exception.MnoException;
import com.maestrano.helpers.MnoDateHelper;
import com.maestrano.testhelpers.HttpSessionStub;
import com.maestrano.testhelpers.MnoHttpClientStub;
import com.maestrano.testhelpers.SamlMnoRespStub;

public class MnoSessionTest {
	private Properties props = new Properties();
	private Map<String,String> testSessObj;
	private HttpSessionStub httpSession;
	private MnoSession subject;
	private MnoHttpClientStub httpClient;
	
	@Before
	public void beforeEach() throws Exception {
		props.setProperty("app.environment", "production");
		props.setProperty("app.host", "https://mysuperapp.com");
		props.setProperty("api.id", "someid");
		props.setProperty("api.key", "somekey");
		props.setProperty("sso.sloEnabled", "true");
		Maestrano.reloadConfiguration(props);
		
		testSessObj = new HashMap<String,String>();
		testSessObj.put("uid","usr-1");
		testSessObj.put("group_uid", "cld-1");
		testSessObj.put("session", "sessiontoken");
		testSessObj.put("session_recheck", "2014-06-22T01:00:00Z");
		
		httpClient = new MnoHttpClientStub();
		httpSession = new HttpSessionStub();
		httpSession.setMnoSessionTo(testSessObj);
	}
	
	@Test
	public void itContructsAnInstanceFromHttpSession() throws ParseException
	{
		httpSession.setMnoSessionTo(testSessObj);
		subject = new MnoSession(httpSession);

		assertEquals(httpSession, subject.getHttpSession());
		assertEquals(testSessObj.get("uid"), subject.getUid());
		assertEquals(testSessObj.get("group_uid"), subject.getGroupUid());
		assertEquals(testSessObj.get("session"), subject.getSessionToken());
		assertEquals(MnoDateHelper.fromIso8601(testSessObj.get("session_recheck")), subject.getRecheck());
	}

	@Test
	public void itContructsAnInstanceFromHttpSessionStateObjectAndSsoUser() throws Exception
	{
		SamlMnoRespStub samlResp = new SamlMnoRespStub();
		MnoUser user = new MnoUser(samlResp);
		subject = new MnoSession(httpSession, user);

		assertEquals(httpSession, subject.getHttpSession());
		assertEquals(user.getUid(), subject.getUid());
		assertEquals(user.getGroupUid(), subject.getGroupUid());
		assertEquals(user.getSsoSession(), subject.getSessionToken());
		assertEquals(user.getSsoSessionRecheck(), subject.getRecheck());
	}

	@Test
	public void isRemoteCheckRequired_ItReturnsTrueIfRecheckIsBeforeNow()
	{
		Date date = new Date( (new Date()).getTime() - 1*60*1000);
		testSessObj.put("session_recheck", MnoDateHelper.toIso8601(date));
		httpSession.setMnoSessionTo(testSessObj);
		subject = new MnoSession(httpSession);

		// test
		assertTrue(subject.isRemoteCheckRequired());
	}

	@Test
	public void isRemoteCheckRequired_ItReturnsFalseIfRecheckIsAfterNow()
	{
		Date date = new Date( (new Date()).getTime() + 1*60*1000);
		testSessObj.put("session_recheck", MnoDateHelper.toIso8601(date));
		httpSession.setMnoSessionTo(testSessObj);
		subject = new MnoSession(httpSession);

		// test
		assertFalse(subject.isRemoteCheckRequired());
	}

	@Test
	public void performRemoteCheck_WhenValid_ItShouldReturnTrueAndAssignRecheckIfValid()
	{   
		// Response preparation
		Date date = new Date( (new Date()).getTime() + 1*60*1000);
		Map<String,String> resp = new HashMap<String,String>();
		resp.put("valid", "true");
		resp.put("recheck", MnoDateHelper.toIso8601(date));
		
		httpClient.setResponseStub(resp);
		subject = new MnoSession(httpSession);

		// Tests
		assertTrue(subject.performRemoteCheck(httpClient));
		assertEquals(MnoDateHelper.toIso8601(date), MnoDateHelper.toIso8601(subject.getRecheck()));
	}

	@Test
	public void performRemoteCheck_WhenInvalid_ItShouldReturnFalseAndLeaveRecheckUnchanged()
	{
		// Response preparation
		Date date = new Date( (new Date()).getTime() + 1*60*1000);
		Map<String,String> resp = new HashMap<String,String>();
		resp.put("valid", "false");
		resp.put("recheck", MnoDateHelper.toIso8601(date));

		httpClient.setResponseStub(resp);
		subject = new MnoSession(httpSession);
		Date recheck = subject.getRecheck();
		
		assertFalse(subject.performRemoteCheck(httpClient));
		assertEquals(recheck, subject.getRecheck());
	}


	@Test
	public void save_ItShouldSaveTheMaestranoSessionInHttpSession()
	{
		httpSession.setMnoSessionTo(testSessObj);
		MnoSession oldSubject = new MnoSession(httpSession);
		oldSubject.setUid(oldSubject.getUid() + "aaa");
		oldSubject.setGroupUid(oldSubject.getGroupUid() + "aaa");
		oldSubject.setSessionToken(oldSubject.getSessionToken() + "aaa");
		oldSubject.setRecheck(new Date(oldSubject.getRecheck().getTime() + 100*60*1000));
		oldSubject.save();
		
		subject = new MnoSession(httpSession);
		assertEquals(oldSubject.getUid(), subject.getUid());
		assertEquals(oldSubject.getGroupUid(), subject.getGroupUid());
		assertEquals(oldSubject.getSessionToken(), subject.getSessionToken());
		assertEquals(oldSubject.getRecheck(), subject.getRecheck());
	}

	@Test
	public void isValid_WhenSloDisabled_ItShouldReturnTrue() throws MnoException
	{
		// Disable SLO
		props.setProperty("sso.sloEnabled", "false");
		Maestrano.reloadConfiguration(props);
		
		// Response preparation (session not valid)
		Date date = new Date( (new Date()).getTime() + 1*60*1000);
		Map<String,String> resp = new HashMap<String,String>();
		resp.put("valid", "false");
		resp.put("recheck", MnoDateHelper.toIso8601(date));
		httpClient.setResponseStub(resp);
		
		// Set local recheck to force remote recheck
		Date localRecheck = new Date( (new Date()).getTime() - 1*60*1000);
		subject = new MnoSession(httpSession);
		subject.setRecheck(localRecheck);
		
		assertTrue(subject.isValid(false,httpClient));
	}

	@Test
	public void isValid_WhenIfSessionSpecifiedAndNoMnoSession_ItShouldReturnTrue()
	{
		// Http context
		httpSession.setAttribute("maestrano", null);

		// test
		subject = new MnoSession(httpSession);
		assertTrue(subject.isValid(true));
	}

	@Test
	public void isValid_WhenNoRecheckRequired_ItShouldReturnTrue()
	{	
		// Make sure any remote response is negative
		Date date = new Date( (new Date()).getTime() + 100*60*1000);
		Map<String,String> resp = new HashMap<String,String>();
		resp.put("valid", "false");
		resp.put("recheck", MnoDateHelper.toIso8601(date));
		httpClient.setResponseStub(resp);
		
		// Set local recheck in the future
		Date localRecheck = new Date( (new Date()).getTime() + 1*60*1000);
		subject = new MnoSession(httpSession);
		subject.setRecheck(localRecheck);

		// test
		assertTrue(subject.isValid(false,httpClient));
	}

	@Test
	public void isValid_WhenRecheckRequiredAndValid_ItShouldReturnTrueAndSaveTheSession()
	{
		// Make sure any remote response is negative
		Date date = new Date( (new Date()).getTime() + 100*60*1000);
		Map<String,String> resp = new HashMap<String,String>();
		resp.put("valid", "true");
		resp.put("recheck", MnoDateHelper.toIso8601(date));
		httpClient.setResponseStub(resp);

		// Set local recheck in the past
		Date localRecheck = new Date( (new Date()).getTime() - 1*60*1000);
		MnoSession oldsubject = new MnoSession(httpSession);
		oldsubject.setRecheck(localRecheck);
		
		// test 1 - validity
		assertTrue(oldsubject.isValid(false,httpClient));
		
		// Create a new subject to test session persistence
		subject = new MnoSession(httpSession);
		
		// test 2 - session persistence
		assertEquals(MnoDateHelper.toIso8601(date), MnoDateHelper.toIso8601(subject.getRecheck()));
	}

	@Test
	public void isValid_WhenRecheckRequiredAndInvalid_ItShouldReturnFalse()
	{
		// Make sure any remote response is negative
		Date date = new Date( (new Date()).getTime() + 100*60*1000);
		Map<String,String> resp = new HashMap<String,String>();
		resp.put("valid", "false");
		resp.put("recheck", MnoDateHelper.toIso8601(date));
		httpClient.setResponseStub(resp);

		// Set local recheck in the past
		Date localRecheck = new Date( (new Date()).getTime() - 1*60*1000);
		subject = new MnoSession(httpSession);
		subject.setRecheck(localRecheck);

		// test 1 - validity
		assertFalse(subject.isValid(false,httpClient));
	}
}
