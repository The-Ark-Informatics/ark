package au.org.theark.core.model;

// Constants for Entity-to-database definitions
public class Constants {
	// Pheno
	public static final String PHENO_TABLE_SCHEMA = "pheno";
	
	// Geno
	public static final String GENO_TABLE_SCHEMA = "GENO";
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

	// Reporting
	public static final String REPORT_TABLE_SCHEMA = "reporting";

	// Lims
	public static final String LIMS_TABLE_SCHEMA = "lims";
	public static final String	LIMS_COLLECTION_PK_SEQ	= LIMS_TABLE_SCHEMA + ".COLLECTION_PK_SEQ";
}
