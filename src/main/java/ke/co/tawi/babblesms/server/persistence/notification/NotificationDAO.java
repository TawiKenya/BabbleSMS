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
package ke.co.tawi.babblesms.server.persistence.notification;

import ke.co.tawi.babblesms.server.beans.notification.Notification;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Copyright (c) Tawi Commercial Services Ltd., July 30, 2014
 *
 * @author <a href="mailto:erickm@tawi.mobi.co.ke">Erick Murimi</a>
 */
public class NotificationDAO extends GenericDAO implements
        BabbleNotificationDAO {

    private static NotificationDAO notificationDAO;
    private Logger logger = LogManager.getLogger(this.getClass());;

    /**
     * Provides the singleton for the {@link  Notification}
     * Data Access Object
     *
     * @return	a data access object
     */
    public static NotificationDAO getInstance() {
        if (notificationDAO == null) {
            notificationDAO = new NotificationDAO();
        }
        return notificationDAO;
    }

    
    /**
     *
     */
    protected NotificationDAO() {
        super();
    }

    
    /**
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPassword
     * @param dbPort
     */
    public NotificationDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
    }

    
    /**
     * 
     * @param notification
     * @return whether the action was successful or not
     */
    @Override
    public boolean putNotification(Notification notification) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO Notification (Uuid, origin, ShortDesc, LongDesc) "
                    + "VALUES (?,?,?,?);");
            pstmt.setString(1, notification.getUuid());
            pstmt.setString(2, notification.getOrigin());
            pstmt.setString(3, notification.getShortDesc());
            pstmt.setString(4, notification.getLongDesc());

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put: " + notification);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return success;
    }

    
    /**
     * 
     * @param origin
     * @return 
     */
    @Override
    public List<Notification> getNotificationbyOrigin(String origin) {
        List<Notification> notification = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Notification WHERE origin = ?;");
            pstmt.setString(1, origin);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                notification = b.toBeanList(rset, Notification.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to get Notification with Uuid: " + origin);
            logger.error(ExceptionUtils.getStackTrace(e));
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException e) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return notification;
    }

   
    
    /**
     *
     */
    @Override
    public List<Notification> getAllNotifications() {
        List<Notification> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Notification ORDER BY NotificationDate DESC;");

            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, Notification.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all notifications.");
            logger.error(ExceptionUtils.getStackTrace(e));
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException e) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return list;
    }

    /**
     *
     */
    @Override
    public boolean editLongDescription(String newLongDescription, String uuid) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE Notification SET LongDesc = ? WHERE Uuid = ?;");
            pstmt.setString(1, newLongDescription);
            pstmt.setString(2, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when editing the long description to " + newLongDescription
                    + " for the notification with uuid " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return success;
    }

    /**
     * 
     * @param newShortDescription
     * @param uuid
     * @return 
     */
    @Override
    public boolean editShortDescription(String newShortDescription, String uuid) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE Notification SET ShortDesc = ? WHERE Uuid = ?;");
            pstmt.setString(1, newShortDescription);
            pstmt.setString(2, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when editing the short description to " + newShortDescription
                    + " for the notification with uuid " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return success;
    }

    /**
     *
     */
    @Override
    public boolean deleteNotification(String uuid) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM Notification WHERE Uuid = ?;");
            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting notification with uuid " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return success;
    }

   /**
     *
     */
    @Override
    public Notification getNotification(String uuid) {
        Notification notification = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM notification WHERE Uuid = ?;");
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                notification = b.toBean(rset, Notification.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting network with uuid: " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException e) {
                }
            }
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return notification;
    }
    
    
    /**
     * 
     * @param notification
     * @return 
     */
    @Override
    public boolean updateNotification(Notification notification) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE Notification SET origin=?,ShortDesc=?,LongDesc=?,published=? WHERE uuid=?");
            
            pstmt.setString(1, notification.getOrigin());
            pstmt.setString(2, notification.getShortDesc());
            pstmt.setString(3, notification.getLongDesc());
            pstmt.setString(4, notification.getPublished());
            pstmt.setString(5, notification.getUuid());

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to putupdate Notification: " + notification);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } finally {
            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }
            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }
        return success;
    }


}
