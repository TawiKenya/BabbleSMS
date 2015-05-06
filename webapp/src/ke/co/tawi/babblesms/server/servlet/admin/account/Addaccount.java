package ke.co.tawi.babblesms.server.servlet.admin.account;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountsDAO;
import ke.co.tawi.babblesms.server.cache.CacheVariables;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;

/**
 * Servlet used to add/edit and delete accounts.
 * <p>
 * Copyright (c) Tawi Ltd., July 7,2014
 *
 * @author <a href="mailto:josephk@tawi.mobi">Joseph Kimani</a>
 */
@WebServlet(name = "addaccount",
        urlPatterns = {"/addaccount", "/editaccount", "/deleteaccount"})
public class Addaccount extends HttpServlet {

    final String ERROR_NO_FIRSTNAME = "Please provide a First Name.";
    final String ERROR_NO_USERNAME = "Please provide a Username.";
    final String ERROR_INVALID_EMAIL = "Please provide a valid email address.";
    //final String ERROR_INVALID_PHONE = "Please provide a valid phone number.";
    final String ERROR_NO_LOGIN_PASSWD = "Please provide a website login password.";
    final String ERROR_LOGIN_PASSWD_MISMATCH = "The website login passwords that you have provided do not match.";
    final String ERROR_UNIQUENAME_EXISTS = "The Username provided already exists in the system.";
    final String ERROR_EMAIL_EXISTS = "The email provided already exists in the system.";
  

    private String firstName, lastName, username, email, loginPasswd, loginPasswd2, phone;

    // This is used to store parameter names and values from the form.
    private HashMap<String, String> paramHash;
    private EmailValidator emailValidator;
    //private PhoneValidator phoneValidator;

    private AccountsDAO accountsDAO;

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

        emailValidator = EmailValidator.getInstance();
       // phoneValidator = PhoneValidator.getInstance();

        accountsDAO = AccountsDAO.getInstance();

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

        String userPath = request.getServletPath();
        HttpSession session = request.getSession(false);

        // if add account is called
        if (userPath.equals("/addaccount")) {
            setClassParameters(request);

            initParamHash();
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_PARAMETERS, paramHash);

            // No First Name provided
            if (StringUtils.isBlank(firstName)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_NO_FIRSTNAME);

                // No Unique userName provided      
            } else if (StringUtils.isBlank(username)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_NO_USERNAME);

                // An invalid email provided    
            } else if (!emailValidator.isValid(email)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_INVALID_EMAIL);

          // An invalid phone provided    
          //  } else if (!phoneValidator.isValid(mobile)) {
            //    session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_INVALID_PHONE);



                // No website login password provided
            } else if (StringUtils.isBlank(loginPasswd) || StringUtils.isBlank(loginPasswd2)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_NO_LOGIN_PASSWD);

                // The website login passwords provided do not match
            } else if (!StringUtils.equals(loginPasswd, loginPasswd2)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_LOGIN_PASSWD_MISMATCH);

                // The username already exists in the system    
            } else if (existsUniqueName(username)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_UNIQUENAME_EXISTS);

                // The email already exists in the system       
            } else if (existsEmail(email)) {
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, ERROR_EMAIL_EXISTS);

            } else {
                // If we get this far then all parameter checks are ok.         
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_SUCCESS_KEY, "s");

                // Reduce our session data
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_PARAMETERS, null);
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, null);

                addAccount();
                session.setAttribute(SessionConstants.ADMIN_ADD_SUCCESS, "Account created successfully.");
            }

            response.sendRedirect("admin/accounts.jsp");
        } // if edit account is called
        else if (userPath.equals("/editaccount")) {
            String accountuuid = request.getParameter("accountuuid");
            String username = request.getParameter("username");
            String name = request.getParameter("name");
            String mobile = request.getParameter("mobile");
            int dailysmslimit = Integer.parseInt(request.getParameter("dailysmslimit"));
            String email = request.getParameter("email");
            String statusuuid = request.getParameter("statusuuid");

            Account account = new Account();
            account.setUsername(username);
            account.setName(name);
            account.setEmail(email);
            account.setMobile(mobile);
            account.setStatusuuid(statusuuid);
            account.setDailysmslimit(dailysmslimit);
            account.setUuid(accountuuid);

            if(accountsDAO.adminupdateAccount(account)){
                session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Account updated successfully.");
            } else {
                session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Account update failed.");
            }
            

            response.sendRedirect("admin/accounts.jsp");

        } // if delete account is called
        else if (userPath.equals("/deleteaccount")) {

            String accountuuid = request.getParameter("accountuuid");
            if(accountsDAO.deleteAccount(accountuuid)){
                session.setAttribute(SessionConstants.ADMIN_DELETE_SUCCESS, "Account deleted successfully.");
            } else {
                session.setAttribute(SessionConstants.ADMIN_DELETE_ERROR, "Account deletion failed.");
            }
            
            response.sendRedirect("admin/accounts.jsp");
        }
    }

    /**
     *
     */
    private void addAccount() {
        Account a = new Account();

        a.setName(firstName);
        a.setUsername(username);
        a.setLogpassword(loginPasswd);
        a.setEmail(email);
        a.setMobile(phone);
        a.setStatusuuid("396F2C7F-961C-5C12-3ABF-867E7FD029E6");

        accountsDAO.putAccount(a);           
        
        a = accountsDAO.getAccountByEmail(email);       // Ensures the account is populated with the correct ID
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

    /**
     * Set the class variables that represent form parameters.
     *
     * @param request
     */
    private void setClassParameters(HttpServletRequest request) {
        firstName = StringUtils.trimToEmpty(request.getParameter("name"));
        username = StringUtils.trimToEmpty(request.getParameter("username"));
        email = StringUtils.trimToEmpty(request.getParameter("email"));
        loginPasswd = StringUtils.trimToEmpty(request.getParameter("password"));
        loginPasswd2 = StringUtils.trimToEmpty(request.getParameter("password2"));
        phone = StringUtils.trimToEmpty(request.getParameter("mobile"));
      

    }

    /**
     * Place all the received parameters in our class HashMap.
     *
     */
    private void initParamHash() {
        paramHash = new HashMap<>();

        paramHash.put("firstName", firstName);
        paramHash.put("lastName", lastName);
        paramHash.put("uniqueName", username);
        paramHash.put("email", email);
        paramHash.put("loginPasswd", loginPasswd);
        paramHash.put("loginPasswd2", loginPasswd2);
        paramHash.put("phone", phone);
    }

    /**
     *
     * @param name
     * @return whether or not the unique name exists in the system
     */
    private boolean existsUniqueName(final String username) {
        boolean exists = false;

        if (accountsDAO.getAccountByName(username) != null) {
            exists = true;
        }

        return exists;
    }

    /**
     *
     * @param email
     * @return whether or not the unique name exists in the system
     */
    private boolean existsEmail(final String email) {
        boolean exists = false;

        if (accountsDAO.getAccountByEmail(email) != null) {
            exists = true;
        }

        return exists;
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
