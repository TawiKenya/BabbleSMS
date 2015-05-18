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

import static org.junit.Assert.*;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;

import org.junit.Test;

/**
 * Tests our persistence implementation for {@link Shortcode}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestShortcodeDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String SH_UUID = "6C8275C2-8FE7-E3AD-6873-8384C41D395F", SH_UUID_NEW = "fU681d2e6-84f2-45ff-914c-522a3b076141UKLW";
    final String SH_SOURCE = "20240",
            SH_SOURCE2 = "10220",
            SH_SOURCE3 = "20272",
            SH_SOURCE4 = "30017",
            SH_SOURCE_NEW = "234322",
            SH_SOURCE_UPDATE = "12345";

    final String ACCOUNT = "C3CFA249-F2A3-8253-1F44-B1C594C6A8D2",
            ACCOUNT1 = "650195B6-9357-C147-C24E-7FBDAEEC74ED",
            ACCOUNT_UPDATE = "8038D870-5455-A2D6-18A9-BD5FA1D0A10A";
            

    final String NETWORK_UUID = "B936DA83-8A45-E9F0-2EAE-D75F5C232E78",
            NETWORK_UUID_NEW = "5C1D9939-136A-55DE-FD0E-61D8204E17C9",
            NETWORK_UUID_UPDATE = "0DE968C9-7309-C481-58F7-AB6CDB1011EF";

    private ShortcodeDAO storage;

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.shedit#GetShortcodeByuuidString(java.lang.String)}.
     */
    @Test
    public void testGetShortcodeByuuidString() {
        storage = new ShortcodeDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Shortcode sh = storage.getShortcode(SH_UUID);

        assertEquals(sh.getUuid(), SH_UUID);
        assertEquals(sh.getCodenumber(), SH_SOURCE);
        assertEquals(sh.getAccountuuid(), ACCOUNT1);
        assertEquals(sh.getNetworkuuid(), NETWORK_UUID);

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.maskcode.Shortcode#ShortcodeByAccount}.
     */
    //@Ignore
    @Test
    public void testShortcodeByAccount() {
        storage = new ShortcodeDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        List<Shortcode> list = storage.getShortcodebyaccountuuid(ACCOUNT);

        //assertEquals(list.size(), 10);
        System.out.println(list);
        for (Shortcode l : list) {
            //System.out.println(l);
            assertTrue(l.getCodenumber().equals(SH_SOURCE2)
                    || l.getCodenumber().equals(SH_SOURCE3)
                    || l.getCodenumber().equals(SH_SOURCE4));
        }
    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.maskcode#PutShortcode(ke.co.tawi.babblesms.server.beans.maskcode.Shortcode)}.
     */
    @Test
    public void testPutShortcode() {
        storage = new ShortcodeDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Shortcode sh = new Shortcode();
        sh.setUuid(SH_UUID_NEW);
        sh.setCodenumber(SH_SOURCE_NEW);
        sh.setNetworkuuid(NETWORK_UUID_NEW);
        sh.setAccountuuid(ACCOUNT);

        assertTrue(storage.putShortcode(sh));

        sh = storage.getShortcode(SH_UUID_NEW);

        assertEquals(sh.getUuid(), SH_UUID_NEW);
        assertEquals(sh.getCodenumber(), SH_SOURCE_NEW);
        assertEquals(sh.getAccountuuid(), ACCOUNT);
        assertEquals(sh.getNetworkuuid(), NETWORK_UUID_NEW);

    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.maskcode#UpdateShortcode(ke.co.tawi.babblesms.server.beans.maskcode.Shortcode)}.
     */
    @Test
    public void testUpdateShortcode() {
        storage = new ShortcodeDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Shortcode sh = new Shortcode();
        sh.setUuid(SH_UUID_NEW);
        sh.setCodenumber(SH_SOURCE_UPDATE);
        sh.setNetworkuuid(NETWORK_UUID_UPDATE);
        sh.setAccountuuid(ACCOUNT_UPDATE);

        assertTrue(storage.updateShortcode(sh));

        sh = storage.getShortcode(SH_UUID_NEW);

        assertEquals(sh.getUuid(), SH_UUID_NEW);
        assertEquals(sh.getCodenumber(), SH_SOURCE_UPDATE);
        assertEquals(sh.getAccountuuid(), ACCOUNT_UPDATE);
        assertEquals(sh.getNetworkuuid(), NETWORK_UUID_UPDATE);

    }
}
