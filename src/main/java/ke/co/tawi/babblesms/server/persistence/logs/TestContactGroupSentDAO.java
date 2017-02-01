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

import java.util.ArrayList;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.log.contactGroupsent;

import org.junit.Test;

/**
 * @author Migwi Ndung'u <code>mailto:<a>migwindungu0@gmail.com</a>
 *</code> 
 *
 * 
 */
public class TestContactGroupSentDAO {	
	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;
	
	final String SentGroupUuid="4c0a1edc-c515-4ded-93ef-b3b2499331d7f",
			SentGroupUuid_NEW="UKYC12345WK2343UI8KLwivrkkey",
			SentGroupUuid_CORRECT="c48222f3-a5df-4d18-a6d2-471ac3e43242";
	final String SentContactUuid="CA756C92-935B-D1FC-7E31-5368148F7429d",
			SentContactUuid_NEW="5d337e4ffd4a4064a190de7c74c683fc",
			SentContactUuid_CORRECT="d566fcf9-25ae-4df6-b97c-f0820b0577ee";
	
	private ContactGroupSentDAO cgsDAO = new ContactGroupSentDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.logs.ContactGroupSentDAO#put(ke.co.tawi.babblesms.server.beans.log.contactGroupsent)}.
	 */
	@Test
	public void testPut() {
		contactGroupsent cGroupsent = new contactGroupsent();
		cGroupsent.setSentContactUuid(SentContactUuid);
		cGroupsent.setSentGroupUuid(SentGroupUuid_CORRECT);
		
		Boolean Success = cgsDAO.put(cGroupsent);
		assertEquals(Success,true);
		
	}
	

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.logs.ContactGroupSentDAO#getUsingSentGroup(java.lang.String)}.
	 */
	//@Test
	public void testGetUsingSentGroup() {
	List<contactGroupsent> cGsent = new ArrayList<>();
	
	cGsent =cgsDAO.getUsingSentGroup(SentGroupUuid_CORRECT);
	
	for(int i=0; i<cGsent.size();i++){
		System.out.println(cGsent.get(i)); 
	}

	}

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.persistence.logs.ContactGroupSentDAO#getUsingSentContact(java.lang.String)}.
	 */
	//@Test
	public void testGetUsingSentContact() {
		List<contactGroupsent> cGsent = new ArrayList<>();
		
		cGsent =cgsDAO.getUsingSentContact(SentContactUuid_CORRECT);
		
		for(int i=0; i<cGsent.size();i++){
			System.out.println(cGsent.get(i)); 
		}
	}

}
