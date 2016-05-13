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

import static org.junit.Assert.*;

import org.junit.Test;

import ke.co.tawi.babblesms.server.beans.geolocation.Country;

public class TestCountryDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String CN_UUID = "57e64152-9e8d-4c45-abbc-67a0331aa46e", CN_UUID_NEW = "f409681d2e6-84f2-45ff-914c-522a3b076141";
    final String CN_NAME = "Kenya",
            CN_NAME_NEW = "New country",
            CN_NAME_UPDATED="Updated country";

    final String CODEFIPS = "KE",
            CODEFIPS_NEW = "NCK",
            CODEFIPS_UPDATE = "NCY";

    private CountryDAO storage;

    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.beans.geolocation.Country#getCountry(java.lang.String)}.
     */
    @Test
    public void testgetCountryString() {
        storage = new CountryDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Country nt = storage.getCountry(CN_UUID);
        assertEquals(nt.getUuid(), CN_UUID);
        assertEquals(nt.getName(), CN_NAME);
        assertEquals(nt.getCodefips(), CODEFIPS);
    }

    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.geolocation.babblesnt.server.persistence.items.Country.CountryDAO#getAllCountries}.
     */
    //@Ignore
    @Test
    public void testgetAllCountries() {
        storage = new CountryDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        List<Country> list = storage.getAllCountries();

        //assertEquals(list.size(), 10);
        //System.out.println(list);
        for (Country l : list) {
            System.out.println(l);
           
        }
    }

}
