package ke.co.tawi.babblesms.server.persistence.notification;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.notification.NotificationStatus;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Nov 27, 2013
 *
 * @author <a href="mailto:erickm@tawi.mobi">Erick Murimi</a>
 * @version %I%, %G%
 */
public interface BabbleNotificationStatusDAO {

    /**
     * Adds a new notification status to the RDBMS
     *
     * @param notificationStatus
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putNotificationStatus(NotificationStatus notificationStatus);

    /**
     *
     * @param uuid
     * @return	a notification status
     */
    public NotificationStatus getNotificationStatus(String uuid);

    /**
     *
     * @return	a list of notification status
     */
    public List<NotificationStatus> getAllNotificationStatus();

    /**
     *
     * @param accountUuid
     * @return	a list of notification status
     */
    public List<NotificationStatus> getAllNotificationStatusByAccount(String accountUuid);

    /**
     *
     * @param notificationUuid
     * @param accountUuid
     * @return		<code>true</code> if set to true; <code>false</code> for
     * otherwise
     */
    public boolean getReadFlag(String notificationUuid, String accountUuid);

    /**
     * Sets the read flag to true and the date at the time of the update
     * <br>
     * This signifies that a specific notification has been read by a user.
     *
     * @param notificationUuid
     * @param accountUuid
     * @return		<code>true</code> if update is successful; <code>false</code> for
     * otherwise
     */
    public boolean updateReadFlag(String notificationUuid, String accountUuid);

    /**
     * Provides a list of notification Uuids for notifications that have not
     * been read for a specified account
     *
     * @param accountUuid
     * @return	a list of Uuids
     */
    public List<String> getUnreadNotificationStatusByAccount(String accountUuid);
    
    /**
     * deletes notification.
     *
     * @param uuid
     * @return	a list of Uuids
     */
    public boolean deleteNotificationStatus(String uuid);

}
