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

import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.account.Account;

import java.util.List;


/**
 * Persistence description for {@link Mask}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleMaskDAO {

	/**
    *
    * @param uuid
    * @return	a {@link Mask} with a matching uuid, or null if there is no 
    * such Mask.
    */
   public Mask get(String uuid);
   
   
    /**
     *
     * @param mask
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean put(Mask mask);

        
    /**
     * @param account
     * @return a List of {@link Mask}s belonging to this account.
     */
    public List<Mask> getMasks(Account account);
    
    
    /**
     * 
     * @return a List of all {@link Mask}s
     */
    public List<Mask> getAllMasks();
    
    
    /**
     * Update a Mask with the corresponding UUID.
     *
     * @param mask
     * @param uuid
     * @return		<code>true</code> if successfully edited; <code>false</code> for
     * otherwise
     */
    public boolean updateMask(Mask mask, String uuid);    
    
}
