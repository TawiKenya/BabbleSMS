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

<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Group"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Status"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO"%>

<%@page import="java.util.List"%>
<%@page import="java.util.ArrayList"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="net.sf.ehcache.CacheManager"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>

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
    
%>

<jsp:include page="messageheader.jsp" />
<script type="text/javascript">
  function cancelme(){
    alert('clickedd');
    window.location.replace('http://www.w3schools.com');
  }
</script>


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">add template</a>
        </li>
    </ul>
</div>




<div class="row-fluid sortable">
    <div class="box span12">
        <div class="box-header well" data-original-title>
            <h2><i class="icon-edit"></i> Add message templates</h2>
            
        </div>
        <div class="box-content">
            <form class="form-horizontal" method="POST" action="AddTemplate">
                <fieldset>
                    
                        <label class="control-label" for="name">Template Title</label>
                        
                            <input class="input-xlarge focused"  id="title" id="contname" type="text" name="title"><br>
                            </br>
                            </br>
                            <p><label class="control-label" for="message">Template Contents:</label> <textarea cols="20" rows="6" class="input-xlarge focused"  id="title" id="contname" name="contents"></textarea></p>

                       
                   
                    <div class="form-actions">
                        <input type="hidden" name="accountuuid" value="<%=accountuuid%>">
                        <button type="submit" class="btn btn-primary">Save changes</button>
                        <button onclick="window.location.replace('http://www.w3schools.com');">Cancel</button>
                    </div>
               
            </form>

        </div>
    </div><!--/span-->

</div><!--/row-->



<jsp:include page="footer.jsp" />

<script>
   $("document").ready(function(){
       //add more phone numbers upon clicking
       $("#addphone").click(function(e){
         e.preventDefault();  
         $("#phone").append("<div class='controls'> <input style='margin:5px 3px 0 0;' class='input-xlarge focused'  id='number' name='phonenum[]' type='text'><select name='network[]' class='network'></select></div>");
             //add values to network thruogh ajax call
             //get url path first
             var basepath=window.location.protocol + "//" + window.location.host + "/";
             var pathArray = window.location.pathname.split( '/' );
             var url=basepath+pathArray[1]+'/'+pathArray[2]+'/ajaxaddcontact.jsp';
             $.ajax({
                   url: url,
                   type:"GET",
                   cache: false,
                   }).done(function ( data ) {
                    
                    $(".network").append(data);
                   });//ajax call ends here
       }); 
       
       //add more email click
       $("#addemail").click(function(e){
         e.preventDefault();  
         $("#mail").append("<div class='controls'> <input style='margin-top:5px;' class='input-xlarge focused'  id='email' name='email[]' type='text'> </div>");
         
       }); 
       
      
       
   });       
   
    
</script>    