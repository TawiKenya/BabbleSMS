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

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;

import org.junit.Test;

import static org.junit.Assert.assertTrue;


/**
 * Tests our {@link ListPartitioner}.
 * <p>
 * Copyright (c) Tawi Commercial Services Ltd., 8 May 2015  
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class TestListPartitioner {

	/**
	 * Test method for {@link ke.co.tawi.babblesms.server.utils.ListPartitioner#partition(java.util.List, int)}.
	 */
	@Test
	public void testPartition() {
		List<String> list = new ArrayList<String>();
	    list.add("one");
	    list.add("two");
	    list.add("three");
	    list.add("four");
	    list.add("five");
	    list.add("six");
	    list.add("seven");
	    list.add("eight");
	    list.add("nine");
	    list.add("ten");
	    list.add("eleven");
	    List<List<String>> partition = ListPartitioner.partition(list, 1);
	    System.out.println(partition.get(2).size()); 
	    assertTrue(partition.size()==11);
	    assertTrue(partition.get(0).size()==1);
	    partition = ListPartitioner.partition(list, 7);
	    assertTrue(partition.size()==2);
	    assertTrue(partition.get(0).size()==7);
	    assertTrue(partition.get(1).size()==4);
	    
	    // now let assume you want to have x number of buckets
	    // How many elements must placed in a list?
	    // Take x as 3
	    
	    int buckets = 3;
	    int divide = list.size() / buckets;  
	    if (list.size() % buckets !=0){
	      divide ++;
	    }
	    System.out.println("Max. number of element in each bucket " + divide);
	    partition = ListPartitioner.partition(list, divide);
	    assertTrue(partition.size()==buckets);
  }
	

}

