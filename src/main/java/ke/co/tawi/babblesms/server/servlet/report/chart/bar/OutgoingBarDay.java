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
package ke.co.tawi.babblesms.server.servlet.report.chart.bar;

import java.io.IOException;
import java.io.OutputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.cache.CacheVariables;
import ke.co.tawi.babblesms.server.persistence.network.NetworkDAO;
import ke.co.tawi.babblesms.server.persistence.utils.CountUtils;
import ke.co.tawi.babblesms.server.session.SessionStatistics;
import net.sf.ehcache.Cache;
import net.sf.ehcache.CacheManager;
import net.sf.ehcache.Element;

import org.joda.time.DateTime;
import org.joda.time.Hours;

import com.google.gson.FieldNamingPolicy;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * Produces a bar chart for the Outgoing SMS in a user account.
 * <p>
 * 
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 * @author <a href="mailto:eugene.g99@gmail.com>Eugene Wang'ombe</a>
 */
public class OutgoingBarDay extends HttpServlet {

	private static final long serialVersionUID = -7965916253424645316L;

	/**
	 * Number of days over which to display the graph
	 */
	public static final int DAY_COUNT = 7;
	private String accountUuid = "";
	Date fromDate = new Date();
	Date toDate = new Date();

	private CacheManager mgr;
	private Cache statisticsCache;

	/**
	 * @param config
	 * @throws ServletException
	 *
	 */
	@Override
	public void init(ServletConfig config) throws ServletException {
		super.init(config);

		mgr = CacheManager.getInstance();
		statisticsCache = mgr
				.getCache(CacheVariables.CACHE_STATISTICS_BY_ACCOUNT);
		CountUtils.getInstance();
		NetworkDAO.getInstance();
		fromDate = new Date();

	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 *             , IOException
	 * @throws java.io.IOException
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doGet(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @param request
	 * @param response
	 * @throws ServletException
	 *             , IOException
	 * @throws java.io.IOException
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
	 *      response)
	 */
	@Override
	protected void doPost(HttpServletRequest request,
			HttpServletResponse response) throws ServletException, IOException {
		OutputStream out = response.getOutputStream();
		accountUuid = request.getParameter("accountuuid");

		try {
			String from = request.getParameter("from");
			String to = request.getParameter("to");

			SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

			fromDate = formatter.parse(from);
			toDate = formatter.parse(to);

		} catch (NullPointerException | ParseException e) {

		}

		response.setContentType("text/plain;charset=UTF-8");

		out.write(getJsonOutgoing(accountUuid).getBytes());
		out.flush();
		out.close();

		// response.setContentType("image/png");

		// ChartUtilities.writeChartAsPNG(out, getChart(), CHART_WIDTH,
		// CHART_HEIGHT);
	}

	/**
	 * Creates Json information for outgoing SMS information against all
	 * {@link Network}s.
	 * <p>
	 * An example is:<br/>
	 * {"outgoingData":[{"date":"Apr 20","orange_ke":98,"safaricom_ke":145,"airtel_ke":63},
	 *					{"date":"Apr 21","orange_ke":70,"safaricom_ke":180,"airtel_ke":120},
						{"date":"Apr 22","orange_ke":20,"safaricom_ke":100,"airtel_ke":140},
	 					{"date":"Apr 23","orange_ke":5,"safaricom_ke":20,"airtel_ke":9},
						{"date":"Apr 24","orange_ke":65,"safaricom_ke":56,"airtel_ke":10},
						{"date":"Apr 25","orange_ke":27,"safaricom_ke":72,"airtel_ke":75},
						{"date":"Apr 26","orange_ke":102,"safaricom_ke":63,"airtel_ke":48} ]}
	 * 
	 * @return a Json String
	 */
	private String getJsonOutgoing(String accountUuid) {
		Gson g = new GsonBuilder().disableHtmlEscaping()
				.setFieldNamingPolicy(FieldNamingPolicy.UPPER_CAMEL_CASE)
				.setPrettyPrinting().serializeNulls().create();

		HashMap<String, ArrayList<Map<String, Object>>> countHash = new HashMap<>();

		SessionStatistics statistics = new SessionStatistics();
		Element element;
		String dateStr;
		if ((element = statisticsCache.get(accountUuid)) != null) {
			statistics = (SessionStatistics) element.getObjectValue();
		}
		Map<String, Map<Network, Integer>> networkOutgoingUSSDCountDay = new HashMap<String, Map<Network, Integer>>();
		// TODO sort out calculations of dates
		DateTime dateMidnightStart;
		if (fromDate == null) {
			dateMidnightStart = new DateTime(fromDate);
		} else {
			dateMidnightStart = DateTime.now().minus(
					Hours.hours(24 * (DAY_COUNT)));
		}

		int numDays = 0;

		if ((element = statisticsCache.get(accountUuid)) != null) {
			statistics = (SessionStatistics) element.getObjectValue();
		}

		networkOutgoingUSSDCountDay = statistics
				.getNetworkOutgoingUSSDCountDay();

		Map<Network, Integer> networkOutgoingUSSDCount;
		// Hold the network and dates with SMS counts
		ArrayList<Map<String, Object>> dateNetworkCountArray = new ArrayList<Map<String, Object>>();

		Iterator<Network> networkIter;
		Network network;

		do {
			dateStr = new SimpleDateFormat("MMM d").format(new Date(
					dateMidnightStart.getMillis()));
			networkOutgoingUSSDCount = networkOutgoingUSSDCountDay.get(dateStr);

			// recreate the Map in order to void duplicating data
			HashMap<String, Object> dateNetworkCount = new HashMap<String, Object>();

			// It is possible that on particular days the account has no
			// incoming SMS
			if (networkOutgoingUSSDCount != null) {
				networkIter = networkOutgoingUSSDCount.keySet().iterator();
				dateNetworkCount.put("date", dateStr.toString());
				while (networkIter.hasNext()) {

					network = networkIter.next();
					// add network names and SMS count to Map. Remove spaces in
					// the network name to avoid typeErrors in JavaScript
					dateNetworkCount.put(network.getName().replace(" ", "_")
							.toLowerCase(),
							networkOutgoingUSSDCount.get(network));

				}
				// add the network statistics and the date to an array. It will
				// be converted to a JSON array
				dateNetworkCountArray.add(dateNetworkCount);
			}

			dateMidnightStart = dateMidnightStart.plus(Hours.hours(24));
			numDays++;

		} while (numDays < DAY_COUNT);
		// finally put the array into a Map which can be converted into a JSON
		// object
		countHash.put("outgoingData", dateNetworkCountArray);
		return g.toJson(countHash);
	}

}
