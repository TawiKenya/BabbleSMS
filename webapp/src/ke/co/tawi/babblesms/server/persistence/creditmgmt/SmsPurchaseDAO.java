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

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.LinkedList;
import java.util.List;

import javax.xml.transform.Source;

import org.apache.commons.dbutils.BeanProcessor;
import org.apache.commons.lang.exception.ExceptionUtils;
import org.apache.log4j.Logger;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.creditmgmt.MaskPurchase;
import ke.co.tawi.babblesms.server.beans.creditmgmt.SMSPurchase;
import ke.co.tawi.babblesms.server.beans.creditmgmt.ShortcodePurchase;
import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.maskcode.SMSSource;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.persistence.GenericDAO;


/**
 * Persistence implementation for shortcode and mask purchace history.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SmsPurchaseDAO extends GenericDAO implements BabbleSmsPurchaseDAO {

	
	private static SmsPurchaseDAO  smspurchaseDAO;
	private Logger logger = Logger.getLogger(this.getClass());
	private BeanProcessor beanProcessor = new BeanProcessor();
	
	
	
	
	public static SmsPurchaseDAO getInstance() {
		if( smspurchaseDAO == null){
			smspurchaseDAO = new SmsPurchaseDAO();
		}
		return smspurchaseDAO;
	}
	
	
	
	protected SmsPurchaseDAO(){
		super();
	}
	/**
	 * 
	 */
	public SmsPurchaseDAO(String dbName, String dbHost, String dbUsername,
			String dbPassword, int dbPort) {
		super(dbName, dbHost, dbUsername, dbPassword, dbPort); 
		
	}

	/* (non-Javadoc)
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsPurchaseDAO#put(ke.co.tawi.babblesms.server.beans.creditmgmt.SMSPurchase)
	 */
	@Override
	public boolean put(SMSPurchase purchase) {
		boolean success = true;
		ShortcodePurchase sp = new ShortcodePurchase();
		MaskPurchase mp = new MaskPurchase();
		//Mask mask = new Mask();
		//Shortcode scode = new Shortcode();
		

		try(
			Connection con = dbCredentials.getConnection();	
			PreparedStatement pst = con.prepareStatement("INSERT INTO shortcodepurchase(uuid,accountuuid,"
					+ "shortcodeuuid,count,purchasedate) "
					+ "VALUES(?,?,?,?,?);");
					
			PreparedStatement pst2 = con.prepareStatement("INSERT INTO maskpurchase(uuid,accountuuid,"
					+ "maskuuid,count,purchasedate) "
					+ "VALUES(?,?,?,?,?);");			
					
				){

			
			if( purchase instanceof ShortcodePurchase) {
				
				pst.setString(1, sp.getUuid());
				pst.setString(2, purchase.getAccountUuid());
				pst.setString(3, purchase.getSourceUuid());
				pst.setInt(4, purchase.getCount());
				pst.setTimestamp(5, new Timestamp(purchase.getPurchaseDate().getTime()));	
				
				pst.executeUpdate();
				
				
				
			}else{  
				pst2.setString(1, mp.getUuid());
				pst2.setString(2, purchase.getAccountUuid());
				pst2.setString(3, purchase.getSourceUuid());
				pst2.setInt(4, purchase.getCount());
				pst2.setTimestamp(5, new Timestamp(purchase.getPurchaseDate().getTime()));
				
				pst2.executeUpdate();
				
			}
			
			success = true;
		}catch(SQLException e){
			logger.error("SQLException while trying to put "+purchase);
			logger.error(ExceptionUtils.getStackTrace(e));
			success= false;
		}
	
		return success;
	}

	/* (non-Javadoc)
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
	 	      
			 ResultSet rs2 = stm2.executeQuery();
			 list.addAll(beanProcessor.toBeanList(rs2, MaskPurchase.class));
			 
			 
        } catch (SQLException e) {
            logger.error("SQLException when getting Purchase of " + account);
            logger.error(ExceptionUtils.getStackTrace(e));
        }
		
		
		return list;
	}

	/* (non-Javadoc)
	 * @see ke.co.tawi.babblesms.server.persistence.creditmgmt.BabbleSmsPurchaseDAO#getAllPurchases()
	 */
	@Override
	public List<SMSPurchase> getAllPurchases() {
		List<SMSPurchase> list =  new LinkedList<>();

        try(   
        		Connection con = dbCredentials.getConnection();
        		PreparedStatement  ps = con.prepareStatement("SELECT * FROM shortcodepurchase;");
        		PreparedStatement  ps2 = con.prepareStatement("SELECT * FROM maskpurchase;");
        		
    		) {
        	
        	ResultSet rs = ps.executeQuery();
        	 list.addAll(beanProcessor.toBeanList(rs, ShortcodePurchase.class));
        	 
    		ResultSet rs2 = ps.executeQuery();
 	        list.addAll(beanProcessor.toBeanList(rs2, MaskPurchase.class));

        } catch(SQLException e){
        	logger.error("SQL Exception when getting all SMSPurchases");
            logger.error(ExceptionUtils.getStackTrace(e));
        }
       
		return list;
	}

}
