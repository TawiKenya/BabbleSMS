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
package ke.co.tawi.babblesms.server.persistence.maskcode;

import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.beans.account.Account;

import java.util.List;


/**
 * Persistence description for {@link Shortcode}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleShortcodeDAO {

	/**
    *
    * @param uuid
    * @return	a {@link Shortcode} which matches the UUID, or null if there is
    * none.
    */
   public Shortcode get(String uuid);
   
   
    /**
     *
     * @param shortcode
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean put(Shortcode shortcode);

    
    /**
    *
    * @param account
    * @return a list of {@link Shortcode}s belonging to this account.
    */
   public List<Shortcode> getShortcodes(Account account);
   
   
   /**
   *
   * @return a list of all {@link Shortcode}s.
   */
  public List<Shortcode> getAllShortcodes();
  
   
    /**
     * Update a Shortcode with the matching UUID.
     *
     * @param uuid
     * @param shortcode
     * @return		<code>true</code> if successfully updated; <code>false</code> for
     * otherwise
     */
    public boolean update(Shortcode shortcode, String uuid);    
    
}
