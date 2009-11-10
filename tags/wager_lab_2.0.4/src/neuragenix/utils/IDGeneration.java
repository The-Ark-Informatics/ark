/*
 * Copyright (c) 2004 Neuragenix, All Rights Reserved.
 * File:        IDGeneration.java
 * Created:     06 July 2004
 * @author:     Navin Parappat
 * Desc:        Auto-generates numbers for ID's (biospecimen and patient)
 *
 * History:     [Author]    [Date]              [Description]
 *              Navin       06-July-2004        Initial Creation
 *              Navin       23-July-2004        Testing and Verified as Working By ??? - CR
 */

// Package Declaration
package neuragenix.utils;

// Package Imports
import java.util.Calendar;// for date/time details   
import java.util.Vector;
import neuragenix.dao.DALQuery;         // for access to the database
import java.sql.ResultSet;              // for access to data from queries

// Exception Class Defintion
// Exception thrown when an error originates from neuragenix.utils.IDGeneration occurs
class IDGenerationException extends Exception {};
class IDGenerationDatabaseQueryException extends Exception {};

// Class Defintion
public class IDGeneration extends java.lang.Object 
{
    // Following static final strings used to define the location of the system
    public static final String SYSTEM_TIMEZONE = "Australia/Melbourne";    
    // http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt
    public static final String SYSTEM_LANGUAGE = "en";
    // http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html 
    public static final String SYSTEM_COUNTRY = "AU";
    
    private static String year;
    private static String biospecimenIDCount;
    private static String patientIDCount;

    /** Creates a new instance of IDGeneration */
    public IDGeneration() 
    {
        // No constructror required for class - call static methods
    };

    //--------------------------------------------------------------------------
    // Auto Generate Patient ID
    // Function automatically generates the required patient ID based on the
    // the last patient inserted.
    //--------------------------------------------------------------------------
    public static String autoGeneratePatientID() throws IDGenerationException 
    {
        try
        {
            String newStrPatID, currentStrPatID;
           
            // Query the database determine the last PATIENTID
            currentStrPatID = getPatientID ();
            // System.err.println ("[IDGeneration::autoGeneratePatientID] currentStrPatID = '" + currentStrPatID + "'");

            // Determine the new PATIENTID
            newStrPatID = generateNextPatientID (currentStrPatID);
            // System.err.println ("[IDGeneration::autoGeneratePatientID] newStrPatID = '" + newStrPatID + "'");

            return newStrPatID;
        }
        catch (Exception e) //catch all exceptions
        {
            e.printStackTrace(System.err);
            throw new IDGenerationException();
        }
    };

    //--------------------------------------------------------------------------
    // Get Biospecimen ID
    // Function attempts to:
    // 1. Query the database for the last (max) record in ix_patient
    // 2. Return the str PatientID for the last record in ix_patient
    //--------------------------------------------------------------------------
    private static String getPatientID () throws IDGenerationDatabaseQueryException
    {
        String reqdStrPatID = new String();
        try
        {
            // Find the Max ("PATIENTKEY");
            DALQuery queryMaxPatID = new DALQuery ();
            queryMaxPatID.clearDomains();
            queryMaxPatID.clearFields();
            queryMaxPatID.clearWhere();
            queryMaxPatID.setDomain("PATIENT", null, null, null);
            queryMaxPatID.setMaxField ("PATIENT_intInternalPatientID");
            java.sql.ResultSet rs = queryMaxPatID.executeSelect();
            
            if (rs.next())
            {
                // Find the "BIOSPECIMENID" for the MAX ("BIOSPECIMENKEY")
                String reqdIntPatID = rs.getString ("MAX_PATN_intIntPatientID");
                // System.err.println ("[IDGeneration::getPatientID] reqdIntPatID '" + reqdIntPatID + "'");
                
                queryMaxPatID.clearDomains();
                queryMaxPatID.clearFields();
                queryMaxPatID.clearWhere();
                queryMaxPatID.clearMaxFields();
                queryMaxPatID.setDomain("PATIENT", null, null, null);
                queryMaxPatID.setField("PATIENT_strPatientID", null);
                queryMaxPatID.setWhere (null, 0, "PATIENT_intInternalPatientID", "=", reqdIntPatID, 0, DALQuery.WHERE_HAS_VALUE);
                rs = queryMaxPatID.executeSelect();
                
                if (rs.next())
                {
                    reqdStrPatID = rs.getString ("PATIENT_strPatientID");
                    // System.err.println ("[IDGeneration::getPatientID] reqdStrPatID '" + reqdStrPatID + "'");
                }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println ("[IDGeneration::getPatientID] *** Exception when attempting database queries! ");
            throw new IDGenerationDatabaseQueryException();
        }        
        return reqdStrPatID;
    };
    
    //--------------------------------------------------------------------------
    // Generate Next Patient ID
    // Function will take in the current patient ID (currentID) and attempt to
    // generate a new patient ID. If problems are encountered when reading the
    // current patient ID, the function will reset the counter and return a count
    // of zero.
    //--------------------------------------------------------------------------
    private static String generateNextPatientID (String currentID)
    {        
        // check to see if the patientID format is correct
        try
        {
            // if able to parse - increment should work
            // System.err.println ("[IDGeneration::getPatientID] Trying to parse the string as an int. PatID: " + patientIDCount);
            int temp = Integer.parseInt(currentID);
            
            if (patientIDCount == null)
            {
                patientIDCount = Integer.toString(temp).toString();
                // System.err.println ("[IDGeneration::getPatientID] Reseting Patient Counter: " + patientIDCount);
            }
        }
        catch (NumberFormatException e)
        {
            // unable to parse - restart counter
            restartPatientIDCounter();
            System.err.println ("[IDGeneration::getPatientID] *** Exception caught: Reinitalising Patient ID counter: '" + patientIDCount + "'");
        }
        
        // increment the PatientID counter
        incrementPatientIDCounter();        
        // System.err.println ("[IDGEneration::genNxtPatID] patientIDCount = '" + patientIDCount + "'");
        
        return patientIDCount;
    };
    
    //--------------------------------------------------------------------------
    // Auto Generate Biosepcimen ID
    // Function automatically generates the required biospecimen ID based on the
    // the last biospecimen inserted.
    //--------------------------------------------------------------------------
    public static String autoGenerateBiospecimenID() throws IDGenerationException 
    { 
        try
        {
            String newStrBioID, currentStrBioID;
// lock           
            // Query the database determine the last BIOSPECIMENID
            currentStrBioID = getBiospeciemID ();
            // System.err.println ("[IDGeneration.autoGenerateBiospecimenID] currentStrBioID = '" + currentStrBioID + "'");
//unlock
            // Store in private variables the current year and counter
            year = getYear (currentStrBioID);
            biospecimenIDCount = getCount (currentStrBioID);
            
            // Determine the new BIOSPECIMEN ID
            newStrBioID = generateNextBiosepcimenID (currentStrBioID);
            // System.err.println ("[IDGeneration.autoGenerateBiospecimenID] newStrBioID = '" + newStrBioID + "'");

            return newStrBioID;
        }
        catch (Exception e) //catch all exceptions
        {
            e.printStackTrace(System.err);
            throw new IDGenerationException();
        }
    };

    //--------------------------------------------------------------------------
    // Auto Generate Biosepcimen ID
    // Function automatically generates the required biospecimen ID based on the
    // the last biospecimen inserted.
    //--------------------------------------------------------------------------
    public static Vector autoGenerateBiospecimenIDs(int intNoOfIDs) throws IDGenerationException 
    { 
        try
        {
            return getBiospecimenIDs(intNoOfIDs);
        }
        catch (Exception e) //catch all exceptions
        {
            e.printStackTrace(System.err);
            throw new IDGenerationException();
        }
    };
    
    //--------------------------------------------------------------------------
    // Get Year
    // Function attempts to parse the string biospecID and returns the first 2 characters
    //--------------------------------------------------------------------------
    private static String getYear (String biospecID)
    {
        String str = new String();
        try
        {
            str = biospecID.substring(0,2);
        }
        catch (IndexOutOfBoundsException e)
        {
            System.err.println ("[IDGeneration::getYear] ** IndexOutOfBoundsException: Trying to Extract Year: biospecID = '" + biospecID + "'");
            e.printStackTrace(System.err);
        }
        return str;
    };

    //--------------------------------------------------------------------------
    // Get Count
    // Function attempts to parse the string biospecID and returns all characters after 3rd character
    //--------------------------------------------------------------------------
    private static String getCount (String biospecID)
    {
        String str = new String();        
        try
        {
            str = biospecID.substring(3);
        }
        catch (IndexOutOfBoundsException e)
        {
            System.err.println ("[IDGeneration::getCount] ** IndexOutOfBoundsException: Trying to Extract Count: biospecID = '" + biospecID + "'");
            e.printStackTrace(System.err);
        }
        return str;
    };

    //--------------------------------------------------------------------------
    // Get Biospecimen ID
    // Function attempts to:
    // 1. Query the database for the last (max) record in ix_biosepcimen
    // 2. Return the str BiospecimenID for the last record in ix_biospecimen
    //--------------------------------------------------------------------------
    private static String getBiospeciemID () throws IDGenerationDatabaseQueryException
    {
        String reqdStrBioID = new String();
        try
        {
            // Find the Max ("BIOSPECIMENKEY");
            DALQuery queryMaxPatID = new DALQuery ();
            queryMaxPatID.clearDomains();
            queryMaxPatID.clearFields();
            queryMaxPatID.clearWhere();
            queryMaxPatID.setDomain("BIOSPECIMEN", null, null, null);
            queryMaxPatID.setMaxField ("BIOSPECIMEN_intBiospecimenID");
            java.sql.ResultSet rs = queryMaxPatID.executeSelect();
            
            if (rs.next())
            {
                // Find the "BIOSPECIMENID" for the MAX ("BIOSPECIMENKEY")
                String reqdIntBioID = rs.getString ("MAX_BIOSPECIMEN_intBiospecimenID");
                // System.err.println ("[IDGeneration::getBiospecimenID] reqdIntBioID '" + reqdIntBioID + "'");
                
                queryMaxPatID.clearDomains();
                queryMaxPatID.clearFields();
                queryMaxPatID.clearWhere();
                queryMaxPatID.clearMaxFields();
                queryMaxPatID.setDomain("BIOSPECIMEN", null, null, null);
                queryMaxPatID.setField("BIOSPECIMEN_strBiospecimenID", null);
                queryMaxPatID.setWhere (null, 0, "BIOSPECIMEN_intBiospecimenID", "=", reqdIntBioID, 0, DALQuery.WHERE_HAS_VALUE);
                rs = queryMaxPatID.executeSelect();
                
                if (rs.next())
                {
                    reqdStrBioID = rs.getString ("BIOSPECIMEN_strBiospecimenID");
                    // System.err.println ("[IDGeneration::getBiospecimenID] reqdStrBioID '" + reqdStrBioID + "'");
                }
            }

        }
        catch (Exception e)
        {
            System.err.println ("[IDGeneration::getBiospecimenID] *** Exception when attempting database queries! ");
            e.printStackTrace();
            throw new IDGenerationDatabaseQueryException();
        }        
        return reqdStrBioID;
    };
    
    //--------------------------------------------------------------------------
    // Get Biospecimen ID
    // Function attempts to:
    // 1. Query the database for the last (max) record in ix_biosepcimen
    // 2. Return the str BiospecimenID for the last record in ix_biospecimen
    //--------------------------------------------------------------------------
    private static Vector getBiospecimenIDs (int intNoOfIDs) throws IDGenerationDatabaseQueryException
    {
        Vector vtBiospecimenIDs = new Vector(10, 5);
        try
        {
            // Find the Max ("BIOSPECIMENKEY");
            DALQuery queryMaxPatID = new DALQuery ();
            queryMaxPatID.clearDomains();
            queryMaxPatID.clearFields();
            queryMaxPatID.clearWhere();
            queryMaxPatID.setDomain("BIOSPECIMEN", null, null, null);
            queryMaxPatID.setMaxField ("BIOSPECIMEN_intBiospecimenID");
            java.sql.ResultSet rs = queryMaxPatID.executeSelect();
            
            if (rs.next())
            {
                // Find the "BIOSPECIMENID" for the MAX ("BIOSPECIMENKEY")
                int reqdIntBioID = rs.getInt ("MAX_BSPC_intBiospecID");
                // System.err.println ("[IDGeneration::getBiospecimenID] reqdIntBioID '" + reqdIntBioID + "'");
                
                for (int i=0; i < intNoOfIDs; i++)
                {
                    reqdIntBioID++;
                    vtBiospecimenIDs.add("" + reqdIntBioID);
                }
            }

            return vtBiospecimenIDs;
        }
        catch (Exception e)
        {
            System.err.println ("[IDGeneration::getBiospecimenIDs] *** Exception when attempting database queries! ");
            e.printStackTrace();
            throw new IDGenerationDatabaseQueryException();
        }        
    };

    //--------------------------------------------------------------------------
    // Generate Next Biospecimen ID
    // Function will take in the current biospecimen ID (currentID) and attempt to
    // generate a new biospecimen ID. If problems are encountered when reading the
    // current patient ID, the function will reset the counter and return a default
    // count.
    //--------------------------------------------------------------------------
    private static String generateNextBiosepcimenID (String currentID)
    {
        // Define the Timezone
        java.util.TimeZone tz = java.util.TimeZone.getTimeZone (neuragenix.utils.IDGeneration.SYSTEM_TIMEZONE);
        
        // Locale information obtaind from:
        
        // Define the locality information
        java.util.Locale loc = new java.util.Locale (neuragenix.utils.IDGeneration.SYSTEM_LANGUAGE, neuragenix.utils.IDGeneration.SYSTEM_COUNTRY);

        // Setup the calender
        java.util.Calendar theCalendar = java.util.Calendar.getInstance(tz,loc);

        // get the year
        String shortYear = new String (Integer.toString(theCalendar.get (java.util.Calendar.YEAR)).substring(2));
        // System.err.println ("[IDGEneration::genNxtBioID] Short Year = '" + shortYear + "'");
        
        // check to see if the year has changed
        if (!shortYear.equals(year))
        {
            // year has changed update year and restart counter
            year = shortYear;
            restartBiospecimenIDCounter();
            // System.err.println ("[IDGEneration::genNxtBioID] Updating Year: year = '" + year + "'");
            // System.err.println ("[IDGEneration::genNxtBioID] Restarting Counter: biospecimenIDCount = '" + biospecimenIDCount + "'");
        }
        
        // increment the BiospecimenID counter
        incrementBiospecimenIDCounter();
        
        String newID = year + "-" + biospecimenIDCount;
        
        // System.err.println ("[IDGEneration::genNxtBioID] newID = '" + newID + "'");
        
        return newID;
    };
    
    // Function restarts the BiospeciemID Counter
    private static void restartBiospecimenIDCounter()
    {
        biospecimenIDCount = "0";
    };

    // Function restarts the PatientID Counter
    private static void restartPatientIDCounter()
    {
        patientIDCount = "0";
    };

    // Function increments the BiospeciemID Counter
    private static void incrementBiospecimenIDCounter()
    {
        try
        {
            // convert the String to an int
            int temp = Integer.parseInt(biospecimenIDCount);
            // increment the int
            temp++;
            // convert the int to a string
            biospecimenIDCount = Integer.toString(temp);
        }
        catch (NumberFormatException e)
        {
            System.err.println ("[IDGeneration::incrementBioIDCounter] Biospecimen ID Number Format Exception: resetting counter.");
            restartBiospecimenIDCounter();
        }
    };

    // Function increments the PatientID Counter
    private static void incrementPatientIDCounter()
    {
        try
        {
            // convert the String to an int
            int temp = Integer.parseInt(patientIDCount);
            // increment the int
            temp++;
            // convert the int to a string
            patientIDCount = Integer.toString(temp);
        }
        catch (NumberFormatException e)
        {
            System.err.println ("[IDGeneration::incrementPatientIDCounter] Patient ID Number Format Exception: resetting counter.");
            restartPatientIDCounter();
        }
    };
};
