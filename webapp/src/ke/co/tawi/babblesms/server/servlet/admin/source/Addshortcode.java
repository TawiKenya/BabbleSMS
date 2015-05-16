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
package ke.co.tawi.babblesms.server.servlet.admin.source;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.beans.account.Credit;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.items.credit.CreditDAO;
import ke.co.tawi.babblesms.server.persistence.maskcode.ShortcodeDAO;

import java.io.IOException;
import java.util.HashMap;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;

/**
 * Servlet used to add shortcode.
 * <p>
 * Copyright (c) Tawi Ltd., July 7,2014
 *
 * @author <a href="mailto:josephk@tawi.mobi">Joseph Kimani</a>
 */

public class Addshortcode extends HttpServlet {
    final String ERROR_NO_CODENUMBER = "Please provide a Code Number.";
    final String ERROR_NO_NETWORKNAME = "No Network selected.";
    final String ERROR_NO_ACCOUNTNAME = "No Account selected.";
    final String ERROR_NO_CODENUMBER_EXISTS = "The Code Number provided already exists in the system.";
    
   
    private String codenumber,accountuuid,networkuuid;
    
    // This is used to store parameter names and values from the form.
    private HashMap<String, String> paramHash;
    
    
    private ShortcodeDAO shortcodeDAO;    
    private CreditDAO creditDAO;    
    private CacheManager cacheManager;
    private HttpSession session;
    

    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        
        shortcodeDAO = ShortcodeDAO.getInstance();
        creditDAO=CreditDAO.getInstance();
        
        cacheManager = CacheManager.getInstance();
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        session = request.getSession(true);

        setClassParameters(request);
       
        initParamHash();
        session.setAttribute(SessionConstants.ADMIN_ADD_SMSSHORTCODE_PARAMETERS, paramHash);

        // No Network Name provided
        if (StringUtils.isBlank(codenumber)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSSHORTCODE_ERROR_KEY, ERROR_NO_NETWORKNAME);

            // No network provided	
        } else if (StringUtils.isBlank(networkuuid)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSSHORTCODE_ERROR_KEY, ERROR_NO_NETWORKNAME);

            // No account provided	
        } else if (StringUtils.isBlank(accountuuid)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSSHORTCODE_ERROR_KEY, ERROR_NO_ACCOUNTNAME);

         
            // The code number already exists in the system	
        } else if (existsshortcode(codenumber,networkuuid)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSSHORTCODE_ERROR_KEY, ERROR_NO_CODENUMBER_EXISTS);

            
        } else {
            // If we get this far then all parameter checks are ok.		
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSSHORTCODE_SUCCESS_KEY, "s");

            // Reduce our session data
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSSHORTCODE_PARAMETERS, null);
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSSHORTCODE_ERROR_KEY, null);

            addShortcode();          
            session.setAttribute(SessionConstants.ADMIN_ADD_SUCCESS, "Shortcode created successfully.");
        }

        response.sendRedirect("admin/addshortcode.jsp");
    }

    /**
     *
     */
    private void addShortcode() {
        Shortcode s = new Shortcode();
                
        s.setCodenumber(codenumber);
        s.setNetworkuuid(networkuuid);
        s.setAccountuuid(accountuuid);
        
        Credit c=new Credit();
        
        int amount=0;        
        c.setAccountuuid(accountuuid);
        c.setSource(codenumber);
        c.setCredit(amount);
        
                
       

         if(shortcodeDAO.put(s)){
             creditDAO.putCredit(c);
           }
        

        //s = shortcodeDAO.getShortcodeBycodeNumber(codenumber, networkuuid);	// Ensures the shortcode is populated with the correct ID
        //updateShortcodeCache(s);
    }

    
    /**
     *
     * @param acc
     */
    private void updateShortcodeCache(Shortcode s) {
    	cacheManager.getCache(CacheVariables.CACHE_SHORTCODE_BY_UUID).put(new Element(s.getUuid(), s));
    }

    
    /**
     * Set the class variables that represent form parameters.
     *
     * @param request
     */
    private void setClassParameters(HttpServletRequest request) {
        codenumber = StringUtils.trimToEmpty(request.getParameter("shortcode"));
        networkuuid = StringUtils.trimToEmpty(request.getParameter("network"));
        accountuuid = StringUtils.trimToEmpty(request.getParameter("account"));
        
    }

    
    /**
     * Place all the received parameters in our class HashMap.
     *
     */
    private void initParamHash() {
        paramHash = new HashMap<>();

        paramHash.put("codenumber", codenumber);
        paramHash.put("networkuuid", networkuuid);
        paramHash.put("accountuuid", accountuuid);
        
    }
    
    
    /**
     *
     * @param name
     * @return whether shortcode name exists in the system
     */
    private boolean existsshortcode(final String codenum,final String networknum) {
        boolean exists = false;

        //if (shortcodeDAO.getShortcodeBycodeNumber(codenum,networknum) != null) {
        //    exists = true;
        //}

        return exists;
    }
    
    
    /**
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
