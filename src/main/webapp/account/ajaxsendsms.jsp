
<%@page import="ke.co.tawi.babblesms.server.beans.accountBalance.AccountBalance"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.accountBalance.AccountBalanceDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.ContactDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Contact"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.Phone"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.contact.UserGroup"%>
<%@page import="ke.co.tawi.babblesms.server.beans.credit.Credit"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.credit.CreditDAO"%>
<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="org.apache.commons.lang3.StringUtils"%>
<%@page import="java.util.ArrayList"%>
<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>
<%@page import="net.sf.ehcache.CacheManager"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.contacts.GroupDAO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<%@page import="java.util.List"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.items.contacts.ContactGroupDAO"%>

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
    Cache statusCache = mgr.getCache(CacheVariables.CACHE_STATUS_BY_UUID);
    Cache groupCache = mgr.getCache(CacheVariables.CACHE_GROUP_BY_ACCOUNTUUID);
    Cache networkCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);

    List<Network> networkList = new ArrayList();
    List<Phone> phonelist = new ArrayList();
    Element element;
    Network network;
    List keys, keys1;

    keys = networkCache.getKeys();
    for (Object key : keys) {
        element = networkCache.get(key);
        network = (Network) element.getObjectValue();
        networkList.add(network);
    }
    int cr = 0;
    
    //for credit
    AccountBalanceDAO accbalDAO = AccountBalanceDAO.getInstance();
    if (!request.getParameter("source").isEmpty()) {
       AccountBalance accountBalance = accbalDAO.getClientBalanceBynetwork(accountuuid,request.getParameter("source"), "B936DA83-8A45-E9F0-2EAE-D75F5C232E78");
        if (accountBalance != null) {
            cr = accountBalance.getBalance();
            out.print("Your credit for the selected source is <b>" + cr + "</b>");
        }
    }

    //for recipients
    if (!request.getParameter("destination").isEmpty()) {
        String dest = "";
        ContactsDAO contactsDAO = ContactsDAO.getInstance();
        //remove (group) or (contact)
        int answer = request.getParameter("destination").indexOf("(");
        if (answer >= 1) {
            dest = request.getParameter("destination").substring(0, answer);
        }
        //check whether it is a group
        GroupDAO groupDAO = GroupDAO.getInstance();
        if (groupDAO.getGroupByName(dest) != null) {
            UserGroup userGroup = groupDAO.getGroupByName(dest);
            String groupuuid = userGroup.getUuid();
            //loop contact group to get contacts
            ContactGroupDAO contactGroupDAO = ContactGroupDAO.getInstance();
            List<ContactGroup> list = new ArrayList();

            list = contactGroupDAO.getUserGroup(groupuuid);
            int size = list.size();
            if (cr < size) {
                out.print("<p>Number of recipients is <b>" + size + "</b></p>");
                out.print("<p style='color:red;'>Credit is not enough to send to all recipients.</p>");
            } else {
                out.print("<p>Number of recipients is <b>" + size + "</b></p>");
            }
        } else if (contactsDAO.getContactByName(dest) != null) {
            Contact contact = new Contact();
            contact = contactsDAO.getContactByName(dest);

            String contactuuid = contact.getUuid();
            PhoneDAO phoneDAO = PhoneDAO.getInstance();
            phonelist = phoneDAO.getphonebyContact(contactuuid);
            int size = phonelist.size();
            if (cr < size) {
                out.print("<p>Number of recipients is <b>" + size + "</b></p>");
                out.print("<p style='color:red;'>Credit is not enough to send to all recipients.</p>");
            } else {
                out.print("<p>Number of recipients is <b>" + size + "</b></p>");
            }
        } else {
            if (cr < 1) {
                out.print("<p>Number of recipients is <b>" + 1 + "</b></p>");
                out.print("<p style='color:red;'>Credit is not enough to send to the recipient.</p>");
            } else {
                out.print("<p>Number of recipients is <b>" + 1 + "</b></p>");
            }
        }
    }
%>
