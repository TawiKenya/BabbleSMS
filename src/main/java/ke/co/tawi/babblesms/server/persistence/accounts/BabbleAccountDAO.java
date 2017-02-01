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
 * Persistence description for {@link Account}s.
 * <p> 
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * 
 */
public interface BabbleAccountDAO {

	
	/**
    *
    * @param uuid
    * @return an {@link Account}
    */
   public Account getAccount(String uuid);
   
   
   /**
   *
   * @param username
   * @return	an {@link Account}
   */
  public Account getAccountByName(String username);
   
  
  /**
  *
  * @return	a list of all {@link Account}s
  */
  public List<Account> getAllAccounts();
 
 
	/**
    *
    * @param account
    * @return <code>true</code> if successfully inserted; <code>false</code>
    * for otherwise
    */
   public boolean putAccount(Account account);
   
   
   /**
   * Update the RDBMS account that has a matching uuid with account given in 
   * the argument. 
   *
   * @param account
   * @return	code>true</code> if successfully updated; <code>false</code> for
   * otherwise
   */
  public boolean updateAccount(String uuid, Account account);
}

