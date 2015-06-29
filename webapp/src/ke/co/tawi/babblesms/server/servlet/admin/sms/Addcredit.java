package ke.co.tawi.babblesms.server.servlet.admin.sms;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.beans.creditmgmt.MaskPurchase;
import ke.co.tawi.babblesms.server.beans.creditmgmt.ShortcodePurchase;
import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsPurchaseDAO;
import ke.co.tawi.babblesms.server.persistence.items.maskcode.MaskDAO;
import ke.co.tawi.babblesms.server.persistence.items.maskcode.ShortcodeDAO;

import java.io.IOException;
import java.util.Date;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;

/**
 * Servlet used to add account credit.
 * <p>
 * Copyright (c) Tawi Ltd., July 7,2014
 *@author <a href="mailto:michael@tawi.mobi">peter</a>
 * @author <a href="mailto:mwenda@tawi.mobi">peter</a>
 */
public class Addcredit extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 7390514626774104095L;
	final String ERROR_NO_ACCOUNTNAME = "No Account selected.";
    final String ERROR_NO_AMOUNT = "Please provide a value of client credits that are greater than zero.";
    final String ERROR_INVALID_AMOUNT = "Please provide a numeric value of client credits that are greater than zero.";
    final String ERROR_INVALID_SOURCE = "Please provide a source that belongs to the account.";
    private final MaskDAO maskDAO = MaskDAO.getInstance();
    private final ShortcodeDAO scodeDAO = ShortcodeDAO.getInstance();
    private SmsPurchaseDAO smspurchaseDAO;
    private HttpSession session;
    ShortcodePurchase shortcodep;
    MaskPurchase maskp;
    Mask mask = new Mask();
    Shortcode scode = new Shortcode();
	
	
    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        smspurchaseDAO = SmsPurchaseDAO.getInstance();
        shortcodep = new ShortcodePurchase();
        maskp = new  MaskPurchase();
    }
  

    /**
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        session = request.getSession(true);
        long Dates = System.currentTimeMillis();
       
       String amount = StringUtils.trimToEmpty(request.getParameter("amount"));
       String sourceuuid = StringUtils.trimToEmpty(request.getParameter("source")); 
       String  accountuuid = StringUtils.trimToEmpty(request.getParameter("user"));
       
       
       int count = Integer.parseInt(amount);

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
        	//this lists holds mask details
        	List<Mask> maskList = maskDAO.getmaskbyaccount(accountuuid);
        	//this lists holds shortcode details
        	List<Shortcode> shortcodeList = scodeDAO.getShortcodebyaccountuuid(accountuuid);
	       
                 
                  //loop via masks and check if source is a mask
                 for(Mask ma : maskList){
                	String maskuuid = ma.getUuid();
                	if(sourceuuid.equals(maskuuid)){
                	  maskp.setAccountUuid(accountuuid);
   	                  maskp.setCount(count); 
   	                  maskp.setPurchaseDate(new Date(new Long(Dates)));
   	                  maskp.setSourceUuid(maskuuid); 
   	                 smspurchaseDAO.put(maskp);
   	              
                	}
                 }
                 //loop via shortcodes and check if the source is a shortcode
                for(Shortcode code : shortcodeList)  {
                	String codeuuid = code.getUuid();
                	if(sourceuuid.equals(codeuuid)){
                	  shortcodep.setAccountUuid(accountuuid);
  		              shortcodep.setPurchaseDate(new Date(new Long(Dates)));
  		              shortcodep.setCount(count);
  		              shortcodep.setSourceUuid(codeuuid); 
  		                smspurchaseDAO.put(shortcodep);
  		              
                	}
                }
             //success message here
           session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Credit updated successfully.");
        	}
        	 //err message and redirect url
        		response.sendRedirect("admin/credit.jsp");
                session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Credit update failed.");	
         
         
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