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
package ke.co.tawi.babblesms.server.persistence.logs;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;

import java.util.List;


/**
 * Persistence description for {@link OutgoingLog}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleOutgoingLogDAO {

    /**
     *
     * @param outgoingLog
     * @return	<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean put(OutgoingLog outgoingLog);

    
    /**
     *
     * @param uuid
     * @return a {@link OutgoingLog}
     */
    public OutgoingLog get(String uuid);
    
    /**
    *
    * @param uuid
    * @return a List of {@link OutgoingLog} objects
    */
   public List<OutgoingLog> getAll(String uuid);
    
    
    /**
     * 
     * Gets outgoing SMS based on account uuid. Results are ordered by time descending.
     * 
     * @param account
     * @param fromIndex
     * @param toIndex
     * @return a list of {@link OutgoingLog}
     */
    public List<OutgoingLog> get(Account account, int fromIndex, int toIndex);
    
}
