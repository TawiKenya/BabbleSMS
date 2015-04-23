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
        <!--<div class="box-header well">
            <h2><i class="icon-info-sign"></i>welcome</h2>
            <div class="box-icon">
                <a href="#" class="btn btn-setting btn-round"><i class="icon-cog"></i></a>
                <a href="#" class="btn btn-minimize btn-round"><i class="icon-chevron-up"></i></a>
                <a href="#" class="btn btn-close btn-round"><i class="icon-remove"></i></a>
            </div>
        </div>-->
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

      
           
            <div id="chart1" style="width:650px; height:500px;"></div>
            <div style="width:500px; height:300px;" id="mobile"></div>
            
            <p>
                &nbsp;&nbsp;&nbsp;
            </p>
            
            <p>
                <img id="inbar" 
                     src="incomingBarDay?accountuuid=<%=URLEncoder.encode(accountuuid, "UTF-8")%>&from=03/04/2015&to=03/21/2015" 
                     alt="Incoming Daily Bar Chart" />
            </p>
            
           
            
            <div>
                <form id="incomingform">
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
            
                    <div class="col-md-3"><label for="infrom">From<input type="text" id="infrom" name="from"></label></div>
                    <div class="col-md-3"><label for="into">to<input type="text" id="into" name="to"></label></div>
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

  
               
               <div id="chart2" style="width:650px; height:500px;"></div>
           

            
            <div>
                <form id="outgoingform">
                    <div class="col-md-3"><label for="outfrom">From<input type="text" id="outfrom" name="from"></label></div>
                    <div class="col-md-3"><label for="outto">to<input type="text" id="outto" name="to"></label></div>
                    <button type="submit" id="outgoingpiebtn" class="btn btn-primary">Filter</button>
                </form>
            </div>
            
            <p>
                <img id="outbar" src="outgoingBarDay?accountuuid=<%=URLEncoder.encode(accountuuid, "UTF-8")%>" alt="Outgoing Daily Bar Chart" />
            </p>
            
            <div class="clear"></div>
            <p class="center">
                <a href="#" class="btn btn-large btn-primary"><i class="icon-chevron-left icon-white"></i> Back</a> 
                <a href="#" class="btn btn-large"><i class="icon-download-alt"></i> Page</a>
            </p>
            <div class="clearfix"></div>
        </div>
    </div>
</div>

<div class="row-fluid sortable">
    <!--/span-->

    <!--/span-->

    <!--/span-->
</div><!--/row-->

<!--/row-->


<script type="text/javascript" src="../js/jqplot.pieRenderer.min.js"></script>
<script src="../js/jquery.min.js"></script>
<script src="../js/jquery.jqplot.min.js"></script>
<script src="../js/jqplot.pieRenderer.min.js"></script>
<link href="../js/query.jqplot.min.css" type="text/css" rel="stylesheet"/>  
    
<script>        
<script class="code" type="text/javascript">
$(document).ready(function(){
  var data = [
    ['Heavy Industry', 12],['Retail', 9], ['Light Industry', 14], 
    ['Out of home', 16],['Commuting', 7], ['Orientation', 9]
  ];
  var plot1 = jQuery.jqplot ('chart1', [data], 
    { 
      seriesDefaults: {
        // Make this a pie chart.
        renderer: jQuery.jqplot.PieRenderer, 
        rendererOptions: {
          // Put data labels on the pie slices.
          // By default, labels show the percentage of the slice.
          showDataLabels: true
        }
      }, 
      legend: { show:true, location: 'e' }
    }
  );
});
</script>



<script language="javascript">
    <% String message="outputs";%>
    var msg='<%out.print("accountuuid=" + URLEncoder.encode(accountuuid, "UTF-8"));%>';

    var msg2 ='<%out.print("accountuuid=" + URLEncoder.encode(accountuuid, "UTF-8"));%>';
</script>


<script type="text/javascript">
 

$(document).ready(function(){
    var line1 = [['Nissan', 4],['Porche', 6],['Acura', 2],['Aston Martin', 5],['Rolls Royce', 6]];

  //var line1 = FETCH_BARDATA.downloadData();
 
    $('#chart4').jqplot([line1], {
        title:'Bar Chart with Varying Colors',
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: { show: true },
            rendererOptions: {
                // Set the varyBarColor option to true to use different colors for each bar.
                // The default series colors are used.
            
                varyBarColor: true
            }
        },
        axes:{
            xaxis:{
                renderer: $.jqplot.CategoryAxisRenderer
                
            }
        }
    });
    var data = [
    ['Android', 75],['iOS', 15],['Windows Phone', 2], 
    ['BlackBerry', 4],['Others', 4]
  ];
  var plot1 = jQuery.jqplot ('mobile', [data], 
    { 
      seriesDefaults: {
        // Make this a pie chart.
        renderer: jQuery.jqplot.PieRenderer, 
        rendererOptions: {
          // Put data labels on the pie slices.
          // By default, labels show the percentage of the slice.
          showDataLabels: true
        }
      }, 
      legend: { show:true, location: 'e' }
    }
  );
});


</script>

<script type="text/javascript">
 var FETCH_BARDATA ={
        
        /*Makes Ajax calls to Servlet to download student Data*/
        downloadData:function() {
             
            //alert(msg);
            
            var formattedListArray =[];
            
                $.ajax({
                    
                  async: false,
                  
                  url: "StudentJsonDataServlet",
                  
                  dataType:"json",
                  
                  success: function(JsonData) {
                    
                    $.each(JsonData,function(index,aData){
                        
                        //formattedListArray.push([aData.mathematicsMark,aData.computerMark]);
                                                  formattedListArray.push([aData.name,aData.count]);
                    });
                  }
                });
            return formattedListArray;
        },
        
};
	    var urls = 'incomingPie?'+msg
            //alert(urls);
$.getJSON(urls, function(data) {
        var items1 = new Array();
        var j=0;
        for ( var i in data ) {
            var items = new Array();
            items.push(i,Number(data[i]));
            items1[j++] = items;
        }
        var plot1 = jQuery.jqplot('chart1', eval([items1]), {
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
var urls2 = 'outgoingPie?'+msg2
            //alert(urls2);
$.getJSON(urls2, function(data) {
        var items1 = new Array();
        var j=0;
        for ( var i in data ) {
            var items = new Array();
            items.push(i,Number(data[i]));
            items1[j++] = items;
        }
        var plot1 = jQuery.jqplot('chart2', eval([items1]), {
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
$.getJSON('StudentJsonDataServlet', function(data) {
        var items1 = new Array();
        var j=0;
        for ( var i in data ) {
            var items = new Array();
            items.push(i,Number(data[i]));
            items1[j++] = items;
        }
        var plot1 = jQuery.jqplot('chart5', eval([items1]), {
            seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            //pointLabels: { show: true, location: 'e', edgeTolerance: -15 },
            rendererOptions: {
                 //pointLabels:'value',
                // Set the varyBarColor option to true to use different colors for each bar.
                // The default series colors are used.
            
                varyBarColor: true
            }
        },
       
        seriesColors: ["#7BE319", "#FF0000", "#FFA500"],
                    highlighter: {
                        show: true
                    },
            legend: {
                //show: true,
                location: 'e',
                placement: 'outside'
            },
        axes:{
            xaxis:{
                renderer: $.jqplot.CategoryAxisRenderer
               
                
            }
        }
                    
                    
                }
        );
        /**
        $('chart2').bind('jqplotDataRightClick',
            function (ev, seriesIndex, pointIndex, data) {
                $('#info3').html('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
            }
        ); **/
    });
/**
 $(document).ready(function(){
        $.jqplot.config.enablePlugins = true;
        var s1 = [2, 6, 7];
        var formatStudentData =  FETCH_BARDATA.downloadData();
        var ticks = ['safaricom', 'airtel'];
        //alert(JSON.stringify(formatStudentData));
         
        plot1 = $.jqplot('chart1', formatStudentData, {
            // Only animate if we're not using excanvas (not in IE 7 or IE 8)..
            animate: !$.jqplot.use_excanvas,
            seriesDefaults:{
                renderer:$.jqplot.BarRenderer,
                pointLabels: { show: true }
            },
            axes: {
                xaxis: {
                    renderer: $.jqplot.CategoryAxisRenderer,
                    ticks: ticks
                }
            },
            highlighter: { show: false }
        });
     
        $('chart1').bind('jqplotDataClick',
            function (ev, seriesIndex, pointIndex, data) {
                $('#info1').html('series: '+seriesIndex+', point: '+pointIndex+', data: '+data);
            }
        );
    });**/
$(document).ready(function(){
    var line1 = [['Nissan', 4],['Porche', 6],['Acura', 2],['Aston Martin', 5],['Rolls Royce', 6]];
  //var line1 = FETCH_BARDATA.downloadData();
 
    $('#chart4').jqplot([line1], {
        title:'Bar Chart with Varying Colors',
        seriesDefaults:{
            renderer:$.jqplot.BarRenderer,
            pointLabels: { show: true },
            rendererOptions: {
                // Set the varyBarColor option to true to use different colors for each bar.
                // The default series colors are used.
            
                varyBarColor: true
            }
        },
        axes:{
            xaxis:{
                renderer: $.jqplot.CategoryAxisRenderer
                
            }
        }
    });
});
$(document).ready(function(){
 var line1 = FETCH_BARDATA.downloadData();
  
  var plot2 = jQuery.jqplot ('chart4', [line1],{
      seriesDefaults: {
        //renderer: jQuery.jqplot.PieRenderer,
         renderer:$.jqplot.PieRenderer,
        rendererOptions: {
          // Turn off filling of slices.
          fill: false,
          showDataLabels: true,
          // Add a margin to seperate the slices.
          sliceMargin: 4,
          // stroke the slices with a little thicker line.
          lineWidth: 5
        }
      },
      legend: { show:true, location: 'e' }
    }
  );
});
</script>

<jsp:include page="footer.jsp" />
