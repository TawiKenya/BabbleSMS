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

import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Persistence implementation for a Contact - Group.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class ContactGroupDAO extends GenericDAO implements BabbleContactGroupDAO {

    private static ContactGroupDAO contactGroupDAO;    

    private ContactDAO contactDAO;
    private GroupDAO groupDAO; 

    private Logger logger = LogManager.getLogger(this.getClass());

    
    /**
     * @return a singleton instance of {@link ContactGroupDAO}
     */
    public static ContactGroupDAO getInstance() {
        if (contactGroupDAO == null) {
            contactGroupDAO = new ContactGroupDAO();
        }
        
        return contactGroupDAO;
    }

    
    /**
     *
     */
    protected ContactGroupDAO() {
        super();
        contactDAO = ContactDAO.getInstance();
        groupDAO = GroupDAO.getInstance();

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
    public ContactGroupDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);

        contactDAO =  new ContactDAO(dbName, dbHost, dbUsername, dbPassword, dbPort);
        groupDAO = new  GroupDAO(dbName, dbHost, dbUsername, dbPassword, dbPort);

        logger = LogManager.getLogger(this.getClass());
    }
    
    
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactGroupDAO#putContact(ke.co.tawi.babblesms.server.beans.contact.Contact, ke.co.tawi.babblesms.server.beans.contact.Group)
	 * 
	 * @return <code>true if the relationship can be established.</code>, <code> false otherwise</code>
	 */
	@Override
	public boolean putContact(Contact contact, Group group) {
		 boolean success = true;
		 
	        try (
	            Connection conn = dbCredentials.getConnection();
	        	PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ContactGroup "
	        			+ "(Uuid, contactuuid, groupuuid, accountuuid) VALUES (?,?,?,?);");
	        	) {
		        	pstmt.setString(1, UUID.randomUUID().toString());
		            pstmt.setString(2, contact.getUuid());
		            pstmt.setString(3, group.getUuid());
		            pstmt.setString(4, contact.getAccountUuid());

	
		            pstmt.execute();

	        } catch (SQLException e) {
	            logger.error("SQL Exception when trying to put " + contact + " into " + group);
	            logger.error(ExceptionUtils.getStackTrace(e));
	            success = false;
	        }
	        
	        return success;
    }


	/**
	 * 
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactGroupDAO#removeContact(ke.co.tawi.babblesms.server.beans.contact.Contact, ke.co.tawi.babblesms.server.beans.contact.Group)
	 * 
	 */
	@Override
	public boolean removeContact(Contact contact, Group group) {
		 boolean success = true;

        try (
        	Connection conn = dbCredentials.getConnection();
        	PreparedStatement pstmt = conn.prepareStatement("DELETE FROM ContactGroup WHERE contactuuid = ? "
            		+ "AND groupuuid = ?;");) {
            pstmt.setString(1, contact.getUuid());
            pstmt.setString(2, group.getUuid());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting "	+ contact + " from " + group);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        
        return success;
    }


	/**
	 * method to get contacts from the contact group relationship given a group object 
	 * 
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactGroupDAO#getContacts(ke.co.tawi.babblesms.server.beans.contact.Group)
	 * 
	 * @return a list of contacts associated with the given group object
	 */
	@Override
	public List<Contact> getContacts(Group group) {		
		List<Contact> contactList = new LinkedList<>();		
		Contact ct;		
		
		try (
			   Connection conn = dbCredentials.getConnection();
			   PreparedStatement pstmt = conn.prepareStatement("SELECT contactuuid FROM contactgroup WHERE groupuuid = ?;");
			) {
			
	           pstmt.setString(1, group.getUuid());
	           
	           try(ResultSet rset = pstmt.executeQuery();){
		           
		           while(rset.next()){
		        	   ct = contactDAO.getContact(rset.getString("contactuuid"));
		        	   contactList.add(ct);
		           }
	           }	           
	           
           } catch (SQLException e) {
	           logger.error("SQL Exception when getting contacts belonging to " + group);
	           logger.error(ExceptionUtils.getStackTrace(e));
	       } 
		
        return contactList;
	}
           

	/**
	 * method to get contacts from the contact group relationship given a group object 
	 * 
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactGroupDAO#getContacts(ke.co.tawi.babblesms.server.beans.contact.Group, int pagesize)
	 * 
	 * @return a list of contacts associated with the given group object
	 */
	@Override
	public List<Contact> getContacts(Group group, int fromIndex, int toIndex) {
		List<Contact> contactList = new LinkedList<>();
		Contact ct;
				
		try (
			   Connection conn = dbCredentials.getConnection();
			   PreparedStatement pstmt = conn.prepareStatement("SELECT contactuuid FROM contactgroup WHERE groupuuid = ?"
			   		+ "LIMIT ? OFFSET ? ;");
			)
		   {
	           pstmt.setString(1,group.getUuid());
	           pstmt.setInt(2, toIndex - fromIndex);
			   pstmt.setInt(3, fromIndex);
			   
	           try(ResultSet rset = pstmt.executeQuery();){
		           
		           while(rset.next()){
		        	   ct = contactDAO.getContact(rset.getString("contactuuid"));
		        	   contactList.add(ct);
		           }
	           }
	           
	           
           } catch (SQLException e) {
	           logger.error("SQL Exception when getting contacts belonging to " + group);
	           logger.error(ExceptionUtils.getStackTrace(e));
	       }
		
        return contactList;
	}

	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactGroupDAO#getGroups(ke.co.tawi.babblesms.server.beans.contact.Contact)
	 */
	@Override
	public List<Group> getGroups(Contact contact) {		
		List<Group> groupList = new LinkedList<>();		
		Group grp;
		
		try (
			Connection conn = dbCredentials.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT groupuuid FROM contactgroup "
					+ "WHERE contactuuid = ? AND accountuuid=?;");
           ) {
	           pstmt.setString(1,contact.getUuid());
	           pstmt.setString(2, contact.getAccountUuid());
	           
	           try(ResultSet rset = pstmt.executeQuery();) {
	           
		           while(rset.next()){
		        	   grp = groupDAO.getGroup(rset.getString("groupuuid"));
		        	   groupList.add(grp);
		           }
	           }
	           
           } catch (SQLException e) {
        	 
	           logger.error("SQL Exception when getting groups belonging to " + contact );
	           logger.error(ExceptionUtils.getStackTrace(e));
	       } 		
		
		Collections.sort(groupList, Group.GroupNameComparator);
        return groupList;	
	}
}
