
<%@page import="java.util.Date"%>
<%@page import="java.util.Locale"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.Random"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Shortcode"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.maskcode.ShortcodeDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.PurchaseHistory"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.purchaseHistory.PurchaseHistoryDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.AccountBalance"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.accounts.AccountBalanceDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="net.sf.ehcache.CacheManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="java.util.List"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>

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
                                
                                
                          //date formatting
                               
                          SimpleDateFormat sdf=new SimpleDateFormat("E MMM dd HH:mm:ss z Y");
                          Date currentdate;
                          String dateAsString = sdf.format(msk.getPurchasetime());
                          currentdate=sdf.parse(dateAsString);
                          SimpleDateFormat sdf2=new SimpleDateFormat("MMM dd,yyyy z yyyy");
                         // System.out.println(sdf2.format(currentdate));
                          
                          shortcode=shortcodeDAO.getShortcodeBycodeNumber(msk.getSource(),msk.getNetworkuuid());
                          
                          //System.out.println(shortcode);
                          if(shortcode==null){
                    %>
                    <tr>
                        <td width="10%"><%=count%></td>
                        <td width="10%"><%=msk.getSource()%></td>
                        <td class="center"><%=msk.getAmount()%></td>
                        <td class="center"><%=networkHash.get(msk.getNetworkuuid())%></td>
                        <!--<td class="center"><%=sdf2.format(currentdate)%></td>-->
			<td class="center"><%=msk.getPurchasetime()%></td>

                    </tr>

                    <%
                                count++;
                            }
                          }     
                        }
                    %>


                </tbody>
            </table> 


        </div>
    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
