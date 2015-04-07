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

import static org.junit.Assert.*;

import java.util.Date;
import java.util.ArrayList;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.log.IncomingLog;

import org.junit.Test;
import org.junit.Ignore;


/**
 * Test for {@link IncomingLogDAO}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestIncomingLogDAO {

	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;
	
	
	final String LOG_UUID = "4c0a1edc-c515-4ded-93ef-b3b2499331d7", LOG_UUID_NEW = "UKYC12345WK2343UI8KLwivrkkey";
	final String LOG_ORIGIN = "254719851015", LOG_ORIGIN_NEW = "254728790694";
	final String LOG_DESTINATIONUUID = "5d337e4ffd4a4064a190de7c74c683fc",
			LOG_DESTINATIONUUID_NEW = "CA756C92-935B-D1FC-7E31-5368148F7429";
	final String    LOG_MESSAGE = "A late star lingered, remotely burning",
			LOG_MESSAGE_NEW = "This a new SMS";	
	final Date LOG_DATE = new Date(new Long("1420070075000") );  // 2014-10-04 03:08:06 (yyyy-MM-dd HH:mm:ss)
	
	final String DESTINATIONUUID1 = "20E36333-7ECB-8D42-8AD1-AF4E603E1A08", 
			DESTINATIONUUID2 = "CA756C92-935B-D1FC-7E31-5368148F7429", 
			DESTINATIONUUID3 = "7E816776-5BA2-171A-8E3A-23C9EE789DFA";
	
	final String ACCOUNT_UUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED";
	
	
	private IncomingLogDAO storage;
		
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.logs.IncomingLogDAO#getIncomingLog(java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testGetIncomingLogString() {
		storage = new IncomingLogDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		IncomingLog log = storage.getIncomingLog(LOG_UUID);
		assertEquals(log.getUuid(), LOG_UUID);	
		assertEquals(log.getOrigin(), LOG_ORIGIN);
		assertEquals(log.getDestination(), LOG_DESTINATIONUUID);
                assertEquals(log.getMessage(), LOG_MESSAGE);
		assertEquals(log.getLogTime(), LOG_DATE);
	}

	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.logs.IncomingLogDAO#getIncomingLog(java.util.List, int, int)}.
	 */
	//@Ignore
	@Test
	public void testGetIncomingLogListOfStringIntInt() {
		storage = new IncomingLogDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		/*List<String> destinationUuids = new ArrayList<String>();
		destinationUuids.add(DESTINATIONUUID1);
		destinationUuids.add(DESTINATIONUUID2);
		destinationUuids.add(DESTINATIONUUID3);*/
		
		Account account = new Account();
		account.setUuid(ACCOUNT_UUID);
		
		List<IncomingLog> list = storage.getIncomingLog(account, 5, 15);
		assertTrue(list.size()==10);
		assertEquals(list.get(0).getDestination() , "20240");
		assertEquals(list.get(0).getOrigin(), "254702765145");
		//assertEquals(list.size(), 10);
		/*System.out.println(list);
		for(IncomingLog l : list) {
			//System.out.println(l);
			assertTrue(l.getDestination().equals(DESTINATIONUUID1) ||
					l.getDestination().equals(DESTINATIONUUID2) ||
					l.getDestination().equals(DESTINATIONUUID3));
		}*/
	}

	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.logs.IncomingLogDAO#putIncomingLog(ke.co.tawi.babblesms.server.beans.log.IncomingLog)}.
	 */
	@Ignore
	@Test
	public void testPutIncomingLog() {
		storage = new IncomingLogDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
				
		IncomingLog log = new IncomingLog();
		log.setUuid(LOG_UUID_NEW);
		log.setOrigin(LOG_ORIGIN_NEW);
		log.setDestination(LOG_DESTINATIONUUID_NEW);
                log.setMessage(LOG_MESSAGE_NEW);
		log.setLogTime(LOG_DATE);
                
                		
		assertTrue(storage.putIncomingLog(log));
		
		log = storage.getIncomingLog(LOG_UUID_NEW);
		assertEquals(log.getUuid(), LOG_UUID_NEW);	
		assertEquals(log.getOrigin(), LOG_ORIGIN_NEW);
		assertEquals(log.getDestination(), LOG_DESTINATIONUUID_NEW);
		assertEquals(log.getMessage(), LOG_MESSAGE_NEW);
		assertEquals(log.getLogTime(), LOG_DATE);
	}

}

