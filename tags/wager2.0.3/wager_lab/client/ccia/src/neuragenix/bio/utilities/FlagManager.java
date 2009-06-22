/*
 * FlagManager.java
 * Copyright (C) Neuragenix Pty Ltd, 2005
 * Author : Daniel Murley
 * Purpose : This class manages the flagging of records for various domains
 *
 *
 *
 * Created on 17 March 2005, 10:43
 */

package neuragenix.bio.utilities;


import neuragenix.dao.*;
import neuragenix.common.*;
import java.util.Vector;
import java.util.Hashtable;
import java.sql.*;

/**
 *
 * @author  dmurley
 */
public class FlagManager {
    
    public static final int DOMAIN_BIOSPECIMEN = 0;
    public static final int DOMAIN_PATIENT = 1;
    
    
    /** Creates a new instance of FlagManager */
    public FlagManager() {
    }
    
    
    public static boolean checkFlag(int intInternalID, int intDomain) {
        return checkFlag(intInternalID, intDomain, null);
    }
    
    /**
     *   Function to check if a particular indentifier from a domain has been flagged.
     */
    public static boolean checkFlag(int intInternalID, int domain, String strUser) {
        try {
            
            DALSecurityQuery dsqFlaggedRecords = getFlagQuery(intInternalID, domain, strUser);
            
           // System.out.println("The Query : " + dsqFlaggedRecords.convertSelectQueryToString());
            
            ResultSet rs = dsqFlaggedRecords.executeSelect();
            
            if (rs.first()) // we've found a flag
            {
                rs.close();
                return true;
            }
            else {
                rs.close();
                return false;
            }
            
        }
        catch(Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
        
    }
    
    public static String getFlaggedRecordsXML(int domain, String strUser) {
        StringBuffer strXML = new StringBuffer();
        try {
            int intInternalID = -1;
            DALSecurityQuery dsqFlaggedRecords = getFlagQuery(intInternalID, domain, strUser);
            Vector vtFormFields = (Vector) DatabaseSchema.getFormFields("ccoredomains_view_flag").clone();
            
            switch (domain) {
                case DOMAIN_BIOSPECIMEN:
                    dsqFlaggedRecords.setDomain("BIOSPECIMEN", "BIOSPECIMEN_intBiospecimenID", "FLAG_intID", "INNER JOIN");
                    dsqFlaggedRecords.setField("BIOSPECIMEN_strBiospecimenID", null);
                    vtFormFields.add("BIOSPECIMEN_strBiospecimenID");
                    break;
                case DOMAIN_PATIENT:
                    dsqFlaggedRecords.setDomain("PATIENT", "PATIENT_intPatientID", "FLAG_intID", "INNER JOIN");
                    dsqFlaggedRecords.setField("PATIENT_strPatientID", null);
                    vtFormFields.add("PATIENT_strPatientID");
            }
            
            //System.out.println("[Flag Manager The Query is]" + dsfFlaggedRecords.convertSelectQueryToString());
            ResultSet rs = dsqFlaggedRecords.executeSelect();
            
            strXML.append(QueryChannel.buildSearchXMLFile("flagged", rs, vtFormFields));
            rs.close();
            return strXML.toString();
            
        }
        catch(Exception e) {
            e.printStackTrace(System.err);
            return "";
        }
    }
    
    
    private static DALSecurityQuery getFlagQuery(int intInternalID, int domain, String strUser) {
        try {
            DALSecurityQuery dsqFlaggedRecords = new DALSecurityQuery();
            Vector vtFlaggedFields = DatabaseSchema.getFormFields("ccoredomains_view_flag");
            
            dsqFlaggedRecords.reset();
            dsqFlaggedRecords.setDomain("FLAG", null, null, null);
            dsqFlaggedRecords.setCaseSensitive(false);
            dsqFlaggedRecords.setFields(vtFlaggedFields, null);
            dsqFlaggedRecords.setWhere(null, 0, "FLAG_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            if (intInternalID != -1) {
                dsqFlaggedRecords.setWhere("AND", 0, "FLAG_intID", "=", intInternalID + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
            
            String strFlagDomain = "";
            
            switch (domain) {
                case DOMAIN_BIOSPECIMEN:
                    strFlagDomain = "BIOSPECIMEN";
                    break;
                case DOMAIN_PATIENT:
                    strFlagDomain = "PATIENT";
                    break;
                default:
                    return null;
                    
            }
            
            dsqFlaggedRecords.setWhere("AND", 0, "FLAG_strFlagDomain", "=", strFlagDomain, 0, DALQuery.WHERE_HAS_VALUE);
            
            //if (strUser != null) {
            //    dsqFlaggedRecords.setWhere("AND", 0, "FLAG_strUser", "=", strUser, 0, DALQuery.WHERE_HAS_VALUE);
            //}
            
            return dsqFlaggedRecords;
        }
        catch (Exception e) {
            e.printStackTrace(System.err);
            return null;
        }
    }
    
    public static boolean toggleFlag(int intInternalID, int domain, String strUser) {
        try {
            
            DALSecurityQuery dsqToggleFlags = getFlagQuery(intInternalID, domain, strUser);
            
            Hashtable htFieldData = new Hashtable();
            
            htFieldData.put("FLAG_intID", intInternalID + "");
            //if (strUser!=null){
            //    htFieldData.put("FLAG_strUser", strUser);
            //}
            
            switch (domain) {
                case DOMAIN_BIOSPECIMEN:
                    htFieldData.put("FLAG_strFlagDomain", "Biospecimen");
                    break;
                    
                case DOMAIN_PATIENT:
                    htFieldData.put("FLAG_strFlagDomain", "Patient");
                    break;
                    
            }
            
            if (dsqToggleFlags != null) {
                ResultSet rs = dsqToggleFlags.executeSelect();
                //System.out.println("[Flag Manager] "+ dsqToggleFlags.convertSelectQueryToString());
                
                if (rs.first()) // item is flagged
                {
                    rs.close();
                    dsqToggleFlags.clearFields();
                   
                    dsqToggleFlags.setField("FLAG_intDeleted", "-1");
                    //System.out.println("About to toogle the flag");
                    return dsqToggleFlags.executeUpdate();
                }
                else // item is not flagged
                {
                    // flag the record
                    rs.close();
                    dsqToggleFlags.setFields(DatabaseSchema.getFormFields("ccoredomains_view_flag"), htFieldData);
                    return dsqToggleFlags.executeInsert();
                    
                }
            }
            
        }
        catch(Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
        return false;
    }
    
    
    // delete the flagged records corresponding to a deleted biospecimen
    public static boolean deleteFlag(int intInternalID, int domain, String strUser) {
        try {
            
            DALSecurityQuery dsqDeleteFlag = getFlagQuery(intInternalID, domain, strUser);
            dsqDeleteFlag.clearFields();
            dsqDeleteFlag.setField("FLAG_intDeleted", "-1");
            System.out.println("Delete flag query : " + dsqDeleteFlag.convertSelectQueryToString());
            return dsqDeleteFlag.executeUpdate();
            
        }
        catch(Exception e) {
            e.printStackTrace(System.err);
            return false;
        }
        
    }
    
    
    
    
    
}
