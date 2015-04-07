<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="java.util.List"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
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
    response.setHeader("Refresh", SessionConstants.SESSION_TIMEOUT + "; url=../Logout");

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
            <a href="#">Add Notice</a>
        </li>
    </ul>
</div>




<div class="row-fluid sortable">
    <div class="box span12">
        <%             String addErrStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_SMSMASK_ERROR_KEY);
           
             String addErrStr2 = (String) session.getAttribute(SessionConstants.ADMIN_ADD_ERROR);
                String addSuccessStr2 = (String) session.getAttribute(SessionConstants.ADMIN_ADD_SUCCESS);
            HashMap<String, String> paramHash = (HashMap<String, String>) session.getAttribute(
                    SessionConstants.ADMIN_ADD_SMSMASK_PARAMETERS);

            if (paramHash == null) {
                paramHash = new HashMap<String, String>();
            }

            if (StringUtils.isNotEmpty(addErrStr)) {
                out.println("<p class=\"error\">");
                out.println("Form error: " + addErrStr);
                out.println("</p>");
                session.setAttribute(SessionConstants.ADMIN_ADD_SMSMASK_ERROR_KEY, null);
            }

           
            if (StringUtils.isNotEmpty(addErrStr2)) {
                    out.println("<p style='color:red;'>");
                    out.println("Form error: " + addErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADMIN_ADD_ERROR, null);
                }

                if(StringUtils.isNotEmpty(addSuccessStr2)) {
                    out.println("<p style='color:green;'>");
                    out.println(addSuccessStr2);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADMIN_ADD_SUCCESS, null);
                }
                
        %>
        <div class="box-header well" data-original-title>

            <h2><i class="icon-edit"></i> add notice</h2>            
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">
            <form class="form-horizontal" action="../addnotification" method="POST">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="origin">Origin*</label>
                        <div class="controls">
                            <select name="origin" required="true">
                                <option value="admin">Admin</option> 
                                <option value="system">System</option>
                            </select>

                        </div>

                    </div>  

                    
                    <div class="control-group">
                        <label class="control-label" for="shortdesc">Short Description*</label>
                        <div class="controls">
                            <textarea name="shortdesc" rows="4" cols="50" required="true"></textarea>
                        </div>
                    </div>  
                            
                    <div class="control-group">
                        <label class="control-label" for="longdesc">Long Description*</label>
                        <div class="controls">
                            <textarea name="longdesc" rows="4" cols="50" required="true"></textarea>
                        </div>
                    </div>    
                            
                    <div class="form-actions">

                        <button type="submit" name="sendsms" value="Send" class="btn btn-primary">Add</button>
                    </div>
                </fieldset>
            </form>


        </div>
    </div><!--/span-->

</div><!--/row-->



<jsp:include page="footer.jsp" />