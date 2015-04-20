<!DOCTYPE html>
<%
    /**
    Copyright 2015 Tawi Commercial Services Ltd

    Licensed under the Open Software License, Version 3.0 (the ?License?); you may 
    not use this file except in compliance with the License. You may obtain a copy 
    of the License at:
    http://opensource.org/licenses/OSL-3.0

    Unless required by applicable law or agreed to in writing, software distributed 
    under the License is distributed on an ?AS IS? BASIS, WITHOUT WARRANTIES OR
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
        <meta name="author" content="eugene" >
   
      
 

        
        <!--some internal css -->
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
        



         




        <!-- jQuery/javascript -->
        <script src="../js/jquery-1.7.2.min.js"></script>
	<script src="jquery_popup.js"></script>
       	<script src="http://code.jquery.com/ui/1.10.4/jquery-ui.js"></script>
	<script src="../js/editcontact.js"></script>
	<script src="../js/editcontact_popup.js"></script>
        
	<!-- CSS -->
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
	<link href="../css/bootstrap-cerulean.css" rel="stylesheet">
	<link href="http://code.jquery.com/ui/1.10.4/themes/ui-lightness/jquery-ui.css" rel="stylesheet">
	<link rel="stylesheet" href="poup.css" />

        <!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
        <!--[if lt IE 9]>
          <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
        <![endif]-->

        <!-- The fav icon -->
        <link rel="shortcut icon" href="img/favicon.ico">
        <script>
        $(document).ready(function() {

           
            $(".input_fields_wrap").on("click",".remove_field", function(e){ //user click on remove text
                e.preventDefault(); $(this).parent('div').remove();
            })
	<!-- pagination button disabling on need-->
        
        $("#SECOND").click(function(){
        	$("#PREVIOUS").removeAttr("disabled");
        });
        $("#LAST").click(function(){
        	$("#NEXT").attr("disabled" , "disabled");
        });
        
        
        document.getElementById("abcd").click(function(e){
            e.preventDefault();
            alert('am clicked');
        })

        });

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

function hello(val){
    $(document).ready(function() {
        //var $td= $(this).closest('tr').children('td'); 
        alert(val.innerHTML);
        var count = val.nextAll('td');
                var tbl = document.getElementById("dd");
        //var count = document.findElement("td");
        alert(count.length);
        //alert(count[0].innerHTML);
        //alert($(this).html());
        alert('i will work');
	$(".tblTest").hide();
        $("#contactdiv").css("display", "block");
        
        
        //alert($td.length);
       // alert($td.eq(1).text());
        

        
    });

    //setTimeout(popup, 3000);
    //alert('hello');
    //document.getElementById("contactdiv").style.display="block";
    //document.getElementById("hello").style.display="none";
    }

        
        
        
        
        var tbl = document.getElementById("dd");
        /*var cells = tbl.rows[i].cells;
        var x1=cells[0];
        var x2=cells[1];
        var x3=cells[2];
        var x4=cells[3];
        var x5=cells[4];
        var x6=cells[5];
 
        document.getElementById("paragraph_1").value=x2;
        document.getElementById("phone2").value=x3;
        document.getElementById("email").value=x4;
        document.getElementById("uuid").value="";
        document.getElementById("group").value=x5;
        document.getElementById("textarea").value=x6;*/

        if (tbl != null) {

            for (var i = 0; i < tbl.rows.length; i++) {
                if(cells.length>0){
                    
                }

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

<!-- ++++++++++++++++++++++++++++++++stuff ends -->

    </head>

    <body>

        <!-- topbar starts -->
        <div class="navbar">
            <div class="navbar-inner">

                <div class="container-fluid">
                    <a class="btn btn-navbar" data-toggle="collapse" data-target=".top-nav.nav-collapse,.sidebar-nav.nav-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </a>
                    <a class="brand" href="index.jsp">  <span>Babble</span></a>

                    <!-- theme selector starts -->
                    <div class="btn-group pull-right theme-container" >
                        <!--<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                                <i class="icon-tint"></i><span class="hidden-phone"> Change Theme / Skin</span>
                                <span class="caret"></span>
                        </a>
                        <ul class="dropdown-menu" id="themes">
                                <li><a data-value="classic" href="#"><i class="icon-blank"></i>mt  kenya</a></li>
                                <li><a data-value="cerulean" href="#"><i class="icon-blank"></i>kilimanjaro</a></li>
                                <li><a data-value="cyborg" href="#"><i class="icon-blank"></i> lake victoria</a></li>
                                <li><a data-value="redy" href="#"><i class="icon-blank"></i>clement</a></li>
                                <li><a data-value="journal" href="#"><i class="icon-blank"></i>nairobi</a></li>
                                <li><a data-value="simplex" href="#"><i class="icon-blank"></i>mombasa</a></li>
                                <li><a data-value="slate" href="#"><i class="icon-blank"></i>naivasha</a></li>
                                <li><a data-value="spacelab" href="#"><i class="icon-blank"></i> Spacelab</a></li>
                                <li><a data-value="united" href="#"><i class="icon-blank"></i> United</a></li>
                        </ul>-->
                    </div>
                    <!-- theme selector ends -->

                    <!-- user dropdown starts -->
                    <div class="btn-group pull-right" >
                        <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                            <i class="icon-user"></i><span class="hidden-phone"> <%=username%></span>
                            <span class="caret"></span>
                        </a>

                        <ul class="dropdown-menu">
                            <!--<li><a href="#">Profile</a></li>-->
                            <li class="divider"></li>
                            <li><a href="../logout">Logout</a></li>
                            <li><a href="setting.jsp">Setting</a>
                                <!--<a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                                <i class="icon-tint"></i><span class="hidden-phone"> Change Theme / Skin</span>
                                <span class="caret"></span>
                                </a>-->

                                <ul class="dropdown-menu" id="themes">
                                    <li><a data-value="classic" href="#"><i class="icon-blank"></i>mt  kenya</a></li>
                                    <li><a data-value="cerulean" href="#"><i class="icon-blank"></i>kilimanjaro</a></li>
                                    <li><a data-value="cyborg" href="#"><i class="icon-blank"></i> lake victoria</a></li>
                                    <li><a data-value="redy" href="#"><i class="icon-blank"></i>clement</a></li>
                                    <li><a data-value="journal" href="#"><i class="icon-blank"></i>nairobi</a></li>
                                    <li><a data-value="simplex" href="#"><i class="icon-blank"></i>mombasa</a></li>
                                    <li><a data-value="slate" href="#"><i class="icon-blank"></i>naivasha</a></li>
                                    <li><a data-value="spacelab" href="#"><i class="icon-blank"></i> Spacelab</a></li>
                                    <li><a data-value="united" href="#"><i class="icon-blank"></i> United</a></li>
                                </ul>

                            </li>

                        </ul>


                    </div>

                    <!-- user dropdown ends -->

                    <div class="top-nav nav-collapse">
                        <!--<ul class="nav">
                                <li><a href="#">Visit Site</a></li>
                                <li>
                                        <form class="navbar-search pull-left">
                                                <input placeholder="Search" class="search-query span2" name="query" type="text">
                                        </form>
                                </li>
                        </ul>-->
                    </div><!--/.nav-collapse -->

                </div>
                <!-- top menu -->                        
                <div class="topmenu">                     
                   <!--  <a href="inbox.jsp">MESSAGES</a>-->
		    <a href="inbox.jsp">MESSAGES</a>
                    <a href="contact.jsp">CONTACTS</a>
                   <!-- <a href="portfolio.jsp">REPORT</a>-->
		     <a href="#">REPORT</a>
                </div>    
            </div>

        </div>
        <!-- topbar ends -->

        <div class="container-fluid">
            <div class="row-fluid">


                <!-- left menu starts -->
                <div class="span2 main-menu-span">
                    <div class="well nav-collapse sidebar-nav">
                        <ul class="nav nav-tabs nav-stacked main-menu">
                            <!--menu to change depending on page requested-->
                            <li class="nav-header hidden-tablet" style="background:red">Contacts</li>
                            <li><div class="dropdown">
  <button class="btn btn-default dropdown-toggle" type="button" id="dropdownMenu1" data-toggle="dropdown" aria-expanded="true">
  <span class="caret"></span>    
  </button>
<ul class="dropdown-menu" role="menu" aria-labelledby="dropdownMenu1">
   <!-- <li role="presentation"><a role="menuitem" tabindex="-1" href="inactivecontacts.jsp">Inactive contacts</a></li>-->
	<li role="presentation"><a role="menuitem" tabindex="-1" href="#">Inactive contacts</a></li>
  </ul>
<a class="ajax-link" href="contact.jsp"><i class="icon-inbox"></i><span class="hidden-tablet">All contacts</span></a>
</div> </li>
                   <li><a class="ajax-link" href="addcontact.jsp"><i class="icon-plus-sign"></i><span class="hidden-tablet">Add contact</span></a></li>
                   <li class="nav-header hidden-tablet">Groups</li>
                  <li><a class="ajax-link" href="groups.jsp"><i class="icon-globe"></i><span class="hidden-tablet">View group</span></a></li>
		  <!--   <li><a class="ajax-link" href="#"><i class="icon-globe"></i><span class="hidden-tablet">View group</span></a></li>-->
                  <li><a class="ajax-link" href="addgroup.jsp"><i class="icon-plus-sign"></i><span class="hidden-tablet">Add group </span></a></li>
	          <!--<li><a class="ajax-link" href="#"><i class="icon-plus-sign"></i><span class="hidden-tablet">Add group </span></a></li>-->	     
                        </ul>
                        <!--<label id="for-is-ajax" class="hidden-tablet" for="is-ajax"><input id="is-ajax" type="checkbox"> Ajax on menu</label>-->
                    </div><!--/.well -->
                </div><!--/span-->
                <!-- left menu ends -->

                <noscript>
                <div class="alert alert-block span10">
                    <h4 class="alert-heading">Warning!</h4>
                    <p>You need to have <a href="http://en.wikipedia.org/wiki/JavaScript" target="_blank">JavaScript</a> enabled to use this site.</p>
                </div>
                </noscript>

                <div id="content" class="span10">
                    <!-- content starts -->

