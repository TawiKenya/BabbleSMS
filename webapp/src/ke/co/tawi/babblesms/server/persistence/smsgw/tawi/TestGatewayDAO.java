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

import org.junit.Ignore;
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
    final String ACCOUNT_UUID_NEW = "78766B6-9357-C754-C24E-7FBDAEEHG6H";
   
    
    final String GATEWAY_URL = "http://localhost:8080/SMSGateway/sendsms",
    		     GATEWAY_URL_NEW = "http://localhost:8080/SMSGateway/sendsmsnew",
    		    GATEWAY_URL_UPDATE = "http://localhost:8080/SMSGateway/sendsmsupdate";
    
    final String  GATEWAY_USERNAME = "demo",GATEWAY_USERNAME_NEW = "newdemouser",
    		                            GATEWAY_USERNAME_UPDATE = "updatedemouser";
    
    final String  GATEWAY_PASSWD = "demo",GATEWAY_PASSWD_NEW = "newdemopass",
    		                        GATEWAY_PASSWD_UPDATE = "updatedemopass";
    
    
    private GatewayDAO storage;
    
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.smsgw.tawi.GatewayDAO#get(ke.co.tawi.babblesms.server.beans.account.Account)}.
	 */
    @Ignore
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
    /**
     *  Test method for {@link ke.co.tawi.babblesms.server.persistence.smsgw.tawi.GatewayDAO#get(ke.co.tawi.babblesms.server.beans.account.Account)}.
     */
    //@Ignore
	@Test
	public void testPut() {
		storage = new GatewayDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		TawiGateway gw = new TawiGateway();
		
		gw.setAccountUuid(ACCOUNT_UUID_NEW); 
		gw.setUrl(GATEWAY_URL_NEW);
		gw.setUsername(GATEWAY_USERNAME_NEW);
		gw.setPasswd(GATEWAY_PASSWD_NEW); 
		
		assertTrue(storage.put(gw));
		
        /*
		gw = storage.get(acc);
		assertEquals(gw.getAccountUuid(), ACCOUNT_UUID_NEW);
		assertEquals(gw.getUrl(), GATEWAY_URL_NEW);
		assertEquals(gw.getUsername(), GATEWAY_USERNAME_NEW);
		assertEquals(gw.getPasswd(), GATEWAY_PASSWD_NEW);*/
		
		
	}
    /**
     *  Test method for {@link ke.co.tawi.babblesms.server.persistence.smsgw.tawi.GatewayDAO#get(ke.co.tawi.babblesms.server.beans.account.Account)}.
     */
    @Ignore
	@Test
	public void testEdit() {
		storage = new GatewayDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		Account acc = new Account();
		acc.setUuid(ACCOUNT_UUID_NEW);
		
		TawiGateway gw = new TawiGateway();
		gw.setAccountUuid(ACCOUNT_UUID_NEW); 
		gw.setUrl(GATEWAY_URL_UPDATE);
		gw.setUsername(GATEWAY_USERNAME_UPDATE);
		gw.setPasswd(GATEWAY_PASSWD_UPDATE); 
		
		assertTrue(storage.edit(gw));
		
		/*gw = storage.get(acc);
		assertEquals(gw.getAccountUuid(), ACCOUNT_UUID_NEW);
		assertEquals(gw.getUrl(), GATEWAY_URL_UPDATE);
		assertEquals(gw.getUsername(), GATEWAY_USERNAME_UPDATE);
		assertEquals(gw.getPasswd(), GATEWAY_PASSWD_UPDATE);*/
	}
}
