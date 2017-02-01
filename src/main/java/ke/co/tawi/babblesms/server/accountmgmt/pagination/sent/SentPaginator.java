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
package ke.co.tawi.babblesms.server.accountmgmt.pagination.sent;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.log.OutgoingLog;
import ke.co.tawi.babblesms.server.persistence.logs.OutgoingLogDAO;
import ke.co.tawi.babblesms.server.persistence.utils.CountUtils;

import java.util.List;


/**
 * Pagination of Sent HTML view.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SentPaginator {

  
    public static final int PAGESIZE = 15; // The number of SMS to display per page
    private OutgoingLogDAO outgoingLogDAO;
    private CountUtils countUtils;
    
    Account account = new Account();
    
    
    /**
     *
     * @param accountuuid
     */
    public SentPaginator(String accountuuid) {
    	account = new Account();
        account.setUuid(accountuuid); 
        
        countUtils = CountUtils.getInstance();
        outgoingLogDAO = OutgoingLogDAO.getInstance();
    }
    

    /**
     *
     * @param dbName
     * @param dbHost
     * @param dbUsername
     * @param dbPasswd
     * @param dbPort
     */
    public SentPaginator(String dbName, String dbHost,
            String dbUsername, String dbPasswd, int dbPort) {

        //initialize the DAOs
        countUtils = new CountUtils(dbName, dbHost, dbUsername, dbPasswd, dbPort);
        outgoingLogDAO = new OutgoingLogDAO(dbName, dbHost, dbUsername, dbPasswd, dbPort);
    }

    
    /**
     *
     * @return
     */
    public SentPage getFirstPage() {
        SentPage page = new SentPage();
        List<OutgoingLog> userList = outgoingLogDAO.get(account,0, PAGESIZE);
        page = new SentPage(1, getTotalPage(), PAGESIZE, userList);   

        return page;
    }
    

    /**
     * Provides the last page of the Incoming USSD session report
     *
     * @return	a Incoming USSD page
     */
    public SentPage getLastPage() {
        SentPage page = new SentPage();

        List<OutgoingLog> sessionList = null;
        int sessionCount, startIndex;
        int totalPage = getTotalPage();

        startIndex = (totalPage - 1) * PAGESIZE;
        sessionCount = countUtils.getIncomingCount(account.getUuid());
        sessionList = outgoingLogDAO.get(account,startIndex, sessionCount);

        page = new SentPage(totalPage, totalPage, PAGESIZE, sessionList);

        return page;
    }

    
    /**
     * Moves you forward to the page of the Incoming USSD session that comes
     * after the current page
     *
     * @param currentPage
     * @return	an Incoming USSD page
     */
    public SentPage getNextPage(final SentPage currentPage) {
        int totalPage = getTotalPage();

        SentPage page = new SentPage();

        List<OutgoingLog> sessionList = outgoingLogDAO.get(account,currentPage.getPageNum() * 
        		PAGESIZE, ((currentPage.getPageNum() * PAGESIZE) + PAGESIZE));

        page = new SentPage(currentPage.getPageNum() + 1, totalPage, PAGESIZE, sessionList);

        return page;
    }

    
    /**
     * Moves you backward to the page of the Incoming USSD session that comes
     * before the current page
     *
     * @param currentPage
     * @return	an Incoming USSD page
     */
    public SentPage getPrevPage(final SentPage currentPage) {
        int totalPage = getTotalPage();

        SentPage page = new SentPage();
        List<OutgoingLog> sessionList = outgoingLogDAO.get(account,(currentPage.getPageNum() - 2)
                * PAGESIZE, ((currentPage.getPageNum() - 1) * PAGESIZE));

        page = new SentPage(currentPage.getPageNum() - 1, totalPage, PAGESIZE, sessionList);

        return page;
    }

    
    /**
     * Calculates the total number of pages that would be printed for the USSD
     * sessions that belong to the logged-in account
     *
     * @return	an integer
     */
    public int getTotalPage() {
        int totalSize = 0;

        //get the number of all sessions belonging to this email
        totalSize = countUtils.getOutgoingLog(account.getUuid());

        //TODO: divide by the page size and add one to take care of remainders and what else?
        return ((totalSize - 1) / PAGESIZE) + 1;
    }
}
