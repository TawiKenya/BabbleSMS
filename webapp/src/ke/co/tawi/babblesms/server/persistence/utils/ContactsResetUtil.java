package ke.co.tawi.babblesms.server.persistence.utils;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.log4j.Logger;

/**
 * Utility that updates the origin of the SMS to an existing phone number
 * <p>
 * 
 * @author <a href="mailto:eugene.g99@gmail.com">Eugene Wang'ombe</a>
 */

public class ContactsResetUtil extends GenericDAO {

	private List<Contact> contactsList = new ArrayList<Contact>();
	private List<Phone> phoneList = new LinkedList<Phone>();

	private final Logger logger = Logger.getLogger(this.getClass());

	private static String accountUUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED";// change
																				// the
																				// account
																				// uuid
																				// to
																				// your
																				// desire

	private static String dbName = "babblesmsdb";
	private static String dbHost = "localhost";
	private static String dbUsername = "babblesms";
	private static String dbPassword = "Hymfatsh8";
	private static int dbPort = 5432;

	private static ContactDAO contactDAO = new ContactDAO(dbName, dbHost,
			dbUsername, dbPassword, dbPort);
	private static PhoneDAO phoneDAO = new PhoneDAO(dbName, dbHost, dbUsername,
			dbPassword, dbPort);

	private static ContactsResetUtil resetUtil;

	public static ContactsResetUtil getInstance() {
		if (resetUtil == null) {
			resetUtil = new ContactsResetUtil();
		}

		return resetUtil;
	}

	protected ContactsResetUtil() {
		super();
	}

	/**
	 * @param dbName
	 * @param dbHost
	 * @param dbUsername
	 * @param dbPassword
	 * @param dbPort
	 */
	public ContactsResetUtil(String dbName, String dbHost, String dbUsername,
			String dbPassword, int dbPort) {
		super(dbName, dbHost, dbUsername, dbPassword, dbPort);
	}

	/**
	 * @param accountUUID
	 * @return list of all Contact objects related to an Account
	 */
	public List<Contact> getContactsByAccountUUID(String accountUUID) {

		Account account = new Account();
		account.setUuid(accountUUID);
		return contactDAO.getContacts(account);

	}

	/**
	 * @param contactUUID
	 * @return {@link List<>} a of Phone objects
	 * 
	 */
	public List<Phone> getPhoneNumbersByContactUUID(String contactUUID) {

		Contact contact = new Contact();
		contact.setUuid(contactUUID);
		return phoneDAO.getPhones(contact);

	}

	/**
	 * @param accountUUID
	 */
	public void resetPhoneContacts(String accountUuid) {

		RandomDataGenerator randomGenerator = new RandomDataGenerator();
		// get all message uuids for this account
		List<String> uuids = new ArrayList<>();
		try (Connection conn = dbCredentials.getConnection();
				PreparedStatement pstmt = conn
						.prepareStatement("SELECT uuid FROM incominglog WHERE recipientuuid=?;");) {
			pstmt.setString(1, accountUuid);
			ResultSet rset = pstmt.executeQuery();

			while (rset.next()) {
				uuids.add(rset.getString("uuid"));
			}

		} catch (SQLException e) {
			logger.error("SQLException when getting uuids of contact from selected account.");
			logger.error(ExceptionUtils.getStackTrace(e));
		}

		// get all the contacts associated with the following account UUID
		contactsList = getContactsByAccountUUID(accountUuid);
		try (Connection conn = dbCredentials.getConnection();
				PreparedStatement updateOriginQuery = conn
						.prepareStatement("UPDATE incominglog SET origin=? WHERE recipientuuid=? AND uuid=?");) {

			// the contact's phone list
			List<Phone> ctPhoneList;
			for (Contact singleContact : contactsList) {
				// get a list of all phone object associated with a contact
				ctPhoneList = getPhoneNumbersByContactUUID(singleContact
						.getUuid());
				phoneList.addAll(ctPhoneList);

			}
			// get all phonenumbers specifically
			List<String> phoneNumbers = new ArrayList<String>();
			for (Phone phone : phoneList)
				phoneNumbers.add(phone.getPhonenumber());

			// generate new phone numbers
			for (int i = 0; i < 50; i++) {
				String phonenumber = "254"
						+ Integer.toString(randomGenerator.nextInt(700000000,
								729000000));
				phoneNumbers.add(phonenumber);
			}

			// update the database

			for (String uuid : uuids) {
				int index = randomGenerator.nextInt(0, phoneNumbers.size() - 1);
				updateOriginQuery.setString(1, phoneNumbers.get(index));
				updateOriginQuery.setString(2, accountUuid);
				updateOriginQuery.setString(3, uuid);
				updateOriginQuery.executeUpdate();
			}
		} catch (SQLException e) {
			logger.error("SQLException when updating origin phonenumbers");
			logger.error(ExceptionUtils.getStackTrace(e));
		}

	}

	public static void main(String... args) {

		ContactsResetUtil cru = new ContactsResetUtil(dbName, dbHost,
				dbUsername, dbPassword, dbPort);
		System.out.println("Updating source phone numbers...");

		cru.resetPhoneContacts(accountUUID);

		System.out.println("Done!");
	}
}
