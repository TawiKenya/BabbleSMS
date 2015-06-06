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
        var jsonURL = 'outgoingBarDay?accountuuid=' + '<%= URLEncoder.encode(accountuuid, "UTF-8") %>';
        //outgoing data for each network for 7days
        var _safaricom = [];
        var _airtel = [];
        var _orange = []; 
        //x-axis markers (these will be the dates [from:to] essentily, they have to be 7 days)
        var _markers = [];

        $.getJSON(jsonURL, function(data){ 

            for (var i = 0; i < data.outgoingData.length; i++) {
                //add data to the arrays but first check if some networks values are unavailable
                if(typeof data.outgoingData[i].airtel_ke === 'undefined')
                    _airtel.push(0);
                else
                    _airtel.push(data.outgoingData[i].airtel_ke);
                if(typeof data.outgoingData[i].orange_ke === 'undefined')
                    _orange.push(0);
                else
                    _orange.push(data.outgoingData[i].orange_ke);
                if(typeof data.outgoingData[i].safaricom_ke === 'undefined')
                    _safaricom.push(0);
                else
                    _safaricom.push(data.outgoingData[i].safaricom_ke);

                //the dates to be used as markers for the graph
                _markers.push(data.outgoingData[i].date);
            }
            // put the chart data in form of an array
            var dataArrayOutgoing = [_safaricom, _airtel, _orange];
            // chart rendering options for outgoing
            var outgoingOptions = {
                stackSeries: 'true',
                title: 'Outgoing SMS for the last ' + data.outgoingData.length + ' days',
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
                    renderer:$.jqplot.BarRenderer,
                    rendererOptions:{barMargin: 25},
                    pointLabels:{show:true, stackedValue: true}
                },
                axes: {
                    xaxis: {
                        renderer: $.jqplot.CategoryAxisRenderer,
                        ticks: _markers,
                    }
                }
            };
            //draw the chart
            $.jqplot('outgoingSMSBarChart', dataArrayOutgoing, outgoingOptions);
        });

    /*
    *
    *Here, we draw the outgoingSMS bar chart
    *
    */
    //$('#incomingform').submit(function(e){
        var jsonURL = 'incomingBarDay?accountuuid=' + '<%= URLEncoder.encode(accountuuid, "UTF-8") %>';
        //outgoing data for each network for 7days
        var safaricom = [];
        var airtel = [];
        var orange = []; 
        //x-axis markers (these will be the dates [from:to] essentily, they have to be 7 days)
        var markers = [];
        
        $.getJSON(jsonURL, function(data){
            for (var i = 0; i < data.incomingData.length; i++) {
                 //add data to the arrays but first check if some networks values are unavailable
                if(typeof data.incomingData[i].airtel_ke === 'undefined')
                    airtel.push(0);
                else
                    airtel.push(data.incomingData[i].airtel_ke);
                if(typeof data.incomingData[i].orange_ke === 'undefined')
                    orange.push(0);
                else
                    orange.push(data.incomingData[i].orange_ke);
                if(typeof data.incomingData[i].safaricom_ke === 'undefined')
                    safaricom.push(0);
                else
                    safaricom.push(data.incomingData[i].safaricom_ke);

                //the dates to be used as markers for the graph
               markers.push(data.incomingData[i].date);
            }
             
            // put the chart data in form of an array
            var dataArrayIncoming = [safaricom, airtel, orange];
            //options for chart
            var incomingOptions = {
                //stack the bars on top of each other
                stackSeries: 'true',
                //title for the graph
                title: 'Incoming SMS for the last ' + data.incomingData.length + ' days',
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
                    renderer:$.jqplot.BarRenderer,
                    rendererOptions:{barMargin: 25},
                    pointLabels:{show:true, stackedValue: true}
                },
                axes: {
                    xaxis: {
                        renderer: $.jqplot.CategoryAxisRenderer,
                        ticks: markers,
                    }
                }                                  
            };
            // draw the chart
            $.jqplot('incomingSMSBarChart', dataArrayIncoming, incomingOptions);
        });