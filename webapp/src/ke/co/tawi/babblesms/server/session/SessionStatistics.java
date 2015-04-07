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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;


/**
 * Holds data that is displayed on the portal once a user has logged in.
 * <p>
 * The data is meant to be cached while a session is still active to avoid
 * expensive computations with the RDBMS.
 * <p>
 *
 * @author <a href="mailto:michael@tawi.mobi">Michael Wakahe</a>
 */
public class SessionStatistics implements Serializable {

    private static final long serialVersionUID = 350183909951635381L;

    private int allIncomingSMSCount;
    private int allOutgoingSMSCount;

    //They are used to keep count against Month
    private final Map<String, Integer> accountCount;

    // These are used to keep a count of SMS against Network
    // They are used to generate pie charts
    private final Map<Network, Integer> networkIncomingSMSCount;
    private final Map<Network, Integer> networkOutgoingSMSCount;


    // These are used to keep a count of USSD against Network on particular days
    // They are used to generate bar charts
    private final Map<String, Map<Network, Integer>> networkIncomingUSSDCountDay;
    private final Map<String, Map<Network, Integer>> networkOutgoingUSSDCountDay;


    // These are used to keep a count of USSD against Network on specific months
    // They are used to generate bar charts
    private final Map<String, Map<Network, Integer>> networkIncomingUSSDCountWeek;
    private final Map<String, Map<Network, Integer>> networkOutgoingUSSDCountWeek;

    // These are used to keep a count of USSD against Network on specific months
    // They are used to generate bar charts
    private final Map<String, Map<Network, Integer>> networkIncomingUSSDCountMonth;
    private final Map<String, Map<Network, Integer>> networkOutgoingUSSDCountMonth;

    /**
     *
     */
    public SessionStatistics() {
        allIncomingSMSCount = 0;
        allOutgoingSMSCount = 0;

        accountCount = new HashMap<>();

        networkIncomingSMSCount = new HashMap<>();
        networkOutgoingSMSCount = new HashMap<>();

        networkIncomingUSSDCountDay = new HashMap<>();
        networkOutgoingUSSDCountDay = new HashMap<>();

        networkIncomingUSSDCountWeek = new HashMap<>();
        networkOutgoingUSSDCountWeek = new HashMap<>();

        networkIncomingUSSDCountMonth = new HashMap<>();
        networkOutgoingUSSDCountMonth = new HashMap<>();
    }

    /**
     * @return the allIncomingUSSDCount
     */
    public int getAllIncomingUSSDCount() {
        return allIncomingSMSCount;
    }

    /**
     * @param allIncomingSMSCount - the allIncomingUSSDCount to set
     */
    public void setAllIncomingSMSCount(int allIncomingSMSCount) {
        this.allIncomingSMSCount = allIncomingSMSCount;
    }

    /**
     * @return the allOutgoingUSSDCount
     */
    public int getAllOutgoingUSSDCount() {
        return allOutgoingSMSCount;
    }

    /**
     * @param allOutgoingSMSCount - the allOutgoingUSSDCount to set
     */
    public void setAllOutgoingSMSCount(int allOutgoingSMSCount) {
        this.allOutgoingSMSCount = allOutgoingSMSCount;
    }

    /**
     * Returns a reference to the original map.
     *
     * @return the networkIncomingUSSDCount
     */
    public Map<Network, Integer> getNetworkIncomingUSSDCount() {
        return networkIncomingSMSCount;
    }

    
    
    /**
     *
     * @param network
     * @param count
     */
    public void addNetworkIncomingSMSCount(Network network, int count) {
        networkIncomingSMSCount.put(network, new Integer(count));
    }

    
    /**
     * Returns a reference to the original map.
     *
     * @return the networkOutgoingUSSDCount
     */
    public Map<Network, Integer> getNetworkOutgoingUSSDCount() {
        return networkOutgoingSMSCount;
    }
    
    
    /**
     * Returns a reference to the original map.
     *
     * @return the networkOutgoingSMSCount
     */
    public Map<Network, Integer> getNetworkOutgoingCount() {
        return networkOutgoingSMSCount;
    }
    

    /**
     *
     * @return accountCount
     */
    public Map<String, Integer> getAccountCount() {
        return accountCount;
    }

    /**
     *
     * @param month
     * @param count
     */
    public void addAccountCount(String month, int count) {
        accountCount.put(month, new Integer(count));
    }
    
    /**
     *
     * @param network
     * @param count
     */
    public void addNetworkOutgoingUSSDCount(Network network, int count) {
        networkOutgoingSMSCount.put(network, new Integer(count));
    }
    
      
    /**
     *
     * @param network
     * @param count
     */
    public void addNetworkOutgoingSMSCount(Network network, int count) {
    	networkOutgoingSMSCount.put(network, count);
    }


    /**
     * @return the networkIncomingUSSDCountDay
     */
    public Map<String, Map<Network, Integer>> getNetworkIncomingUSSDCountDay() {
        return networkIncomingUSSDCountDay;
    }

    /**
     *
     * @param day
     * @param network
     * @param count
     */
    public void addNetworkIncomingUSSDCountDay(String day, Network network, int count) {
        Map<Network, Integer> networkMap = networkIncomingUSSDCountDay.get(day);

        if (networkMap == null) {
            networkMap = new HashMap<>();
        }

        networkMap.put(network, new Integer(count));
        networkIncomingUSSDCountDay.put(day, networkMap);
    }

    /**
     * @return the networkOutgoingUSSDCountDay
     */
    public Map<String, Map<Network, Integer>> getNetworkOutgoingUSSDCountDay() {
        return networkOutgoingUSSDCountDay;
    }

    /**
     *
     * @param day
     * @param network
     * @param count
     */
    public void addNetworkOutgoingUSSDCountDay(String day, Network network, int count) {
        Map<Network, Integer> networkMap = networkOutgoingUSSDCountDay.get(day);

        if (networkMap == null) {
            networkMap = new HashMap<>();
        }

        networkMap.put(network, new Integer(count));
        networkOutgoingUSSDCountDay.put(day, networkMap);
    }

    /**
     * @return the networkIncomingUSSDCountMonth
     */
    public Map<String, Map<Network, Integer>> getNetworkIncomingUSSDCountMonth() {
        return networkIncomingUSSDCountMonth;
    }

    /**
     * @return the networkIncomingUSSDCountWeek
     */
    public Map<String, Map<Network, Integer>> getNetworkIncomingUSSDCountWeek() {
        return networkIncomingUSSDCountWeek;
    }

    /**
     *
     * @param month
     * @param network
     * @param count
     */
    public void addNetworkIncomingUSSDCountMonth(String month, Network network, int count) {
        Map<Network, Integer> networkMap = networkIncomingUSSDCountMonth.get(month);

        if (networkMap == null) {
            networkMap = new HashMap<>();
        }

        networkMap.put(network, new Integer(count));
        networkIncomingUSSDCountMonth.put(month, networkMap);
    }

    /**
     *
     * @param week
     * @param network
     * @param count
     */
    public void addNetworkIncomingUSSDCountWeek(String week, Network network, int count) {
        Map<Network, Integer> networkMap = networkIncomingUSSDCountWeek.get(week);

        if (networkMap == null) {
            networkMap = new HashMap<>();
        }

        networkMap.put(network, new Integer(count));
        networkIncomingUSSDCountWeek.put(week, networkMap);
    }

    /**
     * @return the networkOutgoingUSSDCountMonth
     */
    public Map<String, Map<Network, Integer>> getNetworkOutgoingUSSDCountMonth() {
        return networkOutgoingUSSDCountMonth;
    }

    /**
     * @return the networkOutgoingUSSDCountMonth
     */
    public Map<String, Map<Network, Integer>> getNetworkOutgoingUSSDCountWeek() {
        return networkOutgoingUSSDCountWeek;
    }

    /**
     *
     * @param month
     * @param network
     * @param count
     */
    public void addNetworkOutgoingUSSDCountMonth(String month, Network network, int count) {
        Map<Network, Integer> networkMap = networkOutgoingUSSDCountMonth.get(month);

        if (networkMap == null) {
            networkMap = new HashMap<>();
        }

        networkMap.put(network, new Integer(count));
        networkOutgoingUSSDCountMonth.put(month, networkMap);
    }

    
    /**
     *
     * @param week
     * @param network
     * @param count
     */
    public void addNetworkOutgoingUSSDCountWeek(String week, Network network, int count) {
        Map<Network, Integer> networkMap = networkOutgoingUSSDCountWeek.get(week);

        if (networkMap == null) {
            networkMap = new HashMap<>();
        }

        networkMap.put(network, new Integer(count));
        networkOutgoingUSSDCountWeek.put(week, networkMap);
    }
    

}

 