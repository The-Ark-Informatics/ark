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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.lims.entity.BioCollection;
import au.org.theark.core.model.lims.entity.BioSampletype;
import au.org.theark.core.model.lims.entity.BioTransaction;
import au.org.theark.core.model.lims.entity.BioTransactionStatus;
import au.org.theark.core.model.lims.entity.Biospecimen;
import au.org.theark.core.model.lims.entity.InvCell;
import au.org.theark.core.model.lims.entity.TreatmentType;
import au.org.theark.core.model.lims.entity.Unit;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.XLStoCSV;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;

import com.csvreader.CsvReader;

/**
 * BiospecimenUploader provides support for uploading biospecimen matrix-formatted files. It features state-machine behaviour to allow an external class to
 * deal with how to store the data pulled out of the files.
 * 
 * @author cellis
 */
public class BioCollectionSpecimenUploader {
	private long					recordCount;
	private long					insertCount;
	private long					updateCount;
	private long					curPos;
	private long					srcLength				= -1;
	private StopWatch				timer						= null;
	private char					delimiterCharacter	= Constants.DEFAULT_DELIMITER_CHARACTER;
	private Study					study;
	static Logger					log						= LoggerFactory.getLogger(BioCollectionSpecimenUploader.class);
	@SuppressWarnings("unchecked")
	private IArkCommonService		iArkCommonService		= null;
	private ILimsService			iLimsService			= null;
	private IInventoryService		iInventoryService		= null;
	private StringBuffer			uploadReport			= null;
	private SimpleDateFormat		simpleDateFormat		= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private List<Biospecimen>		insertBiospecimens	= new ArrayList<Biospecimen>();
	//private List<Biospecimen>		updateBiospecimens	= new ArrayList<Biospecimen>();
	private List<BioCollection>		insertBiocollections	= new ArrayList<BioCollection>();
	private List<BioCollection>		updateBiocollections	= new ArrayList<BioCollection>();
	private List<InvCell>			updateInvCells			= new ArrayList<InvCell>();

	/**
	 * BiospecimenUploader constructor
	 * 
	 * @param study
	 *           study identifier in context
	 * @param iArkCommonService
	 *           common ARK service to perform select/insert/updates to the database
	 * @param iLimsService
	 *           LIMS service to perform select/insert/updates to the LIMS database
	 * @param iInventoryService
	 * 	       LIMS inventory service to perform select/insert/updates to the LIMS database
	 */
	@SuppressWarnings("unchecked")
	public BioCollectionSpecimenUploader(Study study, IArkCommonService iArkCommonService, ILimsService iLimsService, IInventoryService iInventoryService) {
		this.study = study;
		this.iArkCommonService = iArkCommonService;
		this.iLimsService = iLimsService;
		this.iInventoryService = iInventoryService;
		simpleDateFormat.setLenient(false);
	}
	/**
	 * 
	 * Upload the biocollection file data.
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
	 * @return the upload report detailing the upload process
	 */
	public StringBuffer uploadAndReportMatrixBiocollectionFile(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkSystemException {
		delimiterCharacter = inDelimChr;
		uploadReport = new StringBuffer();
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		// If Excel, convert to CSV for validation
		if (inFileFormat.equalsIgnoreCase("XLS")) {
			Workbook w;
			try {
				w = Workbook.getWorkbook(fileInputStream);
				delimiterCharacter = ',';
				XLStoCSV xlsToCsv = new XLStoCSV(delimiterCharacter);
				fileInputStream = xlsToCsv.convertXlsToCsv(w);
				fileInputStream.reset();
			}
			catch (BiffException e) {
				log.error(e.getMessage());
			}
			catch (IOException e) {
				log.error(e.getMessage());
			}
		}

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);

			srcLength = inLength;
			if (srcLength <= 0) {
				uploadReport.append("The input size was not greater than 0. Actual length reported: ");
				uploadReport.append(srcLength);
				uploadReport.append("\n");
				throw new FileFormatException("The input size was not greater than 0. Actual length reported: " + srcLength);
			}
			timer = new StopWatch();
			timer.start();
			csvReader.readHeaders();
			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());
			// Loop through all rows in file
			while (csvReader.readRecord()) {
				log.info("At record: " + recordCount);
				String subjectUID = csvReader.get("SUBJECTUID");
				String biocollectionUID = csvReader.get("BIOCOLLECTIONUID");
				LinkSubjectStudy linkSubjectStudy = iArkCommonService.getSubjectByUIDAndStudy(subjectUID, study);
				//this is validated in prior step and should never happen
				if(linkSubjectStudy==null){
					log.error("\n\n\n\n\n\n\n\n\n\n\n\nUnexpected subject? a shouldnt happen...we should have errored this in validation");
					break;//TODO : log appropriately or do some handling
				}
				BioCollection bioCollection = iLimsService.getBioCollectionForStudySubjectByUID(biocollectionUID, study, linkSubjectStudy);
				if(bioCollection == null) {
					bioCollection = new BioCollection();
					if(study.getAutoGenerateBiocollectionUid()){
						// if biocollection not in the system we have to create a new biocollection uid.
						bioCollection.setBiocollectionUid(iLimsService.getNextGeneratedBiospecimenUID(study));
					}else{
						bioCollection.setBiocollectionUid(biocollectionUID);
					}
				}else{// if exsists we do not want to auto genetared the uid.
					bioCollection.setBiocollectionUid(biocollectionUID);
				}
				bioCollection.setStudy(study);
				bioCollection.setLinkSubjectStudy(linkSubjectStudy);

				if (csvReader.getIndex("NAME") > 0) {
					String name = csvReader.get("NAME");
					bioCollection.setName(name);
				}
				if (csvReader.getIndex("COLLECTIONDATE") > 0) {
					String collectionDate = csvReader.get("COLLECTIONDATE");
					bioCollection.setCollectionDate(simpleDateFormat.parse(collectionDate));
				}
				if (csvReader.getIndex("COMMENTS") > 0) {
					String comments = csvReader.get("COMMENTS");
					bioCollection.setComments(comments);
				}
				//validation SHOULD make sure these cases will work.  TODO:  test scripts
				
				if(bioCollection.getId()==null){
					insertBiocollections.add(bioCollection);
					StringBuffer sb = new StringBuffer();
					sb.append("BioCollectionUID: ");
					sb.append(bioCollection.getBiocollectionUid());
					sb.append(" has been created successfully.");
					sb.append("\n");
					uploadReport.append(sb);
					insertCount++;
				}else {
					updateBiocollections.add(bioCollection);
					StringBuffer sb = new StringBuffer();
					sb.append("BioCollectionUID: ");
					sb.append(bioCollection.getBiocollectionUid());
					sb.append(" has been updated successfully.");
					sb.append("\n");
					uploadReport.append(sb);
					updateCount++;
				}
				recordCount++;
			}
		}
		catch (IOException ioe) {
			uploadReport.append("An unexpected I/O exception occurred whilst reading the biospecimen data file.\n");
			log.error("processMatrixBiospecimenFile IOException stacktrace:", ioe);
			throw new ArkSystemException("An unexpected I/O exception occurred whilst reading the biospecimen data file.");
		}
		catch (Exception ex) {
			uploadReport.append("An unexpected exception occurred whilst reading the biospecimen data file.\n");
			log.error("processMatrixBiospecimenFile Exception stacktrace:", ex);
			throw new ArkSystemException("An unexpected exception occurred when trying to process biospecimen data file.");
		}
		finally {
			// Clean up the IO objects
			timer.stop();
			uploadReport.append("\n");
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
		uploadReport.append("Processed ");
		uploadReport.append(recordCount);
		uploadReport.append(" records.");
		uploadReport.append("\n");
		uploadReport.append("Inserted ");
		uploadReport.append(insertCount);
		uploadReport.append(" records.");
		uploadReport.append("\n");
		uploadReport.append("Updated ");
		uploadReport.append(updateCount);
		uploadReport.append(" records.");
		uploadReport.append("\n");

		// Batch insert/update
		iLimsService.batchInsertBiocollections(insertBiocollections);
		iLimsService.batchUpdateBiocollections(updateBiocollections);
		
		return uploadReport;
	}
		/**
	 * Upload Biospecimen Inventory location file.
	 * 
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
	 * @return the upload report detailing the upload process
	 */
	public StringBuffer uploadAndReportMatrixBiospecimenInventoryFile(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkSystemException {
		delimiterCharacter = inDelimChr;
		uploadReport = new StringBuffer();
		curPos = 0;
		List<InvCell> cellsToUpdate = new ArrayList<InvCell>();

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		// If Excel, convert to CSV for validation
		if (inFileFormat.equalsIgnoreCase("XLS")) {
			Workbook w;
			try {
				w = Workbook.getWorkbook(fileInputStream);
				delimiterCharacter = ',';
				XLStoCSV xlsToCsv = new XLStoCSV(delimiterCharacter);
				fileInputStream = xlsToCsv.convertXlsToCsv(w);
				fileInputStream.reset();
			}
			catch (BiffException e) {
				log.error(e.getMessage());
			}
			catch (IOException e) {
				log.error(e.getMessage());
			}
		}

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);

			srcLength = inLength;
			if (srcLength <= 0) {
				uploadReport.append("The input size was not greater than 0. Actual length reported: ");
				uploadReport.append(srcLength);
				uploadReport.append("\n");
				throw new FileFormatException("The input size was not greater than 0. Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				log.info("At record: " + recordCount);
				String biospecimenUID = csvReader.get("BIOSPECIMENUID");
				Biospecimen biospecimen = iLimsService.getBiospecimenByUid(biospecimenUID,study);
				if(biospecimen == null) {
					log.error("\n\n\n\n\n\n\n\n\n....We should NEVER have null biospecimens this should be  validated in prior step");
					break;
				}
				// Allocation details
				InvCell invCell;
				String siteName = null;
				String freezerName = null;
				String rackName = null;
				String boxName = null;
				String row = null;
				String column = null;
				
				if (csvReader.getIndex("SITE") > 0) {
					siteName = csvReader.get("SITE");
				}

				if (csvReader.getIndex("FREEZER") > 0) {
					freezerName = csvReader.get("FREEZER");
				}

				if (csvReader.getIndex("RACK") > 0) {
					rackName = csvReader.get("RACK");
				}

				if (csvReader.getIndex("BOX") > 0) {
					boxName = csvReader.get("BOX");
				}
				
				if (csvReader.getIndex("ROW") > 0) {
					row = csvReader.get("ROW");
				}
				
				if (csvReader.getIndex("COLUMN") > 0) {
					column = csvReader.get("COLUMN");
				}
				
				invCell = iInventoryService.getInvCellByLocationNames(siteName, freezerName, rackName, boxName, row, column);

				if(invCell != null && invCell.getId() != null) {
					if(invCell.getBiospecimen()!=null){
						log.error("This should NEVER happen as validation should ensure no cell will wipte another");
						break;
					}
					invCell.setBiospecimen(biospecimen);
					cellsToUpdate.add(invCell);
 					updateCount++;
				}
				else{
					log.error("This should NEVER happen as validation should ensure all cells valid");
					break;
				}
				
				recordCount++;
			}
		}
		catch (IOException ioe) {
			uploadReport.append("An unexpected I/O exception occurred whilst reading the biospecimen data file.\n");
			log.error("processMatrixBiospecimenFile IOException stacktrace:", ioe);
			throw new ArkSystemException("An unexpected I/O exception occurred whilst reading the biospecimen data file.");
		}
		catch (Exception ex) {
			uploadReport.append("An unexpected exception occurred whilst reading the biospecimen data file\n");
			log.error("processMatrixBiospecimenFile Exception stacktrace:", ex);
			throw new ArkSystemException("An unexpected exception occurred when trying to process biospecimen data file.");
		}
		finally {
			// Clean up the IO objects
			timer.stop();
			uploadReport.append("\n");
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
		uploadReport.append("Processed ");
		uploadReport.append(recordCount);
		uploadReport.append(" records.");
		uploadReport.append("\n");
		uploadReport.append("Updated ");
		uploadReport.append(updateCount);
		uploadReport.append(" records.");
		uploadReport.append("\n");
		
		iLimsService.batchUpdateInvCells(cellsToUpdate);
		
		return uploadReport;
	}
	/**
	 * 
	 * Upload the biospecimen file data.
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
	 * @return the upload report detailing the upload process
	 */
	public StringBuffer uploadAndReportMatrixBiospecimenFile(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkSystemException {
		delimiterCharacter = inDelimChr;
		uploadReport = new StringBuffer();
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		// If Excel, convert to CSV for validation
		if (inFileFormat.equalsIgnoreCase("XLS")) {
			Workbook w;
			try {
				w = Workbook.getWorkbook(fileInputStream);
				delimiterCharacter = ',';
				XLStoCSV xlsToCsv = new XLStoCSV(delimiterCharacter);
				fileInputStream = xlsToCsv.convertXlsToCsv(w);
				fileInputStream.reset();
			}
			catch (BiffException e) {
				log.error(e.getMessage());
			}
			catch (IOException e) {
				log.error(e.getMessage());
			}
		}

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);

			srcLength = inLength;
			if (srcLength <= 0) {
				uploadReport.append("The input size was not greater than 0. Actual length reported: ");
				uploadReport.append(srcLength);
				uploadReport.append("\n");
				throw new FileFormatException("The input size was not greater than 0. Actual length reported: " + srcLength);
			}
			timer = new StopWatch();
			timer.start();
			csvReader.readHeaders();
			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				log.info("At record: " + recordCount);
				String subjectUID = csvReader.get("SUBJECTUID");
				String biospecimenUID = csvReader.get("BIOSPECIMENUID");
				LinkSubjectStudy linkSubjectStudy = iArkCommonService.getSubjectByUIDAndStudy(subjectUID, study);
				//this is validated in prior step and should never happen
				if(linkSubjectStudy==null){
					log.error("\n\n\n\n\n\n\n\n\n\n\n\n Unexpected subject? a shouldnt happen...we should have errored this in validation");
					break;//TODO : log appropriately or do some handling
				}
				//Always create a new biospecimen in this time 
				//exsisting biospecimen are not allow to update in here.
				Biospecimen biospecimen = iLimsService.getBiospecimenByUid(biospecimenUID,study);
				if(biospecimen == null) {
					biospecimen = new Biospecimen();
				}else{
					log.error("\n\n\n\n\n\n\n\n\n....We should NEVER have existing biospecimens this should be  validated in prior step");
					break;
				}
				biospecimen.setStudy(study);
				biospecimen.setLinkSubjectStudy(linkSubjectStudy);
				if (csvReader.getIndex("BIOCOLLECTIONUID") > 0) {
					String biocollectionUid = csvReader.get("BIOCOLLECTIONUID");
					BioCollection bioCollection = iLimsService.getBioCollectionByUID(biocollectionUid,this.study.getId(), subjectUID);
					if(bioCollection == null){
						log.error("\n\n\n\n\n\n\n\n\n....We already validated for the exsisting biocollection and we never created "
								+ "new one if it does not exsists.");
						break;
					}
					else{
						biospecimen.setBioCollection(bioCollection);
					}
				}
				if (csvReader.getIndex("SAMPLETYPE") > 0) {
					String name = csvReader.get("SAMPLETYPE");
					BioSampletype sampleType = new BioSampletype();
					sampleType = iLimsService.getBioSampleTypeByName(name);
					biospecimen.setSampleType(sampleType);
				}
				if (csvReader.getIndex("QUANTITY") > 0) {
					String quantity = csvReader.get("QUANTITY");
					biospecimen.setQuantity(new Double(quantity));
				}
				if (csvReader.getIndex("CONCENTRATION") > 0) {
					String concentration = csvReader.get("CONCENTRATION");
					if (concentration != null && !concentration.isEmpty()) {
						try{
							biospecimen.setConcentration(Double.parseDouble(concentration));
						}
						catch(NumberFormatException ne){
							log.error("Already validated in the previous step and never happen the for error");
						}
					}
				}
				if (csvReader.getIndex("UNITS") > 0) {
					String name = csvReader.get("UNITS");
					Unit unit = iLimsService.getUnitByName(name);
					biospecimen.setUnit(unit);
				}
				if (csvReader.getIndex("TREATMENT") > 0) {
					String name = csvReader.get("TREATMENT");
					TreatmentType treatmentType = iLimsService.getTreatmentTypeByName(name);
					biospecimen.setTreatmentType(treatmentType);
				}
				Set<BioTransaction> bioTransactions = new HashSet<BioTransaction>(0);
				// Inheriently create a transaction for the initial quantity
				BioTransaction bioTransaction = new BioTransaction();
				bioTransaction.setBiospecimen(biospecimen);
				bioTransaction.setTransactionDate(Calendar.getInstance().getTime());
				bioTransaction.setQuantity(biospecimen.getQuantity());
				bioTransaction.setReason(au.org.theark.lims.web.Constants.BIOTRANSACTION_STATUS_INITIAL_QUANTITY);
				bioTransaction.setUnit(biospecimen.getUnit());

				BioTransactionStatus initialStatus = iLimsService.getBioTransactionStatusByName(au.org.theark.lims.web.Constants.BIOTRANSACTION_STATUS_INITIAL_QUANTITY);
				bioTransaction.setStatus(initialStatus);	//ensure that the initial transaction can be identified
				bioTransactions.add(bioTransaction);
				biospecimen.setBioTransactions(bioTransactions);
				//validation SHOULD make sure these cases will work.  TODO:  test scripts
				if(study.getAutoGenerateBiospecimenUid()){
					biospecimen.setBiospecimenUid(iLimsService.getNextGeneratedBiospecimenUID(study));
				}else{
					biospecimen.setBiospecimenUid(biospecimenUID);
				}
				insertBiospecimens.add(biospecimen);
				StringBuffer sb = new StringBuffer();
				sb.append("Biospecimen UID: ");
				sb.append(biospecimen.getBiospecimenUid());
				sb.append(" has been created successfully.");
				sb.append("\n");
				uploadReport.append(sb);
				insertCount++;
				// Allocation details
				String siteName =  csvReader.get("SITE");
				String freezerName =csvReader.get("FREEZER");
				String rackName = csvReader.get("RACK");
				String boxName = csvReader.get("BOX");
				String row = csvReader.get("ROW");
				String column = csvReader.get("COLUMN");
				InvCell invCell = iInventoryService.getInvCellByLocationNames(siteName, freezerName, rackName, boxName, row, column);
				//Biospecimen was supposed to locate in the following valid, empty inventory cell
				// inventory cell is not persist with biospeciman. So we have to update the valid inventory cell location with the 
				//biospecimen uid which we will do it while bispecimen creates.
				biospecimen.setInvCell(invCell);
				recordCount++;
			}
		}
		catch (IOException ioe) {
			uploadReport.append("An unexpected I/O exception occurred whilst reading the biospecimen data file.\n");
			log.error("processMatrixBiospecimenFile IOException stacktrace:", ioe);
			throw new ArkSystemException("An unexpected I/O exception occurred whilst reading the biospecimen data file.");
		}
		catch (Exception ex) {
			uploadReport.append("An unexpected exception occurred whilst reading the biospecimen data file.\n");
			log.error("processMatrixBiospecimenFile Exception stacktrace:", ex);
			throw new ArkSystemException("An unexpected exception occurred occurred when trying to process biospecimen data file.");
		}
		finally {
			// Clean up the IO objects
			timer.stop();
			uploadReport.append("\n");
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
		uploadReport.append("Processed ");
		uploadReport.append(recordCount);
		uploadReport.append(" records.");
		uploadReport.append("\n");
		uploadReport.append("Inserted ");
		uploadReport.append(insertCount);
		uploadReport.append(" records.");
		uploadReport.append("\n");
		uploadReport.append("Updated ");
		uploadReport.append(updateCount);
		uploadReport.append(" records.");
		uploadReport.append("\n");

		// Batch insert/update
		iLimsService.batchInsertBiospecimensAndUpdateInventoryCell(insertBiospecimens);
		return uploadReport;
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