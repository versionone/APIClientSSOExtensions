package com.versionone.sso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import com.versionone.apiclient.ConnectionException;
import com.versionone.apiclient.CookiesManager;

/**
 * Handles HTTP Requests
 * 
 * @author jerry
 *
 */
public class HttpClient {
	
	static final String UTF8 = "UTF-8";

	private final CookiesManager cookiesManager;
	private HttpURLConnection _connection;
	
	/**
	 * Create
	 */
	public HttpClient()
	{
		cookiesManager = CookiesManager.getCookiesManager("/");
	}

	/**
	 * Execute Get request
	 * @param request HttpGet request
	 * @return HttpResponse
	 * @throws ConnectionException
	 */
	public HttpResponse execute(HttpGet request) throws ConnectionException {
		createConnection(request);
		return handleResponse();
	}

	/**
	 * Execute HttpPost request
	 * @param request
	 * @return HttpResponse
	 * @throws ConnectionException
	 */
	public HttpResponse execute(HttpPost request) throws ConnectionException {
		createConnection(request);
		_connection.setDoOutput(true);
		_connection.setRequestProperty("Content-Type", request.getContentType());

		try {
			OutputStreamWriter stream = new OutputStreamWriter(_connection.getOutputStream(), UTF8);
			stream.write(request.getData());
			stream.flush();
		} catch (IOException e) {
			int code;
			try {
				code = _connection.getResponseCode();
			} catch (Exception e1) {
				code = -1;
			}
			throw new ConnectionException("Error writing to output stream", code, e);
		}
		
		return handleResponse();
	}

	/**
	 * Begin a long running Http post request
	 * @param request
	 * @return HttpResponse
	 * @throws ConnectionException
	 */
	public HttpResponse beginRequest(HttpPost request) throws ConnectionException {

		createConnection(request);
		_connection.setDoOutput(true);
		_connection.setDoOutput(true);
		_connection.setDoInput(true);
		_connection.setUseCaches(false);
		_connection.setRequestProperty("Content-Type", request.getContentType());
		
		return new HttpResponseImpl(_connection);		
	}	

	/**
	 * End a long running Http post request
	 * @return HttpResponse
	 * @throws ConnectionException
	 */
	public HttpResponse endRequest() throws ConnectionException {

		if(_connection.getDoOutput())
		{
			try {
				_connection.getOutputStream().flush();
			} catch (IOException e) {
				throw new ConnectionException("Error flushing output stream",	e);
			}
		}
		cookiesManager.addCookie(_connection.getHeaderFields());
		return new HttpResponseImpl(_connection);		
	}
	
	/**
	 * Handle various HTTP Responses
	 * 
	 * @return
	 * @throws ConnectionException
	 */
	private HttpResponse handleResponse() throws ConnectionException
	{
		try {		
			switch(_connection.getResponseCode())
			{
				case HttpURLConnection.HTTP_OK :
					return handleSuccess();

				case HttpURLConnection.HTTP_MOVED_TEMP :
					return handleRedirect();
					
				case HttpURLConnection.HTTP_UNAUTHORIZED :
					return handleRetry();					
					
				default: {
					StringBuffer message = new StringBuffer("Received Error ");
					message.append(_connection.getResponseCode());
					message.append(" from URL ");
					message.append(_connection.getURL().toString());
					throw new ConnectionException(message.toString(), _connection.getResponseCode());
				}

			}
		} catch (IOException e) {
			throw new ConnectionException("Error processing result from URL " + _connection.getURL().getPath(), e);
		}		
	}

	/**
	 * Handle Http Success
	 * @return
	 */
	private HttpResponse handleSuccess() {	
		cookiesManager.addCookie(_connection.getHeaderFields());
		return new HttpResponseImpl(_connection);
	}
	
	/**
	 * Handle retry response
	 * @return
	 * @throws ConnectionException
	 */
	private HttpResponse handleRetry() throws ConnectionException {
		cookiesManager.addCookie(_connection.getHeaderFields());
		HttpGet retry = new HttpGet(_connection.getURL());
		_connection.disconnect();
		return execute(retry);		
	}

	/**
	 * Handle redirect request
	 * @return
	 * @throws ConnectionException
	 */
	private HttpResponse handleRedirect() throws ConnectionException {	

		cookiesManager.addCookie(_connection.getHeaderFields());
    	List<String> urlList = _connection.getHeaderFields().get("Location");
    	String redirectUrl = urlList.get(0);

    	HttpGet request;
		try {
			request = HttpGet.create(redirectUrl);
			_connection.disconnect();
		} catch (MalformedURLException e) {
			request = null;
		}    	
    	if(null == request) { // redirectUrl was invalid, let's try to build one

			StringBuffer buffer = new StringBuffer(_connection.getURL().getProtocol());
			buffer.append("://");
			buffer.append(_connection.getURL().getHost());
			buffer.append(redirectUrl);
			
			_connection.disconnect();
			
    		try {
    			request = HttpGet.create(buffer.toString());
    		} catch (MalformedURLException e) {
    			throw new ConnectionException("Invalid Redirect URL " + redirectUrl, e);
    		}    		
    	}
    	return execute(request);
	}
	
	/**
	 * Create the Http Connection
	 */
	private static final String COOKIE_PARAM = "Cookie";
	private void createConnection(HttpRequest request) throws ConnectionException {
		try {
			_connection = (HttpURLConnection) request.getUrl().openConnection();
			String localeName = Locale.getDefault().toString();
			localeName = localeName.replace("_", "-");
			_connection.setRequestProperty("Accept-Language", localeName);

			/**
			 * Don't setInstanceFollowRedirects to true.  
			 * You will be disappointed.
			 * You will waste hours trying to figure out why it's not working
			 * then you will discover it was never intended to work. 
			 * http://bugs.sun.com/bugdatabase/view_bug.do?bug_id=4620571
			 */
			_connection.setInstanceFollowRedirects(false);

			_connection.setRequestMethod(request.getRequestMethod());
			
//			cookiesManager.addCookiesToRequest(request);					
			String cookies = cookiesManager.getCookies();
			if (cookies != null) {
				_connection.setRequestProperty(COOKIE_PARAM, cookies);
			}	
// end addCookies to request that I copied over
			
			addHeaders(_connection);
			
//System.out.printf(">>>>>> Input=%s Output=%s\n", connection.getDoInput(), connection.getDoOutput());
			
		} catch (MalformedURLException e) {
			throw new ConnectionException("Invalid URL", e);
		} catch (IOException e) {
			throw new ConnectionException("Error Opening Connection", e);
		}
	}

	/**
	 * Additional headers for request to the VersionOne server
	 */
	public final Map<String, String> customHttpHeaders = new HashMap<String, String>();
	private void addHeaders(HttpURLConnection request) {
		for (String key : customHttpHeaders.keySet()) {
			request.setRequestProperty(key, customHttpHeaders.get(key));
		}
	}

	/**
	 * Implementation of HttpResponse
	 * 
	 * @author jerry
	 *
	 */
	class HttpResponseImpl implements HttpResponse  
	{		
		private HttpURLConnection request;

		public HttpResponseImpl(HttpURLConnection connection) {
			request = connection;
		}

		public InputStream getInputStream() throws IOException {
			return request.getInputStream();
		}

		public void close() {
			try {request.getInputStream().close();} catch (IOException e){}
			try {request.getOutputStream().close();} catch (IOException e){}			
		}

		@Override
		public String getContentType() {
			return request.getContentType();
		}

		@Override
		public OutputStream getOutputStream() throws IOException{
			return request.getOutputStream();
		}

		@Override
		public void dump(PrintStream out) {
			
			String contentType = request.getContentType();
			String charset = null;
			for (String param : contentType.replace(" ", "").split(";")) {
			    if (param.startsWith("charset=")) {
			        charset = param.split("=", 2)[1];
			        break;
			    }
			}
	
			if (charset != null) {
				
			    BufferedReader reader = null;
			    InputStreamReader stream = null;
			    try {
			    	
			    	stream = new InputStreamReader(request.getInputStream(), charset);
			        reader = new BufferedReader(stream);
			        for (String line; (line = reader.readLine()) != null;) {
			            out.println(line);
			        }

			    } catch (UnsupportedEncodingException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} finally {
			        if (reader != null) try { reader.close(); } catch (IOException logOrIgnore) {}
			    }
			} else {
			    // It's likely binary content, use InputStream/OutputStream.
				out.println("It's likely binary content");
			}	
		}

		@Override
		public String getResponseAuthority() throws Exception {
			return _connection.getURL().getProtocol() + "://" + _connection.getURL().getAuthority();  
		}
	}
}
