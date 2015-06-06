$(document).ready(function(){

  //"Orange KE":"2 contact(s)","Yu KE":"0 contact(s)","Safaricom KE":"1 contact(s)","Airtel KE":"2 contact(s)","Total contacts":"5 contact(s)"
  var response = null;

  var saf = "Safaricom KE";
  var orange = "Orange KE";
  var yu = "Yu KE";
  var airtel ="Airtel KE";
  var total= "Total contacts";
  var $image=$('<div id="loadimg"><img src="../img/loader.gif" alter="Loading contacts.."></img></div>');
  var jsson = null;
  var $scroll=$('#scroll21');


       //responds if element 'a' in class 'alink' is clicked
  $('a.alink').click(function(){  

   var Accuuid = $(this).attr('name');
   var address = $(this).attr('address');

       jsson=$(this).text(); 
   
    
   $scroll.hide();
   $('.modal-dialog').append($image);

   //console.log('Account uuid:'+Accuuid+'        address: '+address+'      Element: '+jsson);

   //if response has not been cached do this..... 
  if(response===null){ 

  var data = "uuid=" + escape(Accuuid);
    console.log(data);

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
       request.open("POST", address, true);
       request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
       request.send(data); 
 }
//end if

//reponse has been cached....
else{
     var obj = JSON.parse(response); 
    //console.log(obj);             
      var $tablelem = $('<table id="displaycontacts" class="table table-striped table-bordered">'+
                  '<tr><td width="50%">'+saf+'</td><td width="50%">'+obj[jsson][saf]+'</td></tr>'+ 
                  '<tr><td width="50%">'+orange+'</td><td width="50%">'+obj[jsson][orange]+'</td></tr>'+  
                  '<tr><td width="50%">'+yu+'</td><td width="50%">'+obj[jsson][yu]+'</td></tr>'+  
                  '<tr><td width="50%">'+airtel+'</td><td width="50%">'+obj[jsson][airtel]+'</td></tr>'+  
                  '<tr><td width="50%">'+total+'</td><td width="50%">'+obj[jsson][total]+'</td></tr>'+          
                  '</table>');

   $scroll.show();
   $('.par').remove();
   $image.remove();

   $('#modal-display1').html(jsson+' group details.');   
  $('.modal-dialog').append($tablelem);
  $('#displaycontacts:last').remove();
             }
  });
//end of click function



function handleResponse(request) {
//if it succeeds do this....
//alert('readyState: '+request.readyState);
      if ((request.readyState == 4) && (request.status == 200)) {    
               
                //gets responseText
                 response=request.responseText;             

                 var obj = JSON.parse(response);
                 //console.log(obj);
                     var $tablelem = $('<table id="displaycontacts" class="table table-striped table-bordered">'+
                  '<tr><td width="50%">'+saf+'</td><td width="50%">'+obj[jsson][saf]+'</td></tr>'+ 
                  '<tr><td width="50%">'+orange+'</td><td width="50%">'+obj[jsson][orange]+'</td></tr>'+  
                  '<tr><td width="50%">'+yu+'</td><td width="50%">'+obj[jsson][yu]+'</td></tr>'+  
                  '<tr><td width="50%">'+airtel+'</td><td width="50%">'+obj[jsson][airtel]+'</td></tr>'+  
                  '<tr><td width="50%">'+total+'</td><td width="50%">'+obj[jsson][total]+'</td></tr>'+          
                  '</table>');

                  $scroll.show();
                 $('.par').remove();
                $image.remove();
                $('#modal-display1').html(jsson+' group details.');                
                $('.modal-dialog').append($tablelem);
               
          }


      //if it fails do this....
          else if((request.readyState==0)||(request.status=404)){              
                $image.remove();
                 $('#modal-display1').html('could not load details of '+jsson);
                 $('.modal-dialog').html('<p class="par"><font color="red">An Error has occurred!!</font></p>');          
          }
       }

});