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
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;


/**
 * Persistence implementation for a Contact - Group.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class ContactGroupDAO extends GenericDAO implements BabbleContactGroupDAO {

    private static ContactGroupDAO contactGroupDAO;

    private BeanProcessor beanProcessor  = new BeanProcessor();
    
    private final Logger logger;

    
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
    public ContactGroupDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }
    
    
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactGroupDAO#putContact(ke.co.tawi.babblesms.server.beans.contact.Contact, ke.co.tawi.babblesms.server.beans.contact.Group)
	 * 
	 * @return <code>true if the relationship can be established.</code>
	 *            <code> false otherwise</code>
	 */
	@Override
	public boolean putContact(Contact contact, Group group) {
		 boolean success = true;
		 
	        try (
	            Connection conn = dbCredentials.getConnection();
	        	PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ContactGroup "
	        			+ "(Uuid, contactuuid, groupuuid, accountuuid) VALUES (?,?,?,?);");)
	        		{
	        	pstmt.setString(1, UUID.randomUUID().toString());
	            pstmt.setString(2, contact.getUuid());
	            pstmt.setString(3, group.getUuid());
	            pstmt.setString(4, contact.getAccountUuid());

	            pstmt.execute();

	        } catch (SQLException e) {
	            logger.error("SQL Exception when trying to put contactGroup with: " + contact 
	            		+ " and: " + group);
	            logger.error(ExceptionUtils.getStackTrace(e));
	            success = false;
	        }
	        
	        return success;
    }


	/**
	 * 
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactGroupDAO#removeContact(ke.co.tawi.babblesms.server.beans.contact.Contact, ke.co.tawi.babblesms.server.beans.contact.Group)
	 * 
	 * @return true if the relationship is successfully removed and false otherwise
	 */
	@Override
	public boolean removeContact(Contact contact, Group group) {
		 boolean success = true;

        try (
        	Connection conn = dbCredentials.getConnection();
        	PreparedStatement pstmt = conn.prepareStatement("DELETE  FROM ContactGroup WHERE contactuuid = ? "
            		+ "AND groupuuid = ?;");)
            		{
            pstmt.setString(1, contact.getUuid());
            pstmt.setString(2, group.getUuid());

            pstmt.executeUpdate();

        } catch (SQLException e) {
            logger.error("SQL Exception when deleting contactGroup with "
            		+ contact + " and " + group);
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
		
		List<Contact> contactList = new ArrayList<>();
		
		try (
			   Connection conn = dbCredentials.getConnection();
			   PreparedStatement pstmt = conn.prepareStatement("SELECT contactuuid FROM contactgroup WHERE groupuuid = ?;");
			)
		   {
	           pstmt.setString(1,group.getUuid());
	           try(ResultSet rset = pstmt.executeQuery();){
		           
		           while(rset.next()){
		        	   Contact ct = ContactDAO.getInstance().getContact(rset.getString("contactuuid"));
		        	   contactList.add(ct);
		           }
	           }
	           
	           
           } catch (SQLException e) {
	           logger.error("SQL Exception when getting contacts belonging "
	           		+ " to contactgroup with uuid: " + group.getUuid());
	           logger.error(ExceptionUtils.getStackTrace(e));
	       } 
		
        return contactList;
	}


	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleContactGroupDAO#getGroups(ke.co.tawi.babblesms.server.beans.contact.Contact, ke.co.tawi.babblesms.server.beans.account.Account)
	 */
	@Override
	public List<Group> getGroups(Contact contact , Account account ) {		
		List<Group> groupList = new ArrayList<>();
		
		try (
		Connection conn = dbCredentials.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT groupuuid FROM contactgroup WHERE contactuuid = ? and accountuuid= ?;");
           ) {
	           pstmt.setString(1,contact.getUuid());
	           pstmt.setString(2,account.getUuid());
	           
	           try(ResultSet rset = pstmt.executeQuery();){
	           
		           while(rset.next()){
		        	   Group g = GroupDAO.getInstance().getGroup(rset.getString("groupuuid"));
		        	   groupList.add(g);
		           }
	           }
		/* Sort statement*/
	           Collections.sort( groupList, Group.GroupNameComparator );
	           }
	        	  // groupList.add(e);

	        catch (SQLException e) {
	           logger.error("SQL Exception when getting groups belonging "
	           		+ " to contactgroup with contactuuid: " + contact.getUuid());
	           logger.error(ExceptionUtils.getStackTrace(e));
	       } 
		
        return groupList;	
	}

}
