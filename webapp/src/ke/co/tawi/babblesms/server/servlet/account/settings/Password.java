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
package ke.co.tawi.babblesms.server.servlet.account.settings;

import java.io.IOException;

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.mail.Email;
import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.SimpleEmail;
import org.apache.log4j.Logger;

/**
 * Tests our persistence implementation for shortcode and mask balances.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Password  extends HttpServlet{
	public Password(){
		super();
	}
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  doPost(request, response);
		 }
	
  
	static String password ="";

	private Logger logger=Logger.getLogger(this.getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	protected void doPost(HttpServletRequest request,HttpServletResponse response )throws
	ServletException, IOException{
		
		
		
		String username = request.getParameter("username").trim();
		String email = request.getParameter("email").trim();
		
		logger.info(username);
		logger.info(email);
	
			try{
				PassGen passGen = new PassGen();
				
			 Email emails = new SimpleEmail();
			 emails.setHostName("smtp.gmail.com");
			 emails.setSmtpPort(587);
			 emails.setDebug(true);
			 emails.setAuthentication("mwendapeter72@gmail.com", "peter*#mwenda");
			 emails.setStartTLSEnabled(true);
			 emails.setFrom("mwendapeter72@gmail.com");
			 emails.setSubject("Reset password");
			 emails.setMsg("Your passowrd is:"+ passGen.RadPass(password));
			 emails.addTo(email);
			 emails.send();
			 request.setAttribute("success",true);
			 
			}catch (EmailException e) {
				   request.setAttribute("success",false);
				   e.printStackTrace();
				  }
			 
	
		}
		
	public static boolean isValidEmailAddress(String email) {
		   boolean result = true;
		   try {
		      InternetAddress emailAddr = new InternetAddress(email);
		      emailAddr.validate();
		   } catch (AddressException ex) {
		      result = false;
		   }
		   return result;
		}
	
	
	
	
	
	
	
	
	
	
	

}
