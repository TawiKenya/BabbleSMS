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
package ke.co.tawi.babblesms.server.persistence.geolocation;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.geolocation.Country;


/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 */
public interface BabbleCountryDAO {

    
    /**
     *
     * @param uuid
     * @return	a {@link Country} with a matching UUID or null if none is found.
     */
    public Country getCountry(String uuid);

    
    /**
     *
     * @param name
     * @return	a {@link Country} with a matching UUID or null if none is found.
     */
    public Country getCountryByName(String name);

    
    /**
     *
     * @return	a list of all {@link Country}s
     */
    public List<Country> getAllCountries();
    
}
