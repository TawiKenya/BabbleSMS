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
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.beans.creditmgmt.SMSPurchase"%>
<%@page import="ke.co.tawi.babblesms.server.beans.creditmgmt.ShortcodePurchase"%>
<%@page import="ke.co.tawi.babblesms.server.beans.creditmgmt.MaskPurchase"%>

<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.creditmgmt.SmsPurchaseDAO"%>

<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>
<%@page import="java.util.Date"%>
<%@page import="java.text.SimpleDateFormat"%>
<%@page import="java.util.LinkedList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedList"%>




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
    
   
   
    SmsPurchaseDAO smspurchaseDAO = SmsPurchaseDAO.getInstance();

 
   
    
    Account account = new Account();
    Element element;
	if ((element = accountsCache.get(username)) != null) {
        account = (Account) element.getObjectValue();
    }
     
   
    ShortcodePurchase shortcodepurchase;
    MaskPurchase maskpurchase;
    Mask mask;
    Shortcode shortcode;
    Network network;
    
                       //lis to hold  shortcode purchase details
    List <ShortcodePurchase> shortcodepurchaseList = new LinkedList<ShortcodePurchase>();
                        //this list to hold  mask purchase details
    List<MaskPurchase> maskpurchaseList = new LinkedList<MaskPurchase>();
                     //this hashmap to hold  mask name
    HashMap<String, String> maskHash = new HashMap<String, String>();
                   //this hashmap to hold  shortcode name
    HashMap<String, String> shortcodeHash = new HashMap<String, String>();
                 //newtwork hash, not yet implemented
    HashMap<String, String> networkHash = new HashMap<String, String>();
    HashMap<String, String> networkHash2 = new HashMap<String, String>();
    
     HashMap<String, String> networkHash3 = new HashMap<String, String>();
    HashMap<String, String> networkHash4 = new HashMap<String, String>();
    
    
    
    List<SMSPurchase> purchaseList = smspurchaseDAO.getPurchases(account);	
    
        //filter purchase
    for(SMSPurchase purchase : purchaseList) {
        if(purchase instanceof ShortcodePurchase) {   
            
            shortcodepurchaseList.add(  ((ShortcodePurchase) purchase )    );       
                
        } else {
                  
           maskpurchaseList.add(  ((MaskPurchase) purchase )    );
    }
    }//end of for each 
    
    List keys;
    
    //get mask details from chase, put them in hashmap
     keys = maskCache.getKeys();
    for (Object key : keys) {
        element = maskCache.get(key);
        mask = (Mask) element.getObjectValue();
        if (account.getUuid().equals(mask.getAccountuuid())) {
            maskHash.put(mask.getUuid(),mask.getMaskname()     );
        }
    }
    
     //get shortcode details from chase, put them in hashmap
    keys = shortcodesCache.getKeys();
    for (Object key : keys) {
        element = shortcodesCache.get(key);
        shortcode = (Shortcode) element.getObjectValue();
        if (account.getUuid().equals(shortcode.getAccountuuid())) {
            shortcodeHash.put(shortcode.getUuid(),shortcode.getCodenumber()   );
        }
    }
      //for mask, returns networkuuid
     keys = maskCache.getKeys();
    for (Object key : keys) {
        element = maskCache.get(key);
        mask = (Mask) element.getObjectValue();
        if (account.getUuid().equals(mask.getAccountuuid())) {
            networkHash.put(mask.getUuid(),mask.getNetworkuuid()     );
        }
    }
    //for mask, returns network name using networkHash above
    keys = networksCache.getKeys();
    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash2.put(network.getUuid(), network.getName());
    }


  //for shortcode, returns networkuuid
      keys = shortcodesCache.getKeys();
    for (Object key : keys) {
        element = shortcodesCache.get(key);
        shortcode = (Shortcode) element.getObjectValue();
       if (account.getUuid().equals(shortcode.getAccountuuid())) {
            networkHash3.put(shortcode.getUuid(),shortcode.getNetworkuuid()     );
        }
    }
    //for shortcode, returns network name using networkHash3 above
    keys = networksCache.getKeys();
    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash4.put(network.getUuid(), network.getName());
    }



   
   //date format
    SimpleDateFormat dateFormatter = new SimpleDateFormat("EEE, d MMM yyyy HH:mm:ss");
    SimpleDateFormat timezoneFormatter = new SimpleDateFormat("z");

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

<div class="col-lg-9 col-md-9 col-sm-5">		
   

       



            <div class="content_title"> 
                <h3>Mask</h3>

                <p>Below is the History of the Masks managed by your account:</p>
            </div>



            <table class="table table-striped table-bordered bootstrap-datatable datatable">
                <thead>
                    <tr>
                        <th>*</th>  
                        <th>Amount</th>   
                         <th>Date (<%= timezoneFormatter.format(new Date()) %> Time Zone)</th>                   
                        <th>Mask</th>
                        <th>Network</th>
                        
                         
                    </tr>                    
                </thead> 
                
                <tbody>
                
              
                
                
                    <%  
                                         
                        int count = 1;
                        if (maskpurchaseList != null) {
                                 for(MaskPurchase msk : maskpurchaseList) {
                      
                    %>
                                <tr>
                                    <td width="10%"><%=count%></td>    
                                    <td class="center"><%=msk.getCount()%></td>  
                                     <td class="center"><%=msk.getPurchaseDate()%></td>  
                                <td class="center"><%=maskHash.get( msk.getMaskuuid()  )%></td>
               <td class="center"><%=networkHash2.get(networkHash.get( msk.getMaskuuid()))%></td>        
                                </tr>

                    <%
                                count++;
                         
                                 }
                        }// end 'if (maskpurchaseList != null)'
                    %>


                </tbody>
            </table>  

            <p>&nbsp;&nbsp;&nbsp;</p>
                        

            <div class="content_title"> 
                <h3>Shortcode</h3>

                <p>Below is the History of the shortcodes managed by your account:</p>

            </div>



            <table class="table table-striped table-bordered bootstrap-datatable datatable">
                <thead>
                    <tr>
                        <th>*</th>
                         <th>Ammount</th>
                          <th>Date (<%= timezoneFormatter.format(new Date()) %> Time Zone)</th>
                        <th>ShortCodes</th>
                        <th>Network</th>
                       
                       
                    </tr>
                </thead>   
                <tbody>
                    <%
                        count = 1;
                         if (shortcodepurchaseList != null) {
                                 for(ShortcodePurchase code : shortcodepurchaseList) {    
                    %>
                                <tr>
                                    <td width="10%"><%=count%></td>
                                   <td class="center"><%=code.getCount()%></td> 
                                    <td class="center"><%=code.getPurchaseDate()%></td> 
                                 <td class="center"><%=shortcodeHash.get(code.getShortcodeuuid()  )%></td>
                <td class="center"><%=networkHash4.get(networkHash3.get(code.getShortcodeuuid()))%></td>        
                                </tr>

                    <%
                                count++;
                                 }
                        }// end 'if (shortcodepurchaseList != null)'
                    %>

                </tbody>
            </table> 


      
   

</div><!--/row-->
</div>

<jsp:include page="footer.jsp" />


