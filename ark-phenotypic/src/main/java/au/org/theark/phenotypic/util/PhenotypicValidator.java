package au.org.theark.phenotypic.util;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
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

import jxl.Cell;
import jxl.Sheet;
import jxl.Workbook;
import jxl.read.biff.BiffException;

import org.apache.commons.lang.time.StopWatch;
import org.apache.shiro.SecurityUtils;
import org.apache.wicket.util.io.ByteArrayOutputStream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.core.Constants;
import au.org.theark.core.exception.ArkBaseException;
import au.org.theark.core.exception.EntityNotFoundException;
import au.org.theark.core.model.pheno.entity.Field;
import au.org.theark.core.model.pheno.entity.FieldData;
import au.org.theark.core.model.pheno.entity.FieldType;
import au.org.theark.core.model.pheno.entity.PhenoCollection;
import au.org.theark.core.model.study.entity.LinkSubjectStudy;
import au.org.theark.core.model.study.entity.Study;
import au.org.theark.core.service.IArkCommonService;
import au.org.theark.core.web.component.ArkGridCell;
import au.org.theark.phenotypic.exception.FileFormatException;
import au.org.theark.phenotypic.exception.PhenotypicSystemException;
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
	java.util.Collection<String>	fileValidationMessages	= new ArrayList<String>();
	java.util.Collection<String>	dataValidationMessages	= new ArrayList<String>();
	private IPhenotypicService		iPhenotypicService			= null;
	private IArkCommonService<Void>		iArkCommonService			= null;
	private StringBuffer				uploadReport				= null;
	private HashSet<Integer>		insertRows 					= new HashSet<Integer>();
	private HashSet<Integer>		updateRows 					= new HashSet<Integer>();
	private HashSet<ArkGridCell> insertCells 				= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell> updateCells 				= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell> warningCells 				= new HashSet<ArkGridCell>();
	private HashSet<ArkGridCell> errorCells					= new HashSet<ArkGridCell>();
	private String uploadType = new String("");
	private String						fileFormat					= au.org.theark.core.Constants.DEFAULT_FILE_FORMAT;					// default
	private int row = 1;

	/**
	 * PhenotypicValidator constructor
	 * 
	 */
	public PhenotypicValidator()
	{
	}
	
	public PhenotypicValidator(IArkCommonService<Void> iArkCommonService, IPhenotypicService iPhenotypicService, UploadVO uploadVo)
	{
		this.iArkCommonService = iArkCommonService;
		this.iPhenotypicService = iPhenotypicService;
		Long sessionCollectionId = (Long)SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.phenotypic.web.Constants.SESSION_PHENO_COLLECTION_ID);
		if(sessionCollectionId != null)
		{
			this.phenoCollection = iPhenotypicService.getPhenoCollection(sessionCollectionId);
		}
		else
		{
			if(uploadVo.getPhenoCollection() != null)
			{
				this.phenoCollection = iPhenotypicService.getPhenoCollection(uploadVo.getPhenoCollection().getId());
			}
		}
		
		// Set study in context
		Long studyId = (Long) SecurityUtils.getSubject().getSession().getAttribute(au.org.theark.core.Constants.STUDY_CONTEXT_ID);
		if(studyId != null)
		{
			Study study = iArkCommonService.getStudy(studyId);
			this.study = study;
		}
		this.fileValidationMessages = new ArrayList<String>();
		this.dataValidationMessages = new ArrayList<String>();
		
		String filename = uploadVo.getFileUpload().getClientFileName();
		this.fileFormat = filename.substring(filename.lastIndexOf('.')+1).toUpperCase();
		
		this.phenotypicDelimChr = uploadVo.getUpload().getDelimiterType().getDelimiterCharacter();
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
		boolean isValidFieldData = true;
		Field field = fieldData.getField();

		// Number field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER))
		{
			try
			{
				Float floatFieldValue = Float.parseFloat(fieldData.getValue());
			}
			catch (NumberFormatException nfe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDataNotDefinedType(field, fieldData));
				log.error("Field data number format exception " + nfe.getMessage());
				isValidFieldData = false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				isValidFieldData = false;
			}
		}

		// Character field type
		if (field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER))
		{
			try
			{
				String stringFieldValue = fieldData.getValue();
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				isValidFieldData = false;
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
			}
			catch (ParseException pe)
			{
				errorMessages.add(PhenotypicValidationMessage.fieldDataNotValidDate(field, fieldData));
				log.error("Field data date parse exception " + pe.getMessage());
				isValidFieldData = false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				isValidFieldData = false;
			}

		}

		return isValidFieldData;
	}

	/**
	 * Returns true if field data value is within the defined range as specified in the data dictionary
	 * 
	 * @param fieldData
	 * @return boolean
	 */
	public static boolean isInValidRange(FieldData fieldData, java.util.Collection<String> errorMessages)
	{
		boolean isInValidRange = true;
		Field field = fieldData.getField();
		String minValue = field.getMinValue();
		String maxValue = field.getMaxValue();
		
		if((minValue == null || minValue.isEmpty()) && (maxValue == null || maxValue.isEmpty()))
		{
			return isInValidRange;
		}
			
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
					isInValidRange = false;
				}
			}
			catch (NumberFormatException nfe)
			{
				log.error("Field data number format exception " + nfe.getMessage());
				isInValidRange = false;
			}
			catch (NullPointerException npe)
			{
				log.error("Field data null pointer exception " + npe.getMessage());
				isInValidRange = false;
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
						isInValidRange = false;
					}
				}
				catch (ParseException pe)
				{
					isInValidRange = false;
				}
			}
		}
		return isInValidRange;
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

		Field field = fieldData.getField();

		// Validate if encoded values is defined, and not a DATE fieldType 
		if (field.getEncodedValues() != null && !field.getEncodedValues().isEmpty() && !field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))
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
		boolean isValid = true;
		boolean isValidFieldData = true;
		boolean isValidEncodedValues = true;
		boolean isValidRange = true;
		
		isValidFieldData = isValidFieldData(fieldData, errorMessages);
		isValidEncodedValues = isInEncodedValues(fieldData, errorMessages);
		isValidRange = isInValidRange(fieldData, errorMessages);
		
		isValid = (isValidFieldData && isValidEncodedValues && isValidRange);
		return(isValid);
	}
	
	public static boolean fieldDataPassesQualityControl(FieldData fieldData, java.util.Collection<String> errorMessages)
	{
		boolean passesQualityControl = true;
		// Validate the field data
		if (validateFieldData(fieldData, errorMessages))
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
		row = 0;

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
			
			csvReader.readHeaders();

			// Set field list (note 2th column to Nth column)
			// SUBJECTID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			String[] headerColumnArray = csvReader.getHeaders();
			boolean headerError = false;
			
			if(headerColumnArray.length <= 1)
				headerError = true;
			
			// Uploading a data dictionary file
			if(uploadType.equalsIgnoreCase("FIELD"))
			{
				// Uploading a Field (Data Dictionary) file
				
				Collection<String> dataDictionaryColumns = new ArrayList<String>();
				String[] dataDictionaryColumnArray = au.org.theark.phenotypic.web.Constants.DATA_DICTIONARY_HEADER;
				
				for (int i = 0; i < dataDictionaryColumnArray.length; i++)
				{
					dataDictionaryColumns.add(dataDictionaryColumnArray[i]);
				}
				
				for (int i = 0; i < headerColumnArray.length; i++)
				{
					if(!dataDictionaryColumns.contains(headerColumnArray[i]))
					{
						headerError = true;
						break;
					}
				}
				
				if (headerError)
				{
					// Invalid file format
					StringBuffer stringBuffer = new StringBuffer();
					String delimiterTypeName = iPhenotypicService.getDelimiterTypeByDelimiterChar(phenotypicDelimChr);
					
					stringBuffer.append("The specified file does not appear to conform to the expected data dictionary file format.\n");
					stringBuffer.append("The specified file format was: " + fileFormat + "\n");
					stringBuffer.append("The specified delimiter was: [" + phenotypicDelimChr + "] (" + delimiterTypeName + ")\n");
					stringBuffer.append("The default data dictionary format is as follows:\n");
					stringBuffer.append("FIELD_NAME" + phenotypicDelimChr + "FIELD_TYPE" + phenotypicDelimChr + "DESCRIPTION" + phenotypicDelimChr + "UNITS" + phenotypicDelimChr + "ENCODED_VALUES" + phenotypicDelimChr + "MINIMUM_VALUE" + phenotypicDelimChr + "MAXIMUM_VALUE" + phenotypicDelimChr + "MISSING_VALUE" + "\n");
					stringBuffer.append("[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + phenotypicDelimChr + "[...]" + "\n");

					fileValidationMessages.add(stringBuffer.toString());
				}
				
				for (int i = 0; i < headerColumnArray.length; i++)
				{
					if(!dataDictionaryColumns.contains(headerColumnArray[i]))
					{
						fileValidationMessages.add("Error: the column name " + headerColumnArray[i] + " is not a valid column name.");
					}
				}
			}
			else
			{
				// Uploading a fieldData file
				
				// Field count = column count - 2 (SUBJECTID and DATE_COLLECTED)
				fieldCount = csvReader.getHeaderCount() - 2;
				
				if (csvReader.getHeaderCount() < 2 || fieldCount < 1 || !headerColumnArray[0].equalsIgnoreCase(Constants.SUBJECTUID) || !headerColumnArray[1].equalsIgnoreCase(Constants.DATE_COLLECTED))
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
				}
			}

			srcLength = inLength - csvReader.getHeaders().toString().length();
			log.debug("Header length: " + csvReader.getHeaders().toString().length());
			
			row = 1;

			// Loop through all rows in file
			while (csvReader.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				stringLineArray = csvReader.getValues();

				// Loop through columns in current row in file, starting from the 2th position
				for (int i = 0; i < stringLineArray.length; i++)
				{	
					// Update progress
					curPos += stringLineArray[i].length() + 1; // update progress
				}
				
				int numberOfColumnsInLine = stringLineArray.length;
				int numberOfColumnsInHeader = headerColumnArray.length;
				
				// Check each line has same number of columns as header
				//if (stringLineArray.length < headerColumnArray.length)
				//{
				//	fileValidationMessages.add("Error at line " + row + ", the line has missing cells");
				//}
				
				subjectCount++;
				row++;
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
					DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DD_MM_YYYY_HH_MM_SS);
					dateFormat.setLenient(false);
					if(dateCollectedStr.length() == 10)
					{
						dateCollectedStr = dateCollectedStr + " 00:00:00"; 
					}
					dateCollected = dateFormat.parse(dateCollectedStr);
				}
				catch (ParseException pe)
				{
					dataValidationMessages.add(PhenotypicValidationMessage.dateCollectedNotValidDate(subjectUid, dateCollectedStr));
					errorCells.add(new ArkGridCell(1, row));
				}
				
				Collection<FieldData> fieldDataToUpdate = iPhenotypicService.searchFieldDataBySubjectAndDateCollected(linkSubjectStudy, dateCollected);
				// Assume inserts
				insertRows.add(row);
				int cols = stringLineArray.length;

				// Loop through columns in current row in file, starting from the 2th position
				for (int col = 2; col < cols; col++)
				{					
					FieldData fieldData = new FieldData();
					fieldData.setCollection(this.phenoCollection);
					fieldData.setDateCollected(dateCollected);

					// First/0th column should be the Subject UID
					// If no Subject UID found, caught by exception catch
					fieldData.setLinkSubjectStudy(linkSubjectStudy);
					
					// Check field exists
					try
					{
						// Set field
						field = new Field();
						fieldName = fieldNameArray[col];
						field = iPhenotypicService.getFieldByNameAndStudy(fieldName, study);
						fieldData.setField(field);
						
						// Other/ith columns should be the field data value
						String value = stringLineArray[col];
						fieldData.setValue(value);
						
						ArkGridCell gridCell = new ArkGridCell(col, row);
						// Validate the field data
						boolean isValid = validateFieldData(fieldData, dataValidationMessages);
						if(!isValid)
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
					catch (au.org.theark.core.exception.EntityNotFoundException enfe)
					{
						// Field not found...error
						ArkGridCell cell = new ArkGridCell(0, row);
						errorCells.add(cell);
						dataValidationMessages.add(PhenotypicValidationMessage.fieldNotFound(fieldName));
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

		if(errorCells.isEmpty())
		{
			for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
				Integer i = (Integer) iterator.next();
				dataValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update");
			}
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
				
				ArkGridCell gridCell = null;
				
				// Fist column should be Field Name
				fieldName = csvReader.get("FIELD_NAME");
				
				// Only check rows with a valid fieldName
				if(!fieldName.isEmpty())
				{
					int cols = stringLineArray.length;
					field = new Field();
					field.setStudy(study);
					field.setName(fieldName);
					
					FieldType fieldType = new FieldType();
					fieldType = iPhenotypicService.getFieldTypeByName(csvReader.get("FIELD_TYPE"));
					field.setFieldType(fieldType);
					field.setDescription(csvReader.get("DESCRIPTION"));
					field.setUnits((csvReader.get("UNITS")));
					field.setEncodedValues(csvReader.get("ENCODED_VALUES"));
					field.setMinValue(csvReader.get("MINIMUM_VALUE"));
					field.setMaxValue(csvReader.get("MAXIMUM_VALUE"));
					field.setMissingValue(csvReader.get("MISSING_VALUE"));
					
					try
					{
						Field oldField = iPhenotypicService.getFieldByNameAndStudy(fieldName, study);
						// Determine updates
						if(oldField.getId() != null)
						{
							updateRows.add(row);
							for(int col = 0; col < cols; col++)
								updateCells.add(new ArkGridCell(col, row));
							
							// Check field type same as one in database
							if(!(field.getFieldType().getName().equalsIgnoreCase(oldField.getFieldType().getName())))
							{
								gridCell = new ArkGridCell(csvReader.getIndex("FIELD_TYPE"), row);
								StringBuffer stringBuffer = new StringBuffer();
								stringBuffer.append("Error: ");
								stringBuffer.append("The existing field ");
								stringBuffer.append(fieldName);
								stringBuffer.append(" already has data associated with it, and cannot have it's field type changed");
								dataValidationMessages.add(stringBuffer.toString());
								errorCells.add(gridCell);
							}
						}
					}
					catch(EntityNotFoundException enf)
					{
						insertRows.add(row);
					}
					
					if(csvReader.get("FIELD_TYPE") != null)
					{
						gridCell = new ArkGridCell(csvReader.getIndex("FIELD_TYPE"), row);
						if(!PhenotypicValidator.validateFieldType(this.fieldName, csvReader.get("FIELD_TYPE"), dataValidationMessages))
						{
							errorCells.add(gridCell);
							field.getFieldType().setName(csvReader.get("FIELD_TYPE"));
						}
					}
					
					if(field.getEncodedValues() != null  && !field.getEncodedValues().isEmpty())
					{
						gridCell = new ArkGridCell(csvReader.getIndex("ENCODED_VALUES"), row);
						// Validate encoded values not a date type
						if(!PhenotypicValidator.validateEncodedValues(field, dataValidationMessages))
						{
							errorCells.add(gridCell);
						}
					}
	
					if(field.getMinValue() != null  && !field.getMinValue().isEmpty())
					{
						gridCell = new ArkGridCell(csvReader.getIndex("MINIMUM_VALUE"), row);
						// Validate the field definition
						if(!PhenotypicValidator.validateFieldMinDefinition(field, dataValidationMessages))
						{
							errorCells.add(gridCell);
						}
					}
					
					if(field.getMaxValue() != null  && !field.getMaxValue().isEmpty())
					{
						gridCell = new ArkGridCell(csvReader.getIndex("MAXIMUM_VALUE"), row);
						// Validate the field definition
						if(!PhenotypicValidator.validateFieldMaxDefinition(field, dataValidationMessages))
						{
							errorCells.add(gridCell);
						}
					}
					
					if(field.getMissingValue() != null && !field.getMissingValue().isEmpty())
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
		
		if(errorCells.isEmpty())
		{
			for (Iterator<Integer> iterator = updateRows.iterator(); iterator.hasNext();) {
				Integer i = (Integer) iterator.next();
				dataValidationMessages.add("Data on row " + i.intValue() + " exists, please confirm update");
			}
		}

		return dataValidationMessages;
	}
	
	/**
	 * Validates the ENCODED_VALUES column in a matrix Data Dictionary file
	 * 
	 * @param field
	 *           is the field entity in question
	 * @param errorMessages
	 *           is the error messages to add to is any errors
	 * @return true if field.fieldType is NOT a DATE
	 */
	private static boolean validateEncodedValues(Field field, Collection<String> errorMessages)
	{
		boolean isValid = false;
		if(!field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE))
		{	
			isValid = true;
		}
		else
		{
			errorMessages.add(PhenotypicValidationMessage.fieldTypeIsDateWithEncodedValue(field.getName()));
		}
		return isValid;
	}

	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN... Where N is any number of columns
	 * 
	 * @param inputStream
	 *           is the input stream of the file
	 * @param fileFormat
	 *           is the file format (eg txt)
	 * @param delimChar
	 *           is the delimiter character of the file (eg comma)
	 * @return a collection of validation messages
	 */
	public Collection<String> validateMatrixPhenoFileFormat(InputStream inputStream, String fileFormat, char delimChar)
	{
		java.util.Collection<String> validationMessages = null;

		try
		{
			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS"))
			{
				Workbook w;
				try
				{
					w = Workbook.getWorkbook(inputStream);
					inputStream = convertXlsToCsv(w);
					inputStream.reset();
					phenotypicDelimChr = ',';
				}
				catch (BiffException e)
				{
					log.error(e.getMessage());
				}
				catch (IOException e)
				{
					log.error(e.getMessage());
				}
			}
			validationMessages = validateMatrixPhenoFileFormat(inputStream, inputStream.toString().length());
		}
		catch (FileFormatException ffe)
		{
			log.error("FILE_FORMAT_EXCPEPTION: " + ffe);
		}
		catch (ArkBaseException abe)
		{
			log.error("ARK_BASE_EXCEPTION: " + abe);
		}
		return validationMessages;
	}
	
	/**
	 * Validates the file in the default "matrix" file format assumed: SUBJECTUID,FIELD1,FIELD2,FIELDN... Where N is any number of columns
	 * 
	 * @param inputStream
	 *           is the input stream of the file
	 * @param fileFormat
	 *           is the file format (eg txt)
	 * @param delimChar
	 *           is the delimiter character of the file (eg comma)
	 * @return a collection of validation messages
	 */
	public Collection<String> validateMatrixPhenoFileData(InputStream inputStream, String fileFormat, char delimChar)
	{
		java.util.Collection<String> validationMessages = null;

		try
		{
			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS"))
			{
				Workbook w;
				try
				{
					w = Workbook.getWorkbook(inputStream);
					inputStream = convertXlsToCsv(w);
					inputStream.reset();
					phenotypicDelimChr = ',';
				}
				catch (BiffException e)
				{
					log.error(e.getMessage());
				}
				catch (IOException e)
				{
					log.error(e.getMessage());
				}
			}
			validationMessages = validateMatrixPhenoFileData(inputStream, inputStream.toString().length());
		}
		catch (FileFormatException ffe)
		{
			log.error("FILE_FORMAT_EXCPEPTION: " + ffe);
		}
		catch (ArkBaseException abe)
		{
			log.error("ARK_BASE_EXCEPTION: " + abe);
		}
		return validationMessages;
	}
	
	/**
	 * Validates the file in the default "matrix" data dictionary file format assumed: "FIELD_NAME","FIELD_TYPE","DESCRIPTION","UNITS","ENCODED_VALUES","MINIMUM_VALUE","MAXIMUM_VALUE","MISSING_VALUE"
	 * 
	 * @param inputStream
	 *           is the input stream of the file
	 * @param fileFormat
	 *           is the file format (eg CSV, TXT or XLS)
	 * @param delimChar
	 *           is the delimiter character of the file (eg COMMA, TAB, PIPE etc)
	 * @return a collection of validation messages
	 */
	public Collection<String> validateDataDictionaryFileData(InputStream inputStream, String fileFormat, char delimChar)
	{
		java.util.Collection<String> validationMessages = null;

		try
		{
			// If Excel, convert to CSV for validation
			if (fileFormat.equalsIgnoreCase("XLS"))
			{
				Workbook w;
				try
				{
					w = Workbook.getWorkbook(inputStream);
					inputStream = convertXlsToCsv(w);
					inputStream.reset();
					phenotypicDelimChr = ',';
				}
				catch (BiffException e)
				{
					log.error(e.getMessage());
				}
				catch (IOException e)
				{
					log.error(e.getMessage());
				}
			}
			validationMessages = validateDataDictionaryFileData(inputStream, inputStream.toString().length());
		}
		catch (FileFormatException ffe)
		{
			log.error("FILE_FORMAT_EXCPEPTION: " + ffe);
		}
		catch (ArkBaseException abe)
		{
			log.error("ARK_BASE_EXCEPTION: " + abe);
		}
		return validationMessages;
	}
	
	/**
	 * Return the inputstream of the converted workbook as csv
	 * 
	 * @return inputstream of the converted workbook as csv
	 */
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
						osw.write(phenotypicDelimChr);
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
	
	private static boolean validateFieldMissingDefinition(Field field, Collection<String> errorMessages)
	{
		boolean isValid = false;
		
		if (!(field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER) ||
				field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER) ||
				field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)
				))
		{
			errorMessages.add(PhenotypicValidationMessage.fieldMissingValueNotDefinedType(field));
			return isValid;
		}
		
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
		
		if (!(field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER) ||
				field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER) ||
				field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)
				))
		{
			errorMessages.add(PhenotypicValidationMessage.fieldMaxValueNotDefinedType(field));
			return isValid;
		}
		
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
		
		if (!(field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER) ||
				field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER) ||
				field.getFieldType().getName().equalsIgnoreCase(Constants.FIELD_TYPE_DATE)
				))
		{
			errorMessages.add(PhenotypicValidationMessage.fieldMinValueNotDefinedType(field));
			return isValid;
		}
		
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
	
	private static boolean validateFieldType(String fieldName, String fieldType, Collection<String> errorMessages)
	{
		boolean isValid = false;
		
		if (fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_CHARACTER) ||
				fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_NUMBER) ||
				fieldType.equalsIgnoreCase(Constants.FIELD_TYPE_DATE)
				)
		{
			isValid = true;
		}
		else
		{
			errorMessages.add(PhenotypicValidationMessage.fieldTypeNotDefined(fieldName, fieldType));
			isValid = false;
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