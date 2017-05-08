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

import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.util.ArrayList;
import java.util.List;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Utility that can reset the dates of Incoming and Outgoing SMS.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class DateResetUtil extends GenericDAO {

	private static DateResetUtil resetUtil;

    private final Logger logger = LogManager.getLogger(this.getClass());
    
    /**
     *
     * @return {@link CountUtils}
     */
    public static DateResetUtil getInstance() {
        if (resetUtil == null) {
            resetUtil = new DateResetUtil();
        }

        return resetUtil;
    }

    
    /**
    *
    */
   protected DateResetUtil() {
       super();
   }
   
   
	/**
	 * @param dbName
	 * @param dbHost
	 * @param dbUsername
	 * @param dbPassword
	 * @param dbPort
	 */
	public DateResetUtil(String dbName, String dbHost, String dbUsername,
			String dbPassword, int dbPort) {
		super(dbName, dbHost, dbUsername, dbPassword, dbPort);
	}

	
	/**
	 * Reset dates for Incoming SMS.
	 * 
	 * @param startDate A start date in the format yyyy-MM-dd HH:mm:ss
	 * @param endDate An end date in the format yyyy-MM-dd HH:mm:ss
	 */
	public void resetIncomingDates(String startDate, String endDate) {
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Example 2011-06-01 00:16:45" 
		List<String> uuids = new ArrayList<>();
		
		RandomDataGenerator generator = new RandomDataGenerator();
		
		// Get UUIDs of Incoming SMS are in the database
		try (
			   Connection conn = dbCredentials.getConnection();
		       PreparedStatement pstmt = conn.prepareStatement("SELECT uuid FROM incominglog;");    		   
		   ) {	    	             
		       ResultSet rset = pstmt.executeQuery();
		       		       		       
		       while(rset.next()) {
		    	   uuids.add(rset.getString("uuid"));
		       }
	            		       
	       } catch (SQLException e) {
	           logger.error("SQLException when getting uuids of incominglog.");
	           logger.error(ExceptionUtils.getStackTrace(e));
	       }
		
		
		
		try (
	  		   Connection conn = dbCredentials.getConnection();
	  	       PreparedStatement pstmt = conn.prepareStatement("UPDATE incominglog SET logTime=? "
	  	       		+ "WHERE Uuid=?;");        				   
	  	   ) {    
			long start = dateFormatter.parse(startDate).getTime();
			long end = dateFormatter.parse(endDate).getTime();
						
			for(String uuid : uuids) {
				pstmt.setTimestamp(1, new Timestamp(generator.nextLong(start, end)));
				pstmt.setString(2, uuid);
				
				pstmt.executeUpdate();
			}	           
			
	     } catch (SQLException e) {
	         logger.error("SQLException when trying to reset dates of incomingLog.");
	         logger.error(ExceptionUtils.getStackTrace(e));
	         
	     } catch (ParseException e) {			
	    	 logger.error("ParseException when trying to reset dates of incomingLog.");
	         logger.error(ExceptionUtils.getStackTrace(e));
		}		
	}
	
	
	/**
	 * Reset dates for Outgoing SMS.
	 * 
	 * @param startDate A start date in the format yyyy-MM-dd HH:mm:ss
	 * @param endDate An end date in the format yyyy-MM-dd HH:mm:ss
	 */
	public void resetOutgoingDates(String startDate, String endDate) {
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // Example 2011-06-01 00:16:45" 
		List<String> uuids = new ArrayList<>();
		
		RandomDataGenerator generator = new RandomDataGenerator();
		
		// Get UUIDs of Incoming SMS are in the database
		try (
			   Connection conn = dbCredentials.getConnection();
		       PreparedStatement pstmt = conn.prepareStatement("SELECT uuid FROM outgoinglog;");    		   
		   ) {	    	             
		       ResultSet rset = pstmt.executeQuery();
		       		       		       
		       while(rset.next()) {
		    	   uuids.add(rset.getString("uuid"));
		       }
	            		       
	       } catch (SQLException e) {
	           logger.error("SQLException when getting uuids of outgoinglog.");
	           logger.error(ExceptionUtils.getStackTrace(e));
	       }
				
		try (
	  		   Connection conn = dbCredentials.getConnection();
	  	       PreparedStatement pstmt = conn.prepareStatement("UPDATE outgoinglog SET logTime=? "
	  	       		+ "WHERE Uuid=?;");        				   
	  	   ) {    
			long start = dateFormatter.parse(startDate).getTime();
			long end = dateFormatter.parse(endDate).getTime();
						
			for(String uuid : uuids) {
				pstmt.setTimestamp(1, new Timestamp(generator.nextLong(start, end)));
				pstmt.setString(2, uuid);
				
				pstmt.executeUpdate();
			}	           
			
	     } catch (SQLException e) {
	         logger.error("SQLException when trying to reset dates of outgoinglog.");
	         logger.error(ExceptionUtils.getStackTrace(e));
	         
	     } catch (ParseException e) {			
	    	 logger.error("ParseException when trying to reset dates of outgoinglog.");
	         logger.error(ExceptionUtils.getStackTrace(e));
		}		
		
	}
	
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String dbName = "babblesmsdb";
	    String dbHost = "localhost";
	    String dbUsername = "babblesms";
	    String dbPassword = "Hymfatsh8";
	    int dbPort = 5432;
	    
		DateResetUtil util = new DateResetUtil(dbName, dbHost, dbUsername, dbPassword, dbPort);
		
		System.out.println("Have started resetting dates, in progress ...");
		
		// Below is the Start Date and End Date in format yyyy-MM-dd HH:mm:ss
		util.resetIncomingDates("2015-01-20 00:00:00", "2015-05-07 15:00:00"); 
		//util.resetOutgoingDates("2015-01-20 00:00:00", "2015-05-07 15:00:00"); 
		
		System.out.println("Have finished resetting dates.");
	}
}

