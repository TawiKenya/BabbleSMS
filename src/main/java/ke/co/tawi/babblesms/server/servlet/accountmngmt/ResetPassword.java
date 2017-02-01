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
package ke.co.tawi.babblesms.server.servlet.accountmngmt;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.servlet.util.PropertiesConfig;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.utils.net.EmailUtil;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Receives a form request to reset a password of an account holder.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class ResetPassword  extends HttpServlet {
	
		
	final String ERROR_EMPTY_PARAMETERS = "You have to input a value.";
	final String ERROR_INVALID_PARAMETERS = "The username and email do not match.";
			
	private Cache accountsUsernameCache, accountsUuidCache;
		
	
	/**
    *
    * @param config
    * @throws ServletException
    */
   @Override
   public void init(ServletConfig config) throws ServletException {
       super.init(config);

      
       CacheManager mgr = CacheManager.getInstance();
       accountsUsernameCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
       accountsUuidCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID);

   }
	
	
	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
			HttpSession session = request.getSession(false);
			
		    Account account = new Account();   
	    
			String username = request.getParameter("username").trim();
			String email = request.getParameter("email").trim();
	   
			Element element;
	        if ((element = accountsUsernameCache.get(username)) != null) {
	            account = (Account) element.getObjectValue();
	        }
			
		    		
			if(StringUtils.isBlank(username) || StringUtils.isBlank(email)) {
				
				session.setAttribute(SessionConstants.EMAIL_SEND_ERROR, ERROR_EMPTY_PARAMETERS);
				response.sendRedirect("resetPassword.jsp");
		
			// Check to see whether the username and email match
			} else if(!username.equalsIgnoreCase(account.getUsername()) 
					|| !email.equalsIgnoreCase(account.getEmail())) {
				
				session.setAttribute(SessionConstants.EMAIL_SEND_ERROR, ERROR_INVALID_PARAMETERS);
				response.sendRedirect("resetPassword.jsp");
				
				
			// The username and email match	
			} else {
				
					String password = RandomStringUtils.randomAlphabetic(5);
					account.setLogpassword(password);
					
					String body = "Hello " + username + ", your new password is: " + password;
					
					
					EmailUtil util = new EmailUtil(
							PropertiesConfig.getConfigValue("EMAIL_DEFAULT_EMAIL_FROM"), // from
							email, // to 
							"SMS Password Reset", // subject, 
							body, 
							PropertiesConfig.getConfigValue("EMAIL_OUTGOING_SMTP"), // outgoing SMTP host
						 	Integer.parseInt(PropertiesConfig.getConfigValue("EMAIL_OUTGOING_SMTP_PORT")), // outgoing SMTP port
						 	PropertiesConfig.getConfigValue("EMAIL_OUTGOING_SMTP_USERNAME"), // outgoing SMTP username 
						 	PropertiesConfig.getConfigValue("EMAIL_OUTGOING_SMTP_PASSWORD") // outgoing SMTP password
							);
					util.start();
					
					updateCache(account);
					
					session.setAttribute(SessionConstants.EMAIL_SEND_SUCCESS, null);
					response.sendRedirect("successResetPasswd.jsp");	
			}					
	
	}//end doPost

	
	/**
	 * @param account
	 */
	private void updateCache(Account account) {
		accountsUsernameCache.put(new Element(account.getUsername(), account));
		accountsUuidCache.put(new Element(account.getUuid(), account));		
    }	
	
	
	private static final long serialVersionUID = 3380539156339604660L;
}
