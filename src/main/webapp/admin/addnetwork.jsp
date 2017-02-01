<%@page import="java.util.List"%>
<%@page import="ke.co.tawi.babblesms.server.beans.network.Country"%>
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
    Cache countryCache = mgr.getCache(CacheVariables.CACHE_COUNTRY_BY_UUID);
    
    Element element;
    Country country;
    
    List<Country> countryList = new ArrayList();
    
    List keys;
    
    keys = countryCache.getKeys();
    for (Object key : keys) {
        element = countryCache.get(key);
        country = (Country) element.getObjectValue();
        countryList.add(country);
    }



%> 
<jsp:include page="header.jsp" />

<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Add Network</a>
        </li>
    </ul>
</div>




<div class="row-fluid sortable">
    <div class="box span12">
        <%             String addErrStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_NETWORK_ERROR_KEY);
            String addSuccessStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_NETWORK_SUCCESS_KEY);
            HashMap<String, String> paramHash = (HashMap<String, String>) session.getAttribute(
                    SessionConstants.ADMIN_ADD_NETWORK_PARAMETERS);

            if (paramHash == null) {
                paramHash = new HashMap<String, String>();
            }

            if (StringUtils.isNotEmpty(addErrStr)) {
                out.println("<p class=\"error\">");
                out.println("Form error: " + addErrStr);
                out.println("</p>");
                session.setAttribute(SessionConstants.ADMIN_ADD_NETWORK_ERROR_KEY, null);
            }

            if (StringUtils.isNotEmpty(addSuccessStr)) {
                out.println("<p class=\"success\">");
                out.println("You have successfully added a new network.");
                out.println("</p>");
                session.setAttribute(SessionConstants.ADMIN_ADD_NETWORK_SUCCESS_KEY, null);

            }
        %>
        <div class="box-header well" data-original-title>

            <h2><i class="icon-edit"></i> add network</h2>            
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">
            <form class="form-horizontal" action="../addnetwork" method="POST">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="network">Network*</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="receiver" type="text" name="network" value="" required="true">

                        </div>

                    </div>   
                    
                     <div class="control-group">
                        <label class="control-label" for="country">Country*</label>
                        <div class="controls">
                            <select name="countryuuid" required="true">
                                <option value="">Please select one</option> 
                                <%
                                    int count = 1;
                                    if (countryList != null) {
                                        for (Country code : countryList) {
                                %>
                                <option value="<%= code.getUuid()%>"><%= code.getName()%></option>
                                <%
                                            count++;
                                        }
                                    }
                                %>
                            </select>
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