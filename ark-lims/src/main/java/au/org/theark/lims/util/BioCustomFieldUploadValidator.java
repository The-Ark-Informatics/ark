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
package au.org.theark.lims.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.StringTokenizer;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.XLStoCSV;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.worksheet.ArkGridCell;
import au.org.theark.lims.service.ILimsService;

import com.csvreader.CsvReader;

/**
 * Bio CustomField UploadValidator provides support for validating matrix-formatted files.
 * 
 * @author travis
 */
public class BioCustomFieldUploadValidator {
	private static final long		serialVersionUID			= -1933045886948087734L;
	private static Logger			log							= LoggerFactory.getLogger(BioCustomFieldUploadValidator.class);

	private static final String		BIOSPECIMEN				= "Biospecimen";
	private static final String		BIOCOLLECTION			= "Biocollection";
	
	@SuppressWarnings("unchecked")
	private IArkCommonService		iArkCommonService;
	
	private ILimsService			iLimsService;
	private Long					studyId;
	private Study					study;
	java.util.Collection<String>	fileValidationMessages	= new java.util.ArrayList<String>();
	java.util.Collection<String>	dataValidationMessages	= new java.util.ArrayList<String>();
	private HashSet<Integer>		existantSubjectUIDRows;
	private HashSet<Integer>		nonExistantUIDs;
	private HashSet<ArkGridCell>	errorCells;
	private SimpleDateFormat		simpleDateFormat			= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private char					delimiterCharacter		= au.org.theark.core.Constants.DEFAULT_DELIMITER_CHARACTER;
	private String					fileFormat					= au.org.theark.core.Constants.DEFAULT_FILE_FORMAT;
	private int						row							= 1;

	public BioCustomFieldUploadValidator() {
		super();
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		this.study = iArkCommonService.getStudy(studyId);
		this.existantSubjectUIDRows = new HashSet<Integer>();
		this.nonExistantUIDs = new HashSet<Integer>();
		this.errorCells = new HashSet<ArkGridCell>();
		simpleDateFormat.setLenient(false);
	}

	public BioCustomFieldUploadValidator(Study study) {
		super();
		this.study = study;
		this.existantSubjectUIDRows = new HashSet<Integer>();
		this.nonExistantUIDs = new HashSet<Integer>();
		this.errorCells = new HashSet<ArkGridCell>();
		simpleDateFormat.setLenient(false);
	}

	@SuppressWarnings("unchecked")
	public BioCustomFieldUploadValidator(IArkCommonService iArkCommonService, ILimsService iLimsService) {
		super();
		this.iArkCommonService = iArkCommonService;
		this.iLimsService = iLimsService;
		Subject currentUser = SecurityUtils.getSubject();
		studyId = (Long) currentUser.getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		this.study = iArkCommonService.getStudy(studyId);
		this.existantSubjectUIDRows = new HashSet<Integer>();
		this.nonExistantUIDs = new HashSet<Integer>();
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
		return existantSubjectUIDRows;
	}

	public void setInsertRows(HashSet<Integer> insertRows) {
		this.existantSubjectUIDRows = insertRows;
	}

	public HashSet<Integer> getUpdateRows() {
		return nonExistantUIDs;
	}

	public void setUpdateRows(HashSet<Integer> updateRows) {
		this.nonExistantUIDs = updateRows;
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
	public Collection<String> validateBiocollectionCustomFieldFileFormat(UploadVO uploadVo) {
		java.util.Collection<String> validationMessages = null;
		try {
			InputStream inputStream = uploadVo.getFileUpload().getInputStream();
			String filename = uploadVo.getFileUpload().getClientFileName();
			fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			delimiterCharacter = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();
			validationMessages = validateBiocollectionCustomFieldFileFormat(inputStream, fileFormat, delimiterCharacter);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}


	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN... Where N is any number of columns
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validateBiospecimenCustomFieldFileFormat(UploadVO uploadVo) {
		java.util.Collection<String> validationMessages = null;
		try {
			InputStream inputStream = uploadVo.getFileUpload().getInputStream();
			String filename = uploadVo.getFileUpload().getClientFileName();
			fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			delimiterCharacter = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();
			validationMessages = validateBiospecimenCustomFieldFileFormat(inputStream, fileFormat, delimiterCharacter);
		}
		catch (IOException e) {
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
	public Collection<String> validateBiocollectionCustomFieldFileFormat(InputStream inputStream, String fileFormat, char delimChar) {
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
			validationMessages = validateBiocollectionCustomFieldMatrixFileFormat(inputStream, inputStream.toString().length(), fileFormat, delimChar);
		}
		catch (FileFormatException ffe) {
			log.error(au.org.theark.lims.web.Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(au.org.theark.lims.web.Constants.ARK_BASE_EXCEPTION + abe);
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
	public Collection<String> validateBiospecimenCustomFieldFileFormat(InputStream inputStream, String fileFormat, char delimChar) {
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
			validationMessages = validateBiospecimenCustomFieldMatrixFileFormat(inputStream, inputStream.toString().length(), fileFormat, delimChar);
		}
		catch (FileFormatException ffe) {
			log.error(au.org.theark.lims.web.Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(au.org.theark.lims.web.Constants.ARK_BASE_EXCEPTION + abe);
		}
		return validationMessages;
	}

	
	/**
	 * Validates the file in the custom field list.
	 * 
	 * Requires.  SubjectUID specified in row one.  And all Fields must be valid for its type
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
	public java.util.Collection<String> validateBiospecimenCustomFieldMatrixFileFormat(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkBaseException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 0;
		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			if (inLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + inLength);
			}

			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			csvReader.readHeaders();
			String[] headerColumnArray = csvReader.getHeaders();
			boolean headerError = false;
			boolean hasBiospecimenUIDHeader = false;
			ArkFunction biospecimenCustomFieldArkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN);
			List<String> badHeaders = new ArrayList<String>();
			
			for(String header : headerColumnArray){																						
				if(header.equalsIgnoreCase("BIOSPECIMENUID")) {
					hasBiospecimenUIDHeader = true;
				}
				else{
					//TODO just make it get all of them and look through in memory rather than 10-50-300-500 selects?
					if(iArkCommonService.getCustomFieldByNameStudyArkFunction(header, study, biospecimenCustomFieldArkFunction) == null){
						badHeaders.add(header);
						headerError = true;
					}
				}
			}
			if (headerError || !hasBiospecimenUIDHeader) {
				// Invalid file format
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("The specified delimiter type was: " + delimiterCharacter + ".\n\n");
				stringBuffer.append("This file is not valid because; \n");
				fileValidationMessages.add(stringBuffer.toString());
				if(!hasBiospecimenUIDHeader){
					fileValidationMessages.add("The column name \"BIOSPECIMENUID\" must exist as the header of the first column.\n");
				}
				for (String badHeader : badHeaders) {
					fileValidationMessages.add("The column name " + badHeader + " does not with an existing biospecimen-specific custom field.\n");
				}
				log.warn("failed header validation");
			}
			else{
				log.warn("passed header validation");
			}
			row = 1;
		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the bio custom data file");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process bio custom data file");
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
					//TODO ASAP : re-evaluate below
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
	 * Validates the file in the custom field list.
	 * 
	 * Requires.  SubjectUID specified in row one.  And all Fields must be valid for its type
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
	public java.util.Collection<String> validateBiocollectionCustomFieldMatrixFileFormat(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkBaseException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 0;
		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			if (inLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + inLength);
			}

			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			csvReader.readHeaders();
			String[] headerColumnArray = csvReader.getHeaders();
			boolean headerError = false;
			boolean hasBiocollectionUIDHeader = false;							//naming is not great but we are stuck with it for now as it used throughout app/db
			ArkFunction biocollectionCustomFieldArkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION);
			List<String> badHeaders = new ArrayList<String>();
			
			for(String header : headerColumnArray){																						
				if(header.equalsIgnoreCase("BIOCOLLECTIONUID")) {
					hasBiocollectionUIDHeader = true;
				}
				else{
					//TODO just make it get all of them and look through in memory rather than 10-50-300-500 selects?
					if(iArkCommonService.getCustomFieldByNameStudyArkFunction(header, study, biocollectionCustomFieldArkFunction) == null){
						badHeaders.add(header);
						headerError = true;
					}
				}
			}
			if (headerError || !hasBiocollectionUIDHeader) {
				// Invalid file format
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("The specified delimiter type was: " + delimiterCharacter + ".\n\n");
				stringBuffer.append("This file is not valid because; \n");
				fileValidationMessages.add(stringBuffer.toString());
				if(!hasBiocollectionUIDHeader){
					fileValidationMessages.add("The column name \"BIOCOLLECTIONUID\" must exist as the header of the first column.\n");
				}
				for (String badHeader : badHeaders) {
					fileValidationMessages.add("The column name " + badHeader + " does not with an existing biocollection-specific custom field.\n");
				}
				log.warn("failed header validation");
			}
			else{
				log.warn("passed header validation");
			}
			row = 1;
		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the bio custom data file");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process bio custom data file");
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
					//TODO ASAP : re-evaluate below
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
	 * END OF CODE WHICH VALDIATES RAW FILE ISSUES AND HEADER ISSUES
	 */

	
	
	
	
	
	/**
	 * START OF CODE WHICH JUST LOOKS AT VALIDITY OF DATA AND TYPES
	 */
	
	
	/**
	 * Validates the file in the default "matrix" file data assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN... Where N is any number of columns
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validateBiospecimenCustomFieldFileData(UploadVO uploadVo, List<String> uidsToUpdateReference) {
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

			//todo split if we see the differences validationMessages = validateBiospecimenSubjectFileData(inputStream, fileFormat, delimiterCharacter, uidsToUpdateReference);
			 validationMessages = validateSubjectFileData(inputStream, fileFormat, delimiterCharacter, uidsToUpdateReference, BIOSPECIMEN);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}


	/**
	 * Validates the file in the default "matrix" file data assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN... Where N is any number of columns
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validateBiocollectionCustomFieldFileData(UploadVO uploadVo, List<String> uidsToUpdateReference) {
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

			//todo split if we see the differences validationMessages = validateBiocollectionSubjectFileData(inputStream, fileFormat, delimiterCharacter, uidsToUpdateReference);
			validationMessages = validateSubjectFileData(inputStream, fileFormat, delimiterCharacter, uidsToUpdateReference, BIOCOLLECTION);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}

	
	
	/*
	 * TODO ASAP:  investigate this sort of method...all it seems to do is catch an exception and log it (seemingly letting 
	 * the file appear validated.  This is likely to be replicated many places 
	 * @param inputStream
	 * @param fileFormat
	 * @param delimChar
	 * @param uidsToUpdateReference
	 * @return
	 */
	public Collection<String> validateSubjectFileData(InputStream inputStream, String fileFormat, char delimChar, 
			List<String> uidsToUpdateReference, String bioFieldType) {
		java.util.Collection<String> validationMessages = null;

		try {
			//TODO performance of valdation now approx 60-90K records per minute, file creation after validation doubles that
			//I think this is acceptable for now to keep in user interface.  Can make some slight improvements though, and if it bloats with more fields could be part of batch too
			validationMessages = validateMatrixCustomFileData(inputStream, inputStream.toString().length(), fileFormat, delimChar, Long.MAX_VALUE, 
					uidsToUpdateReference, bioFieldType);
		}
		catch (FileFormatException ffe) {
			log.error(au.org.theark.lims.web.Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(au.org.theark.lims.web.Constants.ARK_BASE_EXCEPTION + abe);
		}
		return validationMessages;
	}
	
	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN...
	 * 
	 * TODO:  remove globals unless their is a legit reason
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
	public java.util.Collection<String> validateMatrixCustomFileData(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr, 
			long rowsToValidate, List<String> uidsToUpdateReference, String bioFieldType) throws FileFormatException, ArkSystemException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 1;
		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			String[] stringLineArray;
			if (inLength <= 0) {
				throw new FileFormatException("The input files' size was not greater than 0.  Actual length reported: " + inLength);
			}

			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			csvReader.readHeaders();
			List<String> bioUIDsAlreadyExisting = null;	//TODO evaluate data in future to know if should get all id's in the csv, rather than getting all id's in study to compre
			List<String> fieldNameCollection = Arrays.asList(csvReader.getHeaders());	

			ArkFunction bioCustomFieldArkFunction = null;
			if(bioFieldType.equalsIgnoreCase(BIOSPECIMEN)){
				bioUIDsAlreadyExisting = iLimsService.getAllBiospecimenUIDs(study);
				bioCustomFieldArkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_BIOSPECIMEN);
			}
			else if(bioFieldType.equalsIgnoreCase(BIOCOLLECTION)){
				bioUIDsAlreadyExisting = iLimsService.getAllBiocollectionUIDs(study);
				bioCustomFieldArkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_LIMS_COLLECTION);
			}
			else{
				log.error("invalid biofield type...this should never happen");//TODO fix exception handling globally
			}

			List<CustomFieldDisplay> cfdsThatWeNeed = iArkCommonService.getCustomFieldDisplaysIn(fieldNameCollection, study, bioCustomFieldArkFunction);
			
			while (csvReader.readRecord()) {
				stringLineArray = csvReader.getValues();//i might still need this or might not now that i am evaluating by name ... TODO evaluate
				String bioId = stringLineArray[0];	// First/0th column should be the SubjectUID	
				if(!bioUIDsAlreadyExisting.contains(bioId)){
					nonExistantUIDs.add(row);//TODO test and compare array.
					for(CustomFieldDisplay cfd : cfdsThatWeNeed){
						errorCells.add(new ArkGridCell(csvReader.getIndex(cfd.getCustomField().getName()), row));
					}
					errorCells.add(new ArkGridCell(0, row));
				}
				else{
					if(uidsToUpdateReference.contains(bioId)){
						for(CustomFieldDisplay cfd : cfdsThatWeNeed){
							errorCells.add(new ArkGridCell(csvReader.getIndex(cfd.getCustomField().getName()), row));
						}
						errorCells.add(new ArkGridCell(0, row));
						dataValidationMessages.add("Subject " + bioId + " on row " + row + " is listed multiple times in this file.  " +
								"Please remove this row and retry.");
					}
					else{
						uidsToUpdateReference.add(bioId);
						CustomField customField = null;		
						for(CustomFieldDisplay cfd : cfdsThatWeNeed){
							customField = cfd.getCustomField();
							String theDataAsString = csvReader.get(cfd.getCustomField().getName());
							if(theDataAsString!=null && !theDataAsString.isEmpty()){
								//TODO : also check if the value == "missingvaluePatternThingy" , then dont validate
								if(customField.getMissingValue()!=null && customField.getMissingValue().toString().equalsIgnoreCase(theDataAsString)){
									//then move on and don't validate it...it goes straight in
								}
								else
								{
									//log.info("customField = " + customField==null?"null":customField.getName());
									if(!validateFieldData(customField, theDataAsString, bioId, bioFieldType, dataValidationMessages)){
										errorCells.add(new ArkGridCell(csvReader.getIndex(cfd.getCustomField().getName()), row));
									}								
								}
							}
						}
						existantSubjectUIDRows.add(row);
					}
				}
				row++;
			}
		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the biocustom  data file");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process bio custom data file");
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

		//TODO:  test hashset this i.intvalue or left hashset value??
		for (Iterator<Integer> iterator = nonExistantUIDs.iterator(); iterator.hasNext();) {
			Integer i = (Integer) iterator.next();
			dataValidationMessages.add(bioFieldType + " on row " + i.intValue() + " does not exist in the database.  " +
					"Please remove this row and retry or run upload/create this " + bioFieldType + " first.");
		}
		return dataValidationMessages;
	}

	/**
	 * Returns true of the field data value is a valid format, either NUMBER, CHARACTER or DATE as specified in the data dictionary
	 * 
	 * @param customField
	 * @return boolean
	 */
	public static boolean isValidFieldData(CustomField customField, String value, String bioUID, String bioFieldType, java.util.Collection<String> errorMessages) {
		boolean isValidFieldData = true;
		//TODO ASAP is null coming in acceptable? or do we just just check before call... if value null return false?
		
		// Number field type
		if (customField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
			try {
				Double.parseDouble(value);
			}
			catch (NumberFormatException nfe) {
				errorMessages.add(fieldDataNotDefinedType(customField, value, bioUID, bioFieldType));
				log.error("Field data number format exception " + nfe.getMessage());
				isValidFieldData = false;
			}
			catch (NullPointerException npe) {
				log.error("Field data null pointer exception " + npe.getMessage());
				isValidFieldData = false;
			}
		}

		// Character field type
		if (customField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
			//TODO previously had simple null check by way of making an exception here.... what do we need to validation?	
		}

		// Date field type
		if (customField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
			try {//TODO : think about defining date format with the field, particularly after i18n and if datetime needed 
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFormat.parse(value);
			}
			catch (ParseException pe) {
				errorMessages.add(fieldDataNotValidDate(customField, value, bioUID, bioFieldType));
				log.error("Field data date parse exception " + pe.getMessage());
				isValidFieldData = false;
			}
			catch (NullPointerException npe) {
				log.error("Field data null pointer exception " + npe.getMessage());
				isValidFieldData = false;
			}
		}

		return isValidFieldData;
	}

	/**
	 * Returns true if field data value is within the defined range as specified in the data dictionary
	 * 
	 * @param customField
	 * @return boolean
	 */
	public static boolean isInValidRange(CustomField customField, String valueToValidate, String bioUID, String bioFieldType, java.util.Collection<String> errorMessages) {
		boolean isInValidRange = true;
		//Field field = fieldData.getField();
		String minValue = customField.getMinValue();
		String maxValue = customField.getMaxValue();
		//log.warn("about to validate customField " + customField.getName() + " against value = " + valueToValidate);
		if(valueToValidate!=null && customField.getMissingValue()!=null && valueToValidate.trim().equalsIgnoreCase(customField.getMissingValue().trim())) {
			return isInValidRange;//TODO investigate 
		}

		if ((minValue == null || minValue.isEmpty()) && (maxValue == null || maxValue.isEmpty())) {
			return isInValidRange;
		}

		if (customField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
			try {
				Double doubleMinValue = Double.parseDouble(customField.getMinValue());
				Double doubleMaxValue = Double.parseDouble(customField.getMaxValue());
				Double doubleFieldValue = Double.parseDouble(valueToValidate);

				if ((doubleFieldValue > doubleMaxValue) || (doubleFieldValue < doubleMinValue)) {
					if ((doubleFieldValue > doubleMaxValue)) {
						errorMessages.add( bioFieldType + " " + bioUID + " has a value: " + valueToValidate + " which is greater than the maximum allowed value of " + doubleMaxValue);
					}
					if ((doubleFieldValue < doubleMinValue)) {
						errorMessages.add( bioFieldType + " " + bioUID + " has a value: " + valueToValidate + " which  is less than the minimum allowed value of " + doubleMinValue);
					}
					isInValidRange = false;
				}
			}
			catch (NumberFormatException nfe) {
				log.error("Field data number format exception " + nfe.getMessage());
				isInValidRange = false;
			}
			catch (NullPointerException npe) {
				log.error("Field data null pointer exception " + npe.getMessage());
				isInValidRange = false;
			}
		}
		else if (customField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
			if (customField.getMinValue() != null && customField.getMaxValue() != null) {
				try {
					DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
					dateFormat.setLenient(false);

					Date dateMinValue = dateFormat.parse(customField.getMinValue());
					Date dateMaxValue = dateFormat.parse(customField.getMaxValue());
					Date dateFieldValue = dateFormat.parse(valueToValidate);

					if (dateFieldValue.after(dateMaxValue) || dateFieldValue.before(dateMinValue)) {
						if (dateFieldValue.after(dateMaxValue)) {
							errorMessages.add(valueToValidate + " is greater than the maximum allowed value of " + dateMaxValue);
						}
						if (dateFieldValue.before(dateMinValue)) {
							errorMessages.add(valueToValidate + " is less than the minimum allowed value of " + dateMinValue);
						}
						isInValidRange = false;
					}
				}
				catch (ParseException pe) {
					isInValidRange = false;
				}
			}
		}
		//log.warn("about to return " + isInValidRange);
		return isInValidRange;
	}

	/**
	 * Returns true if the field data value is within the discrete range as defined in the data dictionary
	 * 
	 * @param customfield
	 * @return boolean
	 */
	public static boolean isInEncodedValues(CustomField customField, String value, String bioUID, String bioFieldType, java.util.Collection<String> errorMessages) {
		boolean inEncodedValues = true;
		
		if(customField.getMissingValue()!=null && value!=null && value.trim().equalsIgnoreCase(customField.getMissingValue().trim())) {
			return inEncodedValues;
		}

		// Validate if encoded values is defined, and not a DATE fieldType
		if (customField.getEncodedValues() != null 
				&& !customField.getEncodedValues().isEmpty() 
				&& !customField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
			
			try {
				StringTokenizer stringTokenizer = new StringTokenizer(customField.getEncodedValues(), Constants.ENCODED_VALUES_TOKEN);

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
					errorMessages.add(fieldDataNotInEncodedValues(customField, value, bioUID, bioFieldType));
				}

			}
			catch (NullPointerException npe) {
				log.error("Field data null format exception " + npe.getMessage());
				inEncodedValues = false;
			}

		}
		return inEncodedValues;
	}

	/**
	 * Returns true of the field data value is a valididated
	 * 
	 * @param customField
	 * @param errorMessages
	 * @return boolean
	 */
	public static boolean validateFieldData(CustomField customField, String value, String bioUID, String bioFieldType, java.util.Collection<String> errorMessages) {
		boolean isValid = true;
		boolean isValidFieldData = true;
		boolean isValidEncodedValues = true;
		boolean isValidRange = true;

		isValidFieldData = isValidFieldData(customField, value, bioUID, bioFieldType, errorMessages);
		//log.info("isValidFieldData " + isValidFieldData );
		isValidEncodedValues = isInEncodedValues(customField,value, bioUID, bioFieldType, errorMessages);
		//log.info("isValidEncodedValues " + isValidEncodedValues );
		isValidRange = isInValidRange(customField, value, bioUID, bioFieldType, errorMessages);
		//log.info("isInValidRange " + isValidRange );
		isValid = (isValidFieldData && isValidEncodedValues && isValidRange);
		//log.info("isvalidoverall " + isValid );
		return (isValid);
	}

	/**
	 * Returns field not of the defined type error message
	 * 
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataSubjectUidNotFound(String bioUID, String bioFieldType) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(bioFieldType + " ");
		stringBuffer.append(bioUID);
		stringBuffer.append(" ");
		stringBuffer.append("was not found in the database. Please check and try again.");
		return (stringBuffer.toString());
	}

	/**
	 * Returns field greater than defined max value error message
	 * TODO: Remove after change to new tables
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataGreaterThanMaxValue(CustomField field, String value, String bioUID, String bioFieldType) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(bioFieldType + " ");
		stringBuffer.append(bioUID);
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(value);
		stringBuffer.append(" is greater than the defined maximum value: ");
		stringBuffer.append(field.getMaxValue());
		return (stringBuffer.toString());
	}

	/**
	 * Returns field less than defined min value error message
	 * TODO: Remove after change to new tables
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataLessThanMinValue(CustomField field, String value, String bioUID, String bioFieldType) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(bioFieldType + " ");
		stringBuffer.append(bioUID);
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(value);
		stringBuffer.append(" is less than the defined minimum value: ");
		stringBuffer.append(field.getMinValue());
		return (stringBuffer.toString());
	}

	/**
	 * Returns field not within the defined encoded values error message
	 * TODO: Remove after change to new tables
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataNotInEncodedValues(CustomField field, String value, String bioUID, String bioFieldType) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(bioFieldType + " ");
		stringBuffer.append(bioUID);
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(value);
		stringBuffer.append(" is not in the encoded values: ");
		stringBuffer.append(field.getEncodedValues().replace('\n', ' '));
		return (stringBuffer.toString());
	}

	/**
	 * Returns field not a valid date format error message
	 * TODO: Remove after change to new tables
	 * @param field
	 * @param fieldData
	 * @return String
	 */
	public static String fieldDataNotValidDate(CustomField field, String value, String bioUID, String bioFieldType) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append(bioFieldType + " ");
		stringBuffer.append(bioUID);
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(value);
		stringBuffer.append(" is not in the valid date format of: ");
		stringBuffer.append(Constants.DD_MM_YYYY.toLowerCase());
		return (stringBuffer.toString());
	}

	/**
	 * Returns field not of the defined type error message
	 * 
	 * @param field
	 * @param fieldData
	 * @return String
	 **/
	public static String fieldDataNotDefinedType(CustomField field, String value, String bioUID, String bioFieldType) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append(bioFieldType + " ");
		stringBuffer.append(bioUID);
		stringBuffer.append(": ");
		stringBuffer.append("The field ");
		stringBuffer.append(field.getName().toString());
		stringBuffer.append(" value ");
		stringBuffer.append(value);
		stringBuffer.append(" is not the defined field type: ");
		stringBuffer.append(field.getFieldType().getName());
		return (stringBuffer.toString());
	}
	
	public static String dateCollectedNotValidDate(String bioUID, String bioFieldType, String dateCollectedStr) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append(bioFieldType + " ");
		stringBuffer.append(bioUID);
		stringBuffer.append(": ");
		stringBuffer.append(" with the date collected: ");
		stringBuffer.append(dateCollectedStr);
		stringBuffer.append(" is not in the valid date format of: ");
		stringBuffer.append(Constants.DD_MM_YYYY.toLowerCase());
		return (stringBuffer.toString());
	}


	public static String fieldNotFound(String fieldName) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append("The field ");
		stringBuffer.append(fieldName);
		stringBuffer.append(" was not found in the database. Please check the name and try again, or add the field to the Data Dictionary.");
		return (stringBuffer.toString());
	}

	public static String statusNotValid(String bioUID, String bioFieldType, String statusStr) {
		StringBuffer stringBuffer = new StringBuffer();
		stringBuffer.append("Error: ");
		stringBuffer.append(bioFieldType + " ");
		stringBuffer.append(bioUID);
		stringBuffer.append(": ");
		stringBuffer.append(" with the status: ");
		stringBuffer.append(statusStr);
		stringBuffer.append(" is not amongst the valid status options.");
		return (stringBuffer.toString());
	}

}
