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
package ke.co.tawi.babblesms.server.servlet.accountmngmt;

import java.io.File;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.utils.DbFileUtils;
import ke.co.tawi.babblesms.server.servlet.util.file.CSVUtils;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;

import org.apache.log4j.Logger;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Import a csv or excel file to the database. 
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class UploadFile extends HttpServlet {

    private Logger logger;
    private DbFileUtils dbFileUtils;

    private ContactDAO userDAO;

    private ServletContext context = null;

    private final String ERROR_UPLOAD_FILE = "An error occurred,file not uploaded.";
    private final String FILE_EXTENSION_ERROR = "File extension not supported! Only csv files can be uploaded.";

    private Cache userCache;

    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        CacheManager mgr = CacheManager.getInstance();

        userCache = mgr.getCache(CacheVariables.CACHE_CONTACTS_BY_UUID);

        dbFileUtils = DbFileUtils.getInstance();

        userDAO = ContactDAO.getInstance();

        context = config.getServletContext();

        logger = Logger.getLogger(this.getClass());
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

        String requestURI = request.getRequestURI();
        String accountuuid = "";
        if (!ServletFileUpload.isMultipartContent(request)) {// Check that we have a file upload request
            throw new ServletException("Content type is not multipart/form-data");
        }

        DiskFileItemFactory factory = new DiskFileItemFactory();

        // Location to save data.
        File tempDirectory = FileUtils.getTempDirectory();

        factory.setRepository(tempDirectory);

        // Create a new file upload handler
        ServletFileUpload uploader = new ServletFileUpload(factory);
        String contextPath = request.getServletContext().getContextPath();
        File destination = new File(tempDirectory, contextPath + "_uploads");
        File phoneFile = new File(destination + "/phone.csv");
        File emailFile = new File(destination + "/email.csv");
        if (!destination.exists()) {
            destination.mkdirs();
        }

        String uri = "";
        String sqlQuery, ext, phoneQuery, emailQuery = "";
        try {
            List<FileItem> fileItemsList = uploader.parseRequest(request);
            Iterator<FileItem> fileItemsIterator = fileItemsList.iterator();

            while (fileItemsIterator.hasNext()) {
                FileItem fileItem = fileItemsIterator.next();
                sqlQuery = "";
                if(fileItem.isFormField()){
//                    if(fileItem.getFieldName().equals("accountuuid")){
                        accountuuid = fileItem.getString();
                        logger.info(accountuuid);
//                    }
                }

                if (!fileItem.isFormField()) {
                    
                        uri = "/account/addcontact.jsp";
                        sqlQuery = "COPY contact(uuid,name,accountsuuid,statusuuid) FROM STDIN WITH DELIMITER AS ',' CSV HEADER ";
                        phoneQuery = "COPY phone(uuid,phonenumber,contactsuuid,statusuuid,networkuuid) FROM STDIN WITH DELIMITER AS ',' CSV HEADER ";
                        emailQuery = "COPY email(uuid,address,contactsuuid,statusuuid) FROM STDIN WITH DELIMITER AS ',' CSV HEADER ";
                    
                    String fileName = fileItem.getName();
                    ext = FilenameUtils.getExtension(fileName);

                    //logger.info("File Name :" + fileName);
                    if (!StringUtils.equals(ext, "csv")) {
                        request.setAttribute(SessionConstants.ADMIN_UPLOAD_FILE_ERROR_KEY, FILE_EXTENSION_ERROR);

                    } else {

                        File uploadedFile = new File(destination + "/" + fileName);
                        fileItem.write(uploadedFile);

                        CSVUtils.sanitizeCSV(uploadedFile, destination, accountuuid);//append uuids to each row.

                        boolean importCSVFlag = dbFileUtils.importCSVToDatabase(sqlQuery, uploadedFile);
                        importCSVFlag = importCSVFlag && dbFileUtils.importCSVToDatabase(phoneQuery, phoneFile);
                        importCSVFlag = importCSVFlag && dbFileUtils.importCSVToDatabase(emailQuery, emailFile);

                        logger.info("File location is " + uploadedFile.getAbsolutePath());
//                        logger.info(requestURI);
//                        logger.info("importCSVToDatabase :" + importCSVFlag);
                        if (importCSVFlag) {
                            if (StringUtils.contains(requestURI, "user")) {
                                //updateUserHash();
                                //updateUserCache();
                            }
                            request.setAttribute(SessionConstants.ADMIN_UPLOAD_FILE_SUCCESS_KEY, "s");
                        } else {
                            request.setAttribute(SessionConstants.ADMIN_UPLOAD_FILE_ERROR_KEY, ERROR_UPLOAD_FILE);
                        }
                    }
                }
            }
        } catch (Exception ex) {
            logger.error(ex);
            request.setAttribute(SessionConstants.ADMIN_UPLOAD_FILE_ERROR_KEY, ERROR_UPLOAD_FILE);
        } finally {
            //destination.delete();
        }

        request.getRequestDispatcher(uri).forward(request, response);

    }

    /*
    private void updateUserHash() {
        Map<String, Contact> userHash = new HashMap<>();

        List<Contact> allUsers = userDAO.getAllContacts();
        for (Contact user : allUsers) {
            userHash.put(user.getUuid(), user);
        }
        context.setAttribute("userHash", userHash);

    }

    private void updateUserCache() {
        List<Contact> allUsers = userDAO.getAllContacts();
        for (Contact user : allUsers) {
            userCache.put(new Element(user.getUuid(), user));//update user cache.
        }

        List<Contact> allUser = userDAO.getAllContacts();
        for (Contact user : allUser) {
            usersCache.put(new Element(user.getUuid(), user));//update user cache.
        }
    }*/
    
    /**
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
