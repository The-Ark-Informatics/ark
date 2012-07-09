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
import java.text.DecimalFormat;
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

import org.apache.commons.lang.time.StopWatch;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
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
	private long						curPos;
	private long						srcLength					= -1;
	private StopWatch					timer							= null;
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
					inputStream = convertXlsToCsv(w);
					inputStream.reset();
					delimiterCharacter = ',';
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
		curPos = 0;
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

			timer = new StopWatch();
			timer.start();

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
			timer.stop();

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
	 * Validates the file in the default "matrix" file format assumed: BiospecimenUID,FIELD1,FIELD2,FIELDN...
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
	 * @return a collection of data validation messages
	 */
	public java.util.Collection<String> validateMatrixBiospecimenFileData(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException,
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
			// BiospecimenUID F1 F2 FN
			// 0 1 2 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();

			String[] fieldNameArray = csvReader.getHeaders();

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();

				// First/0th column should be the SubjectUID
				String subjectUID = stringLineArray[0];

				// 1th columsn should be BiospecimenUid
				String biospecimenUID = stringLineArray[1];
				String biocollectionUID = stringLineArray[2];

				// If no SubjectUID/BiospecimenUid found, caught by exception catch
				try {
					LinkSubjectStudy linkSubjectStudy = (iArkCommonService.getSubjectByUID(subjectUID, study));
					linkSubjectStudy.setStudy(study);

					BioCollection biocollection = iLimsService.getBioCollectionByName(biocollectionUID);//TODO this really should be study specific?
					Biospecimen biospecimen = iLimsService.getBiospecimenByUid(biospecimenUID);//TODO this really should be study specific?

					if (biocollection == null) {
						//blow up fail
						StringBuilder errorString = new StringBuilder();
						errorString.append("Error: Row ");
						errorString.append(row);
						errorString.append(": SubjectUID: ");
						errorString.append(subjectUID);
						errorString.append(" The details/name of the BIOCOLLECTION ");
						errorString.append(biocollectionUID);
						errorString.append(" does not exist in the database. Please check and try again");
						dataValidationMessages.add(errorString.toString());
						errorCells.add(new ArkGridCell(csvReader.getIndex("BIOCOLLECTION"), row));
					}//otherwise go on like we always do
					else{
						
						if (biospecimen == null) {
							// Biospecimen not found, thus a new Biospecimen to be inserted
							insertRows.add(row);
						}
						else {
							updateRows.add(row);
						}
	
						if (csvReader.get("SITE") != null && csvReader.get("FREEZER") != null && csvReader.get("RACK") != null && csvReader.get("BOX") != null && csvReader.get("ROW") != null
								&& csvReader.get("COLUMN") != null) {
							InvCell invCell = iInventoryService.getInvCellByLocationNames(csvReader.get("SITE"), csvReader.get("FREEZER"), csvReader.get("RACK"), csvReader.get("BOX"), csvReader.get("ROW"),
									csvReader.get("COLUMN"));
							if (invCell == null) {
								StringBuilder errorString = new StringBuilder();
								errorString.append("Error: Row ");
								errorString.append(row);
								errorString.append(": SubjectUID: ");
								errorString.append(subjectUID);
								errorString.append(" The location details of BiospecimenUID: ");
								errorString.append(biospecimenUID);
								errorString.append(" do not match the details in the database. Please check and try again");
								dataValidationMessages.add(errorString.toString());
								errorCells.add(new ArkGridCell(csvReader.getIndex("SITE"), row));
								errorCells.add(new ArkGridCell(csvReader.getIndex("FREEZER"), row));
								errorCells.add(new ArkGridCell(csvReader.getIndex("RACK"), row));
								errorCells.add(new ArkGridCell(csvReader.getIndex("BOX"), row));
								errorCells.add(new ArkGridCell(csvReader.getIndex("ROW"), row));
								errorCells.add(new ArkGridCell(csvReader.getIndex("COLUMN"), row));
							}
						}
					}
				}
				catch (EntityNotFoundException enf) {
					// SubjectUID not found, thus a new Biospecimen to be inserted
					insertRows.add(row);
				}

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

				log.debug("\n");
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
		catch (Exception ex) {
			log.error("processMatrixBiospecimenFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process Biospecimen data file");
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
