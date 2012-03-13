package com.versionone.sso;

import com.versionone.apiclient.ConnectionException;

public class ResponseParserFactory {
	
	public static String idpResponseParser;
	public static String spResponseParser;

	public static IResponseParser CreateIdentityProviderResponseParser() throws ConnectionException
	{
		if (null == _impl)
			_impl = new ResponseParserFactory();
		return _impl.CreateIdentityProviderParser();
	}

    public static IResponseParser CreateServiceProviderResponseParser() throws ConnectionException
    {
        if (null == _impl)
            _impl = new ResponseParserFactory();
        return _impl.CreateServiceProviderParser();
    }


	// All this to avoid reading the configuration every time.

	private static ResponseParserFactory _impl;
	
	private ResponseParserFactory()	{ }

	private IResponseParser CreateIdentityProviderParser() throws ConnectionException
	{
		IResponseParser parser;
		try {
			Class<?> idpClass = Class.forName(idpResponseParser);
			parser = (IResponseParser)idpClass.newInstance();
		} catch (ClassNotFoundException e) {
			throw new ConnectionException("Cannot find IDP Response Parser class", e);
		} catch (InstantiationException e) {
			throw new ConnectionException("Canot Instantiate IDP Response Parser class", e);
		} catch (IllegalAccessException e) {
			throw new ConnectionException("Canot Access IDP Response Parser class", e);
		}
		
		return parser;
	}

    private IResponseParser CreateServiceProviderParser() throws ConnectionException
    {
    	IResponseParser parser;
    	try
    	{
    		Class<?> spClass = Class.forName(spResponseParser);
    		parser = (IResponseParser)spClass.newInstance();    		
		} catch (ClassNotFoundException e) {
			throw new ConnectionException("Cannot find SP Response Parser class", e);
		} catch (InstantiationException e) {
			throw new ConnectionException("Canot Instantiate SP Response Parser class", e);
		} catch (IllegalAccessException e) {
			throw new ConnectionException("Canot Access SP Response Parser class", e);
		}
		return parser;    	
    }
	
}
