$(document).ready(function() {
var group;
var group1;
var group2;
var groups;
var $t;
var $td;
var count;
var counter = 0;
$("#tablet td:nth-child(1)").click(function(event){  
	//Prevent the hyperlink to perform default behavior  
    event.preventDefault();  
 



	$td= $(this).closest('tr').children('td');
		
	var count2 = 0;
	$("#tablet td").each( function(){
          
        
		if((count2 % 2) == 0){ 
	
         $(this).css("background", "#F9F9F9");
        }  
		else if((count2 % 2) != 0) {
        $(this).css("background", "transparent");
            
	} count2++;
	
         


 	 });





  
  	($td).css("background", "#808080");
	
	/*$(function(){
    //$("#tablet td").bind("click", function(){
        $("#tablet td").removeClass("clicked"); // Remove all highlights
        $($td).addClass("clicked"); // Add the class only for actually clicked element
    });
//});
	(".clicked").css("background", "gray");
*/
  
     //var group = $td.eq(0).text(); 
	group = $td.eq(0).text();
	
       group2 = $td.eq(1).text(); 
 // group = $(this).text();
  //group2 = $(this).val();
	//var fn = document.getElementById("add");
      //fn.on('click',function(event){
	//event.preventDefault();
	

});

	
$("#add1").click(function(event){
  event.preventDefault();
var check1 = false;


$(".groupsdeleted").each(function() {
		
        if ($(this).val() == group) {
		check1 = true;
            $(this).remove();
        }
  });


	if(!(check1)){

       var field1 = $('.groupsadded').clone().val(group);
       
        field1.appendTo("#groupsform");
  
}

var checkrepeatedadd = 0;
	$("#resulttable  tr #td1").each(function(){

	var tdvalue = $(this).text();
           //alert("tdvalue::"+tdvalue);
		if(tdvalue == group){
		
		checkrepeatedadd++;

	}});
	//alert("checkrepeatedadd1"+checkrepeatedadd);
	if(checkrepeatedadd==0){
	//alert("checkrepeatedadd2"+checkrepeatedadd);

  APP.ajax_post(group,group2); 
	}
	$td.remove();
	count++;

	




	
    });

// Creates a global APP variable that encapsulates all of the logic
window.APP = {
   // rowTemplate: $('<tr id="rows"><td><input type="checkbox" class="case"/></td><td><a href="#"></a></td></tr>'),id ="hideANDseek"
	rowTemplate: $('<tr> <td id="td1"><a href="#"></a></td> <td id ="hideANDseek" > <a href="#"></a></td> </tr>'),
    ajax_post: function (cgroup,cgroup2) {
        //<td><input type='checkbox' class='case'/></td>
	 
        var table1 = $("#resulttable");
        
        var row = APP.rowTemplate.clone();
        
        row.find('td :eq(0)').text(cgroup);
        row.find('td :eq(1)').text(cgroup2);
        
        
        row.appendTo(table1);
    }

   
}




  








//handler for remove button
$(document).on("click", "#td1", function (e) {

        var target = e.target;
      $t =  $(target).closest('tr').children('td'); 
	($t).css("background", "gray");
	groups = $t.eq(0).text();
    });

  var check = false; 
 $("#remove2").click(function(event){
  event.preventDefault();
  
        $(".groupsadded").each(function() {
		
        if ($(this).val() == groups) {
		check = true;
            $(this).remove();
        }


	
    });

	if(!(check)){
	var field2 = $(".groupsdeleted").clone().val(groups);
         field2.appendTo("#groupsform");

}


		






 $t.remove();
    });


$("#cancel1").click(function(event){
event.preventDefault();

window.location="../account/contact.jsp";


});

 





});
