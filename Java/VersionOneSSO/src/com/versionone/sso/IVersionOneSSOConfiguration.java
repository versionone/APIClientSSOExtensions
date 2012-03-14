package com.versionone.sso;

/**
 * The configuration requirements for SSO connectivity
 * 
 * @author jerry
 *
 */
public interface IVersionOneSSOConfiguration {

	/**
	 * VersionOne Server URL
	 * @return
	 */
	public String getServerUrl();
	
	/**
	 * VersionOne Username
	 * In SSO, if your IDP uses integrated auth, you may not need to provide this information
	 * 
	 * @return
	 */
	public String getUsername();
	
	/**
	 * Password for VersionOne user
	 * In SSO, if your IDP uses integrated auth, you may not need to provide this information
	 * 
	 * @return
	 */
	public String getPassword();
	
	/**
	 * Does the server support integrated auth.
	 * In SSO, set this to True if your IDP uses integrated auth
	 * @return
	 */
	public boolean getIntegratedAuth();
	
	/**
	 * This is the URL to access the Identity Provider
	 * 
	 * @return
	 */
	public String getIdpUrl();
	
	/**
	 * Name of the class that will parse the response from the Identity Provider
	 * @return
	 */
	public String getIdpResponseParser();
	
	/**
	 * Name of hte class that will parse the response from the Service Provider
	 * @return
	 */
	public String getSpResponseParser();
	
}
