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
   
%> 
<jsp:include page="header.jsp" />


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Edit Network</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        <div class="box-header well" data-original-title>
            <a class="btn" href="network.jsp" title="view networks" data-rel="tooltip">View</a>                  
            <a class="btn" href="addnetwork.jsp" title="add network" data-rel="tooltip">Add</a>  
            <div class="box-icon">
                <a class="btn" href="#" title="refresh page" data-rel="tooltip"><i class="icon-refresh"></i> </a>                  
                <a class="btn" href="#" title="delete message" data-rel="tooltip"><i class="icon-trash"></i></a> 
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">

            <form class="form-horizontal" method="POST" action="../editnetwork">
                <fieldset>

                    <div class="control-group">
                        <label class="control-label" for="network">Network Name</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="networkname" name="networkname" type="text" value="<%=request.getParameter("networkname")%>">
                        </div>
                    </div>
                    

                    <div class="form-actions">
                        <input type="hidden" name="networkuuid" value="<%=request.getParameter("networkuuid")%>">
                        <button type="submit" class="btn btn-primary">Save changes</button>
                        <a href="network.jsp"><button class="btn">Cancel</button></a>
                    </div>

                </fieldset>
            </form>
        </div>
    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
