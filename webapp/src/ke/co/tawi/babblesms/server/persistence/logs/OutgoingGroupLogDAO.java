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
import ke.co.tawi.babblesms.server.beans.log.IncomingLog;
import ke.co.tawi.babblesms.server.beans.log.OutgoingGrouplog;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
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
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>
 */
public class OutgoingGroupLogDAO extends GenericDAO implements BabbleOutgoingGroupLogDAO {

    private static OutgoingGroupLogDAO logDAO;
    
    private  BeanProcessor b = new BeanProcessor();

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
     * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleOutgoingGroupLogDAO#putOutgoingGrouplog(ke.co.tawi.babblesms.server.beans.log.OutgoingGrouplog)
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

        }   return success;

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

              try(
                  ResultSet rset = pstmt.executeQuery();            
                  ){

            if (rset.next()) {
                log = beanProcessor.toBean(rset, OutgoingGrouplog.class);
            }
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

    
    
    
    /**
     * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleOutgoingGroupLogDAO#getAllOutgoingGrouplogs()
     */
    @Override
    public List<OutgoingGrouplog> getAllOutgoingGrouplogs() {
        List<OutgoingGrouplog> list = null; 
        try(
        	Connection conn = dbCredentials.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM OutgoingGroupLog Order By id desc;");
        	) {            
               try(
                   ResultSet rset = pstmt.executeQuery();            
                   ){
                     list = b.toBeanList(rset, OutgoingGrouplog.class);
                     }
        } catch (SQLException e) {
            logger.error("SQL Exception when getting all outgoinggroupLogs");
            logger.error(ExceptionUtils.getStackTrace(e));        
        }
        return list;
    }
    
    

    /**
     *  @see ke.co.tawi.babblesms.server.persistence.logs.BabbleOutgoingGroupLogDAO#updateOutgoingGrouplog(java.lang.String, java.lang.String)    
     */
    @Override
    public boolean updateOutgoingGrouplog(String uuid, String outgoingGroupLog) {
        boolean success = true;       

        try(  Connection  conn = dbCredentials.getConnection();
              PreparedStatement  pstmt = conn.prepareStatement("UPDATE OutgoinggroupLog SET "
                		+ "messagestatusuuid=? WHERE Uuid = ?;");
        		) {           
           
            pstmt.setString(1, outgoingGroupLog);
            pstmt.setString(2, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting outgoingGroupLog with uuid " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        
        return success;
    }
    
    
    

    /**
     * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleOutgoingGroupLogDAO#deleteOutgoingGrouplog(java.lang.String)
     */
    @Override
    public boolean deleteOutgoingGrouplog(String uuid) {
        boolean success = true;
        
        try (
        	Connection conn = dbCredentials.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("DELETE FROM OutgoingGroupLog WHERE Uuid = ?;");
              ){            
            
            pstmt.setString(1, uuid);

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting outgoingGroupLog with uuid " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        
        return success;
    }
 
    /**
     * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleOutgoingGroupLogDAO
     * #getOutGoingGroupLog(ke.co.tawi.babblesms.server.beans.account.Account, int, int)
     */
    @Override
    public List<OutgoingGrouplog> getOutGoingGroupLog(Account account, int fromIndex, int toIndex) {
        List<OutgoingGrouplog> list = new ArrayList<>();
        try 
            (
        		Connection conn =  dbCredentials.getConnection();
    		    PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM OutgoingGroupLog WHERE sender = ?"
        		+ " ORDER BY logTime DESC LIMIT ? OFFSET ? ;");
            ) {
        	
        	pstmt.setString(1, account.getUuid());
        	pstmt.setInt(2, toIndex - fromIndex);
        	pstmt.setInt(3, fromIndex);
        	
        	try(ResultSet rset = pstmt.executeQuery();) {
        		list = b.toBeanList(rset, OutgoingGrouplog.class);
        	}
        } 
        
        catch (SQLException e) {
        	
            logger.error("SQLException while getting incomingLog from index "
                    + fromIndex + " to index " + toIndex + ".");
            logger.error(ExceptionUtils.getStackTrace(e));

        }

        return list;
    }
    


    
}
