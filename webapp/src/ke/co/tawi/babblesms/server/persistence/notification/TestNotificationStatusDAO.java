package ke.co.tawi.babblesms.server.persistence.notification;


import static org.junit.Assert.*;

import java.util.List;



import ke.co.tawi.babblesms.server.beans.notification.NotificationStatus;


import org.junit.Test;

/**
 * Copyright (c) Tawi Commercial Services Ltd., July 30, 2014
 *
 * @author <a href="mailto:erickm@tawi.mobi">Erick Murimi</a>
 * @version %I%, %G%
 */
public class TestNotificationStatusDAO  {

    final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;
	
	final String MS_UUID = "80b836c3-69ae-48e1-a6f6-eb0d7fffd104", MS_UUID_NEW = "f2k681d2e6-84f2-45ff-914c-522a3b076141";

    
    final String NOTUUID = "8b71a568-f95d-4c3e-8b5c-700a155c35a0",
            NOTUUID_NEW = "71098b2f-58d5-4ec8-9940-e5c531158252";
    
    final String ACCUUID="650195B6-9357-C147-C24E-7FBDAEEC74ED";

    final Boolean RFLAG = false;
            

    

    

    private NotificationStatusDAO storage;

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.NotificationStatus#getNotificationStatus(java.lang.String)}.
     */
    @Test
    public void testgetNotificationStatusString() {
        storage = new NotificationStatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        NotificationStatus ms = storage.getNotificationStatus(MS_UUID);
        assertEquals(ms.getUuid(), MS_UUID);
        assertEquals(ms.getNotificationUuid(), NOTUUID);
        assertEquals(ms.isReadFlag(), RFLAG);
        

    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.notification.NotificationStatusDAO#getALLNotificationStatus}.
     */
    //@Ignore
    @Test
    public void testgetALLNotificationStatus() {
        storage = new NotificationStatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        List<NotificationStatus> list = storage.getAllNotificationStatus();

        //assertEquals(list.size(), 10);
        System.out.println(list);
        for (NotificationStatus l : list) {
            System.out.println(l);
            assertTrue(!l.isReadFlag());
                    

        }
    }

    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.notification.NotificationStatus#PutNotificationStatus(ke.co.tawi.babblesms.server.beans.notification.NotificationStatus)}.
     *
     *
     *
     */
    @Test
    public void testPutNotificationStatus() {
        storage = new NotificationStatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        NotificationStatus ms = new NotificationStatus();
        ms.setUuid(MS_UUID_NEW);
        ms.setNotificationUuid(NOTUUID_NEW);
                

        assertTrue(storage.putNotificationStatus(ms));

        ms = storage.getNotificationStatus(MS_UUID_NEW);
        assertEquals(ms.getUuid(), MS_UUID_NEW);
        assertEquals(ms.getNotificationUuid(), NOTUUID_NEW);
        

    }
    
    /**
     * Test method for
     * {@link ke.co.tawi.babblesms.server.persistence.items.notification.NotificationStatus#UpdateNotificationStatus(ke.co.tawi.babblesms.server.beans.notification.NotificationStatus)}.
     *
     *
     *
     */
    @Test
    public void testUpdateNotificationStatus() {
        storage = new NotificationStatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

                
        assertTrue(storage.updateReadFlag(NOTUUID_NEW, ACCUUID));

        NotificationStatus ms = storage.getNotificationStatus(MS_UUID_NEW);
        assertEquals(ms.getUuid(), MS_UUID_NEW);
        assertEquals(ms.getNotificationUuid(), NOTUUID_NEW);
        

    }

}
