package org.wager.dataimport;

import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.TreeSet;

import org.wager.biogenix.exceptions.InvalidBiospecimenException;
import org.wager.biogenix.exceptions.InvalidDNAVolException;
import org.wager.biogenix.exceptions.TEBufferVolException;
import org.wager.biogenix.types.Biospecimen;
import org.wager.dataimport.dao.BiospecimenDAO;

public abstract class AbstractImportStrategy implements IDataImportStrategy {
	String[][] importedData;
	StringBuffer errorXML;
	StringBuffer outputXML;
	HashMap<String,Integer> mapping;
	public static final int BIOSPEC_INDEX = 0;
	public static final int ALIQUOT_DNACONC_INDEX = 3;
	public static final int ALIQUOT_VOLUME_INDEX = 4;
	
	BiospecimenDAO biospecDAO = new BiospecimenDAO();
	List<Biospecimen> biospecUpdateList;
	@Override
	public String getErrorXML() {
		return errorXML.toString();
	}
	@Override
	public String getOutputXML() {
		return outputXML.toString();
	}
	
	@Override
	public abstract void process();

	
	protected abstract List<Integer> verifyRawData();
	
	
	protected abstract void sanityCheck();
	
	public String[] getRecordIDs() {
		List<Integer> validDataRows = verifyRawData();
		int validRowNo = validDataRows.size();
		String ids[] = new String[validRowNo];
		this.mapping = new HashMap<String,Integer>(validRowNo);
		for (int i =0; i< validRowNo; i++) {
			Integer ix = (Integer) validDataRows.get(i);
			mapping.put(importedData[ix.intValue()][BIOSPEC_INDEX], ix);
			ids[i] = importedData[ix.intValue()][BIOSPEC_INDEX];
		}
		return ids;
	}
	
	
	
	protected String[] findDuplicates(String [] s) {
		TreeSet<String> set = new TreeSet<String>();
		
		Arrays.sort(s);
		
		for (int j=0; j < s.length-1; j++) {
			if (s[j].equals(s[j+1])) {
				set.add(s[j]);
				
			}
		}
		Object [] oa = set.toArray();
		String [] sa = new String[oa.length];
		for (int i =0 ; i < oa.length; i++) {
			sa[i] = (String) oa[i];
		}
		return sa;
	}
	
	public void importData(String[][] importData) {
		this.importedData = importData;
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
	

	
	
}
