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

import static org.junit.Assert.*;
import ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO;

import org.junit.Ignore;
import org.junit.Test;

/**
 * Tests our persistence implementation for shortcode and mask balances.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestEmailUtil {
	

	final String DB_NAME = "babblesmsdb";
	final String DB_HOST = "localhost";
	final String DB_USERNAME = "babblesms";
	final String DB_PASSWD = "Hymfatsh8";
	final int DB_PORT = 5432;
	
	
	
	
	
	
	private SmsBalanceDAO storage;

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.utils.net.EmailUtil#sendEmail(java.lang.String, java.lang.String[], java.lang.String[], java.lang.String[], java.lang.String, java.lang.String, java.lang.String, int)}.
	 */
	@Ignore
	@Test
	public void testSendEmailStringStringArrayStringArrayStringArrayStringStringStringInt() {
		
		
		
		
	}

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.utils.net.EmailUtil#sendEmail(java.lang.String, java.lang.String, java.lang.String, java.lang.String, java.lang.String, int)}.
	 */
	@Ignore
	@Test
	public void testSendEmailStringStringStringStringStringInt() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.utils.net.EmailUtil#validateEmail(java.lang.String)}.
	 */
	@Ignore
	@Test
	public void testValidateEmail() {
		fail("Not yet implemented");
	}

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.utils.net.EmailUtil#validateEmails(java.lang.String[])}.
	 */
	@Ignore
	@Test
	public void testValidateEmails() {
		fail("Not yet implemented");
	}

}
