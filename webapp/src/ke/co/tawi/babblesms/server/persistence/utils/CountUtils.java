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
package ke.co.tawi.babblesms.server.persistence.utils;

import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;
import ke.co.tawi.babblesms.server.beans.account.Account;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Database utilities used for counting.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class CountUtils extends GenericDAO {

	// The UUID of a Message Status that indicates an Outgoing SMS has been sent.
	final String MESSAGESTATUS_SENT = "2F4AF191-8557-86C5-5D72-47DD44D303B1";
	
	private static CountUtils countUtils;

    private final Logger logger = Logger.getLogger(this.getClass());

    /**
     *
     * @return {@link CountUtils}
     */
    public static CountUtils getInstance() {
        if (countUtils == null) {
            countUtils = new CountUtils();
        }

        return countUtils;
    }
    

    /**
     *
     */
    protected CountUtils() {
        super();
    }

    
    /**
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPassword
     * @param dbPort
     */
    public CountUtils(String dbName, String dbHost, String dbUsername, String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
    }

    /**
     * Gets the count of all IncomingLog requests belonging to this account.
     *
     * @param accountuuid
     *
     * @return int total count of incominglog requests
     */
    public int getIncomingLog(String accountuuid) {
        int count = 0;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            conn = dbCredentials.getConnection();

            pstmt = conn.prepareStatement("SELECT count(*) FROM incominglog WHERE recipientuuid = ?;");

            pstmt.setString(1, accountuuid);

            rset = pstmt.executeQuery();
            rset.next();
            count = count + rset.getInt(1);

        } catch (SQLException e) {
            logger.error("SQLException exception while getting all messages count of account with uuid '"
                    + accountuuid + "'");
            logger.error(ExceptionUtils.getStackTrace(e));

        } finally {
            if (rset != null) {
                try {
                    rset.close();
                } catch (SQLException e) {
                }
            }

            if (pstmt != null) {
                try {
                    pstmt.close();
                } catch (SQLException e) {
                }
            }

            if (conn != null) {
                try {
                    conn.close();
                } catch (SQLException e) {
                }
            }
        }

        return count;
    }
    
    
    /**
     * Gets the count of all IncomingLog requests belonging to this account.
     *
     * @param accountuuid
     *
     * @return total count of incominglog requests
     */
    public int getIncomingCount(String accountuuid) {
        int count=0;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            conn = dbCredentials.getConnection();

            pstmt = conn.prepareStatement("SELECT count(*) FROM incominglog WHERE recipientuuid = ?;");
            pstmt.setString(1, accountuuid);

            rset = pstmt.executeQuery();
            rset.next();
            count = count + rset.getInt(1);

        } catch (SQLException e) {
            logger.error("SQLException while getting all incoming SMS count of account with uuid '"
                    + accountuuid + "'");
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

        return count;
    }

    /**
     * Gets the count of all incoming USSD sessions belonging to this account
     * holder as specified by the email and based on the network that they came
     * from.
     * <p>
     * If the account has more than one USSD short code, then the count is the
     * total sum of sessions for each short code that belongs to it.
     * <br>
     * <b>Note:</b> This method assumes that a particular short code can only be
     * held by one account holder.
     *
     * @param accountuuid
     * @param network
     * @return  an integer
     */
    public int getIncomingCount(String accountuuid, Network network) {
        int count = 0;

        try(
    		Connection conn = dbCredentials.getConnection();
            PreparedStatement  pstmt = conn.prepareStatement("SELECT COUNT(*) FROM incomingLog WHERE "
                		+ "networkuuid=? AND recipientuuid=?; ");  
    		) {
                     
	            pstmt.setString(1, network.getUuid());
	            pstmt.setString(2, accountuuid);
	
	            try(ResultSet rset = pstmt.executeQuery();) {
	
		            if (rset.next()) {
		                count = rset.getInt(1);
		            }
	            }
            
        } catch (SQLException e) {
            logger.error("SQLException while getting all incoming SMS count of account with uuid '"
                    + accountuuid + "' and of '" + network + "'.");
            logger.error(ExceptionUtils.getStackTrace(e));

        } 
        
        return count;
    }

    
    /**
     * Gets the count of all incoming USSD belonging to this account holder and
     * from a particular network, between a time interval.
     * <p>
     * Note: This method assumes that a particular short code can only be held
     * by one account holder.
     *
     * @param accountuuid
     * @param network
     * @param startTime
     * @param endTime
     * @return  an integer
     */
    public int getIncomingCount(String accountuuid, Network network, Date startTime, Date endTime) {
        int count = 0;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            conn = dbCredentials.getConnection();

            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM incomingLog WHERE destination "
                    + "IN (SELECT uuid FROM shortcode WHERE networkUuid = ? AND accountUuid = ?) "
                    + "AND logTime BETWEEN ? AND ?;");
            pstmt.setString(1, network.getUuid());
            pstmt.setString(2, accountuuid);
            pstmt.setTimestamp(3, new Timestamp(startTime.getTime()));
            pstmt.setTimestamp(4, new Timestamp(endTime.getTime()));

            rset = pstmt.executeQuery();

            if (rset.next()) {
                count = count + rset.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("SQLException while getting all incoming SMS count of account with uuid '"
                    + accountuuid + "' and '" + network + "'."
                    + "between " + startTime + " and " + endTime);
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

        return count;
    }
    

    /**
     * Gets the count of all outgoing USSD belonging to this account holder and
     * from a certain source and a particular network
     * <p>
     * Note: This method assumes that a particular short code can only be held
     * by one account holder.
     *
     * @param accountuuid
     * @param network
     * @param startTime
     * @param endTime
     * @return  an integer
     */
    public int getOutgoingCount(String accountuuid, Network network, Date startTime, Date endTime) {
        int count = 0;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            conn = dbCredentials.getConnection();

            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM outgoingLog WHERE networkUuid = ? AND sender = ? "
                    + "AND logTime BETWEEN ? AND ?;");
            pstmt.setString(1, network.getUuid());
            pstmt.setString(2, accountuuid);
            pstmt.setTimestamp(3, new Timestamp(startTime.getTime()));
            pstmt.setTimestamp(4, new Timestamp(endTime.getTime()));

            rset = pstmt.executeQuery();
            if (rset.next()) {
                count = count + rset.getInt(1);
            }

        } catch (SQLException e) {
            logger.error("SQLException while getting all outgoing USSD count of email '"
                    + accountuuid + "' and network '" + network + "' "
                    + "between " + startTime + " and " + endTime);
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
        
        return count;
    }
    

    /**
     * Gets the count of all outgoing sms belonging to this account holder based
     * on the network that they came from.
     *
     * @param accountuuid
     * @param network
     * @return  an integer
     */
    public int getOutgoingCount(String accountuuid, Network network) {
        int count = 0;
        
        try(
        		Connection conn = dbCredentials.getConnection();                
        		PreparedStatement pstmt = conn.prepareStatement("SELECT COUNT(*) FROM outgoingLog WHERE "
        				+ "networkuuid=? AND sender=? AND messagestatusuuid=?;");	
    		) {        	
            
            pstmt.setString(1, network.getUuid());
            pstmt.setString(2, accountuuid);
            pstmt.setString(3, MESSAGESTATUS_SENT);
            
            try(ResultSet rset = pstmt.executeQuery();) {
            	
	            if (rset.next()) {
	                count = rset.getInt(1);
	            }
            }

        } catch (SQLException e) {
            logger.error("SQLException exception while getting all outgoing SMS count of account with uuid '"
                    + accountuuid + "' and of '" + network + "'.");
            logger.error(ExceptionUtils.getStackTrace(e));

        } 
        
        return count;
    }

    
    /**
     * Gets the count of all outgoing sms belonging to this account holder based
     * on the network that they came from.
     *
     * @param accountuuid
     * @param network
     * @return  an integer
     */
    public int getOutgoingDeliveredCount(String accountuuid, Network network) {
        int count = 0;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            conn = dbCredentials.getConnection();
            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM outgoingLog WHERE networkuuid = ? AND sender = ? "
                    + "where messagestatusuuid='49229BA2-91E5-7E64-F49C-923B7927C40D';");
            pstmt.setString(1, network.getUuid());
            pstmt.setString(2, accountuuid);

            rset = pstmt.executeQuery();
            rset.next();
            count = count + rset.getInt(1);

        } catch (SQLException e) {
            logger.error("SQLException exception while getting all outgoing USSD count of email '"
                    + accountuuid + "' and of network '" + network + "'.");
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
        
        return count;
    }

    
    /**
     * Gets the count of all outgoinglog belonging to this account.
     *
     *
     * @param accountuuid
     * @return int total count of outgoinglog requests
     */
    public int getOutgoingLog(String accountuuid) {
        int count = 0;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            conn = dbCredentials.getConnection();

            pstmt = conn.prepareStatement("SELECT count(*) FROM outgoinglog WHERE sender = ?");
            pstmt.setString(1, accountuuid);

            rset = pstmt.executeQuery();
            rset.next();
            count = count + rset.getInt(1);

        } catch (SQLException e) {
            logger.error("SQLException exception while getting all airtime count of  '"
                    + accountuuid + "' and '" + accountuuid + "'");
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

        return count;
    }

    
    /**
     * Gets the count of all outgoinggrouplog belonging to this account.
     *
     *
     * @param accountuuid
     * @return int total count of outgoinglog requests
     */
    public int getOutgoingGroupLog(String accountuuid) {
        int count = 0;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            conn = dbCredentials.getConnection();

            pstmt = conn.prepareStatement("SELECT count(*) FROM outgoinggrouplog WHERE sender = ?");
            pstmt.setString(1, accountuuid);

            rset = pstmt.executeQuery();
            rset.next();
            count = count + rset.getInt(1);

        } catch (SQLException e) {
            logger.error("SQLException exception while getting all airtime count of  '"
                    + accountuuid + "' and '" + accountuuid + "'");
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

        return count;
    }
    

    /**
     * Gets the count of all outgoinggrouplog belonging to this account.
     *
     *
     * @param groupuuid
     * @return int total count of outgoinglog requests
     */
    public int getCumulativeOutgoingGroup(String groupuuid) {
        int count = 0;
        int groupsmscount = 0;
        int contactscount = 0;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            conn = dbCredentials.getConnection();

            pstmt = conn.prepareStatement("SELECT count(*) FROM outgoinggrouplog WHERE destination = ?");
            pstmt.setString(1, groupuuid);

            rset = pstmt.executeQuery();
            rset.next();
            groupsmscount = groupsmscount + rset.getInt(1);

            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM contactgroup WHERE groupuuid = ?;");
            pstmt.setString(1, groupuuid);

            rset = pstmt.executeQuery();
            rset.next();
            contactscount = contactscount + rset.getInt(1);

            count = contactscount * groupsmscount;

        } catch (SQLException e) {
            logger.error("SQLException exception while getting all airtime count of  '"
                    + groupuuid + "' and '" + groupuuid + "'");
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

        return count;
    }
    

    /**
     * Gets the count of all contacts requests by network operator belonging to
     * this account.
     *
     *
     * @param accountuuid
     * @return total count of Contacts
     */
    public int getContacts(String accountUuid) {
        int count = 0;
        
        try (
     		   Connection conn = dbCredentials.getConnection();
     	       PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM contact WHERE accountuuid = ?");    		   
 	    ) {
        	pstmt.setString(1, accountUuid);           
 	       	ResultSet rset = pstmt.executeQuery();
 	       	
 	       	while(rset.next()){
 	       		count = count + 1;
 	       	}

 	       
        } catch (SQLException e) {
            //logger.error("SQLException when getting count of Account with uuid: " + accountuuid);
            logger.error(ExceptionUtils.getStackTrace(e));
        }
        
        return count;
    }
    

    /**
     * Gets the count of all Groups requests by network operator and status,
     * belonging to this account.
     *
     *
     * @param accountuuid
     * @return int the count of all Groups
     */
    public int getGroups(String accountuuid) {
        int count = 0;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            conn = dbCredentials.getConnection();

            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM groups WHERE accountuuid = ?;");
            pstmt.setString(1, accountuuid);

            rset = pstmt.executeQuery();
            rset.next();
            count = rset.getInt(1);

        } catch (SQLException e) {
            logger.error("SQLException exception while getting all topup of '"
                    + accountuuid + "' and '" + accountuuid + "' and '" + accountuuid + "'.");
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

        return count;
    }

    /**
     * Gets the count of contacts in Group *
     *
     * @param groupuuid
     * @return int the count of all Groups
     */
    public int getContactInGroup(String groupuuid) {
        int count = 0;

        Connection conn = null;
        PreparedStatement pstmt = null;
        ResultSet rset = null;

        try {
            conn = dbCredentials.getConnection();

            pstmt = conn.prepareStatement("SELECT COUNT(*) FROM contactgroup WHERE groupuuid = ?;");
            pstmt.setString(1, groupuuid);

            rset = pstmt.executeQuery();
            rset.next();
            count = rset.getInt(1);

        } catch (SQLException e) {
            logger.error("SQLException exception while getting all topup of '"
                    + groupuuid);
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

        return count;
    }
    
    
    

}
 


