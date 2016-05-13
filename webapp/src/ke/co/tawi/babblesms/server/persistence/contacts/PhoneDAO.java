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
import ke.co.tawi.babblesms.server.beans.contact.Phone;
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
 * Persistence implementation for {@link Phone}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class PhoneDAO extends GenericDAO implements BabblePhoneDAO {

    private static PhoneDAO phoneDAO;

    private BeanProcessor beanProcessor = new BeanProcessor();
    
    private final Logger logger;

    
    /**
     * @return the singleton instance of {@link PhoneDAO}
     */
    public static PhoneDAO getInstance() {
        if (phoneDAO == null) {
            phoneDAO = new PhoneDAO();
        }
        
        return phoneDAO;
    }

    
    /**
     *
     */
    protected PhoneDAO() {
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
    public PhoneDAO(String dbName, String dbHost, String dbUsername,
            String dbPassword, int dbPort) {
        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
        logger = Logger.getLogger(this.getClass());
    }
     
   
   /**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabblePhoneDAO#getPhone(java.lang.String)
	 */
   @Override
   public Phone getPhone(String uuid){
	   Phone phone = null;
	   
	   try(
				Connection conn = dbCredentials.getConnection();
				PreparedStatement psmt= conn.prepareStatement("SELECT * FROM phone WHERE "
						+ "uuid = ?;");
				) {
		   
			psmt.setString(1, uuid);
			
			ResultSet rset = psmt.executeQuery();
					
			if(rset.next()){
				 phone = beanProcessor.toBean(rset, Phone.class);	
			}
			
			rset.close();
			
	   } catch (SQLException e) {
		   		logger.error("SQLException when getting phone with uuid: " + uuid);
	            logger.error(ExceptionUtils.getStackTrace(e));
       }
	   
	   return phone;
   }

      
   
   
   /** 
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabblePhoneDAO#getPhones(java.lang.String)
	 **/
	@Override
	public List<Phone> getPhones(String phoneNum) {
		List<Phone> phoneList = new ArrayList<>();		
		
		try(
				Connection conn = dbCredentials.getConnection();
				PreparedStatement psmt = conn.prepareStatement("SELECT * FROM phone WHERE phonenumber ILIKE ? LIMIT 30;");
		   ){
			
			psmt.setString(1, "%" + phoneNum + "%");
			
			ResultSet rset = psmt.executeQuery();
			
			phoneList = beanProcessor.toBeanList(rset, Phone.class);
			
			rset.close();
			
		} catch (SQLException e) {
	           logger.error("SQL Exception when getting phones that match the "
	           		+ " string: " + phoneNum);
	           logger.error(ExceptionUtils.getStackTrace(e));
	    } 

        return phoneList;
	}
   
	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabblePhoneDAO#getPhones(java.lang.String, ke.co.tawi.babblesms.server.beans.contact.Contact)
	 */
	@Override
	public List<Phone> getPhones(String phoneNum, Contact contact) {
		// TODO Auto-generated method stub
		return null;
	}    
	
   
    /**
     *
     * @param phone
     */
    @Override
    public boolean putPhone(Phone phone) {
        boolean success = true;
        
        try(
        		Connection conn = dbCredentials.getConnection();
        		PreparedStatement pstmt = conn.prepareStatement("INSERT INTo phone (uuid, phonenumber,"
        				+ "contactuuid, statusuuid, networkuuid) VALUES (?,?,?,?,?);")
        		){
            pstmt.setString(1, phone.getUuid());
            pstmt.setString(2, phone.getPhonenumber());
            pstmt.setString(3, phone.getContactUuid());
            pstmt.setString(4, phone.getStatusuuid());
            pstmt.setString(5, phone.getNetworkuuid());
            
            pstmt.execute();
            
        } catch (SQLException e) {
            logger.error("SQLException when trying to put: " + phone);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        }
        
        return success;
    }
    
    
    /** 
     * @see ke.co.tawi.babblesms.server.persistence.contacts.BabblePhoneDAO#updatePhone(java.lang.String, ke.co.tawi.babblesms.server.beans.contact.Phone)
     */
    @Override
	public boolean updatePhone(String uuid, Phone phone) {
    	boolean success = true;
       
        try(
	        Connection conn = dbCredentials.getConnection();
	        PreparedStatement pstmt = conn.prepareStatement("UPDATE phone SET phonenumber = ?, "
        		+ "statusuuid = ?, networkuuid = ? WHERE uuid = ? ;");
           ){
        	pstmt.setString(1, phone.getPhonenumber());
        	pstmt.setString(2, phone.getStatusuuid());
        	pstmt.setString(3, phone.getNetworkuuid());
            pstmt.setString(4, uuid);

            pstmt.executeUpdate();
        }

        catch (SQLException e) {
            logger.error("SQL Exception when trying to put: " + phone + 
            		" on row with uuid:" + uuid);
            logger.error(ExceptionUtils.getStackTrace(e));
            success = false;
        } 
        
        return success;
	}

    
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.contacts.BabblePhoneDAO#getPhones(ke.co.tawi.babblesms.server.beans.contact.Contact)
	 */
	@Override
	public List<Phone> getPhones(Contact contact) {
		List<Phone> phoneList = new ArrayList<>();
		
		try(
				Connection conn = dbCredentials.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM phone WHERE contactuuid = ?;");
				){
			
			pstmt.setString(1, contact.getUuid());
			
			ResultSet rset = pstmt.executeQuery();
			phoneList = beanProcessor.toBeanList(rset, Phone.class);
			rset.close();
		}
		
		 catch (SQLException e) {
	           logger.error("SQL Exception when getting phones that belong to: " + contact);
	           logger.error(ExceptionUtils.getStackTrace(e));
	       } 		
		
        return phoneList;	
	}


	

}
