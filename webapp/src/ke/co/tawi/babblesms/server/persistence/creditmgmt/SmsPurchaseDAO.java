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
import ke.co.tawi.babblesms.server.beans.creditmgmt.SMSPurchase;
import ke.co.tawi.babblesms.server.beans.creditmgmt.ShortcodePurchase;
import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;
import ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

/**
 * Persistence implementation for shortcode and mask purchace history.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SmsPurchaseDAO extends GenericDAO implements BabbleSmsPurchaseDAO {
        
	
	private static SmsBalanceDAO smsbalanceDAO;
	
	private static SmsPurchaseDAO  smspurchaseDAO;
	private Logger logger = Logger.getLogger(this.getClass());
	private BeanProcessor beanProcessor = new BeanProcessor();
	
	
	
	/**
	 * @return the singleton instance of {@link SmsBalanceDAO}
	 */
	public static SmsPurchaseDAO getInstance() {
		if( smspurchaseDAO == null){
			smspurchaseDAO = new SmsPurchaseDAO();
		}
		return smspurchaseDAO;
	}

	
	/**
	 * 
	 */
	protected SmsPurchaseDAO(){
		super();
		smsbalanceDAO = SmsBalanceDAO.getInstance();
	}
		
	
	/**
	 * @param dbName
	 * @param dbHost
	 * @param dbUsername
	 * @param dbPassword
	 * @param dbPort
	 */
	public SmsPurchaseDAO(String dbName, String dbHost, String dbUsername,
			String dbPassword, int dbPort) {
		super(dbName, dbHost, dbUsername, dbPassword, dbPort); 
		
		smsbalanceDAO = new SmsBalanceDAO(dbName, dbHost, dbUsername, dbPassword, dbPort);
	}

	
	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsPurchaseDAO#getPurchase(java.lang.String)
	 */
	@Override
	public SMSPurchase getPurchase(String uuid) {
		SMSPurchase purchase = null;
				try(
					Connection con = dbCredentials.getConnection();	
					   PreparedStatement ps = con.prepareStatement("SELECT * FROM shortcodepurchase WHERE uuid =?");
				       PreparedStatement ps2 = con.prepareStatement("SELECT * FROM maskpurchase WHERE uuid =?");	
						){
					
			
					             ps.setString(1, uuid);
					             ps2.setString(1, uuid );
					             try(
					            		 ResultSet  rs1 = ps.executeQuery();			
					            		 ResultSet  rs2 = ps2.executeQuery();
					            		 ){
					            	 if(rs1.next()){
							        	   uuid = rs1.getString("uuid");
							        if(uuid != null)	 {
							        	purchase = beanProcessor.toBean(rs1, ShortcodePurchase.class);	
							        }				   
							           }else if(rs2.next()){
							        	   uuid = rs2.getString("uuid");
							        	   if(uuid != null)	 {
							        	purchase = beanProcessor.toBean(rs2, MaskPurchase.class);	   
							           }
							        	   }//end if
						
					            	 
					             } //end second try
					             
				}//end first try
					            
				catch(SQLException e) {
					logger.error("SQLException while trying to get Purchase by " + uuid);
					logger.error(ExceptionUtils.getStackTrace(e));
				}
				
				
				return purchase;
	
		
	}

	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsPurchaseDAO#put(ke.co.tawi.babblesms.server.beans.creditmgmt.SMSPurchase)
	 */
	@Override
	public boolean put(SMSPurchase purchase) {
		
		Account account = new Account();
		account.setUuid(purchase.getAccountUuid());
		
		int count = purchase.getCount();
		
		boolean success = true;
		
		try(
			Connection con = dbCredentials.getConnection();				
			
			PreparedStatement pst = con.prepareStatement("INSERT INTO shortcodepurchase(uuid, accountuuid,"
				+ "shortcodeuuid, count, purchasedate) VALUES(?,?,?,?,?);");						

			PreparedStatement pst2 = con.prepareStatement("INSERT INTO maskpurchase(uuid, accountuuid,"
				+ "maskuuid, count, purchasedate) VALUES(?,?,?,?,?);");					    
			){
			
			if(purchase instanceof ShortcodePurchase) {
			
				pst.setString(1, purchase.getUuid());
				pst.setString(2, purchase.getAccountUuid());
				pst.setString(3,purchase.getSourceUuid());
				pst.setInt(4, purchase.getCount());
				pst.setTimestamp(5, new Timestamp(purchase.getPurchaseDate().getTime()));	
				pst.executeUpdate();
				
				Shortcode shortcode = new Shortcode();
				shortcode.setUuid(purchase.getSourceUuid());
				smsbalanceDAO.addBalance(account, shortcode, count);
					
			
			} else {  //  for mask
				
				pst2.setString(1, purchase.getUuid());
				pst2.setString(2, purchase.getAccountUuid());
				pst2.setString(3, purchase.getSourceUuid());
				pst2.setInt(4, purchase.getCount());
				pst2.setTimestamp(5, new Timestamp(purchase.getPurchaseDate().getTime()));
				pst2.executeUpdate();
			
				Mask mask = new Mask();
				mask.setUuid(purchase.getSourceUuid());
				smsbalanceDAO.addBalance(account, mask, count);				
				
			}//end if
		
			
		} catch(SQLException e) {
			logger.error("SQLException while trying to put " + purchase);
			logger.error(ExceptionUtils.getStackTrace(e));
			success= false;
		}
	
		return success;		
	}//end put
	
	

	/**
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsPurchaseDAO#getPurchases(ke.co.tawi.babblesms.server.beans.account.Account)
	 */
	@Override
	public List<SMSPurchase> getPurchases(Account account) {
		List<SMSPurchase> list = new LinkedList<>();
		
		try(
			Connection con = dbCredentials.getConnection();
				PreparedStatement stm = con.prepareStatement("SELECT * FROM shortcodepurchase WHERE accountuuid = ?");
				PreparedStatement stm2 = con.prepareStatement("SELECT * FROM maskpurchase WHERE accountuuid = ?");				
				){
			
			 stm.setString(1, account.getUuid());
			 stm2.setString(1, account.getUuid());
			 
			 ResultSet rs = stm.executeQuery();
			 list.addAll(beanProcessor.toBeanList(rs, ShortcodePurchase.class));
	 	      
			 rs = stm2.executeQuery();
			 list.addAll(beanProcessor.toBeanList(rs, MaskPurchase.class));			 
			 
        } catch (SQLException e) {
            logger.error("SQLException when getting Purchase of " + account);
            logger.error(ExceptionUtils.getStackTrace(e));
        }		
		
		return list;
	}//end getPurchase

	
	/**
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsPurchaseDAO#getAllPurchases()
	 */
	@Override
	public List<SMSPurchase> getAllPurchases() {
		List<SMSPurchase> list = new LinkedList<>();

        try(   
        		Connection con = dbCredentials.getConnection();
        		PreparedStatement  ps = con.prepareStatement("SELECT * FROM shortcodepurchase;");
        		PreparedStatement  ps2 = con.prepareStatement("SELECT * FROM maskpurchase;");
        		
    		) {
        	
        	ResultSet rs = ps.executeQuery();
        	list.addAll(beanProcessor.toBeanList(rs, ShortcodePurchase.class));
        	 
    		rs = ps2.executeQuery();
 	        list.addAll(beanProcessor.toBeanList(rs, MaskPurchase.class));

        } catch(SQLException e){
        	logger.error("SQL Exception when getting all SMSPurchases");
            logger.error(ExceptionUtils.getStackTrace(e));
        }
       
		return list;
	}//end get allpurchases

	
}//class end
