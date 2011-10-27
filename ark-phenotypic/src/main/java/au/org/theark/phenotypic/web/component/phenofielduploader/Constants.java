package au.org.theark.phenotypic.web.component.phenofielduploader;

public class Constants {
	public static final String[]	CUSTOMFIELD_UPLOAD_HEADER									= { "FIELD_NAME", "FIELD_TYPE", "DESCRIPTION", 
																													"UNITS", "ENCODED_VALUES", "MINIMUM_VALUE", 
																													"MAXIMUM_VALUE", "MISSING_VALUE" };

	// relevant to components
	public static final String		DELETE_FILE														= "deleteFile";
	public static final String		DELETE															= "Delete";
	
	// relevant to beans/entities
	public static final String		FILE_FORMAT														= "fileFormat";
	public static final String		FILE_FORMAT_ID													= "id";
	public static final String		FILE_FORMAT_NAME												= "name";
	
	public static final String		DELIMITER_TYPE_ID												= "id";
	public static final String		DELIMITER_TYPE_NAME											= "name";

	// UploadVO
	public static final String		UPLOADVO_UPLOAD_ID											= "upload.id";
	public static final String		UPLOADVO_UPLOAD_COLLECTION_UPLOAD						= "upload.collectionUpload";
	public static final String		UPLOADVO_UPLOAD_COLLECTION_UPLOAD_ID					= "upload.collectionUpload.id";
	public static final String		UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION			= "upload.collectionUpload.collection";
	public static final String		UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION_ID		= "upload.collectionUpload.collection.id";
	public static final String		UPLOADVO_UPLOAD_COLLECTION_UPLOAD_COLLECTION_NAME	= "upload.collectionUpload.collection.name";
	public static final String		UPLOADVO_UPLOAD_FILENAME									= "upload.filename";
	public static final String		UPLOADVO_UPLOAD_FILE_FORMAT								= "upload.fileFormat";
	public static final String		UPLOADVO_UPLOAD_FILE_FORMAT_ID							= "upload.fileFormat.id";
	public static final String		UPLOADVO_UPLOAD_FILE_FORMAT_NAME							= "upload.fileFormat.name";
	public static final String		UPLOADVO_UPLOAD_PAYLOAD										= "upload.payload";
	public static final String		UPLOADVO_UPLOAD_DELIMITER_TYPE							= "upload.delimiterType";
	public static final String		UPLOADVO_UPLOAD_USER											= "upload.user";
	public static final String		UPLOADVO_UPLOAD_INSERT_TIME								= "upload.insertTime";
	public static final String		UPLOADVO_UPLOAD_CHECKSUM									= "upload.checksum";
	public static final String		UPLOADVO_UPLOAD_START_TIME									= "upload.startTime";
	public static final String		UPLOADVO_UPLOAD_FINISH_TIME								= "upload.finishTime";
	public static final String		UPLOADVO_UPLOAD_UPLOAD_REPORT								= "upload.uploadReport";
	public static final String		UPLOADVO_UPLOAD_COLLECTION									= "upload.uploadCollection";
	public static final String 		RESULT_LIST													= "resultList";
	
}
