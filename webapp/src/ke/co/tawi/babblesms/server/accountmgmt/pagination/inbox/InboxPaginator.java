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
package ke.co.tawi.babblesms.server.accountmgmt.pagination.inbox;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.log.IncomingLog;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.persistence.items.maskcode.ShortcodeDAO;
import ke.co.tawi.babblesms.server.persistence.logs.IncomingLogDAO;
import ke.co.tawi.babblesms.server.persistence.utils.CountUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Paginate an Inbox HTML view.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class InboxPaginator {

    /**
     *
     */
    public static final int PAGESIZE = 15; // The number of Incoming SMS to display per page
    
    private IncomingLogDAO incomingLogDAO;
    private CountUtils countUtils;
    
    private String accountuuid;
    private List<String> shortcodes;
    private ShortcodeDAO shortCodeDAO;
    
    private Account account;

    
    /**
     * Disable the default constructor.
     */
    private InboxPaginator() {}
    
    
    /**
     *
     * @param accountuuid
     */
    public InboxPaginator(String accountuuid) {
        this.accountuuid = accountuuid;
        countUtils = CountUtils.getInstance();
        incomingLogDAO = IncomingLogDAO.getInstance();
        
        shortCodeDAO = ShortcodeDAO.getInstance();
        
        getShortCodes(); // Populate with the list of shortcodes belonging to this account

        account = new Account();
        account.setUuid(accountuuid);  
    }
    

    /**
     *
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPasswd
     * @param dbPort
     */
    public InboxPaginator(String dbName, String dbHost,
            String dbUsername, String dbPasswd, int dbPort) {

        //initialize the DAOs
        countUtils = new CountUtils(dbName, dbHost, dbUsername, dbPasswd, dbPort);
        incomingLogDAO = new IncomingLogDAO(dbName, dbHost, dbUsername, dbPasswd, dbPort);
    }

    
    /**
     *
     */
    private void getShortCodes() {
        shortcodes = new ArrayList<>();
        List<Shortcode> smsCodes = shortCodeDAO.getShortcodebyaccountuuid(accountuuid);

        for (Shortcode code : smsCodes) {
            shortcodes.add(code.getUuid());
        }
    }
    

    /**
     *
     * @return the first page
     */
    public InboxPage getFirstPage() {
        InboxPage page = new InboxPage();
        
        List<IncomingLog> smsList = incomingLogDAO.getIncomingLog(account , 0, PAGESIZE);
 
        page = new InboxPage(1, getTotalPage(), PAGESIZE, smsList);	    

        return page;
    }

    
    /**
     * Provides the last page of the Incoming USSD session report
     *
     * @return	a Incoming USSD page
     */
    public InboxPage getLastPage() {
        InboxPage page = new InboxPage();

        List<IncomingLog> smsList = null;
        int sessionCount, startIndex;
        int totalPage = getTotalPage();

        startIndex = (totalPage - 1) * PAGESIZE;
        sessionCount = countUtils.getIncomingCount(accountuuid);
        
        smsList = incomingLogDAO.getIncomingLog(account, startIndex, sessionCount);

        page = new InboxPage(totalPage, totalPage, PAGESIZE, smsList);

        return page;
    }
    

    /**
     * Moves you forward to the page of the Incoming USSD session that comes
     * after the current page
     *
     * @param currentPage
     * @return	an Incoming USSD page
     */
    public InboxPage getNextPage(final InboxPage currentPage) {
        int totalPage = getTotalPage();

        InboxPage page = new InboxPage();
        
        List<IncomingLog> smsList = incomingLogDAO.getIncomingLog(account, currentPage.getPageNum() * PAGESIZE, 
        		((currentPage.getPageNum() * PAGESIZE) + PAGESIZE));

        page = new InboxPage(currentPage.getPageNum() + 1, totalPage, PAGESIZE, smsList);

        return page;
    }

    
    /**
     * Moves you backward to the page of the Incoming USSD session that comes
     * before the current page
     *
     * @param currentPage
     * @return	an Incoming USSD page
     */
    public InboxPage getPrevPage(final InboxPage currentPage) {
        int totalPage = getTotalPage();

        InboxPage page = new InboxPage();
        
        List<IncomingLog> smsList = incomingLogDAO.getIncomingLog(account, (currentPage.getPageNum() - 2) * PAGESIZE, 
        		((currentPage.getPageNum() - 1) * PAGESIZE));

        page = new InboxPage(currentPage.getPageNum() - 1, totalPage, PAGESIZE, smsList);

        return page;
    }
    

    /**
     * Calculates the total number of pages that would be printed for the SMS
     * that belong to the logged-in account
     *
     * @return	an integer
     */
    public int getTotalPage() {
        int totalSize = 0;

        //get the number of all sms belonging to this account
        totalSize = countUtils.getIncomingCount(accountuuid);

        return ((totalSize - 1) / PAGESIZE) + 1;
    }
}
