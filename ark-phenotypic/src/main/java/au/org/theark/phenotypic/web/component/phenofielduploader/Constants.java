package au.org.theark.phenotypic.web.component.phenofielduploader;



public class Constants {
	// relevant to components
	public static final String	DELETE_FILE													= "deleteFile";
	public static final String	DELETE														= "Delete";

	// relevant to beans/entities
	public static final String	FILE_FORMAT													= "fileFormat";
	public static final String	FILE_FORMAT_ID												= "id";
	public static final String	FILE_FORMAT_NAME											= "name";

	public static final String	DELIMITER_TYPE_ID											= "id";
	public static final String	DELIMITER_TYPE_NAME											= "name";

	// UploadVO
	public static final String	UPLOADVO_UPLOAD_ID											= "upload.id";
	public static final String	UPLOADVO_UPLOAD_COLLECTION_UPLOAD							= "upload.collectionUpload";
	public static final String	UPLOADVO_UPLOAD_COLLECTION_UPLOAD_ID						= "upload.collectionUpload.id";
	public static final String	UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION				= "upload.collectionUpload.collection";
	public static final String	UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION_ID				= "upload.collectionUpload.collection.id";
	public static final String	UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION_NAME			= "upload.collectionUpload.collection.name";
	public static final String	UPLOADVO_UPLOAD_FILENAME									= "upload.filename";
	public static final String	UPLOADVO_UPLOAD_CUSTOM_FIELD_GROUP							= "upload.customFieldGroup";
	public static final String	UPLOADVO_UPLOAD_FILE_FORMAT									= "upload.fileFormat";
	public static final String	UPLOADVO_UPLOAD_FILE_FORMAT_ID								= "upload.fileFormat.id";
	public static final String	UPLOADVO_UPLOAD_FILE_FORMAT_NAME							= "upload.fileFormat.name";
	public static final String	UPLOADVO_UPLOAD_PAYLOAD										= "upload.payload";
	public static final String	UPLOADVO_UPLOAD_DELIMITER_TYPE								= "upload.delimiterType";
	public static final String	UPLOADVO_UPLOAD_UPLOAD_TYPE									= "upload.uploadType";
	public static final String	UPLOADVO_UPLOAD_USER										= "upload.user";
	public static final String	UPLOADVO_UPLOAD_INSERT_TIME									= "upload.insertTime";
	public static final String	UPLOADVO_UPLOAD_CHECKSUM									= "upload.checksum";
	public static final String	UPLOADVO_UPLOAD_START_TIME									= "upload.startTime";
	public static final String	UPLOADVO_UPLOAD_FINISH_TIME									= "upload.finishTime";
	public static final String	UPLOADVO_UPLOAD_UPLOAD_STATUS_NAME							= "upload.uploadStatus.name";
	public static final String	UPLOADVO_UPLOAD_UPLOAD_REPORT								= "upload.uploadReport";
	public static final String	UPLOADVO_UPLOAD_COLLECTION									= "upload.uploadCollection";
	public static final String	RESULT_LIST													= "resultList";
	public static final String	UPLOADVO_UPLOAD_USER_ID										= "upload.userId";
	public static final String	UPLOADVO_UPLOAD_UPDATE_USER									= "upload.user";
	public static final String	UPLOADVO_UPLOAD_UPDATE_USER_ID								= "upload.user.id";
	public static final String	UPLOADVO_UPLOAD_UPDATE_TIME									= "upload.updateTime";
	//Add new custom type(category or field)	
	public static final String	UPLOADVO_UPLOAD_UPLOAD_LEVEL								= "upload.uploadLevel";
	public static final String	UPLOAD_LEVEL_ID												= "id";
	public static final String	UPLOAD_LEVEL_NAME											= "name";
	public static final String	UPLOAD_LEVEL_FIELD											= "Field";
	public static final String	UPLOAD_LEVEL_CATEGORY										= "Category";
	public static final String FIELD_TYPE_CHARACTER 										= "Character";
	
	public static final String[][] PHENO_DATASET_CATEGORY_UPLOAD_HEADER={ { "","CATEGORY_NAME","DESCRIPTION"},
		{
			"DESCRIPTION",
			"The unique identifier of the category",
			"The details description of the category",
		},
		{
			"MANDATORY", 
			"Yes", 
			"No",
		},
		{
			"VALID VALUES",
			"", 
			"", 
		},
		{ "NOTE: Removed this first column, and replace rows 2 to 5.",
				"", "", "", "",""
		},
		{
			"Category ordering and categorising(parent/child) are done under the data set definition tab.",
				 "", "", "", "",""
		}
	};
	
	public static final String[][] PHENO_DATASET_FIELD_UPLOAD_HEADER = {{"", "FIELD_NAME","FIELD_TYPE", "DESCRIPTION", "QUESTION", "UNITS", "ENCODED_VALUES",
		"MINIMUM_VALUE", "MAXIMUM_VALUE", "MISSING_VALUE", "DEFAULT_VALUE","REQUIRED", "ALLOW_MULTIPLE_SELECTIONS","MULTI_LINE_DISPLAY"},
		{
			"DESCRIPTION",
			"The unique identifier of the field",
			"The data type of the field",
			"A text description of the field",
			"The question or label text associated with the field ",
			"The units of measurement, if applicable",
			"A specification of encoded values, optional when FIELD_TYPE is CHARACTER ",
			"The minimum value for the field, optional when FIELD_TYPE is NUMBER & DATE",
			"The maximum value for the field, optional when FIELD_TYPE is NUMBER & DATE",
			"A value that represents missing data; the data type must match that indicated by FIELD_TYPE",
			"The default value for the field if no value is set",
			"Whether values for this field are mandatory",
			"If FIELD_TYPE is CHARACTER and encoded values are set, should multiple selections be allowed?",
			"If FIELD_TYPE is CHARACTER and encoded values are not set, should multi-line display be allowed?"
	},
	{ 		"MANDATORY", 
			"Yes", 
			"No", 
			"No",
			"No",
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
			"Character, Number, Date",
			"Maximum 255 characters", 
			"Maximum 255 characters", 
			"Maximum 50 characters",
			"Example: 0=No;1=Yes;",
			"",
			"",
			"",
			"",
			"Yes, No",
			"Yes, No",
			"Yes, No"
	},
	{ "NOTE: Remove this first column, and replace the contents of rows 2 to 5", "", "", "", "", "", "", "", "", "", "", "","",""
	}};
	
	public static final String			UPLOAD_TYPE_CUSTOM_DATA_SETS	= "Custom Datasets";

}
