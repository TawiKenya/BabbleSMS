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
package ke.co.tawi.babblesms.server.persistence.creditmgmt;

import ke.co.tawi.babblesms.server.beans.account.Account;

import ke.co.tawi.babblesms.server.beans.creditmgmt.SMSBalance;
import ke.co.tawi.babblesms.server.beans.maskcode.SMSSource;

import java.util.List;


/**
 * Persistence description for shortcode and mask balances.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleSmsBalanceDAO {

	/**
	 * Checks whether a user has a certain amount of balance on a particular 
	 * short code or mask.
	 * 
	 * @param account
	 * @param smsSource
	 * @param count
	 * @return whether this account has that balance on the specified SMS gateway
	 */
	public boolean hasBalance(Account account, SMSSource smsSource, int count);
	
	
	
	/**
	 * Deducts a certain amount of balance from a user's short code or mask.
	 * 
	 * @param account
	 * @param smsSource
	 * @param count
	 * @return whether it was successful when attempting to deduct the balance 
	 * 
	 */
	public boolean deductBalance(Account account, SMSSource smsSource, int count);	
		
	
	/**
	 * Adds a certain amount of balance from a user's short code or mask.
	 * 
	 * @param account
	 * @param smsCode
	 * @param amount
	 * @return whether it was successful when attempting to add the balance 
	 */
	public boolean addBalance(Account account, SMSSource smsSource, int amount);
	 /**
	  * 
	  * @param account
	  * @param smsSource
	  * @param amount
	  * @return whether the balance was successfully edited or not 
	  */
	public boolean updateBalance(Account account, SMSBalance smsbal, int amount);
			
	
	/**
	 * 
	 * @param account
	 * @return a list of balances belonging to this account
	 */
	public List<SMSBalance> getBalances(Account account);	
	
	
	/**
	 * 
	 * @param account
	 * @return a list of all balances
	 */
	public List<SMSBalance> getAllBalances();
}
