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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.utils.net.EmailUtil;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Password  extends HttpServlet{
	public Password(){
		super();
	}
	
	private final String ERROR_NO_USERN_PASS = "You have to input a value.";
	private final String ERROR_NO_USER_EMAIL = "username or email not found.";
	private final String SUCCESS = "We have sent a new password in your email.";
	
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
		 HttpSession session = request.getSession(false);
		
		
		String username = request.getParameter("username").trim();
		String email = request.getParameter("email").trim();
		
		logger.info(username);
		logger.info(email);
		
		Account acco = new Account();
		String us = acco.getUsername();
		String em =acco.getEmail();
		String lp =acco.getLogpassword();
		System.out.println(us);
		System.out.println(em);
		System.out.println(lp);
		logger.info(us);
		logger.info(em);
		logger.info(lp);
		
		
	if(username.equalsIgnoreCase("") || email.equalsIgnoreCase("")){
		//message
		session.setAttribute(SessionConstants.EMAIL_SEND_ERROR, ERROR_NO_USERN_PASS);
		response.sendRedirect("forgotpas.jsp");
	}else if(existUser(username) || existEmail(email)){
		//message
		session.setAttribute(SessionConstants.EMAIL_SEND_ERROR, ERROR_NO_USER_EMAIL);
		response.sendRedirect("forgotpas.jsp");
	}
	
	else{
			password = RandomStringUtils.randomAlphabetic(10);
			
			putPassword();
			
			if(putPassword()){
			
			String from="mwenda@tawi.mobi";
			String to=email;
			String subject=" BalbbleSMS password reset";
			String body="hello  "+username+"  your new password is....."+  password;
			String outServ="mail.tawi.mobi";
			int outPort = 25;
			EmailUtil.sendEmail(from, to, subject, body, outServ, outPort);
			
			session.setAttribute(SessionConstants.EMAIL_SEND_SUCCESS, SUCCESS);
			response.sendRedirect("success.jsp");
			}
			//message
			
			response.sendRedirect("success.jsp");
			 
	}//end else
	
		}//end doPost

	private boolean existEmail(String email) {
		boolean eml = true;
		Account acc = new Account();
		String e = acc.getUsername();
		logger.info(e);
		
		if(acc.getEmail().equalsIgnoreCase(email)){
			eml = false;
		}
		return eml;
	}
	

	private boolean existUser(String username) {
		boolean user = true;
		Account acc = new Account();
		String u = acc.getUsername();
		logger.info(u);
		
		if(acc.getUsername().equalsIgnoreCase(username)){
			
			user = false;
		}
		return user;
	}

	private boolean putPassword() {
		Account account = new Account();
		account.setLogpassword(password);
		return true;
		
	}
	
}//end class
