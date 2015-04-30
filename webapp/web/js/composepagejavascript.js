//<!-- ++++  script to allow for user to select a template and use its contents +++ -->

//global variable
var creditconsumed = 0;

    $(document).ready(function() {

          /**Reaction logic to click of the drop down list...here we populate the compose messsage text area wit
          h the value of the option selected and call method for the ccharacter and message counter**/

           $(".add_field_button").click(function(e){ 
                e.preventDefault();
                document.myform.message.value=this.value;
                alert('hello');
                countChar(this);
                alert('do i reach here???');

            });

          /** Reaction logic to click of the drop down list for short code aqnd mask(source)...here we determine what values to fill to the credit 
          table...the credit balance and the credit units consumed...also we decide to which network the source belongs**/ 
            
            $(".message_source").click(function(e) {

              e.preventDefault();

              var balancee = this.getAttribute('id');

              var network=this.getAttribute('label');

              if(network==='Safaricom KE'){

              //populate the balance column of credit table  

                document.getElementById("safcreditbalance").innerHTML=balancee;
                document.getElementById("airtelcreditbalance").innerHTML="-";
                document.getElementById("orangecreditbalance").innerHTML="-";
                document.getElementById("yucreditbalance").innerHTML="-";

              //populate the credit consumed column of the credit table

                document.getElementById("safcreditconsumed").innerHTML=balancee;
                document.getElementById("airtelcreditconsumed").innerHTML=""+ 0;
                document.getElementById("orangecreditconsumed").innerHTML=""+ 0;
                document.getElementById("yucreditconsumed").innerHTML=""+ 0;

              }

              if(network==='Yu KE'){

                document.getElementById("yucreditbalance").innerHTML=balancee;
                document.getElementById("safcreditbalance").innerHTML="-";
                document.getElementById("airtelcreditbalance").innerHTML="-";
                document.getElementById("orangecreditbalance").innerHTML="-";

                //populate the credit consumed column of the credit table

                document.getElementById("yucreditconsumed").innerHTML=balancee;
                document.getElementById("airtelcreditconsumed").innerHTML=""+ 0;
                document.getElementById("orangecreditconsumed").innerHTML=""+ 0;
                document.getElementById("safcreditconsumed").innerHTML=""+ 0;

              }

              if(network==='Airtel KE'){

                document.getElementById("airtelcreditbalance").innerHTML=balancee;
                document.getElementById("safcreditbalance").innerHTML="-";
                document.getElementById("yucreditbalance").innerHTML="-";
                document.getElementById("orangecreditbalance").innerHTML="-";

                //populate the credit consumed column of the credit table

                document.getElementById("airtelcreditconsumed").innerHTML=balancee;
                document.getElementById("safcreditconsumed").innerHTML=""+ 0;
                document.getElementById("orangecreditconsumed").innerHTML=""+ 0;
                document.getElementById("yucreditconsumed").innerHTML=""+ 0;

              }

              if(network==='Orange KE'){

                document.getElementById("orangecreditbalance").innerHTML=balancee;
                document.getElementById("safcreditbalance").innerHTML="-";
                document.getElementById("airtelcreditbalance").innerHTML="-";
                document.getElementById("yucreditbalance").innerHTML="-";

                //populate the credit consumed column of the credit table

                document.getElementById("orangecreditconsumed").innerHTML=balancee;
                document.getElementById("airtelcreditconsumed").innerHTML=""+ 0;
                document.getElementById("safcreditconsumed").innerHTML=""+ 0;
                document.getElementById("yucreditconsumed").innerHTML=""+ 0;

              }

            });

});


//function for dynamic character and messages count

function countChar(val) {

        var len = val.value.length;

        alert('test1');
        alert(creditconsumed);

       creditconsumed = smsCount*1;
       alert('test2');
       alert(creditconsumed);

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

