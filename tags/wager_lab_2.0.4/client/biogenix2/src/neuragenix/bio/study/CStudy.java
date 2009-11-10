/**
 * CStudy.java
 * Copyright (C) Neuragenix Pty Ltd, 2004.
 * Date:
 */

package neuragenix.bio.study;


import org.jasig.portal.IChannel;

import org.jasig.portal.ChannelStaticData;

import org.jasig.portal.ChannelRuntimeData;

import org.jasig.portal.ChannelRuntimeProperties;

import org.jasig.portal.PortalEvent;

import org.jasig.portal.PortalException;

import org.jasig.portal.utils.XSLT;

import org.xml.sax.ContentHandler;

import org.jasig.portal.security.*;

import org.jasig.portal.services.LogService;

import neuragenix.common.Utilities;

import neuragenix.common.BaseChannel;

import neuragenix.dao.DBSchema;

import java.util.Hashtable;

import java.util.Vector;

import java.util.Enumeration;

import java.util.Date;

import java.text.SimpleDateFormat;

import neuragenix.bio.patient.exception.*;

import org.jasig.portal.PropertiesManager;

import neuragenix.security.AuthToken;

import neuragenix.dao.ChannelIDs;

import neuragenix.common.LockRequest;

import neuragenix.common.LockRecord;

//Start Secure Download Implementation

import neuragenix.security.exception.SecurityException;
import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.SessionManager;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.dao.DALQuery;
import neuragenix.common.QueryChannel;
import neuragenix.common.NGXRuntimeProperties;

import java.io.InputStream;
import java.sql.*;
import java.lang.StringBuffer;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NotContextException;

//End Secure


/** This channel deals with the patient
 *
 * forms which includes appointments
 *
 *
 *
 */

public class CStudy implements IChannel
{
   
   
   
   // At the very least, you'll need to keep track of the static data and the runtime data that the portal sends you.
   
   
   
   private ChannelStaticData staticData;
   
   private ChannelRuntimeData runtimeData;
   
   
   
   // back to search result
   
   private ChannelRuntimeData studySearchRuntimeData;
   
   private ChannelRuntimeData surveySearchRuntimeData;
   
   
   
   //Locks
   
   private LockRequest lrStudyLock = null;
   
   private LockRequest lrSurveyLock = null;
   
   private LockRequest lrStudyEthicsLock = null;
   
   private LockRequest lrResultsLock = null;
   
   private LockRequest lrAllocationLock = null;
   
   // These are the page names that the value current (current page) can contain
   
   private static final String STUDY_ADD = "study_add";
   
   private static final String STUDY_SEARCH = "study_search";
   
   private static final String STUDY_RESULTS = "study_results";
   
   private static final String STUDY_VIEW = "study_view";
   
   private static final String STUDY_ALLOCATION = "study_allocation";
   
   private static final String ALLOCATION_SAVE = "save_allocation_search";
   
   private static final String ALLOCATION_DELETE = "delete_allocation_search";
   
   private static final String PRIMARY_STUDY_MESSAGE = "This is the primary study, which can not be edited or deleted";
   
   private static final String ALLOCATION_EDIT = "edit_allocation_search";
   
   private static final String ALLOCATION_UPDATE = "update_allocation_search";
   
   private static final String ALLOCATION_REFRESH = "refresh_allocation_search";
   
   private static final String ALLOCATION_SAVE_REFRESH = "refresh_save_allocation_search";
   
   private static final String SURVEY_SEARCH = "survey_search";
   
   private static final String SURVEY_ADD = "survey_add";
   
   private static final String SURVEY_VIEW = "survey_view";
   
   private static final String SURVEY_RESULTS = "survey_results";
   
   private static final String SURVEY_EDIT = "survey_edit";
   
   private static final String SURVEY_DELETE = "survey_delete";
   
   private static final String STUDY_SURVEYS = "study_surveys";
   
   private static final String SURVEY_QUESTIONS = "survey_questions";
   
   // private static final String SURVEY_ADD_QUESTIONS = "survey_add_questions";
   
   private static final String PATIENT_STUDY = "patient_study";
   
   private static final String SURVEY_COMPLETE = "survey_complete";
   
   private static final String SURVEY_FINISHED = "survey_finished";
   
   private static final String SURVEY_OPTIONS = "survey_options";
   
   private static final String STUDY_ETHICS_VIEW = "study_ethics_view";
   
   private static final String STUDY_PERMISSIONS = "study_permissions";
   
   private static final String STUDY_DOWNLOAD_ETHICS = "study_download_ethics";
   
   private static final String SECURITY_ERROR = "security_error";
   
   private static final String STUDY_UPDATE = "study_update";
   
   private static final String STUDY_DELETE = "study_delete";
   
   private static final String SURVEY_LOCK_ERROR = "survey_lock_error";
   
   private static final String CONSENT_DIRECTORY = PropertiesManager.getProperty("neuragenix.bio.patient.SaveConsentLocation");
   
   
   private static final String BIOSPECIMEN_STUDY = "biospecimen_study";
   private static final String BIO_SURVEY_COMPLETE = "bio_survey_complete";
   private static final String BIO_SURVEY_FINISHED = "bio_survey_finished";
   private static final String PERMISSION_STUDY_SETTINGS = "study_settings";
   
   private AuthToken authToken; // used for security
   
   private String strActivity;
   
   
   
   private String strCurrent; // This is used to know what the current page the user is on
   
   private String strXML = ""; // used to create the xml document with the starting XML header
   
   private String strStylesheet; // Used to specify the stylesheet to use for the portal
   
   //Start Secure Download Implementation
   private String strSessionUniqueID;
   private String nodeId;
   //End Secure
   
   // Default sorting order for searching the study is by the study name
   // (ASC)
   // strStudyOrderBy and strStudySortByFromSearch will be used when the
   // user search from the search page
   private String strStudyOrderBy = StudyFormFields.STUDY_NAME;
   private String strStudySortByFromSearch = DBSchema.ORDERBY_ASENDING;
   
   
   // strOrderBy will be used when the user click the study result column title
   // hyperlink to do sorting
   private String strOrderBy = StudyFormFields.STUDY_NAME;
   
   // the list of strSortBy from the patient result column title hyperlinks
   private String strSortByStudyName = DBSchema.ORDERBY_ASENDING;
   private String strSortByStudyOwner = DBSchema.ORDERBY_ASENDING;
   private String strSortByStudyStart = DBSchema.ORDERBY_ASENDING;
   private String strSortByStudyEnd = DBSchema.ORDERBY_ASENDING;
   private String strSortBySurveyName = DBSchema.ORDERBY_ASENDING;
   private String strSortByFromHyperlinks;
   
   private boolean blSurveyFromStudy = false;
   
   private String blStudyDateCheck = "true";
   
   // Base Channel
   
   private SurveyBaseChannel my_base_channel;
   
   // These hashtables are used to pass information to and from the base channel
   
   private Hashtable hashStudyFormFields;
   
   private Hashtable hashStudyFormFieldDropDowns;
   
   private Hashtable hashStudyFormSearchOperators;
   
   private Hashtable hashPatientValidateFields;
   
   private Hashtable hashStudyCutSize;
   
   private StudyAllocation studyAllocations = null;
   
   //
   private String strSource = "";
   /**
   
    *  Contructs the Patient Channel
   
    */
   
   
   private StudySpecimenTransactionAdmin staTransAdmin;
   private ConfigurationManager cfgManager = null;
   private static boolean blUseConfigurationManager = false;
   
   
   public CStudy()
   {
      
      this.strStylesheet = STUDY_SEARCH; // The default stylesheet for the patient
      
   }
   
   static
   {
      try
      {
         blUseConfigurationManager = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.study.useConfigurationManager");
      }
      catch (Exception e)
      {
         System.err.println ("[CStudy] - Property not set : neuragenix.bio.study.useConfigurationManager, defaulting to false");
      }
   }
   
   
   public String getStudyName(int intStudyID)
   {
      DALQuery query = new DALQuery();
      
      try
      {
         query.setDomain("STUDY", null, null, null);
         query.setField("STUDY_strStudyName", null);
         query.setWhere(null, 0, "STUDY_intStudyID", "=", intStudyID + "", 0, DALQuery.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         ResultSet rs = query.executeSelect();
         String strReturnValue = "";
         if (rs.first())
         {
            strReturnValue = rs.getString("STUDY_strStudyName");
         }
         
         rs.close();
         return strReturnValue;
         
         
         
      }
      catch (Exception e)
      {
         System.err.println("[CSTUDY] There was an error getting the study name");
         e.printStackTrace(System.err);
         return "";
      }
      
      
   }
   
   
   
   
   
   
   /**
  
    *  Returns channel runtime properties.
  
    *  Satisfies implementation of Channel Interface.
  
    *
  
    *  @return handle to runtime properties
  
    */
   
   public ChannelRuntimeProperties getRuntimeProperties()
   {
      
      return new ChannelRuntimeProperties();
      
   }
   
   /**
    *
    *  Process layout-level events coming from the portal.
    *
    *  Satisfies implementation of IChannel Interface.
    *
    *
    *
    *  @param PortalEvent ev a portal layout event
    *
    */
   
   public void receiveEvent(PortalEvent ev)
   {
      
      if (ev.getEventNumber() == PortalEvent.SESSION_DONE)
         
      {
         
         // Unlock all current locks!
         
         unlockStudyRecord();
         
         unlockSurveyRecord();
         
         unlockStudyEthicsRecord();
         
         unlockResultsRecord();
         
      }
      
   }
   
   public void resetLock()
   {
      
      try
      {
         
         if(lrAllocationLock!=null)
         {
            lrAllocationLock.unlock();
         }
         
         lrAllocationLock = null;
         
         
      }catch(Exception e)
      {
         
         e.printStackTrace();
         
         LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
         
      }finally
      {
         lrAllocationLock = null;
         lrAllocationLock = new LockRequest( authToken );
         
      }
   }
   
   
  /*
   *
   *  Function to check the loaded status of the study allocations
   *  @author Daniel Murley
   *
   */
   
   public boolean loadStudyAllocations(int intStudyKey) throws neuragenix.common.UnsupportedLockException
   {
      if (studyAllocations == null)
      {
         if (intStudyKey > -1)
         {
            resetLock();
            lrAllocationLock.addLock("STUDY_ALLOCATION", intStudyKey + "", LockRecord.READ_ONLY);
            studyAllocations = new StudyAllocation(intStudyKey);
            
            return true;
         }
         else
         {
            System.err.println("[Study Channel (Critical)] No study key passed to load Study Allocations");
            return false;
         }
      }
      else
         if (intStudyKey > -1)
         {
            if (intStudyKey != studyAllocations.getCurrentStudy())
            {
               studyAllocations = new StudyAllocation(intStudyKey);
               return true;
            }
            else
               return true;
            
         }
         else
            return false;
      
   }
   
   
   /**
   
    *  Receive static channel data from the portal.
   
    *  Satisfies implementation of IChannel Interface.
   
    *
   
    *  @param ChannelStaticData sd static channel data
   
    */
   
   public void setStaticData(ChannelStaticData sd)
   {
      
      this.staticData = sd;
      
      this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");
      
      // channel's ids
      Context globalIDContext = null;
      try
      {
         // Get the context that holds the global IDs for this user
         globalIDContext = (Context)staticData.getJNDIContext().lookup("/channel-ids");
      }
      catch (NotContextException nce)
      {
         LogService.log(LogService.ERROR, "Could not find subcontext /channel-ids in JNDI");
      }
      catch (NamingException e)
      {
         LogService.log(LogService.ERROR, e);
      }
      //rennypv---Smartform
      /**
      try
      {
         strSessionUniqueID = authToken.getSessionUniqueID();
         SessionManager.addSession(strSessionUniqueID);
         SessionManager.addChannelID(strSessionUniqueID, "CSmartform",
         (String) globalIDContext.lookup("CSmartform"));
      }
      
      catch (NotContextException nce)
      {
         LogService.log(LogService.ERROR, "Could not find channel ID for fname=CSmartform");
      }
      catch (NamingException e)
      {
         LogService.log(LogService.ERROR, e);
      }
      //Patient channel
      
      try
      {
         strSessionUniqueID = authToken.getSessionUniqueID();
         SessionManager.addSession(strSessionUniqueID);
         SessionManager.addChannelID(strSessionUniqueID, "CPatient",
         (String) globalIDContext.lookup("CPatient"));
      }
      
      catch (NotContextException nce)
      {
         LogService.log(LogService.ERROR, "Could not find channel ID for fname=CPatient");
      }
      catch (NamingException e)
      {
         LogService.log(LogService.ERROR, e);
      }
      
      try
      {
         strSessionUniqueID = authToken.getSessionUniqueID();
         SessionManager.addSession(strSessionUniqueID);
         SessionManager.addChannelID(strSessionUniqueID, "CSmartformAdmin",
         (String) globalIDContext.lookup("CSmartformAdmin"));
      }
      catch (NotContextException nce)
      {
         LogService.log(LogService.ERROR, "Could not find channel ID for fname=CSmartformAdmin");
      }
      catch (NamingException e)
      {
         LogService.log(LogService.ERROR, e);
      }
      **/
      try
      {
         strSessionUniqueID = authToken.getSessionUniqueID();
         SessionManager.addSession(strSessionUniqueID);
         SessionManager.addChannelID(strSessionUniqueID, "CDownload",
         (String) globalIDContext.lookup("CDownload"));
         
      }
      catch (NotContextException nce)
      {
         LogService.log(LogService.ERROR, "Could not find channel ID for fname=Inventory");
      }
      catch (NamingException e)
      {
         LogService.log(LogService.ERROR, e);
      }
     try
      {
         strSessionUniqueID = authToken.getSessionUniqueID();
         SessionManager.addSession(strSessionUniqueID);
         SessionManager.addChannelID(strSessionUniqueID, "CBiospecimen",
         (String) globalIDContext.lookup("CBiospecimen"));

      }
      catch (NotContextException nce)
      {
         LogService.log(LogService.ERROR, "Could not find channel ID for fname=Inventory");
      }
      catch (NamingException e)
      {
         LogService.log(LogService.ERROR, e);
      }
 
      //End Secure
      
      
   }
   
   /**
   
    *  Receive channel runtime data from the portal.
   
    *  Satisfies implementation of IChannel Interface.
   
    *
   
    *  @param ChannelRuntimeData rd handle to channel runtime data
   
    */
   
   public void setRuntimeData(ChannelRuntimeData rd)
   {
      
      int firstRun = 0;
      
      try
      {
         
         this.runtimeData = rd; // Get the runtime data from the web page
         
         // we use the surveybase channel instead of base channel (which extends base channel) as it has features that are specific to the survey
         
         my_base_channel = new SurveyBaseChannel(authToken);
         
         // Set the default domain
         
         my_base_channel.setDomain(StudyFormFields.STUDY_DOMAIN);
                  
         String strPermissionStudySettings = "";
         // If user has permission to change the study settings
         if (authToken.hasActivity(PERMISSION_STUDY_SETTINGS))               
         {
             strPermissionStudySettings = "<PermissionStudySettings>" + "true" + "</PermissionStudySettings>";
         }    
         
         // Get the current page the users is on
         
         if(runtimeData.getParameter("current") != null)
            
         {
            
            strCurrent = runtimeData.getParameter("current");
            
         }
         
         else
            
         {
            
            strCurrent = STUDY_SEARCH; // The default is the search form.
        	firstRun = 1;   
            LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
            
            hashStudyFormFields = null;
            
            hashStudyFormFieldDropDowns = null;
            
            hashStudyFormSearchOperators = null;
            
            hashPatientValidateFields = null;
            
            hashStudyCutSize = null;
            
            StudyFormFields.unloadFormFields();
            
         }
         
         /*
          * Study Configuration Manager
          *   Allows user to select the study which will serve as the default study
          *
          */
         
         if (blUseConfigurationManager == true)
         {
            if (runtimeData.getParameter("module") != null)
            {
               String module = runtimeData.getParameter("module");
               if (module.equalsIgnoreCase("defaultStudySelection"))
               {
                  if (cfgManager == null)
                  {
                     NGXRuntimeProperties rp = new NGXRuntimeProperties(this.authToken);
                     cfgManager = new ConfigurationManager(rp);
                  }
                  NGXRuntimeProperties rp = cfgManager.processRuntimeData(runtimeData);
                  strXML = rp.getXML();                  
                  strStylesheet = rp.getStylesheet();
                  return;
               }
            }
         }
         
         if(runtimeData.getParameter("orderBy") != null)
         {
            if((runtimeData.getParameter("orderBy")).equals("studyName"))
            {
               strOrderBy = StudyFormFields.STUDY_NAME;
               if(strSortByStudyName.equals(DBSchema.ORDERBY_DESCENDING))
               {
                  strSortByStudyName = DBSchema.ORDERBY_ASENDING;
                  strSortByFromHyperlinks = DBSchema.ORDERBY_ASENDING;
               }
               else
               {
                  strSortByStudyName = DBSchema.ORDERBY_DESCENDING;
                  strSortByFromHyperlinks = DBSchema.ORDERBY_DESCENDING;
               }
            }
            else if((runtimeData.getParameter("orderBy")).equals("studyOwner"))
            {
               strOrderBy = StudyFormFields.STUDY_OWNER;
               if(strSortByStudyOwner.equals(DBSchema.ORDERBY_DESCENDING))
               {
                  strSortByStudyOwner = DBSchema.ORDERBY_ASENDING;
                  strSortByFromHyperlinks = DBSchema.ORDERBY_ASENDING;
               }
               else
               {
                  strSortByStudyOwner = DBSchema.ORDERBY_DESCENDING;
                  strSortByFromHyperlinks = DBSchema.ORDERBY_DESCENDING;
               }
            }
            else if((runtimeData.getParameter("orderBy")).equals("studyStart"))
            {
               strOrderBy = StudyFormFields.STUDY_START;
               if(strSortByStudyStart.equals(DBSchema.ORDERBY_DESCENDING))
               {
                  strSortByStudyStart = DBSchema.ORDERBY_ASENDING;
                  strSortByFromHyperlinks = DBSchema.ORDERBY_ASENDING;
               }
               else
               {
                  strSortByStudyStart = DBSchema.ORDERBY_DESCENDING;
                  strSortByFromHyperlinks = DBSchema.ORDERBY_DESCENDING;
               }
            }
            else if((runtimeData.getParameter("orderBy")).equals("studyEnd"))
            {
               strOrderBy = StudyFormFields.STUDY_END;
               if(strSortByStudyEnd.equals(DBSchema.ORDERBY_DESCENDING))
               {
                  strSortByStudyEnd = DBSchema.ORDERBY_ASENDING;
                  strSortByFromHyperlinks = DBSchema.ORDERBY_ASENDING;
               }
               else
               {
                  strSortByStudyEnd = DBSchema.ORDERBY_DESCENDING;
                  strSortByFromHyperlinks = DBSchema.ORDERBY_DESCENDING;
               }
            }
            else if((runtimeData.getParameter("orderBy")).equals("surveyName"))
            {
               strOrderBy = StudyFormFields.SURVEY_NAME;
               if(strSortBySurveyName.equals(DBSchema.ORDERBY_DESCENDING))
               {
                  strSortBySurveyName = DBSchema.ORDERBY_ASENDING;
                  strSortByFromHyperlinks = DBSchema.ORDERBY_ASENDING;
               }
               else
               {
                  strSortBySurveyName = DBSchema.ORDERBY_DESCENDING;
                  strSortByFromHyperlinks = DBSchema.ORDERBY_DESCENDING;
               }
            }
         }
         
        /*Hashtable hashTemp = DBSchema.getLockManager();
         
        Enumeration enum = hashTemp.keys();
         
        while (enum.hasMoreElements())
         
        {
         
            LockRequest lrTemp = (LockRequest) hashTemp.get(enum.nextElement().toString());
         
            if (lrTemp.isValid())
         
                lrTemp.unlock();
         
        }*/
         
         // ------------------ ADD STUDY -------------------------------------------
         
         // They have clicked on the "add study" menu option
         if(strCurrent != null && strCurrent.equals(STUDY_ADD))
            
         {
            
            strStylesheet = STUDY_ADD; // set the default to add study
            
            unlockStudyRecord(); // unlock any locks
            
            unlockSurveyRecord();
            
            unlockStudyEthicsRecord();
            
            unlockResultsRecord();
            
            my_base_channel.setActivity(STUDY_ADD);
            
            if (!authToken.hasActivity(STUDY_ADD)) // Check to see if the patient has access
               
            {
               
               strXML  = buildSecurityErrorXMLFile("add study details");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            
            else // if ok proceed as normal
               
            {
               
               // They have clicked the save button to add a new study!
               if (runtimeData.getParameter("save") != null)
                  
               {
                  
                  // Check to see if all the required fields have been filled and the correct data is in the fields
                  
                  checkStudySubmittedData();
                  
                  if(my_base_channel.getRequiredStatus() == false || my_base_channel.getValidFieldsStatus() == false || blStudyDateCheck.equals("false"))
                     
                  {
                     
                     strStylesheet = STUDY_ADD;
                     
                  }
                  
                  else // If all data ok add the record
                     
                  {
                     
                     addStudyRecord();
                     
                     int intInsertedRecordId = 0; // Used to get the inserted record id
                     
                     if(my_base_channel.getAddRecordStatus() == true) // If the save was ok get the id and set it for the page
                        
                     {
                        
                        // Add the locks
                        
                        intInsertedRecordId = my_base_channel.getInsertedRecordId();
                        
                        lrStudyLock  = new LockRequest(authToken);
                        
                        lrStudyLock.addLock(StudyFormFields.STUDY_DOMAIN, new Integer(intInsertedRecordId).toString(), LockRecord.READ_WRITE);
                        
                        if(lrStudyLock.lockDelayWrite() == true)
                           
                        {
                           
                           // Build the study view page
                           
                           my_base_channel.setNewXMLFile();
                           
                           my_base_channel.setActivity(STUDY_VIEW);
                           
                           my_base_channel.setXMLFile(buildStudyViewScreen(new Integer(intInsertedRecordId).toString()));
                           
                        }
                        
                        else
                           
                        {
                           
                           // Someone has locked the record in the time of adding it and viewing it
                           
                           my_base_channel.setActivity(STUDY_SEARCH);
                           
                           my_base_channel.setNewXMLFile();
                           
                           buildStudyLockError();
                           
                           buildStudySearchScreen();
                           
                        }
                        
                     }else
                     {strStylesheet = STUDY_VIEW;} // Something went wrong
                     
                  }
                  
               }
               
               // They have clicked the clear button
               
               else if(runtimeData.getParameter("clear") != null && runtimeData.getParameter("clear").equals("true"))
               {
                  buildStudyAddScreen();
               }
               else // first time to the page!
               {
                  buildStudyAddScreen();
               }
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</study>";
            }
         }
         
         // ------------------------------ Biospecimen Transactions Governer Administration -----------------------
/* Author : Daniel Murley
 * Email : dmurley@neuragenix.com
 * Date : 30 - Dec - 2004
 */
         
         else if ((runtimeData.getParameter("target") != null && runtimeData.getParameter("target").equals(STUDY_PERMISSIONS)))
         {
            if (staTransAdmin == null)
            {
               staTransAdmin = new StudySpecimenTransactionAdmin(authToken);
               staTransAdmin.setCurrentStudyKey(Integer.parseInt(runtimeData.getParameter("intStudyID")));
            }
            
            strStylesheet = STUDY_PERMISSIONS;
            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>" + "<intStudyID>" + staTransAdmin.getCurrentStudyKey() + "</intStudyID>"
            + "<strStudyName>" + getStudyName(staTransAdmin.getCurrentStudyKey()) + "</strStudyName>"
            + staTransAdmin.translateToXML() + strPermissionStudySettings + "</study>";
         }
         
         else if (strCurrent != null && strCurrent.equals(STUDY_PERMISSIONS))
         {
            if (runtimeData.getParameter("back") != null)
            {
               resetLock();
               my_base_channel.setActivity(STUDY_VIEW);
               my_base_channel.setNewXMLFile();
               my_base_channel.setXMLFile(buildStudyViewScreen(runtimeData.getParameter("intStudyID")));
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</study>";
               
            }
            else
            {
               if (runtimeData.getParameter("action") != null && runtimeData.getParameter("action").equals("update_matrix"))
               {
                  staTransAdmin.updateMatrix(runtimeData);
                  
               }
               
               if (runtimeData.getParameter("reset") != null)
               {
                  System.out.println("reset calleed");
               }
               
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>" + "<intStudyID>" + staTransAdmin.getCurrentStudyKey() + "</intStudyID>"
               + "<strStudyName>" + getStudyName(staTransAdmin.getCurrentStudyKey()) + "</strStudyName>"
               + staTransAdmin.translateToXML() + strPermissionStudySettings + "</study>";
            }
         }
         
         // ------------------------------- STUDY SEARCH ------------------------------------------------------------
         
         else if(strCurrent != null && strCurrent.equals(STUDY_SEARCH))
            
         {
            
            // unlock any locks
            
            unlockStudyRecord();
            
            unlockSurveyRecord();
            
            unlockStudyEthicsRecord();
            
            unlockResultsRecord();
            
            // Set the stylesheet to results
            
            strStylesheet = STUDY_SEARCH;
            
            my_base_channel.setActivity(STUDY_SEARCH);
            
            if (!authToken.hasActivity(STUDY_SEARCH)) // Check to see if the patient has access
               
            {
               
               strXML  = buildSecurityErrorXMLFile("search study details");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            
            else // if ok proceed as normal
               
            {
               
               // We have data from the search form
               
               if (runtimeData.getParameter("submit") != null || firstRun ==1)
                  
               {
                  
                  studySearchRuntimeData = runtimeData;
                  
                  my_base_channel.setNewXMLFile();
                  
                  buildStudyResultsScreen();
                  
               }
               
               // They have clicked the clear button
               
               else if(runtimeData.getParameter("clear") != null && runtimeData.getParameter("clear").equals("true"))
                  
               {
                  
                  buildStudySearchScreen();
                  
               }
               
               else
                  
               {
                  
                  buildStudySearchScreen();
                  
               }
               
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</study>";
               
            }
            
         }
         
         // --------------------------- STUDY RESULTS ------------------------------------------------------------------------------------------
         
         else if(strCurrent != null && strCurrent.equals(STUDY_RESULTS))
            
         {
            
            // Set the stylesheet to search
            
            strStylesheet = STUDY_SEARCH;
            
            // unlock any locks
            
            unlockStudyRecord();
            
            unlockSurveyRecord();
            
            unlockStudyEthicsRecord();
            
            my_base_channel.setActivity(STUDY_VIEW);
            
            if (!authToken.hasActivity(STUDY_VIEW)) // Check to see if the patient has access
            {
               
               strXML  = buildSecurityErrorXMLFile("view study details");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            else // if ok proceed as normal
            {
               // they clicked on a study result
               
               if(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID) != null)
                  
               {
                  
                  lrStudyLock = new LockRequest(authToken);
                  
                  lrStudyLock.addLock(StudyFormFields.STUDY_DOMAIN, runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID), LockRecord.READ_WRITE);
                  
                  if(lrStudyLock.lockDelayWrite() == true)
                     
                  {
                     
                     my_base_channel.setActivity(STUDY_VIEW);
                     
                     my_base_channel.setXMLFile(buildStudyViewScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID)));
                     
                  }
                  else
                  {
                     
                     // Can't view as someone is editting!
                     
                     my_base_channel.setActivity(STUDY_SEARCH);
                     
                     my_base_channel.setNewXMLFile();
                     
                     buildStudyLockError();
                     
                     buildStudySearchScreen();
                     
                  }
                  
               }
               else if(runtimeData.getParameter("orderBy") != null)
               {
                  // refresh the study result screen so it reflect the
                  // sorting order which was determined by which column
                  // title did the user click at.
                  buildHyperlinkSortedStudyResultsScreen();
               }
               else // First time to the page
               {
                  
                  my_base_channel.setNewXMLFile();
                  
                  my_base_channel.setActivity(STUDY_SEARCH);
                  
                  buildStudySearchScreen();
                  
               }
               
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</study>";
               
            }
         }
         
         
         // ---------------- Study Biospecimen Allocations
         //
         // Author : Daniel Murley
         // Email : dmurley@neuragenix.com
         //
         // Copyright (C) Neuragenix Pty Ltd 2004
         // Designed for : CCV
         // Dependant on : StudyAllocations.java
         //
         
         else if (runtimeData.getParameter("target") != null && runtimeData.getParameter("target").equals(STUDY_ALLOCATION))
         {
            
            
            if (loadStudyAllocations(Integer.parseInt(runtimeData.getParameter("intStudyID"))) == true)
            {
               if (lrAllocationLock.lockDelayWrite() == false)
               {
                  strStylesheet = STUDY_ALLOCATION;
                  strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>";
                  strXML += "<intStudyID>" + studyAllocations.getCurrentStudy() + "</intStudyID>";
                  strXML += "<lockError>true</lockError>";
                  strXML += "<hideAdd>true</hideAdd>";
                  strXML += "<strError>This list is currently being updated by another user.  Please try again later.</strError>";
                  strXML += strPermissionStudySettings + "</study>";
               }
               else
               {
                  strStylesheet = STUDY_ALLOCATION;
                  strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>";
                  strXML += "<intStudyID>" + studyAllocations.getCurrentStudy() + "</intStudyID>";
                  strXML += "<strStudyName>" + getStudyName(studyAllocations.getCurrentStudy()) + "</strStudyName>";
                  strXML += studyAllocations.buildSavedSearchList(Integer.parseInt(runtimeData.getParameter("intStudyID")), -1, null);
                  strXML += studyAllocations.buildAddNewSearch(null) + strPermissionStudySettings + "</study>";
               }
               
            }
            else
            {
               System.err.println("[Study Channel] Unable to load study allocations");
               
               // TODO : Error message here -- most likely a locking issue
               
            }
            
         }
         
         else if(strCurrent != null && strCurrent.equals(STUDY_ALLOCATION))
         {
            String strAction = runtimeData.getParameter("action");
            
            resetLock();
            
            boolean blLockError = false;
            boolean blLockSet = false;
            String strErrorsWhenAddingNewSearch = null;
            if (strAction != null)
            {
               //adding a new search
               if (strAction.equalsIgnoreCase(ALLOCATION_SAVE))
               {
                  strErrorsWhenAddingNewSearch = studyAllocations.addNewSearch(runtimeData, false);
               }
               else if (strAction.equalsIgnoreCase(ALLOCATION_DELETE))
               {
                  if (runtimeData.getParameter("toDelete") != null)
                  {
                     
                     lrAllocationLock.addLock("STUDY_ALLOCATION", studyAllocations.getCurrentStudy() + "", LockRecord.READ_WRITE);
                     lrAllocationLock.lockDelayWrite();
                     blLockSet = true;
                     if (lrAllocationLock.lockWrites() == false)
                        blLockError = true;
                     else
                     {
                        studyAllocations.deleteSearch(Integer.parseInt(runtimeData.getParameter("toDelete")));
                        lrAllocationLock.unlockWrites();
                     }
                     
                  }
                  else
                  {
                     System.err.println("[Study Channel - Delete Allocation Handler] Unable to delete allocation : Deletion Key Parameter not valid");
                  }
                  
               }
               else if (strAction.equalsIgnoreCase(ALLOCATION_EDIT))
               {
                  
                  if (runtimeData.getParameter("toEdit") != null)
                  {
                     lrAllocationLock.addLock("STUDY_ALLOCATION", studyAllocations.getCurrentStudy() + "", LockRecord.READ_WRITE);
                     lrAllocationLock.lockDelayWrite();
                     blLockSet = true;
                     if (lrAllocationLock.lockWrites() == true)
                        studyAllocations.markForUpdate(Integer.parseInt(runtimeData.getParameter("toEdit")));
                     else
                        blLockError = true;
                  }
                  else
                  {
                     System.err.println("[Study Channel - Edit Allocation Handler] Unable to flag for edit : Edit Key parameter not valid");
                  }
               }
               else if (strAction.equalsIgnoreCase(ALLOCATION_UPDATE))
               {
                  strErrorsWhenAddingNewSearch = studyAllocations.addNewSearch(runtimeData, true);
               }
               
            }
            else
            {
               blLockError = true;
            }
            
            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>";
            
            strXML += "<intStudyID>" + studyAllocations.getCurrentStudy() + "</intStudyID>";
            strXML += "<strStudyName>" + getStudyName(studyAllocations.getCurrentStudy()) + "</strStudyName>";
            
            if (blLockError == true)
            {
               studyAllocations.unmarkAll();
               strXML += "<lockError>true</lockError>";
               strXML += "<strError>This list is being viewed or updated by another user.  Please try again later.</strError>";
            }
            
            if(strErrorsWhenAddingNewSearch != null )
            {
               System.out.println("Producing Errors in CStudy");
               strXML += "<strError>" + strErrorsWhenAddingNewSearch +"</strError>";
            }
            
            // build the main section of the list
            if ((strAction != null) && (strAction.equalsIgnoreCase(ALLOCATION_REFRESH)))
            {
               strXML += studyAllocations.buildSavedSearchList(studyAllocations.getCurrentStudy(), Integer.parseInt(runtimeData.getParameter("STUDY_ALLOCATION_intAllocationKey")), runtimeData);
            }
            else
            {
               if (blLockSet == false)
               {
                  lrAllocationLock.addLock("STUDY_ALLOCATION", studyAllocations.getCurrentStudy() + "", LockRecord.READ_ONLY);
                  lrAllocationLock.lockDelayWrite();
               }
               
               strXML += studyAllocations.buildSavedSearchList(studyAllocations.getCurrentStudy(), -1, null);
            }
            
            // build the add section of the list
            if ((strAction != null) && (strAction.equalsIgnoreCase(ALLOCATION_SAVE_REFRESH)))
            {
               strXML += studyAllocations.buildAddNewSearch(runtimeData) + "</study>";
            }
            else
            {
               strXML += studyAllocations.buildAddNewSearch(null) + strPermissionStudySettings + "</study>";
            }
            
            if (runtimeData.getParameter("back") != null)
            {
               resetLock();
               my_base_channel.setActivity(STUDY_VIEW);
               my_base_channel.setNewXMLFile();
               my_base_channel.setXMLFile(buildStudyViewScreen(runtimeData.getParameter("intStudyID")));
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</study>";
               
            }
            
            
         }
         
         // --------------------- STUDY VIEW ------------------------------------
         
         
         
         
         
         
         else if(strCurrent != null && strCurrent.equals(STUDY_VIEW))
            
         {
            
            strStylesheet = STUDY_VIEW;
            
            // unlock any surveys previously locked
            
            unlockSurveyRecord();
            
            unlockStudyEthicsRecord();
            
            unlockResultsRecord();
            
            my_base_channel.setActivity(STUDY_VIEW);
            
            if (!authToken.hasActivity(STUDY_VIEW)) // Check to see if the patient has access
               
            {
               
               strXML  = buildSecurityErrorXMLFile("view study details");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            
            else
               
            {
               
               // They are trying to update a study
               
               if (runtimeData.getParameter("save") != null)
                  
               {
                  
                  if (!authToken.hasActivity(STUDY_UPDATE)) // Check to see if the patient has access
                     
                  {
                     
                     strXML  = buildSecurityErrorXMLFile("update study details");
                     
                     strStylesheet = SECURITY_ERROR;
                     
                     return;
                     
                  }
                  
                  else
                     
                  {
                     
                     my_base_channel.setActivity(STUDY_UPDATE);
                     
                     my_base_channel.setNewXMLFile();
                     
                     if(lrStudyLock != null && lrStudyLock.isValid() == true && lrStudyLock.lockWrites() == true)
                        
                     {
                        
                        // Check to see if the save is good!
                        
                        checkStudyUpdateData();
                        
                        if(my_base_channel.getRequiredStatus() == false || my_base_channel.getValidFieldsStatus() == false || my_base_channel.getValidDataStatus() == false || blStudyDateCheck.equals("false"))
                           
                        {
                           //System.out.println("SetRuntimeData: Branch 1");
                           //strStylesheet = STUDY_VIEW;
                           my_base_channel.setXMLFile(buildStudyViewScreen(runtimeData.getParameter("intStudyID")));
                           
                        }
                        
                        else // Save if the data is good
                           
                        {
                           String baseFieldValue = my_base_channel.getFieldValue("intStudyID");
                           
                           my_base_channel.setRunTimeData(runtimeData);
                           
                           my_base_channel.setWhere("", StudyFormFields.INTERNAL_STUDY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
                           
                           my_base_channel.removeField("intStudyID");
                           // my_base_channel.removeField("STUDY_intStudyID");
                           my_base_channel.updateRecord();
                           
                           
                           lrStudyLock.unlockWrites();
                           
                           
                           // Rebuild the page
                           
                           my_base_channel.addField("intStudyID", baseFieldValue);
                           
                           my_base_channel.setActivity(STUDY_VIEW);
                           
                           my_base_channel.setNewXMLFile();
                           
                           my_base_channel.setXMLFile(buildStudyViewScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID)));
                           
                           my_base_channel.setXMLFile("<strError>Update Successful</strError>");
                           
                        }
                        
                     }
                     
                     else
                        
                     {
                        
                        // Someone else is viewing this study
                        
                        my_base_channel.setNewXMLFile();
                        
                        buildStudyLockError();
                        
                        // Rebuild the page
                        
                        my_base_channel.setActivity(STUDY_VIEW);
                        
                        my_base_channel.setXMLFile(buildStudyViewScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID)));
                        
                     }
                     
                  }
                  
               }
               
               // The want to see the surveys for this study
               
               else if(runtimeData.getParameter("target") != null && runtimeData.getParameter("target").equals(STUDY_SURVEYS))
                  
               {
                  
                  blSurveyFromStudy = true;
                  
                  surveySearchRuntimeData = runtimeData;
                  
                  // Add the survey locks for this study
                  
                  addStudySurveyLocks(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
                  
                  if(lrSurveyLock.lockDelayWrite() == true)
                     
                  {
                     
                     my_base_channel.setNewXMLFile();
                     
                     // Build the study survey screen
                     
                     buildStudySurveyScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
                     
                  }
                  
                  else // Someone else is editing the screen
                     
                  {
                     
                     my_base_channel.setNewXMLFile();
                     
                     buildStudySurveyLockError();
                     
                     my_base_channel.setXMLFile(buildStudyViewScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID)));
                     
                  }
                  
                  
                  
               }
               
               // The want to see the ethics for this study
               
               else if(runtimeData.getParameter("target") != null && runtimeData.getParameter("target").equals(STUDY_ETHICS_VIEW))
                  
               {
                  
                  my_base_channel.setNewXMLFile();
                  
                  addStudyEthicsLocks(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
                  
                  if(lrStudyEthicsLock.lockDelayWrite() == false)
                     
                  {
                     
                     buildStudyEthicsLockError();
                     
                     my_base_channel.setXMLFile(buildStudyViewScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID)));
                     
                  }
                  
                  else
                  {buildStudyEthicsScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));}
                  
               }
               
               // Delete the study record and send them to the search page
               
               else if(runtimeData.getParameter("delete") != null && runtimeData.getParameter("delete").equals("true"))
                  
               {
                  
                  if (!authToken.hasActivity(STUDY_DELETE)) // Check to see if the patient has access
                     
                  {
                     
                     strXML  = buildSecurityErrorXMLFile("delete study details");
                     
                     strStylesheet = SECURITY_ERROR;
                     
                     return;
                     
                  }
                  
                  else
                     
                  {
                     
                     my_base_channel.setActivity(STUDY_DELETE);
                     
                     // see if we can delete the study
                     
                     // First add locks to surveys attached to this study in case someone is editing a survey
                     
                     addStudySurveyLocks(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
                     
                     // Add the surveys locks, then upgrade the survey and study locks
                     
                     if(lrSurveyLock.lockDelayWrite() == true && lrSurveyLock != null && lrSurveyLock.isValid() == true && lrSurveyLock.lockWrites() == true && lrStudyLock != null && lrStudyLock.isValid() == true && lrStudyLock.lockWrites() == true)
                        
                     {
                        
                        // If all ok delete!
                        
                        deleteStudy(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
                        
                        my_base_channel.setActivity(STUDY_SEARCH);
                        
                        my_base_channel.setNewXMLFile();
                        
                        buildStudySearchScreen();
                        
                     }
                     
                     else // Someone has something locked! - send thm back to the study details screen with an error
                        
                     {
                        
                        buildStudyLockError();
                        
                        my_base_channel.setXMLFile(buildStudyViewScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID)));
                        
                     }
                     
                  }
                  
               }
               
               // They want to clear the form
               else if(runtimeData.getParameter("clear") != null && runtimeData.getParameter("clear").equals("true"))
               {
                  my_base_channel.setActivity(STUDY_VIEW);
                  my_base_channel.setNewXMLFile();
                  buildClearStudyScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
               }
               
               // back to search result
               
               else if (runtimeData.getParameter("back") != null)
                  
               {
                  ChannelRuntimeData oldRd = runtimeData;
                  
                  runtimeData = studySearchRuntimeData;
                  
                  my_base_channel.setNewXMLFile();
                  
                  buildStudyResultsScreen();
                  
                  runtimeData = oldRd;
                  
                  unlockStudyRecord();
                  
                  unlockSurveyRecord();
                  
                  unlockStudyEthicsRecord();
                  
                  unlockResultsRecord();
                  
               }
               
               // Make sure all the security points are passed before building the xml file
               
               if(strStylesheet != SECURITY_ERROR)
                  
               {
                  
                  strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</study>";
                  
               }
               
            }
            
         }
         
         // -------------------- ADD ETHICS APPROVAL FOR A STUDY --------------------------------------
         
         else if(strCurrent != null && strCurrent.equals(STUDY_ETHICS_VIEW))
            
         {
            
            // unlock, just in case
            
            unlockSurveyRecord();
            
            unlockResultsRecord();
            
            strStylesheet = STUDY_ETHICS_VIEW;
            
            my_base_channel.setActivity(STUDY_VIEW);
            
            if (!authToken.hasActivity(STUDY_ETHICS_VIEW)) // Check to see if the patient has access
               
            {
               
               strXML  = buildSecurityErrorXMLFile("view study ethics details");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            
            else
               
            {
               
               // They have clicked the add ethics button
               
               if(runtimeData.getParameter("saveApproval") != null)
                  
               {
                  
                  strStylesheet = STUDY_ETHICS_VIEW;
                  
                  // Check to see if the data is correct
                  
                  hashStudyFormFields = StudyFormFields.getViewStudyEthicsFormFields(); // USed for display and getting values
                  
                  my_base_channel.setFormFields(hashStudyFormFields);
                  
                  // You have to remove the ethics id as it hasn't been created yet
                  
                  hashStudyFormFields.remove("intEthicsStudyID");
                  
                  hashStudyFormFields.remove("strEthicsFileName");
                  
                  my_base_channel.clearDomains();
                  
                  my_base_channel.setDomain(StudyFormFields.ETHICS_CONSENT_DOMAIN);
                  
                  my_base_channel.setRunTimeData(runtimeData);
                  
                  my_base_channel.buildAddModifyXMLFile();
                  
                  
                  
                  // Make sure the data is good. If so, add else return them to the appointments page
                  
                  if(my_base_channel.getRequiredStatus() == false || my_base_channel.getValidFieldsStatus() == false || my_base_channel.getValidDataStatus() == false)
                     
                  {
                     
                     strStylesheet = STUDY_ETHICS_VIEW;
                     
                  }
                  
                  else // If the data is ok add the ethics record
                     
                  {
                     
                     my_base_channel.setNewXMLFile();
                     
                     // Check to see if the set of study ethics is locked
                     
                     if(lrStudyEthicsLock != null && lrStudyEthicsLock.isValid() == true)
                        
                     {
                        
                        my_base_channel.addRecord();
                        
                        
                        
                        lrStudyEthicsLock.addLock(StudyFormFields.ETHICS_CONSENT_DOMAIN, new Integer(my_base_channel.getInsertedRecordId()).toString(), LockRecord.READ_WRITE);
                        
                        if(lrStudyEthicsLock.lockDelayWrite() == false)
                           
                        {
                           
                           buildStudyEthicsLockError();
                           
                        }
                        
                        
                        
                     }
                     
                     else
                     { buildStudyEthicsLockError();}
                     
                  }
                  
                  // You have to unload the forms because we removed the ethics id as it will stay removed as the object is static
                  
                  StudyFormFields.unloadFormFields();
                  
                  // Rebuild the screen
                  
                  buildStudyEthicsScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
                  
               }
               
               // They want to delete a ethics record
               
               else if(runtimeData.getParameter("intEthicsStudyID") != null && runtimeData.getParameter("target").equals("delete"))
                  
               {
                  
                  my_base_channel.setNewXMLFile();
                  
                  // Check the locks
                  
                  if(lrStudyEthicsLock != null && lrStudyEthicsLock.isValid() == true && lrStudyEthicsLock.lockWrites() == true)
                     
                  {
                     
                     deleteStudyEthicsRecord(runtimeData.getParameter(StudyFormFields.INTERNAL_ETHICS_CONSENT_ID));
                     
                     lrStudyEthicsLock.unlockWrites();
                     
                  }
                  
                  else
                  { buildStudyEthicsLockError();}
                  
                  // Rebuild the screen
                  
                  buildStudyEthicsScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
                  
               }
               
               // They want to add a ethics file to a study
               
               else if(runtimeData.getParameter("intEthicsStudyID") != null && runtimeData.getParameter("target") != null && runtimeData.getParameter("target").equals("add_file"))
                  
               {
                  
                  if(lrStudyEthicsLock != null && lrStudyEthicsLock.isValid() == true && lrStudyEthicsLock.lockWrites() == true)
                     
                  {
                     
                     strStylesheet = STUDY_DOWNLOAD_ETHICS;
                     
                     my_base_channel.setFormFields(new Hashtable());
                     
                     my_base_channel.setNewXMLFile();
                     
                     my_base_channel.setXMLFile("<" + StudyFormFields.ETHICS_CONSENT_DOMAIN + "_Timestamp>" + runtimeData.getParameter(StudyFormFields.ETHICS_CONSENT_DOMAIN + "_Timestamp") + "</" + StudyFormFields.ETHICS_CONSENT_DOMAIN + "_Timestamp>");
                     
                     my_base_channel.setXMLFile("<" + StudyFormFields.INTERNAL_ETHICS_CONSENT_ID + ">" + runtimeData.getParameter(StudyFormFields.INTERNAL_ETHICS_CONSENT_ID) + "</" + StudyFormFields.INTERNAL_ETHICS_CONSENT_ID + ">");
                     
                     my_base_channel.setXMLFile("<" + StudyFormFields.INTERNAL_STUDY_ID + ">" + runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID) + "</" + StudyFormFields.INTERNAL_STUDY_ID + ">");
                     
                     my_base_channel.buildAddModifyXMLFile();
                     
                  }else
                  {
                     
                     buildStudyEthicsLockError();
                     
                     // Rebuild the screen
                     
                     buildStudyEthicsScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
                     
                  }
                  
               }
               
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>" + my_base_channel.getXMLFile() +  strPermissionStudySettings + "</study>";
               
            }
            
         }
         
         
         
         // ------------------- STUDY ETHICS DOWNLOAD FILE ---------------------------------------
         
         else if(strCurrent != null && strCurrent.equals(STUDY_DOWNLOAD_ETHICS))
            
         {
            
            my_base_channel.setActivity(STUDY_VIEW);
            
            
            // First save the file to the server
            
            if(runtimeData.getParameter("cancel") == null)
               
            {
               hashStudyFormFields = new Hashtable(1);
               
               hashStudyFormFields.put(StudyFormFields.ETHICS_FILENAME, "NA");
               
               my_base_channel.setFormFields(hashStudyFormFields);
               
               my_base_channel.setRunTimeData(runtimeData);
               
               my_base_channel.setDirectory(CONSENT_DIRECTORY);
               
               my_base_channel.saveFileToServer();
               
            }
            
            // If all was not well with the save send them back!
            if(my_base_channel.getOKFileSize() == false)
               
            {
               
               strStylesheet = STUDY_DOWNLOAD_ETHICS;
               
               my_base_channel.clearDomains();
               
               my_base_channel.setDomain(StudyFormFields.ETHICS_CONSENT_DOMAIN);
               
               my_base_channel.setFormFields(new Hashtable());
               
               //rennypv----included Timestamp
               
               
               my_base_channel.setXMLFile("<" + StudyFormFields.ETHICS_CONSENT_DOMAIN + "_Timestamp>" + runtimeData.getParameter(StudyFormFields.ETHICS_CONSENT_DOMAIN + "_Timestamp") + "</" + StudyFormFields.ETHICS_CONSENT_DOMAIN + "_Timestamp>");
               
               my_base_channel.setXMLFile("<" + StudyFormFields.INTERNAL_ETHICS_CONSENT_ID + ">" + runtimeData.getParameter(StudyFormFields.INTERNAL_ETHICS_CONSENT_ID) + "</" + StudyFormFields.INTERNAL_ETHICS_CONSENT_ID + ">");
               
               my_base_channel.setXMLFile("<" + StudyFormFields.INTERNAL_STUDY_ID + ">" + runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID) + "</" + StudyFormFields.INTERNAL_STUDY_ID + ">");
               
               my_base_channel.buildAddModifyXMLFile();
               
            }
            
            else // If the saving of the file went well, add the link to the study
               
            {
               
               // Update the link to the consent study table
               
               my_base_channel.setNewXMLFile();
               
               my_base_channel.clearDomains();
               
               my_base_channel.setDomain(StudyFormFields.ETHICS_CONSENT_DOMAIN);
               
               hashStudyFormFields = StudyFormFields.getViewStudyEthicsFormFields(); // USed for display and getting values
               
               my_base_channel.setFormFields(hashStudyFormFields);
               
               hashStudyFormFields.remove("intEthicsApproved");
               
               hashStudyFormFields.remove("dtEthicsAppDate");
               
               hashStudyFormFields.remove("strEthicsComments");
               
               hashStudyFormFields.remove("strEthicsApprovedBy");
               
               runtimeData.setParameter("strEthicsFileName", my_base_channel.getFileName());
               
               my_base_channel.setRunTimeData(runtimeData);
               
               my_base_channel.removeField(StudyFormFields.INTERNAL_ETHICS_CONSENT_ID);
               
               my_base_channel.setWhere("", StudyFormFields.INTERNAL_ETHICS_CONSENT_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_ETHICS_CONSENT_ID));
               
               if(runtimeData.getParameter("cancel") == null)
               {    
                   my_base_channel.updateRecord();
               }    
               
               my_base_channel.addField(StudyFormFields.INTERNAL_ETHICS_CONSENT_ID, runtimeData.getParameter(StudyFormFields.INTERNAL_ETHICS_CONSENT_ID));
               
               StudyFormFields.unloadFormFields();
               
               // Clear the base channel stuff
               
               my_base_channel.setNewXMLFile();
               
               if(my_base_channel.getUpdateTimeStampFailed() == true) //has someone else has changed the record since we got it last?
                  
               {
                  
                  my_base_channel.setErrorXMLFile();
                  
               }
               
               my_base_channel.clearWhere();
               
               my_base_channel.clearDomains();
               
               // unlock the record
               
               lrStudyEthicsLock.unlockWrites();
               
               // Rebuild the screen
               
               buildStudyEthicsScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
               
            }
            
            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</study>";
            
            
         }
         
         
         
         // --------------------- SURVEYS ATTACHED TO STUDYs VIEW ------------------------------------
         
         else if(strCurrent != null && strCurrent.equals(STUDY_SURVEYS))
            
         {
            if (!authToken.hasActivity(STUDY_VIEW)) // Check to see if the patient has access
               
            {
               
               strXML  = buildSecurityErrorXMLFile("view study details");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            
            else
               
            {
               
               my_base_channel.setActivity(STUDY_VIEW);
               
               // They want to add a survey to a study
               
               if (runtimeData.getParameter("addSurvey") != null)
                  
               {
                  my_base_channel.setNewXMLFile();
                  
                  // Check to see if we can add the survey or if someone is updating these records!
                  
                  
                  if(lrSurveyLock != null && lrSurveyLock.isValid() == true)
                     
                  {
                     
                     //rennypv
                     addSurveyToStudy();
                     
                     addSmartformToParticipants();
                     
                     addSmartformToBiospecimen();
                     // Add the lock
                     
                     
                     //                            lrSurveyLock.unlockWrites();
                     
                     
                     if(lrSurveyLock.lockDelayWrite() == false)
                        
                     {
                        
                        buildStudySurveyLockError();
                        
                     }
                     
                  }
                  
                  else
                  {buildStudySurveyLockError();}
                  
                  StudyFormFields.unloadFormFields();
                  
                  // Build the study view part
                  
                  buildStudySurveyScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
                  
                  
               }
               
               // They have selected the back button
               
               else if (runtimeData.getParameter("back") != null)
                  
               {
                  
                  unlockSurveyRecord();
                  
                  my_base_channel.setNewXMLFile();
                  
                  my_base_channel.setXMLFile(buildStudyViewScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID)));
                  
               }
               
               // They want to delete a survey from the list
               
               else if (runtimeData.getParameter("delete") != null)
                  
               {
                  
                  // Check to see if we can delete the survey or if someone is updating these records!
                  
                  //                    lrSurveyLock.addLock(StudyFormFields.SURVEY_DOMAIN, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID), LockRecord.READ_WRITE);
                  
                  if(lrSurveyLock != null && lrSurveyLock.isValid() == true && lrSurveyLock.lockWrites() == true)
                     
                  {
                     
                     deleteStudySurvey();
                     
                     deleteSmartformFromParticipants();
                     
                     deleteSmartformFromBiospecimen();
                     
                     lrSurveyLock.unlockWrites();
                     
                  }
                  
                  else
                  {buildStudySurveyLockError();}
                  
                  StudyFormFields.unloadFormFields();
                  
                  // Build the study view part
                  
                  buildStudySurveyScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
                  
               }
               
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</study>";
               
            }
            
         }
         
         /**
 
          *
 
          *
 
          *                                  SURVEY
 
          *
 
          *
 
          *
 
          */
         
         // ------------------------------- SURVEY SEARCH ------------------------------------------------------------
         
         
         
         else if(strCurrent != null && strCurrent.equals(SURVEY_SEARCH))
            
         {
            
            blSurveyFromStudy = false;
            
            // Remove any locks
            
            unlockStudyRecord(); // unlock any locks
            
            unlockSurveyRecord();
            
            unlockStudyEthicsRecord();
            
            unlockResultsRecord();
            
            // Set the stylesheet to results
            
            strStylesheet = SURVEY_SEARCH;
            
            my_base_channel.setActivity(SURVEY_SEARCH);
            
            if (!authToken.hasActivity(SURVEY_SEARCH)) // Check to see if the patient has access
               
            {
               
               strXML  = buildSecurityErrorXMLFile("search surveys");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            
            else
               
            {
               
               // We have data from the search form
               
               if (runtimeData.getParameter("submit") != null)
                  
               {
                  
                  surveySearchRuntimeData = runtimeData;
                  
                  
                  
                  // Do the search
                  
                  my_base_channel.setNewXMLFile();
                  
                  buildSurveyResultsFromSearchScreen();
                  
               }
               
               // They have clicked the clear button
               
               else if(runtimeData.getParameter("clear") != null && runtimeData.getParameter("clear").equals("true"))
                  
               {
                  
                  my_base_channel.setNewXMLFile();
                  
                  buildSurveySearchScreen();
                  
               }
               
               else // First time to the page
                  
               {
                  
                  my_base_channel.setNewXMLFile();
                  
                  buildSurveySearchScreen();
                  
               }
               
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><survey>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</survey>";
               
            }
            
         }
         
         // ------------------ ADD SURVEY -------------------------------------------
         
         
         
         else if(strCurrent != null && strCurrent.equals(SURVEY_ADD))
            
         {
            
            unlockStudyRecord(); // unlock any locks
            
            unlockSurveyRecord();
            
            unlockStudyEthicsRecord();
            
            unlockResultsRecord();
            
            strStylesheet = SURVEY_ADD; // set the default to add study
            
            my_base_channel.setActivity(SURVEY_ADD);
            
            if (!authToken.hasActivity(SURVEY_ADD)) // Check to see if the patient has access
               
            {
               
               strXML  = buildSecurityErrorXMLFile("add survey details");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            
            else
               
            {
               
               // They have want to add a new survey
               
               if (runtimeData.getParameter("save") != null)
                  
               {
                  
                  checkSurveyAddSubmittedData();
                  
                  // Check to see if all the required fields have been filled and the correct data is in the fields
                  
                  if(my_base_channel.getRequiredStatus() == false || my_base_channel.getValidFieldsStatus() == false)
                     
                  {
                     
                     strStylesheet = SURVEY_ADD;
                     
                  }
                  
                  else // If all data ok add the record
                     
                  {
                     
                     addSurvey();
                     
                     int intInsertedRecordId = 0;
                     
                     // Get the inserted id if all was good
                     
                     if(my_base_channel.getAddRecordStatus() == true)
                        
                     {
                        
                        intInsertedRecordId = my_base_channel.getInsertedRecordId();
                        
                        lrSurveyLock = new LockRequest(authToken);
                        
                        lrSurveyLock.addLock(StudyFormFields.SURVEY_DOMAIN, new Integer(intInsertedRecordId).toString(), LockRecord.READ_WRITE);
                        
                        if(lrSurveyLock.lockDelayWrite() == true)
                           
                        {
                           
                           // Build the view survey view
                           
                           buildSurveyViewScreen(new Integer(intInsertedRecordId).toString());
                           
                        }
                        
                        else // someone has locked the study straight after adding!
                           
                        {
                           
                           buildSurveyLockError();
                           
                           buildSurveySearchScreen();
                           
                        }
                        
                        
                        
                     }else
                     {strStylesheet = SURVEY_ADD;}
                     
                  }
                  
               }
               
               // They have clicked the clear button
               
               else if(runtimeData.getParameter("clear") != null && runtimeData.getParameter("clear").equals("true"))
                  
               {
                  
                  my_base_channel.setNewXMLFile();
                  
                  buildAddSurveyScreen();
                  
               }
               
               else // first time to the page!
                  
               {
                  
                  buildAddSurveyScreen();
                  
               }
               
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><survey>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</survey>";
               
            }
            
         }
         
         // --------------------------- SURVEY RESULTS ------------------------------------------------------------------------------------------
         
         else if(strCurrent != null && strCurrent.equals(SURVEY_RESULTS))
            
         {
            //rennypv
            
            unlockStudyRecord(); // unlock any locks
            
            unlockSurveyRecord();
            
            unlockStudyEthicsRecord();
            
            unlockResultsRecord();
            
            // Set the stylesheet to search
            
            strStylesheet = SURVEY_SEARCH;
            
            my_base_channel.setActivity(SURVEY_VIEW);
            
            if(runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID) != null) // the clicked on a survey result
            {
               if (!authToken.hasActivity(SURVEY_VIEW)) // Check to see if the user has access
               {
                  strXML  = buildSecurityErrorXMLFile("view survey details");
                  
                  strStylesheet = SECURITY_ERROR;
                  
                  return;
               }
               else
               { // If all security stuff is ok show them the money!
                  // Add a lock
                  lrSurveyLock = new LockRequest(authToken);
                  
                  lrSurveyLock.addLock(StudyFormFields.SURVEY_DOMAIN, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID), LockRecord.READ_WRITE);
                  
                  if(lrSurveyLock.lockDelayWrite() == true)
                  {
                     
                     buildSurveyViewScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID));
                  }
                  else // Someone is editting!
                  {
                     
                     buildSurveyLockError();
                     
                     buildSurveySearchScreen();
                  }
               }
            }
            else if(runtimeData.getParameter("orderBy") != null)
            {
               // refresh the survey result screen so it reflect the
               // sorting order which was determined by which column
               // title did the user click at.
               refreshSortedSurveyResultsFromSearchScreen();
            }
            else // First time to the page
            {
               buildSurveySearchScreen();
            }
            
            if(strStylesheet != SECURITY_ERROR)
            {
               
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><survey>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</survey>";
            }
         }
         
         // --------------------- SURVEY VIEW ------------------------------------
         
         else if(strCurrent != null && strCurrent.equals(SURVEY_VIEW))
            
         {
            
            
            strStylesheet = SURVEY_VIEW;
            
            my_base_channel.setActivity(SURVEY_VIEW);
            
            if (!authToken.hasActivity(SURVEY_VIEW)) // Check to see if the patient has access
               
            {
               
               strXML  = buildSecurityErrorXMLFile("view survey details");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            
            else // if all is ok proceed as normal!
               
            {
               
               // They want to edit a survey
               
               if (runtimeData.getParameter("save") != null)
                  
               {
                  
                  if (!authToken.hasActivity(SURVEY_EDIT)) // Check to see if the patient has access
                     
                  {
                     
                     strXML  = buildSecurityErrorXMLFile("edit survey details");
                     
                     strStylesheet = SECURITY_ERROR;
                     
                     return;
                     
                  }
                  
                  else // if they can edit, let them!
                     
                  {
                     
                     my_base_channel.setNewXMLFile();
                     
                     // Check to see if the save is good!
                     
                     checkSurveySubmittedData();
                     
                     if(my_base_channel.getRequiredStatus() == false || my_base_channel.getValidFieldsStatus() == false || my_base_channel.getValidDataStatus() == false)
                        
                     {
                        
                        strStylesheet = SURVEY_VIEW;
                        
                     }
                     
                     else // Save if the data is good
                        
                     {
                        
                        my_base_channel.setNewXMLFile();
                        
                        // Make sure the survey is not locked!
                        
                        if(lrSurveyLock != null && lrSurveyLock.isValid() == true && lrSurveyLock.lockWrites() == true)
                           
                        {
                           
                           my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID));
                           
                           my_base_channel.updateRecord();
                           
                           lrSurveyLock.unlockWrites();
                           
                        }
                        
                        else
                        { buildSurveyLockError();}
                        
                        // Build the screen
                        
                        buildSurveyViewScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID));
                        
                     }
                     
                  }
                  
               }
               
               // Delete the survey record and send them to the search page
               
               else if(runtimeData.getParameter("delete") != null && runtimeData.getParameter("delete").equals("true"))
                  
               {
                  
                  if (!authToken.hasActivity(SURVEY_DELETE)) // Check to see if the patient has access
                     
                  {
                     
                     strXML  = buildSecurityErrorXMLFile("delete survey details");
                     
                     strStylesheet = SECURITY_ERROR;
                     
                     return;
                     
                  }
                  
                  else // If they have the rights, let them proceed
                     
                  {
                     
                     
                     
                     // Check to see if this survey is attacted to any studies
                     
                     my_base_channel.setActivity(SURVEY_DELETE);
                     
                     my_base_channel.clearDomains();
                     
                     my_base_channel.setDomain(StudyFormFields.STUDY_SURVEY_DOMAIN);
                     
                     Hashtable hashTmp = new Hashtable();
                     
                     hashTmp.put(StudyFormFields.INTERNAL_SURVEY_ID, "");
                     
                     my_base_channel.setFormFields(hashTmp);
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID));
                     
                     my_base_channel.setNewXMLFile();
                     
                     // If this survey is not attached to any studies you may delete
                     
                     if(((Vector)my_base_channel.lookupRecord()).size() == 0)
                        
                     {
                        
                        // Delete the survey record
                        
                        my_base_channel.setActivity(SURVEY_DELETE);
                        
                        if(lrSurveyLock != null && lrSurveyLock.isValid() == true && lrSurveyLock.lockWrites() == true)
                           
                        {
                           
                           deleteSurvey(runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID));
                           
                           lrSurveyLock.unlockWrites();
                           
                           my_base_channel.setNewXMLFile();
                           
                           // Build the search survey form
                           
                           buildSurveySearchScreen();
                           
                        }
                        
                        else
                        { // Someone has locked this survey, send them back to survey view screen with an error
                           
                           my_base_channel.setNewXMLFile();
                           
                           buildSurveyLockError();
                           
                           buildSurveyViewScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID));
                           
                        }
                        
                     }
                     
                     else // Surveys still attached to studies, can't delete!
                        
                     {
                        
                        my_base_channel.clearWhere();
                        
                        my_base_channel.setActivity(SURVEY_VIEW);
                        
                        my_base_channel.setNewXMLFile();
                        
                        my_base_channel.setXMLFile("<strErrorSurveyOnStudy>This survey is currently attached to one or more studies. Please detach the surveys before deleting</strErrorSurveyOnStudy>");
                        
                        buildSurveyViewScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID));
                        
                     }
                     
                  }
                  
               }
               
               // They want to clear the form fields
               
               else if(runtimeData.getParameter("clear") != null && runtimeData.getParameter("clear").equals("true"))
                  
               {
                  
                  strStylesheet = SURVEY_VIEW;
                  
                  my_base_channel.setNewXMLFile();
                  
                  hashStudyFormFields = StudyFormFields.getSurveyViewFormFields();
                  
                  my_base_channel.setFormFields(hashStudyFormFields);
                  
                  my_base_channel.buildDefaultXMLFile();
                  
                  my_base_channel.setXMLFile("<" + StudyFormFields.INTERNAL_SURVEY_ID + ">" + runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID) + "</" + StudyFormFields.INTERNAL_SURVEY_ID + ">");
                  
               }
               
               // They want to add a new question!
               
               else if(runtimeData.getParameter("addQuestions") != null)
                  
               {
                  
                  if (!authToken.hasActivity(SURVEY_QUESTIONS)) // Check to see if the patient has access
                     
                  {
                     
                     strXML  = buildSecurityErrorXMLFile("add survey questions");
                     
                     strStylesheet = SECURITY_ERROR;
                     
                     return;
                     
                  }
                  
                  else // They can add questions to a survey
                     
                  {
                     
                     my_base_channel.setNewXMLFile();
                     
                     // Upgrade the survey lock while they are editing the questions
                     
                     if(lrSurveyLock != null && lrSurveyLock.isValid() == true && lrSurveyLock.lockWrites() == true)
                        
                     {
                        
                        // Build the questions default form
                        
                        buildDefaultQuestionScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                        
                     }
                     
                     else
                        
                     {
                        
                        buildSurveyLockError();
                        
                        buildSurveyViewScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID));
                        
                     }
                     
                  }
                  
               }
               
               else if (runtimeData.getParameter("back") != null)
                  
               {
                  
                  ChannelRuntimeData oldRd = runtimeData;
                  
                  runtimeData = surveySearchRuntimeData;
                  
                  my_base_channel.setNewXMLFile();
                  
                  if (blSurveyFromStudy)
                     
                     buildStudySurveyScreen(runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
                  
                  else
                     
                     buildSurveyResultsFromSearchScreen();
                  
                  
                  
                  runtimeData = oldRd;
                  
                  unlockStudyRecord();
                  
                  //rennypv                    unlockSurveyRecord();
                  
                  unlockStudyEthicsRecord();
                  
                  unlockResultsRecord();
                  
               }
               
               // Make sure all the security points were passed before building the XML
               
               if(strStylesheet != SECURITY_ERROR)
                  
               {
                  
                  if (runtimeData.getParameter("back") != null)
                     
                  {
                     
                     if (blSurveyFromStudy)
                        
                     {
                        
                        //blSurveyFromStudy = false;
                        
                        strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><study>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</study>";
                        
                     }
                     
                     else
                        
                        strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><survey>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</survey>";
                     
                  }
                  
                  else
                     
                  {
                     
                     strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><survey>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</survey>";
                     
                  }
                  
               }
               
            }
            
         }
         
         // --------------------- ADD QUESTIONS TO A SURVEY ------------------------------------
         
         else if(strCurrent != null && strCurrent.equals(SURVEY_QUESTIONS))
            
         {
            
            my_base_channel.setActivity(SURVEY_QUESTIONS);
            
            if (!authToken.hasActivity(SURVEY_QUESTIONS)) // Check to see if the patient has access
               
            {
               
               strXML  = buildSecurityErrorXMLFile("add survey questions");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            
            else // They are allowed to add questions
               
            {
               
               deleteSurveyTemplate(runtimeData.getParameter("intSurveyID"));
               
               strStylesheet = SURVEY_QUESTIONS;
               
               hashStudyFormFields = StudyFormFields.getSurveyQuestionsFormFields();
               
               my_base_channel.setFormFields(hashStudyFormFields);
               
               my_base_channel.buildDefaultXMLFile();
               
               // This passes the survey name and id back to the stlysheet. Save doing any query
               
               my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(Utilities.base64Decode(runtimeData.getParameter("strSurveyNameURL"))) + "</strSurveyName>");
               
               my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
               
               my_base_channel.setXMLFile("<strSurveyNameURL>" + runtimeData.getParameter("strSurveyNameURL") + "</strSurveyNameURL>");
               
               // They are want to add a new question
               
               if(runtimeData.getParameter("addNewQuestion") != null)
                  
               {
                  
                  runtimeData.setParameter(StudyFormFields.INTERNAL_QUESTION_ID, "");
                  
                  // This tag tells the stylesheet whether to display the details of the question
                  
                  my_base_channel.setXMLFile("<blAddEditQuestion>true</blAddEditQuestion>");
                  my_base_channel.setXMLFile("<intQuestionType>ADD_QUESTION</intQuestionType >");
                  
               }
               
               // They have clicked the button to add a title or comment
               
               else if(runtimeData.getParameter("addNewTitleComment") != null)
                  
               {
                  
                  my_base_channel.setXMLFile("<blAddEditTitleComment>true</blAddEditTitleComment>");
                  
               }
               
               // They are adding a page break
               
               else if(runtimeData.getParameter("addNewPageBreak") != null)
                  
               {
                  
                  strStylesheet = SURVEY_QUESTIONS;
                  
                  my_base_channel.clearDomains();
                  
                  my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
                  
                  // Get the highest question no and question order to add to the newly created question
                  
                  Hashtable hashTempHashtable = new Hashtable();
                  
                  hashTempHashtable.put(StudyFormFields.QUESTION_ORDER, "NA");
                  
                  hashTempHashtable.put(StudyFormFields.QUESTION_NUMBER, "NA");
                  
                  my_base_channel.setFormFields(hashTempHashtable);
                  
                  my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                  

                  // Run the query to get the max question order and question number in the table for a specified survey
                  
                  my_base_channel.getMax();
                  
                  Hashtable hashMaxFields =  my_base_channel.getMaxFields();
                  
                  
                  
                  int intHighestQuestionOrder = new Integer(hashMaxFields.get(StudyFormFields.QUESTION_ORDER).toString()).intValue();
                  
                  int intHighestQuestionNo = 0;
                  
                  my_base_channel.clearWhere();
                  
                  
                  
                  hashStudyFormFields = StudyFormFields.getSurveyQuestionsFormFields();
                  
                  hashStudyFormFields.remove(StudyFormFields.INTERNAL_QUESTION_ID);
                  
                  // Add the word "PAGE BREAK" to the question field - has no real effect
                  
                  runtimeData.setParameter(StudyFormFields.QUESTION, "PAGE BREAK");
                  
                  // Add the page break type
                  
                  runtimeData.setParameter(StudyFormFields.QUESTION_TYPE, new Integer(StudyFormFields.QUESTION_TYPE_PAGEBREAK).toString());
                  
                  // Add one to the higest to add it to the bottom of the survey
                  
                  runtimeData.setParameter(StudyFormFields.QUESTION_ORDER, new Integer(intHighestQuestionOrder + 1).toString());
                  
                  //Don't add a question number if its a page break - set it to "0"
                  
                  intHighestQuestionNo = 0;
                  
                  runtimeData.setParameter(StudyFormFields.QUESTION_NUMBER, new Integer(intHighestQuestionNo).toString());
                  
                  my_base_channel.setFormFields(hashStudyFormFields);
                  
                  my_base_channel.setRunTimeData(runtimeData);
                  
                  // Finally add the record
                  
                  my_base_channel.addRecord();
                  
                  StudyFormFields.unloadFormFields();
                  
                  int intInsertedRecordId = 0;
                  
                  // If the add was ok get the inserted record id!
                  
                  if(my_base_channel.getAddRecordStatus() == true)
                     
                  {
                     
                     intInsertedRecordId = my_base_channel.getInsertedRecordId();
                     
                     my_base_channel.setXMLFile("<" + StudyFormFields.INTERNAL_QUESTION_ID +">" + intInsertedRecordId + "</" + StudyFormFields.INTERNAL_QUESTION_ID + ">");
                     
                  }
                  
                  my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                  
                  my_base_channel.setWhere(DBSchema.AND_CONNECTOR, StudyFormFields.INTERNAL_QUESTION_ID, DBSchema.EQUALS_OPERATOR, new Integer(intInsertedRecordId).toString());
                  
                  // This passes the survey name and id back to the stlysheet. Save doing any query
                  
                  //      my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strSurveyName")) + "</strSurveyName>");
                  
                  //     my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
                  
                  hashStudyFormFields = StudyFormFields.getSurveyQuestionsFormFields();
                  
                  my_base_channel.setFormFields(hashStudyFormFields);
                  
                  my_base_channel.buildViewXMLFile();
                  
               }
               
               // They have selected a question to edit
               
               else if(runtimeData.getParameter("intQuestionID") != null && runtimeData.getParameter("duplicateQuestion") == null)
                  
               {
                  
                  
                  
                  strStylesheet = SURVEY_QUESTIONS;
                  
                  
                  
                  my_base_channel.clearDomains();
                  
                  my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
                  
                  my_base_channel.setWhere("", StudyFormFields.INTERNAL_QUESTION_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_QUESTION_ID).toString());
                  
                  hashStudyFormFields = StudyFormFields.getSurveyQuestionsFormFields();
                  
                  my_base_channel.setFormFields(hashStudyFormFields);
                  
                  // See if they have tried to save the record
                  
                  if(runtimeData.getParameter("saveQuestion") != null)
                     
                  {
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.buildAddModifyXMLFile();
                     
                     // Check to see if all the required fields have been filled and the correct data is in the fields
                     
                     if(my_base_channel.getRequiredStatus() == true && my_base_channel.getValidFieldsStatus() == true)
                        
                     {
                        
                        my_base_channel.updateRecord();
                        
                        // my_base_channel.setNewXMLFile();
                        
                        if(my_base_channel.getUpdateTimeStampFailed() == true) //someone else has changed the record since we got it last
                           
                        {
                           
                           my_base_channel.setErrorXMLFile();
                           
                        }
                        
                     }
                     
                  }
                  
                  // They want to move a question up the list
                  
                  else if(runtimeData.getParameter("moveUp") != null)
                     
                  {
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.setQuestionNoOrder(new Integer(runtimeData.getParameter("intQuestionID")).intValue(), new Integer(runtimeData.getParameter("intSurveyID")).intValue(), StudyFormFields.QUESTION_MOVE_UP);
                     
                  }
                  
                  // They want to move a question down the list
                  
                  else if(runtimeData.getParameter("moveDown") != null)
                     
                  {
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.setQuestionNoOrder(new Integer(runtimeData.getParameter("intQuestionID")).intValue(), new Integer(runtimeData.getParameter("intSurveyID")).intValue(), StudyFormFields.QUESTION_MOVE_DOWN);
                     
                  }
                  
                  // They want to delete a question
                  
                  if (runtimeData.getParameter("delete") != null)
                     
                  {
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     // Set these flags to false so we don't show the bottom half of the question
                     
                     my_base_channel.setXMLFile("<blAddEditQuestion>false</blAddEditQuestion>");
                     
                     my_base_channel.setXMLFile("<blAddEditTitleComment>false</blAddEditTitleComment>");
                     
                     my_base_channel.deleteQuestion(new Integer(runtimeData.getParameter("intQuestionID")).intValue(), new Integer(runtimeData.getParameter("intSurveyID")).intValue());
                     
                  }
                  
                  // If they have clicked the question the first time just show them the details
                  
                  else
                  {my_base_channel.setXMLFile("<blAddEditQuestion>true</blAddEditQuestion>");}
                  
                  // Build the view question file
                  
                  my_base_channel.buildViewXMLFile();
                  
               }
               
               // THey are adding a new record
               
               else if(runtimeData.getParameter("intQuestionID") == null && runtimeData.getParameter("saveQuestion") != null)
                  
               {
                  
                  strStylesheet = SURVEY_QUESTIONS;
                  
                  my_base_channel.clearDomains();
                  
                  my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
                  
                  // Get the highest question no and question order to add to the newly created question
                  
                  Hashtable hashTempHashtable = new Hashtable();
                  
                  hashTempHashtable.put(StudyFormFields.QUESTION_ORDER, "NA");
                  
                  hashTempHashtable.put(StudyFormFields.QUESTION_NUMBER, "NA");
                  
                  my_base_channel.setFormFields(hashTempHashtable);
                  
                  my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                  
                  // Run the query to get the max for question number and order
                  
                  my_base_channel.getMax();
                  
                  Hashtable hashMaxFields =  my_base_channel.getMaxFields();
                  
                  int intHighestQuestionOrder = new Integer(hashMaxFields.get(StudyFormFields.QUESTION_ORDER).toString()).intValue();
                  
                  int intHighestQuestionNo = 0;
                  
                  my_base_channel.clearWhere();
                  
                  
                  
                  hashStudyFormFields = StudyFormFields.getSurveyQuestionsFormFields();
                  
                  hashStudyFormFields.remove(StudyFormFields.INTERNAL_QUESTION_ID);
                  
                  
                  
                  // Add one to the higest to add it to the bottom of the survey
                  
                  runtimeData.setParameter(StudyFormFields.QUESTION_ORDER, new Integer(intHighestQuestionOrder + 1).toString());
                  
                  // If it's not a comment or a title or a page break add the question number
                  
                  if(new Integer(runtimeData.getParameter(StudyFormFields.QUESTION_TYPE)).intValue() != StudyFormFields.QUESTION_TYPE_COMMENT && new Integer(runtimeData.getParameter(StudyFormFields.QUESTION_TYPE)).intValue() != StudyFormFields.QUESTION_TYPE_TITLE && new Integer(runtimeData.getParameter(StudyFormFields.QUESTION_TYPE)).intValue() != StudyFormFields.QUESTION_TYPE_PAGEBREAK)
                     
                  {
                     
                     intHighestQuestionNo = new Integer(hashMaxFields.get(StudyFormFields.QUESTION_NUMBER).toString()).intValue() + 1;
                     
                  }
                  
                  else
                  {intHighestQuestionNo = 0;} //Don't add a question number if its a comment, title or page break
                  
                  runtimeData.setParameter(StudyFormFields.QUESTION_NUMBER, new Integer(intHighestQuestionNo).toString());
                  
                  my_base_channel.setFormFields(hashStudyFormFields);
                  
                  my_base_channel.setRunTimeData(runtimeData);
                  
                  
                  
                  // Check to see data ok
                  
                  my_base_channel.buildAddModifyXMLFile();
                  
                  // Check to see if all the required fields have been filled and the correct data is in the fields
                  
                  if(my_base_channel.getRequiredStatus() == true && my_base_channel.getValidFieldsStatus() == true)
                     
                  {
                     
                     my_base_channel.addRecord();
                     
                     StudyFormFields.unloadFormFields();
                     
                     int intInsertedRecordId = 0;
                     
                     
                     
                     if(my_base_channel.getAddRecordStatus() == true)
                        
                     {
                        
                        intInsertedRecordId = my_base_channel.getInsertedRecordId();
                        
                        my_base_channel.setXMLFile("<" + StudyFormFields.INTERNAL_QUESTION_ID +">" + intInsertedRecordId + "</" + StudyFormFields.INTERNAL_QUESTION_ID + ">");
                        
                     }
                     
                     my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                     
                     my_base_channel.setWhere(DBSchema.AND_CONNECTOR, StudyFormFields.INTERNAL_QUESTION_ID, DBSchema.EQUALS_OPERATOR, new Integer(intInsertedRecordId).toString());
                     
                     // Set the this flag to true to show the bottom half of the xsl page
                     
                     my_base_channel.setXMLFile("<blAddEditQuestion>true</blAddEditQuestion>");
                     
                     hashStudyFormFields = StudyFormFields.getSurveyQuestionsFormFields();
                     
                     my_base_channel.setFormFields(hashStudyFormFields);
                     
                     my_base_channel.buildViewXMLFile();
                     
                  }
                  
               }
               
               // THey are adding duplicating a question @@@@
               
               else if(runtimeData.getParameter("intQuestionID") != null && runtimeData.getParameter("duplicateQuestion") != null)
                  
               {
                  
                  strStylesheet = SURVEY_QUESTIONS;
                  
                  my_base_channel.clearDomains();
                  
                  my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
                  
                  // Get the highest question no and question order to add to the newly created question
                  
                  Hashtable hashTempHashtable = new Hashtable();
                  
                  hashTempHashtable.put(StudyFormFields.QUESTION_ORDER, "NA");
                  
                  hashTempHashtable.put(StudyFormFields.QUESTION_NUMBER, "NA");
                  
                  my_base_channel.setFormFields(hashTempHashtable);
                  
                  my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                  
                  // Run the query to get the max for question number and order
                  
                  my_base_channel.getMax();
                  
                  Hashtable hashMaxFields =  my_base_channel.getMaxFields();
                  
                  int intHighestQuestionOrder = new Integer(hashMaxFields.get(StudyFormFields.QUESTION_ORDER).toString()).intValue();
                  
                  int intHighestQuestionNo = 0;
                  
                  my_base_channel.clearWhere();
                  
                  
                  
                  hashStudyFormFields = StudyFormFields.getSurveyQuestionsFormFields();
                  
                  hashStudyFormFields.remove(StudyFormFields.INTERNAL_QUESTION_ID);
                  
                  
                  
                  // Add one to the higest to add it to the bottom of the survey
                  
                  runtimeData.setParameter(StudyFormFields.QUESTION_ORDER, new Integer(intHighestQuestionOrder + 1).toString());
                  
                  // If it's not a comment or a title or a page break add the question number
                  
                  if(new Integer(runtimeData.getParameter(StudyFormFields.QUESTION_TYPE)).intValue() != StudyFormFields.QUESTION_TYPE_COMMENT && new Integer(runtimeData.getParameter(StudyFormFields.QUESTION_TYPE)).intValue() != StudyFormFields.QUESTION_TYPE_TITLE && new Integer(runtimeData.getParameter(StudyFormFields.QUESTION_TYPE)).intValue() != StudyFormFields.QUESTION_TYPE_PAGEBREAK)
                     
                  {
                     
                     intHighestQuestionNo = new Integer(hashMaxFields.get(StudyFormFields.QUESTION_NUMBER).toString()).intValue() + 1;
                     
                  }
                  
                  else
                  {intHighestQuestionNo = 0;} //Don't add a question number if its a comment, title or page break
                  
                  runtimeData.setParameter(StudyFormFields.QUESTION_NUMBER, new Integer(intHighestQuestionNo).toString());
                  
                  my_base_channel.setFormFields(hashStudyFormFields);
                  
                  my_base_channel.setRunTimeData(runtimeData);
                  
                  
                  
                  // Check to see data ok
                  
                  my_base_channel.buildAddModifyXMLFile();
                  
                  // Check to see if all the required fields have been filled and the correct data is in the fields
                  
                  if(my_base_channel.getRequiredStatus() == true && my_base_channel.getValidFieldsStatus() == true)
                     
                  {
                     
                     my_base_channel.addRecord();
                     
                     StudyFormFields.unloadFormFields();
                     
                     int intInsertedRecordId = 0;
                     
                     
                     
                     if(my_base_channel.getAddRecordStatus() == true)
                        
                     {
                        
                        intInsertedRecordId = my_base_channel.getInsertedRecordId();
                        
                        // Copy the options if it is a drop down question
                        
                        setDuplicateDropDown(runtimeData.getParameter("intQuestionID"), new Integer(intInsertedRecordId).toString());
                        
                        my_base_channel.setXMLFile("<" + StudyFormFields.INTERNAL_QUESTION_ID +">" + intInsertedRecordId + "</" + StudyFormFields.INTERNAL_QUESTION_ID + ">");
                        
                     }
                     
                     my_base_channel.clearDomains();
                     
                     my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
                     
                     my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                     
                     my_base_channel.setWhere(DBSchema.AND_CONNECTOR, StudyFormFields.INTERNAL_QUESTION_ID, DBSchema.EQUALS_OPERATOR, new Integer(intInsertedRecordId).toString());
                     
                     // Set the this flag to true to show the bottom half of the xsl page
                     
                     my_base_channel.setXMLFile("<blAddEditQuestion>true</blAddEditQuestion>");
                     
                     hashStudyFormFields = StudyFormFields.getSurveyQuestionsFormFields();
                     
                     my_base_channel.setFormFields(hashStudyFormFields);
                     
                     my_base_channel.buildViewXMLFile();
                     
                  }
                  
               }
               
               // The default for this page is to build the list box of question for the
               
               my_base_channel.clearDomains();
               
               my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
               
               hashStudyFormFields = StudyFormFields.getSurveyQuestionsFormFields();
               
               my_base_channel.setFormFields(hashStudyFormFields);
               
               my_base_channel.clearWhere();
               
               my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
               
               // This reduces the display for the question for the question order
               
               hashStudyCutSize = StudyFormFields.getStudySurveyFormFieldsCutSize();
               
               my_base_channel.sethashCutFields(hashStudyCutSize);
               
               my_base_channel.setOrderBy(StudyFormFields.QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);
               
               my_base_channel.buildSearchResultsXMLFile();
               
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><survey>" + my_base_channel.getXMLFile() + strPermissionStudySettings + "</survey>";
               
               
            }
            
         }
         
         // --------------------- ADDING OPTIONS TO A QUESTION ---------------------------
         
         else if (strCurrent != null && strCurrent.equals(SURVEY_OPTIONS))
            
         {
            
            my_base_channel.setActivity(SURVEY_QUESTIONS);
            
            if (!authToken.hasActivity(SURVEY_QUESTIONS)) // Check to see if the patient has access
               
            {
               
               strXML  = buildSecurityErrorXMLFile("add survey options");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            
            else // If they are allowed to add options, let them!!!!!!!!!
               
            {
               
               strStylesheet = SURVEY_OPTIONS;
               
               hashStudyFormFields = StudyFormFields.getSurveyQuestionOptionsFormFields();
               
               my_base_channel.setFormFields(hashStudyFormFields);
               
               
               my_base_channel.buildDefaultXMLFile();
               
               // This passes the survey name and id back to the stlysheet. Save doing any query
               
               my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(Utilities.base64Decode(runtimeData.getParameter("strSurveyNameURL"))) + "</strSurveyName>");
               
               my_base_channel.setXMLFile("<strSurveyNameURL>" + runtimeData.getParameter("strSurveyNameURL") + "</strSurveyNameURL>");
               
               my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
               
               my_base_channel.setXMLFile("<intQuestionID>" + runtimeData.getParameter("intQuestionID") + "</intQuestionID>");
               
               
               
               if(runtimeData.getParameter("addNewOption") != null) // They are add a new option
                  
               {
                  
                  // This tag tells the stylesheet whether to display the details of the option
                  
                  my_base_channel.setXMLFile("<blAddEditOption>true</blAddEditOption>");
                  
               }
               
               else if(runtimeData.getParameter("intOptionID") != null) // They have selected an option to edit
                  
               {
                  
                  
                  my_base_channel.clearDomains();
                  
                  my_base_channel.setDomain(StudyFormFields.SURVEY_OPTIONS_DOMAIN);
                  
                  my_base_channel.setWhere("", StudyFormFields.INTERNAL_OPTION_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_OPTION_ID).toString());
                  
                  hashStudyFormFields = StudyFormFields.getSurveyQuestionOptionsFormFields();
                  
                  my_base_channel.setFormFields(hashStudyFormFields);
                  
                  my_base_channel.setRunTimeData(runtimeData);
                  
                  // See if they have tried to save the record
                  
                  if(runtimeData.getParameter("saveOption") != null) // They are updating an option
                     
                  {
                     
                     my_base_channel.buildAddModifyXMLFile();
                     
                     // Check to see if all the required fields have been filled and the correct data is in the fields
                     
                     if(my_base_channel.getRequiredStatus() == true && my_base_channel.getValidFieldsStatus() == true && my_base_channel.getValidDataStatus() == true)
                        
                     {
                        
                        my_base_channel.updateRecord();
                        
                        
                        
                     }
                     
                  }
                  
                  // They want to move the option up
                  
                  else if(runtimeData.getParameter("moveUp") != null)
                     
                  {
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.setOptionOrder(new Integer(runtimeData.getParameter("intOptionID")).intValue(), new Integer(runtimeData.getParameter("intQuestionID")).intValue(), StudyFormFields.OPTION_MOVE_UP);
                     
                  }
                  
                  // They want to move the option up
                  
                  else if(runtimeData.getParameter("moveDown") != null)
                     
                  {
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.setOptionOrder(new Integer(runtimeData.getParameter("intOptionID")).intValue(), new Integer(runtimeData.getParameter("intQuestionID")).intValue(), StudyFormFields.OPTION_MOVE_DOWN);
                     
                  }
                  
                  // They want to delete an option
                  
                  if(runtimeData.getParameter("delete") != null)
                     
                  {
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.setXMLFile("<blAddEditOption>false</blAddEditOption>");
                     
                     my_base_channel.deleteOption(new Integer(runtimeData.getParameter("intOptionID")).intValue(), new Integer(runtimeData.getParameter("intQuestionID")).intValue());
                     
                  }
                  
                  else
                  {
                     my_base_channel.setXMLFile("<blAddEditOption>true</blAddEditOption>");}
                  
                  // Build the view option XML
                  
                  my_base_channel.buildViewXMLFile();
                  
               }
               
               // THey are adding a new option
               
               else if(runtimeData.getParameter("intOptionID") == null && runtimeData.getParameter("saveOption") != null)
                  
               {
                  
                  // Tell the page to display the lower part
                  
                  my_base_channel.setXMLFile("<blAddEditOption>true</blAddEditOption>");
                  
                  my_base_channel.clearDomains();
                  
                  my_base_channel.setDomain(StudyFormFields.SURVEY_OPTIONS_DOMAIN);
                  
                  // Get the highest option order to add to the newly created option
                  
                  my_base_channel.setRunTimeData(runtimeData);
                  
                  hashStudyFormFields = StudyFormFields.getSurveyQuestionOptionsFormFields();
                  
                  hashStudyFormFields.remove(StudyFormFields.INTERNAL_OPTION_ID);
                  
                  my_base_channel.setFormFields(hashStudyFormFields);
                  
                  my_base_channel.buildAddModifyXMLFile();
                  
                  StudyFormFields.unloadFormFields();
                  
                  
                  
                  // Check to see if all the required fields have been filled and the correct data is in the fields
                  
                  if(my_base_channel.getRequiredStatus() == true && my_base_channel.getValidFieldsStatus() == true && my_base_channel.getValidDataStatus() == true)
                     
                  {
                     
                     Hashtable hashTempHashtable = new Hashtable();
                     
                     hashTempHashtable.put(StudyFormFields.OPTION_ORDER, "NA");
                     
                     my_base_channel.setFormFields(hashTempHashtable);
                     
                     my_base_channel.setWhere("", StudyFormFields.INTERNAL_QUESTION_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_QUESTION_ID).toString());
                     
                     // Run the query to get the max
                     
                     my_base_channel.getMax();
                     
                     
                     
                     Hashtable hashMaxFields =  my_base_channel.getMaxFields();
                     
                     int intHighestOptionOrder = new Integer(hashMaxFields.get(StudyFormFields.OPTION_ORDER).toString()).intValue();
                     
                     
                     
                     my_base_channel.clearWhere();
                     
                     
                     
                     hashStudyFormFields = StudyFormFields.getSurveyQuestionOptionsFormFields();
                     
                     // Remove the option id as we don't have any data for it because we are adding
                     
                     hashStudyFormFields.remove(StudyFormFields.INTERNAL_OPTION_ID);
                     
                     
                     
                     // Add one to the higest to add it to the bottom of the survey
                     
                     runtimeData.setParameter(StudyFormFields.OPTION_ORDER, new Integer(intHighestOptionOrder + 1).toString());
                     
                     my_base_channel.setFormFields(hashStudyFormFields);
                     
                     // Add the new option
                     
                     my_base_channel.addRecord();
                     
                     
                     
                     StudyFormFields.unloadFormFields();
                     
                     // Build the view option XML
                     
                     int intInsertedRecordId = 0;
                     
                     if(my_base_channel.getAddRecordStatus() == true)
                        
                     {
                        
                        intInsertedRecordId = my_base_channel.getInsertedRecordId();
                        
                        my_base_channel.setXMLFile("<" + StudyFormFields.INTERNAL_OPTION_ID +">" + intInsertedRecordId + "</" + StudyFormFields.INTERNAL_OPTION_ID + ">");
                        
                     }
                     
                     
                     
                     my_base_channel.clearWhere();
                     
                     my_base_channel.setWhere("", StudyFormFields.INTERNAL_OPTION_ID, DBSchema.EQUALS_OPERATOR, new Integer(intInsertedRecordId).toString());
                     
                     hashStudyFormFields = StudyFormFields.getSurveyQuestionOptionsFormFields();
                     
                     my_base_channel.setFormFields(hashStudyFormFields);
                     
                     my_base_channel.buildViewXMLFile();
                     
                  }
                  
                  
                  
               }
               
               // First time to the page so we have to get the question
               
               if (runtimeData.getParameter("strQuestionURL") == null)
                  
               {
                  
                  
                  my_base_channel.clearDomains();
                  
                  my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
                  
                  my_base_channel.setWhere("", StudyFormFields.INTERNAL_QUESTION_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_QUESTION_ID).toString());
                  
                  Hashtable hashTempForQuestion = new Hashtable(1);
                  
                  hashTempForQuestion.put(StudyFormFields.QUESTION, "NA");
                  
                  my_base_channel.setFormFields(hashTempForQuestion);
                  
                  Vector vecQuestion = my_base_channel.lookupRecord();
                  
                  String strTempQuestion = ((Hashtable)vecQuestion.get(0)).get(StudyFormFields.QUESTION).toString();
                  
                  
                  String question = Utilities.cleanForXSL(strTempQuestion);
                  
                  String questionURL = Utilities.base64Encode(strTempQuestion);
                  
                  
                  my_base_channel.setXMLFile("<strQuestion>" + Utilities.cleanForXSL(strTempQuestion) + "</strQuestion>");
                  
                  my_base_channel.setXMLFile("<strQuestionURL>" + Utilities.base64Encode(strTempQuestion) + "</strQuestionURL>");
                  
               }
               
               else
                  
               {
                  
                  //rennypv.....edited to cleanForXSL to handle the XSL
                  
                  my_base_channel.setXMLFile("<strQuestion>" + Utilities.cleanForXSL(Utilities.base64Decode(runtimeData.getParameter("strQuestionURL"))) + "</strQuestion>");
                  
                  my_base_channel.setXMLFile("<strQuestionURL>" + runtimeData.getParameter("strQuestionURL") + "</strQuestionURL>");
                  
               }
               
               
               
               // Build the list box at the top of the page!
               
               my_base_channel.clearDomains();
               
               my_base_channel.setDomain(StudyFormFields.SURVEY_OPTIONS_DOMAIN);
               
               hashStudyFormFields = StudyFormFields.getSurveyQuestionOptionsFormFields();
               
               my_base_channel.setFormFields(hashStudyFormFields);
               
               my_base_channel.clearWhere();
               
               my_base_channel.setWhere("", StudyFormFields.INTERNAL_QUESTION_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_QUESTION_ID).toString());
               
               // This reduces the display for the question for the question order
               
               hashStudyCutSize = StudyFormFields.getStudySurveyFormFieldsCutSize();
               
               my_base_channel.sethashCutFields(hashStudyCutSize);
               
               my_base_channel.setOrderBy(StudyFormFields.OPTION_ORDER, DBSchema.ORDERBY_ASENDING);
               
               my_base_channel.buildSearchResultsXMLFile();
               
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><survey>" + my_base_channel.getXMLFile() +  strPermissionStudySettings + "</survey>";
               
               
            }
            
            
            
         }
         
         
         
         
         
         // --------------------- COMPLETING A SURVEY ------------------------------------
         
         else if(strCurrent != null && strCurrent.equals(PATIENT_STUDY))
            
         {
            
            my_base_channel.setActivity(SURVEY_COMPLETE);
            
            if (!authToken.hasActivity(SURVEY_COMPLETE)) // Check to see if the patient has access
               
            {
               
               strXML  = buildSecurityErrorXMLFile("enter data for a survey");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            
            else
               
            {
               
               // They are in the survey and have already started entering data!
               
               if(runtimeData.getParameter("action") != null && runtimeData.getParameter("action").equals("completing"))
                  
               {
                  
                  // They have clicked the next button
                  
                  if(runtimeData.getParameter("saveNext") != null)
                     
                  {
                     
                     strStylesheet = SURVEY_COMPLETE;
                     
                     my_base_channel.setNewXMLFile();
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     // Check to see the data from the survey is valid
                     
                     my_base_channel.validateSurveyData();
                     
                     if(my_base_channel.getIsSurveyDataValid() == true) // If the data is ok save it!
                        
                     {
                        my_base_channel.saveSurveyData();
                        
                        // Now build the next page of the survey
                        
                        my_base_channel.setNewXMLFile();
                        
                        my_base_channel.clearDomains();
                        
                        my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
                        
                        hashStudyFormFields = StudyFormFields.getSurveyCompleteFormFields();
                        
                        my_base_channel.setFormFields(hashStudyFormFields);
                        
                        my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                        
                        my_base_channel.setWhere(DBSchema.AND_CONNECTOR, StudyFormFields.QUESTION_ORDER, DBSchema.GREATERTHAN_OPERATOR, runtimeData.getParameter("intNextQuestionOrder").toString());
                        
                        my_base_channel.setOrderBy(StudyFormFields.QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);
                        
                        my_base_channel.buildSurveySearchResultsXMLFile();
                        
                        // If there are more questions to come, tell the XSL to display the next button not the submit and complete button
                        
                        if(my_base_channel.getMoreQuestionsToCome() == true) // If there are more questions to come set the next button flag to true
                           
                        {
                           
                           my_base_channel.setXMLFile("<blNextButton>true</blNextButton>");
                           
                        }
                        
                        // Tell the XSL to display a back button
                        
                        my_base_channel.setXMLFile("<blBackButton>true</blBackButton>");
                        
                        // Tell the XSL to display a back to survey list button
                        
                        my_base_channel.setXMLFile("<blBackToSurveyListButton>true</blBackToSurveyListButton>");
                        
                     }
                     
                     // This variable is used for the back button
                     
                     my_base_channel.setXMLFile("<intFirstQuestionOrder>" + my_base_channel.getFirstQuestionOrder() + "</intFirstQuestionOrder>");
                     
                     // These varibles are static and passed to the XSL to save on query the database
                     
                     my_base_channel.setXMLFile("<intInternalPatientID>" + runtimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
                     
                     my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strSurveyName")) + "</strSurveyName>");
                     
                     my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
                     
                     my_base_channel.setXMLFile("<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>");
                     
                     my_base_channel.setXMLFile("<strStudyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strStudyName")) + "</strStudyName>");
                     
                     // Pass the next question id back to the channel
                     
                     my_base_channel.setXMLFile("<intNextQuestionOrder>" + my_base_channel.getNextQuestionOrder() + "</intNextQuestionOrder>");
                     
                  }
                  
                  // They have clicked the back button button
                  
                  else if(runtimeData.getParameter("saveBack") != null)
                     
                  {
                     
                     strStylesheet = SURVEY_COMPLETE;
                     
                     my_base_channel.setNewXMLFile();
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.validateSurveyData();
                     
                     // Make sure the data is good before saving
                     
                     if(my_base_channel.getIsSurveyDataValid() == true)
                        
                     {
                        
                        my_base_channel.setNewXMLFile();
                        
                        my_base_channel.saveSurveyData();
                        
                        my_base_channel.clearDomains();
                        
                        // The flag below tells the survey base channel to get the previous question set
                        
                        my_base_channel.setBackButton(true);
                        
                        my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
                        
                        hashStudyFormFields = StudyFormFields.getSurveyCompleteFormFields();
                        
                        my_base_channel.setFormFields(hashStudyFormFields);
                        
                        my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                        
                        // Due to back button, must be less than
                        
                        my_base_channel.setWhere(DBSchema.AND_CONNECTOR, StudyFormFields.QUESTION_ORDER, DBSchema.LESSTHAN_OPERATOR, runtimeData.getParameter("intNextQuestionOrder").toString());
                        
                        my_base_channel.setOrderBy(StudyFormFields.QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);
                        
                        my_base_channel.buildSurveySearchResultsXMLFile();
                        
                        // Tell the XSL to diplay the next button, because they have just clicked the back button
                        
                        my_base_channel.setXMLFile("<blNextButton>true</blNextButton>");
                        
                        
                        // Tell the XSL to display the back to survey list button
                        
                        my_base_channel.setXMLFile("<blBackToSurveyListButton>true</blBackToSurveyListButton>");
                        
                        if(my_base_channel.getFirstQuestionOrder() != 1) // If there are more questions to come set the next button flag to true
                           
                        {
                           
                           my_base_channel.setXMLFile("<blBackButton>true</blBackButton>");
                           
                        }
                        
                     }
                     
                     // Pass the standard variables back to the XSL
                     
                     my_base_channel.setXMLFile("<intFirstQuestionOrder>" + my_base_channel.getFirstQuestionOrder() + "</intFirstQuestionOrder>");
                     
                     my_base_channel.setXMLFile("<intInternalPatientID>" + runtimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
                     
                     my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strSurveyName")) + "</strSurveyName>");
                     
                     my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
                     
                     my_base_channel.setXMLFile("<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>");
                     
                     my_base_channel.setXMLFile("<strStudyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strStudyName")) + "</strStudyName>");
                     
                     
                     // Pass the next question id back to the channel
                     
                     my_base_channel.setXMLFile("<intNextQuestionOrder>" + my_base_channel.getNextQuestionOrder() + "</intNextQuestionOrder>");
                     
                     
                     
                  }
                  
                  // They have clicked the final submit button
                  
                  else if(runtimeData.getParameter("saveComplete") != null)
                     
                  {
                     
                     
                     
                     my_base_channel.setNewXMLFile();
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.validateSurveyData();
                     
                     
                     
                     // Make sure the data is good before saving
                     
                     if(my_base_channel.getIsSurveyDataValid() == true)
                        
                     {
                        
                        my_base_channel.setNewXMLFile();
                        
                        my_base_channel.saveSurveyData();
                        
                        buildFinalSurveyScreen();
                        
                        // Unlock the results record
                        
                        lrResultsLock.unlockWrites();
                        
                        unlockResultsRecord();
                        
                        unlockSurveyRecord();
                        
                     }
                     
                     else // Send them back to complete!
                        
                     {
                        
                        strStylesheet = SURVEY_COMPLETE;
                        
                        my_base_channel.setXMLFile("<intFirstQuestionOrder>" + my_base_channel.getFirstQuestionOrder() + "</intFirstQuestionOrder>");
                        
                        my_base_channel.setXMLFile("<intInternalPatientID>" + runtimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
                        
                        my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strSurveyName")) + "</strSurveyName>");
                        
                        my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
                        
                        my_base_channel.setXMLFile("<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>");
                        
                        
                        // Pass the next question id back to the channel
                        
                        my_base_channel.setXMLFile("<intNextQuestionOrder>" + my_base_channel.getNextQuestionOrder() + "</intNextQuestionOrder>");
                        
                     }
                     
                  }
                  
                  
                  
                  
                  
               }
               
               else // First time they are entering the survey
                  
               {
                  
                  
                  
                  // Add the Results and survey lock
                  
                  lrResultsLock = new LockRequest(authToken);
                  
                  lrResultsLock.addLock(StudyFormFields.SURVEY_RESULTS_DOMAIN, runtimeData.getParameter(StudyFormFields.INTERNAL_PATIENT_ID) + "_" + runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID), LockRecord.READ_WRITE);
                  
                  lrSurveyLock = new LockRequest(authToken);
                  
                  lrSurveyLock.addLock(StudyFormFields.SURVEY_DOMAIN,runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID), LockRecord.READ_WRITE);
                  
                  if(lrResultsLock.lockDelayWrite() == true && lrSurveyLock.lockDelayWrite() == true && lrResultsLock.lockWrites() == true)
                     
                  {
                     
                     // Build the first page
                     
                     strStylesheet = SURVEY_COMPLETE;
                     
                     my_base_channel.setNewXMLFile();
                     
                     my_base_channel.clearDomains();
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
                     
                     hashStudyFormFields = StudyFormFields.getSurveyCompleteFormFields();
                     
                     my_base_channel.setFormFields(hashStudyFormFields);
                     
                     my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                     
                     my_base_channel.setOrderBy(StudyFormFields.QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);
                     
                     my_base_channel.buildSurveySearchResultsXMLFile();
                     
                     
                     if(my_base_channel.getMoreQuestionsToCome() == true) // If there are more questions to come set the next button flag to true
                        
                     {
                        
                        my_base_channel.setXMLFile("<blNextButton>true</blNextButton>");
                        
                     }
                     
                     
                     // tell the XSL to show the back to survey list button
                     
                     my_base_channel.setXMLFile("<blBackToSurveyListButton>true</blBackToSurveyListButton>");
                     
                     
                     // This passes the survey name and id back to the stlysheet. Save doing any query
                     
                     my_base_channel.setXMLFile("<intInternalPatientID>" + runtimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
                     
                     // This variable is used if the in case of data input problems
                     
                     my_base_channel.setXMLFile("<intFirstQuestionOrder>" + my_base_channel.getFirstQuestionOrder() + "</intFirstQuestionOrder>");
                     
                     my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strSurveyName")) + "</strSurveyName>");
                     
                     my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
                     
                     my_base_channel.setXMLFile("<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>");
                     
                     my_base_channel.setXMLFile("<strStudyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strStudyName")) + "</strStudyName>");
                     
                     // Pass the next question id back to the channel
                     
                     my_base_channel.setXMLFile("<intNextQuestionOrder>" + my_base_channel.getNextQuestionOrder() + "</intNextQuestionOrder>");
                     
                     
                  }
                  
                  else
                     
                  {
                     
                     my_base_channel.setNewXMLFile();
                     
                     buildFinalSurveyScreen();
                     
                     buildResultsLockError();
                     
                     strStylesheet = SURVEY_LOCK_ERROR;
                     
                  }
                  
               }
               
               
               
               String strOriginXML = "";
               
               if( runtimeData.getParameter("strOrigin") != null )
               {
                  strOriginXML = "<strOrigin>" + runtimeData.getParameter("strOrigin") + "</strOrigin>";
               }
               
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><survey>" + strOriginXML + my_base_channel.getXMLFile() + strPermissionStudySettings + "</survey>";
               
               
               
               
            }
            
         }
         
         
         // --------------------- COMPLETING A SURVEY FOR BIO ------------------------------------
         
         else if(strCurrent != null && strCurrent.equals(BIOSPECIMEN_STUDY))
            
         {
            
            my_base_channel.setActivity(SURVEY_COMPLETE);
            
            if (!authToken.hasActivity(SURVEY_COMPLETE)) // Check to see if the patient has access
               
            {
               
               strXML  = buildSecurityErrorXMLFile("enter data for a survey");
               
               strStylesheet = SECURITY_ERROR;
               
               return;
               
            }
            
            else
               
            {
               
               // They are in the survey and have already started entering data!
               
               if(runtimeData.getParameter("action") != null && runtimeData.getParameter("action").equals("completing"))
                  
               {
                  
                  // They have clicked the next button
                  
                  if(runtimeData.getParameter("saveNext") != null)
                     
                  {
                     
                     strStylesheet = BIO_SURVEY_COMPLETE;
                     
                     my_base_channel.setNewXMLFile();
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     // Check to see the data from the survey is valid
                     
                     my_base_channel.validateSurveyData();
                     
                     if(my_base_channel.getIsSurveyDataValid() == true) // If the data is ok save it!
                        
                     {
                        my_base_channel.saveSurveyDataForBiospecimen();
                        
                        // Now build the next page of the survey
                        
                        my_base_channel.setNewXMLFile();
                        
                        my_base_channel.clearDomains();
                        
                        my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
                        
                        hashStudyFormFields = StudyFormFields.getSurveyCompleteFormFields();
                        
                        my_base_channel.setFormFields(hashStudyFormFields);
                        
                        my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                        
                        my_base_channel.setWhere(DBSchema.AND_CONNECTOR, StudyFormFields.QUESTION_ORDER, DBSchema.GREATERTHAN_OPERATOR, runtimeData.getParameter("intNextQuestionOrder").toString());
                        
                        my_base_channel.setOrderBy(StudyFormFields.QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);
                        
                        my_base_channel.buildBioSurveySearchResultsXMLFile();
                        
                        // If there are more questions to come, tell the XSL to display the next button not the submit and complete button
                        
                        if(my_base_channel.getMoreQuestionsToCome() == true) // If there are more questions to come set the next button flag to true
                           
                        {
                           
                           my_base_channel.setXMLFile("<blNextButton>true</blNextButton>");
                           
                        }
                        
                        // Tell the XSL to display a back button
                        
                        my_base_channel.setXMLFile("<blBackButton>true</blBackButton>");
                        
                        // Tell the XSL to display a back to survey list button
                        
                        my_base_channel.setXMLFile("<blBackToSurveyListButton>true</blBackToSurveyListButton>");
                        
                     }
                     
                     // This variable is used for the back button
                     
                     my_base_channel.setXMLFile("<intFirstQuestionOrder>" + my_base_channel.getFirstQuestionOrder() + "</intFirstQuestionOrder>");
                     
                     // These varibles are static and passed to the XSL to save on query the database
                     
                     my_base_channel.setXMLFile("<intInternalPatientID>" + runtimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
                     
                     my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strSurveyName")) + "</strSurveyName>");
                     
                     my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
                     
                     my_base_channel.setXMLFile("<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>");
                     
                     my_base_channel.setXMLFile("<intBiospecimenID>" + runtimeData.getParameter("intBiospecimenID") + "</intBiospecimenID>");
                     
                     my_base_channel.setXMLFile("<strStudyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strStudyName")) + "</strStudyName>");
                     
                     // Pass the next question id back to the channel
                     
                     my_base_channel.setXMLFile("<intNextQuestionOrder>" + my_base_channel.getNextQuestionOrder() + "</intNextQuestionOrder>");
                     
                     my_base_channel.setXMLFile("<strSource>" + strSource + "</strSource>");
                     
                  }
                  
                  // They have clicked the back button button
                  
                  else if(runtimeData.getParameter("saveBack") != null)
                     
                  {
                     
                     strStylesheet = BIO_SURVEY_COMPLETE;
                     
                     my_base_channel.setNewXMLFile();
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.validateSurveyData();
                     
                     // Make sure the data is good before saving
                     
                     if(my_base_channel.getIsSurveyDataValid() == true)
                        
                     {
                        
                        my_base_channel.setNewXMLFile();
                        
                        my_base_channel.saveSurveyDataForBiospecimen();
                        
                        my_base_channel.clearDomains();
                        
                        // The flag below tells the survey base channel to get the previous question set
                        
                        my_base_channel.setBackButton(true);
                        
                        my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
                        
                        hashStudyFormFields = StudyFormFields.getSurveyCompleteFormFields();
                        
                        my_base_channel.setFormFields(hashStudyFormFields);
                        
                        my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                        
                        // Due to back button, must be less than
                        
                        my_base_channel.setWhere(DBSchema.AND_CONNECTOR, StudyFormFields.QUESTION_ORDER, DBSchema.LESSTHAN_OPERATOR, runtimeData.getParameter("intNextQuestionOrder").toString());
                        
                        my_base_channel.setOrderBy(StudyFormFields.QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);
                        
                        my_base_channel.buildBioSurveySearchResultsXMLFile();
                        
                        // Tell the XSL to diplay the next button, because they have just clicked the back button
                        
                        my_base_channel.setXMLFile("<blNextButton>true</blNextButton>");
                        
                        
                        // Tell the XSL to display the back to survey list button
                        
                        my_base_channel.setXMLFile("<blBackToSurveyListButton>true</blBackToSurveyListButton>");
                        
                        if(my_base_channel.getFirstQuestionOrder() != 1) // If there are more questions to come set the next button flag to true
                           
                        {
                           
                           my_base_channel.setXMLFile("<blBackButton>true</blBackButton>");
                           
                        }
                        
                     }
                     
                     // Pass the standard variables back to the XSL
                     
                     my_base_channel.setXMLFile("<intFirstQuestionOrder>" + my_base_channel.getFirstQuestionOrder() + "</intFirstQuestionOrder>");
                     
                     my_base_channel.setXMLFile("<intInternalPatientID>" + runtimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
                     
                     my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strSurveyName")) + "</strSurveyName>");
                     
                     my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
                     
                     my_base_channel.setXMLFile("<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>");
                     
                     my_base_channel.setXMLFile("<strStudyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strStudyName")) + "</strStudyName>");
                     
                     my_base_channel.setXMLFile("<intBiospecimenID>" + runtimeData.getParameter("intBiospecimenID") + "</intBiospecimenID>");
                     
                     // Pass the next question id back to the channel
                     
                     my_base_channel.setXMLFile("<intNextQuestionOrder>" + my_base_channel.getNextQuestionOrder() + "</intNextQuestionOrder>");
                     
                     my_base_channel.setXMLFile("<strSource>" + strSource + "</strSource>");
                     
                  }
                  
                  // They have clicked the final submit button
                  
                  else if(runtimeData.getParameter("saveComplete") != null)
                     
                  {
                     
                     
                     
                     my_base_channel.setNewXMLFile();
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.validateSurveyData();
                     
                     
                     
                     // Make sure the data is good before saving
                     
                     if(my_base_channel.getIsSurveyDataValid() == true)
                        
                     {
                        
                        my_base_channel.setNewXMLFile();
                        
                        my_base_channel.saveSurveyDataForBiospecimen();
                        
                        buildFinalBioSurveyScreen();
                        
                        // Unlock the results record
                        
                        lrResultsLock.unlockWrites();
                        
                        unlockResultsRecord();
                        
                        unlockSurveyRecord();
                        
                     }
                     
                     else // Send them back to complete!
                        
                     {
                        
                        strStylesheet = BIO_SURVEY_COMPLETE;
                        
                        my_base_channel.setXMLFile("<intFirstQuestionOrder>" + my_base_channel.getFirstQuestionOrder() + "</intFirstQuestionOrder>");
                        
                        my_base_channel.setXMLFile("<intInternalPatientID>" + runtimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
                        
                        my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strSurveyName")) + "</strSurveyName>");
                        
                        my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
                        
                        my_base_channel.setXMLFile("<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>");
                        
                        my_base_channel.setXMLFile("<intBiospecimenID>" + runtimeData.getParameter("intBiospecimenID") + "</intBiospecimenID>");
                        
                        // Pass the next question id back to the channel
                        
                        my_base_channel.setXMLFile("<intNextQuestionOrder>" + my_base_channel.getNextQuestionOrder() + "</intNextQuestionOrder>");
                        
                        my_base_channel.setXMLFile("<strSource>" + strSource + "</strSource>");
                     }
                     
                  }
                  
                  
                  
                  
                  
               }
               
               else // First time they are entering the survey
                  
               {
                  
                  
                  // Add the Results and survey lock
                  
                  lrResultsLock = new LockRequest(authToken);
                  
                  lrResultsLock.addLock(StudyFormFields.SURVEY_RESULTS_DOMAIN, runtimeData.getParameter(StudyFormFields.INTERNAL_PATIENT_ID) + "_" + runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID), LockRecord.READ_WRITE);
                  
                  lrSurveyLock = new LockRequest(authToken);
                  
                  lrSurveyLock.addLock(StudyFormFields.SURVEY_DOMAIN,runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID), LockRecord.READ_WRITE);
                  
                  if(lrResultsLock.lockDelayWrite() == true && lrSurveyLock.lockDelayWrite() == true && lrResultsLock.lockWrites() == true)
                     
                  {
                     
                     // Build the first page
                     
                     strStylesheet = BIO_SURVEY_COMPLETE;
                     
                     my_base_channel.setNewXMLFile();
                     
                     my_base_channel.clearDomains();
                     
                     my_base_channel.setRunTimeData(runtimeData);
                     
                     my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
                     
                     hashStudyFormFields = StudyFormFields.getSurveyCompleteFormFields();
                     
                     my_base_channel.setFormFields(hashStudyFormFields);
                     
                     my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID).toString());
                     
                     my_base_channel.setOrderBy(StudyFormFields.QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);
                     
                     my_base_channel.buildBioSurveySearchResultsXMLFile();
                     
                     
                     if(my_base_channel.getMoreQuestionsToCome() == true) // If there are more questions to come set the next button flag to true
                        
                     {
                        
                        my_base_channel.setXMLFile("<blNextButton>true</blNextButton>");
                        
                     }
                     
                     
                     // tell the XSL to show the back to survey list button
                     
                     my_base_channel.setXMLFile("<blBackToSurveyListButton>true</blBackToSurveyListButton>");
                     
                     
                     // This passes the survey name and id back to the stlysheet. Save doing any query
                     
                     my_base_channel.setXMLFile("<intInternalPatientID>" + runtimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
                     
                     // This variable is used if the in case of data input problems
                     
                     my_base_channel.setXMLFile("<intFirstQuestionOrder>" + my_base_channel.getFirstQuestionOrder() + "</intFirstQuestionOrder>");
                     
                     my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strSurveyName")) + "</strSurveyName>");
                     
                     my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
                     
                     my_base_channel.setXMLFile("<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>");
                     
                     my_base_channel.setXMLFile("<strStudyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strStudyName")) + "</strStudyName>");
                     
                     // Pass the next question id back to the channel
                     
                     my_base_channel.setXMLFile("<intNextQuestionOrder>" + my_base_channel.getNextQuestionOrder() + "</intNextQuestionOrder>");
                     
                     my_base_channel.setXMLFile("<intBiospecimenID>" + runtimeData.getParameter("intBiospecimenID") + "</intBiospecimenID>");
                     
                     strSource = runtimeData.getParameter("strSource");
                     my_base_channel.setXMLFile("<strSource>" + strSource + "</strSource>");
                  }
                  
                  else
                     
                  {
                     
                     my_base_channel.setNewXMLFile();
                     
                     buildFinalBioSurveyScreen();
                     
                     buildResultsLockError();
                     
                     strStylesheet = SURVEY_LOCK_ERROR;
                     
                  }
                  
               }               
               
               
               String strOriginXML = "";
               
               if( runtimeData.getParameter("strOrigin") != null )
               {
                  strOriginXML = "<strOrigin>" + runtimeData.getParameter("strOrigin") + "</strOrigin>";
               }
               
               strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><survey>" + strOriginXML + my_base_channel.getXMLFile() + strPermissionStudySettings + "</survey>";
               
               
            }
            
         }

         // ----------KILL THE BASE CHANNEL OBJECT --------------------
         
         my_base_channel = null;
         
         
         
      }
      
      catch(neuragenix.common.exception.BaseChannelException bce)
      
      {
         
         System.out.println(bce.toString());
         
         LogService.instance().log(LogService.ERROR, "Base channel error error in study Channel - " + bce.toString());
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   /**
    *
    *  The following function is fundamentally flawed in that it doesnt add participants correctly.
    *  -- needs to cycle through all the patients and add the patients consented to the study to the
    *     smartform paricipants and patient_smartform table
    *
    */
   
   
   private void addSmartformToParticipants()
   {
      
      boolean blUseThread = true;
      try
      {
         blUseThread = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.study.useThreadedSmartformLinking");
      }
      catch (Exception e)
      {
         e.printStackTrace();
      }
      
      if (blUseThread == true)
      {
         
         int intStudyKey = Integer.parseInt(runtimeData.getParameter("intStudyID"));
         int intSmartformKey = Integer.parseInt(runtimeData.getParameter("intSmartformKey"));
         
         SmartformAttacher sfa = new SmartformAttacher (authToken, intStudyKey, intSmartformKey);
         sfa.start(); // start adding -- at which point we no longer care.
         
         my_base_channel.setXMLFile("<strError>The system has begun to add the forms.  It may be a few minutes before these are available</strError>");
         
      }
      else 
      {
         try
         {
            DALSecurityQuery patSFQuery = new DALSecurityQuery(STUDY_VIEW, authToken);
            patSFQuery.clearDomains();
            patSFQuery.clearFields();
            patSFQuery.clearWhere();
            patSFQuery.setDomain("PATIENT_SMARTFORM", null, null, null);
            patSFQuery.setFields(DatabaseSchema.getFormFields("cstudy_check_patient_smartform"),null);
            patSFQuery.setDistinctField("PATIENT_SMARTFORM_intInternalPatientID");
            patSFQuery.setWhere(null,0,"PATIENT_SMARTFORM_intStudyID","=", runtimeData.getParameter("intStudyID"),0,DALQuery.WHERE_HAS_VALUE);
            patSFQuery.setWhere("AND",0,"PATIENT_SMARTFORM_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
            ResultSet rscheck = patSFQuery.executeSelect();
            while(rscheck.next())
            {
               
               DALSecurityQuery insertQuery = new DALSecurityQuery(STUDY_VIEW, authToken);
               String patientKey = rscheck.getString("PATIENT_SMARTFORM_intInternalPatientID");
               runtimeData.setParameter("SMARTFORMPARTICIPANTS_intParticipantID",rscheck.getString("PATIENT_SMARTFORM_intInternalPatientID"));
               runtimeData.setParameter("SMARTFORMPARTICIPANTS_strDomain","Study");
               
               runtimeData.setParameter("SMARTFORMPARTICIPANTS_intSmartformID",runtimeData.getParameter("intSmartformKey"));
               
               runtimeData.setParameter("SMARTFORMPARTICIPANTS_strSmartformStatus","Not started");
               
               runtimeData.setParameter("SMARTFORMPARTICIPANTS_strAddedBy",authToken.getUserIdentifier());
               
               SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
               
               Date dtcurrent = new Date();
               
               runtimeData.setParameter("SMARTFORMPARTICIPANTS_dtDateAdded", sdf.format(dtcurrent));
               insertQuery.clearDomains();
               insertQuery.clearFields();
               insertQuery.clearWhere();
               insertQuery.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
               insertQuery.setFields(DatabaseSchema.getFormFields("cstudy_smartform_participant_add"),runtimeData);
               if(insertQuery.executeInsert())
               {
                  int newkey = QueryChannel.getNewestKeyAsInt(insertQuery);
                  runtimeData.setParameter("PATIENT_SMARTFORM_intSmartformKey",Integer.toString(newkey));
                  runtimeData.setParameter("PATIENT_SMARTFORM_intInternalPatientID",patientKey);
                  runtimeData.setParameter("PATIENT_SMARTFORM_intStudyID",runtimeData.getParameter("intStudyID"));
                  insertQuery.clearDomains();
                  insertQuery.clearFields();
                  insertQuery.clearWhere();
                  insertQuery.setDomain("PATIENT_SMARTFORM", null, null, null);
                  insertQuery.setFields(DatabaseSchema.getFormFields("cstudy_add_patient_smartform"),runtimeData);
                  insertQuery.executeInsert();
               }
               else
               {
                  System.err.println("[Study Channel] Update failed adding smartform participants");
               }
               
            }
            rscheck.close();
         }
         catch(Exception e)
         {
            e.printStackTrace();
         }
      }
      
   }
   private void addSmartformToBiospecimen()
   {
      try
      {
         
         DALSecurityQuery bioSFQuery = new DALSecurityQuery(STUDY_VIEW, authToken);
         bioSFQuery.clearDomains();
         bioSFQuery.clearFields();
         bioSFQuery.clearWhere();
         bioSFQuery.setDomain("BIOSPECIMEN_SMARTFORM", null, null, null);
         bioSFQuery.setFields(DatabaseSchema.getFormFields("cstudy_check_biospecimen_smartform"),null);
         bioSFQuery.setDistinctField("BIOSPECIMEN_SMARTFORM_intBiospecimenID");
         bioSFQuery.setWhere(null,0,"BIOSPECIMEN_SMARTFORM_intStudyID","=", runtimeData.getParameter("intStudyID"),0,DALQuery.WHERE_HAS_VALUE);
         bioSFQuery.setWhere("AND",0,"BIOSPECIMEN_SMARTFORM_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
         ResultSet rscheck = bioSFQuery.executeSelect();
         while(rscheck.next())
         {
            String BiospecimenKey = rscheck.getString("BIOSPECIMEN_SMARTFORM_intBiospecimenID");
            runtimeData.setParameter("SMARTFORMPARTICIPANTS_intParticipantID",rscheck.getString("BIOSPECIMEN_SMARTFORM_intBiospecimenID"));
            runtimeData.setParameter("SMARTFORMPARTICIPANTS_strDomain","Biospecimen");
            
            runtimeData.setParameter("SMARTFORMPARTICIPANTS_intSmartformID",runtimeData.getParameter("intSmartformKey"));
            
            runtimeData.setParameter("SMARTFORMPARTICIPANTS_strSmartformStatus","Not started");
            
            runtimeData.setParameter("SMARTFORMPARTICIPANTS_strAddedBy",authToken.getUserIdentifier());
            
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy");
            
            Date dtcurrent = new Date();
            
            
            runtimeData.setParameter("SMARTFORMPARTICIPANTS_dtDateAdded", sdf.format(dtcurrent));
            DALSecurityQuery insertQuery = new DALSecurityQuery(STUDY_VIEW, authToken);
            insertQuery.clearDomains();
            insertQuery.clearFields();
            insertQuery.clearWhere();
            insertQuery.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
            insertQuery.setFields(DatabaseSchema.getFormFields("cstudy_smartform_participant_add"),runtimeData);
            if(insertQuery.executeInsert())
               
            {
               int newkey = QueryChannel.getNewestKeyAsInt(insertQuery);
               runtimeData.setParameter("BIOSPECIMEN_SMARTFORM_intSmartformKey",Integer.toString(newkey));
               runtimeData.setParameter("BIOSPECIMEN_SMARTFORM_intBiospecimenID",BiospecimenKey);
               runtimeData.setParameter("BIOSPECIMEN_SMARTFORM_intStudyID",runtimeData.getParameter("intStudyID"));
               insertQuery.clearDomains();
               insertQuery.clearFields();
               insertQuery.clearWhere();
               insertQuery.setDomain("BIOSPECIMEN_SMARTFORM", null, null, null);
               insertQuery.setFields(DatabaseSchema.getFormFields("cstudy_add_biospecimen_smartform"),runtimeData);
               insertQuery.executeInsert();
            }
            
         }
         rscheck.close();
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
      
   }
   private void deleteSmartformFromParticipants()
   {
      try
      {
         DALSecurityQuery patSFQuery = new DALSecurityQuery(STUDY_VIEW, authToken);
         patSFQuery.clearDomains();
         patSFQuery.clearFields();
         patSFQuery.clearWhere();
         patSFQuery.setDomain("PATIENT_SMARTFORM", null, null, null);
         patSFQuery.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "LEFT JOIN");
         patSFQuery.setField("SMARTFORMPARTICIPANTS_intSmartformParticipantID",null);
         patSFQuery.setWhere(null,0,"PATIENT_SMARTFORM_intStudyID","=", runtimeData.getParameter("intStudyID"),0,DALQuery.WHERE_HAS_VALUE);
         patSFQuery.setWhere("AND",0,"PATIENT_SMARTFORM_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
         patSFQuery.setWhere("AND",0,"SMARTFORMPARTICIPANTS_intSmartformID","=", runtimeData.getParameter("intSurveyID"),0,DALQuery.WHERE_HAS_VALUE);
         patSFQuery.setWhere("AND",0,"SMARTFORMPARTICIPANTS_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
         ResultSet rsparticipantKey = patSFQuery.executeSelect();
         while(rsparticipantKey.next())
         {
            DALSecurityQuery updateQuery = new DALSecurityQuery(STUDY_VIEW, authToken);
            updateQuery.clearDomains();
            updateQuery.clearFields();
            updateQuery.clearWhere();
            updateQuery.setDomain("PATIENT_SMARTFORM", null, null, null);
            updateQuery.setField("PATIENT_SMARTFORM_intDeleted","-1");
            updateQuery.setWhere(null,0,"PATIENT_SMARTFORM_intSmartformKey","=", rsparticipantKey.getString("SMARTFORMPARTICIPANTS_intSmartformParticipantID"),0,DALQuery.WHERE_HAS_VALUE);
            if(updateQuery.executeUpdate())
            {
               updateQuery.clearDomains();
               updateQuery.clearFields();
               updateQuery.clearWhere();
               updateQuery.setDomain("SMARTFORMRESULTS", null, null, null);
               updateQuery.setField("SMARTFORMRESULTS_intDeleted","-1");
               updateQuery.setWhere(null,0,"SMARTFORMRESULTS_intSmartformParticipantID","=", rsparticipantKey.getString("SMARTFORMPARTICIPANTS_intSmartformParticipantID"),0,DALQuery.WHERE_HAS_VALUE);
               if(updateQuery.executeUpdate())
               {
                  updateQuery.clearDomains();
                  updateQuery.clearFields();
                  updateQuery.clearWhere();
                  updateQuery.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
                  updateQuery.setField("SMARTFORMPARTICIPANTS_intDeleted","-1");
                  updateQuery.setWhere(null,0,"SMARTFORMPARTICIPANTS_intSmartformParticipantID","=", rsparticipantKey.getString("SMARTFORMPARTICIPANTS_intSmartformParticipantID"),0,DALQuery.WHERE_HAS_VALUE);
               }
            }
         }
         rsparticipantKey.close();
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
      
   }
   
   private void deleteSmartformFromBiospecimen()
   {
      try
      {
         DALSecurityQuery bioSFQuery = new DALSecurityQuery(STUDY_VIEW, authToken);
         bioSFQuery.clearDomains();
         bioSFQuery.clearFields();
         bioSFQuery.clearWhere();
         bioSFQuery.setDomain("BIOSPECIMEN_SMARTFORM", null, null, null);
         bioSFQuery.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "LEFT JOIN");
         bioSFQuery.setField("SMARTFORMPARTICIPANTS_intSmartformParticipantID",null);
         bioSFQuery.setWhere(null,0,"BIOSPECIMEN_SMARTFORM_intStudyID","=", runtimeData.getParameter("intStudyID"),0,DALQuery.WHERE_HAS_VALUE);
         bioSFQuery.setWhere("AND",0,"BIOSPECIMEN_SMARTFORM_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
         bioSFQuery.setWhere("AND",0,"SMARTFORMPARTICIPANTS_intSmartformID","=", runtimeData.getParameter("intSurveyID"),0,DALQuery.WHERE_HAS_VALUE);
         bioSFQuery.setWhere("AND",0,"SMARTFORMPARTICIPANTS_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
         ResultSet rsparticipantKey = bioSFQuery.executeSelect();
         while(rsparticipantKey.next())
         {
            DALSecurityQuery updateQuery = new DALSecurityQuery(STUDY_VIEW, authToken);
            updateQuery.clearDomains();
            updateQuery.clearFields();
            updateQuery.clearWhere();
            updateQuery.setDomain("BIOSPECIMEN_SMARTFORM", null, null, null);
            updateQuery.setField("BIOSPECIMEN_SMARTFORM_intDeleted","-1");
            updateQuery.setWhere(null,0,"BIOSPECIMEN_SMARTFORM_intSmartformKey","=", rsparticipantKey.getString("SMARTFORMPARTICIPANTS_intSmartformParticipantID"),0,DALQuery.WHERE_HAS_VALUE);
            if(updateQuery.executeUpdate())
            {
               updateQuery.clearDomains();
               updateQuery.clearFields();
               updateQuery.clearWhere();
               updateQuery.setDomain("SMARTFORMRESULTS", null, null, null);
               updateQuery.setField("SMARTFORMRESULTS_intDeleted","-1");
               updateQuery.setWhere(null,0,"SMARTFORMRESULTS_intSmartformParticipantID","=", rsparticipantKey.getString("SMARTFORMPARTICIPANTS_intSmartformParticipantID"),0,DALQuery.WHERE_HAS_VALUE);
               if(updateQuery.executeUpdate())
               {
                  updateQuery.clearDomains();
                  updateQuery.clearFields();
                  updateQuery.clearWhere();
                  updateQuery.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
                  updateQuery.setField("SMARTFORMPARTICIPANTS_intDeleted","-1");
                  updateQuery.setWhere(null,0,"SMARTFORMPARTICIPANTS_intSmartformParticipantID","=", rsparticipantKey.getString("SMARTFORMPARTICIPANTS_intSmartformParticipantID"),0,DALQuery.WHERE_HAS_VALUE);
               }
            }
         }
         rsparticipantKey.close();
      }
      catch(Exception e)
      {
         e.printStackTrace();
      }
      
   }
   
   /** Output channel content to the portal
   
    *  @param out a sax document handler
   
    */
   
   public void renderXML(ContentHandler out) throws PortalException
   {
      
      
      
      
      
      // Create a new XSLT styling engine
      
      XSLT xslt = new XSLT(this);
      
      
      // pass the result xml to the styling engine.
      
      xslt.setXML(strXML);
      
      // specify the stylesheet selector
      
      xslt.setXSL("CStudy.ssl", strStylesheet, runtimeData.getBrowserInfo());
      
      
      
      // set parameters that the stylesheet needs.
      
      xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL());
      
      
      //Start Secure Download Implementation
      
      org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL( true ));
      
      upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CDownload"));
      xslt.setStylesheetParameter("downloadURL", upfTmp.getUPFile());
      xslt.setStylesheetParameter("nodeId", SessionManager.getChannelID(strSessionUniqueID, "CDownload"));
      
      //End Secure
      //rennypv-----Smartform
      
      
      /*upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CSmartform"));
      xslt.setStylesheetParameter("smartformChannelURL", upfTmp.getUPFile());
      
      upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CSmartformAdmin"));
      
      xslt.setStylesheetParameter("smartformAdminChannelURL", upfTmp.getUPFile());
      xslt.setStylesheetParameter( "smartformAdminChannelTabOrder", SessionManager.getTabOrder( authToken, "CSmartformAdmin") );*/
	
      
      //rennypv------End of Smartform
      
      //set the URL for the survey channel link used to link from Patient to Study
      
      upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getUPFile());
      
      
      //    upfTmp.setTargetNodeId("n" + ChannelIDs.getPatientChannelID());
      /*upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CPatient"));
      
      xslt.setStylesheetParameter("patientChannelURL", upfTmp.getUPFile());
      xslt.setStylesheetParameter( "patientChannelTabOrder", SessionManager.getTabOrder( authToken, "CPatient") );*/
      
      
      
      // Set the biospecimen link
      
      
      
      //upfTmp.setTargetNodeId("n" + ChannelIDs.getBiospecimenChannelID());
      
      //xslt.setStylesheetParameter("biospecimenChannelURL", upfTmp.getUPFile());
      
      upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CBiospecimen"));
      xslt.setStylesheetParameter("biospecimenChannelURL", upfTmp.getUPFile());
      
      
      
      
      
      // set the output Handler for the output.
      
      xslt.setTarget(out);
      
      
      
      // do the deed
      
      xslt.transform();
      
   }
   
   private String buildSecurityErrorXMLFile(String aFunctionalArea)
   
   {
      
      
      
      String strTempErrorXML  = "<?xml version=\"1.0\" encoding=\"utf-8\"?><securityerror>"
      
      + "<errorstring>No Permission to " + aFunctionalArea + "</errorstring>"
      
      + "<errortext>The person " + staticData.getPerson().getFullName()
      
      + " is not authorised to " + aFunctionalArea + "</errortext>"
      
      + "<errordata></errordata>"
      
      + "</securityerror>";
      
      return strTempErrorXML;
      
      
      
   }
   
   private void buildStudyLockError()
   
   {
      
      my_base_channel.setXMLFile("<strLockError>Another user has locked this study. Please try again shortly</strLockError>");
      
   }
   
   private void buildStudySurveyLockError()
   
   {
      
      my_base_channel.setXMLFile("<strLockError>Another user is currently editing the surveys attached to this study. Please try again shortly</strLockError>");
      
   }
   
   
   
   private void buildStudyEthicsLockError()
   
   {
      
      my_base_channel.setXMLFile("<strLockError>Another user is currently editing the ethics attached to this study. Please try again shortly</strLockError>");
      
   }
   
   private void buildSurveyLockError()
   
   {
      
      my_base_channel.setXMLFile("<strLockError>Another user has locked this survey. Please try again shortly</strLockError>");
      
   }
   
   private void buildResultsLockError()
   
   {
      
      my_base_channel.setXMLFile("<strLockError>The survey for this patient is currently lock by another user. Please try again shortly</strLockError>");
      
   }
   
   private void unlockStudyRecord()
   {
      
      if (lrStudyLock != null)
         
         try
         {
            
            lrStudyLock.unlock();
            
         }
         
         catch (Exception e)
         {
            
            e.printStackTrace();
            
         }
      
      finally
      {
         
         lrStudyLock = null;
         
      }
      
   }
   
   private void unlockResultsRecord()
   {
      
      if ( lrResultsLock != null)
         
         try
         {
            
            lrResultsLock.unlock();
            
         }
         
         catch (Exception e)
         {
            
            e.printStackTrace();
            
         }
      
      finally
      {
         
         lrResultsLock = null;
         
      }
      
   }
   
   private void unlockStudyEthicsRecord()
   {
      
      if (lrStudyEthicsLock != null)
         
         try
         {
            
            lrStudyEthicsLock.unlock();
            
            
         }
         
         catch (Exception e)
         {
            
            e.printStackTrace();
            
         }
      
      finally
      {
         
         lrStudyEthicsLock = null;
         
      }
      
   }
   
   private void unlockSurveyRecord()
   {
      
      if (lrSurveyLock != null)
         
         try
         {
            
            lrSurveyLock.unlock();
            
         }
         
         catch (Exception e)
         {
            
            e.printStackTrace();
            
         }
      
      finally
      {
         
         lrSurveyLock = null;
         
      }
      
   }
   
   private String buildStudyViewScreen(String strAStudyID)
   
   {
      
      StringBuffer strViewXML = new StringBuffer();
      
      // get the name of the primary study
      String strDefaultStudy = null;
      try
      {
         strDefaultStudy = PropertiesManager.getProperty("neuragenix.bio.DefaultStudy");
      }
      catch (Exception e)
      {
         System.err.println("[CStudy] - There was an error reading the properties for neuragenix.bio.DefaultStudy");
         e.printStackTrace(System.err);
      }
      
      if (strDefaultStudy != null)
      {
         if (getStudyName(Integer.parseInt(strAStudyID)).trim().equalsIgnoreCase(strDefaultStudy))
         {
            strViewXML.append("<readonly>true</readonly>");
            strViewXML.append("<strReadonlyMessage>" + PRIMARY_STUDY_MESSAGE + "</strReadonlyMessage>");
         }
      }
      
      
      try
      
      {
         
         Vector vtStudyFormFields = DatabaseSchema.getFormFields("cstudy_study_view_study");
         strStylesheet = STUDY_VIEW;
         
         DALQuery query = new DALQuery();
         ResultSet rsStudyView;
         
         
         query.setDomain("STUDY", null, null, null);
         query.setFields(vtStudyFormFields, null);
         query.setWhere(null, 0, "STUDY_intStudyID", "=", strAStudyID, 0, query.WHERE_HAS_VALUE);
         
         rsStudyView = query.executeSelect();
         
         if (rsStudyView != null)
         {
            if (rsStudyView.first())
            {
               strViewXML.append(QueryChannel.buildViewXMLForCurrentRecord(rsStudyView, vtStudyFormFields));
            }
            else
            {
               rsStudyView.close();
               return "<strError>Database Error : Unable to build screen</strError>";
            }
         }
         
         rsStudyView.close();
         
         query.reset();
         
         query.setDomain("PATIENT", null, null, null);
         query.setDomain("CONSENT", "CONSENT_intInternalPatientID", "PATIENT_intInternalPatientID", "INNER JOIN");
         query.setDomain("CONSENTSTUDY", "CONSENTSTUDY_intConsentKey", "CONSENT_intConsentKey", "INNER JOIN");
         query.setCountField("PATIENT_intInternalPatientID", false);
         query.setWhere(null, 0, "CONSENTSTUDY_intStudyID", "=", strAStudyID, 0, query.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "CONSENTSTUDY_intDeleted", "=", "0", 0, query.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "CONSENT_intDeleted", "=", "0", 0, query.WHERE_HAS_VALUE);
         query.setWhere("AND", 0, "PATIENT_intDeleted", "=", "0", 0, query.WHERE_HAS_VALUE);
         
         rsStudyView = query.executeSelect();
         
         if (rsStudyView.first())
         {
            strViewXML.append("<intActualPatientNo>");
            strViewXML.append(rsStudyView.getInt(1));
            strViewXML.append("</intActualPatientNo>");
         }
         rsStudyView.close();
         strViewXML.append(QueryChannel.buildFormLabelXMLFile(vtStudyFormFields));
         return strViewXML.toString();
      }
      
      catch(Exception e)
      
      {
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
         return "";
         
      }
      
   }
   
   private void buildStudySearchScreen()
   
   {
      
      try
      
      {
         
         strStylesheet = STUDY_SEARCH;
         
         my_base_channel.setNewXMLFile();
         
         hashStudyFormFields = StudyFormFields.getStudySearchFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.buildDefaultXMLFile();
         
      }
      
      catch(Exception e)
      
      {
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void checkStudySubmittedData()
   
   {
      
      try
      
      {
         
         hashStudyFormFields = StudyFormFields.getStudyAddFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.setNewXMLFile();
         
         my_base_channel.setRunTimeData(runtimeData);
         
         my_base_channel.buildAddModifyXMLFile(); // The default xml basically builds the display names for the page
         
         
         blStudyDateCheck = QueryChannel.validateDates("dtStudyStart","dtStudyEnd",runtimeData);
         if(blStudyDateCheck != null && blStudyDateCheck.equals("false"))
         {
            my_base_channel.setXMLFile("<strError>The start date has to be less than the end date</strError>");
         }
         
      }
      
      catch(Exception e)
      
      {
         
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void buildStudyAddScreen()
   
   {
      
      try
      
      {
         //LogService.instance().log(LogService.ERROR, "[CStudy::buildStudyAddScreen] Clear Screen Problem");
         
         strStylesheet = STUDY_ADD;
         
         my_base_channel.setNewXMLFile();
         
         hashStudyFormFields = StudyFormFields.getStudyAddFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.buildDefaultXMLFile();
         
      }
      
      catch(Exception e)
      
      {
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   /**
    * Refresh the study result screen with the a newly sorted result
    * base on which column title the user click on
    */
   private void buildHyperlinkSortedStudyResultsScreen()
   
   {
      
      try
      
      {
         
         strStylesheet = STUDY_RESULTS;
         
         my_base_channel.setRunTimeData(studySearchRuntimeData);
         
         hashStudyFormFields = StudyFormFields.getStudyResultsFormFields(); // USed for display and getting values
         
         hashStudyFormSearchOperators = StudyFormFields.getStudySearchFormOperators();
         
         // Set the where conditions from the search form
         
         my_base_channel.setSearchFieldOperators(hashStudyFormSearchOperators);
         
         my_base_channel.setSearchWhere();
         
         // set the order by in the sql statement
         my_base_channel.setOrderBy( strOrderBy, strSortByFromHyperlinks );
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         // Build the results XML file
         
         my_base_channel.buildSearchResultsXMLFile();
         
      }
      
      catch(Exception e)
      
      {
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   
   private void buildStudyResultsScreen()
   
   {
      
      try
      
      {
         
         strStylesheet = STUDY_RESULTS;
         
         my_base_channel.setRunTimeData(runtimeData);
         
         hashStudyFormFields = StudyFormFields.getStudyResultsFormFields(); // USed for display and getting values
         
         hashStudyFormSearchOperators = StudyFormFields.getStudySearchFormOperators();
         
         // Set the where conditions from the search form
         
         my_base_channel.setSearchFieldOperators(hashStudyFormSearchOperators);
         
         my_base_channel.setSearchWhere();
         
         // set the order by in the sql statement
         my_base_channel.setOrderBy( strStudyOrderBy, strStudySortByFromSearch );
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         // Build the results XML file
         
         my_base_channel.buildSearchResultsXMLFile();
         
      }
      
      catch(Exception e)
      
      {
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void addStudyRecord()
   {
      try
      {
         strStylesheet = STUDY_VIEW;
         my_base_channel.setNewXMLFile();
         
         my_base_channel.setRunTimeData(runtimeData);
         
         // hashStudyFormFields = StudyFormFields.getStudyAddFormFields(); // USed for display and getting values
         
         hashStudyFormFields.clear();
         hashStudyFormFields.put("strStudyName", "Study name");
         hashStudyFormFields.put("strStudyOwner", "Study owner");
         hashStudyFormFields.put("strStudyCode", "Study code");
         hashStudyFormFields.put("strStudyDesc" , "Study description");
         hashStudyFormFields.put("dtStudyStart", "Start date (dd/mm/yyyy)");
         hashStudyFormFields.put("dtStudyEnd", "End date (dd/mm/yyyy)");
         hashStudyFormFields.put("intTargetPatientNo", "Target patient no.");
         
         my_base_channel.setFormFields(hashStudyFormFields);
         my_base_channel.addRecord();
         
      }
      catch(Exception e)
      {
         System.out.println(e.toString());
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
      }
   }
   
   private void checkStudyUpdateData()
   
   {
      
      try
      
      {
         
         hashStudyFormFields = StudyFormFields.getStudyViewFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         // Get the data from the page and create the xml document to return the data if required
         
         my_base_channel.setNewXMLFile();
         
         my_base_channel.setRunTimeData(runtimeData);
         
         my_base_channel.buildAddModifyXMLFile();
         
         
         blStudyDateCheck = QueryChannel.validateDates("dtStudyStart","dtStudyEnd",runtimeData);
         if(blStudyDateCheck != null && blStudyDateCheck.equals("false"))
         {
            my_base_channel.setXMLFile("<strError>The start date has to be less than the end date</strError>");
         }
         
      }
      
      catch(Exception e)
      
      {
         //System.out.println(e.toString());
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void buildStudySurveyScreen(String strAStudyID)
   
   {
      
      try
      
      {
         
         strStylesheet = STUDY_SURVEYS;
         
         my_base_channel.clearDomains();
         
         
         my_base_channel.setDomain(StudyFormFields.STUDY_DOMAIN);
         
         hashStudyFormFields = StudyFormFields.getStudySurveyStudyFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_STUDY_ID, DBSchema.EQUALS_OPERATOR, strAStudyID);
         
         my_base_channel.buildViewXMLFile();
         
         my_base_channel.setDomain(StudyFormFields.STUDY_SURVEY_DOMAIN);
         
         my_base_channel.setDomain(StudyFormFields.SMARTFORM_DOMAIN);
         
         hashStudyFormFields = StudyFormFields.getStudySmartformFormFields(); // USed for display and getting values
         
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.buildSearchResultsXMLFile();
         
         
         
         
         
         DALQuery dalQuery = new DALQuery();
         DALQuery dalNotIn = new DALQuery();
         
         Vector vtFields = new Vector(3);
         vtFields.add("SMARTFORM_intSmartformID");
         vtFields.add("SMARTFORM_strSmartformName");
         
         dalNotIn.setDomain("SMARTFORM", null, null, null);
         dalNotIn.setDomain("STUDYSURVEY", "SMARTFORM_intSmartformID", "STUDYSURVEY_intSurveyKey", "INNER JOIN");
         dalNotIn.setField("SMARTFORM_intSmartformID", null);
         dalNotIn.setWhere(null, 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         dalNotIn.setWhere("AND", 0, "STUDYSURVEY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         dalNotIn.setWhere("AND", 0, "STUDYSURVEY_intStudyID", "=", strAStudyID, 0, DALQuery.WHERE_HAS_VALUE);
         
         
         dalQuery.setCaseSensitive(false);
         dalQuery.setDomain("SMARTFORM", null, null, null);
         dalQuery.setFields(vtFields, null);
         dalQuery.setWhere(null, 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
         dalQuery.setWhere("AND", 0, "SMARTFORM_strDomain", "=", "Study", 0, DALQuery.WHERE_HAS_VALUE);
         dalQuery.setWhere("AND", 0, "SMARTFORM_intSmartformID", "NOT IN", dalNotIn, 0, DALQuery.WHERE_HAS_SUB_QUERY);
         
         ResultSet rs = dalQuery.executeSelect();
         
         my_base_channel.setXMLFile(QueryChannel.buildSearchXMLFile("LOV_SMARTFORM", rs, vtFields));
         
         rs.close();
         
      }
      
      catch(Exception e)
      
      {
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void addStudySurveyLocks(String strAStudyID)
   
   {
      
      try
      
      {
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.STUDY_DOMAIN);
         
         my_base_channel.setDomain(StudyFormFields.STUDY_SURVEY_DOMAIN);
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_DOMAIN);
         
         hashStudyFormFields = StudyFormFields.getStudySurveysFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_STUDY_ID, DBSchema.EQUALS_OPERATOR, strAStudyID);
         
         Vector vecStudySurveyLock = my_base_channel.lookupRecord();
         
         lrSurveyLock= new LockRequest(authToken);
         
         Hashtable hashTempLock = new Hashtable();
         
         Hashtable hashTempLock1 = new Hashtable();
         
         for(int i = 0; i < vecStudySurveyLock.size(); i++)
            
         {
            
            hashTempLock = (Hashtable)vecStudySurveyLock.get(i);
            
            if(!hashTempLock1.containsKey(hashTempLock.get(StudyFormFields.INTERNAL_SURVEY_ID).toString()))
            {
               
               lrSurveyLock.addLock(StudyFormFields.SURVEY_DOMAIN, hashTempLock.get(StudyFormFields.INTERNAL_SURVEY_ID).toString() , LockRecord.READ_WRITE);
               
               hashTempLock1.put(hashTempLock.get(StudyFormFields.INTERNAL_SURVEY_ID).toString(),"1");
            }
            
         }
         
         
         
      }
      
      catch(Exception e)
      
      {
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void buildStudyEthicsScreen(String strAStudyID)
   
   {
      
      try
      
      {
         
         strStylesheet = STUDY_ETHICS_VIEW;
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.STUDY_DOMAIN);
         
         // Get the study details first
         
         hashStudyFormFields = StudyFormFields.getStudyViewFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_STUDY_ID, DBSchema.EQUALS_OPERATOR, strAStudyID);
         
         my_base_channel.buildViewXMLFile();
         
         // Now get the ethics attached to the study
         
         my_base_channel.setDomain(StudyFormFields.ETHICS_CONSENT_DOMAIN);
         
         hashStudyFormFields = StudyFormFields.getViewStudyEthicsFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.buildSearchResultsXMLFile();
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void addStudyEthicsLocks(String strAStudyID)
   
   {
      
      try
      
      {
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.STUDY_DOMAIN);
         
         my_base_channel.setDomain(StudyFormFields.ETHICS_CONSENT_DOMAIN);
         
         hashStudyFormFields = StudyFormFields.getViewStudyEthicsFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_STUDY_ID, DBSchema.EQUALS_OPERATOR, strAStudyID);
         
         Vector vecStudyEthicsLock = my_base_channel.lookupRecord();
         
         lrStudyEthicsLock= new LockRequest(authToken);
         
         Hashtable hashTempLock = new Hashtable();
         
         for(int i = 0; i < vecStudyEthicsLock.size(); i++)
            
         {
            
            hashTempLock = (Hashtable)vecStudyEthicsLock.get(i);
            // DEBUG PRINT STATEMENTS ... REMOVE
            //System.out.println("lrStudyEthicsLock:" + lrStudyEthicsLock);
            //System.out.println("hashTempLock:" + hashTempLock);
            //System.out.println("lrStudyEthicsLock:" + hashTempLock.get("strEthicsApprovedBy"));
            
            // try and add a lock if StudyFormFields.INTERNAL_ETHICS_CONSENT_ID is not null
            if (hashTempLock.get(StudyFormFields.INTERNAL_ETHICS_CONSENT_ID) != null)
            {
                lrStudyEthicsLock.addLock(StudyFormFields.ETHICS_CONSENT_DOMAIN, hashTempLock.get(StudyFormFields.INTERNAL_ETHICS_CONSENT_ID).toString() , LockRecord.READ_WRITE);
            }
            
            // Error -> StudyFormFields.INTERNAL_ETHICS_CONSENT_ID is null - log error
            else
            {
                LogService.instance().log(LogService.ERROR, "[CStudy::addStudyEthicsLocks] For Study " + strAStudyID + " ethics approved by " + hashTempLock.get("strEthicsApprovedBy") + " has  no  StudyFormFields.INTERNAL_ETHICS_CONSENT_ID ");
            }
            
         }
         
         
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void deleteStudy(String strAStudyID)
   
   {
      
      try
      
      {
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.STUDY_DOMAIN);
         
         my_base_channel.setRunTimeData(runtimeData);
         
         my_base_channel.setFormFields(new Hashtable());
         
         my_base_channel.setNewXMLFile();
         
         my_base_channel.deleteRecord();
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_STUDY_ID, DBSchema.EQUALS_OPERATOR, strAStudyID);
         
         my_base_channel.updateRecord();
         
         // Delete the survey study table
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.STUDY_SURVEY_DOMAIN);
         
         my_base_channel.deleteRecord();
         
         my_base_channel.updateRecord();
         
         // Delete the survey results table
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_RESULTS_DOMAIN);
         
         my_base_channel.deleteRecord();
         
         my_base_channel.updateRecord();
         
         // Delete the consent study table
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.CONSENT_STUDY);
         
         my_base_channel.deleteRecord();
         
         my_base_channel.updateRecord();
         
         // Delete the templates
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.TEMPLATE_DOMAIN);
         
         my_base_channel.deleteRecord();
         
         my_base_channel.updateRecord();
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void buildClearStudyScreen(String strAStudyID)
   {
      try
      {
         strStylesheet = STUDY_VIEW;
         
         // Get the number of patients consented to the study
         hashStudyFormFields = StudyFormFields.getStudyPatientCountFormFields();
         
         my_base_channel.setFormFields(hashStudyFormFields);
         my_base_channel.clearDomains();
         my_base_channel.setDomain(StudyFormFields.PATIENT_DOMAIN);
         my_base_channel.setDomain(StudyFormFields.CONSENT_CONTACT);
         my_base_channel.setDomain(StudyFormFields.CONSENT_STUDY);
         my_base_channel.setDomain(StudyFormFields.STUDY_DOMAIN);
         my_base_channel.clearWhere();
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_STUDY_ID, DBSchema.EQUALS_OPERATOR, strAStudyID);
         my_base_channel.resetRecordCount();
         my_base_channel.buildSearchResultsXMLFile();
         my_base_channel.setNewXMLFile();
         my_base_channel.setXMLFile("<intActualPatientNo>" + my_base_channel.getRecordCount() + "</intActualPatientNo>");
         
         // Just build the display names
         hashStudyFormFields = StudyFormFields.getStudyViewFormFields();
         my_base_channel.setFormFields(hashStudyFormFields);
         my_base_channel.buildDefaultXMLFile();
         my_base_channel.setXMLFile("<" + StudyFormFields.INTERNAL_STUDY_ID + ">" + runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID) + "</" + StudyFormFields.INTERNAL_STUDY_ID + ">");
      }
      
      catch(Exception e)
      {
         e.printStackTrace(System.err);
         System.out.println(e.toString());
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
      }
   }
   
   private void deleteStudyEthicsRecord(String strAStudyEthicsID)
   
   {
      
      try
      
      {
         
         // Delete the ethics record
         
         my_base_channel.setRunTimeData(runtimeData);
         
         my_base_channel.setNewXMLFile();
         
         my_base_channel.clearDomains();
         
         my_base_channel.setFormFields(new Hashtable());
         
         my_base_channel.setDomain(StudyFormFields.ETHICS_CONSENT_DOMAIN);
         
         my_base_channel.deleteRecord();
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_ETHICS_CONSENT_ID, DBSchema.EQUALS_OPERATOR, strAStudyEthicsID);
         
         my_base_channel.updateRecord();
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void addSurveyToStudy()
   
   {
      
      try
      
      {
         
         // Add the survey
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.STUDY_SURVEY_DOMAIN);
         
         
         
         String key = runtimeData.getParameter("intSmartformKey");
         
         runtimeData.setParameter("intSurveyID",key);
         
         
         my_base_channel.setRunTimeData(runtimeData);
         
/* replace with smartform rennypv           hashStudyFormFields = StudyFormFields.getStudySurveysFormFields(); // USed for display and getting values
 
            hashStudyFormFields.remove("strSurveyName");
 
            hashStudyFormFields.remove("intStudySurveyID");*/
         
         hashStudyFormFields = StudyFormFields.getStudySurveysFormFields();
         
         
         hashStudyFormFields.remove("strSurveyName");
         
         hashStudyFormFields.remove("intStudySurveyID");
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.addRecord();
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void deleteStudySurvey()
   
   {
      
      try
      
      {
         
         my_base_channel.setRunTimeData(runtimeData);
         
         my_base_channel.setFormFields(new Hashtable());
         
         // Go and delete the surveys from the patient survey table so people can't fill in surveys for this study
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_PATIENTS_DOMAIN);
         
         my_base_channel.deleteRecord();
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID));
         
         my_base_channel.setWhere(DBSchema.AND_CONNECTOR, StudyFormFields.INTERNAL_STUDY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
         
         my_base_channel.updateRecord();
         
         // Now delete the survey from the study to survey table
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.STUDY_SURVEY_DOMAIN);
         
         my_base_channel.deleteRecord();
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_STUDY_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_SURVEY_ID));
         
         my_base_channel.updateRecord();
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   /**
    * Refresh the survey result screen with the a newly sorted result
    * base on which column title the user click on
    */
   private void refreshSortedSurveyResultsFromSearchScreen()
   {
      try
      {
         strStylesheet = SURVEY_RESULTS;
         my_base_channel.setNewXMLFile();
         my_base_channel.clearDomains();
         my_base_channel.setDomain(StudyFormFields.SURVEY_DOMAIN);
         my_base_channel.setRunTimeData(surveySearchRuntimeData);
         
         // used for display and getting values
         hashStudyFormFields = StudyFormFields.getSurveyResultsFormFields();
         hashStudyFormSearchOperators = StudyFormFields.getSurveySearchFormOperators();
         
         // Set the where conditions from the search form
         my_base_channel.setSearchFieldOperators(hashStudyFormSearchOperators);
         my_base_channel.setSearchWhere();
         
         // set the order by in the sql statement
         my_base_channel.setOrderBy( strOrderBy, strSortByFromHyperlinks );
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         // Build the results XML file
         my_base_channel.buildSearchResultsXMLFile();
      }
      catch(Exception e)
      {
         e.printStackTrace(System.err);
         System.out.println(e.toString());
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
      }
   }
   
   private void buildSurveyResultsFromSearchScreen()
   
   {
      
      try
      
      {
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_DOMAIN);
         
         my_base_channel.setRunTimeData(runtimeData);
         
         hashStudyFormFields = StudyFormFields.getSurveyResultsFormFields(); // USed for display and getting values
         
         hashStudyFormSearchOperators = StudyFormFields.getSurveySearchFormOperators();
         
         // Set the where conditions from the search form
         
         my_base_channel.setSearchFieldOperators(hashStudyFormSearchOperators);
         
         my_base_channel.setSearchWhere();
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         // Build the results XML file
         
         my_base_channel.buildSearchResultsXMLFile();
         
         strStylesheet = SURVEY_RESULTS;
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void buildSurveySearchScreen()
   
   {
      
      try
      
      {
         
         strStylesheet = SURVEY_SEARCH;
         
         hashStudyFormFields = StudyFormFields.getSurveySearchFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.buildDefaultXMLFile();
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void checkSurveyAddSubmittedData()
   
   {
      
      try
      
      {
         
         hashStudyFormFields = StudyFormFields.getSurveyAddFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.setNewXMLFile();
         
         my_base_channel.setRunTimeData(runtimeData);
         
         my_base_channel.buildAddModifyXMLFile();
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void addSurvey()
   
   {
      
      try
      
      
      {
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_DOMAIN);
         
         my_base_channel.setNewXMLFile();
         
         my_base_channel.setRunTimeData(runtimeData);
         
         hashStudyFormFields = StudyFormFields.getSurveyAddFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.addRecord();
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void buildSurveyViewScreen(String strASurveyID)
   
   {
      
      try
      
      {
         
         strStylesheet = SURVEY_VIEW;
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_DOMAIN);
         
         my_base_channel.setRunTimeData(runtimeData);
         
         hashStudyFormFields = StudyFormFields.getSurveyViewFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, strASurveyID);
         
         my_base_channel.buildViewXMLFile();
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void buildAddSurveyScreen()
   
   {
      
      try
      
      {
         
         strStylesheet = SURVEY_ADD;
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_DOMAIN);
         
         hashStudyFormFields = StudyFormFields.getSurveyAddFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.buildDefaultXMLFile();
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void checkSurveySubmittedData()
   
   {
      
      try
      
      {
         
         hashStudyFormFields = StudyFormFields.getSurveyViewFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_DOMAIN);
         
         my_base_channel.setRunTimeData(runtimeData);
         
         my_base_channel.buildAddModifyXMLFile();
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void deleteSurvey(String strASurveyID)
   
   {
      
      try
      
      {
         
         my_base_channel.setRunTimeData(runtimeData);
         
         my_base_channel.setFormFields(new Hashtable());
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_DOMAIN);
         
         my_base_channel.deleteRecord();
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, strASurveyID);
         
         my_base_channel.updateRecord();
         
         // delete the surveys attached to studies
         
         //       my_base_channel.clearDomains();
         
         //      my_base_channel.setDomain(StudyFormFields.STUDY_SURVEY_DOMAIN);
         
         //    my_base_channel.deleteRecord();
         
         //   my_base_channel.updateRecord();
         
         // delete the paitent survey table
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_PATIENTS_DOMAIN);
         
         my_base_channel.deleteRecord();
         
         my_base_channel.updateRecord();
         
         // delete the results table
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_RESULTS_DOMAIN);
         
         my_base_channel.deleteRecord();
         
         my_base_channel.updateRecord();
         
         // Delete the templates
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.TEMPLATE_DOMAIN);
         
         my_base_channel.deleteRecord();
         
         my_base_channel.updateRecord();
         
         
         
         // first get the questions attached to the options
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
         
         my_base_channel.setFormFields(StudyFormFields.getSurveyQuestionsFormFields());
         
         Vector vecMyResults = my_base_channel.lookupRecord();
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_OPTIONS_DOMAIN);
         
         Hashtable hashMyResults;
         
         my_base_channel.setFormFields(new Hashtable());
         
         // Delete all the options attached to those questions
         
         for(int i = 0; i < vecMyResults.size(); i++)
            
         {
            
            hashMyResults = (Hashtable)vecMyResults.get(i);
            
            my_base_channel.clearWhere();
            
            my_base_channel.setWhere("", StudyFormFields.INTERNAL_QUESTION_ID, DBSchema.EQUALS_OPERATOR, hashMyResults.get(StudyFormFields.INTERNAL_QUESTION_ID).toString());
            
            my_base_channel.deleteRecord();
            
            my_base_channel.updateRecord();
            
         }
         
         
         
         // delete the questions table
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_SURVEY_ID));
         
         my_base_channel.deleteRecord();
         
         my_base_channel.updateRecord();
         
         
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void buildDefaultQuestionScreen(String strASurveyID)
   
   {
      
      try
      
      {
         
         strStylesheet = SURVEY_QUESTIONS;
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_QUESTIONS_DOMAIN);
         
         
         
         // Give the survey name and id to the next page so we don't have to do another db lookup
         
         my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strSurveyName")) + "</strSurveyName>");
         
         my_base_channel.setXMLFile("<strSurveyNameURL>" + Utilities.base64Encode(runtimeData.getParameter("strSurveyName")) + "</strSurveyNameURL>");
         
         my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
         
         hashStudyFormFields = StudyFormFields.getSurveyQuestionsFormFields();
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.buildDefaultXMLFile();
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, strASurveyID);
         
         hashStudyCutSize = StudyFormFields.getStudySurveyFormFieldsCutSize();
         
         my_base_channel.sethashCutFields(hashStudyCutSize);
         
         my_base_channel.setOrderBy(StudyFormFields.QUESTION_ORDER, DBSchema.ORDERBY_ASENDING);
         
         my_base_channel.buildSearchResultsXMLFile();
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
      
      
   }
   
   private void buildFinalSurveyScreen()
   
   {
      
      try
      
      {
         
         strStylesheet = SURVEY_FINISHED;
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.STUDY_DOMAIN);
         
         hashStudyFormFields = StudyFormFields.getStudyViewFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_STUDY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
         
         my_base_channel.buildViewXMLFile();
         
         my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strSurveyName")) + "</strSurveyName>");
         
         my_base_channel.setXMLFile("<intInternalPatientID>" + runtimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
         
         my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
         
         my_base_channel.setXMLFile("<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>");
         
         if( runtimeData.getParameter("strOrigin") != null )
         {
            my_base_channel.setXMLFile("<strOrigin>" +
            runtimeData.getParameter("strOrigin") +
            "</strOrigin>" );
         }
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void buildFinalBioSurveyScreen()
   
   {
      
      try
      
      {
         
         strStylesheet = BIO_SURVEY_FINISHED;
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.STUDY_DOMAIN);
         
         hashStudyFormFields = StudyFormFields.getStudyViewFormFields(); // USed for display and getting values
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_STUDY_ID, DBSchema.EQUALS_OPERATOR, runtimeData.getParameter(StudyFormFields.INTERNAL_STUDY_ID));
         
         my_base_channel.buildViewXMLFile();
         
         my_base_channel.setXMLFile("<strSurveyName>" + Utilities.cleanForXSL(runtimeData.getParameter("strSurveyName")) + "</strSurveyName>");
         
         my_base_channel.setXMLFile("<intInternalPatientID>" + runtimeData.getParameter("intInternalPatientID") + "</intInternalPatientID>");
         
         my_base_channel.setXMLFile("<intBiospecimenID>" + runtimeData.getParameter("intBiospecimenID") + "</intBiospecimenID>");
         
         my_base_channel.setXMLFile("<intSurveyID>" + runtimeData.getParameter("intSurveyID") + "</intSurveyID>");
         
         my_base_channel.setXMLFile("<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>");
         
         my_base_channel.setXMLFile("<strSource>" + strSource + "</strSource>");
         
         if( runtimeData.getParameter("strOrigin") != null )
         {
            my_base_channel.setXMLFile("<strOrigin>" +
            runtimeData.getParameter("strOrigin") +
            "</strOrigin>" );
         }
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   private void deleteSurveyTemplate(String strASurveyID)
   
   {
      
      try
      
      {
         
         my_base_channel.setRunTimeData(runtimeData);
         
         my_base_channel.setFormFields(new Hashtable());
         
         my_base_channel.clearWhere();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_SURVEY_ID, DBSchema.EQUALS_OPERATOR, strASurveyID);
         
         // Delete the templates
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.TEMPLATE_DOMAIN);
         
         my_base_channel.deleteRecord();
         
         my_base_channel.updateRecord();
         
         my_base_channel.clearWhere();
         
         my_base_channel.clearDomains();
         
         
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in study Channel - " + e.toString());
         
      }
      
   }
   
   public void setDuplicateDropDown(String strQuestionIDToCopy, String strQuestionIDToCopyTo)
   
   {
      
      
      
      try
      
      {
         
         my_base_channel.clearDomains();
         
         my_base_channel.setDomain(StudyFormFields.SURVEY_OPTIONS_DOMAIN);
         
         hashStudyFormFields = StudyFormFields.getSurveyQuestionOptionsFormFields();
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         my_base_channel.setRunTimeData(runtimeData);
         
         my_base_channel.clearWhere();
         
         my_base_channel.clearOrderBy();
         
         my_base_channel.setWhere("", StudyFormFields.INTERNAL_QUESTION_ID, DBSchema.EQUALS_OPERATOR,  strQuestionIDToCopy);
         
         Vector vecOptions = my_base_channel.lookupRecord();
         
         Hashtable hashOptions = new Hashtable(vecOptions.size());
         
         // Create the add query
         
         my_base_channel.clearWhere();
         
         hashStudyFormFields.remove(StudyFormFields.INTERNAL_OPTION_ID);
         
         my_base_channel.setFormFields(hashStudyFormFields);
         
         ChannelRuntimeData runtimeTempData = new ChannelRuntimeData();
         
         runtimeTempData.setParameter(StudyFormFields.INTERNAL_QUESTION_ID, strQuestionIDToCopyTo);
         
         for(int i = 0; i < vecOptions.size(); i++)
            
         {
            
            
            
            hashOptions = (Hashtable)vecOptions.get(i);
            
            runtimeTempData.setParameter("strOptionLabel", hashOptions.get("strOptionLabel").toString());
            
            runtimeTempData.setParameter("strOptionValue", hashOptions.get("strOptionValue").toString());
            
            runtimeTempData.setParameter("intOptionOrder", hashOptions.get("intOptionOrder").toString());
            
            my_base_channel.setRunTimeData(runtimeTempData);
            
            my_base_channel.addRecord();
            
         }
         
         // Clean up
         
         StudyFormFields.unloadFormFields();
         
         my_base_channel.clearWhere();
         
         my_base_channel.setRunTimeData(runtimeData);
         
      }
      
      catch(Exception e)
      
      {
         
         e.printStackTrace(System.err);
         
         System.out.println(e.toString());
         
         LogService.instance().log(LogService.ERROR, "Unknown error in Study Channel - " + e.toString());
         
      }
      
   }
   
   // for testing purpose only
   public static void printRuntimeData(ChannelRuntimeData runtimeData)
   {
      System.out.println("\n----------------");
      System.out.println("Runtime Data : ");
      Enumeration e = runtimeData.keys();
      while (e.hasMoreElements())
      {
         String keyValue = (String) e.nextElement();
         System.out.println(keyValue + ":\t\t" + runtimeData.getParameter(keyValue));
      }
      System.out.println("----------------");
      
   }
   //Start Agus Changes
  /*
  public void releaseAllSurveyLocks()
  {
      // Unlock the results record
      unlockResultsRecord();
      unlockSurveyRecord();
  }
   */
   //End Agus Changes
   
}

