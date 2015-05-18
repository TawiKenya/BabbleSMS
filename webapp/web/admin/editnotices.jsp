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
            <a href="#">Edit Notice</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        <div class="box-header well" data-original-title>
                            
            <a class="btn" href="addnotice.jsp" title="add notice" data-rel="tooltip">Add Notice</a>  
            <div class="box-icon">
                <a class="btn" href="#" title="refresh page" data-rel="tooltip"><i class="icon-refresh"></i> </a>                  
                <a class="btn" href="#" title="delete message" data-rel="tooltip"><i class="icon-trash"></i></a> 
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">

            <form class="form-horizontal" method="POST" action="../editnotification">
                <fieldset>

                    <div class="control-group">
                        <label class="control-label" for="shortdesc">Short Description</label>
                        <div class="controls">
                            <textarea rows="4" collumn="10" name="shortdesc"><%=request.getParameter("shortdesc")%></textarea>
                        </div>
                    </div>
                        
                    <div class="control-group">
                        <label class="control-label" for="longdesc">Long Description</label>
                        <div class="controls">
                            <textarea rows="4" collumn="10" name="longdesc"><%=request.getParameter("longdesc")%></textarea>
                        </div>
                    </div>    
                    
                                      
                    <div class="control-group">
                        <label class="control-label" for="published">Published</label>
                        <div class="controls">
                            <select name="publish" required="true">
                                <%
                            if(request.getParameter("publish").equals("yes")){%>
                            <option value="<%=request.getParameter("publish")%>"><%=request.getParameter("publish")%></option>
                            <option value="no">no</option>
                            <%
                            }
                            else{
                                %>
                            <option value="<%=request.getParameter("publish")%>"><%=request.getParameter("publish")%></option>
                            <option value="yes">yes</option>
                                
                          <%  }  %>
                            </select>
                        </div>
                    </div>   
                            
                    <div class="control-group">
                        <label class="control-label" for="origin">Origin</label>
                        <div class="controls">
                            <select name="origin" required="true">
                                <%
                            if(request.getParameter("origin").equals("admin")){%>
                            <option value="<%=request.getParameter("origin")%>"><%=request.getParameter("origin")%></option>
                            <option value="system">System</option>
                            <%
                            }
                            else{
                                %>
                            <option value="<%=request.getParameter("origin")%>"><%=request.getParameter("origin")%></option>
                            <option value="admin">Admin</option>
                                
                          <%  }  %>
                            </select>
                        </div>
                    </div>                     

                    <div class="form-actions">
                        <input type="hidden" name="notifuuid" value="<%=request.getParameter("notifuuid")%>">
                        <button type="submit" class="btn btn-primary">Save changes</button>
                        <a href="source.jsp"><button class="btn">Cancel</button></a>
                    </div>

                </fieldset>
            </form>
        </div>
    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
