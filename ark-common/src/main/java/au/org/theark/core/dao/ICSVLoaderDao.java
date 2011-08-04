package au.org.theark.core.dao;

import java.io.File;
import java.util.List;

import au.org.theark.core.model.study.entity.CsvBlob;

/**
 * @author cellis
 *
 */
public interface ICSVLoaderDao {
	/**
	 * Loads a CSV file in to the specified database vi SQL insert statements for each row
	 * 
	 * @param csvFile
	 * @param databaseName
	 * @param tableName
	 * @throws Exception
	 */
	public void loadCsvFileAndInsertSQL(File csvFile, String databaseName, String tableName) throws Exception;
	
	/**
	 * Creates a new temporary text file of the BLOB of the specified database table reference on the database server
	 * 
	 * @param databaseName
	 *           The schema/database
	 * @param id
	 *           The unique identifier of the table that contains the BLOB
	 * @param temporaryFileName
	 *           The full path name to write the contents of the BLOB to
	 * @param temporaryFileName
	 *           The file field delimiter (comma, tab, pipe etc)
	 */
	public void writeBlobToTempFile(String databaseName, Long id, String temporaryFileName, char delimiterCharacter);
	
	/**
	 * Load a temporary created file on the database server back into the database as new temporary table
	 * @param temporaryFileName
	 * @param temporaryTableName
	 */
	public void loadTempFileToDatabase(String temporaryFileName, String temporaryTableName);
	
	/**
	 * Creates a temporary table for the session
	 * @param temporaryTableName
	 * @param columList
	 */
	public void createTemporaryTable(String temporaryTableName, List<String> columnNameList);

	/**
	 * Store a file as CSV in the database
	 * @param csvBlob
	 * @return The id of the newly created entity
	 */
	public Long createCsvBlob(CsvBlob csvBlob);
}
