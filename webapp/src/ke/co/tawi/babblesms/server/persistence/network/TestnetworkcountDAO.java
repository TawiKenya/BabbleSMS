package ke.co.tawi.babblesms.server.persistence.network;

import static org.junit.Assert.*;
import ke.co.tawi.babblesms.server.beans.StorableBean;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.GroupContacts;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.persistence.network.networkcountDAO;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.junit.Ignore;
import org.junit.Test;

public class TestnetworkcountDAO {

	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;
	private networkcountDAO netcount= new networkcountDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
	/* "a118c8ea-f831-4288-986d-35e22c91fc4d " uuid belongs to management group*/
	final String management="a118c8ea-f831-4288-986d-35e22c91fc4d";
	final String staff="a2688d72-291b-470c-8926-31a903f5ed0c";
	final String parents="094def52-bc18-4a9e-9b84-c34cc6476c75";
	final String teachers="e9570c5d-0cc4-41e5-81df-b0674e9dda1e";
	final String students="9bef62f6-e682-4efd-98e9-ca41fa4ef993";
					
	final String safaricom = "B936DA83-8A45-E9F0-2EAE-D75F5C232E78";
	final String airtel="0DE968C9-7309-C481-58F7-AB6CDB1011EF";
	final String yu="741AC38C-3E40-6887-9CD6-365EF9EA1EF0";
	final String orange="5C1D9939-136A-55DE-FD0E-61D8204E17C9";
	final String contactuuid="556e1c97-362c-4410-8e92-cd287e4c5c9b",
	             contactuuid_new="556e1c97-362c-4410-8e92-cd287e4c5c9bnew";
	final String phoneNo="254722347063",
	             PhoneNo_new="254722347063new";
	final String statusuuid="396F2C7F-961C-5C12-3ABF-867E7FD029E6",
	             statusuuid_new="396F2C7F-961C-5C12-3ABF-867E7FD029E6new";
	final String phoneuuid="9c918b90-68f2-4d14-a9dd-969f2318f077",
	             phoneuuid_new="9c918b90-68f2-4d14-a9dd-969f2318f077new";

	
	@Test
	public void testallnetworks() {
		HashMap <String, String> allnetworks = new HashMap<String,String>();
		allnetworks.put("741AC38C-3E40-6887-9CD6-365EF9EA1EF0","Yu KE");
		allnetworks.put("0DE968C9-7309-C481-58F7-AB6CDB1011EF","Airtel KE");
		allnetworks.put("5C1D9939-136A-55DE-FD0E-61D8204E17C9","Orange KE");
		allnetworks.put("B936DA83-8A45-E9F0-2EAE-D75F5C232E78","Safaricom KE");
		allnetworks.put("fp769681d2e6-84f2-45ff-914c-522a3b076141","Safaricom UG");
				
		assertEquals(netcount.allnetworks(), allnetworks);
	}
	
	@Test
	/**count all contacts*/
	public void testcollectcontacts(){		
		List<GroupContacts> count=netcount.collectContacts(students);	
		
		assertEquals(count.size(),75); 
	}
	
	
	
	@Test
	public void testnetwork(){	
		
		HashMap<String, String> netest=netcount.network(students);
		
		HashMap<String,String> checknet = new HashMap<String,String>();
		checknet.put("Yu KE", "1 contact(s)");
		checknet.put("Safaricom KE", "27 contact(s)");
		checknet.put("Orange KE", "31 contact(s)");
		checknet.put("Safaricom UG","0 contact(s)");
		checknet.put("Airtel KE", "20 contact(s)");
		checknet.put("Total contacts","79 contact(s)");   
		
		assertEquals(netest,checknet);
		
	}
	
	
	@Test
	public void testcontactspernetwork(){
	List<Phone> phone=new ArrayList<Phone>();
	phone=netcount.contactspernetwork(students, yu);
		assertEquals(phone.size(),1);	
				
	}
	
	/**counts all phone no.*/
	@Test
	public void testallgrpcontacts(){
		List<Phone> phone=new ArrayList<Phone>();
		phone=netcount.allgrpcontacts(students);
		   assertEquals(phone.size(),79);
	}
	

}

