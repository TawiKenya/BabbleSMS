package ke.co.tawi.babblesms.server.persistence.notification;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import ke.co.tawi.babblesms.server.beans.notification.NotificationStatus;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

/**
 * Copyright (c) Tawi Commercial Services Ltd., July 30, 2014
 *
 * @author <a href="mailto:erickm@tawi.mobi">Erick Murimi</a>
 * @version %I%, %G%
 */
public class NotificationStatusDAO extends GenericDAO implements BabbleNotificationStatusDAO {

    private static NotificationStatusDAO notificationStatusDAO;
    private Logger logger = Logger.getLogger(this.getClass());

    /**
     *
     * @return
     */
    public static NotificationStatusDAO getInstance() {
        if (notificationStatusDAO == null) {
            notificationStatusDAO = new NotificationStatusDAO();
        }
        return notificationStatusDAO;
    }

    /**
     *
     */
    protected NotificationStatusDAO() {
        super();
    }

    /**
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPassword
     * @param dbPort
     */
    public NotificationStatusDAO(String dbName, String dbHost,
            String dbUsername, String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
    }

    @Override
    public boolean putNotificationStatus(NotificationStatus notificationStatus) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO NotificationStatus (Uuid, NotificationUuid) VALUES (?,?);");
            pstmt.setString(1, notificationStatus.getUuid());
            pstmt.setString(2, notificationStatus.getNotificationUuid());
            

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put NotificationStatus: " + notificationStatus);
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

    @Override
    public NotificationStatus getNotificationStatus(String uuid) {
        NotificationStatus notificationStatus = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM NotificationStatus WHERE Uuid = ?;");
            pstmt.setString(1, uuid);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                notificationStatus = b.toBean(rset, NotificationStatus.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to get NotificationStatus with Uuid: " + uuid);
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
        return notificationStatus;
    }

    @Override
    public List<NotificationStatus> getAllNotificationStatus() {
        List<NotificationStatus> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM NotificationStatus;");

            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, NotificationStatus.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all notification status.");
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

    @Override
    public List<NotificationStatus> getAllNotificationStatusByAccount(String accountUuid) {
        List<NotificationStatus> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM NotificationStatus WHERE notificationUuid IN (SELECT uuid FROM notification WHERE accountuuid=?);");
            pstmt.setString(1, accountUuid);

            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, NotificationStatus.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all notification status that belonging to the account "
                    + "uuid " + accountUuid);
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

    @Override
    public boolean getReadFlag(String notificationUuid, String accountUuid) {
        boolean status = false;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM NotificationStatus WHERE NotificationUuid IN(SELECT uuid FROM Notification WHERE uuid=? AND AccountUuid =?) AND readflag='Y';");
            pstmt.setString(1, notificationUuid);
            pstmt.setString(2, accountUuid);

            rset = pstmt.executeQuery();

            if (rset.next()) {
                status =true;
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting the read flag for notification with uuid: "
                    + notificationUuid + " and account with uuid: " + accountUuid);
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
        return status;
    }

    @Override
    public boolean updateReadFlag(String notificationUuid, String accountUuid) {
        boolean status = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE NotificationStatus SET ReadFlag ='Y', ReadDate =? WHERE NotificationUuid IN(SELECT uuid FROM Notification WHERE uuid=? AND AccountUuid =?);");
            //pstmt.setBoolean(1, true);
            pstmt.setTimestamp(1, new Timestamp(new Date().getTime()));
            pstmt.setString(2, notificationUuid);
            pstmt.setString(3, accountUuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when updating the read flag to true for notification with uuid: "
                    + notificationUuid + " and account with uuid: " + accountUuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            status = false;
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
        return status;
    }

    @Override
    public List<String> getUnreadNotificationStatusByAccount(String accountUuid) {
        List<String> list = new ArrayList<>();

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT NotificationUuid FROM NotificationStatus WHERE "
                    + "notificationuuid IN(SELECT uuid FROM Notification WHERE AccountUuid = ?) AND ReadFlag = 'N';");
            //pstmt.setBoolean(1, false);
            pstmt.setString(1, accountUuid);

            rset = pstmt.executeQuery();

            while (rset.next()) {
                list.add(rset.getString(1));
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all the NotificationUuids that have "
                    + "the read flag set as false for the account with uuid: " + accountUuid);
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
    
    
    @Override
    public boolean deleteNotificationStatus(String uuid) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("delete from NotificationStatus where notificationuuid=?;");
            pstmt.setString(1, uuid);
            
            

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put NotificationStatus: " + uuid);
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
