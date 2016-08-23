package au.org.theark.phenotypic.util;

import java.io.InputStream;

import jxl.Workbook;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;

public interface IPhenoImporter {
	
	public StringBuffer uploadAndReportMatrixDataDictionaryFile(InputStream fileInputStream, long inLength) throws FileFormatException, ArkSystemException;

	public InputStream convertXlsToCsv(Workbook w);
	
}
