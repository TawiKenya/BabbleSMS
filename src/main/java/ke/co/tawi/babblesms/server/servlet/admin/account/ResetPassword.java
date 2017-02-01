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
import ke.co.tawi.babblesms.server.utils.security.SecurityUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import org.apache.commons.lang3.StringUtils;
/**
 * Servlet used to ResetPassword.
 * <p>
 *
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 *  @author <a href="mailto:mwenda@tawi.mobi">Peter Mwenda</a>
 */
public class ResetPassword extends HttpServlet  {
   
	/**
	 * 
	 */
	private static final long serialVersionUID = -8904736830931073950L;
	private AccountDAO accountDAO;
    private Cache accountCache;
    private Account account;
    
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

       
        accountDAO = AccountDAO.getInstance();
        CacheManager mgr = CacheManager.getInstance();
        accountCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
    }
    
    
    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	HttpSession session = request.getSession(false);
    	
    	String loginPasswd =  StringUtils.trimToEmpty(request.getParameter("loginPasswd"));
    	String loginPasswd2 =  StringUtils.trimToEmpty(request.getParameter("loginPasswd2"));
    	String accountuuid = StringUtils.trimToEmpty(request.getParameter("accountuuid"));
    	
    	 account = accountDAO.getAccount(accountuuid);
        //  System.out.println(accountuuid); 
            
    	
    	if(StringUtils.isEmpty(loginPasswd) && StringUtils.isEmpty(loginPasswd2) ){
    		 session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Password can't be empty."); 
              response.sendRedirect("admin/accounts.jsp");
    	}else if(!loginPasswd.equalsIgnoreCase(loginPasswd2)){
             session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Password Mismatch."); 
              response.sendRedirect("admin/accounts.jsp");
        }else{
 
    	account.setLogpassword(SecurityUtil.getMD5Hash((loginPasswd)));  
        if(accountDAO.updateAccount(accountuuid, account)){
        	 accountCache.put(new Element(account.getUsername(), account));
            session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Account Password updated successfully.");
        } else {
            session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Account Password update failed."); 
                     
          }
        response.sendRedirect("admin/accounts.jsp");
           }
    }
    

   
    
    /**
     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
    
}
