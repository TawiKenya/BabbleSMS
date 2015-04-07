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
package ke.co.tawi.babblesms.server.beans.account;

import java.util.Date;
import ke.co.tawi.babblesms.server.beans.StorableBean;

/**
 * The SMS credit purchase history of a user.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class PurchaseHistory extends StorableBean {

    private int amount;
    private String source;
    private Date purchasetime;
    private String accountuuid;
    private String networkuuid;

    public PurchaseHistory() {
        super();
        amount = 0;
        source = "";
        purchasetime = new Date();
        accountuuid = "";
        networkuuid = "";
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getSource() {
        return source;
    }

    public void setSource(String source) {
        this.source = source;
    }

    public Date getPurchasetime() {
        return purchasetime;
    }

    public void setPurchasetime(Date purchasetime) {
        this.purchasetime = new Date(purchasetime.getTime());
    }

    public String getAccountuuid() {
        return accountuuid;
    }

    public void setAccountuuid(String accountuuid) {
        this.accountuuid = accountuuid;
    }

    public String getNetworkuuid() {
        return networkuuid;
    }

    public void setNetworkuuid(String networkuuid) {
        this.networkuuid = networkuuid;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("PurchaseHistory ");
        builder.append("[id=");
        builder.append(getId());
        builder.append(", uuid=");
        builder.append(getUuid());
        builder.append(", amount=");
        builder.append(amount);
        builder.append(", source=");
        builder.append(source);
        builder.append(", purchasetime=");
        builder.append(purchasetime);
        builder.append(", accountuuid=");
        builder.append(accountuuid);
        builder.append(", networkuuid=");
        builder.append(networkuuid);
        builder.append("]");
        return builder.toString();
    }

}
