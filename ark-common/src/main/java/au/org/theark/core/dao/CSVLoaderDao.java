package au.org.theark.core.dao;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.hibernate.Criteria;
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
			int success = 1;

			CsvBlob csvBlob = getCsvBlob(id);

			try {
				File blobFile = new File(temporaryFileName);
				FileOutputStream outStream = new FileOutputStream(blobFile);
				InputStream inStream = csvBlob.getCsvBlob().getBinaryStream();

				int length = -1;
				int size = (int) csvBlob.getCsvBlob().length();
				byte[] buffer = new byte[size];

				while ((length = inStream.read(buffer)) != -1) {
					outStream.write(buffer, 0, length);
					outStream.flush();
				}

				inStream.close();
				outStream.close();
			}
			catch (Exception e) {
				log.error(e.getMessage());
				log.error("ERROR(djv_exportBlob) Unable to export:" + temporaryFileName);
				success = 0;
			}
			finally {

			}

		}
		log.info("SQL writeBlobToTempFile SUCCEEDED");
	}

	public void loadTempFileToDatabase(String temporaryFileName, String temporaryTableName) {
		if (temporaryTableName != null && temporaryTableName != null) {
			StringBuffer sqlTempFileToTable = new StringBuffer();
			sqlTempFileToTable.append("LOAD DATA INFILE '");
			sqlTempFileToTable.append(temporaryFileName);
			sqlTempFileToTable.append("' INTO TABLE ");
			sqlTempFileToTable.append(temporaryTableName);
			sqlTempFileToTable.append(" FIELDS TERMINATED BY '");
			sqlTempFileToTable.append(delimiterCharacter);
			sqlTempFileToTable.append("' ENCLOSED BY '\"' ");
			sqlTempFileToTable.append("LINES TERMINATED BY '\n' ");
			sqlTempFileToTable.append("IGNORE 1 LINES;");
		}
	}

	public void createTemporaryTable(String temporaryTableName, List<String> columnNameList) {
		if (temporaryTableName != null && !columnNameList.isEmpty()) {
			Session session = getSession();
			session.beginTransaction();
			StringBuffer sqlCreateTemporyTable = new StringBuffer();
			sqlCreateTemporyTable.append("CREATE TEMPORARY TABLE  ");
			sqlCreateTemporyTable.append(temporaryTableName);
			sqlCreateTemporyTable.append("(");

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
			session.createSQLQuery(sqlCreateTemporyTable.toString()).executeUpdate();
			session.flush();
			log.info("SQL createTemporaryTable SUCCEEDED");
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