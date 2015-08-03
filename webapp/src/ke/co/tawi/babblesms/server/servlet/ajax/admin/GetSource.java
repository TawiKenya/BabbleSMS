package ke.co.tawi.babblesms.server.servlet.ajax.admin;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ke.co.tawi.babblesms.server.beans.account.Account;
import ke.co.tawi.babblesms.server.beans.maskcode.Mask;
import ke.co.tawi.babblesms.server.beans.maskcode.Shortcode;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GetSource extends HttpServlet{
	/**
	 * 
	 */
	private static final long serialVersionUID = 2951845021521153428L;
	private Cache accountsCache;
	private Cache shortcodesCache;
	private Cache maskCache;
	private Cache networksCache;
	
	/**
	 * @param config
	 * @throws ServletException
	 * @see javax.servlet.GenericServlet#init(javax.servlet.ServletConfig)
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		CacheManager mgr = CacheManager.getInstance();
		accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_UUID);
		shortcodesCache = mgr.getCache(CacheVariables.CACHE_SHORTCODE_BY_UUID);
		maskCache = mgr.getCache(CacheVariables.CACHE_MASK_BY_UUID);
		networksCache = mgr.getCache(CacheVariables.CACHE_NETWORK_BY_UUID);
	  }
	
	
	/**
	 * @see javax.servlet.http.HttpServlet#doPost(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
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
		
		out.write(gson.toJson(getSource(account)).getBytes());
		out.flush();
		out.close();
	}
	
	

	
	private List <  HashMap <String,String>> getSource( Account account) {
		List <  HashMap <String,String>>  SourceList = new ArrayList<>();	
		HashMap <String,String> networkHash = new HashMap<>();
		Mask mask = new Mask();
		Shortcode shortcode = new Shortcode();
		Network network = new Network();
		Element element;
		List<?> keys;
		
		keys = networksCache.getKeys();
	    for (Object key : keys) {
	        element = networksCache.get(key);
	        network = (Network) element.getObjectValue();
	        networkHash.put(network.getUuid(), network.getName());
	    }

		keys = shortcodesCache.getKeys();
	    for (Object key : keys) {
	    	HashMap <String,String> sourceHash = new HashMap<>();
	        element = shortcodesCache.get(key);
	        shortcode = (Shortcode) element.getObjectValue();
	        if (account.getUuid().equals(shortcode.getAccountuuid())) {
	        sourceHash.put("uuid",shortcode.getUuid());
	        sourceHash.put("name",shortcode.getCodenumber()+"  ("+networkHash.get(shortcode.getNetworkuuid())+")");       
	           }
	        if(!sourceHash.isEmpty()){
	        SourceList.add(sourceHash);
	        }
	    }

	    keys = maskCache.getKeys();
	    for (Object key : keys) {
	    	HashMap <String,String> sourceHash = new HashMap<>();
	         element = maskCache.get(key);
	         mask = (Mask) element.getObjectValue();    
	         if (account.getUuid().equals(mask.getAccountuuid())) {
	         sourceHash.put("uuid", mask.getUuid());
	         sourceHash.put("name", mask.getMaskname()+"  ("+networkHash.get(mask.getNetworkuuid())+")");
	        
	         }
	         if(!sourceHash.isEmpty()){
	 	        SourceList.add(sourceHash);
	 	        }
	    }		
		return SourceList;
	}

	/**
	 * @see javax.servlet.http.HttpServlet#doGet(javax.servlet.http.HttpServletRequest, javax.servlet.http.HttpServletResponse)
	 * @param request
	 * @param response
	 * @throws ServletException
	 * IOException
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	
}
