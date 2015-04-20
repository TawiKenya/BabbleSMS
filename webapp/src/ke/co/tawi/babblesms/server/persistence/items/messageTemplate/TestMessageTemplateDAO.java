package ke.co.tawi.babblesms.server.persistence.items.messageTemplate;

import static org.junit.Assert.*;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.messagetemplate.MessageTemplate;
import ke.co.tawi.babblesms.server.persistence.items.messageTemplate.MessageTemplateDAO;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@mtujaa.co.ke">Japheth Korir</a>
 * @version %I%, %G%
 */

public class TestMessageTemplateDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String MT_UUID = "5b8d3eba-f90c-4993-b45a-0ce430f921eb", MT_UUID_NEW = "f0012y681d2e6-84f2-45ff-914c-522a3b076141";
    final String MT_TITLE = "supermotility", MT_TITLE_NEW = "Metrostate" ,MT_TITLE_UPDATE = "UPDATED title";

    final String CONTENT = "sent by the browser that asks for the data only",
            CONTENT_NEW = "This is a new template contents",
            CONTENT_UPDATED = "This is the updated template contents";

    final String MTUUID = "C937CE62-C4A9-131F-C96E-2DB8A9E886AB",
            MTUUID_NEW = "C937CE62-C4A9-131F-C96E-2DB8A9E886AB";

    private MessageTemplateDAO storage;

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.MessageTemplate#getMessageTemplate(java.lang.String)}.
     */
    @Test
    public void testMessageTemplateString() {
        storage = new MessageTemplateDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        MessageTemplate mt = storage.getMessageTemplate(MT_UUID);
        assertEquals(mt.getUuid(), MT_UUID);
        assertEquals(mt.getTitle(), MT_TITLE);
        assertEquals(mt.getContents(), CONTENT);
        assertEquals(mt.getAccountuuid(), MTUUID);

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.MessageTemplateDAO#GetMessageTemplateByACCOUNT}.
     */
    //@Ignore
    @Test
    public void testGetMessageTemplateByACCOUNT() {
        storage = new MessageTemplateDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        List<MessageTemplate> list = storage.getAllMessageTemplatesbyuuid(MTUUID);

        //assertEquals(list.size(), 10);
        //System.out.println(list);
        for (MessageTemplate l : list) {
            System.out.println(l);
            assertTrue(l.getAccountuuid().equals(MTUUID));
        }
    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.MessageTemplate#PutMessageTemplate(ke.co.tawi.babblesms.server.beans.MessageTemplate)}.
     *
     *
     *
     */
    @Test
    public void testPutMessageTemplate() {
        storage = new MessageTemplateDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        MessageTemplate mt = new MessageTemplate();
        mt.setUuid(MT_UUID_NEW);
        mt.setTitle(MT_TITLE_NEW);
        mt.setContents(CONTENT_NEW);
        mt.setAccountuuid(MTUUID_NEW);

        assertTrue(storage.putMessageTemplate(mt));

        mt = storage.getMessageTemplate(MT_UUID_NEW);
        assertEquals(mt.getUuid(), MT_UUID_NEW);
        assertEquals(mt.getTitle(), MT_TITLE_NEW);
        assertEquals(mt.getContents(), CONTENT_NEW);
        assertEquals(mt.getAccountuuid(), MTUUID_NEW);
    }
   /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.MessageTemplate#UpdateMessageTemplate(ke.co.tawi.babblesms.server.beans.MessageTemplate)}.
     *
     *
     *
     */
    @Ignore
    @Test
    public void testUpdateMessageTemplate() {
        storage = new MessageTemplateDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        
        /* assertTrue(storage.updateMessageTemplate(MT_UUID_NEW, MT_TITLE_UPDATE, CONTENT_UPDATED));

        MessageTemplate mt = storage.getMessageTemplate(MT_UUID_NEW);
        assertEquals(mt.getUuid(), MT_UUID_NEW);
        assertEquals(mt.getTitle(), MT_TITLE_UPDATE);
        assertEquals(mt.getContents(), CONTENT_UPDATED);
        assertEquals(mt.getAccountuuid(), MTUUID_NEW);*/
    }
}
