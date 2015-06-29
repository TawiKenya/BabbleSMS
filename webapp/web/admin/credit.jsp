



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
<script>
    //only number can be entered
    var specialKeys = new Array();
    specialKeys.push(8); //Backspace
    function IsNumeric(e) {
        var keyCode = e.which ? e.which : e.keyCode
        var ret = ((keyCode >= 48 && keyCode <= 57) || specialKeys.indexOf(keyCode) != -1);

        return ret;
    }

</script>


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Credit</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">
    <div class="box span12">
        
        <%        
           String addErrStr = (String) session.getAttribute(SessionConstants.ADMIN_ADD_CREDIT_ERROR_KEY);
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
                out.println("You have successfully added credit.");
                out.println("</p>");
                session.setAttribute(SessionConstants.ADMIN_ADD_CREDIT_SUCCESS_KEY, null);
                session.setAttribute(SessionConstants.ADMIN_UPDATE_SUCCESS,null);

            }
        %>
        <div class="box-header well" data-original-title>

            <h2><i class="icon-edit"></i> add credit</h2>            
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">
            <form class="form-horizontal" action="../addcredit" method="POST">
                <fieldset>
                    <div class="control-group">  
                        <label class="control-label" for="network">User*</label>
                        <div class="controls">
                            <select name="user" id="user" required="true">
                                <option value="">Please select one</option> 
                                <%
                                    int count = 1;
                                    if (userList != null) {
                                        for (Account code : userList) {
                                %>
                                <option value="<%= code.getUuid()%>" onclick="setSource(this)"><%= code.getUsername()%></option>
                                <%
                                            count++;
                                        }
                                    }
                                %>
                            </select>
                        </div>

                    </div>  

                    <div class="control-group">
                        <label class="control-label" for="name">Source*</label>
                        <div class="controls" id ="getsource">
                        <select name="source" id="source" >
                        <!--options appear here-->
                        </select>                            
                        </div>

                    </div> 

                    <div class="control-group">
                        <label class="control-label" for="name">Amount*</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="receiver" type="text" name="amount" value="" onkeypress="return IsNumeric(event);" required="true">



                        </div>

                    </div> 


                    <div class="form-actions">

                        <button type="submit" name="sendsms" value="Send" class="btn btn-primary">Add</button>
                    </div>
                </fieldset>
            </form>


        </div>
    </div><!--/span-->

</div><!--/row-->


<script type="text/javascript"  src ="../js/tawi/admingetsource.js"></script>




<jsp:include page="footer.jsp" />
<script>

    //code for auto populating source on selecting account
    $(document).ready(function() {
        var basepath = window.location.protocol + "//" + window.location.host + "/";
        var pathArray = window.location.pathname.split('/');
        var url = basepath + pathArray[1] + '/' + pathArray[2] + '/ajaxcredit.jsp?accountuuid=';

        $("#user").change(function() {
            var val = $(this).val();
            $.ajax({
                url: url + val,
                type: "GET",
                cache: false,
            }).done(function(data) {
                $('#source').empty();
                $('#source').append("<option value=''>Please select one</option>");
                $('#source').append(data);
            });

        });//end on change here.
    });
</script>
