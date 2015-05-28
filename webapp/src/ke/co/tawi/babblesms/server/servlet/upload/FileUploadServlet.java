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

import java.io.File;
import java.io.IOException;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.session.SessionConstants;

import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

 
/**
 * A Java servlet that handles file upload from client.
 * <p>
 * 
 * @author <a href="mailto:eugene@tawi.mobi">Eugene Chimita</a>
 */
public class FileUploadServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;
         
    // Upload settings
    private static final int MEMORY_THRESHOLD   = 1024 * 1024 * 3;  // 3MB
    private static final int MAX_FILE_SIZE      = 1024 * 1024 * 40; // 40MB
    private static final int MAX_REQUEST_SIZE   = 1024 * 1024 * 50; // 50MB
    
    /**
	 * 
	 * @param config
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);		
	}
 
	
   /**
    * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
 	*/
	@Override
   protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		
		HttpSession session = request.getSession(false);
		
		String path = "";
		
        // Process only if its multipart content
        if(ServletFileUpload.isMultipartContent(request)) {
            try {
                List<FileItem> multiparts = new ServletFileUpload(
                                         new DiskFileItemFactory()).parseRequest(request);
              
                for(FileItem item : multiparts){
                    if(!item.isFormField()){
                        String name = new File(item.getName()).getName();
                        item.write( new File(System.getProperty("java.io.tmpdir") + File.separator + name));
                        
                        path = System.getProperty("java.io.tmpdir")+File.separator+name;
                        
                        //
                        
                        
                    }
                }
                
               //File uploaded successfully
               request.setAttribute("message", "File Uploaded Successfully" + path);
               String msg = ContactUploadReader.readCSVFile(path);
               
               request.setAttribute("message",msg);
            } catch (Exception ex) {
               //request.setAttribute("message", "File Upload Failed due to " + ex);
            	request.setAttribute("message", "File Upload Failed!! make sure your file extention is .csv and it conforms to this format(uuid,name,phone,email,network)" + ex);
            	session.setAttribute("message", "File Upload Failed!! make sure your file extention is .csv and it conforms to this format(uuid,name,phone,email,network)" + ex);
            	System.out.println("Have set session attribute");
            }          
         
        } else {
            request.setAttribute("message",
                                 "Sorry this Servlet only handles file upload request");
        }
    
        request.getRequestDispatcher("/account/addcontact.jsp").forward(request, response);     
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