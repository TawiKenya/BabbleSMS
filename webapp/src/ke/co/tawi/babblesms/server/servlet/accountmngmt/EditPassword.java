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
package ke.co.tawi.babblesms.server.servlet.accountmngmt;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;


import ke.co.tawi.babblesms.server.beans.account.Account;

import ke.co.tawi.babblesms.server.persistence.accounts.AccountsDAO;

import ke.co.tawi.babblesms.server.session.SessionConstants;

import java.io.IOException;
import org.apache.log4j.Logger;
/**
 * Receives a form request toedit a account's details
 * 
 * <p>
 * 
 * @author wambua <a href="mailto:wambua@tawi.mobi">Godfrey Wambua</a>
 *
 */

public class EditPassword extends HttpServlet{
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	/**
	 * @param config
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 * 
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

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
		
		String accuuid = request.getParameter("accuuid");
		
		
		String password = request.getParameter("password");
		String newpassword = request.getParameter("newpassword");
		String confirmpassword = request.getParameter("confirmpassword");
		
		
		

		
		  logger.info("yyyyyyyyyyyy+++++++++++"+newpassword);
			AccountsDAO acDAO = AccountsDAO.getInstance();
		Account accounts = acDAO.getAccount(accuuid);
		     accounts.setLogpassword(newpassword);
			logger.info("nnnnnnnnnnnnnnn+++++++++++"+accounts);
			logger.info("nnnnnnnnnnnnnnn+++++++++++"+accounts.getLogpassword());
		    if( acDAO.updateAccount(accounts)){
		session.setAttribute(SessionConstants.SENT_SUCCESS, "success");
		   }

			else{
			session.setAttribute(SessionConstants.ADD_ERROR, "Password  Editing Failed.");  

                   }
		logger.info("yyyyyyyyyyyy+++++++++++"+newpassword);
		
		
			response.sendRedirect("setting.jsp");	
		}
	
		
	}


