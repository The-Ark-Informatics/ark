package org.wager.dataimport;
import java.util.HashMap;
import java.util.List;

import org.wager.biogenix.types.Biospecimen;

public interface IDataImportStrategy {

	public void importData(String importData[][]);
	
	public void process();
	
	public String getErrorXML();
	
	public String getOutputXML();
	
	
}
