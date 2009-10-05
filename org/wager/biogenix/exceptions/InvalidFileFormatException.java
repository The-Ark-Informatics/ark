package org.wager.biogenix.exceptions;

public class InvalidFileFormatException extends Exception {
private String reason;
	
public InvalidFileFormatException(String reason) {
	super();
	this.reason=reason;
	
}

public String getMessage() {
	return this.reason;
}

	
}
