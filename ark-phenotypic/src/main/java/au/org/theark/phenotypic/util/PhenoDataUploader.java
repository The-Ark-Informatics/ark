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

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.pheno.entity.PhenoDataSetCollection;
import au.org.theark.core.model.pheno.entity.PhenoDataSetData;
import au.org.theark.core.model.pheno.entity.PhenoDataSetField;
import au.org.theark.core.model.pheno.entity.PhenoDataSetFieldDisplay;
import au.org.theark.core.model.pheno.entity.PhenoDataSetGroup;
import au.org.theark.core.model.pheno.entity.QuestionnaireStatus;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.util.XLStoCSV;
import au.org.theark.phenotypic.service.IPhenotypicService;

import com.csvreader.CsvReader;

/**
 * SubjectUploader provides support for uploading subject matrix-formatted files.
 * 
 * @author tendersby
 */
public class PhenoDataUploader {
	private char						delimiterCharacter	= Constants.DEFAULT_DELIMITER_CHARACTER;
	private Study						study;
	static  Logger						log						= LoggerFactory.getLogger(PhenoDataUploader.class);
	private IArkCommonService		iArkCommonService		= null;
	private IPhenotypicService			iPhenotypicService			= null;
	private StringBuffer				uploadReport			= null;
	private SimpleDateFormat		simpleDateFormat		= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

	/**
	 * SubjectUploader constructor
	 * 
	 * @param study
	 *           study identifier in context
	 * @param iArkCommonService
	 *           common ARK service to perform select/insert/updates to the database
	 * @param iPhenotypicService
	 *           study service to perform select/insert/updates to the study database
	 */
	public PhenoDataUploader(Study study, IArkCommonService iArkCommonService, IPhenotypicService iPhenotypicService) {
		this.study = study;
		this.iArkCommonService = iArkCommonService;
		this.iPhenotypicService = iPhenotypicService;
		simpleDateFormat.setLenient(false);
	}
	
	/**
	 * Assumes a UID must be unique as this is only looking for a listOfSubjects PRE FILTERED based on a studies list of subjects to be changed
	 */
	public LinkSubjectStudy getSubjectByUIDFromExistList(List<LinkSubjectStudy> listOfSubjects, String subjectUID){
		for(LinkSubjectStudy potentialSubject : listOfSubjects){
			if(potentialSubject.getSubjectUID().equals(subjectUID)){
				return potentialSubject;
			}
		}
		return null;
	}

	public StringBuffer uploadAndReportCustomDataFile(InputStream inputStream, long size, String fileFormat, char delimChar, List<String> listOfUIDsToUpdate, PhenoDataSetGroup phenoDataSetGroup, PhenoDataSetCollection phenoCollection, boolean overwriteExisting) throws FileFormatException, ArkSystemException {
		List<PhenoDataSetCollection> phenoCollectionsWithTheirDataToInsert = new ArrayList<PhenoDataSetCollection>();
		
		delimiterCharacter = delimChar;
		uploadReport = new StringBuffer();
		
		InputStream convertedInputStream;
		if(fileFormat.equalsIgnoreCase(Constants.FileFormat.XLS.toString())){
			XLStoCSV xlsToCSV = new XLStoCSV(delimiterCharacter);
			convertedInputStream = xlsToCSV.convertXlsInputStreamToCsv(inputStream);
		}
		else {
			convertedInputStream = inputStream;
		}

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		int subjectCount = 0;
		long updateFieldsCount = 0L;
		long insertFieldsCount = 0L;
		long emptyDataCount = 0L;
		try {
			inputStreamReader = new InputStreamReader(convertedInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			String[] stringLineArray;

			List<LinkSubjectStudy> allSubjectWhichWillBeUpdated = null;
			if(listOfUIDsToUpdate.size()>0){
				allSubjectWhichWillBeUpdated = iArkCommonService.getUniqueSubjectsWithTheseUIDs(study, listOfUIDsToUpdate);
			}
			else{
				allSubjectWhichWillBeUpdated 	= new ArrayList<LinkSubjectStudy>();
			}
			if (size <= 0) {
				uploadReport.append("ERROR:  The input size was not greater than 0. Actual length reported: ");
				uploadReport.append(size);
				uploadReport.append("\n");
				throw new FileFormatException("The input size was not greater than 0. Actual length reported: " + size);
			}

			csvReader.readHeaders();

			List<String> fieldNameCollection = Arrays.asList(csvReader.getHeaders());
			ArkFunction phenoCustomFieldArkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_DATA_DICTIONARY);

			//List<CustomFieldDisplay> cfdsThatWeNeed = iArkCommonService.getCustomFieldDisplaysIn(fieldNameCollection, study, phenoCustomFieldArkFunction, customFieldGroup);
			List<PhenoDataSetFieldDisplay> cfdsThatWeNeed = iPhenotypicService.getPhenoFieldDisplaysIn(fieldNameCollection, study, phenoCustomFieldArkFunction, phenoDataSetGroup);
			
			
			//Paul has requested - in pheno we only insert List<PhenoData> dataThatWeHave = iArkCommonService.getCustomFieldDataFor(cfdsThatWeNeed, allSubjectWhichWillBeUpdated);
			//read one line which contains potentially many custom fields
			QuestionnaireStatus uploadingStatus = iPhenotypicService.getPhenoCollectionStatusByName(Constants.PHENO_COLLECTION_STATUS_UPLOADED);
			
			while (csvReader.readRecord()){
				List<PhenoDataSetData> phenoDataToInsertForThisPhenoCollection = new ArrayList<PhenoDataSetData>();
				log.info("reading record " + subjectCount);				
				stringLineArray = csvReader.getValues();
				String subjectUID = stringLineArray[0];
				String recordDate = stringLineArray[1];
				Date recordDate_asDate = (recordDate.isEmpty() ? new Date() : simpleDateFormat.parse(recordDate));
				LinkSubjectStudy subject = getSubjectByUIDFromExistList(allSubjectWhichWillBeUpdated, subjectUID);
				//log.info("get subject from list");
				List<PhenoDataSetCollection> subjectExistingMatchingPhenoCollections = iPhenotypicService.getSubjectMatchingPhenoCollections(subject, phenoDataSetGroup, recordDate_asDate);
				PhenoDataSetCollection phenoCollectionIntoDB = new PhenoDataSetCollection();
				if(subjectExistingMatchingPhenoCollections.size() == 0 || !overwriteExisting) {
					phenoCollectionIntoDB.setDescription(phenoCollection.getDescription());
					phenoCollectionIntoDB.setLinkSubjectStudy(subject);
	//				phenoCollectionIntoDB.setName(phenoCollection.getName());
					phenoCollectionIntoDB.setQuestionnaire(phenoDataSetGroup);
					if(recordDate.isEmpty()) {
						phenoCollectionIntoDB.setRecordDate(new Date());
					} else {
						phenoCollectionIntoDB.setRecordDate(recordDate_asDate);
					}
					phenoCollectionIntoDB.setStatus(uploadingStatus); //TODO for this to be UPLOADED TYPE STATUS
				} else {
					if(subjectExistingMatchingPhenoCollections.size() == 1) {
						recordDate_asDate = (recordDate.isEmpty() ? new Date() : simpleDateFormat.parse(recordDate));
						phenoCollectionIntoDB = subjectExistingMatchingPhenoCollections.get(0);
					} else {
						subjectCount++;
						continue;
					}
				}
				
				for(PhenoDataSetFieldDisplay phenoFieldDisplay : cfdsThatWeNeed){	

					String theDataAsString = null;
					PhenoDataSetField  phenoDataSetField= phenoFieldDisplay.getPhenoDataSetField();
					
					if(csvReader.getIndex(phenoFieldDisplay.getPhenoDataSetField().getName())<0){
						for(String nameAsSeenInFile : fieldNameCollection){
							if(nameAsSeenInFile.equalsIgnoreCase(phenoFieldDisplay.getPhenoDataSetField().getName())){
								theDataAsString = csvReader.get(nameAsSeenInFile);
							}
						}
					}
					else{
						theDataAsString = csvReader.get(phenoFieldDisplay.getPhenoDataSetField().getName());
					}

					if(theDataAsString!=null && !theDataAsString.isEmpty()){
						PhenoDataSetData dataToInsert = new PhenoDataSetData();
						dataToInsert.setPhenoDataSetFieldDisplay(phenoFieldDisplay);
						//as much as i disagree...pheno data isn't tied to subject....pheno collection is dataToInsert.setLinkSubjectStudy(subject);
						setValue(phenoDataSetField, phenoFieldDisplay,  dataToInsert, theDataAsString);
						boolean flag = true;
						for(PhenoDataSetData phenoData : phenoCollectionIntoDB.getPhenoDataSetData()) {
							if(phenoData.getPhenoDataSetFieldDisplay().getId() == phenoFieldDisplay.getId()) {
								phenoData.setDateDataValue(dataToInsert.getDateDataValue());
								phenoData.setErrorDataValue(dataToInsert.getErrorDataValue());
								phenoData.setNumberDataValue(dataToInsert.getNumberDataValue());
								phenoData.setTextDataValue(dataToInsert.getTextDataValue());
								flag = false;
								break;
							}
						}
						if(flag) {
							phenoDataToInsertForThisPhenoCollection.add(dataToInsert);
						}
						insertFieldsCount++;
					}
					else
					{
						emptyDataCount++;
					}
				}
				phenoCollectionIntoDB.getPhenoDataSetData().addAll(phenoDataToInsertForThisPhenoCollection);
				log.info(phenoCollectionIntoDB.toString());
				phenoCollectionsWithTheirDataToInsert.add(phenoCollectionIntoDB);
				subjectCount ++;
			}
			log.info("finished message for " + subjectCount + "\n      DATA inserts = " + insertFieldsCount + "  phenocollections = " + 
					phenoCollectionsWithTheirDataToInsert.size() + "  amount of empty scells =" + emptyDataCount );
		}
		catch (IOException ioe) {
			uploadReport.append("SYSTEM ERROR:   Unexpected I/O exception whilst reading the subject data file\n");
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex) {
			uploadReport.append("SYSTEM ERROR:   Unexpected exception whilst reading the subject data file\n");
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file");
		}
		finally {
			uploadReport.append("Total file size: ");
			uploadReport.append(decimalFormat.format(size / 1024.0 / 1024.0));
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

		}
		uploadReport.append("Processed ");
		uploadReport.append(subjectCount);
		uploadReport.append(" rows.");
		uploadReport.append("\n");
		uploadReport.append("Inserted ");
		uploadReport.append(insertFieldsCount);
		uploadReport.append(" rows of data.");
		uploadReport.append("\n");
		uploadReport.append("Updated ");
		uploadReport.append(updateFieldsCount);
		uploadReport.append(" rows of data.");
		uploadReport.append("\n");

		//TODO better exceptionhandling
		iPhenotypicService.processPhenoCollectionsWithTheirDataToInsertBatch(phenoCollectionsWithTheirDataToInsert, study);
		return uploadReport;
	}

	private PhenoDataSetData setValue(PhenoDataSetField phenoDataSetField, PhenoDataSetFieldDisplay phoDataSetFieldDisplay, PhenoDataSetData data, String theDataAsString){
//		log.warn("cf=" + customField + "\ndata=" + data+ "dataAsString=" + theDataAsString);
		
		if (phenoDataSetField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
			data.setNumberDataValue(new Double(theDataAsString));
		}
		else if (phenoDataSetField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
			DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
			Date dateFieldValue;
			try {
				dateFieldValue = dateFormat.parse(theDataAsString);
				data.setDateDataValue(dateFieldValue);
			}
			catch (ParseException e) {
				data.setErrorDataValue(theDataAsString);
			}
		}
		else if(phenoDataSetField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
			if(phenoDataSetField.getEncodedValues()!=null && !phenoDataSetField.getEncodedValues().isEmpty() 
					&& phoDataSetFieldDisplay.getAllowMultiselect()){
				if(theDataAsString != null){
					theDataAsString = theDataAsString.replaceAll(" ", ";");
				}
			}
			data.setTextDataValue(theDataAsString);
		}
		return data;
	}
	
	/*private PhenoData getCustomFieldFromList(List<PhenoData> dataThatWeHave, String subjectUID, CustomFieldDisplay cfd) {
		for(PhenoData data : dataThatWeHave){
																//TODO ASAP return to ignores case?
			if(data.getLinkSubjectStudy().getSubjectUID().equals(subjectUID) &&
					data.getCustomFieldDisplay().getId().equals(cfd.getId())){
				dataThatWeHave.remove(data); //TODO test this:  but it seems to have drastically reduced the exponential nature of our upload by the final lookups becoming faster rather than exponentially slower
				return data;
			}
		}
		return null;
	}*/

}
