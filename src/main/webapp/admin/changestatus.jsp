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
<%@page import="java.util.ArrayList"%>
<%@page import="java.util.LinkedList"%>
<%@page import="ke.co.tawi.babblesms.server.accountmgmt.admin.SessionConstants"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="net.sf.ehcache.CacheManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="java.util.List"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO"%>



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

    //String accountuuid = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_ACCOUNTUUID);
    CacheManager mgr = CacheManager.getInstance();
    Cache accountCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID);
    
    
    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, String> networkHash = new HashMap<String, String>();
    
    Account account = new Account();
    Element element;
    if ((element = accountCache.get(username)) != null) {
        account = (Account) element.getObjectValue();
    }
   

    List<Account> userList = new ArrayList();
   
    List keys;

    keys = accountCache.getKeys();
    for (Object key : keys) {
        element = accountCache.get(key);
        account = (Account) element.getObjectValue();
        userList.add(account);
    }
    
  

%> 
<jsp:include page="header.jsp" />

<div class="row-fluid sortable">
    <div class="box span12">
        
        <%        
          /* String addErrStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_CREDIT_ERROR_KEY);
            String addSuccessStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_CREDIT_SUCCESS_KEY);
            HashMap<String, String> paramHash = (HashMap<String, String>) session.getAttribute(
                    SessionConstants.ADMIN_ADD_CREDIT_PARAMETERS);

            if (paramHash == null) {
                paramHash = new HashMap<String, String>();
            }

            if (StringUtils.isNotEmpty(addErrStr)) {
                out.println("<p style='color:red;'>");
                out.println("Form error: " + addErrStr);
                out.println("</p>");
                session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_ERROR_KEY, null);
                session.setAttribute(SessionConstants.ADMIN_UPDATE_ERROR,null);
            }

            if (StringUtils.isNotEmpty(addSuccessStr)) {
                out.println("<p class=\"success\">");
                out.println("You have successfully changed account status.");
                out.println("</p>");
                session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_SUCCESS_KEY, null);
                session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS,null);

            } */
        %>
        <div class="box-header well" data-original-title>

            <h2><i class="icon-edit"></i> change account status</h2>            
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">
            <form class="form-horizontal" action="../deleteaccount" method="POST">
                <fieldset>
                    <div class="control-group">  
                        <label class="control-label" for="network">Status</label>
                        <div class="controls">
                            <select name="status" id="status" required="true">
                                <option value="">Please select one</option> 
                         <option value="396F2C7F-961C-5C12-3ABF-867E7FD029E6" selected>Active</option>
                                <option value="19CAAC90-0D72-59D4-1DC1-2C86808459F9"> Suspended</option>
                                <option value="5A13538F-AC41-FDE2-4CD6-B939FA03123B"> Pending</option>
                                 <option value="8E1DEF0F-4DCC-E13B-F89D-35181AD4003D">Unknown</option>
                                <option value="34e05991-5cfa-4fcb-bfbf-4281d77a388c"> Deleted</option>
                            </select>
                        </div>

                    </div>  

                    <div class="control-group">
                        <label class="control-label" for="name">Account*</label>
                        <div class="controls" id ="getsource">
                        <select name="account" id="account" >
                        <option value="">Please select one</option> 

                        <%
                                    int count = 1;
                                    if (userList != null) {
                                        for (Account code : userList) {
                                %>
                         <option value="<%= code.getUuid()%>" ><%= code.getUsername()%> </option>
                                <%
                                            count++;
                                        }
                                    }
                                %>
                        </select>                            
                        </div>

                    </div> 


                    </div> 


                    <div class="form-actions">

                <button type="submit" name="sendsms" value="Send" class="btn btn-primary">Change</button>
                    </div>
                </fieldset>
            </form>


        </div>
    </div><!--/span-->

</div><!--/row-->


<script type="text/javascript"  src ="../js/tawi/admingetsource.js"></script>




<jsp:include page="footer.jsp" />
