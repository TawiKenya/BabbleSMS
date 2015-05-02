<%
    /**
    Copyright 2015 Tawi Commercial Services Ltd

    Licensed under the Open Software License, Version 3.0 (the ?License?); you may 
    not use this file except in compliance with the License. You may obtain a copy 
    of the License at:
    http://opensource.org/licenses/OSL-3.0

    Unless required by applicable law or agreed to in writing, software distributed 
    under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
    CONDITIONS OF ANY KIND, either express or implied.

    See the License for the specific language governing permissions and limitations 
    under the License.
    */
%>

<%@page import="ke.co.tawi.babblesms.server.beans.network.Network"%>
<%@page import="ke.co.tawi.babblesms.server.beans.account.Account"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionConstants"%>
<%@page import="ke.co.tawi.babblesms.server.session.SessionStatistics"%>
<%@page import="ke.co.tawi.babblesms.server.persistence.utils.CountUtils"%>
<%@page import="ke.co.tawi.babblesms.server.cache.CacheVariables"%>

<%@page import="java.util.ArrayList"%>
<%@page import="java.util.List"%>
<%@page import="java.util.Arrays"%>
<%@page import="java.util.Date"%>
<%@page import="java.util.Iterator"%>
<%@page import="java.util.Map"%>
<%@page import="java.net.URLEncoder"%>

<%@page import="org.apache.commons.lang3.StringUtils"%>

<%@page import="net.sf.ehcache.Element"%>
<%@page import="net.sf.ehcache.Cache"%>
<%@page import="net.sf.ehcache.CacheManager"%>

<%@page import="org.joda.time.MutableDateTime"%>



<%
    // The following is for session management.    
    if (session == null) {
        response.sendRedirect("../index.jsp");
    }

    String username = (String) session.getAttribute(SessionConstants.ACCOUNT_SIGN_IN_KEY);
    if (StringUtils.isEmpty(username)) {
        response.sendRedirect("../index.jsp");
    }

    session.setMaxInactiveInterval(SessionConstants.SESSION_TIMEOUT);
    response.setHeader("Refresh", SessionConstants.SESSION_TIMEOUT + "; url=../logout");

    CacheManager mgr = CacheManager.getInstance();
    Cache accountsCache = mgr.getCache(CacheVariables.CACHE_ACCOUNTS_BY_USERNAME);
    Cache statisticsCache = mgr.getCache(CacheVariables.CACHE_STATISTICS_BY_ACCOUNT);
    SessionStatistics statistics = new SessionStatistics();

    Account account = new Account();
    Element element;

    if ((element = accountsCache.get(username)) != null) {
        account = (Account) element.getObjectValue();
    }

    String accountuuid = account.getUuid();

    if ((element = statisticsCache.get(accountuuid)) != null) {
        statistics = (SessionStatistics) element.getObjectValue();
    }

    CountUtils ct = CountUtils.getInstance();
    int incoming = ct.getIncomingLog(accountuuid);
    int outgoing = ct.getOutgoingLog(accountuuid);
    int contacts = ct.getContacts(accountuuid);
    int groups = ct.getGroups(accountuuid);

%>
<jsp:include page="messageheader.jsp" />




<div>
    <ul class="breadcrumb">
        <li>
            Home<span class="divider">/</span>
        </li>
        <li>
            Quick Reports
        </li>
    </ul>
</div>
<div class="sortable row-fluid">
  
    <!-- <div style="width:500px; height:300px;" id="mobile"/> -->
   <a data-rel="tooltip" title="6 new groups." class="well span3 top-block" href="inbox.jsp">
        <span class="icon32 icon-red icon-user"></span>
        <div>Incoming Messages</div>
        <div><%= incoming%></div>
        <span class="notification yellow"><%= incoming%></span>
    </a>

    <a data-rel="tooltip" title="4 new pro members." class="well span3 top-block" href="sent.jsp">
        <span class="icon32 icon-color icon-star-on"></span>
        <div>Sent Messages</div>
        <div><%= outgoing%></div>
        <span class="notification yellow"><%= outgoing%></span>
    </a>

    <a data-rel="tooltip" title="320 contacts." class="well span3 top-block" href="contact.jsp">
        <span class="icon32 icon-color icon-cart"></span>
        <div>Total Contacts</div>
        <div><%= contacts%></div>
        <span class="notification yellow"><%= contacts%></span>
    </a>

    <a data-rel="tooltip" title="12 new messages." class="well span3 top-block" href="groups.jsp">
        <span class="icon32 icon-color icon-envelope-closed"></span>
        <div>Total Groups</div>
        <div><%= groups%></div>
        <span class="notification red"><%= groups%></span>
    </a>
</div>

<div class="row-fluid">
    <div class="box span12">

        <div class="box-content">
            <h1>Babble<small> your convenient messaging tool!</small></h1>
            <p>Welcome to Babble SMS</p>
            <div class="clear"></div>
            <p>The following is a summary of your account activity.</p>


            <article class="stats_overview">
                <div class="overview_today">
                    <p class="overview_day">Total Incoming SMS</p>                                
                    <p class="overview_count"><%=statistics.getAllIncomingUSSDCount()%></p>                                
                    <p class="overview_type">SMS</p>
                </div>
                <div class="overview_previous">
                    <p class="overview_day">Total Outgoing SMS</p>
                    <p class="overview_count"><%= statistics.getAllOutgoingUSSDCount()%></p>                              
                    <p class="overview_type">SMS</p>
                </div>
            </article>
            <div class="clear"></div>

            <div id="incoming_sms" class="content_title">
                <h3>Incoming SMS</h3>
                <p>
                    <a href="#activity_summary"><span>&uarr;&nbsp;</span>Activity Summary</a><span>&nbsp;&bull;&nbsp;</span>
                    <a href="#outgoing_sms"><span>&darr;&nbsp;</span>Outgoing SMS</a><span>&nbsp;&bull;&nbsp;</span>
                    <a href="#submitted_sms"><span>&darr;&nbsp;</span>Submitted SMS</a><span>&nbsp;&bull;&nbsp;</span>   
                    <a href="#top"><span>&uarr;&nbsp;</span>Top</a>
                </p>
            </div>

            <p>
                This is a count of SMS that have been initiated by the mobile user.
            </p>

            <table class="stats">
                <tr>
                    <th>Network</th>
                    <th>Count</th>
                </tr>
                <%
                    Map<Network, Integer> networkIncomingSMSCount = statistics.getNetworkIncomingCount();
                    Iterator<Network> incomingIter = networkIncomingSMSCount.keySet().iterator();
                    Network network;

                    while (incomingIter.hasNext()) {
                        network = incomingIter.next();
                %>
                <tr>
                    <td><%=network.getName()%></td>
                    <td class="count"><%=networkIncomingSMSCount.get(network)%></td>
                </tr>
                <%}%>
            </table>

      
           
            <div id="incomingPieChart" style="width:650px; height:500px;"></div>
            
            <p>&nbsp;&nbsp;&nbsp;</p>
            <!--incoming SMS Bar chart-->
            <div id="incomingSMSBarChart" style="height:400px;width:800px;"></div><br><br>
            <!--<p>
                <img id="inbar" 
                     src="incomingBarDay?accountuuid=<%=URLEncoder.encode(accountuuid, "UTF-8")%>&from=03/04/2015&to=03/21/2015" 
                     alt="Incoming Daily Bar Chart" />
            </p>-->
            
           
            
            <div>
                <form id="incomingform" action='incomingBarDay' method='post'>
                    <%--
                        <p>Select Duration</p>
                        <p>
                            <select name="yyyy">
                                <%
                                    MutableDateTime mt = new MutableDateTime();
                                    int yr = 2015;
                                    for (int i = mt.getYear(); i >= yr; i--) {%>
                                <option> <%=i%> </option>
                                <%}%>

                            </select>
                            <select name="mm">
                                <%
                                    for (int i = 1; i <= 12; i++) {%>
                                <option> <%=i%> </option>
                                <%}%>

                            </select>
                            <select name="dd">
                                <%
                                    for (int i = 1; i <= 31; i++) {%>
                                <option> <%=i%> </option>
                                <%}%>

                            </select>

                            To

                            <select name="yyyy">
                                <%
                                    mt = new MutableDateTime();
                                    yr = 2015;
                                    for (int i = mt.getYear(); i >= yr; i--) {%>
                                <option> <%=i%> </option>
                                <%}%>

                            </select>
                            <select name="mm">
                                <%
                                    for (int i = 1; i <= 12; i++) {%>
                                <option> <%=i%> </option>
                                <%}%>

                            </select>
                            <select name="dd">
                                <%
                                    for (int i = 1; i <= 31; i++) {%>
                                <option> <%=i%> </option>
                                <%}%>

                            </select>
                        --%>
            
                    <div class="col-md-3"><label for="infrom">From<input type="date" id="infrom" name="from"></label></div>
                    <div class="col-md-3"><label for="into">to<input type="date" id="into" name="to"></label></div>
                    <button type="submit" id="incomingbarbtn" class="btn btn-primary">Filter</button>
                </form>
            </div>
            
            
            
            <div class="clear"></div>
            <div id="outgoing_sms" class="content_title">
                <h3>Outgoing SMS</h3>
                <p>
                    <a href="#activity_summary"><span>&uarr;&nbsp;</span>Activity Summary</a><span>&nbsp;&bull;&nbsp;</span>
                    <a href="#incoming_sms"><span>&uarr;&nbsp;</span>Incoming SMS</a><span>&nbsp;&bull;&nbsp;</span>
                    <a href="#submitted_sms"><span>&darr;&nbsp;</span>Submitted SMS</a><span>&nbsp;&bull;&nbsp;</span>   
                    <a href="#top"><span>&uarr;&nbsp;</span>Top</a>
                </p>
            </div>
            
            <p>
                This is a count of SMS that have been accepted for delivery.
            </p>
            <table class="stats">                            
                <tr>
                    <th>Network</th>
                    <th>SMS Sent</th>                    
                </tr>
                <%
                    Map<Network, Integer> networkOutgoingSMSCount = statistics.getNetworkOutgoingCount();
                    Iterator<Network> outgoingIter = networkOutgoingSMSCount.keySet().iterator();
                                         
                    while (outgoingIter.hasNext()) {                        
                        network = outgoingIter.next();
                        
                %>
                
                    <tr>
                        <td> <%=network.getName()%></td>
                        <td class="count"><%=networkOutgoingSMSCount.get(network)%></td>
                    </tr>
                <%}%>
            </table>

  
               
               <div id="outgoingPieChart" style="width:650px; height:500px;"></div>
           

            <!--<p>
                <img id="outbar" src="outgoingBarDay?accountuuid=<%=URLEncoder.encode(accountuuid, "UTF-8")%>" alt="Outgoing Daily Bar Chart" />
            </p>-->
            <!--outgoing SMS Bar chart-->
            <div id="outgoingSMSBarChart" style="height:400px;width:800px;"></div><br><br>
            <div>
                <form id="outgoingform" action='incomingBarDay' method='post'>
                    <div class="col-md-3"><label for="outfrom">From<input type="date" id="outfrom" name="from"></label></div>
                    <div class="col-md-3"><label for="outto">to<input type="date" id="outto" name="to"></label></div>
                    <button type="submit" id="outgoingpiebtn" class="btn btn-primary" >Filter</button>
                </form>
            </div>
            <br><br>
            
            
            <div class="clear"></div>
            <p class="center">
                <a href="#" class="btn btn-large btn-primary"><i class="icon-chevron-left icon-white"></i> Back</a> 
                <a href="#" class="btn btn-large"><i class="icon-download-alt"></i> Page</a>
            </p>
            <div class="clearfix"></div>
        </div>
    </div>
</div>


<script type="text/javascript" src="../js/jqplot.pieRenderer.min.js"></script>
<script src="../js/jquery.min.js"></script>
<script src="../js/jquery.jqplot.min.js"></script>
<script src="../js/jqplot.pieRenderer.min.js"></script>
<script src="../js/jqplot.barRenderer.min.js"></script>
<script src="../js/jqplot.categoryAxisRenderer.min.js"></script>
<link href="../js/jquery.jqplot.min.css" type="text/css" rel="stylesheet"/>  
    


<script type="text/javascript">
 
    /*
    * Here we draw the Incoming SMS Pie Chart
    * 
    */

    var jsonURL = 'incomingPie?accountuuid=' + '<%= URLEncoder.encode(accountuuid, "UTF-8") %>';

    $.getJSON(jsonURL, function(data) {
        var items1 = new Array();
        var j=0;
        for ( var i in data ) {
            var items = new Array();
            items.push(i,Number(data[i]));
            items1[j++] = items;
        }

        var plot1 = jQuery.jqplot('incomingPieChart', eval([items1]), {
                    seriesDefaults:{
                        // Make this a pie chart.
                        renderer:jQuery.jqplot.PieRenderer,
                        rendererOptions:{
                            // Put data labels on the pie slices.
                            // By default, labels show the percentage of the slice.
                            dataLabels:'value',
                            fill: true,
                            showDataLabels: true,
                            // Add a margin to separate the slices.
                            //sliceMargin: 4,
                            // stroke the slices with a little thicker line.
                            //lineWidth: 5,
                            //showDataLabels:true
                        }
                    },

                    //setting the slices color
                    seriesColors: ["#FFA500","#7BE319","#FF0000"],
                    highlighter: { show: true },
                    legend:{ show:true, location:'e' }
                }
        );
    });


    /*
    * Here we draw the Outgoing SMS Pie Chart
    * 
    */
    var jsonURL = 'outgoingPie?accountuuid=' + '<%= URLEncoder.encode(accountuuid, "UTF-8") %>';

    $.getJSON(jsonURL, function(data) {
        var items1 = new Array();
        var j=0;
        for ( var i in data ) {
            var items = new Array();
            items.push(i,Number(data[i]));
            items1[j++] = items;
        }
        var plot1 = jQuery.jqplot('outgoingPieChart', eval([items1]), {
                    seriesDefaults:{
                        // Make this a pie chart.
                        renderer:jQuery.jqplot.PieRenderer,
                        rendererOptions:{
                            // Put data labels on the pie slices.
                            // By default, labels show the percentage of the slice.
                            dataLabels:'value',
                            fill: true,
                            showDataLabels: true,
                            // Add a margin to seperate the slices.
                            //sliceMargin: 4,
                            // stroke the slices with a little thicker line.
                            //lineWidth: 5,
                            //showDataLabels:true
                        }
                    },
                    //setting the slices color
                    seriesColors: ["#FFA500","#7BE319","#FF0000"],
                    highlighter: {
                        show: true
                    },
                    legend:{ show:true, location:'e' }
                }
        );
    });
   /*
    *
    *Here, we draw the outgoingSMS bar chart
    *
    */
    $('#outgoingform').submit(function(e){
        var jsonURL = 'outgoingBarDay?accountuuid=' + '<%= URLEncoder.encode(accountuuid, "UTF-8") %>';
        $.getJSON(jsonURL, function(data){
           /* var outgoing = data.outgoing;
            var _safaricom = [];
            var _airtel = [];
            var _orange = [];
            var _markers = [];
            for(var i in outgoing){
                console.log(outgoing[i].Safaricom_KE);

                markers.push(outgoing[i].date);
               _safaricom.push(outgoing[i].Safaricom_KE);
                 _airtel.push(outgoing[i].Airtel_KE);
                _orange.push(outgoing[i].Orange_KE);
            }*/
        });
        
        //outgoing data for each network for 7days
        var _safaricom = [100, 120, 85, 69, 56, 96, 115];
        var _airtel = [56, 52, 75, 67, 87, 69, 105];
        var _orange = [103, 37, 60, 22, 17, 10, 56];    
        // put the chart data in form of an array
        var dataArrayOutgoing = [_safaricom, _airtel, _orange];
        
        // x-axis markers (these will be the dates [from:to] essentily, they have to be 7 days)
        var markers = ['Mon', 'Tue', 'Wed', 'Thur','Fri', 'Sat', 'Sun'];
      
        // chart rendering options for outgoing
        var outgoingOptions = {
            stackSeries: 'true',            
            title: 'Outgoing SMS for the last 7 days',
            legend: { 
                show: true,
                location: 'ne',
                placement: 'outsideGrid'    
            },
            //labels for the legend
            series: [
                {label: 'Safaricom'},
                {label: 'Airtel'},
                {label: 'Orange'}
            ],
            //specifying the colors of be used for the bars. In this case, they are custom
            seriesColors: [
                '#6eb43f',
                '#db030c',
                '#ff6600'
            ],
            seriesDefaults: {
                renderer:$.jqplot.BarRenderer
            },
            axes: {
                xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: markers
                }
            }
        };
        $.jqplot('outgoingSMSBarChart', dataArrayOutgoing, outgoingOptions);
        return false;
    });
    /*
    *
    *Here, we draw the outgoingSMS bar chart
    *
    */
    $('#incomingform').submit(function(e){
        var jsonURL = 'incomingBarDay?accountuuid=' + '<%= URLEncoder.encode(accountuuid, "UTF-8") %>';
        $.getJSON(jsonURL, function(data){
           /* var outgoing = data.outgoing;
            var _safaricom = [];
            var _airtel = [];
            var _orange = [];
            var _markers = [];
            for(var i in outgoing){
                console.log(outgoing[i].Safaricom_KE);

                markers.push(outgoing[i].date);
               _safaricom.push(outgoing[i].Safaricom_KE);
                 _airtel.push(outgoing[i].Airtel_KE);
                _orange.push(outgoing[i].Orange_KE);
            }*/
        });
        //incoming data for each network for 7days
        var safaricom = [10, 45, 85, 32, 25, 13, 46];
        var airtel = [2, 15, 6, 45, 87, 25, 17];
        var orange = [1, 37, 14, 9, 27, 5, 56]; 
        // put the chart data in form of an array
        var dataArrayIncoming = [safaricom, airtel, orange];
        // x-axis markers (these will be the dates [from:to] essentily, they have to be 7 days)
        var markers = ['Mon', 'Tue', 'Wed', 'Thur','Fri', 'Sat', 'Sun'];

        //options for chart
        var incomingOptions = {
            //stack the bars on top of each other
            stackSeries: 'true',
            //title for the graph
            title: 'Incoming SMS for the last 7 days',
            //declare properties for the legend
            legend: { 
                show: true,
                location: 'ne',
                placement: 'outsideGrid'    
            },
            //labels for the legend
            series: [
                {label: 'Safaricom'},
                {label: 'Airtel'},
                {label: 'Orange'}
            ],
            //specifying the colors of be used for the bars. In this case, they are custom
            seriesColors: [
                '#6eb43f',
                '#db030c',
                '#ff6600'
            ],
            seriesDefaults: {
                renderer:$.jqplot.BarRenderer
            },
            axes: {
                xaxis: {
                renderer: $.jqplot.CategoryAxisRenderer,
                ticks: markers
                }
            }
        };
        // draw the charts
        $.jqplot('incomingSMSBarChart', dataArrayIncoming, incomingOptions);
        return false;
    });
        
        
    //}
</script>



<jsp:include page="footer.jsp" />
