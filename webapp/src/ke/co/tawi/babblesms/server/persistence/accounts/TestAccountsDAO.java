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
package ke.co.tawi.babblesms.server.persistence.accounts;

import java.util.Date;
import static org.junit.Assert.*;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;

import org.junit.Test;

/**
 * Tests our {@link Account} persistence.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestAccountsDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String ACC_UUID = "C3CFA249-F2A3-8253-1F44-B1C594C6A8D2", ACC_UUID_NEW = "f7uL81d2e6-84f2-45ff-914c-522a3b076141";
    final String ACC_USERNAME = "Lacota", ACC_USERNAME_NEW = "newusername" ,ACC_USERNAME_UPDATE = "updateusername";

    final int ACC_SMSLIMIT = 2,
            ACC_SMSLIMIT_NEW = 1000,
            ACC_SMSLIMIT_UPDATE = 200;

    final String LOG_PASSWORD = "lac32@$",
            LOG_PASSWORD_NEW = "NEWpassword",
            LOG_PASSWORD_UPDATE = "UPDATEpassword";
            

    final String API_USERNAME = "admin@tawi.mobi",
            API_USERNAME_NEW = "NEWapiuser";
            
    
    final String API_PASSWORD = "Tawi1@",
            API_PASSWORD_NEW = "newpasswd";
            
    
    final String NAME = "Gloria",
            NAME_NEW = "kiaragwi",
            NAME_UPDATE = "UPDATEkiaragwi";
    
    final String MOBILE = "0871 727 2000",
            MOBILE_NEW = "254738382923",
            MOBILE_UPDATE = "254738382999";
    
    
    final String EMAIL = "cursus@Vestibulumaccumsanneque.ca",
            EMAIL_NEW = "SSASA@gmail.com",
            EMAIL_UPDATE = "UPDATE@gmail.com";
    
    final String STATUS = "396F2C7F-961C-5C12-3ABF-867E7FD029E6",
            STATUS_NEW = "396F2C7F-961C-5C12-3ABF-867E7FD029E6";
    
    final Date CREATION_DATE = new Date(new Long("1413536188000"));  // 2014-10-05 18:33:21 (yyyy-MM-dd HH:mm:ss)

    private AccountsDAO storage;

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.account#getAccountByuuid(java.lang.String)}.
     */
    @Test
    public void testAccountByuuidString() {
        storage = new AccountsDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Account acc = storage.getAccount(ACC_UUID);
        assertEquals(acc.getUuid(), ACC_UUID);
        assertEquals(acc.getUsername(), ACC_USERNAME);
        assertEquals(acc.getLogpassword(), LOG_PASSWORD);
        assertEquals(acc.getName(), NAME);
        assertEquals(acc.getMobile(), MOBILE);
        assertEquals(acc.getEmail(), EMAIL);
        assertEquals(acc.getDailysmslimit(), ACC_SMSLIMIT);
        assertEquals(acc.getStatusuuid(), STATUS);
        /*assertEquals(acc.getCreationdate(), CREATION_DATE);
        
        String one="Fri Oct 17 11:56:28 EAT 2014";
        String two="Fri Oct 17 11:56:28 EAT 2014";
        
        if(one.equals(two)){
            System.out.println("same");
        }
        */

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.account#GetAllAccounts}.
     */
    //@Ignore
    @Test
    public void testGetAllAccounts() {
        storage = new AccountsDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        List<Account> list = storage.getAllAccounts();

        //assertEquals(list.size(), 10);
        System.out.println(list);
        for (Account l : list) {
            System.out.println(l);
            //assertTrue(l.getAccountuuid().equals(SENDERUUID));
        }
    }

   
   /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.logs.accounts#putAccount(ke.co.tawi.babblesms.server.beans.account)}.
     */
    @Test
    public void testPutAccont() {
        storage = new AccountsDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Account acc = new Account();
        acc.setUuid(ACC_UUID_NEW);
        acc.setUsername(ACC_USERNAME_NEW);
        acc.setLogpassword(LOG_PASSWORD_NEW);
        acc.setName(NAME_NEW);
        acc.setMobile(MOBILE_NEW);
        acc.setEmail(EMAIL_NEW);
        acc.setStatusuuid(STATUS_NEW);

        assertTrue(storage.putAccount(acc));

        acc = storage.getAccount(ACC_UUID_NEW);
        assertEquals(acc.getUuid(), ACC_UUID_NEW);
        assertEquals(acc.getUsername(), ACC_USERNAME_NEW);
        assertEquals(acc.getLogpassword(), LOG_PASSWORD_NEW);
        assertEquals(acc.getName(), NAME_NEW);
        assertEquals(acc.getMobile(), MOBILE_NEW);
        assertEquals(acc.getEmail(), EMAIL_NEW);
        assertEquals(acc.getStatusuuid(), STATUS_NEW);
    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.logs.accounts#UpdateAccount(ke.co.tawi.babblesms.server.beans.account)}.
     */
    @Test
    public void testUpdateAccont() {
        storage = new AccountsDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Account acc = new Account();
        acc.setUuid(ACC_UUID_NEW);
        acc.setUsername(ACC_USERNAME_UPDATE);
        acc.setLogpassword(LOG_PASSWORD_UPDATE);
        acc.setName(NAME_UPDATE);
        acc.setMobile(MOBILE_UPDATE);
        acc.setEmail(EMAIL_UPDATE);
        acc.setStatusuuid(STATUS_NEW);

        assertTrue(storage.updateAccount(acc));

        acc = storage.getAccount(ACC_UUID_NEW);
        assertEquals(acc.getUuid(), ACC_UUID_NEW);
        assertEquals(acc.getUsername(), ACC_USERNAME_UPDATE);
        assertEquals(acc.getLogpassword(), LOG_PASSWORD_UPDATE);
        assertEquals(acc.getName(), NAME_UPDATE);
        assertEquals(acc.getMobile(), MOBILE_UPDATE);
        assertEquals(acc.getEmail(), EMAIL_UPDATE);
        assertEquals(acc.getStatusuuid(), STATUS_NEW);
    }

}
