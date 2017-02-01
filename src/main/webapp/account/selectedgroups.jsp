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
    Cache accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
    Cache contactsCache = mgr.getCache(CacheVariables.CACHE_CONTACTS_BY_UUID);
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
    Cache emailCache = mgr.getCache(CacheVariables.CACHE_EMAIL_BY_UUID);
    
    PhoneDAO phoneDAO = PhoneDAO.getInstance();
    EmailDAO emailDAO = EmailDAO.getInstance();
    ContactGroupDAO cgDAO = ContactGroupDAO.getInstance();
    GroupDAO gDAO = GroupDAO.getInstance();
    ContactDAO cdao = ContactDAO.getInstance();

    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, String> networkHash = new HashMap<String, String>();
    HashMap<String, Phone> phoneHash = new HashMap<String, Phone>();
    HashMap<String, Email> emailHash = new HashMap<String, Email>();

    
    Account account = new Account();

    Element element;
    if ((element = accountsCache.get(username)) != null) {
        account = (Account) element.getObjectValue();
    }
    

    Network network;
    
    Phone phone;
    Email email;

    List<Phone> phoneList = new ArrayList<Phone>();
    List<Network> networkList = new ArrayList<Network>();
    List<Contact> newcontlist = new ArrayList<Contact>(); 
    List<Email> emailList = new ArrayList<Email>();
    List<Group> contactGroupList = new ArrayList<Group>();
    List<Group> contactsgrpList = new ArrayList<Group>();
    List<Contact> contactPageList = new ArrayList<Contact>();  
     
     //GroupDAO gDAO=new GroupDAO();
     contactsgrpList = gDAO.getGroups(account); 

    List keys;
     
    keys = networksCache.getKeys();
    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash.put(network.getUuid(), network.getName());
    }
    List keys2;
     keys2 = networksCache.getKeys();
     for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkList.add(network);
    }    
           String name=request.getParameter("g");                       
    // end else
%>
<tbody id ="tablet" class="body-insert" >   
<%    
    if (contactsgrpList != null) {
         for (Group group : contactsgrpList) {

         int jcheck= name.indexOf(group.getName());
             if(jcheck!=-1){ %>
              <tr>
               <td >
               <input type="checkbox" id="remember" value="<%=group.getUuid()%>" name="groupselected[]"checked/>
               <a class ="alink"><%=group.getName()%></a>
                  </td>
       </tr>

        <% } 
         else { %>
             <tr>
               <td >
               <input type="checkbox" id="remember" value="<%=group.getUuid()%>" name="groupselected[]"/>
               <a class ="alink"><%=group.getName()%></a>
                  </td>
       </tr>

         <%}                                 
    
        }
    } 
    
    %>
   </tbody>