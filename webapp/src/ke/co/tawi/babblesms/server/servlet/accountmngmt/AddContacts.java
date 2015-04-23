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

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
//import ke.co.tawi.babblesms.server.beans.contact.ContactGroup;
import ke.co.tawi.babblesms.server.beans.contact.Email;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.EmailDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.session.SessionConstants;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;

/**
 * Receives form values from addcontact.jsp section and adds a new
 * {@link AddContacts} to the database.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */

public class AddContacts extends HttpServlet {
	private Logger logger=Logger.getLogger(this.getClass());
	private final EmailDAO emailDAO = EmailDAO.getInstance();
	private final PhoneDAO phoneDAO = PhoneDAO.getInstance();
	private final ContactDAO ctDAO = ContactDAO.getInstance();
	private final  GroupDAO gDAO = GroupDAO.getInstance();
	private final ContactGroupDAO cgDAO = ContactGroupDAO.getInstance();
	private final EmailValidator emailValidator = EmailValidator.getInstance();
	private final String ERROR_NO_NAME = "You have to input a value.";
	private final String ERROR_EMAIL_EXISTS = "The email provided already exists in the system.";
	private final String ADD_SUCCESS = "created successfully.";
	private final String ERROR_INVALID_EMAIL = "Please provide a valid email address.";
	private final String ERROR_DUPLICATE_EMAIL = "You have supplied duplicate email addresses.";
	private final String ERROR_PUBLICATE_PHONE = "You have supplied duplicate phone numbers.";
	Contact ct;
	Email mail;
	Phone phn;
	Account account = new Account();

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
		//String userPath = request.getServletPath();
		HttpSession session = request.getSession(false);

		// if add contacts is called
		// if (userPath.equals("/account/addcontact")) {

		String[] emailArray = request.getParameterValues("email1[]");
		String[] phonenumArray = request.getParameterValues("phonenum[]");
		String[] networkArray = request.getParameterValues("network[]");
		String contactname = request.getParameter("contname");
		String[] groupArray = request.getParameterValues("groupsadded[]");
		String description = request.getParameter("dept");
		String statusuuid = request.getParameter("statusuuid");
		String accountuuid = request.getParameter("accountuuid");
		Set<String> mySet = new HashSet<String>(Arrays.asList(emailArray));
		Set<String> mySet2 = new HashSet<String>(Arrays.asList(phonenumArray));
		Set<String> myGroupSet = new HashSet<String>(Arrays.asList(groupArray));
		int duplicateemail = emailArray.length - mySet.size();
		int duplicatephone = phonenumArray.length - mySet2.size();

		// No First Name provided
		if ((StringUtils.isBlank(contactname)) || (phonenumArray.length == 1) || (networkArray.length == 0)) {
			session.setAttribute(SessionConstants.ADD_ERROR, ERROR_NO_NAME);
		} 
		if(emailArray.length >1){
			logger.info("ddddddddd"+emailArray.length);
		if (!validemails(emailArray)) {
			session.setAttribute(SessionConstants.ADD_ERROR, ERROR_INVALID_EMAIL);
		}
		}
		else if (existsEmail(emailArray)) {
			session.setAttribute(SessionConstants.ADD_ERROR, ERROR_EMAIL_EXISTS);

		} else if (duplicateemail >= 1) {
			session.setAttribute(SessionConstants.ADD_ERROR, ERROR_DUPLICATE_EMAIL);

		} else if (duplicatephone >= 1) {
			session.setAttribute(SessionConstants.ADD_ERROR, ERROR_PUBLICATE_PHONE);
		} 
		else {

			ct = new Contact();
			ct.setName(contactname);
			ct.setDescription(description);
			ct.setStatusUuid(statusuuid);
			ct.setAccountUuid(accountuuid);

			if(ctDAO.putContact(ct)){
				session.setAttribute(SessionConstants.ADD_SUCCESS, ADD_SUCCESS);
			}
			else{
				session.setAttribute(SessionConstants.ADD_ERROR, "Contact  creation Failed.");  
			}
			//get contact bean to update cache
			String uuid = ct.getUuid();

			ct = ctDAO.getContact(uuid);

			for (String group1 : myGroupSet) {

				if(!(group1.equals(""))){
					
					//set the accounts uuid to the one posted by the form
					account.setUuid(accountuuid);
					Group group = gDAO.getGroupByName(account , group1);
					cgDAO.putContact(ct, group);

				}

			}

			//loop emails
			for (String email2 : emailArray) {
				mail = new Email();
				mail.setAddress(email2);
				mail.setContactuuid(ct.getUuid());
                mail.setStatusuuid(statusuuid);
				emailDAO.putEmail(mail);

			}
			//loop phonenumbers               
			int count = 0;
			for (String phonenum : phonenumArray) {
				phn = new Phone();
				phn.setPhonenumber(phonenum);
				phn.setContactsuuid(ct.getUuid());
				phn.setNetworkuuid(networkArray[count]);
				phn.setStatusuuid(statusuuid);

				phoneDAO.putPhone(phn);


				count++;
			}
		}
		response.sendRedirect("addcontact.jsp");   
	}
	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 * @throws IOException
	 */
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}

	/**
	 * Checks if the email supplied already exists in email database
	 *
	 * @param email
	 * @return
	 */
	private boolean existsEmail(final String[] emailArray) {
		boolean exists = false;

		for (String email : emailArray) {
			if (emailDAO.getEmail(email) != null) {
				exists = true;
			}
		}

		return exists;
	}

	/**
	 * Checks if the phone supplied already exists in phone database
	 *
	 * @param phone
	 * @return
	 */
	private boolean validemails(final String[] emailArray) {
		boolean valid = true;

		for (String email : emailArray) {
			if (!emailValidator.isValid(email)) {
				valid = false;
			}
		}

		return valid;
	}
}



















































































