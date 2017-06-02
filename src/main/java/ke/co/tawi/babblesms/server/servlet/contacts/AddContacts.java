/**
 * Copyright 2015 Tawi Commercial Services Ltd
 * 
 * Licensed under the Open Software License, Version 3.0 (the “License”); you may
 * not use this file except in compliance with the License. You may obtain a copy
 * of the License at:
 * http://opensource.org/licenses/OSL-3.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an “AS IS” BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 * 
 * See the License for the specific language governing permissions and limitations
 * under the License.
 */
package ke.co.tawi.babblesms.server.servlet.contacts;

import ke.co.tawi.babblesms.server.beans.contact.Contact;
//import ke.co.tawi.babblesms.server.beans.contact.ContactGroup;
import ke.co.tawi.babblesms.server.beans.contact.Email;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.EmailDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;
import ke.co.tawi.babblesms.server.session.SessionConstants;

import java.io.IOException;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.validator.routines.EmailValidator;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Receives form values from addcontact.jsp section and adds a new
 * {@link AddContacts} to the database.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
@WebServlet(name = "AddContacts",
        urlPatterns = {
            "/account/addcontact", "/account/editEmail", "/account/deleteemail", "/account/deletecontact"})
public class AddContacts extends HttpServlet {
	private Logger logger = LogManager.getLogger(this.getClass());
	
    private final CacheManager mgr = CacheManager.getInstance();
    private final EmailDAO emailDAO = EmailDAO.getInstance();
    private final PhoneDAO phoneDAO = PhoneDAO.getInstance();
    private final ContactDAO ctDAO = ContactDAO.getInstance();
    private final ContactGroupDAO cgDAO = ContactGroupDAO.getInstance();
    private final EmailValidator emailValidator = EmailValidator.getInstance();
    private final String ERROR_NO_NAME = "You have to input a value.";
    private final String ERROR_NAME_EXISTS = "The username provided already exists in the system.";
    private final String ERROR_EMAIL_EXISTS = "The email provided already exists in the system.";
    private final String ERROR_PHONE_EXISTS = "The phonenumber provided already exists in the system.";
    private final String ADD_SUCCESS = "created successfully.";
    private final String ERROR_INVALID_EMAIL = "Please provide a valid email address.";
    private final String ERROR_DUPLICATE_EMAIL = "You have supplied duplicate email addresses.";
    private final String ERROR_PUBLICATE_PHONE = "You have supplied duplicate phone numbers.";
    
    Contact ct;
    Email mail;
    Phone phn;

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String userPath = request.getServletPath();
        HttpSession session = request.getSession(false);

        // if add contacts is called
        if (userPath.equals("/account/addcontact")) {

            String[] emailArray = request.getParameterValues("email[]");
            String[] phonenumArray = request.getParameterValues("phonenum[]");
            String[] networkArray = request.getParameterValues("network[]");
            String contactname = request.getParameter("contname");
            String groupid = request.getParameter("groupid");
            String statusuuid = request.getParameter("statusuuid");
            String accountuuid = request.getParameter("accountuuid");
            

            Set<String> mySet = new HashSet<String>(Arrays.asList(emailArray));
            Set<String> mySet2 = new HashSet<String>(Arrays.asList(phonenumArray));
            int duplicateemail = emailArray.length - mySet.size();
            int duplicatephone = phonenumArray.length - mySet2.size();

            // No First Name provided
            if ((StringUtils.isBlank(contactname)) || (emailArray.length == 0)
                    || (phonenumArray.length == 0) || (networkArray.length == 0)) {
                session.setAttribute(SessionConstants.ADD_ERROR, ERROR_NO_NAME);
            } else if (!validemails(emailArray)) {
                session.setAttribute(SessionConstants.ADD_ERROR, ERROR_INVALID_EMAIL);
            } else if (existsEmail(emailArray)) {
                session.setAttribute(SessionConstants.ADD_ERROR, ERROR_EMAIL_EXISTS);

            } else if (duplicateemail >= 1) {
                session.setAttribute(SessionConstants.ADD_ERROR, ERROR_DUPLICATE_EMAIL);

            } else if (duplicatephone >= 1) {
                session.setAttribute(SessionConstants.ADD_ERROR, ERROR_PUBLICATE_PHONE);
            } /*else if (existsPhone(phonenumArray)) {
             session.setAttribute(SessionConstants.ADD_ERROR, ERROR_PHONE_EXISTS);
             }*/ else {
                session.setAttribute(SessionConstants.ADD_SUCCESS, ADD_SUCCESS);
             
                ct = new Contact();
                ct.setName(contactname);
                ct.setStatusUuid(statusuuid);
                ct.setAccountUuid(accountuuid);
                
                if(ctDAO.putContact(ct)){
                }
                else{
                  session.setAttribute(SessionConstants.ADD_ERROR, "Contact  creation Failed.");  
                }
                //get contact bean to update cache
                String uuid = ct.getUuid();
                //ct = ctDAO.getContactByName(contactname);
                ct = ctDAO.getContact(uuid);
                //logger.info(ct + " added sucessfully");
               // mgr.getCache(CacheVariables.CACHE_CONTACTS_BY_UUID).put(new Element(ct.getUuid(), ct));

                //loop emails
                for (String email2 : emailArray) {
                    mail = new Email();
                    mail.setAddress(email2);
                    mail.setContactuuid(ct.getUuid());
                    //mail.setStatusuuid(statusuuid);

                    emailDAO.putEmail(mail);
                    //get uuid to update cache
                    mail = emailDAO.getEmail(email2);

                    //logger.info(mail + " added sucessfully");
                    mgr.getCache(CacheVariables.CACHE_EMAIL_BY_UUID).put(new Element(mail.getUuid(), mail));
                }

                //loop phonenumbers               
                int count = 0;
                for (String phonenum : phonenumArray) {
                    phn = new Phone();
                    phn.setPhonenumber(phonenum);
                    phn.setUuid(ct.getUuid());
                    phn.setNetworkuuid(networkArray[count]);
                    //phn.setStatusuuid(statusuuid);

                    phoneDAO.putPhone(phn);
                    //updatecache with correct uuid
                    phn = phoneDAO.getPhone(phonenum);
                    //logger.info(phn + " added sucessfully");
                    mgr.getCache(CacheVariables.CACHE_PHONE_BY_UUID).put(new Element(phn.getUuid(), phn));
                    count++;
                }

               // ContactGroup cg = new ContactGroup();
               // cg.setAccountsuuid(accountuuid);
               // cg.setCgroupuuid(groupid);
                //cg.setContactuuid(ct.getUuid());

                //cgDAO.putContactGroup(cg);
                //logger.info(cg + " added sucessfully");
                //mgr.getCache(CacheVariables.CACHE_CONTACT_GROUP_BY_UUID).put(new Element(cg.getUuid(), cg));
                //response.sendRedirect("account/contact.jsp");

            }
            response.sendRedirect("addcontact.jsp");
            //userPath = "addcontact";

        } else if (userPath.equals("/account/editEmail")) {
            String email = request.getParameter("email");
            String emailuuid = request.getParameter("emailuuid");
            String contactname = request.getParameter("contname");
            String contactuuid = request.getParameter("contactuuid");

            //ctDAO.updateContact(contactuuid, contactname);
            /*if (emailDAO.updateEmail(emailuuid, email)) {
                
                session.setAttribute(SessionConstants.UPDATE_SUCCESS, "Update was successful.");
            } else {
                
                session.setAttribute(SessionConstants.UPDATE_ERROR, "Update Failed.");
            }*/
            response.sendRedirect("contactemail.jsp");
            //userPath = "contactemail";

        } else if (userPath.equals("/account/editcontact")) {

            String phone = request.getParameter("phonenum");
            String phoneuuid = request.getParameter("phoneuuid");
            String contactname = request.getParameter("contname");
            String contactuuid = request.getParameter("contactuuid");

            /*
            ctDAO.updateContact(contactuuid, contactname);
            emailDAO.updateEmail(emailuuid, email);
            if (phoneDAO.updatePhone(phoneuuid, phone)) {
                
                session.setAttribute(SessionConstants.UPDATE_SUCCESS, "Update successful.");
                
            } else {
                session.setAttribute(SessionConstants.UPDATE_ERROR, "Update failed.");
            }*/
            response.sendRedirect("contact.jsp");
            //userPath = "contact";

        } else if (userPath.equals("/account/deletecontact")) {
            String contactuuid = request.getParameter("contactuuid");
            //String emailuuid = request.getParameter("emailuuid");
            //String phone = request.getParameter("phonenum");
            String phoneuuid = request.getParameter("phoneuuid");

             /*
             if(phoneDAO.deletePhone(phoneuuid)){
                 ctDAO.deleteContact(contactuuid);
               session.setAttribute(SessionConstants.DELETE_SUCCESS, "Deletion successful.");  
             }
             else{
               session.setAttribute(SessionConstants.DELETE_ERROR, "Deletion failed.");   
             }*/
            
            response.sendRedirect("contact.jsp");
            //userPath = "contact";

        } else if (userPath.equals("/account/deleteemail")) {
            String contactuuid = request.getParameter("contactuuid");
            String emailuuid = request.getParameter("emailuuid");
            
            /* if(emailDAO.deleteEmail(emailuuid)){
              //   ctDAO.deleteContact(contactuuid);
               session.setAttribute(SessionConstants.DELETE_SUCCESS, "Deletion successful.");  
             }
             else{
               session.setAttribute(SessionConstants.DELETE_ERROR, "Deletion failed.");   
             }*/

            response.sendRedirect("contactemail.jsp");
            //userPath = "contactemail";
        }
        
        

        // use RequestDispatcher to forward request internally
       /* String url = userPath + ".jsp";

        try {
            request.getRequestDispatcher(url).forward(request, response);
        } catch (ServletException | IOException ex) {
        }
      */
    }

    /**
     *
     * @param request
     * @param response
     * @throws ServletException
     * @throws IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);

    }

    /**
     * Checks if the email supplied already exists in email database
     *
     * @param email
     * @return
     */
    private boolean existsEmail(final String[] emailArray) {
        boolean exists = false;

        for (String email : emailArray) {
            if (emailDAO.getEmail(email) != null) {
                exists = true;
            }
        }

        return exists;
    }

    /**
     * Checks if the phone supplied already exists in phone database
     *
     * @param phone
     * @return
     */
    private boolean existsPhone(final String[] phoneArray, String groupuuid) {
        boolean exists = false;
        for (String phone : phoneArray) {
            //ContactGroup contactGroup=new ContactGroup();
            //contactGroup=cgDAO.getUserGroup(groupuuid);
            if (phoneDAO.getPhone(phone) != null) {
                exists = true;
            }
        }
        return exists;
    }

    /**
     * Checks if the phone supplied already exists in phone database
     *
     * @param phone
     * @return
     */
    private boolean validemails(final String[] emailArray) {
        boolean valid = true;

        for (String email : emailArray) {
            if (!emailValidator.isValid(email)) {
                valid = false;
            }
        }

        return valid;
    }
    
}
            
