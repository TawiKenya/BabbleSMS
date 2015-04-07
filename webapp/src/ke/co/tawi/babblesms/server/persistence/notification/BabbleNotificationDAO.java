package ke.co.tawi.babblesms.server.persistence.notification;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.notification.Notification;

/**
 * Copyright (c) Tawi Commercial Services Ltd., July 30, 2014
 *
 * @author <a href="mailto:erickm@tawi.mobi">Erick Murimi</a>
 * @version %I%, %G%
 */
public interface BabbleNotificationDAO {

    /**
     * Adds a new notification to the RDBMS
     *
     * @param notification
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putNotification(Notification notification);

    
    
    /**
     *
     * @return	a list of notifications
     */
    public List<Notification> getAllNotifications();

    /**
     *
     * @param newLongDescription
     * @param uuid
     * @return		<code>true</code> if editing is successful; <code>false</code>
     * for otherwise
     */
    public boolean editLongDescription(String newLongDescription, String uuid);

    /**
     *
     * @param newShortDescription
     * @param uuid
     * @return		<code>true</code> if editing is successful; <code>false</code>
     * for otherwise
     */
    public boolean editShortDescription(String newShortDescription, String uuid);

    /**
     *
     * @param uuid
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    public boolean deleteNotification(String uuid);
    
    /**
     *
     * @param uuid
     * @return		<code>true</code> if selected; <code>false</code> for
     * otherwise
     */
    public Notification getNotification(String uuid);
    
    
    /**
     *
     * @param origin
     * @return		<code>true</code> if selected; <code>false</code> for
     * otherwise
     */
    public List<Notification> getNotificationbyOrigin(String origin);
    
    /**
     *
     * @param notification
     * @return		<code>true</code> if selected; <code>false</code> for
     * otherwise
     */
    
    public boolean updateNotification(Notification notification);

    
}
