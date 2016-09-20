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
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="ke.co.tawi.babblesms.server.beans.log.OutgoingLog"%>
<%@page import="ke.co.tawi.babblesms.server.beans.messagetemplate.MsgStatus"%>

<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.logs.OutgoingLogDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.status.MessageStatusDAO"%>

<%@page import="ke.co.tawi.babblesms.server.accountmgmt.pagination.sent.SentPage"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.pagination.sent.SentPaginator"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionStatistics"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
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

    keys = msgstatusCache.getKeys();   //
    for (Object key : keys) {
        element = msgstatusCache.get(key);
        msgt = (MsgStatus) element.getObjectValue();
        messageHash.put(msgt.getUuid(), msgt.getDescription());
    }

 keys = networksCache.getKeys();

    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash.put(network.getUuid(), StringUtils.split(network.getName())[0] );
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
                || !StringUtils.endsWith(referrer, "sentReceived.jsp")
                || StringUtils.equalsIgnoreCase(pageParam, "first")) {
                
            sentPage = paginator.getFirstPage();
           
            // We are to give the last page
        } else if (StringUtils.equalsIgnoreCase(pageParam, "last")) {
        
            sentPage = paginator.getLastPage();
           
            // We are to give the previous page
        } else if (StringUtils.equalsIgnoreCase(pageParam, "previous")) {
        
            sentPage = paginator.getPrevPage(sentPage);
            
            // We are to give the next page 
        } else if (StringUtils.equalsIgnoreCase(pageParam, "next"))  {
            sentPage = paginator.getNextPage(sentPage);
        }
         
        
        session.setAttribute("currentOutgoingPage", sentPage);
        
        outgoingList = sentPage.getContents();
        
        ussdCount = (sentPage.getPageNum() - 1) * sentPage.getPagesize() + 1;
        
    }

    PhoneDAO phoneDAO = PhoneDAO.getInstance();
    ContactDAO ctDAO = ContactDAO.getInstance();

    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
    SimpleDateFormat timezoneFormatter = new SimpleDateFormat("z");
%>    


    
         <div class="col-lg-10 col-md-10 col-sm-5">
           
           <div id="refresh" class="col-lg-1 col-md-1 col-sm-1">
                <form name="pageForm" method="post" action="sentReceived.jsp">
                    <p><input class="btn btn-info" type="submit" name="refresh" value="Refresh" />
                        <input class="btn btn-info" type="hidden" name="page" value="First" /></p>
                </form>
            </div>

            <div id="pagination" class="col-lg-5 col-md-5 col-sm-2">
                <form name="pageForm" method="post" action="sentReceived.jsp">                                
                    <%                                            if (!sentPage.isFirstPage()) {
                    %>
                    <input class="btn btn-info" type="submit" name="page" value="First" />
                    <input class="btn btn-info" type="submit" name="page" value="Previous" />
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
                    <input class="btn btn-info" type="submit" name="page" value="Next">  
                    <input class="btn btn-info" type="submit" name="page" value="Last">
                    <%
                        }
                    %>                                
                </form>
            </div>                            

                <div id="export" class="col-lg-2 col-md-2 col-sm-1">                             
                 
                <form id="exportToExcel" name="exportExcelForm" method="post" action="exportExcelOutbox" target="_blank">
                    <p>
                        <input class="btn btn-info" type="hidden" name="exportExcel" value="Export All">
                        <input class="btn btn-info" type="submit" name="exporttoxEcel" value="Export Page As Excel" >
                        <input class="btn btn-info" type="hidden" name="page" value="sentReceived.jsp">
                    </p>
                </form>
                </div>
                <div id="export" class="col-lg-2 col-md-2 col-sm-1">
                <form id="exportToExcel" name="exportExcelForm" method="post" action="exportExcelOutbox" target="_blank">
                    <p>
                        <input class="btn btn-info" type="hidden" name="exportExcel" value="Export Page">
                        <input class="btn btn-info" type="submit" name="exporttoExcel" value="Export All As Excel" >
                        <input class="btn btn-info" type="hidden" name="page" value="sentReceived.jsp">
                    </p>
                </form>               
            </div>
    

        <div class="clear"></div>

        <div class="col-lg-10 col-md-10 col-sm-5">
            <table id="incomingUSSD" class="table table-striped table-bordered bootstrap-datatable datatable" summary="Outgoing">
                <thead>
                    <tr width="3%" >
                        <th>*</th>                        
                        <th>Message</th>
                        <th>Source</th>
                         <th>Network</th>
                        <th>Destination</th>                        
                        <th>Message Status</th>              
                        <th>Time (<%= timezoneFormatter.format(new Date()) %> Time Zone)</th>
                        <th>Message Id</th>
                    </tr>
                </thead>   
                <tbody>
                    <%
                        if (outgoingList != null) {
                            Contact contact = new Contact();
                            Phone phone;
                            for (OutgoingLog log : outgoingList) {
                            	if(messageHash.get(log.getMessagestatusuuid()).equals("Received")){ 
                    %>
                            <tr width="5%">

                                <td width="2%"><%=ussdCount%></td>

                                <td class="center"><%= log.getMessage()%></td>
                                <td class="center"><%= log.getOrigin()%> </td>
                                <td class="center"><%= networkHash.get(log.getNetworkUuid()) %></td>
                        <%                            
                            phone = phoneDAO.getPhone(log.getPhoneUuid());
                            if (phone != null) {
                                
                                if ((element = contactsCache.get(phone.getContactUuid() )) != null) {
                                    contact = (Contact) element.getObjectValue();
                                }
                        %>
                                <td class="center"><%= contact.getName() %></td>
                        
                        <%  } else { %>
                                <td class="center"><%= log.getDestination() %></td>  
                        <% } %>
                        
                        <td class="center"><%= messageHash.get(log.getMessagestatusuuid()) %></td>
                        <td class="center"><%= dateFormatter.format(log.getLogTime()) %> </td>
                        <td class="center"><%= log.getUuid() %></td>
                    </tr>

                    <%
                                ussdCount++;
                            }
                            } 	
                        }// end 'if (outgoingList != null)'
                    %>
                </tbody>
            </table>            
        </div>
 <div id="pagination" class="col-lg-10 col-md-10 col-sm-1">
                <form name="pageForm" method="post" action="sent.jsp">                                
                    <%                                            if (!sentPage.isFirstPage()) {
                    %>
                    <input class="btn btn-info" type="submit" name="page" value="First" />
                    <input class="btn btn-info" type="submit" name="page" value="Previous" />
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
                    <input class="btn btn-info" type="submit" name="page" value="Next">  
                    <input class="btn btn-info" type="submit" name="page" value="Last">
                    <%
                        }
                    %>                                
                </form>
            </div>                            
          </div>  
     
                                        
            
     

    

</div><!--/row-->


<jsp:include page="footer.jsp" />
