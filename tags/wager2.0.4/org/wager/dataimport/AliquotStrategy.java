package org.wager.dataimport;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import neuragenix.bio.utilities.BiospecimenUtilities;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.security.AuthToken;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.hibernate.Transaction;
import org.wager.biogenix.exceptions.InvalidBiospecimenException;
import org.wager.biogenix.exceptions.InvalidDNAVolException;
import org.wager.biogenix.exceptions.InvalidDataException;
import org.wager.biogenix.types.BioTransactions;
import org.wager.biogenix.types.Biospecimen;
import org.wager.biogenix.types.Cell;

import java.io.IOException;

public class AliquotStrategy extends AbstractImportStrategy {
	 
	
		static final int STOCK_VOLUME_IDX = 1;
		static final int ALIQUOT_DNACONC_IDX = 3;
		static final int ALIQUOT_DATE_IDX=5;
		static final int ALIQUOT_VOLUME_IDX = 6;
	  

	
	public void sanityCheck() {
		// TODO Auto-generated method stub
		for (int i = 0; i < biospecUpdateList.size(); i++) {
			Biospecimen b = (Biospecimen) biospecUpdateList.get(i);
			int rowval = ((Integer) mapping.get(b.getBiospecimenid()))
					.intValue();
			double dnavol = Double.valueOf(importedData[rowval][ALIQUOT_DNACONC_IDX])
					.doubleValue();
			double biodnavol = b.getQtyCollected().doubleValue()
					+ b.getQtyRemoved().doubleValue();
			if (dnavol > biodnavol) {
				errorXML.append("<error>" + b.getBiospecimenid()
						+ " has insufficient DNA volume.</error>");
			}
		}
	}
	@Override
	public void process() {
		for (int i = 0; i < biospecUpdateList.size(); i++) {
			Biospecimen b = (Biospecimen) biospecUpdateList.get(i);
			int rowval = ((Integer) mapping.get( b.getBiospecimenid())).intValue();
				createAliquot(b,null,rowval);
		}
	}
	
	
	@Override 
	public void verify() {
		clearOutputLog();
		for (int i = 0; i < biospecUpdateList.size(); i++) {
			Biospecimen b = (Biospecimen) biospecUpdateList.get(i);
			
			
			int rowval = ((Integer) mapping.get( b.getBiospecimenid())).intValue();
			
			
				double stockVol = new Double(importedData[rowval][STOCK_VOLUME_IDX]).doubleValue();
				double newVol = new Double(importedData[rowval][ALIQUOT_VOLUME_IDX]).doubleValue();
			    double newConc = new Double(importedData[rowval][ALIQUOT_DNACONC_IDX]).doubleValue();
				outputXML.append("<biospecimen id=\"" + b.getBiospecimenid() + "\" >" 
						+ "<stockvol>"  +  stockVol + "</stockvol>"
					     + "<aliquotvol>" + newVol + "</aliquotvol>"
					     + "<aliquotconc>" + newConc + "</aliquotconc>"
					     + "</biospecimen>");
				
			}
		}
		
		
		
	
	
	public void createAliquot(Biospecimen b, Cell c, int rowval) {
		
			int studykey = b.getStudykey().intValue();
			try {
			String strNewBiospecimenID = BiospecimenUtilities
					.getNewSubBiospecimenStringID(new DALSecurityQuery(
							"biospecimen_add", authToken), b.getSampletype(), b
							.getBiospecimenid(), true, studykey);
			Biospecimen a = new Biospecimen();
			BioTransactions parentbt = new BioTransactions();
			BioTransactions aliquotbt = new BioTransactions();
			a.setBiospecimenid(strNewBiospecimenID);
			a.setSampletype(b.getSampletype());
			a.setParentid(b.getBiospecimenid());
			a.setParentkey(b.getBiospecimenkey());
			a.setDepth(b.getDepth() + 1);
			a.setPatientkey(b.getPatientkey());
			a.setCellkey(new Long(-1)); 
			a.setStudykey(b.getStudykey());
			double stockVol = new Double(importedData[rowval][STOCK_VOLUME_IDX]).doubleValue();
			double newVol = new Double(importedData[rowval][ALIQUOT_VOLUME_IDX]).doubleValue();
		    double newConc = new Double(importedData[rowval][ALIQUOT_DNACONC_IDX]).doubleValue();

			SimpleDateFormat sdf = new SimpleDateFormat("ddmmyyyy");
			Date sampleDate = sdf.parse(importedData[rowval][ALIQUOT_DATE_IDX]);
			parentbt.setTransactiondate(sampleDate);
			parentbt.setBiospecimenkey(b.getBiospecimenkey());
			parentbt.setAction("Delivered");
			parentbt.setReason("Sub-aliquot");
			parentbt.setQuantity(stockVol * -1);
			b.setQtyRemoved(new Double(b.getQtyRemoved().doubleValue()
					- stockVol));
			a.setQtyCollected(new Double(newVol));

			a.setDnaconc(new Double(newConc));

			long biospecId = biospecDAO.saveNewBiospecimen(a);
			aliquotbt.setTransactiondate(sampleDate);
			aliquotbt.setBiospecimenkey(biospecId);
			aliquotbt.setAction("Available");
			aliquotbt.setReason("Initial Quantity");
			aliquotbt.setQuantity(newVol);
			System.err.println("HIB: Save new biospecimen with id: "
					+ biospecId);
			
			biospecDAO.saveNewBiospecimen(a);
			biospecDAO.updateBiospecimen(b);
			biospecDAO.saveTransaction(aliquotbt);
			biospecDAO.saveTransaction(parentbt);
			}
			catch(Exception e) {
				e.printStackTrace();
			}
	
	}
	
	
	public double calcConc(double stockconc, double stockvol, double tevol) {
		return stockconc * stockvol / (stockvol + tevol);
	}
	
	@Override
	protected List<Integer> verifyRawData() {
		List<Integer> validDataRows = new LinkedList<Integer>();
		for (int i = 0; i < importedData.length; i++) {
			try {
				if( importedData[i].length != 13) {
					throw new InvalidDataException();
				}
				if (importedData[i][0] == null || importedData[i][BIOSPEC_INDEX].equals(""))
					throw new InvalidBiospecimenException();

				double dnaconc = Double.valueOf(importedData[i][ALIQUOT_DNACONC_IDX])
						.doubleValue();
				if (dnaconc == 0 || Double.valueOf(importedData[i][ALIQUOT_DNACONC_IDX]).isNaN())
					throw new NumberFormatException();
				double dnavol = Double.valueOf(importedData[i][ALIQUOT_VOLUME_IDX])
						.doubleValue();
				if (dnavol == 0 || Double.valueOf(importedData[i][ALIQUOT_VOLUME_IDX]).isNaN())
					throw new InvalidDNAVolException();
				double parentvol = Double.valueOf(importedData[i][STOCK_VOLUME_IDX])
				.doubleValue();
		if (parentvol == 0 || Double.valueOf(importedData[i][STOCK_VOLUME_IDX]).isNaN())
			throw new InvalidDNAVolException();
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
			} catch (InvalidDNAVolException e) {
				System.err
						.println("[Aliquot] Encountered an incorrect volume value for line: "
								+ i);
				errorXML.append("<error>" + "(line " + i + ") "
						+ importedData[i][0]
						+ " has an invalid volume value ("
						+ importedData[i][4] + ")</error>");
			}
			 catch (InvalidDataException e) {
					System.err
							.println("[Aliquot] Line "
									+ i + " has incorrect number of fields");
					errorXML.append("<error>" + "(line " + i + ") "
							+ " has an incorrect number of fields</error>");
				}
		}
		return validDataRows;
	}
	@Override
	public void importData(InputStream is, String recordSeparator)  {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader( new InputStreamReader(is));
       try {
        	br.readLine(); //Burn off the first header line.
        CSVStrategy strat;
        if (recordSeparator.equals(TAB_SEPARATOR))
        	strat = CSVStrategy.TDF_STRATEGY;
        else
        	strat = CSVStrategy.DEFAULT_STRATEGY;
        
		importedData = (new CSVParser(br,strat)).getAllValues();
		System.out.println("importedData is " + importedData.length);
		
		processRawData();
       }
       catch(IOException io) {
    	   errorXML.append("<error>There was an error reading the file</error>");
       }
	}
	
	protected void processRawData() {
		
		String[] ids = getRecordIDs();
 
			biospecUpdateList = biospecDAO.getUpdateList(ids);
			int count = biospecUpdateList.size();
			outputXML.append("<biocount>"+count+"</biocount>");
		
	}

}
