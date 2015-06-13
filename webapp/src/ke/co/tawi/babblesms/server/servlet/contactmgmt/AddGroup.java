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

package ke.co.tawi.babblesms.server.servlet.contactmgmt;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.beans.status.Status;
import ke.co.tawi.babblesms.server.cache.CacheVariables;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;

/**
 * Receives a form request to add a Group. 
 * <p>
 * 
 * @author <a href="mailto:dennism@tawi.mobi">Dennis Mutegi</a>
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */

public class AddGroup extends HttpServlet {
	
	private final String ERROR_NO_GROUP_NAME = "Please provide the group name";
	
	private Cache accountsCache;
	
	private GroupDAO groupDAO;
	
	
	/**
	 * @param config
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 * 
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		CacheManager mgr = CacheManager.getInstance();
        accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
        
        groupDAO = GroupDAO.getInstance(); 
	}
	
	
	/**
	 * method to handle form processing
	 * @param request
	 * @param response
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * 
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request , HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession(true);
		
		Account account = new Account();
		
		String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
		Element element;
	    if ((element = accountsCache.get(username)) != null) {
	        account = (Account) element.getObjectValue();
	    }
	    
		String name = request.getParameter("name");
		String desc = request.getParameter("desc");

	
		Group group = new Group();
		group.setName(name);
		group.setDescription(desc);
		group.setAccountsuuid(account.getUuid());
		group.setStatusuuid(Status.ACTIVE);
							
		
		if(StringUtils.isBlank(group.getName())) {
			session.setAttribute(SessionConstants.ADD_ERROR, "Please provide a Group Name.");
			response.sendRedirect("addgroup.jsp");
			
		} else if(groupDAO.putGroup(group)) {
			session.setAttribute(SessionConstants.UPDATE_SUCCESS, "Successfully saved.");
			response.sendRedirect("groups.jsp");
			
		} else {
			session.setAttribute(SessionConstants.ADD_ERROR, "Failed! Please try again");
			response.sendRedirect("addgroup.jsp");
		}
		
	}

}



