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
package ke.co.tawi.babblesms.server.persistence.contacts;

import ke.co.tawi.babblesms.server.beans.contact.Email;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;

import org.apache.log4j.Logger;


/**
 * Persistence implementation for an {@link Email}.
 * <p>
 *  
 * @author <a href="mailto:wambua@tawi.mobi">Godfrey Wambua</a>
 */
public class EmailDAO extends GenericDAO implements BabbleEmailDAO {

    private static EmailDAO emailDAO;

    private BeanProcessor beanProcessor  = new BeanProcessor();
   
    private final Logger logger;

   
    /**
     * @return the singleton instance of {@link EmailDAO}
     */
    public static EmailDAO getInstance() {
        if (emailDAO == null) {
            emailDAO = new EmailDAO();
        }
        return emailDAO;
    }
   

    /**
     *
     */
    protected EmailDAO() {
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
    public EmailDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }
   
   
    /**
    *
    * @param uuid
    * @return   an {@link Email}
    */
   @Override
   public Email getEmail(String uuid){
         Email email = null;
       
         try (
	         Connection conn = dbCredentials.getConnection();
	         PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Email WHERE uuid = ?;");         
	         ) {
             
                  pstmt.setString(1, uuid);
                 
                  try (ResultSet rset = pstmt.executeQuery();) {
                      if (rset.next()) {
                              email = beanProcessor.toBean(rset, Email.class);
                      }
                  }
                 
         } catch (SQLException e) {
             logger.error("SQL Exception when getting email with uuid: " + uuid);
             logger.error(ExceptionUtils.getStackTrace(e));
         }
         
         return email;         
   }
   
   
   /**
   *
   * @param text
   * @return a list of {@link Email}s which match the text, either
   * partially or wholly.
   */
   @Override
   public List<Email> getEmails(String text, Contact contact){
       List<Email> emails = new ArrayList<>();

       try(
           Connection conn = dbCredentials.getConnection();
           PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Email WHERE contactuuid = ? and address LIKE '%text%';");)
           {
   
               pstmt.setString(1, contact.getUuid());
               
               try(ResultSet rset = pstmt.executeQuery();) {
                         if (rset.next()) {
                         emails = beanProcessor.toBeanList(rset, Email.class);
                     }
               }
       }
   
       catch (SQLException e) {
           logger.error("SQL Exception when getting email with text '" + text
                           + "' for " + contact);
           logger.error(ExceptionUtils.getStackTrace(e));
       }
       
       return emails;
   }
   
   
   /**
   *
   * @param contact
   * @return    a list of {@link Email}s belonging to this contact
   */
   @Override
   public List<Email> getEmails(Contact contact){
	   List<Email> emails = new ArrayList<>();

       try( 
    		   Connection conn = dbCredentials.getConnection();
    		   PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Email WHERE contactuuid=?;");
       ) {
           
        pstmt.setString(1, contact.getUuid());
	        try(ResultSet rset = pstmt.executeQuery();) {  	           
	
	        	emails = beanProcessor.toBeanList(rset, Email.class);
	    	}
	        
       } catch (SQLException e) {
           logger.error("SQL Exception when getting all emails of " + contact);
           logger.error(ExceptionUtils.getStackTrace(e));
       }
       
       return emails;           
  }
   
   
   /**
   *
   * @param email
   * @return            <code>true</code> if successfully inserted; <code>false</code>
   * for otherwise
   */
   @Override
  public boolean putEmail(Email email){
      boolean success = true;

      try (
          Connection conn = dbCredentials.getConnection();
          PreparedStatement pstm = conn.prepareStatement("INSERT INTO Email (Uuid, address, contactuuid,"
                        + "statusuuid) VALUES (?,?,?,?);");)
                  {
          pstm.setString(1, email.getUuid());
          pstm.setString(2, email.getAddress());
          pstm.setString(3, email.getContactuuid());
          pstm.setString(4, email.getStatusuuid());

          pstm.execute();

      } catch (SQLException e) {
          logger.error("SQL Exception when trying to put " + email);
          logger.error(ExceptionUtils.getStackTrace(e));
          success = false;
      }
     
      return success;
  }
   
   
  /**
  *
  * @param uuid
  * @param email
  * @return             <code>true</code> if successfully updated; <code>false</code> for
  * otherwise
  */
  @Override
  public boolean updateEmail(String uuid, Email email){
     boolean success = true;

     try (
	     Connection conn = dbCredentials.getConnection();
	     PreparedStatement pstmt = conn.prepareStatement("UPDATE Email SET uuid=?, address=?, statusuuid=? WHERE Uuid = ?;");) {
                 pstmt.setString(1, email.getUuid());
                 pstmt.setString(2, email.getAddress() );
                 pstmt.setString(3, email.getStatusuuid());
                 pstmt.setString(4, uuid);
                 pstmt.executeUpdate();

     } catch (SQLException e) {
         logger.error("SQL Exception when updating " + email + " with uuid " + uuid);
         logger.error(ExceptionUtils.getStackTrace(e));
         success = false;
     }
     
     return success;         
  }

}
