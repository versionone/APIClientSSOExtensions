package com.versionone.sso;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintStream;

/**
 * Class represents an HTTP response  
 * 
 * @author jerry
 *
 */
public interface HttpResponse
{
	/**
	 * Get the Input Stream 
	 * @return InputStream
	 * @throws IOException
	 */
	public InputStream getInputStream()  throws IOException;

	/**
	 * Get the output stream
	 * @return OutputStream 
	 * @throws IOException
	 */
	public OutputStream getOutputStream() throws IOException;
	
	/**
	 * Type of content returned by server.
	 * @return
	 */
	public String getContentType();
	
	/**
	 * Close the connection
	 */
	public void close();

	/**
	 * Dump stream results
	 * used during debugging
	 * @param out
	 */
	public void dump(PrintStream out);
	
	
	/**
	 * URL that responded to the request
	 *  
	 * @return
	 */
	String getResponseAuthority() throws Exception;
}
