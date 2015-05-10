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
package ke.co.tawi.babblesms.server.servlet.sms.send;

import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.beans.maskcode.SMSSource;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.persistence.maskcode.ShortcodeDAO;
import ke.co.tawi.babblesms.server.persistence.maskcode.MaskDAO;
import ke.co.tawi.babblesms.server.sendsms.tawismsgw.PostSMS;
import ke.co.tawi.babblesms.server.servlet.util.PropertiesConfig;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.utils.StringUtil;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.log4j.Logger;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.HashSet;
import java.util.Map;
import java.util.LinkedList;

/**
 * Receives a form request to send SMS to Contact(s) or Group(s) 
 * <p>
 * 
 * @author <a href="mailto:dennism@tawi.mobi">Dennis Mutegi</a>
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SendSMS extends HttpServlet {
	final String SMSGW_URL_HTTP = PropertiesConfig.getConfigValue("SMSGW_API_URL"); 
	final String SMSGW_USERNAME = PropertiesConfig.getConfigValue("SMSGW_USERNAME");
	final String SMSGW_PASSWORD = PropertiesConfig.getConfigValue("SMSGW_PASSWORD");
			
			
	private Cache accountsCache;
		
	private PhoneDAO phoneDAO;
	private MaskDAO maskDAO;
	private ShortcodeDAO shortcodeDAO;
	
	private Logger logger = Logger.getLogger(this.getClass());
	
	
	/**
	 * @param config
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 * 
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		phoneDAO = PhoneDAO.getInstance();
		maskDAO = MaskDAO.getInstance();
		shortcodeDAO = ShortcodeDAO.getInstance();
		
		CacheManager mgr = CacheManager.getInstance();
        accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
	}
	
	
	/**
	 * Method to handle form processing
	 * 
	 * @param request
	 * @param response
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * 
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request , HttpServletResponse response) throws IOException {
		HttpSession session = request.getSession(true);
		
		Account account = new Account();
		
		String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
		Element element;
	    if ((element = accountsCache.get(username)) != null) {
	        account = (Account) element.getObjectValue();
	    }
			    
		String [] groupselected = request.getParameterValues("groupselected");
		String [] phones = request.getParameterValues("phones");
		String source = request.getParameter("source");
		String message = request.getParameter("message");
       
		
		// Determine the source to use (shortcode or mask)
		SMSSource smsSource = null;
		
		if((smsSource = maskDAO.get(source)) == null) {
			smsSource = shortcodeDAO.get(source);
		}
		
        
		//Group group;
		
		for(String group : groupselected) {
			System.out.println("Group is: '" + group + "'");
		}
		
		if(phones == null) {
			phones = new String[0];
		}
		phones = StringUtil.removeDuplicates(phones);
		
		
		List<Phone> phoneList = new LinkedList<>();
		for(String phone : phones) {
			phoneList.add(phoneDAO.getPhone(phone));
		}
		
		
		
		Map<String,String> params;
		PostSMS postThread;
		
		for(Phone phone : phoneList) {
			params = new HashMap<>();
		
			params.put("username", SMSGW_USERNAME);		
			params.put("password", SMSGW_PASSWORD);
			params.put("source", smsSource.getSource());
			params.put("destination", phone.getPhonenumber());
			params.put("message", message);
			
			switch (smsSource.getNetworkuuid()) {
				case Network.SAFARICOM_KE:
					params.put("network", "safaricom_ke");
					break;
					
				case Network.AIRTEL_KE:
					params.put("network", "airtel_ke");
					break;
			}
			
										
			//System.out.println("Data to post: " + StringUtil.mapToString(params));
			
			postThread = new PostSMS(SMSGW_URL_HTTP, params, false);	
			postThread.start(); 				
		}
		
		session.setAttribute(SessionConstants.SENT_SUCCESS, "success");
		response.sendRedirect("sendsms.jsp");	
	}
			
}
