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
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;

import org.apache.shiro.SecurityUtils;
import org.apache.wicket.spring.injection.annot.SpringBean;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.exception.SystemDataMismatchException;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ArkModule;
import au.org.theark.core.model.study.entity.FieldType;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PhenoFieldUpload;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.UnitType;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.PhenoDataSetFieldVO;
import au.org.theark.phenotypic.service.IPhenotypicService;

import com.csvreader.CsvReader;

/**
 * CustomFieldImporter provides support for importing matrix-formatted files for defining custom fields. 
 * It features state-machine behaviour to allow an external class to deal with how to store the data pulled out of the files.
 * 
 * @author cellis
 * @author elam
 */
@SuppressWarnings("unused")
public class PhenoDataSetFieldImporter implements IPhenoImporter,Serializable {
	
	private static final long serialVersionUID = 1L;
	private String							fieldName;
	private long							subjectCount;
	private long							fieldCount;
	private long							insertCount;
	private long							updateCount;
	private double							speed;
	private long							curPos;
	private long							srcLength					= -1;																// -1 means nothing being processed
	private char							phenotypicDelimChr		= Constants.IMPORT_DELIM_CHAR_COMMA;						// default phenotypic file
																																						// delimiter: COMMA
	private String							fileFormat;
	private Person							person;
	private List<PhenoDataSetField>				fieldList;
	private Study							study;
	static Logger							log							= LoggerFactory.getLogger(PhenoDataSetFieldImporter.class);
	java.util.Collection<String>			fileValidationMessages	= new ArrayList<String>();
	java.util.Collection<String>			dataValidationMessages	= new ArrayList<String>();
	private IArkCommonService<Void>			iArkCommonService			= null;
	private StringBuffer					uploadReport				= null;
	private List<PhenoFieldUpload>			fieldUploadList			= new ArrayList<PhenoFieldUpload>();
	private Long							phenoCollectionId			= null;
	private ArkFunction 					arkFunction;
	private Date							completionTime = null;
	private ArkModule 						arkModule;
	private IPhenotypicService			iPhenotypicService;
	
	
	/**
	 * PhenotypicImport constructor
	 * 
	 * @param studyId
	 *           study identifier in context
	 * @param arkFunction
	 *           the function that this CustomField import should attach to
	 * @param iArkCommonService
	 *           the common service for dao
	 * @param fileFormat
	 *           format of the file uploaded
	 * @param delimiterChar
	 *           delimiter of the file data (comma, tab etc)
	 */
	public PhenoDataSetFieldImporter(Study study, ArkFunction arkFunction, IArkCommonService<Void> iArkCommonService,IPhenotypicService iPhenotypicService, String fileFormat, char delimiterChar) {
		this.study = study;
		this.iArkCommonService = iArkCommonService;
		this.iPhenotypicService=iPhenotypicService;
		this.fileFormat = fileFormat;
		this.phenotypicDelimChr = delimiterChar;
		this.arkFunction = arkFunction;
		Long sessionModuleId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.ARK_MODULE_KEY);
		this.arkModule= iArkCommonService.getArkModuleById(sessionModuleId);
	}


	/**
	 * Imports the data dictionary file to the database tables, and creates report on the process. Assumes the file is in the default "matrix" file
	 * format:<br>"FIELD_NAME","FIELD_TYPE","DESCRIPTION","QUESTION","UNITS","ENCODED_VALUES","MINIMUM_VALUE","MAXIMUM_VALUE","MISSING_VALUE","REQUIRED"
	 * @param fileInputStream
	 * 		the input stream of the file
	 * @param inLength
	 * 		the lenght of the file
	 * @return A String containing the import report details
	 * @throws FileFormatException
	 * @throws ArkSystemException
	 */
	@Override
	public StringBuffer uploadAndReportMatrixDataDictionaryFile(InputStream fileInputStream, long inLength) throws FileFormatException, ArkSystemException {
		uploadReport = new StringBuffer();
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		Date dateCollected = new Date();
		PhenoDataSetField phenoDataSetField = null;

		completionTime = null;
		
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

			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());
		
			ArkFunction arkFunctionToBeUsed = arkFunction;
				
			log.info("ark function = " + arkFunction.getName());
			
			//these fields must be available for phenocollection...therefore we are to save / update / get by that ark function...ideally this should be by ark module
				arkFunctionToBeUsed = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				String fieldName = stringLineArray[0];
				// Set field
				phenoDataSetField = new PhenoDataSetField();
				phenoDataSetField.setStudy(study);
				//PhenoDataSetField oldField = iArkCommonService.getCustomFieldByNameStudyArkFunction(csvReader.get("FIELD_NAME"), study, arkFunctionToBeUsed);
				PhenoDataSetField oldField =iPhenotypicService.getPhenoDataSetFieldByNameStudyArkFunction(csvReader.get("FIELD_NAME"), study, arkFunctionToBeUsed);
				if (oldField != null) {
					uploadReport.append("Updating field for: ");
					uploadReport.append("\tFIELD: ");
					fieldName = csvReader.get("FIELD_NAME");
					uploadReport.append(csvReader.get("FIELD_NAME"));
					uploadReport.append("\n");
					oldField.setName(fieldName);
					//Update the Custom field Type and Custom Field Category on 2015-08-24
					FieldType fieldType = iArkCommonService.getFieldTypeByName(csvReader.get("FIELD_TYPE"));
					oldField.setFieldType(fieldType);
					oldField.setDescription(csvReader.get("DESCRIPTION"));
					oldField.setFieldLabel(csvReader.get("QUESTION"));
					oldField.setUnitTypeInText(csvReader.get("UNITS"));
					oldField.setEncodedValues(csvReader.get("ENCODED_VALUES"));
					oldField.setMinValue(csvReader.get("MINIMUM_VALUE"));
					oldField.setMaxValue(csvReader.get("MAXIMUM_VALUE"));
					oldField.setMissingValue(csvReader.get("MISSING_VALUE"));
					oldField.setDefaultValue(csvReader.get("DEFAULT_VALUE"));
					uploadReport.append("Updating exsisting field: ");
					uploadReport.append("\tFIELD: ");
					uploadReport.append((stringLineArray[csvReader.getIndex("FIELD_NAME")]));
					uploadReport.append("\n");
					oldField.setRequired(csvReader.get("REQUIRED") != null && 
							(	csvReader.get("REQUIRED").equalsIgnoreCase("yes") ||
								csvReader.get("REQUIRED").equalsIgnoreCase("y") ||
								csvReader.get("REQUIRED").equalsIgnoreCase("true") ||
								csvReader.get("REQUIRED").equalsIgnoreCase("1") ) );
					oldField.setAllowMultiselect(csvReader.get("ALLOW_MULTIPLE_SELECTIONS") != null && 
							(	csvReader.get("ALLOW_MULTIPLE_SELECTIONS").equalsIgnoreCase("yes") ||
								csvReader.get("ALLOW_MULTIPLE_SELECTIONS").equalsIgnoreCase("y") ||
								csvReader.get("ALLOW_MULTIPLE_SELECTIONS").equalsIgnoreCase("true") ||
								csvReader.get("ALLOW_MULTIPLE_SELECTIONS").equalsIgnoreCase("1") ) );
					// Try to update the oldField
					PhenoDataSetFieldVO updateCFVo = new PhenoDataSetFieldVO();
					updateCFVo.setPhenoDataSetField(oldField);
					//updateCFVo.getPhenoDataSetField().setRequired(csvReader.get("REQUIRED") != null);
					iPhenotypicService.updatePhenoDataSetField(updateCFVo);
					updateCount++;
					PhenoFieldUpload phenoFieldUpload=new PhenoFieldUpload();
					phenoFieldUpload.setPhenoDataSetField(oldField);
					fieldUploadList.add(phenoFieldUpload);
				}
				else {
					phenoDataSetField = new PhenoDataSetField();
					phenoDataSetField.setStudy(study);
					phenoDataSetField.setName(fieldName);
					phenoDataSetField.setArkFunction(arkFunctionToBeUsed);
					phenoDataSetField.setUnitTypeInText(csvReader.get("UNITS"));
					FieldType fieldType = iArkCommonService.getFieldTypeByName(csvReader.get("FIELD_TYPE"));
					phenoDataSetField.setFieldType(fieldType);
					phenoDataSetField.setDescription(csvReader.get("DESCRIPTION"));
					phenoDataSetField.setFieldLabel(csvReader.get("QUESTION"));
					phenoDataSetField.setEncodedValues(csvReader.get("ENCODED_VALUES"));
					phenoDataSetField.setMinValue(csvReader.get("MINIMUM_VALUE"));
					phenoDataSetField.setMaxValue(csvReader.get("MAXIMUM_VALUE"));
					phenoDataSetField.setMissingValue(csvReader.get("MISSING_VALUE"));
					phenoDataSetField.setDefaultValue(csvReader.get("DEFAULT_VALUE"));
					uploadReport.append("Creating new field: ");
					uploadReport.append("\tFIELD: ");
					uploadReport.append((stringLineArray[csvReader.getIndex("FIELD_NAME")]));
					uploadReport.append("\n");
					phenoDataSetField.setRequired(csvReader.get("REQUIRED") != null && 
							(	csvReader.get("REQUIRED").equalsIgnoreCase("yes") ||
								csvReader.get("REQUIRED").equalsIgnoreCase("y") ||
								csvReader.get("REQUIRED").equalsIgnoreCase("true") ||
								csvReader.get("REQUIRED").equalsIgnoreCase("1") ) );
					phenoDataSetField.setAllowMultiselect(csvReader.get("ALLOW_MULTIPLE_SELECTIONS") != null && 
							(	csvReader.get("ALLOW_MULTIPLE_SELECTIONS").equalsIgnoreCase("yes") ||
								csvReader.get("ALLOW_MULTIPLE_SELECTIONS").equalsIgnoreCase("y") ||
								csvReader.get("ALLOW_MULTIPLE_SELECTIONS").equalsIgnoreCase("true") ||
								csvReader.get("ALLOW_MULTIPLE_SELECTIONS").equalsIgnoreCase("1") ) );
					// Try to create the field
					PhenoDataSetFieldVO phenoDataSetFieldVO=new PhenoDataSetFieldVO();
					phenoDataSetFieldVO.setPhenoDataSetField(phenoDataSetField);
					//phenoDataSetFieldVO.setUsePhenoDataSetFieldDisplay(true);	// do not create the CustomFieldDisplay entity */
					iPhenotypicService.createPhenoDataSetField(phenoDataSetFieldVO);
					insertCount++;
					PhenoFieldUpload phenoFieldUpload=new PhenoFieldUpload();
					phenoFieldUpload.setPhenoDataSetField(phenoDataSetField);
					fieldUploadList.add(phenoFieldUpload);
				}

				fieldCount++;
			}
			completionTime = new Date(System.currentTimeMillis());
		}
		catch (IOException ioe) {
			uploadReport.append("Unexpected I/O exception whilst reading the phenotypic data file\n");
			log.error("uploadAndReportMatrixDataDictionaryFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the phenotypic data file");
		}
		catch (Exception ex) {
			uploadReport.append("Unexpected exception whilst reading the phenotypic data file\n");
			log.error("uploadAndReportMatrixDataDictionaryFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process phenotypic data file");
		}
		finally {
			uploadReport.append("Total file size: ");
			uploadReport.append(inLength);
			uploadReport.append(" B or ");
			uploadReport.append(decimalFormat.format(inLength / 1024.0 / 1024.0));
			uploadReport.append(" MB");
			uploadReport.append("\n");

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

	public List<PhenoFieldUpload> getFieldUploadList() {
		return fieldUploadList;
	}


	public void setFieldUploadList(List<PhenoFieldUpload> fieldUploadList) {
		this.fieldUploadList = fieldUploadList;
	}


	/**
	 * Return the inputstream of the converted workbook as csv
	 * 
	 * @return inputstream of the converted workbook as csv
	 */
	@Override
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

}
