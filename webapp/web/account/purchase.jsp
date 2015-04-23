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
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Shortcode"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.maskcode.ShortcodeDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.PurchaseHistory"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.purchaseHistory.PurchaseHistoryDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.AccountBalance"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.accounts.AccountBalanceDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>

<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Random"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>
<%@page import="java.text.SimpleDateFormat"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
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
    CacheManager mgr = CacheManager.getInstance();
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);

    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, String> networkHash = new HashMap<String, String>();
    Element element;
    Network network;
    Shortcode shortcode;
    List keys;
    
    
    keys = networksCache.getKeys();
    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash.put(network.getUuid(), network.getName());
    }

    PurchaseHistoryDAO pchsDAO = PurchaseHistoryDAO.getInstance();
    List<PurchaseHistory> psclist = pchsDAO.getPurchaseHistoryByAccount(accountuuid);

    AccountBalanceDAO accDAO = AccountBalanceDAO.getInstance();
    List<AccountBalance> acclist = accDAO.getClientBalanceByAccount(accountuuid);

    ShortcodeDAO shortcodeDAO=ShortcodeDAO.getInstance();
    
    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy");
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
        <div class="box-header well" data-original-title>
            <a class="btn" href="#" title="refresh page" data-rel="tooltip"><i class="icon-refresh"></i> Refresh</a>                  
            <a class="btn" href="#" title="delete message" data-rel="tooltip"><i class="icon-trash"></i> Delete</a>  
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">


            <div class="content_title">
                <h3>Account Balance</h3>
                <p>Below is the summary of your account Balance:</p>
            </div>
            <table class="table table-striped table-bordered bootstrap-datatable datatable">

                <thead>
                    <tr>
                        <th>*</th>

                        <th>Mask</th>
                        <th>Credit</th>
                        <th>Network</th>
                        <th>actions</th>
                    </tr>
                </thead> 

                <tbody>
                    <%                        int count = 1;
                        if (acclist != null) {
                            for (AccountBalance code : acclist) {
                                
                                //random number i.e credit
                                Random rn = new Random();
                                int credit = rn.nextInt(10) + 1;
                    %>
                    <tr>

                        <td width="10%"><%=count%></td>
                        <td class="center"><%=code.getOrigin()%></td>
                        <td class="center"><%=code.getBalance()%></td>
                        <td class="center"><%=networkHash.get(code.getNetworkuuid())%></td>
                        <td class="center">
                            <a class="btn btn-success" href="#">
                                <i class="icon-zoom-in icon-white"></i>  
                                View                                            
                            </a>


                        </td>


                    </tr>

                    <%
                                count++;
                            }
                        }
                    %>
                </tbody>

            </table> 



            <div class="content_title"> 
                <h3>Purchase History</h3>
                <p>Below is the summary of purchase history for your account:</p>
            </div>
            <table class="table table-striped table-bordered bootstrap-datatable datatable">
                <thead>
                    <tr>
                        <th>*</th>

                        
                        <th>For</th>
                        <th>Amount</th>
                        <th>Network</th>
                        <th>Date</th>
                    </tr>
                </thead>   
                <tbody>
                    <%
                        count = 1;

                        if (psclist != null) {
                            for (PurchaseHistory msk : psclist) {
                                                                                    
                            shortcode = shortcodeDAO.getShortcodeBycodeNumber(msk.getSource(), msk.getNetworkuuid());
                          
                    %>
                            <tr>
                                <td width="10%"><%=count%></td>
                                <td width="10%"><%=msk.getSource()%></td>
                                <td class="center"><%=msk.getAmount()%></td>
                                <td class="center"><%=networkHash.get(msk.getNetworkuuid())%></td>
                                <td class="center"><%=dateFormatter.format(msk.getPurchasetime())%></td>

                            </tr>

                    <%
                            count++;

                            }// end 'for (PurchaseHistory msk : psclist)'
                        }// end 'if (psclist != null)'
                    %>


                </tbody>
            </table> 


        </div>
    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
