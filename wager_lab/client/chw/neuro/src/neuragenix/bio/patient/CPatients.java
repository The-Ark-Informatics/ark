/**
 * $Id :$
 * Copyright (c) 2005 Neuragenix Pty Ltd.
 *
 * @author  Long Tran, email: ltran@neuragenix.com
 *
 *
 * Description:
 *
 * Class for managing patients
 *
 *
 **/
 
package neuragenix.bio.patient;


// portal classes
import org.jasig.portal.IChannel;
import org.jasig.portal.PropertiesManager;
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


// java classes
import java.util.*;
import java.io.*;
import javax.swing.tree.*;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NotContextException;
import java.text.SimpleDateFormat;

// neuragenix classes
import neuragenix.dao.*;
import neuragenix.genix.config.SystemConfiguration;
import neuragenix.common.*;
import neuragenix.security.*;
import neuragenix.utils.*;
import neuragenix.bio.utilities.*;


/** Class for managing users and groups
 * @author Long Tran {@link mailto:ltran@neuragenix.com}
 * @version 1.0
 */

public class CPatients implements IChannel {
    // actions
    private static final String VIEW_PATIENT = "view_patient";
    private static final String SEARCH_PATIENT = "search_patient";
    private static final String SEARCH_RESULTS = "search_results";
    private static final String ADD_PATIENT = "add_patient";
    private static final String DELETE_PATIENT = "delete_patient";
    private static final String PATIENT_CONSENT = "view_patient";
    private static final String APPOINTMENTS = "appointments";
    private static final String ADMISSIONS = "admissions";
    private static final String ATTACHMENTS = "attachments";
    private static final String CONSENT = "consent";
    private static final String NOTES = "notes";
    private static final String CONSENT_FILE = "consent_file";
    private static final String DIAGNOSIS = "diagnosis";
    private static final String DIAGNOSIS_SELECTED = "diagnosisSelected";
    private static final String EDIT_ADMISSION = "patient_admissions_edit";
    private final String ACTIVITY_EDIT_DOB = "edit_patient_dob";
    // error msg
    private static final String FILE_SIZE_ERROR = "The file you are trying to upload has invalid size. Please try another file.";
    private static final String LOCK_ERROR = "The record you are trying to update or delete is being viewed by others. Please try again later.";
    private static final String SECURITY_ERROR = "You do not have permission to perform this activity.";
    private static final String COLLECTION_DELETE_ERROR = "The collection cannot be deleted because it is linked to a biospecimen.";
    
    private static final String NO_PERMISSION = "no_permission";
    
    // Following static final strings used to define the location of the system
    public static final String SYSTEM_TIMEZONE = "Australia/Melbourne";    
    // http://www.ics.uci.edu/pub/ietf/http/related/iso639.txt
    public static final String SYSTEM_LANGUAGE = "en";
    // http://www.chemie.fu-berlin.de/diverse/doc/ISO_3166.html 
    public static final String SYSTEM_COUNTRY = "AU";

    private  static String DEFAULT_STUDY = null;
    
    
    static {
        try {
            DEFAULT_STUDY = PropertiesManager.getProperty("neuragenix.bio.DefaultStudy");
        }
        catch (Exception e){
            System.err.print("Cant find properties \"neurogenix.bio.DefaultStudy\"");
        }
    }

    // data for rendering last page
    private StringBuffer strLastXML = null;
    private String strLastStylesheet;
    
    private String strSecurityError = null;
    private String strSessionUniqueID;
    
    
    // search details
    private DALSecurityQuery searchQuery;
    private Hashtable hsOrder;
    
    // authToken
    private AuthToken authToken;
    private LockRequest lock;
    
    // At the very least, you'll need to keep track of the static data and the runtime data that the portal sends you.
    private ChannelStaticData staticData;
    private ChannelRuntimeData runtimeData;
    private ChannelRuntimeData searchRuntimeData;
    private ChannelRuntimeData diagnosisRuntimeData;
    
    private StringBuffer strXML; // used to create the xml document with the starting XML header
    private String strStylesheet; // Used to specify the stylesheet to use for the portal
    
    private String downloadTab="";
    private String biospecimenTab="";
    private String studyTab="";
    private String smartformTab="";
    
    private String intStudyID = null;
    
    private String strIn="";
    private String strEditDiag="";
    
    private LinkedList backActions = new LinkedList();
    
    private Hashtable hashLockToClear = new Hashtable();
    
    // Properties
    private static boolean PROPERTY_ADMISSION_DATA = false;
    private static boolean PROPERTY_DIAGNOSIS_PICKER = false;
    private static boolean PROPERTY_HIGHLIGHT_MENU = false;
    private static boolean PROPERTY_BIOSPECIMEN_COLLECTION = false;
    private static boolean PROPERTY_PATIENT_ADMISSION_SMARTFORM = false;
    private static boolean PROPERTY_VIEW_ONLY_PATIENT_CONSENT_COMMENTS = false;
    private static boolean PROPERTY_USER_LEVEL_FLAGS=true;
    /**
     *
     *  Constructs the CPatients Channel
     *
     */
    
    public CPatients()
    {}
    
    static
    {
        /*============== Set the properties as defined in PropertiesManager============*/ 
        try
        {
            PROPERTY_ADMISSION_DATA = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.PatientAdmissionData");
        }
        catch(Exception e)
        {
            System.out.println ("[CPatient] Property neuragenix.bio.patient.PatientAdmissionData not present default to false.");
        }
        
        try
        {
            PROPERTY_USER_LEVEL_FLAGS = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.userLevelFlags");
        }
        catch(Exception e)
        {
            System.out.println ("[CPatient] Property neuragenix.bio.patient.userLevelFlags not present, default to true");
        }
        
        
        
        try
        {
            PROPERTY_DIAGNOSIS_PICKER = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.DiagnosisPicker");
        }
        catch(Exception e)
        {
            System.out.println ("[CPatient] Property neuragenix.bio.patient.DiagnosisPicker not present default to false.");
        }
        
        try
        {
            PROPERTY_HIGHLIGHT_MENU = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.HighlightMenu");
        }
        catch(Exception e)
        {
            System.out.println ("[CPatient] Property neuragenix.bio.patient.HighlightMenu not present default to false.");
        }
        
        try
        {
            PROPERTY_BIOSPECIMEN_COLLECTION = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.BiospecimenCollection");
        }
        catch(Exception e)
        {
            System.out.println ("[CPatient] Property neuragenix.bio.patient.BiospecimenCollection not present default to false.");
        }
        try
        {
            PROPERTY_PATIENT_ADMISSION_SMARTFORM = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.admission.smartforms");
        }
        catch(Exception e)
        {
            System.out.println ("[CPatient] Property neuragenix.bio.patient.admission.smartforms not present default to none.");
        }
        
        try
        {
            PROPERTY_VIEW_ONLY_PATIENT_CONSENT_COMMENTS = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.consent.viewPatientCommentsOnly");
        }
        catch(Exception e)
        {
            System.out.println ("[CPatient] Property neuragenix.bio.patient.consent.viewPatientCommentsOnly not present default to false.");
        }
    }
    
    
    
    /**
     *
     *  Returns channel runtime properties.
     *
     *  Satisfies implementation of Channel Interface.
     *
     *
     *
     *  @return handle to runtime properties
     *
     */
    
    public ChannelRuntimeProperties getRuntimeProperties() {
        
        return new ChannelRuntimeProperties();
        
    }
    
    /**
     *  Process layout-level events coming from the portal.
     *
     *  Satisfies implementation of IChannel Interface.
     * @param ev ev a portal layout event
     */
    
    public void receiveEvent(PortalEvent ev) {
        
        // If the user logout, destroy lock request object
        if (ev.getEventNumber() == PortalEvent.SESSION_DONE){
            try{
                
                if(lock!=null) lock.unlock();
                
                lock = null;
                
            }catch(Exception e){
                
                e.printStackTrace();
                
                LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
                
            }finally{
                lock = null;
            }
            
            
        }
    }
    
   
    /**
     *
     *  Receive static channel data from the portal.
     *
     *  Satisfies implementation of IChannel Interface.
     * @param sd sd static channel data
     */
    
    public void setStaticData(ChannelStaticData sd) {
        
        
        
        
        this.staticData = sd;
        
        this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");
        
        backActions = new LinkedList();
        
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
            
            searchRuntimeData = new ChannelRuntimeData();
            searchRuntimeData.setParameter( "PATIENT_dtDob", "" );
            
            
            strSessionUniqueID = authToken.getSessionUniqueID();
            SessionManager.addSession(strSessionUniqueID);
            
            hashLockToClear = SessionManager.getSharedChannels( authToken, "CPatient" );
            
            
            SessionManager.addChannelID(strSessionUniqueID, "CDownload",
                (String) globalIDContext.lookup("CDownload"));
            downloadTab = SessionManager.getTabOrder( authToken, "CDownload");
            
        
            SessionManager.addChannelID(strSessionUniqueID, "CBiospecimen",
                (String) globalIDContext.lookup("CBiospecimen"));
            biospecimenTab = SessionManager.getTabOrder( authToken, "CBiospecimen");
            
            SessionManager.addChannelID(strSessionUniqueID, "CStudy",
                (String) globalIDContext.lookup("CStudy"));
            studyTab = SessionManager.getTabOrder( authToken, "CStudy");
            
            SessionManager.addChannelID(strSessionUniqueID, "CSmartform",
                (String) globalIDContext.lookup("CSmartform"));
            smartformTab = SessionManager.getTabOrder( authToken, "CSmartform");
            
        }
        catch (NotContextException nce) {
            LogService.log(LogService.ERROR, "Could not find channel ID for fname=CDownload and CBiospeciment");
        }
        catch (NamingException e) {
            LogService.log(LogService.ERROR, e);
        }
        
        
        
    }
    
    /**
     *    Output channel content to the portal
     * @param out a sax document handler
     * @throws PortalException Portal Exception
     */
    
    public void renderXML(ContentHandler out) throws PortalException {
        
        
        // Create a new XSLT styling engine
        XSLT xslt = new XSLT(this);
        
        // Download tab details
        org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL( true ));//org.jasig.portal.UPFileSpec.FILE_DOWNLOAD_WORKER,true));        
        upfTmp.setTargetNodeId(SessionManager.getChannelID(authToken.getSessionUniqueID(), "CDownload"));
        
        xslt.setStylesheetParameter("downloadURL", upfTmp.getUPFile());
        xslt.setStylesheetParameter("downloadTab", downloadTab);
        if( SessionManager.getChannelID(authToken.getSessionUniqueID(), "CDownload") != null )
            xslt.setStylesheetParameter("downloadNodeId", SessionManager.getChannelID(authToken.getSessionUniqueID(), "CDownload"));
        
        
        upfTmp = null;
        upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL( false ));//org.jasig.portal.UPFileSpec.FILE_DOWNLOAD_WORKER,true));        
        upfTmp.setTargetNodeId(SessionManager.getChannelID(authToken.getSessionUniqueID(), "CBiospecimen"));
        
        xslt.setStylesheetParameter("biospecimenURL", upfTmp.getUPFile());
        xslt.setStylesheetParameter("biospecimenTab", biospecimenTab);
        
        upfTmp.setTargetNodeId(SessionManager.getChannelID(authToken.getSessionUniqueID(), "CStudy"));
        
        xslt.setStylesheetParameter("studyURL", upfTmp.getUPFile());
        xslt.setStylesheetParameter("studyTab", studyTab);
        
        upfTmp = null;
        upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL( false ));//org.jasig.portal.UPFileSpec.FILE_DOWNLOAD_WORKER,true));        
        upfTmp.setTargetNodeId(SessionManager.getChannelID(authToken.getSessionUniqueID(), "CSmartform"));
        
        xslt.setStylesheetParameter("smartformURL", upfTmp.getUPFile());
        xslt.setStylesheetParameter("smartformTab", smartformTab);
        
        // System.out.println ("[CPatient] strXML = " + strXML);
        // pass the result xml to the styling engine.
        xslt.setXML("<?xml version=\"1.0\" encoding=\"utf-8\"?><body>" + strXML.toString() + "</body>");
        
        
        // specify the stylesheet selector
        xslt.setXSL("CPatients.ssl", strStylesheet, runtimeData.getBrowserInfo());
        
        // set parameters that the stylesheet needs.
        xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL());
        


		
        
        // set the output Handler for the output.
        xslt.setTarget(out);
        
        // do the deed
        xslt.transform();
        
    }
    
    /**
     *  Receive channel runtime data from the portal.
     *
     *  Satisfies implementation of IChannel Interface.
     * @param rd rd handle to channel runtime data
     */
    
    public void setRuntimeData(ChannelRuntimeData rd) {
        
        strXML = new StringBuffer();
        
        
        try{
            
            this.runtimeData = rd;
            
            if( !authToken.hasActivity("patient_search") ){
                
                // build the LOVs.
                this.strStylesheet = NO_PERMISSION;
                
                return ;
                
            }
            
            SessionManager.clearLockRequests( strSessionUniqueID, hashLockToClear );
            if( lock == null )
                lock = new LockRequest( authToken );
            
            
            strXML = new StringBuffer();
            
            if( (runtimeData.getParameter( "action" ) != null) || ( (runtimeData.getParameter( "action" ) == null) &&
                (backActions.size() > 0 )) ){
                    
                String action = "";
                if( (runtimeData.getParameter("back") != null ) || (runtimeData.getParameter( "action" ) == null) ){
                    
                    if(runtimeData.getParameter("back") != null ){
                        if( backActions.size() > 1 ){
                            action = (String) backActions.removeLast();
                            action = (String) backActions.removeLast();
                        }else{
                            action = SEARCH_PATIENT;
                        }
                    }else{
                        if( (backActions.size() > 1) && (runtimeData.getParameter("PATIENT_intInternalPatientID") != null ) ){
                            action = (String) backActions.removeLast();
                        }else{
                            action = SEARCH_PATIENT;
                        }
                    }
                    
                }else{
                    
                        action = runtimeData.getParameter( "action" );
                    
                }
                
                if( action.equals( SEARCH_PATIENT )){
                    backActions = new LinkedList();
                    runtimeData.setParameter( "submit", "true" );
                }
                
                backActions.add( action );
                strXML.append("<action>action=" + action + "</action>" );
                
                runtimeData.setParameter( "action", action );
                
                // if a restricted user disable search fields (field-level security)
                strXML.append(disablePatientMenuFields (authToken));
                
                if( runtimeData.getParameter( "action" ).equals( SEARCH_PATIENT ))
                    strXML.append( doSearchPatient() );
                else if( runtimeData.getParameter( "action" ).equals( ADD_PATIENT ))
                    strXML.append( doAddPatient() );
                else if( runtimeData.getParameter( "action" ).equals( VIEW_PATIENT ))
                    strXML.append( doViewPatient() );
                else if( runtimeData.getParameter( "action" ).equals( APPOINTMENTS ))
                    strXML.append( doAppointments() );
                else if( runtimeData.getParameter( "action" ).equals( ATTACHMENTS ))
                    strXML.append( doAttachments() );
                else if( runtimeData.getParameter( "action" ).equals( DELETE_PATIENT ))
                    strXML.append( doDeletePatient() );
                else if( runtimeData.getParameter( "action" ).equals( CONSENT ))
                    strXML.append( doConsent() );
                else if( runtimeData.getParameter( "action" ).equals( ADMISSIONS ))
                    strXML.append( doAdmissions() );
                else if( runtimeData.getParameter( "action" ).equals( DIAGNOSIS ))
                    strXML.append( doDiagnosis() );
                else if( runtimeData.getParameter( "action" ).equals( DIAGNOSIS_SELECTED ))
                    strXML.append( doAdmissions() );                                
                else if( runtimeData.getParameter( "action" ).equals( NOTES ))
                    strXML.append( doNotes() );
                else if( runtimeData.getParameter( "action" ).equals( "nextPatient" ))
                    strXML.append( doChangePatient( true ) );
                else if( runtimeData.getParameter( "action" ).equals( "previousPatient" ))
                    strXML.append( doChangePatient( false ) );
                else
                    strXML.append( doSearchPatient() );
                
                
            } else {
                // if a restricted user disable search fields (field-level security)
                strXML.append(disablePatientMenuFields (authToken));
                
                strXML.append( doSearchPatient() );
   
            }
            
            if (PROPERTY_HIGHLIGHT_MENU)
            {    
                strXML.append(setHighlightMenus());
            }
            
            strXML.append( doListFlaggedRecords() );
            Vector formfields = DatabaseSchema.getFormFields( "cpatient_view_patient_details" );
           
            strXML.append( "<search>" + QueryChannel.buildViewXMLFile( formfields, searchRuntimeData ) + 
                            QueryChannel.buildFormLabelXMLFile(formfields) + "</search>" );
            
            SessionManager.addLockRequestObject( strSessionUniqueID, "CPatient", lock );
            lock.lockDelayWrite();
            
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
            
        }
        
    }
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doListFlaggedRecords(){
        
        StringBuffer strXML = new StringBuffer();
                
        try{
            
            Vector formfields = DatabaseSchema.getFormFields( "cpatient_view_flagged_patients" );
            
            DALQuery query = new DALQuery();
            query.setDomain( "PATIENT", null, null, null );
            query.setDomain( "FLAG", "PATIENT_intInternalPatientID", "FLAG_intID", "INNER JOIN" );
            query.setFields( formfields, null );

            query.setWhere( null, 0, "PATIENT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            query.setWhere( "AND", 0, "FLAG_strFlagDomain", "=", "Patient", 0, DALQuery.WHERE_HAS_VALUE );
            query.setWhere( "AND", 0, "FLAG_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            query.setOrderBy( "PATIENT_strPatientID", "ASC" );
            
            
                
            ResultSet rs = query.executeSelect();
            strXML.append( QueryChannel.buildSearchXMLFile( "patient", rs, formfields ));
            rs.close();
            rs = null;
        }
        catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
            
        }
        
        return "<flags>" + strXML.toString() + "</flags>";
        
    }
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doChangePatient( boolean next ){
        
        StringBuffer strXML = new StringBuffer();
        String patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
                
        try{
            DALSecurityQuery query;
            
            if( searchQuery != null ){
                
                query = searchQuery;
                query.clearLimitOffset();
                
            }else{
                
                query = new DALSecurityQuery();
                query.setDomain( "PATIENT", null, null, null );
                query.setField( "PATIENT_intInternalPatientID", null );
                query.setWhere( null, 0, "PATIENT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                
            }
            
            ResultSet rs = query.executeSelect();
                
            while( rs.next() ){
                
                if( rs.getString( "PATIENT_intInternalPatientID").equals( patientID ))
                    break;
            }
            if( next ){
                if( rs.next() ){
                    patientID = rs.getString( "PATIENT_intInternalPatientID" );
                    
                }
            }else{
                if( rs.previous() ){
                    patientID = rs.getString( "PATIENT_intInternalPatientID" );
                    
                }
            }

            rs.close();
            rs = null;
            
            runtimeData.setParameter( "PATIENT_intInternalPatientID", patientID );
            return doViewPatient();
                        
        }
        catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
            
        }
        
        return "<patient>" + strXML.toString() + "</patient>";
        
    }
    
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doSearchPatient(){
        
        StringBuffer strXML = new StringBuffer();
        
        try{
            
            // build the labels.
            Vector formfields = DatabaseSchema.getFormFields( "cpatient_view_search_details" );
            strXML.append( QueryChannel.buildFormLabelXMLFile( formfields ));
            
            
            // check for permission
            
            
            
            if( !authToken.hasActivity("patient_search") ){
                
                // build the LOVs.
                strXML.append( QueryChannel.buildAddFormXMLFile( formfields ));
                strXML.append( "<error>" + SECURITY_ERROR + "</error>" );
                this.strStylesheet = SEARCH_PATIENT;
                
                
                return "<patient>" + strXML.toString() + "</patient>";
                
            }
            
            // reset the lock;
            resetLock();
            
            if( ((runtimeData.getParameter( "submit" ) == null ) || (searchQuery == null)) && 
                (runtimeData.getParameter("newSearch") == null)){
                
                // build the LOVs.
                //strXML.append( QueryChannel.buildAddFormXMLFile( formfields ));
                if ( (searchRuntimeData == null) || (runtimeData.getParameter( "back") == null)){
                    searchRuntimeData = new ChannelRuntimeData();
                    searchRuntimeData.setParameter( "PATIENT_dtDob", "" );
                    
                }
                
                
                //strXML.append( QueryChannel.buildViewXMLFile( formfields, searchRuntimeData ));
                this.strStylesheet = SEARCH_PATIENT;
                
                if( strSecurityError != null ){
                    strXML.append( "<error>" + strSecurityError + "</error>" );
                    strSecurityError = null;
                }
                
                
                //if( runtimeData.getParameter( "back" ) != null )
                //    strXML.append( "<back>true</back>" );
                return "<patient>" + strXML.toString() + "</patient>";
                
                
            }else{
                
                                
                String noOfRecords = PropertiesManager.getProperty("neuragenix.platformgenix.RecordPerPage");

                if( (runtimeData.getParameter( "noOfRecords") != null ) && 
                    isNumeric( runtimeData.getParameter( "noOfRecords"))){
                    noOfRecords = runtimeData.getParameter( "noOfRecords");
                }

                int intNoOfRecords = Integer.parseInt( noOfRecords );
                int noOfPages = 0;
                int currentPage = 1;
                
                if( (runtimeData.getParameter("newSearch") != null ) || (searchQuery == null) ){
                    
                    searchQuery = null;
                    searchQuery = new DALSecurityQuery( "patient_search", authToken );

                    searchQuery.setDomain( "PATIENT", null, null, null );
                    // add the admissions domains for CCIA
                    searchQuery.setFields( formfields, null );

                  // if( runtimeData.getParameter( "setOrderBy" ) != null )
                  //     searchQuery.setOrderBy( runtimeData.getParameter( "setOrderBy" ) );

                    // build the criteria
                    searchQuery.setWhere( null, 0, "PATIENT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    
                    searchQuery.setCaseSensitive( false );

                    if( runtimeData.getParameter( "PATIENT_strPatientID" ) != null && runtimeData.getParameter( "PATIENT_strPatientID" ).trim().length() > 0 )
                    {
                        searchQuery.setWhere( "AND", 0, "PATIENT_strPatientID", "LIKE",
                        runtimeData.getParameter( "PATIENT_strPatientID" ).trim(), 0, DALQuery.WHERE_HAS_VALUE );
                    }

                    if( runtimeData.getParameter( "PATIENT_strFirstName" ) != null && runtimeData.getParameter( "PATIENT_strFirstName" ).trim().length() > 0 ){
                        searchQuery.setWhere( "AND", 0, "PATIENT_strFirstName", "LIKE",
                        runtimeData.getParameter( "PATIENT_strFirstName" ).trim(), 0, DALQuery.WHERE_HAS_VALUE );
                    }

                    if( runtimeData.getParameter( "PATIENT_strSurname" ) != null && runtimeData.getParameter( "PATIENT_strSurname" ).trim().length() > 0 ){
                        searchQuery.setWhere( "AND", 0, "PATIENT_strSurname", "LIKE",
                        runtimeData.getParameter( "PATIENT_strSurname" ).trim(), 0, DALQuery.WHERE_HAS_VALUE );
                    }

                    if( runtimeData.getParameter( "PATIENT_strSpecies" ) != null && runtimeData.getParameter( "PATIENT_strSpecies" ).trim().length() > 0 ){
                        searchQuery.setWhere( "AND", 0, "PATIENT_strSpecies", "LIKE",
                        runtimeData.getParameter( "PATIENT_strSpecies" ).trim(), 0, DALQuery.WHERE_HAS_VALUE );
                    }

                    
                    
                    // Sep 12 - 2005 : Daniel Murley
                    // Removed admissions dependancy
                    // This was a client customisation that shouldn't have been in core.
                    
                    if( runtimeData.getParameter( "PATIENT_strHospitalUR" ) != null && runtimeData.getParameter( "PATIENT_strHospitalUR" ).trim().length() > 0 ){
                        searchQuery.setWhere( "AND", 0, "PATIENT_strHospitalUR", "LIKE",
                        runtimeData.getParameter( "PATIENT_strHospitalUR" ).trim(), 0, DALQuery.WHERE_HAS_VALUE );
                    }

                    if( runtimeData.getParameter( "PATIENT_strProtocol" ) != null && runtimeData.getParameter( "PATIENT_strProtocol" ).trim().length() > 0 ){
                        searchQuery.setWhere( "AND", 0, "PATIENT_strProtocol", "LIKE",
                        runtimeData.getParameter( "PATIENT_strProtocol" ).trim(), 0, DALQuery.WHERE_HAS_VALUE );
                    }
                    
                    if( ( runtimeData.getParameter( "PATIENT_dtDob_Day" ) != null && runtimeData.getParameter( "PATIENT_dtDob_Day").trim().length() == 2)
                        && runtimeData.getParameter( "PATIENT_dtDob_Month" ) != null && ( runtimeData.getParameter( "PATIENT_dtDob_Month").trim().length() == 2)
                        && runtimeData.getParameter( "PATIENT_dtDob_Year" ) != null && ( runtimeData.getParameter( "PATIENT_dtDob_Year").trim().length() == 4)
                        && ( isNumeric( runtimeData.getParameter( "PATIENT_dtDob_Year") ) ) ) {

                        runtimeData.setParameter( "PATIENT_dtDob", QueryChannel.makeDateFromForm(
                                "PATIENT_dtDob", runtimeData ));
                        searchQuery.setWhere( "AND", 0, "PATIENT_dtDob", "=",
                        runtimeData.getParameter( "PATIENT_dtDob" ).trim(), 0, DALQuery.WHERE_HAS_VALUE );
                    }
                    
                    if(runtimeData.getParameter( "intStudyID" ) != null && runtimeData.getParameter( "intStudyID" ).trim().length() > 0)
                    {
                        intStudyID = runtimeData.getParameter("intStudyID");
                        searchQuery.setDomain( "CONSENT", "PATIENT_intInternalPatientID",
                            "CONSENT_intInternalPatientID", "INNER JOIN");
                        searchQuery.setDomain( "CONSENTSTUDY", "CONSENT_intConsentKey",
                            "CONSENTSTUDY_intConsentKey", "LEFT JOIN");
                        searchQuery.setWhere( "AND", 0, "CONSENTSTUDY_intStudyID", "=",runtimeData.getParameter("intStudyID"), 0, DALQuery.WHERE_HAS_VALUE );
                        searchQuery.setWhere( "AND", 0, "CONSENT_intDeleted", "=","0", 0, DALQuery.WHERE_HAS_VALUE );
                        searchQuery.setWhere( "AND", 0, "CONSENTSTUDY_intDeleted", "=","0", 0, DALQuery.WHERE_HAS_VALUE );
                        strXML.append( "<blBackStudyButton>" + "true" + "</blBackStudyButton>" );
                        strXML.append( "<intStudyID>" + runtimeData.getParameter("intStudyID") + "</intStudyID>" );
                    }
                    

                    if (PROPERTY_ADMISSION_DATA)
                    {    
                        // The search result should only contain the earliest diagnosis date for the patient
                        DALQuery subQuery = new DALQuery();
                        subQuery.setDomain( "ADMISSIONS", null, null, null );
                        subQuery.setMinField ("ADMISSIONS_intAdmissionkey");
                        subQuery.setGroupBy ("ADMISSIONS_intPatientID");                    
                        subQuery.setWhere ( null, 0, "ADMISSIONS_intDeleted", "=","0", 0, DALQuery.WHERE_HAS_VALUE );


                        // Left join with the earliest admission of a patient.
                        DALQuery joinQuery = new DALQuery();
                        joinQuery.setDomain( "ADMISSIONS", null, null, null );
                        joinQuery.setField ("ADMISSIONS_intPatientID", null);
                        joinQuery.setField ("ADMISSIONS_strDiagCategory", null);
                        joinQuery.setField ("ADMISSIONS_dtDiagnosis", null);
                        joinQuery.setField ("ADMISSIONS_strStage", null);                                        
                        joinQuery.setField ("ADMISSIONS_strComments", null);
                        joinQuery.setWhere ( null, 0, "ADMISSIONS_intAdmissionkey", "IN", subQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY );
                        searchQuery.setSubQuery( joinQuery, "ix_admissions", "PATIENT_intInternalPatientID", "ADMISSIONS_intPatientID", "LEFT JOIN" );
                    }
                    hsOrder = null;
                    hsOrder = new Hashtable();
                    
                    // define the ordering
                    searchQuery.setOrderBy("PATIENT_intInternalPatientID", "ASC");
                    
                    searchRuntimeData = (ChannelRuntimeData) runtimeData.clone();
                    if(searchRuntimeData.getParameter( "PATIENT_dtDob") ==  null )
                        searchRuntimeData.setParameter( "PATIENT_dtDob", "" );
                    
                }else{
                    
                    hsOrder = null;
                    hsOrder = new Hashtable();
                    
                    if( runtimeData.getParameter( "orderBy") != null ){
                        
                        if( !hsOrder.containsKey( runtimeData.getParameter("orderBy") ) ){
                            hsOrder.remove( runtimeData.getParameter("orderBy") );
                            
                        }
                        hsOrder.put( runtimeData.getParameter("orderBy"), runtimeData.getParameter("order") );
                        
                        
                        searchQuery.clearOrderBy();
                        
                        Enumeration myEnum =hsOrder.keys();
                        while( myEnum.hasMoreElements() ){
                            String orderBy = (String) myEnum.nextElement();
                            searchQuery.setOrderBy( orderBy , (String) hsOrder.get( orderBy ));
                            
                            strXML.append( "<" + orderBy + "_Order>" + (String) hsOrder.get( orderBy ) + "</" + orderBy + "_Order>");
                        }
                        
                    }
                    
                }
                    
                if( ( runtimeData.getParameter( "currentPage") != null ) &&
                    isNumeric( runtimeData.getParameter( "currentPage"))){
                    currentPage = Integer.parseInt( runtimeData.getParameter( "currentPage") );
                }

                searchQuery.clearLimitOffset();
                
                ResultSet tRS = searchQuery.executeSelect();
                tRS.last();
                int allRecords = tRS.getRow();
                tRS.close();
                tRS = null;
                
                noOfPages = ( (allRecords - 1) / intNoOfRecords ) + 1;
                
                if( currentPage < 1 ) currentPage =1;
                if( currentPage > noOfPages ) currentPage = noOfPages;

                searchQuery.setLimitOffset( intNoOfRecords, ((currentPage - 1) * intNoOfRecords) );
                //searchQuery.setLimitOffset( intNoOfRecords, ((currentPage - 1) * intNoOfRecords), allRecords);    
                
                // searchQuery.setPagingKey("PATIENT_intInternalPatientID");
                
                
                strXML.append( "<currentPage>" + currentPage + "</currentPage>" );
                strXML.append( "<noOfPages>" + noOfPages + "</noOfPages>" );
                strXML.append( "<noOfRecords>" + noOfRecords + "</noOfRecords>" );
                strXML.append( "<noOfResults>" + allRecords + "</noOfResults>" );
                
                //System.out.println ("Query : " + searchQuery.convertSelectQueryToString());
                
                
                ResultSet rs = searchQuery.executeSelect();
//               strXML.append( QueryChannel.buildFormLabelXMLFile( formfields ));
                strXML.append( QueryChannel.buildSearchXMLFileAndLockRecord( "patient", rs, formfields, 
                    "PATIENT", "PATIENT_intInternalPatientID", lock, LockRecord.READ_ONLY ,authToken));
                //strXML.append( QueryChannel.buildSearchXMLFile( "patient", rs, formfields ) );
                rs.close();
                rs = null;
                
                this.strStylesheet = SEARCH_RESULTS;
                if( strSecurityError != null ){
                    strXML.append( "<error>" + strSecurityError + "</error>" );
                    strSecurityError = null;
                }
                //LogService.instance().log(LogService.ERROR, "CPatients::doSearchPatient:XML = " + strXML.toString());
                return "<results>" + strXML.toString() + "</results>";
                
            }
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
            
        }
        
        
        return "";
        
    }
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doDeletePatient(){
        
        StringBuffer strXML = new StringBuffer();
        String patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
        
        try{
            
            Vector formfields = DatabaseSchema.getFormFields( "cpatient_view_patient_details" );
            
            if( patientID != null ){
                
                // check for permision
                if( !authToken.hasActivity( "patient_delete" )){
                    strSecurityError = SECURITY_ERROR;
                    return doViewPatient();
                }
                
                String strError = null;
                
                DALSecurityQuery query = new DALSecurityQuery( "patient_delete", authToken);
                query.setDomain( "PATIENT", null, null, null );
                
                query.setField( "PATIENT_intDeleted", "-1" );
                query.setWhere( null, 0, "PATIENT_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE );
                
                if( lock.isValid() && lock.lockWrites() ){
                    query.executeUpdate();
                    lock.unlockWrites();
                    
                }else{
                    strError = LOCK_ERROR;
                }
                
                
                if( strError != null ){
                    
                    strXML.append( QueryChannel.buildFormLabelXMLFile( formfields ));
                    
                    strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields,
                    "PATIENT_intInternalPatientID", patientID,authToken ));
                    strXML.append( "<error>" + strError + "</error>" );
                    
                    this.strStylesheet = VIEW_PATIENT;
                    return "<patient>" + strXML.toString() + "</patient>";
                    
                }else{
                    
                    return doSearchPatient();
                    
                }
            }
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
            
        }
        
        return "<patient>" + strXML.toString() + "</patient>";
        
    }
    
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doViewPatient(){
        
        StringBuffer strXML = new StringBuffer();

        String patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
        
        try{
            
             Vector formfields = DatabaseSchema.getFormFields( "cpatient_view_patient_details" );
             //Vector formForSearchByHr = DatabaseSchema.getFormFields( "cpatient_search_by_hr" );
             
            // check for permision
            if( !authToken.hasActivity( "patient_view" )){
                runtimeData.setParameter( "submit", "true" );
                strSecurityError = SECURITY_ERROR;
                return doSearchPatient();
            }
           
            if( patientID != null ){
                
                String strError = null;
                
                strXML.append( QueryChannel.buildFormLabelXMLFile( formfields ));
                //strXML.append( QueryChannel.buildFormLabelXMLFile( DatabaseSchema.getFormFields( "cpatient_view_patient_admissions") ));
               
                if( runtimeData.getParameter( "save" ) == null ){
                    strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields,
                    "PATIENT_intInternalPatientID", patientID, authToken ));
                    //strXML.append( QueryChannel.buildViewFromKeyXMLFile( formForSearchByHr,
                    //"PATIENT_intInternalPatientID", patientID ,authToken));
                    // check for flagged

                    DALSecurityQuery query = new DALSecurityQuery( "patient_view", authToken);
                    query.setDomain( "FLAG", null, null, null );
                    query.setField( "FLAG_intFlagKey", null );
                    query.setWhere( null, 0, "FLAG_strFlagDomain", "=", "Patient", 0, DALQuery.WHERE_HAS_VALUE );
                    if (PROPERTY_USER_LEVEL_FLAGS == true)
                    {
                       query.setWhere( "AND", 0, "FLAG_strUser", "=", authToken.getUserIdentifier() , 0, DALQuery.WHERE_HAS_VALUE );
                    }
                    query.setWhere( "AND", 0, "FLAG_intID", "=", patientID , 0, DALQuery.WHERE_HAS_VALUE );
                    query.setWhere( "AND", 0, "FLAG_intDeleted", "=", "0" , 0, DALQuery.WHERE_HAS_VALUE );
                    ResultSet rs = query.executeSelect();
                    
                    if( rs.next() ){
                        strXML.append( "<flagged>true</flagged>" );

                    }

                    if (authToken.hasActivity(ACTIVITY_EDIT_DOB))
                    {
                        strXML.append("<dob_editable>1</dob_editable>");
                    }
                    rs.close();
                    rs = null;
                    
                    resetLock();
                    lock.addLock( "PATIENT", patientID, LockRecord.READ_WRITE );
                  
                }else
                {
                        runtimeData.setParameter( "PATIENT_dtDob", QueryChannel.makeDateFromForm( "PATIENT_dtDob", runtimeData ));
                        runtimeData.setParameter( "PATIENT_dtDateOfDeath", QueryChannel.makeDateFromForm( "PATIENT_dtDateOfDeath", runtimeData ));
                        //during update to remove any data that is hashed out.
                        Vector formfieldsecurity = new Vector(formfields);
                        formfieldsecurity = QueryChannel.prepareFormfieldsForFieldSecurity(formfieldsecurity, authToken);
                    // check for permision
                    if( !authToken.hasActivity( "patient_save" )){
                        
                        strError = SECURITY_ERROR;
                        
                    }else if( (( strError = QueryChannel.checkRequiredFields( formfieldsecurity, runtimeData )) == null)
                    && (( strError = QueryChannel.validateData( formfieldsecurity, runtimeData )) == null)
                    && (( strError = QueryChannel.checkDuplicatesWhenUpdate( formfieldsecurity, runtimeData,  "PATIENT_intInternalPatientID",  patientID )) == null ) ){
                                                
                        String name;

                        // Display the first name in surname in upper case
                        if (runtimeData.getParameter("PATIENT_strFirstName")!= null)
                        {
                            name = runtimeData.getParameter("PATIENT_strFirstName");
                            //if(PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.displayNameInUpperCase"))
                            // Checks property to see whether name is to be displayed in upper case
                            if(PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.displayNameInUpperCase"))
                            {
                                runtimeData.setParameter("PATIENT_strFirstName", name.toUpperCase());
                            }
                            else
                            {
                                runtimeData.setParameter("PATIENT_strFirstName", name);
                            }
                            
                        }    

                        if (runtimeData.getParameter("PATIENT_strSurname") != null)
                        {    
                            name = runtimeData.getParameter("PATIENT_strSurname");
                            
                            // Checks property to see whether name is to be displayed in upper case
                            if(PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.displaySurnameInUpperCase"))
                            {
                                runtimeData.setParameter("PATIENT_strSurname", name.toUpperCase());
                            }
                            else
                            {
                                runtimeData.setParameter("PATIENT_strSurname", name);
                            }
                        }

                        // Update the last modified user
                        runtimeData.setParameter("PATIENT_strUser", authToken.getUserIdentifier()); 

                        DALSecurityQuery query = new DALSecurityQuery( "patient_save", authToken);

                        formfieldsecurity = new Vector(DatabaseSchema.getFormFields( "cpatient_add_patient" ));
                        query.setDomain( "PATIENT", null, null, null );
                        //introduced to remove the hashed data for field level security
                        formfieldsecurity = QueryChannel.prepareFormfieldsForFieldSecurity(formfieldsecurity, authToken);
                        query.setFields( formfieldsecurity, runtimeData );
                        query.setWhere( null, 0, "PATIENT_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE );
                        
                        if( lock.isValid() && lock.lockWrites() ){
                            query.executeUpdate();
                            lock.unlockWrites();
                            
                            query.reset();
                            query.setDomain( "FLAG", null, null, null );
                            if( runtimeData.getParameter("PATIENT_intFlag") != null ){
                                query.setField( "FLAG_intDeleted", "0" );
                                query.setWhere( null, 0, "FLAG_strFlagDomain", "=", "Patient", 0, DALQuery.WHERE_HAS_VALUE );
                                if (PROPERTY_USER_LEVEL_FLAGS == true)
                                {
                                   query.setWhere( "AND", 0, "FLAG_strUser", "=", authToken.getUserIdentifier() , 0, DALQuery.WHERE_HAS_VALUE );
                                }
                                query.setWhere( "AND", 0, "FLAG_intID", "=", patientID , 0, DALQuery.WHERE_HAS_VALUE );
                                
                                ResultSet rs = query.executeSelect();
                                boolean hasEntries = rs.next();
                                rs.close();
                                rs = null;
                                if( !hasEntries ){
                                    
                                    query.setField( "FLAG_strFlagDomain", "Patient" );
                                    query.setField( "FLAG_intID", patientID );
                                    if (PROPERTY_USER_LEVEL_FLAGS == true)
                                    {
                                       query.setField( "FLAG_strUser", authToken.getUserIdentifier() );
                                    }
                                    query.executeInsert();
                                    
                                }else{
                                    query.executeUpdate();
                                }
                                                                
                            }else{
                                query.setField( "FLAG_intDeleted", "-1" );
                                query.setWhere( null, 0, "FLAG_strFlagDomain", "=", "Patient", 0, DALQuery.WHERE_HAS_VALUE );
                                if (PROPERTY_USER_LEVEL_FLAGS == true)
                                {
                                   query.setWhere( "AND", 0, "FLAG_strUser", "=", authToken.getUserIdentifier() , 0, DALQuery.WHERE_HAS_VALUE );
                                }
                                query.setWhere( "AND", 0, "FLAG_intID", "=", patientID , 0, DALQuery.WHERE_HAS_VALUE );
                                query.executeUpdate();
                    
                                
                            }
                            if(formfieldsecurity.size() < DatabaseSchema.getFormFields( "cpatient_view_patient" ).size())
                            {
                                strError = "Details in fields other than the restricted fields are updated";
                            }
                            else
                            {
                                strError = "Details updated.";
                            }
                        }else{
                            strError = LOCK_ERROR;
                        }
                        
                    }
                    
                    // strXML.append( QueryChannel.buildViewXMLFile( formfields, runtimeData ));
                    
                    strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields,
                    "PATIENT_intInternalPatientID", patientID,authToken ));
                    
                    runtimeData.remove("PATIENT_intInternalPatientID");
                    runtimeData.setParameter("PATIENT_intInternalPatientID", patientID);
                    
                    // check for flagged
                    DALSecurityQuery query = new DALSecurityQuery( "patient_view", authToken);
                    query.setDomain( "FLAG", null, null, null );
                    query.setField( "FLAG_intFlagKey", null );
                    query.setWhere( null, 0, "FLAG_strFlagDomain", "=", "Patient", 0, DALQuery.WHERE_HAS_VALUE );
                    if (PROPERTY_USER_LEVEL_FLAGS == true)
                    {
                       query.setWhere( "AND", 0, "FLAG_strUser", "=", authToken.getUserIdentifier() , 0, DALQuery.WHERE_HAS_VALUE );
                    }
                    query.setWhere( "AND", 0, "FLAG_intID", "=", patientID , 0, DALQuery.WHERE_HAS_VALUE );
                    query.setWhere( "AND", 0, "FLAG_intDeleted", "=", "0" , 0, DALQuery.WHERE_HAS_VALUE );
                    ResultSet rs = query.executeSelect();
                    
                    if( rs.next() ){
                        strXML.append( "<flagged>true</flagged>" );

                    }
                   
                    if (authToken.hasActivity(ACTIVITY_EDIT_DOB))
                    {
                        strXML.append("<dob_editable>1</dob_editable>");
                    }

                    rs.close();
                    rs = null;
                    
                }
                

                if (PROPERTY_ADMISSION_DATA)
                {    
                    // get the first admission date and diagnosis for the patient
                    DALSecurityQuery query1 = new DALSecurityQuery( "patient_view", authToken);
                    query1.setDomain( "ADMISSIONS", null, null, null );
                    query1.setField( "ADMISSIONS_dtDiagnosis", null );
                    query1.setField( "ADMISSIONS_strDiagCategory", null );
                    query1.setWhere( null, 0, "ADMISSIONS_intPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE );
                    query1.setWhere( "AND", 0, "ADMISSIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );

                    query1.setOrderBy( "ADMISSIONS_intAdmissionkey", "ASC");
                    ResultSet rs1 = query1.executeSelect();

                    DBField field = (DBField) DatabaseSchema.getFields().get("ADMISSIONS_dtDiagnosis");                    
                    strXML.append("<ADMISSIONS_dtDiagnosisDisplay>" + field.getLabelInForm() + "</ADMISSIONS_dtDiagnosisDisplay>");
                    field = (DBField) DatabaseSchema.getFields().get("ADMISSIONS_strDiagCategory");
                    strXML.append("<ADMISSIONS_strDiagCategoryDisplay>" +  field.getLabelInForm() + "</ADMISSIONS_strDiagCategoryDisplay>");


                    if (rs1.next())
                    {
                        java.sql.Date dtObject = rs1.getDate("ADMISSIONS_dtDiagnosis");
                        if (dtObject != null)
                        {    
                            strXML.append("<ADMISSIONS_dtDiagnosis>" + Utilities.convertDateForDisplay(dtObject) + "</ADMISSIONS_dtDiagnosis>");
                        }
                        String strAdmissionCategory = rs1.getString("ADMISSIONS_strDiagCategory");

                        if (strAdmissionCategory != null)
                        {
                            strXML.append("<ADMISSIONS_strDiagCategory>" + strAdmissionCategory + "</ADMISSIONS_strDiagCategory>");                    
                        }
                    }  

                    rs1.close();
                    rs1 = null;
                }
                
                if( strSecurityError != null ){
                    strXML.append( "<error>" + strSecurityError + "</error>" );
                    strSecurityError = null;
                }
                if( strError != null )
                    strXML.append("<error>" + strError + "</error>" );
                
                strXML.append( buildSmartformList( patientID ));
                
                this.strStylesheet = VIEW_PATIENT;
                return "<patient>" + strXML.toString() + "</patient>";
                
            }
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
            
        }
        
        
        return "<patient>" + strXML.toString() + "</patient>";
        
    }
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doAddPatient(){
        
        StringBuffer strXML = new StringBuffer();
        
        try{
            
            // build the labels.
            Vector formfields = DatabaseSchema.getFormFields( "cpatient_add_patient" );
            strXML.append( QueryChannel.buildFormLabelXMLFile( DatabaseSchema.getFormFields( "cpatient_view_patient" ) ));
            
            // check for permision
            if( !authToken.hasActivity( "patient_view" )){
                strSecurityError = SECURITY_ERROR;
                return doSearchPatient();
            }

            // Save button has been clicked
            if ( runtimeData.getParameter( "save" ) != null )
            {
                
                // check for permision
                if( !authToken.hasActivity( "patient_add" )){
                    strSecurityError = SECURITY_ERROR;
                    return doSearchPatient();
                }
                
                DALSecurityQuery query = new DALSecurityQuery( "patient_add", authToken );
                query.setDomain( "PATIENT", null, null, null );

                
                String strError = "";
                String strTitleError = "";
                
                // Title field not used by CCIA
                // check that the entered title and sex fields match correctly
                // Title Mrs or Ms incorrect for male
                if ((runtimeData.getParameter( "PATIENT_strSex")!= null) && (runtimeData.getParameter( "PATIENT_strTitle") != null))
                {    
                    if ( runtimeData.getParameter( "PATIENT_strSex").equalsIgnoreCase("male") && 
                    ((runtimeData.getParameter("PATIENT_strTitle").equalsIgnoreCase("Mrs")) || (runtimeData.getParameter( "PATIENT_strTitle").equalsIgnoreCase("Ms"))))
                    {
                        strTitleError = "Please check that the title and sex fields match.";
                    }
                    // Title Mr incorrect for female
                    if ( runtimeData.getParameter( "PATIENT_strSex").equalsIgnoreCase("female") && runtimeData.getParameter("PATIENT_strTitle").equalsIgnoreCase("Mr"))
                    {
                        strTitleError = "Please check that the title and sex fields match.";
                    }
                }


                // rebuild the format for QueryChannel to check.
                // TODO: error in rebuilding.
                runtimeData.setParameter( "PATIENT_dtDob", QueryChannel.makeDateFromForm( "PATIENT_dtDob", runtimeData) );
                runtimeData.setParameter( "PATIENT_dtDateOfDeath", QueryChannel.makeDateFromForm( "PATIENT_dtDateOfDeath", runtimeData ));
                

                //System.out.println ("strPatientID" + runtimeData.getParameter("PATIENT_strPatientID") );

                // check if everything is ok to add
                if( (( strError = QueryChannel.checkRequiredFields( formfields, runtimeData )) == null )
                  && (( strError = QueryChannel.validateData( formfields, runtimeData )) == null ) 
                      && (( strError = QueryChannel.checkDuplicates( formfields, runtimeData )) == null )
                        && (strTitleError.equals("")) ){
                    

                    // Generate the new patient ID and update runtime data
                    IPatientIDGenerator idgGenerator = IDGenerationFactory.getPatientIDGenerationInstance();

                    String strNewPatientID = idgGenerator.getPatientID(0, authToken);
                            
                    runtimeData.setParameter( "PATIENT_strPatientID", strNewPatientID);
                    String name;

                    // Display the first name in surname in upper case
                    if (runtimeData.getParameter("PATIENT_strFirstName")!= null)
                    {
                        name = runtimeData.getParameter("PATIENT_strFirstName");
                        
                        // Following line checks property so as to determine whether to convert
                        // first name to upper case or enter into database as is.
                        if(PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.displayNameInUpperCase"))
                            {
                                runtimeData.setParameter("PATIENT_strFirstName", name.toUpperCase());                   
                            }
                            else
                            {
                                runtimeData.setParameter("PATIENT_strFirstName", name);                                
                            }
                    }    

                    if (runtimeData.getParameter("PATIENT_strSurname") != null)
                    {    
                        name = runtimeData.getParameter("PATIENT_strSurname");
                        // Following line checks property so as to determine whether to convert
                        // surname to upper case or enter into database as is.
                        if(PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.displaySurnameInUpperCase"))
                            {
                                runtimeData.setParameter("PATIENT_strSurname", name.toUpperCase());                
                            }
                            else
                            {
                                runtimeData.setParameter("PATIENT_strSurname", name);                                
                            }
                    }
                    
                    // Update the last modified user
                    runtimeData.setParameter("PATIENT_strUser", authToken.getUserIdentifier()); 
                    
                    
                    query.setFields( DatabaseSchema.getFormFields( "cpatient_add_patient" ), runtimeData );
                                    
                    query.executeInsert();
                    String strNewInsertedPatientKey = QueryChannel.getNewestKeyAsString( query );

                    runtimeData.setParameter( "PATIENT_intInternalPatientID", strNewInsertedPatientKey);
                    
                    query = null;
                    query = new DALSecurityQuery( "patient_add", authToken );
                    query.setDomain( "CONSENT", null, null, null );
                    
                    query.setField("CONSENT_intInternalPatientID", strNewInsertedPatientKey );
                    query.setField( "CONSENT_strFutureStudy", "No" );
                    query.setField( "CONSENT_strContactOK", "No" );
                    
                    query.executeInsert();
                    
                    /********************************************************************************/
                    /*----- Set the default study consented to as defined in portal properties -----*/
                    /********************************************************************************/
                    boolean addDefaultStudy = true;
                    try
                    {
                        addDefaultStudy = PropertiesManager .getPropertyAsBoolean("neuragenix.bio.patient.AddDefaultStudy");
                    }
                    catch(Exception e)
                    {
                        System.out.println ("[CPatient::doAddPatient] Property neuragenix.bio.patient.AddDefaultStudy not present default to true.");
                    }
                                      
                    // Check if there is a default study to consent to
                    if ( (DEFAULT_STUDY != null) && (!DEFAULT_STUDY.equals("")) && addDefaultStudy )
                    {
                        // Get the newly inserted consent key
                        String strNewInsertedConsentKey = QueryChannel.getNewestKeyAsString( query );

                        // Get the study key of the default study
                        query.reset();
                        query.setDomain( "STUDY", null, null, null );
                        query.setField("STUDY_intStudyID", null);
                        query.setWhere( null, 0, "STUDY_strStudyName", "=", DEFAULT_STUDY, 0, DALQuery.WHERE_HAS_VALUE );
                        query.setWhere( "AND", 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );

                        ResultSet rs = query.executeSelect();
                        // Check that the study exists
                        if (rs.first() == false)
                           System.out.println ("[Patient Channel] : Unable to get study Key");
                        else
                        {    
                            String strStudyKey = rs.getString("STUDY_intStudyID");

                            // Set
                            query = null;
                            query = new DALSecurityQuery( "patient_add", authToken );
                            query.setDomain( "CONSENTSTUDY", null, null, null );
                            query.setField( "CONSENTSTUDY_intConsentKey", strNewInsertedConsentKey);
                            query.setField( "CONSENTSTUDY_intStudyID", strStudyKey );
                            query.setField( "CONSENTSTUDY_strApproved", "Yes");

                            // Get the current date as the approved date
                            // Define the Timezone
                            java.util.TimeZone tz = java.util.TimeZone.getTimeZone (SYSTEM_TIMEZONE);
                            // Define the locality information
                            java.util.Locale loc = new java.util.Locale (SYSTEM_LANGUAGE, SYSTEM_COUNTRY);
                            // Setup the calender
                            java.util.Calendar theCalendar = java.util.Calendar.getInstance(tz,loc);

                            String dateDay = "";
                            if ( (theCalendar.get (java.util.Calendar.DATE)) < 10)
                            {    
                                dateDay = "0" + Integer.toString(theCalendar.get (java.util.Calendar.DATE));
                            }
                            else 
                            {
                                dateDay = Integer.toString(theCalendar.get (java.util.Calendar.DATE));

                            }    

                            int month = (theCalendar.get (java.util.Calendar.MONTH) + 1);

                            String dateMonth = "";
                            if ( month < 10)
                            {    
                                dateMonth = "0" + Integer.toString(month);
                            }
                            else 
                            {
                                dateMonth = Integer.toString(month);                    
                            }    

                            String date = dateDay + "/" +
                                          dateMonth + "/" +
                                          Integer.toString(theCalendar.get (java.util.Calendar.YEAR));


                            query.setField( "CONSENTSTUDY_dtApprovedDate", date );

                            query.executeInsert();

                            // integerate with Smartforms.
                            query = null;
    //                        formfields = null;
                            query = new DALSecurityQuery( "patient_consent", authToken );
    //                        formfields = DatabaseSchema.getFormFields( "view_smartforms" );

                            query.setDomain("SMARTFORM", null, null, null);
                            query.setDomain("STUDYSURVEY", "SMARTFORM_intSmartformID", "STUDYSURVEY_intSurveyKey", "LEFT JOIN");
                            query.setField( "STUDYSURVEY_intSurveyKey",null);
                            query.setWhere(null,0,"STUDYSURVEY_intStudyID","=", strStudyKey,0,DALQuery.WHERE_HAS_VALUE);
                            query.setWhere("AND", 0, "STUDYSURVEY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                            query.setWhere("AND",0,"SMARTFORM_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);

                            // get the smartform list
                            ResultSet sfrs;
                            sfrs = query.executeSelect();

                            //  build the insert query

                            DALSecurityQuery insertQuery = new DALSecurityQuery( "patient_consent", authToken );
                            insertQuery.setDomain( "SMARTFORMPARTICIPANTS", null, null, null);

                            DALSecurityQuery insertPSQuery = new DALSecurityQuery( "patient_consent", authToken );
                            insertPSQuery.setDomain( "PATIENT_SMARTFORM", null, null, null);

                            // build the runtime for PATIENT_SMARTFORM
                            runtimeData.setParameter( "PATIENT_SMARTFORM_intInternalPatientID", strNewInsertedPatientKey );
                            runtimeData.setParameter( "PATIENT_SMARTFORM_intStudyID", strStudyKey );

                            // build the runtime data for SMARTFORMPARTICIPANTS
                            runtimeData.setParameter("SMARTFORMPARTICIPANTS_strDomain","Study");
                            runtimeData.setParameter("SMARTFORMPARTICIPANTS_intParticipantID", strNewInsertedPatientKey);
                            runtimeData.setParameter("SMARTFORMPARTICIPANTS_strSmartformStatus","Not started");
                            runtimeData.setParameter("SMARTFORMPARTICIPANTS_strAddedBy",authToken.getUserIdentifier());
                            runtimeData.setParameter("SMARTFORMPARTICIPANTS_dtDateAdded", Utilities.getDateTimeStampAsString( "dd/MM/yyyy"));


                            while(sfrs.next())
                            {
                                // insert SMARTFORMPARTICIPANTS
                                insertQuery.clearFields();
                                runtimeData.setParameter("SMARTFORMPARTICIPANTS_intSmartformID",sfrs.getString("STUDYSURVEY_intSurveyKey"));
                                insertQuery.setFields( DatabaseSchema.getFormFields( "cpatient_view_smartform_participant" ), runtimeData);
                                insertQuery.executeInsert();
                                String smartformparticipantKey = QueryChannel.getNewestKeyAsString(insertQuery);
                                // insert PATIENT_SMARTFORM
                                insertPSQuery.clearFields();
                                runtimeData.setParameter("PATIENT_SMARTFORM_intSmartformKey",smartformparticipantKey);
                                insertPSQuery.setFields( DatabaseSchema.getFormFields( "cpatient_view_patient_smartform" ), runtimeData);
                                insertPSQuery.executeInsert();

                            }
                            sfrs.close();
                            sfrs = null;
                        }
                        rs.close();
                        rs = null;
                    }    
            
                    // TODO: "submit" button may remain.
                    runtimeData.remove( "save" );

                    this.strStylesheet = VIEW_PATIENT;
                    return doViewPatient();                    

                }
                else
                {
                    
                    if (strError == null)
                    {
                        // Reset the strError so that null is not displayed
                        strError = "";
                    }    
                    
                    strXML.append( "<error>" + strError + "</error>" );
                    strXML.append ( "<titleError>" + strTitleError + "</titleError>" );
                    strXML.append( QueryChannel.buildViewXMLFile( DatabaseSchema.getFormFields( "cpatient_view_patient" ), runtimeData ));
                    
                    this.strStylesheet = ADD_PATIENT;
                    return "<patient>" + strXML.toString() + "</patient>";
                    
                }
                
            }
            else
            {
                
                // build the LOVs.
                strXML.append( QueryChannel.buildAddFormXMLFile( DatabaseSchema.getFormFields( "cpatient_view_patient" ) ));
                this.strStylesheet = ADD_PATIENT;
                
                return "<patient>" + strXML.toString() + "</patient>";
            }
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
            
        }
        
        
        return strXML.toString();
        
    }
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doAdmissions(){
        
        StringBuffer strXML = new StringBuffer();
        String strError = "";
        String patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
        
        try{
            
            if( patientID != null ){
                
                // check for permision
                if( !authToken.hasActivity( "patient_admissions" )){
                    strSecurityError = SECURITY_ERROR;
                    return doViewPatient();
                }
                
                Vector formfields;
                DALSecurityQuery query;
              
                formfields = null;
                formfields = DatabaseSchema.getFormFields( "cpatient_view_patient_admissions" );
                
                strXML.append( QueryChannel.buildFormLabelXMLFile(formfields));
                
                if( runtimeData.getParameter( "deleteAdmission" ) != null )
                {
                    // check for permision
                    if( !authToken.hasActivity( "patient_admissions_delete" )){
                        strError = SECURITY_ERROR;
                        strXML.append( QueryChannel.buildAddFormXMLFile( formfields ));
                    }
                    else
                    {

                        boolean isUpdateAllowed = true;
                        
                        if (PROPERTY_BIOSPECIMEN_COLLECTION)
                        {
                            // only allow deletion of admissions if
                            // -- No biospecimens are matched against them
                            // -- Collection mode isn't running

                            // get the defining permission

                            boolean blAllowDeleteWithSpecimens = false;
                            try
                            {
                               blAllowDeleteWithSpecimens = PropertiesManager.getPropertyAsBoolean("neuragenix.bio.patient.allowDeleteCollectionWithSpecimens");
                            }
                            catch (Exception e)
                            {
                               e.printStackTrace();
                            }

                            if (blAllowDeleteWithSpecimens == false) // need to check this patient has no specimens
                            {
                               String strPatientKey = runtimeData.getParameter("PATIENT_intInternalPatientID");

                               if (strPatientKey != null)
                               {
                                  // lookup the biospecimens for this collection
                                  String strCollectionNumber = runtimeData.getParameter("ADMISSIONS_strAdmissionID");

                                  if (strCollectionNumber != null)
                                  {
                                     DALQuery collectionQuery = new DALQuery();
                                     collectionQuery.setDomain("BIOSPECIMEN", null, null, null);
                                     collectionQuery.setCountField("BIOSPECIMEN_intBiospecimenID", false);
                                     collectionQuery.setWhere(null, 0, "BIOSPECIMEN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                                     collectionQuery.setWhere("AND", 0, "BIOSPECIMEN_strEncounter", "=", strCollectionNumber, 0, DALQuery.WHERE_HAS_VALUE);
                                     collectionQuery.setWhere("AND", 0, "BIOSPECIMEN_intPatientID", "=", strPatientKey, 0, DALQuery.WHERE_HAS_VALUE);
                                     ResultSet rs = collectionQuery.executeSelect();

                                     int countValue = 0;
                                     if (rs.first())
                                     {
                                        countValue = rs.getInt(1);
                                     }

                                     if (countValue > 0)
                                        isUpdateAllowed = false;


                                  }

                                 }
                                 else
                                 {
                                  System.err.println ("[Patient Channel] : Patient ID is unavailable in this context");
                                 }
                            }
                        
                        }    
                        
                        
                        if (isUpdateAllowed == true)               
                        {
                            String strPatientKey = runtimeData.getParameter("PATIENT_intInternalPatientID");
                            String strAdmissionKey = runtimeData.getParameter("ADMISSIONS_intAdmissionkey");
                            // possibly delete admission smartform results
                            doAdmissionSmartformsDelete(strPatientKey, strAdmissionKey);
                            
                            query = null;
                            query = new DALSecurityQuery("patient_admissions", authToken);

                            query.setDomain( "ADMISSIONS", null, null, null );
                            query.setField( "ADMISSIONS_intDeleted", "-1" );
                            query.setWhere( null, 0, "ADMISSIONS_intAdmissionkey", "=", strAdmissionKey, 0, DALQuery.WHERE_HAS_VALUE );


                            if( lock.isValid() && lock.lockWrites() )
                            {
                                query.executeUpdate();
                                lock.unlockWrites();

                                strError = "History deleted.";
                                

                            }else{
                                strError = LOCK_ERROR;
                            }
                        }
                        else
                        {
                            strError = COLLECTION_DELETE_ERROR;
                        }    
                    }    
                }
                
                
                
                if( runtimeData.getParameter( "addAdmission" ) != null )
                {
                    // check for permision
                    if( !authToken.hasActivity( "patient_admissions_add" )){
                        strError = SECURITY_ERROR;
                        strXML.append( QueryChannel.buildAddFormXMLFile( formfields ));
                    }
                    else
                    {
                        runtimeData.setParameter( "ADMISSIONS_dtAdmission",
                            QueryChannel.makeDateFromForm( "ADMISSIONS_dtAdmission", runtimeData ));
                        runtimeData.setParameter( "ADMISSIONS_dtDiagnosis",
                            QueryChannel.makeDateFromForm( "ADMISSIONS_dtDiagnosis", runtimeData ));
                        runtimeData.setParameter( "ADMISSIONS_dtRelapse",
                           QueryChannel.makeDateFromForm( "ADMISSIONS_dtRelapse", runtimeData ));
                        runtimeData.setParameter( "ADMISSIONS_dtEvent",
                           QueryChannel.makeDateFromForm( "ADMISSIONS_dtEvent", runtimeData ));
                       runtimeData.setParameter( "ADMISSIONS_dtDischarge",
                           QueryChannel.makeDateFromForm( "ADMISSIONS_dtDischarge", runtimeData ));
                       runtimeData.setParameter( "ADMISSIONS_dtSurgery",
                           QueryChannel.makeDateFromForm( "ADMISSIONS_dtSurgery", runtimeData ));
                        
                        query = null;                                     

                        if( ((strError = QueryChannel.checkRequiredFields( formfields, runtimeData )) == null ) && 
                             ((strError = QueryChannel.validateData( formfields, runtimeData )) == null ) )
                            {

                            if( lock.isValid() && lock.lockWrites() ){

                                runtimeData.setParameter("ADMISSIONS_strAdmissionID", "1");
                                runtimeData.setParameter("ADMISSIONS_intPatientAge", "1" );
                                query = new DALSecurityQuery("patient_admissions", authToken);
                                query.setDomain( "ADMISSIONS", null, null, null );
                                query.setFields( formfields, runtimeData );
                                query.executeInsert();

                                String newKey = QueryChannel.getNewestKeyAsString(query);
                                
                                // possibly add admission smartforms
                                doAdmissionSmartformsAdd(runtimeData.getParameter("PATIENT_intInternalPatientID"), newKey, 
                                    runtimeData.getParameter("ADMISSIONS_strCollectGrp"), runtimeData.getParameter("ADMISSIONS_strEpisodeNo"));
                                
                                query = null;
                                query = new DALSecurityQuery("patient_admissions", authToken);
                                query.setDomain("ADMISSIONS", null, null, null);
                                query.setWhere(null, 0, "ADMISSIONS_intAdmissionkey", "=", newKey, 0, DALQuery.WHERE_HAS_VALUE);
                                // Get the patient admission number
                                String admissionNumber = getPatientAdmissionNumber (patientID);
                                query.setField ("ADMISSIONS_strAdmissionID", admissionNumber);

                                // Get the patient age at surgery
                                int intAge = getPatientAge(patientID, newKey);
                                query.setField ("ADMISSIONS_intPatientAge", Integer.toString(intAge));


                                if (PROPERTY_DIAGNOSIS_PICKER)
                                {
                                    String diagnosisCategory="";
                                    String diagnosisList[] = runtimeData.getParameterValues("allDiagnosis");
                                    if( diagnosisList != null ){

                                        for( int i = 0; i < diagnosisList.length; i++ ){
                                            diagnosisCategory = diagnosisCategory + diagnosisList[i].trim() + "; ";
                                        }
                                    }

                                    query.setField ("ADMISSIONS_strDiagCategory",  diagnosisCategory );                            

                                    strIn = "";
                                    strEditDiag = "";
                                }    

                                query.executeUpdate();
                                lock.unlockWrites ();
                                strXML.append( QueryChannel.buildAddFormXMLFile( formfields ));
                                strError = "History added.";
                            }else{
                                strError = LOCK_ERROR;
                                strXML.append( QueryChannel.buildViewXMLFile(formfields, runtimeData ));
                            }
                        }else{                        
                            strXML.append( QueryChannel.buildViewXMLFile(formfields, runtimeData ));


                            if (PROPERTY_DIAGNOSIS_PICKER)
                            {
                                // Rebuild the diagnosis that was selected using the diagnosis picker
                                query = null;
                                query = new DALSecurityQuery("patient_admissions", authToken);
                                formfields = DatabaseSchema.getFormFields( "cpatient_view_diagnosis" );
                                query.setDomain( "DIAGNOSIS", null, null, null );
                                query.setFields(formfields, null);

                                // When cancel is pressed the selected items from the picker list should still remain in the diagnosis
                                if ( (strIn != null) && (strIn.length() > 2))
                                {
                                    query.setWhere( null, 0, "DIAGNOSIS_intDiagnosisKey", "IN", strIn, 0, DALQuery.WHERE_HAS_VALUE );
                                    ResultSet rs = query.executeSelect();
                                    strXML.append( QueryChannel.buildSearchXMLFile( "diagnosis", rs, formfields ));                        
                                }    
                            }
                        }    
                    
                    }    
                    
               }
               else if( runtimeData.getParameter( "action" ).equals( DIAGNOSIS_SELECTED ))
               {
                   formfields = DatabaseSchema.getFormFields( "cpatient_add_patient_admissions" );

                   
                   diagnosisRuntimeData.setParameter( "ADMISSIONS_dtAdmission",
                       QueryChannel.makeDateFromForm( "ADMISSIONS_dtAdmission", diagnosisRuntimeData ));
                   diagnosisRuntimeData.setParameter( "ADMISSIONS_dtDiagnosis",
                       QueryChannel.makeDateFromForm( "ADMISSIONS_dtDiagnosis", diagnosisRuntimeData ));
                   diagnosisRuntimeData.setParameter( "ADMISSIONS_dtRelapse",
                       QueryChannel.makeDateFromForm( "ADMISSIONS_dtRelapse", diagnosisRuntimeData ));
                   diagnosisRuntimeData.setParameter( "ADMISSIONS_dtEvent",
                       QueryChannel.makeDateFromForm( "ADMISSIONS_dtEvent", diagnosisRuntimeData ));
                   diagnosisRuntimeData.setParameter( "ADMISSIONS_dtDischarge",
                       QueryChannel.makeDateFromForm( "ADMISSIONS_dtDischarge", diagnosisRuntimeData ));
                   diagnosisRuntimeData.setParameter( "ADMISSIONS_dtSurgery",
                       QueryChannel.makeDateFromForm( "ADMISSIONS_dtSurgery", diagnosisRuntimeData ));
                   
                   
                   strXML.append( QueryChannel.buildViewXMLFile( formfields, diagnosisRuntimeData ));

                   strXML.append( "<ADMISSIONS_intAdmissionkey>" + runtimeData.getParameter("ADMISSIONS_intAdmissionkey")
                       + "</ADMISSIONS_intAdmissionkey>" );

                   query = null;
                   query = new DALSecurityQuery("patient_admissions", authToken);
                   formfields = DatabaseSchema.getFormFields( "cpatient_view_diagnosis" );
                   query.setDomain( "DIAGNOSIS", null, null, null );
                   query.setFields(formfields, null);

                   if( runtimeData.getParameter( "diagnosisSubmit" ).equals("true")){

                       strIn = "(";
                       boolean hasValue = false;

                       int diagnosisCount = QueryChannel.getNewestKeyAsInt( "DIAGNOSIS_intDiagnosisKey" );

                       for( int i = 1; i <= diagnosisCount; i++ ){
                           if( runtimeData.getParameter( "diagnosis_" + i) != null ){
                               strIn += i + ",";
                               hasValue = true;
                           }
                       }

                       strIn = strIn.substring( 0, strIn.length() - 1) + ")";
                       // New Diagnosis has been selected set the variable that holds the original 
                       // selected diagnosis to null
                       strEditDiag = "";

                       query.setWhere( null, 0, "DIAGNOSIS_intDiagnosisKey", "IN", strIn, 0, DALQuery.WHERE_HAS_VALUE );
                       if( hasValue ){
                           ResultSet rs = query.executeSelect();
                           strXML.append( QueryChannel.buildSearchXMLFile( "diagnosis", rs, formfields ));
                       }
                       
                       // If we are currently editing an admission - keep in edit mode
                       if (runtimeData.getParameter("editAdmissionSelected").equals ("true"))
                       {    
                           strXML.append ("<editAdmissionSelected>" + "true" + "</editAdmissionSelected>");
                       }
                    }
                    else 
                    {

                       // When cancel is presses the selected items from the picker list should still remain in the diagnosis
                       if ( (strIn != null) && (strIn.length() > 2) )
                       {
                           query.setWhere( null, 0, "DIAGNOSIS_intDiagnosisKey", "IN", strIn, 0, DALQuery.WHERE_HAS_VALUE );
                           ResultSet rs = query.executeSelect();
                           strXML.append( QueryChannel.buildSearchXMLFile( "diagnosis", rs, formfields ));                        
                       }
                       else 
                       {
                           // If editing an admission and the select diagnosis from picker has
                           // been cancelled, display the original diagnosis before editing
                           if (runtimeData.getParameter("editAdmissionSelected").equals ("true"))
                           {    
                               if (strEditDiag != null)
                               {    
                                   strXML.append(strEditDiag);
                               }
                           }
                       
                       }
                       
                       // If we are currently editing an admission - keep in edit mode
                       if (runtimeData.getParameter("editAdmissionSelected").equals ("true"))
                       {    
                           strXML.append ("<editAdmissionSelected>" + "true" + "</editAdmissionSelected>");
                       }
                       
                    }    

                }
                else{

                    if (runtimeData.getParameter( "editAdmission" ) != null){
                        
                        // check for permission
                        if( !authToken.hasActivity( EDIT_ADMISSION ))
                        {
                            strError = SECURITY_ERROR;
                            strXML.append( QueryChannel.buildAddFormXMLFile( formfields ));
                        }
                        else
                        {
                            // Build the admission details for the selected admission key
                            strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields, "ADMISSIONS_intAdmissionkey" , runtimeData.getParameter( "ADMISSIONS_intAdmissionkey") , authToken));
                            strXML.append ("<editAdmissionSelected>" + "true" + "</editAdmissionSelected>");


                            if (PROPERTY_DIAGNOSIS_PICKER)
                            {
                                strEditDiag = "";
                                strIn = "";

                                // Build the diagnosis categories
                                formfields = null;
                                formfields = DatabaseSchema.getFormFields( "cpatient_view_patient_admissions" );

                                query = new DALSecurityQuery("patient_admissions", authToken);               
                                query.setDomain("ADMISSIONS", null, null, null);
                                query.setField( "ADMISSIONS_strDiagCategory", null );
                                query.setWhere(null, 0, "ADMISSIONS_intAdmissionkey", "=", runtimeData.getParameter( "ADMISSIONS_intAdmissionkey"), 0, DALQuery.WHERE_HAS_VALUE);
                                ResultSet rsResultSet = query.executeSelect();

                                if (rsResultSet.next())
                                {
                                    String strDiagCategory = rsResultSet.getString("ADMISSIONS_strDiagCategory");

                                    if ((strDiagCategory != null) && !strDiagCategory.equals(""))
                                    {    
                                        String strDiag[] = strDiagCategory.split(";");
                                        for (int i=0; i<strDiag.length; i++)
                                        {
                                            if (strDiag[i].length() > 0 && !strDiag[i].equals(" "))
                                            {    
                                                strEditDiag += ("<diagnosis>" + "<DIAGNOSIS_strDiagnosisName>" + strDiag[i] + "</DIAGNOSIS_strDiagnosisName>" + "</diagnosis>");
                                            }
                                        }    
                                        strXML.append(strEditDiag);
                                    }
                                }    

                                // Close the resultset
                                rsResultSet.close();
                            }    
                        }    

                    }
                    else if (runtimeData.getParameter( "updateAdmission" ) != null)
                    {
                        String strUpdateXML[] = doUpdateAdmission ();
                        strXML.append(strUpdateXML[0]);
                        strError += strUpdateXML[1];                    
                    }                            
                    else
                    {    
                        strXML.append( QueryChannel.buildAddFormXMLFile( formfields ));
                        strIn = "";
                        strEditDiag = "";
                    }
                }
                
                resetLock();

                query = null;
                formfields = null;

                
                formfields = DatabaseSchema.getFormFields( "cpatient_view_patient" );
                query = new DALSecurityQuery("patient_admissions", authToken);

                strXML.append( QueryChannel.buildFormLabelXMLFile( formfields ));
                strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields, "PATIENT_intInternalPatientID", patientID,authToken ));
                
                lock.addLock( "PATIENT", patientID, LockRecord.READ_ONLY );
                
                formfields = null;
                formfields = DatabaseSchema.getFormFields( "cpatient_view_patient_admissions" );
                
                query = null;
                query = new DALSecurityQuery("patient_admissions", authToken);               
                query.setDomain("PATIENT", null, null, null);
                query.setDomain("ADMISSIONS","PATIENT_intInternalPatientID","ADMISSIONS_intPatientID","LEFT JOIN");
                query.setFields( formfields, null );
                query.setWhere(null, 0, "PATIENT_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"PATIENT_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"ADMISSIONS_intPatientID","=",patientID.toString(),0,DALQuery.WHERE_HAS_VALUE);            
                query.setWhere("AND",0,"ADMISSIONS_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);            
                query.setOrderBy("ADMISSIONS_strAdmissionID", "DESC");
                
                ResultSet rsResultSet = query.executeSelect();
                strXML.append( QueryChannel.buildSearchXMLFile("admission", rsResultSet, formfields ));
                rsResultSet.close();
                rsResultSet = null;
                
                lock.addLock( "PATIENT_ADMISSIONS", patientID, LockRecord.READ_WRITE );
                
                 
                strXML.append( "<error>" + strError + "</error>" );
                strXML.append( buildSmartformList( patientID ));
                this.strStylesheet = ADMISSIONS;
                return "<patient>" + strXML.toString() + "</patient>";
                 
            }
        }
        catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
            
        }
        
        
        return strXML.toString();
        
    }
     
    
    /**
     * Method to select the patient's diagnosis
     *
     * @return XML String to be parsed
     *
     **/
    private String doDiagnosis(){
        
        StringBuffer strXML = new StringBuffer();
        String patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
        String strError = "";
        try {
            
            if( patientID != null ){
                
                diagnosisRuntimeData = (ChannelRuntimeData) runtimeData.clone();
                
                Vector formfields = DatabaseSchema.getFormFields("cpatient_view_diagnosis");
                DALQuery query = new DALQuery();
                
                query.setDomain( "DIAGNOSIS", null, null, null );
                query.setFields( formfields, null );
                query.setWhere( null, 0, "DIAGNOSIS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setDistinctField( "DIAGNOSIS_intDiagnosisKey" );
                
                ResultSet rs = query.executeSelect();
                
                strXML.append( QueryChannel.buildSearchXMLFile( "diagnosis", rs, formfields ));

                rs.close();
                rs = null;
                
                query.reset();                                
                query.setDomain( "DIAGNOSIS", null, null, null );
                query.setCountField ("DIAGNOSIS_intDiagnosisKey", true);
                query.setWhere(null, 0, "DIAGNOSIS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                rs = query.executeSelect();
                rs.next();
                
                strXML.append( "<diagnosisCount>" + Integer.toString(rs.getInt(1)) + "</diagnosisCount>" );
                strXML.append( "<currentField>" + runtimeData.getParameter( "currentField" ) + "</currentField>" );
                
                
                Enumeration myEnum = runtimeData.keys();
                
                while( myEnum.hasMoreElements() ){
                    String param = (String) myEnum.nextElement();
                    
                    strXML.append( "<parameter><name>" + param + "</name><value>" + runtimeData.getParameter( param ) +
                        "</value></parameter>" );                    
                }
                
                formfields = null;
                formfields = DatabaseSchema.getFormFields("cpatient_view_patient");
                
                strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields, "PATIENT_intInternalPatientID", 
                    patientID ));
                strXML.append( QueryChannel.buildFormLabelXMLFile(formfields) );
                
                // build smartform list, always
                strXML.append( buildSmartformList( patientID ));
                
                
                this.strStylesheet = DIAGNOSIS;
                return "<patient>" + strXML.toString() + "</patient>";
               
            }
            
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Patient Channel - " + e.toString(), e);
        }
        return "<patient>" + strXML.toString() + "</patient>";
    }
    
    private String[] doUpdateAdmission ()
    {
        StringBuffer strXML = new StringBuffer();
        String strError = "";
        String patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
        String admissionID = runtimeData.getParameter("ADMISSIONS_intAdmissionkey");
        String strReturn[] = new String[2];
        Vector formfields;
        DALSecurityQuery query;

        try
        {
            formfields = null;
            formfields = DatabaseSchema.getFormFields( "cpatient_add_patient_admissions" );

            strXML.append( QueryChannel.buildFormLabelXMLFile(formfields));

            runtimeData.setParameter( "ADMISSIONS_dtAdmission",
                QueryChannel.makeDateFromForm( "ADMISSIONS_dtAdmission", runtimeData ));
            runtimeData.setParameter( "ADMISSIONS_dtDiagnosis",
                QueryChannel.makeDateFromForm( "ADMISSIONS_dtDiagnosis", runtimeData ));
            runtimeData.setParameter( "ADMISSIONS_dtRelapse",
               QueryChannel.makeDateFromForm( "ADMISSIONS_dtRelapse", runtimeData ));
            runtimeData.setParameter( "ADMISSIONS_dtEvent",
               QueryChannel.makeDateFromForm( "ADMISSIONS_dtEvent", runtimeData ));
           runtimeData.setParameter( "ADMISSIONS_dtDischarge",
               QueryChannel.makeDateFromForm( "ADMISSIONS_dtDischarge", runtimeData ));
           runtimeData.setParameter( "ADMISSIONS_dtSurgery",
               QueryChannel.makeDateFromForm( "ADMISSIONS_dtSurgery", runtimeData ));
            
            query = null;                                     

            if( ((strError = QueryChannel.checkRequiredFields( formfields, runtimeData )) == null ) && 
                 ((strError = QueryChannel.validateData( formfields, runtimeData )) == null ) )
                {

                if( lock.isValid() && lock.lockWrites() )
                {
                    // possibly edit admission smartforms
                    doAdmissionSmartformsEdit(patientID, admissionID, runtimeData);
                    
                    Vector vtUpdateFormFields = formfields;
                    // Do not want to update the admission ID therefore remove from the formfields
                    vtUpdateFormFields.remove("ADMISSIONS_strAdmissionID");
                    query = new DALSecurityQuery("patient_admissions", authToken);
                    query.setDomain( "ADMISSIONS", null, null, null );
                    query.setFields( formfields, runtimeData );
                    query.setWhere(null, 0, "ADMISSIONS_intAdmissionkey", "=", admissionID, 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();
                    
                    query = null;
                    query = new DALSecurityQuery("patient_admissions", authToken);
                    query.setDomain("ADMISSIONS", null, null, null);
                    query.setWhere(null, 0, "ADMISSIONS_intAdmissionkey", "=", admissionID, 0, DALQuery.WHERE_HAS_VALUE);

                    // Get the patient age at surgery
                    int intAge = getPatientAge(patientID, admissionID);
                    query.setField ("ADMISSIONS_intPatientAge", Integer.toString(intAge));


                    if (PROPERTY_DIAGNOSIS_PICKER)
                    {    
                        String diagnosisCategory="";
                        String diagnosisList[] = runtimeData.getParameterValues("allDiagnosis");
                        if( diagnosisList != null ){

                            for( int i = 0; i < diagnosisList.length; i++ ){
                                diagnosisCategory = diagnosisCategory + diagnosisList[i].trim() + "; ";
                            }
                        }

                        query.setField ("ADMISSIONS_strDiagCategory",  diagnosisCategory );                            
                        strEditDiag = "";
                    }
                    query.executeUpdate();
                    lock.unlockWrites ();
                    // Build the admission add form
                    strXML.append( QueryChannel.buildAddFormXMLFile( formfields ));
                    strError = "History updated.";   
                    
                }else{
                    strError = LOCK_ERROR;
                    strXML.append( "<ADMISSIONS_intAdmissionkey>" + admissionID + "</ADMISSIONS_intAdmissionkey>" );                    
                    strXML.append( QueryChannel.buildViewXMLFile(formfields, runtimeData ));
                    strXML.append ("<editAdmissionSelected>" + "true" + "</editAdmissionSelected>");
                }
            }         
            else{
                strXML.append( "<ADMISSIONS_intAdmissionkey>" + admissionID + "</ADMISSIONS_intAdmissionkey>" );
                strXML.append( QueryChannel.buildViewXMLFile(formfields, runtimeData ));
                strXML.append ("<editAdmissionSelected>" + "true" + "</editAdmissionSelected>");


                if (PROPERTY_DIAGNOSIS_PICKER)
                {                    
                    // Rebuild the diagnosis that was selected using the diagnosis picker
                    query = null;
                    query = new DALSecurityQuery("patient_admissions", authToken);
                    formfields = DatabaseSchema.getFormFields( "cpatient_view_diagnosis" );
                    query.setDomain( "DIAGNOSIS", null, null, null );
                    query.setFields(formfields, null);

                    // When cancel is pressed the selected items from the picker list should still remain in the diagnosis
                    if ( (strIn != null) && (strIn.length() > 2))
                    {
                        query.setWhere( null, 0, "DIAGNOSIS_intDiagnosisKey", "IN", strIn, 0, DALQuery.WHERE_HAS_VALUE );
                        ResultSet rs = query.executeSelect();
                        strXML.append( QueryChannel.buildSearchXMLFile( "diagnosis", rs, formfields ));                        
                    }
                    else
                    {
                        // No new diagnosis has been selected
                        // Display the original diagnosis for the admission that
                        // was selected for editing
                        if (strEditDiag != null)
                        {    
                            strXML.append(strEditDiag);
                        }
                    }
                }
            }  
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Patient Channel - " + e.toString(), e);
        }
        
        // return a string array
        // element 0 is the generated XML
        // element 1 is any error XML
        strReturn[0] = strXML.toString();
        strReturn[1] = strError;
        return strReturn;
    }
    
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doAppointments(){
        
        StringBuffer strXML = new StringBuffer();
        
        String patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
        
        try{
            
            
            
            if( patientID != null ){
                
                String strError =  "";
                Vector formfields;
                DALSecurityQuery query;
                
                // check for permision
                if( !authToken.hasActivity( "patient_appointments" )){
                    strSecurityError = SECURITY_ERROR;
                    return doViewPatient();
                }
                
                /**************************** PROCESS PREACTIONs **********************/
                
                if( runtimeData.getParameter( "deleteAppointment") != null ){
                    
                    // check for permision
                    if( !authToken.hasActivity( "patient_appointments_delete" )){
                        strError = SECURITY_ERROR;
                    }
                    else
                    {
                        formfields = DatabaseSchema.getFormFields( "cpatient_view_appointments");
                        query = new DALSecurityQuery( "patient_appointments", authToken );


                        query.setDomain( "APPOINTMENTS", null, null, null );

                        query.setField( "APPOINTMENTS_intDeleted", "-1" );
                        query.setWhere( null, 0, "APPOINTMENTS_intAppointmentKey", "=",
                        runtimeData.getParameter( "APPOINTMENTS_intAppointmentKey" ), 0, DALQuery.WHERE_HAS_VALUE);

                        if( lock.isValid() && lock.lockWrites() ){
                            query.executeUpdate();
                            lock.unlockWrites();

                            strError = "Appointment deleted.";

                        }else{
                            strError = LOCK_ERROR;
                        }
                    }    

                }
                
                if( (runtimeData.getParameter ("saveAppointment") != null) &&
                    (runtimeData.getParameter ("updateAppointment") == null) )
                {
                    // check for permision
                    if( !authToken.hasActivity( "patient_appointments_add" )){
                        strError = SECURITY_ERROR;
                    }
                    else
                    {
                        formfields = null;
                        query = null;

                        formfields = DatabaseSchema.getFormFields ( "cpatient_view_appointments");
                        query = new DALSecurityQuery ( "patient_appointments", authToken );
                        runtimeData.setParameter ( "APPOINTMENTS_dtAppDate",
                        QueryChannel.makeDateFromForm ( "APPOINTMENTS_dtAppDate", runtimeData ));
                        runtimeData.setParameter ( "APPOINTMENTS_dtAppAlertDate",
                        QueryChannel.makeDateFromForm ( "APPOINTMENTS_dtAppAlertDate", runtimeData ));
                        runtimeData.setParameter ( "APPOINTMENTS_tmAppTime",
                        QueryChannel.makeTimeFromForm ( "APPOINTMENTS_tmAppTime", runtimeData ));
                        runtimeData.setParameter ( "APPOINTMENTS_intInternalPatientID", patientID );


                        if( (strError = QueryChannel.checkRequiredFields ( formfields, runtimeData )) == null &&
                            (strError = QueryChannel.validateData ( formfields, runtimeData ))  == null )
                        {

                            query.setDomain ( "APPOINTMENTS", null, null, null );
                            query.setFields ( formfields, runtimeData );
                            if( lock.isValid () && lock.lockWrites () )
                            {
                                query.executeInsert ();
                                lock.unlockWrites ();
                                strError = "Appointment inserted.";
                            }
                            else
                            {
                                strError = LOCK_ERROR;
                            }
                        }                    
                    }    
                }

                if ((runtimeData.getParameter ("saveAppointment") != null) && 
                    (runtimeData.getParameter ("updateAppointment") != null))
                {
                    // check for permision
                    if( !authToken.hasActivity( "patient_appointments_edit" )){
                        strError = SECURITY_ERROR;
                    }
                    else
                    {
                        query = null;
                        runtimeData.setParameter ("APPOINTMENTS_intAppointmentKey", runtimeData.getParameter("updateAppointment"));
                        runtimeData.setParameter ( "APPOINTMENTS_dtAppDate", QueryChannel.makeDateFromForm ( "APPOINTMENTS_dtAppDate", runtimeData ));
                        runtimeData.setParameter ( "APPOINTMENTS_dtAppAlertDate", QueryChannel.makeDateFromForm ( "APPOINTMENTS_dtAppAlertDate", runtimeData ));
                        runtimeData.setParameter ( "APPOINTMENTS_tmAppTime", QueryChannel.makeTimeFromForm ( "APPOINTMENTS_tmAppTime", runtimeData ));
                        runtimeData.setParameter ( "APPOINTMENTS_intInternalPatientID", patientID );
                        query = new DALSecurityQuery ( "patient_appointments", authToken );

                        query.setDomain ( "APPOINTMENTS", null, null, null );
                        query.setFields ( DatabaseSchema.getFormFields ( "cpatient_update_appointments"), runtimeData );
                        query.setWhere ( null, 0, "APPOINTMENTS_intAppointmentKey", "=",
                                 runtimeData.getParameter ("updateAppointment"), 0, DALQuery.WHERE_HAS_VALUE );

                        if( (strError = QueryChannel.checkRequiredFields ( DatabaseSchema.getFormFields ( "cpatient_update_appointments"), runtimeData )) == null &&
                            (strError = QueryChannel.validateData ( DatabaseSchema.getFormFields ( "cpatient_update_appointments"), runtimeData ))  == null )
                        {
                            if( lock.isValid () && lock.lockWrites () )
                            {
                                query.executeUpdate();
                                lock.unlockWrites ();
                                strError = "Appointment Updated.";
                            }
                            else
                            {
                                strError = LOCK_ERROR;
                            }
                        }                    
                    }    
                    
                }                
                
                resetLock();
                /**************************** BUILD DETAIL **********************/
                query = null;
                query = new DALSecurityQuery( "patient_view", authToken);
                
                // build patient details
                formfields = null;
                formfields = DatabaseSchema.getFormFields( "cpatient_view_patient_details" );
                strXML.append( QueryChannel.buildFormLabelXMLFile( formfields ));
                strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields,
                "PATIENT_intInternalPatientID", patientID,authToken ) );
                lock.addLock( "PATIENT", patientID, LockRecord.READ_ONLY );
                
                // build appointment list
                query = null;
                query = new DALSecurityQuery( "patient_appointments", authToken);
                query.setDomain( "APPOINTMENTS", null, null, null );
                
                formfields = null;
                formfields = DatabaseSchema.getFormFields( "cpatient_view_appointments" );
                query.setFields( formfields, null );
                
                if( runtimeData.getParameter ( "editAppointment" ) == null )
                {
                    query.setWhere ( null, 0, "APPOINTMENTS_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE );
                    query.setWhere ( "AND", 0, "APPOINTMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );

                }
                else
                {
                    query.setWhere ( null, 0, "APPOINTMENTS_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE );
                    query.setWhere ( "AND", 0, "APPOINTMENTS_intAppointmentKey", "<>", runtimeData.getParameter ("editAppointment"), 0, DALQuery.WHERE_HAS_VALUE );
                    query.setWhere ( "AND", 0, "APPOINTMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                }


                ResultSet rs = query.executeSelect ();

                strXML.append ( QueryChannel.buildAddFormXMLFile ( formfields ));
                strXML.append ( QueryChannel.buildFormLabelXMLFile ( formfields ));
                strXML.append ( QueryChannel.buildSearchXMLFile ( "appointment", rs, formfields));
                
                rs.close();
                rs = null;
                
                // build editable appointment

                if( runtimeData.getParameter ( "editAppointment" ) != null )
                {
                    query = null;
                    query = new DALSecurityQuery ( "patient_appointments", authToken);
                    query.setDomain ( "APPOINTMENTS", null, null, null );

                    formfields = null;
                    formfields = DatabaseSchema.getFormFields ( "cpatient_view_appointments" );
                    query.setFields ( formfields, null );

                    query.setWhere ( null, 0, "APPOINTMENTS_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE );
                    query.setWhere ( "AND", 0, "APPOINTMENTS_intAppointmentKey", "=", runtimeData.getParameter ("editAppointment"), 0, DALQuery.WHERE_HAS_VALUE );

                    query.setWhere ( "AND", 0, "APPOINTMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );

                    rs = query.executeSelect ();
                    


                    /*strXML.append ( QueryChannel.buildViewXMLFile ( formfields, runtimeData ));
                    strXML.append ( QueryChannel.buildFormLabelXMLFile ( formfields ));
                    strXML.append ( QueryChannel.buildSearchXMLFile ( "editAppointment", rs, formfields)); */


                    strXML.append ("<editFields>");
                       strXML.append ( QueryChannel.buildViewFromKeyXMLFile ( DatabaseSchema.getFormFields ( "cpatient_view_appointments" ),
                        "APPOINTMENTS_intAppointmentKey", runtimeData.getParameter ("editAppointment")));
                    strXML.append ("</editFields>");

                    strXML.append ( "<strEdit>" + runtimeData.getParameter ("editAppointment") + "</strEdit>" );



                     strXML.append ( QueryChannel.buildViewFromKeyXMLFile ( DatabaseSchema.getFormFields ( "cpatient_view_consent_study" ),
                    "CONSENTSTUDY_intConsentStudyKey", runtimeData.getParameter ("CONSENTSTUDY_intConsentStudyKey")));
                    strXML.append ( "<strEdit>" + runtimeData.getParameter ("CONSENTSTUDY_intConsentStudyKey") + "</strEdit>" );
                }

                lock.addLock ( "PATIENT_APPOINTMENTS", patientID, LockRecord.READ_WRITE );

                // build study and email list
                query = null;
                query = new DALSecurityQuery( "patient_appointments", authToken);
                formfields = null;
                formfields = DatabaseSchema.getFormFields( "ccoredomains_view_user_details" );
                query.setDomain( "USERDETAILS", null, null, null);
                query.setFields( formfields, null );
                
                //query.setWhere( null, 0, "USERDETAILS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                rs = null;
                rs = query.executeSelect();
                strXML.append( QueryChannel.buildFormLabelXMLFile( formfields ));
                strXML.append( QueryChannel.buildSearchXMLFile( "user", rs, formfields ));

                rs.close();
                
                // build study list
                query = null;
                query = new DALSecurityQuery( "patient_appointments", authToken);
                formfields = DatabaseSchema.getFormFields( "cpatient_view_smartform_name" );
                query.setDomain( "SMARTFORM", null, null, null);
                query.setDomain( "PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intSmartformKey",
                "SMARTFORM_intSmartformID", "INNER JOIN");
                query.setFields( formfields, null );
                
                query.setWhere( null, 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setWhere( "AND", 0, "PATIENT_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                
                // TO-DO: Lock surveys as well.
                rs = null;
                rs = query.executeSelect();
                strXML.append( QueryChannel.buildFormLabelXMLFile( formfields ));
                strXML.append( QueryChannel.buildSearchXMLFile( "smartform", rs, formfields ));
                
                rs.close();
                
                
                
                this.strStylesheet = APPOINTMENTS;
                
                strXML.append( "<error>" + strError + "</error>" );
                strXML.append( buildSmartformList( patientID ));
                
                return "<patient>" + strXML.toString() + "</patient>";
                
                /*if( strError != null ){
                 
                    strXML.append( QueryChannel.buildFormLabelXMLFile( formfields ));
                 
                    strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields,
                        "PATIENT_intInternalPatientID", patientID ));
                    strXML.append( "<error>" + strError + "</error>" );
                 
                    this.strStylesheet = VIEW_PATIENT;
                    return "<patient>" + strXML.toString() + "</patient>";
                 
                }else{
                 
                    return doSearchPatient();
                 
                }*/
            }
            
        }
        catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
            
        }
        
        
        
        
        return strXML.toString();
        
    }
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doAttachments(){
        
        StringBuffer strXML = new StringBuffer();
        String patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
        String strError = "";
        try {
            
            if( patientID != null ){
                
                // check for permision
                if( !authToken.hasActivity( "patient_attachments" )){
                    strSecurityError = SECURITY_ERROR;
                    return doViewPatient();
                }
                
                if( runtimeData.getParameter( "saveAttachments" ) != null ){
                    
                    // check for permision
                    if( !authToken.hasActivity( "patient_attachments_add" )){
                        strError = SECURITY_ERROR;
                    }
                    else
                    {
                        if( lock.isValid() && lock.lockWrites() ){

                            String fileName = "";
                            if( (fileName = QueryChannel.saveUploadFile( "ATTACHMENTS_strAttachmentsFileName",
                                runtimeData, PropertiesManager.getProperty( "neuragenix.bio.patient.SaveAttachmentsLocation" ) )) == null ){

                                strError = FILE_SIZE_ERROR;

                            }else{

                                strError = "File uploaded.";

                                runtimeData.setParameter("ATTACHMENTS_strAttachedBy", authToken.getUserIdentifier() );
                                runtimeData.setParameter("ATTACHMENTS_strAttachmentsFileName",fileName);
                                runtimeData.setParameter("ATTACHMENTS_strID", patientID);
                                DALSecurityQuery query = new DALSecurityQuery("patient_attachments", authToken);
                                query.setDomain("ATTACHMENTS", null, null, null);    
                                query.setFields(DatabaseSchema.getFormFields("cpatient_add_patient_attachments"), runtimeData);
                                query.executeInsert();

                            }
                            lock.unlockWrites();

                        }else{
                                strError = LOCK_ERROR;
                        }
                    }    
                   
                }
                
                if( runtimeData.getParameter( "deleteAttachments" ) != null ){
                            
                        // check for permision
                        if( !authToken.hasActivity( "patient_attachments_delete" )){
                            strError = SECURITY_ERROR;
                        }
                        else
                        {
                            DALSecurityQuery query = new DALSecurityQuery("patient_attachments", authToken);
                            query.setDomain("ATTACHMENTS", null, null, null);    
                            query.setField("ATTACHMENTS_intDeleted", "-1");

                            query.setWhere( null, 0, "ATTACHMENTS_attachmentkey", "=", runtimeData.getParameter("ATTACHMENTS_attachmentkey"), 0,
                             DALQuery.WHERE_HAS_VALUE );


                            if( lock.isValid() && lock.lockWrites() ){
                                query.executeUpdate();
                                lock.unlockWrites();

                                strError = "File deleted.";

                            }else{
                                strError = LOCK_ERROR;
                            }
                        }                       
                }
                
                resetLock();
                
                Vector vtViewPatientAttachmentFormFields = DatabaseSchema.getFormFields("cpatient_view_patient_attachments");
                DALSecurityQuery query = new DALSecurityQuery("patient_attachments", authToken);

                query.setDomain("PATIENT", null, null, null);
                query.setDomain("ATTACHMENTS","PATIENT_intInternalPatientID","ATTACHMENTS_strID","LEFT JOIN");
                query.setFields(vtViewPatientAttachmentFormFields, null);

                query.setWhere(null, 0, "PATIENT_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"PATIENT_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"ATTACHMENTS_domainName","=","Patient",0,DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"ATTACHMENTS_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
                
                ResultSet rsResultSet = query.executeSelect();
                strXML.append( QueryChannel.buildSearchXMLFile("attachment", rsResultSet, vtViewPatientAttachmentFormFields));
                strXML.append(QueryChannel.buildFormLabelXMLFile(vtViewPatientAttachmentFormFields));
                
                rsResultSet.close();
                rsResultSet = null;
                
                lock.addLock( "PATIENT_ATTACHMENTS", patientID, LockRecord.READ_WRITE );

                Vector vtViewPatientFormFields = DatabaseSchema.getFormFields("cpatient_view_patient");
                
                strXML.append( QueryChannel.buildViewFromKeyXMLFile( vtViewPatientFormFields, "PATIENT_intInternalPatientID", 
                    patientID ,authToken));
                strXML.append( QueryChannel.buildFormLabelXMLFile(vtViewPatientFormFields) );
                lock.addLock( "PATIENT", patientID, LockRecord.READ_ONLY );
                
                
                
                strXML.append( "<error>" + strError + "</error>" );
                strXML.append( buildSmartformList( patientID ));
                
                this.strStylesheet = ATTACHMENTS;
                return "<patient>" + strXML.toString() + "</patient>";
               


            }
            
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Patient Channel - " + e.toString(), e);
        }
        return "<patient>" + strXML.toString() + "</patient>";
        
        
        
    }
    
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doAddConsentFile(){
        
        StringBuffer strXML = new StringBuffer();
        String patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
        String strError = "";
        try {
            
            if( patientID != null ){
                
                Vector formfields;
                DALSecurityQuery query;
                
                
                
                if( runtimeData.getParameter( "saveFile" ) != null ){
                    
                    String fileName = QueryChannel.saveUploadFile( "CONSENTSTUDY_strFileName",
                        runtimeData, PropertiesManager.getProperty( "neuragenix.bio.patient.SaveConsentLocation" ) );
                    
                    
                    if( fileName != null ){
                        
                        query = null;
                        query = new DALSecurityQuery( "patient_consent", authToken );
                        query.setDomain( "CONSENTSTUDY", null, null, null );

                        query.setField( "CONSENTSTUDY_strFileName", fileName );
                        query.setWhere( null, 0, "CONSENTSTUDY_intConsentStudyKey", "=", 
                                    runtimeData.getParameter("CONSENTSTUDY_intConsentStudyKey"), 0, DALQuery.WHERE_HAS_VALUE );

                        
                        if( lock.isValid() && lock.lockWrites() ){
                            query.executeUpdate();
                            lock.unlockWrites();

                            strError = null;

                        }else{
                            strError = LOCK_ERROR;
                        }
                        
                        if( strError == null ){
                            runtimeData.remove( "addFile" );
                            return doConsent();
                        }
                    
                    }else{
                        strError = FILE_SIZE_ERROR;
                        
                    }
                    
                }
                
                resetLock();
                
                // build patient details

                formfields = null;
                formfields = DatabaseSchema.getFormFields("cpatient_view_patient");
                
                strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields, "PATIENT_intInternalPatientID", 
                    patientID,authToken ));
                strXML.append( QueryChannel.buildFormLabelXMLFile(formfields) );
                lock.addLock( "PATIENT", patientID, LockRecord.READ_ONLY );
                lock.addLock( "PATIENT_CONSENT", patientID, LockRecord.READ_WRITE );
                
   
                
                this.strStylesheet = CONSENT_FILE;
                strXML.append( "<error>" + strError + "</error>" );
                strXML.append( "<CONSENTSTUDY_intConsentStudyKey>" + runtimeData.getParameter("CONSENTSTUDY_intConsentStudyKey") + 
                    "</CONSENTSTUDY_intConsentStudyKey>" );
                return "<patient>" + strXML.toString() + "</patient>";
               


            }
            
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Patient Channel - " + e.toString(), e);
        }
        return "<patient>" + strXML.toString() + "</patient>";
        
        
    }
    
    
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doAddNoteFile(){
        
        StringBuffer strXML = new StringBuffer();
        String patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
        String strError = "";
        try {
            
            if( patientID != null ){
                
                Vector formfields;
                DALSecurityQuery query;
                
                
                
                if( runtimeData.getParameter( "saveFile" ) != null ){
                    
                    String fileName = QueryChannel.saveUploadFile( "CONSENTSTUDY_strFileName",
                        runtimeData, PropertiesManager.getProperty( "neuragenix.bio.patient.SaveConsentLocation" ) );
                    
                    
                    if( fileName != null ){
                        
                        query = null;
                        query = new DALSecurityQuery( "patient_consent", authToken );
                        query.setDomain( "CONSENTSTUDY", null, null, null );

                        query.setField( "CONSENTSTUDY_strFileName", fileName );
                        query.setWhere( null, 0, "CONSENTSTUDY_intConsentStudyKey", "=", 
                                    runtimeData.getParameter("CONSENTSTUDY_intConsentStudyKey"), 0, DALQuery.WHERE_HAS_VALUE );

                        LockRequest aLock = new LockRequest( authToken );
                        aLock.addLock( "CONSENTSTUDY", runtimeData.getParameter( "CONSENTSTUDY_intConsentStudyKey"), LockRecord.READ_WRITE );
                        aLock.lockDelayWrite();

                        if( aLock.isValid() && aLock.lockWrites() ){
                            query.executeUpdate();
                            aLock.unlockWrites();

                            strError = null;

                        }else{
                            strError = LOCK_ERROR;
                        }

                        aLock.unlock();
                        aLock = null;
                        
                        if( strError == null ){
                            runtimeData.remove( "addFile" );
                            return doConsent();
                        }
                    
                    }else{
                        strError = FILE_SIZE_ERROR;
                        
                    }
                    
                }
                
                
                // build patient details

                formfields = null;
                formfields = DatabaseSchema.getFormFields("cpatient_view_patient");
                
                strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields, "PATIENT_intInternalPatientID", 
                    patientID,authToken ));
                strXML.append( QueryChannel.buildFormLabelXMLFile(formfields) );
                lock.addLock( "PATIENT", patientID, LockRecord.READ_ONLY );
                lock.addLock( "CONSENTSTUDY", runtimeData.getParameter("CONSENTSTUDY_intConsentStudyKey"), LockRecord.READ_ONLY );
                
   
                
                this.strStylesheet = CONSENT_FILE;
                strXML.append( "<error>" + strError + "</error>" );
                strXML.append( "<CONSENTSTUDY_intConsentStudyKey>" + runtimeData.getParameter("CONSENTSTUDY_intConsentStudyKey") + 
                    "</CONSENTSTUDY_intConsentStudyKey>" );
                return "<patient>" + strXML.toString() + "</patient>";
               


            }
            
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Patient Channel - " + e.toString(), e);
        }
        return "<patient>" + strXML.toString() + "</patient>";
        
        
    }
    
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doConsent(){
        DEFAULT_STUDY = SystemConfiguration.getConfigValue("DEFAULTSTUDY");
        StringBuffer strXML = new StringBuffer();
        String patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
        String strError = "";
        try {
            
            if( patientID != null ){
                
                Vector formfields;
                DALSecurityQuery query;
                
                // check for permision
                if( !authToken.hasActivity( "patient_consent" )){
                    strSecurityError = SECURITY_ERROR;
                    return doViewPatient();
                }
                
                // adding file
                if( runtimeData.getParameter( "addFile" ) != null )
                {
                    // check for permision
                    if( !authToken.hasActivity( "patient_consent_study_edit" )){
                        strError = SECURITY_ERROR;
                    }
                    else
                    {
                        if( lock.isValid() && lock.lockWrites() ){
                            return doAddConsentFile();
                        }else{
                            strError = LOCK_ERROR;
                        }
                    
                    }    
                            
                }
                // update consent details
                if( runtimeData.getParameter( "updateContactConsent" ) != null ){
                    
                    // check for permision
                    if( !authToken.hasActivity( "patient_consent_update" )){
                        strError = SECURITY_ERROR;
                    }
                    else
                    {
                        query = null;
                        query = new DALSecurityQuery( "patient_consent", authToken );
                        query.setDomain( "CONSENT", null, null, null );

                        if (PROPERTY_VIEW_ONLY_PATIENT_CONSENT_COMMENTS)
                        {
                            query.setFields( DatabaseSchema.getFormFields( "cpatient_edit_consent_comments" ), runtimeData );
                        }
                        else
                        {
                            query.setFields( DatabaseSchema.getFormFields( "cpatient_edit_consent" ), runtimeData );
                        }
                        query.setWhere( null, 0, "CONSENT_intConsentKey", "=", 
                                    runtimeData.getParameter("CONSENT_intConsentKey"), 0, DALQuery.WHERE_HAS_VALUE );

                        if( lock.isValid() && lock.lockWrites() ){
                            query.executeUpdate();
                            lock.unlockWrites();

                            strError = "Details updated.";

                        }else{
                            strError = LOCK_ERROR;
                        }
                    }    
                    
                }
                
                // delete a consent.
                if( runtimeData.getParameter( "deleteConsent" ) != null ){
                    
                    // check for permision
                    if( !authToken.hasActivity( "patient_consent_study_delete" )){
                        strError = SECURITY_ERROR;
                    }
                    else
                    {
                        query = null;
                        query = new DALSecurityQuery( "patient_consent", authToken );
                        query.setDomain( "CONSENTSTUDY", null, null, null );

                        query.setField( "CONSENTSTUDY_intDeleted", "1" );
                        query.setWhere( null, 0, "CONSENTSTUDY_intConsentStudyKey", "=", 
                                    runtimeData.getParameter("CONSENTSTUDY_intConsentStudyKey"), 0, DALQuery.WHERE_HAS_VALUE );

                        if( lock.isValid() && lock.lockWrites() ){
                            query.executeUpdate();
                            lock.unlockWrites();

                            // delete the smartforms result....
                            query = null;
                            query = new DALSecurityQuery("patient_consent", authToken);
                            query.setDomain("PATIENT_SMARTFORM", null, null, null);
                            query.setField("PATIENT_SMARTFORM_intSmartformKey",null);
                            query.setWhere(null,0,"PATIENT_SMARTFORM_intStudyID","=", runtimeData.getParameter("CONSENTSTUDY_intStudyID"),0,DALQuery.WHERE_HAS_VALUE);
                            query.setWhere("AND", 0, "PATIENT_SMARTFORM_intInternalPatientID", "=", runtimeData.getParameter("PATIENT_intInternalPatientID"), 0, DALQuery.WHERE_HAS_VALUE);
                            query.setWhere("AND",0,"PATIENT_SMARTFORM_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
                            ResultSet rs = query.executeSelect();

                            DALSecurityQuery query2 = new DALSecurityQuery();
                            query2.setDomain("PATIENT_SMARTFORM", null, null, null);
                            query2.setField( "PATIENT_SMARTFORM_intDeleted", "-1" );
                            query2.setWhere(null,0,"PATIENT_SMARTFORM_intStudyID","=", runtimeData.getParameter("CONSENTSTUDY_intStudyID"),0,DALQuery.WHERE_HAS_VALUE);
                            query2.setWhere("AND", 0, "PATIENT_SMARTFORM_intInternalPatientID", "=", runtimeData.getParameter("PATIENT_intInternalPatientID"), 0, DALQuery.WHERE_HAS_VALUE);
                            query2.setWhere("AND",0,"PATIENT_SMARTFORM_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);
                            query2.executeUpdate();

                            while(rs.next())
                            {

                                // delete records in smartformparticipants
                                String smartformParticipantKey = rs.getString("PATIENT_SMARTFORM_intSmartformKey");
                                query2 = null;
                                query2 = new DALSecurityQuery("patient_consent", authToken);
                                query2.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
                                query2.setField("SMARTFORMPARTICIPANTS_intDeleted","-1");
                                query2.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "=", smartformParticipantKey, 0, DALQuery.WHERE_HAS_VALUE);
                                query2.executeUpdate();

                                // delete records in smartformresult
                                query2 = null;
                                query2 = new DALSecurityQuery("patient_consent", authToken);
                                query2.setDomain("SMARTFORMRESULTS", null, null, null);
                                query2.setField("SMARTFORMRESULTS_intDeleted","-1");
                                query2.setWhere(null, 0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", smartformParticipantKey, 0, DALQuery.WHERE_HAS_VALUE);
                                query2.executeUpdate();

                            }
                            rs.close();
                            rs = null;

                            strError = "Consent deleted.";

                        }else{
                            strError = LOCK_ERROR;
                        }
                    
                    }    
                    
                    
                }
                
                
                // check for saving or adding consents
                if( runtimeData.getParameter( "saveConsent" ) != null ){
                    
                    formfields = null;
                    query = null;
                    
                    formfields = DatabaseSchema.getFormFields("cpatient_edit_consent_study");
                    query = new DALSecurityQuery( "patient_consent", authToken );
                    
                    runtimeData.setParameter( "CONSENTSTUDY_dtApprovedDate", QueryChannel.makeDateFromForm( "CONSENTSTUDY_dtApprovedDate", 
                        runtimeData ));
                    
                    query.setDomain( "CONSENTSTUDY", null, null, null );
                    query.setFields( formfields, runtimeData );
                    
                    if( (( strError = QueryChannel.checkRequiredFields( formfields, runtimeData )) == null ) &&
                        (( strError = QueryChannel.validateData( formfields, runtimeData )) == null )){
                        if( runtimeData.getParameter( "CONSENTSTUDY_intConsentStudyKey") != null )
                        {
                            // check for permision
                            if( !authToken.hasActivity( "patient_consent_study_edit" )){
                                strError = SECURITY_ERROR;
                            }
                            else
                            {
                    
                                query.setWhere( null, 0, "CONSENTSTUDY_intConsentStudyKey", "=", 
                                    runtimeData.getParameter("CONSENTSTUDY_intConsentStudyKey"), 0, DALQuery.WHERE_HAS_VALUE );

                                if( lock.isValid() && lock.lockWrites() ){
                                    query.executeUpdate();
                                    lock.unlockWrites();

                                    strError = "Details updated.";

                                }else{
                                    strError = LOCK_ERROR;
                                }
                            }    
  
                        }
                        else
                        {
                            // check for permision
                            if( !authToken.hasActivity( "patient_consent_study_add")){
                                strError = SECURITY_ERROR;
                            }
                            else
                            {
                                if( lock.isValid() && lock.lockWrites() ){
                                    query.executeInsert();
                                    lock.unlockWrites();

                                    // integerate with Smartforms.
                                    query = null;
                                    query = new DALSecurityQuery( "patient_consent", authToken );

                                    query.setDomain("SMARTFORM", null, null, null);
                                    query.setDomain("STUDYSURVEY", "SMARTFORM_intSmartformID", "STUDYSURVEY_intSurveyKey", "LEFT JOIN");
                                    query.setField( "STUDYSURVEY_intSurveyKey",null);
                                    query.setWhere(null,0,"STUDYSURVEY_intStudyID","=", runtimeData.getParameter("CONSENTSTUDY_intStudyID"),0,DALQuery.WHERE_HAS_VALUE);
                                    query.setWhere("AND", 0, "STUDYSURVEY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                                    query.setWhere("AND",0,"SMARTFORM_intDeleted","=", "0",0,DALQuery.WHERE_HAS_VALUE);

                                    // get the smartform list
                                    ResultSet rs;
                                    rs = query.executeSelect();

                                    //  build the insert query

                                    DALSecurityQuery insertQuery = new DALSecurityQuery( "patient_consent", authToken );
                                    insertQuery.setDomain( "SMARTFORMPARTICIPANTS", null, null, null);

                                    DALSecurityQuery insertPSQuery = new DALSecurityQuery( "patient_consent", authToken );
                                    insertPSQuery.setDomain( "PATIENT_SMARTFORM", null, null, null);

                                    // build the runtime for PATIENT_SMARTFORM
                                    runtimeData.setParameter( "PATIENT_SMARTFORM_intInternalPatientID", patientID );
                                    runtimeData.setParameter( "PATIENT_SMARTFORM_intStudyID", runtimeData.getParameter("CONSENTSTUDY_intStudyID") );

                                    // build the runtime data for SMARTFORMPARTICIPANTS
                                    runtimeData.setParameter("SMARTFORMPARTICIPANTS_strDomain","Study");
                                    runtimeData.setParameter("SMARTFORMPARTICIPANTS_intParticipantID", patientID);
                                    runtimeData.setParameter("SMARTFORMPARTICIPANTS_strSmartformStatus","Not started");
                                    runtimeData.setParameter("SMARTFORMPARTICIPANTS_strAddedBy",authToken.getUserIdentifier());
                                    runtimeData.setParameter("SMARTFORMPARTICIPANTS_dtDateAdded", Utilities.getDateTimeStampAsString( "dd/MM/yyyy"));


                                    while(rs.next())
                                    {
                                        // insert SMARTFORMPARTICIPANTS
                                        insertQuery.clearFields();
                                        runtimeData.setParameter("SMARTFORMPARTICIPANTS_intSmartformID",rs.getString("STUDYSURVEY_intSurveyKey"));
                                        insertQuery.setFields( DatabaseSchema.getFormFields( "cpatient_view_smartform_participant" ), runtimeData);
                                        insertQuery.executeInsert();
                                        String smartformparticipantKey = QueryChannel.getNewestKeyAsString(insertQuery);
                                        // insert PATIENT_SMARTFORM
                                        insertPSQuery.clearFields();
                                        runtimeData.setParameter("PATIENT_SMARTFORM_intSmartformKey",smartformparticipantKey);
                                        insertPSQuery.setFields( DatabaseSchema.getFormFields( "cpatient_view_patient_smartform" ), runtimeData);
                                        insertPSQuery.executeInsert();

                                    }

                                    rs.close();
                                    rs = null;

                                    strError = "Consent inserted.";
                                }else{
                                    strError = LOCK_ERROR;
                                }
                            }    
                            
                        }
                        
                    }else{
                        if( runtimeData.getParameter( "CONSENTSTUDY_intConsentStudyKey") != null )
                        {    
                            runtimeData.setParameter( "editConsent", "true" );
                        } 
                    }
                }
                
                
                // building consent details
                resetLock();
                
                formfields = null;
                formfields = DatabaseSchema.getFormFields("cpatient_view_consent");
                query = null;
                query = new DALSecurityQuery("patient_consent", authToken);

                query.setDomain("PATIENT", null, null, null);
                query.setDomain("CONSENT", "PATIENT_intInternalPatientID", "CONSENT_intInternalPatientID", "LEFT JOIN" );
                //query.setDomain("CONSENTSTUDY","CONSENT_intConsentKey","CONSENTSTUDY_intConsentKey","LEFT JOIN");
                query.setFields(formfields, null);

                query.setWhere(null, 0, "PATIENT_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"PATIENT_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"CONSENT_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                
                // execute select
                ResultSet rs = query.executeSelect();
                
                if( rs.next() ){
                    strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields, "CONSENT_intConsentKey", 
                        rs.getString( "CONSENT_intConsentKey" ) ));
                }
                rs.close();
                rs = null;
                
                strXML.append(QueryChannel.buildFormLabelXMLFile(formfields));
                
                lock.addLock( "PATIENT_CONSENT", patientID, LockRecord.READ_WRITE );
                             
                
                // build patient details, always
                formfields = null;
                formfields = DatabaseSchema.getFormFields("cpatient_view_patient");
                
                strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields, "PATIENT_intInternalPatientID", 
                    patientID,authToken ));
                strXML.append( QueryChannel.buildFormLabelXMLFile(formfields) );
                
                lock.addLock( "PATIENT", patientID, LockRecord.READ_ONLY );
                
                                
                // build list of studies.
                
                formfields = null;
                formfields = DatabaseSchema.getFormFields("cpatient_view_patient_study");
                
                DALQuery subQuery = new DALQuery();
                
                subQuery.setDomain("PATIENT", null, null, null);
                subQuery.setDomain("CONSENT", "PATIENT_intInternalPatientID", "CONSENT_intInternalPatientID", "INNER JOIN" );
                subQuery.setDomain("CONSENTSTUDY", "CONSENTSTUDY_intConsentKey", "CONSENT_intConsentKey", "INNER JOIN" );
                subQuery.setDomain("STUDY", "STUDY_intStudyID", "CONSENTSTUDY_intStudyID", "INNER JOIN" );
                
                
                subQuery.setFields(formfields, null);

                subQuery.setWhere(null, 0, "PATIENT_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                subQuery.setWhere("AND",0,"PATIENT_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                subQuery.setWhere("AND",0,"CONSENT_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                subQuery.setWhere("AND",0,"CONSENTSTUDY_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                subQuery.setWhere("AND",0,"STUDY_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                
                if( runtimeData.getParameter( "editConsent") != null ){
                    
                    subQuery.setWhere("AND",0,"CONSENTSTUDY_intConsentStudyKey","<>",
                        runtimeData.getParameter("CONSENTSTUDY_intConsentStudyKey"),0,DALQuery.WHERE_HAS_VALUE);    
                    
                    
                }
                                
                rs = null;
                rs = subQuery.executeSelect();
                //
                // display the list
                strXML.append( QueryChannel.buildSearchXMLFile("consent_study", rs, formfields));
                strXML.append(QueryChannel.buildFormLabelXMLFile(formfields));
                strXML.append("<default_study>"+this.DEFAULT_STUDY+"</default_study>");
                rs.close();
                
                // THIS MUST GO HERE BECAUSE IT USES THE QUERY BUILT ABOVE
                DALSecurityQuery sQuery = new DALSecurityQuery( "patient_consent", authToken );
                sQuery.setDomain( "STUDY", null, null, null );
                sQuery.setFields( DatabaseSchema.getFormFields( "cpatient_view_study_name" ), null );
                
                sQuery.setWhere( null, 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                subQuery.clearFields();
                subQuery.setField( "CONSENTSTUDY_intStudyID", null );
                sQuery.setWhere( "AND", 0, "STUDY_intStudyID", "NOT IN", subQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY );
                sQuery.setWhere( "AND", 0, "STUDY_dtStudyEndDate", ">=", neuragenix.common.Utilities.getDateTimeStampAsString( "dd/MM/yyyy" )
                    , 0, DALQuery.WHERE_HAS_VALUE );
                
                
                // build the study names, names and ids only
                rs = null;
                rs = sQuery.executeSelect();
                
                strXML.append( QueryChannel.buildSearchXMLFile( "study", rs, DatabaseSchema.getFormFields( "cpatient_view_study_name" ) ));
                
                rs.close();
                rs = null;
                
                // add or edit???
                if( runtimeData.getParameter( "editConsent") != null ){
                    
                    strXML.append( QueryChannel.buildViewFromKeyXMLFile( DatabaseSchema.getFormFields( "cpatient_view_consent_study" ),
                        "CONSENTSTUDY_intConsentStudyKey", runtimeData.getParameter("CONSENTSTUDY_intConsentStudyKey")));
                    strXML.append( "<strEdit>" + runtimeData.getParameter("CONSENTSTUDY_intConsentStudyKey") + "</strEdit>" );
                    
                }else{
                    strXML.append( QueryChannel.buildViewXMLFile( formfields, runtimeData ));
                }
                
                // determine if only display comments
                if (PROPERTY_VIEW_ONLY_PATIENT_CONSENT_COMMENTS)
                {
                    strXML.append("<displayPatientCommentsOnly>1</displayPatientCommentsOnly>");
                }
                else
                {
                    strXML.append("<displayPatientCommentsOnly>0</displayPatientCommentsOnly>");
                }

                strXML.append( "<error>" + strError + "</error>" );
                strXML.append( buildSmartformList( patientID ));
                
                this.strStylesheet = CONSENT;
                return "<patient>" + strXML.toString() + "</patient>";
               


            }
            
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Patient Channel - " + e.toString(), e);
        }
        return "<patient>" + strXML.toString() + "</patient>";
        
        
    }
    
    /**
     * Method to perform search action on patients
     *
     * @return XML String to be parsed
     *
     **/
    
    private String doNotes(){
        
        StringBuffer strXML = new StringBuffer();
        String patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
        String strError = "";
        try {
            
            if( patientID != null ){
                
                Vector formfields = DatabaseSchema.getFormFields("cpatient_view_notes");
                
                // check for permision
                if( !authToken.hasActivity( "patient_notes" )){
                    strSecurityError = SECURITY_ERROR;
                    return doViewPatient();
                }
                
                if( runtimeData.getParameter( "saveNotes" ) != null ){
                    
                    // check for permision
                    if( !authToken.hasActivity( "patient_notes_add" )){
                        strError = SECURITY_ERROR;
                    }
                    else
                    {
                        runtimeData.setParameter( "NOTES_dtDate", QueryChannel.makeDateFromForm(
                                    "NOTES_dtDate", runtimeData ));

                        if( ((strError = QueryChannel.checkRequiredFields( formfields, runtimeData)) == null) && 
                            ((strError = QueryChannel.validateData( formfields, runtimeData)) == null) &&
                            ((strError = QueryChannel.checkRequiredFields( formfields, runtimeData)) == null) ){

                            if( lock.isValid() && lock.lockWrites() ){

                                String fileName = "";
                                org.jasig.portal.MultipartDataSource fileToSave = (org.jasig.portal.MultipartDataSource) runtimeData.getObjectParameter( "NOTES_strFileName" );

                                if( ((fileName = QueryChannel.saveUploadFile( "NOTES_strFileName",
                                    runtimeData, PropertiesManager.getProperty( "neuragenix.bio.patient.SaveNotesLocation" ) )) == null ) 
                                    && ( fileToSave != null ) && (fileToSave.getInputStream().available() > 0)){

                                    strError = FILE_SIZE_ERROR;

                                }else{

                                    if( ( fileToSave == null ) || (fileToSave.getInputStream().available() < 1 )){
                                        runtimeData.remove("NOTES_strFileName");

                                    }else{
                                        runtimeData.setParameter("NOTES_strFileName",fileName);
                                    }


                                    runtimeData.setParameter("NOTES_strFileName",fileName);
                                    runtimeData.setParameter("NOTES_strID",patientID);
                                    DALSecurityQuery query = new DALSecurityQuery("patient_notes", authToken);
                                    query.setDomain("NOTES", null, null, null);    
                                    query.setFields( formfields, runtimeData);
                                    query.executeInsert();

                                    strError = "Note added.";
                                }
                                lock.unlockWrites();
                            }else{
                                strError = LOCK_ERROR;

                            }
                        }                    
                    }    
                    
                   
                }
                
                if( runtimeData.getParameter( "deleteNotes" ) != null ){
                            
                        // check for permision
                        if( !authToken.hasActivity( "patient_notes_delete" )){
                            strError = SECURITY_ERROR;
                        }
                        else
                        {
                            DALSecurityQuery query = new DALSecurityQuery("patient_notes", authToken);
                            query.setDomain("NOTES", null, null, null);    
                            query.setField("NOTES_intDeleted", "-1");

                            query.setWhere( null, 0, "NOTES_intNotesKey", "=", runtimeData.getParameter("NOTES_intNotesKey"), 0,
                             DALQuery.WHERE_HAS_VALUE );


                            if( lock.isValid() && lock.lockWrites() ){
                                query.executeUpdate();
                                lock.unlockWrites();

                                strError = "Note deleted.";

                            }else{
                                strError = LOCK_ERROR;
                            }
                        }    
                }
                
                resetLock();
                
                // build form to add
                if( strError.equals( "Note deleted.") || strError.equals( "Note added.")){
                    strXML.append( QueryChannel.buildAddFormXMLFile( formfields));
                }else{
                    strXML.append( QueryChannel.buildViewXMLFile( formfields, runtimeData ));
                }
                
                //build the list
                DALSecurityQuery query = new DALSecurityQuery("patient_notes", authToken);

                query.setDomain("NOTES", null, null, null);
                query.setFields(formfields, null);

                query.setWhere(null, 0, "NOTES_intID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"NOTES_intDeleted","=","0",0,DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND",0,"NOTES_strDomain","=","Patient",0,DALQuery.WHERE_HAS_VALUE);
                                

                ResultSet rsResultSet = query.executeSelect();
                strXML.append( QueryChannel.buildSearchXMLFile("note", rsResultSet, formfields));
                rsResultSet.close();
                rsResultSet = null;
                strXML.append(QueryChannel.buildFormLabelXMLFile(formfields));
                
                lock.addLock( "PATIENT_NOTES", patientID, LockRecord.READ_WRITE );

                formfields = DatabaseSchema.getFormFields("cpatient_view_patient");
                
                strXML.append( QueryChannel.buildViewFromKeyXMLFile( formfields, "PATIENT_intInternalPatientID", 
                    patientID,authToken ));
                strXML.append( QueryChannel.buildFormLabelXMLFile(formfields) );
                lock.addLock( "PATIENT", patientID, LockRecord.READ_ONLY );
                
                
                strXML.append( "<error>" + strError + "</error>" );
                strXML.append( buildSmartformList( patientID ));
                
                this.strStylesheet = NOTES;
                return "<patient>" + strXML.toString() + "</patient>";
               


            }
            
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Patient Channel - " + e.toString(), e);
        }
        return "<patient>" + strXML.toString() + "</patient>";
        
        
        
    }
    
    private String buildSmartformList( String patientID ){
        
        StringBuffer strXML = new StringBuffer();
        
        try{
            
            Vector formfields = DatabaseSchema.getFormFields( "cpatient_view_study_name" );
            DALQuery query = new DALQuery();
            
            query.setDomain( "STUDY", null, null, null );
            query.setDomain( "CONSENTSTUDY", "STUDY_intStudyID", "CONSENTSTUDY_intStudyID", "INNER JOIN" );
            query.setDomain( "CONSENT", "CONSENT_intConsentKey", "CONSENTSTUDY_intConsentKey", "INNER JOIN" );
            query.setDistinctField( "STUDY_intStudyID" );
            
            
            query.setFields( formfields, null );
            
            query.setWhere( null, 0, "CONSENT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            query.setWhere( "AND", 0, "CONSENTSTUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            query.setWhere( "AND", 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
            query.setWhere( "AND", 0, "CONSENT_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE );
            
            ResultSet rs = query.executeSelect();
            
            formfields = DatabaseSchema.getFormFields( "cpatient_view_smartform_name" );
            
            while( rs.next() )
            {
                strXML.append( "<study>");
                strXML.append( "<STUDY_intStudyID>" + rs.getString("STUDY_intStudyID") + "</STUDY_intStudyID>" );
                strXML.append( "<STUDY_strStudyName>" + rs.getString("STUDY_strStudyName") + "</STUDY_strStudyName>" );
                
                    Vector vtSmartformParticipantView = DatabaseSchema.getFormFields("cpatient_smartform_participant_study_view");
                    //Vector vtSmartformView = DatabaseSchema.getFormFields("smartform_view");
            
                
                // build the list of all smartform related to this domain and this participant
                    DALQuery aQuery = new DALQuery();
                    aQuery.setDomain("PATIENT_SMARTFORM", null, null, null);
                    aQuery.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "INNER JOIN");
//                    query.setDomain("STUDYSURVEY", "SMARTFORMPARTICIPANTS_intSmartformID", "STUDYSURVEY_intSurveyID", "LEFT JOIN");
                    aQuery.setDomain("SMARTFORM", "SMARTFORMPARTICIPANTS_intSmartformID", "SMARTFORM_intSmartformID", "LEFT JOIN");
                    aQuery.setFields(vtSmartformParticipantView, null);
                    aQuery.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    aQuery.setWhere("AND", 0, "PATIENT_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    aQuery.setWhere("AND", 0, "PATIENT_SMARTFORM_intStudyID", "=", rs.getString("STUDY_intStudyID"), 0, DALQuery.WHERE_HAS_VALUE);
                    aQuery.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Study", 0, DALQuery.WHERE_HAS_VALUE);
                    aQuery.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intParticipantID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                    aQuery.setOrderBy("SMARTFORMPARTICIPANTS_dtDateAdded", "DESC");
                    aQuery.setOrderBy("SMARTFORMPARTICIPANTS_intSmartformParticipantID", "DESC");
//                    System.out.println(aQuery.convertSelectQueryToString());                
                    ResultSet rsResult = aQuery.executeSelect();
                    strXML.append( QueryChannel.buildSearchXMLFile("smartform", rsResult, vtSmartformParticipantView ));
                    rsResult.close();
                    
                    //rsResult.beforeFirst();
                    
                    
                /*
                DALQuery aQuery = new DALQuery();
                aQuery.setDomain( "PATIENT_SMARTFORM", null, null, null );
                aQuery.setDomain( "SMARTFORM", "SMARTFORM_intSmartformID", "PATIENT_SMARTFORM_intSmartformKey", "INNER JOIN" );
                
                aQuery.setFields( formfields, null );
                
                aQuery.setWhere( null, 0, "PATIENT_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                aQuery.setWhere( "AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                
                aQuery.setDistinctField( "SMARTFORM_intSmartformID" );
                
                ResultSet aRs = aQuery.executeSelect();
                
                strXML.append( QueryChannel.buildSearchXMLFile( "smartform", aRs, formfields));
                aRs.close();
                 
                    */
                strXML.append( "</study>" );
            }
            
            if( (runtimeData.getParameter( "action" ) != null ) && 
                (!runtimeData.getParameter( "action" ).equals(CONSENT))){
                
                lock.addLock( "PATIENT_CONSENT", patientID, LockRecord.READ_ONLY );
            }
            
            rs.close();
            rs = null;
            
            // build Admission Smartfomrs
            strXML.append(this.buildAdmissionSmartforms(patientID));
        }
        catch (Exception e) {
            LogService.instance().log(LogService.ERROR, "Unknown error in Patient Channel - " + e.toString(), e);
        }
        
        return "<smartforms>" + strXML.toString() + "</smartforms>";
        
    }
    
    private boolean isNumeric(String s){
        
        String validChars = "0123456789";
        boolean isNumber = true;
        for (int i = 0; i < s.length() && isNumber; i++){
            char c = s.charAt(i);
            if (validChars.indexOf(c) == -1){
                return false;
            }
        }
        return true;
    }
    
    
    // Gets the new admission number for the patient
    private String getPatientAdmissionNumber (String patientID){

        DALSecurityQuery query;
        String admissionNumber = "1";
            
        try{
            // Count the current number of admissions for the patient
            query = new DALSecurityQuery("patient_admissions", authToken);
            query.setDomain("ADMISSIONS", null, null, null);
            query.setCountField ("ADMISSIONS_intAdmissionkey", true);
            query.setWhere(null, 0, "ADMISSIONS_intPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                        
            ResultSet rsCount = query.executeSelect();
            if(rsCount.next())
            {
                admissionNumber = rsCount.getString("COUNT_ADMN_intAdmissionkey");
            }
            rsCount.close();
            rsCount = null;

        }
        catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
            
        }
        
        return (admissionNumber);

    }
    
    // Calculates the patient's age on date of surgery
    private int getPatientAge (String patientID, String admissionKey){

        DALSecurityQuery query;
        int patientAge = 1;
        java.sql.Date dtSurgery = new java.sql.Date (1000);
            
        try{

            // Get the date of surgery
            query = new DALSecurityQuery("patient_admissions", authToken);
            query.setDomain("ADMISSIONS", null, null, null);
            query.setField ("ADMISSIONS_dtSurgery", null);
            query.setWhere(null, 0, "ADMISSIONS_intPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "ADMISSIONS_intAdmissionkey", "=", admissionKey, 0, DALQuery.WHERE_HAS_VALUE);            
                        
            ResultSet rsResult = query.executeSelect();
            if(rsResult.next())
            {
                dtSurgery = rsResult.getDate("ADMISSIONS_dtSurgery");
            }
            
            rsResult.close();
            rsResult = null;
            
            // Get the DOB for the patient
            query.reset();
            query.setDomain("PATIENT", null, null, null);
            query.setField ("PATIENT_dtDob", null);
            query.setWhere(null, 0, "PATIENT_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                        
            rsResult = query.executeSelect();
            if(rsResult.next())
            {
                java.util.Calendar cal = java.util.Calendar.getInstance();
                if (dtSurgery != null)
                {    
                    cal.setTime (dtSurgery);
                }    
                int currentYear = cal.get(java.util.Calendar.YEAR);
                int currentDay = cal.get(java.util.Calendar.DAY_OF_YEAR);

                Date dtDob = rsResult.getDate("PATIENT_dtDob");
                if (dtDob != null)
                {    
                    cal.setTime(dtDob);
                    int patientYear = cal.get(java.util.Calendar.YEAR);
                    int patientDay = cal.get(java.util.Calendar.DAY_OF_YEAR);

                    // How old will the patient turn on the year of surgery
                    patientAge = currentYear - patientYear;

                    // If the patient has not had a birthday yet, decrement the year
                    if (patientDay > currentDay)
                    {
                        patientAge--;
                    }
                }
                
            }
            rsResult.close();
            

        }
        catch (Exception e) {
            
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());
            
        }
        
        return (patientAge);

    }
    
    public void resetLock(){
        
        try{
                
            if(lock!=null){
                lock.unlock();
            }

            lock = null;
            

        }catch(Exception e){

            e.printStackTrace();

            LogService.instance().log(LogService.ERROR, "Unknown error in CPatients Channel - " + e.toString());

        }finally{
            lock = null;
            lock = new LockRequest( authToken );
            
        }
    }
    
    public StringBuffer disablePatientMenuFields (AuthToken authToken)
    {
        StringBuffer sbDisableBuffer = new StringBuffer();
        Vector vtFormfields;
        sbDisableBuffer.append("<disable>");
        
        //System.out.println ("disablePatientMenuFields] Start ... ");
        try
        {
            vtFormfields = DatabaseSchema.getFormFields( "cpatient_search_details" );
            Enumeration eformfields = vtFormfields.elements();
            
            while (eformfields.hasMoreElements())
            {
                String key = (String)eformfields.nextElement();
                //System.out.println ("disablePatientMenuFields] key = " + key);
                
                if(authToken.hasDenyActivity(key)) 
                {
                    //System.out.println ("disablePatientMenuFields] Disabling this key: " + key);
                    sbDisableBuffer.append("<disable-").append(key).append(">true</disable-").append(key).append(">");
                }
                else
                {
                    sbDisableBuffer.append("<disable-").append(key).append(">false</disable-").append(key).append(">");
                }
            }
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in [CPatient::disablePatientMenuFields] " + e.toString(), e);
            e.printStackTrace();
        }
        
        sbDisableBuffer.append("</disable>");
        //System.out.println ("disablePatientMenuFields] End. Returning: " + sbDisableBuffer.toString());
        return sbDisableBuffer;
    }
 
    public StringBuffer setHighlightMenus()
    {
        StringBuffer sbXML = new StringBuffer();
        String patientID = new String();
        patientID = runtimeData.getParameter("PATIENT_intInternalPatientID");
        sbXML.append("<highlightMenu>");
        
        try
        {
            DALQuery qry = new DALQuery();
            ResultSet rs = null;
            String count = "0";
            
            // look at consent_study
            try
            {
                if (patientID != null)
                {
                    qry.setDomain("CONSENTSTUDY", null, null, null);
                    qry.setDomain( "CONSENT", "CONSENTSTUDY_intConsentKey", "CONSENT_intConsentKey", "INNER JOIN" );
                    qry.setCountField ("CONSENTSTUDY_intConsentStudyKey", true);
                    qry.setWhere(null, 0, "CONSENTSTUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    qry.setWhere("AND", 0, "CONSENT_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                    //System.out.println ("CNST: qry = " + qry.convertSelectQueryToString());

                    rs = qry.executeSelect();
                    if (rs.first())
                    {
                        count = rs.getString(1);
                    }
                }
            }
            catch (Exception e)
            {
                LogService.instance().log(LogService.ERROR, "[CPatient::setHighlightMenus] Consent " + e.toString(), e);
                e.printStackTrace();
            }            
            sbXML.append("<consentCount>").append(count).append("</consentCount>");

            // look at admissions
            sbXML.append(getCount("ADMISSIONS", "intAdmissionkey", "intPatientID", patientID));
            
            // look at appointments            
            sbXML.append(getCount("APPOINTMENTS", "intAppointmentKey", "intInternalPatientID", patientID));
            
            // look at biospecimen
            sbXML.append(getCount("BIOSPECIMEN", "intBiospecimenID", "intPatientID", patientID));
            
            // look at notes
            sbXML.append(getCount("NOTES", "intNotesKey", "intID", patientID));
            
            // look at attachments
            sbXML.append(getCount("ATTACHMENTS", "attachmentkey", "strID", patientID));
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in [CPatient::setHighlightMenus] " + e.toString(), e);
            e.printStackTrace();
        }
        
        sbXML.append("</highlightMenu>");
        //System.out.println ("[CPatient::setHighlightMenus] returns: " + sbXML.toString());
        return sbXML;
    }
    
    /**
     *  PRIVATE FUNCTION - Conditions of use
     *  - Function will count the number of records joined to the patient table
     *  - The join will be based from table; 'domian' joining use the key 'joinkey'
     *  - The count will be based on the formfield: 'countKey' which must exist in the table 'domain'
     *  - The function assumes that you are looking for non deleted entries.
     *  - The function assumes that the table 'domain' has a formfield 'intDeleted' in the schema
     */
    private StringBuffer getCount (String domain, String countKey, String joinKey, String patientID)
    {
        String count = new String ("0");
        StringBuffer sbXML = new StringBuffer();
        DALQuery qry = new DALQuery();
        ResultSet rs = null;
        
        try
        {
            if (patientID != null)
            {
                qry.setDomain(domain, null, null, null);
                qry.setCountField (domain+"_"+countKey, true);
                qry.setWhere(null, 0, domain+"_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                qry.setWhere("AND", 0, domain+"_"+joinKey, "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                //System.out.println (domain + ": qry = " + qry.convertSelectQueryToString());

                rs = qry.executeSelect();

                if (rs.first())
                {
                    count = rs.getString(1);
                }
            }
            sbXML.append("<").append(domain.toLowerCase()).append("Count>").append(count).append("</").append(domain.toLowerCase()).append("Count>");
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "[CPatient::setHighlightMenus] " + domain + e.toString(), e);
            e.printStackTrace();
        }
        return sbXML;
    }
    
    public StringBuffer buildAdmissionSmartforms(String patientID) throws java.sql.SQLException
    {
        StringBuffer sbXML = new StringBuffer();
        StringBuffer sbLink = null;
        String strPatientID = new String();
        String strPatientFullName = new String();
        DALQuery qry = new DALQuery();
        ResultSet rs = null;
        sbXML.append("<admission-smartforms>");
        
        try
        {
            if (PROPERTY_PATIENT_ADMISSION_SMARTFORM)
            {
                sbXML.append("<displayAdmissionsSmartforms>1</displayAdmissionsSmartforms>");

                // get patient Details
                qry.reset();
                qry.setDomain("PATIENT", null, null, null);
                qry.setFields(DatabaseSchema.getFormFields("cpatient_view_patient_details"), null);
                qry.setWhere(null, 0, "PATIENT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                qry.setWhere("AND", 0, "PATIENT_intInternalPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                //System.out.println ("[Patients::buildAdmissionSmartforms] query = " + qry.convertSelectQueryToString());
                rs = qry.executeSelect();
                if (rs.first())
                {
                    strPatientID = rs.getString("PATIENT_strPatientID");
                    if (authToken.hasDenyActivity("PATIENT_strSurname"))
                        strPatientFullName = "######, ";
                    else
                        strPatientFullName = rs.getString("PATIENT_strSurname") + ", ";
                    if(authToken.hasDenyActivity("PATIENT_strFirstName"))
                        strPatientFullName += "######";
                    else
                        strPatientFullName += rs.getString("PATIENT_strFirstName");
                }
                
                qry.reset();
                qry.setDomain("ADMISSIONS", null, null, null);
                qry.setField ("ADMISSIONS_strCollectGrp", null);
                qry.setField ("ADMISSIONS_strEpisodeNo", null);
                qry.setField ("ADMISSIONS_intGroupKey", null);
                qry.setDistinctField ("ADMISSIONS_strCollectGrp");
                qry.setWhere(null, 0, "ADMISSIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                qry.setWhere("AND", 0, "ADMISSIONS_intPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                qry.setOrderBy("ADMISSIONS_strEpisodeNo", "ASC");
                qry.setOrderBy("ADMISSIONS_strCollectGrp", "ASC");
                // System.out.println ("[Patients::buildAdmissionSmartforms] query = " + qry.convertSelectQueryToString());
                rs = qry.executeSelect();
                rs.beforeFirst();
                
                while (rs.next())
                {
                    // Need to get a unique number for the participant ID
                    // Must be unique for each patient, episode and collection group => Episode no * 1e8 + coll Grp * 1e6 + patID
                    // int intAdmKey = Integer.parseInt(rs.getString("ADMISSIONS_strEpisodeNo")) * 100000000 + Integer.parseInt(rs.getString("ADMISSIONS_strCollectGrp")) * 1000000 + Integer.parseInt(patientID);
                    
                    if (rs.getString("ADMISSIONS_intGroupKey") != null)
                    {
                        sbXML.append("<admissions>");
                        sbXML.append("<ADMISSIONS_strCollectGrp>").append(rs.getString("ADMISSIONS_strCollectGrp")).append("</ADMISSIONS_strCollectGrp>");
                        sbXML.append("<ADMISSIONS_strEpisodeNo>").append(rs.getString("ADMISSIONS_strEpisodeNo")).append("</ADMISSIONS_strEpisodeNo>");
                        sbXML.append("<ADMISSIONS_intPatientID>").append(patientID).append("</ADMISSIONS_intPatientID>");
                        sbXML.append("<ADMISSIONS_strPatientFullName>").append(strPatientFullName).append("</ADMISSIONS_strPatientFullName>");

                        // Define Smartform Link
                        sbLink = new StringBuffer();
                        sbLink.append("&domain=Admissions&participant=").append(rs.getString("ADMISSIONS_intGroupKey"));
                        sbLink.append("&strcurrent=admissions_view");
                        sbLink.append("&ADMISSIONS_intPatientID=").append(patientID);
                        sbLink.append("&strOrigin=patient_view&title=").append(rs.getString("ADMISSIONS_strCollectGrp"));
                        sbLink.append("&var1=").append(strPatientID).append("&fromPatient=true");
                        sbXML.append("<admissionsSmartformLink>").append(Utilities.cleanForXSL(sbLink.toString())).append("</admissionsSmartformLink>");
                        sbXML.append("</admissions>");
                    }
                }
            }
            else
            {
                sbXML.append("<displayAdmissionsSmartforms>0</displayAdmissionsSmartforms>");
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "[CPatient::buildAdmissionSmartforms] " + e.toString(), e);
            e.printStackTrace();
        }
        finally
        {
            if (rs!=null)
                rs.close();
        }
        sbXML.append("</admission-smartforms>");
        //System.out.println ("[CPatient::buildAdmissionSmartforms] returns = " + sbXML);
        return sbXML;
    }
    
    public void doAdmissionSmartformsDelete(String patientID, String admissionID) throws java.sql.SQLException
    {
        DALQuery qry = new DALQuery();
        ResultSet rs = null;
        String strCollectionGroup = new String();
        String strEpisdoeNum = new String();
        int intNumberDistinctGroups = -1;
        
        try
        {
            if (PROPERTY_PATIENT_ADMISSION_SMARTFORM)
            {
                // get the strCollectionGroup and strEpisdoeNum
                qry.reset();
                qry.setDomain ("ADMISSIONS", null, null, null);
                qry.setField ("ADMISSIONS_strCollectGrp", null);
                qry.setField ("ADMISSIONS_strEpisodeNo", null);
                qry.setWhere(null, 0, "ADMISSIONS_intAdmissionkey", "=", admissionID, 0, DALQuery.WHERE_HAS_VALUE);
                //System.out.println ("[Patients::doDeleteAdmissionSmartforms] query1 = " + qry.convertSelectQueryToString());
                rs = qry.executeSelect();
                rs.beforeFirst();

                if (rs.next())
                {
                    strCollectionGroup = rs.getString("ADMISSIONS_strCollectGrp");
                    strEpisdoeNum = rs.getString("ADMISSIONS_strEpisodeNo");
                }
                rs.close();
                rs = null;

                // find the number of distinct collection groups
                qry.reset();
                qry.setDomain ("ADMISSIONS", null, null, null);
                qry.setCountField("ADMISSIONS_intAdmissionkey", false);
                qry.setWhere(null, 0, "ADMISSIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                qry.setWhere("AND", 0, "ADMISSIONS_intPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                qry.setWhere("AND", 0, "ADMISSIONS_strCollectGrp", "=", strCollectionGroup, 0, DALQuery.WHERE_HAS_VALUE);
                qry.setWhere("AND", 0, "ADMISSIONS_strEpisodeNo", "=", strEpisdoeNum, 0, DALQuery.WHERE_HAS_VALUE);
                //System.out.println ("[Patients::doDeleteAdmissionSmartforms] query2 = " + qry.convertSelectQueryToString());
                rs = qry.executeSelect();
                rs.beforeFirst();

                if (rs.next())
                {
                    intNumberDistinctGroups = rs.getInt(1);
                }
                rs.close();
                rs = null;

                // delete from ix_collection_group
                if (intNumberDistinctGroups == 1)
                {
                    qry.reset();
                    qry.setDomain ("COLLECTIONGROUP", null, null, null);
                    qry.setField( "COLLECTIONGROUP_intDeleted", "-1" );
                    qry.setWhere(null, 0, "COLLECTIONGROUP_intPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                    qry.setWhere("AND", 0, "COLLECTIONGROUP_strCollectGrp", "=", strCollectionGroup, 0, DALQuery.WHERE_HAS_VALUE);
                    qry.setWhere("AND", 0, "COLLECTIONGROUP_strEpisodeNo", "=", strEpisdoeNum, 0, DALQuery.WHERE_HAS_VALUE);
                    //System.out.println ("[Patients::doDeleteAdmissionSmartforms] query3 = " + qry.convertSelectQueryToString());
                    qry.executeUpdate();
                }
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "[CPatient::doDeleteAdmissionSmartforms] " + e.toString(), e);
            e.printStackTrace();
        }
        finally
        {
            if (rs!=null)
                rs.close();
        }
    }
    
    public void doAdmissionSmartformsAdd(String patientID, String admissionID, String strCollectionGroup, String strEpisdoeNum) throws java.sql.SQLException
    {
        DALQuery qry = new DALQuery();
        ResultSet rs = null;
        String newCollGroupKey = new String();
        int intNumberDistinctGroups = -1;
        
        try
        {
            if (PROPERTY_PATIENT_ADMISSION_SMARTFORM)
            {
                // check to see if there exists a unique episodenum/collectiongroup combo in ix_collection_group
                qry.reset();
                qry.setDomain ("COLLECTIONGROUP", null, null, null);
                qry.setCountField("COLLECTIONGROUP_intGroupKey", false);
                qry.setWhere(null, 0, "COLLECTIONGROUP_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                qry.setWhere("AND", 0, "COLLECTIONGROUP_intPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                qry.setWhere("AND", 0, "COLLECTIONGROUP_strCollectGrp", "=", strCollectionGroup, 0, DALQuery.WHERE_HAS_VALUE);
                qry.setWhere("AND", 0, "COLLECTIONGROUP_strEpisodeNo", "=", strEpisdoeNum, 0, DALQuery.WHERE_HAS_VALUE);
                //System.out.println ("[Patients::doAddAdmissionSmartforms] query2 = " + qry.convertSelectQueryToString());
                rs = qry.executeSelect();
                rs.beforeFirst();

                if (rs.next())
                {
                    intNumberDistinctGroups = rs.getInt(1);
                }
                rs.close();
                rs = null;
                
                // none exists - add
                if (intNumberDistinctGroups == 0)
                {
                    qry.reset();
                    qry.setDomain ("COLLECTIONGROUP", null, null, null);
                    qry.setField( "COLLECTIONGROUP_intPatientID", patientID);
                    qry.setField( "COLLECTIONGROUP_strCollectGrp", strCollectionGroup);
                    qry.setField( "COLLECTIONGROUP_strEpisodeNo", strEpisdoeNum);
                    //System.out.println ("[Patients::doDeleteAdmissionSmartforms] query3 = " + qry.convertSelectQueryToString());
                    qry.executeInsert();
                    newCollGroupKey = QueryChannel.getNewestKeyAsString(qry);
                }
                
                // if the insert did not take place still determine the COLLECTIONGROUP_intGroupKey
                if (newCollGroupKey.length()==0)
                {
                    qry.reset();
                    qry.setDomain ("COLLECTIONGROUP", null, null, null);
                    qry.setField( "COLLECTIONGROUP_intGroupKey", null);
                    qry.setWhere(null, 0, "COLLECTIONGROUP_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    qry.setWhere("AND", 0, "COLLECTIONGROUP_intPatientID", "=", patientID, 0, DALQuery.WHERE_HAS_VALUE);
                    qry.setWhere("AND", 0, "COLLECTIONGROUP_strCollectGrp", "=", strCollectionGroup, 0, DALQuery.WHERE_HAS_VALUE);
                    qry.setWhere("AND", 0, "COLLECTIONGROUP_strEpisodeNo", "=", strEpisdoeNum, 0, DALQuery.WHERE_HAS_VALUE);
                    //System.out.println ("[Patients::doDeleteAdmissionSmartforms] query4 = " + qry.convertSelectQueryToString());
                    rs = qry.executeSelect();
                    rs.beforeFirst();

                    if (rs.next())
                    {
                        newCollGroupKey = rs.getString("COLLECTIONGROUP_intGroupKey");
                    }
                    rs.close();
                    rs = null;
                }
                
                // update ix_admissions with ADMISSIONS_intGroupKey = COLLECTIONGROUP_intGroupKey
                qry.reset();
                qry.setDomain ("ADMISSIONS", null, null, null);
                qry.setField( "ADMISSIONS_intGroupKey", newCollGroupKey);
                qry.setWhere(null, 0, "ADMISSIONS_intAdmissionkey", "=", admissionID, 0, DALQuery.WHERE_HAS_VALUE);
                qry.executeUpdate();
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "[CPatient::doAddAdmissionSmartforms] " + e.toString(), e);
            e.printStackTrace();
        }
        finally
        {
            if (rs!=null)
                rs.close();
        }
    }
    
    public void doAdmissionSmartformsEdit(String patientID, String admissionID, ChannelRuntimeData rd) throws java.sql.SQLException
    {
        DALQuery qry = new DALQuery();
        ResultSet rs = null;
        String strOldCollectionGroup = new String();
        String strOldEpisdoeNum = new String();
        String strNewCollectionGroup = new String(rd.getParameter("ADMISSIONS_strCollectGrp"));
        String strNewEpisdoeNum = new String(rd.getParameter("ADMISSIONS_strEpisodeNo"));
        int intNumberDistinctGroups = -1;
        
        try
        {
            if (PROPERTY_PATIENT_ADMISSION_SMARTFORM)
            {
                // get the old strCollectionGroup and strEpisdoeNum
                qry.reset();
                qry.setDomain ("ADMISSIONS", null, null, null);
                qry.setField ("ADMISSIONS_strCollectGrp", null);
                qry.setField ("ADMISSIONS_strEpisodeNo", null);
                qry.setWhere(null, 0, "ADMISSIONS_intAdmissionkey", "=", admissionID, 0, DALQuery.WHERE_HAS_VALUE);
                //System.out.println ("[Patients::doAddAdmissionSmartforms] query1 = " + qry.convertSelectQueryToString());
                rs = qry.executeSelect();
                rs.beforeFirst();

                if (rs.next())
                {
                    strOldCollectionGroup = rs.getString("ADMISSIONS_strCollectGrp");
                    strOldEpisdoeNum = rs.getString("ADMISSIONS_strEpisodeNo");
                }
                rs.close();
                rs = null;
                
                // if no old data then add a new record
                if ((strOldCollectionGroup == null) || (strOldEpisdoeNum == null))
                {
                    this.doAdmissionSmartformsAdd(patientID, admissionID, strNewCollectionGroup, strNewEpisdoeNum);
                }
                
                // check to see if CollectionGroup or EpisodeNum it is different
                else if (!strOldCollectionGroup.equalsIgnoreCase(strNewCollectionGroup) || 
                        !strOldEpisdoeNum.equalsIgnoreCase(strNewEpisdoeNum))
                {
                    this.doAdmissionSmartformsDelete(patientID, admissionID);
                    this.doAdmissionSmartformsAdd(patientID, admissionID, strNewCollectionGroup, strNewEpisdoeNum);
                }
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "[CPatient::doAddAdmissionSmartforms] " + e.toString(), e);
            e.printStackTrace();
        }
        finally
        {
            if (rs!=null)
                rs.close();
        }
    }
}