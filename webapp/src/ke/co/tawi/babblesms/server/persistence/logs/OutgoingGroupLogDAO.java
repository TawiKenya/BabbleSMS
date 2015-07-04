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
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
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

    private static OutgoingGroupLogDAO logDAO;

    private BeanProcessor beanProcessor = new BeanProcessor();
    
    private Logger logger;

    
    /**
     * @return the singleton instance of {@link OutgoingGroupLogDAO}
     */
    public static OutgoingGroupLogDAO getInstance() {
        if (logDAO == null) {
            logDAO = new OutgoingGroupLogDAO();
        }
        
        return logDAO;
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
    public boolean put(OutgoingGrouplog log) {
        boolean success = true;

        try(
        		Connection conn = dbCredentials.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO OutgoinggroupLog (Uuid, origin, networkuuid,"
                		+ "destination, message, sender, messagestatusuuid, logTime) "
                		+ "VALUES (?,?,?,?,?,?,?,?);");	
        		) {            

            pstmt.setString(1, log.getUuid());
            pstmt.setString(2, log.getOrigin());
            pstmt.setString(3, log.getNetworkUuid()); 
            pstmt.setString(4, log.getDestination());
            pstmt.setString(5, log.getMessage());
            pstmt.setString(6, log.getSender()); 
            pstmt.setString(7, log.getMessagestatusuuid());
            pstmt.setTimestamp(8, new Timestamp(log.getLogTime().getTime()));
            
            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQL Exception when trying to put: " + log);
            logger.error(ExceptionUtils.getStackTrace(e));
            
            success = false;
        } 
        
        return success;
    }
    
   
    /**
     * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleOutgoingGroupLogDAO#get(java.lang.String)
     */
    @Override
    public OutgoingGrouplog get(String uuid) {
        OutgoingGrouplog log = null;

        try(
        		Connection conn = dbCredentials.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM OutgoinggroupLog WHERE Uuid = ?;");
        		) {            
            
            pstmt.setString(1, uuid);
            ResultSet rset = pstmt.executeQuery();
            
            if (rset.next()) {
                log = beanProcessor.toBean(rset, OutgoingGrouplog.class);
            }

        } catch (SQLException e) {
            logger.error("SQL Exception when getting outgoingGrouplog with uuid: " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));            
        } 
        
        return log;
    }

    
    /**
     * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleOutgoingGroupLogDAO#get(ke.co.tawi.babblesms.server.beans.account.Account)
     */
    @Override
    public List<OutgoingGrouplog> get(Account account) {
        List<OutgoingGrouplog> logList = null;


        try(
        		Connection conn = dbCredentials.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM OutgoinggroupLog WHERE sender=? "
        				+ "Order By logTime desc;");
    		) {
            
            pstmt.setString(1, account.getUuid());
            
            try( ResultSet rset = pstmt.executeQuery();	) {
            	logList = beanProcessor.toBeanList(rset, OutgoingGrouplog.class);
	        }            

        } catch (SQLException e) {
            logger.error("SQL Exception when getting outgoingGrouplog of: " + account);
            logger.error(ExceptionUtils.getStackTrace(e));
            
        } 
        
        return logList;
    }        
    
}
