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

import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.creditmgmt.MaskBalance;
import ke.co.tawi.babblesms.server.beans.creditmgmt.SMSBalance;
import ke.co.tawi.babblesms.server.beans.creditmgmt.ShortcodeBalance;
import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests our persistence implementation for shortcode and mask balances.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestSmsBalanceDAO {
	
	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;
	
	final String ACCOUNT_UUID_DEMO = "650195B6-9357-C147-C24E-7FBDAEEC74ED",
			     ACC_UUID_NEW ="hfgfhyuyxrtuipiutyrec";
			    
	final String SHORTCODE_UUID = "6C8275C2-8FE7-E3AD-6873-8384C41D395F",	// Short code 21146 on Safaricom
			     SOURCE_UUID_NEW = "kjgfdfghjoiuydhjkuy";			     
			   
	
	final String MASK_UUID ="D0F7EC32-EA25-7D32-8708-2CC132446A2E",	// 'tawi' mask on Safaricom
			     MASK_UUID_NEW ="kjhreguhutreghuytredfghty",
			     MASK_UUID_D="69243408-AAEF-B125-2AA9-FA6F49207C41";
	
	
	final int COUNT = 1000, COUNT2 = 100000;
		
	private SmsBalanceDAO storage;
	
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO#hasBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)}.
	 */
	@Ignore
	@Test
	public void testHasBalance() {
		
		storage = new SmsBalanceDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		Account account = new Account();
		account.setUuid(ACCOUNT_UUID_DEMO);
		
		Shortcode s = new Shortcode();
		s.setUuid(SHORTCODE_UUID);
		
		Mask m = new Mask();
		m.setUuid(MASK_UUID);
		
		//assertTrue(storage.hasBalance(account, s, COUNT));
		//assertFalse(storage.hasBalance(account, s, COUNT2));
		assertTrue(storage.hasBalance(account, m, 1));		
	}
	

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO#deductBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)}.
	 */
	@Ignore
	@Test
	public void testDeductBalance() {
		storage = new SmsBalanceDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Account acc = new Account();
		acc.setUuid(ACCOUNT_UUID_DEMO);
		

		Shortcode shortcode = new Shortcode();
		shortcode.setUuid(SHORTCODE_UUID);
		
		Mask mask = new Mask();
		mask.setUuid(MASK_UUID);
		
		
		assertTrue(storage.deductBalance(acc, shortcode, 10));
		assertTrue(storage.deductBalance(acc, mask, 10));		
	}
	

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO#deductBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)}.
	 */
	//@Ignore
	@Test
	public void testUpdateBalance() {
		storage = new SmsBalanceDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Account acc = new Account();
		acc.setUuid(ACCOUNT_UUID_DEMO);
		
		 ShortcodeBalance shortcodeb = new ShortcodeBalance(); 
		 shortcodeb.setUuid(SHORTCODE_UUID);
		 MaskBalance maskb = new MaskBalance();
		 maskb.setUuid(MASK_UUID);

		
		assertTrue(storage.updateBalance(acc, shortcodeb, 11111));  
		assertTrue(storage.updateBalance(acc, maskb, 22222));		
	}
	

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO#addBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)}.
	 */
	@Ignore
	@Test
	public void testAddBalance() {
		storage = new SmsBalanceDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		Account a = new Account();
		a.setUuid(ACCOUNT_UUID_DEMO);
		Mask mask = new Mask();
		mask.setUuid(MASK_UUID);
		Shortcode s = new Shortcode();
		s.setUuid(SHORTCODE_UUID);
		assertTrue(storage.addBalance(a, s, 10));
		assertTrue(storage.addBalance(a, mask, 10));		
	}
	

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO#getBalances(ke.co.tawi.babblesms.server.beans.account.Account)}.
	 */
	@Ignore
	@Test
	public void testGetBalances() {
		storage = new SmsBalanceDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Account account = new Account();
		account.setUuid(ACCOUNT_UUID_DEMO);
		
		List<SMSBalance> list = storage.getBalances(account);
		System.out.println(list);
		for(int j=0; j<2; j++) {
		System.out.println(list.get(j));
		}
	}
	

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO#getAllBalances()}.
	 */
	@Ignore
	@Test
	public void testGetAllBalances() {
	storage = new SmsBalanceDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
	List<SMSBalance> list = storage.getAllBalances();
	
	assertEquals(list.size(), 0);
	System.out.println(list);
	for (SMSBalance sm : list) {
		System.out.println(sm);
	
	}
	}

}
