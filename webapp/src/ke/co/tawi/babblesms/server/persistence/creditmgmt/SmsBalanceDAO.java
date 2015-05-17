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
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.util.List;


/**
 * Persistence implementation for shortcode and mask balances.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SmsBalanceDAO extends GenericDAO implements BabbleSmsBalanceDAO {

	/**
	 * 
	 */
	public SmsBalanceDAO() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param dbName
	 * @param dbHost
	 * @param dbUsername
	 * @param dbPassword
	 * @param dbPort
	 */
	public SmsBalanceDAO(String dbName, String dbHost, String dbUsername,
			String dbPassword, int dbPort) {
		super(dbName, dbHost, dbUsername, dbPassword, dbPort);
		// TODO Auto-generated constructor stub
	}

	/* (non-Javadoc)
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsBalanceDAO#hasBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)
	 */
	@Override
	public boolean hasBalance(Account account, SMSSource smsSource, int count) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsBalanceDAO#deductBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)
	 */
	@Override
	public boolean deductBalance(Account account, SMSSource smsSource, int count) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsBalanceDAO#addBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)
	 */
	@Override
	public boolean addBalance(Account account, SMSSource smsCode, int amount) {
		// TODO Auto-generated method stub
		return false;
	}

	/* (non-Javadoc)
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsBalanceDAO#getBalances(ke.co.tawi.babblesms.server.beans.account.Account)
	 */
	@Override
	public List<SMSBalance> getBalances(Account account) {
		// TODO Auto-generated method stub
		return null;
	}

	/* (non-Javadoc)
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsBalanceDAO#getAllBalances()
	 */
	@Override
	public List<SMSBalance> getAllBalances() {
		// TODO Auto-generated method stub
		return null;
	}

}
