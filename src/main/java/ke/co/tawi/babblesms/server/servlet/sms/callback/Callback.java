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
package ke.co.tawi.babblesms.server.servlet.sms.callback;

import ke.co.tawi.babblesms.server.beans.log.IncomingLog;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;
import ke.co.tawi.babblesms.server.beans.messagetemplate.MsgStatus;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.logs.IncomingLogDAO;
import ke.co.tawi.babblesms.server.persistence.logs.OutgoingLogDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;

import java.io.IOException;
import java.util.Map;
import java.util.HashMap;
import java.util.List;
import java.util.LinkedList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;

import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;


/**
 * A Servlet that receives notifications on Incoming SMS and DLR status change
 * from the Tawi SMS Gateway and processes them.
 * <p>
 *
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Callback extends HttpServlet {
    
	
	private OutgoingLogDAO outgoingLogDAO;
    private IncomingLogDAO incomingLogDAO;
    private PhoneDAO phoneDAO;
    
    private Map<String,String> dlrstatusMap; // A mapping between the SMS Gateway DLR status
     										 // codes and the Message Status codes of BabbleSMS
    
    private Map<String,String> networkMap; 	// A mapping between the Network parameters that can
	 										// be received from the SMS Gateway and the corresponding
    										// UUID in BabbleSMS
    
    private List<Shortcode>  shortcodeList;
        
    /**
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        outgoingLogDAO = OutgoingLogDAO.getInstance();
        incomingLogDAO = IncomingLogDAO.getInstance();
        phoneDAO = PhoneDAO.getInstance();
        
        dlrstatusMap = new HashMap<>();
        dlrstatusMap.put("ACCEPTED_FOR_DELIVERY", MsgStatus.SENT);
        dlrstatusMap.put("DELIVERY_SUCCESS", MsgStatus.RECEIVED);
        dlrstatusMap.put("DELIVERY_FAILURE", MsgStatus.FAILURE);
        dlrstatusMap.put("SMSC_REJECT", MsgStatus.FAILURE);
        dlrstatusMap.put("SMSC_SUBMIT", MsgStatus.IN_TRANSIT); 
        
        networkMap = new HashMap<>();
        networkMap.put("safaricom_ke", Network.SAFARICOM_KE);
        networkMap.put("airtel_ke", Network.AIRTEL_KE);
        
        shortcodeList = new LinkedList<>();
        
        CacheManager mgr = CacheManager.getInstance();
        Cache shortcodeCache = mgr.getCache(CacheVariables.CACHE_SHORTCODE_BY_UUID);
        
        List keys = shortcodeCache.getKeys();
        
        Element element; 
        Shortcode shortcode;
        for (Object key : keys) {
            element = shortcodeCache.get(key);
            shortcode = (Shortcode) element.getObjectValue();
            shortcodeList.add(shortcode);
        }
    }

    

    /**     
     * @param request 
     * @param response 
     * @throws ServletException, IOException     
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
    	DateTimeFormatter timeFormatter = ISODateTimeFormat.dateTimeNoMillis();
    	
        String callbackType = request.getParameter("callbackType");
        String messageId;
        LocalDateTime datetime;
        
        switch(callbackType) {
        
        	case "status":	// A notification of an SMS Status change
        		messageId = request.getParameter("messageId");
        		
        		datetime = timeFormatter.parseLocalDateTime(request.getParameter("datetime"));
        		String status = request.getParameter("status");
        		
        		OutgoingLog log = outgoingLogDAO.get(messageId);
        		log.setLogTime(datetime.toDate());
        		log.setMessagestatusuuid(dlrstatusMap.get(status));
        		
        		outgoingLogDAO.put(log);
        		
        		break;
        		
        		
        	case "incomingSms":        		
        		String network = request.getParameter("network").toLowerCase();        		
        		datetime = timeFormatter.parseLocalDateTime(request.getParameter("datetime"));
        		
        		IncomingLog incomingLog = new IncomingLog();
        		incomingLog.setDestination(request.getParameter("destination"));
        		incomingLog.setUuid(request.getParameter("messageId"));
        		incomingLog.setMessage(request.getParameter("message"));
        		incomingLog.setLogTime(datetime.toDate());
        		incomingLog.setNetworkUuid(networkMap.get(network));
        		
        		
        		// The source saved in the address book may begin with "07"
        		// but the one received for Kenya would begin with "254"
        		// We have to reconcile the two
        		String source = request.getParameter("source");
        		
        		String phoneNum = "";
        		if(StringUtils.startsWith(source, "254")) {
					phoneNum = "07" + StringUtils.substring(source, 4);
				}
        		        		
        		if(phoneDAO.getPhones(phoneNum).size() > 0) {
        			incomingLog.setOrigin(phoneNum);
        			
        		} else {
        			incomingLog.setOrigin(source);
        		}
        		
        		
        		// Determine the account that it is destined for
        		// This assumes that the same shortcode number cannot
        		// be owned by multiple accounts
        		for(Shortcode shortcode : shortcodeList) {
        			if(shortcode.getCodenumber().equals(incomingLog.getDestination())) {
        				incomingLog.setRecipientUuid(shortcode.getAccountuuid());
        				break;
        			}
        		}
        		      		
        		
        		incomingLogDAO.putIncomingLog(incomingLog);
        		break;
        }
        
    }
    
    
    private static final long serialVersionUID = -1235395974580302882L;

}
