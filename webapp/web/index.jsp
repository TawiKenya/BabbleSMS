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
<%@page
	import="ke.co.tawi.babblesms.server.servlet.util.FontImageGenerator"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>

<%@page import="java.net.URLEncoder"%>
<%@page import="java.util.Calendar"%>

<%@page import="org.apache.commons.lang3.RandomStringUtils"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="org.jasypt.util.text.BasicTextEncryptor"%>
<%@ page import="net.tanesha.recaptcha.ReCaptcha"%>
<%@ page import="net.tanesha.recaptcha.ReCaptchaFactory"%>
<%
	BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
	textEncryptor.setPassword(FontImageGenerator.SECRET_KEY);

	final int CAPTCHA_LENGTH = 4;
	String captchaStr = RandomStringUtils.randomAlphabetic(CAPTCHA_LENGTH);
	String encryptedCaptchaStr = textEncryptor.encrypt(captchaStr.toLowerCase());
%>

<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<meta charset="utf-8">
<title>Babble SMS</title>
<link href="css/bootstrap/bootstrap.min.css" rel="stylesheet">
<link href="css/tawi/index.css" rel="stylesheet">
<link href="bootstrap/css/bootstrap.min.css" rel="stylesheet"
	type="text/css" />
<script type="text/javascript" src="bootstrap/js/bootstrap.min.js"></script>
<script src='https://www.google.com/recaptcha/api.js'></script>
</head>
<body>
	<div class="container-fluid">



		<div class="row">

			<div class="col-lg-3 col-md-3 col-sm-2">


				<!-- logo display on left corner -->
				<div class="logo ">
					<img alt="Tawi Commercials" title="Tawi Commercials"
						src="img/tawi-logo-with-slogan.png"><br /> <a href="#"><h1>Babble
							SMS</h1></a>
				</div>



             <!-- login Form using bootstrap-->

				<form class="form-horizontal login-form" action="login"
					method="POST">

					<div class="form-group">
						<label for="username">User Name</label> <input id="username"
							class="form-control" type="text" name="username" value=""
							required placeholder="username" />
					</div>


					<div class="loginError">
						<%
							String loginErrStr = "";
							session = request.getSession(false);

							if (session != null) {
								loginErrStr = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_ERROR_KEY);
							}

							if (StringUtils.isNotEmpty(loginErrStr)) {
								out.println("<p class=\"error\">");
								out.println("Login error: " + loginErrStr);
								out.println("</p>");
								session.setAttribute(SessionConstants.ACCOUNT_SIGN_IN_ERROR_KEY, null);
							}
						%>


					</div>



					<div class="form-group">
						<label for="password">Password</label> <input id="password"
							class="form-control" type="password" name="password" value=""
							required placeholder="password" />
					</div>





              <!-- the google API ReCaptcha to prevent bots from logging in -->
              
					<div class="g-recaptcha captcha"
						data-sitekey="6LdieygTAAAAALAxlVrB4SHlYH_5Jeq1Np2lWSSx"
						style="transform: scale(0.77); -webkit-transform: scale(0.77); transform-origin: 0 0; -webkit-transform-origin: 0 0;"></div>


					<button type="submit" class="btn btn-default">Submit</button>



				</form>


                <!-- reset password div -->
                
				<p>
					<a href="account/resetPassword.jsp" target="_top"> Forgot
						Password</a>
				</p>

			</div>

         <!-- the left section with the banner image -->

			<div class="col-lg-9 col-md-9 col-sm-4 banner"
				style="min-height: 800px;">


				<h3>
					<a href="#">Babble SMS</a>
				</h3>

				<h1>Your Convenient SMS Messenger</h1>

				<p>Allows you to send SMS from the convenience of your web
					browser.</p>

			</div>




		</div>
		
		<!-- End of first row containing the login form and the image banner -->





      <!-- the footer -->
		<div class="row footer">
			<a>Tawi Commercial Services &nbsp; </a><%=Calendar.getInstance().get(Calendar.YEAR)%></a>
		</div>

		<script src="js/jquery/jquery.js"></script>
		<script src="js/bootstrap/bootstrap.min.js"></script>
		<script>
			$(".nav-link").click(
					function(e) {
						e.preventDefault();
						var link = $(this);
						var href = link.attr("href");
						$("html,body").animate({
							scrollTop : $(href).offset().top - 80
						}, 500);
						link.closest(".navbar").find(
								".navbar-toggle:not(.collapsed)").click();
					});
		</script>


		<!-- Piwik and Google Analytics Tracking Code -->
		<!-- Commented out during development to prevent false hits. -->
		<!-- 
        <script type="text/javascript">
            var _paq = _paq || [];
            _paq.push(['trackPageView']);
            _paq.push(['enableLinkTracking']);
            (function() {
              var u="//www.tawi.mobi/piwik/";
              _paq.push(['setTrackerUrl', u+'piwik.php']);
              _paq.push(['setSiteId', 1]);
              var d=document, g=d.createElement('script'), s=d.getElementsByTagName('script')[0];
              g.type='text/javascript'; g.async=true; g.defer=true; g.src=u+'piwik.js'; s.parentNode.insertBefore(g,s);
             </div>})();
        </script>

        <noscript>
            <p><img src="//www.tawi.mobi/piwik/piwik.php?idsite=1" style="border:0;" alt="" /></p>
        </noscript>
        
        <script>
          (function(i,s,o,g,r,a,m){i['GoogleAnalyticsObject']=r;i[r]=i[r]||function(){
          (i[r].q=i[r].q||[]).push(arguments)},i[r].l=1*new Date();a=s.createElement(o),
          m=s.getElementsByTagName(o)[0];a.async=1;a.src=g;m.parentNode.insertBefore(a,m)
          })(window,document,'script','//www.google-analytics.com/analytics.js','ga');

          ga('create', 'UA-66126620-5', 'auto');
          ga('send', 'pageview');
        </script>
        -->
		<!-- End Piwik and Google Analytics Tracking Code -->
	</div>

</body>

</html>