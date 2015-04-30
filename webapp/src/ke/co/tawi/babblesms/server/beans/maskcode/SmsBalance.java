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
package ke.co.tawi.babblesms.server.beans.maskcode;

import ke.co.tawi.babblesms.server.beans.StorableBean;

import org.apache.commons.lang3.StringUtils;

/**
 * A SmsBalance
 * <p>
 *  
 * @author <a href="mailto:wambua@tawi.mobi">Godfrey Wambua</a>
 */
public class SmsBalance extends StorableBean {

    private String count;
    private String accountuuid;
   

    public SmsBalance() {
        super();
        count = "";
        accountuuid = "";
       
    }
 
    public String getAccountuuid() {
        System.out.println(accountuuid);
       return accountuuid;
   }

   public void setAccountuuid(String accountuuid) {
       //System.out.println(accountuuid);
       this.accountuuid = StringUtils.trimToEmpty(accountuuid);
   }
   
   public String getCount() {
       return count;
   }

   public void setCount(String maskname) {
       this.count = StringUtils.trimToEmpty(maskname);
   }
}