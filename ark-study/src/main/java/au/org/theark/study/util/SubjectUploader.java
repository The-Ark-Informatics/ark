package au.org.theark.study.util;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Locale;

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.WorkbookSettings;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.time.StopWatch;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.ArkSubjectInsertException;
import au.org.theark.core.exception.ArkSystemException;
import au.org.theark.core.exception.ArkUniqueException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.exception.FileFormatException;
import au.org.theark.core.model.study.entity.GenderType;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.MaritalStatus;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.core.model.study.entity.PersonContactMethod;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.model.study.entity.SubjectStatus;
import au.org.theark.core.model.study.entity.TitleType;
import au.org.theark.core.model.study.entity.VitalStatus;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.vo.SubjectVO;
import au.org.theark.core.web.component.ArkErrorCell;
import au.org.theark.study.service.IStudyService;

import com.csvreader.CsvReader;

/**
 * SubjectUploader provides support for uploading subject matrix-formatted files. It features state-machine behaviour to allow an external class to
 * deal with how to store the data pulled out of the files.
 * 
 * @author cellis
 */
public class SubjectUploader
{
	private long						subjectCount;
	private long						insertCount;
	private long						updateCount;
	private long						fieldCount;
	private long						curPos;
	private long						srcLength					= -1;																	// -1 means nothing being processed 
	private StopWatch					timer							= null;
	private char						delimiterCharacter		= Constants.DEFAULT_DELIMITER_CHARACTER;						// default delimiter: COMMA
	private String						fileFormat					= Constants.DEFAULT_FILE_FORMAT;						// default file fomat: CSV
	private Study						study;
	static Logger						log							= LoggerFactory.getLogger(SubjectUploader.class);
	java.util.Collection<String>	fileValidationMessages	= null;
	java.util.Collection<String>	dataValidationMessages	= null;
	private IArkCommonService		iArkCommonService			= null;
	private IStudyService			studyService				= null;
	private StringBuffer				uploadReport				= null;
	private SimpleDateFormat		simpleDateFormat			= new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
	private int row													= 1;
	private HashSet<Integer>		updateRows 					= new HashSet<Integer>();
	private HashSet<Integer> 		errorCols					= new HashSet<Integer>();
	private HashSet<Integer> 		errorRows					= new HashSet<Integer>();
	private HashSet<ArkErrorCell>	errorCells					= new HashSet<ArkErrorCell>();

	/**
	 * SubjectUploader constructor
	 * 
	 * @param study
	 *           study identifier in context
	 * @param iArkCommonService
	 *           common ARK service
	 * @param studyService
	 *           study service to perform select/insert/updates to the database
	 */
	public SubjectUploader(Study study, IArkCommonService iArkCommonService, IStudyService studyService)
	{
		this.study = study;
		this.fileValidationMessages = new ArrayList<String>();
		this.dataValidationMessages = new ArrayList<String>();
		this.iArkCommonService = iArkCommonService;
		this.studyService = studyService;
		simpleDateFormat.setLenient(false);
	}

	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of a file
	 * @throws FileFormatException
	 *            file format Exception
	 * @throws ArkBaseException
	 *            general ARK Exception
	 * @return a collection of validation messages
	 */
	public java.util.Collection<String> validateSubjectMatrixFileFormat(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkBaseException
	{
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		try
		{
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0)
			{
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTUID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			String[] fieldNameArray = csvReader.getHeaders();

			// Field count = column count - 1 (SUBJECTUID column not counted)
			fieldCount = fieldNameArray.length - 1;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();

				if (csvReader.getColumnCount() < 2 || fieldCount < 1 || !fieldNameArray[0].equalsIgnoreCase(Constants.SUBJECTUID))
				{
					// Invalid file format
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer.append("The specified file does not appear to conform to the expected file format.\n");
					stringBuffer.append("The specified fileformat was: " + fileFormat +".\n");
					stringBuffer.append("The specified delimiter type was: " + delimiterCharacter +".\n");
					stringBuffer.append(".\n");
					stringBuffer.append("The default format should be as follows:\n");
					stringBuffer.append(Constants.SUBJECTUID + delimiterCharacter + "FIELDNAME1"+ delimiterCharacter + "FIELDNAME2"+ delimiterCharacter + "FIELDNAME3"+ delimiterCharacter + "FIELDNAMEX\n");
					stringBuffer.append("[subjectUid]" + delimiterCharacter + "[field1value]" + delimiterCharacter + "[field2value]" + delimiterCharacter + "[field3value]" + delimiterCharacter + "[fieldXvalue]\n");
					stringBuffer.append("[...]" + delimiterCharacter + "[...]" + delimiterCharacter + "[...]" + delimiterCharacter + "[...]" + delimiterCharacter + "[...]" + delimiterCharacter + "[...]\n");
					stringBuffer.append("\nNOTE: Enclosing quotes are optional");

					fileValidationMessages.add(stringBuffer.toString());
					break;
				}
				else
				{
					// Check each line has same number of columns as header
					if (stringLineArray.length < fieldNameArray.length)
					{
						fileValidationMessages.add("Error at row " + row + ", the row has missing cells, or missing the required number of delimiters.");
					}
				}

				row++;
				subjectCount++;
			}

			if (fileValidationMessages.size() > 0)
			{
				for (Iterator<String> iterator = fileValidationMessages.iterator(); iterator.hasNext();)
				{
					String errorMessage = iterator.next();
					log.debug(errorMessage);
				}
			}
			else
			{
				log.debug("Validation is ok");
			}
		}
		catch (IOException ioe)
		{
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex)
		{
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file");
		}
		finally
		{
			// Clean up the IO objects
			timer.stop();
			// fileValidationMessages.add("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			// fileValidationMessages.add("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");

			if (timer != null)
				timer = null;
			if (csvReader != null)
			{
				try
				{
					csvReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null)
			{
				try
				{
					inputStreamReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
			// Restore the state of variables
			srcLength = -1;
		}

		return fileValidationMessages;
	}

	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of a file
	 * @throws FileFormatException
	 *            file format Exception
	 * @throws ArkBaseException
	 *            general ARK Exception
	 * @return a collection of validation messages
	 */
	public java.util.Collection<String> validateMatrixSubjectFileData(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkSystemException
	{
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		curPos = 0;
		row = 1;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		try
		{
			if(fileFormat.equalsIgnoreCase("XLS"))
			{
				try
				{
					fileInputStream = convertXlsToCsv(Workbook.getWorkbook(fileInputStream));
					delimiterCharacter = ',';
				}
				catch (BiffException e)
				{
					log.error("BiffException: " + e);
				}
				catch (IOException e)
				{
					log.error("IOException: " + e);
				}
			}
			
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0)
			{
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 1th column to Nth column)
			// SUBJECTUID F1 F2 FN
			// 0 1 2 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();

			String[] fieldNameArray = csvReader.getHeaders();

			// Field count = column count - 1 (SUBJECTUID column not counted)
			fieldCount = fieldNameArray.length - 1;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();

				// First/0th column should be the SubjectUID
				String subjectUID = stringLineArray[0];

				// If no SubjectUID found, caught by exception catch
				try
				{
					LinkSubjectStudy linksubjectStudy = (iArkCommonService.getSubjectByUID(subjectUID));
					linksubjectStudy.setStudy(study);
					updateRows.add(row);
					
				}
				catch (EntityNotFoundException enf)
				{
					// Subject not found, thus a new subject to be inserted
				}

				int col = 0;
				String dateStr = new String();

				if (csvReader.getIndex("DATE_OF_BIRTH") > 0 || csvReader.getIndex("DOB") > 0)
				{

					if (csvReader.getIndex("DATE_OF_BIRTH") > 0)
					{
						col = csvReader.getIndex("DATE_OF_BIRTH");
					}
					else
					{
						col = csvReader.getIndex("DOB");
					}

					try
					{
						dateStr = stringLineArray[col];
						if (dateStr != null && dateStr.length() > 0)
							simpleDateFormat.parse(dateStr);
					}
					catch (ParseException pex)
					{
						dataValidationMessages.add("Row: " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + stringLineArray[col] + " is not in the valid date format of: " + Constants.DD_MM_YYYY.toLowerCase());
						errorCols.add(col);
						errorRows.add(row);
						errorCells.add(new ArkErrorCell(col, row));
					}
				}

				if (csvReader.getIndex("DATE_OF_DEATH") > 0 || csvReader.getIndex("DODEATH") > 0)
				{

					if (csvReader.getIndex("DATE_OF_DEATH") > 0)
					{
						col = csvReader.getIndex("DATE_OF_DEATH");
					}
					else
					{
						col = csvReader.getIndex("DODEATH");
					}
					try
					{
						dateStr = stringLineArray[col];
						if (dateStr != null && dateStr.length() > 0)
							simpleDateFormat.parse(dateStr);
					}
					catch (ParseException pex)
					{
						dataValidationMessages.add("Row: " + row + ": Subject UID: " + subjectUID + " " + fieldNameArray[col] + ": " + stringLineArray[col] + " is not in the valid date format of: " + Constants.DD_MM_YYYY.toLowerCase());
						errorCols.add(col);
						errorRows.add(row);
						errorCells.add(new ArkErrorCell(col, row));
					}
				}

				log.debug("\n");
				subjectCount++;
				row++;
			}

			if (dataValidationMessages.size() > 0)
			{
				log.debug("Validation messages: " + dataValidationMessages.size());
				for (Iterator<String> iterator = dataValidationMessages.iterator(); iterator.hasNext();)
				{
					String errorMessage = iterator.next();
					log.debug(errorMessage);
				}
			}
			else
			{
				log.debug("Validation is ok");
			}
		}
		catch (IOException ioe)
		{
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex)
		{
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file");
		}
		finally
		{
			// Clean up the IO objects
			timer.stop();
			log.debug("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			log.debug("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
			if (timer != null)
				timer = null;
			if (csvReader != null)
			{
				try
				{
					csvReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null)
			{
				try
				{
					inputStreamReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
			// Restore the state of variables
			srcLength = -1;
		}
		log.debug("Validated " + subjectCount + " subjects");

		return dataValidationMessages;
	}

	/**
	 * Imports the subject data file to the database tables Assumes the file is in the default "matrix" file format: SUBJECTUID,FIELD1,FIELD2,FIELDN...
	 * 1,01/01/1900,99.99,99.99,, ...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of a file
	 * @throws FileFormatException
	 *            file format Exception
	 * @throws ArkBaseException
	 *            general ARK Exception
	 */
	public void uploadMatrixSubjectFile(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkSystemException
	{
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		try
		{
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0)
			{
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTUID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			String[] fieldNameArray = csvReader.getHeaders();

			// Field count = column count - 1 (SUBJECTUID column not counted)
			fieldCount = fieldNameArray.length - 1;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				String subjectUID = stringLineArray[0];

				// Loop through columns in current row in file, starting from the 2nd (i=1) position
				for (int i = 1; i < stringLineArray.length; i++)
				{
					// Print out column details
					log.debug(fieldNameArray[i] + "\t" + stringLineArray[i]);

					log.debug("Creating new field data for: " + Constants.SUBJECTUID + ": " + subjectUID + "\t" + Constants.DATE_COLLECTED + ": " + stringLineArray[1] + "\tFIELD: " + fieldNameArray[i]
							+ "\tVALUE: " + stringLineArray[i]);

					// Update progress
					curPos += stringLineArray[i].length() + 1; // update progress

					// Debug only - Show progress and speed
					log.debug("progress: " + decimalFormat.format(getProgress()) + " % | speed: " + decimalFormat.format(getSpeed()) + " KB/sec");
				}

				log.debug("\n");
				subjectCount++;
			}
		}
		catch (IOException ioe)
		{
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex)
		{
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file");
		}
		finally
		{
			// Clean up the IO objects
			timer.stop();
			log.debug("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			log.debug("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
			if (timer != null)
				timer = null;
			if (csvReader != null)
			{
				try
				{
					csvReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null)
			{
				try
				{
					inputStreamReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
			// Restore the state of variables
			srcLength = -1;
		}
		log.debug("Inserted " + subjectCount + " subjects");
	}

	/**
	 * Imports the subject data file to the database tables, and creates report on the process Assumes the file is in the default "matrix" file format:
	 * SUBJECTUID,FIELD1,FIELD2,FIELDN... 1,01/01/1900,99.99,99.99,, ...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @param inLength
	 *           is the length of a file
	 * @throws FileFormatException
	 *            file format Exception
	 * @throws ArkBaseException
	 *            general ARK Exception
	 * @return the upload report detailing the upload process
	 */
	public StringBuffer uploadAndReportMatrixSubjectFile(InputStream fileInputStream, long inLength, String inFileFormat, char inDelimChr) throws FileFormatException, ArkSystemException
	{
		delimiterCharacter = inDelimChr;
		fileFormat = inFileFormat;
		uploadReport = new StringBuffer();
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		try
		{
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, delimiterCharacter);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0)
			{
				uploadReport.append("The input size was not greater than 0. Actual length reported: ");
				uploadReport.append(srcLength);
				uploadReport.append("\n");
				throw new FileFormatException("The input size was not greater than 0. Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTUID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			String[] fieldNameArray = csvReader.getHeaders();

			// Field count = column count - 1 (SUBJECTUID column not counted)
			fieldCount = fieldNameArray.length - 1;
			int index = 0;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				String subjectUID = stringLineArray[0];

				SubjectVO subjectVo = new SubjectVO();
				LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
				linkSubjectStudy.setStudy(study);

				try
				{
					linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUID);
				}
				catch (EntityNotFoundException enf)
				{
					// New subject
					linkSubjectStudy.setSubjectUID(subjectUID);
					linkSubjectStudy.setStudy(study);
				}
				Person person = linkSubjectStudy.getPerson();
				subjectVo.setSubjectStudy(linkSubjectStudy);

				if (linkSubjectStudy.getId() == null && linkSubjectStudy.getPerson().getId() == null)
				{
					person = new Person();
				}
				else
				{
					person = linkSubjectStudy.getPerson();
				}

				if (csvReader.getIndex("FIRST_NAME") > 0)
					person.setFirstName(stringLineArray[csvReader.getIndex("FIRST_NAME")]);

				if (csvReader.getIndex("MIDDLE_NAME") > 0)
					person.setMiddleName(stringLineArray[csvReader.getIndex("MIDDLE_NAME")]);

				if (csvReader.getIndex("LAST_NAME") > 0)
					person.setLastName(stringLineArray[csvReader.getIndex("LAST_NAME")]);

				if (csvReader.getIndex("PREFERRED_NAME") > 0)
					person.setPreferredName(stringLineArray[csvReader.getIndex("PREFERRED_NAME")]);

				if (csvReader.getIndex("GENDER_TYPE") > 0 || csvReader.getIndex("GENDER") > 0 || csvReader.getIndex("SEX") > 0)
				{
					GenderType genderType;
					if (csvReader.getIndex("GENDER_TYPE") > 0)
					{
						index = csvReader.getIndex("GENDER_TYPE");
					}
					else if (csvReader.getIndex("GENDER") > 0)
					{
						index = csvReader.getIndex("GENDER");
					}
					else
					{
						index = csvReader.getIndex("SEX");
					}

					if (stringLineArray[index] != null && stringLineArray[index].length() > 0)
					{
						genderType = iArkCommonService.getGenderType(stringLineArray[index]);
						person.setGenderType(genderType);
					}
				}

				if (csvReader.getIndex("DATE_OF_BIRTH") > 0 || csvReader.getIndex("DOB") > 0)
				{
					Date dateOfBirth = new Date();

					if (csvReader.getIndex("DATE_OF_BIRTH") > 0)
					{
						index = csvReader.getIndex("DATE_OF_BIRTH");
					}
					else
					{
						index = csvReader.getIndex("DOB");
					}

					if (stringLineArray[index] != null && stringLineArray[index].length() > 0)
					{
						dateOfBirth = simpleDateFormat.parse(stringLineArray[index]);
						person.setDateOfBirth(dateOfBirth);
					}
				}

				if (csvReader.getIndex("DATE_OF_DEATH") > 0 || csvReader.getIndex("DODEATH") > 0)
				{
					Date dateOfDeath = new Date();
					if (csvReader.getIndex("DATE_OF_DEATH") > 0)
					{
						index = csvReader.getIndex("DATE_OF_DEATH");
					}
					else
					{
						index = csvReader.getIndex("DODEATH");
					}

					if (stringLineArray[index] != null && stringLineArray[index].length() > 0)
					{
						dateOfDeath = simpleDateFormat.parse(stringLineArray[index]);
						person.setDateOfDeath(dateOfDeath);
					}
				}

				if (csvReader.getIndex("CAUSE_OF_DEATH") > 0 || csvReader.getIndex("CODEATH") > 0)
				{
					if (csvReader.getIndex("CAUSE_OF_DEATH") > 0)
					{
						index = csvReader.getIndex("CAUSE_OF_DEATH");
					}
					else
					{
						index = csvReader.getIndex("CODEATH");
					}

					if (stringLineArray[index] != null && stringLineArray[index].length() > 0)
					{
						person.setCauseOfDeath(stringLineArray[index]);
					}
				}

				if (csvReader.getIndex("VITAL_STATUS") > 0)
				{
					String vitalStatusStr = (stringLineArray[csvReader.getIndex("VITAL_STATUS")]);
					VitalStatus vitalStatus = iArkCommonService.getVitalStatus(vitalStatusStr);
					person.setVitalStatus(vitalStatus);
				}

				if (csvReader.getIndex("PREFERRED_EMAIL") > 0)
				{
					person.setPreferredEmail(stringLineArray[csvReader.getIndex("PREFERRED_EMAIL")]);
				}

				if (csvReader.getIndex("OTHER_EMAIL") > 0)
				{
					person.setPreferredEmail(stringLineArray[csvReader.getIndex("OTHER_EMAIL")]);
				}

				if (csvReader.getIndex("TITLE") > 0)
				{
					String titleStr = (stringLineArray[csvReader.getIndex("TITLE")]);
					TitleType titleType = iArkCommonService.getTitleType(titleStr);
					person.setTitleType(titleType);
				}

				if (csvReader.getIndex("MARITAL_STATUS") > 0)
				{
					String maritalStatusStr = (stringLineArray[csvReader.getIndex("MARITAL_STATUS")]);
					MaritalStatus maritalStatus = iArkCommonService.getMaritalStatus(maritalStatusStr);
					person.setMaritalStatus(maritalStatus);
				}

				if (csvReader.getIndex("PERSON_CONTACT_METHOD") > 0 || csvReader.getIndex("CONTACT_METHOD") > 0)
				{
					String personContactMethodStr = null;
					if (csvReader.getIndex("PERSON_CONTACT_METHOD") > 0)
					{
						personContactMethodStr = (stringLineArray[csvReader.getIndex("PERSON_CONTACT_METHOD")]);
					}
					else
					{
						personContactMethodStr = (stringLineArray[csvReader.getIndex("CONTACT_METHOD")]);
					}
					PersonContactMethod personContactMethod = iArkCommonService.getPersonContactMethod(personContactMethodStr);
					person.setPersonContactMethod(personContactMethod);
				}

				if (csvReader.getIndex("STATUS") > 0)
				{
					String statusStr = (stringLineArray[csvReader.getIndex("STATUS")]);
					SubjectStatus subjectStatus = iArkCommonService.getSubjectStatus(statusStr);
					linkSubjectStudy.setSubjectStatus(subjectStatus);
				}

				linkSubjectStudy.setPerson(person);
				subjectVo.setSubjectStudy(linkSubjectStudy);

				if (subjectVo.getSubjectStudy().getId() == null || subjectVo.getSubjectStudy().getPerson().getId() == 0)
				{
					// Save new Subject
					try
					{
						studyService.createSubject(subjectVo);
						StringBuffer sb = new StringBuffer();
						sb.append("Subject UID: ");
						sb.append(subjectVo.getSubjectStudy().getSubjectUID());
						sb.append(" has been created successfully and linked to the study: ");
						sb.append(study.getName());
						sb.append("\n");
						uploadReport.append(sb);
						insertCount++;
					}
					catch (ArkUniqueException ex)
					{
						uploadReport.append("Subject UID must be unique:" + subjectUID);
					}
					catch (ArkSubjectInsertException ex)
					{
						log.error(ex.getMessage());
					}
				}
				else
				{
					// Update existing Subject
					try
					{
						studyService.updateSubject(subjectVo);
						StringBuffer sb = new StringBuffer();
						sb.append("Subject UID: ");
						sb.append(subjectVo.getSubjectStudy().getSubjectUID());
						sb.append(" has been updated successfully and linked to the study: ");
						sb.append(study.getName());
						sb.append("\n");
						uploadReport.append(sb);
						updateCount++;
					}
					catch (ArkUniqueException e)
					{
						uploadReport.append("Subject UID must be unique:" + subjectUID);
					}
				}

				log.debug("\n");
				subjectCount++;
			}
		}
		catch (IOException ioe)
		{
			uploadReport.append("Unexpected I/O exception whilst reading the subject data file\n");
			log.error("processMatrixSubjectFile IOException stacktrace:", ioe);
			throw new ArkSystemException("Unexpected I/O exception whilst reading the subject data file");
		}
		catch (Exception ex)
		{
			uploadReport.append("Unexpected exception whilst reading the subject data file\n");
			log.error("processMatrixSubjectFile Exception stacktrace:", ex);
			throw new ArkSystemException("Unexpected exception occurred when trying to process subject data file");
		}
		finally
		{
			// Clean up the IO objects
			timer.stop();
			uploadReport.append("\n");
			uploadReport.append("Total elapsed time: ");
			uploadReport.append(timer.getTime());
			uploadReport.append(" ms or ");
			uploadReport.append(decimalFormat.format(timer.getTime() / 1000.0));
			uploadReport.append(" s");
			uploadReport.append("\n");
			uploadReport.append("Total file size: ");
			uploadReport.append(srcLength);
			uploadReport.append(" B or ");
			uploadReport.append(decimalFormat.format(srcLength / 1024.0 / 1024.0));
			uploadReport.append(" MB");
			uploadReport.append("\n");

			if (timer != null)
				timer = null;

			if (csvReader != null)
			{
				try
				{
					csvReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (inputStreamReader != null)
			{
				try
				{
					inputStreamReader.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
			// Restore the state of variables
			srcLength = -1;
		}
		uploadReport.append("Processed ");
		uploadReport.append(subjectCount);
		uploadReport.append(" subjects.");
		uploadReport.append("\n");
		uploadReport.append("Inserted ");
		uploadReport.append(insertCount);
		uploadReport.append(" subjects.");
		uploadReport.append("\n");
		uploadReport.append("Updated ");
		uploadReport.append(updateCount);
		uploadReport.append(" subjects.");
		uploadReport.append("\n");

		return uploadReport;
	}

	public org.apache.wicket.util.file.File convertXlsToCsv(File file, char delimChr)
	{
		delimiterCharacter = delimChr;
		
		// File to store data in form of CSV
		org.apache.wicket.util.file.File f = new org.apache.wicket.util.file.File(file.getAbsoluteFile()+".csv");
		try
		{
			OutputStream os = (OutputStream) new FileOutputStream(f);
			String encoding = "UTF8";
			OutputStreamWriter osw = new OutputStreamWriter(os, encoding);
			BufferedWriter bw = new BufferedWriter(osw);

			// Excel document to be imported
			String filename = file.getAbsolutePath();
			WorkbookSettings ws = new WorkbookSettings();
			ws.setLocale(new Locale("en", "EN"));
			Workbook w = Workbook.getWorkbook(new File(filename), ws);

			// Gets first sheet from workbook
			Sheet s = w.getSheet(0);

			Cell[] row = null;

			// Gets the cells from sheet
			for (int i = 0; i < s.getRows(); i++)
			{
				row = s.getRow(i);

				if (row.length > 0)
				{
					bw.write(row[0].getContents());
					for (int j = 1; j < row.length; j++)
					{
						bw.write(delimiterCharacter);
						bw.write(row[j].getContents());
					}
				}
				bw.newLine();
			}
			
			bw.flush();
			bw.close();
		}
		catch (UnsupportedEncodingException e)
		{
			System.err.println(e.toString());
		}
		catch (IOException e)
		{
			System.err.println(e.toString());
		}
		catch (Exception e)
		{
			System.err.println(e.toString());
		}
		return f;
	}
	
	public InputStream convertXlsToCsv(Workbook w)
	{
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		try
		{
			OutputStreamWriter osw = new OutputStreamWriter(out);

			// Gets first sheet from workbook
			Sheet s = w.getSheet(0);

			Cell[] row = null;

			// Gets the cells from sheet
			for (int i = 0; i < s.getRows(); i++)
			{
				row = s.getRow(i);

				if (row.length > 0)
				{
					osw.write(row[0].getContents());
					for (int j = 1; j < row.length; j++)
					{
						osw.write(delimiterCharacter);
						osw.write(row[j].getContents());
					}
				}
				osw.write("\n");
			}
			
			osw.flush();
			osw.close();
		}
		catch (UnsupportedEncodingException e)
		{
			System.err.println(e.toString());
		}
		catch (IOException e)
		{
			System.err.println(e.toString());
		}
		catch (Exception e)
		{
			System.err.println(e.toString());
		}
		return new ByteArrayInputStream(out.toByteArray());
	}
	
	/**
	 * Return the progress of the current process in %
	 * 
	 * @return if a process is actively running, then progress in %; or if no process running, then returns -1
	 */
	public double getProgress()
	{
		double progress = -1;

		if (srcLength > 0)
			progress = curPos * 100.0 / srcLength; // %

		return progress;
	}

	/**
	 * Return the speed of the current process in KB/s
	 * 
	 * @return if a process is actively running, then speed in KB/s; or if no process running, then returns -1
	 */
	public double getSpeed()
	{
		double speed = -1;

		if (srcLength > 0)
			speed = curPos / 1024 / (timer.getTime() / 1000.0); // KB/s

		return speed;
	}

	public HashSet<Integer> getUpdateRows()
	{
		return updateRows;
	}

	public void setUpdateRows(HashSet<Integer> updateRows)
	{
		this.updateRows = updateRows;
	}

	/**
	 * @param errorCols the errorCols to set
	 */
	public void setErrorCols(HashSet<Integer> errorCols)
	{
		this.errorCols = errorCols;
	}

	/**
	 * @return the errorCols
	 */
	public HashSet<Integer> getErrorCols()
	{
		return errorCols;
	}

	/**
	 * @return the errorRows
	 */
	public HashSet<Integer> getErrorRows()
	{
		return errorRows;
	}

	/**
	 * @param errorRows the errorRows to set
	 */
	public void setErrorRows(HashSet<Integer> errorRows)
	{
		this.errorRows = errorRows;
	}

	public HashSet<ArkErrorCell> getErrorCells()
	{
		return errorCells;
	}

	public void setErrorCells(HashSet<ArkErrorCell> errorCells)
	{
		this.errorCells = errorCells;
	}
}