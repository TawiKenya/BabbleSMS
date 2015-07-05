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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.persistence.network.NetworkDAO;

/**
 * method gets the network count of contacts per group and also required phone list per 
 * group
 * <p>
 *  
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>
 */

public class Networkcount {	 	  
	  
	  private ContactGroupDAO cgDAO=null;
	  private NetworkDAO nDAO=null;
	  private PhoneDAO phDAO=null;  	  
	  private static Networkcount netcountDAO; 
	  
	  
	  
	  public static Networkcount getInstance() {
	        if (netcountDAO == null) {
	        	netcountDAO = new Networkcount();
	        }
	        return netcountDAO;
	    }
	  
	  /**constructor for testing only 
	 * @return */
	 public Networkcount(String DB_NAME, String DB_HOST, String DB_USERNAME, String DB_PASSWORD, int DB_PORT){ 
		 super();		
		 cgDAO = new ContactGroupDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWORD, DB_PORT);		 
		 nDAO = new NetworkDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWORD, DB_PORT);		
		 phDAO = new PhoneDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWORD, DB_PORT);
		
	 }
	 
	 
	 /**Default constructor for actual running of the methods*/
	 protected Networkcount(){ 
		 super();		
		 cgDAO =  ContactGroupDAO.getInstance();		
		 nDAO = NetworkDAO.getInstance();		
		 phDAO =  PhoneDAO.getInstance();		
	 }   	  

	     /**method that gets the network names and returns a hashmap with a count per network.   */
	  
	public  HashMap<String,Integer> network(String uuid){		 
		HashMap<String, Integer> totalCount= new HashMap<>();		 
		 /**returns a list of network names and uuids*/ 	 
		 
		 HashMap<String,String> getNetwork=allnetworks();
     
      int count1=0, count2=0;  
      
      for(Entry<String, String> net: getNetwork.entrySet()){
    	  count1=contactspernetwork(uuid, net.getKey()).size();
    	  count2=count2+count1;
    	  totalCount.put(net.getValue(), count1);
    	  }     
     totalCount.put("Total contacts", count2); 
     
     
     return totalCount;
	}
     
	 
	 
	 /**method returns a list of contacts per network */
	  
		public List<Phone> contactspernetwork(String grpuuid,String nwkuuid){
		 			
			 List<Phone>phoneSelected= new ArrayList<Phone>();
			 List<Phone> phoneLists;
			 List<Contact> contactList;			 
			 Phone createphone=null;			
			
	contactList =collectContacts(grpuuid);
	
	      if(phoneSelected.isEmpty()){	    	  
	     for(Contact contact : contactList) { 
	            phoneLists = phDAO.getPhones(contact);	            
	            for(Phone fone : phoneLists) {    	
	             
	             if((fone.getNetworkuuid()).equalsIgnoreCase(nwkuuid)){
	            createphone= new Phone();
	            createphone.setContactUuid(fone.getContactUuid());
	            createphone.setPhonenumber(fone.getPhonenumber());
	            createphone.setNetworkuuid(fone.getNetworkuuid()); 
	            createphone.setStatusuuid(fone.getStatusuuid());
	            createphone.setUuid(fone.getUuid());	            
	            phoneSelected.add(createphone);
	            }     
	          }	     
		     }
	       }
	     return phoneSelected;	     
	 }
	 
		
	 /**method returns a list with the required number of item to be viewed in the view */ 
		 
		 public List<Phone>allgrpcontacts(String grpuuid,int count){     	 
			 
			 HashMap<String,String> getNetwork =allnetworks(); 
			 
			 List<Phone> PhoneList=new ArrayList<Phone>();
			 List<Phone> pList=new ArrayList<Phone>();
			 List<Phone> regList = new ArrayList<>();			 
			 
			int i=count,k=10;			
			//checks if regList is null		
			if(regList.isEmpty()){
			     for(Entry<String, String> key: getNetwork.entrySet()){		 
				 pList=contactspernetwork(grpuuid, key.getKey());
				 regList.addAll(pList);			 
			      }			
			   }
			
			//gets a new arrayList with only the required item number only
			for(k=k+i;i<k;i++){			
				//if list throw indexoutofbounds exception break the loop 
				if((i>-1)&&(i<regList.size())){					
					Phone phone=regList.get(i);
				    PhoneList.add(phone);	
				   }
				else{
				    break;
				    }		
				
			    }
			 return PhoneList;
		     
		 }     
     

     /**method that gets the network names from the database and thier respective uuuids
      *and returns a hashmap with both.   */
	
     public HashMap<String, String> allnetworks() { 
		 
    	 HashMap<String, String > networkHash=new HashMap<>();  	 
    	 List<Network> NetworkList = nDAO.getAllNetworks();
    	 
    	 for(Network net:NetworkList){
    		 networkHash.put(net.getUuid(), net.getName()); 
    	 }
    	 
         return networkHash;
     }     
     
	 
	 
     /**method that gets the contact uuids from the database for a given group
      *and returns a list with both.  */
	 
     public List<Contact> collectContacts(String uuid) {		
 		List<Contact> list ;
 		
 		Group group = new Group();
 		group.setUuid(uuid);  		
 		
 		list =  cgDAO.getContacts(group);        
           
         Collections.sort(list);
         return list;
 	} 	

     

}
