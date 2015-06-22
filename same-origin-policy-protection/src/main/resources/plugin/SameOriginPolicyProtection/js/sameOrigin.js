


function submitXHR(){
    document.getElementById("testedURL").value = document.getElementById("requestedURL").value;
	document.getElementById("responseTitle").innerHTML="Response: ";
   	
   	document.getElementById("responseArea").innerHTML=""; 

	//alert("creating XHR request for: " + document.getElementById("requestedURL").value);
	console.log("creating XHR request for: " + document.getElementById("requestedURL").value);

	
	try{
		ajaxFunction();
	}
	catch(err){
		alert(err);
		document.getElementById("requestedURL").value=""; 
	}
}



function ajaxFunction()
  {
	var xmlHttp;
  try
    {
    // Firefox, Opera 8.0+, Safari
    xmlHttp=new XMLHttpRequest();
    }
  catch (e)
    {
    // Internet Explorer
    try
      {
      xmlHttp=new ActiveXObject("Msxml2.XMLHTTP");
      }
    catch (e)
      {
      try
        {
        xmlHttp=new ActiveXObject("Microsoft.XMLHTTP");
        }
      catch (e)
        {
        alert("Your browser does not support AJAX!");
        return false;
        }
      }
    }
    xmlHttp.onreadystatechange=function()
      {
      
      var result = xmlHttp.responseText;
      if(xmlHttp.readyState==4)
        {
        	
        	
        	document.getElementById("responseTitle").innerHTML="Response from: " 
        		+ document.getElementById("requestedURL").value ;
        		
        	document.getElementById("responseArea").innerHTML=result;         	
        	
        	document.getElementById("requestedURL").value=""; 
        	
        }
      }
      
    xmlHttp.open("GET",document.getElementById("requestedURL").value,true);
    xmlHttp.send(null);
  }
