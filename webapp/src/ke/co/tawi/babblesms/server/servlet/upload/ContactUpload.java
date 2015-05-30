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
package ke.co.tawi.babblesms.server.servlet.upload;

import ke.co.tawi.babblesms.server.session.SessionConstants;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.log4j.Logger;

/**
 * To capture a Contact upon upload.
 * <p>
 *
 * @author <a href="mailto:eugene@tawi.mobi">Eugene Chimita</a>
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class ContactUpload extends HttpServlet {
	  	
	public final static String UPLOAD_FEEDBACK = "UploadFeedback";
	public final static String UPLOAD_SUCCESS = "You have successfully uploaded your contacts.";
	
	private Logger logger;
	
	private UploadUtil uploadUtil;
	
	
	/**
    *
    * @param config
    * @throws ServletException
    */
   @Override
   public void init(ServletConfig config) throws ServletException {
       super.init(config);
       
       // Create a factory for disk-based file items
       DiskFileItemFactory factory = new DiskFileItemFactory();

       File repository = FileUtils.getTempDirectory();
       factory.setRepository(repository);
       
       uploadUtil = new UploadUtil();
   }
	
	
	/**
    * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 	*/
	@Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		File uploadedFile = null;
		
		HttpSession session = request.getSession(false);
		
		String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
		
		// Create a factory for disk-based file items
       DiskFileItemFactory factory = new DiskFileItemFactory();

       // Set up where the files will be stored on disk
       File repository = new File(System.getProperty("java.io.tmpdir") + File.separator + username);
       FileUtils.forceMkdir(repository); 
       factory.setRepository(repository);
       
       // Create a new file upload handler
       ServletFileUpload upload = new ServletFileUpload(factory);

       // Parse the request              
       try {
		List<FileItem> items = upload.parseRequest(request);
		Iterator<FileItem> iter = items.iterator();
		
		FileItem item;
		
		while (iter.hasNext()) {
		    item = iter.next();

		    if (item.isFormField()) {
		        processFormField(item);
		        
		    } else {
		    	uploadedFile = processUploadedFile(item);
		    }
		}// end 'while (iter.hasNext())'
				
	    } catch (FileUploadException e) {
	    	logger.error("FileUploadException while getting File Items.");
			logger.error(e);
	   }
	       
       // Here we assume that only one file was uploaded
       // First we inspect if it is ok
	   session.setAttribute(UPLOAD_FEEDBACK, uploadUtil.inspectContactFile(uploadedFile));
    	    	
       response.sendRedirect("addcontact.jsp");
	}	

	
	/**
	 * @param item
	 */
	private void processFormField(FileItem item) {
		String name = item.getFieldName();
	    String value = item.getString();
	    
	    logger.info("Field name is '" + name + "', value is '" + value + "'.");
	}
	
	
	/**
	 * @param item
	 * @return the file handle
	 */
	private File processUploadedFile(FileItem item) {
		// A random folder in the system temporary directory and write the file there
		String folder = System.getProperty("java.io.tmpdir") + File.separator + RandomStringUtils.randomAlphabetic(5);
		File file = null;
		
        try {
			FileUtils.forceMkdir(new File(folder));
			file = new File(folder + File.separator + item.getName());
			item.write(file); 
			
		} catch (IOException e) {
			logger.error("IOException while processUploadedFile: " + item.getName());
			logger.error(e);
			
		} catch (Exception e) {
			logger.error("Exception while processUploadedFile: " + item.getName());
			logger.error(e);
		} 
        
        return file;
	}
}
