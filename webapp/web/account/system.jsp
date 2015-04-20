
<%@page import="ke.co.tawi.babblesms.server.persistence.notification.NotificationStatusDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.notification.Notification"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.notification.NotificationDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="net.sf.ehcache.CacheManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="java.util.List"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>

<%
    // The following is for session management.    
    if (session == null) {
        response.sendRedirect("../index.jsp");
    }

    String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
    if (StringUtils.isEmpty(username)) {
        response.sendRedirect("../index.jsp");
    }

    session.setMaxInactiveInterval(SessionConstants.SESSION_TIMEOUT);
    response.setHeader("Refresh", SessionConstants.SESSION_TIMEOUT + "; url=../logout");

    String accountuuid = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_ACCOUNTUUID);

    NotificationDAO ntsDAO = NotificationDAO.getInstance();
    List<Notification> ntlist = new ArrayList();
    ntlist = ntsDAO.getNotificationSystem(accountuuid);

    NotificationStatusDAO nstatusDAO = NotificationStatusDAO.getInstance();
%> 
<jsp:include page="reportheader.jsp" />


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">View Notices</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        <div class="box-header well" data-original-title>
            <a class="btn" href="#" title="refresh page" data-rel="tooltip"><i class="icon-refresh"></i> Refresh</a>                  
            <a class="btn" href="#" title="delete message" data-rel="tooltip"><i class="icon-trash"></i> Delete</a>  
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">
            <div class="content_title">
                <h3>Administrator Notices</h3>
                <p>Below are notices form the System Administrator:</p>
            </div>

            <table class="table table-striped table-bordered bootstrap-datatable datatable">
                <thead>
                    <tr>
                        <th>*</th>

                        <th>Short Description</th>
                        <th>Long Description</th>
                        <th>Notification Date</th>
                        <th>actions</th>
                    </tr>
                </thead>   
                <tbody>
                    <%                        int count = 1;
                        if (ntlist != null) {
                            for (Notification code : ntlist) {
                    %>
                    <tr>

                        <td width="10%"><%=count%></td>
                        <td class="center"><%=code.getShortDesc()%></td>
                        <td class="center"><%=code.getLongDesc()%></td>
                        <td class="center"><%=code.getNotificationDate()%></td>
                        <td class="center">
                            <a class="btn btn-success" href="#">
                                <i class="icon-zoom-in icon-white"></i>  
                                View                                            
                            </a>


                        </td>


                    </tr>

                    <%
                                count++;
                            }
                        }
                    %>
                </tbody>
            </table>            
        </div>
    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
