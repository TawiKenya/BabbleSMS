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
package ke.co.tawi.babblesms.server.persistence.smsgw.tawi;

import ke.co.tawi.babblesms.server.beans.smsgateway.TawiGateway;
import ke.co.tawi.babblesms.server.beans.account.Account;

import java.util.Date;
import java.util.List;

/**
 * Persistence description for {@link TawiGateway}
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public interface BabbleGatewayDAO {
    /**
     * 
     * @param account
     * @return
     */
	public TawiGateway get(Account account);
	
	   /**
	    * 
	    * @param username
	    * @return
	    */
	public TawiGateway getByAccountUsername(String username);
	  /**
	   * 
	   * @param gateway
	   * @return
	   */
	public boolean  put(TawiGateway gateway);
	   /**
	    * 
	    * @param gateway
	    * @return
	    */
	public  boolean edit(TawiGateway gateway);
	
	
	   /**
	    * 
	    * @return
	    */
	public List<TawiGateway> getAllRecords();
	
	
	/**
	 * @param account
	 * @param response
	 * @param date
	 */
	public void logResponse(Account account, String response, Date date);
}
