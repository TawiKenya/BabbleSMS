<!DOCTYPE html>
<%
	/**
	Copyright 2015 Tawi Commercial Services Ltd
	Licensed under the Open Software License, Version 3.0 (the "License"); you may
	not use this file except in compliance with the License. You may obtain a copy
	of the License at:
	http://opensource.org/licenses/OSL-3.0
	Unless required by applicable law or agreed to in writing, software distributed
	under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
	CONDITIONS OF ANY KIND, either express or implied.
	See the License for the specific language governing permissions and limitations
	under the License.
	*/
%>

<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>


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
%>


<html lang="en">
<head>

<meta charset="utf-8">
<title>Babble SMS :: Contacts</title>
<meta name="viewport" content="width=device-width, initial-scale=1.0">
<meta name="description" content=".">
<meta name="author" content="eugene">



<script src="../js/jquery/jquery-1.8.2.min.js"></script>
<script src="../js/jquery/jquery-1.7.2.min.js"></script>
<!-- CSS -->
<link href="../css/tawi/errorFormat.css" rel='stylesheet'>
<link href="../css/bootstrap/bootstrap.min.css">
<link href="../css/bootstrap/dataTables.bootstrap.css">
<link href="../css/bootstrap/bootstrap-responsive.css" rel="stylesheet">
<link href="../css/tawi/charisma-app.css" rel="stylesheet">
<link href="../css/tawi/poup.css" rel="stylesheet">
<link href="../css/jquery/jquery-ui-1.8.21.custom.css" rel="stylesheet">
<link href='../css/tawi/fullcalendar.css' rel='stylesheet'>
<link href='../css/tawi/fullcalendar.print.css' rel='stylesheet'
	media='print'>
<link href='../css/tawi/chosen.css' rel='stylesheet'>
<link href='../css/tawi/grouptable.css' rel='stylesheet'>
<link href='../css/tawi/uniform.default.css' rel='stylesheet'>
<link href='../css/tawi/colorbox.css' rel='stylesheet'>
<link href='../css/jquery/jquery.cleditor.css' rel='stylesheet'>
<link href='../css/jquery/jquery.noty.css' rel='stylesheet'>
<link href='../css/tawi/noty_theme_default.css' rel='stylesheet'>
<link href='../css/tawi/elfinder.min.css' rel='stylesheet'>
<link href='../css/tawi/elfinder.theme.css' rel='stylesheet'>
<link href='../css/jquery/jquery.iphone.toggle.css' rel='stylesheet'>
<link href='../css/tawi/opa-icons.css' rel='stylesheet'>
<link href='../css/tawi/uploadify.css' rel='stylesheet'>
<link href='../css/tawi/template.css' rel='stylesheet'>
<link href="../css/tawi/inbox.css" rel="stylesheet">
<link href="../css/bootstrap/bootstrap-cerulean.css" rel="stylesheet">

<!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
<!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

<!-- The fav icon -->
<link rel="shortcut icon" href="img/favicon.ico">

<script>
        $(document).ready(function() {
	
		$("[href]").each(function() {
    if (this.href == window.location.href) {
        $(this).css('background-color', 'rgba(6,156,6,.83)');
        }
    });
           
            $(".input_fields_wrap").on("click",".remove_field", function(e){ //user click on remove text
                e.preventDefault(); $(this).parent('div').remove();
            })
	<!-- /*pagination button disabling on need*/-->
        
        $("#SECOND").click(function(){
        	$("#PREVIOUS").removeAttr("disabled");
        });
        $("#LAST").click(function(){
        	$("#NEXT").attr("disabled" , "disabled");
        });
        
        
        
        });

	</script>


<!-- ++++++++++++++++++
++++++++++++++++++++script for an ajax call to access server side dynamically -->
<script>
          function showuser(str){ 
               var xreq;
               if (window.XMLHttpRequest) {
             xreq=new XMLHttpRequest();//for modern browsers, i.e. Opera,Mozilla, chrome e.t.c.
             } 

             else if (window.ActiveXObject) {
               xreq=new ActiveXObject("Microsoft.XMLHTTP"); //for internet explorer
             } 
             else if(window.createRequest){             
              xreq=window.createRequest();// for crystal browser
             }
             else {
             xreq=null; 
             alert("Your current browser failed, try Mozilla or chrome browsers");
             }
              xreq.onreadystatechange=function (){

              if((xreq.readyState==4) && (xreq.status==200)){
                $('#showtext').remove(); 
           $('#search-head').after(xreq.responseText);                                          
                       }
                   }                       
                
               xreq.open("get","search.jsp?q="+str,"true");
               xreq.send();
                }

function hello(val){
    $(document).ready(function() {
 
        alert(val.innerHTML);
        var count = val.nextAll('td');
                var tbl = document.getElementById("dd");


        alert('i will work');
	$(".tblTest").hide();
        $("#contactdiv").css("display", "block");

        
           });

         }
        var tbl = document.getElementById("dd");
        if (tbl != null) {

            for (var i = 0; i < tbl.rows.length; i++) {
                if(cells.length>0){}

                for (var j = 0; j < tbl.rows[i].cells.length; j++)

                    tbl.rows[i].cells[j].onclick = function () { getval(this); };

            }

        }

 

        function getval(cel) {
        $(document).ready(function() {
            alert('finally');
	$(".tblTest").hide();
        $("#contactdiv").css("display", "block");

    });
}
    
  
</script>

<!-- +++++++++++++++++++++++++++++++ client-server ajax communication script ends here -->
<link href="../bootstrap/css/bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="../bootstrap/js/bootstrap.min.js"></script>
</head>

<body>

	<!-- topbar starts -->
	<nav class="navbar navbar-custom">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="index.jsp">Babble SMS</a>
			</div>
			<ul class="nav navbar-nav">
				<li ><a href="inbox.jsp">MESSAGES</a></li>
				<li class="active"><a href="contact.jsp">CONTACTS</a></li>
				<li><a href="portfolio.jsp">REPORT</a></li>
			</ul>
			<ul class="nav navbar-nav navbar-right">
				<li><a href="#"><span class="hidden-phone"> <%=username%></span>
						Log In</a></li>
				<li><a href="../logout"><span
						class="glyphicon glyphicon-user"></span> Log Out</a></li>
				<li><a href="setting.jsp"><span
						class="glyphicon glyphicon-log-in"></span> Settings</a></li>
			</ul>
		</div>
	</nav>
	<!-- topbar ends -->

	

			<!-- left menu starts -->
			<!--  
			<div class="span2 main-menu-span">
				<div class="well nav-collapse sidebar-nav">
					<ul class="nav nav-tabs nav-stacked main-menu">
						<!--menu to change depending on page requested-->
			<!--  			
						<li class="nav-header hidden-tablet">Contacts</li>
						<li><div class="dropdown">
								<button class="btn btn-default dropdown-toggle" type="button"
									id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
									<span class="caret"></span>
								</button>
								<ul class="dropdown-menu" role="menu"
									aria-labelledby="dropdownMenu1">
									<!-- <li role="presentation"><a role="menuitem" tabindex="-1" href="inactivecontacts.jsp">Inactive contacts</a></li>-->
			<!--  					
									<li role="presentation"><a role="menuitem" tabindex="-1"
										href="#">Inactive contacts</a></li>
								</ul>
								<a class="ajax-link" href="contact.jsp"><i
									class="icon-inbox"></i><span class="hidden-tablet">All
										contacts</span></a>
							</div></li>
						<li><a class="ajax-link" href="addcontact.jsp"><i
								class="icon-plus-sign"></i><span class="hidden-tablet">Add
									contact</span></a></li>
						<li class="nav-header hidden-tablet">Groups</li>
						<li><a class="ajax-link" href="groups.jsp"><i
								class="icon-globe"></i><span class="hidden-tablet">All
									groups</span></a></li>
						<!--   <li><a class="ajax-link" href="#"><i class="icon-globe"></i><span class="hidden-tablet">View group</span></a></li>-->
				<!--  		<li><a class="ajax-link" href="addgroup.jsp"><i
								class="icon-plus-sign"></i><span class="hidden-tablet">Add
									group </span></a></li>
						<!--<li><a class="ajax-link" href="#"><i class="icon-plus-sign"></i><span class="hidden-tablet">Add group </span></a></li> 
	          <li><a class="ajax-link" href="contactspergroup.jsp"><i class="icon-folder-open"></i><span class="hidden-tablet">Group Contacts</span></a></li>-->
			<!--			</ul>
					<!--<label id="for-is-ajax" class="hidden-tablet" for="is-ajax"><input id="is-ajax" type="checkbox"> Ajax on menu</label>-->
			<!--		</div>
				<!--/.well -->
		<!--		</div>
			<!--/span-->
			<!-- left menu ends -->

			<noscript>
				<div class="alert alert-block span10">
					<h4 class="alert-heading">Warning!</h4>
					<p>
						You need to have <a href="http://en.wikipedia.org/wiki/JavaScript"
							target="_blank">JavaScript</a> enabled to use this site.
					</p>
				</div>
			</noscript>
