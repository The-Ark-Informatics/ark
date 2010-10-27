package au.org.theark.phenotypic.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Date;
import java.util.List;

import org.apache.commons.lang.time.StopWatch;
import org.hibernate.Criteria;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import au.org.theark.phenotypic.exception.FileFormatException;
import au.org.theark.phenotypic.exception.PhenotypicSystemException;
import au.org.theark.phenotypic.exception.StorageIOException;
import au.org.theark.phenotypic.model.dao.IPhenotypicDao;
import au.org.theark.phenotypic.model.entity.Collection;
import au.org.theark.phenotypic.model.entity.Field;
import au.org.theark.phenotypic.model.entity.FieldData;
import au.org.theark.study.model.entity.Person;
import au.org.theark.study.model.entity.VitalStatus;

import com.csvreader.CsvReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import au.org.theark.core.Constants;

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
	private List<Field>		fieldList;
	private Long				studyId;
	static Logger				log						= LoggerFactory.getLogger(PhenotypicImport.class);

	/**
	 * PhenotypicImport constructor
	 * 
	 * @param phenotypicDao
	 *           data access object perform select/insert/updates to the database
	 */
	public PhenotypicImport(IPhenotypicDao phenotypicDao)
	{
		this.phenotypicDao = phenotypicDao;
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
	}

	/**
	 * Imports the phenotypic data file in the default "matrix" file format assumed: SUBJECTID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN...
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
	public void processMatrixPhenoFile(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException
	{
		if (phenotypicDao == null)
		{
			throw new PhenotypicSystemException("Aborting: Must have a phenotypic storage object defined before calling.");
		}

		curPos = 0;

		InputStreamReader isr = null;
		CsvReader csvRdr = null;
		DecimalFormat twoPlaces = new DecimalFormat("0.00");
		FieldData fieldDataEntity = new FieldData();

		try
		{
			isr = new InputStreamReader(fileInputStream);
			csvRdr = new CsvReader(isr, phenotypicDelimChr);
			String[] newLine;

			// srcLength = file.length();
			srcLength = inLength;
			if (srcLength <= 0)
			{
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
				// return;
			}

			timer = new StopWatch();
			timer.start();

			// Set field list (note 2th column to Nth column)
			// SUBJECTID DATE_COLLECTED F1 F2 FN
			// 0 1 2 3 N
			String[] fieldNameArray = csvRdr.getHeaders();

			while (csvRdr.readRecord())
			{
				// do something with the newline to put the data into
				// the variables defined above
				newLine = csvRdr.getValues();

				if (csvRdr.getColumnCount() < 2)
				{
					// non-compliant file
					throw new FileFormatException("The specified file does not appear to conform to the expected phenotypic file format.");
				}
				else
				{
					for (int i = 0; i < newLine.length; i++)
					{
						// Store actual file cell data
						String cellData = newLine[i];

						switch (i)
						{
						case 0:
							// First column should be the SubjectUID

							try
							{
								// Try to cast cellData to person/subject identifier
								// TODO: studyService.getSubject(String subjectUid))
								// eg: person = studyService.getSubject(cellData));
								person = new Person(new Long(cellData));
							}
							catch (NumberFormatException nfe)
							{
								log.error("PhenotypicImport: Tried to cast PersonId/SubjectUid to a number and failed.... " + nfe);
							}

							// Set Vital status of person/subject
							VitalStatus vitalStatus = new VitalStatus(new Long(1));
							vitalStatus.setStatusName("Alive");
							person.setVitalStatus(vitalStatus);

							// Set Person
							fieldDataEntity.setPerson(person);

							break;
						case 1:
							// Second column should be date collected
							DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DATE_FORMAT);
							Date cellDate = dateFormat.parse(cellData);
							fieldDataEntity.setDateCollected(cellDate);
							break;
						default:
							// Set field
							Field field = phenotypicDao.getFieldByName(studyId, fieldNameArray[i]);
							fieldDataEntity.setField(field);

							// Set fieldData values
							fieldDataEntity.setValue(cellData);

							continue;
						}

						phenotypicDao.createFieldData(fieldDataEntity);
						curPos += cellData.length() + 1; // update progress
					}
				}
				subjectCount++;

				// Debug only - Show progress and speed
				log.info("progress: " + twoPlaces.format(getProgress()) + " % | speed: " + twoPlaces.format(getSpeed()) + " KB/sec");
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
			log.info("Total elapsed time: " + timer.getTime() + " ms or " + twoPlaces.format(timer.getTime() / 1000.0) + " s");
			log.info("Total file size: " + srcLength + " B or " + twoPlaces.format(srcLength / 1024.0 / 1024.0) + " MB");
			if (timer != null)
				timer = null;
			if (csvRdr != null)
			{
				try
				{
					csvRdr.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (isr != null)
			{
				try
				{
					isr.close();
				}
				catch (Exception ex)
				{
					log.error("Cleanup operation failed: isr.close()", ex);
				}
			}
			// Restore the state of variables
			srcLength = -1;
		}
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
