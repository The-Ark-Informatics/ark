package au.org.theark.phenotypic.web;

public class Constants {
	// DAO
	public static final String PHENOTYPIC_DAO = "phenotypicDao";
	
	// Tabs/menus
	public static final String PHENOTYPIC_MAIN_TAB = "Phenotypic";
	
	// Sub tabs/menus
	public static final String PHENOTYPIC_SUBMENU = "phenotypicSubMenus";
	public static final String	PHENOTYPIC_SUMMARY_SUBMENU	= "Summary";
	public static final String PHENO_COLLECTION_SUBMENU = "Collections";
	public static final String FIELD_SUBMENU = "Fields";
	public static final String PHENOTYPIC_IMPORT_SUBMENU = "Data Import";
	public static final String REPORT_SUBMENU = "Reports";
	
	// Resource keys
	public static final String	PHENOTYPIC_SUMMARY_RESOURCEKEY	= "tab.module.phenotypic.summaryModule";
	public static final String COLLECTION_RESOURCEKEY = "tab.module.phenotypic.collection";
	public static final String FIELD_RESOURCEKEY = "tab.module.phenotypic.field";
	public static final String PHENOTYPIC_IMPORT_RESOURCEKEY = "tab.module.phenotypic.phenotypicImport";
	public static final String REPORT_RESOURCEKEY = "tab.module.phenotypic.report";
	
	// Generic Buttons
	public static final String NEW_BUTTON = "newButton";
	public static final String SAVE_BUTTON = "saveButton";
	public static final String EDIT_BUTTON = "editButton";
	
	// Import buttons
	public static final String VALIDATE_PHENOTYPIC_DATA_FILE = "validatePhenotypicDataFile";
	public static final String IMPORT_PHENOTYPIC_DATA_FILE = "importPhenotypicDataFile";
	
	// Test
	public static final String TEST_SUBMENU = "Test";
	public static final String TEST_RESOURCEKEY = "tab.module.phenotypic.test";
	public static final String TEST = "Test";
	public static final String FIRETEST = "fireInTheHole";
	public static final String WATERTEST = "theGreatFlood";
	public static final String NOAHTEST = "noahsRescue";

	public static final String	PHENOTYPIC_GRID_VIEW	= "phenotypicGridView";
	
	// Session items
	public static final String	SESSION_PHENO_COLLECTION_ID = "phenoCollectionId";	
	
	// VO fields
	public static final String	PHENO_COLLECTIONVO_PHENO_COLLECTION_ID	= "phenoCollection.id";
	public static final String	PHENO_COLLECTIONVO_PHENO_COLLECTION_NAME	= "phenoCollection.name";
	public static final String	PHENO_COLLECTIONVO_PHENO_COLLECTION_DESCRIPTION	= "phenoCollection.description";
	public static final String PHENO_COLLECTIONVO_FIELD = "phenoCollection.field";
	public static final String PHENO_COLLECTIONVO_FIELD_ID = "phenoCollection.field.id";
	public static final String PHENO_COLLECTIONVO_PERSON = "phenoCollection.person";
	public static final String PHENO_COLLECTIONVO_PERSON_ID = "phenoCollection.person.id";
	public static final String PHENO_COLLECTIONVO_FIELD_DATA = "phenoCollection.fieldData";
	public static final String PHENO_COLLECTIONVO_FIELD_DATA_ID = "phenoCollection.fieldData.id";
	public static final String PHENO_COLLECTIONVO_PHENO_COLLECTION_STATUS = "phenoCollection.status";
	public static final String	PHENO_COLLECTIONVO_PHENO_COLLECTION_START_DATE	= "phenoCollection.startDate";
	public static final String	PHENO_COLLECTIONVO_PHENO_COLLECTION_EXPIRY_DATE	= "phenoCollection.expiryDate";
	
	public static final String FIELDVO_FIELD_ID = "field.id";
	public static final String	FIELDVO_FIELD_STUDY	= "field.study";
	public static final String FIELDVO_FIELD_FIELD_TYPE = "field.fieldType";
	public static final String FIELDVO_FIELD_NAME = "field.name";
	public static final String FIELDVO_FIELD_DESCRIPTION = "field.description";
	public static final String FIELDVO_FIELD_UNITS = "field.units";
	public static final String FIELDVO_FIELD_SEQ_NUM = "field.seqNum";
	public static final String	FIELDVO_FIELD_MIN_VALUE	= "field.minValue";
	public static final String	FIELDVO_FIELD_MAX_VALUE	= "field.maxValue";
	public static final String	FIELDVO_FIELD_DISCRETE_VALUES	= "field.discreteValues";
	
	public static final String	FIELD_DATAVO_FIELD_DATA_ID = "fieldData.id";
	public static final String	FIELD_DATAVO_FIELD_DATA_COLLECTION	= "fieldData.collection";
	public static final String	FIELD_DATAVO_FIELD_DATA_COLLECTION_ID	= "fieldData.collection.id";
	public static final String	FIELD_DATAVO_FIELD_DATA_LINK_SUBJECT_STUDY	= "fieldData.linkSubjectStudy";
	public static final String	FIELD_DATAVO_FIELD_DATA_LINK_SUBJECT_STUDY_KEY	= "fieldData.linkSubjectStudy.key";
	public static final String	FIELD_DATAVO_FIELD_DATA_DATE_COLLECTED = "fieldData.dateCollected";
	public static final String	FIELD_DATAVO_FIELD_DATA_FIELD	= "fieldData.field";
	public static final String	FIELD_DATAVO_FIELD_DATA_FIELD_ID	= "fieldData.field.id";
	public static final String	FIELD_DATAVO_FIELD_DATA_VALUE = "fieldData.value";
	public static final String FIELD_DATAVO_FIELD_DATA_UPDATE_USER = "fieldData.user";
	public static final String FIELD_DATAVO_FIELD_DATA_UPDATE_USER_ID = "fieldData.user.id";
	public static final String FIELD_DATAVO_FIELD_DATA_INSERT_TIME = "fieldData.insertTime";
	public static final String FIELD_DATAVO_FIELD_DATA_UPDATE_TIME = "fieldData.updateTime";
	
	// Entity fields
	public static final String PHENO_COLLECTION = "phenoCollection";
	public static final String PHENO_COLLECTION_ID = "id";
	public static final String PHENO_COLLECTION_STUDY = "study";
	public static final String PHENO_COLLECTION_NAME = "name";
	public static final String PHENO_COLLECTION_DESCRIPTION = "description";
	public static final String PHENO_COLLECTION_STATUS = "status";
	public static final String PHENO_COLLECTION_START_DATE = "startDate";
	public static final String PHENO_COLLECTION_EXPIRY_DATE= "expiryDate";
	public static final String PHENO_COLLECTION_USER = "user";
	public static final String	PHENO_COLLECTION_USER_ID	= "userId";
	public static final String PHENO_COLLECTION_INSERT_TIME = "insertTime";
	public static final String PHENO_COLLECTION_UPDATE_USER = "user";
	public static final String	PHENO_COLLECTION_UPDATE_USER_ID	= "userId";
	public static final String PHENO_COLLECTION_UPDATE_TIME = "updateTime";
	
	public static final String COLLECTION_IMPORT = "collectionImport";
	public static final String COLLECTION_IMPORT_ID = "id";
	public static final String COLLECTION_IMPORT_COLLECTION = "collection";
	public static final String COLLECTION_IMPORT_TYPE = "importType";
	public static final String COLLECTION_IMPORT_DELIMITER_TYPE = "delimiterType";
	public static final String COLLECTION_IMPORT_START_TIME = "startTime";
	public static final String COLLECTION_IMPORT_FINISH_TIME= "finishTime";
	public static final String COLLECTION_IMPORT_USER = "user";
	public static final String COLLECTION_IMPORT_USER_ID = "userId";
	public static final String COLLECTION_IMPORT_INSERT_TIME = "insertTime";
	public static final String COLLECTION_IMPORT_UPDATE_USER = "user";
	public static final String COLLECTION_IMPORT_UPDATE_USER_ID = "userId";
	public static final String COLLECTION_IMPORT_UPDATE_TIME = "updateTime";
	
	public static final String	FIELD	= "field";
	public static final String	FIELD_ID	= "id";
	public static final String	FIELD_STUDY	= "study";
	public static final String FIELD_FIELD_TYPE = "fieldType";
	public static final String FIELD_NAME = "name";
	public static final String FIELD_DESCRIPTION = "description";
	public static final String FIELD_UNITS = "units";
	public static final String FIELD_SEQ_NUM = "seqNum";
	public static final String FIELD_MIN_VALUE = "minValue";
	public static final String FIELD_MAX_VALUE = "maxValue";
	public static final String FIELD_DISCRETE_VALUES = "discreteValues";
	public static final String FIELD_USER = "user";
	public static final String FIELD_INSERT_TIME = "insertTime";
	public static final String FIELD_UPDATE_USER = "user";
	public static final String FIELD_UPDATE_TIME = "updateTime";
	
	public static final String FIELD_TYPE = "fieldType";
	public static final String FIELD_TYPE_ID = "id";
	public static final String FIELD_TYPE_NAME = "name";
	
	public static final String FIELD_DATA = "fieldData";
	public static final String FIELD_DATA_ID = "id";
	public static final String	FIELD_DATA_PHENO_COLLECTION	= "collection";
	public static final String	FIELD_DATA_LINK_SUBJECT_STUDY	= "linkSubjectStudy";
	public static final String FIELD_DATA_COLLECTED = "dateCollected";
	public static final String FIELD_DATA_FIELD = "field";
	public static final String FIELD_DATA_VALUE = "value";
	public static final String FIELD_DATA_USER = "user";
	public static final String FIELD_DATA_INSERT_TIME = "insertTime";
	public static final String FIELD_DATA_UPDATE_USER = "user";
	public static final String FIELD_DATA_UPDATE_USER_ID = "userId";
	public static final String FIELD_DATA_UPDATE_TIME = "updateTime";

	public static final String	STATUS	= "status";
	public static final String	STATUS_ID	= "id";
	public static final String	STATUS_NAME	= "name";

	public static final String	LINK_SUBJECT_STUDY	= "linkSubjectStudy";

		
}
