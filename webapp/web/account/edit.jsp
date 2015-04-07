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

<jsp:include page="contactheader.jsp" />
<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Edit contact</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">

    <div class="box span12">
        <div class="box-header well" data-original-title>
            <h2><i class="icon-edit"></i> Edit Contact</h2>
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">


            <% if (StringUtils.equals(request.getParameter("editContact"), "Edit")) {%>




            <form class="form-horizontal" method="POST" action="editcontact">
                <fieldset>

                    <div class="control-group">
                        <label class="control-label" for="phonenum">Phone Number</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="phonenum" name="phonenum" type="text" value="<%=request.getParameter("phonenum")%>">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="contname">Contact name</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="contname" type="text" name="contname" value="<%=request.getParameter("name")%>">
                        </div>
                    </div>

                    <div class="form-actions">
                        <input type="hidden" name="contactuuid" value="<%=request.getParameter("contactuuid")%>">
                        <input type="hidden" name="phoneuuid" value="<%=request.getParameter("phoneuuid")%>">
                        <button type="submit" class="btn btn-primary">Save changes</button>
                        <a href="contact.jsp"><button class="btn">Cancel</button></a>
                    </div>

                </fieldset>
            </form>

            <% } else if (StringUtils.equals(request.getParameter("editEmail"), "Edit")) {%>




            <form class="form-horizontal" method="POST" action="editEmail">
                <fieldset>

                    <div class="control-group">
                        <label class="control-label" for="email">Email</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="email" name="email" type="text" value="<%=request.getParameter("emailaddr")%>">
                        </div>
                    </div>                    
                    <div class="control-group">
                        <label class="control-label" for="contname">Contact name</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="contname" type="text" name="contname" value="<%=request.getParameter("name")%>">
                        </div>
                    </div>

                    <div class="form-actions">
                        <input type="hidden" name="contactuuid" value="<%=request.getParameter("contactuuid")%>">
                        <input type="hidden" name="emailuuid" value="<%=request.getParameter("emailuuid")%>">
                        <button type="submit" class="btn btn-primary">Save changes</button>
                        <a href="contact.jsp"><button class="btn">Cancel</button></a>
                    </div>

                </fieldset>
            </form>            

            <% } else if (StringUtils.equals(request.getParameter("editGroup"), "Edit")) {%>


            <form class="form-horizontal" method="POST" action="editGroup">
                <fieldset>

                    <div class="control-group">
                        <label class="control-label" for="grpname">Group Name</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="grpname" name="grpname" type="text" value="<%=request.getParameter("grpname")%>">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="desc">Description</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="desc" name="desc" type="text" value="<%=request.getParameter("desc")%>">
                        </div>
                    </div>

                    <div class="form-actions">
                        <input type="hidden" name="grpuuid" value="<%=request.getParameter("groupuuid")%>">
                        <button type="submit" class="btn btn-primary">Save changes</button>
                        <a href="groups.jsp"><button class="btn">Cancel</button></a>
                    </div>

                </fieldset>
            </form>


            <%}%>        



        </div>
    </div>
</div>
<jsp:include page="footer.jsp" />