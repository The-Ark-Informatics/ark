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
import au.org.theark.study.service.IStudyService;

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
	private IStudyService			iStudyService			= null;
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
	public DataUploader(Study study, IArkCommonService iArkCommonService, IStudyService iStudyService) {
		this.study = study;
		this.iArkCommonService = iArkCommonService;
		this.iStudyService = iStudyService;
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
		List<LinkSubjectStudy>	insertSubjects			= new ArrayList<LinkSubjectStudy>();
		List<LinkSubjectStudy>	updateSubjects			= new ArrayList<LinkSubjectStudy>();
		long						subjectCount = 0;
		long						insertCount = 0;
		long						updateCount = 0;
		long						srcLength = -1;	// -1 means nothing being processed
		delimiterCharacter = inDelimChr;
		uploadReport = new StringBuffer();

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			String[] stringLineArray;

			//this is a list of all our somewhat enum like ref tables...
			//much better to call this once than each one n times in the for loop...plus each ones default is n times
			//should save 200,000-250,000 selects for a 17K insert.  may still wish to evaluate whats best here
			Collection<MaritalStatus> maritalStatiiPossible = iArkCommonService.getMaritalStatus();
			Collection<SubjectStatus> subjectStatiiPossible = iArkCommonService.getSubjectStatus();
			Collection<GenderType> genderTypesPossible = iArkCommonService.getGenderTypes();
			Collection<TitleType> titleTypesPossible = iArkCommonService.getTitleType();
			List<AddressType> addressTypesPossible = iArkCommonService.getAddressTypes();
			List<AddressStatus> addressStatiiPossible = iArkCommonService.getAddressStatuses();
			List<PhoneType> phoneTypesPossible = iArkCommonService.getPhoneTypes();
			List<PhoneStatus> phoneStatiiPossible = iArkCommonService.getPhoneStatuses();
			List<Country> countriesPossible = iArkCommonService.getCountries();
			//List<State> statesPossible = iArkCommonService.getStates(country);
			
			Collection<VitalStatus> vitalStatiiPossible = iArkCommonService.getVitalStatus();
			Collection<PersonContactMethod> personContactMethodPossible = iArkCommonService.getPersonContactMethodList();
			//Collection<MaritalStatus> yesNoList = iArkCommonService.getYesNoList(); //TODO would boolean not be better?

			boolean autoConsent = study.getAutoConsent();
			SubjectStatus defaultSubjectStatus = iStudyService.getDefaultSubjectStatus();
			TitleType defaultTitleType = iStudyService.getDefaultTitleType();
			AddressType defaultAddressType = iStudyService.getDefaultAddressType();
			AddressStatus defaultAddressStatus = iStudyService.getDefaultAddressStatus();
			PhoneType defaultPhoneType = iStudyService.getDefaultPhoneType();
			PhoneStatus defaultPhoneStatus = iStudyService.getDefaultPhoneStatus();
			GenderType defaultGenderType = iStudyService.getDefaultGenderType();
			VitalStatus defaultVitalStatus = iStudyService.getDefaultVitalStatus();
			MaritalStatus defaultMaritalStatus = iStudyService.getDefaultMaritalStatus();
			ConsentOption concentOptionOfYes = iStudyService.getConsentOptionForBoolean(true);//sounds a lot like boolean blah = true????
			ConsentStatus consentStatusOfConsented = iStudyService.getConsentStatusByName("Consented");
			ConsentType consentTypeOfElectronic = iStudyService.getConsentTypeByName("Electronic");

			List<LinkSubjectStudy> allSubjectWhichWillBeUpdated = null;
			if(uidsWhichNeedUpdating.size()>0){
				//TODO analyse performance of bringing all back and having to iterate everytime, vs conditional query + looping through less
				// TODO analyze performance of getting that big list of UIDs and doing a .contains(subjectuid)   VS    getting all the entities and doing a .getSubjectUID.equals(subjectUID)
				allSubjectWhichWillBeUpdated = iArkCommonService.getUniqueSubjectsWithTheseUIDs(study, uidsWhichNeedUpdating);
			}
			else{
				allSubjectWhichWillBeUpdated 	= new ArrayList();
			}
			
			srcLength = inLength;
			if (srcLength <= 0) {
				uploadReport.append("The input size was not greater than 0. Actual length reported: ");
				uploadReport.append(srcLength);
				uploadReport.append("\n");
				throw new FileFormatException("The input size was not greater than 0. Actual length reported: " + srcLength);
			}

			csvReader.readHeaders();
			srcLength = inLength - csvReader.getHeaders().toString().length();
			int firstNameIndex 		= csvReader.getIndex("FIRST_NAME");
			int middleNameIndex 		= csvReader.getIndex("MIDDLE_NAME");
			int lastNameIndex 		= csvReader.getIndex("LAST_NAME");
			int preferredNameIndex 	= csvReader.getIndex("PREFERRED_NAME");
			int preferredEmailIndex = csvReader.getIndex("PREFERRED_EMAIL");
			int otherEmailIndex 		= csvReader.getIndex("OTHER_EMAIL");
			int titleIndex 			= csvReader.getIndex("TITLE");
			int vitalStatusIndex		= csvReader.getIndex("VITAL_STATUS");
			int maritalStatusIndex 	= csvReader.getIndex("MARITAL_STATUS");
			int statusIndex 			= csvReader.getIndex("STATUS");
 
				int addressLine1Index			= csvReader.getIndex("ADDRESS_LINE_1");
			int addressLine2Index			= csvReader.getIndex("ADDRESS_LINE_2");
			int suburbIndex					= csvReader.getIndex("SUBURB");
			int stateIndex						= csvReader.getIndex("STATE");
			int countryIndex					= csvReader.getIndex("COUNTRY");
			int postCodeIndex					= csvReader.getIndex("POST_CODE");
			int addressSourceIndex			= csvReader.getIndex("ADDRESS_SOURCE");
			int addressStatusIndex			= csvReader.getIndex("ADDRESS_STATUS");
			int addressTypeIndex				= csvReader.getIndex("ADDRESS_TYPE");
			int addressReceivedIndex		= csvReader.getIndex("ADDRESS_DATE_RECEIVED");
			int addressCommentsIndex		= csvReader.getIndex("ADDRESS_COMMENTS");
			int isPreferredMailingIndex	= csvReader.getIndex("IS_PREFERRED_MAILING_ADDRESS");
			

			int phoneNumberIndex				= csvReader.getIndex("PHONE_NUMBER");
			int areaCodeIndex					= csvReader.getIndex("PHONE_AREA_CODE");
			int phoneStatusIndex				= csvReader.getIndex("PHONE_STATUS");
			int phoneTypeIndex				= csvReader.getIndex("PHONE_TYPE");
			int phoneSourceIndex				= csvReader.getIndex("PHONE_SOURCE");
			int phoneCommentsIndex			= csvReader.getIndex("PHONE_COMMENTS");
			int phoneDateReceivedIndex		= csvReader.getIndex("PHONE_DATE_RECEIVED");
			
			//if(PERSON_CONTACT_METHOD is in headers, use it, 
								//else, if CONTACT_METHOD, us IT, else, just set to -1 
			int personContactIndex 	= ((csvReader.getIndex("PERSON_CONTACT_METHOD")>0)?csvReader.getIndex("PERSON_CONTACT_METHOD"):
											((csvReader.getIndex("CONTACT_METHOD") > 0)?csvReader.getIndex("CONTACT_METHOD"):-1));
			int dateOfBirthIndex 				= ((csvReader.getIndex("DATE_OF_BIRTH")>0)?csvReader.getIndex("DATE_OF_BIRTH"):
				((csvReader.getIndex("DOB") > 0)?csvReader.getIndex("DOB"):-1));
			int dateOfDeathIndex 				= ((csvReader.getIndex("DATE_OF_DEATH")>0)?csvReader.getIndex("DATE_OF_DEATH"):
				((csvReader.getIndex("DODEATH") > 0)?csvReader.getIndex("DODEATH"):-1));
			int causeOfDeathIndex 				= ((csvReader.getIndex("CAUSE_OF_DEATH")>0)?csvReader.getIndex("CAUSE_OF_DEATH"):
				((csvReader.getIndex("CODEATH") > 0)?csvReader.getIndex("CODEATH"):-1));			
			//in reality, validation doesnt permit this yet anyway...but probably not bad to align it over in validation
			int genderIndex 			= ( (csvReader.getIndex("GENDER_TYPE") > 0) ? csvReader.getIndex("GENDER_TYPE") :
												((csvReader.getIndex("GENDER") > 0) ? csvReader.getIndex("GENDER"): 
												((csvReader.getIndex("SEX") > 0) ? csvReader.getIndex("SEX"):-1)));   

			while (csvReader.readRecord()) {
				LinkSubjectStudy subject = null;
				stringLineArray = csvReader.getValues();
				String subjectUID = stringLineArray[0];
				subject = getSubjectByUIDFromExistList(allSubjectWhichWillBeUpdated, subjectUID);
				boolean thisSubjectAlreadyExists = (subject!=null);

				Person person = null;
				if(thisSubjectAlreadyExists){
					person = subject.getPerson();
				}
				else{
					subject = new LinkSubjectStudy();
					subject.setSubjectUID(subjectUID);//note: this will be overwritten IF study.isautogenerate
					subject.setStudy(study);
					person = new Person();
				}
				
				if (firstNameIndex > 0)
					person.setFirstName(stringLineArray[firstNameIndex]);

				if (middleNameIndex > 0)
					person.setMiddleName(stringLineArray[middleNameIndex]);

				if (lastNameIndex > 0){
					String lastnameFromFile = stringLineArray[lastNameIndex];
					
					if (thisSubjectAlreadyExists  && lastnameFromFile!=null  && !lastnameFromFile.equalsIgnoreCase(person.getLastName()) ) {
						PersonLastnameHistory personLastNameHistory = new PersonLastnameHistory();
						personLastNameHistory.setPerson(person);
						personLastNameHistory.setLastName(person.getLastName());
						person.getPersonLastnameHistory().add(personLastNameHistory);//TODO analyze this
					}
					person.setLastName(lastnameFromFile);
				}
				if (preferredNameIndex > 0){
					person.setPreferredName(stringLineArray[preferredNameIndex]);
				}

				if(genderIndex>0){
					if (stringLineArray[genderIndex] != null && stringLineArray[genderIndex].length() > 0) {
						for(GenderType boygirl : genderTypesPossible){
							if(boygirl.getName().equalsIgnoreCase(stringLineArray[genderIndex])){
								person.setGenderType(boygirl);		
							}
						}
						if (person.getGenderType() == null || 
								StringUtils.isBlank(person.getGenderType().getName())) {
							person.setGenderType(defaultGenderType);
						}
					}	
				}

				if (dateOfBirthIndex > 0) {
					Date dateOfBirth = new Date();
					
					if (stringLineArray[dateOfBirthIndex] != null && stringLineArray[dateOfBirthIndex].length() > 0) {
						dateOfBirth = simpleDateFormat.parse(stringLineArray[dateOfBirthIndex]);
						person.setDateOfBirth(dateOfBirth);
					}
				}

				if (dateOfDeathIndex > 0) {
					Date dateOfDeath = new Date();
					if (stringLineArray[dateOfDeathIndex] != null && stringLineArray[dateOfDeathIndex].length() > 0) {
						dateOfDeath = simpleDateFormat.parse(stringLineArray[dateOfDeathIndex]);
						person.setDateOfDeath(dateOfDeath);
					}
				}

				if (causeOfDeathIndex > 0) {
					if (stringLineArray[causeOfDeathIndex] != null && stringLineArray[causeOfDeathIndex].length() > 0) {
						person.setCauseOfDeath(stringLineArray[causeOfDeathIndex]);
					}
				}

				if (vitalStatusIndex > 0) {
					String vitalStatusStr = (stringLineArray[vitalStatusIndex]);
					for(VitalStatus vitalStat : vitalStatiiPossible){
						if(vitalStat.getName().equalsIgnoreCase(vitalStatusStr)){
							person.setVitalStatus(vitalStat);		
						}	
					}					
					if (person.getVitalStatus() == null || StringUtils.isBlank(person.getVitalStatus().getName())) {
						person.setVitalStatus(defaultVitalStatus);
					}
				}

				if (preferredEmailIndex > 0) {
					person.setPreferredEmail(stringLineArray[preferredEmailIndex]);
				}

				if (otherEmailIndex > 0) {
					person.setPreferredEmail(stringLineArray[otherEmailIndex]);
				}

				if (titleIndex > 0) {
					String titleStr = (stringLineArray[titleIndex]);
					for(TitleType titleType : titleTypesPossible){
						if(titleType.getName().equalsIgnoreCase(titleStr)){
							person.setTitleType(titleType);		
						}
					}
					if (person.getTitleType() == null || StringUtils.isBlank(person.getTitleType().getName())) {
						person.setTitleType(defaultTitleType);
					}
				}
				
				if (maritalStatusIndex > 0) {
					String maritalStatusStr = (stringLineArray[maritalStatusIndex]);
					for(MaritalStatus maritalStat : maritalStatiiPossible){
						if(maritalStat.getName().equalsIgnoreCase(maritalStatusStr)){
							person.setMaritalStatus(maritalStat);		
						}
					}
					if (person.getMaritalStatus() == null || 
							StringUtils.isBlank(person.getMaritalStatus().getName())) {
						person.setMaritalStatus(defaultMaritalStatus);
					}
				}

				if (personContactIndex > 0) {
					String personContactMethodStr = null;
					personContactMethodStr = (stringLineArray[personContactIndex]);				
					for(PersonContactMethod possibleMethod : personContactMethodPossible){
						if(possibleMethod.getName().equalsIgnoreCase(personContactMethodStr)){
							person.setPersonContactMethod(possibleMethod);		
						}	
					}//TODO if we get to the end and personcontactmethod doesnt exist...what do we do?  do we want a default or does it get ignored
				}

				if (statusIndex > 0) {
					String statusStr = (stringLineArray[statusIndex]);
					for(SubjectStatus subjectStat : subjectStatiiPossible){
						if(subjectStat.getName().equalsIgnoreCase(statusStr)){
							subject.setSubjectStatus(subjectStat);		
						}
					}
					if (subject.getSubjectStatus() == null || StringUtils.isBlank(subject.getSubjectStatus().getName())) {
						subject.setSubjectStatus(defaultSubjectStatus);
					}
					//if the study is autoconsent...then there are some defaults we have to set TODO get rid of hardcoding
					if(autoConsent && subject.getSubjectStatus().getName().equalsIgnoreCase("Subject")) {
						subject.setConsentDate(new Date());
						subject.setConsentStatus(consentStatusOfConsented);
						subject.setConsentType(consentTypeOfElectronic);

						ConsentOption defaultConsentOption = concentOptionOfYes;
						subject.setConsentToActiveContact(defaultConsentOption);
						subject.setConsentToPassiveDataGathering(defaultConsentOption);
						subject.setConsentToUseData(defaultConsentOption);
					}
				}
				
				//if no address info - ignore
				if(addressLine1Index>0 || addressLine1Index>0){
					boolean updateAddress = false;
					String address1String = stringLineArray[addressLine1Index];
					String address2String = stringLineArray[addressLine2Index];
					
					if(	(address1String == null || StringUtils.isBlank(address1String)) &&
							(address2String == null || StringUtils.isBlank(address2String)) ){
						//then lets just jump out as there is no address to validate.  lay down to user that they must have data if they want an update
					}
					else{
						Address addressToAttachToPerson = null;
						if(thisSubjectAlreadyExists){
							String typeString = null;
							String statusString = null;

							if (addressTypeIndex > 0)
								typeString = stringLineArray[addressTypeIndex];
							if (addressStatusIndex > 0)
								statusString = stringLineArray[addressStatusIndex];							
							
							for(Address a : person.getAddresses()){
								if(a.getAddressStatus().getName().equalsIgnoreCase(statusString) &&
									a.getAddressType().getName().equalsIgnoreCase(typeString)){
									addressToAttachToPerson = a;
									updateAddress = true;
								}
							}
						}
						else{
							addressToAttachToPerson = new Address();
						}
						
						AddressType type = findAddressTypeOrSetDefault(addressTypesPossible, defaultAddressType, stringLineArray[addressTypeIndex]);
						AddressStatus status = findAddressStatusOrSetDefault(addressStatiiPossible, defaultAddressStatus, stringLineArray[addressTypeIndex]);
						String addressComments = stringLineArray[addressCommentsIndex];
						String suburb = stringLineArray[suburbIndex];
						
						String countryString = stringLineArray[countryIndex];
						Country country = findCountry(countriesPossible, countryString);
						if(country!=null){
							addressToAttachToPerson.setCountry(country);
							String stateString = stringLineArray[stateIndex];
							//TODO one option: all possible states locally and test where it matches might work...or lets see how the entity goes first, and if it hits db again! 
							//State state = findState(statesPossible, stateString, country);
							State state = findStateWithinThisCountry(stateString, country);
							if(state==null){
								uploadReport.append("could not find a state named '" + stateString + "' in " + country.getName() + "\n");
								addressToAttachToPerson.setOtherState(stateString);
							}
							else{
								addressToAttachToPerson.setState(state);
							}
						}
						else{
							uploadReport.append("Could not find country '" + countryString + "'\n");
						}
						
						String postCode = stringLineArray[postCodeIndex];
						String addressSource = stringLineArray[addressSourceIndex];
						String dateReceivedString = stringLineArray[addressReceivedIndex];
						String isPreferredMailingString = stringLineArray[isPreferredMailingIndex];

						addressToAttachToPerson.setAddressType(type);
						addressToAttachToPerson.setAddressStatus(status);
						if(postCode!=null && !postCode.isEmpty())
							addressToAttachToPerson.setPostCode(postCode);
						if(address1String!=null && !address1String.isEmpty())
							addressToAttachToPerson.setAddressLineOne(address1String);
						if(address2String!=null && !address2String.isEmpty())
							addressToAttachToPerson.setStreetAddress(address2String);//yeah..
						if(dateReceivedString!=null && !dateReceivedString.isEmpty()){
							// TODO dateconvert and set
							Date dateReceived = new Date();
							dateReceived = simpleDateFormat.parse(dateReceivedString);
							addressToAttachToPerson.setDateReceived(dateReceived);
						}
						if(suburb!=null && !suburb.isEmpty())
							addressToAttachToPerson.setCity(suburb);
						if(addressComments!=null && !addressComments.isEmpty())
							addressToAttachToPerson.setComments(addressComments);
						if(isPreferredMailingString!=null && !isPreferredMailingString.isEmpty())
							addressToAttachToPerson.setPreferredMailingAddress(Boolean.valueOf(isPreferredMailingString));
						if(addressSource!=null && !addressSource.isEmpty())
							addressToAttachToPerson.setSource(addressSource);
						if(!updateAddress){
							//TODO check this works in all cases
							person.getAddresses().add(addressToAttachToPerson);
						}
					}							
					
				}

				//if no address info - ignore
				if(phoneNumberIndex  >0){
					boolean updatePhones= false;
					String phoneNumberString = stringLineArray[phoneNumberIndex];
					
					if(phoneNumberString == null || StringUtils.isBlank(phoneNumberString)){
						//then lets just jump out as there is no phone to validate.  lay down to user that they must have data if they want an update
					}
					else{
						Phone phoneToAttachToPerson = null;
						if(thisSubjectAlreadyExists){
							String typeString = null;
							String statusString = null;

							if (phoneTypeIndex > 0)
								typeString = stringLineArray[phoneTypeIndex];
							if (phoneStatusIndex > 0)
								statusString = stringLineArray[phoneStatusIndex];							
							
							for(Phone phone : person.getPhones()){
								if(phone.getPhoneStatus().getName().equalsIgnoreCase(statusString) &&
									phone.getPhoneType().getName().equalsIgnoreCase(typeString)){
									phoneToAttachToPerson = phone;
									updatePhones = true;
								}
							}
						}
						else{
							phoneToAttachToPerson = new Phone();
						}
						
						PhoneType type = findPhoneTypeOrSetDefault(phoneTypesPossible, defaultPhoneType, stringLineArray[phoneTypeIndex]);
						PhoneStatus status = findPhoneStatusOrSetDefault(phoneStatiiPossible, defaultPhoneStatus, stringLineArray[phoneTypeIndex]);
						String phoneComments = stringLineArray[phoneCommentsIndex];
													
						String areaCode = stringLineArray[areaCodeIndex];
						String phoneSource = stringLineArray[phoneSourceIndex];
						String phoneDateReceivedString = stringLineArray[phoneDateReceivedIndex];
						//log.warn("phone Date Reveived = " + phoneDateReceivedString + " for index = " +  phoneDateReceivedIndex);

						phoneToAttachToPerson.setPhoneType(type);
						phoneToAttachToPerson.setPhoneStatus(status);
						if(areaCode!=null && !areaCode.isEmpty())
							phoneToAttachToPerson.setAreaCode(areaCode);
						if(phoneNumberString !=null && !phoneNumberString.isEmpty())
							phoneToAttachToPerson.setPhoneNumber(phoneNumberString);
						if(phoneDateReceivedString!=null && !phoneDateReceivedString.isEmpty()){
							// TODO dateconvert and set
							Date dateReceived = new Date();
							dateReceived = simpleDateFormat.parse(phoneDateReceivedString);
							phoneToAttachToPerson.setDateReceived(dateReceived);
						}
						if(phoneComments!=null && !phoneComments.isEmpty())
							phoneToAttachToPerson.setComment(phoneComments);
						if(phoneSource!=null && !phoneSource.isEmpty())
							phoneToAttachToPerson.setSource(phoneSource);
						if(!updatePhones){
							//TODO check this works in all cases
							person.getPhones().add(phoneToAttachToPerson);
						}
					}
				}

				subject.setPerson(person);

				if (subject.getId() == null || subject.getPerson().getId() == 0) {
					insertSubjects.add(subject);
					/*StringBuffer sb = new StringBuffer();	//does this report have to happen? ... and in reality it hasnt had success yet
					sb.append("\nCreated subject from original Subject UID: ");
					sb.append(subject.getSubjectUID());
					//sb.append(" has been created successfully and linked to the study: ");
					//sb.append(study.getName());
					//sb.append("\n");
					uploadReport.append(sb);*/
					insertCount++;
				}
				else {
					// iStudyService.updateSubject(subjectVo);
					updateSubjects.add(subject);
					/*StringBuffer sb = new StringBuffer();
					sb.append("\nUpdate subject with Subject UID: ");
					sb.append(subject.getSubjectUID());
					//sb.append(" has been updated successfully and linked to the study: ");
					//sb.append(study.getName());
					//sb.append("\n");
					uploadReport.append(sb);*/
					updateCount++;
				}

				subjectCount++;
				//log.warn("finished message for " + subjectCount + "         updates= " + updateCount + "     inserts = " + insertCount + "   " );
			}
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
		uploadReport.append("Processed ");
		uploadReport.append(subjectCount);
		uploadReport.append(" subjects.");
		uploadReport.append("\n");
		uploadReport.append("Inserted ");
		uploadReport.append(insertCount);
		uploadReport.append(" subjects.");
		uploadReport.append("\n");
		uploadReport.append("Updated ");
		uploadReport.append(updateCount);
		uploadReport.append(" subjects.");
		uploadReport.append("\n");

		//TODO better exceptionhandling
		iStudyService.processBatch(insertSubjects, study, updateSubjects);
		
		return uploadReport;
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
		iStudyService.processFieldsBatch(customFieldsToUpdate, study, customFieldsToInsert);
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
