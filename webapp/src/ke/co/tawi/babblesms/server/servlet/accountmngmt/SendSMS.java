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

import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
import ke.co.tawi.babblesms.server.session.SessionConstants;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
/**
 * Receives a form request toedit a group's details
 * 
 * <p>
 * 
 * @author dennis <a href="mailto:dennism@tawi.mobi">Dennis Mutegi</a>
 *
 */

public class SendSMS extends HttpServlet{
	
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
		
		//String[] destination = request.getParameterValues("recipientcontact");
		String [] groupselected = request.getParameterValues("groupselected");
		String [] contactselected = request.getParameterValues("contactselected[]");
		String source = request.getParameter("source");
		String message = request.getParameter("message");
		

		List<String> grouplist = new ArrayList<String>();
                if(groupselected.length >0){
    		for(String s :groupselected ) {
      		  if(s != null && s.length() > 0) {
         	  grouplist.add(s);
      		  }
    		}

   		 groupselected = grouplist.toArray(new String[grouplist.size()]);
		logger.info("wwwwwwwwwwwwwwwwwww+++++++++++"+contactselected);
		}
			Set<String> groupSet = new HashSet<String>(Arrays.asList(groupselected));
			session.setAttribute(SessionConstants.SENT_SUCCESS, "success");
			if(groupSet != null){
			for (String group1 : groupSet) {
			logger.info("yyyyyyyyyyyy+++++++++++"+group1);
				}}
			logger.info("my message is"+message+"sent by"+source+"to"+"whose phone is"+contactselected);
			if(contactselected!=null){
			for(int i=0;i<contactselected.length;i++){
			logger.info("xxxxxxxxxxxxxxxi+++++"+i+"++++++"+contactselected[i]);
			
		}}

			response.sendRedirect("sendsms.jsp");	
		}
	
		
	}


