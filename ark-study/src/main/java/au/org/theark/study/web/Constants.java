package au.org.theark.study.web;


public class Constants {
	
	public static final String ADMIN_TAB="Administration";
	public static final String STUDY_TAB="Study";
	public static final String SUBJECT_TAB="Subject";
	public static final String STUDY_DETAILS="Study Details";
	public static final String SUB_STUDIES ="Sub Studies";
	public static final String SITES="Sites";
	public static final String CONSENT_SECTIONS="Consent Sections";
	public static final String STUDY_COMPONENTS="Study Components";
	public static final String USERS="Users";
	public static final String MY_DETAILS="My Details";
	public static final String STUDY_ADMIN="Study Admin";
	public static final String PARTICIPANT_MANAGEMENT="Participant Management";
	public static final String USER_NAME = "userName"; 
	public static final String FIRST_NAME ="firstName";
	public static final String LAST_NAME ="lastName";
	public static final String EMAIL ="email";
	public static final String 	PHONE_NUMBER="phoneNumber";
	public static final String PASSWORD = "password";
	public static final String OLD_PASSWORD = "oldPassword";
	public static final String CONFIRM_PASSWORD = "confirmPassword";
	public static final String SAVE ="save";
	public static final String CANCEL ="cancel";
	public static final String DELETE="delete";
	public static final String ARCHIVE="archive";
	public static final String SEARCH_USER_FORM ="searchUserForm";
	public static final String SEARCH_FORM ="searchForm";
	public static final String SEARCH="search";
	public static final String NEW="new";
	public static final String RESET="reset";
	public static final String ADD_PHONE="addPhone";
	public static final int MODE_NEW = 1;
	public static final int MODE_EDIT =2;
	public static final int MODE_READ =3;
	
	/* Person */
	
	public static final String PERSON_PREFERRED_NAME="person.preferredName";
	public static final String PERSON_VITAL_STATUS ="person.vitalStatus";
	public static final String PERSON_PERSON_ID="person.id";
	public static final String SUBJECT_UID="subjectUID";
	public static final String PERSON_FIRST_NAME = "person.firstName";
	public static final String PERSON_MIDDLE_NAME = "person.middleName";
	public static final String PERSON_LAST_NAME = "person.lastName";
	public static final String PERSON ="person";
	public static final String GENDER_TYPE="genderType";
	public static final String ID="id";
	public static final String SUBJECT_STATUS ="subjectStatus";
	public static final String SUBJECT_STATUS_ID ="id";
	public static final String VITAL_STATUS="vitalStatus";
	public static final String STATUS_NAME ="statusName";
	public static final String PERSON_MARITAL_STATUS ="person.maritalStatus";
	public static final String PERSON_DOB="person.dateOfBirth";
	public static final String PERSON_TYTPE_TYPE = "person.titleType";
	public static final String PERSON_GENDER_TYPE ="person.genderType";
	public static final String PERSON_GENDER_TYPE_NAME ="person.genderType.name";
	public static final String SUBJECT_FULL_NAME ="subjectFullName";
	public static final String PERSON_VITAL_STATUS_NAME="person.vitalStatus.statusName";
	public static final String SUBJECT_STATUS_NAME ="subjectStatus.name";
	public static final String EVEN="even";
	public static final String ODD="odd";
	public static final String CLASS="class";
	public static final String SUBJECT_KEY_LBL ="subjectKeyLbl";
	public static final String SUBJECT_LIST ="subjectList";
	public static final String PHONE_LIST = "phoneList";
	public static final String PHONE_NUMBER_LABEL="phoneNumberLabel";
	
	public static final String ADDRESS_LIST="addressList";
	public static final String ADDRESS_LABEL="addressLabel";
	/* Multiple choice List Controls */
	public static final String AVAILABLE_ROLES_MLC ="availableRolesLMC";
	public static final String SELECTED_ROLES_MLC="selectedRolesLMC";
	
	/*Buttons*/
	public static final String ADD_ALL_BUTTON ="addAllBtn";
	public static final String ADD_SELECTED="addSelectedBtn";
	public static final String REMOVE_ALL_BUTTON ="removeAllBtn";	
	public static final String REMOVE_SELECTED_BUTTON="removeSelectedBtn";
	/*Accordion Constants*/
	public static final String ACCORDION ="accordion";
	public static final String ACCORDION_SECTION ="accordionSection";
	public static final String ACCORDION_SECTION_NAME_LBL ="accordionSectionNameLbl";
	public static final String AJAX_CONTAINER ="ajaxContainer";
	public static final String APP_ROLE_ACCORDION="appRoleAccordion";
	
	/*Form Objects */
	public static final String 	USER_FORM ="userForm";
	public static final String 	USER_DETAILS_FORM ="userDetailsForm";
	public static final String  APP_ROLE_FORM ="appRoleForm";
	
	
	/* Study */
	
	public static final String STUDY_SEARCH_KEY="study.id";
	public static final String STUDY_SEARCH_NAME="study.name";
	public static final String STUDY_SEARCH_DOA="study.dateOfApplication";
	public static final String STUDY_SEARCH_CONTACT="study.contactPerson";
	
	/**
	 * Site
	 */
	public static final String STUDY_SITE_KEY="studySite.studySiteKey";
	public static final String STUDY_SITE_NAME="studySite.name";
	
	
	public static final String STUDY_KEY = "study.id";
	
	public static final String STUDY_NAME="study.name";
	public static final String STUDY_DESCRIPTION="study.description";
	public static final String STUDY_STATUS = "study.studyStatus";
	public static final String STUDY_DATE_OF_APPLICATION ="study.dateOfApplication";
	public static final String STUDY_CONTACT_PERSON="study.contactPerson";
	public static final String STUDY_CONTACT_PERSON_PHONE ="study.contactPersonPhone";
	public static final String STUDY_ESTIMATED_YEAR_OF_COMPLETION="study.estimatedYearOfCompletion";
	public static final String STUDY_CHIEF_INVESTIGATOR ="study.chiefInvestigator";
	public static final String STUDY_CO_INVESTIGATOR ="study.coInvestigator";
	public static final String STUDY_AUTO_GENERATE_SUBJECT_KEY = "study.autoGenerateSubjectUId";
	public static final String SUBJECT_KEY_START="study.subjectUIdStart";
	public static final String SUBJECT_ID_PREFIX = "study.subjectIdPrefix";
	public static final String STUDY_LDAP_GROUP_NAME="ldapGroupName";
	public static final String STUDY_AUTO_CONSENT="study.autoConsent";
	public static final String SUB_STUDY_BIOSPECIMENT_PREFIX="study.subStudyBiospecimenPrefix";
	public static final String STUDY_MAIN_TAB="Study";
	public static final String SUBJECT_MAIN_TAB="Subject";
	//public static final String STUDY_STATUS_KEY="studyStatusId";
	public static final String STUDY_STATUS_KEY="id";
	public static final String STUDY_DROP_DOWN_CHOICE ="studyChoice";
	public static final String YES="Yes";
	public static final String NO="No";
	public static final String NAME ="name";
	public static final String MY_DETAILS_PANEL ="myDetailsPanel";
	public static final String LMC_AJAX_CONTAINER="lmcAjaxContainer";
	public static final String LMC_SELECTED_APPS="lmcSelectedApps";
	public static final String LMC_AVAILABLE_APPS="lmcAvailableApps";
	public static final String PHONE_TYPE_ID="phoneTypeId";
	public static final String AUTO_GEN_SUBJECT_KEY ="autoGenerateSubjectKey";
	public static final String AUTO_CONSENT ="autoConsent";
	public static final String 	AUTO_GEN_SUB_ID ="autoGenSubId";
	public static final String MODULE_NAME ="moduleName";
	public static final String MODULES_SELECTED="modulesSelected";
	public static final String MODULES_AVAILABLE="modulesAvailable";
	public static final String STUDY_COMPONENT_ID ="studyComponent.id";
	public static final String STUDY_COMPONENT_NAME="studyComponent.name";
	public static final String STUDY_COMPONENT_DESCRIPTION = "studyComponent.description";
	public static final String STUDY_COMPONENT_KEYWORD="studyComponent.keyword";
	/* Error Messages and Fields references from Resource files */
	public static final String ERROR_STUDY_NAME_REQUIRED ="error.study.name.required";
	public static final String ERROR_STUDY_STATUS_REQUIRED="error.study.status.required";
	public static final String ERROR_STUDY_CHIEF_INVESTIGATOR ="error.study.chiefInvestigator";
	public static final String ERROR_STUDY_SUBJECT_KEY_PREFIX="error.study.subject.key.prefix";
	public static final String ERROR_STUDY_DOA_MAX_RANGE = "error.study.doa.max.range";
	public static final String CHIEF_INVESTIGATOR = "Chief Investigator";
	public static final String ERROR_STUDY_CO_INVESTIGATOR = "error.study.co.investigator";
	public static final String CO_INVESTIGATOR ="Co-Investigator";
	public static final String STATUS ="Status";
	public static final String ERROR_STUDY_NAME ="Study Name";
	
	/*Spring Bean names*/
	public static final String STUDY_SERVICE= "studyService";
	public static final String STUDY_DAO ="studyDao";
	
	public static final String ACTION_ADD_SELECTED="addSelected";
	public static final String ACTION_ADD_ALL="addAll";
	public static final String ACTION_REMOVE_SELECTED="removeSelected";
	public static final String ACTION_REMOVE_ALL="removeAll";
	
	/* General ones */
	public static final String PAGE_SEARCH ="page.search";
	public static final String PAGE_NEW ="page.new";
	public static final String CANCEL_KEY ="cancelKey";
	public static final String SAVE_KEY ="saveKey";
	public static final String DELETE_KEY ="deleteKey";
	public static final String STUDY ="study";
	
	/* Person Address */
	public static final String ADDRESS_STREET_ADDRESS="address.streetAddress";
	public static final String ADDRESS_CITY="address.city";
	public static final String ADDRESS_POST_CODE="address.postCode";
	public static final String ADDRESS_COUNTRY="address.country";
	public static final String ADDRESS_COUNTRYSTATE_STATE="address.countryState";
	
	
			 
}
