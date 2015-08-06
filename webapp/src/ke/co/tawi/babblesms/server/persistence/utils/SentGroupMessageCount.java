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
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.exception.ExceptionUtils;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;
import ke.co.tawi.babblesms.server.beans.log.contactGroupsent;
import ke.co.tawi.babblesms.server.beans.messagetemplate.MsgStatus;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.logs.ContactGroupSentDAO;
import ke.co.tawi.babblesms.server.persistence.logs.OutgoingLogDAO;
import ke.co.tawi.babblesms.server.persistence.status.MessageStatusDAO;

/**
 * Method Should Return a count of each Status for a given sentGroupUuid
 * 
 * @author Migwi Ndung'u <code>mailto:<a>migwindungu0@gmail.com</a>
 *</code> 
 *
 * 
 */
public class SentGroupMessageCount {
	
	private static SentGroupMessageCount SGMCount=null;		
	private ContactGroupSentDAO cgsDAO ;
	private OutgoingLogDAO outDAO;
	private MessageStatusDAO msDAO;
	
	
	/**
	 *  
	 * @return {@link ContactGroupSentDAO} Instance
	 */
	
	   public static SentGroupMessageCount getInstance(){
	        if(SGMCount==null){
		       SGMCount = new SentGroupMessageCount();
	             }
	          return SGMCount;
	         }
	
	   /**
	    * Default Constructor
	    */
	
	   protected SentGroupMessageCount(){
		cgsDAO = ContactGroupSentDAO.getInstance();
		outDAO = OutgoingLogDAO.getInstance();
		msDAO = MessageStatusDAO.getInstance();
	    }
	   
	   
	   /**
	    * Constructor Used for Testing Only
	    * 
	    * @param DB_NAME
	    * @param DB_HOST
	    * @param DB_USERNAME
	    * @param DB_PASSWORD
	    * @param DB_PORT
	    */	
	   
	   public SentGroupMessageCount(String DB_NAME, String DB_HOST, String DB_USERNAME, String DB_PASSWORD, int DB_PORT){
		 cgsDAO = new ContactGroupSentDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWORD, DB_PORT);
		 outDAO = new OutgoingLogDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWORD, DB_PORT);
		 msDAO = new MessageStatusDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWORD, DB_PORT);
	   }
	
	
	
	/**
	 * Method returns a HashMap with each status Name And its respective Count
	 * 
	 * @param SentGroupUuid
	 * @return HashMap<String , Integer>
	 */	
	
	public HashMap<String,Integer> getGroupContactMessages(String SentGroupUuid){
		HashMap<String,String> MsgSts = getMessageStatus();
		HashMap<String,Integer> MapCount = new HashMap<>();
		
		//put in the HashMap, Status name And its respective Count.
		for(Entry<String, String> sts: MsgSts.entrySet()){
			MapCount.put(sts.getValue(), 
					getContactMessageCount(SentGroupUuid, sts.getKey()));
		}		
		
		return MapCount;
	}
	
	/**
	 * 
	 * @param SentGroupUuid
	 * @param StatusUuid
	 * @return the count of each Status Name In the given SentGroupUuid
	 */	
	
	public int getContactMessageCount(String SentGroupUuid, String StatusUuid){
		List<contactGroupsent> cgSent = new ArrayList<>();
		List<OutgoingLog> Sentlog =new ArrayList<>();
		int count = 0;
		
		cgSent = cgsDAO.getUsingSentGroup(SentGroupUuid);
		for(contactGroupsent sent:cgSent){						
			Sentlog =  outDAO.getAll(sent.getSentContactUuid());
			
		if(Sentlog !=null){
			for(OutgoingLog sLog:Sentlog){				
		      if((sLog.getMessagestatusuuid()).equalsIgnoreCase(StatusUuid)){
				     count++;				    	 
			    }
			}
		  }
		}
	
		return count;
	}
	
	
	/**
	 * 
	 * @return HashMap with all Message Status and thier respective uuids
	 */
	
	
	public HashMap<String, String> getMessageStatus(){		
		HashMap<String, String> StatusMap = new HashMap<>();
		List<MsgStatus> mST = new ArrayList<>();
		
		mST = msDAO.getAllMessageStatus();
		
		for(MsgStatus MS: mST){
			StatusMap.put(MS.getUuid(), MS.getDescription());
		}		
	
		return StatusMap;
	}	

}
