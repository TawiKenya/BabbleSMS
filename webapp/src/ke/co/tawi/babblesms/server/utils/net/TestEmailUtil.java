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
package ke.co.tawi.babblesms.server.utils.net;

import static org.junit.Assert.assertTrue;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests our email utility.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestEmailUtil {
		
	final String[] EMAILS = {"mwendapeter72@gmail.com","mwendapeter72@gmail.com"};
	final String[] EMAILS2 = {"mwendapeter72gmail.com","mwendapeter72gmail.com"};
	final String[] EMAILS3 = {"hfgsghjfgsghfgghjfg","hgfsfghjufgdfghjk"};
	
	final String EMAIL ="mwendapeter72@gmail.com";
	final String EMAIL2 ="mwendapeter72gmail.com";
	final String EMAIL3 ="hhggggujjjiiuugyghghghj";
	
	
	
	final String FROM ="mwenda@tawi.mobi";
	
	final String[] TO ={"mwenda@tawi.mobi","migwi@tawi.mobi","petermwenda83@yahoo.com"};
	
	final String TO2 ="mwenda@tawi.mobi";
	
	final String[] CC ={"mwenda@tawi.mobi","migwi@tawi.mobi"};
	
	final String CC2 ="mwenda@tawi.mobi";
	
	final String[] BCC = {"mwenda@tawi.mobi","migwi@tawi.mobi"};
	
	final String BCC2 = "mwenda@tawi.mobi";
	
	final String SUBJECT ="TAWI!!BABBLE..UnitTest";
	final String BODY ="hello...! this is tawi";
	
	final String OUT_E_SERVER ="mail.tawi.mobi";
	final int OUT_E_PORT =25;
		

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.utils.net.EmailUtil#sendEmail(java.lang.String, java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String, java.lang.String, java.lang.String, int)}.
	 */
	//@Ignore
	@Test
	public void testSendEmailStringStringArrayStringArrayStringArrayStringStringStringInt() {
		EmailUtil util = new EmailUtil(FROM, TO, CC, BCC, SUBJECT, BODY, OUT_E_SERVER, OUT_E_PORT);
		util.run();
		
	}

	
	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.utils.net.EmailUtil#sendEmail(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)}.
	 */
	@Ignore
	@Test
	public void testSendEmailStringStringStringStringStringInt() {
		EmailUtil util = new EmailUtil(FROM, TO2, SUBJECT, BODY, OUT_E_SERVER, OUT_E_PORT);
		util.run();
		
	}	

}
