/*
 * DefaultIDGenerator.java
 *
 * Created on 20 April 2005, 13:25
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
public class CCIAPatientIDGenerator implements IPatientIDGenerator 
{
    
    private static String patientIDCount;
    private static String prefix;
    private static int prefixLength;
    
    
    /** Creates a new instance of DefaultIDGenerator */
    public CCIAPatientIDGenerator() 
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

        //System.out.println ("The new ID is" + newStrPatID);        
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
//Temp            queryMaxPatID.setMaxField ("PATIENT_intInternalPatientID");
//Temp inserted: fix get max ID directly
            queryMaxPatID.setMaxField ("PATIENT_strPatientID");
            java.sql.ResultSet rs = queryMaxPatID.executeSelect();
            
            if (rs.next())
            {
                // Find the "BIOSPECIMENID" for the MAX ("BIOSPECIMENKEY")
//Temp                String reqdIntPatID = rs.getString ("MAX_PATN_intIntPatientID");
                // System.err.println ("[IDGeneration::getPatientID] reqdIntPatID '" + reqdIntPatID + "'");
                
//Temp                queryMaxPatID.clearDomains();
//Temp                queryMaxPatID.clearFields();
//Temp                queryMaxPatID.clearWhere();
//Temp                queryMaxPatID.clearMaxFields();
//Temp                queryMaxPatID.setDomain("PATIENT", null, null, null);
//Temp                queryMaxPatID.setField("PATIENT_strPatientID", null);
//Temp                queryMaxPatID.setWhere (null, 0, "PATIENT_intInternalPatientID", "=", reqdIntPatID, 0, DALQuery.WHERE_HAS_VALUE);
//Temp                rs = queryMaxPatID.executeSelect();
                
//Temp                if (rs.next())
//Temp                {
//Temp                    reqdStrPatID = rs.getString ("PATIENT_strPatientID");
//Temp inserted:                 
                    reqdStrPatID = rs.getString ("MAX_PATN_strPatientID");
                    // System.err.println ("[IDGeneration::getPatientID] reqdStrPatID '" + reqdStrPatID + "'");
//Temp                }
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
    public static String generateNextPatientID (String currentID)
    {
        patientIDCount ="";
        
        int temp = -1;
         
        // check to see if the patientID format is correct
        try
        {
            prefixLength = currentID.length() - 4;
            // if able to parse - increment should work
            // System.err.println ("[IDGeneration::generateNextPatientID] Trying to parse the string as an int. PatID: " + currentID.substring(prefixLength));
            temp = Integer.parseInt(currentID.substring(prefixLength));
            
            // System.err.println ("[IDGeneration::generateNextPatientID] Reseting patientIDCount: " + patientIDCount);
            patientIDCount = Integer.toString(temp).toString();

        }
        catch (Exception e)
        {
            // unable to parse - restart counter
            System.err.println ("[IDGeneration::generateNextPatientID] *** Exception caught: Reinitalising Patient ID counter: 'A0001'");
            return "A0001";
        }

        // check for overflow
        if (temp >= 9999)
        {
            // System.err.println ("[IDGEneration::generateNextPatientID] patientIDCount overflow = '" + temp + "'");
            String currentPrefix = currentID.substring (0, prefixLength);
            prefix = incrementPrefix (currentPrefix, prefixLength);
        }
        else
        {
            prefix = currentID.substring (0, prefixLength);
        }
        
        // increment the PatientID counter
        incrementPatientIDCounter();        
        // System.err.println ("[IDGEneration::generateNextPatientID] patientIDCount = '" + patientIDCount + "'");
        
        // convert to required 4 digit format
        switch (patientIDCount.length())
        {
            case 1:     patientIDCount = "000" + patientIDCount;
                        break;
            case 2:     patientIDCount = "00" + patientIDCount;
                        break;
            case 3:     patientIDCount = "0" + patientIDCount;
                        break;
            default:    // do nothing
                        break;
        }
        
        return prefix + patientIDCount;
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
            
            if (temp >= 9999)
            {
                temp = 0;
            }
            else
            {    
                // increment the int
                temp++;
            }
            // convert the int to a string
            patientIDCount = Integer.toString(temp);
        }
        catch (NumberFormatException e)
        {
            System.err.println ("[IDGeneration::incrementPatientIDCounter] Patient ID Number Format Exception: resetting counter.");
            restartPatientIDCounter();
        }
    };

    
    private static String incrementPrefix (String thePrefix, int length)
    {
        char s[] = new char[length+1];
        s[0] = '\0';
        for (int i=0; i<length; i++)
        {
            s[i] = thePrefix.charAt(i);
        }

        if (length == 0)
        {
            return "A";
        }
        else
        {
            // System.err.println ("[IDGeneration::incrementPrefix] s: " + s + "(" + length + ")");
            // check to see if overflow will happen 'Z'
            if (s[length-1] == 'Z')
            {
                for (int j=1; j<=length; j++)
                {
                    /* Loop through the characters from the last character to the first. 
                     * If the character is Z and there are no more preceding characters
                     * then change the character to A, prepend A and exit from loop. 
                     * e.g. ZZZ finally increments to AAAA
                     * If the character is Z and there is a preceding character that is not also
                     * a Z, then change the character to A, increment the preceding character 
                       and exit from the loop. eg. AZ increments to BA */
                    if (s[length-j] == 'Z')
                    {    
                        s[length-j] = 'A';
                        
                        // if there are no more characters to loop through
                        if ((length-j) == 0)
                        {
                            thePrefix = ("A" + thePrefix.copyValueOf(s));
                            break;
                        }
                        else 
                        {
                            // if the character in front of the Z is not also a Z increment and break from loop
                            // if the character in front of the Z is also Z then the next iteration of the loop
                            // will ensure correct incrementation.
                            if (s[length-j-1] != 'Z')
                            {
                                int intValue = (int) s[length-j-1];
                                intValue++;
                                s[length-j-1] = (char) intValue;
                                thePrefix = thePrefix.copyValueOf(s);
                                break;
                            }
                        }
                    }  
                }
            }
            else
            {
                int intValue = (int) s[length-1];
                intValue++;
                s[length-1] = (char) intValue;
                thePrefix = thePrefix.copyValueOf(s);
            }
            // System.err.println ("[IDGeneration::incrementPrefix] New prefix: " + thePrefix + "(" + length + ")");
        }
        
        return thePrefix.trim();
    };
    
}
