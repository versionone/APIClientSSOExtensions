package com.versionone.sso.providers;

import java.io.IOException;
import java.io.Reader;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

/**
 * 
 * I'm not sure any of this is valid anymore.
 * 
 * 
 * 
 * 
 * So everyone, including me, will understand why this is implemented using javax.swing.text.html.HTMLEditorKit
 * The HTML being returned by the IdP in testing was invalid.  
 * @author jerry
 *
 */
public class SamlResponseParser {
	
    private Document _doc;

    public String getPostUrl() { return getValueFromNode("//form", "action"); }
    public String getSamlResponse() { return getValueFromNode("//input[@name=\"SAMLResponse\"]", "value"); } 
    public String getRelayState() { return getValueFromNode("//input[@name=\"RelayState\"]", "value"); } 
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
    
    public void Load(Reader reader) throws ParserConfigurationException, SAXException, IOException 
    {
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

//    public String getPostUrl() { return callback.url; }
//    public String getSamlResponse() { return callback.response; } 
//    public String getRelayState() { return callback.state; } 
//    
//    
//    private HtmlParserCallback callback = new HtmlParserCallback();
//    public void Load(Reader stream) throws IOException
//    {
//    	HtmlParserFactory factory = new HtmlParserFactory();
//    	HTMLEditorKit.Parser parser = factory.getParser();
//    	parser.parse(stream, callback, true);
//    	System.out.println("Parse Complete");
//    }
//
//    
//    class HtmlParserCallback extends HTMLEditorKit.ParserCallback {
//
//    	String url;
//    	String response;
//    	String state;
//    	
//		@Override
//		public void handleStartTag(Tag tag, MutableAttributeSet attributes, int position) {
//			
//			System.out.println(tag);
//			if(Tag.FORM == tag)
//			{
//				url = attributes.getAttribute(HTML.Attribute.ACTION).toString();
//			}
//			else if (Tag.INPUT == tag)
//			{
//				String name = attributes.getAttribute(HTML.Attribute.NAME).toString();
//				if("SAMLResponse" == name)
//					response = attributes.getAttribute(HTML.Attribute.VALUE).toString();
//				else if("RelayState" == name)
//					state = attributes.getAttribute(HTML.Attribute.VALUE).toString();
//			}
//			super.handleStartTag(tag, attributes, position);
//		}		
//    }
//    
//    class HtmlParserFactory extends HTMLEditorKit {
//    	private static final long serialVersionUID = 1013504103824727893L;
//    	public HTMLEditorKit.Parser getParser() {return super.getParser();}
//  	}
}

