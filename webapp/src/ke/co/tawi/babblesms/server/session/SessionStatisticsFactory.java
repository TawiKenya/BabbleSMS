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

import ke.co.tawi.babblesms.server.beans.network.Network;
import ke.co.tawi.babblesms.server.persistence.network.NetworkDAO;
import ke.co.tawi.babblesms.server.persistence.utils.CountUtils;
import ke.co.tawi.babblesms.server.servlet.report.chart.bar.IncomingBarDay;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.MutableDateTime;


/**
 * Factory used to create statistics to be held in the session of account 
 * holder when logged into the portal.
 * <p>
 *
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SessionStatisticsFactory {

    private static CountUtils countUtils;
    private static NetworkDAO networkDAO;

    static {
        countUtils = CountUtils.getInstance();
        networkDAO = NetworkDAO.getInstance();
    }

    
    /**
     *
     * @param accountUuid
     * @return session statistics
     */
    public static SessionStatistics getSessionStatistics(String accountUuid) {
        int count;
        //int[] arraycount = new int[2];

        SessionStatistics stats = new SessionStatistics();

        // Get the list of all networks
        List<Network> networkList = networkDAO.getAllNetworks();;

        //set the count of all incoming SMS
        stats.setAllIncomingSMSCount(countUtils.getIncomingCount(accountUuid));

        //set the count of all outgoing SMS
        stats.setAllOutgoingSMSCount(countUtils.getOutgoingLog(accountUuid));

        //set the count of all outgoing Group SMS
        //stats.setAllOutgoingSMSCount(countUtils.getOutgoingGroupLog(accountUuid));
        

        // Set up data for the pie charts
        for (Network network : networkList) {
            //get the count of incoming SMS according to the account and network
            count = countUtils.getIncomingCount(accountUuid, network);
            
            //if count is greater than zero, add the information
            if (count > 0) {
                stats.addNetworkIncomingCount(network, count);
            }
            
            //get the count of outgoing SMS according to the account and network
            count = countUtils.getOutgoingCount(accountUuid, network);
            
            //if count is greater than zero, add the information
            if (count > 0) {
                stats.addNetworkOutgoingSMSCount(network, count);
            }
        }

        // Set up data for the bar charts
        DateTime dateMidnightStart = DateTime.now().minus(Hours.hours(24 * (IncomingBarDay.DAY_COUNT )));
        DateTime dateMidnightEnd = dateMidnightStart.plus(Hours.hours(24));
        int numDays = 0;
        do {
            for (Network network : networkList) {
            	
                //get the daily count for incoming
                count = countUtils.getIncomingCount(accountUuid, network,
                        new Date(dateMidnightStart.getMillis()), new Date(dateMidnightEnd.getMillis()));
                if (count > 0) {
                    stats.addNetworkIncomingUSSDCountDay(new SimpleDateFormat("MMM d").format(new Date(dateMidnightStart.getMillis())),
                            network, count);
                }
                //get the daily count for outgoing
                count = countUtils.getOutgoingCount(accountUuid, network,
                        new Date(dateMidnightStart.getMillis()), new Date(dateMidnightEnd.getMillis()));

                if (count > 0) {
                    stats.addNetworkOutgoingUSSDCountDay(new SimpleDateFormat("MMM d").format(new Date(dateMidnightStart.getMillis())),
                            network, count);
                }

            }
            dateMidnightStart = dateMidnightStart.plus(Hours.hours(24));
            dateMidnightEnd = dateMidnightEnd.plus(Hours.hours(24));
            numDays++;
        } while (numDays < IncomingBarDay.DAY_COUNT);

        // Set up data for the Weekly bar charts                
        int numWeeks = 0;
        MutableDateTime startwkMutableDateTime = new MutableDateTime();
        MutableDateTime endwkMutableDateTime;
        startwkMutableDateTime.setDayOfWeek(1); //get the first day of the week
        startwkMutableDateTime.setMillisOfDay(0);       //get 00:00:00 time of the day

        //go back to 7 weeks
        startwkMutableDateTime.addWeeks(-7);

        do {
            // set the end date by creating a copy
            endwkMutableDateTime = new MutableDateTime(startwkMutableDateTime);
            //push it by one week
            endwkMutableDateTime.addWeeks(1);

            for (Network network : networkList) {

                //get the Weekly count for Incoming USSD
                count = countUtils.getIncomingCount(accountUuid, network,
                        new Date(startwkMutableDateTime.toDate().getTime()), new Date(endwkMutableDateTime.toDate().getTime()));

                if (count > 0) {
                    stats.addNetworkIncomingUSSDCountWeek(new SimpleDateFormat("MMM d").format(new Date(startwkMutableDateTime.getMillis())),
                            network, count);
                }

                //get the Weekly count for Outgoing USSD
                count = countUtils.getOutgoingCount(accountUuid, network,
                        new Date(startwkMutableDateTime.toDate().getTime()), new Date(endwkMutableDateTime.toDate().getTime()));
                if (count > 0) {
                    stats.addNetworkOutgoingUSSDCountWeek(new SimpleDateFormat("MMM d").format(new Date(startwkMutableDateTime.getMillis())),
                            network, count);
                }

            }
            // get the next week
            startwkMutableDateTime.addWeeks(1);
            numWeeks++;
        } while (numWeeks < 7);

        // Set up data for the monthly bar charts               
        int numMonths = 0;
        MutableDateTime startMutableDateTime = new MutableDateTime();
        MutableDateTime endMutableDateTime;
        startMutableDateTime.setDayOfMonth(1);  //get the first day of the month
        startMutableDateTime.setMillisOfDay(0); //get 00:00:00 time of the day

        //go back to 6 months
        startMutableDateTime.addMonths(-5);

        do {
            //set the end date by creating a copy
            endMutableDateTime = new MutableDateTime(startMutableDateTime);
            //push it by one month
            endMutableDateTime.addMonths(1);
            //System.out.println("Start date: " + startMutableDateTime);
            //System.out.println("End date: " + endMutableDateTime);

            for (Network network : networkList) {
                //change to use millis
                count = countUtils.getIncomingCount(accountUuid, network,
                        new Date(startMutableDateTime.toDate().getTime()), new Date(endMutableDateTime.toDate().getTime()));

                if (count > 0) {
                    stats.addNetworkIncomingUSSDCountMonth(new SimpleDateFormat("MMM").format(new Date(startMutableDateTime.getMillis())),
                            network, count);
                }

                //System.out.println(count);                            
            }
            //get the next month
            startMutableDateTime.addMonths(1);
            numMonths++;
        } while (numMonths < 6);

        return stats;
    }

}

