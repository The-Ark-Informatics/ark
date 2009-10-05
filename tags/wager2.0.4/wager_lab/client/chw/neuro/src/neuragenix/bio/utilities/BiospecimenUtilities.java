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
import java.lang.StringBuffer;
import neuragenix.security.AuthToken;

import java.util.Hashtable;
import java.util.Vector;

import org.jasig.portal.ChannelRuntimeData;
import org.jasig.portal.services.LogService;

public class BiospecimenUtilities {
    
    /** Creates a new instance of BiospecimenUtilities */
    public BiospecimenUtilities() {
    }
    
    public static final int SPECIMEN_NOT_FOUND = -1;
    public static final int BIOSPECIMENID = 0;
    public static final int BARCODE = 1;
    public static final int VIAL_CALCULATION_NOT_FOUND = -1;
    
    public static final int DOMAIN_BIOSPECIMEN = 0;
    
    public static String getBiospecimenDetails(String intBiospecimentID, AuthToken authToken) 
    {
        StringBuffer sbBiospecimenDetails = new StringBuffer();
        try 
        {
            if (authToken.hasActivity ("biospecimen_view"))
            {
                Vector formfields = DatabaseSchema.getFormFields( "cbiospecimen_view_biospecimen" );
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
    
     public static String getNewSubBiospecimenStringID(DALSecurityQuery query, String strPrefix, int intSuffix){
         String strActualStringID;
         String strValidation = null;
         
         do {
             if (strPrefix != null)
                strActualStringID = strPrefix + "-" + intSuffix;
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
    
}
