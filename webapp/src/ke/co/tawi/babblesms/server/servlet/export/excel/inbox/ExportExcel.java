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
package ke.co.tawi.babblesms.server.servlet.export.excel.inbox;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import ke.co.tawi.babblesms.server.accountmgmt.pagination.inbox.InboxPage;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.log.IncomingLog;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.logs.IncomingLogDAO;
import ke.co.tawi.babblesms.server.persistence.utils.DbFileUtils;
import ke.co.tawi.babblesms.server.session.SessionConstants;
import ke.co.tawi.babblesms.server.utils.export.ZipUtil;
import ke.co.tawi.babblesms.server.utils.export.topups.AllTopupsExportUtil;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

/**
 * Allows the client to export a list of Topup activity to a Microsoft Excel
 * sheet.
 * <p>
 * For a list of HTTP header fields, see
 * <a href="http://en.wikipedia.org/wiki/List_of_HTTP_header_fields">
 * http://en.wikipedia.org/wiki/List_of_HTTP_header_fields}
 * </a>
 * <p>
 * For a list of Microsoft Office MIME types, see
 * <a href="http://bit.ly/aZQzzH">http://bit.ly/aZQzzH</a>
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class ExportExcel extends HttpServlet {

    private final String SPREADSHEET_NAME = "Inbox Export.xlsx";
    private static final long serialVersionUID = 3896751907947782599L;

    private Cache accountsCache, networksCache, inboxCache;

    // This is a mapping between the UUIDs of networks and their names
    private HashMap<String, String> networkHash;

    // This is a mapping between the UUIDs of TopupStatuses and their status in English
    private HashMap<String, IncomingLog> incomingLogHash;

    private DbFileUtils dbFileUtils;
    private IncomingLogDAO incomingLogDAO;

    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
               

        CacheManager mgr = CacheManager.getInstance();
        accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
        networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
        incomingLogDAO = IncomingLogDAO.getInstance();
        
        networkHash = new HashMap<>();
        incomingLogHash = new HashMap<>();

        List keys = networksCache.getKeys();
        Element element;
        Network network;

        for (Object key : keys) {
            element = networksCache.get(key);
            network = (Network) element.getObjectValue();
            networkHash.put(network.getUuid(), network.getName());
        }

        IncomingLog incomingLog;
//        keys = inboxCache.getKeys();
//
//        for (Object key : keys) {
//            element = inboxCache.get(key);
//            incomingLog = (IncomingLog) element.getObjectValue();
//            incomingLogHash.put(incomingLog.getUuid(), incomingLog);
//        }

        dbFileUtils = DbFileUtils.getInstance();
    }

    /**
     * Returns a zipped MS Excel file of the data specified for exporting.
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     * @throws java.io.IOException
     */
    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        ServletOutputStream out = response.getOutputStream();
        response.setContentType("application/zip");
        response.setHeader("Cache-Control", "cache, must-revalidate");
        response.setHeader("Pragma", "public");

        HttpSession session = request.getSession(false);
        Account account;
        String fileName;

        String exportExcelOption = request.getParameter("exportExcel");

        String sessionEmail = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);

        Element element = accountsCache.get(sessionEmail);
        account = (Account) element.getObjectValue();

        fileName = new StringBuffer(account.getName()).append(" ")
                .append(StringUtils.trimToEmpty(account.getUsername()))
                .append(" ")
                .append(SPREADSHEET_NAME)
                .toString();

        response.setHeader("Content-Disposition", "attachment; filename=\""
                + StringUtils.replace(fileName, ".xlsx", ".zip") + "\"");

        File excelFile = new File(FileUtils.getTempDirectoryPath() + File.separator + fileName);
        File csvFile = new File(StringUtils.replace(excelFile.getCanonicalPath(), ".xlsx", ".csv"));
        File zippedFile = new File(StringUtils.replace(excelFile.getCanonicalPath(), ".xlsx", ".zip"));

        // These are to determine whether or not we have created a CSV & Excel file on disk
        boolean successCSVFile = true, successExcelFile = true;

        if (StringUtils.equalsIgnoreCase(exportExcelOption, "Export All")) { //export all pages
            successCSVFile = dbFileUtils.sqlResultToCSV(getExportInboxSqlQuery(account),
                    csvFile.toString(), '|');

            if (successCSVFile) {
                successExcelFile = AllTopupsExportUtil.createExcelExport(csvFile.toString(), "|", excelFile.toString());
            }

        } else if (StringUtils.equalsIgnoreCase(exportExcelOption, "Export Page")) { //export a single page

            InboxPage inboxPage = (InboxPage) session.getAttribute("currentIncomingPage");

            successExcelFile = AllTopupsExportUtil.createExcelExport(inboxPage.getContents(), networkHash,
                    networkHash, "|", excelFile.toString());

        } else {	//export search results

            InboxPage inboxPage = (InboxPage) session.getAttribute("currentSearchPage");

            successExcelFile = AllTopupsExportUtil.createExcelExport(inboxPage.getContents(), networkHash,
                    networkHash, "|", excelFile.toString());
        }

        if (successExcelFile) { // If we successfully created the MS Excel File on disk  
            // Zip the Excel file
            List<File> filesToZip = new ArrayList<>();
            filesToZip.add(excelFile);
            ZipUtil.compressFiles(filesToZip, zippedFile.toString());

            // Push the file to the request
            FileInputStream input = FileUtils.openInputStream(zippedFile);
            IOUtils.copy(input, out);
        }

        out.close();

        FileUtils.deleteQuietly(excelFile);
        FileUtils.deleteQuietly(csvFile);
        FileUtils.deleteQuietly(zippedFile);
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException, IOException
     * @throws java.io.IOException
     */
    @Override
    public void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * Gets the String that will be used to export all the topup activity of an
     * account holder.
     * <p>
     * Note that it is tied to the design of the database.
     *
     * @param account
     * @return the SQL query to be used
     */
    private String getExportInboxSqlQuery(Account account) {
        String query = "SELECT incominglog.uuid, incominglog.origin, shortcode.codenumber, "
                + "incominglog.message, incominglog.deleted, incominglog.datetime FROM incominglog "
                + "INNER JOIN shortcode ON incominglog.destination=shortcode.uuid INNER JOIN "
                + "account ON shortcode.accountuuid=account.uuid WHERE shortcode.accountuuid = '" 
                + account.getUuid() + "';";

        return query;
    }
}

/*
 ** Local Variables:
 **   mode: java
 **   c-basic-offset: 2
 **   tab-width: 2
 **   indent-tabs-mode: nil
 ** End:
 **
 ** ex: set softtabstop=2 tabstop=2 expandtab:
 **
 */
