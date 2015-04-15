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

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import org.apache.commons.io.FileUtils;

import org.apache.commons.math3.random.RandomDataGenerator;

/**
 * @author michael
 *
 */
public class RandomListGenerator {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		System.out.println("Have started list generator.");
		File outFile = new File("/tmp/randomList.txt");

		RandomDataGenerator generator = new RandomDataGenerator();
		
		int listSize = 503;	// The size of the array / list to be generated
		
		// The Strings to be randomized
		String[] strings = {"0DE968C9-7309-C481-58F7-AB6CDB1011EF", "5C1D9939-136A-55DE-FD0E-61D8204E17C9",
				"B936DA83-8A45-E9F0-2EAE-D75F5C232E78"};
				
		try {
		 	
			for(int j=0; j<listSize; j++) {
				FileUtils.write(outFile,						 
						strings[generator.nextInt(0, strings.length - 1)] 
						+ "\n",					
						true); // Append to file                              
			}
			
		} catch(IOException e) {
			System.err.println("IOException in main.");
			e.printStackTrace();
		}			
		
		System.out.println("Have finished list generator.");
	}

}
