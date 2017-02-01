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

import java.util.AbstractList;
import java.util.List;


/**
 * To split a list into smaller lists of fixed size.
 * <p>
 * Copyright (c) Tawi Commercial Services Ltd., 8 May 2015  
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public class ListPartitioner {

	/** 
	 * Returns consecutive {@linkplain List#subList(int, int) sublists} of a list, 
	 * each of the same size (the final list may be smaller). For example, 
	 * partitioning a list containing {@code [a, b, c, d, e]} with a partition 
	 * size of 3 yields {@code [[a, b, c], [d, e]]} -- an outer list containing 
	 * two inner lists of three and two elements, all in the original order. 
	 * <p>
	 * 
	 * The outer list is unmodifiable, but reflects the latest state of the 
	 * source list. The inner lists are sublist views of the original list, 
	 * produced on demand using {@link List#subList(int, int)}, and are subject 
	 * to all the usual caveats about modification as explained in that API.
	 * <p>
	 *  
	 * Adapted from http://code.google.com/p/google-collections/ 
	 * 
	 * @param list the list to return consecutive sublists of 
	 * @param size the desired size of each sublist (the last may be * smaller) 
	 * @return a list of consecutive sublists * @throws IllegalArgumentException if {@code partitionSize} is nonpositive 
	 **/    
    public static <T> List<List<T>> partition(List<T> list, int size) {
   
     if (list == null)
        throw new NullPointerException("'list' must not be null");
      if (!(size > 0))
        throw new IllegalArgumentException("'size' must be greater than 0");

      return new Partition<T>(list, size);
    }

    
    /**
     *
     * @param <T>
     */
    private static class Partition<T> extends AbstractList<List<T>> {

      final List<T> list;
      final int size;

      Partition(List<T> list, int size) {
        this.list = list;
        this.size = size;
      }

      
    /**
    * @see java.util.AbstractList#get(int)
    */
    @Override
    public List<T> get(int index) {
        int listSize = size();
        if (listSize < 0)
          throw new IllegalArgumentException("negative size: " + listSize);
        if (index < 0)
          throw new IndexOutOfBoundsException("index " + index + " must not be negative");
        if (index >= listSize)
          throw new IndexOutOfBoundsException("index " + index + " must be less than size " + listSize);
        int start = index * size;
        int end = Math.min(start + size, list.size());
        return list.subList(start, end);
    }

    
    /**
     * @see java.util.AbstractCollection#size()
     */
    @Override
    public int size() {
        return (list.size() + size - 1) / size;
    }

    
    /**
     * @see java.util.AbstractCollection#isEmpty()
     */
    @Override
    public boolean isEmpty() {
        return list.isEmpty();
      }
    }

}

