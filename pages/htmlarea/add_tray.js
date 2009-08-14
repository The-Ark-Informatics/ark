function processType() {
	
	var value = dijit.byId('typeSelect').attr('value');
	if (value == '1') {
		dijit.byId('TRAY_strTrayName').attr('value','Auto-generated');
		dijit.byId('TRAY_strTrayName').attr('display','none');
		dijit.byId('studySelect').domNode.style.display="";
	}
	else {
		dijit.byId('TRAY_strTrayName').attr('value','');
		dijit.byId('TRAY_strTrayName').attr('display','');
		dijit.byId('studySelect').domNode.style.display="none";
	}
}