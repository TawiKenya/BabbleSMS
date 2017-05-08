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
import ke.co.tawi.babblesms.server.beans.creditmgmt.MaskPurchase;
import ke.co.tawi.babblesms.server.beans.creditmgmt.SMSBalance;
import ke.co.tawi.babblesms.server.beans.creditmgmt.ShortcodeBalance;
import ke.co.tawi.babblesms.server.beans.creditmgmt.MaskBalance;
import ke.co.tawi.babblesms.server.beans.creditmgmt.ShortcodePurchase;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.beans.maskcode.SMSSource;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * Persistence implementation for shortcode and mask balances.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SmsBalanceDAO extends GenericDAO implements BabbleSmsBalanceDAO {

	private static SmsBalanceDAO balanceDAO;
	
	private BeanProcessor beanProcessor = new BeanProcessor();
	
	private Logger logger = LogManager.getLogger(this.getClass());
	
	
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
					
					PreparedStatement pstmt = conn.prepareStatement("UPDATE ShortcodeBalance " +
							"SET count = (SELECT count FROM ShortcodeBalance WHERE accountUuid=? "
							+ "AND Shortcodeuuid=?) - ? " +				
							"WHERE uuid = (SELECT uuid FROM ShortcodeBalance WHERE accountUuid=? "
							+ "AND Shortcodeuuid=?);");	
					
					PreparedStatement pstmt2 = conn.prepareStatement("UPDATE MaskBalance " +
							"SET count = (SELECT count FROM MaskBalance WHERE accountUuid=? "
							+ "AND maskuuid=?) - ? " +				
							"WHERE uuid = (SELECT uuid FROM MaskBalance WHERE accountUuid=? "
							+ "AND maskuuid=?);");						
					) {
				
				
				if(smsSource instanceof Shortcode) {
					pstmt.setString(1, account.getUuid());					
					pstmt.setString(2, smsSource.getUuid());				
					pstmt.setInt(3, count);
					pstmt.setString(4, account.getUuid());
					pstmt.setString(5, smsSource.getUuid());
					
					pstmt.executeUpdate();
					
				} else {	// This is a mask
					pstmt2.setString(1, account.getUuid());					
					pstmt2.setString(2, smsSource.getUuid());				
					pstmt2.setInt(3, count);
					pstmt2.setString(4, account.getUuid());
					pstmt2.setString(5, smsSource.getUuid());
					
					pstmt2.executeUpdate();				
				}				
										
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

	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsBalanceDAO#addBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)
	 */
	@Override
	public boolean addBalance(Account account, SMSSource smsSource, int count) {
		boolean success = true;
		ShortcodePurchase sp = new ShortcodePurchase();
		MaskPurchase mp = new MaskPurchase();
			
		if(hasBalance(account, smsSource, 0)) {
			try(				
					Connection conn = dbCredentials.getConnection();
					
					PreparedStatement pstmt = conn.prepareStatement("UPDATE ShortcodeBalance " +
							"SET count = (SELECT count FROM ShortcodeBalance WHERE accountUuid=? "
							+ "AND Shortcodeuuid=?) + ? " +				
							"WHERE uuid = (SELECT uuid FROM ShortcodeBalance WHERE accountUuid=? "
							+ "AND Shortcodeuuid=?);");	
					
					PreparedStatement pstmt2 = conn.prepareStatement("UPDATE MaskBalance " +
							"SET count = (SELECT count FROM MaskBalance WHERE accountUuid=? "
							+ "AND maskuuid=?) + ? " +				
							"WHERE uuid = (SELECT uuid FROM MaskBalance WHERE accountUuid=? "
							+ "AND maskuuid=?);");						
					) {
				
				if(smsSource instanceof Shortcode) {
					pstmt.setString(1, account.getUuid());					
					pstmt.setString(2, smsSource.getUuid());				
					pstmt.setInt(3, count);
					pstmt.setString(4, account.getUuid());
					pstmt.setString(5, smsSource.getUuid());
					
					pstmt.executeUpdate();
					
				} else {	// This is a mask
					pstmt2.setString(1, account.getUuid());					
					pstmt2.setString(2, smsSource.getUuid());				
					pstmt2.setInt(3, count);
					pstmt2.setString(4, account.getUuid());
					pstmt2.setString(5, smsSource.getUuid());
					
					pstmt2.executeUpdate();				
				}				
										
			} catch(SQLException e) {
				logger.error("SQLException while adding by updating the balance of '" + account +
						"' of amount " + count + " for '" + smsSource + "'.");
				logger.error(ExceptionUtils.getStackTrace(e));
				success = false;				
			} 
			
			
		} else { // This is the first time that we are adding balance to this short code or mask
			try(	
					Connection conn = dbCredentials.getConnection();
					
					PreparedStatement pstmt = conn.prepareStatement("INSERT INTO ShortcodeBalance(uuid,"
							+ "accountuuid,shortcodeuuid,count) VALUES(?,?,?,?);");	
					
					PreparedStatement pstmt2 = conn.prepareStatement("INSERT INTO MaskBalance " +
							"(uuid,accountuuid,maskuuid,count) VALUES (?,?,?,?);");						
					) {
				
				if(smsSource instanceof Shortcode) {
					pstmt.setString(1, sp.getUuid());
					pstmt.setString(2, account.getUuid());					
					pstmt.setString(3, smsSource.getUuid());				
					pstmt.setInt(4, count);
					pstmt.executeUpdate();
					
				} else {	// This is a mask
					pstmt2.setString(1, mp.getUuid());
					pstmt2.setString(2, account.getUuid());					
					pstmt2.setString(3, smsSource.getUuid());				
					pstmt2.setInt(4, count);
					pstmt2.executeUpdate();				
				}				
										
			} catch(SQLException e) {
				logger.error("SQLException while adding by creating the balance of '" + account +
						"' of amount " + count + " for '" + smsSource + "'.");
				logger.error(ExceptionUtils.getStackTrace(e));
				success = false;				
			} 
		}
						
		
		return success;
	}

	
	/*
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsBalanceDAO#updateBalance(ke.co.tawi.babblesms.server.beans.account.Account, ke.co.tawi.babblesms.server.beans.maskcode.SMSSource, int)
	 */
	public boolean updateBalance(Account account, SMSBalance smsbal,int amount) {
		boolean success = true;
				
			try(				
					Connection conn = dbCredentials.getConnection();
					
					PreparedStatement pstmt = conn.prepareStatement("UPDATE ShortcodeBalance " +
							"SET count =? WHERE accountUuid =? AND Shortcodeuuid =?;");	
					
					PreparedStatement pstmt2 = conn.prepareStatement("UPDATE MaskBalance " +
							"SET count =? WHERE accountUuid =? AND Maskuuid =?;");						
					) {
				
				if(smsbal instanceof ShortcodeBalance) {								
					pstmt.setInt(1, amount);
					pstmt.setString(2, account.getUuid());
					pstmt.setString(3, smsbal.getUuid());					
					pstmt.executeUpdate();
					
				} else {	// MaskBalance		
					pstmt2.setInt(1, amount);
					pstmt2.setString(2, account.getUuid());
					pstmt2.setString(3, smsbal.getUuid());
					
					pstmt2.executeUpdate();				
				}				
										
			} catch(SQLException e) {
				logger.error("SQLException while updating the balance of '" + account +
						"' of amount " + amount + " for '" + smsbal + "'.");
				logger.error(ExceptionUtils.getStackTrace(e));
				System.out.println(ExceptionUtils.getStackTrace(e));
				success = false;				
			} 
	
		
		return success;
	
	}
	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsBalanceDAO#getBalances(ke.co.tawi.babblesms.server.beans.account.Account)
	 */
	@Override
	public List<SMSBalance> getBalances(Account account) {
		List<SMSBalance> list = new LinkedList<>();
				
        try (
     		   Connection conn = dbCredentials.getConnection();
     	       PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ShortcodeBalance WHERE "
     	       		+ "accountuuid = ?;");    
        		PreparedStatement pstmt2 = conn.prepareStatement("SELECT * FROM MaskBalance WHERE "
         	       		+ "accountuuid = ?;");  
     	   ) {
         	   pstmt.setString(1, account.getUuid());
         	   pstmt2.setString(1, account.getUuid()); 
         	  
     	       ResultSet rset = pstmt.executeQuery();
     	       
     	       
     	       list.addAll(beanProcessor.toBeanList(rset, ShortcodeBalance.class));
     	       
     	       rset = pstmt2.executeQuery();
    	       
    	       list.addAll(beanProcessor.toBeanList(rset, MaskBalance.class));
     	       
        } catch (SQLException e) {
            logger.error("SQLException when getting sms balances of " + account);
            logger.error(ExceptionUtils.getStackTrace(e));
        }
                  
        return list;
	}
	

	/**
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsBalanceDAO#getAllBalances()
	 */
	@Override
	public List<SMSBalance> getAllBalances() {
		List<SMSBalance> list = new LinkedList<>();
		
        try (
     		   Connection conn = dbCredentials.getConnection();
     	        PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM ShortcodeBalance");    
        		PreparedStatement pstmt2 = conn.prepareStatement("SELECT * FROM MaskBalance");  
        		ResultSet rset = pstmt.executeQuery();
        		ResultSet rset2 = pstmt2.executeQuery();
     	   ) {
         	        	       
     	       list.addAll(beanProcessor.toBeanList(rset, ShortcodeBalance.class));
    	       list.addAll(beanProcessor.toBeanList(rset2, MaskBalance.class));
     	       
        } catch (SQLException e) {
            logger.error("SQLException when getting all sms balances.");
            logger.error(ExceptionUtils.getStackTrace(e));
        }
                  
        return list;
	}



}
