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
        <!--
                eugene 1.0.0

                Copyright 2012 eugene
                Licensed under the Apache License v2.0
                http://www.apache.org/licenses/LICENSE-2.0

                
        -->
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
        <link href="../css/bootstrap-responsive.css" rel="stylesheet">
        <link href="../css/charisma-app.css" rel="stylesheet">
        <link href="../css/jquery-ui-1.8.21.custom.css" rel="stylesheet">
        <link href='../css/fullcalendar.css' rel='stylesheet'>
        <link href='../css/fullcalendar.print.css' rel='stylesheet'  media='print'>
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

        
         <!--jqplot charts-->
        <!--<script language="javascript" type="text/javascript" src="../js/jquery.min.js"></script>-->
        <script language="javascript" type="text/javascript" src="../js/jquery.jqplot.min.js"></script>
        <link rel="stylesheet" type="text/css" href="../css/jquery.jqplot.min.css" />
   

   
        <script type="text/javascript" src="../js/jqplot.barRenderer.min.js"></script>
        <script type="text/javascript" src="../js/jqplot.categoryAxisRenderer.min.js"></script>
        <script type="text/javascript" src="../js/jqplot.pointLabels.min.js"></script>
        <script type="text/javascript" src="../js/jqplot.pieRenderer.min.js"></script>
        

        
         
        

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
                    <a class="brand" href="index.html">  <span>Babble</span></a>

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

                        <!--<li class="help"><a href="#">Help</a></li>-->                  
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
                <!--  < <a href="inbox.jsp">MESSAGES</a>
                    <a href="contact.jsp">CONTACTS</a>
                    <a href="portfolio.jsp">REPORT</a>-->
		    <a href="#">MESSAGES</a>
                    <a href="contact.jsp">CONTACTS</a>
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

                          <!--   <li><a class="btn-danger" href="sendsms.jsp" title="write message" data-rel="tooltip"><i class="icon-folder-open"></i><span class="hidden-tablet"> Compose</span></a></li>
                           <li><a class="ajax-link" href="index.jsp"><i class="icon-home"></i><span class="hidden-tablet"> Dashboard</span></a></li>
                            <li><a class="ajax-link" href="inbox.jsp"><i class="icon-envelope"></i><span class="hidden-tablet">Inbox</span></a></li>
                            <li><a class="ajax-link" href="sent.jsp"><i class="icon-edit"></i><span class="hidden-tablet">Sent</span></a></li>
                            <li><a class="ajax-link" href="sentgroup.jsp"><i class="icon-edit"></i><span class="hidden-tablet">Sent Group</span></a></li>
                            <li><a class="ajax-link" href="messagetemplate.jsp"><i class="icon-edit"></i><span class="hidden-tablet">Message Template</span></a></li>
-->
		<li><a class="btn-danger" href="#" title="write message" data-rel="tooltip"><i class="icon-folder-open"></i><span class="hidden-tablet"> Compose</span></a></li>	
			<li><a class="ajax-link" href="index.jsp"><i class="icon-home"></i><span class="hidden-tablet"> Dashboard</span></a></li>
                            <li><a class="ajax-link" href="inbox.jsp"><i class="icon-envelope"></i><span class="hidden-tablet">Inbox</span></a></li>
                            <li><a class="ajax-link" href="sent.jsp"><i class="icon-edit"></i><span class="hidden-tablet">Sent</span></a></li>
                         <li><a class="ajax-link" href="sentgroup.jsp"><i class="icon-edit"></i><span class="hidden-tablet">Sent Group</span></a></li>
                            <li><a class="ajax-link" href="messagetemplate.jsp"><i class="icon-edit"></i><span class="hidden-tablet">Message Template</span></a></li>





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

