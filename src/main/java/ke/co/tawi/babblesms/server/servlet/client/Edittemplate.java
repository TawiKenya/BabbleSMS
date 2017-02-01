package ke.co.tawi.babblesms.server.servlet.client;

import java.io.IOException;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.log4j.Logger;

import ke.co.tawi.babblesms.server.beans.messagetemplate.MessageTemplate;
import ke.co.tawi.babblesms.server.persistence.template.MessageTemplateDAO;
import ke.co.tawi.babblesms.server.session.SessionConstants;

/**
 * Receives a form request to edit a template's details
 * 
 * <p>
 * 
 * @author dennis <a href="mailto:dennism@tawi.mobi">Dennis Mutegi</a>
 *
 */

public class Edittemplate extends HttpServlet {
	
	private final String success = "Your message template has been updated successfully";
	private final String failure ="failed to update your message template.Try again";
	private Logger logger = Logger.getLogger(this.getClass());
	
	
	/**
	 * @param config
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 * 
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

	}
	
	/**
	 * method to handle form processing
	 * @param request
	 * @param response
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * 
	 * @throws IOException
	 */
	@Override
	protected void doPost(HttpServletRequest request , HttpServletResponse response) throws IOException{
		HttpSession session = request.getSession(true);
		
		String title = request.getParameter("title");
		String contents = request.getParameter("content");
		String templateuuid = request.getParameter("templateuuid");
		
		if(title.equals("")){
			session.setAttribute(SessionConstants.ADD_ERROR, "please provide template title");
		}

		else{
			MessageTemplate template = new MessageTemplate();
			template.setUuid(templateuuid);
			template.setTitle(title);;
			template.setContents(contents);
			
			MessageTemplateDAO templateDAO = MessageTemplateDAO.getInstance();
			if(templateDAO.update(template,templateuuid)){
				session.setAttribute(SessionConstants.UPDATE_SUCCESS, success);
			}
			else{
				session.setAttribute(SessionConstants.ADD_ERROR, failure);
			}
			
		}
	
		response.sendRedirect("messagetemplate.jsp");
	}


}
