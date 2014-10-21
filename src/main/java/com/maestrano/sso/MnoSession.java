//package com.maestrano.sso;
//
//import java.io.BufferedReader;
//import java.io.InputStreamReader;
//import java.lang.reflect.Type;
//import java.net.URL;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
//import javax.servlet.http.HttpSession;
//import javax.xml.bind.DatatypeConverter;
//
//import com.google.gson.Gson;
//import com.google.gson.reflect.TypeToken;
//
//
//public class MnoSession {
//	
//	public String uid;
//    public String groupUid;
//    public Date recheck;
//    public String sessionToken;
//    public HttpSession httpSession;
//    
//    /**
//     * Constructor
//     * @param HttpSession httpSession
//     */
//    public MnoSession(HttpSession httpSession)
//    {
//        this.httpSession = httpSession;
//        
//        SimpleDateFormat simpleDf = new SimpleDateFormat("yyyy-MM-dd'T'H:mm:ssZ");
//        String mnoSessEntry = (String) httpSession.getAttribute("maestrano");
//        
//        if (httpSession != null && mnoSessEntry != null)
//        {
//        	Map<String,String> sessionObj;
//        	
//            try {
//            	Gson gson = new Gson();
//            	String decryptedSession = new String(DatatypeConverter.parseBase64Binary(mnoSessEntry),"UTF-8");
//                
//            	Type type = new TypeToken<Map<String,String>>(){}.getType();
//            	sessionObj = gson.fromJson(decryptedSession, type);
//            }
//            catch (Exception e){
//            	sessionObj = new HashMap<String,String>();
//            }
//            
//            // Assign attributes
//            uid = sessionObj.get("uid");
//            groupUid = sessionObj.get("group_uid");
//            sessionToken = sessionObj.get("session");
//            
//            // Session Recheck
//            try
//            {
//                recheck = simpleDf.parse(sessionObj.get("session_recheck"));
//            }
//            catch (Exception e) { 
//            	recheck = new Date( (new Date()).getTime() - 1*60*1000);
//            }
//        }
//    }
//    
//    /**
//     * Constructor retrieving Maestrano session from user
//     * @param HttpSession httpSession
//     * @param MnoUser user
//     */
//    public MnoSession(HttpSession httpSession, MnoUser user)
//    {
//        this.httpSession = httpSession;
//
//        if (user != null)
//        {
//            uid = user.getUid();
//            groupUid = user.getGroupUid();
//            sessionToken = user.getSsoSession();
//            recheck = user.getSsoSessionRecheck();
//        }
//    }
//    
//    /**
//     * Check whether the session should be checked remotely
//     * @return Boolean remote check required
//     */
//    public Boolean isRemoteCheckRequired()
//    {
//        if (uid != null && sessionToken != null && recheck != null)
//        {
//        	return recheck.before(new Date());
//        }
//        return true;
//    }
//
//    
//    /**
//     * Check whether the remote maestrano session is still valid
//     * @param client
//     * @return Boolean session valid
//     */
//    public Boolean performRemoteCheck(RestClient client)
//    {
//        if (uid != null && sessionToken != null && !uid.isEmpty() && !sessionToken.isEmpty())
//        {
//            // Prepare request
//            var request = new RestRequest("api/v1/auth/saml/{id}", Method.GET);
//            request.AddUrlSegment("id", Uid);
//            request.AddParameter("session", SessionToken);
//            JObject resp = new JObject();
//            try {
//                resp = JObject.Parse(client.Execute(request).Content);
//            }
//            catch (Exception) { }
//
//            bool valid = Convert.ToBoolean(resp.Value<String>("valid"));
//            String dateStr = resp.Value<String>("recheck");
//            if ( valid && dateStr != null && dateStr.Length > 0)
//            {
//                Recheck = DateTime.Parse(dateStr);
//                return true;
//            }
//        }
//        return false;
//    }
//    
//    
//
//    /// <summary>
//    /// Check whether the remote maestrano session is still
//    /// valid
//    /// </summary>
//    /// <returns></returns>
//    public Boolean PerformRemoteCheck()
//    {
//        var client = new RestClient(MnoHelper.Sso.Idp);
//        return PerformRemoteCheck(client);
//    }
//
//    /// <summary>
//    /// Return wether the session is valid or not. Perform
//    /// remote check to maestrano if recheck is overdue.
//    /// </summary>
//    /// <param name="client"></param>
//    /// <param name="ifSession"></param>
//    /// <returns></returns>
//    public Boolean IsValid(RestClient client, Boolean ifSession = false)
//    {
//        // Return true automatically if SLO is disabled
//        if (!MnoHelper.Sso.SloEnabled)
//            return true;
//
//        // Return true if maestrano session not set
//        // and ifSession option enabled
//        if (ifSession && (HttpSession == null || HttpSession["maestrano"] == null))
//            return true;
//
//        // Return false if HttpSession is nil
//        if (HttpSession == null)
//            return false;
//
//        if (isRemoteCheckRequired())
//        {
//            if (PerformRemoteCheck(client))
//            {
//                Save();
//                return true;
//            }
//            else
//            {
//                return false;
//            }
//        }
//        return true;
//    }
//
//    /// <summary>
//    /// Return wether the session is valid or not. Perform
//    /// remote check to maestrano if recheck is overdue.
//    /// </summary>
//    /// <param name="ifSession">If set to true then session return false ONLY if maestrano session exists and is invalid</param>
//    /// <returns></returns>
//    public Boolean IsValid(Boolean ifSession = false)
//    {
//        var client = new RestClient(MnoHelper.Sso.Idp);
//        return IsValid(client,ifSession);
//    }
//
//    /// <summary>
//    /// Save the Maestrano session in
//    /// HTTP Session
//    /// </summary>
//    public void Save()
//    {
//        var enc = System.Text.Encoding.UTF8;
//        JObject sessionObject = new JObject(
//            new JProperty("uid",Uid),
//            new JProperty("session",SessionToken),
//            new JProperty("session_recheck",Recheck.ToString("s")),
//            new JProperty("group_uid",GroupUid));
//
//        // Finally store the maestrano session
//        HttpSession["maestrano"] = Convert.ToBase64String(enc.GetBytes(sessionObject.ToString()));
//    }
//    
//    
//    /**
//     * Fetch url and return content. Wrapper function.
//     *
//     * @param string full url to fetch
//     * @return string page content
//     */
//    private String fetchUrl(String url) {
//      String outputLine = "";
//      
//      try {
//        URL urlObj = new URL(url);
//        
//        BufferedReader in = new BufferedReader(
//        new InputStreamReader(urlObj.openStream()));
//        String inputLine;
//      
//      
//        while ((inputLine = in.readLine()) != null)
//            outputLine = outputLine + inputLine;
//        in.close();
//      } catch (Exception e) {}
//      
//      return outputLine;
//    }
//}
//	
//}
