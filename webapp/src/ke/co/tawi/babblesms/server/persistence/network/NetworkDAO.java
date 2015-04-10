package ke.co.tawi.babblesms.server.persistence.network;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

/**
 * Copyright (c) Tawi Commercial Services Ltd., Jun 27, 2013
 *
 * @author <a href="mailto:japhethk@tawi.mobi">Japheth Korir</a>
 * @version %I%, %G%
 */
public class NetworkDAO extends GenericDAO implements BabbleNetworkDAO {

    private static NetworkDAO networkDAO;

    private final Logger logger;

    public static NetworkDAO getInstance() {
        if (networkDAO == null) {
            networkDAO = new NetworkDAO();
        }
        return networkDAO;
    }

    /**
     *
     */
    protected NetworkDAO() {
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
    public NetworkDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }

    /**
     *
     */
    @Override
    public boolean putNetwork(Network network) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO Network (Uuid, Name,countryuuid) VALUES (?,?,?);");
            pstmt.setString(1, network.getUuid());
            pstmt.setString(2, network.getName());
            pstmt.setString(3, network.getCountryuuid());
            

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put network: " + network);
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
     * @param name
     * @return network
     *
     */
    @Override
    public Network getNetworkByName(String name) {
        Network network = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Network WHERE LOWER(name) like LOWER(?);");
            pstmt.setString(1, "%"+name +"%");
            rset = pstmt.executeQuery();

            if (rset.next()) {
                network = b.toBean(rset, Network.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting network with uuid: " + name);
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
        return network;
    }

    /**
     *
     */
    @Override
    public Network getNetwork(String uuid) {
        Network network = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Network WHERE Uuid = ?;");
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                network = b.toBean(rset, Network.class);
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
        return network;
    }

    /**
     *
     */
    @Override
    public List<Network> getAllNetworks() {
        List<Network> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Network;");
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, Network.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all networks");
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
     * @param network
     * @return success
     */
    @Override
    public boolean updateNetwork(String uuid, String network) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE Network SET Name=? WHERE Uuid = ?;");
            pstmt.setString(1, network);
            pstmt.setString(2, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting network with uuid " + uuid);
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
    public boolean deleteNetwork(String uuid) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM Network WHERE Uuid = ?;");
            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting network with uuid " + uuid);
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