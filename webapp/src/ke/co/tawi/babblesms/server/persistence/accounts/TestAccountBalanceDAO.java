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

import ke.co.tawi.babblesms.server.beans.account.AccountBalance;

import java.util.List;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests our persistence for the account balance.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestAccountBalanceDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String ACC_UUID = "143bd229-bb55-4b6d-b1c4-ca7b8f4c78a7", ACC_UUID_NEW = "fwk681d2e6-84f2-45ff-914c-522a3b076141";
    final String ACC_ORIGIN = "Notice", ACC_ORIGIN_NEW = "Info";
    
    final int ACC_BALANCE = 1852,
            ACC_BALANCE_NEW = 1000,
            ACC_BALANCE_FINAL=ACC_BALANCE_NEW + 12611,
            ACC_DEDUCT_NEW = 100,
            ACC_DEDUCT_FINAL=ACC_BALANCE_FINAL - 100;

    final String SENDERUUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED",
            SENDERUUID_NEW = "8038D870-5455-A2D6-18A9-BD5FA1D0A10A",
            SENDERUUID_NEW2 = "C3CFA249-F2A3-8253-1F44-B1C594C6A8D2";

    final String NETWORKUUID = "5C1D9939-136A-55DE-FD0E-61D8204E17C9",
            NETWORKUUID_NEW = "741AC38C-3E40-6887-9CD6-365EF9EA1EF0";

    private AccountBalanceDAO storage;

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.beans.account.AccountBalance#getAccountBalance(java.lang.String)}.
     */
    @Test
    public void testAccountBalanceString() {
        storage = new AccountBalanceDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        AccountBalance acc = storage.getClientBalanceByUuid(ACC_UUID);
        assertEquals(acc.getUuid(), ACC_UUID);
        assertEquals(acc.getOrigin(), ACC_ORIGIN);
        assertEquals(acc.getAccountuuid(), SENDERUUID);
        assertEquals(acc.getNetworkuuid(), NETWORKUUID);
        assertEquals(acc.getBalance(), ACC_BALANCE);
       

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.accounts.AccountBalanceDAO#GetAccountBalanceByaccount}.
     */
    //@Ignore
    @Test
    public void testGetAccountBalanceByaccount() {
        storage = new AccountBalanceDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        List<AccountBalance> list = storage.getClientBalanceByAccount(SENDERUUID);

        //assertEquals(list.size(), 10);
        System.out.println(list);
        for (AccountBalance l : list) {
            System.out.println(l);
            assertTrue(l.getAccountuuid().equals(SENDERUUID));
        }
    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.beans.account.AccountBalance#addCredit(ke.co.tawi.babblesms.server.beans.account.AccountBalance)}.
     *
     */
    @Test
    public void testUpdateAccountBalanceLog() {
        storage = new AccountBalanceDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        AccountBalance acc = new AccountBalance();
        

        assertTrue(storage.addCredit(ACC_UUID_NEW,ACC_BALANCE_NEW));
        
        acc = storage.getClientBalanceByUuid(ACC_UUID_NEW);
        assertEquals(acc.getUuid(), ACC_UUID_NEW);
        assertEquals(acc.getOrigin(), ACC_ORIGIN_NEW);
        assertEquals(acc.getNetworkuuid(),NETWORKUUID_NEW);
        assertEquals(acc.getAccountuuid(), SENDERUUID_NEW2);
        assertEquals(acc.getBalance(), ACC_BALANCE_FINAL);
    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.beans.account.AccountBalance#deductCredit(ke.co.tawi.babblesms.server.beans.account.AccountBalance)}.
     *
     *
     *
     */
    @Test
    public void testDeductAccountBalanceLog() {
        storage = new AccountBalanceDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        AccountBalance acc = new AccountBalance();
        

        assertTrue(storage.deductCredit(ACC_ORIGIN_NEW,NETWORKUUID_NEW,1));
        
        acc = storage.getClientBalanceByUuid(ACC_UUID_NEW);
        assertEquals(acc.getUuid(), ACC_UUID_NEW);
        assertEquals(acc.getOrigin(), ACC_ORIGIN_NEW);
        assertEquals(acc.getNetworkuuid(),NETWORKUUID_NEW);
        assertEquals(acc.getAccountuuid(), SENDERUUID_NEW2);
        assertEquals(acc.getBalance(), ACC_DEDUCT_FINAL);
    }
  
}
