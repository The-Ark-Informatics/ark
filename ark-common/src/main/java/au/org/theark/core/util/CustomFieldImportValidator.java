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
package au.org.theark.core.util;

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
import java.util.regex.Pattern;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.time.StopWatch;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.hibernate.hql.ast.ErrorReporter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.exception.PhenotypicSystemException;
import au.org.theark.core.exception.SystemDataMismatchException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.worksheet.ArkGridCell;

import com.csvreader.CsvReader;

/**
 * CustomFieldImportValidator provides support for validating import file before 
 * trying to use CustomFieldImporter to import it into data dictionary
 * 
 * @author cellis
 * @author elam
 */
public class CustomFieldImportValidator {
	static Logger							log							= LoggerFactory.getLogger(CustomFieldImportValidator.class);
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
	private char							phenotypicDelimChr		= Constants.IMPORT_DELIM_CHAR_COMMA;						// default phenotypic file
																																						// delimiter: COMMA
	java.util.Collection<String>		fileValidationMessages	= new ArrayList<String>();
	java.util.Collection<String>		dataValidationMessages	= new ArrayList<String>();
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

	/**
	 * PhenotypicValidator constructor
	 * 
	 */

	public CustomFieldImportValidator(IArkCommonService<Void> iArkCommonService, UploadVO uploadVo) {
		this.iArkCommonService = iArkCommonService;
		this.arkFunction = uploadVo.getUpload().getArkFunction();

		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (studyId != null) {
			Study study = iArkCommonService.getStudy(studyId);
			this.study = study;
		}
		this.fileValidationMessages = new ArrayList<String>();
		this.dataValidationMessages = new ArrayList<String>();

		String filename = uploadVo.getFileUpload().getClientFileName();
		this.fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();

		this.phenotypicDelimChr = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();
			
	}

	public boolean isQualityControl() {
		return qualityControl;
	}

	public void setQualityControl(boolean qualityControl) {
		this.qualityControl = qualityControl;
	}

	/**
	 * Validates the phenotypic file in the default "matrix" file format assumed: SUBJECTID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN...
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
	public java.util.Collection<String> validateMatrixPhenoFileFormat(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException {
		curPos = 0;
		row = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;

		/*
		 * FieldData table requires: COLLECTION_ID PERSON_ID DATE_COLLECTED FIELD_ID USER_ID INSERT_TIME
		 */

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

			csvReader.readHeaders();

			// Set field list (note 2th column to Nth column)
			// FIELD_NAME FIELD_TYPE DESCRIPTION UNITS ENCODED_VALUES MINIMUM_VALUE MAXIMUM_VALUE MISSING_VALUE
			// 0 1 2 3 4 5 6 7
			String[] headerColumnArray = csvReader.getHeaders();
			boolean headerError = false;

			if (headerColumnArray.length <= 1)
				headerError = true;

			// Uploading a Field (Data Dictionary) file

			Collection<String> dataDictionaryColumns = new ArrayList<String>();
			String[] dataDictionaryColumnArray = Constants.DATA_DICTIONARY_HEADER;

			for (int i = 0; i < dataDictionaryColumnArray.length; i++) {
				dataDictionaryColumns.add(dataDictionaryColumnArray[i]);
			}

			for (int i = 0; i < headerColumnArray.length; i++) {
				if (!dataDictionaryColumns.contains(headerColumnArray[i])) {
					headerError = true;
					break;
				}
			}

			if (headerError) {
				// Invalid file format
				StringBuffer stringBuffer = new StringBuffer();
				String delimiterTypeName = iArkCommonService.getDelimiterTypeNameByDelimiterChar(phenotypicDelimChr);

				stringBuffer.append("The specified file does not appear to conform to the expected data dictionary file format.\n");
				stringBuffer.append("The specified file format was: " + fileFormat + "\n");
				stringBuffer.append("The specified delimiter was: [" + phenotypicDelimChr + "] (" + delimiterTypeName + ")\n");
				stringBuffer.append("The default data dictionary format is as follows:\n");
				stringBuffer.append("FIELD_NAME" + phenotypicDelimChr + "FIELD_TYPE" + phenotypicDelimChr + "DESCRIPTION" + phenotypicDelimChr + "UNITS" + phenotypicDelimChr + "ENCODED_VALUES"
						+ phenotypicDelimChr + "MINIMUM_VALUE" + phenotypicDelimChr + "MAXIMUM_VALUE" + phenotypicDelimChr + "MISSING_VALUE" + "\n");
				stringBuffer.append("[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]"
						+ phenotypicDelimChr + "[...]" + "\n");

				fileValidationMessages.add(stringBuffer.toString());
			}

			for (int i = 0; i < headerColumnArray.length; i++) {
				if (!dataDictionaryColumns.contains(headerColumnArray[i])) {
					fileValidationMessages.add("Error: the column name " + headerColumnArray[i] + " is not a valid column name.");
				}
			}


			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			row = 1;

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();

				// Loop through columns in current row in file, starting from the 2th position
				for (int i = 0; i < stringLineArray.length; i++) {
					// Update progress
					curPos += stringLineArray[i].length() + 1; // update progress
				}

				subjectCount++;
				row++;
			}

			if (fileValidationMessages.size() > 0) {
				for (Iterator<String> iterator = fileValidationMessages.iterator(); iterator.hasNext();) {
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
			// fileValidationMessages.add("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			// fileValidationMessages.add("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");

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

		return fileValidationMessages;
	}

	/**
	 * Validates the phenotypic data dictionary file in the default "matrix" file format assumed:
	 * "FIELD_NAME","FIELD_TYPE","DESCRIPTION","UNITS","ENCODED_VALUES","MINIMUM_VALUE","MAXIMUM_VALUE","MISSING_VALUE"
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @throws IOException
	 *            input/output Exception
	 * @throws OutOfMemoryError
	 *            out of memory Exception
	 */
	public java.util.Collection<String> validateDataDictionaryFileData(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException {
		curPos = 0;
		int rowIdx = 1;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		/*
		 * Field table requires: ID, STUDY_ID, FIELD_TYPE_ID, NAME, DESCRIPTION, UNITS, MIN_VALUE, MAX_VALUE, ENCODED_VALUES, MISSING_VALUE
		 */

		CustomField field = new CustomField();
		field.setStudy(study);

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

			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();

				ArkGridCell gridCell = null;

				// Fist column should be Field Name
				fieldName = csvReader.get("FIELD_NAME");

				// Only check rows with a valid fieldName
				if (!fieldName.isEmpty()) {
					int cols = stringLineArray.length;
					field = new CustomField();
					field.setStudy(study);
					field.setName(fieldName);

					field.setDescription(csvReader.get("DESCRIPTION"));
					if (csvReader.get("UNITS") != null && !csvReader.get("UNITS").isEmpty()) {
						UnitType unitType = iArkCommonService.getUnitTypeByNameAndArkFunction(csvReader.get("UNITS"), arkFunction);
						if (unitType == null) {
							gridCell = new ArkGridCell(csvReader.getIndex("UNITS"), rowIdx);
							StringBuffer stringBuffer = new StringBuffer();
							stringBuffer.append("Error: ");
							stringBuffer.append("Unit '");
							stringBuffer.append(csvReader.get("UNITS"));
							stringBuffer.append("' in file does not match known units in internal system table");
							dataValidationMessages.add(stringBuffer.toString());
							errorCells.add(gridCell);
						}
						else  {
							field.setUnitType(unitType);
						}
					}
					
					FieldType studyFieldType = new FieldType();
					studyFieldType = iArkCommonService.getFieldTypeByName(csvReader.get("FIELD_TYPE"));

					field.setFieldType(studyFieldType);
					
					field.setEncodedValues(csvReader.get("ENCODED_VALUES"));
					field.setMinValue(csvReader.get("MINIMUM_VALUE"));
					field.setMaxValue(csvReader.get("MAXIMUM_VALUE"));
					field.setMissingValue(csvReader.get("MISSING_VALUE"));

					CustomField oldField = iArkCommonService.getCustomFieldByNameStudyArkFunction(csvReader.get("FIELD_NAME"), study, arkFunction);
					if (oldField == null) {
						// This is a new record - not able to find an existing field by that name
						insertRows.add(rowIdx);							
					}
					else {
						// Determine updates
						if (oldField.getId() != null) {
							if (oldField.getCustomFieldHasData()) {
								// Block updates to field that already have data
								for (int colIdx = 0; colIdx < cols; colIdx++)
									errorCells.add(new ArkGridCell(colIdx, rowIdx));
								
								StringBuffer stringBuffer = new StringBuffer();
								stringBuffer.append("Error: ");
								stringBuffer.append("The existing field ");
								stringBuffer.append(fieldName);
								stringBuffer.append(" already has data associated with it and thus no changes can be made to this field.");
								dataValidationMessages.add(stringBuffer.toString());
								errorCells.add(gridCell);
							}
							else {
								updateRows.add(rowIdx);
								for (int colIdx = 0; colIdx < cols; colIdx++)
									updateCells.add(new ArkGridCell(colIdx, rowIdx));
							}
						}
					}

					if (csvReader.get("FIELD_TYPE") != null) {
						gridCell = new ArkGridCell(csvReader.getIndex("FIELD_TYPE"), rowIdx);
						if (!CustomFieldImportValidator.validateFieldType(this.fieldName, csvReader.get("FIELD_TYPE"), dataValidationMessages)) {
							errorCells.add(gridCell);
							field.getFieldType().setName(csvReader.get("FIELD_TYPE"));
						}
					}

					if (field.getEncodedValues() != null && !field.getEncodedValues().isEmpty()) {
						gridCell = new ArkGridCell(csvReader.getIndex("ENCODED_VALUES"), rowIdx);
						// Validate encoded values not a date type
						if (!CustomFieldImportValidator.validateEncodedValues(field, dataValidationMessages)) {
							errorCells.add(gridCell);
						}
					}

					if (field.getMinValue() != null && !field.getMinValue().isEmpty()) {
						gridCell = new ArkGridCell(csvReader.getIndex("MINIMUM_VALUE"), rowIdx);
						// Validate the field definition
						if (!CustomFieldImportValidator.validateFieldMinDefinition(field, dataValidationMessages)) {
							errorCells.add(gridCell);
						}
					}

					if (field.getMaxValue() != null && !field.getMaxValue().isEmpty()) {
						gridCell = new ArkGridCell(csvReader.getIndex("MAXIMUM_VALUE"), rowIdx);
						// Validate the field definition
						if (!CustomFieldImportValidator.validateFieldMaxDefinition(field, dataValidationMessages)) {
							errorCells.add(gridCell);
						}
					}

					if (field.getMissingValue() != null && !field.getMissingValue().isEmpty()) {
						gridCell = new ArkGridCell(csvReader.getIndex("MISSING_VALUE"), rowIdx);
						// Validate the field definition
						if (!CustomFieldImportValidator.validateFieldMissingDefinition(field, dataValidationMessages)) {
							errorCells.add(gridCell);
						}
					}

					fieldCount++;
					rowIdx++;
				}
			}

			if (dataValidationMessages.size() > 0) {
				log.debug("Validation messages: " + dataValidationMessages.size());
				for (Iterator<String> iterator = dataValidationMessages.iterator(); iterator.hasNext();) {
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

		if (errorCells.isEmpty()) {
			for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
				Integer i = (Integer) iterator.next();
				dataValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update");
			}
		}

		return dataValidationMessages;
	}

	/**
	 * Validates the ENCODED_VALUES column in a matrix Data Dictionary file
	 * 
	 * @param field
	 *           is the field entity in question
	 * @param errorMessages
	 *           is the error messages to add to is any errors
	 * @return true if field.fieldType is NOT a DATE
	 */
	private static boolean validateEncodedValues(CustomField field, Collection<String> errorMessages) {
		boolean isValid = false;
		if (!field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
			// At the moment, only allowed to have encodedValues for a field where fieldType == CHARACTER
			errorMessages.add(CustomFieldValidationMessage.fieldTypeIsNotCharacterWithEncodedValue(field.getName(), field.getFieldType().getName()));
		}
		else if (!Pattern.matches("(\\b[\\w]+=[^;]+;)*", field.getEncodedValues())) {
			errorMessages.add(CustomFieldValidationMessage.nonConformingEncodedValue(field.getName()));
		}
		else {
			isValid = true;
		}
		
		return isValid;
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
	public Collection<String> validateMatrixPhenoFileFormat(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;

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
			validationMessages = validateMatrixPhenoFileFormat(inputStream, inputStream.toString().length());
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
	 * Validates the file in the default "matrix" data dictionary file format assumed:
	 * "FIELD_NAME","FIELD_TYPE","DESCRIPTION","UNITS","ENCODED_VALUES","MINIMUM_VALUE","MAXIMUM_VALUE","MISSING_VALUE"
	 * 
	 * @param inputStream
	 *           is the input stream of the file
	 * @param fileFormat
	 *           is the file format (eg CSV, TXT or XLS)
	 * @param delimChar
	 *           is the delimiter character of the file (eg COMMA, TAB, PIPE etc)
	 * @return a collection of validation messages
	 */
	public Collection<String> validateDataDictionaryFileData(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;

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
			validationMessages = validateDataDictionaryFileData(inputStream, inputStream.toString().length());
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

	private static boolean validateFieldMissingDefinition(CustomField field, Collection<String> errorMessages) {
		boolean isValid = true;

		if (!(field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER) || field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER) || field.getFieldType()
				.getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))) {
			errorMessages.add(CustomFieldValidationMessage.fieldMissingValueNotDefinedType(field));
			isValid = false;
		}

		// Number field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
			try {
				Float floatFieldValue = Float.parseFloat(field.getMissingValue());
				isValid = true;
			}
			catch (NumberFormatException nfe) {
				errorMessages.add(CustomFieldValidationMessage.fieldMissingValueNotDefinedType(field));
				isValid = false;
			}
			catch (NullPointerException npe) {
				isValid = false;
			}
		}

		// Date field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
			try {
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(field.getMissingValue());
				isValid = true;
			}
			catch (ParseException pe) {
				errorMessages.add(CustomFieldValidationMessage.fieldDefinitionMissingValueNotValidDate(field));
				isValid = false;
			}
			catch (NullPointerException npe) {
				isValid = false;
			}
		}
		return isValid;
	}

	private static boolean validateFieldMaxDefinition(CustomField field, Collection<String> errorMessages) {
		boolean isValid = false;

		if (!(field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER) || field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER) || field.getFieldType()
				.getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))) {
			errorMessages.add(CustomFieldValidationMessage.fieldMaxValueNotDefinedType(field));
			return isValid;
		}

		// Number field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
			try {
				Float floatFieldValue = Float.parseFloat(field.getMaxValue());
				isValid = true;
			}
			catch (NumberFormatException nfe) {
				errorMessages.add(CustomFieldValidationMessage.fieldMaxValueNotDefinedType(field));
				isValid = false;
			}
			catch (NullPointerException npe) {
				isValid = false;
			}
		}

		// Date field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
			try {
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(field.getMaxValue());
				isValid = true;
			}
			catch (ParseException pe) {
				errorMessages.add(CustomFieldValidationMessage.fieldDefinitionMaxValueNotValidDate(field));
				isValid = false;
			}
			catch (NullPointerException npe) {
				isValid = false;
			}
		}
		return isValid;
	}

	private static boolean validateFieldMinDefinition(CustomField field, Collection<String> errorMessages) {
		boolean isValid = false;

		if (!(field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER) || field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER) || field.getFieldType()
				.getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))) {
			errorMessages.add(CustomFieldValidationMessage.fieldMinValueNotDefinedType(field));
			return isValid;
		}

		// Number field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
			try {
				Float floatFieldValue = Float.parseFloat(field.getMinValue());
				isValid = true;
			}
			catch (NumberFormatException nfe) {
				errorMessages.add(CustomFieldValidationMessage.fieldMinValueNotDefinedType(field));
				log.error("Field data number format exception " + nfe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe) {
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}
		}

		// Date field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
			try {
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(field.getMinValue());
				isValid = true;
			}
			catch (ParseException pe) {
				errorMessages.add(CustomFieldValidationMessage.fieldDefinitionMinValueNotValidDate(field));
				log.error("Field data date parse exception " + pe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe) {
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}
		}
		return isValid;
	}

	private static boolean validateFieldDefinition(CustomField field, Collection<String> errorMessages) {
		boolean isValid = false;

		// Number field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
			try {
				Float floatFieldValue = Float.parseFloat(field.getMinValue());
				isValid = true;
			}
			catch (NumberFormatException nfe) {
				errorMessages.add(CustomFieldValidationMessage.fieldMinValueNotDefinedType(field));
				log.error("Field data number format exception " + nfe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe) {
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}

			try {
				Float floatFieldValue = Float.parseFloat(field.getMaxValue());
				isValid = true;
			}
			catch (NumberFormatException nfe) {
				errorMessages.add(CustomFieldValidationMessage.fieldMaxValueNotDefinedType(field));
				log.error("Field data number format exception " + nfe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe) {
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}

			try {
				Float floatFieldValue = Float.parseFloat(field.getMissingValue());
				isValid = true;
			}
			catch (NumberFormatException nfe) {
				errorMessages.add(CustomFieldValidationMessage.fieldMissingValueNotDefinedType(field));
				log.error("Field data number format exception " + nfe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe) {
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}
		}

		// Date field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
			try {
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(field.getMinValue());
				isValid = true;
			}
			catch (ParseException pe) {
				errorMessages.add(CustomFieldValidationMessage.fieldDefinitionMinValueNotValidDate(field));
				log.error("Field data date parse exception " + pe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe) {
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}

			try {
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(field.getMaxValue());
				isValid = true;
			}
			catch (ParseException pe) {
				errorMessages.add(CustomFieldValidationMessage.fieldDefinitionMaxValueNotValidDate(field));
				log.error("Field data date parse exception " + pe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe) {
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}

			try {
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(field.getMissingValue());
				isValid = true;
			}
			catch (ParseException pe) {
				errorMessages.add(CustomFieldValidationMessage.fieldDefinitionMissingValueNotValidDate(field));
				log.error("Field data date parse exception " + pe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe) {
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}

		}

		return isValid;
	}

	private static boolean validateFieldType(String fieldName, String fieldType, Collection<String> errorMessages) {
		boolean isValid = false;

		if (fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER) || fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER) || fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
			isValid = true;
		}
		else {
			errorMessages.add(CustomFieldValidationMessage.fieldTypeNotDefined(fieldName, fieldType));
			isValid = false;
		}

		return isValid;
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
