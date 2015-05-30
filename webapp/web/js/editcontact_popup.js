/*
  Copyright (c) 2015, Tawi Commercial Services Ltd. All rights reserved.
  Licensed under the OSL-3.0 License:
  http://opensource.org/licenses/OSL-3.0
*/

$(document).ready(function() {
var phonenumCols = 0 ;
var emailnumCols = 0 ;
var groupnumCols = 0 ;
var testrepeatedphones = 0;
var testrepeatedemails = 0;
    

   $(".tblTest a").click(function(event){  
	//Prevent the hyperlink to perform default behavior  
event.preventDefault();  


var $td= $(this).closest('tr').children('td'); 
        var rows = $(this).closest('tr');
	var getrows = $(this).closest('tr').find("#hiddenphones").length;
           
	var getrows2 = $(this).closest('tr').find("#hiddenemails").length;
	  
	rows.find("#hiddenphones").each(function() {
	phonenumCols++;
	
   	var phone1 = $(this).text();
	$(".phonee").each(function() {
	var testa = $(this).val();
	
	if (testa == phone1){
	testrepeatedphones++;
	
	} 
	}); 
	if(testrepeatedphones == 0){
	var phones = $("#addphones1").clone();
	phones.find("#phone2").val(phone1);
	phones.find("#phone2").attr("class" , "phonee");
	phones.find("#addphns").remove();
	phones.appendTo("#phone");
	}
	
	
	}); 

	rows.find("#hiddenemails").each(function() {
	emailnumCols++;
	
   	var email1 = $(this).text();
	$(".emailee").each(function() {
	var testa2 = $(this).val();
	
	if (testa2 == email1){
	testrepeatedemails++;
	
	} 
	}); 
	if(testrepeatedemails == 0){
	var emails = $("#addemails1").clone();
	emails.find("#email").val(email1);
	emails.find("#email").attr("class" , "emailee");
	emails.find("#addemails").remove();
	emails.appendTo("#mail");
	}
	
	
	}); 

  	rows.find("#hiddengroups").each(function() {
	var groupvalue = $(this).text();
	
	groupnumCols++; 
	
	APP.ajax_post(groupvalue);
	window.APP = {
   
	rowTemplate: $('<tr> <td id="td1"><a href="#"></a></td>  </tr>'),
        ajax_post: function (groupvalue) {
        
        var table1 = $("#resulttable");
      
        var row = APP.rowTemplate.clone();
        
        row.find('td :eq(0)').text(groupvalue);
        
        
        
        row.appendTo(table1);
        }}
	 
	
	});
		


	
        var account =$td.eq(0).text();
	
	var name= $td.eq(1).text();  
  
	var phone= $td.eq(2).text();  
  
	var email= $td.eq(4 + phonenumCols).text();  
	
	var group= $td.eq( phonenumCols + 6 + emailnumCols ).text();
	
	var description= $td.eq( phonenumCols + 8 + emailnumCols + groupnumCols ).text();
	
  	var uuid= $td.eq(phonenumCols + 9 + emailnumCols+groupnumCols).text();

	$("#paragraph_1").val(name);
	$("#phone2").val(phone);
	$("#email").val(email);
	$("#uuid").val(uuid);
	$("#group").val(group);
	$("#textarea").val(description);
	$(".tblTest").hide();
        $("#contactdiv").css("display", "block");
        

        
    });

   
	$("#cancel1").click(function(event){
         event.preventDefault();

         window.location="../account/contact.jsp";


        });
   

	$("#close").click(function(event) {
	event.preventDefault();
	event.preventDefault();

         window.location="../account/contact.jsp";
	});
        
    $("#cancel").click(function(event) {
	event.preventDefault();
        $(this).parent().parent().hide();
	$(".groupstablee").show();
    });
    $("#close").click(function(event) {
	event.preventDefault();
        $(this).parent().parent().hide();
	$(".groupstablee").show();
    });


 

	function formValidator() {
        var name = $("#paragraph_1").val();
        var email = $("#email").val();
        var contact = $("#phone").val();
        var description = $("#dept").val();
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

    $("#cancel").click(function(event) {
	event.preventDefault();
        $(this).parent().parent().hide();
	$(".groupstablee").show();
    });
    $("#close").click(function(event) {
	event.preventDefault();
        $(this).parent().parent().hide();
	$(".groupstablee").show();
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

    $("#cancel").click(function(event) {
	event.preventDefault();
        $(this).parent().parent().hide();
	$(".templatestable").show();
    });
    $("#close").click(function(event) {
	event.preventDefault();
        $(this).parent().parent().hide();
	$(".templatestable").show();
    });


});

 
