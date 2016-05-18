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

import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.beans.contact.Contact;

import java.util.List;

/**
 * Persistence description for a {@link Phone}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabblePhoneDAO {

	/**
    *
    * @param uuid
    * @return a {@link Phone}
    */
   public Phone getPhone(String uuid);

  /**
  *
  * @param phoneNum
  * @param contact 
  * @return a list of {@link Phone}s which match the phone number, either 
  * partially or wholly. The result is limited to 30 numbers. The phones belong
  * to this particular Contact.
  */
 public List<Phone> getPhones(String phoneNum, Contact contact);
 
 
  /**
  *
  * @param contact
  * @return	a list of {@link Phone}s belonging to this contact
  */
 public List<Phone> getPhones(Contact contact);
  
 
    /**
     *
     * @param phone
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putPhone(Phone phone);

    
    /**
     * Note that the Contact that the Phone belongs to does not change with this method.
     *
     * @param uuid
     * @param phone
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean updatePhone(String uuid, Phone phone);

}
