var sleepCounter = 0;

function findPrinter() {
	var applet = document.jZebra;
	if (applet != null) {
		// Searches for locally installed printer with "zebra" in the name
		applet.findPrinter("zebra");
	}

	monitorFinding();
}

function findPrinters() {
	var applet = document.jZebra;
	if (applet != null) {
		// Searches for locally installed printer with "zebra" in the name
		applet.findPrinter("{dummy printer name for listing}");
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

function monitorPrinting() {
	var applet = document.jZebra;
	if (applet != null) {
		if (!applet.isDonePrinting()) {
			window.setTimeout('monitorPrinting()', 100);
		} else {
			var e = applet.getException();
			alert(e == null ? "Printed Successfully" : "Exception occured: "
					+ e.getLocalizedMessage());
		}
	} else {
		alert("Applet not loaded!");
	}
}

function monitorFinding() {
	var applet = document.jZebra;
	if (applet != null) {
		if (!applet.isDoneFinding()) {
			window.setTimeout('monitorFinding()', 100);
		} else {
			var printer = applet.getPrinterName();
			alert(printer == null ? "Printer not found" : "Printer \""
					+ printer + "\" found");
		}
	} else {
		alert("Applet not loaded!");
	}
}

function monitorFinding2() {
	var applet = document.jZebra;
	if (applet != null) {
		if (!applet.isDoneFinding()) {
			window.setTimeout('monitorFinding2()', 100);
		} else {
			var printersCSV = applet.getPrinters();
			var printers = printersCSV.split(",");
			for (p in printers) {
				alert(printers[p]);
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