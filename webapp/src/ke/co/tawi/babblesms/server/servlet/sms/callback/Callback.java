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
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;
import ke.co.tawi.babblesms.server.beans.messagetemplate.MsgStatus;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.maskcode.ShortcodeDAO;
import ke.co.tawi.babblesms.server.persistence.logs.IncomingLogDAO;
import ke.co.tawi.babblesms.server.persistence.network.NetworkDAO;
import ke.co.tawi.babblesms.server.persistence.logs.OutgoingLogDAO;

import java.io.IOException;
import java.io.PrintWriter;
import java.util.Map;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.joda.time.LocalDateTime;
import org.joda.time.format.DateTimeFormatter;
import org.joda.time.format.ISODateTimeFormat;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * A Servlet that receives notifications on Incoming SMS and DLR status change
 * from the Tawi SMS Gateway and processes them.
 * <p>
 *
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class Callback extends HttpServlet {
    
    private CacheManager cacheManager;

    private OutgoingLogDAO outgoingLogDAO;
    
    private Map<String,String> dlrstatusMap; // A mapping between the SMS Gateway DLR status
     										 // codes and the Message Status codes of BabbleSMS
    
    private Logger logger = Logger.getLogger(this.getClass());
    
    /**
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
        
        outgoingLogDAO = OutgoingLogDAO.getInstance();
        
        dlrstatusMap = new HashMap<>();
        dlrstatusMap.put("ACCEPTED_FOR_DELIVERY", MsgStatus.SENT);
        dlrstatusMap.put("DELIVERY_SUCCESS", MsgStatus.RECEIVED);
        dlrstatusMap.put("DELIVERY_FAILURE", MsgStatus.FAILURE);
        dlrstatusMap.put("SMSC_REJECT", MsgStatus.FAILURE);
        dlrstatusMap.put("SMSC_SUBMIT", MsgStatus.IN_TRANSIT);        
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
        		
        		OutgoingLog log = outgoingLogDAO.getOutgoingLog(messageId);
        		log.setLogTime(datetime.toDate());
        		log.setMessagestatusuuid(dlrstatusMap.get(status));
        		
        		outgoingLogDAO.putOutgoingLog(log);
        		
        		break;
        		
        		
        	case "incomingSms":
        		logger.info("Have received incoming sms");
        		
        		String destination = request.getParameter("destination");
        		logger.info("destination is " + destination);
        		
        		String source = request.getParameter("source");
        		logger.info("source is " + source);
        		
        		String message = request.getParameter("message");
        		logger.info("message is " + message);
        		
        		messageId = request.getParameter("messageId");
        		logger.info("messageId is " + message);
        		
        		String network = request.getParameter("network");
        		logger.info("network is " + network);
        		
        		datetime = timeFormatter.parseLocalDateTime(request.getParameter("datetime"));
        		logger.info("datetime is " + datetime);
        		
        		break;
        }
        
        
        //get parameter values
        
            /*//if callbacktype is incomingSMS proceed
            if (request.getParameter("callbackType").equals("incomingSms")) {
                
                if (request.getParameter("destination") != null && request.getParameter("source") != null && request.getParameter("message") != null && request.getParameter("messageId") != null && request.getParameter("network") != null && request.getParameter("datetime") != null) {
                    IncomingLogDAO incomingLogDAO = IncomingLogDAO.getInstance();
                    IncomingLog incomingLog = new IncomingLog();
                    
                    //get network uuid
                    NetworkDAO networkDAO=NetworkDAO.getInstance();
                    Network network=new Network();
                    String [] networkname=request.getParameter("network").split(" ");
                    
                    network=networkDAO.getNetworkByName(networkname[0]);
                    //get source uuid
                    ShortcodeDAO shortcodeDAO = ShortcodeDAO.getInstance();
                    Shortcode shortcode = new Shortcode();                    
                    //shortcode = shortcodeDAO.getShortcodeBycodeNumber(request.getParameter("destination"),network.getUuid());
                   
                    incomingLog.setMessage(request.getParameter("message"));
                    incomingLog.setOrigin(request.getParameter("source"));
                    incomingLog.setDestination(shortcode.getUuid());
                    
                    
                    //add incoming SMS.
                    incomingLogDAO.putIncomingLog(incomingLog);
                    
                   }
            }*/
        
    }
    

}
