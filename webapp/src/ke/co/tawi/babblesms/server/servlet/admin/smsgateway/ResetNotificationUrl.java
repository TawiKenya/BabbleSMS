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
package ke.co.tawi.babblesms.server.servlet.admin.smsgateway;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.smsgateway.TawiGateway;
import ke.co.tawi.babblesms.server.persistence.smsgw.tawi.GatewayDAO;

import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:mmwenda@tawi.mobi">Peter Mwenda</a>
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class ResetNotificationUrl extends HttpServlet  {
	/**
	 * 
	 */
	private static final long serialVersionUID = -4579860664481682965L;
	private GatewayDAO gatewayDAO;

	 public void init(ServletConfig config) throws ServletException {
	        super.init(config);
	        gatewayDAO = GatewayDAO.getInstance();
	 }
	 
	 /**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    	HttpSession session = request.getSession(false);
	    	String Url =  StringUtils.trimToEmpty(request.getParameter("url"))  ;
	    	String Username =  StringUtils.trimToEmpty(request.getParameter("username"));
	    	String AccountUuid = StringUtils.trimToEmpty(request.getParameter("accountuuid"));
	    	       
              System.out.println(Url);
              
            Account account = new Account();
	    	        account.setUuid(AccountUuid); 
	    	
	    	TawiGateway gateway = gatewayDAO.get(account);
	  	    		gateway.setUrl(Url);
	    	        gateway.setUsername(Username);
	    	        gateway.setAccountUuid(AccountUuid); 
	    	       
	   if(gatewayDAO.edit(gateway)){
            session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "TawiGateway updated successfully.");
        } else {
            session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "TawiGateway update failed."); 
                     
          }
	    	 response.sendRedirect("admin/accounts.jsp");

	    	 	 
	    	 
	 }//end dopost
	             
	    /**
	     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	     */
	    public void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        doPost(request, response);
	    }

}
