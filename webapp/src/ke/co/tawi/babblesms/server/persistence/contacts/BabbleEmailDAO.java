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
import ke.co.tawi.babblesms.server.beans.contact.Email;

import java.util.List;

/**
 * Persistence description for an {@link Email}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleEmailDAO {

	/**
    *
    * @param uuid
    * @return	an {@link Email}
    */
   public Email getEmail(String uuid);
   
   
   /**
   *
   * @param text
   * @param contact
   * @return a list of {@link Email}s which match the text, either 
   * partially or wholly.
   */
  public List<Email> getEmails(String text, Contact contact);
  
  
  /**
  *
  * @param contact
  * @return	a list of {@link Email}s belonging to this contact
  */
  public List<Email> getEmails(Contact contact);
  
 
    /**
     *
     * @param email
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putEmail(Email email);

    
    /**
     *
     * @param uuid
     * @param email
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean updateEmail(String uuid, Email email);
    
}
