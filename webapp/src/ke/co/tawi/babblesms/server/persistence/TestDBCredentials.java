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
package ke.co.tawi.babblesms.server.persistence;

import static org.junit.Assert.*;

import org.junit.Test;

import java.sql.Connection;

/**
 * Tests our class with database credentials.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestDBCredentials {

	private DBCredentials dbCredentials;

	/**
	 * Test method for {@link DBCredentials#getConnection()}.
	 */
	@Test
	public void testGetConnection() {
                
		dbCredentials = new DBCredentials("babblesmsdb", "localhost", "babblesms", 
				"Hymfatsh8", 5432);
		
		Connection conn; 
		
		//for(int j=0; j<100; j++) {
			//try {
				conn = dbCredentials.getConnection();
				System.out.println("Connection is: " + conn);
				//System.out.println("Connection " + j + " is: " + conn);
				//conn.close();
				
			//} catch(SQLException e) {
	    	//	System.out.println("SQL exception while trying to test connections");
	    	//	e.printStackTrace();
	    	//}			
		//}
				
		
	}	
}
