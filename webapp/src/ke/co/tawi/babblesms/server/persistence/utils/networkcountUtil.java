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

import java.io.IOException;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Group;
//import ke.co.tawi.babblesms.server.beans.contact.GroupContacts;
import ke.co.tawi.babblesms.server.persistence.network.networkcountDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
//import ke.co.tawi.babblesms.server.servlet.group.Group;

//import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Persistence implementation
 * <p>
 *  
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>
 */
public class networkcountUtil {
	
	//private Logger logger = Logger.getLogger(this.getClass());
	
	private Gson gson;
	
@SuppressWarnings("unchecked")
public String networkcount(Account Account){
	String networkString = null;
	
	 gson = new GsonBuilder().create();	
	
	HashMap<String,String> checknet = new HashMap<>();
	
	HashMap<String,HashMap<String,String>> checknet2 = new HashMap<>();
	
	networkcountDAO netcount= new networkcountDAO();
	
	@SuppressWarnings("rawtypes")
	List<Group> groupslist = new ArrayList();
	
	 GroupDAO groups =GroupDAO.getInstance();	  
	groupslist =groups.getGroups(Account);
	
	
	//get network count per group associated with a given account user
	//sample of the expected output
/**{"Management":{"saf com":"92 contacts", "orange":"16 contacts","airtel":"39 contacts", "yu" :"17 contacts"},'+
   *
   *'"Parents":{"saf com" :"72 contacts","orange":"156 contacts", "airtel":"71 contacts", "yu" :"35 contacts"},'+
   *
   *"Teachers":{"saf com" :"223 contacts", "orange":"17 contacts", "airtel":"6 contacts", "yu" :"5 contacts"},'+
   *
   *"Staff":{"saf com" :"9 contacts", "orange" :"16 contacts", "airtel":"61 contacts", "yu" :"999 contacts"},'+
   *
   *"Students":{"saf com" :"21 contacts", "orange":"34 contacts", "airtel":"27 contacts", "yu" :"67 contacts"}'+
 *}'*/
	
	
	 if (groupslist != null) {
         for (Group code : groupslist) {        	 
       //get a list of count per network into a hashmap	 
       checknet =netcount.network(code.getUuid());
       
         //put a the network count per group  and group name into a hashmp   
        checknet2.put(code.getName(),checknet );  
                 
           }
         //creates a JSON object that shud b converted to a string for sending back  
         networkString= gson.toJson(checknet2); 
         
       }
	
return networkString;	
}

}