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

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
 
/**
 * Produces a bar chart for the Outgoing SMS in a user account.
 * <p>
 *  
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class OutgoingBarDay extends HttpServlet {

    private static final long serialVersionUID = -7965916253424645316L;

    final String CHART_TITLE = "Outgoing SMS by Day";
    final int CHART_WIDTH = 800;
    final int CHART_HEIGHT = 600;

    /**
     * Number of days over which to display the graph
     */
    public static final int DAY_COUNT = 7; // Number of days over which to display the graph

    //private DefaultCategoryDataset dataset;

    CountUtils countUtils;
    NetworkDAO networkDAO;

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
        statisticsCache = mgr.getCache(CacheVariables.CACHE_STATISTICS_BY_ACCOUNT);
        countUtils = CountUtils.getInstance();
        networkDAO = NetworkDAO.getInstance();
        fromDate = new Date();
        
    }

    /**
     * @param request
     * @param response
     * @throws ServletException, IOException
     * @throws java.io.IOException
     * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doPost(request, response);
    }

    /**
     * @param request
     * @param response
     * @throws ServletException, IOException
     * @throws java.io.IOException
     * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse
     * response)
     */
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        OutputStream out = response.getOutputStream();

        accountUuid = request.getParameter("accountuuid");
        
        try {
            String from = request.getParameter("from");
            System.out.println(request.getParameter("from"));
            String to = request.getParameter("to");
            System.out.println(request.getParameter("to"));
            SimpleDateFormat formatter = new SimpleDateFormat("MM/dd/yyyy");

            fromDate = formatter.parse(from);
            toDate = formatter.parse(to);

        } catch (NullPointerException | ParseException e) {

        }
        
        response.setContentType("text/plain;charset=UTF-8");
        
        System.out.println("outgoing Bar: " + getJsonOutgoing(accountUuid));
        out.write(getJsonOutgoing(accountUuid).getBytes());
        out.flush();
        out.close();
        
        //response.setContentType("image/png");
        
        //ChartUtilities.writeChartAsPNG(out, getChart(), CHART_WIDTH, CHART_HEIGHT);
    }
    /**
     * Creates Json information for outgoing SMS information against all {@link Network}s.
     * <p>
     * An example is:<br/>
     * {"data":[{"date":"Apr 20","Orange KE":98,"Safaricom KE":145,"Airtel KE":63},
     *			{"date":"Apr 21","Orange KE":70,"Safaricom KE":180,"Airtel KE":120},
     * 			{"date":"Apr 22","Orange KE":20,"Safaricom KE":100,"Airtel KE":140},
     * 			{"date":"Apr 23","Orange KE":5,"Safaricom KE":20,"Airtel KE":9},
     * 			{"date":"Apr 24","Orange KE":65,"Safaricom KE":56,"Airtel KE":10},
     * 			{"date":"Apr 25","Orange KE":27,"Safaricom KE":72,"Airtel KE":75},
     * 			{"date":"Apr 26","Orange KE":102,"Safaricom KE":63,"Airtel KE":48}
     * ]}
     *
     * @return	a Json String
     */
    private String getJsonOutgoing(String accountUuid) {
    	Gson g = new GsonBuilder().disableHtmlEscaping().create();

    	
    	HashMap<String, Map<String,Object>> countHash = new HashMap<>();
        
        ///////////////////////////////////
        
        SessionStatistics statistics = new SessionStatistics();
        Element element;
        String dateStr;
        if ((element = statisticsCache.get(accountUuid)) != null) {
            statistics = (SessionStatistics) element.getObjectValue();
        }
        Map<String, Map<Network, Integer>> networkOutgoingUSSDCountDay = new HashMap<String, Map<Network, Integer>>();
        //dataset = new DefaultCategoryDataset();

        DateTime dateMidnightStart ;
        if (!(fromDate == null) && !(toDate == null)) {
            dateMidnightStart = new DateTime(fromDate);
        }else {
            dateMidnightStart = DateTime.now().minus(Hours.hours(24 * (DAY_COUNT)));
        }
        
        int numDays = 0;

        if ((element = statisticsCache.get(accountUuid)) != null) {
            statistics = (SessionStatistics) element.getObjectValue();
        }

        networkOutgoingUSSDCountDay = statistics.getNetworkOutgoingUSSDCountDay();

        Map<Network, Integer> networkOutgoingUSSDCount;
        Map<String, Object> networkSMSCount = new HashMap<String, Object>();
        
        Iterator<Network> networkIter;
        Network network;

        do {
            dateStr = new SimpleDateFormat("MMM d").format(new Date(dateMidnightStart.getMillis()));
            networkOutgoingUSSDCount = networkOutgoingUSSDCountDay.get(dateStr);

            if (networkOutgoingUSSDCount != null) {	// It is possible that on particular days the account has no incoming USSD
                networkIter = networkOutgoingUSSDCount.keySet().iterator();
                while (networkIter.hasNext()) {
                	
                    network = networkIter.next();
                    //networkSMSCount.put("date", dateStr.toString());
                    
                    //networkSMSCount.put(network.getName(), networkOutgoingUSSDCount.get(network));
                    //countHash.put("data", networkSMSCount);
                    
                    //dataset.addValue(networkOutgoingUSSDCount.get(network), network.getName(), dateStr);
                    
                }
               
            }
            dateMidnightStart = dateMidnightStart.plus(Hours.hours(24));
            numDays++;
           
        } while (numDays < DAY_COUNT);
        
       return g.toJson(countHash);
    }
   
    
    /**
     *
     * @return	chart
     *
    private JFreeChart getChart() {
        SessionStatistics statistics = new SessionStatistics();
        Element element;
        String dateStr;
        Map<String, Map<Network, Integer>> networkOutgoingUSSDCountDay = new HashMap<String, Map<Network, Integer>>();
        dataset = new DefaultCategoryDataset();

        DateTime dateMidnightStart ;
        if (!(fromdt == null) && !(todt == null)) {
            dateMidnightStart = new DateTime(fromdt);
        }else {
            dateMidnightStart = DateTime.now().minus(Hours.hours(24 * (DAY_COUNT)));
        }
        
        int numDays = 0;
//        DateTime dateMidnightStart = DateTime.now().minus(Hours.hours(24 * (DAY_COUNT)));

        if ((element = statisticsCache.get(accountUuid)) != null) {
            statistics = (SessionStatistics) element.getObjectValue();
        }

        networkOutgoingUSSDCountDay = statistics.getNetworkOutgoingUSSDCountDay();

        Map<Network, Integer> networkOutgoingUSSDCount;
        Iterator<Network> networkIter;
        Network network;

        do {
            dateStr = new SimpleDateFormat("MMM d").format(new Date(dateMidnightStart.getMillis()));
            networkOutgoingUSSDCount = networkOutgoingUSSDCountDay.get(dateStr);

            if (networkOutgoingUSSDCount != null) {	// It is possible that on particular days the account has no incoming USSD
                networkIter = networkOutgoingUSSDCount.keySet().iterator();
                while (networkIter.hasNext()) {
                    network = networkIter.next();
                    dataset.addValue(networkOutgoingUSSDCount.get(network), network.getName(), dateStr);
                }
            }
            dateMidnightStart = dateMidnightStart.plus(Hours.hours(24));
            numDays++;
        } while (numDays < DAY_COUNT);

        JFreeChart chart = ChartFactory.createStackedBarChart(
                CHART_TITLE + " for the last 7 days.", // chart title
                "Day", // domain axis label
                "Count", // range axis label
                dataset, // data
                PlotOrientation.VERTICAL, // orientation
                true, // include legend
                true, // tooltips?
                false // URLs?
        );

        CategoryPlot categoryPlot = (CategoryPlot) chart.getCategoryPlot();

        StackedBarRenderer stackedBarRenderer = (StackedBarRenderer) categoryPlot.getRenderer();

        int seriesIndex = -1;
		//set the colors for the series

        seriesIndex = dataset.getRowIndex("Safaricom");
        if (seriesIndex >= 0) { //-1 means it doesn't exist
            stackedBarRenderer.setSeriesPaint(dataset.getRowIndex("Safaricom"), new Color(147, 192, 31));	//strong green for Safaricom 147, 192, 31
        }

        seriesIndex = dataset.getRowIndex("Airtel");
        if (seriesIndex >= 0) {
            stackedBarRenderer.setSeriesPaint(dataset.getRowIndex("Airtel"), new Color(219, 3, 12));	//vivid red for Airtel
        }

        seriesIndex = dataset.getRowIndex("Orange");
        if (seriesIndex >= 0) {
            stackedBarRenderer.setSeriesPaint(dataset.getRowIndex("Orange"), new Color(255, 102, 0));	//pure orange for Orange
        }

        seriesIndex = dataset.getRowIndex("Yu");
        if (seriesIndex >= 0) {
            stackedBarRenderer.setSeriesPaint(dataset.getRowIndex("Yu"), new Color(255, 255, 51));	//vivid yellow for Yu
        }

        //set the color of the labels
        stackedBarRenderer.setBaseItemLabelGenerator(
                new StandardCategoryItemLabelGenerator());
        stackedBarRenderer.setBaseItemLabelPaint(new Color(255, 255, 255));
        stackedBarRenderer.setBaseItemLabelsVisible(true);

        return chart;
    }*/

}
