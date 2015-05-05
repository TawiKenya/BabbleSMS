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
package ke.co.tawi.babblesms.server.utils;

import java.util.Arrays;

import static org.junit.Assert.*;

import org.junit.Ignore;
import org.junit.Test;


/**
 * Tests our {@link StringUtil}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class TestStringUtil {

	final String STRING_TO_BREAK1 = "My mum is cooking",
			
			// This string is 413 characters long, and should fit in 3 sms.
			STRING_TO_BREAK2 = "Societal pressure, however, led to my pursuing engineering. Animation, like all other art-based "
					+ "careers, has unfairly received a wide shun in developing countries with many assuming it is a waste of time. "
					+ "As a result, many talented individuals have been forced to settle for 'better paying' traditional careers. "
					+ "Nairobi Governor Evans Kidero is now eyeing a top leadership position in the Orange Democratic Party.";
	
	final int STRING_LENGTH = 160;
	
	/**
	 * Test method for {@link mobi.tawi.smsgw.util.StringUtil#getRandomStr()}.
	 */
	@Ignore
	@Test
	public void testGetRandomStr() {		
		/*
		System.out.println("Random string 1 is: " + StringUtil.getRandomStr());
		System.out.println("Random string 2 is: " + StringUtil.getRandomStr());
		System.out.println("Random string 3 is: " + StringUtil.getRandomStr());
		System.out.println("Random string 4 is: " + StringUtil.getRandomStr());
		System.out.println("Random string 5 is: " + StringUtil.getRandomStr());
		*/
		
		/*
		for(int j=0; j<2783; j++) { // 
			System.out.println(StringUtil.getRandomStr());
		}*/
	}
	
	
	/**
	 * Test method for {@link mobi.tawi.smsgw.util.StringUtil#getRandomStr()}.
	 */
	@Ignore
	@Test
	public void testBreakupStr() {
		String[] broken = StringUtil.breakupStr(STRING_TO_BREAK1, STRING_LENGTH);
		assertEquals(broken.length, 1);
		System.out.println(broken[0]);
		
		broken = StringUtil.breakupStr(STRING_TO_BREAK2, STRING_LENGTH);
		assertEquals(broken.length, 3);
		System.out.println(broken[0]);
		System.out.println(broken[1]);
		System.out.println(broken[2]);
	}

	
	/**
	 * Test method for {@link mobi.tawi.smsgw.util.StringUtil#removeDuplicates(String[])}.
	 */
	@Test
	public void testRemoveDuplicates() {
		String[] data = {
                "A", "C", "B", "D", "A", "B", "E", "D", "B", "C", "A"
        };
		
        System.out.println("Original array: " + Arrays.toString(data));
        
        String[] uniqueData = StringUtil.removeDuplicates(data);
        
        System.out.println("New array: " + Arrays.toString(uniqueData));
	}
}

