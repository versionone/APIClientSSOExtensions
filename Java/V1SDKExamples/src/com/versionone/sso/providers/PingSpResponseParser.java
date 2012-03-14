package com.versionone.sso.providers;

import java.io.IOException;
import java.io.InputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;

import com.versionone.sso.PostData;
import com.versionone.sso.IResponseParser;

public class PingSpResponseParser implements IResponseParser {

	private Document _doc;
	
	@Override
	public String getPostUrl() {
		return getValueFromNode("//form", "action");
	}

	@Override
	public void setUrlAuthority(String value) {
	}

	@Override
	public void loadResponse(InputStream reader) throws Exception {
		try {
			DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();

		    // For HTML, we don't want to validate without a DTD
			factory.setValidating(false);
		    // Ignore text elements that are completely empty:
		    factory.setIgnoringElementContentWhitespace(false);
		    factory.setExpandEntityReferences(true);
		    factory.setCoalescing(true);
		    
		    // Ensure that getLocalName() returns the HTML element name
		    factory.setNamespaceAware(true);
			
			DocumentBuilder builder = factory.newDocumentBuilder();
			
			_doc = builder.parse(new InputSource(reader));
		}
		finally {
			if(null != reader){try {reader.close();} catch (IOException e) {}}
		}
	}

	@Override
	public PostData getPostData() {
		PostData formData = new PostData(){

			@Override
			public void setCredentials(String username, String password) {}			
		};
		formData.add("opentoken", getOpenToken());
		return formData;
	}

	private String getOpenToken() 
	{
		return getValueFromNode("//input[@name=\"opentoken\"]", "value");
	}
	
    private String getValueFromNode(String elementXpath, String attributeName)
    {
    	XPath xpath = XPathFactory.newInstance().newXPath();    	
    	Node node = null;
		try {
			node = (Node)xpath.evaluate(elementXpath, _doc.getDocumentElement(), XPathConstants.NODE);
		} catch (XPathExpressionException e) {
		
			e.printStackTrace();
		}
    	
        if ((node != null) && (node.hasAttributes()))
        {
            return node.getAttributes().getNamedItem(attributeName).getNodeValue();
        }
        return null;
    }

}
