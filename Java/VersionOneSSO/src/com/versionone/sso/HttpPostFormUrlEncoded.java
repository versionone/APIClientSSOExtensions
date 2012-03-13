package com.versionone.sso;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Post data that's application/x-www-form-urlencoded encoded
 * @author jerry
 *
 */
public class HttpPostFormUrlEncoded extends HttpPost
{
	/**
	 * creation helper (factory)
	 * @param url URL for post
	 * @param data data to post
	 * @return HttpPost ready for use
	 * @throws MalformedURLException
	 */
	public static HttpPost create(String url, String data) throws MalformedURLException
	{
		HttpPost rc = new HttpPostFormUrlEncoded(new URL(url));
		rc.setData(data);
		return rc; 
	}

	/**
	 * Create
	 * @param url
	 */
	public HttpPostFormUrlEncoded(URL url) {
		super(url, "application/x-www-form-urlencoded");
	}
}
