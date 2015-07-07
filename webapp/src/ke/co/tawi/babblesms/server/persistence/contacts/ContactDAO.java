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

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;


/**
 * Persistence implementation for {@link Contact}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class ContactDAO extends GenericDAO implements BabbleContactDAO {

    private static ContactDAO contactDAO;

    private Logger logger = Logger.getLogger(this.getClass());

    private BeanProcessor beanProcessor = new BeanProcessor();
    
    
    /**
     * @return the {@link ContactDAO}
     */
    public static ContactDAO getInstance() {
        if (contactDAO == null) {
            contactDAO = new ContactDAO();
        }
        return contactDAO;
    }

    
    /**
     *
     */
    protected ContactDAO() {
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
    public ContactDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
    }

    
    
	/**
	* @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactDAO#getContactByUuid(java.lang.String)
	*/
   @Override
   public Contact getContact(String uuid) {
       Contact contact = null;

       try (
		   Connection conn = dbCredentials.getConnection();
	       PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Contact WHERE Uuid = ?;");    		   
	   ) {
    	   pstmt.setString(1, uuid);           
	       ResultSet rset = pstmt.executeQuery();
	       
	       if (rset.next()) {
               contact = beanProcessor.toBean(rset, Contact.class);
           }
	       
       } catch (SQLException e) {
           logger.error("SQLException when getting contact with uuid: " + uuid);
           logger.error(ExceptionUtils.getStackTrace(e));
       }
              
       return contact;
   }
    
    
    /**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactDAO#getContactByName(Account, java.lang.String)
	 */
	@Override
	public List<Contact> getContactByName(Account account, String name) {
		List<Contact> list = new ArrayList<>();

        try (
     		   Connection conn = dbCredentials.getConnection();
     	       PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Contact WHERE accountuuid = ? "
     	       		+ "AND name ILIKE ?;");    		   
     	   ) {
         	   pstmt.setString(1, account.getUuid());           
         	   pstmt.setString(2, "%" + name + "%");
         	   try( ResultSet rset = pstmt.executeQuery();){
     	       
     	       list = beanProcessor.toBeanList(rset, Contact.class);
         	   }
        } catch (SQLException e) {
            logger.error("SQLException when getting contacts of " + account  +
            		" and name '" + name +  "'");
            logger.error(ExceptionUtils.getStackTrace(e));
        }
                
        Collections.sort(list);
        return list;
	}
	
	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactDAO#getContacts(ke.co.tawi.babblesms.server.beans.account.Account)
	 */
	@Override
	public List<Contact> getContacts(Account account) {		
		List<Contact> list = new ArrayList<>();

        try (
     		   Connection conn = dbCredentials.getConnection();
     	       PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Contact WHERE accountuuid = ?;");    		   
     	   ) {
         	   pstmt.setString(1, account.getUuid());   
     	       try(ResultSet rset = pstmt.executeQuery();){
     	       
     	       list = beanProcessor.toBeanList(rset, Contact.class);
     	       }  
        } catch (SQLException e) {
            logger.error("SQLException when getting contacts of " + account);
            logger.error(ExceptionUtils.getStackTrace(e));
        }
          
        Collections.sort(list);
        return list;
	}
	
	
    /**
     * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactDAO#putContact(Contact)
     */
    @Override
    public boolean putContact(Contact c) {
        boolean success = true;

        try (
      		   Connection conn = dbCredentials.getConnection();
      	       PreparedStatement pstmt = conn.prepareStatement("INSERT INTO contact "
      	       		+ "(uuid, name, description, accountuuid, statusuuid) VALUES (?,?,?,?,?);");    		   
      	   ) {
          	   	pstmt.setString(1, c.getUuid());   
	            pstmt.setString(2, c.getName());
	            pstmt.setString(3, c.getDescription());
	            pstmt.setString(4, c.getAccountUuid());
	            pstmt.setString(5, c.getStatusUuid());
      	       
	            pstmt.executeUpdate();
	            
         } catch (SQLException e) {
             logger.error("SQLException when trying to put " + c);
             logger.error(ExceptionUtils.getStackTrace(e));
             success = false;
         }
                
        return success;
    }


    /**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactDAO#updateContact(java.lang.String, ke.co.tawi.babblesms.server.beans.contact.Contact)
	 */
	@Override
	public boolean updateContact(String uuid, Contact c) {
		boolean success = true;

        try (
      		   Connection conn = dbCredentials.getConnection();
      	       PreparedStatement pstmt = conn.prepareStatement("UPDATE contact SET name=?, "
      	       		+ "description=?,  statusuuid=? WHERE Uuid=?;");        				   
      	   ) {          	   	  
	            pstmt.setString(1, c.getName());
	            pstmt.setString(2, c.getDescription());
	           // pstmt.setString(3, c.getAccountUuid());
	            pstmt.setString(3, c.getStatusUuid());
	            pstmt.setString(4, c.getUuid()); 
	            
	            pstmt.executeUpdate();
	            
         } catch (SQLException e) {
             logger.error("SQLException when trying to update Contact with UUID '" + uuid + 
            		 "' with "+ c);
             logger.error(ExceptionUtils.getStackTrace(e));
             success = false;
         }
                
        return success;
	}
	
	/**
	 * @param account
	 * @param startIndex The difference between startIndex and endIndex gives <br>
	 * the limit or maximum number of rows that can be returned
	 * @param endIndex determines the offset the number of rows to be skipped <br>
	 * before tuples can be returned
	 * @return <p>
	 * A list of contacts that is from index start index(inclusive) and count<br>
	 * count as many contacts as endIndex - startIndex. 
	 */
	public List<Contact> getContactList (Account account , int startIndex , int endIndex){
		List<Contact> contactList = new ArrayList<>();
		
		try(
				Connection conn = dbCredentials.getConnection();
				PreparedStatement psmt= conn.prepareStatement("SELECT * FROM contact WHERE "
						+ "accountuuid = ? ORDER BY NAME ASC LIMIT ? OFFSET ? ;");
				) {
			psmt.setString(1, account.getUuid());
			psmt.setInt(2, endIndex - startIndex);
			psmt.setInt(3, startIndex);
			
			try(ResultSet rset = psmt.executeQuery();){
			
			 contactList = beanProcessor.toBeanList(rset, Contact.class);
			}
		} catch (SQLException e) {
			logger.error("SQLException when trying to get a Contact List with an index and offset.");
            logger.error(ExceptionUtils.getStackTrace(e));
	    }
		
		return contactList;		
	}
	
	/**
	 * Method to fetch contacts that match the search string
	 * @param contMatcher
	 * @return a list of contacts
	 */
	public List<Contact> getContactListMatch(Account account,String contMatcher){
		List<Contact> contactList = new ArrayList<Contact>();
		ResultSet rset = null;
		BeanProcessor b = new BeanProcessor();
		
		try(
				Connection conn = dbCredentials.getConnection();
				PreparedStatement psmt = conn.prepareStatement("SELECT * FROM contact WHERE accountuuid = ? AND name ILIKE ? ORDER BY NAME ASC LIMIT ? OFFSET ?;");
			){
			psmt.setString(1, account.getUuid());
			psmt.setString(2,"%" + contMatcher + "%");
			psmt.setInt(3, 15);
			psmt.setInt(4, 0);
			rset = psmt.executeQuery();

			contactList = b.toBeanList(rset, Contact.class);
		}
		catch (SQLException e) {
	           logger.error("SQL Exception when getting contacts from table contact that match the "
	           		+ " string : " + contMatcher);
	           logger.error(ExceptionUtils.getStackTrace(e));
	       }
		
		return contactList;
		
	}
}
