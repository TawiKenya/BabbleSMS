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

import ke.co.tawi.babblesms.server.persistence.HibernateUtil;
import ke.co.tawi.babblesms.server.beans.account.Status;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import org.hibernate.Session;
import org.hibernate.query.Query;

/**
 * Persistence implementation for {@link Status}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class StatusDAO extends GenericDAO implements BabbleStatusDAO{
    
    private static StatusDAO statusDAO;

    private final Logger logger = Logger.getLogger(this.getClass());

    private BeanProcessor beanProcessor = new BeanProcessor();
    
    
    /** 
     * @return the StatusDAO
     */
    public static StatusDAO getInstance() {
        if (statusDAO == null) {
            statusDAO = new StatusDAO();
        }
        return statusDAO;
    }

    
    /**
     *
     */
    protected StatusDAO() {
        super();
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
    public StatusDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
    }

    
    /**
	 * @see ke.co.tawi.babblesms.server.persistence.accounts.BabbleStatusDAO#getStatus(java.lang.String)
	 */
   @Override
   public Status getStatus(String uuid) {
       Status status = null;

       Session session = HibernateUtil.getSessionFactory().getCurrentSession();
       session.beginTransaction();
       
       Query<Status> query = session.createQuery("from Status where uuid = :uuid ");
       query.setParameter("uuid", uuid);
       List<Status> list = query.list();
       
       if(list.size() > 0) {
    	   status = list.get(0);
       }
            
       session.getTransaction().commit();
                     
       return status;
   }

   
   /**
   *
   * @param description
   * @return network
   *
   */
  @Override
  public Status getStatusByName(String description) {
	  Status status = null;

      try (
		   Connection conn = dbCredentials.getConnection();
	       PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Status WHERE description = ?;");
	   ) {
    	  
   	   pstmt.setString(1, description);           
   	   ResultSet rset = pstmt.executeQuery();
	       
       if (rset.next()) {
    	   status = beanProcessor.toBean(rset, Status.class);
       }
       
       rset.close();
	       
      } catch (SQLException e) {
          logger.error("SQLException when getting Status with description: " + description);
          logger.error(ExceptionUtils.getStackTrace(e));
      }
      
      return status;      
  }
  
  
  /**
   * @see ke.co.tawi.babblesms.server.persistence.accounts.BabbleStatusDAO#getAllStatus()
   */
  @Override
  public List<Status> getAllStatus() {
      List<Status> list = null;

      try (
    		  Connection conn = dbCredentials.getConnection();
    		  PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Status;");
    		  ResultSet rset = pstmt.executeQuery();
	   ) {
    	        
    	  list = beanProcessor.toBeanList(rset, Status.class);
	       	       
      } catch (SQLException e) {
          logger.error("SQLException when getting all Status");
          logger.error(ExceptionUtils.getStackTrace(e));
      }
            
      return list;
  }
  
  
   
    /**
     * @see ke.co.tawi.babblesms.server.persistence.accounts.BabbleStatusDAO#putStatus(ke.co.tawi.babblesms.server.beans.account.Status)
     */
    @Override
    public boolean putStatus(Status status) {
        boolean success = true;

    	try (
     		   Connection conn = dbCredentials.getConnection();
     	       PreparedStatement pstmt = conn.prepareStatement("INSERT INTO status (uuid, description) VALUES (?,?);");
 	   ) {
    		pstmt.setString(1, status.getUuid());
            pstmt.setString(2, status.getDescription());
            pstmt.execute(); 	       
 	       
       } catch (SQLException e) {
           logger.error("SQLException when trying to put " + status);
           logger.error(ExceptionUtils.getStackTrace(e));
           success = false;
       }
        
        return success;
    }

       

    
    /**
     * @see ke.co.tawi.babblesms.server.persistence.accounts.BabbleStatusDAO#updateStatus(java.lang.String, Status)
     */
    @Override
    public boolean updateStatus(String uuid, Status status) {
        boolean success = true;

        try (
      		   Connection conn = dbCredentials.getConnection();
      	       PreparedStatement pstmt = conn.prepareStatement("UPDATE Status SET description=? WHERE uuid = ?;");
  	   ) {
        	pstmt.setString(1, status.getDescription());
            pstmt.setString(2, uuid);

            pstmt.executeUpdate(); 	       
  	       
        } catch (SQLException e) {
            logger.error("SQLException when trying to update " + status + " with uuid " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        }
        
        return success;
    }
}
