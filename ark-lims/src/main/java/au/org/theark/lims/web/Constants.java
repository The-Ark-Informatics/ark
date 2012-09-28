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
package au.org.theark.lims.web;

public class Constants {
	public static final String			UPLOAD_STATUS_OF_AWAITING_VALIDATION				= "AWAITING_VALIDATION";
	public static final String			UPLOAD_STATUS_OF_COMPLETED								= "COMPLETED";
	public static final String			UPLOAD_STATUS_OF_ERROR_IN_DATA_VALIDATION			= "ERROR_IN_DATA_VALIDATION";
	public static final String			UPLOAD_STATUS_OF_ERROR_IN_FILE_VALIDATION			= "ERROR_IN_FILE_VALIDATION";
	public static final String			UPLOAD_STATUS_OF_ERROR_ON_DATA_IMPORT				= "ERROR_ON_DATA_IMPORT";
	public static final String			UPLOAD_STATUS_OF_STATUS_NOT_DEFINED					= "STATUS_NOT_DEFINED";
	public static final String			UPLOAD_STATUS_OF_VALIDATED								= "VALIDATED";
	
	public static final String	LIMS_MAIN_TAB									= "LIMS";

	/* Tab names */
	public static final String	MENU_LIMS_SUBMENU								= "limsSubMenus";
	public static final String	TAB_BIO_COLLECTION							= "Collection";
	public static final String	TAB_BIOSPECIMEN								= "Biospecimen";
	public static final String	TAB_INVENTORY									= "Inventory";

	/* Resource Keys */
	public static final String	TAB_MODULE_LIMS								= "tab.module.lims";
	public static final String	TAB_MODULE_LIMS_COLLECTION					= "tab.module.lims.collection";
	public static final String	TAB_MODULE_BIOSPECIMEN						= "tab.module.lims.biospecimen";

	/* Session items */
	public static final String	BIO_COLLECTION									= "BIO_COLLECTION";
	public static final String	BIOSPECIMEN										= "BIOSPECIMEN";

	public static final String	LIMS_SERVICE									= "limsService";
	public static final String	LIMS_SUBJECT_SERVICE							= "limsSubjectService";
	public static final String	LIMS_INVENTORY_SERVICE						= "limsInventoryService";
	public static final String	LIMS_ADMIN_SERVICE							= "limsAdminService";

	public static final String	ADMIN_TAB										= "Administration";
	public static final String	STUDY_TAB										= "Study";
	public static final String	SUBJECT_TAB										= "Subject";
	public static final String	STUDY_DETAILS									= "Study Details";
	public static final String	SUB_STUDIES										= "Sub Studies";
	public static final String	SITES												= "Sites";
	public static final String	CONSENT_SECTIONS								= "Consent Sections";
	public static final String	STUDY_COMPONENTS								= "Study Components";
	public static final String	USERS												= "Users";
	public static final String	MY_DETAILS										= "My Details";
	public static final String	STUDY_ADMIN										= "Study Admin";
	public static final String	PARTICIPANT_MANAGEMENT						= "Participant Management";
	public static final String	USER_NAME										= "userName";
	public static final String	FIRST_NAME										= "firstName";
	public static final String	LAST_NAME										= "lastName";
	public static final String	EMAIL												= "email";
	public static final String	PHONE_NUMBER									= "phoneNumber";
	public static final String	PASSWORD											= "password";
	public static final String	OLD_PASSWORD									= "oldPassword";
	public static final String	CONFIRM_PASSWORD								= "confirmPassword";
	public static final String	SAVE												= "save";
	public static final String	SAVEANDCLOSE									= "saveAndClose";
	public static final String	CLOSE												= "Close";
	public static final String	CANCEL											= "cancel";
	public static final String	DELETE											= "delete";
	public static final String	COPY												= "copy";
	public static final String	ARCHIVE											= "archive";
	public static final String	SEARCH_USER_FORM								= "searchUserForm";
	public static final String	SEARCH_FORM										= "searchForm";
	public static final String	SEARCH											= "search";
	public static final String	NEW												= "new";
	public static final String	RESET												= "reset";
	public static final String	ADD_PHONE										= "addPhone";
	public static final String	EDIT												= "edit";
	public static final int		MODE_NEW											= 1;
	public static final int		MODE_EDIT										= 2;
	public static final int		MODE_READ										= 3;

	/* Person */
	public static final String	PERSON_PREFERRED_EMAIL						= "linkSubjectStudy.person.preferredEmail";
	public static final String	PERSON_OTHER_EMAIL							= "linkSubjectStudy.person.otherEmail";
	public static final String	PERSON_PREFERRED_NAME						= "linkSubjectStudy.person.preferredName";
	public static final String	PERSON_VITAL_STATUS							= "linkSubjectStudy.person.vitalStatus";
	public static final String	PERSON_PERSON_ID								= "linkSubjectStudy.person.id";
	public static final String	SUBJECT_UID										= "linkSubjectStudy.subjectUID";
	public static final String	PERSON_FIRST_NAME								= "linkSubjectStudy.person.firstName";
	public static final String	PERSON_MIDDLE_NAME							= "linkSubjectStudy.person.middleName";
	public static final String	PERSON_LAST_NAME								= "linkSubjectStudy.person.lastName";
	public static final String	SUBJECT_PREVIOUS_LAST_NAME					= "subjectPreviousLastname";
	public static final String	PERSON											= "person";
	public static final String	GENDER_TYPE										= "genderType";
	public static final String	ID													= "id";
	public static final String	NAME												= "name";
	public static final String	SUBJECT_STATUS									= "linkSubjectStudy.subjectStatus";
	public static final String	SUBJECT_STATUS_ID								= "id";
	public static final String	VITAL_STATUS									= "vitalStatus";
	public static final String	STATUS_NAME										= "statusName";
	public static final String	PERSON_MARITAL_STATUS						= "linkSubjectStudy.person.maritalStatus";
	public static final String	PERSON_CONTACT_METHOD						= "linkSubjectStudy.person.personContactMethod";
	public static final String	PERSON_DOB										= "linkSubjectStudy.person.dateOfBirth";
	public static final String	PERSON_DOD										= "linkSubjectStudy.person.dateOfDeath";
	public static final String	PERSON_CONSENT_DATE							= "linkSubjectStudy.consentDate";
	public static final String	SUBJECT_CONSENT_STATUS						= "linkSubjectStudy.consentStatus";
	public static final String	SUBJECT_CONSENT_TYPE							= "linkSubjectStudy.consentType";
	public static final String	PERSON_CAUSE_OF_DEATH						= "linkSubjectStudy.person.causeOfDeath";
	public static final String	PERSON_TYTPE_TYPE								= "linkSubjectStudy.person.titleType";
	public static final String	PERSON_GENDER_TYPE							= "linkSubjectStudy.person.genderType";
	public static final String	PERSON_GENDER_TYPE_NAME						= "linkSubjectStudy.person.genderType.name";
	public static final String	SUBJECT_FULL_NAME								= "subjectFullName";
	public static final String	PERSON_VITAL_STATUS_NAME					= "linkSubjectStudy.person.vitalStatus.statusName";
	public static final String	SUBJECT_STATUS_NAME							= "linkSubjectStudy.person.subjectStatus.name";
	public static final String	EVEN												= "even";
	public static final String	ODD												= "odd";
	public static final String	CLASS												= "class";
	public static final String	SUBJECT_KEY_LBL								= "subjectKeyLbl";
	public static final String	SUBJECT_LIST									= "subjectList";
	public static final String	PHONE_LIST										= "phoneList";
	public static final String	PHONE_NUMBER_VALUE							= "phoneNumberValue";
	public static final String	CONSENT_LIST									= "consentList";
	public static final String	CONSENT_COMPONENT_LABEL						= "consentComponentLabel";

	public static final String	STATE_SELECTOR_WMC					= "stateSelector";										// WebMarkupContainer that
	// will house the DropDowns
	// for States
	public static final String	SUBJECT_STATE									= "linkSubjectStudy.state";
	public static final String	SUBJECT_OTHER_STATE							= "linkSubjectStudy.otherState";
	public static final String	SUBJECT_COUNTRY								= "linkSubjectStudy.country";
	public static final String	SUBJECT_AMDRIFID								= "linkSubjectStudy.amdrifId";
	public static final String	SUBJECT_YR_FIRST_MAMMOGRAM					= "linkSubjectStudy.yearOfFirstMamogram";
	public static final String	SUBJECT_YR_RECENT_MAMMOGRAM				= "linkSubjectStudy.yearOfRecentMamogram";
	public static final String	SUBJECT_TOTAL_MAMMOGRAM						= "linkSubjectStudy.totalNumberOfMamograms";
	public static final String	SUBJECT_SITE_ADDRESS							= "linkSubjectStudy.siteAddress";
	public static final String	SUBJECT_CITY									= "linkSubjectStudy.city";
	public static final String	SUBJECT_POST_CODE								= "linkSubjectStudy.postCode";
	public static final String	SUBJECT_CONSENT_TO_ACTIVE_CONTACT		= "linkSubjectStudy.consentToActiveContact";
	public static final String	SUBJECT_CONSENT_TO_USEDATA					= "linkSubjectStudy.consentToUseData";
	public static final String	SUBJECT_CONSENT_PASSIVE_DATA_GATHER		= "linkSubjectStudy.consentToPassiveDataGathering";
	public static final String	SUBJECT_AUTO_GENERATED						= "Auto-generated";
	public static final String	SUBJECTUID_EXAMPLE							= "AAA-000000001";
	public static final String	BIOCOLLECTION_NAME_EXAMPLE					= "AAA-000000001";
	public static final String	BIOSPECIMENUID_EXAMPLE						= "AAA-000000001";

	public static final String	BIOTRANSACTION_STATUS_INITIAL_QUANTITY	= "Initial Quantity";

	public static final String	BIOSPECIMEN_PROCESSING_PROCESSING		= "Processing";
	public static final String	BIOSPECIMEN_PROCESSING_ALIQUOTING		= "Aliquoting";

	public static final String	SUBJECTUIDSEQ_STUDYNAMEID					= "studyNameId";

	public static final String	AUTO_GENERATED									= "Auto-generated";
	public static final String	MANUAL_UIDS_NEXT_RELEASE									= "Manual UIDs next release";
	

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
	public static final String			UPLOAD_FILE_FORMAT										= "fileFormat";
	public static final String			UPLOAD_DELIMITER_TYPE									= "delimiterType";
	public static final String			UPLOAD_FILENAME											= "filename";
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
	
	/* General ones */
	public static final String			PAGE_SEARCH												= "page.search";
	public static final String			PAGE_NEW												= "page.new";
	public static final String			CANCEL_KEY												= "cancelKey";
	public static final String			SAVE_KEY												= "saveKey";
	public static final String			DELETE_KEY												= "deleteKey";
	public static final String			STUDY													= "study";
}
