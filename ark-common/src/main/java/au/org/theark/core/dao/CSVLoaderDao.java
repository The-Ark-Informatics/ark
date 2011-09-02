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
package au.org.theark.core.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.math.BigInteger;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
import org.hibernate.JDBCException;
import org.hibernate.Session;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Repository;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.CsvBlob;

/**
 * 
 * @author cellis
 * 
 */
@Repository("CSVLoaderDao")
public class CSVLoaderDao extends HibernateSessionDao implements ICSVLoaderDao {
	private static final Logger	log						= LoggerFactory.getLogger(CSVLoaderDao.class);
	private static String			SQL_INSERT				= "INSERT INTO ${schema}.${table}(${keys}) VALUES(${values})";
	private static final String	SCHEMA_REGEX			= "\\$\\{schema\\}";
	private static final String	TABLE_REGEX				= "\\$\\{table\\}";
	private static final String	KEYS_REGEX				= "\\$\\{keys\\}";
	private static final String	VALUES_REGEX			= "\\$\\{values\\}";
	private char						delimiterCharacter	= Constants.DEFAULT_DELIMITER_CHARACTER;

	public void loadCsvFileAndInsertSQL(File csvFile, String databaseName, String tableName) throws Exception {
		String filename = csvFile.getName();
		log.info("Reading contents of :" + filename);
		log.info("Obtained database name: " + databaseName);
		log.info("Obtained table name: " + tableName);

		BufferedReader in = new BufferedReader(new FileReader(csvFile));

		// First line will always be the column names
		String keys = in.readLine();
		if (keys == null || keys.length() == 0) {
			throw new Exception("No columns defined in :" + filename);
		}

		// trailing garbage plus " from column names
		keys = keys.replaceAll("\"", "");
		String insertTemplate = SQL_INSERT.replaceFirst(SCHEMA_REGEX, databaseName);
		insertTemplate = insertTemplate.replaceFirst(TABLE_REGEX, tableName);
		insertTemplate = insertTemplate.replaceFirst(KEYS_REGEX, keys);
		String values = null;
		while ((values = in.readLine()) != null) {
			values = values.replaceAll("\"", "\\\'");
			insertIntoDatabaseByCreateSQLQuery(insertTemplate.replaceFirst(VALUES_REGEX, values));
		}
	}

	/**
	 * Calls hibernate and inserts the data into the database
	 * 
	 * @param statement
	 */
	private void insertIntoDatabaseByCreateSQLQuery(String statement) {
		log.info(statement);
		Session session = getSession();
		session.beginTransaction();
		session.createSQLQuery(statement).executeUpdate();
		session.flush();
		log.info("SQL insertIntoDatabase SUCCEEDED");
	}

	public void writeBlobToTempFile(String databaseName, Long id, String temporaryFileName, char delimiterCharacter) {
		if (databaseName != null && id != null) {
			Session session = getSession();
			session.beginTransaction();
			
			StringBuffer sqlBlobToFile = new StringBuffer();
			/*
			sqlBlobToFile.append("SELECT csv_blob ");
			sqlBlobToFile.append("FROM study.csv_blob ");
			sqlBlobToFile.append("WHERE id = ");
			sqlBlobToFile.append(id.intValue());
			sqlBlobToFile.append(" ");
			sqlBlobToFile.append("INTO OUTFILE '");
			sqlBlobToFile.append(temporaryFileName);
			sqlBlobToFile.append("' ");
			
			sqlBlobToFile.append("CALL ");
			sqlBlobToFile.append(databaseName);
			sqlBlobToFile.append(".");
			sqlBlobToFile.append("export_csv_blob(");
			sqlBlobToFile.append(id.intValue());
			sqlBlobToFile.append(")");
			
			try {
				log.info("Writing out file: " + temporaryFileName);
				log.info(sqlBlobToFile.toString());
				session.createSQLQuery(sqlBlobToFile.toString()).executeUpdate();
				log.info("SQL writeBlobToTempFile SUCCEEDED");
			}
			catch(JDBCException e) {
				log.error(e.getMessage());
				log.error("SQL writeBlobToTempFile FAILED");
			}
			finally {
				session.flush();
			}
			*/		
		}
	}

	/**
	 * Load the temporary created file back into the database, to temporary table, using the [LOAD DATA INFILE] SQL statement
	 * @param temporaryFileName
	 * @param databaseName
	 * @param temporaryTableName
	 * @return the number fo rows in the table
	 */
	public int loadTempFileToDatabase(String temporaryFileName, String databaseName, String temporaryTableName) {
		int rowCount = 0;
		
		StringBuffer tableName = new StringBuffer();
		tableName.append(databaseName);
		tableName.append(".");
		tableName.append(temporaryTableName);
		
		if (temporaryTableName != null && temporaryTableName != null) {
			Session session = getSession();
			session.beginTransaction();
			StringBuffer sqlTempFileToTable = new StringBuffer();
			sqlTempFileToTable.append("LOAD DATA LOCAL INFILE '");
			sqlTempFileToTable.append(temporaryFileName);
			sqlTempFileToTable.append("' INTO TABLE ");
			sqlTempFileToTable.append(tableName.toString());
			sqlTempFileToTable.append(" FIELDS TERMINATED BY '");
			sqlTempFileToTable.append(delimiterCharacter);
			sqlTempFileToTable.append("' ENCLOSED BY '\"' ");
			sqlTempFileToTable.append("LINES TERMINATED BY '\\n' ");
			sqlTempFileToTable.append("IGNORE 1 LINES;");
			
			try {
				log.info("Loading data into temporary table: " + tableName);
				session.createSQLQuery(sqlTempFileToTable.toString()).executeUpdate();
				log.info("select count(*) from " + tableName);
				BigInteger rowInteger = (BigInteger) session.createSQLQuery("SELECT count(*) from " + tableName.toString()).uniqueResult();
				rowCount = rowInteger.intValue();
				
				log.info("SQL loadTempFileToDatabase SUCCEEDED");
			}
			catch(JDBCException e) {
				log.error(e.getMessage());
				log.error("SQL loadTempFileToDatabase FAILED");
			}
			finally {
				session.flush();
			}
		}
		log.info("Rowcount: " + rowCount);
		
		return rowCount;
	}

	/**
	 * Create a temporary table to store data from an external file into
	 * @param databaseName
	 * @param temporaryTableName
	 * @param columnNameList
	 */
	public void createTemporaryTable(String databaseName, String temporaryTableName, List<String> columnNameList) {
		if (temporaryTableName != null && !columnNameList.isEmpty()) {
			Session session = getSession();
			session.beginTransaction();
			
			StringBuffer sqlDropTemporyTable = new StringBuffer();
			sqlDropTemporyTable.append("DROP TABLE ");
			sqlDropTemporyTable.append(databaseName);
			sqlDropTemporyTable.append(".");
			sqlDropTemporyTable.append(temporaryTableName);
			
			StringBuffer sqlCreateTemporyTable = new StringBuffer();
			//sqlCreateTemporyTable.append("CREATE TEMPORARY TABLE ");
			sqlCreateTemporyTable.append("CREATE TEMPORARY TABLE ");
			sqlCreateTemporyTable.append(databaseName);
			sqlCreateTemporyTable.append(".");
			sqlCreateTemporyTable.append(temporaryTableName);
			sqlCreateTemporyTable.append(" (");

			StringBuffer colNameAndType = new StringBuffer();

			for (Iterator<String> iterator = columnNameList.iterator(); iterator.hasNext();) {
				String columnName = (String) iterator.next();
				colNameAndType.append(columnName);
				colNameAndType.append(" varchar(255)");
				colNameAndType.append(delimiterCharacter);
				colNameAndType.append("\n");
			}
			colNameAndType.deleteCharAt(colNameAndType.lastIndexOf(","));

			sqlCreateTemporyTable.append(colNameAndType);
			sqlCreateTemporyTable.append(");");
			
			try {
				session.createSQLQuery(sqlDropTemporyTable.toString()).executeUpdate();
				log.info("SQL dropTemporaryTable SUCCEEDED");
			}
			catch(JDBCException e) {
				log.error(e.getMessage());
				log.info("SQL dropTemporaryTable FAILED");
			}
			
			try {
				session.createSQLQuery(sqlCreateTemporyTable.toString()).executeUpdate();
				log.info("SQL createTemporaryTable SUCCEEDED");
			}
			catch(JDBCException e) {
				log.error(e.getMessage());
				log.info("SQL createTemporaryTable FAILED");
			}
			finally {
				session.flush();
			}
		}
	}

	public Long createCsvBlob(CsvBlob csvBlob) {
		getSession().save(csvBlob);
		return csvBlob.getId();
	}

	public CsvBlob getCsvBlob(Long id) {
		Criteria criteria = getSession().createCriteria(CsvBlob.class);
		criteria.add(Restrictions.eq("id", id));
		CsvBlob csvBlob = (CsvBlob) criteria.list().get(0);
		return csvBlob;
	}
}
