package ke.co.tawi.babblesms.server.persistence.utils;

import static org.junit.Assert.*;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.persistence.utils.networkcount;

import java.util.HashMap;
import java.util.List;

import org.junit.Test;

public class Testnetworkcount {

	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;

	@Test
	public void testallnetworks() {
		HashMap <String, String> allnetworks = new HashMap<String,String>();
		allnetworks.put("741AC38C-3E40-6887-9CD6-365EF9EA1EF0","Yu KE");
		allnetworks.put("0DE968C9-7309-C481-58F7-AB6CDB1011EF","Airtel KE");
		allnetworks.put("5C1D9939-136A-55DE-FD0E-61D8204E17C9","Orange KE");
		allnetworks.put("B936DA83-8A45-E9F0-2EAE-D75F5C232E78","Safaricom KE");
		networkcount netcount= new networkcount(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		assertEquals(netcount.allnetworks(), allnetworks);
	}
	
	
	public void testcollectcontacts(){
		networkcount netcount= new networkcount(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		/* "a118c8ea-f831-4288-986d-35e22c91fc4d " uuid belongs to management group*/
		List<Contact> count=netcount.collectContacts("a118c8ea-f831-4288-986d-35e22c91fc4d ");
		
		/* "a118c8ea-f831-4288-986d-35e22c91fc4d " uuid belongs to management group*/	
		assertEquals(count,"b9065130-f924-4d95-bce5-8e78db0eeea1"); 
	}
	
	
	
	public void testnetwork(){
		networkcount netcount= new networkcount(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		/* "a118c8ea-f831-4288-986d-35e22c91fc4d " uuid belongs to management group*/
		HashMap<String, String> netest=netcount.network("a118c8ea-f831-4288-986d-35e22c91fc4d");
		
		HashMap<String,String> checknet = new HashMap<String,String>();
		checknet.put("Yu KE", "0 contact(s)");
		
		assertEquals(netest,checknet);
		
	}
	

}
