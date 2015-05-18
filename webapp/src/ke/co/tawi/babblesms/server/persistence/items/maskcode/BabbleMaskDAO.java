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
package ke.co.tawi.babblesms.server.persistence.items.maskcode;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.maskcode.Mask;

/**
 * Persistence description for {@link Mask}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleMaskDAO {

    /**
     *
     * @param mask
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putMask(Mask mask);

    /**
     *
     * @param mask
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean updateMask(Mask mask);

    /**
     *
     * @param uuid
     * @return	a network
     */
    public Mask getMask(String uuid);
    
    /**
     *
     * @param name
     * @return	a network
     */
    public Mask getMaskByName(String name);

    
    
    /**
     *
     * @return	a list of networks
     */
    public List<Mask> getmaskbyaccount(String accuuid);

    /**
     *
     * @return	a list of networks
     */
    public List<Mask> getAllMasks();

    /**
     *
     * @param uuid
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean deleteMask(String uuid);

    
}
