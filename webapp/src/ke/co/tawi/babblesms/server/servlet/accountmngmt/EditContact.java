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

import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.account.Status;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.EmailDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.beans.contact.Email;
import ke.co.tawi.babblesms.server.beans.contact.Group;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;

/**
 * Receives a form request by an account holder to edit a contact. 
 * <p>
 *  
 * @author <a href="mailto:wambua@tawi.mobi">Godfrey Wambua</a>
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>
 */
public class EditContact extends HttpServlet {

	final String ERROR_NO_NAME = "Some fields are empty please fill in all the fields.";
	
	private Cache accountCache;
	private static Contact contact;
	private ContactGroupDAO cgDAO;
	private EmailDAO eDAO;
	private String ACTIVE_STATUS;
	private String SUSPENDED_STATUS;
	private HashMap<String, Email> eMap;
	private HashMap<String, Phone> phMap;
	
	
	/**
	 *
	 * @param config
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		 eDAO=EmailDAO.getInstance();
	     cgDAO = ContactGroupDAO.getInstance();
		CacheManager mgr = CacheManager.getInstance();
		accountCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
		Status state = new Status();
		ACTIVE_STATUS=Status.ACTIVE;
		SUSPENDED_STATUS=Status.SUSPENDED;
	}

	
	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException, IOException
	 */
	
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		HttpSession session = request.getSession(true);

		String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
		if (StringUtils.isEmpty(username)) {
			response.sendRedirect("../index.jsp");
		}
		Account account = new Account();

		Element element;
		if ((element = accountCache.get(username)) != null) {
			account = (Account) element.getObjectValue();
		}		
		String contactname = request.getParameter("name").trim();
		String contactstatusuuid = request.getParameter("statusuuid").trim();
		String description =request.getParameter("description").trim();
		
		String [] groupArray =request.getParameterValues("groupselected[]");
		String cuuid = request.getParameter("uuid").trim();
		String [] phonenumArray = request.getParameterValues("phone1[]"); 
		
		String [] emailArray = request.getParameterValues("email[]");
		String[] networkArray = request.getParameterValues("network[]");
		
		if(contactname.equals("") || phonenumArray.equals("")){
			session.setAttribute("fail", ERROR_NO_NAME);
		}

		else{	
			
			boolean Contact = saveContact(cuuid,contactname,description,account);
			boolean Phone = savePhone(phonenumArray,networkArray,cuuid,contactstatusuuid);
			boolean Email= saveEmail(cuuid, emailArray);
			boolean Group = saveGroup(account, groupArray );
			
			if(Contact==true && Phone==true && Email==true && Group==true){
				session.setAttribute("success", "Saved Successfully!!");
				
			}else{
				session.setAttribute("fail", "An Error occurred, Please try Again!!");
			}
		}

		response.sendRedirect("contact.jsp");
	}
	
	
	/**
	 * 
	 * @param String cuuid
	 * @param String contactname
	 * @param String description
	 * @param {{@link Account} account
	 * 
	 * @return <code> true if a contact is saved </code> and <code> false if  
	 * otherwise;</code>
	 */	
	  public boolean saveContact(String cuuid,String contactname, String description, Account account){
		  boolean save = true;
		  try{
	    	   ContactDAO contactsDAO = ContactDAO.getInstance();
				contact = contactsDAO.getContact(cuuid);
				contact.setName(contactname);
				contact.setDescription(description);
				contact.setAccountUuid(account.getUuid());		
				contactsDAO.updateContact(cuuid, contact);
		  }catch(Exception e){			  
			  save =false;
		  }
		    return save;
	        }
	       
	  
	  /**
	   * @param  String Array phonenumArray
	   * @param  String Array networkArray
	   * @param String cuuid
	   * @param String contactstatusuuid
	   * 
	   *  @return <code> true if a contact is saved </code> and <code> false if  
	   * otherwise;</code>
	   */
	    public boolean savePhone(String [] phonenumArray, String []networkArray,String cuuid, String contactstatusuuid){
	    	boolean save=true;
	    	try{
	    	PhoneDAO phoneDAO=PhoneDAO.getInstance();
	    	
			List<Phone> plist = phoneDAO.getPhones(contact);
			
			phMap = new HashMap<>();
			
			for(Phone ph:plist){
				ph.setStatusuuid(SUSPENDED_STATUS);
				phMap.put(ph.getPhonenumber(), ph);	
				phoneDAO.updatePhone(ph.getUuid(), ph);
			   } 
			
			
			
			int i=0;
			//Avoid the Null exception Error
			try{
			for(String phoneNum: phonenumArray){
				
					Phone newPhone =new Phone();					
					newPhone.setPhonenumber(phoneNum.trim());
					newPhone.setContactUuid(cuuid);
					newPhone .setStatusuuid(ACTIVE_STATUS);
					newPhone.setNetworkuuid(networkArray[i].trim());
					phoneDAO.putPhone(newPhone);					
					i++;
				
			}
			}catch(Exception e){}
	    	}catch(Exception e){	    		
	    		save=false;
	    	}
	    	
	    return save;	
	      }
	    
	    
	    /**
	     * 
	     * @param String cuuid
	     * @param String Array emailArray
	     * 
	     *  @return <code> true if a contact is saved </code> and <code> false if  
	     * otherwise;</code> 
	     */
	    
	     public boolean saveEmail(String cuuid,String [] emailArray){
	    	  boolean save = true;
	    	  try{
				//Map to hold email address and email object
				eMap = new HashMap<>();		
				List<Email> elist = eDAO.getEmails(contact);
				for(Email email:elist){
				    email.setStatusuuid(SUSPENDED_STATUS);
				    eMap.put(email.getAddress(), email);
				     }
				//Avoid the Null exception Error
				try{
				for(String emailAddr: emailArray){
					if(eMap.containsKey(emailAddr.trim())){
						Email nEmail = new Email();
						nEmail=eMap.get(emailAddr.trim());
					    nEmail.setStatusuuid(ACTIVE_STATUS); 
					    eDAO.updateEmail(nEmail.getUuid(), nEmail);					   
					}
					else{
						Email newEmail =new Email();					
						newEmail.setAddress(emailAddr.trim());
						newEmail.setContactuuid(cuuid);
			            newEmail.setStatusuuid(ACTIVE_STATUS);
			            eDAO.putEmail(newEmail);			
					}
				} 
				}catch(Exception e){}
	    	  }catch (Exception e){	    		  
	    		  save=false;
	    	  }
				
		  return save;
	  }

	     
	     /**
	      * @param {@link Account} account
	      * @param  String [] groupArray
	      * 
	      *  @return <code> true if a contact is saved </code> and <code> false if  
	      * otherwise;</code>
	      */

         public boolean saveGroup(Account account, String [] groupArray ){
        	boolean save=true;        	
        	try{	          			
	           GroupDAO gDAO =GroupDAO.getInstance();
		        List<Group> Glist=gDAO.getGroups(account);
		        for(Group gp:Glist){
		        cgDAO.removeContact(contact, gp);	
		           }
		        //Avoid the Null exception Error
		        try{
              if(groupArray.length>0){            	  
	          for (String groupuuid : groupArray) {		    
	             Group group  = new Group();
	             group.setUuid(groupuuid.trim());
	             group.setAccountsuuid(contact.getAccountUuid());	             
	             cgDAO.putContact(contact, group);
		                 }
                     }
                   }catch(Exception e){}
        	}catch(Exception e){        		
        		save = false;
        	}
	              return save;
              }
}