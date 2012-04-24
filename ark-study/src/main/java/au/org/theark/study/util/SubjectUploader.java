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
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkSubjectInsertException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.ConsentOption;
import au.org.theark.core.model.study.entity.ConsentStatus;
import au.org.theark.core.model.study.entity.ConsentType;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.PersonLastnameHistory;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.study.service.IStudyService;

import com.csvreader.CsvReader;

/**
 * SubjectUploader provides support for uploading subject matrix-formatted files. It features state-machine behaviour to allow an external class to
 * deal with how to store the data pulled out of the files.
 * 
 * @author cellis
 */
public class SubjectUploader {
	private long						subjectCount;
	private long						insertCount;
	private long						updateCount;
	private long						curPos;
	private long						srcLength				= -1;																				// -1 means nothing being
	// processed
	private StopWatch					timer						= null;
	private char						delimiterCharacter	= Constants.DEFAULT_DELIMITER_CHARACTER;									// default
	// delimiter:
	// COMMA
	private Study						study;
	static Logger						log						= LoggerFactory.getLogger(SubjectUploader.class);
	private IArkCommonService		iArkCommonService		= null;
	private IStudyService			iStudyService			= null;
	private StringBuffer				uploadReport			= null;
	private SimpleDateFormat		simpleDateFormat		= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private List<LinkSubjectStudy>	insertSubjects			= new ArrayList<LinkSubjectStudy>();
	private List<LinkSubjectStudy>	updateSubjects			= new ArrayList<LinkSubjectStudy>();

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
	public SubjectUploader(Study study, IArkCommonService iArkCommonService, IStudyService iStudyService) {
		this.study = study;
		this.iArkCommonService = iArkCommonService;
		this.iStudyService = iStudyService;
		simpleDateFormat.setLenient(false);
	}
	
	
	 /* Imports the subject data file to the database tables, and creates report on the process Assumes the file is in the default "matrix" file format:
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
		public StringBuffer uploadAndReportMatrixSubjectFile(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkSystemException {
			
			delimiterCharacter = inDelimChr;
			uploadReport = new StringBuffer();
			curPos = 0;

			InputStreamReader inputStreamReader = null;
			CsvReader csvReader = null;
			DecimalFormat decimalFormat = new DecimalFormat("0.00");

			try {
				inputStreamReader = new InputStreamReader(fileInputStream);
				csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
				String[] stringLineArray;

				//this is a list of all our somewhat enum like ref tables...
				//much better to call this once than each one n times in the for loop
				//should save 100,000-150,000 selects for a 17K insert.  may still wish to evaluate whats best here
				Collection<MaritalStatus> maritalStatiiPossible = iArkCommonService.getMaritalStatus();
				Collection<SubjectStatus> subjectStatiiPossible = iArkCommonService.getSubjectStatus();
				Collection<GenderType> genderTypesPossible = iArkCommonService.getGenderTypes();
				Collection<TitleType> titleTypesPossible = iArkCommonService.getTitleType();
				Collection<VitalStatus> vitalStatiiPossible = iArkCommonService.getVitalStatus();
				Collection<PersonContactMethod> personContactMethodPossible = iArkCommonService.getPersonContactMethodList();
				//Collection<MaritalStatus> yesNoList = iArkCommonService.getYesNoList();

				SubjectStatus defaultSubjectStatus = iStudyService.getDefaultSubjectStatus();
				TitleType defaultTitleType = iStudyService.getDefaultTitleType();
				GenderType defaultGenderType = iStudyService.getDefaultGenderType();
				VitalStatus defaultVitalStatus = iStudyService.getDefaultVitalStatus();
				MaritalStatus defaultMaritalStatus = iStudyService.getDefaultMaritalStatus();
				ConsentOption concentOptionOfYes = iStudyService.getConsentOptionForBoolean(true);//sounds a lot like boolean blah = true????
				ConsentStatus consentStatusOfConsented = iStudyService.getConsentStatusByName("Consented");
				ConsentType consentTypeOfElectronic = iStudyService.getConsentTypeByName("Electronic");
				
				List<String> subjectUIDsAlreadyExisting = iArkCommonService.getAllSubjectUIDs(study);	//TODO evaluate data in future to know if should get all id's in the csv, rather than getting all id's in study to compre
				
				boolean autoConsent = study.getAutoConsent();
				
				srcLength = inLength;
				if (srcLength <= 0) {
					uploadReport.append("The input size was not greater than 0. Actual length reported: ");
					uploadReport.append(srcLength);
					uploadReport.append("\n");
					throw new FileFormatException("The input size was not greater than 0. Actual length reported: " + srcLength);
				}

				timer = new StopWatch();
				timer.start();
				// Set field list (note 2th column to Nth column)		// SUBJECTUID DATE_COLLECTED F1 F2 FN
																						// 0 1 2 3 N      this must be done
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
				
				//log.warn("    genderIndex   "+genderIndex);
		
				// Loop through all rows in file
				while (csvReader.readRecord()) {
					//log.warn("reading msg " + subjectCount);
					stringLineArray = csvReader.getValues();
					String subjectUID = stringLineArray[0];

					boolean thisSubjectAlreadyExists = subjectUIDsAlreadyExisting.contains(subjectUID);

					//TODO ASAP maybe this can be replaced with a getAllSubjectUIDsForThisStudy up top...then just search throw all uids for a match?
					//can even pre-getAllSubjects in the already exists group
					LinkSubjectStudy subject = null;
					Person person = null;
					if(thisSubjectAlreadyExists){
						log.warn("about to ask service right now");
						subject = iArkCommonService.getSubjectByUIDAndStudy(subjectUID, study);
						log.warn("got the hibernate result back now");
						//subject should always have a person
						person = subject.getPerson();
					}
					else{
						subject = new LinkSubjectStudy();
						subject.setSubjectUID(subjectUID);//TODO ASAP: this seems wrong...if it's supposed to be autogenerated...we're maybe screwing next method 
						subject.setStudy(study);
						person = new Person();
					}
					log.warn("got the person");

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
							person.getPersonLastnameHistory().add(personLastNameHistory);//TODO ASAP analyze this
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
								}	//TODO else might belonog here really...but its already written in the batch insert anyway
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

					//TODO make this smarter about making a db call every time for just getting an emum
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
					/*cleaned this up...replaced it with code below
					if (csvReader.getIndex("PERSON_CONTACT_METHOD") > 0 || csvReader.getIndex("CONTACT_METHOD") > 0) {
						String personContactMethodStr = null;
						if (csvReader.getIndex("PERSON_CONTACT_METHOD") > 0) {
							personContactMethodStr = (stringLineArray[csvReader.getIndex("PERSON_CONTACT_METHOD")]);
						}
						else {
							personContactMethodStr = (stringLineArray[csvReader.getIndex("CONTACT_METHOD")]);
						}
						for(PersonContactMethod possibleMethod : personContactMethodPossible){
							if(possibleMethod.getName().equalsIgnoreCase(personContactMethodStr)){
								person.setPersonContactMethod(possibleMethod);		
							}	
						}
						//TODO if we get to the end and personcontactmethod doesnt exist...what do we do?  do we want a default or does it get ignored
						
					}*/
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

					log.warn("did all the statii junk..now about to set person and add to the insert or update list");
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
					log.warn("finished message for " + subjectCount + "         updates= " + updateCount + "     inserts = " + insertCount + "   " );//+
							//" travsguessatupdatestotal= " +  travsguessatupdatestotal + " " +	"   size of uploadReport= " + uploadReport.length());
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
				// Clean up the IO objects
				timer.stop();
				uploadReport.append("\n");
				uploadReport.append("Total elapsed time: ");
//				uploadReport.append(timer.getTime());
//				uploadReport.append(" ms or ");
				uploadReport.append(decimalFormat.format(timer.getTime() / 1000.0));
				uploadReport.append(" s");
				uploadReport.append("\n");
				uploadReport.append("Total file size: ");
//				uploadReport.append(inLength);
//				uploadReport.append(" B or ");
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

			// Batch insert/update
			try {
				iStudyService.batchInsertSubjects(insertSubjects);
			}
			catch (ArkUniqueException e) {
				e.printStackTrace();
			}
			catch (ArkSubjectInsertException e) {
				e.printStackTrace();
			}
			try {
				iStudyService.batchUpdateSubjects(updateSubjects);
			}
			catch (ArkUniqueException e) {
				e.printStackTrace();
			}
			catch (ArkSubjectInsertException e) {
				e.printStackTrace();
			}

			return uploadReport;
		}

	/**
	 * getallexistingsubjectuids
	 * 
	 * 
	 * 
	 * 
	 * 
	 * 
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
	 *
	public StringBuffer uploadAndReportMatrixSubjectFile(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkSystemException {
		delimiterCharacter = inDelimChr;
		uploadReport = new StringBuffer();
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			String[] stringLineArray;

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
			// SUBJECTUID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			//log.debug("Header length: " + csvReader.getHeaders().toString().length());

			int index = 0;

			// Loop through all rows in file
			while (csvReader.readRecord()) {
				log.warn("reading msg " + subjectCount);
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				String subjectUID = stringLineArray[0];

				SubjectVO subjectVo = new SubjectVO();
				LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
				linkSubjectStudy.setStudy(study);

				try {
					linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUID, study);
				}
				catch (EntityNotFoundException enf) {
					// New subject
					linkSubjectStudy.setSubjectUID(subjectUID);
					linkSubjectStudy.setStudy(study);
				}
				Person person = linkSubjectStudy.getPerson();
				subjectVo.setLinkSubjectStudy(linkSubjectStudy);

				if (linkSubjectStudy.getId() == null && linkSubjectStudy.getPerson().getId() == null) {
					person = new Person();
				}
				else {
					person = linkSubjectStudy.getPerson();
				}

				if (csvReader.getIndex("FIRST_NAME") > 0)
					person.setFirstName(stringLineArray[csvReader.getIndex("FIRST_NAME")]);

				if (csvReader.getIndex("MIDDLE_NAME") > 0)
					person.setMiddleName(stringLineArray[csvReader.getIndex("MIDDLE_NAME")]);

				if (csvReader.getIndex("LAST_NAME") > 0)
					person.setLastName(stringLineArray[csvReader.getIndex("LAST_NAME")]);

				if (csvReader.getIndex("PREFERRED_NAME") > 0)
					person.setPreferredName(stringLineArray[csvReader.getIndex("PREFERRED_NAME")]);

				if (csvReader.getIndex("GENDER_TYPE") > 0 || csvReader.getIndex("GENDER") > 0 || csvReader.getIndex("SEX") > 0) {
					GenderType genderType;
					if (csvReader.getIndex("GENDER_TYPE") > 0) {
						index = csvReader.getIndex("GENDER_TYPE");
					}
					else if (csvReader.getIndex("GENDER") > 0) {
						index = csvReader.getIndex("GENDER");
					}
					else {
						index = csvReader.getIndex("SEX");
					}

					if (stringLineArray[index] != null && stringLineArray[index].length() > 0) {
						genderType = iArkCommonService.getGenderType(stringLineArray[index]);
						person.setGenderType(genderType);
					}
				}

				if (csvReader.getIndex("DATE_OF_BIRTH") > 0 || csvReader.getIndex("DOB") > 0) {
					Date dateOfBirth = new Date();

					if (csvReader.getIndex("DATE_OF_BIRTH") > 0) {
						index = csvReader.getIndex("DATE_OF_BIRTH");
					}
					else {
						index = csvReader.getIndex("DOB");
					}

					if (stringLineArray[index] != null && stringLineArray[index].length() > 0) {
						dateOfBirth = simpleDateFormat.parse(stringLineArray[index]);
						person.setDateOfBirth(dateOfBirth);
					}
				}

				if (csvReader.getIndex("DATE_OF_DEATH") > 0 || csvReader.getIndex("DODEATH") > 0) {
					Date dateOfDeath = new Date();
					if (csvReader.getIndex("DATE_OF_DEATH") > 0) {
						index = csvReader.getIndex("DATE_OF_DEATH");
					}
					else {
						index = csvReader.getIndex("DODEATH");
					}

					if (stringLineArray[index] != null && stringLineArray[index].length() > 0) {
						dateOfDeath = simpleDateFormat.parse(stringLineArray[index]);
						person.setDateOfDeath(dateOfDeath);
					}
				}

				if (csvReader.getIndex("CAUSE_OF_DEATH") > 0 || csvReader.getIndex("CODEATH") > 0) {
					if (csvReader.getIndex("CAUSE_OF_DEATH") > 0) {
						index = csvReader.getIndex("CAUSE_OF_DEATH");
					}
					else {
						index = csvReader.getIndex("CODEATH");
					}

					if (stringLineArray[index] != null && stringLineArray[index].length() > 0) {
						person.setCauseOfDeath(stringLineArray[index]);
					}
				}

				if (csvReader.getIndex("VITAL_STATUS") > 0) {
					String vitalStatusStr = (stringLineArray[csvReader.getIndex("VITAL_STATUS")]);
					VitalStatus vitalStatus = iArkCommonService.getVitalStatus(vitalStatusStr);
					person.setVitalStatus(vitalStatus);
				}

				if (csvReader.getIndex("PREFERRED_EMAIL") > 0) {
					person.setPreferredEmail(stringLineArray[csvReader.getIndex("PREFERRED_EMAIL")]);
				}

				if (csvReader.getIndex("OTHER_EMAIL") > 0) {
					person.setPreferredEmail(stringLineArray[csvReader.getIndex("OTHER_EMAIL")]);
				}

				if (csvReader.getIndex("TITLE") > 0) {
					String titleStr = (stringLineArray[csvReader.getIndex("TITLE")]);
					TitleType titleType = iArkCommonService.getTitleType(titleStr);
					person.setTitleType(titleType);
				}

				if (csvReader.getIndex("MARITAL_STATUS") > 0) {
					String maritalStatusStr = (stringLineArray[csvReader.getIndex("MARITAL_STATUS")]);
					MaritalStatus maritalStatus = iArkCommonService.getMaritalStatus(maritalStatusStr);
					person.setMaritalStatus(maritalStatus);
				}

				if (csvReader.getIndex("PERSON_CONTACT_METHOD") > 0 || csvReader.getIndex("CONTACT_METHOD") > 0) {
					String personContactMethodStr = null;
					if (csvReader.getIndex("PERSON_CONTACT_METHOD") > 0) {
						personContactMethodStr = (stringLineArray[csvReader.getIndex("PERSON_CONTACT_METHOD")]);
					}
					else {
						personContactMethodStr = (stringLineArray[csvReader.getIndex("CONTACT_METHOD")]);
					}
					PersonContactMethod personContactMethod = iArkCommonService.getPersonContactMethod(personContactMethodStr);
					person.setPersonContactMethod(personContactMethod);
				}

				if (csvReader.getIndex("STATUS") > 0) {
					String statusStr = (stringLineArray[csvReader.getIndex("STATUS")]);
					SubjectStatus subjectStatus = iArkCommonService.getSubjectStatus(statusStr);
					linkSubjectStudy.setSubjectStatus(subjectStatus);
				}

				linkSubjectStudy.setPerson(person);
				subjectVo.setLinkSubjectStudy(linkSubjectStudy);

				if (subjectVo.getLinkSubjectStudy().getId() == null || subjectVo.getLinkSubjectStudy().getPerson().getId() == 0) {
					// iStudyService.createSubject(subjectVo);
					insertSubjects.add(subjectVo);
					StringBuffer sb = new StringBuffer();
					sb.append("Subject UID: ");
					sb.append(subjectVo.getLinkSubjectStudy().getSubjectUID());
					sb.append(" has been created successfully and linked to the study: ");
					sb.append(study.getName());
					sb.append("\n");
					uploadReport.append(sb);
					insertCount++;
				}
				else {
					// iStudyService.updateSubject(subjectVo);
					updateSubjects.add(subjectVo);
					StringBuffer sb = new StringBuffer();
					sb.append("Subject UID: ");
					sb.append(subjectVo.getLinkSubjectStudy().getSubjectUID());
					sb.append(" has been updated successfully and linked to the study: ");
					sb.append(study.getName());
					sb.append("\n");
					uploadReport.append(sb);
					updateCount++;
				}

				//log.debug("\n");
				subjectCount++;
				log.warn("finished message for " + subjectCount);
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

		// Batch insert/update
		try {
			iStudyService.batchInsertSubjects(insertSubjects);
		}
		catch (ArkUniqueException e) {
			e.printStackTrace();
		}
		catch (ArkSubjectInsertException e) {
			e.printStackTrace();
		}
		try {
			iStudyService.batchUpdateSubjects(updateSubjects);
		}
		catch (ArkUniqueException e) {
			e.printStackTrace();
		}
		catch (ArkSubjectInsertException e) {
			e.printStackTrace();
		}

		return uploadReport;
	}
*/

	/**
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of a file
	 */
	public List getListOfUidsFromInputStream(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkSystemException {
		List uids = new ArrayList<String>();
		delimiterCharacter = inDelimChr;
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;

		try {
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0) {
				uploadReport.append("The input size was not greater than 0. Actual length reported: ");
				uploadReport.append(srcLength);
				uploadReport.append("\n");
				throw new FileFormatException("The input size was not greater than 0. Actual length reported: " + srcLength);
			}
			csvReader.readHeaders();
			
			// Loop through all rows in file
			while (csvReader.readRecord()) {
				log.warn("reading row # " + subjectCount);
				stringLineArray = csvReader.getValues();
				String subjectUID = stringLineArray[0];
				uids.add(subjectUID);
				subjectCount++;
				//log.warn("finished message for " + subjectCount);
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

		return uids;
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
