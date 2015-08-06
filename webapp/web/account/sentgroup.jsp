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

<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Group"%>
<%@page import="ke.co.tawi.babblesms.server.beans.log.OutgoingGrouplog"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.logs.OutgoingLogDAO"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.pagination.sent.SentGroupPage"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.pagination.sent.SentGroupPaginator"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.status.MessageStatusDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.logs.OutgoingGroupLogDAO"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="net.sf.ehcache.CacheManager"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>

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

    CacheManager mgr = CacheManager.getInstance();
    Cache accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);    
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);

    OutgoingGroupLogDAO outgoinglogDAO = OutgoingGroupLogDAO.getInstance();
    

    GroupDAO groupDAO = GroupDAO.getInstance();
    
    Account account = new Account();
    Element element;
    if ((element = accountsCache.get(username)) != null) {
        account = (Account) element.getObjectValue();
    }

    List<OutgoingGrouplog> outgoingList ;

    List<Group> groupList = groupDAO.getGroups(account);

    HashMap<String, String> groupHash = new HashMap<String, String>();
    HashMap<String, String> networkHash = new HashMap<String, String>();


    List keys = networksCache.getKeys();
    Network network;

    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash.put(network.getUuid(), StringUtils.split(network.getName())[0] );
    }

    for(Group group : groupList) {
        groupHash.put(group.getUuid(), group.getName());
    }        

    SentGroupPaginator paginator = new SentGroupPaginator(account.getUuid());    
    
    SentGroupPage sgPage;
    int ussdCount = 0; // The current count of the USSD sessions

    
        sgPage = (SentGroupPage) session.getAttribute("currentPage");
        String referrer = request.getHeader("referer");
        String pageParam = (String) request.getParameter("page");

        // We are to give the first page
        if (sgPage == null
                || !StringUtils.endsWith(referrer, "sentgroup.jsp")
                || StringUtils.equalsIgnoreCase(pageParam, "First")) {
            sgPage = paginator.getFirstPage();

            // We are to give the last page
        } else if (StringUtils.equalsIgnoreCase(pageParam, "Last")) {
            sgPage = paginator.getLastPage();

            // We are to give the previous page
        } else if (StringUtils.equalsIgnoreCase(pageParam, "Previous")) {
            sgPage = paginator.getPreviousPage(sgPage);

            // We are to give the next page 
        } else if (StringUtils.equalsIgnoreCase(pageParam, "next"))  {
            sgPage = paginator.getNextpage(sgPage);
        }

        session.setAttribute("currentPage", sgPage);

        outgoingList = sgPage.getOutGoingGroupList();

        ussdCount = (sgPage.getPageNumber() - 1) * sgPage.getPageSize() + 1;

    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy h:mma");
    SimpleDateFormat timezoneFormatter = new SimpleDateFormat("z");
%>    
<jsp:include page="messageheader.jsp" />


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Sent Group</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        <!--<div class="box-header well" data-original-title>
            <a class="toolbarBtn" href="#" title="refresh page" data-rel="tooltip"><i class="icon-refresh"></i> Refresh</a>                  
            <a class="toolbarBtn" href="#" title="delete message" data-rel="tooltip"><i class="icon-trash"></i> Delete</a>  
           <!-- <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>-->
        <!--</div>-->


        <div class="clear"></div>

        <div class="ussdNavControls" style="margin-top: 1%;width:98%;margin-left: 9px;">
        <div id="refresh">
                <form name="pageForm" method="post" action="sentgroup.jsp">
                    <input class="toolbarBtn" type="submit" name="refresh" value="Refresh" />
                    <input class="toolbarBtn" type="hidden" name="page" value="First" />
                     </form>
            </div>
            <div id="pagination">
                <form name="pageForm" method="post" action="sentgroup.jsp">                                
                    <%                                            
                        if (!sgPage.isFirstPage()) {
                    %>
                        <input class="toolbarBtn" type="submit" name="page" value="First" />
                        <input class="toolbarBtn" type="submit" name="page" value="Previous" />
                    <%
                        }
                    %>
                    <span class="pageInfo">Page 
                        <span class="pagePosition currentPage"><%= sgPage.getPageNumber()%></span> of 
                        <span class="pagePosition"><%= sgPage.getTotalSize()%></span>
                    </span>   
                    <%
                        if (!sgPage.isLastPage()) {                        
                    %>
                        <input class="toolbarBtn" type="submit" name="page" value="Next">  
                        <input class="toolbarBtn" type="submit" name="page" value="Last">
                    <%
                        }
                    %>                                
                </form>
            </div>

            
        </div>

        <div class="clear"></div>

         <div class="box-content" style="margin-top: -0.5%">
            <table id="incomingUSSD" class="ussdTable" summary="Outgoing">
                <thead>
                    <tr>

                        <th>*</th>  
                        <th>Group</th>
                        <th>Message</th>
                        <th>Source</th> 
                        <th>Network</th>  
                        <th>Time (<%= timezoneFormatter.format(new Date()) %> Time Zone)</th>
                        <th>Group Message Id</th>
                    </tr>
                </thead>   
                <tbody>
                    <%                       
                        
                        if (outgoingList != null) {
                            for (OutgoingGrouplog code : outgoingList) {                                
                    %>
                    
                                <tr>
                                    <td width="2%"><%=ussdCount%></td>
                                    <td class="center">
                                           <a class ="msglink" href="#"  data-toggle="modal" data-target="#groupcheck">
                                                 <%= groupHash.get(code.getDestination()) %>
                                             </a>
                                     </td>
                                    <td class="center"><%= code.getMessage() %></td>
                                    <td class="center" width="4%"><%= code.getOrigin() %> </td> 
                                    <td class="center"><%= networkHash.get(code.getNetworkUuid()) %></td> 
                                    <td class="center"><%= dateFormatter.format(code.getLogTime()) %> </td>
                                    <td class="center" name="<%= code.getUuid() %>"><%= code.getUuid() %> </td>
                                </tr>

                    <%
                                ussdCount++;
                            }// end 'for (OutgoingGrouplog code : outgoingList)'
                        }// end 'if (outgoingList != null)'
                    %>
                </tbody>
            </table>            
        </div>
        <div class="clear"></div>

        <div class="ussdNavControls" style="margin-top: 1%;width:98%;margin-left: 9px;">
           <div id="pagination">
                <form name="pageForm" method="post" action="sentgroup.jsp">                                
                    <%                                            
                        if (!sgPage.isFirstPage()) {
                    %>
                        <input class="toolbarBtn" type="submit" name="page" value="First" />
                        <input class="toolbarBtn" type="submit" name="page" value="Previous" />
                    <%
                        }
                    %>
                    <span class="pageInfo">Page 
                        <span class="pagePosition currentPage"><%= sgPage.getPageNumber()%></span> of 
                        <span class="pagePosition"><%= sgPage.getTotalSize()%></span>
                    </span>   
                    <%
                        if (!sgPage.isLastPage()) {                        
                    %>
                        <input class="toolbarBtn" type="submit" name="page" value="Next">  
                        <input class="toolbarBtn" type="submit" name="page" value="Last">
                    <%
                        }
                    %>                                
                </form>
            </div>

            
        </div>

        <div class="clear"></div>

    </div><!--/span-->

</div><!--/row-->

<div class="modal fade" id="groupcheck" tabindex="-1" role="dialog" arialabelled="exampleModalLabeled" aria-hidden="true">
 
                                            <div class="modal-content1">
                                            <a id="modal-display1">Please Wait .....</a>                                     
                                          <table id="scroll21">
                                          <tr ><td width=50%>Message Status</td><td width=50%>Count</td></tr> 
                                                </table>
                                             </div>
                                     <div class="modal-dialog">
                                                                       
                                      </div>
                                   
                                </div>
<script type="text/javascript" src="../js/tawi/getSentMessages.js"></script>

<jsp:include page="footer.jsp" />
