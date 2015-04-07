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
package ke.co.tawi.babblesms.server.persistence.accounts;

import ke.co.tawi.babblesms.server.beans.account.AccountBalance;
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
 * Persistence implementation for the account balance.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class AccountBalanceDAO extends GenericDAO implements BabbleAccountBalanceDAO {

    private static AccountBalanceDAO clientBalanceDAO;
    private final Logger logger = Logger.getLogger(AccountBalanceDAO.class);

    public static AccountBalanceDAO getInstance() {
        if (clientBalanceDAO == null) {
            clientBalanceDAO = new AccountBalanceDAO();
        }
        return clientBalanceDAO;
    }

    protected AccountBalanceDAO() {
        super();
    }

    /**
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPassword
     * @param dbPort
     */
    public AccountBalanceDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
    }

    /**
     *
     * @param uuid
     * @param balance
     * @return
     */
    @Override
    public boolean addCredit(String uuid, int balance) {
        boolean success = true;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE accountbalance SET balance=(balance+?) WHERE uuid=?");
            pstmt.setInt(1, balance);
            pstmt.setString(2, uuid);
            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to update client balance for uuid: " + uuid);
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
     * @param uuid
     * @param balance
     * @return
     */
    @Override
    public boolean deductCredit(String source,String networkuuid, int balance) {
        boolean success = true;
        Connection conn = null;
        PreparedStatement pstmt = null;
        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE accountbalance SET balance=(balance-?) WHERE origin=? AND networkuuid=?");
            pstmt.setInt(1, balance);
            pstmt.setString(2, source);
            pstmt.setString(3, networkuuid);
            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to update client balance for uuid: " + networkuuid);
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
     * @param accountuuid
     * @return
     */
    @Override
    public List<AccountBalance> getClientBalanceByAccount(String accountuuid) {

        List<AccountBalance> list = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        BeanProcessor b = new BeanProcessor();
        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM accountbalance WHERE accountUuid = ?;");

            pstmt.setString(1, accountuuid);

            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, AccountBalance.class);
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
     * @param uuid
     * @return
     */
    @Override
    public AccountBalance getClientBalanceByUuid(String uuid) {

        AccountBalance accountBalance = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM accountbalance WHERE Uuid = ?;");
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                accountBalance  = b.toBean(rset, AccountBalance .class);
            }

        } catch (SQLException e) {
            logger.error("SQLException when getting incomingLog with uuid: " + uuid);
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

        return accountBalance;

    }

    /**
     *
     * @param accountuuid
     * @param emreturn
     */
    @Override
    public List<AccountBalance> getClientBalanceByAccountEmail(String email) {

        List<AccountBalance> list = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        BeanProcessor b = new BeanProcessor();
        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM accountbalance WHERE accountuuid "
                    + "IN (SELECT uuid FROM account WHERE email = ?) ;");

            pstmt.setString(1, email);

            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, AccountBalance.class);
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
     * @param accountuuid
     * @return
     */
    @Override
    public List<AccountBalance> getClientBalanceByShortcode(String accountuuid) {
        List<AccountBalance> list = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        BeanProcessor b = new BeanProcessor();
        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM accountbalance WHERE  accountuuid=? ;");
            pstmt.setString(1, accountuuid);

            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, AccountBalance.class);
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
     * @return
     */
    @Override
    public List<AccountBalance> getClientBalance() {
        List<AccountBalance> list = null;
        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        BeanProcessor b = new BeanProcessor();
        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM accountbalance;");

            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, AccountBalance.class);
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
     * @param uuid
     * @return
     */
    @Override
    public AccountBalance getClientBalanceBynetwork(String accountuuid,String source,String networkuuid){

        AccountBalance accountBalance = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM accountbalance WHERE origin=? AND accountuuid = ? AND networkuuid=?;");
            pstmt.setString(1, source);
            pstmt.setString(2, accountuuid);
            pstmt.setString(3, networkuuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                accountBalance  = b.toBean(rset, AccountBalance .class);
            }

        } catch (SQLException e) {
            logger.error("SQLException when getting incomingLog with uuid: " + accountuuid);
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

        return accountBalance;
    }
}
