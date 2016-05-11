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
package ke.co.tawi.babblesms.server.servlet.admin.account;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.account.Status;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * Servlet used to add Accounts.
 * <p>
 *
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class AddAccount extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String ERROR_NO_NAME = "Please provide a Name.";
    final String ERROR_NO_USERNAME = "Please provide a Username.";
    final String ERROR_INVALID_EMAIL = "Please provide a valid email address.";
    final String ERROR_NO_LOGIN_PASSWD = "Please provide a website login password.";
    final String ERROR_LOGIN_PASSWD_MISMATCH = "The website login passwords that you have provided do not match.";
    final String ERROR_UNIQUENAME_EXISTS = "The Username provided already exists in the system.";
    final String ERROR_EMAIL_EXISTS = "The email provided already exists in the system.";
   
    private EmailValidator emailValidator;

    private AccountDAO accountDAO;
    private CacheManager cacheManager;
    
    
    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        emailValidator = EmailValidator.getInstance();

        accountDAO = AccountDAO.getInstance();

        cacheManager = CacheManager.getInstance();
    }

    
    /**
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	
    	HttpSession session = request.getSession(false); 
    	
    	String name = StringUtils.trimToEmpty(request.getParameter("name"));
    	String username = StringUtils.trimToEmpty(request.getParameter("username"));
    	String email = StringUtils.trimToEmpty(request.getParameter("email"));
    	String loginPasswd = StringUtils.trimToEmpty(request.getParameter("password"));
    	String loginPasswd2 = StringUtils.trimToEmpty(request.getParameter("password2"));
    	String phone = StringUtils.trimToEmpty(request.getParameter("phone"));  
   
            	
    	// This is used to store parameter names and values from the form.
    	Map<String, String> paramHash = new HashMap<>();    	
    	paramHash.put("name", name);
    	paramHash.put("username", username);
    	paramHash.put("email", email);
    	paramHash.put("phone", phone);
   
        

        // No Name provided
        if (StringUtils.isBlank(name)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_NO_NAME);

            // No Unique username provided      
        } else if (StringUtils.isBlank(username)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_NO_USERNAME);

            // usernane exist   
        }else if(userNameExist(username)){
        	session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, "username already exist in the System");
        }
        else if (!emailValidator.isValid(email)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_INVALID_EMAIL);

            // No website login password provided
        } else if (StringUtils.isBlank(loginPasswd) || StringUtils.isBlank(loginPasswd2)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_NO_LOGIN_PASSWD);
        
            // The website login passwords provided do not match
        } else if (!StringUtils.equals(loginPasswd, loginPasswd2)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_LOGIN_PASSWD_MISMATCH);
           
           
        } else {
        	// If we get this far then all parameter checks are ok.         
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_SUCCESS_KEY, "s");
            
            // Reduce our session data
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_PARAMETERS, null);
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, null);
        	
        	 addAccount(name, username, email, loginPasswd, phone);
        	 
             session.setAttribute(SessionConstants.ADMIN_ADD_SUCCESS, "Account created successfully.");
        }
        
        session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_PARAMETERS, paramHash);

        response.sendRedirect("addaccount.jsp");
    }
    

   
    private boolean userNameExist(String username) {
		boolean exist = false;
		if(accountDAO.getAccountByName(username)!=null){
			exist = true;
		}
		
		return exist;
	}


	/**
     * @param name
     * @param username
     * @param email
     * @param loginPasswd
     * @param phone
     */
    private void addAccount(String name, String username, String email, String loginPasswd, 
    		String phone) {
        Account a = new Account();

        a.setName(name);
        a.setUsername(username);
        a.setLogpassword(loginPasswd);
        a.setEmail(email);
        a.setMobile(phone);
        a.setStatusuuid(Status.ACTIVE);

        accountDAO.putAccount(a);           
        
        a = accountDAO.getAccountByName(username);      // Ensures the account is populated with the correct ID
        updateAccountCache(a);
    }
    

    /**
     *
     * @param acc
     */
    private void updateAccountCache(Account acc) {
        cacheManager.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME).put(new Element(acc.getUsername(), acc));
        cacheManager.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID).put(new Element(acc.getUuid(), acc));
    }

}
