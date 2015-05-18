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
    Cache networkCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
    Cache accountCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID);
    
    

    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, Network> networkHash = new HashMap();
    HashMap<String, Account> accountHash = new HashMap();
    
    Element element;
    Network network;
    Account account;

    List<Network> list = new ArrayList();
    List<Account> list2=new ArrayList();

    List keys;

    keys = networkCache.getKeys();
    for (Object key : keys) {
        element = networkCache.get(key);
        network = (Network) element.getObjectValue();
        list.add(network);
    }

 keys = networkCache.getKeys();
    for (Object key : keys) {
        element = networkCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash.put(network.getUuid(), network);
    }
    
    keys = accountCache.getKeys();
    for (Object key : keys) {
        element = accountCache.get(key);
        account = (Account) element.getObjectValue();
        list2.add(account);
    }

 keys = accountCache.getKeys();
    for (Object key : keys) {
        element = accountCache.get(key);
        account = (Account) element.getObjectValue();
        accountHash.put(account.getUuid(), account);
    }
    
    
%> 
<jsp:include page="header.jsp" />

<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Edit Source</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        <div class="box-header well" data-original-title>
            <a class="btn" href="source.jsp" title="view sources" data-rel="tooltip">View</a>                  
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

            <form class="form-horizontal" method="POST" action="../editsource">
                <fieldset>

                    <div class="control-group">
                        <label class="control-label" for="network">Source</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="source" name="source" type="text" value="<%=request.getParameter("source")%>">
                        </div>
                    </div>
                    
                    <div class="control-group">
                        <label class="control-label" for="Network">Network</label>
                        <div class="controls">
                           <select id="selectError3" data-rel="chosenn" name="networkuuid">
                               <option value="<%=request.getParameter("networkuuid")%>"><%=networkHash.get(request.getParameter("networkuuid")).getName()%></option>
                                <% 
                                    
                                    int count = 1;
                                    for (Network code : list) {
                                     if(!code.getUuid().equals(request.getParameter("networkuuid"))){    
                                %>
                                <option value="<%=code.getUuid()%>"><%=code.getName()%></option>
                                <%  
                                     }
                                        count++;
                                    }
                                %>   

                            </select>
                        </div>
                    </div>

                    
                   <div class="control-group">
                        <label class="control-label" for="Network">Owner</label>
                        <div class="controls">
                           <select id="selectError3" data-rel="chosenn" name="accuuid">
                               <option value="<%=request.getParameter("accuuid")%>"><%=accountHash.get(request.getParameter("accuuid")).getUsername()%></option>
                                <% 
                                    
                                    count = 1;
                                    for (Account code : list2) {
                                     if(!code.getUuid().equals(request.getParameter("accuuid"))){    
                                %>
                                <option value="<%=code.getUuid()%>"><%=code.getUsername()%></option>
                                <%  
                                     }
                                        count++;
                                    }
                                %>   

                            </select>
                        </div>
                    </div>             


                    <div class="form-actions">
                        <input type="hidden" name="sourceuuid" value="<%=request.getParameter("sourceuuid")%>">
                        <button type="submit" class="btn btn-primary">Save changes</button>
                        <a href="source.jsp"><button class="btn">Cancel</button></a>
                    </div>

                </fieldset>
            </form>
        </div>
    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
