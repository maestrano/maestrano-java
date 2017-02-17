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

import com.maestrano.configuration.Preset;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.helpers.MnoDateHelper;
import com.maestrano.testhelpers.DefaultPropertiesHelper;
import com.maestrano.testhelpers.HttpSessionStub;
import com.maestrano.testhelpers.MnoHttpClientStub;
import com.maestrano.testhelpers.SamlMnoRespStub;

public class MnoSessionTest {
	private Properties properties;
	private Map<String, String> testSessObj;
	private HttpSessionStub httpSession;
	private Session subject;
	private MnoHttpClientStub httpClient;
	private Preset preset;

	@Before
	public void beforeEach() throws Exception {
		properties = DefaultPropertiesHelper.loadDefaultProperties();

		properties.setProperty("environment", "production");
		properties.setProperty("app.host", "https://mysuperapp.com");
		properties.setProperty("api.id", "someid");
		properties.setProperty("api.key", "somekey");
		properties.setProperty("sso.sloEnabled", "true");
		this.preset = new Preset("test", properties);

		testSessObj = new HashMap<String, String>();
		testSessObj.put("uid", "usr-1");
		testSessObj.put("group_uid", "cld-1");
		testSessObj.put("session", "sessiontoken");
		testSessObj.put("session_recheck", "2014-06-22T01:00:00Z");

		httpClient = new MnoHttpClientStub();
		httpSession = new HttpSessionStub();
		httpSession.setMnoSessionTo(testSessObj);
	}

	@Test
	public void itContructsAnInstanceFromHttpSession() throws ParseException {
		httpSession.setMnoSessionTo(testSessObj);
		subject = new Session(preset, httpSession);

		assertEquals(httpSession, subject.getHttpSession());
		assertEquals(testSessObj.get("uid"), subject.getUid());
		assertEquals(testSessObj.get("group_uid"), subject.getGroupUid());
		assertEquals(testSessObj.get("session"), subject.getSessionToken());
		assertEquals(MnoDateHelper.fromIso8601(testSessObj.get("session_recheck")), subject.getRecheck());
	}

	@Test
	public void itContructsAnInstanceFromHttpSessionWithPreset() throws ParseException, MnoConfigurationException {

		Properties otherProps = DefaultPropertiesHelper.loadDefaultProperties();
		otherProps.setProperty("environment", "theotherproduction");
		otherProps.setProperty("app.host", "https://theothersuperapp.com");
		otherProps.setProperty("api.id", "anotherId");
		otherProps.setProperty("api.key", "anotherKey");
		otherProps.setProperty("sso.sloEnabled", "false");
		Preset otherPreset = new Preset("other", otherProps);

		httpSession.setMnoSessionTo(testSessObj);
		subject = new Session(otherPreset, httpSession);

		assertEquals(httpSession, subject.getHttpSession());
		assertEquals(testSessObj.get("uid"), subject.getUid());
		assertEquals(testSessObj.get("group_uid"), subject.getGroupUid());
		assertEquals(testSessObj.get("session"), subject.getSessionToken());
		assertEquals(MnoDateHelper.fromIso8601(testSessObj.get("session_recheck")), subject.getRecheck());
	}

	@Test
	public void itContructsAnInstanceFromHttpSessionStateObjectAndSsoUser() throws Exception {
		SamlMnoRespStub samlResp = new SamlMnoRespStub();
		User user = new User(samlResp);
		subject = new Session(preset, httpSession, user);

		assertEquals(httpSession, subject.getHttpSession());
		assertEquals(user.getUid(), subject.getUid());
		assertEquals(user.getGroupUid(), subject.getGroupUid());
		assertEquals(user.getSsoSession(), subject.getSessionToken());
		assertEquals(user.getSsoSessionRecheck(), subject.getRecheck());
	}

	@Test
	public void itContructsAnInstanceFromHttpSessionStateObjectAndSsoUserWithPreset() throws Exception {
		SamlMnoRespStub samlResp = new SamlMnoRespStub();
		User user = new User(samlResp);
		subject = new Session(preset, httpSession, user);

		assertEquals(httpSession, subject.getHttpSession());
		assertEquals(user.getUid(), subject.getUid());
		assertEquals(user.getGroupUid(), subject.getGroupUid());
		assertEquals(user.getSsoSession(), subject.getSessionToken());
		assertEquals(user.getSsoSessionRecheck(), subject.getRecheck());
	}

	@Test
	public void isRemoteCheckRequired_ItReturnsTrueIfRecheckIsBeforeNow() {
		Date date = new Date((new Date()).getTime() - 1 * 60 * 1000);
		testSessObj.put("session_recheck", MnoDateHelper.toIso8601(date));
		httpSession.setMnoSessionTo(testSessObj);
		subject = new Session(preset, httpSession);

		// test
		assertTrue(subject.isRemoteCheckRequired());
	}

	@Test
	public void isRemoteCheckRequired_ItReturnsFalseIfRecheckIsAfterNow() {
		Date date = new Date((new Date()).getTime() + 1 * 60 * 1000);
		testSessObj.put("session_recheck", MnoDateHelper.toIso8601(date));
		httpSession.setMnoSessionTo(testSessObj);
		subject = new Session(preset, httpSession);

		// test
		assertFalse(subject.isRemoteCheckRequired());
	}

	@Test
	public void performRemoteCheck_WhenValid_ItShouldReturnTrueAndAssignRecheckIfValid() {
		// Response preparation
		Date date = new Date((new Date()).getTime() + 1 * 60 * 1000);
		Map<String, String> resp = new HashMap<String, String>();
		resp.put("valid", "true");
		resp.put("recheck", MnoDateHelper.toIso8601(date));

		httpClient.setResponseStub(resp);
		subject = new Session(preset, httpSession);

		// Tests
		assertTrue(subject.performRemoteCheck(httpClient));
		assertEquals(MnoDateHelper.toIso8601(date), MnoDateHelper.toIso8601(subject.getRecheck()));
	}

	@Test
	public void performRemoteCheck_WhenInvalid_ItShouldReturnFalseAndLeaveRecheckUnchanged() {
		// Response preparation
		Date date = new Date((new Date()).getTime() + 1 * 60 * 1000);
		Map<String, String> resp = new HashMap<String, String>();
		resp.put("valid", "false");
		resp.put("recheck", MnoDateHelper.toIso8601(date));

		httpClient.setResponseStub(resp);
		subject = new Session(preset, httpSession);
		Date recheck = subject.getRecheck();

		assertFalse(subject.performRemoteCheck(httpClient));
		assertEquals(recheck, subject.getRecheck());
	}

	@Test
	public void save_ItShouldSaveTheMaestranoSessionInHttpSession() {
		httpSession.setMnoSessionTo(testSessObj);
		Session oldSubject = new Session(preset, httpSession);
		oldSubject.setUid(oldSubject.getUid() + "aaa");
		oldSubject.setGroupUid(oldSubject.getGroupUid() + "aaa");
		oldSubject.setSessionToken(oldSubject.getSessionToken() + "aaa");
		oldSubject.setRecheck(new Date(oldSubject.getRecheck().getTime() + 100 * 60 * 1000));
		oldSubject.save();

		subject = new Session(preset, httpSession);
		assertEquals(oldSubject.getUid(), subject.getUid());
		assertEquals(oldSubject.getGroupUid(), subject.getGroupUid());
		assertEquals(oldSubject.getSessionToken(), subject.getSessionToken());
		assertEquals(oldSubject.getRecheck(), subject.getRecheck());
	}


	@Test
	public void isValid_WhenIfSessionSpecifiedAndNoMnoSession_ItShouldReturnTrue() {
		// Http context
		httpSession.setAttribute("maestrano", null);

		// test
		subject = new Session(preset, httpSession);
		assertTrue(subject.isValid(true));
	}

	@Test
	public void isValid_WhenNoRecheckRequired_ItShouldReturnTrue() {
		// Make sure any remote response is negative
		Date date = new Date((new Date()).getTime() + 100 * 60 * 1000);
		Map<String, String> resp = new HashMap<String, String>();
		resp.put("valid", "false");
		resp.put("recheck", MnoDateHelper.toIso8601(date));
		httpClient.setResponseStub(resp);

		// Set local recheck in the future
		Date localRecheck = new Date((new Date()).getTime() + 1 * 60 * 1000);
		subject = new Session(preset, httpSession);
		subject.setRecheck(localRecheck);

		// test
		assertTrue(subject.isValid(false, httpClient));
	}

	@Test
	public void isValid_WhenRecheckRequiredAndValid_ItShouldReturnTrueAndSaveTheSession() {
		// Make sure any remote response is negative
		Date date = new Date((new Date()).getTime() + 100 * 60 * 1000);
		Map<String, String> resp = new HashMap<String, String>();
		resp.put("valid", "true");
		resp.put("recheck", MnoDateHelper.toIso8601(date));
		httpClient.setResponseStub(resp);

		// Set local recheck in the past
		Date localRecheck = new Date((new Date()).getTime() - 1 * 60 * 1000);
		Session oldsubject = new Session(preset, httpSession);
		oldsubject.setRecheck(localRecheck);

		// test 1 - validity
		assertTrue(oldsubject.isValid(false, httpClient));

		// Create a new subject to test session persistence
		subject = new Session(preset, httpSession);

		// test 2 - session persistence
		assertEquals(MnoDateHelper.toIso8601(date), MnoDateHelper.toIso8601(subject.getRecheck()));
	}

	@Test
	public void isValid_WhenRecheckRequiredAndInvalid_ItShouldReturnFalse() {
		// Make sure any remote response is negative
		Date date = new Date((new Date()).getTime() + 100 * 60 * 1000);
		Map<String, String> resp = new HashMap<String, String>();
		resp.put("valid", "false");
		resp.put("recheck", MnoDateHelper.toIso8601(date));
		httpClient.setResponseStub(resp);

		// Set local recheck in the past
		Date localRecheck = new Date((new Date()).getTime() - 1 * 60 * 1000);
		subject = new Session(preset, httpSession);
		subject.setRecheck(localRecheck);

		// test 1 - validity
		assertFalse(subject.isValid(false, httpClient));
	}

	@Test
	public void getLogoutUrl() {
		subject = new Session(preset, httpSession);
		assertEquals("https://api-hub.maestrano.com/app_logout?user_uid=usr-1", subject.getLogoutUrl());
	}
}
