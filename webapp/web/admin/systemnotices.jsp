<%@page import="ke.co.tawi.babblesms.server.persistence.notification.NotificationStatusDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.notification.Notification"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.notification.NotificationDAO"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="net.sf.ehcache.CacheManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="java.util.List"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.accounts.AccountsDAO"%>

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

    

    NotificationDAO ntsDAO = NotificationDAO.getInstance();
    List<Notification> ntlist = new ArrayList();
    ntlist = ntsDAO.getNotificationbyOrigin("system");

    NotificationStatusDAO nstatusDAO = NotificationStatusDAO.getInstance();
%> 
<jsp:include page="header.jsp" />

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
             
            <a class="btn" href="addnotice.jsp" title="add notice" data-rel="tooltip">Add</a>  
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
               String addErrStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY);
            //String addSuccessStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_SMSSHORTCODE_SUCCESS_KEY);
                String addErrStr2 = (String) session.getAttribute(SessionConstants.ADMIN_ADD_ERROR);
                String addSuccessStr2 = (String) session.getAttribute(SessionConstants.ADMIN_ADD_SUCCESS);
                
                String deleteErrStr = (String) session.getAttribute(SessionConstants.ADMIN_DELETE_ERROR);
                String deleteSuccessStr = (String) session.getAttribute(SessionConstants.ADMIN_DELETE_SUCCESS);
                
                String updateErrStr = (String) session.getAttribute(SessionConstants.ADMIN_UPDATE_ERROR);
                String updateSuccessStr = (String) session.getAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS);

                if (StringUtils.isNotEmpty(addErrStr)) {
                    out.println("<p style='color:red;'>");
                    out.println("Form error: " + addErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADMIN_ADD_ACCOUNT_ERROR_KEY, null);
                }
               
                 if (StringUtils.isNotEmpty(addErrStr2)) {
                    out.println("<p style='color:red;'>");
                    out.println("Form error: " + addErrStr2);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADMIN_ADD_ERROR, null);
                }

                if (StringUtils.isNotEmpty(addSuccessStr2)) {
                    out.println("<p style='color:green;'>");
                    out.println(addSuccessStr2);
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
            <div class="content_title">
                <h3>System Notices</h3>
                
            </div>

            <table class="table table-striped table-bordered bootstrap-datatable datatable">
                <thead>
                    <tr>
                        <th>*</th>

                        <th>Short Description</th>
                        <th>Long Description</th>
                        <th>Notification Date</th>
                        <th>Published</th>
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
                        <td class="center"><%=code.getPublished()%></td>
                        <td class="center">
                            <form name="edit" method="post" action="editnotices.jsp"> 
                                <input type="hidden" name="origin" value="<%=code.getOrigin()%>">
                                <input type="hidden" name="shortdesc" value="<%=code.getShortDesc()%>">
                                <input type="hidden" name="longdesc" value="<%=code.getLongDesc()%>">
                                <input type="hidden" name="publish" value="<%=code.getPublished()%>">
                                <input type="hidden" name="notifuuid" value="<%=code.getUuid()%>">
                                <input class="btn btn-success" type="submit" name="editsource" id="submit" value="Edit" /> 
                            </form>
                            <form name="delete" method="post" action="../deletenotification">
                                <input type="hidden" name="origin" value="<%=code.getOrigin()%>">
                                <input type="hidden" name="notifuuid" value="<%=code.getUuid()%>">
                                <input class="btn btn-success" type="submit" name="deletenotif" id="submit" value="Delete" /> 
                            </form>
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
