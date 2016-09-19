
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
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Mask"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Shortcode"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.beans.creditmgmt.SMSBalance"%>
<%@page
	import="ke.co.tawi.babblesms.server.beans.creditmgmt.ShortcodeBalance"%>
<%@page
	import="ke.co.tawi.babblesms.server.beans.creditmgmt.MaskBalance"%>

<%@page
	import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page
	import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page
	import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>
<%@page
	import="ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsBalanceDAO"%>

<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>

<%@page import="java.util.LinkedList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>

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

	CacheManager mgr = CacheManager.getInstance();
	Cache shortcodesCache = mgr.getCache(CacheVariables.CACHE_SHORTCODE_BY_UUID);
	Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
	Cache maskCache = mgr.getCache(CacheVariables.CACHE_MASK_BY_UUID);
	Cache accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);

	ContactDAO contactDAO = ContactDAO.getInstance();
	PhoneDAO phoneDAO = PhoneDAO.getInstance();
	NetworkDAO networkDAO = NetworkDAO.getInstance();
	SmsBalanceDAO smsBalanceDAO = SmsBalanceDAO.getInstance();

	HashMap<String, String> networkHash = new HashMap<String, String>();

	Account account = new Account();
	Element element;
	if ((element = accountsCache.get(username)) != null) {
		account = (Account) element.getObjectValue();
	}

	Network network;
	Shortcode shortcode;
	Mask mask;

	List<Network> networkList = new LinkedList<Network>();
	List<Shortcode> shortcodeList = new LinkedList<Shortcode>();
	List<Mask> maskList = new LinkedList<Mask>();

	HashMap<String, Integer> maskBalanceHash = new HashMap<String, Integer>();
	HashMap<String, Integer> shortcodeBalanceHash = new HashMap<String, Integer>();
	List<SMSBalance> balanceList = smsBalanceDAO.getBalances(account);

	for (SMSBalance balance : balanceList) {
		if (balance instanceof ShortcodeBalance) {
			shortcodeBalanceHash.put(((ShortcodeBalance) balance).getShortcodeUuid(),
					new Integer(balance.getCount()));
		} else {
			maskBalanceHash.put(((MaskBalance) balance).getMaskUuid(), new Integer(balance.getCount()));
		}
	} // end 'for(SMSBalance balance : balanceList)'

	List keys;
	keys = shortcodesCache.getKeys();
	for (Object key : keys) {
		element = shortcodesCache.get(key);
		shortcode = (Shortcode) element.getObjectValue();
		if (account.getUuid().equals(shortcode.getAccountuuid())) {
			shortcodeList.add(shortcode);
		}
	}

	keys = networksCache.getKeys();
	for (Object key : keys) {
		element = networksCache.get(key);
		network = (Network) element.getObjectValue();
		networkHash.put(network.getUuid(), network.getName());
	}

	keys = maskCache.getKeys();
	for (Object key : keys) {
		element = maskCache.get(key);
		mask = (Mask) element.getObjectValue();
		if (account.getUuid().equals(mask.getAccountuuid())) {
			maskList.add(mask);
		}
	}
%>
<jsp:include page="reportheader.jsp" />

<div class="row">


	<div class="col-lg-3 col-md-3 col-sm-2">

		<!-- left menu starts -->


		<ul class="nav nav-tabs nav-stacked main-menu">
			<!--menu to change depending on page requested-->
			<li class="nav-header hidden-tablet">Message Board</li>
			<li><a class="ajax-link" href="portfolio.jsp"><i
					class="icon-globe"></i><span class="hidden-tablet">Balance</span></a></li>
			<li><a class="ajax-link" href="purchase.jsp"><i
					class="icon-plus-sign"></i><span class="hidden-tablet">Purchase
						History </span></a></li>
			<li><a class="ajax-link" href="administrator.jsp"><i
					class="icon-plus-sign"></i><span class="hidden-tablet">Admin
						Notices</span></a></li>


		</ul>
		<!--<label id="for-is-ajax" class="hidden-tablet" for="is-ajax"><input id="is-ajax" type="checkbox"> Ajax on menu</label>-->


		<!-- left menu ends -->



	</div>

	<div class="col-lg-9 col-md-9 col-sm-4">






		<div class="content_title">
			<h3>Sender Ids</h3>

			<p>Below is the summary of the Sender Ids managed by your
				account: A Sender Id can only send SMS to mobile users</p>
		</div>



		<table
			class="table table-striped table-bordered bootstrap-datatable datatable">
			<thead>
				<tr>
					<th>*</th>
					<th>Sender Id</th>
					<th>Network</th>
					<th>Balance</th>
				</tr>
			</thead>

			<tbody>
				<%
					int count = 1;
					if (maskList != null) {
						for (Mask msk : maskList) {
				%>
				<tr>
					<td width="10%"><%=count%></td>
					<td class="center"><%=msk.getMaskname()%></td>
					<td class="center"><%=networkHash.get(msk.getNetworkuuid())%></td>
					<td class="center"><%=maskBalanceHash.get(msk.getUuid())%></td>
				</tr>

				<%
					count++;
						}
					} // end 'if (masklist != null)'
				%>


			</tbody>
		</table>

		<p>&nbsp;&nbsp;&nbsp;</p>


		<div class="content_title">
			<h3>Shortcode</h3>

			<p>Below is the summary of the short codes managed by your
				account: A shortcode can send and receive SMS from mobile users</p>

		</div>



		<table
			class="table table-striped table-bordered bootstrap-datatable datatable">
			<thead>
				<tr>
					<th>*</th>
					<th>ShortCodes</th>
					<th>Network</th>
					<th>Balance</th>
				</tr>
			</thead>
			<tbody>
				<%
					count = 1;
					if (shortcodeList != null) {
						for (Shortcode code : shortcodeList) {
				%>
				<tr>
					<td width="10%"><%=count%></td>
					<td class="center"><%=code.getCodenumber()%></td>
					<td class="center"><%=networkHash.get(code.getNetworkuuid())%></td>
					<td class="center"><%=shortcodeBalanceHash.get(code.getUuid())%></td>
				</tr>

				<%
					count++;
						}
					} // end 'if (list != null)'
				%>

			</tbody>
		</table>





	</div>
	<!--/row-->
</div>

<jsp:include page="footer.jsp" />
