
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



<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page
	import="ke.co.tawi.babblesms.server.accountmgmt.pagination.inbox.InboxPage"%>
<%@page
	import="ke.co.tawi.babblesms.server.accountmgmt.pagination.inbox.InboxPaginator"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionStatistics"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page
	import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page
	import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page
	import="ke.co.tawi.babblesms.server.persistence.logs.IncomingLogDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.log.IncomingLog"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Shortcode"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.CacheManager"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

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

	List<IncomingLog> incomingList;

	CacheManager mgr = CacheManager.getInstance();
	Cache statisticsCache = mgr.getCache(CacheVariables.CACHE_STATISTICS_BY_ACCOUNT);
	Cache shortcodesCache = mgr.getCache(CacheVariables.CACHE_SHORTCODE_BY_UUID);
	Cache contactsCache = mgr.getCache(CacheVariables.CACHE_CONTACTS_BY_UUID);
	Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);

	HashMap<String, String> shortcodeHash = new HashMap<String, String>();
	HashMap<String, String> contactHash = new HashMap<String, String>();
	HashMap<String, String> networkHash = new HashMap<String, String>();

	Element element;
	List keys;
	Shortcode shortcode;
	Contact contct;
	Network network;
	int incount = 0; // Generic counter

	InboxPaginator paginator = new InboxPaginator(accountuuid);

	SessionStatistics statistics = new SessionStatistics();

	if ((element = statisticsCache.get(accountuuid)) != null) {
		statistics = (SessionStatistics) element.getObjectValue();
	}

	keys = shortcodesCache.getKeys();

	for (Object key : keys) {
		element = shortcodesCache.get(key);
		shortcode = (Shortcode) element.getObjectValue();
		shortcodeHash.put(shortcode.getUuid(), shortcode.getCodenumber());
	}

	keys = networksCache.getKeys();

	for (Object key : keys) {
		element = networksCache.get(key);
		network = (Network) element.getObjectValue();
		networkHash.put(network.getUuid(), StringUtils.split(network.getName())[0]);
	}

	incount = statistics.getAllIncomingUSSDCount();
	InboxPage inboxPage;
	int ussdCount = 0; // The current count of the USSD sessions

	if (incount == 0) { // This user has no Incoming USSD in the account
		inboxPage = new InboxPage();
		incomingList = new ArrayList<IncomingLog>();
		ussdCount = 0;

	} else {
		inboxPage = (InboxPage) session.getAttribute("currentInboxPage");
		String referrer = request.getHeader("referer");
		String pageParam = (String) request.getParameter("page");

		// We are to give the first page
		if (inboxPage == null || !StringUtils.endsWith(referrer, "inbox.jsp")
				|| StringUtils.equalsIgnoreCase(pageParam, "first")) {
			inboxPage = paginator.getFirstPage();

			// We are to give the last page
		} else if (StringUtils.equalsIgnoreCase(pageParam, "last")) {
			inboxPage = paginator.getLastPage();

			// We are to give the previous page
		} else if (StringUtils.equalsIgnoreCase(pageParam, "previous")) {
			inboxPage = paginator.getPrevPage(inboxPage);

			// We are to give the next page 
		} else if (StringUtils.equalsIgnoreCase(pageParam, "next")) {
			inboxPage = paginator.getNextPage(inboxPage);
		}

		session.setAttribute("currentInboxPage", inboxPage);

		incomingList = inboxPage.getContents();

		ussdCount = (inboxPage.getPageNum() - 1) * inboxPage.getPagesize() + 1;
	}

	PhoneDAO phnDAO = PhoneDAO.getInstance();
	ContactDAO ctssDAO = ContactDAO.getInstance();

	SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
	SimpleDateFormat timezoneFormatter = new SimpleDateFormat("z");
%>

<jsp:include page="messageheader.jsp" />





		<div class="col-lg-10 col-md-10 col-sm-5">

			<div class="col-lg-1 col-md-1 col-sm-1">

				<form name="pageForm" method="post" action="inbox.jsp">
					<input class="btn btn-info" type="submit" name="refresh"
						value="Refresh" /> <input class="toolbarBtn" type="hidden"
						name="page" value="First" />

				</form>
			</div>

			<div class="col-lg-5 col-md-5 col-sm-2"></div>

			<!--  

			<div id="pagination" class="col-lg-5 col-md-5 col-sm-2">
				<form name="pageForm" method="post" action="inbox.jsp">
					<%if (!inboxPage.isFirstPage()) {%>
					<input class="btn btn-info" type="submit" name="page" value="First" />
					<input class="btn btn-info" type="submit" name="page" value="Previous" />
					<%}%>
					Page
					<%=inboxPage.getPageNum()%>
					of
					<%=inboxPage.getTotalPage()%>


					<%if (!inboxPage.isLastPage()) {%>
					<input class="btn btn-info" type="submit" name="page" value="Next">
					<input class="btn btn-info" type="submit" name="page" value="Last">
					<%}%>
				</form>
			</div>
-->

			<!--<form id="exportToPDF" name="exportCSV" method="post" action="exportCSV" target="_blank">
                    <p>
                        <input class="toolbarBtn" type="submit" name="exportCSV" value="Export CSV" >
                        <input class="toolbarBtn" type="hidden" name="page" value="inbox.jsp">
                    </p>
                </form>-->

			<div id="export" class="col-lg-2 col-md-2 col-sm-1">
				<form id="exportToExcel" name="exportExcelForm" method="post"
					action="exportExcel" target="_blank">

					<input class="btn btn-info" type="submit" name="exportExcel"
						value="Export As Excel&nbsp;&nbsp &nbsp;&nbsp;" /> <input
						class="btn btn-info" type="hidden" name="ExportPageExcel"
						value="<%=inboxPage.getPageNum()%>" /> <input class="btn btn-info"
						type="hidden" name="page" value="inbox.jsp" />

				</form>
			</div>

			<div id="export" class="col-lg-2 col-md-2 col-sm-1">
				<form id="exportToExcel" name="exportExcelForm" method="post"
					action="exportExcel" target="_blank">

					<input class="btn btn-info" type="submit" name="exportExcel"
						value="Export All Excel&nbsp;&nbsp;" /> <input class="btn btn-info"
						type="hidden" name="ExportAllExcel" value="All" /> <input
						class="btn btn-info" type="hidden" name="page" value="inbox.jsp" />

				</form>


			</div>

			<br />



			<table id="incomingUSSD"
				class="table table-striped table-bordered bootstrap-datatable datatable"
				summary="Outgoing">
				<thead>
					<tr>
						<th>*</th>
						<th>Message</th>
						<th>Source</th>
						<th>Destination</th>
						<th>Network</th>
						<th>Time (<%=timezoneFormatter.format(new Date())%> Time
							Zone)
						</th>
						<th>Message Id</th>
					</tr>
				</thead>
				<tbody>
					<%
						if (incomingList != null) {
							for (IncomingLog code : incomingList) {
					%>
					<tr width="5%">
						<td width="2%"><%=ussdCount%></td>

						<td class="center"><%=code.getMessage()%></td>
						<%
							List<Phone> phoneList;
									if (phnDAO.getPhones(code.getOrigin()).size() > 0) {

										phoneList = phnDAO.getPhones(code.getOrigin());
										Phone phone = phoneList.get(0);
										Contact contacts = ctssDAO.getContact(phone.getContactUuid());
						%>
						<td class="center"><%=contacts.getName()%></td>

						<%
							} else {
						%>
						<td class="center"><%=code.getOrigin()%></td>
						<%
							}
						%>

						<td class="center"><%=code.getDestination()%></td>
						<td class="center"><%=networkHash.get(code.getNetworkUuid())%></td>
						<td class="center"><%=dateFormatter.format(code.getLogTime())%>
						</td>
						<td class="center"><%=code.getUuid()%></td>
					</tr>

					<%
						ussdCount++;
							}
						}
						//<%=networkHash.get(code.getNetwork())use
					%>
				</tbody>
			</table>


		
	

	

	

		<div id="pagination">
			<form name="pageForm" method="post" action="inbox.jsp">
				<%
					if (!inboxPage.isFirstPage()) {
				%>
				<input class="btn btn-info" type="submit" name="page" value="First" />
				<input class="btn btn-info" type="submit" name="page" value="Previous" />
				<%
					}
				%>
				<span class="pageInfo">Page <span
					class="pagePosition currentPage"><%=inboxPage.getPageNum()%></span>
					of <span class="pagePosition"><%=inboxPage.getTotalPage()%></span>
				</span>
				<%
					if (!inboxPage.isLastPage()) {
				%>
				<input class="btn btn-info" type="submit" name="page" value="Next">
				<input class="btn btn-info" type="submit" name="page" value="Last">
				<%
					}
				%>
			</form>
		</div>

		<!--<div id="export">                
                <form id="exportToPDF" name="exportCSV" method="post" action="exportCSV" target="_blank">
                    <p>
                        <input class="toolbarBtn" type="submit" name="exportCSV" value="Export CSV" >
                        <input class="toolbarBtn" type="hidden" name="page" value="inbox.jsp">
                    </p>
                </form>

                <form id="exportToExcel" name="exportExcelForm" method="post" action="exportExcel" target="_blank">
                    <p>
                        <input class="toolbarBtn" type="submit" name="exportExcel" value="Export Excel" >
                        <input class="toolbarBtn" type="hidden" name="page" value="inbox.jsp">
                    </p>
                </form>
            </div>-->
	</div>


</div>


	<jsp:include page="footer.jsp" />

</div>