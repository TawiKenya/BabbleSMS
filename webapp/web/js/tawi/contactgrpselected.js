/*
  Copyright (c) 2015, Tawi Commercial Services Ltd. All rights reserved.
  Licensed under the OSL-3.0 License:
  http://opensource.org/licenses/OSL-3.0

  Author: Migwi Ndung'u  <migwi@tawi.mobi>
*/
var count=0;
var objt=null;
var pageNo=1;
var total1=0;
var grpname=null;
var grpselect;

//gets the total contact count json before the page fully loads
   sendRequest2();


//used by chrome or chromium  browsers
function Chromecheck(){
	$.browser.chrome = /chrom(e|ium)/.test(navigator.userAgent.toLowerCase());
  if($.browser.chrome){
    count=0;
    pageNo=Math.floor(count/10)+1;
     executevent(count);
	    }
  }

//done after the page fully loads
$(document).ready(function(){ 

//gets first page of a given group contacts
	$('.grp').on('click',function(){
    count=0;
    pageNo=Math.floor(count/10)+1;
    executevent(count);
    calcPageNo();
		});

//gets the default page after the page has been fully loaded   
   executevent(count);
   calcPageNo();

//get the next page
   $('#next').on('click',function(){

     var it=0;
     if(it=0){sendRequest2(); it++;}

    if( pageNo<(total1) ){
       count=count+10;
       pageNo=Math.floor(count/10)+1;       
       executevent(count);
        calcPageNo();
        }    
      });

//gets the previous page if it available
   $('#prev').on('click',function(){

     var it=0;
     if(it=0){sendRequest2(); it++;}    
    
    if(count>0){         
         count=count-10;
         pageNo=Math.floor(count/10)+1;        
         executevent(count);
         calcPageNo();
         }           
      }); 

});


function executevent(count){  
	  grpselect=$('.groupselect option:selected').val();		
		grpname=$('.groupselect option:selected').attr('name');
		var nwkname=$('.networkselect option:selected').attr('name');
		$('#header-display').html("<h3>Displaying "+grpname+" contacts </h3>");
    var str= "allnetworks.jsp?grp="+grpselect+"&count="+count;
    //alert(str);
    sendRequest1(str);		
}

//make ajax call to various files
function sendRequest1(str){         
           var request = getRequestObject1();
           request.onreadystatechange = function() { handleRespons(request); };
           request.open("GET",str , true);
           request.send();
        }
        
    function getRequestObject1() {
          if (window.XMLHttpRequest) {
             return(new XMLHttpRequest());
             } 

             else if (window.ActiveXObject) {
               return(new ActiveXObject("Microsoft.XMLHTTP"));
             } 
             else if(window.createRequest){             
              return(window.createRequest());
             }
             else {
             return(null); 
             }
           }
     

     function handleRespons(request) {     	
           if ((request.readyState == 4)&&(request.status==200)) {
           	//alert(request.responseText);  
             $('#contactgrp').remove();        	
            $('#header-display').after(request.responseText);
             }
         }
         
    //make ajax call to get the total contacts per group
       function sendRequest2(){  
         if(objt===null){ 
          var Accuuid=$('#prev').attr('name');          
           var data = "accountuuid=" + escape(Accuuid);      
           var request = getRequestObject1();
           request.onreadystatechange =function() { handleTotalCon(request); };
           request.open("POST", "getGroups", true);
           request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
           request.send(data); 
           }
        }

       //handles the json with the total contacts per group
        function handleTotalCon(request) {       
           if ((request.readyState == 4)&&(request.status==200)) {
                objt= JSON.parse(request.responseText);
                //console.log(objt);             
             }
         }

         //puts the page no that is currently on display
          function calcPageNo(){
            if(objt!==null){ 
             $('#totalcons').remove();                 
             var allCons =objt[grpname]["Total contacts"];            
             total1 = Math.floor(allCons/10)+1;
             var $result = $('<span id="totalcons">Page <b>'+pageNo+'</b> of <b>'+total1+'</b> Page(s)</span>');           
            $('#prev').append($result); 
            }                     
         }
        
         

