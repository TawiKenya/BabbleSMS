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
package ke.co.tawi.babblesms.server.persistence.contacts;

import static org.junit.Assert.*;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.beans.account.Account;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests our persistence for Contact - Group.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestContactGroupDAO {
	

	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;
	
	final String FDB_UUID = "ked34-bf85-49e3-af69-e49de1158847sd346";
	final String FDB_CONTUUID = "4aa7446f-dbd5-4a04-90cf-2cdf62911a5f";
	final String FDB_CONTUUID2 = "e4d6df82-f93c-4275-9cb0-ba7f2ad0fd99";
	final String FDB_CGRPUUID = "dd80cff2-7902-42db-a918-da139cc86d0d";
        final String FDB_CGRPUUID2 = "218febb9-3542-4dca-b5c9-061b1a5a19f9";                            
	final String FDB_ACCUUID = "C3CFA249-F2A3-8253-1F44-B1C594C6A8D2";
	
	final String CG_UUID = "abc03dda-a020-41fb-a561-d7f1e25ade10", CG_UUID_NEW = "fwUk681d2e6-84f2-45ff-914c-522a3b076141";

    final String CONTACTUUID = "8b2900c2-fa2c-4ca8-9a21-c5c24143442f",
                 CONTACTUUID_NEW = "e0735818-1140-4379-906a-49c028959a12",
                 CONTACTUUID_UPDATE = "0269b471-bbcd-43e2-95be-a4c0687830e5";

    final String ACCUUID = "8038D870-5455-A2D6-18A9-BD5FA1D0A10A",
            ACCUUID_NEW = "650195B6-9357-C147-C24E-7FBDAEEC74ED";

    final String CGROUPUUID = "9bef62f6-e682-4efd-98e9-ca41fa4ef993",
            CGROUPUUID_NEW = "8669a4db-b899-489d-b469-92a9b87d0ba4";

    

    

    private ContactGroupDAO storage;

    /**
     * method for testing removing a contact from a contact group 
     */
    @Ignore
    @Test
    public void testRemovecontact(){
    	 storage = new ContactGroupDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
    	 
    	 Group group = new Group();
    	 group.setUuid(CGROUPUUID);
    	 
    	 Contact contact = new Contact();
    	 contact.setUuid(CONTACTUUID);
    	 
    	 assertTrue(storage.removeContact(contact, group));
    	
    }
    
    
    /**
     * method for testing getting  contacts given a group object
     */    
    @Ignore
    @Test
    public void testGetcontacts(){
    	 storage = new ContactGroupDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
    	 
    	 Group group = new Group();
    	 group.setUuid(CGROUPUUID);
    	 
    	 List<Contact> list = storage.getContacts(group);
    	 

    	 assertEquals(list.size(), 73);    	

    }
    
    
    /**
     * method for testing getting a given limit of contacts
     */
    @Ignore
    @Test
    public void testGetContactsbyLimit(){
    	storage = new ContactGroupDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
    	Group group = new Group();
   	    group.setUuid(CGROUPUUID);
   	 
   	 List<Contact> list = storage.getContacts(group,1,15);
   	 for(int i=0;i<list.size(); i++){
   		 System.out.println(list.get(i)); 
   	 }
    	
    }
    
    
    /**
     * method for testing getting groups given a contact object
     */
    //@Ignore
    @Test
    public void testGetgroups(){
    	storage = new ContactGroupDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
    	
    	Contact contact = new Contact();
    	contact.setUuid("290a302f-7de0-4126-9a05-ab9b94a5c8a0");
    	contact.setAccountUuid("650195B6-9357-C147-C24E-7FBDAEEC74ED");
    	    	
    	List<Group> list = storage.getGroups(contact);
    	System.out.println(list.toString());
    	
    	assertEquals(list.size(), 0);    	
    }
    
    
    /**
     * method to test creating a new contactgroup association given a group and contact object
     */
    @Ignore
    @Test
    public void putContactgroup(){
    	storage = new ContactGroupDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
    	
    	Contact contact = new Contact();
    	contact.setUuid(CONTACTUUID_NEW);
    	contact.setAccountUuid(ACCUUID);
    	
    	Group group = new Group();
    	group.setUuid(CGROUPUUID);
    	
    	assertTrue(storage.putContact(contact, group));
    }

}
