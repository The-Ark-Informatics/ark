/*
 * BiogenixSmartformUtilities.java
 *
 * Created on 21 March 2005, 15:44
 */



// TODO: Come up with a better name for this

package neuragenix.bio.utilities;

/**
 *
 * @author  dmurley
 */

import javax.xml.transform.Result;
import neuragenix.dao.*;
import neuragenix.security.AuthToken;
import neuragenix.common.*;
import java.sql.ResultSet;

import java.util.Vector;
import java.util.Hashtable;


import neuragenix.bio.biospecimen.BiospecimenCore;


public class SmartformManager
{
   
   /** Creates a new instance of BiogenixSmartformUtilities */
   public SmartformManager()
   {
   }
   
   
   // TODO: make static and set a return value
   public static boolean deleteSmartformParticipants(String intStudyID, String intBiospecimenID, AuthToken authToken)
   {
      boolean blReturnValue = true;
      
      try
      {
         DALSecurityQuery query = new DALSecurityQuery(BiospecimenCore.ACTION_BIOSPECIMEN_DELETE, authToken);
         DALSecurityQuery updateQuery = new DALSecurityQuery(BiospecimenCore.ACTION_BIOSPECIMEN_DELETE, authToken);
         query.setDomain("BIOSPECIMEN_SMARTFORM", null, null, null);
         query.setField("BIOSPECIMEN_SMARTFORM_intSmartformKey",null);
         query.setWhere(null, 0, "BIOSPECIMEN_SMARTFORM_intDeleted","=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         if (intStudyID != null)
         {
            query.setWhere("AND", 0, "BIOSPECIMEN_SMARTFORM_intStudyID","=", intStudyID, 0, DALQuery.WHERE_HAS_VALUE);
         }
         query.setWhere("AND", 0, "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "=", intBiospecimenID, 0, DALQuery.WHERE_HAS_VALUE);
         
         ResultSet rs = query.executeSelect();
         
         
         updateQuery.reset();
         updateQuery.setDomain("BIOSPECIMEN_SMARTFORM", null, null, null);
         updateQuery.setField( "BIOSPECIMEN_SMARTFORM_intDeleted", "-1" );
         if (intStudyID != null)
         {
            updateQuery.setWhere(null,0,"BIOSPECIMEN_SMARTFORM_intStudyID","=", intStudyID,0,DALQuery.WHERE_HAS_VALUE);
         }
         updateQuery.setWhere("AND", 0, "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "=", intBiospecimenID, 0, DALQuery.WHERE_HAS_VALUE);
         updateQuery.setWhere("AND",0,"BIOSPECIMEN_SMARTFORM_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
         
         boolean blQueryReturn = updateQuery.executeUpdate();
         if (blReturnValue != false)
            blReturnValue = blQueryReturn;
         
         while(rs.next())
         {
            
            // delete records in smartformparticipants
            String smartformParticipantKey = rs.getString("BIOSPECIMEN_SMARTFORM_intSmartformKey");
            updateQuery = null;
            updateQuery = new DALSecurityQuery(BiospecimenCore.ACTION_BIOSPECIMEN_DELETE, authToken);
            updateQuery.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
            updateQuery.setField("SMARTFORMPARTICIPANTS_intDeleted","-1");
            updateQuery.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "=", smartformParticipantKey, 0, DALQuery.WHERE_HAS_VALUE);
            
            blQueryReturn = updateQuery.executeUpdate();
            if (blReturnValue != false)
               blReturnValue = blQueryReturn;
            
            // delete records in smartformresult
            updateQuery = null;
            updateQuery = new DALSecurityQuery(BiospecimenCore.ACTION_BIOSPECIMEN_DELETE, authToken);
            updateQuery.setDomain("SMARTFORMRESULTS", null, null, null);
            updateQuery.setField("SMARTFORMRESULTS_intDeleted","-1");
            updateQuery.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", smartformParticipantKey, 0, DALQuery.WHERE_HAS_VALUE);
            
            blQueryReturn = updateQuery.executeUpdate();
            if (blReturnValue != false)
               blReturnValue = blQueryReturn;
            
         }
         rs.close();
         return blReturnValue;
      }
      catch(Exception e)
      {
         e.printStackTrace();
         return false;
      }
   }
   
   
   
   
   public static boolean addBiospecimenSmartformParticipant(int intStudyID, int intBiospecimenID, AuthToken authToken)
   {
      try
      {
         Hashtable htFieldData = new Hashtable();
         DALSecurityQuery query = new DALSecurityQuery(BiospecimenCore.ACTION_BIOSPECIMEN_ADD, authToken );
         //    formfields = DatabaseSchema.getFormFields( "view_smartforms" );
         query.setDomain("SMARTFORM", null, null, null);
         query.setDomain("STUDYSURVEY", "SMARTFORM_intSmartformID", "STUDYSURVEY_intSurveyKey", "LEFT JOIN");
         query.setField( "STUDYSURVEY_intSurveyKey",null);
         query.setWhere(null,0,"STUDYSURVEY_intStudyID","=", intStudyID + "", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "STUDYSURVEY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND",0,"SMARTFORM_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
         
         // get the smartform list
         ResultSet rs;
         rs = query.executeSelect();
         
         //  build the insert query
         
         DALSecurityQuery insertQuery = new DALSecurityQuery( BiospecimenCore.ACTION_BIOSPECIMEN_ADD, authToken );
         insertQuery.setDomain( "SMARTFORMPARTICIPANTS", null, null, null);
         
         DALSecurityQuery insertPSQuery = new DALSecurityQuery( BiospecimenCore.ACTION_BIOSPECIMEN_ADD, authToken );
         insertPSQuery.setDomain( "BIOSPECIMEN_SMARTFORM", null, null, null);
         
         // build the runtime for PATIENT_SMARTFORM
         htFieldData.put( "BIOSPECIMEN_SMARTFORM_intBiospecimenID", intBiospecimenID + "");
         htFieldData.put( "BIOSPECIMEN_SMARTFORM_intStudyID", intStudyID + "");
         
         // build the runtime data for SMARTFORMPARTICIPANTS
         htFieldData.put("SMARTFORMPARTICIPANTS_strDomain", "Biospecimen");
         htFieldData.put("SMARTFORMPARTICIPANTS_intParticipantID", intBiospecimenID + "");
         htFieldData.put("SMARTFORMPARTICIPANTS_strSmartformStatus", "Not started");
         htFieldData.put("SMARTFORMPARTICIPANTS_strAddedBy", authToken.getUserIdentifier());
         htFieldData.put("SMARTFORMPARTICIPANTS_dtDateAdded", Utilities.getDateTimeStampAsString( "dd/MM/yyyy"));
         
         
         boolean blReturnValue = true;
         while(rs.next())
         {
            // insert SMARTFORMPARTICIPANTS
            insertQuery.clearFields();
            htFieldData.put("SMARTFORMPARTICIPANTS_intSmartformID", rs.getString("STUDYSURVEY_intSurveyKey"));
            insertQuery.setFields( DatabaseSchema.getFormFields( "cbiospecimen_view_smartform_participant" ), htFieldData);
            insertQuery.executeInsert();
            String newKey = QueryChannel.getNewestKeyAsString(insertQuery);
            // insert BIOSPECIMEN_SMARTFORM
            insertPSQuery.clearFields();
            htFieldData.put("BIOSPECIMEN_SMARTFORM_intSmartformKey",newKey);
            insertPSQuery.setFields( DatabaseSchema.getFormFields( "cbiospecimen_add_biospecimen_smartform" ), htFieldData);
            boolean blQueryResult = insertPSQuery.executeInsert();
            if (blQueryResult != true)
               blReturnValue = false;
         }
         rs.close();
         return blReturnValue;
      }
      catch(Exception e)
      {
         e.printStackTrace(System.err);
         return false;
      }
      
   }
   
   
   public static boolean addStudySmartformsToPatient(int intStudyID, int intInternalPatientID, String strAddingUser)
   {
      // integerate with Smartforms.
      
      DALQuery query = new DALQuery();
      Hashtable htFieldData = new Hashtable();
      try
      {
         
         query.setDomain("SMARTFORM", null, null, null);
         query.setDomain("STUDYSURVEY", "SMARTFORM_intSmartformID", "STUDYSURVEY_intSurveyKey", "LEFT JOIN");
         query.setField( "STUDYSURVEY_intSurveyKey",null);
         query.setWhere(null,0,"STUDYSURVEY_intStudyID","=", intStudyID + "",0,DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "STUDYSURVEY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND",0,"SMARTFORM_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
         
         // get the smartform list
         ResultSet rs;
         rs = query.executeSelect();
         
         //  build the insert query
         
         DALQuery insertQuery = new DALQuery();
         insertQuery.setDomain( "SMARTFORMPARTICIPANTS", null, null, null);
         
         DALQuery insertPSQuery = new DALQuery();
         insertPSQuery.setDomain( "PATIENT_SMARTFORM", null, null, null);
         
         // build the runtime for PATIENT_SMARTFORM
         htFieldData.put( "PATIENT_SMARTFORM_intInternalPatientID", intInternalPatientID + "" );
         htFieldData.put( "PATIENT_SMARTFORM_intStudyID", intStudyID + "" );
         
         // build the runtime data for SMARTFORMPARTICIPANTS
         htFieldData.put("SMARTFORMPARTICIPANTS_strDomain","Study");
         htFieldData.put("SMARTFORMPARTICIPANTS_intParticipantID", intInternalPatientID + "");
         htFieldData.put("SMARTFORMPARTICIPANTS_strSmartformStatus","Not started");
         htFieldData.put("SMARTFORMPARTICIPANTS_strAddedBy", strAddingUser);
         htFieldData.put("SMARTFORMPARTICIPANTS_dtDateAdded", Utilities.getDateTimeStampAsString( "dd/MM/yyyy"));
         
         
         while(rs.next())
         {
            // insert SMARTFORMPARTICIPANTS
            insertQuery.clearFields();
            htFieldData.put("SMARTFORMPARTICIPANTS_intSmartformID",rs.getString("STUDYSURVEY_intSurveyKey"));
            insertQuery.setFields( DatabaseSchema.getFormFields( "cpatient_view_smartform_participant" ), htFieldData);
            insertQuery.executeInsert();
            String smartformparticipantKey = insertQuery.getInsertedRecordKey() + "";
            // insert PATIENT_SMARTFORM
            insertPSQuery.clearFields();
            htFieldData.put("PATIENT_SMARTFORM_intSmartformKey",smartformparticipantKey);
            insertPSQuery.setFields( DatabaseSchema.getFormFields( "cpatient_view_patient_smartform" ), htFieldData);
            insertPSQuery.executeInsert();
            
         }
         rs.close();
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }
      
      return true;
      
      
      
   }
   
   /**
    * checkForBiospecimenParticipant
    *
    * Looks up the ix_biospecimen_smartform table to check if a biospecimen
    * is already a participant in the smartforms
    *
    * You must pass this the specimen KEY, not ID.
    *
    * @returns true - if specimen is a participant
    *
    */
   
   public static boolean checkForBiospecimenParticipant(int intBiospecimenID, int intStudyKey)
   {
      DALQuery query = new DALQuery();
      boolean participant = false;
      
      if (intStudyKey == -1)
          return false;
      
      try
      {
         query.setDomain("BIOSPECIMEN_SMARTFORM", null, null, null);
         query.setField("BIOSPECIMEN_SMARTFORM_intBiospecimenID", null);
         query.setWhere(null, 0, "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "=", intBiospecimenID + "", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "BIOSPECIMEN_SMARTFORM_intStudyID", "=", intStudyKey + "", 0,  DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "BIOSPECIMEN_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         System.out.println("Query : " + query.convertSelectQueryToString());
         
         ResultSet rs = query.executeSelect();
         
         if (rs.first())
         {
            participant = true;
         }
         rs.close();
         
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
     
      return participant;
      
   }
   
   
   
   
   
}
