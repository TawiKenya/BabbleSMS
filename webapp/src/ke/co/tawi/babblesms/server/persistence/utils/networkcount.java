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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.contact.Contact;
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

public class networkcount extends GenericDAO{
	
	 private Logger logger = Logger.getLogger(this.getClass());	 
	 
	  private static ArrayList<String> NetworkName= new ArrayList<String>();  
	  
	  private BeanProcessor beanProcessor = new BeanProcessor();
	  
	  

	    /**
	     *
	     */
	    public networkcount() {
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
	  public networkcount(String dbName, String dbHost, String dbUsername,
	            String dbPassword, int dbPort) {
	        super(dbName, dbHost, dbUsername, dbPassword, dbPort);
	    }
	
	  
	  
	  

	     /**
	      *method that gets the network names and 
	      *and returns a hashmap with a count per network.
	      */
	  
	public  HashMap<String, String> network(String uuid){
		
		 
		 List<Phone> phoneLists;
		 List<Contact> contactList;	
		 HashMap<String,String> totalCount= new HashMap<String,String>();
		 
		 /**	
			* returns a list of network names and uuids
			*/ 	 
		 
		 HashMap<String,String> getNetwork=allnetworks();
		 
		 
		 
  /**	
	* returns a list  contacts from a given group
	*/ 	 
	
    contactList =collectContacts(uuid);   
    
    
    
    
    /**
	* linked to the PhoneDAO
	* returns a list phones associated with a given contact from a given group
	*/    
    PhoneDAO phoneDAO = PhoneDAO.getInstance();	
	 
    
    
     int countnet= (NetworkName.size())-1;
     
      int count1=0, count2=0, count3=0, count4=0, count6=0;
      
     for(Contact contact1 : contactList) { 
            phoneLists = phoneDAO.getPhones(contact1);

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
           count4++;
         }
             
         }
       }
     totalCount.put(getNetwork.get((String)NetworkName.get(countnet)), count1+" contact(s)");
     totalCount.put(getNetwork.get((String)NetworkName.get(countnet)), count2+" contact(s)");
     totalCount.put(getNetwork.get((String)NetworkName.get(countnet)), count3+" contact(s)");
     totalCount.put(getNetwork.get((String)NetworkName.get(countnet)), count4+" contact(s)");
     totalCount.put("Total contacts", count6+" contact(s)");
     
     
     return totalCount;
	}
     
     
     

     /**
      *method that gets the network names from the database and thier respective uuuids
      *and returns a hashmap with both.
      */

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
     public List<Contact> collectContacts(String uuid) {		
 		List<Contact> list = new ArrayList<>();

         try (
      		   Connection conn = dbCredentials.getConnection();
      	       PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM contactgroup WHERE groupuuid = ?;");    		   
      	   ) {
          	   pstmt.setString(1, uuid);   
      	       ResultSet rset = pstmt.executeQuery();
      	       
      	       list = beanProcessor.toBeanList(rset, Contact.class);
      	       
         } catch (SQLException e) {
             logger.error("SQLException when getting contacts of " + uuid);
             logger.error(ExceptionUtils.getStackTrace(e));
         }
           
         Collections.sort(list);
         return list;
 	}
 	
     

}
