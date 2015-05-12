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
package ke.co.tawi.babblesms.server.servlet.util.file;

import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Utility functions for CSV files
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class CSVUtils {

    private static final String ACTIVE_USER_STATUS_UUID = "396F2C7F-961C-5C12-3ABF-867E7FD029E6";
    private static final String SUSPENDED_USER_STATUS_UUID = "8f556c57-978a-478e-aba4-d4475e01099f";
    private static final String DELETED_USER_STATUS_UUID = "509ff54b-3696-4ad9-8ce3-48bab4364c6b";
    private static final String UNKNOWN_USER_STATUS_UUID = "913b147d-1c27-4370-99e5-b9e73eaf56d6";
    private static final String safaricom = "741AC38C-3E40-6887-9CD6-365EF9EA1EF0";
    private static final String Airtel = "0DE968C9-7309-C481-58F7-AB6CDB1011EF";
    private static final String orange = "5C1D9939-136A-55DE-FD0E-61D8204E17C9";
    private static final String yu = "B936DA83-8A45-E9F0-2EAE-D75F5C232E78";

    private ContactDAO contactsDAO;

    private static final Map<String, String> outletHash = new HashMap<>();//Hash map contains outlet name as key

    private static final Map<String, String> userHash = new HashMap<>();//Hash map contains outlet name as key

    private static final Logger logger = Logger.getLogger(CSVUtils.class);

    /**
     * @param sc
     * @throws ServletException
     *//*
    @Override
    public void init(ServletConfig sc) throws ServletException {
        super.init(sc);
        contactsDAO = ContactDAO.getInstance();
        List<Contact> allUsers = contactsDAO.getAllContacts();
        for (Contact user : allUsers) {
            userHash.put(user.getAccountsuuid(), user.getUuid());
        }

    }*/

    /**
     * Append a uuid at the beginning of each row in a csv file.
     *
     * @param fileLocation -absolute path of the csv file.
     * @param destination
     * @param accountsuuid
     */
    public static void sanitizeCSV(File fileLocation, File destination, String accountsuuid) {
        List<String> contactList = new ArrayList<>();
        String uuid, phoneuuid, emailuuid, contactcontent, phonecontent, emailcontent;
        String name;
        String phone;
        String email;
        String network;
        try {
            File phoneFile = new File(destination + "/phone.csv");
            File emailFile = new File(destination + "/email.csv");
            contactList = FileUtils.readLines(fileLocation);
            FileUtils.write(fileLocation, "", false);//clear file contents, and rewrite it again
            FileUtils.write(phoneFile, "", false);
            FileUtils.write(emailFile, "", false);

            for (String contact : contactList) {
                name = StringUtils.substringBefore(contact, ",");
                phone = StringUtils.substringAfter(contact, ",");
                phone = StringUtils.substringBefore(phone, ",");
                email = StringUtils.substringAfterLast(contact, phone + ",");
                email = StringUtils.substringBefore(email, ",");
                
                network = StringUtils.substringAfterLast(contact, email + ",");
                if (StringUtils.equalsIgnoreCase(network, "safaricom")){
                    network = safaricom;
                }else if (StringUtils.equalsIgnoreCase(network, "airtel")){
                    network = Airtel;
                }
                else if (StringUtils.equalsIgnoreCase(network, "orange")){
                    network = orange;
                }
                else if (StringUtils.equalsIgnoreCase(network, "yu")){
                    network = yu;
                }else{
                    network = "";
                }

                uuid = UUID.randomUUID().toString();
                phoneuuid = UUID.randomUUID().toString();
                emailuuid = UUID.randomUUID().toString();

                contactcontent = uuid + "," + name + "," + accountsuuid + "," + ACTIVE_USER_STATUS_UUID + "\n";
                FileUtils.write(fileLocation, contactcontent, true);

                phonecontent = phoneuuid + "," + phone + "," + uuid + "," + ACTIVE_USER_STATUS_UUID + "," + network + "\n";
                FileUtils.write(phoneFile, phonecontent, true);

                emailcontent = emailuuid + "," + email + "," + uuid + "," + ACTIVE_USER_STATUS_UUID + "\n";
                FileUtils.write(emailFile, emailcontent, true);
            }
        } catch (IOException ex) {
            logger.error(ex);
        }

    }

    /**
     * Replace status names with uuids.
     *
     * @param content .
     */
    private static String replaceStatus(String content) {

        if (StringUtils.containsIgnoreCase(content, "Active")) {
            content = StringUtils.replace(content, "Active", ACTIVE_USER_STATUS_UUID);
        } else if (StringUtils.containsIgnoreCase(content, "Suspended")) {
            content = StringUtils.replace(content, "Suspended", SUSPENDED_USER_STATUS_UUID);
        } else if (StringUtils.containsIgnoreCase(content, "Deleted")) {
            content = StringUtils.replace(content, "Deleted", DELETED_USER_STATUS_UUID);
        } else if (StringUtils.containsIgnoreCase(content, "Unknown")) {
            content = StringUtils.replace(content, "Unknown", UNKNOWN_USER_STATUS_UUID);
        }

        return content;

    }

    /**
     * Replace outlet names with uuids.
     *
     * @param content .
     */
    private static String replaceOutletNames(String content) {
        String outletUuid = "";
        for (String outlet : outletHash.keySet()) {
            outletUuid = (String) outletHash.get(outlet);

            if (StringUtils.containsIgnoreCase(content, outlet)) {
                content = StringUtils.replace(content, outlet, outletUuid);
            }

        }

        return content;

    }
}
