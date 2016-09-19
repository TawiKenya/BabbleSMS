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

       
        <link href="../css/bootstrap/bootstrap-cerulean.css" rel="stylesheet">
        <style type="text/css">
            body {
                padding-bottom: 40px;
            }
            .sidebar-nav {
                padding: 9px 0;
            }
        </style>
        
        <!-- jQuery -->	
	<script src="../js/tawi/editcontact.js"></script>
        <script src="../js/tawi/jquery-1.7.2.min.js"></script>
        
        <link href="../css/bootstrap/bootstrap-responsive.css" rel="stylesheet">
        <link href="../css/tawi/charisma-app.css" rel="stylesheet">
        <link href="../css/jquery/jquery-ui-1.8.21.custom.css" rel="stylesheet">
        <link href='../css/tawi/fullcalendar.css' rel='stylesheet'>
        <link href='../css/tawi/fullcalendar.print.css' rel='stylesheet'  media='print'>
        <link href='../css/tawi/chosen.css' rel='stylesheet'>
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
         <link href="../css/tawi/errorFormat.css" rel='stylesheet'>
        <link href="../css/tawi/inbox.css" rel="stylesheet">
        <!-- The fav icon -->
        <link rel="shortcut icon" href="img/favicon.ico">
        
        <script src="../js/jquery/jquery-1.8.2.min.js"></script>  
  

        
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
				<li><a href="contact.jsp">CONTACTS</a></li>
				<li class="active"><a href="portfolio.jsp">REPORT</a></li>
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


                <noscript>
                    <div class="alert alert-block span10">
                        <h4 class="alert-heading">Warning!</h4>
                        <p>You need to have <a href="http://en.wikipedia.org/wiki/JavaScript" target="_blank">JavaScript</a> enabled to use this site.</p>
                    </div>
                </noscript>


