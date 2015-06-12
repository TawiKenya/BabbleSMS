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
package ke.co.tawi.babblesms.server.persistence.network;

import java.util.HashMap;
import java.util.List;





/**
 * Persistence implementation
 * <p>
 *  
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>
 */
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.GroupContacts;
import ke.co.tawi.babblesms.server.beans.contact.Phone;

public interface BabblenetworkcountDAO {
	
	
	/**
     *gets the network names and 
     *and returns a hashmap with a count per network.
     */
	public  HashMap<String, String> network(String uuid);
	

    /**
     *gets the network names from the database and thier respective uuuids
     *and returns a hashmap with both.
     */
	public HashMap<String, String> allnetworks();
	
	
	
	/**
     *gets the contact uuids from the database for a given group
     *and returns a list with both.
     */
	public List<GroupContacts> collectContacts(String uuid);
	
	
	
	/**
     *gets the contact uuids from the database for a given group
     *and returns a list with both.
     */	
	public List<Phone> getAllPhones(GroupContacts contact);
	
	
	/**    
     * returns a list with all contacts per network per group.
     */
	
	public  List<Phone> contactspernetwork(String grpuuid,String nwkuuid);
	
	
	
	/**    
     * returns a list with all contacts per group.
     */
	public List<Phone>allgrpcontacts(String grpuuid);
}

