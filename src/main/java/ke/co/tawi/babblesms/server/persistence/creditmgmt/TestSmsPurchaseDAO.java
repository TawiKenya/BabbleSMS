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
import ke.co.tawi.babblesms.server.beans.creditmgmt.ShortcodePurchase;
import ke.co.tawi.babblesms.server.beans.creditmgmt.MaskPurchase;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests the persistence implementation for shortcode and mask purchases.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
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

 	
 	
 	final String ACCOUNT_UUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED";//demo 
	        //uuid primarykeys
	final String SHORT_CODE_UUID = "53e7be0e-bfd8-419e-b7fc-00d2cd3bb1d0";//for demo
	final String MASKS_UUID ="3b2ad031-577a-4290-a739-f5f7e58aea74";//for demo
	
 	
	
	// source uuid
 	final String MASK_UUID ="D0F7EC32-EA25-7D32-8708-2CC132446A2E";	//'tawi' mask on Safaricom		      
 	final String SHORTCODE_UUID = "6C8275C2-8FE7-E3AD-6873-8384C41D395F";	//Short code 21146 on Safaricom
 	
 	final Date PURCHASE_DATE = new Date(new Long("1420070075009") ); 
	//source date
	final Date MASK_DATE = new Date(new Long("1417892442000") );
	final Date SHORTCODE_DATE = new Date(new Long("1419410347000") );
	
	//source count
	final int MASK_COUNT = 19000;
	final int SHORTCODE_COUNT = 18000;
 	
 	final int COUNT = 9000, COUNT2 = 66000;
	
 	private SmsPurchaseDAO storage;
	
	
	/**
		 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsPurchaseDAO#getPurchase(java.lang.String)}.
		 */
		@Test
		//@Ignore
		public void testgetPurchase(){
			storage = new SmsPurchaseDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
			//for shortcode
			ShortcodePurchase sp = (ShortcodePurchase)storage.getPurchase(SHORT_CODE_UUID);
			assertEquals(sp.getAccountUuid(),ACCOUNT_UUID);
			assertEquals(sp.getSourceUuid(),SHORTCODE_UUID);
			assertEquals(sp.getPurchaseDate(),SHORTCODE_DATE);
			assertEquals(sp.getCount(),SHORTCODE_COUNT);
			
			//for mask
			MaskPurchase mp = (MaskPurchase)storage.getPurchase(MASKS_UUID);
			assertEquals(mp.getAccountUuid(),ACCOUNT_UUID);
			assertEquals(mp.getSourceUuid(),MASK_UUID);
			assertEquals(mp.getPurchaseDate(),MASK_DATE);
			assertEquals(mp.getCount(),MASK_COUNT);
		}
		
	
	
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsPurchaseDAO#put(ke.co.tawi.babblesms.server.beans.creditmgmt.SMSPurchase)}.
	 */
	
	@Test
	@Ignore
	public void testPut() {
		
		storage = new SmsPurchaseDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		// Test against a Shortcode Purchase
		ShortcodePurchase sp = new ShortcodePurchase();
		String spUuid = sp.getUuid();
		
		sp.setAccountUuid(ACCOUNT_UUID);
		sp.setSourceUuid(SHORTCODE_UUID);
		sp.setPurchaseDate(PURCHASE_DATE);
		sp.setCount(COUNT);
	  
		assertTrue(storage.put(sp));
		
		sp = (ShortcodePurchase)storage.getPurchase(spUuid);
		assertEquals(sp.getUuid(), spUuid);
		assertEquals(sp.getAccountUuid(), ACCOUNT_UUID);
		assertEquals(sp.getSourceUuid(), SHORTCODE_UUID);
		assertEquals(sp.getPurchaseDate().getTime(), PURCHASE_DATE);
		assertEquals(sp.getCount(), COUNT);
		
		
		// Test against a Mask Purchase
		MaskPurchase mp = new MaskPurchase();
		spUuid = mp.getUuid();
		
		mp.setAccountUuid(ACCOUNT_UUID);
		mp.setSourceUuid(MASK_UUID);
	    mp.setCount(COUNT);
		mp.setPurchaseDate(PURCHASE_DATE);
		
		assertTrue(storage.put(mp));	
		
		mp = (MaskPurchase)storage.getPurchase(spUuid);
		assertEquals(mp.getUuid(), spUuid);
		assertEquals(mp.getAccountUuid(), ACCOUNT_UUID);
		assertEquals(mp.getSourceUuid(), MASK_UUID);
		assertEquals(mp.getPurchaseDate().getTime(), PURCHASE_DATE);
		assertEquals(mp.getCount(), COUNT);
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
