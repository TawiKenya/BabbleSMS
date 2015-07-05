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
package ke.co.tawi.babblesms.server.servlet.ajax.client;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.contact.Contact;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.beans.contact.Phone;
import ke.co.tawi.babblesms.server.beans.network.Network;

import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.ContactGroupDAO;
import ke.co.tawi.babblesms.server.persistence.contacts.PhoneDAO;

import ke.co.tawi.babblesms.server.cache.CacheVariables;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.HashMap;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
 * Returns a JSON list of Groups for a given account.
 * <p>
 * Sample output is as follows:<br/>
 *{  "Teachers": {"Orange KE": 2, "Yu KE": 0, "Safaricom KE": 1, "Airtel KE": 2, "Total contacts": 5 },
 *   "Students": {"Orange KE": 31, "Yu KE": 0, "Safaricom KE": 30, "Airtel KE": 17, "Total contacts": 78 },
 *   "Parents": {"Orange KE": 1, "Yu KE": 0, "Safaricom KE": 2, "Airtel KE": 2, "Total contacts": 5 },
 *   "Staff": {"Orange KE": 11,  "Yu KE": 0,  "Safaricom KE": 7,  "Airtel KE": 13,  "Total contacts": 31 },
 *   "Management": {"Orange KE": 9,  "Yu KE": 0,  "Safaricom KE": 9,  "Airtel KE": 13,  "Total contacts": 31 }
 * } 
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * @author <a href="mailto:eugene.g99@gmail.com">Eugene Wang'ombe</a>
 * @author <a href="mailto:migwi@tawi.mobi">Migwi Ndung'u</a>
 * 
 */
public class GetGroups extends HttpServlet {
	

	private Cache accountsCache;

	private GroupDAO groupDAO;
	private ContactGroupDAO ctGrpDAO;
	private PhoneDAO phoneDAO;
	
	// In the follwing map, the key is the uuid of the network 
	private Map<String,Network> networkHash;
	
	
	/**
	 *
	 * @param config
	 * @throws ServletException
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		CacheManager mgr = CacheManager.getInstance();
		accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID);
		
		Cache networkCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
		networkHash = new HashMap<>();
		
		Element element;
		Network network;
		
		List keys = networkCache.getKeys();
		for (Object key : keys) {
	        element = networkCache.get(key);
	        network = (Network) element.getObjectValue();
	        networkHash.put(network.getUuid(), network);
	    }
	       
		groupDAO = GroupDAO.getInstance();
		ctGrpDAO = ContactGroupDAO.getInstance();
		phoneDAO = PhoneDAO.getInstance();		
	}
	

	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest,
	 *      javax.servlet.http.HttpServletResponse)
	 */
	@Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response) 
			throws ServletException, IOException {
		Account account = new Account();

		String accountUuid = request.getParameter("accountuuid");

		Element element;
		if ((element = accountsCache.get(accountUuid)) != null) {
			account = (Account) element.getObjectValue();
		}

		OutputStream out = response.getOutputStream();

		response.setContentType("application/json;charset=UTF-8");

		// Instantiate the JSon
		// The '=' sign is encoded to \u003d. Hence you need to use
		// disableHtmlEscaping().
		Gson gson = new GsonBuilder().disableHtmlEscaping()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.setPrettyPrinting().serializeNulls().create();
		
		out.write(gson.toJson(getGroupMaps(account)).getBytes());
		out.flush();
		out.close();
	}

	
	/**
	 * @param account
	 * @return a HashMaps containing a list of other HashMaps, each HashMap with
	 *         the relevant Group information.
	 */
	private Map<String, Map<String,Integer>> getGroupMaps(Account account) {
		
		Map<String,Integer> groupHash = new HashMap<>();
		
		Map<String, Map<String,Integer>> parentGroupHash = new HashMap<>();
		
		if (account == null) {
			return parentGroupHash;
		}

		List<Group> groupList = groupDAO.getGroups(account);

		for (Group group : groupList) {			
		    //get a list of count per network into a hashmap
			groupHash = getGroupMap(group);
			       
			//put a the network count per group  and group name into a hashmp   
			parentGroupHash.put(group.getName(), groupHash );  
		}
		

		return parentGroupHash;
	}
	
	
	
	/**
	 * Given a group, this method returns a Map where the key is the network 
	 * name and the value is the count in the network
	 * 
	 * @param group
	 * @return
	 */
	protected Map<String,Integer> getGroupMap(Group group) {
		Map<String,Integer> groupMap = new HashMap<>();
		
		// Set the count of all networks to zero
		Iterator<String> keys = networkHash.keySet().iterator();
		while(keys.hasNext()) {
			groupMap.put(networkHash.get(keys.next()).getName(), 0);
		}
		
		List<Phone> phoneList = new LinkedList<>();
		
		List<Contact> contactList =	ctGrpDAO.getContacts(group); 
		
		for(Contact contact : contactList) {
			phoneList.addAll(phoneDAO.getPhones(contact) );
		}
		
		// At this stage, the phoneList has all the phones of the group
		Network network;
		for(Phone phone : phoneList) {
			network = networkHash.get(phone.getNetworkuuid());
			groupMap.put(network.getName(), groupMap.get(network.getName()) + 1);
		}// end 'for(Phone phone : phoneList)'
		
		groupMap.put("Total contacts", contactList.size());
		
		return groupMap;
	}

	
	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException, IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	
	private static final long serialVersionUID = 2839939167530680340L;
}
