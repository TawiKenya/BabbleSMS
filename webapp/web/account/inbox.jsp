<%
    /**
    Copyright 2015 Tawi Commercial Services Ltd

    Licensed under the Open Software License, Version 3.0 (the “License”); you may 
    not use this file except in compliance with the License. You may obtain a copy 
    of the License at:
    http://opensource.org/licenses/OSL-3.0

    Unless required by applicable law or agreed to in writing, software distributed 
    under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR
    CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and limitations 
    under the License.
    */
%>

<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.pagination.inbox.InboxPage"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.pagination.inbox.InboxPaginator"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionStatistics"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.logs.IncomingLogDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.log.IncomingLog"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Shortcode"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>

<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.CacheManager"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>



<%
    // The following is for session management.    
    if (session == null) {
        response.sendRedirect("../index.jsp");
    }

    String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
    if (StringUtils.isEmpty(username)) {
        response.sendRedirect("../index.jsp");
    }

    session.setMaxInactiveInterval(SessionConstants.SESSION_TIMEOUT);
    response.setHeader("Refresh", SessionConstants.SESSION_TIMEOUT + "; url=../logout");

    String accountuuid = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_ACCOUNTUUID);
    //IncomingLogDAO incominglogDAO = IncomingLogDAO.getInstance();
//    List<IncomingLog> incomingList = incominglogDAO.getIncomingLogByAccount(accountuuid);
    List<IncomingLog> incomingList;

    CacheManager mgr = CacheManager.getInstance();
    Cache statisticsCache = mgr.getCache(CacheVariables.CACHE_STATISTICS_BY_ACCOUNT);
    Cache shortcodesCache = mgr.getCache(CacheVariables.CACHE_SHORTCODE_BY_UUID);
    Cache contactsCache = mgr.getCache(CacheVariables.CACHE_CONTACTS_BY_UUID);
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);

    HashMap<String, String> shortcodeHash = new HashMap<String, String>();
    HashMap<String, String> contactHash = new HashMap<String, String>();
    HashMap<String, String> networkHash = new HashMap<String, String>();

    Element element;
    List keys;
    Shortcode shortcode;
    Contact contct;
    Network network;
    int incount = 0;  // Generic counter

    InboxPaginator paginator = new InboxPaginator(accountuuid);

    SessionStatistics statistics = new SessionStatistics();

    if ((element = statisticsCache.get(accountuuid)) != null) {
        statistics = (SessionStatistics) element.getObjectValue();
    }

    keys = shortcodesCache.getKeys();

    for (Object key : keys) {
        element = shortcodesCache.get(key);
        shortcode = (Shortcode) element.getObjectValue();
        shortcodeHash.put(shortcode.getUuid(), shortcode.getCodenumber());
    }

   /* keys = contactsCache.getKeys();
System.out.println("ddddddddddddd"+keys.size()+"ddddddeeee");
    for (Object key : keys) {
        element = contactsCache.get(key);
        contct = (Contact) element.getObjectValue();
        contactHash.put(contct.getUuid(), contct.getName());
    }*/
    
     keys = networksCache.getKeys();

    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash.put(network.getUuid(), network.getName());
    }

    incount = statistics.getAllIncomingUSSDCount();
    InboxPage inboxPage;
    int ussdCount = 0; // The current count of the USSD sessions

    if (incount == 0) { 	// This user has no Incoming USSD in the account
        inboxPage = new InboxPage();
        incomingList = new ArrayList<IncomingLog>();
        ussdCount = 0;

    } else {
        inboxPage = (InboxPage) session.getAttribute("currentInboxPage");
        String referrer = request.getHeader("referer");
        String pageParam = (String) request.getParameter("page");

        // We are to give the first page
        if (inboxPage == null
                || !StringUtils.endsWith(referrer, "inbox.jsp")
                || StringUtils.equalsIgnoreCase(pageParam, "first")) {
            inboxPage = paginator.getFirstPage();

            // We are to give the last page
        } else if (StringUtils.equalsIgnoreCase(pageParam, "last")) {
            inboxPage = paginator.getLastPage();

            // We are to give the previous page
        } else if (StringUtils.equalsIgnoreCase(pageParam, "previous")) {
            inboxPage = paginator.getPrevPage(inboxPage);

            // We are to give the next page 
        } else {
            inboxPage = paginator.getNextPage(inboxPage);
        }

        session.setAttribute("currentInboxPage", inboxPage);

        incomingList = inboxPage.getContents();

        ussdCount = (inboxPage.getPageNum() - 1) * inboxPage.getPagesize() + 1;
    }

    PhoneDAO phnDAO = PhoneDAO.getInstance();
    ContactDAO ctssDAO = ContactDAO.getInstance();

%> 
<jsp:include page="messageheader.jsp" />


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Inbox</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        <!--<div class="box-header well" data-original-title>-->
            <!--<div id="refresh">
                <form name="pageForm" method="post" action="refreshUSSD">
                    <p><input class="toolbarBtn" type="submit" name="refresh" value="Refresh" />
                        <input class="toolbarBtn" type="hidden" name="page" value="inbox.jsp" /></p>
                </form>
            </div>-->

           <!-- <a class="btn" href="#" title="delete message" data-rel="tooltip"><i class="icon-trash"></i> Delete</a> --> 
           <!-- <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>-->
           
        <!--</div>-->

        <div class="clear"></div>

        <div class="ussdNavControls">
            <div id="pagination">
                <form name="pageForm" method="post" action="inbox.jsp">                                
                    <%                                            if (!inboxPage.isFirstPage()) {
                    %>
                    <input class="toolbarBtn" type="submit" name="page" value="First" />
                    <input class="toolbarBtn" type="submit" name="page" value="Previous" />
                    <%
                        }
                    %>
                    <span class="pageInfo">Page 
                        <span class="pagePosition currentPage"><%= inboxPage.getPageNum()%></span> of 
                        <span class="pagePosition"><%= inboxPage.getTotalPage()%></span>
                    </span>   
                    <%
                        if (!inboxPage.isLastPage()) {
                    %>
                    <input class="toolbarBtn" type="submit" name="page" value="Next">  
                    <input class="toolbarBtn" type="submit" name="page" value="Last">
                    <%
                        }
                    %>                                
                </form>
            </div>

            <div id="export">                
                <form id="exportToPDF" name="exportCSV" method="post" action="exportCSV" target="_blank">
                    <p>
                        <input class="toolbarBtn" type="submit" name="exportCSV" value="Export CSV" >
                        <input class="toolbarBtn" type="hidden" name="page" value="inbox.jsp">
                    </p>
                </form>

                <form id="exportToExcel" name="exportExcelForm" method="post" action="exportExcel" target="_blank">
                    <p>
                        <input class="toolbarBtn" type="hidden" name="exportExcel" value="Export Page">
                        <input class="toolbarBtn" type="submit" name="exporttoExcel" value="Export Page Excel" >
                        <input class="toolbarBtn" type="hidden" name="page" value="inbox.jsp">
                    </p>
                </form>

                <form id="exportToExcel" name="exportExcelForm" method="post" action="exportExcel" target="_blank">
                    <p>
                        <input class="toolbarBtn" type="hidden" name="exportExcel" value="Export All">
                        <input class="toolbarBtn" type="submit" name="exporttoxEcel" value="Export All Excel" >
                        <input class="toolbarBtn" type="hidden" name="page" value="inbox.jsp">
                    </p>
                </form>

            </div>
        </div>

        <div class="clear"></div>



        <div class="box-content">
            <table id="incomingUSSD" class="ussdTable" summary="Outgoing">
                <thead>
                    <tr>
                        <th>*</th>
                        <th>Message Id</th>
                        <th>Message</th>
                        <th>Source</th>                        
                        <th>Destination</th>
                        <!--<th>network</th>-->
                        <th>Time</th>
                    </tr>
                </thead>   
                <tbody>
                    <%
                        if (incomingList != null) {
                            for (IncomingLog code : incomingList) {
                    %>
                    <tr>
                        <td width="10%"><%=ussdCount%></td>
                        <td class="center"><%=code.getUuid()%></td>
                        <td class="center"><%=code.getMessage()%></td>
                        <%
                            //if phone number exists print out the contact name else print the number
                            if (phnDAO.getPhones(code.getOrigin()).size() >= 1) {
                                Phone p =phnDAO.getPhones(code.getOrigin()).get(0);
                                String contact = p.getContactsuuid();
                                String sourcename = ctssDAO.getContact(contact).getName();
                        %>
                        <td class="center"><%=sourcename%></td>  
                        <%} else {%>
                        <td class="center"><%=code.getOrigin()%></td>  
                        <%}%>
                        <td class="center"><%=code.getDestination()%></td>
                        <!-- <td class="center"></td>-->
                        <td class="center"><%=code.getLogTime()%> </td>

                    </tr>

                    <%
                                ussdCount++;
                            }
                        }
//<%=networkHash.get(code.getNetwork())use
                    %>
                </tbody>
            </table>            
        </div>

        <div class="clear"></div>

        <div class="ussdNavControls">
            <div id="refresh">
                <form name="pageForm" method="post" action="refreshUSSD">
                    <p><input class="toolbarBtn" type="submit" name="refresh" value="Refresh" />
                        <input class="toolbarBtn" type="hidden" name="page" value="inbox.jsp" /></p>
                </form>
            </div>

            <div id="pagination">
                <form name="pageForm" method="post" action="inbox.jsp">                                
                    <%
                        if (!inboxPage.isFirstPage()) {
                    %>
                    <input class="toolbarBtn" type="submit" name="page" value="First" />
                    <input class="toolbarBtn" type="submit" name="page" value="Previous" />
                    <%
                        }
                    %>
                    <span class="pageInfo">Page 
                        <span class="pagePosition currentPage"><%= inboxPage.getPageNum()%></span> of 
                        <span class="pagePosition"><%= inboxPage.getTotalPage()%></span>
                    </span>   
                    <%
                        if (!inboxPage.isLastPage()) {
                    %>
                    <input class="toolbarBtn" type="submit" name="page" value="Next">  
                    <input class="toolbarBtn" type="submit" name="page" value="Last">
                    <%
                        }
                    %>                                
                </form>
            </div>                            

            <div id="export">                
                <form id="exportToPDF" name="exportCSV" method="post" action="exportCSV" target="_blank">
                    <p>
                        <input class="toolbarBtn" type="submit" name="exportCSV" value="Export CSV" >
                        <input class="toolbarBtn" type="hidden" name="page" value="inbox.jsp">
                    </p>
                </form>

                <form id="exportToExcel" name="exportExcelForm" method="post" action="exportExcel" target="_blank">
                    <p>
                        <input class="toolbarBtn" type="submit" name="exportExcel" value="Export Excel" >
                        <input class="toolbarBtn" type="hidden" name="page" value="inbox.jsp">
                    </p>
                </form>
            </div>
        </div>    


    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />