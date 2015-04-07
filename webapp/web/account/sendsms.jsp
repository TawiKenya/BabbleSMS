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

<%@page import="ke.co.tawi.babblesms.server.beans.account.AccountBalance"%>
<%@page import="ke.co.tawi.babblesms.server.beans.log.OutgoingGrouplog"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Credit"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Email"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Group"%>
<%@page import="ke.co.tawi.babblesms.server.beans.messagetemplate.MessageTemplate"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Mask"%>
<%@page import="ke.co.tawi.babblesms.server.beans.log.OutgoingLog"%>
<%@page import="ke.co.tawi.babblesms.server.beans.maskcode.Shortcode"%>

<%@page import="ke.co.tawi.babblesms.server.persistence.items.accounts.AccountsDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.maskcode.MaskDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.maskcode.ShortcodeDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.credit.CreditDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO"%>
<%@page import= "ke.co.tawi.babblesms.server.persistence.items.accounts.AccountBalanceDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.logs.OutgoingGroupLogDAO"%>

<%@page import="ke.co.tawi.babblesms.server.threads.SendSMS.SendSMS"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.HashMap"%>

<%@page import="net.sf.ehcache.CacheManager"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="org.apache.commons.lang3.StringEscapeUtils"%>

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

    Account account = new Account(); 
   
    CacheManager mgr = CacheManager.getInstance();
    Cache accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
    Cache shortcodeCache = mgr.getCache(CacheVariables.CACHE_SHORTCODE_BY_UUID);
    Cache maskCache = mgr.getCache(CacheVariables.CACHE_MASK_BY_UUID);
    Cache networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
    Cache messagetemplateCache = mgr.getCache(CacheVariables.CACHE_MESSAGE_TEMPLATE_BY_UUID);
    Cache groupCache = mgr.getCache(CacheVariables.CACHE_GROUP_BY_UUID);
    Cache contactsCache = mgr.getCache(CacheVariables.CACHE_CONTACTS_BY_UUID);
    Cache phoneCache = mgr.getCache(CacheVariables.CACHE_PHONE_BY_UUID);


    Element element;
    if ((element = accountsCache.get(username)) != null) {
        account = (Account) element.getObjectValue();
    }


    HashMap<String, Phone> phoneHash = new HashMap();
    HashMap<String, String> networkHash = new HashMap();

    List<Network> networkList = new ArrayList();
    List<Shortcode> shortcodelist = new ArrayList();
    List<Mask> masklist = new ArrayList();
    List<MessageTemplate> list = new ArrayList();
    List<Group> contactsgrpList = new ArrayList<Group>();
    List<Contact> contactlist = new ArrayList();

    //Element element;
    List keys;
    Shortcode shortcode;
    Mask mask;
    Network network;
   // Account account;
    Contact contacts;
    Phone phone;

    MessageTemplate messageTemplate;
    List<Phone> list2 = new ArrayList();
    Group cgroup = new Group();
/*
    keys = networksCache.getKeys();
    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkList.add(network);
    }

    keys = shortcodeCache.getKeys();
    for (Object key : keys) {
        element = shortcodeCache.get(key);
        shortcode = (Shortcode) element.getObjectValue();
        if (account.getUuid().equals(shortcode.getAccountuuid())) {
            shortcodelist.add(shortcode);
        }
    }

    keys = maskCache.getKeys();
    for (Object key : keys) {
        element = maskCache.get(key);
        mask = (Mask) element.getObjectValue();
        if (account.getUuid().equals(mask.getAccountuuid())) {
            masklist.add(mask);
        }
    }

    keys = messagetemplateCache.getKeys();
    for (Object key : keys) {
        element = messagetemplateCache.get(key);
        messageTemplate = (MessageTemplate) element.getObjectValue();
        if (account.getUuid().equals(messageTemplate.getAccountuuid())) {

            list.add(messageTemplate);
        }

    }

    keys = groupCache.getKeys();
    for (Object key : keys) {

        element = groupCache.get(key);
        cgroup = (Group) element.getObjectValue();
        if (account.getUuid().equals(cgroup.getAccountsuuid())) {
            contactsgrpList.add(cgroup);
        }
    }

    keys = contactsCache.getKeys();
    for (Object key : keys) {
        element = contactsCache.get(key);
        contacts = (Contact) element.getObjectValue();
        if (account.getUuid().equals(contacts.getAccountUuid())) {

            contactlist.add(contacts);
        }

    }

    keys = phoneCache.getKeys();
    for (Object key : keys) {
        element = phoneCache.get(key);
        phone = (Phone) element.getObjectValue();
        phoneHash.put(phone.getContactsuuid(), phone);
    }
    
    keys = networksCache.getKeys();
    for (Object key : keys) {
        element = networksCache.get(key);
        network = (Network) element.getObjectValue();
        networkHash.put(network.getUuid(), network.getName());
    }
*/
%>
<jsp:include page="messageheader.jsp" />

<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
            <a href="#">new sms</a>
        </li>
    </ul>
</div>

<div class="row-fluid sortable">
    <div class="box span12">
        <div class="box-header well" data-original-title>
            <h2><i class="icon-edit"></i> new sms</h2>
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>
        <div class="box-content">
            <%                String addErrStr = (String) session.getAttribute(SessionConstants.SENT_ERROR);
                String addSuccessStr = (String) session.getAttribute(SessionConstants.SENT_SUCCESS);
                 //display errors
                if (StringUtils.isNotEmpty(addErrStr)) {
                    out.println("<p style='color:red'>");
                    out.println(addErrStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADD_ERROR, null);
                }

                if (StringUtils.isNotEmpty(addSuccessStr)) {
                    out.println("<p style='color:green'>");
                    out.println(addSuccessStr);
                    out.println("</p>");
                    session.setAttribute(SessionConstants.ADD_SUCCESS, null);
                }
            %>
            <form id="sendsms" class="form-horizontal" action="sendsms.jsp" method="POST">
                <fieldset>
                    <div class="control-group">
                        <label class="control-label" for="destination">TO:</label>
                        <div class="controls">
                            <div id="destination">
                                <input class="input-xlarge focused" id="receiver" type="text" name="destination" value="" required="true">
                            </div>
                        </div>
                    </div>
                    <div class="control-group">
                        <label class="control-label" for="source">Source:</label>
                        <div class="controls">
                            <select name="source" id="source" required="true">
                                <%
                                    //for mask
                                    int count = 1;
                                    if (masklist != null) {
                                        for (Mask code : masklist) {
                                %>
                                <option value="<%= code.getMaskname()%>"><%= code.getMaskname()+'('+networkHash.get(code.getNetworkuuid())+')'%></option>
                                <%
                                            count++;
                                        }

                                    }
                                    //for shortcode
                                    count = 1;
                                    if (shortcodelist != null) {
                                        for (Shortcode code : shortcodelist) {
                                %>
                                <option value="<%= code.getCodenumber()%>"><%=code.getCodenumber()+'('+networkHash.get(code.getNetworkuuid())+')'%></option>
                                <%
                                            count++;
                                        }

                                    }


                                %>
                            </select>
                        </div>
                    </div>    
                    <div class="control-group">
                        <label class="control-label" for="network">Network:</label>
                        <div class="controls">
                            <select name="network[]" multiple required="true">
                                <%                                    count = 1;
                                    for (Network code : networkList) {
                                %>
                                <option value="<%= code.getUuid()%>"><%= code.getName()%></option>
                                <%
                                        count++;
                                    }
                                %>
                            </select>
                        </div>
                    </div>

                    <div class="control-group">
                        <label class="control-label" for="message">Message Template:</label>
                        <div class="controls">
                            <select name="msgtemplate" id="msgtemplate" >
                                <option value="">Please select one</option>
                                <%
                                    count = 1;
                                    if (list != null) {
                                        for (MessageTemplate code : list) {
                                %>
                                <option value="<%= code.getContents()%>"><%= code.getTemplatetitle()%></option>
                                <%
                                            count++;
                                        }
                                    }
                                %>
                            </select>
                        </div>
                    </div>


                    <div class="control-group">
                        <div class="controls">
                            <button type="submit" id="template" name="template" value="Template" class="btn btn-primary" >Use Template</button>

                        </div>         
                    </div>          

                    <div class="control-group">
                        <label class="control-label" for="message">Message:</label>
                        <div class="controls">
                            <textarea cols="200" rows="6" class="input-xlarge focused"  id="messaged" name="message" required="true"></textarea>
                        </div>
                        <div class="controls">
                            <label  class="control-label" for="message" id="sms"> SMS</label><label class="control-label" for="message" id="count"> Characters</label>
                        </div>
                    </div>          

                    <div id="dialog-confirm">

                    </div>
                    <div class="form-actions">


                        <button type="submit" name="sendsms" id="send" value="Send" class="btn btn-primary">Send</button>
                        <!--<button class="btn">Trash       </button>-->
                    </div>
                </fieldset>
            </form>

        </div>
    </div><!--/span-->

</div><!--/row-->



<jsp:include page="footer.jsp" />
