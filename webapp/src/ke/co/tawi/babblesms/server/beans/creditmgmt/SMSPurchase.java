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

    /*CREATE TABLE ShortcodePurchase(
            
            shortcodeuuid text references Shortcode(uuid),
            count integer NOT NULL CHECK (count>=0),
            
            
            CREATE TABLE MaskPurchase(
                   
                    maskuuid text references Mask(uuid),
                    count integer NOT NULL CHECK (count>=0),
                    );*/
            
    
    /**
     * 
     */
    public SMSPurchase() {
        super();
        count = 0;
        accountUuid = "";
        purchaseDate = new Date();
    }
 
    public String getAccountUuid() {
       return accountUuid;
   }

   public void setAccountUuid(String accountuuid) {
       this.accountUuid = StringUtils.trimToEmpty(accountuuid);
   }
   
   public int getCount() {
       return count;
   }

   public void setCount(int count) {
       this.count = count;
   }
    
   

   /**
	 * @param purchasedate
	 */
	public void setPurchaseDate(Date purchasedate) {
       this.purchaseDate = new Date(purchasedate.getTime());
   }
	
    
   /**
	 * @return the purchase date
	 */
   public Date getPurchasedate() {
       return new Date(purchaseDate.getTime());
   }

   
   
   @Override
   public String toString() {
       StringBuilder builder = new StringBuilder();
       builder.append("ShortcodePurchases");
       builder.append("[id=");
       builder.append(getId());
       builder.append(", uuid=");
       builder.append(getUuid());
       builder.append(", maskname=");
       builder.append(count);
       builder.append(", accountuuid=");
       builder.append(accountUuid);
       builder.append(", purchasedate=");
       builder.append(purchaseDate);
       builder.append("]");
       return builder.toString();
   }

    
}
