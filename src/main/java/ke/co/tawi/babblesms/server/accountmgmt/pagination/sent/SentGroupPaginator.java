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
package ke.co.tawi.babblesms.server.accountmgmt.pagination.sent;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.log.OutgoingGrouplog;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;
import ke.co.tawi.babblesms.server.persistence.logs.OutgoingGroupLogDAO;
import ke.co.tawi.babblesms.server.persistence.utils.CountUtils;

/**
 * Paginate an Sent group details HTML view.
 * <p>
 *  
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>
 */
public class SentGroupPaginator {
	
	final int PAGE_SIZE=15;	
	private Account account;	
	private CountUtils countUtils;
	private OutgoingGroupLogDAO outGoingGrplog;
	
	/**
	 * Default Constructor
	 * */
	public SentGroupPaginator(String accountuuid){
		account = new Account();
		account.setUuid(accountuuid);
		
		countUtils = CountUtils.getInstance();
		outGoingGrplog = OutgoingGroupLogDAO.getInstance(); 
		
	}
	
	 /**
    *Constructor used for testing ONLY
    *
    * @param dbName
    * @param dbHost
    * @param dbUsername
    * @param dbPasswd
    * @param dbPort
    */
     public SentGroupPaginator(String dbName, String dbHost, String dbUsername, String dbPasswd, int dbPort){
    	  countUtils = new CountUtils(dbName, dbHost, dbUsername, dbPasswd, dbPort);
    	  outGoingGrplog = new OutgoingGroupLogDAO(dbName, dbHost, dbUsername, dbPasswd, dbPort); 
     }
     
     
     /**
      * @return the first page details, of the Html view
      */
     public SentGroupPage getFirstPage(){
    	 SentGroupPage sent  = new SentGroupPage();
    	 
    	 List<OutgoingGrouplog> Olog = null;
    	 Olog = outGoingGrplog.getOutGoingGroupLog(account, 0, PAGE_SIZE);
    	 
    	 sent = new SentGroupPage(1, this.getTotalPages(),PAGE_SIZE,Olog); 
    	 return sent;
     }
     
     
     /**
      * moves the next page and gets all its comprehensive details
      * 
      *@param current page details
      *@return next Page details 
      *
      */
     public SentGroupPage  getNextpage(final SentGroupPage sg){
    	 SentGroupPage sent =new SentGroupPage();
    	 
    	 List<OutgoingGrouplog> Olog = null;
    	 int pg = sg.getPageNumber()*PAGE_SIZE;
    	 Olog = outGoingGrplog.getOutGoingGroupLog(account, pg+1, pg+PAGE_SIZE);
    	 
    	 sent = new SentGroupPage(sg.getPageNumber()+1, this.getTotalPages(),PAGE_SIZE,Olog); 
    	 return sent; 
     }     
     
    
     /**
      *Moves to the previous page and gets its comprehensive details
      *
      * @param current page details
      * @return previous page details
      */
     public SentGroupPage  getPreviousPage(final SentGroupPage sg){
    	 SentGroupPage sent  = new SentGroupPage();
    	 
    	 List<OutgoingGrouplog> Olog = null;
    	  int pg = (sg.getPageNumber()-1)*PAGE_SIZE;
    	 Olog = outGoingGrplog.getOutGoingGroupLog(account, pg, pg+PAGE_SIZE);
    	 
    	 sent = new SentGroupPage(sg.getPageNumber()-1, this.getTotalPages(),PAGE_SIZE,Olog); 
    	 return sent;  
     }
     
     
     /***
      * MOves to the last page and gets its comprehensive details
      */
     public SentGroupPage  getLastPage(){
    	 SentGroupPage sent  = new SentGroupPage();
    	 
    	 List<OutgoingGrouplog> Olog = null;
    	 int totalcount = countUtils.getOutgoingGroupLog(account.getUuid());
    	 Olog = outGoingGrplog.getOutGoingGroupLog(account,(this.getTotalPages()-1)*PAGE_SIZE, totalcount);
    	 
    	 sent = new SentGroupPage(this.getTotalPages(), this.getTotalPages(),PAGE_SIZE,Olog); 
    	 return sent;
     }
     
     
     /**
      * @return the total pages of all OutgoingGrouplogs
      */
     public int getTotalPages(){
    	 int totalPages=0;
    	 
    	 totalPages = countUtils.getOutgoingGroupLog(account.getUuid());
    	 return ((totalPages-1)/PAGE_SIZE)+1;
     }
}
