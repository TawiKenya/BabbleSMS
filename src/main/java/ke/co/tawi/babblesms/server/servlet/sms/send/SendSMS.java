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
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.beans.smsgateway.TawiGateway;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.maskcode.SMSSource;
import ke.co.tawi.babblesms.server.beans.messagetemplate.MsgStatus;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.log.OutgoingGrouplog;

import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.persistence.smsgw.tawi.GatewayDAO;
import ke.co.tawi.babblesms.server.persistence.maskcode.ShortcodeDAO;
import ke.co.tawi.babblesms.server.persistence.maskcode.MaskDAO;
import ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO;
import ke.co.tawi.babblesms.server.persistence.logs.OutgoingGroupLogDAO;

import ke.co.tawi.babblesms.server.sendsms.tawismsgw.PostSMS;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.utils.ListPartitioner;
import ke.co.tawi.babblesms.server.utils.StringUtil;
import ke.co.tawi.babblesms.server.utils.comparator.PhonesByNetworkPredicate;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import java.io.IOException;
import java.util.List;
import java.util.LinkedList;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.list.SetUniqueList;

/**
 * Receives a form request to send SMS to Contact(s) or Group(s) 
 * <p>
 * 
 * @author <a href="mailto:dennism@tawi.mobi">Dennis Mutegi</a>
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SendSMS extends HttpServlet {
							
	private Cache accountsCache;
		
	private PhoneDAO phoneDAO;
	private GatewayDAO gatewayDAO;
	private ContactGroupDAO ctgrpDAO;
	private ShortcodeDAO shortcodeDAO;
	private MaskDAO maskDAO;
	private SmsBalanceDAO smsBalanceDAO;	
	private OutgoingGroupLogDAO groupLogDAO;	
	
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
		gatewayDAO = GatewayDAO.getInstance(); 
		ctgrpDAO= ContactGroupDAO.getInstance();
		shortcodeDAO = ShortcodeDAO.getInstance();
		maskDAO = MaskDAO.getInstance();
		smsBalanceDAO = SmsBalanceDAO.getInstance();
		groupLogDAO = OutgoingGroupLogDAO.getInstance();
		
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
	@SuppressWarnings("unchecked")
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
		
		// Respond as soon as possible to the client request
		HttpSession session = request.getSession(true);
		session.setAttribute(SessionConstants.SENT_SUCCESS, "success");
		response.sendRedirect("sendsms.jsp");
		
		// Get the relevant account		
		Account account = new Account();
		
		String username = request.getParameter("username");
		
		Element element;
	    if ((element = accountsCache.get(username)) != null) {
	        account = (Account) element.getObjectValue();
	    }
		
	    TawiGateway smsGateway = gatewayDAO.get(account);
	    
	    // Retrieve the web parameters
		String[] groupselected = request.getParameterValues("groupselected");
		String[] phones = request.getParameterValues("phones");
		String source = request.getParameter("source");
		String message = request.getParameter("message");
       
		
		
		// Deal with the case where one or more Groups has been selected
		// Get phones of all the Contacts in the Group(s)
		SetUniqueList phoneList = SetUniqueList.decorate(new LinkedList<Phone>()); // Does not allow duplicates 
				
		Group group;
		List<Contact> contactList;		
		List<OutgoingGrouplog> outgoingGroupList = new LinkedList<>();
		
		if(groupselected != null) {
			OutgoingGrouplog groupLog;
			
			for(String groupUuid : groupselected) {
				group = new Group();
				group.setUuid(groupUuid);
				contactList = ctgrpDAO.getContacts(group);
			 	
				for(Contact contact : contactList) {
					phoneList.addAll(phoneDAO.getPhones(contact) );
				}				
				
				// Save an outgoing log for the group 
				groupLog = new OutgoingGrouplog();
				groupLog.setMessagestatusUuid(MsgStatus.SENT);
				groupLog.setSender(account.getUuid());
				groupLog.setDestination(group.getUuid());
				groupLog.setMessage(message);
				outgoingGroupList.add(groupLog);
				
			}// end 'for(String groupUuid : groupselected)'
		}// end 'if(groupselected != null)'
		
		
		// This is the case where individual Contacts may have been selected		
		if(phones == null) {
			phones = new String[0];
		}
		phones = StringUtil.removeDuplicates(phones);
				
		for(String phone : phones) {
			phoneList.add(phoneDAO.getPhone(phone));
		}
		
		
		// Determine whether a shortcode or mask is the source
		SMSSource smsSource;
		Shortcode shortcode = shortcodeDAO.get(source);
		Mask mask = null;
		if(shortcode == null) {
			mask = maskDAO.get(source);
			smsSource = mask;
			
		} else {
			smsSource = shortcode;
		}
		
		// Set the network in the groups (if any) and save the log
		for(OutgoingGrouplog log : outgoingGroupList) {
			log.setNetworkUuid(smsSource.getNetworkuuid());
			log.setOrigin(smsSource.getUuid());
			groupLogDAO.put(log);
		}
		
		
		// Filter the phones to the Network of the source (mask or short code)
		List<Phone> validPhoneList = new LinkedList<>();
		validPhoneList.addAll(CollectionUtils.select(phoneList, 
				new PhonesByNetworkPredicate(smsSource.getNetworkuuid())));
		
		
		// Break down the phone list to go out to manageable sizes, each sublist
		// being sent to the SMS Gateway in one URL POST
		List<List<Phone>> phonePartition = ListPartitioner.partition(validPhoneList, 10);
		
		
		
		// Send the lists one by one
		PostSMS postThread;
		
		for(List<Phone> list : phonePartition) {	
			postThread = new PostSMS(smsGateway, list, smsSource, message, account, true);
					
			postThread.start(); 	
		}
		
		
		// Deduct credit
		smsBalanceDAO.deductBalance(account, smsSource, validPhoneList.size());	
	}
			
}
