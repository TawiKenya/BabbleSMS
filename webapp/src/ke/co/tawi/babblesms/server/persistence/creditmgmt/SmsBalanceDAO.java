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
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.beans.maskcode.SMSSource;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;


/**
 * Persistence implementation for shortcode and mask balances.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SmsBalanceDAO extends GenericDAO implements BabbleSmsBalanceDAO {

	private static SmsBalanceDAO balanceDAO;
	
	private BeanProcessor beanProcessor = new BeanProcessor();
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	
	/**
     * @return the singleton instance of {@link SmsBalanceDAO}
     */
	public static SmsBalanceDAO getInstance(){
		if(balanceDAO == null){
			balanceDAO = new SmsBalanceDAO();
		}
		
		return balanceDAO;		
	}
	
	
	/**
	 * 
	 */
	protected SmsBalanceDAO() {
		super();
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
	}
	

	/**
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsBalanceDAO#hasBalance(Account, SMSSource, int)
	 */
	@Override
	public boolean hasBalance(Account account, SMSSource smsSource, int count) {
		boolean hasBalance = false;
		int smsCount = 0;		
				
		try(
				Connection conn = dbCredentials.getConnection();
				PreparedStatement pstmt = conn.prepareStatement("SELECT count FROM ShortcodeBalance "
						+ "WHERE accountUuid=? AND Shortcodeuuid=?;");
				PreparedStatement pstmt2 = conn.prepareStatement("SELECT count FROM MaskBalance "
						+ "WHERE accountUuid=? AND maskuuid=?;");
				) {	
			
			pstmt.setString(1, account.getUuid());	
			pstmt.setString(2, smsSource.getUuid());
			
			pstmt2.setString(1, account.getUuid());	
			pstmt2.setString(2, smsSource.getUuid());
						
			try(
					ResultSet rset = pstmt.executeQuery();
					ResultSet rset2 = pstmt2.executeQuery();
					) {
				
				if(rset.next()) {
					smsCount = rset.getInt("count");	
					hasBalance = (smsCount >= count) ? true : false;
							
				} else if(rset2.next()) {
					smsCount = rset2.getInt("count");	
					hasBalance = (smsCount >= count) ? true : false;
				}
				
			}
			
			
		} catch(SQLException e) {
			logger.error("SQLException exception while checking whether '" + account +
					"' has balance of " + count + " for '" + smsSource + "'.");
			logger.error(ExceptionUtils.getStackTrace(e));
			
		} 
		
		return hasBalance;
	}
	

	/**
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsBalanceDAO#deductBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)
	 */
	@Override
	public boolean deductBalance(Account account, SMSSource smsSource, int count) {
		boolean success = true;
						
		if(hasBalance(account, smsSource, count)) {
		
			try(
					Connection conn = dbCredentials.getConnection();	
					PreparedStatement pstmt = conn.prepareStatement("UPDATE ? " +
						"SET count = (SELECT count FROM ? WHERE accountUuid=? AND ?=?) " +
						"- ? " +				
						"WHERE uuid = (SELECT uuid FROM ? WHERE accountUuid=? "
						+ "AND ?=?);");	
					) {
				
				if(smsSource instanceof Shortcode) {
					pstmt.setString(1, "ShortcodeBalance");
					pstmt.setString(2, "ShortcodeBalance");
					pstmt.setString(4, "Shortcodeuuid");
					pstmt.setString(7, "ShortcodeBalance");
					pstmt.setString(9, "Shortcodeuuid");
					
				} else {	// This is a mask
					pstmt.setString(1, "MaskBalance");
					pstmt.setString(2, "MaskBalance");
					pstmt.setString(4, "maskuuid");
					pstmt.setString(7, "MaskBalance");
					pstmt.setString(9, "maskuuid");					
				}				
			
				pstmt.setString(3, account.getUuid());					
				pstmt.setString(5, smsSource.getUuid());				
				pstmt.setInt(6, count);
				pstmt.setString(8, account.getUuid());
				pstmt.setString(10, smsSource.getUuid());
				
				pstmt.executeUpdate();
							
			} catch(SQLException e) {
				logger.error("SQLException while deducting the balance of '" + account +
						"' of amount " + count + " for '" + smsSource + "'.");
				logger.error(ExceptionUtils.getStackTrace(e));
				success = false;				
			} 
						
		} else { // end 'if(hasBalance(account, smsSource, count))'
			success = false;
		}
		
		return success;
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
