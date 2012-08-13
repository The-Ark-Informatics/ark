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
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.Address;
import au.org.theark.core.model.study.entity.AddressStatus;
import au.org.theark.core.model.study.entity.AddressType;
import au.org.theark.core.model.study.entity.ArkFunction;
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.Country;
import au.org.theark.core.model.study.entity.CustomField;
import au.org.theark.core.model.study.entity.CustomFieldDisplay;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Phone;
import au.org.theark.core.model.study.entity.PhoneStatus;
import au.org.theark.core.model.study.entity.PhoneType;
import au.org.theark.core.model.study.entity.State;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectCustomFieldData;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.lims.service.ILimsService;

import com.csvreader.CsvReader;

/**
 * SubjectUploader provides support for uploading subject matrix-formatted files.
 * 
 * @author cellis
 */
public class DataUploader {
	private char						delimiterCharacter	= Constants.DEFAULT_DELIMITER_CHARACTER;
	private Study						study;
	static  Logger						log						= LoggerFactory.getLogger(DataUploader.class);
	private IArkCommonService		iArkCommonService		= null;
	private ILimsService			iLimsService			= null;
	private StringBuffer				uploadReport			= null;
	private SimpleDateFormat		simpleDateFormat		= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);

	/**
	 * SubjectUploader constructor
	 * 
	 * @param study
	 *           study identifier in context
	 * @param iArkCommonService
	 *           common ARK service to perform select/insert/updates to the database
	 * @param iStudyService
	 *           study service to perform select/insert/updates to the study database
	 */
	public DataUploader(Study study, IArkCommonService iArkCommonService, ILimsService iLimsService) {
		this.study = study;
		this.iArkCommonService = iArkCommonService;
		this.iLimsService = iLimsService;
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
	
	 /** Imports the subject data file to the database tables, and creates report on the process Assumes the file is in the default "matrix" file format:
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
		 **/
	public StringBuffer uploadAndReportMatrixSubjectFile(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr, List<String> uidsWhichNeedUpdating) throws FileFormatException, ArkSystemException {
		
		return new StringBuffer();//uploadReport;
	}

	private State findStateWithinThisCountry(String stateString, Country country) {
		if(stateString!=null && !StringUtils.isBlank(stateString)){
			for(State state : country.getStates()){
				if(state.getName().equalsIgnoreCase(stateString)){
					return state;
				}
			}
		}
		return null;
	}

	private Country findCountry(List<Country> countriesPossible, String countryString) {
		if(countryString!=null && !StringUtils.isBlank(countryString)){
			for(Country country : countriesPossible){
				if(country.getName().equalsIgnoreCase(countryString)){
					return country;
				}
			}
		}
		return null;
	}

	private AddressStatus findAddressStatusOrSetDefault(List<AddressStatus> StatiiAlreadyExisting, AddressStatus defaultAddressStatus, String stringRepresentingTheStatusWeWant) {
		if(stringRepresentingTheStatusWeWant!=null && !StringUtils.isBlank(stringRepresentingTheStatusWeWant)){
			for(AddressStatus nextStatus : StatiiAlreadyExisting){
				if(nextStatus.getName().equalsIgnoreCase(stringRepresentingTheStatusWeWant)){
					return nextStatus;
				}
			}
		}
		return defaultAddressStatus;
	}


	private AddressType findAddressTypeOrSetDefault(List<AddressType> typesAlreadyExisting, AddressType defaultAddressType, String stringRepresentingTheTypeWeWant) {
		if(stringRepresentingTheTypeWeWant!=null && !StringUtils.isBlank(stringRepresentingTheTypeWeWant)){
			for(AddressType nextType : typesAlreadyExisting){
				if(nextType.getName().equalsIgnoreCase(stringRepresentingTheTypeWeWant)){
					return nextType;
				}
			}
		}
		return defaultAddressType;
	}


	private PhoneStatus findPhoneStatusOrSetDefault(List<PhoneStatus> StatiiAlreadyExisting, PhoneStatus defaultPhoneStatus, String stringRepresentingTheStatusWeWant) {
		if(stringRepresentingTheStatusWeWant!=null && !StringUtils.isBlank(stringRepresentingTheStatusWeWant)){
			for(PhoneStatus nextStatus : StatiiAlreadyExisting){
				if(nextStatus.getName().equalsIgnoreCase(stringRepresentingTheStatusWeWant)){
					return nextStatus;
				}
			}
		}
		return defaultPhoneStatus;
	}


	private PhoneType findPhoneTypeOrSetDefault(List<PhoneType> typesAlreadyExisting, PhoneType defaultPhoneType, String stringRepresentingTheTypeWeWant) {
		if(stringRepresentingTheTypeWeWant!=null && !StringUtils.isBlank(stringRepresentingTheTypeWeWant)){
			for(PhoneType nextType : typesAlreadyExisting){
				if(nextType.getName().equalsIgnoreCase(stringRepresentingTheTypeWeWant)){
					return nextType;
				}
			}
		}
		return defaultPhoneType;
	}

	public StringBuffer uploadAndReportCustomDataFile(InputStream inputStream, long size, String fileFormat, char delimChar, List<String> listOfUIDsToUpdate) throws FileFormatException, ArkSystemException {
		List<SubjectCustomFieldData> customFieldsToUpdate = new ArrayList<SubjectCustomFieldData>();
		List<SubjectCustomFieldData> customFieldsToInsert = new ArrayList<SubjectCustomFieldData>();
		delimiterCharacter = delimChar;
		uploadReport = new StringBuffer();

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		int subjectCount = 0;
		long updateFieldsCount = 0L;
		long insertFieldsCount = 0L;
		long emptyDataCount = 0L;
		try {
			inputStreamReader = new InputStreamReader(inputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			String[] stringLineArray;

			List<LinkSubjectStudy> allSubjectWhichWillBeUpdated = null;
			if(listOfUIDsToUpdate.size()>0){
				allSubjectWhichWillBeUpdated = iArkCommonService.getUniqueSubjectsWithTheseUIDs(study, listOfUIDsToUpdate);
			}
			else{
				allSubjectWhichWillBeUpdated 	= new ArrayList();
			}
			if (size <= 0) {
				uploadReport.append("The input size was not greater than 0. Actual length reported: ");
				uploadReport.append(size);
				uploadReport.append("\n");
				throw new FileFormatException("The input size was not greater than 0. Actual length reported: " + size);
			}

			csvReader.readHeaders();

			List<String> fieldNameCollection = Arrays.asList(csvReader.getHeaders());
			ArkFunction subjectCustomFieldArkFunction = iArkCommonService.getArkFunctionByName(Constants.FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD);//");

			List<CustomFieldDisplay> cfdsThatWeNeed = iArkCommonService.getCustomFieldDisplaysIn(fieldNameCollection, study, subjectCustomFieldArkFunction);
			List<SubjectCustomFieldData> dataThatWeHave = iArkCommonService.getCustomFieldDataFor(cfdsThatWeNeed, allSubjectWhichWillBeUpdated);
			//read one line which contains potentially many custom fields
			while (csvReader.readRecord()) {
				log.info("reading record " + subjectCount);
				
				stringLineArray = csvReader.getValues();
				String subjectUID = stringLineArray[0];
				LinkSubjectStudy subject = getSubjectByUIDFromExistList(allSubjectWhichWillBeUpdated, subjectUID);
				//log.info("get subject from list");
				CustomField customField = null;		
				for(CustomFieldDisplay cfd : cfdsThatWeNeed){					
					customField = cfd.getCustomField();
					//log.info("got customfield from cfd");
					SubjectCustomFieldData dataInDB = getCustomFieldFromList(dataThatWeHave, subjectUID, cfd);
					//log.info("got 'data in db' from cfd, subject and ALL data");
					String theDataAsString = csvReader.get(cfd.getCustomField().getName());
					//log.info("read data from file");
					
					if(theDataAsString!=null && !theDataAsString.isEmpty()){
						if(dataInDB != null){
							dataInDB = setValue(customField, dataInDB, theDataAsString);	
							//log.info("have set value to entity");
							customFieldsToUpdate.add(dataInDB);
							//log.info("added entity to list");
							updateFieldsCount++;
						}
						else{
							SubjectCustomFieldData dataToInsert = new SubjectCustomFieldData();
							dataToInsert.setCustomFieldDisplay(cfd);
							dataToInsert.setLinkSubjectStudy(subject);
							setValue(customField, dataToInsert, theDataAsString);	
							customFieldsToInsert.add(dataToInsert);
							insertFieldsCount++;
						}
					}
					else
					{
						emptyDataCount++;
					}
				}
				
				subjectCount ++;
			}
			log.info("finished message for " + subjectCount + "         updates= " + updateFieldsCount + " or \ncustomFieldsToupdate.size=" + customFieldsToUpdate.size() +
					"\n     inserts = " + insertFieldsCount + "  or  \ncustomFieldsToInsert.size = " + customFieldsToInsert.size() + "   amount of empty scells =" + emptyDataCount );
		}
		catch (IOException ioe) {
			uploadReport.append("Unexpected I/O exception whilst reading the subject data file\n");
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex) {
			uploadReport.append("Unexpected exception whilst reading the subject data file\n");
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
		uploadReport.append(" subjects.");
		uploadReport.append("\n");
		uploadReport.append("Updated ");
		uploadReport.append(updateFieldsCount);
		uploadReport.append(" subjects.");
		uploadReport.append("\n");

		//TODO better exceptionhandling
		//
		//
		
		
		
		
		
		
		
//		TODO ASAP
		
		//iStudyService.processFieldsBatch(customFieldsToUpdate, study, customFieldsToInsert);
		
		
		
		
		
		
		
		
		
		return uploadReport;
	}

	private SubjectCustomFieldData setValue(CustomField customField, SubjectCustomFieldData data, String theDataAsString){
//		log.warn("cf=" + customField + "\ndata=" + data+ "dataAsString=" + theDataAsString);
		
		if (customField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER)) {
			data.setNumberDataValue(new Double(theDataAsString));
		}
		else if (customField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)) {
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
		else if(customField.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER)) {
			data.setTextDataValue(theDataAsString);
		}
		return data;
	}
	
	private SubjectCustomFieldData getCustomFieldFromList(List<SubjectCustomFieldData> dataThatWeHave, String subjectUID, CustomFieldDisplay cfd) {
		for(SubjectCustomFieldData data : dataThatWeHave){
																//TODO ASAP return to ignores case?
			if(data.getLinkSubjectStudy().getSubjectUID().equals(subjectUID) &&
					data.getCustomFieldDisplay().getId().equals(cfd.getId())){
				dataThatWeHave.remove(data); //TODO test this:  but it seems to have drastically reduced the exponential nature of our upload by the final lookups becoming faster rather than exponentially slower
				return data;
			}
		}
		return null;
	}

}
