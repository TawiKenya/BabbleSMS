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

import org.apache.commons.lang3.text.WordUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.List;
import java.util.Set;

/**
 * Generic String utilities.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class StringUtil {

	final static int RANDOM_STRING_SIZE = 5;
	
	
	/**
	 * This method should return a string random enough such that there is no
	 * need to do a database lookup to see whether another one exists. It 
	 * therefore relies on a random string generator method together with the
	 * current time to create a String.
	 * 
	 * @return String
	 */
	public static String getRandomStr() {
		
		return RandomStringUtils.randomAlphabetic(RANDOM_STRING_SIZE) + (new Date()).getTime();		
	}
	
	
	/**
	 * Breaks up a long String into shorter strings of a specified length.
	 * <p>
	 * If the String to be broken is shorter than the length, then the size of 
	 * the returned array is one.
	 * <p>
	 * An example of where this function is useful is breaking up a long SMS into
	 * shorter ones.
	 * 
	 * @param toBreak
	 * @param length
	 * @return an array of Strings
	 */
	public static String[] breakupStr(String toBreak, int length) {
		String tokenStr = RandomStringUtils.randomAlphabetic(RANDOM_STRING_SIZE);
		
		String wrappedStr = WordUtils.wrap(toBreak, length, tokenStr, true);
		
		return wrappedStr.split(tokenStr);
	}
	
	
	/**
	 * Eliminate repetitions from an array of Strings.
	 * 
	 * @param strArray
	 * @return an array of Strings where there are no duplicates
	 */
	public static String[] removeDuplicates(String[] strArray) {
		List<String> list = Arrays.asList(strArray);
        Set<String> set = new HashSet<String>(list);
        
        String[] result = new String[set.size()];
        set.toArray(result);
        
        return result;
	}
	
	
	/**
	 * Converts a {@link Map} int a String, for example for logging purposes
	 * 
	 * @param map
	 * @return a String representation of a Map.
	 */
	public static String mapToString(Map<String, String> map) {  
		   StringBuilder stringBuilder = new StringBuilder();  
		  
		   for (String key : map.keySet()) {  
			    if (stringBuilder.length() > 0) {  
			    	stringBuilder.append("&");  
			    }  
			    String value = map.get(key);  
			    
			    stringBuilder.append(StringUtils.trimToEmpty(key));  
			    stringBuilder.append("=");  
			    stringBuilder.append(StringUtils.trimToEmpty(value));
		   }  
		  
		   return stringBuilder.toString();  
	}  
		
}

