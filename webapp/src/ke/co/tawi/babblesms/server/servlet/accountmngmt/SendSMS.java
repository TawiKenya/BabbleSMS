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
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.sendsms.tawismsgw.PostSMS;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountsDAO;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
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
	final String SMSGW_URL_HTTP = "http://192.168.0.50:8080/SMSGateway/sendsms";
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
		
		String  accountuuid = request.getParameter("account");
		String [] groupselected = request.getParameterValues("groupselected");
		String [] contactselected = request.getParameterValues("contactselected[]");
		String source = request.getParameter("source");
		String message = request.getParameter("message");
                String phones ="";
		Group group;
		AccountsDAO aDAO = AccountsDAO.getInstance();
		 Account account = new Account();
			account = aDAO.getAccount(accountuuid);
		//removing any blank input field value passed here
		List<String> grouplist = new ArrayList<String>();
                if(groupselected.length >0){
    		for(String s :groupselected ) {
      		  if(s != null && s.length() > 0) {
         	  grouplist.add(s);
      		  }
    		}
			//converting the list back to an array
   		 groupselected = grouplist.toArray(new String[grouplist.size()]);
		
		//logger.info("wwwwwwwwwwwwwwwwwww+++++++++++"+groupselected[0]);
		}
			List<String> newgroupList = new ArrayList<String>(new HashSet(grouplist));
			
			if(newgroupList != null){
			for (String group1 : newgroupList) {
			logger.info("yyyyyyyyyyyy+++++++++++"+group1);
				}}
			logger.info("my message is"+message+"sent by"+source+"to"+"whose phone is"+contactselected);
			if(contactselected!=null){
			for(int i=0;i<contactselected.length;i++){
			logger.info("xxxxxxxxxxxxxxxi+++++"+i+"++++++"+contactselected[i]);
			
		}}
			ContactGroupDAO cgDAO = ContactGroupDAO.getInstance();
			GroupDAO gDAO = GroupDAO.getInstance();
			


			PhoneDAO pDAO = PhoneDAO.getInstance();
			ContactDAO cDAO = ContactDAO.getInstance();
			Contact contact = new Contact();
			List<Phone> phonelist = new ArrayList<Phone>();
			List<Contact> contactList = new ArrayList<>();
			if(newgroupList != null){
			for (String groupname : newgroupList) {
		     	group = gDAO.getGroupByName(account ,groupname);
			contactList = cgDAO.getContacts(group); 
			for(Contact code:contactList){
			for(int i =0; i < pDAO.getPhones(code).size();i++){
			phonelist.add(pDAO.getPhones(code).get(i));
		}	
		}
		}
			List<Phone> newphoneList = new ArrayList<Phone>(new HashSet(phonelist));
			logger.info("my phonenumbers"+ phonelist);
			 for(Phone phone:newphoneList){
                         phones +=phone.getPhonenumber()+";"; 
                
			 
		} 
			 logger.info("my phones"+phones);
		}

			if(contactselected!=null){
			for(String contactuuid:contactselected){
			contact = cDAO.getContact(contactuuid);
			for(int i =0; i < pDAO.getPhones(contact).size();i++){
			phonelist.add(pDAO.getPhones(contact).get(i));
			}}
			logger.info("my phonenumbers"+ phonelist);
                       for(Phone phone:phonelist){
                         phones +=phone.getPhonenumber()+";"; 
                 }}
                       logger.info("my phones"+phones);
			Map<String,String> params = new HashMap<String,String>();
			 
			params.put("username", "tawi");		
			params.put("password", "tawi123");
			params.put("source", "2024");
			params.put("destination", phones);
			params.put("message", message);
			params.put("network", "safaricom_ke");
					
			
			PostSMS postThread;
					
			postThread = new PostSMS(SMSGW_URL_HTTP, params, false);	
			postThread.run(); 	// Use this when testing. However use 'postThread.start()' when
								// running in an application server.
			session.setAttribute(SessionConstants.SENT_SUCCESS, "success");
			response.sendRedirect("sendsms.jsp");	
		}
	
		
	}


