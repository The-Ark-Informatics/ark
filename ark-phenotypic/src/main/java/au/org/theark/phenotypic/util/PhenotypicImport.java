package au.org.theark.phenotypic.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;
import java.util.Date;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import au.org.theark.phenotypic.exception.FileFormatException;
import au.org.theark.phenotypic.exception.PhenotypicSystemException;
import au.org.theark.phenotypic.exception.StorageIOException;
import au.org.theark.phenotypic.model.dao.IPhenotypicStorage;

import com.csvreader.CsvReader;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.text.ParseException;

import au.org.theark.core.Constants;


/**
 * PhenotypicImport provides support for importing PLINK compatible
 * Ped and Map files.  It features state-machine behaviour to 
 * allow an external class to deal with how to store the data
 * pulled out of the files.
 *
 * @author cellis
 */
@SuppressWarnings("unused")
public class PhenotypicImport {
	
	
	private String fieldName;
	private long subjectCount;
	private long fieldCount;
	private double speed;
	private long curPos;
	private long srcLength = -1;	// -1 means nothing being processed
	private StopWatch timer = null;
	private char phenotypicDelimChr = ',';	//default phenotypic file delimiter: COMMA
	
	private IPhenotypicStorage phenotypicStorage = null;
	
	static Logger log = LoggerFactory.getLogger(PhenotypicImport.class);
	

	public void setPhenotypicStorage(IPhenotypicStorage phenotypicStorage) {
		this.phenotypicStorage = phenotypicStorage;
	}

	public IPhenotypicStorage setPhenotypicStorage() {
		return phenotypicStorage;
	}
	
	/**
	 * 
	 * @param phenotypicStorage is required for defining where the phenotypic data is to go
	 */
	public PhenotypicImport(IPhenotypicStorage phenotypicStorage) {
		setPhenotypicStorage(phenotypicStorage);
	}
	
	/**
	 * Imports the phenotypic data file
	 * Default file format assumed:
	 * SUBJECTID,DATE_COLLECTED,FIELD1,FIELD2,FIELDN...
	 * 
	 * Where N is any number of columns
	 * 
	 * @param fileInputStream is the input stream of a file
	 * @throws IOException
	 * @throws OutOfMemoryError
	 */
	public void processFile(InputStream fileInputStream, long inLength) throws FileFormatException, PhenotypicSystemException {
		if (phenotypicStorage == null) {
			throw new PhenotypicSystemException("Aborting: Must have a phenotypic storage object defined before calling.");
		}
		
		//File file = new File(mapFilePath);
		curPos = 0;

		InputStreamReader isr = null;
		CsvReader csvRdr = null;
		DecimalFormat twoPlaces = new DecimalFormat("0.00");

		try {
			isr = new InputStreamReader(fileInputStream);
			csvRdr = new CsvReader(isr, phenotypicDelimChr);
			String[] newLine;
			
			//srcLength = file.length();
			srcLength = inLength;
			if (srcLength <= 0) {
				throw new FileFormatException("The input size was not greater than 0.  Actual length reported: " + srcLength);
				//return;
			}

			timer = new StopWatch();
			timer.start();
			
			
			while(csvRdr.readRecord()) {
				// do something with the newline to put the data into
				// the variables defined above
				
				newLine = csvRdr.getValues();
				
				try {
					phenotypicStorage.init();
					if (csvRdr.getColumnCount() < 2) {
						// non-compliant map file
						throw new FileFormatException("The specified file does not appear to conform to the expected phenotypic file format.");
					}
					else
					{	
						for (int i = 0; i < newLine.length; i++) {
							String cellData = newLine[i];
							/* Debug only - Show data
							if (i < newLine.length - 1)
								System.out.print(cellData + ",");
							else
								log.info(cellData);
							*/
							switch (i) {
							case 0:
								// first column should be the subjectid
								phenotypicStorage.setSubjectId(cellData);
								break;
							case 1:
								// second column should be date collected
								DateFormat dateFormat = new SimpleDateFormat(au.org.theark.core.Constants.DATE_FORMAT);
								Date cellDate = dateFormat.parse(cellData);
								phenotypicStorage.setDateCollected(cellDate);
								break;
							default:
								// any other columns are fields
								phenotypicStorage.setFieldName(cellData);
								continue;
							}
								
							curPos += cellData.length() + 1;	//update progress
						}
					}
					subjectCount++;
					phenotypicStorage.commit();
				}
				catch (StorageIOException ex) {
					//log.info(ex);
				}
				// Debug only - Show progress and speed 
				//log.info("progress: " + twoPlaces.format(getProgress()) + " % | speed: " + twoPlaces.format(getSpeed()) + " KB/sec");
			}
		}
		catch (IOException ioe) {
			log.error("processFile IOException stacktrace:", ioe);
			throw new PhenotypicSystemException("Unexpected I/O exception whilst reading the phenotypic data");
		}
		catch (Exception ex) {
			log.error("processFile Exception stacktrace:", ex);
			throw new PhenotypicSystemException("Unexpected exception occurred when trying to process phenotypic data");			
		}
		finally {
			// Clean up the IO objects
			timer.stop();
			log.info("Total elapsed time: " + timer.getTime() + " ms or " + twoPlaces.format(timer.getTime()/1000.0) + " s");
			log.info("Total file size: " + srcLength + " B or " + twoPlaces.format(srcLength / 1024.0 / 1024.0 ) + " MB");
			if (timer != null)
				timer = null;
			if (csvRdr != null) {
				try {
					csvRdr.close();					
				}
				catch (Exception ex) {
					log.error("Cleanup operation failed: csvRdr.close()", ex);
				}
			}
			if (isr != null) {
				try {
					isr.close();
				}
				catch (Exception ex) {
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
	 * @return if a process is actively running, then progress in %; or
	 *         if no process running, then returns -1 
	 */	
	public double getProgress() {
		double progress = -1; 
		
		if (srcLength > 0)
			progress = curPos * 100.0 / srcLength;	// %
	
		return progress;
	}

	/**
	 * Return the speed of the current process in KB/s
	 * 
	 * @return if a process is actively running, then speed in KB/s; or
	 *         if no process running, then returns -1 
	 */
	public double getSpeed() {
		double speed = -1;
		
		if (srcLength > 0)
			speed = curPos / 1024 / (timer.getTime() / 1000.0);	// KB/s

		return speed;
	}
}
