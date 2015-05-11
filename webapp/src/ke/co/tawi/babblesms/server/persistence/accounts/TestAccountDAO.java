/**
 * 
 */
package ke.co.tawi.babblesms.server.persistence.accounts;

import static org.junit.Assert.*;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;

import org.junit.Ignore;
import org.junit.Test;

/**
 * @author peter
 *
 */
public class TestAccountDAO {

	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;

	final String ACC_UUID = "C3CFA249-F2A3-8253-1F44-B1C594C6A8D2",
			ACC_UUID_NEW = "f7uL81d2e6-84f2-45ff-914c-522a3b076141";

	final String ACC_USERNAME = "Lacota", ACC_USERNAME_NEW = "newusername",
			ACC_USERNAME_UPDATE = "updateusername";

	final String LOG_PASSWORD = "lac32@$", LOG_PASSWORD_NEW = "NEWpassword",
			LOG_PASSWORD_UPDATE = "UPDATEpassword";

	final String NAME = "Gloria", NAME_NEW = "kiaragwi",
			NAME_UPDATE = "UPDATEkiaragwi";
//0871 727 2000
	final String MOBILE = "0871 727 2000",
	             MOBILE_NEW = "254738382923",
			MOBILE_UPDATE = "254738382999";
//ukeria86@yahoo.com
	final String EMAIL = "ukeria86@yahoo.com",
			EMAIL_NEW = "SSASA@gmail.com",
			EMAIL_UPDATE = "UPDATE@gmail.com";

	private AccountDAO storage;

	/**
	 * Test method for
	 * {@link ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO#getAccount(java.lang.String)}
	 * .
	 */
	//@Ignore
	@Test
	public void testGetAccount() {
		storage = new AccountDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD,
				DB_PORT);
		
		Account a = new Account();
		a.setUuid(ACC_UUID);
		a.setUsername(ACC_USERNAME);
		a.setLogpassword(LOG_PASSWORD);
		a.setName(NAME);
		a.setMobile(MOBILE);
		a.setEmail(EMAIL);
		
		Account acc= storage.getAccount(ACC_UUID);
		
	    assertEquals(acc.getUuid(), ACC_UUID);
	    assertEquals(acc.getUsername(),ACC_USERNAME);
	    assertEquals(acc.getLogpassword(),LOG_PASSWORD);
      	assertEquals(acc.getName(),NAME);  
      	assertEquals(acc.getMobile(),MOBILE);
      	assertEquals(acc.getEmail(),EMAIL);
		
	}

	/**
	 * Test method for
	 * {@link ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO#getAccountByName(java.lang.String)}
	 * .
	 */
	// @Ignore
	@Test
	public void testGetAccountByName() {
		
	storage = new AccountDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD,DB_PORT);
		
		
		Account a = storage.getAccount(ACC_UUID);
		
		
		
			assertEquals(a.getUuid(),ACC_UUID);
	        assertEquals(a.getUsername(), ACC_USERNAME);
	        assertEquals(a.getLogpassword(), LOG_PASSWORD);
	        assertEquals(a.getName(), NAME);
	        assertEquals(a.getMobile(), MOBILE);
	        assertEquals(a.getEmail(), EMAIL);
	      
		
	}

	/**
	 * Test method for
	 * {@link ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO#getAllAccounts()}
	 * .
	 */
	//@Ignore
	@Test
	public void testGetAllAccounts() {

		storage = new AccountDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD,DB_PORT);

		List<Account> list = storage.getAllAccounts();

		//assertEquals(list.size(), 10);
		System.out.println(list);
		for (Account l : list) {
			System.out.println(l);
			// assertTrue(l.getUuid().equals(ACC_UUID));
		}
	}

	/**
	 * Test method for
	 * {@link ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO#putAccount(ke.co.tawi.babblesms.server.beans.account.Account)}
	 * .
	 */
	//@Ignore
	@Test
	public void testPutAccount() {

		storage = new AccountDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

		
		Account acc = new Account();
		
		acc.setUuid(ACC_UUID);
		acc.setUsername(ACC_USERNAME);
		acc.setLogpassword(LOG_PASSWORD);
		acc.setName(NAME);
		acc.setMobile(MOBILE);
		acc.setEmail(EMAIL);
		

		
		
		assertTrue(storage.updateAccount( ACC_UUID, acc));
		
		acc = storage.getAccount(ACC_UUID);
		assertEquals(acc.getUuid(), ACC_UUID);
		assertEquals(acc.getUsername(), ACC_USERNAME);
		assertEquals(acc.getLogpassword(),LOG_PASSWORD);
		assertEquals(acc.getName(),NAME);
		assertEquals(acc.getMobile(),MOBILE);
		assertEquals(acc.getEmail(),EMAIL);
		

	}

	/**
	 * Test method for
	 * {@link ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO#updateAccount(java.lang.String, ke.co.tawi.babblesms.server.beans.account.Account)}
	 * .
	 */
	//@Ignore
	@Test
	public void testUpdateAccount() {

		storage = new AccountDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD,
				DB_PORT);

		
		  Account acc = new Account();
		  
		 acc.setUuid(ACC_UUID); 
		 acc.setUsername(ACC_USERNAME);
		 acc.setLogpassword(LOG_PASSWORD);
		 acc.setName(NAME);
		 acc.setMobile(MOBILE);
		 acc.setEmail(EMAIL);
		  
		 
		assertTrue(storage.updateAccount( ACC_USERNAME, acc));
		acc = storage.getAccount(ACC_UUID);
		 
		 assertEquals(acc.getUuid(), ACC_UUID);
		 assertEquals(acc.getUsername(), ACC_USERNAME);
		 assertEquals(acc.getLogpassword(), LOG_PASSWORD);
		 assertEquals(acc.getName(), NAME); 
		 assertEquals(acc.getMobile(), MOBILE); 
		 assertEquals(acc.getEmail(), EMAIL);
		 

	}

}
