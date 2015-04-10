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

//import ke.co.tawi.babblesms.server.beans.contact.ContactGroup;
import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.session.SessionConstants;

import org.apache.log4j.Logger;

/**
 * Receives form values from addcontact.jsp section and adds a new
 * {@link AddContacts} to the database.
 * <p>
 *  
 * @author <a href="mailto:dennism@tawi.mobi">dennis mutegi</a>
 */

public class SendSMS extends HttpServlet {

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
		
		//session.setAttribute(SessionConstants.SENT_SUCCESS, "message sent to"+destination+"successfully");
		
		String destination = request.getParameter("destination");
		String source = request.getParameter("source");
		String message = request.getParameter("message");
		String[] contactselected = request.getParameterValues("contactselected");
		
		session.setAttribute(SessionConstants.SENT_SUCCESS, "message sent to"+""+destination+""+"successfully");
	
		logger.info("This message here"+message +"is sent to"+destination+"from"+source+"name"+"xxx"+contactselected[0]);
	
		response.sendRedirect("sendsms.jsp");
	}

}
