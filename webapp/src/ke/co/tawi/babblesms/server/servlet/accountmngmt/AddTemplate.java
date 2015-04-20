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

import ke.co.tawi.babblesms.server.beans.messagetemplate.MessageTemplate;
import ke.co.tawi.babblesms.server.persistence.items.messageTemplate.MessageTemplateDAO;
import ke.co.tawi.babblesms.server.session.SessionConstants;

/**
 * Receives a form request to edit a template's details
 * 
 * <p>
 * 
 * @author dennis <a href="mailto:dennism@tawi.mobi">Dennis Mutegi</a>
 *
 */

public class AddTemplate extends HttpServlet{
	
	private final String success = "Your message template has been successfully added";
	private final String failure ="failed to add your message template.Try again";
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
		
		String title = request.getParameter("title");
		String contents = request.getParameter("contents");
		String accountUuid = request.getParameter("accountuuid");
			
		if(title.equals("")){
			session.setAttribute(SessionConstants.ADD_ERROR, "please provide template title");
		}

		else{
			MessageTemplate template = new MessageTemplate();
			template.setTitle(title);
			template.setContents(contents);
			template.setAccountuuid(accountUuid);
			logger.info(template);
			MessageTemplateDAO templateDAO = MessageTemplateDAO.getInstance();
			if(templateDAO.putMessageTemplate(template)){
				session.setAttribute(SessionConstants.UPDATE_SUCCESS, success);
			}
			else{
				session.setAttribute(SessionConstants.ADD_ERROR, failure);
			}
			
		}
	
		response.sendRedirect("addmsgtemplate.jsp");
	}


}
