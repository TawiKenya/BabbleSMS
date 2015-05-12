
package ke.co.tawi.babblesms.server.servlet.client;

import java.io.IOException;
import java.io.PrintWriter;

import static java.lang.System.out;

import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;

/**
 * Servlet for editing client account settings
 * <p>
 * Copyright (c) Tawi Ltd., July 7,2014
 *
 * @author <a href="mailto:josephk@tawi.mobi">Joseph Kimani</a>
 */
public class Editsetting extends HttpServlet {

    final String ERROR_NO_NAMES = "Please provide name(s).";
    final String ERROR_NO_USERNAME = "Please provide a username.";
    final String ERROR_NO_MOBILE = "Please provide a phone Number.";
    final String ERROR_NO_PASSWORD = "Please provide a password.";
    final String ERROR_NO_EMAIL = "Please provide an email.";
    final String ERROR_EMAIL_EXISTS="Email supplied already used by another account";
    
   
    private String accuuid,names,username,mobile,email,password;
    
    // This is used to store parameter names and values from the form.
    private HashMap<String, String> paramHash;
    
    
    private AccountDAO accountsDAO;    
    private CacheManager cacheManager;
    private HttpSession session;
    

    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        
        accountsDAO = AccountDAO.getInstance();
      
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
        session = request.getSession(true);

        setClassParameters(request);
       
        initParamHash();
        session.setAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_PARAMETERS, paramHash);

        // No names provided
        if (StringUtils.isBlank(names)) {
            session.setAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_ERROR_KEY, ERROR_NO_NAMES);

            // No username provided	
        } else if (StringUtils.isBlank(username)) {
            session.setAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_ERROR_KEY, ERROR_NO_USERNAME);

            // No mobile provided	
        } else if (StringUtils.isBlank(mobile)) {
            session.setAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_ERROR_KEY, ERROR_NO_MOBILE);

          // No password provided	
        } else if (StringUtils.isBlank(password)) {
            session.setAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_ERROR_KEY, ERROR_NO_PASSWORD);
 
            // No email provided	
        } else if (StringUtils.isBlank(email)) {
            session.setAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_ERROR_KEY, ERROR_NO_EMAIL);
       
            
        } else {
            // If we get this far then all parameter checks are ok.		
            session.setAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_SUCCESS_KEY, "s");

            // Reduce our session data
            session.setAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_PARAMETERS, null);
            session.setAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_ERROR_KEY, null);

            addAccount();            
        }

        response.sendRedirect("account/setting.jsp");
    }

    /**
     *
     */
    private void addAccount() {
       Account a = new Account();
         
        a.setUuid(accuuid);
        a.setName(names);
        a.setUsername(username);
        a.setLogpassword(password);
        a.setEmail(email);
        a.setMobile(mobile);
        

        

        // accountDAO.updateAccount(a);
        
        
        updateAccountCache(a);
       
    }

    
    /**
     *
     * @param acc
     */
    private void updateAccountCache(Account a) {
    	cacheManager.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID).put(new Element(a.getUuid(), a));
    }

    
    /**
     * Set the class variables that represent form parameters.
     *
     * @param request
     */
    private void setClassParameters(HttpServletRequest request) {
        accuuid = StringUtils.trimToEmpty(request.getParameter("accuuid"));
        names = StringUtils.trimToEmpty(request.getParameter("names"));
        username = StringUtils.trimToEmpty(request.getParameter("username"));
        password = StringUtils.trimToEmpty(request.getParameter("password"));
        email = StringUtils.trimToEmpty(request.getParameter("email"));
        mobile = StringUtils.trimToEmpty(request.getParameter("phone"));
        
    }

    
    /**
     * Place all the received parameters in our class HashMap.
     *
     */
    private void initParamHash() {
        paramHash = new HashMap<>();

        paramHash.put("accuuid", accuuid);
        paramHash.put("names", names);
        paramHash.put("username", username);
        paramHash.put("password", password);
        paramHash.put("email", email);
        paramHash.put("mobile", mobile);
        
    }
    
   
    /**
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
