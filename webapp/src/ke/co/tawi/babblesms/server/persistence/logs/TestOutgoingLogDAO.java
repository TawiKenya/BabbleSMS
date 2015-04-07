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
package ke.co.tawi.babblesms.server.persistence.logs;

import java.util.ArrayList;
import java.util.Date;

import static org.junit.Assert.*;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.log.IncomingLog;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountsDAO;
import ke.co.tawi.babblesms.server.persistence.logs.OutgoingLogDAO;

import org.junit.Test;

/**
 * Test for {@link OutgoingLogDAO}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestOutgoingLogDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String LOG_UUID = "3a173565-10c1-4f83-8aaa-af8a80dc59eb", LOG_UUID_NEW = "RXCUYRHDJHSDWEUIRFJHKFSKJ";
    final String LOG_ORIGIN = "impala", LOG_ORIGIN_NEW = "20272";
    final String LOG_DESTINATION = "254712058706",
            LOG_DESTINATION_NEW = "254712058704";
    final String LOG_MESSAGE = "In the scheme of things",
            LOG_MESSAGE_NEW = "This a new SMS";
    final Date LOG_DATE = new Date(new Long("1412523201000"));  // 2014-10-05 18:33:21 (yyyy-MM-dd HH:mm:ss)

    final String NETWORKUUID1 = "B936DA83-8A45-E9F0-2EAE-D75F5C232E78",
            NETWORKUUID2 = "5C1D9939-136A-55DE-FD0E-61D8204E17C9",
            NETWORKUUID3 = "0DE968C9-7309-C481-58F7-AB6CDB1011EF";

    final String SENDERUUID = "C3CFA249-F2A3-8253-1F44-B1C594C6A8D2",
            SENDERUUID_NEW = "8038D870-5455-A2D6-18A9-BD5FA1D0A10A";

    final String MESSAGESTATUSUUID = "49229BA2-91E5-7E64-F49C-923B7927C40D",
            MESSAGESTATUSUUID_NEW = "2F4AF191-8557-86C5-5D72-47DD44D303B1",
            MESSAGESTATUSUUID_UPDATE = "04C7CD60-9CC2-1B1A-EB6B-4A3E1FA8AC38";

    private OutgoingLogDAO storage;

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.logs.OutgoingLog#getOutgoingLog(java.lang.String)}.
     */
    @Test
    public void testOutgoingLogString() {
        storage = new OutgoingLogDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        OutgoingLog log = storage.getOutgoingLog(LOG_UUID);
        assertEquals(log.getUuid(), LOG_UUID);
        assertEquals(log.getOrigin(), LOG_ORIGIN);
        assertEquals(log.getDestination(), LOG_DESTINATION);
        assertEquals(log.getMessage(), LOG_MESSAGE);
        assertEquals(log.getNetworkuuid(), NETWORKUUID1);
        assertEquals(log.getSender(), SENDERUUID);
        assertEquals(log.getMessagestatusuuid(), MESSAGESTATUSUUID);
        assertEquals(log.getLogTime(), LOG_DATE);

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.logs.OutgoingLogDAO#getOutgoingLog(java.util.List, int, int)}.
     */
    //@Ignore
    @Test
    public void testGetOutgoingLogDListOfAccountIntInt() {
        storage = new OutgoingLogDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
        AccountsDAO acDAO = AccountsDAO.getInstance();
        Account account = new Account();
        account = acDAO.getAccount(SENDERUUID);
        List<OutgoingLog> list = storage.getOutgoingLog(account, 5, 15);

        //assertEquals(list.size(), 10);
        System.out.println(list);
        for (OutgoingLog l : list) {
            System.out.println(l);
            assertTrue(l.getSender().equals(SENDERUUID));
        }
    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.logs.OutgoingLog#PutOutgoingLog(ke.co.tawi.babblesms.server.beans.log.OutgoingLog)}.
     */
    @Test
    public void testPutOutgoingLog() {
        storage = new OutgoingLogDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        OutgoingLog log = new OutgoingLog();
        log.setUuid(LOG_UUID_NEW);
        log.setOrigin(LOG_ORIGIN_NEW);
        log.setDestination(LOG_DESTINATION_NEW);
        log.setMessage(LOG_MESSAGE_NEW);
        log.setNetworkuuid(NETWORKUUID2);
        log.setSender(SENDERUUID_NEW);
        log.setMessagestatusuuid(MESSAGESTATUSUUID_NEW);

        assertTrue(storage.putOutgoingLog(log));

        log = storage.getOutgoingLog(LOG_UUID_NEW);
        assertEquals(log.getUuid(), LOG_UUID_NEW);
        assertEquals(log.getOrigin(), LOG_ORIGIN_NEW);
        assertEquals(log.getDestination(), LOG_DESTINATION_NEW);
        assertEquals(log.getMessage(), LOG_MESSAGE_NEW);
        assertEquals(log.getNetworkuuid(), NETWORKUUID2);
        assertEquals(log.getSender(), SENDERUUID_NEW);
        assertEquals(log.getMessagestatusuuid(), MESSAGESTATUSUUID_NEW);
    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.logs.OutgoingLog#updateOutgoingLog(ke.co.tawi.babblesms.server.beans.log.OutgoingLog)}.
     */
   

}
