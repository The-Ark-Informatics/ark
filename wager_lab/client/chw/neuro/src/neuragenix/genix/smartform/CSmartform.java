/**
 * CSmartform.java
 * Copyright @ 2004 Neuragenix, Inc .  All rights reserved.
 * Date: 26/03/2004
 */

package neuragenix.genix.smartform;

/**
 * Smartform channel
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 */

// java packages
import java.util.ArrayList;
import java.util.List;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.*;
import java.sql.*;
import java.text.SimpleDateFormat;

// javax packages
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NotContextException;

// uPortal packages
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
import org.jasig.portal.PropertiesManager;

// neuragenix packages
import neuragenix.security.AuthToken;
import neuragenix.dao.*;
import neuragenix.bio.utilities.BiospecimenUtilities;
import neuragenix.common.*;




public class CSmartform implements IChannel {
    
    // Smartform status
    public static final String[] SMARTFORM_STATUS = {"Not started", "Incompleted", "Completed"};
    
    // pages & acticities
    private final String SMARTFORM_LIST = "smartform_list";
    private final String SMARTFORM_RESULT_VIEW = "smartform_result_view";
    //Study
    private final String SMARTFORM_LIST_STUDY = "smartform_list_study";
    
    // Bioanalysis
    private final String SMARTFORM_BIOANALYSIS_LIST = "smartform_list_study";
    
    
    private final String SMARTFORM_NO_DOMAIN = "smartform_no_domain";
    private final String SECURITY = "security";
    
    private final String SMARTFORM_ADD = "smartform_add";
    private final String SMARTFORM_VIEW = "smartform_view";
    private final String SMARTFORM_UPDATE = "smartform_update";
    private final String SMARTFORM_DELETE = "smartform_delete";
    private final String SMARTFORM_COPY = "smartform_copy";
    private final String SMARTFORM_COMPLETE = "smartform_complete";

    // Picker stylesheets
    private static final String DIAGNOSIS = "diagnosis";
    private static final String ICDCODES = "icdcodes";
    private static final String SDIAGNOSIS = "sdiagnosis";
    private static final String SYNDROMES = "syndromes";
    private static final String ECGCODES = "ecgcodes";        
    
    // channel's runtime & static data
    private ChannelStaticData staticData;
    private ChannelRuntimeData runtimeData;
    
    // Authorization token
    private AuthToken authToken ;
    
    // Session UniqueID
    private String strSessionUniqueID;
    
    // Current stylesheet
    private String strCurrent;
    
    // XML string
    private String strXML = "";
    
    // Previous XML string
    private String strPreviousXML = "";
    
    // Stylesheet used for transforming
    private String strStylesheet;
    
    // Previous stylesheet
    private String strPreviousStylesheet;
    
    // First time to channel
    private boolean firstTime = false;
    
    // Error message
    private String strErrorMessage = "";
    
    private String strErrorCompleted ="";
    
    // Lock request object to handle record locking
    private LockRequest lockRequest = null;
    
    // Total pages of the smart form
    private int intTotalPage = 0;
    
    // Current page
    private int intCurrentPage = 0;
    
    // Current domain
    private String strCurrentDomain = null;
    
    // Current participant key
    private String strCurrentParticipant = null;
    
    // Current title
    private String strTitle = null;
    
    //patient and biospecimen details
    private String var1 = null;
    
    private String var2 = null;
    
    private String var3 = null;
    
    private String var4 = null;
    
    // Back button link
    private String strBackButton = null;
    
    // to know if the previous page is workspace
    private boolean isFromWorkspace = false;
    
    // to keep the original page
    private String strOrigin = "";
    
    // to keep the source page
    private String strSource = "";
    
    // to keep current task key
    private String strTaskKey = "";
    
    // Type of smartform sensitivity
    private String HIGH = "High";
    private String LOW = "Low";
    
    // Based on this variable, value for each dataelement is taken
    // from hashSmartformResult or from runtimeData
    private String ValueFromRuntimeData = "false";
    
    private IPerson ip;
    
    
    
    /** Channels don't need to clear lock
     */
    private Hashtable hashChannelsNotToClearLock = null;
    
    // Location where the attachment will get saved
    private String attachmentLocation = PropertiesManager.getProperty("neuragenix.genix.smartform.AttachmentLocation");
    
    private ISmartformEntity SFEntityObject;
    
    private Hashtable htRepeatableDEResults = null;
    
    
    /** Contructs the Smartform Channel
     */
    public CSmartform() {
        this.strStylesheet = SMARTFORM_LIST;
    }
    
    /**
     *  Returns channel runtime properties.
     *  Satisfies implementation of Channel Interface.
     *
     *  @return handle to runtime properties
     */
    public ChannelRuntimeProperties getRuntimeProperties() {
        return new ChannelRuntimeProperties();
    }
    
    /**
     *  Process layout-level events coming from the portal.
     *  Satisfies implementation of IChannel Interface.
     *
     *  @param PortalEvent ev a portal layout event
     */
    public void receiveEvent(PortalEvent ev) {
        // If the user logout, destroy lock request object
        if (ev.getEventNumber() == PortalEvent.SESSION_DONE)
            clearLockRequest();
    }
    
    /**
     *  Receive static channel data from the portal.
     *  Satisfies implementation of IChannel Interface.
     *
     *  @param ChannelStaticData sd static channel data
     */
    public void setStaticData(ChannelStaticData sd) {
        firstTime = true;
        this.staticData = sd;
        this.ip = sd.getPerson();
        this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");
        
        // create domains & formfields for this channel
        InputStream file;
        
        // load system lookup objects
        file = CSmartform.class.getResourceAsStream("SmartformLookupObjects.xml");
        DatabaseSchema.loadLookupObjects(file, "SmartformLookupObjects.xml");
        
        // channel's ids
        Context globalIDContext = null;
        try {
            // Get the context that holds the global IDs for this user
            globalIDContext = (Context)staticData.getJNDIContext().lookup("/channel-ids");
        }
        catch (NotContextException nce) {
            LogService.log(LogService.ERROR, "Could not find subcontext /channel-ids in JNDI");
        }
        catch (NamingException e) {
            LogService.log(LogService.ERROR, e);
        }
        try {
            if(PropertiesManager.getProperty("solution.type").equals("Casegenix")) {
                try {
                    strSessionUniqueID = authToken.getSessionUniqueID();
                    SessionManager.addSession(strSessionUniqueID);
                    SessionManager.addChannelID(strSessionUniqueID, "CCase", (String) globalIDContext.lookup("CCase"));
                    
                }
                catch (NotContextException nce) {
                    LogService.log(LogService.ERROR, "Could not find channel ID for fname=CCase");
                }
                catch (NamingException e) {
                    LogService.log(LogService.ERROR, e);
                }
                
                try {
                    //strSessionUniqueID = authToken.getSessionUniqueID();
                    //SessionManager.addSession(strSessionUniqueID);
                    SessionManager.addChannelID(strSessionUniqueID, "CWorkspace", (String) globalIDContext.lookup("CWorkspace"));
                    
                }
                catch (NotContextException nce) {
                    LogService.log(LogService.ERROR, "Could not find channel ID for fname=CWorkspace");
                }
                catch (NamingException e) {
                    LogService.log(LogService.ERROR, e);
                }
            }
            else if(PropertiesManager.getProperty("solution.type").equals("Biogenix")) {
                try {
                    strSessionUniqueID = authToken.getSessionUniqueID();
                    SessionManager.addSession(strSessionUniqueID);
                    SessionManager.addChannelID(strSessionUniqueID, "CPatient", (String) globalIDContext.lookup("CPatient"));
                    
                }
                catch (NotContextException nce) {
                    LogService.log(LogService.ERROR, "Could not find channel ID for fname=Patient");
                }
                catch (NamingException e) {
                    LogService.log(LogService.ERROR, e);
                }
                try {
                    strSessionUniqueID = authToken.getSessionUniqueID();
                    SessionManager.addSession(strSessionUniqueID);
                    SessionManager.addChannelID(strSessionUniqueID, "CBiospecimen", (String) globalIDContext.lookup("CBiospecimen"));
                    
                }
                catch (NotContextException nce) {
                    LogService.log(LogService.ERROR, "Could not find channel ID for fname=Biospecimen");
                }
                catch (NamingException e) {
                    LogService.log(LogService.ERROR, e);
                }
                
            }
            
            try{
                
                String strSessionUniqueID = authToken.getSessionUniqueID();
                SessionManager.addSession(strSessionUniqueID);
                
                SessionManager.addChannelID(strSessionUniqueID, "CDownload",
                (String) globalIDContext.lookup("CDownload"));
                
            }catch (NotContextException nce) {
                LogService.log(LogService.ERROR, "Could not find channel ID for fname = CDownload");
            }catch (javax.naming.NameNotFoundException e ){
                LogService.log(LogService.ERROR, e);
            }catch( Exception ae ){
                LogService.log(LogService.ERROR, ae);
            }
            
            SessionManager.addLockRequestSession(strSessionUniqueID);
            
            // instantiate the hashChannelNotToClearLock
            hashChannelsNotToClearLock = SessionManager.getSharedChannels(authToken, "CSmartform");
        }
        catch(Exception e) {
            LogService.log(LogService.ERROR, "Could not find the property solutions.type in portal.properties");
        }
    }
    
    /**
     *  Receive channel runtime data from the portal.
     *  Satisfies implementation of IChannel Interface.
     *
     *  @param ChannelRuntimeData rd handle to channel runtime data
     */
    public void setRuntimeData(ChannelRuntimeData rd) {
        
        try {
            SessionManager.clearLockRequests(strSessionUniqueID, hashChannelsNotToClearLock);
            this.runtimeData = rd;
            strErrorMessage = "";
            strErrorCompleted = "";
            
            // Get the current page the users is on
            if(runtimeData.getParameter("current") != null) {
                
                firstTime = false;
                strCurrent = runtimeData.getParameter("current");
                //runtimeData.setParameter("domain", strCurrentDomain);
                //runtimeData.setParameter("participant", strCurrentParticipant);
            }
            else if (firstTime) {
                //SessionManager.clearLockRequests(strSessionUniqueID, hashChannelsNotToClearLock);
                //LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
                firstTime = false;
                strCurrent = SMARTFORM_LIST;
                
                strBackButton = runtimeData.getParameter("backbutton");
                
                // check if the previous page is from workspace channel
                if (runtimeData.getParameter("isFromWorkspace") != null) {
                    isFromWorkspace = true;
                    strOrigin = runtimeData.getParameter("origin");
                    strSource = runtimeData.getParameter("source");
                    strTaskKey = runtimeData.getParameter("WORKFLOW_TASK_intTaskKey");
                }
                else
                    isFromWorkspace = false;
                
            }
            else {
                //SessionManager.clearLockRequests(strSessionUniqueID, hashChannelsNotToClearLock);
                //LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
                strCurrent = SMARTFORM_LIST;
                
                strBackButton = runtimeData.getParameter("backbutton");
                
                // check if the previous page is from workspace channel
                if (runtimeData.getParameter("isFromWorkspace") != null) {
                    isFromWorkspace = true;
                    strOrigin = runtimeData.getParameter("origin");
                    strSource = runtimeData.getParameter("source");
                    strTaskKey = runtimeData.getParameter("WORKFLOW_TASK_intTaskKey");
                }
                else
                    isFromWorkspace = false;
            }
            if( runtimeData.getParameter("title") != null )
                strTitle = runtimeData.getParameter("title");
            
            // If we are performing a batch SF results generation
            // create a new instance of the SFEntity Object
            // on first time viewing the smartform results page
            if( runtimeData.getParameter("SFBatchGenerationFirstPage") != null )
            {    
                SFEntityObject = (ISmartformEntity) SmartformFactory.getSmartformEntityInstance ();
            }    
            
            if (strCurrent == null) {
                strXML = strPreviousXML;
                strStylesheet = strPreviousStylesheet;
            }
            else {
                runtimeData.setParameter("ValueFromRuntimeData", "false");
                // Update all repeatable DE data from the runtime data
                if (htRepeatableDEResults != null)
                {    
                    updateDataInRepeatableDE();
                }

                
                // current page is smartform_list
                if (strCurrent.equals(SMARTFORM_LIST)) {
                    // user click on Add button to add a smartform
                    if (runtimeData.getParameter("add") != null) {
                        // if the user doesn't have permission to add
                        // display unauthorization message
                        if (!checkPermission(runtimeData.getParameter("domain"), runtimeData.getParameter("participant"), authToken, runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), "add"))
                            buildSecurityErrorXMLFile("add smart form");
                        else
                            doAddSmartformParticipant();
                    }
                    // user click on Save to update the user note
                    else if (runtimeData.getParameter("save") != null) {
                        // if the user doesn't have permission to update
                        // display unauthorization message
                        if (!checkPermission(runtimeData.getParameter("domain"), runtimeData.getParameter("participant"), authToken, runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), "edit"))
                            buildSecurityErrorXMLFile("update smart form");
                        else
                            doUpdateSmartformParticipantUserNote();
                    }
                    // user click on Copy to copy the smartform
                    else if (runtimeData.getParameter("clone") != null) {
                        // if the user doesn't have permission to copy
                        // display unauthorization message
                        if (!checkPermission(runtimeData.getParameter("domain"), runtimeData.getParameter("participant"), authToken, runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), "copy"))
                            buildSecurityErrorXMLFile("copy smart form");
                        else
                            doCopySmartformParticipant();
                    }
                    // user click on Del to delete the smartform
                    else if (runtimeData.getParameter("delete") != null) {
                        // if the user doesn't have permission to delete
                        // display unauthorization message
                        if (!checkPermission(runtimeData.getParameter("domain"), runtimeData.getParameter("participant"), authToken, runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), "delete"))
                            buildSecurityErrorXMLFile("delete smart form");
                        else
                            doDeleteSmartformParticipant();
                    }
                    else {
                        // if the user doesn't have permission to view
                        // display unauthorization message
                        if (!checkPermission(runtimeData.getParameter("domain"), runtimeData.getParameter("participant"), authToken, runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), "view"))
                            buildSecurityErrorXMLFile("view smart form");
                        else
                            buildViewDomainSmartformList();
                    }
                }
                // current page is smart form result
                else if (strCurrent.equals(SMARTFORM_RESULT_VIEW)) {
                    
                    // go to the previous page
                    if (runtimeData.getParameter("previous") != null) {
                        // if the user doesn't have permission to update
                        // display unauthorization message
                        if (!checkPermission(runtimeData.getParameter("domain"), runtimeData.getParameter("participant"), authToken, runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), "edit"))
                            buildViewSmartFormResultCurrentPage();
                        else {
                            strErrorMessage = validateData();
                            if (strErrorMessage.equals("")) {
                                ValueFromRuntimeData = "false";
                                runtimeData.setParameter("ValueFromRuntimeData", "false");
                                
                                if (lockRequest.isValid() && lockRequest.lockWrites()) {
                                    if(saveSmartformResult() == true){
                                        lockRequest.unlockWrites();
                                        
                                        buildViewSmartFormResultPreviousPage();
                                    }else{
                                        strErrorMessage = "Please complete the required fields";
                                        ValueFromRuntimeData = "true";
                                        runtimeData.setParameter("ValueFromRuntimeData", "true");
                                        
                                        buildViewSmartFormResultCurrentPage();
                                    }
                                    
                                }
                                else {
                                    strErrorMessage = "Update failed since this record is being viewed by other users. Please try again later.";
                                    buildViewSmartFormResultCurrentPage();
                                }
                            }
                            else {
                                ValueFromRuntimeData = "true";
                                runtimeData.setParameter("ValueFromRuntimeData", "true");
                                
                                buildViewSmartFormResultCurrentPage();
                            }
                        }
                    }
                    // save the current page
                    else if (runtimeData.getParameter("save") != null) {
                        
                        // if the user doesn't have permission to update
                        // display unauthorization message
                        if (!checkPermission(runtimeData.getParameter("domain"), runtimeData.getParameter("participant"), authToken, runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), "edit"))
                            buildSecurityErrorXMLFile("update smart form");
                        else if((runtimeData.getParameter("domain").equals("CASE")) &&
                        (runtimeData.getParameter("SMARTFORMPARTICIPANTS_strSmartformStatus").equals("Completed")) &&
                        (!checkPermission(runtimeData.getParameter("domain"), runtimeData.getParameter("participant"), authToken, runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), "case_smartform_completion_superuser"))) {
                            strStylesheet = SMARTFORM_RESULT_VIEW;
                            strErrorCompleted  = "Form cannot be updated as status is Completed";
                            buildViewSmartFormResultCurrentPage();
                        }
                        else {
                            
                            strErrorMessage = validateData();
                            if (strErrorMessage.equals("")) {
                                ValueFromRuntimeData = "false";
                                runtimeData.setParameter("ValueFromRuntimeData", "false");
                                
                                if (lockRequest.isValid() && lockRequest.lockWrites()) {
                                    if(saveSmartformResult() == true){
                                        ;
                                        lockRequest.unlockWrites();
                                        
                                        buildViewSmartFormResultCurrentPage();
                                        
                                    }else{
                                        
                                        strErrorMessage = "Please complete the required fields";
                                        ValueFromRuntimeData = "true";
                                        runtimeData.setParameter("ValueFromRuntimeData", "true");
                                        
                                        buildViewSmartFormResultCurrentPage();
                                    }
                                    //System.out.println("ValueFromRuntimeData in runtimeData:"+runtimeData.getParameter("ValueFromRuntimeData"));
                                    
                                }
                                else {
                                    strErrorMessage = "Update failed since this record is being viewed by other users. Please try again later.";
                                    buildViewSmartFormResultCurrentPage();
                                }
                            }
                            else {
                                ValueFromRuntimeData = "true";
                                runtimeData.setParameter("ValueFromRuntimeData", "true");
                                
                                buildViewSmartFormResultCurrentPage();
                            }
                        }
                    }
                    // go to the next page
                    else if (runtimeData.getParameter("next") != null) {
                        // if the user doesn't have permission to update
                        // display unauthorization message
                        if (!checkPermission(runtimeData.getParameter("domain"), runtimeData.getParameter("participant"), authToken, runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), "edit"))
                            buildViewSmartFormResultCurrentPage();
                        else {
                            strErrorMessage = validateData();
                            if (strErrorMessage.equals("")) {
                                ValueFromRuntimeData = "false";
                                runtimeData.setParameter("ValueFromRuntimeData", "false");
                                
                                if (lockRequest.isValid() && lockRequest.lockWrites()) {
                                    if(saveSmartformResult() == true){
                                        lockRequest.unlockWrites();
                                        
                                        buildViewSmartFormResultNextPage();
                                    }else{
                                        strErrorMessage = "Please complete the required fields";
                                        ValueFromRuntimeData = "true";
                                        runtimeData.setParameter("ValueFromRuntimeData", "true");
                                        
                                        buildViewSmartFormResultCurrentPage();
                                    }
                                    
                                    
                                }
                                else {
                                    strErrorMessage = "Update failed since this record is being viewed by other users. Please try again later.";
                                    buildViewSmartFormResultCurrentPage();
                                }
                            }
                            else {
                                ValueFromRuntimeData = "true";
                                runtimeData.setParameter("ValueFromRuntimeData", "true");
                                
                                buildViewSmartFormResultCurrentPage();
                            }
                        }
                    }
                    // complete & submit the smart form
                    else if (runtimeData.getParameter("complete") != null) {
                        // if the user doesn't have permission to update
                        // display unauthorization message
                        if (!checkPermission(runtimeData.getParameter("domain"), runtimeData.getParameter("participant"), authToken, runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), "edit"))
                            buildSecurityErrorXMLFile("update smart form");
                        else if((runtimeData.getParameter("domain").equals("CASE")) &&
                        (runtimeData.getParameter("SMARTFORMPARTICIPANTS_strSmartformStatus").equals("Completed")) &&
                        (!checkPermission(runtimeData.getParameter("domain"), runtimeData.getParameter("participant"), authToken, runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), "case_smartform_completion_superuser"))) {
                            strStylesheet = SMARTFORM_RESULT_VIEW;
                            strErrorCompleted  = "Form cannot be updated as status is Completed";
                            buildViewSmartFormResultCurrentPage();
                        }
                        else {
                            strErrorMessage = validateData();
                            if (strErrorMessage.equals("")) {
                                ValueFromRuntimeData = "false";
                                runtimeData.setParameter("ValueFromRuntimeData", "false");
                                
                                if (lockRequest.isValid() && lockRequest.lockWrites()) {
                                    if(saveSmartformResult() == true){
                                        lockRequest.unlockWrites();
                                        
                                        buildViewSmartFormResultComplete();
                                    }else{
                                        strErrorMessage = "Please complete the required fields";
                                        ValueFromRuntimeData = "true";
                                        runtimeData.setParameter("ValueFromRuntimeData", "true");
                                        
                                        buildViewSmartFormResultCurrentPage();
                                    }
                                    
                                }
                                else {
                                    strErrorMessage = "Update failed since this record is being viewed by other users. Please try again later.";
                                    buildViewSmartFormResultCurrentPage();
                                }
                            }
                            else {
                                ValueFromRuntimeData = "true";
                                runtimeData.setParameter("ValueFromRuntimeData", "true");
                                
                                buildViewSmartFormResultCurrentPage();
                            }
                        }
                    }
                    // add data to a repeatable data element
                    else if (runtimeData.getParameter("AddRepeatableDE") != null && !runtimeData.getParameter("AddRepeatableDE").equals("")) {
                        // Add the new data
                        addDatatoRepeatableDE(runtimeData.getParameter("AddRepeatableDE"));
                        buildViewSmartFormResultCurrentPage();
                    }  
                    // add data to a repeatable data element
                    else if (runtimeData.getParameter("RemoveRepeatableDE") != null && !runtimeData.getParameter("RemoveRepeatableDE").equals("")) {
                        // remove the new data
                        if (runtimeData.getParameter("RemoveRepeatableDEIndex") != null)
                        {    
                            int index = Integer.parseInt(runtimeData.getParameter("RemoveRepeatableDEIndex")); 
                            removeDataFromRepeatableDE(runtimeData.getParameter("RemoveRepeatableDE"), index);
                        }
                        
                        buildViewSmartFormResultCurrentPage();
                    }
                    // back to smartform list
                    else if (runtimeData.getParameter("back") != null) {
                        buildViewDomainSmartformList();
                    }
                    // Delete the attachment
                    else if (runtimeData.getParameter("del") != null){
                        
                        deleteAttachment();
                        buildViewSmartFormResultCurrentPage();
                    }
                    else if (runtimeData.getParameter("formLink") != null) {
                        ValueFromRuntimeData = "true";
                        runtimeData.setParameter("ValueFromRuntimeData", "true");
                        buildViewSmartFormResultCurrentPage();
                    }
                    else {
                        
                        htRepeatableDEResults = null;
                        // if the user doesn't have permission to add
                        // display unauthorization message
                        if (!checkPermission(runtimeData.getParameter("domain"), runtimeData.getParameter("participant"), authToken, runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID"), "edit"))
                            buildSecurityErrorXMLFile("view smartform");
                        else
                            buildViewSmartformResultFirstTime();
                    }
                }
                else if(strCurrent.equals( DIAGNOSIS ))
                {
                    //implement the diagnosis picker
                    strXML = SmartformPickers.doDiagnosis(runtimeData);
                    this.strStylesheet = DIAGNOSIS;
                }else if(strCurrent.equals( ICDCODES ))
                {
                    //implement the ICD codes picker
                    strXML = SmartformPickers.doICDPicker(runtimeData);
                    this.strStylesheet = ICDCODES;
                }         
                else if(strCurrent.equals( SDIAGNOSIS ))
                {
                    //implement the additional diagnosis picker
                    strXML = SmartformPickers.doSDiagnosis(runtimeData);
                    this.strStylesheet = SDIAGNOSIS;
                }else if(strCurrent.equals( SYNDROMES ))
                {
                    //implement the syndormes picker
                    strXML = SmartformPickers.doSyndromes(runtimeData);
                    this.strStylesheet = SYNDROMES;
                }else if(strCurrent.equals( ECGCODES ))
                {
                    //implement the ECG codes picker
                    strXML = SmartformPickers.doECGPicker(runtimeData);
                    this.strStylesheet = ECGCODES;
                }                
                
                strPreviousXML = strXML;
                strPreviousStylesheet = strStylesheet;
            }
            strXML += "<strErrorMessage>" + strErrorMessage + "</strErrorMessage>";
            strXML += "<strErrorCompleted>" + strErrorCompleted + "</strErrorCompleted>";
            strXML += "<strTitle>" + Utilities.cleanForXSL(strTitle) + "</strTitle>";
            strXML += "<strBackButton>" + strBackButton + "</strBackButton>";
            
            if (runtimeData.getParameter("PATIENT_strSurname") != null)
            {    
                strXML += "<PATIENT_strSurname>" + runtimeData.getParameter("PATIENT_strSurname") + "</PATIENT_strSurname>";
            }
            
            if (runtimeData.getParameter("PATIENT_strFirstName") != null)
            {    
                strXML += "<PATIENT_strFirstName>" + runtimeData.getParameter("PATIENT_strFirstName") + "</PATIENT_strFirstName>";
            }
            
            strXML += "<isFromWorkspace>" + isFromWorkspace + "</isFromWorkspace>";
            strXML += "<strOrigin>" + strOrigin + "</strOrigin>";
            strXML += "<strSource>" + strSource + "</strSource>";
            if(runtimeData.getParameter("domain") != null && runtimeData.getParameter("domain").equals("Study")) {
                strXML += "<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>";
            }
            if(runtimeData.getParameter("domain") != null && runtimeData.getParameter("domain").equals("Biospecimen")) {
                strXML += "<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>";
            }
           
            strXML += "<WORKFLOW_TASK_intTaskKey>" + strTaskKey + "</WORKFLOW_TASK_intTaskKey>";
            strXML += "<SMARTFORMPARTICIPANTS_strSmartformStatus>" + runtimeData.getParameter("SMARTFORMPARTICIPANTS_strSmartformStatus") + "</SMARTFORMPARTICIPANTS_strSmartformStatus>";
            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><smartform>" + strXML + "</smartform>";
            //System.out.println(strXML);
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
    }
    
    /**
     * Build the view page for all smart forms related to a domain
     */
    private void buildViewDomainSmartformList() {
        if(runtimeData.getParameter("domain") != null && runtimeData.getParameter("domain").equals("Study")) {
            strStylesheet = SMARTFORM_LIST_STUDY;
            buildViewStudySmartformList();
        }
        else if(runtimeData.getParameter("domain") != null && runtimeData.getParameter("domain").equals("Biospecimen")) {
            strStylesheet = SMARTFORM_LIST_STUDY;
            buildViewBiospecimenSmartformList();
        }
        else if(runtimeData.getParameter("domain") != null &&
            ((runtimeData.getParameter("domain").equals("Bioanalysis")) || (runtimeData.getParameter("domain").equals("Admissions"))))
        {
            strStylesheet = SMARTFORM_LIST_STUDY;
            buildViewBioanalysisSmartformList();
        }
        else {
            strStylesheet = SMARTFORM_LIST;
            Vector vtSmartformParticipantView = DatabaseSchema.getFormFields("csmartform_smartform_participant_view");
            Vector vtSmartformView = DatabaseSchema.getFormFields("csmartform_smartform_view");
            
            try {
                // unlock all records
                clearLockRequest();
                
                // create new lock request
                lockRequest = new LockRequest(authToken);
                
                // register the lock to session manager
                SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartform", lockRequest);
                
                // check if the domain specified
                if (runtimeData.getParameter("domain") != null) {
                    // build the list of all smartform related to this domain and this participant
                    String strDomain = runtimeData.getParameter("domain");
                    String intParticipantID = runtimeData.getParameter("participant");
                    DALQuery query = new DALQuery();
                    query.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
                    query.setDomain("SMARTFORM", "SMARTFORMPARTICIPANTS_intSmartformID", "SMARTFORM_intSmartformID", "INNER JOIN");
                    query.setFields(vtSmartformParticipantView, null);
                    query.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", strDomain, 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intParticipantID", "=", intParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                    query.setOrderBy("SMARTFORMPARTICIPANTS_dtDateAdded", "DESC");
                    query.setOrderBy("SMARTFORMPARTICIPANTS_intSmartformParticipantID", "DESC");
                    ResultSet rsResult = query.executeSelect();
                    String strSearchSmartformParticipant = QueryChannel.buildSearchXMLFileAndLockRecord("search_smartform_participant", rsResult, vtSmartformParticipantView, "SMARTFORMPARTICIPANTS", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", lockRequest, LockRecord.READ_ONLY);
                    rsResult.close();
                    
                    // build smart form dropdown list
                    query.reset();
                    query.setDomain("SMARTFORM", null, null, null);
                    query.setFields(vtSmartformView, null);
                    query.setWhere(null, 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    query.setOrderBy("SMARTFORM_intSmartformOrder","ASC");
                    rsResult = query.executeSelect();
                    String strSearchSmartform = QueryChannel.buildSearchXMLFileAndLockRecord("search_smartform", rsResult, vtSmartformView, "SMARTFORM", "SMARTFORM_intSmartformID", lockRequest, LockRecord.READ_ONLY);
                    //String strSearchSmartform = QueryChannel.buildSearchXMLFile("search_smartform", rsResult, vtSmartformView);
                    rsResult.close();
                    
                    strXML = QueryChannel.buildColumnLabelXMLFile(vtSmartformParticipantView) +
                    strSearchSmartformParticipant + strSearchSmartform;
                    strXML += "<domain>" + strDomain + "</domain>";
                    strXML += "<participant>" + intParticipantID + "</participant>";
                    
                    if(runtimeData.getParameter("var1") != null) {
                    var1 = runtimeData.getParameter("var1");
                    var2 = runtimeData.getParameter("var2");                    
                    }
                    
                    var3 = runtimeData.getParameter("var3");
                    var4 = runtimeData.getParameter("var4");
                    
                    lockRequest.lockDelayWrite();
                }
                else {
                    buildNoDomainForm();
                }
                
            }
            catch(Exception e) {
                LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
            }
       // }
    }
    
    }
    // DM-30-09-04 : Biospecimen Analysis (BioAnalysis) domain handler
    
    private void buildViewBioanalysisSmartformList() {

        strStylesheet = SMARTFORM_BIOANALYSIS_LIST;
        Vector vtSmartformParticipantView = DatabaseSchema.getFormFields("csmartform_smartform_participant_bioanalysis_view");
        Vector vtSmartformView = DatabaseSchema.getFormFields("csmartform_smartform_view");

        try {
            // unlock all records
            clearLockRequest();

            // create new lock request
            lockRequest = new LockRequest(authToken);

            // register the lock to session manager
            SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartform", lockRequest);

            // check if the domain specified
            if (runtimeData.getParameter("domain") != null) {


                // build the list of all smartform related to this domain and this participant
                String strDomain = runtimeData.getParameter("domain");

                List listOfPartipants = new ArrayList();


                if ("patient_view".equalsIgnoreCase(runtimeData.getParameter("strcurrent"))){

                    strXML = "<patient_view>true</patient_view>";

                    //get list of biospecimen for this patient and build up a listOfParticipant for them
                    DALQuery query = new DALQuery();
                    query.setDomain("BIOSPECIMEN", null, null, null);
                    query.setField("BIOSPECIMEN_intBiospecimenID", null);
                    query.setWhere(null, 0, "BIOSPECIMEN_intPatientID", "=", runtimeData.getParameter("PATIENT_intPatientID"), 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rs = query.executeSelect();
                    while (rs.next()){
                            listOfPartipants.add(rs.getString("BIOSPECIMEN_intBiospecimenID"));
                    }
                }
                else {
                    strXML = "";
                    listOfPartipants.add(runtimeData.getParameter("participant"));
                }

                String strSearchSmartformParticipant = "";
                DALQuery query = new DALQuery();
                for (int counter =0; counter< listOfPartipants.size(); counter++){

                    if ("patient_view".equalsIgnoreCase(runtimeData.getParameter("strcurrent"))){
                        strXML += "<biospecimen id=\""+listOfPartipants.get(counter)+ "\">"
                               + BiospecimenUtilities.getUserBiospecimenID(Integer.parseInt((String)listOfPartipants.get(counter))) +
                               "</biospecimen>";
                    }	
                    
                    DALQuery query_smrtresults = new DALQuery();
                    query_smrtresults.setDomain("SMARTFORMRESULTS",null,null,null);
                    query_smrtresults.setField("SMARTFORMRESULTS_strDataElementResult",null);
                    query_smrtresults.setField("SMARTFORMRESULTS_intSmartformParticipantID",null);
                    query_smrtresults.setField("SMARTFORMRESULTS_intDataElementID",null);
                    query_smrtresults.setWhere(null, 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    
                    DALQuery query_smrtToDataE = new DALQuery();
                    query_smrtToDataE.setDomain("SMARTFORMTODATAELEMENTS",null,null,null);
                    query_smrtToDataE.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder",null);
                    query_smrtToDataE.setField("SMARTFORMTODATAELEMENTS_intDataElementNo",null);
                    query_smrtToDataE.setField("SMARTFORMTODATAELEMENTS_intDataElementID",null);
                    query_smrtToDataE.setField("SMARTFORMTODATAELEMENTS_intSmartformID",null);
                    query_smrtToDataE.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    
                    DALQuery query_DE =  new DALQuery();
                    query_DE.setDomain("DATAELEMENTS",null,null,null);
                    query_DE.setField("DATAELEMENTS_intDataElementOrder",null);
                    query_DE.setField("DATAELEMENTS_strDataElementName",null);
                    query_DE.setField("DATAELEMENTS_intDataElementID",null);
                    query_DE.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    
                    query.reset();
                    query.setDomain("SMARTFORM", null, null, null);
                    query.setDomain("SMARTFORMPARTICIPANTS", "SMARTFORMPARTICIPANTS_intSmartformID", "SMARTFORM_intSmartformID", "INNER JOIN");
                    query.setSubQuery(query_smrtresults,"ix_smartform_results", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "SMARTFORMRESULTS_intSmartformParticipantID", "LEFT JOIN");
                    query.setSubQuery(query_smrtToDataE,"ix_smartform_to_dataelements","SMARTFORMRESULTS_intDataElementID", "SMARTFORMTODATAELEMENTS_intDataElementID", "LEFT JOIN");
                    query.setSubQuery(query_DE,"ix_dataelements", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID", "LEFT JOIN");
                    query.setFields(vtSmartformParticipantView, null);
                    query.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);


                    
                    query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", strDomain, 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intParticipantID", "=", (String) listOfPartipants.get(counter), 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 1, "SMARTFORM_intSmartformID", "=", "SMARTFORMTODATAELEMENTS_intSmartformID", 0, DALQuery.WHERE_BOTH_FIELDS);
                    query.setWhere("OR", 0, "SMARTFORMPARTICIPANTS_strSmartformStatus", "=", "Not started", 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("OR", 1, "SMARTFORMTODATAELEMENTS_intDataElementOrder", "IS NULL", "IS NULL", 0, DALQuery.WHERE_HAS_NULL_VALUE);
                    query.setWhere("AND", 0, "DATAELEMENTS_intDataElementOrder", "IS NULL", "IS NULL", 2, DALQuery.WHERE_HAS_NULL_VALUE);


                    query.setOrderBy("SMARTFORMPARTICIPANTS_dtDateAdded", "DESC");
                    query.setOrderBy("SMARTFORMPARTICIPANTS_intSmartformParticipantID", "DESC");
                    query.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");


                    ResultSet rsResult = query.executeSelect();


                    //String strSearchSmartformParticipant = QueryChannel.buildSearchXMLFileAndLockRecord("search_smartform_participant", rsResult, vtSmartformParticipantView, "SMARTFORMPARTICIPANTS", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", lockRequest, LockRecord.READ_ONLY);

                    if (rsResult.next()){

                        rsResult.beforeFirst();
                        Vector vecRows = SmartformUtilities.buildBioanalysisTable(rsResult);
                        if (vecRows != null)
                        {
                            strXML += QueryChannel.buildColumnLabelXMLFile(vtSmartformParticipantView);
                            strXML += "<participant>" + listOfPartipants.get(counter) +  "</participant>"; 
                            for (int i = 0; i < vecRows.size(); i++)
                            {
                                    strSearchSmartformParticipant = SmartformUtilities.buildSearchXMLFromTable("search_smartform_participant", (Hashtable)vecRows.elementAt(i));
                                    strXML += strSearchSmartformParticipant;
                            }                                
                        }
                    }
                    else
                    {
                        if ("biospecimen_view".equalsIgnoreCase(runtimeData.getParameter("strcurrent"))){
                        strXML += "<participant>" + listOfPartipants.get(counter) +  "</participant>";
                        }
                    }    

                    rsResult.close();

                }

                // build smart form dropdown list
                query.reset();
                query.setDomain("SMARTFORM", null, null, null);
                query.setFields(vtSmartformView, null);
                query.setWhere(null, 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SMARTFORM_strDomain", "=", strDomain, 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("SMARTFORM_intSmartformOrder","ASC");
                ResultSet rsResult = query.executeSelect();
                String strSearchSmartform = QueryChannel.buildSearchXMLFileAndLockRecord("search_smartform", rsResult, vtSmartformView, "SMARTFORM", "SMARTFORM_intSmartformID", lockRequest, LockRecord.READ_ONLY);
                strXML += strSearchSmartform;
                strXML += "<domain>" + strDomain + "</domain>";

                //String strSearchSmartform = QueryChannel.buildSearchXMLFile("search_smartform", rsResult, vtSmartformView);
                rsResult.close();
                //System.out.println(strXML);

                if(runtimeData.getParameter("var1") != null)
                {
                    var1 = runtimeData.getParameter("var1");
                    var2 = runtimeData.getParameter("var2");
                }
                var3 = runtimeData.getParameter("var3");
                var4 = runtimeData.getParameter("var4");                    
                
                if((runtimeData.getParameter("strcurrent") != null) && (strDomain.equalsIgnoreCase("Admissions")))
                {
                    if (runtimeData.getParameter("ADMISSIONS_intPatientID") != null)
                    {
                        strBackButton = "module=core&action=admissions";
                        strBackButton += "&PATIENT_intInternalPatientID=" + runtimeData.getParameter("ADMISSIONS_intPatientID");
                        strBackButton = Utilities.cleanForXSL(strBackButton);
                    }
                }
                else if(runtimeData.getParameter("strcurrent") != null && runtimeData.getParameter("strcurrent").equalsIgnoreCase("biospecimen_view")) // doing bioanalysis from biospecimen channel
                {
                    strBackButton = "module=core&action=view_biospecimen";
                    strBackButton += "&BIOSPECIMEN_intBiospecimenID=" + runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID");
                    strBackButton = Utilities.cleanForXSL(strBackButton);
                }
                else if(runtimeData.getParameter("strcurrent") != null && runtimeData.getParameter("strcurrent").equalsIgnoreCase("patient_view")){
                       strBackButton = "current=patient_results&PATIENT_intInternalPatientID=" + runtimeData.getParameter("PATIENT_intPatientID");
                       strBackButton += "&action=view_patient";
                       strBackButton = Utilities.cleanForXSL(strBackButton); 
                       strXML += "<PATIENT_intPatientID>" + runtimeData.getParameter("PATIENT_intPatientID") + "</PATIENT_intPatientID>";
                }
                lockRequest.lockDelayWrite();

                // Buid the header for the Bioanalysis Table
                if (SmartformUtilities.BIOANALYSIS_BIO_ID == true)
                {
                    strXML += "<OriginalBioID>true</OriginalBioID>";
                }    
                
                String strPatientKey = "";
                
                if (runtimeData.getParameter("PATIENT_intPatientID")!= null && !runtimeData.getParameter("PATIENT_intPatientID").equals(""))
                {
                    strPatientKey = runtimeData.getParameter("PATIENT_intPatientID");
                }
                else if ( runtimeData.getParameter("intInternalPatientID") != null && !runtimeData.getParameter("intInternalPatientID").equals("")) 
                {                
                    strPatientKey = runtimeData.getParameter("intInternalPatientID");
                }    
                else
                {
                    // get the patient key from the biospecimen key
                    if (runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID") != null && !runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID").equals(""))
                    {    
                        strPatientKey = Integer.toString(neuragenix.bio.utilities.PatientUtilities.getPatientKey(Integer.parseInt(runtimeData.getParameter("BIOSPECIMEN_intBiospecimenID"))));
                    }                    
                }    

                if (!strPatientKey.equals(""))
                {    
                    strXML += "<PATIENT_intInternalPatientID>" + strPatientKey +  "</PATIENT_intInternalPatientID>";
                    // build smart form dropdown list
                    query.reset();
                    query.setDomain("PATIENT", null, null, null);
                    query.setField("PATIENT_strFirstName", null);
                    query.setField("PATIENT_strSurname", null);
                    query.setWhere(null, 0, "PATIENT_intInternalPatientID", "=", strPatientKey, 0, DALQuery.WHERE_HAS_VALUE);
                    rsResult = query.executeSelect();

                    if (rsResult.next())
                    {
                        strXML += "<PATIENT_strSurname>" + rsResult.getString("PATIENT_strSurname") +  "</PATIENT_strSurname>";
                        strXML += "<PATIENT_strFirstName>" + rsResult.getString("PATIENT_strFirstName") +  "</PATIENT_strFirstName>";
                    }
                }
                                            
            }
            else {
                buildNoDomainForm();
            }

        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
    }
    //}
    
 
    
     
        
    //Study---SmartformList
    private void buildViewStudySmartformList() {
        Vector vtSmartformParticipantView = DatabaseSchema.getFormFields("csmartform_smartform_participant_study_view");
        Vector vtSmartformView = DatabaseSchema.getFormFields("csmartform_smartform_view");
        try {
            // unlock all records
            clearLockRequest();
            
            // create new lock request
            lockRequest = new LockRequest(authToken);
            
            // register the lock to session manager
            SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartform", lockRequest);
            
            // check if the domain specified
            if (runtimeData.getParameter("domain") != null) {
                // build the list of all smartform related to this domain and this participant
                String strDomain = runtimeData.getParameter("domain");
                String intParticipantID = runtimeData.getParameter("participant");
                DALQuery query = new DALQuery();
                query.setDomain("PATIENT_SMARTFORM", null, null, null);
                query.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "INNER JOIN");
                //                    query.setDomain("STUDYSURVEY", "SMARTFORMPARTICIPANTS_intSmartformID", "STUDYSURVEY_intSurveyID", "LEFT JOIN");
                query.setDomain("SMARTFORM", "SMARTFORMPARTICIPANTS_intSmartformID", "SMARTFORM_intSmartformID", "LEFT JOIN");
                query.setFields(vtSmartformParticipantView, null);
                query.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "PATIENT_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "PATIENT_SMARTFORM_intStudyID", "=", runtimeData.getParameter("intStudyID"), 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", strDomain, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intParticipantID", "=", intParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("SMARTFORMPARTICIPANTS_dtDateAdded", "DESC");
                query.setOrderBy("SMARTFORMPARTICIPANTS_intSmartformParticipantID", "DESC");
                ResultSet rsResult = query.executeSelect();
                String strSearchSmartformParticipant = QueryChannel.buildSearchXMLFileAndLockRecord("search_smartform_participant", rsResult, vtSmartformParticipantView, "SMARTFORMPARTICIPANTS", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", lockRequest, LockRecord.READ_ONLY);
                
                rsResult.close();
                
                // build smart form dropdown list
                query.reset();
                query.setDomain("SMARTFORM", null, null, null);
                query.setFields(vtSmartformView, null);
                query.setWhere(null, 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                rsResult = query.executeSelect();
                String strSearchSmartform = QueryChannel.buildSearchXMLFileAndLockRecord("search_smartform", rsResult, vtSmartformView, "SMARTFORM", "SMARTFORM_intSmartformID", lockRequest, LockRecord.READ_ONLY);
                
                rsResult.close();
                
                //String strSearchSmartform = QueryChannel.buildSearchXMLFile("search_smartform", rsResult, vtSmartformView);
                strXML = QueryChannel.buildColumnLabelXMLFile(vtSmartformParticipantView) +
                strSearchSmartformParticipant + strSearchSmartform;
                strXML += "<domain>" + strDomain + "</domain>";
                strXML += "<participant>" + intParticipantID + "</participant>";
                strXML += "<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>";
                if(runtimeData.getParameter("var1") != null) {
                    var1 = runtimeData.getParameter("var1");
                    var2 = runtimeData.getParameter("var2");
                }
                var3 = runtimeData.getParameter("var3");
                var4 = runtimeData.getParameter("var4");                    

                
                if( strDomain.equals( "Study" )){
                    strBackButton = "title="+runtimeData.getParameter("title")+"&PATIENT_intInternalPatientID="+runtimeData.getParameter("participant")+"&strOrigin="+runtimeData.getParameter("strOrigin");
                    strBackButton = Utilities.cleanForXSL(strBackButton);
                }
                
                lockRequest.lockDelayWrite();
            }
            else {
                buildNoDomainForm();
            }
            
        }
        catch(Exception e) {
            
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
    }
    
    //Biospecimen smartform
    private void buildViewBiospecimenSmartformList() {
        Vector vtSmartformParticipantView = DatabaseSchema.getFormFields("csmartform_smartform_participant_biospecimen_view");
        Vector vtSmartformView = DatabaseSchema.getFormFields("csmartform_smartform_view");
        try {
            // unlock all records
            clearLockRequest();
            
            // create new lock request
            lockRequest = new LockRequest(authToken);
            
            // register the lock to session manager
            SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartform", lockRequest);
            
            // check if the domain specified
            if (runtimeData.getParameter("domain") != null) {
                // build the list of all smartform related to this domain and this participant
                String strDomain = runtimeData.getParameter("domain");
                String intParticipantID = runtimeData.getParameter("participant");
                DALQuery query = new DALQuery();
                query.setDomain("BIOSPECIMEN_SMARTFORM", null, null, null);
                query.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "INNER JOIN");
                //                    query.setDomain("STUDYSURVEY", "SMARTFORMPARTICIPANTS_intSmartformID", "STUDYSURVEY_intSurveyID", "LEFT JOIN");
                query.setDomain("SMARTFORM", "SMARTFORMPARTICIPANTS_intSmartformID", "SMARTFORM_intSmartformID", "LEFT JOIN");
                query.setFields(vtSmartformParticipantView, null);
                query.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "BIOSPECIMEN_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "BIOSPECIMEN_SMARTFORM_intStudyID", "=", runtimeData.getParameter("intStudyID"), 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", strDomain, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intParticipantID", "=", intParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("SMARTFORMPARTICIPANTS_dtDateAdded", "DESC");
                query.setOrderBy("SMARTFORMPARTICIPANTS_intSmartformParticipantID", "DESC");
                ResultSet rsResult = query.executeSelect();
                String strSearchSmartformParticipant = QueryChannel.buildSearchXMLFileAndLockRecord("search_smartform_participant", rsResult, vtSmartformParticipantView, "SMARTFORMPARTICIPANTS", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", lockRequest, LockRecord.READ_ONLY);
                
                rsResult.close();
                
                // build smart form dropdown list
                query.reset();
                query.setDomain("SMARTFORM", null, null, null);
                query.setFields(vtSmartformView, null);
                query.setWhere(null, 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                rsResult = query.executeSelect();
                String strSearchSmartform = QueryChannel.buildSearchXMLFileAndLockRecord("search_smartform", rsResult, vtSmartformView, "SMARTFORM", "SMARTFORM_intSmartformID", lockRequest, LockRecord.READ_ONLY);
                
                rsResult.close();
                
                //String strSearchSmartform = QueryChannel.buildSearchXMLFile("search_smartform", rsResult, vtSmartformView);
                strXML = QueryChannel.buildColumnLabelXMLFile(vtSmartformParticipantView) +
                strSearchSmartformParticipant + strSearchSmartform;
                strXML += "<domain>" + strDomain + "</domain>";
                strXML += "<participant>" + intParticipantID + "</participant>";
                strXML += "<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>";
                if(runtimeData.getParameter("var1") != null) {
                    var1 = runtimeData.getParameter("var1");
                    var2 = runtimeData.getParameter("var2");
                }
                var3 = runtimeData.getParameter("var3");
                var4 = runtimeData.getParameter("var4");
                
                if(runtimeData.getParameter("strcurrent") != null) // doing study analysis from biospecimen channel
                {
                   strBackButton = "module=core&action=view_biospecimen";
                   strBackButton += "&BIOSPECIMEN_intBiospecimenID=" + runtimeData.getParameter("intBiospecimenID");
                   strBackButton = Utilities.cleanForXSL(strBackButton);
                }
                lockRequest.lockDelayWrite();
            }
            else {
                buildNoDomainForm();
            }
            
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
    }
    /**
     * Add a smartform to the participant
     */
    private void doAddSmartformParticipant() {
        Vector vtAddSmartformParticipant = DatabaseSchema.getFormFields("csmartform_smartform_participant_add");
        
        try {
            // setting needed parameters
            SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
            runtimeData.setParameter("SMARTFORMPARTICIPANTS_strAddedBy", authToken.getUserIdentifier());
            runtimeData.setParameter("SMARTFORMPARTICIPANTS_dtDateAdded", fm.format(new java.util.Date()));
            runtimeData.setParameter("SMARTFORMPARTICIPANTS_strSmartformStatus", SMARTFORM_STATUS[0]);
            
            // insert new smartform participant
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
            query.setFields(vtAddSmartformParticipant, runtimeData);
            query.executeInsert();
            
            // rebuild the view smartform participant page
            buildViewDomainSmartformList();
        }
        catch(Exception e) {
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
    }
    
    /**
     * Update a smartform user note
     */
    private void doUpdateSmartformParticipantUserNote() {
        Vector vtUpdateSmartformParticipantUserNote = DatabaseSchema.getFormFields("csmartform_smartform_participant_user_note_update");
        
        try {
            // setting needed parameters
            SimpleDateFormat fm = new SimpleDateFormat("dd/MM/yyyy");
            runtimeData.setParameter("SMARTFORMPARTICIPANTS_strLastUpdatedBy", authToken.getUserIdentifier());
            runtimeData.setParameter("SMARTFORMPARTICIPANTS_dtDateLastUpdated", fm.format(new java.util.Date()));
            
            // unlock all records
            clearLockRequest();
            
            // create new lock request
            lockRequest = new LockRequest(authToken);
            // register the lock to session manager
            SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartform", lockRequest);
            lockRequest.addLock("SMARTFORMPARTICIPANTS", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), LockRecord.READ_WRITE);
            lockRequest.lockDelayWrite();
            
            // if we can lock the record for update, perform the update
            if (lockRequest.isValid() && lockRequest.lockWrites()) {
                // update smartform participant
                DALQuery query = new DALQuery();
                query.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
                query.setFields(vtUpdateSmartformParticipantUserNote, runtimeData);
                query.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), 0, DALQuery.WHERE_HAS_VALUE);
                query.executeUpdate();
                if(runtimeData.getParameter("domain") != null && runtimeData.getParameter("domain").equals("Study")) {
                    query.reset();
                    query.setDomain("PATIENT_SMARTFORM", null, null, null);
                    query.setField("PATIENT_SMARTFORM_intStudyID",null);
                    query.setWhere(null, 0, "PATIENT_SMARTFORM_intSmartformKey", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rsstudy = query.executeSelect();
                    if(rsstudy.next()) {
                        String intStudyID = rsstudy.getString("PATIENT_SMARTFORM_intStudyID");
                        runtimeData.setParameter("intStudyID",intStudyID);
                    }
                    rsstudy.close();
                }
                else if(runtimeData.getParameter("domain") != null && (runtimeData.getParameter("domain").equals("Biospecimen") || runtimeData.getParameter("domain").equals("Bioanalysis")))  {
                    query.reset();
                    query.setDomain("BIOSPECIMEN_SMARTFORM", null, null, null);
                    query.setField("BIOSPECIMEN_SMARTFORM_intStudyID",null);
                    query.setWhere(null, 0, "BIOSPECIMEN_SMARTFORM_intSmartformKey", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rsstudy = query.executeSelect();
                    if(rsstudy.next()) {
                        String intStudyID = rsstudy.getString("BIOSPECIMEN_SMARTFORM_intStudyID");
                        runtimeData.setParameter("intStudyID",intStudyID);
                    }
                    rsstudy.close();
                }
                // unlock the record
                lockRequest.unlockWrites();
            }
            else
                strErrorMessage = "Update failed since this record is being viewed by other users. Please try again later.";
            
            // rebuild the view smartform participant page
            buildViewDomainSmartformList();
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
    }
    
    /**
     * Copy a smartform participant
     */
    private void doCopySmartformParticipant() {
        Vector vtSmartformParticipantCopy = DatabaseSchema.getFormFields("csmartform_smartform_participant_copy");
        Vector vtSmartformResultCopy = DatabaseSchema.getFormFields("csmartform_smartform_result_copy");
        String strNewInsertedSmartformPartKey = "";
        try {
            // get the current smartform participant
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
            query.setFields(vtSmartformParticipantCopy, null);
            query.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsResult = query.executeSelect();
            
            // copy the smartform participant
            if (rsResult.next()) {
                query.reset();
                query.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
                if(rsResult.getString("SMARTFORMPARTICIPANTS_strSmartformStatus").equals("Completed")) {
                    if(runtimeData.getParameter("domain") != null && runtimeData.getParameter("domain").equals("CASE")) {
                        vtSmartformParticipantCopy.remove("SMARTFORMPARTICIPANTS_strSmartformStatus");
                        query.setField("SMARTFORMPARTICIPANTS_strSmartformStatus","Incomplete");
                    }
                }
                for (int i=0; i < vtSmartformParticipantCopy.size(); i++) {
                    DBField field = (DBField) DatabaseSchema.getFields().get(vtSmartformParticipantCopy.get(i));
                    //added to handle empty fields----------rennypv
                    if(rsResult.getString((String) vtSmartformParticipantCopy.get(i)) == null || (rsResult.getString((String) vtSmartformParticipantCopy.get(i)).equals(""))) {
                        query.setField((String) vtSmartformParticipantCopy.get(i), "");
                    }
                    //rennypv
                    if (field.getDataType() == DBMSTypes.DATE_TYPE)
                        query.setField((String) vtSmartformParticipantCopy.get(i), Utilities.convertDateForDisplay(rsResult.getString((String) vtSmartformParticipantCopy.get(i))));
                    else
                        query.setField((String) vtSmartformParticipantCopy.get(i), rsResult.getString((String) vtSmartformParticipantCopy.get(i)));
                }
                query.executeInsert();
                strNewInsertedSmartformPartKey = QueryChannel.getNewestKeyAsString(query);
            }
            
            rsResult.close();
            
            if(runtimeData.getParameter("domain") != null && runtimeData.getParameter("domain").equals("Study")) {
                String smartformkey = strNewInsertedSmartformPartKey;
                String intStudyID = null;
                query.reset();
                query.setDomain("PATIENT_SMARTFORM", null, null, null);
                query.setFields(DatabaseSchema.getFormFields("csmartform_check_smartform_participant"),null);
                query.setWhere(null, 0, "PATIENT_SMARTFORM_intSmartformKey", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsstudy = query.executeSelect();
                if(rsstudy.next()) {
                    //intStudyID = rsstudy.getString("PATIENT_SMARTFORM_intStudyID");
                    ResultSetMetaData rsmd = rsstudy.getMetaData();
                    for(int i=1;i<=rsmd.getColumnCount();i++) {
                        runtimeData.setParameter(rsmd.getColumnName(i),rsstudy.getString(rsmd.getColumnName(i)));
                    }
                    runtimeData.setParameter("PATIENT_SMARTFORM_intSmartformKey",smartformkey);
                    query.clearFields();
                    query.clearWhere();
                    query.setFields(DatabaseSchema.getFormFields("csmartform_check_smartform_participant"),runtimeData);
                    query.executeInsert();
                    runtimeData.setParameter("intStudyID",runtimeData.getParameter("PATIENT_SMARTFORM_intStudyID"));
                }
                rsstudy.close();
                
            }
            else if(runtimeData.getParameter("domain") != null && runtimeData.getParameter("domain").equals("Biospecimen")) {
                String smartformkey = strNewInsertedSmartformPartKey;
                String intStudyID = null;
                query.reset();
                query.setDomain("BIOSPECIMEN_SMARTFORM", null, null, null);
                query.setFields(DatabaseSchema.getFormFields("csmartform_check_smartform_biospecimen_participant"),null);
                query.setWhere(null, 0, "BIOSPECIMEN_SMARTFORM_intSmartformKey", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsstudy = query.executeSelect();
                if(rsstudy.next()) {
                    //intStudyID = rsstudy.getString("PATIENT_SMARTFORM_intStudyID");
                    ResultSetMetaData rsmd = rsstudy.getMetaData();
                    for(int i=1;i<=rsmd.getColumnCount();i++) {
                        runtimeData.setParameter(rsmd.getColumnName(i),rsstudy.getString(rsmd.getColumnName(i)));
                    }
                    runtimeData.setParameter("BIOSPECIMEN_SMARTFORM_intSmartformKey",smartformkey);
                    query.clearFields();
                    query.clearWhere();
                    query.setFields(DatabaseSchema.getFormFields("csmartform_check_smartform_biospecimen_participant"),runtimeData);
                    query.executeInsert();
                    runtimeData.setParameter("intStudyID",runtimeData.getParameter("BIOSPECIMEN_SMARTFORM_intStudyID"));
                }
                rsstudy.close();
                
            }
            // get smartform result for this smartform participant
            query = new DALQuery();
            query.setDomain("SMARTFORMRESULTS", null, null, null);
            query.setFields(vtSmartformResultCopy, null);
            query.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), 0, DALQuery.WHERE_HAS_VALUE);
            rsResult = query.executeSelect();
            
            // Get the newly inserted smartformParticipant key
            String strSFNewKey = strNewInsertedSmartformPartKey;
            
            // copy the smartform results
            while (rsResult.next()) {
                query.reset();
                query.setDomain("SMARTFORMRESULTS", null, null, null);
                for (int i=0; i < vtSmartformResultCopy.size(); i++) {
                    DBField field = (DBField) DatabaseSchema.getFields().get(vtSmartformResultCopy.get(i));
                    
                    // Check if the field is Smartform Participant key
                    if(field.getInternalName().equals("\"SMARTFORMRESULTS_intSmartformParticipantID\"")){
                        query.setField("SMARTFORMRESULTS_intSmartformParticipantID", strSFNewKey);
                        
                    }else{
                        if (field.getDataType() == DBMSTypes.DATE_TYPE)
                            query.setField((String) vtSmartformResultCopy.get(i), Utilities.convertDateForDisplay(rsResult.getString((String) vtSmartformResultCopy.get(i))));
                        else
                            query.setField((String) vtSmartformResultCopy.get(i), rsResult.getString((String) vtSmartformResultCopy.get(i)));
                    }
                    
                }
                
                query.executeInsert();
            }
            
            rsResult.close();
            // rebuild the view smartform participant page
            buildViewDomainSmartformList();
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
    }
    
    /**
     * Delete a smartform participant
     */
    private void doDeleteSmartformParticipant() {
        try {
            // unlock all records
            clearLockRequest();
            
            // create new lock request
            lockRequest = new LockRequest(authToken);
            // register the lock to session manager
            SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartform", lockRequest);
            lockRequest.addLock("SMARTFORMPARTICIPANTS", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), LockRecord.READ_WRITE);
            lockRequest.lockDelayWrite();
            
            // if we can lock the record for update, perform the update
            if (lockRequest.isValid() && lockRequest.lockWrites()) {
                // delete a smartform participant
                DALQuery query = new DALQuery();
                query.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
                query.setField("SMARTFORMPARTICIPANTS_intDeleted", "-1");
                query.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), 0, DALQuery.WHERE_HAS_VALUE);
                query.executeUpdate();
                if(runtimeData.getParameter("domain") != null && runtimeData.getParameter("domain").equals("Study")) {
                    query.reset();
                    query.setDomain("SMARTFORMRESULTS",null,null,null);
                    query.setField("SMARTFORMRESULTS_intDeleted", "-1");
                    query.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();
                    query.reset();
                    query.setDomain("PATIENT_SMARTFORM",null,null,null);
                    query.setField("PATIENT_SMARTFORM_intStudyID",null);
                    query.setWhere(null, 0, "PATIENT_SMARTFORM_intSmartformKey", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rs = query.executeSelect();
                    if(rs.next()) {
                        runtimeData.setParameter("intStudyID",rs.getString("PATIENT_SMARTFORM_intStudyID"));
                    }
                    
                    rs.close();
                    
                    query.clearFields();
                    query.setField("PATIENT_SMARTFORM_intDeleted", "-1");
                    query.executeUpdate();
                }
                else if(runtimeData.getParameter("domain") != null && runtimeData.getParameter("domain").equals("Biospecimen")) {
                    query.reset();
                    query.setDomain("SMARTFORMRESULTS",null,null,null);
                    query.setField("SMARTFORMRESULTS_intDeleted", "-1");
                    query.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();
                    query.reset();
                    query.setDomain("BIOSPECIMEN_SMARTFORM",null,null,null);
                    query.setField("BIOSPECIMEN_SMARTFORM_intStudyID",null);
                    query.setWhere(null, 0, "BIOSPECIMEN_SMARTFORM_intSmartformKey", "=", runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID"), 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rs = query.executeSelect();
                    if(rs.next()) {
                        runtimeData.setParameter("intStudyID",rs.getString("BIOSPECIMEN_SMARTFORM_intStudyID"));
                    }
                    rs.close();
                    query.clearFields();
                    query.setField("BIOSPECIMEN_SMARTFORM_intDeleted", "-1");
                    query.executeUpdate();
                }
                // unlock the record
                lockRequest.unlockWrites();
            }
            else
                strErrorMessage = "Delete failed since this record is being viewed by other users. Please try again later.";
            
            // rebuild the view smartform participant page
            buildViewDomainSmartformList();
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
    }
    
    /**
     * Build the page for the first time to go to the smartform result.
     */
    private void buildViewSmartformResultFirstTime() {
 
        
        strStylesheet = SMARTFORM_RESULT_VIEW;
        
        try {
            String strSmartformID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID");
            String strSmartformParticipantID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID");
            intCurrentPage = Integer.parseInt(runtimeData.getParameter("SMARTFORMPARTICIPANTS_intCurrentPage"));
            
            if(runtimeData.getParameter("var1") != null) {
                var1 = runtimeData.getParameter("var1");
                var2 = runtimeData.getParameter("var2");
            }
            var3 = runtimeData.getParameter("var3");
            var4 = runtimeData.getParameter("var4");                
            
            
            // create lock
            // unlock all records
            clearLockRequest();
            
            // create new lock request
            lockRequest = new LockRequest(authToken);
            // register the lock to session manager
            SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartform", lockRequest);
            lockRequest.addLock("SMARTFORM", strSmartformID, LockRecord.READ_ONLY);
            if ( (strSmartformParticipantID != null) && !strSmartformParticipantID.equals("") && !strSmartformParticipantID.equals("null"))
            {    
                lockRequest.addLock("SMARTFORMPARTICIPANTS", strSmartformParticipantID, LockRecord.READ_WRITE);
            }            
            lockRequest.lockDelayWrite();
            
            strXML = "<domain>" + runtimeData.getParameter("domain") + "</domain>" +
            "<participant>" + runtimeData.getParameter("participant") + "</participant>" +
            "<SMARTFORMPARTICIPANTS_intSmartformID>" + runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID") + "</SMARTFORMPARTICIPANTS_intSmartformID>" +
            "<SMARTFORMPARTICIPANTS_intSmartformParticipantID>" + runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID") + "</SMARTFORMPARTICIPANTS_intSmartformParticipantID>" +
            // Anita Start
            "<SMARTFORMPARTICIPANTS_intCurrentPage>"+ runtimeData.getParameter("SMARTFORMPARTICIPANTS_intCurrentPage") +"</SMARTFORMPARTICIPANTS_intCurrentPage>" +
            //Anita End
            buildSmartformResultPage(strSmartformID, strSmartformParticipantID, intCurrentPage);
            //rennypv for Study
            if((runtimeData.getParameter("domain").equals("Study")) || (runtimeData.getParameter("domain").equals("Biospecimen")) || (runtimeData.getParameter("domain").equals("Patient")) || (runtimeData.getParameter("domain").equals("Bioanalysis")) || (runtimeData.getParameter("domain").equals("Admissions"))) {
                strXML += "<SMARTFORM_smartformname>" + runtimeData.getParameter("SMARTFORM_smartformname")+ "</SMARTFORM_smartformname>";
                
                if( runtimeData.getParameter("fromPatient") != null ){
                    strXML += "<fromPatient>"+ runtimeData.getParameter("fromPatient") + "</fromPatient>";
                }
                
                if( runtimeData.getParameter("fromPatientAnalysis") != null ){
                    strXML += "<fromPatientAnalysis>"+ runtimeData.getParameter("fromPatientAnalysis") + "</fromPatientAnalysis>";
                    if (runtimeData.getParameter("PATIENT_intPatientID") != null)
                    {    
                        strXML += "<PATIENT_intPatientID>" + runtimeData.getParameter("PATIENT_intPatientID") + "</PATIENT_intPatientID>";
                    }
                    
                    if (runtimeData.getParameter("participant") != null)
                    {
                        runtimeData.setParameter("strTitle", BiospecimenUtilities.getUserBiospecimenID(Integer.parseInt(runtimeData.getParameter("participant"))));
                    }    
                }
                                
                if( runtimeData.getParameter("fromSFBatchGeneration") != null ){
                    strXML += "<fromSFBatchGeneration>"+ runtimeData.getParameter("fromSFBatchGeneration") + "</fromSFBatchGeneration>";
                }

                if( runtimeData.getParameter("fromBiospecimenKey") != null ){
                    strXML += "<fromBiospecimenKey>"+ runtimeData.getParameter("fromBiospecimenKey") + "</fromBiospecimenKey>";
                }
                
                if(runtimeData.getParameter("var1") != null) {
                    strXML += "<strTitle>"+runtimeData.getParameter("strTitle")+"</strTitle>"+"<var1>"+ runtimeData.getParameter("var1") + "</var1>" + "<var2>" + runtimeData.getParameter("var2") + "</var2>"+ "<var3>" + runtimeData.getParameter("var3") + "</var3>"+ "<var4>" + runtimeData.getParameter("var4") + "</var4>";
                }
                else {
                    strXML += "<strTitle>" + runtimeData.getParameter("strTitle") + "</strTitle>" + "<var1>"+ var1 + "</var1>" + "<var2>" + var2 + "</var2>"+ "<var3>" + var3 + "</var3>"+ "<var4>" + var4 + "</var4>";
                }
                
            }
            //rennypv end
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
    }
    
    /**
     * Build the next page of the smartform result.
     */
    private void buildViewSmartFormResultCurrentPage() {
        strStylesheet = SMARTFORM_RESULT_VIEW;
        
        try {
            String strSmartformID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID");
            String strSmartformParticipantID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID");
            if(strErrorCompleted.equals("") && strSmartformParticipantID != null) {
                updateSmartform(strSmartformParticipantID, "Incomplete", intCurrentPage);
            }
            
            // create lock
            // unlock all records
            clearLockRequest();
            
            // create new lock request
            lockRequest = new LockRequest(authToken);
            // register the lock to session manager
            SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartform", lockRequest);
            lockRequest.addLock("SMARTFORM", strSmartformID, LockRecord.READ_ONLY);
            if ( (strSmartformParticipantID != null) && !strSmartformParticipantID.equals("") && !strSmartformParticipantID.equals("null"))
            {    
                lockRequest.addLock("SMARTFORMPARTICIPANTS", strSmartformParticipantID, LockRecord.READ_WRITE);
            }
            lockRequest.lockDelayWrite();
            
            strXML = "<domain>" + runtimeData.getParameter("domain") + "</domain>" +
            "<participant>" + runtimeData.getParameter("participant") + "</participant>" +
            "<SMARTFORMPARTICIPANTS_intSmartformID>" + runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID") + "</SMARTFORMPARTICIPANTS_intSmartformID>" +
            "<SMARTFORMPARTICIPANTS_intSmartformParticipantID>" + runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID") + "</SMARTFORMPARTICIPANTS_intSmartformParticipantID>" +
            // Anita Start
            "<SMARTFORMPARTICIPANTS_intCurrentPage>"+ runtimeData.getParameter("SMARTFORMPARTICIPANTS_intCurrentPage") +"</SMARTFORMPARTICIPANTS_intCurrentPage>" +
            //Anita End
            buildSmartformResultPage(strSmartformID, strSmartformParticipantID, intCurrentPage);
            
            
            
           /* if ((runtimeData.getParameter("domain").equals("Bioanalysis")) && (runtimeData.getParameter("formLink").equals("true")))
            {
                
                
                
                
            }
            */
            
            
            if((runtimeData.getParameter("domain").equals("Study")) || (runtimeData.getParameter("domain").equals("Biospecimen")) || (runtimeData.getParameter("domain").equals("Patient")) || (runtimeData.getParameter("domain").equals("Bioanalysis"))  || (runtimeData.getParameter("domain").equals("Admissions"))) {
                strXML += "<SMARTFORM_smartformname>" + runtimeData.getParameter("SMARTFORM_smartformname")+ "</SMARTFORM_smartformname>" +
                "<var1>"+ var1 + "</var1>" + "<var2>" + var2 + "</var2>" + "<var3>" + var3 + "</var3>"+ "<var4>" + var4 + "</var4>";
                
                
                if( runtimeData.getParameter("fromPatient") != null ){
                    strXML += "<fromPatient>"+ runtimeData.getParameter("fromPatient") + "</fromPatient>";
                }
                
                if( runtimeData.getParameter("fromPatientAnalysis") != null ){
                    strXML += "<fromPatientAnalysis>"+ runtimeData.getParameter("fromPatientAnalysis") + "</fromPatientAnalysis>";
                    if (runtimeData.getParameter("PATIENT_intPatientID") != null)
                    {    
                        strXML += "<PATIENT_intPatientID>" + runtimeData.getParameter("PATIENT_intPatientID") + "</PATIENT_intPatientID>";
                    }

                    if (runtimeData.getParameter("participant") != null)
                    {
                        runtimeData.setParameter("strTitle", BiospecimenUtilities.getUserBiospecimenID(Integer.parseInt(runtimeData.getParameter("participant"))));
                    }                        
                }
                
                if( runtimeData.getParameter("fromSFBatchGeneration") != null ){
                    strXML += "<fromSFBatchGeneration>"+ runtimeData.getParameter("fromSFBatchGeneration") + "</fromSFBatchGeneration>";
                }
                
                if( runtimeData.getParameter("fromBiospecimenKey") != null ){
                    strXML += "<fromBiospecimenKey>"+ runtimeData.getParameter("fromBiospecimenKey") + "</fromBiospecimenKey>";
                }
                
                strXML += "<strTitle>"+runtimeData.getParameter("strTitle")+"</strTitle>";
            }
            
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
    }
    
    /**
     * Build the next page of the smartform result.
     */
    private void buildViewSmartFormResultNextPage() {
        strStylesheet = SMARTFORM_RESULT_VIEW;
        
        String strSmartformID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID");
        String strSmartformParticipantID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID");
        intCurrentPage++;
        
        updateSmartform(strSmartformParticipantID, "Incomplete", intCurrentPage);
        
        strXML = "<domain>" + runtimeData.getParameter("domain") + "</domain>" +
        "<participant>" + runtimeData.getParameter("participant") + "</participant>" +
        "<SMARTFORMPARTICIPANTS_intSmartformID>" + runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID") + "</SMARTFORMPARTICIPANTS_intSmartformID>" +
        "<SMARTFORMPARTICIPANTS_intSmartformParticipantID>" + runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID") + "</SMARTFORMPARTICIPANTS_intSmartformParticipantID>" +
        // Anita Start
        "<SMARTFORMPARTICIPANTS_intCurrentPage>"+ runtimeData.getParameter("SMARTFORMPARTICIPANTS_intCurrentPage") +"</SMARTFORMPARTICIPANTS_intCurrentPage>" +
        //Anita End
        buildSmartformResultPage(strSmartformID, strSmartformParticipantID, intCurrentPage);
        if((runtimeData.getParameter("domain").equals("Study")) || (runtimeData.getParameter("domain").equals("Biospecimen")) || (runtimeData.getParameter("domain").equals("Patient")) || (runtimeData.getParameter("domain").equals("Bioanalysis"))  || (runtimeData.getParameter("domain").equals("Admissions"))) {
            strXML += "<SMARTFORM_smartformname>" + runtimeData.getParameter("SMARTFORM_smartformname")+ "</SMARTFORM_smartformname>" +
            "<var1>"+ var1 + "</var1>" + "<var2>" + var2 + "</var2>" + "<var3>" + var3 + "</var3>"+ "<var4>" + var4 + "</var4>";
            
            if( runtimeData.getParameter("fromPatient") != null ){
                strXML += "<fromPatient>"+ runtimeData.getParameter("fromPatient") + "</fromPatient>";
            }
            
            if( runtimeData.getParameter("fromPatientAnalysis") != null ){
                strXML += "<fromPatientAnalysis>"+ runtimeData.getParameter("fromPatientAnalysis") + "</fromPatientAnalysis>";
                if (runtimeData.getParameter("PATIENT_intPatientID") != null)
                {    
                    strXML += "<PATIENT_intPatientID>" + runtimeData.getParameter("PATIENT_intPatientID") + "</PATIENT_intPatientID>";
                }       
                
                if (runtimeData.getParameter("participant") != null)
                {
                    runtimeData.setParameter("strTitle", BiospecimenUtilities.getUserBiospecimenID(Integer.parseInt(runtimeData.getParameter("participant"))));
                }    
                
            }
            
            if( runtimeData.getParameter("fromSFBatchGeneration") != null ){
                strXML += "<fromSFBatchGeneration>"+ runtimeData.getParameter("fromSFBatchGeneration") + "</fromSFBatchGeneration>";
            }
            
            if( runtimeData.getParameter("fromBiospecimenKey") != null ){
                strXML += "<fromBiospecimenKey>"+ runtimeData.getParameter("fromBiospecimenKey") + "</fromBiospecimenKey>";
            }            
            strXML += "<strTitle>"+runtimeData.getParameter("strTitle")+"</strTitle>";
        }
    }
    
    /**
     * Build the previous page of the smartform result.
     *
     */
    private void buildViewSmartFormResultPreviousPage() {
        strStylesheet = SMARTFORM_RESULT_VIEW;
        
        String strSmartformID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID");
        String strSmartformParticipantID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID");
        intCurrentPage--;
        
        updateSmartform(strSmartformParticipantID, "Incomplete", intCurrentPage);
        
        strXML = "<domain>" + runtimeData.getParameter("domain") + "</domain>" +
        "<participant>" + runtimeData.getParameter("participant") + "</participant>" +
        "<SMARTFORMPARTICIPANTS_intSmartformID>" + runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID") + "</SMARTFORMPARTICIPANTS_intSmartformID>" +
        "<SMARTFORMPARTICIPANTS_intSmartformParticipantID>" + runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID") + "</SMARTFORMPARTICIPANTS_intSmartformParticipantID>" +
        // Anita Start
        "<SMARTFORMPARTICIPANTS_intCurrentPage>"+ runtimeData.getParameter("SMARTFORMPARTICIPANTS_intCurrentPage") +"</SMARTFORMPARTICIPANTS_intCurrentPage>" +
        //Anita End
        buildSmartformResultPage(strSmartformID, strSmartformParticipantID, intCurrentPage);
        if((runtimeData.getParameter("domain").equals("Study")) || (runtimeData.getParameter("domain").equals("Biospecimen")) || (runtimeData.getParameter("domain").equals("Patient")) || (runtimeData.getParameter("domain").equals("Bioanalysis")) || (runtimeData.getParameter("domain").equals("Admissions"))) {
            strXML += "<SMARTFORM_smartformname>" + runtimeData.getParameter("SMARTFORM_smartformname")+ "</SMARTFORM_smartformname>" +
            "<var1>"+ var1 + "</var1>" + "<var2>" + var2 + "</var2>" + "<var3>" + var3 + "</var3>"+ "<var4>" + var4 + "</var4>";
            
            if( runtimeData.getParameter("fromPatient") != null ){
                strXML += "<fromPatient>"+ runtimeData.getParameter("fromPatient") + "</fromPatient>";
            }
            
            if( runtimeData.getParameter("fromPatientAnalysis") != null ){
                strXML += "<fromPatientAnalysis>"+ runtimeData.getParameter("fromPatientAnalysis") + "</fromPatientAnalysis>";
                if (runtimeData.getParameter("PATIENT_intPatientID") != null)
                {    
                    strXML += "<PATIENT_intPatientID>" + runtimeData.getParameter("PATIENT_intPatientID") + "</PATIENT_intPatientID>";
                }
                
                if (runtimeData.getParameter("participant") != null)
                {
                    runtimeData.setParameter("strTitle", BiospecimenUtilities.getUserBiospecimenID(Integer.parseInt(runtimeData.getParameter("participant"))));
                }    
                
            }
            
            if( runtimeData.getParameter("fromSFBatchGeneration") != null ){
                strXML += "<fromSFBatchGeneration>"+ runtimeData.getParameter("fromSFBatchGeneration") + "</fromSFBatchGeneration>";
            }
            
            if( runtimeData.getParameter("fromBiospecimenKey") != null ){
                strXML += "<fromBiospecimenKey>"+ runtimeData.getParameter("fromBiospecimenKey") + "</fromBiospecimenKey>";
            }  
            
            strXML += "<strTitle>"+runtimeData.getParameter("strTitle")+"</strTitle>";
            
        }
    }
    
    /**
     * Complete the smartform result.
     */
    private void buildViewSmartFormResultComplete() {
        strStylesheet = SMARTFORM_RESULT_VIEW;
        
        String strSmartformID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID");
        String strSmartformParticipantID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID");
        
        updateSmartform(strSmartformParticipantID, "Completed", intCurrentPage);
        
        buildViewDomainSmartformList();
    }
    
    /**
     * Build the smartform result based on the last data element key
     */
    private String buildSmartformResultPage(String strSmartformID, String strSmartformParticipantID, int intCurrentPage) {
        //StringBuffer strResult = new StringBuffer();
        Vector vtDataElementDetails = DatabaseSchema.getFormFields("csmartform_dataelement_details");
        try {
            // getting all page break questions
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);
            query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID", "INNER JOIN");
            query.setFields(vtDataElementDetails, null);
            query.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", strSmartformID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "DATAELEMENTS_intDataElementType", "=", "PAGEBREAK", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementNo", "ASC");
            query.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");
            ResultSet rsResult = query.executeSelect();
            
            // calculate where the starting and ending orders
            int intStartDataElementOrder = 0;
            int intEndDataElementOrder = 0;
            int intPageCount = 1;
            
            while (rsResult.next()) {
                if (intPageCount == intCurrentPage - 1) {
                    intStartDataElementOrder = rsResult.getInt("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                }
                else if (intPageCount == intCurrentPage) {
                    intEndDataElementOrder = rsResult.getInt("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                }
                
                intPageCount++;
            }
            rsResult.close();
            // if the smartform has only one page
            if (intStartDataElementOrder == intEndDataElementOrder) {
                query = new DALQuery();
                query.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);
                query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID", "INNER JOIN");
                query.setFields(vtDataElementDetails, null);
                query.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", strSmartformID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");
                
                rsResult = query.executeSelect();
                if (rsResult != null && rsResult.next())
                {
                    rsResult.last();
                }
                else
                {
                    rsResult.first();
                }
                
                if (rsResult.getRow() != 0)
                {
                    intEndDataElementOrder = rsResult.getInt("SMARTFORMTODATAELEMENTS_intDataElementOrder") + 1;
                }    
                rsResult.beforeFirst();
            }
            // if the current page is the last page
            else if (intPageCount == intCurrentPage) {
                query = new DALQuery();
                query.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);
                query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID", "INNER JOIN");
                query.setFields(vtDataElementDetails, null);
                query.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", strSmartformID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", ">", new Integer(intStartDataElementOrder).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");
                rsResult = query.executeSelect();
                rsResult.last();
                intEndDataElementOrder = rsResult.getInt("SMARTFORMTODATAELEMENTS_intDataElementOrder") + 1;
                rsResult.beforeFirst();
            }
            else {
                query = new DALQuery();
                query.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);
                query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID", "INNER JOIN");
                query.setFields(vtDataElementDetails, null);
                query.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", strSmartformID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", ">", new Integer(intStartDataElementOrder).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", "<", new Integer(intEndDataElementOrder).toString(), 0, DALQuery.WHERE_HAS_VALUE);
                query.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");
                rsResult = query.executeSelect();
            }
            

            // From the database get all the results of type "REPEATABLE TEXT"
            if (htRepeatableDEResults == null)
            {    
                htRepeatableDEResults = SmartformUtilities.getSmartformResultsForType (strSmartformParticipantID, intStartDataElementOrder, intEndDataElementOrder, "REPEATABLE TEXT");
                // Put in correct format to generate XML
                processRepeatableDEforDisplay();
            }
            
           
            
            String strReturnValue = "";
            if( runtimeData.getParameter("fromSFBatchGeneration") != null )
            {
                strReturnValue = SmartformUtilities.buildSearchXMLFile("search_dataelement", rsResult, htRepeatableDEResults, vtDataElementDetails, strSmartformParticipantID, intStartDataElementOrder, intEndDataElementOrder, runtimeData, SFEntityObject);     
            }
            else
            {
                strReturnValue = SmartformUtilities.buildSearchXMLFile("search_dataelement", rsResult, htRepeatableDEResults, vtDataElementDetails, strSmartformParticipantID, intStartDataElementOrder, intEndDataElementOrder, runtimeData, null);
            }    

             
            strReturnValue += "<pagecount>" + intPageCount + "</pagecount>" +
            "<currentpage>" + intCurrentPage + "</currentpage>" +
            "<startorder>" + intStartDataElementOrder + "</startorder>" +
            "<endorder>" + intEndDataElementOrder + "</endorder>";
            rsResult.close();
            
            return strReturnValue;
            
            
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
        return "";
    }
    
    /**
     * Save the result from the current page
     */
    private boolean saveSmartformResult() {
        boolean blVal = true;
        try {
            String strSmartformID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID");
            String strSmartformParticipantID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID");
            String strStartOrder = runtimeData.getParameter("startorder");
            String strEndOrder = runtimeData.getParameter("endorder");

            processRepeatableDEforDB();
            if ( !strSmartformParticipantID.equals("") && !strSmartformParticipantID.equals("null"))
            {    
                if( SmartformUtilities.saveSmartformResult(strSmartformID, strSmartformParticipantID, strStartOrder, strEndOrder, runtimeData) == true){
                    blVal = true;
                }else{
                    blVal = false;
                }
            }
            else
            {
                if( runtimeData.getParameter("fromSFBatchGeneration") != null )
                {    
                    // We are performing a smartform batch results generation - do not have a smartform participant ID
                    // Get the smarform entity details to be used in smartform batch generation that is the dataelement answers
                    boolean blSaved = SmartformUtilities.saveSmartformResulttoSFEntity(strSmartformID, strStartOrder, strEndOrder, runtimeData, SFEntityObject);

                    if(blSaved  == true){
                        blVal = true;
                        // Update the object that was created for Smartform batch generation
                        SmartformBatchGenerationInfo SFBatchGenInfo = (SmartformBatchGenerationInfo) ip.getAttribute("SFBatchGenInfo");                    
                        // Set the field data to cloned during smartform batch generation
                        SFBatchGenInfo.setSmartformFieldData (SFEntityObject);
                    }else{
                        blVal = false;
                    }
                }
            }    
            
        }
        catch(Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
        return blVal;
    }
    
    /**
     * Validate data
     */
    private String validateData() {
        String strSmartformID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformID");
        String strStartOrder = runtimeData.getParameter("startorder");
        String strEndOrder = runtimeData.getParameter("endorder");
        
        return SmartformUtilities.validateSmartformData(runtimeData, strSmartformID, strStartOrder, strEndOrder);
    }
    
    /**
     * Update smartform status and current page
     * @param smartform participant key
     * @param status
     * @param current page
     */
    private void updateSmartform(String strSmartformParticipantID, String strStatus, int intCurrentPage) {
        try {
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
            query.setField("SMARTFORMPARTICIPANTS_strSmartformStatus", strStatus);
            query.setField("SMARTFORMPARTICIPANTS_intCurrentPage", new Integer(intCurrentPage).toString());
            query.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
            query.executeUpdate();
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
    }
    
    /**
     * Build the page to show the message whenever ther is no domain specified.
     */
    private void buildNoDomainForm() {
        strStylesheet = SMARTFORM_NO_DOMAIN;
        strXML = "";
    }
    
    /**
     * Clear the lock request
     */
    private void clearLockRequest() {
        try {
            if (lockRequest != null && lockRequest.isValid())
                lockRequest.unlock();
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
        finally {
            lockRequest = null;
        }
    }
    
    /** Output channel content to the portal
     *  @param out a sax document handler
     */
    public void renderXML(ContentHandler out) throws PortalException {
        
        // Create a new XSLT styling engine
        XSLT xslt = new XSLT(this);
        
        //System.out.println ("[CSmartform] XML = " + strXML);
        xslt.setXML(strXML);
        
        // specify the stylesheet selector
        xslt.setXSL("CSmartform.ssl", strStylesheet, runtimeData.getBrowserInfo());
        
        // set parameters that the stylesheet needs.
        xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL());
        
        org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL());
        try {
            if(PropertiesManager.getProperty("solution.type").equals("Casegenix")) {
                // set case channel URL param
                upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CCase"));
                xslt.setStylesheetParameter("caseChannelURL", upfTmp.getUPFile());
                xslt.setStylesheetParameter( "caseChannelTabOrder", SessionManager.getTabOrder( authToken, "CCase") );
                
                // set smartform channel URL param
                upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CWorkspace"));
                xslt.setStylesheetParameter("workspaceChannelURL", upfTmp.getUPFile());
                xslt.setStylesheetParameter( "workspaceChannelTabOrder", SessionManager.getTabOrder( authToken, "CWorkspace") );
            }
            else if(PropertiesManager.getProperty("solution.type").equals("Biogenix")) {
                upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CPatient"));
                xslt.setStylesheetParameter("patientChannelURL", upfTmp.getUPFile());
                xslt.setStylesheetParameter( "patientChannelTabOrder", SessionManager.getTabOrder( authToken, "CPatient") );
                
                upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CBiospecimen"));
                xslt.setStylesheetParameter("BiospecimenChannelURL", upfTmp.getUPFile());
                xslt.setStylesheetParameter( "BiospecimenChannelTabOrder", SessionManager.getTabOrder( authToken, "CBiospecimen") );
            }
            
            // set download URL param
            upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL( true ));
            upfTmp.setTargetNodeId(SessionManager.getChannelID(authToken.getSessionUniqueID(), "CDownload"));
            xslt.setStylesheetParameter("nodeId", SessionManager.getChannelID(authToken.getSessionUniqueID(), "CDownload"));
            xslt.setStylesheetParameter("downloadURL", upfTmp.getUPFile());
            
            
        }
        catch(Exception e) {
            LogService.log(LogService.ERROR, "Could not find property solution.type in portal.properties");
        }
        // set the output Handler for the output.
        xslt.setTarget(out);
        
        // do the deed
        xslt.transform();
        
    }
    
    /**
     *  Print out message if user doesn't have the autority to do a specific activity
     */
    private void buildSecurityErrorXMLFile(String aFunctionalArea) {
        strStylesheet = SECURITY;
        strXML  = "<errorstring>No Permission to " + aFunctionalArea + "</errorstring>"
        + "<errortext>The " + staticData.getPerson().getFullName()
        + " is not authorised to " + aFunctionalArea + "</errortext>"
        + "<errordata></errordata>";

    }
    
    /** Check permission
     * @param domain name
     * @param status of the object
     * @param authorisational token
     * @param smartform key
     * @param smartform sensitivity
     * @param permission name
     * @return has permission or not
     */
    private boolean checkPermission(String strDomain, String strParticipantID, AuthToken authToken, String strSmartformID, String strPermissionName) {
        boolean blHasPermission = true;
        
        try {
            if (strSmartformID == null || strSmartformID.trim().length() == 0) {
                //blHasPermission = true;
                return blHasPermission;
            }
            
            /******************* CASE **********************/
            // Check permission for CASE
            if (strDomain.equals("CASE")) {
                // get CASE status
                DALQuery query = new DALQuery();
                query.setDomain("ASSIGNMENTCASE", null, null, null);
                query.setField("ASSIGNMENTCASE_strStatus", null);
                query.setWhere(null, 0, "ASSIGNMENTCASE_intCaseKey", "=", strParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsResult = query.executeSelect();
                
                if (!rsResult.next())
                    return true;
                
                String strStatus = rsResult.getString("ASSIGNMENTCASE_strStatus");
                
                // get smartform sensitivity
                query = new DALQuery();
                query.setDomain("SMARTFORM", null, null, null);
                query.setField("SMARTFORM_strSensitivity", null);
                query.setWhere(null, 0, "SMARTFORM_intSmartformID", "=", strSmartformID, 0, DALQuery.WHERE_HAS_VALUE);
                rsResult = query.executeSelect();
                
                if (!rsResult.next())
                    return true;
                
                String strSensitivity = rsResult.getString("SMARTFORM_strSensitivity");
                
                // the user is assigned the case or not
                
                query = new DALQuery();
                query.setDomain("ASSIGNMENT", null, null, null);
                query.setDomain("ORGUSER", "ASSIGNMENT_intUserKey", "ORGUSER_intOrgUserKey", "INNER JOIN");
                query.setField("ORGUSER_intOrgUserKey", null);
                query.setWhere(null, 0, "ASSIGNMENT_intCaseKey", "=", strParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "ORGUSER_strSystemUser", "=", authToken.getUserIdentifier(), 0, DALQuery.WHERE_HAS_VALUE);
                rsResult = query.executeSelect();
                boolean blAssignedCase = rsResult.next();
                
                
                // if the CASE status is closed, only allow viewing the data
                if (strStatus.equals("Closed")) {
                    if (strPermissionName.equals("add") || strPermissionName.equals("edit") ||
                    strPermissionName.equals("copy") || strPermissionName.equals("delete"))
                        blHasPermission = false;
                    else {
                        // if user is not a super user
                        if (!authToken.hasActivity("case_smartform_completion_superuser")) {
                            // if the Sensitivity is HIGH
                            if (strSensitivity.equals(HIGH)) {
                                String strActivity = "smartform_id_" + strSmartformID + "_" + strPermissionName;
                                
                                if (!blAssignedCase || !authToken.hasActivity(strActivity))
                                    blHasPermission = false;
                            }
                            // if the Sensitivity is LOW
                            else {
                                String strActivity = "smartform_id_" + strSmartformID + "_" + strPermissionName;
                                
                                if (!authToken.hasActivity(strActivity))
                                    blHasPermission = false;
                            }
                        }
                    }
                }
                // if the CASE status is not closed
                else {
                    // if user is not a super user
                    if (!authToken.hasActivity("case_smartform_completion_superuser")) {
                        // if the Sensitivity is HIGH
                        if (strSensitivity.equals(HIGH)) {
                            String strActivity = "smartform_id_" + strSmartformID + "_" + strPermissionName;
                            
                            if (!blAssignedCase || !authToken.hasActivity(strActivity))
                                blHasPermission = false;
                        }
                        // if the Sensitivity is LOW
                        else {
                            String strActivity = "smartform_id_" + strSmartformID + "_" + strPermissionName;
                            
                            if (!authToken.hasActivity(strActivity))
                                blHasPermission = false;
                        }
                    }
                }
            }
            /******************* END CASE **********************/
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
        
        return blHasPermission;
    }
    
    /* To delete the attachment already saved
     *
     */
    private void deleteAttachment() {
        try {
            String strSmartformParticipantID = runtimeData.getParameter("SMARTFORMPARTICIPANTS_intSmartformParticipantID");
            String strDEId = runtimeData.getParameter("varDataElementID");
            
            DALQuery currentQuery = new DALQuery();
            currentQuery.setDomain("SMARTFORMRESULTS", null, null, null);
            currentQuery.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strSmartformParticipantID, 0, DALQuery.WHERE_HAS_VALUE);
            currentQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strDEId, 0, DALQuery.WHERE_HAS_VALUE);
            currentQuery.setWhere("AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            currentQuery.setField("SMARTFORMRESULTS_strDataElementResult", "");
            currentQuery.executeUpdate();
            
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);
        }
        
    }
    
    // Processes the DE result from the database for a repeatable text data element
    // splits the DE result string into individual data based on the separator between each data (";")
    // and puts each data into a vector     
    private void processRepeatableDEforDisplay() 
    {
        
        Enumeration enum = htRepeatableDEResults.keys();
        
        while (enum.hasMoreElements())
        {
            String strDEOrder = enum.nextElement().toString();
            String strDEResult = htRepeatableDEResults.get(strDEOrder).toString();
            String strRepeatableData[] = strDEResult.split(";");
            Vector vtRepeatableDEData = new Vector();
            for (int i=0; i<strRepeatableData.length; i++)
            {
                vtRepeatableDEData.add(strRepeatableData[i]);
            }   
            htRepeatableDEResults.put(strDEOrder, vtRepeatableDEData);
        }    
        
    }    
    
    // Adds data to a repeatable text data element
    // A new entry is added to the vector containing the DE Result Data
    private void addDatatoRepeatableDE(String strDEOrder) 
    {
        
        String strDate = "";
        String strResult = "";
        
        if ((runtimeData.getParameter("d" + strDEOrder + "_Day") == null ||
            runtimeData.getParameter("d" + strDEOrder + "_Day").trim().length() == 0) &&
            (runtimeData.getParameter("d" + strDEOrder + "_Month") == null ||
            runtimeData.getParameter("d" + strDEOrder + "_Month").trim().length() == 0) &&
            (runtimeData.getParameter("d" + strDEOrder + "_Year") == null ||
            runtimeData.getParameter("d" + strDEOrder + "_Year").trim().length() == 0))
        { 
            //Do Nothing
        } 
        else
        {
            strDate = runtimeData.getParameter("d" + strDEOrder + "_Day").trim() + "/" +
                      runtimeData.getParameter("d" + strDEOrder + "_Month").trim() + "/" +          
                      runtimeData.getParameter("d" + strDEOrder + "_Year").trim();
        }
        
        if (runtimeData.getParameter("d" + strDEOrder) != null)
        {    
            strResult = strDate + " " + runtimeData.getParameter("d" + strDEOrder);
        }    
        
        
        Vector vtRepeatableDEData = (Vector) htRepeatableDEResults.get(strDEOrder);
        if (vtRepeatableDEData == null)
        {
            vtRepeatableDEData = new Vector ();
        }    
        
        if (!strResult.equals(""))
        {
            vtRepeatableDEData.add(strResult);
        }    
        
        htRepeatableDEResults.put (strDEOrder, vtRepeatableDEData);
        
    }   
    
    // Removes data from a repeatable text data element
    // The data entry is removed from the vector containing the DE Result Data
    private void removeDataFromRepeatableDE(String strDEOrder, int index) 
    {        
        
        Vector vtRepeatableDEData = (Vector) htRepeatableDEResults.get(strDEOrder);
        vtRepeatableDEData.remove(index);        
        htRepeatableDEResults.put (strDEOrder, vtRepeatableDEData);
        
    }     

    // Updates data in a repeatable text data element
    // The data entry is updated in the vector containing the DE Result Data
    private void updateDataInRepeatableDE() 
    {        
        
        Enumeration enum = htRepeatableDEResults.keys();
        
        while (enum.hasMoreElements())
        {
            String strDEOrder = enum.nextElement().toString();
            Vector vtRepeatableDEData = (Vector) htRepeatableDEResults.get(strDEOrder);
            for (int index=0; index<vtRepeatableDEData.size(); index++)
            {    
                String strDate = "";
                String strResult = "";

                if (runtimeData.getParameter("Dated" + strDEOrder + "_" + Integer.toString(index)) != null)
                {
                    strDate = runtimeData.getParameter("Dated" + strDEOrder + "_" + Integer.toString(index));
                }    

                if (runtimeData.getParameter("Datad" + strDEOrder + "_" + Integer.toString(index)) != null)
                {    
                    strResult = strDate + " " + runtimeData.getParameter("Datad" + strDEOrder + "_" + Integer.toString(index));
                }         


                vtRepeatableDEData.set(index, strResult);        
            }
            htRepeatableDEResults.put (strDEOrder, vtRepeatableDEData);        
        }
    } 
    
    // Processes the DE result from the Vector and converts it to a string to
    // be written to the database
    private void processRepeatableDEforDB() 
    {
        
        Enumeration enum = htRepeatableDEResults.keys();
        String strResult = "";
        
        while (enum.hasMoreElements())
        {
            String strDEOrder = enum.nextElement().toString();
            Vector vtRepeatableDEData = (Vector) htRepeatableDEResults.get(strDEOrder);
            for (int i=0; i<vtRepeatableDEData.size(); i++)
            {
                strResult += vtRepeatableDEData.get(i);
                if (i != vtRepeatableDEData.size()-1)
                {
                    strResult += ";";
                }    
            }   
            runtimeData.setParameter ("d" + strDEOrder, strResult);
        }    
        
    }       
    
}

