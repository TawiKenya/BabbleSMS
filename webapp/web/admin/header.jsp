<!DOCTYPE html>
<%
    /**
    Copyright 2015 Tawi Commercial Services Ltd

    Licensed under the Open Software License, Version 3.0 (the ?License?); you may 
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
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants"%>

<%
    // The following is for session management.    
    if (session == null) {
        response.sendRedirect("index.jsp");
    }

    String username = (String) session.getAttribute(SessionConstants.ADMIN_SESSION_KEY);
    if (StringUtils.isEmpty(username)) {
        response.sendRedirect("index.jsp");
    }

    session.setMaxInactiveInterval(SessionConstants.SESSION_TIMEOUT);
    response.setHeader("Refresh", SessionConstants.SESSION_TIMEOUT + "; url=../Logout");

%>                       

<html lang="en">
    <head>
        
        <meta charset="utf-8">
        <title>Babble SMS :: Home</title>
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <meta name="description" content=".">
        <meta name="author" content="eugene" >
        
          <script src="../js/tawi/jquery-1.7.2.min.js"></script>

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
	<script src="../js/tawi/editcontact.js"></script>
      
        
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
        <link href='../css/checkpass.css' rel='stylesheet'>
        <link href='../css/styles.css' rel='stylesheet'>
        
      
        
        <!-- The fav icon -->
        <link rel="shortcut icon" href="img/favicon.ico">
        
        <script src="../js/jquery/jquery-1.8.2.min.js"></script>  
  
	
        
   


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
                    
		    <!-- user dropdown starts -->
                    <div class="btn-group pull-right" >
                         <a class="btn dropdown-toggle" data-toggle="dropdown" href="#">
                            <i class="icon-user"></i><span class="hidden-phone"> <%=username%></span>
                            <span class="caret"></span>
                        </a>

                        <ul class="dropdown-menu">
                            <!--<li><a href="#">Profile</a></li>-->
                            <li class="divider"></li>
                            <li><a href="Logout">Logout</a></li>
                            <li><a href="setting.jsp">Setting</a>
                               
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
                    
                    
                    
                    
                    
                    
                </div>
                            
                       <!-- top menu -->        
                <div id='cssmenu'>
                  <ul>
                  <li class='has-sub' ><a href='#'><span>Account Management</span></a>
                  <ul>
                <li><a href="addaccount.jsp" ><span>Add Account</span></a></li>
                <li><a href="#" ><span>Edit Account</span></a></li>
             <li class='last'><a href='#'><span>View Accounts</span></a></li>
                </ul>


        </li>
            <li class='has-sub'><a href='#'><span>SMS Management</span></a>
      
             </li>
        <li class='has-sub'><a href='#'><span>SMS Trafic</span></a></li>
            <li class='has-sub'><a href='#'><span>Nitifications& Feeback</span></a></li>
            <li class='has-sub'><a href='#'><span>Reports</span></a></li>
            <li class='has-sub'><a href='#'><span>Admin settings</span></a></li>

            </ul>

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


                            <li><a class="ajax-link" href="accounts.jsp"><i class="icon-home"></i><span class="hidden-tablet"> Accounts</span></a></li>
                            <li><a class="ajax-link" href="credit.jsp"><i class="icon-envelope"></i><span class="hidden-tablet">Credit</span></a></li>
                            <li><a class="ajax-link" href="source.jsp"><i class="icon-edit"></i><span class="hidden-tablet">manage shortcode/mask</span></a></li>
                            <li><a class="ajax-link" href="network.jsp"><i class="icon-trash"></i><span class="hidden-tablet">Network</span></a></li>
                           
                           <li class="nav-header hidden-tablet">Message Board</li> 
                           
                            <li><a class="ajax-link" href="systemnotices.jsp"><i class="icon-home"></i><span class="hidden-tablet"> System Notices</span></a></li>
                            <li><a class="ajax-link" href="adminnotices.jsp"><i class="icon-envelope"></i><span class="hidden-tablet">Admin Notices</span></a></li>
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

