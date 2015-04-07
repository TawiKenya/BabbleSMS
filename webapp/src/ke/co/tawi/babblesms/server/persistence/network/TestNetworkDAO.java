package ke.co.tawi.babblesms.server.persistence.network;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.network.Network;
import static org.junit.Assert.*;

import org.junit.Test;

public class TestNetworkDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String NT_UUID = "B936DA83-8A45-E9F0-2EAE-D75F5C232E78", NT_UUID_NEW = "fp769681d2e6-84f2-45ff-914c-522a3b076141";
    final String NT_NAME = "Safaricom KE",
            NT_NAME_NEW = "Safaricom TZ",
            NT_NAME_UPDATED="Safaricom UG";

    final String COUNTRYUUID = "57e64152-9e8d-4c45-abbc-67a0331aa46e",
            COUNTRYUUID_NEW = "9851f026-da6a-49b8-9d76-15d34950c5a2";

    private NetworkDAO storage;

    /**
     * Test method for
     * {@link ke.co.tawi.babblesnt.server.persistence.items.Network#getNetwork(java.lang.String)}.
     */
    @Test
    public void testgetNetworkString() {
        storage = new NetworkDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Network nt = storage.getNetwork(NT_UUID);
        assertEquals(nt.getUuid(), NT_UUID);
        assertEquals(nt.getName(), NT_NAME);
        assertEquals(nt.getCountryuuid(), COUNTRYUUID);

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesnt.server.persistence.items.Network.NetworkDAO#getNetworkByACCOUNT}.
     */
    //@Ignore
    @Test
    public void testgetMaskByACCOUNT() {
        storage = new NetworkDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        List<Network> list = storage.getAllNetworks();

        //assertEquals(list.size(), 10);
        //System.out.println(list);
        for (Network l : list) {
            System.out.println(l);
            assertTrue(l.getCountryuuid().equals(COUNTRYUUID)
                       || l.getCountryuuid().equals(COUNTRYUUID_NEW));

        }
    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesnt.server.persistence.items.Network#PutNetwork(ke.co.tawi.babblesnt.server.beans.Network)}.
     *
     *
     *
     */
    @Test
    public void testPutNetwork() {
        storage = new NetworkDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Network nt = new Network();
        nt.setUuid(NT_UUID_NEW);
        nt.setName(NT_NAME_NEW);
        nt.setCountryuuid(COUNTRYUUID_NEW);

        assertTrue(storage.putNetwork(nt));

        nt = storage.getNetwork(NT_UUID_NEW);
        assertEquals(nt.getUuid(), NT_UUID_NEW);
        assertEquals(nt.getName(), NT_NAME_NEW);
        assertEquals(nt.getCountryuuid(), COUNTRYUUID_NEW);
    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesnt.server.persistence.items.Network#UpdateNetwork(ke.co.tawi.babblesms.server.beans.Network)}.
     *
     *
     *
     */
    @Test
    public void testUpdateNetwork() {
        storage = new NetworkDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        
        assertTrue(storage.updateNetwork(NT_UUID_NEW, NT_NAME_UPDATED));

        Network nt = storage.getNetwork(NT_UUID_NEW);
        assertEquals(nt.getUuid(), NT_UUID_NEW);
        assertEquals(nt.getName(), NT_NAME_UPDATED);
        assertEquals(nt.getCountryuuid(), COUNTRYUUID_NEW);
    }

}
