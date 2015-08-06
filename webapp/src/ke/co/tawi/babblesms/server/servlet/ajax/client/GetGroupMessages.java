/**
 * 
 */
package ke.co.tawi.babblesms.server.servlet.ajax.client;

import java.io.IOException;
import java.io.OutputStream;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ke.co.tawi.babblesms.server.persistence.utils.SentGroupMessageCount;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * @author Migwi Ndung'u <code>mailto:<a>migwindungu0@gmail.com</a>
 *</code> 
 *Returns JSON with STatus and their various  count for the given SentGroupUuid
 *
 *Sample output {  "In Transit": 8,  "Received": 8,  "Rejected": 8,  "Failure": 0,  "Sent": 8};
 * 
 */
public class GetGroupMessages extends HttpServlet {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private SentGroupMessageCount SGMCount;
	

	/**
	 * Initiate the servlet life
	 *
	 * @param config
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		 SGMCount = SentGroupMessageCount.getInstance();
	}
	
	
	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		String SentGroupUuid = request.getParameter("SentGroupUuid");	

		OutputStream out = response.getOutputStream();

		response.setContentType("application/json;charset=UTF-8");

		// Instantiate the JSon
		// The '=' sign is encoded to \u003d. Hence you need to use
		// disableHtmlEscaping().
		Gson gson = new GsonBuilder().disableHtmlEscaping()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.setPrettyPrinting().serializeNulls().create();
		
		out.write(gson.toJson(SGMCount.getGroupContactMessages(SentGroupUuid)).getBytes()); 
		out.flush();
		out.close();
	}
	
	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException, IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

}
