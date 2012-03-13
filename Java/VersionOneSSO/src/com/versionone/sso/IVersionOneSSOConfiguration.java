package com.versionone.sso;

public interface IVersionOneSSOConfiguration {

	public String getServerUrl();
	
	public String getUsername();
	
	public String getPassword();
	
	public boolean getIntegratedAuth();
	
	public String getIdpUrl();
	
	public String getIdpResponseParser();
	
	public String getSpResponseParser();
	
}
