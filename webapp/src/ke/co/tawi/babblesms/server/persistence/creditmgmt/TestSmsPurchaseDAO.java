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
package ke.co.tawi.babblesms.server.persistence.creditmgmt;

import static org.junit.Assert.*;

import java.util.Date;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.creditmgmt.SMSPurchase;
import org.junit.Ignore;
import org.junit.Test;

/**
 *@author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 *
 */
public class TestSmsPurchaseDAO extends SMSPurchase {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;
	
	
	final String ACCOUNT_UUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED";
	
	final String MASK_UUID ="D0F7EC32-EA25-7D32-8708-2CC132446A2E";				      
	final String SHORTCODE_UUID = "6C8275C2-8FE7-E3AD-6873-8384C41D395F";	
	
	final Date PURCHASE_DATE = new Date(new Long("1420070075009") ); 
	
	final int COUNT = 9000, COUNT2 = 66000;
		
	private SmsPurchaseDAO storage;
	
	
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsPurchaseDAO#put(ke.co.tawi.babblesms.server.beans.creditmgmt.SMSPurchase)}.
	 */
	
	@Test
	//@Ignore
	public void testPut() {
		
		storage = new SmsPurchaseDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		SMSPurchase purchase = new SMSPurchase();
		
		purchase.setAccountUuid(ACCOUNT_UUID);
		purchase.setSourceUuid(MASK_UUID);
		//purchase.setSourceUuid(SHORTCODE_UUID);
		purchase.setCount(COUNT);
		purchase.setPurchaseDate(PURCHASE_DATE);
	   
		assertTrue(storage.put(purchase));
		
		
		
		
		
		
	}
	
	
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsPurchaseDAO#getPurchases(ke.co.tawi.babblesms.server.beans.account.Account)}.
	 */
	@Test
	@Ignore
	public void testGetPurchases() {
		
		storage = new SmsPurchaseDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Account a = new Account();
		a.setUuid(ACCOUNT_UUID);
		
		List<SMSPurchase> list = storage.getPurchases(a);
		System.out.println(list);
		for(int j=0; j<30; j++) {
			System.out.println(list.get(j));
		}
	}

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsPurchaseDAO#getAllPurchases()}.
	 */
	@Test
	@Ignore
	public void testGetAllPurchases() {
		
		storage = new SmsPurchaseDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD,DB_PORT);

		List<SMSPurchase> list = storage.getAllPurchases();

		assertEquals(list.size(), 98);
		System.out.println(list);
		for (SMSPurchase sm : list) {
			System.out.println(sm);
		
		}
		}

}
