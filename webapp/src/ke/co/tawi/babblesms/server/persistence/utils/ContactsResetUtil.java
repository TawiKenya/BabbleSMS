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
package ke.co.tawi.babblesms.server.persistence.utils;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.log4j.Logger;

/**
 * Utility that updates the origin of the SMS to phone numbers that exist in 
 * an Account's address book.
 * <p>
 * 
 * @author <a href="mailto:eugene.g99@gmail.com">Eugene Wang'ombe</a>
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */

public class ContactsResetUtil extends GenericDAO {

	// Our Database credentials
	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWORD = "Hymfatsh8";
	final int DB_PORT = 5432;
	
	private ContactDAO contactDAO;
	private PhoneDAO phoneDAO;
	
	private Account account;
	private Logger logger;
	
	
	/**
	 * @param accountUuid
	 */
	public ContactsResetUtil(String accountUuid) {
		contactDAO = new ContactDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWORD, DB_PORT);
		phoneDAO = new PhoneDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWORD, DB_PORT);
		
		account = new Account();		
		account.setUuid(accountUuid);
		
		logger = Logger.getLogger(this.getClass());
	}

	

	/**
	 * 
	 */
	public void resetPhoneContacts() {
		RandomDataGenerator randomGenerator = new RandomDataGenerator();
		
		// get all contact uuids for this account
		List<String> uuids = new LinkedList<>();
		
		List<Contact> contactsList =  contactDAO.getContacts(account);
		
		for(Contact contact : contactsList) {
			uuids.add(contact.getUuid());
		}
		
		
		// Get all the phone objects of this account
		List<Phone> phoneList = new LinkedList<>();
		
		List<Phone> ctPhoneList;
		for (Contact contact : contactsList) {
			// get a list of all phone object associated with a contact
			ctPhoneList = phoneDAO.getPhones(contact);
			phoneList.addAll(ctPhoneList);
		}
		
		// get all phone numbers specifically
		List<String> phoneNumbers = new LinkedList<>();
		for (Phone phone : phoneList) {
			phoneNumbers.add(phone.getPhonenumber());
		}
		
		// generate new phone numbers and add to the list
		String phonenumber;
		for (int j = 0; j < 50; j++) {
			phonenumber = "254" + 
					Integer.toString(randomGenerator.nextInt(700000000,	729000000));
			phoneNumbers.add(phonenumber);
		}
					
		
		// Update the IncomingLog with the new phone numbers		
		try (
				Connection conn = dbCredentials.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("UPDATE incominglog SET origin=? "
						+ "WHERE recipientuuid=? AND uuid=?");) {
			
			int index;
			for (String uuid : uuids) {
				index = randomGenerator.nextInt(0, phoneNumbers.size() - 1);
				
				pstmt.setString(1, phoneNumbers.get(index));
				pstmt.setString(2, account.getUuid());
				pstmt.setString(3, uuid);
				pstmt.executeUpdate();
			}
			
		} catch (SQLException e) {
			logger.error("SQLException when updating IncomingLog phonenumbers");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// This is the Account UUID of 'demo'
		String accountUuid = "650195B6-9357-C147-C24E-7FBDAEEC74ED";
		
		System.out.println("Updating source phone numbers...");
		
		ContactsResetUtil util = new ContactsResetUtil(accountUuid);
		util.resetPhoneContacts();
		
		System.out.println("Done!");
	}
}
