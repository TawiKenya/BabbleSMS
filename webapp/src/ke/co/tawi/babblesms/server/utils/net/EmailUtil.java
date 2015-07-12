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
package ke.co.tawi.babblesms.server.utils.net;

import java.util.Arrays;

import org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.EmailException;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.commons.mail.DefaultAuthenticator;

import org.apache.log4j.Logger;

/**
 * Email utility
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class EmailUtil extends Thread {    
    
    private EmailValidator emailValidator = EmailValidator.getInstance();   

    private Logger logger = Logger.getLogger(this.getClass());
	
    private String from;
    private String[] to, cc, bcc; 
	private String subject, body, outgoingEmailServer;
	private int outgoingEmailPort;
    private String outgoingUsername, outgoingPassword;
    
	
    /**
     * Disable the default constructor
     */
    private EmailUtil() {}
    
    
    /**
     * @param from
     * @param to
     * @param cc
     * @param bcc
     * @param subject
     * @param body
     * @param outgoingEmailServer
     * @param outgoingEmailPort
     * @param outgoingUsername 
     * @param outgoingPassword 
     */
    public EmailUtil(String from, String[] to, String[] cc, String[] bcc, 
			String subject, String body, String outgoingEmailServer, int outgoingEmailPort,
			String outgoingUsername, String outgoingPassword) {
    	
    	this.from = from;
    	this.to = to;
    	this.cc = cc;
    	this.bcc = bcc;
    	this.subject = subject;
    	this.body = body;
    	this.outgoingEmailServer = outgoingEmailServer;
    	this.outgoingEmailPort = outgoingEmailPort;
    	this.outgoingUsername = outgoingUsername;
    	this.outgoingPassword = outgoingPassword;
    }
    
    
    /**
     * @param from
     * @param to
     * @param subject
     * @param body
     * @param outgoingEmailServer
     * @param outgoingEmailPort
     * @param outgoingUsername 
     * @param outgoingPassword 
     */
    public EmailUtil(String from, String to, String subject, String body, 
    		String outgoingEmailServer, int outgoingEmailPort,
    		String outgoingUsername, String outgoingPassword) {
    	
    	this.from = from;
    	this.to = new String[] {to};
    	this.cc = new String[] {};
    	this.bcc = new String[] {};
    	this.subject = subject;
    	this.body = body;
    	this.outgoingEmailServer = outgoingEmailServer;
    	this.outgoingEmailPort = outgoingEmailPort;
    	this.outgoingUsername = outgoingUsername;
    	this.outgoingPassword = outgoingPassword;
    }
	
     
    /**
     * Validates an email address
     * 
     * @param email 
     * @return boolean indicating the validity of the email
     */      
    public boolean validateEmail(String email) {
       
		return  emailValidator.isValid(email);		
 
	}
    
    
    /**
     * Validates multiple emails at once
     * 
     * @param emailsToValidate 
     * @return boolean indicating whether or not all the emails are valid
     */
    public boolean validateEmails(String[] emailsToValidate) {
        
         for(String email : emailsToValidate) {
           if(!validateEmail(email)){
               return false;
           }
         }
 
		 return true;	
	}



	/**
	 * @see java.lang.Thread#run()
	 */
	@Override
	public void run() {
		SimpleEmail email;
		
		try {
			email = new SimpleEmail();
			
			email.setHostName(outgoingEmailServer);
			email.setSmtpPort(outgoingEmailPort); 
			//email.setAuthenticator(new DefaultAuthenticator(outgoingUsername, outgoingPassword));
			//email.setSSLOnConnect(true);
			
			email.setFrom(from);						
			email.addTo(to);
			
			if(cc.length > 0) {
				email.addCc(cc);
			}
			
			if(bcc.length > 0) {
				email.addBcc(bcc);
			}
						
			email.setSubject(subject);
			email.setMsg(body);
			
			
			if(validateEmails(to)) {
				email.send();
				
			} else {
				logger.error("Invalid destinations in " + toString());
			}
			
			
		} catch(EmailException e) {
			logger.error("EmailException when trying to send out a SimpleEmail: " + this.toString());
			logger.error(ExceptionUtils.getStackTrace(e));
		}	
	}


	/**
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("EmailUtil [from=");
		builder.append(from);
		builder.append(", to=");
		builder.append(Arrays.toString(to));
		builder.append(", cc=");
		builder.append(Arrays.toString(cc));
		builder.append(", bcc=");
		builder.append(Arrays.toString(bcc));
		builder.append(", subject=");
		builder.append(subject);
		builder.append(", body=");
		builder.append(body);
		builder.append(", outgoingEmailServer=");
		builder.append(outgoingEmailServer);
		builder.append(", outgoingEmailPort=");
		builder.append(outgoingEmailPort);
		builder.append("]");
		
		return builder.toString();
	}
    
}
