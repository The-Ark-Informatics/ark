/*
 * JavaScript to display a busy/lodaing indicator on the page when page loading is taking time
 * Disables the entire page (via and overlay div tag), and displays a pop-up "Loading..." message
 * 
 * Note: HTML required as follows (at bottom of page):
 * <div id="overlay">
		<div id="busyIndicator" style=""><img height="16" width="16" src="images/indicator.gif"> Loading...</div>
	</div>
 */

window.onload = setupFunc;

function setupFunc() {
	document.getElementsByTagName('body')[0].onclick = clickFunc;
	hideBusySign();
	// Disabled this line, as it was setting the sign incorrectly...
	//Wicket.Ajax.registerPreCallHandler(showBusySign);
	Wicket.Ajax.registerPostCallHandler(hideBusySign);
	Wicket.Ajax.registerFailureHandler(hideBusySign);
}

function hideBusySign() {
	document.getElementById('busyIndicator').style.display ='none';
	overlay = document.getElementById("overlay");
	overlay.style.visibility = "hidden";
}

function showBusySign() {
	document.getElementById('busyIndicator').style.display ='inline';
	overlay = document.getElementById("overlay");
	overlay.style.visibility = "visible";
}

function clickFunc(eventData) {
	var clickedElement = (window.event) ? event.srcElement : eventData.target;
	// Only show loading for links
	if (clickedElement.tagName.toUpperCase() == 'A' || clickedElement.parentNode.tagName.toUpperCase() == 'A') {
		showBusySign();
	}
}