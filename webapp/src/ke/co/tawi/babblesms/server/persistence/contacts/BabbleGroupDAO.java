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

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Group;

import java.util.List;
import java.util.Map;

/**
 * Persistence description for a {@link Group}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleGroupDAO {

	/**
    *
    * @param uuid
    * @return	a uuid
    */
   public Group getGroup(String uuid);
   
 
   
   
   /**
	 * @param group
	 * @return  a group
	 */
	public Group getGroupByName(Account account , String group);
      
   
   /**
   *
   * @param account
   * @return	a list of {@link Group}s belonging to that {@link Account}
   */
   public List<Group> getGroups(Account account);
    
     
   /**
   *
   * @param account
   * @return a list of all {@link Group}s
   */
   public List<Group> getAllGroups();
   
   
    /**
     *
     * @param group
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putGroup(Group group);

    
    /**
     *
     * @param uuid
     * @param group
     * @return	<code>true</code> if successfully updated; <code>false</code> for
     * otherwise
     */
    public boolean updateGroup(String uuid, Group group);


    /**
     * Return a mapping of the count of Contacts in a Group for all the Groups
     * of this particular account holder.
     * <p>
     * The key of the returned {@link Map} is the UUID of the {@link Group}, 
     * and the value is the count. 
     * 
     * @param account
     * @return
     */
    public Map<String,Integer> getGroupCount(Account account);
}
