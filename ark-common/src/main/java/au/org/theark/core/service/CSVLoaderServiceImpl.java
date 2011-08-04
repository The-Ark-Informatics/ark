package au.org.theark.core.service;

import java.io.File;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import au.org.theark.core.Constants;
import au.org.theark.core.dao.ICSVLoaderDao;
import au.org.theark.core.model.study.entity.CsvBlob;

/**
 * The implementation of ICSVLoaderService. We want to auto-wire and hence use the @Service annotation.
 * 
 * @author cellis
 * 
 */
@Transactional
@Service(Constants.ARK_CSV_LOADER_SERVICE)
public class CSVLoaderServiceImpl implements ICSVLoaderService {
	private ICSVLoaderDao	iCSVLoaderDao;
	
	public ICSVLoaderDao getiCSVLoaderDao() {
		return iCSVLoaderDao;
	}

	@Autowired
	public void setiCSVLoaderDao(ICSVLoaderDao iCSVLoaderDao) {
		this.iCSVLoaderDao = iCSVLoaderDao;
	}

	public void loadCsvFileAndInsertSQL(File csvFile, String databaseName, String tableName) throws Exception {
		iCSVLoaderDao.loadCsvFileAndInsertSQL(csvFile, databaseName, tableName);
	}

	public void writeBlobToTempFile(String databaseName, Long id, String temporaryFileName, char delimiterCharacter) {
		iCSVLoaderDao.writeBlobToTempFile(databaseName, id, temporaryFileName, delimiterCharacter);
	}

	public void loadTempFileToDatabase(String temporaryFileName, String temporaryTableName) {
		iCSVLoaderDao.loadTempFileToDatabase(temporaryFileName, temporaryTableName);
	}

	public void createTemporaryTable(String temporaryTableName, List<String> columnNameList) {
		iCSVLoaderDao.createTemporaryTable(temporaryTableName, columnNameList);
	}

	public Long createCsvBlob(CsvBlob csvBlob) {
		return iCSVLoaderDao.createCsvBlob(csvBlob);
	}
}