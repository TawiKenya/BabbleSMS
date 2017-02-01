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
package ke.co.tawi.babblesms.server.persistence.utils;

import static org.junit.Assert.*;

import java.util.HashMap;

import ke.co.tawi.babblesms.server.beans.messagetemplate.MsgStatus;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.junit.Test;

/**
 * @author Migwi Ndung'u <code>mailto:<a>migwindungu0@gmail.com</a>
 *</code> 
 *
 * 
 */
public class TestSentGroupMessageCount {
	
	final String DB_NAME = "babblesmsdb";
	  final String DB_HOST = "localhost";
	  final String DB_USERNAME = "babblesms";
	  final String DB_PASSWORD = "Hymfatsh8";
	  final int DB_PORT = 5432;
	  
	  
	private SentGroupMessageCount SGMCount = new SentGroupMessageCount(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWORD, DB_PORT);
	private MsgStatus state = new MsgStatus();
	
	final String SentGroupUuid="c48222f3-a5df-4d18-a6d2-471ac3e43242";
	final String SentContactUuid= "d566fcf9-25ae-4df6-b97c-f0820b0577ee";
	final String STATUS_SENT = state.SENT;
	final String STATUS_RECIEVED = state.RECEIVED;
	final String STATUS_REJECTED = state.REJECTED;
	final String STATUS_INTRANSIT=state.IN_TRANSIT;
	final String STATUS_FAILED=state.FAILURE;

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.utils.SentGroupMessageCount#getGroupContactMessages(java.lang.String)}.
	 */
	@Test
	public void testGetGroupContactMessages() {
		HashMap<String,Integer> str = new HashMap<>();
		
		str = SGMCount.getGroupContactMessages(SentGroupUuid);
		System.out.println(str);
		
	}

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.utils.SentGroupMessageCount#getContactMessageCount(java.lang.String, java.lang.String)}.
	 */
    @Test
	public void testGetContactMessageCount() {
    	int count =0;    	
		count =SGMCount.getContactMessageCount(SentGroupUuid, "2F4AF191-8557-86C5-5D72-47DD44D303B1");
    	
		System.out.println(count);		
	}

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.utils.SentGroupMessageCount#getMessageStatus()}.
	 */
	@Test
	public void testGetMessageStatus() {
		HashMap<String, String> MapStatus = new HashMap<>();		
		MapStatus = SGMCount.getMessageStatus();
				
		HashMap<String, String> checkStatus = new HashMap<>();
		checkStatus.put(STATUS_REJECTED,"Rejected");
		checkStatus.put(STATUS_RECIEVED,"Received");
		checkStatus.put(STATUS_INTRANSIT,"In Transit");		
		checkStatus.put(STATUS_SENT, "Sent");	
		checkStatus.put(STATUS_FAILED, "Failure");	
		assertEquals( checkStatus,MapStatus);
	}

}
