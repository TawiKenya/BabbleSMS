<%-- 
    Document   : ajaxcredit
    Created on : Sep 6, 2014, 7:02:01 PM
    Author     : josephk
--%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="net.sf.ehcache.CacheManager"%>



<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Shortcode"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Mask"%>
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

    String accountuuid = request.getParameter("accountuuid");
    CacheManager mgr = CacheManager.getInstance();
    Cache shortcodesCache = mgr.getCache(CacheVariables.CACHE_SHORTCODE_BY_UUID);
    Cache maskCache = mgr.getCache(CacheVariables.CACHE_MASK_BY_UUID);
    
    Element element;
    Shortcode shortcode;
    Mask mask;
   

    List<Shortcode> shortcodelist = new ArrayList();
    List<Mask> masklist = new ArrayList();

    List keys;

    keys = shortcodesCache.getKeys();
    for (Object key : keys) {
        element = shortcodesCache.get(key);
        shortcode = (Shortcode) element.getObjectValue();
        if (accountuuid.equals(shortcode.getAccountuuid())) {
            shortcodelist.add(shortcode);
        }
    }

    keys = maskCache.getKeys();
    for (Object key : keys) {
        element = maskCache.get(key);
        mask = (Mask) element.getObjectValue();
        if (accountuuid.equals(mask.getAccountuuid())) {
            masklist.add(mask);
        }
    }
%>
<%
    //for mask
    int count = 1;
    if (masklist != null) {
        for (Mask code : masklist) {
%>

<option value="<%= code.getMaskname()%>"><%= code.getMaskname()%></option>
<%
            count++;
        }

    }
    //for shortcode
    count = 1;
    if (shortcodelist != null) {
        for (Shortcode code : shortcodelist) {
%>
<option value="<%= code.getCodenumber()%>"><%= code.getCodenumber()%></option>
<%
            count++;
        }

    }


%> 
