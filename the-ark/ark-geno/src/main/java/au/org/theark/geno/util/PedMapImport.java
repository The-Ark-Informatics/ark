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
package au.org.theark.geno.util;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import org.apache.commons.lang.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import au.org.theark.geno.exception.DataAcceptorIOException;
import au.org.theark.geno.exception.FileFormatException;
import au.org.theark.geno.exception.GenoSystemException;

import com.csvreader.CsvReader;


/**
 * GWASImport provides support for importing PLINK compatible
 * Ped and Map files.  It features state-machine behaviour to 
 * allow an external class to deal with how to store the data
 * pulled out of the files.
 *
 * @author elam
 */
public class PedMapImport {
	
	private long subjectCount;
	private int markerCount;	// assumes #(markers) < (2^31)-1 (about 2e09);
	private long curPos;
	private long srcLength = -1;	// -1 means nothing being processed

	private char mapDelimChr = '\t';	//default map file delimiter: TAB
	private char pedDelimChr = ' ';		//default ped file delimiter: SPACE
	
	private StopWatch timer = null;
	
	private ArrayList<String> markerIdList = null;
	
	private IMapDataAcceptor mapPipe = null;
	private IPedDataAcceptor pedPipe = null;
	
	static Logger log = LoggerFactory.getLogger(PedMapImport.class);
	
	/**
	 * Get the Map Data Acceptor object
	 */
	public IMapDataAcceptor getMapStorage() {
		return mapPipe;
	}
	
	/**
	 * Set the Map Data Acceptor object
	 * @param mapStorage
	 */
	public void setMapStorage(IMapDataAcceptor mapStorage) {
		this.mapPipe = mapStorage;
	}

	/**
	 * Get the Ped Data Acceptor object
	 */
	public IPedDataAcceptor getPedStorage() {
		return pedPipe;
	}
	
	/**
	 * Set the Ped Data Acceptor object
	 * @param mapStorage
	 */
	public void setPedStorage(IPedDataAcceptor pedStorage) {
		this.pedPipe = pedStorage;
	}

	public char getMapDelimChr() {
		return mapDelimChr;
	}

	public void setMapDelimChr(char mapDelimChr) {
		this.mapDelimChr = mapDelimChr;
	}

	public char getPedDelimChr() {
		return pedDelimChr;
	}

	public void setPedDelimChr(char pedDelimChr) {
		this.pedDelimChr = pedDelimChr;
	}

	/**
	 * Default constructor
	 * @param mapStore is required for defining where the marker data is to go
	 * @param pedStore is required for defining where the pedigree data is to go
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

		// Reset counters/indices
		curPos = 0;
		markerCount = 0;
		// see ArrayList memory pre-allocation code further below
		markerIdList = new ArrayList<String>();

		InputStreamReader isr = null;
		CsvReader csvRdr = null;

		// Map import assumptions:
		// - all markers must have unique Ids
		// - it is possible for some markers to have the same position
		//   (e.g. markers are yet to be merged)
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
					if (newLine.length < 3) {
						// non-compliant Map file
						throw new FileFormatException("The specified file does not appear to conform to the Map file format.");
					}
					else
					{
						mapPipe.init();
						// ArrayList memory pre-allocation after reading 1st line of file
						if (markerCount == 1) {
							// (over-)estimate the number of markers
							int minCapacity = (int) (1.5 * srcLength / curPos);
							markerIdList.ensureCapacity(minCapacity);
						}

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
								markerIdList.add(cellData);
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
						markerCount++;
						mapPipe.sync();
					}
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
			log.info("Total elapsed time: " + timer.getTime() + " ms or " + au.org.theark.geno.service.Constants.TWO_DECPLACES.format(timer.getTime()/1000.0) + " s");
			log.info("Total file size: " + srcLength + " B or " + au.org.theark.geno.service.Constants.TWO_DECPLACES.format(srcLength / 1024.0 / 1024.0 ) + " MB");
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

		if (pedPipe == null) {
			throw new NullPointerException("Must have a Ped Data Acceptor object defined before calling.");
		}
		// Reset counters/indices
		curPos = 0;
		subjectCount = 0;
		int markerIdx = 0;

		InputStreamReader isr = null;
		CsvReader csvRdr = null;
		
		boolean firstInPair = true;
		// Ped import assumptions:
		// - all bi-allelic genotypes are separated by the default delimiter.
		// - if for some reason the marker Id re-occurs in the list, then
		//   the the genotype for the last occurrence will apply.
		try {
			isr = new InputStreamReader(pedInStream);
			csvRdr = new CsvReader(isr, pedDelimChr);
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
					if (newLine.length < 6) {
						// non-compliant Ped file
						throw new FileFormatException("The specified file does not appear to conform to the Ped file format.");
					}
					else
					{
						markerIdx = 0;
						pedPipe.init();

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
								// first column should be the family id
								pedPipe.setFamilyId(cellData);
								break;
							case 1:
								// second column should be the individual id
								pedPipe.setIndivId(cellData);
								break;
							case 2:
								// third column should be the paternal (father) id
								pedPipe.setFatherId(cellData);
								break;
							case 3:
								// fourth column should be the maternal (mother) id
								pedPipe.setMotherId(cellData);
								break;
							case 4:
								// fifth column should be the sex (gender)
								pedPipe.setGender(cellData);
								break;								
							case 5:
								// sixth column should be a phenotype status 
								pedPipe.setPhenotype(cellData);
								break;
								
							default:
								// additional columns should correspond to the genotyped pair of   
								// alleles per marker (assumes human studies, i.e. bi-allelic)
								pedPipe.setMarkerName(markerIdList.get(markerIdx));
								if (firstInPair) {
									pedPipe.setAllele1(cellData);
								}
								else {
									pedPipe.setAllele2(cellData);
									// keep track of which marker we are up to
									markerIdx++;
								}
								// toggle variable to prepare for accepting the next allele in the pair
								firstInPair = !firstInPair;
							}

							curPos += cellData.length() + 1;	//update progress
						}
						subjectCount++;
						pedPipe.sync();
					}
				}
				catch (DataAcceptorIOException ex) {
					log.error("Failure during call to data acceptor: ", ex);
				}
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
			log.info("Total elapsed time: " + timer.getTime() + " ms or " + au.org.theark.geno.service.Constants.TWO_DECPLACES.format(timer.getTime()/1000.0) + " s");
			log.info("Total file size: " + srcLength + " B or " + au.org.theark.geno.service.Constants.TWO_DECPLACES.format(srcLength / 1024.0 / 1024.0 ) + " MB");
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
