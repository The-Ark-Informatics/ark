package au.org.theark.gdmi.util;
/**
 * GWASImport shall support reading PED+MAP file format
 * and decoding the information into the relevant fields
 * to be updated into the database tables.
 * 
 * The main method process(..) is thread-safe.
 *    
 * @author elam
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.Arrays;

import org.apache.commons.lang.time.StopWatch;

import au.com.bytecode.opencsv.CSVReader;

/**
 * GWASImport provides support for importing PLINK compatible
 * ped and map files.  It features state-machine behaviour to 
 * allow an external class to deal with how to store the data
 * pulled out of the files.
 *
 * @author elam
 */
public class GWASImport {
	
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
	private long fileSize = -1;	// -1 means nothing being processed
	private StopWatch timer = null;
	private char mapDelimChr = '\t';	//default map file delimiter: TAB
	private char pedDelimChr = ' ';		//default ped file delimiter: SPACE
	
	private IMapStorage mapStorage = null;
	private IPedStorage pedStorage = null;
	
	/**
	 * 
	 * @param mapStore is required for defining where the marker data is to go
	 * @param pedStore is required for 
	 */
	public GWASImport(IMapStorage aMkrStore, IPedStorage aPedStore) {
		
		if (aMkrStore == null) {
			throw new NullPointerException("Aborting: Must have a map storage object defined before calling.");
		}
		mapStorage = aMkrStore;
		
		if (aPedStore == null) {
			throw new NullPointerException("Aborting: Must have a ped storage object defined before calling.");
		}
		pedStorage = aPedStore;
	}
	
	/**
	 * Imports the PLINK compatible map file.
	 * Default map file format assumed (i.e. 4 columns) as per:
	 *  http://pngu.mgh.harvard.edu/~purcell/plink/data.shtml#map
	 * 
	 * @param mapFilePath is the file path to the map file
	 * @throws IOException
	 */
	public void processMap(String mapFilePath) throws IOException, OutOfMemoryError {
			
		File file = new File(mapFilePath);
		curPos = 0;

		FileReader fr = null;
		CSVReader csvRdr = null;
		
		DecimalFormat twoPlaces = new DecimalFormat("0.00");

		try {
			fr = new FileReader(file);
			csvRdr = new CSVReader(fr, mapDelimChr);
			String[] newLine;
			
			fileSize = file.length();
			if (fileSize <= 0) {
				System.out.println("ERROR: The file size is less than or equal to 0: " + fileSize);
				return;
			}

			timer = new StopWatch();
			timer.start();
			
			while((newLine = csvRdr.readNext()) != null) {
				// do something with the newline to put the data into
				// the variables defined above
				try {
					mapStorage.init();
					if (newLine.length < 4) {
						// non-compliant map file
						System.out.println("This record was skipped: " + Arrays.toString(newLine));
					}
					else
					{
						for (int i = 0; i < newLine.length; i++) {
							String cellData = newLine[i];
							/* Debug only - Show data
							if (i < newLine.length - 1)
								System.out.print(cellData + ",");
							else
								System.out.println(cellData);
							*/
							switch (i) {
							case 0:
								// first column should be the chromosome number
								long chromoNum = -1;
								if (cellData.trim().toUpperCase().equals("X"))
									chromoNum = 23;
								else if (cellData.trim().toUpperCase().equals("Y"))
									chromoNum = 24;
								else if (cellData.trim().toUpperCase().equals("XY"))
									chromoNum = 25;
								else if (cellData.trim().toUpperCase().equals("MT"))
									chromoNum = 26;
								else
									chromoNum = Long.getLong(cellData);
								mapStorage.setChromoNum(chromoNum);
								break;
							case 1:
								// second column should be the marker id
								mapStorage.setMarkerName(cellData);
								break;
							case 2:
								// third column should be the genetic distance (morgans)
								mapStorage.setGeneDist(Long.getLong(cellData));
								break;
							case 3:
								// fourth column should be the base-pair position (bp units)
								mapStorage.setBpPos(Long.getLong(cellData));
								break;
		
							default:
								// ignore any additional columns
								break;
							}
								
							curPos += cellData.length() + 1;	//update progress
						}
					}
					mapStorage.commit();
				}
				catch (Exception ex) {
					System.out.println(ex);
				}
				// Debug only - Show progress and speed 
				//System.out.println("progress: " + twoPlaces.format(getProgress()) + " % | speed: " + twoPlaces.format(getSpeed()) + " KB/sec");
			}
		}
		/*
		 * Disabled local error handling because it shouldn't be necessary.
		 * Only re-enable if there is anything special that needs to be done 
		 * before the exception is thrown up to the previous level
		 */
//		catch (IOException ioe) {
//			// Throw the exception up to the previous level
//			throw ioe;
//		}
		finally {
			// Clean up the IO objects
			timer.stop();
			System.out.println("Total elapsed time: " + timer.getTime() + " ms or " + twoPlaces.format(timer.getTime()/1000.0) + " s");
			System.out.println("Total file size: " + fileSize + " B or " + twoPlaces.format(fileSize / 1024.0 / 1024.0 ) + " MB");
			if (timer != null)
				timer = null;
			if (csvRdr != null)
				csvRdr.close();
			if (fr != null)
				fr.close();
			// Restore the state of variables
			fileSize = -1;
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
		
		if (fileSize > 0)
			progress = curPos * 100.0 / fileSize;	// %
	
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
		
		if (fileSize > 0)
			speed = curPos / 1024 / (timer.getTime() / 1000.0);	// KB/s

		return speed;
	}
}
