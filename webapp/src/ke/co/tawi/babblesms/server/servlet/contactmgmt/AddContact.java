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
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.log4j.Logger;

/**
 * Receives form values from addcontact.jsp section and adds a new
 * {@link AddContact} to the database.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */

public class AddContact extends HttpServlet {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Logger logger=Logger.getLogger(this.getClass());
	
	
	
	
	
	//private final String ERROR_NO_NAME = "You have to input a value.";
	private final String ERROR_EMAIL_EXISTS = "The email provided already exists in the system.";
	private final String ERROR_PHONE_EXISTS = "The phone provided already exists in the system.";
	private final String ADD_SUCCESS = "contcat added successfully.";
	private final String ERROR_INVALID_EMAIL = "Please provide a valid email address.";
	private final String ERROR_DUPLICATE_EMAIL = "You have supplied duplicate email addresses.";
	private final String ERROR_PUBLICATE_PHONE = "You have supplied duplicate phone numbers.";

	
	private  EmailDAO emailDAO = EmailDAO.getInstance();
	private  PhoneDAO phoneDAO = PhoneDAO.getInstance();
	private  ContactDAO contactDAO = ContactDAO.getInstance();
	private  GroupDAO gDAO = GroupDAO.getInstance();
	private  ContactGroupDAO cgDAO = ContactGroupDAO.getInstance();
	private EmailValidator emailValidator = EmailValidator.getInstance();
	
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
		
		HttpSession session = request.getSession(false);
		
		
		String[] emailArray = request.getParameterValues("email1[]");
		String[] phonenumArray = request.getParameterValues("phonenum[]");
		String[] networkArray = request.getParameterValues("network[]");
		String contactname = request.getParameter("contname").trim();
		
		String[] groupArray = request.getParameterValues("groupsadded[]");
		
		String description = request.getParameter("dept");
		String statusuuid = request.getParameter("statusuuid");
		String accountuuid = request.getParameter("accountuuid");
		
		logger.info(phonenumArray[0].equals(""));
		logger.info(emailArray[0].equals(""));
		logger.info(contactname);
		//System.out.println(phonenumArray[0]);
		
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
			
			if (!validemails(emailArray)) {
		session.setAttribute(SessionConstants.ADD_ERROR, ERROR_INVALID_EMAIL);
	}
			
		}
		else if (existsEmail(emailArray)) {
			session.setAttribute(SessionConstants.ADD_ERROR, ERROR_EMAIL_EXISTS);
		}
		else if(existPhone(phonenumArray)){
			session.setAttribute(SessionConstants.ADD_ERROR, ERROR_PHONE_EXISTS);
		}else if (duplicateemail >= 1) {
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

			if(contactDAO.putContact(ct)){
				session.setAttribute(SessionConstants.ADD_SUCCESS, ADD_SUCCESS);
			}
			else{
				session.setAttribute(SessionConstants.ADD_ERROR, "Contact add Failed.");  
			}
			//get contact bean to update cache
			String uuid = ct.getUuid();

			ct = contactDAO.getContact(uuid);

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
				phn.setContactUuid(ct.getUuid());
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
	


	private boolean existPhone(final String[] phonenumArray) {
			boolean exists = false; 
		for(String phone: phonenumArray){
		if(phoneDAO.getPhone1(phone) !=null){
		exists= true;
		}
		}
			return exists;
	}
    /**
	 * Checks if the email supplied already exists in email database
	 *
	 * @param email
	 * @return
	 */
	//emailArray
	private boolean existsEmail(final String[]emailArray) {
		boolean exists = false;

		for (String email : emailArray) {
			if (emailDAO.getEmails(email) != null) {
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
	
	
	
	
	
	
	
	@Override
	public void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doPost(request, response);

	}
}










