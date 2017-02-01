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

import java.util.List;

import static org.junit.Assert.*;
import org.junit.Ignore;
import org.junit.Test;

import ke.co.tawi.babblesms.server.beans.account.Status;

/**
 * Tests our persistence implementation for {@link Status}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestStatusDAO {

	final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;
    
    final String UUID = "396F2C7F-961C-5C12-3ABF-867E7FD029E6", UUID2 = "f32wk681d2e6-84f2-45ff-914c-522a3b076141";

    final String DESCRIPTION = "Active",
            DESCRIPTION2 = "Newstatus",
            DESCRIPTION3 = "updated Status";

    final int STATUS_COUNT = 4;
    
    private StatusDAO storage;
    
    
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.accounts.StatusDAO#getStatus(java.lang.String)}.
	 */
    @Ignore
	@Test
	public void testGetStatus() {
		storage = new StatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Status s = storage.getStatus(UUID);
        assertEquals(s.getUuid(), UUID);
        assertEquals(s.getDescription(), DESCRIPTION);
	}


	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.accounts.StatusDAO#getStatusByName(java.lang.String)}.
	 */
    @Ignore
	@Test
	public void testGetStatusByName() {
		storage = new StatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Status s = storage.getStatusByName(DESCRIPTION);
        assertEquals(s.getUuid(), UUID);
        assertEquals(s.getDescription(), DESCRIPTION);
	}
	
	
    /**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.accounts.StatusDAO#getAllStatus()}.
	 */
    @Ignore
	@Test
	public void testGetAllStatus() {
		storage = new StatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		List<Status> list = storage.getAllStatus();
		assertEquals(list.size(), STATUS_COUNT);
		
	}
	
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.accounts.StatusDAO#putStatus(ke.co.tawi.babblesms.server.beans.account.Status)}.
	 */
    @Ignore
	@Test
	public void testPutStatus() {
		storage = new StatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Status s = new Status();
		s.setUuid(UUID2);
		s.setDescription(DESCRIPTION2);
		
		assertTrue(storage.putStatus(s));
		
		s = storage.getStatus(UUID2);
        assertEquals(s.getUuid(), UUID2);
        assertEquals(s.getDescription(), DESCRIPTION2);
	}
	

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.accounts.StatusDAO#updateStatus(java.lang.String, java.lang.String)}.
	 */
	@Test
	public void testUpdateStatus() {
		storage = new StatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Status s = new Status();
		s.setUuid(UUID2);
		s.setDescription(DESCRIPTION3);
		
		assertTrue(storage.updateStatus(UUID2, s));
		
		s = storage.getStatus(UUID2);
        assertEquals(s.getUuid(), UUID2);
        assertEquals(s.getDescription(), DESCRIPTION3);
	}

}
