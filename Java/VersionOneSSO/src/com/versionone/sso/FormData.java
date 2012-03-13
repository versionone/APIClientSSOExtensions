package com.versionone.sso;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.lang.StringUtils;

public abstract class FormData {

    private List<String> _formData = new ArrayList<String>();
    
    public void add(String key, String value)
    {
        try {
			_formData.add(String.format("%s=%s", key, URLEncoder.encode(value, "UTF-8")));
		} catch (UnsupportedEncodingException e) {
			e.printStackTrace();
			_formData.add(String.format("%s=%s", key, value));
		}
    }
    
    public void add(String key)
    {
        _formData.add(String.format("%s=", key));
    }

    public String getPostData()
    {	
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
