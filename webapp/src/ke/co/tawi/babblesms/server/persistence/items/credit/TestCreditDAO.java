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
package ke.co.tawi.babblesms.server.persistence.items.credit;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Credit;
import ke.co.tawi.babblesms.server.persistence.items.credit.CreditDAO;
import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Tests our persistence implementation for {@link Credit}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestCreditDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String CR_UUID = "1966047C-8771-0E53-4943-0BE1132FD508", CR_UUID_NEW = "fP81d2e6-84f2-45ff-914c-522a3b076141UKL";
    final String CR_SOURCE = "32903",
            CR_SOURCE2 = "offer",
            CR_SOURCE3 = "notice",
            CR_SOURCE_NEW = "SOURCE12";

    final int CR_CREDIT = 3,
            CR_CREDIT_NEW = 10,
            CR_CREDIT_UPDATE = 10,
            CR_CREDIT_FINAL = 40;

    final String ACCOUNT = "C937CE62-C4A9-131F-C96E-2DB8A9E886AB",
            ACCOUNT1 = "650195B6-9357-C147-C24E-7FBDAEEC74ED",
            ACCOUNT_NEW = "C3CFA249-F2A3-8253-1F44-B1C594C6A8D2";

    private CreditDAO storage;

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.credit#GetAccountByuuidString(java.lang.String)}.
     */
    @Test
    public void testGetAccountByuuidString() {
        storage = new CreditDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Credit cr = storage.getCredit(CR_UUID);
        assertEquals(cr.getUuid(), CR_UUID);
        assertEquals(cr.getSource(), CR_SOURCE);
        assertEquals(cr.getCredit(), CR_CREDIT);
        assertEquals(cr.getAccountuuid(), ACCOUNT);

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.credit#CreditByAccounts}.
     */
    //@Ignore
    @Test
    public void testCreditByAccounts() {
        storage = new CreditDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        List<Credit> list = storage.getAllCreditsByaccountuuid(ACCOUNT1);

        //assertEquals(list.size(), 10);
        System.out.println(list);
        for (Credit l : list) {
            System.out.println(l);
            assertTrue(l.getSource().equals(CR_SOURCE2)
                    || l.getSource().equals(CR_SOURCE3));
        }
    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.credit#Putcredit(ke.co.tawi.babblesms.server.beans.credit)}.
     */
    @Test
    public void testPutCredit() {
        storage = new CreditDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Credit cr = storage.getCredit(CR_UUID);
        cr.setUuid(CR_UUID_NEW);
        cr.setSource(CR_SOURCE2);
        cr.setCredit(CR_CREDIT_NEW);
        cr.setAccountuuid(ACCOUNT_NEW);

        assertTrue(storage.putCredit(cr));

        cr = storage.getCredit(CR_UUID_NEW);
        assertEquals(cr.getUuid(), CR_UUID_NEW);
        assertEquals(cr.getSource(), CR_SOURCE2);
        assertEquals(cr.getCredit(), CR_CREDIT_NEW);
        assertEquals(cr.getAccountuuid(), ACCOUNT_NEW);
        
        

    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.credit#Updatecredit(ke.co.tawi.babblesms.server.beans.credit)}.
     */
    @Test
    public void testUpdateCredit() {
        
        storage = new CreditDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        
        assertTrue(storage.updateCredit(CR_UUID_NEW,CR_CREDIT_UPDATE));

        Credit cr = storage.getCredit(CR_UUID_NEW);
        assertEquals(cr.getUuid(), CR_UUID_NEW);
        assertEquals(cr.getSource(), CR_SOURCE2);
        assertEquals(cr.getCredit(), CR_CREDIT_FINAL);
        assertEquals(cr.getAccountuuid(), ACCOUNT_NEW);

    }

}
