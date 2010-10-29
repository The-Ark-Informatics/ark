package au.org.theark.geno.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DecimalFormat;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;

import com.csvreader.CsvReader;

import au.org.theark.geno.exception.DataAcceptorIOException;
import au.org.theark.geno.exception.FileFormatException;
import au.org.theark.geno.exception.GenoSystemException;


/**
 * GWASImport provides support for importing PLINK compatible
 * Ped and Map files.  It features state-machine behaviour to 
 * allow an external class to deal with how to store the data
 * pulled out of the files.
 *
 * @author elam
 */
public class PedMapImport {
	
	private char minorAllele;
	private char majorAllele;
	private char basisAllele;
	private int basisCount;
	private String markerName;
	private long markerPosition;
	private int markerChromosome;
	
	//private double progress;
	private long subjectCount;
	private long markerCount;
	private double speed;
	private long curPos;
	private long srcLength = -1;	// -1 means nothing being processed
	private StopWatch timer = null;
	private char mapDelimChr = '\t';	//default map file delimiter: TAB
	private char pedDelimChr = ' ';		//default ped file delimiter: SPACE
	
	private IMapDataAcceptor mapPipe = null;
	private IPedDataAcceptor pedStorage = null;
	
	static Logger log = LoggerFactory.getLogger(PedMapImport.class);
	
	public void setMapStorage(IMapDataAcceptor mapStorage) {
		this.mapPipe = mapStorage;
	}

	public IMapDataAcceptor getMapStorage() {
		return mapPipe;
	}
	
	public void setPedStorage(IPedDataAcceptor pedStorage) {
		this.pedStorage = pedStorage;
	}

	public IPedDataAcceptor getPedStorage() {
		return pedStorage;
	}
	
	/**
	 * 
	 * @param mapStore is required for defining where the marker data is to go
	 * @param pedStore is required for 
	 */
	public PedMapImport(IMapDataAcceptor aMapStore, IPedDataAcceptor aPedStore) {
		setMapStorage(aMapStore);
		setPedStorage(aPedStore);
	}
	
	/**
	 * Imports the PLINK compatible Map file.
	 * Default Map file format assumed (i.e. 4 columns) as per:
	 *  http://pngu.mgh.harvard.edu/~purcell/plink/data.shtml#map
	 * 
	 * @param mapInputStream is the input stream of a map file
	 * @throws IOException
	 * @throws OutOfMemoryError
	 */
	public void processMap(InputStream mapInStream, long inLength) throws FileFormatException, GenoSystemException {
		if (mapPipe == null) {
			throw new GenoSystemException("Aborting: Must have a Map Data Acceptor object defined before calling.");
		}
		
		//File file = new File(mapFilePath);
		curPos = 0;

		InputStreamReader isr = null;
		CsvReader csvRdr = null;
		
		DecimalFormat twoPlaces = new DecimalFormat("0.00");

		try {
			isr = new InputStreamReader(mapInStream);
			csvRdr = new CsvReader(isr, mapDelimChr);
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
					mapPipe.init();
					if (newLine.length < 3) {
						// non-compliant map file
						throw new FileFormatException("The specified file does not appear to conform to the Map file format.");
					}
					else
					{
						for (int i = 0; i < newLine.length; i++) {
							String cellData = newLine[i];
							/* Debug only - Show data
							if (i < newLine.length - 1)
								log.debug(cellData + ",");
							else
								log.debug(cellData);
							*/
							switch (i) {
							case 0:
								// first column should be the chromosome
								mapPipe.setChromosome(cellData);
								break;
							case 1:
								// second column should be the marker id
								mapPipe.setMarkerName(cellData);
								break;
							case 2:
								// third column should be the genetic distance (morgans)
								mapPipe.setGeneDist(Long.parseLong(cellData));
								break;
							case 3:
								// fourth column should be the base-pair position (bp units)
								mapPipe.setBpPos(Long.parseLong(cellData));
								break;
		
							default:
								// ignore any additional columns
								log.debug("Warning: more than expected number of columns for a map file");
								continue;
							}
								
							curPos += cellData.length() + 1;	//update progress
						}
					}
					markerCount++;
					mapPipe.sync();
				}
				catch (DataAcceptorIOException ex) {
					log.error("Failure during call to data acceptor: ", ex);
				}
				// Debug only - Show progress and speed 
				//log.debug("progress: " + twoPlaces.format(getProgress()) + " % | speed: " + twoPlaces.format(getSpeed()) + " KB/sec");
			}
		}
		catch (IOException ioe) {
			log.error("processMap IOException stacktrace:", ioe);
			throw new GenoSystemException("Unexpected I/O exception whilst reading the map data");
		}
		catch (Exception ex) {
			log.error("processMap Exception stacktrace:", ex);
			throw new GenoSystemException("Unexpected exception occurred when trying to process map data");			
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
	 * Imports the PLINK compatible Ped file.
	 * Default Ped file format assumed (i.e. sixth column for phenotype 
	 *  and subsequent columns for alleles) as per:
	 *  http://pngu.mgh.harvard.edu/~purcell/plink/data.shtml#ped
	 * 
	 * @param pedInStream is the input stream of a ped file
	 * @throws IOException
	 * @throws OutOfMemoryError
	 */
	public void processPed(InputStream pedInStream, long inLength) throws FileFormatException, GenoSystemException {

		if (pedStorage == null) {
			throw new NullPointerException("Must have a Ped Data Acceptor object defined before calling.");
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
