package au.org.theark.phenotypic.service;

public class Constants {
	public static final String TABLE_SCHEMA = "pheno";
	
	public static final String STATUS_CREATED = "Created"; 
	public static final String STATUS_ACTIVE = "Active"; 
	public static final String STATUS_EXPIRED = "Expired"; 
	public static final String STATUS_DISABLED = "Disabled"; 
	
	// Attributes
	public static final String COLLECTION_ID = "collectionId";
	
	// Testing
	public static final String TEST_FILE = "/home/ark/TestData/testPhenoData.txt";
	
	// Services
	public static final String PHENOTYPIC_SERVICE = "phenotypicService";
	
	// Exception messages
	public static final String IO_EXCEPTION = "IOException: Input error. ";
	public static final String FILE_FORMAT_EXCEPTION = "File Format Exception: Input error. ";
	public static final String PHENOTYPIC_SYSTEM_EXCEPTION = "Phenotypic System Exception: Input error. ";
}
