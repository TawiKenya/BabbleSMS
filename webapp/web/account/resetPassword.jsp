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

<%@page contentType="text/html" pageEncoding="UTF-8"%>

<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>


<html>
    <head>
        <title>Forgot Password</title>
    <link rel="stylesheet" type="text/css" href="../css/resetPassword.css">    
       </head>
    
    
    <body>      
        <div class="err">
        <%
        String sendEmailerr = (String) session.getAttribute(SessionConstants.EMAIL_SEND_ERROR);
        String sendEmail = (String) session.getAttribute(SessionConstants.EMAIL_SEND_SUCCESS);

        if (StringUtils.isNotEmpty(sendEmailerr)) {
            out.println("<p style = \"color:red;\">");
            out.println("Form error: " + sendEmailerr);
            out.println("</p>");
            session.setAttribute(SessionConstants.EMAIL_SEND_ERROR, null);
        }

        if(StringUtils.isNotEmpty(sendEmail)) {
            out.println("<p style='color:green;'>");
            out.println(sendEmail);
            out.println("</p>");
            session.setAttribute(SessionConstants.EMAIL_SEND_SUCCESS, null);
        }
        %>
        </div>


        <div class="container"> 

        <div class="babble">
        <img alt="Tawi Commercials" title="Tawi Commercials" src="../img/tawi-logo-with-slogan.png">                
                <h1><a href="?">BabbleSMS</a><h1></div>

        <form class="form-horizontal" action="resetPassword" method="POST">
        <ul>
        <li>

        <div class="input-prepend" title="Enter Username" data-rel="tooltip">
        <label class="complex-input auth-input" autocapitalize="off" autocorrect="off" data-nb="input">        
                <input class="input-controller" autocapitalize="off" autocorrect="off" placeholder="Enter Username" type="text" 
                                name="username" id="username" value="" required size="15">
                                        
             
                </label> 
        </div>

        </li>
        <li>

        <div class="input-prepend" title=" Enter E-mail" data-rel="tooltip">
                <label class="complex-input auth-input" autocapitalize="off" autocorrect="off" data-nb="input">            
                <input class="input-controller" autocapitalize="off" autocorrect="off" placeholder="Enter E-mail" type="text" 
                                name="email" id="email" value="" size="30" required>                        
                
                </label>  
        </div>

        </li>
        <li>
        <button class="button1" type="submit" name="submit">Reset Password</button>
        </li>
        </ul>


        </div>

        </form>

    </body>
</html>
