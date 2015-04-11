package ke.co.tawi.babblesms.server.servlet.upload;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Email;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.EmailDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.supercsv.cellprocessor.Optional;
import org.supercsv.cellprocessor.constraint.NotNull;
import org.supercsv.cellprocessor.ift.CellProcessor;
import org.supercsv.io.CsvBeanReader;
import org.supercsv.io.ICsvBeanReader;
import org.supercsv.prefs.CsvPreference;
import org.supercsv.exception.SuperCsvException;
 
/**
 * This program demonstrates how to read CSV file using SuperCSV library.
 * Each line is read into a JavaBean class (POJO).
 * @author www.codejava.net
 *
 */
public class ContactUploadReader {
	
	
    
	
	
	static String readCSVFile(String csvFileName) {
    //static void readCSVFile(String csvFileName) {
		EmailDAO emailDAO = EmailDAO.getInstance();
	    PhoneDAO phoneDAO = PhoneDAO.getInstance();
	    ContactDAO ctDAO = ContactDAO.getInstance();
	    ContactGroupDAO cgDAO = ContactGroupDAO.getInstance();
	    CacheManager mgr = CacheManager.getInstance();

	    
		Contact ct;
	    Email mail;
	    Phone phn;
	    
        String msg = "";
        String statusuuid = "396F2C7F-961C-5C12-3ABF-867E7FD029E6";
        String accountuuid = "650195B6-9357-C147-C24E-7FBDAEEC74ED";
    	
    	ICsvBeanReader beanReader = null;
        CellProcessor[] processors = new CellProcessor[] {
                new NotNull(), //uuid
                new NotNull(), //name
                new NotNull(), //phone
                new Optional(), // email()no processing is required (the String is used unchanged). Semantically, 
                				//it might have been better to replace that with new Optional(), which means the same thing. 
                				//If we wanted to guarantee that name was supplied (i.e. it's mandatory), then we could have used new NotNull()
                new NotNull() //network
               
                
        };
     
        try {
        	    
        	//ContactDAO contactDAO = ContactDAO.getInstace();
        	//saves eugene = new saves();
            beanReader = new CsvBeanReader(new FileReader(csvFileName),
                    CsvPreference.STANDARD_PREFERENCE);
            String[] header = beanReader.getHeader(true);
            ContactUpload contactBean = null;
            while ((contactBean = beanReader.read(ContactUpload.class, header, processors)) != null) {
                /**System.out.printf("%s %-20s %-20s %-20s %-20s ",
                		contactBean.getUuid(), contactBean.getName(),
                        contactBean.getPhone(), contactBean.getEmail(),
                        contactBean.getNetwork());
                System.out.println();**/
            	
            	
                //add contact.
            	ct = new Contact();
                ct.setName(contactBean.getName());
                ct.setStatusUuid(statusuuid);
                ct.setAccountUuid(accountuuid);
                
                //eugene.savecontact(s);
                
                if(!ctDAO.putContact(ct)){
                	msg = "Could not save in db";
                }
                
                //loop emails
                //for (String email2 : emailArray) {
                    mail = new Email();
                    mail.setAddress(contactBean.getEmail());
                    mail.setContactuuid(ct.getUuid());
                    //mail.setStatusuuid(statusuuid);

                    emailDAO.putEmail(mail);
                    //get uuid to update cache
                    mail = emailDAO.getEmail(contactBean.getEmail());

                    //logger.info(mail + " added sucessfully");
                    mgr.getCache(CacheVariables.CACHE_EMAIL_BY_UUID).put(new Element(mail.getUuid(), mail));
                //}
                    
                    msg = "Successfully uploaded Contacts";
                    
                
            }
        } catch (SuperCsvException ex){
            //logger.log(Level.SEVERE, "ERROR ON ROW "+beanReader.getRowNumber(), ex);
                      //treatedOk = false;
        	msg = "File Upload Failed!! make sure your file extention is .csv and it conforms to this formart(uuid,name,phone,email,network) ";
       
        } catch (FileNotFoundException ex) {
            System.err.println("Could not find the CSV file: " + ex);
              msg = "Could not find the CSV file: " + ex;
        } catch (IOException ex) {
            System.err.println("Error reading the CSV file: " + ex);
              msg = "Error reading the CSV file: " + ex;
              
        } finally {
            if (beanReader != null) {
                try {
                    beanReader.close();
                } catch (IOException ex) {
                    System.err.println("Error closing the reader: " + ex);
                    msg = "Error closing the reader: " + ex;
                }
            }
        }
      //delete the file after you have or havent performed operations on it.
        File f = new File(csvFileName);
        f.delete();
        return msg;
    }
 
    
    /**
    public static void main(String[] args) {
    	File f = new File("/tmp/contact.csv");
        String csvFileName =f.getAbsolutePath();
        readCSVFile(csvFileName);
        //f.delete();
    }**/
}