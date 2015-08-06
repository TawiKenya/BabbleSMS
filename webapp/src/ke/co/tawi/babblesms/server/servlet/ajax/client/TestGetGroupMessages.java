/**
 * 
 */
package ke.co.tawi.babblesms.server.servlet.ajax.client;

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
 * @author Migwi Ndung'u <code>mailto:<a>migwindungu0@gmail.com</a>
 *</code> 
 *
 * 
 */
public class TestGetGroupMessages {
	
final String CGI_URL = "http://localhost:8080/BabbleSMS/account/getSentGroupMessages";
	
final String SentGroupUuid="c48222f3-a5df-4d18-a6d2-471ac3e43242";

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.servlet.ajax.client.GetGroupMessages#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)}.
	 */
	@Test
	public void testDoPostHttpServletRequestHttpServletResponse() {
		
		 try {
        	System.out.println("JSON response for SentGroup Uuid '" + SentGroupUuid + "':\n" + 
            		getResponse(CGI_URL + "?" +	"SentGroupUuid=" + URLEncoder.encode(SentGroupUuid, "UTF-8")));
        	
        } catch(UnsupportedEncodingException e) {
        	fail("Test to get SentGroup Messages for SentGroupUuid " + SentGroupUuid);
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
