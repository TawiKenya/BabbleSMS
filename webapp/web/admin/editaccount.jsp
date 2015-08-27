<%
    /**
    Copyright 2015 Tawi Commercial Services Ltd

    Licensed under the Open Software License, Version 3.0 (the ?License?); you may 
    not use this file except in compliance with the License. You may obtain a copy 
    of the License at:
    http://opensource.org/licenses/OSL-3.0

    Unless required by applicable law or agreed to in writing, software distributed 
    under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
    CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and limitations 
    under the License.
    */
%>
<%@page import="ke.co.tawi.babblesms.server.beans.status.Status"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="net.sf.ehcache.CacheManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="java.util.List"%>

<%
    // The following is for session management.    
    if (session == null) {
        response.sendRedirect("index.jsp");
    }

    String username = (String) session.getAttribute(SessionConstants.ADMIN_SESSION_KEY);
    if (StringUtils.isEmpty(username)) {
        response.sendRedirect("index.jsp");
    }

    session.setMaxInactiveInterval(SessionConstants.SESSION_TIMEOUT);
    response.setHeader("Refresh", SessionConstants.SESSION_TIMEOUT + "; url=../Logout");

    //String accountuuid = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_ACCOUNTUUID);
    CacheManager mgr = CacheManager.getInstance();
    Cache statusCache = mgr.getCache(CacheVariables.CACHE_STATUS_BY_UUID);
    
    

    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, Status> statusHash = new HashMap();
    Element element;
    Network network;
    Status status;

    List<Status> list = new ArrayList();

    List keys;

    keys = statusCache.getKeys();
    for (Object key : keys) {
        element = statusCache.get(key);
        status = (Status) element.getObjectValue();
        list.add(status);
    }

 keys = statusCache.getKeys();
    for (Object key : keys) {
        element = statusCache.get(key);
        status = (Status) element.getObjectValue();
        statusHash.put(status.getUuid(), status);
    }
%> 
<jsp:include page="header.jsp" />


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Edit Account</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        <div class="box-header well" data-original-title>               
            <a class="btn" href="addaccount.jsp" title="add account" data-rel="tooltip">Add</a>  
            <div class="box-icon">
                <a class="btn" href="#" title="refresh page" data-rel="tooltip"><i class="icon-refresh"></i> </a>                
                <a class="btn" href="#" title="delete message" data-rel="tooltip"><i class="icon-trash"></i></a> 
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">

            <form class="form-horizontal" action="../editAccount" method="POST"  >
                <fieldset>

                    <div class="control-group">
                        <label class="control-label" for="network">Username</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="networkname" name="username" type="text" value="<%=request.getParameter("username")%>">
                        </div>
                    </div>
                        
                         <div class="control-group">
                        <label class="control-label" for="network">Names</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="networkname" name="name" type="text" value="<%=request.getParameter("name")%>">
                        </div>
                    </div>
                    
                    <div class="control-group">
                        <label class="control-label" for="network">Phone Number</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="networkname" name="mobile" type="text" value="<%=request.getParameter("mobile")%>">
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="network">Email</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="networkname" name="email" type="text" value="<%=request.getParameter("email")%>">
                        </div>
                    </div>

                   
                    <div class="form-actions">
                        <input type="hidden" name="accountuuid" value="<%=request.getParameter("accountuuid")%>">
                        <button type="submit" class="btn btn-primary">Save changes</button>
                        <a href="accounts.jsp"><button class="btn">Cancel</button></a>
                    </div>

                </fieldset>
            </form>

        </div>



<div class="box-content">

    
             <p> 
            <a href="#">Edit Log Password</a>               
            </p>
           <form  class="form-horizontal" action="../resetPassword" method="POST">
              <fieldset>
                  
                     <div class="control-group">
                        <label class="control-label" for="network">Log Password</label>
                        <div class="controls">
                         <input class="input-xlarge focused"  id="networkname" name="loginPasswd" type="password" value="">
                        </div>
                    </div>


                    <div class="control-group">
                        <label class="control-label" for="network">Cornfirm Log Password</label>
                        <div class="controls">
                     <input class="input-xlarge focused"  id="networkname" name="loginPasswd2" type="password" value="">
                        </div>
                    </div>



                    <div class="form-actions">
                        <input type="hidden" name="accountuuid" value="<%=request.getParameter("accountuuid")%>">
                        <button type="submit" class="btn btn-primary">Edit</button>
                        <a href="accounts.jsp"><button class="btn">Cancel</button></a>
                    </div>
                       </fieldset>
            </form>
                    </div>
                  <br><br>
       <div class="box-content">

    
             <p> 
            <a href="#">Edit TawiSMSGateway </a>               
            </p>
           <form  class="form-horizontal" action="../resetNotificationUrl" method="POST">
              <fieldset>
                     




                    <div class="control-group">
                        <label class="control-label" for="network">username</label>
                        <div class="controls">
                     <input class="input-xlarge focused"  id="networkname" name="username" 
                     type="text" value="<%=request.getParameter("username")%>" readonly> 
                        </div>
                    </div>
                    <%
                    
                            if(!StringUtils.isEmpty(request.getParameter("url"))){       
                              String url = StringUtils.trimToEmpty(request.getParameter("url"));               
                       %>
                     <div class="control-group">
                        <label class="control-label" for="network">Notification Url</label>
                        <div class="controls">
                         <input class="input-xlarge focused"  id="networkname" name="url" 
                         type="text" value="<%=url%>">
                        </div>
                    </div>
                      <%
                      }
                      %>


                    <div class="form-actions">
                        <input type="hidden" name="accountuuid" value="<%=request.getParameter("accountuuid")%>">
                        <button type="submit" class="btn btn-primary">Edit</button>
                        <a href="accounts.jsp"><button class="btn">Cancel</button></a>
                    </div>
                       </fieldset>
            </form>
                    </div>



    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
