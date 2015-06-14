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
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Email;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.beans.status.Status;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.EmailDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.utils.StringUtil;

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
 * Receives form values from addcontact.jsp section and adds a new
 * {@link Contact} to the database.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class AddContact extends HttpServlet {
	
	
	final String ADD_SUCCESS = "Contact added successfully.";
	final String ERROR_INVALID_EMAIL = "Please provide a valid email address.";
	
	private Cache accountsCache;
	
	private EmailDAO emailDAO;
	private PhoneDAO phoneDAO;
	private ContactDAO contactDAO;
	private ContactGroupDAO cgDAO;
		
	
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
        
		emailDAO = EmailDAO.getInstance();
		phoneDAO = PhoneDAO.getInstance();
		contactDAO = ContactDAO.getInstance();		
		cgDAO = ContactGroupDAO.getInstance();
	}
	
	
	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
			
		
		Account account = new Account();
		
		String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
		Element element;
	    if ((element = accountsCache.get(username)) != null) {
	        account = (Account) element.getObjectValue();
	    }
	    
	    
	    String name = request.getParameter("name").trim();
		String[] phoneArray = request.getParameterValues("phones");		
		String[] networkArray = request.getParameterValues("networks");		
		String[] emailArray = request.getParameterValues("emails");
		for(String str : emailArray) {
			System.out.println("Email is: " + str);
		}
		
		String description = request.getParameter("description");
		String[] groupArray = request.getParameterValues("groups");
						
		if (StringUtils.isBlank(name) ) {
			session.setAttribute(SessionConstants.ADD_ERROR, "Please provide a contact name.");
			
		} else if(phoneArray.length < 1) {
				session.setAttribute(SessionConstants.ADD_ERROR, "Please provide at least one phone number.");
			
		} else if(networkArray.length < 1) {
			session.setAttribute(SessionConstants.ADD_ERROR, "Please select a network.");
			
		} else if(emailArray.length > 0 && StringUtils.isNotBlank(emailArray[0]) &&
				!StringUtil.validateEmails(emailArray)) {
			session.setAttribute(SessionConstants.ADD_ERROR, ERROR_INVALID_EMAIL);			
		
		} else {

			Contact contact = new Contact();
			contact.setName(name);
			contact.setDescription(description);
			contact.setStatusUuid(Status.ACTIVE);
			contact.setAccountUuid(account.getUuid());

			if(contactDAO.putContact(contact)) {
				session.setAttribute(SessionConstants.ADD_SUCCESS, ADD_SUCCESS);
				
			} else {
				session.setAttribute(SessionConstants.ADD_ERROR, "Contact add Failed.");  
			}
						

			// Save emails
			Email email;
			for (String email2 : emailArray) {
				email = new Email();
				email.setAddress(email2);
				email.setContactuuid(contact.getUuid());
                email.setStatusuuid(Status.ACTIVE);
				emailDAO.putEmail(email);

			}
			
			
			// Save phone numbers    
			Phone phone;
			int count = 0;
			for (String phonenum : phoneArray) {
				phone = new Phone();
				phone.setPhonenumber(phonenum);
				phone.setContactUuid(contact.getUuid());
				phone.setNetworkuuid(networkArray[count]);
				phone.setStatusuuid(Status.ACTIVE);

				phoneDAO.putPhone(phone);
			    
				count++;
			}
			
			
			// Associate the Contact with the Groups chosen
			Group group;
			if(groupArray != null) {
				for (String groupUuud : groupArray) {

					group = new Group();
					group.setUuid(groupUuud);
					cgDAO.putContact(contact, group);
				}
			}
			
		}
		
		response.sendRedirect("addcontact.jsp");   
	}
	
	
	private static final long serialVersionUID = 1L;
}

