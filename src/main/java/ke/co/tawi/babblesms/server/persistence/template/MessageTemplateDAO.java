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


package ke.co.tawi.babblesms.server.persistence.template;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.messagetemplate.MessageTemplate;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;
import ke.co.tawi.babblesms.server.persistence.template.BabbleMessageTemplateDAO;


/**
 * Copyright (c) Tawi Commercial Services Ltd., April 28, 2015
 *
 * @author <a href="mailto:wambua@tawi.mobi">Godfrey Wambua</a>
 * @version %I%, %G%
 */

public class MessageTemplateDAO extends GenericDAO implements BabbleMessageTemplateDAO {

    private static MessageTemplateDAO messageTemplateDAO;

    private final Logger logger;

    public static MessageTemplateDAO getInstance() {
        if (messageTemplateDAO == null) {
            messageTemplateDAO = new MessageTemplateDAO();
        }
        return messageTemplateDAO;
    }

    /**
     *
     */
    protected MessageTemplateDAO() {
        super();
        logger = LogManager.getLogger(this.getClass());
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
    public MessageTemplateDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = LogManager.getLogger(this.getClass());
    }
    
    /**
    *
    */
   @Override
   public boolean put(MessageTemplate template) {
       boolean success = true;

      

       try(    Connection conn = dbCredentials.getConnection();
    	       PreparedStatement pstmt = conn.prepareStatement("INSERT INTO MessageTemplate (Uuid,title,contents,accountuuid) VALUES (?,?,?,?);");
    		   
           )
      {
           pstmt.setString(1, template.getUuid());
           pstmt.setString(2, template.getTitle());
           pstmt.setString(3, template.getContents());
           pstmt.setString(4, template.getAccountuuid());
           
           System.out.println(template);

           pstmt.execute();

       } catch (SQLException e) {
           logger.error("SQL Exception when trying to put messageTemplate: " + template);
           logger.error(ExceptionUtils.getStackTrace(e));
           success = false;
       } 
       return success;
   }

   /**
   *
   */
  @Override
  public MessageTemplate getTemplate(String uuid) {
      MessageTemplate messageTemplate = null;

      
     
      BeanProcessor b = new BeanProcessor();
      
      try (   
    		  Connection conn = dbCredentials.getConnection();
    	      PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM MessageTemplate WHERE Uuid = ?;");
    	  )
          {
              pstmt.setString(1, uuid);
          
          try(
        	 ResultSet rset = pstmt.executeQuery(); 
        	) {
          if (rset.next()) {
              messageTemplate = b.toBean(rset, MessageTemplate.class);
          }

      } }catch (SQLException e) {
          logger.error("SQL Exception when getting messageTemplate with uuid: " + uuid);
          logger.error(ExceptionUtils.getStackTrace(e));
      } 
      return messageTemplate;
  }

  /**
  *
  */
 @Override
   public List<MessageTemplate> getTemplates(Account account) {
     List<MessageTemplate> list = null;

     
     
     BeanProcessor b = new BeanProcessor();

     try (
    		 Connection conn  = dbCredentials.getConnection();
    		 PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM MessageTemplate WHERE accountuuid=? ORDER BY title ASC;");
    	 )
       {
         
         pstmt.setString(1, account.getUuid());
         try (
        		 ResultSet rset =  pstmt.executeQuery();
        		 )
        		 {

         list = b.toBeanList(rset, MessageTemplate.class);

     } }catch (SQLException e) {
         logger.error("SQL Exception when getting all messageTemplates" + account.getUuid());
         logger.error(ExceptionUtils.getStackTrace(e));
     } 
     return list;
 }

 /**
  * @param uuid
  * @param messageTemplate
  * @return success
  */
 @Override
 public boolean update(MessageTemplate template, String uuid) {
     boolean success = true;

     

     try (
    		 Connection conn = dbCredentials.getConnection();
    	     PreparedStatement pstmt = conn.prepareStatement("UPDATE MessageTemplate SET title=?,contents=? WHERE uuid = ?;");
    	 )
     {
        
         pstmt.setString(1, template.getTitle());
         pstmt.setString(2, template.getContents());
         pstmt.setString(3, uuid);

         pstmt.executeUpdate();

     } catch (SQLException e) {
         logger.error("SQL Exception when deleting"+ template);
         logger.error(ExceptionUtils.getStackTrace(e));
         success = false;
     } 
     return success;
 }

 /**
 *
 */
@Override
public void delete(MessageTemplate template) {
    boolean success = true;

    

    try(
    		Connection conn = dbCredentials.getConnection();
    	    PreparedStatement pstmt = conn.prepareStatement("DELETE FROM MessageTemplate WHERE Uuid = ?;");
    	)
    {
        
        pstmt.setString(1, template.getUuid());

        pstmt.executeUpdate();

    } catch (SQLException e) {
        logger.error("SQL Exception when deleting messageTemplate with uuid " + template.getUuid());
        logger.error(ExceptionUtils.getStackTrace(e));
        success = false;
    }
}
   
    /**
    *
    */
@Override
   public boolean titleExists(String title, Account account) {
	 boolean success = false;

       try (
    		   Connection conn = dbCredentials.getConnection();
    	       PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM MessageTemplate WHERE title = ? and accountuuid= ?;");
    	    )
        { 
           pstmt.setString(1, title);
           pstmt.setString(1, account.getUuid());
           try   (
        		   ResultSet rset = pstmt.executeQuery();
        		  )
        		 {
               if (rset.next()) {
            	   success = true;
                 }

       } }catch (SQLException e) {
           logger.error("SQL Exception when getting messageTemplate with uuid: " + title);
           logger.error(ExceptionUtils.getStackTrace(e));
       } 
       return success;
   }
    
 }






















