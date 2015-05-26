/**
 * 
 */
package ke.co.tawi.babblesms.server.servlet.account.settings;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.session.SessionConstants;

import org.apache.log4j.Logger;

/**
 * @author peter
 *
 */
public class Password  extends HttpServlet{

	
	private Logger logger=Logger.getLogger(this.getClass());
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public void doPost(HttpServletRequest request,HttpServletResponse response )throws
	ServletException, IOException{
		//HttpSession session = request.getSession(false);
		
		String username = request.getParameter("username").trim();
		String email = request.getParameter("email").trim();
		
		
		if(username==""){
			username="peter";
		}else if(email==""){
			email="peter@yahoo.com";
		}else{
			
			logger.info(username);
			logger.info(email);
			
			
			
			
		}
		
		
		
		
		
		
	}
	
	@Override
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws
	ServletException ,IOException{
		
	}

}
