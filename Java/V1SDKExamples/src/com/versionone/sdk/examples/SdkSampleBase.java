package com.versionone.sdk.examples;

public abstract class SdkSampleBase {

    protected SdkSampleBase()
    {
    }

    private String _serverUrl;
    public String getServerUrl() {return _serverUrl;}
    protected void setServerUrl(String value) {_serverUrl = value;}
    
    private String _username;
    public String getUsername() {return _username;}
    protected void setUsername(String value) {_username = value;}
    
    private String _password;
    public String getPassword() {return _password;}
    protected void setPassword(String value) {_password = value;}
    
    private boolean _integratedAuth;
    public boolean getIntegratedAuth() {return _integratedAuth;}
    protected void setIntegratedAuth(boolean value) {_integratedAuth = value;}
    
    /**
     * Set attributes from Commandline
     * Default action does nothing
     * @param args - Commandline arguments
     */
	public void commandLineArguments(String[] args) {}


	/**
	 * Load configuration.
	 */
	public void loadConfig()
	{
		setServerUrl(Configuration.instance().getServerUrl());
		setUsername(Configuration.instance().getUsername());
		setPassword(Configuration.instance().getPassword());
		setIntegratedAuth(Configuration.instance().getIntegratedAuth());
	}
	
	public abstract void run();
	public abstract void connect();

}
