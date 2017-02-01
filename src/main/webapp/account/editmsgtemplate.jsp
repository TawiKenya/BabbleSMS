<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>

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

%>

<jsp:include page="messageheader.jsp" />
<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Edit Message template</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">

    <div class="box span12">
        <div class="box-header well" data-original-title>
            <h2><i class="icon-edit"></i> Message template</h2>
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">


            <form class="form-horizontal" method="POST" action="../Edittemplate">
                <fieldset>

                    <div class="control-group">
                        <label class="control-label" for="msgtitle">Message Title</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="title" name="title" type="text" value="<%=request.getParameter("title")%>">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="contents">Content</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="content" name="content" type="text" value="<%=request.getParameter("content")%>">
                        </div>
                    </div>
                    

                    <div class="form-actions">
                        <input type="hidden" name="templateuuid" value="<%=request.getParameter("templateuuid")%>">
                        <button type="submit" class="btn btn-primary">Save changes</button>
                        <a href="messagetemplate.jsp"><button class="btn">Cancel</button></a>
                    </div>

                </fieldset>
            </form>

            
        </div>
    </div>
</div>
<jsp:include page="footer.jsp" />