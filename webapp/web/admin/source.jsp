<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="net.sf.ehcache.CacheManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Shortcode"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Mask"%>
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="java.util.List"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.maskcode.ShortcodeDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.maskcode.MaskDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO"%>
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
    Cache accountCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID);

    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, String> networkHash = new HashMap();
    HashMap<String, String> accountHash = new HashMap();

    Element element;
    Shortcode shortcode;
    Mask mask;
    Network network;
    Account account;

    List<Shortcode> shortcodelist = new ArrayList();
    List<Mask> masklist = new ArrayList();

    List keys;

    keys = shortcodesCache.getKeys();
    for (Object key : keys) {
        element = shortcodesCache.get(key);
        shortcode = (Shortcode) element.getObjectValue();
        shortcodelist.add(shortcode);
    }

    keys = maskCache.getKeys();
    for (Object key : keys) {
        element = maskCache.get(key);
        mask = (Mask) element.getObjectValue();
        masklist.add(mask);
    }

    keys = accountCache.getKeys();
    for (Object key : keys) {
        element = accountCache.get(key);
        account = (Account) element.getObjectValue();
        accountHash.put(account.getUuid(), account.getName());

    }

    keys = networksCache.getKeys();
    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash.put(network.getUuid(), network.getName());

    }


%> 


<jsp:include page="header.jsp" />


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Source</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        <div class="box-header well" data-original-title>
                         
            <a class="btn" href="addmask.jsp" title="add mask" data-rel="tooltip">Add Mask</a>
            <a class="btn" href="addshortcode.jsp" title="add shortcode" data-rel="tooltip">Add Shortcode</a>  
            <div class="box-icon">
                <a class="btn" href="#" title="refresh page" data-rel="tooltip"><i class="icon-refresh"></i> </a>                  
                <a class="btn" href="#" title="delete message" data-rel="tooltip"><i class="icon-trash"></i></a> 
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">
              <%                String addErrStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_ERROR);
                String addSuccessStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_SUCCESS);
                
                String deleteErrStr = (String) session.getAttribute(SessionConstants.ADMIN_DELETE_ERROR);
                String deleteSuccessStr = (String) session.getAttribute(SessionConstants.ADMIN_DELETE_SUCCESS);
                
                String updateErrStr = (String) session.getAttribute(SessionConstants.ADMIN_UPDATE_ERROR);
                String updateSuccessStr = (String) session.getAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS);

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
                        <th>Source</th>
                        <th>Network</th>
                         <th>Owner</th>
                        <th>actions</th>
                    </tr>
                </thead>   
                <tbody>
                    <%    int count = 1;

                        for (Shortcode code : shortcodelist) {
                    %>
                    <tr>
                        <td width="10%"><%=count%></td>                     	
                         <td class="center"><%=code.getCodenumber()%></td>	
                          <td class="center"><%=networkHash.get(code.getNetworkuuid())%></td>	
                          <td class="center"><%=accountHash.get(code.getAccountuuid())%></td>			
                        <td class="center">
                            <form name="edit" method="post" action="editsource.jsp"> 
                                <input type="hidden" name="accuuid" value="<%=code.getAccountuuid()%>">
                                <input type="hidden" name="source" value="<%=code.getCodenumber()%>">
                                <input type="hidden" name="networkuuid" value="<%=code.getNetworkuuid()%>">
                                <input type="hidden" name="sourceuuid" value="<%=code.getUuid()%>">
                                <input class="btn btn-success" type="submit" name="editsource" id="submit" value="Edit" /> 
                            </form>
                            <form name="delete" method="post" action="../deletesource">
                                <input type="hidden" name="sourceuuid" value="<%=code.getUuid()%>">
                                <input class="btn btn-success" type="submit" name="deletesource" id="submit" value="Delete" /> 
                            </form>
                        </td>



                    </tr>

                    <%
                            count++;
                        }
                    %>

                    <%
                        for (Mask code : masklist) {
                    %>
                    <tr>
                        <td width="10%"><%=count%></td>
                        <td class="center"><%=code.getMaskname()%></td>
                        <td class="center"><%=networkHash.get(code.getNetworkuuid())%></td>		
                         <td class="center"><%=accountHash.get(code.getAccountuuid())%></td>				
                        <td class="center">
                            <form name="edit" method="post" action="editsource.jsp"> 
                                <input type="hidden" name="accuuid" value="<%=code.getAccountuuid()%>">
                                <input type="hidden" name="source" value="<%=code.getMaskname()%>">
                                <input type="hidden" name="networkuuid" value="<%=code.getNetworkuuid()%>">
                                <input type="hidden" name="sourceuuid" value="<%=code.getUuid()%>">
                                <input class="btn btn-success" type="submit" name="editsource" id="submit" value="Edit" /> 
                            </form>
                            <form name="delete" method="post" action="../deletesource">
                                <input type="hidden" name="sourceuuid" value="<%=code.getUuid()%>">
                                <input class="btn btn-success" type="submit" name="deletesource" id="submit" value="Delete" /> 
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
