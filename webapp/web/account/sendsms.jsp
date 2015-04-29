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
<%@page import="ke.co.tawi.babblesms.server.persistence.accounts.AccountBalanceDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.logs.OutgoingGroupLogDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.network.NetworkDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.template.MessageTemplateDAO"%>
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

<style type= "text/css">
.tokenize-sample { width: 300px ;}
</style>

<script type="text/javascript" src="../js/jquery.tokenize.js"></script>
<link rel="stylesheet" type="text/css" href="../css/jquery.tokenize.css" />


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
    List<Contact> contactList = new ArrayList();

    GroupDAO gDAO = new GroupDAO();
    contactsgrpList = gDAO.getGroups(account);
    ContactDAO cDAO =  ContactDAO.getInstance();
    contactList= cDAO.getContacts(account);

    PhoneDAO phoneDAO = PhoneDAO.getInstance();

   MaskDAO maskDAO = MaskDAO.getInstance();
   ShortcodeDAO shortcodeDAO = ShortcodeDAO.getInstance();
   NetworkDAO networkDAO = NetworkDAO.getInstance();
   MessageTemplateDAO msgtemplDAO = MessageTemplateDAO.getInstance();
  

   masklist =maskDAO.getmaskbyaccount(account.getUuid());
   shortcodelist = shortcodeDAO.getShortcodebyaccountuuid(account.getUuid());
   list = msgtemplDAO.getTemplates(account);

    //Element element;
    List keys;
    Shortcode shortcode;
    Mask mask;
    Network network;
   // Account account;
    Contact contacts;
    

    MessageTemplate messageTemplate;
    List<Phone> list2 = new ArrayList();
    Group cgroup = new Group();

/** Declare and initialize variables to be used for crediting**/

int credit_Balance = 0;
int credit_Consumed = 0;

%>
<jsp:include page="messageheader.jsp" />



<div>
    <ul class="breadcrumb">
        <li>
            <a href="#">Home</a> <span class="divider">/</span>
        </li>
        <li>
           new sms
        </li>
    </ul>
</div>



<div class="row-fluid sortable">
    <div class="box span12">
        <div class="box-header well" data-original-title>
            <h2><i class="icon-edit"></i> new sms</h2>

        </div>
        <div class="box-content">
            <%                
                String addErrStr = (String) session.getAttribute(SessionConstants.SENT_ERROR);
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
            

            
            <form id="sendsms" name="myform" class="form-horizontal" action="SendSMS" method="POST">
                <fieldset>
                    <div class="control-group" id ="grouptable">
                        <label class="control-label" for="destination">TO:</label>
                        
                        
                
                        <div class="controls">
                           
            <select name ="destination" id="destination" required="true">
            <option value ="Choose">Choose Groups or Contacts</option>
            <option value = "Group">Group</option>
            
            <option value = "Contact">Contact(s)</option>
            </select></div>
                        
                     <div id="credittable">
                                <table border="1">
                                    <tr width="5%">
                                        <th>NETWORK</th>
                                        <th>Credit consumed</th>
                                        <th>Balance</th>
                                    </tr>
                                   <tr width="5%">
                                        <td>Safaricom</td>
                                        <td id="creditconsumed"><%=credit_Consumed%></td>
                                        <td id="safcreditbalance"><%=credit_Balance%></td>
                                    </tr>
                                   <tr width="5%">
                                        <td>Airtel</td>
                                        <td id="airtelcreditconsumed"><%=credit_Consumed%></td>
                                        <td id="airtelcreditbalance"><%=credit_Balance%></td>
                                    </tr>
                                   <tr width="5%">
                                        <td>Orange</td>
                                        <td id="orangecreditconsumed"><%=credit_Consumed%></td>
                                        <td id="orangecreditbalance"><%=credit_Balance%></td>
                                    </tr>
                                     <tr width="5%">
                                        <td>Yu</td>
                                        <td id="yucreditconsumed"><%=credit_Consumed%></td>
                                        <td id="yucreditbalance"><%=credit_Balance%></td>
                                    </tr>
                                </table>
                     </div>
            
            <div class="control-group">
                            <div class="controls" >
                                <!-- Group table here-->
                                <div class="tablets">
                                    <p>&nbsp;&nbsp;&nbsp;</p>
                                    
                                    <table id="scroll" class="table table-striped table-bordered">
                                        <thead>
                                            <tr>
                                            <th>Select Groups</th>
                                            </tr>
                                        </thead>
                                        <tbody id ="tablet">

                                            <%
                                                if (contactsgrpList != null) {
                                                    for (Group code : contactsgrpList) {
                                            %>

                                                    <tr>
                                                        <td class="center"><a href="#"><%=code.getName()%></a></td>
                                                        <td class="center" id ="hideANDseek"><%=code.getUuid()%></td>
                                                    </tr>
                                                <%   


                                                    }// end 'for (Group code : contactsgrpList)'
                                                }// end 'if (contactsgrpList != null)' 
                                            %>

                                        </tbody>
                                    </table>
                                    
                                    <div id="groupsform">
                                        <br/><br/><br/>
                                        <button type="submit"  id ="add1" > &nbsp;&nbsp;&nbsp;&nbsp;&nbsp; Add >> </button><br/><br/>
                                        <button type="submit"  id = "remove2" > << Remove </button>

                                        <input type="hidden"  class ="groupsadded" name="groupselected"  />
                                    </div>
                                    
                                    <table id="scroll1" class="table table-striped table-bordered">
                                        <thead>
                                            <tr>
                                                <th>Selected Groups</th>
                                            </tr>
                                        </thead>
                                        <tbody id = "resulttable">

                                        </tbody>
                                    </table>

                                </div>      
                            </div>
            </div>
            
            
                        <div class="section control-group">

                            <div class="fluid">
                                <div class="span50">

                                    <label for="tokenize_simple"class="control_label">Type a contact name here  :</label>
                                    <select id="tokenize_simple" class="tokenize-sample controls" name="phones"  multiple="multiple" style="margin: 0px; padding: 0px; border: 0px none; display: none;">
                                        <%
                                            List<Phone> phoneList;

                                            for(Contact contact : contactList) { 
                                                // If a Contact has only one phone number, just print out the Contact name
                                                // Else print the Contact name and associated phone number
                                                phoneList = phoneDAO.getPhones(contact);
                                                
                                                if(phoneList.size() < 2) {
                                                    out.println("<option value=\"" + phoneList.get(0).getUuid() + 
                                                            "\">" + contact.getName() + "</option>"); 
                                                                              
                                                } else {
                                                    for(Phone phone : phoneList) {
                                        
                                                        out.println("<option value=\"" + phone.getUuid() + 
                                                            "\">" + contact.getName() + " (" + phone.getPhonenumber() +
                                                            ")</option>");                                                    

                                                    }// end 'for(Phone phone : phoneList)'

                                                }
                                            }// end 'for(Contact contact : contactList)'
                                        %>
                                    </select>
                                    
                                    <div class="tokenize-samples Tokenizer">
                                        <ul class="TokensContainer">
                                            <li class="TokenSearch">
                                            <input size="5">
                                            </li>
                                        </ul>
                                        <ul class="Dropdown"></ul>
                                    </div>
                                </div>                                
                            </div>

                            <script type="text/javascript">
                                $('select#tokenize_simple').tokenize({
                                onAddToken: function(){
                                    update_tokenize_result('#tokenize_simple', '#tokenize_result_simple');
                                },
                                onRemoveToken: function(){
                                    update_tokenize_result('#tokenize_simple', '#tokenize_result_simple');
                                }
                                });
                                    update_tokenize_result('#tokenize_simple', '#tokenize_result_simple');
                            </script>

                        </div>  

        <div class="control-group">
                        <label class="control-label" for="source">Source:</label>
                        <div class="controls">
                            <form action="sendsms.jsp">
                            <select name="source" id="source" required="true">
                                <%
                                    //for mask
                                    int count = 1;
                                    if (masklist != null) {
                                        for (Mask code : masklist) {

                                %>
                                
                                
                                    <option class="message_source" id="<%= code.getMaskname()%>" value="<%= code.getMaskname()%>" label="<%=networkDAO.getNetwork(code.getNetworkuuid()).getName()%>">
                                                       

                                        
                                        <%= code.getMaskname() + " (" + networkDAO.getNetwork(code.getNetworkuuid()).getName() + ")" %>
                                    </option>
                                <%
                                            count++;
                                        }

                                    }
                                    //for shortcode
                                    count = 1;
                                    if (shortcodelist != null) {
                                        for (Shortcode code : shortcodelist) {
                                %>
                                        <option class="message_source" id="<%=code.getCodenumber()%>" value="<%= code.getCodenumber() %>" label="<%=networkDAO.getNetwork(code.getNetworkuuid()).getName()%>">
                                      
                                            <%=code.getCodenumber() + " (" + networkDAO.getNetwork(code.getNetworkuuid()).getName() + ")"%>
                                        </option>
                                <%
                                            count++;
                                        }

                                    }
                                %>
                            </select>
                        </div>
                    </div>   
                            


                    <div class="control-group">
                        <label class="control-label" for="message">Message Template:</label>
                        <div class="controls">
                            <select name="msgtemplate" id="msgtemplate" >
                                <option class = "add_field_button" value="">Please Select One</option>
                                <%
                                    count = 1;
                                    if (list != null) {
                                        for (MessageTemplate code : list) {
                                %>
                                <option class = "add_field_button" id="<%=code.getUuid()%>" value="<%= code.getContents()%>"><%= code.getTitle()%></option>
                                
                                <%
                                            count++;
                                                                                  }
                                    }
                                %>
                            </select>
                            </form>
                        </div>
                    </div>       

                    <div class="control-group">
                        <label class="control-label" for="message">Message:</label>
                        <div class="controls">
                            <textarea cols="200" rows="6" class="input-xlarge focused input_fields_wrap"  id="messaged" name="message" required="true"this.change="countChar(this)"></textarea>
                        </div>
                       <div class="controls">
                            <label  class="control-label" for="message" id="sms"> SMS  <quote id="smsNum"></quote></label><label class="control-label" for="message" id="count"> Characters <quote id="charCount"></quote></label>
                        </div>
                        
                    </div>          

                    <div id="dialog-confirm">

                    </div>
                    <div class="form-actions">


                        <button type="submit" name="sendsms" id="send" value="Send" class="btn btn-primary">Send</button>

                    </div>
                    </div>
                </fieldset>
            </form>
                           
                       

        </div>
                            
                             
    </div><!--/span-->
    
    </div><!--/row-->




<jsp:include page="footer.jsp" />
