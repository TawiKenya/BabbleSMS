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
    @Override
	public void init(ServletConfig config) throws ServletException {
	        super.init(config);
	        accountDAO = AccountDAO.getInstance();
	        cacheManager = CacheManager.getInstance();
	    }
	 
	 
	 /**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	@Override
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
		      
		    /*if(accountDAO.updateStatus(uuid, status)){
		    	updateAccountCache(account);
		    	 session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_SUCCESS_KEY, "Account deleted successifully");
		    	 response.sendRedirect("admin/changestatus.jsp");
		    }else{
		    	 session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, "Failed to delete account");
		    }*/
		   
	 }


	private void updateAccountCache(Account account) { 
cacheManager.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID).put(new Element(account.getUuid(), account));
		
	}
 
}
