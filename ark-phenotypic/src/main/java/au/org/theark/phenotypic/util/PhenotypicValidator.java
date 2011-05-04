package au.org.theark.phenotypic.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashSet;
import java.util.Iterator;
import java.util.StringTokenizer;

import org.apache.commons.lang.time.StopWatch;
import org.apache.shiro.SecurityUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkGridCell;
import au.org.theark.phenotypic.exception.FileFormatException;
import au.org.theark.phenotypic.exception.PhenotypicSystemException;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.phenotypic.model.entity.FieldType;
import au.org.theark.phenotypic.model.entity.PhenoCollection;
import au.org.theark.phenotypic.model.vo.UploadVO;
import au.org.theark.phenotypic.service.IPhenotypicService;

import com.csvreader.CsvReader;

/**
 * PhenotypicValidator provides support for validating phenotypic data with the defined data dictionary
 * 
 * @author cellis
 */
@SuppressWarnings("unused")
public class PhenotypicValidator
{
	static Logger	log	= LoggerFactory.getLogger(PhenotypicValidator.class);
	boolean qualityControl = false;
	private Study						study;
	private PhenoCollection			phenoCollection;
	private String						fieldName;
	private long						subjectCount;
	private long						fieldCount;
	private long						insertCount;
	private long						updateCount;
	private double						speed;
	private long						curPos;
	private long						srcLength					= -1;															// -1 means nothing being processed
	private StopWatch					timer							= null;
	private char						phenotypicDelimChr		= Constants.IMPORT_DELIM_CHAR_COMMA;					// default phenotypic file delimiter: COMMA
	private String						fileFormat;
	java.util.Collection<String>	fileValidationMessages	= new ArrayList<String>();
	java.util.Collection<String>	dataValidationMessages	= new ArrayList<String>();
	private IPhenotypicService		iPhenoService			= null;
	private IArkCommonService		iArkCommonService			= null;
	private StringBuffer				uploadReport				= null;
	private HashSet<Integer>		insertRows 					= new HashSet<Integer>();
	private HashSet<Integer>		updateRows 					= new HashSet<Integer>();
	private HashSet<ArkGridCell> insertCells 				= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell> updateCells 				= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell> warningCells 				= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell> errorCells					= new HashSet<ArkGridCell>();
	private String uploadType = new String("");

	/**
	 * PhenotypicValidator constructor
	 * 
	 */
	public PhenotypicValidator()
	{
	}
	
	public PhenotypicValidator(IArkCommonService iArkCommonService, IPhenotypicService iPhenoService, UploadVO uploadVo)
	{
		this.iArkCommonService = iArkCommonService;
		this.iPhenoService = iPhenoService;
		Long sessionCollectionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		if(sessionCollectionId != null)
			this.phenoCollection = iPhenoService.getPhenoCollection(sessionCollectionId);
		
		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(studyId != null)
		{
			Study study = iArkCommonService.getStudy(studyId);
			this.study = study;
		}
		this.fileValidationMessages = new ArrayList<String>();
		this.dataValidationMessages = new ArrayList<String>();
		this.fileFormat = uploadVo.getUpload().getFileFormat().getName();
		this.phenotypicDelimChr = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter().charAt(0);
		if(uploadVo.getUpload().getUploadType() != null)
			this.uploadType = uploadVo.getUpload().getUploadType();
	}

	public boolean isQualityControl() {
		return qualityControl;
	}

	public void setQualityControl(boolean qualityControl) {
		this.qualityControl = qualityControl;
	}

	/**
	 * Returns true of the field data value is a valid format, either NUMBER, CHARACTER or DATE as specified in the data dictionary
	 * 
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isValidFieldData(FieldData fieldData, java.util.Collection<String> errorMessages)
	{
		Field field = (Field) fieldData.getField();
		// errorMessages.add("Validating field data: " + field.getName().toString() + "\t" + fieldData.getValue().toString());

		// Number field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER))
		{
			try
			{
				Float floatFieldValue = Float.parseFloat(fieldData.getValue());
				return true;
			}
			catch (NumberFormatException nfe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDataNotDefinedType(field, fieldData));
				log.error("Field data number format exception " + nfe.getMessage());
				return false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				return false;
			}
		}

		// Character field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER))
		{
			try
			{
				String stringFieldValue = fieldData.getValue();
				return true;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				return false;
			}
		}

		// Date field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))
		{
			try
			{
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(fieldData.getValue());
				return true;
			}
			catch (ParseException pe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDataNotValidDate(field, fieldData));
				log.error("Field data date parse exception " + pe.getMessage());
				return false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				return false;
			}

		}

		return false;
	}

	/**
	 * Returns true if field data value is within the defined range as specified in the data dictionary
	 * 
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isInValidRange(FieldData fieldData, java.util.Collection<String> errorMessages)
	{
		Field field = (Field) fieldData.getField();

		if(errorMessages != null)
		{	
			if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER))
			{
				try
				{
					Float floatMinValue = Float.parseFloat(field.getMinValue());
					Float floatMaxValue = Float.parseFloat(field.getMaxValue());
					Float floatFieldValue = Float.parseFloat(fieldData.getValue());
	
					if ((floatFieldValue > floatMaxValue) || (floatFieldValue < floatMinValue))
					{
						if ((floatFieldValue > floatMaxValue))
						{
							errorMessages.add(PhenotypicValidationMessage.fieldDataGreaterThanMaxValue(field, fieldData));
						}
						if ((floatFieldValue < floatMinValue))
						{
							errorMessages.add(PhenotypicValidationMessage.fieldDataLessThanMinValue(field, fieldData));
						}
						return false;
					}
					return true;
				}
				catch (NumberFormatException nfe)
				{
					log.error("Field data number format exception " + nfe.getMessage());
					return false;
				}
				catch (NullPointerException npe)
				{
					log.error("Field data null pointer exception " + npe.getMessage());
					return false;
				}
			}
			else if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))
			{
				if(field.getMinValue() != null && field.getMaxValue() != null)
				{
					try
					{
						DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
						dateFormat.setLenient(false);
						
						Date dateMinValue = dateFormat.parse(field.getMinValue());
						Date dateMaxValue = dateFormat.parse(field.getMaxValue());
						Date dateFieldValue = dateFormat.parse(fieldData.getValue());
						
						if (dateFieldValue.after(dateMaxValue) || dateFieldValue.before(dateMinValue))
						{
							if (dateFieldValue.after(dateMaxValue))
							{
								errorMessages.add(PhenotypicValidationMessage.fieldDataGreaterThanMaxValue(field, fieldData));
							}
							if (dateFieldValue.before(dateMinValue))
							{
								errorMessages.add(PhenotypicValidationMessage.fieldDataLessThanMinValue(field, fieldData));
							}
							return false;
						}
						return true;
					}
					catch (ParseException pe)
					{
						return false;
					}
				}
				else
					return true;
			}
		}
		return false;
	}

	/**
	 * Returns true if the field data value is within the discrete range as defined in the data dictionary
	 * 
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isInEncodedValues(FieldData fieldData, java.util.Collection<String> errorMessages)
	{
		boolean inEncodedValues = true;

		Field field = (Field) fieldData.getField();

		// Validate if encoded values is defined
		if (field.getEncodedValues() != null)
		{
			try
			{
				StringTokenizer stringTokenizer = new StringTokenizer(field.getEncodedValues(), Constants.ENCODED_VALUES_TOKEN);

				// Iterate through all discrete defined values and compare to field data value
				while (stringTokenizer.hasMoreTokens())
				{
					String encodedValueToken = stringTokenizer.nextToken();
					StringTokenizer encodedValueSeparator = new StringTokenizer(encodedValueToken, Constants.ENCODED_VALUES_SEPARATOR);
					String encodedValue = encodedValueSeparator.nextToken().trim();
					
					if (encodedValue.equalsIgnoreCase(fieldData.getValue().trim()))
					{
						inEncodedValues = true;
						break;
					}
					else
					{
						inEncodedValues = false;
					}
				}

				if (!inEncodedValues)
				{
					errorMessages.add(PhenotypicValidationMessage.fieldDataNotInEncodedValues(field, fieldData));
				}

			}
			catch (NullPointerException npe)
			{
				log.error("Field data null format exception " + npe.getMessage());
				inEncodedValues = false;
			}

		}
		return inEncodedValues;
	}

	/**
	 * Returns true of the field data value is a valididated
	 * 
	 * @param fieldData
	 * @param errorMessages
	 * @return boolean
	 */
	public static boolean validateFieldData(FieldData fieldData, java.util.Collection<String> errorMessages)
	 {
		return(isValidFieldData(fieldData, errorMessages) && isInEncodedValues(fieldData, errorMessages) && isInValidRange(fieldData, errorMessages));
	}
	
	public static boolean fieldDataPassesQualityControl(FieldData fieldData, java.util.Collection<String> errorMessages)
	{
		boolean passesQualityControl = true;
		// Validate the field data
		if (isValidFieldData(fieldData, errorMessages) && isInEncodedValues(fieldData, errorMessages) && isInValidRange(fieldData, errorMessages))
		{
			passesQualityControl = true;
		}
		else
		{
			passesQualityControl = false;
		}
		return passesQualityControl;
	}
	
	/**
	 * Validates the phenotypic file in the default "matrix" file format assumed: SUBJECTID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN...
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
	public java.util.Collection<String> validateMatrixPhenoFileFormat(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException
	{
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		/*
		 * FieldData table requires: COLLECTION_ID PERSON_ID DATE_COLLECTED FIELD_ID USER_ID INSERT_TIME
		 */

		Date dateCollected = new Date();
		Field field = null;

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
			// SUBJECTID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			String[] fieldNameArray = csvReader.getHeaders();

			// Field count = column count - 2 (SUBJECTID and DATE_COLLECTED)
			fieldCount = fieldNameArray.length - 2;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();

				// Uploading a data dictionary file
				if(uploadType.equalsIgnoreCase("FIELD"))
				{
					if (csvReader.getColumnCount() < 8)
					{
						// Invalid file format
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append("The specified file does not appear to conform to the expected data dictionary file format.\n");
						stringBuffer.append("The specified file format was: " + fileFormat + "\n");
						stringBuffer.append("The specified delimiter was: " + phenotypicDelimChr + "\n");
						stringBuffer.append("The default data dictionary format is as follows:\n");
						stringBuffer.append("FIELD_NAME" + phenotypicDelimChr + "FIELD_TYPE" + phenotypicDelimChr + "DESCRIPTION" + phenotypicDelimChr + "UNITS" + phenotypicDelimChr + "ENCODED_VALUES" + phenotypicDelimChr + "MINIMUM_VALUE" + phenotypicDelimChr + "MAXIMUM_VALUE" + phenotypicDelimChr + "MISSING_VALUE" + "\n");
						stringBuffer.append("[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + "\n");
	
						fileValidationMessages.add(stringBuffer.toString());
						break;
					}
					else
					{
						// Loop through columns in current row in file, starting from the 2th position
						for (int i = 0; i < stringLineArray.length; i++)
						{
							// Check each line has same number of columns as header
							if (stringLineArray.length < fieldNameArray.length)
							{
								fileValidationMessages.add("Error at line " + i + ", the line has missing cells");
							}
	
							// Update progress
							curPos += stringLineArray[i].length() + 1; // update progress
						}
					}
				}
				else 
				{
					if (csvReader.getColumnCount() < 2 || fieldCount < 1 || !fieldNameArray[0].equalsIgnoreCase(Constants.SUBJECTUID) || !fieldNameArray[1].equalsIgnoreCase(Constants.DATE_COLLECTED))
					{
						// Invalid file format
						StringBuffer stringBuffer = new StringBuffer();
						stringBuffer.append("The specified file does not appear to conform to the expected phenotypic file format.\n");
						stringBuffer.append("The specified file format was: " + fileFormat + "\n");
						stringBuffer.append("The specified delimiter was: " + phenotypicDelimChr + "\n");
						stringBuffer.append("The default format is as follows:\n");
						stringBuffer.append(Constants.SUBJECTUID + phenotypicDelimChr + Constants.DATE_COLLECTED + phenotypicDelimChr + "FIELDNAME1" + phenotypicDelimChr + "FIELDNAME2" + phenotypicDelimChr + "FIELDNAME3" + phenotypicDelimChr + "FIELDNAMEX\n");
						stringBuffer.append("[subjectUid]" + phenotypicDelimChr + "[dateCollected]" + phenotypicDelimChr + "[field1value]" + phenotypicDelimChr + "[field2value]" + phenotypicDelimChr + "[field3value]" + phenotypicDelimChr + "[fieldXvalue]\n");
						stringBuffer.append("[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]\n");
	
						fileValidationMessages.add(stringBuffer.toString());
						break;
					}
					else
					{
						// Loop through columns in current row in file, starting from the 2th position
						for (int i = 0; i < stringLineArray.length; i++)
						{
							// Check each line has same number of columns as header
							if (stringLineArray.length < fieldNameArray.length)
							{
								fileValidationMessages.add("Error at line " + i + ", the line has missing cells");
							}
	
							// Update progress
							curPos += stringLineArray[i].length() + 1; // update progress
						}
					}
				}
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
	public java.util.Collection<String> validateMatrixPhenoFileData(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException
	{
		curPos = 0;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		/*
		 * FieldData table requires: COLLECTION_ID PERSON_ID DATE_COLLECTED FIELD_ID USER_ID INSERT_TIME
		 */

		Date dateCollected = new Date();
		Field field = null;

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
			// SUBJECTID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			String[] fieldNameArray = csvReader.getHeaders();

			// Field count = column count - 2 (SUBJECTID and DATE_COLLECTED)
			fieldCount = fieldNameArray.length - 2;

			int row = 1;
			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				
				// Fist column should be SubjectUID
				String subjectUid = stringLineArray[0];
				// Second/1th column should be date collected
				String dateCollectedStr = stringLineArray[1];
				
				// Check subject exists
				LinkSubjectStudy linkSubjectStudy = new LinkSubjectStudy();
				try
				{
					linkSubjectStudy = iArkCommonService.getSubjectByUID(subjectUid);
				}
				catch (au.org.theark.core.exception.EntityNotFoundException enfe)
				{
					// Subject not found...error
					ArkGridCell cell = new ArkGridCell(0, row);
					errorCells.add(cell);
					dataValidationMessages.add(PhenotypicValidationMessage.fieldDataSubjectUidNotFound(subjectUid));
				}
				
				// Check date collected is valid
				try
				{
					DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
					dateFormat.setLenient(false);
					dateCollected = dateFormat.parse(dateCollectedStr);
				}
				catch (ParseException pe)
				{
					dataValidationMessages.add(PhenotypicValidationMessage.dateCollectedNotValidDate(subjectUid, dateCollectedStr));
					errorCells.add(new ArkGridCell(1, row));
				}
				
				Collection<FieldData> fieldDataToUpdate = iPhenoService.searchFieldDataBySubjectAndDateCollected(linkSubjectStudy, dateCollected);
				// Assume inserts
				insertRows.add(row);
				int cols = stringLineArray.length;

				// Loop through columns in current row in file, starting from the 2th position
				for (int col = 2; col < cols; col++)
				{					
					FieldData fieldData = new FieldData();
					PhenoCollection phenoCollection = this.phenoCollection; 
					fieldData.setCollection(phenoCollection);
					fieldData.setDateCollected(dateCollected);

					// First/0th column should be the Subject UID
					// If no Subject UID found, caught by exception catch
					fieldData.setLinkSubjectStudy(linkSubjectStudy);
					
					// Set field
					field = new Field();
					field = iPhenoService.getFieldByNameAndStudy(fieldNameArray[col], study);
					fieldData.setField(field);
					
					// Other/ith columns should be the field data value
					String value = stringLineArray[col];
					fieldData.setValue(value);
					
					ArkGridCell gridCell = new ArkGridCell(col, row);
					// Validate the field data
					if(!PhenotypicValidator.validateFieldData(fieldData, dataValidationMessages))
					{
						warningCells.add(gridCell);
					}
					
					// Determine updates
					if(fieldDataToUpdate.contains(fieldData))
					{
						updateCells.add(gridCell);
						updateRows.add(row);
					}
					else
					{
						insertCells.add(gridCell);
						insertRows.add(row);
					}
					
					// Update progress
					curPos += stringLineArray[col].length() + 1; // update progress

					// Debug only - Show progress and speed
					log.debug("progress: " + decimalFormat.format(getProgress()) + " % | speed: " + decimalFormat.format(getSpeed()) + " KB/sec");
				}

				log.debug("\n");
				subjectCount++;
				row++;
			}
			
			for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
				Integer i = (Integer) iterator.next();
				dataValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update");
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
		log.debug("Validated " + subjectCount * fieldCount + " rows of data");

		for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
			Integer i = (Integer) iterator.next();
			dataValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update");
		}
		
		return dataValidationMessages;
	}
	
	/**
	 * Validates the phenotypic data dictionary file in the default "matrix" file format assumed: "FIELD_NAME","FIELD_TYPE","DESCRIPTION","UNITS","ENCODED_VALUES","MINIMUM_VALUE","MAXIMUM_VALUE","MISSING_VALUE"
	 * 
	 * @param fileInputStream
	 *           is the input stream of a file
	 * @throws IOException
	 *            input/output Exception
	 * @throws OutOfMemoryError
	 *            out of memory Exception
	 */
	public java.util.Collection<String> validateDataDictionaryFileData(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException
	{
		curPos = 0;
		int row = 1;

		InputStreamReader inputStreamReader = null;
		CsvReader csvReader = null;
		DecimalFormat decimalFormat = new DecimalFormat("0.00");

		/*
		 * Field table requires: ID, STUDY_ID, FIELD_TYPE_ID, NAME, DESCRIPTION, UNITS, MIN_VALUE, MAX_VALUE, ENCODED_VALUES, MISSING_VALUE 
		 */

		Field field = new Field();
		field.setStudy(study);
		
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

			csvReader.readHeaders();

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();
				
				// Fist column should be Field Name
				String fieldName = stringLineArray[0];
				int cols = stringLineArray.length;
				field = new Field();
				field.setStudy(study);
				field.setName(fieldName);
				
				if (csvReader.getIndex("FIELD_TYPE") > 0)
				{
					FieldType fieldType = new FieldType();
					fieldType = iPhenoService.getFieldTypeByName(stringLineArray[csvReader.getIndex("FIELD_TYPE")]);
					field.setFieldType(fieldType);
				}
				if (csvReader.getIndex("DESCRIPTION") > 0)
				{
					if(stringLineArray[csvReader.getIndex("DESCRIPTION")].length() > 0)
						field.setDescription(stringLineArray[csvReader.getIndex("DESCRIPTION")]);
				}
				if (csvReader.getIndex("UNITS") > 0)
				{
					if(stringLineArray[csvReader.getIndex("UNITS")].length() > 0)
						field.setUnits((stringLineArray[csvReader.getIndex("UNITS")]));
				}
				if (csvReader.getIndex("ENCODED_VALUES") > 0)
				{
					if(stringLineArray[csvReader.getIndex("ENCODED_VALUES")].length() > 0)
						field.setEncodedValues(stringLineArray[csvReader.getIndex("ENCODED_VALUES")]);
				}
				if (csvReader.getIndex("MINIMUM_VALUE") > 0)
				{
					if(stringLineArray[csvReader.getIndex("MINIMUM_VALUE")].length() > 0)
						field.setMinValue(stringLineArray[csvReader.getIndex("MINIMUM_VALUE")]);
				}
				if (csvReader.getIndex("MAXIMUM_VALUE") > 0)
				{	
					if(stringLineArray[csvReader.getIndex("MAXIMUM_VALUE")].length() > 0)
						field.setMaxValue(stringLineArray[csvReader.getIndex("MAXIMUM_VALUE")]);
				}
				if (csvReader.getIndex("MISSING_VALUE") > 0)
				{
					if(stringLineArray[csvReader.getIndex("MISSING_VALUE")].length() > 0)
						field.setMissingValue(stringLineArray[csvReader.getIndex("MISSING_VALUE")]);
				}
				
				Field oldField = iPhenoService.getFieldByNameAndStudy(fieldName, study);
				// Determine updates
				if(oldField.getId() != null)
				{
					updateRows.add(row);
					for(int col = 0; col < cols; col++)
						updateCells.add(new ArkGridCell(col, row));
				}
				else
				{
					insertRows.add(row);
				}
				
				ArkGridCell gridCell = null;

				if(field.getMinValue() != null)
				{
					gridCell = new ArkGridCell(csvReader.getIndex("MINIMUM_VALUE"), row);
					// Validate the field definition
					if(!PhenotypicValidator.validateFieldMinDefinition(field, dataValidationMessages))
					{
						errorCells.add(gridCell);
					}
				}
				
				if(field.getMaxValue() != null)
				{
					gridCell = new ArkGridCell(csvReader.getIndex("MAXIMUM_VALUE"), row);
					// Validate the field definition
					if(!PhenotypicValidator.validateFieldMaxDefinition(field, dataValidationMessages))
					{
						errorCells.add(gridCell);
					}
				}
				
				if(field.getMissingValue() != null)
				{
					gridCell = new ArkGridCell(csvReader.getIndex("MISSING_VALUE"), row);
					// Validate the field definition
					if(!PhenotypicValidator.validateFieldMissingDefinition(field, dataValidationMessages))
					{
						errorCells.add(gridCell);
					}
				}
				
				fieldCount++;
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
		
		for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
			Integer i = (Integer) iterator.next();
			dataValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update");
		}

		return dataValidationMessages;
	}
	
	private static boolean validateFieldMissingDefinition(Field field, Collection<String> errorMessages)
	{
		boolean isValid = false;
		// Number field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER))
		{
			try
			{
				Float floatFieldValue = Float.parseFloat(field.getMissingValue());
				isValid = true;
			}
			catch (NumberFormatException nfe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldMissingValueNotDefinedType(field));
				isValid = false;
			}
			catch (NullPointerException npe)
			{
				isValid = false;
			}
		}
		
		// Date field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))
		{
			try
			{
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(field.getMissingValue());
				isValid = true;
			}
			catch (ParseException pe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDefinitionMissingValueNotValidDate(field));
				isValid = false;
			}
			catch (NullPointerException npe)
			{
				isValid = false;
			}
		}
		return isValid;
	}

	private static boolean validateFieldMaxDefinition(Field field, Collection<String> errorMessages)
	{
		boolean isValid = false;
		// Number field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER))
		{
			try
			{
				Float floatFieldValue = Float.parseFloat(field.getMaxValue());
				isValid = true;
			}
			catch (NumberFormatException nfe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldMaxValueNotDefinedType(field));
				isValid = false;
			}
			catch (NullPointerException npe)
			{
				isValid = false;
			}
		}	
		
		// Date field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))
		{
			try
			{
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(field.getMaxValue());
				isValid = true;
			}
			catch (ParseException pe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDefinitionMaxValueNotValidDate(field));
				isValid = false;
			}
			catch (NullPointerException npe)
			{
				isValid = false;
			}
		}
		return isValid;
	}

	private static boolean validateFieldMinDefinition(Field field, Collection<String> errorMessages)
	{
		boolean isValid = false;
		// Number field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER))
		{
			try
			{
				Float floatFieldValue = Float.parseFloat(field.getMinValue());
				isValid = true;
			}
			catch (NumberFormatException nfe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldMinValueNotDefinedType(field));
				log.error("Field data number format exception " + nfe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}
		}
		
		// Date field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))
		{
			try
			{
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(field.getMinValue());
				isValid = true;
			}
			catch (ParseException pe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDefinitionMinValueNotValidDate(field));
				log.error("Field data date parse exception " + pe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}
		}
		return isValid;
	}

	private static boolean validateFieldDefinition(Field field, Collection<String> errorMessages)
	{
		boolean isValid = false;
		// Number field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER))
		{
			try
			{
				Float floatFieldValue = Float.parseFloat(field.getMinValue());
				isValid = true;
			}
			catch (NumberFormatException nfe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldMinValueNotDefinedType(field));
				log.error("Field data number format exception " + nfe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}
			
			try
			{
				Float floatFieldValue = Float.parseFloat(field.getMaxValue());
				isValid = true;
			}
			catch (NumberFormatException nfe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldMaxValueNotDefinedType(field));
				log.error("Field data number format exception " + nfe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}
			
			try
			{
				Float floatFieldValue = Float.parseFloat(field.getMissingValue());
				isValid = true;
			}
			catch (NumberFormatException nfe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldMissingValueNotDefinedType(field));
				log.error("Field data number format exception " + nfe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}
		}

		// Date field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))
		{
			try
			{
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(field.getMinValue());
				isValid = true;
			}
			catch (ParseException pe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDefinitionMinValueNotValidDate(field));
				log.error("Field data date parse exception " + pe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}
			
			try
			{
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(field.getMaxValue());
				isValid = true;
			}
			catch (ParseException pe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDefinitionMaxValueNotValidDate(field));
				log.error("Field data date parse exception " + pe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}
			
			try
			{
				Date dateFieldValue = new Date();
				DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY);
				dateFormat.setLenient(false);
				dateFieldValue = dateFormat.parse(field.getMissingValue());
				isValid = true;
			}
			catch (ParseException pe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDefinitionMissingValueNotValidDate(field));
				log.error("Field data date parse exception " + pe.getMessage());
				isValid = false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				isValid = false;
			}

		}

		return isValid;
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

	public HashSet<Integer> getInsertRows()
	{
		return insertRows;
	}

	public void setInsertRows(HashSet<Integer> insertRows)
	{
		this.insertRows = insertRows;
	}

	public HashSet<Integer> getUpdateRows()
	{
		return updateRows;
	}

	public void setUpdateRows(HashSet<Integer> updateRows)
	{
		this.updateRows = updateRows;
	}
	
	public HashSet<ArkGridCell> getInsertCells()
	{
		return insertCells;
	}

	public void setInsertCells(HashSet<ArkGridCell> insertCells)
	{
		this.insertCells = insertCells;
	}

	public HashSet<ArkGridCell> getUpdateCells()
	{
		return updateCells;
	}

	public void setUpdateCells(HashSet<ArkGridCell> updateCells)
	{
		this.updateCells = updateCells;
	}

	public HashSet<ArkGridCell> getErrorCells()
	{
		return errorCells;
	}

	public void setErrorCells(HashSet<ArkGridCell> errorCells)
	{
		this.errorCells = errorCells;
	}

	public HashSet<ArkGridCell> getWarningCells()
	{
		return warningCells;
	}

	public void setWarningCells(HashSet<ArkGridCell> warningCells)
	{
		this.warningCells = warningCells;
	}
}