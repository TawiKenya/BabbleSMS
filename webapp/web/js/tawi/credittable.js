/*
  Copyright (c) 2015,
  Tawi Commercial Services Ltd. All rights reserved.
  Licensed under the OSL-3.0 License:
  http://opensource.org/licenses/OSL-3.0

  Author: Migwi Ndung'u  <migwi@tawi.mobi>
*/

   //global variables
   var jssonBalance=null;
   var response=null;
   var saf="";
   var safContact="";
   var orange="";
   var orangeContact="";
   var yu="";
   var yuContact="";
   var airtel="";
   var airtelContact="";
  

function networkselect(val){	
         //Name of the selected network service provider
       var networkprovider=val.label;

       //gets the selected individual contact       
       getContactNetworkcount();
     
          //maskuuid or shortcodeuuid of the source to be used
       var sourceUuid = val.value;
          //gets current balance of a given mask or shortcode
          if(jssonBalance===null){
            currentBalance="<font font-size=8px color=red><i><u>No Destination!!</u></i></font>";
          }
          else{  var currentBalance=jssonBalance[sourceUuid];
          }
        
          //changes network provider color according to the item clicked
      if(networkprovider==='Safaricom KE') {
          creditConsumed("<font color=green>Safaricom</font>","Orange","Yu","Airtel");
          creditBalance("<font color=green>"+currentBalance+"</font>","","","");
	     }
	
      else if (networkprovider==='Yu KE') {  
          creditConsumed("Safaricom","Orange","<font color=blue>Yu</font>","Airtel");	
          creditBalance("","","<font color=blue>"+currentBalance+"</font>","");  	
	     }
	
      else if(networkprovider==='Airtel KE'){
         creditConsumed("Safaricom","Orange","Yu","<font color=red>Airtel</font>");
         creditBalance("","","","<font color=red>"+currentBalance+"</font>");	
	     }
	
      else if (networkprovider==='Orange KE'){
          creditConsumed("Safaricom","<font color=orange>Orange</font>","Yu","Airtel");	
          creditBalance("","<font color=orange>"+currentBalance+"</font>","","");
        }
    }

   function creditConsumed(safcom1,orange1,yu1,airtel1){
         $("#Safaricom").html('<td>'+safcom1+'</td>');
         $("#Orange").html('<td>'+orange1+'</td>');
         $("#Yu").html('<td>'+yu1+'</td>');
         $("#Airtel").html('<td>'+airtel1+'</td>');
         }   


   function creditBalance(safcom3,orange3,yu3,airtel3){
        $("#safcreditbalance").html('<td>'+safcom3+'</td>');
        $("#orangecreditbalance").html('<td>'+orange3+'</td>');
        $("#yucreditbalance").html('<td>'+yu3+'</td>');
        $("#airtelcreditbalance").html('<td>'+airtel3+'</td>');
   }

function getCreditBalance(called){
  //dont get the jssonBalance again if it already exists
      if(jssonBalance===null){
  var accountUuid = called.name;
  //alert(" its called :"+accountUuid);
  var data = "accountuuid=" + escape(accountUuid);  
     xreq=getRequestObject11();
     xreq.onreadystatechange= function(){HandleBalance(xreq);};                
     xreq.open("POST", "getBalance", true);
     xreq.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
     xreq.send(data);
       }
    }

function HandleBalance(balance){
 if((balance.readyState===4) && (balance.status===200)){
     jssonBalance=JSON.parse(balance.responseText);     
   }
}


function getRequestObject11() {
          if (window.XMLHttpRequest) {
             return(new XMLHttpRequest());//for modern browsers, i.e. Opera,Mozilla, chrome e.t.c.
             } 

             else if (window.ActiveXObject) {
               return(new ActiveXObject("Microsoft.XMLHTTP")); //for internet explorer
             } 
             else if(window.createRequest){             
              return(window.createRequest());// for crystal browser
             }
             else {
             return(null);//the browser failed to create a reques object
             alert("Your current browser failed, try Mozilla or chrome browsers");
             }
           }  

function getcount(val){	
	
	var $item=$(val).parent().parent().siblings();
	var Accuuid = $item.attr('name');
    var jsson;
    var select=$item.html();    
    var xreq;     
    
    //assigned 'once' if clicked only once and assigned 'repeat' if it is clicked for the second time
	if($(val).is(":checked")){		
	    jsson='once';		        	
	}else{		
	    jsson='repeat';		   
	}	
       
       if(response===null){
	var data = "accountuuid=" + escape(Accuuid);  
     xreq=getRequestObject11();
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
     if((xreq.readyState===4) && (xreq.status===200)){      
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
       	return saf + safContact;
       }
        //return the current orange contacts count
       function orangeGroupCount(){          
       	return orange + orangeContact;
       }
         //return the current yu contacts count
       function yuGroupCount(){        
       	return yu + yuContact;
       }
         //return the current airtel contacts count
       function airtelGroupCount(){        
       	return airtel + airtelContact;
       }


//calculate the network count of the individual contacts selected
        function getContactNetworkcount(){
    //ensure every time the function is called all the previous counts are lost
          orangeContact =0;
          safContact=0;
          yuContact =0;
          airtelContact = 0;

            $elem = $('#tokenize_simple.tokenize-sample.controls option:selected');
       for (var i = 0; i < $elem.length; i++) {
        //get count of individual safaricom contacts
                  if($elem.eq(i).attr('network')==="Safaricom KE"){
                    safContact= safContact+1;
                  }
        //get count of individual orange contacts
             else if($elem.eq(i).attr('network')==="Orange KE"){
              orangeContact =orangeContact +1;
             }
        //get count of individual Yu contacts
             else if($elem.eq(i).attr('network')==="Yu KE"){
              yuContact = yuContact +1;
             }
        //get count of individual airtel contacts
             else if($elem.eq(i).attr('network')==="Airtel KE"){
              airtelContact =airtelContact +1 ;
             }

              };
             
        }

