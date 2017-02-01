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
package ke.co.tawi.babblesms.server.persistence.contacts;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;


/**
 * Tests our persistence for {@link Contact}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestContactDAO {

	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;
	
	final String CONTACT_UUID = "3fc96ad6-3bd4-4eab-aca2-768132f70586", CONTACT_UUID2 = "tegAcEfWuAp3";
	
	final String CONTACT_NAME = "Beatrice Nyabira", CONTACT_NAME2 = "Michael Jackson";
	
	final String CONTACT_DESCR = "Someone important.", CONTACT_DESCR2 = "A musician";
	
	final String CONTACT_ACCOUNTUUID = "C937CE62-C4A9-131F-C96E-2DB8A9E886AB";
	
	final String CONTACT_STATUSUUID = "5A13538F-AC41-FDE2-4CD6-B939FA03123B";

	final String ACCOUNT_UUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED";
	final int ACCOUNT_CONTACTCOUNT = 100;
	
	private ContactDAO storage;
	
	private AccountDAO storagee;
	
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO#getContactByUuid(java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testGetContactByUuid() {
		storage = new ContactDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Contact c = storage.getContact(CONTACT_UUID);
        assertEquals(c.getUuid(), CONTACT_UUID);
        assertEquals(c.getName(), CONTACT_NAME);
        assertEquals(c.getDescription(), CONTACT_DESCR);
        assertEquals(c.getAccountUuid(), CONTACT_ACCOUNTUUID);
        assertEquals(c.getStatusUuid(), CONTACT_STATUSUUID);
	}


	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO#getContactByName(Account, java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testGetContactByName() {
		storage = new ContactDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Account a = new Account();
		a.setUuid(ACCOUNT_UUID);
		
		List<Contact> list = storage.getContactByName(a, "Ann");
		assertEquals(list.size(), 2);		
	}
	
	@Ignore
	@Test
	public void testGetmatchcontact(){
		storage = new ContactDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		List<Contact> list = new ArrayList<Contact>();
		
	    //list = storage.getContactListMatch("A");
	    
	    assertEquals(list.size(), 4);
	    assertEquals(list.get(0).getName() , "hello");

	}
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO#getContacts(ke.co.tawi.babblesms.server.beans.account.Account)}.
	 */
	//@Ignore
	@Test
	public void testGetContacts() {
		storage = new ContactDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Account a = new Account();
		a.setUuid(ACCOUNT_UUID);
		
		List<Contact> list = storage.getContacts(a);
		//assertEquals(list.size(), ACCOUNT_CONTACTCOUNT);
		for(int j=0; j<3; j++) {
			System.out.println(list.get(j));
		}
	}
	
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO#putContact(ke.co.tawi.babblesms.server.beans.contact.Contact)}.
	 */
	//@Ignore
	@Test
	public void testPutContact() {
		storage = new ContactDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Contact c = new Contact();
		c.setUuid(CONTACT_UUID2);
		c.setName(CONTACT_NAME2);
		c.setDescription(CONTACT_DESCR2);
		c.setAccountUuid(CONTACT_ACCOUNTUUID);
		c.setStatusUuid(CONTACT_STATUSUUID);
		
		assertTrue(storage.putContact(c));
		
		c = storage.getContact(CONTACT_UUID2);
        assertEquals(c.getUuid(), CONTACT_UUID2);
        assertEquals(c.getName(), CONTACT_NAME2);
        assertEquals(c.getDescription(), CONTACT_DESCR2);
        assertEquals(c.getAccountUuid(), CONTACT_ACCOUNTUUID);
        assertEquals(c.getStatusUuid(), CONTACT_STATUSUUID);
	}
	

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO#updateContact(java.lang.String, ke.co.tawi.babblesms.server.beans.contact.Contact)}.
	 */
	@Ignore
	@Test
	public void testUpdateContact() {
		storage = new ContactDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Contact c = new Contact();
		c.setUuid(CONTACT_UUID2);
		c.setName(CONTACT_NAME);
		c.setDescription(CONTACT_DESCR);
		c.setAccountUuid(CONTACT_ACCOUNTUUID);
		c.setStatusUuid(CONTACT_STATUSUUID);
		
		assertTrue(storage.updateContact(CONTACT_UUID2, c));
		
		c = storage.getContact(CONTACT_UUID2);
        assertEquals(c.getUuid(), CONTACT_UUID2);
        assertEquals(c.getName(), CONTACT_NAME);
        assertEquals(c.getDescription(), CONTACT_DESCR);
        assertEquals(c.getAccountUuid(), CONTACT_ACCOUNTUUID);
        assertEquals(c.getStatusUuid(), CONTACT_STATUSUUID);
	}
	
	
	/**
	 * 
	 */
	@Ignore
	@Test
	public void testGetContactList(){
		storage = new ContactDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		storagee = new AccountDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Account account = storagee.getAccount(ACCOUNT_UUID);
		
		List<Contact> contactList = storage.getContactList(account, 0, 15);
		
		assertEquals(contactList.size() , 15);
		
		
	}
}

