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
import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.items.credit.CreditDAO;
import ke.co.tawi.babblesms.server.persistence.maskcode.MaskDAO;

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
 * Servlet used to add mask.
 * <p>
 * Copyright (c) Tawi Ltd., July 7,2014
 *
 * @author <a href="mailto:josephk@tawi.mobi">Joseph Kimani</a>
 */
public class Addmask extends HttpServlet {

    final String ERROR_NO_MASKNAME = "Please provide a Mask Name.";
    final String ERROR_NO_NETWORKNAME = "No Network selected.";
    final String ERROR_NO_ACCOUNTNAME = "No Account selected.";
        
   
    private String maskname,accountuuid,networkuuid;
    
    // This is used to store parameter names and values from the form.
    private HashMap<String, String> paramHash;
    
    
    private MaskDAO maskDAO;    
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

        
        maskDAO = MaskDAO.getInstance();
        creditDAO = CreditDAO.getInstance();
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
        session.setAttribute(SessionConstants.ADMIN_ADD_SMSMASK_PARAMETERS, paramHash);

        // No Network Name provided
        if (StringUtils.isBlank(maskname)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSMASK_ERROR_KEY, ERROR_NO_MASKNAME);

            // No network provided	
        } else if (StringUtils.isBlank(networkuuid)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSMASK_ERROR_KEY, ERROR_NO_NETWORKNAME);

            // No account provided	
        } else if (StringUtils.isBlank(accountuuid)) {
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSMASK_ERROR_KEY, ERROR_NO_ACCOUNTNAME);

            
        } else {
            // If we get this far then all parameter checks are ok.		
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSMASK_SUCCESS_KEY, "s");

            // Reduce our session data
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSMASK_PARAMETERS, null);
            session.setAttribute(SessionConstants.ADMIN_ADD_SMSMASK_ERROR_KEY, null);

            addMask();
            //set session value
            session.setAttribute(SessionConstants.ADMIN_ADD_SUCCESS, "Mask created successfully.");
        }

        response.sendRedirect("admin/addmask.jsp");
    }

    /**
     *
     */
    private void addMask() {
        Mask m = new Mask();
                
        m.setMaskname(maskname);
        m.setNetworkuuid(networkuuid);
        m.setAccountuuid(accountuuid);
        
        int amount=0;
        Credit c=new Credit();
        c.setAccountuuid(accountuuid);
        c.setSource(maskname);
        c.setCredit(amount);


        if(maskDAO.put(m)){
           creditDAO.putCredit(c);
         }

        //m = maskDAO.getMaskByName(maskname);	// Ensures the mask is populated with the correct ID
        updateMaskCache(m);
    }

    
    /**
     *
     * @param acc
     */
    private void updateMaskCache(Mask ms) {
    	cacheManager.getCache(CacheVariables.CACHE_MASK_BY_UUID).put(new Element(ms.getUuid(), ms));
    }

    
    /**
     * Set the class variables that represent form parameters.
     *
     * @param request
     */
    private void setClassParameters(HttpServletRequest request) {
        maskname = StringUtils.trimToEmpty(request.getParameter("maskname"));
        networkuuid = StringUtils.trimToEmpty(request.getParameter("network"));
        accountuuid = StringUtils.trimToEmpty(request.getParameter("account"));
        
    }

    
    /**
     * Place all the received parameters in our class HashMap.
     *
     */
    private void initParamHash() {
        paramHash = new HashMap<>();

        paramHash.put("maskname", maskname);
        paramHash.put("networkuuid", networkuuid);
        paramHash.put("accountuuid", accountuuid);
        
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
