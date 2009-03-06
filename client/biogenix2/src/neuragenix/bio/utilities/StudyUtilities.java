/*
 * StudyUtilities.java
 *
 * Created on 16 March 2005, 11:19
 */

package neuragenix.bio.utilities;

/**
 * $Id: StudyUtilities.java,v 1.14 2005/11/03 04:18:41 sparappat Exp $
 * $Name:  $
 * @author  dmurley
 */

import java.util.Date;
import neuragenix.bio.study.Study;
import neuragenix.dao.*;
import neuragenix.common.*;
import neuragenix.genix.config.SystemConfiguration;
import neuragenix.utils.*;
import neuragenix.bio.utilities.PatientUtilities;
import java.util.Vector;
import java.sql.ResultSet;

public class StudyUtilities
{
   
   /** Creates a new instance of StudyUtilities */
   public StudyUtilities()
   {
   }
   
   public static final int DOMAIN_BIOSPECIMEN = 0;
   public static final int DOMAIN_PATIENT = 1;
   
   
   
   
   public static String getStudyName(int intStudyID)
   {
      DALSecurityQuery query = new DALSecurityQuery();
      
      try
      {
         query.setDomain("STUDY", null, null, null);
         query.setField("STUDY_strStudyName", null);
         query.setWhere(null, 0, "STUDY_intStudyID", "=", intStudyID + "", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         ResultSet rs = query.executeSelect();
         String strReturnString = "";
         if (rs.first())
         {
            strReturnString = rs.getString("STUDY_strStudyName");
            
         }
         rs.close();
         return strReturnString;
      }
      catch (Exception e)
      {
         System.err.println("[Study Utilities] There was an error getting the study name");
         e.printStackTrace(System.err);
         return "";
      }
   }
   
   
   public static String getListOfCurrentStudiesXML()
   {
      return getListOfStudiesXML(false);
   }
   
   public static String getListOfStudiesXML(boolean showExpiredStudies)
   {
      return getListOfStudiesXML(showExpiredStudies, true, -1, -1, null);
   }
   
   
   /**
    *  This function returns a list of studies in XML format
    *
    *  @param showExpiredStudies - when true, shows expired studies as well
    *  @param showNonConsented - when true, shows studies which have not been consented to
    *  @param intDomainID - Primary key value for the particular domain with which we're interested in
    *  @param domain - Biospecimen or Patient domain, as reference for intDomainID
    *
    */
   
   public static String getListOfStudiesXML(boolean showExpiredStudies, boolean showNonConsented, int intDomainID, int domain, String strSuffix)
   {
      // DALSecurityQuery studyQuery = new DALSecurityQuery();
      DALQuery studyQuery = new DALQuery();
      Vector vtFormFields = DatabaseSchema.getFormFields("cbiospecimen_build_study");
      
      try
      {
         studyQuery.setWhere(null, 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         
         if (showNonConsented == false)
         {
            int intInternalPatientID = -1;
            
            switch (domain)
            {
               case DOMAIN_PATIENT:
                  intInternalPatientID = intDomainID;
                  break;
               case DOMAIN_BIOSPECIMEN:
                  // get the patient key for this biospecimen
                  intInternalPatientID = PatientUtilities.getPatientKey(intDomainID);
                  break;
            }
            
            if (intInternalPatientID != -1)
            {
               studyQuery.setDomain("PATIENT", null, null, null);
               studyQuery.setDomain("CONSENT", "PATIENT_intInternalPatientID", "CONSENT_intInternalPatientID", "INNER JOIN");
               studyQuery.setDomain("CONSENTSTUDY", "CONSENT_intConsentKey", "CONSENTSTUDY_intConsentKey", "INNER JOIN");
               studyQuery.setDomain("STUDY", "CONSENTSTUDY_intStudyID", "STUDY_intStudyID", "INNER JOIN");
               studyQuery.setWhere("AND",0, "CONSENTSTUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
               studyQuery.setWhere("AND", 0, "PATIENT_intInternalPatientID", "=", intInternalPatientID + "", 0, DALQuery.WHERE_HAS_VALUE);
            }
         }
         else
         {
            studyQuery.setDomain("STUDY", null, null, null);
         }
         
         studyQuery.setFields(vtFormFields, null);
         studyQuery.setField("STUDY_dtStudyEndDate", null);
         
         if (showExpiredStudies == false)
         {
            studyQuery.setWhere("AND", 1, "STUDY_dtStudyEndDate", ">=", Utilities.getDateTimeStampAsString("dd/MM/yyyy"), 0, DALQuery.WHERE_HAS_VALUE);
            studyQuery.setWhere("OR", 0, "STUDY_dtStudyEndDate", "IS NULL", "IS NULL", 1, DALQuery.WHERE_HAS_NULL_VALUE);
         }
         
       studyQuery.setOrderBy("STUDY_intStudyID","ASC");   
         ResultSet rsStudyQuery = studyQuery.executeSelect();
         
         String strReturn = "";
         if (strSuffix == null)
         {
            strReturn = buildStudySearchXMLFile("study_list", rsStudyQuery, vtFormFields);
         }
         else
         {
            strReturn = buildStudySearchXMLFile("study_list_" + strSuffix, rsStudyQuery, vtFormFields);
         }
         
         // Default Study Enhancement
         String strTempStudy = SystemConfiguration.getConfigValue("DEFAULTSTUDY");
         
         if (strTempStudy != null && (!strTempStudy.equals("")))
         {
            strReturn = strReturn + "<default_system_study>" + strTempStudy + "</default_system_study>";
         }
         
         return strReturn;
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return "";
      }
   }
   
  
   
   public static Study getStudy(int intStudyKey)
   {
      DALQuery query = new DALQuery();
      Vector vtFields = DatabaseSchema.getFormFields("cstudy_study_view_study");
      
      try
      {
         query.setDomain("STUDY", null, null, null);
         query.setFields(vtFields, null);
         query.setWhere(null, 0, "STUDY_intStudyID", "=", intStudyKey + "", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         ResultSet rs = query.executeSelect();
         
         Study tempStudy = new Study();
         tempStudy.setStudyKey(rs.getInt("STUDY_intStudyID"));
         tempStudy.setStudyName(rs.getString("STUDY_strStudyName"));
         tempStudy.setStudyOwner(rs.getString("STUDY_strStudyOwner"));
         tempStudy.setStudyCode(rs.getString("STUDY_strStudyCode"));
         tempStudy.setStudyDescription(rs.getString("STUDY_strStudyDescription"));
         tempStudy.setStartDate(rs.getDate("STUDY_dtStudyStartDate"));
         tempStudy.setEndDate(rs.getDate("STUDY_dtStudyEndDate"));
         
         rs.close();
         return tempStudy;
         
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return null;
      }
      
   }
   
   public static synchronized boolean updateStudy(Study study)
   {
      DALQuery query = new DALQuery();
      Vector vtFields = DatabaseSchema.getFormFields("cstudy_study_view_study");
      boolean newInsert = false;
      boolean updateResult = false;
      
      
      if (study == null)
      {
         return false;
      }
      
      if (study.getStudyKey() == -1)
      {
         newInsert = true;
      }
      try
      {
         query.setDomain("STUDY", null, null, null);
         
         query.setField("STUDY_strStudyDescription", study.getStudyDescription());
         query.setField("STUDY_strStudyName", study.getStudyName());
         query.setField("STUDY_strStudyCode", study.getStudyCode());
         if (study.getEndDate() != null)
         {
            query.setField("STUDY_dtStudyEndDate", Utilities.convertDateForDAL(study.getEndDate()));
         }
         
         query.setField("STUDY_strStudyOwner", study.getStudyOwner());
         if (study.getStartDate() != null)
         {
            query.setField("STUDY_dtStudyStartDate", Utilities.convertDateForDAL(study.getStartDate()));
         }
         
         if (study.getTargetPatientNumber() > 0)
         {
            query.setField("STUDY_intTargetPatientNumber", study.getTargetPatientNumber() + "");
         }
         
         
         
         if (newInsert == false)
         {
            query.setWhere(null, 0, "STUDY_intStudyID", "=", study.getStudyKey() + "", 0, DALQuery.WHERE_HAS_VALUE);
            updateResult = query.executeUpdate();
         }
         else
         {
            updateResult = query.executeInsert();
            if (updateResult == true)
            {
               // update the reference object with the key
               study.setStudyKey(query.getInsertedRecordKey());
            }
         }
         
         
      }
      catch (Exception e)
      {
         e.printStackTrace();
         return false;
      }
      
      return updateResult;
   }
   /**
    *
    *  Consents a patient to a particular study
    *  Assumes the patient already has a consent record
    *  Does not add the smartforms
    *
    */
   
   public static void consentPatientToStudy(int intStudyKey, int intPatientKey, int intConsentKey)
   {
      DALQuery query = new DALQuery();
      try
      {
         query.setDomain( "CONSENTSTUDY", null, null, null );
         query.setField("CONSENTSTUDY_intStudyID", intStudyKey + "");
         query.setField("CONSENTSTUDY_strApproved", "Yes");
         query.setField("CONSENTSTUDY_intConsentKey", intConsentKey + "");
         query.setField("CONSENTSTUDY_dtApprovedDate", Utilities.convertDateForDAL(new Date(System.currentTimeMillis())));
         query.setField("CONSENTSTUDY_strComments", "Automatically consented by Biogenix");
         query.executeInsert();
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
   }
   
   public static int getPatientConsentKey(int intPatientKey)
   {
      DALQuery query = new DALQuery();
      try
      {
         query.setDomain("CONSENT", null, null, null);
         query.setField("CONSENT_intConsentKey", null);
         query.setWhere(null, 0,  "CONSENT_intInternalPatientID", "=", intPatientKey + "", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "CONSENT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         
         ResultSet rs = query.executeSelect();
         int intConsentKey = -1;
         
         if (rs.first())
         {
            intConsentKey = rs.getInt("CONSENT_intConsentKey");
         }
         
         rs.close();
         
         return intConsentKey;
         
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      return -1;
   }
   
   /** Build XML string for search result
    */
   public static String buildStudySearchXMLFile(String strSearchTag, ResultSet rsResultSet, Vector vtFields) 
   {
      StringBuffer strXML = new StringBuffer();
      int intNoOfFields = vtFields.size();
      String strTempData = null;
      
      
      try
      {
         while (rsResultSet.next())
         {
            //strXML += "<" + strSearchTag + ">";
            strXML.append("<" + strSearchTag + ">");
            for (int i=0; i < intNoOfFields; i++)
            {
               String strFieldName = (String) vtFields.get(i);
               strTempData = rsResultSet.getString(strFieldName);
               
               if (strFieldName.equals ("STUDY_strStudyName"))
               {    
                   if (strTempData != null)
                   {    
                       String strExpired = "0";
                       String strExpDate = rsResultSet.getString("STUDY_dtStudyEndDate");
                       
                       // Set Expired next to the study name for all the studies that have ended based on today's date
                       if (strExpDate != null && !strExpDate.equals("") && Utilities.getDateDifference( Utilities.convertDateForDisplay(strExpDate) , Utilities.getDateTimeStampAsString("dd/MM/yyyy"), "dd/MM/yyyy") < 0)
                       {
                          strTempData += "   ===Expired===";
                          strExpired = "1";
                       }    

                       strXML.append("<" + strFieldName + " expired=\"" + strExpired + "\">");               
                       strXML.append(Utilities.cleanForXSL(strTempData));
                       strXML.append("</" + strFieldName + ">");
                   }
               }
               else
               {
                   strXML.append("<" + strFieldName + ">");                     
                   strXML.append(Utilities.cleanForXSL(strTempData));
                   strXML.append("</" + strFieldName + ">");
               }    
               
            }
            
            strXML.append("</" + strSearchTag + ">");
         }
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }

      return strXML.toString();
   }   
}
