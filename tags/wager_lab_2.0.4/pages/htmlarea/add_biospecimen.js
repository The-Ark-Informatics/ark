
function aliquotdialog() {
    
    var cancel = new LertButton('Cancel', function () {
        
        //do nothing
    });
    
    var submit = new LertButton('Create Aliquot', function () {
        document.biospecimen_form.submit(); return false;
    });
    var parentid = document.biospecimen_form.BIOSPECIMEN_strParentID.value;
    var dnaconc = document.biospecimen_form.BIOSPECIMEN_flDNAConc.value;
    var volume = document.biospecimen_form.BIOSPECIMEN_flQuantity.value;
    var parentVolume = document.biospecimen_form.parentQuantity.value;
    var type = document.biospecimen_form.BIOSPECIMEN_strProcessingType.value;
    var success = - 1;
    if (type != "Processing") {
        var parentConc = document.biospecimen_form.parentDNA.value;
        var quantityreq = volume * dnaconc;
        var volumestockreq = quantityreq / parentConc;
        if (! volume || ! dnaconc) {
            var message = "<b><font color='red'>Volume and DNA concentrations are required values</font></b>";
            var exampleLert = new Lert(
            message,[cancel], {
                defaultButton: cancel,
                icon: 'htmlarea/i/dialog-error.png'
            });
        } else if (parentVolume < volumestockreq) {
            var message = "<b><font color='red'>" +
            "There is insufficient volume of " +
            parentid +
            " to complete this transaction</font></b>";
            
            var exampleLert = new Lert(
            message,[cancel], {
                defaultButton: cancel,
                icon: 'htmlarea/i/dialog-error.png'
            });
        } else {
            var message = '<b>' + volumestockreq.toFixed(2) + ' mls</b> of sample <b>' + parentid +
            '</b> will be used to create the following aliquot <br/>' +
            'Concentration: ' + dnaconc + '<br/> Volume: ' + volume;
            var exampleLert = new Lert(
            message,[cancel, submit], {
                defaultButton: submit,
                icon: 'htmlarea/i/dialog-information.png'
            });
        }
        exampleLert.display();
    } else {
        var message = 'All of sample<i>' + parentid + '</i>' +
        ' will used in processing.';
        var exampleLert = new Lert(
        message,[cancel, submit], {
            defaultButton: submit,
            icon: 'htmlarea/i/dialog-information.png'
        });
        exampleLert.display();
    }
    
    
}

function fillDiv() {
	var parentid = document.biospecimen_form.BIOSPECIMEN_strParentID.value;
	var dnaconc = document.biospecimen_form.BIOSPECIMEN_flDNAConc.value;
	var volume = document.biospecimen_form.BIOSPECIMEN_flQuantity.value;
	var parentVolume = document.biospecimen_form.parentQuantity.value;
	var type = document.biospecimen_form.BIOSPECIMEN_strProcessingType.value;
	var success = - 1;
	if (type != "Processing") {
		var parentConc = document.biospecimen_form.parentDNA.value;
		var quantityreq = volume * dnaconc;
		var volumestockreq = quantityreq / parentConc;
		if (! volume || ! dnaconc) {
			var message = "<b><font color='red'>" +
			"Volume and DNA concentrations are required values</font></b>";
			dijit.byId('submitButton').domNode.style.display="none";
	
		} else if (parentVolume < volumestockreq) {
				var message = "<b><font color='red'>" +
				"There is insufficient volume of " +
				parentid +
				" to complete this transaction</font></b>";
				dijit.byId('submitButton').domNode.style.display="none";
	
		} else {
			var message = '<b>' + volumestockreq.toFixed(2) + ' mls</b> of sample <b>' + parentid +
			'</b> will be used to create the following aliquot <br/>' +
			'Concentration: ' + dnaconc + '<br/> Volume: ' + volume;
			dijit.byId('submitButton').domNode.style.display="";
		}
	} else {
	var message = 'All of sample <b>' + parentid + '</b>' +
	' will used in processing.';
	dijit.byId('submitButton').domNode.style.display="";
	}
	dijit.byId('dialog1pane').attr('content',message);
	}