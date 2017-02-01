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
package ke.co.tawi.babblesms.server.beans.creditmgmt;

import ke.co.tawi.babblesms.server.beans.StorableBean;

import org.apache.commons.lang3.StringUtils;

import java.util.Date;

/**
 * A short code or mask purchase.
 * <p>
 *  
 * @author <a href="mailto:wambua@tawi.mobi">Godfrey Wambua</a>
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SMSPurchase extends StorableBean {

    private int count;
    private String accountUuid;
    private Date purchaseDate;
    private String sourceUuid;	// A shortcode or mask uuid
              
    
    /**
     * 
     */
    protected SMSPurchase() {
        super();
        
        count = 0;
        accountUuid = "";
        purchaseDate = new Date();
    }
 
    /**
     * @return an Account UUID
     */
    public String getAccountUuid() {
       return accountUuid;
   }

   /**
    * @param accountuuid
 	*/
    public void setAccountUuid(String accountuuid) {
       this.accountUuid = StringUtils.trimToEmpty(accountuuid);
   }
   
    
   /**
    * @return the count
 	*/
    public int getCount() {
       return count;
   }

    
   /**
    * @param count
    */
    public void setCount(int count) {
       this.count = count;
   }
    
    
    /**
     * @return the purchaseDate
     */
    public Date getPurchaseDate() {
    	return new Date(purchaseDate.getTime());
    }
    
    
   /**
	 * @param purchasedate
	 */
	public void setPurchaseDate(Date purchasedate) {
       this.purchaseDate = new Date(purchasedate.getTime());
   }
	

	/**
	 * @return the sourceUuid
	 */
	public String getSourceUuid() {
		return sourceUuid;
	}
	
	
	/**
	 * @param sourceUuid the sourceUuid to set
	 */
	public void setSourceUuid(String sourceUuid) {
		this.sourceUuid = sourceUuid;
	}
	
}
