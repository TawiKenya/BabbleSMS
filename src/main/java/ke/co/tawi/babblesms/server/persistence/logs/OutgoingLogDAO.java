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

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Persistence implementation for {@link OutgoingLog}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class OutgoingLogDAO extends GenericDAO implements BabbleOutgoingLogDAO {

    private static OutgoingLogDAO outgoingLogDAO;

    private BeanProcessor beanProcessor = new BeanProcessor();
    
    private final Logger logger;

    
    /**
     * @return the singleton instance of {@link OutgoingLogDAO}
     */
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
     * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleOutgoingLogDAO#put(ke.co.tawi.babblesms.server.beans.log.OutgoingLog)
     */
    @Override
    public boolean put(OutgoingLog outgoingLog) {
        boolean success = true;

        try (
        		Connection conn = dbCredentials.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO OutgoingLog "
        				+ "(Uuid, origin, destination, message, logtime, networkuuid, sender, messagestatusuuid, phoneuuid) "
        				+ "VALUES (?,?,?,?,?,?,?,?,?);");
        		) {
        	
            pstmt.setString(1, outgoingLog.getUuid());
            pstmt.setString(2, outgoingLog.getOrigin());
            pstmt.setString(3, outgoingLog.getDestination());
            pstmt.setString(4, outgoingLog.getMessage());
            pstmt.setTimestamp(5, new Timestamp(outgoingLog.getLogTime().getTime()));
            pstmt.setString(6, outgoingLog.getNetworkUuid());
            pstmt.setString(7, outgoingLog.getSender());
            pstmt.setString(8, outgoingLog.getMessagestatusuuid());
            pstmt.setString(9, outgoingLog.getPhoneUuid());
            
            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put " + outgoingLog);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        
        return success;
    }
    

    
    /**
     * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleOutgoingLogDAO#get(java.lang.String)
     */
    @Override
    public OutgoingLog get(String uuid) {
        OutgoingLog outgoingLog = null;

        try (
        	Connection conn = dbCredentials.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM OutgoingLog WHERE Uuid = ?;");
        		) {
        	
            pstmt.setString(1, uuid);
            
            try(ResultSet rset = pstmt.executeQuery();) {
                if (rset.next()) {
                	outgoingLog = beanProcessor.toBean(rset, OutgoingLog.class);
                }
            }
            
        } catch (SQLException e) {
            logger.error("SQL Exception when getting outgoingLog with uuid: " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        
        return outgoingLog;
    }
    
    
    
    /**
     * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleOutgoingLogDAO#get(java.lang.String)
     */
    @Override
    public List<OutgoingLog> getAll(String uuid) {
        List<OutgoingLog> outgoingLog = null;

        try (
        	Connection conn = dbCredentials.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM OutgoingLog WHERE Uuid = ?;");
        		) {
        	
            pstmt.setString(1, uuid);
            
            try(ResultSet rset = pstmt.executeQuery();) {
                if (rset.next()) {
                	outgoingLog = beanProcessor.toBeanList(rset, OutgoingLog.class);
                }
            }
            
        } catch (SQLException e) {
            logger.error("SQL Exception when getting outgoingLog with uuid: " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
        } 
        
        return outgoingLog;
    }
    
   
    
    /**
     * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleOutgoingLogDAO#get(Account, int, int)
     */
    @Override
    public List<OutgoingLog> get(Account account, int fromIndex, int toIndex) {
        List<OutgoingLog> list = new ArrayList<>();
        
        try (
        	Connection conn = dbCredentials.getConnection();            
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM outgoingLog where sender = ? "
            		+ "ORDER BY logtime DESC LIMIT ? OFFSET ?;");
        		) {
        	
            pstmt.setString(1, account.getUuid());
	        pstmt.setInt(2, toIndex - fromIndex);
	        pstmt.setInt(3, fromIndex);
	        
	        try( ResultSet rset = pstmt.executeQuery();	) {
	        	list = beanProcessor.toBeanList(rset, OutgoingLog.class);
	        }
	        
        } catch (SQLException e) {
            logger.error("SQLException while getting outgoingLog from index "
                    + fromIndex + " to index " + toIndex + " for " + account);
            logger.error(ExceptionUtils.getStackTrace(e));
        } 

        return list;
    }
    
    

}

