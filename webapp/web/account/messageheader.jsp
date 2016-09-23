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
<meta name="author" content="eugene">
<!-- The styles -->
<!--<link id="bs-css" href="css/bootstrap-cerulean.css" rel="stylesheet">-->
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
<script src="../js/jquery/jquery-1.7.2.min.js"></script>

<link href="../css/tawi/errorFormat.css" rel='stylesheet'>

<link href="../css/bootstrap/bootstrap-responsive.css" rel="stylesheet">

<link href="../css/tawi/charisma-app.css" rel="stylesheet">

<link href="../css/jquery/jquery-ui-1.8.21.custom.css" rel="stylesheet">

<link href='../css/tawi/fullcalendar.css' rel='stylesheet'>

<link href='../css/tawi/fullcalendar.print.css' rel='stylesheet'
	media='print'>
	
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

<link href='../css/tawi/site.css' rel='stylesheet'>

<link href='../css/tawi/grouptable.css' rel='stylesheet'>

<link href="../css/tawi/poup.css" rel="stylesheet">

<link href="../css/tawi/credit.css" rel="stylesheet">

<link href="../css/tawi/inbox.css" rel="stylesheet">

<!--jqplot charts-->
<!--<script language="javascript" type="text/javascript" src="../js/jqplot/jquery.min.js"></script>-->

<script language="javascript" type="text/javascript" src="../js/jqplot/jquery.jqplot.min.js"></script>

<link rel="stylesheet" type="text/css" href="../css/jquery/jquery.jqplot.min.css" />

<script type="text/javascript" src="../js/jquery/jquery-1.4.2.min.js"></script>

<script src="../js/jquery/jquery.autocomplete.js"></script>

<style type="text/css">
.tokenize-sample {
	width: 300px;
}
</style>
<script type="text/javascript" src="https://ajax.googleapis.com/ajax/libs/jquery/1.8.2/jquery.min.js"></script>

<script type="text/javascript" src="../js/jquery/jquery.tokenize.js"></script>

<link rel="stylesheet" type="text/css" href="../css/jquery/jquery.tokenize.css" />

<script type="text/javascript" src="../js/jqplot/jquery.jqplot.min.js"></script>

<script type="text/javascript" src="../js/jqplot/jqplot.barRenderer.min.js"></script>

<script type="text/javascript" src="../js/jqplot/jqplot.categoryAxisRenderer.min.js"></script>

<script type="text/javascript" src="../js/jqplot/jqplot.pointLabels.min.js"></script>

<script type="text/javascript" src="../js/jqplot/jqplot.pieRenderer.min.js"></script>

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
		$('#destination').change(function() {
			if ($(this).val() == "Group") {
				$('#destinationdiv').hide();
				$('#label2').hide();
				$('#sendtocontact').hide();
				$("#scroll3").hide();
				$('.section').hide();
				$(".tablets").show();
			} else if ($(this).val() == "Contact") {
				$("#scroll2").hide();
				$(".tablets").hide();
				$("#scroll3").hide();
				$('#label1').hide();
				$('#destinationdiv').hide();
				$('#label2').hide();
				$('.section').show();
			}
		});
		
		
		jQuery(function() {
			$("#receiver").autocomplete("sentTocontact.jsp");
		});
		$("#scroll2 td:nth-child(1)").click(function(event) {
			//Prevent the hyperlink to perform default behavior
			event.preventDefault();
			$td = $(this).closest('tr').children('td');
			var count2 = 0;
			$("#scroll2 td").each(function() {
				if ((count2 % 2) == 0) {
					$(this).css("background", "#F9F9F9");
				} else if ((count2 % 2) != 0) {
					$(this).css("background", "transparent");
				}
				count2++;
			});

			($td).css("background", "#808080");
			group = $td.eq(0).text();
			group2 = $td.eq(1).text();

			$('.groupselected').each(function() {
				var testa2 = $(this).val();
				if ($(this).val().length != 0) {
					$(this).remove();
				}
			});
			var field1 = $('.groupselected').clone().val(group2);
			field1.appendTo("#grouptable");
		});
		//reaction when cancel on addmessagetemplate.jsp is clicked
		$("#canceltemp").click(function(e) {
			e.preventDefault();
			window.location.href = "../account/messagetemplate.jsp";
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
<link href="../bootstrap/css/bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="../bootstrap/js/bootstrap.min.js"></script>
</head>


<body>
	<nav class="navbar navbar-custom">
		<div class="container-fluid">
			<div class="navbar-header">
				<a class="navbar-brand" href="index.jsp">Babble SMS</a>
			</div>
			<ul class="nav navbar-nav">
				<li class="active"><a href="inbox.jsp">MESSAGES</a></li>
				<li><a href="contact.jsp">CONTACTS</a></li>
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
	<div class="container-fluid">



		<div class="row">





			<div class="col-lg-2 col-md-2 col-sm-1">


				<ul class="nav nav-tabs nav-stacked main-menu">
					<!--menu to change depending on page requested-->
					<li class="nav-header hidden-tablet">Main</li>
					<li><a id="btn-dangers1" href="sendsms.jsp"
						title="write message" data-rel="tooltip"><i
							class="icon-folder-open"></i><span class="hidden-tablet">Compose</span></a></li>
					<li><a class="ajax-link" id="btn-dangers1" href="index.jsp"><i
							class="icon-home"></i><span class="hidden-tablet">Quick
								Reports</span></a></li>
					<li><a class="ajax-link" id="btn-dangers1" href="inbox.jsp"><i
							class="icon-envelope"></i><span class="hidden-tablet">Inbox</span></a></li>

					<li><a class="ajax-link" id="btn-dangers1"
						href="sentgroup.jsp"><i class="icon-edit"></i><span
							class="hidden-tablet">Sent Group</span></a></li>
					<li><a class="ajax-link" id="btn-dangers1"
						href="messagetemplate.jsp"><i class="icon-edit"></i><span
							class="hidden-tablet">Message Template</span></a></li>

					<li><a class="ajax-link" id="btn-dangers1" href="sent.jsp"><i
							class="icon-edit"></i><span class="hidden-tablet">Sent
								Messages</span></a></li>

					<!--  	
				<li>&nbsp;&nbsp;&nbsp; <i class="icon-edit"></i> <span class="hidden-tablet"> 
					
					
					<select class="ajax-link" id="btn-dangers1" onchange="location = this.value;"
						style="width: 140px; color: #369bd7; font-weight: 550; font-family: sans-serif;">
						
							<option value="sent.jsp">Sent Messages</option>
							<option value="sent.jsp">All Sent</option>
							<option value="sentStatus.jsp">Sent</option>
							<option value="sentReceived.jsp">Sent And Received</option>
							<option value="sentRejected.jsp">Sent And Rejected</option>
							<option value="sentFailed.jsp">Sent And Failed</option>
							<option value="sentTransit.jsp">Sent And In Transit</option>
							
						
					</select>

				</span>
				</li>
			-->


				</ul>

			</div>

			<br /> <br />



			<noscript>
				<div class="alert alert-block span10">
					<h4 class="alert-heading">Warning!</h4>
					<p>
						You need to have <a href="http://en.wikipedia.org/wiki/JavaScript"
							target="_blank">JavaScript</a> enabled to use this site.
					</p>
				</div>
			</noscript>