<%
    /**
    Copyright 2015 Tawi Commercial Services Ltd

    Licensed under the Open Software License, Version 3.0 (the “License”); you may 
    not use this file except in compliance with the License. You may obtain a copy 
    of the License at:
    http://opensource.org/licenses/OSL-3.0

    Unless required by applicable law or agreed to in writing, software distributed 
    under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR
    CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and limitations 
    under the License.
    */
%>

<%@page import="ke.co.tawi.babblesms.server.beans.contact.Group"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.utils.CountUtils"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>

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
    CacheManager mgr = CacheManager.getInstance();
    Cache groupCache = mgr.getCache(CacheVariables.CACHE_GROUP_BY_UUID);

    Group cgroup = new Group();

    List<Group> list = new ArrayList();
    Element element;
    List keys;

    AccountDAO accountDAO = AccountDAO.getInstance();
    Account account = accountDAO.getAccountByName(username);
    list = GroupDAO.getInstance().getGroups(account);
    CountUtils cts = CountUtils.getInstance();

    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy");
%> 
<jsp:include page="contactheader.jsp" />

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
    <!--<div class="box span12">
        <div class="box-header well" data-original-title>
            <a class="btn" href="#" title="refresh page" data-rel="tooltip"><i class="icon-refresh"></i> Refresh</a>                  
            <a class="btn" href="#" title="delete message" data-rel="tooltip"><i class="icon-trash"></i> Delete</a>  
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>-->
        <div class="box-content">
            <%                String addErrStr = (String) session.getAttribute(SessionConstants.ADD_ERROR);
                String addSuccessStr = (String) session.getAttribute(SessionConstants.ADD_SUCCESS);
                
                String deleteErrStr = (String) session.getAttribute(SessionConstants.DELETE_ERROR);
                String deleteSuccessStr = (String) session.getAttribute(SessionConstants.DELETE_SUCCESS);
                
                String updateErrStr = (String) session.getAttribute(SessionConstants.UPDATE_ERROR);
                String updateSuccessStr = (String) session.getAttribute(SessionConstants.UPDATE_SUCCESS);

                if (StringUtils.isNotEmpty(addErrStr)) {
                     out.println("<p class='error'>");
                    out.println("Form error: " + addErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADD_ERROR, null);
                }

                if(StringUtils.isNotEmpty(addSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(addSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADD_SUCCESS, null);
                }
                
                if (StringUtils.isNotEmpty(deleteErrStr)) {
                     out.println("<p class='error'>");
                    out.println("Form error: " + deleteErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.DELETE_ERROR, null);
                }

                if (StringUtils.isNotEmpty(deleteSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(deleteSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.DELETE_SUCCESS, null);
                }
                
                if (StringUtils.isNotEmpty(updateErrStr)) {
                     out.println("<p class='error'>");
                    out.println("Form error: " + updateErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.UPDATE_ERROR, null);
                }

                if (StringUtils.isNotEmpty(updateSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(updateSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.UPDATE_SUCCESS, null);
                }
            %>
            <table class="table table-striped table-bordered bootstrap-datatable datatable">
                <thead>
                    <tr>

                        <th>*</th>

                        <th>Group name</th>
                        <th>Group description</th>
                        <th>Total Contacts</th>
                        <th>SMS Sent</th>
                        <th style = "display:none">group uuid </th>
                        <th>Date of Creation</th>
                       <!-- <th>actions</th> -->
                    </tr>
                </thead>   
                <tbody class="groupstablee">
                    <%
                        int count = 1;
                        if (list != null) {
                            for (Group code : list) {
                    %>
                    <tr>

                        <td width="5%"><%=count%></td>
                        <td class="center"><a class="Ylink" href="#"><%=code.getName()%></a></td>
                        <td class="center"><%=code.getDescription()%> </td>
                        <td class="center"><%=cts.getContactInGroup(code.getUuid())%> </td>
                        <td class="center"><%=cts.getCumulativeOutgoingGroup(code.getUuid())%> </td>
                        <td class="center"><%= dateFormatter.format(code.getCreationdate()) %>
                        <td style="display:none"><%=code.getUuid()%></td>

                        <!--<td class="center">
                            <form name="edit" method="post" action="edit.jsp"> 
                                <input type="hidden" name="grpname" value="<%=code.getName()%>">
                                <input type="hidden" name="desc" value="<%=code.getDescription()%>">
                                <input type="hidden" name="groupuuid" value="<%=code.getUuid()%>">
                                <input class="btn btn-success" type="submit" name="editGroup" id="submit" value="Edit" /> 
                            </form>
                            <form name="delete" method="post" action="deletegroup.jsp">
                                <input type="hidden" name="groupuuid" value="<%=code.getUuid()%>">
                                <input class="btn btn-success" type="submit" name="deletecontact" id="submit" value="Delete" /> 
                            </form>
                        </td> -->


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

<!-- Group Form  for the pop up starts-->
<div id="contactdiv">
<form class="form" onsubmit="return formValidator()" action = "editGroup" method = "POST" id="contact" >
<p style ="margin-top: 1px;margin-right: 2px;position:absolute;top:1%;right:1%; color:red; font-size:20px;" id ="close">x</p>
<b>Groups Details</b>
<label>Name: <span></span></label>
<input type="text" id="name" name = "name" />
<label>description <span></span></label>
<textarea cols="3" rows="2" id ="desc" name = "phone1"></textarea>
<!--<input type="textarea" id="phone2" name ="phone1" /> -->
<label>Total contacts:</label>
<input type="text" id="tcontacts" class="edit_area" readonly ="readonly" />
<label>SMS sent:</label>
<input type="text" id="smssent"  class="edit_area" disabled = "disabled"/>
<input type="hidden" id="guuid" name = "uuid" class="edit_area" />

<br>
<input type="submit" class="btn btn-primary" value="Save" />
<input type="submit" class="btn btn-primary" id="cancel" value="Cancel"/>


<br/>

</form>
</div>


<!-- Contact Form  for the pop up ends-->

<jsp:include page="footer.jsp" />

