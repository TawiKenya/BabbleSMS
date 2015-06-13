<%
    /**
    Copyright 2015 Tawi Commercial Services Ltd

    Licensed under the Open Software License, Version 3.0 (the ?License?); you may 
    not use this file except in compliance with the License. You may obtain a copy 
    of the License at:
    http://opensource.org/licenses/OSL-3.0

    Unless required by applicable law or agreed to in writing, software distributed 
    under the License is distributed on an ?AS IS? BASIS, WITHOUT WARRANTIES OR
    CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and limitations 
    under the License.
    */
%>

<%@page import="ke.co.tawi.babblesms.server.beans.status.Status"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>


<jsp:include page="contactheader.jsp" />
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

     AccountDAO accountDAO = AccountDAO.getInstance();
     Account account = accountDAO.getAccountByName(username);

    String accountuuid = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_ACCOUNTUUID);
    CacheManager mgr = CacheManager.getInstance();
    Cache statusCache = mgr.getCache(CacheVariables.CACHE_STATUS_BY_UUID);

    List<Status> list = new ArrayList<Status>();

    Element element;
    List keys;
    Status st;
    keys = statusCache.getKeys();
    for (Object key : keys) {
        element = statusCache.get(key);
        st = (Status) element.getObjectValue();
        list.add(st);

    }

%>

<div>
    <ul class="breadcrumb">
        <li>
            <a href="index.jsp">Home</a> <span class="divider">/</span>
        </li>
        <li>
            Add group
        </li>
    </ul>
</div>




<div class="row-fluid sortable">
    <div class="box span12">
       <!-- <div class="box-header well" data-original-title>
            <h2><i class="icon-edit"></i> Add Group</h2>
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>-->
        <div class="box-content">

            <%                String addErrStr = (String) session.getAttribute(SessionConstants.ADD_ERROR);
                String addSuccessStr = (String) session.getAttribute(SessionConstants.ADD_SUCCESS);

                if (StringUtils.isNotEmpty(addErrStr)) {
                    out.println("<p style='color:red;'>");
                    out.println("Form error: " + addErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADD_ERROR, null);
                }

                if (StringUtils.isNotEmpty(addSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(addSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADD_SUCCESS, null);
                }
            %>
            <form class="form-horizontal" action="addgroup" method="POST">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="gname" autofocus>Name</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="groupname" type="text" name="name" value="">
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="desc">Description</label>
                        <div class="controls">
                            <textarea rows ="3" cols ="3" id="groupdesc" name="desc" > </textarea>
                            <!--<input class="input-xlarge focused"  id="groupdesc" name="gdesc" type="text">-->
                        </div>
                    </div>

                    <!--<div class = "input_fields_wrap">
                    <button class = "add_field_button">Add contacts</button>
                       </div>-->
                    <div class="form-actions">
                        <input type="hidden" name="accountuuid" value="<%=account.getUuid()%>">
                        <button type="submit" class="btn btn-primary">Save changes</button>
                        <!--<button class="btn">Cancel</button>-->
                        <input type="submit" id="cancel" value="Cancel"/>
                    </div>
                </fieldset>
            </form>

        </div>
    </div><!--/span-->

</div><!--/row-->



<jsp:include page="footer.jsp" />
