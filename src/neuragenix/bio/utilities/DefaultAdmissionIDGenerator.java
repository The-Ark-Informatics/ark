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
import java.text.*;

/**
 *
 * @author  sparappat
 */
public class DefaultAdmissionIDGenerator implements IAdmissionIDGenerator 
{
    
    private static String admissionIDCount;    
    
    /** Creates a new instance of DefaultPatientIDGenerator */
    public DefaultAdmissionIDGenerator() 
    {
        
    }
    
    public String getAdmissionID(int intSequenceKey, int intStudyKey, int intSubStudyKey, AuthToken authToken) 
    {
        String newStrAdminID = "0";
        String currentStrAdminID = "0";

        try
        {                       
            // Query the database determine the last AdmissionID for this study.
            currentStrAdminID = getMaxAdmissionID (intStudyKey,intSubStudyKey);
            // System.err.println ("[DefaultPatientIDGenerator::autoGeneratePatientID] currentStrPatID = '" + currentStrPatID + "'");

            // Determine the new PATIENTID
            DecimalFormat df = new DecimalFormat("00000");

            String studyCode = StudyUtilities.getStudyCode(intStudyKey,intSubStudyKey);
            System.err.println("Study Code is : " + studyCode);
            String nextID = generateNextAdmissionID (currentStrAdminID);
            newStrAdminID =  studyCode+df.format(Integer.parseInt(nextID));
           
            // System.err.println ("[DefaultPatientIDGenerator::autoGeneratePatientID] newStrPatID = '" + newStrPatID + "'");
            
        }
        catch (Exception e) //catch all exceptions
        {
            e.printStackTrace(System.err);            
        }
        
        return newStrAdminID;
    }
    
    public String getAdmissionIDPrefix() 
    {
        return null;
    }
    
    //--------------------------------------------------------------------------
    // Get the current maximum Admission ID
    // Function attempts to:
    // 1. Query the database for the last (max) record in ix_admissions
    // 2. Return the str AdmissionID for the last record in ix_admissions
    //--------------------------------------------------------------------------
    private static String getMaxAdmissionID (int intStudyKey, int intSubStudyKey) 
    {
        String reqdStrAdmissionID = new String();
        try
        {
            // Find the Max ("PATIENTKEY");
            DALQuery queryMaxAdminID = new DALQuery ();
            queryMaxAdminID.clearDomains();
            queryMaxAdminID.clearFields();
            queryMaxAdminID.clearWhere();
            queryMaxAdminID.setDomain("ADMISSIONS", null, null, null);
            queryMaxAdminID.setMaxField ("ADMISSIONS_strAdmissionID");
            queryMaxAdminID.setWhere(null,0,"ADMISSIONS_intStudyID", "=", ""+intStudyKey, 0, DALQuery.WHERE_HAS_VALUE);
            queryMaxAdminID.setWhere("AND",0,"ADMISSIONS_intSubStudyID","=",""+intSubStudyKey,0,DALQuery.WHERE_HAS_VALUE);
            java.sql.ResultSet rs = queryMaxAdminID.executeSelect();
            System.err.println(queryMaxAdminID.convertSelectQueryToString());
            if (rs.next())
            {
                
                reqdStrAdmissionID = rs.getString ("MAX_ADMN_strAdmissionID");
                // System.err.println ("[IDGeneration::getPatientID] reqdIntPatID '" + reqdIntPatID + "'");
                
               // queryMaxAdminID.clearDomains();
               //queryMaxAdminID.clearFields();
               // queryMaxAdminID.clearWhere();
               // queryMaxAdminID.clearMaxFields();
               // queryMaxAdminID.setDomain("ADMISSIONS", null, null, null);
               // queryMaxAdminID.setField("ADMISSIONS_strAdmissionID", null);
               // queryMaxAdminID.setWhere (null, 0, "ADMISSIONS_intAdmissionkey", "=", reqdIntAdminID, 0, DALQuery.WHERE_HAS_VALUE);
               // queryMaxAdminID.setWhere("AND",0,"ADMISSIONS_intStudyID", "=", ""+intStudyKey, 0, DALQuery.WHERE_HAS_VALUE);
              //  rs = queryMaxAdminID.executeSelect();
              // 
              //  if (rs.next())
              //  {
              //      reqdStrAdmissionID = rs.getString ("ADMISSIONS_strAdmissionID");
               //     // System.err.println ("[IDGeneration::getPatientID] reqdStrPatID '" + reqdStrPatID + "'");
               // }
            }
        }
        catch (Exception e)
        {
            e.printStackTrace();
            System.err.println ("[DefaultPatientIDGenerator::getMaxPatientID] *** Exception when obtaining maximum patient ID! ");            
        }        
        return reqdStrAdmissionID;
    };
    
    //--------------------------------------------------------------------------
    // Generate Next Patient ID
    // Function will take in the current patient ID (currentID) and attempt to
    // generate a new patient ID. If problems are encountered when reading the
    // current patient ID, the function will reset the counter and return a count
    // of zero.
    //--------------------------------------------------------------------------
    private static String generateNextAdmissionID (String currentID)
    {        
        // check to see if the patientID format is correct
        try
        {
            // if able to parse - increment should work
            // System.err.println ("[DefaultPatientIDGenerator::getPatientID] Trying to parse the string as an int. PatID: " + patientIDCount);
        	//Strip last 5 digits off admissionID - if possible - length should be 5 digits + 2 or 3 letters
        	if (currentID != null) {
        	if (currentID.length() < 7) throw new NumberFormatException(); 
            int temp = Integer.parseInt(currentID.substring(currentID.length() - 5)); //FIXME: Magic numbers here - need to remove.
            
            admissionIDCount = Integer.toString(temp).toString();
        	}
        	else
        		restartPatientIDCounter();
        }
        catch (NumberFormatException e)
        {
            // unable to parse - restart counter
            restartPatientIDCounter();
            System.err.println ("[DefaultAdmissionIDGenerator::getAdmissionID] *** Exception caught: Reinitalising Patient ID counter: '" + admissionIDCount + "'");
        }
        
        
        // increment the PatientID counter
        incrementPatientIDCounter();        
        // System.err.println ("[DefaultPatientIDGenerator::genNxtPatID] patientIDCount = '" + patientIDCount + "'");
        
        return admissionIDCount;
    };
    
    
    // Function restarts the PatientID Counter
    private static void restartPatientIDCounter()
    {
        admissionIDCount = "0";
    };


    // Function increments the PatientID Counter
    private static void incrementPatientIDCounter()
    {
        try
        {
            // convert the String to an int
            int temp = Integer.parseInt(admissionIDCount);
            // increment the int
            temp++;
            // convert the int to a string
            admissionIDCount = Integer.toString(temp);
        }
        catch (NumberFormatException e)
        {
            System.err.println ("[DefaultAdmissionIDGenerator::incrementAdmissionIDCounter] Admission ID Number Format Exception: resetting counter.");
            restartPatientIDCounter();
        }
    };
    
}
