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
package ke.co.tawi.babblesms.server.persistence.accounts;

import ke.co.tawi.babblesms.server.beans.account.AccountBalance;

import java.util.List;

/**
 * Persistence description for the account balance.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleAccountBalanceDAO {

    /**
     * 
     * @param uuid
     * @param balance
     * @return <code>true</code>if client balance updated successfully
     */
    public boolean addCredit(String uuid,int balance);
    
    /**
     * 
     * @param uuid
     * @param balance
     * @return <code>true</code>if client balance updated successfully
     */
    public boolean deductCredit(String source, String networkuuid, int balance);
    
    /**
     *
     * @return	a list of client balance
     */
    public List<AccountBalance> getClientBalance();
    
    
    /**
     *
     * @param email
     * @return	a list of client balance
     */
    public List<AccountBalance> getClientBalanceByAccountEmail(String email);
    
    
    /**
     *
     * @param shortcode
     * @return	a list of client balance
     */
    public List<AccountBalance> getClientBalanceByShortcode(String shortcode);
    
    /**
     *
     * @param name
     * @return	a list of client balance
     */
    public List<AccountBalance> getClientBalanceByAccount(String name);
    
    
    /**
     *
     * @param uuid
     * @return	account balance values
     */
    public AccountBalance getClientBalanceByUuid(String uuid);
    
    
    /**
     *
     * @param accountuuid
     * @param source
     * @param networkuuid
     * @return	account balance values
     */
    public AccountBalance getClientBalanceBynetwork(String accountuuid, String source, String networkuuid);
}
