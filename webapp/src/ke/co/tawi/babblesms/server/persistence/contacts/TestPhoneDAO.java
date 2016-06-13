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
package ke.co.tawi.babblesms.server.persistence.contacts;

import static org.junit.Assert.*;

import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;

import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests our persistence for {@link Phone}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestPhoneDAO {
	
	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;
	
	final String PHONE_UUID = "78c315d6-e253-4165-9493-d20f5d3367df", PHONE_UUID_NEW = "W88wk681d2e6-84f2-45ff-914c-522a3b076141" , 
			TEST_UUID ="78c315d6-e253-4165-9493-d20f5d3367zz",
			PUTPHONEUUID ="78h315d6-e253-4165-9493-d20f5d3367zz"; 

    final String PHONENUM = "254708976405",
            PHONENUM_NEW = "254708976409",
            PHONENUM_UPDATE = "254708976411";

    final String NETWORK_UUID = "5C1D9939-136A-55DE-FD0E-61D8204E17C9",
            NETWORK_UUID_NEW = "B936DA83-8A45-E9F0-2EAE-D75F5C232E78";

    final String CONTACTUUID = "2735fd77-3f71-4846-9cc5-c9c53be9f92f",
            CONTACTUUID_NEW = "52ea7495-2568-4a0c-b952-abe7bed2d2de",
            CONTACTUUID_LATEST = "556e1c97-362c-4410-8e92-cd287e4c5c9b";
    

    final String STATUSUUID = "396F2C7F-961C-5C12-3ABF-867E7FD029E6",
            STATUSUUID_NEW = "396F2C7F-961C-5C12-3ABF-867E7FD029E6";
    
    final String UUID = "9c918b90-68f2-4d14-a9dd-969f2318f077",
    		PHONE ="254722347063" , CUUID = "556e1c97-362c-4410-8e92-cd287e4c5c9b",
    		SUUID = "396F2C7F-961C-5C12-3ABF-867E7FD029E6",
    		NUUID = "741AC38C-3E40-6887-9CD6-365EF9EA1EF0";
    
    final String PHONEMATCH = "723";
    

    private PhoneDAO storage;
    private ContactDAO storagenew;
    
    
    /**
     * 
     */
    @Ignore
    @Test
    public void testGetPhone() {
    	storage = new PhoneDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
    	
    	Phone p = storage.getPhone(PHONE_UUID);
    	assertEquals(p.getUuid(), PHONE_UUID);
    	assertEquals(p.getPhonenumber(), PHONENUM);
    	assertEquals(p.getContactUuid(), CONTACTUUID);
    	assertEquals(p.getStatusuuid(), STATUSUUID);
    	assertEquals(p.getNetworkuuid(), NETWORK_UUID);
    }
    
 
    /**
     * 
     
    @Ignore
    @Test
    public void  testGetPhones(){
    	storage = new PhoneDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
    	
    	Phone p = storage.getPhone(UUID);
    	
    	List<Phone> phoneList = storage.getPhones("7");
    	List<Phone> phoneList2 = storage.getPhones("72");

    	assertTrue(phoneList.size() >= 502);
    	assertFalse(phoneList.isEmpty());
    	//assertTrue(phoneList.contains(p));
    	assertEquals(p.getPhonenumber() , PHONE );
    	assertTrue(phoneList2.size() <= phoneList.size());
    	
    }
    
    
    /**
     * 
     */
    @Ignore
    @Test
    public void testGetPhones2(){
    	storagenew = new ContactDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
    	
    	Contact contact = storagenew.getContact(CONTACTUUID_LATEST);
    	
    	assertNotNull(contact);
    	assertEquals(contact.getUuid() , CONTACTUUID_LATEST);
    	
    	storage = new PhoneDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
    	List<Phone> contactPhoneList = storage.getPhones(contact);
    	
        assertTrue(contactPhoneList.size() >= 7);       	
    }
    

	/**
	 * 
	 */ 
    @Ignore 
	@Test
   public void testupdatePhone(){
		storage = new PhoneDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
	    Phone phone = new Phone();
	    phone.setUuid(PHONE_UUID_NEW);
	    phone.setPhonenumber(PHONE);
	    
	    
	    assertTrue(storage.updatePhone(PHONE_UUID_NEW, phone));
	    
	    phone = storage.getPhone(PHONE_UUID_NEW);
	    assertEquals(phone.getUuid(), PHONE_UUID_NEW);
	    assertEquals(phone.getPhonenumber(), PHONE);	
   }
    
    
	/**
	 * 
	 */
	@Ignore
	@Test
	public void testPutPhone(){
		storage = new PhoneDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
	    Phone phone = new Phone();
	    phone.setContactUuid(CONTACTUUID_NEW);
	    phone.setPhonenumber(PHONE);
	    phone.setStatusuuid(STATUSUUID_NEW);
	    phone.setNetworkuuid(NETWORK_UUID_NEW);
	    phone.setUuid(PUTPHONEUUID);
	    
	    assertTrue(storage.putPhone(phone));
	    Phone p = storage.getPhone(PUTPHONEUUID);
	    
    	assertEquals(p.getUuid(), PUTPHONEUUID);
    	assertEquals(p.getPhonenumber() , PHONE );
    	assertEquals(p.getContactUuid() , CONTACTUUID_NEW);
    	assertEquals(p.getStatusuuid() , STATUSUUID_NEW );
    	assertEquals(p.getNetworkuuid() , NETWORK_UUID_NEW );		
	}	
	
	@Test
	public void testPhoneDAO3(){
		
		Contact contact=new Contact();
	
		contact.setUuid(CONTACTUUID);
		storage = new PhoneDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		List<Phone>phoneList =storage.getPhones(PHONENUM, contact);
		
		System.out.println(phoneList.toString());
		
		assertTrue(phoneList.size()>0); 
		
		
		 
		
		
		
	}

}
