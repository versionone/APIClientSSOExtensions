package com.versionone.sso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

/**
 * Data posted to SSO Providers (IDP and SP)
 * This object needs to be created and returned by the class that implements IResponseProvider
 * @author jerry
 *
 */
public abstract class PostData {

    private List<String> _formData = new ArrayList<String>();
    
    /**
     * Add a key-value pair
     * results in post data that read key=value
     * 
     * @param key
     * @param value
     */
    public void add(String key, String value)
    {
        try {
			_formData.add(String.format("%s=%s", key, URLEncoder.encode(value, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			_formData.add(String.format("%s=%s", key, value));
		}
    }
    
    /**
     * adds a key with no value
     * results in post data that reads key=
     * @param key
     */
    public void add(String key)
    {
        _formData.add(String.format("%s=", key));
    }

    /**
     * Returns the post data string
     */
    @Override
	public String toString() {
    	return StringUtils.join(_formData, "&");
	}
    
    /**
     * Identity Providers have different field names for credentials.   
     * This abstraction allows the code to make one call to set credentials.  
     * @param username
     * @param password
     */
    public abstract void setCredentials(String username, String password);
}
