//<!-- ++++  script to allow for user to select a template and use its contents +++ -->
            $(document).ready(function() {

           $(".add_field_button").click(function(e){ //on add input button click
                e.preventDefault();
                alert('yap me'+this.getAttribute('id'));
                document.myform.message.value=this.value;
                countChar(this);
                

               //add input box

            });
            
            $(".message_source").click(function(e) {
              e.preventDefault();

              var balancee = this.getAttribute('id');
             
              

              var network=this.getAttribute('label');

              if(network==='Safaricom KE'){

                document.getElementById("safcreditbalance").innerHTML=balancee;
                document.getElementById("airtelcreditbalance").innerHTML="-";
                document.getElementById("orangecreditbalance").innerHTML="-";
                document.getElementById("yucreditbalance").innerHTML="-";
              }

              if(network==='Yu KE'){

                document.getElementById("yucreditbalance").innerHTML=balancee;
                document.getElementById("safcreditbalance").innerHTML="-";
                document.getElementById("airtelcreditbalance").innerHTML="-";
                document.getElementById("orangecreditbalance").innerHTML="-";
              }
              if(network==='Airtel KE'){

                document.getElementById("airtelcreditbalance").innerHTML=balancee;
                document.getElementById("safcreditbalance").innerHTML="-";
                document.getElementById("yucreditbalance").innerHTML="-";
                document.getElementById("orangecreditbalance").innerHTML="-";
              }
              if(network==='Orange KE'){

                document.getElementById("orangecreditbalance").innerHTML=balancee;
                document.getElementById("safcreditbalance").innerHTML="-";
                document.getElementById("airtelcreditbalance").innerHTML="-";
                document.getElementById("yucreditbalance").innerHTML="-";
              }


              // body...
            });
});


//function for dynamic character and messages count
function countChar(val) {
        var len = val.value.length;
        var smsCount = Math.floor(len/160)+1;


alert(document.getElementById("creditconsumed").value);
       document.getElementById("creditconsumed").innerHTML=smsCount*1;

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

