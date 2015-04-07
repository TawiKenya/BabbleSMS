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
package ke.co.tawi.babblesms.server.persistence.items.credit;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Credit;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Persistence implementation for {@link Credit}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class CreditDAO extends GenericDAO implements BabbleCreditDAO {

    private static CreditDAO creditDAO;

    private final Logger logger;

    public static CreditDAO getInstance() {
        if (creditDAO == null) {
            creditDAO = new CreditDAO();
        }
        return creditDAO;
    }

    /**
     *
     */
    protected CreditDAO() {
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
    public CreditDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }

    /**
     *
     */
    @Override
    public boolean putCredit(Credit credit) {
        boolean success = true;
        
        System.out.println(credit);
        
        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO Credit (Uuid, source,credit,accountuuid) VALUES (?,?,?,?);");
            pstmt.setString(1, credit.getUuid());
            pstmt.setString(2, credit.getSource());
            pstmt.setInt(3, credit.getCredit());
            pstmt.setString(4, credit.getAccountuuid());

            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put credit: " + credit);
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
    public Credit getCredit(String uuid) {
        Credit credit = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Credit WHERE Uuid = ?;");
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                credit = b.toBean(rset, Credit.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting credit with uuid: " + uuid);
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
        return credit;
    }
    
    
    /**
     *
     */
    @Override
    public Credit getCreditbysource(String source,String accuuid) {
        Credit credit = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Credit WHERE source = ? AND accountuuid=?;");
            pstmt.setString(1, source);
            pstmt.setString(2, accuuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                credit = b.toBean(rset, Credit.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting credit with uuid: " + accuuid);
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
        return credit;
    }


    /**
     *
     */
    @Override
    public List<Credit> getAllCredits() {
        List<Credit> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Credit;");
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, Credit.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all credits");
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
    public List<Credit> getAllCreditsByaccountuuid(String accuuid) {
        List<Credit> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Credit WHERE accountuuid=?;");
            pstmt.setString(1, accuuid);
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, Credit.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all credits"+ accuuid);
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
     * @param credit
     * @return success
     */
    @Override
    public boolean updateCredit(String uuid, int credit) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE Credit SET credit=credit + ? WHERE Uuid = ?;");
            pstmt.setInt(1, credit);
            pstmt.setString(2, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting credit with uuid " + uuid);
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
     * @param source
     * @param credit
     * @return success
     */
    @Override
    public boolean deductCredit(String source, int amount) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE Credit SET credit=credit - ? WHERE source = ?;");
            pstmt.setInt(1, amount);
            pstmt.setString(2, source);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting credit with uuid " + source);
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
