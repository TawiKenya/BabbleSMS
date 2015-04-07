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
package ke.co.tawi.babblesms.server.servlet.contacts;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;

/**
 * Receives form values from addgroup.jsp section and adds a new
 * {@link Group} to the database.
 *
 * <p>
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
@WebServlet(name = "AddGroup",
        urlPatterns = {
            "/account/addgroup", "/account/editGroup", "/account/deletegroup"})
public class AddGroup extends HttpServlet {
    
    final String ERROR_NO_NAME = "You have to input group name.";
    final String ERROR_NO_DESC = "You have to input group description.";
    final String ERROR_NAME_EXISTS = "The group already exists in your account. Please enter another one.";
    final String ADD_SUCCESS = "Group created successfully.";

    private CacheManager mgr;
    private GroupDAO gpDAO;
    
    
    /**
	 * @param config
	 * @throws ServletException
	 * @see javax.servlet.Servlet#init(ServletConfig)
	 */
    @Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		
		mgr = CacheManager.getInstance();
	    gpDAO = GroupDAO.getInstance();		
	}
        
        
    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String userPath = request.getServletPath();
        HttpSession session = request.getSession(false);

        if (userPath.equals("/account/addgroup")) {

            String gname = request.getParameter("gname");
            String gdesc = request.getParameter("gdesc");
            String account = request.getParameter("accountuuid");
            String status = "396F2C7F-961C-5C12-3ABF-867E7FD029E6";

            // No First Name provided
            if (StringUtils.isBlank(gname)) {
                session.setAttribute(SessionConstants.ADD_ERROR, ERROR_NO_NAME);

            } else if (existsName(gname)) {
                session.setAttribute(SessionConstants.ADD_ERROR, ERROR_NAME_EXISTS);
                
            } else if (StringUtils.isBlank(gdesc)) {
                session.setAttribute(SessionConstants.ADD_ERROR, ERROR_NO_DESC);

            } 
            
            else {
      
                Group cg = new Group();
                String uuid=cg.getUuid();
                cg.setUuid(uuid);
                cg.setName(gname);
                cg.setDescription(gdesc);
                cg.setStatusuuid(status);
                cg.setAccountsuuid(account);

                if(gpDAO.putGroup(cg)){
                	session.setAttribute(SessionConstants.ADD_SUCCESS, ADD_SUCCESS);
                }
                else{
                	session.setAttribute(SessionConstants.ADD_ERROR, "creation failed");
                }
                //logger.info(cg + " added sucessfully");
               // cg = gpDAO.getGroupByName(gname);
                
               // cg=gpDAO.getGroupByName(gname);

                mgr.getCache(CacheVariables.CACHE_GROUP_BY_UUID).put(new Element(cg.getUuid(), cg));
            }
            userPath = "/account/addgroup";

        }
        if (userPath.equals("/account/editGroup")) {
            String gname = request.getParameter("grpname");
            String gdesc = request.getParameter("desc");
            String grpuuid = request.getParameter("grpuuid");
            
           // gpDAO.updateGroup(grpuuid,gname,gdesc);
            
            userPath = "groups";

        }
        if (userPath.equals("/account/deletegroup")) {
            String grpuuid = request.getParameter("groupuuid");
            
           // gpDAO.deleteGroup(grpuuid);
            
            userPath = "groups";

        }

        // use RequestDispatcher to forward request internally
        String url = userPath + ".jsp";

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (ServletException | IOException ex) {
        }

    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
    

    /**
     * Checks if the group exists in the group database
     *
     * @param gname
     * @return
     */
    private boolean existsName(final String gname) {
        boolean exists = false;

     /*   if (gpDAO.getGroupByName(gname) != null) {
            exists = true;
        }*/

        return exists;
    }
}
