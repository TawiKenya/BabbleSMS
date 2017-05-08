package ke.co.tawi.babblesms.server.persistence.status;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import ke.co.tawi.babblesms.server.beans.messagetemplate.MsgStatus;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 */
public class MessageStatusDAO extends GenericDAO implements BabbleMessageStatusDAO {

    private static MessageStatusDAO messageStatusDAO;

    private final Logger logger;

    public static MessageStatusDAO getInstance() {
        if (messageStatusDAO == null) {
            messageStatusDAO = new MessageStatusDAO();
        }
        return messageStatusDAO;
    }

    /**
     *
     */
    protected MessageStatusDAO() {
        super();
        logger = LogManager.getLogger(this.getClass());
    }

    /**
     * Used for testing purposes only.
     *
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPassword
     * @param dbPort
     */
    public MessageStatusDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = LogManager.getLogger(this.getClass());
    }

    /**
     *MessageStatus
     */
    @Override
    public boolean putMessageStatus(MsgStatus messageStatus) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO messagestatus (Uuid, description) VALUES (?,?);");
            
            pstmt.setString(1, messageStatus.getUuid());
            pstmt.setString(2, messageStatus.getDescription());
            
            

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put outgoingLog: " + messageStatus);
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
    public MsgStatus getMessageStatus(String uuid) {
        MsgStatus messageStatus = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM messagestatus WHERE Uuid = ?;");
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                messageStatus = b.toBean(rset, MsgStatus.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting outgoingLog with uuid: " + uuid);
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
        return messageStatus;
    }

    /**
     *
     */
    @Override
    public List<MsgStatus> getAllMessageStatus() {
        List<MsgStatus> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM messagestatus;");
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, MsgStatus.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all outgoingLogs");
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
     * @param uuid
     * @param messagestatus
     * @return success
     */
    @Override
    public boolean updateMessageStatus(String uuid, String description) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE messagestatus SET description=? WHERE Uuid = ?;");
            pstmt.setString(1, description);
            pstmt.setString(2, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting outgoingLog with uuid " + uuid);
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
    public boolean deleteMessageStatus(String uuid) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM messagestatus WHERE Uuid = ?;");
            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting outgoingLog with uuid " + uuid);
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
