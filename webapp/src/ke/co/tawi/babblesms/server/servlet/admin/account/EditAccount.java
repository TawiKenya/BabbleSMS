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
 * Servlet used to edit Accounts.
 * <p>
 *
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 *  @author <a href="mailto:mwenda@tawi.mobi">Peter Mwenda</a>
 */
public class EditAccount extends HttpServlet {

	
	
	

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
        String name = request.getParameter("name");
        String mobile = request.getParameter("mobile");
        String email = request.getParameter("email");
        String callback = request.getParameter("callback");
      

        Account account = accountDAO.getAccount(accountuuid);
        account.setUuid(accountuuid);
        account.setUsername(username);
        account.setName(name);
        account.setMobile(mobile);
        account.setEmail(email);
        account.setCallback(callback); 
        
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
