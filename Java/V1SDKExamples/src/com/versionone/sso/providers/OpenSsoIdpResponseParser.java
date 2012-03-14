package com.versionone.sso.providers;

import java.io.InputStream;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.versionone.sso.PostData;
import com.versionone.sso.IResponseParser;

/**
 * This parser handles the Identity Provider response from an Open SSO implementation

 * @author jerry
 *
 */
public class OpenSsoIdpResponseParser implements IResponseParser {

	private String _urlAuthority;
	private Document _doc;
	
	@Override
	public String getPostUrl() {
		String postUrl = _urlAuthority +  getValueFromNode("form[name=Login]", "action"); 
		return postUrl;
	}
	
	@Override
	public void setUrlAuthority(String value) {
		_urlAuthority = value;
	}

	@Override
	public void loadResponse(InputStream reader)  throws Exception
	{		
		_doc = Jsoup.parse(reader, null, "");
	}

	@Override
	public PostData getPostData() {
        
		PostData formData = new PostData() {

			@Override
			public void setCredentials(String username, String password) {
		        add("IDToken1", username);
		        add("IDToken2", password);				
			}
        	
        };
        formData.add("IDButton");
        formData.add("goto", StringEscapeUtils.unescapeHtml(getSamlResponse()));
        formData.add("SunQueryParamsString", StringEscapeUtils.unescapeHtml(getRelayState()));
        formData.add("encoded", "true");
        formData.add("gx_charset", "UTF-8");
        formData.add("gx_charset", "UTF-8");
        return formData;
	}

	private String getSamlResponse() {
		return getValueFromNode("input[name=goto]", "value");
	}

	private String getRelayState() {
		return getValueFromNode("input[name=SunQueryParamsString]", "value");
	}
	
	private String getValueFromNode(String query, String attribute) {
		Elements elements = _doc.select(query);
		if(0 < elements.size()) {
			return elements.get(0).attributes().get(attribute);
		}
		return "";
	}

}
