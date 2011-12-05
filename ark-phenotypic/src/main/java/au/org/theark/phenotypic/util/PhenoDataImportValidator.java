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

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.exception.PhenotypicSystemException;
import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.model.pheno.entity.PhenoData;
import au.org.theark.core.model.pheno.entity.PhenotypicCollection;
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
	private char							phenotypicDelimChr		= Constants.IMPORT_DELIM_CHAR_COMMA;						// default phenotypic file
																																						// delimiter: COMMA
	java.util.Collection<String>		fileValidationMessages	= new ArrayList<String>();
	java.util.Collection<String>		dataValidationMessages	= new ArrayList<String>();

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

	/**
	 * PhenotypicValidator constructor
	 * 
	 */

	public PhenoDataImportValidator(IArkCommonService<Void> iArkCommonService, IPhenotypicService iPhenotypicService, 
												PhenoFieldDataUploadVO phenoFieldDataUploadVo) {
		this.iArkCommonService = iArkCommonService;
		this.iPhenotypicService = iPhenotypicService;
		this.arkFunction = phenoFieldDataUploadVo.getUpload().getArkFunction();
		this.questionnaire = phenoFieldDataUploadVo.getQuestionnaire();

		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if (studyId != null) {
			Study study = iArkCommonService.getStudy(studyId);
			this.study = study;
		}
		this.fileValidationMessages = new ArrayList<String>();
		this.dataValidationMessages = new ArrayList<String>();

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
			validationMessages = validateMatrixPhenoFileData(inputStream, inputStream.toString().length());
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
	 * Validates the phenotypic data file in the default "matrix" file format assumed: SUBJECTUID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN...
	 * Assumes that if either the DATE_COLLECTED or SUBJECTUID changes, that a new PhenotypicCollection (questionnaire instance) is created.
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
	public java.util.Collection<String> validateMatrixPhenoFileData(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException {
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		/*
		 * FieldData table requires: COLLECTION_ID PERSON_ID DATE_COLLECTED FIELD_ID USER_ID INSERT_TIME
		 */

		Date dateCollected = new Date();
		Field field = null;

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

			if (!StringUtils.equalsIgnoreCase(fieldNameArray[0], "SUBJECTUID")) {
				headerError = true;
			}

			if (headerError) {
				// Invalid file format
				StringBuffer stringBuffer = new StringBuffer();
				String delimiterTypeName = iArkCommonService.getDelimiterTypeNameByDelimiterChar(phenotypicDelimChr);

				stringBuffer.append("The specified file does not appear to conform to the expected data dictionary file format.\n");
				stringBuffer.append("The specified file format was: " + fileFormat + "\n");
				stringBuffer.append("The specified delimiter was: [" + phenotypicDelimChr + "] (" + delimiterTypeName + ")\n");
				stringBuffer.append("The default pheno data import format is as follows:\n");
				stringBuffer.append("SUBJECTUID" + phenotypicDelimChr + "DATE_COLLECTED" + phenotypicDelimChr);
				for (int i = 0; i < 3; i++) {
					if (i > 0) {
						stringBuffer.append(phenotypicDelimChr);
					}
					if (i < 2) {
						stringBuffer.append("Field" + (i+1));
					}
					else {
						stringBuffer.append("..." + phenotypicDelimChr);
						stringBuffer.append("FieldN");
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

				dataValidationMessages.add(stringBuffer.toString());
			}
			else {
				// Field count = column count - 2 (SUBJECTID and DATE_COLLECTED)
				fieldCount = fieldNameArray.length - 2;
	
				int row = 1;
				// Loop through all rows in file
				while (csvReader.readRecord()) {
					// do something with the newline to put the data into
					// the variables defined above
					stringLineArray = csvReader.getValues();
	
					// Fist column should be SubjectUID
					String subjectUid = stringLineArray[0];
					// Second/1th column should be date collected
					String dateCollectedStr = stringLineArray[1];
	
					// Check subject exists
					LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
					try {
						linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUid, study);
					}
					catch (au.org.theark.core.exception.EntityNotFoundException enfe) {
						// Subject not found...error
						ArkGridCell cell = new ArkGridCell(0, row);
						errorCells.add(cell);
						dataValidationMessages.add(PhenotypicValidationMessage.fieldDataSubjectUidNotFound(subjectUid));
					}
	
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
						dataValidationMessages.add(PhenotypicValidationMessage.dateCollectedNotValidDate(subjectUid, dateCollectedStr));
						errorCells.add(new ArkGridCell(1, row));
					}
	
					/* 
					 * Always Insert records - never update (thus never have to look up existing PhenotypicCollection based on dateCollected) 
					 */

					// Assume inserts
					insertRows.add(row);
					int cols = stringLineArray.length;
	
					// Loop through columns in current row in file, starting from the 2th position
					for (int col = 2; col < cols; col++) {
						PhenoData fieldData = new PhenoData();
						CustomFieldDisplay customFieldDisplay = new CustomFieldDisplay();
						customFieldDisplay.setCustomFieldGroup(this.questionnaire);
						PhenotypicCollection phenoCollection = new PhenotypicCollection();
						fieldData.setCustomFieldDisplay(customFieldDisplay);
						phenoCollection.setRecordDate(dateCollected);
	
						// First/0th column should be the Subject UID
						// If no Subject UID found, caught by exception catch
						phenoCollection.setLinkSubjectStudy(linkSubjectStudy);
	
						// Check field exists
//						try {
							// Set field
							field = new Field();
							fieldName = fieldNameArray[col];
							CustomField cf = iArkCommonService.getCustomFieldByNameStudyArkFunction(fieldName, study, arkFunction);
							if (cf == null || cf.getId() == null) {
								
							}
							customFieldDisplay.setCustomField(cf);
							// TODO: perform lookup of customFieldDisplay
							
							// Other/ith columns should be the field data value
							String value = stringLineArray[col];
							// TODO: If statement required
							fieldData.setTextDataValue(value);
	
							ArkGridCell gridCell = new ArkGridCell(col, row);
							// Validate the field data
							boolean isValid = false; // TODO: Fix: validateFieldData(fieldData, dataValidationMessages);
							if (!isValid) {
								warningCells.add(gridCell);
							}
	
//							// Determine updates NEVER UPDATES ANYMORE
//							if (fieldDataToUpdate.contains(fieldData)) {
//								updateCells.add(gridCell);
//								updateRows.add(row);
//							}
//							else {
								insertCells.add(gridCell);
								insertRows.add(row);
//							}
	
							// Update progress
							curPos += stringLineArray[col].length() + 1; // update progress
	
							// Debug only - Show progress and speed
							log.debug("progress: " + decimalFormat.format(getProgress()) + " % | speed: " + decimalFormat.format(getSpeed()) + " KB/sec");
//						}
//						catch (au.org.theark.core.exception.EntityNotFoundException enfe) {
//							// Field not found...error
//							ArkGridCell cell = new ArkGridCell(0, row);
//							errorCells.add(cell);
//							dataValidationMessages.add(PhenotypicValidationMessage.fieldNotFound(fieldName));
//						}
					}
	
					log.debug("\n");
					subjectCount++;
					row++;
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
		log.debug("Validated " + subjectCount * fieldCount + " rows of data");

		if (errorCells.isEmpty()) {
			for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
				Integer i = (Integer) iterator.next();
				dataValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update");
			}
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
