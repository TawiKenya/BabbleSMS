
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants"%>
<%@page import="java.net.URLEncoder"%>
<%@page import="org.apache.commons.lang3.RandomStringUtils"%>
<%@page import="ke.co.tawi.babblesms.server.servlet.util.PropertiesConfig"%>
<%@page import="org.jasypt.util.text.BasicTextEncryptor"%>
<%
    String username = (String) session.getAttribute("username");
    if (username != null) {
        response.sendRedirect("index.jsp");
    }

    BasicTextEncryptor textEncryptor = new BasicTextEncryptor();
    // String ENCRYPT_PASSWORD = "Vuwachip2";
    textEncryptor.setPassword(PropertiesConfig.getConfigValue("ENCRYPT_PASSWORD"));
    //textEncryptor.setPassword(ENCRYPT_PASSWORD);

    final int CAPTCHA_LENGTH = 4;
    String captchaStr = RandomStringUtils.randomAlphabetic(CAPTCHA_LENGTH);
    String encryptedCaptchaStr = textEncryptor.encrypt(captchaStr);
%>
<!DOCTYPE html>
<html lang="en">
    <head>
        <meta charset="utf-8">
        <title>Babble SMS</title>
        <meta name="viewport" content="width=device-width,initial-scale=1.0">
        <link href="../css/bootstrap.min.css" rel="stylesheet">
        <link href="../css/strapped.css" rel="stylesheet">
    </head>
    <body>


        <nav class="navbar navbar-default navbar-inverse navbar-fixed-top" role="navigation">
            <img src="../img/tawilogo2.png" alt="logo">
            <div class="container">

                <div class="navbar-header">
                    <button type="button" class="navbar-toggle" data-toggle="collapse" data-target=".navbar-ex1-collapse">
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                        <span class="icon-bar"></span>
                    </button>
                    <a class="navbar-brand nav-link" href="#top">Babble SMS</a>
                </div> <!-- /.navbar-header -->

                <div class="collapse navbar-collapse navbar-ex1-collapse">
                    <!--<ul class="nav navbar-nav navbar-right">
                      <li><a href="#benefits" class="nav-link">Benefits</a></li>
                      <li><a href="#tour" class="nav-link">Tour</a></li>
                      <li><a href="#about" class="nav-link">About</a></li>
                      <li><a href="#contact">Contact Us</a></li>
                      <li><button class="btn btn-warning btn-sm navbar-btn">Login</button></li>
                    </ul>-->
                </div> <!-- /.navbar-collapse -->
            </div> <!-- /.container -->
        </nav> <!-- /.navbar -->

        <div id="top" class="jumbotron">
            <div class="container">
                <h1>Your convenient SMS Messenger</h1>
                <h2>Allows for you to send SMS from the convenience of your web browser.</h2>
                <p>
                <div class="alert alert-info">
                    <%
                        String loginErrStr = (String) session.getAttribute(SessionConstants.ADMIN_SIGN_IN_ERROR_KEY);

                        if (StringUtils.isNotEmpty(loginErrStr)) {
                            out.println("<p class=\"error\">");
                            out.println("Login error: " + loginErrStr);
                            out.println("</p>");
                            session.setAttribute(SessionConstants.ADMIN_SIGN_IN_ERROR_KEY, null);
                             } else {%>
                    Please login with your Username and Password.

                    <% }
                    %>

                </div>
                <form class="form-horizontal" action="../Loggin" method="POST">
                    <fieldset>
                        <div class="input-prepend" title="Username" data-rel="tooltip">
                            <span class="add-on"><i class="icon-user"></i></span><input autofocus class="input-large span10" name="username" id="username" type="text" value="" />
                        </div>
                        <div class="clearfix"></div>

                        <div class="input-prepend" title="Password" data-rel="tooltip">
                            <span class="add-on"><i class="icon-lock"></i></span><input class="input-large span10" name="password" id="password" type="password" value="" />
                        </div>
                        <div class="clearfix"></div>

                        <%
                            String fontImageUrl = "../fontImageGenerator?text=" + URLEncoder.encode(encryptedCaptchaStr, "UTF-8");
                        %>
                        <div class='wrapper'>
                            <label for='captchaAnswer'>Word Verification:</label>
                            <div id="spam-check">
                                <span id="captchaGuidelines">Type the characters you see in the image below. (<em>Letters are not case-sensitive</em>)</span><br>
                                <img id="captcha" src=<% out.println("\"" + fontImageUrl + "\"");%> width="68" height="29" />
                                <input type="text" name="captchaAnswer" id="captchaAnswer" size="5" class="input_normal" />
                                <input type="hidden" name="captchaHidden" id="captchaHidden"
                                       value=<% out.println("\"" + URLEncoder.encode(encryptedCaptchaStr, "UTF-8") + "\"");%> />
                            </div>
                        </div>





                        <div class="input-prepend">
                            <label class="remember" for="remember"><input type="checkbox" id="remember" />Remember me</label>
                        </div>
                        <div class="clearfix"></div>

                        <p class="center span5">
                            <button type="submit" class="btn btn-primary">Login</button>
                        </p>
                    </fieldset>
                </form>
                </p>
            </div> <!-- /.container -->
        </div>  <!-- /.jumbotron -->

        <div class="container">

        </div> <!-- /.container -->

        <footer>
            <div class="container clearfix">
                <p class="pull-left">
                    Copyright &copy; Tawi Commercial Services 2014
                </p>
                <p class="pull-right"align="left" >
                    <!--<a href="#">designed by @chimitahugetech</a>-->

                </p>
            </div> <!-- /.container -->
        </footer>

        <script src="js/jquery.js"></script>
        <script src="js/bootstrap.min.js"></script>
        <script>
            $(".nav-link").click(function(e) {
                e.preventDefault();
                var link = $(this);
                var href = link.attr("href");
                $("html,body").animate({scrollTop: $(href).offset().top - 80}, 500);
                link.closest(".navbar").find(".navbar-toggle:not(.collapsed)").click();
            });
        </script>
    </body>
</html>
