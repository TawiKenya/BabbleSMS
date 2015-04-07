package ke.co.tawi.babblesms.server.persistence.network;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.network.Country;
import static org.junit.Assert.*;

import org.junit.Test;

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
     * {@link ke.co.tawi.babblesms.server.persistence.items.Country#getCountry(java.lang.String)}.
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
     * {@link ke.co.tawi.babblesnt.server.persistence.items.Country.CountryDAO#getAllCountries}.
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

    /**
     * Test method for
     * {@link ke.co.tawi.babblesnt.server.persistence.items.Country#PutCountry(ke.co.tawi.babblesnt.server.beans.Country)}.
     *
     *
     *
     */
    @Test
    public void testPutCountry() {
        storage = new CountryDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Country nt = new Country();
        nt.setUuid(CN_UUID_NEW);
        nt.setName(CN_NAME_NEW);
        nt.setCodefips(CODEFIPS_NEW);

        assertTrue(storage.putCountry(nt));

        nt = storage.getCountry(CN_UUID_NEW);
        assertEquals(nt.getUuid(), CN_UUID_NEW);
        assertEquals(nt.getName(), CN_NAME_NEW);
        assertEquals(nt.getCodefips(), CODEFIPS_NEW);
    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesnt.server.persistence.items.Country#UpdateCountry(ke.co.tawi.babblesms.server.beans.Country)}.
     *
     *
     *
     */
    @Test
    public void testUpdateCountry() {
        storage = new CountryDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        
        assertTrue(storage.updateCountry(CN_UUID_NEW, CN_NAME_UPDATED,CODEFIPS_UPDATE));

        Country nt = storage.getCountry(CN_UUID_NEW);
        assertEquals(nt.getUuid(), CN_UUID_NEW);
        assertEquals(nt.getName(), CN_NAME_UPDATED);
        assertEquals(nt.getCodefips(), CODEFIPS_UPDATE);
    }

}
