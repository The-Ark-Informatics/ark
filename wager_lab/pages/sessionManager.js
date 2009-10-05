var req;
var itemDescription;
var message="true";
var pollingInterval=10000;

getSessionManagerResponse("reset=true");

message="true";

function renewSession(){
    if(message=="true"){
        getSessionManagerResponse("renew=true");
        setTimeout("renewSession()",pollingInterval);
    }
}
setTimeout("renewSession()",pollingInterval);


function getSessionManagerResponse( command ) {

   if (window.XMLHttpRequest) {
       req = new XMLHttpRequest();
   } else if (window.ActiveXObject) {
       req = new ActiveXObject("Microsoft.XMLHTTP");
   }
   req.open("GET", "SessionManagerServlet?"+command, true);

   req.onreadystatechange = callback;
   req.send(null);
}

function callback() {
    if (req.readyState == 4) {
        if (req.status == 200) {
           message = req.responseText;
        }//else{
          // alert("Problem: " + req.statusText);
        //}
    }
}
