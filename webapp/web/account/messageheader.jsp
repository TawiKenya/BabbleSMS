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

<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>

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
    <title>Babble SMS :: Home</title>
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta name="description" content=".">
    <meta name="author" content="eugene" >
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
    </style>
    <!-- jQuery -->
    <script src="../js/jquery-1.7.2.min.js"></script>
    <script src="../js/editcontact.js"></script>
    <script src="../js/composepagejavascript.js"></script>
    <script src="../js/jquery_popup.js"></script>

    <script src="../js/editcontact_popup.js"></script>
    <link href="../css/bootstrap-responsive.css" rel="stylesheet">
    <link href="../css/charisma-app.css" rel="stylesheet">
    <link href="../css/jquery-ui-1.8.21.custom.css" rel="stylesheet">
    <link href='../css/fullcalendar.css' rel='stylesheet'>
    <link href='../css/fullcalendar.print.css' rel='stylesheet' media='print'>
    <link href='../css/chosen.css' rel='stylesheet'>
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
    <link href='../css/site.css' rel='stylesheet'>
    <link href='../css/grouptable.css' rel='stylesheet'>
    <link href="../css/poup.css" rel="stylesheet">
    <link href="../css/credit.css" rel="stylesheet">
    <!--jqplot charts-->
    <!--<script language="javascript" type="text/javascript" src="../js/jquery.min.js"></script>-->
    <script language="javascript" type="text/javascript" src="../js/jquery.jqplot.min.js"></script>
    <link rel="stylesheet" type="text/css" href="../css/jquery.jqplot.min.css" />
    <script type="text/javascript" src="../js/jquery-1.4.2.min.js"></script>
    <script src="../js/jquery.autocomplete.js"></script>
    <style type= "text/css">
    .tokenize-sample { width: 300px ;}
    </style>
    <script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>
    <script type="text/javascript" src="../js/jquery.tokenize.js"></script>
    <link rel="stylesheet" type="text/css" href="../css/jquery.tokenize.css" />
    <script type="text/javascript" src="../js/jqplot.barRenderer.min.js"></script>
    <script type="text/javascript" src="../js/jqplot.categoryAxisRenderer.min.js"></script>
    <script type="text/javascript" src="../js/jqplot.pointLabels.min.js"></script>
    <script type="text/javascript" src="../js/jqplot.pieRenderer.min.js"></script>
    <script>
    $(document).ready(function() {
    // calling of password matcher function
    $("#txtConfirmPassword").keyup(checkPasswordMatch); 


    $("[href]").each(function() {
    if (this.href == window.location.href) {
    $(this).css('background-color', 'rgba(6,156,6,.83)');
    }
    });
    $(".tablets").hide();
    $('#destinationdiv').hide();
    $('#label1').hide();
    $('#label2').hide();
    $('#sendtocontact').hide();
    $('.section').hide();
    $('#destination').change(function(){
    if($(this).val()=="Group"){
    $('#destinationdiv').hide();
    $('#label2').hide();
    $('#sendtocontact').hide();
    $("#scroll3").hide();$('.section').hide();
    $(".tablets").show();
    }
    else if($(this).val()=="Contact"){
    $("#scroll2").hide();
    $(".tablets").hide();
    $("#scroll3").hide();
    $('#label1').hide();
    $('#destinationdiv').hide();
    $('#label2').hide();
    $('.section').show();
    }
    });
    jQuery(function(){
    $("#receiver").autocomplete("sentTocontact.jsp");
    });
    $("#scroll2 td:nth-child(1)").click(function(event){
    //Prevent the hyperlink to perform default behavior
    event.preventDefault();
    $td= $(this).closest('tr').children('td');
    var count2 = 0;
    $("#scroll2 td").each( function(){
    if((count2 % 2) == 0){
    $(this).css("background", "#F9F9F9");
    }
    else if((count2 % 2) != 0) {
    $(this).css("background", "transparent");
    } count2++;
    });

    ($td).css("background", "#808080");
    group = $td.eq(0).text();
    group2 = $td.eq(1).text();

    $('.groupselected').each( function(){
    var testa2 = $(this).val();
    if($(this).val().length != 0){
    $(this).remove();
    }
    });
    var field1 = $('.groupselected').clone().val(group2);
    field1.appendTo("#grouptable");
    });
    //reaction when cancel on addmessagetemplate.jsp is clicked
    $("#canceltemp").click(function(e){
    e.preventDefault();
    window.location.href="../account/messagetemplate.jsp";
    });
    });
    //function to check whether the passwords match in settings.jsp
    function checkPasswordMatch() {
        var password = $("#txtNewPassword").val();
        var confirmPassword = $("#txtConfirmPassword").val();

        if (password != confirmPassword)
            $("#divCheckPasswordMatch").html("Passwords do not match!");

        else
            $("#divCheckPasswordMatch").html("Passwords match.");
    }



    </script>
    <!-- The HTML5 shim, for IE6-8 support of HTML5 elements -->
    <!--[if lt IE 9]>
    <script src="http://html5shim.googlecode.com/svn/trunk/html5.js"></script>
    <![endif]-->
    <!-- The fav icon -->
    <link rel="shortcut icon" href="img/favicon.ico">
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
<a class="brand" href="index.jsp"> <span>Babble</span></a>
<!-- theme selector starts -->
<div class="btn-group pull-right theme-container" >
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
<li><a data-value="classic" href="#"><i class="icon-blank"></i>mt kenya</a></li>
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
<!--<li class="help"><a href="#">Help</a></li>-->
</div>
<!-- user dropdown ends -->
<div class="top-nav nav-collapse">
</div><!--/.nav-collapse -->
</div>

<!-- top menu -->
<div class="topmenu">
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
<li class="nav-header hidden-tablet">Main</li>
<li><a id ="btn-dangers1" href="sendsms.jsp" title="write message" data-rel="tooltip"><i class="icon-folder-open"></i><span class="hidden-tablet">Compose</span></a></li>
<li><a class="ajax-link"id ="btn-dangers1" href="index.jsp"><i class="icon-home"></i><span class="hidden-tablet">Quick Reports</span></a></li>
<li><a class="ajax-link" id ="btn-dangers1"href="inbox.jsp"><i class="icon-envelope"></i><span class="hidden-tablet">Inbox</span></a></li>
<li><a class="ajax-link"id ="btn-dangers1" href="sent.jsp"><i class="icon-edit"></i><span class="hidden-tablet">Sent</span></a></li>
<li><a class="ajax-link" id ="btn-dangers1" href="sentgroup.jsp"><i class="icon-edit"></i><span class="hidden-tablet">Sent Group</span></a></li>
<li><a class="ajax-link"id ="btn-dangers1" href="messagetemplate.jsp"><i class="icon-edit"></i><span class="hidden-tablet">Message Template</span></a></li>
</ul>
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
