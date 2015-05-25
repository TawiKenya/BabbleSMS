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
package ke.co.tawi.babblesms.server.persistence.logs;

import ke.co.tawi.babblesms.server.beans.log.OutgoingGrouplog;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Persistence implementation for {@link OutgoingGrouplog}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class OutgoingGroupLogDAO extends GenericDAO implements BabbleOutgoingGroupLogDAO {

    private static OutgoingGroupLogDAO outgoingGroupLogDAO;

    private final Logger logger;

    public static OutgoingGroupLogDAO getInstance() {
        if (outgoingGroupLogDAO == null) {
            outgoingGroupLogDAO = new OutgoingGroupLogDAO();
        }
        return outgoingGroupLogDAO;
    }

    /**
     *
     */
    protected OutgoingGroupLogDAO() {
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
    public OutgoingGroupLogDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }

    /**
     *
     */
    @Override
    public boolean putOutgoingGrouplog(OutgoingGrouplog outgoinggroupLog) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO OutgoinggroupLog (Uuid,origin,networkuuid,destination,message,sender,messagestatusuuid) VALUES (?,?,?,?,?,?);");

            pstmt.setString(1, outgoinggroupLog.getUuid());
            pstmt.setString(2, outgoinggroupLog.getOrigin());
            pstmt.setString(3, outgoinggroupLog.getNetworkUuid()); 
            pstmt.setString(4, outgoinggroupLog.getDestination());
            pstmt.setString(5, outgoinggroupLog.getMessage());
            pstmt.setString(6, outgoinggroupLog.getSender());
            pstmt.setString(7, outgoinggroupLog.getMessagestatusuuid());

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put outgoinggroupLog: " + outgoinggroupLog);
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
    public OutgoingGrouplog getOutgoingGrouplog(String uuid) {
        OutgoingGrouplog outgoingGrouplog = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM OutgoinggroupLog WHERE Uuid = ?;");
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                outgoingGrouplog = b.toBean(rset, OutgoingGrouplog.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting outgoingGrouplog with uuid: " + uuid);
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
        return outgoingGrouplog;
    }

    /**
     *
     */
    @Override
    public List<OutgoingGrouplog> getOutgoingGrouplogByAccount(String accountuuid) {
        List<OutgoingGrouplog> outgoingGrouplog = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM OutgoinggroupLog WHERE sender = ? Order By logTime desc;");
            pstmt.setString(1, accountuuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                outgoingGrouplog = b.toBeanList(rset, OutgoingGrouplog.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting outgoingGrouplog with uuid: " + accountuuid);
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
        return outgoingGrouplog;
    }

    /**
     *
     */
    @Override
    public List<OutgoingGrouplog> getAllOutgoingGrouplogs() {
        List<OutgoingGrouplog> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM OutgoingGroupLog Order By id desc;");
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, OutgoingGrouplog.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all outgoinggroupLogs");
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
     * @param outgoingLog
     * @return success
     */
    @Override
    public boolean updateOutgoingGrouplog(String uuid, String outgoingGroupLog) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE OutgoinggroupLog SET messagestatusuuid=? WHERE Uuid = ?;");
            pstmt.setString(1, outgoingGroupLog);
            pstmt.setString(2, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting outgoingGroupLog with uuid " + uuid);
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
    public boolean deleteOutgoingGrouplog(String uuid) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM OutgoingGroupLog WHERE Uuid = ?;");
            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting outgoingGroupLog with uuid " + uuid);
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
