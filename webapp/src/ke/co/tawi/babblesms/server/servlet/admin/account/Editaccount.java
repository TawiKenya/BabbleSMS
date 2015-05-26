
package ke.co.tawi.babblesms.server.servlet.admin.account;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import java.io.IOException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * @author peter
 *
 */
public class Editaccount extends HttpServlet {

	
	
	

    /**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private AccountDAO accountDAO;
    private CacheManager cacheManager;
    
    
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

       
        accountDAO = AccountDAO.getInstance();

        cacheManager = CacheManager.getInstance();
    }
    
    
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	   HttpSession session = request.getSession(false);
    	
    	String accountuuid = request.getParameter("accountuuid");
        String username = request.getParameter("username");
        String password = request.getParameter("loginPasswd");
        String name = request.getParameter("name");
        String mobile = request.getParameter("mobile");
        String email = request.getParameter("email");
      

        Account account = new Account();
        account.setUuid(accountuuid);
        account.setUsername(username);
        account.setLogpassword(password);
        account.setName(name);
        account.setMobile(mobile);
        account.setEmail(email);
        
        updateAccountCache(account);
      
        if(accountDAO.updateAccount(accountuuid, account)){
            session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Account updated successfully.");
        } else {
            session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Account update failed."); 
          
            
  }
        

        response.sendRedirect("admin/accounts.jsp");

 
    	
    }
    
    
    private void updateAccountCache(Account acc) {
        cacheManager.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME).put(new Element(acc.getUsername(), acc));
        cacheManager.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID).put(new Element(acc.getUuid(), acc));
    }
    
   
    
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
    
    


}
