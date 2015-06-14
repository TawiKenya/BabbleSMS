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
package ke.co.tawi.babblesms.server.sendsms.tawismsgw;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.utils.ListPartitioner;
import ke.co.tawi.babblesms.server.utils.comparator.PhonesByNetworkPredicate;
import ke.co.tawi.babblesms.server.utils.StringUtil;

import static org.junit.Assert.*;

import java.util.LinkedList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.apache.commons.collections.CollectionUtils;

/**
 * Tests our class that pushes an SMS to the Tawi SMS Gateway.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestPostSMS {
	
	final String SMSGW_URL_HTTP = "http://192.168.0.50:8080/SMSGateway/sendsms";
	final String SMSGW_USERNAME = "tawi", SMSGW_PASSWORD = "tawi123";
	final String MESSAGE = 	"A test message.";
	final String SOURCE = 	"2024";
	
	final String ACCOUNT_UUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED"; // The 'demo' account
	
	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;
	
	/**
	 * Test method for {@link PostSMS#PostSMS(String, java.util.Map, boolean)}.
	 */
	@Test
	public void testPostThread() {
		
		// Get all the Contacts of the 'demo' account and make a list of Outgoing SMS
		ContactDAO contactDAO = new ContactDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Account a = new Account();
		a.setUuid(ACCOUNT_UUID);
		
		List<Contact> allContacts = contactDAO.getContacts(a);
		
		PhoneDAO phoneDAO = new PhoneDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		List<Phone> allPhones = new LinkedList<>(); 
		
		for(Contact c : allContacts) {
			allPhones.addAll(phoneDAO.getPhones(c));
		}
		
		System.out.println("All phone size: " + allPhones.size());		
		/* for(Phone p : allPhones) {
			System.out.println(p.getNetworkuuid());
		}*/
		
		List<Phone> safaricomPhones = new LinkedList<>();
		safaricomPhones.addAll(CollectionUtils.select(allPhones, 
				new PhonesByNetworkPredicate(Network.SAFARICOM_KE)));
		System.out.println("Safaricom phone size: " + safaricomPhones.size());
		
		
		List<List<Phone>> phonePartition = ListPartitioner.partition(safaricomPhones, 10);
		assertEquals(phonePartition.size(), 4);
		assertEquals(phonePartition.get(0).size(), 10);
		assertEquals(phonePartition.get(1).size(), 10);
		assertEquals(phonePartition.get(2).size(), 10);
		//assertEquals(phonePartition.get(3).size(), 9);
		
		
		Map<String,String> params;
		PostSMS postThread;
		
		for(List<Phone> list : phonePartition) {
			params = new HashMap<String,String>();
			params.put("username", SMSGW_USERNAME);		
			params.put("password", SMSGW_PASSWORD);
			params.put("source", SOURCE);
			params.put("destination", "254720123456");
			params.put("message", MESSAGE);
			params.put("network", "safaricom_ke");
					
			System.out.println("Hash to be POSTED: " + StringUtil.mapToString(params));
			
					
			//postThread = new PostSMS(SMSGW_URL_HTTP, params, false);	
			//postThread.run(); 	// Use this when testing. However use 'postThread.start()' when
								// running in an application server.
		}
		
		
		
		// Create a list of Outgoing SMS from the Phones 
		/*
		List<OutgoingLog> logList = new ArrayList<OutgoingLog>();
		
		OutgoingLog log;
		for(Phone p : allPhones) {
			log = new OutgoingLog();
			log.setOrigin(SOURCE);
			log.setDestination(p.getPhonenumber());
			log.setMessage(MESSAGE);
			log.setSender(ACCOUNT_UUID);
			log.setNetworkuuid(p.getNetworkuuid());
						
			logList.add(log);
		}
		*/		
	}

}

