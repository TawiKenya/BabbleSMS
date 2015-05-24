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
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Utility that creates new associations among contacts, groups and accounts
 * <p>
 * 
 * @author <a href="mailto:eugene.g99@gmail.com">Eugene Wang'ombe</a>
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */

public class ContactGroupUtil extends GenericDAO {

	private final Logger logger;

	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWORD = "Hymfatsh8";
	final int DB_PORT = 5432;

	private ContactDAO contactDAO;
	private GroupDAO groupDAO;

	private Account account;

	
	/**
	 * @param accountUUID
	 */
	public ContactGroupUtil(String accountUUID) {
		contactDAO = new ContactDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWORD,
				DB_PORT);
		groupDAO = new GroupDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWORD, DB_PORT);

		account = new Account();
		account.setUuid(accountUUID);

		logger = Logger.getLogger(this.getClass());
	}

	
	/**
	 * Assigns groups to contacts
	 * 
	 */
	public void putContactsIntoGroups() {

		List<Contact> contactList = new LinkedList<>();
		List<Group> groupList = new LinkedList<>();
		
		// get all the accounts
		// get all contactgroup uuids for this account
		List<String> uuids = new LinkedList<>();

		try (Connection conn = dbCredentials.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT uuid FROM contactgroup;");) {

			ResultSet rset = pstmt.executeQuery();
			while (rset.next()) {
				uuids.add(rset.getString("uuid"));
			}

		} catch (SQLException e) {
			logger.error("SQLException when getting uuids of contactcontactgroup.");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		
		
		// insert new associations in contactgroup table
		try (
				Connection conn = dbCredentials.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("INSERT INTO contactgroup "
						+ "(uuid, contactuuid, groupuuid, accountuuid) VALUES (?,?,?,?);");) {

			groupList = groupDAO.getGroups(account);
			contactList = contactDAO.getContacts(account);
			
			
			// distribute contacts into groups in the ratio 7:2:1
			int contactListSize = contactList.size();
			
			// first portion of the ratio
			int firstPortion = (int) Math.floor(contactListSize * 0.7);// 7/10
			for (int i = 0; i < firstPortion; i++) {

				pstmt.setString(1, UUID.randomUUID().toString());
				pstmt.setString(2, contactList.get(i).getUuid());
				pstmt.setString(3, groupList.get(3).getUuid());
				pstmt.setString(4, account.getUuid());
				pstmt.executeUpdate();
			}
			
			
			// second portion of the ratio
			int secondPortion = firstPortion
					+ (int) Math.floor(contactListSize * 0.2);// 2/10
			for (int i = firstPortion; i < secondPortion; i++) {

				pstmt.setString(1, UUID.randomUUID().toString());
				pstmt.setString(2, contactList.get(i).getUuid());
				pstmt.setString(3, groupList.get(2).getUuid());
				pstmt.setString(4, account.getUuid());
				pstmt.executeUpdate();

			}
			
			
			// third portion of the ratio
			for (int i = secondPortion; i < contactListSize; i++) {

				pstmt.setString(1, UUID.randomUUID().toString());
				pstmt.setString(2, contactList.get(i).getUuid());
				pstmt.setString(3, groupList.get(0).getUuid());
				pstmt.setString(4, account.getUuid());
				pstmt.executeUpdate();

			}
			
			
			// put some contacts from one group into another group
			for (int i = firstPortion; i < secondPortion; i++) {

				pstmt.setString(1, UUID.randomUUID().toString());
				pstmt.setString(2, contactList.get(i).getUuid());
				pstmt.setString(3, groupList.get(0).getUuid());
				pstmt.setString(4, account.getUuid());
				pstmt.executeUpdate();

				if (i == (secondPortion - 5))
					break;
			}
			
			
			// put from contacts from one group into another group
			for (int i = secondPortion + 5; i < contactListSize; i++) {

				pstmt.setString(1, UUID.randomUUID().toString());
				pstmt.setString(2, contactList.get(i).getUuid());
				pstmt.setString(3, groupList.get(2).getUuid());
				pstmt.setString(4, account.getUuid());
				pstmt.executeUpdate();

				if (i == 20)
					break;
			}
			

		} catch (SQLException e) {
			logger.error("SQLException while inserting new association in contactgroup table");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
	}

	
	/**
	 * @param args
	 */
	public static void main(String[] args) {

		// This is the Account UUID of 'demo'
		String accountUUID = "50195B6-9357-C147-C24E-7FBDAEEC74ED";

		System.out.println("Creating new contact-group associations...");

		ContactGroupUtil util = new ContactGroupUtil(accountUUID);
		util.putContactsIntoGroups();

		System.out.println("Done!");
	}

}
