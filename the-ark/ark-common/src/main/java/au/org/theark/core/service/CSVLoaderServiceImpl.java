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

	public int loadTempFileToDatabase(String databaseName, String temporaryFileName, String temporaryTableName) {
		return iCSVLoaderDao.loadTempFileToDatabase(databaseName, temporaryFileName, temporaryTableName);
	}

	public void createTemporaryTable(String databaseName, String temporaryTableName, List<String> columnNameList) {
		iCSVLoaderDao.createTemporaryTable(databaseName, temporaryTableName, columnNameList);
	}

	public Long createCsvBlob(CsvBlob csvBlob) {
		return iCSVLoaderDao.createCsvBlob(csvBlob);
	}
}
