/*
 * CCIAUtilities.java
 *
 * Created on 18 May 2005, 16:47
 */

package neuragenix.bio.utilities;

import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.utils.*;
import org.jasig.portal.ChannelRuntimeData;
import java.util.*;
import java.sql.ResultSet;
import java.text.SimpleDateFormat;
import org.jasig.portal.services.LogService;

/**
 *
 * @author  navin
 */
public class CCIAUtilities {
    
    /** Creates a new instance of CCIAUtilities */
    public CCIAUtilities() {
    }
    
    /*
     *  converts Dates in the hshtable to dd-mm-yyyy format and returns the hashtable
     *
     */
    public static Hashtable convertToAusDateFormat (Hashtable hshResults)
    {
        Hashtable hshNewResults = hshResults;
        /*
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = Calendar.getInstance();
        String day, month, year, newDate;
        DBField field;
        
        try 
        {
            // LogService.log(LogService.ERROR, "convertToAusDateFormat] Entered Function");
            String keyName, valueName;
            Enumeration keys = hshNewResults.keys();
            while ( keys.hasMoreElements() )
            {
                keyName = (String) keys.nextElement();
                valueName = (String) hshNewResults.get(keyName);
                
                // get the field
                // field = (DBField) DatabaseSchema.getFields().get(keyName);
                
                // If dealing with a date then change it to Australian format dd-mm-yyyy
                // if (field.getDataType() == DBMSTypes.DATE_TYPE)
		if (keyName.indexOf("_dt") > 0)
                {
                    // LogService.log(LogService.ERROR, "Need to Change: Key: '" +keyName + "' has '" + valueName + "'.");
                    
                    // Define cal based on the value in the hash table
                    cal.setTime(sdf.parse(valueName));
                    
                    // extract day/month/year variables
                    day = String.valueOf(cal.get(cal.DAY_OF_MONTH));
                    if ((cal.get(cal.MONTH)+1) < 10)
                        month = "0" + String.valueOf((cal.get(cal.MONTH)+1));
                    else
                        month = String.valueOf((cal.get(cal.MONTH)+1));
                    year = String.valueOf(cal.get(cal.YEAR));
                    newDate = day + "-" + month + "-" + year;
                    // LogService.log(LogService.ERROR, "Date is '" + newDate + "'");
                    
                    // remove exsisting entry and add new entry based on newDate format
                    hshNewResults.remove(keyName);
                    hshNewResults.put(keyName, newDate);
                }
            }
        }
        catch (Exception e)
        {
            System.err.println("[CCIAUtilities::convertToAusDateFormat] Exception Caught.");
            e.printStackTrace(System.err);
        }*/
        return hshNewResults;
    }
    
    /*
     *  updates the runtimeData for a new CCIA field ClinicalAge
     *
     */
    public static void calculateClinicalAge (Vector vtSaveBiospecimenFields, ChannelRuntimeData runtimeData)
    {
        // LogService.log(LogService.ERROR, "calculateClinicalAge] Entered Function");
        String strPatientId = new String();
        String strCollectionDate = new String();
        String strDiagnosisDate = new String();
        DALQuery dqGetDiagnosisInfo = new DALQuery();
        DALQuery dqGetAdmissionIdInfo = new DALQuery();
        SimpleDateFormat sdfDAL = new SimpleDateFormat("dd/MM/yyyy");
        SimpleDateFormat sdfDB = new SimpleDateFormat("yyyy-MM-dd");
        Calendar cal = new GregorianCalendar();
        
        try
        {
            
            if (vtSaveBiospecimenFields.contains("BIOSPECIMEN_intPatientID"))
            {
                strPatientId = runtimeData.getParameter("BIOSPECIMEN_intPatientID");
            }
            // LogService.log(LogService.ERROR, "[calcClinAge] strPatientId: " + strPatientId);

            if (vtSaveBiospecimenFields.contains("BIOSPECIMEN_dtSampleDate"))
            {
                strCollectionDate = runtimeData.getParameter("BIOSPECIMEN_dtSampleDate");
            }
            // LogService.log(LogService.ERROR, "[calcClinAge] strCollectionDate: " + strCollectionDate);
            
            // if a valid patientID is obtained try and define strDiagnosisDate
            if ((strPatientId != null) && (strPatientId.length()!=0))
            {
                dqGetAdmissionIdInfo.setDomain("ADMISSIONS", null, null, null);
                dqGetAdmissionIdInfo.setMinField("ADMISSIONS_intAdmissionkey");
                dqGetAdmissionIdInfo.setWhere(null, 0, "ADMISSIONS_intPatientID", "=", strPatientId, 0, DALQuery.WHERE_HAS_VALUE );                
                dqGetAdmissionIdInfo.setWhere("AND", 0, "ADMISSIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                ResultSet rsAdmKey = dqGetAdmissionIdInfo.executeSelect();
                
                String strAdmKey = "";
                if (rsAdmKey.next())
                {
                   strAdmKey = rsAdmKey.getString("MIN_ADMN_intAdmissionkey");
                }
                
                if (strAdmKey != null)
                {    
                    dqGetDiagnosisInfo.setDomain("ADMISSIONS", null, null, null);
                    dqGetDiagnosisInfo.setField("ADMISSIONS_dtDiagnosis",null);
                    dqGetDiagnosisInfo.setWhere(null, 0, "ADMISSIONS_intAdmissionkey", "=", strAdmKey, 0, DALQuery.WHERE_HAS_VALUE );

                    // LogService.log(LogService.ERROR, "[calcClinAge] About to interroage db: " + dqGetDiagnosisInfo.convertSelectQueryToString());

                    ResultSet rs = dqGetDiagnosisInfo.executeSelect();
                    if (rs.next())
                    {
                        strDiagnosisDate = rs.getString( "ADMISSIONS_dtDiagnosis" );
                    }
                    rs.close();
                    rs = null;
                }
                rsAdmKey.close();
                rsAdmKey = null;
            }
                
            // LogService.log(LogService.ERROR, "[calcClinAge] ADMISSIONS_dtDiagnosis: " + strDiagnosisDate);
            
            if ((strDiagnosisDate != null) && (strCollectionDate != null) && 
                (strDiagnosisDate.length() != 0) && (strCollectionDate.length() != 0))
            {
                // Determine DiagnosisDate using sdfDB as data coming from the database
                cal.setTime(sdfDB.parse(strDiagnosisDate));
                long lngDiagDate = cal.getTimeInMillis();

                // Determine DiagnosisDate using sdfDAL as data coming from the DAL
                cal.setTime(sdfDAL.parse(strCollectionDate));
                long lngCollectionDate = cal.getTimeInMillis();
                
                // Differance in Dates
                double difference = lngCollectionDate - lngDiagDate;
                long lngDays = Math.round((difference/(1000*60*60*24)));
                
                runtimeData.setParameter ("BIOSPECIMEN_intClinicalAge", java.lang.String.valueOf(lngDays));
                // LogService.log(LogService.ERROR, "[calcClinAge] Set runtime data, BIOSPECIMEN_intClinicalAge to '" + lngDays + "'");
            }
            
        }
        catch (Exception e)
        {
            System.err.println("[CCIAUtilities::calculateClinicalAge] Exception Caught.");
            e.printStackTrace(System.err);
        }
    }

}
