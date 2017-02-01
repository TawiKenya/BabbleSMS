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
package ke.co.tawi.babblesms.server.accountmgmt.admin;


/**
 * Various constants used for HTTP session management.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SessionConstants {

    final public static int SESSION_TIMEOUT = 500;  // Number of seconds for which a session is active.

    final public static String ADMIN_SESSION_KEY = "Admin Session Key";
    final public static String ADMIN_SIGN_IN_ERROR_KEY = "Admin Error Login";
    final public static String ADMIN_SIGN_IN_ERROR_VALUE = "Sorry, the administrator username and/or "
            + "password are incorrect. Please try again.";
    final public static String ADMIN_LOGIN_TIME_KEY = "Admin login time key";

    final public static String ADMIN_ADD_ACCOUNT_ERROR_KEY = "Admin Add Account Error";
    final public static String ADMIN_ADD_ACCOUNT_SUCCESS_KEY = "Admin Add Account Success";
    final public static String ADMIN_ADD_ACCOUNT_PARAMETERS = "Admin Add Account Parameters";

    final public static String ADMIN_ADD_CREDIT_ERROR_KEY = "Admin Add Credit Error";
    final public static String ADMIN_ADD_CREDIT_SUCCESS_KEY = "Admin Add Credit Success";
    final public static String ADMIN_ADD_CREDIT_PARAMETERS = "Admin Add Credit Parameters";

    final public static String ADMIN_ADD_SMSCODE_ERROR_KEY = "Admin Add SMSCode Error";
    final public static String ADMIN_ADD_SMSCODE_SUCCESS_KEY = "Admin Add SMSCode Success";
    final public static String ADMIN_ADD_SMSCODE_PARAMETERS = "Admin Add SMSCode Parameters";

    final public static String ADMIN_ADD_SMSMASK_ERROR_KEY = "Admin Add SMSMask Error";
    final public static String ADMIN_ADD_SMSMASK_SUCCESS_KEY = "Admin Add SMSMask Success";
    final public static String ADMIN_ADD_SMSMASK_PARAMETERS = "Admin Add SMSMask Parameters";

    final public static String ADMIN_ADD_SMSSHORTCODE_ERROR_KEY = "Admin Add SMSShortcode Error";
    final public static String ADMIN_ADD_SMSSHORTCODE_SUCCESS_KEY = "Admin Add SMSShortcode Success";
    final public static String ADMIN_ADD_SMSSHORTCODE_PARAMETERS = "Admin Add SMSShortcode Parameters";

    final public static String ADMIN_ADD_NETWORK_ERROR_KEY = "Admin Add network Error";
    final public static String ADMIN_ADD_NETWORK_SUCCESS_KEY = "Admin Add network Success";
    final public static String ADMIN_ADD_NETWORK_PARAMETERS = "Admin Add network Parameters";

    final public static String ADMIN_ADD_MASTER_CREDIT_ERROR_KEY = "Admin Add Master Credit Error";
    final public static String ADMIN_ADD_MASTER_CREDIT_SUCCESS_KEY = "Admin Add Master Credit Success";
    final public static String ADMIN_ADD_MASTER_CREDIT_PARAMETERS = "Admin Add Master Credit Parameters";

    final public static String ADMIN_ADD_CLIENT_CREDIT_ERROR_KEY = "Admin Add Client Credit Error";
    final public static String ADMIN_ADD_CLIENT_CREDIT_SUCCESS_KEY = "Admin Add Client Credit Success";
    final public static String ADMIN_ADD_CLIENT_CREDIT_PARAMETERS = "Admin Add Client Credit Parameters";

    final public static String ADMIN_CHECK_SHORTCODE_KEY = "Admin verify short code belongs to account";
    final public static String ADMIN_CHECK_SMSMASK_KEY = "Admin verify sms mask belongs to account";
    
    final public static String ADMIN_ADD_NOTIFICATION_ERROR_KEY = "Admin Add Notification Error";
    final public static String ADMIN_ADD_NOTIFICATION_SUCCESS_KEY = "Admin Add Notification Success";
    final public static String ADMIN_ADD_NOTIFICATION_PARAMETERS = "Admin Add Notification Parameters";

    //Add,edit,delete
    final public static String ADMIN_ADD_SUCCESS = "Add successful";
    final public static String ADMIN_ADD_ERROR = "Add  Failed";
    final public static String ADMIN_UPDATE_SUCCESS = "Update successful";
    final public static String ADMIN_UPDATE_ERROR = "Update Failed";
    final public static String ADMIN_DELETE_SUCCESS = "Delete successful";
    final public static String ADMIN_DELETE_ERROR = "Delete Failed";

    //Constants relating to quartz job scheduler
    final public static String ADMIN_QUARTZ_ADD_JOB_KEY = "Admin add quartz job";
    //A key corresponding to the email address of the recipient
    final public static String ADMIN_RECIPIENT_KEY = "Admin recpient key holding recipient address";
    final public static String ADMIN_MESSAGE_TO_SEND_KEY = "Admin message to be sent";
    final public static String ADMIN_JOB_EXISTS = "Sorry,email notification already set";
    final public static String JOB_DELETE_SUCCESSFUL = "delete successful!";
    //group name associated with a job instance
    final public static String GROUP_NAME_DEFAULT = "SMS Balance Default Notification";
    //trigger quartz job 15th of every month at 9 a.m
    final public static String ADMIN_CRON_EXPRESSION = "0 00 09 15 * ? *";

}
