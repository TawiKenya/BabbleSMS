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
package ke.co.tawi.babblesms.server.servlet.admin.smsgateway;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants;
import ke.co.tawi.babblesms.server.beans.smsgateway.TawiGateway;
import ke.co.tawi.babblesms.server.persistence.smsgw.tawi.GatewayDAO;

import org.apache.commons.lang3.StringUtils;

/**
 * @author <a href="mailto:mmwenda@tawi.mobi">Peter Mwenda</a>
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class AddNotificationUrl extends HttpServlet {

	private GatewayDAO gatewayDAO;
	/**
	 * 
	 */
	private static final long serialVersionUID = 5319626033395362723L;

	@Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        gatewayDAO = GatewayDAO.getInstance();
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
   	
   	HttpSession session = request.getSession(false); 

	String user = StringUtils.trimToEmpty(request.getParameter("user"));
	String url = StringUtils.trimToEmpty(request.getParameter("url"));
	String password = StringUtils.trimToEmpty(request.getParameter("password"));
	String password2 = StringUtils.trimToEmpty(request.getParameter("password2"));
	// This is used to store parameter names and values from the form.
	Map<String, String> paramHash = new HashMap<>();    
	paramHash.put("url", url);
	
     String uuid = user.split("\\|")[0];
     String uname = user.split("\\|")[1];
	/*System.out.println(uuid);
	System.out.println(uname);*/
	
	
        // No Url provided      
    if (StringUtils.isBlank(url)) {
        session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, "please provide a Url");

        // usernane exist   
    }    // No Url provided      
    
    else if (StringUtils.isBlank(password)) {
        session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, "please provide password");

        // usernane exist   
    }
    else if (StringUtils.isBlank(password2)) {
        session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, "please confirm password");

        // usernane exist   
    }else if(userNameExist(uname)){
    	session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, "username already exist in the System");
    }
    else if (!StringUtils.equals(password, password2)) {
        session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, "Password Mismatch");      
    } else {
    	// If we get this far then all parameter checks are ok.         
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_SUCCESS_KEY, "s");
            
            // Reduce our session data
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_PARAMETERS, null);
            session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, null);
    	TawiGateway g = new TawiGateway();
    	 g.setAccountUuid(uuid);
    	 g.setUrl(url);
    	 g.setUsername(uname);
    	 g.setPasswd(password);
    	 gatewayDAO.put(g);
    	 //session.setAttribute(SessionConstants.ADMIN_ADD_SUCCESS, "Account created successfully.");
    }
	
    response.sendRedirect("addaccount.jsp");
    session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_PARAMETERS, paramHash);
   }

	private boolean userNameExist(String uname) {
		
		boolean exist = false;
		if(gatewayDAO.getByAccountUsername(uname)!=null){ 
			exist = true;
		}
		
		return exist;
		
	}
	
}
