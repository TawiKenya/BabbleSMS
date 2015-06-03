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

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.session.SessionStatistics;
import ke.co.tawi.babblesms.server.session.SessionStatisticsFactory;
import ke.co.tawi.babblesms.server.utils.net.EmailUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * 
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Password  extends HttpServlet{
	
	//private Cache accountsCache;
	
	private final String ERROR_NO_USERN_PASS = "You have to input a value.";
	private final String ERROR_NO_USER_EMAIL = "username or email not found.";
	//private final String SUCCESS = "We have sent a new password in your email.";
	
	
  
	
  
	static String password ="";
	private Cache accountsCache, statisticsCache;
	private Logger logger=Logger.getLogger(this.getClass());
	
	
	
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
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		  doPost(request, response);
		 }
	
	
	
	private static final long serialVersionUID = 1L;
	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request,HttpServletResponse response )throws
	ServletException, IOException{
		
		 HttpSession session = request.getSession(false);
		 if (session != null) {
	            session.invalidate();  
	        }
	        session = request.getSession(true);
	    Account account = new Account();   
	    
		String username = request.getParameter("username").trim();
		String email = request.getParameter("email").trim();
	   
		Element element;
        if ((element = accountsCache.get(username)) != null) {
            account = (Account) element.getObjectValue();
        }
		
		logger.info(username);
		logger.info(email);
		
	    		
	if(username.equalsIgnoreCase("") || email.equalsIgnoreCase("")){
		//message
		session.setAttribute(SessionConstants.EMAIL_SEND_ERROR, ERROR_NO_USERN_PASS);
		response.sendRedirect("forgotpas.jsp");
	}else if(!username.equalsIgnoreCase(account.getUsername()) || !email.equalsIgnoreCase(account.getEmail())){
		//message
		session.setAttribute(SessionConstants.EMAIL_SEND_ERROR, ERROR_NO_USER_EMAIL);
		response.sendRedirect("forgotpas.jsp");
	}
	
	else{
		
		String x = account.getLogpassword();
	    String z = account.getMobile();
		logger.info(x);
		logger.info(z);
		
			password = RandomStringUtils.randomAlphabetic(10);
			account.setLogpassword(password);
			
			String from="mwenda@tawi.mobi";
			String to=email;
			String subject=" BalbbleSMS password reset";
			String body="hello  "+username+"  your new password is....."+  password;
			String outServ="mail.tawi.mobi";
			int outPort = 25;
			EmailUtil.sendEmail(from, to, subject, body, outServ, outPort);
			
			logger.info(password);
			updateCache(account.getUuid());
			}
			
			//message
			session.setAttribute(SessionConstants.EMAIL_SEND_SUCCESS,null);
			response.sendRedirect("success.jsp");
	
	
	
	String x = account.getLogpassword();
	String y = account.getEmail();
	String z = account.getMobile();
	logger.info(x);
	logger.info(y);
	logger.info(z);
	
	
	
	
	
		}//end doPost

	private void updateCache(String accountuuid) {
        SessionStatistics statistics = SessionStatisticsFactory.getSessionStatistics(accountuuid);

        statisticsCache.put(new Element(accountuuid, statistics));
    }
	
	
}//end class
