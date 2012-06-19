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
package au.org.theark.phenotypic.web;

public class Constants {
	// DAO
	public static final String		PHENOTYPIC_DAO													= "phenotypicDao";

	// Tabs/menus
	public static final String		PHENOTYPIC_MAIN_TAB											= "Phenotypic";

	// Exception messages
	public static final String			IO_EXCEPTION													= "IOException: Input error. ";
	public static final String			FILE_FORMAT_EXCEPTION										= "File Format Exception: Input error. ";
	public static final String			ARK_SYSTEM_EXCEPTION											= "General ARK System Exception: ";
	public static final String			ARK_BASE_EXCEPTION											= "Base ARK System Exception: ";
	
	// Sub tabs/menus
	public static final String		PHENOTYPIC_SUBMENU											= "phenotypicSubMenus";
	public static final String		PHENOTYPIC_SUMMARY_SUBMENU									= "Summary";
	public static final String		FIELD_SUBMENU													= "DATA_DICTIONARY";
	public static final String		FIELD_UPLOAD_SUBMENU											= "DATA_DICTIONARY_UPLOAD";
	public static final String		PHENO_COLLECTION_SUBMENU									= "PHENO_COLLECTION";
	public static final String		FIELD_DATA_SUBMENU											= "FIELD_DATA";
	public static final String		FIELD_DATA_UPLOAD_SUBMENU									= "FIELD_DATA_UPLOAD";
	public static final String		REPORT_SUBMENU													= "Reports";

	// Resource keys
	public static final String		PHENOTYPIC_SUMMARY_RESOURCEKEY							= "tab.module.phenotypic.summaryModule";
	public static final String		COLLECTION_RESOURCEKEY										= "tab.module.phenotypic.collection";
	public static final String		FIELD_RESOURCEKEY												= "tab.module.phenotypic.field";
	public static final String		FIELD_UPLOAD_RESOURCEKEY									= "tab.module.phenotypic.fieldUpload";
	public static final String		FIELD_DATA_RESOURCEKEY										= "tab.module.phenotypic.fieldData";
	public static final String		FIELD_DATA_UPLOAD_RESOURCEKEY								= "tab.module.phenotypic.phenoUpload";
	public static final String		REPORT_RESOURCEKEY											= "tab.module.phenotypic.report";

	// Generic Buttons
	public static final String		NEW_BUTTON														= "newButton";
	public static final String		SAVE_BUTTON														= "saveButton";
	public static final String		EDIT_BUTTON														= "editButton";

	// Import buttons
	public static final String		VALIDATE_PHENOTYPIC_DATA_FILE								= "validatePhenotypicDataFile";
	public static final String		IMPORT_PHENOTYPIC_DATA_FILE								= "importPhenotypicDataFile";

	// Test
	public static final String		TEST_SUBMENU													= "Test";
	public static final String		TEST_RESOURCEKEY												= "tab.module.phenotypic.test";
	public static final String		TEST																= "Test";
	public static final String		FIRETEST															= "fireInTheHole";
	public static final String		WATERTEST														= "theGreatFlood";
	public static final String		NOAHTEST															= "noahsRescue";

	public static final String		PHENOTYPIC_GRID_VIEW											= "phenotypicGridView";

	// Session items
	public static final String		SESSION_PHENO_COLLECTION_ID								= "phenoCollectionId";

	// VO fields
	public static final String		PHENO_COLLECTIONVO_PHENO_COLLECTION_ID					= "phenoCollection.id";
	public static final String		PHENO_COLLECTIONVO_PHENO_COLLECTION_NAME				= "phenoCollection.name";
	public static final String		PHENO_COLLECTIONVO_PHENO_COLLECTION_DESCRIPTION		= "phenoCollection.description";
	public static final String		PHENO_COLLECTIONVO_FIELD									= "phenoCollection.field";
	public static final String		PHENO_COLLECTIONVO_FIELD_ID								= "phenoCollection.field.id";
	public static final String		PHENO_COLLECTIONVO_PERSON									= "phenoCollection.person";
	public static final String		PHENO_COLLECTIONVO_PERSON_ID								= "phenoCollection.person.id";
	public static final String		PHENO_COLLECTIONVO_FIELD_DATA								= "phenoCollection.fieldData";
	public static final String		PHENO_COLLECTIONVO_FIELD_DATA_ID							= "phenoCollection.fieldData.id";
	public static final String		PHENO_COLLECTIONVO_PHENO_COLLECTION_STATUS			= "phenoCollection.status";
	public static final String		PHENO_COLLECTIONVO_PHENO_COLLECTION_START_DATE		= "phenoCollection.startDate";
	public static final String		PHENO_COLLECTIONVO_PHENO_COLLECTION_END_DATE			= "phenoCollection.endDate";
	public static final String		PHENO_COLLECTIONVO_FIELD_PALETTE							= "phenoCollection.fieldPalette";
	public static final String		PHENO_COLLECTIONVO_UPLOAD									= "phenoCollection.upload";
	public static final String		PHENO_COLLECTIONVO_UPLOAD_ID								= "phenoCollection.upload.id";
	public static final String		PHENO_COLLECTIONVO_UPLOAD_NAME							= "phenoCollection.upload.filename";
	public static final String		PHENO_COLLECTIONVO_UPLOAD_FILE_FORMAT					= "phenoCollection.upload.fileFormat";
	public static final String		PHENO_COLLECTIONVO_UPLOAD_FILE_FORMAT_ID				= "phenoCollection.upload.fileFormat.id";
	public static final String		PHENO_COLLECTIONVO_UPLOAD_FILE_FORMAT_NAME			= "phenoCollection.upload.fileFormat.name";

	public static final String		FIELDVO_FIELD_ID												= "field.id";
	public static final String		FIELDVO_FIELD_STUDY											= "field.study";
	public static final String		FIELDVO_FIELD_FIELD_TYPE									= "field.fieldType";
	public static final String		FIELDVO_FIELD_NAME											= "field.name";
	public static final String		FIELDVO_FIELD_DESCRIPTION									= "field.description";
	public static final String		FIELDVO_FIELD_UNITS											= "field.units";
	public static final String		FIELDVO_FIELD_SEQ_NUM										= "field.seqNum";
	public static final String		FIELDVO_FIELD_MIN_VALUE										= "field.minValue";
	public static final String		FIELDVO_FIELD_MAX_VALUE										= "field.maxValue";
	public static final String		FIELDVO_FIELD_ENCODED_VALUES								= "field.encodedValues";
	public static final String		FIELDVO_FIELD_MISSING_VALUE								= "field.missingValue";

	public static final String		FIELD_DATAVO_FIELD_DATA_ID									= "fieldData.id";
	public static final String		FIELD_DATAVO_FIELD_DATA_COLLECTION						= "fieldData.collection";
	public static final String		FIELD_DATAVO_FIELD_DATA_COLLECTION_ID					= "fieldData.collection.id";
	public static final String		FIELD_DATAVO_FIELD_DATA_COLLECTION_NAME				= "fieldData.collection.name";
	public static final String		FIELD_DATAVO_FIELD_DATA_LINK_SUBJECT_STUDY			= "fieldData.linkSubjectStudy";
	public static final String		FIELD_DATAVO_FIELD_DATA_LINK_SUBJECT_STUDY_ID		= "fieldData.linkSubjectStudy.id";
	public static final String		FIELD_DATAVO_FIELD_DATA_SUBJECTUID						= "fieldData.linkSubjectStudy.subjectUID";
	public static final String		FIELD_DATAVO_FIELD_DATA_DATE_COLLECTED					= "fieldData.dateCollected";
	public static final String		FIELD_DATAVO_FIELD_DATA_FIELD								= "fieldData.field";
	public static final String		FIELD_DATAVO_FIELD_DATA_FIELD_ID							= "fieldData.field.id";
	public static final String		FIELD_DATAVO_FIELD_DATA_FIELD_NAME						= "fieldData.field.name";
	public static final String		FIELD_DATAVO_FIELD_DATA_VALUE								= "fieldData.value";
	public static final String		FIELD_DATAVO_FIELD_DATA_PASSED_QUALITY_CONTROL		= "fieldData.passedQualityControl";
	public static final String		FIELD_DATAVO_FIELD_DATA_UPDATE_USER						= "fieldData.user";
	public static final String		FIELD_DATAVO_FIELD_DATA_UPDATE_USER_ID					= "fieldData.user.id";
	public static final String		FIELD_DATAVO_FIELD_DATA_INSERT_TIME						= "fieldData.insertTime";
	public static final String		FIELD_DATAVO_FIELD_DATA_UPDATE_TIME						= "fieldData.updateTime";

	// UploadVO
	public static final String		UPLOADVO_UPLOAD_ID											= "upload.id";
	public static final String		UPLOADVO_UPLOAD_COLLECTION_UPLOAD						= "upload.collectionUpload";
	public static final String		UPLOADVO_UPLOAD_COLLECTION_UPLOAD_ID					= "upload.collectionUpload.id";
	public static final String		UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION			= "upload.collectionUpload.collection";
	public static final String		UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION_ID		= "upload.collectionUpload.collection.id";
	public static final String		UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION_NAME	= "upload.collectionUpload.collection.name";
	public static final String		UPLOADVO_UPLOAD_FILENAME									= "upload.filename";
	public static final String		UPLOADVO_UPLOAD_CUSTOM_FIELD_GROUP									= "upload.customFieldGroup";
	public static final String		UPLOADVO_UPLOAD_FILE_FORMAT								= "upload.fileFormat";
	public static final String		UPLOADVO_UPLOAD_FILE_FORMAT_ID							= "upload.fileFormat.id";
	public static final String		UPLOADVO_UPLOAD_FILE_FORMAT_NAME							= "upload.fileFormat.name";
	public static final String		UPLOADVO_UPLOAD_PAYLOAD										= "upload.payload";
	public static final String		UPLOADVO_UPLOAD_DELIMITER_TYPE							= "upload.delimiterType";
	public static final String		UPLOADVO_UPLOAD_USER											= "upload.user";
	public static final String		UPLOADVO_UPLOAD_USER_ID										= "upload.userId";
	public static final String		UPLOADVO_UPLOAD_INSERT_TIME								= "upload.insertTime";
	public static final String		UPLOADVO_UPLOAD_UPDATE_USER								= "upload.user";
	public static final String		UPLOADVO_UPLOAD_UPDATE_USER_ID							= "upload.user.id";
	public static final String		UPLOADVO_UPLOAD_UPDATE_TIME								= "upload.updateTime";
	public static final String		UPLOADVO_UPLOAD_CHECKSUM									= "upload.checksum";
	public static final String		UPLOADVO_UPLOAD_START_TIME									= "upload.startTime";
	public static final String		UPLOADVO_UPLOAD_FINISH_TIME								= "upload.finishTime";
	public static final String		UPLOADVO_UPLOAD_UPLOAD_REPORT								= "upload.uploadReport";
	public static final String		UPLOADVO_PHENO_COLLECTION									= "phenoCollection";
	public static final String			UPLOADVO_UPLOAD_UPLOAD_TYPE								= "upload.uploadType";
	public static final String			UPLOAD_TYPE_ID												= "id";
	public static final String			UPLOAD_TYPE_NAME												= "name";
	
	
	// Entity fields
	public static final String		PHENO_COLLECTION												= "phenoCollection";
	public static final String		PHENO_COLLECTION_ID											= "id";
	public static final String		PHENO_COLLECTION_STUDY										= "study";
	public static final String		PHENO_COLLECTION_NAME										= "name";
	public static final String		PHENO_COLLECTION_DESCRIPTION								= "description";
	public static final String		PHENO_COLLECTION_STATUS										= "status";
	public static final String		PHENO_COLLECTION_START_DATE								= "startDate";
	public static final String		PHENO_COLLECTION_END_DATE									= "endDate";
	public static final String		PHENO_COLLECTION_USER										= "user";
	public static final String		PHENO_COLLECTION_USER_ID									= "userId";
	public static final String		PHENO_COLLECTION_INSERT_TIME								= "insertTime";
	public static final String		PHENO_COLLECTION_UPDATE_USER								= "user";
	public static final String		PHENO_COLLECTION_UPDATE_USER_ID							= "userId";
	public static final String		PHENO_COLLECTION_UPDATE_TIME								= "updateTime";

	public static final String		PHENO_COLLECTION_UPLOAD										= "collectionUpload";
	public static final String		PHENO_COLLECTION_UPLOAD_ID									= "id";
	public static final String		PHENO_COLLECTION_UPLOAD_COLLECTION						= "collection";
	public static final String		PHENO_COLLECTION_UPLOAD_COLLECTION_ID					= "collection.id";
	public static final String		PHENO_COLLECTION_UPLOAD_UPLOAD							= "upload";
	public static final String		PHENO_COLLECTION_UPLOAD_UPLOAD_ID						= "upload.id";
	public static final String		PHENO_COLLECTION_UPLOAD_USER								= "user";
	public static final String		PHENO_COLLECTION_UPLOAD_USER_ID							= "userId";
	public static final String		PHENO_COLLECTION_UPLOAD_INSERT_TIME						= "insertTime";
	public static final String		PHENO_COLLECTION_UPLOAD_UPDATE_USER_ID					= "updateUserId";
	public static final String		PHENO_COLLECTION_UPLOAD_UPDATE_TIME						= "updateTime";
	
	public static final String		PHENOTYPIC_COLLECTION_ID									= "id";
	public static final String		PHENOTYPIC_COLLECTION_QUESTIONNAIRE_STATUS			= "";

	public static final String		FIELD																= "field";
	public static final String		FIELD_ID															= "id";
	public static final String		FIELD_STUDY														= "study";
	public static final String		FIELD_FIELD_TYPE												= "fieldType";
	public static final String		FIELD_NAME														= "name";
	public static final String		FIELD_DESCRIPTION												= "description";
	public static final String		FIELD_UNITS														= "units";
	public static final String		FIELD_SEQ_NUM													= "seqNum";
	public static final String		FIELD_MIN_VALUE												= "minValue";
	public static final String		FIELD_MAX_VALUE												= "maxValue";
	public static final String		FIELD_DISCRETE_VALUES										= "discreteValues";
	public static final String		FIELD_USER														= "user";
	public static final String		FIELD_INSERT_TIME												= "insertTime";
	public static final String		FIELD_UPDATE_USER												= "user";
	public static final String		FIELD_UPDATE_TIME												= "updateTime";

	public static final String		FIELD_TYPE														= "fieldType";
	public static final String		FIELD_TYPE_ID													= "id";
	public static final String		FIELD_TYPE_NAME												= "name";

	public static final String		FIELD_DATA														= "fieldData";
	public static final String		FIELD_DATA_ID													= "id";
	public static final String		FIELD_DATA_PHENO_COLLECTION								= "collection";
	public static final String		FIELD_DATA_LINK_SUBJECT_STUDY								= "linkSubjectStudy";
	public static final String		FIELD_DATA_DATE_COLLECTED									= "dateCollected";
	public static final String		FIELD_DATA_FIELD												= "field";
	public static final String		FIELD_DATA_VALUE												= "value";
	public static final String		FIELD_DATA_USER												= "user";
	public static final String		FIELD_DATA_INSERT_TIME										= "insertTime";
	public static final String		FIELD_DATA_UPDATE_USER										= "user";
	public static final String		FIELD_DATA_UPDATE_USER_ID									= "userId";
	public static final String		FIELD_DATA_UPDATE_TIME										= "updateTime";

	public static final String		STATUS															= "status";
	public static final String		STATUS_ID														= "id";
	public static final String		STATUS_NAME														= "name";

	public static final String		LINK_SUBJECT_STUDY											= "linkSubjectStudy";

	public static final String		FILE_FORMAT														= "fileFormat";
	public static final String		FILE_FORMAT_ID													= "id";
	public static final String		FILE_FORMAT_NAME												= "name";

	public static final String		UPLOAD															= "upload";
	public static final String		UPLOAD_ID														= "id";
	public static final String		UPLOAD_STUDY													= "study";
	public static final String		UPLOAD_FILE_FORMAT											= "fileFormat";
	public static final String		UPLOAD_DELIMITER_TYPE										= "delimiterType";
	public static final String		UPLOAD_FILENAME												= "filename";
	public static final String		UPLOAD_PAYLOAD													= "payload";
	public static final String		UPLOAD_USER														= "user";
	public static final String		UPLOAD_CHECKSUM												= "checksum";
	public static final String		UPLOAD_UPLOAD_TYPE											= "uploadType";

	public static final String		DELIMITER_TYPE_ID												= "id";
	public static final String		DELIMITER_TYPE_NAME											= "name";

	public static final String		DOWNLOAD_FILE													= "downloadFile";
	public static final String		DELETE_FILE														= "deleteFile";
	public static final String		DELETE															= "Delete";

	public static final String		FIELD_UPLOAD_ID												= "id";
	public static String				FIELD_UPLOAD_UPLOAD											= "upload";

	public static final String[]	DATA_DICTIONARY_HEADER										= { "FIELD_NAME", "FIELD_TYPE", "DESCRIPTION", "UNITS", "ENCODED_VALUES", "MINIMUM_VALUE", "MAXIMUM_VALUE",
			"MISSING_VALUE"																				};

	public static final String		FIELD_PHENO_COLLECTION										= "fieldPhenoCollection";
	public static final String		FIELD_PHENO_COLLECTION_FIELD								= "field";
	public static final String		FIELD_PHENO_COLLECTION_STUDY								= "study";
	public static final String		FIELD_PHENO_COLLECTION_PHENO_COLLECTION				= "phenoCollection";
	
	public static final String		QUESTIONNAIRE_ID											= "id";
	public static final String		QUESTIONNAIRE_NAME										= "name";

	public static final String[]	PHENO_TEMPLATE_CELLS	= {"SUBJECT_UID","REQUIRED_FIELD_1","ETC"};

	public static final String		PHENOCOLLECTION_STATUS_IN_PROGRESS						= "In Progress";//TODO check

	public static final String 		CUSTOM_FIELD_GROUP 										= "customFieldGroup";

}
