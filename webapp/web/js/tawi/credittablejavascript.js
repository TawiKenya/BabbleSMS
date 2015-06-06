function networkselect(val){
	
//Name of the selected network service provider
var networkprovider=val.label;

//changes network provider color according to the item clicked
if(networkprovider=='Safaricom KE') {
document.getElementById("Safaricom").innerHTML='<td><font color=green>Safaricom</font></td>';
	document.getElementById("Orange").innerHTML='<td>Orange</td>';
	document.getElementById("Yu").innerHTML='<td>Yu</td>';
	document.getElementById("Airtel").innerHTML='<td>Airtel</td>';
	}
	
else if (networkprovider=='Yu KE') {  	
  	document.getElementById("Yu").innerHTML='<td><font color=blue>Yu</font></td>';
	document.getElementById("Safaricom").innerHTML='<td>Safaricom</td>';
	document.getElementById("Orange").innerHTML='<td>Orange</td>';
	document.getElementById("Airtel").innerHTML='<td>Airtel</td>';
	}
	
else if(networkprovider=='Airtel KE'){
		document.getElementById("Airtel").innerHTML='<td><font color=red>Airtel</font></td>';
	document.getElementById("Safaricom").innerHTML='<td>Safaricom</td>';
	document.getElementById("Orange").innerHTML='<td>Orange</td>';
	document.getElementById("Yu").innerHTML='<td>Yu</td>';
	}
	
else if (networkprovider=='Orange KE'){
		document.getElementById("Orange").innerHTML='<td><font color=orange>Orange</font></td>';
	document.getElementById("Safaricom").innerHTML='<td>Safaricom</td>';
	document.getElementById("Yu").innerHTML='<td>Yu</td>';
	document.getElementById("Airtel").innerHTML='<td>Airtel</td>';
}
}