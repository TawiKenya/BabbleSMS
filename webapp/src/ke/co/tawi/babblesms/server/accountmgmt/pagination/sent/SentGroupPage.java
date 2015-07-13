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
package ke.co.tawi.babblesms.server.accountmgmt.pagination.sent;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.log.OutgoingGrouplog;

/**
 * Paginate the SentGroup page
 * 
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>*/

public class SentGroupPage implements Serializable {
  
	private int pageNumber;
	private int totalSize;
	private int pageSize;
	private List<OutgoingGrouplog> outGoingGroupList;
	
	
	public SentGroupPage(){
		pageNumber=1;
		totalSize=1;
		pageSize=1;
		outGoingGroupList=new ArrayList<>();
	}
	/**
	 * Sets this parameters
	 * @param int pageNumber
	 * @param int totalSize
	 * @param int pageSize
	 * @param list of {@link of OutgoingGrouplog}
	 *  */
	
	public SentGroupPage(final int pageNumber, final int totalSize, final int pageSize, 
			final List<OutgoingGrouplog> outGoingGroupList){
		this.pageNumber=pageNumber;
		this.totalSize = totalSize;
		this.pageSize = pageSize;
		this.outGoingGroupList = outGoingGroupList;
	}
	
	/** 
	 * Sets the PageNumber
	 * @param int pageNumber
	 * */
	
	public void setPageNumber(int pageNumber){
		this.pageNumber=pageNumber;
	}
	
	   /**
	    * Sets the totalSize
	     * @param totalSize
	     *  */
	   public void setTotalSize(int totalSize){
		this.totalSize = totalSize;
	    }
	
	   /**
	   * Sets the PageSize
	   * @param pageSize
	   * */
	     public void setPageSize(int pageSize){
		 this.pageSize = pageSize;
	     }
	
	  /**
        * Sets the list of {@link of OutgoingGrouplog}
	    * @param list of {@link of OutgoingGrouplog}
	    * */
	      public void setOutGoingGroupList(List<OutgoingGrouplog> outGoingGroupList){
		  this.outGoingGroupList = outGoingGroupList;
	     }
	
	    /**
	    * @return pageNumber
	    * */
	     public int getPageNumber(){
		return pageNumber;
	    }
	
	   /**
	    * @return totalSize
	    * */
	    public int getTotalSize(){
		return totalSize;
	     }
	
	   /**
	    * @return pageSize
	    * */
	    public int getPageSize(){
		return pageSize;
	     }
	
	
	 /**
	 * @return list of {@link of OutgoingGrouplog}
	 * */
	    public List<OutgoingGrouplog> getOutGoingGroupList(){
		return outGoingGroupList;
	    }
	    
	    /**
	     *
	     * @return		<code>true</code> if this is the first page; <code>false</code>
	     * for otherwise
	     */
	    public boolean isFirstPage() {
	        return pageNumber == 1;
	    }

	    /**
	     *
	     * @return		<code>true</code> if this is the last page; <code>false</code>
	     * for otherwise
	     */
	    public boolean isLastPage() {
	        return pageNumber == totalSize;
	    }
	    
	   
}
