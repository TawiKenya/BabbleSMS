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

<%@page import="ke.co.tawi.babblesms.server.persistence.accounts.AccountsDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.maskcode.MaskDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.maskcode.ShortcodeDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.credit.CreditDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO"%>
<%@page import= "ke.co.tawi.babblesms.server.persistence.accounts.AccountBalanceDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.logs.OutgoingGroupLogDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.messageTemplate.MessageTemplateDAO"%>

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
	List<Contact> contactList = new ArrayList();
	GroupDAO gDAO=new GroupDAO();
         contactsgrpList = gDAO.getGroups(account);
	ContactDAO cDAO =  ContactDAO.getInstance();
	contactList= cDAO.getContacts(account);
   MaskDAO maskDAO = MaskDAO.getInstance();
   ShortcodeDAO shortcodeDAO = ShortcodeDAO.getInstance();
   NetworkDAO networkDAO = NetworkDAO.getInstance();
   MessageTemplateDAO msgtemplDAO = MessageTemplateDAO.getInstance();
  

   masklist =maskDAO.getmaskbyaccount(account.getUuid());
   shortcodelist = shortcodeDAO.getShortcodebyaccountuuid(account.getUuid());
   list = msgtemplDAO.getAllMessageTemplatesbyuuid(account.getUuid());

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
           <!-- <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>-->
        </div>
        <div class="box-content" style="margin-top:4%">
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
            <form id="sendsms" name = "myform" class="form-horizontal" action="sendsms.jsp" method="POST">
                <fieldset>
                    <div class="control-group" id ="grouptable">
                        <label class="control-label" for="destination">TO:</label>
				
                        <div class="controls">
                           <!-- <div id="destination">
                                <input class="input-xlarge focused" id="receiver" type="text" name="destination" value="" required="true">
                            </div> -->
			<select name ="destination" id="destination" required="true">
			<option value ="A Contact">A Contact</option>
			<option value = "Group">Group</option>
			<option value = "Groups">Group</option>
			</select></div>
			<!--<div class="control-group" id = "sendtocontact">
			<div class="controls">
                        <label class="control-label" id ="label2" for="destination">Select Contact:</label>
			<input class="input-xlarge focused" type="text" id="receiver" name="receiver" value="" required="true"/>
                        <button type="submit">search</button>>
                         </div> 
			</div>-->
			<div class="control-group">
			 <div class="controls">
			<table id='scroll2'  class="table table-striped table-bordered">
			<thead>
				<tr>
				    <th>Select Group</th>
				</tr>
			    </thead>
			<%
	
			if (contactsgrpList != null) {

			 for (Group code : contactsgrpList) {
			%>

			<tr style="width:30px">
			   
			 <td class="center"><a href="#"><%=code.getName()%></a></td>
			 <td class="center" id ="hideANDseek"><%=code.getUuid()%></td>
			</tr>
			<%  
			}
			    } 
			 
			%>
			  
			 
			</table>
			</div>
			</div>
			<div class="control-group">
			 <div class="controls">
			<table id="scroll3" style="height:200px;" class="table table-striped table-bordered">
			<thead>
				<tr>
				    <th>Select Contact</th>
				</tr>
			    </thead>
			<%
	
			if (contactList != null) {

			 for (Contact code : contactList) {
			%>

			<tr >
			   
			 <td class="center"><a href="#"><%=code.getName()%></a></td>
			 <td class="center" id ="hideANDseek"><%=code.getUuid()%></td>
			</tr>
			<%  
			}
			    } 
			 
			%>
			  
			 
			</table>
			</div>
			</div>
			<div class="control-group" id ="destinationdiv"> 
			<label class="control-label" id ="label1" for="destinations">Select Group:</label>
			<div class="controls">
			<select name ="destination" id="destination" required="true">
			<%if (contactsgrpList != null) {

			 for (Group code : contactsgrpList) {
			%>
			<option value ="<%=code.getName()%>"><%=code.getName()%></option>
			<%  
			}
			    } 
			 
			%>
			</select></div>
			</div>
			<input type="hidden"  class ="groupselected" name="groupselected"  />
			<input type="hidden"  class ="contactselected" name="contactselected"  />
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
                                <option value="<%= code.getMaskname()%>"><%= code.getMaskname()+'('+networkDAO.getNetwork(code.getNetworkuuid()).getName()+')'%></option>
                                <%
                                            count++;
                                        }

                                    }
                                    //for shortcode
                                    count = 1;
                                    if (shortcodelist != null) {
                                        for (Shortcode code : shortcodelist) {
                                %>
                                <option value="<%= code.getCodenumber()%>"><%=code.getCodenumber()+'('+networkDAO.getNetwork(code.getNetworkuuid()).getName()+')'%></option>
                                <%
                                            count++;
                                        }

                                    }


                                %>
                            </select>
                        </div>
                    </div>    
                    <!--<div class="control-group">
                        <label class="control-label" for="network">Network:</label>
                        
                    </div>-->

                    <div class="control-group">
                        <label class="control-label" for="message">Message Template:</label>
                        <div class="controls">
                            <select name="msgtemplate" id="msgtemplate" >
                                <option class = "add_field_button" value="">Please select one</option>
                                <%
                                    count = 1;
                                    if (list != null) {
                                        for (MessageTemplate code : list) {
                                %>
                                <option class = "add_field_button" value="<%= code.getContents()%>"><%= code.getTemplatetitle()%></option>
                                
                                <%
                                            count++;
                                                                                  }
                                    }
                                %>
                            </select>
                        </div>
                    </div>       

                    <div class="control-group">
                        <label class="control-label" for="message">Message:</label>
                        <div class="controls">
                            <textarea cols="200" rows="6" class="input-xlarge focused input_fields_wrap"  id="messaged" name="message" required="true"onkeyup="countChar(this)"></textarea>
                        </div>
                       <div class="controls">
                            <label  class="control-label" for="message" id="sms"> SMS  <quote id="smsNum"></quote></label><label class="control-label" for="message" id="count"> Characters <quote id="charCount"></quote></label>
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
