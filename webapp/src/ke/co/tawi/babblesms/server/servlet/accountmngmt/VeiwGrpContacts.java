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

package ke.co.tawi.babblesms.server.servlet.accountmngmt;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.contact.GroupContacts;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.network.networkcountDAO;
/**
 * servlet implementation .
 * <p>
 *  
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>
 */
public class VeiwGrpContacts {
	private static VeiwGrpContacts grpcontacts;
	
	public static VeiwGrpContacts getInstance(){
		if(grpcontacts==null){
			grpcontacts=new VeiwGrpContacts();
		}
		return grpcontacts;
	}
	
	
	public HashMap getcontacts(String uuid){		
		
	     List<GroupContacts> grpList = new ArrayList<>();
	     
	     HashMap<String, String> itemlist = new HashMap<>();
	     
	     ContactDAO cDAO = ContactDAO.getInstance();
	     
	     networkcountDAO gcDAO = new networkcountDAO();
	     grpList = gcDAO.collectContacts(uuid);  
	     
	     
	if (grpList != null) {
        List<Phone> foneList =new ArrayList<>();                               
         for(GroupContacts grp : grpList) { 
  
		foneList = gcDAO.getAllPhones( grp );

       for(Phone fone: foneList){        
         
    itemlist.put(cDAO.getContact(fone.getContactUuid()).getName(),fone.getPhonenumber());              
       }
       }                            
   } 

    return itemlist;
	}

}