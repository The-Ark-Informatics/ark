var qz_loaded = false;

function setupSigning(certCallback, signCallback) {
    qz.security.setCertificatePromise(function(resolve, reject) {
        $.ajax(certCallback).then(resolve, reject);
    });
    qz.security.setSignaturePromise(function(toSign) {
        return function(resolve,reject) {
            $.post(signCallback, {request: toSign}).then(resolve, reject);
        };
    });
};

function printBarcode(barcodePrinter, data) {

    if(!qz_loaded) {
        qz.websocket.connect().then(function() {
            return qz.printers.find(barcodePrinter);
        }).then(function(printer) {
            print(printer, data);
        });
        qz_loaded = true;
    } else {
        print(barcodePrinter, data);
    }
};

function print(printer, data) {
    var config = qz.configs.create(printer);
    qz.print(config, data).catch(function(e) {
        console.log(e);
    }).then(function() {
        qz.websocket.disconnect();
        qz_loaded=false;
    });
};

function changeHiddenInput(printerList, selectedMarkupId) {
    $("#" + selectedMarkupId).val($("#" + printerList).val());
    callWicket($("#" + selectedMarkupId).val());
}


function findPrinters(printerListMarkupId, selectedPrinterMarkupId) {

    var qz_connected = true;

    qz.websocket.connect({retries:1, delay:0}).then(function() {
        return qz.printers.find();
	}).then(function(all_printers) {
        var printerExists = ($("#" + printerListMarkupId + " option[value='" + $("#" + selectedPrinterMarkupId).val() + "']").length > 0);
	    $('#' + printerListMarkupId).empty();
	    $('#' + printerListMarkupId).append(new Option('Choose One', 'Choose One', printerExists, printerExists));
	    for (i=0; i < all_printers.length; i++) {
	    	console.log(all_printers[i]);
		    var is_printer = (all_printers[i] == $('#' + selectedPrinterMarkupId).value);
		    $('#' + printerListMarkupId).append(new Option(all_printers[i], all_printers[i], is_printer, is_printer));
	    }
	    if(qz_connected) {
	        $("#"+printerListMarkupId).prop("disabled", false);
	    }
        changeHiddenInput(selectedPrinterMarkupId, printerListMarkupId); //used in reverse to copy from hidden to dropdown
    }).then(function() {
	    qz.websocket.disconnect();
	}).catch(function(e) {
	    console.log(e);
	    qz_connected = false;

        if(!qz_connected) {
            var current_hidden_item = $("#" + selectedPrinterMarkupId).val();
            $("#" + printerListMarkupId)[0].append(new Option(current_hidden_item, current_hidden_item, false, false));
            $("#" + printerListMarkupId).val(current_hidden_item);
            $("#" + printerListMarkupId).prop("disabled", true);
        }
	})
};
