
<%@page import="ke.co.tawi.babblesms.server.beans.account.PurchaseHistory"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.purchaseHistory.PurchaseHistoryDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Mask"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Shortcode"%>
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
    Cache shortcodesCache = mgr.getCache(CacheVariables.CACHE_SHORTCODE_BY_UUID);
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
    Cache maskCache = mgr.getCache(CacheVariables.CACHE_MASK_BY_UUID);

    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, String> networkHash = new HashMap<String, String>();
    Element element;
    Network network;
    Shortcode shortcode;
    Mask mask;

    List<Shortcode> list = new ArrayList();
    List<Mask> masklist = new ArrayList();
    List keys;

    keys = shortcodesCache.getKeys();
    for (Object key : keys) {
        element = shortcodesCache.get(key);
        shortcode = (Shortcode) element.getObjectValue();
        if (accountuuid.equals(shortcode.getAccountuuid())) {

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

        if (accountuuid.equals(mask.getAccountuuid())) {

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
                    <%                        int count = 1;
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
                        }
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
                        }
                    %>

                </tbody>
            </table> 



        </div>
    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
