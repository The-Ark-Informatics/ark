/*
 * PatientUtiltities.java
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
import neuragenix.utils.*;
import java.util.Vector;
import java.sql.ResultSet;
import org.jasig.portal.services.LogService;
import java.lang.StringBuffer;
import neuragenix.security.AuthToken;


public class PatientUtilities
{
   
   /** Creates a new instance of PatientUtiltities */
   public PatientUtilities()
   {
   }
   
   
   /**
    *
    *  gets the patient key associated with a particular biospecimen
    *  Author : Daniel Murley
    *  Added : 9 Dec 2004
    *
    *
    *  @param strInternalBiospecimenKey Internal (Primary Key) for the biospecimen
    *  @return Internal Patient Key
    */
   
   public static int getPatientKey(int intBiospecimenKey)
   {
      return getPatientKey(intBiospecimenKey, null);
   }
   
   public static int getPatientKey(int intBiospecimenKey, DALSecurityQuery dalQuery)
   {
      if (intBiospecimenKey < 1)
         return -1;
      
      DALSecurityQuery query = null;
      
      if (dalQuery != null)
         query = dalQuery;
      else
         query = new DALSecurityQuery();
      
      
      ResultSet rs = null;
      
      try
      {
         query.reset();
         query.setDomain("BIOSPECIMEN", null, null, null);
         query.setField("BIOSPECIMEN_intPatientID", null);
         query.setDistinctField("BIOSPECIMEN_intPatientID");
         query.setWhere(null, 0, "BIOSPECIMEN_intBiospecimenID", "=", intBiospecimenKey + "", 0, DALQuery.WHERE_HAS_VALUE);
         rs = query.executeSelect();
      }
      catch (Exception e)
      {
         System.err.println("[Patient Utilities (Non-Critical)] Unable to produce patient details XML.  Unable to execute query. See portal log for details.");
         LogService.instance().log(LogService.ERROR, "[Biospecimen Channel (Non-Critical)] Unable to get patient details for sidebar.  Unable to execute query. \n" + e.getMessage());
      }
      
      try
      {
         int intPatientID = -1;
         if (rs.first() == true)
         {
            intPatientID = rs.getInt("BIOSPECIMEN_intPatientID");
         }
         else
         {
            System.err.println("[Biospecimen Channel (Non-Critical)] Unable to get patient details for sidebar - Unable to read result set.");
            LogService.instance().log(LogService.ERROR, "[Biospecimen Channel (Non-Critical)] Unable to get patient details for sidebar.  Unable to read result set");
         }
         rs.close();
         return intPatientID;
      }
      catch (Exception e)
      {
         System.err.println("[Biospecimen Channel (Non-Critical)] Unable to get patient details for sidebar. See Portal log for details.");
         LogService.instance().log(LogService.ERROR, "[Biospecimen Channel (Non-Critical)] Unable to get patient details for sidebar.\n" + e.getMessage());
      }
      
      return -1;
   }
   
   public static String getPatientDetailsXML(String intPatientID, AuthToken authToken)
   {
      StringBuffer patientXML = new StringBuffer();
      
      try
      {
         if (authToken.hasActivity("patient_view"))
         {
            Vector formfields = DatabaseSchema.getFormFields( "cbiospecimen_patient_details" );
            patientXML.append(QueryChannel.buildFormLabelXMLFile(formfields));
            patientXML.append(QueryChannel.buildViewFromKeyXMLFile(formfields,"PATIENT_intInternalPatientID",intPatientID, authToken));
         }
         return patientXML.toString();
      }
      catch(Exception e)
      {
         e.printStackTrace();
         return patientXML.toString();
      }
   }
   
   public static String getPatientEncountersXML(int intInternalPatientID)
   {
      StringBuffer sbEncounterXML = new StringBuffer();
      Vector vtEncounterField = DatabaseSchema.getFormFields("cbiospecimen_add_biospecimen_encounter");
      
      // Get the the list of encounters linked to the patient
      DALQuery encounterQuery = new DALQuery();
      try
      {
         encounterQuery.setDomain("ADMISSIONS", null, null, null);
         encounterQuery.setFields(vtEncounterField, null);
         encounterQuery.setWhere(null, 0, "ADMISSIONS_intPatientID", "=", intInternalPatientID + "", 0, DALQuery.WHERE_HAS_VALUE);
         encounterQuery.setWhere("AND", 0, "ADMISSIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         encounterQuery.setOrderBy("ADMISSIONS_strAdmissionID", "ASC");
         ResultSet rsBiospecimenEncounterList = encounterQuery.executeSelect();
         
         sbEncounterXML.append(QueryChannel.buildSearchXMLFile("search_encounter_list", rsBiospecimenEncounterList, vtEncounterField));
         rsBiospecimenEncounterList.close();
      }
      catch (Exception e)
      {
         e.printStackTrace(System.err);
      }
      return sbEncounterXML.toString();
   }
   
   public static int getPatientKeyFromUserID(String strPatientID)
   {
      DALQuery query = new DALQuery();
      ResultSet rs;
      
      try
      {
         query.setDomain("PATIENT", null, null, null);
         query.setField("PATIENT_intInternalPatientID", null);
         query.setWhere(null, 0, "PATIENT_strPatientID", "=", strPatientID, 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "PATIENT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         query.setCaseSensitive(false);
         rs = query.executeSelect();
         if (rs.first())
         {
            int returnValue = rs.getInt("PATIENT_intInternalPatientID");
            rs.close();
            return returnValue;
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         System.err.println("[CBiospecimen] :: There was an error getting internal patient key");
      }
      return -1;
      
   }
   
   public static boolean checkForAdmissions(int intPatientKey)
   {
      DALQuery query = new DALQuery();
      
      
      try
      {
         query.setDomain("ADMISSIONS", null, null, null);
         query.setCountField("ADMISSIONS_intPatientID", false);
         query.setWhere(null, 0, "ADMISSIONS_intPatientID", "=", intPatientKey + "", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "ADMISSIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         
         ResultSet rs = query.executeSelect();
         
         int admissionCount = 0;
         
         if (rs.first())
         {
            admissionCount = rs.getInt(1);
         }
         
         rs.close();
         rs = null;
         
         if (admissionCount > 0)
         {
            return true;
         }
         else
         {
            return false;
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }
   }
   
   public static int getConsentKey(int internalPatientID)
   {
      DALQuery query = new DALQuery();
      int returnValue = -1;
      try
      {
         query.setDomain("CONSENT", null, null, null);
         query.setField("CONSENT_intConsentKey", null);
         query.setWhere(null, 0, "CONSENT_intInternalPatientID", "=", internalPatientID+"", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "CONSENT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         ResultSet rs = query.executeSelect();
         
         if (rs.first())
         {
            returnValue = rs.getInt("CONSENT_intConsentKey");
         }
         rs.close();
         
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return returnValue;
      
   }
   
}
