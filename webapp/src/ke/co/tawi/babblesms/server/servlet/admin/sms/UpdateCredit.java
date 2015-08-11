/**
 * 
 */
package ke.co.tawi.babblesms.server.servlet.admin.sms;

import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.creditmgmt.MaskBalance;
import ke.co.tawi.babblesms.server.beans.creditmgmt.ShortcodeBalance;
import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO;
import ke.co.tawi.babblesms.server.persistence.items.maskcode.MaskDAO;
import ke.co.tawi.babblesms.server.persistence.items.maskcode.ShortcodeDAO;
import ke.co.tawi.babblesms.server.servlet.util.PropertiesConfig;

import org.apache.commons.lang3.StringUtils;

/**
 * Servlet used to Update credit.
 * <p>
 * Copyright (c) Tawi Ltd.
 *
 * @author <a href="mwenda@tawi.mobi">Peter Mwenda</a>
 * @author <a href="michael@tawi.mobi">Michael Wahahe</a>
 */
public class UpdateCredit extends HttpServlet  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4184451798004522074L;
	final String ERROR_NO_ACCOUNTNAME = "No Account selected.";
    final String ERROR_NO_AMOUNT = "Please provide a value of client credits that are greater than zero.";
    final String ERROR_INVALID_AMOUNT = "Please provide a numeric value of client credits that are greater than zero.";
    final String ERROR_INVALID_SOURCE = "Please provide a source that belongs to the account.";
    final String ERROR_ADMIN_PASS_INVALID = "Password provided don't match with the one in the System";
    private String amount,sourceuuid,accountuuid,password;
    private int count;
	
	 private  SmsBalanceDAO balanceDAO;
	 private MaskDAO maskDAO;
	 private ShortcodeDAO scodeDAO;
	
	 
	 private HttpSession session;
	 
	 ShortcodeBalance shortcodebalance = new ShortcodeBalance(); 
	 MaskBalance maskbalance = new MaskBalance();
	 Account account = new Account();
	 
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
       balanceDAO = SmsBalanceDAO.getInstance();
       maskDAO = MaskDAO.getInstance();
       scodeDAO = ShortcodeDAO.getInstance();
    
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

    }else if (!StringUtils.equals(password, PropertiesConfig.getConfigValue("ADMIN_PASSWORD"))) {
        session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_ERROR_KEY,ERROR_ADMIN_PASS_INVALID);
        response.sendRedirect("admin/editcredit.jsp");
    }  else {
    	 // If we get this far then all parameter checks are ok.		
        session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_SUCCESS_KEY, "s");

        // Reduce our session data
        session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_PARAMETERS, null);
        session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_ERROR_KEY, null);

        
      
            EditCredit();
            session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Credit updated successfully."); 
       
    	}
    	 //err message and redirect url
    		response.sendRedirect("admin/editcredit.jsp");
           // session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Credit update failed.");	
     
       
       
   
        }

   private void setClassParameters(HttpServletRequest request) {
	    amount = StringUtils.trimToEmpty(request.getParameter("amount"));
	    sourceuuid = StringUtils.trimToEmpty(request.getParameter("source")); 
	    accountuuid = StringUtils.trimToEmpty(request.getParameter("user"));
	    password = StringUtils.trimToEmpty(request.getParameter("password"));
	    count = Integer.parseInt(amount); 
	    
	   /* System.out.println(sourceuuid);
	    System.out.println(accountuuid);
	    System.out.println(password);
	    System.out.println(count);*/
}


private void EditCredit() {
	//this lists holds mask details
	List<Mask> maskList = maskDAO.getmaskbyaccount(accountuuid);
	//this lists holds shortcode details
	List<Shortcode> shortcodeList = scodeDAO.getShortcodebyaccountuuid(accountuuid);
   
	
          //loop via masks and check if source is a mask
         for(Mask ma : maskList){
        	String maskuuid = ma.getUuid();
        	if(StringUtils.equals( sourceuuid, maskuuid)){     		
        		account.setUuid(accountuuid); 
        		maskbalance.setUuid(maskuuid);
        		
                if( balanceDAO.updateBalance(account, maskbalance, count)){
                	//session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Credit updated successfully.");
                }
              
              //System.out.println(maskuuid);
        	}
        	 
         }
         //loop via shortcodes and check if the source is a shortcode
        for(Shortcode code : shortcodeList)  {
        	String codeuuid = code.getUuid();
        	if(StringUtils.equals(sourceuuid, codeuuid)){
        		account.setUuid(accountuuid); 
        		shortcodebalance.setUuid(codeuuid); 
	               if(balanceDAO.updateBalance(account, shortcodebalance, count)){	            
                 session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Credit updated successfully.");
        	}
	               }
        	 //System.out.println(codeuuid);
        }
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
