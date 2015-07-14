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

import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.account.Account;

import java.util.List;

/**
 * Persistence description for Contact - Group relationship.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleContactGroupDAO {

   
    /**
     * Method used for creating a new relationship between a contact and a group
     * 
     * @param contact
     * @param group
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putContact(Contact contact, Group group);

    
    /**
    * Method for removing a relationship between a contact and a group
    * 
    * @param contact
    * @param group
    * @return		<code>true</code> if successfully inserted; <code>false</code>
    * for otherwise
    */
   public boolean removeContact(Contact contact, Group group);
   
   
   /**
  * @param group
  * @return a list of {@link Contact}s belonging to the particular {@link Group}
  */
	public List<Contact> getContacts(Group group);
	
	
	/**
	 * @param int fromIndex
	 * @param int toIndex
	  * @param group
	  * @return a list of {@link Contact}s belonging to the particular {@link Group} of given pageSize
	  */
		public List<Contact> getContacts(Group group, int fromIndex, int toIndex);
   
   
   /**
	 * @param contact
	 * @param account
	 * @return a list of {@link Contact}s belonging to the particular {@link Group}
	 */
	public List<Group> getGroups(Contact contact, Account account);   
}
