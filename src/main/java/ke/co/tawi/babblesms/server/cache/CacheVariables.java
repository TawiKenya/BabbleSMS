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
package ke.co.tawi.babblesms.server.cache;

/**
 * Various global settings to be used in manipulating the cache. We are using
 * <a href="http://www.ehcache.org">Ehcache</a> as our library.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class CacheVariables {

    // Names of caches
    // These are caches of Accounts in the system
    // The following caches have an email as the key for the object
    public static String CACHE_STATISTICS_BY_ACCOUNT = "StatisticsEmail";
    public final static String CACHE_ACCOUNTS_BY_USERNAME = "AccountsUsername";

    // The following caches have an UUID as the key for the object
    public final static String CACHE_ACCOUNTS_BY_UUID = "AccountsUuid";
    public final static String CACHE_STATUS_BY_UUID = "StatusUuid";
    public final static String CACHE_NETWORK_BY_UUID = "NetworkUuid";
    public final static String CACHE_MESSAGE_STATUS_BY_UUID = "MessageStatusUuid";
    public final static String CACHE_CONTACTS_BY_UUID = "ContactUuid";
    public final static String CACHE_PHONE_BY_UUID = "PhoneUuid";
    public final static String CACHE_EMAIL_BY_UUID = "EmailUuid";
    public final static String CACHE_GROUP_BY_UUID = "GroupUuid";
    public final static String CACHE_CONTACT_GROUP_BY_UUID = "ContactGroupUuid";
    public final static String CACHE_SHORTCODE_BY_UUID = "ShortcodeUuid";  
    public final static String CACHE_MASK_BY_UUID = "MaskUuid";          
    public final static String CACHE_MESSAGE_TEMPLATE_BY_UUID = "MessageTemplateUuid";
    public final static String CACHE_COUNTRY_BY_UUID = "CountryUuid";


    public final static String CACHE_SHORTCODE_BALANCE_BY_UUID = "ShortcodeUuid";          
    public final static String CACHE_MASK_BALANCE_BY_UUID = "MaskUuid";
    




    public final static String CACHE_CONTACTS_BY_ACCOUNTUUID = "ContactsUser";
    public final static String CACHE_PHONE_BY_CONTACTS_UUID = "PhoneByUuid";
    public final static String CACHE_EMAIL_BY_CONTACTS_UUID = "EmailByUuid";
    public final static String CACHE_SHORTCODE_BY_ACCOUNTUUID = "ShortcodeByUuid"; //
    public final static String CACHE_MASK_BY_ACCOUNTUUID = "MaskByUuid";
    public final static String CACHE_MESSAGE_TEMPLATE_BY_ACCOUNTUUID = "MessageTemplateByUuid";

    //Cache variables to be used in the admin section
    public static String CACHE_ALL_ACCOUNTS_STATISTICS_KEY = "CacheAllAccountsStatisticsKey";
    public static String CACHE_ALL_ACCOUNTS_STATISTICS = "CacheAllAccountsStatisticsKey";
}

