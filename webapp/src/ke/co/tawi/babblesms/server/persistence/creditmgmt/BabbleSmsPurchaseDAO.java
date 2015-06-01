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
import ke.co.tawi.babblesms.server.beans.creditmgmt.SMSPurchase;
import ke.co.tawi.babblesms.server.beans.creditmgmt.ShortcodePurchase;
import ke.co.tawi.babblesms.server.beans.creditmgmt.MaskPurchase;

import java.util.List;

/**
 * Persistence description for shortcode and mask purchases.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleSmsPurchaseDAO {

	
	/**
	 * To be called when a client purchases SMS credits on a Short Code or Mask.
	 * 
	 * @param purchase
	 * @return whether or not the purchase action was successful
	 */
	public boolean put(SMSPurchase purchase);
	
	
	/**
	 * Returns a list of both {@link ShortcodePurchase}s and 
	 * {@link MaskPurchase}s.
	 * 
	 * @param account
	 * @return a list of purchases done by this client account.
	 */
	public List<SMSPurchase> getPurchases(Account account);
	
	
	/**
	 * Returns a list of both {@link ShortcodePurchase}s and 
	 * {@link MaskPurchase}s. 
	 * 
	 * @return a list of purchases done by all client accounts.
	 */
	public List<SMSPurchase> getAllPurchases();
}
