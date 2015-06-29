/*
  Copyright (c) 2015, Tawi Commercial Services Ltd. All rights reserved.
  Licensed under the OSL-3.0 License:
  http://opensource.org/licenses/OSL-3.0

  Author: Migwi Ndung'u  <migwi@tawi.mobi>
*/
/*
  Copyright (c) 2015, Tawi Commercial Services Ltd. All rights reserved.
  Licensed under the OSL-3.0 License:
  http://opensource.org/licenses/OSL-3.0

  Author: Migwi Ndung'u  <migwi@tawi.mobi>
*/
  function setSource(val11) {  
  var Accuuid = val11.value;

  var data = "accountuuid=" + escape(Accuuid);
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
       request.open("POST", "getSource", true);
       request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
       request.send(data);
  }
//end of click function



function handleResponse(request) {
      if ((request.readyState == 4) && (request.status == 200)) {               
                 var obj = JSON.parse(request.responseText);
                 //console.log(obj);
                 $("#source").remove();
                 var $source = $('<select name="source" id="source" ></select>');                   
               
               for(var i=0;i<obj.length;i++){
               	var uuid = obj[i].uuid;
               	var name = obj[i].name;
             var $item=$('<option value='+uuid+'>'+name+'</option>');
             $source.append($item);
                 }
                 $('#getsource').append($source);
         }
       }

