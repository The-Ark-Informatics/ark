package au.org.theark.phenotypic.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.Person;
import au.org.theark.phenotypic.exception.FileFormatException;
import au.org.theark.phenotypic.exception.PhenotypicSystemException;
import au.org.theark.phenotypic.model.dao.IPhenotypicDao;
import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;

import com.csvreader.CsvReader;

/**
 * PhenotypicImport provides support for importing phenotypic matrix-formatted files. It features state-machine behaviour to allow an external class
 * to deal with how to store the data pulled out of the files.
 * 
 * @author cellis
 */
@SuppressWarnings("unused")
public class PhenotypicImport
{
	private String				fieldName;
	private long				subjectCount;
	private long				fieldCount;
	private double				speed;
	private long				curPos;
	private long				srcLength				= -1;															// -1 means nothing being processed
	private StopWatch			timer						= null;
	private char				phenotypicDelimChr	= ',';															// default phenotypic file delimiter: COMMA
	private IPhenotypicDao	phenotypicDao			= null;
	private Person				person;
	private Collection		collection;
	private List<Field>		fieldList;
	private Long				studyId;
	static Logger				log						= LoggerFactory.getLogger(PhenotypicImport.class);
	java.util.Collection<String> validationMessages = null;

	/**
	 * PhenotypicImport constructor
	 * 
	 * @param phenotypicDao
	 *           data access object perform select/insert/updates to the database
	 */
	public PhenotypicImport(IPhenotypicDao phenotypicDao)
	{
		this.phenotypicDao = phenotypicDao;
		this.collection = new Collection();
		this.collection.setId(new Long(1));
		this.validationMessages = new ArrayList<String>();
	}

	/**
	 * PhenotypicImport constructor
	 * 
	 * @param phenotypicDao
	 *           data access object perform select/insert/updates to the database
	 * @param studyId
	 *           study identifier in context
	 * @param collection
	 *           phenotypic collection in context
	 */
	public PhenotypicImport(IPhenotypicDao phenotypicDao, Long studyId, Collection collection)
	{
		this.phenotypicDao = phenotypicDao;
		this.studyId = studyId;
		this.collection = collection;
		this.validationMessages = new ArrayList<String>();
	}
	
	/**
	 * Validates the phenotypic data file in the default "matrix" file format assumed: SUBJECTID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @throws IOException
	 *            input/output Exception
	 * @throws OutOfMemoryError
	 *            out of memory Exception
	 */
	public java.util.Collection<String> validateMatrixPhenoFile(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException
	{	
		if (phenotypicDao == null){
			throw new PhenotypicSystemException("Aborting: Must have a phenotypic dao object defined before calling.");
		}

		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		
		
		/* FieldData table requires:
		COLLECTION_ID
		PERSON_ID
		DATE_COLLECTED
		FIELD_ID
		USER_ID
		INSERT_TIME
		*/
		
		Date dateCollected = new Date();
		Field field = null;
		
		try {
			log.info("phenotypicImport.validateMatrixPhenoFile collection name: " + collection.getName());
		}
		catch (NullPointerException npe){
			log.error("Error with Collection...no object instatiated...");
			collection = phenotypicDao.getCollection(new Long(1));
		}
		
		try
		{
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, phenotypicDelimChr);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0)
			{
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTID	DATE_COLLECTED F1	F2	FN
			// 0 				1 					2 	3	N  
			csvReader.readHeaders();
			
			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.info("Header length: " + csvReader.getHeaders().toString().length());
			
			String[] fieldNameArray =  csvReader.getHeaders();
			
			// Field count = column count - 2 (SUBJECTID and DATE_COLLECTED)
			fieldCount = fieldNameArray.length - 2;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				
				/*if(fieldNameArray[0].equalsIgnoreCase(Constants.SUBJECT_IDENTIFIER)){
					log.info("VALIDATION: Subject Identifier: " + fieldNameArray[0]);
				}
				
				if(fieldNameArray[1].equalsIgnoreCase(Constants.DATE_COLLECTED)){
					log.info("VALIDATION: Date Collected: " + fieldNameArray[1]);
				}*/
						
				if (csvReader.getColumnCount() < 2)
				{
					// Non-compliant file
					StringBuffer stringBuffer = new StringBuffer();
					stringBuffer = stringBuffer.append("The specified file does not appear to conform to the expected phenotypic file format.\n");
					stringBuffer = stringBuffer.append("The default format is as follows:\n");
					stringBuffer = stringBuffer.append(Constants.SUBJECT_IDENTIFIER + "," + Constants.DATE_COLLECTED + ",FIELDNAME1,FIELDNAME2,FIELDNAME3,FIELDNAMEX\n");
					stringBuffer = stringBuffer.append("[subjectId],[dateCollected],[field1value],[field2value],[field3value],[fieldXvalue]\n");
					stringBuffer = stringBuffer.append("[..,],[...],[...],[...],[...],[...]\n");
					
					throw new FileFormatException(stringBuffer.toString());
				}
				else
				{
					// Loop through columns in current row in file, starting from the 2th position
					for (int i = 0; i < stringLineArray.length; i++)
					{
						// Field data actually the 2th colum onward
						if(i > 1){
							// Print out column details
							//log.info(fieldNameArray[i] + "\t" + stringLineArray[i]);
							
							try{
								/*log.info("Validating new field data for: SUBJECTID: " + stringLineArray[0] 
								                                  + "\tDATE_COLLECTED: " + stringLineArray[1] 
								                                  + "\tFIELD: " + fieldNameArray[i] 
								                                  + "\tVALUE: " + stringLineArray[i]);*/
								
								FieldData fieldData = new FieldData();
								
								fieldData.setCollection(this.collection);
								
								// First/0th column should be the personId
								fieldData.setPersonId(new Long(stringLineArray[0]));
								
								// Second/1th column should be date collected
								DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DATE_FORMAT);
								fieldData.setDateCollected(dateFormat.parse(stringLineArray[1]));
								
								// Set field
								field = new Field();
								field = phenotypicDao.getFieldByName(studyId, fieldNameArray[i]);
								fieldData.setField(field);
								
								// Other/ith columns should be the field data value
								fieldData.setValue(stringLineArray[i]);
								
								// Validate the field data
								PhenotypicValidator.validateFieldData(fieldData, validationMessages);
							}
							catch(org.hibernate.PropertyValueException pve){
								log.error("Error with DAO: " + pve.getMessage());
							}
							catch(org.apache.wicket.WicketRuntimeException wre){
								log.error("Error with Wicket: " + wre.getMessage());
							}
						}

						// Update progress
						curPos += stringLineArray[i].length() + 1; // update progress
						
						// Debug only - Show progress and speed
						log.info("progress: " + decimalFormat.format(getProgress()) + " % | speed: " + decimalFormat.format(getSpeed()) + " KB/sec");
					}
				}
				
				log.info("\n");
				subjectCount++;
			}
			
			if(validationMessages.size() > 0){
				log.info("Validation messages: " + validationMessages.size());
				for (Iterator<String> iterator = validationMessages.iterator(); iterator.hasNext();)
				{
					String errorMessage = iterator.next();
					log.info(errorMessage);
				}
			}
			else{
				log.info("Validation is ok");
			}
		}
		catch (IOException ioe)
		{
			log.error("processMatrixPhenoFile IOException stacktrace:", ioe);
			throw new PhenotypicSystemException("Unexpected I/O exception whilst reading the phenotypic data file");
		}
		catch (Exception ex)
		{
			log.error("processMatrixPhenoFile Exception stacktrace:", ex);
			throw new PhenotypicSystemException("Unexpected exception occurred when trying to process phenotypic data file");
		}
		finally
		{
			// Clean up the IO objects
			timer.stop();
			log.info("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			log.info("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
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
		log.info("Validated " + subjectCount * fieldCount + " rows of data");
		return validationMessages;
	}
	
	/**
	 * Imports the phenotypic data file to the database tables
	 * Assumes the file is in the default "matrix" file format:
	 * SUBJECTID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN...
	 * 1,01/01/1900,99.99,99.99,, ...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @throws IOException
	 *            input/output Exception
	 * @throws OutOfMemoryError
	 *            out of memory Exception
	 */
	public void importMatrixPhenoFile(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException
	{
		if (phenotypicDao == null)
		{
			throw new PhenotypicSystemException("Aborting: Must have a phenotypic storage object defined before calling.");
		}

		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");
		Date dateCollected = new Date();
		Field field = null;
		
		try {
			log.info("phenotypicImport.processMatrixPhenoFile collection name: " + collection.getName());
		}
		catch (NullPointerException npe){
			log.error("Error with Collection...no object instatiated...");
			collection = phenotypicDao.getCollection(new Long(1));
		}
		
		try
		{
			inputStreamReader = new InputStreamReader(fileInputStream);
			csvReader = new CsvReader(inputStreamReader, phenotypicDelimChr);
			String[] stringLineArray;

			srcLength = inLength;
			if (srcLength <= 0)
			{
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTID	DATE_COLLECTED F1	F2	FN
			// 0 				1 					2 	3	N  
			csvReader.readHeaders();
			
			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.info("Header length: " + csvReader.getHeaders().toString().length());
			
			String[] fieldNameArray =  csvReader.getHeaders();
			
			// Field count = column count - 2 (SUBJECTID and DATE_COLLECTED)
			fieldCount = fieldNameArray.length - 2;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				
				// Loop through columns in current row in file, starting from the 2th position
				for (int i = 0; i < stringLineArray.length; i++)
				{
					// Field data actually the 2th colum onward
					if(i > 1){
						// Print out column details
						log.info(fieldNameArray[i] + "\t" + stringLineArray[i]);
						
						try{
							log.info("Creating new field data for: " + Constants.SUBJECT_IDENTIFIER + ": " + stringLineArray[0] 
							                                  + "\t" + Constants.DATE_COLLECTED + ": " + stringLineArray[1] 
							                                  + "\tFIELD: " + fieldNameArray[i] 
							                                  + "\tVALUE: " + stringLineArray[i]);
							
							FieldData fieldData = new FieldData();
							
							fieldData.setCollection(this.collection);
							
							// First/0th column should be the personId
							fieldData.setPersonId(new Long(stringLineArray[0]));
							
							// Second/1th column should be date collected
							DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DATE_FORMAT);
							fieldData.setDateCollected(dateFormat.parse(stringLineArray[1]));
							
							// Set field
							field = new Field();
							field = phenotypicDao.getFieldByName(studyId, fieldNameArray[i]);
							fieldData.setField(field);
							
							// Other/ith columns should be the field data value
							fieldData.setValue(stringLineArray[i]);
							
							// Try to create the field data
							phenotypicDao.createFieldData(fieldData);
						}
						catch(org.hibernate.PropertyValueException pve){
							log.error("Error with DAO: " + pve.getMessage());
						}
						catch(org.apache.wicket.WicketRuntimeException wre){
							log.error("Error with Wicket: " + wre.getMessage());
						}
					}

					// Update progress
					curPos += stringLineArray[i].length() + 1; // update progress
					
					// Debug only - Show progress and speed
					log.info("progress: " + decimalFormat.format(getProgress()) + " % | speed: " + decimalFormat.format(getSpeed()) + " KB/sec");
				}
				
				log.info("\n");
				subjectCount++;
			}
		}
		catch (IOException ioe)
		{
			log.error("processMatrixPhenoFile IOException stacktrace:", ioe);
			throw new PhenotypicSystemException("Unexpected I/O exception whilst reading the phenotypic data file");
		}
		catch (Exception ex)
		{
			log.error("processMatrixPhenoFile Exception stacktrace:", ex);
			throw new PhenotypicSystemException("Unexpected exception occurred when trying to process phenotypic data file");
		}
		finally
		{
			// Clean up the IO objects
			timer.stop();
			log.info("Total elapsed time: " + timer.getTime() + " ms or " + decimalFormat.format(timer.getTime() / 1000.0) + " s");
			log.info("Total file size: " + srcLength + " B or " + decimalFormat.format(srcLength / 1024.0 / 1024.0) + " MB");
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
		log.info("Inserted " + subjectCount * fieldCount + " rows of data");
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
}
