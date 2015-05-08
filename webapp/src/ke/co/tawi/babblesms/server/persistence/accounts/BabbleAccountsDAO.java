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

import java.util.List;

import ke.co.tawi.babblesms.server.beans.account.Account;

/**
 * Persistence description for {@link Account}.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleAccountsDAO {

    /**
     *
     * @param account
     * @return		<code>true</code> if successfully inserted; <code>false</code>
     * for otherwise
     */
    public boolean putAccount(Account account);

    
    /**
     *	
     *
     * @param account
     * @return	code>true</code> if successfully inserted; <code>false</code> for
     * otherwise
     */
    public boolean updateAccount(Account account);

    
    /**
     *
     * @param uuid
     * @return	a network
     */
    public Account getAccount(String uuid);
    
    
    /**
     *
     * @param name
     * @return	a network
     */
    public Account getAccountByName(String name);

    
    /**
     *
     * @param name
     * @return	a network
     */
    public Account getAccountByEmail(String email);
    
    
    /**
     *
     * @param name
     * @return	a network
     */
    public Account getAccountByEmailuuid(String uuid,String email);


    /**
     *
     * @return	a list of networks
     */
    public List<Account> getAllAccounts();
    
    
    /**
     *
     * @param uuid
     * @param uuid
     * @return		<code>true</code> if successfully updated; <code>false</code> for
     * otherwise
     */
    public boolean adminupdateAccount(Account accounts);

    /**
     *
     * @param uuid
     * @return		<code>true</code> if successfully deleted; <code>false</code> for
     * otherwise
     */
    
    public boolean deleteAccount(String uuid);

}

