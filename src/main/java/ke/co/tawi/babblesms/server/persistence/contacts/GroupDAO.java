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
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Persistence implementation for {@link Group}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class GroupDAO extends GenericDAO implements BabbleGroupDAO {

    private static GroupDAO GroupDAO;

    private final Logger logger;
    
    private BeanProcessor beanProcessor = new BeanProcessor();

    
    /**
     * @return the singleton instance of {@link GroupDAO}
     */
    public static GroupDAO getInstance() {
        if (GroupDAO == null) {
            GroupDAO = new GroupDAO();
        }
        return GroupDAO;
    }
	
    
    /**
     *
     */
    public GroupDAO() {
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
    public GroupDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = LogManager.getLogger(this.getClass());
    }
  

    /**
     * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleGroupDAO#getGroup(java.lang.String)
     */
    @Override
   public Group getGroup(String uuid) {
       Group group = null;
        
       try(
    		   Connection conn = dbCredentials.getConnection();
    		   PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE Uuid = ?;");
    	  ){
    	   
    	   pstmt.setString(1, uuid);
    	   
    	   try(ResultSet rset = pstmt.executeQuery()) {
    		   if (rset.next()) {
                   group = beanProcessor.toBean(rset, Group.class);
               }
    	   }           
       }

       catch (SQLException e) {
           logger.error("SQL Exception when getting Group with uuid: " + uuid);
           logger.error(ExceptionUtils.getStackTrace(e));
       } 
       
       return group;
   }

    
	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleGroupDAO#getGroupByName(ke.co.tawi.babblesms.server.beans.account.Account, java.lang.String)
	 */
	@Override
	public Group getGroupByName(Account account , String groupname){
		Group group1 = null;
	    
	    
	    try(
	    		Connection conn = dbCredentials.getConnection();
	    		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE name = ? and accountuuid = ?;");
	       ){
	    	
	    	pstmt.setString(1, groupname);
	    	pstmt.setString(2, account.getUuid());
	        
	        try(ResultSet rset = pstmt.executeQuery()) {
	        	if (rset.next()) {
		            group1 = beanProcessor.toBean(rset, Group.class);
		        }
	        }
	        
	    } catch (SQLException e) {
	        logger.error("SQL Exception when getting Group with name: " + groupname + " for " + account);
	        logger.error(ExceptionUtils.getStackTrace(e));
	    } 
	    
	    return group1;
	}
	

	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleGroupDAO#getGroups(ke.co.tawi.babblesms.server.beans.account.Account)
	 */
	@Override
	public List<Group> getGroups(Account account) {
		List<Group> groupList = new ArrayList<>();
		
		try(
			Connection conn = dbCredentials.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE accountuuid = ? ORDER BY NAME ASC;");	
		   ){
			pstmt.setString(1, account.getUuid());
			
			try(ResultSet rset = pstmt.executeQuery();) {
				groupList = beanProcessor.toBeanList(rset, Group.class);
			}			
			 
		} catch (SQLException e) {
           logger.error("SQL Exception when getting groups that belong to: " + account);
           logger.error(ExceptionUtils.getStackTrace(e));
       }
		
		return groupList;
	}

	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleGroupDAO#getAllGroups()
	 */
	@Override
	public List<Group> getAllGroups() {
		List<Group> groupList = new ArrayList<>();
		
		try(
			Connection conn = dbCredentials.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups;");	
			ResultSet rset = pstmt.executeQuery();
		   ){			
				groupList = beanProcessor.toBeanList(rset, Group.class);						
			 
		} catch (SQLException e) {
           logger.error("SQL Exception when getting all groups.");
           logger.error(ExceptionUtils.getStackTrace(e));
       }
		
		return groupList;
	}
	
	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleGroupDAO#updateGroup(java.lang.String, ke.co.tawi.babblesms.server.beans.contact.Group)
	 */
	@Override
	public boolean updateGroup(String uuid, Group group) {
		boolean success = true;
		
		try(
				Connection conn = dbCredentials.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("UPDATE groups SET name=?, description=?, "
						+ "statusuuid=? WHERE uuid=?;");	
		   ){
			
			pstmt.setString(1, group.getName());
			pstmt.setString(2, group.getDescription());
			pstmt.setString(3, group.getStatusuuid());
			pstmt.setString(4, uuid);
			
			pstmt.executeUpdate();
				
		} catch (SQLException e) {
            logger.error("SQL Exception when trying to update " + group + "on groups table with uuid: " + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        }
		
        return success;
	}		

    
    
    /**
     * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleGroupDAO#putGroup(ke.co.tawi.babblesms.server.beans.contact.Group)
     */
    @Override
    public boolean putGroup(Group group) {
        boolean success = true;

        try(
        		Connection conn = dbCredentials.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO groups (Uuid, name, description,"
        				+ "creationdate, accountuuid, statusuuid) VALUES (?,?,?,?,?,?);");	
        	   ){
        	
        	 pstmt.setString(1, group.getUuid());
             pstmt.setString(2, group.getName());
             pstmt.setString(3, group.getDescription());             
             pstmt.setDate(4, new Date(group.getCreationdate().getTime()));
             pstmt.setString(5, group.getAccountsuuid());
             pstmt.setString(6, group.getStatusuuid());

             pstmt.execute();
	
    	} catch (SQLException e) {
            logger.error("SQL Exception when trying to put: " + group);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        
        return success;
    }

    
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleGroupDAO#getGroupCount(Account)
	 */
	@Override
	public Map<String, Integer> getGroupCount(Account account) {
		Map<String, Integer> groupMap = new HashMap<>();
		
		List<String> groupUuids = new LinkedList<>();
		
		try(
			Connection conn = dbCredentials.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE accountuuid = ? ORDER BY NAME ASC;");
			PreparedStatement pstmt2 = conn.prepareStatement("select count(*) from contactgroup where groupuuid=?;");
		   ){
			
			// First get a list of all Group UUIDs belonging to this account
			pstmt.setString(1, account.getUuid());
			
			try(ResultSet rset = pstmt.executeQuery();) {
				while(rset.next()) {
					groupUuids.add(rset.getString("uuid"));
				}
			}		
			
			
			// Then get a count of Contacts per Group
			for(String uuid : groupUuids) {
				pstmt2.setString(1, uuid);
				
				try(ResultSet rset = pstmt2.executeQuery();) {
					rset.next();
					groupMap.put(uuid, new Integer(rset.getInt(1)));					
				}	
			}
			 
		} catch (SQLException e) {
           logger.error("SQL Exception when getting a count of groups that belong to: " + account);
           logger.error(ExceptionUtils.getStackTrace(e));
       }				
		
		return groupMap;
	}
    
}

