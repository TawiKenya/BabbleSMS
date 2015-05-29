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

package ke.co.tawi.babblesms.server.servlet.accountmngmt;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO;
import ke.co.tawi.babblesms.server.persistence.utils.networkcountUtil;


/**
 * servlet implementation .
 * <p>
 *  
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>
 */

@SuppressWarnings("serial")
public class Netcount  extends HttpServlet {
	
	
     static String Hashnet;
	
     @Override
 	protected void doPost(HttpServletRequest request, HttpServletResponse response)
 			throws ServletException, IOException {
    		
    
	    String accountuuid = request.getParameter("uuid");	    
	    AccountDAO AccDAO = AccountDAO.getInstance();
	    Account account = AccDAO.getAccount(accountuuid);
    	
	       	
		networkcountUtil countnet = new networkcountUtil();		
		Hashnet= countnet.networkcount(account);		
		
			//System.out.println(Hashnet);
		try(	
	   PrintWriter display = response.getWriter();){
			display.println(Hashnet);
			}
		catch(Exception e){System.out.println("Error caught on NetCount servlet::: "+e);}
	    	 
  }
  /*public String Networklist(){
	  return Hashnet;
  }*/

}