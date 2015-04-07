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
<%@page import="ke.co.tawi.babblesms.server.beans.status.Status"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>

<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO"%>

<%@page import="java.util.ArrayList"%>
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
    //Cache groupCache = mgr.getCache(CacheVariables.CACHE_GROUP_BY_UUID);
    Cache networkCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
    Cache accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);

    List<Status> list = new ArrayList<Status>();
    List<Group> contactsgrpList = new ArrayList<Group>();
    List<Network> networkList = new ArrayList<Network>();
  
    Network network;
    List keys;
    Status st;
    Group cg;

    Group cgroup = new Group();

    Account account = new Account();

    Element element;
    if ((element = accountsCache.get(username)) != null) {
        account = (Account) element.getObjectValue();
    }
    
    String accountuuid = account.getUuid();
    String statusuuid = account.getStatusuuid();

  /*  keys = groupCache.getKeys();
    for (Object key : keys) {

        element = groupCache.get(key);
        cgroup = (Group) element.getObjectValue();
        if (accountuuid.equals(cgroup.getAccountsuuid())) {
            contactsgrpList.add(cgroup);
        }
    }*/

    keys = statusCache.getKeys();
    for (Object sta : keys) {
        element = statusCache.get(sta);
        st = (Status) element.getObjectValue();
        list.add(st);
    }

    keys = networkCache.getKeys();
    for (Object key : keys) {
        element = networkCache.get(key);
        network = (Network) element.getObjectValue();
        networkList.add(network);
    }
%>

<jsp:include page="contactheader.jsp" />


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            Add contact
        </li>
    </ul>
</div>




<div class="row-fluid sortable">
    <div class="box span12">
        <div class="box-header well" data-original-title>
            <h2><i class="icon-edit"></i> Add Contacts</h2>
            
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
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
                    out.println("<p style='color:red;'>");
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
                    out.println("<p style='color:red;'>");
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
                    out.println("<p style='color:red;'>");
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
            <form class="form-horizontal" method="POST" action="addContact">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="name">Name</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="name" id="contname" type="text" name="contname">
                        </div>
                    </div>

                    <div class="control-group" id="phone">
                        <label class="control-label" for="phone">Phone Number</label>
                        <div class="controls" id="addphones1">
                            <input class="input-xlarge focused"  id="number" name="phonenum[]" type="text">
                            <button id='addphn'>+</button>
                            <select name="network[]" class="network" id="addphones">

                                <%
                                    int count = 1;
                                    if (networkList != null) {
                                        for (Network code : networkList) {
                                %>
                                    <option value="<%= code.getUuid()%>"><%= code.getName()%></option>
                                <%
                                            count++;
                                        }
                                    }
                                %>
                            </select>
                        </div>

                    </div>
                    <div class="control-group" id="mail">
                        <label class="control-label" for="email">Email</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="email" name="email[]" type="text" value="">
                            <button id='addemail'>+</button>
                        </div>
                    </div>

		<div class="control-group">
		 <label class="control-label">Description</label>
		<div class="controls">
		<textarea rows="3" cols="5" class="textarea" name="description" placeholder="Add description here..."></textarea>
		</div>	
		</div>
                    <br/>
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
	   
            <td class="center"><a href="#"><%=code.getName()%></a></td>
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
<button type="submit"  id ="add" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Add >> </button><br/><br/>
<button type="submit"  id = "remove" > << Remove </button>

<input type="hidden"  class ="groupsadded" name="groupsadded[]"  />
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
<br/><br/><br/><br/><br/><br/><br/><br/>
<div class="form-actions">
<input type="hidden" name="accountuuid" value="<%=accountuuid%>">
<input type="hidden" name="statusuuid" value="<%=statusuuid%>">
<div id="savecancelButtons">
<button type="submit" id="save" class="btn btn-primary">Save changes</button>
<button type="" id="cancel1" class="btn btn-primary" >&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;Cancel&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;</button>
 </div>
                    </div>
                </fieldset>
            </form>

        </div>

        <div class="box-content">
            <p>Upload csv file with format <code>name,phone,email,network</code></p>
            <c:set var="uploadErrStr" value="${requestScope[SessionConstants.ADMIN_UPLOAD_FILE_ERROR_KEY]}" />  <%--Access session variables using constants as keys--%>
            <c:set var="uploadSuccessStr" value="${requestScope[SessionConstants.ADMIN_UPLOAD_FILE_SUCCESS_KEY]}"/>
            <c:if test="${!empty uploadErrStr}">
                <p class="error">${uploadErrStr}</p> 
                <c:set var="uploadErrStr" value=""/>
            </c:if>
            <form class="form-horizontal" method="POST" action="uploadFile" name="uploadFile"
                  enctype="multipart/form-data">
                <fieldset>
                    <div class="control-group">
                        <input type="hidden" name="accountuuid" value="<%=accountuuid%>">
                        <label class="control-label" for="upload">Contact CSV</label>
                        <div class="controls">
                            <input class="input-xlarge focused"  id="upload" id="contname" type="file" name="upload">
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



<jsp:include page="footer.jsp" />

<script>
    $("document").ready(function() {
        //add more phone numbers upon clicking
        $("#addphone").click(function(e) {
            e.preventDefault();
            $("#phone").append("<div class='controls'> <input style='margin:5px 3px 0 0;' class='input-xlarge focused'  id='number' name='phonenum[]' type='text'><select name='network[]' class='network'></select></div>");
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

        //add more email click
        $("#addemail").click(function(e) {
            e.preventDefault();
            $("#mail").append("<div class='controls'> <input style='margin-top:5px;' class='input-xlarge focused'  id='email' name='email[]' type='text'> </div>");

        });



    });

</script>    
