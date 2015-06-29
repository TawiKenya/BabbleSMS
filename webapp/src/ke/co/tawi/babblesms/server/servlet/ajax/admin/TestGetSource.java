/**
 * 
 */
package ke.co.tawi.babblesms.server.servlet.ajax.admin;

import static org.junit.Assert.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;

import org.junit.Test;

/**
 * @author peter
 *
 */
public class TestGetSource {

final String CGI_URL = "http://localhost:8080/BabbleSMS/admin/getSource";
	
	final String ACCOUNTUUID_DEMO = "650195B6-9357-C147-C24E-7FBDAEEC74ED";
	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.servlet.ajax.admin.GetSource#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 */
	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() {
		
		 try {
        	System.out.println("JSON response for account uuid '" + ACCOUNTUUID_DEMO + "':\n" + 
            		getResponse(CGI_URL + "?" +	"accountuuid=" + URLEncoder.encode(ACCOUNTUUID_DEMO, "UTF-8")));
        	
        } catch(UnsupportedEncodingException e) {
        	fail("Test to get Groups for account uuid " + ACCOUNTUUID_DEMO);
        	e.printStackTrace();
        }
	}

	
	/**
	 * 
	 * @param urlStr
	 * @return String
	 */
	private String getResponse(String urlStr) {		
        URLConnection conn;
        URL url;
        BufferedReader reader;
		String line;
		StringBuffer stringBuff = new StringBuffer();
		
		try {            
            url = new URL(urlStr);
            conn = url.openConnection();
            conn.setDoInput(true);
            conn.setDoOutput(true); 
            
            reader = new BufferedReader(new InputStreamReader(conn.getInputStream())); 
            while( (line = reader.readLine()) != null) {
            	stringBuff.append(line);
            }
            
            reader.close();
            
        } catch(MalformedURLException e) {
            System.err.println("MalformedURLException exception");
            e.printStackTrace();
            
        } catch(IOException e) {
            System.err.println("IOException exception");
            e.printStackTrace();
        }
        
		return stringBuff.toString();
	}

}
