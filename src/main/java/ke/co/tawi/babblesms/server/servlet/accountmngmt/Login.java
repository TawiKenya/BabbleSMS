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

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.util.Date;

import javax.json.Json;
import javax.json.JsonObject;
import javax.json.JsonReader;
import javax.net.ssl.HttpsURLConnection;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jasypt.util.text.BasicTextEncryptor;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.servlet.util.FontImageGenerator;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.session.SessionStatistics;
import ke.co.tawi.babblesms.server.session.SessionStatisticsFactory;
import ke.co.tawi.babblesms.server.utils.security.SecurityUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Receives the request for an account holder to log in.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Login extends HttpServlet {

	private Logger logger;

    // Error message provided when incorrect captcha is submitted
    final String ACCOUNT_SIGN_IN_BAD_CAPTCHA = "Sorry, the characters you entered did not "
            + "match those provided in the image. Please try again.";

    private BasicTextEncryptor textEncryptor;

    private String hiddenCaptchaStr = "";

    private Cache accountsCache, statisticsCache;

    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        textEncryptor = new BasicTextEncryptor();
        textEncryptor.setPassword(FontImageGenerator.SECRET_KEY);

        CacheManager mgr = CacheManager.getInstance();
        accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
        statisticsCache = mgr.getCache(CacheVariables.CACHE_STATISTICS_BY_ACCOUNT);

        logger = Logger.getLogger(this.getClass());

    }

    
    /**
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    
    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.invalidate();  // This is in case the user had previously signed
            					   // in and his/her session is still active.
        }
        
        session = request.getSession(true);
        
        Account account = new Account();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String gRecaptchaResponse = request.getParameter("g-recaptcha-response");
        
        
       // hiddenCaptchaStr = request.getParameter("captchaHidden");
       // String captchaAnswer = request.getParameter("captchaAnswer").trim();
        
        Element element;
        if ((element = accountsCache.get(username)) != null) {
            account = (Account) element.getObjectValue();
        }
        
        if (account != null) {
            // Check that the system generated captcha and the user input for the captcha match
        	
        	if (validateCaptcha(gRecaptchaResponse ) == true) { // disable captcha check
            //if (validateCaptcha(gRecaptchaResponse ) == false) {        		
                session.setAttribute(SessionConstants.ACCOUNT_SIGN_IN_ERROR_KEY, ACCOUNT_SIGN_IN_BAD_CAPTCHA);
                response.sendRedirect("index.jsp");

            } else {
                // Correct login
                if (StringUtils.equals(SecurityUtil.getMD5Hash(password), account.getLogpassword())) {
                    updateCache(account.getUuid());
                    session.setAttribute(SessionConstants.ACCOUNT_SIGN_IN_ACCOUNTUUID, account.getUuid());
                    session.setAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY, username);

                    session.setAttribute(SessionConstants.ACCOUNT_SIGN_IN_TIME, String.valueOf(new Date().getTime()));

                    response.sendRedirect("account/inbox.jsp");

                    // Incorrect login, password not matching	
                } else {
                    session.setAttribute(SessionConstants.ACCOUNT_SIGN_IN_ERROR_KEY, SessionConstants.ACCOUNT_SIGN_IN_WRONG_PASSWORD);
                    response.sendRedirect("index.jsp");
                }
            }

        // This is also an incorrect login whereby the username does not exist.	
        } else { // end 'if(account != null)'
            session.setAttribute(SessionConstants.ACCOUNT_SIGN_IN_ERROR_KEY, SessionConstants.ACCOUNT_SIGN_IN_NO_EMAIL);
            response.sendRedirect("index.jsp");
        }        
    }

    
    
    
    
    public static final String url = "https://www.google.com/recaptcha/api/siteverify";
	public static final String secret = "6LdieygTAAAAAFBXbgIIbQ6MTH8T_9YENyjz_Pzs";
	private final static String USER_AGENT = "Mozilla/5.0";

	public static boolean validateCaptcha(String gRecaptchaResponse) throws IOException {
		if (gRecaptchaResponse == null || "".equals(gRecaptchaResponse)) {
			return false;
		}
		
		try{
		URL obj = new URL(url);
		HttpsURLConnection con = (HttpsURLConnection) obj.openConnection();

		// add reuqest header
		con.setRequestMethod("POST");
		con.setRequestProperty("User-Agent", USER_AGENT);
		con.setRequestProperty("Accept-Language", "en-US,en;q=0.5");

		String postParams = "secret=" + secret + "&response="
				+ gRecaptchaResponse;

		// Send post request
		con.setDoOutput(true);
		DataOutputStream wr = new DataOutputStream(con.getOutputStream());
		wr.writeBytes(postParams);
		wr.flush();
		wr.close();

		int responseCode = con.getResponseCode();
		

		BufferedReader in = new BufferedReader(new InputStreamReader(
				con.getInputStream()));
		String inputLine;
		StringBuffer response = new StringBuffer();

		while ((inputLine = in.readLine()) != null) {
			response.append(inputLine);
		}
		in.close();

	
		
		
		//parse JSON response and return 'success' value
		JsonReader jsonReader = Json.createReader(new StringReader(response.toString()));
		JsonObject jsonObject = jsonReader.readObject();
		jsonReader.close();
		
		return jsonObject.getBoolean("success");
		}catch(Exception e){
			
			return false;
		}
	}
    
    
    
    
    
    
    
    
    
    
    /**
     * Checks to see that the captcha generated by the person and the captcha
     * submitted are equal. Case is ignored.
     *
     * @param encodedSystemCaptcha
     * @param userCaptcha
     * @return boolean
     */
    
    
    /**
    private boolean validateCaptcha(String encodedSystemCaptcha, String userCaptcha) {
        boolean valid = false;
        String decodedHiddenCaptcha = "";

        try {
            decodedHiddenCaptcha = textEncryptor.decrypt(URLDecoder.decode(encodedSystemCaptcha, "UTF-8"));

        } catch (UnsupportedEncodingException e) {
            logger.error("UnsupportedEncodingException while trying to validate captcha.");
            logger.error(ExceptionUtils.getStackTrace(e));
        }

        if (StringUtils.equalsIgnoreCase(decodedHiddenCaptcha, userCaptcha)) {
            valid = true;
        }

        return valid;
    }
*/
    
    /**
     * @param accountuuid
     */
    private void updateCache(String accountuuid) {
        SessionStatistics statistics = SessionStatisticsFactory.getSessionStatistics(accountuuid);

        statisticsCache.put(new Element(accountuuid, statistics));
    }

}

