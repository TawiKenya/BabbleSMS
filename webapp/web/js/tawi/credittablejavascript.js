/*
  Copyright (c) 2015, Tawi Commercial Services Ltd. All rights reserved.
  Licensed under the OSL-3.0 License:
  http://opensource.org/licenses/OSL-3.0

  Author: Migwi Ndung'u  <migwi@tawi.mobi>
*/

function networkselect(val){	
//Name of the selected network service provider
var networkprovider=val.label;

//changes network provider color according to the item clicked
if(networkprovider=='Safaricom KE') {
$("#Safaricom").html('<td><font color=green>Safaricom</font></td>');
	$("#Orange").html('<td>Orange</td>');
	$("#Yu").html('<td>Yu</td>');
	$("#Airtel").html('<td>Airtel</td>');
	     }
	
else if (networkprovider=='Yu KE') {  	
  	$("#Yu").html('<td><font color=blue>Yu</font></td>');
	$("#Safaricom").html('<td>Safaricom</td>');
	$("#Orange").html('<td>Orange</td>');
	$("#Airtel").html('<td>Airtel</td>');
	     }
	
else if(networkprovider=='Airtel KE'){
	$("#Airtel").html('<td><font color=red>Airtel</font></td>');
	$("#Safaricom").html('<td>Safaricom</td>');
	$("#Orange").html('<td>Orange</td>');
	$("#Yu").html('<td>Yu</td>');
	     }
	
else if (networkprovider=='Orange KE'){
	$("#Orange").html('<td><font color=orange>Orange</font></td>');
	$("#Safaricom").html('<td>Safaricom</td>');
	$("#Yu").html('<td>Yu</td>');
	$("#Airtel").html('<td>Airtel</td>');
        }
}

var response=null;
var saf=0;
var orange=0;
var yu=0;
var airtel=0;

function getcount(val){	
	
	var $item=$(val).parent().parent().siblings();
	var Accuuid = $item.attr('name');
    var jsson;
    var select=$item.html();    
    var xreq;     
    
    //assigned once if click only once n assigned repeat if it is clicked for the second time
	if($(val).is(":checked")){		
	    jsson='once';		        	
	}else{		
	    jsson='repeat';		   
	}	
       
       if(response===null){
	var data = "accountuuid=" + escape(Accuuid);

	
      if (window.XMLHttpRequest) {
      xreq=new XMLHttpRequest();//for modern browsers, i.e. Opera,Mozilla, chrome e.t.c.
             } 

      else if (window.ActiveXObject) {
      xreq=new ActiveXObject("Microsoft.XMLHTTP"); //for internet explorer
             } 

      else if(window.createRequest){             
      xreq=window.createRequest();// for crystal browser
             }

      else { xreq=null; //the browser failed to create a reques object
      alert("Your current browser failed, try Mozilla or chrome browsers");
             }  

     xreq.onreadystatechange= function(){Handleresp(xreq);};                
     xreq.open("POST", "getGroups", true);
     xreq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
     xreq.send(data); 
        }
       

       else{
          if(jsson==='once'){          	
                 saf= saf + response[select]["Safaricom KE"];
                 orange= orange + response[select]["Orange KE"];
                 yu=yu + response[select]["Yu KE"];
                 airtel=airtel + response[select]["Airtel KE"];

            //alert("2 once Safaricom KE:"+saf+"  Orange KE:"+orange+"  Yu KE:"+yu+"   Airtel KE:"+airtel);

          }else if(jsson==='repeat'){          	
                 saf= saf - response[select]["Safaricom KE"];
                 orange= orange - response[select]["Orange KE"];
                 yu=yu - response[select]["Yu KE"];
                 airtel=airtel - response[select]["Airtel KE"];

           // alert("2 repeat Safaricom KE:"+saf+"  Orange KE:"+orange+"  Yu KE:"+yu+"   Airtel KE:"+airtel);

          }else{ alert("An error has occurred");}
             
           jsson=null;
          }
          
       
        function Handleresp(xreq){        	
     if((xreq.readyState==4) && (xreq.status==200)){      
     //console.log(xreq.responseText);              
       var obj=JSON.parse(xreq.responseText); 
       response=obj;   

     if(jsson==='once'){          	
              saf= saf + response[select]["Safaricom KE"];
              orange= orange + response[select]["Orange KE"];
              yu=yu + response[select]["Yu KE"];
              airtel=airtel + response[select]["Airtel KE"];

        //alert("1 once Safaricom KE:"+saf+"  Orange KE:"+orange+"  Yu KE:"+yu+"   Airtel KE:"+airtel);

          }else if(jsson==='repeat'){          
              saf= saf - response[select]["Safaricom KE"];
              orange= orange - response[select]["Orange KE"];
              yu=yu - response[select]["Yu KE"];
               airtel=airtel - response[select]["Airtel KE"];

        //alert("1 repeat Safaricom KE:"+saf+"  Orange KE:"+orange+"  Yu KE:"+yu+"   Airtel KE:"+airtel);

          }else{ alert("An error has occurred");}      

     jsson=null;                                         
              }
          }
          


       }
        //return the current safaricom contacts count
       function safGroupCount(){
       	return saf;
       }
        //return the current orange contacts count
       function orangeGroupCount(){
       	return orange;
       }
         //return the current yu contacts count
       function yuGroupCount(){
       	return yu;
       }
         //return the current airtel contacts count
       function airtelGroupCount(){
       	return airtel;
       }