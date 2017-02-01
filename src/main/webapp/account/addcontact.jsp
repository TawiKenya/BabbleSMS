
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
<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Group"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Status"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page
	import="ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO"%>
<%@page
	import="ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO"%>
<%@page
	import="ke.co.tawi.babblesms.server.servlet.upload.ContactUpload"%>

<%@page import="java.util.LinkedList"%>
<%@page import="java.util.List"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>


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

	CacheManager mgr = CacheManager.getInstance();
	Cache statusCache = mgr.getCache(CacheVariables.CACHE_STATUS_BY_UUID);
	Cache networkCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
	Cache accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);

	List<Group> contactsgrpList = new LinkedList<Group>();
	List<Network> networkList = new LinkedList<Network>();

	List keys;
	Group cg;
	String notNull = null;

	Group cgroup = new Group();

	Account account = new Account();

	Element element;
	if ((element = accountsCache.get(username)) != null) {
		account = (Account) element.getObjectValue();
	}

	String accountuuid = account.getUuid();

	GroupDAO groupDAO = new GroupDAO();
	contactsgrpList = groupDAO.getGroups(account);

	keys = networkCache.getKeys();
	for (Object key : keys) {
		element = networkCache.get(key);
		networkList.add((Network) element.getObjectValue());
	}
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

	<br /> <br />


	<div class="col-lg-10 col-md-10 col-sm-5">



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

		<div class="col-lg-4 col-md-4">
		<h3>Add Contacts</h3>
			<form class="form-horizontal" method="POST" action="addContact">

				<div class="form-group">
					<label for="name" for="name">Name</label> <input
						class="form-control input-xlarge focused" id="name" type="text"
						name="name" required>

				</div>

				<div class="form-group" id="phone">
					<label for="number" for="phone">Phone Number</label>
					<div id="addphones1">
						<input class="form-control input-xlarge focused" id="number"
							name="phones" type="text" onkeypress='return validateQty(event);'
							required>
						<button id='addphns'>+</button>

						<select name="networks" class="form-control network"
							id="addphones">
							<%
								for (Network code : networkList) {
							%>
							<option value="<%=code.getUuid()%>"><%=code.getName()%></option>
							<%
								}
							%>
						</select>
					</div>
				</div>

				<div class="form-group" id="mail">
					<label for="email">Email</label> <input
						class="form-control input-xlarge focused" id="email" name="emails"
						type="text">
					<button id="addemail">+</button>
				</div>

				<div class="form-group">
					<label for="description">Description</label>

					<textarea class="form-control" rows="3" cols="5" name="description"
						id="description" placeholder="Add description here..."></textarea>

				</div>
				<!-- Group table starts here-->
				<div class="form-group">
					<label for="tableselect11">Groups</label>
					<div id="tableselect11" class="form-control">
						<table id="groupsel" class="table table-striped table-bordered">
							<%
								if (contactsgrpList != null) {
									for (Group group : contactsgrpList) {
							%>
							<tr>
								<td class="center"><input type="checkbox" id="remember"
									value="<%=group.getUuid()%>" name="groups" /> <a
									groupuuid="<%=account.getUuid()%>"><%=group.getName()%></a></td>
							</tr>
							<%
								} // end 'for (Group group : contactsgrpList)'
								} // end 'if (contactsgrpList != null)'
							%>
						</table>
					</div>
				</div>
				<!-- Group table ends here-->




				<button type="submit" id="save" class="btn btn-primary">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Save
					&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
				<button type="" id="cancel1" class="btn btn-primary">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cancel&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>



			</form>

		</div>



		<div class="col-lg-1 col-md-1"></div>

		<div class="col-lg-5 col-md-5">
			<h3>
				<%
					if (StringUtils.isNotBlank((String) session.getAttribute(ContactUpload.UPLOAD_FEEDBACK))) {
						String servletResponse = (String) session.getAttribute(ContactUpload.UPLOAD_FEEDBACK);
						out.println(servletResponse);
						//used by javascript 
						if (servletResponse != null) {
							notNull = servletResponse.substring(0, 10);
						}
						session.setAttribute(ContactUpload.UPLOAD_FEEDBACK, null);
					}
				%>
			</h3>
			
			<p>
				<h3>Upload CSV file</h3> 
				   ( <b>With this format:</b>
				   <code>name, phone, network</code>)
			</p>

			<br />
			<form class="form-horizontal" method="POST" action="uploadContacts"
				name="uploadContacts" enctype="multipart/form-data">

				<div class="form-group" id="javascript" javaScriptCheck="<%=notNull%>">

                    <label for="upload">Contact CSV</label>
					
						<input class="form-control input-xlarge focused" id="upload" id="contname" type="file" name="upload" required>

				</div>
				
				<div class="form-group">
				<label  for="upload">Add Contacts To a Group (Optional)</label>
				
					<div id="tableselect" class="form-control">
						<table id="groupsel" class="table table-striped table-bordered">
							<%
								if (contactsgrpList != null) {
									for (Group code : contactsgrpList) {
							%>

							<tr>
								<td><input type="checkbox" id="remember"
									value="<%=code.getUuid()%>" name="groupselected" /> <a
									groupuuid="<%=account.getUuid()%>"><%=code.getName()%></a></td>
							</tr>

							<%
								} // end 'for (Group code : contactsgrpList)'
								} // end 'if (contactsgrpList != null)'
							%>
						</table>
					</div>

	
				</div>


				<button type="submit" class="btn btn-primary">Upload File</button>
				<button class="btn">Cancel</button>

			</form>



			<a href="../files/Contacts.csv" title="Download a sample csv file"
				download> Download a sample CSV file <img src="../img/csv.png"
				alter="CSV file"></a> <br />
		    <a href="../files/Contacts.xlsx"
				title="Download a sample excelsheet file"> Download a sample MS
				Excel file <img src="../img/excel.png" alter="Excel Sheet File">
			</a>


		</div>

	</div>
</div>
<!--scroll to the bottom the page if file upload is done -->
<script type="text/javascript">
	$("document").ready(function() {
		var check1 = $("#javascript").attr("javaScriptCheck");
		if (check1.length > 4) {
			$("html, body").animate({
				scrollTop : $(document).height()
			});
		}
	});
</script>


<jsp:include page="footer.jsp" />

<script>
	$("document")
			.ready(
					function() {
						//add more phone numbers upon clicking
						$("#addphone")
								.click(
										function(e) {
											e.preventDefault();
											$("#phone")
													.append(
															"<div class='controls'> <input style='margin:5px 3px 0 0;' class='input-xlarge focused'  id='number' name='phones' type='text'><select name='networks' class='network'></select></div>");
											//add values to network thruogh ajax call
											//get url path first
											var basepath = window.location.protocol
													+ "//"
													+ window.location.host
													+ "/";
											var pathArray = window.location.pathname
													.split('/');
											var url = basepath + pathArray[1]
													+ '/' + pathArray[2]
													+ '/ajaxaddcontact.jsp';
											$.ajax({
												url : url,
												type : "GET",
												cache : false,
											}).done(function(data) {

												$(".network").append(data);
											});//ajax call ends here
										});

						$("#deleteclone").click(function(e) {
							e.preventDefault();
							$(this).parent().hide();
						});

					});
</script>

<script type="text/javascript">
	function validateQty(event) {
		var key = window.event ? event.keyCode : event.which;
		//choose the only keys allowed..
		if (event.keyCode === 8 //backspace key
				|| event.keyCode === 46 //delete key
				|| event.keyCode === 37 //left arrow key
				|| event.keyCode === 39 //right arrow key
				|| key === 0 //right and left arrow keys i mozilla
				|| key === 32 //whitespace key
				|| key === 45 //hypen key
				|| key === 118 // for mozilla (ctrl + v).|| (key === 17 && key === 67) //(ctrl + c)
				|| (key > 47 && key < 58)//from 0 to 9
		) {
			return true;

		} else
			return false;
	}
</script>