/*******************************************************************************
 * Copyright (c) 2011  University of Western Australia. All rights reserved.
 * 
 * This file is part of The Ark.
 * 
 * The Ark is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 3
 * of the License, or (at your option) any later version.
 * 
 * The Ark is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 * 
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 ******************************************************************************/
package au.org.theark.study.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.DataConversionAndManipulationHelper;
import au.org.theark.core.util.XLStoCSV;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.worksheet.ArkGridCell;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * SubjectUploadValidator provides support for validating subject matrix-formatted files.
 * 
 * @author cellis
 */
public class SubjectUploadValidator {
	private static final long		serialVersionUID			= -1933045886948087734L;
	private static Logger			log							= LoggerFactory.getLogger(SubjectUploadValidator.class);
	@SuppressWarnings("unchecked")
	private IArkCommonService		iArkCommonService;
	private Long						studyId;
	private Study						study;
	java.util.Collection<String>	fileValidationMessages	= new java.util.ArrayList<String>();
	java.util.Collection<String>	dataValidationMessages	= new java.util.ArrayList<String>();
	private HashSet<Integer>		insertRows;
	private HashSet<Integer>		updateRows;
	private HashSet<ArkGridCell>	errorCells;
	private SimpleDateFormat		simpleDateFormat			= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private long						subjectCount;
	private char						delimiterCharacter		= au.org.theark.core.Constants.DEFAULT_DELIMITER_CHARACTER;
	private String						fileFormat					= au.org.theark.core.Constants.DEFAULT_FILE_FORMAT;
	private int							row							= 1;

	public SubjectUploadValidator() {
		super();
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		this.study = iArkCommonService.getStudy(studyId);
		this.insertRows = new HashSet<Integer>();
		this.updateRows = new HashSet<Integer>();
		this.errorCells = new HashSet<ArkGridCell>();
		simpleDateFormat.setLenient(false);
	}

	public SubjectUploadValidator(Study study) {
		super();
		this.study = study;
		this.insertRows = new HashSet<Integer>();
		this.updateRows = new HashSet<Integer>();
		this.errorCells = new HashSet<ArkGridCell>();
		simpleDateFormat.setLenient(false);
	}

	@SuppressWarnings("unchecked")
	public SubjectUploadValidator(IArkCommonService iArkCommonService) {
		super();
		this.iArkCommonService = iArkCommonService;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		this.study = iArkCommonService.getStudy(studyId);
		this.insertRows = new HashSet<Integer>();
		this.updateRows = new HashSet<Integer>();
		this.errorCells = new HashSet<ArkGridCell>();
		simpleDateFormat.setLenient(false);
	}

	public java.util.Collection<String> getFileValidationMessages() {
		return fileValidationMessages;
	}

	public void setFileValidationMessages(java.util.Collection<String> fileValidationMessages) {
		this.fileValidationMessages = fileValidationMessages;
	}

	public java.util.Collection<String> getDataValidationMessages() {
		return dataValidationMessages;
	}

	public void setDataValidationMessages(java.util.Collection<String> dataValidationMessages) {
		this.dataValidationMessages = dataValidationMessages;
	}

	public Long getStudyId() {
		return studyId;
	}

	public void setStudyId(Long studyId) {
		this.studyId = studyId;
	}

	public Study getStudy() {
		return study;
	}

	public void setStudy(Study study) {
		this.study = study;
	}

	public HashSet<Integer> getInsertRows() {
		return insertRows;
	}

	public void setInsertRows(HashSet<Integer> insertRows) {
		this.insertRows = insertRows;
	}

	public HashSet<Integer> getUpdateRows() {
		return updateRows;
	}

	public void setUpdateRows(HashSet<Integer> updateRows) {
		this.updateRows = updateRows;
	}

	public HashSet<ArkGridCell> getErrorCells() {
		return errorCells;
	}

	public void setErrorCells(HashSet<ArkGridCell> errorCells) {
		this.errorCells = errorCells;
	}

	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN... Where N is any number of columns
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validateSubjectFileFormat(UploadVO uploadVo) {
		java.util.Collection<String> validationMessages = null;
		try {
			InputStream inputStream = uploadVo.getFileUpload().getInputStream();
			String filename = uploadVo.getFileUpload().getClientFileName();
			fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			delimiterCharacter = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();
			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					delimiterCharacter = ',';
					XLStoCSV xlsToCsv = new XLStoCSV(delimiterCharacter);
					inputStream = xlsToCsv.convertXlsToCsv(w);
					inputStream.reset();
					validationMessages = validateSubjectMatrixFileFormat(inputStream, inputStream.toString().length(),fileFormat, delimiterCharacter);
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
				catch (IOException e) {
					log.error(e.getMessage());
				}catch (ArkBaseException e) {
					log.error(au.org.theark.study.web.Constants.ARK_BASE_EXCEPTION + e);
				}
				
			}
			//validationMessages = validateSubjectFileFormat(inputStream, fileFormat, delimiterCharacter);
			/*try {
				validationMessages = validateSubjectMatrixFileFormat(inputStream, inputStream.toString().length(),fileFormat, delimiterCharacter);
			} catch (ArkBaseException e) {
				log.error(au.org.theark.study.web.Constants.ARK_BASE_EXCEPTION + e);
			}
			*/
		}catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}

	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN... Where N is any number of columns
	 * 
	 * @param inputStream
	 *           is the input stream of the file
	 * @param fileFormat
	 *           is the file format (eg txt)
	 * @param delimChar
	 *           is the delimiter character of the file (eg comma)
	 * @return a collection of validation messages
	 */
	/*private Collection<String> validateSubjectFileFormat(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;

		try {
			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					delimiterCharacter = ',';
					XLStoCSV xlsToCsv = new XLStoCSV(delimiterCharacter);
					inputStream = xlsToCsv.convertXlsToCsv(w);
					inputStream.reset();
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
				catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			validationMessages = validateSubjectMatrixFileFormat(inputStream, inputStream.toString().length(), fileFormat, delimChar);
		}
		catch (FileFormatException ffe) {
			log.error(au.org.theark.study.web.Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(au.org.theark.study.web.Constants.ARK_BASE_EXCEPTION + abe);
		}
		return validationMessages;
	}*/

	/**
	 * Validates the file in the default "matrix" file data assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN... Where N is any number of columns
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validateSubjectFileData(UploadVO uploadVo, List<String> uidsToUpdateReference) {
		java.util.Collection<String> validationMessages = null;
		try {
			InputStream inputStream = uploadVo.getFileUpload().getInputStream();
			String filename = uploadVo.getFileUpload().getClientFileName();
			fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			delimiterCharacter = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();

			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					delimiterCharacter = ',';
					XLStoCSV xlsToCsv = new XLStoCSV(delimiterCharacter);
					inputStream = xlsToCsv.convertXlsToCsv(w);
					inputStream.reset();
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
			}

			validationMessages = validateSubjectFileData(inputStream, fileFormat, delimiterCharacter, uidsToUpdateReference);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}

	private Collection<String> validateSubjectFileData(InputStream inputStream, String fileFormat, char delimChar, List<String> uidsToUpdateRefence) {
		java.util.Collection<String> validationMessages = null;

		try {
			// TODO performance of valdation now approx 60-90K records per minute, file creation after validation doubles that
			// I think this is acceptable for now to keep in user interface. Can make some slight improvements though, and if it bloats with more fields
			// could be part of batch too
			validationMessages = validateMatrixSubjectFileData(inputStream, inputStream.toString().length(), fileFormat, delimChar, Long.MAX_VALUE, uidsToUpdateRefence);
		}
		catch (FileFormatException ffe) {
			log.error(au.org.theark.study.web.Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(au.org.theark.study.web.Constants.ARK_BASE_EXCEPTION + abe);
		}
		return validationMessages;
	}

	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of a file
	 * @throws FileFormatException
	 *            file format Exception
	 * @throws ArkBaseException
	 *            general ARK Exception
	 * @return a collection of file format validation messages
	 */
	public java.util.Collection<String> validateSubjectMatrixFileFormat(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkBaseException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 0;
		long srcLength = -1;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			csvReader.readHeaders();// can not check this..
			
			srcLength = inLength - csvReader.getHeaders().toString().length();
			String[] fileHeaderColumnArray = csvReader.getHeaders();

			//List<String> subjectColumns = new ArrayList<String>();
			String[] requiredHeaderColumnArray = au.org.theark.study.web.Constants.SUBJECT_TEMPLATE_HEADER;
			boolean headerError = false;
			/*for (int i = 0; i < subjectHeaderColumnArray.length; i++) {
				String colName = subjectHeaderColumnArray[i];
				subjectColumns.add(colName);
			}*/
			for (int i = 0; i < fileHeaderColumnArray.length; i++) {
				String colName = fileHeaderColumnArray[i];
				if (!Arrays.asList(requiredHeaderColumnArray).contains(colName) ) {
					headerError = true;
					break;
				}
			}
			if (headerError) {
				StringBuffer stringBuffer = new StringBuffer();
				// TODO ASAP : this should utilize the file that creates the template/requirements!
				stringBuffer.append("Error: The specified file does not appear to conform to the expected file format.\n");
				stringBuffer.append("Please refer to the template, as seen on step one, for the correct format. \n");
					fileValidationMessages.add(stringBuffer.toString());
				for (int i = 0; i < fileHeaderColumnArray.length; i++) {
					if (!Arrays.asList(requiredHeaderColumnArray).contains(fileHeaderColumnArray[i])) {
						fileValidationMessages.add("Error: The column name " + fileHeaderColumnArray[i] + " is not a valid column name.");
					}
				}
			}
			row = 1;
		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("An unexpected I/O exception occurred whilst reading the subject data file");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("An unexpected exception occurred when trying to process subject data file");
		}
		finally {

			if (csvReader != null) {
				try {
					csvReader.close();
				}
				catch (Exception ex) {
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null) {
				try {
					// TODO ASAP : re-evaluate below
					inputStreamReader.close();
				}
				catch (Exception ex) {
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
		}

		return fileValidationMessages;
	}

	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN...
	 * 
	 * TODO: remove globals unless their is a legit reason
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of a file
	 * @param rowsToValidate
	 *           validate the number of rows specified (or as many as exist, if that number is greater).
	 * @throws FileFormatException
	 *            file format Exception
	 * @throws ArkBaseException
	 *            general ARK Exception
	 * @return a collection of data validation messages
	 */
	public java.util.Collection<String> validateMatrixSubjectFileData(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr, long rowsToValidate,
			List<String> uidsToUpdateReferenceToBeUpdated) throws FileFormatException, ArkSystemException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 1;
		long srcLength = -1L;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		
		

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			csvReader.readHeaders();
			srcLength = inLength - csvReader.getHeaders().toString().length();
			String[] fieldNameArray = csvReader.getHeaders();
			boolean isAutoGen = study.getAutoGenerateSubjectUid();

			List<String> subjectUIDsAlreadyExisting = iArkCommonService.getAllSubjectUIDs(study); // TODO evaluate data in future to know if should get
																																// all id's in the csv, rather than getting all id's
																																// in study to compre
			List<ConsentStatus> consentStatusList = iArkCommonService.getConsentStatus();
			List<ConsentType> consentTypeList = iArkCommonService.getConsentType();
			List<ConsentOption> consentOptionList = iArkCommonService.getConsentOptionList();

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				stringLineArray = new String[fieldNameArray.length];
				stringLineArray = csvReader.getValues();
				// First/0th column should be the SubjectUID
				String subjectUID = stringLineArray[0];

				boolean hasSomeData = false;
				for (String next : stringLineArray) {
					if (next != null && !next.isEmpty()) {
						hasSomeData = true;
					}
				}

				if (!isAutoGen && (subjectUID == null || subjectUID.isEmpty())) {

					if (!hasSomeData) {
						// TODO Add some ability to have a lower level info setting...that warns but doesnt require a checkbox
						// etcdataValidationMessages.add("Warning/Info: Row " + row +
						// ":  There appeared to be no data on this row, so we ignored this line");
						row++;
					}
					else {
						dataValidationMessages.add("Error: Row " + row + ":  There is no subject UID on this row, "
								+ "yet the study is not set up to auto generate subject UIDs.  Please remove this line or provide an ID");
						errorCells.add(new ArkGridCell(0, row));
						row++;
					}
				}
				else if (isAutoGen && (subjectUID == null || subjectUID.isEmpty()) && !hasSomeData) {
					// TODO Add some ability to have a lower level info setting...that warns but doesnt require a checkbox etc
					// dataValidationMessages.add("Warning/Info: Row " + row + ":  There appeared to be no data on this row, so we ignored this line");
					row++;
				}
				else {

					boolean isUpdate = subjectUIDsAlreadyExisting.contains(subjectUID);

					if (isUpdate) {
						updateRows.add(row);
						uidsToUpdateReferenceToBeUpdated.add(subjectUID);
					}
					else {
						insertRows.add(row);
					}

					int col = 0;
					String dateStr = new String();
					String cellValue = new String();

					if (csvReader.getIndex("DATE_OF_BIRTH") > 0 || csvReader.getIndex("DOB") > 0) {
						if (csvReader.getIndex("DATE_OF_BIRTH") > 0) {
							col = csvReader.getIndex("DATE_OF_BIRTH");
							cellValue = csvReader.get("DATE_OF_BIRTH");
						}
						else {
							col = csvReader.getIndex("DOB");
							cellValue = csvReader.get("DOB");
						}
						try {
							dateStr = cellValue;
							if (dateStr != null && dateStr.length() > 0)
								simpleDateFormat.parse(dateStr);
						}
						catch (ParseException pex) {
							dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue + " is not in the valid date format of: "
									+ Constants.DD_MM_YYYY.toLowerCase());
							errorCells.add(new ArkGridCell(col, row));
						}
					}

					if (csvReader.getIndex("DATE_OF_DEATH") > 0 || csvReader.getIndex("DODEATH") > 0) {
						if (csvReader.getIndex("DATE_OF_DEATH") > 0) {
							col = csvReader.getIndex("DATE_OF_DEATH");
							cellValue = csvReader.get("DATE_OF_DEATH");
						}
						else {
							col = csvReader.getIndex("DODEATH");
							cellValue = csvReader.get("DODEATH");
						}
						try {
							dateStr = cellValue;
							if (dateStr != null && dateStr.length() > 0)
								simpleDateFormat.parse(dateStr);
						}
						catch (ParseException pex) {
							dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue + " is not in the valid date format of: "
									+ Constants.DD_MM_YYYY.toLowerCase());
							errorCells.add(new ArkGridCell(col, row));
						}
					}


					if (csvReader.getIndex("DATE_LAST_KNOWN_ALIVE") > 0 || csvReader.getIndex("LAST_KNOWN_ALIVE") > 0) {
						if (csvReader.getIndex("DATE_LAST_KNOWN_ALIVE") > 0) {
							col = csvReader.getIndex("DATE_LAST_KNOWN_ALIVE");
							cellValue = csvReader.get("DATE_LAST_KNOWN_ALIVE");
						}
						else {
							col = csvReader.getIndex("LAST_KNOWN_ALIVE");
							cellValue = csvReader.get("LAST_KNOWN_ALIVE");
						}
						try {
							dateStr = cellValue;
							if (dateStr != null && dateStr.length() > 0)
								simpleDateFormat.parse(dateStr);
						}
						catch (ParseException pex) {
							dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + 
									cellValue + " is not in the valid date format of: "
									+ Constants.DD_MM_YYYY.toLowerCase());
							errorCells.add(new ArkGridCell(col, row));
						}
					}

					if (csvReader.getIndex("ADDRESS_DATE_RECEIVED") > 0) {
						col = csvReader.getIndex("ADDRESS_DATE_RECEIVED");
						cellValue = csvReader.get("ADDRESS_DATE_RECEIVED");
						try {
							dateStr = cellValue;
							if (dateStr != null && dateStr.length() > 0)
								simpleDateFormat.parse(dateStr);
						}
						catch (ParseException pex) {
							dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue + " is not in the valid date format of: "
									+ Constants.DD_MM_YYYY.toLowerCase());
							errorCells.add(new ArkGridCell(col, row));
						}
					}

					// BOOLEAN CHECKS
					if (csvReader.getIndex("PHONE_SILENT") > 0) {
						col = csvReader.getIndex("PHONE_SILENT");
						cellValue = csvReader.get("PHONE_SILENT");
						String silent = cellValue;
						if (silent != null && !silent.isEmpty()) {// if null or empty just ignore...if invalid flag
							if (!DataConversionAndManipulationHelper.isSomethingLikeABoolean(silent)) {
								dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue
										+ " is not a valid boolean value.  Please use true or false for this column.");
								errorCells.add(new ArkGridCell(col, row));
							}
						}
					}
					if (csvReader.getIndex("PHONE_IS_PREFERRED") > 0) {
						col = csvReader.getIndex("PHONE_IS_PREFERRED");
						cellValue = csvReader.get("PHONE_IS_PREFERRED");
						String prefer = cellValue;
						if (prefer != null && !prefer.isEmpty()) {// if null or empty just ignore...if invalid flag
							if (prefer != null && !DataConversionAndManipulationHelper.isSomethingLikeABoolean(prefer)) {
								dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue
										+ " is not a valid boolean value. Please use true or false for this column.");
								errorCells.add(new ArkGridCell(col, row));
							}
						}
					}
					if (csvReader.getIndex("ADDRESS_IS_PREFERRED") > 0) {
						col = csvReader.getIndex("ADDRESS_IS_PREFERRED");
						cellValue = csvReader.get("ADDRESS_IS_PREFERRED");
						String prefer = cellValue;
						if (prefer != null && !prefer.isEmpty()) {// if null or empty just ignore...if invalid flag
							if (prefer != null && !DataConversionAndManipulationHelper.isSomethingLikeABoolean(prefer)) {
								dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue
										+ " is not a valid boolean value. Please use true or false for this column.");
								errorCells.add(new ArkGridCell(col, row));
							}
						}
					}

					if (csvReader.getIndex("PHONE_DATE_RECEIVED") > 0) {
						col = csvReader.getIndex("PHONE_DATE_RECEIVED");
						cellValue = csvReader.get("PHONE_DATE_RECEIVED");
						try {
							dateStr = cellValue;
							if (dateStr != null && dateStr.length() > 0)
								simpleDateFormat.parse(dateStr);
						}
						catch (ParseException pex) {
							dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue + " is not in the valid date format of: "
									+ Constants.DD_MM_YYYY.toLowerCase());
							errorCells.add(new ArkGridCell(col, row));
						}
					}
					
					if (csvReader.getIndex("CONSENT_DATE") > 0) {
						col = csvReader.getIndex("CONSENT_DATE");
						cellValue = csvReader.get("CONSENT_DATE");
						try {
							dateStr = cellValue;
							if (dateStr != null && !dateStr.isEmpty())
								simpleDateFormat.parse(dateStr);
						}
						catch (ParseException pex) {
							dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue + " is not in the valid date format of: "
									+ Constants.DD_MM_YYYY.toLowerCase());
							errorCells.add(new ArkGridCell(col, row));
						}
					}
					
					
					if (csvReader.getIndex("CONSENT_STATUS") > 0) {
						boolean validData = true;
						col = csvReader.getIndex("CONSENT_STATUS");
						cellValue = csvReader.get("CONSENT_STATUS");
						
						if(!cellValue.isEmpty()) {
							for(ConsentStatus cs : consentStatusList){
								if(cellValue.equalsIgnoreCase(cs.getName())) {
									validData = true;
									break;
								}
								else {
									validData = false;
								}
							}
							
							if(!validData) {
								dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue + " is not a valid option");
								errorCells.add(new ArkGridCell(col, row));
							}
						}
					}
					
					if (csvReader.getIndex("CONSENT_TYPE") > 0) {
						boolean validData = true;
						col = csvReader.getIndex("CONSENT_TYPE");
						cellValue = csvReader.get("CONSENT_TYPE");
						
						if(!cellValue.isEmpty()) {
							for(ConsentType ct : consentTypeList){
								if(cellValue.equalsIgnoreCase(ct.getName())) {
									validData = true;
									break;
								}
								else {
									validData = false;
								}
							}
							
							if(!validData) {
								dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue + " is not a valid option.");
								errorCells.add(new ArkGridCell(col, row));
							}
						}
					}
					
					if (csvReader.getIndex("CONSENT_TO_PASSIVE_DATA_GATHERING") > 0) {
						boolean validData = true;
						col = csvReader.getIndex("CONSENT_TO_PASSIVE_DATA_GATHERING");
						cellValue = csvReader.get("CONSENT_TO_PASSIVE_DATA_GATHERING");
						
						if(!cellValue.isEmpty()) {
							for(ConsentOption co : consentOptionList){
								if(cellValue.equalsIgnoreCase(co.getName())) {
									validData = true;
									break;
								}
								else {
									validData = false;
								}
							}
							
							if(!validData) {
								dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue + " is not a valid option");
								errorCells.add(new ArkGridCell(col, row));
							}
						}
					}
					
					if (csvReader.getIndex("CONSENT_TO_ACTIVE_CONTACT") > 0) {
						boolean validData = true;
						col = csvReader.getIndex("CONSENT_TO_ACTIVE_CONTACT");
						cellValue = csvReader.get("CONSENT_TO_ACTIVE_CONTACT");
						
						if(!cellValue.isEmpty()) {
							for(ConsentOption co : consentOptionList){
								if(cellValue.equalsIgnoreCase(co.getName())) {
									validData = true;
									break;
								}
								else {
									validData = false;
								}
							}
							
							if(!validData) {
								dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue + " is not a valid option");
								errorCells.add(new ArkGridCell(col, row));
							}
						}
					}
					
					if (csvReader.getIndex("CONSENT_TO_USE_DATA") > 0) {
						boolean validData = true;
						col = csvReader.getIndex("CONSENT_TO_USE_DATA");
						cellValue = csvReader.get("CONSENT_TO_USE_DATA");
						if(!cellValue.isEmpty()) {
							for(ConsentOption co : consentOptionList){
								if(cellValue.equalsIgnoreCase(co.getName())) {
									validData = true;
									break;
								}
								else {
									validData = false;
								}
							}
							
							if(!validData) {
								dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + cellValue + " is not a valid option");
								errorCells.add(new ArkGridCell(col, row));
							}
						}
					}

					subjectCount++;
					row++;
				}
			}

		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("An unexpected I/O exception occurred whilst reading the subject data file.");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("An unexpected exception occurred when trying to process subject data file.");
		}
		finally {
			if (csvReader != null) {
				try {
					csvReader.close();
				}
				catch (Exception ex) {
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null) {
				try {
					inputStreamReader.close();
				}
				catch (Exception ex) {
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
		}

		for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
			Integer i = (Integer) iterator.next();
			dataValidationMessages.add("Warning:  Data on row " + i.intValue() + " exists, please confirm you wish to update an existing Subject.");
		}

		return dataValidationMessages;
	}
}
