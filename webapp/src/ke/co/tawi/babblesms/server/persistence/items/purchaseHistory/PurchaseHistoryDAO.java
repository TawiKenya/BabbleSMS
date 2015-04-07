package ke.co.tawi.babblesms.server.persistence.items.purchaseHistory;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import ke.co.tawi.babblesms.server.beans.account.PurchaseHistory;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 * @version %I%, %G%
 */
public class PurchaseHistoryDAO extends GenericDAO implements BabblePurchaseHistoryDAO {

    private static PurchaseHistoryDAO purchaseHistoryDAO;

    private final Logger logger;

    public static PurchaseHistoryDAO getInstance() {
        if (purchaseHistoryDAO == null) {
            purchaseHistoryDAO = new PurchaseHistoryDAO();
        }
        return purchaseHistoryDAO;
    }

    /**
     *
     */
    protected PurchaseHistoryDAO() {
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
    public PurchaseHistoryDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }

    /**
     *
     * @param purchaseHistory
     */
    @Override
    public boolean putPurchaseHistory(PurchaseHistory purchaseHistory) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO purchasehistory(Uuid,amount,source,accountuuid,networkuuid,purchasetime) VALUES (?,?,?,?,?,?);");
            pstmt.setString(1, purchaseHistory.getUuid());
            pstmt.setInt(2, purchaseHistory.getAmount());
            pstmt.setString(3, purchaseHistory.getSource());
            pstmt.setString(4, purchaseHistory.getAccountuuid());
            pstmt.setString(5, purchaseHistory.getNetworkuuid());
            pstmt.setTimestamp(6, new Timestamp(purchaseHistory.getPurchasetime().getTime()));

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put purchaseHistory: " + purchaseHistory);
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
    public PurchaseHistory getPurchaseHistory(String uuid) {
        PurchaseHistory purchaseHistory = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM PurchaseHistory WHERE Uuid = ?;");
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                purchaseHistory = b.toBean(rset, PurchaseHistory.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting purchaseHistory with uuid: " + uuid);
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
        return purchaseHistory;
    }

    /**
     *
     */
    @Override
    public List<PurchaseHistory> getAllPurchaseHistory() {
        List<PurchaseHistory> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM PurchaseHistory;");
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, PurchaseHistory.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all purchaseHistorys");
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
    public List<PurchaseHistory> getPurchaseHistoryByAccount(String account) {
        List<PurchaseHistory> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM PurchaseHistory where accountuuid = ?;");
            pstmt.setString(1, account);
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, PurchaseHistory.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all purchaseHistorys");
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
     * @param purchaseHistory
     * @return success
     */
    @Override
    public boolean updatePurchaseHistory(String uuid, String source) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE PurchaseHistory SET source=? WHERE Uuid = ?;");
            pstmt.setString(1, source);
            pstmt.setString(2, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting purchaseHistory with uuid " + uuid);
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
    public boolean deletePurchaseHistory(String uuid) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM PurchaseHistory WHERE Uuid = ?;");
            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting purchaseHistory with uuid " + uuid);
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
