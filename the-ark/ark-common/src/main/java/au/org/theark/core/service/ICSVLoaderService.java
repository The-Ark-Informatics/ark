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
package au.org.theark.core.service;

import java.io.File;
import java.util.List;

import au.org.theark.core.model.study.entity.CsvBlob;

public interface ICSVLoaderService {
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
	 * Load the temporary created file back into the database, to temporary table, using the [LOAD DATA INFILE] SQL statement
	 * @param temporaryFileName
	 * @param databaseName
	 * @param temporaryTableName
	 * @return the number of rows in the table
	 */
	public int loadTempFileToDatabase(String temporaryFileName, String databaseName, String temporaryTableName);
	
	/**
	 * Creates a temporary table for the session
	 * @param databaseName
	 * @param temporaryTableName
	 * @param columList
	 */
	public void createTemporaryTable(String databaseName, String temporaryTableName, List<String> columnNameList);

	/**
	 * Store a file as CSV in the database
	 * @param csvBlob
	 * @return The id of the newly created entity
	 */
	public Long createCsvBlob(CsvBlob csvBlob);
}
