package com.versionone.sdk.examples;

import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.XMLConfiguration;

import com.versionone.sso.IVersionOneSSOConfiguration;

/**
 * Wrapper for sample program configuration
 * 
 * @author jerry
 *
 */
public class Configuration implements IVersionOneSSOConfiguration 
{
	
	private static String ServerUrl = "v1ServerUrl";
	private static String Username = "username";
	private static String Password = "password";
	private static String IntegratedAuth = "integrated";
	private static String IdpUrl = "idpUrl";
	private static String IdpResponseParser = "idpResponseParser";
	private static String SpResponseParser = "spResponseParser";
	
	private XMLConfiguration config = new XMLConfiguration();
	
	public static Configuration _instance = null;
	
	private Configuration(){}
	
	public static Configuration instance()
	{
		if(null == _instance)
		{
			_instance = new Configuration();
			_instance.load();
		}
		return _instance;
	}
	
	public String getServerUrl() {return config.getString(ServerUrl);}
	public String getUsername() {return config.getString(Username);}
	public String getPassword() {return config.getString(Password);}
	public boolean getIntegratedAuth() {return config.getBoolean(IntegratedAuth);}
	public String getIdpUrl() {return config.getString(IdpUrl);}
	public String getIdpResponseParser() {return config.getString(IdpResponseParser);}
	public String getSpResponseParser() {return config.getString(SpResponseParser);}
	

	private void load()
	{
		try {			
			config.load(getClass().getResourceAsStream("config.xml"));
		} catch (ConfigurationException e) {
			throw new RuntimeException("Cannot read config.xml", e);
		}
	}
}
