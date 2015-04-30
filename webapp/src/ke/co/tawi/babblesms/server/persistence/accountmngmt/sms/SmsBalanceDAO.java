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
import ke.co.tawi.babblesms.server.persistence.GenericDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.util.UUID;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.apache.log4j.Logger;


/**
 * Abstraction for persistence of bulk SMS balance in an account.
 * <p>
 *
 * 
 * @author <a href="mailto:wambua@tawi.mobi">Godfrey Wambua</a>
 * @version %I%, %G%
 * 
 */

public class SmsBalanceDAO extends GenericDAO implements BabbleSmsBalanceDAO {
	
	private static SmsBalanceDAO smsBalanceDAO;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	private BeanProcessor beanProcessor = new BeanProcessor();

	/**
	 * 
	 * @return SmsBalanceDAO
	 */
	public static SmsBalanceDAO getInstance() {
		if(smsBalanceDAO == null) {
			smsBalanceDAO = new SmsBalanceDAO();
		}
		
		return smsBalanceDAO;
	}
	
	
	/**
	 * 
	 */
	protected SmsBalanceDAO() {
		super();
				
		logger = Logger.getLogger(this.getClass());
	}

	/**
	 * 
	 * @param dbName
	 * @param dbHost
	 * @param dbUsername
	 * @param dbPassword
	 * @param dbPort
	 */
	public SmsBalanceDAO(String dbName, String dbHost, String dbUsername, String dbPassword, int dbPort) {
		super(dbName, dbHost, dbUsername, dbPassword, dbPort);
	}

	
	/**
	 * @see SMSGWSmsBalanceDAO#hasBalance(mobi.tawi.smsgw.beans.accountmgmt.Account, SMSCode, int)
	 */
	@Override
	public boolean hasBalance(Account account, Shortcode smsCode, int count) {
		boolean hasBalance = false;
		int scount = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
				
		try {
			conn = dbCredentials.getConnection();	
			pstmt = conn.prepareStatement("SELECT count FROM shortcodebalance WHERE accountuuid=? " +
					"AND shortcodeuuid=?;");
			pstmt.setString(1, account.getUuid());	
			pstmt.setString(2, smsCode.getUuid());
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				scount = rset.getInt("count");				
			}
			
			if(scount >= count) {
				hasBalance = true;
			}
			
		} catch(SQLException e) {
			logger.error("SQLException exception while checking whether '" + account +
					"' has balance of " + count + " for '" + smsCode + "'.");
			logger.error(ExceptionUtils.getStackTrace(e));
			
		} finally {
		   if(rset != null ) {
		        try{rset.close();} catch(SQLException e) {} 
		    }              
		           
		    if(pstmt != null ) {
		        try{pstmt.close();} catch(SQLException e) {} 
		    }
		                
		    if(conn != null ) {
		        try{conn.close( );} catch(SQLException e) {} 
		    }    
		}
		
		return hasBalance;
	}

	
	/**
	 * @see SMSGWSmsBalanceDAO#hasBalance(mobi.tawi.smsgw.beans.accountmgmt.Account, mobi.tawi.smsgw.beans.sms.SMSMask, int)
	 */
	@Override
	public boolean hasBalance(Account account, Mask smsMask, int count) {
		boolean hasBalance = false;
		int sCount = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
				
		try {
			conn = dbCredentials.getConnection();	
			pstmt = conn.prepareStatement("SELECT count FROM maskbalance WHERE accountUuid=? " +
					"AND maskuuid=?;");
			pstmt.setString(1, account.getUuid());	
			pstmt.setString(2, smsMask.getUuid());
			rset = pstmt.executeQuery();
			
			if(rset.next()) {
				sCount = rset.getInt("count");				
			}
			
			if(sCount >= count) {
				hasBalance = true;
			}
			
		} catch(SQLException e) {
			logger.error("SQLException exception while checking whether '" + account +
					"' has balance of " + count + " for '" + smsMask + "'.");
			logger.error(ExceptionUtils.getStackTrace(e));
			
		} finally {
		   if(rset != null ) {
		        try{rset.close();} catch(SQLException e) {} 
		    }              
		           
		    if(pstmt != null ) {
		        try{pstmt.close();} catch(SQLException e) {} 
		    }
		                
		    if(conn != null ) {
		        try{conn.close( );} catch(SQLException e) {} 
		    }    
		}
		
		return hasBalance;
	}
	/*	
	* @see SMSGWSmsBalanceDAO#deductBalance(mobi.tawi.smsgw.beans.accountmgmt.Account, SMSCode, int)
	 */
	@Override
	public boolean deductBalance(Account account, Shortcode smsCode, int amount) {
		boolean success = true;
			
		Connection conn = null;
		PreparedStatement pstmt = null;
				
		if(hasBalance(account, smsCode, amount)) {
		
			try {
				conn = dbCredentials.getConnection();	
				pstmt = conn.prepareStatement("UPDATE shortcodebalance " +
					"SET count = (SELECT count FROM shortcodebalance WHERE accountUuid=? AND shortcodeuuid=?) " +
					"- ? " +				
					"WHERE uuid = (SELECT uuid FROM shortcodebalance WHERE accountUuid=? "
					+ "AND shortcodeuuid=?);");
			
				pstmt.setString(1, account.getUuid());	
				pstmt.setString(2, smsCode.getUuid());
				pstmt.setInt(3, amount);
				pstmt.setString(4, account.getUuid());	
				pstmt.setString(5, smsCode.getUuid());
				pstmt.executeUpdate();
							
			} catch(SQLException e) {
				logger.error("SQLException exception while deducting the balance of '" + account +
						"' of amount " + amount + " for '" + smsCode + "'.");
				logger.error(ExceptionUtils.getStackTrace(e));
				success = false;
				
			} finally {		           
			    if(pstmt != null ) {
			        try{pstmt.close();} catch(SQLException e) {} 
			    }
			                
			    if(conn != null ) {
			        try{conn.close( );} catch(SQLException e) {} 
			    }    
			}
						
		} else { // end 'if(hasBalance(account, smsCode, amount))'
			success = false;
		}
		
		return success;
	}
	
	/**
	 * @see SMSGWSmsBalanceDAO#deductBalance(mobi.tawi.smsgw.beans.accountmgmt.Account, mobi.tawi.smsgw.beans.sms.SMSMask, int)
	 */
	@Override
	public boolean deductBalance(Account account, Mask smsMask, int amount) {
		boolean success = true;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
					
		if(hasBalance(account, smsMask, amount)) {			
		
			try {
				conn = dbCredentials.getConnection();	
				pstmt = conn.prepareStatement("UPDATE maskbalance " +
					"SET count = (SELECT count FROM maskbalance WHERE accountUuid=? AND maskuuid=?) " +
					"- ? " +				
					"WHERE uuid = (SELECT uuid FROM maskbalance WHERE accountUuid=? "
					+ "AND maskuuid=?);");
			
				pstmt.setString(1, account.getUuid());	
				pstmt.setString(2, smsMask.getUuid());
				pstmt.setInt(3, amount);
				pstmt.setString(4, account.getUuid());	
				pstmt.setString(5, smsMask.getUuid());
				pstmt.executeUpdate();
							
			} catch(SQLException e) {
				logger.error("SQLException exception while deducting the balance of '" + account +
						"' of amount " + amount + " for '" + smsMask + "'.");
				logger.error(ExceptionUtils.getStackTrace(e));
				success = false;
				
			} finally {		           
			    if(pstmt != null ) {
			        try{pstmt.close();} catch(SQLException e) {} 
			    }
			                
			    if(conn != null ) {
			        try{conn.close( );} catch(SQLException e) {} 
			    }    
			}
			
		} else {
			success = false;
		}
		
		return success;
	}
		
	/**
	 * @see mobi.tawi.smsgw.persistence.accountmgmt.sms.SMSGWSmsBalanceDAO#addBalance(mobi.tawi.smsgw.beans.accountmgmt.Account, mobi.tawi.smsgw.beans.sms.SMSCode, int)
	 */
	@Override
	public boolean addBalance(Account account, Shortcode smsCode, int amount) {
		boolean success = true;
		
		Connection conn = null;
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rset = null;
		
		try {
			conn = dbCredentials.getConnection();
						
			// Check whether this account has an existing balance or not on this network
			pstmt = conn.prepareStatement("SELECT uuid FROM shortcodebalance WHERE accountUuid=? "
				+ "AND shortcodeuuid=?;");
			pstmt.setString(1, account.getUuid());	
			pstmt.setString(2, smsCode.getUuid());
			rset = pstmt.executeQuery();
		
			// The account has a pre-existing balance
			if(rset.next()) {			
				pstmt2 = conn.prepareStatement("UPDATE shortcodebalance " +
						"SET count = (SELECT count FROM shortcodebalance WHERE accountUuid=? AND shortcodeuuid=?) " +
						"+ ? " +				
						"WHERE uuid = (SELECT uuid FROM shortcodebalance WHERE accountUuid=? "
						+ "AND shortcodeuuid=?);");
				
				pstmt2.setString(1, account.getUuid());	
				pstmt2.setString(2, smsCode.getUuid());
				pstmt2.setInt(3, amount);
				pstmt2.setString(4, account.getUuid());	
				pstmt2.setString(5, smsCode.getUuid());
				pstmt2.executeUpdate();
			
			// The account does not have a pre-existing balance
			} else  {
				pstmt2 = conn.prepareStatement("INSERT INTO shortcodebalance(uuid, accountUuid, shortcodeuuid, "
	                    + "count) VALUES(?,?,?,?);");

				pstmt2.setString(1, UUID.randomUUID().toString());
				pstmt2.setString(2, account.getUuid());
				pstmt2.setString(3, smsCode.getUuid());
				pstmt2.setInt(4, amount);
				pstmt2.execute();
			}
						
		} catch(SQLException e) {
			logger.error("SQLException exception while adding the balance of '" + account +
					"' of amount " + amount + " for '" + smsCode + "'.");
			logger.error(ExceptionUtils.getStackTrace(e));
			success = false;
			
		} finally {		
			if(rset != null ) {
		        try{rset.close();} catch(SQLException e) {} 
		    } 
			
		    if(pstmt != null ) {
		        try{pstmt.close();} catch(SQLException e) {} 
		    }
		    
		    if(pstmt2 != null ) {
		        try{pstmt2.close();} catch(SQLException e) {} 
		    }
		                
		    if(conn != null ) {
		        try{conn.close( );} catch(SQLException e) {} 
		    }    
		}
		
		return success;
	}

	/**
	 * @see mobi.tawi.smsgw.persistence.accountmgmt.sms.SMSGWSmsBalanceDAO#addBalance(mobi.tawi.smsgw.beans.accountmgmt.Account, mobi.tawi.smsgw.beans.sms.SMSMask, int)
	 */
	@Override
	public boolean addBalance(Account account, Mask smsMask, int amount) {
		boolean success = true;
		
		Connection conn = null;
		PreparedStatement pstmt = null, pstmt2 = null;
		ResultSet rset = null;
				
		try {
			conn = dbCredentials.getConnection();
						
			// Check whether this account has an existing balance or not on this network
			pstmt = conn.prepareStatement("SELECT uuid FROM maskbalance WHERE accountUuid=? "
				+ "AND maskuuid=?;");
			pstmt.setString(1, account.getUuid());	
			pstmt.setString(2, smsMask.getUuid());
			rset = pstmt.executeQuery();
		
			// The account has a pre-existing balance
			if(rset.next()) {			
				pstmt2 = conn.prepareStatement("UPDATE maskbalance " +
						"SET smsCount = (SELECT count FROM maskbalance WHERE accountUuid=? AND maskuuid=?) " +
						"+ ? " +				
						"WHERE uuid = (SELECT uuid FROM maskbalance WHERE accountUuid=? "
						+ "AND maskuuid=?);");
				
				pstmt2.setString(1, account.getUuid());	
				pstmt2.setString(2, smsMask.getUuid());
				pstmt2.setInt(3, amount);
				pstmt2.setString(4, account.getUuid());	
				pstmt2.setString(5, smsMask.getUuid());
				pstmt2.executeUpdate();
			
			// The account does not have a pre-existing balance
			} else  {
				pstmt2 = conn.prepareStatement("INSERT INTO maskbalance(uuid, accountUuid, maskuuid, "
	                    + "count) VALUES(?,?,?,?);");

				pstmt2.setString(1, UUID.randomUUID().toString());
				pstmt2.setString(2, account.getUuid());
				pstmt2.setString(3, smsMask.getUuid());
				pstmt2.setInt(4, amount);
				pstmt2.execute();
			}
			
		} catch(SQLException e) {
			logger.error("SQLException exception while adding the balance of '" + account +
					"' of amount " + amount + " for '" + smsMask + "'.");
			logger.error(ExceptionUtils.getStackTrace(e));
			success = false;
			
		} finally {		           
			if(rset != null ) {
		        try{rset.close();} catch(SQLException e) {} 
		    } 
			
		    if(pstmt != null ) {
		        try{pstmt.close();} catch(SQLException e) {} 
		    }
		    
		    if(pstmt2 != null ) {
		        try{pstmt2.close();} catch(SQLException e) {} 
		    }
		                
		    if(conn != null ) {
		        try{conn.close( );} catch(SQLException e) {} 
		    }    
		}
		
		return success;
	}
	
	public int getBalance(Account account, Mask smsMask) {
		int balance = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
				
		try {
			conn = dbCredentials.getConnection();
						
			// Check whether this account has an existing balance or not on this network
			pstmt = conn.prepareStatement("SELECT  count FROM maskbalance WHERE accountUuid=? "
				+ "AND maskuuid=?;");
			pstmt.setString(1, account.getUuid());	
			pstmt.setString(2, smsMask.getUuid());
			rset = pstmt.executeQuery();
		
			// The account has a pre-existing balance
			if(rset.next()) {			
			balance = rset.getInt("count");	
			} 
		} catch(SQLException e) {
			logger.error("SQLException exception while adding the balance of '" + account +
					"'  for '" + smsMask + "'.");
			logger.error(ExceptionUtils.getStackTrace(e));
			
			
		} finally {		           
			if(rset != null ) {
		        try{rset.close();} catch(SQLException e) {} 
		    } 
			
		    if(pstmt != null ) {
		        try{pstmt.close();} catch(SQLException e) {} 
		    }
		    
		    
		                
		    if(conn != null ) {
		        try{conn.close( );} catch(SQLException e) {} 
		    }    
		}
		
		return balance;
	}
	
	public int getBalance(Account account, Shortcode code) {
		int balance = 0;
		
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rset = null;
				
		try {
			conn = dbCredentials.getConnection();
						
			// Check whether this account has an existing balance or not on this network
			pstmt = conn.prepareStatement("SELECT  count FROM maskbalance WHERE accountUuid=? "
				+ "AND maskuuid=?;");
			pstmt.setString(1, account.getUuid());	
			pstmt.setString(2, code.getUuid());
			rset = pstmt.executeQuery();
		
			// The account has a pre-existing balance
			if(rset.next()) {			
			balance = rset.getInt("count");	
			} 
		} catch(SQLException e) {
			logger.error("SQLException exception while adding the balance of '" + account +
					"'  for '" + code + "'.");
			logger.error(ExceptionUtils.getStackTrace(e));
			
			
		} finally {		           
			if(rset != null ) {
		        try{rset.close();} catch(SQLException e) {} 
		    } 
			
		    if(pstmt != null ) {
		        try{pstmt.close();} catch(SQLException e) {} 
		    }
		    
		    
		                
		    if(conn != null ) {
		        try{conn.close( );} catch(SQLException e) {} 
		    }    
		}
		
		return balance;
	}
	
	
	
	
	
	
	
}