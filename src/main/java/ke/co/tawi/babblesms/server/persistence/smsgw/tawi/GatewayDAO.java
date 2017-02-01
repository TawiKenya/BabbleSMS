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
package ke.co.tawi.babblesms.server.persistence.smsgw.tawi;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.smsgateway.TawiGateway;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;
import ke.co.tawi.babblesms.server.utils.security.SecurityUtil;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Persistence implementation for {@link TawiGateway}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class GatewayDAO extends GenericDAO implements BabbleGatewayDAO {

	private static GatewayDAO gatewayDAO;

    private final Logger logger = Logger.getLogger(this.getClass());

    private BeanProcessor beanProcessor = new BeanProcessor();
    

    /** 
     * @return the singleton instance of {@link GatewayDAO}
     */
    public static GatewayDAO getInstance() {
        if (gatewayDAO == null) {
        	gatewayDAO = new GatewayDAO();
        }
        
        return gatewayDAO;
    }

    
    /**
     *
     */
    protected GatewayDAO() {
        super();
    }
    
    
	/**
	 * @param dbName
	 * @param dbHost
	 * @param dbUsername
	 * @param dbPassword
	 * @param dbPort
	 */
	public GatewayDAO(String dbName, String dbHost, String dbUsername,
			String dbPassword, int dbPort) {
		super(dbName, dbHost, dbUsername, dbPassword, dbPort);
	}

	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.smsgw.tawi.BabbleGatewayDAO#get(ke.co.tawi.babblesms.server.beans.account.Account)
	 */
	@Override
	public TawiGateway get(Account account) {
		TawiGateway gw = null;

		try (
			Connection conn = dbCredentials.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM SMSGateway WHERE accountuuid = ?;");
			) {
			
			pstmt.setString(1, account.getUuid());           
			ResultSet rset = pstmt.executeQuery();
       
			if (rset.next()) {
				gw = beanProcessor.toBean(rset, TawiGateway.class);
			}
       
		} catch (SQLException e) {
			logger.error("SQLException when getting TawiGateway for: " + account);
			logger.error(ExceptionUtils.getStackTrace(e));
		}
   
		return gw;
	}

	
	

	/**
	 * @see ke.co.tawi.babblesms.server.persistence.smsgw.tawi.BabbleGatewayDAO#getByAccountUsername(java.lang.String)
	 */
	@Override
	public TawiGateway getByAccountUsername(String username) {
		TawiGateway gw = null;

		try (
			Connection conn = dbCredentials.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM SMSGateway WHERE username = ?;");
			) {
			
			pstmt.setString(1, username);           
			ResultSet rset = pstmt.executeQuery();
       
			if (rset.next()) {
				gw = beanProcessor.toBean(rset, TawiGateway.class);
			}
       
		} catch (SQLException e) {
			logger.error("SQLException when getting TawiGateway with username: " + username);
			logger.error(ExceptionUtils.getStackTrace(e));
		}
   
		return gw;
	}

	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.smsgw.tawi.BabbleGatewayDAO#put(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.smsgateway.TawiGateway)
	 */
	public boolean put(TawiGateway gateway) {
		boolean success = true;
		
		try(
				Connection conn = dbCredentials.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("INSERT INTO SMSGateway" 
		        		+"(accountUuid, url, username, passwd) VALUES (?,?,?,?);");
				) {
			pstmt.setString(1, gateway.getAccountUuid() );
			pstmt.setString(2, gateway.getUrl());
			pstmt.setString(3, gateway.getUsername());
			pstmt.setString(4, SecurityUtil.getMD5Hash(gateway.getPasswd()));
			pstmt.execute();
			
		} catch (SQLException e) {
			logger.error("SQLException when trying to put TawiGateway " + gateway);
			logger.error(ExceptionUtils.getStackTrace(e));
			success = false;
		}
		
		return success;
	}


	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.smsgw.tawi.BabbleGatewayDAO#edit(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.smsgateway.TawiGateway)
	 */
	public boolean edit(TawiGateway gateway) {
		boolean success = true;
		
		try(Connection conn = dbCredentials.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("UPDATE SMSGateway SET url=?, "
				+ "username=?, passwd=? WHERE accountUuid = ?;");
		) {
			pstmt.setString(1, gateway.getUrl());
			pstmt.setString(2, gateway.getUsername());
			pstmt.setString(3, gateway.getPasswd());
			pstmt.setString(4, gateway.getAccountUuid());
			pstmt.executeUpdate();

		} catch (SQLException e) {
			logger.error("SQLException when editing TawiGateway " + gateway);
			logger.error(ExceptionUtils.getStackTrace(e));
		    success = false;
	   }		
		
		return success;
	}


	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.smsgw.tawi.BabbleGatewayDAO#getAll()
	 */
	public List<TawiGateway> getAllRecords() {
		List<TawiGateway> list = null;
		
		try( Connection conn = dbCredentials.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM SMSGateway;");
				ResultSet rset = pstmt.executeQuery();
				){
			list = beanProcessor.toBeanList(rset, TawiGateway.class);
			
		} catch(SQLException e){
			logger.error("SQLException when getting all SMSGateways");
            logger.error(ExceptionUtils.getStackTrace(e));
		}
		
		return list;
	}


	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.smsgw.tawi.BabbleGatewayDAO#logResponse(Account, String, Date)
	 */
	@Override
	public void logResponse(Account account, String response, Date date) {
				
		try(
			Connection con = dbCredentials.getConnection();				
			
			PreparedStatement pst = con.prepareStatement("INSERT INTO SentGatewayLog(uuid, accountuuid, "
				+ "response, responsedate) VALUES(?,?,?,?);");						
			    
			){			
				pst.setString(1, UUID.randomUUID().toString());
				pst.setString(2, account.getUuid());
				pst.setString(3, response);
				pst.setTimestamp(4, new Timestamp(date.getTime()));	
				pst.executeUpdate();
							
		} catch(SQLException e) {
			logger.error("SQLException while trying to log " + response + " for " + account);
			logger.error(ExceptionUtils.getStackTrace(e));
		}
		
	}
	
}
