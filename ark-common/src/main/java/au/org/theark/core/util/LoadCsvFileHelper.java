package au.org.theark.core.util;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.nio.charset.Charset;
import java.sql.Blob;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.wicket.markup.html.form.upload.FileUpload;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.CsvBlob;
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
	 * @param iCSVLoaderService The service required for Hibernate access
	 * @param databaseName The schema/database name to where the data resides
	 */
	public LoadCsvFileHelper(ICSVLoaderService iCSVLoaderService, String databaseName) {
		setTemporaryFileName(createTemporaryFileName());
		this.iCSVLoaderService = iCSVLoaderService;
		this.databaseName = databaseName;
	}
	
	/**
	 * Constructor. Takes in a fileUpload as a parameter and attempts to load data into the database
	 * 
	 * @param iCSVLoaderService 
	 * 			 The service required for Hibernate access
	 * @param databaseName
	 *           The schema/database name to where the data resides
	 * @param delimiterCharacter
	 *           The file field delimiter (comma, tab, pipe etc)
	 */
	public LoadCsvFileHelper(ICSVLoaderService iCSVLoaderService, String databaseName, char delimiterCharacter) {
		setTemporaryFileName(createTemporaryFileName());
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
		setTemporaryFileName(createTemporaryFileName());
		this.csvFile = csvFile;
		this.databaseName = databaseName;
		this.columnNameList = getColumnsFromHeader();
	}

	/**
	 * Convert the specified fileUpload to CSV format, and save as a CsvBlob
	 * @param fileUpload
	 * @param delimiterCharacter
	 */
	public void convertToCSVAndSave(FileUpload fileUpload, char delimiterCharacter) {
		String filename = fileUpload.getClientFileName();
		String fileFormat = filename.substring(filename.lastIndexOf('.') + 1).toUpperCase();
		InputStream inputStream = null;
		
		try {
			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS")) {
				inputStream = convertXlsInputStreamToCsv(fileUpload.getInputStream());
			}
			else{
				inputStream = fileUpload.getInputStream();
			}
			inputStream.reset();
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		
		this.columnNameList = getColumnsFromHeader(inputStream, delimiterCharacter);
		saveFileAsCsvBlob(inputStream, delimiterCharacter);
	}
	
	private void saveFileAsCsvBlob(InputStream inputStream, char delimiterCharacter) {
		Long id;
		try {
			CsvBlob csvBlob = new CsvBlob();
			inputStream.reset();
			Blob blob = Hibernate.createBlob(inputStream);
			csvBlob.setId(new Long(1));
			csvBlob.setCsvBlob(blob);
			id = iCSVLoaderService.createCsvBlob(csvBlob);
			iCSVLoaderService.writeBlobToTempFile("study", id, createTemporaryFileName(), delimiterCharacter);
			
			this.temporaryTableName = "tmp_table";
			
			iCSVLoaderService.createTemporaryTable(temporaryTableName, this.columnNameList);
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		catch (Exception e) {
			log.error(e.getMessage());
		}
	}
	
	/**
	 * Creates the full path name to the newly created temporary file on the database server
	 */
	private String createTemporaryFileName() {
		StringBuffer temporaryFileName = new StringBuffer();
		StringBuffer tempDir = new StringBuffer();
		tempDir.append(System.getProperty("java.io.tmpdir"));
		tempDir.append(File.separator);

		String fileName = UUID.randomUUID().toString();

		temporaryFileName.append(tempDir);
		temporaryFileName.append(fileName);
		temporaryFileName.append(".csv");
		return (temporaryFileName.toString());
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
			inputStream = convertWorkbookToCsv(w);
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

	/**
	 * Return the inputstream of the converted workbook as csv
	 * 
	 * @return inputstream of the converted workbook as csv
	 */
	public InputStream convertWorkbookToCsv(Workbook w) {
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try {
			OutputStreamWriter osw = new OutputStreamWriter(out);

			// Gets first sheet from workbook
			Sheet s = w.getSheet(0);

			Cell[] row = null;

			// Gets the cells from sheet
			for (int i = 0; i < s.getRows(); i++) {
				row = s.getRow(i);

				if (row.length > 0) {
					osw.write(row[0].getContents());
					for (int j = 1; j < row.length; j++) {
						osw.write(delimiterCharacter);
						osw.write(row[j].getContents());
					}
				}
				osw.write("\n");
			}

			osw.flush();
			osw.close();
		}
		catch (UnsupportedEncodingException e) {
			System.err.println(e.toString());
		}
		catch (IOException e) {
			System.err.println(e.toString());
		}
		catch (Exception e) {
			System.err.println(e.toString());
		}
		return new ByteArrayInputStream(out.toByteArray());
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
			log.error(e.getMessage());
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		return columnsFromHeader;
	}
	
	private List<String> getColumnsFromHeader(InputStream inputStream, char delimiterCharacter) {
		List<String> columnsFromHeader = new ArrayList<String>(0);
		try {
			Charset charset = Charset.forName("ISO-8859-1");
			inputStream.reset();
			csvReader = new CsvReader(inputStream, delimiterCharacter, charset);
			csvReader.readHeaders();
			String[] headerArray = csvReader.getHeaders();
			for (int i = 0; i < headerArray.length; i++) {
				columnsFromHeader.add(headerArray[i]);
			}
		}
		catch (FileNotFoundException e) {
			log.error(e.getMessage());
		}
		catch (IOException e) {
			log.error(e.getMessage());
		}
		catch (NullPointerException e) {
			log.error(e.getMessage());
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
