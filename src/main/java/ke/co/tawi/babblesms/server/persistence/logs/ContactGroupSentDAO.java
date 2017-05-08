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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import ke.co.tawi.babblesms.server.beans.log.contactGroupsent;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

/**
 * @author Migwi Ndung'u <code>mailto:<a>migwindungu0@gmail.com</a>
 *</code> 
 *
 * 
 */
public class ContactGroupSentDAO extends GenericDAO implements BabbleContactGroupSentDAO {
	private static ContactGroupSentDAO ContactGroupSentDAO;

    private BeanProcessor beanProcessor = new BeanProcessor();
    
    private final Logger logger;

    
    /**
     * @return the singleton instance of {@link OutgoingLogDAO}
     */
    public static ContactGroupSentDAO getInstance() {
        if (ContactGroupSentDAO == null) {
        	ContactGroupSentDAO = new ContactGroupSentDAO();
        }
        
        return ContactGroupSentDAO;
    }

    
    /**
     *
     */
    protected ContactGroupSentDAO() {
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
    public ContactGroupSentDAO(String dbName, String dbHost, String dbUsername,String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = LogManager.getLogger(this.getClass());
    }

    
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleContactGroupSentDAO#put(ke.co.tawi.babblesms.server.beans.log.contactGroupsent)
	 */
	@Override
	public boolean put(contactGroupsent cGroupsent) {
		boolean success=true;
		try(Connection conn = dbCredentials.getConnection();
			PreparedStatement pstmt = conn.prepareStatement("INSERT INTO contactgroupsent (sentcontactuuid, sentgroupuuid)"
					+ "VALUES(?,?); ");
					){
			pstmt.setString(1, cGroupsent.getSentContactUuid() );
			pstmt.setString(2, cGroupsent.getSentGroupUuid());
			
			pstmt.execute();
			
		} catch (SQLException e) {
            logger.error("SQL Exception when trying to put " + cGroupsent);
            logger.error(ExceptionUtils.getStackTrace(e));
            //System.out.println(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        
        return success;
	}

	/**
	 * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleContactGroupSentDAO#getUsingSentGroup(java.lang.String)
	 */
	@Override
	public List<contactGroupsent> getUsingSentGroup(String SentGroupUuid) {
		List<contactGroupsent> GrpsentList=new ArrayList<>();
		try(
		Connection conn = dbCredentials.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM contactgroupsent WHERE sentgroupuuid=?;" );
			){
			pstmt.setString(1, SentGroupUuid);
		try(ResultSet rset=pstmt.executeQuery();){
			GrpsentList =beanProcessor.toBeanList(rset, contactGroupsent.class);
		    }
			
		}catch (SQLException e) {
            logger.error("SQL Exception when trying to List associated with " + SentGroupUuid);
            logger.error(ExceptionUtils.getStackTrace(e));
		}
		
		return GrpsentList;
	}

	/**
	 * @see ke.co.tawi.babblesms.server.persistence.logs.BabbleContactGroupSentDAO#getUsingSentContact(java.lang.String)
	 */
	@Override
	public List<contactGroupsent> getUsingSentContact(String SentContactUuid) {
		List<contactGroupsent> ContactsentList=new ArrayList<>();
		try(
		Connection conn = dbCredentials.getConnection();
		PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM contactgroupsent WHERE sentcontactuuid=?;" );
			){
			pstmt.setString(1, SentContactUuid);
		try(ResultSet rset=pstmt.executeQuery();){
			ContactsentList =beanProcessor.toBeanList(rset, contactGroupsent.class);
		    }
			
		}catch (SQLException e) {
            logger.error("SQL Exception when trying to List associated with " + SentContactUuid);
            logger.error(ExceptionUtils.getStackTrace(e));
		}
		
		return ContactsentList;
	}

}
