package au.org.theark.core.util;

import java.io.InputStream;
import java.util.Collection;
import java.util.HashSet;

import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.CustomFieldSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.web.component.worksheet.ArkGridCell;

public interface ICustomImportValidator {
	
	/**
	 * 
	 * @param fileInputStream
	 * @param inLength
	 * @param isForPheno
	 * @return
	 * @throws FileFormatException
	 * @throws CustomFieldSystemException
	 */
	public java.util.Collection<String> validateMatrixFileFormat(InputStream fileInputStream, long inLength, boolean isForPheno) throws FileFormatException, CustomFieldSystemException  ;
	/**
	 * 
	 * @param inputStream
	 * @param fileFormat
	 * @param delimChar
	 * @return
	 * @throws FileFormatException 
	 * @throws ArkBaseException 
	 */
	public Collection<String> validateDataDictionaryFileData(InputStream inputStream, String fileFormat, char delimChar) throws FileFormatException, ArkBaseException;
	/**
	 * 
	 * @param inputStream
	 * @param fileFormat
	 * @param delimChar
	 * @return
	 */
	public Collection<String> validateCustomDataMatrixFileFormat(InputStream inputStream, String fileFormat, char delimChar) ;
	
	public HashSet<Integer> getInsertRows() ;

	public void setInsertRows(HashSet<Integer> insertRows);

	public HashSet<Integer> getUpdateRows();

	public void setUpdateRows(HashSet<Integer> updateRows);

	public HashSet<ArkGridCell> getInsertCells() ;

	public void setInsertCells(HashSet<ArkGridCell> insertCells);

	public HashSet<ArkGridCell> getUpdateCells();
	
	public void setUpdateCells(HashSet<ArkGridCell> updateCells);
	
	public HashSet<ArkGridCell> getErrorCells() ;
	
	public void setErrorCells(HashSet<ArkGridCell> errorCells);
	
	public HashSet<ArkGridCell> getWarningCells();
	
	public void setWarningCells(HashSet<ArkGridCell> warningCells);
}
