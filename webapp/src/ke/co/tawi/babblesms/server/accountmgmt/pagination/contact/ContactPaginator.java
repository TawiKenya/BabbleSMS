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
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
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
	
	private String userName; 
	public final int PAGESIZE = 15; 
	private CountUtils countUtils;
    private String accountuuid;
	
	/**
	 * 
	 */
	public ContactPaginator(String accountuuid) {
		countUtils = CountUtils.getInstance();
		this.accountuuid = accountuuid;
	}

	
	/**
	 * @param uname
	 */
//	public ContactPaginator() {
	//	this.userName = uname;		
	//}

	
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
	public ContactPage getFirstContactPage(Account account) {
		int startIndex = 0;
		int endIndex = startIndex + PAGESIZE;
		List<Contact> returnList = new ArrayList<Contact>();
		   
        returnList = ContactDAO.getInstance().getContactList (account , startIndex , endIndex);

        return new ContactPage(1, getTotalPage(), PAGESIZE, returnList);		   
	}
	
	
	/**
	 * Method for fetching the second contact page
	 * 
	 * @param account
	 * @return ContactPage instance with a list of second contact sub list
	 */
	public ContactPage getSecondContactPage(Account account) {
		int startIndex = PAGESIZE;
		int endIndex = startIndex + PAGESIZE;
		List<Contact> returnList = new ArrayList<Contact>();
		     
		returnList = ContactDAO.getInstance().getContactList (account , startIndex , endIndex);
	        
		return new ContactPage(2, getTotalPage(), PAGESIZE, returnList);
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
	public ContactPage getLastContactPage(Account account , int StartIndex) {
		int startIndex = StartIndex - PAGESIZE;
		int endIndex = PAGESIZE + startIndex;
		
		List<Contact> returnList = new ArrayList<Contact>();
				
		returnList = ContactDAO.getInstance().getContactList (account , startIndex , endIndex);

        return new ContactPage(getTotalPage(), getTotalPage(), PAGESIZE, returnList);
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
	public ContactPage getNextContactPage(Account account , final ContactPage currentPage) {
		int startIndex = currentPage.getPageNum() * PAGESIZE;
		int endIndex = PAGESIZE + startIndex;
		
		List<Contact> returnList = new ArrayList<Contact>();
		    	  
    	returnList = ContactDAO.getInstance().getContactList (account , startIndex , endIndex);    
     
    	return new ContactPage(currentPage.getPageNum() + 1 , getTotalPage(), PAGESIZE, returnList);
	}
	
	
	/**
	 * Used for fetching the previous contact sublist from the current sublist
	 * 
	 * @param account
	 * @param currentPage used as a central point so that its possible to fetch the  previous contact page
	 * @return ContactPage  with the previous contact sublist
	 */
	public ContactPage getPreviousContactPage( Account account , final ContactPage currentPage) {				
		int startIndex  = ( (currentPage.getPageNum() - 2) * PAGESIZE );
		int endIndex = (currentPage.getPageNum() - 1) * PAGESIZE;
		List<Contact> returnList = new ArrayList<Contact>();
				
		if(startIndex >= 0){
			  returnList = ContactDAO.getInstance().getContactList (account , startIndex , endIndex);
		} else {
	        returnList = currentPage.getContents();		
		}
	      
		return new ContactPage(currentPage.getPageNum() - 1 , getTotalPage(), PAGESIZE, returnList);        
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
	
}
