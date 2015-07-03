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
<%@page import="ke.co.tawi.babblesms.server.beans.status.Status"%>
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
    final int PAGESIZE = 15;   

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
    int allContact = cUtils.getContacts(account.getUuid());
    ContactPaginator contactPaginator = new ContactPaginator(account.getUuid());
    ContactPage contactPage;
    List<Contact> contactPageList;
    String pageParam = (String) request.getParameter("first");
    int contactCount;
    int count = allContact;
    String headerreference = request.getHeader("referer");

    if (count == 0) { 
        contactPage = new ContactPage();
        contactPageList = new ArrayList<Contact>();
        contactCount = 0;

    } 
    
    else {
        contactPage = (ContactPage) session.getAttribute("currentIncomingPage");
       

        // Fetching the first contact page
        if (contactPage == null || StringUtils.equalsIgnoreCase(pageParam, "first") || !StringUtils.endsWith(headerreference, "contact.jsp")) {
            contactPage = contactPaginator.getFirstContactPage( account);

        }
        
        //  Fetching the last page
        if (StringUtils.equalsIgnoreCase(pageParam, "last")) {
            contactPage = contactPaginator.getLastContactPage( account , allContact);
        }

            // Fetching the previous page
       if (StringUtils.equalsIgnoreCase(pageParam, "<<")) {
            contactPage = contactPaginator.getPreviousContactPage( account , contactPage);
       }

       if (StringUtils.equalsIgnoreCase(pageParam, "second")) {
            contactPage = contactPaginator.getSecondContactPage( account);
          }
          
          // Fetching the next page 
       if(StringUtils.equalsIgnoreCase(pageParam, ">>")) {
          
            contactPage = contactPaginator.getNextContactPage(account , contactPage);
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
<jsp:include page="contactheader.jsp" />


<!--<link rel="stylesheet" type="text/css" href="../css/grouptable.css">-->


<div>
    <ul class="breadcrumb">
        <li>
            <a href="index.jsp">Home</a> <span class="divider">/</span>
        </li>
        <li>
          All contacts
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        <div class="box-header well" data-original-title>
		 <h2><i class="icon-align-left"></i> All Contacts Listings</h2>           
            
        </div>
        <div class="box-content">

        <a class="btn" href="#"  onclick="del()" title="Export contacts" type="submit" data-rel="tooltip">Export</a> 
     
  <button class="btn btn-default dropdown-toggle dropdown" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
more
  <span class="caret"></span>    
  </button>
<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Send email</a></li>
     <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Sortby</a></li>  
</ul>

            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Search:&nbsp;<input type="text" name="search" onkeyup="showuser(this.value)"  placeholder = "type then click enter to search" autofocus="autofocus">
            
            <%                      
                String addErrStr = (String) session.getAttribute(SessionConstants.ADD_ERROR);
                String addSuccessStr = (String) session.getAttribute(SessionConstants.ADD_SUCCESS);
                
                String deleteErrStr = (String) session.getAttribute(SessionConstants.DELETE_ERROR);
                String deleteSuccessStr = (String) session.getAttribute(SessionConstants.DELETE_SUCCESS);
                
                String updateErrStr = (String) session.getAttribute(SessionConstants.UPDATE_ERROR);
                String updateSuccessStr = (String) session.getAttribute(SessionConstants.UPDATE_SUCCESS);
                

                if (StringUtils.isNotEmpty(addErrStr)) {
                     out.println("<p class='error'>");
                    out.println("Form error: " + addErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADD_ERROR, null);
                }

                if(StringUtils.isNotEmpty(addSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(addSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADD_SUCCESS, null);
                }
                
                if (StringUtils.isNotEmpty(deleteErrStr)) {
                     out.println("<p class='error'>");
                    out.println("Form error: " + deleteErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.DELETE_ERROR, null);
                }

                if (StringUtils.isNotEmpty(deleteSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(deleteSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.DELETE_SUCCESS, null);
                }
                
                if (StringUtils.isNotEmpty(updateErrStr)) {
                     out.println("<p class='error'>");
                    out.println("Form error: " + updateErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.UPDATE_ERROR, null);
                }

                if (StringUtils.isNotEmpty(updateSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(updateSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.UPDATE_SUCCESS, null);
                } 

            %>
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
                    contactGroupList = cgDAO.getGroups(contact, account);
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
           </div>  


        </div>
    </div><!--/span-->



</div><!--/row-->
<div style ="margin-left:30%; width:50%;">
<form action = "contact.jsp" method = "POST">

        <% 
        if(contactPage.isFirstPage()){
    %>
        <input type = "submit" value = "first" name = "first" disabled ="disabled">
        <input type = "submit" value = "<<" name = "first" disabled = "disabled">
        <!--<input type = "submit" value = "second" name = "first"> -->  
        <input type = "submit" value = ">>" name = "first">  
        <input type = "submit" value = "last" name = "first">
    <%
    }

        if(contactPage.isLastPage()){
    %>
        <input type = "submit" value = "first" name = "first">
        <input type = "submit" value = "<<" name = "first">
       <!-- <input type = "submit" value = "second" name = "first"> -->  
       <input type = "submit" value = ">>" name = "first" disabled ="disabled">  
       <input type = "submit" value = "last" name = "first"disabled ="disabled">
    <%
        }
 
     if(!contactPage.isFirstPage() && !contactPage.isLastPage()){
    %>
       <input type = "submit" value = "first" name = "first">
       <input type = "submit" value = "<<" name = "first">
       <!-- <input type = "submit" value = "second" name = "first"> -->  
       <input type = "submit" value = ">>" name = "first">  
       <input type = "submit" value = "last" name = "first">
       <%
          }
        %>
</form>
</div> 



<!-- Contact Form  for the pop up starts-->
<div id="contactdiv"style="display:none;">
<form class="form"  onsubmit="return formValidator()" action = "editcontact" method = "POST" id="contact" >
<!--onsubmit="return formValidator()"-->
<b> Edit Contact Details</b>
<p style ="margin-top: 1px;margin-right: 2px;position:absolute;top:1%;right:1%; color:red; font-size:20px;" id ="close">x</p>


<div class="control-group">
         <label class="control-label" for="name">Name</label>
             <div class="controls">
         <input class="input-xlarge focused"  id="paragraph_1" type="text" name="name" required>
             </div>
</div>

<div class="control-group" id="phone">
        <label class="control-label" for="phone">Phone Number</label>
        <div class="controls" id="addphones1">
        <input class="input-xlarge focused"  id="phone2"  name ="phone1[]" type="text" required>
        <button id='addphns'>+</button>
        <select name="network[]" class="network" id="addphones">

                                <%
                                    int count2 = 1;
                                    if (networkList != null) {
                                        for (Network netwk : networkList) {
                                %>
                                    <option value="<%= netwk.getUuid()%>"><%= netwk.getName()%></option>
                                <%
                                            count2++;
                                        }
                                    }
                                %>
          </select>
          </div>
</div>
<div class="control-group" id="mail">
<label class="control-label" for="email">Email</label>
<div class="controls" id = "addemails1">
<input class="input-xlarge focused"id="email" name="email[]" type="text" value="" >
<button id="addemail">+</button>
</div>
</div>


<div class="control-group">
<label class="control-label">Description</label>
<div class="controls">
<textarea rows="2" cols="9" style="width:50%;" class="textarea" id = "textarea" name="description" ></textarea>
</div>  
</div>

<!-- Group table here-->
<div class="table-save">
<div id="scrolledit">
    <table id="scroll" class="table table-striped table-bordered">
    <thead class="head-insert">
        <tr>
            <th> Choose A Group</th>
        </tr>
    </thead>
    <!--table body is inserted here-->
</table>
</div>
<!-- Group table ends here-->

<input type="hidden" id="uuid" name = "uuid" class="edit_area" />
<input type="hidden" name="statusuuid" value="<%=Status.ACTIVE%>">
<br/><br/>
<div id="savecancelButtons2">
<button type="submit" id="save" class="btn btn-primary">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Save&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
<button type="" id="cancel1" class="btn btn-primary" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cancel&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
 </div>
</div>

<br/>

</form>
</div>

<!-- Contact Form  for the pop up ends-->


<jsp:include page="footer.jsp" />
