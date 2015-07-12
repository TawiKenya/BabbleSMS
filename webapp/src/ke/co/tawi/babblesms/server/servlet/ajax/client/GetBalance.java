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
package ke.co.tawi.babblesms.server.servlet.ajax.client;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.creditmgmt.MaskBalance;
import ke.co.tawi.babblesms.server.beans.creditmgmt.SMSBalance;
import ke.co.tawi.babblesms.server.beans.creditmgmt.ShortcodeBalance;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


/**
 * Returns a JSON list of smsSources for a given account.
 * <p>
 * Sample output is as follows:<br/>
 * 
 *{  "Maskuuid/Shortcodeuuid":"current balance"(contents),
 *   "3d04925d-44b9-4b93-a541-18884e5d4b83": 7000,  "45309a3a-c3d2-4bed-be2b-dbe23aedeaf5": 9000, 
 *   "4dddaece-994d-4e9a-8ecb-53abd5ab9971": 8000,  "652030be-4de8-4d81-bdf3-69ce7cc7ac91": 8000, 
 *   "e2c3a40b-8d86-4325-b08b-b7456eefbe07": 9000,  "68edc772-0f25-4484-a231-0b667c69173d": 8500
 * } 
 *
 * <p>
 * 
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a> 
 */
public class GetBalance extends HttpServlet {
	
	 
	private SmsBalanceDAO smsBalanceDAO;
	private Cache accountsCache;
		
	
	
	/**
	 * @see javax.servlet.GenericServlet#init()
	 */
	@Override
	public void init() throws ServletException{	
		super.init();
		CacheManager mgr = CacheManager.getInstance();
		  accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID);
		
		smsBalanceDAO = SmsBalanceDAO.getInstance();		
   }
	   
	   
	  	
    /**
     * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
     */
    @Override
	protected void doPost(HttpServletRequest request,HttpServletResponse response) 
	         throws ServletException,IOException{
		String accountUuid = request.getParameter("accountuuid");
						
		Element element;
		Account account = null;
		
		if ((element = accountsCache.get(accountUuid)) != null) {
			account = (Account) element.getObjectValue();
		}

		OutputStream out = response.getOutputStream();

		response.setContentType("application/json;charset=UTF-8");

		// Instantiate the JSon
		// The '=' sign is encoded to \u003d. Hence you need to use
		// disableHtmlEscaping().
		Gson gson = new GsonBuilder().disableHtmlEscaping()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.setPrettyPrinting().serializeNulls().create();
		
		out.write(gson.toJson(getSMS(account)).getBytes());
		out.flush();
		out.close();		
    }
	   
    
	/**
	  * @param account
	  * @return Map with smsSource and its balance
      */	   
    public Map<String,Integer> getSMS(Account account){
	   List<SMSBalance> balanceList = new LinkedList<>();
	   Map<String,Integer> balance = new HashMap<>();
	   
	   balanceList = smsBalanceDAO.getBalances(account);
	   
	   ShortcodeBalance scb;
	   MaskBalance mb;
	   for(SMSBalance bal : balanceList){
		   
		   //if list object is of type ShortcodeBalance
		   if(bal instanceof ShortcodeBalance){
			 scb = (ShortcodeBalance) bal;
			 balance.put(scb.getShortcodeUuid(), bal.getCount());
			   		   
		   //if list object is of type MaskBalance
		   } else if(bal instanceof MaskBalance){
			   mb = (MaskBalance)bal;
			   balance.put(mb.getMaskUuid(), bal.getCount());
		   }			 
	   }
	   
	   return balance;		   
   }
	   
	   
	   
   /**
    * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
    */
   @Override
   protected void doGet(HttpServletRequest request,HttpServletResponse response) 
	         throws ServletException,IOException{
		doPost(request,response);
    }	   

   
	private static final long serialVersionUID = -7568761645749920089L;
}
