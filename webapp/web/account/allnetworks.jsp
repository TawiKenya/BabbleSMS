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
<%@page import="ke.co.tawi.babblesms.server.beans.account.Status"%>
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
<%@page import="ke.co.tawi.babblesms.server.persistence.utils.CountUtils"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.pagination.contact.ContactPaginator"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.pagination.contact.ContactPage"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>



<%    
        Object object;    
    final int PAGESIZE = 15;     
    String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);  
    
    CacheManager mgr = CacheManager.getInstance();
    Cache accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
    Cache contactsCache = mgr.getCache(CacheVariables.CACHE_CONTACTS_BY_UUID);
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
    Cache emailCache = mgr.getCache(CacheVariables.CACHE_EMAIL_BY_UUID);
    
    PhoneDAO phoneDAO = PhoneDAO.getInstance();
    EmailDAO emailDAO = EmailDAO.getInstance();
    ContactGroupDAO cgDAO = ContactGroupDAO.getInstance();
    GroupDAO groupDAO = GroupDAO.getInstance();

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
     
    contactsgrpList = groupDAO.getGroups(account); 

    List keys;
     
    keys = networksCache.getKeys();
    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash.put(network.getUuid(), network.getName());
        networkList.add(network);
    }
    
        
    //pagination logic
    
    // Declaring variables for pagination logic
    CountUtils cUtils = CountUtils.getInstance();
    int allContact;
    ContactPaginator contactPaginator = new ContactPaginator(account.getUuid());
    ContactPage contactPage;
    List<Contact> contactPageList;

    String pageParam = (String) request.getParameter("first");
    String uuid = request.getParameter("uuid"); 
      
       //fetch the all contacts page
    if(StringUtils.equalsIgnoreCase(uuid, "empty")) {    
        allContact = cUtils.getContacts(account.getUuid());
        object = account;      
 
     //or fetch the selected group contacts page
    } else {
        allContact = cUtils.getContactInGroup(uuid);
        Group group = groupDAO.getGroup(uuid);
        object = group;                
    }
   

    int contactCount;
    int count = allContact;
    
    if (count == 0) { 
        contactPage = new ContactPage();
        contactPageList = new ArrayList<Contact>();
        contactCount = 0;

    } else {
        contactPage = (ContactPage) session.getAttribute("currentIncomingPage");       

        // Fetching the first contact page
        if (contactPage == null || StringUtils.equalsIgnoreCase(pageParam, "first")) {
            contactPage = contactPaginator.getFirstContactPage( object);

        }
        
        //  Fetching the last page
        if (StringUtils.equalsIgnoreCase(pageParam, "last")) {
            contactPage = contactPaginator.getLastContactPage( object , allContact);
        }

            // Fetching the previous page
       if (StringUtils.equalsIgnoreCase(pageParam, "prev")) {
            contactPage = contactPaginator.getPreviousContactPage( object , contactPage);
       }

       if (StringUtils.equalsIgnoreCase(pageParam, "second")) {
            contactPage = contactPaginator.getSecondContactPage( object);
          }
          
          // Fetching the next page 
       if(StringUtils.equalsIgnoreCase(pageParam, "next")) {
          
            contactPage = contactPaginator.getNextContactPage(object , contactPage);
        }

        session.setAttribute("currentIncomingPage", contactPage);

        contactPageList = contactPage.getContents();

        //logic to determine contactCount
       if (StringUtils.equalsIgnoreCase(pageParam, "last")){
              contactCount = (allContact - PAGESIZE) + 1;
       }
       else{
        contactCount = ((contactPage.getPageNum() - 1) * PAGESIZE) + 1;
           
    } 
    }// end else
    

            %>

        <div id="ContactsAdd">
        <span style="text-align:center;" >Page 
            <span class="pgNum" style="color:#317EAC;font-weight:bold;"><%=contactPage.getPageNum() %></span> 
            of 
            <span class="pgSize" style="color:#317EAC;font-weight:bold;"><%=contactPage.getTotalPage()%></span> 
            Page(s)
        </span>

            <div id="search-head"></div>
           <div id="showtext">
             <table class="table table-striped table-bordered " id="table_id">

                <thead>
                    <tr>
                        <th>*</th>
                        <th><a href="#">Name</a></th>
                        <th><a href="#">Phone</a></th>
                        <th><a href="#">Email</a></th>
                        <th><a href="#">Group</a></th>
                    </tr>
                </thead>   
       
        <tbody class="tblTest" >
                    
            <%
                if (contactPageList != null) {
                    int count1 = 1;

                 for (Contact contact : contactPageList) {
                                    
                    emailList = emailDAO.getEmails(contact);
                    contactGroupList = cgDAO.getGroups(contact);
                    phoneList = phoneDAO.getPhones(contact);                  
            %>
          
                    <tr>

                       <td width="5%"><%=contactCount%></td>
                       <td class="center"><a class="Zlink" href="#" title="click to edit details"><%=contact.getName()%></a></td>
                        
                    <% 
                        // Print Phone Numbers
                        if (phoneList != null) {
                            out.println("<td>");
                            for(Phone ph : phoneList) {
                                out.println(ph.getPhonenumber() + " (" +
                                    StringUtils.split( networkHash.get(ph.getNetworkuuid()) )[0] + 
                                    ")<br/>");           
                            }
                            out.println("</td>");

                        } else { // end 'if (phoneList != null)'
                            out.println("<td>&nbsp;</td>");
                        }


                        // Print Emails
                        if (emailList != null) {
                            out.println("<td>");
                            for(Email mail : emailList) {
                                out.println(mail.getAddress() + "<br/>");           
                            }
                            out.println("</td>");

                        } else { // end 'if ( emailList != null)'
                            out.println("<td>&nbsp;</td>");
                        }               
                      
                        // Print Groups
                        if (contactGroupList != null) {                        
                            out.println("<td>");
                            for(Group gr : contactGroupList) {
                                out.println(gr.getName() + "<br/>");           
                            }
                            out.println("</td>");

                        } else { // end 'if ( contactGroupList != null)'
                            out.println("<td>&nbsp;</td>");
                        }

                        out.println("<td style='display:none'>"+contact.getDescription()+"</td>");
                        out.println("<td style='display:none'>"+contact.getUuid()+"</td>");
                        
                     contactCount++;
                     out.println("</tr>");

                }// end 'for (Contact contact : contactPageList)'       

            }// end 'if (contactPageList != null)'
        %>
               
               
                </tbody>
            </table>            
            <span style="text-align:center;" >Page 
                        <span class="pgNum" style="color:#317EAC;font-weight:bold;" name="<%=contactPage.getPageNum() %>">
                        <%=contactPage.getPageNum() %></span> 
                        of 
                        <span class="pgSize" style="color:#317EAC;font-weight:bold;"  name="<%=contactPage.getTotalPage()%>">
                        <%=contactPage.getTotalPage()%></span> 
                        Page(s)
                    </span>
           </div> 
          </div>
                        
<!--affect the popup on contact page-->
<script src="../js/tawi/editcontact.js"></script>
<!--affect the popup on contact page-->
<script src="../js/tawi/editcontact_popup.js"></script>
