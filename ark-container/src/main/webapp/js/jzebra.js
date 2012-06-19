var $d, $j;
window.onload=function(){ $d = document; $j = $d.jzebra; }

// Error messages: safe to change
var ERR_LOAD = 'jZebra has not loaded yet.';
var ERR_FUNC = 'jZebra does not recognize function: "%"';
var ERR_PRINT = 'jZebra could not find the specified printer: "%"';
var ERR_LIST = 'jZebra could not listing all printers';
var ERR = 'jZebra Exception: ';

/**
* Helper function for applet.findPrinter(); and monitorApplet('isDoneFinding",...);
*/

var printerName;
function getPrinterThenCall(printer, doneFunc) {
   // Fix windows shared printers - broken
   //printer = printer.replace(/\\/g,'\\\\');
   printerName = ' "' + printer + '"';
   $j.findPrinter(printer);
   monitorApplet('isDoneFinding()', doneFunc, "Finding printer " + printer);
}

function getPrinters() {
   try {if (isActive()) { return $j.getPrinters().split(','); }}
   catch (err) { alert(ERR_LIST + ': ' + err); return [];}
   alert(ERR_LIST); return [];		
}

function monitorLoadingThenCall(doneFunc) {
   try { if (isActive()) { eval(doneFunc); return; }} 
   catch (err) { alert("Unknown error occured: " + err); return; }
   doneFunc =  "'" + doneFunc.replace(/\"/g,'\\"') + "'";
   window.setTimeout("monitorLoadingThenCall(" + doneFunc + ")", 100);
}

function isActive() {
   if ($d && $j) {
      try { return $j.isActive; }
      // IE Fix, don't expect it to make sense
      catch (err) { return true; }
   }
   return false;
}

/**
* Monitors the Java applet until it is complete with the specified function
*    applFunc:  Should return a "true" or "false"
*    doneFunc:  Will be called if the function completes without error
*    desc:      Optional description for errors, etc
*
* Example:
*    monitorApplet('isDoneFinding()', 'alert(\\"Success\\")', '');
*/
function monitorApplet(applFunc, doneFunc, desc) {
   if ($d && $j) {
      var done;
      try { done = eval('$j.' + applFunc); }
      catch (err) { alert(ERR + ERR_FUNC.replace('%', applFunc)); return; }
      if (!done) { window.setTimeout('monitorApplet("' + applFunc + '", "' + 
         doneFunc.replace(/\"/g,'\\"') + '", "' + desc + '")', 100); return;
      } else { 
	   var p = $j.getPrinterName();
	   if (!p) { alert(ERR + ERR_PRINT.replace('%', printerName)); return; }
	   var e = $j.getException();
	   if (e) { alert(ERR + (desc ? '' : ' [' + desc + '] ') + $j.getExceptionMessage());
	      $j.clearException();
	   } else { eval(doneFunc); }
      }
   } else {
      alert(ERR + ERR_LOAD);
   }
}


function createApplet(printer) {
   var $a = document.createElement('applet');
   $a.setAttribute('name', 'jzebra');
   $a.setAttribute('code', 'jzebra.PrintApplet.class');
   $a.setAttribute('alt', 'Error loading jZebra applet');
   $a.setAttribute('archive', './jzebra.jar');
   $a.setAttribute('width', '0');
   $a.setAttribute('height', '0');
   if (printer) {
      var $p = document.createElement('param');
      $p.setAttribute('printer', 'zebra');
      $a.appendChild($p);
   }
   document.getElementsByTagName('body')[0].appendChild($a);
}

function Chr(i) { return chr(i); }
function CHR(i) { return chr(i); }
function chr(i) { return String.fromCharCode(i); }
