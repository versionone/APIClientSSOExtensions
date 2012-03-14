package com.versionone.sso;

import java.io.InputStream;

/**
 * Interface that a SSO provider (IDP and SP) needs to implement
 * @author jerry
 *
 */
public interface IResponseParser {

	/**
	 * 	The the Post URL from the form on the page
	 * @return
	 */
	String getPostUrl();

	/**
	 * This is the UriPartial.Authority part of the site that sent us the SAML response. 
	 * This is important when the PostsUrl is relative.
	 * @param value
	 */
	void setUrlAuthority(String value);

	/**
	 * CLoad the response received from a server
	 * @param reader 
	 * @throws Exception
	 */
	void loadResponse(InputStream reader) throws Exception;
	
	/**
	 * Return the form data necessary to take the next step.
	 */
	PostData getPostData();
}
