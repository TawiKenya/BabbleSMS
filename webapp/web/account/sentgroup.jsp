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

    OutgoingGroupLogDAO outgoinglogDAO = OutgoingGroupLogDAO.getInstance();
    

    GroupDAO groupDAO = GroupDAO.getInstance();
    
    Account account = new Account();
    Element element;
    if ((element = accountsCache.get(username)) != null) {
        account = (Account) element.getObjectValue();
    }

    List<OutgoingGrouplog> outgoingList = outgoinglogDAO.getOutgoingGrouplogByAccount(account.getUuid());

    List<Group> groupList = groupDAO.getGroups(account);

    HashMap<String, String> groupHash = new HashMap<String, String>();

    for(Group group : groupList) {
        groupHash.put(group.getUuid(), group.getName());
    }

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
        <div class="box-header well" data-original-title>
            <a class="btn" href="#" title="refresh page" data-rel="tooltip"><i class="icon-refresh"></i> Refresh</a>                  
            <a class="btn" href="#" title="delete message" data-rel="tooltip"><i class="icon-trash"></i> Delete</a>  
           <!-- <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>-->
        </div>
        <div class="box-content">
            <table class="table table-striped table-bordered bootstrap-datatable datatable">
                <thead>
                    <tr>

                        <th>*</th>  
                        <th>Group</th>
                        <th>Message</th>
                        <th>Source</th>                        
                        <th>Time (<%= timezoneFormatter.format(new Date()) %> Time Zone)</th>
                        <th>Group Message Id</th>
                    </tr>
                </thead>   
                <tbody>
                    <%                       
                        int count = 1;

                        if (outgoingList != null) {
                            for (OutgoingGrouplog code : outgoingList) {                                
                    %>
                    
                                <tr>
                                    <td width="10%"><%=count%></td>
                                    <td class="center"><%= groupHash.get(code.getDestination()) %> </td>
                                    <td class="center"><%= code.getMessage() %></td>
                                    <td class="center"><%= code.getOrigin() %> </td>                        
                                    <td class="center"><%= dateFormatter.format(code.getLogTime()) %> </td>
                                    <td class="center"><%= code.getUuid() %> </td>
                                </tr>

                    <%
                                count++;
                            }// end 'for (OutgoingGrouplog code : outgoingList)'
                        }// end 'if (outgoingList != null)'
                    %>
                </tbody>
            </table>            
        </div>
    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
