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
package ke.co.tawi.babblesms.server.persistence.smsgw.tawi;

import static org.junit.Assert.*;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.smsgateway.TawiGateway;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO;

import org.junit.Test;

/**
 * Tests our persistence implementation for {@link TawiGateway}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestGatewayDAO {

	final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;
    
    final String ACCOUNT_UUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED"; // 'demo' account uuid
    final String GATEWAY_URL = "http://localhost:8080/SMSGateway/sendsms",
    		GATEWAY_USERNAME = "demo", GATEWAY_PASSWD = "demo";
    
    private GatewayDAO storage;
    
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.smsgw.tawi.GatewayDAO#get(ke.co.tawi.babblesms.server.beans.account.Account)}.
	 */
	@Test
	public void testGet() {
		storage = new GatewayDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Account account = new Account();
		account.setUuid(ACCOUNT_UUID);
		
		TawiGateway gw = storage.get(account);
		
		assertEquals(gw.getAccountUuid(), ACCOUNT_UUID);
		assertEquals(gw.getUrl(), GATEWAY_URL);
		assertEquals(gw.getUsername(), GATEWAY_USERNAME);
		assertEquals(gw.getPasswd(), GATEWAY_PASSWD);
	}

}
