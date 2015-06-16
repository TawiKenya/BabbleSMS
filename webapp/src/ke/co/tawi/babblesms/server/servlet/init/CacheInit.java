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
package ke.co.tawi.babblesms.server.servlet.init;

import ke.co.tawi.babblesms.server.beans.StorableBean;
import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.beans.network.Country;
import ke.co.tawi.babblesms.server.cache.CacheVariables;

import ke.co.tawi.babblesms.server.persistence.accounts.AccountDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
import ke.co.tawi.babblesms.server.persistence.maskcode.MaskDAO;
import ke.co.tawi.babblesms.server.persistence.maskcode.ShortcodeDAO;
import ke.co.tawi.babblesms.server.persistence.template.MessageTemplateDAO;
import ke.co.tawi.babblesms.server.persistence.network.CountryDAO;
import ke.co.tawi.babblesms.server.persistence.network.NetworkDAO;
import ke.co.tawi.babblesms.server.persistence.status.MessageStatusDAO;
import ke.co.tawi.babblesms.server.persistence.status.StatusDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactDAO;

import ke.co.tawi.babblesms.server.servlet.util.PropertiesConfig;

import java.io.File;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;
import net.sf.ehcache.Status;
import net.sf.ehcache.config.CacheConfiguration;
import net.sf.ehcache.config.Configuration;
import net.sf.ehcache.config.DiskStoreConfiguration;
import net.sf.ehcache.config.SizeOfPolicyConfiguration;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

/**
 * Brings certain frequently accessed variables into cache.
 * <br />
 * Remember that Ehcache objects have to be serializable to allow for off-disk
 * storage.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class CacheInit extends HttpServlet {

    protected AccountDAO accountDAO;
    protected StatusDAO statusDAO;
    protected NetworkDAO networkDAO;
    protected GroupDAO groupDAO;
    protected ContactGroupDAO cgDAO;
    protected ShortcodeDAO shortcodeDAO;
    protected MaskDAO maskDAO;
    protected MessageStatusDAO msgDAO;
    protected MessageTemplateDAO msgtDAO;
    protected CountryDAO countryDAO;
    protected ContactDAO contactDAO;
    
    private CacheManager cacheManager;
    
    private SizeOfPolicyConfiguration sizeOfPolicyConfiguration;

    private Logger logger = Logger.getLogger(this.getClass());
    
    /**
     *
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        accountDAO = AccountDAO.getInstance();
        statusDAO = StatusDAO.getInstance();
        networkDAO = NetworkDAO.getInstance();
        groupDAO = GroupDAO.getInstance();
        cgDAO = ContactGroupDAO.getInstance();
        shortcodeDAO = ShortcodeDAO.getInstance();
        maskDAO = MaskDAO.getInstance();
        msgDAO = MessageStatusDAO.getInstance();
        msgtDAO = MessageTemplateDAO.getInstance();
        countryDAO = CountryDAO.getInstance();
        contactDAO = ContactDAO.getInstance();
        
        sizeOfPolicyConfiguration = new SizeOfPolicyConfiguration();
        sizeOfPolicyConfiguration.setMaxDepthExceededBehavior("abort");

        logger.info("Starting to initialize cache");
        initCache();
        logger.info("Have finished initializing cache");
    }
    

    /**
     *
     */
    protected void initCache() {
        DiskStoreConfiguration diskConfig = new DiskStoreConfiguration();
        diskConfig.setPath(System.getProperty("java.io.tmpdir") + File.separator +
        		"ehcache" + File.separator + PropertiesConfig.getConfigValue("CACHE_FILE"));        
        
        Configuration config = (new Configuration()).diskStore(diskConfig);
        config.setMaxBytesLocalHeap(Long.parseLong(PropertiesConfig.getConfigValue("MAX_BYTES_LOCAL_HEAP")));
        config.setMaxBytesLocalDisk(Long.parseLong(PropertiesConfig.getConfigValue("MAX_BYTES_LOCAL_DISK")));
        config.setUpdateCheck(false);

        cacheManager = CacheManager.create(config);

        List<? extends StorableBean> objList;

        objList = accountDAO.getAllAccounts();
        initCacheByUuid(CacheVariables.CACHE_ACCOUNTS_BY_UUID, objList);
        
        List<? extends StorableBean> contactList;
        for(Object obj : objList) {
        	contactList = contactDAO.getContacts((Account)obj);
        	initCacheByUuid(CacheVariables.CACHE_CONTACTS_BY_UUID, contactList);
        }
        

        objList = statusDAO.getAllStatus();
        initCacheByUuid(CacheVariables.CACHE_STATUS_BY_UUID, objList);

        objList = networkDAO.getAllNetworks();
        initCacheByUuid(CacheVariables.CACHE_NETWORK_BY_UUID, objList);

        objList = msgDAO.getAllMessageStatus();
        initCacheByUuid(CacheVariables.CACHE_MESSAGE_STATUS_BY_UUID, objList);
                
        objList = shortcodeDAO.getAllShortcodes();
        initCacheByUuid(CacheVariables.CACHE_SHORTCODE_BY_UUID, objList);
        
        objList = maskDAO.getAllMasks();
        initCacheByUuid(CacheVariables.CACHE_MASK_BY_UUID, objList);
        
        objList = groupDAO.getAllGroups();
        initCacheByUuid(CacheVariables.CACHE_GROUP_BY_UUID, objList);
        
        objList = countryDAO.getAllCountries();
        initCacheByUuid(CacheVariables.CACHE_COUNTRY_BY_UUID, objList);

        initAccountsCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
                
        initContactsCache(CacheVariables.CACHE_CONTACTS_BY_ACCOUNTUUID);
                        
        initShortcodeCache(CacheVariables.CACHE_SHORTCODE_BY_ACCOUNTUUID);
        
        initMaskCache(CacheVariables.CACHE_MASK_BY_ACCOUNTUUID);
        
        initMessageTemplateCache(CacheVariables.CACHE_MESSAGE_TEMPLATE_BY_ACCOUNTUUID);
        
        initCountryCache(CacheVariables.CACHE_COUNTRY_BY_UUID);

        initGenericCache(CacheVariables.CACHE_STATISTICS_BY_ACCOUNT);
        initGenericCache(CacheVariables.CACHE_ALL_ACCOUNTS_STATISTICS);        
    }
    

    /**
     *
     * @param cacheName
     * @param objList
     */
    private void initCacheByUuid(String cacheName, List<? extends StorableBean> objList) {
    	Cache cache = null;
        if (!cacheManager.cacheExists(cacheName)) {
            CacheConfiguration cacheConfig = new CacheConfiguration().sizeOfPolicy(sizeOfPolicyConfiguration);
            cacheConfig.setCopyOnRead(false); // Whether the Cache should copy elements it returns
            cacheConfig.setCopyOnWrite(false); // Whether the Cache should copy elements it gets
            cacheConfig.setEternal(true); // Sets whether elements are eternal.        
            cacheConfig.setName(cacheName); // Sets the name of the cache.

            cache = new Cache(cacheConfig);
            cacheManager.addCacheIfAbsent(cache);
            if (cache.getStatus() == Status.STATUS_UNINITIALISED) {
                cache.initialise();
            }
            
        } else {
        	CacheManager mgr = CacheManager.getInstance();
        	cache = mgr.getCache(cacheName);
        }
     
        for (StorableBean b : objList) {
            cache.put(new Element(b.getUuid(), b)); // UUID as the key            
        }
    }    
    

    /**
     *
     * @param cacheName
     */
    private void initAccountsCache(String cacheName) {

        if (!cacheManager.cacheExists(cacheName)) {
            CacheConfiguration cacheConfig = new CacheConfiguration().sizeOfPolicy(sizeOfPolicyConfiguration);
            cacheConfig.setCopyOnRead(false); // Whether the Cache should copy elements it returns
            cacheConfig.setCopyOnWrite(false); // Whether the Cache should copy elements it gets
            cacheConfig.setEternal(true); // Sets whether elements are eternal.    	
            cacheConfig.setName(cacheName); // Sets the name of the cache.

            Cache accountsCache = new Cache(cacheConfig);
            cacheManager.addCacheIfAbsent(accountsCache);
            if (accountsCache.getStatus() == Status.STATUS_UNINITIALISED) {
                accountsCache.initialise();
            }

            List<Account> allAccounts = accountDAO.getAllAccounts();

            if (StringUtils.equals(cacheName, CacheVariables.CACHE_ACCOUNTS_BY_USERNAME)) {
                for (Account a : allAccounts) {
                    accountsCache.put(new Element(a.getUsername(), a));		// Username as the key
                }
            }
        }
    }

    
    /**
     *
     * @param cacheName
     */
    private void initContactsCache(String cacheName) {

        if (!cacheManager.cacheExists(cacheName)) {
            CacheConfiguration cacheConfig = new CacheConfiguration().sizeOfPolicy(sizeOfPolicyConfiguration);
            cacheConfig.setCopyOnRead(false); // Whether the Cache should copy elements it returns
            cacheConfig.setCopyOnWrite(false); // Whether the Cache should copy elements it gets
            cacheConfig.setEternal(true); // Sets whether elements are eternal.    	
            cacheConfig.setName(cacheName); // Sets the name of the cache.

            Cache contactsCache = new Cache(cacheConfig);
            cacheManager.addCacheIfAbsent(contactsCache);
            if (contactsCache.getStatus() == Status.STATUS_UNINITIALISED) {
                contactsCache.initialise();
            }

            /*List<Contact> allContacts = contactsDAO.getAllContacts();

            if (StringUtils.equals(cacheName, CacheVariables.CACHE_CONTACTS_BY_ACCOUNTUUID)) {
                for (Contact a : allContacts) {
                    contactsCache.put(new Element(a.getAccountsuuid(), a)); 	// Email as the key
                }

            }*/
        }
    }


    /**
     *
     * @param cacheName
     */
    private void initShortcodeCache(String cacheName) {

        if (!cacheManager.cacheExists(cacheName)) {
            CacheConfiguration cacheConfig = new CacheConfiguration().sizeOfPolicy(sizeOfPolicyConfiguration);
            cacheConfig.setCopyOnRead(false); // Whether the Cache should copy elements it returns
            cacheConfig.setCopyOnWrite(false); // Whether the Cache should copy elements it gets
            cacheConfig.setEternal(true); // Sets whether elements are eternal.    	
            cacheConfig.setName(cacheName); // Sets the name of the cache.

            Cache shortcodeCache = new Cache(cacheConfig);
            cacheManager.addCacheIfAbsent(shortcodeCache);
            if (shortcodeCache.getStatus() == Status.STATUS_UNINITIALISED) {
                shortcodeCache.initialise();
            }

            List<Shortcode> allShortcodes = shortcodeDAO.getAllShortcodes();

            if (StringUtils.equals(cacheName, CacheVariables.CACHE_SHORTCODE_BY_ACCOUNTUUID)) {
                for (Shortcode a : allShortcodes) {
                    shortcodeCache.put(new Element(a.getAccountuuid(), a)); 	// Email as the key
                }

            }
        }
    }

    
    /**
     *
     * @param cacheName
     */
    private void initMaskCache(String cacheName) {

        if (!cacheManager.cacheExists(cacheName)) {
            CacheConfiguration cacheConfig = new CacheConfiguration().sizeOfPolicy(sizeOfPolicyConfiguration);
            cacheConfig.setCopyOnRead(false); // Whether the Cache should copy elements it returns
            cacheConfig.setCopyOnWrite(false); // Whether the Cache should copy elements it gets
            cacheConfig.setEternal(true); // Sets whether elements are eternal.    	
            cacheConfig.setName(cacheName); // Sets the name of the cache.

            Cache maskCache = new Cache(cacheConfig);
            cacheManager.addCacheIfAbsent(maskCache);
            if (maskCache.getStatus() == Status.STATUS_UNINITIALISED) {
                maskCache.initialise();
            }

            List<Mask> allMasks = maskDAO.getAllMasks();

            if (StringUtils.equals(cacheName, CacheVariables.CACHE_MASK_BY_ACCOUNTUUID)) {
                for (Mask a : allMasks) {
                    maskCache.put(new Element(a.getAccountuuid(), a)); 	// Email as the key
                }

            }
        }
    }
    

    /**
     *
     * @param cacheName
     */
    private void initMessageTemplateCache(String cacheName) {

        if (!cacheManager.cacheExists(cacheName)) {
            CacheConfiguration cacheConfig = new CacheConfiguration().sizeOfPolicy(sizeOfPolicyConfiguration);
            cacheConfig.setCopyOnRead(false); // Whether the Cache should copy elements it returns
            cacheConfig.setCopyOnWrite(false); // Whether the Cache should copy elements it gets
            cacheConfig.setEternal(true); // Sets whether elements are eternal.    	
            cacheConfig.setName(cacheName); // Sets the name of the cache.
            
            Cache msgTemplateCache = new Cache(cacheConfig);
            cacheManager.addCacheIfAbsent(msgTemplateCache);
            if (msgTemplateCache.getStatus() == Status.STATUS_UNINITIALISED) {
                msgTemplateCache.initialise();
            }
            
        }
    }
    
    
    /**
     *
     * @param cacheName
     */
    private void initCountryCache(String cacheName) {

        if (!cacheManager.cacheExists(cacheName)) {
            CacheConfiguration cacheConfig = new CacheConfiguration().sizeOfPolicy(sizeOfPolicyConfiguration);
            cacheConfig.setCopyOnRead(false); // Whether the Cache should copy elements it returns
            cacheConfig.setCopyOnWrite(false); // Whether the Cache should copy elements it gets
            cacheConfig.setEternal(true); // Sets whether elements are eternal.    	
            cacheConfig.setName(cacheName); // Sets the name of the cache.

            Cache countryCache = new Cache(cacheConfig);
            cacheManager.addCacheIfAbsent(countryCache);
            if (countryCache.getStatus() == Status.STATUS_UNINITIALISED) {
                countryCache.initialise();
            }

            List<Country> allcountries = countryDAO.getAllCountries();

            if (StringUtils.equals(cacheName, CacheVariables.CACHE_COUNTRY_BY_UUID)) {
                for (Country a : allcountries) {
                    countryCache.put(new Element(a.getUuid(), a)); 	// Email as the key
                }

            }
        }
    }
  
    
    /**
     *
     * @param cacheName
     */
    private void initGenericCache(String cacheName) {
        if (!cacheManager.cacheExists(cacheName)) {
            CacheConfiguration cacheConfig = new CacheConfiguration().sizeOfPolicy(sizeOfPolicyConfiguration);
            cacheConfig.setCopyOnRead(false); // Whether the Cache should copy elements it returns
            cacheConfig.setCopyOnWrite(false); // Whether the Cache should copy elements it gets
            cacheConfig.setEternal(true); // Sets whether elements are eternal.    	
            cacheConfig.setName(cacheName); // Sets the name of the cache.

            Cache cache = new Cache(cacheConfig);
            cacheManager.addCacheIfAbsent(cache);
            if (cache.getStatus() == Status.STATUS_UNINITIALISED) {
                cache.initialise();
            }
        }
    }

    
    /**
     * @see javax.servlet.GenericServlet#destroy()
     */
    @Override
    public void destroy() {
        super.destroy();

        CacheManager.getInstance().shutdown();
    }
}
