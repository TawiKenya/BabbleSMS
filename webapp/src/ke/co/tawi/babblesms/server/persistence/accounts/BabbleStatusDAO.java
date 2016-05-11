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
package ke.co.tawi.babblesms.server.persistence.accounts;

import ke.co.tawi.babblesms.server.beans.account.Status;

import java.util.List;

/**
 * Persistence description for {@link Status}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleStatusDAO {

	/**
    *
    * @param uuid
    * @return	a {@link Status}
    */
   public Status getStatus(String uuid);
   
   
   /**
   *
   * @param description
   * @return	a {@link Status}
   */
  public Status getStatusByName(String description);
  
  
  /**
  *
  * @return	a list of {@link Status}
  */
 public List<Status> getAllStatus(); 
 
 
  /**
    *
    * @param status
    * @return		<code>true</code> if successfully inserted; <code>false</code>
    * for otherwise
    */
  public boolean putStatus(Status status);
    
    
    /**
     *
     * @param uuid
     * @param status
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean updateStatus(String uuid, Status status);
         
}
