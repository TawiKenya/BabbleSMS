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

<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>



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
    response.setHeader("Refresh", SessionConstants.SESSION_TIMEOUT + "; url=adminLogout");

    CacheManager mgr = CacheManager.getInstance();
    Cache accountCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID);
    Cache networkCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);

    Element element;
    Account account;
    Network network;

    List<Account> userList = new ArrayList();
    List<Network> networkList = new ArrayList();

    List keys;

    keys = accountCache.getKeys();
    for (Object key : keys) {
        element = accountCache.get(key);
        account = (Account) element.getObjectValue();
        userList.add(account);
    }

    keys = networkCache.getKeys();
    for (Object key : keys) {
        element = networkCache.get(key);
        network = (Network) element.getObjectValue();
        networkList.add(network);
    }

%> 
<jsp:include page="header.jsp" />

<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Add Account</a>
        </li>
    </ul>
</div>




<div class="row-fluid sortable">
    <div class="box span12">
        <%             String addErrStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY);
            String addSuccessStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_SUCCESS_KEY);
            HashMap<String, String> paramHash = (HashMap<String, String>) session.getAttribute(
                    SessionConstants.ADMIN_ADD_ACCOUNT_PARAMETERS);

            if (paramHash == null) {
                paramHash = new HashMap<String, String>();
            }

            if (StringUtils.isNotEmpty(addErrStr)) {
                out.println("<p class=\"error\">");
                out.println("Form error: " + addErrStr);
                out.println("</p>");
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, null);
            }

            if (StringUtils.isNotEmpty(addSuccessStr)) {
                out.println("<p class=\"success\">");
                out.println("You have successfully added a new account.");
                out.println("</p>");
                session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_SUCCESS_KEY, null);

            }
        %>



        <div class="box-header well" data-original-title>

            <h2><i class="icon-edit"></i>Add Account</h2>            
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">
            <form  class="form-horizontal"   action="addAccount" method="POST" >
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="name">Name*</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="receiver" type="text" name="name"  value="<%= StringUtils.trimToEmpty(paramHash.get("name")) %>" required="true">

                        </div>

                    </div>  

                    <div class="control-group">
                        <label class="control-label" for="name">Username*</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="receiver" type="text" name="username" required="true"
                                   value="<%= StringUtils.trimToEmpty(paramHash.get("username")) %>" >                                    
                        </div>

                    </div> 

                    <div class="control-group">
                        <label class="control-label" for="name">Password*</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="receiver" type="password" name="password" value="" required="true">

                        </div>

                    </div> 

                    <div class="control-group">
                        <label class="control-label" for="name">Repeat Password*</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="receiver" type="password" name="password2" value="" required="true">

                        </div>

                    </div> 


                    <div class="control-group">
                        <label class="control-label" for="name">Phone Number</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="mobile" type="text" name="phone"   value="<%= StringUtils.trimToEmpty(paramHash.get("phone")) %>">

                        </div>

                    </div> 

                    <div class="control-group">
                        <label class="control-label" for="name">Email*</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="receiver" type="email" name="email"value="<%= StringUtils.trimToEmpty(paramHash.get("email")) %>"  required="true>

                        </div>

                    </div> 


                    <div class="form-actions">

                        <button type="submit" name="sendsms" value="Send"   class="btn btn-primary">Add</button>
                    </div>
                </fieldset>
            </form>


        </div>
    </div><!--/span-->

</div><!--/row-->



<jsp:include page="footer.jsp" />

