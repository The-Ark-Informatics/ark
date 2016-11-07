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


	/* Common Service Names */
	public static final String ARK_COMMON_SERVICE = "arkCommonService";
	// public static final String ARK_STUDY_SERVICE = "StudyService";
	public static final String ARK_CSV_LOADER_SERVICE = "arkCsvLoaderService";
	public static final String ARK_DISEASE_SERVICE = "arkDiseaseService";
	public static final String ARK_AUDIT_SERVICE = "arkAuditService";
	public static final String ARK_PHENO_DATA_SERVICE = "phenotypicService";
	
	
	/* Global constants */
	public static final String STUDY_CONTEXT_ID = "studyId";
	public static final String PERSON_CONTEXT_ID = "personId";
	public static final String PERSON_TYPE = "personType";
	public static final String PERSON_CONTEXT_TYPE_SUBJECT = "subject";
	public static final String PERSON_CONTEXT_TYPE_CONTACT = "contact";
	public static final String PERSON_CONTEXT_CONSENT_ID = "consentId";
	public static final String MODULE_CONTEXT_NAME = "moduleName";
	public static final String	COPY												= "copy";

	/* Disease tabs */
	
	public static final String MENU_DISEASE_SUBMENU = "diseaseSubMenus";
	
	
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
	public static final String TAB_MODULE_SUBJECT_SEARCH = "tab.module.subject.search";
	public static final String TAB_MODULE_SUBJECT_DETAIL = "tab.module.subject.detail";
	public static final String TAB_MODULE_PERSON_PHONE = "tab.module.person.phone";
	public static final String TAB_MODULE_PERSON_ADDRESS = "tab.module.person.address";
	public static final String TAB_MODULE_PERSON_CONTACT = "tab.module.person.contact";
	public static final String TAB_MODULE_SUBJECT_CONSENT = "tab.module.subject.consent";
	public static final String TAB_MODULE_SUBJECT_SUBJECT_FILE = "tab.module.subject.subjectFile";
	//public static final String TAB_MODULE_SUBJECT_SUBJECT_UPLOAD = "tab.module.subject.subjectUpload";
	public static final String TAB_MODULE_STUDY_STUDY_DATA_UPLOAD = "tab.module.study.studyDataUpload";
	public static final String TAB_MODULE_SUBJECT_CORRESPONDENCE = "tab.module.subject.correspondence";
	public static final String TAB_MODULE_LIMS_SUBJECT_DETAIL = "tab.module.lims.subject.detail";

	/* General date format, as used by ArkDatePicker */
	public static final String DD_MM_YYYY = "dd/MM/yyyy";
	public static final String DD_MM_YYYY_HH_MM_SS = "dd/MM/yyyy hh:mm:ss";
	public static final String	yyyy_MM_dd_hh_mm_ss_S	= "yyyy-MM-dd hh:mm:ss.S";

	/* Phenotypic data file column names */
	public static final String SUBJECTUID = "SUBJECTUID";
	public static final String FAMILYUID = "FAMILYUID";
	public static final String NOT_SUBJECT_OR_FAMILY = "NOT_SUBJECT_OR_FAMILY";
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
	//public static final String PHENO_SERVICE = "phenotypicService";
	public static final String LIMS_SERVICE = "limsService";
	public static final String STUDY_DAO = "studyDao";

	public static final String FIELD_TYPE_NUMBER = "NUMBER";
	public static final String FIELD_TYPE_CHARACTER = "CHARACTER";
	public static final String FIELD_TYPE_DATE = "DATE";
	public static final String FIELD_TYPE_LOOKUP = "LOOKUP";
	public static final String DISCRETE_RANGE_TOKEN = ",";
	public static final String ENCODED_VALUES_TOKEN = ";";
	public static final String ENCODED_VALUES_FROM_TELEFORMS_TOKEN_SPACE = " ";
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
	public static final String VIEW = "view";
	public static final String FATHER = "father";
	public static final String MOTHER = "mother";
	public static final String TWIN = "twin";
	public static final String EXPORT = "export";
	public static final String CONFIG = "configure";
	public static final String FAMILY = "family";
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
	public static final int ROWS_PER_PAGE = 20;
	public static final int CUSTOM_FIELDS_PER_PAGE = 20;
	public static final String RESULT_LIST = "resultList";

	/* Schema */
	public static final String STUDY_SCHEMA = "study";
	public static final String AUDIT_SCHEMA = "audit";
	public static final String ADMIN_SCHEMA = "admin";
	public static final String GENO_SCHEMA = "geno";
	public static final String CONFIG_SCHEMA = "config";
	public static final String DISEASE_SCHEMA = "disease";
	public static final String SPARK_SCHEMA = "spark";

	/* Geno fields for output in dataextraction - will later allow choice
	 * - these are used in a linked hash map often, so please 
	 * 	- so we will try to keep order whereever they are used*/
	public static final String GENO_FIELDS_PIPELINE_ID = "pipelineId";
	public static final String GENO_FIELDS_PIPELINE_NAME = "pipelineName";
	public static final String GENO_FIELDS_PIPELINE_DECSRIPTION = "pipelineDescription";
	public static final String GENO_FIELDS_PROCESS_ID = "processId";
	public static final String GENO_FIELDS_PROCESS_NAME = "processName";
	public static final String GENO_FIELDS_PROCESS_DESCRIPTION = "processDescription";
	public static final String GENO_FIELDS_PROCESS_START_TIME = "startTime";
	public static final String GENO_FIELDS_PROCESS_END_TIME = "endTime";
	public static final String GENO_FIELDS_PROCESS_COMMAND_SERVER_URL = "commandServerUrl";
	public static final String GENO_FIELDS_PROCESS_COMMAND_NAME = "commandName";
	public static final String GENO_FIELDS_PROCESS_COMMAND_LOCATION = "commandLocation";
//	public static final String GENO_FIELDS_PROCESS_COMMAND_INPUT_FILE_FORMAT;
//	public static final String GENO_FIELDS_PROCESS_COMMAND_OUTPUT_FILE_FORMAT;
	public static final String GENO_FIELDS_PROCESS_INPUT_SERVER = "inputServer";
	public static final String GENO_FIELDS_PROCESS_INPUT_LOCATION = "inputLocation";
	public static final String GENO_FIELDS_PROCESS_INPUT_FILE_HASH = "inputFileHash";
	public static final String GENO_FIELDS_PROCESS_INPUT_FILE_TYPE = "inputFileType";
	public static final String GENO_FIELDS_PROCESS_INPUT_KEPT = "outputKept";
	public static final String GENO_FIELDS_PROCESS_OUTPUT_SERVER = "outputServer";
	public static final String GENO_FIELDS_PROCESS_OUTPUT_LOCATION = "outputLocation";
	public static final String GENO_FIELDS_PROCESS_OUTPUT_FILE_HASH = "outputFileHash";
	public static final String GENO_FIELDS_PROCESS_OUTPUT_FILE_TYPE = "outputFileType";
	public static final String GENO_FIELDS_PROCESS_OUTPUT_KEPT = "outputKept";
	

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
	//Add the new custom field category.
	public static final String ENTITY_TYPE_CUSTOM_FIELD_CATEGORY = "Custom Field Category";
	public static final String ENTITY_TYPE_CUSTOM_FIELD_DISPLAY = "Custom Field Display";
	public static final String ENTITY_TYPE_CUSTOM_FIELD_GROUP = "Custom Field Group";
	public static final String ENTITY_TYPE_INV_SITE = "InvSite";
	public static final String ENTITY_TYPE_INV_FREEZER = "InvFreezer";
	public static final String ENTITY_TYPE_INV_RACK = "InvRack";
	public static final String ENTITY_TYPE_INV_BOX = "InvBox";
	public static final String ENTITY_TYPE_INV_CELL = "InvCell";
	public static final String	ENTITY_TYPE_SEARCH	= "Search";

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
	public static final String FUNCTION_KEY_VALUE_SUBJECT_SEARCH = "SUBJECT_SEARCH";
	public static final String FUNCTION_KEY_VALUE_DEMOGRAPHIC_DATA = "DEMOGRAPHIC_DATA";
	public static final String FUNCTION_KEY_VALUE_PHONE = "PHONE";
	public static final String FUNCTION_KEY_VALUE_CONSENT = "CONSENT";
	public static final String FUNCTION_KEY_VALUE_ATTACHMENT = "ATTACHMENT";
	public static final String FUNCTION_KEY_VALUE_ADDRESS = "ADDRESS";
	public static final String FUNCTION_KEY_VALUE_CONTACT = "CONTACT";
	public static final String FUNCTION_KEY_VALUE_CALENDAR = "CALENDAR";
	//public static final String FUNCTION_KEY_VALUE_SUBJECT_UPLOAD = "SUBJECT_UPLOAD";
	public static final String FUNCTION_KEY_VALUE_STUDY_STUDY_DATA_UPLOAD = "STUDY_DATA_UPLOAD";
	public static final String FUNCTION_KEY_VALUE_SUBJECT_CORRESPONDENCE = "CORRESPONDENCE";
	public static final String FUNCTION_KEY_VALUE_REPORT = "REPORT";
	public static final String FUNCTION_KEY_VALUE_PHENO_SUMMARY = "SUMMARY";
	
	public static final String FUNCTION_KEY_VALUE_LIMS_SUBJECT = "LIMS_SUBJECT";
	//public static final String FUNCTION_KEY_VALUE_LIMS_COLLECTION = "LIMS_COLLECTION";
	public static final String FUNCTION_KEY_VALUE_BIOSPECIMEN = "BIOSPECIMEN";
	public static final String FUNCTION_KEY_VALUE_INVENTORY = "INVENTORY";
	
	//Add on 2015-06-22 to categories  custom field
	public static final String FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_CATEGORY = "SUBJECT_CUSTOM_FIELD_CATEGORY";
	public static final String FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD = "SUBJECT_CUSTOM_FIELD";
	public static final String FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_DATA = "SUBJECT_CUSTOM_DATA";
	
	//Add on 2015-11-05 to categories the data set
	public static final String FUNCTION_KEY_VALUE_DATA_CATEGORY = "DATASET_CATEGORY";//(1)
	public static final String FUNCTION_KEY_VALUE_DATA_CATEGORY_UPLOAD = "DATASET_CATEGORY_UPLOAD";//(2)
	public static final String FUNCTION_KEY_VALUE_DATA_DICTIONARY = "DATA_DICTIONARY";//(3)
	public static final String FUNCTION_KEY_VALUE_DATA_DICTIONARY_UPLOAD = "DATA_DICTIONARY_UPLOAD";//(4)
	public static final String FUNCTION_KEY_VALUE_PHENO_COLLECTION = "PHENO_COLLECTION";//(5)
	public static final String FUNCTION_KEY_VALUE_FIELD_DATA = "FIELD_DATA";//(6)
	public static final String FUNCTION_KEY_VALUE_FIELD_DATA_UPLOAD = "FIELD_DATA_UPLOAD";//(7)
	
	
	public static final String FUNCTION_KEY_VALUE_MODULE = "MODULE";
	public static final String FUNCTION_KEY_VALUE_FUNCTION = "FUNCTION";
	public static final String FUNCTION_KEY_VALUE_MODULE_FUNCTION = "MODULE_FUNCTION";
	public static final String FUNCTION_KEY_VALUE_ROLE = "ROLE";
	public static final String FUNCTION_KEY_VALUE_MODULE_ROLE = "MODULE_ROLE";
	public static final String FUNCTION_KEY_VALUE_ROLE_POLICY_TEMPLATE = "ROLE_POLICY_TEMPLATE";
	public static final String FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD_CATEGORY = "LIMS_CUSTOM_FIELD_CATEGORY";
	//public static final String FUNCTION_KEY_VALUE_LIMS_COLLECTION_CUSTOM_FIELD = "LIMS_COLLECTION_CUSTOM_FIELD";
	public static final String FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD = "LIMS_CUSTOM_FIELD";//new LIMS custom field.
	public static final String FUNCTION_KEY_VALUE_LIMS_COLLECTION_CUSTOM_DATA = "LIMS_COLLECTION_CUSTOM_DATA";
	//public static final String FUNCTION_KEY_VALUE_BIOSPECIMEN_CUSTOM_FIELD = "BIOSPECIMEN_CUSTOM_FIELD";
	public static final String FUNCTION_KEY_VALUE_BIOSPECIMEN_CUSTOM_DATA = "BIOSPECIMEN_CUSTOM_DATA";
	public static final String FUNCTION_KEY_VALUE_BIOSPECIMENUID_TEMPLATE = "BIOSPECIMENUID_TEMPLATE";
	public static final String FUNCTION_KEY_VALUE_BARCODE_PRINTER = "BARCODE_PRINTER";
	public static final String FUNCTION_KEY_VALUE_BARCODE_LABEL = "BARCODE_LABEL";
	public static final String FUNCTION_KEY_VALUE_BIOSPECIMEN_UPLOAD = "BIOSPECIMEN_UPLOAD";
	public static final String FUNCTION_KEY_VALUE_SUBJECT_CUSTOM_FIELD_UPLOAD = "SUBJECT_CUSTOM_FIELD_UPLOAD";
	//public static final String FUNCTION_KEY_VALUE_BIOCOLLECTION_CUSTOM_FIELD_UPLOAD = "BIOCOLLECTION_CUSTOM_FIELD_UPLOAD";
	public static final String FUNCTION_KEY_VALUE_LIMS_CUSTOM_FIELD_UPLOAD = "LIMS_CUSTOM_FIELD_UPLOAD";// new LIMS custom field upload.
	//public static final String FUNCTION_KEY_VALUE_BIOSPECIMEN_CUSTOM_FIELD_UPLOAD = "BIOSPECIMEN_CUSTOM_FIELD_UPLOAD";
	//public static final String FUNCTION_KEY_VALUE_BIOSPECIMEN_AND_BIOCOLLECTION_CUSTOM_FIELD_UPLOAD = "BIOSPECIMEN_AND_BIOCOLLECTION_CUSTOM_FIELD_UPLOAD";
	public static final String FUNCTION_KEY_VALUE_DATA_EXTRACTION	= "DATA_EXTRACTION";
	public static final String FUNCTION_KEY_VALUE_PEDIGREE = "PEDIGREE";
	public static final String FUNCTION_KEY_VALUE_GENO_TABLE = "GENO_TABLE";
	public static final String FUNCTION_KEY_VALUE_AUDIT = "Audit";
	
	public static final String FUNCTION_KEY_VALUE_GENE = "GENE";
	public static final String FUNCTION_KEY_VALUE_DISEASE = "DISEASE";
	public static final String FUNCTION_KEY_VALUE_DISEASE_CUSTOM_FIELDS = "DISEASE_CUSTOM_FIELDS";
	public static final String FUNCTION_KEY_VALUE_DISEASE_CUSTOM_FIELDS_DISPLAY = "DISEASE_CUSTOM_FIELD_DISPLAY";
	public static final String FUNCTION_KEY_VALUE_DISEASE_AFFECTION = "DISEASE_AFFECTION";

	public static final String FUNCTION_KEY_VALUE_GLOBAL_BIOSPECIMEN_SEARCH = "GLOBAL_BIOSPECIMEN_SEARCH";
	
	/* Ark Module Keys */
	public static final String ARK_MODULE_KEY = "MODULE_KEY";

	/* Ark Module Key values */
	public static final String ARK_MODULE_STUDY = "Study";
	public static final String ARK_MODULE_SUBJECT = "Subject";
	public static final String ARK_MODULE_USER = "User";
	public static final String ARK_MODULE_PHENOTYPIC = "Datasets";
	public static final String ARK_MODULE_GENOTYPIC = "Genotypic";
	public static final String ARK_MODULE_LIMS = "LIMS";
	public static final String ARK_MODULE_REGISTRY = "Registry";
	public static final String ARK_MODULE_REPORTING = "Reporting";
	public static final String ARK_MODULE_ADMIN = "Admin";
	public static final String ARK_MODULE_WORKTRACKING = "Work Tracking";
	public static final String ARK_MODULE_DISEASE = "Disease";
	public static final String ARK_MODULE_GLOBAL_SEARCH = "Global Search";
	public static final String ARK_MODULE_GENOMICS = "Genomics";

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
	public static final String[][] CUSTOM_FIELD_UPLOAD_HEADER = {{"", "FIELD_NAME","CUSTOM_FIELD_TYPE","CUSTOM_FIELD_CATEGORY",
			"FIELD_TYPE", "DESCRIPTION", "QUESTION", "UNITS", "ENCODED_VALUES",
			"MINIMUM_VALUE", "MAXIMUM_VALUE", "MISSING_VALUE", "REQUIRED", "ALLOW_MULTIPLE_SELECTIONS"},
			{
				"DESCRIPTION",
				"The unique identifier of the field",
				"The type of the field",
				"The pre defined category name of the field",
				"The field type of the field",
				"The details description of the field",
				"The question or the label for the field ",
				"The units for the field",
				"The encoded value for the field ",
				"The minimum value for the field",
				"The maximum value for the field",
				"The missing value for the field",
				"The required status for the field",
				"The multiple selection allowed status for the field"
		},
		{ 		"MANDATORY", 
				"Yes", 
				"Yes", 
				"No",
				"Yes",
				"No",
				"No",
				"No",
				"No",
				"No",
				"No",
				"No",
				"No",
				"No"
		 },
		{
				"VALID VALUES",
				"Maximum 50 characters", 
				"", 
				"", 
				"",
				"Maximum 255 characters", 
				"Maximum 255 characters", 
				"Maximum 50 characters",
				"0=Yes;1=No;",
				"",
				"",
				"",
				"true;yes;1;y;false;no;0;n",
				"true;yes;1;y;false;no;0;n"
		},
		{ "NOTE: Removed this first column, and replace rows 2 to 5", "", "", "", "", "", "", "","", "", "", "", "", ""
		}};
	public static final String[][] CUSTOM_FIELD_CATEGORY_UPLOAD_HEADER={ { "","CATEGORY_NAME",
		"CUSTOM_FIELD_TYPE","DESCRIPTION","PARENT_CATEGORY_NAME","ORDER_NUMBER"	},
		{
			"DESCRIPTION",
			"The unique identifier of the category",
			"The type of the category",
			"The details description of the category",
			"The parent category of the category",
			"The order number of the category"
		},
		{
			"MANDATORY", 
			"Yes", 
			"Yes", 
			"No",
			"No-if blank category becomes a parent category",
			"Yes"
		},
		{
			"VALID VALUES",
			"Maximum 50 characters", 
			"Maximum 50 characters", 
			"Maximum 255 characters",
			"Maximum 50 characters",
			"Any Integer value starting from 1"
		},
		{ "NOTE: Removed this first column, and replace rows 2 to 5.",
				"", "", "", "",""
		},
		{
			"Please make sure to upload all the parent categories to the system first before assign them to sub categories. ",
				 "", "", "", "",""
		}
	};

	public static final String NAME = "name";
	public static final String CUSTOM_FIELD_DOT_NAME = "customField.name";
	public static final String PUBLIC_FIELD_NAME = "publicFieldName";
	public static final String ID = "id";

	public static final String	CLASS												= "class";
	public static final String	EVEN												= "even";
	public static final String	ODD												= "odd";
	/* Ark Session Details */
	public static final String ARK_SESSION_ID = "ARK_SESSION_ID";
	public static final String ARK_USERID = "ARK_USERID";
	public static final String ARK_HOST = "ARK_HOST";
	public static final String ARK_USER_NAME = "ARK_USER_NAME";
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


	// 1 digit, 1 lower, 1 upper, 1 symbol "~!@#$%^&*()_-+={}[]:;\"<>|", from 6
	// to 20
	public static final String PASSWORD_PATTERN = "((?=.*\\d)(?=.*[a-z])(?=.*[A-Z])(?=.*[~!@#$%^&*()_\\-\\+\\=\\{\\}\\[\\]:;\\\"<>|]).{6,20})";

	public static final String[] BIOCOLLECTION_TEMPLATE_HEADER = { "SUBJECTUID",
		"BIOCOLLECTIONUID","NAME","COLLECTIONDATE","COMMENTS"};
	
	public static final String[] BIOSPECIMEN_TEMPLATE_HEADER = { "SUBJECTUID",
		"BIOSPECIMENUID", "BIOCOLLECTIONUID", "SAMPLETYPE", "QUANTITY",
		"UNITS", "TREATMENT", "CONCENTRATION", "SITE", "FREEZER", "RACK", "BOX", "ROW", "COLUMN" };
	
	public static final String[] BIOSPECIMEN_INVENTORY_TEMPLATE_HEADER = {"BIOSPECIMENUID",
		"SITE", "FREEZER", "RACK", "BOX", "ROW","COLUMN" };
	
	public static final String[][] BIOCOLLECTION_TEMPLATE_CELLS = {
		{ "", "SUBJECTUID",  "BIOCOLLECTIONUID","NAME","COLLECTIONDATE","COMMENTS" },
		{
				"DESCRIPTION",
				"The unique identifier assigned for this subject.",
				"The unique identifier of the biospecimen",
				"The name of the collection",
				"The collection date",
				"The comment"
		},
		{ 		"MANDATORY", 
				"Mandatory IF study not set to autogenerate id's for biocollections",
				"Yes",
				"No", 
				"No", 
				"No" },
		{
				"VALID VALUES",
				"",
				"",
				"",
				"",
				""},
		{ "NOTE: Removed this first column, and replace rows 2 to 5",
				"", "",	"", "", "" } };
	
	public static final String[][] BIOSPECIMEN_INVENTORY_TEMPLATE_CELLS = {
			{  "", "BIOSPECIMENUID", "SITE",
					"FREEZER", "RACK", "BOX", "ROW", "COLUMN" 
			},
			{
					"DESCRIPTION",
					"The unique identifier of the biospecimen",
					"The site of the bispecimen location",
					"The freezer of the bispecimen location",
					"The rack of the bispecimen location",
					"The box of the bispecimen location",
					"The row of the bispecimen location",
					"The column of the bispecimen location" 
			},
			{ 		"MANDATORY", 
					"Yes", 
					"Yes", 
					"Yes", 
					"Yes", 
					"Yes", 
					"Yes",
					"Yes",
			 },
			{		"VALID VALUES",
					"",
					"", 
					"", 
					"", 
					"", 
					"", 
					""
			},
			{ "NOTE: Removed this first column, and replace rows 2 to 5", "", "", "", "", "", "", ""} };
	
	public static final String[][] BIOSPECIMEN_TEMPLATE_CELLS = {
			{ "", "SUBJECTUID", "BIOSPECIMENUID", "BIOCOLLECTIONUID",
					"SAMPLETYPE", "QUANTITY", "UNITS", "TREATMENT", "CONCENTRATION", "SITE",
					"FREEZER", "RACK", "BOX", "ROW", "COLUMN" },
			{
					"DESCRIPTION",
					"The unique identifier assigned for this subject.",
					"The unique identifier of the biospecimen",
					"The unique identifier of collection to which the biospecimen is associated with",
					"The sample type of the biospecimen",
					"The quantity of the bispecimen", 
					"The units of quanity",
					"The treatment type of the bispecimen",
					"The concentration of the bispecimen",
					"The site of the bispecimen location",
					"The freezer of the bispecimen location",
					"The rack of the bispecimen location",
					"The box of the bispecimen location",
					"The row of the bispecimen location",
					"The column of the bispecimen location" },
			{ 		"MANDATORY", 
					"Mandatory IF study not set to autogenerate id's for biospecimens", 
					"Mandatory IF study not set to autogenerate id's for biocollections", "" +
					"Yes", 
					"Yes", 
					"Yes", 
					"Yes", 
					"Yes",
					"No",
					"No", 
					"No", 
					"No", 
					"No", 
					"No", 
					"No" },
			{
					"VALID VALUES",
					"",
					"",
					"",
					"",
					"",
					"",
					"70% Alcohol Fixed, Formalin Fixed, Frozen, RN later, RNA later, then Formalin Fixed, RNA later, then Snap Frozen, Tissue Cultured, Unprocessed",
					"",
					"", 
					"", 
					"", 
					"", 
					"", 
					""},
			{ "NOTE: Removed this first column, and replace rows 2 to 5", "", "",
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
	
	//Security permission types
	public static final String PERMISSION_UPDATE="UPDATE";
	public static final String	ENCODED_VALUES_PATTERN	= "(-?[0-9]+=[^;]+;)+";
	public static final String SEARCH_ID = "search.id";
	public static final String SEARCH_NAME = "search.name";
	public static final String SEARCH_INCLUDE_GENO = "search.includeGeno";
	public static final String SEARCH_INCLUDE_MEGA = "search.includeMega";
	public static final String	SAVEANDCLOSE									= "saveAndClose";
	public static final String	yyyy_MM_dd	= "yyyy-MM-dd";
	public static final String ARK_GENO_SERVICE = "genoService";
	public enum FileFormat {CSV, XLS, PDF, TXT, DOC, XML};

	public static final String	UPLOAD_TYPE_ID	= "id";
	public static final String	UPLOAD_TYPE_NAME= "name";
	
	public static final String CONFIG_ROWS_PER_PAGE = "ROWS_PER_PAGE";
	public static final String CONFIG_CUSTOM_FIELDS_PER_PAGE = "CUSTOM_FIELDS_PER_PAGE";
	
	public static final String MESSAGE_NO_SUBJECT_IN_CONTEXT="There is no subject in context. Please bring a subject into context via the subject tab.";
	
	public static final String MESSAGE_NO_STUDY_IN_CONTEXT="There is no study in context. Please select a study";
	
	public static final String ENTITY_TYPE_PHENO_DATASET_CATEGORY = "Pheno Data Set Category";
	
	public static final String ENTITY_TYPE_PHENO_DATASET_FIELD = "PhenoDataSet Field";
	
	public static final String ENTITY_TYPE_PHENO_DATASET_FIELD_DISPLAY = "PhenoDataSet Field Display";
	
	public static final String ENTITY_TYPE_PHENO_DATASET_CATEGORY_FIELD_GROUP = "PhenoDataSet Category Field Group";
	
	public static final String UPLOAD_STATUS_COMPLETED = "COMPLETED";
	
	public static final String UPLOAD_STATUS_STATUS_NOT_DEFINED = "STATUS_NOT_DEFINED";
	
	public static final String UPLOAD_STATUS_VALIDATED = "VALIDATED";
	
	public static final String UPLOAD_STATUS_AWAITING_VALIDATION = "AWAITING_VALIDATION";
	
	public static final String UPLOAD_STATUS_OF_ERROR_ON_DATA_IMPORT= "ERROR_ON_DATA_IMPORT";
	
	//General Form text field maximum field length validation
	public static final Integer GENERAL_FIELD_NAME_MAX_LENGTH_50 = 50;
	
	public static final Integer GENERAL_FIELD_DESCRIPTIVE_MAX_LENGTH_255 = 255;
	
	public static final Integer GENERAL_FIELD_WHOLE_NUMBER_MIN_QUANTITY_1 = 1;
	
	public static final Integer GENERAL_FIELD_WHOLE_NUMBER_MAX_QUANTITY_100 = 100;
	
	public static final Integer GENERAL_FIELD_COMMENTS_MAX_LENGTH_500 = 500;
	
	public static final Integer GENERAL_FIELD_MAX_LENGTH_100 = 100;
	//This is 200MB
	public static final Integer MAXIMUM_PERMISSABLE_FILE_SIZE=(int)((int)2*Math.pow(10,8));
	
	public static final String	PERSON_PERSON_ID				= "person.id";
	public static final String	STREET_ADDRESS					= "streetAddress";
	public static final String	COUNTRY_NAME					= "country";
	public static final String	POST_CODE						= "postCode";
	public static final String	CITY							= "city";
	public static final String	STATE_NAME						= "state";
	public static final String	ADDRESS_TYPE					= "addressType";
	public static final String DELETE_UPLOAD 					= "deleteUpload";

			
}
