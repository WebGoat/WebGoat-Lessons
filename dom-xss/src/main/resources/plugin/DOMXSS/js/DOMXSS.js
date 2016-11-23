 function displayGreeting(name) { if (name != ''){ document.getElementById("greeting").innerHTML="Hello, " + escapeHTML(name); + "!"; } }
