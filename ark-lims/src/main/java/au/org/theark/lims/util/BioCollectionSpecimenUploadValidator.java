/*******************************************************************************
 * Copyright © The University of Melbourne and Peter MacCallum Cancer Centre - All Rights Reserved
 * Unauthorized copying of this file, via any medium, is strictly prohibited.
 * Proprietary and confidential.
 * Written by Sanjay Maddumarachchi <sanjaya.maddumarachchi@unimelb.edu.au>, May 2015
 *  
 *
 *******************************************************************************/
package au.org.theark.lims.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.csvreader.CsvReader;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.TreatmentType;
import au.org.theark.core.model.lims.entity.Unit;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.XLStoCSV;
import au.org.theark.core.vo.BiospecimenLocationVO;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.worksheet.ArkGridCell;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;
import jxl.Workbook;
import jxl.read.biff.BiffException;

/**
 * BiospecimenUploadValidator provides support for validating Biospecimen matrix-formatted files.
 * 
 * @author cellis
 */
public class BioCollectionSpecimenUploadValidator {
	private static Logger			log							= LoggerFactory.getLogger(BioCollectionSpecimenUploadValidator.class);
	@SuppressWarnings("unchecked")
	private IArkCommonService		iArkCommonService;
	private ILimsService			iLimsService;
	private IInventoryService		iInventoryService;
	private Long					studyId;
	private Study					study;
	java.util.Collection<String>	fileValidationMessages	= new java.util.ArrayList<String>();
	java.util.Collection<String>	dataValidationMessages	= new java.util.ArrayList<String>();
	private HashSet<Integer>		insertRows;
	private HashSet<Integer>		updateRows;
	private HashSet<ArkGridCell>	errorCells;
	private long					recordCount;
	private long					srcLength					= -1;
//	private StopWatch					timer							= null;
	private char					delimiterCharacter		= au.org.theark.core.Constants.DEFAULT_DELIMITER_CHARACTER;
	private String					fileFormat					= au.org.theark.core.Constants.DEFAULT_FILE_FORMAT;
	private SimpleDateFormat		simpleDateFormat			= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private int						row							= 1;

	@SuppressWarnings("unchecked")
	public BioCollectionSpecimenUploadValidator(Study study, IArkCommonService iArkCommonService, ILimsService iLimsService, IInventoryService iInventoryService) {
		super();
		this.iArkCommonService = iArkCommonService;
		this.iLimsService = iLimsService;
		this.iInventoryService = iInventoryService;
		this.study = study;
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
	 * Validate the Lims file format from here.
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validateLimsFileFormatAndHeaderDetail(UploadVO uploadVo,String[] header) {
		java.util.Collection<String> validationMessages = null;
		try {
			InputStream inputStream = uploadVo.getFileUpload().getInputStream();
			String filename = uploadVo.getFileUpload().getClientFileName();
			fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
			delimiterCharacter = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();
			validationMessages = validateLimsFileFormatAndHeader(inputStream, fileFormat, delimiterCharacter,header);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}
	/**
	 * Validates the file in the default "matrix" file format assumed: 
	 * 
	 * @param inputStream
	 *           is the input stream of the file
	 * @param fileFormat
	 *           is the file format (eg txt)
	 * @param delimChar
	 *           is the delimiter character of the file (eg comma)
	 * @return a collection of validation messages
	 */
	private Collection<String> validateLimsFileFormatAndHeader(InputStream inputStream, String fileFormat, char delimChar,String[] header) {
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
					delimiterCharacter = ',';
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
				catch (IOException e) {
					log.error(e.getMessage());
				}
			}
			validationMessages = validateLimsFileFormatAndHeader(inputStream, inputStream.toString().length(), fileFormat, delimChar,header);
		}
		catch (FileFormatException ffe) {
			log.error(Constants.FILE_FORMAT_EXCEPTION + ffe);
		}
		catch (ArkBaseException abe) {
			log.error(Constants.ARK_BASE_EXCEPTION + abe);
		}
		return validationMessages;
	}
	/**
	 * Validates the Lims file in the default "matrix" file format assumed
	 * 
	 * @param fileInputStream
	 * @param inLength
	 * @param inFileFormat
	 * @param inDelimChr
	 * @return
	 * @throws FileFormatException
	 * @throws ArkBaseException
	 */
	private java.util.Collection<String> validateLimsFileFormatAndHeader(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr,String[] header) throws FileFormatException,
			ArkBaseException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);

			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			// Set field list (note 2th column to Nth column)
			// BiospecimenUID SITE F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());
			String[] headerColumnArray = csvReader.getHeaders();

			Collection<String> biospecimenColumns = new ArrayList<String>();
			String[] biospecimenHeaderColumnArray =header;
			boolean headerError = false;
			for (int i = 0; i < biospecimenHeaderColumnArray.length; i++) {
				String colName = biospecimenHeaderColumnArray[i];
				biospecimenColumns.add(colName);
			}

			for (int i = 0; i < headerColumnArray.length; i++) {
				String colName = headerColumnArray[i];
				if (!biospecimenColumns.contains(colName)) {
					headerError = true;
					break;
				}
			}

			if (headerError) {
				// Invalid file format
				StringBuffer stringBuffer = new StringBuffer();
				stringBuffer.append("Error: The specified file does not appear to conform to the expected file format.\n");
				stringBuffer.append("The specified fileformat was: " + fileFormat + ".\n");
				stringBuffer.append("The specified delimiter type was: " + delimiterCharacter + ".\n");
				stringBuffer.append("The default format should be as follows:\n");

				// Column headers
				for (String column : header) {
					stringBuffer.append(column);
					stringBuffer.append(delimiterCharacter);
				}
				stringBuffer.deleteCharAt(stringBuffer.length()-1);
				stringBuffer.append("\n");

				fileValidationMessages.add(stringBuffer.toString());

				for (int i = 0; i < headerColumnArray.length; i++) {
					if (!biospecimenColumns.contains(headerColumnArray[i].toUpperCase())) {
						fileValidationMessages.add("Error: the column name " + headerColumnArray[i] + " is not a valid column name.");
					}
				}
			}

			row = 1;
		}
		catch (IOException ioe) {
			log.error("locationFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the Location data file");
		}
		catch (Exception ex) {
			log.error("locationFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process Location data file");
		}
		finally {
			// Clean up the IO objects
			
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
	 * Validates the file in the default "matrix" file data assumed: collection details.
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validateBiocollectionFileData(UploadVO uploadVo) {
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
			try {
				validationMessages = validateMatrixBiocollectionFileData(inputStream,inputStream.toString().length(), fileFormat, delimiterCharacter);
			} catch (FileFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ArkSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}
	/**
	 * Validates the file in the default "matrix" file data assumed: Inventory details.
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validateBiospecimenInvetoryFileData(UploadVO uploadVo) {
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
			try {
				validationMessages = validateMatrixBiospecimenInventoryFileData(inputStream,inputStream.toString().length(), fileFormat, delimiterCharacter);
			} catch (FileFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ArkSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}
	/**
	 * Validates the file in the default "matrix" file data assumed: BiospecimenUID,FIELD1,FIELD2,FIELDN... Where N is any number of columns
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validateBiospecimenFileData(UploadVO uploadVo) {
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
			try {
				validationMessages = validateMatrixBiospecimenFileData(inputStream,inputStream.toString().length(), fileFormat, delimiterCharacter);
			} catch (FileFormatException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (ArkSystemException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}
	 /** Validates the file in the default "matrix" file format assumed: BiospecimenUID,FIELD1,FIELD2,FIELDN...
		 * Where N is any number of columns
	 * 
	 * 
	 * 
	 * 
	 * 
	 * attempting to apply logic as seen: https://the-ark.atlassian.net/browse/ARK-790
	 * 
	 * THERE IS NO UPDATING

		For Biospecimen;
		if you have auto gen & you dont specific an id...then upload and generate biospec-id
		if you have auto gen & you do specific an id...then error message - don't upload
		if you DON'T have auto gen & you do specific an id...then
		...if something exists with that id already...THEN ERROR
		...else create the biospecimen with given id
		if you DON'T have auto gen & you dont specific an id...then error - don't upload
		
		For Biocollection;
		if you have auto gen & you dont specific an id...then upload and gen biocol
		if you have auto gen & you do specific an id
		...then look for existing biocol to point at
		...if exists then put biospec in that biolcollection
		...ELSE error
		if you DON'T have auto gen & you do specific an id
		...then if biocol already exists, use that existing biocol and tie biospec to it
		........however if biocol doesnt exist...create biocol with the given id
		if you DON'T have auto gen & you dont specific an id...then error 
*/	
	/**
	 * Validate bio collection file data.
	 * 
	 * @param fileInputStream
	 * @param inLength
	 * @param inFileFormat
	 * @param inDelimChr
	 * @return
	 * @throws FileFormatException
	 * @throws ArkSystemException
	 */
	private java.util.Collection<String> validateMatrixBiocollectionFileData(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException,
	ArkSystemException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		List<String> currentBiocollectuidLst=new ArrayList<String>();
		row = 1;
		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}
			csvReader.readHeaders();
			srcLength = inLength - csvReader.getHeaders().toString().length();
			String[] fieldNameArray = csvReader.getHeaders();
			// Loop through all rows in file
			while (csvReader.readRecord()) {
				LinkSubjectStudy linkSubjectStudy=null;
				String subjectUID=csvReader.get("SUBJECTUID");
				if(subjectUID!=null && !subjectUID.trim().isEmpty()){
					linkSubjectStudy = (iArkCommonService.getSubjectByUIDAndStudy(subjectUID, study));
					if(linkSubjectStudy==null){
						errorForSubjectUIDNotExsist(csvReader, subjectUID);
					}
				}else {
					StringBuilder errorString = new StringBuilder();
					errorString.append("Error: Row ");
					errorString.append(row);
					errorString.append(": SubjectUID: ");
					errorString.append(" can not be empty.");
					dataValidationMessages.add(errorString.toString());
					errorCells.add(new ArkGridCell(csvReader.getIndex("SUBJECTUID"), row));
				}
				
				//Handiling biocollection uids here.....
				BioCollection bioCollection = null;
				String biocollectionUID=csvReader.get("BIOCOLLECTIONUID");
				if(study.getAutoGenerateBiocollectionUid()){
					if(biocollectionUID!=null && !biocollectionUID.trim().isEmpty()){
						if(!currentBiocollectuidLst.isEmpty()){
							if(currentBiocollectuidLst.contains(biocollectionUID)){
								createErroForDuplicateBiocollectionUid(csvReader,biocollectionUID);
							}
						}
						currentBiocollectuidLst.add(biocollectionUID);
						bioCollection = iLimsService.getBioCollectionForStudySubjectByUID(biocollectionUID, study, linkSubjectStudy);	
						if(bioCollection == null){
							insertRows.add(row);	
						}else{
							updateRows.add(row);
						}
					}else{
						insertRows.add(row);	
					}
				}else{
					if(biocollectionUID!=null && !biocollectionUID.trim().isEmpty()){
						if(!currentBiocollectuidLst.isEmpty()){
							if(currentBiocollectuidLst.contains(biocollectionUID)){
								createErroForDuplicateBiocollectionUid(csvReader,biocollectionUID);
							}
						}
						currentBiocollectuidLst.add(biocollectionUID);
						bioCollection = iLimsService.getBioCollectionForStudySubjectByUID(biocollectionUID, study, linkSubjectStudy);	
						if(bioCollection == null){
							insertRows.add(row);	
						}else{
							updateRows.add(row);
						}
					}else{
						createErroForEmptyBiocollectionUid(csvReader,biocollectionUID);
					}
				}
				int col = 0;
				String dateStr = new String();
				String cellValue = new String();
				if (csvReader.getIndex("COLLECTIONDATE") > 0) {
					col = csvReader.getIndex("COLLECTIONDATE");
					cellValue = csvReader.get("COLLECTIONDATE");
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
			recordCount++;
			row++;
			}//end of while means end of loop through records.
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
			log.error("processMatrixBiospecimenFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the Biospecimen data file");
		}
		finally {
			// Clean up the IO objects
			//timer.stop();
			//log.debug("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			//log.debug("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
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
		
		for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
			Integer i = (Integer) iterator.next();
			dataValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update");
		}
		
		return dataValidationMessages;
		}
	/**
	 * Create error for empty bio collection.
	 * @param csvReader
	 * @param biocollectionUID
	 * @throws IOException
	 */
	private void createErroForEmptyBiocollectionUid(CsvReader csvReader,
			String biocollectionUID) throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": BIOCOLLECTIONUID: ");
		errorString.append(biocollectionUID);
		errorString.append(" can not be empty when study biocollection auto generated function deactiveted.");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("BIOCOLLECTIONUID"), row));
	}
	/**
	 * Create error once duplicates.
	 * @param csvReader
	 * @param biocollectionUID
	 * @throws IOException
	 */
	private void createErroForDuplicateBiocollectionUid(CsvReader csvReader,String biocollectionUID) throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": BIOCOLLECTIONUID: ");
		errorString.append(biocollectionUID);
		errorString.append("This biocollection is already exsist in the list");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("BIOCOLLECTIONUID"), row));
	}
	/**
	 * Validate Biospecimen Inventory File Data.
	 * 
	 * @param fileInputStream
	 * @param inLength
	 * @param inFileFormat
	 * @param inDelimChr
	 * @return
	 * @throws FileFormatException
	 * @throws ArkSystemException
	 */
	private java.util.Collection<String> validateMatrixBiospecimenInventoryFileData(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException,
	ArkSystemException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		row = 1;
		
		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
		
			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}
			csvReader.readHeaders();
		
			srcLength = inLength - csvReader.getHeaders().toString().length();
			// Loop through all rows in file
			while (csvReader.readRecord()) {
				String biospecimenUID = csvReader.get("BIOSPECIMENUID");
				Biospecimen biospecimen = iLimsService.getBiospecimenByUid(biospecimenUID,study);
				
				if(biospecimen == null){
					errorBiospecimenUidNotExsist(csvReader, biospecimenUID);
				}else{
					BiospecimenLocationVO biospecimenLocationVO=iInventoryService.getBiospecimenLocation(biospecimen);
					if(biospecimenLocationVO.getIsAllocated()){
						errorBiospecimenUidAlredyAllocatedTo(csvReader,biospecimenUID, biospecimenLocationVO);
					}else{	
						String site = csvReader.get("SITE");
						String freezer = csvReader.get("FREEZER");
						String rack = csvReader.get("RACK");
						String box = csvReader.get("BOX");
						String rowString = csvReader.get("ROW");
						String columnString = csvReader.get("COLUMN");
						//if anything is empty
						if ((site != null && freezer != null && rack != null && 
										box != null && rowString != null && columnString != null) &&
									(!site.isEmpty() && !freezer.isEmpty() && !rack.isEmpty() &&
										!box.isEmpty() && !rowString.isEmpty() && !columnString.isEmpty())) {
									InvCell invCell = iInventoryService.getInvCellByLocationNames(site, freezer, rack, box, rowString, columnString);
									if (invCell == null) {
										errorInventoryLocationNotAvailable(csvReader, biospecimenUID);
									}else{
										if(invCell.getBiospecimen()==null){
											insertRows.add(row);
										}else{
											errorInventoryLocationAlreadyAllocated(csvReader, biospecimenUID,invCell);
										}
								}
						}else{
									//else you have incomplete info, and we aren't cool with that;
									errorInCompleteInventoryLocation(csvReader,biospecimenUID);
						}
						
					}
				}
			recordCount++;
			row++;
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
	}catch (IOException ioe) {
			log.error("processMatrixBiospecimenFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the Biospecimen data file");
	}finally {
			// Clean up the IO objects
			//timer.stop();
			//log.debug("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			//log.debug("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
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
	for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
		Integer i = (Integer) iterator.next();
		dataValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update");
	}
		
		return dataValidationMessages;
}
	/**
	 * Biospecimen Inventary validation  in complete Inventory location.
	 * @param csvReader
	 * @param biospecimenUID
	 * @throws IOException
	 */
	private void errorInCompleteInventoryLocation(CsvReader csvReader,String biospecimenUID) throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(" The location details provided for BiospecimenUID: ");
		errorString.append(biospecimenUID);
		errorString.append(" are incomplete. Please ensure you either provide all of the following information \"SITE, FREEZER, RACK, BOX, ROW and COLUMN\" or none of these ");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("SITE"), row));
		errorCells.add(new ArkGridCell(csvReader.getIndex("FREEZER"), row));
		errorCells.add(new ArkGridCell(csvReader.getIndex("RACK"), row));
		errorCells.add(new ArkGridCell(csvReader.getIndex("BOX"), row));
		errorCells.add(new ArkGridCell(csvReader.getIndex("ROW"), row));
		errorCells.add(new ArkGridCell(csvReader.getIndex("COLUMN"), row));
	}
	/**
	 * Biospecimen Inventary validation for inventory location not available to specimen..
	 * @param csvReader
	 * @param biospecimenUID
	 * @param invCell
	 * @throws IOException
	 */
	private void errorInventoryLocationAlreadyAllocated(CsvReader csvReader,
			String biospecimenUID, InvCell invCell) throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": biospecimen UID: ");
		errorString.append(biospecimenUID);
		errorString.append(" cannot be placed at this cell location as there is already something there (" +
		invCell.getBiospecimen().getBiospecimenUid() + ").  Please check this UID or location and try again.");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("BIOSPECIMENUID"), row));
		errorCells.add(new ArkGridCell(csvReader.getIndex("ROW"), row));
		errorCells.add(new ArkGridCell(csvReader.getIndex("COLUMN"), row));
	}
	/**
	 * Biospecimen Inventary validation inventory location not available.
	 * @param csvReader
	 * @param biospecimenUID
	 * @throws IOException
	 */
	private void errorInventoryLocationNotAvailable(CsvReader csvReader,
			String biospecimenUID) throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(".  The location details provided for BiospecimenUID: ");
		errorString.append(biospecimenUID);
		errorString.append(" do not match any location in the database. Please check and try again");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("SITE"), row));
		errorCells.add(new ArkGridCell(csvReader.getIndex("FREEZER"), row));
		errorCells.add(new ArkGridCell(csvReader.getIndex("RACK"), row));
		errorCells.add(new ArkGridCell(csvReader.getIndex("BOX"), row));
		errorCells.add(new ArkGridCell(csvReader.getIndex("ROW"), row));
		errorCells.add(new ArkGridCell(csvReader.getIndex("COLUMN"), row));
	}
	/**
	 * Biospecimen Inventary validation for UID already allocated..
	 * @param csvReader
	 * @param biospecimenUID
	 * @param biospecimenLocationVO
	 * @throws IOException
	 */
	private void errorBiospecimenUidAlredyAllocatedTo(CsvReader csvReader,
			String biospecimenUID, BiospecimenLocationVO biospecimenLocationVO)
			throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": bioscpeim UID: ");
		errorString.append(biospecimenUID);
		errorString.append(" This biospecimen has already been alocated to "+
		biospecimenLocationVO.getSiteName()+">"+biospecimenLocationVO.getFreezerName()+">"+biospecimenLocationVO.getRackName()+
		">"+biospecimenLocationVO.getBoxName()+" Cell("+biospecimenLocationVO.getRowLabel()+","+biospecimenLocationVO.getColLabel()+")");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("BIOSPECIMENUID"), row));
	}
	/**
	 * Biospecimen Inventary validation for UID not exsist.
	 * @param csvReader
	 * @param biospecimenUID
	 * @throws IOException
	 */
	private void errorBiospecimenUidNotExsist(CsvReader csvReader,
			String biospecimenUID) throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": bioscpeim UID: ");
		errorString.append(biospecimenUID);
		errorString.append(" does not exist in study '" + study.getName() + "   Please check this Study and the Subject UID and try again.");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("BIOSPECIMENUID"), row));
	}
	/**
	 * Validate bio specimen file data.
	 * 
	 * @param fileInputStream
	 * @param inLength
	 * @param inFileFormat
	 * @param inDelimChr
	 * @return
	 * @throws FileFormatException
	 * @throws ArkSystemException
	 */
	private java.util.Collection<String> validateMatrixBiospecimenFileData(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException,ArkSystemException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		List<String> currentBiospecimenuidLst=new ArrayList<String>();
		row = 1;
		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			//String[] stringLineArray;
			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}
			csvReader.readHeaders();
			srcLength = inLength - csvReader.getHeaders().toString().length();
			//String[] fieldNameArray = csvReader.getHeaders();
			
			while (csvReader.readRecord()) {
				// csvReader.getValues();
				String subjectUID = csvReader.get("SUBJECTUID");
				String biocollectionUID = csvReader.get("BIOCOLLECTIONUID");
				String biospecimenUID = csvReader.get("BIOSPECIMENUID");
				LinkSubjectStudy linkSubjectStudy = (iArkCommonService.getSubjectByUIDAndStudy(subjectUID, study));
				if(linkSubjectStudy==null){
					errorForSubjectUIDNotExsist(csvReader, subjectUID);
				}
				BioCollection biocollection =iLimsService.getBioCollectionForStudySubjectByUID(biocollectionUID,study,linkSubjectStudy);
				if(biocollection==null){
					errorForBiocollectionUID(csvReader,subjectUID, biocollectionUID,linkSubjectStudy);
				}
				Biospecimen biospecimen = iLimsService.getBiospecimenByUid(biospecimenUID,study);
				if(biospecimen!=null){
					errorBiospecimenAlreadyExsist(csvReader, subjectUID,biospecimenUID);
				}else{
					if(study.getAutoGenerateBiospecimenUid()){
						insertRows.add(row);
					}else{//not auto generated.
						if(biospecimenUID!=null && !biospecimenUID.trim().isEmpty()){
							if(!currentBiospecimenuidLst.isEmpty()){
								if(currentBiospecimenuidLst.contains(biospecimenUID)){
									errorBiospecimenUidAlreadyExsisOnList(csvReader, subjectUID, biospecimenUID);
								}
							}
							currentBiospecimenuidLst.add(biospecimenUID);
						}else{//empty biospecimen when autogenerated off
							errorForEmptyBiospecimenWhenAutoGeneratedOff(csvReader, subjectUID, biospecimenUID);
						}
					}
				}
				if (csvReader.getIndex("SAMPLETYPE") > 0) {
					String name = csvReader.get("SAMPLETYPE");
					BioSampletype sampleType = iLimsService.getBioSampleTypeByName(name);
					if (sampleType == null) {
						erroSampleTypeNotExsist(csvReader, subjectUID,biospecimenUID, name);
					}
				}
				if (csvReader.getIndex("QUANTITY") > 0) {
					String quantityString = csvReader.get("QUANTITY");
					if (quantityString.isEmpty()) {
						errorQuantityCanNotBeEmpty(csvReader, subjectUID,biospecimenUID);
					}
					else {
						try{
							Double.parseDouble(quantityString);
						}
						catch(NumberFormatException ne){
							errorQuantityInvalidNumber(csvReader, subjectUID,biospecimenUID, quantityString);
						}
					}
				}
				if (csvReader.getIndex("CONCENTRATION") > 0) {
					String concentrationString = csvReader.get("CONCENTRATION");
					if (concentrationString != null && !concentrationString.isEmpty()) {
						try{
							Double.parseDouble(concentrationString);
						}
						catch(NumberFormatException ne){
							errorInvalidConcentrationNumber(csvReader,subjectUID, biospecimenUID,concentrationString);
						}
					}
				}
				if (csvReader.getIndex("UNITS") > 0) {
					String name = csvReader.get("UNITS");
					Unit unit = iLimsService.getUnitByName(name);
					if (unit == null) {
						errorInValidUnitType(csvReader, subjectUID,biospecimenUID, name);
					}
				}
				if (csvReader.getIndex("TREATMENT") > 0) {
					String name = csvReader.get("TREATMENT");
					TreatmentType treatmentType = iLimsService.getTreatmentTypeByName(name);
					if (treatmentType == null) {
						errorInvalidTreatmentType(csvReader, subjectUID,biospecimenUID, name);
					}
				}
				String site = csvReader.get("SITE");
				String freezer = csvReader.get("FREEZER");
				String rack = csvReader.get("RACK");
				String box = csvReader.get("BOX");
				String rowString = csvReader.get("ROW");
				String columnString = csvReader.get("COLUMN");
				//if anything is empty
				if ((site != null && freezer != null && rack != null && 
								box != null && rowString != null && columnString != null) &&
							(!site.isEmpty() && !freezer.isEmpty() && !rack.isEmpty() &&
								!box.isEmpty() && !rowString.isEmpty() && !columnString.isEmpty())) {
							InvCell invCell = iInventoryService.getInvCellByLocationNames(site, freezer, rack, box, rowString, columnString);
							if (invCell == null) {
								errorInventoryLocationNotAvailable(csvReader, biospecimenUID);
							}else{
								if(invCell.getBiospecimen()==null){
									insertRows.add(row);
								}else{
									errorInventoryLocationAlreadyAllocated(csvReader, biospecimenUID,invCell);
								}
						}
				}else{
							//else you have incomplete info, and we aren't cool with that;
							errorInCompleteInventoryLocation(csvReader,biospecimenUID);
				}
			
				/*if(insertThisRow){
					int col = 0;
					String dateStr = new String();
	
					if (csvReader.getIndex("SAMPLEDATE") > 0 || csvReader.getIndex("SAMPLE_DATE") > 0) {
	
						if (csvReader.getIndex("SAMPLEDATE") > 0) {
							col = csvReader.getIndex("SAMPLEDATE");
						}
						else {
							col = csvReader.getIndex("SAMPLE_DATE");
						}
	
						try {
							dateStr = stringLineArray[col];
							if (dateStr != null && dateStr.length() > 0)
								simpleDateFormat.parse(dateStr);
						}
						catch (ParseException pex) {
							StringBuilder errorString = new StringBuilder();
							errorString.append("Error: Row ");
							errorString.append(row);
							errorString.append(": SubjectUID: ");
							errorString.append(subjectUID);
							errorString.append(" ");
							errorString.append(fieldNameArray[col]);
							errorString.append(": ");
							errorString.append(stringLineArray[col]);
							errorString.append(" is not in the valid date format of: ");
							errorString.append(Constants.DD_MM_YYYY.toLowerCase());
	
							dataValidationMessages.add(errorString.toString());
							errorCells.add(new ArkGridCell(col, row));
							
						}
					}
				}*/
				recordCount++;
				row++;
				
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
			log.error("processMatrixBiospecimenFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the Biospecimen data file");
		}
		finally {
			// Clean up the IO objects
			//timer.stop();
			//log.debug("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			//log.debug("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
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

		for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
			Integer i = (Integer) iterator.next();
			dataValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update");
		}

		return dataValidationMessages;
	}

	private void errorInvalidTreatmentType(CsvReader csvReader,
			String subjectUID, String biospecimenUID, String name)
			throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": SubjectUID: ");
		errorString.append(subjectUID);
		errorString.append(" The treatment ");
		errorString.append(name);
		errorString.append(" of BiospecimenUID: ");
		errorString.append(biospecimenUID);
		errorString.append(" do not match the details in the database. Please check and try again");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("TREATMENT"), row));
	}

	private void errorInValidUnitType(CsvReader csvReader, String subjectUID,
			String biospecimenUID, String name) throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": SubjectUID: ");
		errorString.append(subjectUID);
		errorString.append(" The unit ");
		errorString.append(name);
		errorString.append(" of BiospecimenUID: ");
		errorString.append(biospecimenUID);
		errorString.append(" do not match the details in the database. Please check and try again");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("UNITS"), row));
	}

	private void errorInvalidConcentrationNumber(CsvReader csvReader,
			String subjectUID, String biospecimenUID, String concentrationString)
			throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": SubjectUID: ");
		errorString.append(subjectUID);
		errorString.append(" The concentration ");
		errorString.append(concentrationString);
		errorString.append(" of BiospecimenUID: ");
		errorString.append(biospecimenUID);
		errorString.append(" is not a valid number.");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("CONCENTRATION"), row));
	}

	private void errorQuantityInvalidNumber(CsvReader csvReader,
			String subjectUID, String biospecimenUID, String quantityString)
			throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": SubjectUID: ");
		errorString.append(subjectUID);
		errorString.append(" The quantity ");
		errorString.append(quantityString);
		errorString.append(" of BiospecimenUID: ");
		errorString.append(biospecimenUID);
		errorString.append(" is not a valid number.");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("QUANTITY"), row));
	}

	private void errorQuantityCanNotBeEmpty(CsvReader csvReader,
			String subjectUID, String biospecimenUID) throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": SubjectUID: ");
		errorString.append(subjectUID);
		errorString.append(" The quantity ");
		errorString.append(" of BiospecimenUID: ");
		errorString.append(biospecimenUID);
		errorString.append(" is required.");
		errorString.append(" Please enter a valid number.");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("QUANTITY"), row));
	}

	private void erroSampleTypeNotExsist(CsvReader csvReader,String subjectUID, String biospecimenUID, String name)
			throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": SubjectUID: ");
		errorString.append(subjectUID);
		errorString.append(" The sample type ");
		errorString.append(name);
		errorString.append(" of BiospecimenUID: ");
		errorString.append(biospecimenUID);
		errorString.append(" do not match the details in the database. Please check and try again");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("SAMPLETYPE"), row));
	}

	private void errorForEmptyBiospecimenWhenAutoGeneratedOff(CsvReader csvReader,String subjectUID, String biospecimenUID) throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": SubjectUID: ");
		errorString.append(subjectUID);
		errorString.append(": BIOSPECIMENUID: ");
		errorString.append(biospecimenUID);
		errorString.append("The biospecimen uid can not be empty when auto generated biospecimen was off. ");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("BIOSPECIMENUID"), row));
	}

	private void errorBiospecimenUidAlreadyExsisOnList(CsvReader csvReader,String subjectUID,String biospecimenUID) throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": SubjectUID: ");
		errorString.append(subjectUID);
		errorString.append(": BIOSPECIMENUID: ");
		errorString.append(biospecimenUID);
		errorString.append("This biospecimen uid is already exsist in the list");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("BIOSPECIMENUID"), row));
	}
	/**
	 * 
	 * 
	 * @param csvReader
	 * @param biospecimenUID
	 * @throws IOException
	 */
	private void errorBiospecimenAlreadyExsist(CsvReader csvReader,String subjectUID,String biospecimenUID) throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": SubjectUID: ");
		errorString.append(subjectUID);
		errorString.append(": BiospecimenUID: ");
		errorString.append(biospecimenUID);
		errorString.append(". must be unique for study "+study.getName()+".Please check the file and try again.");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("BIOSPECIMENUID"), row));
	}

	/**
	 *For study and subject biocollection uid does not exsist.
	 *  
	 * @param csvReader
	 * @param biocollectionUID
	 * @param linkSubjectStudy
	 * @throws IOException
	 */
	private void errorForBiocollectionUID(CsvReader csvReader,String subjectUID,String biocollectionUID, LinkSubjectStudy linkSubjectStudy)throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": SubjectUID: ");
		errorString.append(subjectUID);
		errorString.append(": BioCollectionUID: ");
		errorString.append(biocollectionUID);
		errorString.append(" does not exist for this study "+study.getName()+" of subjectUID "+ subjectUID+" Please check this Biocollection UID and try again.");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("BIOCOLLECTIONUID"), row));
	}
	/**
	 * For study the subject uid not exsist.
	 * 
	 * @param csvReader
	 * @param subjectUID
	 * @throws IOException
	 */
	private void errorForSubjectUIDNotExsist(CsvReader csvReader,String subjectUID) throws IOException {
		StringBuilder errorString = new StringBuilder();
		errorString.append("Error: Row ");
		errorString.append(row);
		errorString.append(": SubjectUID: ");
		errorString.append(subjectUID);
		errorString.append(" does not exist.  Please check this Subject UID for this"+study.getName()+" and try again.");
		dataValidationMessages.add(errorString.toString());
		errorCells.add(new ArkGridCell(csvReader.getIndex("SUBJECTUID"), row));
	}
	
		
	}
	

