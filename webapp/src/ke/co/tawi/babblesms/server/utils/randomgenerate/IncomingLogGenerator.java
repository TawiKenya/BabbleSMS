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
package ke.co.tawi.babblesms.server.utils.randomgenerate;

import org.apache.commons.math3.random.RandomDataGenerator;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * Generates random IncomingSMS that are written to file.
 * <p> 
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class IncomingLogGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Have started IncomingSMSGenerator.");
		File outFile = new File("/tmp/logs/incomingSMS.csv");
		RandomDataGenerator randomDataImpl = new RandomDataGenerator();
		
		String randomStrFile = "/tmp/randomized.txt";
		
		List<String> randomStrings = new ArrayList<>();
		int randStrLength = 0;
				
		long startDate = 1412380800;	// Unix time in seconds for Oct 4 2014
		long stopDate =  1420070340;	// Unix time for in seconds Dec 31 2014
		
		SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); // 2011-06-01 00:16:45" 
		
		List<String> shortcodeUuids = new ArrayList<>();
		shortcodeUuids.add("20240");
		shortcodeUuids.add("20272");
		shortcodeUuids.add("30017");
		shortcodeUuids.add("20100");
		shortcodeUuids.add("10220");
		
		int shortcodeCount = shortcodeUuids.size() - 1;
				
		try {
			randomStrings = FileUtils.readLines(new File(randomStrFile));
			randStrLength = randomStrings.size();
		 	
			for(int j=0; j<30000; j++) {
				FileUtils.write(outFile,
						// shortcodeUuids.get(randomDataImpl.nextInt(0, shortcodeCount)) + "|"	// Destination 
						 UUID.randomUUID().toString() + "|" // Unique Code
						+ randomDataImpl.nextLong(new Long("254700000000").longValue(), new Long("254734999999").longValue()) + "|"	// Origin
						 + shortcodeUuids.get(randomDataImpl.nextInt(0, shortcodeCount)) + "|"
						+ randomStrings.get(randomDataImpl.nextInt(0, randStrLength-1)) + "|" // Message
						+  "N|" // deleted
						+ dateFormatter.format(new Date(randomDataImpl.nextLong(startDate, stopDate) * 1000)) + "\n", // smsTime						
						true); // Append to file                              
			}
			
		} catch(IOException e) {
			System.err.println("IOException in main.");
			e.printStackTrace();
		}			
		
		System.out.println("Have finished IncomingSMSGenerator.");
	}

}
