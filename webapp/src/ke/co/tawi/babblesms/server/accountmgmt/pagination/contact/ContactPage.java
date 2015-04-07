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
package ke.co.tawi.babblesms.server.accountmgmt.pagination.contact;

import java.util.ArrayList;
import java.util.List;

import ke.co.tawi.babblesms.server.beans.contact.Contact;

/**
 * An abstraction of a Page that is part of pagination on the JSP view.
 * <p>
 *  
 * @author <a href="mailto:dennism@tawi.mobii">Dennis Mutegi</a>
 *
 */
public class ContactPage {
	private int pageNum;
	private int totalPage;
	private int pagesize;
    private List<Contact> contents;
    
    /**
	 * 
	 */
	public ContactPage (){
		pageNum = 1;
		totalPage = 1;
		pagesize = 1;
		contents = new ArrayList<>();
	}
	
	/**
	 * 
	 * @param pageNum
	 * @param totalPage
	 * @param pagesize
	 * @param contents
	 */
	public ContactPage(final int pageNum, final int totalPage, final int pagesize,
			final List<Contact> contents) {
		this.pageNum = pageNum;
		this.totalPage = totalPage;
		this.pagesize = pagesize;
		this.contents = contents;
	}

	
	/**
	 * 
	 * @return int
	 */
	public int getPageNum() {
		return pageNum;
	}

	
	/**
	 * 
	 * @return int
	 */
	public int getTotalPage() {
		return totalPage;
	}

	
	/**
	 * 
	 * @return int
	 */
	public int getPagesize() {
		return pagesize;
	}

	
	/** 
	 * 
	 * @return List<IncomingSMS> 
	 */
	public List<Contact> getContents() {
		return new ArrayList<Contact>(contents);		
	}
	
    /**
    *
    * @return		<code>true</code> if this is the first page; <code>false</code>
    * for otherwise
    */
   public boolean isFirstPage() {
       return pageNum == 1;
   }
   
   /**
   *
   * @return		<code>true</code> if this is the last page; <code>false</code>
   * for otherwise
   */
  public boolean isLastPage() {
      return pageNum == totalPage;
  }
}
