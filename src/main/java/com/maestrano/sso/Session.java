package com.maestrano.sso;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maestrano.configuration.Preset;
import com.maestrano.configuration.Sso;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.helpers.MnoDateHelper;
import com.maestrano.net.MnoHttpClient;

public class Session {

	private static final String MAESTRANO_SESSION_ID = "maestrano";
	private static final Logger logger = LoggerFactory.getLogger(Session.class);
	private Sso sso;

	private final String uid;
	private final String groupUid;
	private Date recheck;
	private final String sessionToken;

	/**
	 * Constructor retrieving Maestrano session from user for a given ssoService
	 * 
	 * @param sso
	 * @param httpSession
	 * @param user
	 */
	public Session(Preset preset, User user) {
		this.sso = preset.getSso();
		uid = user.getUid();
		groupUid = user.getGroupUid();
		sessionToken = user.getSsoSession();
		recheck = user.getSsoSessionRecheck();
	}

	/**
	 * Empty Session
	 */
	private Session(Preset preset) {
		super();
		this.sso = preset.getSso();
		this.uid = null;
		this.groupUid = null;
		this.recheck = null;
		this.sessionToken = null;
	}

	Session(Preset preset, String uid, String groupUid, Date recheck, String sessionToken) {
		super();
		this.sso = preset.getSso();
		this.uid = uid;
		this.groupUid = groupUid;
		this.recheck = recheck;
		this.sessionToken = sessionToken;
	}

	public static boolean hasSession(HttpSession httpSession) {
		return httpSession.getAttribute(MAESTRANO_SESSION_ID) != null;
	}

	/**
	 * Retrieving Maestrano session from httpSession, returns an empty Session if there is no Session
	 * 
	 * @param marketplace
	 *            marketplace previously configured
	 * @param HttpSession
	 *            httpSession
	 */
	public static Session loadFromHttpSession(Preset preset, HttpSession httpSession) {
		String mnoSessEntry = (String) httpSession.getAttribute(MAESTRANO_SESSION_ID);
		if (mnoSessEntry == null) {
			return new Session(preset);
		}
		Map<String, String> sessionObj;

		try {
			Gson gson = new Gson();
			String decryptedSession = new String(DatatypeConverter.parseBase64Binary(mnoSessEntry), "UTF-8");

			Type type = new TypeToken<Map<String, String>>() {
			}.getType();
			sessionObj = gson.fromJson(decryptedSession, type);
		} catch (Exception e) {
			logger.error("could not deserialized maestrano session: " + mnoSessEntry, e);
			throw new RuntimeException("could not deserialized maestrano session: " + mnoSessEntry, e);
		}

		// Assign attributes
		String uid = sessionObj.get("uid");
		String groupUid = sessionObj.get("group_uid");
		String sessionToken = sessionObj.get("session");
		Date recheck;
		// Session Recheck
		try {
			recheck = MnoDateHelper.fromIso8601(sessionObj.get("session_recheck"));
		} catch (Exception e) {
			recheck = new Date((new Date()).getTime() - 1 * 60 * 1000);
		}
		return new Session(preset, uid, groupUid, recheck, sessionToken);
	}

	/**
	 * Return whether the session is valid or not. Perform remote check to maestrano if recheck is overdue.
	 * 
	 * @return boolean session valid
	 */
	public boolean isValid() {
		return isValid(new MnoHttpClient());
	}

	/**
	 * Return whether the session is valid or not. Perform remote check to maestrano if recheck is overdue.
	 * 
	 * @param MnoHttpClient
	 *            http client
	 * @return boolean session valid
	 */
	public boolean isValid(MnoHttpClient httpClient) {
		if (uid == null) {
			return false;
		}
		if (isRemoteCheckRequired()) {
			if (this.performRemoteCheck(httpClient)) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}

	/**
	 * Save the Maestrano session in HTTP Session
	 */
	public void save(HttpSession httpSession) {
		String sessionSerialized = serializeSession();
		// Finally store the maestrano session
		httpSession.setAttribute(MAESTRANO_SESSION_ID, sessionSerialized);
	}

	/**
	 * Check whether the session should be checked remotely
	 * 
	 * package private for testing
	 * 
	 * @return boolean remote check required
	 */
	boolean isRemoteCheckRequired() {
		return recheck.before(new Date());
	}

	/**
	 * Check whether the remote maestrano session is still valid
	 * 
	 * package private for testing
	 */
	boolean performRemoteCheck(MnoHttpClient httpClient) {
		// Prepare request
		String url = sso.getSessionCheckUrl(this.uid, this.sessionToken);
		String respStr;
		try {
			respStr = httpClient.get(url);
		} catch (AuthenticationException e1) {
			// TODO log error
			e1.printStackTrace();
			return false;
		} catch (ApiException e1) {
			// TODO log error
			e1.printStackTrace();
			return false;
		}

		// Parse response
		Gson gson = new Gson();
		Type type = new TypeToken<Map<String, String>>() {
		}.getType();
		Map<String, String> respObj = gson.fromJson(respStr, type);
		Boolean isValid = (respObj.get("valid") != null && respObj.get("valid").equals("true"));

		if (isValid) {
			try {
				this.recheck = MnoDateHelper.fromIso8601(respObj.get("recheck"));
			} catch (Exception e) {
				return false;
			}

			return true;
		}
		return false;
	}

	private String serializeSession() {
		Map<String, String> sessObj = new HashMap<String, String>();
		sessObj.put("uid", this.uid);
		sessObj.put("session", this.sessionToken);
		sessObj.put("session_recheck", MnoDateHelper.toIso8601(this.recheck));
		sessObj.put("group_uid", this.groupUid);

		// Encode session
		Gson gson = new Gson();
		String sessionSerialized = gson.toJson(sessObj);
		sessionSerialized = DatatypeConverter.printBase64Binary(sessionSerialized.getBytes());
		return sessionSerialized;
	}

	/**
	 * @return the Idp logout url where users should be redirected to upon logging out
	 */
	public String getLogoutUrl() {
		return sso.getLogoutUrl(this.uid);
	}

	public String getUid() {
		return uid;
	}

	public String getGroupUid() {
		return groupUid;
	}

	public Date getRecheck() {
		return recheck;
	}

	public String getSessionToken() {
		return sessionToken;
	}

}
