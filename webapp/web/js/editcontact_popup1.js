$(document).ready(function() {
var numCols = 0 ;
var numCols2 = 0 ;
var numCols3 = 0 ;
var testrepeat = 0;
var testrepeat2 = 0;
    setTimeout(popup, 3000);

    function popup() {
        $("#logindiv").css("display", "block");
    }

    //$("#login #cancel").click(function() {
      //  $(this).parent().parent().hide();
   // });

   $(".tblTest td:nth-child(2)").click(function(event){  
	//Prevent the hyperlink to perform default behavior  
event.preventDefault();  


var $td= $(this).closest('tr').children('td'); 
        var rows = $(this).closest('tr');
	var getrows = $(this).closest('tr').find("#hiddenphones").length;
           
	var getrows2 = $(this).closest('tr').find("#hiddenemails").length;
	  
	rows.find("#hiddenphones").each(function() {
	numCols++;
	
   	var phone1 = $(this).text();
	$(".phonee").each(function() {
	var testa = $(this).val();
	
	if (testa == phone1){
	testrepeat++;
	
	} 
	}); 
	if(testrepeat == 0){
	var phones = $("#addphones1").clone();
	phones.find("#phone2").val(phone1);
	phones.find("#phone2").attr("class" , "phonee");
	phones.find("#addphns").remove();
	phones.appendTo("#phone");
	}
	
	
	}); 

	rows.find("#hiddenemails").each(function() {
	numCols2++;
	
   	var email1 = $(this).text();
	$(".emailee").each(function() {
	var testa2 = $(this).val();
	
	if (testa2 == email1){
	testrepeat2++;
	
	} 
	}); 
	if(testrepeat2 == 0){
	var emails = $("#addemails1").clone();
	emails.find("#email").val(email1);
	emails.find("#email").attr("class" , "emailee");
	emails.find("#addemails").remove();
	emails.appendTo("#mail");
	}
	
	
	}); 

  	rows.find("#hiddengroups").each(function() {
	var groupvalue = $(this).text();
	
	numCols3++; 
	
	APP.ajax_post(groupvalue);
	window.APP = {
   
	rowTemplate: $('<tr> <td id="td1"><a href="#"></a></td>  </tr>'),
        ajax_post: function (groupvalue) {
        
        var table1 = $("#resulttable");
      
        var row = APP.rowTemplate.clone();
        
        row.find('td :eq(0)').text(groupvalue);
        
        
        
        row.appendTo(table1);
        }}




	 
	//alert("numCols4::" +numCols3);
	//alert("#Testhiddengroups:::" +groupvalue);
	});
		/*var array1 = new Array();
		var i = 0;
	rows.find("#hiddengroups").each(function() {
	      array1[i] = $(this).text();i++;alert("here now");
	});	
	 alert("array1.length::"+array1.length);
	var array2 = new Array(); 
	var counts = 0;alert("and  again");
	alert($("#tablet tr td:eq(0)").length );
	$("#tablet tr td:eq(0)").each( function(){
	var counts = 0;alert("and  again2");	
          array2[counts] = $(this).text();counts++;
	alert("array2[i]::"+array2.length);
	alert($(this).text());
	 });
	for(var j=0; j<array2.length; j++) {
		alert(array2[j]);
	for(var j2=0; j2<array1.length; j2++) {
	alert("here again");
	if(array1[j] == array2[j2]){
		$(this).remove();alert("lastly here ");
	
	}}}
	
	*/


	
        var account =$td.eq(0).text();
	//alert("account:::"+account);
	var name= $td.eq(1).text();  
  
	var phone= $td.eq(2).text();  
  
	var email= $td.eq(4 + numCols).text();  
	
	var group= $td.eq( numCols + 6 + numCols2 ).text();
	//alert("group :::" + group);
	var description= $td.eq( numCols + 8 + numCols2 + numCols3 ).text();
	//alert("description :::" + description);
  	var uuid= $td.eq(numCols + 9 + numCols2+numCols3).text();
	//alert("uuid" + uuid);
        
  ;
	

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


//contact form popup send-button click event
   // $("#send").click(function() 

	function formValidator(){
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



});

 