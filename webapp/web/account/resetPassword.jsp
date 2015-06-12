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

    <style type="text/css">

        body{
          background-image:url("../img/background.jpg");
          background-repeat: no-repeat;
          background-size: cover;

        }

         .container{
                border-radius: 50px;
            background-color: green;
            color: ;
            position: absolute;
            width: 40%;
            height: 40%;
            margin: auto;
            top: 25%;
            left: 30%;
            text-align: center;

         }

         .babble{
            background-color: #fff;
              border-radius: 10px;
         }

        li{
            display: inline;
            padding-right: 10px;
        }
        ul{
            text-decoration: none;
            color: #666;
        }
        form ul{
            list-style: none;
            margin-bottom: 20px;
            padding-left: 0px;
        }
        label{
            display: block;
            color: #292929;
            font-family: sans-serif;
            padding-bottom: 8px;
        }

        h1{
                font-size: 50px;
                font-family: monospace;
                letter-spacing: -1px;
                color: black;
                text-shadow: 5px -1px 0px blue,
                                        6px -2px 0px white;
        }
        .uname{

        }
        .email{

        }
        .err{
        width: 400px;
        margin: auto;
        text-align: center;
        font-size: 30px;
        }

    </style>

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

        <div class="babble"><h1> BabbleSMS <h1></div>

        <form class="form-horizontal" action="resetPassword" method="POST">
        <ul>
        <li>

        <div class="uname">
        <label class="control-label" for="username">Username:</label>
        <input class="input-xlarge focused" type="text" name="username" size="15">
        </div>

        </li>
        <li>

        <div class="email">
        <label class="control-label" for="email">Email</label>
        <input class="input-xlarge focused" type="text" name="email" id="email" size="30">  
        </div>

        </li>
        <li>
        <input type="submit" name="submit" value="Continue">
        </li>
        </ul>


        </div>

        </form>

    </body>
</html>
