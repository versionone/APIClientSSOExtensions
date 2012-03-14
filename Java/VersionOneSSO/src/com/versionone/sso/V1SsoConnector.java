package com.versionone.sso;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.Reader;
import java.net.Authenticator;
import java.net.MalformedURLException;
import java.net.PasswordAuthentication;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang.StringEscapeUtils;

import sun.net.www.protocol.http.AuthCacheImpl;
import sun.net.www.protocol.http.AuthCacheValue;

import com.versionone.apiclient.ConnectionException;
import com.versionone.apiclient.IAPIConnector;

/**
 * IAPIConnector that supports SSO authenticationZ
 * @author jerry
 *
 */
public class V1SsoConnector implements IAPIConnector {

    private String _v1Url;
    private String _idpUrl;
    private String _username;
    private String _password;
        
	/**
	 * Additional headers for request to the VersionOne server
	 */
	public final Map<String, String> customHttpHeaders = new HashMap<String, String>();

	/**
	 * Helper method to create a Meta connection (ServerUrl + /meta.v1/)
	 * @param config - Configuration 
	 * @return
	 */
	public static V1SsoConnector createMetaConnector(IVersionOneSSOConfiguration config)
	{
		return new V1SsoConnector(config, "/meta.v1/"); 
	}
	
	/**
	 * Helper method to create a data connection (ServerUrl + /rest-1.v1/)
	 * @param config - Configuration 
	 * @param authenticate - true if this method should attempt authentication
	 * @return
	 * @throws ConnectionException
	 */
	public static V1SsoConnector createDataConnector(IVersionOneSSOConfiguration config, boolean authenticate) throws ConnectionException
	{
		V1SsoConnector dataConnector = new V1SsoConnector(config, "/rest-1.v1/");
        if(authenticate)
            dataConnector.authenticate();
        return dataConnector;
	}

	/**
	 * Construction
	 * @param config - configuration
	 * @param path - path extension (/meta.v1/ or /rest-1.v1/)
	 */
	public V1SsoConnector(IVersionOneSSOConfiguration config, String path)
	{
	    _v1Url = config.getServerUrl() + path;
	    _username = config.getUsername();
	    _password = config.getPassword();
	    
	    _idpUrl = config.getIdpUrl();
	    ResponseParserFactory.idpResponseParser = config.getIdpResponseParser();
	    ResponseParserFactory.spResponseParser = config.getSpResponseParser();
	    
		// WORKAROUND: http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=6626700
		if (_username != null) {
			AuthCacheValue.setAuthCache(new AuthCacheImpl());
			Authenticator.setDefault(new Credentials(_username, _password));
		}    
	}

	/**
	 * Authenticate the SSO Connection
	 * @throws ConnectionException
	 */
    public void authenticate() throws ConnectionException  
    {  	
        IResponseParser idpResponse = IdentityProviderRequest();
        IResponseParser spResponse = IdentityProviderAuthentication(idpResponse);
        ServiceProviderRequest(spResponse);       
    }

    /**
     * Make a request to the IdP to get the Response value.
     * 
     * ** This is here, as opposed to a separate object because we need to share the Cookie container **
     * 
     * @return
     * @throws ConnectionException
     */
    private IResponseParser IdentityProviderRequest() throws ConnectionException 
    {
    	IResponseParser idpResponseParser = ResponseParserFactory.CreateIdentityProviderResponseParser(); 	
    	HttpGet request;
		try {
			request = HttpGet.create(StringEscapeUtils.unescapeXml(_idpUrl));		
		} catch (MalformedURLException e) {
			throw new ConnectionException("Invalid IdP URL " + _idpUrl, e);
		}
		
    	HttpResponse response = new HttpClient().execute(request);     	
    	
    	try {
			idpResponseParser.loadResponse(response.getInputStream());
			idpResponseParser.setUrlAuthority(response.getResponseAuthority());
		} catch (IOException e) {
			throw new ConnectionException("Error Reading IPD Response",e);	
		} catch (Exception e) {
			throw new ConnectionException("Error getting response authority", e);
		}
    	
    	return idpResponseParser;    	

    }

	/**
	 * Post the response from the Identity Provider challenge to the proper URL for authentication
	 * 
	 * ** This is here, as opposed to a separate object because we need to share the Cookie container **
	 * 
	 * @param idpResponse - The response parser from the Identity Provider request 
	 * @return the response parser necessary to make the Service Provider request
	 * @throws ConnectionException
	 */
	private IResponseParser IdentityProviderAuthentication(IResponseParser idpResponse) throws ConnectionException
    {
        IResponseParser spResponseParser = ResponseParserFactory.CreateServiceProviderResponseParser();
        
    	PostData formData = idpResponse.getPostData();
    	formData.setCredentials(_username, _password);
    	
        // Prepare web request...
        HttpPost request;
		try {

			request = HttpPost.create(idpResponse.getPostUrl(), "application/x-www-form-urlencoded");
	        request.setData(formData.toString());

		} catch (MalformedURLException e) {
			throw new ConnectionException("Invalid URL from IDP", e);
		}
        
        HttpResponse spResponse = new HttpClient().execute(request);
        
        try {
			spResponseParser.loadResponse(spResponse.getInputStream());
		} catch (Exception e) {
			throw new ConnectionException("Error Reading IPD Response",e);
		}
        
        return spResponseParser;
    }

	/**
	 * Post data to the Service Provider 
	 * 
	 * @param spResponse - The response that contains data for the Service Provider
	 * @throws ConnectionException
	 */
    private void ServiceProviderRequest(IResponseParser spResponse) throws ConnectionException
    {
    	PostData formData = spResponse.getPostData();

        HttpPost request;
		try {
			request = HttpPost.create(StringEscapeUtils.unescapeHtml(spResponse.getPostUrl()), "application/x-www-form-urlencoded");
			request.setData(formData.toString());
		} catch (MalformedURLException e) {
			throw new ConnectionException("Invalid URL for Service Provider Request", e);
		}
		
        HttpResponse respponse = new HttpClient().execute(request);
        respponse.close();        
    }
    
    private final Map<String, HttpClient> _requests = new HashMap<String, HttpClient>();
	@Override
	public OutputStream beginRequest(String path, String contentType) throws ConnectionException {

		HttpPost request;
		try {
			request = HttpPost.create(_v1Url + path, contentType);
		} catch (MalformedURLException e) {
			throw new ConnectionException("Invalid URL " + _v1Url + path, e);
		}
		HttpClient client = new HttpClient();
		_requests.put(path, client);
		
		try {
			return client.beginRequest(request).getOutputStream();
		} catch (IOException e) {
			throw new ConnectionException("Cannot begin the request for " + _v1Url + path, e);
		}		
	}

	@Override
	public InputStream endRequest(String path) throws ConnectionException {
		
		HttpClient client = _requests.get(path); 
		try {
			return client.endRequest().getInputStream();
		} catch (IOException e) {
			throw new ConnectionException("Cannot get response stream for " + path, e);
		}		
	}

	@Override
	public Reader getData() throws ConnectionException {
		return getData("");
	}

	@Override
	public Reader getData(String path) throws ConnectionException {
		
    	HttpGet request;
		try {
			request = HttpGet.create(_v1Url + path);
		} catch (MalformedURLException e) {
			throw new ConnectionException("Invalid URL " + _v1Url + path, e);
		}
		
    	HttpResponse response = new HttpClient().execute(request);
    	if(isSamlResponse(response.getContentType()))
    	{
    		response.close();
    		
			authenticate();
    		return getData(path);
    	}

    	try {
			return new InputStreamReader(response.getInputStream());
		} catch (IOException e) {
			throw new ConnectionException("Error processing result from URL "
					+ path, e);
		}
	}

	private boolean isSamlResponse(String contentType) {
		return contentType.startsWith("text/html");
	}

	@Override
	public Reader sendData(String path, String data) throws ConnectionException {
		HttpPost request;
		try {
			request = HttpPostXmlContent.create(_v1Url + path, data);
		} catch (MalformedURLException e) {
			throw new ConnectionException("Invalid URL " + _v1Url + path, e);
		}

    	HttpResponse response = new HttpClient().execute(request);
    	try {
			return new InputStreamReader(response.getInputStream());
		} catch (IOException e) {
			throw new ConnectionException("Error processing result from URL "
					+ path, e);
		}
	}

	/**
	 * Authenticator
	 * @author Jerry D. Odenwelder Jr.
	 *
	 */
	private class Credentials extends Authenticator {

		PasswordAuthentication _value;

		@Override
		protected PasswordAuthentication getPasswordAuthentication() {
			return _value;
		}

		Credentials(String userName, String password) {
			if (null == password) {
				_value = new PasswordAuthentication(userName, "".toCharArray());
			} else {
				_value = new PasswordAuthentication(userName, password
						.toCharArray());
			}
		}

		@Override
		public String toString() {
			return _value.getUserName() + ":"
					+ String.valueOf(_value.getPassword());
		}
	}
}
