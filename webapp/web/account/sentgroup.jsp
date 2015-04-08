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
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Group"%>
<%@page import="ke.co.tawi.babblesms.server.beans.log.OutgoingGrouplog"%>
<%@page import="ke.co.tawi.babblesms.server.beans.messagetemplate.MsgStatus"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.logs.OutgoingLogDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.status.MessageStatusDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.logs.OutgoingGroupLogDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>

<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>

<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>
<%@page import="net.sf.ehcache.Element"%>

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
    OutgoingGroupLogDAO outgoinglogDAO = OutgoingGroupLogDAO.getInstance();
    List<OutgoingGrouplog> outgoingList = outgoinglogDAO.getOutgoingGrouplogByAccount(accountuuid);

    CacheManager mgr = CacheManager.getInstance();
    Cache groupsCache = mgr.getCache(CacheVariables.CACHE_GROUP_BY_UUID);
    Cache msgstatusCache = mgr.getCache(CacheVariables.CACHE_MESSAGE_STATUS_BY_UUID);

    HashMap<String, String> contactgrpHash = new HashMap<String, String>();
    HashMap<String, String> messageHash = new HashMap<String, String>();

    Element element;
    List keys;
    Group group;
    MsgStatus msgt;

    /*keys = groupsCache.getKeys();
    for (Object key : keys) {
        element = groupsCache.get(key);
        System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwww");
        cntgrp = (Group) element.getObjectValue();
         System.out.println("wwwwwwwwwwwwwwwwwwwwwwwwwwwwww2");
        contactgrpHash.put(cntgrp.getUuid(), cntgrp.getName());
    }
*/
    keys = msgstatusCache.getKeys();
    for (Object key : keys) {
        element = msgstatusCache.get(key);
        msgt = (MsgStatus) element.getObjectValue();
        messageHash.put(msgt.getUuid(), msgt.getDescription());
    }

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
                        <th>GroupSentId</th>
                        <th>message</th>
                        <th>source</th>
                        <th>Group</th>
                        <th>time</th>
                    </tr>
                </thead>   
                <tbody>
                    <%                        int count = 1;
                        if (outgoingList != null) {
                            for (OutgoingGrouplog code : outgoingList) {
                                String groupname = contactgrpHash.get(code.getDestination());
                           GroupDAO grpDAO = GroupDAO.getInstance();
                           group = grpDAO.getGroup(code.getDestination());
                         
     
                    %>
                    <tr>

                        <td width="10%"><%=count%></td>
                        <td class="center"><%=code.getUuid()%> </td>
                        <td class="center"><%=code.getMessage()%></td>
                        <td class="center"><%=code.getOrigin()%> </td>
                        <td class="center"><%=group.getName()%> </td>
                        <td class="center"><%=code.getLogTime()%> </td>

                    </tr>

                    <%
                                count++;
                            }
                        }
                    %>
                </tbody>
            </table>            
        </div>
    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
