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

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

/**
 * Persistence implementation for {@link Group}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class GroupDAO extends GenericDAO implements BabbleGroupDAO {

    private static GroupDAO GroupDAO;

    private final Logger logger;
    
    BeanProcessor b = new BeanProcessor();

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
    public GroupDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }
    

  
/**
 * This method is used for fetching a group given the group UUID
 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleGroupDAO#getGroup(java.lang.String)
 * @param uuid -the uuid given to every group and can explicitly identify each group object.
 * @return Group - return a group object, the one specified by the uuid passed.
 */
@Override
   public Group getGroup(String uuid) {
       Group group = null;

       ResultSet rset = null;
        
       try(
    		   Connection conn = dbCredentials.getConnection();
    		   PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE Uuid = ?;");
    	  ){
    	   pstmt.setString(1, uuid);
           rset = pstmt.executeQuery();

           if (rset.next()) {
               group = b.toBean(rset, Group.class);
           }
       }

       catch (SQLException e) {
           logger.error("SQL Exception when getting Group with uuid: " + uuid);
           logger.error(ExceptionUtils.getStackTrace(e));
       } 
       return group;
   }

/**
 * This method is used for fetching a group given the group name
 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleGroupDAO#getAGroup(java.lang.String)
 * @param name -the name given to every group and can explicitly identify each group object.
 * @return Group - return a group object, the one specified by the name passed.
 */

@Override
public Group getGroupByName(Account account , String groupname){
	Group group1 = null;
    ResultSet rset = null;
    
    try(
    		Connection conn = dbCredentials.getConnection();
    		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE name = ? and accountuuid = ?;");
       ){
    	pstmt.setString(1, groupname);
    	pstmt.setString(2, account.getUuid());
        rset = pstmt.executeQuery();

        if (rset.next()) {
            group1 = b.toBean(rset, Group.class);
        }
    }

    catch (SQLException e) {
        logger.error("SQL Exception when getting Group with name: " + groupname);
        logger.error(ExceptionUtils.getStackTrace(e));
    } 
	return group1;
}

	/**
	 * method to get a list of groups belonging to a given account
	 * 
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleGroupDAO#getGroups(ke.co.tawi.babblesms.server.beans.account.Account)
	 * 
	 * @return a list of groups belonging to the specified account
	 */
	@Override
	public List<Group> getGroups(Account account) {
		List<Group> groupList = new ArrayList<Group>();
		ResultSet rset = null;
		
		try(
			Connection conn = dbCredentials.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM groups WHERE accountuuid = ? ORDER BY NAME ASC;");	
		   ){
			pstmt.setString(1, account.getUuid());
			
			rset = pstmt.executeQuery();
			
			 groupList = b.toBeanList(rset, Group.class);
		}
		catch (SQLException e) {
	           logger.error("SQL Exception when getting groups that belong to: " + account);
	           logger.error(ExceptionUtils.getStackTrace(e));
	       }
		
     return groupList;
	}

	/**
	 * method to update group tables given the group uuid and a group object
	 * 
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleGroupDAO#updateGroup(java.lang.String, ke.co.tawi.babblesms.server.beans.contact.Group)
	 * 
	 *@return true if the group updates successfully otherwise false 
	 */
	@Override
	public boolean updateGroup(String uuid, Group group) {
		// TODO Auto-generated method stub
		boolean success = true;
		
		try(
				Connection conn = dbCredentials.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("UPDATE groups SET name =? , description =? , statusuuid = ? WHERE uuid =?;");	
		   ){
			pstmt.setString(1, group.getName());
			pstmt.setString(2, group.getDescription());
			pstmt.setString(3, group.getStatusuuid());
			pstmt.setString(4, uuid);
			
			 pstmt.executeUpdate();
				
			}
		catch (SQLException e) {
            logger.error("SQL Exception when trying to update: " + group + "on groups table on row with uuid:" + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        }
		
        return success;
	}
		

    
    /**
     * method to put a new group into the group table
     * 
     * @see ke.co.tawi.babblesms.server.persistence.contacts.BabbleGroupDAO#putGroup(ke.co.tawi.babblesms.server.beans.contact.Group)
     * 
     * @return true if the group object is successfully inserted else returns false
     */
    @Override
    public boolean putGroup(Group group) {
        boolean success = true;

        try(
        		Connection conn = dbCredentials.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement("INSERT INTO groups (Uuid,name,descriptioneationdate,cr,accountuuid,statusuuid) VALUES (?,?,?,?,?,?);");	
        	   ){
        	 pstmt.setString(1, group.getUuid());
             pstmt.setString(2, group.getName());
             pstmt.setString(3, group.getDescription());
             
             //convert java.util.Date to java.sql.Date
             Date sqlDate = new java.sql.Date(group.getCreationdate().getTime());
             pstmt.setDate(4, sqlDate);
             pstmt.setString(5, group.getAccountsuuid());
             pstmt.setString(6, group.getStatusuuid());

             pstmt.execute();
	
        	}
        catch (SQLException e) {
            logger.error("SQL Exception when trying to put: " + group);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        return success;
    }
    
}

