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
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;

/**
 * Persistence description for {@link Shortcode}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleShortcodeDAO {

    /**
     *
     * @param shortcode
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putShortcode(Shortcode shortcode);

    /**
     *
     * @param uuid
     * @param network
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean updateShortcode(Shortcode shortcode);

    /**
     *
     * @param uuid
     * @return	a network
     */
    public Shortcode getShortcode(String uuid);
    
    
    /**
     *
     * @param name
     * @return	a network
     */
    public Shortcode getShortcodeBycodeNumber(String codenumber,String networkuuid);

    
  
    /**
     *
     * @param uuid
     * @return	a network
     */
    public List<Shortcode> getShortcodebyaccountuuid(String accuuid);

    /**
     *
     * @return	a list of networks
     */
    public List<Shortcode> getAllShortcode();
    

    /**
     *
     * @param uuid
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean deleteShortcode(String uuid);

    
}
