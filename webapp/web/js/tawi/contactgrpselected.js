
function Chromecheck(){
	$.browser.chrome = /chrom(e|ium)/.test(navigator.userAgent.toLowerCase());
  if($.browser.chrome){
     executevent();
	    }
  }


$(document).ready(function(){
	$('.grp').on('click',(function(){
		executevent();
		}));

   executevent();
});

function executevent(){
	var grpselect=$('.groupselect option:selected').val();		
		var grpname=$('.groupselect option:selected').attr('name');
		var nwkname=$('.networkselect option:selected').attr('name');
		$('#header-display').html("<h3>Displaying "+grpname+" contacts </h3>");
    sendRequest("allnetworks.jsp?grp="+grpselect);		
}

//make ajax call to various files
function sendRequest(str){         
           var request = getRequestObject1();
           request.onreadystatechange =
              function() { handleRespons(request); };
           request.open("GET", str , true);
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
