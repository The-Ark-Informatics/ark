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

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.time.StopWatch;
import org.apache.shiro.SecurityUtils;
import org.apache.shiro.subject.Subject;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.UploadVO;
import au.org.theark.core.web.component.worksheet.ArkGridCell;

import com.csvreader.CsvReader;

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
	private long						subjectCount;
	private long						curPos;
	private long						srcLength					= -1;
	private StopWatch					timer							= null;
	private char						delimiterCharacter		= au.org.theark.core.Constants.DEFAULT_DELIMITER_CHARACTER;
	private String						fileFormat					= au.org.theark.core.Constants.DEFAULT_FILE_FORMAT;
	private SimpleDateFormat		simpleDateFormat			= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
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
			validationMessages = validateSubjectFileFormat(inputStream, fileFormat, delimiterCharacter);
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
	public Collection<String> validateSubjectFileFormat(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;

		try {
			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS")) {
				Workbook w;
				try {
					w = Workbook.getWorkbook(inputStream);
					inputStream = convertXlsToCsv(w);
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
			validationMessages = validateSubjectMatrixFileFormat(inputStream, inputStream.toString().length(), fileFormat, delimChar);
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
	 * Validates the file in the default "matrix" file data assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN... Where N is any number of columns
	 * 
	 * @param uploadVo
	 *           is the UploadVO of the file
	 * @return a collection of validation messages
	 */
	public Collection<String> validateSubjectFileData(UploadVO uploadVo) {
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
					inputStream = convertXlsToCsv(w);
					inputStream.reset();
					delimiterCharacter = ',';
				}
				catch (BiffException e) {
					log.error(e.getMessage());
				}
			}

			validationMessages = validateSubjectFileData(inputStream, fileFormat, delimiterCharacter);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return validationMessages;
	}

	public Collection<String> validateSubjectFileData(InputStream inputStream, String fileFormat, char delimChar) {
		java.util.Collection<String> validationMessages = null;

		try {
			// TODO Travis: If can't be sped up adequately, change this to an initial Validation, then can choose to kick off batch
			//performance has improved from timeout after 50 to validation of ~6K per minute...still this is a user interface element...so will try to speed up 
			// which should save user from having to send this to batch, only to find out there were error discovered in first few rows
			// validationMessages = validateMatrixSubjectFileData(inputStream, inputStream.toString().length(), fileFormat, delimChar);
			validationMessages = validateMatrixSubjectFileData(inputStream, inputStream.toString().length(), fileFormat, delimChar, Long.MAX_VALUE);
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
		curPos = 0;
		row = 0;

		//TODO ASAP TRAV ... make one query instead of something times n like it currently is  ... need a list more than a count really...but the count will still be useful for uid locking
		//iStudyService.countNumberOfUniqueSubjects(study, uploader.getListOfUidsFromInputStream(fileIS, file.length(), fileFormat, delimiter));

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);

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
			//log.debug("Header length: " + csvReader.getHeaders().toString().length());
			String[] headerColumnArray = csvReader.getHeaders();

			Collection<String> subjectColumns = new ArrayList<String>();
			String[] subjectHeaderColumnArray = au.org.theark.study.web.Constants.SUBJECT_TEMPLATE_HEADER;
			boolean headerError = false;
			for (int i = 0; i < subjectHeaderColumnArray.length; i++) {
				String colName = subjectHeaderColumnArray[i];
				subjectColumns.add(colName);
			}

			for (int i = 0; i < headerColumnArray.length; i++) {
				String colName = headerColumnArray[i];
				if (!subjectColumns.contains(colName)) {
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
				stringBuffer.append(".\n");
				stringBuffer.append("The default format should be as follows:\n");
				// SUBJECTUID TITLE FIRST_NAME MIDDLE_NAME LAST_NAME PREFERRED_NAME DATE_OF_BIRTH VITAL_STATUS GENDER STATUS DATE_OF_DEATH CAUSE_OF_DEATH
				// MARITAL_STATUS PREFERRED_CONTACT EMAIL

				// Column headers
				stringBuffer.append(Constants.SUBJECTUID);
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("TITLE");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("FIRST_NAME");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("MIDDLE_NAME");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("LAST_NAME");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("PREFERRED_NAME");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("DATE_OF_BIRTH");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("VITAL_STATUS");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("GENDER");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("STATUS");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("DATE_OF_DEATH");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("CAUSE_OF_DEATH");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("MARITAL_STATUS");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("PREFERRED_CONTACT");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("EMAIL\n");

				// Column values
				stringBuffer.append("[ABC000001]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[Mr]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[JOSEPH]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[BLOGGS]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[JOEY]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[19/02/1976]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[Alive]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[Male]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[Active]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[Single]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[Phone]");
				stringBuffer.append(delimiterCharacter);
				stringBuffer.append("[joebloggs@somewhere.com]\n");
				stringBuffer.append("\n\nNOTE: Enclosing quotes are optional");

				fileValidationMessages.add(stringBuffer.toString());

				for (int i = 0; i < headerColumnArray.length; i++) {
					if (!subjectColumns.contains(headerColumnArray[i])) {
						fileValidationMessages.add("Error: the column name " + headerColumnArray[i] + " is not a valid column name.");
					}
				}
			}

			row = 1;

			// Loop through all rows in file
			/*
			 * while (csvReader.readRecord()) { // do something with the newline to put the data into // the variables defined above stringLineArray =
			 * csvReader.getValues();
			 * 
			 * // Check each line has same number of columns as header if (stringLineArray.length < headerColumnArray.length) {
			 * fileValidationMessages.add("Error: the row " + row + " has missing cells, or missing the required number of delimiters."); }
			 * 
			 * row++; subjectCount++; }
			 * 
			 * if (fileValidationMessages.size() > 0) { for (Iterator<String> iterator = fileValidationMessages.iterator(); iterator.hasNext();) { String
			 * errorMessage = iterator.next(); log.debug(errorMessage); } } else { log.debug("Validation is ok"); }
			 */
		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file");
		}
		finally {
			// TODO can the timer go sometime soon

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
					//TODO ASAP : reinstate below
					//inputStreamReader.close();
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
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN...
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
	public java.util.Collection<String> validateMatrixSubjectFileData(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr, long rowsToValidate) throws FileFormatException,
			ArkSystemException {
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		curPos = 0;
		row = 1;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 1th column to Nth column)
			// SUBJECTUID F1 F2 FN
			// 0 1 2 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();

			String[] fieldNameArray = csvReader.getHeaders();

			List<String> subjectUIDsAlreadyExisting = iArkCommonService.getAllSubjectUIDs(study);

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();

				// First/0th column should be the SubjectUID
				String subjectUID = stringLineArray[0];
				//subjectUIDsInCSV.add(subjectUID);

/*				if(iArkCommonService.getSubjectByUIDAndStudy(subjectUID, study) != null){
					updateRows.add(row);
				}
				else{
					insertRows.add(row);
				}*/
				for(String subjectFromDB : subjectUIDsAlreadyExisting){
					if(subjectFromDB.equals(subjectUID)){
						updateRows.add(row);
					}
					else{
						insertRows.add(row);
					}
				}

				int col = 0;
				String dateStr = new String();

				if (csvReader.getIndex("DATE_OF_BIRTH") > 0 || csvReader.getIndex("DOB") > 0) {

					if (csvReader.getIndex("DATE_OF_BIRTH") > 0) {
						col = csvReader.getIndex("DATE_OF_BIRTH");
					}
					else {
						col = csvReader.getIndex("DOB");
					}

					try {
						dateStr = stringLineArray[col];
						if (dateStr != null && dateStr.length() > 0)
							simpleDateFormat.parse(dateStr);
					}
					catch (ParseException pex) {
						dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + stringLineArray[col] + " is not in the valid date format of: "
								+ Constants.DD_MM_YYYY.toLowerCase());
						errorCells.add(new ArkGridCell(col, row));
					}
				}

				if (csvReader.getIndex("DATE_OF_DEATH") > 0 || csvReader.getIndex("DODEATH") > 0) {

					if (csvReader.getIndex("DATE_OF_DEATH") > 0) {
						col = csvReader.getIndex("DATE_OF_DEATH");
					}
					else {
						col = csvReader.getIndex("DODEATH");
					}
					try {
						dateStr = stringLineArray[col];
						if (dateStr != null && dateStr.length() > 0)
							simpleDateFormat.parse(dateStr);
					}
					catch (ParseException pex) {
						dataValidationMessages.add("Error: Row " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + stringLineArray[col] + " is not in the valid date format of: "
								+ Constants.DD_MM_YYYY.toLowerCase());
						errorCells.add(new ArkGridCell(col, row));
					}
				}

				//log.debug("\n");
				subjectCount++;
				row++;
			}


			if (dataValidationMessages.size() > 0) {
				log.warn("Validation messages: " + dataValidationMessages.size());
				for (Iterator<String> iterator = dataValidationMessages.iterator(); iterator.hasNext();) {
					String errorMessage = iterator.next();
					log.warn(errorMessage);
				}
			}
			else {
				//log.debug("Validation is ok");
			}
		}
		catch (IOException ioe) {
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex) {
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file");
		}
		finally {
			// Clean up the IO objects
			timer.stop();
			log.warn("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			log.warn("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
			log.warn("updates = " + updateRows.size() + "     inserts = " + insertRows.size());
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

		for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
			Integer i = (Integer) iterator.next();
			dataValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update");
		}

		return dataValidationMessages;
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
						osw.write(delimiterCharacter);
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
}
