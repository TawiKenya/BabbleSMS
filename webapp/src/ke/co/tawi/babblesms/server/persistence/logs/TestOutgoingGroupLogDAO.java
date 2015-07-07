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
import ke.co.tawi.babblesms.server.beans.log.OutgoingGrouplog;
import ke.co.tawi.babblesms.server.persistence.logs.OutgoingGroupLogDAO;

import org.junit.Ignore;
import org.junit.Test;


/**
 * Test for {@link OutgoingGroupLogDAO}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestOutgoingGroupLogDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String LOG_UUID = "a79c5824-6d32-4a94-9005-adc925db15d6", LOG_UUID_NEW = "R77YRHDJHSDWEUIRFJHKFSKJ";
    final String LOG_ORIGIN = "tawi", LOG_ORIGIN_NEW = "20272";
    final String LOG_DESTINATION = "a118c8ea-f831-4288-986d-35e22c91fc4",
            LOG_DESTINATION_NEW = "d312e301-9103-4326-93b8-fd3114264e00";
    final String LOG_MESSAGE = "The simple truth is",
            LOG_MESSAGE_NEW = "This a new SMS";
    final Date LOG_DATE = new Date(new Long("1420003360000"));  // 2014-10-05 18:33:21 (yyyy-MM-dd HH:mm:ss)

    final String SENDERUUID = "C6D5BA33-C95E-BCC3-D951-2A099E5F9230",
            SENDERUUID_NEW = "8038D870-5455-A2D6-18A9-BD5FA1D0A10A",
            SENDERUUID_NEW2 = "C3CFA249-F2A3-8253-1F44-B1C594C6A8D2";

    final String MESSAGESTATUSUUID = "49229BA2-91E5-7E64-F49C-923B7927C40D",
            MESSAGESTATUSUUID_NEW = "2F4AF191-8557-86C5-5D72-47DD44D303B1",
            MESSAGESTATUSUUID_UPDATE = "04C7CD60-9CC2-1B1A-EB6B-4A3E1FA8AC38";
    final String DEMO_USER = "650195B6-9357-C147-C24E-7FBDAEEC74ED";

    private OutgoingGroupLogDAO storage = new  OutgoingGroupLogDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.logs.OutgoingGroupLog#getOutgoingGroupLog(java.lang.String)}.
     */
    @Test
    public void testOutgoingGroupLogString() {
        storage = new OutgoingGroupLogDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        OutgoingGrouplog log = storage.get(LOG_UUID);
        assertEquals(log.getUuid(), LOG_UUID);
        assertEquals(log.getOrigin(), LOG_ORIGIN);
        assertEquals(log.getDestination(), LOG_DESTINATION);
        assertEquals(log.getMessage(), LOG_MESSAGE);
        assertEquals(log.getSender(), SENDERUUID);
        assertEquals(log.getMessagestatusuuid(), MESSAGESTATUSUUID);
        assertEquals(log.getLogTime(), LOG_DATE);

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.logs.OutgoingGroupLogDAO#getOutgoingGroupLog(java.util.List, int, int)}.
     */
    @Ignore
    @Test
    public void testGetOutgoingGroupLogDListOfStringIntInt() {
       
        /*List<OutgoingGrouplog> list = storage.getOutgoingGrouplogByAccount(SENDERUUID);

        //assertEquals(list.size(), 10);
        System.out.println(list);
        for (OutgoingGrouplog l : list) {
            System.out.println(l);
            assertTrue(l.getSender().equals(SENDERUUID));
        }*/
    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.logs.OutgoingGroupLog#PutOutgoingGroupLog(ke.co.tawi.babblesms.server.beans.log.OutgoingGroupLog)}.
     *
     * 
     * */
     @Ignore
     @Test 

     public void testPutOutgoingGroupLog() { 
    	 

     OutgoingGrouplog log = new OutgoingGrouplog(); 
     log.setUuid(LOG_UUID_NEW);
     log.setOrigin(LOG_ORIGIN_NEW);
     log.setDestination(LOG_DESTINATION_NEW);
     log.setMessage(LOG_MESSAGE_NEW);
     log.setSender(SENDERUUID_NEW);
     log.setMessagestatusUuid(MESSAGESTATUSUUID_NEW);
     
      assertTrue(storage.put(log));
      log = storage.get(LOG_UUID_NEW);
      assertEquals(log.getUuid(), LOG_UUID_NEW);
      assertEquals(log.getOrigin(),LOG_ORIGIN_NEW); 
      assertEquals(log.getDestination(), LOG_DESTINATION_NEW);
      assertEquals(log.getMessage(), LOG_MESSAGE_NEW);
      assertEquals(log.getSender(), SENDERUUID_NEW);
      assertEquals(log.getMessagestatusuuid(), MESSAGESTATUSUUID_NEW);
     }    
     

     /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.logs.OutgoingGroupLog#UpdateOutgoingGroupLog(ke.co.tawi.babblesms.server.beans.log.OutgoingGroupLog)}.
     *
     * 
     * */
     @Ignore
     @Test 
     public void testUpdateOutgoingGroupLog() { 
    	 
      assertTrue(storage.updateOutgoingGrouplog(LOG_UUID_NEW,MESSAGESTATUSUUID_UPDATE));
      
      OutgoingGrouplog log = storage.get(LOG_UUID_NEW);
      assertEquals(log.getUuid(), LOG_UUID_NEW);
      assertEquals(log.getOrigin(),LOG_ORIGIN_NEW); 
      assertEquals(log.getDestination(), LOG_DESTINATION_NEW);
      assertEquals(log.getMessage(), LOG_MESSAGE_NEW);
      assertEquals(log.getSender(), SENDERUUID_NEW);
      assertEquals(log.getMessagestatusuuid(), MESSAGESTATUSUUID_UPDATE);
     }
     

     /**
      * Test method for
      * {@link ke.co.tawi.babblesms.server.persistence.logs.BabbleOutgoingGroupLogDAO#getOutGoingGroupLog(ke.co.tawi.babblesms.server.beans.account.Account, int, int)
      * 
      * */
     
     @Test
     public void testgetOutGoingGroupLog(){  
    	 Account account = new Account();
    	 account.setUuid(DEMO_USER);
     
     List<OutgoingGrouplog> list = new ArrayList<>();
     list= storage.getOutGoingGroupLog(account, 0, 15);
     for(int i=0; i<list.size();i++){
    	 System.out.println(list.get(i));
     }
    	 
     }
     
     
}
