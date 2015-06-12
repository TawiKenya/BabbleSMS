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
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Email"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Group"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.EmailDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.networkcountDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.pagination.contact.ContactPaginator"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.pagination.contact.ContactPage"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.utils.CountUtils"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>

<%    
    
    final int PAGESIZE = 15;  
    int contactCount =1; 

    /** The following is for session management.    
    if (session == null) {
        response.sendRedirect("../index.jsp");
    }

    String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
    if (StringUtils.isEmpty(username)) {
        response.sendRedirect("../index.jsp");
    }

    session.setMaxInactiveInterval(SessionConstants.SESSION_TIMEOUT);
    response.setHeader("Refresh", SessionConstants.SESSION_TIMEOUT + "; url=../logout");
    **/
    CacheManager mgr = CacheManager.getInstance();
    Cache accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
    Cache contactsCache = mgr.getCache(CacheVariables.CACHE_CONTACTS_BY_UUID);
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
   // Cache emailCache = mgr.getCache(CacheVariables.CACHE_EMAIL_BY_UUID);
    
    //PhoneDAO phoneDAO = PhoneDAO.getInstance();
    //EmailDAO emailDAO = EmailDAO.getInstance();
    //ContactGroupDAO cgDAO = ContactGroupDAO.getInstance();
    //GroupDAO gDAO = GroupDAO.getInstance();
    //ContactDAO cdao = ContactDAO.getInstance();

    networkcountDAO ncDAO= new networkcountDAO();

    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, String> networkHash = new HashMap<String, String>();
    HashMap<String, String> contactHash = new HashMap<String, String>();
   //HashMap<String, Email> emailHash = new HashMap<String, Email>();

    String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
    Account account = new Account();

    Element element;
    if ((element = accountsCache.get(username)) != null) {
        account = (Account) element.getObjectValue();
    }
    

    Network network;
    Contact contact;
    //Phone phone;
    Email email;

    List<Phone> phoneList = new ArrayList<Phone>();
    List<Network> networkList = new ArrayList<Network>();
    //List<Contact> newcontlist = new ArrayList<Contact>(); 
    //List<Email> emailList = new ArrayList<>();
    //List<Group> contactGroupList = new ArrayList<Group>();
    //List<Group> contactsgrpList = new ArrayList<Group>();
    //List<Contact> contactPageList = new ArrayList<Contact>();  
     
     //GroupDAO gDAO=new GroupDAO();
     ///contactsgrpList = gDAO.getGroups(account); 

    List keys;
     
    keys = networksCache.getKeys();
    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash.put(network.getUuid(), network.getName());
        networkList.add(network);
    }

    List key2;
    key2=contactsCache.getKeys();
    for(Object key:key2){
      element=contactsCache.get(key);
      contact =(Contact)element.getObjectValue();
      contactHash.put(contact.getUuid(),contact.getName());
    } 

    
           String grpuuid=request.getParameter("grp"); 
           String nwkuuid=request.getParameter("nwk");

     /** if((grpuuid==null)||(nwkuuid==null)){
           response.sendRedirect("../account/contact.jsp");            
       }else{
           //phoneList=ncDAO.contactspernetwork(grpuuid,nwkuuid);*/ 
           phoneList=ncDAO.contactspernetwork("9bef62f6-e682-4efd-98e9-ca41fa4ef993","B936DA83-8A45-E9F0-2EAE-D75F5C232E78");          
      /** }*/

        
         
 
    // end else
%>
 
<table class="table table-striped table-bordered" id="contactgrp"> 

                <thead>
                        <tr>
                        <th>*</th>
                        <th><a href="#">Name</a></th>
                        <th><a href="#">Phone</a></th>                                               
                    </tr>
                </thead>   
       
    <tbody>   
                 <%
                  if(phoneList!=null){

                 for (Phone phone:phoneList) {                
         %>
         <tr>
             
             <td width="5%"> <%=contactCount%> </td>

            <td class="center"> <a href="#"><%=contactHash.get(phone.getContactUuid())%></a></td> 

            <td class="center"><%=phone.getPhonenumber()%><%=networkHash.get(phone.getNetworkuuid())%></td>  
  
            <td style="display:none"><%=phone.getStatusuuid()%></td>

            <td style="display:none"><%=phone.getUuid()%></td>


         </tr>

         <%         
                     contactCount++;
                 }
             }       
        %>
        </tbody>
        </table> 

              
       


      