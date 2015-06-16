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
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.beans.contact.Group;
import ke.co.tawi.babblesms.server.persistence.contacts.GroupDAO;
import ke.co.tawi.babblesms.server.persistence.network.networkcountDAO;

import java.io.IOException;
import java.io.OutputStream;
import java.util.List;
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
 *{"Management":{"saf com":"92",    "orange":"16 ",  "airtel":"39 ",   "yu" :"17 "},' 
 *'"   Parents":{"saf com" :"72",   "orange":"156 ",  "airtel":"71 ",  "yu" :"35 "},'
 *"   Teachers":{"saf com" :"223 ", "orange":"17 ",   "airtel":"6 ",   "yu" :"5 "},'
 *"      Staff":{"saf com" :"9 ",   "orange":"16 ",  "airtel":"61 ",   "yu" :"999"},'
 *"   Students":{"saf com" :"21 ",  "orange":"34 ",   "airtel":"27 ",   "yu" :"67"}'
 *}'
 *
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

		groupDAO = GroupDAO.getInstance();
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
	private HashMap<String,HashMap<String,Integer>> getGroupMaps(Account account) {
		
		HashMap<String,Integer> groupHash = new HashMap<>();
		
		HashMap<String,HashMap<String,Integer>> parentGroupHash = new HashMap<>();
		
		networkcountDAO netcount= networkcountDAO.getInstance();

		if (account == null) {
			return parentGroupHash;
		}

		List<Group> groupList = groupDAO.getGroups(account);

		for (Group group : groupList) {			
	    //get a list of count per network into a hashmap	 
		groupHash =netcount.network(group.getUuid());
		       
		//put a the network count per group  and group name into a hashmp   
		parentGroupHash.put(group.getName(),groupHash );  
		}
		

		return parentGroupHash;
	}
	

	/**
	 *
	 * @param request
	 * @param response
	 * @throws ServletException
	 *             , IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
	
	private static final long serialVersionUID = 2839939167530680340L;
}
