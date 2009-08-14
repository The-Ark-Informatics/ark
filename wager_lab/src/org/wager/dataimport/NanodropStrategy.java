package org.wager.dataimport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.wager.biogenix.exceptions.InvalidBiospecimenException;
import org.wager.biogenix.exceptions.InvalidDNAConcException;
import org.wager.biogenix.exceptions.InvalidDNAPurityException;
import org.wager.biogenix.exceptions.InvalidDNAVolException;
import org.wager.biogenix.exceptions.InvalidDataException;
import org.wager.biogenix.types.Biospecimen;

public class NanodropStrategy extends AbstractImportStrategy {

	static final int NANODROP_CONC_IDX = 4;
	static final int NANODROP_PURITY_IDX = 7;
	
	@Override
	public void importData(InputStream is) {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader( new InputStreamReader(is));
       try {
    	   for (int i=0; i < 5; i++) { // Burn off five lines
    	 		br.readLine();
    	 		} //Burn off the first header line.
        CSVStrategy strat;
        	strat = CSVStrategy.DEFAULT_STRATEGY;
        
		importedData = (new CSVParser(br,strat)).getAllValues();
		System.out.println("importedData is " + importedData.length);
		
		processRawData();
       }
       catch(IOException io) {
    	   errorXML.append("<error>There was an error reading the file</error>");
       }
	}

	

	@Override
	public void process() {
		for (int i = 0; i < biospecUpdateList.size(); i++) {
			Biospecimen b = (Biospecimen) biospecUpdateList.get(i);
			int rowval = ((Integer) mapping.get( b.getBiospecimenid())).intValue();
			 b.setDnaconc(new Double(importedData[rowval][NANODROP_CONC_IDX]));
			    b.setPurity(new Double(importedData[rowval][NANODROP_PURITY_IDX])); 
		}
	}


	protected void sanityCheck() {
		// TODO Auto-generated method stub

	}
	
	@Override
	protected List<Integer> verifyRawData() {
		List<Integer> validDataRows = new LinkedList<Integer>();
		for (int i = 0; i < importedData.length; i++) {
			try {
				
				if (importedData[i][0] == null || importedData[i][BIOSPEC_INDEX].equals(""))
					throw new InvalidBiospecimenException();

				double dnaconc = Double.valueOf(importedData[i][NANODROP_CONC_IDX])
						.doubleValue();
				if (dnaconc == 0 || Double.valueOf(importedData[i][NANODROP_CONC_IDX]).isNaN())
					throw new InvalidDNAConcException();
				double dnaPurity = Double.valueOf(importedData[i][NANODROP_PURITY_IDX])
						.doubleValue();
				if (dnaPurity == 0 || Double.valueOf(importedData[i][NANODROP_PURITY_IDX]).isNaN())
					throw new InvalidDNAPurityException();
				
				validDataRows.add(new Integer(i));

			} catch (NumberFormatException e) {
				System.err
						.println("[Aliquot] Encountered an incorrect value for line: "
								+ i);
				errorXML.append("<error>" + "(line " + i + ") "
						+ importedData[i][0] + " has an invalid value</error>");
			} catch (InvalidBiospecimenException ibe) {
				System.err
						.println("[Aliquot] Encountered an incorrect value for line: "
								+ i);
				errorXML.append("<error>" + "(line " + i + ") "
						+ importedData[i][0]
						+ " is not a valid biospecimen</error>");
			} catch (InvalidDNAConcException e) {
				System.err
						.println("[Aliquot] Encountered an incorrect concentration value for line: "
								+ i);
				errorXML.append("<error>" + "(line " + i + ") "
						+ importedData[i][0]
						+ " has an invalid concentration value ("
						+ importedData[i][NANODROP_CONC_IDX] + ")</error>");
			}
		 catch (InvalidDNAPurityException e) {
			System.err
					.println("[Aliquot] Encountered an incorrect purity value for line: "
							+ i);
			errorXML.append("<error>" + "(line " + i + ") "
					+ importedData[i][0]
					+ " has an invalid concentration value ("
					+ importedData[i][NANODROP_CONC_IDX] + ")</error>");
		}
		
		}
		return validDataRows;
	}
	
	protected void processRawData() {
		
		String[] ids = getRecordIDs();
		String dupes [] = findDuplicates(ids);
		if (dupes.length > 0) {
			for (int i=0; i< dupes.length; i++) {
				errorXML.append("<error>"+ dupes[i] + " has duplicate entries</error>");
			}
		} else { 
			biospecUpdateList = biospecDAO.getUpdateList(ids);
			int count = biospecUpdateList.size();
			outputXML.append("<biocount>"+count+"</biocount>");
			
		}
	}

	@Override
	public void verify() {
		// TODO Auto-generated method stub

	}

}
