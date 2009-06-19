/*
 * BiospecimenUtilities.java
 *
 * Created on 16 March 2005, 11:13
 */

package neuragenix.bio.utilities;

/**
 *
 * @author  dmurley
 */

import neuragenix.dao.*;
import neuragenix.common.*;

import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import java.lang.StringBuffer;
import neuragenix.security.AuthToken;

import java.util.Hashtable;
import java.util.Vector;

import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;

public class BiospecimenUtilities  { 
    
    /** Creates a new instance of BiospecimenUtilities */
    public BiospecimenUtilities() {
    }
    
    public static final int SPECIMEN_NOT_FOUND = -1;
    public static final int BIOSPECIMENID = 0;
    public static final int BARCODE = 1;
    public static final int VIAL_CALCULATION_NOT_FOUND = -1;
    
    public static final int DOMAIN_BIOSPECIMEN = 0;
    
    public static String padLeft(String str, int fullLength, char ch){
    	return (fullLength>str.length())?buildString(ch,fullLength-str.length()).concat(str):str;
    }

    public static String buildString(char ch, int length){
    	char newStr[] = new char[length];
    	for(int i=0;i<length;++i) newStr[i]=ch;
    	return new String(newStr);
    } 
    
    
    public static String getBiospecimenDetails(String intBiospecimentID, AuthToken authToken) 
    {
        StringBuffer sbBiospecimenDetails = new StringBuffer();
        try 
        {
            if (authToken.hasActivity ("biospecimen_view"))
            {
                Vector formfields = DatabaseSchema.getFormFields( "view_biospecimen" );
                sbBiospecimenDetails.append(QueryChannel.buildFormLabelXMLFile(formfields));
                sbBiospecimenDetails.append(QueryChannel.buildViewFromKeyXMLFile(formfields,"BIOSPECIMEN_intBiospecimenID",intBiospecimentID));
            }
            
            return sbBiospecimenDetails.toString();
        }
        catch(Exception e) {
            e.printStackTrace();
            return sbBiospecimenDetails.toString();
        }
    }

    public static Vector getBarcodeFormat(String strAdmissionID, int intPatientID) {
    	try {
    		 Vector formfields = DatabaseSchema.getFormFields( "cbiospecimen_barcode_format" );
    	DALQuery query = new DALQuery();
    	query.setDomain("BCFORMAT", null,null,null);
    	query.setDomain("ADMISSIONS", "ADMISSIONS_intSubStudyID", "BCFORMAT_intSubStudyKey", "INNER JOIN");
    	query.setWhere(null,0,"ADMISSIONS_intPatientID","=",""+intPatientID,0,DALQuery.WHERE_HAS_VALUE);
    	query.setWhere("AND",0,"ADMISSIONS_strAdmissionID","=",strAdmissionID,0,DALQuery.WHERE_HAS_VALUE);
    	query.setWhere("AND",0,"ADMISSIONS_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
    	query.setFields(formfields, null);
    	query.setOrderBy("BCFORMAT_intOrder", "ASC");
    	System.err.println(query.convertSelectQueryToString());
    	
    	ResultSet rs = query.executeSelect();
    	Vector v = QueryChannel.lookupRecord(rs, formfields);
    	rs.close();
    	return v;
    	
    	}catch (Exception e) {
    		e.printStackTrace(System.err);
    		
    	}
    	return null;
    }
    
   
    	
    	
    	
   
    
    public static String getNewBiospecimenID ( String strAdmissionID, int intPatientID , ChannelRuntimeData runtimeData, Hashtable cloneData) {
    	try {
    		StringBuffer barcode = new StringBuffer();
    	DALQuery query = new DALQuery();
    	query.setDomain("PATIENT", null,null,null);
    	Vector fields = new Vector();
    	if (strAdmissionID == null )
    		return null;
    	query.setDomain("ADMISSIONS","ADMISSIONS_intPatientID","PATIENT_intInternalPatientID","INNER JOIN");
    	query.setWhere(null,0,"ADMISSIONS_intPatientID","=",""+intPatientID,0,DALQuery.WHERE_HAS_VALUE);
    	query.setWhere("AND",0,"ADMISSIONS_strAdmissionID","=",""+strAdmissionID,0,DALQuery.WHERE_HAS_VALUE);
    	query.setWhere("AND",0,"ADMISSIONS_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
    	Vector bcFormat = getBarcodeFormat(strAdmissionID, intPatientID);
    	if (bcFormat.size()  == 0)
    		return null;
    	for (int i = 0; i< bcFormat.size(); i++) {
    		Hashtable result = (Hashtable) bcFormat.get(i);
    		String fieldName = (String) result.get("BCFORMAT_strFieldName");
    		if (fieldName != null && !fieldName.matches("BIOSPECIMEN_.+")) {
    			fields.add(fieldName);
    			query.setField(fieldName, null);
    		}
    		
    		
    	}
    	System.err.println(query.convertSelectQueryToString());
    	ResultSet rs = query.executeSelect();
    	Vector v = QueryChannel.lookupRecord(rs, fields);
    	rs.close();
    	if (v.size() != 1) {
    		System.err.println("Size of record is not equal to 1");
    		// we got problems 
    		return null;
    		
    	}
    		
    	
    	//Now we build up the barcode..
    	for (int i = 0; i< bcFormat.size(); i++) {
    		Hashtable result = (Hashtable) bcFormat.get(i);
    		String fieldName = (String) result.get("BCFORMAT_strFieldName");
    		String constant = (String) result.get("BCFORMAT_strConst");
    		int length = new Integer((String)result.get("BCFORMAT_intLength")).intValue();
    		String format = (String) result.get("BCFORMAT_strFormat");
    		int type = new Integer((String) result.get("BCFORMAT_intType")).intValue();
    		
    		if (constant != null)  { // notlooking for a field.
    			if (constant.length() < length)
    				constant = padLeft(constant,length,'0'); // pad with zeroes;
    			barcode.append(constant);
    			
    		}
    		else { // ok we are looking for a field now.
    			Object value = null;
    			String strValue = null;
    			if (!fields.contains(fieldName)) {
    			
    			if (cloneData != null)
    				value = cloneData.get(fieldName);
    			else
    				value = runtimeData.getParameter(fieldName);
    			}
    			else {
    				Hashtable queryResult = (Hashtable) v.get(0);
    				 DBField fieldTemp = (DBField) DatabaseSchema.getFields().get(fieldName);
    				value =  queryResult.get(fieldName);
    				System.err.println("Checked hash for key " + fieldName + " got " + value);
    			}
    			
    			if (value == null) {
    				 System.err.println("Value " + fieldName + " object is null" );
    				return null;
    			}
    			
    			//Check type and length and format if necessary
    			
    			if (type == 2) { //Dates are only ones we really need to check.
    				SimpleDateFormat dtFormatter = new SimpleDateFormat("dd/MM/yyyy");
    				java.util.Date dtValue = dtFormatter.parse((String) value);
    				 SimpleDateFormat dfFormatter = new SimpleDateFormat(format);
                     
                     if (dtValue != null)
                     {
                        strValue = dfFormatter.format(dtValue);
                     }
                     else {
                    	 System.err.println("dtValue is null");
                    	 return null;
                     }
                    	 
    			}
    			else 
    				strValue = (String) value;
    				
    			if (strValue.length() < length)
    				strValue = padLeft(strValue,length,'0');
    			
    			
    			barcode.append(strValue);
    			
    			
    			
    		}
    		
    		
    		
    		
    	}
    	
    	return barcode.toString();
    	
    	}catch (Exception e) {	e.printStackTrace(System.err);}
    	return null;
    }
    
    
    public static int getChildCount(int intBiospecimenKey)
    {
        if (intBiospecimenKey < 0)
            return -1;
        
        DALSecurityQuery dsqChildCount = new DALSecurityQuery();
        ResultSet rs = null;
        try
        {
            dsqChildCount.setDomain("BIOSPECIMEN", null, null, null);
            dsqChildCount.setCountField("BIOSPECIMEN_intBiospecimenID", false);
            dsqChildCount.setWhere(null, 0, "BIOSPECIMEN_intParentID", "=", intBiospecimenKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            dsqChildCount.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            rs = dsqChildCount.executeSelect();
            if (rs.first())
            {
                int result = rs.getInt(1);
                rs.close();
                return result;
            }
            else
            {
                rs.close();
                return 0;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.print("There wasan error getting the child count");
        }
        return -1;
        
    }
    
    public static String getClonedSuffix(String origSuffix, String sampleType) {
    	return getClonedSuffix(origSuffix, sampleType,false);
    }
    
    public static String getSuffixDB(String origSuffix, String sampleType, boolean isSubBio, int intStudyID) {
    	System.err.println("getSuffixDB(" + origSuffix + "," + sampleType + "," + isSubBio + "," + intStudyID+ ")");
 
    	
    	try{
    		 Vector formfields = DatabaseSchema.getFormFields( "cbiospecimen_sample_codes" );
    		DALQuery codequery = new DALQuery();
    		codequery.setDomain("SAMPLECODES", null,null,null);
    		codequery.setFields(formfields,null);
    		codequery.setWhere(null, 0, "SAMPLECODES_strSampleType", "=", sampleType,0, DALQuery.WHERE_HAS_VALUE);
    		codequery.setWhere("AND", 0, "SAMPLECODES_intStudyID", "=", intStudyID+"",0, DALQuery.WHERE_HAS_VALUE);
    		codequery.setOrderBy("SAMPLECODES_intOrder", "ASC");
    		ResultSet rs = codequery.executeSelect();
    		Vector results = QueryChannel.lookupRecord(rs, formfields);
    		rs.close();
    		if (results.size() == 0) {
    		
    			codequery.reset();
    			codequery.setDomain("SAMPLECODES", null,null,null);
    			codequery.setFields(formfields,null);
    			codequery.setWhere(null, 0, "SAMPLECODES_strSampleType", "=", sampleType,0, DALQuery.WHERE_HAS_VALUE);
    			codequery.setWhere("AND", 0, "SAMPLECODES_intStudyID", "=", "0",0, DALQuery.WHERE_HAS_VALUE);
    			codequery.setOrderBy("SAMPLECODES_intOrder", "ASC");
    			rs = codequery.executeSelect();
    			results = QueryChannel.lookupRecord(rs, formfields);
    			rs.close();
    			if (results.size() ==0) {
    				// Ok didnt find it in default - something is wrong...
    				return null;
    			}

    		}
    		

    		// Get position in the range.
    		int foundIndex = -1;
    		boolean sameSampleType = false;
    		int nextIndex = 0;
    		if (origSuffix != null ) {
    			for (int i =0 ; i < results.size(); i++) {
    				Hashtable h = (Hashtable) results.get(i);
    				String childCode = (String) h.get("SAMPLECODES_strChildCode");
    				String code = (String) h.get("SAMPLECODES_strCode");

    				if (origSuffix.equals(childCode) || origSuffix.equals(code) ) {
    					foundIndex = i;
    					break;
    				}
    				// If sample is the same sample type as its parent, then use the child code, else use the standard code.

    			}
    	

    		if (foundIndex == -1 )
    			;
    		else
    		 nextIndex = (foundIndex + 1 ) % results.size();
    		}
    		
    			
    		Hashtable next = (Hashtable) results.get(nextIndex);

    		String nextCode = null;
    		String subBioCode = (String) next.get("SAMPLECODES_strChildCode");
    		String bioCode = (String) next.get("SAMPLECODES_strCode");
    		
    		if (isSubBio && subBioCode != null && foundIndex != -1) {
    			nextCode = subBioCode;
    		}
    		else {
    			nextCode =  bioCode;

    		}
    		return nextCode;

    	}catch (Exception e) {
    		e.printStackTrace(System.err);
    	}
    	return null;
    }
    
   
    
    
    public static String getClonedSuffix(String origSuffix, String sampleType, boolean isSubBio) {
    
    	
    	if (sampleType.equals("DNA")) {
    		if (isSubBio) {
    			return "W";
    		}
    		else {
    		if (origSuffix == null)
    			return "H";
    		else if (origSuffix.equals("H")) {
    			return "J";
    		}
    		else if (origSuffix.equals("J")) {
    			return "H";
    		}
    		else if (origSuffix.equals("W")) {
    			return "W";
    		}
    		}
    		return null;
    	}
    	else if (sampleType.equals("Serum")) {
    		if (origSuffix == null)
    			return "A";
    		char suf = origSuffix.charAt(0);
    		if (suf >= 'A' && suf < 'C') {
    			suf++;
    			return "" + suf;
    		}
    		else if (suf == 'C') return ""+'A';
    		else return null;
    	}
    	
    	else if (sampleType.equals("Plasma")) {
    		if (origSuffix == null)
    			return "D";
    		char suf = origSuffix.charAt(0);
    		if (suf >= 'D' && suf < 'F') {
    			suf++;
    			return "" + suf;
    		}
    		else if (suf == 'F') return ""+'D';
    		else return null;
    	}
    	else if (sampleType.equals("Buffy Coat")) {
    		return "G";
    	}
    	else if (sampleType.equals("RNA")) {
    		return "R";
    	}
    	else if (sampleType.equals("Urine")) {
    		return "U";
    	}
    	return null;
    }
    
    
    public static int getParentIDFromParentKey(String strInternalParentKey)
    {
        if (strInternalParentKey == null)
            return -1;
        
        else
        {
            DALSecurityQuery dsqParentQuery = new DALSecurityQuery();
            ResultSet rs = null;
            
            try
            {
                dsqParentQuery.setDomain("BIOSPECIMEN", null, null, null);
                dsqParentQuery.setField("BIOSPECIMEN_strBiospecimenID", null);
                dsqParentQuery.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strInternalParentKey, 0, DALQuery.WHERE_HAS_VALUE);
                dsqParentQuery.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                rs = dsqParentQuery.executeSelect();
                if (rs.first())
                {
                    int result = rs.getInt(1);
                    rs.close();
                    return result;
                }
                else
                {
                    rs.close();
                    return -1;
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return -1;
    }
    
    
    
    
    
    public static int getBiospecimenParentKey(int intBiospecimenKey)
    {
        if (intBiospecimenKey <= 0)
           return -1;
        
        DALSecurityQuery dsqParent = new DALSecurityQuery();
        ResultSet rs = null;
        try
        {
            dsqParent.setDomain("BIOSPECIMEN", null, null, null);
            dsqParent.setField("BIOSPECIMEN_intParentID", null);
            dsqParent.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenKey+"", 0, DALQuery.WHERE_HAS_VALUE);
            dsqParent.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            rs = dsqParent.executeSelect();
            if (rs.first())
            {
                int result = rs.getInt(1);
                rs.close();
                return result;
            }
            else
            {
                rs.close();
                return -1;
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.out.println("There was an error getting the parent key");
        }
        
        return -1;
    }
    
    
    public static int getBiospecimenKey(String strBiospecimenID)
    {
       return getBiospecimenKey(strBiospecimenID, null);
    }
    
    public static int getBiospecimenKey(String strBiospecimenID, DALSecurityQuery queryObj)
    {
       
        DALSecurityQuery query = null;
        
        if (queryObj != null)
           query = queryObj;
        else
           query = new DALSecurityQuery();
       
        try
        {
            ResultSet rs;
            query.reset();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField("BIOSPECIMEN_intBiospecimenID", null);
            query.setWhere(null, 0, "BIOSPECIMEN_strBiospecimenID", "=", strBiospecimenID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();
            
            if (rs.first())
            {
                int keyValue = rs.getInt(1);
                rs.close();
                return keyValue;
            }
            else
            {
                rs.close();
                return SPECIMEN_NOT_FOUND;
            }
        }
        catch (Exception e)
        {
            System.err.println ("[BiospecimenGenerator] There was an error finding the Biospecimen key");
            return SPECIMEN_NOT_FOUND;
        }    
        
    }
    
    public static String getLastBioAddedSuffix(int intPatientID, String strEncounter, String strSampleType, int depth) {
    	StringBuffer retSuffix = new StringBuffer();
    	String strBiospecimenID = null;
    	Vector bcf = getBarcodeFormat( strEncounter,  intPatientID) ;
    	if (bcf == null) return null;
    	int prefix_size = 0;
    	for (int i = 0; i< bcf.size(); i++) {
    		Hashtable h = (Hashtable) bcf.get(i);
    		int size = new Integer( (String) h.get("BCFORMAT_intLength")).intValue();
    		prefix_size += size;
    	}
    	
    	DALQuery query = new DALQuery();
        ResultSet rs;
        try{
        query.setDomain("BIOSPECIMEN", null, null, null);
        query.setField("BIOSPECIMEN_strSampleType", null);
        query.setWhere(null, 0, "BIOSPECIMEN_intPatientID", "=", ""+intPatientID , 0, DALQuery.WHERE_HAS_VALUE);
        query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
        query.setWhere("AND" , 0 ,"BIOSPECIMEN_strEncounter","=", strEncounter, 0, DALQuery.WHERE_HAS_VALUE);
        query.setWhere("AND" , 0 ,"BIOSPECIMEN_strSampleType","=", strSampleType, 0, DALQuery.WHERE_HAS_VALUE);
        query.setWhere("AND" , 0 ,"BIOSPECIMEN_intDepth","=", ""+depth, 0, DALQuery.WHERE_HAS_VALUE);
        query.setField("BIOSPECIMEN_strBiospecimenID", null);
        query.setOrderBy("BIOSPECIMEN_intBiospecimenID", "DESC");
        rs = query.executeSelect();
        
        if (rs.first())
        {
        	strBiospecimenID = rs.getString("BIOSPECIMEN_strBiospecimenID");
        	
        	
        }
        rs.close();
        if ( strBiospecimenID != null) {
        	System.err.println("Getting suffix for biospecimen: " + strBiospecimenID  + " prefix size = " + prefix_size);
        String suffix = strBiospecimenID.substring(prefix_size);
        System.err.println("took substring: "+ suffix);
    	int x = 0;
    	while (x < suffix.length() && Character.isLetter(suffix.charAt(x)) ) {
    		retSuffix.append(suffix.charAt(x));
    		x++;

    	}
    	return retSuffix.toString();
        }else
        	return null;
    	
        }
        catch (Exception e) {
        	e.printStackTrace(System.err);
        	return null;}
    	
    }
    
    public static String getBiospecimenSampleType(String strInternalBiospecimenKey)
    {
        try
        {
            DALQuery query = new DALQuery();
            ResultSet rs;
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField("BIOSPECIMEN_strSampleType", null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strInternalBiospecimenKey , 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();
            
            if (rs.first())
            {
                String strSampleType = rs.getString(1);
                if (strSampleType != null)
                {
                    rs.close();
                    return strSampleType;
                }
                else
                {
                    rs.close();
                    return SPECIMEN_NOT_FOUND + "";
                }
            }
            else
            {
                rs.close();
                return SPECIMEN_NOT_FOUND + "";
            }
        }
        catch (Exception e)
        {
            System.err.println ("[BiospecimenGenerator] There was an error finding the user defined biospecimen sample type");
            return SPECIMEN_NOT_FOUND + "";
        }    
        
    }
    
    public static String getBiospecimenStudy (int intBiospecimenKey)
    {
        try
        {
            DALQuery query = new DALQuery();
            ResultSet rs;
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField("BIOSPECIMEN_intStudyKey", null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", ""+intBiospecimenKey , 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();
            
            if (rs.first())
            {
                String studykey  = ""+rs.getInt(1);
                if (studykey != null)
                {
                    rs.close();
                    return studykey;
                }
                else
                {
                    rs.close();
                    return null;
                }
            }
            else
            {
                rs.close();
                return null;
            }
        }
        catch (Exception e)
        {
            System.err.println ("[BiospecimenGenerator] There was an error finding the user defined biospecimen study key");
            e.printStackTrace(System.err);
            return null;
        }    
        
    }
    
    
    
    
    public static String getUserBiospecimenID(int intBiospecimenKey)
    {
        try
        {
            DALQuery query = new DALQuery();
            ResultSet rs;
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField("BIOSPECIMEN_strBiospecimenID", null);
            query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();
            
            if (rs.first())
            {
                String strFriendlyID = rs.getString(1);
                if (strFriendlyID != null)
                {
                    rs.close();
                    return strFriendlyID;
                }
                else
                {
                    rs.close();
                    return SPECIMEN_NOT_FOUND + "";
                }
            }
            else
            {
                rs.close();
                return SPECIMEN_NOT_FOUND + "";
            }
        }
        catch (Exception e)
        {
            System.err.println ("[BiospecimenGenerator] There was an error finding the user defined biospecimen id");
            return SPECIMEN_NOT_FOUND + "";
        }    
        
    }
    
    public static String getVialCalculationKey(int intBiospecimenKey){
        try {
            DALQuery query = new DALQuery();
            ResultSet rs;
            query.setDomain("VIAL_CALCULATION", null, null, null);
            query.setField("VIAL_CALCULATION_intVialCalculationKey", null);
            query.setWhere(null, 0, "VIAL_CALCULATION_intBiospecimenID", "=", intBiospecimenKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "VIAL_CALCULATION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "VIAL_CALCULATION_intSaved", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            rs = query.executeSelect();

            if (rs.first())
            {
                String strFriendlyID = rs.getString(1);
                rs.close();

                if (strFriendlyID != null)
                    return strFriendlyID;
                else
                    return VIAL_CALCULATION_NOT_FOUND + "";
            }
            else
            {
                rs.close();
                return VIAL_CALCULATION_NOT_FOUND + "";
            }
        }
        catch (Exception e)
        {
            System.err.println ("[BiospecimenUtilities] There was an error finding the vial calculation key");
            return SPECIMEN_NOT_FOUND + "";
        }    
    }
    
    
    public static String checkForDuplicateIDs (String strIDToCheck, int intIDType)
    {
        String tempString[] = new String[1];
        tempString[0] = strIDToCheck;
        
        String returnValue[] = checkForDuplicateIDs(tempString, intIDType);
        if (returnValue != null)
        {
            return returnValue[0];
        }
        else
        {
            return null;
        }
        
        
    }
    
    
    /*
     *  checkForDuplicateIDs
     *  -- REMEMBER WE WANT NULL -- it will return a list of duplicates
     *
     */
    
    public static String[] checkForDuplicateIDs (String strIDsToCheck[], int intIDType)
    {
        DALQuery duplicateQuery = new DALQuery();
        ResultSet rs = null;
        String strFieldToCheck = null;
        
        
        try
        { 
            if (intIDType == BARCODE)
            {
                strFieldToCheck = "BIOSPECIMEN_strOtherID";
            }
            else if (intIDType == BIOSPECIMENID)
            {
                strFieldToCheck = "BIOSPECIMEN_strBiospecimenID";
            }

            duplicateQuery.setDomain("BIOSPECIMEN", null, null, null);
            duplicateQuery.setField(strFieldToCheck, null);
            duplicateQuery.setWhere(null, 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            
            String strConnector = "";
            int intOpenBrackets = 0;
            int intCloseBrackets = 0;
            for (int i = 0; i < strIDsToCheck.length; i++)
            {
                if (i == 0) 
                {
                    intOpenBrackets = 1;
                    strConnector = "AND";
                }
                else 
                {
                    intOpenBrackets = 0;
                    strConnector = "OR";
                }
                
                if (i == (strIDsToCheck.length - 1))
                    intCloseBrackets = 1;
                else
                    intCloseBrackets = 0;
                
                duplicateQuery.setWhere(strConnector, intOpenBrackets, strFieldToCheck, "=", strIDsToCheck[i], intCloseBrackets, DALQuery.WHERE_HAS_VALUE);    
            }

            rs = duplicateQuery.executeSelect();

           
            if (rs.first() == false) // success!
            {
                rs.close();
                return null;
            }
            else
            {
                int intResultSetSize = -1;

                rs.last();

                intResultSetSize = rs.getRow();
                
                String strDuplicates[] = new String[intResultSetSize];
                int intCurrentArrayPosition = 0;

                rs.beforeFirst();

                while (rs.next())
                {
                    strDuplicates[intCurrentArrayPosition++] = rs.getString(strFieldToCheck);
                }
                rs.close();
                return strDuplicates;
            }
        }
        catch (Exception e)
        {
            
            e.printStackTrace();
            return null;
        }
        
    }
    

    public static String checkForDuplicateIDs (DALSecurityQuery query, String strIDToCheck, int intIDType)
    {
        String tempString[] = new String[1];
        tempString[0] = strIDToCheck;
        
        String returnValue[] = checkForDuplicateIDs(query, tempString, intIDType);
        if (returnValue != null)
        {
            return returnValue[0];
        }
        else
        {
            return null;
        }
        
        
    }
    
    
    /*
     *  checkForDuplicateIDs
     *  -- REMEMBER WE WANT NULL -- it will return a list of duplicates
     *
     */
    
    public static String[] checkForDuplicateIDs (DALSecurityQuery query, String strIDsToCheck[], int intIDType)
    {
        ResultSet rs = null;
        String strFieldToCheck = null;
        
        
        try
        { 
            if (intIDType == BARCODE)
            {
                strFieldToCheck = "BIOSPECIMEN_strOtherID";
            }
            else if (intIDType == BIOSPECIMENID)
            {
                strFieldToCheck = "BIOSPECIMEN_strBiospecimenID";
            }
            
            //create a new query if passed-in parameter is null 
            if ( query == null){
            		query = new DALSecurityQuery();
            }

            query.reset();
            query.setDomain("BIOSPECIMEN", null, null, null);
            query.setField(strFieldToCheck, null);
            query.setWhere(null, 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            
            String strConnector = "";
            int intOpenBrackets = 0;
            int intCloseBrackets = 0;
            for (int i = 0; i < strIDsToCheck.length; i++)
            {
                if (i == 0) 
                {
                    intOpenBrackets = 1;
                    strConnector = "AND";
                }
                else 
                {
                    intOpenBrackets = 0;
                    strConnector = "OR";
                }
                
                if (i == (strIDsToCheck.length - 1))
                    intCloseBrackets = 1;
                else
                    intCloseBrackets = 0;
                
                query.setWhere(strConnector, intOpenBrackets, strFieldToCheck, "=", strIDsToCheck[i], intCloseBrackets, DALQuery.WHERE_HAS_VALUE);    
            }

            rs = query.executeSelect();

           
            if (rs.first() == false) // success!
            {
                rs.close();
                return null;
            }
            else
            {
                int intResultSetSize = -1;

                rs.last();

                intResultSetSize = rs.getRow();
                
                String strDuplicates[] = new String[intResultSetSize];
                int intCurrentArrayPosition = 0;

                rs.beforeFirst();

                while (rs.next())
                {
                    strDuplicates[intCurrentArrayPosition++] = rs.getString(strFieldToCheck);
                }
                rs.close();
                return strDuplicates;
            }
        }
        catch (Exception e)
        {
            
            e.printStackTrace();
            return null;
        }
        
    }
    
    public static int getParentDepthFromParentKey(String strInternalParentKey)
    {
       return getParentDepthFromParentKey(strInternalParentKey, null);
    }
    
    /** Return the depth of biospecimen
     */
        public static int getDepthFromKey(String strInternalKey, DALSecurityQuery dsqQuery)
       {
           if (strInternalKey == null)
               return 0;
           
           else
           {
              DALSecurityQuery dsqParentQuery = null;
              if (dsqQuery != null)
              {
                 dsqParentQuery = dsqQuery;
              }
              else
              {
                 dsqParentQuery = new DALSecurityQuery();
              }
              
               ResultSet rs = null;
               
               try
               {
                   dsqParentQuery.reset();
                   dsqParentQuery.setDomain("BIOSPECIMEN", null, null, null);
                   dsqParentQuery.setField("BIOSPECIMEN_intDepth", null);
                   dsqParentQuery.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strInternalKey, 0, DALQuery.WHERE_HAS_VALUE);
                   dsqParentQuery.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                   rs = dsqParentQuery.executeSelect();
                   
                   int intResult = 0;
                   if (rs.first())
                   {
                       intResult = rs.getInt(1);
                   }
                   rs.close();
                   return intResult;
                   
               }
               catch (Exception e)
               {
                   e.printStackTrace();
               }
           }
           return 0;
       }
    
    
    
    
  /** Return the depth of parent biospecimen
  */
     public static int getParentDepthFromParentKey(String strInternalParentKey, DALSecurityQuery dsqQuery)
    {
        if (strInternalParentKey == null)
            return 0;
        
        else
        {
           DALSecurityQuery dsqParentQuery = null;
           if (dsqQuery != null)
           {
              dsqParentQuery = dsqQuery;
           }
           else
           {
              dsqParentQuery = new DALSecurityQuery();
           }
           
            ResultSet rs = null;
            
            try
            {
                dsqParentQuery.reset();
                dsqParentQuery.setDomain("BIOSPECIMEN", null, null, null);
                dsqParentQuery.setField("BIOSPECIMEN_intDepth", null);
                dsqParentQuery.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", strInternalParentKey, 0, DALQuery.WHERE_HAS_VALUE);
                dsqParentQuery.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                rs = dsqParentQuery.executeSelect();
                
                int intResult = 0;
                if (rs.first())
                {
                    intResult = rs.getInt(1);
                }
                rs.close();
                return intResult;
                
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return 0;
    }  
     
     
     
     public static String getNewSubBiospecimenStringID(String strPrefix, int intSuffix){
         String strActualStringID;
         String strValidation = null;
         
         do {
             if (strPrefix != null)
                strActualStringID = strPrefix + "-" + intSuffix;
             else strActualStringID = "" + intSuffix + "";
             
             strValidation = checkForDuplicateIDs(strActualStringID, BIOSPECIMENID);
             intSuffix++;
         }
         while (strValidation != null);
         
         return strActualStringID;
         
         
     }
     
     public static String[] getBarcodeComponents(String s) {
     
    	 String[] components = new String[3];
     int numpos = -1;
     int samplecodestart = -1;
     int i = s.length()-1;
     if (Character.isDigit(s.charAt(i))) {
     	while (i > 0) {
     		if (Character.isLetter(s.charAt(i-1)) && Character.isDigit(s.charAt(i))) {

     		System.out.println("numpos is " + i);
     		numpos = i;
     		break;
     		}
     		i = i-1;
     	}
     }
     while (true) {
     	if (Character.isLetter(s.charAt(i)) && Character.isDigit(s.charAt(i-1))) {
     		System.out.println("samplecode start is " + i);
     		samplecodestart = i;
     		break;
     		
     	}
     	i--;
     	
     }

     System.out.println("Prefix is : " + s.substring(0,samplecodestart));
     components[0] =  s.substring(0,samplecodestart);
     if (numpos == -1) {
     System.out.println("Sample code is : " + s.substring(samplecodestart));
     components[1] = s.substring(samplecodestart);
     }
     else {
    	 System.out.println("Sample code is : " + s.substring(samplecodestart,numpos));
    	 components[1]=s.substring(samplecodestart,numpos);
     }
     	
     if (numpos != -1) {
     System.out.println("Clone no is: "+s.substring(numpos));
     	components[2] = s.substring(numpos);
     
     
     }
     else {
    	 components[2] = null;
     }
     return components;
}

     public static String getNewSubBiospecimenStringID(DALSecurityQuery query, String strSampleType, String origBiospecID, boolean isSubBio, int studykey) {
    	 //Check for empty string
    	 
    	 
    	 // Here we deal with different barcode types...
    	 String strPrefix = origBiospecID;
    	String sampleCode = null;
    	String newSampleCode = null;
    	 // Check to see if biospecimen is a duplicate already...
    	 int biospec_len = origBiospecID.length();
    	// int biospec_prefix_len = 
    	 if (biospec_len > 5) {
    		 if (Character.isDigit(origBiospecID.charAt(biospec_len-1)) && Character.isLetter(origBiospecID.charAt(biospec_len-2))) {
    			 // Strip off the last two 
    			 strPrefix = origBiospecID.substring(0,biospec_len-2);
    			 sampleCode = "" + origBiospecID.charAt(biospec_len-2);
    		 }
    		 else if (Character.isLetter(origBiospecID.charAt(biospec_len-1))) {
    			 sampleCode = "" + origBiospecID.charAt(biospec_len-1);
    			 strPrefix = origBiospecID.substring(0,biospec_len-1);
    		 }
    	 } else
    		 return null;
    		  
    	 String[] components = BiospecimenUtilities.getBarcodeComponents(origBiospecID);
    	 sampleCode = components[1];
    	 strPrefix = components[0];
    	 if (sampleCode == null) return null; // we have a problem.
    	 
    	  newSampleCode = 	  getSuffixDB(sampleCode, strSampleType, isSubBio, studykey);
    	  System.err.println("running clone with "+ sampleCode + " and " + strSampleType + " and " + isSubBio + " = " + newSampleCode);
    	 
    	 return getNewSubBiospecimenStringID(query,strPrefix + newSampleCode,0);
     	 
     }
    
     public static String getNewSubBiospecimenStringID(DALSecurityQuery query, String strPrefix, int intSuffix){
         String strActualStringID;
         String strValidation = null;
         
         do {
             if (strPrefix != null)
            	 if (intSuffix > 0)
            		 strActualStringID = strPrefix  + intSuffix;
            	 else strActualStringID = strPrefix;
             else strActualStringID = "" + intSuffix + "";
             
             strValidation = checkForDuplicateIDs(query, strActualStringID, BIOSPECIMENID);
             intSuffix++;
         }
         while (strValidation != null);
         
         return strActualStringID;
         
         
     }

     public static Hashtable getChildrenIDs(int intParentID, DALSecurityQuery query){
     	StringBuffer sbIntIDs = new StringBuffer();
     	StringBuffer sbStringIDs = new StringBuffer();
     	Hashtable htReturn = new Hashtable();
     	try {
     		query.setDomain("BIOSPECIMEN", null,null,null);
     		query.setField("BIOSPECIMEN_intBiospecimenID",null);
     		query.setField("BIOSPECIMEN_strBiospecimenID", null);
     		query.setWhere(null, 0, "BIOSPECIMEN_intParentID","=", intParentID+"",0,DALQuery.WHERE_HAS_VALUE);
     		query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
     		System.out.println("[BIOSPECIMEN UTE] Children IDs"+ query.convertSelectQueryToString() );
     		ResultSet rs = query.executeSelect();
     		while (rs.next()){
     			sbIntIDs.append(rs.getString("BIOSPECIMEN_intBiospecimenID") +",");
     			sbStringIDs.append(rs.getString("BIOSPECIMEN_strBiospecimenID") +",");
     			
     		}
     		
     		htReturn.put("VIAL_CALCULATION_strBiospecimenKeys", sbIntIDs.toString());
     		htReturn.put("VIAL_CALCULATION_strBiospecimenIDs",  sbStringIDs.toString());
     		rs.close();
     	}
     	catch(Exception e ){
     		e.printStackTrace(System.err);
     	}
     	
     	
     	return htReturn;
     }
    
     public static Hashtable getChildrenLocations(int intParentID, DALSecurityQuery query){
     	StringBuffer sbLocationIDs = new StringBuffer();
     	StringBuffer sbLocations = new StringBuffer();
     	Hashtable htReturn = new Hashtable();
     	htReturn.put("VIAL_CALCULATION_intTrayID","-1");
     	try {
     		query.reset();
     		query.setDomain("BIOSPECIMEN", null,null,null);
     		query.setField("BIOSPECIMEN_intBiospecimenID",null);
     		query.setField("BIOSPECIMEN_intCellID",null);
     		query.setWhere(null, 0, "BIOSPECIMEN_intParentID","=", intParentID+"",0,DALQuery.WHERE_HAS_VALUE);
     		query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
     		System.out.println("[BIOSPECIMEN UTE] Children Locations"+ query.convertSelectQueryToString() );
     		ResultSet rs = query.executeSelect();
     		
     		while (rs.next()){
     			if (rs.getInt("BIOSPECIMEN_intCellID") != -1 ){
     				
     				sbLocationIDs.append(rs.getInt("BIOSPECIMEN_intCellID")+",");
     				String strLocation[] = InventoryUtilities.getBiospecimenLocation("inventory",rs.getInt("BIOSPECIMEN_intCellID"),(AuthToken) null,query);
     				sbLocations.append(strLocation[InventoryUtilities.ROW_NUMBER]+ " " +strLocation[InventoryUtilities.COLUMN_NUMBER]+ ",");
     				//htReturn.put("VIAL_CALCULATION_intTrayID", strLocation[InventoryUtilities.TRAY_ID]);
     			}
     			else { 
     				sbLocationIDs.append("NA," );
     				sbLocations.append("NA,");
     			}
     		}
     		rs.close();
     		
     		
     		htReturn.put("VIAL_CALCULATION_strLocationIDs", sbLocationIDs.toString());
     		htReturn.put("VIAL_CALCULATION_strLocations",  sbLocations.toString());
     		//System.out.println("strLocations: " + sbLocationIDs.toString() + " KEYS: "+ sbLocations.toString());
     	}
     	catch(Exception e ){
     		e.printStackTrace(System.err);
     	}
     	
     	
     	return htReturn;
     }

     public static String[] removeEmptyBarcodes(boolean barcodeRequired, String strBarcodeIDs[])
     {
     	String newBarcodes[] = null;
        int count = 0;
        
     	try 
        {
            if (!barcodeRequired)
            {
                // barcodes are not required remove empty barcodes
                for (int i = 0; i < strBarcodeIDs.length; i++)
                {
                    if (strBarcodeIDs[i].length() > 0)
                    {
                        newBarcodes[count] = strBarcodeIDs[i];
                        count++;
                    }
                }
            }
            else
            {
                for (int i = 0; i < strBarcodeIDs.length; i++)
                {
                    newBarcodes[i] = strBarcodeIDs[i];
                }
            }
        }
     	catch(Exception e )
        {
            e.printStackTrace();
     	}
     	
     	return newBarcodes;
     }
     /** This function takes in a biospecimen ID and build up and return a RuntimeData for that biospecimen 
      *  
      * @param intBiospecimenID
      * @return
      */
     
     public static ChannelRuntimeData buildBiospecimenRuntimeData(int intBiospecimenID) {
         Vector vtParentBiospecDetail = DatabaseSchema.getFormFields("cbiospecimen_view_biospecimen");
         ChannelRuntimeData rdParentBioRuntimeData = new ChannelRuntimeData();
         DALQuery query = new DALQuery();
         try {
             //reset the query
             query.reset();
             query.setDomain("BIOSPECIMEN", null, null, null);
             query.setFields(vtParentBiospecDetail, null);
             query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenID+"", 0, DALQuery.WHERE_HAS_VALUE);
             ResultSet rsResult = query.executeSelect();
             
             if (rsResult.next()) {
                 for (int i=0; i < vtParentBiospecDetail.size(); i++) {
                     String strFieldName = (String) vtParentBiospecDetail.get(i);
                     String strFieldValue = rsResult.getString(strFieldName);
                     
                     if (strFieldName.equals("BIOSPECIMEN_dtSampleDate") || strFieldName.equals("BIOSPECIMEN_dtExtractedDate")) {
                         if (strFieldValue != null && strFieldValue.length() > 0)
                             rdParentBioRuntimeData.setParameter(strFieldName, Utilities.convertDateForDisplay(strFieldValue));
                     }
                     else if (strFieldName.equals("BIOSPECIMEN_tmExtractedTime") || strFieldName.equals("BIOSPECIMEN_tmSampleTime")){
                          if (strFieldValue != null && strFieldValue.length() > 0)
                             rdParentBioRuntimeData.setParameter(strFieldName, Utilities.convertTimeForDisplay(strFieldValue));
                     }
                     else {
                         rdParentBioRuntimeData.setParameter(strFieldName, strFieldValue);
                     }
                 }
                 
                 // set the parent ID and key for the sub-specimens
                 
                 rdParentBioRuntimeData.setParameter("BIOSPECIMEN_intParentID", intBiospecimenID+"");
                 rdParentBioRuntimeData.setParameter("BIOSPECIMEN_strParentID", BiospecimenUtilities.getUserBiospecimenID(intBiospecimenID));
                 rdParentBioRuntimeData.setParameter("BIOSPECIMEN_intBiospecimenID", (String) null);
                
                 
             }
             rsResult.close();
         }
         catch (Exception e) {
             e.printStackTrace();
             LogService.instance().log(LogService.ERROR, "CBiospecimen::() : Unexpected error ", e);
         }
         
         return rdParentBioRuntimeData;
     }
    
     public static String validateBiospecimenID(int intDepth, String strBiospecimentID)
     {
    	 //Tom add verify BiospecimenID with the format of CHW
     	 String strResult =null;
    	 if (intDepth == 2)
    	 {
    		 // verify the format of the biospecimenId for fist level: 10001DNAI
    		 if (!strBiospecimentID.matches("\\w+DNA[IVX]+"))
    		 {	
    			 strResult = "Invalid First Level BiospecimenID Format(Should be like 10001DNAI)";
    			 //LogService.log(LogService.INFO,"SUB: "+ strBiospecimentID.substring(strBiospecimentID.lastIndexOf("DNA")+ 3, strBiospecimentID.length()));
    			 //LogService.log(LogService.INFO, "Match ID: "+runtimeData.getParameter("BIOSPECIMEN_strBiospecimenID"));
    		 }
    		
    	 }
    	 else if (intDepth ==3)
    	 {
    		 //verify the format of the biospecimenId for fist level: 10001DNAI
    		 if (!strBiospecimentID.matches("\\w+DNA[IVX]+[a-z]{1}"))
    		 {	
    			 strResult = "Invalid Second Level BiospecimenID Format(Should be like 10001DNAIa)";
    			 //LogService.log(LogService.INFO,"SUB: "+ strBiospecimentID.substring(strBiospecimentID.lastIndexOf("DNA")+ 3, strBiospecimentID.length()));
    			 //LogService.log(LogService.INFO, "Match ID: "+runtimeData.getParameter("BIOSPECIMEN_strBiospecimenID"));
    		 }
    		 
    	 }
    	 else if (intDepth ==4)
    	 {
    		 if (!strBiospecimentID.matches("\\w+DNA[IVX]+[a-z]{1}[a-z]{1}"))
    		 {	
    			 strResult = "Invalid Third Level BiospecimenID Format(Should be like 10001DNAIab)";
    			 //LogService.log(LogService.INFO,"SUB: "+ strBiospecimentID.substring(strBiospecimentID.lastIndexOf("DNA")+ 3, strBiospecimentID.length()));
    			 //LogService.log(LogService.INFO, "Match ID: "+runtimeData.getParameter("BIOSPECIMEN_strBiospecimenID"));
    		 }
    	 }
    	 return strResult;
    	 //End tom modifi
     }
}
