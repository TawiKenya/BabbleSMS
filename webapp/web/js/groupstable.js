$(document).ready(function() {
var group;
var group1;
var group2;
var groups;
var $t;
var $td;
var count;

	


	
$("#add").click(function(event){
  event.preventDefault();
 // $("#groupsform").append('<input type="hidden"  class ="groupsadded" name="groupsadded[]"  />');
       
	//$("#groups").addClass(group2).val(group2);
	//$('input[type="text"]').removeClass(group2);
		// if($(".groupsadded").val() == ""){
	// $(".groupsadded").val(group2);
//function () {
        //<td><input type='checkbox' class='case'/></td>
         //var clonetext = "<input type="text"  class ="groupsadded" name="groupsadded[]"  />";
	 //var id_input = "<input class='id' maxlength='1' name='id' type='text' />";
	 //var div1 = $("#groupsform");
       var field1 = $('.groupsadded').clone().val(group);
        //var field1 = clonetext.clone();
        
      // (".groupsadded").val(group2);
        
        
        //div1.append(field1);
        field1.appendTo("#groupsform");
  //  }




//}
  APP.ajax_post(group,group2);
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


  








//handler for remove button
$(document).on("click", "#td1", function (e) {

        var target = e.target;
      $t =  $(target).closest('tr').children('td'); 
	($t).css("background", "gray");
	groups = $t.eq(0).text();
    });

   
 $("#remove").click(function(event){
  event.preventDefault();
  
        $(".groupsadded").each(function() {
	
        if ($(this).val() == groups) {
            $(this).remove();
        }
    });
       






 $t.remove();
    });


$("#cancel1").click(function(event){
event.preventDefault();

window.location="../account/contact.jsp";


});
//add more email click
        $("#addemail").click(function(e) {
            e.preventDefault();
            $("#mail").append("<div class='controls'> <input style='margin-top:5px;' class='input-xlarge focused'  id='email' name='email[]' type='text'> </div>");

        });
 





});
