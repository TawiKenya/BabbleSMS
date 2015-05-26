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
<%@page import="ke.co.tawi.babblesms.server.servlet.util.PropertiesConfig"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>

<%@page import="java.net.URLEncoder"%>

<%@page import="org.apache.commons.lang3.RandomStringUtils"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>

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

<html lang="en">
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <meta charset="utf-8">
        <title>Babble SMS</title>
        <meta name="viewport" content="width=device-width,initial-scale=1.0">
        <link href="css/bootstrap.min.css" rel="stylesheet">
        <link href="css/site.css" rel="stylesheet">
        <link href="css/strapped.css" rel="stylesheet">        
        <link href="css/indexpage.css" rel="stylesheet">
            </head>
    <body>   
  
<div class="b-page b-page_en">
     <div class="new-left">
          <div class="new-auth js-new-auth">
                <div class="b-stamp ">
                    <img alt="Tawi Commercials" title="Tawi Commercials" src="img/tawi-logo-with-slogan.png" >
                <a class="b-stamp__mail" href="?">BabbleSMS</a>
                </div>
               <form class="form-horizontal" action="login" method="POST">
               <fieldset>
                        <!--<div class="formalign">-->
                        <div class="input-prepend" title="Username" data-rel="tooltip">
                          <label class=" nb-input _nb-small-complex-input new-auth-input js-login _init" autocapitalize="off" autocorrect="off" data-nb="input">
                          <span class="_nb-input-content">
                                <span class="_nb-input-hint">
                                      <span class="_nb-input-hint-inner">Username</span>
                                </span>
                                <input class="_nb-input-controller" autocapitalize="off" autocorrect="off" placeholder="Username" type="text" 
                                name="username" id="username" value="">
                                       <span class="_nb-input-view"> </span>          
                                              </span>                    
                                                                  
                          
                          </label>
                          </div>
                          <div class="alert alert-info">
                    <%
                        String loginErrStr = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_ERROR_KEY);

                        if (StringUtils.isNotEmpty(loginErrStr)) {
                            out.println("<p class=\"error\">");
                            out.println("Login error: " + loginErrStr);
                            out.println("</p>");
                            session.setAttribute(SessionConstants.ACCOUNT_SIGN_IN_ERROR_KEY, null);
                          } else {%>          <% }                  %>


                </div>
                               <div class="input-prepend" title="Password" data-rel="tooltip">
                               <label class=" nb-input _nb-small-complex-input new-auth-input js-passwd _init" type="password" data-nb="input">
                               <span class="_nb-input-content">
                                      <span class="_nb-input-hint">
                                         <span class="_nb-input-hint-inner">Password</span>
                                       </span>
                                    <input class="_nb-input-controller" name="password" id="password" placeholder="Password" type="password" value="" />
                                    <span class="_nb-input-view"> </span>
                                </span>
                                </label>
                                </div>
                                
                                <div class="formalign">
                                        <span class="_nb-checkbox-label"><i>Type the characters you see in the image below. 
                                         (Letters are not case-sensitive)</i></span>
                                      </div>
                                        <%  String fontImageUrl = "fontImageGenerator?text=" + URLEncoder.encode(encryptedCaptchaStr, "UTF-8"); %>

                                      <div id="spam-check">
                                
                                <img id="captcha" src=<% out.println("\"" + fontImageUrl + "\"");%> width="68" height="29" />
                                <input type="text" name="captchaAnswer" id="captchaAnswer" size="5" class="input_normal" />
                                <input type="hidden" name="captchaHidden" id="captchaHidden"
                                       value=<% out.println("\"" + URLEncoder.encode(encryptedCaptchaStr, "UTF-8") + "\"");%> />
                            </div>                                                         
                                     
                                         <div class="formalign formalign_justify formalign_nowrap">
                                         <input name="twoweeks" value="yes" type="hidden">
                                         <label data-nb="checkbox" for="nb-checkbox_2" class=" new-auth-alien-computer js-remember formalign__item nb-checkbox _nb-small-checkbox-checkbox _init">
                                         <input class="_nb-checkbox-input" id="nb-checkbox_2" type="checkbox">
                                         <span class="_nb-checkbox-flag _nb-checkbox-normal-flag">
                                                     <span class="_nb-checkbox-flag-icon"></span>
                                         </span>
                                         <span class="_nb-checkbox-label">Remember Me</span>
                                         </label> 
                                         
                                         </div>
                                                <div class="formalign formalign_justify formalign_nowrap">
                                                <span class="new-auth-submit formalign__item">
                                                <button class=" nb-button _nb-action-button nb-group-start" type="submit">
                                                <span class="_nb-button-content">Log in</span>
                                                </button>                                         
                                                </span>                                                 
                                                 </div>
                                                         <div class="formalign">
                                                               <div class="b-mail-domik__social js-socials"></div>
                                                          </div>
                            </fieldset>  
                            <p> <a href="account/forgotpas.jsp" target="_top"> Forgot password!!</a>  </p>
                 </form>
            </div>                        
                 
                   <div class="made-in-kenya"></div> 
                            </div>
                 </div>
                            
              
        <div class="new-right">
              <div class="new-content new-content_promo js-promo">
                   <div class="container">
                         <div class="left-panel">
                         <h2 class="promo-title"><a href="?">Babble SMS</a></h2>
                         <h1 class="promo-title">Your Convenient SMS Messenger</h1>
                         <p class="promo-text promo-text_main">Allows for you to send SMS from the convenience of your web browser.</p>
                         <div class="promo-form-background">
                             <div class="promo-form">
                             </div>
                                                          
                         </div>                        
                           </div>
                   </div>
              </div>
        <i class="js-pdd-domik-anchor"></i>
      </div>  
                            
  
        
    <div class="new-footer">    
   Copyright &copy; <a class="new-link"><font-color="grey"> Tawi Commercial Services</font></a> 2015   
    
    </div>
    
     <div class="overlay g-hidden"></div>
     <div class="popup g-hidden">
         <div class="popup-close">×</div>
         <div class="popup-content"></div>
    </div>        

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


</body></html>