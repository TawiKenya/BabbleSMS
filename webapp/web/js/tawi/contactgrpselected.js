/*
  Copyright (c) 2015, Tawi Commercial Services Ltd. All rights reserved.
  Licensed under the OSL-3.0 License:
  http://opensource.org/licenses/OSL-3.0

  Author: Migwi Ndung'u  <migwi@tawi.mobi>
*/
var count=null;
var objt=null;
var pageSize=0;
var pageNum=0;
var grpname=null;
var grpselect='empty';


//used by chrome or chromium  browsers
function Chromecheck(){
	$.browser.chrome = /chrom(e|ium)/.test(navigator.userAgent.toLowerCase());
  if($.browser.chrome){
    grpselect=$('.groupselect option:selected').val();
    count='first';    
    executevent(grpselect,count);
	    }
  }

//done after the page fully loads
$(document).ready(function(){ 

//gets first page of a given group contacts
	$('.grp').on('click',function(){
    grpselect=$('.groupselect option:selected').val();
    count='first';    
    executevent(grpselect,count);    
		});

//gets the default page after the page has been fully loaded   
   executevent(grpselect,'first');
   

//get the next page
   $('#next').on('click',function(){
     pageNum = $('.pgNum').html();
     pageSize=$('.pgSize').html();    
    if( pageNum<pageSize){             
       executevent(grpselect,'next');            
       }    
      });

//gets the previous page if it available
   $('#prev').on('click',function(){      
     pageNum = $('.pgNum').html();    
    if(pageNum>1){               
         executevent(grpselect,'prev');         
         }           
      }); 

   //gets the last page
   $('#last').on('click',function(event){
    event.preventDefault();
    pageNum = $('.pgNum').html();
     pageSize=$('.pgSize').html(); 
     if(pageNum<pageSize){
      executevent(grpselect,'last');
     }

     //gets the first page
     $('#first').on('click',function(event){
      event.preventDefault();
      pageNum = $('.pgNum').html();
       if(pageNum >1){
        executevent(grpselect,'first');
       }
     });

   });

});


function executevent(uuid,count){ 
    var str= "allnetworks.jsp?uuid="+uuid+"&first="+count;
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
             $('#ContactsAdd').remove();        	
            $('#header-display').after(request.responseText);
             }
         }
    