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
package ke.co.tawi.babblesms.server.utils.randomgenerate;

import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.StorableBeanByUUID;
import ke.co.tawi.babblesms.server.beans.StorableBeanById;

import ke.co.tawi.babblesms.server.persistence.Storage;
import ke.co.tawi.babblesms.server.persistence.StorageImpl;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import java.util.Iterator;
import java.util.Set;
import java.util.HashSet;
import java.util.LinkedList;
import java.nio.charset.Charset;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.hibernate.query.Query;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.io.FileUtils;

/**
 * Generate random association between {@link Contact}s and {@link Group}s
 * <p>
 * The algorithm is as follows:
 * 
 * For all the accounts:
 * 		get all groups
 * 		get all contacts
 * 
 * For each contact of account:
 * 		assign to zero or more groups
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class ContactGroupGenerator {

	final static String OUT_FILE = "/tmp/Contact_Group.csv";
	
	private Storage storage;
	
	private SessionFactory sessionFactory;
	
	private RandomDataGenerator randomDataGenerator;
	
		
	
	/**
	 * 
	 */
	public ContactGroupGenerator() {
		try {
            // Create the SessionFactory from hibernate.cfg.xml            
			sessionFactory = new Configuration().configure("hibernate.cfg.xml").buildSessionFactory();
        	
			storage = new StorageImpl(sessionFactory);
			
        } catch (Throwable ex) {
            // Make sure you log the exception, as it might be swallowed
            System.err.println("Initial SessionFactory creation failed." + ex);
            throw new ExceptionInInitializerError(ex);
        }	
		
		randomDataGenerator = new RandomDataGenerator(); 
		
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Have started program");
		
		File outFile = new File(OUT_FILE);
		
		ContactGroupGenerator generator = new ContactGroupGenerator();
		
		List<Account> allAccounts = generator.getAllAccounts();
		List<Group> groups = new LinkedList<>();
		List<Contact> contacts = new LinkedList<>();
		
		Set<Group> randomGroups;
		
		for(Account a : allAccounts) {
			groups = generator.getGroups(a);
			contacts = generator.getContacts(a);
		
			for(Contact c : contacts) {
				randomGroups = generator.getRandomSet(groups);				
				
				Iterator<Group> groupIter = randomGroups.iterator();
				while(groupIter.hasNext()) {
					try {
						FileUtils.write(outFile, // file
								c.getId() + "|" + groupIter.next().getId() + "\n", // data 
								Charset.forName("UTF-8"),  // encoding
								true); // boolean append
						
					} catch (IOException e) {
						e.printStackTrace();
					} 
				}// end 'while(groupIter.hasNext())'
				
				
				
			}// end 'for(Contact c : contacts)'
			
		}// end 'for(Account a : allAccounts)'	
		
		System.out.println("Have finished program");
	}
	
	
	/**
	 * @return a list of all Accounts
	 */
	private List<Account> getAllAccounts() {
		List<StorableBeanByUUID> list = storage.getAll(Account.class);
		
		List<Account> accountList = new LinkedList<>();
		
		for(StorableBeanByUUID b : list) {
			accountList.add((Account) b);
		}
		
		return accountList;
	}
	
	
	/**
	 * @param account
	 * @return a list of {@link Group}s belonging to this account
	 */
	private List<Group> getGroups(Account account) {
		List<Group> groupList = new LinkedList<>();
		List<StorableBeanById> list;
		
		String hql = "from Group where accountUuid = :accountUuid";
		
		Session session = sessionFactory.openSession();
		
		Query query = session.createQuery(hql);
		query.setParameter("accountUuid", account.getUuid());
		list = query.list();						
				
		session.close();
		
		for(StorableBeanById b : list) {
			groupList.add((Group) b);
		}
		
		return groupList;
	}
	
	
	/**
	 * @param account
	 * @return a list of {@link Contact}s belonging to this account
	 */
	private List<Contact> getContacts(Account account) {
		List<Contact> contactList = new LinkedList<>();
		List<StorableBeanById> list;
		
		String hql = "from Contact where accountUuid = :accountUuid";
		
		Session session = sessionFactory.openSession();
		
		Query query = session.createQuery(hql);
		query.setParameter("accountUuid", account.getUuid());
		list = query.list();						
				
		session.close();
		
		for(StorableBeanById b : list) {
			contactList.add((Contact) b);
		}
		
		return contactList;
	}
	
	
	/**
	 * @param groups
	 * @return
	 */
	private Set<Group> getRandomSet(List<Group> groups) {
		List<Group> allGroups = new LinkedList<>(groups);
		Collections.shuffle(allGroups);
		
		Set<Group> randomSet = new HashSet<Group>(allGroups.
				subList(0, randomDataGenerator.nextInt(0, groups.size() - 1) ));
		
		return randomSet;
	}

}
