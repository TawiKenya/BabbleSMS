/*
  Copyright (c) 2015, Tawi Commercial Services Ltd. All rights reserved.
  Licensed under the OSL-3.0 License:
  http://opensource.org/licenses/OSL-3.0
*/
/*for use by the second showtext table*/


function ContactEdit(val){
   //Prevent the hyperlink to perform default behavior 
	$("a").click(function(event){ event.preventDefault() });
	   var $element=$(val);       
      populatePopup($element);
    }



/**for use by the first showtext table*/
$(document).ready(function() {
	//Prevent the hyperlink to perform default behavior  
   $(".Zlink").click(function(event){	
     event.preventDefault();
     var $element=$(this);      		
      populatePopup($element);  
        });
    });
/*end of click function*/
   
   /*used by search tbltest table after search
   *a copy of this function exist in editcontact.js
   *but function differently*/
	$("#cancel1").click(function(event){
             event.preventDefault();             
             window.location="../accounts/contact.jsp";             
             });
   
     /*used by search tbltest table after search
     *a copy of this function exist in editcontact.js
    *but function differently*/
	$("#close").click(function(event) {
	         event.preventDefault();
	         $('.checkphones').remove();
	         $('.checkemails').remove();
	         $('.body-insert').remove();
	         $(this).parent().parent().hide();
	         $(".groupstablee").show();
	         $(".templatestable").show();
	         $(".tblTest").show();
	          });

        
    $("#cancel").click(function(event) {
	         event.preventDefault();	         
             $(this).parent().parent().hide();
	         $(".groupstablee").show();
	         $(".templatestable").show();
	         $(".tblTest").show();	         
             });

       
        
    function checkgroups(str){   	
        	    $.ajax({
                   method: "GET",
                   url: "selectedgroups.jsp?g="+str                      
                    })
                    .done( function (data) {
                  $('.head-insert').after(data);
                });
           }

             //appends extra phones found      
        function checkphones(str){
        	var $clone;
        	var $parent=$("<div class='checkphones'></div");
        	var $item=$('#addphones1');
        	var text,text2;
        	var lists = [];

        	for(var i=0;i<str.length;i++){
        		if(str[i]===">"){ lists.push(i+1);}
        	              }
        	      var str2=str;
        	          
          $('#phone2').val(str2.substring(1, str2.indexOf("(")));
          $('#phone2').attr('title',str2.substring(str2.indexOf("(")+1, str2.indexOf(")")));           
       while(lists.length>1){       	        
        	 	str2=str2.slice(str2.indexOf(">")+1);        	 	
        	 	text= str2.substring(1,str2.indexOf("(")); 
        	 	text2= str2.substring(str2.indexOf("(")+1, str2.indexOf(")"));      	 	     	 	 
        	 	$clone = $item.clone();        	 	
        	 	$clone.find('#phone2').val(text); 
        	 	$clone.find('#phone2').attr('title',text2);      
        	 	$parent.append($clone);
        	 	$parent.find('#addphns').remove();
        	 	lists.length=lists.length-1;
        	 }        	 
        	 $('#phone').append($parent);
        	}   	
        


             //appends extra emails found
        function checkemails(str){

        	var $clone;
        	var $parent=$("<div class='checkemails'></div");
        	var $item =$("#addemails1");
        	var text="";
        	var lists = [];

        	for(var i=0;i<str.length;i++){
        		if(str[i]===">"){ lists.push(i+1);}
        	            }
        	      var str2=str;
                      
            $item.find('#email').val(str2.substring(1,str2.indexOf("<")));    
       while(lists.length>1){
       	        str2 = str2.slice(str2.indexOf(">")+1);
        	 	text= str2.substring(1,str2.indexOf("<"));        	 	
        	 	$clone = $item.clone();
        	 	$clone.find('#email').val(text);
        	 	$parent.append($clone);
        	 	$parent.find('#addemail').remove();
        	 	lists.length=lists.length-1;
        	  }        	  
        	  $item.append($parent);
        	}      	
    


        //gets the values of the clicked contact and populates the popup
    function populatePopup($element){
       var $td = $element.closest('tr').children('td');
             var account =$td.eq(0).text();
	         var name= $td.eq(1).text(); 
	         var phone= $td.eq(2).html();
	         var email= $td.eq(3).html();	
	         var group= $td.eq(4).text(); 	
	         var description= $td.eq(5).text();	
  	         var uuid= $td.eq(6).text();
  	           	
	         $("#paragraph_1").val(name);
	         checkphones(phone);
	         checkemails(email);
	          $("#uuid").val(uuid);
	          checkgroups(group);
	         //$("#group").val(group);
	         $("#textarea").val(description);
	         $(".tblTest").hide();
             $("#contactdiv").css("display", "block"); 
         } 


 

	function formValidator() {
        var name = $("#paragraph_1").val();
        var email = $("#email").val();
        var contact = $("#phone2").val();
        var description = $("#textarea").val();
        if (name == "" || email == "" || contact == "" || description == "")
        {
            alert("Please Fill All Fields");
		
        }
        else
        {
            if (validateEmail(email)) {
                $("#contactdiv").css("display", "none");
		$(".tblTest").show();
            }
            else {
                alert('Invalid Email Address');

            }
            function validateEmail(email) {
                var filter = /^[\w\-\.\+]+\@[a-zA-Z0-9\.\-]+\.[a-zA-z0-9]{2,4}$/;

                if (filter.test(email)) {
                    return true;
                }
                else {
                    return false;
                }
            }
        }
    }


$(document).ready(function() {
    setTimeout(popup, 3000);
    function popup() { $("#logindiv").css("display", "block");  }
    $("#login #cancel").click(function() {  $(this).parent().parent().hide();  });

//add more phone click
  $("#addphns").click(function(e){ 
                e.preventDefault();
		
		var control = $('#addphones1').clone();
		control.find("#phone2").val("");
		control.find("#addphns").remove();
		$("#phone").append(control);
              
           });

//login form popup login-button click event
    $("#loginbtn").click(function() {
        var name = $("#username").val();
        var password = $("#password").val();
        if (username == "" || password == "")
        {
            alert("Username or Password was Wrong");
        }
        else
        {
            $("#logindiv").css("display", "none");
        }
    });
});


/**+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * 
 * handler for edit group popup
 * 
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 * ++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++
 */
$(".groupstablee td:nth-child(2)").click(function(event){  
	//Prevent the hyperlink to perform default behavior  
event.preventDefault();  

var $tdd= $(this).closest('tr').children('td'); 

	var group_name= $tdd.eq(1).text();  
  
	var description= $tdd.eq(2).text(); 

        var total_Contactss = $tdd.eq(3).text();  
  
	var sms_sent = $tdd.eq(4).text();  
	
	var groupuuid = $tdd.eq(6).text();

	$("#name").val(group_name);
	$("#desc").val(description);
        $("#tcontacts").val(total_Contactss);
	$("#smssent").val(sms_sent);
	$("#guuid").val(groupuuid);
        $(".groupstablee").hide();
        $("#contactdiv").css("display", "block");
    });   



//for editing message template
$(".templatestable td:nth-child(2)").click(function(event){  
	//Prevent the hyperlink to perform default behavior  
event.preventDefault();  

var $tddd= $(this).closest('tr').children('td'); 

	var template_title= $tddd.eq(1).text();  
  
	var template_contents= $tddd.eq(2).text(); 

        var templates_uuid = $tddd.eq(3).text();  

	$("#title").val(template_title);
	$("#contents").val(template_contents);
        $("#templateuuid").val(templates_uuid);
        $(".templatestable").hide();
        $("#contactdiv").css("display", "block");
	});    
    