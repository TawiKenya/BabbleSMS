$(document).ready(function() {
var numCols = 0 ;
    setTimeout(popup, 3000);

    function popup() {
        $("#logindiv").css("display", "block");
    }

    $("#login #cancel").click(function() {
        $(this).parent().parent().hide();
    });

   $(".tblTest td:nth-child(2)").click(function(event){  
	//Prevent the hyperlink to perform default behavior  
event.preventDefault();  
//alert($(event.target).text())  


 //numCols = $(".tblTest").find('tr')[0].cells.length;
//alert('Total columns : '+numCols);

var $td= $(this).closest('tr').children('td'); 
	
	$("#hiddenphones").each(function() {
	numCols++;
	var testrepeat = true;
   	var phone1 = $(this).text();
	$("#addphones1").each(function() {
	if ($(this).val() == phone1){
	testrepeat = false;
	}  
	if(testrepeat){
	var phones = $("#addphones1").clone();
	phones.find("#phone2").val(phone1);
	phones.find("#addphn").remove();
	phones.appendTo("#phone");
	}
	});
	alert(phone1);
	}); 
  
       /* for (i = 5; i < numCols-3; i++) {
         var phone1= $td.eq(i).text(); 
	 var phones = $("#addphones1").clone();
	phones.find("#phone2").val(phone1);
	phones.appendTo("#phone");
        }*/
	alert(numCols);
	var name= $td.eq(1).text();  
  
	var phone= $td.eq(2).text();  
  
	var email= $td.eq(4 + numCols).text();  
	
	var group= $td.eq(5 + numCols).text();
	alert(group);
	var description= $td.eq( 6+ numCols).text();
	
  	var uuid= $td.eq( 7 + numCols).text();
	alert(uuid);
	

	$("#paragraph_1").val(name);
	$("#phone2").val(phone);
	$("#email").val(email);
	$("#uuid").val(uuid);
	$("#group").val(group);
	$("#textarea").val(description);
	$(".tblTest").hide();
        $("#contactdiv").css("display", "block");
    });

    $("#cancel").click(function(event) {
	event.preventDefault();
        $(this).parent().parent().hide();
	$(".tblTest").show();
    });
    $("#close").click(function(event) {
	event.preventDefault();
        $(this).parent().parent().hide();
	$(".tblTest").show();
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

 
