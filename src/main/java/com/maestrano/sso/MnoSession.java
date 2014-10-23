package com.maestrano.sso;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.http.HttpSession;
import javax.xml.bind.DatatypeConverter;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.maestrano.Maestrano;
import com.maestrano.helpers.MnoDateHelper;
import com.maestrano.net.MnoHttpClient;


public class MnoSession {
	
	private String uid;
	private String groupUid;
	private Date recheck;
	private String sessionToken;
	private HttpSession httpSession;
    
    /**
     * Constructor
     * @param HttpSession httpSession
     */
    public MnoSession(HttpSession httpSession)
    {
        this.httpSession = httpSession;
        
        String mnoSessEntry = (String) httpSession.getAttribute("maestrano");
        
        if (httpSession != null && mnoSessEntry != null)
        {
        	Map<String,String> sessionObj;
        	
            try {
            	Gson gson = new Gson();
            	String decryptedSession = new String(DatatypeConverter.parseBase64Binary(mnoSessEntry),"UTF-8");
                
            	Type type = new TypeToken<Map<String,String>>(){}.getType();
            	sessionObj = gson.fromJson(decryptedSession, type);
            }
            catch (Exception e){
            	sessionObj = new HashMap<String,String>();
            }
            
            // Assign attributes
            uid = sessionObj.get("uid");
            groupUid = sessionObj.get("group_uid");
            sessionToken = sessionObj.get("session");
            
            // Session Recheck
            try
            {
                recheck = MnoDateHelper.fromIso8601(sessionObj.get("session_recheck"));
            }
            catch (Exception e) { 
            	recheck = new Date( (new Date()).getTime() - 1*60*1000);
            }
        }
    }
    
    /**
     * Constructor retrieving Maestrano session from user
     * @param HttpSession httpSession
     * @param MnoUser user
     */
    public MnoSession(HttpSession httpSession, MnoUser user)
    {
        this.httpSession = httpSession;

        if (user != null)
        {
            uid = user.getUid();
            groupUid = user.getGroupUid();
            sessionToken = user.getSsoSession();
            recheck = user.getSsoSessionRecheck();
        }
    }
    
    /**
     * Check whether the session should be checked remotely
     * @return Boolean remote check required
     */
    public Boolean isRemoteCheckRequired()
    {
        if (uid != null && sessionToken != null && recheck != null)
        {
        	return recheck.before(new Date());
        }
        return true;
    }

    /**
     * Check whether the remote maestrano session is still valid
     * @param httpClient Maestrano http client
     * @return Boolean session valid
     */
    public Boolean performRemoteCheck() {
    	return performRemoteCheck(new MnoHttpClient());
    }
    
    /**
     * Check whether the remote maestrano session is still valid
     * @param httpClient Maestrano http client
     * @return Boolean session valid
     */
    public Boolean performRemoteCheck(MnoHttpClient httpClient)
    {
        if (uid != null && sessionToken != null && !uid.isEmpty() && !sessionToken.isEmpty())
        {
            // Prepare request
        	String url = Maestrano.ssoService().getSessionCheckUrl(this.uid, this.sessionToken);
        	String respStr;
        	try {
        		respStr = httpClient.get(url);
        	} catch (IOException e) {
        		return false;
        	}
        	
        	// Parse response
        	Gson gson = new Gson();
        	Type type = new TypeToken<Map<String,String>>(){}.getType();
        	Map<String,String> respObj = gson.fromJson(respStr, type);
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
     * Return whether the session is valid or not. Perform remote check to 
     * maestrano if recheck is overdue.
     * @return Boolean session valid
     */
    public Boolean isValid()
    {
        return this.isValid(false);
    }
    
    /**
     * Return wether the session is valid or not. Perform remote check to maestrano 
     * if recheck is overdue.
     * @param ifSession If set to true then session return false ONLY if maestrano session exists and is invalid
     * @return Boolean session valid
     */
    public Boolean isValid(Boolean ifSession) {
    	return isValid(ifSession, new MnoHttpClient());
    }
    
    /**
     * Return wether the session is valid or not. Perform remote check to maestrano 
     * if recheck is overdue.
     * @param ifSession If set to true then session return false ONLY if maestrano session exists and is invalid
     * @param Maestrano http client
     * @return Boolean session valid
     */
    public Boolean isValid(Boolean ifSession, MnoHttpClient httpClient)
    {
        // Return true automatically if SLO is disabled
        if (!Maestrano.ssoService().getSloEnabled()) return true;

        // Return true if maestrano session not set
        // and ifSession option enabled
        if (ifSession && (httpSession == null || httpSession.getAttribute("maestrano") == null))
            return true;

        // Return false if HttpSession is nil
        if (httpSession == null) return false;

        if (isRemoteCheckRequired())
        {
            if (this.performRemoteCheck(httpClient))
            {
                save();
                return true;
            }
            else
            {
                return false;
            }
        }
        return true;
    }

    /// <summary>
    /// Save the Maestrano session in
    /// HTTP Session
    /// </summary>
    public void save()
    {
        Map <String,String> sessObj = new HashMap<String,String>();
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

	public void setHttpSession(HttpSession httpSession) {
		this.httpSession = httpSession;
	}
}
