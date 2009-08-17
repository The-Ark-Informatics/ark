package org.wager.dataimport;

import java.io.IOException;
import java.io.InputStream;


public interface IDataImportStrategy {

	public void importData(InputStream is, String recordSeparator);
	
	public void process();
	
	public void verify();
	
	public String getErrorXML();
	
	public String getOutputXML();
	 public String getOutput();
	
}
