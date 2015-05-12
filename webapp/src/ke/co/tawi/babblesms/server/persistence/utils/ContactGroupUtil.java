package ke.co.tawi.babblesms.server.persistence.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Utility that creates new associations among contacts, groups and accounts
 * <p>
 * 
 * @author <a href="mailto:eugene.g99@gmail.com">Eugene Wang'ombe</a>
 */

public class ContactGroupUtil extends GenericDAO {

	private final Logger logger = Logger.getLogger(this.getClass());

	// change the account uuid to you desire
	private static String accountUUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED";

	private static String dbName = "babblesmsdb";
	private static String dbHost = "localhost";
	private static String dbUsername = "babblesms";
	private static String dbPassword = "Hymfatsh8";
	private static int dbPort = 5432;

	private static ContactDAO contactDAO = new ContactDAO(dbName, dbHost,
			dbUsername, dbPassword, dbPort);
	private static GroupDAO groupDAO = new GroupDAO(dbName, dbHost, dbUsername,
			dbPassword, dbPort);

	private static List<Contact> contactList = new ArrayList<Contact>();
	private static List<Group> groupList = new ArrayList<Group>();

	private static ContactGroupUtil contactGroupUtil;

	public static ContactGroupUtil getInstance() {
		if (contactGroupUtil == null) {
			contactGroupUtil = new ContactGroupUtil();
		}
		return contactGroupUtil;
	}

	protected ContactGroupUtil() {
		super();
	}

	/**
	 * @param dbName
	 * @param dbHost
	 * @param dbUsername
	 * @param dbPassword
	 * @param dbPort
	 */
	public ContactGroupUtil(String dbName, String dbHost, String dbUsername,
			String dbPassword, int dbPort) {
		super(dbName, dbHost, dbUsername, dbPassword, dbPort);
	}

	public List<Group> getGroupsFromAccountUUID(String accounUUID) {

		Account account = new Account();
		account.setUuid(accountUUID);
		return groupDAO.getGroups(account);

	}

	public List<Contact> getContactsByAccountUUID(String accountUUID) {

		Account account = new Account();
		account.setUuid(accountUUID);
		return contactDAO.getContacts(account);

	}

	public void putContactsIntoGroups(String accountUUID) {
		// get all the accounts
		// get all contactgroup uuids for this account
		List<String> uuids = new ArrayList<>();

		try (Connection conn = dbCredentials.getConnection();
				PreparedStatement pstmt = conn
						.prepareStatement("SELECT uuid FROM contactgroup;");) {

			ResultSet rset = pstmt.executeQuery();
			while (rset.next()) {
				uuids.add(rset.getString("uuid"));
			}

		} catch (SQLException e) {
			logger.error("SQLException when getting uuids of contactcontactgroup.");
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		// insert new associations in contactgroup table
		try (Connection conn = dbCredentials.getConnection();
				PreparedStatement pstmt = conn
						.prepareStatement("INSERT INTO contactgroup (uuid, contactuuid, groupuuid, accountuuid) VALUES (?,?,?,?);");) {

			groupList = getGroupsFromAccountUUID(accountUUID);
			contactList = getContactsByAccountUUID(accountUUID);
			// distribute contacts into groups in the ratio 7:2:1
			int contactListSize = contactList.size();
			// first portion of the ratio
			int firstPortion = (int) Math.floor(contactListSize * 0.7);// 7/10
			for (int i = 0; i < firstPortion; i++) {

				pstmt.setString(1, UUID.randomUUID().toString());
				pstmt.setString(2, contactList.get(i).getUuid());
				pstmt.setString(3, groupList.get(3).getUuid());
				pstmt.setString(4, accountUUID);
				pstmt.executeUpdate();

			}
			// second portion of the ratio
			int secondPortion = firstPortion
					+ (int) Math.floor(contactListSize * 0.2);// 2/10
			for (int i = firstPortion; i < secondPortion; i++) {

				pstmt.setString(1, UUID.randomUUID().toString());
				pstmt.setString(2, contactList.get(i).getUuid());
				pstmt.setString(3, groupList.get(2).getUuid());
				pstmt.setString(4, accountUUID);
				pstmt.executeUpdate();

			}
			// third portion of the ratio
			for (int i = secondPortion; i < contactListSize; i++) {

				pstmt.setString(1, UUID.randomUUID().toString());
				pstmt.setString(2, contactList.get(i).getUuid());
				pstmt.setString(3, groupList.get(0).getUuid());
				pstmt.setString(4, accountUUID);
				pstmt.executeUpdate();

			}
			//put some contacts from one group into another group
			for (int i = firstPortion; i < secondPortion; i++) {

				pstmt.setString(1, UUID.randomUUID().toString());
				pstmt.setString(2, contactList.get(i).getUuid());
				pstmt.setString(3, groupList.get(0).getUuid());
				pstmt.setString(4, accountUUID);
				pstmt.executeUpdate();

				if (i == (secondPortion - 5))
					break;
			}
			//put from contacts from one group into another group
			for (int i = secondPortion + 5; i < contactListSize; i++) {

				pstmt.setString(1, UUID.randomUUID().toString());
				pstmt.setString(2, contactList.get(i).getUuid());
				pstmt.setString(3, groupList.get(2).getUuid());
				pstmt.setString(4, accountUUID);
				pstmt.executeUpdate();

				if (i == 20)
					break;

			}

		} catch (SQLException e) {
			logger.error("SQLException while inserting new association in contactgroup table");
			logger.error(ExceptionUtils.getStackTrace(e));
		}

	}

	public static void main(String[] args) {
		ContactGroupUtil cgu = new ContactGroupUtil(dbName, dbHost, dbUsername,
				dbPassword, dbPort);
		System.out.println("Creating new contact-group associations...");
		cgu.putContactsIntoGroups(accountUUID);
		System.out.println("Done!");

	}

}
