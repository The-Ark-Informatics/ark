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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.worksheet.ArkGridCell;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;

import com.csvreader.CsvReader;

/**
 * BiospecimenUploadValidator provides support for validating Biospecimen matrix-formatted files.
 * 
 * @author cellis
 */
public class BiospecimenUploadValidator {
	private static Logger			log							= LoggerFactory.getLogger(BiospecimenUploadValidator.class);
	@SuppressWarnings("unchecked")
	private IArkCommonService		iArkCommonService;
	private ILimsService				iLimsService;
	private IInventoryService		iInventoryService;
	private Long						studyId;
	private Study						study;
	java.util.Collection<String>	fileValidationMessages	= new java.util.ArrayList<String>();
	java.util.Collection<String>	dataValidationMessages	= new java.util.ArrayList<String>();
	private HashSet<Integer>		insertRows;
	private HashSet<Integer>		updateRows;
	private HashSet<ArkGridCell>	errorCells;
	private long						recordCount;
	private long						srcLength					= -1;
//	private StopWatch					timer							= null;
	private char						delimiterCharacter		= au.org.theark.core.Constants.DEFAULT_DELIMITER_CHARACTER;
	private String						fileFormat					= au.org.theark.core.Constants.DEFAULT_FILE_FORMAT;
	private SimpleDateFormat		simpleDateFormat			= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private int							row							= 1;

	@SuppressWarnings("unchecked")
	public BiospecimenUploadValidator(Study study, IArkCommonService iArkCommonService, ILimsService iLimsService, IInventoryService iInventoryService) {
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
			validationMessages = validateBiospecimenFileFormat(inputStream, fileFormat, delimiterCharacter);
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
	public Collection<String> validateBiospecimenFileFormat(InputStream inputStream, String fileFormat, char delimChar) {
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
			validationMessages = validateBiospecimenMatrixFileFormat(inputStream, inputStream.toString().length(), fileFormat, delimChar);
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

			validationMessages = validateBiospecimenFileData(inputStream, fileFormat, delimiterCharacter);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}

	public Collection<String> validateBiospecimenFileData(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;

		try {
			validationMessages = validateMatrixBiospecimenFileData(inputStream, inputStream.toString().length(), fileFormat, delimChar);
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
	 * Validates the Biospecimen file in the default "matrix" file format assumed
	 * 
	 * @param fileInputStream
	 * @param inLength
	 * @param inFileFormat
	 * @param inDelimChr
	 * @return
	 * @throws FileFormatException
	 * @throws ArkBaseException
	 */
	public java.util.Collection<String> validateBiospecimenMatrixFileFormat(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException,
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
			// BiospecimenUID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());
			String[] headerColumnArray = csvReader.getHeaders();

			Collection<String> biospecimenColumns = new ArrayList<String>();
			String[] biospecimenHeaderColumnArray = Constants.BIOSPECIMEN_TEMPLATE_HEADER;
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
				for (String column : Constants.BIOSPECIMEN_TEMPLATE_HEADER) {
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
			log.error("processMatrixBiospecimenFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the Biospecimen data file");
		}
		catch (Exception ex) {
			log.error("processMatrixBiospecimenFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process Biospecimen data file");
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
	 * Validates the file in the default "matrix" file format assumed: BiospecimenUID,FIELD1,FIELD2,FIELDN...
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

	 * 
	 * 
	 * 
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of a file
	 * @throws FileFormatException
	 *            file format Exception
	 * @throws ArkBaseException
	 *            general ARK Exception
	 * @return a collection of data validation messages
	 */
	public java.util.Collection<String> validateMatrixBiospecimenFileData(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException,
			ArkSystemException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;

		row = 1;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}


			// Set field list (note 1th column to Nth column)
			// BiospecimenUID F1 F2 FN
			// 0 1 2 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();

			String[] fieldNameArray = csvReader.getHeaders();

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				boolean insertThisRow = true;
				stringLineArray = csvReader.getValues();
				// First/0th column should be the SubjectUID
				String subjectUID = stringLineArray[0];
				// next should be BiospecimenUid -- TODO:  These assumptions is not tested earlier!!!;
				String biospecimenUID = stringLineArray[1]; // io think something like this might be betteR?  csvReader.getIndex("BIOSPECIMENUID")
				String biocollectionUID = stringLineArray[2];

			
				LinkSubjectStudy linkSubjectStudy = (iArkCommonService.getSubjectByUIDAndStudy(subjectUID, study));
				if(linkSubjectStudy==null){
					StringBuilder errorString = new StringBuilder();
					errorString.append("Error: Row ");
					errorString.append(row);
					errorString.append(": SubjectUID: ");
					errorString.append(subjectUID);
					errorString.append(" does not exist.  Please check this Subject UID and try again.");
					dataValidationMessages.add(errorString.toString());
					errorCells.add(new ArkGridCell(csvReader.getIndex("BIOSPECIMENUID"), row));
					insertThisRow = false;//drop out also?
					recordCount++;
					row++;
					break;
				}
				
				if(linkSubjectStudy.getStudy()==null){
					linkSubjectStudy.setStudy(study);
				}

				BioCollection biocollection = null; //iLimsService.getBioCollectionByName(biocollectionUID,study.getId());
				Biospecimen biospecimen = null;//iLimsService.getBiospecimenByUid(biospecimenUID,study);
				
				/*TODO ASAP 
				 * 
				 * once logic laid out
				 * ...make sure you just break out of while on the first error 
				 * ...change insertRow stuff to say rowIsOk = true/false
				 * ....once we pass all tests THEN if(rowIsOK) THEN insertRows.add(row)
				 * */	
				
				if (biospecimenUID == null || biospecimenUID.isEmpty() ) {
					if(study.getAutoGenerateBiospecimenUid()){
						//insertRows.add(row);								
					}
					else{
						StringBuilder errorString = new StringBuilder();
						errorString.append("Error: Row ");
						errorString.append(row);
						errorString.append(": SubjectUID: ");
						errorString.append(subjectUID);
						errorString.append(".  You have not specified a biospecimen UID, yet your study is not set up" +
								" to auto generate biospecimen UIDs.  Please specify a unique ID.");
						dataValidationMessages.add(errorString.toString());
						errorCells.add(new ArkGridCell(csvReader.getIndex("BIOSPECIMENUID"), row));
						insertThisRow = false;//drop out also?
					}
				}
				else {
					if(study.getAutoGenerateBiospecimenUid()){
						StringBuilder errorString = new StringBuilder();
						errorString.append("Error: Row ");
						errorString.append(row);
						errorString.append(": SubjectUID: ");
						errorString.append(subjectUID);
						errorString.append(": BIOSPECIMENUID: ");
						errorString.append(biospecimenUID);
						errorString.append(".  You have specified a biospecimen UID, yet your study is set up" +
								" to auto generate biospecimen UIDs.");
						dataValidationMessages.add(errorString.toString());
						errorCells.add(new ArkGridCell(csvReader.getIndex("BIOSPECIMENUID"), row));
						insertThisRow = false;//drop out also?
					}
					else{
						biospecimen = iLimsService.getBiospecimenByUid(biospecimenUID,study);
						if(biospecimen == null){
							insertRows.add(row);								
						}
						else{
							StringBuilder errorString = new StringBuilder();
							errorString.append("Error: Row ");
							errorString.append(row);
							errorString.append(": SubjectUID: ");
							errorString.append(subjectUID);
							errorString.append(": BIOSPECIMENUID: ");
							errorString.append(biospecimenUID);
							errorString.append(".  You have specified an existing biospecimen UID.  This uploader created new " +
									"biospecimens, but does not update existing biospecimens.  Please remove this row");
							dataValidationMessages.add(errorString.toString());
							errorCells.add(new ArkGridCell(csvReader.getIndex("BIOSPECIMENUID"), row));
							insertThisRow = false;//drop out also?
						}
					}
				}
				
				
				if (biocollectionUID == null  || biocollectionUID.isEmpty() ) {
					if(study.getAutoGenerateBiocollectionUid()){
						//insertRows.add(row);								
					}
					else{
						StringBuilder errorString = new StringBuilder();
						errorString.append("Error: Row ");
						errorString.append(row);
						errorString.append(": SubjectUID: ");
						errorString.append(subjectUID);
						errorString.append(".  You have not specified a biocollection UID, yet your study is not set up" +
								" to auto generate biocollection UIDs.  Please specify a unique ID.");
						dataValidationMessages.add(errorString.toString());
						errorCells.add(new ArkGridCell(csvReader.getIndex("BIOCOLLECTIONUID"), row));
						insertThisRow = false;//drop out also?
					}
				}
				else {
					biocollection = iLimsService.getBioCollectionByUID(biocollectionUID,study.getId());
					if(study.getAutoGenerateBiocollectionUid()){//ie; auto gen, id supplied.
						if(biocollection==null){
							StringBuilder errorString = new StringBuilder();
							errorString.append("Error: Row ");
							errorString.append(row);
							errorString.append(": SubjectUID: ");
							errorString.append(subjectUID);
							errorString.append(": BIOCOLLECTIONUID: ");
							errorString.append(biocollectionUID);
							errorString.append(".  You have specified a non-existant biocollection UID, yet your study is set up" +
									" to auto generate biocollection UIDs.  Check the biocollectionUID if you intended to relate it " +
									"to a collection, otherwise remove the biocollectionid if you wish to generate a new biocollection");
							dataValidationMessages.add(errorString.toString());
							errorCells.add(new ArkGridCell(csvReader.getIndex("BIOCOLLECTIONUID"), row));
							insertThisRow = false;//drop out also?
						}
						else{
							//insertRows.add(row);
						}
					}
					else{//ie; not auto gen, id supplied.
						
						if(biocollection == null){
							//insertRows.add(row);	//this instance will need biocol created							
						}
						else{
							//insertRows.add(row);  //this istance will use the provided biocol.
						}
					}
				}
				
				if (csvReader.getIndex("SAMPLETYPE") > 0) {
					String name = csvReader.get("SAMPLETYPE");
					BioSampletype sampleType = new BioSampletype();
					sampleType = iLimsService.getBioSampleTypeByName(name);
					if (sampleType == null) {
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
						insertThisRow = false;//drop out also?
					}
				}
				

				
				/***
				 * TODO ASAP : ADD CONCENTRATION HERE
				 */
				

				
				if (csvReader.getIndex("CONCENTRATION") > 0) {
					String concentrationString = csvReader.get("CONCENTRATION");
					if (concentrationString != null && !concentrationString.isEmpty()) {
						try{
							Double.parseDouble(concentrationString);
						}
						catch(NumberFormatException ne){

							StringBuilder errorString = new StringBuilder();
							errorString.append("Error: Row ");
							errorString.append(row);
							errorString.append(": SubjectUID: ");
							errorString.append(subjectUID);
							errorString.append(" The concentration ");
							errorString.append(concentrationString);
							errorString.append(" of BiospecimenUID: ");
							errorString.append(biospecimenUID);
							errorString.append(" is not a valid number");
							dataValidationMessages.add(errorString.toString());
							errorCells.add(new ArkGridCell(csvReader.getIndex("CONCENTRATION"), row));
							insertThisRow = false;//drop out also?
						}
						
					}
				}

				
				if (csvReader.getIndex("UNITS") > 0) {
					String name = csvReader.get("UNITS");
					Unit unit = iLimsService.getUnitByName(name);
					if (unit == null) {
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
						insertThisRow = false;//drop out also?
					}
				}
				
				if (csvReader.getIndex("TREATMENT") > 0) {
					String name = csvReader.get("TREATMENT");
					TreatmentType treatmentType = new TreatmentType(); 
					treatmentType = iLimsService.getTreatmentTypeByName(name);
					if (treatmentType == null) {
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
						insertThisRow = false;//drop out also?
					}
				}

				String site = csvReader.get("SITE");
				String freezer = csvReader.get("FREEZER");
				String rack = csvReader.get("RACK");
				String box = csvReader.get("BOX");
				String rowString = csvReader.get("ROW");
				String columnString = csvReader.get("COLUMN");
				
				if ((site == null && freezer == null && rack == null && box == null && rowString == null && columnString == null) ||
					(site.isEmpty() && freezer.isEmpty() && rack.isEmpty() && box.isEmpty() && rowString.isEmpty() && columnString.isEmpty())	
					){
						//ie; EVERYTHING IS EMPTY...in which case, we let you go ahead and don't specify where the biospec lives
						log.debug("EVERYTHING is empty so we still create it...we just don't put it somewhere");
				}
				else if ((site != null && freezer != null && rack != null && 
						box != null && rowString != null && columnString != null) &&
					(!site.isEmpty() && !freezer.isEmpty() && !rack.isEmpty() &&
						!box.isEmpty() && !rowString.isEmpty() && !columnString.isEmpty())	
				) {
					InvCell invCell = iInventoryService.getInvCellByLocationNames(site, freezer, rack, box, rowString, columnString);
					if (invCell == null) {
						StringBuilder errorString = new StringBuilder();
						errorString.append("Error: Row ");
						errorString.append(row);
						errorString.append(": SubjectUID: ");
						errorString.append(subjectUID);
						errorString.append(" The location details provided for BiospecimenUID: ");
						errorString.append(biospecimenUID);
						errorString.append(" do not match the details in the database. Please check and try again");
						dataValidationMessages.add(errorString.toString());
						errorCells.add(new ArkGridCell(csvReader.getIndex("SITE"), row));
						errorCells.add(new ArkGridCell(csvReader.getIndex("FREEZER"), row));
						errorCells.add(new ArkGridCell(csvReader.getIndex("RACK"), row));
						errorCells.add(new ArkGridCell(csvReader.getIndex("BOX"), row));
						errorCells.add(new ArkGridCell(csvReader.getIndex("ROW"), row));
						errorCells.add(new ArkGridCell(csvReader.getIndex("COLUMN"), row));
						insertThisRow = false;//drop out also?
					}
				}
				else{
					//else you have incomplete info, and we aren't cool with that;
					StringBuilder errorString = new StringBuilder();
					errorString.append("Error: Row ");
					errorString.append(row);
					errorString.append(": SubjectUID: ");
					errorString.append(subjectUID);
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
					insertThisRow = false;//drop out also?
					
				}
			
				if(insertThisRow){
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
							insertThisRow = false;//drop out also?
						}
					}
				}

				//log.debug("\n");
				recordCount++;
				row++;
				if(insertThisRow){
					insertRows.add(row);
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
}
