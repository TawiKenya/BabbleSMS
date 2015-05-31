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
package ke.co.tawi.babblesms.server.servlet.upload;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.beans.status.Status;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.beans.contact.Group;

import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;

import java.io.File;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;
import java.util.Arrays;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.LineIterator;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Various manipulations on an uploaded Contact CSV file.
 * <p> 
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class UploadUtil {

	private String[] networkArray, networkUuidArray;
	private List<String> networkList;
	
	private static Logger logger = Logger.getLogger(UploadUtil.class);
	
	
	/**
	 * 
	 */
	protected UploadUtil() {
		networkArray = new String[] {"safaricom", "airtel", "orange", "yu"};
		networkUuidArray = new String[] {Network.SAFARICOM_KE, Network.AIRTEL_KE, Network.ORANGE_KE, 
				Network.YU_KE};
		
		networkList = Arrays.asList(networkArray);
	}
	
	
	/**
	 * Checks that an uploaded Contact file is in proper order.
	 * 
	 * @param file
	 * @return the feedback of having inspected the file, whether it was proper
	 */
	protected String inspectContactFile(File file) {
		String feedback = ContactUpload.UPLOAD_SUCCESS;
		int count = 1;
		
		LineIterator lineIterator =  null;
		try {
			lineIterator = FileUtils.lineIterator(file, "UTF-8");
			
			String line;
			String[] rowTokens, phoneTokens, networkTokens;
			String network;
			while (lineIterator.hasNext()) {
			     line = lineIterator.nextLine();
			     
			     rowTokens = StringUtils.split(line, ',');
			     
			     if(rowTokens.length != 3 && line.length() > 0) {// Every row must have 3 columns
			    	 return ("Invalid format on line " + count + ": " + line);
			     }
			     
			     phoneTokens = StringUtils.split(rowTokens[1], ';');
			     networkTokens = StringUtils.split(rowTokens[2], ';');
			     
			     // Check that the number of phone numbers and number of networks match
			     if(phoneTokens.length != networkTokens.length) {
			    	 return ("Invalid format on line " + count + ": " + line);
			     }
			     
			     // Check to see that only valid networks have been provided
			     for(String s : networkTokens) {
			    	 network = StringUtils.lowerCase( StringUtils.trimToEmpty(s) );
			    	 if(!networkList.contains(network)) {
			    		 return ("Invalid network on line " + count + ": " + line);
			    	 }
			     }
			     
			     count++;
		    }
			
		} catch (IOException e) {
			logger.error("IOException when inspecting: " + file);
			logger.error(e);
			
		} finally {
			if(lineIterator != null) {
				lineIterator.close();
			}		   
		}
		 
		return feedback;
	}
	
	
	protected void saveContacts(File contactFile, Account account, ContactDAO contactDAO, 
			PhoneDAO phoneDAO, List<String> groupUuids, ContactGroupDAO contactGroupDAO) {
		LineIterator lineIterator =  null;
		Contact contact;
		Phone phone;
		
		List<Group> groupList = new LinkedList<>();
		Group grp;
		for(String uuid : groupUuids) {
			grp = new Group();
			grp.setUuid(uuid);
			groupList.add(grp);
		}
		
		try {
			lineIterator = FileUtils.lineIterator(contactFile, "UTF-8");
			
			String line;
			String[] rowTokens, phoneTokens, networkTokens;
			
			while (lineIterator.hasNext()) {
			     line = lineIterator.nextLine();
			     
			     rowTokens = StringUtils.split(line, ',');
			     
			     // Extract the Contact and save
			     contact = new Contact();
			     contact.setAccountUuid(account.getUuid());
			     contact.setName(rowTokens[0]);
			     contact.setStatusUuid(Status.ACTIVE);
			     contactDAO.putContact(contact);
			    
			     // Extract the phones and save
			     phoneTokens = StringUtils.split(rowTokens[1], ';');
			     networkTokens = StringUtils.split(rowTokens[2], ';');	
			     
			     String network;
			     
			     for(int j=0; j<phoneTokens.length; j++ ) {
			    	 phone = new Phone();
			    	 phone.setPhonenumber(StringUtils.trimToEmpty(phoneTokens[j]));
			    	 phone.setContactUuid(contact.getUuid());
			    	 phone.setStatusuuid(Status.ACTIVE);
			    	 
			    	 network = StringUtils.lowerCase( StringUtils.trimToEmpty( networkTokens[j] ));
			    	 phone.setNetworkuuid(networkUuidArray[ networkList.indexOf(network) ]);
			    	 
			    	 phoneDAO.putPhone(phone);
			     }
			      
			     // Associate the Contact to the Groups
			     for(Group group : groupList) {
			    	 contactGroupDAO.putContact(contact, group);
			     }
			     
		    }// end 'while (lineIterator.hasNext())'
			
		} catch (IOException e) {
			logger.error("IOException when storing: " + contactFile);
			logger.error(e);
			
		} finally {
			if(lineIterator != null) {
				lineIterator.close();
			}		   
		}
	}
}

