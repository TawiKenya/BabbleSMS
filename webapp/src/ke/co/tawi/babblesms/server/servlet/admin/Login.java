package ke.co.tawi.babblesms.server.servlet.admin;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.servlet.util.PropertiesConfig;

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

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import org.jasypt.util.text.BasicTextEncryptor;

/**
 *
 * @author josephk
 */
public class Login extends HttpServlet {

    
    // Error message provided when incorrect captcha is submitted
    final String ACCOUNT_SIGN_IN_BAD_CAPTCHA = "Sorry, the characters you entered did not "
            + "match those provided in the image. Please try again.";
    
    private BasicTextEncryptor textEncryptor;
    private String hiddenCaptchaStr = "";
    
    private Cache accountsCache, statisticsCache, statisticsByUsernameCache,
            purchasesCache, balancesCache;
    
    //private SmsPurchaseDAO smsPurchaseDAO;
    //private SmsBalanceDAO smsBalanceDAO;
        
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
        textEncryptor.setPassword(PropertiesConfig.getConfigValue("ENCRYPT_PASSWORD"));

        //smsPurchaseDAO = SmsPurchaseDAO.getInstance();
        //smsBalanceDAO = SmsBalanceDAO.getInstance();

        CacheManager mgr = CacheManager.getInstance();
        //accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
        //statisticsCache = mgr.getCache(CacheVariables.CACHE_STATISTICS_FOR_ALL_ACCOUNTS);
        //statisticsByUsernameCache = mgr.getCache(CacheVariables.CACHE_STATISTICS_BY_USERNAME);

        //urchasesCache = mgr.getCache(CacheVariables.CACHE_CLIENTPURCHASE_BY_ACCOUNTUUID);
        //balancesCache = mgr.getCache(CacheVariables.CACHE_CLIENTBALANCE_BY_ACCOUNTUUID);

        logger = Logger.getLogger(this.getClass());
    }


    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
           throws ServletException, IOException {
        HttpSession session = request.getSession(true);

        String username = StringUtils.trimToEmpty(request.getParameter("username"));
        String password = StringUtils.trimToEmpty(request.getParameter("password"));

        hiddenCaptchaStr = request.getParameter("captchaHidden");
        String captchaAnswer = request.getParameter("captchaAnswer").trim();

        if (!validateCaptcha(hiddenCaptchaStr, captchaAnswer)) {
            session.setAttribute(SessionConstants.ADMIN_SIGN_IN_ERROR_KEY, ACCOUNT_SIGN_IN_BAD_CAPTCHA);
            response.sendRedirect("admin/index.jsp");

        } // The username supplied does not match what is in the config file
        else if (!StringUtils.equals(username, PropertiesConfig.getConfigValue("ADMIN_USERNAME"))) {
            session.setAttribute(SessionConstants.ADMIN_SIGN_IN_ERROR_KEY, SessionConstants.ADMIN_SIGN_IN_ERROR_KEY);
            response.sendRedirect("admin/index.jsp");

            // The password supplied does not match what is in the config file	
        } else if (!StringUtils.equals(password, PropertiesConfig.getConfigValue("ADMIN_PASSWORD"))) {
            session.setAttribute(SessionConstants.ADMIN_SIGN_IN_ERROR_KEY, SessionConstants.ADMIN_SIGN_IN_ERROR_VALUE);
            response.sendRedirect("admin/index.jsp");

            // The login is correct	
        } else {
            session.setAttribute(SessionConstants.ADMIN_SESSION_KEY, "admin");
            session.setAttribute(SessionConstants.ADMIN_LOGIN_TIME_KEY, new Date());

            //initCache();
            
            response.sendRedirect("admin/accounts.jsp");
        
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

    
    /**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }

}
