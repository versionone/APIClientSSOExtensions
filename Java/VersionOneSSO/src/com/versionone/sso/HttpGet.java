package com.versionone.sso;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Represents Http get
 *  
 * @author jerry
 *
 */
public class HttpGet implements HttpRequest {

	URL _url;

	/**
	 * Create
	 * @param url
	 * @return
	 * @throws MalformedURLException
	 */
	public static HttpGet create(String url) throws MalformedURLException
	{
		return new HttpGet(new URL(url));
	}
	
	/**
	 * Create
	 * @param url
	 */
	public HttpGet(URL url) {
		_url = url;
	}

	/**
	 * Get type of request
	 * @return GET
	 */
	@Override
	public String getRequestMethod() {
		return "GET";
	}

	/**
	 * Get the URL
	 */
	@Override
	public URL getUrl() {
		return _url;
	}
}
