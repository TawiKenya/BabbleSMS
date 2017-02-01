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
package ke.co.tawi.babblesms.server.accountmgmt.pagination.contact;

import java.util.List;
import java.util.ArrayList;

import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.persistence.utils.CountUtils;

/**
 * Helps break down the contact list into smaller lists
 * <p>
 *  
 * @author <a href="mailto:dennism@tawi.mobii">Dennis Mutegi</a>
 *
 */
public class ContactPaginator {
	
	public final int PAGESIZE = 15; 
	private CountUtils countUtils;
	private ContactDAO contactDAO;
	private ContactGroupDAO cgrpDAO;
    private String accountuuid;
	
	/**
	 * 
	 */
	public ContactPaginator(String accountuuid) {
		countUtils = CountUtils.getInstance();
		contactDAO = ContactDAO.getInstance();
		cgrpDAO = ContactGroupDAO.getInstance();
		this.accountuuid = accountuuid;
	}
	
	
	/**
	 * a method used to fetch the first contact page 
	 * 
	 * <p>
	 * This method will return a contactPage instance with <br>
	 * contacts from index zero to index PAGESIZE - 1.
	 * 
	 * @param account
	 * @return ContactPage an instance of the class contactPage
	 */
	public ContactPage getFirstContactPage(Object object) {
		int startIndex = 0;		
		int endIndex = startIndex + PAGESIZE;
		List<Contact> returnList = new ArrayList<Contact>();
		
		returnList=CheckType(object,startIndex , endIndex);		
		
        return new ContactPage(1, calculateTotalPage(object), PAGESIZE, returnList);
		
		
	}
	
	
	/**
	 * Method for fetching the second contact page
	 * 
	 * @param account
	 * @return ContactPage instance with a list of second contact sub list
	 */
	public ContactPage getSecondContactPage(Object object) {
		int startIndex = PAGESIZE;
		int endIndex = startIndex + PAGESIZE;
		List<Contact> returnList = new ArrayList<Contact>();
		     
		returnList=CheckType(object,startIndex , endIndex);
	        
		return new ContactPage(2, calculateTotalPage(object), PAGESIZE, returnList);
	}
	
	
	/**
	 * For fetching the last contact page. 
	 * <p>
	 * This method is expected to return to return contacts
	 * that are from the indices last index - PAGESIZE.
	 * 
	 * @param account 
	 * @param StartIndex refers to the total count of the contacts of the account under inspection
	 * @return an instance of contactPAge containing the last contact sub list
	 */
	public ContactPage getLastContactPage(Object object , int StartIndex) {
		int startIndex = StartIndex - PAGESIZE;
		int endIndex = PAGESIZE + startIndex;
		
		List<Contact> returnList = new ArrayList<Contact>();
				
		returnList=CheckType(object,startIndex , endIndex);

        return new ContactPage(calculateTotalPage(object), calculateTotalPage(object), PAGESIZE, returnList);
	}
	
	
	/**
	 * This method from the current page is able to get the next contact page 
	 * <p>
	 * 
	 * @param account an account object
	 * @param currentPage <p>this parameter shouts the current page hence <br>
	 * to fetch the next contact page
	 * @return a {@link ContactPage}
	 */
	public ContactPage getNextContactPage(Object object, final ContactPage currentPage) {
		int startIndex = currentPage.getPageNum() * PAGESIZE;
		int endIndex = PAGESIZE + startIndex;
		
		List<Contact> returnList = new ArrayList<Contact>();
		    	  
		returnList=CheckType(object,startIndex , endIndex);    
     
    	return new ContactPage(currentPage.getPageNum() + 1 , calculateTotalPage(object), PAGESIZE, returnList);
	}
	
	
	/**
	 * Used for fetching the previous contact sublist from the current sublist
	 * 
	 * @param account
	 * @param currentPage used as a central point so that its possible to fetch the  previous contact page
	 * @return ContactPage  with the previous contact sublist
	 */
	public ContactPage getPreviousContactPage( Object object , final ContactPage currentPage) {				
		int startIndex  = ( (currentPage.getPageNum() - 2) * PAGESIZE );
		int endIndex = (currentPage.getPageNum() - 1) * PAGESIZE;
		List<Contact> returnList = new ArrayList<Contact>();
				
		if(startIndex >= 0){
			returnList=CheckType(object,startIndex , endIndex);
		} else {
	        returnList = currentPage.getContents();		
		}
	      
		return new ContactPage(currentPage.getPageNum() - 1 , calculateTotalPage(object), PAGESIZE, returnList);        
	}
	
    /**
     * Calculates the total number of pages that would be printed for the USSD
     * sessions that belong to the logged-in account
     *
     * @return	an integer
     */
    public int getTotalPage() {
        int totalSize = 0;

        //get the number of all sessions belonging to this email
        totalSize = countUtils.getContacts(accountuuid);

//TODO: divide by the page size and add one to take care of remainders and what else?
        return ((totalSize - 1) / PAGESIZE) + 1;
    }
    
    
    /**
     * Calculates the total number of pages that would be printed for the selected group
     * sessions that belong to the logged-in account
     *@param Group group
     * @return	an integer
     */
    public int getTotalPage(Group group) {
        int totalSize = 0;

        //get the number of all sessions belonging to this email
        totalSize = countUtils.getContactInGroup(group.getUuid());

//TODO: divide by the page size and add one to take care of remainders and what else?
        return ((totalSize - 1) / PAGESIZE) + 1;
    }
    
    
    /**
     * @param Object of type Account or Group
     * @param int startIndex
     * @param int endIndex
     * 
     * method checks if the object parameter is of type Account or Group
     * @return list of contacts
     */
    
    private List<Contact> CheckType(Object object, int startIndex , int endIndex){    	
      List<Contact> reList=new ArrayList<>();
		   if(object instanceof Account){
			   Account account =(Account)object;
     reList = contactDAO.getContactList (account , startIndex , endIndex);
		   }
		   else if(object instanceof Group){
			   Group group = (Group)object;
		reList = cgrpDAO.getContacts(group , startIndex , endIndex);
		   }
    	return reList;
    }
    
    
    /**
     * Method checks the type of Object and then returns the correct totalPage value
     * @param Object object(Either Account or Group)
     * @return int total
     */   
    
    
    public int calculateTotalPage(Object object){
    	int TotalPage=0;
    	if(object instanceof Account){
			TotalPage=getTotalPage();
		}
		else if(object instanceof Group){
			Group group = (Group)object;
			TotalPage=getTotalPage(group);
		}
    	return TotalPage;
    }
	
}
