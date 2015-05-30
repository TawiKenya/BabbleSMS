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
package ke.co.tawi.babblesms.server.session;


/**
 * Constants which are used in session management of a normal user account.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SessionConstants {

    final public static int SESSION_TIMEOUT = 500;  // Number of seconds for which a session is active.

    final public static String ACCOUNT_SIGN_IN_KEY = "Account Signin Key";

    final public static String ACCOUNT_SIGN_IN_ACCOUNTUUID = "Account Signin AccountUuid";

    // The value associated with the following key is the Unix time in seconds when the user logged in.
    final public static String ACCOUNT_SIGN_IN_TIME = "Account Signin Time";

    final public static String ACCOUNT_SIGN_IN_ERROR_KEY = "Error Login";
    final public static String ACCOUNT_SIGN_IN_NO_EMAIL = "Sorry, there is no user with that email. Please try again.";
    final public static String ACCOUNT_SIGN_IN_WRONG_PASSWORD = "Sorry, the email and password do not match. Please try again.";

    final public static String ACCOUNT_CHANGE_PASSWORD_KEY = "Account change password key";
    //messages associated with passwords;
    final public static String INCORRECT_PASSWORD = "The password you gave is incorrect.";
    final public static String CORRECT_PASSWORD = "Password Changed";
    final public static String MISMATCHED_PASSWORD = "The passwords supplied do not match";

    final public static String ACCOUNT_SEND_SMS_ERROR_KEY = "Account send sms error key";
    final public static String ACCOUNT_SEND_SMS_SUCCESS_KEY = "Account send sms success key";

    final public static String ACCOUNT_SEND_CALLBACK_ERROR_KEY = "Account send callback error key";
    final public static String ACCOUNT_SEND_CALLBACK_SUCCESS_KEY = "Account send callback success key";

    final public static String ACCOUNT_EDIT_CODE_ID = "Account Edit Code Id";

    //A key relating quartz job scheduler error messages
    final public static String QUARTZ_ADD_JOB_KEY = "Error key for adding quartz jobs";
    final public static String QUARTZ_UPDATE_JOB_KEY = "Error key for updating balance notification jobs";

    final public static String SEND_EMAIL_CONTACT_KEY = "Error key in contacts.jsp";
    final public static String SEND_EMAIL_CONTACT_SUCESS = "Email sent successfully";
    final public static String SEND_EMAIL_CONTACT_FAIL = "Invalid Email Address";

    //constants used by email notifications/quartz job scheduler
    final public static String SESSION_EMAIL_KEY = "Session Email";
    final public static String JOB_EXISTS = "Sorry,email notification already set";
    final public static String JOB_NOT_EXISTS = "Sorry, email notification does not exist";
    final public static String GROUP_NAME_BALANCE_THRESHOLD = "Balance Threshold";
    //A key corresponding to the email address of the recipient
    final public static String RECIPIENT_KEY = "RECIPIENT_KEY";
    //group name associated with a job instance
    final public static String GROUP_NAME = "SMS Balance Notification";

    //group name associated with a job instance
    final public static String GROUP_NAME_DEFAULT = "SMS Balance Default Notification";

    final public static String MESSAGE_TO_SEND_KEY = "message to be sent";

    final public static String DELETE_SUCCESSFUL = "Email notification deleted successfully !";
    final public static String ADD_BALANCE_THRESHOLD = "Add Balance Threshold";
    final public static String VALIDATE_EMAIL_ADDRESS = "Validate email address";
    final public static String SMSCODE_UUID_KEY = "Get smscode uuid";
    final public static String BALANCE_THRESHOLD_KEY = "balance threshold";
    final public static String QUARTZ_ADD_THRESHOLD_KEY = "Error key for adding balance threshold jobs";
    final public static String QUARTZ_UPDATE_THRESHOLD_KEY = "Error key for updating balance threshold jobs";
    final public static String CHECK_BALANCE_CRONEXPRESSION = "0 0/30 * * * ? *";

    final public static String UPDATE_NOTIFICATION_SUCCESSFUL = "Email notification updated successfully";
    final public static String IS_EMAIL_SENT_KEY = "Prevent email from being resent";
    
    
    //Add,edit,delete
    final public static String ADD_SUCCESS = "Add successful";
    final public static String ADD_ERROR = "Add  Failed";
    final public static String UPDATE_SUCCESS = "Update successful";
    final public static String UPDATE_ERROR = "Update Failed";
    final public static String DELETE_SUCCESS = "Delete successful";
    final public static String DELETE_ERROR = "Delete Failed";
    final public static String ADD_NAME_EXISTS = "Name Exixts";
    
   
    //sending sms status
    final public static String SENT_SUCCESS = "SMS sent";
    final public static String SENT_ERROR = "SMS not sent";
    
    final public static String CLIENT_EDIT_ACCOUNT_ERROR_KEY = "Client Edit Account Error";
    final public static String CLIENT_EDIT_ACCOUNT_SUCCESS_KEY = "Client Edit Account Success";
    final public static String CLIENT_EDIT_ACCOUNT_PARAMETERS = "Client Edit Account Parameters";
    
    //forgot password
    final public static String EMAIL_SEND_ERROR = "error sening the email";
    final public static String EMAIL_SEND_SUCCESS = "email sent successfully";
    
    
    /**
     * A key holding a value that indicates whether a registered user file has been uploaded
     * successfully or not.
     */
    final public static String ADMIN_UPLOAD_FILE_SUCCESS_KEY = "AdminUploadUserSuccess";
    /**
     * A constant that corresponds to error messages when adding uploading users files in the
     * admin section.
     */
    final public static String ADMIN_UPLOAD_FILE_ERROR_KEY = "AdminUploadFileErrorKey";
    public static String ADMIN_ADD_FORM_PARAMETERS="AdminAddFormParameters";
   
}
