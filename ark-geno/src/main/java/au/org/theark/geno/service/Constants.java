package au.org.theark.geno.service;


public class Constants {
	
	public static final String TEST = "Test1";
	public static final String STATUS_CREATED = "Created"; 
	public static final String STATUS_ACTIVE = "Active"; 
	public static final String STATUS_EXPIRED = "Expired"; 
	public static final String STATUS_DISABLED = "Disabled"; 

	public static final String GENO_TABLE_SCHEMA = "GDMI";	//TODO: To be renamed to "GENO";
	public static final String COLLECTION_PK_SEQ =  GENO_TABLE_SCHEMA + ".COLLECTION_PK_SEQ";
	public static final String COLLECTION_IMPORT_PK_SEQ =  GENO_TABLE_SCHEMA + ".COLLECTION_IMPORT_PK_SEQ";
	public static final String ENCODED_DATA_PK_SEQ = GENO_TABLE_SCHEMA + ".ENCODED_DATA_PK_SEQ";
	public static final String MARKER_PK_SEQ = GENO_TABLE_SCHEMA + ".MARKER_PK_SEQ";
	public static final String MARKER_GROUP_PK_SEQ = GENO_TABLE_SCHEMA + ".MARKER_GROUP_PK_SEQ";
	public static final String META_DATA_PK_SEQ = GENO_TABLE_SCHEMA + ".META_DATA_PK_SEQ";
	public static final String META_DATA_FIELD_PK_SEQ = GENO_TABLE_SCHEMA + ".META_DATA_FIELD_PK_SEQ";
	public static final String META_DATA_TYPE_PK_SEQ = GENO_TABLE_SCHEMA + ".META_DATA_TYPE_PK_SEQ";
	public static final String UPLOAD_PK_SEQ = GENO_TABLE_SCHEMA + ".UPLOAD_PK_SEQ";
	public static final String UPLOAD_COLLECTION_PK_SEQ = GENO_TABLE_SCHEMA + ".UPLOAD_COLLECTION_PK_SEQ";
	public static final String UPLOAD_MARKER_PK_SEQ = GENO_TABLE_SCHEMA + ".UPLOAD_MARKER_PK_SEQ";
	
}
