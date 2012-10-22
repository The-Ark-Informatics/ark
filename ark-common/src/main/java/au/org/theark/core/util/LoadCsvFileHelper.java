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
package au.org.theark.core.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.util.file.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.service.ICSVLoaderService;

import com.csvreader.CsvReader;

public class LoadCsvFileHelper {
	private static final Logger	log						= LoggerFactory.getLogger(LoadCsvFileHelper.class);
	private ICSVLoaderService		iCSVLoaderService;

	private File						csvFile;
	private String						databaseName;
	private char						delimiterCharacter	= Constants.DEFAULT_DELIMITER_CHARACTER;
	private CsvReader					csvReader				= null;
	private String						temporaryFileName;
	private String						temporaryTableName;
	private List<String>				columnNameList			= new ArrayList<String>(0);

	/**
	 * Default constructor
	 * 
	 * @param iCSVLoaderService
	 *           The service required for Hibernate access
	 * @param databaseName
	 *           The schema/database name to where the data resides
	 */
	public LoadCsvFileHelper(ICSVLoaderService iCSVLoaderService, String databaseName) {
		this.iCSVLoaderService = iCSVLoaderService;
		this.databaseName = databaseName;
	}

	/**
	 * Constructor. Takes in a fileUpload as a parameter and attempts to load data into the database
	 * 
	 * @param iCSVLoaderService
	 *           The service required for Hibernate access
	 * @param databaseName
	 *           The schema/database name to where the data resides
	 * @param delimiterCharacter
	 *           The file field delimiter (comma, tab, pipe etc)
	 */
	public LoadCsvFileHelper(ICSVLoaderService iCSVLoaderService, String databaseName, char delimiterCharacter) {
		this.iCSVLoaderService = iCSVLoaderService;
		this.databaseName = databaseName;
	}

	/**
	 * Constructor. Takes in a csvFile as a parameter and attempts to load data into the database
	 * 
	 * @param csvFile
	 *           The comma separated file reference
	 * @param databaseName
	 *           The schema/database name to where the data resides
	 */
	public LoadCsvFileHelper(File csvFile, String databaseName) {
		this.csvFile = csvFile;
		this.databaseName = databaseName;
		this.columnNameList = getColumnsFromHeader();
	}

	/**
	 * Convert the specified fileUpload to CSV format, and save as a CsvBlob
	 * 
	 * @param fileUpload
	 * @param delimiterCharacter
	 */
	public void convertToCSVAndWriteToFile(FileUpload fileUpload, char delimiterCharacter) {
		String filename = fileUpload.getClientFileName();
		String fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
		InputStream inputStream = null;

		try {
			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS")) {
				inputStream = convertXlsInputStreamToCsv(fileUpload.getInputStream());
			}
			else {
				inputStream = fileUpload.getInputStream();
			}
			
			writeToTempFile(inputStream);
		}
		catch (IOException e) {
			log.error(".convertToCSVAndSave IOException: " + e.getMessage());
		}

		this.columnNameList = getColumnsFromHeader(inputStream, delimiterCharacter);
	}
	
	/**
	 * Saves this file upload to a given file on the server side.
	 * 
	 * @param inputStream
	 *            The input stream
	 * @throws IOException
	 */
	public void writeToTempFile(final InputStream inputStream) throws IOException
	{
		log.info("Writing to temp file: " + temporaryFileName + ".tmp");
		File file = File.createTempFile(temporaryFileName, null);
		int bufSize = 4096;
		InputStream is = inputStream;
		try
		{
			Files.writeTo(file, is, bufSize);
		}
		finally
		{
			is.close();
		}
	}

	public int createTemporaryTable() {
		int rowCount = 0;
		iCSVLoaderService.createTemporaryTable(databaseName, temporaryTableName, this.columnNameList);
		rowCount = iCSVLoaderService.loadTempFileToDatabase(temporaryFileName, databaseName, temporaryTableName);
		return rowCount;
	}

	/**
	 * Converts an XLS inputStream to a CSV file
	 * 
	 * @param inputStream
	 */
	public InputStream convertXlsInputStreamToCsv(InputStream inputStream) {
		Workbook w;
		try {
			w = Workbook.getWorkbook(inputStream);
			delimiterCharacter = ',';
			XLStoCSV xlsToCsv = new XLStoCSV(delimiterCharacter);
			inputStream = xlsToCsv.convertXlsToCsv(w);
			inputStream.reset();
		}
		catch (BiffException e) {
			log.error(e.getMessage());
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return inputStream;
	}

	public File getCsvFile() {
		return csvFile;
	}

	public void setCsvFile(File csvFile) {
		this.csvFile = csvFile;
	}

	public String getDatabaseName() {
		return databaseName;
	}

	public void setDatabaseName(String databaseName) {
		this.databaseName = databaseName;
	}

	private List<String> getColumnsFromHeader() {
		List<String> columnsFromHeader = new ArrayList<String>(0);
		try {
			csvReader = new CsvReader(temporaryFileName, delimiterCharacter);
			String[] headerArray = csvReader.getHeaders();
			for (int i = 0; i < headerArray.length; i++) {
				columnsFromHeader.add(headerArray[i]);
			}
		}
		catch (FileNotFoundException e) {
			log.error(".getColumnsFromHeader FileNotFoundException " + e.getMessage());
		}
		catch (IOException e) {
			log.error(".getColumnsFromHeader IOException " + e.getMessage());
		}
		return columnsFromHeader;
	}

	private List<String> getColumnsFromHeader(InputStream inputStream, char delimiterCharacter) {
		List<String> columnsFromHeader = new ArrayList<String>(0);
		try {
			csvReader = new CsvReader(inputStream, delimiterCharacter, Charset.defaultCharset());
			csvReader.readHeaders();
			String[] headerArray = csvReader.getHeaders();
			for (int i = 0; i < headerArray.length; i++) {
				columnsFromHeader.add(headerArray[i]);
			}
		}
		catch (FileNotFoundException e) {
			log.error(".getColumnsFromHeader FileNotFoundException " + e.getMessage());
		}
		catch (IOException e) {
			log.error(".getColumnsFromHeader IOException " + e.getMessage());
		}
		catch (NullPointerException e) {
			log.error(".getColumnsFromHeader NullPointerException " + e.getMessage());
		}
		return columnsFromHeader;
	}

	/**
	 * @param temporaryTableName
	 *           the temporaryTableName to set
	 */
	public void setTemporaryTableName(String temporaryTableName) {
		this.temporaryTableName = temporaryTableName;
	}

	/**
	 * @return the temporaryTableName
	 */
	public String getTemporaryTableName() {
		return temporaryTableName;
	}

	/**
	 * @return the temporaryFileName
	 */
	public String getTemporaryFileName() {
		return temporaryFileName;
	}

	/**
	 * @param temporaryFileName
	 *           the temporaryFileName to set
	 */
	public void setTemporaryFileName(String temporaryFileName) {
		this.temporaryFileName = temporaryFileName;
	}

	public void setColumnNameList(List<String> columnNameList) {
		this.columnNameList = columnNameList;
	}

	public List<String> getColumnNameList() {
		return columnNameList;
	}
}
