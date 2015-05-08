/**
 * Copyright 2015 Tawi Commercial Services Ltd
 * 
 * Licensed under the Open Software License, Version 3.0 (the “License”); you may
 * not use this file except in compliance with the License. You may obtain a copy
 * of the License at:
 * http://opensource.org/licenses/OSL-3.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package ke.co.tawi.babblesms.server.sendsms.tawismsgw;

import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Map;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.validator.routines.UrlValidator;

import org.apache.log4j.Logger;


/**
 * A thread that can send POST an SMS, to both unsecure HTTP and secure 
 * HTTPS. For HTTPs, the validity of the target SSL certificate is ignored.
 * <p>
 * The class is also responsible for the following:<br/>
 * - Logging the SMS as having been sent
 * - Deducting the credit of the account holder (this is deducted even before a 
 *   successful communication with the SMS Gateway)
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class PostSMS extends Thread {

	// Number of seconds to wait before Posting a message after an unsuccessful try
	final int RETRY_WAIT = 60;
	
	// Number of times to retry before quitting
	// This is currently calculated as the number of seconds in a day divided by
	// the RETRY_WAIT
	final int RETRY_NUMBER = 1450;
		
	private int retryCount;
	
	private String urlStr;
		
	private Map<String,String> params;
	private boolean retry;
	
	private UrlValidator urlValidator;
	
	private Logger logger;
	
	
	/**
	 * Disable default constructor
	 */
	private PostSMS() {}
	
	
	/**
	 * The parameter map must contain what is required to send SMS using the 
	 * Tawi SMS Gateway. 
	 * 
	 * @param urlStr the endpoint on the SMS Gateway that is used for sending SMS 
	 * @param params
	 * @param retry	whether or not to keep retrying if the POST fails
	 */
	public PostSMS(String urlStr, Map<String,String> params, boolean retry) {
		this.urlStr = urlStr;
		this.params = params;
		this.retry = retry;
		
		retryCount = RETRY_NUMBER;
		
		urlValidator = new UrlValidator(UrlValidator.ALLOW_2_SLASHES + UrlValidator.ALLOW_LOCAL_URLS);	
		
		logger = Logger.getLogger(this.getClass());
	}

	
	/**
	 * 
	 */
	@Override
	public void run() {
		
		if(urlValidator.isValid(urlStr)) {		
			try {		
				URL url = new URL(urlStr);
							
				if(StringUtils.equalsIgnoreCase(url.getProtocol(), "http")) {
					doPost(urlStr, params, retry);
					
				} else if(StringUtils.equalsIgnoreCase(url.getProtocol(), "https")) {
					doPostSecure(urlStr, params, retry);
				}
							
			} catch (MalformedURLException e) {
				logger.error("MalformedURLException for URL: '" + urlStr + "'");
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}// end 'if(urlValidator.isValid(urlStr))'
	}
	
	
	/**
	 * 
	 * @param url
	 * @param params
	 * @param retry
	 */
	private void doPost(String url, Map<String,String> params, boolean retry) {
		CloseableHttpClient httpclient = HttpClients.createDefault();
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		UrlEncodedFormEntity entity;
		
		Iterator<String> iter = params.keySet().iterator();
		String key;
		
		while(iter.hasNext()) {
			key = iter.next();
			formparams.add(new BasicNameValuePair(key, params.get(key)));
		}
		
		try {
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
            HttpPost httppost = new HttpPost(url);
            httppost.setEntity(entity);
            HttpResponse response = httpclient.execute(httppost);
						
            HttpEntity responseEntity = response.getEntity();
            
            //for debugging
            System.out.println(EntityUtils.toString(responseEntity));
                        
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException for URL: '" + url + "'");
			logger.error(ExceptionUtils.getStackTrace(e));
			
			if(retry) {
				try { Thread.sleep(RETRY_WAIT * 1000); } catch (InterruptedException e1) {	}
				retryCount --;
				if(retryCount > 0) {
					doPost(url, params, true);
				}	
			}// end 'if(retry)'
			
			
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException for URL: '" + url + "'");
			logger.error(ExceptionUtils.getStackTrace(e));
			
			if(retry) {
				try { Thread.sleep(RETRY_WAIT * 1000); } catch (InterruptedException e1) {	}
				retryCount --;
				if(retryCount > 0) {
					doPost(url, params, true);
				}	
			}// end 'if(retry)'
			
		} catch (IOException e) {
			logger.error("IOException for URL: '" + url + "'");
			logger.error(ExceptionUtils.getStackTrace(e));
			
			if(retry) {
				try { Thread.sleep(RETRY_WAIT * 1000); } catch (InterruptedException e1) {	}
				retryCount --;
				if(retryCount > 0) {
					doPost(url, params, true);
				}	
			}// end 'if(retry)'
		}
	}
	
	
	/**
	 * 
	 * @param url
	 * @param params
	 * @param retry
	 */
	private void doPostSecure(String url, Map<String,String> params, boolean retry) {				
		List<NameValuePair> formparams = new ArrayList<NameValuePair>();
		UrlEncodedFormEntity entity;
		
		Iterator<String> iter = params.keySet().iterator();
		String key;
		
		while(iter.hasNext()) {
			key = iter.next();
			formparams.add(new BasicNameValuePair(key, params.get(key)));
		}		
				
		try {
			KeyStore trustStore = KeyStore.getInstance(KeyStore.getDefaultType());
			
			// Trust own CA and all self-signed certs
			SSLContext sslcontext = SSLContexts.custom()
					.loadTrustMaterial(trustStore, new TrustSelfSignedStrategy())
					.build();
			
			// Allow TLSv1 protocol only
			SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
					sslcontext, new String[] { "TLSv1" }, null,
					SSLConnectionSocketFactory.ALLOW_ALL_HOSTNAME_VERIFIER);
			
			CloseableHttpClient httpclient = HttpClients.custom()
					.setSSLSocketFactory(sslsf).build();
			
			entity = new UrlEncodedFormEntity(formparams, "UTF-8");
			HttpPost httppost = new HttpPost(url);
			httppost.setEntity(entity);

			CloseableHttpResponse response = httpclient.execute(httppost);
			HttpEntity responseEntity = response.getEntity();

			/*
			 * For debugging purposes
			 */
			/*
			System.out.println("----------------------------------------");
			System.out.println(response.getStatusLine());
			Header[] headers = response.getAllHeaders();
			for (int i = 0; i < headers.length; i++) {
				System.out.println(headers[i]);
			}
			System.out.println("----------------------------------------");

			System.out.println(EntityUtils.toString(responseEntity));
			*/
		} catch (KeyStoreException e) {
			logger.error("KeyStoreException for URL: '" + url + "'");
			logger.error(ExceptionUtils.getStackTrace(e));	
			
		} catch (KeyManagementException e) {
			logger.error("KeyManagementException for URL: '" + url + "'");
			logger.error(ExceptionUtils.getStackTrace(e));	
			
		} catch (NoSuchAlgorithmException e) {
			logger.error("NoSuchAlgorithmException for URL: '" + url + "'");
			logger.error(ExceptionUtils.getStackTrace(e));
			
		} catch (UnsupportedEncodingException e) {
			logger.error("UnsupportedEncodingException for URL: '" + url + "'");
			logger.error(ExceptionUtils.getStackTrace(e));
			
		} catch (ClientProtocolException e) {
			logger.error("ClientProtocolException for URL: '" + url + "'");
			logger.error(ExceptionUtils.getStackTrace(e));
			
		} catch (IOException e) {
			logger.error("IOException for URL: '" + url + "'");
			logger.error(ExceptionUtils.getStackTrace(e));
			
			if(retry) {
				try { Thread.sleep(RETRY_WAIT * 1000); } catch (InterruptedException e1) {	}
				retryCount --;
				if(retryCount > 0) {
					doPost(url, params, true);
				}	
			}// end 'if(retry)'
		}
	}
}

