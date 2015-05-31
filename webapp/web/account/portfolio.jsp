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
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Mask"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Shortcode"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.PurchaseHistory"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>

<%@page import="ke.co.tawi.babblesms.server.persistence.items.purchaseHistory.PurchaseHistoryDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>

<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>

<%@page import="java.util.ArrayList"%>
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

    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, String> networkHash = new HashMap<String, String>();
    Element element;

    Network network;
    Shortcode shortcode;
    Mask mask;
    Account account = new Account();

    List<Shortcode> list = new ArrayList();
    List<Mask> masklist = new ArrayList();
    List keys;

    if ((element = accountsCache.get(sessionUsername)) != null) {
        account = (Account) element.getObjectValue();
    }

    keys = shortcodesCache.getKeys();
    for (Object key : keys) {
        element = shortcodesCache.get(key);
        shortcode = (Shortcode) element.getObjectValue();
        if (account.getUuid().equals(shortcode.getAccountuuid())) {

            list.add(shortcode);
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

            masklist.add(mask);
        }
    }

%> 
<jsp:include page="reportheader.jsp" />


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">View contacts</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">

        <div class="box-content">



            <div class="content_title"> 
                <h3>Masks</h3>

                <p>Below is the summary of the Masks managed by your account:
                    A Mask can only send SMS to mobile users</p>
            </div>



            <table class="table table-striped table-bordered bootstrap-datatable datatable">
                <thead>
                    <tr>
                        <th>*</th>

                        
                        <th>Mask</th>
                        <th>Network</th>
                    </tr>
                </thead>   
                <tbody>
                    <%                        
                        int count = 1;
                        if (masklist != null) {
                            for (Mask msk : masklist) {

                    %>
                    <tr>
                        <td width="10%"><%=count%></td>
                        <td class="center"><%=msk.getMaskname()%></td>
                        <td class="center"><%=networkHash.get(msk.getNetworkuuid())%></td>

                    </tr>

                    <%
                                count++;
                            }
                        }// end 'if (masklist != null)'
                    %>


                </tbody>
            </table>  



            <div class="content_title"> 
                <h3>Shortcode</h3>

                <p>Below is the summary of the short codes managed by your account:
                    A shortcode can send and receive SMS from mobile users</p>

            </div>



            <table class="table table-striped table-bordered bootstrap-datatable datatable">
                <thead>
                    <tr>
                        <th>*</th>
                        <th>ShortCode</th>
                        <th>Network</th>
                    </tr>
                </thead>   
                <tbody>
                    <%
                        count = 1;
                        if (list != null) {
                            for (Shortcode code : list) {
                    %>
                                <tr>
                                    <td width="10%"><%=count%></td>
                                   <td class="center"><%=code.getCodenumber()%></td>
                                    <td class="center"><%=networkHash.get(code.getNetworkuuid())%></td>

                                </tr>

                    <%
                                count++;
                            }
                        }// end 'if (list != null)'
                    %>

                </tbody>
            </table> 


        </div>
    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
