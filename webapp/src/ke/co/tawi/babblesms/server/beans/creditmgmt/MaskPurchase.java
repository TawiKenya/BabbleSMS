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

import org.apache.commons.lang3.StringUtils;

/**
 *  MaskPurchases
 * <p>
 *  
 * @author <a href="mailto:wambua@tawi.mobi">Godfrey Wambua</a>
 */
public class MaskPurchase extends SMSPurchase {

    private String maskuuid;
     
    public MaskPurchase() {
        super();
        maskuuid = "";
    }
 
    
    
   public String getMaskuuid() {
       return maskuuid;
   }
   
   public void setMaskuuid(String maskuuid) {
       this.maskuuid = StringUtils.trimToEmpty(maskuuid);
   }

  

   
   
   @Override
   public String toString() {
       StringBuilder builder = new StringBuilder();
       builder.append("ShortcodePurchases");
       builder.append("[id=");
       builder.append(getId());
       builder.append(", uuid=");
       builder.append(getUuid());
       builder.append(", count=");
       builder.append(getCount());
       builder.append(", accountuuid=");
       builder.append(getAccountUuid());
       builder.append(", networkuuid=");
       builder.append(maskuuid);
       builder.append(", purchasedate=");
       builder.append(getPurchasedate());
       builder.append("]");
       return builder.toString();
   }

    
}
