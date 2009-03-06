/**
 * Copyright (C) Neuragenix Pty Ltd, 2004
 *
 * IntelligentSearch.java
 *
 * Created on 14 December 2004, 18:36
 *
 * @author Daniel Murley
 *
 * Purpose : Refactor of original ISearch into a reusable cross channel module
 *           Please see Anita re: design questions/CISearch
 *                      Daniel re: usage questions for channels other than CISearch
 */

/*
 *  Modified 26 Feb, 2005
 *    - Adding forced domains
 *    - Adding forced fields
 *    - Adding picker mode
 *    - adding picker code
 */
 
 

package neuragenix.genix.isearch;

import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.io.*;
import java.sql.*;

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
import neuragenix.common.Utilities;
import neuragenix.common.LockRequest;
import neuragenix.common.LockRecord;
import neuragenix.common.QueryChannel;
import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.DBField;
import neuragenix.dao.DALQuery;

import neuragenix.security.exception.SecurityException;
import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.SessionManager;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.dao.DALQuery;
import neuragenix.common.QueryChannel;
import java.io.InputStream;
import java.sql.*;

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NotContextException;

import javax.xml.parsers.DocumentBuilder; 
import javax.xml.parsers.DocumentBuilderFactory;  
import javax.xml.parsers.FactoryConfigurationError;  
import javax.xml.parsers.ParserConfigurationException;
 
import org.xml.sax.SAXException;  
import org.xml.sax.SAXParseException;  

import org.w3c.dom.Document;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.NamedNodeMap;



 
public class IntelligentSearch 
{
    
    //Directory to store data for export
    private static String EXPORT_DIRECTORY = null;
    private static boolean ORDER_BY_BIOSPECIMEN = false;    
    private static int CACHE_PAGE_SIZE = 50;    
    private static Hashtable hashNonDisplayFields = new Hashtable();
    
    // pages
    // pages
    private final String ISEARCH_MENU = "isearch_menu";
    private final String BUILD_ISEARCH = "build_isearch";
    private final String BUILD_ISEARCH_NORMAL = "build_isearch_normal";
    private final String BUILD_ISEARCH_SIMPLIFIED = "build_isearch_simplified";
    private final String ISEARCH_RESULT = "isearch_result";
    private final String SAVE_QUERY = "save_query";
    private final String SECURITY = "security"; 
    private final int EXPORT = 1;
    private final int EXPORT_SMARTFORM = 2;
    
    // search activities
    private final String DO_ISEARCH = "do_isearch";
    
    // channel's runtime & static data
    private ChannelStaticData staticData;
    protected ChannelRuntimeData runtimeData;
    private ChannelRuntimeData oldRuntimeData;
        
    private AuthToken authToken ;
    private String strSessionUniqueID;
    private String strActivity;
    private String strCurrent;
    private String strXML = "";
    private String strPreviousXML = "";
    private String strStylesheet;
    private String strFileName = "";
    private boolean firstTime = false;
    protected String strErrorMessage = "";
    private boolean blHasDomain = true;
    private boolean blFirstTimeDoSearch = true; 
    private boolean blSubQuery = false;
    private boolean blPerformExport = false;
    private String strDelimiter = ",";
    private boolean blExplicitFields = false;
    private boolean blPartBioAnalysis = false;
    private boolean blPerformSearch = false;
    private String strDisplayedQueryID;   
    private String strDisplayMode = "advanced";
    
    
    
    /* Picker Mode - Will show check boxes in the XML*/
    private boolean blPickerMode = false;
    
    
    // To capture the first submit after clicking a Query name(i.e Edit)
    private boolean blDoneEditFirstSubmit = false;
   
    private Vector vtDomainList = new Vector(10, 2);
    protected Vector vtSelectedDomains = new Vector(5, 2);
    protected Vector vtSearchFormFields = new Vector(10, 5);
    protected Vector vtJoin = new Vector(10,2);
    protected Vector vtDomainVal = new Vector(10,2);
    protected Vector vtField = new Vector(10,2); 
    protected Vector vtOp = new Vector(10,2); 
    protected Vector vtSearchVal = new Vector(10,2); 
    private Vector vtFieldLabel = new Vector(10,2);
    protected Vector vtOpenBracket = new Vector(10,2);
    protected Vector vtCloseBracket = new Vector(10,2);
    protected Vector vtParticipantDomain = new Vector(10,2);
    private Vector vtCriteriasDomains = new Vector(10,2);
    protected Hashtable hashCriteriaDomains = new Hashtable();
    private Hashtable hashDomainStatus = new Hashtable();
    private Hashtable hashDomainNames = new Hashtable();    
    private Vector vtSmartformList = new Vector(10,2);
    protected Vector vtSelectedSmartforms = new Vector(10,2);  
    private Vector vtStudyList = new Vector(10, 2);
    protected Vector vtSelectedStudys = new Vector(10, 2);
    protected Vector vtSelectedDataelements = new Vector(10,2);
    protected Vector vtSelectedSFDataelements = new Vector(10,2);
    private Vector vtDomains = new Vector(10, 2);
    private Vector vtMissingFor = new Vector(10, 2);
    private Vector vtMissingWithout = new Vector(10, 2);
    private Vector vtMissingSF = new Vector(10, 2);
    private Vector vtMissingSFDomain = new Vector(10, 2);
    private Vector vtMissingSFField = new Vector(10, 2);
    private Vector vtSearchResult = new Vector(100, 50); 

    private Vector vtRequiredDomains = new Vector(10, 2);
    private Vector vtRequiredFields = new Vector(10, 2);
    private Vector vtRequiredFieldDomains = new Vector(10, 2);
    // Holds the selected  dataelements to be output in the export file
    private Vector vtExportDataElements = new Vector( 10, 2);
    
    private Hashtable htFlaggedItems;
    
    
    // to Store the start record for each page
    private Hashtable hashStartRecordForPage = new Hashtable(); 
    private Hashtable hashVisibility = new Hashtable();
    private Hashtable hashPrimaryKey = new Hashtable();
    
    // To keep track of the bioanalysis smartform criteria
    private Hashtable hashBioAnalysisSFCriteria = new Hashtable();
    
    // Variables 
    private String strDomainSel = "";     
    private String strFieldSel = "";
    private String strSelectedDomainSel = "";
    private String strRadioVal = "";
    private String strQueryID = "";
    private String strQueryName = "";   
    private String strBioSF = "";
    private String strMissingForSel = "";
    private String strMissingWithoutSel = "";
    private String strMissingSFDomainSel = "";
    private String strMissingSFFieldSel = "";
    
    // Start Secure Download Implementation    
    private String nodeId;
    // End Secure
    
    // if ParticipantDomain is BIOSPECIMEN / Not
    private boolean blPartBio = false;
       
    
    /** Lock request object to handle record locking
     */
    private LockRequest lockRequest = null;    
    
    //paging
    private int intStartRecord = 0;
    private int intRecordPerPage = Integer.parseInt(PropertiesManager.getProperty("neuragenix.bio.RecordPerPage"));
    private int intMaxPage = 0;
    private int intCurrentPage = 1;
    private int intRecordSetSize = 0;
    private int intHiddenStartRecord = 0;
    private int intHiddenRecordPerPage = 0;
    private int intHiddenMaxPage = 0;
    private int intHiddenCurrentPage = 1;
    private int intFirstDataRow = 0;
    private int intLastDataRow = 0;
    private int intRequiredStartRow = 0;
    private int intRequiredEndRow = 0;    
    private int intMaxCachePage = 0;
    private int intMinCachePage = 0;
    // The total number of data rows available from the resultset
    private int intTotalResultRows = 0;
    private int intSmartformColumn = 0;
    private boolean blFinishButtonVisible = false;
    private String strFinishButtonLabel = "";
    private BufferedWriter ExportFile;
    
    private boolean blCriteriaAdded = false;
    
    protected static boolean FILTER_BIOSPECIMEN_BY_STUDY = true;
    private static boolean PROPERTY_BIOANALYSISSF_SEP_LINE = true;  
    private static boolean PROPERTY_COLLECTIONSF_SEP_LINE = true;
    
    /** Creates a new instance of IntelligentSearch */
    public IntelligentSearch() 
    {
         this.strStylesheet = BUILD_ISEARCH;
    }
    
    static
    {
        hashNonDisplayFields.put("PATIENT_intInternalPatientID", "PATIENT_intInternalPatientID");
        hashNonDisplayFields.put("BIOSPECIMEN_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID");
        hashNonDisplayFields.put("ADMISSIONS_intAdmissionkey", "ADMISSIONS_intAdmissionkey");
        hashNonDisplayFields.put("ADMISSIONS_intGroupKey", "ADMISSIONS_intGroupKey");
        hashNonDisplayFields.put("SMARTFORMPARTICIPANTS_intSmartformParticipantID", "SMARTFORMPARTICIPANTS_intSmartformParticipantID");
        hashNonDisplayFields.put("DATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID");
        hashNonDisplayFields.put("STUDY_intStudyID", "STUDY_intStudyID");
        hashNonDisplayFields.put("SMARTFORM_strSmartformName", "SMARTFORM_strSmartformName");
        hashNonDisplayFields.put("SMARTFORMPARTICIPANTS_intParticipantID", "SMARTFORMPARTICIPANTS_intParticipantID");
        hashNonDisplayFields.put("BIOSPECIMEN_TRANSACTIONS_intBioTransactionID", "BIOSPECIMEN_TRANSACTIONS_intBioTransactionID");
        
        try
        {
            EXPORT_DIRECTORY = PropertiesManager.getProperty("neuragenix.bio.search.ExportFileLocation");
        }
        catch(Exception e)
        {
            System.out.println ("[IntelligentSearch (CRITICAL)] Property neuragenix.bio.search.ExportFileLocation not present.");
        }
        
        try
        {
            ORDER_BY_BIOSPECIMEN = PropertiesManager.getPropertyAsBoolean("neuragenix.genix.isearch.orderByBiospecimen");
        }
        catch(Exception e)
        {
            System.out.println ("[IntelligentSearch] Property neuragenix.genix.isearch.orderByBiospecimen not present, default to false");
        }
        
        try
        {
            CACHE_PAGE_SIZE = PropertiesManager.getPropertyAsInt("neuragenix.genix.isearch.cachePageSize");
        }
        catch(Exception e)
        {
            System.out.println ("[IntelligentSearch] Property neuragenix.genix.isearch.cachePageSize not present, default to 50");
            CACHE_PAGE_SIZE = 50;
        }
        
        try
        {
            FILTER_BIOSPECIMEN_BY_STUDY = PropertiesManager.getPropertyAsBoolean("neuragenix.genix.isearch.FilterBiospecimenByStudy");
        }
        catch(Exception e)
        {
            System.out.println ("[IntelligentSearch] Property neuragenix.genix.isearch.FilterBiospecimenByStudy not present, default to true");
            FILTER_BIOSPECIMEN_BY_STUDY = true;
        }                

        try
        {
            PROPERTY_BIOANALYSISSF_SEP_LINE = PropertiesManager.getPropertyAsBoolean("neuragenix.genix.isearch.BioanalysisSFResultsSepLine");
        }
        catch(Exception e)
        {
            System.out.println ("[IntelligentSearch] Property neuragenix.genix.isearch.BioanalysisSFResultsSepLine not present, default to true");
            PROPERTY_BIOANALYSISSF_SEP_LINE = true;
        }
        
        try
        {
            PROPERTY_COLLECTIONSF_SEP_LINE = PropertiesManager.getPropertyAsBoolean("neuragenix.genix.isearch.CollectionSFResultsSepLine");
        }
        catch(Exception e)
        {
            System.out.println ("[IntelligentSearch] Property neuragenix.genix.isearch.CollectionSFResultsSepLine not present, default to true");
            PROPERTY_COLLECTIONSF_SEP_LINE = true;
        }                
    }
    
    public IntelligentSearch(ChannelStaticData sd)
    {
        firstTime = true;
        this.staticData = sd;
        this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");
        
        // create domains & formfields for this channel
        InputStream file;
        
        file = CISearch.class.getResourceAsStream("ISearchRelatedSchema.xml");
        DatabaseSchema.loadDomains(file, "ISearchRelatedSchema.xml");     
        
        file = CISearch.class.getResourceAsStream("ISearchDBSchema.xml");
        DatabaseSchema.loadSearchDomains(file, "ISearchDBSchema.xml"); 
        
        // Start Secure Download Implementation    

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

        try 
        {
            strSessionUniqueID = authToken.getSessionUniqueID();
            SessionManager.addSession(strSessionUniqueID);
            SessionManager.addChannelID(strSessionUniqueID, "CDownload", (String) globalIDContext.lookup("CDownload"));            
        }
        catch (NotContextException nce)
        {
            LogService.log(LogService.ERROR, "Could not find channel ID for fname=Download");
        } 
        catch (NamingException e) 
        {
            LogService.log(LogService.ERROR, e);
        }

        
        // create a document factory instance
        DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
        Document document;
        
        // This portion retrieves from the schema file, the display names for the domains
        // This is held within the <domainDisplayName> tags
        // The elements in the tag specify the actual domain name and the display name that
        // should appear in the search channel
        
        try 
        {
            
            file = CISearch.class.getResourceAsStream("ISearchDBSchema.xml");
            // parse the XML file
            DocumentBuilder builder = factory.newDocumentBuilder();
            document = builder.parse(file);
            
                        
            // Get the alternative names for the domains
            NodeList domainDisplayNamesNodes = document.getElementsByTagName("domainDisplayNames");
            for (int index=0; index < domainDisplayNamesNodes.getLength(); index++)
            {
                Node currentDomainDisplayNames = domainDisplayNamesNodes.item(index);
                NodeList domainNameNodes = currentDomainDisplayNames.getChildNodes();

                
                for (int index1=0; index1 < domainNameNodes.getLength(); index1++)
                {
                    Node currentDomain = domainNameNodes.item(index1);
                    if (currentDomain.getNodeType() == Node.ELEMENT_NODE)
                    {
                        NamedNodeMap domainAttributes = currentDomain.getAttributes();
                        hashDomainNames.put(domainAttributes.getNamedItem("name").getNodeValue(), domainAttributes.getNamedItem("display").getNodeValue() );
                    }
                }                                
            }        
                        
            
            // For debug
            //Enumeration enum = hashDomainNames.keys();
            
            //while (enum.hasMoreElements())
            //{
            //    String domain = (String) enum.nextElement();
            //    System.out.println ("DOMAIN DISPLAY NAMES");                
            //    System.out.println ("DOMAIN" + domain + " DISPLAY NAME" + hashDomainNames.get(domain));
            //}
                
                
        }
        catch (Exception e)
        {
            LogService.log(LogService.ERROR, e);
        }
        
    }
    
    
    public String getCurrentXML ()
    {
        return strXML;
    }
    
    public String getCurrentStylesheet()
    {
        return strStylesheet;
    }
    
    
    public void setPickerMode(boolean mode)
    {
        if (mode == true)
        {
            htFlaggedItems = new Hashtable(100);
            
        }
        else
        {
            htFlaggedItems = null;
        }
        blPickerMode = mode;
    }
    
    
    
    
    
    
    public String processRuntimeData(ChannelRuntimeData rd)
    {
        try
        {
            this.runtimeData = rd;
            strErrorMessage = "";
            blPerformSearch = false;
            blCriteriaAdded = false;

            // Get the current page the users is on
            if(runtimeData.getParameter("current") != null)
            {
                firstTime = false;
                strCurrent = runtimeData.getParameter("current");
            }
            else if (firstTime)
            {
                LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
                firstTime = false;
                strCurrent = BUILD_ISEARCH;
            }
            else
            {
                LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
                strCurrent = null;
            }
            
            // if the user doesn't have permission to do search
            // display unauthorization message
            if (!authToken.hasActivity(DO_ISEARCH))
            {
                strXML  = buildSecurityErrorXMLFile("isearch");
                strStylesheet = SECURITY;
                return null;
            }
            
            if (strCurrent != null)
            {
                // user entered first time
                if (strCurrent.equals(BUILD_ISEARCH)){             
                    
                    // Clear anything in search result vector
                    vtSearchResult.clear();
                    
                    hashBioAnalysisSFCriteria.clear();
                    
                    // Set to explicit field selection to false
                    blExplicitFields = false;
                    
                    strStylesheet = BUILD_ISEARCH;     
                    
                    if (runtimeData.getParameter("rdDisplayAs") != null)
                    {
                        strDisplayMode = runtimeData.getParameter("rdDisplayAs");
                    }    
                    
                    // Set the stylesheet based on the display mode
                    if (strDisplayMode.equals("normal"))
                    {
                        strStylesheet = BUILD_ISEARCH_NORMAL;
                    }
                    else if (strDisplayMode.equals("simplified"))
                    {
                        strStylesheet = BUILD_ISEARCH_SIMPLIFIED;
                    }
                    
                   
                    // Get the selected domains, load field accordingly and 
                    // get the selected field and show the criteria's value accordingly
                    strXML = buildISearch(); 
                    
                    // Capture the latest selected fields for all domains expanded
                    buildSelectedFieldsForDomainsExpanded();
                    
                    // If domain is selected and action performed
                    // in EDIT mode , when page is submitted or any other submit action
                    if( strQueryID != null && !strQueryID.equals("")){    
                          
                            // build the search criteria on each submit
                            buildEditCriteriaVectors();
                            
                            // build the missing records criterias
                            buildEditMissingCriteriasVectors();
                            
                            // build the missing smartform records criterias
                            buildEditMissingSFCriteriasVectors();

                            // Build the status of each domain - if it is Expanded or Collapsed
                            //buildEditDomainStatus();         
                                
                    }                    
                    
                        
                    // If Add is clicked
                    if(runtimeData.getParameter("add") != null){
                       
                        addSelectedDomain();                                  
                        // Rebuild the form at the end of the actions to identify the dataset dropdown display
                        strXML = buildISearch();     
                        
                    }else if(runtimeData.getParameter("remove") != null)
                    {                        
                        if (!vtRequiredDomains.contains(strSelectedDomainSel))    
                        {
                            removeRelatedJoin(strSelectedDomainSel);                          
                            // Rebuild the form at the end of the actions
                            strXML = buildISearch(); 
                        }
                        
                    }
                    else if(runtimeData.getParameter("remove_all") != null)
                    {
                        removeAllSelectedDomains();
                        reloadForcedDomains();
                        // Rebuild the form at the end of the actions
                        strXML = buildISearch(); 
                        
                    }            
                    // Add Criteria clicked
                    else if(runtimeData.getParameter("addCriteria") != null){
                        
                        addCriteria();                         
                        // Rebuild the form at the end of the actions
                        strXML = buildISearch();
                        
                    }
                    // Edit Criteria
                    else if(runtimeData.getParameter("target") != null){  
                        
                        // Rebuild the form at the end of the actions
                        strXML = buildISearch(); 
                        
                    }
                    // Update Criteria
                    else if(runtimeData.getParameter("updateCriteria") != null){  
                        
                        updateCriteria();
                        // Rebuild the form at the end of the actions
                        strXML = buildISearch(); 
                        
                    }
                    // Delete Criteria
                    else if(runtimeData.getParameter("delete") != null){
                        
                        deleteCriteria();
                        strXML = buildISearch(); 
                        
                    }                      
                    // When Criterias domain is expanded - build its fields
                    else if(runtimeData.getParameter("expanded") != null){
                        
                        // update the status for the hidden field
                        runtimeData.setParameter("status"+runtimeData.getParameter("criteriasDomain"), "expanded");                        
                        hashDomainStatus.put(runtimeData.getParameter("criteriasDomain"), "expanded");                 
                        
                    }
                    // When Criterias domain is collapsed - do not display its fields
                    else if(runtimeData.getParameter("collapsed") != null){
                        
                        // update the status for the hidden field
                        runtimeData.setParameter("status"+runtimeData.getParameter("criteriasDomain"), "collapsed");
                        hashDomainStatus.put(runtimeData.getParameter("criteriasDomain"), "collapsed");                   
                       
                    }
                    // When search Criteria is expanded - display its details
                    else if(runtimeData.getParameter("showVisibility1") != null){
                        
                        // update the status for the hidden field
                        runtimeData.setParameter("statusVisibility1", "expanded");                        
                        hashVisibility.put("visibility1", "expanded");         
                        
                    }
                    // When search Criteria is collapsed - do not display its details
                    else if(runtimeData.getParameter("hideVisibility1") != null){
                        
                        // update the status for the hidden field
                        runtimeData.setParameter("statusVisibility1", "collapsed");
                        hashVisibility.put("visibility1", "collapsed");                      
                       
                    }
                    // When search Missing Criteria is expanded - display its details
                    else if(runtimeData.getParameter("showVisibility2") != null){
                        
                        // update the status for the hidden field
                        runtimeData.setParameter("statusVisibility2", "expanded");                        
                        hashVisibility.put("visibility2", "expanded");          
                        
                    }
                    // When search missing Criteria is collapsed - do not display its details
                    else if(runtimeData.getParameter("hideVisibility2") != null){
                        
                        // update the status for the hidden field
                        runtimeData.setParameter("statusVisibility2", "collapsed");
                        hashVisibility.put("visibility2", "collapsed");      
                        
                    }                    
                    // When search Missing Smartform Criteria is expanded - display its details
                    else if(runtimeData.getParameter("showVisibility3") != null){
                        
                        // update the status for the hidden field
                        runtimeData.setParameter("statusVisibility3", "expanded");                        
                        hashVisibility.put("visibility3", "expanded");          
                        
                    }
                    // When search Missing Smartform Criteria is collapsed - do not display its details
                    else if(runtimeData.getParameter("hideVisibility3") != null){
                        
                        // update the status for the hidden field
                        runtimeData.setParameter("statusVisibility3", "collapsed");
                        hashVisibility.put("visibility3", "collapsed");      
                        
                    }                    
                    // When Perform Search is clicked
                    else if(runtimeData.getParameter("performSearch") != null){                         
                        
                        // Capture the current runtimeData to get back to the same page
                        oldRuntimeData = runtimeData;
                        
                        // default current page is the first page
                        blFirstTimeDoSearch = true;
                        intHiddenCurrentPage = 1;          
                        intCurrentPage = 1;
                        intFirstDataRow = 0;
                        intLastDataRow = 0;
                        intRequiredStartRow = 0;
                        intRequiredEndRow = CACHE_PAGE_SIZE * intRecordPerPage;
                        intMaxCachePage = CACHE_PAGE_SIZE;
                        intMinCachePage = 0;        
                        
                        blPerformSearch = true;
                        strXML+=buildSearchResultXMLFile();
                    }
                    // Delete Query is clicked
                    else if( runtimeData.getParameter("delete_query") != null){                        
                        
                         deleteQuery(strDisplayedQueryID);
                        strXML = buildNewSearch();
                        
                    }              
                    // When New Search is clicked
                    else if(runtimeData.getParameter("addNew") != null){
                        
                        strXML = buildNewSearch();
                        
                    }          
                    // When Add Criteria is clicked for Searching Missing Records
                    else if( runtimeData.getParameter("addMissingCriteria") != null){  
                        
                        addCriteriaForMissingRecords();
                        
                    }
                    // When Edit Criteria is clicked for Searching Missing Records
                    else if( runtimeData.getParameter("targetMissing") != null ){
                        strXML = buildISearch();
                        
                    }                    
                    // When update Criteria is clicked for Searching Missing Records
                    else if( runtimeData.getParameter("updateMissingCriteria") != null ){
                        
                        updateCriteriaForMissingRecords();
                        
                    }
                    // When delete Criteria is clicked for Searching Missing Records
                    else if( runtimeData.getParameter("deleteMissingCriteria") != null ){
                        
                        deleteCriteriaForMissingRecords();
                        
                    }                   
                    // When Add Criteria is clicked for Searching Missing Records
                    else if( runtimeData.getParameter("addMissingSFCriteria") != null){  
                        
                        addCriteriaForMissingSFRecords();
                        
                    }
                    // When Edit Criteria is clicked for Searching Missing Records
                    else if( runtimeData.getParameter("targetMissingSF") != null ){
                        
                        strXML = buildISearch();
                        
                    }                    
                    // When update Criteria is clicked for Searching Missing Records
                    else if( runtimeData.getParameter("updateMissingSFCriteria") != null ){
                        
                        updateCriteriaForMissingSFRecords();
                        
                    }
                    // When delete Criteria is clicked for Searching Missing Records
                    else if( runtimeData.getParameter("deleteMissingSFCriteria") != null ){
                        
                        deleteCriteriaForMissingSFRecords();
                        
                    }                   
                   
                    // Display the ISearch form with the domains loaded  
                    // And load the latest selected domains, fields, smartforms etc.,                                                  
                    strXML+= buildXML();
                    
                    
                     // """"""""""""""Do this after building the complete strXML
                     // When Save Query is clicked
                     if(runtimeData.getParameter( SAVE_QUERY ) != null){
                        
                         saveQuery();
                     }
                     // Edit a Query - first edit click 
                     else if( runtimeData.getParameter("edit") != null ){            
                       
                        strXML = displayQueryDetails( runtimeData.getParameter("strQueryID") );
                        
                        // Check for the users rights to view the domain selected
                        // if the user doesn't have permission, do as below
                        if( authorizeUser() == false ){
                            
                            strXML = buildNewSearch();                            
                            strXML+=buildXML();                            
                            strErrorMessage = "You do not have permission to view the query";
                            strStylesheet = BUILD_ISEARCH;
                            strCurrent = BUILD_ISEARCH;
                            
                        }               
                        
                        strXML+= buildCriteriaVectorsXMLFile() ;                  
                                               
                     }                     
                                        
                    // Error message
                    strXML+= "<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";
                                         
                    // """""""""""""""""" do this after saving
                    //Build the list of Query names from the database 
                    strXML+=buildQueryListXMLFile();

                    // There is a request to change the display mode
                    // Get the required display mode
                    // and set the stylesheet accordingly with the 
                    // same XML data as was being displayed
                    if(runtimeData.getParameter("changeDisplayMode") != null)
                    {
                        strXML = strPreviousXML;
                        if (runtimeData.getParameter("rdDisplayAs") != null)
                        {
                            strDisplayMode = runtimeData.getParameter("rdDisplayAs");
                        }    

                        strStylesheet = BUILD_ISEARCH;
                        // Set the stylesheet based on the display mode
                        if (strDisplayMode.equals("normal"))
                        {
                            strStylesheet = BUILD_ISEARCH_NORMAL;                            
                        }
                        else if (strDisplayMode.equals("simplified"))
                        {
                            strStylesheet = BUILD_ISEARCH_SIMPLIFIED;
                        }
                        
                    }    
                    
                    
                }else if (strCurrent.equals(ISEARCH_RESULT)){              
                    
                    if(runtimeData.getParameter("ISEARCH_back") != null){                        
                        
                        ChannelRuntimeData currentRuntimeData = runtimeData;
                        runtimeData = oldRuntimeData;
                        
                        // Rebuild the search page with previous runtimedata
                        RebuildISearch();
                        
                        // Reset back to the current runtimeData
                        runtimeData = currentRuntimeData;                 
                    }
                    
                    // create export file
                    else if( runtimeData.getParameter("ISEARCH_performExport") != null ){

                        blExplicitFields = false;

                        // Set the explicit field selection
                        if (runtimeData.getParameter("ExplicitFields") != null)
                        {
                            blExplicitFields = true;
                        }    

                        strDelimiter = runtimeData.getParameter("delimiter");  
                        oldRuntimeData.setParameter("delimiter",strDelimiter);
                        
                        ChannelRuntimeData currentRuntimeData = runtimeData;
                        runtimeData = oldRuntimeData;
                        
                        // Rebuild the search page with previous runtimedata
                        RebuildISearch();
                        
                        // execute the query based on all the conditions and create export file
                        strXML+= performExport();
                        
                        // Reset back to the current runtimeData
                        runtimeData = currentRuntimeData;                      
                        
                    }else{
                        
                        // user changes the number of records for a page
                        
                        if (blPickerMode == true)
                        {
                            updateFlags();
                        }
                        
                        if (runtimeData.getParameter("ISEARCH_set") != null)
                        {
                            changeRecordPerPage();
                        }
                        // user changes current page
                        else if (runtimeData.getParameter("ISEARCH_go") != null)
                        {
                            changeCurrentPage();
                        }
                        // user wants to view next page
                        else if (runtimeData.getParameter("ISEARCH_next") != null)
                        {                            
                            goNextPage();
                        }
                        // user wants to view previous page
                        else if (runtimeData.getParameter("ISEARCH_previous") != null)
                        {
                            goPreviousPage();
                        }              
                        
                        ChannelRuntimeData currentRuntimeData = runtimeData;
                        runtimeData = oldRuntimeData;                        
                        
                        strXML =buildSearchResultXMLFile() + buildQueryListXMLFile(); 
                        runtimeData = currentRuntimeData; 
                         
                    }          
                }
                
                strPreviousXML = strXML;
            }
            else
            {
                strXML = strPreviousXML;
            }

            // If the display mode has just been changed
            // Build the XMl to overwrite the display mode
            // otherwise the previous display mode will be displayed
            if(runtimeData.getParameter("changeDisplayMode") != null)
            {
                strXML += "<strNewRadioVal>" + strDisplayMode + "</strNewRadioVal>";
            }
                        
            // Get the alternative display domain names
            strXML += getDisplayDomainNameXML();
            
            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root><isearch>" + strXML + "</isearch></root>";
                      
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
        }

        return null;        
    }
    
    public void setForcedDomain(String domain)
    {
        System.out.println("Forcing Domain : " + domain);
        vtSelectedDomains.add(domain);
        vtDomains.remove(domain);
        vtRequiredDomains.add(domain);
        hashDomainStatus.put(domain, "expanded");
        strSelectedDomainSel = domain;
        removeStudies(domain);
        
    }
   
    private void reloadForcedDomains()
    {
        for (int i = 0; i < vtRequiredDomains.size(); i++)
        {
            String domain = (String) vtRequiredDomains.get(i);
            vtSelectedDomains.add(domain);
            vtDomains.remove(domain);
            hashDomainStatus.put(domain, "expanded");
            strSelectedDomainSel = domain;
            removeStudies(domain);
        }
    }
    public void setForcedField (String domain, String field)
    {
    /*    System.out.println ("Forced Field : " + field + ", " + domain);
       // Vector vtSelectedFieldHandle = (Vector) hashCriteriaDomains.get(domain);
        Vector vtSelectedFieldHandle = new Vector();
        if (vtSelectedFieldHandle == null)
        {
            System.out.println ("Hashtable is null");
            vtSelectedFieldHandle = new Vector(10 ,5);
            
        }
        vtSelectedFieldHandle.add(field);
        hashCriteriaDomains.put(domain, vtSelectedFieldHandle);
      */
        String[] forcedField = new String[2];
        forcedField[0] = domain;
        forcedField[1] = field;
        vtRequiredFields.add(forcedField);
    }
    
    public void setForcedCriteria (String criteria)
    {
        //TODO: Implement
    }
    
    public void setFinishButtonVisible(boolean visible, String strLabel)
    {
        blFinishButtonVisible = visible;
        strFinishButtonLabel = strLabel;
        
    }
    
    /**
     *  Returns a hashtable containing flag/column keys.
     *
     *  Key = Column Data to prevent Double ups
     *  Data = Flag ID
     *
     */
    
    public Hashtable getFlaggedData(String fieldToReturn)
    {
        Hashtable returnData = new Hashtable();
        Enumeration flagEnum = htFlaggedItems.keys();
        
        Hashtable rowHandle = null;
        
        int currentFlagID = -1;
        while (flagEnum.hasMoreElements())
        {
            currentFlagID = Integer.parseInt((String) flagEnum.nextElement());
            rowHandle = (Hashtable) vtSearchResult.get(currentFlagID);
            returnData.put(rowHandle.get(fieldToReturn), new Integer(currentFlagID));
        }
        
        return returnData;        
        
    }
    
    
    public void updateFlags()
    {
        Enumeration rdEnum = runtimeData.getParameterNames();
        String paramName = null;
        String paramValue = null;
        String flagID = null;
        
        if (htFlaggedItems == null)
            htFlaggedItems = new Hashtable(100);
        
        while (rdEnum.hasMoreElements())
        {
            paramName = (String) rdEnum.nextElement();
            if (paramName.startsWith("flags_"))
            {
                paramValue = runtimeData.getParameter(paramName);
                if (paramValue != null)
                {
                    flagID = paramName.split("_")[1];
                    htFlaggedItems.put(flagID, flagID);
                    
                }
            }
        }
    }
    
    public void updateFlags(ChannelRuntimeData rdLocal)
    {
        Enumeration rdEnum = rdLocal.getParameterNames();
        String paramName = null;
        String paramValue = null;
        String flagID = null;
        
        if (htFlaggedItems == null)
            htFlaggedItems = new Hashtable(100);
        
        while (rdEnum.hasMoreElements())
        {
            paramName = (String) rdEnum.nextElement();
            if (paramName.startsWith("flags_"))
            {
                paramValue = rdLocal.getParameter(paramName);
                if (paramValue != null)
                {
                    flagID = paramName.split("_")[1];
                    htFlaggedItems.put(flagID, flagID);
                    
                }
            }
        }
    }
    
    
    public String getXML()
    {
        return strXML;
        
    }
    
    public String getSessionID()
    {
        return strSessionUniqueID;
    }
    
    public String getStylesheet()
    {
        return strStylesheet;
    }
      
     
    /*
     * Build the Search page
     */
     
     private void RebuildISearch(){
        
        // Get the selected domains, load field accordingly and 
        // get the selected field and show the criteria's value accordingly
        strXML = buildISearch(); 

        // Capture the latest selected fields for all domains expanded
        buildSelectedFieldsForDomainsExpanded();

        // Display the ISearch form with the domains loaded  
        // And build the latest selected domains, fields, smartforms etc.,    
        strXML+= buildXML();
        strXML+= "<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";
        strXML += "<pickerMode>" + blPickerMode + "</pickerMode>";
        strXML += "<finishButton label=\"" + strFinishButtonLabel + "\">" + blFinishButtonVisible + "</finishButton>";
        //Build the list of Query names from the database 
        strXML+=buildQueryListXMLFile();
        
        strCurrent = BUILD_ISEARCH;                        
        
        // Set the stylesheet
        if(strDisplayMode.equals("simplified")){                        
           strStylesheet = BUILD_ISEARCH_SIMPLIFIED;
        }else if(strDisplayMode.equals("normal")){                        
           strStylesheet = BUILD_ISEARCH_NORMAL;
        }else {                        
           strStylesheet = BUILD_ISEARCH;
        }                                    
                                  
     }
     
     /**
     *  Print out message if user doesn't have the autority to do a specific activity
     */
    private String buildSecurityErrorXMLFile(String aFunctionalArea)
    {
        String strTempErrorXML  = "<?xml version=\"1.0\" encoding=\"utf-8\"?><securityerror>"
                + "<errorstring>No Permission to " + aFunctionalArea + "</errorstring>"
                + "<errortext>The " + staticData.getPerson().getFullName() 
                + " is not authorised to access" + aFunctionalArea + "</errortext>"
                + "<errordata></errordata>"
                + "</securityerror>";
        return strTempErrorXML;      
    }
    
    
    /**
     * Clear the lock request
     */
    private void clearLockRequest()
    {
        try
        {
            if (lockRequest != null && lockRequest.isValid())
                lockRequest.unlock();
        }
        catch (Exception e)
        {
            LogService.instance().log(
                LogService.ERROR, "Unknown error in Search Channel - " + e.toString(), e);
        }
        finally
        {
            lockRequest = null;
        }
    }
    
    
    /** Checks data validation
     */
    private boolean validateData(int intDataType, String strDataToCheck)
    {
        // checks data validation
        switch (intDataType)
        {
            case DBMSTypes.DATE_TYPE:
                return Utilities.validateDateValue(strDataToCheck);

            case DBMSTypes.INT_TYPE:
                return Utilities.validateIntValue(strDataToCheck);

            case DBMSTypes.DOUBLE_TYPE:
                return Utilities.validateFloatValue(strDataToCheck);

            case DBMSTypes.FLOAT_TYPE:
                return Utilities.validateFloatValue(strDataToCheck);
        }
        
        // we have valid data
        return true;
    }
    
       
    /*
     * To load all the domains
     *
     */ 
    private void buildDomainList(){
        
       try{         
            // No additional processing required here if the domain contains NOTES
            if (vtSelectedDomains.contains("NOTES") )
            {
                if (runtimeData.getParameter("remove") != null)
                {
                    if (vtSelectedDomains.contains("PATIENT") || vtSelectedDomains.equals("BIOSPECIMEN"))
                    {
                        vtDomainList.clear();
                    }
                    else if (!vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN"))
                    {
                        if (!vtDomainList.contains("PATIENT"))
                        {
                            vtDomainList.add("PATIENT");
                        }
                        
                        if (!vtDomainList.contains("BIOSPECIMEN"))
                        {
                            vtDomainList.add("BIOSPECIMEN");
                        }    
                    }    
                    
                    return;
                }
                else
                {    
                    return;
                }    
            }    
            

            Hashtable hashSearchDomains = DatabaseSchema.getSearchDomains();
            Enumeration domainIterator = hashSearchDomains.keys();
            
            
            vtDomainList.clear();
            while(domainIterator.hasMoreElements()){
                
                String strCurrentDomain = (String) domainIterator.nextElement();
                String strActivityRequired = DatabaseSchema.getActivityRequired(strCurrentDomain);  
                
                // add the domain into the list only if it's not in selected domain list
                // and has a join with a domain in selected domain list
                if (!isSelectedDomain(strCurrentDomain) && hasJoin(strCurrentDomain))
                {
                    if (strActivityRequired == null || authToken.hasActivity(strActivityRequired))
                        vtDomainList.add(strCurrentDomain);
                }                
            }    
            
            // case 1: if only 1 study is selected and no domains are selected, 
            // then show only PATIENT & BIOSPECIMEN domains
            if( vtSelectedDomains.isEmpty() == true && vtSelectedStudys.size() == 1){
                
                vtDomainList.clear(); 
                String strActivityRequired = "";
                
                strActivityRequired = DatabaseSchema.getActivityRequired("PATIENT");  
                if (strActivityRequired == null || authToken.hasActivity(strActivityRequired)){
                    vtDomainList.add(0, "PATIENT");
                }
                
                strActivityRequired = DatabaseSchema.getActivityRequired("BIOSPECIMEN");  
                if (strActivityRequired == null || authToken.hasActivity(strActivityRequired)){
                    vtDomainList.add(1, "BIOSPECIMEN");
                }               
                
            }              
            
            /*
           for(Enumeration e = vtDomainList.elements(); e.hasMoreElements();){               
            while(e.hasMoreElements()){          
                //System.err.println("elt:"+e.nextElement());
                }
            }
             */
            
        }catch(Exception e){ 
           
        }
    }
    
    
    /* build smartform list
     */    
    private void buildSmartformList(){        
        
        try{
            
            if (vtSelectedDomains.contains("NOTES"))
            {
                vtSmartformList.clear();  
                return;
            }    
            
            vtSmartformList.clear();  
            
            // display all the smartform names
            Vector vtQueryFields = DatabaseSchema.getFormFields("isearch_view_smartform");

            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORM", null, null, null);
            query.setFields(vtQueryFields, null);
            query.setWhere(null, 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);               
            ResultSet rs = query.executeSelect();

            while(rs.next()){                
                String smartformName = rs.getString("SMARTFORM_strSmartformName");    
                if(!isSelectedSmartform(smartformName)){                    
                    vtSmartformList.add(smartformName);     
                }
            }
            
                        
            //Close the resultset
            rs.close();
            rs = null;
            
            
            //case 1: if selected domains, selected smartforms and selected studys is empty, then dont show any smartforms
            if( vtSelectedDomains.isEmpty() == true && vtSelectedSmartforms.isEmpty() == true && vtSelectedStudys.isEmpty() == true){
                vtSmartformList.clear(); 
               
            }else{           
            
                //if biospecimen and atleast 1 study is selected
                // then show the smartforms related to the study as well as the other smartforms
                if( vtSelectedDomains.contains( "BIOSPECIMEN" ) && vtSelectedStudys.size() > 0 ){
                 
                    LoadStudysSmartforms();                
                }               
                // if Biospecimen is selected, and no study eltd, then show all throws sf's
                else if(  vtSelectedDomains.contains("BIOSPECIMEN") ){
                    // show all the smartforms                    
                } 
                // if a ADMISSIONS is selected, show the smartforms related to admissions
                else if(  vtSelectedDomains.contains( "ADMISSIONS" ) ){     
                    
                    LoadAdmissionsSmartforms();                    
                }                                
                // if a study is selected, show sf's related to study only
                else if( vtSelectedStudys.size() > 0 ){     
                    
                    vtSmartformList.clear();  
                    LoadStudysSmartforms();                    
                }
                // if Biospecimen is not selected, do not show smartforms
                else{                    
                    vtSmartformList.clear(); 
                }
            }  
            
        }catch(Exception e){  
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);            
        }    
    }
    
    
    /*
     * Build the study names
     *
     */
    private void buildStudyList(){
        
    try{ 
        
        if (vtSelectedDomains.contains("NOTES"))
        {
            vtStudyList.clear();
            return;
        }   
        
        vtStudyList.clear();

        // display all the study names
        Vector vtQueryFields = DatabaseSchema.getFormFields("isearch_study_details");

        DALQuery query = new DALQuery();
        query.setDomain("STUDY", null, null, null);
        query.setFields(vtQueryFields, null);
        query.setWhere(null, 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);               
        ResultSet rs = query.executeSelect();

        while(rs.next()){                
            String studyName = rs.getString("STUDY_strStudyName");    
            if(!isSelectedStudy(studyName)){
                vtStudyList.add(studyName);     
            }
        }
        
        // Close the resultset
        rs.close();
        rs = null;
        
        

        //case 1: if selected domains, selected smartforms and selected studys is empty, then show all the studys
        if( vtSelectedDomains.isEmpty() == true && vtSelectedSmartforms.isEmpty() == true && vtSelectedStudys.isEmpty() == true){
            
        }else{
            
            // case 2: if selected domains has Patient then show all the studys related to Patient
            if( vtSelectedDomains.contains("PATIENT") ){
                // show all the studys               
                
            }else{                 
                // if PATIENT is not selected, no studys to be displayed
                vtStudyList.clear();
            }            
        } 
        
        
        
        }catch(Exception e){  
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);        
        }   
    
    }
    
    
    /** Build XML string for domain's list
     */
    private String buildDomainListXMLFile() 
    {
        buildDomainList();
        String strResult = "";
        
        for (int i=0; i < vtDomainList.size(); i++)
            strResult += "<domain><strDomainName>" + vtDomainList.get(i) + "</strDomainName></domain>";
        
        return strResult;
    }
    
    
    /*
     *  Build xml for the smartform list
     */
    private String buildSmartformListXMLFile(){
        
        buildSmartformList();
        
        String strResult = "";
        
        for( int i=0; i < vtSmartformList.size(); i++ ){
            String smartformName = (String)vtSmartformList.get(i);
            strResult+= "<smartform>"+
                        "<strDomainName>"+ smartformName +"</strDomainName>" +
                        "</smartform>";
        }
        
         return strResult;
    }
    
    
    /*
     * Build xml for the study list
     */
    private String buildStudyListXMLFile(){
        
        buildStudyList();
        
        String strResult = "";
        
        for( int i=0; i < vtStudyList.size(); i++ ){
            String studyName = (String)vtStudyList.get(i);
            strResult+= "<study>"+
                        "<strDomainName>"+ studyName +"</strDomainName>" +
                        "</study>";
        }
        
        return strResult;
        
    } 
    
    
    /** Checks if a domain is in selected domain list
     */
    private boolean isSelectedDomain(String strDomainName)
    {
        if (vtSelectedDomains.size() < 1)
            return false;
        
        for (int i=0; i < vtSelectedDomains.size(); i++)
            if (strDomainName.equals(vtSelectedDomains.get(i)))
                return true;
        
        return false;
    }
    
    
    /** Checks if a smartform is in selected smartform list
     */
    private boolean isSelectedSmartform(String strSmartformName)
    {
        if (vtSelectedSmartforms.size() < 1)
            return false;
        
        for (int i=0; i < vtSelectedSmartforms.size(); i++)
            if (strSmartformName.equals(vtSelectedSmartforms.get(i)))
                return true;
        
        return false;
    }
    
    
    /* Checks if a study is in the selected study list
     */ 
    private boolean isSelectedStudy( String strStudyName ){
        
        if( vtSelectedStudys.size() < 1 )
            return false;
        
        for( int i=0; i < vtSelectedStudys.size(); i++ )
            if( strStudyName.equals(vtSelectedStudys.get(i)) )
                return true;
        
        return false;
    }
    
    
    /** Checks if a domain has a join with a domain in selected domain list
     */
    private boolean hasJoin(String strDomainName)
    {
        if (vtSelectedDomains.size() < 1)
            return true;
        
        Hashtable hashSearchDomains = DatabaseSchema.getSearchDomains();
        for (int i=0; i < vtSelectedDomains.size(); i++)
        {
            Hashtable hashJoins = (Hashtable) hashSearchDomains.get(vtSelectedDomains.get(i));
            if (hashJoins.containsKey(strDomainName))
                return true;
        }
        
        return false;
    }
    
    
    /* 
     * When a particular domain is removed, 
     * remove all the related domains too, recursively
     *  
     */
    private void removeRelatedJoin( String strDomainName ){
        
        Vector vtCheckDomains = new Vector();
        
        // Get all the selected domains into vtCheckDomains vector
        for( int i = 0; i < vtSelectedDomains.size(); i++ ){
            vtCheckDomains.add((String)vtSelectedDomains.get(i).toString());            
        }
        
        // Remove the selected domain
        strSelectedDomainSel = strDomainName;
        removeSelectedDomain();     
              
        // Check the related domain only if the count of the remaining domains is > 1
        if( vtSelectedDomains.size() > 1){
            // Remove the related domain
            Hashtable hashSearchDomains = DatabaseSchema.getSearchDomains();
            for( int j = 0; j < vtCheckDomains.size(); j++ ){

                Hashtable hashJoins = (Hashtable)hashSearchDomains.get((String)vtCheckDomains.get(j));

                if( hashJoins.containsKey(strDomainName) ){              

                    strDomainName = (String)vtCheckDomains.get(j);
                    strSelectedDomainSel = (String)vtCheckDomains.get(j);
                    removeSelectedDomain();
                    
                }            
            }
        }// End of if
        
        
        // Also Remove if the removed domains existed in the Missing records criteria
        for( int k=0; k < vtMissingFor.size(); k++){
            if( !vtSelectedDomains.contains(vtMissingFor.get(k).toString()) || !vtSelectedDomains.contains(vtMissingWithout.get(k).toString())){
                vtMissingFor.remove(k);
                vtMissingWithout.remove(k);
            }
        }        

        // Also Remove if the removed domains existing in the Missing records criteria
        for( int k=0; k < vtMissingSFDomain.size(); k++){
            if( !vtSelectedDomains.contains(vtMissingSFDomain.get(k).toString())){
                vtMissingSFDomain.remove(k);
                vtMissingSF.remove(k);
                vtMissingSFField.remove(k);
            }
        }        
        
    }
    
    
    /**  Returns all the domains that has a join with the selected domain
     */
    private void addSelectedDomain()
    { 
        if (strDomainSel != null)
        {
            String strSelectedDomain = strDomainSel;
            
            if (strSelectedDomain.equals("NOTES") || vtSelectedDomains.contains("NOTES"))
            {
                manageNotesDomains();
            } 
            else
            {    
                
                // Check if it is schema's domain or smartform names
                if( vtDomainList.contains(strSelectedDomain)){

                    if(!isSelectedDomain(strSelectedDomain)){
                        vtSelectedDomains.add(strSelectedDomain);            
                    }  
                }
                // check if it is a study
                else if (isStudy(strSelectedDomain) == true){

                    if(!isSelectedStudy(strSelectedDomain)){
                        vtSelectedStudys.add(strSelectedDomain);
                    }
                }
                // check if it is a smartform
                else if (isSmartform(strSelectedDomain) == true){

                    if(!isSelectedSmartform(strSelectedDomain)){
                        vtSelectedSmartforms.add(strSelectedDomain);
                    }
                }

                 hashDomainStatus.put( strSelectedDomain, "collapsed" );

                 // if PATIENT is not selected, study is selected first, and then BIOSPECIMEN is selected
                 // then remove all the studys
                 if( !vtSelectedDomains.contains("PATIENT") && strSelectedDomain.equals("BIOSPECIMEN")){

                     for(int i =0; i < vtSelectedStudys.size(); i++){

                        hashDomainStatus.remove( (String)vtSelectedStudys.get(i) );
                        hashCriteriaDomains.remove( (String)vtSelectedStudys.get(i) );
                        vtCriteriasDomains.remove( (String)vtSelectedStudys.get(i) );
                     }
                     vtSelectedStudys.removeAllElements();

                 }
             
             }             
        }
    }
    
    private void manageNotesDomains ()
    {    
        String strSelectedDomain = strDomainSel;         
        
        if (strSelectedDomain.equals("NOTES"))
        {
            vtDomainList.clear();
            hashDomainStatus.clear();
            hashCriteriaDomains.clear();
            vtCriteriasDomains.clear();
            vtSelectedDomains.clear();
            
            vtDomainList.add("PATIENT");
            vtDomainList.add("BIOSPECIMEN");
        }
        else if (strSelectedDomain.equals("PATIENT") || strSelectedDomain.equals("BIOSPECIMEN"))
        {
            vtDomainList.clear();
        }
        
        if(!isSelectedDomain(strSelectedDomain)){
            vtSelectedDomains.add(strSelectedDomain);            
        }         
        
        hashDomainStatus.put( strSelectedDomain, "collapsed" );
        
    }
    
    
    public void removeStudies(String strSelectedDomain)
    {       
         if( !vtSelectedDomains.contains("PATIENT") && strSelectedDomain.equals("BIOSPECIMEN")){

             for(int i =0; i < vtSelectedStudys.size(); i++){

                hashDomainStatus.remove( (String)vtSelectedStudys.get(i) );
                hashCriteriaDomains.remove( (String)vtSelectedStudys.get(i) );
                vtCriteriasDomains.remove( (String)vtSelectedStudys.get(i) );
             }
             vtSelectedStudys.removeAllElements();

         }
    }
    
    
    
    /** Remove a domain from selected domain list
     */
    private void removeSelectedDomain()
    {
        if (strSelectedDomainSel != null)
        {
            String strDomainToRemove = strSelectedDomainSel;
            
            // remove if it is domain
            for (int i=0; i < vtSelectedDomains.size(); i++)
            {
                if (strDomainToRemove.equals(vtSelectedDomains.get(i)))
                {
                    vtSelectedDomains.removeElementAt(i);                    
                    break;
                }
            }
            
            // remove if it is a study
            if( isStudy(strDomainToRemove)){
                
                vtSelectedStudys.remove(strDomainToRemove);
            }
            
            // remove if it is a smartform
            for (int k=0; k < vtSelectedSmartforms.size(); k++)
            {
                if (strDomainToRemove.equals(vtSelectedSmartforms.get(k)))
                {
                    vtSelectedSmartforms.removeElementAt(k);
                    break;
                }
            }
                       
            
            // Removed the criterias added for this domain
            for( int j=0; j < vtDomainVal.size() ; j++){                
                
                if (strDomainToRemove.equals((String)vtDomainVal.get(j)))
                {                    
                    vtJoin.remove(j);
                    vtDomainVal.remove(j);
                    vtField.remove(j);
                    vtOp.remove(j);
                    vtSearchVal.remove(j);      
                    vtFieldLabel.remove(j);
                    vtOpenBracket.remove(j);
                    vtCloseBracket.remove(j);
                    vtParticipantDomain.remove(j);                    
                    
                    //set the next join to blank if the criteria deleted is the first
                    if( j == 0){
                        if( vtJoin.isEmpty() == false){
                            vtJoin.set(0, "");
                        }
                    }
                    
                    j= j - 1;
                }
                
            }
            
            //remove from criterias domains
            for (int m=0; m < vtCriteriasDomains.size(); m++)
            {
                if (strDomainToRemove.equals(vtCriteriasDomains.get(m)))
                {
                    vtCriteriasDomains.removeElementAt(m);
                    break;
                }
            }
            
            hashDomainStatus.remove( strDomainToRemove );
            hashCriteriaDomains.remove( strDomainToRemove );
            
            runtimeData.setParameter("fieldSel", ""); 
            runtimeData.setParameter("selFieldType", "text"); 
            strSelectedDomainSel = null;            
            
            // Studys can be selected only if Patient is selected, 
            // else clear selected studys
            if ( strDomainToRemove.equals("PATIENT")){
                                   
                    // if more than 1 study
                    if( vtSelectedStudys.size() > 0 ){                        
                        
                        for(int i =0; i < vtSelectedStudys.size(); i++){
                            hashDomainStatus.remove( (String)vtSelectedStudys.get(i) );
                            hashCriteriaDomains.remove( (String)vtSelectedStudys.get(i) );
                            vtCriteriasDomains.remove( (String)vtSelectedStudys.get(i) );
                        }
                        
                        for(int i =0; i < vtDomainVal.size(); i++){                            
                            String dom = (String)vtDomainVal.get(i);
                            
                            // if criteria is added for study OR
                            // if there are participantdomains for smartforms set to PATIENT
                            if ( vtSelectedStudys.contains(dom) || ( vtParticipantDomain.get(i).equals("PATIENT") ) ){
                                                                
                                vtJoin.remove(i);
                                vtDomainVal.remove(i);
                                vtField.remove(i);
                                vtOp.remove(i);
                                vtSearchVal.remove(i);   
                                vtFieldLabel.remove(i);
                                vtOpenBracket.remove(i);
                                vtCloseBracket.remove(i);
                                vtParticipantDomain.remove(i);
                                
                                //set the next join to blank if the criteria deleted is the first
                                if( i == 0){
                                    if( vtJoin.isEmpty() == false){
                                        vtJoin.set(0, "");
                                    }
                                }
                                
                                i = i - 1;                                
                            }            
                           
                        }
                        
                        vtSelectedStudys.clear();
                    }                
            }           
            
            
            // Smartforms can be selected only if Biospecimen/ Study name is selected, 
            // else clear selected smartforms
            if ( strDomainToRemove.equals("BIOSPECIMEN") || isStudy(strDomainToRemove) == true || ( !vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedStudys.isEmpty() == true )){
                                
                    // if more than 1 smartform
                    if( vtSelectedSmartforms.size() > 0 ){                        
                        
                        for(int i =0; i < vtSelectedSmartforms.size(); i++){
                            hashDomainStatus.remove( (String)vtSelectedSmartforms.get(i) );
                            hashCriteriaDomains.remove( (String)vtSelectedSmartforms.get(i) );
                            vtCriteriasDomains.remove( (String)vtSelectedSmartforms.get(i) );
                        }
                        
                        for(int i =0; i < vtDomainVal.size(); i++){                            
                            String dom = (String)vtDomainVal.get(i);
                            
                            if (vtSelectedSmartforms.contains(dom)){                         
                                
                                vtJoin.remove(i);
                                vtDomainVal.remove(i);
                                vtField.remove(i);
                                vtOp.remove(i);
                                vtSearchVal.remove(i);   
                                vtFieldLabel.remove(i);
                                vtOpenBracket.remove(i);
                                vtCloseBracket.remove(i);
                                vtParticipantDomain.remove(i);
                                
                                //set the next join to blank if the criteria deleted is the first
                                if( i == 0){
                                    if( vtJoin.isEmpty() == false){
                                        vtJoin.set(0, "");
                                    }
                                }
                                
                                i = i - 1;
                            }
                        }
                        
                        vtSelectedSmartforms.clear();
                    }              
            }
            
            // if there are no smartforms selected, clear the dataelements
            if(vtSelectedSmartforms.size() == 0){
                vtSelectedDataelements.clear();
                vtSelectedSFDataelements.clear();                
            }    
            
            // if study is removed, clear the smartforms as above 
            // and remove study from the hashtables also
            if( isStudy(strDomainToRemove) == true ){
                
                 hashDomainStatus.remove( strDomainToRemove );
                 hashCriteriaDomains.remove( strDomainToRemove );
                 vtCriteriasDomains.remove( strDomainToRemove );
            }
                      
            
            // clear the domainselected and field selected
            strFieldSel = "";
            
            runtimeData.setParameter("strFieldSel", "");                   
            runtimeData.setParameter("fieldSel", (String)null);  
            runtimeData.setParameter("selFieldType", "");      
            runtimeData.setParameter("field", "");            
            runtimeData.setParameter("number", (String)null);         
             
        }
    }
    
    /** Remove the last domain from selected domain list
     */
    private void removeLastDomain()
    {
        if (vtSelectedDomains.size() > 0)
            vtSelectedDomains.removeElementAt(vtSelectedDomains.size() - 1);
    }
    
    /** Remove all selected domains
     */
    private void removeAllSelectedDomains()
    {
        vtSelectedDomains.clear();
        strSelectedDomainSel = null;
        
        vtSelectedSmartforms.clear();  
        vtSelectedStudys.clear();
        vtStudyList.clear();
        vtCriteriasDomains.clear();
        vtJoin.clear();
        vtDomainVal.clear();
        vtField.clear();
        vtOp.clear();
        vtSearchVal.clear();   
        vtFieldLabel.clear();
        vtOpenBracket.clear();
        vtCloseBracket.clear();
        vtParticipantDomain.clear();
        hashDomainStatus.clear();
        hashCriteriaDomains.clear();
        
       
        strSelectedDomainSel = null;
        runtimeData.setParameter("fieldSel", ""); 
        runtimeData.setParameter("selFieldType", "text"); 
        
        
        // Clear vectors and hashtables
        vtDomainList.clear();
        vtSelectedDomains.clear();
	vtSearchFormFields.clear();
        vtSelectedDataelements.clear();
        vtSelectedSFDataelements.clear();        
        vtJoin.clear();
        vtDomainVal.clear();
        vtField.clear();
        vtOp.clear();
        vtSearchVal.clear();
        vtFieldLabel.clear();
        vtCriteriasDomains.clear();
        hashCriteriaDomains.clear();
        hashDomainStatus.clear();
        vtSmartformList.clear();
        vtSelectedSmartforms.clear();
    
        // Variables 
        strDomainSel = "";     
        strFieldSel = "";
        strSelectedDomainSel = "";        
        
        //runtimedata        
        runtimeData.setParameter("strDomainSel", "");
        runtimeData.setParameter("strFieldSel", "");
        runtimeData.setParameter("strSelectedDomainSel", "");
        runtimeData.setParameter("SelectedDomainSel", (String)null);
        runtimeData.setParameter("domainSel", (String)null);
        runtimeData.setParameter("fieldSel", (String)null);  
       
        runtimeData.setParameter("selFieldType", ""); 
        runtimeData.setParameter("number", ""); 
        runtimeData.setParameter("domains", ""); 
        runtimeData.setParameter("field", ""); 
        runtimeData.setParameter("selected_domains", ""); 
        runtimeData.setParameter("rdDisplayAs", "");  
        runtimeData.setParameter("labelInColumn", "");          

        runtimeData.setParameter("number", (String)null);
        
    }
    
    /**
     * Add the domain selected into the selected domain list
     *
     */
    private String buildSelectedDomainsXMLFile(){        
        
        String strResult = "";
        
        for (int i=0; i < vtSelectedDomains.size(); i++)
           strResult += "<selected_domain><strDomainName>" + vtSelectedDomains.get(i) + "</strDomainName></selected_domain>";
        
        return strResult;
        
    }
    
    /*
     * Add the Smartform selected into the Selected Smartform list
     */
    private String buildSelectedSmartformsXMLFile(){        
        
        String strResult = "";
        
        for (int i=0; i < vtSelectedSmartforms.size(); i++)
           strResult += "<selected_smartform><strDomainName>" + vtSelectedSmartforms.get(i) + "</strDomainName></selected_smartform>";
        
        return strResult;
        
    }    
    
    /*
     * Add the Smartform selected into the Selected Smartform list
     */
    private String buildSelectedStudysXMLFile(){        
        
        String strResult = "";
        
        for (int i=0; i < vtSelectedStudys.size(); i++)
           strResult += "<selected_study><strDomainName>" + vtSelectedStudys.get(i) + "</strDomainName></selected_study>";
        
        return strResult;
        
    }    
    
    
    /** Build the field list for the domain added into the selected domain list 
      */
    private String buildFieldList(){
        String strResult = "";  
        try{
        
            if(strSelectedDomainSel == null || strSelectedDomainSel.equals("null") || strSelectedDomainSel.equals("")){ } 
            else{

                // Check if the selected domain submitted is a domain / smartform
                // if it belongs to schema domains
                if(vtSelectedDomains.contains(strSelectedDomainSel)){            

                    if( !strSelectedDomainSel.equals("BIOANALYSIS")){
                        
                        //Vector vtFormFields = DatabaseSchema.getFormFields("advanced search "+strSelectedDomainSel.toLowerCase());        
                        /*
                            rennypv--edited this bit- to remove the fields that are to be masked from being seen for selection
                         */
                        Vector vtFormFieldsAll = DatabaseSchema.getFormFields("advanced search "+strSelectedDomainSel.toLowerCase());
                        Vector vtFormFields = QueryChannel.prepareFormfieldsForFieldSecurity(vtFormFieldsAll,authToken);        
                        for(int i=0; i < vtFormFields.size() ; i++){
                            String strField = (String) vtFormFields.get(i);
                            DBField dbField = (DBField) DatabaseSchema.getFields().get(strField);
                            strResult+= "<FieldList>";
                            strResult+= "<Label>"+ dbField.getLabelInForm() +"</Label>" +
                                        "<InternalName>"+ dbField.getInternalName() +"</InternalName>" +
                                        "<LOVType>"+ dbField.getLOVType() +"</LOVType>";                 
                            strResult+= "</FieldList>";
                            
                            // If no field has been selected default it to the first field in the list
                            if ((strFieldSel == null) || strFieldSel.equals(""))
                            {
                                strFieldSel = dbField.getInternalName(); 
                            } 
                        }
                    }

                }else if(vtSelectedSmartforms.contains(strSelectedDomainSel)){ // if it belongs to smartform name

                    ResultSet rs = buildSmartformsDataelements(strSelectedDomainSel);

                    // build field list for dropdown
                    while( rs.next()){

                        strResult+= "<DataelementsList>";
                        strResult+= "<DATAELEMENTS_intDataElementID>"+ rs.getString("DATAELEMENTS_intDataElementID") +"</DATAELEMENTS_intDataElementID>" +
                                    "<DATAELEMENTS_strDataElementName>"+ rs.getString("DATAELEMENTS_strDataElementName") +"</DATAELEMENTS_strDataElementName>" +
                                    "<DATAELEMENTS_intDataElementType>"+ rs.getString("DATAELEMENTS_intDataElementType") +"</DATAELEMENTS_intDataElementType>";                 
                        strResult+= "</DataelementsList>";

                    }
                    
                    // Close the resultset
                    rs.close();
                    rs = null;
                    
                }
            }
            
        }catch(Exception e){ 
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
             return null;
        } 
        return strResult;
    }
       
    
    /** 1. Load the domain list
     *  2. Get the domain selected in the domain list (and accordingly load the related domains to add later)
     *  3. Get the domain selected in the selected domain list and according load their fields
     *  4. Get the field selected in the criteria and accordingly show the value field as a textbox or LOV dropdown 
     */    
    private String buildISearch(){
        
     try{
            
            String strResult = "";

            // Get the Domain selected to add
            if(runtimeData.getParameter("domains") != null && !runtimeData.getParameter("domains").equals("")){
                strDomainSel = runtimeData.getParameter("domains");
            }else if(runtimeData.getParameter("domainSel") != null ){//&& !runtimeData.getParameter("domainSel").equals("")
                strDomainSel = runtimeData.getParameter("domainSel");
            }            
            
            // Get the Field selected if present
            if(runtimeData.getParameter("field") != null && !runtimeData.getParameter("field").equals("")){
                strFieldSel = runtimeData.getParameter("field");
            }else if(runtimeData.getParameter("fieldSel") != null ){//&& !runtimeData.getParameter("fieldSel").equals("")                
                strFieldSel = runtimeData.getParameter("fieldSel");
            }
            
            // Get the domain selected in the Selected domain list
            if(runtimeData.getParameter("selected_domains") != null && !runtimeData.getParameter("selected_domains").equals("")){
                strSelectedDomainSel = runtimeData.getParameter("selected_domains"); 
            }else if(runtimeData.getParameter("SelectedDomainSel") != null){// && !runtimeData.getParameter("SelectedDomainSel").equals("")                
                strSelectedDomainSel = runtimeData.getParameter("SelectedDomainSel");
            }      
            
            // if edit criteria is clicked
            if(runtimeData.getParameter("target") != null && !runtimeData.getParameter("target").equals("")){
                strSelectedDomainSel = runtimeData.getParameter("SelectedDomainSel");
            }

            // Get the value of the radio button
            if(runtimeData.getParameter("rdDisplayAs") != null){
                strRadioVal = runtimeData.getParameter("rdDisplayAs");
            }else if(runtimeData.getParameter("strRadioVal") != null){
                strRadioVal = runtimeData.getParameter("strRadioVal");     
            } 
            
            // Get the query ID            
            if( runtimeData.getParameter("strQueryID") != null && !runtimeData.getParameter("strQueryID").equals("")){
                strQueryID = runtimeData.getParameter("strQueryID");
            }           
            
            // Get the QueryName            
            if( runtimeData.getParameter("QueryName") != null && !runtimeData.getParameter("QueryName").equals("")){
                strQueryName = Utilities.cleanForXSL(runtimeData.getParameter("QueryName"));
            }
           
            // search Value
            if (runtimeData.getParameter("searchValue") == null){
                runtimeData.setParameter("searchValue", "");
            }
            
            //Get the delimiter
            if(runtimeData.getParameter("delimiter") != null){
                strDelimiter = runtimeData.getParameter("delimiter");
            }else{
                strDelimiter = ",";
            }      
            
            // Get the Missing For List value            
            if(runtimeData.getParameter("MissingForList") != null && !runtimeData.getParameter("MissingForList").equals("")){
                strMissingForSel = runtimeData.getParameter("MissingForList");
            }else if(runtimeData.getParameter("MissingForListSel") != null ){//&& !runtimeData.getParameter("domainSel").equals("")
                strMissingForSel = runtimeData.getParameter("MissingForListSel");
            }  
            
            
            // Get the Missing Without List value            
            if(runtimeData.getParameter("MissingWithoutListSel") != null ){//&& !runtimeData.getParameter("domainSel").equals("")
                strMissingWithoutSel = runtimeData.getParameter("MissingWithoutListSel");
            }              
            

            // Get the Missing Smartform Domain List value            
            if(runtimeData.getParameter("MissingSFDomainList") != null && !runtimeData.getParameter("MissingSFDomainList").equals("")){
                strMissingSFDomainSel = runtimeData.getParameter("MissingSFDomainList");
            }else if(runtimeData.getParameter("MissingSFDomainListSel") != null ){
                strMissingSFDomainSel = runtimeData.getParameter("MissingSFDomainListSel");
            }  
            
            // Get the Missing Smartform Field List value            
            if(runtimeData.getParameter("MissingSFFieldList") != null && !runtimeData.getParameter("MissingSFFieldList").equals("")){
                strMissingSFFieldSel = runtimeData.getParameter("MissingSFFieldList");
            }else if(runtimeData.getParameter("MissingSFFieldListSel") != null ){
                strMissingSFFieldSel = runtimeData.getParameter("MissingSFFieldListSel");
            }  
            
            
            String strJoinSel = runtimeData.getParameter("join");
            String strOpenBracketSel = runtimeData.getParameter("openBracket");
            String strCloseBracketSel = runtimeData.getParameter("closeBracket");
            String strOpSel = runtimeData.getParameter("operatorType");
            String strParticipantDomainSel = runtimeData.getParameter("participantDomain");
            
            if (blCriteriaAdded == true || (runtimeData.getParameter("newDomainSelected") != null && runtimeData.getParameter("newDomainSelected").equals("true"))) 
            {
                strJoinSel = "";
                strOpenBracketSel = "";
                strCloseBracketSel = "";
                strOpSel = "";
                strParticipantDomainSel = "";
            }    
            else if ( (runtimeData.getParameter("delete") != null && runtimeData.getParameter("delete").equals("true")) || (runtimeData.getParameter("target") != null && runtimeData.getParameter("target").equals("editCriteria")))
            {
                strJoinSel = runtimeData.getParameter("joinSel");
                strOpenBracketSel = runtimeData.getParameter("openBracketSel");
                strCloseBracketSel = runtimeData.getParameter("closeBracketSel");
                strOpSel = runtimeData.getParameter("opSel");
                strParticipantDomainSel = runtimeData.getParameter("participantDomainSel");
            } 
            
            if ((blCriteriaAdded == false) && (runtimeData.getParameter("addCriteria") != null) || (runtimeData.getParameter("updateCriteria") != null))
            {
                strResult = "<searchValue>"+runtimeData.getParameter("searchValue")+"</searchValue>" ;
            }            
            
            // add the selected domain and the field selected to the XML
            strResult += "<domainSel>"+strDomainSel+"</domainSel>" +
                        "<fieldSel>"+ strFieldSel +"</fieldSel>" +
                        "<SelectedDomainSel>"+strSelectedDomainSel+"</SelectedDomainSel>" +
                        "<joinSel>"+strJoinSel+"</joinSel>" +
                        "<opSel>"+strOpSel+"</opSel>" +    
                        "<openBracketSel>"+ strOpenBracketSel +"</openBracketSel>" +
                        "<closeBracketSel>"+ strCloseBracketSel +"</closeBracketSel>" +
                        "<participantDomainSel>"+ strParticipantDomainSel +"</participantDomainSel>" +                             
                        "<criteriasDomain>"+runtimeData.getParameter("criteriasDomain")+"</criteriasDomain>" +
                        "<expanded>"+runtimeData.getParameter("expanded")+"</expanded>" +
                        "<strRadioVal>"+ strRadioVal +"</strRadioVal>" +
                        "<strQueryID>"+ strQueryID +"</strQueryID>" +
                        "<strQueryName>"+ strQueryName +"</strQueryName>" +
                        "<target>"+ runtimeData.getParameter("target") +"</target>" +
                        "<labelInColumn>"+ runtimeData.getParameter("labelInColumn") +"</labelInColumn>" +
                        "<strSaveAs>"+ runtimeData.getParameter("saveAs") +"</strSaveAs>" +
                        "<strDelimiter>"+ Utilities.cleanForXSL(strDelimiter) +"</strDelimiter>" +
                        "<ExplicitFieldsVal>" + blExplicitFields + "</ExplicitFieldsVal>" + 
                        "<target>"+ runtimeData.getParameter("target") +"</target>" +
                        "<SensitivityVal>"+ runtimeData.getParameter("Sensitivity") +"</SensitivityVal>" +
                        "<MissingForListSel>"+ strMissingForSel +"</MissingForListSel>" +
                        "<MissingWithoutListSel>"+ strMissingWithoutSel +"</MissingWithoutListSel>" +
                        "<targetMissing>"+ runtimeData.getParameter("targetMissing") +"</targetMissing>" + 
                        "<MissingSFDomainListSel>"+ strMissingSFDomainSel +"</MissingSFDomainListSel>" +
                        "<MissingSFFieldListSel>"+ strMissingSFFieldSel +"</MissingSFFieldListSel>" +
                        "<targetMissingSF>"+ runtimeData.getParameter("targetMissingSF") +"</targetMissingSF>";                
                        
            // Add the number as null to xml, only if delete criteria is clicked
            if( runtimeData.getParameter("delete") != null){   
                 strResult+= "<number>null</number>" ;                
            }else{
                strResult+= "<number>"+runtimeData.getParameter("number")+"</number>" ;
            }  
            
            
            // Add the number as null to xml, only if delete of Missing Records criteria is clicked
            if( runtimeData.getParameter("deleteMissingCriteria") != null){   
                 strResult+= "<MissingCriteriaNo>null</MissingCriteriaNo>" ;                
            }else{
                strResult+= "<MissingCriteriaNo>"+runtimeData.getParameter("MissingCriteriaNo")+"</MissingCriteriaNo>" ;
            }     
                 

            // Add the number of missing smartform criteria as null to xml, only if delete of Missing Records criteria is clicked
            if( runtimeData.getParameter("deleteMissingSFCriteria") != null){   
                 strResult+= "<MissingSFCriteriaNo>null</MissingSFCriteriaNo>" ;                
            }else{
                strResult+= "<MissingSFCriteriaNo>"+runtimeData.getParameter("MissingSFCriteriaNo")+"</MissingSFCriteriaNo>" ;
            }     
            
            
            // display the participantDomain dropdowns - based on the selected domains and only if a smartform is selected
            if( vtSelectedSmartforms.contains(strSelectedDomainSel)){
                
                String strParticipantDomainLOV = "<strParticipantDomainLOV>";
                if (vtSelectedDomains.contains("PATIENT")) 
                {
                    strParticipantDomainLOV += "<Domain>PATIENT</Domain>";
                }
                
                if (vtSelectedDomains.contains("BIOSPECIMEN")) 
                {
                    strParticipantDomainLOV += "<Domain>BIOSPECIMEN</Domain>";
                }
                
                if (vtSelectedDomains.contains("BIOANALYSIS"))
                {
                    strParticipantDomainLOV += "<Domain>BIOANALYSIS</Domain>";  
                }

                if (vtSelectedDomains.contains("ADMISSIONS"))
                {
                    strParticipantDomainLOV += "<Domain>ADMISSIONS</Domain>";
                }
                
                strParticipantDomainLOV += "</strParticipantDomainLOV>";

                strResult+= strParticipantDomainLOV;                            
            }
                       
            // if no criterias are added or if edit criteria is clicked for the first criteria or updating the first criteria, show blank in join dropdown
            if( vtJoin.isEmpty() == true || ( runtimeData.getParameter("target") != null && runtimeData.getParameter("number") != null && runtimeData.getParameter("number").equals("0") ) || ( runtimeData.getParameter("updateCriteria") != null && runtimeData.getParameter("number") != null &&  runtimeData.getParameter("number").equals("0") ) || ( runtimeData.getParameter("number") != null &&  runtimeData.getParameter("number").equals("0") )){
                strResult+= "<showJoin>false</showJoin>" ;
            }else{
                strResult+= "<showJoin>true</showJoin>" ;
            }                 
              
            
            //Get the domain selected in the selected domain list and according load their fields in the search criteria dropdown
            if((runtimeData.getParameter("remove") == null) && (runtimeData.getParameter("remove_all") == null)){
                strResult+= buildFieldList();
            }
            
                        
            // if Edit criteria is clicked
            if(runtimeData.getParameter("target") != null){
                
                strResult+= "<searchValue>"+runtimeData.getParameter("searchValue")+"</searchValue>" ;
                // Build the LOV dropdown if the field type is LOV
                strFieldSel = runtimeData.getParameter("fieldSel");
                strSelectedDomainSel = runtimeData.getParameter("SelectedDomainSel");
                
                // if the selected domain is a domain, build fields, if it is a smartform dont build value field 
                if( vtSelectedDomains.contains(strSelectedDomainSel)){
                    strResult+= buildCriteriaValueField(strFieldSel);    
                }
                
            }else{
            
                // if field selected belongs to the domain selected, then buildcriteriavaluefield
                // if a domain is selected & edit criteria is not clicked
                if( strSelectedDomainSel != null && !strSelectedDomainSel.equals("") && runtimeData.getParameter("target") == null){
                    if(vtSelectedDomains.contains(strSelectedDomainSel) == true ){

                        Vector vtFormFieldsAll = DatabaseSchema.getFormFields("advanced search "+strSelectedDomainSel.toLowerCase());
                        Vector vtFormFields = QueryChannel.prepareFormfieldsForFieldSecurity(vtFormFieldsAll,authToken);        
                        
                        if( strFieldSel != null && !strFieldSel.equals("") && strFieldSel.startsWith("\"")){
                            String strNewField = strFieldSel.substring(1, strFieldSel.length() - 1);
                        
                            for( int i = 0; i < vtFormFields.size(); i++ ){
                                if( vtFormFields.get(i).equals(strNewField)){                
                                    // Build the LOV dropdown if the field type is LOV
                                    strResult+= buildCriteriaValueField(strFieldSel);     
                                }else{
                                    strResult+= "<selFieldType>text</selFieldType>"; 
                                }
                            }
                        }                    
                    }
                }else{ // if a smartform is selected or domain selected is empty or edit criteria is clicked
                    strResult+= "<selFieldType>text</selFieldType>";
                }                
            }   
                      
            // For Searching missing records, load all the related domains only in Without list,
            // when For list is selected
            if( vtSelectedDomains.isEmpty() == false && (strMissingForSel == null || strMissingForSel.equals("")) ){
                strMissingForSel = vtSelectedDomains.firstElement().toString();                
            }
            
            strResult+= buildWithoutList( strMissingForSel );
            
            // Get the primarykeys of each domain & set it
            // accordingly into the hashtable
            sethashPrimaryKey();

            // if domain STUDY (related to BIO) is selected and then Study names (related to patient) is
            // selected then, remove the domain STUDY
            if( vtSelectedStudys.isEmpty() == false && vtSelectedDomains.contains( "STUDY" )){
                strSelectedDomainSel = "STUDY";
                removeSelectedDomain();                              
                strErrorMessage = "Dataset STUDY cannot be in the selected dataset list, as study(s) are in the selected dataset list";
            }     
            
            
            return strResult;

        
    }catch(Exception e){ 
         LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
         return null;
    } 
  }
    
    
    /*
     *   Add Search Criteria     
     */
    
    public void addCriteria(){  
        
        // Check if field is specified for the criteria
        if( runtimeData.getParameter("field") == null || runtimeData.getParameter("field").equals("") ){           
            
            strErrorMessage = "Please click on the dataset to display the fields";            
        }
        else if( vtJoin.isEmpty() == false && runtimeData.getParameter("join").equals("") ){
            
            strErrorMessage = "Please select the join";            
        }
        // if no domains are selected, only study& sf selected, and if criteria is added for sf with partDomain
        else if( vtSelectedDomains.isEmpty() == true ){
            
            strErrorMessage = "Please select the dataset PATIENT or BIOSPECIMEN to add a criteria";
        }
        else{
            
                vtJoin.add(runtimeData.getParameter("join"));
                vtDomainVal.add(runtimeData.getParameter("SelectedDomainSel"));
                vtField.add(runtimeData.getParameter("field"));
                vtOp.add(runtimeData.getParameter("operatorType"));
                vtSearchVal.add(runtimeData.getParameter("searchValue"));     
                vtOpenBracket.add(runtimeData.getParameter("openBracket"));
                vtCloseBracket.add(runtimeData.getParameter("closeBracket"));          
                
                // if criteria is added for the domain
            if( vtSelectedDomains.contains(strSelectedDomainSel)){ 
            
                // get label for display
                String fieldVal =  runtimeData.getParameter("field");
                fieldVal = fieldVal.substring(1, fieldVal.length() - 1);
                
                DBField dbField = (DBField) DatabaseSchema.getFields().get(fieldVal);

                String label = dbField.getLabelInForm();                
                vtFieldLabel.add( label );
                vtParticipantDomain.add("blank");
                
            }else{ // criteria is added for the smartform        
                 
                // get the dataelement name                             
                vtFieldLabel.add( getDataelementName(runtimeData.getParameter("field")) );          
                vtParticipantDomain.add( runtimeData.getParameter("participantDomain") );         
                
            }
                
            // Clear the field selected
            runtimeData.setParameter("field", (String) null);
            runtimeData.setParameter("fieldSel", (String) null);
            runtimeData.setParameter("strFieldSel", (String) null);
            strFieldSel = "";
            
            blCriteriaAdded = true;
        }  
        
    }
    
    /** Build XML string for Search Criteria
     */
    private String buildCriteriaListXMLFile()   
    {        
        String strResult = "";        
        
        for(int i=0; i < vtJoin.size(); i++){  
            
            // if criteria is not for a smartform
            if( !vtSelectedSmartforms.contains(vtDomainVal.get(i).toString()) ){
            
               strResult+= "<searchCriteria>" +
                        "<openBracketSel>"+ vtOpenBracket.get(i) +"</openBracketSel>" +
                        "<joinSel>"+ vtJoin.get(i) +"</joinSel>" +
                        "<domainVal>"+ vtDomainVal.get(i) +"</domainVal>" +
                        "<fieldSel>"+ vtField.get(i) +"</fieldSel>" +
                        "<opSel>"+ vtOp.get(i) +"</opSel>" +
                        "<searchValue>"+ Utilities.cleanForXSL((String) vtSearchVal.get(i)) +"</searchValue>" +
                        "<closeBracketSel>"+ vtCloseBracket.get(i) +"</closeBracketSel>" +
                        "<participantDomainSel>"+ vtParticipantDomain.get(i)+"</participantDomainSel>" +
                        "<criteriaNo>"+ i +"</criteriaNo>" +
                        "<labelInColumn>"+ vtFieldLabel.get(i) +"</labelInColumn>" +
                        "</searchCriteria>";  
            
            }else{// if criteria is for a smartform
                
                strResult+= "<smartformCriteria>" +
                        "<openBracketSel>"+ vtOpenBracket.get(i) +"</openBracketSel>" +
                        "<joinSel>"+ vtJoin.get(i) +"</joinSel>" +
                        "<domainVal>"+ vtDomainVal.get(i) +"</domainVal>" +
                        "<fieldSel>"+ vtField.get(i) +"</fieldSel>" +
                        "<opSel>"+ vtOp.get(i) +"</opSel>" +
                        "<searchValue>"+ Utilities.cleanForXSL((String) vtSearchVal.get(i)) +"</searchValue>" +
                        "<closeBracketSel>"+ vtCloseBracket.get(i) +"</closeBracketSel>" +
                        "<participantDomainSel>"+ vtParticipantDomain.get(i)+"</participantDomainSel>" +
                        "<criteriaNo>"+ i +"</criteriaNo>" +
                        "<labelInColumn>"+ vtFieldLabel.get(i) +"</labelInColumn>" +
                        "</smartformCriteria>";  
                
            }
            
        } 
        
        return strResult; 
    }
    
    
     /** update Search Criteria
     */
    private void updateCriteria() 
    {    
        // Check if field is specified for the criteria
        if( runtimeData.getParameter("field") == null || runtimeData.getParameter("field").equals("") ){
            
            strErrorMessage = "Please click on the dataset to display the fields";            
        }
        else if( vtJoin.isEmpty() == false && runtimeData.getParameter("join").equals("") && !runtimeData.getParameter("number").equals("0")){
            
            strErrorMessage = "Please select the join";            
        }         
        else{
            
            deleteCriteria();

            if(runtimeData.getParameter("number") != null && !runtimeData.getParameter("number").equals("")){
                int k = Integer.parseInt(runtimeData.getParameter("number")); 

                vtJoin.insertElementAt( runtimeData.getParameter("join"), k);
                vtDomainVal.insertElementAt( runtimeData.getParameter("SelectedDomainSel"), k);
                vtField.insertElementAt(runtimeData.getParameter("field"), k); 
                vtOp.insertElementAt( runtimeData.getParameter("operatorType"), k);  
                vtSearchVal.insertElementAt( runtimeData.getParameter("searchValue"), k);
                vtOpenBracket.insertElementAt( runtimeData.getParameter("openBracket"), k);
                vtCloseBracket.insertElementAt( runtimeData.getParameter("closeBracket"), k);


                  // if criteria is added for the domain
                if( vtSelectedDomains.contains(strSelectedDomainSel)){

                    String fieldVal =  runtimeData.getParameter("field");
                    fieldVal = fieldVal.substring(1, fieldVal.length() - 1);                    

                    DBField dbField = (DBField) DatabaseSchema.getFields().get(fieldVal);
                    String label = dbField.getLabelInForm();                    

                    vtFieldLabel.insertElementAt( label, k );
                    vtParticipantDomain.insertElementAt("blank", k);

                }else{// criteria is added for the smartform     

                    // get the dataelement name               
                    vtFieldLabel.insertElementAt( getDataelementName(runtimeData.getParameter("field")), k);                                    
                    vtParticipantDomain.insertElementAt( runtimeData.getParameter("participantDomain"), k );
                }
                
                runtimeData.setParameter("number", (String)null);

            }

            blCriteriaAdded = true;

        }
            
    }
    
    
    /** delete Search Criteria
     */
    private void deleteCriteria() 
    {
        
        if(runtimeData.getParameter("number") != null && !runtimeData.getParameter("number").equals("")){
            
            int k = Integer.parseInt(runtimeData.getParameter("number"));       
            
            // Delete the search criteria
            vtJoin.remove(k);
            vtDomainVal.remove(k);
            vtField.remove(k);
            vtOp.remove(k);
            vtSearchVal.remove(k);    
            vtFieldLabel.remove(k);
            vtOpenBracket.remove(k);
            vtCloseBracket.remove(k);
            vtParticipantDomain.remove(k);
            
            //set the next join to blank if the criteria deleted is the first && if delete criteria is clicked only
            if( k == 0 && runtimeData.getParameter("delete") != null ){
                if( vtJoin.isEmpty() == false){
                    vtJoin.set(0, "");
                }
            }
            
            if( runtimeData.getParameter("delete") != null ){
                runtimeData.setParameter("number", (String)null);
            }
            
        }        
    }
    
    
    /* get dataelement name
     */ 
    private String getDataelementName(String strDEId){
        
        String strResult = "";
        try{
            
            DALQuery query = new DALQuery();
            query.setDomain("DATAELEMENTS", null, null, null);     
            query.setField("DATAELEMENTS_strDataElementName", null);
            query.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);   
            query.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", strDEId, 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs = query.executeSelect();            
            
            while( rs.next() ){
                strResult = rs.getString("DATAELEMENTS_strDataElementName");                    
            }
            
            // Close the resultset
            rs.close();
            rs = null;

        }catch(Exception e){  
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
             return null;
        } 
        return strResult;
        
    }
    
        
    /** Build domains & smartforms selected, for which display fields will be defined
     */
    private void buildCriteriasDomainList() 
    { 
        // selected domains
        for( int i=0; i < vtSelectedDomains.size(); i++ ){
            String strDomain = (String)vtSelectedDomains.get(i);            
            
            if(!isCriteriaDomain(strDomain)){                             
                vtCriteriasDomains.add( strDomain );               
            } 
        }     
        
        //selected smartforms
        for( int j=0; j < vtSelectedSmartforms.size(); j++ ){
            
            String strDomain = (String)vtSelectedSmartforms.get(j);    
             if(!isCriteriaDomain(strDomain)){                                
                vtCriteriasDomains.add( strDomain );               
            } 
        } 
        
        //selected studys
        for( int k=0; k < vtSelectedStudys.size(); k++ ){
            
            String strDomain = (String)vtSelectedStudys.get(k);    
             if(!isCriteriaDomain(strDomain)){                                
                vtCriteriasDomains.add( strDomain );               
            } 
        } 
        
    } 
    
    
    /** Build domains to which criteria was defined in the xml
     */
    private String buildCriteriasDomainListXMLFile() 
    {
        buildCriteriasDomainList();
        
        String strResult = "";
                
        for(int i=0; i < vtCriteriasDomains.size(); i++){
            
            strResult+= "<buildCriteriasDomains>" +
                        "<domainName>" + vtCriteriasDomains.get(i) + "</domainName>";                 
            
            if((String)hashDomainStatus.get( vtCriteriasDomains.get(i) ) != null){
                strResult+= "<status>"+ (String)hashDomainStatus.get( vtCriteriasDomains.get(i) )+"</status>";
            }
            
            strResult+= "</buildCriteriasDomains>";

        }
        return strResult;            
    } 
    
    
    /*
     * Build the xml for the search Criteria & Missing Records criterias visibility
     */
    private String buildVisibilityXMLFile(){
        String strResult = "";
        
        if((String)hashVisibility.get("visibility1") != null ){
            strResult+= "<visibility1>"+ (String)hashVisibility.get("visibility1")+"</visibility1>";
        }
        if((String)hashVisibility.get("visibility2") != null ){
            strResult+= "<visibility2>"+ (String)hashVisibility.get("visibility2")+"</visibility2>";
        }
        
        if((String)hashVisibility.get("visibility3") != null ){
            strResult+= "<visibility3>"+ (String)hashVisibility.get("visibility3")+"</visibility3>";
        }

        return strResult;
    }
    
    
    /** Checks if a domain is in Criteria domain list
     */
    
    private boolean isCriteriaDomain(String strDomainName)
    {
        if (vtCriteriasDomains.size() < 1)
            return false;
        
        for (int i=0; i < vtCriteriasDomains.size(); i++)
            if (strDomainName.equals(vtCriteriasDomains.get(i)))
                return true;
        
        return false;
    }
    
    
    /** Build the list of fields for the criterias domain is EXPANDED
     */
    private String buildFieldsToSelect()
    {
        String strResult = "";    
        
        try{
            String strCriteriasDomains = runtimeData.getParameter("criteriasDomain");

            if(strCriteriasDomains != null && !strCriteriasDomains.equals("")){
                
                // if the expanded domain is a domain name, so not a smartform
                if( vtSelectedDomains.contains( strCriteriasDomains )){

                    if(!strCriteriasDomains.equals("BIOANALYSIS")){
                        
                        Vector vtFormFieldsAll = DatabaseSchema.getFormFields("advanced search "+strCriteriasDomains.toLowerCase());

                        if( vtFormFieldsAll.size() > 1 ){
                            Vector vtFormFields = QueryChannel.prepareFormfieldsForFieldSecurity(vtFormFieldsAll,authToken);                                    
                            for(int i=0; i < vtFormFields.size(); i++){
                                String strField = (String) vtFormFields.get(i);                     

                                    DBField dbField = (DBField) DatabaseSchema.getFields().get(strField);
                                    String strIntlName = dbField.getInternalName();
                                    strIntlName = strIntlName.substring(1, strIntlName.length() - 1);

                                    strResult+= "<selectFields>";
                                    strResult+= "<fieldForDomain>"+strCriteriasDomains+"</fieldForDomain>";
                                    strResult+= "<Label>"+ dbField.getLabelInForm() +"</Label>" +
                                                "<InternalName>"+ strIntlName +"</InternalName>" +
                                                "<LOVType>"+ dbField.getLOVType() +"</LOVType>";                 
                                    strResult+= "</selectFields>";

                            }
                        }
                    }else{
                        
                        // if field is built for Bioanalysis
                        strResult+= "<selectFields>";
                        strResult+= "<fieldForDomain>"+strCriteriasDomains+"</fieldForDomain>";
                        strResult+= "<Label>"+ strCriteriasDomains +"</Label>" +
                                    "<InternalName>nocheckbox</InternalName>" ;                                                 
                        strResult+= "</selectFields>";
                        
                    }

                }
                // the expanded domain is a smartform name
                else if(vtSelectedSmartforms.contains( strCriteriasDomains )){ 

                    ResultSet rs =  buildSmartformsDataelements(strCriteriasDomains);

                    // build fields to select when expanded
                    while( rs.next()){

                        strResult+= "<selectFields>";
                        strResult+= "<fieldForDomain>"+strCriteriasDomains+"</fieldForDomain>";
                        strResult+= "<Label>"+ rs.getString("DATAELEMENTS_strDataElementName") +"</Label>" +
                                    "<InternalName>"+ rs.getString("DATAELEMENTS_intDataElementID") +"</InternalName>" +
                                    "<LOVType>"+ rs.getString("DATAELEMENTS_intDataElementType") +"</LOVType>";                 
                        strResult+= "</selectFields>";

                    }
                    
                    // Close the resultset
                    rs.close();
                    rs = null;

                }
                // the expanded domain is a study name
                else if(vtSelectedStudys.contains( strCriteriasDomains )){             

                        strResult+= "<selectFields>";
                        strResult+= "<fieldForDomain>"+strCriteriasDomains+"</fieldForDomain>";
                        strResult+= "<Label>"+ strCriteriasDomains +"</Label>" +
                                    "<InternalName>nocheckbox</InternalName>" ;                                                 
                        strResult+= "</selectFields>";
                        
                }               
                
            }
            
        }catch(Exception e){ 
         LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
         return null;
        } 
    
        return strResult;
        
    }
    
    
    /* Build the fields that were already selected
     * on expanding this criteria domain
     *
     */
    /*
    private void buildSelectedFields(){       
        
        
        String strCriteriasDomains = runtimeData.getParameter("criteriasDomain");
        Vector vtFormFields = DatabaseSchema.getFormFields("advanced search "+strCriteriasDomains.toLowerCase());        
        // vector to hold the fields selected to display       
        Vector vtSelectedFields = new Vector (5,2);        
        
        // Check if each of the form field is selected or not
        for( int i=0; i < vtFormFields.size(); i++ ){
            
            //System.err.println((String)vtFormFields.get(i)+ ":" +runtimeData.getParameter((String)vtFormFields.get(i)));
            
            // If checkbox is selected, add it to the vector
            if(runtimeData.getParameter((String)vtFormFields.get(i)) != null){
               vtSelectedFields.add((String)vtFormFields.get(i));
            }
            
        }
        
        // Add the vector (which has the selected formfields) to the hashtable
        // hashtable key is the domain and value is vector containing the fields selected
         hashCriteriaDomains.put(strCriteriasDomains, vtSelectedFields);    
         //System.err.println("strCriteriasDomains in Hashtable:"+strCriteriasDomains);
                  
        
    }
     */
    
    
    /* Build the fields that are selected for all the criteria domain
     * while expanding one of the criteria domains
     *
     */
    
    private void buildSelectedFieldsForDomainsExpanded(){        
       
      try{
            Enumeration e = hashDomainStatus.keys();           

            while(e.hasMoreElements()){

                String strCriteriasDomains = (String) e.nextElement();            
                String strStatus = (String)hashDomainStatus.get(strCriteriasDomains);
                
                // vector to hold the fields selected to display       
                Vector vtSelectedFields = new Vector (5,2);        

                
                // Check if the domain is expanded, if so get their selected fields
                if(strStatus != null && strStatus.equals("expanded")){

                    // only in EDIT mode
                    if(strQueryID != null && !strQueryID.equals("")){

                        // Check if this domain contains fields as value in hashtable
                        if(hashCriteriaDomains.containsKey( strCriteriasDomains ) == true){
                            Vector vtList = (Vector)hashCriteriaDomains.get(strCriteriasDomains);            

                            // Expand is clicked
                            if( runtimeData.getParameter("open") != null && runtimeData.getParameter("open").equals( strCriteriasDomains )){
                                hashCriteriaDomains.put( strCriteriasDomains, vtList );   
                                //System.err.println("===============================stored vector is put back for this domain");
                            }
                        }
                    }

                    /* 1. if in add mode 
                     * 2. if domain does not have fields selected in hashtable
                     * 3. if in edit mode, and this domain is not clicked for expanding
                     *    ( but its values are captured when some other submit action takes place)
                     * 
                     */

                    if(( strQueryID != null && runtimeData.getParameter("open") == null) || ( strQueryID != null && !runtimeData.getParameter("open").equals(strCriteriasDomains) ) ||  ( strQueryID == null ) || (hashCriteriaDomains.containsKey( strCriteriasDomains ) == false)){
                    
                        // Check if the expanded domain is a domain name
                        if( vtSelectedDomains.contains(strCriteriasDomains) ){
                        
                            //System.err.println("====================it is a domain=======");
                            //String strCriteriasDomains = runtimeData.getParameter("criteriasDomain");
                            Vector vtFormFieldsAll = DatabaseSchema.getFormFields("advanced search "+strCriteriasDomains.toLowerCase());
                            Vector vtFormFields = QueryChannel.prepareFormfieldsForFieldSecurity(vtFormFieldsAll,authToken);        
                                                       
                            // Check if each of the form field is selected or not
                            for( int j=0; j < vtFormFields.size(); j++ ){
                                //System.err.println((String)vtFormFields.get(j)+ ":" +runtimeData.getParameter((String)vtFormFields.get(j)));
                                // If checkbox is selected, add it to the vector
                                if(runtimeData.getParameter((String)vtFormFields.get(j)) != null){
                                    //System.err.println("checkbox is checked :"+(String)vtFormFields.get(j));
                                   vtSelectedFields.add((String)vtFormFields.get(j));
                                }
                                                     
                            }                      
                            
                        }
                        // the expanded domain is a smartform name
                        else if( vtSelectedSmartforms.contains( strCriteriasDomains ) ){ 
                            
                                              
                            //System.err.println("=====================it is a smartform=============");
                            // get smartforms dataelements
                            ResultSet rs =  buildSmartformsDataelements(strCriteriasDomains);                

                            // Check if each of the smartforms dataelement id is selected or not
                            while( rs.next()){                            

                                String strDEId = rs.getString("DATAELEMENTS_intDataElementID");                            
                                // If checkbox is selected, add it to the vector
                                if(runtimeData.getParameter(strDEId) != null){
                                     vtSelectedFields.add(strDEId); 
                                     //System.err.println("checkbox is checked :"+strDEId);
                                }
                            }   

                            // Close the resultset
                            rs.close();
                            rs = null;
                            
                        }
                        
                        // the expanded domain is a study name
                        else if( vtSelectedStudys.contains( strCriteriasDomains ) ){                
                                              
                            //System.err.println("=====================it is a study=============");
                                                      
                                // If checkbox is selected, add it to the vector
                                if(runtimeData.getParameter( strCriteriasDomains ) != null){
                                     vtSelectedFields.add( strCriteriasDomains ); 
                                     //System.err.println("checkbox is checked :"+strCriteriasDomains);
                                }
                                              
                        }


                        // Add the vector (which has the selected formfields) to the hashtable
                        // hashtable key is the domain or smartform name and value is vector containing the fields selected
                        hashCriteriaDomains.put(strCriteriasDomains, vtSelectedFields);    
                        //System.err.println("================================strCriteriasDomains in buildSelectedFieldsForDomainsExpanded:"+strCriteriasDomains);

                    }
                }


            }// End of for loop

            // for debugging purpose
            /*
            Enumeration e1 = hashCriteriaDomains.keys();
            while(e1.hasMoreElements()){
                String strCriteriaDomain = (String)e1.nextElement();
                //System.err.println("domain:"+strCriteriaDomain);
               Vector vtFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);            

                for(int i=0; i < vtFields.size(); i++){
                    //System.err.println("field"+i+":"+vtFields.get(i));
                } 
            } 
             */
            
        
         }catch(Exception e){ 
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);

         }  
        
    }
    
    
    /*  Build the xml for the fields selected
     *  for all the domains
     */
    
    private String buildSelectedFieldsXMLFile(){
        
        String strResult = "";
        
        Enumeration e = hashCriteriaDomains.keys();
        
        while(e.hasMoreElements()){
            
            String strCriteriaDomain = (String) e.nextElement();            
            Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);
            
            if(strResult.equals("")){
                strResult = "<ListOfDomainsAndFields>";
            }else{
                strResult+= "<ListOfDomainsAndFields>";
            }
            
            strResult+= "<NameOfDomain>"+strCriteriaDomain+"</NameOfDomain>";
            for( int i=0; i < vtSelectedFields.size(); i++ ){                
                
                strResult+= "<CheckedFields>";
                strResult+= vtSelectedFields.get(i);                
                strResult+= "</CheckedFields>";
               
            }
            strResult+= "</ListOfDomainsAndFields>";
            
        }
        
        strResult += "<ListOfDomainsAndFields>";
        
        for (int i = 0; i < vtRequiredFields.size(); i++)
        {
            String[] tempString = (String []) vtRequiredFields.get(i);
            strResult += "<NameOfDomain>"+tempString[0]+"</NameOfDomain>";
            strResult+= "<CheckedFields forced=\"true\">" + tempString[1] + "</CheckedFields>";
        }
        strResult += "</ListOfDomainsAndFields>";
        
        return strResult;
        
    }
    
    
    /*
     * Build the hashtable with the current status of all the domains
     *
     */   

    private String buildDomainStatusXMLFile(){
        
        String strResult = "";
        
        Enumeration e = hashDomainStatus.keys();
        
        while(e.hasMoreElements()){
            
            String strCriteriaDomain = (String) e.nextElement();            
            String strStatus = (String)hashDomainStatus.get(strCriteriaDomain);      
            
            runtimeData.setParameter("criteriasDomain", strCriteriaDomain);            
            
            // NOT CHECKED NOW DOES NTO BUILD FIELDS FOR SAVE FOR COLLAPSED -"Build the fields only if the domain is expanded"
           //if(strStatus != null && strStatus.equals("expanded")){
                if(strResult.equals("")){
                    strResult = buildFieldsToSelect(); 
                }else{
                    strResult+= buildFieldsToSelect(); 
                }   
           // }            
            
        }
        
        return strResult;       
        
    }
    
    
    /*
     * Build Criteria value field according to the type of field 
     * as dropdown with LOV or as a textbox
     */
    private String buildCriteriaValueField(String strFieldSel){
        
        String strResult = "";
    try{
        
        if(strFieldSel != null && !strFieldSel.equals("")){
                
                // Remove "" from strFieldSel                 
                String strNewFieldSel = strFieldSel.substring(1, strFieldSel.length() - 1);
                
                // get the LOVType
                DBField dbField = (DBField) DatabaseSchema.getFields().get(strNewFieldSel);
                
                // field is not LOV, so display textbox
                if(dbField.getLOVType() == null || dbField.getLOVType().equals("null")){    

                    strResult+= "<selFieldType>text</selFieldType>";     

                }else{ //If the field is LOV type   
                    
                    DALQuery query = new DALQuery();
                    query.setDomain("LOV", null, null, null);
                    query.setWhere(null, 0, "LOV_strLOVType", "=", dbField.getLOVType(), 0, DALQuery.WHERE_HAS_VALUE);
                    query.setWhere("AND", 0, "LOV_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    query.setField("LOV_strLOVValue", null);
                    ResultSet rs = query.executeSelect();

                    strResult+= "<selFieldType>LOV</selFieldType>";  

                    while(rs.next()){
                        strResult+= "<ListOfValues>";
                        strResult+= "<LOV>"+ Utilities.cleanForXSL(rs.getString("LOV_strLOVValue")) +"</LOV>";
                        strResult+= "</ListOfValues>";
                    }   
                    
                    // Close the resultset
                    rs.close();
                    rs = null;
                }       

            }    
         
        return strResult;
        
        }catch(Exception e){ 
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
             return null;
        } 
        
    }
    
   
    /*
     * Build the Search Query based on the search criterias, execute
     *
     */
   private ResultSet executeSearchQuery(){
       
   try{
       
       DALQuery query = new DALQuery();      
       
       // Set the first domain
       // if only 1 study is selected and no domains are selected
       if( vtSelectedDomains.isEmpty() == true && vtSelectedStudys.size() == 1){
            query.setDomain("STUDY", null, null, null);  
            
       }else{
           
            if( vtSelectedDomains.isEmpty() == true || (vtSelectedDomains.size() == 1 && vtSelectedDomains.get(0).toString().equals("BIOANALYSIS")) ){
                strErrorMessage = "Please add another dataset";
                return null;
                
            }else{
                // if the first domain is not BIOANALYSIS
                if(!vtSelectedDomains.get(0).toString().equals("BIOANALYSIS")){
                    query.setDomain(vtSelectedDomains.get(0).toString(), null, null, null);                  
                }else{
                    // if the first domain is BIOANALYSIS
                    query.setDomain(vtSelectedDomains.get(1).toString(), null, null, null);                  
                }
            } 
            
       }
       
       if (vtSelectedDomains.contains("NOTES"))
       {
            if (vtSelectedDomains.contains("PATIENT"))
            {    
                query.setDomain("PATIENT", "NOTES_intID", "PATIENT_intInternalPatientID", "INNER JOIN");           
            }    
            if (vtSelectedDomains.contains("BIOSPECIMEN"))
            {    
                query.setDomain("BIOSPECIMEN", "NOTES_intID", "BIOSPECIMEN_intBiospecimenID", "INNER JOIN");           
            }    
       }    
       else
       {    
           // Set the other domains and the join between them
           // Get all the selected domains except first
           for (int i=1; i < vtSelectedDomains.size(); i++){

               String strDomain = (String)vtSelectedDomains.get(i);

               // if the first domain is BIOANALYSIS and if i is 1, skip since it is already set OR if the domain is BIOANALYSIS do not set
               if( (vtSelectedDomains.get(0).toString().equals("BIOANALYSIS") && i == 1) || (strDomain.equals("BIOANALYSIS")) ){}
               else{

                   // For each domain, get its join object               
                   Hashtable hashJoins = (Hashtable)DatabaseSchema.getSearchDomains().get(strDomain);

                       // Get all possible joins for this strDomain, with all the domains in vtSelectedDomains
                       for( int j=0; j < i; j++ ){

                           // Check to see if there is a join between strDomain and vtSelectedDomains(j)
                           if(hashJoins.containsKey((String)vtSelectedDomains.get(j))){ 

                               // Get the join object, which consists of join between strDomain and vtSelectedDomains.get(j)
                                DBJoin joinObj = (DBJoin)hashJoins.get(vtSelectedDomains.get(j));
                                query.setDomain( strDomain, joinObj.getFirstField(), joinObj.getSecondField(), joinObj.getType());
                                break; 
                           } 
                       }   
               }

           }
       }
       
       // Set the Consent and Consent Study domains if Study is selected & a domain is also selected
       if( vtSelectedDomains.isEmpty() == false  ){//&& vtSelectedStudys.size() > 0
           if(vtSelectedDomains.contains("PATIENT")){        
               
                // if Consent is selected & Consent Study is not selected, and Study is selected along with Patient
                if( vtSelectedDomains.contains("CONSENT") && !vtSelectedDomains.contains("CONSENTSTUDY") && vtSelectedStudys.isEmpty() == false ){
                    
                    query.setDomain( "CONSENTSTUDY", "CONSENTSTUDY_intConsentKey", "CONSENT_intConsentKey", "INNER JOIN");
                    query.setDomain( "STUDY", "STUDY_intStudyID", "CONSENTSTUDY_intStudyID", "INNER JOIN");                                 
                }                
                
                // if Consent & Consent Study is not selected, and Study is selected along with Patient
                if( !vtSelectedDomains.contains("CONSENT") && !vtSelectedDomains.contains("CONSENTSTUDY") && vtSelectedStudys.isEmpty() == false ){
                    query.setDomain( "CONSENT", "CONSENT_intInternalPatientID", "PATIENT_intInternalPatientID", "INNER JOIN");
                    query.setDomain( "CONSENTSTUDY", "CONSENTSTUDY_intConsentKey", "CONSENT_intConsentKey", "INNER JOIN");
                    query.setDomain( "STUDY", "STUDY_intStudyID", "CONSENTSTUDY_intStudyID", "INNER JOIN");            
                    
                }   
                // if Consent is selected & Consent Study is not selected, and Study is selected along with Patient
                if( vtSelectedDomains.contains("CONSENT") && vtSelectedDomains.contains("CONSENTSTUDY") && vtSelectedStudys.isEmpty() == false ){
                    query.setDomain( "STUDY", "STUDY_intStudyID", "CONSENTSTUDY_intStudyID", "INNER JOIN");                                 
                }                
                
           }
       }      
       
       
       // Build the form fields from all the fields selected in criteriadomains
       vtSearchFormFields.clear();
       boolean setStudy = false;
       
       for (int z=0; z<vtSelectedDomains.size(); z++)
       {
           if (hashCriteriaDomains.containsKey((String) vtSelectedDomains.get(z)))
           {
               String strCriteriaDomain = (String) vtSelectedDomains.get(z);
               Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);
               
                // if domain is a study, dont add Study Name as the formfield            
                for( int i=0; i < vtSelectedFields.size(); i++ ){                
                    // Set the selected field to formfield, to be used in search query
                    vtSearchFormFields.add((String)vtSelectedFields.get(i));
                }            
               
           }    
       }    

       
       // Check for formfields - if it is not empty proceed
       if( vtSearchFormFields.isEmpty() == false){ 
           
           // display the study name
           if(  vtSelectedDomains.isEmpty() == false && vtSelectedStudys.isEmpty() == false ){
               vtSearchFormFields.add("STUDY_strStudyName");
           }
       
           // Set the form fields
           query.setFields(vtSearchFormFields, null);             
           
           
           // Set the Where condition
           for(int i=0; i < vtJoin.size(); i++){ 

                String strOp = "";
                String strSearchVal = "";
                
                int intOpenBracket = getBracketNo(vtOpenBracket.get(i).toString());
                int intCloseBracket = getBracketNo(vtCloseBracket.get(i).toString());
                                
                strOp = dbOperator(vtOp.get(i).toString());
                if( strOp.equals("IS NULL") ){
                    strOp = "";
                    strSearchVal = "IS NULL";
                    
                }else if( strOp.equals("IS NOT NULL") ){
                    strOp = "";
                    strSearchVal = "IS NOT NULL";
                }

                String strIntlName = vtField.get(i).toString().substring(1, vtField.get(i).toString().length() - 1); 
                
                
                if( vtOp.get(i).toString().equals("Is empty") || vtOp.get(i).toString().equals("Is not empty")){
                         
                    // if condition contains EXCLUDING - and also NULL / NOT NULL
                    if( vtJoin.get(i).toString().equals("EXCLUDING")){
                                        
                         if(i == 0){
                            query.setWhere( "NOT", intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                        }else{
                            query.setWhere( "AND NOT", intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                        }

                    }else{ // if condition contains only NULL / NOT NULL
                    
                        if(i == 0){
                            query.setWhere( null, intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                        }else{
                            query.setWhere( vtJoin.get(i).toString(), intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                        }
                    }
                    
                }else if( vtJoin.get(i).toString().equals("EXCLUDING")){
                                        
                     if(i == 0){
                        query.setWhere( "NOT", intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );
                    }else{
                        query.setWhere( "AND NOT", intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );
                    }
                    
                }else{
                    
                    if(i == 0){
                        query.setWhere( null, intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );
                    }else{
                        query.setWhere( vtJoin.get(i).toString(), intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );
                    }
                }

            } 

            // if study is selected, add it to the where condition
           for( int j=0; j < vtSelectedStudys.size(); j++){
                if(vtJoin.size() == 0 && j == 0){
                    query.setWhere( null, 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(j).toString(), 0, DALQuery.WHERE_HAS_VALUE );
                }else{
                    if (j == 0 )
                    {
                        query.setWhere( "AND", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(j).toString(), 0, DALQuery.WHERE_HAS_VALUE );
                    }
                    else 
                    {
                        query.setWhere( "OR", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(j).toString(), 0, DALQuery.WHERE_HAS_VALUE );
                    }    
                }
           }
           
           
           // set the deleted fields
            String conn = "";
            if( vtJoin.size() == 0 && vtSelectedStudys.isEmpty() == true ){
                conn =null;
            }
            
            for( int m = 0; m < vtSelectedDomains.size(); m++){
                
                String name = (String)vtSelectedDomains.get(m);
                
                if(!name.equals("BIOANALYSIS")){
                    
                    name= name+"_intDeleted";

                    if( conn == null){
                        conn = "";
                        query.setWhere( null, 0, name, "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    }               
                    else{
                        query.setWhere( "AND", 0, name, "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    }
                }
            }
             
            if (vtSelectedDomains.contains("NOTES") && vtSelectedDomains.contains("PATIENT"))
            {
                query.setWhere( "AND", 0, "NOTES_strDomain", "=", "PATIENT", 0, DALQuery.WHERE_HAS_VALUE );
            }    
            else if (vtSelectedDomains.contains("NOTES") && vtSelectedDomains.contains("BIOSPECIMEN"))
            {
                query.setWhere( "AND", 0, "NOTES_strDomain", "=", "BIOSPECIMEN", 0, DALQuery.WHERE_HAS_VALUE );
            }                
                
            // if study is selected set the study deleted field
            if( vtSelectedStudys.size() > 0){
                query.setWhere( "AND", 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                
                if( !vtSelectedDomains.contains("CONSENT") ){
                    query.setWhere( "AND", 0, "CONSENT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                
                }
                if( !vtSelectedDomains.contains("CONSENTSTUDY") ){
                    query.setWhere( "AND", 0, "CONSENTSTUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                
                }
                
                // if PAT & Study is selected
                if( vtSelectedDomains.contains("PATIENT") && vtSelectedStudys.isEmpty() == false){
                    
                }
                
            }
            
           //System.err.println("QUERY================================: "+query.convertSelectQueryToString());
            
            // case sensitive
            if( runtimeData.getParameter("Sensitivity") != null ){
                query.setCaseSensitive(true);
            }else{
                query.setCaseSensitive(false);
            }

            if (ORDER_BY_BIOSPECIMEN)
            {
                if(vtSelectedDomains.contains("BIOSPECIMEN")){
                    query.setOrderBy("BIOSPECIMEN_intBiospecimenID", "ASC", false);
                }
                if(vtSelectedDomains.contains("PATIENT")){
                    query.setOrderBy("PATIENT_intInternalPatientID", "ASC", false);
                }
            }
            else
            {
                if(vtSelectedDomains.contains("PATIENT")){
                    query.setOrderBy("PATIENT_intInternalPatientID", "ASC", false);
                }
                if(vtSelectedDomains.contains("BIOSPECIMEN")){
                    query.setOrderBy("BIOSPECIMEN_intBiospecimenID", "ASC", false);
                }
            }

                       
            //System.err.println("QUERY================================: "+query.convertSelectQueryToString());
            //ResultSet rs = query.executeSelect();
            return query.executeSelect();
       }else{
            return null;
       }
        
       
    }catch(Exception e){  
         LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
         return null;
    } 
       
   }
    
   
    /*
     * Build the Search Query based on the search criterias with smartform selected, and execute
     *
     */
  private ResultSet executeSearchQueryForSmartform()
  {
     // create a BuildQuery object that will perform
     // the smartform search query and return the result set
     BuildQuery sfquery = new BuildQuery(this);
     
     return sfquery.executeSFSearch();   
  }    
      
  // renamed by Seena
  // this is temporary as the new method of executeSearchQueryForSmartform is being tested
  private ResultSet OLDexecuteSearchQueryForSmartform(){     
   try{
       
       DALQuery query = new DALQuery();
       DALQuery mainQuery = new DALQuery();
       DALQuery mainQuery1 = new DALQuery();
       DALQuery mainQuery2 = new DALQuery();
       String strSetSF = "";
       blSubQuery = false;       
       
       // Set the formfields for DISTINCT conditions           
       String strDistinct = "";               

       // if PAT & BIO is selected
       if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
            //strDistinct = "PATIENT_intInternalPatientID";
            strDistinct = "BIOSPECIMEN_intBiospecimenID";
       }
       // if only PAT is selected
       else if( vtSelectedDomains.contains("PATIENT")){
           strDistinct = "PATIENT_intInternalPatientID";
       }
       // if only BIO is selected
       else if( vtSelectedDomains.contains("BIOSPECIMEN") ){
           strDistinct = "BIOSPECIMEN_intBiospecimenID";
       }
       // if only study is selected, and no PAT/ BIO
       else if( vtSelectedStudys.isEmpty() == false){
           strDistinct = "STUDY_intStudyID";
       }
       vtSearchFormFields.clear();
       vtSearchFormFields.add(strDistinct);

       // Add BIO KEY to formfields only if PAT & BIO is selected, but it is hidden
       if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
            vtSearchFormFields.add("PATIENT_intInternalPatientID");
       }       
       

       // Build the form fields from all the fields selected in criteriadomains - only schema domains
       // And also the dataelements selected for the smartforms           
       vtSelectedDataelements.clear();
       vtSelectedSFDataelements.clear();       
       
       for (int z=0; z<vtSelectedDomains.size(); z++)
       {
           if (hashCriteriaDomains.containsKey((String) vtSelectedDomains.get(z)))
           {
               String strCriteriaDomain = (String) vtSelectedDomains.get(z);
               Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);
               
                // if domain is a study, dont add Study Name as the formfield            
                for( int i=0; i < vtSelectedFields.size(); i++ ){                
                    // Set the selected field to formfield, to be used in search query
                    vtSearchFormFields.add((String)vtSelectedFields.get(i));
                }            
               
           }    
       }    
       
       for (int y=0; y<vtSelectedSmartforms.size(); y++)
       {
           if (hashCriteriaDomains.containsKey((String) vtSelectedSmartforms.get(y)))
           {
               String strCriteriaDomain = (String) vtSelectedSmartforms.get(y);
               Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);
               
                // if domain is a study, dont add Study Name as the formfield            
                for( int j=0; j < vtSelectedFields.size(); j++ ){

                    vtSelectedDataelements.add( (String)vtSelectedFields.get(j) );
                    // Prepend the smartform name to the dataelement, this is to
                    // account for the the same dataelements belonging to different smartforms
                    // This is used when building the XML file for the search results page
                    vtSelectedSFDataelements.add ( (strCriteriaDomain + "__" + (String)vtSelectedFields.get(j)) );                    
                }            
           }    
       }    
       
       // When criterias are added for smartform and when dataelements are selected for display
       // do not execute in the normal way, but execute by putting the "query" object as subquery                                 
       if ( vtSelectedDataelements.isEmpty() == false ){
           for( int h = 0; h < vtSelectedSmartforms.size(); h++){
                if( vtDomainVal.contains( vtSelectedSmartforms.get(h).toString() ) ){
                    blSubQuery = true;
                    break;
                }   
           }   
       }    

       
       // Check if smartforms are selected
       if(vtSelectedSmartforms.size() > 0){       
           
            if(blSubQuery == true){
                 if(vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN")){
               
                   query.setDomain("PATIENT", null, null, null);
                   query.setDomain("BIOSPECIMEN", "BIOSPECIMEN_intPatientID", "PATIENT_intInternalPatientID", "INNER JOIN");
                 }
                  else if ( vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") ){
               
                        query.setDomain("PATIENT", null, null, null);  
                  }
                 else if( !vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
                   
                       query.setDomain("BIOSPECIMEN", null, null, null);         
                 }
                 else if ( !vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS") ){
                
                        query.setDomain("BIOSPECIMEN", null, null, null); 
                 }
                 else if( !vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedStudys.isEmpty() == false){
            
                         query.setDomain("STUDY", null, null, null);
                 }                 
            }
            else{
           
           // if PATIENT & BIO & SF is in the selected domain
           if(vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN")){
               
               query.setDomain("PATIENT", null, null, null);
               query.setDomain("BIOSPECIMEN", "BIOSPECIMEN_intPatientID", "PATIENT_intInternalPatientID", "INNER JOIN");
               
                               
                       // if Participant domain has  PAT & BIO/ PATIENT / BIO/ No PAT & BIO
                       if ( vtParticipantDomain.contains("PATIENT") && vtParticipantDomain.contains("BIOSPECIMEN") ){                  
               
                           query.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
                           query.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
                           query.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                           strSetSF = "pat-sf";
                           
                       }else if( vtParticipantDomain.contains("PATIENT") ){                       

                           query.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
                           query.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
                           query.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                           strSetSF = "pat-sf";
                       }else if ( vtParticipantDomain.contains("BIOSPECIMEN")){                       

                           query.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
                           query.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_SMARTFORM_intStudyID", "INNER JOIN");                           
                           query.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                           strSetSF = "bio-sf";
                       }
                       // if it contains criteria for only BIOANALYSIS, with PAT & BIO selected
                       else if ( vtParticipantDomain.contains("BIOANALYSIS") ){                           
                           query.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_intBiospecimenID", "SMARTFORMPARTICIPANTS_intParticipantID","INNER JOIN");
                           query.setDomain( "STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN");
                           strSetSF = "";
                       }                                          
                       else if ( vtParticipantDomain.contains("blank") || vtParticipantDomain.isEmpty() == true ){ // if participant domain is not defined
                       
                           query.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
                           query.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
                           query.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                           strSetSF = "pat-sf";
                       }              
               
           }            
           // if PAT & SF is selected & BIO is not selected
           else if ( vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") ){
               
               query.setDomain("PATIENT", null, null, null);                      
               query.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
               query.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
               query.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                   
               strSetSF = "pat-sf";        
           }           
           // if BIO & SF is selected and PAT & Study & ANALYSIS is not selected
           else if( !vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
          
               query.setDomain("BIOSPECIMEN", null, null, null);             
               query.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
               query.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_SMARTFORM_intStudyID", "INNER JOIN");
               query.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                       
               strSetSF = "bio-sf";
           }  
           // if it contains  BIOANALYSIS & not PAT / BIO / Study
           else if ( !vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS") ){
                
               query.setDomain("BIOSPECIMEN", null, null, null);                
               query.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_intBiospecimenID", "SMARTFORMPARTICIPANTS_intParticipantID","INNER JOIN");
               query.setDomain( "STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN");               
               strSetSF = "";
           } 
           // if study & SF is selected and no PAT & BIO is selected
           else if( !vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedStudys.isEmpty() == false){
            
                   query.setDomain("STUDY", null, null, null);
                   query.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intStudyID", "STUDY_intStudyID","INNER JOIN");                   
                   query.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                   
                   strSetSF = "pat-sf";       
           }   
           query.setDomain("SMARTFORM", "SMARTFORM_intSmartformID", "SMARTFORMPARTICIPANTS_intSmartformID", "INNER JOIN");           
           query.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN"); 
           query.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID","INNER JOIN");           

           query.setDomain("SMARTFORMRESULTS", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "SMARTFORMRESULTS_intSmartformParticipantID", "INNER JOIN");

            }           
            
           
           // Set the other domains and the join between them           
           for (int i=0; i < vtSelectedDomains.size(); i++){

               // For each domain, get its join object
               String strDomain = (String)vtSelectedDomains.get(i);
               
               if(!strDomain.equals("PATIENT") && !strDomain.equals("BIOSPECIMEN") && !strDomain.equals("BIOANALYSIS")){
               
                   Hashtable hashJoins = (Hashtable)DatabaseSchema.getSearchDomains().get(strDomain); 

                   // if the first domain is CONSENT
                   if( i == 0 && strDomain.equals("CONSENT")){
                       query.setDomain( "CONSENT", "PATIENT_intInternalPatientID", "CONSENT_intInternalPatientID", "INNER JOIN");
                       
                   }

                   // if the first domain is STUDY
                   else if( i == 0 && strDomain.equals("STUDY")){
                       query.setDomain( "STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN");
                       
                   }                                                        
                   
                   // Get all possible joins for this strDomain, with all the domains in vtSelectedDomains                   
                   for( int j=0; j < i; j++ ){

                       // Check to see if there is a join between strDomain and vtSelectedDomains(j)
                       if(hashJoins.containsKey((String)vtSelectedDomains.get(j))){ 
                           
                           // Get the join object, which consists of join between strDomain and vtSelectedDomains.get(j)
                            DBJoin joinObj = (DBJoin)hashJoins.get(vtSelectedDomains.get(j));
                            if(strDomain.equals("CONSENT")){
                                query.setDomain( "CONSENT", "PATIENT_intInternalPatientID", "CONSENT_intInternalPatientID", "INNER JOIN");
                            }else{
                                query.setDomain( strDomain, joinObj.getFirstField(), joinObj.getSecondField(), joinObj.getType());
                            }                            
                            break;
                       } 
                   }
               }
           }     
           
           // if first domain is CONSENTSTUDY, set it after consent & other domains have been set
           if(vtSelectedDomains.isEmpty() == false && vtSelectedDomains.get(0).toString().equals("CONSENTSTUDY")){
               query.setDomain( "CONSENTSTUDY", "CONSENT_intConsentKey", "CONSENTSTUDY_intConsentKey", "INNER JOIN");
           }    
           
           // if no subquery, then set the selected fields as formfields for display
           if( blSubQuery == false ){            

               // only if datelements are selected show smartform details
               if( vtSelectedDataelements.isEmpty() == false ){        

                   // it is a smartform so add smartform columns
                   // if patient & biospecimen is not selected
                   if(!vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN")){
                      vtSearchFormFields.add("SMARTFORMPARTICIPANTS_intParticipantID"); 
                   }
 
                   vtSearchFormFields.add("STUDY_intStudyID");
                   vtSearchFormFields.add("STUDY_strStudyName");
                   vtSearchFormFields.add("SMARTFORM_strSmartformName");
                   vtSearchFormFields.add("SMARTFORMPARTICIPANTS_intSmartformParticipantID");       
                   vtSearchFormFields.add("DATAELEMENTS_intDataElementID");                  
                   vtSearchFormFields.add("DATAELEMENTS_strDataElementName");                     
                   vtSearchFormFields.add("SMARTFORMRESULTS_strDataElementResult");
                   
               }

           }else{               
               // if blSubQuery is true do not set the other form fields, just set the distinct formfield
                vtSearchFormFields.clear();
                vtSearchFormFields.add(strDistinct);
           }
                             
           // Check for formfields - if it is not empty proceed
           if( vtSearchFormFields.isEmpty() == false){
               
               // Set the form fields
               query.setFields(vtSearchFormFields, null);
               
                              
//Seenadist               query.setDistinctField( strDistinct );     

               if( blSubQuery == false ){    
               // to get the smartform names
               for( int k=0; k < vtSelectedSmartforms.size(); k++ ){
                   
                   int last = vtSelectedSmartforms.size() - 1;
                  
                     if( vtSelectedSmartforms.size() == 1){
                        query.setWhere( null, 0, "SMARTFORM_strSmartformName", "=", (String)vtSelectedSmartforms.get(k), 0, DALQuery.WHERE_HAS_VALUE );                            
                     }else if(k == 0){
                        query.setWhere( null, 1, "SMARTFORM_strSmartformName", "=", (String)vtSelectedSmartforms.get(k), 0, DALQuery.WHERE_HAS_VALUE );                            
                     }else if( k == last){
                        query.setWhere( "OR", 0, "SMARTFORM_strSmartformName", "=", (String)vtSelectedSmartforms.get(k), 1, DALQuery.WHERE_HAS_VALUE );                            
                     }else{
                        query.setWhere( "OR", 0, "SMARTFORM_strSmartformName", "=", (String)vtSelectedSmartforms.get(k), 0, DALQuery.WHERE_HAS_VALUE );                            
                     }          
               }

               
               // Search by study
               // if study is selected, add it to the where condition
               for( int m=0; m < vtSelectedStudys.size(); m++){

                       int last = vtSelectedStudys.size() - 1;

                         if( vtSelectedStudys.size() == 1){
                            query.setWhere( "AND", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                         }else if(m == 0){
                            query.setWhere( "AND", 1, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                         }else if( m == last){
                            query.setWhere( "OR", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 1, DALQuery.WHERE_HAS_VALUE );                            
                         }else{
                            query.setWhere( "OR", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                         }          
               }
               
               
               // set the domain to Study/ Bio accordingly, only if participantDomain is not defined
               if ( vtParticipantDomain.isEmpty() == true ){
                   
                   if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
                       query.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Study", 0, DALQuery.WHERE_HAS_VALUE );                            
                   }
                   else if( vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") ){
                       query.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Study", 0, DALQuery.WHERE_HAS_VALUE );                            
                   }
                   else if( !vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
                       //&& !vtSelectedDomains.contains("BIOANALYSIS")
                       query.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Biospecimen", 0, DALQuery.WHERE_HAS_VALUE );                            
                   }
                   else if( !vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS")){
                       query.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Bioanalysis", 0, DALQuery.WHERE_HAS_VALUE );                            
                   }
                   
               }           
               
               // to get the dataelement name              
               query.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", "SMARTFORMRESULTS_intDataElementID", 0, DALQuery.WHERE_BOTH_FIELDS);
                     
               // set the smartforms dataelements selected in where condition
               int last = vtSelectedDataelements.size() - 1;
                
               for( int m=0; m < vtSelectedDataelements.size(); m++ ){

                    if( vtSelectedDataelements.size() == 1){
                        query.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSelectedDataelements.get(m), 0, DALQuery.WHERE_HAS_VALUE);
                    }else if( m == 0 ){
                        query.setWhere("AND", 1, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSelectedDataelements.get(m), 0, DALQuery.WHERE_HAS_VALUE);
                    }else if( m == last ){
                        query.setWhere("OR", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSelectedDataelements.get(m), 1, DALQuery.WHERE_HAS_VALUE);
                    }else{                            
                        // Set the selected field to where condition, to be used in search query                        
                        query.setWhere("OR", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSelectedDataelements.get(m), 0, DALQuery.WHERE_HAS_VALUE);
                    }

               }       
                                
                query.setWhere( "AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setWhere( "AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setWhere( "AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query.setWhere( "AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );           
                

                if ((strSetSF.equals( "pat-sf" ) || strSetSF.equals("bio-sf")) ){
                        query.setWhere( "AND", 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );   
                    }

                if( vtSelectedStudys.isEmpty() == false ){                    
//                    if (strSetSF.equals( "pat-sf" )){
//                        query.setWhere( "AND", 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );   
//                    }
                    
                }else{
                
                    if( strSetSF.equals( "pat-sf" )){
                       query.setWhere( "AND", 0, "PATIENT_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                                        
                    }else if( strSetSF.equals( "bio-sf" )){
                        query.setWhere( "AND", 0, "BIOSPECIMEN_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );

                    }
                }
                
               }               
               
               // set the deleted fields
                for( int m = 0; m < vtSelectedDomains.size(); m++){
                    String name = (String)vtSelectedDomains.get(m);              
                    
                    if(!name.equals("BIOANALYSIS")){
                        
                        name = name+"_intDeleted";
                        if((blSubQuery == true && m == 1 && vtSelectedDomains.get(0).toString().equals("BIOANALYSIS")) || (blSubQuery == true && m == 0)){
                            query.setWhere( null, 0, name, "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                        }else{
                            query.setWhere( "AND", 0, name, "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                        }
                    }
                }              
               
               // join **
                
               query.clearSiblingQueries();
               int lastCriteria = vtJoin.size() - 1;
               
               Vector vtQueries = new Vector(10, 2);
               
                // Set the Where condition for criterias
               for(int i=0; i < vtJoin.size(); i++){             
                   
                   int intOpenBracket = getBracketNo(vtOpenBracket.get(i).toString());
                   int intCloseBracket = getBracketNo(vtCloseBracket.get(i).toString());            
                   
                   if( i == 0){
                       vtJoin.set(0, "AND");
                   }
                   
                    DALQuery query1 = new DALQuery();
                   
                    if ( vtParticipantDomain.get(i).equals("BIOSPECIMEN")){// && vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN")
                        blPartBio = true;
                    }else{
                        blPartBio = false;
                    }
                    
                    // if Participant domain is BIOANALYSIS and PAT & BIO is selected
                    if(vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS")){   
                        if( vtParticipantDomain.get(i).equals("BIOANALYSIS")){
                            blPartBioAnalysis = true;
                        }
                    }else{
                        blPartBioAnalysis = false;
                    }
                    
                    // set the new query object "query1" to be same as object "query"
                    //query1 = setQueryObject();                    
                   
                    String strOp = "";
                    String strSearchVal = "";
                    
                    strOp = dbOperator(vtOp.get(i).toString());
                    if( strOp.equals("IS NULL") ){
                        strOp = "";
                        strSearchVal = "IS NULL";                    
                    }else if( strOp.equals("IS NOT NULL") ){
                        strOp = "";
                        strSearchVal = "IS NOT NULL";
                    }
                    
                   // if the criteria is added for schema domain
                   if( vtSelectedDomains.contains( vtDomainVal.get(i) )){                   
                       
                        String strIntlName = vtField.get(i).toString().substring(1, vtField.get(i).toString().length() - 1);
                        
                        if( vtOp.get(i).toString().equals("Is empty") || vtOp.get(i).toString().equals("Is not empty")){
                         
                            // if condition contains EXCLUDING - and also NULL / NOT NULL
                            if( vtJoin.get(i).toString().equals("EXCLUDING")){   
                                
                                query.setWhere( "AND NOT",intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                            }else{
                                 query.setWhere( vtJoin.get(i).toString(), intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                            }
                            
                        }else if( vtJoin.get(i).toString().equals("EXCLUDING")){ 
                            
                            query.setWhere( "AND NOT", intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );                            

                        }else{                            
                            query.setWhere( vtJoin.get(i).toString(), intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );
                        }                          
                        
                        
                   }else{ // criteria is added for the smartforms 
                       
                        // set the new query object "query1" to be same as object "query"
                        query1 = setQueryObject();
                        //System.err.println("setQueryObject: " +query1.convertSelectQueryToString());                        
                   
                        String SFName = (String)vtDomainVal.get(i);
                        String DEId = (String)vtField.get(i);                    
                        
                        if( vtOp.get(i).toString().equals("Is empty") || vtOp.get(i).toString().equals("Is not empty")){
                         
                            // if condition contains EXCLUDING - and also NULL / NOT NULL
                            if( vtJoin.get(i).toString().equals("EXCLUDING")){
                                
                                query1.setWhere( "AND NOT", 1, "SMARTFORMRESULTS_strDataElementResult", strOp, strSearchVal, 0, DALQuery.WHERE_HAS_NULL_VALUE );
                            }else{
//Seena start                               
                                if ((vtJoin.get(i).toString()).equals("OR"))
                                {    
                                    query1.setWhere( "AND", 1, "SMARTFORMRESULTS_strDataElementResult", strOp, strSearchVal, 0, DALQuery.WHERE_HAS_NULL_VALUE );                                
                                }
                                else 
                                {
                                    query1.setWhere( vtJoin.get(i).toString(), 1, "SMARTFORMRESULTS_strDataElementResult", strOp, strSearchVal, 0, DALQuery.WHERE_HAS_NULL_VALUE );                                
                                }    
//Seena end                                
                            }
                            
                        }else if( vtJoin.get(i).toString().equals("EXCLUDING")){                   
                           
                            query1.setWhere( "AND NOT", 1, "SMARTFORMRESULTS_strDataElementResult", strOp, vtSearchVal.get(i).toString(), 0, DALQuery.WHERE_HAS_VALUE );

                        }else{
//Seena start                            
                            if ((vtJoin.get(i).toString()).equals("OR"))
                            {
                                query1.setWhere( "AND", 1, "SMARTFORMRESULTS_strDataElementResult", strOp, vtSearchVal.get(i).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                            }
                            else 
                            {
                                query1.setWhere( vtJoin.get(i).toString(), 1, "SMARTFORMRESULTS_strDataElementResult", strOp, vtSearchVal.get(i).toString(), 0, DALQuery.WHERE_HAS_VALUE );
                            }                                
//Seena end                            
                        }
                        

			if( (vtParticipantDomain.get(i).toString().equals("BIOANALYSIS")) && (strOp.equals("=") || strOp.equals("LIKE") )  )
                        {
                            // Put all the criterias (the DEID and the result) for bio-analysis in a hashtable
                            // This is so that we can filter out results that do not match the exact Bioanalysis criteria from
                            // the database resultset
                            // This is necessary because the Bio-analysis domain allows for multiple instances of the same smartform
                            // Because the built query currently filters on the Biospecimen key and not the Smartform Participant key
                            // when one Biospecimen key has a smartform that matches the set bioanalysis criteria, all the other smartforms of the same name
                            // under that Biospecimen key will be returned
                            
                            String strBioAnalysisDE = SFName + "__" + DEId;
                            if (!hashBioAnalysisSFCriteria.containsKey(strBioAnalysisDE))
                            {
                                Vector vtBioAnalysisSFValue = new Vector(2,2); 
                                vtBioAnalysisSFValue.add(vtSearchVal.get(i).toString());
                                hashBioAnalysisSFCriteria.put (strBioAnalysisDE, vtBioAnalysisSFValue);
                            }
                            else 
                            {
                                Vector vtBioAnalysisSFValue = (Vector) hashBioAnalysisSFCriteria.get(strBioAnalysisDE);
                                hashBioAnalysisSFCriteria.remove(strBioAnalysisDE);
                                vtBioAnalysisSFValue.add(vtSearchVal.get(i).toString());
                                hashBioAnalysisSFCriteria.put (strBioAnalysisDE, vtBioAnalysisSFValue);
                            }

			}                                
                        
                        
                        // set the participant domain
                        if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN")){
                            if( vtParticipantDomain.get(i).toString().equals("PATIENT") ){
                                query1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Study" , 0, DALQuery.WHERE_HAS_VALUE );
                            }else if( vtParticipantDomain.get(i).toString().equals("BIOSPECIMEN") ){
                                query1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Biospecimen" , 0, DALQuery.WHERE_HAS_VALUE );
                            }else if( vtParticipantDomain.get(i).toString().equals("BIOANALYSIS") ){
                                
                                query1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Bioanalysis" , 0, DALQuery.WHERE_HAS_VALUE );
                            }else{
                                query1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Study" , 0, DALQuery.WHERE_HAS_VALUE );
                            }                            
                        }
                        
                        query1.setWhere( "AND", 0, "DATAELEMENTS_intDataElementID", "=", DEId, 1, DALQuery.WHERE_HAS_VALUE );
                        
                        // Set the case sensitivity of the dataelement result
                        if( runtimeData.getParameter("Sensitivity") != null ){
                            query1.setCaseSensitive(true);
                        }else{
                            query1.setCaseSensitive(false);
                        }
                        
                                      
                   String strJoin = "";
                   if( vtJoin.get(i).toString().equals("AND")){
                       strJoin = "INTERSECT";
                   }else if( vtJoin.get(i).toString().equals("OR")){
                       strJoin = "UNION";
                   }else if( vtJoin.get(i).toString().equals("EXCLUDING")){                                               
                       strJoin = "INTERSECT";
                   }
                                        
                   if(i == 0){                       
                       
                       query.setWhere("AND", getBracketNo(vtOpenBracket.get(i).toString()), strDistinct , "IN", query1, getBracketNo(vtCloseBracket.get(i).toString()), DALQuery.WHERE_HAS_SUB_QUERY);
// Seena                      query.setSiblingQuery("INTESECT", query1, getBracketNo(vtOpenBracket.get(i).toString()), getBracketNo(vtCloseBracket.get(i).toString()));
                                               
                   }else{                 
                       query.setSiblingQuery(strJoin, query1, getBracketNo(vtOpenBracket.get(i).toString()), getBracketNo(vtCloseBracket.get(i).toString()));
                   }
                   
                   
                   }                    
                    
                   // reset back to blank
                   if( i == 0){
                       vtJoin.set(0, "");
                   }
                }       
                
                // join **             
               
               // if subquery is not needed
               if( blSubQuery == false){
                    
                    // case sensitive
                    if( runtimeData.getParameter("Sensitivity") != null ){
                        query.setCaseSensitive(true);
                    }else{
                        query.setCaseSensitive(false);
                    }
                    

                    if(vtSelectedDomains.contains("PATIENT")){
                        query.setOrderBy("PATIENT_intInternalPatientID", "ASC", true);
                    }
                    if(vtSelectedDomains.contains("BIOSPECIMEN")){
                        query.setOrderBy("BIOSPECIMEN_intBiospecimenID", "ASC", true);
                    }
                    if ((vtSelectedDataelements.isEmpty() == false))
                    {
                        query.setOrderBy("STUDY_intStudyID", "ASC", true);
                    }   
                    
                    // Set orderby SF participant, only if dataelements are selected
                    if( vtSelectedDataelements.isEmpty() == false ){
                        query.setOrderBy("SMARTFORMPARTICIPANTS_intSmartformParticipantID", "ASC", true);
                    }
                    

                    //System.err.println("QUERY: "+query.convertSelectQueryToString());
                    //ResultSet rs = query.executeSelect();
                   return query.executeSelect();
                   
               }
               // if criterias are given for sf and sf dataelements are selected for display
               // then execute the query with subquery
               else{
                   strBioSF = "";
                  if( vtSelectedDomains.contains("PATIENT") || vtSelectedDomains.contains("BIOSPECIMEN") || (vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN")) ){
                 
                       if( vtSelectedDomains.contains("BIOSPECIMEN") && !vtSelectedDomains.contains("PATIENT") ){
                           strBioSF = "bio";
                       }                       
                       
                       mainQuery = executeSubQuery(); 

                       //System.err.println("executeSubQuery: " + mainQuery.convertSelectQueryToString());                                               

                       for(int i=0; i < vtJoin.size(); i++){            
                           
                           int intOpenBracket = getBracketNo(vtOpenBracket.get(i).toString());
                           int intCloseBracket = getBracketNo(vtCloseBracket.get(i).toString());
                                           
                           if( i == 0){
                               vtJoin.set(0, "AND");
                           }                 

                            String strOp = "";
                            String strSearchVal = "";

                            strOp = dbOperator(vtOp.get(i).toString());
                            if( strOp.equals("IS NULL") ){
                                strOp = "";
                                strSearchVal = "IS NULL";                    
                            }else if( strOp.equals("IS NOT NULL") ){
                                strOp = "";
                                strSearchVal = "IS NOT NULL";
                            }

                           // if the criteria is added for schema domain
                           if( vtSelectedDomains.contains( vtDomainVal.get(i) )){                   

                                String strIntlName = vtField.get(i).toString().substring(1, vtField.get(i).toString().length() - 1);

                                if( vtOp.get(i).toString().equals("Is empty") || vtOp.get(i).toString().equals("Is not empty")){

                                    // if condition contains EXCLUDING - and also NULL / NOT NULL
                                    if( vtJoin.get(i).toString().equals("EXCLUDING")){   
                                        
                                        mainQuery.setWhere( "AND NOT", intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                                    }else{
                                        mainQuery.setWhere( vtJoin.get(i).toString(), intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                                    }

                                }else if( vtJoin.get(i).toString().equals("EXCLUDING")){ 
                                   
                                    mainQuery.setWhere( "AND NOT", intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );                            

                                }else{                            
                                    mainQuery.setWhere( vtJoin.get(i).toString(), intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );
                                }
                           }
                            
                           // reset back to blank
                           if( i == 0){
                               vtJoin.set(0, "");
                           }

                       }                  

                       mainQuery.setWhere("AND", 0, strDistinct , "IN", query, 0, DALQuery.WHERE_HAS_SUB_QUERY);
                  
                  }// end of if PAT/ PAT & BIO is present
        
                 
                // UNION with bio - SF, only if there is a smartform criteria for Biospecimen
                if(  (vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN"))  ){                                
                  
                       strBioSF = "bio";
                       mainQuery1 = executeSubQuery();

                       for(int i=0; i < vtJoin.size(); i++){            
                           
                           int intOpenBracket = getBracketNo(vtOpenBracket.get(i).toString());
                           int intCloseBracket = getBracketNo(vtCloseBracket.get(i).toString());
                            
                           if( i == 0){
                               vtJoin.set(0, "AND");
                           }                 

                            String strOp = "";
                            String strSearchVal = "";

                            strOp = dbOperator(vtOp.get(i).toString());
                            if( strOp.equals("IS NULL") ){
                                strOp = "";
                                strSearchVal = "IS NULL";                    
                            }else if( strOp.equals("IS NOT NULL") ){
                                strOp = "";
                                strSearchVal = "IS NOT NULL";
                            }

                           // if the criteria is added for schema domain
                           if( vtSelectedDomains.contains( vtDomainVal.get(i) )){                   

                                String strIntlName = vtField.get(i).toString().substring(1, vtField.get(i).toString().length() - 1);

                                if( vtOp.get(i).toString().equals("Is empty") || vtOp.get(i).toString().equals("Is not empty")){

                                    // if condition contains EXCLUDING - and also NULL / NOT NULL
                                    if( vtJoin.get(i).toString().equals("EXCLUDING")){   
                                        
                                        mainQuery1.setWhere( "AND NOT", intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                                    }else{
                                         mainQuery1.setWhere( vtJoin.get(i).toString(), intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                                    }

                                }else if( vtJoin.get(i).toString().equals("EXCLUDING")){ 
                                    
                                    mainQuery1.setWhere( "AND NOT", intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );                            

                                }else{                            
                                    mainQuery1.setWhere( vtJoin.get(i).toString(), intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );
                                }
                           }

                           // reset back to blank
                           if( i == 0){
                               vtJoin.set(0, "");
                           } 
                            
                       }                  

                       mainQuery1.setWhere("AND", 0, strDistinct , "IN", query, 0, DALQuery.WHERE_HAS_SUB_QUERY);
                       
                       // Set the case sensitivity of the dataelement result
                       if( runtimeData.getParameter("Sensitivity") != null ){
                           mainQuery1.setCaseSensitive(true);
                       }else{
                           mainQuery1.setCaseSensitive(false);
                       }                        
                                              
                       mainQuery.setSiblingQuery("UNION", mainQuery1);
                
                }// End of if BIO / PAT & BIO is present    
                  
                // UNION with bioanalysis - bio - SF =====> if BIOANALYSIS is also selected and only if there is a smartform criteria for BIOANALYSIS
                if(  (vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS")) ){                  
                                
                       strBioSF = "bioanalysis";
                       mainQuery2 = executeSubQuery();

                       for(int i=0; i < vtJoin.size(); i++){            
                           
                           int intOpenBracket = getBracketNo(vtOpenBracket.get(i).toString());
                           int intCloseBracket = getBracketNo(vtCloseBracket.get(i).toString());
                            
                           if( i == 0){
                               vtJoin.set(0, "AND");
                           }                 

                            String strOp = "";
                            String strSearchVal = "";

                            strOp = dbOperator(vtOp.get(i).toString());
                            if( strOp.equals("IS NULL") ){
                                strOp = "";
                                strSearchVal = "IS NULL";                    
                            }else if( strOp.equals("IS NOT NULL") ){
                                strOp = "";
                                strSearchVal = "IS NOT NULL";
                            }

                           // if the criteria is added for schema domain
                           if( vtSelectedDomains.contains( vtDomainVal.get(i) )){                   

                                String strIntlName = vtField.get(i).toString().substring(1, vtField.get(i).toString().length() - 1);

                                if( vtOp.get(i).toString().equals("Is empty") || vtOp.get(i).toString().equals("Is not empty")){

                                    // if condition contains EXCLUDING - and also NULL / NOT NULL
                                    if( vtJoin.get(i).toString().equals("EXCLUDING")){   
                                        
                                        mainQuery2.setWhere( "AND NOT", intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                                    }else{
                                        mainQuery2.setWhere( vtJoin.get(i).toString(), intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                                    }

                                }else if( vtJoin.get(i).toString().equals("EXCLUDING")){ 
                                    
                                    mainQuery2.setWhere( "AND NOT", intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );                            

                                }else{                            
                                    mainQuery2.setWhere( vtJoin.get(i).toString(), intOpenBracket, strIntlName, strOp, vtSearchVal.get(i).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );
                                }
                           }

                           // reset back to blank
                           if( i == 0){
                               vtJoin.set(0, "");
                           } 
                            
                       }                  

                       mainQuery2.setWhere("AND", 0, strDistinct , "IN", query, 0, DALQuery.WHERE_HAS_SUB_QUERY);
                       
                       // Set the case sensitivity of the dataelement result
                       if( runtimeData.getParameter("Sensitivity") != null ){
                           mainQuery2.setCaseSensitive(true);
                       }else{
                           mainQuery2.setCaseSensitive(false);
                       }
                       
                       mainQuery.setSiblingQuery("UNION", mainQuery2);
                
                }// End of if BIO & BIOANALYSIS                               
                                          
                // case sensitive
                if( runtimeData.getParameter("Sensitivity") != null ){
                    mainQuery.setCaseSensitive(true);
                }else{
                    mainQuery.setCaseSensitive(false);
                }
                    
                if(vtSelectedDomains.contains("PATIENT")){
                    mainQuery.setOrderBy("PATIENT_intInternalPatientID", "ASC", true);
                }
                if(vtSelectedDomains.contains("BIOSPECIMEN")){
                    mainQuery.setOrderBy("BIOSPECIMEN_intBiospecimenID", "ASC", true);
                }
                
                if ( (vtSelectedDataelements.isEmpty() == false) )
                {    
                    mainQuery.setOrderBy("STUDY_intStudyID", "ASC", true);
                }
                    
                if( vtSelectedDataelements.isEmpty() == false ){
                    mainQuery.setOrderBy("SMARTFORMPARTICIPANTS_intSmartformParticipantID", "ASC", true);
                }
                   

                //System.err.println("mainQuery: "+mainQuery.convertSelectQueryToString());                    

               return mainQuery.executeSelect(); 
                   
               }
               
           
          }// end of isEmpty()
           
           if( blSubQuery == false ){

                //System.err.println("query: "+query.convertSelectQueryToString());
                return query.executeSelect();
           }else{              

                //System.err.println("mainQuery: "+mainQuery.convertSelectQueryToString());                    
                return mainQuery.executeSelect();    
           }
           
       }else{
           return null;
       } 
       
       }catch(Exception e){  
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
             return null;
       } 
       
   }
   
   
   /*
    * Pass the operator as defined and get the equivalent database operator
    */
   protected String dbOperator(String op){
      String strResult = "";
       
      if(op.equals("Is equal to")){
           strResult = "=";
      }else if(op.equals("Is less than")){
           strResult = "<";
      }else if(op.equals("Is greater than")){
           strResult = ">";
      }else if(op.equals("Contains")){
           strResult = "LIKE";
      }else if(op.equals("Greater than equals to")){
           strResult = ">=";
      }else if(op.equals("Less than equals to")){
           strResult = "<=";
      }else if(op.equals("Is empty")){
           strResult = "IS NULL";
      }else if(op.equals("Is not empty")){
           strResult = "IS NOT NULL";
      }else{
           strResult = "=";
      }
       
       return strResult;
       
   }
    
   
    /*
     * Build the Search Query based on the search criterias, execute
     * and display the search result
     */    
    private String buildSearchResultXMLFile(){       
               
        String strResult = "";
         try{     
            
            String strError = validateBrackets() ;
            
            // validate the number of open & closing brackets
            if(!strError.equals("")){
                
                strErrorMessage = strError;
                return null;
                
            }             
            
            // Keep this order of precedence for building the search
            // if Criteria is given for searching missing records
            if( vtMissingFor.isEmpty() == false || vtMissingSFDomain.isEmpty() == false){
                strResult+= buildSearchResultXMLForSchemaDomains();
            }
            //if smartforms are selected 
            else if(vtSelectedSmartforms.isEmpty() == false ){
                
                strResult+= buildSearchResultXMLForSmartform();
                
            }else{
                strResult+= buildSearchResultXMLForSchemaDomains();
            }                 

            strResult += "<pickerMode>" + blPickerMode + "</pickerMode>";
            strResult += "<finishButton label=\"" + strFinishButtonLabel + "\">" + blFinishButtonVisible + "</finishButton>";
        }catch(Exception e){  
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
             return null;
        } 
        
        return strResult;
    }
    

    
    /*
     * Build search result when smartform is in the selected domain list
     */
    private String buildSearchResultXMLForSmartform(){
        
        String strResult = "";
        // Holds the number when dataelement name starts
        int cnt = vtSearchFormFields.size();
        // Holds the dataelements selected
        Vector vtDataElements = new Vector( 10, 2);
        
        // Holds the list smartform names for the selected dataelements
        Vector vtSFs = new Vector( 10, 2);
        
        Vector vtDataElementsPerSF = new Vector( 10, 2);
        
        // Holds the smartform name that a dataelement belongs to
        String strSFName = "";
        
        // The dataelements related to a particular smartform
        int intDECnt=0;
        // The number of columns that are not related to a smartform
        int intSchemaCnt = 0;
        
        try{
            
            // if perform search has been clicked, perform the query
            if ( (blPerformSearch == true)  || (intLastDataRow < intRequiredEndRow) || (intFirstDataRow > intRequiredStartRow))
            {
                ResultSet rs = executeSearchQueryForSmartform();
                if (rs != null)
                {
                    if(vtSelectedDomains.contains("BIOANALYSIS") || vtSelectedDomains.contains("ADMISSIONS")){
                        
                        boolean blSFResultsSepLine = false;
                        if ((vtSelectedDomains.contains("BIOANALYSIS") && PROPERTY_BIOANALYSISSF_SEP_LINE == true) || (vtSelectedDomains.contains("ADMISSIONS") && PROPERTY_COLLECTIONSF_SEP_LINE == true))
                        {
                            blSFResultsSepLine = true;
                        }
                        vtSearchResult = lookUpRecords(rs, vtSearchFormFields, blSFResultsSepLine, intRequiredStartRow, intRequiredEndRow, 0 );
                    }
                    else
                    {
                        vtSearchResult = lookUpRecords(rs, vtSearchFormFields, false, intRequiredStartRow, intRequiredEndRow, 0 );
                    }    

                    // Set the number of rows
                    intRecordSetSize = intTotalResultRows;
                    rs.close();
                }
                // if no fields are selected
                else if(vtSearchFormFields.isEmpty() == true){
                    strErrorMessage = "Please select fields to be displayed";
                    return null;
                }
                // if it throws exception
                else{
                    strErrorMessage = "Please refine the search. Invalid search.";
                    return null;
                }

                
                // Close the resultset
                rs.close();
                rs = null;
                
            }

            
            cnt = vtSearchFormFields.size();
            
            // perform the paging calculation
            pagingCalculation();
            
            //check if the resultset is empty or not
            boolean blHasData = false;

            if(!vtSearchResult.isEmpty() ){
                blHasData = true;
            }

            // Build the headers for all the selected fields except smartform's dataelements
            for( int i=1; i < vtSearchFormFields.size(); i++ ){                    

                if( !hashNonDisplayFields.contains(vtSearchFormFields.get(i).toString()) ){

                    DBField field = (DBField) DatabaseSchema.getFields().get(vtSearchFormFields.get(i).toString()); 
                    String strFormFieldName = field.getExternalName();

                    // If the formfield is not a question, build the header
                    // Basically build all the formfields as headers until the formfield is a dataelement name                           
                    if( !strFormFieldName.equals("ix_dataelements.\"DATAELEMENT_NAME\"")){         

                        strResult+= "<headers><name>"+ field.getLabelInColumn() +"</name></headers>";
                        intSchemaCnt++;

                    }else{
                        cnt = i-1;
                        strResult+= "<intSchemaListCount>" + intSchemaCnt + "</intSchemaListCount>";
                        break;                        
                    }
                }// End of if
            }

            
            // Build the headers for the smartform elements
            
            vtDataElements.clear();

            DALQuery query = new DALQuery();

            for( int i=0; i < vtSelectedDataelements.size(); i++ ){                    

                query.reset();                    
                query.setDomain("DATAELEMENTS", null, null, null);
                query.setField("DATAELEMENTS_strDataElementName", null);                
                query.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=", (String) vtSelectedDataelements.get(i), 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsResult = query.executeSelect();

                while( rsResult.next() ){

                String strDataElementID = (String) vtSelectedSFDataelements.get(i);
                String strDataElementName = rsResult.getString("DATAELEMENTS_strDataElementName"); 

                    if( !vtDataElements.contains( strDataElementID )){

                        vtDataElements.add( strDataElementID );                        
                        strResult+= "<headers><name>"+ strDataElementName +"</name></headers>";                    
                    }

                }
                
                // Close the resultset
                rsResult.close();
                rsResult = null;
            }
            
            // Get the list of smartforms for which dataelements have been selected for display
            // so that above the headers we can display which smartform a dataelement belongs to
            vtSFs.clear();
            vtDataElementsPerSF.clear();
                        
            for( int i=0; i < vtSelectedSFDataelements.size(); i++ ){              
                
                // Get the smartform name and dataelement ID
                String SFDataelementData[] = vtSelectedSFDataelements.get(i).toString().split("__");;




                
                // Get the smartform name to which the dataelement belongs
                if (!vtSFs.contains(SFDataelementData[0]))
                {
                    // Get the name of the smartform name to display as header
                    vtSFs.add(SFDataelementData[0]);
                    // Get the number of dataelements belonging to the smartform
                    if(intDECnt!=0){                            
                        vtDataElementsPerSF.add(Integer.toString(intDECnt));



                    }
                        
                    intDECnt=1;



                }
                else
                {
                    intDECnt++;                
                }    
                 
            }
            
            
            // Add the last entry corresponding to the last smartform
            if(intDECnt!=0){                            
                vtDataElementsPerSF.add(Integer.toString(intDECnt));
            }
            
            
            for (int sf=0; sf<vtSFs.size(); sf++)
            {
                strResult+= "<smartformlist><num>" + (sf+1) + "</num><name>" + (String) vtSFs.get(sf) + "</name><count>" + (String) vtDataElementsPerSF.get(sf) + "</count></smartformlist>";                    
            }    
            

            int intPageEndRecord;
            if ( (intStartRecord + intRecordPerPage) >= intTotalResultRows)
                intPageEndRecord = intTotalResultRows;                
            else 
                intPageEndRecord = (intStartRecord + intRecordPerPage);
            
            // subtract intStartRecord-intRequiredStartRow to get the entries in the vector
            // that correspond to the required rows for display
            for (int i=(intStartRecord-intRequiredStartRow); i < (intPageEndRecord-intRequiredStartRow); i++)
            {

                // Build the rows with records
                // XXX: Modified by daniel for picker code
                strResult += "<row id=\"" + i + "\"";
                if (htFlaggedItems != null && htFlaggedItems.get(i + "") != null)
                {
                    strResult += " flagged=\"true\">";
                }
                else
                {
                    strResult += " flagged=\"false\">";
                }
                
                // Build the results for non-smartform data - Patient, Biospecimen, etc
                for (int j=1; j < cnt; j++)
                { 
                    if( !hashNonDisplayFields.contains(vtSearchFormFields.get(j).toString()))
                    {

                        String value = ((Hashtable) vtSearchResult.get(i)).get((String) vtSearchFormFields.get(j)).toString();                
                            if ( (value == null) || (value.equals("Empty")) )
                                value = "";

                        strResult += "<record><value>" + Utilities.cleanForXSL(value) + "</value></record>";
                    }
                }    

                // Build the results for smartform data - Patient, Biospecimen, etc
                for( int k = 0; k < vtDataElements.size(); k++ )
                {
                    String value;
                    if (((Hashtable) vtSearchResult.get(i)).get((String) vtDataElements.get(k)) == null )
                    {
                        value = "";
                    }
                    else
                    {
                        value = ((Hashtable) vtSearchResult.get(i)).get((String) vtDataElements.get(k)).toString();                
                        if (value.equals("Empty"))
                            value = "";                    
                    }    

                    strResult += "<record><value>" + Utilities.cleanForXSL(value) + "</value></record>";
                }    

                strResult += "</row>";

            }


            // paging
            strResult += "<intCurrentPage>" + intCurrentPage + "</intCurrentPage>" +
                         "<intRecordPerPage>" + intRecordPerPage + "</intRecordPerPage>" +
                         "<intMaxPage>" + intMaxPage + "</intMaxPage>" +
                         "<strDelimiter>"+ Utilities.cleanForXSL(strDelimiter) +"</strDelimiter>" +
                         "<blHasData>" + blHasData + "</blHasData>" +
                         "<strFileName>" + strFileName + "</strFileName>" + 
                         "<intTotalRecords>" + intTotalResultRows  + "</intTotalRecords>";

            strStylesheet = ISEARCH_RESULT;                    
            strCurrent = ISEARCH_RESULT;                         

            return strResult;

        }catch(Exception e){  
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
             return null;
        }  
        
    }
    
    
     /*
      * Build search result when smartform is not in the selected domain list
      */
    private String buildSearchResultXMLForSchemaDomains(){
        
        String strResult = "";
        
        try{
            
            
            // if perform search has been clicked, perform the query
            if (blPerformSearch == true || (intLastDataRow < intRequiredEndRow) || (intFirstDataRow > intRequiredStartRow))
            {                
                ResultSet rs = null;
                
                // if Criteria is given for searching missing records
                if(vtMissingFor.isEmpty() == false){
                    rs = executeSearchQueryForMissingRecords();
                }
                else if (vtMissingSFDomain.isEmpty() == false){

                    rs = executeSearchQueryForMissingSFResults ();               

                    // Get the domain in the criterion : Patient or Biospecimen
                    String strMissingSFDomain = vtMissingSFDomain.get(0).toString();

                    // Get the formfields for display
                    
                   for (int z=0; z<vtSelectedDomains.size(); z++)
                   {
                       if (hashCriteriaDomains.containsKey((String) vtSelectedDomains.get(z)))
                       {
                           String strCriteriaDomain = (String) vtSelectedDomains.get(z);

                           // Only display the fields for the domain in the criterion
                           if (strCriteriaDomain.equals(strMissingSFDomain))
                           {    

                               Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);

                               // if domain is a study, dont add Study Name as the formfield            
                               for( int i=0; i < vtSelectedFields.size(); i++ ){                
                                    // Set the selected field to formfield, to be used in search query
                                    vtSearchFormFields.add((String)vtSelectedFields.get(i));
                               } 
                           }    

                       }    
                   }    

                }    
                else{
                    rs = executeSearchQuery();
                }                 
                
                
                if (rs != null)
                {
                    
                    vtSearchResult = lookUpRecords(rs, vtSearchFormFields, false, intRequiredStartRow, intRequiredEndRow, 0 );
                    // Set the number of rows
                    intRecordSetSize = intTotalResultRows;
                }
                // if no fields are selected
                else if(vtSearchFormFields.isEmpty() == true){
                    strErrorMessage = "Please select fields to be displayed";
                    return null;
                }
                // if it throws exception
                else{
                    strErrorMessage = "Please refine your search. Invalid search criteria.";
                    return null;
                }
                
               
                // Close the resultset
                rs.close();
                rs = null;

            }

                
            // Do the paging calculation
            pagingCalculation();
            
            boolean blHasData = false;

            // Build the headers
            for( int i=0; i < vtSearchFormFields.size(); i++ ){

                if(  !hashNonDisplayFields.contains(vtSearchFormFields.get(i).toString()) ){
                    DBField field = (DBField) DatabaseSchema.getFields().get(vtSearchFormFields.get(i).toString());
                    strResult+= "<headers><name>"+ field.getLabelInColumn() +"</name></headers>";
                }
            }

            //check if the resultset is empty or not
            if(!vtSearchResult.isEmpty()){
                blHasData = true;
            }

            
            int intPageEndRecord;
            if ( (intStartRecord + intRecordPerPage) >= intTotalResultRows)
                intPageEndRecord = intTotalResultRows;                
            else 
                intPageEndRecord = (intStartRecord + intRecordPerPage);
            
            
            // subtract intStartRecord-intRequiredStartRow to get the entries in the vector
            // that correspond to the required rows for display
            for (int i=(intStartRecord-intRequiredStartRow); i < (intPageEndRecord-intRequiredStartRow); i++)
            {

                // Build the rows with records
                strResult += "<row id=\"" + i + "\"";
                if (htFlaggedItems != null && htFlaggedItems.get(i + "") != null)
                {
                    strResult += " flagged=\"true\">";
                }
                else
                {
                    strResult += " flagged=\"false\">";
                }
                
                for (int j=0; j < vtSearchFormFields.size(); j++)
                { 
                    if( !hashNonDisplayFields.contains(vtSearchFormFields.get(j).toString())){
                        String value = ((Hashtable) vtSearchResult.get(i)).get((String) vtSearchFormFields.get(j)).toString();                
                            if ( (value == null) || (value.equals("Empty")) )
                                value = "";

                        strResult += "<record><value>" + Utilities.cleanForXSL(value) + "</value></record>";
                    }
                }    
                
                strResult += "</row>";
            }

             // paging
            strResult += "<intCurrentPage>" + intCurrentPage + "</intCurrentPage>" +
                         "<intRecordPerPage>" + intRecordPerPage + "</intRecordPerPage>" +
                         "<intMaxPage>" + intMaxPage + "</intMaxPage>" +
                         "<strDelimiter>"+ Utilities.cleanForXSL(strDelimiter) +"</strDelimiter>" +
                         "<blHasData>" + blHasData + "</blHasData>" +
                         "<strFileName>" + strFileName + "</strFileName>" + 
                         "<intTotalRecords>" + intTotalResultRows  + "</intTotalRecords>";

            strStylesheet = ISEARCH_RESULT;                    
            strCurrent = ISEARCH_RESULT;                         

            return strResult;
            
        }catch(Exception e){  
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
             return null;
        }  
        
    }
   
        
    /*
     * Save Query
     */
    private void saveQuery(){
        
    try{            
            
            Vector vtValidate = DatabaseSchema.getFormFields("isearch_validate_queryname");
                                    
            if ( runtimeData.getParameter("QueryName") == null ||  runtimeData.getParameter("QueryName").equals("")){ 
                strErrorMessage = "Please enter a value for query name";
            }      
            // if criteria is not updated and save is clicked
            else if( runtimeData.getParameter("number") != null && !runtimeData.getParameter("number").equals("null") ){
                strErrorMessage = "Please update the edited criteria to save the query";
            }                      
            else{ 

                // get the domain list & smartform list that is selected
                String strSeltdDomainList = "";
                String strSeltdSmartformList = "";
                String strSeltdStudyList = "";
                
                for(int i = 0; i < vtSelectedDomains.size(); i++){          
                    
                    if(strSeltdDomainList.equals("")){
                        strSeltdDomainList = (String) vtSelectedDomains.get(i) + ",";
                        
                    }else{ 
                        strSeltdDomainList+= (String) vtSelectedDomains.get(i) + ",";
                    }
                }       
                
                // get the selected smartforms
                for(int j = 0; j < vtSelectedSmartforms.size(); j++){                    
                  
                    if(strSeltdSmartformList.equals("")){
                        strSeltdSmartformList = (String) vtSelectedSmartforms.get(j) + ",";
                        
                    }else{ 
                        strSeltdSmartformList+= (String) vtSelectedSmartforms.get(j) + ",";
                    }
                }    
                
                // get the selected studys
                for(int k = 0; k < vtSelectedStudys.size(); k++){                    
                  
                    if(strSeltdStudyList.equals("")){
                        strSeltdStudyList = (String) vtSelectedStudys.get(k) + ",";
                        
                    }else{ 
                        strSeltdStudyList+= (String) vtSelectedStudys.get(k) + ",";
                    }
                }                    
                          
                String strXMLInserted =  strXML.toString();
                
                               
                               
                // insert a new query
                if(strQueryID == null || strQueryID.equals("")){
                
                    int intID = QueryChannel.getNewestKeyAsInt("FAVOURITEQUERY_intQueryID") + 1;

                    // Add the Query Id and the name to the xml
                    strXMLInserted+= "<strQueryID>"+ intID +"</strQueryID>" +
                                     "<strQueryName>"+ Utilities.cleanForXSL(runtimeData.getParameter("QueryName")) +"</strQueryName>" +
                                     "<strRadioVal>"+ strRadioVal +"</strRadioVal>" +
                                     "<strSaveAs>"+ runtimeData.getParameter("saveAs") +"</strSaveAs>";

 
                    DALQuery query = new DALQuery();
                    query.setDomain("FAVOURITEQUERY", null, null, null);                    
                    query.setField("FAVOURITEQUERY_strUserName", (String)authToken.getUserIdentifier());
                    query.setField("FAVOURITEQUERY_strQueryName", Utilities.cleanForXSL(runtimeData.getParameter("QueryName")));
                    query.setField("FAVOURITEQUERY_strXMLString", strXMLInserted.toString());
                    query.setField("FAVOURITEQUERY_strDomainList", strSeltdDomainList); 
                    query.setField("FAVOURITEQUERY_strDisplayAs", strRadioVal);
                    query.setField("FAVOURITEQUERY_strSaveAs", runtimeData.getParameter("saveAs"));
                    query.setField("FAVOURITEQUERY_strSmartformList", strSeltdSmartformList);   
                    query.setField("FAVOURITEQUERY_strStudyList", strSeltdStudyList); 
                    query.setField("FAVOURITEQUERY_intDeleted", "0");
                    query.executeInsert();  
                    
                    // QueryID
                    strQueryID = QueryChannel.getNewestKeyAsString(query);
                                    
                    strErrorMessage = "Query added successfully";
                    
                    
                }else{ // Update an existing query or save as a new query
                    
                     // set the values for update
                    // Indicator to determine if an existing query is being updated or creating a new query
                    boolean blNewQuery = false;
                    DALQuery query = new DALQuery();
                    query.setDomain("FAVOURITEQUERY", null, null, null);                   
                    query.setField("FAVOURITEQUERY_strQueryName", null);
                    query.setWhere(null, 0, "FAVOURITEQUERY_intQueryID", "=", strDisplayedQueryID, 0, DALQuery.WHERE_HAS_VALUE);
                    
                    ResultSet rs = query.executeSelect();
                    
                    // if we have an entry in the table for this query ID, check if the user has changed the name
                    // of the query indicating he would like to save it as a new query
                    if (rs.next())
                    {
                        String strQueryName = rs.getString("FAVOURITEQUERY_strQueryName");
                        // If the query name has been changed, save as a new query
                        if ( !strQueryName.equals(Utilities.cleanForXSL(runtimeData.getParameter("QueryName")) ))
                        {
                            blNewQuery = true;
                        }
                    }    
                    
                    // Close the resultset 
                    rs.close();
                    rs = null;

                    query.reset();
                    query.setDomain("FAVOURITEQUERY", null, null, null);                   
                    query.setField("FAVOURITEQUERY_strUserName", (String)authToken.getUserIdentifier());
                    query.setField("FAVOURITEQUERY_strQueryName", Utilities.cleanForXSL(runtimeData.getParameter("QueryName")));
                    query.setField("FAVOURITEQUERY_strXMLString", strXMLInserted.toString());
                    query.setField("FAVOURITEQUERY_strDomainList", strSeltdDomainList); 
                    query.setField("FAVOURITEQUERY_strDisplayAs", strRadioVal);
                    query.setField("FAVOURITEQUERY_strSaveAs", runtimeData.getParameter("saveAs"));
                    query.setField("FAVOURITEQUERY_strSmartformList", strSeltdSmartformList);    
                    query.setField("FAVOURITEQUERY_strStudyList", strSeltdStudyList);

                    
                    if ( blNewQuery == true )
                    {
                        query.setField("FAVOURITEQUERY_intDeleted", "0");
                        query.executeInsert();  

                        // QueryID
                        strQueryID = QueryChannel.getNewestKeyAsString(query);
                        strErrorMessage = "Query added successfully";

                    }
                    else 
                    {
                        query.setWhere(null, 0, "FAVOURITEQUERY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);   
                        query.setWhere("AND", 0, "FAVOURITEQUERY_intQueryID", "=", strDisplayedQueryID, 0, DALQuery.WHERE_HAS_VALUE);
                        query.executeUpdate();
                        strErrorMessage = "Query updated successfully";
                        strQueryID = strDisplayedQueryID;
                    }    
                                        
                }
                
                 // Rebuilding the page                 
                 runtimeData.setParameter("strQueryID", strQueryID);

                 strQueryName = Utilities.cleanForXSL(runtimeData.getParameter("QueryName"));
                 runtimeData.setParameter("strQueryName", strQueryName);
                 strXML = displayQueryDetails( strQueryID );  
                 

            }
        
         }catch(Exception e){  
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
         }         

    }
    
    
    private String buildQueryListXMLFile(){
    
        String strResult = "";
        try{ 
        
            // Fetch all the queries in the database
            DALQuery query = new DALQuery();
            query.setDomain("FAVOURITEQUERY", null, null, null);
            query.setField("FAVOURITEQUERY_intQueryID", null);
            query.setField("FAVOURITEQUERY_strQueryName", null);
            query.setWhere(null, 0, "FAVOURITEQUERY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);    
            
            query.setWhere("AND", 0, "FAVOURITEQUERY_strSaveAs", "=", "for all users", 0, DALQuery.WHERE_HAS_VALUE);    
            query.setWhere("OR", 1, "FAVOURITEQUERY_strSaveAs", "=", "my favourite", 0, DALQuery.WHERE_HAS_VALUE);    
            query.setWhere("AND", 0, "FAVOURITEQUERY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);                
            query.setWhere("AND", 0, "FAVOURITEQUERY_strUserName", "=", (String)authToken.getUserIdentifier(), 1, DALQuery.WHERE_HAS_VALUE);    
            
            ResultSet rs = query.executeSelect();
            
            while(rs.next()){                
                // Add it to the xml
                strResult+= "<query_list>" +
                            "<FAVOURITEQUERY_intQueryID>"+ rs.getString("FAVOURITEQUERY_intQueryID") +"</FAVOURITEQUERY_intQueryID>" +
                            "<FAVOURITEQUERY_strQueryName>"+ rs.getString("FAVOURITEQUERY_strQueryName") +"</FAVOURITEQUERY_strQueryName>" +
                            "</query_list>";
            }
            
            // Close the resultset
            rs.close();
            rs = null;
            
        }catch(Exception e){  
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
             return null;
        } 
        
        return strResult;  
    }
       
    
    /* Display the details of the query
     */
    private String displayQueryDetails(String queryID){
        String strResult = "";

        strDisplayedQueryID =    queryID;     
          
        
        try{
            
             Vector vtQueryFields = DatabaseSchema.getFormFields("isearch_favourite_query");            
            
             // Clear all the formfields
             vtJoin.clear();
             vtDomainVal.clear();
             vtField.clear();
             vtOp.clear();
             vtSearchVal.clear();
             vtFieldLabel.clear();
             vtOpenBracket.clear();
             vtCloseBracket.clear();             
             vtParticipantDomain.clear();
             vtCriteriasDomains.clear();
             hashDomainStatus.clear();
             hashVisibility.clear();
            
             // Fetch all the details of this query in the database
            DALQuery query = new DALQuery();
            query.setDomain("FAVOURITEQUERY", null, null, null);
            query.setFields(vtQueryFields, null);                        
            query.setWhere(null, 0, "FAVOURITEQUERY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);   
            query.setWhere("AND", 0, "FAVOURITEQUERY_intQueryID", "=", queryID, 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rs = query.executeSelect();
            
            
            if(rs.next()){
                
                String ID = rs.getString("FAVOURITEQUERY_intQueryID");
                String strUserName = rs.getString("FAVOURITEQUERY_strUserName");
                String strQueryName = Utilities.cleanForXSL(rs.getString("FAVOURITEQUERY_strQueryName"));
                String strXMLSaved = rs.getString("FAVOURITEQUERY_strXMLString");
                String strSeltdDomainList = rs.getString("FAVOURITEQUERY_strDomainList");
                String strDisplayAs = rs.getString("FAVOURITEQUERY_strDisplayAs");
                String strSaveAs = rs.getString("FAVOURITEQUERY_strSaveAs");
                String strSeltdSmartformList = rs.getString("FAVOURITEQUERY_strSmartformList");
                String strSeltdStudyList = rs.getString("FAVOURITEQUERY_strStudyList");
                
                // Set the stylesheet
                strDisplayMode = strDisplayAs;
                if(strDisplayAs != null){
                   if(strDisplayAs.equals("simplified")){                        
                        strStylesheet = BUILD_ISEARCH_SIMPLIFIED;
                        strCurrent = BUILD_ISEARCH;                        
                   }else if(strDisplayAs.equals("normal")){                        
                        strStylesheet = BUILD_ISEARCH_NORMAL;
                        strCurrent = BUILD_ISEARCH;                        
                   }else if(strDisplayAs.equals("advanced")){                        
                        strStylesheet = BUILD_ISEARCH;
                        strCurrent = BUILD_ISEARCH;                                             
                   }                                    
                }                   
                
                // Set the strXML 
                strResult = strXMLSaved;   
                
                vtSelectedDomains.clear();
                vtSelectedSmartforms.clear();
                vtSelectedDataelements.clear();
                vtSelectedSFDataelements.clear();
                vtSelectedStudys.clear();
                
                // Set the edit selected domain list i.e domains that were in the selected domain list for this
                // edited QueryID & smartform list
                 if(strSeltdDomainList != null && !strSeltdDomainList.equals("")){
                    
                    String[] strEditSeltdDomains = strSeltdDomainList.split(",");           
                
                    for (int i=0; i < strEditSeltdDomains.length; i++){
                    
                        // ADDED TO XML BIT IS NOT USED
                        strResult+= "<edit_seltddomains>" +
                                    "<strDomainName>" + strEditSeltdDomains[i] + "</strDomainName>" +
                                    "</edit_seltddomains>";

                        vtSelectedDomains.add(strEditSeltdDomains[i]);                            
                    }                
                 }
                
                if(strSeltdSmartformList != null && !strSeltdSmartformList.equals("")){
                    String[] strEditSeltdSmartforms = strSeltdSmartformList.split(",");   
                    
                     // Build the selected smartforms
                    for (int i=0; i < strEditSeltdSmartforms.length; i++){

                       // it is a smartform selected                       
                       vtSelectedSmartforms.add(strEditSeltdSmartforms[i]);
                    }
                }       
                
                 if(strSeltdStudyList != null && !strSeltdStudyList.equals("")){
                    String[] strEditSeltdStudys = strSeltdStudyList.split(",");   
                    
                     // Build the selected studys
                    for (int i=0; i < strEditSeltdStudys.length; i++){

                        // it is a study selected                        
                        vtSelectedStudys.add(strEditSeltdStudys[i]);
                    }
                }     
                
                
                // Build the related domains for the selected domains
                buildDomainList();                
                
                // Add the domains to the xml
                for ( int k = 0; k < vtDomainList.size(); k++){
                    strResult+= "<edit_domains>"+
                                "<strDomainName>"+ vtDomainList.get(k) +"</strDomainName>" +
                                "</edit_domains>";
                    
                }
                
                
                String strDomainVisibility = "expanded"; 
                String strVisibility1 = "expanded"; 
                String strVisibility2 = "expanded";
                String strVisibility3 = "expanded";
                
                // Build the hashDomainStatus for the domains
                for ( int h =0; h < vtSelectedDomains.size(); h++ ){                    
                    hashDomainStatus.put( (String) vtSelectedDomains.get(h), strDomainVisibility);                                  
                } 
                
                 // Build the hashDomainStatus for the smartforms
                for ( int m =0; m < vtSelectedSmartforms.size(); m++ ){              
                    hashDomainStatus.put( (String) vtSelectedSmartforms.get(m), strDomainVisibility);                                                 
                }                
                
                // Build the hashDomainStatus for the studys
                for ( int n =0; n < vtSelectedStudys.size(); n++ ){                                 
                    hashDomainStatus.put( (String) vtSelectedStudys.get(n), strDomainVisibility);                            
                }  
                
                // set the visibility status                
                hashVisibility.put("visibility1", strVisibility1);
                hashVisibility.put("visibility2", strVisibility2);
                hashVisibility.put("visibility3", strVisibility3);
                
                
                // build smartform list
                buildSmartformList();
                
                // Add the smartform names to the xml
                for ( int j=0; j < vtSmartformList.size(); j++){
                     
                    strResult+= "<edit_smartforms>"+
                                 "<strDomainName>"+ vtSmartformList.get(j) +"</strDomainName>" +
                                 "</edit_smartforms>";
                    
                }                
                 
                // build study list
                buildStudyList();
                
                // Add the study names to the xml
                for ( int p=0; p < vtStudyList.size(); p++){
                     
                    strResult+= "<edit_studys>"+
                                 "<strDomainName>"+ vtStudyList.get(p) +"</strDomainName>" +
                                 "</edit_studys>";
                    
                }                
                          
                strResult+= "<edit>true</edit>";               
                
            }   
            
            // Close the resultset
            rs.close();
            rs = null;

        }catch(Exception e){  
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
             return null;
        } 

        return strResult;  
        
    }
     
    
    /*  Build the Search criteria vectors
     */ 
    
    private void buildEditCriteriaVectors(){
     
         vtJoin.clear();
         vtDomainVal.clear();
         vtField.clear();
         vtOp.clear();
         vtSearchVal.clear();   
         vtFieldLabel.clear();
         vtOpenBracket.clear();
         vtCloseBracket.clear();
         vtParticipantDomain.clear();
        
        for(int i = 0;;i++){      
            
            if( runtimeData.getParameter("joinSel"+i) != null ){
                                
                 vtJoin.add(i, runtimeData.getParameter("joinSel"+i));
                 vtDomainVal.add(i, runtimeData.getParameter("domainVal"+i));
                 vtField.add(i, runtimeData.getParameter("fieldSel"+i));
                 vtOp.add(i, runtimeData.getParameter("opSel"+i));
                 vtSearchVal.add(i, runtimeData.getParameter("searchValue"+i));     
                 vtFieldLabel.add(i, runtimeData.getParameter("labelInColumn"+i));
                 vtOpenBracket.add(i, runtimeData.getParameter("openBracketSel"+i));
                 vtCloseBracket.add(i, runtimeData.getParameter("closeBracketSel"+i));
                 vtParticipantDomain.add(i, runtimeData.getParameter("participantDomainSel"+i));
                
            }else{
                break;
            }        
        }        
    }    
   
    
    /* Build Criteria XML file - dont think its used
     */ 
    private String buildCriteriaVectorsXMLFile(){
        String strResult = "";        
        
        for( int i=0; i < vtJoin.size(); i++){
            
             // if criteria is not for a smartform
            if( !vtSelectedSmartforms.contains(vtDomainVal.get(i).toString()) ){
            
                strResult+= "<searchCriteria>" +
                         "<openBracketSel>"+ vtOpenBracket.get(i) +"</openBracketSel>" +
                         "<joinSel>"+ vtJoin.get(i) +"</joinSel>" +
                         "<domainVal>"+ vtDomainVal.get(i) +"</domainVal>" +
                         "<fieldSel>"+ vtField.get(i) +"</fieldSel>" +
                         "<opSel>"+ vtOp.get(i) +"</opSel>" +
                         "<searchValue>"+ Utilities.cleanForXSL((String) vtSearchVal.get(i)) +"</searchValue>" +
                         "<closeBracketSel>"+ vtOpenBracket.get(i) +"</closeBracketSel>" +
                         "<participantDomainSel>"+ vtParticipantDomain.get(i)+"</participantDomainSel>" +
                         "<criteriaNo>"+ i +"</criteriaNo>" +
                         "<labelInColumn>"+ vtFieldLabel.get(i) +"</labelInColumn>" +
                         "</searchCriteria>"; 
        
             }else{// if criteria is for a smartform
                
                strResult+= "<smartformCriteria>" +
                        "<openBracketSel>"+ vtOpenBracket.get(i) +"</openBracketSel>" +
                        "<joinSel>"+ vtJoin.get(i) +"</joinSel>" +
                        "<domainVal>"+ vtDomainVal.get(i) +"</domainVal>" +
                        "<fieldSel>"+ vtField.get(i) +"</fieldSel>" +
                        "<opSel>"+ vtOp.get(i) +"</opSel>" +
                        "<searchValue>"+ Utilities.cleanForXSL((String) vtSearchVal.get(i)) +"</searchValue>" +
                        "<closeBracketSel>"+ vtCloseBracket.get(i) +"</closeBracketSel>" +
                        "<participantDomainSel>"+ vtParticipantDomain.get(i)+"</participantDomainSel>" +
                        "<criteriaNo>"+ i +"</criteriaNo>" +
                        "<labelInColumn>"+ vtFieldLabel.get(i) +"</labelInColumn>" +
                        "</smartformCriteria>";  
                
            }
        }       
        
        return strResult;  
        
    }
    
    
    /* Build the status of each domain if it is expanded or collapsed
     * on edit & submit action( obtained from xml through hidden field)
     *
    private void buildEditDomainStatus(){
        
        hashDomainStatus.clear();
        
        for(int i=0; i < vtSelectedDomains.size(); i++){
            if(runtimeData.getParameter("status"+(String)vtSelectedDomains.get(i)) != null){
                
                hashDomainStatus.put( (String)vtSelectedDomains.get(i), runtimeData.getParameter("status"+(String)vtSelectedDomains.get(i)));
            }
                
        }
         *
         
    } 
     */    

    
    
    /* for debugging purpose
     *
     */
    private void checkValues(){
        
        
        // vtDomainList         
        //System.err.println("===================DOMAIN LIST START==========================");        
            for(int i=0; i < vtDomainList.size(); i++){
               //System.err.println("field"+i+":"+vtDomainList.get(i));
            } 
        //System.err.println("===================DOMAIN LIST END==========================");
        
        // vtSelectedDomains         
        //System.err.println("===================SELECTED DOMAIN LIST START==========================");        
            for(int i=0; i < vtSelectedDomains.size(); i++){
               //System.err.println("field"+i+":"+vtSelectedDomains.get(i));
            } 
        //System.err.println("===================SELECTED DOMAIN LIST END==========================");
        
        // hashdomainstatus
         // for debugging purpose
        Enumeration e1 = hashDomainStatus.keys();
        //System.err.println("=================DOMAIN STATUS START(hashDomainStatus)============================");
        while(e1.hasMoreElements()){            
            String strCriteriaDomain = (String)e1.nextElement();            
            String value = (String)hashDomainStatus.get(strCriteriaDomain);              
            //System.err.println("domain:"+strCriteriaDomain +"status:"+value );            
        } 
        //System.err.println("===================DOMAIN STATUS END==========================");
        
        
        //hashCriteriaDomains         
        Enumeration e2 = hashCriteriaDomains.keys();
        //System.err.println("===================DOMAIN & ITS FIELDS SELECTED START(hashCriteriaDomains)==========================");
        while(e2.hasMoreElements()){
            String strCriteriaDomain = (String)e2.nextElement();
            //System.err.println("domain:"+strCriteriaDomain);
            Vector vtFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);            
            
            for(int i=0; i < vtFields.size(); i++){
                //System.err.println("field"+i+":"+vtFields.get(i));
            } 
        } 
        //System.err.println("===================DOMAIN & ITS FIELDS SELECTED END==========================");
               
        
         // vtSearchFormFields    
        //System.err.println("===================SEARCH FORM FIELD START==========================");        
            for(int i=0; i < vtSearchFormFields.size(); i++){
              //System.err.println("field"+i+":"+vtSearchFormFields.get(i));
            } 
        //System.err.println("===================SEARCH FORM FIELD END==========================");          
        
    }
    
    
    /*  Delete the Query
     */  
    private void deleteQuery(String queryID){     
     
    try{
            
             if( queryID != null && !queryID.equals("")){
                DALQuery query = new DALQuery();
                query.setDomain("FAVOURITEQUERY", null, null, null);                
                query.setField("FAVOURITEQUERY_intDeleted", "1");
                query.setWhere(null, 0, "FAVOURITEQUERY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);   
                query.setWhere("AND", 0, "FAVOURITEQUERY_intQueryID", "=", queryID, 0, DALQuery.WHERE_HAS_VALUE);
                query.executeUpdate();
                
                strErrorMessage = "Query deleted successfully";                
             }
            
        }catch(Exception e){  
         LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
         
        }     
            
    }
    
    /* Build new search page
     * so clear all values
     */ 
    private String buildNewSearch(){
        
        String strResult = "";
        
        // Clear vectors and hashtables
        vtDomainList.clear();
        vtSelectedDomains.clear();
	vtSearchFormFields.clear();
        vtJoin.clear();
        vtDomainVal.clear();
        vtField.clear();
        vtOp.clear();
        vtSearchVal.clear();
        vtFieldLabel.clear();
        vtOpenBracket.clear();
        vtCloseBracket.clear();
        vtParticipantDomain.clear();
        vtCriteriasDomains.clear();
        hashCriteriaDomains.clear();
        hashDomainStatus.clear();
        vtSmartformList.clear();
        vtSelectedSmartforms.clear();
        vtStudyList.clear();
        vtSelectedStudys.clear();
        
        vtSelectedDataelements.clear();
        vtSelectedSFDataelements.clear();        
        hashStartRecordForPage.clear();     
        hashVisibility.clear();
        hashPrimaryKey.clear();
        vtMissingFor.clear();
        vtMissingWithout.clear();
        vtMissingSF.clear();
        vtMissingSFDomain.clear();
        vtMissingSFField.clear();
        
        blSubQuery = false;
        blFirstTimeDoSearch = true;
        blPartBioAnalysis = false;
        strBioSF = "";
        
        //paging
        intStartRecord = 0;
        intRecordPerPage = Integer.parseInt(PropertiesManager.getProperty("neuragenix.bio.RecordPerPage"));
        intMaxPage = 0;
        intCurrentPage = 1;
        intRecordSetSize = 0;
        intHiddenStartRecord = 0;
        intHiddenRecordPerPage = 0;
        intHiddenMaxPage = 0;
        intHiddenCurrentPage = 1;       
        intFirstDataRow = 0;
        intLastDataRow = 0;
        intRequiredStartRow = 0;
        intRequiredEndRow = CACHE_PAGE_SIZE * intRecordPerPage;
        intMaxCachePage = CACHE_PAGE_SIZE;
        intMinCachePage = 0;        
       
        // Variables 
        strDomainSel = "";     
        strFieldSel = "";
        strSelectedDomainSel = "";
        strRadioVal = "";
        strQueryID = "";
        strQueryName = "";
        strMissingForSel = "";
        strMissingWithoutSel = "";
        strMissingSFDomainSel = "";
        strMissingSFFieldSel = "";
       
        strBioSF = "";
        blPartBio = false;
         
        //runtimedata        
        runtimeData.setParameter("strDomainSel", "");
        runtimeData.setParameter("strFieldSel", "");
        runtimeData.setParameter("strSelectedDomainSel", "");
        runtimeData.setParameter("SelectedDomainSel", (String)null);
        runtimeData.setParameter("domainSel", (String)null);
        runtimeData.setParameter("fieldSel", (String)null);  
        runtimeData.setParameter("strRadioVal", "");
        runtimeData.setParameter("strQueryID", "");
        runtimeData.setParameter("strQueryName", ""); 
        runtimeData.setParameter("QueryName", ""); 
        runtimeData.setParameter("selFieldType", ""); 
        runtimeData.setParameter("number", ""); 
        runtimeData.setParameter("domains", ""); 
        runtimeData.setParameter("field", ""); 
        runtimeData.setParameter("selected_domains", ""); 
        runtimeData.setParameter("rdDisplayAs", "");  
        runtimeData.setParameter("labelInColumn", "");          
        runtimeData.setParameter("strMissingForSel", "") ;
        runtimeData.setParameter("strMissingWithoutSel", "");
        runtimeData.setParameter("strMissingSFDomainSel", "");
        runtimeData.setParameter("strMissingSFFieldSel", "");

        
        // Set the display mode
        strDisplayMode = "advanced";
        strStylesheet = BUILD_ISEARCH;
        
        
        strResult = buildISearch();
        
        return strResult;
                
    } 
    
   
    /*  Build the smartforms questions
     */
    private ResultSet buildSmartformsDataelements(String domainName){
        
        String strResult = "";
        try{
            
            Vector vtFormFields = DatabaseSchema.getFormFields("isearch_smartforms_dataelements");
            
            DALQuery queryJoin = new DALQuery();    
            queryJoin.setDomain("SMARTFORM", null, null, null);
            queryJoin.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN"); 
            queryJoin.setDomain("DATAELEMENTS","SMARTFORMTODATAELEMENTS_intDataElementID","DATAELEMENTS_intDataElementID","INNER JOIN");
            queryJoin.setFields(vtFormFields, null);    
            queryJoin.setWhere(null, 0, "SMARTFORM_strSmartformName", "=", domainName, 0, DALQuery.WHERE_HAS_VALUE);  
            queryJoin.setWhere("AND", 0, "DATAELEMENTS_intDataElementType", "<>", "TITLE", 0, DALQuery.WHERE_HAS_VALUE);
            queryJoin.setWhere("AND", 0, "DATAELEMENTS_intDataElementType", "<>", "COMMENT", 0, DALQuery.WHERE_HAS_VALUE);
            queryJoin.setWhere("AND", 0, "DATAELEMENTS_intDataElementType", "<>", "PAGEBREAK", 0, DALQuery.WHERE_HAS_VALUE);
            queryJoin.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            queryJoin.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            queryJoin.setWhere("AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            queryJoin.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");            
            ResultSet rs = queryJoin.executeSelect();
            
            return queryJoin.executeSelect();          
            
        }catch(Exception e){  
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
             return null;
        } 
       
    }
    
    
    /** Set paging
     */
    private void pagingCalculation()
    {

        if ( ((intRecordSetSize % intRecordPerPage) == 0) )
            intMaxPage = intRecordSetSize/intRecordPerPage;
        else
            intMaxPage = intRecordSetSize/intRecordPerPage + 1;
        
        intStartRecord = intRecordPerPage * (intCurrentPage - 1);
        
    }
    
    /** Change the number of record for a page
     */
    private void changeRecordPerPage()
    {
        try
        {
            int intNewRecordPerPage = Integer.parseInt(runtimeData.getParameter("intRecordPerPage"));
            
            if (intNewRecordPerPage > 0)
            {
                intRecordPerPage = intNewRecordPerPage;
                intCurrentPage = intStartRecord / intRecordPerPage + 1;
            }
            
            getRequiredRowsPageSet ();
            
        }
        catch (Exception e) {}
    }
    
    /** Change the current page
     */
    private void changeCurrentPage()
    {
        try
        {
            int intNewCurrentPage = Integer.parseInt(runtimeData.getParameter("intCurrentPage"));
    
            if (intNewCurrentPage > 0 && intNewCurrentPage <= intMaxPage)
                intCurrentPage = intNewCurrentPage;
            
            getRequiredRows ();
            
        }
        catch (Exception e) {}
    }
    
    /** Go to next page
     */
    private void goNextPage()
    {       
        if (intCurrentPage < intMaxPage)
            intCurrentPage++;

        getRequiredRows ();
        
    }
    
    /** Go to previous page
     */
    private void goPreviousPage()
    {      
        if (intCurrentPage > 1)
            intCurrentPage--;

        getRequiredRows ();

    }
    
    
     /** Create export file For Smartforms
     */
    private void createExportFileForSmartforms()
    {
        // Holds the number when dataelement name starts
        intSmartformColumn = vtSearchFormFields.size();
        
        try
        {      
            strDelimiter = runtimeData.getParameter("delimiter");
            
            // Make tab the default delimiter if none is specified
            if ( ( strDelimiter == null) || (strDelimiter.equals("tab")) )

            {
                int i=9;
                char c = (char) i;
                // The delimiter is the ascii equivalent of tab
                strDelimiter = String.valueOf(c);
            }    
            
            strFileName = "ABN_QueryResult_1.csv";
            ExportFile = new BufferedWriter(new FileWriter(new File(EXPORT_DIRECTORY + "/" + strFileName)));
            
            String column = "";             
            
            // Build the headers for all the selected fields except smartform's dataelements
            for( int i=1; i < vtSearchFormFields.size(); i++ ){                    

                
                if( !hashNonDisplayFields.contains(vtSearchFormFields.get(i).toString()) ){

                    DBField field = (DBField) DatabaseSchema.getFields().get(vtSearchFormFields.get(i).toString()); 
                    String strFormFieldName = field.getExternalName();

                    // If the formfield is not a question, build the header
                    // Basically build all the formfields as headers until the formfield is a dataelement name                           
                    if( !strFormFieldName.equals("ix_dataelements.\"DATAELEMENT_NAME\"")){         

                        String data = Utilities.cleanForExcelFile(field.getLabelInColumn());                    
                        column += data + strDelimiter;
                    }else{
                        intSmartformColumn = i-1;                          
                        break;                        
                    }

                }// End of if
            }

            vtExportDataElements.clear();

            DALQuery query = new DALQuery();

            // Build the smartform dataelement headers
            for( int i=0; i < vtSelectedDataelements.size(); i++ ){                    

                query.reset();                    
                query.setDomain("DATAELEMENTS", null, null, null);
                query.setField("DATAELEMENTS_strDataElementName", null);                
                query.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=", (String) vtSelectedDataelements.get(i), 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsResult = query.executeSelect();

                while( rsResult.next() ){

                String strDataElementID = (String) vtSelectedSFDataelements.get(i);
                String strDataElementName = rsResult.getString("DATAELEMENTS_strDataElementName"); 

                    if( !vtExportDataElements.contains( strDataElementID )){

                        vtExportDataElements.add( strDataElementID );                        
                        String data = Utilities.cleanForExcelFile(strDataElementName);   
                        
                        // If the explicit fields check box has been checked
                        // Determine the smartform name to which the dataelement belongs
                        // Prepend the smartform name to the dataelement name
                        if( blExplicitFields == true ){
                            
                            String strSFDataelementData[] = strDataElementID.split("__");
                            String strSFName = "";
                            // Get the smartform name
                            strSFName = strSFDataelementData[0];
                        
                            // If the smartform name has been obtained for the dataelement, prepend it to the header
                            if (!strSFName.equals("")){
                                data = strSFName + "." + data;
                            }
                        } 

                        column += data + strDelimiter;
                    }
                }
                
                // Close the resultset
                rsResult.close();
                rsResult = null;

            }
            ExportFile.write(column, 0, column.length() - 1);
            ExportFile.write("\n");


            ResultSet rs = executeSearchQueryForSmartform();
            
            if (rs != null)
            {
                if(vtSelectedDomains.contains("BIOANALYSIS") || vtSelectedDomains.contains("ADMISSIONS")){
                    
                    boolean blSFResultsSepLine = false;
                    if ((vtSelectedDomains.contains("BIOANALYSIS") && PROPERTY_BIOANALYSISSF_SEP_LINE == true) || (vtSelectedDomains.contains("ADMISSIONS") && PROPERTY_COLLECTIONSF_SEP_LINE == true))
                    {
                        blSFResultsSepLine = true;
                    }
                                        
                    lookUpRecords(rs, vtSearchFormFields, blSFResultsSepLine, intFirstDataRow, intLastDataRow, EXPORT_SMARTFORM );
                }
                else
                {
                    lookUpRecords(rs, vtSearchFormFields, false, intFirstDataRow, intLastDataRow, EXPORT_SMARTFORM );
                }    
            }

            vtExportDataElements.clear();
            
            // close the output stream
            ExportFile.close();       

            // Close the resultset
            rs.close();
            rs = null;
            
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Advanced Search Channel - " + e.toString(), e);
        }
    }
    
    
    /** Create export file
     */
    private void createExportFile()
    {
        try
        {
            strDelimiter = runtimeData.getParameter("delimiter");
            
            strDelimiter = runtimeData.getParameter("delimiter");
            
            if ( (strDelimiter == null) || (strDelimiter.equals("tab")) )
            {
                int i=9;
                char c = (char) i;
                // The delimiter is the ascii equivalent of tab
                strDelimiter = String.valueOf(c);
            }
            
            strFileName = "ABN_QueryResult_1.csv";
            ExportFile = new BufferedWriter(new FileWriter(new File(EXPORT_DIRECTORY + "/" + strFileName)));
            String column = "";  
            
            for (int j=0; j < vtSearchFormFields.size(); j++)
            {          
                if( !hashNonDisplayFields.contains(vtSearchFormFields.get(j).toString()) ){
                             
                    DBField field = (DBField) DatabaseSchema.getFields().get(vtSearchFormFields.get(j));
                    String data = Utilities.cleanForExcelFile(field.getLabelInColumn());                    
                    if (field.getDataType() == 4)
			column += "\"" +  data + "\"" + strDelimiter;
		    else
			column += data + strDelimiter;
                }
            }
            ExportFile.write(column, 0, column.length() - 1);
            ExportFile.write("\n");
                

            ResultSet rs = null;
             
            // if Criteria is given for searching missing records
            if(vtMissingFor.isEmpty() == false){
                rs = executeSearchQueryForMissingRecords();
            }
            else if (vtMissingSFDomain.isEmpty() == false){

                rs = executeSearchQueryForMissingSFResults ();               
            }    
            else{
                rs = executeSearchQuery();
            }                 

            
            if (rs != null)
            {    
                lookUpRecords(rs, vtSearchFormFields, false, intFirstDataRow, intLastDataRow, EXPORT );                                
            }
            // close the output stream
            ExportFile.close();
            
            // Close the resultset
            rs.close();
            rs = null;
 
        }
        catch(Exception e)
        {
            //ExportFile.close();
            LogService.instance().log(LogService.ERROR, "Unknown error in Advanced Search Channel - " + e.toString(), e);
        }
    }
        
    /*
     *
     * export file     
    private void createFile(ResultSet rs){
        
        try{
        String filename = "ABN_QueryResult_1.csv";
        
        BufferedWriter exportfile = new BufferedWriter( new FileWriter( new File( EXPORT_DIRECTORY + "/" + filename))); 
        
        while(rs.next()){
             String data = "Anita";
             data = data +"======B";
             exportfile.write(data, 0, data.length() - 1);
             exportfile.close();
             
        }
        }catch(Exception e){  
           LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
        }        
        
    }
     */
    
    
    /*
     * Execute the query, create the export file and build the xml
     */
    
    private String performExport(){
        
    String strResult = "";
    try{
        
        blPerformExport = true;
        blFirstTimeDoSearch = true;

        // Keep this order of precedence for building the search
        // if Criteria is given for searching missing records
        if( vtMissingFor.isEmpty() == false || vtMissingSFDomain.isEmpty() == false){
            createExportFile();
            blPerformExport = false;
            strResult+= buildSearchResultXMLForSchemaDomains();
        }
        //if smartforms are selected 
        else if(vtSelectedSmartforms.isEmpty() == false ){
            createExportFileForSmartforms();
            blPerformExport = false;
            strResult+= buildSearchResultXMLForSmartform();

        }else{
            createExportFile();
            blPerformExport = false;
            strResult+= buildSearchResultXMLForSchemaDomains();
        }                 
        
        strCurrent = ISEARCH_RESULT;                        
        strStylesheet = ISEARCH_RESULT;  
      
        return strResult;
        
      }catch(Exception e){  
           LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
           return null;
      } 
        
    }
    
    /*
     * Authenticate the User to view the Query
     
    private boolean authenticateUser(String queryID){
        
        boolean blVar = true;
        
        Vector vtQueryFields = DatabaseSchema.getFormFields("isearch_favourite_query");
        
        DALQuery query = new DALQuery();
        query.setDomain("FAVOURITEQUERY", null, null, null);
        query.setFields(vtQueryFields, null);                    
        query.setWhere(null, 0, "FAVOURITEQUERY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);   
        query.setWhere("AND", 0, "FAVOURITEQUERY_intQueryID", "=", queryID, 0, DALQuery.WHERE_HAS_VALUE);
        ResultSet rs = query.executeSelect();
        
        String saveAs = "";
        String userName = "";
        while(rs.next()){
            saveAs = rs.getString("FAVOURITEQUERY_strSaveAs");
            userName = rs.getString("FAVOURITEQUERY_strUserName");     
        }
        
        // if query is saved as my favourite
        if(saveAs.equals("my favourite")){
            blVar = false;
            // check if login name is same as username who saved query
            if( (String)authToken.getUserIdentifier().equals(userName) ){
                blVar = true;                
            }    
        } 
        
        return blVar;
        
    }
     */
    
    
    /* 
     * Check for the users rights to view the domain selected
     */
    private boolean authorizeUser(){
        
        boolean blVar = true;
        
        try{        
            
            for( int i =0; i < vtSelectedDomains.size(); i++){
                String permission = DatabaseSchema.getActivityRequired(vtSelectedDomains.get(i).toString());
                
                if( permission == null){                    
                }else if( !authToken.hasActivity(permission) ){                                   
                    blVar = false;      
                    break;
                }
            }

            if(blVar == true){
                for( int j =0; j < vtSelectedSmartforms.size(); j++){
                    String permission = DatabaseSchema.getActivityRequired(vtSelectedSmartforms.get(j).toString());
                    
                    if( permission == null ){
                    }else if (!authToken.hasActivity(permission) ){                                       
                        blVar = false;
                        break;
                    }
                }
            }         
        
          }catch(Exception e){  
               LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
               //return false;
          } 
         return blVar;
        
    }
    
    
    /* Build all the lists and build the xml
     */
    private String buildXML(){
        
        String strResult = "";
        // Display the ISearch form with the domains loaded  
        // And load the selected domains                              
        strResult+= buildDomainListXMLFile()+ buildSelectedDomainsXMLFile() + buildCriteriaListXMLFile() + buildCriteriasDomainListXMLFile() + buildDomainStatusXMLFile() + buildSelectedFieldsXMLFile() + buildSmartformListXMLFile() + buildSelectedSmartformsXMLFile() + buildStudyListXMLFile() + buildSelectedStudysXMLFile() + buildCriteriasForMissingRecords() + buildCriteriasForMissingSFRecords() + buildVisibilityXMLFile();                          
        
        return strResult;
    }
    
    
    /*
     * Load all the studys related to the Patient i.e of domain Study
     *
    private void LoadPatientsStudy(){
        
    try{
    
        vtStudyList.clear();
        
        // display all the study names
        Vector vtQueryFields = DatabaseSchema.getFormFields("participants_studys");

        DALQuery query = new DALQuery();
        query.setDomain("PATIENT_SMARTFORM", null, null, null);
        query.setDomain("ISEARCH_STUDY", "PATIENT_SMARTFORM_intStudyID", "ISEARCH_STUDY_intStudyID", "INNER JOIN");
        
        query.setFields(vtQueryFields, null);
        query.setWhere(null, 0, "ISEARCH_STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);              
        query.setWhere("AND", 0, "PATIENT_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);              
        
        ResultSet rs = query.executeSelect();

        while(rs.next()){  
            //System.err.println("ISEARCH_STUDY_intStudyID:"+rs.getString("ISEARCH_STUDY_intStudyID"));
            
            String studyName = rs.getString("ISEARCH_STUDY_strStudyName");    
            if(!isSelectedStudy(studyName)){
                vtStudyList.add(studyName);     
            }
        }
        
        
        }catch(Exception e){  
           LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
           //return false;
        }   
        
        
    }
     */
    
    
    /*
     * Load all the smartforms related to the study
     */
    private void LoadStudysSmartforms(){        
        try{       
            
            String studyName ="";
            for(int i=0; i < vtSelectedStudys.size(); i++){
                
                studyName = (String)vtSelectedStudys.get(i);
                DALQuery query = new DALQuery();
                query.setDomain("STUDY", null, null, null);
                query.setDomain("STUDYSURVEY", "STUDY_intStudyID", "STUDYSURVEY_intStudyID", "INNER JOIN");
                query.setDomain("SMARTFORM", "STUDYSURVEY_intSurveyKey", "SMARTFORM_intSmartformID", "INNER JOIN");
                query.setField("SMARTFORM_strSmartformName", null);
                query.setWhere(null, 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);               
                query.setWhere("AND", 0, "STUDYSURVEY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);               
                query.setWhere("AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);               
                query.setWhere("AND", 0, "STUDY_strStudyName", "=", studyName, 0, DALQuery.WHERE_HAS_VALUE);               

                query.setDistinctField("STUDYSURVEY_intStudyID");
                ResultSet rs = query.executeSelect();

                while(rs.next()){
                    String sfName = rs.getString("SMARTFORM_strSmartformName");
                    if(!isSelectedSmartform(sfName) && !vtSmartformList.contains(sfName)){                        
                        vtSmartformList.add(sfName);      
                    }
                }           

                // Close the result set
                rs.close();
                rs = null;

            }
            
        }catch(Exception e){  
               LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);               
        }    
            
    }
    
    /*
     * Load all the smartforms related to the study
     */
    private void LoadAdmissionsSmartforms()
    {        
        try
        {       
           DALQuery query = new DALQuery ();
           query.setDomain("SMARTFORM", null, null, null);  
           query.setField ("SMARTFORM_strSmartformName", null);
           query.setWhere( null, 0, "SMARTFORM_strDomain", "=", "Admissions", 0, DALQuery.WHERE_HAS_VALUE );
           query.setWhere( "AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
           ResultSet rs = query.executeSelect ();

            while(rs.next())
            {
                String sfName = rs.getString("SMARTFORM_strSmartformName");
                if(!isSelectedSmartform(sfName) && !vtSmartformList.contains(sfName)){                        
                    vtSmartformList.add(sfName);      
                }
            }    

            // Close the result set
            rs.close();
            rs = null;
            
        }
        catch(Exception e)
        {  
            LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);               
        }                
    }

    
    /*
     * Load all the smartforms related to the biospecimen
     *
     *
    private void LoadBiospecimensSmartforms(){
        
        try{            
            
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
            query.setDomain("SMARTFORM", "SMARTFORM_intSmartformID", "SMARTFORMPARTICIPANTS_intSmartformID", "INNER JOIN");
            //query.setFields(vtQueryFields, null);
            query.setWhere(null, 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);               
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);               
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Biospecimen", 0, DALQuery.WHERE_HAS_VALUE);               
            query.setDistinctField("SMARTFORMPARTICIPANTS_intSmartformID");
            ResultSet rs = query.executeSelect();

            while(rs.next()){         
                //System.err.println("bios smartforms:"+rs.getString("SMARTFORM_intSmartformID"));
                String smartformName = rs.getString("SMARTFORM_strSmartformName");    
                if(!isSelectedSmartform(smartformName)){
                    vtSmartformList.add(smartformName);     
                }
            }
            
       
        }catch(Exception e){  
               LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
               //return false;
        }    
        
    }
     */
    
    
    /*
     * to check if the selected domain belongs to study
     *
     */
    private boolean isStudy(String strSelectedDomain){
        boolean blVar = false;
        
        try{
            
            // check if the domain selected is a study name
            Vector vtQueryFields = DatabaseSchema.getFormFields("isearch_study_details");

            DALQuery query = new DALQuery();
            query.setDomain("STUDY", null, null, null);
            query.setFields(vtQueryFields, null);
            query.setWhere(null, 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);    
            query.setWhere("AND", 0, "STUDY_strStudyName", "=", strSelectedDomain, 0, DALQuery.WHERE_HAS_VALUE);    
            ResultSet rs = query.executeSelect();

            if(rs.next()){                
                blVar = true;            
            }  

            // Close the result set
            rs.close();
            rs = null;
            
        }catch(Exception e){  
               LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);               
        }  
        
        return blVar;
    }
    
    /*
     * to check if the selected domain belongs to study
     *
     */
    private boolean isSmartform(String strSelectedDomain){
        boolean blVar = false;
        
        try{
            
            // check if the domain selected is a study name            
            Vector vtQueryFields = DatabaseSchema.getFormFields("isearch_view_smartform");

            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORM", null, null, null);
            query.setFields(vtQueryFields, null);
            query.setWhere(null, 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);   
            query.setWhere("AND", 0, "SMARTFORM_strSmartformName", "=", strSelectedDomain, 0, DALQuery.WHERE_HAS_VALUE);   
            ResultSet rs = query.executeSelect();

            if(rs.next()){                
                blVar = true;            
            }  

            // Close the result set
            rs.close();
            rs = null;
            
        }catch(Exception e){  
               LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);               
        }  
        
        return blVar;
    }
    
    
    /* if a study is removed, remove the smartforms related to the study
     */
    private void removeStudysSmartforms(){
        
        try{ 
            
            DALQuery query = new DALQuery();
            query.setDomain("SMARTFORMPARTICIPANTS", null, null, null);
            query.setDomain("SMARTFORM", "SMARTFORM_intSmartformID", "SMARTFORMPARTICIPANTS_intSmartformID", "INNER JOIN");
            query.setDomain("STUDY", "STUDY_", "SMARTFORMPARTICIPANTS_intSmartformID", "INNER JOIN");            
            query.setWhere(null, 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);               
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);               
            query.setWhere("AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Study", 0, DALQuery.WHERE_HAS_VALUE);               
            query.setDistinctField("SMARTFORMPARTICIPANTS_intSmartformID");
            query.executeSelect();
            
            
        }catch(Exception e){  
               LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);               
        } 
        
    }
    
    /* Cloning the main query executed in 
     *
     * executeSearchQueryForSmartform()
     */
    
    private DALQuery setQueryObject(){
        
    DALQuery query1 = new DALQuery();
    try{
       
       String strSetSF = "";
       blSubQuery = false;
       Vector vtSelectedDataelements = new Vector(10,2);
      
       // Check if smartforms are selected
       if(vtSelectedSmartforms.size() > 0){         
           
           // if PATIENT & BIO & SF is in the selected domain
           if( (vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN")) || (vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS"))){
               
               if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){                    
                    query1.setDomain("PATIENT", null, null, null);
                    query1.setDomain("BIOSPECIMEN", "BIOSPECIMEN_intPatientID", "PATIENT_intInternalPatientID", "INNER JOIN");               
               }
               
               // if it contains BIO and BIOANALYSIS, not PAT
               else if (vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS")){
                    query1.setDomain("BIOSPECIMEN", null, null, null);                    
               } 
               
               // if Participant domain has  PAT & BIO or BIO & ANALYSIS/ PATIENT / BIO/ ANALYSIS / No PAT & BIO
               if ( vtParticipantDomain.contains("PATIENT") && vtParticipantDomain.contains("BIOSPECIMEN") || (vtParticipantDomain.contains("PATIENT") && vtParticipantDomain.contains("BIOANALYSIS")) || (vtParticipantDomain.contains("BIOSPECIMEN") && vtParticipantDomain.contains("BIOANALYSIS"))){                  

                   // if the participantDomain is not BIOSPECIMEN & not BIOANLAYSIS - then PAT - PATSF - SF & PAT - BIO ===> IF PAT is selected
                   if ( (blPartBio == false && blPartBioAnalysis == false) && vtSelectedDomains.contains("PATIENT") ){

                        query1.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
                        query1.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
                        query1.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                        
                        strSetSF = "pat-sf";

                   }
                   else if ( (blPartBio == false && blPartBioAnalysis == false) && vtSelectedDomains.contains("BIOSPECIMEN") ){

                        query1.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
                        query1.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_SMARTFORM_intStudyID", "INNER JOIN");
                        query1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                        strSetSF = "bio-sf";
                   }
                   // if the participantDomain is BIOSPECIMEN then - PAT - BIO - BIOSF - SF
                   else if( blPartBio == true ){   
                       
                        query1.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
                        query1.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_SMARTFORM_intStudyID", "INNER JOIN");
                        query1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                        strSetSF = "bio-sf";
                   }
                   // if it contains criteria for only BIOANALYSIS
                   else if ( blPartBioAnalysis == true ){                      
                       
                        query1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_intBiospecimenID", "SMARTFORMPARTICIPANTS_intParticipantID","INNER JOIN");
                        query1.setDomain( "STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN");               
                        
                        strSetSF = "";
                   } 
                   
                }else if( vtParticipantDomain.contains("PATIENT") ){                       

                   query1.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
                   query1.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
                   query1.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                   strSetSF = "pat-sf";
                   
                }else if ( vtParticipantDomain.contains("BIOSPECIMEN") ){                       

                   query1.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
                   query1.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_SMARTFORM_intStudyID", "INNER JOIN");
                   query1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                   strSetSF = "bio-sf";
                } 
                // if it contains criteria for only BIOANALYSIS
                else if ( vtParticipantDomain.contains("BIOANALYSIS") ){
                    
                   //query1.setDomain("BIOANALYSIS", "BIOANALYSIS_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
                   query1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_intBiospecimenID", "SMARTFORMPARTICIPANTS_intParticipantID","INNER JOIN");
                   query1.setDomain( "STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN");                        
                   
                   strSetSF = "";
                } 
                else if ( vtParticipantDomain.contains("blank") || vtParticipantDomain.isEmpty() == true ){// if participant domain is not defined

                   query1.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
                   query1.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
                   query1.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                   strSetSF = "pat-sf";
                }                     
               
           }   
                     
           // if PAT & SF is selected & BIO is not selected
           else if ( vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") ){
               query1.setDomain("PATIENT", null, null, null);              
                 
               query1.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
               query1.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
               query1.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                   
               strSetSF = "pat-sf";
           }           
           // if BIO & SF is selected and PAT & Study is not selected
           else if( !vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") && !vtSelectedDomains.contains("BIOANALYSIS")){
           
               query1.setDomain("BIOSPECIMEN", null, null, null);             
               query1.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
               query1.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_SMARTFORM_intStudyID", "INNER JOIN");
               query1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                       
               strSetSF = "bio-sf";
           } 
           // if it contains BIO & BIOANALYSIS & not PAT / Study
           else if ( !vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS") ){
                
               query1.setDomain("BIOSPECIMEN", null, null, null); 
               //query1.setDomain("BIOANALYSIS", "BIOANALYSIS_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
               query1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_intBiospecimenID", "SMARTFORMPARTICIPANTS_intParticipantID","INNER JOIN");
               query1.setDomain( "STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN");
               
               strSetSF = "";
           }           
           // if study & SF is selected and no PAT & BIO is selected
           else if( !vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedStudys.isEmpty() == false){
            
               query1.setDomain("STUDY", null, null, null);
               query1.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intStudyID", "STUDY_intStudyID","INNER JOIN");
               query1.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                   
               strSetSF = "pat-sf";
           }   
           query1.setDomain("SMARTFORM", "SMARTFORM_intSmartformID", "SMARTFORMPARTICIPANTS_intSmartformID", "INNER JOIN");           
           query1.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN"); 
           query1.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID","INNER JOIN");           
           query1.setDomain("SMARTFORMRESULTS", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "SMARTFORMRESULTS_intSmartformParticipantID", "INNER JOIN");
          
           // Set the other domains and the join between them           
           for (int i=0; i < vtSelectedDomains.size(); i++){

               // For each domain, get its join object
               String strDomain = (String)vtSelectedDomains.get(i);

               if(!strDomain.equals("PATIENT") && !strDomain.equals("BIOSPECIMEN") && !strDomain.equals("BIOANALYSIS")){

                   Hashtable hashJoins = (Hashtable)DatabaseSchema.getSearchDomains().get(strDomain);
                  
                   // if the first domain is CONSENT
                   if( i == 0 && strDomain.equals("CONSENT")){
                       query1.setDomain( "CONSENT", "PATIENT_intInternalPatientID", "CONSENT_intInternalPatientID", "INNER JOIN");
                       
                   }                   

                   // if the first domain is STUDY
                   else if( i == 0 && strDomain.equals("STUDY")){
                       query1.setDomain( "STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN");
                       
                   }
                                      
                   // Get all possible joins for this strDomain, with all the domains in vtSelectedDomains
                   for( int j=0; j < i; j++ ){

                       // Check to see if there is a join between strDomain and vtSelectedDomains(j)
                       if(hashJoins.containsKey((String)vtSelectedDomains.get(j))){ 
                           // Get the join object, which consists of join between strDomain and vtSelectedDomains.get(j)
                            DBJoin joinObj = (DBJoin)hashJoins.get(vtSelectedDomains.get(j));
                            if(strDomain.equals("CONSENT")){
                                query1.setDomain( "CONSENT", "PATIENT_intInternalPatientID", "CONSENT_intInternalPatientID", "INNER JOIN");
                            }else{
                                query1.setDomain( strDomain, joinObj.getFirstField(), joinObj.getSecondField(), joinObj.getType());
                            }
                            break;
                       } 
                   }

               }
           }  
           
           // if first domain is CONSENTSTUDY, set it after consent & other domains have been set
           if(vtSelectedDomains.isEmpty() == false && vtSelectedDomains.get(0).toString().equals("CONSENTSTUDY")){
               query1.setDomain( "CONSENTSTUDY", "CONSENT_intConsentKey", "CONSENTSTUDY_intConsentKey", "INNER JOIN");
           }
           
           // Set the formfields for DISTINCT conditions           
           String strDistinct = "";           
          
           // if PAT & BIO is selected
           if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
                //strDistinct = "PATIENT_intInternalPatientID";
               strDistinct = "BIOSPECIMEN_intBiospecimenID";
           }
           // if only PAT is selected
           else if( vtSelectedDomains.contains("PATIENT")){
               strDistinct = "PATIENT_intInternalPatientID";
           }
           // if only BIO is selected
           else if( vtSelectedDomains.contains("BIOSPECIMEN") ){
               strDistinct = "BIOSPECIMEN_intBiospecimenID";
           }
           // if only study is selected, and no PAT/ BIO
           else if( vtSelectedStudys.isEmpty() == false){
               strDistinct = "STUDY_intStudyID";
           }
           vtSearchFormFields.clear();
           vtSearchFormFields.add(strDistinct);
           
           // Add BIO KEY to formfields only if PAT & BIO is selected, but it is hidden
           if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
                vtSearchFormFields.add("PATIENT_intInternalPatientID");
           }
           
           
           // Build the form fields from all the fields selected in criteriadomains - only schema domains
           // And also the dataelements selected for the smartforms
           // vtSearchFormFields.clear();
           vtSelectedDataelements.clear();
           vtSelectedSFDataelements.clear();           

           for (int z=0; z<vtSelectedDomains.size(); z++)
           {
               if (hashCriteriaDomains.containsKey((String) vtSelectedDomains.get(z)))
               {
                   String strCriteriaDomain = (String) vtSelectedDomains.get(z);
                   Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);

                    // if domain is a study, dont add Study Name as the formfield            
                    for( int i=0; i < vtSelectedFields.size(); i++ ){                
                        // Set the selected field to formfield, to be used in search query
                        vtSearchFormFields.add((String)vtSelectedFields.get(i));
                    }            

               }    
           }    

           for (int y=0; y<vtSelectedSmartforms.size(); y++)
           {
               if (hashCriteriaDomains.containsKey((String) vtSelectedSmartforms.get(y)))
               {
                   String strCriteriaDomain = (String) vtSelectedSmartforms.get(y);
                   Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);

                    // if domain is a study, dont add Study Name as the formfield            
                    for( int j=0; j < vtSelectedFields.size(); j++ ){

                        vtSelectedDataelements.add( (String)vtSelectedFields.get(j) );
                        // Prepend the smartform name to the dataelement, this is to
                        // account for the the same dataelements belonging to different smartforms
                        // This is used when building the XML file for the search results page
                        vtSelectedSFDataelements.add ( (strCriteriaDomain + "__" + (String)vtSelectedFields.get(j)) );                    
                    }            
               }    
           }    
               
               
           // When criterias are added for smartform and when dataelements are selected for display
           // do not execute in the normal way, but execute by putting the "query" object as subquery               
           if ( vtSelectedDataelements.isEmpty() == false ){
               for( int h = 0; h < vtSelectedSmartforms.size(); h++){
                    if( vtDomainVal.contains( vtSelectedSmartforms.get(h).toString() ) ){
                        blSubQuery = true;
                        break;
                    }   
               }   
           }    

           
           // if blSubquery is false, then set the display fields as formfields
           if( blSubQuery == false ){

               //boolean blSubQuery = false;
               // only if datelements are selected show smartform details
               if( vtSelectedDataelements.isEmpty() == false ){        

                   // it is a smartform so add smartform columns
                   // if patient & biospecimen is not selected
                   if(!vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN")){
                      vtSearchFormFields.add("SMARTFORMPARTICIPANTS_intParticipantID"); 
                   }

                   vtSearchFormFields.add("STUDY_intStudyID");
                   vtSearchFormFields.add("STUDY_strStudyName");
                   vtSearchFormFields.add("SMARTFORM_strSmartformName");
                   vtSearchFormFields.add("SMARTFORMPARTICIPANTS_intSmartformParticipantID");                
                   vtSearchFormFields.add("DATAELEMENTS_intDataElementID");       
                   vtSearchFormFields.add("DATAELEMENTS_strDataElementName");                     
                   vtSearchFormFields.add("SMARTFORMRESULTS_strDataElementResult");

               }

           }else{
               // if blSubquery is true, then set only the distinct field as the formfield                          
                vtSearchFormFields.clear();
                vtSearchFormFields.add(strDistinct);
           }
           
           // Check for formfields - if it is not empty proceed
           if( vtSearchFormFields.isEmpty() == false){
               
               // Set the form fields
               query1.setFields(vtSearchFormFields, null);               

//Seenadist               query1.setDistinctField( strDistinct );     

               // to get the smartform names
               for( int k=0; k < vtSelectedSmartforms.size(); k++ ){
                   
                   int last = vtSelectedSmartforms.size() - 1;
                   
                     if( vtSelectedSmartforms.size() == 1){
                        query1.setWhere( null, 0, "SMARTFORM_strSmartformName", "=", (String)vtSelectedSmartforms.get(k), 0, DALQuery.WHERE_HAS_VALUE );                            
                     }else if(k == 0){
                        query1.setWhere( null, 1, "SMARTFORM_strSmartformName", "=", (String)vtSelectedSmartforms.get(k), 0, DALQuery.WHERE_HAS_VALUE );                            
                     }else if( k == last){
                        query1.setWhere( "OR", 0, "SMARTFORM_strSmartformName", "=", (String)vtSelectedSmartforms.get(k), 1, DALQuery.WHERE_HAS_VALUE );                            
                     }else{
                        query1.setWhere( "OR", 0, "SMARTFORM_strSmartformName", "=", (String)vtSelectedSmartforms.get(k), 0, DALQuery.WHERE_HAS_VALUE );                            
                     }          
               }
               
               
               // if study is selected, add it to the where condition
               for( int m=0; m < vtSelectedStudys.size(); m++){

                       int last = vtSelectedStudys.size() - 1;

                         if( vtSelectedStudys.size() == 1){
                            query1.setWhere( "AND", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                         }else if(m == 0){
                            query1.setWhere( "AND", 1, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                         }else if( m == last){
                            query1.setWhere( "OR", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 1, DALQuery.WHERE_HAS_VALUE );                            
                         }else{
                            query1.setWhere( "OR", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                         }          
               }
               
               
               // set the domain to Study/ Bio accordingly, only if participantDomain is not defined
               if ( vtParticipantDomain.isEmpty() == true ){
                   
                   if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
                       query1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Study", 0, DALQuery.WHERE_HAS_VALUE );                            
                   }
                   else if( vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") ){
                       query1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Study", 0, DALQuery.WHERE_HAS_VALUE );                            
                   }
                   else if( !vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") && !vtSelectedDomains.contains("BIOANALYSIS")){
                       query1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Biospecimen", 0, DALQuery.WHERE_HAS_VALUE );                            
                   }
                   else if( !vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS")){
                       query1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Bioanalysis", 0, DALQuery.WHERE_HAS_VALUE );                            
                   }
               }             
               
               // to get the dataelement name              
               query1.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", "SMARTFORMRESULTS_intDataElementID", 0, DALQuery.WHERE_BOTH_FIELDS);

               // set the smartforms dataelements selected in where condition
                int last = vtSelectedDataelements.size() - 1;

                // if no suquery, then set the selected fields as formfields for display
                if( blSubQuery == false ){    

                    for( int m=0; m < vtSelectedDataelements.size(); m++ ){

                        if( vtSelectedDataelements.size() == 1){
                            query1.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSelectedDataelements.get(m), 0, DALQuery.WHERE_HAS_VALUE);
                        }else if( m == 0 ){
                            query1.setWhere("AND", 1, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSelectedDataelements.get(m), 0, DALQuery.WHERE_HAS_VALUE);
                        }else if( m == last ){
                            query1.setWhere("OR", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSelectedDataelements.get(m), 1, DALQuery.WHERE_HAS_VALUE);
                        }else{                            
                            // Set the selected field to where condition, to be used in search query                        
                            query1.setWhere("OR", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSelectedDataelements.get(m), 0, DALQuery.WHERE_HAS_VALUE);
                        }

                    }      
                }


                 // set the deleted fields
                for( int m = 0; m < vtSelectedDomains.size(); m++){

                    String name = (String)vtSelectedDomains.get(m);

                    if(!name.equals("BIOANALYSIS")){                     
                        name = name+"_intDeleted";
                        query1.setWhere( "AND", 0, name, "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    }
                }                

                query1.setWhere( "AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query1.setWhere( "AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query1.setWhere( "AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                query1.setWhere( "AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );           

                if((strSetSF.equals("pat-sf") || strSetSF.equals("bio-sf")) ){
                    query1.setWhere( "AND", 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );   
                }
                
                if( vtSelectedStudys.isEmpty() == false ){                    





                }
                //else if( vtParticipantDomain.isEmpty() == false ){
               // else{  
                    //if ( vtParticipantDomain.contains("PATIENT") && blPartBio == false){
                    if(strSetSF.equals("pat-sf")){
                        query1.setWhere( "AND", 0, "PATIENT_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );

                    //}else if( vtParticipantDomain.contains("BIOSPECIMEN") ){
                    }else if(strSetSF.equals("bio-sf")){
                           query1.setWhere( "AND", 0, "BIOSPECIMEN_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    }
                //}

                // if partDomain was BIO, the domain was set to BIO & so now reset the flag
                if(blPartBio == true){
                    blPartBio = false;
                }
                // Similarly reset flag for BIOANALYSIS
                if( blPartBioAnalysis == true){
                    blPartBioAnalysis = false;
                }
   
        }
        }
        }catch(Exception e){  
            LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);           
        } 
    
     return query1;
        
    }   
    
    
    /*
     * This is executed if the criterias are added for smartform and 
     * and if dataelements are selected for display
     */
    private DALQuery executeSubQuery(){
        
       DALQuery mainQuery1 = new DALQuery();
       
    try{
        String strSetSF = "";
        
           // if PATIENT & BIO & SF is in the selected domain
           if( (vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN")) || (vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS")) ){
               
                if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
                   
                    mainQuery1.setDomain("PATIENT", null, null, null);
                    mainQuery1.setDomain("BIOSPECIMEN", "BIOSPECIMEN_intPatientID", "PATIENT_intInternalPatientID", "INNER JOIN");
                 }
               
                // if it contains BIO and BIOANALYSIS, not PAT
                else if (vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS")){
                    mainQuery1.setDomain("BIOSPECIMEN", null, null, null);
                    
                } 
              
               // if PATIENT & BIO & Study & SF is selected
               
                       // if Participant domain has  PAT & BIO/ PATIENT / BIO/ No PAT & BIO
                       if ( (vtParticipantDomain.contains("PATIENT") && vtParticipantDomain.contains("BIOSPECIMEN")) || (vtParticipantDomain.contains("PATIENT") && vtParticipantDomain.contains("BIOANALYSIS")) || (vtParticipantDomain.contains("BIOSPECIMEN") && vtParticipantDomain.contains("BIOANALYSIS"))){                  

                           // if the participantDomain is not BIOSPECIMEN - then PAT - PATSF - SF & PAT - BIO
                           if (strBioSF.equals("") && vtSelectedDomains.contains("PATIENT") ){
                               
                                mainQuery1.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
                                mainQuery1.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
                                mainQuery1.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                                strSetSF = "pat-sf";
                           }   
                           else if (strBioSF.equals("") && vtSelectedDomains.contains("BIOSPECIMEN") ){
                               
                                mainQuery1.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
                                mainQuery1.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_SMARTFORM_intStudyID", "INNER JOIN");
                                mainQuery1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                                strSetSF = "bio-sf";
                           } 
                           // if the participantDomain is BIOSPECIMEN then - PAT - BIO - BIOSF - SF
                           else if (strBioSF.equals("bio")){                                
                               
                                mainQuery1.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
                                mainQuery1.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_SMARTFORM_intStudyID", "INNER JOIN");
                                mainQuery1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                                strBioSF = "";
                                strSetSF = "bio-sf";
                           }   
                           // if the participantDomain is BIOANALYSIS then - PAT - BIO - BIOSF - SF
                           else if (strBioSF.equals("bioanalysis")){      
                                                         
                                mainQuery1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_intBiospecimenID", "SMARTFORMPARTICIPANTS_intParticipantID","INNER JOIN");
                                mainQuery1.setDomain( "STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN");
                                strBioSF = "";
                                strSetSF = "";
                           }   

                       }else if( vtParticipantDomain.contains("PATIENT") ){                       

                           mainQuery1.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
                           mainQuery1.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
                           mainQuery1.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                           strSetSF = "pat-sf";
                           
                       }else if ( vtParticipantDomain.contains("BIOSPECIMEN") ){                       

                           mainQuery1.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
                           mainQuery1.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_SMARTFORM_intStudyID", "INNER JOIN");
                           mainQuery1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                           strSetSF = "bio-sf";
                       }
                        else if (vtParticipantDomain.contains("BIOANALYSIS")){      
                               
                            //mainQuery1.setDomain("BIOANALYSIS", "BIOANALYSIS_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
                            mainQuery1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_intBiospecimenID", "SMARTFORMPARTICIPANTS_intParticipantID","INNER JOIN");
                            mainQuery1.setDomain( "STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN");                            
                            strSetSF = "";
                        }            
                        else if ( vtParticipantDomain.contains("blank") || vtParticipantDomain.isEmpty() == true ){ // if participant domain is not defined
                       
                           mainQuery1.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
                           mainQuery1.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
                           mainQuery1.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
                           strSetSF = "pat-sf";
                        }                  
               
           } 
           
           // if PAT & SF is selected & BIO is not selected
           else if ( vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") ){
               mainQuery1.setDomain("PATIENT", null, null, null);
               
               // if PATIENT & Study & SF is selected & BIO is not selected
                              
                   mainQuery1.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
                   mainQuery1.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
                   mainQuery1.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                   
                   strSetSF = "pat-sf";                   
           }
           
           // if BIO & SF is selected and PAT & Study is not selected
           else if( !vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") && !vtSelectedDomains.contains("BIOANALYSIS")){
           
               mainQuery1.setDomain("BIOSPECIMEN", null, null, null);             
               mainQuery1.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
               mainQuery1.setDomain("STUDY", "STUDY_intStudyID", "BIOSPECIMEN_SMARTFORM_intStudyID", "INNER JOIN");
               mainQuery1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                       
               strSetSF = "bio-sf";
           }
           // if it contains BIO & BIOANALYSIS & not PAT / Study
           else if ( !vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS") ){
                
               mainQuery1.setDomain("BIOSPECIMEN", null, null, null); 
               //mainQuery1.setDomain("BIOANALYSIS", "BIOANALYSIS_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
               mainQuery1.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_intBiospecimenID", "SMARTFORMPARTICIPANTS_intParticipantID","INNER JOIN");
               mainQuery1.setDomain( "STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN");
               
               strSetSF = "";
           }   
           // if study & SF is selected and no PAT & BIO is selected
           else if( !vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedStudys.isEmpty() == false){
            
               mainQuery1.setDomain("STUDY", null, null, null);
               mainQuery1.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intStudyID", "STUDY_intStudyID","INNER JOIN");                   
               mainQuery1.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");                   
               strSetSF = "pat-sf";
           }   
           mainQuery1.setDomain("SMARTFORM", "SMARTFORM_intSmartformID", "SMARTFORMPARTICIPANTS_intSmartformID", "INNER JOIN");           
           mainQuery1.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN"); 
           mainQuery1.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID","INNER JOIN");           
           mainQuery1.setDomain("SMARTFORMRESULTS", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "SMARTFORMRESULTS_intSmartformParticipantID", "INNER JOIN");

           
           // Set the other domains and the join between them           
           for (int i=0; i < vtSelectedDomains.size(); i++){

               // For each domain, get its join object
               String strDomain = (String)vtSelectedDomains.get(i);

               if(!strDomain.equals("PATIENT") && !strDomain.equals("BIOSPECIMEN") && !strDomain.equals("BIOANALYSIS")){

                   Hashtable hashJoins = (Hashtable)DatabaseSchema.getSearchDomains().get(strDomain);
                   
                   // if the first domain is CONSENT
                   if( i == 0 && strDomain.equals("CONSENT")){
                       mainQuery1.setDomain( "CONSENT", "PATIENT_intInternalPatientID", "CONSENT_intInternalPatientID", "INNER JOIN");
                       
                   }
                   
                   // if the first domain is STUDY
                   else if( i == 0 && strDomain.equals("STUDY")){
                       mainQuery1.setDomain( "STUDY", "STUDY_intStudyID", "BIOSPECIMEN_intStudyKey", "INNER JOIN");
                       
                   }                                                                          
                                     
                   // Get all possible joins for this strDomain, with all the domains in vtSelectedDomains
                   for( int j=0; j < i; j++ ){

                       // Check to see if there is a join between strDomain and vtSelectedDomains(j)
                       if(hashJoins.containsKey((String)vtSelectedDomains.get(j))){ 
                           // Get the join object, which consists of join between strDomain and vtSelectedDomains.get(j)
                            DBJoin joinObj = (DBJoin)hashJoins.get(vtSelectedDomains.get(j));
                             if(strDomain.equals("CONSENT")){
                                mainQuery1.setDomain( "CONSENT", "PATIENT_intInternalPatientID", "CONSENT_intInternalPatientID", "INNER JOIN");
                            }else{
                                mainQuery1.setDomain( strDomain, joinObj.getFirstField(), joinObj.getSecondField(), joinObj.getType());
                            }
                            break;
                       } 
                   }

               }
           }                  
           
           // if first domain is CONSENTSTUDY, set it after consent & other domains have been set
           if(vtSelectedDomains.isEmpty() == false && vtSelectedDomains.get(0).toString().equals("CONSENTSTUDY")){
               mainQuery1.setDomain( "CONSENTSTUDY", "CONSENT_intConsentKey", "CONSENTSTUDY_intConsentKey", "INNER JOIN");
           }
           
           // Set the formfields for DISTINCT conditions           
           String strDistinct = "";
           
           // if PAT & BIO is selected
           if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
                //strDistinct = "PATIENT_intInternalPatientID";
               strDistinct = "BIOSPECIMEN_intBiospecimenID";
           }
           // if only PAT is selected
           else if( vtSelectedDomains.contains("PATIENT")){
               strDistinct = "PATIENT_intInternalPatientID";
           }
           // if only BIO is selected
           else if( vtSelectedDomains.contains("BIOSPECIMEN") ){
               strDistinct = "BIOSPECIMEN_intBiospecimenID";
           }
           // if only study is selected, and no PAT/ BIO
           else if( vtSelectedStudys.isEmpty() == false){
               strDistinct = "STUDY_intStudyID";
           }
           vtSearchFormFields.clear();
           vtSearchFormFields.add(strDistinct);

       
           // Add BIO KEY to formfields only if PAT & BIO is selected, but it is hidden
           if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
                vtSearchFormFields.add("PATIENT_intInternalPatientID");
           }
           
           // Build the form fields from all the fields selected in criteriadomains - only schema domains
           // And also the dataelements selected for the smartforms
           vtSelectedDataelements.clear();
           vtSelectedSFDataelements.clear();

           for (int z=0; z<vtSelectedDomains.size(); z++)
           {
               if (hashCriteriaDomains.containsKey((String) vtSelectedDomains.get(z)))
               {
                   String strCriteriaDomain = (String) vtSelectedDomains.get(z);
                   Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);

                    // if domain is a study, dont add Study Name as the formfield            
                    for( int i=0; i < vtSelectedFields.size(); i++ ){                
                        // Set the selected field to formfield, to be used in search query
                        vtSearchFormFields.add((String)vtSelectedFields.get(i));
                    }            

               }    
           }    

           for (int y=0; y<vtSelectedSmartforms.size(); y++)
           {
               if (hashCriteriaDomains.containsKey((String) vtSelectedSmartforms.get(y)))
               {
                   String strCriteriaDomain = (String) vtSelectedSmartforms.get(y);
                   Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);

                    // if domain is a study, dont add Study Name as the formfield            
                    for( int j=0; j < vtSelectedFields.size(); j++ ){

                        vtSelectedDataelements.add( (String)vtSelectedFields.get(j) );
                        // Prepend the smartform name to the dataelement, this is to
                        // account for the the same dataelements belonging to different smartforms
                        // This is used when building the XML file for the search results page
                        vtSelectedSFDataelements.add ( (strCriteriaDomain + "__" + (String)vtSelectedFields.get(j)) );                    
                    }            
               }    
           }    
          
           // only if datelements are selected show smartform details
           if( vtSelectedDataelements.isEmpty() == false ){        

               // it is a smartform so add smartform columns
               // if patient & biospecimen is not selected
               if(!vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN")){
                  vtSearchFormFields.add("SMARTFORMPARTICIPANTS_intParticipantID"); 
               }
               
               vtSearchFormFields.add("STUDY_intStudyID");
               vtSearchFormFields.add("STUDY_strStudyName");
               vtSearchFormFields.add("SMARTFORM_strSmartformName");
               vtSearchFormFields.add("SMARTFORMPARTICIPANTS_intSmartformParticipantID");    
               vtSearchFormFields.add("DATAELEMENTS_intDataElementID");               
               vtSearchFormFields.add("DATAELEMENTS_strDataElementName");                     
               vtSearchFormFields.add("SMARTFORMRESULTS_strDataElementResult");             

           }
           
           // Check for formfields - if it is not empty proceed
           if( vtSearchFormFields.size() > 1){
               
               // Set the form fields
               mainQuery1.setFields(vtSearchFormFields, null);
               
               //mainQuery1.setDistinctField("SMARTFORMPARTICIPANTS_intParticipantID"); 
//Seenadist               mainQuery1.setDistinctField( strDistinct );  

                              
               // to get the smartform names
               for( int k=0; k < vtSelectedSmartforms.size(); k++ ){
                   
                   int last = vtSelectedSmartforms.size() - 1;
                   
                     if( vtSelectedSmartforms.size() == 1){
                        mainQuery1.setWhere( null, 0, "SMARTFORM_strSmartformName", "=", (String)vtSelectedSmartforms.get(k), 0, DALQuery.WHERE_HAS_VALUE );                            
                     }else if(k == 0){
                        mainQuery1.setWhere( null, 1, "SMARTFORM_strSmartformName", "=", (String)vtSelectedSmartforms.get(k), 0, DALQuery.WHERE_HAS_VALUE );                            
                     }else if( k == last){
                        mainQuery1.setWhere( "OR", 0, "SMARTFORM_strSmartformName", "=", (String)vtSelectedSmartforms.get(k), 1, DALQuery.WHERE_HAS_VALUE );                            
                     }else{
                        mainQuery1.setWhere( "OR", 0, "SMARTFORM_strSmartformName", "=", (String)vtSelectedSmartforms.get(k), 0, DALQuery.WHERE_HAS_VALUE );                            
                     }          
               }
               
               
               // if study is selected, add it to the where condition
               for( int m=0; m < vtSelectedStudys.size(); m++){

                       int last = vtSelectedStudys.size() - 1;

                         if( vtSelectedStudys.size() == 1){
                            mainQuery1.setWhere( "AND", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                         }else if(m == 0){
                            mainQuery1.setWhere( "AND", 1, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                         }else if( m == last){
                            mainQuery1.setWhere( "OR", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 1, DALQuery.WHERE_HAS_VALUE );                            
                         }else{
                            mainQuery1.setWhere( "OR", 0, "STUDY_strStudyName", "=", vtSelectedStudys.get(m).toString(), 0, DALQuery.WHERE_HAS_VALUE );                            
                         }          
               }

               
               // set the domain to Study/ Bio accordingly, only if participantDomain is not defined
               if ( vtParticipantDomain.isEmpty() == true ){
                   
                   if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
                       mainQuery1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Study", 0, DALQuery.WHERE_HAS_VALUE );                            
                   }
                   else if( vtSelectedDomains.contains("PATIENT") && !vtSelectedDomains.contains("BIOSPECIMEN") ){
                       mainQuery1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Study", 0, DALQuery.WHERE_HAS_VALUE );                            
                   }
                   else if( !vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") && !vtSelectedDomains.contains("BIOANALYSIS")){
                       mainQuery1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Biospecimen", 0, DALQuery.WHERE_HAS_VALUE );                            
                   }
                   else if( !vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") && vtSelectedDomains.contains("BIOANALYSIS")){
                       mainQuery1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_strDomain", "=", "Bioanalysis", 0, DALQuery.WHERE_HAS_VALUE );                            
                   }
               }           
               
               // to get the dataelement name              
               mainQuery1.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", "SMARTFORMRESULTS_intDataElementID", 0, DALQuery.WHERE_BOTH_FIELDS);
                    
                
               // set the smartforms dataelements selected in where condition
                int last = vtSelectedDataelements.size() - 1;                

                for( int m=0; m < vtSelectedDataelements.size(); m++ ){
                    
                    if( vtSelectedDataelements.size() == 1){
                        mainQuery1.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSelectedDataelements.get(m), 0, DALQuery.WHERE_HAS_VALUE);
                    }else if( m == 0 ){
                        mainQuery1.setWhere("AND", 1, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSelectedDataelements.get(m), 0, DALQuery.WHERE_HAS_VALUE);
                    }else if( m == last ){
                        mainQuery1.setWhere("OR", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSelectedDataelements.get(m), 1, DALQuery.WHERE_HAS_VALUE);
                    }else{                            
                        // Set the selected field to where condition, to be used in search query                        
                        mainQuery1.setWhere("OR", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtSelectedDataelements.get(m), 0, DALQuery.WHERE_HAS_VALUE);
                    }

                }                    
                
                // set the deleted fields
                for( int m = 0; m < vtSelectedDomains.size(); m++){

                     String name = (String)vtSelectedDomains.get(m);
                    
                    if(!name.equals("BIOANALYSIS")){
                     
                        name = name+"_intDeleted";
                        mainQuery1.setWhere( "AND", 0, name, "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    }
                }
                                
                mainQuery1.setWhere( "AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                mainQuery1.setWhere( "AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                mainQuery1.setWhere( "AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                mainQuery1.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                mainQuery1.setWhere( "AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );           

                if ((strSetSF.equals( "pat-sf" ) || strSetSF.equals("bio-sf"))){
                        mainQuery1.setWhere( "AND", 0, "STUDY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                }        
                
                if( vtSelectedStudys.isEmpty() == false ){                    




                    
                }//else if( vtParticipantDomain.isEmpty() == false ){
                    
                    //if ( vtParticipantDomain.contains("PATIENT")){
                    if( strSetSF.equals("pat-sf")){
                        mainQuery1.setWhere( "AND", 0, "PATIENT_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    }else if( strSetSF.equals("bio-sf") ){
                    //}else if( vtParticipantDomain.contains("BIOSPECIMEN") ){
                        mainQuery1.setWhere( "AND", 0, "BIOSPECIMEN_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    }
                //}     
               
           }
           
           strBioSF = "";
           
        }catch(Exception e){  
            LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);           
        } 
       
       return mainQuery1;       
        
    }
    
    
    /*
     * Add criteria for searching missing records
     */
     private void addCriteriaForMissingRecords(){
         
         // if the Missing for domain/ missing without domain is blank
         if( runtimeData.getParameter("MissingForList") == null || runtimeData.getParameter("MissingWithoutList") == null){
             strErrorMessage = "Please add another dataset, to the selected dataset list";
         }         
         // Only 1 criteria can be added
         else if(vtMissingFor.isEmpty() == false){
             strErrorMessage = "Please delete the existing missing record criteria to add a criteria";
         }
         else{
            vtMissingFor.add( runtimeData.getParameter("MissingForList") );
            vtMissingWithout.add( runtimeData.getParameter("MissingWithoutList") );
         }
     }
     
    /*
     * Add criteria for searching missing smartform records
     */
     private void addCriteriaForMissingSFRecords(){
         
         // if the Missing SF: selected smartform, selected domain and selected field is blank
         if( runtimeData.getParameter("MissingSFDomainList") == null || runtimeData.getParameter("MissingSFFieldList") == null){
             strErrorMessage = "Please add another dataset, to the selected dataset list";
         }         
         // Only 1 criteria can be added
         else if(vtMissingSFDomain.isEmpty() == false){
             strErrorMessage = "Please delete the existing missing smartform record criteria to add a criteria";
         }
         else{
            // Add the currently selected (highlighted) smartform in the dataset
            vtMissingSF.add( strSelectedDomainSel );
            vtMissingSFDomain.add( runtimeData.getParameter("MissingSFDomainList") );
            vtMissingSFField.add( runtimeData.getParameter("MissingSFFieldList") );
         }
     }

     /*
      * Build the xml for the criterias used for searching Missing Records
      */
     private String buildCriteriasForMissingRecords(){         
             
        String strResult = "";        
        
        for(int i=0; i < vtMissingFor.size(); i++){  
                       
            int j = i + 1;
            strResult+= "<searchMissingCriterias>" + 
                        "<MissingCriteriaNo>" + j + "</MissingCriteriaNo>" +
                        "<MissingForSel>"+ vtMissingFor.get(i) +"</MissingForSel>" +
                        "<MissingWithoutSel>"+ vtMissingWithout.get(i) +"</MissingWithoutSel>" +                        
                        "</searchMissingCriterias>";  
        } 
        
        return strResult; 
         
     }
     

     /*
      * Build the xml for the criterias used for searching Missing smartform Records
      */
     private String buildCriteriasForMissingSFRecords(){         
             
        String strResult = "";        
        DALQuery query = new DALQuery();
        String fieldName = "";
        ResultSet rs = null;
        
        try{
        for(int i=0; i < vtMissingSFDomain.size(); i++){  
                       
            int j = i + 1;
            
            // Get the question name corresponding to the selected question ID (dataelement key)
            query.reset();
            query.setDomain("DATAELEMENTS", null, null, null);
            query.setField("DATAELEMENTS_strDataElementName", null);            
            query.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=", (String)(vtMissingSFField.get(i)), 0, DALQuery.WHERE_HAS_VALUE);            
            rs = query.executeSelect();

            if (rs != null)
            {
                rs.next();  
                fieldName = rs.getString("DATAELEMENTS_strDataElementName");                
            }    

            // Close the resultset
            rs.close();
            rs = null;            
            
            strResult+= "<searchMissingSFCriterias>" + 
                        "<MissingSFCriteriaNo>" + j + "</MissingSFCriteriaNo>" +
                        "<MissingSFSel>"+ vtMissingSF.get(i) +"</MissingSFSel>" +
                        "<MissingSFDomainSel>"+ vtMissingSFDomain.get(i) +"</MissingSFDomainSel>" +                        
                        "<MissingSFFieldSel>"+ vtMissingSFField.get(i) +"</MissingSFFieldSel>" + 
                        "<MissingSFFieldNameSel>"+ fieldName +"</MissingSFFieldNameSel>" +                         
                        "</searchMissingSFCriterias>";  
        } 
        
        return strResult; 
        }
        catch(Exception e){
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
             return strResult;
        }
        
     }
     
     
     /*
      * Delete the criteria added for searching missing records
      *
      */
     private void deleteCriteriaForMissingRecords(){         
       
         int k = Integer.parseInt( runtimeData.getParameter("MissingCriteriaNo") );
         k = k - 1 ;
         
         if( runtimeData.getParameter("MissingCriteriaNo") != null && !runtimeData.getParameter("MissingCriteriaNo").equals("") ){
              vtMissingFor.remove( k ) ;
              vtMissingWithout.remove( k );
         }         
     }
     
     /*
      * Delete the criteria added for searching missing records
      *
      */
     private void deleteCriteriaForMissingSFRecords(){         
       
         int k = Integer.parseInt( runtimeData.getParameter("MissingSFCriteriaNo") );
         k = k - 1 ;
         
         if( runtimeData.getParameter("MissingSFCriteriaNo") != null && !runtimeData.getParameter("MissingSFCriteriaNo").equals("") ){
              vtMissingSF.remove( k ) ;
              vtMissingSFDomain.remove( k );
              vtMissingSFField.remove( k );              
         }         
     }
     
     /*
      * Update the criteria for searching missing records
      */
     private void updateCriteriaForMissingRecords(){         
       
         deleteCriteriaForMissingRecords();
         int k = Integer.parseInt( runtimeData.getParameter("MissingCriteriaNo") );
         k = k - 1 ;
         
         if( runtimeData.getParameter("MissingCriteriaNo") != null && !runtimeData.getParameter("MissingCriteriaNo").equals("") ){
              vtMissingFor.insertElementAt( runtimeData.getParameter("MissingForList"), k);
              vtMissingWithout.insertElementAt( runtimeData.getParameter("MissingWithoutList"), k);
         }         
     }
     

     /*
      * Update the criteria for searching missing smartform records
      */
     private void updateCriteriaForMissingSFRecords(){         
       
         deleteCriteriaForMissingSFRecords();
         int k = Integer.parseInt( runtimeData.getParameter("MissingSFCriteriaNo") );
         k = k - 1 ;
         
         if( runtimeData.getParameter("MissingSFCriteriaNo") != null && !runtimeData.getParameter("MissingSFCriteriaNo").equals("") ){
              vtMissingSF.insertElementAt( strSelectedDomainSel, k);
              vtMissingSFDomain.insertElementAt( runtimeData.getParameter("MissingSFDomainList"), k);
              vtMissingSFField.insertElementAt( runtimeData.getParameter("MissingSFFieldList"), k);              
         }         
     }

     
     /*
     * Build the missing records search criterias in Edit mode
     *
     */
     private void buildEditMissingCriteriasVectors(){
     
        vtMissingFor.clear();
        vtMissingWithout.clear();        
        
        //runtimeData.setParameter("MissingCriteriaNo", runtimeData.getParameter("MissingCriteriaNo"));
        for(int i = 1;;i++){
          
            if( runtimeData.getParameter("MissingCriteriaNo"+i) != null ){
                
                vtMissingFor.add( runtimeData.getParameter("MissingForSel"+i));
                vtMissingWithout.add( runtimeData.getParameter("MissingWithoutSel"+i));        
              
            }else{
                break;
            }
        
        }
        
    }
    
     /*
     * Build the missing records search criterias in Edit mode
     *
     */
     private void buildEditMissingSFCriteriasVectors(){
     
        vtMissingSF.clear();
        vtMissingSFDomain.clear();
        vtMissingSFField.clear();
        
        //runtimeData.setParameter("MissingCriteriaNo", runtimeData.getParameter("MissingCriteriaNo"));
        for(int i = 1;;i++){
          
            if( runtimeData.getParameter("MissingSFCriteriaNo"+i) != null ){
                
                vtMissingSF.add( runtimeData.getParameter("MissingSFSel"+i));
                vtMissingSFDomain.add( runtimeData.getParameter("MissingSFDomainSel"+i)); 
                vtMissingSFField.add( runtimeData.getParameter("MissingSFFieldSel"+i));
              
            }else{
                break;
            }
        
        }
        
    }

     
     /*
      * For a domain selected in searching missing records, 
      * load the related domains in without list
      *
      */
     private String buildWithoutList( String strDomainInFor ){
         
         String strResult = "";
         
        // Fetch all the related domains of strDomainInFor
        Hashtable hashSearchDomains = DatabaseSchema.getSearchDomains();
        for( int j = 0; j < vtSelectedDomains.size(); j++ ){

            Hashtable hashJoins = (Hashtable)hashSearchDomains.get((String)vtSelectedDomains.get(j));
            String strCurrentDomain =  (String)vtSelectedDomains.get(j);

            if( hashJoins.containsKey(strDomainInFor) ){  
                strResult+= "<MissingWithoutList>";
                strResult+= "<strDomainName>"+ strCurrentDomain +"</strDomainName>";
                strResult+= "</MissingWithoutList>";                    
            }
        }     

        // if forSelected is PATIENT, then load the studys in WITHOUTlist
        if( strDomainInFor.equals("PATIENT") && vtSelectedStudys.isEmpty() == false ){
            for( int i =0; i < vtSelectedStudys.size(); i++){
                strResult+= "<MissingWithoutList>";
                strResult+= "<strDomainName>"+ vtSelectedStudys.get(i).toString() +"</strDomainName>";
                strResult+= "</MissingWithoutList>";
            }                
        }

        return strResult;         
         
     }
     
     
     /*
      *  Execute Search for Missing Records
      *
      */
     private ResultSet executeSearchQueryForMissingRecords(){     
         DALQuery query = new DALQuery();
         try{           
             
             Vector vtJoinDomain = new Vector(10, 2);
             vtSearchFormFields.clear();
             vtSelectedDataelements.clear();
             vtSelectedSFDataelements.clear();             
             
             // Set the first domain field
             query.setDomain(vtMissingFor.get(0).toString(), null, null, null);
             
             // get the missing For & Without values
             String strMissingFor = vtMissingFor.get(0).toString();
             String strMissingWithout = vtMissingWithout.get(0).toString();       
             
             // if Missing Without domain is a study name, then set domain as STUDY
             // which is later joined as PAT- CONSENT -CONSENTSTUDY- STUDY
             if(isStudy(strMissingWithout)){
                 strMissingWithout = "STUDY";
             }
             
             // Get the selected fields of the MissingFor domain                        
             Vector vtSelectedFields1 = (Vector)hashCriteriaDomains.get(vtMissingFor.get(0).toString());

             for( int i=0; i < vtSelectedFields1.size(); i++ ){                
                 // Set the selected field to vtSearchFormFields, to be used in search query
                 vtSearchFormFields.add((String)vtSelectedFields1.get(i));
             }
             vtJoinDomain.add(strMissingFor);  
             
             // Set the other joins by getting all the other domains related to the MissingFor domain value
             Hashtable hashSearchDomains = DatabaseSchema.getSearchDomains();
             for( int j = 0; j < vtSelectedDomains.size(); j++ ){

                String strDomain = (String)vtSelectedDomains.get(j);
                Hashtable hashJoins = (Hashtable)hashSearchDomains.get(strDomain);
                
                // The domain must have join with the MissingFor domain & must not be MissingWithout domain 
                if( hashJoins.containsKey(strMissingFor) && !strDomain.equals(strMissingWithout)){ 
                                        
                    DBJoin joinObj = (DBJoin)hashJoins.get(strMissingFor);                
                    query.setDomain( strDomain, joinObj.getFirstField(), joinObj.getSecondField(), joinObj.getType());
                    
                    // For this domain which is joined, get its selected fields
                    Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strDomain);
                    
                    if(vtSelectedFields == null){}
                    else{
                        for( int i=0; i < vtSelectedFields.size(); i++ ){                
                            // Set the selected field to vtSearchFormFields, to be used in search query
                            vtSearchFormFields.add((String)vtSelectedFields.get(i));                            
                        }  
                    }
                    // set the joindomains into the vector
                    vtJoinDomain.add(strDomain);
                    
                }
             }      
             
             query.setFields(vtSearchFormFields, null);
             // Set the Where condition with the primary key of MissingFor domain             
             String strPrimaryKey = (String)hashPrimaryKey.get( strMissingFor );
             String strForeignKey = (String)hashPrimaryKey.get( strMissingFor+ " WITHOUT " +strMissingWithout);
                         
             // Define the subQuery
             DALQuery subQuery = new DALQuery();
             // if MissingWithout is a Study name, then set the below join condition
             if(isStudy(vtMissingWithout.get(0).toString())){    
                 
                 subQuery.setDomain( "PATIENT", null, null, null);                       
                 subQuery.setDomain( "CONSENT", "CONSENT_intInternalPatientID", "PATIENT_intInternalPatientID", "INNER JOIN");
                 subQuery.setDomain( "CONSENTSTUDY", "CONSENTSTUDY_intConsentKey", "CONSENT_intConsentKey", "INNER JOIN");
                 subQuery.setDomain( "STUDY", "STUDY_intStudyID", "CONSENTSTUDY_intStudyID", "INNER JOIN");                             
             }else{
                 subQuery.setDomain(strMissingWithout, null, null, null);             
             }
             
             subQuery.setField(strForeignKey, null);
//Seenadist             subQuery.setDistinctField(strForeignKey);
             
             // if MissingWithout is CONSENT, then set the below where condition
             if(strMissingWithout.equals("CONSENT"))
                subQuery.setWhere(null, 0, "CONSENT_strFutureStudy", "=", "Yes", 0, DALQuery.WHERE_HAS_VALUE);
             
             // if MissingWithout is a Study name, then set the below where condition
             else if(isStudy(vtMissingWithout.get(0).toString()))
                subQuery.setWhere(null, 0, "STUDY_strStudyName", "=", vtMissingWithout.get(0).toString(), 0, DALQuery.WHERE_HAS_VALUE);
             
             // set the subQuery
             query.setWhere(null, 0, strPrimaryKey , "NOT IN", subQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY);
             
             // set the Where condition based on Search Criterias
             for( int k=0; k < vtJoin.size(); k++){
                 if (vtJoinDomain.contains(vtDomainVal.get(k).toString())){
                    
                        if( k == 0){
                            vtJoin.set(0, "AND");
                        }
                        
                        String strOp = "";
                        String strSearchVal = "";
                        int intOpenBracket = getBracketNo(vtOpenBracket.get(k).toString());
                        int intCloseBracket = getBracketNo(vtCloseBracket.get(k).toString());

                        strOp = dbOperator(vtOp.get(k).toString());
                        if( strOp.equals("IS NULL") ){
                            strOp = "";
                            strSearchVal = "IS NULL";

                        }else if( strOp.equals("IS NOT NULL") ){
                            strOp = "";
                            strSearchVal = "IS NOT NULL";
                        }

                        String strIntlName = vtField.get(k).toString().substring(1, vtField.get(k).toString().length() - 1); 


                        if( vtOp.get(k).toString().equals("Is empty") || vtOp.get(k).toString().equals("Is not empty")){

                            // if condition contains EXCLUDING - and also NULL / NOT NULL
                            if( vtJoin.get(k).toString().equals("EXCLUDING")){
                                 
                                    query.setWhere( "AND NOT", intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                               
                            }else{ // if condition contains only NULL / NOT NULL

                                    query.setWhere( vtJoin.get(k).toString(), intOpenBracket, strIntlName, strOp, strSearchVal, intCloseBracket, DALQuery.WHERE_HAS_NULL_VALUE );
                             
                            }

                        }else if( vtJoin.get(k).toString().equals("EXCLUDING")){

                                query.setWhere( "AND NOT", intOpenBracket, strIntlName, strOp, vtSearchVal.get(k).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );
                            
                        }else{                           
                                query.setWhere( vtJoin.get(k).toString(), intOpenBracket, strIntlName, strOp, vtSearchVal.get(k).toString(), intCloseBracket, DALQuery.WHERE_HAS_VALUE );
                           
                        }

                        // reset back to blank
                        if( k == 0){
                            vtJoin.set(0, "");
                        }
                        
                 }
                 
                 // If the criteria is not related to the Missing Records criterias domains, then delete
                 else{
                     String s = k+"";
                     runtimeData.setParameter("number",s);
                     deleteCriteria();
                 }
             }
             
             query.setWhere( "AND", 0, (vtMissingFor.get(0).toString() + "_intDeleted"), "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
             
             // if smartforms are added, remove
             if(vtSelectedSmartforms.isEmpty() == false){
                  //selected smartforms
                for( int s=0; s < vtSelectedSmartforms.size(); s++ ){
                    vtCriteriasDomains.removeElement(vtSelectedSmartforms.get(s).toString());
                }
                 vtSelectedSmartforms.clear();                 
             }
               
             runtimeData.setParameter("number", (String)null);
             
             //System.err.println("Missing Record Query:======="+query.convertSelectQueryToString());
             
            // case sensitive
            if( runtimeData.getParameter("Sensitivity") != null ){
                query.setCaseSensitive(true);
            }else{
                query.setCaseSensitive(false);
            } 
             
             return query.executeSelect();       
             
         }catch(Exception e){
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
            return null;
         }       
         
     }
     

     /*
      *  Execute Search for Missing Smartform Results - 
      *  smartforms that have not been started 
      *  (smartform not saved for the particular patient or biospecimen)
      *  or new questions added to existing smartforms
      *  and smartform has not since not been resaved for the particular patient or biospecimen.
      *
      */
     
     private ResultSet executeSearchQueryForMissingSFResults(){     
         DALQuery query = new DALQuery();
         DALQuery subQuery = new DALQuery();


         try{           
            
             // set the new query object "query1" to be same as object "query"
            query = executeMissingSFQuery(true);
            subQuery = executeMissingSFQuery(false);
            
            String strMissingSFDomain = vtMissingSFDomain.get(0).toString();
            String strPrimaryKey = (String)hashPrimaryKey.get( strMissingSFDomain );            
            
            // set the subQuery
            query.setWhere("AND", 0, strPrimaryKey , "NOT IN", subQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY);

            // Order by the patient or biospecimen ID    
            if(strMissingSFDomain.equals("PATIENT")){
                query.setOrderBy("PATIENT_intInternalPatientID", "ASC", true);
            }
            else if(strMissingSFDomain.equals("BIOSPECIMEN")){
                query.setOrderBy("BIOSPECIMEN_intBiospecimenID", "ASC", true);
            }

            
            //System.err.println("Missing SF query: " +query.convertSelectQueryToString());            
            
            // case sensitive
            if( runtimeData.getParameter("Sensitivity") != null ){
                query.setCaseSensitive(true);
            }else{
                query.setCaseSensitive(false);
            } 
             
             return query.executeSelect();       
             
         }catch(Exception e){
             LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);
            return null;
         }       
         
     }
     
    
    /*
     * Get the Primarykey of each domain and set it in the Hashtable
     */
    public void sethashPrimaryKey(){
        
        hashPrimaryKey.clear();
        hashPrimaryKey.put("PATIENT", "PATIENT_intInternalPatientID");      
        hashPrimaryKey.put("PATIENT WITHOUT STUDY", "PATIENT_intInternalPatientID");    
        hashPrimaryKey.put("BIOSPECIMEN", "BIOSPECIMEN_intBiospecimenID");
        hashPrimaryKey.put("PATIENT WITHOUT BIOSPECIMEN", "BIOSPECIMEN_intPatientID"); 
        hashPrimaryKey.put("BIOSPECIMEN_TRANSACTIONS", "BIOSPECIMEN_TRANSACTIONS_intBioTransactionID");
        hashPrimaryKey.put("BIOSPECIMEN WITHOUT CELL", "CELL_intBiospecimenID");
        hashPrimaryKey.put("BIOSPECIMEN WITHOUT BIOSPECIMEN_TRANSACTIONS", "BIOSPECIMEN_TRANSACTIONS_intBiospecimenID");
        hashPrimaryKey.put("SITE", "SITE_intSiteID");
        hashPrimaryKey.put("TANK", "TANK_intTankID");
        hashPrimaryKey.put("SITE WITHOUT TANK", "TANK_intSiteID");
        hashPrimaryKey.put("BOX", "BOX_intBoxID");
        hashPrimaryKey.put("TANK WITHOUT BOX", "BOX_intTankID");
        hashPrimaryKey.put("TRAY", "TRAY_intTrayID");
        hashPrimaryKey.put("BOX WITHOUT TRAY", "TRAY_intBoxID");
        hashPrimaryKey.put("CELL", "CELL_intCellID");
        hashPrimaryKey.put("TRAY WITHOUT CELL", "CELL_intTrayID");
        hashPrimaryKey.put("CONSENT", "CONSENT_intConsentKey");
        hashPrimaryKey.put("PATIENT WITHOUT CONSENT", "CONSENT_intInternalPatientID");
        hashPrimaryKey.put("CONSENTSTUDY", "CONSENTSTUDY_intConsentStudyKey");
        hashPrimaryKey.put("CONSENT WITHOUT CONSENTSTUDY", "CONSENTSTUDY_intConsentKey");              
        
    }
    
    
    /*
     * Get the equivalent number,
     * according to the number of brackets
     * 
     */
    
    protected int getBracketNo( String strBrackets ){
        int no = 0;
        
        if(strBrackets.equals("(") || strBrackets.equals(")")){
            no = 1;
        }else if(strBrackets.equals("((") || strBrackets.equals("))")){
            no = 2;
        }else if(strBrackets.equals("(((") || strBrackets.equals(")))")){
            no = 3;
        }        
        return no;        
    }
    
    
    /*
     * To check if all the open brackets have a matching close brackets
     */
    
    private String validateBrackets(){ 
        String strError = "";
        
        int openCnt = 0;
        int closeCnt = 0;
     
        for( int i =0; i < vtOpenBracket.size(); i++){
            openCnt+= getBracketNo (vtOpenBracket.get(i).toString());            
        }
     
       for( int i =0; i < vtCloseBracket.size(); i++){
            closeCnt+= getBracketNo (vtCloseBracket.get(i).toString());            
        }
         
        if( openCnt == closeCnt ){
        }else{            
          strError = "Please refine the criterias brackets. Invalid matching brackets.";
        }
     
       return strError;
        
    }
    
    /*
     * This is executed when missing SF criterias are added
     */
    private DALQuery executeMissingSFQuery (boolean blquery1){
        
       DALQuery querySF = new DALQuery();
       String strSetSF = "";
       
    try{
        
           if( vtMissingSFDomain.contains("PATIENT") ){                       

               querySF.setDomain("PATIENT", null, null, null);
               querySF.setDomain("PATIENT_SMARTFORM", "PATIENT_SMARTFORM_intInternalPatientID", "PATIENT_intInternalPatientID","INNER JOIN");
               if( vtSelectedStudys.isEmpty() == false ){ 
               querySF.setDomain("STUDY", "STUDY_intStudyID", "PATIENT_SMARTFORM_intStudyID", "INNER JOIN");                  
               }
               querySF.setDomain("SMARTFORMPARTICIPANTS", "PATIENT_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
               strSetSF = "pat-sf";

           }else if ( vtMissingSFDomain.contains("BIOSPECIMEN") ){                       

               querySF.setDomain("BIOSPECIMEN", null, null, null);
               querySF.setDomain("BIOSPECIMEN_SMARTFORM", "BIOSPECIMEN_SMARTFORM_intBiospecimenID", "BIOSPECIMEN_intBiospecimenID","INNER JOIN");                   
               querySF.setDomain("SMARTFORMPARTICIPANTS", "BIOSPECIMEN_SMARTFORM_intSmartformKey", "SMARTFORMPARTICIPANTS_intSmartformParticipantID","INNER JOIN");
               strSetSF = "bio-sf";
           }

           querySF.setDomain("SMARTFORM", "SMARTFORM_intSmartformID", "SMARTFORMPARTICIPANTS_intSmartformID", "INNER JOIN");           
           querySF.setDomain("SMARTFORMTODATAELEMENTS", "SMARTFORM_intSmartformID", "SMARTFORMTODATAELEMENTS_intSmartformID", "INNER JOIN"); 
           querySF.setDomain("DATAELEMENTS", "SMARTFORMTODATAELEMENTS_intDataElementID", "DATAELEMENTS_intDataElementID","INNER JOIN");           

           // If the first query in executeSearchforMissingSFResults is being performed, left join on smartform results
           if (blquery1 == true)
           {
               querySF.setDomain("SMARTFORMRESULTS", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "SMARTFORMRESULTS_intSmartformParticipantID", "LEFT JOIN");           
           }
           else
           {
               querySF.setDomain("SMARTFORMRESULTS", "SMARTFORMPARTICIPANTS_intSmartformParticipantID", "SMARTFORMRESULTS_intSmartformParticipantID", "INNER JOIN");           
           }    

           // Set the formfields for DISTINCT conditions           
           String strMissingSFDomain = vtMissingSFDomain.get(0).toString();
           String strDistinct = (String)hashPrimaryKey.get( strMissingSFDomain );            
           // Clear the formfields
           vtSearchFormFields.clear();
           vtSearchFormFields.add(strDistinct);
           
           // Add the selected formfields only in the first query
           if (blquery1 == true)
           {    
               // Build the form fields from all the fields selected in criteriadomains - only schema domains
               // And also the dataelements selected for the smartforms
               vtSelectedDataelements.clear();
	       vtSelectedSFDataelements.clear();
               
               Enumeration e = hashCriteriaDomains.keys();         
               while(e.hasMoreElements()){

                    String strCriteriaDomain = (String) e.nextElement();
                    // Add the selected fields only for the domain for which we are
                    // searching for missing records
                    if (strCriteriaDomain.equals(strMissingSFDomain))
                    {    
                        Vector vtSelectedFields = (Vector)hashCriteriaDomains.get(strCriteriaDomain);

                        // if it is a domain name
                        if( vtSelectedDomains.contains(strCriteriaDomain)){
                            for( int i=0; i < vtSelectedFields.size(); i++ ){                                   

                                // Set the selected field to formfield, to be used in search query
                                vtSearchFormFields.add((String)vtSelectedFields.get(i));                        
                            }                    
                        }
                    }  
                }
           }  
          
                    
           // Check for formfields - if it is not empty proceed
           if( vtSearchFormFields.size() > 0){
               
               // Set the form fields
               querySF.setFields(vtSearchFormFields, null);
               
               //querySF.setDistinctField("SMARTFORMPARTICIPANTS_intParticipantID"); 
               querySF.setDistinctField( strDistinct );  
               
               // Set the smartform name to the one selected (highlighted) in the dataset area
               querySF.setWhere( null, 0, "SMARTFORM_strSmartformName", "=", (String)vtMissingSF.get(0), 0, DALQuery.WHERE_HAS_VALUE );
               
               // Do the rest, only when performing the second query in executeSearchForMissingSFRecords
               if (blquery1 != true)
               {    
                   
                    // to get the dataelement name              
                    querySF.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", "SMARTFORMRESULTS_intDataElementID", 0, DALQuery.WHERE_BOTH_FIELDS);
                    querySF.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", (String)vtMissingSFField.get(0), 0, DALQuery.WHERE_HAS_VALUE);

                    // set the deleted fields
                    String name = strMissingSFDomain;                   
                    name = name+"_intDeleted";
                    querySF.setWhere( "AND", 0, name, "=", "0", 0, DALQuery.WHERE_HAS_VALUE );

                    querySF.setWhere( "AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    querySF.setWhere( "AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    querySF.setWhere( "AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    querySF.setWhere( "AND", 0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    querySF.setWhere( "AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );           

                    if( strSetSF.equals("pat-sf")){
                        querySF.setWhere( "AND", 0, "PATIENT_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    }else if( strSetSF.equals("bio-sf") ){
                        querySF.setWhere( "AND", 0, "BIOSPECIMEN_SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE );
                    }
               
                 }
           }    
           

           
        }catch(Exception e){  
            LogService.instance().log(LogService.ERROR, "Unknown error in ISearch Channel - " + e.toString(), e);           
        } 
       
       return querySF;       
        
    }
    
    
    /** Method: lookupRecord
     *  Description: Returns a vector holding the result from a search query
     *  Parameters:  ResultSet rsResultSet - the result set from the query
     *               Vector vtFields - the search fields
     *               blSeparateSF - whether to separate results from different smartforms, that is
     *                            - a new entry into the vector based on a change in the
     *                            - smartform partcipant key
     *            int intStartRow - the starting record to cache in the vector
     *            int intStartRow - the last record to cache in the vector
     *           boolean intExport - is this function called during an export function
     *                             - 0 - no export build the cache vector, 1 - normal export, 2 - export containing smartform data
     *  Return : A vector containing the result
     *
     **/    
    private Vector lookUpRecords (ResultSet rsResultSet, Vector vtFields, boolean blSeparateSF, int intStartRow, int intEndRow, int intExport) throws Exception {
        Vector vtResult = new Vector(100, 50);        
        
        int intNoOfFields = vtFields.size();
        int intDataElementLocation = 0;
        String strCurrentPartKey="";
        String strCurrentStudyKey="";
        String strCurrentId="";
        String strFieldName;
        String strFieldValue;

        String strIdentity1 = "";

        // if PAT & BIO is selected
        if( vtSelectedDomains.contains("PATIENT") && vtSelectedDomains.contains("BIOSPECIMEN") ){
             strIdentity1 = "BIOSPECIMEN_intBiospecimenID";
        }
        // if only PAT is selected
        else if( vtSelectedDomains.contains("PATIENT")){
            strIdentity1 = "PATIENT_intInternalPatientID";
        }
        // if only BIO is selected
        else if( vtSelectedDomains.contains("BIOSPECIMEN") ){
            strIdentity1 = "BIOSPECIMEN_intBiospecimenID";
        }
        else if( vtSelectedDomains.contains("ADMISSIONS") ){
            strIdentity1 = "ADMISSIONS_intAdmissionkey";
        }        
        // if only study is selected, and no PAT/ BIO
        else if( vtSelectedStudys.isEmpty() == false){
            strIdentity1 = "STUDY_intStudyID";
        }
        
        
        // Initialise the total number of rows
        intTotalResultRows = 0;
        
        try {
            
            while (rsResultSet.next()) {
                Hashtable hashRowResult = new Hashtable(10);
                
                int row = rsResultSet.getRow();
                // For each row get the correspoding row number from the resultset
                hashRowResult.put("Row Number", Integer.toString(row) );
                
                // Put the row result into a hash table
                for (int i=0; i < intNoOfFields; i++) {                    
                    strFieldName = (String) vtFields.get(i);
                    String colValue = rsResultSet.getString(i + 1);
                    // if a result is not null in the row for the field
                    // put it in the hashtabele
                    // if the result is null, put it as "Empty" in the hashtable
                    if (colValue != null){                        
                        // Get the details of the field from the database schema
                        DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
                        // if date type convert the date for display
                        if (currentField.getDataType() == DBMSTypes.DATE_TYPE){
                            colValue = Utilities.convertDateForDisplay(colValue);
                        }    
                       	if (currentField.getDataType() == DBMSTypes.STR_TYPE) {
			    colValue = "\"" + colValue + "\"";
			}	 
                        hashRowResult.put(strFieldName, colValue);
                        
                        // Get the location where the dataelement information starts
                        if ( strFieldName.equals("DATAELEMENTS_intDataElementID") )
                        {
                            intDataElementLocation = i;
                        }    
                    }
                    else{
                        hashRowResult.put(strFieldName, "Empty");
                    }    
                }    
                  
                // If this row contains smartform results
                if ( hashRowResult.containsKey("DATAELEMENTS_intDataElementID") )
                {
                    // Prepend the smartform name to the dataelement ID, this is to allow for different
                    // smartforms having the same dataelements
                    String DEId = (String) hashRowResult.get("SMARTFORM_strSmartformName") + "__" +  (String) hashRowResult.get("DATAELEMENTS_intDataElementID");
                    String DEResult = (String) hashRowResult.get("SMARTFORMRESULTS_strDataElementResult");
                    hashRowResult.put (DEId, DEResult);
                    
                    // Get the unique ID by which to distinguish whether to create a new entry in the vector
                    // Get the id value, this may be the patient, biospecimen or study key
                    if (!strIdentity1.equals(""))                        
                    strCurrentId = (String) hashRowResult.get(strIdentity1);

                    // Get the current participant key
                    strCurrentPartKey = (String) hashRowResult.get("SMARTFORMPARTICIPANTS_intSmartformParticipantID");   
                    
                    
                    if (vtFields.contains("STUDY_intStudyID"))
                    {
                        strCurrentStudyKey = (String) hashRowResult.get("STUDY_intStudyID");
                    }    
                
                }
                
                
                // if there are more dataelements for display
                // decide whether a new entry in the vector (a new row of data)
                // or add the dataelement to the same hash table (same row of data)

                // add all the dataelements to the same hashtable
                // if we are on the same smartform participant key
                // also if we are on a different smartform participant key but
                // all smartform results are to be displayed on the same line.
                if (vtSelectedDataelements.size() > 0)
                {    
                    for (  int i=1; i<vtSelectedDataelements.size(); i++ )
                    {    
                        if (rsResultSet.next())
                        {    
                            Hashtable hashNewRow = new Hashtable(10);
                            
                            int intNewRow = rsResultSet.getRow();
                            // For each row get the correspoding row number from the resultset
                            hashNewRow.put("Row", Integer.toString(intNewRow));
                            for (int k=0; k < intNoOfFields; k++) {                    
                                strFieldName = (String) vtFields.get(k);
                                String colValue = rsResultSet.getString(k + 1);
                                // if a result is not null in the row for the field
                                // put it in the hashtabele
                                // if the result is null, put it as "Empty" in the hashtable
                                if (colValue != null){                        
                                    // Get the details of the field from the database schema
                                    DBField currentField = (DBField) DatabaseSchema.getFields().get(strFieldName);
                                    // if date type convert the date for display
                                    if (currentField.getDataType() == DBMSTypes.DATE_TYPE){
                                        colValue = Utilities.convertDateForDisplay(colValue);
                                    }    

                                    hashNewRow.put(strFieldName, colValue);

                                }
                                else{
                                    hashNewRow.put(strFieldName, "Empty");
                                }                               
                            }        
                            
                            if (!hashNewRow.get("SMARTFORMPARTICIPANTS_intSmartformParticipantID").equals(strCurrentPartKey) && blSeparateSF == true)
                            {
                                // New smartform data on a different row
                                // Go back to previous, otherwise this result will be missed in the while loop
                                rsResultSet.previous();
                                // break out of the loop
                                break;
                            }    
                            else
                            {    
                                // Check that the dataelement results are for the same id (patient, biospecimen or study)
                                if (hashNewRow.get(strIdentity1).equals(strCurrentId))
                                {    
                                    boolean blSameRow = true;

                                    // If studies have been added to the domain, check if the next row has the same study id
                                    // as the current row
                                    if ((vtFields.contains("STUDY_intStudyID")) && (hashNewRow.get("STUDY_intStudyID").equals(strCurrentStudyKey)))
                                    {    
                                        blSameRow = true;
                                    }
                                    else
                                    {
                                        // if we are searching based on a study and the new row's study key is different
                                        // the data should go as a new entry in the vector
                                        if (vtFields.contains("STUDY_intStudyID"))
                                        {                                            
                                            blSameRow = false;
                                            // Go back to previous, otherwise this result will be missed in the while loop
                                            rsResultSet.previous();
                                            // break out of the loop
                                            break;
                                        }    
                                    }
                                    
                                    if (blSameRow == true)
                                    {    
                                        for (int j=intDataElementLocation; j<intNoOfFields; j++ )
                                        {
                                            strFieldName = (String) vtFields.get(j);
 
                                            hashRowResult.put(strFieldName, hashNewRow.get(strFieldName) );
                                            // in the hashtable put the dataelement result value next to the dataelement ID                        
                                            if ( strFieldName.equals("DATAELEMENTS_intDataElementID") )
                                            {
                                                // Prepend the smartform name to the dataelement ID, this is to allow for different
                                                // smartforms having the same dataelements
                                                String DEId = (String) hashNewRow.get("SMARTFORM_strSmartformName") + "__" + (String) hashNewRow.get("DATAELEMENTS_intDataElementID");
                                                String DEResult = (String) hashNewRow.get("SMARTFORMRESULTS_strDataElementResult");
                                                // Don't overwrite data, put in a new row
                                                if (!hashRowResult.containsKey(DEId))
                                                {    
                                                    hashRowResult.put (DEId, DEResult);
                                                }
                                                else
                                                {
                                                    // Go back to previous, otherwise this result will be missed in the while loop
                                                    rsResultSet.previous();
                                                    // break out of the loop
                                                    break;
                                                }    
                                            }
                                        }

                                    }
                                    
                                }
                                else
                                {
                                    // Go back to previous, otherwise this result will be missed in the while loop
                                    rsResultSet.previous();
                                    // break out of the loop
                                    break;
                                }

                            }

                            strCurrentPartKey = (String) hashNewRow.get("SMARTFORMPARTICIPANTS_intSmartformParticipantID");
                            if (vtFields.contains("STUDY_intStudyID"))
                            {    
                                strCurrentStudyKey = (String) hashNewRow.get("STUDY_intStudyID");
                            }    
                        }    
                    }  
                }
               
                
                // check for any bio-analysis criteria before adding to the vector
                if (bioAnalysisCriteriaMatch (hashRowResult))
                {    
                    // if in the process of an export
                    // write the row data to the export file
                    // otherwise build the vector consisting of the rows to cache
                    if (intExport != 0)
                    {
                       if (intExport == EXPORT_SMARTFORM)
                       {
                           // write the smartform row data into export file
                           writeExportDataForSmartform(hashRowResult);
                       }
                       else
                       {
                           // write the row data into export file
                           writeExportData (hashRowResult);
                       }    
                       intTotalResultRows ++;
                    }
                    else
                    {    
                        // The first time Perform search is hit, store the first 50 pages of results
                        // in the vector
                        // For the rest of the results keep the starting row number of the resultset in the vector entry
                        if (intTotalResultRows >= intStartRow && intTotalResultRows < intEndRow)
                        {                    
                            vtResult.add(hashRowResult);
                            intTotalResultRows ++;
                        } 
                        else
                        {
                            intTotalResultRows ++;
                        }
                    }
                }
            }
            
            intFirstDataRow = intStartRow;
            intLastDataRow = intEndRow;
        }
        catch (Exception e) {
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in ISearch:LookUpRecords - " + e.toString(), e);
            throw new Exception("Unknown error in ISearch:LookUpRecords - " + e.toString());
        }
        
        return vtResult;
    }
    
    private void writeExportData (Hashtable hashRow)
    {
        String line = "";
        
        try
        {
            for (int j=0; j < vtSearchFormFields.size(); j++)
            { 
                if( !hashNonDisplayFields.contains(vtSearchFormFields.get(j).toString()) )
                {
                    String data = hashRow.get((String) vtSearchFormFields.get(j)).toString();                
                        if ( (data == null) || (data.equals("Empty")) )
                            data = "";

                    data = Utilities.cleanForExcelFile(data);
                    line += data + strDelimiter;
                }
            }    

            ExportFile.write(line, 0, line.length() - 1);
            ExportFile.write("\n");

        }
        catch(Exception e)
        {
            //ExportFile.close();
            LogService.instance().log(LogService.ERROR, "Unknown error in Advanced Search Channel - " + e.toString(), e);
        }
    }
    
    private void writeExportDataForSmartform (Hashtable hashRow)
    {
        String line = "";
        
        try
        {

            // Build all the non-smartform data
            for (int j=1; j < intSmartformColumn; j++)
            { 
                if( !hashNonDisplayFields.contains(vtSearchFormFields.get(j).toString()) )
                {

                    String value = hashRow.get((String) vtSearchFormFields.get(j)).toString();                
                        if ( (value == null) || (value.equals("Empty")) )
                            value = "";

                    String data = Utilities.cleanForExcelFile(value);                    
                    line += data + strDelimiter;
                }
            }    

            // Build all the smartform result data
            // If there is no value for a particular dataelement for a particular entry (hashtable) in the vector
            // build an empty result
            for( int k = 0; k < vtExportDataElements.size(); k++ )
            {
                String value;
                if (hashRow.get((String) vtExportDataElements.get(k)) == null )
                {
                    value = "";
                }
                else
                {
                    value = hashRow.get((String) vtExportDataElements.get(k)).toString();                
                    if (value.equals("Empty"))
                        value = "";                    
                }    

                String data = Utilities.cleanForExcelFile(value);                    
                line += data + strDelimiter;
            }    

            ExportFile.write(line, 0, line.length() - 1);
            ExportFile.write("\n");

        }
        catch(Exception e)
        {
            //ExportFile.close();
            LogService.instance().log(LogService.ERROR, "Unknown error in Advanced Search Channel - " + e.toString(), e);
        }
    }

    
    // Check the specified Bio-analysis smartform criteria
    // If it matches, return true, which means that the row will be added
    // to the vector
    // If the row has the dataelement specified in the criteria, but
    // it does not match any of the criteria answers, return false
    // which means that this row will not be added to the vector
    private boolean bioAnalysisCriteriaMatch (Hashtable hashRow)
    {
        boolean blcriteriaMatch = false;
        boolean blRowContainsDE = false;
        
        
        if ( hashBioAnalysisSFCriteria.isEmpty() == true  )
        {
            // there is no bio-analysis criteria, therefore return true
            blcriteriaMatch= true;
        }    
        else
        {
            Enumeration e = hashBioAnalysisSFCriteria.keys();          
            while(e.hasMoreElements()){

                String strBioAnalysisQuestion = (String) e.nextElement();           

                if (hashRow.containsKey(strBioAnalysisQuestion))
                {
                   blRowContainsDE = true;
                   Vector vtAnswer = (Vector) hashBioAnalysisSFCriteria.get(strBioAnalysisQuestion);
                   for (int i=0; i<vtAnswer.size(); i++)
                   {
                     // if one of the possible answers is matched, return true
                     
                     String strBioAnswer = hashRow.get(strBioAnalysisQuestion).toString().toLowerCase();
                     
                     if ( strBioAnswer.indexOf(vtAnswer.get(i).toString().toLowerCase()) != -1 )                     
                     {
                         blcriteriaMatch = true;
                         break;
                     }
                   }    

                }   
            }    
        }    
     
        
        // if the row doesn't have any of the dataelements we are looking for
        // to match bio-analysis criteria, return true because we still want to add
        // it to the vector
        if (blRowContainsDE == false)
        {
            blcriteriaMatch = true;        
        }

        return blcriteriaMatch; 
    }
    
        
    // Gets the DOMAIN Names for display
    // The is to cater for different domain names from those specified in the DBSchema as required by the client 
    // The name for display will be specified in the SearchDBSchema using DOMAIN_strDisplayName
    private String getDisplayDomainNameXML ()
    {
        String strDisplayDomainNameXML = "";
        Hashtable hashSearchDomains = DatabaseSchema.getSearchDomains();
        Enumeration domainIterator = hashSearchDomains.keys();
            
        while(domainIterator.hasMoreElements()){

            String strCurrentDomain = (String) domainIterator.nextElement();            

            String strDisplayName = (String) hashDomainNames.get(strCurrentDomain);
            
            // If an alternate display name is specified use this name, otherwise use the
            // actual domain name
            if (strDisplayName != null)
            {
                strDisplayDomainNameXML += "<" + strCurrentDomain + "_DisplayName>" + strDisplayName + "</" + strCurrentDomain + "_DisplayName>";
            }
            else 
            {
                strDisplayDomainNameXML += "<" + strCurrentDomain + "_DisplayName>" + strCurrentDomain + "</" + strCurrentDomain + "_DisplayName>";
            }    
             
        }
        
        return strDisplayDomainNameXML;

    }
    
    // This method modifies the XML to be displayed
    // when a particular mode is selected.
    // For example, when going from
    // advanced to simple mode, collapse
    // all the areas that do not have a criterion
    // as well as the display fields
    private String modifyXMLForMode (String strInputXML, String strCurrentMode, String strNewMode)
    {
        String something ="";
        
        if ( (strCurrentMode.equals("Advanced")) && (strNewMode.equals("Normal") || strNewMode.equals("Simplfied")) )
        {
           
        
        }
        
        if (strNewMode.equals("Advanced"))
        {
        
        }
        
        return something;
    }

    // What rows need to be retrieved from the seach results vector
    private void getRequiredRows ()
    {
        // If the new current page is not within the cached range
        // recalulate
        if ((intCurrentPage > intMaxCachePage) || (intCurrentPage <= intMinCachePage))
        {
            intMinCachePage = ((intCurrentPage-1) - (CACHE_PAGE_SIZE/2));
            intMaxCachePage = ((intCurrentPage-1) + (CACHE_PAGE_SIZE/2));
        }    
    
        if (intMinCachePage  < 0)
        {
            intMinCachePage = 0;
        }      
        
        intRequiredStartRow = intMinCachePage * intRecordPerPage;
        intRequiredEndRow = intMaxCachePage * intRecordPerPage;

        if (intRequiredEndRow > intTotalResultRows)
        {
            intRequiredEndRow = intTotalResultRows;
        }

    }

    // What rows need to be retrieved from the seach results vector
    private void getRequiredRowsPageSet ()
    {
        
        intMaxCachePage = (intCurrentPage) + (CACHE_PAGE_SIZE/2);
        intMinCachePage = (intCurrentPage) - (CACHE_PAGE_SIZE/2);
        
        if (intMinCachePage  < 0)
        {
            intMinCachePage = 0;
        }
        
        intRequiredStartRow = intMinCachePage * intRecordPerPage;
        intRequiredEndRow = intMaxCachePage * intRecordPerPage;
        
        if (intRequiredEndRow > intTotalResultRows)
        {
            intRequiredEndRow = intTotalResultRows;
        }
        
        // Force a re-execution of the query even if the data required for display
        // is within the previously cached pages
        blPerformSearch = true;
        
    }


}    
