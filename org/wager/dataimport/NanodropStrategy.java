package org.wager.dataimport;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.LinkedList;
import java.util.List;
import java.util.regex.Pattern;

import neuragenix.bio.utilities.BiospecimenUtilities;
import neuragenix.dao.DALSecurityQuery;

import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVStrategy;
import org.jasig.portal.services.LogService;
import org.wager.biogenix.exceptions.InvalidBiospecimenException;
import org.wager.biogenix.exceptions.InvalidDNAConcException;
import org.wager.biogenix.exceptions.InvalidDNAPurityException;
import org.wager.biogenix.exceptions.InvalidDNAVolException;
import org.wager.biogenix.exceptions.InvalidDataException;
import org.wager.biogenix.exceptions.InvalidFileFormatException;
import org.wager.biogenix.types.Biospecimen;

public class NanodropStrategy extends AbstractImportStrategy {

	static final int NANODROP_CONC_IDX = 4;
	static final int NANODROP_PURITY_IDX = 7;

	@Override
	public void importData(InputStream is, String recordSeparator) {
		// TODO Auto-generated method stub
		BufferedReader br = new BufferedReader(new InputStreamReader(is));
		try {
			int lineNo = 1;
			CSVStrategy strat;
String separator = ",";
			if (recordSeparator.equals(TAB_SEPARATOR)) {
				strat = CSVStrategy.TDF_STRATEGY;
				separator = "\t";
			}
			else 
				strat = CSVStrategy.DEFAULT_STRATEGY;
			
			String currentLine;
			String regex = "^Sample ID"+separator+"User ID"+separator+"Date"+separator+"Time"+separator+"ng/ul.+";
			 
			while ((currentLine = br.readLine()) != null) {
				LogService.log(LogService.DEBUG, lineNo+": "+currentLine);
				if (Pattern.matches(regex, currentLine)) {
					break;
				}
				lineNo++;
			}
			LogService.log(LogService.DEBUG, "Found Nanodrop data at line " + lineNo);
			if (currentLine == null) {
				throw new InvalidFileFormatException("The header line could not be found in the file. Is this a Nanodrop upload file?");
			}
			
			importedData = (new CSVParser(br, strat)).getAllValues();
			LogService.instance();
			LogService.log(LogService.DEBUG, "Length: " + importedData.length);

			LogService
					.log(LogService.DEBUG, "Width: " + importedData[0].length);
			
			if (importedData.length == 0)
				throw new InvalidFileFormatException("No data was found in the file");
			
			if (importedData[0].length < 8)
				throw new InvalidFileFormatException("Please check the file format and record separator used.");
			processRawData();
		} catch (IOException io) {
			errorXML
					.append("<error>There was an error reading the file</error>");
		}
	 catch (InvalidFileFormatException io) {
		 errorXML.append("<error>"+io.getMessage()+"</error>");
	 }
	}

	@Override
	public void process() {
		for (int i = 0; i < biospecUpdateList.size(); i++) {
			Biospecimen b = (Biospecimen) biospecUpdateList.get(i);
			int rowval = ((Integer) mapping.get(b.getBiospecimenid()))
					.intValue();
			b.setDnaconc(new Double(importedData[rowval][NANODROP_CONC_IDX]));
			b.setPurity(new Double(importedData[rowval][NANODROP_PURITY_IDX]));
			biospecDAO.updateBiospecimen(b);
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

				if (importedData[i][0] == null
						|| importedData[i][BIOSPEC_INDEX].equals(""))
					throw new InvalidBiospecimenException();

				double dnaconc = Double.valueOf(
						importedData[i][NANODROP_CONC_IDX]).doubleValue();
				if (dnaconc == 0
						|| Double.valueOf(importedData[i][NANODROP_CONC_IDX])
								.isNaN())
					throw new InvalidDNAConcException();
				double dnaPurity = Double.valueOf(
						importedData[i][NANODROP_PURITY_IDX]).doubleValue();
				if (dnaPurity == 0
						|| Double.valueOf(importedData[i][NANODROP_PURITY_IDX])
								.isNaN())
					throw new InvalidDNAPurityException();

				validDataRows.add(new Integer(i));

			} catch (NumberFormatException e) {
				LogService.log(LogService.DEBUG,
						"[Aliquot] Encountered an incorrect value for line: "
								+ i);
				errorXML.append("<error>" + "(line " + i + ") "
						+ importedData[i][0] + " has an invalid value</error>");
			} catch (InvalidBiospecimenException ibe) {
				LogService.log(LogService.DEBUG,
						"[Aliquot] Encountered an incorrect value for line: "
								+ i);
				errorXML.append("<error>" + "(line " + i + ") "
						+ importedData[i][0]
						+ " is not a valid biospecimen</error>");
			} catch (InvalidDNAConcException e) {
				LogService.log(LogService.DEBUG,
						"[Aliquot] Encountered an incorrect concentration value for line: "
								+ i);
				errorXML.append("<error>" + "(line " + i + ") "
						+ importedData[i][0]
						+ " has an invalid concentration value ("
						+ importedData[i][NANODROP_CONC_IDX] + ")</error>");
			} catch (InvalidDNAPurityException e) {
				LogService.log(LogService.DEBUG,
						"[Aliquot] Encountered an incorrect purity value for line: "
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
		String dupes[] = findDuplicates(ids);
		if (dupes.length > 0) {
			for (int i = 0; i < dupes.length; i++) {
				errorXML.append("<error>" + dupes[i]
						+ " has duplicate entries</error>");
			}
		} else {
			biospecUpdateList = biospecDAO.getUpdateList(ids);
			int count = biospecUpdateList.size();
			outputXML.append("<biocount>" + count + "</biocount>");
			for (int i = 0; i < biospecUpdateList.size(); i++) {
				Biospecimen b = (Biospecimen) biospecUpdateList.get(i);
				String purity = b.getPurity() == null ? "0" : "1";
				String dnaConc = b.getDnaconc() == null ? "0" : "1";
				outputXML.append("<biospecimen " + "id=\""
						+ b.getBiospecimenid() + "\" " + "><conc>" + dnaConc
						+ "</conc>" + "<purity>" + purity + "</purity>"
						+ "<rowid>" + mapping.get(b.getBiospecimenid())
						+ "</rowid>" + "</biospecimen>");
			}

		}
	}

	@Override
	public void verify() {
		this.clearOutputLog();
		for (int i = 0; i < biospecUpdateList.size(); i++) {
			Biospecimen b = (Biospecimen) biospecUpdateList.get(i);
			String strNewBiospecimenID = "";
			try {
				int studykey = (int) b.getStudykey().intValue();
				strNewBiospecimenID = BiospecimenUtilities
						.getNewSubBiospecimenStringID(new DALSecurityQuery(
								"biospecimen_add", authToken), b
								.getSampletype(), b.getBiospecimenid(), true,
								studykey);
			} catch (Exception e) {
			}

			int rowval = ((Integer) mapping.get(b.getBiospecimenid()))
					.intValue();

			outputXML.append("<biospecimen id=\"" + b.getBiospecimenid()
					+ "\" >" + "<oldconc>" + b.getDnaconc() + "</oldconc>"
					+ "<oldpurity>" + b.getPurity() + "</oldpurity>"
					+ "<newconc>" + importedData[rowval][4] + "</newconc>"
					+ "<newpurity>" + importedData[rowval][7] + "</newpurity>"
					+ "</biospecimen>");

		}
	}
}
