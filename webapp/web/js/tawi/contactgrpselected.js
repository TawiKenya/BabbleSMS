
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

		$('.nwk').on('click',(function(){
		executevent();
	}));
});

function executevent(){
	var grpselect=$('.groupselect option:selected').val();
		var nwkselect=$('.networkselect option:selected').val();
		var grpname=$('.groupselect option:selected').attr('name');
		var nwkname=$('.networkselect option:selected').attr('name');
		$('#header-display').html("<h3>Displaying "+grpname+" contacts in "+nwkname+"</h3>");
		typenetcheck(grpselect,nwkselect);
}

function typenetcheck(grpselect,nwkselect){
	if(nwkselect==="allnetworks"){
	  sendRequest("allnetworks.jsp?grp="+grpselect);		
	}
	 else {
	  sendRequest("selectednetwork.jsp?grp="+grpselect +"&amp;nwk="+nwkselect);	 	
	}
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
     	alert(request.responseText);  
           if ((request.readyState == 4)&&(request.status==200)) {
             $('#contactgrp').remove();        	
            $('#header-display').after(request.responseText);
             }
         }     	    
