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
import com.maestrano.helpers.MnoDateHelper;
import com.maestrano.testhelpers.DefaultPropertiesHelper;
import com.maestrano.testhelpers.HttpSessionStub;
import com.maestrano.testhelpers.MnoHttpClientStub;
import com.maestrano.testhelpers.SamlMnoRespStub;

public class SessionTest {
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
		subject = Session.loadFromHttpSession(preset, httpSession);

		assertEquals(testSessObj.get("uid"), subject.getUid());
		assertEquals(testSessObj.get("group_uid"), subject.getGroupUid());
		assertEquals(testSessObj.get("session"), subject.getSessionToken());
		assertEquals(MnoDateHelper.fromIso8601(testSessObj.get("session_recheck")), subject.getRecheck());
	}

	@Test
	public void itContructsAnInstanceFromHttpSessionStateObjectAndSsoUser() throws Exception {
		SamlMnoRespStub samlResp = new SamlMnoRespStub();
		User user = new User(samlResp);
		subject =  new Session(preset, user);

		assertEquals(user.getUid(), subject.getUid());
		assertEquals(user.getGroupUid(), subject.getGroupUid());
		assertEquals(user.getSsoSession(), subject.getSessionToken());
		assertEquals(user.getSsoSessionRecheck(), subject.getRecheck());
	}

	@Test
	public void itContructsAnInstanceFromHttpSessionStateObjectAndSsoUserWithPreset() throws Exception {
		SamlMnoRespStub samlResp = new SamlMnoRespStub();
		User user = new User(samlResp);
		subject = new Session(preset, user);

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
		subject = Session.loadFromHttpSession(preset, httpSession);

		// test
		assertTrue(subject.isRemoteCheckRequired());
	}

	@Test
	public void isRemoteCheckRequired_ItReturnsFalseIfRecheckIsAfterNow() {
		Date date = new Date((new Date()).getTime() + 1 * 60 * 1000);
		testSessObj.put("session_recheck", MnoDateHelper.toIso8601(date));
		httpSession.setMnoSessionTo(testSessObj);
		subject = Session.loadFromHttpSession(preset, httpSession);

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
		subject = Session.loadFromHttpSession(preset, httpSession);

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
		subject = Session.loadFromHttpSession(preset, httpSession);
		Date recheck = subject.getRecheck();

		assertFalse(subject.performRemoteCheck(httpClient));
		assertEquals(recheck, subject.getRecheck());
	}

	@Test
	public void save_ItShouldSaveTheMaestranoSessionInHttpSession() {
		httpSession.setMnoSessionTo(testSessObj);
		Session oldSubject = Session.loadFromHttpSession(preset, httpSession);

		String uid = oldSubject.getUid() + "aaa";
		String groupUid = oldSubject.getGroupUid() + "aaa";
		String sessionToken = oldSubject.getSessionToken() + "aaa";
		Date recheck = new Date(oldSubject.getRecheck().getTime() + 100 * 60 * 1000);
		Session session = new Session(preset, uid, groupUid, recheck, sessionToken);
		session.save(httpSession);

		subject = Session.loadFromHttpSession(preset, httpSession);
		assertEquals(session.getUid(), subject.getUid());
		assertEquals(session.getGroupUid(), subject.getGroupUid());
		assertEquals(session.getSessionToken(), subject.getSessionToken());
		assertEquals(session.getRecheck(), subject.getRecheck());
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
		subject = new Session(preset, null, null, localRecheck, null);

		// test 1 - validity
		assertFalse(subject.isValid(httpClient));
	}

	@Test
	public void getLogoutUrl() {
		subject = Session.loadFromHttpSession(preset, httpSession);
		assertEquals("https://api-hub.maestrano.com/app_logout?user_uid=usr-1", subject.getLogoutUrl());
	}
}
