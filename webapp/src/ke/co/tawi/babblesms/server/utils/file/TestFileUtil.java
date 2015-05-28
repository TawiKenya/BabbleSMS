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
package ke.co.tawi.babblesms.server.utils.file;

import static org.junit.Assert.*;

import org.junit.Test;

/**
 * Test the file utility.
 * <p>
 * Copyright (c) Tawi Commercial Services Ltd., 28 May 2015  
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class TestFileUtil {

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.utils.file.FileUtil#identifyFileType(java.lang.String)}.
	 */
	@Test
	public void testIdentifyFileType() {
		String fileName1 = "/tmp/files/commons-email-1.4-bin.tar.gz";
		String fileName2 = "/tmp/files/Notes.txt";
		String fileName3 = "/tmp/files/ganttproject-2.6.6-r1715.zip";
		String fileName4 = "/tmp/files/rufus-2.1.exe";
		String fileName5 = "/tmp/files/insync_1.2.7.35123-trusty_amd64.deb";
		String fileName6 = "/tmp/files/sample.war";
		String fileName7 = "/tmp/files/Contacts.csv";
		
		System.out.println("File type is: " + FileUtil.identifyFileType(fileName1) );         
		System.out.println("File type is: " + FileUtil.identifyFileType(fileName2) );  
		System.out.println("File type is: " + FileUtil.identifyFileType(fileName3) );
		System.out.println("File type is: " + FileUtil.identifyFileType(fileName4) );
		System.out.println("File type is: " + FileUtil.identifyFileType(fileName5) );
		System.out.println("File type is: " + FileUtil.identifyFileType(fileName6) );
		System.out.println("File type is: " + FileUtil.identifyFileType(fileName7) );
	}

}

