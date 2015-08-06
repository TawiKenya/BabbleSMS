/*
  Copyright (c) 2015, Tawi Commercial Services Ltd. All rights reserved.
  Licensed under the OSL-3.0 License:
  http://opensource.org/licenses/OSL-3.0

  Author: Migwi Ndung'u  <migwi@tawi.mobi>
*/
$(document).ready(function(){

  //Sample JSON input {  "In Transit": 8,  "Received": 8,  "Rejected": 8,  "Failure": 0,  "Sent": 8}"
 
  var transit = "In Transit";
  var recieved = "Received";
  var rejected = "Rejected";
  var failed ="Failure";
  var sent= "Sent";
  var $image=$('<div id="loadimg"><img src="../img/loader.gif" alter="Loading contacts.."></img></div>');
  var jsson = null;
   var $scroll=$('#scroll21');     


       //responds if element 'a' in class 'alink' is clicked
  $('a.msglink').click(function(){  

   var SentGroupUuid = $(this).parent().siblings().last().attr('name');
   jsson = $(this).text();
   //console.log(SentGroupUuid);          
    
   $scroll.hide();
   $('.modal-dialog').append($image);   

  
  var data = "SentGroupUuid=" + escape(SentGroupUuid);
    //console.log(data);

     //depending on a users browser a request object is created
     function getRequestObject() {
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
             return(null); 
             alert("Your current browser failed, try Mozilla or chrome browsers");
             }
           }
    
      var request=getRequestObject();
      request.onreadystatechange =function() { handleResponse(request); };
       request.open("POST", "getSentGroupMessages", true);
       request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
       request.send(data); 

    });

function handleResponse(request) {
      if ((request.readyState == 4) && (request.status == 200)) {                        
                 $('#displaycontacts').remove();
                 var obj = JSON.parse(request.responseText);
                 //console.log(obj);
                     var $tablelem = $('<table id="displaycontacts" class="table table-striped table-bordered">'+
                  '<tr class="center"><td width="50%">'+transit+'</td><td width="50%">'+obj[transit]+' contact(s)</td></tr>'+ 
                  '<tr class="center"><td width="50%">'+recieved+'</td><td width="50%">'+obj[recieved]+' contact(s)</td></tr>'+  
                  '<tr class="center"><td width="50%">'+rejected+'</td><td width="50%">'+obj[rejected]+' contact(s)</td></tr>'+  
                  '<tr class="center"><td width="50%">'+sent+'</td><td width="50%">'+obj[sent]+' contact(s)</td></tr>'+  
                  '<tr class="center"><td width="50%">'+failed+'</td><td width="50%">'+obj[failed]+' contact(s)</td></tr>'+          
                  '</table>');

                  $scroll.show();
                 $('.par').remove();
                $image.remove();
                $('#modal-display1').html(jsson+' Group Sent Message Details.');                
                $('.modal-dialog').append($tablelem);
               
          }


      //if it fails do this....
          else if((request.readyState==0)&&(request.status=404)){              
                $image.remove();
                 $('#modal-display1').html('could not load details of '+jsson);
                 $('.modal-dialog').html('<p class="par"><font color="red">An Error has occurred!!</font></p>');          
          }
       }

});