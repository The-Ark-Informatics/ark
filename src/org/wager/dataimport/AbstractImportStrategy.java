package org.wager.dataimport;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

import neuragenix.security.AuthToken;

import org.wager.biogenix.types.Biospecimen;
import org.wager.dataimport.dao.BiospecimenDAO;

public abstract class AbstractImportStrategy implements IDataImportStrategy {
	String[][] importedData;
	StringBuffer errorXML = new StringBuffer();
	StringBuffer outputXML = new StringBuffer();
	AuthToken authToken;
	HashMap<String,Integer> mapping;
	public static final int BIOSPEC_INDEX = 0;
	
	public static final String TAB_SEPARATOR = "tab";
	public static final String COMMA_SEPARATOR = "comma";

	
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
	
	public String getOutput() {
		return outputXML.toString() + errorXML.toString();
	}
	protected void clearOutputLog() {
		outputXML = new StringBuffer();
	}
	
	protected void clearErrorLog() {
		errorXML = new StringBuffer();
	}
	
	@Override
	public abstract void process();

	
	protected abstract List<Integer> verifyRawData();
	
	
	
	
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
	
	
	public void setAuthToken(AuthToken auth) {
		this.authToken= auth;
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
	
	public abstract void importData(InputStream is, String recordSeparator) ;
	
	
	
		
	
	

	
	
}
