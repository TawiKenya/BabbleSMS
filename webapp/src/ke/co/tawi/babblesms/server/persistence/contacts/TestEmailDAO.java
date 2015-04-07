package ke.co.tawi.babblesms.server.persistence.contacts;

import static org.junit.Assert.*;

import java.util.List;

import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Email;

import org.junit.Ignore;
import org.junit.Test;

public class TestEmailDAO {
	

    final String DB_NAME = "babblesmsdb";
    final String DB_HOST = "localhost";
    final String DB_USERNAME = "babblesms";
    final String DB_PASSWD = "Hymfatsh8";
    final int DB_PORT = 5432;

    final String MAIL_UUID = "05301c8c-e7f8-4f8d-a799-294f566d53f2", MAIL_UUID_NEW = "f5wk681d2e6-84f2-45ff-914c-522a3b076141";

    final String ADDRESS = "mjtyce@nj-0pkvn0y.com",
            ADDRESS_NEW = "kent@gmail.com",
            ADDRESS_UPDATE = "ksanto@gmail.com";

    final String CONTACTUUID = "de14786f-9a41-462d-8df6-7d1b3bf408cd",
            CONTACTUUID_NEW = "8b2900c2-fa2c-4ca8-9a21-c5c24143442f";

    final String ACCUUID = "8038D870-5455-A2D6-18A9-BD5FA1D0A10A",
            ACCUUID_NEW = "650195B6-9357-C147-C24E-7FBDAEEC74ED";

    final String STATUSUUID = "5A13538F-AC41-FDE2-4CD6-B939FA03123B",
            STATUSUUID_NEW = "396F2C7F-961C-5C12-3ABF-867E7FD029E6";

    private EmailDAO emailDAO;

	
	
	
	
	

	@Test
	public void testGetEmail() {
		emailDAO = new EmailDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		Email mail = emailDAO.getEmail(MAIL_UUID);
        assertEquals(mail.getUuid(), MAIL_UUID);
        assertEquals(mail.getAddress(), ADDRESS);
        assertEquals(mail.getContactuuid(), CONTACTUUID);
        assertEquals(mail.getStatusuuid(), STATUSUUID_NEW);
		
		
	}
	@Test
	public void testGetEmailsTextContact() {
	
	Email mail2 = new Email();
		mail2.setContactuuid(CONTACTUUID);
		mail2.setAddress(ADDRESS);
		
    Contact ct = new Contact();
    emailDAO.putEmail(mail2);
    ct.setUuid(CONTACTUUID);
		
    List<Email> email = emailDAO.getEmails("mjtyce@nj" , ct);
     for(Email mail3: email){
		assertEquals(mail3.getAddress(), ADDRESS);
     }
	}		
		
	

	@Test
	public void testGetEmailsContact() {
		emailDAO = new EmailDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
        ContactDAO ctDAO = ContactDAO.getInstance(); 
		Contact contact = ctDAO.getContact(CONTACTUUID);
		List<Email> mails = emailDAO.getEmails(contact);
		
		for(Email mail:mails ){
        assertEquals(mail.getUuid(), MAIL_UUID);
        assertEquals(mail.getAddress(), ADDRESS);
        assertEquals(mail.getContactuuid(), CONTACTUUID);
        assertEquals(mail.getStatusuuid(), STATUSUUID_NEW);
		}
	}
    @Ignore
	@Test
	public void testPutEmail() {
		emailDAO = new EmailDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);

        Email mail = new Email();
        mail.setUuid(MAIL_UUID_NEW);
        mail.setAddress(ADDRESS_NEW);
        mail.setContactuuid(CONTACTUUID_NEW);

        assertTrue(emailDAO.putEmail(mail));

        mail = emailDAO.getEmail(MAIL_UUID_NEW);
        assertEquals(mail.getUuid(), MAIL_UUID_NEW);
        assertEquals(mail.getAddress(), ADDRESS_NEW);
        assertEquals(mail.getContactuuid(), CONTACTUUID_NEW);
		
		
		
		
		
		
		
	}

	@Test
	public void testUpdateEmail() {
		emailDAO = new EmailDAO(DB_NAME, DB_HOST, DB_USERNAME, DB_PASSWD, DB_PORT);
		
		 Email mail = new Email();
	        mail.setUuid(MAIL_UUID_NEW);
	        mail.setAddress(ADDRESS_NEW);
	        mail.setContactuuid(CONTACTUUID_NEW);
	        mail.setStatusuuid(STATUSUUID_NEW);

        assertTrue(emailDAO.updateEmail(MAIL_UUID_NEW, mail));

        Email mail1 = emailDAO.getEmail(MAIL_UUID_NEW);
        assertEquals(mail1.getUuid(), MAIL_UUID_NEW);
        assertEquals(mail1.getAddress(), ADDRESS_UPDATE);
        assertEquals(mail1.getContactuuid(), CONTACTUUID_NEW);
	}

}
