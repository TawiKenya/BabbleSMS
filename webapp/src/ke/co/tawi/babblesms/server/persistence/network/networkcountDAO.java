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

package ke.co.tawi.babblesms.server.persistence.network;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.GroupContacts;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Persistence implementation
 * <p>
 *  
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>
 */

public class networkcountDAO extends GenericDAO implements BabblenetworkcountDAO{
	
	 private Logger logger = Logger.getLogger(this.getClass());	 
	 
	  private static ArrayList<String> NetworkName= new ArrayList<String>();  
	  
	  private BeanProcessor beanProcessor = new BeanProcessor();
	  
	  

	    /**
	     *
	     */
	    public networkcountDAO() {
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
	  public networkcountDAO(String dbName, String dbHost, String dbUsername,
	            String dbPassword, int dbPort) {
	        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
	    }
	
	  
	  
	  

	     /**
	      *method that gets the network names and 
	      *and returns a hashmap with a count per network.
	      */
	 @Override 
	public  HashMap<String, String> network(String uuid){
		
		 
		 List<Phone> phoneLists;
		 List<GroupContacts> contactList;	
		 HashMap<String,String> totalCount= new HashMap<String,String>();
		 
		 /**	
			* returns a list of network names and uuids
			*/ 	 
		 
		 HashMap<String,String> getNetwork=allnetworks();
		 
		 
		 
  /**	
	* returns a list  contacts from a given group
	*/ 	 
	
    contactList =collectContacts(uuid);   
   
    
     int countnet= (NetworkName.size())-1;
     
      int count1=0, count2=0, count3=0, count4=0, count6=0;     
      
      
     for(GroupContacts contact1 : contactList) { 
            phoneLists = getAllPhones(contact1);
            
            for(Phone fone : phoneLists) {                                                  
            	
            count6++; 
             if((fone.getNetworkuuid()).equalsIgnoreCase((String)NetworkName.get(countnet))){
           count1++;          
         }

         else if((fone.getNetworkuuid()).equalsIgnoreCase((String)NetworkName.get(countnet-1))){
           count2++;           
         }

         else if((fone.getNetworkuuid()).equalsIgnoreCase((String)NetworkName.get(countnet-2))){
           count3++;           
         } 

         else if((fone.getNetworkuuid()).equalsIgnoreCase((String)NetworkName.get(countnet-3))){
           count4++;           ;
         }
         
         }
       }
     totalCount.put(getNetwork.get((String)NetworkName.get(countnet)), count1+" contact(s)");
     totalCount.put(getNetwork.get((String)NetworkName.get(countnet-1)), count2+" contact(s)");
     totalCount.put(getNetwork.get((String)NetworkName.get(countnet-2)), count3+" contact(s)");
     totalCount.put(getNetwork.get((String)NetworkName.get(countnet-3)), count4+" contact(s)");
     totalCount.put("Total contacts", count6+" contact(s)");
     
     
     return totalCount;
	}
     
     
     

     /**
      *method that gets the network names from the database and thier respective uuuids
      *and returns a hashmap with both.
      */
	 @Override 
     public HashMap<String, String> allnetworks() {
    	 
    	 HashMap<String, String > networkHash=new HashMap<String, String>(); 
    	 
    	     	 
         try(
             Connection conn = dbCredentials.getConnection();
             PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM Network;");
             ResultSet rset = pstmt.executeQuery();
        		 ){        	 
        	
        	 
             while(rset.next()){
            networkHash.put(rset.getString("uuid"), rset.getString("name")); 
             NetworkName.add(rset.getString("uuid"));                          	 
             }             
            
         } catch (SQLException e) {
             logger.error("SQL Exception when getting all networks");
             logger.error(ExceptionUtils.getStackTrace(e));
         } 
         return networkHash;
     }
     
     
     /**
      *method that gets the contact uuids from the database for a given group
      *and returns a list with both.
      */
	 @Override 
     public List<GroupContacts> collectContacts(String uuid) {		
 		List<GroupContacts> list = new ArrayList<>();

         try (
      		   Connection conn = dbCredentials.getConnection();
      	       PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM contactgroup WHERE groupuuid = ?;");    		   
      	   ) {
          	   pstmt.setString(1, uuid);   
      	       ResultSet rset = pstmt.executeQuery();
      	       
      	       list = beanProcessor.toBeanList(rset, GroupContacts.class);
      	       
         } catch (SQLException e) {
             logger.error("SQLException when getting contacts of " + uuid);
             logger.error(ExceptionUtils.getStackTrace(e));
         }
           
         //Collections.sort(List<GroupCantacts>list);
         return list;
 	}
 	

     /**
      *method that gets all phones for a given contact
      *
      */     
	 
	 @Override
		public List<Phone> getAllPhones(GroupContacts contact) {
			List<Phone> phoneList = new ArrayList<>();
			
			try(
					Connection conn = dbCredentials.getConnection();
					PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM phone WHERE contactuuid = ?;");
					){
				
				pstmt.setString(1, contact.getContactUuid());
				
				try(ResultSet rset = pstmt.executeQuery();) {
					phoneList = beanProcessor.toBeanList(rset, Phone.class);
				}
			}
			
			 catch (SQLException e) {
		           logger.error("SQL Exception when getting phones that belong to: " + contact);
		           logger.error(ExceptionUtils.getStackTrace(e));
		       } 		
			
	        return phoneList;	
		}    

}
