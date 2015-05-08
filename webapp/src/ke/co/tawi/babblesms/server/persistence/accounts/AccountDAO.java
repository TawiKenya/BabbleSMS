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
 *  description for {@link Account}s.
 * <p> 
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */

public class AccountDAO extends GenericDAO implements BabbleAccountDAO {
	
	private static AccountDAO accountDAO;
	private Logger logger = Logger.getLogger(this.getClass());
    private BeanProcessor beanProcessor = new BeanProcessor();
	

    /**
     * @return the {@link AccountDAO}
     */
	public static AccountDAO getInstance(){
		if(accountDAO == null){
			accountDAO = new AccountDAO();
		}
		
		return accountDAO;
		
	}
	/**
	 * 
	 */
	
	protected AccountDAO(){
		super();
	}
	
	/**
	 * Used for testing purposes only.
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPassword
     * @param dbPort
	 */
	
	public AccountDAO(String dbName,String dbHost, String dbUsername,
			String dbPassword, int dbPort){
		super(dbName,dbHost,dbUsername,dbPassword,dbPort);
	}
	
	/**
	 * @see ke.co.tawi.babblesms.server.beans.account.BabbleAccountDAO#getAccountByUuid(java.lang.String)
	 */
	@Override
	public Account getAccount(String uuid) {
		Account account = null;
		
		
		try(
			Connection conn = dbCredentials.getConnection();
			 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Account WHERE Uuid = ?;");    		   
			   ) {
			  pstmt.setString(1, uuid);           
		       ResultSet rset = pstmt.executeQuery();
		       
		       if (rset.next()) {
	               account = beanProcessor.toBean(rset, Account.class);
	           }
			
			
		} catch(SQLException e){
			
			   logger.error("SQLException when getting account with uuid: " + uuid);
	           logger.error(ExceptionUtils.getStackTrace(e));
		}
		
		
		return account;
	}

	@Override
	public Account getAccountByName(String username) {
		// TODO Auto-generated method stub
		return null;
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
        
        try{
        	conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT * FROM Account;");
            rset = pstmt.executeQuery();

            list = b.toBeanList(rset, Account.class);

        }catch(SQLException e){
        	logger.error("SQL Exception when getting all accountss");
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        finally{
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
     * @see ke.co.tawi.babblesms.server.persistence.account.BabbleAccountDAO#putAccount(Account)
     */

	@Override
	public boolean putAccount(Account account) {
		boolean success = true;
		
		try(
				Connection conn = dbCredentials.getConnection();
	      	    PreparedStatement pstmt = conn.prepareStatement("INSERT INTO account "
	         	 + "(uuid, name, description, accountuuid, statusuuid) VALUES (?,?,?,?,?);"); )
	         	 {
			pstmt.setString(1, account.getUuid());   
            pstmt.setString(2, account.getName());
            pstmt.setString(3, account.getDescription());
            pstmt.setString(4, account.getStatusUuid());
  	       
            pstmt.executeUpdate();
			
			
			
		}catch(SQLException e){
			
			logger.error("SQLException when trying to put " + account);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
		}
		
		
		return success;
	}

    /**
	 * @see ke.co.tawi.babblesms.server.persistence.account.BabbleAccountDAO#updateAccount(java.lang.String, ke.co.tawi.babblesms.server.beans.account.Account)
	 */
	@Override
	public boolean updateAccount(String uuid, Account account) {
		boolean success = true;
		
		try(Connection conn = dbCredentials.getConnection();
	      	 PreparedStatement pstmt = conn.prepareStatement("UPDATE account SET name=?, "
	         + "description=?,  statusuuid=? WHERE Uuid=?;");   ){
			
			pstmt.setString(1, account.getName());
            pstmt.setString(2, account.getDescription());
            pstmt.setString(3, account.getStatusUuid());
            pstmt.setString(4, account.getUuid()); 
            
            pstmt.executeUpdate();
			
		}catch(SQLException e){
			 logger.error("SQLException when trying to put " + account);
             logger.error(ExceptionUtils.getStackTrace(e));
             success = false;
			
		}
		
		return success;
	}

	

}
