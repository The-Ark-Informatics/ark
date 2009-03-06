/*
 * SmartformAttacher.java
 *
 * Created on 17 May 2005, 23:59
 */

package neuragenix.bio.study;

/**
 *
 * This threaded class serves to add all the data for the smartform participants
 *
 * @author  Daniel Murley <a href="mailto:dmurley@neuragenix.com">dmurley@neuragenix.com</a>
 */

import java.lang.Thread;
import neuragenix.dao.*;
import neuragenix.security.AuthToken;
import java.sql.ResultSet;
import neuragenix.common.Utilities;
import java.util.Hashtable;

public class SmartformAttacher extends Thread
{
   
   private AuthToken authToken = null;
   private int intStudyKey = -1;
   private int intSmartformKey = -1;
   
   
   
   
   /** Creates a new instance of SmartformAttacher */
   public SmartformAttacher(AuthToken authToken, int intStudyKey, int intSmartformKey)
   {
      this.authToken = authToken;
      this.intStudyKey = intStudyKey;
      this.intSmartformKey = intSmartformKey;
      
   }
   
   
   private void doParticipantInsert() throws Exception
   {
      
      DALQuery dsqGetSmartformKeys = new DALQuery();
      dsqGetSmartformKeys.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
      dsqGetSmartformKeys.setField("SMARTFORMPARTICIPANTS_intSmartformParticipantID", null);
      dsqGetSmartformKeys.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intSmartformID", "=", intSmartformKey + "", 0, DALQuery.WHERE_HAS_VALUE);
      dsqGetSmartformKeys.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
      
      // check if the smartform is already attached to this patient
      DALQuery dsqCheckForm = new DALQuery();
      dsqCheckForm.setDomain("PATIENT_SMARTFORM", null, null, null);
      dsqCheckForm.setField("PATIENT_SMARTFORM_intInternalPatientID", null);
      dsqCheckForm.setWhere(null, 0, "PATIENT_SMARTFORM_intStudyID", "=", intStudyKey + "", 0, DALQuery.WHERE_HAS_VALUE);
      dsqCheckForm.setWhere("AND", 0, "PATIENT_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
      dsqCheckForm.setWhere("AND", 0, "PATIENT_SMARTFORM_intSmartformKey", "IN", dsqGetSmartformKeys, 0, DALQuery.WHERE_HAS_SUB_QUERY);
      
      
      // find all the patients consented to this study and dont have an entry in the above query
      DALSecurityQuery dsqQuery = new DALSecurityQuery();
      dsqQuery.setDomain("CONSENT", null, null, null);
      dsqQuery.setDomain("CONSENTSTUDY", "CONSENT_intConsentKey", "CONSENTSTUDY_intConsentKey", "INNER JOIN");
      dsqQuery.setField("CONSENT_intInternalPatientID", null);
      dsqQuery.setWhere(null, 0, "CONSENT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
      dsqQuery.setWhere("AND", 0, "CONSENTSTUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
      dsqQuery.setWhere("AND", 0, "CONSENTSTUDY_intStudyID", "=", intStudyKey + "", 0, DALQuery.WHERE_HAS_VALUE);
      dsqQuery.setWhere("AND", 0, "CONSENT_intInternalPatientID", "NOT IN", dsqCheckForm, 0, DALQuery.WHERE_HAS_SUB_QUERY);
      
      ResultSet rs = dsqQuery.executeSelect();
      
      
      
      DALSecurityQuery dsqInsert = new DALSecurityQuery();
      dsqInsert.setManualCommit(true);
      
      
      
      Hashtable htSmartformData = new Hashtable(10);
      htSmartformData.put("SMARTFORMPARTICIPANTS_strDomain", "Study");
      htSmartformData.put("SMARTFORMPARTICIPANTS_strSmartformStatus", "Not started");
      htSmartformData.put("SMARTFORMPARTICIPANTS_strAddedBy",authToken.getUserIdentifier());
      htSmartformData.put("SMARTFORMPARTICIPANTS_dtDateAdded", Utilities.getDateTimeStampAsString( "dd/MM/yyyy"));
      htSmartformData.put("SMARTFORMPARTICIPANTS_intSmartformID", intSmartformKey + "");
      
      boolean failure = false;
      
      while (rs.next()) // cycle through all the patients
      {
         String strCurrentPatientID = rs.getString("CONSENT_intInternalPatientID");
         
         // insert the smartform into ix_smartform_participants
         
         dsqInsert.reset();
         
         htSmartformData.put("SMARTFORMPARTICIPANTS_intParticipantID", strCurrentPatientID);
         
         dsqInsert.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
         dsqInsert.setFields(DatabaseSchema.getFormFields("cpatient_view_smartform_participant"), htSmartformData);
         
         if (dsqInsert.executeInsert() == false)
         {
            dsqInsert.cancelTransaction();
            failure = true;
            break;
         }
         
      
         int intSmartformParticipantKey = dsqInsert.getInsertedRecordKey();
         dsqInsert.reset();
         
         dsqInsert.setDomain( "PATIENT_SMARTFORM", null, null, null);
         dsqInsert.setField("PATIENT_SMARTFORM_intInternalPatientID", strCurrentPatientID);
         dsqInsert.setField("PATIENT_SMARTFORM_intStudyID", intStudyKey + "");
         dsqInsert.setField("PATIENT_SMARTFORM_intSmartformKey", intSmartformParticipantKey + "");
      
         if (dsqInsert.executeInsert() == false)
         {
            dsqInsert.cancelTransaction();
            failure = true;
            break;
         }
         
      }
      
      rs.close();
      rs = null;
      
      
      if (failure == false)
      {
         // commit the transaction
         dsqInsert.commitTransaction();
         System.err.println ("[Study Smartform Attacher Process] Processing was successfully completed");
      }
      else
      {
         System.err.println("[Study Smartform Attacher Process] Processing failed - no smartforms were attached");
      }
      
   }
   
   
   
   public void run()
   {
      try
      {
         doParticipantInsert();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      
   }
   
   
}
