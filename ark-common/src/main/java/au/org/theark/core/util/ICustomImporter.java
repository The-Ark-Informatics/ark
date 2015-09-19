package au.org.theark.core.util;

import java.io.InputStream;
import java.util.List;

import jxl.Workbook;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.CustomFieldUpload;

public interface ICustomImporter {
	
	public StringBuffer uploadAndReportMatrixDataDictionaryFile(InputStream fileInputStream, long inLength) throws FileFormatException, ArkSystemException;

	public InputStream convertXlsToCsv(Workbook w);
	
}
