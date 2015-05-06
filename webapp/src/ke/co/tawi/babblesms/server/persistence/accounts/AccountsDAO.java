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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;


/**
 * Persistence implementation for {@link Account}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class AccountsDAO extends GenericDAO implements BabbleAccountsDAO {

    private static AccountsDAO accountsDAO;

    private final Logger logger;

    
    /**
     * @return {@link AccountsDAO}
     */
    public static AccountsDAO getInstance() {
        if (accountsDAO == null) {
            accountsDAO = new AccountsDAO();
        }
        return accountsDAO;
    }

    /**
     *
     */
    protected AccountsDAO() {
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
    public AccountsDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }
    

    /**
     *
     * @param accounts
     */
    @Override
    public boolean putAccount(Account accounts) {
        boolean success = true;
//apiusername, apipassword, 
        try (
        		Connection conn = dbCredentials.getConnection();
<<<<<<< .mine
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Account (Uuid, username,apiusername, apipassword, logpassword,"
                		+ "name, mobile, email, statusuuid) "
                		+ "VALUES (?,?,?,?,?,?,?);")
=======
                PreparedStatement pstmt = conn.prepareStatement("INSERT INTO Account (Uuid, username, logpassword,"
                		+ "name, mobile, email, statusuuid) "
                		+ "VALUES (?,?,?,?,?,?,?);")
>>>>>>> .r97
            ) {
        	
            pstmt.setString(1, accounts.getUuid());
            pstmt.setString(2, accounts.getUsername());
            pstmt.setString(3, accounts.getLogpassword());
            pstmt.setString(4, accounts.getName());
            pstmt.setString(5, accounts.getMobile());
            pstmt.setString(6, accounts.getEmail());
            pstmt.setString(7, accounts.getStatusuuid());

            pstmt.execute();
            
        } catch (SQLException e) {
        	logger.error("SQLException when trying to put: " + accounts);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        }
      
        
        return success;
    }

    
    /**
     *
     * @param name
     * @return accounts
     *
     */
    @Override
    public Account getAccountByName(String name) {
        Account accounts = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Account WHERE username = ?;");
            pstmt.setString(1, name);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                accounts = b.toBean(rset, Account.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting accounts with uuid: " + name);
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
        return accounts;
    }
    
    
    /**
     *
     * @param name
     * @return accounts
     *
     */
    @Override
    public Account getAccountByEmail(String email) {
        Account accounts = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Account WHERE email = ?;");
            pstmt.setString(1, email);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                accounts = b.toBean(rset, Account.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting accounts with uuid: " + email);
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
        return accounts;
    }
   
    /**
     *
     * @param name
     * @return accounts
     *
     */
    @Override
    public Account getAccountByEmailuuid(String uuid,String email){
        Account accounts = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Account WHERE email = ? AND uuid=?;");
            pstmt.setString(1, email);
            pstmt.setString(2, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                accounts = b.toBean(rset, Account.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting accounts with uuid: " + email);
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
        return accounts;
    }



    /**
     *
     */
    @Override
    public Account getAccount(String uuid) {
        Account accounts = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Account WHERE Uuid = ?;");
            pstmt.setString(1, uuid);
            rset = pstmt.executeQuery();

            if (rset.next()) {
                accounts = b.toBean(rset, Account.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting accounts with uuid: " + uuid);
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
        return accounts;
    }

    /**
     *
     */
    @Override
    public List<Account> getAllAccounts() {
        List<Account> list = null;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;
        BeanProcessor b = new BeanProcessor();

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Account;");
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, Account.class);

        } catch (SQLException e) {
            logger.error("SQL Exception when getting all accountss");
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
     * @param accounts
     * @return success
     */
    @Override
    public boolean updateAccount(Account accounts) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("UPDATE Account SET username=?,logpassword=?,name=?,mobile=?,email=? WHERE Uuid = ?;");
            pstmt.setString(1, accounts.getUsername());
            pstmt.setString(2, accounts.getLogpassword());
            pstmt.setString(3, accounts.getName());
            pstmt.setString(4, accounts.getMobile());
            pstmt.setString(5, accounts.getEmail());
            pstmt.setString(6, accounts.getUuid());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting accounts with uuid " + accounts);
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
    public boolean deleteAccount(String uuid) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("DELETE FROM Account WHERE Uuid = ?;");
            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting accounts with uuid " + uuid);
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
     * @param uuid
     * @param accounts
     * @return success
     */
    @Override
    public boolean adminupdateAccount(Account accounts) {
        boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;
<<<<<<< .mine
                                 //,apiusername=?,apipassword=?
=======
                                            //,apiusername=?,apipassword=?
>>>>>>> .r97
        try {
            conn = dbCredentials.getConnection();
<<<<<<< .mine
            pstmt = conn.prepareStatement("UPDATE Account SET username=?,name=?,mobile=?,apiusername=?,apipassword=?,email=?,dailysmslimit=?,statusuuid=? WHERE Uuid = ?;");
=======
            pstmt = conn.prepareStatement("UPDATE Account SET username=?,name=?,mobile=?,email=?,dailysmslimit=?,statusuuid=? WHERE Uuid = ?;");
>>>>>>> .r97
            pstmt.setString(1, accounts.getUsername());
            pstmt.setString(2, accounts.getName());
            pstmt.setString(3, accounts.getMobile());
            pstmt.setString(4, accounts.getEmail());
            pstmt.setInt(5, accounts.getDailysmslimit());
            pstmt.setString(6, accounts.getStatusuuid());
            pstmt.setString(7, accounts.getUuid());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting accounts with uuid " + accounts);
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
