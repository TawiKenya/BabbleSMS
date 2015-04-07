package ke.co.tawi.babblesms.server.persistence.items.purchaseHistory;

import java.util.Date;

import static org.junit.Assert.*;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.PurchaseHistory;

import org.junit.Test;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 * @version %I%, %G%
 */
public class TestPurchaseHistoryDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String NT_UUID = "1fd59dee-e992-40ee-8b69-6040ea0e0d39", NT_UUID_NEW = "f0034681d2e6-84f2-45ff-914c-522a3b076141";
    final String NT_SOURCE = "Sale",
            NT_SOURCE1 = "Info",
            NT_SOURCE2 = "impala",
            NT_SOURCE3 = "30017",
            NT_SOURCE4 = "10220",
            NT_SOURCE_NEW = "20272",
            NT_SOURCE_NEW2 = "Notice",
            NT_SOURCE_UPDATED="10220";

    final String NETWORKUUID = "B936DA83-8A45-E9F0-2EAE-D75F5C232E78",
            NETWORKUUID_NEW = "5C1D9939-136A-55DE-FD0E-61D8204E17C9";

    final String ACCUUID = "C937CE62-C4A9-131F-C96E-2DB8A9E886AB",
            ACCUUID_NEW = "C3CFA249-F2A3-8253-1F44-B1C594C6A8D2";

    final int AMOUNT = 5000,
            AMOUNT_NEW = 1000;

    final Date PURCHASE_DATE = new Date(new Long("1408265135000"));  // 2014-10-04 03:08:06 (yyyy-MM-dd HH:mm:ss)

    private PurchaseHistoryDAO storage;

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.beans.account.PurchaseHistory#getPurchaseHistory(java.lang.String)}.
     */
    @Test
    public void testgetPurchaseHistoryString() {
        storage = new PurchaseHistoryDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        PurchaseHistory nt = storage.getPurchaseHistory(NT_UUID);
        assertEquals(nt.getUuid(), NT_UUID);
        assertEquals(nt.getAmount(), AMOUNT);
        assertEquals(nt.getSource(), NT_SOURCE);
        assertEquals(nt.getAccountuuid(), ACCUUID);
        assertEquals(nt.getNetworkuuid(), NETWORKUUID);
        assertEquals(nt.getPurchasetime(), PURCHASE_DATE);

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.PurchaseHistoryDAO#getPurchaseHistoryByACCOUNT}.
     */
    //@Ignore
    @Test
    public void testgetPurchaseHistoryByACCOUNT() {
        storage = new PurchaseHistoryDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        List<PurchaseHistory> list = storage.getPurchaseHistoryByAccount(ACCUUID_NEW);

        //assertEquals(list.size(), 10);
        //System.out.println(list);
        for (PurchaseHistory l : list) {
            System.out.println(l);
            assertTrue(l.getSource().equals(NT_SOURCE)
                    || l.getSource().equals(NT_SOURCE1)
                    || l.getSource().equals(NT_SOURCE2)
                    || l.getSource().equals(NT_SOURCE3)
                    || l.getSource().equals(NT_SOURCE4)
                    || l.getSource().equals(NT_SOURCE_NEW)
                    || l.getSource().equals(NT_SOURCE_NEW2)
            );

        }
    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.beans.account.PurchaseHistory#PutPurchaseHistory(ke.co.tawi.babblesms.server.beans.account.PurchaseHistory)}.
     *
     *
     *
     */
    @Test
    public void testPutPurchaseHistory() {
        storage = new PurchaseHistoryDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        PurchaseHistory nt = new PurchaseHistory();
        nt.setUuid(NT_UUID_NEW);
        nt.setAmount(AMOUNT_NEW);
        nt.setSource(NT_SOURCE_NEW);
        nt.setAccountuuid(ACCUUID_NEW);
        nt.setNetworkuuid(NETWORKUUID_NEW);
        nt.setPurchasetime(PURCHASE_DATE);

        assertTrue(storage.putPurchaseHistory(nt));

        nt = storage.getPurchaseHistory(NT_UUID_NEW);
        assertEquals(nt.getUuid(), NT_UUID_NEW);
        assertEquals(nt.getAmount(), AMOUNT_NEW);
        assertEquals(nt.getSource(), NT_SOURCE_NEW);
        assertEquals(nt.getAccountuuid(), ACCUUID_NEW);
        assertEquals(nt.getNetworkuuid(), NETWORKUUID_NEW);
        assertEquals(nt.getPurchasetime(), PURCHASE_DATE);
    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.beans.account.PurchaseHistory#UpdatedPurchaseHistory(ke.co.tawi.babblesms.server.beans.account.PurchaseHistory)}.
     *
     *
     *
     */
    @Test
    public void testUpdatePurchaseHistory() {
        storage = new PurchaseHistoryDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        assertTrue(storage.updatePurchaseHistory(NT_UUID_NEW, NT_SOURCE_UPDATED));

        PurchaseHistory nt = storage.getPurchaseHistory(NT_UUID_NEW);
        assertEquals(nt.getUuid(), NT_UUID_NEW);
        assertEquals(nt.getAmount(), AMOUNT_NEW);
        assertEquals(nt.getSource(), NT_SOURCE_UPDATED);
        assertEquals(nt.getAccountuuid(), ACCUUID_NEW);
        assertEquals(nt.getNetworkuuid(), NETWORKUUID_NEW);
        assertEquals(nt.getPurchasetime(), PURCHASE_DATE);
    }
}
