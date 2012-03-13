package com.versionone.sso;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Post text/xml data
 * this is the most common for VersionOne
 * 
 * @author jerry
 *
 */
public class HttpPostXmlContent extends HttpPost {

	/**
	 * Construction helper
	 * @param url URL for post
	 * @param data data to post
	 * @return HttpPost ready for use
	 * @throws MalformedURLException
	 */
	public static HttpPost create(String url, String data) throws MalformedURLException
	{
		HttpPost rc = new HttpPostXmlContent(new URL(url));
		rc.setData(data);
		return  rc;
	}
	
	/**
	 * Create
	 * @param url
	 */
	public HttpPostXmlContent(URL url) {
		super(url, "text/xml");
	}

}
