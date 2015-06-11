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

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.servlet.util.PropertiesConfig;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.session.SessionStatistics;
import ke.co.tawi.babblesms.server.session.SessionStatisticsFactory;
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
	
	//private Cache accountsCache;
	
	final String ERROR_EMPTY_PARAMETERS = "You have to input a value.";
	final String ERROR_INVALID_PARAMETERS = "The username and email do not match.";
	//private final String SUCCESS = "We have sent a new password in your email.";
	
	static final long serialVersionUID = 1L;
  
	
  
	
	private Cache accountsCache, statisticsCache;
	
	
	
	/**
    *
    * @param config
    * @throws ServletException
    */
   @Override
   public void init(ServletConfig config) throws ServletException {
       super.init(config);

      
       CacheManager mgr = CacheManager.getInstance();
       accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
       statisticsCache = mgr.getCache(CacheVariables.CACHE_STATISTICS_BY_ACCOUNT);

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
        if ((element = accountsCache.get(username)) != null) {
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
				//account.setLogpassword(password);
				
				String body = "Hello " + username + ", your new password is: " + password;
				
				
				EmailUtil util = new EmailUtil(
						PropertiesConfig.getConfigValue("EMAIL_DEFAULT_EMAIL_FROM"), // from
						email, // to 
						"BabbleSMS Password Reset", // subject, 
						body, 
						PropertiesConfig.getConfigValue("EMAIL_OUTGOING_SMTP"), // outgoing SMTP 
					 	Integer.parseInt(PropertiesConfig.getConfigValue("EMAIL_OUTGOING_SMTP_PORT")) // outgoing SMTP port
						);
				util.start();
				
				//updateCache(account.getUuid());
				}
				
				//message
				session.setAttribute(SessionConstants.EMAIL_SEND_SUCCESS, null);
				response.sendRedirect("success.jsp");	
	
		}//end doPost

	
	private void updateCache(String accountuuid) {
        SessionStatistics statistics = SessionStatisticsFactory.getSessionStatistics(accountuuid);

        statisticsCache.put(new Element(accountuuid, statistics));
    }
	
	
}
