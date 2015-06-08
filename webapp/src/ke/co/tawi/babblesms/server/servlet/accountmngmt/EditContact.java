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

import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.EmailDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.beans.contact.Email;
import ke.co.tawi.babblesms.server.beans.contact.Group;

import java.io.IOException;
import java.util.List;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

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
 * Receives a form request by an account holder to edit a contact. 
 * <p>
 *  
 * @author <a href="mailto:wambua@tawi.mobi">Godfrey Wambua</a>
 */
public class EditContact extends HttpServlet {

	final String ERROR_NO_NAME = "Some fields are empty please fill in all the fields.";
	
	private Cache accountCache;	

	
	/**
	 *
	 * @param config
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		CacheManager mgr = CacheManager.getInstance();
		accountCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
	}

	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException, IOException
	 */
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);

		String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
		if (StringUtils.isEmpty(username)) {
			response.sendRedirect("../index.jsp");
		}
		
		Account account = new Account();

		Element element;
		if ((element = accountCache.get(username)) != null) {
			account = (Account) element.getObjectValue();
		}
		//String[] emailArray = request.getParameterValues("email[]");
		String contactname = request.getParameter("name");
		String contactstatusuuid = request.getParameter("statusuuid");
		String description =request.getParameter("description");
		String [] groupArray =request.getParameterValues("groupselected[]");		
		
		System.out.println("JSP sent ::::"+groupArray.toString());	
		
		//String [] groupsArray =request.getParameterValues("groupsdeleted[]");
		String cuuid = request.getParameter("uuid");
		String [] phonenumArray = request.getParameterValues("phone1[]"); 
		String [] emailArray = request.getParameterValues("email[]");
		String[] networkArray = request.getParameterValues("network[]");
		Set<String> myGroupSet = new HashSet<String>(Arrays.asList(groupArray));
		//Set<String> groupSet = new HashSet<String>(Arrays.asList(groupsArray));
		if(contactname.equals("") || phonenumArray.equals("")){
			session.setAttribute(SessionConstants.ADD_ERROR, ERROR_NO_NAME);
		}

		else{
			//The update method  
			//Contact contact = new Contact();
			Contact contact = ContactDAO.getInstance().getContact(cuuid);
			contact.setName(contactname);
			contact.setDescription(description);	
			ContactDAO contactsDAO = ContactDAO.getInstance();
			contactsDAO.updateContact(cuuid, contact);
			Phone phone;
			PhoneDAO phoneDAO=PhoneDAO.getInstance();
			List<Phone> plist = phoneDAO.getPhones(contact);
			
			for(int i = 0; i < plist.size();i++){
			 phone = plist.get(i);
			
			String phonenum = phonenumArray[i];
			phone.setPhonenumber(phonenum);
			phone.setNetworkuuid(networkArray[i]);	
			phoneDAO.updatePhone(phone.getUuid(), phone);
			}
			
			if(phonenumArray.length > plist.size()){
			//int plistSize = plist.size();
			//int arrayLength = phonenumArray.length;
			for(int count = plist.size();count < phonenumArray.length;count ++){
			Phone newPhone =new Phone();
			String phonenums = phonenumArray[count];
			newPhone.setPhonenumber(phonenums);
			newPhone.setContactUuid(cuuid);
			newPhone .setStatusuuid(contactstatusuuid);
			newPhone.setNetworkuuid(networkArray[count]);
			phoneDAO.putPhone(newPhone);
                        }

			}
                       

			
			
			
			Email email;
			EmailDAO emailDAO=EmailDAO.getInstance();
			List<Email> elist = emailDAO.getEmails(contact);
			for(int j = 0; j < elist.size();j++){
			email = elist.get(j);
			
			String phonenums = emailArray[j];
			email.setAddress(phonenums);
			emailDAO.updateEmail(email.getUuid(), email);
			}
			if(emailArray.length > elist.size()){
			
			for(int count2 = elist.size();count2 < emailArray.length;count2 ++){
			Email newEmail =new Email();
			
			newEmail.setAddress(emailArray[count2]);
			newEmail.setContactuuid(cuuid);
                        newEmail.setStatusuuid(contactstatusuuid);
                        emailDAO.putEmail(newEmail);
                        }

			}
			
			
			for (String group1 : myGroupSet) {

			if(!(group1.equals(""))){
			ContactGroupDAO cgDAO = ContactGroupDAO.getInstance();
			GroupDAO gDAO = GroupDAO.getInstance();
			Group group = gDAO.getGroupByName(account , group1);
			cgDAO.putContact(contact, group);

				}

			}
			
			/*for (String group2 : groupSet) {

			if(!(group2.equals(""))){
			ContactGroupDAO cgDAO = ContactGroupDAO.getInstance();
			GroupDAO gDAO = GroupDAO.getInstance();
			Group groups = gDAO.getGroupByName(account , group2);
			cgDAO.removeContact(contact, groups);

				}

			}*/
			                
			
		}

		response.sendRedirect("contact.jsp");
	}
	
}
