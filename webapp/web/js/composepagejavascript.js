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
        }
