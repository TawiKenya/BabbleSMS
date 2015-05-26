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

import org.apache.commons.mail.SimpleEmail;
import org.apache.commons.mail.EmailException;

import org.apache.commons.validator.routines.EmailValidator;

import org.apache.log4j.Logger;

/**
 * Email utility
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class EmailUtil {
    
    
    private static EmailValidator emailValidator = EmailValidator.getInstance();   

    private static Logger logger = Logger.getLogger(EmailUtil.class);
	
    
	/**
	 *  
	 * @param from
	 * @param to
	 * @param cc
	 * @param bcc
	 * @param subject
	 * @param body
	 * @param outgoingEmailServer
	 * @param outgoingEmailPort Outgoing SMTP port
	 */
	public static void sendEmail(String from, String[] to, String[] cc, String[] bcc, 
			String subject, String body, String outgoingEmailServer, int outgoingEmailPort) {
		
		SimpleEmail email;
		
		try {
			email = new SimpleEmail();
			
			email.setFrom(from);						
			email.addTo(to);
			email.addCc(cc);
			email.addBcc(bcc);
			
			email.setSubject(subject);
			email.setMsg(body);
			email.setHostName(outgoingEmailServer);
			email.setSmtpPort(outgoingEmailPort); 
			email.send();
			
		} catch(EmailException e) {
			logger.error("EmailException when trying to send out a SimpleEmail.");
			logger.error(e);
		}		 
	}
	
    
    
    /**
     * Send a simple email message
     * 
     * @param body
     * @param from
     * @param to
     * @param subject
     * @param outgoingEmailServer
     * @param outgoingEmailPort
     */
    public static void sendEmail(String from, String to,String subject, String body, 
    		String outgoingEmailServer, int outgoingEmailPort)  {
                
        SimpleEmail email;
		
		try {
			email = new SimpleEmail();
			
			email.setFrom(from);						
			email.addTo(to);
			
			email.setSubject(subject);
			email.setMsg(body);
			email.setHostName(outgoingEmailServer);
			email.setSmtpPort(outgoingEmailPort); 
			email.send();
			
		} catch(EmailException e) {
			logger.error("EmailException when trying to send out a SimpleEmail.");
			logger.error(e);
		}	
    }
    
     
    /**
     * Validates an email address
     * 
     * @param email 
     * @return boolean indicating the validity of the email
     */
      
    public static boolean validateEmail(String email) {
       
		return  emailValidator.isValid(email);		
 
	}
    
    
    /**
     * Validates multiple emails at once
     * 
     * @param emailsToValidate 
     * @return boolean indicating whether or not all the emails are valid
     */
    public static boolean validateEmails(String[] emailsToValidate) {
        
         for(String email : emailsToValidate){
           if(!validateEmail(email)){
               return false;
           }
         }
 
		 return true;	
	}
    
}
