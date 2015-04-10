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
<%@page import="ke.co.tawi.babblesms.server.persistence.accounts.AccountsDAO"%>
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
		
           
            <a class="btn" href="#"  onclick="del()" title="Export contacts" type="submit" data-rel="tooltip">Export</a> 
     
  <button class="btn btn-default dropdown-toggle dropdown" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
more
  <span class="caret"></span>    
  </button>
<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
    <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Send email</a></li>
     <li role="presentation"><a role="menuitem" tabindex="-1" href="#">Sortby</a></li>  
</ul>

            &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<input type="text" name="search" onkeyup="showuser(this.value)"  placeholder = "type then click enter to search" autofocus>
        </div>
        <div class="box-content">
            
            <%                      
                String addErrStr = (String) session.getAttribute(SessionConstants.ADD_ERROR);
                String addSuccessStr = (String) session.getAttribute(SessionConstants.ADD_SUCCESS);
                
                String deleteErrStr = (String) session.getAttribute(SessionConstants.DELETE_ERROR);
                String deleteSuccessStr = (String) session.getAttribute(SessionConstants.DELETE_SUCCESS);
                
                String updateErrStr = (String) session.getAttribute(SessionConstants.UPDATE_ERROR);
                String updateSuccessStr = (String) session.getAttribute(SessionConstants.UPDATE_SUCCESS);
                

                if (StringUtils.isNotEmpty(addErrStr)) {
                    out.println("<p style = \"color:red;\">");
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
                    out.println("<p style = \"color:red;\">");
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
                    out.println("<p style = \"color:red;\">");
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
           <div id="showtext">
             <table class="table table-striped table-bordered " id="table_id">

                <thead>
                    <tr>
                        <th>*</th>
                        <th><a href="#">Name</a></th>
                        <th style = "border-right: none;"><a href="#">Number</a></th> 
			 <th style = "border-left: none;"> &nbsp;&nbsp;&nbsp;&nbsp;</th>                     
                        <th><a href="#">Email</a></th>
			<th style = "border-left: none;"> &nbsp;&nbsp;&nbsp;&nbsp;</th>
                        <th><a href="#">Group(s)</a></th>
			<th style = "border-left: none;"> &nbsp;&nbsp;&nbsp;&nbsp;</th>
                    </tr>
                </thead>   
       
		<tbody class="tblTest">
            <%
                if (contactPageList != null) {
                    int count1 =1;

                 for (Contact code:contactPageList) {
                
                    
                    emailList = emailDAO.getEmails(code);
                    contactGroupList = cgDAO.getGroups(code,account);
                    phoneList = phoneDAO.getPhones(code);
                  
                    // if (phoneList != null) {
                        // for (Phone code2 :phoneList) {
         %>
          
         <tr>

             
	<td width="10%"><%=contactCount%></td>
	 <td class="center"><a href="#"><%=code.getName()%></a></td>
		<% if (phoneList != null) {
                         //for (Phone code2 :phoneList) {
			if(phoneList.size() > 1) {
		%>
             <td class="center" style = "border-right: none;"><%=phoneList.get(0).getPhonenumber()%></td>&nbsp;
	     <td class="center" style = "border-left: none;"><%=phoneList.get(1).getPhonenumber()%></td>
			<%
				      }
		else {

			%>
		<td class="center" style = "border-right: none;"><%=phoneList.get(0).getPhonenumber()%></td>&nbsp;
	        <td class="center" style = "border-left: none;">&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<%
                     }
			
			if(phoneList.size()>1){
			for (int i = 1;i < phoneList.size();i++) {
				%>
			<td class="center" id = "hiddenphones" style = "display: none;"><%=phoneList.get(i).getPhonenumber()%></td>
			<%

			}
			}
		}
		%>
		
	     <% if ( emailList != null) {
                         
			if( emailList.size() > 1) {
		%>
             <td  style = "border-right: none;"><%= emailList.get(0).getAddress()%></td>&nbsp;
	     <td  style = "border-left: none;"><%= emailList.get(1).getAddress()%></td>
			<%
				      }
		else {

			%>
		<td  style = "border-right: none;"><%= emailList.get(0).getAddress()%></td>&nbsp;
	        <td  style = "border-left: none;">&nbsp;&nbsp;&nbsp;&nbsp;</td>
		<%
                     }
			
			if( emailList.size()>1){
			for (int i = 1;i <  emailList.size();i++) {
				%>
			<td  id = "hiddenemails" style = "display: none;"><%= emailList.get(i).getAddress()%></td>
			<%

			}
			}
		}
		%>
		
	     <%  
			 if (contactGroupList.size()>1){
		  %>
             <td class="center" style = "border-right: none;"><%=contactGroupList.get(0).getName()%></td>
	     <td class="center" style = "border-left: none;"><%=contactGroupList.get(1).getName()%></td>
             
                 <%  } 

             else if (contactGroupList.size()>0) {
		 %>
            <td class="center" style = "border-right: none;"><%=contactGroupList.get(0).getName()%></td>
	    <td class = "center"style = "border-left: none;">&nbsp </td>
            <% } 
     			else if (contactGroupList.size()<=0) { %>
                <td class = "center" style = "border-right: none;">&nbsp </td>
		<td class = "center" style = "border-left: none;">&nbsp </td>
 		 <% } 
  
	    if(contactGroupList.size()>0){
			for (int i = 0;i < contactGroupList.size();i++) {   
				%>
			<td class="center" id = "hiddengroups" style = "display: none;"><%=contactGroupList.get(i).getName()%></td>
			<%

			}
			}
			%>



  	<td style="display:none"><%=code.getDescription()%></td>
	<td style="display:none"><%=code.getUuid()%></td>


         </tr>

         <%     
                       //  }
                   //  }
                     contactCount++;
                 }
             }			
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
<div id="contactdiv">
<form class="form"  action = "editContact" method = "POST" id="contact" >
<!--onsubmit="return formValidator()"-->
<b>Contact Details</b>
<img src ="../img/close.png" style ="margin-top: 1px;margin-right: 2px;position:absolute;top:0;right:0;" id ="close">


<div class="control-group">
         <label class="control-label" for="name">Name</label>
             <div class="controls">
         <input class="input-xlarge focused"  id="paragraph_1" type="text" name="name">
             </div>
</div>

<div class="control-group" id="phone">
        <label class="control-label" for="phone">Phone Number</label>
        <div class="controls" id="addphones1">
        <input class="input-xlarge focused"  id="phone2"  name ="phone1[]" type="text">
        <button id='addphns'>+</button>
        <select name="network[]" class="network" id="addphones">

                                <%
                                    int count2 = 1;
                                    if (networkList != null) {
                                        for (Network code : networkList) {
                                %>
                                    <option value="<%= code.getUuid()%>"><%= code.getName()%></option>
                                <%
                                            count2++;
                                        }
                                    }
                                %>
          </select>
          </div>
</div>

<div class = "input_fields_wrap">
<button class = "add_field_button">+ </button>
</div>

<div class="control-group" id="mail">
<label class="control-label" for="email">Email</label>
<div class="controls" id = "addemails1">
<input class="input-xlarge focused"id="email" name="email[]" type="text" value="">
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
<div class="tablets">
    <table id="scroll" class="table table-striped table-bordered">
    <thead>
        <tr>
            <th>All Groups</th>
        </tr>
    </thead>
    <tbody id ="tablet">

	<%
	
	if (contactsgrpList != null) {
         for (Group code : contactsgrpList) {
	%>

        <tr>
	   
            <td class="center" id ="td2"><a href="#"><%=code.getName()%></a></td>
	    <td class="center" id="hideANDseek"><%=code.getUuid()%></td>
			
		
        </tr>
        <%   
	
	
    }
    } 
	
	%>
  
    </tbody>
</table>
<div id = "groupsform">
<br/><br/><br/>
<button type="submit"  id ="add1" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Add >> </button><br/><br/>
<button type="submit"  id = "remove2" > << Remove </button>

<input type="hidden"  class ="groupsadded" name="groupsadded[]"  />
<input type="hidden"  class ="groupsdeleted" name="groupsdeleted[]"  />
</div>
<table id="scroll1" class="table table-striped table-bordered">
    <thead>
        <tr>
            <th>Contact Groups</th>
        </tr>
    </thead>
    <tbody id = "resulttable">
    
	
  
    </tbody>
</table>

</div>
<!-- Group table ends here-->

<input type="hidden" id="uuid" name = "uuid" class="edit_area" />
<input type="hidden" name="statusuuid" value="<%=Contact.ACTIVE_STATUSUUID%>">
<br/><br/>
<div id="savecancelButtons2">
<button type="submit" id="save" class="btn btn-primary">Save changes</button>
<button type="" id="cancel1" class="btn btn-primary" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cancel&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
 </div>


<br/>

</form>
</div>


<!-- Contact Form  for the pop up ends-->

<jsp:include page="footer.jsp" />