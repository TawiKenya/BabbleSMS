package ke.co.tawi.babblesms.server.persistence.notification;

import java.util.Date;
import static org.junit.Assert.*;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.notification.Notification;

import org.junit.Test;

/**
 * Copyright (c) Tawi Commercial Services Ltd., July 30, 2014
 *
 * @author <a href="mailto:erickm@tawi.mobi.co.ke">Erick Murimi</a>
 * @version %I%, %G%
 */
public class TestNotificationDAO {

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String MS_UUID = "9cefc293-7ac2-4b20-8d45-d3c359cc0493", MS_UUID_NEW = "fu12k681d2e6-84f2-45ff-914c-522a3b076141";

    final String ORIGIN = "system",
                 ORIGIN_NEW = "admin";

    final String SHORTDESC = "Downgrade of feature:",
            SHORTDESC_NEW = "new short dec";
    
    final String PUBLISH = "yes",
            PUBLISH_NEW = "no";

    final String LONGDESC = "Unfortunately we shall have to downgrade the account management.",
            LONGDESC_NEW = "NEW long desc",
            LONGDESC_UPDATED = "UPDATED long desc";

    final Date LOG_DATE = new Date(new Long("1410709309000"));  // 2014-10-04 03:08:06 (yyyy-MM-dd HH:mm:ss)

    private NotificationDAO storage;

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.Notification#getNotification(java.lang.String)}.
     */
    @Test
    public void testgetNotificationString() {
        storage = new NotificationDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Notification ms = storage.getNotification(MS_UUID);
        assertEquals(ms.getUuid(), MS_UUID);
        assertEquals(ms.getOrigin(), ORIGIN);
        assertEquals(ms.getShortDesc(), SHORTDESC);
        assertEquals(ms.getLongDesc(), LONGDESC);
        assertEquals(ms.getNotificationDate(), LOG_DATE);

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.notification.NotificationDAO#getALLNotification}.
     */
    //@Ignore
    @Test
    public void testgetALLNotification() {
        storage = new NotificationDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        List<Notification> list = storage.getAllNotifications();

        //assertEquals(list.size(), 10);
        //System.out.primsln(list);
        for (Notification l : list) {
            System.out.println(l);
            assertTrue(l.getOrigin().equals(ORIGIN)
                    || l.getOrigin().equals(ORIGIN_NEW));

        }
    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.notification.Notification#PutNotification(ke.co.tawi.babblesms.server.beans.notification.Notification)}.
     *
     *
     *
     */
    @Test
    public void testPutNotification() {
        storage = new NotificationDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Notification ms = new Notification();
        ms.setUuid(MS_UUID_NEW);
        ms.setOrigin(ORIGIN_NEW);
        ms.setShortDesc(SHORTDESC_NEW);
        ms.setLongDesc(LONGDESC_NEW);

        assertTrue(storage.putNotification(ms));

        ms = storage.getNotification(MS_UUID_NEW);
        assertEquals(ms.getUuid(), MS_UUID_NEW);
        assertEquals(ms.getOrigin(), ORIGIN_NEW);
        assertEquals(ms.getShortDesc(), SHORTDESC_NEW);
        assertEquals(ms.getLongDesc(), LONGDESC_NEW);
        

    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.notification.Notification#UpdateNotification(ke.co.tawi.babblesms.server.beans.notification.Notification)}.
     *
     *
     *
     */
    @Test
    public void testUpdateNotification() {
        storage = new NotificationDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
         
        Notification ms = new Notification();
        ms.setUuid(MS_UUID_NEW);
        ms.setOrigin(ORIGIN_NEW);
        ms.setShortDesc(SHORTDESC_NEW);
        ms.setLongDesc(LONGDESC_NEW);
        ms.setPublished(PUBLISH_NEW);
        
        assertTrue(storage.updateNotification(ms));

        ms = storage.getNotification(MS_UUID_NEW);
        assertEquals(ms.getUuid(), MS_UUID_NEW);
        assertEquals(ms.getOrigin(), ORIGIN_NEW);
        assertEquals(ms.getShortDesc(), SHORTDESC_NEW);
        assertEquals(ms.getLongDesc(), LONGDESC_NEW);
        assertEquals(ms.getPublished(), PUBLISH_NEW);

    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.notification.Notification#UpdateNotification(ke.co.tawi.babblesms.server.beans.notification.Notification)}.
     *
     *
     *
     */
    @Test
    public void testUpdateNotificationlongdesc() {
        storage = new NotificationDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        assertTrue(storage.editLongDescription(LONGDESC_UPDATED, MS_UUID_NEW));

        Notification ms = storage.getNotification(MS_UUID_NEW);
        assertEquals(ms.getUuid(), MS_UUID_NEW);
        assertEquals(ms.getOrigin(), ORIGIN_NEW);
        assertEquals(ms.getShortDesc(), SHORTDESC_NEW);
        assertEquals(ms.getLongDesc(), LONGDESC_UPDATED);

    }


}
