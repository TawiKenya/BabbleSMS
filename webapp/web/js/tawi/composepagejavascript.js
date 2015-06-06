//<!-- ++++  script to allow for user to select a template and use its contents +++ -->
            $(document).ready(function() {

           $(".add_field_button").click(function(e){ //on add input button click
                e.preventDefault();
                document.myform.message.value=this.value;
                countChar(this);
                
               //add input box

            });
});


//function for dynamic character and messages count
function countChar(val) {
        var len = val.value.length;
        var smsCount = Math.floor(len/160)+1;        
              

        if(len===0){ 
          $('#charCount').text(0);
          $('#smsNum').text(0);   
        }
        else{
        	 $('#charCount').text(len);
          $('#smsNum').text(smsCount);
          if(len<=160){
          document.getElementById("count").style.color = "green"; 
          document.getElementById("sms").style.color = "green"; 
          document.getElementById("charCount").style.color = "green"; 
          document.getElementById("smsNum").style.color = "green"; 
      }
      else{
        document.getElementById("count").style.color = "red"; 
        document.getElementById("sms").style.color = "red"; 
        document.getElementById("charCount").style.color = "red"; 
        document.getElementById("smsNum").style.color = "red"; 
      }
        }
        
        //checks creditconsumed row on credittable based on the selected  network..
        
        var sms = val.value.length; 
        var Count=200*(Math.floor(sms/160)+1);
        var net =document.getElementById("source");
        var netcheck = net.options[net.selectedIndex].label;
         //alert('System check1 by Migwi Ndungu '+netcheck+' on the '+Count);
        
       if (netcheck=='Safaricom KE') {       	
   document.getElementById("safcreditconsumed").innerHTML='<td><font color=green>'+Count+'</font></td>';
	document.getElementById("orangecreditconsumed").innerHTML='<td>0</td>';
	document.getElementById("yucreditconsumed").innerHTML='<td>0</td>';
	document.getElementById("airtelcreditconsumed").innerHTML='<td>0</td>';
	}
	
else if (netcheck=='Yu KE') {	 
  	document.getElementById("yucreditconsumed").innerHTML='<td><font color=blue>'+Count+'</font></td>';
	document.getElementById("safcreditconsumed").innerHTML='<td>0</td>';
	document.getElementById("orangecreditconsumed").innerHTML='<td>0</td>';
	document.getElementById("airtelcreditconsumed").innerHTML='<td>0</td>';
	}
	
else if(netcheck=='Airtel KE'){	
	document.getElementById("airtelcreditconsumed").innerHTML='<td><font color=red>'+Count+'</font></td>';
	document.getElementById("safcreditconsumed").innerHTML='<td>0</td>';
	document.getElementById("orangecreditconsumed").innerHTML='<td>0</td>';
	document.getElementById("yucreditconsumed").innerHTML='<td>0</td>';
	}
	
else if (netcheck=='Orange KE'){
	document.getElementById("orangecreditconsumed").innerHTML='<td><font color=orange>'+Count+'</font></td>';
	document.getElementById("safcreditconsumed").innerHTML='<td>0</td>';
	document.getElementById("yucreditconsumed").innerHTML='<td>0</td>';
	document.getElementById("airtelcreditconsumed").innerHTML='<td>0</td>';
}
       
        }

        //function change color of button in the compose page modal
        function changecolor(val){          
          if( val.disabled !=="true"){
          val.disabled="true";
          val.style.backgroundColor="grey"
        }
          else{
            val.style.backgroundColor="rgba(6, 156, 6, 0.45)";
            val.disabled="false";
          }         
        }

        function changecolor1z(val){          
          if( val.disabled !=="true"){
          val.disabled="true";
          val.style.backgroundColor="grey"
        }
          else{
            val.style.backgroundColor="rgba(6, 156, 6, 0.45)";
            val.disabled="false";
          }         
        }

        function changecolorf(val){
          alert();
          alert(val.selected.name);
          alert(val.selected.class);
          val.selected.disabled="true";
          alert(val.selected.class);
        }

        
