package org.wager.dataimport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import neuragenix.bio.utilities.BiospecimenUtilities;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.security.AuthToken;

import org.hibernate.Transaction;
import org.wager.biogenix.exceptions.InvalidBiospecimenException;
import org.wager.biogenix.exceptions.InvalidDNAVolException;
import org.wager.biogenix.types.BioTransactions;
import org.wager.biogenix.types.Biospecimen;
import org.wager.biogenix.types.Cell;

public class AliquotStrategy extends AbstractImportStrategy {
	StringBuffer errorXML;

	@Override
	public void sanityCheck() {
		// TODO Auto-generated method stub
		for (int i = 0; i < biospecUpdateList.size(); i++) {
			Biospecimen b = (Biospecimen) biospecUpdateList.get(i);
			int rowval = ((Integer) mapping.get(b.getBiospecimenid()))
					.intValue();
			double dnavol = Double.valueOf(importedData[rowval][ALIQUOT_DNACONC_INDEX])
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

	}
	
	public void createAliquot(Biospecimen b, Cell c, int rowval,AuthToken authToken) {
		Transaction tx = null;
		try {
			int studykey = (int) b.getStudykey();
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
			a.setStudykey(b.getStudykey());
			double stockVol = new Double(importedData[rowval][IDX_DNAVOL])
					.doubleValue();
			double stockConv = new Double(importedData[rowval][IDX_DNACONC])
					.doubleValue();
			double teVol = new Double(importedData[rowval][IDX_TEVOL])
					.doubleValue();
			double newConc = stockVol * stockConv / (stockVol + teVol);

			SimpleDateFormat sdf = new SimpleDateFormat("dd/mm/yyyy");
			Date sampleDate = sdf.parse(importedData[rowval][IDX_SAMPLEDATE]);
			parentbt.setTransactiondate(sampleDate);
			parentbt.setBiospecimenkey(b.getBiospecimenkey());
			parentbt.setAction("Delivered");
			parentbt.setReason("Sub-aliquot");
			parentbt.setQuantity(stockVol * -1);
			b.setQtyRemoved(new Double(b.getQtyRemoved().doubleValue()
					- stockVol));
			a.setQtyCollected(new Double(teVol + stockVol));

			a.setDnaconc(new Double(newConc));

			long biospecId = biospecDAO.saveNewBiospecimen(a);
			aliquotbt.setTransactiondate(sampleDate);
			aliquotbt.setBiospecimenkey(biospecId);
			aliquotbt.setAction("Available");
			aliquotbt.setReason("Initial Quantity");
			aliquotbt.setQuantity(stockVol + teVol);
			System.err.println("HIB: Save new biospecimen with id: "
					+ biospecId);
			
			biospecDAO.saveNewBiospecimen(a);
			biospecDAO.updateBiospecimen(b);
			biospecDAO.saveTransaction(aliquotbt);
			biospecDAO.saveTransaction(parentbt);
			tx.commit();
		} catch (RuntimeException e) {
			if (tx != null)
				tx.rollback();
			throw e; // or display error message
		} catch (Exception e) {

		}
	}
	
	
	private double calcConc(double stockconc, double stockvol, double tevol) {
		return stockconc * stockvol / (stockvol + tevol);
	}
	
	@Override
	protected List<Integer> verifyRawData() {
		List<Integer> validDataRows = new LinkedList<Integer>();
		for (int i = 0; i < importedData.length; i++) {
			try {

				if (importedData[i][0] == null || importedData[i][BIOSPEC_INDEX].equals(""))
					throw new InvalidBiospecimenException();

				double dnaconc = Double.valueOf(importedData[i][ALIQUOT_DNACONC_INDEX])
						.doubleValue();
				if (dnaconc == 0 || Double.valueOf(importedData[i][ALIQUOT_DNACONC_INDEX]).isNaN())
					throw new NumberFormatException();
				double dnavol = Double.valueOf(importedData[i][ALIQUOT_VOLUME_INDEX])
						.doubleValue();
				if (dnavol == 0 || Double.valueOf(importedData[i][ALIQUOT_VOLUME_INDEX]).isNaN())
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
		}
		return validDataRows;
	}

}
