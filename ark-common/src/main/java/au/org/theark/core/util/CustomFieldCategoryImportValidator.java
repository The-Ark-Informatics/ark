package au.org.theark.core.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Serializable;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jxl.Workbook;
import jxl.read.biff.BiffException;

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

public class CustomFieldCategoryImportValidator implements ICustomImportValidator,Serializable {

	private static final long serialVersionUID = 1L;
	static Logger						log							= LoggerFactory.getLogger(CustomFieldCategoryImportValidator.class);
	//boolean								qualityControl				= false;
	private Study						study;
	private String						categoryName;
	private long						subjectCount;
	private long						fieldCount;
	private long						curPos;
	private long						srcLength					= -1;
//	private StopWatch					timer							= null;
	private char						delimChr						= Constants.IMPORT_DELIM_CHAR_COMMA;
	private List<String>				fileValidationMessages	= new ArrayList<String>();
	private List<String>				dataValidationMessages	= new ArrayList<String>();
	private IArkCommonService<Void>		iArkCommonService			= null;
	private HashSet<Integer>			insertRows					= new HashSet<Integer>();
	private HashSet<Integer>			updateRows					= new HashSet<Integer>();
	private HashSet<ArkGridCell>		insertCells					= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell>		updateCells					= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell>		warningCells				= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell>		errorCells					= new HashSet<ArkGridCell>();
	private String						fileFormat					= au.org.theark.core.Constants.DEFAULT_FILE_FORMAT;
	private int							row							= 1;
	private ArkFunction					arkFunction;
	private ArkModule 					arkModule;
	private List<CustomFieldType>       customFieldTypeLstForModule;
	private static CustomFieldType  selectedCustomFieldType;
	private List<CustomFieldCategory>  customFieldCategoryLstForCustomFieldType;



/**
 * Constructor.
 * @param iArkCommonService
 * @param uploadVo
 */
public CustomFieldCategoryImportValidator(IArkCommonService<Void> iArkCommonService, UploadVO uploadVo) {
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
/**
 * Validate the Data dictionary general structure format.
 * "CATEGORY_NAME","CUSTOM_FIELD_TYPE","DESCRIPTION","PARENT_CATEGORY_NAME","ORDER_NUMBER"
 * @param fileInputStream
 * @param inLength
 * @return
 * @throws FileFormatException
 * @throws CustomFieldSystemException
 */
@Override
public java.util.Collection<String> validateMatrixFileFormat(InputStream fileInputStream, long inLength,boolean dummy) throws FileFormatException, CustomFieldSystemException {
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
		String[] fileHeaderColumnArray = csvReader.getHeaders();
		boolean headerError = false;
		Collection<String> fileHeaderCollection = new ArrayList<String>();
		String[] requiredHeaderArray = null;
		//Remove the first array variable.
		requiredHeaderArray = Arrays.copyOfRange(Constants.CUSTOM_FIELD_CATEGORY_UPLOAD_HEADER[0],1,Constants.CUSTOM_FIELD_CATEGORY_UPLOAD_HEADER[0].length);
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
					fileValidationMessages.add("Error: the column name " + fileHeaderColumnArray[i] + " is not a valid column name.");
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
		throw new CustomFieldSystemException("Unexpected I/O exception whilst reading the phenotypic data file");
	}
	catch (Exception ex) {
		log.error("processMatrixPhenoFile Exception stacktrace:", ex);
		throw new CustomFieldSystemException("Unexpected exception occurred when trying to process phenotypic data file");
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
		// Restore the state of variables
		srcLength = -1;
	}
	return fileValidationMessages;
}
/**
 * Validates the file in the default "matrix" data dictionary file format assumed:
 * "CATEGORY_NAME",
		"CUSTOM_FIELD_TYPE","DESCRIPTION","PARENT_CATEGORY_NAME","ORDER_NUMBER"
 * 
 * @param inputStream
 *           is the input stream of the file
 * @param fileFormat
 *           is the file format (eg CSV, TXT or XLS)
 * @param delimChar
 *           is the delimiter character of the file (eg COMMA, TAB, PIPE etc)
 * @return a collection of validation messages
 */
@Override
public Collection<String> validateDataDictionaryFileData(InputStream inputStream, String fileFormat, char delimChar) {
	java.util.Collection<String> validationMessages = new ArrayList<String>();
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
		validationMessages.add(ffe.getMessage());
		log.error("FILE_FORMAT_EXCPEPTION: " + ffe);
	}
	catch (ArkBaseException abe) {
		validationMessages.add(abe.getMessage());
		log.error("ARK_BASE_EXCEPTION: " + abe);
	}
	return validationMessages;
}


private java.util.Collection<String> validateDataDictionaryFileData(InputStream fileInputStream, long inLength) throws FileFormatException, CustomFieldSystemException {
	curPos = 0;
	int rowIdx = 1;

	InputStreamReader inputStreamReader = null;
	CsvReader csvReader = null;
	DecimalFormat decimalFormat = new DecimalFormat("0.00");

	/*"CATEGORY_NAME","CUSTOM_FIELD_TYPE","DESCRIPTION","PARENT_CATEGORY_NAME","ORDER_NUMBER"*/
	CustomFieldCategory category;
	CustomFieldType customFieldType;
	
	if(Constants.ARK_MODULE_STUDY.equalsIgnoreCase(arkModule.getName())){
		arkFunction=iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_CATEGORY);
	}else if(Constants.ARK_MODULE_LIMS.equalsIgnoreCase(arkModule.getName())){
		arkFunction=iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD_CATEGORY);
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
			categoryName = csvReader.get("CATEGORY_NAME");

			// Only check rows with a valid fieldName
			if (!categoryName.isEmpty()) {
				int cols = stringLineArray.length;
				category = new CustomFieldCategory();
				category.setStudy(study);
				category.setName(categoryName);
				category.setDescription(csvReader.get("DESCRIPTION"));
				
				if(csvReader.get("CUSTOM_FIELD_TYPE")!=null && !csvReader.get("CUSTOM_FIELD_TYPE").isEmpty()){
					customFieldType=iArkCommonService.getCustomFieldTypeByName(csvReader.get("CUSTOM_FIELD_TYPE"));
					category.setCustomFieldType(customFieldType);
				}
				if(csvReader.get("PARENT_CATEGORY_NAME")!=null && !csvReader.get("PARENT_CATEGORY_NAME").isEmpty()){
					CustomFieldCategory parentCategory=iArkCommonService.getCustomFieldCategotyByNameAndCustomFieldType(csvReader.get("PARENT_CATEGORY_NAME"),category.getCustomFieldType());
					category.setParentCategory(parentCategory);
				}
				
				if(csvReader.get("ORDER_NUMBER")!=null && !csvReader.get("ORDER_NUMBER").isEmpty() && isNumeric(csvReader.get("ORDER_NUMBER"))){
					category.setOrderNumber(Long.valueOf(csvReader.get("ORDER_NUMBER")));
				}
				CustomFieldCategory oldcategory = iArkCommonService.getCustomFieldCategoryByNameStudyAndArkFunction(csvReader.get("CATEGORY_NAME"),study, arkFunction); 
				if (oldcategory == null) {
					// This is a new record - not able to find an existing field by that name
					insertRows.add(rowIdx);
				}
				else {
					// Determine updates
					if (oldcategory.getId() != null) {
						if (iArkCommonService.isCustomFieldCategoryBeingUsed(oldcategory)) {
							// Block updates to field that already have data
							for (int colIdx = 0; colIdx < cols; colIdx++) {
								errorCells.add(new ArkGridCell(colIdx, rowIdx));
							}
							StringBuffer stringBuffer = new StringBuffer();
							stringBuffer.append("Error: ");
							stringBuffer.append("The existing category ");
							stringBuffer.append(categoryName);
							stringBuffer.append(" already has data associated with it and thus no changes can be made to this category.");
							dataValidationMessages.add(stringBuffer.toString());
							errorCells.add(gridCell);
						}
						else {
							updateRows.add(rowIdx);
							for (int colIdx = 0; colIdx < cols; colIdx++) {
								updateCells.add(new ArkGridCell(colIdx, rowIdx));
							}
						}
					}
				}
				//Added following validation methods for name,description,labelTxt and UnitTypeText
				//Name
				if (categoryName != null && !categoryName.isEmpty()) {
					gridCell = new ArkGridCell(csvReader.getIndex("CATEGORY_NAME"), rowIdx);
					if (!CustomFieldCategoryImportValidator.validateFieldName(categoryName, dataValidationMessages)) {
						errorCells.add(gridCell);
					}
				}
				//Description
				if (csvReader.get("DESCRIPTION") != null && !csvReader.get("DESCRIPTION").isEmpty()) {
					gridCell = new ArkGridCell(csvReader.getIndex("DESCRIPTION"), rowIdx);
					if (!CustomFieldCategoryImportValidator.validateFieldDescription(categoryName, csvReader.get("DESCRIPTION"), dataValidationMessages)){
						errorCells.add(gridCell);
					}
				}
				/*//Validate Custom field type.
				if (csvReader.get("CUSTOM_FIELD_TYPE") != null) {
					gridCell = new ArkGridCell(csvReader.getIndex("CUSTOM_FIELD_TYPE"), rowIdx);
					if (!CustomFieldCategoryImportValidator.validateCustomFieldType(category,categoryName,csvReader.get("CUSTOM_FIELD_TYPE"), dataValidationMessages)) {
						errorCells.add(gridCell);
					}
				}*/
				//Validate Custom field type.
				if (csvReader.get("CUSTOM_FIELD_TYPE") != null) {
					gridCell = new ArkGridCell(csvReader.getIndex("CUSTOM_FIELD_TYPE"), rowIdx);
					if (!CustomFieldCategoryImportValidator.validateCustomFieldType(customFieldTypeLstForModule,this.categoryName, csvReader.get("CUSTOM_FIELD_TYPE"), dataValidationMessages)) {
						errorCells.add(gridCell);
					}
				}
				//We validate the categories just only for that custom field types.
				customFieldCategoryLstForCustomFieldType=iArkCommonService.getCustomFieldCategoryByCustomFieldTypeAndStudy(study, selectedCustomFieldType);
				
				//Validate parent category
				//if (csvReader.get("PARENT_CATEGORY_NAME") != null) {
					gridCell = new ArkGridCell(csvReader.getIndex("PARENT_CATEGORY_NAME"), rowIdx);
					if (!CustomFieldCategoryImportValidator.validateParentCategory(customFieldCategoryLstForCustomFieldType,category,categoryName, csvReader.get("PARENT_CATEGORY_NAME"), dataValidationMessages)) {
						errorCells.add(gridCell);
					}
				//}
				
				//validate order number.
				if (csvReader.get("ORDER_NUMBER") != null) {
					gridCell = new ArkGridCell(csvReader.getIndex("ORDER_NUMBER"), rowIdx);
					if (!CustomFieldCategoryImportValidator.validateOrderNumber(category,categoryName, csvReader.get("ORDER_NUMBER"), dataValidationMessages)) {
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
		throw new CustomFieldSystemException("Unexpected I/O exception whilst reading the data file");
	}
	catch (Exception ex) {
		log.error("processMatrixFile Exception stacktrace:", ex);
		throw new CustomFieldSystemException("Unexpected exception occurred when trying to process data file");
	}
	finally {
		// Clean up the IO objects
//		timer.stop();
//		log.debug("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
		log.debug("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
//		if (timer != null)
//			timer = null;
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
		validationMessages = validateMatrixFileFormat(inputStream, inputStream.toString().length(),false);
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
 * Validate  custom field type for the category. 
 * @param fieldName
 * @param fieldType
 * @param errorMessages
 * @return
 *//*
private static  boolean validateCustomFieldType(CustomFieldCategory category,String categoryName, String fieldType, Collection<String> errorMessages) {
	boolean isValid = false;
	
	if(category!=null && category.getCustomFieldType()!=null && category.getCustomFieldType().getName().equalsIgnoreCase(fieldType)){
		selectedCustomFieldType=cusFieldType;
		isValid=true;
	}
	if(isValid==false){
		errorMessages.add(CustomFieldValidationMessage.invalidCustomFieldTypeForCategory(categoryName, fieldType));
	}
	return isValid;
}*/
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
 * Validate Parent category for the category.
 * 
 * @param category
 * @param categoryName
 * @param fieldType
 * @param errorMessages
 * @return
 */
private static  boolean validateParentCategory(List<CustomFieldCategory> customFieldCategoryLstForCustomFieldType,CustomFieldCategory category,String categoryName, String fieldType, Collection<String> errorMessages) {
	//Check with customfieldType to agree.
	if(!customFieldCategoryLstForCustomFieldType.isEmpty()){
		for (CustomFieldCategory categoryForthisCustomfieldType : customFieldCategoryLstForCustomFieldType) {
			if(categoryForthisCustomfieldType.getName().equalsIgnoreCase(fieldType)){
				return true;
			}
		}
	}	
	if(category.getParentCategory()==null && (fieldType.isEmpty() || fieldType.length()<=0)){
		return true;
	}else{
		errorMessages.add(CustomFieldValidationMessage.invalidParentCategryForCategory(categoryName, fieldType));
		return false;
	}
	
}
/**
 * Validate Parent category order number.
 * 
 * @param category
 * @param categoryName
 * @param fieldType
 * @param errorMessages
 * @return
 */
private static  boolean validateOrderNumber(CustomFieldCategory category,String categoryName, String fieldType, Collection<String> errorMessages) {
	boolean isValid = false;
	isValid=isNumeric(fieldType);
	if(isValid==false){
		errorMessages.add(CustomFieldValidationMessage.invalidOrderNumberForCategory(categoryName, fieldType));
	}
	return isValid;
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

private static boolean isNumeric(String str) {  
	try {  
		Long d = Long.parseLong(str);  
	} catch(NumberFormatException nfe) {  
		return false;  
	}  
	return true;  
}

}