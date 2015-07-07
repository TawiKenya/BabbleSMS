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

import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;
import ke.co.tawi.babblesms.server.beans.maskcode.SMSSource;
import ke.co.tawi.babblesms.server.beans.smsgateway.TawiGateway;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.messagetemplate.MsgStatus;
import ke.co.tawi.babblesms.server.persistence.logs.OutgoingLogDAO;
import ke.co.tawi.babblesms.server.utils.StringUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.SSLContext;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.NameValuePair;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.entity.UrlEncodedFormEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
//import org.apache.http.conn.ssl.SSLContexts;
import org.apache.http.conn.ssl.TrustSelfSignedStrategy;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.message.BasicNameValuePair;
import org.apache.http.util.EntityUtils;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.validator.routines.UrlValidator;

import org.apache.log4j.Logger;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;


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
	
	private TawiGateway smsGateway;
		
	private List<Phone> phoneList;
	private SMSSource smsSource;
	private String message;
	
	private Account account;
	
	// To map between the number used to send and the phone object
	private Map<String,Phone> phoneMap;
	
	private boolean retry;
	
	private OutgoingLogDAO outgoingLogDAO;
	
	private UrlValidator urlValidator;
	
	private Logger logger;
	
	
	
	/**
	 * Disable default constructor
	 */
	private PostSMS() {}
	
		
	/**
	 * Contains what is required to send SMS using the Tawi SMS Gateway. 
	 * 
	 * @param smsGateway
	 * @param phoneList
	 * @param smsSource
	 * @param message
	 * @param account
	 * @param retry whether or not to keep retrying if the POST fails
	 */
	public PostSMS(TawiGateway smsGateway, List<Phone> phoneList, SMSSource smsSource, 
			String message, Account account, boolean retry) {
		this.smsGateway = smsGateway;
		this.phoneList = phoneList;
		this.smsSource = smsSource;
		this.message = message;
		
		this.account = account;
		
		this.retry = retry;
		
		retryCount = RETRY_NUMBER;
		
		urlValidator = new UrlValidator(UrlValidator.ALLOW_2_SLASHES + UrlValidator.ALLOW_LOCAL_URLS);	
		
		outgoingLogDAO = OutgoingLogDAO.getInstance(); 
		
		logger = Logger.getLogger(this.getClass());		
	}

	
	/**
	 * 
	 */
	@Override
	public void run() {
		HttpEntity responseEntity = null;
		Map<String,String> params;
		
		if(urlValidator.isValid(smsGateway.getUrl())) {		
						
			// Prepare the parameters to send
			params = new HashMap<>();
			
			params.put("username", smsGateway.getUsername());		
			params.put("password", smsGateway.getPasswd());
			params.put("source", smsSource.getSource());
			params.put("message", message);
			
			switch(smsSource.getNetworkuuid()) {
				case Network.SAFARICOM_KE:
					params.put("network", "safaricom_ke");
					break;
					
				case Network.AIRTEL_KE:
					params.put("network", "safaricom_ke");  // TODO: change to airtel_ke
					break;	
			}
			
			
			// When setting the destination, numbers beginning with '07' are edited
			// to begin with '254'
			phoneMap = new HashMap<>();
			StringBuffer phoneBuff = new StringBuffer();
			String phoneNum;
			
			for(Phone phone : phoneList) {
				phoneNum = phone.getPhonenumber();
				
				if(StringUtils.startsWith(phoneNum, "07")) {
					phoneNum = "254" + StringUtils.substring(phoneNum, 1);
				}
				
				phoneMap.put(phoneNum, phone);				
				phoneBuff.append(phoneNum).append(";");
			}
			
			
			params.put("destination", StringUtils.removeEnd(phoneBuff.toString(), ";"));
						
			
			
			// Push to the URL
			try {		
				URL url = new URL(smsGateway.getUrl());
							
				if(StringUtils.equalsIgnoreCase(url.getProtocol(), "http")) {
					responseEntity = doPost(smsGateway.getUrl(), params, retry);
										
				} 
//				else if(StringUtils.equalsIgnoreCase(url.getProtocol(), "https")) {
//					doPostSecure(smsGateway.getUrl(), params, retry);
//				}
							
			} catch (MalformedURLException e) {
				logger.error("MalformedURLException for URL: '" + smsGateway.getUrl() + "'");
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}// end 'if(urlValidator.isValid(urlStr))'
		
		
		// Process the response from the SMS Gateway
		// Assuming all is ok, it would have the following pattern:
		// requestStatus=ACCEPTED&messageIds=254726176878:b265ce23;254728932844:367941a36d2e4ef195;254724300863:11fca3c5966d4d
		if(responseEntity != null) {
			OutgoingLog outgoingLog;
			
			
            try {
            	String[] strTokens = StringUtils.split(EntityUtils.toString(responseEntity), '&');
            	String tmpStr = "", dateStr = "";
            	for(String str : strTokens) {
            		if(StringUtils.startsWith(str, "messageIds")) {
            			tmpStr = StringUtils.removeStart(str, "messageIds=");
            		} else if(StringUtils.startsWith(str, "datetime")) {
            			dateStr = StringUtils.removeStart(str, "datetime=");
            		}
            	}
				
            	strTokens = StringUtils.split(tmpStr, ';');
            	String phoneStr, uuid;
            	Phone phone;            	
            	DateTimeFormatter timeFormatter = ISODateTimeFormat.dateTimeNoMillis();
            	
            	for(String str : strTokens) {
            		phoneStr = StringUtils.split(str, ':')[0];
            		uuid = StringUtils.split(str, ':')[1];
            		phone = phoneMap.get(phoneStr);
            		
            		outgoingLog = new OutgoingLog();
            		outgoingLog.setUuid(uuid);
            		outgoingLog.setOrigin(smsSource.getSource());
            		outgoingLog.setMessage(message);
            		outgoingLog.setDestination(phone.getPhonenumber());
            		outgoingLog.setNetworkUuid(phone.getNetworkuuid());            		
            		outgoingLog.setMessagestatusuuid(MsgStatus.SENT);
            		outgoingLog.setSender(account.getUuid());
            		outgoingLog.setPhoneUuid(phone.getUuid());         
            		
            		// Set the date of the OutgoingLog to match the SMS Gateway time
            		
            		LocalDateTime datetime = timeFormatter.parseLocalDateTime(dateStr);
            		outgoingLog.setLogTime(datetime.toDate());
            		
            		outgoingLogDAO.put(outgoingLog);
            	}
				
			} catch (ParseException e) {
				logger.error("ParseException when reading responseEntity");
				logger.error(ExceptionUtils.getStackTrace(e));

			} catch (IOException e) {
				logger.error("IOException when reading responseEntity");
				logger.error(ExceptionUtils.getStackTrace(e));
			}
		}
	}
	
	
	/**
	 * 
	 * @param url
	 * @param params
	 * @param retry
	 */
	private HttpEntity doPost(String url, Map<String,String> params, boolean retry) {
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
						
            return response.getEntity();            
            
                        
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
		
		return null;
	}
	
	
	/**
	 * 
	 * @param url
	 * @param params
	 * @param retry
	 */
	/*private void doPostSecure(String url, Map<String,String> params, boolean retry) {				
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

			
//			For debugging purposes
//			 
//			
//			System.out.println("----------------------------------------");
//			System.out.println(response.getStatusLine());
//			Header[] headers = response.getAllHeaders();
//			for (int i = 0; i < headers.length; i++) {
//				System.out.println(headers[i]);
//			}
//			System.out.println("----------------------------------------");
//
//			System.out.println(EntityUtils.toString(responseEntity));
			
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
	}*/
}

