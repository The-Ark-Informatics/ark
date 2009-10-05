package org.wager.dataimport;


public class ImportStrategyFactory {
	  private static final int MODE_NANODROP = 0;
	  private static final int MODE_ALIQUOT = 1;
	 public static IDataImportStrategy getInstance(int mode)
	    {
	      
	        if (mode == MODE_ALIQUOT) {
	        	return new AliquotStrategy();
	        }
	        else if (mode == MODE_NANODROP) {
	        	return new NanodropStrategy();
	        }
	        
	      
	      return null;
	    }
	
}
