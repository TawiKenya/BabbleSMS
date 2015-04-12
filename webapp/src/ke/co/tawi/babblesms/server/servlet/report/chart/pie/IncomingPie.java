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
package ke.co.tawi.babblesms.server.servlet.report.chart.pie;

import ke.co.tawi.babblesms.server.session.SessionStatistics;
import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.cache.CacheVariables;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

/**
*
* Produces a pie chart for incoming SMS.
 * <p>
*
* @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
* @author <a href="mailto:eugene@tawi.mobi">Eugene Chimita</a>
*/

public class IncomingPie extends HttpServlet {

    private static final long serialVersionUID = 2190230806914521830L;
    
    private Cache statisticsCache;
    

    /**
     * @param config
     * @throws ServletException
     */
    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        CacheManager mgr = CacheManager.getInstance();
        
        statisticsCache = mgr.getCache(CacheVariables.CACHE_STATISTICS_BY_ACCOUNT);
    }
 
    
    /**
     * @param request
     * @param response
     * @throws ServletException, IOException
     */
    @Override
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		OutputStream out = response.getOutputStream();
				
		String accountUuid = request.getParameter("accountuuid");
		
		response.setContentType("text/plain;charset=UTF-8");
		response.setDateHeader("Expires", new Date().getTime()); // Expiration date
		response.setDateHeader("Date", new Date().getTime()); // Date and time
															  // that the message was sent
		
		out.write(getJsonIncoming(accountUuid).getBytes());
		out.flush();
		out.close();		
	}

    
    /**
     * Creates Json information for incoming SMS information against all {@link Network}s.
     * <p>
     * An example is:<br/>
     * {"Orange KE":1141,"Safaricom KE":3713,"Airtel KE":1189}
     *
     * @return	a Json String
     */
	private String getJsonIncoming(String accountUuid) {    	
    	Gson g = new GsonBuilder().disableHtmlEscaping().create();
    	
        HashMap<String, Integer> countHash = new HashMap<String, Integer>();
       
        Element element;
        SessionStatistics statistics = null;

        if ((element = statisticsCache.get(accountUuid)) != null) {
            statistics = (SessionStatistics) element.getObjectValue();
        }

        Map<Network, Integer> networkIncomingSMSCount = statistics.getNetworkIncomingCount();
        Iterator<Network> incomingIter = networkIncomingSMSCount.keySet().iterator();
        Network network;

        while (incomingIter.hasNext()) {
            network = incomingIter.next();
            countHash.put(network.getName(), networkIncomingSMSCount.get(network));
        }
               
        return g.toJson(countHash);
    }
    	
    	
	/**
     * @param request
     * @param response
     * @throws ServletException, IOException
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }
}
