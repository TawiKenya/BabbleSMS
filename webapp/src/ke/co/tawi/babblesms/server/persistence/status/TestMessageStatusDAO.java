package ke.co.tawi.babblesms.server.persistence.status;

import static org.junit.Assert.*;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.messagetemplate.MsgStatus;

import org.junit.Test;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 * @version %I%, %G%
 */
public class TestMessageStatusDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String MS_UUID = "BBA890E6-FDA9-77B0-03B7-1F6B000C85DD", MS_UUID_NEW = "f9k681d2e6-84f2-45ff-914c-522a3b076141";

    final String DESCRIPTION = "Received",
            DESCRIPTION1 = "Sent",
            DESCRIPTION2 = "Pending",
            DESCRIPTION3 = "Delivered",
            DESCRIPTION_NEW = "transit",
            DESCRIPTION_UPDATE = "updated Status";

    private MessageStatusDAO storage;

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.status#getMessageStatus(java.lang.String)}.
     */
    @Test
    public void testgetMessageStatusString() {
        storage = new MessageStatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        MsgStatus ms = storage.getMessageStatus(MS_UUID);
        assertEquals(ms.getUuid(), MS_UUID);
        assertEquals(ms.getDescription(), DESCRIPTION);

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.status.MessageStatusDAO#getMessageStatusByACC}.
     */
    //@Ignore
    @Test
    public void testgetMessageStatusByACC() {
        storage = new MessageStatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        List<MsgStatus> list = storage.getAllMessageStatus();

        //assertEquals(list.size(), 10);
        //System.out.primsln(list);
        for (MsgStatus l : list) {
            System.out.println(l);
            assertTrue(l.getDescription().equals(DESCRIPTION)
                    || l.getDescription().equals(DESCRIPTION1)
                    || l.getDescription().equals(DESCRIPTION2)
                    || l.getDescription().equals(DESCRIPTION3));

        }
    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.status.MessageStatus#PutMessageStatus(ke.co.tawi.babblesms.server.beans.status.MessageStatus)}.
     *
     *
     *
     */
    @Test
    public void testPutMessageStatus() {
        storage = new MessageStatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        MsgStatus ms = new MsgStatus();
        ms.setUuid(MS_UUID_NEW);
        ms.setDescription(DESCRIPTION_NEW);

        assertTrue(storage.putMessageStatus(ms));

        ms = storage.getMessageStatus(MS_UUID_NEW);
        assertEquals(ms.getUuid(), MS_UUID_NEW);
        assertEquals(ms.getDescription(), DESCRIPTION_NEW);
    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.status.MessageStatus#UpdateMessageStatus(ke.co.tawi.babblesms.server.beans.status.MessageStatus)}.
     *
     *
     *
     */
    @Test
    public void testUpdateMessageStatus() {
        storage = new MessageStatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        assertTrue(storage.updateMessageStatus(MS_UUID_NEW, DESCRIPTION_UPDATE));

        MsgStatus ms = storage.getMessageStatus(MS_UUID_NEW);
        assertEquals(ms.getUuid(), MS_UUID_NEW);
        assertEquals(ms.getDescription(), DESCRIPTION_UPDATE);
    }

}
