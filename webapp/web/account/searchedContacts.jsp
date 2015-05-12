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
<%@page import="ke.co.tawi.babblesms.server.persistence.items.network.NetworkDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.accounts.AccountDAO"%>
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
%>        
 
<!DOCTYPE html>
<html>
    <head>
<meta charset="utf-8">
        <title>Babble SMS :: Contacts</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content=".">
        <meta name="author" content="eugene" >
   <link href="http://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css" rel="stylesheet">
      <script src="http://code.jquery.com/jquery-1.10.2.js"></script>
<script src="jquery_popup.js"></script>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.11.0/jquery.min.js"></script>
      <script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
 <link rel="stylesheet" href="poup.css" />

        <!-- The styles -->
        <!--<link id="bs-css" href="css/bootstrap-cerulean.css" rel="stylesheet">-->
        <link href="../css/bootstrap-cerulean.css" rel="stylesheet">
        <style type="text/css">
            body {
                padding-bottom: 40px;
            }
            .sidebar-nav {
                padding: 9px 0;
            }
            .breadcrumb
            {
                z-index:999;
            }
        </style>
        <!-- jQuery -->
	<!-- DataTables CSS -->
        <link rel="stylesheet" type="text/css" href="../DataTables-1.10.5/media/css/jquery.dataTables.css">

        <!-- jQuery -->
        <script type="text/javascript" charset="utf8" src="../DataTables-1.10.5/media/js/jquery.js"></script>

        <!-- DataTables--> 
        <script type="text/javascript" charset="utf8" src="../DataTables-1.10.5/media/js/jquery.dataTables.js"></script>



         <!-- <script type="text/javascript" charset="utf8" src="//code.jquery.com/jquery-1.11.1.min.js"></script>-->
         <!-- <script type="text/javascript" charset="utf8" src="//cdn.datatables.net/1.10.5/js/jquery.dataTables.min.js"></script>-->




        <!-- jQuery -->
        <script src="../js/jquery-1.7.2.min.js"></script>
	<script src="../js/jquery.jeditable.js"></script>
	<!--<script src="../js/jquery_popup.js"></script>-->
	<script src="../js/editcontact1.js"></script>
	<script src="../js/editcontact_popup1.js"></script>
	<script src="../js/groupstable.js"></script>
        <link href="../css/bootstrap-responsive.css" rel="stylesheet">
        <link href="../css/charisma-app.css" rel="stylesheet">
	<link href="../css/poup.css" rel="stylesheet">
        <link href="../css/jquery-ui-1.8.21.custom.css" rel="stylesheet">
        <link href='../css/fullcalendar.css' rel='stylesheet'>
        <link href='../css/fullcalendar.print.css' rel='stylesheet'  media='print'>
        <link href='../css/chosen.css' rel='stylesheet'>
	<link href='../css/grouptable.css' rel='stylesheet'>
        <link href='../css/uniform.default.css' rel='stylesheet'>
        <link href='../css/colorbox.css' rel='stylesheet'>
        <link href='../css/jquery.cleditor.css' rel='stylesheet'>
        <link href='../css/jquery.noty.css' rel='stylesheet'>
        <link href='../css/noty_theme_default.css' rel='stylesheet'>
        <link href='../css/elfinder.min.css' rel='stylesheet'>
        <link href='../css/elfinder.theme.css' rel='stylesheet'>
        <link href='../css/jquery.iphone.toggle.css' rel='stylesheet'>
        <link href='../css/opa-icons.css' rel='stylesheet'>
        <link href='../css/uploadify.css' rel='stylesheet'>
        <link href='../css/template.css' rel='stylesheet'>

        <!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

        <!-- The fav icon -->
        <link rel="shortcut icon" href="img/favicon.ico">
        <script>
        $(document).ready(function() {

           $(".add_field_button").click(function(e){ //on add input button click
                e.preventDefault();

                    $(".input_fields_wrap").append('<div><input type="text" placeholder = "search" autofocus><br><select name="cars" multiple><%for(int i =0; i<100; i++){%><option value ="saf">Safaricom</option><%}%></select><br><button class="btn">Done</button><button class="remove_field">undo</button></div>'); //add input box

            });

            $(".input_fields_wrap").on("click",".remove_field", function(e){ //user click on remove text
                e.preventDefault(); $(this).parent('div').remove();
            })
		



		







             $('.edit_area').editable("http://www.appelsiini.net/projects/jeditable/php/echo.php", {
                 type      : 'textarea',
                 cancel    : 'Cancel',
                 submit    : 'OK',
                 indicator : '<img src="img/indicator.gif">',
                 tooltip   : 'Click to edit...'
             });


        <!--Initialising DataTables-->

         //$('#table_id').dataTable();
        <!-- pagination button disabling on need-->
        
        $("#SECOND").click(function(){
        	$("#PREVIOUS").removeAttr("disabled");
        });
        $("#LAST").click(function(){
        	$("#NEXT").attr("disabled" , "disabled");
        });

        });

	function formValidator(){
        var name = $("#paragraph_1").val();
        var email = $("#email").val();
        var contact = $("#phone").val();
        var description = $("#dept").val();
        if (name == "" || email == "" || contact == "" || description == "")
        {
            alert("Please Fill All Fields");
		$(".tblTest").hide();
	    $("#contactdiv").css("display", "block");
        }
        else
        {
            if (validateEmail(email)) {
                $("#contactdiv").css("display", "none");
		$(".tblTest").show();
            }
            else {
                alert('Invalid Email Address');
            }
            function validateEmail(email) {
                var filter = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;

                if (filter.test(email)) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
    }





        </script>


<!-- ++++++++++++++++++
++++++++++++++++++++trying some stuff -->
<script>
function showuser(str)
{ 
var xreq;
if(str=="")
{
document.getElementById("showtext").innerHTML="";
return;
}
if(window.XMLHttpRequest)
{
xreq=new XMLHttpRequest();
}
else
{
xreq=new ActiveXObject("Microsoft.XMLHTTP");
}
xreq.onreadystatechange=function ()
{
if((xreq.readyState==4) && (xreq.status==200))
{
document.getElementById("showtext").innerHTML 
                                   =xreq.responseText;
}
}
xreq.open("get","dynamic1.jsp?q="+str,"true");
xreq.send();
}
</script>

<!-- ++++++++++++++++++++++++++++++++stuff ends -->

    </head>
    <body>
<%
String count = request.getParameter("count");
String name = request.getParameter("name");
System.out.println("vvvvvvvvvvvvvvvvvv"+name);
System.out.println("yyyyyyyyyyyyyyyyyy"+count);
%>
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
<tr><td><%=count%></td><td><%=name%></td><td>hello</td><td>hello</td><td>hello</td><td>hello</td><td>hello</td><td>hello</td></tr>
</tbody>
</table>

 
<!-- Contact Form  for the pop up starts-->
<div id="contactdiv"style="display:Block;">
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
<button id='addemails'>+</button>
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
<button type="submit"  id ="adds" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Add >> </button><br/><br/>
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

