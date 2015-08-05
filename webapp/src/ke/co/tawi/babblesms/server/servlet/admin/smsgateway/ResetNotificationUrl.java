/**
 * 
 */
package ke.co.tawi.babblesms.server.servlet.admin.smsgateway;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * @author peter
 *
 */
public class ResetNotificationUrl extends HttpServlet  {

	 public void init(ServletConfig config) throws ServletException {
	        super.init(config);
	 }
	 
	 /**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 */
	public void doPost(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	    	HttpSession session = request.getSession(false);
	    	
	 }
	             
	    /**
	     * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	     */
	    public void doGet(HttpServletRequest request, HttpServletResponse response)
	            throws ServletException, IOException {
	        doPost(request, response);
	    }

}
