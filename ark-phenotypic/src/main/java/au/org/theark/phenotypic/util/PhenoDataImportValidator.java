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
package au.org.theark.phenotypic.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.exception.PhenotypicSystemException;
import au.org.theark.core.model.pheno.entity.PhenoData;
import au.org.theark.core.model.pheno.entity.PhenotypicCollection;
import au.org.theark.core.model.pheno.entity.QuestionnaireStatus;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.CustomFieldGroup;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.worksheet.ArkGridCell;
import au.org.theark.phenotypic.model.vo.PhenoFieldDataUploadVO;
import au.org.theark.phenotypic.service.IPhenotypicService;

import com.csvreader.CsvReader;

/**
 * PhenoDataImportValidator provides support for validating import file before 
 * trying to use PhenoDataImporter to import data into the database
 * 
 * @author cellis
 * @author elam
 */
public class PhenoDataImportValidator {
	static Logger							log							= LoggerFactory.getLogger(PhenoDataImportValidator.class);
	boolean									qualityControl				= false;
	private Study							study;
	private String							fieldName;
	private long							subjectCount;
	private long							fieldCount;
	private long							insertCount;
	private long							updateCount;
	private double							speed;
	private long							curPos;
	private long							srcLength					= -1;																// -1 means nothing being processed
	private StopWatch						timer							= null;
	private char							phenotypicDelimChr		= au.org.theark.core.Constants.IMPORT_DELIM_CHAR_COMMA;						// default phenotypic file
																																						// delimiter: COMMA
	java.util.Collection<String>		fileValidationMessages	= new ArrayList<String>();
	java.util.Collection<String>		importValidationMessages	= new ArrayList<String>();

	private IPhenotypicService			iPhenotypicService		= null;
	private IArkCommonService<Void>	iArkCommonService			= null;
	
	private StringBuffer					uploadReport				= null;
	private HashSet<Integer>			insertRows					= new HashSet<Integer>();
	private HashSet<Integer>			updateRows					= new HashSet<Integer>();
	private HashSet<ArkGridCell>		insertCells					= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell>		updateCells					= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell>		warningCells				= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell>		errorCells					= new HashSet<ArkGridCell>();
	private String							fileFormat					= au.org.theark.core.Constants.DEFAULT_FILE_FORMAT;	// default
	private int								row							= 1;
	private ArkFunction 					arkFunction;
	private CustomFieldGroup			questionnaire;
	private PhenotypicCollection		phenoDataCollection;
	private List<CustomFieldDisplay>	cfdHeaderList;

	/**
	 * PhenotypicValidator constructor
	 * 
	 */

	public PhenoDataImportValidator(IArkCommonService<Void> iArkCommonService, IPhenotypicService iPhenotypicService, 
												PhenoFieldDataUploadVO phenoFieldDataUploadVo) {
		this.iArkCommonService = iArkCommonService;
		this.iPhenotypicService = iPhenotypicService;
		this.arkFunction = phenoFieldDataUploadVo.getQuestionnaire().getArkFunction();
		this.questionnaire = phenoFieldDataUploadVo.getQuestionnaire();

		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (studyId != null) {
			Study study = iArkCommonService.getStudy(studyId);
			this.study = study;
		}
		this.fileValidationMessages = new ArrayList<String>();
		this.importValidationMessages = new ArrayList<String>();

		String filename = phenoFieldDataUploadVo.getFileUpload().getClientFileName();
		this.fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();

		this.phenotypicDelimChr = phenoFieldDataUploadVo.getUpload().getDelimiterType().getDelimiterCharacter();
			
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
	public Collection<String> validateMatrixPhenoDataFileFormat(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;
		cfdHeaderList = new ArrayList<CustomFieldDisplay>();

		try {
			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					inputStream = convertXlsToCsv(w);
					inputStream.reset();
					phenotypicDelimChr = ',';
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
				catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			validationMessages = validatePhenoDataImportFileFormat(inputStream, inputStream.toString().length());
		}
		catch (FileFormatException ffe) {
			log.error("FILE_FORMAT_EXCPEPTION: " + ffe);
		}
		catch (ArkBaseException abe) {
			log.error("ARK_BASE_EXCEPTION: " + abe);
		}
		return validationMessages;
	}
	
	/**
	 * Validates the phenotypic data file in the default "matrix" file format assumed: SUBJECTUID,DATE_COLLECTED,QUESTIONNAIRE_STATUS,FIELD1,FIELD2,FIELDN...
	 * NB: A new PhenotypicCollection (questionnaire instance) is created per row
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @throws IOException
	 *            input/output Exception
	 * @throws OutOfMemoryError
	 *            out of memory Exception
	 */
	public java.util.Collection<String> validatePhenoDataImportFileFormat(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException {
		curPos = 0;
		cfdHeaderList = new ArrayList<CustomFieldDisplay>();
		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		Date dateCollected = new Date();

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, phenotypicDelimChr);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTUID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			String[] fieldNameArray = csvReader.getHeaders();
			
			boolean headerError = false;
			boolean databaseIntegrityError = false;

			int totalCols = fieldNameArray.length;
			if (totalCols <= 3) {
				headerError = true;
			}

			int col = 0;
			if (headerError) {
				errorCells.add(new ArkGridCell(col, row));
			}
			else {
				if (!StringUtils.equalsIgnoreCase(fieldNameArray[col], "SUBJECTUID")) {
					headerError = true;
					errorCells.add(new ArkGridCell(col, row));
				}
				col++;
				if (!StringUtils.equalsIgnoreCase(fieldNameArray[col], "DATE_COLLECTED")) {
					headerError = true;
					errorCells.add(new ArkGridCell(col, row));
				}
				col++;
				if (!StringUtils.equalsIgnoreCase(fieldNameArray[col], "QUESTIONNAIRE_STATUS")) {
					headerError = true;
					errorCells.add(new ArkGridCell(col, row));
				}
			}

			if (!headerError) {
				try {
					for (col = 3; col < totalCols; col++) {
						CustomField cfCriteria = iArkCommonService.getCustomFieldByNameStudyArkFunction(fieldNameArray[col], study, arkFunction);
						if (cfCriteria == null)
						{
							headerError = true;
							errorCells.add(new ArkGridCell(col, row));
							cfdHeaderList.add(null);
						}
						else {
							CustomFieldDisplay cfdCheck = iArkCommonService.getCustomFieldDisplayByCustomField(cfCriteria, this.questionnaire);
							if (cfdCheck == null) {
								headerError = true;
								errorCells.add(new ArkGridCell(col, row));
							}
							cfdHeaderList.add(cfdCheck);
						}
					}				
				}
				catch (HibernateException he) {
					// if results are non-unique something is terribly wrong with the database
					databaseIntegrityError = true;
					errorCells.add(new ArkGridCell(col, row));
				}
			}
			
			if (databaseIntegrityError) {
				// Database somehow has incorrect data
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("Database integrity error - Did not obtain a unique result for the specified field name: ");
				stringBuffer.append(fieldNameArray[col]);
				stringBuffer.append("\n for the given study in-context or the selected questionnaire: ");
				stringBuffer.append(this.questionnaire.getName());
				stringBuffer.append("\n");
				stringBuffer.append("Please contact your system administrator ASAP\n");
				importValidationMessages.add(stringBuffer.toString());
			}
			else if (headerError) {
				// Invalid file format
				StringBuffer stringBuffer = new StringBuffer();
				String delimiterTypeName = iArkCommonService.getDelimiterTypeNameByDelimiterChar(phenotypicDelimChr);

				stringBuffer.append("The specified file does not appear to conform to the expected data dictionary file format.\n");
				stringBuffer.append("The specified file format was: " + fileFormat + "\n");
				stringBuffer.append("The specified delimiter was: [" + phenotypicDelimChr + "] (" + delimiterTypeName + ")\n");
				if (col >= 3) {
					for (int i = 0; i < cfdHeaderList.size(); i++ ) {
						if (cfdHeaderList.get(i) == null) {
							stringBuffer.append("Error encountered in header columns due to bad field header: " + fieldNameArray[i+3] + " at column number:" + (i+3) + "\n");
							stringBuffer.append("(i.e. does not match any of the valid field names for questionnaire: " + this.questionnaire.getName() + ")\n");
						}
					}
				}
				else {
					stringBuffer.append("Error in the required header columns. A mockup of the expected pheno data import format is as follows\n");
					stringBuffer.append(" (where DummyField# should actually be the field names for the give questionnaire):\n");
					stringBuffer.append("SUBJECTUID" + phenotypicDelimChr);
					stringBuffer.append("DATE_COLLECTED" + phenotypicDelimChr);
					stringBuffer.append("STATUS" + phenotypicDelimChr);
					for (int i = 0; i < 3; i++) {
						if (i > 0) {
							stringBuffer.append(phenotypicDelimChr);
						}
						if (i < 2) {
							stringBuffer.append("DummyField" + (i+1));
						}
						else {
							stringBuffer.append("..." + phenotypicDelimChr);
							stringBuffer.append("DummyFieldN");
						}
					}
					stringBuffer.append("\n");
					stringBuffer.append("Subject001" + phenotypicDelimChr + "31/12/2011" + phenotypicDelimChr);
					for (int i = 0; i < 3; i++) {
						if (i > 0) {
							stringBuffer.append(phenotypicDelimChr);
						}
						if (i < 2) {
							stringBuffer.append("[...]");
						}
						else {
							stringBuffer.append("..." + phenotypicDelimChr);
							stringBuffer.append("[...]");
						}
					}
				}
				importValidationMessages.add(stringBuffer.toString());
			}
			else {
				// Field count = column count - 3 (SUBJECTUID, DATE_COLLECTED and STATUS)
				fieldCount = fieldNameArray.length - 3;
	
				int row = 1;
				// Loop through all rows in file
				while (csvReader.readRecord()) {
					// do something with the newline to put the data into
					// the variables defined above
					stringLineArray = csvReader.getValues();
	
					// Fist column should be SubjectUID
					col = 0;
					String subjectUid = stringLineArray[col];
					// Check subject exists
					LinkSubjectStudy linkSubjectStudy = null;
					try {
						linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUid, study);
					}
					catch (au.org.theark.core.exception.EntityNotFoundException enfe) {
						// Subject not found...error
						errorCells.add(new ArkGridCell(col, row));
						importValidationMessages.add(PhenotypicValidationMessage.fieldDataSubjectUidNotFound(subjectUid));
					}
	
					// Second/1th column should be date collected
					col++;
					String dateCollectedStr = stringLineArray[col];
					// Check date collected is valid
					try {
						DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY_HH_MM_SS);
						dateFormat.setLenient(false);
						if (dateCollectedStr.length() == 10) {
							dateCollectedStr = dateCollectedStr + " 00:00:00";
						}
						dateCollected = dateFormat.parse(dateCollectedStr);
					}
					catch (ParseException pe) {
						importValidationMessages.add(PhenotypicValidationMessage.dateCollectedNotValidDate(subjectUid, dateCollectedStr));
						errorCells.add(new ArkGridCell(col, row));
					}
	
					// Third column should be status
					col++;
					String statusName = stringLineArray[col];
					// Check the status is valid
					QuestionnaireStatus status = null;
					if (StringUtils.isBlank(statusName)) {
						// if blank retrieve default status 
						status = iPhenotypicService.getDefaultPhenotypicCollectionStatus();
					}
					else {
						try {
							status = iPhenotypicService.getPhenotypicCollectionStatusByName(statusName);
						}
						catch (HibernateException he) {
							status = null;
						}
					}
					if (status == null) {
						importValidationMessages.add(PhenotypicValidationMessage.statusNotValid(subjectUid, statusName));
						errorCells.add(new ArkGridCell(col, row));
					}
					
					/* 
					 * Always Insert records - never update (thus never have to look up existing PhenotypicCollection based on dateCollected) 
					 */

					// Loop through columns in current row in file, starting from array index 3
					for (col = 3; col < totalCols; col++) {
						// Validation should be ok, so just pull the cfd from our list 
						CustomFieldDisplay cfd = cfdHeaderList.get(col - 3);

						PhenoData fieldData = new PhenoData();
						PhenotypicCollection phenotypicCollection = new PhenotypicCollection();
						phenotypicCollection.setLinkSubjectStudy(linkSubjectStudy);
						phenotypicCollection.setRecordDate(dateCollected);
						phenotypicCollection.setStatus(status);
						phenotypicCollection.setQuestionnaire(this.questionnaire);
						fieldData.setPhenotypicCollection(phenotypicCollection);
						fieldData.setCustomFieldDisplay(cfd);
	
						// Other/ith columns should be the field data value
						String value = StringUtils.trimToNull(stringLineArray[col]);	//trims to null
						
						// By default we will set the value into the errorDataValue... 
						// If the validation is ok, then it will be put in the appropriate place.
						fieldData.setErrorDataValue(value);

						ArkGridCell gridCell = new ArkGridCell(col, row);
						// Validate the field data
						boolean isValid = validateFieldData(fieldData, importValidationMessages);
						if (!isValid) {
							warningCells.add(gridCell);
						}

//						// Determine updates NEVER UPDATES ANYMORE
//						if (fieldDataToUpdate.contains(fieldData)) {
//							updateCells.add(gridCell);
//							updateRows.add(row);
//						}
//						else {
						insertCells.add(gridCell);
						insertRows.add(row);
//						}

						// Update progress
						curPos += stringLineArray[col].length() + 1; // update progress

						// Debug only - Show progress and speed
						log.debug("progress: " + decimalFormat.format(getProgress()) + " % | speed: " + decimalFormat.format(getSpeed()) + " KB/sec");

					}
	
					log.debug("\n");
					subjectCount++;
					row++;
				}
			}
			
			if (importValidationMessages.size() > 0) {
				log.debug("Validation messages: " + importValidationMessages.size());
				for (Iterator<String> iterator = importValidationMessages.iterator(); iterator.hasNext();) {
					String errorMessage = iterator.next();
					log.debug(errorMessage);
				}
			}
			else {
				log.debug("Validation is ok");
			}
		}
		catch (IOException ioe) {
			log.error("processMatrixPhenoFile IOException stacktrace:", ioe);
			throw new PhenotypicSystemException("Unexpected I/O exception whilst reading the phenotypic data file");
		}
		catch (Exception ex) {
			log.error("processMatrixPhenoFile Exception stacktrace:", ex);
			throw new PhenotypicSystemException("Unexpected exception occurred when trying to process phenotypic data file");
		}
		finally {
			// Clean up the IO objects
			timer.stop();
			log.debug("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			log.debug("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
			if (timer != null)
				timer = null;
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
			// Restore the state of variables
			srcLength = -1;
		}
		log.debug("Validated " + subjectCount * fieldCount + " rows of data");

		if (errorCells.isEmpty()) {
			for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
				Integer i = (Integer) iterator.next();
				importValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update");
			}
		}

		return importValidationMessages;
	}

	/**
	 * Returns true of the field data value is a valididated
	 * 
	 * @param fieldData
	 * @param errorMessages
	 * @return boolean
	 */
	public static boolean validateFieldData(PhenoData fieldData, java.util.Collection<String> errorMessages) {
		boolean isValid = false;
		boolean isValidFieldData = true;

		isValidFieldData = isFieldDataValid(fieldData, errorMessages);	// will put errorDataValue into the appropriate destination column if valid
		boolean isValidRange = isInValidRange(fieldData, errorMessages);

		isValid = isValidFieldData && isValidRange;
		return (isValid);
	}

	/**
	 * Returns true of the field data value is a valid format, either NUMBER, CHARACTER or DATE as specified in the data dictionary
	 * 
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isFieldDataValid(PhenoData fieldData, java.util.Collection<String> errorMessages) {
		boolean isValidFieldData = true;
		CustomField field = fieldData.getCustomFieldDisplay().getCustomField();

		if (fieldData.getErrorDataValue() == null) {
			// Data is empty so we don't actually need to store any data (i.e. returns true)
		}
		else if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
			// Number field type
			try {
				Double floatFieldValue = Double.parseDouble(fieldData.getErrorDataValue());
				if (isValidFieldData && StringUtils.isNotBlank(fieldData.getErrorDataValue())) {
					// Move the data into the appropriate place (assuming it's not null/blank)
					fieldData.setNumberDataValue(floatFieldValue);
					fieldData.setErrorDataValue(null);
				}
			}
			catch (NumberFormatException nfe) {
				errorMessages.add(PhenotypicValidationMessage.fieldDataNotDefinedType(field, fieldData));
				log.error("Field data number format exception " + nfe.getMessage());
				isValidFieldData = false;
			}
		}
		else if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
			// Character field type
			String stringFieldValue = fieldData.getErrorDataValue();
			if (isValidFieldData == true && StringUtils.isNotBlank(fieldData.getErrorDataValue())) {
				// Move the data into the appropriate place (assuming it's not null/blank)
				fieldData.setTextDataValue(stringFieldValue);
				fieldData.setErrorDataValue(null);
				// Do encodedValue check
				boolean isValidEncodedValues = isInEncodedValues(fieldData, errorMessages);
				isValidFieldData = isValidFieldData && isValidEncodedValues;
			}
		}
		else if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
			// Date field type
			try {
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(fieldData.getErrorDataValue());
				if (isValidFieldData && StringUtils.isNotBlank(fieldData.getErrorDataValue())) {
					// Move the data into the appropriate place (assuming it's not null/blank)
					fieldData.setDateDataValue(dateFieldValue);
					fieldData.setErrorDataValue(null);
				}
			}
			catch (ParseException pe) {
				errorMessages.add(PhenotypicValidationMessage.fieldDataNotValidDate(field, fieldData));
				log.error("Field data is not a date: " + pe.getMessage());
				isValidFieldData = false;
			}
		}

		return isValidFieldData;
	}

	/**
	 * Returns true if the field data value is within the discrete range as defined in the data dictionary
	 * (called after verifying the data matches CHARACTER type)
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isInEncodedValues(PhenoData fieldData, java.util.Collection<String> errorMessages) {
		boolean inEncodedValues = true;

		CustomField field = fieldData.getCustomFieldDisplay().getCustomField();

		if (StringUtils.isNotBlank(field.getEncodedValues())) {
			// Validate if encoded values
			String value = fieldData.getTextDataValue();	// should already have passed CHARACTER type check and not be null
			String missingValue = field.getMissingValue().trim();
			if ((StringUtils.equalsIgnoreCase(value, missingValue))) {
				// If matched MISSING_VALUE, then ok
				return inEncodedValues;
			}
	
			StringTokenizer stringTokenizer = new StringTokenizer(field.getEncodedValues(), Constants.ENCODED_VALUES_TOKEN);

			// Iterate through all discrete defined values and compare to field data value
			while (stringTokenizer.hasMoreTokens()) {
				String encodedValueToken = stringTokenizer.nextToken();
				StringTokenizer encodedValueSeparator = new StringTokenizer(encodedValueToken, Constants.ENCODED_VALUES_SEPARATOR);
				String encodedValue = encodedValueSeparator.nextToken().trim();

				if (encodedValue.equalsIgnoreCase(value)) {
					inEncodedValues = true;
					break;
				}
				else {
					inEncodedValues = false;
				}
			}

			if (!inEncodedValues) {
				// move the data back into the errorDataValue before raising error message
				fieldData.setErrorDataValue(value);
				fieldData.setTextDataValue(null);
				errorMessages.add(PhenotypicValidationMessage.fieldDataNotInEncodedValues(field, fieldData));
			}

		}

		return inEncodedValues;
	}

	/**
	 * Returns true if field data value is within the defined range as specified in the data dictionary
	 * 
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isInValidRange(PhenoData fieldData, java.util.Collection<String> errorMessages) {
		boolean isInValidRange = true;
		CustomField field = fieldData.getCustomFieldDisplay().getCustomField();
		String minValue = StringUtils.trimToNull(field.getMinValue());
		String maxValue = StringUtils.trimToNull(field.getMaxValue());
		
		if (StringUtils.isBlank(minValue) && StringUtils.isBlank(maxValue)) {
			return isInValidRange;
		}
		else if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
			try {
				Double floatMinValue = Double.parseDouble(field.getMinValue());
				Double floatMaxValue = Double.parseDouble(field.getMaxValue());
				Double floatFieldValue = fieldData.getNumberDataValue();	// should already have passed NUMBER type check and not be null

				if (floatFieldValue != null) {
					if (StringUtils.isNotBlank(maxValue) && (floatFieldValue > floatMaxValue)) {
						// error so move the data back to errorDataValue
						errorMessages.add(PhenotypicValidationMessage.fieldDataGreaterThanMaxValue(field, fieldData));
						isInValidRange = false;
					}
					if (StringUtils.isNotBlank(minValue) && (floatFieldValue < floatMinValue)) {
						errorMessages.add(PhenotypicValidationMessage.fieldDataLessThanMinValue(field, fieldData));
						isInValidRange = false;
					}
				}
			}
			catch (NumberFormatException nfe) {
				log.error("Invalid number in database for min/max value: " + nfe.getMessage());
				isInValidRange = false;
			}
		}
		else if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
			if (StringUtils.isNotBlank(minValue) && StringUtils.isNotBlank(maxValue)) {
				try {
					DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
					dateFormat.setLenient(false);

					Date dateMinValue = dateFormat.parse(field.getMinValue());
					Date dateMaxValue = dateFormat.parse(field.getMaxValue());
					Date dateFieldValue = fieldData.getDateDataValue();	// should already have passed DATE type check and not be null

					if (StringUtils.isNotBlank(maxValue) && dateFieldValue.after(dateMaxValue)) {
						errorMessages.add(PhenotypicValidationMessage.fieldDataGreaterThanMaxValue(field, fieldData));
						isInValidRange = false;
					}
					if (StringUtils.isNotBlank(minValue) && dateFieldValue.before(dateMinValue)) {
						errorMessages.add(PhenotypicValidationMessage.fieldDataLessThanMinValue(field, fieldData));
						isInValidRange = false;
					}
				}
				catch (ParseException pe) {
					log.error("Invalid date in database for min/max value: " + pe.getMessage());
					isInValidRange = false;
				}
			}
		}
		return isInValidRange;
	}

	/**
	 * Return the inputstream of the converted workbook as csv
	 * 
	 * @return inputstream of the converted workbook as csv
	 */
	public InputStream convertXlsToCsv(Workbook w) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			OutputStreamWriter osw = new OutputStreamWriter(out);

			// Gets first sheet from workbook
			Sheet s = w.getSheet(0);

			Cell[] row = null;

			// Gets the cells from sheet
			for (int i = 0; i < s.getRows(); i++) {
				row = s.getRow(i);

				if (row.length > 0) {
					osw.write(row[0].getContents());
					for (int j = 1; j < row.length; j++) {
						osw.write(phenotypicDelimChr);
						osw.write(row[j].getContents());
					}
				}
				osw.write("\n");
			}

			osw.flush();
			osw.close();
		}
		catch (UnsupportedEncodingException e) {
			System.err.println(e.toString());
		}
		catch (IOException e) {
			System.err.println(e.toString());
		}
		catch (Exception e) {
			System.err.println(e.toString());
		}
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	public boolean isQualityControl() {
		return qualityControl;
	}

	public void setQualityControl(boolean qualityControl) {
		this.qualityControl = qualityControl;
	}


	/**
	 * Return the progress of the current process in %
	 * 
	 * @return if a process is actively running, then progress in %; or if no process running, then returns -1
	 */
	public double getProgress() {
		double progress = -1;

		if (srcLength > 0)
			progress = curPos * 100.0 / srcLength; // %

		return progress;
	}

	/**
	 * Return the speed of the current process in KB/s
	 * 
	 * @return if a process is actively running, then speed in KB/s; or if no process running, then returns -1
	 */
	public double getSpeed() {
		double speed = -1;

		if (srcLength > 0)
			speed = curPos / 1024 / (timer.getTime() / 1000.0); // KB/s

		return speed;
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

	public HashSet<ArkGridCell> getInsertCells() {
		return insertCells;
	}

	public void setInsertCells(HashSet<ArkGridCell> insertCells) {
		this.insertCells = insertCells;
	}

	public HashSet<ArkGridCell> getUpdateCells() {
		return updateCells;
	}

	public void setUpdateCells(HashSet<ArkGridCell> updateCells) {
		this.updateCells = updateCells;
	}

	public HashSet<ArkGridCell> getErrorCells() {
		return errorCells;
	}

	public void setErrorCells(HashSet<ArkGridCell> errorCells) {
		this.errorCells = errorCells;
	}

	public HashSet<ArkGridCell> getWarningCells() {
		return warningCells;
	}

	public void setWarningCells(HashSet<ArkGridCell> warningCells) {
		this.warningCells = warningCells;
	}
}
