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
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.utils.StringUtil;

import java.io.IOException;
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
import org.apache.log4j.Logger;


/**
 * Receives form values from addcontact.jsp section and adds a new
 * {@link AddContact} to the database.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class AddContact extends HttpServlet {
	
	
	//private final String ERROR_NO_NAME = "You have to input a value.";
	final String ERROR_PHONE_EXISTS = "The phone provided already exists in the system.";
	final String ADD_SUCCESS = "contcat added successfully.";
	final String ERROR_INVALID_EMAIL = "Please provide a valid email address.";
	final String ERROR_DUPLICATE_EMAIL = "You have supplied duplicate email addresses.";
	final String ERROR_PUBLICATE_PHONE = "You have supplied duplicate phone numbers.";

	private Cache accountsCache;
	
	private EmailDAO emailDAO;
	private PhoneDAO phoneDAO;
	private ContactDAO contactDAO;
	private GroupDAO groupDAO;
	private ContactGroupDAO cgDAO;
		
	private Logger logger;

	
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
		groupDAO = GroupDAO.getInstance();
		cgDAO = ContactGroupDAO.getInstance();
		
		
		logger = Logger.getLogger(this.getClass());
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
		
		Contact contact;
		Email email;
		Phone phone;
		
		Account account = new Account();
		
		String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
		Element element;
	    if ((element = accountsCache.get(username)) != null) {
	        account = (Account) element.getObjectValue();
	    }
	    
		String[] emailArray = request.getParameterValues("email1[]");
		String[] phonenumArray = request.getParameterValues("phonenum[]");
		String[] networkArray = request.getParameterValues("network[]");
		String contactname = request.getParameter("contname").trim();
		
		String[] groupArray = request.getParameterValues("groupsadded[]");
		
		String description = request.getParameter("dept");
		
			
		
		Set<String> mySet = new HashSet<String>(Arrays.asList(emailArray));
		Set<String> mySet2 = new HashSet<String>(Arrays.asList(phonenumArray));
		Set<String> myGroupSet = new HashSet<String>(Arrays.asList(groupArray));
		
		int duplicateemail = emailArray.length - mySet.size();
		int duplicatephone = phonenumArray.length - mySet2.size();
		
	
		
		// No First Name provided
		if (StringUtils.isBlank(contactname) )
		{
			session.setAttribute(SessionConstants.ADD_ERROR, "contact name cant be empty");
		} 
		else if(phonenumArray[0].equals("")){
				session.setAttribute(SessionConstants.ADD_ERROR, "phone number cant be empty");
			
		}else if(networkArray[0].equals("")){
			session.setAttribute(SessionConstants.ADD_ERROR, "u must select a network");
		}
		else if(emailArray[0].equals("")){
				session.setAttribute(SessionConstants.ADD_ERROR,"email field cant be empty");
				
		   logger.info("d"+emailArray.length);
			
			if (!StringUtil.validateEmails(emailArray)) {
		session.setAttribute(SessionConstants.ADD_ERROR, ERROR_INVALID_EMAIL);
	}
			
		} else if(existPhone(phonenumArray)){
			session.setAttribute(SessionConstants.ADD_ERROR, ERROR_PHONE_EXISTS);
		}else if (duplicateemail >= 1) {
			session.setAttribute(SessionConstants.ADD_ERROR, ERROR_DUPLICATE_EMAIL);

		} else if (duplicatephone >= 1) {
			session.setAttribute(SessionConstants.ADD_ERROR, ERROR_PUBLICATE_PHONE);
		} 
		else {

			contact = new Contact();
			contact.setName(contactname);
			contact.setDescription(description);
			contact.setStatusUuid(Status.ACTIVE);
			contact.setAccountUuid(account.getUuid());

			if(contactDAO.putContact(contact)){
				session.setAttribute(SessionConstants.ADD_SUCCESS, ADD_SUCCESS);
			}
			else{
				session.setAttribute(SessionConstants.ADD_ERROR, "Contact add Failed.");  
			}
			//get contact bean to update cache
			String uuid = contact.getUuid();

			contact = contactDAO.getContact(uuid);

			for (String group1 : myGroupSet) {

				if(!(group1.equals(""))){
					
					//set the accounts uuid to the one posted by the form
					account.setUuid(account.getUuid());
					Group group = groupDAO.getGroupByName(account , group1);
					cgDAO.putContact(contact, group);

				}

			}

			//loop emails
			for (String email2 : emailArray) {
				email = new Email();
				email.setAddress(email2);
				email.setContactuuid(contact.getUuid());
                email.setStatusuuid(Status.ACTIVE);
				emailDAO.putEmail(email);

			}
			//loop phonenumbers               
			int count = 0;
			for (String phonenum : phonenumArray) {
				phone = new Phone();
				phone.setPhonenumber(phonenum);
				phone.setContactUuid(contact.getUuid());
				phone.setNetworkuuid(networkArray[count]);
				phone.setStatusuuid(Status.ACTIVE);

				phoneDAO.putPhone(phone);


				count++;
			}
		}
		response.sendRedirect("addcontact.jsp");   
	}
	
	

	private boolean existPhone(final String[] phonenumArray) {
			boolean exists = false; 
		for(String phone: phonenumArray){
		if(phoneDAO.getPhone1(phone) !=null){
		exists= true;
		}
		}
			return exists;
	}
	
		
	
	private static final long serialVersionUID = 1L;
}

