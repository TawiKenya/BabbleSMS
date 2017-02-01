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

<%@page import="ke.co.tawi.babblesms.server.beans.account.Status"%>
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
<div class="row">
<div class="col-lg-2 col-md-2 col-sm-2">


		<ul class="nav nav-tabs nav-stacked main-menu">
						<!--menu to change depending on page requested-->
			 			
						<li class="nav-header hidden-tablet">Contacts</li>
						<li><div class="dropdown">
								<button class="btn btn-default dropdown-toggle" type="button"
									id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu" role="menu"
									aria-labelledby="dropdownMenu1">
									<!-- <li role="presentation"><a role="menuitem" tabindex="-1" href="inactivecontacts.jsp">Inactive contacts</a></li>-->
			 					
									<li role="presentation"><a role="menuitem" tabindex="-1"
										href="#">Inactive contacts</a></li>
								</ul>
								<a class="ajax-link" href="contact.jsp"><i
									class="icon-inbox"></i><span class="hidden-tablet">All
										contacts</span></a>
							</div></li>
						<li><a class="ajax-link" href="addcontact.jsp"><i
								class="icon-plus-sign"></i><span class="hidden-tablet">Add
									contact</span></a></li>
						<li class="nav-header hidden-tablet">Groups</li>
						<li><a class="ajax-link" href="groups.jsp"><i
								class="icon-globe"></i><span class="hidden-tablet">All
									groups</span></a></li>
						<!--   <li><a class="ajax-link" href="#"><i class="icon-globe"></i><span class="hidden-tablet">View group</span></a></li>-->
				 		<li><a class="ajax-link" href="addgroup.jsp"><i
								class="icon-plus-sign"></i><span class="hidden-tablet">Add
									group </span></a></li>
						<!--<li><a class="ajax-link" href="#"><i class="icon-plus-sign"></i><span class="hidden-tablet">Add group </span></a></li> 
	          <li><a class="ajax-link" href="contactspergroup.jsp"><i class="icon-folder-open"></i><span class="hidden-tablet">Group Contacts</span></a></li>-->
						</ul>
				<!--  	<label id="for-is-ajax" class="hidden-tablet" for="is-ajax"><input id="is-ajax" type="checkbox"> Ajax on menu</label>-->
					

	</div>




<div class="col-lg-10 col-md-10 col-sm-5">
    
       <!-- <div class="box-header well" data-original-title>
            <h2><i class="icon-edit"></i> Add Group</h2>
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>-->
     

            <%                
                String addErrStr = (String) session.getAttribute(SessionConstants.ADD_ERROR);
                String addSuccessStr = (String) session.getAttribute(SessionConstants.ADD_SUCCESS);

                if (StringUtils.isNotEmpty(addErrStr)) {
                     out.println("<p class='error'>");
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
            <div class="col-lg-2 col-md-2"></div>
            <div class="cl-lg-4 col-md-4">
            <form class="form-horizontal" action="addGroup" method="POST">
               
                    <div class="form-group">
                        <label for="gname" autofocus>Name</label>
                        
                          <input class="form-control input-xlarge focused" id="groupname" type="text" name="name" value="">
                        
                    </div>
                    <div class="form-group">
                        <label  for="desc">Description</label>
                       
                            <textarea class="form-control" rows ="3" cols ="3" id="groupdesc" name="desc" > </textarea>
                        
                    </div>

                    <!--<div class = "input_fields_wrap">
                    <button class = "add_field_button">Add contacts</button>
                       </div>-->
                    
                        <button type="submit" class="btn btn-primary">Save</button>
                        <input type="submit" id="cancel" value="Cancel"/>
                  
                
            </form>
</div>
       
    

</div>

</div>

<jsp:include page="footer.jsp" />
