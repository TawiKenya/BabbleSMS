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

import ke.co.tawi.babblesms.server.beans.network.Network;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests our {@link CountUtils}.
 * <p> 
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class TestCountUtils {

    final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;
    
    final String ACCOUNT_USERNAME = "tawi";
    final String DEMO_ACCOUNTUUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED";
    final String SAFARICOM_NETWORKUUID = "B936DA83-8A45-E9F0-2EAE-D75F5C232E78";
    final String FDB_DATE= "04/04/2015";
    final String FDB_DATE2 = "05/14/2015";
       
    private CountUtils storage;
    
    private Network network;
   
    
	/**
	 * Test method for {@link CountUtils#getAllIncomingSMSCount(java.lang.String)}.
	 */
    @Ignore
	@Test
	public void testgetIncomingLog() {
		storage = new CountUtils(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		System.out.println("SMS count is: " + storage.getIncomingCount(DEMO_ACCOUNTUUID));
	}
	

	/**
	 * Test method for {@link CountUtils#getIncomingCount(java.lang.String, Network)}.
	 */
    @Ignore
	@Test
	public void testGetIncomingCount() {
		storage = new CountUtils(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
                
        network = new Network();
        network.setUuid(SAFARICOM_NETWORKUUID);
		
		System.out.println("Incoming SMS count is: " + storage.getIncomingCount(DEMO_ACCOUNTUUID, network));
	}
                
   
	/**
	 * Test method for {@link CountUtils#getOutgoingCount(java.lang.String, Network)}.
	 */
    @Ignore
	@Test
	public void testGetOutgoingCount() {
		storage = new CountUtils(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
                
        network = new Network();
        network.setUuid(SAFARICOM_NETWORKUUID);
		
		System.out.println("Outgoing SMS count is: " + storage.getIncomingCount(DEMO_ACCOUNTUUID, network));
	}
	
	
	/**
	 * Test method for {@link CountUtils#getAllIncomingSMSCount(java.lang.String)}.
	 */
	
	@Test
	public void testgetIncomingCount2() throws ParseException {
		storage = new CountUtils(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
                
                network=new Network();
                network.setUuid(SAFARICOM_NETWORKUUID);
                
                Date date = new SimpleDateFormat("MM/dd/yyyy").parse(FDB_DATE);
                Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse(FDB_DATE2);
		
		System.out.println("SMS count is: " + storage.getIncomingCount(DEMO_ACCOUNTUUID, network, date, date2));
	}
	
        
    /**
	 * Test method for {@link CountUtils#getAllIncomingSMSCount(java.lang.String)}.
	 */
	@Ignore
	@Test
	public void getOutgoingCount() throws ParseException {
		storage = new CountUtils(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
                
                network=new Network();
                network.setUuid(SAFARICOM_NETWORKUUID);
                
                Date date = new SimpleDateFormat("MM/dd/yyyy").parse(FDB_DATE);
                Date date2 = new SimpleDateFormat("MM/dd/yyyy").parse(FDB_DATE2);
		
		System.out.println("SMS count is: " + storage.getOutgoingCount(DEMO_ACCOUNTUUID, network, date, date2));
	}

}
