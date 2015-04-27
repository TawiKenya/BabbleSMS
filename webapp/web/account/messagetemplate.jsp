<%
    /**
    Copyright 2015 Tawi Commercial Services Ltd

    Licensed under the Open Software License, Version 3.0 (the “License”); you may 
    not use this file except in compliance with the License. You may obtain a copy 
    of the License at:
    http://opensource.org/licenses/OSL-3.0

    Unless required by applicable law or agreed to in writing, software distributed 
    under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR
    CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and limitations 
    under the License.
    */
%>

<%@page import="ke.co.tawi.babblesms.server.beans.messagetemplate.MessageTemplate"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Email"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.HashMap"%>
<%@page import="java.util.List"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>

<%@page contentType="text/html" pageEncoding="UTF-8"%>


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

    String accountuuid = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_ACCOUNTUUID);
    CacheManager mgr = CacheManager.getInstance();
    Cache messagetemplateCache = mgr.getCache(CacheVariables.CACHE_MESSAGE_TEMPLATE_BY_UUID);
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
    Cache phoneCache = mgr.getCache(CacheVariables.CACHE_PHONE_BY_UUID);
    Cache emailCache = mgr.getCache(CacheVariables.CACHE_EMAIL_BY_UUID);

    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, String> networkHash = new HashMap();
    HashMap<String, Phone> phoneHash = new HashMap();
    HashMap<String, Email> emailHash = new HashMap();

    Element element;
    MessageTemplate messageTemplate;
    

    List<MessageTemplate> list = new ArrayList();
    List keys;

    keys = messagetemplateCache.getKeys();
    for (Object key : keys) {
        element = messagetemplateCache.get(key);
        messageTemplate = (MessageTemplate) element.getObjectValue();
          if (accountuuid.equals(messageTemplate.getAccountuuid())) {

            list.add(messageTemplate);
       
          }
    }
   
%> 
<jsp:include page="messageheader.jsp" />


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            View contacts
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        <%                String addErrStr = (String) session.getAttribute(SessionConstants.ADD_ERROR);
                String addSuccessStr = (String) session.getAttribute(SessionConstants.ADD_SUCCESS);
                
                String deleteErrStr = (String) session.getAttribute(SessionConstants.DELETE_ERROR);
                String deleteSuccessStr = (String) session.getAttribute(SessionConstants.DELETE_SUCCESS);
                
                String updateErrStr = (String) session.getAttribute(SessionConstants.UPDATE_ERROR);
                String updateSuccessStr = (String) session.getAttribute(SessionConstants.UPDATE_SUCCESS);

                if (StringUtils.isNotEmpty(addErrStr)) {
                    out.println("<p style='color:red;'>");
                    out.println("Form error: " + addErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADD_ERROR, null);
                }

                if(StringUtils.isNotEmpty(addSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(addSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADD_SUCCESS, null);
                }
                
                if (StringUtils.isNotEmpty(deleteErrStr)) {
                    out.println("<p style='color:red;'>");
                    out.println("Form error: " + deleteErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.DELETE_ERROR, null);
                }

                if (StringUtils.isNotEmpty(deleteSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(deleteSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.DELETE_SUCCESS, null);
                }
                
                if (StringUtils.isNotEmpty(updateErrStr)) {
                    out.println("<p style='color:red;'>");
                    out.println("Form error: " + updateErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.UPDATE_ERROR, null);
                }

                if (StringUtils.isNotEmpty(updateSuccessStr)) {
                    out.println("<p style='color:green;'>");
                    out.println(updateSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.UPDATE_SUCCESS, null);
                }
            %>
        <div class="box-header well" data-original-title>
            <a class="btn" href="messagetemplate.jsp" title="view accounts" data-rel="tooltip">View</a>                  
            <a class="btn" href="addmsgtemplate.jsp" title="add accounts" data-rel="tooltip">Add</a>  

        </div>
        <div class="box-content">
            <table class="table table-striped table-bordered bootstrap-datatable datatable">
                <thead>
                    <tr>
                        <th>*</th>

                        <th>Template Title</th>
                        <th>Contents</th>
                        <th hidden>actions</th>
                    </tr>
                </thead>   
                <tbody class="templatestable">
                    <%
                        int count = 1;
                        if (list != null) {
                            for (MessageTemplate code : list) {
                    %>
                    <tr>

                        <td width="10%"><%=count%></td>
                        <td class="center"><a href="#"><%=code.getTitle()%></a></td>
                        <td class="center"><%=code.getContents()%></td>
                        <td class="center"  hidden><%=code.getUuid()%></td>



                    </tr>

                    <%
                                count++;
                            }
                        }
                    %>
                </tbody>
            </table>            
        </div>
    </div><!--/span-->

</div><!--/row-->
<div id="contactdiv" style="display:none;">
<form class="form" onsubmit="return formValidator()" action = "EditTemplate" method = "POST" id="contact" >

<b>Template's Details</b>
<label>Title: <span></span></label>
<input type="text" id="title" name = "title" />
<label>Contents <span></span></label>
<textarea cols="3" rows="2" id ="contents" name = "contents"></textarea>
<input type="hidden" id="templateuuid" name = "templateuuid" class="edit_area" />

<br>
<input type="submit"  value="Save" />
<input type="submit" id="cancel" value="Cancel"/>


<br/>

</form>
</div>


<!-- Contact Form  for the pop up ends-->

<jsp:include page="footer.jsp" />