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
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.commons.lang.time.StopWatch;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.model.pheno.entity.FieldData;
import au.org.theark.core.model.pheno.entity.FieldPhenoCollection;
import au.org.theark.core.model.pheno.entity.FieldType;
import au.org.theark.core.model.pheno.entity.FieldUpload;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.pheno.entity.Status;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.phenotypic.exception.FileFormatException;
import au.org.theark.phenotypic.exception.PhenotypicSystemException;
import au.org.theark.phenotypic.model.dao.IPhenotypicDao;
import au.org.theark.phenotypic.model.vo.PhenoCollectionVO;
import au.org.theark.phenotypic.service.IPhenotypicService;

import com.csvreader.CsvReader;

/**
 * PhenotypicImport provides support for importing phenotypic matrix-formatted files. It features state-machine behaviour to allow an external class
 * to deal with how to store the data pulled out of the files.
 * 
 * @author cellis
 */
@SuppressWarnings("unused")
public class PhenoDataUploader {
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
	private String							fileFormat;
	private IPhenotypicDao				phenotypicDao				= null;
	private Person							person;
	private PhenoCollection				phenoCollection;
	private List<Field>					fieldList;
	private Study							study;
	static Logger							log							= LoggerFactory.getLogger(PhenoDataUploader.class);
	java.util.Collection<String>		fileValidationMessages	= new ArrayList<String>();
	java.util.Collection<String>		dataValidationMessages	= new ArrayList<String>();
	private IPhenotypicService			iPhenoService				= null;
	private IArkCommonService<Void>	iArkCommonService			= null;
	private StringBuffer					uploadReport				= null;
	private Collection<FieldUpload>	fieldUploadCollection	= new ArrayList<FieldUpload>();
	private Long							phenoCollectionId			= null;

	/**
	 * PhenotypicImport constructor
	 * 
	 * @param phenotypicDao
	 *           data access object perform select/insert/updates to the database
	 * @param studyId
	 *           study identifier in context
	 * @param collection
	 *           phenotypic collection in context
	 * @param iArkCommonService
	 *           the common service for dao
	 * @param fileFormat
	 *           format of the file uploaded
	 * @param delimiterChar
	 *           delimiter of the file data (comma, tab etc)
	 */
	public PhenoDataUploader(IPhenotypicService iPhenoService, Study study, PhenoCollection collection, IArkCommonService<Void> iArkCommonService, String fileFormat, char delimiterChar) {
		this.iPhenoService = iPhenoService;
		this.study = study;

		// Not needed for Data Dictionary upload
		if (collection != null) {
			this.phenoCollection = collection;
			this.phenoCollectionId = phenoCollection.getId();
		}
		this.iArkCommonService = iArkCommonService;
		this.fileFormat = fileFormat;
		this.phenotypicDelimChr = delimiterChar;
	}

	/**
	 * Imports the phenotypic data file to the database tables Assumes the file is in the default "matrix" file format:
	 * SUBJECTID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN... 1,01/01/1900,99.99,99.99,, ...
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
	public void uploadMatrixFieldDataFile(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException {
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		Date dateCollected = new Date();
		Field field = null;

		PhenoCollectionVO phenoCollectionVo = new PhenoCollectionVO();
		phenoCollectionVo.setPhenoCollection(this.phenoCollection);

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
			// SUBJECTID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			String[] fieldNameArray = csvReader.getHeaders();

			// Field count = column count - 2 (SUBJECTID and DATE_COLLECTED)
			fieldCount = fieldNameArray.length - 2;

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				String subjectUid = stringLineArray[0];
				LinkSubjectStudy linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUid);
				Collection<FieldData> fieldDataToUpdate = iPhenoService.searchFieldDataBySubjectAndDateCollected(linkSubjectStudy, dateCollected);

				// Loop through columns in current row in file, starting from the 2th position
				for (int i = 0; i < stringLineArray.length; i++) {
					// Field data actually the 2th colum onward
					if (i > 1) {
						log.debug("Creating new field data for: " + Constants.SUBJECTUID + ": " + subjectUid + "\t" + Constants.DATE_COLLECTED + ": " + stringLineArray[1] + "\tFIELD: " + fieldNameArray[i]
								+ "\tVALUE: " + stringLineArray[i]);

						FieldData fieldData = new FieldData();
						fieldData.setCollection(this.phenoCollection);

						// First/0th column should be the Subject UID
						// If no Subject UID found, caught by exception catch
						fieldData.setLinkSubjectStudy(linkSubjectStudy);

						// Second/1th column should be date collected
						try {
							DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY_HH_MM_SS);
							String dateString = stringLineArray[1];

							// If date, just raw date with no time, add default time
							if (dateString.length() <= 10) {
								dateString = dateString.concat(" 00:00:00");
							}

							fieldData.setDateCollected(dateFormat.parse(dateString));
						}
						catch (ParseException pex) {
							// Shouldn't really get here, as date validiated well before this point
							log.error("DateCollected not parsed");
						}

						// Set field
						field = new Field();
						field = iPhenoService.getFieldByNameAndStudy(fieldNameArray[i], study);
						fieldData.setField(field);

						// Other/ith columns should be the field data value
						fieldData.setValue(stringLineArray[i]);

						if (!fieldDataToUpdate.contains(fieldData)) {
							// Try to create the field data
							iPhenoService.createFieldData(fieldData);
						}
						else { // Try to update the field data
							iPhenoService.updateFieldData(fieldData);
						}

						// For update of fields in collection
						phenoCollectionVo.getFieldsSelected().add(field);
					}

					// Update progress
					curPos += stringLineArray[i].length() + 1; // update progress

					// Debug only - Show progress and speed
					log.debug("progress: " + decimalFormat.format(getProgress()) + " % | speed: " + decimalFormat.format(getSpeed()) + " KB/sec");
				}

				log.debug("\n");
				subjectCount++;
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
		log.debug("Inserted " + subjectCount * fieldCount + " rows of data");

		// Update collection/fields in collection
		iPhenoService.updateCollection(phenoCollectionVo);
	}

	/**
	 * Imports the phenotypic data file to the database tables, and creates report on the process Assumes the file is in the default "matrix" file
	 * format: SUBJECTID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN... 1,01/01/1900,99.99,99.99,, ...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @throws IOException
	 *            input/output Exception
	 * @throws OutOfMemoryError
	 *            out of memory Exception
	 * @return the import report detailing the import process
	 */
	public StringBuffer uploadAndReportMatrixFieldDataFile(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException {
		uploadReport = new StringBuffer();
		curPos = 0;
		insertCount = 0;
		updateCount = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		Date dateCollected = new Date();
		Field field = null;

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, phenotypicDelimChr);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0) {
				uploadReport.append("The input size was not greater than 0.  Actual length reported: ");
				uploadReport.append(srcLength);
				uploadReport.append("\n");
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			String[] fieldNameArray = csvReader.getHeaders();

			// Field count = column count - 2 (SUBJECTID and DATE_COLLECTED)
			fieldCount = fieldNameArray.length - 2;

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();

				String subjectUid = stringLineArray[0];
				LinkSubjectStudy linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUid);

				// Second/1th column should be date collected
				try {
					DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY_HH_MM_SS);
					String dateString = stringLineArray[1];

					// If date, just raw date with no time, add default time
					if (dateString.length() <= 10) {
						dateString = dateString.concat(" 00:00:00");
					}

					dateCollected = dateFormat.parse(dateString);
				}
				catch (ParseException pex) {
					// Shouldn't really get here, as date validiated well before this point
					log.error("DateCollected not parsed");
				}

				Collection<FieldData> fieldDataToUpdate = iPhenoService.searchFieldDataBySubjectAndDateCollected(linkSubjectStudy, dateCollected);

				// Loop through columns in current row in file, starting from the 2th position
				for (int i = 0; i < stringLineArray.length; i++) {
					// Field data actually the 2th column onward
					if (i > 1) {
						// Print out column details
						log.debug(fieldNameArray[i] + "\t" + stringLineArray[i]);

						FieldData fieldData = new FieldData();
						fieldData.setCollection(this.phenoCollection);

						// First/0th column should be the Subject UID
						// If no Subject UID found, caught by exception catch
						fieldData.setLinkSubjectStudy(linkSubjectStudy);

						fieldData.setDateCollected(dateCollected);

						// Set field
						String fieldName = fieldNameArray[i];

						try {
							field = iPhenoService.getFieldByNameAndStudy(fieldName, study);
							fieldData.setField(field);
						}
						catch (EntityNotFoundException enf) {
							log.error(enf.getMessage());
						}

						// Check if field in collection
						FieldPhenoCollection fieldPhenoCollection = new FieldPhenoCollection();
						fieldPhenoCollection.setStudy(study);
						fieldPhenoCollection.setField(field);
						fieldPhenoCollection.setPhenoCollection(phenoCollection);

						fieldPhenoCollection = iPhenoService.getFieldPhenoCollection(fieldPhenoCollection);
						if (fieldPhenoCollection == null) {
							// New field to be added to the collection
							fieldPhenoCollection = new FieldPhenoCollection();
							fieldPhenoCollection.setStudy(study);
							fieldPhenoCollection.setField(field);
							fieldPhenoCollection.setPhenoCollection(phenoCollection);
							iPhenoService.createFieldPhenoCollection(fieldPhenoCollection);
						}

						// Other/ith columns should be the field data value
						fieldData.setValue(stringLineArray[i]);

						// Flag data that failed validation, but was overridden and uploaded
						boolean passedQc = PhenotypicValidator.fieldDataPassesQualityControl(fieldData, dataValidationMessages);
						fieldData.setPassedQualityControl(passedQc);

						if (!fieldDataToUpdate.contains(fieldData)) {
							uploadReport.append("Creating new field data for: ");
							uploadReport.append(Constants.SUBJECTUID);
							uploadReport.append(": ");
							uploadReport.append(subjectUid);
							uploadReport.append("\t");
							uploadReport.append(Constants.DATE_COLLECTED);
							uploadReport.append(": ");
							uploadReport.append(stringLineArray[1]);
							uploadReport.append("\tFIELD: ");
							uploadReport.append(fieldName);
							uploadReport.append("\tVALUE: ");
							uploadReport.append(stringLineArray[i]);
							uploadReport.append("\n");

							// Try to create the field data
							iPhenoService.createFieldData(fieldData);

							insertCount++;
						}
						else {
							FieldData oldFieldData = iPhenoService.getFieldData(fieldData);
							oldFieldData.setPassedQualityControl(PhenotypicValidator.fieldDataPassesQualityControl(fieldData, dataValidationMessages));

							uploadReport.append("Updating field data for: ");
							uploadReport.append(Constants.SUBJECTUID);
							uploadReport.append(": ");
							uploadReport.append(subjectUid);
							uploadReport.append("\t");
							uploadReport.append(Constants.DATE_COLLECTED);
							uploadReport.append(": ");
							uploadReport.append(stringLineArray[1]);
							uploadReport.append("\tFIELD: ");
							uploadReport.append(fieldNameArray[i]);
							uploadReport.append("\tOld VALUE: ");
							uploadReport.append(oldFieldData.getValue());
							uploadReport.append("\tNew VALUE: ");
							uploadReport.append(stringLineArray[i]);
							uploadReport.append("\n");

							oldFieldData.setValue(stringLineArray[i]);

							// Try to update the field data
							iPhenoService.updateFieldData(oldFieldData);

							updateCount++;
						}
					}

					// Update progress
					curPos += stringLineArray[i].length() + 1;

					// Debug only - Show progress and speed
					log.debug("progress: " + decimalFormat.format(getProgress()) + " % | speed: " + decimalFormat.format(getSpeed()) + " KB/sec");
				}

				log.debug("\n");
				subjectCount++;
			}
		}
		catch (IOException ioe) {
			uploadReport.append("Unexpected I/O exception whilst reading the phenotypic data file\n");
			log.error("processMatrixPhenoFile IOException stacktrace:", ioe);
			throw new PhenotypicSystemException("Unexpected I/O exception whilst reading the phenotypic data file");
		}
		catch (Exception ex) {
			uploadReport.append("Unexpected exception whilst reading the phenotypic data file\n");
			log.error("processMatrixPhenoFile Exception stacktrace:", ex);
			throw new PhenotypicSystemException("Unexpected exception occurred when trying to process phenotypic data file");
		}
		finally {
			// Clean up the IO objects
			timer.stop();
			uploadReport.append("Total elapsed time: ");
			uploadReport.append(timer.getTime());
			uploadReport.append(" ms or ");
			uploadReport.append(decimalFormat.format(timer.getTime() / 1000.0));
			uploadReport.append(" s");
			uploadReport.append("\n");
			uploadReport.append("Total file size: ");
			uploadReport.append(inLength);
			uploadReport.append(" B or ");
			uploadReport.append(decimalFormat.format(inLength / 1024.0 / 1024.0));
			uploadReport.append(" MB");
			uploadReport.append("\n");

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
		uploadReport.append("Inserted ");
		uploadReport.append(insertCount);
		uploadReport.append(" rows of data");
		uploadReport.append("\n");

		uploadReport.append("Updated ");
		uploadReport.append(updateCount);
		uploadReport.append(" rows of data");
		uploadReport.append("\n");

		// Set status of collection
		PhenoCollection phenoCollection = iPhenoService.getPhenoCollection(this.phenoCollectionId);
		Status status = new Status();
		status = iPhenoService.getStatusByName("ACTIVE");
		phenoCollection.setStatus(status);
		iPhenoService.updateCollection(phenoCollection);

		return uploadReport;
	}

	/**
	 * Imports the data dictionary file to the database tables, and creates report on the process Assumes the file is in the default "matrix" file
	 * format: "FIELD_NAME","FIELD_TYPE","DESCRIPTION","UNITS","ENCODED_VALUES","MINIMUM_VALUE","MAXIMUM_VALUE","MISSING_VALUE"
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @throws IOException
	 *            input/output Exception
	 * @throws OutOfMemoryError
	 *            out of memory Exception
	 * @return the import report detailing the import process
	 */
	public StringBuffer uploadAndReportMatrixDataDictionaryFile(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException {
		uploadReport = new StringBuffer();
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		Date dateCollected = new Date();
		Field field = null;

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, phenotypicDelimChr);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0) {
				uploadReport.append("The input size was not greater than 0.  Actual length reported: ");
				uploadReport.append(srcLength);
				uploadReport.append("\n");
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
				String fieldName = stringLineArray[0];

				// Set field
				field = new Field();
				field.setStudy(study);

				try {
					Field oldField = iPhenoService.getFieldByNameAndStudy(fieldName, study);

					uploadReport.append("Updating field for: ");
					uploadReport.append("\tFIELD: ");
					fieldName = csvReader.get("FIELD_NAME");
					uploadReport.append(csvReader.get("FIELD_NAME"));
					uploadReport.append("\n");

					oldField.setName(fieldName);

					FieldType fieldType = new FieldType();
					fieldType = iPhenoService.getFieldTypeByName(csvReader.get("FIELD_TYPE"));
					oldField.setFieldType(fieldType);

					oldField.setDescription(csvReader.get("DESCRIPTION"));
					oldField.setUnits(csvReader.get("UNITS"));
					oldField.setEncodedValues(csvReader.get("ENCODED_VALUES"));
					oldField.setMinValue(csvReader.get("MINIMUM_VALUE"));
					oldField.setMaxValue(csvReader.get("MAXIMUM_VALUE"));
					oldField.setMissingValue(csvReader.get("MISSING_VALUE"));

					// Try to update the oldField
					iPhenoService.updateField(oldField);
					updateCount++;

					FieldUpload fieldUpload = new FieldUpload();
					fieldUpload.setField(oldField);
					fieldUploadCollection.add(fieldUpload);
				}
				catch (EntityNotFoundException enf) {
					field = new Field();
					field.setStudy(study);
					field.setName(fieldName);

					FieldType fieldType = new FieldType();
					fieldType = iPhenoService.getFieldTypeByName(csvReader.get("FIELD_TYPE"));
					field.setFieldType(fieldType);
					field.setDescription(csvReader.get("DESCRIPTION"));
					field.setUnits((csvReader.get("UNITS")));
					field.setEncodedValues(csvReader.get("ENCODED_VALUES"));
					field.setMinValue(csvReader.get("MINIMUM_VALUE"));
					field.setMaxValue(csvReader.get("MAXIMUM_VALUE"));
					field.setMissingValue(csvReader.get("MISSING_VALUE"));

					uploadReport.append("Creating new field: ");
					uploadReport.append("\tFIELD: ");
					uploadReport.append((stringLineArray[csvReader.getIndex("FIELD_NAME")]));
					uploadReport.append("\n");

					// Try to create the field
					iPhenoService.createField(field);
					insertCount++;

					FieldUpload fieldUpload = new FieldUpload();
					fieldUpload.setField(field);
					fieldUploadCollection.add(fieldUpload);
				}

				// Debug only - Show progress and speed
				log.debug("progress: " + decimalFormat.format(getProgress()) + " % | speed: " + decimalFormat.format(getSpeed()) + " KB/sec");
				log.debug("\n");
				fieldCount++;
			}
		}
		catch (IOException ioe) {
			uploadReport.append("Unexpected I/O exception whilst reading the phenotypic data file\n");
			log.error("uploadAndReportMatrixDataDictionaryFile IOException stacktrace:", ioe);
			throw new PhenotypicSystemException("Unexpected I/O exception whilst reading the phenotypic data file");
		}
		catch (Exception ex) {
			uploadReport.append("Unexpected exception whilst reading the phenotypic data file\n");
			log.error("uploadAndReportMatrixDataDictionaryFile Exception stacktrace:", ex);
			throw new PhenotypicSystemException("Unexpected exception occurred when trying to process phenotypic data file");
		}
		finally {
			// Clean up the IO objects
			timer.stop();
			uploadReport.append("Total elapsed time: ");
			uploadReport.append(timer.getTime());
			uploadReport.append(" ms or ");
			uploadReport.append(decimalFormat.format(timer.getTime() / 1000.0));
			uploadReport.append(" s");
			uploadReport.append("\n");
			uploadReport.append("Total file size: ");
			uploadReport.append(inLength);
			uploadReport.append(" B or ");
			uploadReport.append(decimalFormat.format(inLength / 1024.0 / 1024.0));
			uploadReport.append(" MB");
			uploadReport.append("\n");

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

		uploadReport.append("Inserted ");
		uploadReport.append(insertCount);
		uploadReport.append(" rows of data");
		uploadReport.append("\n");

		uploadReport.append("Updated ");
		uploadReport.append(updateCount);
		uploadReport.append(" rows of data");
		uploadReport.append("\n");

		return uploadReport;
	}

	public Collection<FieldUpload> getFieldUploadCollection() {
		return fieldUploadCollection;
	}

	public void setFieldUploadCollection(Collection<FieldUpload> fieldUploadCollection) {
		this.fieldUploadCollection = fieldUploadCollection;
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
