/**
 * 
 */
package ke.co.tawi.babblesms.server.servlet.admin.account;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @author peter
 *
 */
public class AccountStatus extends HttpServlet{

	
	private static final long serialVersionUID = -9062947248527284222L;
	private AccountDAO accountDAO;
    private CacheManager cacheManager;
	/**
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	public void init(ServletConfig config) throws ServletException {
	        super.init(config);
	        accountDAO = AccountDAO.getInstance();
	        cacheManager = CacheManager.getInstance();
	    }
	 
	 
	 /**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
		     HttpSession session = request.getSession(false); 
		     
		     String uuid = request.getParameter("account");
		     String status = request.getParameter("status");
		     System.out.println(uuid);
		     System.out.println(status);
		
		     Account account = new Account();
		     account.setUuid(uuid);
		     account.setStatusuuid(status); 
		    if(accountDAO.updateStatus(uuid, status)){
		    	updateAccountCache(account);
		    	 session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_SUCCESS_KEY, "Account deleted successifully");
		    	 response.sendRedirect("admin/changestatus.jsp");
		    }else{
		    	 session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, "Failed to delete account");
		    }
		   
	 }


	private void updateAccountCache(Account account) { 
cacheManager.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID).put(new Element(account.getUuid(), account));
		
	}
 
}
