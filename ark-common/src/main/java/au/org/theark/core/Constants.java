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
package au.org.theark.core;

/*
 * Common constants that apply to
 *  all of the Ark modules
 */
public class Constants {
	/* Module Role Display Names */
	public static final String DISPLAY_ROLE_STUDY_ADMIN = "Study Admin";
	public static final String DISPLAY_ROLE_ORDINARY_USER = "Ordinary User";
	public static final String DISPLAY_ROLE_POWER_USER = "Power User";
	public static final String DISPLAY_ROLE_LAB_PERSON = "Lab Person";
	public static final String DISPLAY_ROLE_WADB_ADMIN = "WADB Admin";
	public static final String DISPLAY_ROLE_WADB_PERSON = "WADB Person";
	public static final String DISPLAY_ROLE_SUPER_ADMIN = "Super Admin";

	public static final String ROLE_STUDY_ADMIN = "study_administrator";
	public static final String ROLE_SUPER_ADMIN = "super_administrator";
	public static final String ROLE_ORDINARY_USER = "ordinary_user";
	public static final String ROLE_POWER_USER = "power_user";
	public static final String ROLE_LAB_PERSON = "lab_person";
	public static final String ROLE_WADB_ADMINISTRATOR = "wadb_administrator";
	public static final String ROLE_WADB_PERSON = "wadb_person";

	/* Module Names for display */
	public static final String DISP_MODULE_ARK = "ARK";
	public static final String DISP_MODULE_STUDY_MANAGER = "Study Management";
	public static final String DISP_MODULE_SUBJECT = "Subject Management";
	public static final String DISP_MODULE_GWAS = "Genotypic";
	public static final String DISP_MODULE_PHENOTYPIC = "Phenotypic";
	public static final String DISP_MODULE_REGISTRY = "Registry";
	// TODO: All wager_lab references to become lims
	public static final String DISP_MODULE_WAGER_LAB = "LIMS";
	public static final String DISP_MODULE_LIMS = "LIMS";
	public static final String DISP_MODULE_DATA_ANALYSIS = "Data Extraction";

	/* Module Names stored in the system */
	public static final String MODULE_STUDY_MANAGER = "study_manager";
	public static final String MODULE_ARK = "ark";
	public static final String MODULE_SUBJECT = "subject";
	public static final String MODULE_GWAS = "gwas";
	public static final String MODULE_PHENOTYPIC = "phenotypic";
	public static final String MODULE_REGISTRY = "registry";
	// TODO: All wager_lab references to become lims
	public static final String MODULE_WAGER_LAB = "wager_lab";
	public static final String MODULE_LIMS = "lims";
	// TODO: data_analysis should be data_extract ??
	public static final String MODULE_DATA_ANALYSIS = "data_analysis";

	/* Common Service Names */
	public static final String ARK_COMMON_SERVICE = "arkCommonService";
	// public static final String ARK_STUDY_SERVICE = "StudyService";
	public static final String ARK_CSV_LOADER_SERVICE = "arkCsvLoaderService";

	/* Global constants */
	public static final String STUDY_CONTEXT_ID = "studyId";
	public static final String PERSON_CONTEXT_ID = "personId";
	public static final String PERSON_TYPE = "personType";
	public static final String PERSON_CONTEXT_TYPE_SUBJECT = "subject";
	public static final String PERSON_CONTEXT_TYPE_CONTACT = "contact";
	public static final String PERSON_CONTEXT_CONSENT_ID = "consentId";
	public static final String MODULE_CONTEXT_NAME = "moduleName";

	/* Menu tabs */
	public static final String MENU_SUBJECT_SUBMENU = "subjectSubMenus";
	public static final String TAB_SUBJECT_DETAIL = "Participant Detail";
	public static final String TAB_PERSON_PHONE = "Phone";
	public static final String TAB_PERSON_ADDRESS = "Address";
	public static final String TAB_SUBJECT_CONSENT = "Consent";
	public static final String TAB_SUBJECT_SUBJECT_FILE = "Attachments";
	public static final String TAB_SUBJECT_SUBJECT_UPLOAD = "Subject Upload";
	public static final String TAB_SUBJECT_CORRESPONDENCE = "Correspondence";

	/* Menu Tab keys */
	public static final String TAB_MODULE_SUBJECT_DETAIL = "tab.module.subject.detail";
	public static final String TAB_MODULE_PERSON_PHONE = "tab.module.person.phone";
	public static final String TAB_MODULE_PERSON_ADDRESS = "tab.module.person.address";
	public static final String TAB_MODULE_SUBJECT_CONSENT = "tab.module.subject.consent";
	public static final String TAB_MODULE_SUBJECT_SUBJECT_FILE = "tab.module.subject.subjectFile";
	public static final String TAB_MODULE_SUBJECT_SUBJECT_UPLOAD = "tab.module.subject.subjectUpload";
	public static final String TAB_MODULE_SUBJECT_CORRESPONDENCE = "tab.module.subject.correspondence";
	public static final String TAB_MODULE_LIMS_SUBJECT_DETAIL = "tab.module.lims.subject.detail";

	/* General date format, as used by ArkDatePicker */
	public static final String DD_MM_YYYY = "dd/MM/yyyy";
	public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy hh:mm:ss";

	/* Phenotypic data file column names */
	public static final String SUBJECTUID = "SUBJECTUID";
	public static final String DATE_COLLECTED = "DATE_COLLECTED";

	/* Tab Panel Names/Keys */
	public static final String TAB_MODULE_STUDY_DETAIL = "tab.module.study.details";
	public static final String STUDY_DETAIL = "Study Detail";
	public static final String SITE = "Site";
	public static final String TAB_MODULE_SITE = "tab.module.sites";
	public static final String STUDY_COMPONENT = "Study Component";
	public static final String TAB_MODULE_STUDY_COMPONENT = "tab.module.study.components";
	public static final String USER = "User";
	public static final String TAB_MODULE_USER = "tab.module.users";
	public static final String TAB_MODULE_USER_MANAGEMENT = "tab.module.user.management";
	public static final String MY_DETAIL = "My Detail";
	public static final String TAB_MODULE_MY_DETAIL = "tab.module.mydetails";
	public static final String SUBJECT = "Subject";
	public static final String TAB_MODULE_SUBJECT = "tab.module.subject";
	public static final String MENU_STUDY_SUBMENU = "studySubMenus";
	public static final String CUSTOM_FIELD = "Custom Field";
	public static final String TAB_CUSTOM_FIELD = "tab.custom.field";
	public static final String MENU_REGISTRY_SUBMENU = "registrySubMenus";

	/*
	 * People Menu
	 */
	public static final String PEOPLE = "People";
	public static final String TAB_PEOPLE_FIELD = "tab.people";
	public static final String STUDY_KEY = "id";

	public static final String STUDY_NAME = "name";
	public static final String DATE_OF_APPLICATION = "dateOfApplication";
	public static final String EST_YEAR_OF_COMPLETION = "estimatedYearOfCompletion";
	public static final String CHIEF_INVESTIGATOR = "chiefInvestigator";
	public static final String CONTACT_PERSON = "contactPerson";
	public static final String STUDY_STATUS = "studyStatus";
	public static final String STUDY_STATUS_ARCHIVE = "Archive";

	public static final String STUDY_SERVICE = "studyService";
	public static final String PHENO_SERVICE = "phenotypicService";
	public static final String LIMS_SERVICE = "limsService";
	public static final String STUDY_DAO = "studyDao";

	public static final String FIELD_TYPE_NUMBER = "NUMBER";
	public static final String FIELD_TYPE_CHARACTER = "CHARACTER";
	public static final String FIELD_TYPE_DATE = "DATE";
	public static final String DISCRETE_RANGE_TOKEN = ",";
	public static final String ENCODED_VALUES_TOKEN = ";";
	public static final String ENCODED_VALUES_SEPARATOR = "=";

	/* Button names/labels */
	public static final String SEARCH = "search";
	public static final String NEW = "new";
	public static final String RESET = "reset";
	public static final String SAVE = "save";
	public static final String CANCEL = "cancel";
	public static final String DELETE = "delete";
	public static final String EDIT = "edit";
	public static final String EDIT_CANCEL = "editCancel";
	public static final String SEARCH_RESULTS = "searchResults";
	public static final String OK = "ok";
	public static final String FIRST = "first";
	public static final String PREVIOUS = "previous";
	public static final String NEXT = "next";
	public static final String LAST = "last";
	public static final String FINISH = "finish";
	public static final String DONE = "done";
	public static final String REMOVE = "remove";
	/* Delete confirmation */
	public static final String DELETE_CONFIRM_TITLE = "Delete confirmation";
	public static final String DELETE_CONFIRM_MESSAGE = "Are you sure you want to delete?";

	public static final String SEARCH_FORM = "searchForm";

	/* Form modes (view/edit) */
	public static final int MODE_NEW = 1;
	public static final int MODE_EDIT = 2;
	public static final int MODE_READ = 3;

	/* Common import field delimiters */
	public static final char IMPORT_DELIM_CHAR_COMMA = ',';
	public static final char IMPORT_DELIM_CHAR_TAB = '	';

	/* Search result list rows per page */
	public static final int ROWS_PER_PAGE = 10;
	public static final String RESULT_LIST = "resultList";

	/* Schema */
	public static final String STUDY_SCHEMA = "study";
	public static final String AUDIT_SCHEMA = "audit";
	public static final String ADMIN_SCHEMA = "admin";

	/* Default Country */
	public static final String DEFAULT_COUNTRY = "Australia";
	public static final String DEFAULT_COUNTRY_CODE = "AU";
	public static final String NO = "No";
	public static final String YES = "Yes";

	/* Action Type - Audit History */
	public static final String ACTION_TYPE_CREATED = "CREATED";
	public static final String ACTION_TYPE_UPDATED = "UPDATED";
	public static final String ACTION_TYPE_DELETED = "DELETED";
	public static final String ACTION_TYPE_ARCHIVED = "ARCHIVED";

	/* Entity Type */
	public static final String ENTITY_TYPE_STUDY = "Study";
	public static final String ENTITY_TYPE_STUDY_COMPONENT = "Study Component";
	public static final String ENTITY_TYPE_PHONE = "Phone";
	public static final String ENTITY_TYPE_SUBJECT = "Subject";
	public static final String ENTITY_TYPE_ADDRESS = "Address";
	public static final String ENTITY_TYPE_CONSENT = "Consent";
	public static final String ENTITY_TYPE_CONSENT_FILE = "Consent File";
	public static final String ENTITY_TYPE_USER = "User";
	public static final String ENTITY_TYPE_PERSON_LASTNAME_HISTORY = "Person Lastname History";
	public static final String ENTITY_TYPE_PHENO_COLLECTION = "Phenotypic Collection";
	public static final String ENTITY_TYPE_PHENO_COLLECTION_UPLOAD = "Pheno Collection Upload";
	public static final String ENTITY_TYPE_GENO_COLLECTION = "Geno Collection";
	public static final String ENTITY_TYPE_GENO_COLLECTION_IMPORT = "Geno Collection Import";
	public static final String ENTITY_TYPE_GENO_METADATA = "MetaData";
	public static final String ENTITY_TYPE_GENO_ENCODED_DATA = "Encoded data";
	public static final String ENTITY_TYPE_GENO_UPLOAD_COLLECTION = "Upload Collection";
	public static final String ENTITY_TYPE_GENO_METADATA_FIELD = "Meta Data Field";
	public static final String ENTITY_TYPE_FIELD = "Field";
	public static final String ENTITY_TYPE_FIELD_DATA = "Field Data";
	public static final String ENTITY_TYPE_PHENO_UPLOAD = "Upload";
	public static final String ENTITY_TYPE_SUBJECT_FILE = "Subject File";
	public static final String ENTITY_TYPE_STUDY_UPLOAD = "Study Upload";
	public static final String ENTITY_TYPE_CUSTOM_FIELD = "Custom Field";
	public static final String ENTITY_TYPE_CUSTOM_FIELD_DISPLAY = "Custom Field Display";
	public static final String ENTITY_TYPE_CUSTOM_FIELD_GROUP = "Custom Field Group";
	public static final String ENTITY_TYPE_INV_SITE = "InvSite";
	public static final String ENTITY_TYPE_INV_FREEZER = "InvFreezer";
	public static final String ENTITY_TYPE_INV_RACK = "InvRack";
	public static final String ENTITY_TYPE_INV_BOX = "InvBox";
	public static final String ENTITY_TYPE_INV_CELL = "InvCell";

	/* Audit history actions */
	public static final String ACTION_INSERT = "Insert";
	public static final String ACTION_UPDATE = "Update";
	public static final String ACTION_DELETE = "Delete";

	public static final String PERSON_SURNAME_HISTORY_PERSON = "person";
	public static final String PERSON_SURNAME_HISTORY_SURNAME = "surname";

	/* Ark Function Id */
	public static final String ARK_FUNCTION_KEY = "ARK_FUNCTION_KEY";

	/*
	 * Ark Function Key value that maps to the database table values in
	 * ark_usecase.If the values in the table change, this must be modified.
	 */
	public static final String FUNCTION_KEY_VALUE_STUDY = "STUDY";
	public static final String FUNCTION_KEY_VALUE_STUDY_COMPONENT = "STUDY_COMPONENT";
	public static final String FUNCTION_KEY_VALUE_MY_DETAIL = "MY_DETAIL";
	public static final String FUNCTION_KEY_VALUE_USER = "USER";
	public static final String FUNCTION_KEY_VALUE_SUBJECT = "SUBJECT";
	public static final String FUNCTION_KEY_VALUE_PHONE = "PHONE";
	public static final String FUNCTION_KEY_VALUE_CONSENT = "CONSENT";
	public static final String FUNCTION_KEY_VALUE_ATTACHMENT = "ATTACHMENT";
	public static final String FUNCTION_KEY_VALUE_ADDRESS = "ADDRESS";
	public static final String FUNCTION_KEY_VALUE_SUBJECT_UPLOAD = "SUBJECT_UPLOAD";
	public static final String FUNCTION_KEY_VALUE_SUBJECT_CORRESPONDENCE = "CORRESPONDENCE";
	public static final String FUNCTION_KEY_VALUE_REPORT = "REPORT";
	public static final String FUNCTION_KEY_VALUE_PHENO_SUMMARY = "SUMMARY";
	public static final String FUNCTION_KEY_VALUE_DATA_DICTIONARY = "DATA_DICTIONARY";
	public static final String FUNCTION_KEY_VALUE_DATA_DICTIONARY_UPLOAD = "DATA_DICTIONARY_UPLOAD";
	public static final String FUNCTION_KEY_VALUE_PHENO_COLLECTION = "PHENO_COLLECTION";
	public static final String FUNCTION_KEY_VALUE_FIELD_DATA = "FIELD_DATA";
	public static final String FUNCTION_KEY_VALUE_FIELD_DATA_UPLOAD = "FIELD_DATA_UPLOAD";
	public static final String FUNCTION_KEY_VALUE_LIMS_SUBJECT = "LIMS_SUBJECT";
	public static final String FUNCTION_KEY_VALUE_LIMS_COLLECTION = "LIMS_COLLECTION";
	public static final String FUNCTION_KEY_VALUE_BIOSPECIMEN = "BIOSPECIMEN";
	public static final String FUNCTION_KEY_VALUE_INVENTORY = "INVENTORY";
	public static final String FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD = "SUBJECT_CUSTOM_FIELD";
	public static final String FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_DATA = "SUBJECT_CUSTOM_DATA";
	public static final String FUNCTION_KEY_VALUE_MODULE = "MODULE";
	public static final String FUNCTION_KEY_VALUE_FUNCTION = "FUNCTION";
	public static final String FUNCTION_KEY_VALUE_MODULE_FUNCTION = "MODULE_FUNCTION";
	public static final String FUNCTION_KEY_VALUE_ROLE = "ROLE";
	public static final String FUNCTION_KEY_VALUE_MODULE_ROLE = "MODULE_ROLE";
	public static final String FUNCTION_KEY_VALUE_ROLE_POLICY_TEMPLATE = "ROLE_POLICY_TEMPLATE";
	public static final String FUNCTION_KEY_VALUE_LIMS_COLLECTION_CUSTOM_FIELD = "LIMS_COLLECTION_CUSTOM_FIELD";
	public static final String FUNCTION_KEY_VALUE_LIMS_COLLECTION_CUSTOM_DATA = "LIMS_COLLECTION_CUSTOM_DATA";
	public static final String FUNCTION_KEY_VALUE_BIOSPECIMEN_CUSTOM_FIELD = "BIOSPECIMEN_CUSTOM_FIELD";
	public static final String FUNCTION_KEY_VALUE_BIOSPECIMEN_CUSTOM_DATA = "BIOSPECIMEN_CUSTOM_DATA";
	public static final String FUNCTION_KEY_VALUE_BIOSPECIMENUID_TEMPLATE = "BIOSPECIMENUID_TEMPLATE";
	public static final String FUNCTION_KEY_VALUE_BARCODE_PRINTER = "BARCODE_PRINTER";
	public static final String FUNCTION_KEY_VALUE_BARCODE_LABEL = "BARCODE_LABEL";
	public static final String FUNCTION_KEY_VALUE_BIOSPECIMEN_UPLOAD = "BIOSPECIMEN_UPLOAD";
	public static final String FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_UPLOAD = "SUBJECT_CUSTOM_FIELD_UPLOAD";
	public static final String FUNCTION_KEY_VALUE_BIOCOLLECTION_CUSTOM_FIELD_UPLOAD = "BIOCOLLECTION_CUSTOM_FIELD_UPLOAD";
	public static final String FUNCTION_KEY_VALUE_BIOSPECIMEN_CUSTOM_FIELD_UPLOAD = "BIOSPECIMEN_CUSTOM_FIELD_UPLOAD";
	public static final String FUNCTION_KEY_VALUE_BIOSPECIMEN_AND_BIOCOLLECTION_CUSTOM_FIELD_UPLOAD = "BIOSPECIMEN_AND_BIOCOLLECTION_CUSTOM_FIELD_UPLOAD";

	/* Ark Module Keys */
	public static final String ARK_MODULE_KEY = "MODULE_KEY";

	/* Ark Module Key values */
	public static final String ARK_MODULE_STUDY = "Study";
	public static final String ARK_MODULE_SUBJECT = "Subject";
	public static final String ARK_MODULE_USER = "User";
	public static final String ARK_MODULE_PHENOTYPIC = "Phenotypic";
	public static final String ARK_MODULE_GENOTYPIC = "Genotypic";
	public static final String ARK_MODULE_LIMS = "LIMS";
	public static final String ARK_MODULE_REGISTRY = "Registry";
	public static final String ARK_MODULE_REPORTING = "Reporting";
	public static final String ARK_MODULE_ADMIN = "Admin";
	public static final String ARK_MODULE_WORKTRACKING = "Work Tracking";

	public static final char DEFAULT_DELIMITER_CHARACTER = ',';
	public static final String DEFAULT_FILE_FORMAT = "CSV";

	public static final String STUDY = "study";
	public static final String NO_STUDY_IN_CONTEXT_MESSAGE = "There is no study in context. Please select a Study.";
	public static final String MODULE_NOT_ACCESSIBLE_MESSAGE = "You do not have the required security privileges to work with this function. Please see your Administrator.";

	/* Session Objects */
	public static final String SESSION_STUDY_KEY = "SESSION_STUDY";
	public static final String SESSION_STUDY_MODULES_KEY = "SESSION_STUDY_MODULES";
	public static final String SESSION_SUBJECT_KEY = "SESSION_SUBJECT";

	/* Ark Function Types */
	public static final String ARK_FUNCTION_TYPE_REPORT = "REPORT";
	public static final String ARK_FUNCTION_TYPE_NON_REPORT = "NON-REPORT";

	/* Default Role for Study Module */
	public static final String ARK_STUDY_DEFAULT_ROLE = "Study Read-Only user";
	public static final int PALETTE_ROWS = 5;

	/* Default "from" paramter for emails */
	public static final String ARK_ADMIN_EMAIL = "admin@the-ark.org.au";

	public static final String[] DATA_DICTIONARY_HEADER = { "FIELD_NAME",
			"FIELD_TYPE", "DESCRIPTION", "QUESTION", "UNITS", "ENCODED_VALUES",
			"MINIMUM_VALUE", "MAXIMUM_VALUE", "MISSING_VALUE" }; //ALLOW MULTI  not here because it is Questionairre/customfieldgroup based
	public static final String[] CUSTOM_FIELD_UPLOAD_HEADER = { "FIELD_NAME",
			"FIELD_TYPE", "DESCRIPTION", "QUESTION", "UNITS", "ENCODED_VALUES",
			"MINIMUM_VALUE", "MAXIMUM_VALUE", "MISSING_VALUE", "REQUIRED", "ALLOW_MULTIPLE_SELECTIONS"  };

	public static final String NAME = "name";
	public static final String ID = "id";

	/* Ark Session Details */
	public static final String ARK_SESSION_ID = "ARK_SESSION_ID";
	public static final String ARK_USERID = "ARK_USERID";
	public static final String ARK_HOST = "ARK_HOST";
	public static final String ARK_SESSION_START_TIMESTAMP = "ARK_SESSION_START_TIMESTAMP";
	public static final String ARK_SESSION_LAST_ACCESS_TIME = "ARK_SESSION_LAST_ACCESS_TIME";
	public static final String ARK_SESSION_ACTION = "ARK_SESSION_ACTION";
	public static final String ARK_LOGOFF_SESSION_ID = "ARK_LOGOFF_SESSION_ID";

	public static final String UPLOADVO_UPLOAD_ID = "upload.id";
	public static final String UPLOADVO_UPLOAD_COLLECTION_UPLOAD = "upload.collectionUpload";
	public static final String UPLOADVO_UPLOAD_COLLECTION_UPLOAD_ID = "upload.collectionUpload.id";
	public static final String UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION = "upload.collectionUpload.collection";
	public static final String UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION_ID = "upload.collectionUpload.collection.id";
	public static final String UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION_NAME = "upload.collectionUpload.collection.name";
	public static final String UPLOADVO_UPLOAD_FILENAME = "upload.filename";
	public static final String UPLOADVO_UPLOAD_FILE_FORMAT = "upload.fileFormat";
	public static final String UPLOADVO_UPLOAD_FILE_FORMAT_ID = "upload.fileFormat.id";
	public static final String UPLOADVO_UPLOAD_FILE_FORMAT_NAME = "upload.fileFormat.name";
	public static final String UPLOADVO_UPLOAD_PAYLOAD = "upload.payload";
	public static final String UPLOADVO_UPLOAD_DELIMITER_TYPE = "upload.delimiterType";
	public static final String UPLOADVO_UPLOAD_UPLOAD_TYPE = "upload.uploadType";
	public static final String UPLOADVO_UPLOAD_USER = "upload.user";
	public static final String UPLOADVO_UPLOAD_USER_ID = "upload.userId";
	public static final String UPLOADVO_UPLOAD_INSERT_TIME = "upload.insertTime";
	public static final String UPLOADVO_UPLOAD_UPDATE_USER = "upload.user";
	public static final String UPLOADVO_UPLOAD_UPDATE_USER_ID = "upload.user.id";
	public static final String UPLOADVO_UPLOAD_UPDATE_TIME = "upload.updateTime";
	public static final String UPLOADVO_UPLOAD_CHECKSUM = "upload.checksum";
	public static final String UPLOADVO_UPLOAD_START_TIME = "upload.startTime";
	public static final String UPLOADVO_UPLOAD_FINISH_TIME = "upload.finishTime";

	public static final String UPLOADVO_UPLOAD_UPLOAD_STATUS_NAME = "upload.uploadStatus.name";
	public static final String UPLOADVO_UPLOAD_UPLOAD_REPORT = "upload.uploadReport";
	public static final String UPLOADVO_UPLOAD_STUDY = "upload.study";

	public static final String UPLOAD = "upload";
	public static final String UPLOAD_ID = "id";
	public static final String UPLOAD_STUDY = "study";
	public static final String UPLOAD_FILE_FORMAT = "fileFormat";
	public static final String UPLOAD_DELIMITER_TYPE = "delimiterType";
	public static final String UPLOAD_FILENAME = "filename";
	public static final String UPLOAD_PAYLOAD = "payload";
	public static final String UPLOAD_USER = "user";
	public static final String UPLOAD_CHECKSUM = "checksum";
	public static final String DELIMITER_TYPE_ID = "id";
	public static final String DELIMITER_TYPE_NAME = "name";

	public static final String FILE_FORMAT = "fileFormat";
	public static final String FILE_FORMAT_ID = "id";
	public static final String FILE_FORMAT_NAME = "name";

	public static final String[] SUBJECT_TEMPLATE_HEADER = { "SUBJECTUID",
			"TITLE", "FIRST_NAME", "MIDDLE_NAME", "LAST_NAME",
			"PREFERRED_NAME", "DATE_OF_BIRTH", "VITAL_STATUS", "GENDER",
			"STATUS", "DATE_OF_DEATH", "CAUSE_OF_DEATH", "MARITAL_STATUS",
			"PREFERRED_CONTACT", "EMAIL" };
	public static final String[][] SUBJECT_TEMPLATE_CELLS = {
			{ "", "SUBJECTUID", "TITLE", "FIRST_NAME", "MIDDLE_NAME",
					"LAST_NAME", "PREFERRED_NAME", "DATE_OF_BIRTH",
					"VITAL_STATUS", "GENDER", "STATUS", "DATE_OF_DEATH",
					"CAUSE_OF_DEATH", "MARITAL_STATUS", "PREFERRED_CONTACT",
					"EMAIL" },
			{
					"DESCRIPTION",
					"The unique identifier assigned for this subject.  This may be automatically generated on upload into The Ark",
					"The title by which the subject prefers to be addressed",
					"The subjects first name", "The subjects middle name",
					"The subjects last or family name",
					"The name by which the subject prefers to be addressed",
					"The date the subject was born",
					"The subject's vital status", "The subject's gender",
					"The status of the subject as it pertains to the study",
					"The date the subject died, if known",
					"The cause of death", "The marital status of the subject",
					"The preferred method for contacting the subject",
					"The subject's primary email address" },
			{ "FORMAT", "CHARACTER", "CHARACTER", "CHARACTER", "CHARACTER",
					"CHARACTER", "CHARACTER", "DD/MM/YYYY", "CHARACTER",
					"CHARACTER", "CHARACTER", "DD/MM/YYYY", "ALPHANUMERIC",
					"CHARACTER", "CHARACTER", "ALPHANUMERIC" },
			{ "MANDATORY", "Yes - unless assigned on upload", "No", "No", "No",
					"No", "No", "No", "No", "No", "No", "No", "No", "No", "No",
					"No" },
			{
					"VALID VALUES",
					"",
					"Unknown, Br, Capt, Col, Cpl, Dean, Dr, Fr, Lac, Major, Miss, Mr, Mrs, Ms, Past, Prof, Pstr, Rev, Sir, Sr",
					"", "", "", "", "", "Alive, Deceased, Unknown",
					"Male, Female, Unknown",
					"Subject, Prospect, Withdrawn Subject, Archive", "", "",
					"Married, Single, Divorced, Unknown",
					"Email, Home telephone, Mobile telephone, Post", "" },
			{ "NOTE: Removed this first column, and replace rows 2 to 6", "",
					"", "", "", "", "", "", "", "", "", "", "", "", "", "" } };

	// 1 digit, 1 lower, 1 upper, 1 symbol "~!@#$%^&*()_-+={}[]:;\"<>|", from 6
	// to 20
	public static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*()_\\-\\+\\=\\{\\}\\[\\]:;\\\"<>|]).{6,20})";

	public static final String[] BIOSPECIMEN_TEMPLATE_HEADER = { "SUBJECTUID",
			"BIOSPECIMENUID", "BIOCOLLECTION", "SAMPLETYPE", "QUANTITY",
			"UNITS", "TREATMENT", "SITE", "FREEZER", "RACK", "BOX", "ROW",
			"COLUMN" };
	public static final String[][] BIOSPECIMEN_TEMPLATE_CELLS = {
			{ "", "SUBJECTUID", "BIOSPECIMENUID", "BIOCOLLECTION",
					"SAMPLETYPE", "QUANTITY", "UNITS", "TREATMENT", "SITE",
					"FREEZER", "RACK", "BOX", "ROW", "COLUMN" },
			{
					"DESCRIPTION",
					"The unique identifier assigned for this subject.",
					"The unique identifier of the biospecimen",
					"The collection to which the biospecimen is assoicated with",
					"The sample type of the biospecimen",
					"The quantity of the bispecimen", "The units of quanity",
					"The treatement type of the bispecimen",
					"The site of the bispecimen location",
					"The freezer of the bispecimen location",
					"The rack of the bispecimen location",
					"The box of the bispecimen location",
					"The row of the bispecimen location",
					"The column of the bispecimen location" },
			{ "MANDATORY", "Yes", "Yes", "Yes", "Yes", "Yes", "Yes", "Yes",
					"No", "No", "No", "No", "No", "No" },
			{
					"VALID VALUES",
					"",
					"",
					"",
					"",
					"",
					"70% Alcohol Fixed, Formalin Fixed, Frozen, RN later, RNA later, then Formalin Fixed, RNA later, then Snap Frozen, Tissue Cultured, Unprocessed",
					"", "", "", "", "", "", "" },
			{ "NOTE: Removed this first column, and replace rows 2 to 5", "",
					"", "", "", "", "", "", "", "", "", "", "", "" } };
	public static final String DOWNLOAD_FILE = "downloadFile";
	public static final String DELETE_FILE = "deleteFile";

	// Exception messages
	public static final String IO_EXCEPTION = "IOException: Input error. ";
	public static final String FILE_FORMAT_EXCEPTION = "File Format Exception: Input error. ";
	public static final String ARK_SYSTEM_EXCEPTION = "General ARK System Exception: ";
	public static final String ARK_BASE_EXCEPTION = "Base ARK System Exception: ";
	public static final String DOWNLOAD_REPORT = "downloadReport";
	public static final String SHIB_SESSION_ID = "SHIB_SESSION_ID";
	public static final String PHENO_COLLECTION_STATUS_UPLOADED = "Uploaded From File";

}
