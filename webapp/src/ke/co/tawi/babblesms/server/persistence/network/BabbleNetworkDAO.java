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
package ke.co.tawi.babblesms.server.persistence.network;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.network.Network;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 */
public interface BabbleNetworkDAO {

    /**
     *
     * @param network
     * @return	<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putNetwork(Network network);

    
    /**
     *
     * @param uuid
     * @param network
     * @return	<code>true</code> if successfully updated; <code>false</code> for
     * otherwise
     */
    public boolean updateNetwork(String uuid, Network network);

    
    /**
     *
     * @param uuid
     * @return	a {@link Network} with a matching uuid or null if there is none
     * found
     */
    public Network getNetwork(String uuid);

    
    /**
     *
     * @param name
     * @return	a {@link Network} with a matching uuid or null if there is none
     * found
     */
    public Network getNetworkByName(String name);

    
    /**
     *
     * @return	a list of all {@link Network}s
     */
    public List<Network> getAllNetworks();
    
}
