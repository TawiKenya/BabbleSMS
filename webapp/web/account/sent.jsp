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

<jsp:include page="messageheader.jsp" />

<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.beans.log.IncomingLog"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.pagination.sent.SentPage"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.pagination.sent.SentPaginator"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionStatistics"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.log.OutgoingLog"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.logs.OutgoingLogDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.status.MessageStatusDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.messagetemplate.MsgStatus"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
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

    OutgoingLogDAO outgoinglogDAO = OutgoingLogDAO.getInstance();

    List<OutgoingLog> outgoingList;

    CacheManager mgr = CacheManager.getInstance();
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
    Cache msgstatusCache = mgr.getCache(CacheVariables.CACHE_MESSAGE_STATUS_BY_UUID);
    Cache contactsCache = mgr.getCache(CacheVariables.CACHE_CONTACTS_BY_UUID);
    Cache statisticsCache = mgr.getCache(CacheVariables.CACHE_STATISTICS_BY_ACCOUNT);

    HashMap<String, String> networkHash = new HashMap<String, String>();
    HashMap<String, String> messageHash = new HashMap<String, String>();
    HashMap<String, String> contactHash = new HashMap<String, String>();

    Element element;
    List keys;
    Network network;
    MsgStatus msgt;
    Contact contct;
    int incount = 0;  // Generic counter

    keys = msgstatusCache.getKeys();
    for (Object key : keys) {
        element = msgstatusCache.get(key);
        msgt = (MsgStatus) element.getObjectValue();
        messageHash.put(msgt.getUuid(), msgt.getDescription());
    }

    SentPaginator paginator = new SentPaginator(accountuuid);

    SessionStatistics statistics = new SessionStatistics();
    
    if ((element = statisticsCache.get(accountuuid)) != null) {
        statistics = (SessionStatistics) element.getObjectValue();
       
    }
  
   incount = statistics.getAllOutgoingUSSDCount();
   
    
    SentPage sentPage;
    int ussdCount = 0; // The current count of the USSD sessions

    if (incount == 0) { 	// This user has no Incoming USSD in the account
        sentPage = new SentPage();
        outgoingList = new ArrayList<OutgoingLog>();
        ussdCount = 0;

    } else {
        
        sentPage = (SentPage) session.getAttribute("currentOutgoingPage");
        
        String referrer = request.getHeader("referer");
        
        String pageParam = (String) request.getParameter("page");
        
        // We are to give the first page
        if (sentPage == null
                || !StringUtils.endsWith(referrer, "sent.jsp")
                || StringUtils.equalsIgnoreCase(pageParam, "first")) {
                
            sentPage = paginator.getFirstPage();
           
            // We are to give the last page
        } else if (StringUtils.equalsIgnoreCase(pageParam, "last")) {
        
            sentPage = paginator.getLastPage();
           
            // We are to give the previous page
        } else if (StringUtils.equalsIgnoreCase(pageParam, "previous")) {
        
            sentPage = paginator.getPrevPage(sentPage);
            
            // We are to give the next page 
        } else {
            sentPage = paginator.getNextPage(sentPage);
        }
         
        
        session.setAttribute("currentOutgoingPage", sentPage);
        
        outgoingList = sentPage.getContents();
        
        ussdCount = (sentPage.getPageNum() - 1) * sentPage.getPagesize() + 1;
        
    }

    PhoneDAO phnDAO = PhoneDAO.getInstance();
    ContactDAO ctDAO = ContactDAO.getInstance();

    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
%>    


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Sent</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        <!--<div class="box-header well" data-original-title>
            <i class="icon-refresh"></i>
            <div id="refres">

                <form name="pageForm" method="post" action="refreshUSSD">

                    <input class="btn" title="refresh page" type="submit" name="refresh" value="Refresh"/>

                    <input class="toolbarBtn" type="hidden" name="page" value="sent.jsp" />
                </form>
            </div>

            <a class="btn" href="#" title="delete message" data-rel="tooltip"><i class="icon-trash"></i> Delete</a>  
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>-->

        <div class="clear"></div>

        <div class="ussdNavControls" style="margin-top: 1%;width:98%;margin-left:9px;">


            <div id="pagination">
                <form name="pageForm" method="post" action="sent.jsp">                                
                    <%                                            if (!sentPage.isFirstPage()) {
                    %>
                    <input class="toolbarBtn" type="submit" name="page" value="First" />
                    <input class="toolbarBtn" type="submit" name="page" value="Previous" />
                    <%
                        }
                    %>
                    <span class="pageInfo">Page 
                        <span class="pagePosition currentPage"><%= sentPage.getPageNum()%></span> of 
                        <span class="pagePosition"><%= sentPage.getTotalPage()%></span>
                    </span>   
                    <%
                        if (!sentPage.isLastPage()) {
                    %>
                    <input class="toolbarBtn" type="submit" name="page" value="Next">  
                    <input class="toolbarBtn" type="submit" name="page" value="Last">
                    <%
                        }
                    %>                                
                </form>
            </div>                            

                <div id="export" class="row">
                <form id="exportToPDF" name="exportCSV" method="post" action="exportCSV" target="_blank">
                    <p>
                        <input class="toolbarBtn" type="submit" name="exportCSV" value="Export CSV" >
                        <input class="toolbarBtn" type="hidden" name="page" value="sent.jsp">
                    </p>
                </form>
                
                 
                <form id="exportToExcel" name="exportExcelForm" method="post" action="exportExcel" target="_blank">
                    <p>
                        <input class="toolbarBtn" type="hidden" name="exportExcel" value="Export All">
                        <input class="toolbarBtn" type="submit" name="exporttoxEcel" value="Export All Excel" >
                        <input class="toolbarBtn" type="hidden" name="page" value="sent.jsp">
                    </p>
                </form>

                <form id="exportToExcel" name="exportExcelForm" method="post" action="exportExcel" target="_blank">
                    <p>
                        <input class="toolbarBtn" type="hidden" name="exportExcel" value="Export Page">
                        <input class="toolbarBtn" type="submit" name="exporttoExcel" value="Export Excel" >
                        <input class="toolbarBtn" type="hidden" name="page" value="sent.jsp">
                    </p>
                </form>               
            </div>
        </div>

        <div class="clear"></div>

        <div class="box-content" style="margin-top: -0.5%">
            <table id="incomingUSSD" class="ussdTable" summary="Outgoing">
                <thead>
                    <tr>
                        <th>*</th>                        
                        <th>Message</th>
                        <th>Source</th>
                        <th>Destination</th>
                        <th>Message Id</th>
                        <th>Message Status</th>
                        <th>Sent</th>
                        <th>Delivered</th>
                    </tr>
                </thead>   
                <tbody>
                    <%
                        if (outgoingList != null) {
                            for (OutgoingLog code : outgoingList) {
                               String status = messageHash.get(code.getMessagestatusuuid());

                    %>
                    <tr>

                        <td width="10%"><%=ussdCount%></td>
                        
                        <td class="center"><%=code.getMessage()%></td>
                        <td class="center"><%=code.getOrigin()%> </td>
                        <%
                            if (phnDAO.getPhones(code.getDestination()) != null) {
                               
                                List<Phone> phoneList = phnDAO.getPhones(code.getDestination());
                                Phone phone = phoneList.get(0);
                                 String contactuuid = phone.getContactsuuid();
				Contact contacts = ctDAO.getContact(contactuuid);
				String contactname = contacts.getName();
                        %>
                       <td class="center"><%=contactname%></td> 
                        <%} else {%>
                        <td class="center"><%=code.getDestination()%></td>  
                      <%}%>
                        <td class="center"><%=code.getUuid()%></td>
                        <td class="center"><%=status%></td>
                        <td class="center"><%= dateFormatter.format(code.getLogTime()) %> </td>
                        <td class="center"><%=code.getLogTime()%> </td>
                    </tr>

                    <%
                                ussdCount++;
                            }
                        }// end 'if (outgoingList != null)'
                    %>
                </tbody>
            </table>            
        </div>

        <div class="clear"></div>

        <div class="ussdNavControls" style="margin-top: 1%;width:98%;margin-left:9px;">
            <div id="refresh">
                <form name="pageForm" method="post" action="refreshUSSD">
                    <p><input class="toolbarBtn" type="submit" name="refresh" value="Refresh" />
                        <input class="toolbarBtn" type="hidden" name="page" value="sent.jsp" /></p>
                </form>
            </div>

            <div id="pagination">
                <form name="pageForm" method="post" action="sent.jsp">                                
                    <%                                            if (!sentPage.isFirstPage()) {
                    %>
                    <input class="toolbarBtn" type="submit" name="page" value="First" />
                    <input class="toolbarBtn" type="submit" name="page" value="Previous" />
                    <%
                        }
                    %>
                    <span class="pageInfo">Page 
                        <span class="pagePosition currentPage"><%= sentPage.getPageNum()%></span> of 
                        <span class="pagePosition"><%= sentPage.getTotalPage()%></span>
                    </span>   
                    <%
                        if (!sentPage.isLastPage()) {
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
                        <input class="toolbarBtn" type="hidden" name="page" value="sent.jsp">
                    </p>
                </form>

                <form id="exportToExcel" name="exportExcelForm" method="post" action="exportExcel" target="_blank">
                    <p>
                        <input class="toolbarBtn" type="submit" name="exportExcel" value="Export Excel" >
                        <input class="toolbarBtn" type="hidden" name="page" value="sent.jsp">
                    </p>
                </form>
            </div>
        </div>

    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
