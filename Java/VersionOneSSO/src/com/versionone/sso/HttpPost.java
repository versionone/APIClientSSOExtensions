package com.versionone.sso;

import java.net.MalformedURLException;
import java.net.URL;

/**
 * Class represents an HTTP Post
 * @author jerry
 *
 */
public class HttpPost implements HttpRequest
{
	private URL _url;
	private String _contentType;
	private String _data = null;
	
	/**
	 * Construction Helper (Factory)
	 * @param url - post URL
	 * @param contentType - content type
	 * @return Http post ready for use
	 * @throws MalformedURLException
	 */
	public static HttpPost create(String url, String contentType) throws MalformedURLException
	{
		return new HttpPost(new URL(url), contentType); 
	}

	/**
	 * Create 
	 * @param url
	 * @param contentType
	 */
	public HttpPost(URL url, String contentType) {
		_url = url;
		_contentType = contentType;
	}

	/**
	 * Type of method 
	 * @return POST
	 */
	@Override
	public String getRequestMethod() {
		return "POST";
	}

	/**
	 * URL for post
	 */
	@Override
	public URL getUrl() {
		return _url;
	}

	/**
	 * get data to post
	 * @return
	 */
	public String getData() {
		return _data;
	}

	/**
	 * Set data to post
	 * @param value
	 */
	public void setData(String value) {
		_data = value;
	}

	/**
	 * get content type
	 * @return
	 */
	public String getContentType() {
		return _contentType;
	}	
}
