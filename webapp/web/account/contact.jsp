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
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);    
    
    GroupDAO groupDAO = GroupDAO.getInstance();

    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, String> networkHash = new HashMap<String, String>();    

    
    Account account = new Account();

    Element element;
    if ((element = accountsCache.get(username)) != null) {
        account = (Account) element.getObjectValue();
    }
    
    Network network;    
    
    List<Network> networkList = new ArrayList<Network>();   
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
	
    
       
    
%> 
<jsp:include page="contactheader.jsp" />

<!--manages the table on contacts page-->
<script src="../js/tawi/contactgrpselected.js"></script>

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


    String success = (String)session.getAttribute("success");
    String fail = (String)session.getAttribute("fail");

    if (StringUtils.isNotEmpty(fail)) {
                    out.println("<p class='error'>");
                    out.println(fail);
                    out.println("</p>");
                    session.setAttribute("fail", null);
                }

                if (StringUtils.isNotEmpty(success)) {
                    out.println("<p style='color:green'>");
                    out.println(success);
                    out.println("</p>");
                    session.setAttribute("success", null);
                }
          %>
                  
          <h4>
          &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Search by Name:&nbsp;<input type="text" name="search" onkeyup="showuser(this.value)"  placeholder = "type then click enter to search" autofocus="autofocus"> &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;


          Search by Group:&nbsp;             
            <select class="groupselect" onclick="Chromecheck()">
             <option class="grp" value="empty" name="All Contacts" >All Contacts</option>
                 <% if (contactsgrpList != null) {                        
                    out.println("<td>");                   
                    for(Group gr : contactsgrpList) { %>
                  <option class="grp" value="<%=gr.getUuid()%>" name="<%=gr.getName()%>" ><%=gr.getName()%></option>          
                          <%}                            
                 } else {%> 
                 <option >No groups available</option>
                 <% } %>                          
                           
            </select>
           
          </h4>
           <div id="header-display"></div>
                

            <!--the new page is appended here-->
              
           </div>    

            


        </div>
    </div><!--/span-->



</div><!--/row-->


<div style ="margin-left:30%; width:50%;">
                   <span id="prev" name="<%=account.getUuid()%>" > 
                <span class="icon-fast-backward"></span>
                   &nbsp;<a>Prev</a>&nbsp;&nbsp;&nbsp;
                   </span>
                  &nbsp;&nbsp;&nbsp;

                   <span id="next" > 
                   <a>Next</a>&nbsp;
                <span class="icon-fast-forward"></span>
                   </span>
                    &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                   <input id="first"  value="First" type="submit" style="color:#fff; background-color:#555;">
                   &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;
                   <input id="last"  value="Last" type="submit" style="color:#fff; background-color:#555;">                      

                   </div>


        


<!-- Contact Form  for the pop up starts-->
<div id="contactdiv"style="display:none;">
<form class="form" action = "editcontact" method = "POST" id="contact" >
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
        <input class="input-xlarge focused"  id="phone2"  name ="phone1[]" type="text" onkeypress='return validateQty(event);' required>
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

<script type="text/javascript">    
    function validateQty(event) {
          var key = window.event ? event.keyCode : event.which; 
           //choose the only keys allowed..
       if (   event.keyCode === 8 //backspace key
           || event.keyCode === 46  //delete key
           || event.keyCode === 37   //left arrow key
           || event.keyCode === 39  //right arrow key
           || key === 0 //right and left arrow keys i mozilla
           || key === 32  //whitespace key
           || key === 45 //hypen key
           || key === 118 // for mozilla (ctrl + v).|| (key === 17 && key === 67) //(ctrl + c)
           || (key > 47 && key < 58 )//from 0 to 9
           ) {
               return true;

         }        
        else return false;
      }
</script>


<jsp:include page="footer.jsp" />
