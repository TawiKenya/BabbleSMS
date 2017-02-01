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

import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.account.Account;

import java.util.List;

/**
 * Persistence implementation for {@link Contact}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleContactDAO {

	/**
    *
    * @param uuid
    * @return	a {@link Contact} with a matching uuid, or null if there is no
    * matching Contact.
    */
   public Contact getContact(String uuid);
   
   
   /**
    * 
    * @param account
    * @param name
    * @return	a {@link List} of {@link Contact}s whose name partly or wholly
    * matches the name and belongs to a particular account. Matching is case 
    * insensitive. An empty list is returned if no contact matches the name.
    */
  public List<Contact> getContactByName(Account account, String name);
  
  
  /**
   * @param account
   * @return	a list of networks by account uuid
   */
  public List<Contact> getContacts(Account account);
  
  
    /**
     *
     * @param contact
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putContact(Contact contact);

    
    /**
     * Note that the Account that a Contact belongs to is not updated.
     *
     * @param uuid
     * @param contact
     * @return	<code>true</code> if successfully updated; <code>false</code> for
     * otherwise
     */
    public boolean updateContact(String uuid, Contact contact);    
    
}
