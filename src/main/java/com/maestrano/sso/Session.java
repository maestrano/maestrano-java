package com.maestrano.sso;

import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maestrano.Maestrano;
import com.maestrano.configuration.Preset;
import com.maestrano.configuration.Sso;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.exception.MnoConfigurationException;
import com.maestrano.exception.MnoException;
import com.maestrano.helpers.MnoDateHelper;
import com.maestrano.net.MnoHttpClient;

public class Session {

	private final Sso sso;
	private final HttpSession httpSession;

	private String uid;
	private String groupUid;
	private Date recheck;
	private String sessionToken;

	/**
	 * Constructor retrieving Maestrano session from httpSession for a given maestrano configuration
	 * 
	 * @param maestrano
	 *            Maestrano configuration for a given marketplace
	 * @param HttpSession
	 *            httpSession
	 */

	public Session(Preset preset, HttpSession httpSession) {
		this(preset.getSso(), httpSession);
	}

	/**
	 * Constructor retrieving Maestrano session from httpSession for a given marketplace
	 * 
	 * @param marketplace
	 *            marketplace previously configured
	 * @param HttpSession
	 *            httpSession
	 */

	public Session(String marketplace, HttpSession httpSession) throws MnoConfigurationException {
		this(Maestrano.get(marketplace), httpSession);
	}

	public Session(Sso sso, HttpSession httpSession) {
		this.sso = sso;
		this.httpSession = httpSession;

		String mnoSessEntry = (String) httpSession.getAttribute("maestrano");

		if (httpSession != null && mnoSessEntry != null) {
			Map<String, String> sessionObj;

			try {
				Gson gson = new Gson();
				String decryptedSession = new String(DatatypeConverter.parseBase64Binary(mnoSessEntry), "UTF-8");

				Type type = new TypeToken<Map<String, String>>() {
				}.getType();
				sessionObj = gson.fromJson(decryptedSession, type);
			} catch (Exception e) {
				sessionObj = new HashMap<String, String>();
			}

			// Assign attributes
			uid = sessionObj.get("uid");
			groupUid = sessionObj.get("group_uid");
			sessionToken = sessionObj.get("session");

			// Session Recheck
			try {
				recheck = MnoDateHelper.fromIso8601(sessionObj.get("session_recheck"));
			} catch (Exception e) {
				recheck = new Date((new Date()).getTime() - 1 * 60 * 1000);
			}
		}
	}

	/**
	 * Constructor retrieving Maestrano session from user for a given maestrano configuration
	 * 
	 * @param maestrano
	 *            Maestrano configuration for a given marketplace
	 * @param HttpSession
	 *            httpSession
	 * @param User
	 *            user
	 * @throws MnoConfigurationException
	 */
	public Session(Preset preset, HttpSession httpSession, User user) {
		this(preset.getSso(), httpSession, user);
	}

	/**
	 * Constructor retrieving Maestrano session from user for a given maestrano configuration
	 * 
	 * @param marketplace
	 *            configuration marketplace
	 * @param HttpSession
	 *            httpSession
	 * @param User
	 *            user
	 * @throws MnoConfigurationException
	 */
	public Session(String marketplace, HttpSession httpSession, User user) throws MnoConfigurationException {
		this(Maestrano.get(marketplace), httpSession, user);
	}

	/**
	 * Constructor retrieving Maestrano session from user for a given ssoService
	 * 
	 * @param sso
	 * @param httpSession
	 * @param user
	 */
	public Session(Sso sso, HttpSession httpSession, User user) {
		this.sso = sso;
		this.httpSession = httpSession;

		if (user != null) {
			uid = user.getUid();
			groupUid = user.getGroupUid();
			sessionToken = user.getSsoSession();
			recheck = user.getSsoSessionRecheck();
		}
	}

	/**
	 * Check whether the session should be checked remotely
	 * 
	 * @return Boolean remote check required
	 */
	public boolean isRemoteCheckRequired() {
		if (uid != null && sessionToken != null && recheck != null) {
			return recheck.before(new Date());
		} else {
			return true;
		}
	}

	/**
	 * Check whether the remote maestrano session is still valid
	 * 
	 * @param httpClient
	 *            Maestrano http client
	 * @return Boolean session valid
	 * @throws MnoException
	 */
	public boolean performRemoteCheck() throws MnoException {
		return performRemoteCheck(new MnoHttpClient());
	}

	/**
	 * Check whether the remote maestrano session is still valid
	 * 
	 * @param httpClient
	 *            Maestrano http client
	 * @return Boolean session valid
	 * @throws ApiException
	 * @throws AuthenticationException
	 * @throws MnoException
	 */
	public boolean performRemoteCheck(MnoHttpClient httpClient) {
		if (uid != null && sessionToken != null && !uid.isEmpty() && !sessionToken.isEmpty()) {
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
		}
		return false;
	}

	/**
	 * Return whether the session is valid or not. Perform remote check to maestrano if recheck is overdue.
	 * 
	 * @return Boolean session valid
	 * @throws MnoException
	 */
	public boolean isValid() {
		return this.isValid(false);
	}

	/**
	 * Return wether the session is valid or not. Perform remote check to maestrano if recheck is overdue.
	 * 
	 * @param ifSession
	 *            If set to true then session return false ONLY if maestrano session exists and is invalid
	 * @return Boolean session valid
	 * @throws MnoException
	 */
	public boolean isValid(boolean ifSession) {
		return isValid(ifSession, new MnoHttpClient());
	}

	/**
	 * Return wether the session is valid or not. Perform remote check to maestrano if recheck is overdue.
	 * 
	 * @param ifSession
	 *            If set to true then session return false ONLY if maestrano session exists and is invalid
	 * @param Maestrano
	 *            http client
	 * @return Boolean session valid
	 * @throws MnoException
	 */
	public boolean isValid(boolean ifSession, MnoHttpClient httpClient) {

		// Return true if maestrano session not set
		// and ifSession option enabled
		if (ifSession && (httpSession == null || httpSession.getAttribute("maestrano") == null)) {
			return true;
		}

		// Return false if HttpSession is nil
		if (httpSession == null) {
			return false;
		}

		if (isRemoteCheckRequired()) {
			if (this.performRemoteCheck(httpClient)) {
				save();
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
	public void save() {
		Map<String, String> sessObj = new HashMap<String, String>();
		sessObj.put("uid", this.uid);
		sessObj.put("session", this.sessionToken);
		sessObj.put("session_recheck", MnoDateHelper.toIso8601(this.recheck));
		sessObj.put("group_uid", this.groupUid);

		// Encode session
		Gson gson = new Gson();
		String sessStr = gson.toJson(sessObj);
		sessStr = DatatypeConverter.printBase64Binary(sessStr.getBytes());

		// Finally store the maestrano session
		httpSession.setAttribute("maestrano", sessStr);
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

	public HttpSession getHttpSession() {
		return httpSession;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public void setGroupUid(String groupUid) {
		this.groupUid = groupUid;
	}

	public void setRecheck(Date recheck) {
		this.recheck = recheck;
	}

	public void setSessionToken(String sessionToken) {
		this.sessionToken = sessionToken;
	}
}
