package com.versionone.sso.providers;

import java.io.InputStream;

import org.apache.commons.lang.StringEscapeUtils;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

import com.versionone.sso.FormData;
import com.versionone.sso.IResponseParser;

/**
 * This parser handles the Service Provider response from an Open SSO implementation
 * 
 * @author jerry
 *
 */
public class OpenSsoSpResponseParser implements IResponseParser {

	private Document _doc;

	@Override
	public String getPostUrl() {
		return getValueFromNode("form", "action");
	}

	@Override
	public String getSamlResponse() {
		return getValueFromNode("input[name=SAMLResponse]", "value");
	}

	@Override
	public String getRelayState() {
		return getValueFromNode("input[name=RelayState]", "value");
	}

	@Override
	public void setUrlAuthority(String value) {
	}

	@Override
	public void load(InputStream reader)  throws Exception
	{
		_doc = Jsoup.parse(reader, null, "");
	}

	@Override
	public FormData getFormData() {
    	FormData formData = new FormData(){

			@Override
			public void setCredentials(String username, String password) {
				// no credentials needed on this page				
			}
    	};
        formData.add("SAMLResponse", StringEscapeUtils.unescapeHtml(getSamlResponse()));
        formData.add("RelayState", StringEscapeUtils.unescapeHtml(getRelayState()));
        return formData;
	}

	private String getValueFromNode(String query, String attribute) {
		Elements elements = _doc.select(query);
		if(0 < elements.size()) {
			return elements.get(0).attributes().get(attribute);
		}
		return "";
	}

}
