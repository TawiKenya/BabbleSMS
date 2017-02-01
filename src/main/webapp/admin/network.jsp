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
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>

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
    Cache shortcodesCache = mgr.getCache(CacheVariables.CACHE_SHORTCODE_BY_UUID);
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
    Cache maskCache = mgr.getCache(CacheVariables.CACHE_MASK_BY_UUID);

    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, String> networkHash = new HashMap<String, String>();
    Element element;
    Network network;

    List<Network> networklist = new ArrayList();

    List keys;

    keys = networksCache.getKeys();
    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networklist.add(network);
    }


%> 
<jsp:include page="header.jsp" />


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Network</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        
        <div class="box-header well" data-original-title>
            <a class="btn" href="network.jsp" title="view networks" data-rel="tooltip">View</a>                  
            <a class="btn" href="addnetwork.jsp" title="add networks" data-rel="tooltip">Add</a>  
            <div class="box-icon">
                <a class="btn" href="#" title="refresh page" data-rel="tooltip"><i class="icon-refresh"></i> </a>                  
                <a class="btn" href="#" title="delete message" data-rel="tooltip"><i class="icon-trash"></i></a> 
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">
            
             <% 
                  
                String addErrStr2 = (String) session.getAttribute(SessionConstants.ADMIN_ADD_NETWORK_ERROR_KEY);          
                String addErrStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_ERROR);
                String addSuccessStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_SUCCESS);
                
                String deleteErrStr = (String) session.getAttribute(SessionConstants.ADMIN_DELETE_ERROR);
                String deleteSuccessStr = (String) session.getAttribute(SessionConstants.ADMIN_DELETE_SUCCESS);
                
                String updateErrStr = (String) session.getAttribute(SessionConstants.ADMIN_UPDATE_ERROR);
                String updateSuccessStr = (String) session.getAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS);

                
                if (StringUtils.isNotEmpty(addErrStr2)) {
                    out.println("<p style='color:red;'>");
                    out.println("Form error: " + addErrStr2);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADMIN_ADD_NETWORK_ERROR_KEY, null);
                }
                
                if (StringUtils.isNotEmpty(addErrStr)) {
                    out.println("<p style='color:red;'>");
                    out.println("Form error: " + addErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADMIN_ADD_ERROR, null);
                }

                if(StringUtils.isNotEmpty(addSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(addSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADMIN_ADD_SUCCESS, null);
                }
                
                if (StringUtils.isNotEmpty(deleteErrStr)) {
                    out.println("<p style='color:red;'>");
                    out.println("Form error: " + deleteErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADMIN_DELETE_ERROR, null);
                }

                if (StringUtils.isNotEmpty(deleteSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(deleteSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADMIN_DELETE_SUCCESS, null);
                }
                
                if (StringUtils.isNotEmpty(updateErrStr)) {
                    out.println("<p style='color:red;'>");
                    out.println("Form error: " + updateErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR, null);
                }

                if (StringUtils.isNotEmpty(updateSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(updateSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS, null);
                }
            %>
            <table class="table table-striped table-bordered bootstrap-datatable datatable">
                <thead>
                    <tr>
                        <th>*</th>
                        <th>Uuid</th>
                        <th>Network Name</th>		 
                        <th>actions</th>
                    </tr>
                </thead>   
                <tbody>
                    <%                        int count = 1;
                        for (Network code : networklist) {
                    %>
                    <tr>
                        <td width="10%"><%=count%></td>
                        <td class="center"><%=code.getUuid()%></td>
                        <td class="center"><%=code.getName()%></td>

                        <td class="center">
                            <form name="edit" method="post" action="editnetwork.jsp"> 
                                <input type="hidden" name="networkname" value="<%=code.getName()%>">
                                <input type="hidden" name="networkuuid" value="<%=code.getUuid()%>">
                                <input class="btn btn-success" type="submit" name="editnetwork" id="submit" value="Edit" /> 
                            </form>
                            <form name="delete" method="post" action="../deletenetwork">

                                <input type="hidden" name="networkuuid" value="<%=code.getUuid()%>">
                                <input class="btn btn-success" type="submit" name="deletenetwork" id="submit" value="Delete" /> 
                            </form>
                        </td>      

                    </tr>

                    <%
                            count++;
                        }
                    %>
                </tbody>
            </table>            
        </div>
    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
