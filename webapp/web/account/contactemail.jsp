
<%@page import="ke.co.tawi.babblesms.server.persistence.items.contacts.EmailDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Email"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="java.util.ArrayList"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="java.util.HashMap"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="net.sf.ehcache.CacheManager"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>


<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="java.util.List"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.contacts.ContactsDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.network.NetworkDAO"%>

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
    Cache contactsCache = mgr.getCache(CacheVariables.CACHE_CONTACTS_BY_UUID);
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
    Cache phoneCache = mgr.getCache(CacheVariables.CACHE_PHONE_BY_UUID);
    Cache emailCache = mgr.getCache(CacheVariables.CACHE_EMAIL_BY_UUID);

    // This HashMap contains the UUIDs of Contacts as keys and the names of Contacts as values
    HashMap<String, String> networkHash = new HashMap();
    HashMap<String, Phone> phoneHash = new HashMap();
    HashMap<String, Email> emailHash = new HashMap();

    Element element;
    Network network;
    Phone phone;
    Email email;
    Contact contacts;

    List<Contact> list = new ArrayList();
    List<Email> list2 = new ArrayList();
    List keys;

    keys = contactsCache.getKeys();
    for (Object key : keys) {
        element = contactsCache.get(key);
        contacts = (Contact) element.getObjectValue();
        if (accountuuid.equals(contacts.getAccountsuuid())) {

            list.add(contacts);
        }

    }
    keys = networksCache.getKeys();
    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash.put(network.getUuid(), network.getName());
    }

    keys = phoneCache.getKeys();
    for (Object key : keys) {
        element = phoneCache.get(key);
        phone = (Phone) element.getObjectValue();
        phoneHash.put(phone.getContactsuuid(), phone);
    }

    keys = emailCache.getKeys();
    for (Object key : keys) {
        element = emailCache.get(key);
        email = (Email) element.getObjectValue();
        emailHash.put(email.getContactsuuid(), email);
    }
%> 
<jsp:include page="contactheader.jsp" />


<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">View Emails</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">		
    <div class="box span12">
        <div class="box-header well" data-original-title>

            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">
             <%                String addErrStr = (String) session.getAttribute(SessionConstants.ADD_ERROR);
                String addSuccessStr = (String) session.getAttribute(SessionConstants.ADD_SUCCESS);
                
                String deleteErrStr = (String) session.getAttribute(SessionConstants.DELETE_ERROR);
                String deleteSuccessStr = (String) session.getAttribute(SessionConstants.DELETE_SUCCESS);
                
                String updateErrStr = (String) session.getAttribute(SessionConstants.UPDATE_ERROR);
                String updateSuccessStr = (String) session.getAttribute(SessionConstants.UPDATE_SUCCESS);

                if (StringUtils.isNotEmpty(addErrStr)) {
                     out.println("<p class='error'>");
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
                     out.println("<p class='error'>");
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
                     out.println("<p class='error'>");
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
            <table class="table table-striped table-bordered bootstrap-datatable datatable">
                <thead>
                    <tr>
                        <th>*</th>

                        <th>contact name</th>
                        <th>Email</th>                       
                        <th>actions</th>
                    </tr>
                </thead>   
                <tbody>
                    <%
                        int count = 1;
                        if (list != null) {
                            for (Contact code : list) {
                                //for contact with multiple numbers  
                                EmailDAO emailDAO = EmailDAO.getInstance();
                                list2 = emailDAO.getAllEmailbyContact(code.getUuid());
                                int count2 = 1;
                                if (list != null) {
                                    for (Email code2 : list2) {
                    %>
                    <tr>

                        <td width="10%"><%=count%></td>
                        <td class="center"><%=code.getName()%></td>
                        <td class="center"><%=code2.getAddress()%></td>          


                        <td class="center">
                            <form name="edit" method="post" action="edit.jsp"> 
                                <input type="hidden" name="name" value="<%=code.getName()%>">
                                <input type="hidden" name="emailaddr" value="<%=emailHash.get(code.getUuid()).getAddress()%>">
                                <input type="hidden" name="emailuuid" value="<%=emailHash.get(code.getUuid()).getUuid()%>">
                                <input type="hidden" name="contactuuid" value="<%=code.getUuid()%>">
                                <input class="btn btn-success" type="submit" name="editEmail" id="submit" value="Edit" /> 
                            </form>
                            <form name="delete" method="post" action="deleteemail">
                                <input type="hidden" name="emailuuid" value="<%=emailHash.get(code.getUuid()).getUuid()%>">
                                <input type="hidden" name="contactuuid" value="<%=code.getUuid()%>">
                                <input class="btn btn-success" type="submit" name="deleteEmail" id="submit" value="Delete" /> 
                            </form>
                        </td>



                    </tr>

                    <%        count2++;
                                    }
                                }
                                count++;
                            }
                        }
                    %>
                </tbody>
            </table>            
        </div>
    </div><!--/span-->

</div><!--/row-->


<jsp:include page="footer.jsp" />
