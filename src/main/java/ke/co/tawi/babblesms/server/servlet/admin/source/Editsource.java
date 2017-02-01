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
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.maskcode.MaskDAO;
import ke.co.tawi.babblesms.server.persistence.maskcode.ShortcodeDAO;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Servlet used to edit source.
 * <p>
 * Copyright (c) Tawi Ltd., July 7,2014
 *
 * @author <a href="mailto:josephk@tawi.mobi">Joseph Kimani</a>
 */
@WebServlet(name = "editsource",
        urlPatterns = {"/editsource", "/deletesource"})
public class Editsource extends HttpServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = 6839655965091886176L;
	private CacheManager cacheManager;
    private ShortcodeDAO shortcodeDAO;
    private MaskDAO maskDAO;
    Account account = new Account();

    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        maskDAO = MaskDAO.getInstance();
        shortcodeDAO = ShortcodeDAO.getInstance();
        cacheManager = CacheManager.getInstance();
    }

    // <editor-fold defaultstate="collapsed" desc="HttpServlet methods. Click on the + sign on the left to edit the code.">
    /**
     * Handles the HTTP <code>GET</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

    }

    /**
     * Handles the HTTP <code>POST</code> method.
     *
     * @param request servlet request
     * @param response servlet response
     * @throws ServletException if a servlet-specific error occurs
     * @throws IOException if an I/O error occurs
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userPath = request.getServletPath();
        HttpSession session = request.getSession(false);
        // if edit source is called
        if (userPath.equals("/editsource")) {
        	
            Mask mask = new Mask();
            Shortcode shortcode = new Shortcode();
            
            String accountuuid = request.getParameter("accuuid");
            String source = request.getParameter("source");
            String networkuuid = request.getParameter("networkuuid");
            String sourceuuid = request.getParameter("sourceuuid");
            account.setUuid(accountuuid); 
           
            if(maskDAO.getMasks(account)!=null){ 
                mask.setAccountuuid(accountuuid);
                mask.setMaskname(source);
                mask.setNetworkuuid(networkuuid);
                mask.setUuid(sourceuuid);  
                if (maskDAO.updateMask(mask, sourceuuid)) {
                	updateMaskCache(mask); 
                    session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Mask updated successfully.");
                }else{
                	session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Mask update failed.");
                }
            }
              
            else if(shortcodeDAO.getShortcodes(account)!=null){	 
                shortcode.setAccountuuid(accountuuid);
                shortcode.setCodenumber(source);
                shortcode.setNetworkuuid(networkuuid);
                shortcode.setUuid(sourceuuid);
               
                if (shortcodeDAO.update(shortcode, sourceuuid)) {
                    session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, "Shortcode updated successfully.");
                    updateShortcodeCache(shortcode); 
                } 
                else{
                	session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, "Shortcode update failed.");	
                }
                
            }
            response.sendRedirect("admin/source.jsp");
            }//end if editsource
            
       
     
    
    
    }//end dopodt

    private void updateShortcodeCache(Shortcode shortcode) {
   cacheManager.getCache(CacheVariables.CACHE_SHORTCODE_BY_UUID).put(new Element(shortcode.getUuid(),shortcode));
		
	}

	private void updateMaskCache(Mask mask) {
  cacheManager.getCache(CacheVariables.CACHE_MASK_BY_UUID).put(new Element(mask.getUuid(),mask)); 
	}

	/**
     * Returns a short description of the servlet.
     *
     * @return a String containing servlet description
     */
    @Override
    public String getServletInfo() {
        return "Short description";
    }// </editor-fold>

}
