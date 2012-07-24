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

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.EntityNotFoundException;
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
import au.org.theark.lims.model.vo.LimsVO;
import au.org.theark.lims.service.IInventoryService;
import au.org.theark.lims.service.ILimsService;

import com.csvreader.CsvReader;

/**
 * BiospecimenUploader provides support for uploading biospecimen matrix-formatted files. It features state-machine behaviour to allow an external class to
 * deal with how to store the data pulled out of the files.
 * 
 * @author cellis
 */
public class BiospecimenUploader {
	private long					recordCount;
	private long					insertCount;
	private long					updateCount;
	private long					curPos;
	private long					srcLength				= -1;
	private StopWatch				timer						= null;
	private char					delimiterCharacter	= Constants.DEFAULT_DELIMITER_CHARACTER;
	private Study					study;
	static Logger					log						= LoggerFactory.getLogger(BiospecimenUploader.class);
	@SuppressWarnings("unchecked")
	private IArkCommonService		iArkCommonService		= null;
	private ILimsService			iLimsService			= null;
	private IInventoryService		iInventoryService		= null;
	private StringBuffer			uploadReport			= null;
	private SimpleDateFormat		simpleDateFormat		= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private List<Biospecimen>		insertBiospecimens	= new ArrayList<Biospecimen>();
	private List<Biospecimen>		updateBiospecimens	= new ArrayList<Biospecimen>();
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
	public BiospecimenUploader(Study study, IArkCommonService iArkCommonService, ILimsService iLimsService, IInventoryService iInventoryService) {
		this.study = study;
		this.iArkCommonService = iArkCommonService;
		this.iLimsService = iLimsService;
		this.iInventoryService = iInventoryService;
		simpleDateFormat.setLenient(false);
	}

	/**
	 * Imports the subject data file to the database tables, and creates report on the process Assumes the file is in the default "matrix" file format:
	 * SUBJECTUID,FIELD1,FIELD2,FIELDN... 1,01/01/1900,99.99,99.99,, ...
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

			// Set field list (note 2th column to Nth column)
			// SUBJECTUID BIOSPECIMENUID F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				log.info("At record: " + recordCount);
				String subjectUID = csvReader.get("SUBJECTUID");
				String biospecimenUID = csvReader.get("BIOSPECIMENUID");

				LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
				//linkSubjectStudy.setStudy(study);

				try {
					linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUID, study);
				}
				catch (EntityNotFoundException enf) {
					log.error("\n\n\n\n\n\n\n\n\n\n\n\nUnexpected subject?   ashouldnt happen");
					// New subject
					linkSubjectStudy.setSubjectUID(subjectUID);
					linkSubjectStudy.setStudy(study);
				}

				Biospecimen biospecimen = iLimsService.getBiospecimenByUid(biospecimenUID);
				if(biospecimen == null) {
					biospecimen = new Biospecimen();
				}
				biospecimen.setLinkSubjectStudy(linkSubjectStudy);

				if (csvReader.getIndex("BIOSPECIMENUID") > 0) {
					biospecimen.setBiospecimenUid(csvReader.get("BIOSPECIMENUID"));
				}
				
				if (csvReader.getIndex("BIOCOLLECTION") > 0) {
					String name = csvReader.get("BIOCOLLECTION");
					BioCollection bioCollection = iLimsService.getBioCollectionByName(name);
					if(bioCollection == null){
						bioCollection = new BioCollection();
						bioCollection.setLinkSubjectStudy(linkSubjectStudy);
						bioCollection.setStudy(study);
						bioCollection = iLimsService.createBioCollection(bioCollection);
						biospecimen.setBioCollection(bioCollection);
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
				
				if (csvReader.getIndex("UNITS") > 0) {
					String name = csvReader.get("UNITS");
					Unit unit = iLimsService.getUnitByName(name);
					biospecimen.setUnit(unit);
				}

				if (csvReader.getIndex("TREATMENT") > 0) {
					String name = csvReader.get("TREATMENT");
					TreatmentType treatmentType = new TreatmentType(); 
					treatmentType = iLimsService.getTreatmentTypeByName(name);
					biospecimen.setTreatmentType(treatmentType);
				}
				
				if (biospecimen.getBiospecimenUid() == null || biospecimen.getBiospecimenUid().isEmpty()) {
					biospecimen.setStudy(study);
					
					Set<BioTransaction> bioTransactions = new HashSet<BioTransaction>(0);
					
					// Inheriently create a transaction for the initial quantity
					BioTransaction bioTransaction = new BioTransaction();
					bioTransaction.setBiospecimen(biospecimen);
					bioTransaction.setTransactionDate(Calendar.getInstance().getTime());
					bioTransaction.setQuantity(biospecimen.getQuantity());
					bioTransaction.setReason(au.org.theark.lims.web.Constants.BIOTRANSACTION_STATUS_INITIAL_QUANTITY);
					
					BioTransactionStatus initialStatus = iLimsService.getBioTransactionStatusByName(au.org.theark.lims.web.Constants.BIOTRANSACTION_STATUS_INITIAL_QUANTITY);
					bioTransaction.setStatus(initialStatus);	//ensure that the initial transaction can be identified
					bioTransactions.add(bioTransaction);
					biospecimen.setBioTransactions(bioTransactions);
					biospecimen.setBiospecimenUid(iLimsService.getNextGeneratedBiospecimenUID(study));
					insertBiospecimens.add(biospecimen);
					StringBuffer sb = new StringBuffer();
					sb.append("Biospecimen UID: ");
					sb.append(biospecimen.getBiospecimenUid());
					sb.append(" has been created successfully.");
					sb.append("\n");
					uploadReport.append(sb);
					insertCount++;
				}
				else {
					updateBiospecimens.add(biospecimen);
					StringBuffer sb = new StringBuffer();
					sb.append("Biospecimen UID: ");
					sb.append(biospecimen.getBiospecimenUid());
					sb.append(" has been updated successfully.");
					sb.append("\n");
					uploadReport.append(sb);
					updateCount++;
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
				
				invCell = new InvCell();
				invCell = iInventoryService.getInvCellByLocationNames(siteName, freezerName, rackName, boxName, row, column);
				//TODO : null checking here.  should be picked up ikn validation  JIRA 657 Created  log.info("invcell null?" + (invCell == null));

				if(invCell != null && invCell.getId() != null) {
					invCell.setBiospecimen(biospecimen);
					updateInvCells.add(invCell);
				}
				
				log.debug("\n");
				recordCount++;
			}
		}
		catch (IOException ioe) {
			uploadReport.append("Unexpected I/O exception whilst reading the biospecimen data file\n");
			log.error("processMatrixBiospecimenFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the biospecimen data file");
		}
		catch (Exception ex) {
			uploadReport.append("Unexpected exception whilst reading the biospecimen data file\n");
			log.error("processMatrixBiospecimenFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process biospecimen data file");
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
		iLimsService.batchInsertBiospecimens(insertBiospecimens);
		iLimsService.batchUpdateBiospecimens(updateBiospecimens);
		iLimsService.batchUpdateInvCells(updateInvCells);
		
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