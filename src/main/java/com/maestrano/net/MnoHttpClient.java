package com.maestrano.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import com.maestrano.Maestrano;

import exception.ApiException;
import exception.AuthenticationException;

public class MnoHttpClient {
	private String defaultUserAgent;
	private String basicAuthHash;
	
	public MnoHttpClient() {
		this.defaultUserAgent = "maestrano-java/" + System.getProperty("java.version");
	}
	
	/**
	 * Return a client with HTTP Basic Authentication setup
	 */
	public static MnoHttpClient getAuthenticatedClient() {
		MnoHttpClient client = new MnoHttpClient();
		String authStr = Maestrano.apiService().getId() + ":" + Maestrano.apiService().getKey();
		client.basicAuthHash = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());
		
		return client;
	}
	
	/**
	 * Perform a GET request on the specified endpoint
	 * @param url
	 * @return response body
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 */
	public String get(String url) throws AuthenticationException, ApiException {
		return performRequest(url,"GET",null,null,null);
	}
	
	/**
	 * Perform a GET request on the specified endpoint
	 * @param url
	 * @return response body
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 */
	public String get(String url, Map<String,String> params) throws AuthenticationException, ApiException {
		return performRequest(url,"GET",params, null,null);
	}
	
	/**
	 * Perform a GET request on the specified endpoint
	 * @param url
	 * @return response body
	 * @throws IOException
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 */
	public String get(String url, Map<String,String> params, Map<String,String> header) throws AuthenticationException, ApiException {
		return performRequest(url,"GET",params, header,null);
	}
	
	/**
	 * Perform a POST request on the specified endpoint
	 * @param url
	 * @param header
	 * @param payload
	 * @return response body
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 */
	public String post(String url, String payload) throws AuthenticationException, ApiException {
		return performRequest(url,"POST",null,null,payload);
	}
	
	/**
	 * Perform a PUT request on the specified endpoint
	 * @param url
	 * @param header
	 * @param payload
	 * @return response body
	 * @throws ApiException 
	 * @throws AuthenticationException
	 */
	public String put(String url, String payload) throws AuthenticationException, ApiException {
		return performRequest(url,"PUT",null,null,payload);
	}
	
	/**
	 * Perform a PUT request on the specified endpoint
	 * @param url
	 * @param header
	 * @param payload
	 * @return response body
	 * @throws ApiException 
	 * @throws AuthenticationException 
	 */
	public String delete(String url) throws AuthenticationException, ApiException {
		return performRequest(url,"DELETE",null,null,null);
	}
	
	/**
	 * Perform a request to the remote endpoint
	 * @param url the remote endpoint to contact
	 * @param method such as 'GET', 'PUT', 'POST' or 'DELETE'
	 * @param header values
	 * @param payload data to send
	 * @return response body
	 * @throws AuthenticationException 
	 * @throws ApiException 
	 */
	protected String performRequest(String url, String method, Map<String,String> params, Map<String,String> header, String payload) throws AuthenticationException, ApiException {
		// Prepare header
		if (header == null) {
			header = new HashMap<String,String>();
		}
		
		// Set method
		header.put("method",method.toUpperCase());
		
		// Set user agent
		if (header.get("User-Agent") == null || header.get("User-Agent").isEmpty()) {
			header.put("User-Agent",defaultUserAgent);
		}
		
		// Set authorization
		if (this.basicAuthHash != null && ! this.basicAuthHash.isEmpty()) {
			if(header.get("Authorization") == null || header.get("Authorization").isEmpty()) {
				header.put("Authorization",basicAuthHash);
			}
		}
		
		// Prepare url
		String realUrl = url;
		if (params != null && !params.isEmpty()) {
			realUrl += "?";
			
			for (Map.Entry<String, String> param : params.entrySet())
			{
				String key;
				try {
					key = URLEncoder.encode(param.getKey(),"UTF-8");
					String val = URLEncoder.encode(param.getValue(), "UTF-8");
					realUrl += "&" + key + "=" + val;
				} catch (UnsupportedEncodingException e) {
					throw new ApiException("Wrong query parameter encoding for pair: " + param.getKey() + " = " + param.getValue(),e);
				}
				
			}
		}
		
		// Get connection
		HttpURLConnection conn;
		try {
			conn = openConnection(realUrl,header);
			
			if (conn.getResponseCode() == HttpURLConnection.HTTP_UNAUTHORIZED) {
				throw new AuthenticationException("Invalid API credentials");
			}
			
		} catch (IOException e) {
			throw new ApiException("Something wrong happened while trying establish the connection",e);
		}
		
		// Send Data if PUT/POST
		if (payload != null) {
			if (method.equalsIgnoreCase("PUT") || method.equalsIgnoreCase("POST")) {
				OutputStream output = null;
				try {
					output = conn.getOutputStream();
					output.write(payload.getBytes());
				} catch (IOException e) {
					throw new ApiException("Unable to send data to server",e);
				} finally {
					if (output != null) {
						try { output.close(); } catch (IOException e) {}
					}
				}
			}
		}
		
		
		// Parse response
		BufferedReader in;
		try {
			in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			
			String inputLine;
			StringBuffer html = new StringBuffer();

			while ((inputLine = in.readLine()) != null) {
				html.append(inputLine);
			}
			in.close();
			
			return html.toString();
			
		} catch (IOException e) {
			throw new ApiException("Unable to read response from server",e);
		}
	}
	
	/**
	 * Open a connection and follow the redirect
	 * @param String url
	 * @param header contain 
	 * @return HttpURLConnection connection
	 * @throws IOException
	 */
	protected HttpURLConnection openConnection(String url, Map<String,String> header) throws IOException {
		// Initialize connection
		URL urlObj = null;
		HttpURLConnection conn = null;

		int redirectCount = 0;
		boolean redirect = true;
		while (redirect) {
			if (redirectCount > 10) {
				throw new ProtocolException("Too many redirects: " + redirectCount);
			}

			if (conn == null) {
				urlObj = new URL(url);
			} else {
				// get redirect url from "location" header field
				urlObj = new URL(conn.getHeaderField("Location"));
			}

			// open the new connection again
			conn = (HttpURLConnection) urlObj.openConnection();
			conn.setRequestMethod(header.get("method"));
			conn.addRequestProperty("User-Agent", header.get("User-Agent"));
			conn.setInstanceFollowRedirects(true);
			
			// Check if redirect
			redirect = false;
			int status = conn.getResponseCode();
			if (status != HttpURLConnection.HTTP_OK) {
				if (status == HttpURLConnection.HTTP_MOVED_TEMP
						|| status == HttpURLConnection.HTTP_MOVED_PERM
						|| status == HttpURLConnection.HTTP_SEE_OTHER)
					redirect = true;
				redirectCount ++;
			}
		}

		return conn;
	}
}
