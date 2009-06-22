/*
 * DefaultPatientIDGenerator.java
 *
 * Created on 24 May 2005, 13:25
 */

package neuragenix.bio.utilities;

import java.util.Vector;
import neuragenix.security.AuthToken;
import org.jasig.portal.PropertiesManager;
import neuragenix.dao.DALQuery;         // for access to the database
import java.sql.ResultSet;              // for access to data from queries

/**
 *
 * @author  sparappat
 */
public class DefaultPatientIDGenerator implements IPatientIDGenerator 
{
    
    private static String patientIDCount;    
    
    /** Creates a new instance of DefaultPatientIDGenerator */
    public DefaultPatientIDGenerator() 
    {
        
    }
    
    public String getPatientID(int intSequenceKey, AuthToken authToken) 
    {
        String newStrPatID = "0";
        String currentStrPatID = "0";

        try
        {                       
            // Query the database determine the last PATIENTID
            currentStrPatID = getMaxPatientID ();
            // System.err.println ("[DefaultPatientIDGenerator::autoGeneratePatientID] currentStrPatID = '" + currentStrPatID + "'");

            // Determine the new PATIENTID
            newStrPatID = generateNextPatientID (currentStrPatID);
            // System.err.println ("[DefaultPatientIDGenerator::autoGeneratePatientID] newStrPatID = '" + newStrPatID + "'");
            
        }
        catch (Exception e) //catch all exceptions
        {
            e.printStackTrace(System.err);            
        }
        
        return newStrPatID;
    }
    
    public String getPatientIDPrefix() 
    {
        return null;
    }
    
    //--------------------------------------------------------------------------
    // Get the current maximum Patient ID
    // Function attempts to:
    // 1. Query the database for the last (max) record in ix_patient
    // 2. Return the str PatientID for the last record in ix_patient
    //--------------------------------------------------------------------------
    private static String getMaxPatientID () 
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
            System.err.println ("[DefaultPatientIDGenerator::getMaxPatientID] *** Exception when obtaining maximum patient ID! ");            
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
            // System.err.println ("[DefaultPatientIDGenerator::getPatientID] Trying to parse the string as an int. PatID: " + patientIDCount);
            int temp = Integer.parseInt(currentID);
            
            patientIDCount = Integer.toString(temp).toString();
        }
        catch (NumberFormatException e)
        {
            // unable to parse - restart counter
            restartPatientIDCounter();
            System.err.println ("[DefaultPatientIDGenerator::getPatientID] *** Exception caught: Reinitalising Patient ID counter: '" + patientIDCount + "'");
        }
        
        // increment the PatientID counter
        incrementPatientIDCounter();        
        // System.err.println ("[DefaultPatientIDGenerator::genNxtPatID] patientIDCount = '" + patientIDCount + "'");
        
        return patientIDCount;
    };
    
    
    // Function restarts the PatientID Counter
    private static void restartPatientIDCounter()
    {
        patientIDCount = "0";
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
            System.err.println ("[DefaultPatientIDGenerator::incrementPatientIDCounter] Patient ID Number Format Exception: resetting counter.");
            restartPatientIDCounter();
        }
    };
    
}
