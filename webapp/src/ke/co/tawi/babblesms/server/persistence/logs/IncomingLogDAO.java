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
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Persistence implementation for {@link IncomingLog}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class IncomingLogDAO extends GenericDAO implements BabbleIncomingLogDAO {

    private static IncomingLogDAO incomingLogDAO;

    private Logger logger;

    BeanProcessor b = new BeanProcessor();
    
    /**
     * @return
     */
    public static IncomingLogDAO getInstance() {
        if (incomingLogDAO == null) {
            incomingLogDAO = new IncomingLogDAO();
        }
        
        return incomingLogDAO;
    }

    
    /**
     *
     */
    protected IncomingLogDAO() {
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
    public IncomingLogDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
    	
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }

    
    
   /**
    * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleIncomingLogDAO#getIncomingLog(java.lang.String)
    */
    @Override
   public IncomingLog getIncomingLog(String uuid) {
       IncomingLog incomingLog = null;

       Connection conn = null;
       PreparedStatement pstmt = null;
       ResultSet rset = null;
      
       try {
           conn = dbCredentials.getConnection();
           pstmt = conn.prepareStatement("SELECT * FROM IncomingLog WHERE Uuid = ?;");
           pstmt.setString(1, uuid);
           rset = pstmt.executeQuery();

           if (rset.next()) {
               incomingLog = b.toBean(rset, IncomingLog.class);
           }

       } catch (SQLException e) {
           logger.error("SQLException when getting incomingLog with uuid: " + uuid);
           logger.error(ExceptionUtils.getStackTrace(e));
           
       } finally {
           if (rset != null) {
               try { rset.close(); } catch (SQLException e) { }
           }
           
           if (pstmt != null) {
               try { pstmt.close(); } catch (SQLException e) { }
           }
           
           if (conn != null) {
               try { conn.close(); } catch (SQLException e) { }
           }
       }
       
       return incomingLog;
   }
   
   
  

    /**
     * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleIncomingLogDAO#getIncomingLog(java.util.List, int, int)
     */
    @Override
    public List<IncomingLog> getIncomingLog(Account account, int fromIndex, int toIndex) {
        List<IncomingLog> list = new ArrayList<>();

        try 
            (
        		Connection conn =  dbCredentials.getConnection();
    		    PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM incomingLog WHERE recipientuuid = ?"
        		+ "ORDER BY logTime DESC LIMIT ? OFFSET ? ;");
            ) {
        	
        	pstmt.setString(1, account.getUuid());
        	pstmt.setInt(2, toIndex - fromIndex);
        	pstmt.setInt(3, fromIndex);
        	
        	try(ResultSet rset = pstmt.executeQuery();) {
        		list = b.toBeanList(rset, IncomingLog.class);
        	}
        } 
        
        catch (SQLException e) {
        	
            logger.error("SQLException while getting incomingLog from index "
                    + fromIndex + " to index " + toIndex + ".");
            logger.error(ExceptionUtils.getStackTrace(e));

        }

        return list;
    }
    


	/**
	 * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleIncomingLogDAO#putIncomingLog(ke.co.tawi.babblesms.server.beans.log.IncomingLog)
	 */
	@Override
	public boolean putIncomingLog(IncomingLog incomingLog) {
		boolean success = true;

        Connection conn = null;
        PreparedStatement pstmt = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("INSERT INTO IncomingLog (Uuid,origin, destination, message, logtime)"
                    + " VALUES (?,?,?,?,?);");
            pstmt.setString(1, incomingLog.getUuid());
            pstmt.setString(2, incomingLog.getOrigin());
            pstmt.setString(3, incomingLog.getDestination());
            pstmt.setString(4, incomingLog.getMessage());
            pstmt.setTimestamp(5, new Timestamp(incomingLog.getLogTime().getTime()));
            
            pstmt.execute();

        } catch (SQLException e) {
            logger.error("SQLException when trying to put: " + incomingLog);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
            
        } finally {
            if (pstmt != null) {
                try { pstmt.close(); } catch (SQLException e) { }
            }
            
            if (conn != null) {
                try { conn.close(); } catch (SQLException e) { }
            }
        }
        
        return success;
	}

}
