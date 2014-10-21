package com.maestrano.net;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;

public class MnoHttpClient {
	private String userAgent;
	
	public MnoHttpClient() {
		this.userAgent = "maestrano-java/" + System.getProperty("java.version");
	}
	
	/**
	 * Perform a GET request on the specified endpoint
	 * @param url
	 * @return response body
	 * @throws IOException
	 */
	public String get(String url) throws IOException {
		return performRequest(url,"GET");
	}
	
	/**
	 * Perform a request to the remote endpoint
	 * @param url the remote endpoint to contact
	 * @param method such as 'GET', 'PUT', 'POST' or 'DELETE'
	 * @return response body
	 * @throws IOException
	 */
	protected String performRequest(String url, String method) throws IOException {
		// Prepare header
		Map<String,String> header = new HashMap<String,String>();
		header.put("method",method);
		header.put("User-Agent",userAgent);
		
		// Get connection
		HttpURLConnection conn = openConnection(url,header);
		
		// Parse response
		BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
		String inputLine;
		StringBuffer html = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			html.append(inputLine);
		}
		in.close();

		return html.toString();
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
