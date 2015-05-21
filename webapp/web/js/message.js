
function getRequestObject() {
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
function sendRequest(inputField,address) {	
	var data = "uuid=" + escape(inputField.name);
     var request = getRequestObject();
      request.onreadystatechange =function() { handleResponse(request); };
       request.open("POST", address, true);
       request.setRequestHeader("Content-Type", "application/x-www-form-urlencoded");
       request.send(data);
        }

 function handleResponse(request) {
      if ((request.readyState == 4) && (request.status == 200)) {
       alert(request.responseText);
             }
       }