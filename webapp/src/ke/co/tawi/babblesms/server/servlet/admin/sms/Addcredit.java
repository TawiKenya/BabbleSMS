package ke.co.tawi.babblesms.server.servlet.admin.sms;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.beans.account.Credit;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.items.credit.CreditDAO;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;

/**
 * Servlet used to add account credit.
 * <p>
 * Copyright (c) Tawi Ltd., July 7,2014
 *
 * @author <a href="mailto:josephk@tawi.mobi">Joseph Kimani</a>
 */
public class Addcredit extends HttpServlet {

    final String ERROR_NO_ACCOUNTNAME = "No Account selected.";
    final String ERROR_NO_AMOUNT = "Please provide a value of client credits that are greater than zero.";
    final String ERROR_INVALID_AMOUNT = "Please provide a numeric value of client credits that are greater than zero.";
    final String ERROR_INVALID_SOURCE = "Please provide a source that belongs to the account.";

    private String sourceuuid, accountuuid, amount;

    // This is used to store parameter names and values from the form.
    private HashMap<String, String> paramHash;

    private CreditDAO creditDAO;

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

        creditDAO = CreditDAO.getInstance();

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
        session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_PARAMETERS, paramHash);

        // No source provided
        if (StringUtils.isBlank(sourceuuid)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_ERROR_KEY, ERROR_INVALID_SOURCE);
        
        //amount is not provided
        } else if (StringUtils.isBlank(amount)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_ERROR_KEY, ERROR_NO_AMOUNT);
         
            // Amount provided is not numeric value.	
        } else if (!StringUtils.isNumeric(amount)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_ERROR_KEY, ERROR_INVALID_AMOUNT);

            // No account provided	
        } else if (StringUtils.isBlank(accountuuid)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_ERROR_KEY, ERROR_NO_ACCOUNTNAME);

        } else {
            // If we get this far then all parameter checks are ok.		
            session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_SUCCESS_KEY, "s");

            // Reduce our session data
            session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_PARAMETERS, null);
            session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_ERROR_KEY, null);

            addCredit();
        }

        response.sendRedirect("admin/credit.jsp");
    }

    /**
     *
     */
    private void addCredit() {
        Credit c = new Credit();

        c.setSource(sourceuuid);
        c.setCredit(Integer.parseInt(amount));
        c.setAccountuuid(accountuuid);

       c = creditDAO.getCreditbysource(sourceuuid, accountuuid);	// Ensures the credit is populated with the correct ID

        String uuid = c.getUuid();
        //if successfully updated set session variable.
        if(creditDAO.updateCredit(uuid, Integer.parseInt(amount))){
            session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Credit updated successfully.");
        }
        else{
           session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Credit update failed.");
        } 
       

        //updateCreditCache(c);
    }

    /**
     * populate purchase history table
     *
     *
     * Private void addpurchaseHistory(){
     *
     * }
     */
    /**
     *
     * @param acc
     */
    private void updateCreditCache(Credit cr) {
        //cacheManager.getCache(CacheVariables.CACHE_MASK_BY_UUID).put(new Element(cr.getUuid(), cr));
    }

    /**
     * Set the class variables that represent form parameters.
     *
     * @param request
     */
    private void setClassParameters(HttpServletRequest request) {
        amount = StringUtils.trimToEmpty(request.getParameter("amount"));
        sourceuuid = StringUtils.trimToEmpty(request.getParameter("source"));
        accountuuid = StringUtils.trimToEmpty(request.getParameter("user"));

    }

    /**
     * Place all the received parameters in our class HashMap.
     *
     */
    private void initParamHash() {
        paramHash = new HashMap<>();

        paramHash.put("amount", amount);
        paramHash.put("sourceuuid", sourceuuid);
        paramHash.put("accountuuid", accountuuid);

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
