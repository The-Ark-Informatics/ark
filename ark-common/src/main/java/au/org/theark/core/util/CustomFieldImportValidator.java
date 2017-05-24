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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.regex.Pattern;

import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.CustomFieldSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldCategory;
import au.org.theark.core.model.study.entity.CustomFieldType;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.worksheet.ArkGridCell;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * CustomFieldImportValidator provides support for validating import file before trying to use  <br>
 * CustomFieldImporter to import it into custom field table for the specifed function in question <br>
 * i.e. Function could be Subject, BioCollection or Biospecimen (or others if set up)
 * 
 * @author cellis
 * @author elam
 */
public class CustomFieldImportValidator implements ICustomImportValidator,Serializable {
	
	private static final long serialVersionUID = 1L;
	static Logger							log							= LoggerFactory.getLogger(CustomFieldImportValidator.class);
	boolean									qualityControl				= false;
	private Study							study;
	private String							fieldName;
	private long							subjectCount;
	private long							fieldCount;
	private long							curPos;
	private long							srcLength					= -1;
//	private StopWatch						timer							= null;
	private char							delimChr						= Constants.IMPORT_DELIM_CHAR_COMMA;
	private List<String>					fileValidationMessages	= new ArrayList<String>();
	private List<String>					dataValidationMessages	= new ArrayList<String>();
	private static IArkCommonService<Void>			iArkCommonService			= null;

	private HashSet<Integer>			insertRows					= new HashSet<Integer>();
	private HashSet<Integer>			updateRows					= new HashSet<Integer>();
	private HashSet<ArkGridCell>		insertCells					= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell>		updateCells					= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell>		warningCells				= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell>		errorCells					= new HashSet<ArkGridCell>();
	private String							fileFormat					= au.org.theark.core.Constants.DEFAULT_FILE_FORMAT;
	private int								row							= 1;
	private ArkFunction					arkFunction;
	private ArkModule 					arkModule;
	private List<CustomFieldType>       customFieldTypeLstForModule;
	private static CustomFieldType  selectedCustomFieldType;
	private List<CustomFieldCategory>  customFieldCategoryLstForCustomFieldType;
	
	

	/**
	 * CustomFieldImportValidator constructor
	 * @param iArkCommonService
	 * @param uploadVo
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
		
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		arkModule = iArkCommonService.getArkModuleById(sessionModuleId);
		customFieldTypeLstForModule=iArkCommonService.getCustomFieldTypes(arkModule);
		
		this.fileValidationMessages = new ArrayList<String>();
		this.dataValidationMessages = new ArrayList<String>();

		String filename = uploadVo.getFileUpload().getClientFileName();
		this.fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();

		this.delimChr = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();

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

	public boolean isQualityControl() {
		return qualityControl;
	}

	public void setQualityControl(boolean qualityControl) {
		this.qualityControl = qualityControl;
	}

	/**
	 * Validates the data dictionary file's general structure/format:<BR>
	 * "FIELD_NAME","CUSTOM_FIELD_TYPE",CUSTOM_FIELD_CATEGORY,//These fields newly introduced.
	 * "FIELD_TYPE","DESCRIPTION", "QUESTION", "UNITS","ENCODED_VALUES","MINIMUM_VALUE","MAXIMUM_VALUE","MISSING_VALUE","REQUIRED","ALLOW_MULTIPLE_SELECTION"
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 @param inLength
	 *           is the length of the file
	 * @throws IOException
	 *            input/output Exception
	 * @throws CustomFieldSystemException
	 *            custom field system Exception
	 */
	@Override
	public java.util.Collection<String> validateMatrixFileFormat(InputStream fileInputStream, long inLength, boolean isForPheno) throws FileFormatException, CustomFieldSystemException {
		curPos = 0;
		row = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimChr);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			csvReader.readHeaders();

			// Set field list (note 2th column to Nth column)
			// FIELD_NAME FIELD_TYPE DESCRIPTION QUESTION UNITS ENCODED_VALUES MINIMUM_VALUE MAXIMUM_VALUE MISSING_VALUE REQUIRED
			// 0 1 2 3 4 5 6 7 8
			String[] fileHeaderColumnArray = csvReader.getHeaders();
			boolean headerError = false;

			// Uploading a Custom Field (Data Dictionary) file
			Collection<String> fileHeaderCollection = new ArrayList<String>();
			
			
			String[] requiredHeaderArray = null;
			if(isForPheno){
				requiredHeaderArray = Constants.DATA_DICTIONARY_HEADER;
			}
			else{
				//remove the first element from array.
				requiredHeaderArray = Arrays.copyOfRange(Constants.CUSTOM_FIELD_UPLOAD_HEADER[0], 1,Constants.CUSTOM_FIELD_UPLOAD_HEADER[0].length ); 
			}
			
			log.info("requiredArray = " + Arrays.toString(requiredHeaderArray));
			log.info("requiredlength = " + requiredHeaderArray.length);
			log.info("fileHeaderColumnArray = "  + Arrays.toString(fileHeaderColumnArray));
			log.info("fileHeaderColumnlength = " + fileHeaderColumnArray.length);

			String specificError = "";
			 
			//all columns mandatory, even if data empty
			if (fileHeaderColumnArray.length < requiredHeaderArray.length) {
				specificError =  "File did not contain all " +  requiredHeaderArray.length + " expected headers.\n";
				log.info("error because less headers than required");
				headerError = true;
			}
			// Populate the collection for a search
			for (int i = 0; i < fileHeaderColumnArray.length; i++) {
				fileHeaderCollection.add(fileHeaderColumnArray[i]);
			}

			log.info("fileHeaderlength now = " + fileHeaderCollection.size());
			
			// Search the dataDictionaryHeader for missing headers
			for (int i = 0; i < requiredHeaderArray.length; i++) {
				//String ithHeader = (String)requiredHeaderArray[i];
				//ithHeader.compareToIgnoreCase(str)
				if (!fileHeaderCollection.contains(requiredHeaderArray[i])) {
					log.info("error because didn't contact the following required header" + requiredHeaderArray[i]);
					specificError =  "File was missing the following required header: " + requiredHeaderArray[i] + ".\n";
					headerError = true;
					break;
				}
			}

			if (headerError) {
				// Invalid file format
				StringBuffer stringBuffer = new StringBuffer();
				String delimiterTypeName = iArkCommonService.getDelimiterTypeNameByDelimiterChar(delimChr);

				stringBuffer.append("The specified file does not appear to conform to the expected data dictionary file format.\n");
				stringBuffer.append(specificError);
				stringBuffer.append("The specified file format was: " + fileFormat + "\n");
				stringBuffer.append("The specified delimiter was: [" + delimChr + "] (" + delimiterTypeName + ")\n");
				stringBuffer.append("The default data dictionary format is as follows:\n");
				for (int i = 0; i < requiredHeaderArray.length; i++) {
					if (i > 0) {
						stringBuffer.append(delimChr);
					}
					stringBuffer.append(requiredHeaderArray[i]);
				}
				stringBuffer.append("\n");
				for (int i = 0; i < requiredHeaderArray.length; i++) {
					if (i > 0) {
						stringBuffer.append(delimChr);
					}
					stringBuffer.append("[...]");
				}
				stringBuffer.append("\n");

				fileValidationMessages.add(stringBuffer.toString());
			}
			else {
				List<String> requiredHeaders = new ArrayList<String>();
				for (int i = 0; i < requiredHeaderArray.length; i++) {
					requiredHeaders.add(requiredHeaderArray[i]);
				}
				for (int i = 0; i < fileHeaderColumnArray.length; i++) {
					if (!requiredHeaders.contains(fileHeaderColumnArray[i])) {
						fileValidationMessages.add("Error: The column name " + fileHeaderColumnArray[i] + " is not a valid column name.");
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
			throw new CustomFieldSystemException("An unexpected I/O exception occurred whilst reading the phenotypic data file.");
		}
		catch (Exception ex) {
			log.error("processMatrixPhenoFile Exception stacktrace:", ex);
			throw new CustomFieldSystemException("An unexpected exception occurred when trying to process phenotypic data file.");
		}
		finally {
			// Clean up the IO objects
			//timer.stop();
			// fileValidationMessages.add("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			// fileValidationMessages.add("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");

			//if (timer != null)
			//	timer = null;
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
	 * Validates the values supplied in the data dictionary file to ensure they meet requirements for import to database
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of the file
	 * @throws IOException
	 *            input/output Exception
	 * @throws CustomFieldSystemException
	 *            CustomField System Exception
	 */
	private java.util.Collection<String> validateDataDictionaryFileData(InputStream fileInputStream, long inLength) throws FileFormatException, CustomFieldSystemException {
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
		if(Constants.ARK_MODULE_STUDY.equalsIgnoreCase(arkModule.getName())){
			arkFunction=iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD);
		}else if(Constants.ARK_MODULE_LIMS.equalsIgnoreCase(arkModule.getName())){
			arkFunction=iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD);
		}
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimChr);
			String[] stringLineArray;
			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}
			csvReader.readHeaders();
			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());
			// Loop through all rows in file
			while (csvReader.readRecord()) {
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				ArkGridCell gridCell = null;
				// First column should be Field Name
				fieldName = csvReader.get("FIELD_NAME");
				// Only check rows with a valid fieldName
				if (!fieldName.isEmpty()) {
					int cols = stringLineArray.length;
					field = new CustomField();
					field.setStudy(study);
					field.setName(fieldName);
					field.setDescription(csvReader.get("DESCRIPTION"));
					field.setFieldLabel(csvReader.get("QUESTION"));
					//Remove the Units validation for the unit types.
					if(!Constants.ARK_MODULE_STUDY.equalsIgnoreCase(arkModule.getName())){
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
								else {
									field.setUnitType(unitType);
								}
							}
					}else{
						field.setUnitTypeInText(csvReader.get("UNITS"));
					}
					//Add the custom field type and the relevant caregory on 2015-08-21
					if(csvReader.get("CUSTOM_FIELD_TYPE")!=null && !csvReader.get("CUSTOM_FIELD_TYPE").isEmpty()){
						CustomFieldType customFieldType=iArkCommonService.getCustomFieldTypeByName(csvReader.get("CUSTOM_FIELD_TYPE"));
						field.setCustomFieldType(customFieldType);
					}
					if(csvReader.get("CUSTOM_FIELD_CATEGORY")!=null && !csvReader.get("CUSTOM_FIELD_CATEGORY").isEmpty()){
						CustomFieldCategory customFieldCategory=iArkCommonService.getCustomFieldCategotyByName(csvReader.get("CUSTOM_FIELD_CATEGORY"));
						field.setCustomFieldCategory(customFieldCategory);
					}
					FieldType studyFieldType = new FieldType();
					try {
						studyFieldType = iArkCommonService.getFieldTypeByName(csvReader.get("FIELD_TYPE"));
						field.setFieldType(studyFieldType);
					}
					catch (EntityNotFoundException e){
						// Field Type not found, handled in error messaging below....
					}
					String encodedValues = csvReader.get("ENCODED_VALUES");
					field.setEncodedValues(encodedValues);
					/* removed the below logic whie this is moved to customfieldgroup
					if(encodedValues!=null && !encodedValues.isEmpty()){
						field.setEncodedValues(encodedValues);
						if(csvReader.get("ALLOW_MULTIPLE_SELECTIONS")!=null && !csvReader.get("ALLOW_MULTIPLE_SELECTIONS").isEmpty()){
							field.
						}
					}*/
					field.setMinValue(csvReader.get("MINIMUM_VALUE"));
					field.setMaxValue(csvReader.get("MAXIMUM_VALUE"));
					field.setMissingValue(csvReader.get("MISSING_VALUE"));
					field.setDefaultValue(csvReader.get("DEFAULT_VALUE"));

					//This is how the old custom field being captured by the name.
					//It is unique field according to the db index defined.
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
								for (int colIdx = 0; colIdx < cols; colIdx++) {
									errorCells.add(new ArkGridCell(colIdx, rowIdx));
								}

								StringBuffer stringBuffer = new StringBuffer();
								stringBuffer.append("Error: ");
								stringBuffer.append("The existing field ");
								stringBuffer.append(fieldName);
								stringBuffer.append(" already has data associated with it and thus no changes can be made to this field.");
								dataValidationMessages.add(stringBuffer.toString());
								errorCells.add(gridCell);
							}else {
								updateRows.add(rowIdx);
								for (int colIdx = 0; colIdx < cols; colIdx++) {
									updateCells.add(new ArkGridCell(colIdx, rowIdx));
								}
							}
						}
					}
					//Added following validation methods for name,description,labelTxt and UnitTypeText
					//Name
					if (fieldName != null && !fieldName.isEmpty()) {
						gridCell = new ArkGridCell(csvReader.getIndex("FIELD_NAME"), rowIdx);
						if (!CustomFieldImportValidator.validateFieldName(fieldName, dataValidationMessages)) {
							errorCells.add(gridCell);
						}
					}
					//Description
					if (csvReader.get("DESCRIPTION") != null && !csvReader.get("DESCRIPTION").isEmpty()) {
						gridCell = new ArkGridCell(csvReader.getIndex("DESCRIPTION"), rowIdx);
						if (!CustomFieldImportValidator.validateFieldDescription(fieldName, csvReader.get("DESCRIPTION"), dataValidationMessages)){
							errorCells.add(gridCell);
						}
					}
					//Question label
					if(csvReader.get("QUESTION") != null && !csvReader.get("QUESTION").isEmpty()) {
						gridCell = new ArkGridCell(csvReader.getIndex("QUESTION"), rowIdx);
						if (!CustomFieldImportValidator.validateQuestionLabel(fieldName, csvReader.get("QUESTION"), dataValidationMessages)){
							errorCells.add(gridCell);
						}
					}
					//UniteTypeText
					if(csvReader.get("UNITS") != null && !csvReader.get("UNITS").isEmpty()) {
						gridCell = new ArkGridCell(csvReader.getIndex("UNITS"), rowIdx);
						if (!CustomFieldImportValidator.validateUnitTypeTxt(fieldName, csvReader.get("UNITS"), dataValidationMessages)){
							errorCells.add(gridCell);
						}
					}
					//Validate Custom field type.
					if (csvReader.get("CUSTOM_FIELD_TYPE") != null) {
						gridCell = new ArkGridCell(csvReader.getIndex("CUSTOM_FIELD_TYPE"), rowIdx);
						if (!CustomFieldImportValidator.validateCustomFieldType(customFieldTypeLstForModule,this.fieldName, csvReader.get("CUSTOM_FIELD_TYPE"), dataValidationMessages)) {
							errorCells.add(gridCell);
						}
					}
					//We validate the categories just only for that custom field types.
					customFieldCategoryLstForCustomFieldType=iArkCommonService.getCustomFieldCategoryByCustomFieldTypeAndStudy(study, selectedCustomFieldType);
					
					//Validate custom field category
					if (csvReader.get("CUSTOM_FIELD_CATEGORY") != null) {
						gridCell = new ArkGridCell(csvReader.getIndex("CUSTOM_FIELD_CATEGORY"), rowIdx);
						if (!CustomFieldImportValidator.validateCustomFieldCategory(customFieldCategoryLstForCustomFieldType,this.fieldName, csvReader.get("CUSTOM_FIELD_CATEGORY"), dataValidationMessages)) {
							errorCells.add(gridCell);
						}
					}
					if (csvReader.get("FIELD_TYPE") != null) {
						gridCell = new ArkGridCell(csvReader.getIndex("FIELD_TYPE"), rowIdx);
						if (!CustomFieldImportValidator.validateFieldType(this.fieldName, csvReader.get("FIELD_TYPE"), dataValidationMessages)) {
							errorCells.add(gridCell);
						}
					}
					boolean validForMultiSelect = false;
					String allowMultiple = (csvReader.get("ALLOW_MULTIPLE_SELECTIONS"));
					if (field.getEncodedValues() != null && !field.getEncodedValues().isEmpty()) {
						gridCell = new ArkGridCell(csvReader.getIndex("ENCODED_VALUES"), rowIdx);
						// Validate encoded values not a date type
						if (!CustomFieldImportValidator.validateEncodedValues(field, dataValidationMessages)) {
							errorCells.add(gridCell);
						}
						else {
							validForMultiSelect = true;
							if(!DataConversionAndManipulationHelper.isSomethingLikeABoolean(allowMultiple) && !allowMultiple.isEmpty()){
								gridCell = new ArkGridCell(csvReader.getIndex("ALLOW_MULTIPLE_SELECTIONS"), rowIdx);
								dataValidationMessages.add(CustomFieldValidationMessage.invalidOption(field.getName(), "ALLOW_MULTIPLE_SELECTIONS"));
								errorCells.add(gridCell);
							}
							else if(!validForMultiSelect){
								gridCell = new ArkGridCell(csvReader.getIndex("ALLOW_MULTIPLE_SELECTIONS"), rowIdx);
								dataValidationMessages.add(CustomFieldValidationMessage.nonConformingAllowMultipleSelect(field.getName()));
								errorCells.add(gridCell);
							}
						}
					} else if (!DataConversionAndManipulationHelper.isSomethingLikeABoolean(allowMultiple) && !allowMultiple.isEmpty()) {// please check this again if goes wrong...
						gridCell = new ArkGridCell(csvReader.getIndex("ALLOW_MULTIPLE_SELECTIONS"), rowIdx);
						dataValidationMessages.add(CustomFieldValidationMessage.nonConformingAllowMultipleSelect(field.getName()));
						errorCells.add(gridCell);
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
					if (field.getDefaultValue() != null && !field.getDefaultValue().isEmpty()) {
						gridCell = new ArkGridCell(csvReader.getIndex("DEFAULT_VALUE"), rowIdx);
						// Validate the field definition
						if (!CustomFieldImportValidator.validateFieldDefaultDefinition(field, dataValidationMessages)) {
							errorCells.add(gridCell);
						}
					}
					// Required column only relevant to specific custom field data (eg subject custom field)
					if(csvReader.getIndex("REQUIRED") > 0) {
						if(!DataConversionAndManipulationHelper.isSomethingLikeABoolean(csvReader.get("REQUIRED"))){
							gridCell = new ArkGridCell(csvReader.getIndex("REQUIRED"), rowIdx);
							dataValidationMessages.add(CustomFieldValidationMessage.invalidOption(field.getName(), "REQUIRED"));
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
			log.error("processMatrixFile IOException stacktrace:", ioe);
			throw new CustomFieldSystemException("An unexpected I/O exception occurred whilst reading the data file.");
		}
		catch (Exception ex) {
			log.error("processMatrixFile Exception stacktrace:", ex);
			throw new CustomFieldSystemException("An unexpected exception occurred when trying to process data file.");
		}
		finally {
			// Clean up the IO objects
//			timer.stop();
//			log.debug("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			log.debug("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
//			if (timer != null)
//				timer = null;
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
				dataValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update.");
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
		else if (!Pattern.matches(Constants.ENCODED_VALUES_PATTERN, field.getEncodedValues())) {
			errorMessages.add(CustomFieldValidationMessage.nonConformingEncodedValue(field.getName()));
		}
		else {
			isValid = true;
		}

		return isValid;
	}
	
	
	/**
	 * Validates the general file format confirms to a data dictionary upload
	 * 
	 * @param inputStream
	 *           is the input stream of the file
	 * @param fileFormat
	 *           is the file format (eg txt)
	 * @param delimChar
	 *           is the delimiter character of the file (eg comma)
	 * @return a collection of validation messages
	 */
	@Override
	public Collection<String> validateCustomDataMatrixFileFormat(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;

		try {
			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					delimChr = ',';
					XLStoCSV xlsToCsv = new XLStoCSV(delimChr);
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
			validationMessages = validateMatrixFileFormat(inputStream, inputStream.toString().length(), false);
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
	 * Validates the general file format confirms to a data dictionary upload
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
					delimChr = ',';
					XLStoCSV xlsToCsv = new XLStoCSV(delimChr);
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
			validationMessages = validateMatrixFileFormat(inputStream, inputStream.toString().length(), true);
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
	 * @throws ArkBaseException 
	 */
	@Override
	public Collection<String> validateDataDictionaryFileData(InputStream inputStream, String fileFormat, char delimChar) throws ArkBaseException {
		java.util.Collection<String> validationMessages = null;

		try {
			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					delimChr = ',';
					XLStoCSV xlsToCsv = new XLStoCSV(delimChr);
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
			validationMessages = validateDataDictionaryFileData(inputStream, inputStream.toString().length());
		}
		catch (FileFormatException ffe) {
			log.error("FILE_FORMAT_EXCPEPTION: " + ffe);
			throw new FileFormatException("Problem during validating the data dictionary file data.");
		}
		catch (ArkBaseException abe) {
			log.error("ARK_BASE_EXCEPTION: " + abe);
			throw new ArkBaseException("Problem during validating the data dictionary file data.");
		}
		return validationMessages;
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
				Float.parseFloat(field.getMissingValue());
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
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFormat.parse(field.getMissingValue());
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
	
	private static boolean validateFieldDefaultDefinition(CustomField field, Collection<String> errorMessages) {
		boolean isValid = true;
		if (!(field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER) || field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER) || field.getFieldType()
				.getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))) {
			errorMessages.add(CustomFieldValidationMessage.fieldDefaultValueNotDefinedType(field));
			isValid = false;
		}
		//Character field type
		//if Encoded value has been introduced check the default value has one of the encoded value occupied.
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
			if(iArkCommonService.isEncodedValue(field, field.getDefaultValue())){
				isValid=true;
			}else{
				errorMessages.add(CustomFieldValidationMessage.fieldDefaultValueNotINEncodedLst(field));
				isValid=false;
			}
		}
		// Number field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
			try {
				Float defaultVal = null;
				Float minVal= null;
				Float maxVal= null;		
				
				if(!field.getDefaultValue().isEmpty()&& field.getDefaultValue()!=null ){
					defaultVal=	Float.parseFloat(field.getDefaultValue());
				}
				if(!field.getMinValue().isEmpty()&& field.getMinValue()!=null ){
					minVal=	Float.parseFloat(field.getMinValue());
				}
				if(!field.getMaxValue().isEmpty()&& field.getMaxValue()!=null ){
					maxVal=	Float.parseFloat(field.getMaxValue());
				}
				//check default value in between min and max
				if(defaultVal!=null && minVal !=null && maxVal !=null){	
					if((minVal.equals(defaultVal) && maxVal > defaultVal) || (maxVal.equals(defaultVal) && minVal < defaultVal) ||(minVal < defaultVal && maxVal > defaultVal)){
						isValid = true;
					}else{
						errorMessages.add(CustomFieldValidationMessage.fieldDefaultValueInsideMinAndMaxRange(field));
						isValid = false;
					}
				}	
			}
			catch (NumberFormatException nfe) {
				errorMessages.add(CustomFieldValidationMessage.fieldDefaultValueNotDefinedType(field));
				isValid = false;
			}
			catch (NullPointerException npe) {
				errorMessages.add(CustomFieldValidationMessage.fieldDefinitionDefaultValueValidatingNull(field));
				isValid = false;
			}
		}

		// Date field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
			try {
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				Date defaultDate= null;
				Date minDate=	null;
				Date maxDate=	null;
				if(!field.getDefaultValue().isEmpty()&& field.getDefaultValue()!=null ){
					defaultDate=	dateFormat.parse(field.getDefaultValue());
				}
				if(!field.getMinValue().isEmpty()&& field.getMinValue()!=null ){
					minDate=	dateFormat.parse(field.getMinValue());
				}
				if(!field.getMaxValue().isEmpty()&& field.getMaxValue()!=null ){
					maxDate=	dateFormat.parse(field.getMaxValue());
				}
				//check default value in between min and max
				if(defaultDate!=null && minDate !=null && maxDate!=null){
					if((minDate.equals(defaultDate) && maxDate.after(defaultDate))|| (maxDate.equals(defaultDate) && minDate.before(defaultDate)) ||(minDate.before(defaultDate) && maxDate.after(defaultDate))){
						isValid = true;
					}else{
						errorMessages.add(CustomFieldValidationMessage.fieldDefaultDateInsideMinAndMaxRange(field, defaultDate, minDate, maxDate));
						isValid = false;
					}
				}
			}
			catch (ParseException pe) {
				errorMessages.add(CustomFieldValidationMessage.fieldDefinitionDefaultValueNotValidDate(field));
				isValid = false;
			}
			catch (NullPointerException npe) {
				errorMessages.add(CustomFieldValidationMessage.fieldDefinitionDefaultValueValidatingNull(field));
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
				Float.parseFloat(field.getMaxValue());
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
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFormat.parse(field.getMaxValue());
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
				Float.parseFloat(field.getMinValue());
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
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFormat.parse(field.getMinValue());
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
	 * Validate (module or custom field type) 
	 * Here we checked the entered module(Subject or Family already exsist in the list)
	 * @param fieldName
	 * @param fieldType
	 * @param errorMessages
	 * @return
	 */
	private static  boolean validateCustomFieldType(List<CustomFieldType> customFieldTypesLst,String fieldName, String fieldType, Collection<String> errorMessages) {
		boolean isValid = false;
		
		if(!customFieldTypesLst.isEmpty()){
			for (CustomFieldType cusFieldType : customFieldTypesLst) {
				if(cusFieldType.getName().equalsIgnoreCase(fieldType)){
					selectedCustomFieldType=cusFieldType;
					isValid=true;
					break;
				}
			}
		}
			if(isValid==false){
				errorMessages.add(CustomFieldValidationMessage.invalidCustomFieldType(fieldName, fieldType));
			}
		return isValid;
	}
	/**
	 * 
	 * @param fieldName
	 * @param fieldType
	 * @param errorMessages
	 * @return
	 */
	private static boolean validateCustomFieldCategory(List<CustomFieldCategory> customFieldCategoryLstForCustomFieldType,String fieldName, String fieldType, Collection<String> errorMessages) {
		boolean isValid = false;
		if(!customFieldCategoryLstForCustomFieldType.isEmpty()){
			for (CustomFieldCategory category : customFieldCategoryLstForCustomFieldType) {
				if(category.getName().equalsIgnoreCase(fieldType)){
					isValid=true;
					break;
				}
			} 
		}
			if(isValid==false){
				errorMessages.add(CustomFieldValidationMessage.invalidCategory(fieldName, fieldType));
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
	 * 
	 * @param fieldName
	 * @param errorMessages
	 * @return
	 */
	private static boolean validateFieldName(String fieldName, Collection<String> errorMessages) {
		boolean isValid = false;
		if (fieldName.length() > Constants.GENERAL_FIELD_NAME_MAX_LENGTH_50 ) {
			errorMessages.add(CustomFieldValidationMessage.invalidFieldName(fieldName));
			isValid = false;
		}
		else {
			isValid = true;
		}
		return isValid;
	}
	/**
	 * 
	 * @param fieldName
	 * @param fieldDescription
	 * @param errorMessages
	 * @return
	 */
	private static boolean validateFieldDescription(String fieldName,String fieldDescription, Collection<String> errorMessages) {
		boolean isValid = false;
		if (fieldDescription.length() > Constants.GENERAL_FIELD_DESCRIPTIVE_MAX_LENGTH_255) {
			errorMessages.add(CustomFieldValidationMessage.invalidFieldDescription(fieldDescription, fieldName));
			isValid = false;
		}
		else {
			isValid = true;
		}
		return isValid;
	}
	/**
	 * 
	 * @param fieldName
	 * @param questionLabel
	 * @param errorMessages
	 * @return
	 */
	private static boolean validateQuestionLabel(String fieldName,String questionLabel, Collection<String> errorMessages) {
		boolean isValid = false;
		if (questionLabel.length() > Constants.GENERAL_FIELD_DESCRIPTIVE_MAX_LENGTH_255) {
			errorMessages.add(CustomFieldValidationMessage.invalidFieldQuestionLabel(questionLabel, fieldName));
			isValid = false;
		}
		else {
			isValid = true;
		}
		return isValid;
	}
	/**
	 * 
	 * @param fieldName
	 * @param unitType
	 * @param errorMessages
	 * @return
	 */
	private static boolean validateUnitTypeTxt(String fieldName,String unitType, Collection<String> errorMessages) {
		boolean isValid = false;
		if (unitType.length() > Constants.GENERAL_FIELD_NAME_MAX_LENGTH_50) {
			errorMessages.add(CustomFieldValidationMessage.invalidFieldUnitTypeTxt(unitType, fieldName));
			isValid = false;
		}
		else {
			isValid = true;
		}
		return isValid;
	}
}
