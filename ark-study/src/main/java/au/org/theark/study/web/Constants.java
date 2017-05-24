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
package au.org.theark.study.web;

import java.util.ArrayList;
import java.util.List;

public class Constants {
	public static final String			UPLOAD_STATUS_OF_AWAITING_VALIDATION							= "AWAITING_VALIDATION";
	public static final String			UPLOAD_STATUS_OF_COMPLETED										= "COMPLETED";
	public static final String			UPLOAD_STATUS_OF_ERROR_IN_DATA_VALIDATION						= "ERROR_IN_DATA_VALIDATION";
	public static final String			UPLOAD_STATUS_OF_ERROR_IN_FILE_VALIDATION						= "ERROR_IN_FILE_VALIDATION";
	public static final String			UPLOAD_STATUS_OF_ERROR_ON_DATA_IMPORT							= "ERROR_ON_DATA_IMPORT";
	public static final String			UPLOAD_STATUS_OF_STATUS_NOT_DEFINED								= "STATUS_NOT_DEFINED";
	public static final String			UPLOAD_STATUS_OF_VALIDATED										= "VALIDATED";
	public static final String			ADMIN_TAB														= "Administration";
	public static final String			STUDY_TAB														= "Study";
	public static final String			SUBJECT_TAB														= "Subject";
	public static final String			STUDY_DETAILS													= "Study Details";
	public static final String			SUB_STUDIES														= "Sub Studies";
	public static final String			SITES																= "Sites";
	public static final String			CONSENT_SECTIONS												= "Consent Sections";
	public static final String			STUDY_COMPONENTS												= "Study Components";
	public static final String			USERS																= "Users";
	public static final String			MY_DETAILS														= "My Details";
	public static final String			STUDY_ADMIN														= "Study Admin";
	public static final String			PARTICIPANT_MANAGEMENT										= "Participant Management";
	public static final String			USER_NAME														= "userName";
	public static final String			FIRST_NAME														= "firstName";
	public static final String			LAST_NAME														= "lastName";
	public static final String			EMAIL																= "email";
	public static final String			PHONE_NUMBER													= "phoneNumber";
	public static final String			PASSWORD															= "password";
	public static final String			OLD_PASSWORD													= "oldPassword";
	public static final String			CONFIRM_PASSWORD												= "confirmPassword";
	public static final String			SAVE																= "save";
	public static final String			CANCEL															= "cancel";
	public static final String			DELETE															= "delete";
	public static final String			ARCHIVE															= "archive";
	public static final String			SEARCH_USER_FORM												= "searchUserForm";
	public static final String			SEARCH_FORM														= "searchForm";
	public static final String			SAVE_FORM														= "saveForm";
	public static final String			CONFIG_FORM														= "configurationForm";
	public static final String			SEARCH															= "search";
	public static final String			NEW																= "new";
	public static final String			RESET																= "reset";
	public static final String			CLOSE																= "close";
	public static final String			ADD_PHONE														= "addPhone";
	public static final int				MODE_NEW														= 1;
	public static final int				MODE_EDIT														= 2;
	public static final int				MODE_READ														= 3;
	//public static final int				MODE_EDIT_USER_ROLE_ONLY										= 4;

	/* Person */
	public static final String			PERSON_PREFERRED_EMAIL										= "linkSubjectStudy.person.preferredEmail";
	public static final String			PERSON_PREFERRED_EMAIL_STATUS								= "linkSubjectStudy.person.preferredEmailStatus";
	public static final String			PERSON_OTHER_EMAIL_STATUS									= "linkSubjectStudy.person.otherEmailStatus";
	public static final String			PERSON_OTHER_EMAIL											= "linkSubjectStudy.person.otherEmail";
	public static final String			PERSON_PREFERRED_NAME										= "linkSubjectStudy.person.preferredName";
	public static final String			PERSON_VITAL_STATUS											= "linkSubjectStudy.person.vitalStatus";
	public static final String			PERSON_PERSON_ID											= "linkSubjectStudy.person.id";
	public static final String			SUBJECT_UID													= "linkSubjectStudy.subjectUID";
	public static final String			FAMILY_ID													= "linkSubjectStudy.familyId";
	public static final String			PERSON_FIRST_NAME											= "linkSubjectStudy.person.firstName";
	public static final String			PERSON_MIDDLE_NAME											= "linkSubjectStudy.person.middleName";
	public static final String			PERSON_LAST_NAME											= "linkSubjectStudy.person.lastName";
	public static final String			SUBJECT_PREVIOUS_LAST_NAME									= "subjectPreviousLastname";
	public static final String			PERSON														= "person";
	public static final String			GENDER_TYPE													= "genderType";
	public static final String			ID															= "id";
	public static final String			SUBJECT_STATUS												= "linkSubjectStudy.subjectStatus";
	public static final String			SUBJECT_STATUS_ID											= "id";
	public static final String			VITAL_STATUS												= "vitalStatus";
	public static final String			STATUS_NAME													= "statusName";
	public static final String			PERSON_MARITAL_STATUS										= "linkSubjectStudy.person.maritalStatus";
	public static final String			PERSON_CONTACT_METHOD										= "linkSubjectStudy.person.personContactMethod";
	public static final String			PERSON_DOB													= "linkSubjectStudy.person.dateOfBirth";
	public static final String			PERSON_CURRENT_OR_DEATH_AGE									= "linkSubjectStudy.person.currentOrDeathAge";
	public static final String			PERSON_DOD													= "linkSubjectStudy.person.dateOfDeath";
	public static final String			PERSON_COMMENT												= "linkSubjectStudy.comment";
	public static final String			PERSON_DATE_LAST_KNOWN_ALIVE								= "linkSubjectStudy.person.dateLastKnownAlive";
	public static final String			PERSON_CONSENT_DATE											= "linkSubjectStudy.consentDate";
	public static final String			PERSON_CONSENT_DOWNLOADED									= "linkSubjectStudy.consentDownloaded";
	public static final String			SUBJECT_CONSENT_STATUS										= "linkSubjectStudy.consentStatus";
	public static final String			SUBJECT_CONSENT_TYPE											= "linkSubjectStudy.consentType";
	public static final String			PERSON_CAUSE_OF_DEATH										= "linkSubjectStudy.person.causeOfDeath";
	public static final String			PERSON_TYTPE_TYPE												= "linkSubjectStudy.person.titleType";
	public static final String			PERSON_GENDER_TYPE											= "linkSubjectStudy.person.genderType";
	public static final String			PERSON_GENDER_TYPE_NAME										= "linkSubjectStudy.person.genderType.name";
	public static final String			SUBJECT_FULL_NAME												= "subjectFullName";
	public static final String			PERSON_VITAL_STATUS_NAME									= "linkSubjectStudy.person.vitalStatus.statusName";
	public static final String			SUBJECT_STATUS_NAME											= "linkSubjectStudy.person.subjectStatus.name";
	public static final String			EVEN																= "even";
	public static final String			ODD																= "odd";
	public static final String			CLASS																= "class";
	public static final String			SUBJECT_KEY_LBL												= "subjectKeyLbl";
	public static final String			SUBJECT_LIST													= "subjectList";
	public static final String			PHONE_LIST														= "phoneList";
	public static final String			PHONE_NUMBER_VALUE											= "phoneNumberValue";
	public static final String			CONSENT_LIST													= "consentList";
	public static final String			CONSENT_COMPONENT_LABEL										= "consentComponentLabel";

	public static final String			ADDRESS_LIST													= "addressList";
	public static final String			ADDRESS_LABEL													= "addressLabel";
	/* Multiple choice List Controls */
	public static final String			AVAILABLE_ROLES_MLC											= "availableRolesLMC";
	public static final String			SELECTED_ROLES_MLC											= "selectedRolesLMC";

	/* Buttons */
	public static final String			ADD_ALL_BUTTON													= "addAllBtn";
	public static final String			ADD_SELECTED													= "addSelectedBtn";
	public static final String			REMOVE_ALL_BUTTON												= "removeAllBtn";
	public static final String			REMOVE_SELECTED_BUTTON										= "removeSelectedBtn";
	/* Accordion Constants */
	public static final String			ACCORDION														= "accordion";
	public static final String			ACCORDION_SECTION												= "accordionSection";
	public static final String			ACCORDION_SECTION_NAME_LBL									= "accordionSectionNameLbl";
	public static final String			AJAX_CONTAINER													= "ajaxContainer";
	public static final String			APP_ROLE_ACCORDION											= "appRoleAccordion";

	/* Form Objects */
	public static final String			USER_FORM														= "userForm";
	public static final String			USER_DETAILS_FORM												= "userDetailsForm";
	public static final String			APP_ROLE_FORM													= "appRoleForm";

	/* Study */

	public static final String			STUDY_SEARCH_KEY												= "study.id";
	public static final String			STUDY_SEARCH_NAME												= "study.name";
	public static final String			STUDY_SEARCH_DOA												= "study.dateOfApplication";
	public static final String			STUDY_SEARCH_CONTACT											= "study.contactPerson";

	/**
	 * Site
	 */
	public static final String			STUDY_SITE_KEY													= "studySite.studySiteKey";
	public static final String			STUDY_SITE_NAME												= "studySite.name";

	public static final String			STUDY_ID															= "study.id";
	public static final String			STUDY_NAME														= "study.name";
	public static final String			STUDY_DESCRIPTION												= "study.description";
	public static final String			STUDY_STATUS													= "study.studyStatus";
	public static final String			STUDY_DATE_OF_APPLICATION									= "study.dateOfApplication";
	public static final String			STUDY_CONTACT_PERSON											= "study.contactPerson";
	public static final String			STUDY_CONTACT_PERSON_PHONE									= "study.contactPersonPhone";
	public static final String			STUDY_ESTIMATED_YEAR_OF_COMPLETION						= "study.estimatedYearOfCompletion";
	public static final String			STUDY_CHIEF_INVESTIGATOR									= "study.chiefInvestigator";
	public static final String			STUDY_CO_INVESTIGATOR										= "study.coInvestigator";
	public static final String			STUDY_AUTO_GENERATE_SUBJECTUID							= "study.autoGenerateSubjectUid";
	public static final String			STUDY_AUTO_GENERATE_BIOCOLLECTIONUID					= "study.autoGenerateBiocollectionUid";
	public static final String			STUDY_AUTO_GENERATE_BIOSPECIMENUID						= "study.autoGenerateBiospecimenUid";
	public static final String			SUBJECT_UID_START												= "study.subjectUidStart";
	public static final String			SUBJECT_UID_PREFIX											= "study.subjectUidPrefix";
	public static final String			SUBJECT_UID_TOKEN												= "study.subjectUidToken";
	public static final String			SUBJECT_UID_PADCHARS											= "study.subjectUidPadChars";
	public static final String			SUBJECT_UID_PADCHAR											= "study.subjectUidPadChar";
	public static final String			STUDY_LDAP_GROUP_NAME										= "ldapGroupName";
	public static final String			STUDY_AUTO_CONSENT											= "study.autoConsent";
	public static final String			SUB_STUDY_BIOSPECIMENT_PREFIX								= "study.subStudyBiospecimenPrefix";
	public static final String			STUDY_FILENAME													= "study.filename";

	public static final String			STUDY_MAIN_TAB													= "Study";
	public static final String			SUBJECT_MAIN_TAB												= "Subject";
	// public static final String STUDY_STATUS_KEY="studyStatusId";
	public static final String			STUDY_STATUS_KEY												= "id";
	public static final String			STUDY_DROP_DOWN_CHOICE										= "studyChoice";
	public static final String			YES																= "Yes";
	public static final String			NO																	= "No";
	public static final String			NAME																= "name";
	public static final String			MY_DETAILS_PANEL												= "myDetailsPanel";
	public static final String			LMC_AJAX_CONTAINER											= "lmcAjaxContainer";
	public static final String			LMC_SELECTED_APPS												= "lmcSelectedApps";
	public static final String			LMC_AVAILABLE_APPS											= "lmcAvailableApps";
	public static final String			PHONE_TYPE_ID													= "phoneTypeId";
	public static final String			AUTO_GEN_SUBJECT_KEY											= "autoGenerateSubjectKey";
	public static final String			AUTO_GENERATE_SUBJECTUID									= "autoGenerateSubjectUid";
	public static final String			AUTO_CONSENT													= "autoConsent";
	public static final String			MODULE_NAME														= "moduleName";
	public static final String			MODULES_SELECTED												= "modulesSelected";
	public static final String			MODULES_AVAILABLE												= "modulesAvailable";
	public static final String			STUDY_COMPONENT_ID											= "studyComponent.id";
	public static final String			STUDY_COMPONENT_NAME											= "studyComponent.name";
	public static final String			STUDY_COMPONENT_DESCRIPTION								= "studyComponent.description";
	public static final String			STUDY_COMPONENT_KEYWORD										= "studyComponent.keyword";
	/* Error Messages and Fields references from Resource files */
	public static final String			ERROR_STUDY_NAME_REQUIRED									= "error.study.name.required";
	public static final String			ERROR_STUDY_STATUS_REQUIRED								= "error.study.status.required";
	public static final String			ERROR_STUDY_CHIEF_INVESTIGATOR							= "error.study.chiefInvestigator";
	public static final String			ERROR_STUDY_SUBJECT_KEY_PREFIX							= "error.study.subject.key.prefix";
	public static final String			ERROR_STUDY_DOA_MAX_RANGE									= "error.study.doa.max.range";
	public static final String			CHIEF_INVESTIGATOR											= "Chief Investigator";
	public static final String			ERROR_STUDY_CO_INVESTIGATOR								= "error.study.co.investigator";
	public static final String			CO_INVESTIGATOR												= "Co-Investigator";
	public static final String			STATUS															= "Status";
	public static final String			ERROR_STUDY_NAME												= "Study Name";

	/* Spring Bean names */
	public static final String			STUDY_SERVICE													= "studyService";
	public static final String			STUDY_DAO														= "studyDao";
	public static final String			LIMS_SERVICE 													= "limsService";

	public static final String			ACTION_ADD_SELECTED											= "addSelected";
	public static final String			ACTION_ADD_ALL													= "addAll";
	public static final String			ACTION_REMOVE_SELECTED										= "removeSelected";
	public static final String			ACTION_REMOVE_ALL												= "removeAll";

	/* General ones */
	public static final String			PAGE_SEARCH														= "page.search";
	public static final String			PAGE_NEW															= "page.new";
	public static final String			CANCEL_KEY														= "cancelKey";
	public static final String			SAVE_KEY															= "saveKey";
	public static final String			DELETE_KEY														= "deleteKey";
	public static final String			STUDY																= "study";

	/* Person Address */
	/*public static final String			ADDRESS_STREET_ADDRESS										= "addressVo.address.streetAddress";
	public static final String			ADDRESS_CITY													= "addressVo.address.city";
	public static final String			ADDRESS_POST_CODE												= "addressVo.address.postCode";
	public static final String			ADDRESS_COUNTRY												= "addressVo.address.country";
	public static final String			ADDRESS_STATE													= "addressVo.address.state";
	public static final String			ADDRESS_ADDRESSTYPE											= "addressVo.address.addressType";
	public static final String			ADDRESS_ADDRESSSTATUS										= "addressVo.address.addressStatus";
	public static final String			ADDRESS_DATE_RECEIVED										= "addressVo.address.dateReceived";
	public static final String			ADDRESS_COMMENTS												= "addressVo.address.comments";
	public static final String			ADDRESS_PREFERRED_MAILING									= "addressVo.address.preferredMailingAddress";
*/
	public static final String			CONSENT_CONSENT_TYPE											= "consent.consentType";
	public static final String			CONSENT_CONSENTED_BY											= "consent.consentedBy";
	public static final String			CONSENT_CONSENT_STATUS										= "consent.consentStatus";
	public static final String			CONSENT_STUDY_COMP											= "consent.studyComp";
	public static final String			CONSENT_STUDY_COMP_STATUS									= "consent.studyComponentStatus";
	public static final String			CONSENT_CONSENT_DATE										= "consent.consentDate";
	public static final String			CONSENT_REQUESTED_DATE										= "consent.requestedDate";
	public static final String			CONSENT_RECEIVED_DATE										= "consent.receivedDate";
	public static final String			CONSENT_COMPLETED_DATE										= "consent.completedDate";

	public static final String			CONSENT_CONSENT_COMMENT										= "consent.comments";
	public static final String			CONSENT_CONSENT_DOWNLOADED									= "consent.consentDownloaded";

	public static final String			CONSENT_FILE_ID												= "consentFile.id";
	public static final String			CONSENT_FILE_FILENAME										= "consentFile.filename";
	public static final String			CONSENT_FILE_PAYLOAD											= "consentFile.payload";
	public static final String			CONSENT_FILE_USER_ID											= "consentFile.userId";

	public static final int				PALETTE_ROWS													= 5;
	public static final long			STUDY_LOGO_FILESIZE_KB										= 100;

	/* Custom Field */
	public static final String			CUSTOM_FIELD_FIELD_TITLE									= "customField.fieldTitle";
	public static final String			CUSTOM_FIELD_FIELD_NAME										= "customField.name";
	public static final String			SEARCH_RESULT_LIST											= "searchResultList";
	public static final String			CUSTOM_FIELD_LABEL											= "fieldTitleLabel";
	public static final String			DOWNLOAD_FILE													= "downloadFile";
	public static final String			DELETE_FILE														= "deleteFile";
	public static final String			STUDY_STATUS_COMPLETED										= "Completed";
	public static final String			STUDY_STATUS_REQUESTED										= "Requested";
	public static final String			STUDY_STATUS_RECEIVED										= "Received";
	public static final String			WMC_RECIEVED													= "wmc-received";
	public static final String			WMC_COMPLETED													= "wmc-completed";
	public static final String			WMC_REQUESTED													= "wmc-requested";
	public static final String			WMC_PLAIN														= "wmc-plain";
	public static final String			SUBJECT_FILE_ID												= "subjectFile.id";
	public static final String			SUBJECT_FILE_FILENAME										= "subjectFile.filename";
	public static final String			SUBJECT_FILE_STUDY_COMP										= "subjectFile.studyComp";
	public static final String			SUBJECT_FILE_USER_ID											= "subjectFile.userId";
	public static final String			SUBJECT_FILE_COMMENTS										= "subjectFile.comments";

	public static final String			STATE_SELECTOR_WMC											= "stateSelector";
	public static final String			SUBJECT_FILE_FILENAMELINK									= "subjectFile.link";
	// WebMarkupContainer
	// that
	// will
	// house
	// the
	// DropDowns
	// for
	// States

	public static final String			SUBJECT_CONSENT_TO_ACTIVE_CONTACT						= "linkSubjectStudy.consentToActiveContact";
	public static final String			SUBJECT_CONSENT_TO_USEDATA								= "linkSubjectStudy.consentToUseData";
	public static final String			SUBJECT_CONSENT_PASSIVE_DATA_GATHER						= "linkSubjectStudy.consentToPassiveDataGathering";
	public static final String			SUBJECT_HEARD_ABOUT_STUDY_FROM							= "linkSubjectStudy.heardAboutStudy";
	public static final String			SUBJECT_AUTO_GENERATED									= "Auto-generated";
	public static final String			SUBJECTUID_EXAMPLE										= "AAA-000000001";
	public static final String			BIOSPECIMENUID_EXAMPLE									= "AAA-000000001";
	public static final String			BIOCOLLECTIONUID_EXAMPLE								= "AAA-000000001";

	// UploadVO
	public static final String			UPLOADVO_UPLOAD_ID										= "upload.id";
	public static final String			UPLOADVO_UPLOAD_COLLECTION_UPLOAD						= "upload.collectionUpload";
	public static final String			UPLOADVO_UPLOAD_COLLECTION_UPLOAD_ID					= "upload.collectionUpload.id";
	public static final String			UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION			= "upload.collectionUpload.collection";
	public static final String			UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION_ID			= "upload.collectionUpload.collection.id";
	public static final String			UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION_NAME		= "upload.collectionUpload.collection.name";
	public static final String			UPLOADVO_UPLOAD_FILENAME								= "upload.filename";
	public static final String			UPLOADVO_UPLOAD_FILE_FORMAT								= "upload.fileFormat";
	public static final String			UPLOADVO_UPLOAD_FILE_FORMAT_ID							= "upload.fileFormat.id";
	public static final String			UPLOADVO_UPLOAD_FILE_FORMAT_NAME						= "upload.fileFormat.name";
	public static final String			UPLOADVO_UPLOAD_PAYLOAD									= "upload.payload";
	public static final String			UPLOADVO_UPLOAD_DELIMITER_TYPE							= "upload.delimiterType";
	public static final String			UPLOADVO_UPLOAD_UPLOAD_TYPE								= "upload.uploadType";
	public static final String			UPLOADVO_UPLOAD_USER									= "upload.user";
	public static final String			UPLOADVO_UPLOAD_USER_ID									= "upload.userId";
	public static final String			UPLOADVO_UPLOAD_INSERT_TIME								= "upload.insertTime";
	public static final String			UPLOADVO_UPLOAD_UPDATE_USER								= "upload.user";
	public static final String			UPLOADVO_UPLOAD_UPDATE_USER_ID							= "upload.user.id";
	public static final String			UPLOADVO_UPLOAD_UPDATE_TIME								= "upload.updateTime";
	public static final String			UPLOADVO_UPLOAD_CHECKSUM								= "upload.checksum";
	public static final String			UPLOADVO_UPLOAD_START_TIME								= "upload.startTime";
	public static final String			UPLOADVO_UPLOAD_FINISH_TIME								= "upload.finishTime";
	public static final String			UPLOADVO_UPLOAD_UPLOAD_STATUS_NAME						= "upload.uploadStatus.name";
	public static final String			UPLOADVO_UPLOAD_UPLOAD_REPORT							= "upload.uploadReport";

	public static final String			FILE_FORMAT												= "fileFormat";
	public static final String			FILE_FORMAT_ID											= "id";
	public static final String			FILE_FORMAT_NAME										= "name";

	public static final String			UPLOAD													= "upload";
	public static final String			UPLOAD_ID												= "id";
	public static final String			UPLOAD_STUDY											= "study";
	public static final String			UPLOAD_FILE_FORMAT									= "fileFormat";
	public static final String			UPLOAD_DELIMITER_TYPE								= "delimiterType";
	public static final String			UPLOAD_FILENAME										= "filename";
	public static final String			UPLOAD_PAYLOAD											= "payload";
	public static final String			UPLOAD_USER												= "user";
	public static final String			UPLOAD_CHECKSUM											= "checksum";
	public static final String			DELIMITER_TYPE_ID										= "id";
	public static final String			UPLOAD_TYPE_ID											= "id";
	public static final String			DELIMITER_TYPE_NAME										= "name";
	public static final String			UPLOAD_TYPE_NAME										= "name";

	// Exception messages
	public static final String			IO_EXCEPTION											= "IOException: Input error. ";
	public static final String			FILE_FORMAT_EXCEPTION									= "File Format Exception: Input error. ";
	public static final String			ARK_SYSTEM_EXCEPTION									= "General ARK System Exception: ";
	public static final String			ARK_BASE_EXCEPTION										= "Base ARK System Exception: ";

	public static final String[]		SUBJECT_TEMPLATE_HEADER										= { "SUBJECTUID", "FAMILY_ID", "TITLE", "FIRST_NAME", "MIDDLE_NAME", "LAST_NAME", "PREFERRED_NAME", "DATE_OF_BIRTH",
			"VITAL_STATUS", "GENDER", "STATUS", "DATE_OF_DEATH",  "DATE_LAST_KNOWN_ALIVE", "CAUSE_OF_DEATH", "MARITAL_STATUS", "PREFERRED_CONTACT", "EMAIL",
			"ADDRESS_BUILDING_NAME", "ADDRESS_STREET_ADDRESS", "ADDRESS_SUBURB", "ADDRESS_STATE", "ADDRESS_COUNTRY", "ADDRESS_POST_CODE", "ADDRESS_SOURCE", "ADDRESS_STATUS",
			"ADDRESS_TYPE", "ADDRESS_DATE_RECEIVED", "ADDRESS_COMMENTS", "ADDRESS_IS_PREFERRED", "PHONE_AREA_CODE",
			"PHONE_NUMBER", "PHONE_TYPE", "PHONE_STATUS", "PHONE_SOURCE", "PHONE_COMMENTS","PHONE_SILENT", "PHONE_IS_PREFERRED","PHONE_DATE_RECEIVED", 
			"PREVIOUS_LAST_NAME", "OTHER_EMAIL", "HEARD_ABOUT_STUDY" , "COMMENTS", "EMAIL_STATUS", "OTHER_EMAIL_STATUS", "EMAIL_STATUS",
			"CONSENT_DATE", "CONSENT_STATUS", "CONSENT_TYPE", "CONSENT_TO_PASSIVE_DATA_GATHERING", "CONSENT_TO_ACTIVE_CONTACT", "CONSENT_TO_USE_DATA"		
	};
	
	public static final String[] SUBJECT_CONSENT_TEMPLATE_HEADER = {"SUBJECTUID", "STUDY_COMPONENT", "STUDY_COMPONENT_STATUS","REQUESTED_DATE","RECEIVED_DATE","COMPLETED_DATE","CONSENT_TYPE","CONSENT_STATUS",
		"CONSENT_DOWNLOADED","CONSENTED_BY","CONSENT_DATE","COMMENT"};

	
	//TODO this is all for validation and templates.  maybe we need some form of validation mapping to db, entities or something like that as this will not be maintainable for long
	public static final String[][]	SUBJECT_OR_FAMILY_CUSTOM_FIELD_DATA_TEMPLATE_CELLS										= {
			{ "", "SUBJECTUID OR FAMILYUID", "YOUR_FIRST_CUSTOM_FIELD_NAME_HERE", "YOUR_SECOND_CUSTOM_FIELD_NAME_HERE", "AND SO ON"},
			{ "DESCRIPTION", "The unique identifier assigned for this subject or family.", "Value for first custom field","Value for second field", "AND SO ON" },
			{ "NOTE: This is the common template to upload the Subject or the Family custom field data.", "This must match the subjectUID in the database or if it's a FAMILYUID it should be the valid FAMILYUID of a SUBJECT", "" , "", "" },
			{ "		 Please make sure to use either one of a dataset.", "", "" , "", "" },
			{ "		 Removed this first column, and replace row 2 with your actual custom field names (the headers only appear once, row 3 will have your next subject or family and his/her values", "", "" , "", "" } };

	public static final String[][]	SUBJECT_CONSENT_FIELD_TEMPLATE_CELLS										= {
		{ "", "SUBJECTUID", "STUDY_COMPONENT", "STUDY_COMPONENT_STATUS","REQUESTED_DATE","RECEIVED_DATE","COMPLETED_DATE","CONSENT_TYPE","CONSENT_STATUS","CONSENT_DOWNLOADED","CONSENTED_BY","CONSENT_DATE","COMMENT"},
		{ "DESCRIPTION", "The unique identifier assigned for this subject.  This must match the subjectUID in the database", "@CompName","@CompNameStatus","If status is requested then (dd/mm/yyyy)","If status is received then (dd/mm/yyyy)","If status is completed then (dd/mm/yyyy)","@ConsentName","@ConsentStatus","Yes/No","","dd/mm/yyyy","" },
		{ "NOTE: Removed this first column", "", "" , "", "","","","","","","","",""} };
	
	public static final String[] SUBJECT_ATTACHMENT_TEMPLATE_HEADER = {"SUBJECTUID", "FILE_NAME_WITH_FULL_PATH", "STUDY_COMPONENT","COMMENT"};
	
	public static final String[][] SUBJECT_ATTACHMENT_TEMPLATE_CELLS = {
		{"", "SUBJECTUID", "FILE_NAME_WITH_FULL_PATH", "STUDY_COMPONENT","COMMENT"},
		{"DESCRIPTION", "The unique identifier assigned for this subject.  This must match the subjectUID in the database","File name with full path. Ex:/mnt/data/arkAttachments/sample.csv", "Valid study component name","" },
		{ "NOTE: Removed this first column", "", "" , "", ""}};
	
	//TODO this is all for validation and templates.  maybe we need some form of validation mapping to db, entities or something like that as this will not be maintainable for long
	/**
	public static final String[][]	SUBJECT_TEMPLATE_CELLS										= {
			{ "", "SUBJECTUID", "TITLE", "FIRST_NAME", "MIDDLE_NAME", "LAST_NAME", "PREFERRED_NAME", "DATE_OF_BIRTH", "VITAL_STATUS", "GENDER", "STATUS", "DATE_OF_DEATH", "DATE_LAST_KNOWN_ALIVE", "CAUSE_OF_DEATH",
			"MARITAL_STATUS", "PREFERRED_CONTACT", "EMAIL", 
			"ADDRESS_BUILDING_NAME", "ADDRESS_STREET_ADDRESS", "ADDRESS_SUBURB", "ADDRESS_STATE", "ADDRESS_COUNTRY", "ADDRESS_POST_CODE", "ADDRESS_SOURCE", "ADDRESS_STATUS",
			"ADDRESS_TYPE", "ADDRESS_DATE_RECEIVED", "ADDRESS_COMMENTS", "ADDRESS_IS_PREFERRED", "PHONE_AREA_CODE",
			"PHONE_NUMBER", "PHONE_TYPE", "PHONE_STATUS", "PHONE_SOURCE", "PHONE_COMMENTS","PHONE_SILENT","PHONE_IS_PREFERRED", "PHONE_DATE_RECEIVED", 
			"PREVIOUS_LAST_NAME", "OTHER_EMAIL", "HEARD_ABOUT_STUDY" , "COMMENTS", "EMAIL_STATUS", "OTHER_EMAIL_STATUS",
			"CONSENT_DATE", "CONSENT_STATUS", "CONSENT_TYPE", "CONSENT_TO_PASSIVE_DATA_GATHERING", "CONSENT_TO_ACTIVE_CONTACT", "CONSENT_TO_USE_DATA"},
			{ "DESCRIPTION", "The unique identifier assigned for this subject.  This may be automatically generated on upload into The Ark", "The title by which the subject prefers to be addressed",
			"The subjects first name", "The subjects middle name", "The subjects last or family name", "The name by which the subject prefers to be addressed", "The date the subject was born",
			"The subject's vital status", "The subject's gender", "The status of the subject as it pertains to the study", "The date the subject died, if known",  "The date the subject was last known to be alive", "The cause of death",
			"The marital status of the subject", "The preferred method for contacting the subject", "The subject's primary email address", 
			"The subject's building name", "The subject's street address", "The subject's suburb", "The subject's state", "The subject's country", "The subject's postcode", "source of the address", "The subject's address status",
			"The subject's address type", "received date of subject's address", "any comment for a address", "Whether this is the preferred mailing address", "The subject's area code",
			"The subject's phone number", "The subject's phone type", "The subject's phone status", "The subject's phone source", "The subject's phone comments","Whether this number is silent or not","Whether this is a prefered number.", "Date that the phone number was recorded", 
			"The subject's previous last name", "The subject's other email", "The subject's heard about the study" , "The subject's comment", "The subject's email status", "The subject's other email status",
			"The subject's consent date", "The subject's consent status", "The subject's consent type", "The subject's consent to passive data gathering", "The subject's consent to active data", "The subject's consent to use data"},
			{ "FORMAT", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "DD/MM/YYYY", "CHARACTER", "CHARACTER", "CHARACTER", "DD/MM/YYYY", "DD/MM/YYYY", "ALPHANUMERIC", "CHARACTER",
			"CHARACTER", "ALPHANUMERIC" , 
			"CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER",
			"CHARACTER", "DD/MM/YYYY", "CHARACTER", "CHARACTER", "ALPHANUMERIC",
			"ALPHANUMERIC", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER","Silent?","Preferred?" ,"DD/MM/YYYY", "CHARACTER", "CHARACTER", "CHARACTER" , "CHARACTER", "CHARACTER", "CHARACTER",
			"DD/MM/YYYY", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER"},
			{ "MANDATORY", "Yes - unless assigned on upload", "No", "No", "No", "No", "No", "No", "No", "No", "Yes", "No", "No", "No", "No", "No",  "No", 
				"No", "No", "No", "No", "No", "No", "No", "No",
				"No", "No", "No", "No", "No",
				"No", "No", "No", "No", "No","No","Yes","No", "No", "No", "No" , "No" , "No", "No",
				"No", "No", "No" , "No" , "No", "No"},
			{ "VALID VALUES", "", "Unknown, Br, Capt, Col, Cpl, Dean, Dr, Fr, Lac, Major, Miss, Mr, Mrs, Ms, Past, Prof, Pstr, Rev, Sir, Sr", "", "", "", "", "", "Alive, Deceased, Unknown",
			"Male, Female, Unknown", "Subject, Prospect, Withdrawn Subject, Archive, Inactive", "", "", "", "Married, Single, Divorced, Unknown", "Email, Home telephone, Mobile telephone, Post", "", 
			"", "", "", "Refer to user interface for options", "Refer to user interface for options", "",  //last three on this line are state country and postcode - i really could enforce something 
			"", "Current,Current - Alternative,Current - Under Investigation,Incorrect address,Valid past address", "Postal,Work,Residential", "", "", "", "",
			"", "Mobile,Home,Work", "Unknown,Current,Current Alternative,Current Under Investigation,Valid Past,Incorrect or Disconnected", "", "","Yes,No", "Yes,No","", "", "", "" , "", "Unknown, Verified, Unverified, Bounced", "Unknown, Verified, Unverified, Bounced",
			"", "Consented, Ineligible, Refused, Withdrawn, Pending", "Electronic, Hard Copy" , "Yes, No, Pending, Unavailable, Limited, Revoked" , "Yes, No, Pending, Unavailable, Limited, Revoked", "Yes, No, Pending, Unavailable, Limited, Revoked"},
			{ "NOTE: Remove this first column, and replace the contents of rows 2 to 6", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
				"", "", "", "", "", "", "", "",
				"", "", "", "", "",
<<<<<<< HEAD
				"", "", "", "", "","", "", "" , "", "" , "" , "" , "", "",
				"", "", "", "", "",""} };
=======
				"", "", "", "", "","", "", "" , "", "" , "" , "" , "",
				"", "", "", "", "","", ""} };
	*/
	public static final String[][]	SUBJECT_TEMPLATE_CELLS										= {
		{ "", "SUBJECTUID", "FAMILY_ID", "TITLE", "FIRST_NAME", "MIDDLE_NAME", "LAST_NAME", "PREFERRED_NAME", "DATE_OF_BIRTH", "VITAL_STATUS", "GENDER", "STATUS", "DATE_OF_DEATH", "DATE_LAST_KNOWN_ALIVE", "CAUSE_OF_DEATH",
		"MARITAL_STATUS", "PREFERRED_CONTACT", "EMAIL", 
		"ADDRESS_BUILDING_NAME", "ADDRESS_STREET_ADDRESS", "ADDRESS_SUBURB", "ADDRESS_STATE", "ADDRESS_COUNTRY", "ADDRESS_POST_CODE", "ADDRESS_SOURCE", "ADDRESS_STATUS",
		"ADDRESS_TYPE", "ADDRESS_DATE_RECEIVED", "ADDRESS_COMMENTS", "ADDRESS_IS_PREFERRED", "PHONE_AREA_CODE",
		"PHONE_NUMBER", "PHONE_TYPE", "PHONE_STATUS", "PHONE_SOURCE", "PHONE_COMMENTS","PHONE_SILENT", "PHONE_DATE_RECEIVED", 
		"PREVIOUS_LAST_NAME", "OTHER_EMAIL", "HEARD_ABOUT_STUDY" , "COMMENTS", "EMAIL_STATUS", "OTHER_EMAIL_STATUS",
		"CONSENT_DATE", "CONSENT_STATUS", "CONSENT_TYPE", "CONSENT_TO_PASSIVE_DATA_GATHERING", "CONSENT_TO_ACTIVE_CONTACT", "CONSENT_TO_USE_DATA"},
		{ "DESCRIPTION", "The unique identifier assigned for this subject.  This may be automatically generated on upload into The Ark", "The Family Identity of the subject to group family information", "The title by which the subject prefers to be addressed",
		"The subjects first name", "The subjects middle name", "The subjects last or family name", "The name by which the subject prefers to be addressed", "The date the subject was born",
		"The subject's vital status", "The subject's gender", "The status of the subject as it pertains to the study", "The date the subject died, if known",  "The date the subject was last known to be alive", "The cause of death",
		"The marital status of the subject", "The preferred method for contacting the subject", "The subject's primary email address", 
		"The subject's building name", "The subject's street address", "The subject's suburb", "The subject's state", "The subject's country", "The subject's postcode", "source of the address", "The subject's address status",
		"The subject's address type", "received date of subject's address", "any comment for a address", "Whether this is the preferred mailing address", "The subject's area code",
		"The subject's phone number", "The subject's phone type", "The subject's phone status", "The subject's phone source", "The subject's phone comments","Whether this number is silent or not", "Date that the phone number was recorded", 
		"The subject's previous last name", "The subject's other email", "The subject's heard about the study" , "The subject's comment", "The subject's email ststus", "The subject's other email status",
		"The subject's consent date", "The subject's consent status", "The subject's consent type", "The subject's consent to passive data gathering", "The subject's consent to active data", "The subject's consent to use data"},
		{ "FORMAT", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "DD/MM/YYYY", "CHARACTER", "CHARACTER", "CHARACTER", "DD/MM/YYYY", "DD/MM/YYYY", "ALPHANUMERIC", "CHARACTER",
		"CHARACTER", "ALPHANUMERIC" , 
		"CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER",
		"CHARACTER", "DD/MM/YYYY", "CHARACTER", "CHARACTER", "ALPHANUMERIC",
		"ALPHANUMERIC", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER","SILENT", "DD/MM/YYYY", "CHARACTER", "CHARACTER", "CHARACTER" , "CHARACTER", "CHARACTER", "CHARACTER",
		"DD/MM/YYYY", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER"},
		{ "MANDATORY", "Yes - unless assigned on upload", "No", "No", "No", "No", "No", "No", "No", "No", "No", "Yes", "No", "No", "No", "No", "No",  "No", 
			"No", "No", "No", "No", "No", "No", "No", "No",
			"No", "No", "No", "No", "No",
			"No", "No", "No", "No", "No","No", "No", "No", "No", "No" , "No" , "No", "No",
			"No", "No", "No" , "No" , "No", "No"},
		{ "VALID VALUES", "", "", "Unknown, Br, Capt, Col, Cpl, Dean, Dr, Fr, Lac, Major, Miss, Mr, Mrs, Ms, Past, Prof, Pstr, Rev, Sir, Sr", "", "", "", "", "", "Alive, Deceased, Unknown",
		"Male, Female, Unknown", "Subject, Prospect, Withdrawn Subject, Archive, Inactive", "", "", "", "Married, Single, Divorced, Unknown", "Email, Home telephone, Mobile telephone, Post", "", 
		"", "", "", "Refer to user interface for options", "Refer to user interface for options", "",  //last three on this line are state country and postcode - i really could enforce something 
		"", "Current,Current - Alternative,Current - Under Investigation,Incorrect address,Valid past address", "Postal,Work,Residential", "", "", "", "",
		"", "Mobile,Home,Work", "Unknown,Current,Current Alternative,Current Under Investigation,Valid Past,Incorrect or Disconnected", "", "","Yes,No", "", "", "", "" , "", "Unknown, Verified, Unverified, Bounced", "Unknown, Verified, Unverified, Bounced",
		"", "Consented, Ineligible, Refused, Withdrawn, Pending", "Electronic, Hard Copy" , "Yes, No, Pending, Unavailable, Limited, Revoked" , "Yes, No, Pending, Unavailable, Limited, Revoked", "Yes, No, Pending, Unavailable, Limited, Revoked"},
		{ "NOTE: Remove this first column, and replace the contents of rows 2 to 6", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", "", 
			"", "", "", "", "", "", "", "",
			"", "", "", "", "",
			"", "", "", "", "","", "", "" , "", "" , "" , "" , "",
			"", "", "", "", "","", ""} };


	// 1 digit, 1 lower, 1 upper, 1 symbol "~!@#$%^&*()_-+={}[]:;\"<>|", from 6 to 20
	public static final String			PASSWORD_PATTERN												= "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*()_\\-\\+\\=\\{\\}\\[\\]:;\\\"<>|]).{6,20})";
	public static final String			SUBJECT_DEMOGRAPHIC_DATA = 	"Subject Demographic Data";
	public static final String			SUBJECT_CONSENT_DATA = 	"Subject Consent Data";
	public static final String			PEDIGREE_DATA = 	"Pedigree Data";
	public static final String			STUDY_SPECIFIC_CUSTOM_DATA = "Study-specific (custom) Data";
	public static final String			FILENAME	= "fileName";
	public static final String			SUBJECT_ATTACHMENT_DATA	= "Subject Attachment Data";
	
	public static final String PEDIGREE_INDIVIDUAL_ID="pedigree.individualId";
	public static final String PEDIGREE_FIRST_NAME="pedigree.firstName";
	public static final String PEDIGREE_LAST_NAME="pedigree.lastName";
	public static final String PEDIGREE_DOB="pedigree.dob";
	public static final String PEDIGREE_RELATIONSHIP="pedigree.relationship";
	public static final String PEDIGREE_TWIN="pedigree.twin";
	public static final String PEDIGREE_RELATIONSHIP_DELETE="pedigree.relationship.delete";
	public static final String PEDIGREE_INBREED="pedigree.inbreed";
	
	public static final String MALE = "male";
	public static final String FEMALE = "female";
	
	public static final List<String> TWIN_LIST = new ArrayList<String>(){
			{
				add("N/A");
				add("MZ");
				add("DZ");
		   }
	};
	
	public static final String MADELINE_PEDIGREE_TEMPLATE="pedigree_template";
	public static final String PEDIGREE_TEMPLATE_EXT="txt";
	public static final String ARK_PEDIGREE_TEMPLATE="ark_pedigree_template";
	
	public static final String ARK_SUBJECT_ATTACHEMENT_DIR="attachments";
	public static final String ARK_SUBJECT_CORRESPONDENCE_DIR="correspondence";
	public static final String ARK_STUDY_COMPONENT_DIR="studycomponent";
	
	public static final String PHONE_DETAIL_PANEL="phoneDetailPanel";
	public static final String ADDRESS_DETAIL_PANEL="addressDetailPanel";
	
	public static final String INBREED_ALLOWED="inbreedAllowed";
	
	public static final String STUDY_CALENDAR_ID = "studyCalendar.id";
	public static final String STUDY_CALENDAR_NAME = "studyCalendar.name";
	public static final String STUDY_CALENDAR_START_DATE = "studyCalendar.startDate";
	public static final String STUDY_CALENDAR_END_DATE = "studyCalendar.endDate";
	public static final String STUDY_CALENDAR_DESCRIPTION = "studyCalendar.description";
	public static final String STUDY_CALENDAR_STUDY_COMP = "studyCalendar.studyComp";
	
	public static final Integer MAXIMUM_ACCEPTABLE_AGE = 150;
	
	public static final String FILE="file";
	
	public static final String ARK_SUBJECT_CONSENT_DIR="consent";
	
	
	
}
