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

import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.persistence.items.maskcode.MaskDAO;

import org.junit.Test;

/**
 * Tests our persistence implementation for {@link Mask}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestMaskDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String MS_UUID = "69243408-AAEF-B125-2AA9-FA6F49207C41", MS_UUID_NEW = "fwUIk681d2e6-84f2-45ff-914c-522a3b076141";
    final String MS_MASK = "offer",
            MS_MASK2 = "info",
            MS_MASK3 = "impala",
            MS_MASK_NEW = "NEW MASK",
            MS_MASK_UPDATE = "UPDATEDMASK";

    final String NETWORKUUID = "741AC38C-3E40-6887-9CD6-365EF9EA1EF0",
            NETWORKUUID_NEW = "B936DA83-8A45-E9F0-2EAE-D75F5C232E78",
            NETWORKUUID_UPDATED = "0DE968C9-7309-C481-58F7-AB6CDB1011EF";

    final String MSUUID = "650195B6-9357-C147-C24E-7FBDAEEC74ED",
            MSUUID_NEW = "C3CFA249-F2A3-8253-1F44-B1C594C6A8D2",
            MSUUID_UPDATED = "8038D870-5455-A2D6-18A9-BD5FA1D0A10A";

    private MaskDAO storage;

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.Maskcode.Mask#getMask(java.lang.String)}.
     */
    @Test
    public void testgetMaskString() {
        storage = new MaskDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Mask ms = storage.getMask(MS_UUID);
        assertEquals(ms.getUuid(), MS_UUID);
        assertEquals(ms.getMaskname(), MS_MASK);
        assertEquals(ms.getNetworkuuid(), NETWORKUUID);
        assertEquals(ms.getAccountuuid(), MSUUID);

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.Maskcode.MASKDAO#getMaskByACCOUNT}.
     */
    //@Ignore
    @Test
    public void testgetMaskByACCOUNT() {
        storage = new MaskDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        List<Mask> list = storage.getmaskbyaccount(MSUUID_NEW);

        //assertEquals(list.size(), 10);
        //System.out.println(list);
        for (Mask l : list) {
            System.out.println(l);
            assertTrue(l.getMaskname().equals(MS_MASK2)
                    || l.getMaskname().equals(MS_MASK3));
        }
    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.Maskcode.Mask#PutMask(ke.co.tawi.babblesms.server.beans.Maskcode.Mask)}.
     *
     *
     *
     */
    @Test
    public void testPutMask() {
        storage = new MaskDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Mask ms = new Mask();
        ms.setUuid(MS_UUID_NEW);
        ms.setMaskname(MS_MASK_NEW);
        ms.setNetworkuuid(NETWORKUUID_NEW);
        ms.setAccountuuid(MSUUID);

        assertTrue(storage.putMask(ms));

        ms = storage.getMask(MS_UUID_NEW);
        assertEquals(ms.getUuid(), MS_UUID_NEW);
        assertEquals(ms.getMaskname(), MS_MASK_NEW);
        assertEquals(ms.getNetworkuuid(), NETWORKUUID_NEW);
        assertEquals(ms.getAccountuuid(), MSUUID);
    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.Maskcode.Mask#UpdateMask(ke.co.tawi.babblesms.server.beans.Maskcode.Mask)}.
     *
     *
     *
     */
    @Test
    public void testUpdateMask() {
        storage = new MaskDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Mask ms = new Mask();
        ms.setUuid(MS_UUID_NEW);
        ms.setMaskname(MS_MASK_UPDATE);
        ms.setNetworkuuid(NETWORKUUID_UPDATED);
        ms.setAccountuuid(MSUUID_UPDATED);

        assertTrue(storage.updateMask(ms));

        ms = storage.getMask(MS_UUID_NEW);
        assertEquals(ms.getUuid(), MS_UUID_NEW);
        assertEquals(ms.getMaskname(), MS_MASK_UPDATE);
        assertEquals(ms.getNetworkuuid(), NETWORKUUID_UPDATED);
        assertEquals(ms.getAccountuuid(), MSUUID_UPDATED);
    }
}
