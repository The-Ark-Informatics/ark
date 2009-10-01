

package neuragenix.bio.utilities;
import neuragenix.security.AuthToken;
import neuragenix.dao.DALQuery;         // for access to the database
import java.text.*;

/**
 *
 * @author  sparappat
 */
public class DefaultPlateIDGenerator implements IInventoryIDGenerator
{
    
    private static String plateIDCount;    
    
    /** Creates a new instance of DefaultPatientIDGenerator */
    public DefaultPlateIDGenerator() 
    {
        
    }
    
    public String getInventoryID( int intStudyKey, AuthToken authToken) 
    {
        String newStrPlateID = "0";
        String currentStrPlateID = "0";

        try
        {                       
            // Query the database determine the last AdmissionID for this study.
            currentStrPlateID = getMaxPlateID (intStudyKey);
            // System.err.println ("[DefaultPatientIDGenerator::autoGeneratePatientID] currentStrPatID = '" + currentStrPatID + "'");

            // Determine the new PATIENTID
            DecimalFormat df = new DecimalFormat("000");
//TODO: Need to extend this to a generic, customisable solution here.
            String plateCode = InventoryUtilities.getPlateCode(intStudyKey);
            System.err.println("Plate Code is : " + plateCode);
            String nextID = generateNextPlateID (currentStrPlateID);
            newStrPlateID =  plateCode+df.format(Integer.parseInt(nextID));
           
            // System.err.println ("[DefaultPatientIDGenerator::autoGeneratePatientID] newStrPatID = '" + newStrPatID + "'");
            
        }
        catch (Exception e) //catch all exceptions
        {
            e.printStackTrace(System.err);            
        }
        
        return newStrPlateID;
    }
    
    public String getInventoryIDPrefix() 
    {
        return null;
    }
    
    //--------------------------------------------------------------------------
    // Get the current maximum Admission ID
    // Function attempts to:
    // 1. Query the database for the last (max) record in ix_admissions
    // 2. Return the str AdmissionID for the last record in ix_admissions
    //--------------------------------------------------------------------------
    private static String getMaxPlateID (int intStudyKey) 
    {
        String reqdStrAdmissionID = new String();
        try
        {
            // Find the Max ("PATIENTKEY");
            DALQuery queryMaxAdminID = new DALQuery ();
            queryMaxAdminID.clearDomains();
            queryMaxAdminID.clearFields();
            queryMaxAdminID.clearWhere();
            queryMaxAdminID.setDomain("TRAY", null, null, null);
            queryMaxAdminID.setMaxField ("TRAY_strTrayName");
            queryMaxAdminID.setWhere(null,0,"TRAY_intStudyID", "=", ""+intStudyKey, 0, DALQuery.WHERE_HAS_VALUE);
            queryMaxAdminID.setWhere("AND",0,"TRAY_intTrayType","=","1",0,DALQuery.WHERE_HAS_VALUE);
            java.sql.ResultSet rs = queryMaxAdminID.executeSelect();
            System.err.println(queryMaxAdminID.convertSelectQueryToString());
            if (rs.next())
            {
                
                reqdStrAdmissionID = rs.getString ("MAX_TRAY_strTrayName");
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
    private static String generateNextPlateID (String currentID)
    {        
        // check to see if the patientID format is correct
        try
        {
            // if able to parse - increment should work
            // System.err.println ("[DefaultPatientIDGenerator::getPatientID] Trying to parse the string as an int. PatID: " + patientIDCount);
        	//Strip last 5 digits off admissionID - if possible - length should be 5 digits + 2 or 3 letters
        	if (currentID != null) {
        	if (currentID.length() < 3) throw new NumberFormatException(); 
            int temp = Integer.parseInt(currentID.substring(currentID.length() - 3)); //FIXME: Magic numbers here - need to remove.
            
            plateIDCount = Integer.toString(temp).toString();
        	}
        	else
        		restartPlateIDCounter();
        }
        catch (NumberFormatException e)
        {
            // unable to parse - restart counter
            restartPlateIDCounter();
            System.err.println ("[DefaultPlateIDGenerator::generateNextPlateID] *** Exception caught: Reinitalising Plate ID counter: '" + plateIDCount + "'");
        }
        
        
        // increment the PatientID counter
        incrementPlateIDCounter();        
        // System.err.println ("[DefaultPatientIDGenerator::genNxtPatID] patientIDCount = '" + patientIDCount + "'");
        
        return plateIDCount;
    };
    
    
    // Function restarts the PatientID Counter
    private static void restartPlateIDCounter()
    {
        plateIDCount = "0";
    };


    // Function increments the PatientID Counter
    private static void incrementPlateIDCounter()
    {
        try
        {
            // convert the String to an int
            int temp = Integer.parseInt(plateIDCount);
            // increment the int
            temp++;
            // convert the int to a string
            plateIDCount = Integer.toString(temp);
        }
        catch (NumberFormatException e)
        {
            System.err.println ("[DefaultPlateIDGenerator::incrementPlateIDCounter] Plate ID Number Format Exception: resetting counter.");
            restartPlateIDCounter();
        }
    };
    
}
