var sleepCounter = 0;

function findPrinter() {
	var applet = document.jZebra;
	if (applet != null) {
		// Searches for locally installed printer with "zebra" in the name
		applet.findPrinter(document.getElementById("printerField").value);
	}
	monitorFinding();
}

function findPrinters() {
	var applet = document.jZebra;
	if (applet != null) {
		// Searches for locally installed printer with "zebra" in the name
		applet.findPrinter(",");
	}
	monitorFinding2();
}

function printBarcode() {
	var applet = document.jZebra;
	if (applet != null) {
		// Send characters/raw commands to applet using "append"
		// Hint: Carriage Return = \r, New Line = \n, Escape Double Quotes= \"
		applet.append("D14\n");
		applet.append("q457\n");
		applet.append("N\n");
		applet.append("b200,15,D,h3,\"10TST66663H\"\n");
		applet.append("A100,20,0,2,1,1,N,\"10TST\"\n");
		applet.append("A100,40,0,2,1,1,N,\"999\"\n");
		applet.append("A115,60,0,2,1,1,N,\"ZZZ\"\n");
		applet.append("A260,15,0,1,1,2,N,\"10TST66663H\"\n")
		applet.append("A260,55,0,1,1,2,N,\"31/12/2222\"\n");
		applet.append("P1\n");

		// Send characters/raw commands to printer
		applet.print();
	}

	monitorPrinting();

	/**
	 * PHP PRINTING: // Uses the php `"echo"` function in conjunction with
	 * jZebra `"append"` function // This assumes you have already assigned a
	 * value to `"$commands"` with php document.jZebra.append(<?php echo
	 * $commands; ?>);
	 */

	/**
	 * SPECIAL ASCII ENCODING //applet.setEncoding("UTF-8");
	 * applet.setEncoding("Cp1252"); applet.append("\xDA");
	 * applet.append(String.fromCharCode(218)); applet.append(chr(218));
	 */

}

function printZebraBarcode(barcodeString) {
	var applet = document.jZebra;
	if (applet != null) {
		// Send characters/raw commands to applet using "append"
		applet.append(unescape(barcodeString));
		// Send characters/raw commands to printer
		applet.print();
	}

	monitorPrinting();

	/**
	 * PHP PRINTING: // Uses the php `"echo"` function in conjunction with
	 * jZebra `"append"` function // This assumes you have already assigned a
	 * value to `"$commands"` with php document.jZebra.append(<?php echo
	 * $commands; ?>);
	 */

	/**
	 * SPECIAL ASCII ENCODING //applet.setEncoding("UTF-8");
	 * applet.setEncoding("Cp1252"); applet.append("\xDA");
	 * applet.append(String.fromCharCode(218)); applet.append(chr(218));
	 */

}

function print64() {
	var applet = document.jZebra;
	if (applet != null) {
		// Use jZebra's `"append64"` function. This will automatically convert
		// provided
		// base64 encoded text into ascii/bytes, etc.
		applet
				.append64("QTU5MCwxNjAwLDIsMywxLDEsTiwialplYnJhIHNhbXBsZS5odG1sIgpBNTkwLDE1NzAsMiwzLDEsMSxOLCJUZXN0aW5nIHRoZSBwcmludDY0KCkgZnVuY3Rpb24iClAxCg==");

		// Send characters/raw commands to printer
		applet.print();
	}
	monitorPrinting();
}

function printPages() {
	var applet = document.jZebra;
	if (applet != null) {
		applet.append("A590,1600,2,3,1,1,N,\"jZebra 1\"\n");
		applet
				.append("A590,1570,2,3,1,1,N,\"Testing the printPages() function\"\n");
		applet.append("P1\n");

		applet.append("A590,1600,2,3,1,1,N,\"jZebra 2\"\n");
		applet
				.append("A590,1570,2,3,1,1,N,\"Testing the printPages() function\"\n");
		applet.append("P1\n");

		applet.append("A590,1600,2,3,1,1,N,\"jZebra 3\"\n");
		applet
				.append("A590,1570,2,3,1,1,N,\"Testing the printPages() function\"\n");
		applet.append("P1\n");

		applet.append("A590,1600,2,3,1,1,N,\"jZebra 4\"\n");
		applet
				.append("A590,1570,2,3,1,1,N,\"Testing the printPages() function\"\n");
		applet.append("P1\n");

		applet.append("A590,1600,2,3,1,1,N,\"jZebra 5\"\n");
		applet
				.append("A590,1570,2,3,1,1,N,\"Testing the printPages() function\"\n");
		applet.append("P1\n");

		applet.append("A590,1600,2,3,1,1,N,\"jZebra 6\"\n");
		applet
				.append("A590,1570,2,3,1,1,N,\"Testing the printPages() function\"\n");
		applet.append("P1\n");

		applet.append("A590,1600,2,3,1,1,N,\"jZebra 7\"\n");
		applet
				.append("A590,1570,2,3,1,1,N,\"Testing the printPages() function\"\n");
		applet.append("P1\n");

		applet.append("A590,1600,2,3,1,1,N,\"jZebra 8\"\n");
		applet
				.append("A590,1570,2,3,1,1,N,\"Testing the printPages() function\"\n");
		applet.append("P1\n");

		// Mark the end of a label, in this case P1 plus a newline character
		// jZebra knows to look for this and treat this as the end of a "page"
		// for better control of larger spooled jobs (i.e. 50+ labels)
		applet.setEndOfDocument("P1\n");

		// The amount of labels to spool to the printer at a time. When
		// jZebra counts this many `EndOfDocument`'s, a new print job will
		// automatically be spooled to the printer and counting will start
		// over.
		applet.setDocumentsPerSpool("3");

		// Send characters/raw commands to printer
		applet.print();

	}
	monitorPrinting();
}

function printXML() {
	var applet = document.jZebra;
	if (applet != null) {
		// Appends the contents of an XML file from a SOAP response, etc.
		// a valid relative URL or a valid complete URL is required for the XML
		// file. The second parameter must be a valid XML tag/node containing
		// base64 encoded data, i.e. <node_1>aGVsbG8gd29ybGQ=</node_1>
		// Example:
		// applet.appendXML("http://yoursite.com/zpl.xml", "node_1");
		// applet.appendXML("http://justtesting.biz/jZebra/dist/epl.xml",
		// "v7:Image");
		applet.appendXML(window.location.href + "/../zpl.xml", "v7:Image");

		// Send characters/raw commands to printer
		// applet.print(); // Can't do this yet because of timing issues with
		// XML
	}

	// Monitor the append status of the xml file, prints when appending if
	// finished
	monitorAppending();
}

function printHex() {
	var applet = document.jZebra;
	if (applet != null) {
		// Using jZebra's "append()" function, hexadecimanl data can be sent
		// by using JavaScript's "\x00" notation. i.e. "41 35 39 30 2c ...", etc
		// Example:
		// applet.append("\x41\x35\x39\x30\x2c"); // ...etc
		applet
				.append("\x41\x35\x39\x30\x2c\x31\x36\x30\x30\x2c\x32\x2c\x33\x2c\x31\x2c\x31\x2c\x4e\x2c\x22\x6a\x5a\x65\x62\x72\x61\x20\x73\x61\x6d\x70\x6c\x65\x2e\x68\x74\x6d\x6c\x22\x0A\x41\x35\x39\x30\x2c\x31\x35\x37\x30\x2c\x32\x2c\x33\x2c\x31\x2c\x31\x2c\x4e\x2c\x22\x54\x65\x73\x74\x69\x6e\x67\x20\x74\x68\x65\x20\x70\x72\x69\x6e\x74\x48\x65\x78\x28\x29\x20\x66\x75\x6e\x63\x74\x69\x6f\x6e\x22\x0A\x50\x31\x0A");

		// Send characters/raw commands to printer
		applet.print();

	}

	monitorPrinting();

	/**
	 * CHR/ASCII PRINTING: // Appends CHR(27) + CHR(29) using `"fromCharCode"`
	 * function // CHR(27) is commonly called the "ESCAPE" character
	 * document.jZebra.append(String.fromCharCode(27) +
	 * String.fromCharCode(29));
	 */
}

function printFile() {
	var applet = document.jZebra;
	if (applet != null) {
		// Using jZebra's "appendFile()" function, a file containg your raw
		// EPL/ZPL
		// can be sent directly to the printer
		// Example:
		// applet.appendFile("http://yoursite/zpllabel.txt"); // ...etc
		applet.appendFile(window.location.href + "/../barcode.txt");
		applet.print();
	}

	monitorPrinting();
}

function printImage() {
	var applet = document.jZebra;
	if (applet != null) {
		// Using jZebra's "appendImage()" function, a png, jpeg file
		// can be sent directly to the printer supressing the print dialog
		// Example:
		// applet.appendImage("http://yoursite/logo1.png"); // ...etc

		// Sample only: Searches for locally installed printer with "pdf" in the
		// name
		// Can't use Zebra, because this function needs a PostScript capable
		// printer
		applet.findPrinter("PDF");
		while (!applet.isDoneFinding()) {
			// Wait
		}

		// Sample only: If a PDF printer isn't installed, try the Microsoft XPS
		// Document
		// Writer'
		if (applet.getPrinterName() == null) {
			applet.findPrinter("Microsoft XPS Document Writer");
			while (!applet.isDoneFinding()) {
				// Wait
			}
		}

		// No suitable printer found, exit
		if (applet.getPrinterName() == null) {
			alert("Could not find a suitable printer for images");
			return;
		}

		// Append our image (only one image can be appended per print)
		applet.appendImage(window.location.href + "/../logo.jpg");
	}

	// Very important for images, uses printPS() insetad of print()
	monitorAppending2();
}

function chr(i) {
	return String.fromCharCode(i);
}

function monitorFinding() {
	monitorApplet('isDoneFinding()',
			'alert("Found printer [" + document.jZebra.getPrinter() + "]")',
			'monitor finding job');
}

function monitorPrinting() {
	// "alert\(\"Data sent to printer successfully\"\)"
	monitorApplet(
			"isDonePrinting()",
			'alert("Data sent to printer [" + document.jZebra.getPrinter() + "] successfully.")',
			"monitor printing job");
}

/**
 * Monitors the Java applet until it is complete with the specified function
 * appletFunction: should return a "true" or "false" finishedFunction: will be
 * called if the function completes without error description: optional
 * description for errors, etc
 * 
 * Example: monitorApplet('isDoneFinding()', 'alert(\\"Success\\")', '');
 */
function monitorApplet(appletFunction, finishedFunction, description) {
	var NOT_LOADED = "jZebra hasn't loaded yet.";
	var INVALID_FUNCTION = 'jZebra does not recognize function: "' + appletFunction;
	+'"';
	var INVALID_PRINTER = "jZebra could not find the specified printer";
	if (document.jZebra != null) {
		var finished = false;
		try {
			finished = eval('document.jZebra.' + appletFunction);
		} catch (err) {
			alert('jZebra Exception:  ' + INVALID_FUNCTION);
			return;
		}
		if (!finished) {
			window.setTimeout('monitorApplet("' + appletFunction + '", "'
					+ finishedFunction.replace(/\"/g, '\\"') + '", "'
					+ description + '")', 100);
		} else {
			var p = document.jZebra.getPrinterName();
			if (p == null) {
				alert("jZebra Exception:  " + INVALID_PRINTER);
				return;
			}
			var e = document.jZebra.getException();
			if (e != null) {
				var desc = description == "" ? "" : " [" + description + "] ";
				alert("jZebra Exception: " + desc
						+ document.jZebra.getExceptionMessage());
				document.jZebra.clearException();
			} else {
				eval(finishedFunction);
			}
		}
	} else {
		alert("jZebra Exception:  " + NOT_LOADED);
	}
}

/*
 * function monitorPrinting() { var applet = document.jZebra; if (applet !=
 * null) { if (!applet.isDonePrinting()) {
 * window.setTimeout('monitorPrinting()', 100); } else { var e =
 * applet.getException(); alert(e == null ? "Printed Successfully" : "Exception
 * occured: " + e.getLocalizedMessage()); } } else { alert("Applet not
 * loaded!"); } }
 */

/*
 * function monitorFinding() { var applet = document.jZebra; if (applet != null) {
 * if (!applet.isDoneFinding()) { window.setTimeout('monitorFinding()', 100); }
 * else { var printer = applet.getPrinterName(); alert(printer == null ?
 * "Printer not found" : "Printer \"" + printer + "\" found"); } } else {
 * alert("Applet not loaded!"); } }
 */

function monitorFinding2() {
	var applet = document.jZebra;
	if (applet != null) {
		if (!applet.isDoneFinding()) {
			window.setTimeout('monitorFinding2()', 100);
		} else {
			var listing = applet.getPrinters();
			var printers = listing.split(',');
			for ( var i in printers) {
				document.getElementById("printersList").options[i] = new Option(
						printers[i]);
				// alert(printers[i]);
			}
		}
	} else {
		alert("Applet not loaded!");
	}
}

function monitorAppending() {
	var applet = document.jZebra;
	if (applet != null) {
		if (!applet.isDoneAppending()) {
			window.setTimeout('monitorAppending()', 100);
		} else {
			applet.print(); // Don't print until all of the data has been
			// appended
			monitorPrinting();
		}
	} else {
		alert("Applet not loaded!");
	}
}

function monitorAppending2() {
	var applet = document.jZebra;
	if (applet != null) {
		if (!applet.isDoneAppending()) {
			window.setTimeout('monitorAppending2()', 100);
		} else {
			applet.printPS(); // Don't print until all of the image data has
			// been appended
			monitorPrinting();
		}
	} else {
		alert("Applet not loaded!");
	}
}

function monitorLoading() {
	var applet = document.jZebra;
	if (document.jZebra != null) {
		try {
			if (document.jZebra.isActive()) {
				document.getElementById("version").innerHTML = "<strong>Status:</strong>  jZebra "
						+ applet.getVersion() + " loaded.";
				if (navigator.appName == "Microsoft Internet Explorer") { // IE
					// Fix
					document.getElementById("logo").src = "http://jzebra.googlecode.com/files/logo_small.png"
				} else { // Use embedded logo
					document.getElementById("logo").src = "data:image/png;base64,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";
				}
			}
		} catch (err) {
			// Firefox fix
			window.setTimeout("monitorLoading()", 500);
		}
	} else {
		window.setTimeout("monitorLoading()", 100);
	}
}