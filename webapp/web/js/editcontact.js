                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                                            $(document).ready(function() {
var groupname;

var groupuuid;
var groups;
var $tdclicked;
var groupuuids;


$("#tablet td:nth-child(1)").click(function(event){  
	//Prevent the hyperlink to perform default behavior  
    event.preventDefault();  
 



	$tdclicked= $(this).closest('tr').children('td');
		
	var tdcounter = 0;
	$("#tablet td").each( function(){
          
        
		if((tdcounter % 2) == 0){ 
	
         $(this).css("background", "#F9F9F9");
        }  
		else if((tdcounter % 2) != 0) {
        $(this).css("background", "transparent");
            
	} tdcounter++;
	
         


 	 });





  
  	($tdclicked).css("background", "#808080");
	
	
	groupname = $tdclicked.eq(0).text();
	
       groupuuid = $tdclicked.eq(1).text(); 
 
	

});

	
$("#add1").click(function(event){
  event.preventDefault();
var check1 = false;


$(".groupsdeleted").each(function() {
		
        if ($(this).val() == groupname) {
		check1 = true;
            $(this).remove();
        }
  });


	if(!(check1)){

       var field1 = $('.groupsadded').clone().val(groupname);
       
        field1.appendTo("#groupsform");
  
}

var checkrepeatedadd = 0;
	$("#resulttable  tr #td1").each(function(){

	var tdvalue = $(this).text();
           
		if(tdvalue == groupname){
		
		checkrepeatedadd++;

	}});
	
	if(checkrepeatedadd==0){
	

  APP.ajax_post(groupname,groupuuid); 
	}
	$tdclicked.remove();
	

	




	
    });

// Creates a global APP variable that encapsulates all of the logic
window.APP = {
   
	rowTemplate: $('<tr> <td id="td1"><a href="#"></a></td> <td id ="hideANDseek" > <a href="#"></a></td> </tr>'),
    ajax_post: function (groupname,groupuuid) {
       
	 
        var table1 = $("#resulttable");
        
        var row = APP.rowTemplate.clone();
        
        row.find('td a:eq(0)').text(groupname);
        row.find('td a:eq(1)').text(groupuuid);
        
        
        row.appendTo(table1);
    }

   
}




  







var $clickedgroupcontacttd;
//handler for remove button
$(document).on("click", "#td1", function (e) {

        var target = e.target;
     $clickedgroupcontacttd =  $(target).closest('tr').children('td'); 
	($clickedgroupcontacttd).css("background", "gray");
	groups = $clickedgroupcontacttd.eq(0).text();
	groupuuids = $clickedgroupcontacttd.eq(1).text();
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

	//var table2 = $("#tablet");
	//var row = APP.rowTemplate.clone();
        
        //row.find('td a:eq(0)').text(groups);
       // row.find('td a:eq(1)').text(groupuuids);
        
        
       // row.appendTo(table2);
	
    

	if(!(check)){
	var field2 = $(".groupsdeleted").clone().val(groups);
         field2.appendTo("#groupsform");

	}


	$clickedgroupcontacttd.remove();
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
 



    $("[href]").each(function() {
    if (this.href == window.location.href) {
        $(this).css('background-color', '#d41e24');
        }
    });
	//settings edit password matcher called
  



});
 