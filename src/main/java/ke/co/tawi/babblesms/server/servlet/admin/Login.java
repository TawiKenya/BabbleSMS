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
package ke.co.tawi.babblesms.server.servlet.admin;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Date;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.servlet.util.PropertiesConfig;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.jasypt.util.text.BasicTextEncryptor;


/**
 * Administrator account login servlet. 
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Login extends HttpServlet {

    
    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	// Error message provided when incorrect captcha is submitted
    final String ACCOUNT_SIGN_IN_BAD_CAPTCHA = "Sorry, the characters you entered did not "
            + "match those provided in the image. Please try again.";
    
    private BasicTextEncryptor textEncryptor;
    private String hiddenCaptchaStr = "";
    
    /*private Cache accountsCache, statisticsCache, statisticsByUsernameCache,
            purchasesCache, balancesCache;
    */
          
    private Logger logger;
    
    
    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        textEncryptor = new BasicTextEncryptor();
       // textEncryptor.setPassword(PropertiesConfig.getConfigValue("ENCRYPT_PASSWORD"));
        String ENCRYPT_PASSWORD = "Vuwachip2";
       textEncryptor.setPassword(ENCRYPT_PASSWORD);
       // CacheManager mgr = CacheManager.getInstance();
       
       logger = LogManager.getLogger(this.getClass());
    }


    /**
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
        HttpSession session = request.getSession(true);

       
        String username = StringUtils.trimToEmpty(request.getParameter("username"));
        String password = StringUtils.trimToEmpty(request.getParameter("password"));

        hiddenCaptchaStr = request.getParameter("captchaHidden");
        String captchaAnswer = request.getParameter("captchaAnswer").trim();

         

        if (!validateCaptcha(hiddenCaptchaStr, captchaAnswer)) {
            session.setAttribute(SessionConstants.ADMIN_SIGN_IN_ERROR_KEY, ACCOUNT_SIGN_IN_BAD_CAPTCHA);
            response.sendRedirect("index.jsp");

         // The username supplied does not match what is in the config file
        } else if (!StringUtils.equals(username, PropertiesConfig.getConfigValue("ADMIN_USERNAME"))) {
            session.setAttribute(SessionConstants.ADMIN_SIGN_IN_ERROR_KEY, SessionConstants.ADMIN_SIGN_IN_ERROR_KEY);
            response.sendRedirect("index.jsp");

        // The password supplied does not match what is in the config file	
        } else if (!StringUtils.equals(password, PropertiesConfig.getConfigValue("ADMIN_PASSWORD"))) {
            session.setAttribute(SessionConstants.ADMIN_SIGN_IN_ERROR_KEY, SessionConstants.ADMIN_SIGN_IN_ERROR_VALUE);
            response.sendRedirect("index.jsp");

            
        // The login is correct	
        } else {
            session.setAttribute(SessionConstants.ADMIN_SESSION_KEY, "admin");
            session.setAttribute(SessionConstants.ADMIN_LOGIN_TIME_KEY, new Date());

            //initCache();
            
            response.sendRedirect("accounts.jsp");
        }
    }

    
    /**
     *
     */
    private void initCache() {
    	/*List<Account> accounts = new ArrayList<>();
    	
    	Element element;
        List keys = accountsCache.getKeysWithExpiryCheck();
        
        for (Object key : keys) {
            element = accountsCache.get(key);
            Account account = (Account)element.getObjectValue();
            accounts.add(account);
        }
    	
        SessionStatistics sessionStatistics = null;
        
        for(Account account : accounts) {
            purchasesCache.put(new Element(account.getUuid(), smsPurchaseDAO.getClientPurchases(account)));
            balancesCache.put(new Element(account.getUuid(), smsBalanceDAO.getClientBalances(account)));
            
            // Cache statistics about this account
            // Also cache SMS Purchases and Balances
            sessionStatistics = mobi.tawi.smsgw.accountmgmt.session.SessionStatisticsFactory.getSessionStatistics(account);

            statisticsByUsernameCache.put(new Element(account.getUsername(), sessionStatistics)); 	// Username as the key
        }
        
        sessionStatistics = SessionStatisticsFactory.getSessionStatistics();
        statisticsCache.put(new Element(CacheVariables.CACHE_STATISTICS_ALL_ACCOUNTS_KEY, sessionStatistics));

        purchasesCache.put(new Element(CacheVariables.CACHE_MASTERPURCHASE_KEY, smsPurchaseDAO.getAllMasterPurchases()));
        
        */
    }

    
    /**
     * Checks to see that the captcha generated by the person and the captcha
     * submitted are equal. Case is ignored.
     *
     * @param systemCaptcha
     * @param userCaptchath
     * @return boolean
     */
    private boolean validateCaptcha(String encodedSystemCaptcha, String userCaptcha) {
        boolean valid = false;
        String decodedHiddenCaptcha = "";

        try {
            decodedHiddenCaptcha = textEncryptor.decrypt(URLDecoder.decode(encodedSystemCaptcha, "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException while validating administrator captcha.");
            logger.error(ExceptionUtils.getStackTrace(e));
        }

        if(StringUtils.equalsIgnoreCase(decodedHiddenCaptcha, userCaptcha)) {
            valid = true;
        }

        return valid;
    }
        

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

}
