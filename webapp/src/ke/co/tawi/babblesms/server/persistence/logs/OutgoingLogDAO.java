package ke.co.tawi.babblesms.server.persistence.logs;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 * @version %I%, %G%
 */
public class OutgoingLogDAO extends GenericDAO implements BabbleOutgoingLogDAO {

    private static OutgoingLogDAO outgoingLogDAO;

    private final Logger logger;

    public static OutgoingLogDAO getInstance() {
        if (outgoingLogDAO == null) {
            outgoingLogDAO = new OutgoingLogDAO();
        }
        return outgoingLogDAO;
    }

    /**
     *
     */
    protected OutgoingLogDAO() {
        super();
        logger = Logger.getLogger(this.getClass());
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
    public OutgoingLogDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }

    /**
     *
     */
    @Override
    public boolean putOutgoingLog(OutgoingLog outgoingLog) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO OutgoingLog (Uuid,origin,destination,message,networkuuid,sender,messagestatusuuid) VALUES (?,?,?,?,?,?,?);");

            pstmt.setString(1, outgoingLog.getUuid());
            pstmt.setString(2, outgoingLog.getOrigin());
            pstmt.setString(3, outgoingLog.getDestination());
            pstmt.setString(4, outgoingLog.getMessage());
            pstmt.setString(5, outgoingLog.getNetworkuuid());
            pstmt.setString(6, outgoingLog.getSender());
            pstmt.setString(7, outgoingLog.getMessagestatusuuid());

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put outgoingLog: " + outgoingLog);
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
    public OutgoingLog getOutgoingLog(String uuid) {
        OutgoingLog outgoingLog = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM OutgoingLog WHERE Uuid = ?;");
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                outgoingLog = b.toBean(rset, OutgoingLog.class);
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
        return outgoingLog;
    }
    
   

    
    @Override
    public List<OutgoingLog> getOutgoingLog(Account account, int fromIndex, int toIndex) {
        List<OutgoingLog> list = new ArrayList<>();
        StringBuilder queryBuff = new StringBuilder();
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            
            pstmt = conn.prepareStatement("SELECT * FROM outgoingLog where sender =  ?  ORDER BY logtime DESC  LIMIT ? OFFSET ?;");
            pstmt.setString(1, account.getUuid());
	    pstmt.setInt(2, toIndex - fromIndex);
	    pstmt.setInt(3, fromIndex);
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, OutgoingLog.class);

        } catch (SQLException e) {
            logger.error(queryBuff.toString() + "SQLException exception while getting outgoingLog from index "
                    + +fromIndex + " to index " + toIndex + "and sender" + account+ ".");
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

    
   
    

}
