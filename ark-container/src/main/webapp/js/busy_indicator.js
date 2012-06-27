window.onload = setupFunc;

function setupFunc() {
	document.getElementsByTagName('body')[0].onclick = clickFunc;
	hideBusysign();
	Wicket.Ajax.registerPreCallHandler(showBusysign);
	Wicket.Ajax.registerPostCallHandler(hideBusysign);
	Wicket.Ajax.registerFailureHandler(hideBusysign);
}

function hideBusysign() {
	var hideBusy = document.getElementById('bysy_indicator').style.display = 'none';
}

function showBusysign() {
	var showBusy = document.getElementById('bysy_indicator').style.display = 'inline';
}

function clickFunc(eventData) {
	var clickedElement = (window.event) ? event.srcElement : eventData.target;
	if ((clickedElement.tagName.toUpperCase() == 'A' 
        && ((clickedElement.target == null) || (clickedElement.target.length <= 0))
        && (clickedElement.href.lastIndexOf('#') != (clickedElement.href.length-1))
        && (!('nobusy' in clickedElement))
        && (clickedElement.href.indexOf('skype') < 0)
        && (clickedElement.href.indexOf('mailto') < 0)
        && (clickedElement.href.indexOf('WicketAjaxDebug') < 0)
        && (clickedElement.href.lastIndexOf('doc') != (clickedElement.href.length-3))
        && (clickedElement.href.lastIndexOf('csv') != (clickedElement.href.length-3))
        && (clickedElement.href.lastIndexOf('xls') != (clickedElement.href.length-3))
        && (clickedElement.href.lastIndexOf('pdf') != (clickedElement.href.length-3))
        && ((clickedElement.onclick == null) || (clickedElement.onclick.toString().indexOf('window.open') <= 0))
        ) 
    || (clickedElement.parentNode.tagName.toUpperCase() == 'A' 
        && ((clickedElement.parentNode.target == null) || (clickedElement.parentNode.target.length <= 0))
        && (clickedElement.parentNode.href.indexOf('skype') < 0)
        && (clickedElement.parentNode.href.indexOf('mailto') < 0)
        && (clickedElement.parentNode.href.lastIndexOf('#') != (clickedElement.parentNode.href.length-1))
        && (clickedElement.parentNode.href.lastIndexOf('doc') != (clickedElement.parentNode.href.length-3))
        && (clickedElement.parentNode.href.lastIndexOf('csv') != (clickedElement.parentNode.href.length-3))
        && (clickedElement.parentNode.href.lastIndexOf('xls') != (clickedElement.parentNode.href.length-3))
        && (clickedElement.parentNode.href.lastIndexOf('pdf') != (clickedElement.parentNode.href.length-3))
        && ((clickedElement.parentNode.onclick == null) || (clickedElement.parentNode.onclick.toString().indexOf('window.open') <= 0))
        ) 
    || (
       ((clickedElement.onclick == null) 
         || 
         ((clickedElement.onclick.toString().indexOf('confirm') <= 0)
          && (clickedElement.onclick.toString().indexOf('alert') <= 0) 
          && (clickedElement.onclick.toString().indexOf('Wicket.Palette') <= 0)))
       && (clickedElement.tagName.toUpperCase() == 'INPUT' && (clickedElement.type.toUpperCase() == 'BUTTON' 
            || clickedElement.type.toUpperCase() == 'SUBMIT' || clickedElement.type.toUpperCase() == 'IMAGE')
            && (clickedElement.name != 'Download File')
          )
       )
    ) {
    showBusysign();
  }
}