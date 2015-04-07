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

import ke.co.tawi.babblesms.server.beans.StorableBean;

/**
 * Balance of an account within a network (MNO).
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class AccountBalance extends StorableBean {

    private String origin;
    private String accountuuid;
    private String networkuuid;
    private int balance;

    /**
     * Constructor
     */
    public AccountBalance() {
        super();
        origin = "";
        accountuuid = "";
        networkuuid = "";
        balance = 0;
    }

    /**
     *
     * @return origin
     */
    public String getOrigin() {
        return origin;
    }

    /**
     *
     * @param origin
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     * @return accountuuid
     */
    public String getAccountuuid() {
        return accountuuid;
    }

    /**
     *
     * @param accountuuid
     */
    public void setAccountuuid(String accountuuid) {
        this.accountuuid = accountuuid;
    }

    /**
     *
     * @return networkuuid
     */
    public String getNetworkuuid() {
        return networkuuid;
    }

    /**
     *
     * @param networkuuid
     */
    public void setNetworkuuid(String networkuuid) {
        this.networkuuid = networkuuid;
    }

    /**
     *
     * @return balance
     */
    public int getBalance() {
        return balance;
    }

    /**
     *
     * @param balance
     */
    public void setBalance(int balance) {
        this.balance = balance;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("[id=");
        builder.append(getId());
        builder.append(",uuid=");
        builder.append(getUuid());
        builder.append(",origin=");
        builder.append(origin);
        builder.append(",accountuuid=");
        builder.append(accountuuid);
        builder.append(",networkuuid=");
        builder.append(networkuuid);
        builder.append(",balance=");
        builder.append(balance);
        builder.append("]");
        return builder.toString();
    }
}
