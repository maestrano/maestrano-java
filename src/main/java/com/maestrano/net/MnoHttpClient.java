package com.maestrano.net;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import javax.xml.bind.DatatypeConverter;

import org.apache.commons.io.IOUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.maestrano.ApiService;
import com.maestrano.exception.ApiException;
import com.maestrano.exception.AuthenticationException;
import com.maestrano.json.DateSerializer;

public class MnoHttpClient {	
	public static final Gson GSON = new GsonBuilder().registerTypeAdapter(Date.class, new DateSerializer()).create();

	private String defaultUserAgent;
	private String basicAuthHash;
	private String defaultContentType;
	private String defaultAccept;

	public MnoHttpClient() {
		this.defaultUserAgent = "maestrano-java/" + System.getProperty("java.version");
		this.defaultContentType = "application/vnd.api+json";
		this.defaultAccept = "application/vnd.api+json";
	}

	private MnoHttpClient(String contentType) {
		this.defaultUserAgent = "maestrano-java/" + System.getProperty("java.version");
		this.defaultContentType = contentType;
		this.defaultAccept = contentType;
	}

	/**
	 * Return a client with HTTP Basic Authentication setup
	 */
	public static MnoHttpClient getAuthenticatedClient(ApiService apiService) {
		return getAuthenticatedClient(apiService.getId(), apiService.getKey(), "application/vnd.api+json");
	}

	public static MnoHttpClient getAuthenticatedClient(ApiService apiService, String contentType) {
		return getAuthenticatedClient(apiService.getId(), apiService.getKey(), contentType);
	}

	/**
	 * Return a client with HTTP Basic Authentication setup
	 */
	public static MnoHttpClient getAuthenticatedClient(String key, String secret, String contentType) {
		MnoHttpClient client = new MnoHttpClient(contentType);
		String authStr = key + ":" + secret;
		client.basicAuthHash = "Basic " + DatatypeConverter.printBase64Binary(authStr.getBytes());

		return client;
	}

	/**
	 * Perform a GET request on the specified endpoint
	 * 
	 * @param url
	 * @return response body
	 * @throws ApiException
	 * @throws AuthenticationException
	 */
	public String get(String url) throws AuthenticationException, ApiException {
		return performRequest(url, "GET", null, null, null);
	}

	/**
	 * Perform a GET request on the specified endpoint
	 * 
	 * @param <V>
	 * @param url
	 * @return response body
	 * @throws ApiException
	 * @throws AuthenticationException
	 */
	public <V> String get(String url, Map<String, V> params) throws AuthenticationException, ApiException {
		return performRequest(url, "GET", params, null, null);
	}

	/**
	 * Perform a GET request on the specified endpoint
	 * 
	 * @param <V>
	 * @param url
	 * @return response body
	 * @throws IOException
	 * @throws ApiException
	 * @throws AuthenticationException
	 */
	public <V> String get(String url, Map<String, V> params, Map<String, String> header) throws AuthenticationException, ApiException {
		return performRequest(url, "GET", params, header, null);
	}

	/**
	 * Perform a POST request on the specified endpoint
	 * 
	 * @param url
	 * @param header
	 * @param payload
	 * @return response body
	 * @throws ApiException
	 * @throws AuthenticationException
	 */
	public String post(String url, String payload) throws AuthenticationException, ApiException {
		return performRequest(url, "POST", null, null, payload);
	}

	/**
	 * Perform a PUT request on the specified endpoint
	 * 
	 * @param url
	 * @param header
	 * @param payload
	 * @return response body
	 * @throws ApiException
	 * @throws AuthenticationException
	 */
	public String put(String url, String payload) throws AuthenticationException, ApiException {
		return performRequest(url, "PUT", null, null, payload);
	}

	/**
	 * Perform a PUT request on the specified endpoint
	 * 
	 * @param url
	 * @param header
	 * @param payload
	 * @return response body
	 * @throws ApiException
	 * @throws AuthenticationException
	 */
	public String delete(String url) throws AuthenticationException, ApiException {
		return performRequest(url, "DELETE", null, null, null);
	}

	/**
	 * Perform a request to the remote endpoint
	 * 
	 * @param <V>
	 * @param url
	 *            the remote endpoint to contact
	 * @param method
	 *            such as 'GET', 'PUT', 'POST' or 'DELETE'
	 * @param header
	 *            values
	 * @param payload
	 *            data to send
	 * @return response body
	 * @throws AuthenticationException
	 * @throws ApiException
	 */
	protected <V> String performRequest(String url, String method, Map<String, V> params, Map<String, String> header, String payload) throws AuthenticationException, ApiException {
		// Prepare header
		if (header == null) {
			header = new HashMap<String, String>();
		}

		// Set method
		header.put("method", method.toUpperCase());

		// Set 'User-Agent'
		if (header.get("User-Agent") == null || header.get("User-Agent").isEmpty()) {
			header.put("User-Agent", defaultUserAgent);
		}

		// Set 'Authorization'
		if (this.basicAuthHash != null && !this.basicAuthHash.isEmpty()) {
			if (header.get("Authorization") == null || header.get("Authorization").isEmpty()) {
				header.put("Authorization", basicAuthHash);
			}
		}

		// Set 'Content-Type'
		if (header.get("Content-Type") == null || header.get("Content-Type").isEmpty()) {
			header.put("Content-Type", defaultContentType);
		}

		// Set 'Accept'
		if (header.get("Accept") == null || header.get("Accept").isEmpty()) {
			header.put("Accept", defaultAccept);
		}

		// Prepare url
		String realUrl = url;
		if (params != null && !params.isEmpty()) {
			realUrl += "?";
			boolean isFirst = true;

			for (Map.Entry<String, V> param : params.entrySet()) {
				String key;
				try {
					key = URLEncoder.encode(param.getKey(), "UTF-8");
					String realParam = GSON.fromJson(GSON.toJson(param.getValue()), String.class);
					String val = URLEncoder.encode(realParam, "UTF-8");
					if (!isFirst)
						realUrl += "&";
					realUrl += key + "=" + val;
					isFirst = false;
				} catch (UnsupportedEncodingException e) {
					throw new ApiException("Wrong query parameter encoding for pair: " + param.getKey() + " = " + param.getValue(), e);
				}

			}
		}

		// Get connection
		HttpURLConnection conn = openConnection(header, realUrl);

		// Send Data if PUT/POST
		if (payload != null) {
			if (method.equalsIgnoreCase("PUT") || method.equalsIgnoreCase("POST")) {
				OutputStream output = null;
				try {
					OutputStream outputStream = conn.getOutputStream();
					IOUtils.write(payload, outputStream);
				} catch (IOException e) {
					throw new ApiException("Unable to send data to server", e);
				} finally {
					IOUtils.closeQuietly(output);
				}
			}
		}

		// Check Response code
		int responseCode;
		try {
			responseCode = conn.getResponseCode();
		} catch (IOException e) {
			throw new ApiException("Unable to read response code", e);
		}
		
		switch (responseCode) {
		case HttpURLConnection.HTTP_UNAUTHORIZED:
			throw new AuthenticationException("Invalid API credentials");
		case HttpURLConnection.HTTP_BAD_REQUEST:
			InputStream errorStream = conn.getErrorStream();
			if (errorStream != null){
				String error = readInputStream(errorStream);
				throw new ApiException("Connection could not be made. Bad Request: " + error);
			}else{
				throw new ApiException("Connection could not be made. Bad Request");
			}
		}
		
		// Parse response
		InputStream inputStream = null;
		try {
			inputStream = conn.getInputStream();
			return readInputStream(inputStream);
		} catch (IOException e) {
			throw new ApiException("Unable to read response from server", e);
		}finally{
			IOUtils.closeQuietly(inputStream);
		
		}

	}

	private HttpURLConnection openConnection(Map<String, String> header, String realUrl) throws ApiException {
		try {
			return openConnection(realUrl, header);
		} catch (IOException e) {
			throw new ApiException("Something wrong happened while trying establish the connection " + realUrl, e);
		}
	}

	private static String readInputStream(InputStream is) throws ApiException{
		if(is == null){
			throw new ApiException("Connection InputStream is null");
		}
		try {
			return IOUtils.toString(is);
		} catch (IOException e) {
			throw new ApiException("Could not read Input Stream", e);
		}
	}
	
	
	/**
	 * Open a connection and follow the redirect
	 * 
	 * @param String
	 *            url
	 * @param header
	 *            contain
	 * @return HttpURLConnection connection
	 * @throws IOException
	 */
	protected HttpURLConnection openConnection(String url, Map<String, String> header) throws IOException {
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
			conn.setInstanceFollowRedirects(true);

			// Set output if PUT or POST
			if (conn.getRequestMethod() == "POST" || conn.getRequestMethod() == "PUT") {
				conn.setDoOutput(true);
			}

			// Set request header
			for (Map.Entry<String, String> headerAttr : header.entrySet()) {
				conn.addRequestProperty(headerAttr.getKey(), headerAttr.getValue());
			}

			// Check if redirect
			redirect = false;
			if (conn.getRequestMethod() == "GET") {
				int status = conn.getResponseCode();
				if (status != HttpURLConnection.HTTP_OK) {
					if (status == HttpURLConnection.HTTP_MOVED_TEMP || status == HttpURLConnection.HTTP_MOVED_PERM || status == HttpURLConnection.HTTP_SEE_OTHER)
						redirect = true;
					redirectCount++;
				}
			}
		}

		return conn;
	}
}
