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
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Group"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.status.Status"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO"%>
<%@page import="ke.co.tawi.babblesms.server.servlet.upload.ContactUpload"%>

<%@page import="java.util.LinkedList"%>
<%@page import="java.util.List"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>


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
    Cache statusCache = mgr.getCache(CacheVariables.CACHE_STATUS_BY_UUID);
    Cache networkCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
    Cache accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
    
    List<Group> contactsgrpList = new LinkedList<Group>();
    List<Network> networkList = new LinkedList<Network>();
  
    List keys;
    Group cg;
    String notNull=null;

    Group cgroup = new Group();
    
    Account account = new Account();

    Element element;
    if ((element = accountsCache.get(username)) != null) {
        account = (Account) element.getObjectValue();
    }
    
    String accountuuid = account.getUuid();
    
    GroupDAO groupDAO = new GroupDAO();
    contactsgrpList = groupDAO.getGroups(account);

    keys = networkCache.getKeys();
    for (Object key : keys) {
        element = networkCache.get(key);
        networkList.add((Network) element.getObjectValue());
    }
%>

<jsp:include page="contactheader.jsp" />


<div>
    <ul class="breadcrumb">
        <li>
            <a href="index.jsp">Home</a> <span class="divider">/</span>
        </li>
        <li>
            Add Contact
        </li>
    </ul>
</div>

<div class="row-fluid sortable">
    <div class="box span12">
      
        <div class="box-content">
            <%                
                String addErrStr = (String) session.getAttribute(SessionConstants.ADD_ERROR);
              
                String addSuccessStr = (String) session.getAttribute(SessionConstants.ADD_SUCCESS);
              
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
            %>
            
            <form class="form-horizontal" method="POST" action="addContact">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="name">Name</label >
                        <div class="controls">
                            <input class="input-xlarge focused"  id="name" id="name" type="text" name="name" required>
                        </div>
                    </div>

                    <div class="control-group" id="phone">
                        <label class="control-label" for="phone">Phone Number</label>
                        <div class="controls" id="addphones1">
                            <input class="input-xlarge focused"  id="number" name="phones" type="text" onkeypress='return validateQty(event);' required>
                            <button id='addphns'>+</button>
                            
                            <select name="networks" class="network" id="addphones">
                                <%                                    
                                    for (Network code : networkList) {
                                %>
                                        <option value="<%= code.getUuid()%>"><%= code.getName()%></option>
                                <%                                         
                                    }
                                %>
                            </select>
                        </div>
                    </div>
                            
                    <div class="control-group" id="mail">
                        <label class="control-label" for="email">Email</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="email" name="emails" type="text" >
                            <button id="addemail">+</button>
                        </div>
                    </div>

		<div class="control-group">
                    <label class="control-label">Description</label>
                    <div class="controls">
                        <textarea rows="3" cols="5" class="textarea" name="description" placeholder="Add description here..." ></textarea>
                    </div>	
		</div>                
        <!-- Group table starts here-->
        <div class="control-group">
        <label class="control-label">Groups</label>
        <div id="tableselect11" class="controls" >
            <table id="groupsel" class="table table-striped table-bordered">
            <%
                if (contactsgrpList != null) {
                    for (Group group : contactsgrpList) {
            %>
                        <tr>
                            <td class="center" >
                                <input type="checkbox" id="remember" value="<%=group.getUuid()%>" name="groups" />
                                <a groupuuid="<%=account.getUuid()%>" ><%=group.getName()%></a>
                            </td>
                        </tr>
            <%   
                    }// end 'for (Group group : contactsgrpList)'
                }// end 'if (contactsgrpList != null)' 
            %>
            </table>
        </div>
        </div>
        <!-- Group table ends here-->

<div class="form-actions">

<div id="savecancelButtons">
<button type="submit" id="save" class="btn btn-primary">&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Save &nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
<button type="" id="cancel1" class="btn btn-primary" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cancel&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
 </div>
                    </div>
                </fieldset>
            </form>

        </div>

        <div class="box-content1">
            <h3>
                <%
                    if(StringUtils.isNotBlank((String)session.getAttribute( ContactUpload.UPLOAD_FEEDBACK ))) {
                    String servletResponse =(String)session.getAttribute( ContactUpload.UPLOAD_FEEDBACK );
                        out.println(servletResponse);
                        //used by javascript 
                        if(servletResponse!=null){notNull=servletResponse.substring(0,10);
                        }                    
                        session.setAttribute(ContactUpload.UPLOAD_FEEDBACK, null);
                    }
                %>  
            </h3>
            <p>Upload CSV file with format <code>name, phone, network</code></p>
            
            
            <form class="form-horizontal" method="POST" action="uploadContacts" name="uploadContacts" enctype="multipart/form-data">
                <fieldset>
                    <div class="control-group" id="javascript" javaScriptCheck="<%=notNull%>">
                                              
                         
                        <label class="control-label" for="upload">Contact CSV</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="upload" id="contname" type="file" name="upload" required>



                            <div id="clink">
                        <div class="imgload">
                      <a class="load" href="../files/Contacts.csv" title="Download a sample csv file" download>
                      Download<br> a sample<br> CSV file
                      <img src="../img/csv.png"  alter="CSV file"></a>
                      </div>

                      <div class="imgload">
                      <a  href="../files/Contacts.xlsx" title="Download a sample excelsheet file">
                      Download<br> a sample<br> MS Excel file
                      <img src="../img/excel.png"  alter="Excel Sheet File"></a>
                      </div>
                        </div>



                        </div>
                        <label class="control-label" for="upload">Add Contacts To a Group (Optional)</label>
                        <div class="controls">
                        <div id="tableselect">
                                 <table id="groupsel" class="table table-striped table-bordered">
                         <%
                                                if (contactsgrpList != null) {
                                                    for (Group code : contactsgrpList) {
                                            %>

                                                    <tr>
                                                        <td class="center" >
                                                        <input type="checkbox" id="remember" value="<%=code.getUuid()%>" name="groupselected"/>
                                                        <a groupuuid="<%=account.getUuid()%>" ><%=code.getName()%></a>
                                                        </td>
                                                    </tr>
                                                    
                                                <%   
                                                    }// end 'for (Group code : contactsgrpList)'
                                                }// end 'if (contactsgrpList != null)' 
                                            %>
                                        </table>
                                </div>
                           
                        </div>

                        
                    </div>

                    <div class="form-actions">                        
                        <button type="submit" class="btn btn-primary">Upload File</button>
                        <button class="btn">Cancel</button>
                    </div>
                </fieldset>
            </form>

        </div>
       
        
    </div><!--/span-->

</div><!--/row-->

<!--scroll to the bottom the page if file upload is done -->
<script type="text/javascript">
$("document").ready(function() {    
        var check1 = $("#javascript").attr("javaScriptCheck");
        if(check1.length>4){
           $("html, body").animate({ scrollTop: $(document).height() });
        }   
    });
</script>


<jsp:include page="footer.jsp" />

<script>
    $("document").ready(function() {
        //add more phone numbers upon clicking
        $("#addphone").click(function(e) {
            e.preventDefault();
            $("#phone").append("<div class='controls'> <input style='margin:5px 3px 0 0;' class='input-xlarge focused'  id='number' name='phones' type='text'><select name='networks' class='network'></select></div>");
            //add values to network thruogh ajax call
            //get url path first
            var basepath = window.location.protocol + "//" + window.location.host + "/";
            var pathArray = window.location.pathname.split('/');
            var url = basepath + pathArray[1] + '/' + pathArray[2] + '/ajaxaddcontact.jsp';
            $.ajax({
                url: url,
                type: "GET",
                cache: false,
            }).done(function(data) {

                $(".network").append(data);
            });//ajax call ends here
        });

        $("#deleteclone").click(function(e){
        e.preventDefault();
        $(this).parent().hide();
     });

    });

</script>    

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