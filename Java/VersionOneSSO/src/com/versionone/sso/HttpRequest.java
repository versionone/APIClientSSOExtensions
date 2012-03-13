package com.versionone.sso;

import java.net.URL;

/**
 * Represents a generic Http Request 
 * @author jerry
 *
 */
public interface HttpRequest {
	
	/**
	 * URL for request
	 * @return
	 */
	URL getUrl();
	
	/**
	 * What type of Request (Get, Post, etc)
	 * @return
	 */
	String getRequestMethod();
		
	/**
	 * The ability to follow redirects was intentionally omitted because  
	 * the Java HTTPUrlConnection doesn't honor that setting. 
	 */
}
