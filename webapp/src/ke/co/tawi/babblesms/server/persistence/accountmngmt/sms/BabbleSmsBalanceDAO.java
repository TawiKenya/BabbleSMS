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

package ke.co.tawi.babblesms.server.persistence.accountmngmt.sms;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.maskcode.SmsBalance;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.maskcode.ShortcodeBalance;
import ke.co.tawi.babblesms.server.beans.maskcode.MaskBalance;

import java.util.List;

/**
 * Abstraction for persistence of bulk SMS balance in an account.
 * <p>
 *
 * 
 * @author <a href="mailto:wambua@tawi.mobi">Godfrey Wambua</a>
 * @version %I%, %G%
 * 
 */
public interface BabbleSmsBalanceDAO {

	/**
	 * Checks whether a user has a certain amount of balance on a particular 
	 * short code.
	 * 
	 * @param account
	 * @param smsCode
	 * @param count
	 * @return whether this account has that balance on the specified SMS gateway
	 */
	public boolean hasBalance(Account account, Shortcode smsCode, int count);
	
	
	/**
	 * Checks whether a user has a certain amount of balance on a particular 
	 * mask.
	 * 
	 * @param account
	 * @param smsMask
	 * @param count
	 * @return boolean
	 */
	public boolean hasBalance(Account account, Mask smsMask, int count);
	
	
	/**
	 * Deducts a certain amount of balance from a user's short code.
	 * 
	 * @param account
	 * @param smsCode
	 * @param count
	 * @return whether it was successful when attempting to deduct the balance 
	 * of this account on the specified SMS gateway
	 */
	public boolean deductBalance(Account account, Shortcode smsCode, int count);
	
	
	/**
	 * Deducts a certain amount of balance from a user's mask.
	 * 
	 * @param account
	 * @param smsMask
	 * @param count
	 * @return  boolean whether it was successful when attempting to deduct the balance 
	 * of this account on the specified SMS gateway
	 */
	public boolean deductBalance(Account account, Mask smsMask, int count);
	
	
	/**
	 * Adds a certain amount of balance from a user's short code.
	 * 
	 * @param account
	 * @param smsCode
	 * @param amount
	 * @return whether it was successful when attempting to add the balance 
	 * of this account on the specified SMS gateway
	 */
	public boolean addBalance(Account account, Shortcode smsCode, int amount);
	
	
	/**
	 * Adds a certain amount of balance from a user's mask.
	 * 
	 * @param account
	 * @param smsMask
	 * @param amount
	 * @return  boolean whether it was successful when attempting to add the balance 
	 * of this account on the specified SMS gateway
	 */
	public boolean addBalance(Account account, Mask smsMask, int amount);
	
	
	
}

