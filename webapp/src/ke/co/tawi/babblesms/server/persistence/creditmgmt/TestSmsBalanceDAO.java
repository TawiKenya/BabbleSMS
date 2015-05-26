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
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.creditmgmt.SMSBalance;
import ke.co.tawi.babblesms.server.beans.maskcode.SMSSource;

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
	
	final String ACC_UUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED",
			     ACC_UUID_NEW ="hfgfhyuyxrtuipiutyrec";
	final String SOURCE_UUID = "6C8275C2-8FE7-E3AD-6873-8384C41D395F",
			     SOURCE_UUID_NEW = "kjgfdfghjoiuydhjkuy";
	
	final int COUNT = 1000,
			  COUNT_NEW = 80000000;
	
	
	private SmsBalanceDAO storage;
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO#hasBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)}.
	 */
	@Test
	public void testHasBalance() {
		
		storage = new SmsBalanceDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		Account account = new Account();
		account.setUuid(ACC_UUID);
		//SMSSource smsSource = new SMSSource();
		//smsSource.setUuid(SOURCE_UUID);
		//assertTrue(storage.hasBalance(account, smsSource, COUNT));
		
		
		
		
		
	}

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO#deductBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)}.
	 */
	@Ignore
	@Test
	public void testDeductBalance() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO#addBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)}.
	 */
	@Ignore
	@Test
	public void testAddBalance() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO#getBalances(ke.co.tawi.babblesms.server.beans.account.Account)}.
	 */
	@Ignore
	@Test
	public void testGetBalances() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO#getAllBalances()}.
	 */
	@Ignore
	@Test
	public void testGetAllBalances() {
		fail("Not yet implemented");
	}

}
