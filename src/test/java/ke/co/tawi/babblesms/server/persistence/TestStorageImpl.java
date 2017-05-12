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
package ke.co.tawi.babblesms.server.persistence;

import ke.co.tawi.babblesms.server.beans.StorableBeanByUUID;
import ke.co.tawi.babblesms.server.beans.account.Status;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.beans.contact.Email;

import java.util.List;
import java.util.Iterator;

import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

import static org.junit.Assert.*;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.Ignore;


/**
 * Test our {@link Storage}
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class TestStorageImpl {
			
	private Storage storage;
	
	private SessionFactory sessionFactory;
	
	
	/**
	 * @throws java.lang.Exception
	 */
	@Before
	public void setUp() throws Exception {
		try {
            // Create the SessionFactory from hibernate.cfg.xml            
			sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        	
			storage = new StorageImpl(sessionFactory);
			
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }
	}

	
	/**
	 * @throws java.lang.Exception
	 */
	@After
	public void tearDown() throws Exception {
		sessionFactory.close();
	}
	
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.Storage#get(Class, String)}.
	 */	
	@Ignore
	@Test
	public void testGetByUUID() {
			
		//*****************
		// Test Status
		//*****************
		String uuid = "396F2C7F-961C-5C12-3ABF-867E7FD029E6"; // 'Active' status
		
		Status status = (Status)storage.get(Status.class, uuid);
		
		assertEquals(status.getDescription(), "Active");
		
		
		//*****************
		// Test Account
		//*****************
		uuid = "C3CFA249-F2A3-8253-1F44-B1C594C6A8D2";  // The 'Tawi' account
		
		Account account = (Account)storage.get(Account.class, uuid);
		
		assertEquals(account.getName(), "Tawi");
		assertEquals(account.getStatus().getDescription(), "Active");
		assertEquals(account.getEmail(), "admin@tawi.mobi");
		
		//System.out.println("Account is: " + account);		
	}

	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.Storage#save(mobi.tawi.smsgw2.beans.StorableBean)}.
	 */
	@Ignore
	@Test
	public void testSave() {
		//***********************************
		// Test updating an existing Account
		//***********************************
		String uuid = "C3CFA249-F2A3-8253-1F44-B1C594C6A8D2"; // The 'Tawi' account
		String newEmail = "this@company.com";
		
		Account account = (Account)storage.get(Account.class, uuid);	
		account.setEmail(newEmail);
		
		storage.save(account);
		
		Account account2 = (Account)storage.get(Account.class, uuid);
		assertEquals(account2.getEmail(), newEmail);		
	}
	
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.Storage#get(Class, String)}.
	 */
	//@Ignore
	@Test
	public void testGetById() {	
		//*****************
		// Test Contact
		//*****************	
		long id = 3l;
		
		Contact contact = (Contact)storage.get(Contact.class, id);
		
		assertEquals(contact.getName(), "Susan Omeara");
		assertTrue(contact.getAccount().getUuid().equals("650195B6-9357-C147-C24E-7FBDAEEC74ED"));
		
		
		assertEquals(contact.getEmails().size(), 3);
		assertEquals(contact.getPhones().size(), 3);
		
		Iterator<Phone> phoneIter = contact.getPhones().iterator();
		while(phoneIter.hasNext()) {
			//System.out.println(phoneIter.next());
		}
		
		
		//*****************
		// Test Phone
		//*****************		
		id = 4l;  

		Phone phone = (Phone)storage.get(Phone.class, id);
		
		assertEquals(phone.getUuid(), "af235d97-8ae8-4bd6-87dd-c5b5247fec82");
		assertEquals(phone.getPhonenumber(), "254724300863");
		
		System.out.println("Phone is: "  + phone);
		
	}
	
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.Storage#get(Class, String)}.
	 */
	@Ignore
	@Test
	public void testGetAll() {
			
		List<StorableBeanByUUID> list = storage.getAll(Status.class);
		assertEquals(list.size(), 5);
		
		//System.out.println("1st Status is " + list.get(0));
	}
	
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.Storage#get(Class, Object, Object)}.
	 */
	@Ignore
	@Test
	public void testGetList() {
		
		
	}
	
}
