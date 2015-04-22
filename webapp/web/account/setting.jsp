
<%@page import="java.util.Arrays"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.log.IncomingLog"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Shortcode"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="net.sf.ehcache.CacheManager"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@page import="java.util.List"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.logs.IncomingLogDAO"%>

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
    Cache accountCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID);
    Cache contactsCache = mgr.getCache(CacheVariables.CACHE_CONTACTS_BY_UUID);

    HashMap<String, String> shortcodeHash = new HashMap<String, String>();
    HashMap<String, String> contactHash = new HashMap<String, String>();

    List<Account> accountlist = new ArrayList();
    
    Element element;
    List keys;
    Account account;
    Contact contct;

    keys = accountCache.getKeys();
    for (Object key : keys) {
        element = accountCache.get(key);

        account = (Account) element.getObjectValue();
        if (accountuuid.equals(account.getUuid())) {  
        accountlist.add(account);
       }
    }
  
	if(contactsCache!=null){
    keys = contactsCache.getKeys();
    for (Object key : keys) {
        element = contactsCache.get(key);
        contct = (Contact) element.getObjectValue();
        contactHash.put(contct.getUuid(), contct.getName());
    }}

    

%> 
<jsp:include page="messageheader.jsp" />

<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">Your Setting</a>
        </li>
    </ul>
</div>




<div class="row-fluid sortable">
    <div class="box span12">
         <%			
                            String addErrStr = (String) session.getAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_ERROR_KEY);
                            String addSuccessStr = (String) session.getAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_SUCCESS_KEY);

                            HashMap<String, String> paramHash = (HashMap<String, String>) session.getAttribute(
      SessionConstants.CLIENT_EDIT_ACCOUNT_PARAMETERS);
			
                            if (paramHash == null) { 
                                paramHash = new HashMap<String, String>();System.out.println("test14");
                            }

                            if (StringUtils.isNotEmpty(addErrStr)) {
                                out.println("<p class=\"error\">");
                                out.println("Form error: " + addErrStr);
                                out.println("</p>");
                                session.setAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_ERROR_KEY, null);
                            }

                            if (StringUtils.isNotEmpty(addSuccessStr)) {
                                out.println("<p class=\"success\">");
                                out.println("You have successfully edited your settings.");
                                out.println("</p>");
                                session.setAttribute(SessionConstants.CLIENT_EDIT_ACCOUNT_SUCCESS_KEY, null);
                          
                            }
                        %>
        <div class="box-header well" data-original-title>
            
            <h2><i class="icon-edit"></i> Edit Settings</h2>            
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">
            <form class="form-horizontal" action="editaccounts" method="POST">
                 <fieldset>
                     <%    
                        int count = 1;
                        if (accountlist != null) {
                            for (Account code : accountlist) {
                                
                    %>
                    <input type="hidden" name="accuuid" value="<%= code.getUuid() %>"/>
                    <div class="control-group">
                        <label class="control-label" for="network">Names</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="receiver" type="text" name="names" value="<%= code.getName() %>" required="true">
                            
                        </div>
                    
                    </div>    
                            
                    
                    
                       
                            
                    <div class="control-group">
                        <label class="control-label" for="network">Phone Number</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="receiver" type="text" name="phone" value="<%= code.getMobile() %>" required="true">
                            
                        </div>
                    
                    </div>    
                            
                    <div class="control-group">
                        <label class="control-label" for="network">Email</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="receiver" type="text" name="email" value="<%= code.getEmail() %>" required="true">
                            
                        </div>
                    
                    </div>    
                            
                           
                                      <%
                                                          count++;
                                                              }
                                                          }                                                      
                                                     %>
                    
                      <div class="form-actions">

                        <button type="submit" name="sendsms" value="Send" class="btn btn-primary">Edit</button>
                     </div>
                </fieldset>
            </form>

        </div>


	<div class="form-actions" data-original-title>
            
            <h2><i class="icon-edit"></i> Edit Password</h2>            
        </div>
	<div class="box-content">
            <form class="form-horizontal" action="editpassword" method="POST">
                 <fieldset>
                     <%    
                        int counts = 1;
                        if (accountlist != null) {
                            for (Account codes : accountlist) {
                                
                    %>
                    <input type="hidden" name="accuuid" value="<%= codes.getUuid() %>"/>
	<div class="control-group">
                        <label class="control-label" for="network">Old Password</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="receiver" type="password" name="oldpassword" value="<%= codes.getLogpassword() %>" required="true">
                            
                        </div>
                    
                    </div> 
	<div class="control-group">
                        <label class="control-label" for="network">New Password</label>
                        <div class="controls">
                            <input class="input-xlarge focused" id="receiver" type="password" name="newpassword" value=""required="true">
                            
                        </div>
                    
                    </div>
	<div class="control-group">
                        <label class="control-label" for="network">Confirm Password</label>
                        <div class="controls">
                       <input class="input-xlarge focused" id="receiver" type="password" name="confirmpassword" value="" required="true">
                            
                        </div>
                    
                    </div> 

	 <%
                                                          counts++;
                                                              }
                                                          }                                                      
                                                     %>
                    
                      <div class="form-actions">

                        <button type="submit" name="sendsms" value="Send" class="btn btn-primary">Edit Password</button>
                     </div>
                </fieldset>
            </form>

        </div>


    </div><!--/span-->

</div><!--/row-->



<jsp:include page="footer.jsp" />
