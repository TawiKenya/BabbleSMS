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

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.LinkedList;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

import org.apache.commons.io.FileUtils;

import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;

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
	
	private ContactDAO contactDAO;
	private PhoneDAO phoneDAO;
	private ContactGroupDAO contactGroupDAO;
	private Cache accountsCache;
	
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
       
       contactDAO = ContactDAO.getInstance();
       phoneDAO = PhoneDAO.getInstance();
       contactGroupDAO = ContactGroupDAO.getInstance(); 
       
       CacheManager mgr = CacheManager.getInstance();
       accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
   }
	
	
	/**
    * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 	*/
	@Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		File uploadedFile = null;
		
		List<String> groupUuids = new LinkedList<>();
		Account account = new Account();
		
		HttpSession session = request.getSession(false);
		
		String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
		
		Element element;
        if ((element = accountsCache.get(username)) != null) {
            account = (Account) element.getObjectValue();
        }
        
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
		    	// Here we assume only a Group UUID has been submitted as a text field
		    	groupUuids.add(item.getString());
		        
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
       String feedback = uploadUtil.inspectContactFile(uploadedFile);
	   session.setAttribute(UPLOAD_FEEDBACK, feedback);
    	    	
       response.sendRedirect("addcontact.jsp");
       
       
       // Process the file into the database if it is ok
       if(StringUtils.equals(feedback, UPLOAD_SUCCESS)) {
    	   uploadUtil.saveContacts(uploadedFile, account, contactDAO, phoneDAO, groupUuids, contactGroupDAO);
       }
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
