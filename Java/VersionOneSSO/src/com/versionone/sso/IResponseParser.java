package com.versionone.sso;

import java.io.InputStream;

/**
 *  Interface that a SamlResponseParser needs to implement
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
	 * The SAML response  
	 * @return
	 */
	String getSamlResponse();

	/**
	 * The relay state
	 * @return
	 */
	String getRelayState();

	/**
	 * This is the UriPartial.Authority part of the site that sent us the SAML response. 
	 * This is important when the PostsUrl is relative.
	 * @param value
	 */
	void setUrlAuthority(String value);

	/**
	 * Create the SAML Response from a stream
	 * @param reader - page returned from server
	 * @throws Exception
	 */
	void load(InputStream reader) throws Exception;
	
	/**
	 * Return the form data necessary to take the next step.
	 */
	FormData getFormData();
}
