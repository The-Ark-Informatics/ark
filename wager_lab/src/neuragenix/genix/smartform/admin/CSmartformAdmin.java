
/**
 * CSmartformAdmin.java
 * Copyright (C) 2005 Neuragenix Pty Ltd.
 * Date: 19/01/2003
 *
 * $Id $
 *
 */

package neuragenix.genix.smartform.admin;


/**
 * Smartform Admin
 * @author <a href="mailto:abalraj@neuragenix.com">Anita Balraj</a>
 * Description: To define smartforms, dataelements, options
 *
 */

// java packages
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.util.StringTokenizer;
import java.io.*;
import java.sql.*;
 import java.lang.Math.*;
 import java.text.*;
 //import java.util.*;

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

import javax.naming.Context;
import javax.naming.NamingException;
import javax.naming.NotContextException;

/** */
public class CSmartformAdmin implements IChannel

{    
    // pages (strSylesheets)
    private final String SEARCH_SMARTFORM = "search_smartform";
    private final String ADD_SMARTFORM = "add_smartform"; 
    private final String VIEW_SMARTFORM = "view_smartform";   
    private final String BUILD_SMARTFORM = "build_smartform";
    private final String ADDEDIT_DATAELEMENTS = "addedit_dataelements";
    private final String ADD_DATAELEMENTPOOL = "add_dataelementpool";
    private final String VIEW_DATAELEMENTPOOL = "view_dataelementpool";
    private final String ADDEDIT_OPTIONS = "addedit_options";
    private final String ADD_OPTIONPOOL = "add_optionpool";
    private final String VIEW_OPTIONPOOL = "view_optionpool";
    private final String SECURITY = "security";
    
    // search activities (strActivity)
    private final String DO_SMARTFORM_ADMIN = "do_smartform_admin";
    private final String SMARTFORM_ADD = "smartform_admin_add";
    private final String SMARTFORM_VIEW = "smartform_admin_view";
    private final String SMARTFORM_INSERT = "smartform_admin_insert";
    private final String SMARTFORM_DELETE = "smartform_admin_delete";
    private final String SMARTFORM_UPDATE = "smartform_admin_update";
    private final String SMARTFORM_SEARCH = "smartform_admin_search";
    private final String SMARTFORM_BUILD = "smartform_build";
    private final String SMARTFORM_ADDEDIT_DE = "smartform_addedit_de";
    private final String SMARTFORM_DEPOOL_INSERT = "smartform_depool_insert";
    private final String SMARTFORM_DEPOOL_UPDATE = "smartform_depool_update";
    private final String SMARTFORM_DEPOOL_DELETE = "smartform_depool_delete"; 
    private final String SMARTFORM_ADDEDIT_OPTIONS = "smartform_addedit_options";
    private final String SMARTFORM_OPTIONPOOL_INSERT = "smartform_optionpool_insert";
    private final String SMARTFORM_OPTIONPOOL_UPDATE = "smartform_optionpool_update";
    private final String SMARTFORM_OPTIONPOOL_DELETE = "smartform_optionpool_delete";
   
    
   
    // channel's runtime & static data
    private ChannelStaticData staticData;
    private ChannelRuntimeData runtimeData;
    private ChannelRuntimeData oldRuntimeData;
    
    private AuthToken authToken ;
    private String strSessionUniqueID;
    private String strActivity;
    private String strCurrent;
    private String strXML = "";
    private String strPreviousXML = "";
    private String strStylesheet;
    private boolean firstTime = false;
    private String strErrorMessage = "";
    private String strCurrentNode = "";
    private String strSmartformName;
    private String strSmartformID;
    private String strDisplay = "";
    private String strMode = "null";
    private String strDataElementID = "";
    private String strOptionPoolSel = "";
    private String strOptionIdSel = "";
    private String strFlag = "";
    private String strRadioVal = "";
    private boolean blFirst = false;
    //Study---rennypv
    private String strBackButton = null;
    
   // private int intCurrentDataElementID;
    
     
    /*private Vector vtSearchSmartForm = new Vector(10, 5);
    private Vector vtAddSmartForm = new Vector(10, 5);
    private Vector vtViewSmartForm = new Vector(10,5); */
    
   
    /** Lock request object to handle record locking
     */
    private LockRequest lockRequest = null;
    private LockRequest lockRequestDEPool = null;
    private LockRequest lockRequestOptionPool = null;
    
    /** Channels don't need to clear lock
     */
    private Hashtable hashChannelsNotToClearLock = null;

    private static boolean bldeleteResults = false;
    static
    {
        try
        {
        	bldeleteResults = PropertiesManager.getPropertyAsBoolean("neuragenix.genix.smartform.admin.deleteResults");
        }
        catch (Exception e)
        {
            System.err.println ("[CSmartformAdmin - Unable to read property : neuragenix.genix.smartform.admin.deleteResults. Setting it to false");
            bldeleteResults = false;
        }
    }
    
    /** Creates a new instance of CSmartformAdmin
     */
    public CSmartformAdmin()
    {
        this.strStylesheet = SEARCH_SMARTFORM;
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

    /**  Process layout-level events coming from the portal.
     *  Satisfies implementation of IChannel Interface.
     * @param ev ev a portal layout event
     */
    public void receiveEvent(PortalEvent ev)
    {
        // If the user logout, destroy lock request object
        if (ev.getEventNumber() == PortalEvent.SESSION_DONE)
            clearLockRequest();
            //*******
            clearLockRequestDEPool();
            clearLockRequestOptionPool();
            //*******
    }

    /**  Receive static channel data from the portal.
     *  Satisfies implementation of IChannel Interface.
     * @param sd sd static channel data
     */
    public void setStaticData(ChannelStaticData sd)
    {
        firstTime = true;
        this.staticData = sd;
        this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");
        strSessionUniqueID = authToken.getSessionUniqueID();
        
        // create domains & formfields for this channel
        InputStream file;
        
        file = CSmartformAdmin.class.getResourceAsStream("SmartformAdminLookupObjects.xml");
        DatabaseSchema.loadLookupObjects(file, "SmartformAdminLookupObjects.xml"); 
        
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
        
        if(PropertiesManager.getProperty("solution.type").equals("Biogenix"))
        {
            try 
            {
                
                //System.err.println("---------session unique id---------" + authToken.getSessionUniqueID() + " " + (String) globalIDContext.lookup("Study"));
                SessionManager.addSession(strSessionUniqueID);
                SessionManager.addChannelID(strSessionUniqueID, "CStudy",
                                                    (String) globalIDContext.lookup("CStudy"));        
            }
            catch (NotContextException nce)
            {
                LogService.log(LogService.ERROR, "Could not find channel ID for fname=Study");
            } 
            catch (NamingException e) 
            {
                LogService.log(LogService.ERROR, e);
            }
        }
        
        SessionManager.addLockRequestSession(strSessionUniqueID); 

        // instantiate the hashChannelNotToClearLock
        hashChannelsNotToClearLock = SessionManager.getSharedChannels(authToken, "CSmartformAdmin"); 
    }
    
    /**  Receive channel runtime data from the portal.
     *  Satisfies implementation of IChannel Interface.
     * @param rd rd handle to channel runtime data
     */
    public void setRuntimeData(ChannelRuntimeData rd)
    {
        try
        {   
            this.runtimeData = rd;
            strErrorMessage = "";

            //System.err.println( "runtimeData; " + runtimeData.toString() );
            //Utilities.printRuntimeData(rd);
            System.out.println();
            // Get the current page the users is on
            if(runtimeData.getParameter("current") != null)
            {                 
                firstTime = false;
                strCurrent = runtimeData.getParameter("current");
            }
            else if (firstTime)
            {
                SessionManager.clearLockRequests(strSessionUniqueID, hashChannelsNotToClearLock);
                //LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
                firstTime = false;
                strCurrent = SEARCH_SMARTFORM;
                strDisplay = "";
                
                // Start lock        
                // create new lock request
                lockRequest = new LockRequest(authToken);
                // register the lock to session manager
                SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartformAdmin", lockRequest);
                lockRequestDEPool = new LockRequest(authToken);
                // register the lock to session manager
                SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartformAdmin", lockRequestDEPool);
                lockRequestOptionPool = new LockRequest(authToken);
                // register the lock to session manager
                SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartformAdmin", lockRequestOptionPool);
                // End lock
                
            }            
            else
            {
                SessionManager.clearLockRequests(strSessionUniqueID, hashChannelsNotToClearLock);
                //LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
                strCurrent = null;
            }
            
            // if the user doesn't have permission to do search
            // display unauthorization message
            if (!authToken.hasActivity(DO_SMARTFORM_ADMIN))
            {
                strXML  = buildSecurityErrorXMLFile("Smartform Admin");
                strStylesheet = SECURITY;
                return;
            }
            
            if (strCurrent != null)                
            {           
                if (strCurrent.equals(SEARCH_SMARTFORM))
                {                
                    
                    // display unauthorization message
                    if (!authToken.hasActivity(SMARTFORM_SEARCH))
                    {
                        strXML  = buildSecurityErrorXMLFile("search smartform");
                        strStylesheet = SECURITY;
                        return;
                    }
                    
                    // To build the search form labels and fields
                    buildSearchSmartForm();                   
                    
                    //To check the actions performed in Smartform
                    checkSmartformActions();
                    
                    
                    
                }
                else if (strCurrent.equals(ADD_SMARTFORM))
                {  
                    
                    checkSmartformActions();              
                    
                    
                }
                else if (strCurrent.equals(VIEW_SMARTFORM))
                {
                   
                    checkSmartformActions();                    
                    
                }
                else if (strCurrent.equals(BUILD_SMARTFORM))
                {                   

                    // In Build Smartform, user clicks on back
                    if (runtimeData.getParameter("backtoViewSmartform") != null)
                    {
                        //lockRequest.unlock();
                        //clearLockRequest();
                        displaySmartformDetails();
                    }

                   // In Build Smartform, user clicks on save
                    else if (runtimeData.getParameter("saveBuildSmartForm") != null)
                    {
                        
                        // display unauthorization message
                        if (!authToken.hasActivity(SMARTFORM_BUILD))
                        {
                            strXML  = buildSecurityErrorXMLFile("save BuildSmartform");
                            strStylesheet = SECURITY;
                            return;
                        }

                        
                        //buildSmartForm();
                        saveBuildSmartForm();      

                    }

                    // In Build Smartform, user clicks on move up
                    else if(runtimeData.getParameter("moveUp") != null)
                    {
                        // display unauthorization message
                        if (!authToken.hasActivity(SMARTFORM_BUILD))
                        {
                            strXML  = buildSecurityErrorXMLFile("move Smartforms DataElements");
                            strStylesheet = SECURITY;
                            return;
                        }
                        
                        ChangeDEOrder(runtimeData.getParameter("SMARTFORM_intSmartformID"), runtimeData.getParameter("SMARTFORMTODATAELEMENTS_DEIdSelected"),"moveUp");
                        
                    }

                    // In Build Smartform, user clicks on move up
                    else if(runtimeData.getParameter("moveDown") != null)
                    {
                        // display unauthorization message
                        if (!authToken.hasActivity(SMARTFORM_BUILD))
                        {
                            strXML  = buildSecurityErrorXMLFile("move Smartforms DataElements");
                            strStylesheet = SECURITY;
                            return;
                        }
                        
                        ChangeDEOrder(runtimeData.getParameter("SMARTFORM_intSmartformID"), runtimeData.getParameter("SMARTFORMTODATAELEMENTS_DEIdSelected"),"moveDown");

                    } 
                    
                    else if(runtimeData.getParameter("deleteSFDE") != null)
                    {
                        // display unauthorization message
                        if (!authToken.hasActivity(SMARTFORM_BUILD))
                        {
                            strXML  = buildSecurityErrorXMLFile("delete Smartforms DataElements");
                            strStylesheet = SECURITY;
                            return;
                        }
                        
                        deleteSmartformsDataElement();
                    }
                    
                    else if(runtimeData.getParameter("AddEditDataElements") != null)
                    {
                        blFirst = true;
                       //System.out.println("Add edit dataelements clicked"); 
                       buildAddEditDataElements();                       
                       strCurrent = ADDEDIT_DATAELEMENTS;   
                    }
                    
                     else 
                     {
                        buildSmartForm();
                     }                 
                    
                    checkSmartformActions();
                }
                else if (strCurrent.equals(ADDEDIT_DATAELEMENTS))
                {                    
                    checkSmartformActions();
                }
                // Actions in Add DataElement Pool 
                else if (strCurrent.equals(ADD_DATAELEMENTPOOL))
                {           
                     // In Add DataElement pool, user clicks on back
                    if (runtimeData.getParameter("backtoAddEditDataElements") != null)
                    {                     
                        strCurrent = ADDEDIT_DATAELEMENTS;
                        strStylesheet = ADDEDIT_DATAELEMENTS;
                         
                    }
                    // if user clicks on save new DataElement Pool
                    else  if (runtimeData.getParameter("saveNewDataElementPool") != null)
                    {                       
                        saveNewDataElementPool();   
                        
                        
                    } 
                    //user clicks on Add DataElement Pool
                    else
                    {
                        displayAddDataElementPool();   
                    }
                        
                    checkSmartformActions();
                    
                }
                // Actions in View DataElement Pool 
                else if (strCurrent.equals(VIEW_DATAELEMENTPOOL))
                {           
                     // In Edit DataElement pool, user clicks on back
                    if (runtimeData.getParameter("backtoAddEditDataElements") != null)
                    {                      
                        strCurrent = ADDEDIT_DATAELEMENTS;
                        strStylesheet = ADDEDIT_DATAELEMENTS;                       
                         
                    }
                    // if user clicks on save DataElement Pool
                    else  if (runtimeData.getParameter("updateDataElementPool") != null)
                    {                       
                        doUpdateDataElementPool();   
                        
                        // These values are required to access Add/Edit DataElements page, if back button is clicked
                        strXML+= "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                                 "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                                 "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";
               
                    } 
                    // if user clicks on delete DataElement Pool
                    else  if (runtimeData.getParameter("deleteDataElementPool") != null)
                    {                       
                        doDeleteDataElementPool();                 
                        
                    } 
                    // user clicks on Edit DataElement Pool in Add/Edit DataElements
                    else
                    {
                        displayDataElementPoolDetails();  
                         // These values are required to access Add/Edit DataElements page, if back button is clicked
                         strXML+= "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                                 "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                                 "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";

                    }
                        
                    checkSmartformActions();
                    
                }
                //User clicks on the link Add/Edit Options
                 else if (strCurrent.equals(ADDEDIT_OPTIONS))
                {              
                    //System.out.println("inside Add/Edit options");

                     // User clicks on back button
                    if (runtimeData.getParameter("backtoAddEditDataElements") != null)
                    {     
                        lockRequestOptionPool.unlock();
                        clearLockRequestOptionPool();
                        
                        strCurrent = ADDEDIT_DATAELEMENTS;
                        strStylesheet = ADDEDIT_DATAELEMENTS;  
                    }
                    
                    
                    // User clicks on Save to save Option
                    else if (runtimeData.getParameter("saveOption") != null)
                    {    
                        //System.out.println("clicked on save");
                         saveNewOption(); 
                         //buildAddEditOptions();
                    }
                    
                    // User clicks on Update to update Option
                    else if (runtimeData.getParameter("updateOption") != null)
                    {    
                        //System.out.println("clicked on update");
                         doUpdateOption(); 
                         //buildAddEditOptions();
                    }
                    
                    // In Add/Edit options, user clicks on move up
                    else if(runtimeData.getParameter("moveUp") != null)
                    {
                       //System.out.println("move up is clicked");
                        ChangeOptionsOrderinAddEditOptions(runtimeData.getParameter("OPTIONS_intOptionID"),"moveUp");
                        
                    }
                    
                    // In Add/Edit options, user clicks on move down
                    else if(runtimeData.getParameter("moveDown") != null)
                    {
                       
                        ChangeOptionsOrderinAddEditOptions(runtimeData.getParameter("OPTIONS_intOptionID"),"moveDown");
                        
                    }
                    
                    // In Add/Edit options, user clicks on move down
                    else if(runtimeData.getParameter("deleteOption") != null)
                    {
                       
                       deleteOptioninAddEditOption();
                        
                    }
                    
                    // In Add/Edit options, user clicks on Link to DataElement
                    else if(runtimeData.getParameter("LinkToDataElement") != null)
                    {
                       
                       linkOptionPoolToDataElement();
                        
                    }
                    
                    else
                    {
                        // To display the Add/Edit Options Page
                        buildAddEditOptions();     
                    }              
            
                    
                  checkSmartformActions();
                  
                }
                // Actions in Add Option Pool 
                else if (strCurrent.equals(ADD_OPTIONPOOL))
                { 
                    strFlag = "";
                    // User clicks on back button
                    if (runtimeData.getParameter("backtoAddEditOptions") != null)
                    {    
                         //runtimeData.setParameter("OptionPoolSelected",new Integer(intCurrentOptionPoolID).toString());
                
                        buildAddEditOptions();
                        strCurrent = ADDEDIT_OPTIONS;
                        strStylesheet = ADDEDIT_OPTIONS;
                        
                    }
                    // if user clicks on save new Option Pool
                    else  if (runtimeData.getParameter("saveNewOptionPool") != null)
                    {                       
                        saveNewOptionPool();                
                    } 
                    else
                    {
                        displayAddOptionPool();
                    }
                    checkSmartformActions();
                    
                }
                
                // Actions in View Option Pool 
                else if (strCurrent.equals(VIEW_OPTIONPOOL))
                {          
                    strFlag = "";
                     // In Edit Option pool, user clicks on back
                    if (runtimeData.getParameter("backtoAddEditOptions") != null)
                    {     
                        buildAddEditOptions();
                        strCurrent = ADDEDIT_OPTIONS;
                        strStylesheet = ADDEDIT_OPTIONS;                       
                         
                    }
                    // if user clicks on save Option Pool
                    else  if (runtimeData.getParameter("updateOptionPool") != null)
                    {                       
                        doUpdateOptionPool();   
                        
                          // These values are required to access Add/Edit Options and Add/edit DataElements page, if back button is clicked
                        strXML+= "<OptionPoolSelected>"+runtimeData.getParameter("OptionPoolSelected")+"</OptionPoolSelected>" +
                                 "<OPTIONS_intOptionIDSelected>"+runtimeData.getParameter("OPTIONS_intOptionIDSelected")+"</OPTIONS_intOptionIDSelected>" + 
                                 "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                                 "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                                 "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";

                    } 
                    // if user clicks on delete Option Pool
                    else  if (runtimeData.getParameter("deleteOptionPool") != null)
                    {                       
                        doDeleteOptionPool();                 
                        
                    } 
                    // user clicks on Edit Option Pool in Add/Edit Options
                    else
                    {
                        displayOptionPoolDetails();  
                         // These values are required to access Add/Edit Options and Add/edit DataElements page, if back button is clicked
                        strXML+= "<OptionPoolSelected>"+runtimeData.getParameter("OptionPoolSelected")+"</OptionPoolSelected>" +
                                 "<OPTIONS_intOptionIDSelected>"+runtimeData.getParameter("OPTIONS_intOptionIDSelected")+"</OPTIONS_intOptionIDSelected>" + 
                                 "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                                 "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                                 "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";

                    }
                        
                    checkSmartformActions();
                    
                }
                 
                
                strPreviousXML = strXML;
            }
            else
            {
                strXML = strPreviousXML;
            }     

            
            
            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><smartform>" + strXML + "</smartform>";
            
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Advanced Search Channel - " + e.toString(), e);
        }
    }  
    
    
    
     /** Output channel content to the portal
     *  @param out a sax document handler
     */
    public void renderXML(ContentHandler out) throws PortalException
    {

        // Create a new XSLT styling engine
        XSLT xslt = new XSLT(this);

        //System.err.println("strXML: " + strXML);
        
        // pass the result xml to the styling engine.
        xslt.setXML(strXML);

        // specify the stylesheet selector
        xslt.setXSL("CSmartformAdmin.ssl", strStylesheet, runtimeData.getBrowserInfo());

        // set parameters that the stylesheet needs.
        xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL()); 
        
        try
        {
            if(PropertiesManager.getProperty("solution.type").equals("Biogenix"))
            {
                //Study------

                org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL( true ));

                upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CStudy"));
        //        System.err.println("-----------stylesheetparameter----------" + upfTmp.getUPFile());
                xslt.setStylesheetParameter("studyChannelURL", upfTmp.getUPFile());
                xslt.setStylesheetParameter( "studyChannelTabOrder", SessionManager.getTabOrder( authToken, "CStudy") );
            }
        }catch(Exception e)
        {
            LogService.log(LogService.ERROR, "Could not find property solutions.type in portal.properties");
        }


        // set the output Handler for the output.
        xslt.setTarget(out);

        // do the deed
        xslt.transform();
    }
    
    
     /**
     *  Print out message if user doesn't have the autority to do a specific activity
     */
    private String buildSecurityErrorXMLFile(String aFunctionalArea)
    {
        String strTempErrorXML  = "<securityerror>"
                + "<errorstring>No Permission to " + aFunctionalArea + "</errorstring>"
                + "<errortext>The " + staticData.getPerson().getFullName() 
                + " is not authorised to " + aFunctionalArea + "</errortext>"
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
    
    /**
     * Clear the lock request DE Pool
     */
    private void clearLockRequestDEPool()
    {
        try
        {          
            //********************
            if (lockRequestDEPool != null && lockRequestDEPool.isValid())
            {    lockRequestDEPool.unlock();}
            //********************
            
        }
        catch (Exception e)
        {
            LogService.instance().log(
                LogService.ERROR, "Unknown error in Search Channel - " + e.toString(), e);
        }
        finally
        {
            
            //********************
            lockRequestDEPool = null;
            //********************
        }
    }   
    
     /**
     * Clear the lock request Option Pool
     */
    private void clearLockRequestOptionPool()
    {
        try
        {          
            //********************
            if (lockRequestOptionPool != null && lockRequestOptionPool.isValid())
            {    lockRequestOptionPool.unlock();}
            //********************
            
        }
        catch (Exception e)
        {
            LogService.instance().log(
                LogService.ERROR, "Unknown error in Search Channel - " + e.toString(), e);
        }
        finally
        {
            
            //********************
            lockRequestOptionPool = null;
            //********************
        }
    }   
    
    /* To search Smartforms
     *
     */  
    private void buildSearchSmartForm()
    {
        try
        {
                       
            //double d =100.98567;      
            //int k = (int)Math.round(d);
            
            strStylesheet = SEARCH_SMARTFORM;
            
            // Get the form fields to display the labels and their corresponding fields
            Vector vtSearchSmartForm = DatabaseSchema.getFormFields("csmartformadmin_search_smartform");        
            strXML = QueryChannel.buildFormLabelXMLFile(vtSearchSmartForm)
                     +"<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";    

            // Start lock
            clearLockRequest();
            //********
            clearLockRequestDEPool();
            clearLockRequestOptionPool();
            //********
            // End lock
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }          
        
    }
    
    /* To display a page to Add Smartform
     *
     *
     */
    private void displayAddSmartForm()
    {
        try
        {
            strCurrent = ADD_SMARTFORM;                

             // Get the form fields to display all the labels and their corresponding fields
            Vector vtViewSmartForm = DatabaseSchema.getFormFields("csmartformadmin_view_smartform");

            strXML = QueryChannel.buildFormLabelXMLFile(vtViewSmartForm) +
                     QueryChannel.buildAddFormXMLFile(vtViewSmartForm);

            strStylesheet = ADD_SMARTFORM;  
            // Start lock
            clearLockRequest();
            // End lock
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    /* To Add Smartform
     *
     *
     */
    private void saveNewSmartForm()
    {
        
        Vector vtAddSmartForm = DatabaseSchema.getFormFields("csmartformadmin_add_smartform");
        Vector vtViewSmartForm = DatabaseSchema.getFormFields("csmartformadmin_view_smartform");
        //Vector vtAddActivity = DatabaseSchema.getFormFields("view_activity");
        
        
        
        try
        {
           
            String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddSmartForm, runtimeData);
            
             // checks data validation
            String strValidation = QueryChannel.validateData(vtAddSmartForm, runtimeData);               
            
            // If all required fields values are supplied, then perform insert
            if (((strCheckRequiredFields == null) || (strCheckRequiredFields.equals(""))) && ((strValidation == null) || (strValidation.equals(""))))
            {                              
                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_INSERT, authToken);                
                
                // Get the max no in Smartform Order column
                int maxSmartformOrder = QueryChannel.getNewestKeyAsInt("SMARTFORM_intSmartformOrder");
                maxSmartformOrder = maxSmartformOrder + 1;                
                
                // Add new smartform                
                query.setDomain("SMARTFORM", null, null, null);         
                query.setFields(vtAddSmartForm, runtimeData);
                runtimeData.setParameter("SMARTFORM_intSmartformOrder",new Integer(maxSmartformOrder).toString());
                query.executeInsert();                 

                int intCurrentSmartformID = QueryChannel.getNewestKeyAsInt(query);
                strCurrentNode = "SMARTFORM_" + intCurrentSmartformID;                
                
                //*****************************INSERT INTO ACTIVITY TABLE***********************
                String insertActivity = "";
                
                // To add the new Smartform ID ADD in activity table
                insertActivity= "smartform_id_"+new Integer(intCurrentSmartformID).toString()+"_add";          
                DALSecurityQuery query1 = new DALSecurityQuery(SMARTFORM_INSERT, authToken);                
                query1.setDomain("ACTIVITY", null, null, null);           
                query1.setField("ACTIVITY_strActivityName",insertActivity);                
                query1.setField("ACTIVITY_strActivityDesc","Smartform Admin - SmartformId Add ");          
                query1.executeInsert();   
                 
                // To add the new Smartform ID EDIT in activity table
                insertActivity= "smartform_id_"+new Integer(intCurrentSmartformID).toString()+"_edit";                
                DALSecurityQuery query2 = new DALSecurityQuery(SMARTFORM_INSERT, authToken);                
                query2.setDomain("ACTIVITY", null, null, null);           
                query2.setField("ACTIVITY_strActivityName",insertActivity);                
                query2.setField("ACTIVITY_strActivityDesc","Smartform Admin - SmartformId Edit ");          
                query2.executeInsert();  
                
                // To add the new Smartform ID DELETE in activity table
                insertActivity= "smartform_id_"+new Integer(intCurrentSmartformID).toString()+"_delete";                
                DALSecurityQuery query3 = new DALSecurityQuery(SMARTFORM_INSERT, authToken);                
                query3.setDomain("ACTIVITY", null, null, null);           
                query3.setField("ACTIVITY_strActivityName",insertActivity);                
                query3.setField("ACTIVITY_strActivityDesc","Smartform Admin - SmartformId Delete");          
                query3.executeInsert(); 
                
                // To add the new Smartform ID SEARCH in activity table
                insertActivity= "smartform_id_"+new Integer(intCurrentSmartformID).toString()+"_search";                
                DALSecurityQuery query4 = new DALSecurityQuery(SMARTFORM_INSERT, authToken);                
                query4.setDomain("ACTIVITY", null, null, null);           
                query4.setField("ACTIVITY_strActivityName",insertActivity);                
                query4.setField("ACTIVITY_strActivityDesc","Smartform Admin - SmartformId Search");          
                query4.executeInsert(); 
                
                // To add the new Smartform ID VIEW in activity table
                insertActivity= "smartform_id_"+new Integer(intCurrentSmartformID).toString()+"_view";                
                DALSecurityQuery query5 = new DALSecurityQuery(SMARTFORM_INSERT, authToken);                
                query5.setDomain("ACTIVITY", null, null, null);           
                query5.setField("ACTIVITY_strActivityName",insertActivity);                
                query5.setField("ACTIVITY_strActivityDesc","Smartform Admin - SmartformId View");          
                query5.executeInsert(); 
                
                // To add the new Smartform ID COPY in activity table
                insertActivity= "smartform_id_"+new Integer(intCurrentSmartformID).toString()+"_copy";                
                DALSecurityQuery query6 = new DALSecurityQuery(SMARTFORM_INSERT, authToken);                
                query6.setDomain("ACTIVITY", null, null, null);           
                query6.setField("ACTIVITY_strActivityName",insertActivity);                
                query6.setField("ACTIVITY_strActivityDesc","Smartform Admin - SmartformId Copy");          
                query6.executeInsert(); 
                
                
                //********************END OF INSERT INTO ACTIVITY TABLE***************************
                // Start lock
                
                 if (strCurrentNode.equals("SMARTFORM_" + intCurrentSmartformID))
                 {                     
                     clearLockRequest();
                     lockRequest = new LockRequest(authToken);
                     // register the lock to session manager
                     SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartformAdmin", lockRequest);
                     lockRequest.addLock("SMARTFORM", new Integer(intCurrentSmartformID).toString(), LockRecord.READ_WRITE);                     
                    
                 }
                else
                {                    
                     clearLockRequest();
                     lockRequest = new LockRequest(authToken);
                     // register the lock to session manager
                    SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartformAdmin", lockRequest);
                    lockRequest.addLock("SMARTFORM", new Integer(intCurrentSmartformID).toString(), LockRecord.READ_ONLY);
                    
                }                
                lockRequest.lockDelayWrite();
                
                // End lock

                strXML = QueryChannel.buildFormLabelXMLFile(vtViewSmartForm) +
                         QueryChannel.buildViewAfterAddXMLFile(vtViewSmartForm, runtimeData, "SMARTFORM_intSmartformID", intCurrentSmartformID);                
                
                strStylesheet = VIEW_SMARTFORM;
                
                

            }

            else
            { 
                
                strStylesheet = ADD_SMARTFORM;                
                strXML = QueryChannel.buildFormLabelXMLFile(vtViewSmartForm) +
                         QueryChannel.buildViewXMLFile(vtViewSmartForm, runtimeData);
                  
                strErrorMessage = strCheckRequiredFields;                 
                strXML = strXML + 
                         "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";
                
            }

            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    /* To Delete Smartform
     *
     *
     */
    private void doDeleteSmartForm()
    {
        
        try
        {       
            
                   
            Vector vtViewSmartForm = DatabaseSchema.getFormFields("csmartformadmin_view_smartform"); 
            Vector vtEditSmartForm = DatabaseSchema.getFormFields("csmartformadmin_add_smartform");
            Vector vtBuildSmartFormsDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElements");
            
            
            if (runtimeData.getParameter("SMARTFORM_intSmartformID") != null)
             {
                // Delete from Smartform table
                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_DELETE, authToken);   
                query.setDomain("SMARTFORM", null, null, null);  
                query.setFields(vtEditSmartForm, runtimeData);                
                query.setWhere(null, 0, "SMARTFORM_intSmartformID", "=", runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);                
                query.setField("SMARTFORM_intDeleted", "-1");                
                
                 //Locking
                   
                   if (lockRequest.isValid() && lockRequest.lockWrites())
                   {  
                       // delete from Smartform table
                      query.executeUpdate();
                      
                      // delete from SmartformToDataElements table
                      DALSecurityQuery query1 = new DALSecurityQuery(SMARTFORM_DELETE, authToken);
                      query1.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);                            
                      query1.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);                
                      query1.setField("SMARTFORMTODATAELEMENTS_intDeleted", "-1");   
                      query1.executeUpdate();
                      
                      //delete from *********************Activity table*****************************
                      //delete Add
                      DALSecurityQuery query2 = new DALSecurityQuery(SMARTFORM_DELETE, authToken);
                      query2.setDomain("ACTIVITY", null, null, null);                            
                      query2.setWhere(null, 0, "ACTIVITY_strActivityName", "=", "smartform_id_"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"_add", 0, DALQuery.WHERE_HAS_VALUE);                
                      query2.setField("ACTIVITY_intDeleted", "-1");   
                      query2.executeUpdate();
                      
                      //delete Edit
                      DALSecurityQuery query3 = new DALSecurityQuery(SMARTFORM_DELETE, authToken);
                      query3.setDomain("ACTIVITY", null, null, null);                            
                      query3.setWhere(null, 0, "ACTIVITY_strActivityName", "=", "smartform_id_"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"_edit", 0, DALQuery.WHERE_HAS_VALUE);                
                      query3.setField("ACTIVITY_intDeleted", "-1");   
                      query3.executeUpdate();
                      
                      //delete Delete
                      DALSecurityQuery query4 = new DALSecurityQuery(SMARTFORM_DELETE, authToken);
                      query4.setDomain("ACTIVITY", null, null, null);                            
                      query4.setWhere(null, 0, "ACTIVITY_strActivityName", "=", "smartform_id_"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"_delete", 0, DALQuery.WHERE_HAS_VALUE);                
                      query4.setField("ACTIVITY_intDeleted", "-1");   
                      query4.executeUpdate();
                      
                      //delete Search
                      DALSecurityQuery query5 = new DALSecurityQuery(SMARTFORM_DELETE, authToken);
                      query5.setDomain("ACTIVITY", null, null, null);                            
                      query5.setWhere(null, 0, "ACTIVITY_strActivityName", "=", "smartform_id_"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"_search", 0, DALQuery.WHERE_HAS_VALUE);                
                      query5.setField("ACTIVITY_intDeleted", "-1");   
                      query5.executeUpdate();
                      
                      //delete View
                      DALSecurityQuery query6 = new DALSecurityQuery(SMARTFORM_DELETE, authToken);
                      query6.setDomain("ACTIVITY", null, null, null);                            
                      query6.setWhere(null, 0, "ACTIVITY_strActivityName", "=", "smartform_id_"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"_view", 0, DALQuery.WHERE_HAS_VALUE);                
                      query6.setField("ACTIVITY_intDeleted", "-1");   
                      query6.executeUpdate();
                      
                      //delete Copy
                      DALSecurityQuery query7 = new DALSecurityQuery(SMARTFORM_DELETE, authToken);
                      query7.setDomain("ACTIVITY", null, null, null);                            
                      query7.setWhere(null, 0, "ACTIVITY_strActivityName", "=", "smartform_id_"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"_copy", 0, DALQuery.WHERE_HAS_VALUE);                
                      query7.setField("ACTIVITY_intDeleted", "-1");   
                      query7.executeUpdate();
                      
                      //End of delete from *********************Activity table*****************************
                
                      lockRequest.unlockWrites();  
                      strErrorMessage = "The Smartform is deleted successfully";
                      buildSearchSmartForm();

                   }
                   else
                   {                       
                       strErrorMessage = "This record is being viewed by other users. You cannot delete it now. Please try again later. ";
                       displayViewSmartForm();
                       strXML = strXML + 
                         "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";
                       
                   }
                //query.executeUpdate();          
                
             }            
            
               
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }    
    
           
    /*
     * To go to the first page of the channel - Smartform
     */
     private void doViewSmartForm()
    {
        
        strStylesheet = SEARCH_SMARTFORM;
        strXML = "";

    }
     
     
     /* To Update Smartform
     *
     *
     */
    private void buildSearchSmartFormResults()
    {        
        try
        {
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_SEARCH, authToken);                                            
            Vector vtSearchSmartForm = DatabaseSchema.getFormFields("csmartformadmin_search_smartform");
            
            strSmartformName = runtimeData.getParameter("SMARTFORM_strSmartformName");                 
            if ( runtimeData.getParameter("current") != null) {
               strBackButton = "current=" + runtimeData.getParameter("current");
               strBackButton += "&SMARTFORM_strSmartformName=" +  runtimeData.getParameter("SMARTFORM_strSmartformName");
               strBackButton += "&submitSearchSmartform=true";
               strBackButton = Utilities.cleanForXSL(strBackButton);
                     
            }
               
            query.setDomain("SMARTFORM", null, null, null);
            query.setFields(vtSearchSmartForm, null);
            query.setWhere(null, 0, "SMARTFORM_strSmartformName", "LEFT LIKE", runtimeData.getParameter("SMARTFORM_strSmartformName"), 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "SMARTFORM_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("SMARTFORM_strSmartformName", "ASC");    
            ResultSet rsResult = query.executeSelect();          
            
            strXML = QueryChannel.buildSearchXMLFile("search_smartform", rsResult,vtSearchSmartForm)
                     + QueryChannel.buildFormLabelXMLFile(vtSearchSmartForm);
            
            rsResult.last();
            int count = rsResult.getRow();
            
            if (count < 1)
            {
                strErrorMessage = "No matching smartform found";
                strXML = strXML + 
                         "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";            

            }    
 /*           //Study------------rennypv
            if(runtimeData.getParameter("strcurrent") != null)
            {
            
//                System.err.println("back button----------" + runtimeData.getParameter("strcurrent") + " " + runtimeData.getParameter("intStudyID"));
                strBackButton = "current="+runtimeData.getParameter("strcurrent") + "&intStudyID=" + runtimeData.getParameter("intStudyID");
                strBackButton = Utilities.cleanForXSL(strBackButton);


            }
            if(strBackButton != null)
            {
             strXML += "<strBackButton>"+strBackButton+"</strBackButton>";
            }
            */
            //rennypv
            // Start lock        

            rsResult.beforeFirst();
            clearLockRequest();
            lockRequest = new LockRequest(authToken);
            // register the lock to session manager
            SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartformAdmin", lockRequest);
            
            while(rsResult.next())
            {

                 String strSmartformID = rsResult.getString("SMARTFORM_intSmartformID");
                 if (strSmartformID != null)
                 {             
                    lockRequest.addLock("SMARTFORM", strSmartformID, LockRecord.READ_ONLY);

                 }
               
            }
            lockRequest.lockDelayWrite();
            
            // End lock
            
            rsResult.close();
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    /* To Display Smartform Details in Search
     *
     *
     */
    private void displaySmartformDetails()
    {        
        try
        {
            
            Vector vtViewSmartForm = DatabaseSchema.getFormFields("csmartformadmin_view_smartform");
            
            int intSmartformID = Integer.parseInt(runtimeData.getParameter("SMARTFORM_intSmartformID"));

            strXML = QueryChannel.buildFormLabelXMLFile(vtViewSmartForm) +
                     QueryChannel.buildViewFromKeyXMLFile(vtViewSmartForm,"SMARTFORM_intSmartformID",intSmartformID);            
            
            strStylesheet =VIEW_SMARTFORM;
            if(runtimeData.getParameter("strcurrent") != null)
            {
            
//                System.err.println("back button----------" + runtimeData.getParameter("strcurrent") + " " + runtimeData.getParameter("intStudyID"));
                strBackButton = "current="+runtimeData.getParameter("strcurrent") + "&intStudyID=" + runtimeData.getParameter("intStudyID");
                strBackButton = Utilities.cleanForXSL(strBackButton);


            }
            if(strBackButton != null)
            {
             strXML += "<strBackButton>"+strBackButton+"</strBackButton>";
            }
            // Start Lock
            
            if (new Integer(intSmartformID).toString() != null)
            {               
                clearLockRequest();
                lockRequest = new LockRequest(authToken);
                // register the lock to session manager
                SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartformAdmin", lockRequest);
                lockRequest.addLock("SMARTFORM", new Integer(intSmartformID).toString(), LockRecord.READ_WRITE);
            }
            
            lockRequest.lockDelayWrite();
            // End lock
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }  
    
     /* To display the same page again, after updating or deleting the smartform
     *
     *
     */
    private void displayViewSmartForm()
    {
        try
        {
            strCurrent = VIEW_SMARTFORM;
            
             // Get the form fields to display all the labels and their corresponding fields
            Vector vtViewSmartForm = DatabaseSchema.getFormFields("csmartformadmin_view_smartform");

            strXML = QueryChannel.buildFormLabelXMLFile(vtViewSmartForm) +
                     QueryChannel.buildViewXMLFile(vtViewSmartForm, runtimeData);      
            // need to go the previouus page
            strXML += "<strBackButton>" + strBackButton + "</strBackButton>"; 
            strStylesheet = VIEW_SMARTFORM;
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    /* To Update Smartform
     *
     *
     */
    private void doUpdateSmartForm()
    {        
        try
        {     
            
           strStylesheet = VIEW_SMARTFORM;
           Vector vtViewSmartForm = DatabaseSchema.getFormFields("csmartformadmin_view_smartform");
           Vector vtEditSmartForm = DatabaseSchema.getFormFields("csmartformadmin_add_smartform");
           DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_UPDATE,authToken); 
           
           
           //System.out.println(runtimeData.getParameter("SMARTFORM_strDomain")); 
           
           String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtViewSmartForm, runtimeData);            
           // checks data validation
           String strValidation = QueryChannel.validateData(vtViewSmartForm, runtimeData);
            
            if (((strCheckRequiredFields == null) || (strCheckRequiredFields.equals(""))) && ((strValidation == null) || (strValidation.equals(""))))
            {
           
               if (runtimeData.getParameter("SMARTFORM_intSmartformID") != null)
               {               

                   // Get the Smartform Order for this smartform id
                   query.setDomain("SMARTFORM", null,null,null);
                   query.setFields(vtViewSmartForm , runtimeData);                      
                   query.setWhere(null, 0, "SMARTFORM_intSmartformID", "=", runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);                               
                   query.setField("SMARTFORM_intDeleted","0");
                   ResultSet rs = query.executeSelect();                 
                   int maxSmartformOrder = 0;
                   while(rs.next()){
                   maxSmartformOrder = Integer.parseInt(rs.getString("SMARTFORM_intSmartformOrder"));                                
                   }                   
                   
                   rs.close();
                   
                   query.reset();
                   query.setDomain("SMARTFORM", null,null,null);
                   query.setFields(vtEditSmartForm , runtimeData);   
                   query.setWhere(null, 0, "SMARTFORM_intSmartformID", "=", runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);                               
                   runtimeData.setParameter("SMARTFORM_intSmartformOrder",new Integer(maxSmartformOrder).toString());
                   query.setField("SMARTFORM_intDeleted","0");
                   
                    // Start Lock                  
                   
                   if (lockRequest.isValid() && lockRequest.lockWrites())
                   {                       
                      query.executeUpdate();
                      lockRequest.unlockWrites(); 
                      strErrorMessage = "The Smartform is updated successfully";

                   }
                   else
                   {                       
                       strErrorMessage = "This record is being viewed by other users. You cannot update it now. Please try again later. ";                       
                       displayViewSmartForm();
                   }
                   // End lock
                   

                   
                   int intSmartformID = Integer.parseInt(runtimeData.getParameter("SMARTFORM_intSmartformID"));               

                   strXML = QueryChannel.buildFormLabelXMLFile(vtViewSmartForm) +                        
                            QueryChannel.buildViewAfterUpdateXMLFile(vtViewSmartForm, runtimeData, "SMARTFORM_intSmartformID") +                        
                           "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";  
                   
                     strXML += "<strBackButton>" + strBackButton + "</strBackButton>"; 
                           
                  
                   strStylesheet = VIEW_SMARTFORM;              

               }
               
            }
             else
            { 
                
                strStylesheet = VIEW_SMARTFORM;                
                strXML = QueryChannel.buildFormLabelXMLFile(vtViewSmartForm) +
                         QueryChannel.buildViewXMLFile(vtViewSmartForm, runtimeData);
                  
                strErrorMessage = strCheckRequiredFields;                 
                strXML = strXML + 
                         "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";
                
            }
            
           
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    /* To check the actions performed in Smartform
     *
     *
     */
    private void checkSmartformActions()
    {        
        try
        {           
             // User wants to add a smartform
            if (runtimeData.getParameter("addSmartform") != null)
            {
                // display unauthorization message
                if (!authToken.hasActivity(SMARTFORM_ADD))
                {
                    strXML  = buildSecurityErrorXMLFile("add smartform");                    
                    strStylesheet = SECURITY;
                    return;
                }
                
                displayAddSmartForm();           

            }
            
             // User wants to search a smartform
            else if (runtimeData.getParameter("searchSmartform") != null)
            {
                // display unauthorization message
                if (!authToken.hasActivity(SMARTFORM_SEARCH))
                {
                    strXML  = buildSecurityErrorXMLFile("search smartform");
                    strStylesheet = SECURITY;
                    return;
                }
                
                buildSearchSmartForm();         

            }            
            
            // User wants to search a existing smartform
            else if (runtimeData.getParameter("submitSearchSmartform") != null)
            {
                
                // Check if the name field is entered to do search
                if (runtimeData.getParameter("SMARTFORM_strSmartformName") != null )
                {                    
                    strErrorMessage ="";
                    buildSearchSmartFormResults();
                }                                             

            }       
            
            //User wants to save a new Smartform 
            else if(runtimeData.getParameter("saveNewSmartform") != null)
            {
                
                 // display unauthorization message
                if (!authToken.hasActivity(SMARTFORM_INSERT))
                {
                    strXML  = buildSecurityErrorXMLFile("insert smartform");
                    strStylesheet = SECURITY;
                    return;
                }
                
                 saveNewSmartForm();

            }  
            
            //User wants to update a Smartform 
            else if(runtimeData.getParameter("updateSmartform") != null)
            {
                 // display unauthorization message
                if (!authToken.hasActivity(SMARTFORM_UPDATE))
                {
                    strXML  = buildSecurityErrorXMLFile("update smartform");
                    strStylesheet = SECURITY;
                    return;
                }
                
                 doUpdateSmartForm();

            }             

             // User wants to delete a smartform
            else if (runtimeData.getParameter("deleteSmartform") != null && runtimeData.getParameter("deleteSmartform").equals("true"))
            {                       
                 // display unauthorization message
                if (!authToken.hasActivity(SMARTFORM_DELETE))
                {
                    strXML  = buildSecurityErrorXMLFile("delete smartform");
                    strStylesheet = SECURITY;
                    return;
                }
                
                doDeleteSmartForm();                 

            }     

             // On click of a Smartform in search results, User needs to view the smartform details
            else if (runtimeData.getParameter("displaySmartform") != null)
            {                   
                 // display unauthorization message
                if (!authToken.hasActivity(SMARTFORM_VIEW))
                {
                    strXML  = buildSecurityErrorXMLFile("view smartform");
                    strStylesheet = SECURITY;
                    return;
                }
                
                displaySmartformDetails();        

            }     
            
            
            // click of Build Smartform
            else if (runtimeData.getParameter("buildsmartform") != null)
            {        



                 // Start Lock             
                   if (lockRequest.isValid() && lockRequest.lockWrites())
                   {                            
                      buildSmartForm();
                      lockRequest.unlockWrites();
                   
                   }
                   else
                   {                
                       strErrorMessage = "This record is being viewed by other users. You cannot build it now. Please try again later. ";                       
                       displayViewSmartForm();
                       strXML+="<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";
                   }
                   // End lock
                   
                //buildSmartForm();
            }     
             
            // Actions in Add/Edit DataElements
            else if (strCurrent.equals(ADDEDIT_DATAELEMENTS))
            {
                    // In Add/Edit DataElements, user clicks on back to go to Build Smartform
                    if (runtimeData.getParameter("backtoBuildSmartform") != null)
                    {
                        lockRequestDEPool.unlock();
                        clearLockRequestDEPool();
                        buildSmartForm();
                    }
                    // user clicks on Add DataElement
                    else if (runtimeData.getParameter("addDataElement") != null)
                    {
                           //System.out.println("add dataelement clicked");
                           strMode = "newDEClicked";
                           buildAddEditDataElements();

                           // To clear the deleted id's value in the hidden field
                            int startPos = strXML.indexOf("<strMode>",0);                        
                            strXML = strXML.substring(0,startPos);  

                            //System.out.println("strMode:"+strMode);

                           strXML+="<strMode>"+ strMode +"</strMode>";

                    }
                    // user clicks on Add DataElement
                    else if (runtimeData.getParameter("addTitleComment") != null)
                    {
                           //System.out.println("add title clicked");
                           strMode = "newTitleClicked";
                           buildAddEditDataElements();

                           // To clear the deleted id's value in the hidden field
                            int startPos = strXML.indexOf("<strMode>",0);                        
                            strXML = strXML.substring(0,startPos);  

                            //System.out.println("strMode:"+strMode);
                           strXML+="<strMode>"+ strMode +"</strMode>";

                    }
                     // user clicks on Add PageBreak
                    else if (runtimeData.getParameter("addPageBreak") != null)
                    {
                           //System.out.println("add Pagebreak clicked");
                           strMode = "addPageBreak";
                           
                           // to insert a page break
                           int intDataElementID = insertPageBreak();
                           
                           buildAddEditDataElements();
                           
                           // To build the page back
                            buildPageAfterInsert(intDataElementID);

                          /* // To clear the deleted id's value in the hidden field
                            int startPos = strXML.indexOf("<strMode>",0);                        
                            strXML = strXML.substring(0,startPos);  

                            //System.out.println("strMode:"+strMode);
                           strXML+="<strMode>"+ strMode +"</strMode>";
                           */

                    }
                    
                    // In Add/Edit DataElements, user clicks on moveup
                    else if (runtimeData.getParameter("moveUp") != null)
                    {                        
                        ChangeDEOrderinAddEditDE(runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"), "moveUp");
                    }
                    
                    // In Add/Edit DataElements, user clicks on movedown
                    else if (runtimeData.getParameter("moveDown") != null)
                    {                        
                        ChangeDEOrderinAddEditDE(runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"), "moveDown");
                    }
                    
                    // In Add/Edit DataElements, user clicks on delete
                    else if (runtimeData.getParameter("deleteDE") != null)
                    {                        
                         deleteDataElementinAddEditDE();
                    }
                    
                    // user clicks on save
                    else if (runtimeData.getParameter("saveDataElement") != null)
                    {
                        //System.out.println("save is clicked");
                        saveDataElementInAddEditDE();
                    }
                    
                    // user clicks on duplicate
                     else if (runtimeData.getParameter("duplicateDataElement") != null)
                    {
                        //System.out.println("duplicate is clicked");
                        duplicateDataElementInAddEditDE();
                    }
                    
                    // user clicks on Add to Add Where condition
                     else if (runtimeData.getParameter("addWhere") != null)
                    {
                        //System.out.println("Add Where is clicked");
                        addWhere();
                        
                    }
                    
                    // user clicks on Update to Update Where condition
                     else if (runtimeData.getParameter("updateWhere") != null)
                    {
                        //System.out.println("Update Where is clicked");
                        updateWhere();
                        
                    }             
                    else
                    {                        
                       buildAddEditDataElements();                           
                    }
                    
                
            }   
            
           
            else{}
            
            
            
            /*//User wants to clear the Add Smartform and display the same page again
            if(runtimeData.getParameter("clear") != null && runtimeData.getParameter("clear").equals("true"))
            {

                 displayAddSmartForm();

            }*/                    
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }  
    
    
     /* To build a Smartform with Dataelements
     *
     *
     */
    private void buildSmartForm()
    {
        try
        {  
                //System.out.println("DE id selected: "+runtimeData.getParameter("SMARTFORMTODATAELEMENTS_intDataElementID"));
                // Get the form fields to display all the labels and their corresponding fields
                Vector vtSearchSmartForm = DatabaseSchema.getFormFields("csmartformadmin_search_smartform");            
                Vector vtBuildDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElementPool");
                Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_viewDataElements");
                Vector vtBuildSmartFormsDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElements");

                String strDataElementPoolID=""; 
                String strDEIDClicked = "";
                String DEClicked = "";
                
                strCurrent = BUILD_SMARTFORM;    
                
                 
                // To Load this Smartform's Dataelements 
                DALSecurityQuery queryJoin = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                queryJoin.setDomain("SMARTFORMTODATAELEMENTS", null, null, null); 
                queryJoin.setDomain("DATAELEMENTS","SMARTFORMTODATAELEMENTS_intDataElementID","DATAELEMENTS_intDataElementID","INNER JOIN");
                queryJoin.setFields(vtBuildSmartFormsDataElements, null);    
                queryJoin.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);            
                queryJoin.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                queryJoin.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");            

                ResultSet rsResultJoin = queryJoin.executeSelect();   

                // To Load the DataElement Pool names in the dropdown
                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                query.setDomain("DATAELEMENTPOOL", null, null, null); 
                query.setFields(vtBuildDataElementPool, null);
                query.setWhere(null, 0, "DATAELEMENTPOOL_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);                            
                query.setOrderBy("DATAELEMENTPOOL_strDataElementPoolName", "ASC");            

                ResultSet rsResult = query.executeSelect();
                
                String strSmartformID = runtimeData.getParameter("SMARTFORM_intSmartformID");          
                
               // To get the value of DataElement Id selected
                if (runtimeData.getParameter("SMARTFORMTODATAELEMENTS_intDataElementID") != null)
                {
                     strDEIDClicked = runtimeData.getParameter("SMARTFORMTODATAELEMENTS_intDataElementID");
                     //System.out.println("strDEIDClicked 1:"+strDEIDClicked);
                }
                else
                {
                     strDEIDClicked = runtimeData.getParameter("SMARTFORMTODATAELEMENTS_DEIdSelected");                     
                     //System.out.println("strDEIDClicked 2:"+strDEIDClicked);
                }
                                

                 // To Load all the dataelements, when a dataelement pool is selected
                DALSecurityQuery query1 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);    
                query1.setDomain("DATAELEMENTS", null, null, null); 
                query1.setFields(vtBuildDataElements, null);
                query1.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);     
                
                // Only When DataElement is selected, display its dataelement pool as selected in the dropdown
                if (runtimeData.getParameter("DEClicked") != null && runtimeData.getParameter("DEClicked").equals("true"))
                {
                    if (strDEIDClicked != null)
                    {
                        //System.out.println("DE is clicked & pool is not clicked");
                        DALSecurityQuery query2 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);    
                        query2.setDomain("DATAELEMENTS", null, null, null); 
                        query2.setFields(vtBuildDataElements, null);
                        query2.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);     
                        query2.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", strDEIDClicked, 0, DALQuery.WHERE_HAS_VALUE);
                        ResultSet rsResult2 = query2.executeSelect();

                        while (rsResult2.next())
                        {
                            strDataElementPoolID = rsResult2.getString("DATAELEMENTS_intDataElementPoolID");                        
                        }
                        
                        rsResult2.close();
                    }
                    else
                    {
                        strDataElementPoolID = null;
                    }
                }
                if (runtimeData.getParameter("DEClicked") == null)                
                {               
                    if(runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID") != null)            
                    {           
                         strDataElementPoolID = runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID");                                                        

                    }
                    if(runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID") == null || runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID").equals(""))            
                    {           
                         strDataElementPoolID = runtimeData.getParameter("DEPoolSelected");                         
                    }
                }    
                  
                //System.out.println("strDataElementPoolID :"+strDataElementPoolID);
                query1.setWhere("AND", 0, "DATAELEMENTS_intDataElementPoolID", "=", strDataElementPoolID, 0, DALQuery.WHERE_HAS_VALUE);
                
                //query1.setOrderBy("DATAELEMENTS_intDataElementID", "ASC");  
                query1.setOrderBy("DATAELEMENTS_intDataElementOrder", "ASC"); 
                

                ResultSet rsResult1 = query1.executeSelect();

               
                    
                    strXML = QueryChannel.buildFormLabelXMLFile(vtSearchSmartForm) +
                             QueryChannel.buildViewFromKeyXMLFile(vtSearchSmartForm,"SMARTFORM_intSmartformID",strSmartformID) +           
                             QueryChannel.buildFormLabelXMLFile(vtBuildDataElementPool) +  
                             QueryChannel.buildSearchXMLFile("buildsmartform_DataElementPool", rsResult,vtBuildDataElementPool) +
                             QueryChannel.buildFormLabelXMLFile(vtBuildDataElements) +
                             QueryChannel.buildSearchXMLFile("buildsmartform_DataElements", rsResult1,vtBuildDataElements) +
                             "<DEPoolSelected>"+ strDataElementPoolID +"</DEPoolSelected>" +                 
                             QueryChannel.buildFormLabelXMLFile(vtBuildSmartFormsDataElements);
                             
                             // If Smartform to dataelements(table) is not empty, then load the Smartforms dataelements
                             if (rsResultJoin != null)
                             {                                 
                                strXML+=QueryChannel.buildSearchXMLFile("SmartformsDataElements", rsResultJoin, vtBuildSmartFormsDataElements);
                             }
                             
                             rsResultJoin.close();
                             rsResult.close();
                             rsResult1.close();
                             
                 strXML+= "<SMARTFORMTODATAELEMENTS_DEIdSelected>"+ strDEIDClicked +"</SMARTFORMTODATAELEMENTS_DEIdSelected>";
                                                    
                             
              
                strStylesheet = BUILD_SMARTFORM;         

          
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    /* To save the buildsmartforms dataelements
     *
     *
     */
    private void saveBuildSmartForm()
    {
        try
        {
            strCurrent = BUILD_SMARTFORM;            
           
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
            DALSecurityQuery queryOrder = new DALSecurityQuery(SMARTFORM_BUILD, authToken);  
            
             // Get the form fields to display all the labels and their corresponding fields            
            Vector vtBuildSmartFormsDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElements");
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_viewDataElements"); 
            Vector vtBuildSmartFormsDataElementsOnly = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElementsOnly");
            
             // To get the Order for this Smartform id, if it exists else set to 0
            queryOrder.setDomain("SMARTFORMTODATAELEMENTS", null, null, null); 
            queryOrder.setFields(vtBuildSmartFormsDataElementsOnly, null);
            queryOrder.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);                            
            queryOrder.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=",runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);
            queryOrder.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementNo", "DESC");
            
            ResultSet rsResultOrder = queryOrder.executeSelect();
            int DENo;
            
            if(rsResultOrder.next())
            {
                DENo = Integer.parseInt(rsResultOrder.getString("SMARTFORMTODATAELEMENTS_intDataElementNo"));                
            }
            else
            {    
                DENo = 0;
            }
            
            rsResultOrder.close();
            
            // To build this smartforms dataelements
            query.setDomain("DATAELEMENTS", null, null, null); 
            query.setFields(vtBuildDataElements, null);
            query.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);                            
            query.setWhere("AND", 0, "DATAELEMENTS_intDataElementPoolID", "=", runtimeData.getParameter("DEPoolSelected"), 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("DATAELEMENTS_intDataElementOrder", "ASC");        
            
            String strNumber;
            
            ResultSet rsResult = query.executeSelect();
            
            
            while(rsResult.next())
            {
                String strId = rsResult.getString("DATAELEMENTS_intDataElementID");      
                                
                if (runtimeData.getParameter(strId) != null)
                {
                    
                    // Check if the DataElement Id is already existing in SmartformtoDataElements, if not add                    
                    DALSecurityQuery queryChk = new DALSecurityQuery(SMARTFORM_BUILD, authToken);                   
                    
                    queryChk.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);
                    queryChk.setFields(vtBuildSmartFormsDataElementsOnly, null);
                    queryChk.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);
                    queryChk.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementID", "=", strId, 0, DALQuery.WHERE_HAS_VALUE);
                    queryChk.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

                    ResultSet rsResultChk = queryChk.executeSelect();          
                    
                    // If the checked dataElement Id is not already added, then do insert
                    if (rsResultChk.next() == false)
                    {
                        // Get the max no in Dataelement Order column
                        int maxDataElementOrder = QueryChannel.getNewestKeyAsInt("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                        maxDataElementOrder = maxDataElementOrder + 1;                    

                        // Get the DataElement Number for this id
                         String strType = rsResult.getString("DATAELEMENTS_intDataElementType");

                        // Assign number according to the Type

                         // Comment
                         if (strType.equals("COMMENT"))
                         {
                            //strNumber = "101"; 
                             strNumber = "0"; 
                         }
                         // Title
                         else if (strType.equals("TITLE"))
                         {
                             strNumber = "0"; 
                         }
                         // Page Break
                         else if (strType.equals("PAGEBREAK"))
                         {
                             strNumber = "0"; 
                         }                     
                         else 
                         {     
                            DENo = DENo + 1; 
                            strNumber = new Integer(DENo).toString(); 
                         }                 
                         
                        //cut

                         //insert the dataelements selected
                        DALSecurityQuery query1 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);                      

                        query1.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);    

                        // Set the value for all other fields
                        query1.setField("SMARTFORMTODATAELEMENTS_intSmartformID",runtimeData.getParameter("SMARTFORM_intSmartformID"));
                        query1.setField("SMARTFORMTODATAELEMENTS_intDataElementID",strId);
                        query1.setField("SMARTFORMTODATAELEMENTS_intDataElementNo",strNumber);
                        query1.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder",new Integer(maxDataElementOrder).toString());

                        query1.executeInsert();  
                        //System.out.println("DENo3:"+DENo);
                        //System.out.println("strNumber3:"+strNumber);

                    }                                
                    
                    rsResultChk.close();
                }
                
                else // if checkbox is not clicked and save is clicked
                {
                   
                    // Check if the DataElement Id is already existing in SmartformtoDataElements, if existing then delete                   
                    DALSecurityQuery queryChk = new DALSecurityQuery(SMARTFORM_BUILD, authToken);  
                    
                    //Vector vtBuildSmartFormsDataElementsOnly = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElementsOnly");
                    
                    queryChk.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);
                    queryChk.setFields(vtBuildSmartFormsDataElementsOnly, null);
                    queryChk.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);
                    queryChk.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementID", "=", strId, 0, DALQuery.WHERE_HAS_VALUE);
                    queryChk.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

                    ResultSet rsResultChk = queryChk.executeSelect();          
                    
                    // If the checked dataElement Id is not already added, then do insert
                    if (rsResultChk.next() == true)
                    {
                        
                         //insert the dataelements selected
                        DALSecurityQuery query1 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);                      

                        query1.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);    
                       
                        // Set the value for all other fields
                        query1.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);
                        query1.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementID", "=", strId, 0, DALQuery.WHERE_HAS_VALUE);                  
                        query1.setField("SMARTFORMTODATAELEMENTS_intDataElementNo","");
                        query1.setField("SMARTFORMTODATAELEMENTS_intDeleted","-1");                  
                        query1.executeUpdate();
                        
                        //delete the smartform results
                        
	                        if(this.bldeleteResults)
	                        {	
		                        DALSecurityQuery query2 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);                      
		                        query2.setDomain("SMARTFORMPARTICIPANTS",null,null,null);
		                        query2.setField("SMARTFORMPARTICIPANTS_intSmartformParticipantID",null);
		                        query2.setWhere(null, 0, "SMARTFORMPARTICIPANTS_intSmartformID", "=", runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);
		                        query2.setWhere("AND",0, "SMARTFORMPARTICIPANTS_intDeleted", "=", "0" , 0, DALQuery.WHERE_HAS_VALUE);
		                        ResultSet rspartcpnt = query2.executeSelect();
		                        while(rspartcpnt.next())
		                        {
		                        	String strparticipant = rspartcpnt.getString("SMARTFORMPARTICIPANTS_intSmartformParticipantID");
		                        	DALSecurityQuery query3 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
		                        	query3.setDomain("SMARTFORMRESULTS",null, null,null);    
		                        	query3.setField("SMARTFORMRESULTS_intDeleted","-1"); 	                       
			                        // Set the value for all other fields
			                        query3.setWhere(null,0, "SMARTFORMRESULTS_intSmartformParticipantID", "=", strparticipant, 0, DALQuery.WHERE_HAS_VALUE);
		                        	query3.setWhere("AND", 0, "SMARTFORMRESULTS_intDataElementID", "=", strId, 0, DALQuery.WHERE_HAS_VALUE);                  
		                        	query3.setWhere("AND", 0, "SMARTFORMRESULTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
		                        	query3.executeUpdate();
		                        }
		                        rspartcpnt.close();
	                        }   
                        }                                
                    
                    rsResultChk.close();
                }
                
            }
            
            rsResult.close();  
            this.resetDEOrder(runtimeData.getParameter("SMARTFORM_intSmartformID"));
            // Save and call the same page again
             buildSmartForm();        
            strXML+="<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";     
          
           
        }
        catch (Exception e)
        {
            e.printStackTrace();
            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    /* To update the DataElement Order, when it is moved up & down in Build Smartform
     *
     *
     */
    private void ChangeDEOrder(String strSmartformId, String strSmartformsDataElementId, String strMove)
    {
        try
        {
            // Get the form fields to display all the labels and their corresponding fields
            Vector vtSearchSmartForm = DatabaseSchema.getFormFields("csmartformadmin_search_smartform");            
            Vector vtBuildDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElementPool");
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElements");
            Vector vtBuildSmartFormsDataElements1 = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElements");            
           
            String strSmartformsDataElementOrder = "";
            String strNewSmartformsDataElementId = "";
            String strNewSmartformsDataElementOrder = "";
            String strSmartformsDataElementNo = "";
            String strNewSmartformsDataElementNo = "";
            boolean blUpdate = false;
            
            Vector vtBuildSmartFormsDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElementsOnly");
            
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
            query.setDomain("SMARTFORMTODATAELEMENTS", null, null, null); 
            query.setFields(vtBuildSmartFormsDataElements, null);
            query.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", strSmartformId, 0, DALQuery.WHERE_HAS_VALUE);            
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);             
            query.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");            
            
            ResultSet rsResult = query.executeSelect();
            
            while(rsResult.next())
            {
                
                if(strSmartformsDataElementId.equals(rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementID")))
                {                  
                    
                    strSmartformsDataElementOrder = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");                   
                    strSmartformsDataElementNo = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo");              
                    
                    if (strMove.equals("moveUp"))
                    {                       
                        if (rsResult.isFirst() == false)
                        {
                            rsResult.previous();
                            
                            blUpdate = true;
                            strNewSmartformsDataElementId = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementID");
                            strNewSmartformsDataElementOrder = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                            strNewSmartformsDataElementNo = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo");                            
                            rsResult.next();
                        }
                        else
                        {
                            blUpdate = false;
                        }
                        
                        
                    }
                    else if (strMove.equals("moveDown"))
                    {
                        if(rsResult.isLast() == false)
                        {   
                            rsResult.next();
                            
                            blUpdate = true;
                            strNewSmartformsDataElementId = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementID");
                            strNewSmartformsDataElementOrder = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");   
                            strNewSmartformsDataElementNo = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo");
                            
                            rsResult.previous();
                        }
                        else
                        {
                            blUpdate = false;
                        }                  
                        
                    }
                    
                }
                
            }            
           
            rsResult.close();
            
            if (blUpdate == true)
            {               
                  
                // Update the passed DataElement Id with the new Order
                DALSecurityQuery queryUpd1 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                queryUpd1.setDomain("SMARTFORMTODATAELEMENTS", null, null, null); 
                
                queryUpd1.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", strSmartformId, 0, DALQuery.WHERE_HAS_VALUE);            
                queryUpd1.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                queryUpd1.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementID", "=", strSmartformsDataElementId, 0, DALQuery.WHERE_HAS_VALUE);                            
                queryUpd1.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder",strNewSmartformsDataElementOrder);
                // to check if the number is o, if not, swap the numbers also with the order
                if (strNewSmartformsDataElementNo.equals("0") && strSmartformsDataElementNo.equals("0") || strNewSmartformsDataElementNo.equals("0") && !strSmartformsDataElementNo.equals("0") || !strNewSmartformsDataElementNo.equals("0") && strSmartformsDataElementNo.equals("0"))
                {}
                else
                {                    
                    queryUpd1.setField("SMARTFORMTODATAELEMENTS_intDataElementNo",strNewSmartformsDataElementNo);                
                }
                queryUpd1.executeUpdate();    
                   

                 // Update the new DataElement Id with the passed DE id's Order
                DALSecurityQuery queryUpd2 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                queryUpd2.setDomain("SMARTFORMTODATAELEMENTS", null, null, null); 
                //queryUpd2.setFields(vtBuildSmartFormsDataElements, null);
                queryUpd2.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", strSmartformId, 0, DALQuery.WHERE_HAS_VALUE);            
                queryUpd2.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                queryUpd2.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementID", "=", strNewSmartformsDataElementId, 0, DALQuery.WHERE_HAS_VALUE);                            
                queryUpd2.setField("SMARTFORMTODATAELEMENTS_intDataElementOrder",strSmartformsDataElementOrder);
               
                if (strNewSmartformsDataElementNo.equals("0") && strSmartformsDataElementNo.equals("0") || strNewSmartformsDataElementNo.equals("0") && !strSmartformsDataElementNo.equals("0") || !strNewSmartformsDataElementNo.equals("0") && strSmartformsDataElementNo.equals("0"))
                {}
                else
                {
                     queryUpd2.setField("SMARTFORMTODATAELEMENTS_intDataElementNo",strSmartformsDataElementNo);                
                }
                queryUpd2.executeUpdate();                   

            }          
            
            String DEPoolSelected = runtimeData.getParameter("DEPoolSelected");
            
            buildSmartForm();
             
             runtimeData.setParameter("DEPoolSelected",DEPoolSelected);
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    
     private void resetDEOrder(String strSmartformId)
    {
        try
        {
            int intDENo = 1;
            // Get the form fields to display all the labels and their corresponding fields
            Vector vtSearchSmartForm = DatabaseSchema.getFormFields("csmartformadmin_search_smartform");            
            Vector vtBuildDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElementPool");
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElements");
            Vector vtBuildSmartFormsDataElements1 = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElements");            
           

            String strSmartformsDataElementId = "";


            
            Vector vtBuildSmartFormsDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElementsOnly");
            
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
            query.setDomain("SMARTFORMTODATAELEMENTS", null, null, null); 
            query.setFields(vtBuildSmartFormsDataElements, null);
            query.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", strSmartformId, 0, DALQuery.WHERE_HAS_VALUE);            
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);             
            query.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementNo", "ASC");            
            
            ResultSet rsResult = query.executeSelect();
            
            while(rsResult.next())
            {
                String strSmartformsDataElementOrder = new String();
                String strSmartformsDataElementNo = new String();
                
                if(!strSmartformsDataElementId.equals(rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementID")))
                {                  
                    strSmartformsDataElementId = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementID");
                    int intExistingDataElementNo = new Integer(rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementNo")).intValue();              
                    if(intExistingDataElementNo > 0)
                    {
                        strSmartformsDataElementNo = new Integer(intDENo).toString();
                        strSmartformsDataElementOrder = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");
                        strSmartformsDataElementId = rsResult.getString("SMARTFORMTODATAELEMENTS_intDataElementID");
                        
                        //updating the DB
                        
                        DALSecurityQuery queryUpd1 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                        queryUpd1.setDomain("SMARTFORMTODATAELEMENTS", null, null, null); 

                        queryUpd1.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", strSmartformId, 0, DALQuery.WHERE_HAS_VALUE);            
                        queryUpd1.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                        queryUpd1.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementID", "=", strSmartformsDataElementId, 0, DALQuery.WHERE_HAS_VALUE);                            
                        if(!strSmartformsDataElementNo.equals("0") && new Integer(strSmartformsDataElementNo).intValue() > 0)
                        {
                            queryUpd1.setField("SMARTFORMTODATAELEMENTS_intDataElementNo",strSmartformsDataElementNo);                
                        }
                        queryUpd1.executeUpdate();    
                        intDENo++;
                    }
                }
            }            
           
            rsResult.close();
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    /* To delete the Smartforms dataelement in Build Smartform
     *
     *
     */
    private void deleteSmartformsDataElement()
    {
        try
        {
            
            strCurrent = BUILD_SMARTFORM;
            
            // Get the form fields to display all the labels and their corresponding fields            
            Vector vtBuildSmartFormsDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElementsOnly");
            
            updateNoAndOrderBeforeDelete();
            
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
            query.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);                  
            query.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);                
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementID", "=",runtimeData.getParameter("SMARTFORMTODATAELEMENTS_DEIdSelected") , 0, DALQuery.WHERE_HAS_VALUE);                            
            query.setField("SMARTFORMTODATAELEMENTS_intDeleted", "-1");
           
            query.executeUpdate();
            
            buildSmartForm();
            
            // To clear the deleted id's value in the hidden field
            int startPos = strXML.indexOf("<SMARTFORMTODATAELEMENTS_DEIdSelected>",0);                        
            strXML = strXML.substring(0,startPos);   

            String setVar = null;
            strXML+="<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";
            strXML+="<SMARTFORMTODATAELEMENTS_DEIdSelected>"+setVar+"</SMARTFORMTODATAELEMENTS_DEIdSelected>";            

            runtimeData.setParameter("SMARTFORMTODATAELEMENTS_DEIdSelected",strXML);
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
   
     /* To build a Smartform with Dataelements
     *
     *
     */
    private void buildAddEditDataElements()
    {
        try
        {
            
            //System.out.println("-------------------------------------------------------------");
            //System.out.println("DATAELEMENTS_intDataElementIDselected:"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"));
            //System.out.println("DATAELEMENTS_intDataElementType:"+runtimeData.getParameter("DATAELEMENTS_intDataElementType"));
            //System.out.println("strmode:"+runtimeData.getParameter("strMode"));
            //System.out.println("Default SmartformID:"+runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultSmartformID"));
            //System.out.println("Default DE ID:"+runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultDataElementID"));
            //System.out.println("target:"+runtimeData.getParameter("target"));
            //System.out.println("edit where id:"+runtimeData.getParameter("SYSTEMLOOKUPWHERE_intDataElementLookupWhereID"));
           
            
             // Get the form fields to display all the labels and their corresponding fields
            Vector vtSearchSmartForm = DatabaseSchema.getFormFields("csmartformadmin_search_smartform");            
            Vector vtBuildDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElementPool");
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_viewDataElements");
            Vector vtBuildSmartFormsDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElements");
            Vector vtBuildSmartFormDataElementsOnly = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElementsOnly");
            Vector vtBuildListofSmartForms = DatabaseSchema.getFormFields("csmartformadmin_build_ListofSmartforms");
            Vector vtBuildSmartformsDataElementsView = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_viewDataElements");
            Vector vtBuildLookupWhere = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_SystemLookupWhere");
            Vector vtViewLookupWhere = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_ViewSystemLookupWhere");            
            Vector vtBuildLookupField = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_SystemLookupField");            
            Vector vtLookupFieldRuntimeData = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_SystemLookupFieldRuntimeData");          
            Vector vtViewSmartForm = DatabaseSchema.getFormFields("csmartformadmin_view_smartform");
            Vector vtBuildSmartform = DatabaseSchema.getFormFields("csmartformadmin_build_smartform"); 
            Vector vtViewDataElements = DatabaseSchema.getFormFields("csmartformadmin_view_DataElements");
            
            
            String strDataElementPoolID="";  
            String strSmartformID = runtimeData.getParameter("SMARTFORM_intSmartformID");  
            //System.out.println("====================================================strSmartformID=== :"+strSmartformID);
            //String strDataElementID = "";
            String strDataElementType = "";
            String strDefaultSmartformID = "";
            String strDefaultDataElementID = "";
            String strLookupTypeSel = "";

            strCurrent = ADDEDIT_DATAELEMENTS;

            // To Load the DataElement Pool names in the dropdown
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
            query.setDomain("DATAELEMENTPOOL", null, null, null); 
            query.setFields(vtBuildDataElementPool, null);
            query.setWhere(null, 0, "DATAELEMENTPOOL_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);                            
            query.setOrderBy("DATAELEMENTPOOL_strDataElementPoolName", "ASC");
            ResultSet rsResult = query.executeSelect();               

            //************************************
            if (blFirst == true)
            {
                //System.out.println("first time");
                blFirst = false;
                //************************************************
                // Start lock        
                //System.out.println("setting all depools to readonly");
                rsResult.beforeFirst();
                clearLockRequestDEPool();
                lockRequestDEPool = new LockRequest(authToken);
                // register the lock to session manager
                SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartformAdmin", lockRequestDEPool);

                while(rsResult.next())
                {
                    if (rsResult.getString("DATAELEMENTPOOL_intDataElementPoolID") != null)
                    {             
                        lockRequestDEPool.addLock("DATAELEMENTPOOL", rsResult.getString("DATAELEMENTPOOL_intDataElementPoolID"), LockRecord.READ_ONLY);
                        
                    }

                }
                rsResult.beforeFirst();
                lockRequestDEPool.lockDelayWrite();

                // End lock
             } 

            
            //**************************************     
            

             // To Load all the dataelements, when a dataelement pool is selected
            DALSecurityQuery query1 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);    
            query1.setDomain("DATAELEMENTS", null, null, null); 
            query1.setFields(vtBuildSmartformsDataElementsView, null);
            query1.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);     
            if((runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID") != null) && !(runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID").equals("")))            
            {               
                 strDataElementPoolID = runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID");                   
                //query1.setWhere("AND", 0, "DATAELEMENTS_intDataElementPoolID", "=", runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID"), 0, DALQuery.WHERE_HAS_VALUE);
                //System.out.println("1:"+strDataElementPoolID);

            }      
            if(runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID") == null || runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID").equals(""))            
            {         
                strDataElementPoolID = runtimeData.getParameter("DEPoolSelected");       
                //query1.setWhere("AND", 0, "DATAELEMENTS_intDataElementPoolID", "=", runtimeData.getParameter("DEPoolSelected"), 0, DALQuery.WHERE_HAS_VALUE);
                //System.out.println("2:"+strDataElementPoolID);

            }
            // If the DE Pool wasnt selected in build smartform, then load the first de pool
            if(strDataElementPoolID == null || strDataElementPoolID.equals("null"))
            {
                 if(rsResult.next())
                  {
                      strDataElementPoolID = rsResult.getString("DATAELEMENTPOOL_intDataElementPoolID");
                      rsResult.beforeFirst();
                  }
                
            }
            
            query1.setWhere("AND", 0, "DATAELEMENTS_intDataElementPoolID", "=", strDataElementPoolID, 0, DALQuery.WHERE_HAS_VALUE);             
            query1.setOrderBy("DATAELEMENTS_intDataElementOrder", "ASC");
            ResultSet rsResult1 = query1.executeSelect();
            
            //************************************
            // Start Lock

            //System.out.println("set depool to read write");
            if (strDataElementPoolID != null)
            {    
                //System.out.println("setting selected depool to readwrite");
                clearLockRequestDEPool();
                lockRequestDEPool = new LockRequest(authToken);
                // register the lock to session manager
                SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartformAdmin", lockRequestDEPool);
                lockRequestDEPool.addLock("DATAELEMENTPOOL", strDataElementPoolID , LockRecord.READ_WRITE);
                lockRequestDEPool.lockDelayWrite();
            }


            // End lock
            //**************************************  


            //System.out.println("DATAELEMENTS_intDataElementID:"+runtimeData.getParameter("DATAELEMENTS_intDataElementID"));             
            //System.out.println("DATAELEMENTS_intDataElementType"+runtimeData.getParameter("DATAELEMENTS_intDataElementType"));            
            // if the dataelement is selected, load the list of smartforms using this dataelement           
             if (runtimeData.getParameter("DATAELEMENTS_intDataElementID") != null)
             {
                strDataElementID = runtimeData.getParameter("DATAELEMENTS_intDataElementID");
             }
             else
             {
                 strDataElementID = runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected");
             }     
            // To get the Smartforms using the selected Dataelement
            DALSecurityQuery queryJoin = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
            queryJoin.setDomain("SMARTFORMTODATAELEMENTS", null, null, null); 
            queryJoin.setDomain("SMARTFORM","SMARTFORMTODATAELEMENTS_intSmartformID","SMARTFORM_intSmartformID","INNER JOIN");
            queryJoin.setFields(vtBuildListofSmartForms, null);    
            queryJoin.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);            
            queryJoin.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            queryJoin.setOrderBy("SMARTFORMTODATAELEMENTS_intSmartformID", "ASC");
            
            ResultSet rsResultJoin = queryJoin.executeSelect();
            
            
            
            // if dataelement type is selected, load the dropdown with the last selected type
            if (runtimeData.getParameter("DATAELEMENTS_intDataElementType") != null)
            {
                strDataElementType = runtimeData.getParameter("DATAELEMENTS_intDataElementType");
                //System.out.println("strDataElementType 1: "+strDataElementType);
            }            
            else
            //if(runtimeData.getParameter("DATAELEMENTS_intDataElementType") == null || runtimeData.getParameter("DATAELEMENTS_intDataElementType").equals(""))            
            {
                 strDataElementType = runtimeData.getParameter("DATAELEMENTS_intDataElementTypeSelected");
                 //System.out.println("DataElementType 2: "+strDataElementType);
            }                      
       
            
            
            // DEFAULT EXISTING SMARTFORM & DATAELEMENT ID'S
            // To load all the Smartform names and the DataElement Id's for each Smartform ID for Default Existing option           
            DALSecurityQuery queryDefault = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
            queryDefault.setDomain("SMARTFORMTODATAELEMENTS", null, null, null); 
            queryDefault.setDomain("SMARTFORM","SMARTFORMTODATAELEMENTS_intSmartformID","SMARTFORM_intSmartformID","INNER JOIN");
            queryDefault.setFields(vtBuildListofSmartForms, null); 
            queryDefault.setDistinctField("SMARTFORM_intSmartformID");
            queryDefault.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            queryDefault.setOrderBy("SMARTFORMTODATAELEMENTS_intSmartformID", "ASC");            
            //System.out.println("*************************************************************************");
            ResultSet rsResultDefault = queryDefault.executeSelect();     
            
            // to get the value of Default existing smartform id selected
            if(runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultSmartformID") != null)  
            {                
                strDefaultSmartformID = runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultSmartformID"); 

                //System.out.println("def sfid is not blank:"+strDefaultSmartformID);

            }      
             // First time when the Smartform Id is not selected, to load the dataElement Id's for the first Smartform Id
            //if (runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultSmartformID") == null || runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultSmartformID").equals(""))
            else
            {      
                strDefaultSmartformID = runtimeData.getParameter("DE_DefaultSmartformIDSelected ");
                //System.out.println("def sfid is blank from hidden 1:"+strDefaultSmartformID);

                 // if it is first time, load dataelements for the first Smartform Id
                 if (strDefaultSmartformID == null)
                 {
                    if(rsResultDefault.next())
                    {
                        strDefaultSmartformID = rsResultDefault.getString("SMARTFORM_intSmartformID");
                        //System.out.println("strDefaultSmartformId:"+strDefaultSmartformID);
                        rsResultDefault.beforeFirst(); 
                    }
                 }
                //System.out.println("def sfid is blank from hidden 2:"+strDefaultSmartformID);
            }

            
            
            //System.out.println("strDataElementType:"+strDataElementType);
           
            // When DE is clicked for editing, DEtype is null, so get the DE type for this DE Id
            String strDEType = "";
            if (strDataElementType == null)
            {
                DALSecurityQuery queryT = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                queryT.setDomain("DATAELEMENTS", null, null, null);                
                queryT.setFields(vtBuildSmartformsDataElementsView, null);          
                queryT.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);                                                         
                queryT.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);         
                ResultSet rsResultT = queryT.executeSelect();  
                
                while(rsResultT.next())
                {
                    strDEType = rsResultT.getString("DATAELEMENTS_intDataElementType");
                }
                //System.out.println("strDataElementType after:"+strDEType);
                rsResultT.close();
                
            }            
            // new mode first time
            else if( runtimeData.getParameter("addDataElement") != null)
            {
                strDEType = "TEXT";
            }            
            else //in New mode or when page is reloaded
            {
                 strDEType = strDataElementType;
            }
            
             // To Load this Smartform's Dataelements whose type is same as the edited DEId's type
            DALSecurityQuery queryDE = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
            queryDE.setDomain("SMARTFORMTODATAELEMENTS", null, null, null); 
            queryDE.setDomain("DATAELEMENTS","SMARTFORMTODATAELEMENTS_intDataElementID","DATAELEMENTS_intDataElementID","INNER JOIN");
            queryDE.setFields(vtBuildSmartFormsDataElements, null);         
            queryDE.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", strDefaultSmartformID, 0, DALQuery.WHERE_HAS_VALUE);                                             
            queryDE.setWhere("AND", 0, "DATAELEMENTS_intDataElementType", "LIKE", strDEType, 0, DALQuery.WHERE_HAS_VALUE);            
            queryDE.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            queryDE.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");
            //System.out.println("*************************************************************************");
            ResultSet rsResultDE = queryDE.executeSelect();           
               
            // To assign the SmartformId from the default SF id field or from the hidden field accordingly
             if (runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultDataElementID") != null )           
             {
                 strDefaultDataElementID = runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultDataElementID");
                 //System.out.println("Def DE id2: "+strDefaultDataElementID);
             }  
            // if dataelement id is selected, load the dropdown with the last selected type
            //if (runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultDataElementID") == null || runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultDataElementID").equals(""))            
             else
             {                 
                strDefaultDataElementID = runtimeData.getParameter("DE_DefaultDataElementIDSelected");
                //System.out.println("Def DE id hidden 1: "+strDefaultDataElementID);

                  // if it is loaded for first time
                 if (strDefaultDataElementID == null)
                //if (runtimeData.getParameter("DE_DefaultSmartformIDSelected ") == null)
                 {
                     //System.out.println("strDefaultDEID is null");
                      if(rsResultDE.first())
                       {
                            strDefaultDataElementID = rsResultDE.getString("SMARTFORMTODATAELEMENTS_intDataElementID");
                            //System.out.println("strDefaultDataElementID:"+strDefaultDataElementID);
                            rsResultDE.beforeFirst();   
                       }
                 }

                //System.out.println("Def dE id hidden 2: "+strDefaultDataElementID);
             }        
           
            // from    
            
            // Build the strXML
            strXML = QueryChannel.buildFormLabelXMLFile(vtSearchSmartForm) +
                     QueryChannel.buildViewFromKeyXMLFile(vtSearchSmartForm,"SMARTFORM_intSmartformID",strSmartformID) +           
                     QueryChannel.buildFormLabelXMLFile(vtBuildDataElementPool) +  
                     QueryChannel.buildSearchXMLFile("buildsmartform_DataElementPool", rsResult,vtBuildDataElementPool) +
                     QueryChannel.buildFormLabelXMLFile(vtBuildSmartformsDataElementsView) +
                     QueryChannel.buildSearchXMLFile("buildsmartform_DataElements", rsResult1,vtBuildSmartformsDataElementsView) +               
                     "<DEPoolSelected>"+ strDataElementPoolID +"</DEPoolSelected>" +
                     QueryChannel.buildFormLabelXMLFile(vtBuildLookupWhere) +
                     //QueryChannel.buildSearchXMLFile("buildLookupWhere", rsResultWhere,vtBuildLookupWhere) +
                     QueryChannel.buildFormLabelXMLFile(vtBuildLookupField); 
             rsResult.close();
             rsResult1.close();
            
             // If delete Where is clicked
             if (runtimeData.getParameter("target") != null && runtimeData.getParameter("target").equals("deleteWhere"))
             {                 
                deleteWhere();                
             }
            
            // if Edit Where is clicked, load the details of this LookupWhereId in their respective dropdowns
             if (runtimeData.getParameter("target") != null && runtimeData.getParameter("target").equals("editWhere"))
             {
                 //System.out.println("target is editWhere");
                buildEditWhere();         
             }        
            
            // to Load all the System lookup Types
            loadLookupTypes(); 
             
            // to get the current value of SystemLookupType(eg.Investigators) dropdown
             if (runtimeData.getParameter("DATAELEMENTS_strDataElementLookupType") != null)
             {
                strLookupTypeSel = runtimeData.getParameter("DATAELEMENTS_strDataElementLookupType");
                //System.out.println("strLookuptype 1: "+strLookupTypeSel);
             }
             else
             {
                 strLookupTypeSel = runtimeData.getParameter("DELookupTypeSelected");
                 //System.out.println("strLookupType 2: "+strLookupTypeSel);
                 
                 // When page reloads or when dataElement type is changed to system lookup type if lookuptype is null
                 if (strLookupTypeSel == null || strLookupTypeSel.equals(""))
                 {  
                      // To set the first lookup Type as strLookupTypeSel
                      Hashtable hashLookupObjsAll = DatabaseSchema.getLookupObjects();
                      Enumeration enumFirst = hashLookupObjsAll.keys();  
                      if (enumFirst.hasMoreElements())
                      {                         
                            strLookupTypeSel =  (String)enumFirst.nextElement(); 
                            //System.out.println("first lookuptype:"+strLookupTypeSel);
                      }                     
                 }
             }    
            
             
          // to
            
           //** To get Date radio buttons value
             if (runtimeData.getParameter("rdDefaultorExisting") != null)
             {
                strRadioVal = runtimeData.getParameter("rdDefaultorExisting");
                //System.out.println("strRadioVal 1: "+strRadioVal);
             }
             else
             {
                strRadioVal = runtimeData.getParameter("strRadioVal");
                //System.out.println("strRadioVal 2: "+strRadioVal);
             }      
             //**

            
            
            if (runtimeData.getParameter("addDataElement") != null)
            {
                strDataElementID = "null"; 
                strDataElementType = "TEXT";
                strDefaultSmartformID = "null";
                strDefaultDataElementID = "null";   
                strRadioVal = "";
                
            }
            else if (runtimeData.getParameter("addTitleComment") != null)
            {
                strDataElementID = "null"; 
                strDataElementType = "TITLE";
                strDefaultSmartformID = "null";
                strDefaultDataElementID = "null";                             
            }
            else if (runtimeData.getParameter("addPageBreak") != null)
            {
                strDataElementID = "null";   
                strDefaultSmartformID = "null";
                strDefaultDataElementID = "null";               
            }          
              
              
             // To load the Smartforms ID's and DataElement ID's
             strXML+= QueryChannel.buildFormLabelXMLFile(vtBuildListofSmartForms) +
                      QueryChannel.buildSearchXMLFile("LookupExistingSmartformNames",rsResultDefault,vtBuildListofSmartForms) +
                      QueryChannel.buildFormLabelXMLFile(vtBuildSmartFormsDataElements);
                      
            
              
              // When it is any mode except first click of dataelement in edit mode
              if(runtimeData.getParameter("DATAELEMENTS_intDataElementTypeSelected") != null )
              {
                    
                  //System.out.println("de is not clicked for editing");
                  strXML+=  QueryChannel.buildSearchXMLFile("LookupExistingDataElementId",rsResultDE,vtBuildSmartFormsDataElements) +
                            "<DE_DefaultSmartformIDSelected>"+strDefaultSmartformID+"</DE_DefaultSmartformIDSelected>" +
                           "<DE_DefaultDataElementIDSelected>"+strDefaultDataElementID+"</DE_DefaultDataElementIDSelected>";                      
                  
                 
                  
                  // Add the current lookuptype's value
                  strXML+= "<DELookupTypeSelected>"+strLookupTypeSel+"</DELookupTypeSelected>";
                  
                  
                   //**
                   // get the lookup type for this DataElement Id
                   DALSecurityQuery queryType = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                   queryType.setDomain("DATAELEMENTS", null, null, null);            
                   queryType.setFields(vtBuildDataElements, null);     
                   queryType.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                   queryType.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);                         

                   ResultSet rsResultType = queryType.executeSelect();     


                    if (rsResultType.next())
                    {   
                        String typeResult = "";
                        typeResult = rsResultType.getString("DATAELEMENTS_strDataElementLookupType");
                        if( typeResult != null && typeResult.equals(strLookupTypeSel))
                        {
                             // In System Lookup type, to display the Where which was added, in the form of label
                            DALSecurityQuery queryWhere = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                            queryWhere.setDomain("SYSTEMLOOKUPWHERE", null, null, null);            
                            queryWhere.setFields(vtBuildLookupWhere, null);     
                            queryWhere.setWhere(null, 0, "SYSTEMLOOKUPWHERE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                            queryWhere.setWhere("AND", 0, "SYSTEMLOOKUPWHERE_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);
                            if (runtimeData.getParameter("target") != null)
                            {
                                //System.out.println("target is not null");
                                // if Edit where is clicked, this particular where condition should not be loaded as label
                                queryWhere.setWhere("AND", 0, "SYSTEMLOOKUPWHERE_intDataElementLookupWhereID", "<>", runtimeData.getParameter("SYSTEMLOOKUPWHERE_intDataElementLookupWhereID"), 0, DALQuery.WHERE_HAS_VALUE);

                            }
                            queryWhere.setOrderBy("SYSTEMLOOKUPWHERE_strOrder", "ASC");      
                            ResultSet rsResultWhere = queryWhere.executeSelect();   

                            // Display the lookuptype's where as labels
                            strXML+= QueryChannel.buildSearchXMLFile("buildLookupWhere", rsResultWhere,vtBuildLookupWhere);
                            
                            rsResultWhere.close();
                        }
                    }
                   
                   rsResultType.close();
                   
                    //**
                    // Display the lookuptype's where as labels
                    //strXML+= QueryChannel.buildSearchXMLFile("buildLookupWhere", rsResultWhere,vtBuildLookupWhere);
                  
                    // Load all the field names for the selected lookupType
                    loadLookupObjsFieldNames(strLookupTypeSel);
                  
                    //** To get radio buttons value
                    //System.out.println("strRadioVal in reload or first:"+strRadioVal);
                    strXML+= "<strRadioVal>"+strRadioVal+"</strRadioVal>";
                    //**         

              }
             
             //CUT
             strXML+= "<DATAELEMENTS_intDataElementTypeSelected>"+ strDataElementType +"</DATAELEMENTS_intDataElementTypeSelected>" +
                      "<DATAELEMENTS_intDataElementIDselected>"+ strDataElementID +"</DATAELEMENTS_intDataElementIDselected>";
             
             
             
             String DefaultSFClicked = "";
             
             if(runtimeData.getParameter("DynamicSmartform") != null){
                 if(runtimeData.getParameter("DynamicSmartform").equals("")){
                     runtimeData.setParameter("DynamicSmartform",(String)null);
                 } 
             }
             
             //System.out.println("dynamic smartfomr is: "+runtimeData.getParameter("DynamicSmartform"));
             if (runtimeData.getParameter("DynamicSmartform") != null)
             {                 
                 strXML+= "<DynamicSmartform>true</DynamicSmartform>";
                 //System.out.println("dynamic true");  
                 //int DEId = Integer.parseInt(strDataElementID);
                 strFlag = "error";
                 //DefaultSFClicked = "yes";
                 //if Add mode
                 if(runtimeData.getParameter("strMode").equals("newDEClicked"))
                 {
                     strXML+= QueryChannel.buildViewXMLFile(vtViewDataElements, runtimeData);      
                 }
                 else
                 {
                    strXML+= QueryChannel.buildViewXMLFile(vtBuildSmartformsDataElementsView, runtimeData);      
                 }
                 //System.out.println("********************");
             }
            
              
             //System.out.println("strFlag:"+strFlag);
             
             // if there is no error in saveDataElementinAddEditDE(), build view from key, else build view from runtimedata in saveDataElementinAddEditDE()
             if(strFlag.equals(""))
             {
                  //System.out.println("dynamic not true");
                  strXML+= QueryChannel.buildViewFromKeyXMLFile(vtBuildSmartformsDataElementsView,"DATAELEMENTS_intDataElementID",strDataElementID);             
             }    
             
              //  To reset strFlag only if previously DefaultExistingSmartform was changed
              if(runtimeData.getParameter("DynamicSmartform") != null)
             {
                 strFlag = "";
             }
             
              // EDIT: When dataelement is clicked for editing            
              if(runtimeData.getParameter("DATAELEMENTS_intDataElementTypeSelected") == null )
              {
                   //System.out.println("get out");
                   //System.out.println("de is clicked for editing");
                   //System.out.println("DATAELEMENTS_intDataElementIDselected:"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"));
                    
                    Vector vtResults = new Vector();
                    Hashtable htResults = new Hashtable();
                    String strCurrentData = "";
                  
                    DALSecurityQuery queryVal = new DALSecurityQuery(SMARTFORM_BUILD, authToken);    
                    queryVal.setDomain("DATAELEMENTS", null, null, null); 
                    queryVal.setFields(vtBuildDataElements, null);
                    queryVal.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);     
                    queryVal.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);                                           

                    ResultSet rsResultVal = queryVal.executeSelect();

                    if (rsResultVal.next())
                    {
                        for (int i = 0; i < vtBuildDataElements.size(); i++)
                        {
                            strCurrentData = rsResultVal.getString((String) vtBuildDataElements.get(i));
                            if (strCurrentData != null)
                            {
                                htResults.put(vtBuildDataElements.get(i), strCurrentData);
                            }
                        }
                        vtResults.add(htResults);
                    }
                    
                    rsResultVal.close();
                    
                  
                    for (int i = 0; i < vtResults.size(); i++)
                    {
                        // get the lookuptype for this dataElement id
                        strLookupTypeSel = (String) ((Hashtable) vtResults.get(i)).get("DATAELEMENTS_strDataElementLookupType");
                         
                        // get the default Smartform & dataElement id, for this dataELement id
                        strDefaultSmartformID = (String) ((Hashtable) vtResults.get(i)).get("DATAELEMENTS_intDataElementDefaultSmartformID");
                        strDefaultDataElementID = (String) ((Hashtable) vtResults.get(i)).get("DATAELEMENTS_intDataElementDefaultDataElementID");
                       
                        
                        if (strDefaultDataElementID == null)
                        {
                            rsResultDefault.beforeFirst();
                             if(rsResultDefault.next())
                             {
                                strDefaultSmartformID = rsResultDefault.getString("SMARTFORM_intSmartformID");
                                //System.out.println("strDefaultSmartformId:"+strDefaultSmartformID);
                             } 
                             rsResultDefault.beforeFirst();
                            
                             rsResultDE.beforeFirst();
                             if(rsResultDE.next())
                             {
                                strDefaultDataElementID = rsResultDE.getString("SMARTFORMTODATAELEMENTS_intDataElementID");
                             }
                             rsResultDE.beforeFirst();  
                            
                         }
                         //System.out.println("de SFid from buildviewfromkey:"+rsResultVal.getString("DATAELEMENTS_intDataElementDefaultSmartformID"));
                         //System.out.println("de SFid from buildviewfromkey:"+strDefaultSmartformID);
                            
                            
                         // if in Edit mode, dataElement type is changed to System lookup type from some other type, to load the field names for the first lookup type
                         if (strLookupTypeSel == null)
                         {  
                              // To set the first lookup Type as strLookupTypeSel
                              Hashtable hashLookupObjsAll = DatabaseSchema.getLookupObjects();
                              Enumeration enumFirst = hashLookupObjsAll.keys();  
                              if (enumFirst.hasMoreElements())
                              {                         
                                 strLookupTypeSel =  (String)enumFirst.nextElement(); 
                                 //System.out.println("first lookuptype in edit:"+strLookupTypeSel);
                              }

                          }

                          //runtimeData.setParameter("DE_DefaultSmartformIDSelected",runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultSmartformID"));
                          strXML+= "<DE_DefaultSmartformIDSelected>"+strDefaultSmartformID+"</DE_DefaultSmartformIDSelected>" +
                                   "<DE_DefaultDataElementIDSelected>"+strDefaultDataElementID+"</DE_DefaultDataElementIDSelected>" +
                                   "<DELookupTypeSelected>"+strLookupTypeSel+"</DELookupTypeSelected>";                      

                          // Load all the field names for the selected lookupType
                          loadLookupObjsFieldNames(strLookupTypeSel);

                          //**
                          //System.out.println("db val:"+rsResultVal.getString("DATAELEMENTS_strDataElementDefaultTodaysDate"));
                          if ((String) ((Hashtable) vtResults.get(i)).get("DATAELEMENTS_strDataElementDefaultTodaysDate") != (String)null)
                          {
                             strRadioVal = "DATAELEMENTS_strDataElementDefaultTodaysDate";
                             //System.out.println("strRadioVal 1 edit:"+strRadioVal);
                          }
                          else if((String) ((Hashtable) vtResults.get(i)).get("DATAELEMENTS_dtDataElementDefaultDate") != (String)null)
                          {
                              strRadioVal = "DATAELEMENTS_dtDataElementDefaultDate";
                              //System.out.println("strRadioVal 2 edit:"+strRadioVal);
                          }
                          else if((String) ((Hashtable) vtResults.get(i)).get("DATAELEMENTS_strDataElementDefaultWorkingDaysOperator") != (String)null)
                          {
                              strRadioVal = "DATAELEMENTS_dtDataElementTodaysDatePlusWorkingDays";
                              //System.out.println("strRadioVal 3 edit:"+strRadioVal);
                          }
                          else if((String) ((Hashtable) vtResults.get(i)).get("DATAELEMENTS_strDataElementDefault") != (String)null)
                          {
                              strRadioVal = "rdDefault";
                              //System.out.println("strRadioVal 4 edit:"+strRadioVal);
                          }
                          else if((String) ((Hashtable) vtResults.get(i)).get("DATAELEMENTS_intDataElementDefaultSmartformID") != (String)null)
                          {
                              strRadioVal = "rdExisting";
                              //System.out.println("strRadioVal 5 edit:"+strRadioVal);
                          }
                          else
                          {
                              strRadioVal = "";
                          }
                          //System.out.println("strRadioVal in edit:"+strRadioVal);
                          strXML+= "<strRadioVal>"+strRadioVal+"</strRadioVal>";
                          //**
                          
                    }         
                    
                    
                    rsResultDE.close();
                    
                    DALSecurityQuery queryLd = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                    queryLd.setDomain("SMARTFORMTODATAELEMENTS", null, null, null); 
                    queryLd.setDomain("DATAELEMENTS","SMARTFORMTODATAELEMENTS_intDataElementID","DATAELEMENTS_intDataElementID","INNER JOIN");
                    queryLd.setFields(vtBuildSmartFormsDataElements, null);                 

                    queryLd.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", strDefaultSmartformID, 0, DALQuery.WHERE_HAS_VALUE);                                             
                    queryLd.setWhere("AND", 0, "DATAELEMENTS_intDataElementType", "LIKE", strDEType, 0, DALQuery.WHERE_HAS_VALUE);            
                    queryLd.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    queryLd.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder", "ASC");            

                    ResultSet rsResultLd = queryLd.executeSelect();

                    strXML+= QueryChannel.buildSearchXMLFile("LookupExistingDataElementId",rsResultLd,vtBuildSmartFormsDataElements) ;                  
                    rsResultLd.close();
                   
                    //**
                    // In System Lookup type, to display the Where which was added, in the form of label
                    DALSecurityQuery queryWhere = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                    queryWhere.setDomain("SYSTEMLOOKUPWHERE", null, null, null);            
                    queryWhere.setFields(vtBuildLookupWhere, null);     
                    queryWhere.setWhere(null, 0, "SYSTEMLOOKUPWHERE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                    queryWhere.setWhere("AND", 0, "SYSTEMLOOKUPWHERE_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);                    
                    queryWhere.setOrderBy("SYSTEMLOOKUPWHERE_strOrder", "ASC");      
                    ResultSet rsResultWhere1 = queryWhere.executeSelect();   
                    
                    strXML+= QueryChannel.buildSearchXMLFile("buildLookupWhere", rsResultWhere1,vtBuildLookupWhere);
                    //**                    
                    
                    rsResultWhere1.close();
               }            
              
              
              //System.out.println("strXML:"+strXML);
              //System.out.println("outside if strDataElementID:"+strDataElementID);
              // When the Data Element type is changed to Date and DefaultExistingSmartform not changed
              if (runtimeData.getParameter("DynamicSmartform") != null)
              {}
              else
              {
                  if (strDataElementID == null || strDataElementID.equals("null"))
                  {
                      //System.out.println("inside if");
                      // To load the date dropdowns with the current date 
                      strXML+= QueryChannel.buildDateDropDownXMLFile("DATAELEMENTS_dtDataElementDateMin",null) +
                               QueryChannel.buildDateDropDownXMLFile("DATAELEMENTS_dtDataElementDateMax",null) +
                               QueryChannel.buildDateDropDownXMLFile("DATAELEMENTS_dtDataElementDefaultDate",null);                           

                  }           
              }
              // To display the list of Smartforms using this dataelement
              strXML+=  QueryChannel.buildFormLabelXMLFile(vtBuildListofSmartForms) +
                        QueryChannel.buildSearchXMLFile("smartform_list", rsResultJoin, vtBuildListofSmartForms);
             
               rsResultJoin.close();
              
               if (runtimeData.getParameter("DynamicSmartform") != null)
              {
                  //System.out.println("before for");
                  String internalName = "";
                  String fieldOrder = "";
                  String strIntlName = "";
                  
                   if(runtimeData.getParameter("DATAELEMENTS_intDataElementType").equals("SYSTEM LOOKUP"))
                   { 
                       // get all internal names for this de id
                                             
                       //Vector vtIntName = new Vector();
                       Vector vtIntName = DatabaseSchema.getLookupObjectFields(strLookupTypeSel);              
                       
                      //System.out.println("type is system lookup");                                            
                       for(int i=0; i< vtIntName.size();i++){         
                         
                         LookupObjectField obj = (LookupObjectField)vtIntName.elementAt(i);
 
                         //String strK = (String)e.nextElement();
                         String strK = obj.getName();
                         //System.out.println("inside:"+strK);                         
                         
                         strIntlName = "SYSTEMLOOKUPFIELD_strInternalName";                         
                         internalName = strK;
                         //System.out.println("internalName:"+internalName);
                         //System.out.println("internalName getparam:"+runtimeData.getParameter(internalName));
                         fieldOrder = "SYSTEMLOOKUPFIELD_strFieldOrder" + strK;
                         //System.out.println("Order dropdown: "+fieldOrder);
                         //System.out.println("Order dropdown runtimedata : "+runtimeData.getParameter(fieldOrder));
                         
                         // Additional Info Start
                         String display = "SYSTEMLOOKUPFIELD_strInDisplayAddnl";
                         String strDisplayName = display.concat(strK);
                         //System.out.println("strDisplayName:"+strDisplayName);
                         //System.out.println("strDisplayName getparam:"+runtimeData.getParameter(strDisplayName));
                                          
                         // Additional Info End
                          if (runtimeData.getParameter(internalName) != null)
                          {
                            // System.out.println("true");
                          }
                         //System.out.println("chkbox val:"+runtimeData.getParameter(internalName));
                         
                         if (runtimeData.getParameter(internalName) != null)
                         {                             
                             strXML+= "<Lookup_SelectedDisplayField><SYSTEMLOOKUPFIELD_strInternalNameSelected>"+ internalName +"</SYSTEMLOOKUPFIELD_strInternalNameSelected><DELookupDisplayOrderSelected>"+ runtimeData.getParameter(fieldOrder) +"</DELookupDisplayOrderSelected></Lookup_SelectedDisplayField>";                                                                                   
                         }
                         // Additional Info Start
                         if(runtimeData.getParameter(strDisplayName) != null){
                                 
                                 // To get the internal name by cutting the initial string Systemlookupfield_strIndsiplayAddnl                                 
                                 if(strDisplayName.startsWith("SYSTEMLOOKUPFIELD_strInDisplayAddnl")){

                                     String x = strDisplayName.substring(35,strDisplayName.length());                           
                                     //System.out.println("x:"+x);
                                     
                                     strXML+= "<Lookup_SelectedDisplayFieldAddnl><SYSTEMLOOKUPFIELD_strInDisplayAddnlSelected>" + x + "</SYSTEMLOOKUPFIELD_strInDisplayAddnlSelected></Lookup_SelectedDisplayFieldAddnl>";
                                 }                                 
                                 
                                 
                         }
                         // Additional Info End
                         
                      }
                   }
              }
              else
              {
                  // To get all the Field names for this DataElement key, from System Lookup Field table               
                  DALSecurityQuery queryField = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                  queryField.setDomain("SYSTEMLOOKUPFIELD", null, null, null);                    
                  queryField.setFields(vtBuildLookupField, null);    
                  queryField.setWhere(null, 0, "SYSTEMLOOKUPFIELD_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);            
                  queryField.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);                            
                  
                  // Additional Info Start
                  queryField.setWhere("AND",0, "SYSTEMLOOKUPFIELD_strInDropdown", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);                  
                  // Additional Info End
                  
                  ResultSet rsResultField = queryField.executeSelect();  

                  while(rsResultField.next())
                  {
                      //System.out.println("internal name exist in field table");
                      strXML+= "<Lookup_SelectedDisplayField><SYSTEMLOOKUPFIELD_strInternalNameSelected>"+ rsResultField.getString("SYSTEMLOOKUPFIELD_strInternalName") +"</SYSTEMLOOKUPFIELD_strInternalNameSelected><DELookupDisplayOrderSelected>"+ rsResultField.getString("SYSTEMLOOKUPFIELD_strFieldOrder") +"</DELookupDisplayOrderSelected></Lookup_SelectedDisplayField>";                                   
                      
                  }
                  
                  rsResultField.close();
                  
                  // Additional Info Start
                  DALSecurityQuery queryField1 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                  queryField1.setDomain("SYSTEMLOOKUPFIELD", null, null, null);                    
                  queryField1.setFields(vtBuildLookupField, null);    
                  queryField1.setWhere(null, 0, "SYSTEMLOOKUPFIELD_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);            
                  queryField1.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                  queryField1.setWhere("AND",0, "SYSTEMLOOKUPFIELD_strInDisplayAddnl", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                  
                  ResultSet rsResultField1 = queryField1.executeSelect();  

                  while(rsResultField1.next())
                  {          
                    strXML+= "<Lookup_SelectedDisplayFieldAddnl><SYSTEMLOOKUPFIELD_strInDisplayAddnlSelected>" + rsResultField1.getString("SYSTEMLOOKUPFIELD_strInternalName") + "</SYSTEMLOOKUPFIELD_strInDisplayAddnlSelected></Lookup_SelectedDisplayFieldAddnl>";
                  }

                  rsResultField1.close();
                  
                  // Additional Info End
                  
                  
                  
              }
              //PAS  
             //System.out.println("strXML:"+strXML);

              // To assign the mode
              //System.out.println("strMode:"+strMode);
              strXML+="<strMode>"+ runtimeData.getParameter("strMode") +"</strMode>";
              
              //System.out.println("strxml -----------------------------------------------------------------------:"+strXML);
              strStylesheet = ADDEDIT_DATAELEMENTS;         


        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }    
    
    
    /* To update the DataElement Order, when it is moved up & down
     *
     *
     */
    private void ChangeDEOrderinAddEditDE(String strDataElementId, String strMove)
    {
        try
        {
            
            // Get the form fields to display all the labels and their corresponding fields            
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElements");
            Vector vtBuildSmartformsDataElementsView = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_viewDataElements");
             
            String strDataElementOrder = "";
            String strNewDataElementId = "";
            String strNewDataElementOrder = "";
           
            boolean blUpdate = false;      
            
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
            query.setDomain("DATAELEMENTS", null, null, null); 
            query.setFields(vtBuildSmartformsDataElementsView, null);
            query.setWhere(null, 0, "DATAELEMENTS_intDataElementPoolID", "=", runtimeData.getParameter("DEPoolSelected"), 0, DALQuery.WHERE_HAS_VALUE);             
            query.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);     
            query.setOrderBy("DATAELEMENTS_intDataElementOrder", "ASC");            
            
            ResultSet rsResult = query.executeSelect();
            
            while(rsResult.next())
            {
                
                if(strDataElementId.equals(rsResult.getString("DATAELEMENTS_intDataElementID")))
                {                  
                    
                    strDataElementOrder = rsResult.getString("DATAELEMENTS_intDataElementOrder");             
                    
                    if (strMove.equals("moveUp"))
                    {              
                               
                        if (rsResult.isFirst() == false)
                        {
                            rsResult.previous();
                            
                            blUpdate = true;
                            strNewDataElementId = rsResult.getString("DATAELEMENTS_intDataElementID");
                            strNewDataElementOrder = rsResult.getString("DATAELEMENTS_intDataElementOrder");
                            
                            rsResult.next();
                        }
                        else
                        {
                            blUpdate = false;
                        }
                        
                        
                    }
                    else if (strMove.equals("moveDown"))
                    {
                        if(rsResult.isLast() == false)
                        {   
                            rsResult.next();
                            
                            blUpdate = true;
                            strNewDataElementId = rsResult.getString("DATAELEMENTS_intDataElementID");
                            strNewDataElementOrder = rsResult.getString("DATAELEMENTS_intDataElementOrder");   
                            
                            rsResult.previous();
                        }
                        else
                        {
                            blUpdate = false;
                        }                  
                        
                    }
                    
                }
                
            }            
            rsResult.close();
            
            if (blUpdate == true)
            {     
                           
                // Update the passed DataElement Id with the new Order
                DALSecurityQuery queryUpd1 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                queryUpd1.setDomain("DATAELEMENTS", null, null, null);                
                
                queryUpd1.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                queryUpd1.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", strDataElementId, 0, DALQuery.WHERE_HAS_VALUE);                            
                queryUpd1.setField("DATAELEMENTS_intDataElementOrder",strNewDataElementOrder);
                                
                queryUpd1.executeUpdate();    
                   

                 // Update the new DataElement Id with the passed DE id's Order
                DALSecurityQuery queryUpd2 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                queryUpd2.setDomain("DATAELEMENTS", null, null, null);                
                queryUpd2.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                queryUpd2.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", strNewDataElementId, 0, DALQuery.WHERE_HAS_VALUE);                            
                queryUpd2.setField("DATAELEMENTS_intDataElementOrder",strDataElementOrder);             
               
                queryUpd2.executeUpdate();        

            }
            
            buildAddEditDataElements();
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    /* To delete the dataelement in AddEditDataElement
     *
     *
     */
    private void deleteDataElementinAddEditDE()
    {
        try
        {
            
            strCurrent = ADDEDIT_DATAELEMENTS;
            
             // Get the form fields to display all the labels and their corresponding fields            
            Vector vtBuildSmartFormsDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElements");
            Vector vtViewDataElements = DatabaseSchema.getFormFields("csmartformadmin_view_DataElements");
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElements");
            
            
            //check if the dataelement belongs to the pool selected, then delete, else dont delete
            DALSecurityQuery queryChkDE = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
            queryChkDE.setDomain("DATAELEMENTS", null, null, null);  
            queryChkDE.setFields(vtBuildDataElements,null);
            queryChkDE.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                            
            queryChkDE.setWhere("AND", 0, "DATAELEMENTS_intDataElementPoolID", "=",runtimeData.getParameter("DEPoolSelected") , 0, DALQuery.WHERE_HAS_VALUE);                            
            queryChkDE.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=","0" , 0, DALQuery.WHERE_HAS_VALUE);                            

            ResultSet rsqueryChkDE = queryChkDE.executeSelect();

            // If DE belongs to the DE pool selected, then proceed to delete
            if(rsqueryChkDE.next())
            {
                
                // To check if the dataelement is used in any of the Smartforms
                DALSecurityQuery queryChk = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                queryChk.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);     
                queryChk.setDomain("DATAELEMENTS", "DATAELEMENTS_intDataElementID", "SMARTFORMTODATAELEMENTS_intDataElementID", "INNER JOIN"); 
                queryChk.setFields(vtBuildSmartFormsDataElements,null);
                queryChk.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                            
                queryChk.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=","0" , 0, DALQuery.WHERE_HAS_VALUE);                            

                //queryChk.executeSelect();
                
                ResultSet rsResultChk = queryChk.executeSelect();

                // Dataelement is still being used in the smartforms
                if(rsResultChk.next())
                {     
                     buildAddEditDataElements();
                     strErrorMessage = "DataElement cannot be deleted, as it is still being used in the Smartforms";                 

                }
                // DataElement is not used in any of the smartforms, so delete
                else
                {

                    //**********************************
                    // Start Lock  
                        //System.out.println("checking depool lock if it is readwrite");
                       if (lockRequestDEPool.isValid() && lockRequestDEPool.lockWrites())
                       {        
                            //System.out.println("can update");



                                // delete in DataElements table
                                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                                query.setDomain("DATAELEMENTS", null, null, null);  
                                //query.setFields(vtBuildDataElements,null);
                                query.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                            
                                query.setField("DATAELEMENTS_intDeleted", "-1");

                                query.executeUpdate();
                                //System.out.println("updated de");            
                                lockRequestDEPool.unlockWrites();
                                strErrorMessage = "The DataElement is deleted successfully";

                                 buildAddEditDataElements();                  

                                //query.executeUpdate();
                                //System.out.println("deleted");

                                // delete dataElement in SystemLookupWhere table
                                DALSecurityQuery query1 = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                                query1.setDomain("SYSTEMLOOKUPWHERE", null, null, null);     
                                query1.setWhere(null, 0, "SYSTEMLOOKUPWHERE_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                            
                                query1.setField("SYSTEMLOOKUPWHERE_intDeleted", "-1");

                                query1.executeUpdate();

                                 // delete dataElement in SystemLookupField table                
                                DALSecurityQuery query2 = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                                query2.setDomain("SYSTEMLOOKUPFIELD", null, null, null);     
                                query2.setWhere(null, 0, "SYSTEMLOOKUPFIELD_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                            
                                query2.setField("SYSTEMLOOKUPFIELD_intDeleted", "-1");

                                query2.executeUpdate();


                                buildAddEditDataElements();

                                // To clear the deleted id's value in the hidden field
                                int startPos = strXML.indexOf("<DATAELEMENTS_intDataElementIDselected>",0);                        
                                strXML = strXML.substring(0,startPos);   

                                String setVar = null;
                                strErrorMessage = "DataElement is deleted successfully";
                                strXML+="<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";
                                strXML+="<DATAELEMENTS_intDataElementIDselected>"+setVar+"</DATAELEMENTS_intDataElementIDselected>" +
                                        "<DATAELEMENTS_intDataElementTypeSelected>"+setVar+"</DATAELEMENTS_intDataElementTypeSelected>";            

                                runtimeData.setParameter("DATAELEMENTS_intDataElementIDselected",strXML);
                                runtimeData.setParameter("DATAELEMENTS_intDataElementTypeSelected",strXML);


                       }
                       else
                       {       
                            //System.out.println("cannot update");
                            strErrorMessage = "This record is being viewed by other users. You cannot delete it now. Please try again later. ";                       
                            strFlag = "error";
                            buildAddEditDataElements();
                            strFlag = "";

                            //strXML+= QueryChannel.buildViewXMLFile(vtViewDataElements, runtimeData);
                            strXML+= QueryChannel.buildViewFromKeyXMLFile(vtBuildDataElements,"DATAELEMENTS_intDataElementID",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"));             
                            runtimeData.setParameter("strXML",strXML);


                       }
                       // End lock
                    //**********************************
                }
                
                rsResultChk.close();
                strXML+="<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";

             } // End of check - if the DE belonged to the same pool i.e. the DEpool selected
            
            else
            {   //DE belongs to DEpool different from DEpool selected, so dont delete, call same page again
                buildAddEditDataElements();
            }
                        
            

        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
     /* To save the dataelement in Add/Edit DataElements
     *
     *
     */
    private void  saveDataElementInAddEditDE()
    {
        try
        {            
            
            strCurrent = ADDEDIT_DATAELEMENTS;
                       
             // Get the form fields to display all the labels and their corresponding fields
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElements");
            Vector vtViewDataElements = DatabaseSchema.getFormFields("csmartformadmin_view_DataElements");
            Vector vtDataElementsNoOrder = DatabaseSchema.getFormFields("csmartformadmin_view_DataElementsNoOrder");
            
            
            //System.out.println("dataElement id:"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"));
            //System.out.println("vtBuildDataElements :"+vtBuildDataElements);
            //System.out.println("radio buttons value:"+runtimeData.getParameter("rdDATAELEMENTS_dtDataElementDefaultDate"));
            
            // Combine the day, month & year as one date
            String dtDataElementDateMin = runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMin_Day") + "/" +
                                          runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMin_Month") + "/" +
                                          runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMin_Year");

            String dtDataElementDateMax = runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMax_Day") + "/" +
                                          runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMax_Month") + "/" +
                                          runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMax_Year");
            
            // if date is not null, then set the respective values
            if (runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMin_Day") != null)
            {
                //System.out.println("it is not null");
                runtimeData.setParameter("DATAELEMENTS_dtDataElementDateMin",dtDataElementDateMin); 
                runtimeData.setParameter("DATAELEMENTS_dtDataElementDateMax",dtDataElementDateMax); 

                //******** Get the value of radio button selected
                getDateRadioBtnValue();



            }

            //System.out.println("dtDataElementDateMin:"+dtDataElementDateMin);     
           
            
            // If save is clicked in edit mode, then update
            if (!runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected").equals("null"))
            {
                
                // To check if the DataElement id exist in this pool id, Exist - update, Does not exist -insert
                DALSecurityQuery queryChk = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                queryChk.setDomain("DATAELEMENTS", null, null, null);
                queryChk.setFields(vtBuildDataElements, null);            
                queryChk.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=", runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"), 0, DALQuery.WHERE_HAS_VALUE);
                queryChk.setWhere("AND", 0, "DATAELEMENTS_intDataElementPoolID", "=", runtimeData.getParameter("DEPoolSelected"), 0, DALQuery.WHERE_HAS_VALUE);
                queryChk.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

                ResultSet rsResultChk = queryChk.executeSelect();         
                
                // if record exist, then do update
                if(rsResultChk.next())
                {
                    
                    // To set the value of optionpool for the below parameter, so that strcheckReqdFields will not throw error
                    runtimeData.setParameter("DATAELEMENTS_intDataElementPoolID",runtimeData.getParameter("DEPoolSelected"));               

                    String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtDataElementsNoOrder, runtimeData);

                     // checks data validation
                    String strValidation = QueryChannel.validateData(vtViewDataElements, runtimeData); 
                    
                    //System.out.println("strCheckRequiredFields:"+strCheckRequiredFields);
                    //System.out.println("strValidation:"+strValidation);
                    
                    
                    //---------------Monetary validation rennypv
                    
                    if(runtimeData.getParameter("DATAELEMENTS_intDataElementTypeSelected").equals("MONETARY") || runtimeData.getParameter("DATAELEMENTS_intDataElementType").equals("MONETARY"))
                    {
                        String minvalue = runtimeData.getParameter("DATAELEMENTS_intDataElementIntMin");
                        if(!Utilities.validateMonetaryValue(minvalue))
                        {
                            strValidation = "Please enter valid input in Min value field";
                        }
                        String maxvalue = runtimeData.getParameter("DATAELEMENTS_intDataElementIntMax");
                        if(!Utilities.validateMonetaryValue(maxvalue))
                        {
                            strValidation = "Please enter valid input in Max value field";
                        }
                    }
                    //----------------Numeric validation rennypv
                    if(runtimeData.getParameter("DATAELEMENTS_intDataElementTypeSelected").equals("NUMERIC") || runtimeData.getParameter("DATAELEMENTS_intDataElementType").equals("NUMERIC"))
                    {
                        String minvalue = runtimeData.getParameter("DATAELEMENTS_intDataElementIntMin");
                        if(!Utilities.validateFloatValue(minvalue))
                        {
                            strValidation = "Please enter valid input in Min value field";
                        }
                        String maxvalue = runtimeData.getParameter("DATAELEMENTS_intDataElementIntMax");
                        if(!Utilities.validateFloatValue(maxvalue))
                        {
                            strValidation = "Please enter valid input in Max value field";
                        }
                    }
                    //**Script Validation
                    
                    // if Script DEtype, then validate script
                    
                    String strScript = "";
                    if(runtimeData.getParameter("DATAELEMENTS_intDataElementTypeSelected").equals("SCRIPT") || runtimeData.getParameter("DATAELEMENTS_intDataElementType").equals("SCRIPT"))
                    {                
                        strScript = runtimeData.getParameter("DATAELEMENTS_strDataElementScript");

                         if(Utilities.validateScriptValue(strScript) == false)
                         {
                             strErrorMessage = "Please enter a valid input for Script";
                             strFlag = "error";
                             buildAddEditDataElements();
                             strFlag = "";


                             strXML+= QueryChannel.buildViewXMLFile(vtViewDataElements, runtimeData);                        
                             runtimeData.setParameter("strXML",strXML);
                             strXML+="<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";                
                         }
                    }

                    if(Utilities.validateScriptValue(strScript) != false)
                    {
                        // If all required fields values are supplied, then perform insert
                        if (((strCheckRequiredFields == null) || (strCheckRequiredFields.equals(""))) && ((strValidation == null) || (strValidation.equals(""))))
                        {                        
                             // Start Lock  
                            //System.out.println("checking depool lock if it is readwrite");
                           if (lockRequestDEPool.isValid() && lockRequestDEPool.lockWrites())
                           {  
                                // System.out.println("no error in update de");                    

                                //System.out.println("DE update");
                                String strDataElementOrder = "";

                                // Update the Display fields selected and its order
                                getCheckedDisplayFields();
                                // Additional Info Start
                                getLookupsAdditionalInfo();
                                // Additional Info End

                                // if type is not System lookup, then delete from where & update column lookup type as blank
                                typeNotSystemLookup();

                                // To check if the already saved lookup type is same as the one currently saved. if not, delete previous records from where table
                                //***                        
                                 // get the lookup type for this DataElement Id
                                DALSecurityQuery queryType = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                                queryType.setDomain("DATAELEMENTS", null, null, null);            
                                queryType.setFields(vtBuildDataElements, null);     
                                queryType.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                                queryType.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"), 0, DALQuery.WHERE_HAS_VALUE);                         

                                ResultSet rsResultType = queryType.executeSelect();     

                                if (rsResultType.next())
                                {  
                                    //System.out.println("the field lookup type is:"+runtimeData.getParameter("DATAELEMENTS_strDataElementLookupType"));
                                    String typeResult = "";
                                    typeResult = rsResultType.getString("DATAELEMENTS_strDataElementLookupType");
                                    if((typeResult != null) && !(typeResult.equals(runtimeData.getParameter("DATAELEMENTS_strDataElementLookupType"))))
                                    {
                                        // delete records for this dataELement Id
                                        DALSecurityQuery queryDel = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                                        queryDel.setDomain("SYSTEMLOOKUPWHERE", null, null, null);            
                                        queryDel.setWhere(null, 0, "SYSTEMLOOKUPWHERE_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                                        
                                        queryDel.setField("SYSTEMLOOKUPWHERE_intDeleted","-1");
                                        queryDel.executeUpdate();
                                        //System.out.println("deleted in where");                    

                                    }
                                }
                                
                                rsResultType.close();
                                
                                //***

                                // get the value of DataElement Order for this DataElement Id, to update with the same order                
                                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                                query.setDomain("DATAELEMENTS", null, null, null);
                                query.setFields(vtBuildDataElements, null);            
                                query.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=", runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"), 0, DALQuery.WHERE_HAS_VALUE);
                                query.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

                                ResultSet rsResult = query.executeSelect();         

                                while(rsResult.next())
                                {
                                    strDataElementOrder = rsResult.getString("DATAELEMENTS_intDataElementOrder");
                                }
                                
                                rsResult.close();
                                
                                // Perform the update
                                DALSecurityQuery queryUpd = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                                queryUpd.setDomain("DATAELEMENTS", null, null, null); 
                                runtimeData.setParameter("DATAELEMENTS_intDataElementID",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"));
                                queryUpd.setFields(vtBuildDataElements,  runtimeData);                
                                queryUpd.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                  

                                runtimeData.setParameter("DATAELEMENTS_intDataElementPoolID",runtimeData.getParameter("DEPoolSelected"));                                                                                        
                                runtimeData.setParameter("DATAELEMENTS_intDataElementOrder",strDataElementOrder);


                                /*else
                                {
                                    //System.out.println("it is null");
                                    //runtimeData.setParameter("DATAELEMENTS_dtDataElementDateMin",(String) null); 
                                    //runtimeData.setParameter("DATAELEMENTS_dtDataElementDateMax",(String) null); 
                                }*/
                                // Update radio buttons value
                                getDefaultRadioBtnValue();         


                                //System.out.println("can update");
                                queryUpd.executeUpdate();
                                //System.out.println("updated de");            
                                lockRequestDEPool.unlockWrites();
                                strErrorMessage = "The DataElement is updated successfully";

                                 buildAddEditDataElements();

                           }
                           else
                           {       
                                //System.out.println("cannot update");
                                strErrorMessage = "This record is being viewed by other users. You cannot update it now. Please try again later. ";                       
                                strFlag = "error";
                                buildAddEditDataElements();
                                strFlag = "";

                                strXML+= QueryChannel.buildViewXMLFile(vtViewDataElements, runtimeData);
                                runtimeData.setParameter("strXML",strXML);


                           }
                           // End lock
                           strXML+="<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";


                            //queryUpd.setField("DATAELEMENTS_intDataElementPoolID",runtimeData.getParameter("DEPoolSelected"));                                                 
                           // buildAddEditDataElements();

                        }
                        else // error
                        {                        
                            //System.out.println("error in update de, return to same page");        

                            if(strCheckRequiredFields != null && strValidation != null)
                            {
                                strErrorMessage = strCheckRequiredFields;
                                strErrorMessage+=". ";
                                strErrorMessage+= strValidation;
                                strErrorMessage+=". ";
                            }
                            if(strCheckRequiredFields == null)
                            {              
                                strErrorMessage = strValidation;
                            }
                            if(strValidation == null)
                            {
                                strErrorMessage = strCheckRequiredFields;                
                            }

                            strFlag = "error";
                            buildAddEditDataElements();
                            strFlag = "";


                            //????

                            strXML+= QueryChannel.buildViewXMLFile(vtViewDataElements, runtimeData);                        
                            runtimeData.setParameter("strXML",strXML);
                            strXML+="<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";

                        }

                    }//End of Script Validation If
                    
                }
                
                // record does not exist, then insert
                else
                {
                    //saveNewDataElement();
                    duplicateDataElementInAddEditDE();
                                       
                }
                
                rsResultChk.close();


            }
            // insert in New mode
            else if (runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected").equals("null"))
            {
                saveNewDataElement();
            }     
            
            strStylesheet = ADDEDIT_DATAELEMENTS; 
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
     /* To display the same page again, after updating or deleting the smartform
     *
     *
     */
    private void duplicateDataElementInAddEditDE()
    {
        try
        {
            strCurrent = ADDEDIT_DATAELEMENTS;
            Hashtable resultSetDetails = new Hashtable();
            
             // Get the form fields to display all the labels and their corresponding fields
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElements");
            Vector vtBuildLookupWhere = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_SystemLookupWhere");
            Vector vtBuildLookupField = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_SystemLookupField");            
            Vector vtDEToOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_DataElementsToOptions");
            
            // Get all the values for this DataElement Id
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
            query.setDomain("DATAELEMENTS", null, null, null);
            query.setFields(vtBuildDataElements, null);            
            query.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=", runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"), 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResult = query.executeSelect();         
            
            // Get the values and insert a new record with same data in DataElements table
            if(rsResult.next())
            {
                
                
                String tempData = "";
                for (int i = 1; i < vtBuildDataElements.size(); i++)
                {
                    tempData = rsResult.getString(vtBuildDataElements.get(i).toString());
                    if (tempData == null) tempData = "";
                    
                    resultSetDetails.put(vtBuildDataElements.get(i).toString(), tempData);                    
                }
                
                
                //insert the dataelements selected
                DALSecurityQuery query1 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);                      

                query1.setDomain("DATAELEMENTS", null, null, null); 
                //query1.setFields(vtBuildDataElements,null);
                
                // Get the max no in Dataelement Order column
                int maxDataElementOrder = QueryChannel.getNewestKeyAsInt("DATAELEMENTS_intDataElementOrder");                
                maxDataElementOrder = maxDataElementOrder + 1;                               
                
                // Set the value for all the fields except id
                query1.setField("DATAELEMENTS_intDataElementPoolID",runtimeData.getParameter("DEPoolSelected"));
                query1.setField("DATAELEMENTS_strDataElementName", (String) resultSetDetails.get("DATAELEMENTS_strDataElementName"));
                query1.setField("DATAELEMENTS_intDataElementType", (String) resultSetDetails.get("DATAELEMENTS_intDataElementType"));
                query1.setField("DATAELEMENTS_intDataElementOrder",new Integer(maxDataElementOrder).toString());
                query1.setField("DATAELEMENTS_intDataElementRow", (String) resultSetDetails.get("DATAELEMENTS_intDataElementRow"));
                query1.setField("DATAELEMENTS_intDataElementColumn", (String) resultSetDetails.get("DATAELEMENTS_intDataElementColumn"));
                query1.setField("DATAELEMENTS_intDataElementIntMax", (String) resultSetDetails.get("DATAELEMENTS_intDataElementIntMax"));
                query1.setField("DATAELEMENTS_intDataElementIntMin", (String) resultSetDetails.get("DATAELEMENTS_intDataElementIntMin"));
                
                String strMinDateVal = (String) resultSetDetails.get("DATAELEMENTS_dtDataElementDateMin");
                //System.out.println("datemin rsval:"+strMinDateVal);
                // When type DATE is duplicated
                if(strMinDateVal != null)
                {                    
                    query1.setField("DATAELEMENTS_dtDataElementDateMax",Utilities.convertDateForDisplay((String) resultSetDetails.get("DATAELEMENTS_dtDataElementDateMax")));
                    query1.setField("DATAELEMENTS_dtDataElementDateMin",Utilities.convertDateForDisplay(strMinDateVal));
                    
                    String strDefaultDate = (String) resultSetDetails.get("DATAELEMENTS_dtDataElementDefaultDate");
                    //System.out.println("strDefaultDate:"+strDefaultDate);
                    
                    if (strDefaultDate != null)
                    {
                        query1.setField("DATAELEMENTS_dtDataElementDefaultDate",Utilities.convertDateForDisplay(strDefaultDate));
                    }
                }
                query1.setField("DATAELEMENTS_strDataElementScript", (String) resultSetDetails.get("DATAELEMENTS_strDataElementScript"));
                query1.setField("DATAELEMENTS_strDataElementLookupType", (String) resultSetDetails.get("DATAELEMENTS_strDataElementLookupType"));
                query1.setField("DATAELEMENTS_strDataElementHelp", (String) resultSetDetails.get("DATAELEMENTS_strDataElementHelp"));                
                query1.setField("DATAELEMENTS_strDataElementDefaultTodaysDate", (String) resultSetDetails.get("DATAELEMENTS_strDataElementDefaultTodaysDate"));
                //query1.setField("DATAELEMENTS_dtDataElementDefaultDate", (String) resultSetDetails.get("DATAELEMENTS_dtDataElementDefaultDate"));
                query1.setField("DATAELEMENTS_strDataElementDefaultWorkingDaysOperator", (String) resultSetDetails.get("DATAELEMENTS_strDataElementDefaultWorkingDaysOperator"));
                query1.setField("DATAELEMENTS_intDataElementDefaultWorkingDaysNumber", (String) resultSetDetails.get("DATAELEMENTS_intDataElementDefaultWorkingDaysNumber"));
                query1.setField("DATAELEMENTS_strDataElementDefaultWdOrDaysOption", (String) resultSetDetails.get("DATAELEMENTS_strDataElementDefaultWdOrDaysOption"));                                
                query1.setField("DATAELEMENTS_strDataElementDefault", (String) resultSetDetails.get("DATAELEMENTS_strDataElementDefault"));               
                query1.setField("DATAELEMENTS_intDataElementDefaultSmartformID", (String) resultSetDetails.get("DATAELEMENTS_intDataElementDefaultSmartformID"));
                query1.setField("DATAELEMENTS_intDataElementDefaultDataElementID", (String) resultSetDetails.get("DATAELEMENTS_intDataElementDefaultDataElementID"));
                query1.setField("DATAELEMENTS_intMandatory", (String) resultSetDetails.get("DATAELEMENTS_intMandatory"));                
          
                // Inserted new record with same DataElement Details
                query1.executeInsert();
                // Get the inserted id         
               
            
             
                int intCurrentDataElementID = QueryChannel.getNewestKeyAsInt(query1); 
                //System.out.println("intCurrentDataElementID:"+intCurrentDataElementID);

                //****************
               // To get the SystemLookupWhere Details of the DataElement ID, which is duplicated
                DALSecurityQuery query2 = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                query2.setDomain("SYSTEMLOOKUPWHERE", null, null, null);
                query2.setFields(vtBuildLookupWhere, null);            
                query2.setWhere(null, 0, "SYSTEMLOOKUPWHERE_intDataElementID", "=", runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"), 0, DALQuery.WHERE_HAS_VALUE);
                query2.setWhere("AND", 0, "SYSTEMLOOKUPWHERE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query2.setOrderBy("SYSTEMLOOKUPWHERE_strOrder","ASC");
                ResultSet rsResult2 = query2.executeSelect();         

                // Get the values and insert a new record with same data
                while(rsResult2.next())
                {                
                    //insert the where details of this dataElement Id selected
                    DALSecurityQuery query3 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                    query3.setDomain("SYSTEMLOOKUPWHERE", null, null, null);                     
                    
                    // Get the max no in SystemLookupWhere Order column
                    int intLookupWhereOrder = QueryChannel.getNewestKeyAsInt("SYSTEMLOOKUPWHERE_strOrder");                
                    intLookupWhereOrder = intLookupWhereOrder + 1;            
                    
                    // Set the value for all the fields except id
                    query3.setField("SYSTEMLOOKUPWHERE_intDataElementID",new Integer(intCurrentDataElementID).toString());
                    query3.setField("SYSTEMLOOKUPWHERE_strConnector",rsResult2.getString("SYSTEMLOOKUPWHERE_strConnector"));
                    query3.setField("SYSTEMLOOKUPWHERE_strField",rsResult2.getString("SYSTEMLOOKUPWHERE_strField"));
                    query3.setField("SYSTEMLOOKUPWHERE_strInternalName",rsResult2.getString("SYSTEMLOOKUPWHERE_strInternalName"));
                    query3.setField("SYSTEMLOOKUPWHERE_strOperator",rsResult2.getString("SYSTEMLOOKUPWHERE_strOperator"));
                    query3.setField("SYSTEMLOOKUPWHERE_strValue",rsResult2.getString("SYSTEMLOOKUPWHERE_strValue"));
                    query3.setField("SYSTEMLOOKUPWHERE_strOrder",new Integer(intLookupWhereOrder).toString());
                    
                     // Inserted new record with same System Lookup Where Details
                    query3.executeInsert(); 
                    
                }
                //System.out.println("inserted duplicate dataelement lookupwhere");
                rsResult2.close();
                
                
                 //****************
               // To get the SystemLookupField Details of the DataElement ID, which is duplicated
                DALSecurityQuery query4 = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                query4.setDomain("SYSTEMLOOKUPFIELD", null, null, null);
                query4.setFields(vtBuildLookupField, null);            
                query4.setWhere(null, 0, "SYSTEMLOOKUPFIELD_intDataElementID", "=", runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"), 0, DALQuery.WHERE_HAS_VALUE);
                query4.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                query4.setOrderBy("SYSTEMLOOKUPFIELD_strFieldOrder","ASC");
                ResultSet rsResult4 = query4.executeSelect();         

                // Get the values and insert a new record with same data
                while(rsResult4.next())
                {                
                    //insert the where details of this dataElement Id selected
                    DALSecurityQuery query5 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                    query5.setDomain("SYSTEMLOOKUPFIELD", null, null, null);                     
                    

                    // Get the max no in SystemLookupField Order column
                    int intLookupFieldOrder = QueryChannel.getNewestKeyAsInt("SYSTEMLOOKUPFIELD_strFieldOrder");                
                    intLookupFieldOrder = intLookupFieldOrder + 1;            
                    
                    // Set the value for all the fields except id
                    query5.setField("SYSTEMLOOKUPFIELD_intDataElementID",new Integer(intCurrentDataElementID).toString());
                    query5.setField("SYSTEMLOOKUPFIELD_strInternalName",rsResult4.getString("SYSTEMLOOKUPFIELD_strInternalName"));
                    query5.setField("SYSTEMLOOKUPFIELD_strFieldOrder",rsResult4.getString("SYSTEMLOOKUPFIELD_strFieldOrder"));
                    
                    // Inserted new record with same System Lookup Field Details
                    query5.executeInsert(); 
                    
                }
                //System.out.println("inserted duplicate dataelement's systemlookupfield details");
                rsResult4.close();
                
                //****************
               // To get the DataElementsToOptions Details of the DataElement ID, which is duplicated
                DALSecurityQuery query6 = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                query6.setDomain("DATAELEMENTSTOOPTIONS", null, null, null);
                query6.setFields(vtDEToOptions, null);            
                query6.setWhere(null, 0, "DATAELEMENTSTOOPTIONS_intDataElementID", "=", runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"), 0, DALQuery.WHERE_HAS_VALUE);
                query6.setWhere("AND", 0, "DATAELEMENTSTOOPTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsResult6 = query6.executeSelect();         

                // Get the values and insert a new record with same data for the new dataelement id obtained by duplicating
                while(rsResult6.next())
                {                
                    //insert the DEToOptions details of this dataElement Id selected
                    DALSecurityQuery query7 = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                    query7.setDomain("DATAELEMENTSTOOPTIONS", null, null, null);                     
                    
                    // Set the value for all the fields except id
                    query7.setField("DATAELEMENTSTOOPTIONS_intDataElementID",new Integer(intCurrentDataElementID).toString());
                    query7.setField("DATAELEMENTSTOOPTIONS_intOptionPoolID",rsResult6.getString("DATAELEMENTSTOOPTIONS_intOptionPoolID"));
                    
                     // Inserted new record with same DataelementToOptions Details
                    query7.executeInsert(); 
                    
                }
                //System.out.println("inserted duplicate dataelementstooptions");
                rsResult6.close();
                
                
                // to build the page back with the inserted id
               strDataElementID = new Integer(intCurrentDataElementID).toString();
               runtimeData.setParameter("DATAELEMENTS_intDataElementIDselected",strDataElementID);
            }
            rsResult.close();
            
           buildAddEditDataElements();
            
            // To build the page back    
            //buildPageAfterInsert();
            
            strStylesheet = ADDEDIT_DATAELEMENTS; 
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
     /* To insert a page break
     *
     *
     */
    private int insertPageBreak()
    {
        try
        {
            strCurrent = ADDEDIT_DATAELEMENTS;
            
            // Get the form fields to display all the labels and their corresponding fields
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElements");
            
            DALSecurityQuery queryIns = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
            queryIns.setDomain("DATAELEMENTS", null, null, null);             
            queryIns.setField("DATAELEMENTS_intDataElementPoolID",runtimeData.getParameter("DEPoolSelected"));            
            queryIns.setField("DATAELEMENTS_strDataElementName","---- PAGE BREAK ----");
            queryIns.setField("DATAELEMENTS_intDataElementType","PAGEBREAK");

            // Get the max no in Dataelement Order column
            int maxDataElementOrder = QueryChannel.getNewestKeyAsInt("DATAELEMENTS_intDataElementOrder");                
            maxDataElementOrder = maxDataElementOrder + 1;               
            queryIns.setField("DATAELEMENTS_intDataElementOrder",new Integer(maxDataElementOrder).toString());               
            
            queryIns.executeInsert();         

            //System.out.println("inserted pagebreak");
            
           //buildAddEditDataElements();
            
            // To build the page back
           // buildPageAfterInsert();
            
            strStylesheet = ADDEDIT_DATAELEMENTS; 
            return QueryChannel.getNewestKeyAsInt(queryIns);
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
        return -1;
    }
    
     /* To build the same page again, after inserting 
     *
     *
     */
    private void buildPageAfterInsert(int intDataElementID)
    {
        try
        {
            
            // Get the form fields to display all the labels and their corresponding fields
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElements");
            
            // To build back the same page again
            int intCurrentDataElementID = intDataElementID;           
            //System.out.println("intCurrentDataElementID:"+intCurrentDataElementID);
            
            // To assign the duplicate id's value in the hidden field
            int startPos = strXML.indexOf("<DATAELEMENTS_intDataElementIDselected>",0);                        
            strXML = strXML.substring(0,startPos);
            
            runtimeData.remove("DATAELEMENTS_intDataElementIDselected");
            // runtimeData.remove("strMode");           
            
            strXML+= "<DATAELEMENTS_intDataElementIDselected>"+ intCurrentDataElementID +"</DATAELEMENTS_intDataElementIDselected>" +
                     QueryChannel.buildFormLabelXMLFile(vtBuildDataElements) +                         
                     QueryChannel.buildViewFromKeyXMLFile(vtBuildDataElements, "DATAELEMENTS_intDataElementID" , new Integer(intCurrentDataElementID).toString());
             
           if (runtimeData.getParameter("DELookupTypeSelected") == null || runtimeData.getParameter("DELookupTypeSelected").equals(""))
           {
                // To set the first lookup Type as strLookupTypeSel
                Hashtable hashLookupObjsAll = DatabaseSchema.getLookupObjects();
                Enumeration enumFirst = hashLookupObjsAll.keys();
                String strLookupTypeSel = "";
                if (enumFirst.hasMoreElements())
                {                         
                    strLookupTypeSel =  (String)enumFirst.nextElement(); 
                    //System.out.println("first lookuptype:"+strLookupTypeSel);
                }
                // Load all the field names for the first lookupType
                loadLookupObjsFieldNames(strLookupTypeSel);

                // Add the current lookuptype's value
                strXML+= "<DELookupTypeSelected>"+strLookupTypeSel+"</DELookupTypeSelected>";

           }
            
            strXML+="<strMode>addPageBreak</strMode>";
            runtimeData.setParameter("strXML",strXML);  
            
            //System.out.println("strxml:"+strXML);
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    /* To get the Date radio button's value and set the parameters accordingly
     *
     *
     */
    private void getDateRadioBtnValue()
    {
        try
        {
            
            String dtDataElementDefaultDate = runtimeData.getParameter("DATAELEMENTS_dtDataElementDefaultDate_Day") + "/" +
                                              runtimeData.getParameter("DATAELEMENTS_dtDataElementDefaultDate_Month") + "/" +
                                              runtimeData.getParameter("DATAELEMENTS_dtDataElementDefaultDate_Year");

                       
            // check if the first radio button is clicked
            if (runtimeData.getParameter("rdDefaultorExisting").equals("DATAELEMENTS_strDataElementDefaultTodaysDate"))
            {
                //System.out.println("first radio button is clicked");
                //runtimeData.setParameter("DATAELEMENTS_dtDataElementDefaultDate",dtDataElementDefaultDate); 
                runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultTodaysDate","TODAYSDATE");
                runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultWorkingDaysOperator",(String) null);
                runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultWorkingDaysNumber",(String) null); 
                runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultWdOrDaysOption",(String) null); 
                
                runtimeData.setParameter("DATAELEMENTS_strDataElementDefault",(String) null);
                runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultSmartformID",(String) null);
                runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultDataElementID",(String) null);  
                    

            }
            // check if the second radio button is clicked
            else if (runtimeData.getParameter("rdDefaultorExisting").equals("DATAELEMENTS_dtDataElementDefaultDate"))
            {
                //System.out.println("2 radio button is clicked");
                runtimeData.setParameter("DATAELEMENTS_dtDataElementDefaultDate",dtDataElementDefaultDate);
                runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultWorkingDaysOperator",(String) null);
                runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultWorkingDaysNumber",(String) null); 
                runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultWdOrDaysOption",(String) null); 
                
                runtimeData.setParameter("DATAELEMENTS_strDataElementDefault",(String) null);
                runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultSmartformID",(String) null);
                runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultDataElementID",(String) null);  
                    

            }
            // check if the third radio button is clicked
            else if (runtimeData.getParameter("rdDefaultorExisting").equals("DATAELEMENTS_dtDataElementTodaysDatePlusWorkingDays"))
            {
                //System.out.println("3 radio button is clicked");
                runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultWorkingDaysOperator",runtimeData.getParameter("DATAELEMENTS_strDataElementDefaultWorkingDaysOperator")); 
                runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultWorkingDaysNumber",runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultWorkingDaysNumber")); 
                runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultWdOrDaysOption",runtimeData.getParameter("DATAELEMENTS_strDataElementDefaultWdOrDaysOption")); 
                
                runtimeData.setParameter("DATAELEMENTS_strDataElementDefault",(String) null);
                runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultSmartformID",(String) null);
                runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultDataElementID",(String) null);  
                    

            }
                    
                    
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    /* To get the Default Radio button's value and set the parameters accordingly
     *
     *
     */
    private void getDefaultRadioBtnValue()
    {
        try
        {    
            // If while adding Title, it is null since there is no default radio buttons. So check if it is null.
            if(runtimeData.getParameter("rdDefaultorExisting") == null)
            { }
            else
            {
                //System.out.println("def DE id selected: "+runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultDataElementID"));             
                // check if the 4th radio button is clicked
                if (runtimeData.getParameter("rdDefaultorExisting").equals("rdDefault"))
                {
                    //System.out.println("first radio button is clicked");
                    //runtimeData.setParameter("DATAELEMENTS_dtDataElementDefaultDate",dtDataElementDefaultDate); 
                    runtimeData.setParameter("DATAELEMENTS_strDataElementDefault",runtimeData.getParameter("DATAELEMENTS_strDataElementDefault"));
                    runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultSmartformID",(String) null);
                    runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultDataElementID",(String) null);  
                    
                    runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultWorkingDaysOperator",(String) null);
                    runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultWorkingDaysNumber",(String) null); 
                    runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultWdOrDaysOption",(String) null); 

                }
                // check if the 5th radio button is clicked
                else if ((runtimeData.getParameter("rdDefaultorExisting").equals("rdExisting")) && (!(runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultDataElementID") == null)))
                {
                    //System.out.println("2 radio button is clicked");
                    runtimeData.setParameter("DATAELEMENTS_strDataElementDefault",(String)null);
                    runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultSmartformID",runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultSmartformID"));
                    runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultDataElementID",runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultDataElementID"));       
                    
                    runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultWorkingDaysOperator",(String) null);
                    runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultWorkingDaysNumber",(String) null); 
                    runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultWdOrDaysOption",(String) null); 


                }            
                 // check if the last radio button is clicked and the DataElementId is blank
                else if (runtimeData.getParameter("rdDefaultorExisting").equals("rdExisting") && runtimeData.getParameter("DATAELEMENTS_intDataElementDefaultDataElementID") == null)
                {
                    //System.out.println("2 radio button is clicked and dataelement id is blank");
                    runtimeData.setParameter("DATAELEMENTS_strDataElementDefault",(String)null);
                    runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultSmartformID",(String) null);
                    runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultDataElementID",(String) null);
                    
                                      
                    runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultWorkingDaysOperator",(String) null);
                    runtimeData.setParameter("DATAELEMENTS_intDataElementDefaultWorkingDaysNumber",(String) null); 
                    runtimeData.setParameter("DATAELEMENTS_strDataElementDefaultWdOrDaysOption",(String) null); 
                }   
            }
                    
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    
     /* To save the Where condition
     *
     *
     */
    private void addWhere()
    {
        try
        {
            strCurrent = ADDEDIT_DATAELEMENTS;
            
             // Get the form fields to display all the labels and their corresponding fields
            Vector vtBuildLookupWhere = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_SystemLookupWhere");
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElements");
            Vector vtValidateLookupWhere = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_ViewSystemLookupWhere");
            
            
            String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtValidateLookupWhere, runtimeData);
            
             // checks data validation
            String strValidation = QueryChannel.validateData(vtValidateLookupWhere, runtimeData);               
            
            // If all required fields values are supplied, then perform insert
            if (((strCheckRequiredFields == null) || (strCheckRequiredFields.equals(""))) && ((strValidation == null) || (strValidation.equals(""))))
            {   
            
                // get the lookup type for this DataElement Id
                 DALSecurityQuery queryType = new DALSecurityQuery(SMARTFORM_BUILD, authToken);     
                queryType.setDomain("DATAELEMENTS", null, null, null);            
                queryType.setFields(vtBuildDataElements, null);     
                queryType.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                queryType.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);                         

                ResultSet rsResultType = queryType.executeSelect();     

                if (rsResultType.next())
                {  
                    //System.out.println("the field lookup type is:"+runtimeData.getParameter("DATAELEMENTS_strDataElementLookupType"));
                    String typeResult = "";
                    typeResult = rsResultType.getString("DATAELEMENTS_strDataElementLookupType");
                    if((typeResult != null) && !(typeResult.equals(runtimeData.getParameter("DATAELEMENTS_strDataElementLookupType"))))
                    {
                        // delete records for this dataELement Id
                        DALSecurityQuery queryDel = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                        queryDel.setDomain("SYSTEMLOOKUPWHERE", null, null, null);            
                        queryDel.setWhere(null, 0, "SYSTEMLOOKUPWHERE_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                                        
                        queryDel.setField("SYSTEMLOOKUPWHERE_intDeleted","-1");
                        queryDel.executeUpdate();
                        //System.out.println("deleted in where");                    

                    }
                }

                rsResultType.close();
                
                 // update type for this dataELement Id
                DALSecurityQuery queryUpd = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                queryUpd.setDomain("DATAELEMENTS", null, null, null);            
                queryUpd.setWhere(null, 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                queryUpd.setWhere("AND", 0, "DATAELEMENTS_intDataElementID", "=", strDataElementID, 0, DALQuery.WHERE_HAS_VALUE);                                     
                queryUpd.setField("DATAELEMENTS_strDataElementLookupType",runtimeData.getParameter("DATAELEMENTS_strDataElementLookupType"));
                queryUpd.executeUpdate();
                //System.out.println("updated type");


                // Set the Domains & runtimeData & other fields to do Insert
                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                query.setDomain("SYSTEMLOOKUPWHERE", null, null, null); 
                query.setFields(vtBuildLookupWhere, runtimeData);
                // cant use setfield use as below - queryIns.setField("DATAELEMENTS_intDataElementPoolID",runtimeData.getParameter("DEPoolSelected"));
                runtimeData.setParameter("SYSTEMLOOKUPWHERE_intDataElementID",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"));               
                //System.out.println("vtBuildLookupWhere:"+vtBuildLookupWhere);
                
                // To set the corresponding Internal name for the field name
                
                //***********
                Vector vtLookupFieldNames = DatabaseSchema.getLookupObjectFields(runtimeData.getParameter("DELookupTypeSelected"));
                //System.out.println("vtLookupFieldNames:"+vtLookupFieldNames);
                String strIntName = "";
                
loop1:          for(int i=0; i< vtLookupFieldNames.size();i++)
                {                          
                    LookupObjectField obj = (LookupObjectField)vtLookupFieldNames.elementAt(i);

                    // If this name can be displayed in the Where condition
                    if (obj.isInWhere())
                    {
                        //System.out.println("name:"+obj.getName());
                        //get field name for the internal name what which is edited
                        if(obj.getDisplayName().equals(runtimeData.getParameter("SYSTEMLOOKUPWHERE_strField")))
                        {
                            //System.out.println("get name of the field:"+obj.getName());
                            strIntName = obj.getName();
                            break loop1;

                        }
                    }
                }

                runtimeData.setParameter("SYSTEMLOOKUPWHERE_strInternalName",strIntName.toString());
                //System.out.println("strIntName:"+strIntName);
                 //************
                
                // Get the max no in SystemLookupWhere Order column
                int maxLookupWhereOrder = QueryChannel.getNewestKeyAsInt("SYSTEMLOOKUPWHERE_strOrder");                
                maxLookupWhereOrder = maxLookupWhereOrder + 1;               

                runtimeData.setParameter("SYSTEMLOOKUPWHERE_strOrder",new Integer(maxLookupWhereOrder).toString());               

                
                query.executeInsert();    
                //System.out.println("inserted where");         

                buildAddEditDataElements();
                
            }
            else
            {
                
                strStylesheet = ADDEDIT_DATAELEMENTS;                
                buildAddEditDataElements();
                
                strErrorMessage = strCheckRequiredFields;   
                
                strXML+= QueryChannel.buildViewXMLFile(vtValidateLookupWhere, runtimeData) +
                         "<DELookupFieldSelected>"+ runtimeData.getParameter("SYSTEMLOOKUPWHERE_strField") +"</DELookupFieldSelected>" +
                         "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";
                
            }

            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
     /* To build the page when Edit Where is clicked
     *
     *
     */
    private void buildEditWhere()
    {
        try
        {
            strCurrent = ADDEDIT_DATAELEMENTS;
            // Get the form fields to display all the labels and their corresponding fields
            Vector vtBuildLookupWhere = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_SystemLookupWhere");
           
            // Get details for this Where Id, whose Edit button is clicked
            strXML+= QueryChannel.buildViewFromKeyXMLFile(vtBuildLookupWhere,"SYSTEMLOOKUPWHERE_intDataElementLookupWhereID",new Integer(runtimeData.getParameter("SYSTEMLOOKUPWHERE_intDataElementLookupWhereID")).toString());
                
            // To display the field dropdown with the fieldname, of the selected where id, whose Edit is clicked
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
            query.setDomain("SYSTEMLOOKUPWHERE", null, null, null);     
            query.setFields(vtBuildLookupWhere,null);
            query.setWhere(null, 0, "SYSTEMLOOKUPWHERE_intDataElementLookupWhereID", "=",runtimeData.getParameter("SYSTEMLOOKUPWHERE_intDataElementLookupWhereID") , 0, DALQuery.WHERE_HAS_VALUE);                            
            query.setWhere("AND", 0, "SYSTEMLOOKUPWHERE_intDeleted", "=","0" , 0, DALQuery.WHERE_HAS_VALUE);      
            ResultSet rsResult = query.executeSelect();
            
            while(rsResult.next())
            {
                strXML+= "<DELookupFieldSelected>"+ rsResult.getString("SYSTEMLOOKUPWHERE_strField") +"</DELookupFieldSelected>";
            }
            
            rsResult.close();
             
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
     /* To update the Where condition
     *
     *
     */
    private void updateWhere()
    {
        try
        {
            strCurrent = ADDEDIT_DATAELEMENTS;
            
             // Get the form fields to display all the labels and their corresponding fields
            Vector vtBuildLookupWhere = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_SystemLookupWhere");
            Vector vtValidateLookupWhere = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_ViewSystemLookupWhere");
            
            
            String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtValidateLookupWhere, runtimeData);
            
             // checks data validation
            String strValidation = QueryChannel.validateData(vtValidateLookupWhere, runtimeData);               
            
            // If all required fields values are supplied, then perform insert
            if (((strCheckRequiredFields == null) || (strCheckRequiredFields.equals(""))) && ((strValidation == null) || (strValidation.equals(""))))
            {    
            
                 // To get the order of the selected where id, whose Edit is clicked
                DALSecurityQuery querySel = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                querySel.setDomain("SYSTEMLOOKUPWHERE", null, null, null);     
                querySel.setFields(vtBuildLookupWhere,null);
                querySel.setWhere(null, 0, "SYSTEMLOOKUPWHERE_intDataElementLookupWhereID", "=",runtimeData.getParameter("SYSTEMLOOKUPWHERE_intDataElementLookupWhereID") , 0, DALQuery.WHERE_HAS_VALUE);                            
                querySel.setWhere("AND", 0, "SYSTEMLOOKUPWHERE_intDeleted", "=","0" , 0, DALQuery.WHERE_HAS_VALUE);      
                ResultSet rsResultSel = querySel.executeSelect();

                String strLookupWhereOrder = "";
                while(rsResultSel.next())
                {
                     strLookupWhereOrder = rsResultSel.getString("SYSTEMLOOKUPWHERE_strOrder");
                }     

                rsResultSel.close();
                
                //Set the Domains & runtimeData & other fields to do Insert
                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                query.setDomain("SYSTEMLOOKUPWHERE", null, null, null); 
                query.setFields(vtBuildLookupWhere, runtimeData);
                query.setWhere(null, 0, "SYSTEMLOOKUPWHERE_intDataElementLookupWhereID", "=",runtimeData.getParameter("SYSTEMLOOKUPWHERE_intDataElementLookupWhereID") , 0, DALQuery.WHERE_HAS_VALUE);                            

                runtimeData.setParameter("SYSTEMLOOKUPWHERE_intDataElementID",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"));               
                runtimeData.setParameter("SYSTEMLOOKUPWHERE_strOrder",strLookupWhereOrder);

                // To set the corresponding Internal name for the field name
                
                //***********
                Vector vtLookupFieldNames = DatabaseSchema.getLookupObjectFields(runtimeData.getParameter("DELookupTypeSelected"));
                //System.out.println("vtLookupFieldNames:"+vtLookupFieldNames);
                String strIntName = "";
                
loop1:          for(int i=0; i< vtLookupFieldNames.size();i++)
                {                          
                    LookupObjectField obj = (LookupObjectField)vtLookupFieldNames.elementAt(i);

                    // If this name can be displayed in the Where condition
                    if (obj.isInWhere())
                    {
                        //System.out.println("name:"+obj.getName());
                        //get field name for the internal name what which is edited
                        if(obj.getDisplayName().equals(runtimeData.getParameter("SYSTEMLOOKUPWHERE_strField")))
                        {
                            //System.out.println("get name of the field:"+obj.getName());
                            strIntName = obj.getName();
                            break loop1;

                        }
                    }
                }

                runtimeData.setParameter("SYSTEMLOOKUPWHERE_strInternalName",strIntName.toString());
                //System.out.println("strIntName:"+strIntName);
                 //************

                query.executeUpdate();   
                //System.out.println("updated where");      
                 buildAddEditDataElements();
                 
            }
            else
            {
                strStylesheet = ADDEDIT_DATAELEMENTS;                
                buildAddEditDataElements();
                
                strErrorMessage = strCheckRequiredFields;   
                
                strXML+= QueryChannel.buildViewXMLFile(vtValidateLookupWhere, runtimeData) +
                         "<DELookupFieldSelected>"+ runtimeData.getParameter("SYSTEMLOOKUPWHERE_strField") +"</DELookupFieldSelected>" +
                         "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";
                
            }

            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    /* To delete the Where condition
     *
     *
     */
    private void deleteWhere()
    {
        try
        {
           
            //Set the Domains & runtimeData & other fields to do Insert
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
            query.setDomain("SYSTEMLOOKUPWHERE", null, null, null);            
            query.setWhere(null, 0, "SYSTEMLOOKUPWHERE_intDataElementLookupWhereID", "=",runtimeData.getParameter("SYSTEMLOOKUPWHERE_intDataElementLookupWhereID") , 0, DALQuery.WHERE_HAS_VALUE);                                        
            query.setField("SYSTEMLOOKUPWHERE_intDeleted","-1");
            query.executeUpdate(); 
            //System.out.println("deleted where");  
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
     /* To load the lookup objects and field names and order corresponding to each lookup object
     *
     *
     */
    private void loadLookupTypes()
    {
        try
        {           
            
            // Type System Lookup - Get Lookup Object names and its field names
            Hashtable hashLookupObjs = DatabaseSchema.getLookupObjects();
            Enumeration key_enum = hashLookupObjs.keys();            
            while (key_enum.hasMoreElements())
            {   
                // Get Lookup Object Names
                String lookupObjName =  (String)key_enum.nextElement();                
                //System.out.println("lookupObjName:"+lookupObjName);                
                strXML+= "<build_LookupTypes><DATAELEMENTS_strDataElementLookupType>"+lookupObjName +"</DATAELEMENTS_strDataElementLookupType></build_LookupTypes>";      
                
            }    
            
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
     /* To load the lookup objects and field names and order corresponding to each lookup object
     *
     *
     */
    private void loadLookupObjsFieldNames(String lookupObjName)
    {
        try
        {           
            
           
                 // Get the lookup Objects Field Names
                Vector vtLookupFieldNames = DatabaseSchema.getLookupObjectFields(lookupObjName);
                
                int j = 0;
                
                for(int i=0; i< vtLookupFieldNames.size();i++)
                {            
                    LookupObjectField obj = (LookupObjectField)vtLookupFieldNames.elementAt(i);
                    //System.out.println("name:"+obj.getDisplayName());
                    
                    // If this name can be displayed in the Where condition
                    if (obj.isInWhere())
                    {
                        //System.out.println("in where");
                        strXML+= "<Lookup_FieldNames><SYSTEMLOOKUPWHERE_strField>"+ obj.getDisplayName() +"</SYSTEMLOOKUPWHERE_strField></Lookup_FieldNames>"; 
                    }
                    
                    // If this name can be displayed as a label with checkbox
                    if (obj.isInFieldList())
                    {
                        // to Start the order from 1 instead of 0
                        j= j + 1;
                        
                        //System.out.println("in display");
                        //System.out.println("internal name:"+obj.getName());
                        //System.out.println("order:"+new Integer(i).toString());                        
                        
                       
                        // Load the order
                        //strXML+= "<Lookup_DisplayField><SYSTEMLOOKUPFIELD_strInternalName>"+ obj.getName() +"</SYSTEMLOOKUPFIELD_strInternalName><SYSTEMLOOKUPWHERE_strField>"+ obj.getDisplayName() +"</SYSTEMLOOKUPWHERE_strField><DELookupDisplayOrderSelected>"+ new Integer(j).toString() +"</DELookupDisplayOrderSelected></Lookup_DisplayField>";                           
                        strXML+= "<Lookup_DisplayField><SYSTEMLOOKUPFIELD_strInternalName>"+ obj.getName() +"</SYSTEMLOOKUPFIELD_strInternalName><SYSTEMLOOKUPWHERE_strField>"+ obj.getDisplayName() +"</SYSTEMLOOKUPWHERE_strField></Lookup_DisplayField>";                           
                        //System.out.println("order:"+new Integer(j).toString());    
                        strXML+= "<LoadFieldCount><SYSTEMLOOKUPFIELD_strFieldOrder>"+ new Integer(j).toString() +"</SYSTEMLOOKUPFIELD_strFieldOrder></LoadFieldCount>";                 
                      
                    }
                    
                    
                    
                }    
                
                // Enhancement Start
                // Lookup the Display Fields for Smartform Completion
                Vector vtLookupDisplayFields = DatabaseSchema.getLookupObjectDisplayFields(lookupObjName);
                
                int k = 0;
                
                for(int m=0; m< vtLookupDisplayFields.size();m++)
                {            
                    String strDisplayFieldIntName = (String)vtLookupDisplayFields.elementAt(m);
                    //System.out.println("strDisplayFieldIntName :"+strDisplayFieldIntName);
                    
                    //strXML+= "<Lookup_DisplayFieldExtraInfo><SYSTEMLOOKUPFIELD_strInternalName>"+ strDisplayFieldIntName +"</SYSTEMLOOKUPFIELD_strInternalName><SYSTEMLOOKUPWHERE_strField>"+ strDisplayFieldIntName +"</SYSTEMLOOKUPWHERE_strField></Lookup_DisplayFieldExtraInfo>";                           
                     
                }
                
                //Enhancement End
                
            
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
     /* To get the values of the display fields checkboxes selected 
     *   
     *
     */
    private void getCheckedDisplayFields()
    {
        try
        {     
            //System.out.println("inside display fields checking");
            
            // Type System Lookup - Get Lookup Object names and its field names
            Hashtable hashLookupObjs = DatabaseSchema.getLookupObjects();
            Enumeration key_enum = hashLookupObjs.keys();            
            while (key_enum.hasMoreElements())
            {   
                // Get Lookup Object Names
                String lookupObjName =  (String)key_enum.nextElement();                
                //System.out.println("lookupObjName:"+lookupObjName);                
                
                 // Get the lookup Objects Field Names
                Vector vtLookupFieldNames = DatabaseSchema.getLookupObjectFields(lookupObjName);

                for(int i=0; i< vtLookupFieldNames.size();i++)
                {            
                    LookupObjectField obj = (LookupObjectField)vtLookupFieldNames.elementAt(i);
                    //System.out.println("internal name:"+obj.getName());              
                    
                    // If this name can be displayed as a label with checkbox
                    if (obj.isInFieldList())
                    {                                               
                        String strInternalName = obj.getName();
                        //System.out.println("internal name in field list:"+strInternalName);
                        
                        // Get the form fields to display all the labels and their corresponding fields
                        Vector vtBuildLookupField = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_SystemLookupField");

                        // Check if this internal name is already present for this DataElement key in SystemLookupField table
                        DALSecurityQuery querySel = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                        querySel.setDomain("SYSTEMLOOKUPFIELD", null, null, null);     
                        querySel.setFields(vtBuildLookupField,null);
                        querySel.setWhere(null, 0, "SYSTEMLOOKUPFIELD_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                            
                        querySel.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_strInternalName", "=", strInternalName, 0, DALQuery.WHERE_HAS_VALUE);                           
                        querySel.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_intDeleted", "=","0" , 0, DALQuery.WHERE_HAS_VALUE);       
                        ResultSet rsResultSel = querySel.executeSelect();

                        
                        // Check if the checkbox is selected and then insert
                        if(runtimeData.getParameter(strInternalName) != null)
                        {
                            //System.out.println(strInternalName+" selected");
                            //System.out.println("its order:"+runtimeData.getParameter("SYSTEMLOOKUPFIELD_strFieldOrder"+obj.getName()));
                            
                            
                            // If the selected internal name already exist, then do update
                            if (rsResultSel.next())
                            {
                                
                                //Set the Domains & runtimeData & other fields to do Insert
                                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                                query.setDomain("SYSTEMLOOKUPFIELD", null, null, null);            
                                query.setWhere(null, 0, "SYSTEMLOOKUPFIELD_intDataElementLookupFieldID", "=",rsResultSel.getString("SYSTEMLOOKUPFIELD_intDataElementLookupFieldID") , 0, DALQuery.WHERE_HAS_VALUE);                                        
                                query.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_intDeleted", "=","0" , 0, DALQuery.WHERE_HAS_VALUE);  
                                query.setField("SYSTEMLOOKUPFIELD_strFieldOrder", runtimeData.getParameter("SYSTEMLOOKUPFIELD_strFieldOrder"+obj.getName()));
                                // Additional Info Start
                                query.setField("SYSTEMLOOKUPFIELD_strInDropdown","0");
                                // Additional Info End
                                query.executeUpdate(); 
                                //System.out.println("updated lookup field order");
                                
                                
                            }
                            // if the selected internal name does not exist already, then do insert
                            else
                            {
                                //Set the Domains & runtimeData & other fields to do Insert
                                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                                query.setDomain("SYSTEMLOOKUPFIELD", null, null, null);            
                                query.setField("SYSTEMLOOKUPFIELD_intDataElementID",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"));
                                query.setField("SYSTEMLOOKUPFIELD_strInternalName",strInternalName);                         
                                query.setField("SYSTEMLOOKUPFIELD_strFieldOrder", runtimeData.getParameter("SYSTEMLOOKUPFIELD_strFieldOrder"+obj.getName()));
                                 // Additional Info Start
                                query.setField("SYSTEMLOOKUPFIELD_strInDropdown","0");
                                query.setField("SYSTEMLOOKUPFIELD_strInDisplayAddnl","-1");
                                 // Additional Info End
                                query.executeInsert();
                                //System.out.println("inserted lookup field");
                                
                            }
                             

                            
                        }
                        // if checkbox is not selected
                        else
                        {
                            // If the deselected internal name exist, then do delete
                            if (rsResultSel.next())
                            {
                                
                                //Set the Domains & runtimeData & other fields to do Insert
                                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                                query.setDomain("SYSTEMLOOKUPFIELD", null, null, null);            
                                query.setWhere(null, 0, "SYSTEMLOOKUPFIELD_intDataElementLookupFieldID", "=",rsResultSel.getString("SYSTEMLOOKUPFIELD_intDataElementLookupFieldID") , 0, DALQuery.WHERE_HAS_VALUE);                                                                        
                                //query.setField("SYSTEMLOOKUPFIELD_intDeleted", "-1");
                                query.setField("SYSTEMLOOKUPFIELD_strInDropdown","-1");
                                query.executeUpdate(); 
                                //System.out.println("deleted lookup field order");
                                
                                
                            }
                            
                        }               
                        
                        rsResultSel.close();
                       
                    
                    
                    } // end of if isinfieldlist
                    
                    
                    
                }  // end of for  
                
            } // end of while   
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
     /* To insert new DataElements in AddEditDE
     *
     *
     */
    private void saveNewDataElement()
    {
        try
        {
            // Get the form fields to display all the labels and their corresponding fields
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElements");
            Vector vtViewDataElements = DatabaseSchema.getFormFields("csmartformadmin_view_DataElements");
            Vector vtDataElementsNoOrder = DatabaseSchema.getFormFields("csmartformadmin_view_DataElementsNoOrder");
            
            
             // Combine the day, month & year as one date
            String dtDataElementDateMin = runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMin_Day") + "/" +
                                          runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMin_Month") + "/" +
                                          runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMin_Year");

            String dtDataElementDateMax = runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMax_Day") + "/" +
                                          runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMax_Month") + "/" +
                                          runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMax_Year");

             // if date type is selected, then insert the max date & min date
            if (runtimeData.getParameter("DATAELEMENTS_dtDataElementDateMin_Day") != null)
            {
                runtimeData.setParameter("DATAELEMENTS_dtDataElementDateMin",dtDataElementDateMin); 
                runtimeData.setParameter("DATAELEMENTS_dtDataElementDateMax",dtDataElementDateMax); 

                //*****************
                getDateRadioBtnValue();


            }
            
             // To set the value of optionpool for the below parameter, so that strcheckReqdFields will not throw error
            runtimeData.setParameter("DATAELEMENTS_intDataElementPoolID",runtimeData.getParameter("DEPoolSelected"));               
            
            String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtDataElementsNoOrder, runtimeData);
            
             // checks data validation
            String strValidation = QueryChannel.validateData(vtViewDataElements, runtimeData); 
            
            //System.out.println("strCheckRequiredFields:"+strCheckRequiredFields);
            //System.out.println("strValidation:"+strValidation);
             
            //***Script Validation
            String strScript = "";
            // if Script DEtype, then validate script
            if(runtimeData.getParameter("DATAELEMENTS_intDataElementTypeSelected").equals("SCRIPT") || runtimeData.getParameter("DATAELEMENTS_intDataElementType").equals("SCRIPT"))
            {                
                strScript = runtimeData.getParameter("DATAELEMENTS_strDataElementScript");
                
                 if(Utilities.validateScriptValue(strScript) == false)
                 {
                     strErrorMessage = "Please enter a valid input for Script";
                     buildAddEditDataElements();         
                
                     strXML+= QueryChannel.buildViewXMLFile(vtViewDataElements, runtimeData);
                     runtimeData.setParameter("strXML",strXML);                
                 }
            }
            
            if(Utilities.validateScriptValue(strScript) != false)
            {
             
                // If all required fields values are supplied, then perform insert
                if (((strCheckRequiredFields == null) || (strCheckRequiredFields.equals(""))) && ((strValidation == null) || (strValidation.equals(""))))
                {   
                     //System.out.println("no error");

                    //System.out.println("dataElement id:"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"));
                    //System.out.println("vtBuildDataElements :"+vtBuildDataElements);
                    //System.out.println("radio buttons value:"+runtimeData.getParameter("rdDATAELEMENTS_dtDataElementDefaultDate"));
                    //System.out.println("dtDataElementDateMin:"+dtDataElementDateMin); 
                    //System.out.println("new DE save");

                    DALSecurityQuery queryIns = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                    queryIns.setDomain("DATAELEMENTS", null, null, null); 
                    queryIns.setFields(vtBuildDataElements,  runtimeData);
                    // cant use setfield use as below(set parameter) not as - queryIns.setField("DATAELEMENTS_intDataElementPoolID",runtimeData.getParameter("DEPoolSelected"));
                    //runtimeData.setParameter("DATAELEMENTS_intDataElementPoolID",runtimeData.getParameter("DEPoolSelected"));               


                    // Get the max no in Dataelement Order column
                    int maxDataElementOrder = QueryChannel.getNewestKeyAsInt("DATAELEMENTS_intDataElementOrder");                
                    maxDataElementOrder = maxDataElementOrder + 1;               

                    runtimeData.setParameter("DATAELEMENTS_intDataElementOrder",new Integer(maxDataElementOrder).toString());               


                    getDefaultRadioBtnValue();
                    queryIns.executeInsert();         

                    //System.out.println("inserted");



                     //System.out.println("set default sf id");
                    buildAddEditDataElements();

                     //To build the page back
                    buildPageAfterInsert(QueryChannel.getNewestKeyAsInt(queryIns));

                    strErrorMessage = "The DataElement is added successfully";               

                    //runtimeData.setParameter("DATAELEMENTS_intDataElementIDselected",strXML);

                    //System.out.println("strXML"+strXML);
                }
                else
                {
                    //System.out.println("error return to same page");        

                    if(strCheckRequiredFields != null && strValidation != null)
                    {
                        strErrorMessage = strCheckRequiredFields;
                        strErrorMessage+=". ";
                        strErrorMessage+= strValidation;
                        strErrorMessage+=". ";
                    }
                    if(strCheckRequiredFields == null)
                    {              
                        strErrorMessage = strValidation;
                    }
                    if(strValidation == null)
                    {
                        strErrorMessage = strCheckRequiredFields;                
                    }

                    buildAddEditDataElements();


                    //?????


                    strXML+= QueryChannel.buildViewXMLFile(vtViewDataElements, runtimeData);

                    runtimeData.setParameter("strXML",strXML);



                }
                
            }// End of Script Validation If
            
           strXML+="<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
     /* To delete from where if type is not system lookup
     *
     *
     */
    private void typeNotSystemLookup()
    {
        try
        {
            if (!(runtimeData.getParameter("DATAELEMENTS_intDataElementType").equals("SYSTEM LOOKUP")))
            {
            
                //delete from where, for this dataELement Id
                DALSecurityQuery queryDel = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                queryDel.setDomain("SYSTEMLOOKUPWHERE", null, null, null);            
                queryDel.setWhere(null, 0, "SYSTEMLOOKUPWHERE_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                                        
                queryDel.setField("SYSTEMLOOKUPWHERE_intDeleted","-1");
                queryDel.executeUpdate();
                //System.out.println("deleted in where"); 

                // update lookup type in DataElements table
                DALSecurityQuery queryUpd = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                queryUpd.setDomain("DATAELEMENTS", null, null, null);
                queryUpd.setWhere(null, 0, "DATAELEMENTS_intDataElementID", "=", runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"), 0, DALQuery.WHERE_HAS_VALUE);
                queryUpd.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                queryUpd.setField("DATAELEMENTS_strDataElementLookupType","");
                queryUpd.executeUpdate();
                //System.out.println("updated & deleted since not lookup type");
                
                queryDel = null;
                queryUpd = null;
                
            }
            
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    /* onclick of add DataElement Pool, in Add/Edit DataElements
     *
     *
     */
    private void displayAddDataElementPool()
    {
        try
        {
            strCurrent = ADD_DATAELEMENTPOOL;
            
             // Get the form fields to display all the labels and their corresponding fields
            Vector vtViewDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_view_dataElementPool");
            
            // pass the fields for displaying Add/Edit DE if back button is clicked
            strXML = QueryChannel.buildFormLabelXMLFile(vtViewDataElementPool) +
                     QueryChannel.buildAddFormXMLFile(vtViewDataElementPool) +
                     "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                     "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                     "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";
            
            strStylesheet = ADD_DATAELEMENTPOOL; 
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
     /* To Add new DataElement Pool
     *
     *
     */
    private void saveNewDataElementPool()
    {
        
       Vector vtAddDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_add_dataElementPool"); 
       Vector vtViewDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_view_dataElementPool");       
        
        
        try
        {
           
            String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddDataElementPool, runtimeData);
            
             // checks data validation
            String strValidation = QueryChannel.validateData(vtAddDataElementPool, runtimeData);               
            
            // If all required fields values are supplied, then perform insert
            if (((strCheckRequiredFields == null) || (strCheckRequiredFields.equals(""))) && ((strValidation == null) || (strValidation.equals(""))))
            {                              
                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_DEPOOL_INSERT, authToken);                

                // Add new smartform                
                query.setDomain("DATAELEMENTPOOL", null, null, null);         
                query.setFields(vtAddDataElementPool, runtimeData);
                query.executeInsert();                

                
                /*
                // Start lock
                
                 if (strCurrentNode.equals("DATAELEMENTPOOL_" + intCurrentDataElementPoolID))
                 {                     
                     clearLockRequest();
                     lockRequest = new LockRequest(authToken);
                     lockRequest.addLock("DATAELEMENTPOOL", new Integer(intCurrentDataElementPoolID).toString(), LockRecord.READ_WRITE);                     
                    
                 }
                else
                {                    
                     clearLockRequest();
                     lockRequest = new LockRequest(authToken);
                    lockRequest.addLock("DATAELEMENTPOOL", new Integer(intCurrentDataElementPoolID).toString(), LockRecord.READ_ONLY);
                    
                }                
                lockRequest.lockDelayWrite();
                
                // End lock
                */
                
                // After adding new DataELement Pool, go back to Add/Edit DataElements page with added record
                int intCurrentOptionID = QueryChannel.getNewestKeyAsInt(query);
                runtimeData.setParameter("DEPoolSelected",new Integer(intCurrentOptionID).toString());
                
                strCurrent = ADDEDIT_DATAELEMENTS;
                strStylesheet = ADDEDIT_DATAELEMENTS;              
               
            }

            else
            { 
                
                strStylesheet = ADD_DATAELEMENTPOOL;                
                strXML = QueryChannel.buildFormLabelXMLFile(vtViewDataElementPool) +
                         QueryChannel.buildViewXMLFile(vtViewDataElementPool, runtimeData);
                  
                strErrorMessage = strCheckRequiredFields;                 
                strXML = strXML + 
                         "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";
                
               //***                
            }
            
             // These values are required to access Add/Edit DataElements page, if back button is clicked
            strXML+= "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                     "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                     "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";


            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
   
    
    /* To Display DataElementPool Details of the Pool selected, on click of Edit DataElement Pool in Add/Edit DataElements
     *
     *
     */
    private void displayDataElementPoolDetails()
    {        
        try
        {
            
            if(runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID").equals("null"))
            {
                buildAddEditDataElements();
                strErrorMessage = "Please add a DataElement Pool";
                strXML+= "<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";
            }
            else
            {
                Vector vtViewDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_view_dataElementPool");  

                int intDataElementPoolID = Integer.parseInt(runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID"));

                strXML = QueryChannel.buildFormLabelXMLFile(vtViewDataElementPool) +
                         QueryChannel.buildViewFromKeyXMLFile(vtViewDataElementPool,"DATAELEMENTPOOL_intDataElementPoolID",intDataElementPoolID);

                strStylesheet =VIEW_DATAELEMENTPOOL;
            }
            /*
            // Start Lock
            
            if (new Integer(intDataElementPoolID).toString() != null)
            {               
                clearLockRequest();
                lockRequest = new LockRequest(authToken);
                lockRequest.addLock("DATAELEMENTPOOL", new Integer(intDataElementPoolID).toString(), LockRecord.READ_WRITE);
            }
            
            lockRequest.lockDelayWrite();
            // End lock
             */
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }  
    
    /* To Update DataElementPool, on click of update in Edit DataElement Pool(in Add/Edit DataElements)
     *
     *
     */
    private void doUpdateDataElementPool()
    {        
        try
        {     
           strStylesheet = VIEW_DATAELEMENTPOOL;
           Vector vtAddDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_add_dataElementPool"); 
           Vector vtViewDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_view_dataElementPool"); 
           
           DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_DEPOOL_UPDATE,authToken);     
           
           String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddDataElementPool, runtimeData);            
           // checks data validation
           String strValidation = QueryChannel.validateData(vtAddDataElementPool, runtimeData);
           
            
            if (((strCheckRequiredFields == null) || (strCheckRequiredFields.equals(""))) && ((strValidation == null) || (strValidation.equals(""))))
            {
                
               if (runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID") != null)
               {           
                   query.setDomain("DATAELEMENTPOOL", null,null,null);
                   query.setFields(vtAddDataElementPool, runtimeData);     
                   query.setWhere(null, 0, "DATAELEMENTPOOL_intDataElementPoolID", "=", runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID"), 0, DALQuery.WHERE_HAS_VALUE);                               
                   query.setField("DATAELEMENTPOOL_intDeleted","0");
                   
                   
                    // Start Lock                  
                   
                   if (lockRequestDEPool.isValid() && lockRequestDEPool.lockWrites())
                   {  
                                       
                   
                        query.executeUpdate();
                   
                   
                      lockRequestDEPool.unlockWrites();                   
                   
                        strErrorMessage = "The DataElement Pool is updated successfully";
                        // After adding new DataELement Pool, go back to Add/Edit DataElements page
                    strCurrent = ADDEDIT_DATAELEMENTS;
                    strStylesheet = ADDEDIT_DATAELEMENTS;  

                    // These values are required to access Add/Edit DataElements page, if back button is clicked
                    strXML+= "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                             "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                             "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";

                   
                   }
                   else
                   {                       
                       strErrorMessage = "This record is being viewed by other users. You cannot update it now. Please try again later. ";                       
                       //displayViewDataElementPool();
                       strStylesheet = VIEW_DATAELEMENTPOOL;                
                         strXML = QueryChannel.buildFormLabelXMLFile(vtViewDataElementPool) +
                         QueryChannel.buildViewXMLFile(vtViewDataElementPool, runtimeData);
                  
                   }
                   
                   // End lock
                      
                   
                        
                    

               }
               
            }
             else
            { 
                
                strStylesheet = VIEW_DATAELEMENTPOOL;                
                strXML = QueryChannel.buildFormLabelXMLFile(vtViewDataElementPool) +
                         QueryChannel.buildViewXMLFile(vtViewDataElementPool, runtimeData);
                  
                strErrorMessage = strCheckRequiredFields;                 
               
            }
            strXML+= "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";
                
            
           
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
     /* To display the same page again, after updating or deleting the dataElement Pool
     *
     *
     */
    private void displayViewDataElementPool()
    {
        try
        {
            strCurrent = VIEW_DATAELEMENTPOOL;
            
            // Get the form fields to display all the labels and their corresponding fields
            Vector vtViewDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_view_dataElementPool");  

            strXML = QueryChannel.buildFormLabelXMLFile(vtViewDataElementPool) +
                     QueryChannel.buildViewXMLFile(vtViewDataElementPool, runtimeData);      
            
            strStylesheet = VIEW_DATAELEMENTPOOL; 
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
     /* To Delete DataElement Pool
     *
     *
     */
    private void doDeleteDataElementPool()
    {
        
        try
        {     
                      
            Vector vtViewDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_view_dataElementPool"); 
            Vector vtBuildDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElements");
            Vector vtBuildDataElementPool = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_DataElementPool");
            
            // To check if this dataElement pool is still being used in dataElements
            DALSecurityQuery querySel = new DALSecurityQuery(SMARTFORM_DEPOOL_DELETE, authToken);  
            querySel.setDomain("DATAELEMENTS", null, null, null);  
            querySel.setFields(vtBuildDataElements, null);                
            querySel.setWhere(null, 0, "DATAELEMENTS_intDataElementPoolID", "=", runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID"), 0, DALQuery.WHERE_HAS_VALUE);                
            querySel.setWhere("AND", 0, "DATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);                    
            ResultSet resultSel = querySel.executeSelect();
            
            // In DataElements, if record exist for this DataElement pool, can't delete
            if (resultSel.next())
            {                
                strErrorMessage = "This DataElementPool contains DataElements, so unable to delete.";
                displayViewDataElementPool();
              
            }
            // if no record exist for this dataElement pool, then delete
            else
            {
            
                if (runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID") != null)
                 {
                    DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_DEPOOL_DELETE, authToken);  
                    query.setDomain("DATAELEMENTPOOL", null, null, null);  
                    query.setFields(vtViewDataElementPool, runtimeData);                
                    query.setWhere(null, 0, "DATAELEMENTPOOL_intDataElementPoolID", "=", runtimeData.getParameter("DATAELEMENTPOOL_intDataElementPoolID"), 0, DALQuery.WHERE_HAS_VALUE);                
                    query.setField("DATAELEMENTPOOL_intDeleted", "-1");                

                    
                     //Locking

                       if (lockRequestDEPool.isValid() && lockRequestDEPool.lockWrites())
                       {  
                                        
                          query.executeUpdate();                       
                          lockRequestDEPool.unlockWrites(); 
                        
                          strErrorMessage = "The DataElement Pool is deleted successfully";

                          // After adding new DataELement Pool, go back to Add/Edit DataElements page by setting DE Pool id as below
                          runtimeData.setParameter("DATAELEMENTPOOL_intDataElementPoolID","null");
                          runtimeData.setParameter("DEPoolSelected","null");
                          
                          strCurrent = ADDEDIT_DATAELEMENTS;
                          strStylesheet = ADDEDIT_DATAELEMENTS; 
                          

                       }
                       else
                       {                       
                           strErrorMessage = "This record is being viewed by other users. You cannot delete it now. Please try again later. ";
                           displayViewDataElementPool();   
                                             

                       }
                                         

                 }
                
            }
            
            resultSel.close();
            
             // These values are required to access Add/Edit DataElements page, if back button is clicked
                strXML+= "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                         "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                         "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";
      
                strXML+= "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";   
               
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }    
    
    
     /* onclick of link Add/Edit Options, in Add/Edit DataElements
     *
     *
     */
    private void buildAddEditOptions()
    {
        try
        {          
            String strOptionPoolID = "";
            
            strCurrent = ADDEDIT_OPTIONS;
            
            // Get the form fields to display all the labels and their corresponding fields
            Vector vtLoadOptionPool = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_loadOptionPool");
            Vector vtLoadOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_loadOptions");
            Vector vtDEToOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_DataElementsToOptions");
            Vector vtListOfDataElements = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_ListOfDataElements");
            
            
            // Load all the option pools
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken);  
            query.setDomain("OPTIONPOOL", null, null, null);  
            query.setFields(vtLoadOptionPool, null);      
            query.setWhere(null, 0, "OPTIONPOOL_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);                    
            query.setOrderBy("OPTIONPOOL_intOptionPoolID","ASC");
            ResultSet rsResult = query.executeSelect();
            //System.out.println("*************************************************************");
            //************************************************
            if (runtimeData.getParameter("blFirstTime") != null)
            {
                // Start lock        
                //System.out.println("setting all optionpools to readonly firsttime");
                rsResult.beforeFirst();
                clearLockRequestOptionPool();
                lockRequestOptionPool = new LockRequest(authToken);
                // register the lock to session manager
                SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartformAdmin", lockRequestOptionPool);

                while(rsResult.next())
                {
                    strOptionPoolID = rsResult.getString("OPTIONPOOL_intOptionPoolID");
                     if (strOptionPoolID != null)
                     {             
                        lockRequestOptionPool.addLock("OPTIONPOOL", strOptionPoolID, LockRecord.READ_ONLY);

                     }

                }
                rsResult.beforeFirst();
                lockRequestOptionPool.lockDelayWrite();

                // End lock
            }
            //************************************************           
            
            
             // If it is the firsttime u r entering this page, then this dataelement's optionpool is shown selected
            if (runtimeData.getParameter("blFirstTime") != null)            
            {       
                //System.out.println("first time");
                // To get the optionpool for this DataElement Id, if it exists
                DALSecurityQuery queryPool = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken);  
                queryPool.setDomain("DATAELEMENTSTOOPTIONS",null,null,null);
                queryPool.setFields(vtDEToOptions, null);    
                queryPool.setWhere(null, 0, "DATAELEMENTSTOOPTIONS_intDataElementID", "=", runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"), 0, DALQuery.WHERE_HAS_VALUE);                          
                queryPool.setWhere("AND", 0, "DATAELEMENTSTOOPTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

                ResultSet rsResultPool = queryPool.executeSelect();  
                //System.out.println("**************************************************");
                if(rsResultPool.next())
                {
                    strOptionPoolSel = rsResultPool.getString("DATAELEMENTSTOOPTIONS_intOptionPoolID");
                }  
                else // if no record exists
                {
                    strOptionPoolSel = null;   
                }
                
                rsResultPool.close();
            }
            else
            {
                //System.out.println("not first time"); 
                // To get the value of Option pool being selected and load options in this option pool
                 if((runtimeData.getParameter("OPTIONPOOL_intOptionPoolID") != null) && !(runtimeData.getParameter("OPTIONPOOL_intOptionPoolID").equals("")))            
                {        
                    //System.out.println("1");  
                     strOptionPoolSel = runtimeData.getParameter("OPTIONPOOL_intOptionPoolID");
                }            
                if(runtimeData.getParameter("OPTIONPOOL_intOptionPoolID") == null || runtimeData.getParameter("OPTIONPOOL_intOptionPoolID").equals(""))            
                {         
                    //System.out.println("2");
                     strOptionPoolSel = runtimeData.getParameter("OptionPoolSelected");                    

                    
                }  
            }
             //System.out.println("strOptionPoolSel:"+strOptionPoolSel);
             // if no optionpool exists in DataElement to options table, set first optionpool as selected and load options for the first option pool 
             if (strOptionPoolSel == null || strOptionPoolSel.equals((String)null) || strOptionPoolSel.equals("") || strOptionPoolSel.equals("null"))
             {
                 //System.out.println("set optionpool to null since no op linked to de");  
                 /*
                if(rsResult.next())
                {                        
                    strOptionPoolSel = rsResult.getString("OPTIONPOOL_intOptionPoolID");                        
                    rsResult.beforeFirst(); 
                }  
                */
                 strOptionPoolSel = null;
                 
             }  
             
             //************************************
            // Start Lock
            
            if (strOptionPoolSel != null)
            {      
                //System.out.println("setting selected optionpool to readwrite");
                clearLockRequestOptionPool();
                lockRequestOptionPool = new LockRequest(authToken);
                // register the lock to session manager
                SessionManager.addLockRequestObject(strSessionUniqueID, "CSmartformAdmin", lockRequestOptionPool);
                lockRequestOptionPool.addLock("OPTIONPOOL", strOptionPoolSel , LockRecord.READ_WRITE);
                lockRequestOptionPool.lockDelayWrite();
            }
            
            
            // End lock
            //**************************************
            
            
            // Load all the options for the Selected Option Pool
            DALSecurityQuery query1 = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken);  
            query1.setDomain("OPTIONS", null, null, null);  
            query1.setFields(vtLoadOptions, null);  
            query1.setWhere(null, 0, "OPTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);    
            query1.setWhere("AND", 0, "OPTIONS_intOptionPoolID", "=", strOptionPoolSel, 0, DALQuery.WHERE_HAS_VALUE);
            query1.setOrderBy("OPTIONS_intOptionOrder", "ASC");
            ResultSet rsResult1 = query1.executeSelect();
            
            // To list all the DataElements using this optionpool      
            DALSecurityQuery querySel = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken);                        
            querySel.setDomain("DATAELEMENTSTOOPTIONS", null, null, null);         
            querySel.setDomain("DATAELEMENTS","DATAELEMENTSTOOPTIONS_intDataElementID","DATAELEMENTS_intDataElementID","INNER JOIN");
            querySel.setFields(vtListOfDataElements, null);    
            //querySel.setWhere(null, 0, "DATAELEMENTSTOOPTIONS_intDataElementID", "=", runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"), 0, DALQuery.WHERE_HAS_VALUE);            
            querySel.setWhere(null, 0, "DATAELEMENTSTOOPTIONS_intOptionPoolID", "=",strOptionPoolSel,   0, DALQuery.WHERE_HAS_VALUE);
            querySel.setWhere("AND", 0, "DATAELEMENTSTOOPTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            
            ResultSet rsResultSel = querySel.executeSelect();    
            
            // To get the value of the Option Id that is selected
            if (runtimeData.getParameter("OPTIONS_intOptionID") != null)
            {                
                strOptionIdSel = runtimeData.getParameter("OPTIONS_intOptionID");                
            }
            else
            {                
                strOptionIdSel = runtimeData.getParameter("OPTIONS_intOptionIDSelected");                 
            }    
            
            String addClicked = "";
            // If Add option is clicked
            if (runtimeData.getParameter("addOption")!=null)
            {
                //System.out.println("add option is clicked");                
                strOptionIdSel = "null";
                addClicked = "yes";
                
            }
            
            //System.out.println("strOptionPoolSel:"+strOptionPoolSel);
            //System.out.println("strOptionIdSel:"+strOptionIdSel);
            // When Option pool is changed, the details of the option(belonging to previously selected option pool) displayed should be cleared
            if(strOptionPoolSel != null && strOptionIdSel != null)
            {
                // Check if the Option pool selected and the option pool of the option selected is the same
                // If they are the same show the options details, else clear the options details in this page
                DALSecurityQuery queryChk = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken);  
                queryChk.setDomain("OPTIONS", null, null, null);  
                queryChk.setFields(vtLoadOptions, null);  
                queryChk.setWhere(null, 0, "OPTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);    
                queryChk.setWhere("AND", 0, "OPTIONS_intOptionID", "=", strOptionIdSel, 0, DALQuery.WHERE_HAS_VALUE);
                queryChk.setWhere("AND", 0, "OPTIONS_intOptionPoolID", "=", strOptionPoolSel, 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsResultChk = queryChk.executeSelect();
                
                // If record does not exist, then clear the options details in this page
                if (!(rsResultChk.next()))
                {
                    //System.out.println("option belongs to some other pool");
                    strOptionIdSel = "null";
                    
                }
                
                rsResultChk.close();
                
            }
          
            
            // Build Add/Edit options page
            strXML = QueryChannel.buildFormLabelXMLFile(vtLoadOptionPool) +
                     QueryChannel.buildSearchXMLFile("addEditOptions_loadOptionPool",rsResult,vtLoadOptionPool) +
                     "<OptionPoolSelected>"+strOptionPoolSel+"</OptionPoolSelected>" +
                     QueryChannel.buildFormLabelXMLFile(vtLoadOptions) +
                     QueryChannel.buildSearchXMLFile("addEditOptions_loadOptions",rsResult1,vtLoadOptions) +
                     "<OPTIONS_intOptionIDSelected>"+strOptionIdSel+"</OPTIONS_intOptionIDSelected>" +
                     QueryChannel.buildFormLabelXMLFile(vtListOfDataElements) +
                     QueryChannel.buildSearchXMLFile("list_of_dataelements",rsResultSel,vtListOfDataElements) +
                     "<addClicked>"+addClicked+"</addClicked>";
                     
             
            rsResult.close();
            rsResult1.close();
            rsResultSel.close();
            
            // if there is no error in doUpdateOption(), buildviewfrom key, else build view from runtimedata
            if(strFlag.equals(""))
            {
                //System.out.println("no error");
                strXML+= QueryChannel.buildViewFromKeyXMLFile(vtLoadOptions,"OPTIONS_intOptionID",strOptionIdSel);
            }
            
                     
            
            // pass the fields for displaying Add/Edit DE if back button is clicked           
            strXML+= "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                     "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                     "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";
            
            strStylesheet = ADDEDIT_OPTIONS; 
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
   
    /* onclick of add DataElement Pool, in Add/Edit DataElements
     *
     *
     */
    private void displayAddOption()
    {
        try
        {
            strCurrent = ADDEDIT_OPTIONS;
            
             // Get the form fields to display all the labels and their corresponding fields
            Vector vtLoadOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_loadOptions");
            
            // pass the fields for displaying Add/Edit DE if back button is clicked
            strXML+= QueryChannel.buildFormLabelXMLFile(vtLoadOptions) +
                     QueryChannel.buildAddFormXMLFile(vtLoadOptions) +
                     "<addClicked>yes</addClicked>" +
                     "<OPTIONS_intOptionIDSelected>null</OPTIONS_intOptionIDSelected>";
          
            strStylesheet = ADDEDIT_OPTIONS; 
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
     /* To Add New Option
     *
     *
     */
    private void saveNewOption()
    {
        
        //Vector vtLoadOptionPool = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_loadOptionPool");
        Vector vtAddOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_addOptions");
        Vector vtLoadOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_loadOptions");
        
        // To set the value of optionpool for the below parameter, so that strcheckReqdFields will not throw error
        runtimeData.setParameter("OPTIONS_intOptionPoolID",runtimeData.getParameter("OptionPoolSelected"));               
        
        try
        {
           // Get the max no in Option Order column
            int maxOptionOrder = QueryChannel.getNewestKeyAsInt("OPTIONS_intOptionOrder");                
            maxOptionOrder = maxOptionOrder + 1;
            runtimeData.setParameter("OPTIONS_intOptionOrder",new Integer(maxOptionOrder).toString());               
            
            String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddOptions, runtimeData);
            
             // checks data validation
            String strValidation = QueryChannel.validateData(vtAddOptions, runtimeData); 
            
            
            // If all required fields values are supplied, then perform insert
            if (((strCheckRequiredFields == null) || (strCheckRequiredFields.equals(""))) && ((strValidation == null) || (strValidation.equals(""))))
            {           
                
                // Add new option   
                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken);                        
                query.setDomain("OPTIONS", null, null, null);         
                query.setFields(vtAddOptions, runtimeData);                
                runtimeData.setParameter("OPTIONS_intOptionOrder",new Integer(maxOptionOrder).toString());               
                query.executeInsert();                 
                
                int intCurrentOptionID = QueryChannel.getNewestKeyAsInt(query);
                strCurrentNode = "OPTIONS_" + intCurrentOptionID;
               
                /*
                // Start lock
                
                 if (strCurrentNode.equals("OPTIONS_" + intCurrentOptionID))
                 {                     
                     clearLockRequest();
                     lockRequest = new LockRequest(authToken);
                     lockRequest.addLock("OPTIONS", new Integer(intCurrentOptionID).toString(), LockRecord.READ_WRITE);                     
                    
                 }
                else
                {                    
                     clearLockRequest();
                     lockRequest = new LockRequest(authToken);
                    lockRequest.addLock("OPTIONS", new Integer(intCurrentOptionID).toString(), LockRecord.READ_ONLY);
                    
                }                
                lockRequest.lockDelayWrite();
                
                // End lock
                */
                
                
                // To Load the page with added options details
                runtimeData.setParameter("OPTIONS_intOptionIDSelected",new Integer(intCurrentOptionID).toString());
                runtimeData.setParameter("addClicked","yes");
                buildAddEditOptions();
               

            }

            else
            { 
                buildAddEditOptions();
                strStylesheet = ADDEDIT_OPTIONS;
                runtimeData.setParameter("addClicked","yes");
                strXML+= QueryChannel.buildFormLabelXMLFile(vtLoadOptions) +
                         QueryChannel.buildViewXMLFile(vtLoadOptions, runtimeData);
                  
                strErrorMessage = strCheckRequiredFields;                 
                strXML = strXML + 
                         "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";
                
            }

            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
   
    
    /* To Update Option
     *
     *
     */
    private void doUpdateOption()
    {        
        try
        {     
            
           strStylesheet = ADDEDIT_OPTIONS;
           Vector vtAddOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_addOptions");
           Vector vtLoadOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_loadOptions");
           DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS,authToken); 
           
           
           // To set the value of optionpool for the below parameter, so that strcheckReqdFields will not throw error
           runtimeData.setParameter("OPTIONS_intOptionPoolID",runtimeData.getParameter("OptionPoolSelected"));               
        
           if (runtimeData.getParameter("OPTIONS_intOptionIDSelected") != null)
           {               

               // Get the order for this Option id, inorder to update with the same order
               DALSecurityQuery querySel = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken);     
               querySel.setDomain("OPTIONS", null, null, null); 
               querySel.setFields(vtLoadOptions, null);
               querySel.setWhere(null, 0, "OPTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);             
               querySel.setWhere("AND", 0, "OPTIONS_intOptionID", "=", runtimeData.getParameter("OPTIONS_intOptionIDSelected"), 0, DALQuery.WHERE_HAS_VALUE);                                       

                ResultSet rsResultSel = querySel.executeSelect();
                String strOptionOrder ="";
                while(rsResultSel.next())
                {
                    strOptionOrder = rsResultSel.getString("OPTIONS_intOptionOrder");   
                }
                runtimeData.setParameter("OPTIONS_intOptionOrder",strOptionOrder);
                rsResultSel.close();
                
            }
            
           
           String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddOptions, runtimeData);            
           // checks data validation
           String strValidation = QueryChannel.validateData(vtAddOptions, runtimeData);
            
            if (((strCheckRequiredFields == null) || (strCheckRequiredFields.equals(""))) && ((strValidation == null) || (strValidation.equals(""))))
            {
                
                   // perform the update
                   query.setDomain("OPTIONS", null,null,null);
                   query.setFields(vtAddOptions , runtimeData);     
                   query.setWhere(null, 0, "OPTIONS_intOptionID", "=", runtimeData.getParameter("OPTIONS_intOptionIDSelected"), 0, DALQuery.WHERE_HAS_VALUE);                               
                   query.setField("OPTIONS_intDeleted","0");
                   //runtimeData.setParameter("OPTIONS_intOptionOrder",strOptionOrder);
                   
                   //**********
                    // Start Lock                  
                   
                   if (lockRequestOptionPool.isValid() && lockRequestOptionPool.lockWrites())
                   {  
                       //System.out.println("updated option");                 
                      query.executeUpdate();
                      
                      lockRequestOptionPool.unlockWrites(); 
                      strErrorMessage = "The Option Pool is updated successfully";
                      
                      int intCurrentOptionID = Integer.parseInt(runtimeData.getParameter("OPTIONS_intOptionIDSelected"));               

                       // To Load the page with added options details
                       runtimeData.setParameter("OPTIONS_intOptionIDSelected",new Integer(intCurrentOptionID).toString());
                       buildAddEditOptions(); 

                   }
                   else
                   {      
                       //System.out.println("cannot update option");  
                       strFlag = "error";
                       buildAddEditOptions();
                       strFlag = "";
                       strErrorMessage = "This record is being viewed by other users. You cannot update it now. Please try again later. ";                       
                       strXML+= QueryChannel.buildFormLabelXMLFile(vtLoadOptions) +
                         QueryChannel.buildViewXMLFile(vtLoadOptions, runtimeData);
                       
                   }
                    strXML+= "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";   
                   // End lock
                   
                   
                   //*******
                    /*
                   int intCurrentOptionID = Integer.parseInt(runtimeData.getParameter("OPTIONS_intOptionIDSelected"));               

                   // To Load the page with added options details
                   runtimeData.setParameter("OPTIONS_intOptionIDSelected",new Integer(intCurrentOptionID).toString());
                   buildAddEditOptions();            
                   */
                   strStylesheet = ADDEDIT_OPTIONS;              

              // }
               
            }
             else
            { 
                strFlag = "Error";
                buildAddEditOptions();
                strFlag = "";
                strStylesheet = ADDEDIT_OPTIONS;                
                strXML+= QueryChannel.buildFormLabelXMLFile(vtLoadOptions) +
                         QueryChannel.buildViewXMLFile(vtLoadOptions, runtimeData);
                  
                strErrorMessage = strCheckRequiredFields;                 
                strXML+= "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";               
                
            }
            
           
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    
    /* To update the Options Order, when it is moved up & down
     *
     *
     */
    private void ChangeOptionsOrderinAddEditOptions(String strOptionId, String strMove)
    {
        try
        {
            //System.out.println("inside changeoptions");
            // Get the form fields to display all the labels and their corresponding fields            
            Vector vtLoadOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_loadOptions");
            
            String strOptionOrder = "";
            String strNewOptionId = "";
            String strNewOptionOrder = "";
           
            boolean blUpdate = false;      
            
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken);     
            query.setDomain("OPTIONS", null, null, null); 
            query.setFields(vtLoadOptions, null);
            query.setWhere(null, 0, "OPTIONS_intOptionPoolID", "=", runtimeData.getParameter("OptionPoolSelected"), 0, DALQuery.WHERE_HAS_VALUE);             
            query.setWhere("AND", 0, "OPTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);             
            query.setOrderBy("OPTIONS_intOptionOrder", "ASC");            
            
            ResultSet rsResult = query.executeSelect();
            
            while(rsResult.next())
            {
                
                if(strOptionId.equals(rsResult.getString("OPTIONS_intOptionID")))
                {                  
                    
                    strOptionOrder = rsResult.getString("OPTIONS_intOptionOrder");             
                    
                    if (strMove.equals("moveUp"))
                    {              
                               
                        if (rsResult.isFirst() == false)
                        {
                            rsResult.previous();
                            
                            blUpdate = true;
                            strNewOptionId = rsResult.getString("OPTIONS_intOptionID");
                            strNewOptionOrder = rsResult.getString("OPTIONS_intOptionOrder");
                            
                            rsResult.next();
                        }
                        else
                        {
                            blUpdate = false;
                        }
                        
                        
                    }
                    else if (strMove.equals("moveDown"))
                    {
                        if(rsResult.isLast() == false)
                        {   
                            rsResult.next();
                            
                            blUpdate = true;
                            strNewOptionId = rsResult.getString("OPTIONS_intOptionID");
                            strNewOptionOrder = rsResult.getString("OPTIONS_intOptionOrder");   
                            
                            rsResult.previous();
                        }
                        else
                        {
                            blUpdate = false;
                        }                  
                        
                    }
                    
                }
                
            }            
           
            rsResult.close();
            
            if (blUpdate == true)
            {     
                           
                // Update the passed Option Id with the new Order
                DALSecurityQuery queryUpd1 = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken);     
                queryUpd1.setDomain("OPTIONS", null, null, null);                
                
                queryUpd1.setWhere(null, 0, "OPTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                queryUpd1.setWhere("AND", 0, "OPTIONS_intOptionID", "=", strOptionId, 0, DALQuery.WHERE_HAS_VALUE);                            
                queryUpd1.setField("OPTIONS_intOptionOrder",strNewOptionOrder);
                                
                queryUpd1.executeUpdate();    
                   

                 // Update the new Option Id with the passed Option id's Order
                DALSecurityQuery queryUpd2 = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken);     
                queryUpd2.setDomain("OPTIONS", null, null, null);                
                queryUpd2.setWhere(null, 0, "OPTIONS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                queryUpd2.setWhere("AND", 0, "OPTIONS_intOptionID", "=", strNewOptionId, 0, DALQuery.WHERE_HAS_VALUE);                            
                queryUpd2.setField("OPTIONS_intOptionOrder",strOptionOrder);             
               
                queryUpd2.executeUpdate();        

            }
            
            buildAddEditOptions();
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
     /* To delete the Option in AddEditOption
     *
     *
     */
    private void deleteOptioninAddEditOption()
    {
        try
        {
            
            strCurrent = ADDEDIT_OPTIONS;
            
            // Get the form fields to display all the labels and their corresponding fields            
            Vector vtLoadOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_loadOptions");
            Vector vtDEToOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_DataElementsToOptions");
            
            /*
            // To check if the optionpool is used in any of the DataElements
            DALSecurityQuery queryChk = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken); 
            queryChk.setDomain("DATAELEMENTSTOOPTIONS", null, null, null);     
            queryChk.setFields(vtDEToOptions,null);
            queryChk.setWhere(null, 0, "DATAELEMENTSTOOPTIONS_intOptionPoolID", "=",runtimeData.getParameter("OptionPoolSelected") , 0, DALQuery.WHERE_HAS_VALUE);                            
            queryChk.setWhere("AND", 0, "DATAELEMENTSTOOPTIONS_intDeleted", "=","0" , 0, DALQuery.WHERE_HAS_VALUE);                            
                                 
            ResultSet rsResultChk = queryChk.executeSelect();
            
            // Optionpool is still being used in the dataElements
            if(rsResultChk.next())
            {     
                 buildAddEditOptions();
                 strErrorMessage = "This option is still being used in the DataElements, so unable to delete";
                 strXML+="<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";
                 
            }
            // Option is not used in any of the dataElements, so delete
            else
            {*/
                // delete the option in options table
                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                query.setDomain("OPTIONS", null, null, null);     
                query.setWhere(null, 0, "OPTIONS_intOptionID", "=",runtimeData.getParameter("OPTIONS_intOptionID") , 0, DALQuery.WHERE_HAS_VALUE);                            
                query.setField("OPTIONS_intDeleted", "-1");

                 //**********
                    // Start Lock                  
                   
                   if (lockRequestOptionPool.isValid() && lockRequestOptionPool.lockWrites())
                   {  
                       //System.out.println("delete option");                 
                      query.executeUpdate();
                      
                      lockRequestOptionPool.unlockWrites(); 
                      strErrorMessage = "The Option Pool is deleted successfully";
                      
                      //int intCurrentOptionID = Integer.parseInt(runtimeData.getParameter("OPTIONS_intOptionIDSelected"));               

                       // To Load the page with added options details
                       //runtimeData.setParameter("OPTIONS_intOptionIDSelected",new Integer(intCurrentOptionID).toString());
                       buildAddEditOptions(); 
                       
                       // Set the OptionID Selected to null                
                        runtimeData.setParameter("OPTIONS_intOptionID","null");
                        runtimeData.setParameter("OPTIONS_intOptionIDSelected","null");
                        buildAddEditOptions();

                   }
                   else
                   {      
                       //System.out.println("cannot delete option");  
                       strFlag = "error";
                       buildAddEditOptions();
                       strFlag = "";
                       strErrorMessage = "This record is being viewed by other users. You cannot delete it now. Please try again later. ";                       
                       strXML+= QueryChannel.buildFormLabelXMLFile(vtLoadOptions) +
                         QueryChannel.buildViewXMLFile(vtLoadOptions, runtimeData);
                       
                   }
                    strXML+= "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";   
                   // End lock
                   
                   
                   //*******
                
                
                //query.executeUpdate();
                //System.out.println("deleted");
                    
                 //***
                //strErrorMessage = "Option is deleted successfully";
                //strXML+="<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";
                //***
                    
                //strXML+="<OPTIONS_intOptionIDSelected>"+setVar+"</OPTIONS_intOptionIDSelected>";

               
               
                
            //}
            
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
   
     /* To save the DataElement with current option pool
     *
     *
     */
    private void linkOptionPoolToDataElement()
    {
        try
        {
            strCurrent = ADDEDIT_OPTIONS;
            
            // Get the form fields to display all the labels and their corresponding fields            
            Vector vtLoadOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_loadOptions");
            Vector vtDEToOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_DataElementsToOptions");
            
            
             // Start Lock                  
                   
               if (lockRequestDEPool.isValid() && lockRequestDEPool.lockWrites())
               {           
            
                    // To check if the DataElement already has a optionpool linked
                    DALSecurityQuery querySel = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken);                        
                    querySel.setDomain("DATAELEMENTSTOOPTIONS", null, null, null);         
                    querySel.setFields(vtDEToOptions, null);   
                    querySel.setWhere(null, 0, "DATAELEMENTSTOOPTIONS_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                            
                    querySel.setWhere("AND", 0, "DATAELEMENTSTOOPTIONS_intDeleted", "=","0" , 0, DALQuery.WHERE_HAS_VALUE);                            
                    ResultSet rsquerySel = querySel.executeSelect();

                    //if a optionpool is already saved for this dataelement, then update optionpool
                    if(rsquerySel.next())
                    {
                        // update the optionpool in options table
                        DALSecurityQuery queryDel = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken); 
                        queryDel.setDomain("DATAELEMENTSTOOPTIONS", null, null, null);     
                        queryDel.setWhere(null, 0, "DATAELEMENTSTOOPTIONS_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                            
                        queryDel.setField("DATAELEMENTSTOOPTIONS_intOptionPoolID",runtimeData.getParameter("OptionPoolSelected"));                            
                        queryDel.executeUpdate();               

                    }
                    else
                    {
                        // Add option pool to DataElement  
                        DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_ADDEDIT_OPTIONS, authToken);                        
                        query.setDomain("DATAELEMENTSTOOPTIONS", null, null, null);                    
                        query.setField("DATAELEMENTSTOOPTIONS_intDataElementID",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"));               
                        query.setField("DATAELEMENTSTOOPTIONS_intOptionPoolID",runtimeData.getParameter("OptionPoolSelected"));               
                        query.executeInsert();  

                    }
                    
                    rsquerySel.close();
                    //query.executeUpdate();
                    lockRequest.unlockWrites(); 

                    // After saving this option pool for the dataElement, go back to Add/Edit DataElements page
                    strCurrent = ADDEDIT_DATAELEMENTS;
                    strStylesheet = ADDEDIT_DATAELEMENTS;

                    strErrorMessage = "The selected OptionPool is linked to the DataElement successfully";

               }
               else
               {                       
                   strErrorMessage = "The DataElementPool is being viewed by other users. You cannot link it now. Please try again later. ";                       
                   buildAddEditOptions();
               }
               // End lock
            
            strXML+="<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    /* onclick of add Option Pool, in Add/Edit Options
     *
     *
     */
    private void displayAddOptionPool()
    {
        try
        {
            strCurrent = ADD_OPTIONPOOL;
            
             // Get the form fields to display all the labels and their corresponding fields
            Vector vtViewOptionPool = DatabaseSchema.getFormFields("csmartformadmin_view_OptionPool");
            
            // pass the fields for displaying Add/Edit DE if back button is clicked
            strXML = QueryChannel.buildFormLabelXMLFile(vtViewOptionPool) +
                     QueryChannel.buildAddFormXMLFile(vtViewOptionPool) +
                     "<OptionPoolSelected>"+runtimeData.getParameter("OptionPoolSelected")+"</OptionPoolSelected>" +
                     "<OPTIONS_intOptionIDSelected>"+runtimeData.getParameter("OPTIONS_intOptionIDSelected")+"</OPTIONS_intOptionIDSelected>" +
                     "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                     "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                     "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";
            
            strStylesheet = ADD_OPTIONPOOL; 
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
     /* To Add new Option Pool
     *
     *
     */
    private void saveNewOptionPool()
    {
        
       Vector vtAddOptionPool = DatabaseSchema.getFormFields("csmartformadmin_add_OptionPool"); 
       Vector vtViewOptionPool = DatabaseSchema.getFormFields("csmartformadmin_view_OptionPool");       
        
        
        try
        {
           
            String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddOptionPool, runtimeData);
            
             // checks data validation
            String strValidation = QueryChannel.validateData(vtAddOptionPool, runtimeData);               
            
            // If all required fields values are supplied, then perform insert
            if (((strCheckRequiredFields == null) || (strCheckRequiredFields.equals(""))) && ((strValidation == null) || (strValidation.equals(""))))
            {                              
                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_OPTIONPOOL_INSERT, authToken);                

                // Add new smartform                
                query.setDomain("OPTIONPOOL", null, null, null);         
                query.setFields(vtAddOptionPool, runtimeData);
                query.executeInsert();                

                int intCurrentOptionPoolID = QueryChannel.getNewestKeyAsInt(query);
                /*
                // Start lock
                
                 if (strCurrentNode.equals("OPTIONPOOL_" + intCurrentOptionPoolID))
                 {                     
                     clearLockRequest();
                     lockRequest = new LockRequest(authToken);
                     lockRequest.addLock("OPTIONPOOL", new Integer(intCurrentOptionPoolID).toString(), LockRecord.READ_WRITE);                     
                    
                 }
                else
                {                    
                     clearLockRequest();
                     lockRequest = new LockRequest(authToken);
                    lockRequest.addLock("OPTIONPOOL", new Integer(intCurrentOptionPoolID).toString(), LockRecord.READ_ONLY);
                    
                }                
                lockRequest.lockDelayWrite();
                
                // End lock
                */
                
                // After adding new Option Pool, go back to Add/Edit Options page with added record
                
                runtimeData.setParameter("OptionPoolSelected",new Integer(intCurrentOptionPoolID).toString());
                
                buildAddEditOptions();
                strCurrent = ADDEDIT_OPTIONS;
                strStylesheet = ADDEDIT_OPTIONS;              
               
            }

            else
            { 
                
                strStylesheet = ADD_OPTIONPOOL;                
                strXML = QueryChannel.buildFormLabelXMLFile(vtViewOptionPool) +
                         QueryChannel.buildViewXMLFile(vtViewOptionPool, runtimeData);
                  
                strErrorMessage = strCheckRequiredFields;                 
                strXML = strXML + 
                         "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";
                
                          
            }
            
             // These values are required to access Add/Edit DataElements page, if back button is clicked
            strXML+= "<OptionPoolSelected>"+runtimeData.getParameter("OptionPoolSelected")+"</OptionPoolSelected>" +
                     "<OPTIONS_intOptionIDSelected>"+runtimeData.getParameter("OPTIONS_intOptionIDSelected")+"</OPTIONS_intOptionIDSelected>" +
                     "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                     "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                     "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";


            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
   
    
     /* To Display OptionPool Details of the Pool selected, on click of Edit Option Pool in Add/Edit Options
     *
     *
     */
    private void displayOptionPoolDetails()
    {        
        try
        {
            //System.out.println("optionpool:"+runtimeData.getParameter("OPTIONPOOL_intOptionPoolID"));
            
            if (runtimeData.getParameter("OPTIONPOOL_intOptionPoolID").equals("null"))
            {
                //System.out.println("optionpool is null");                
                buildAddEditOptions();
                strErrorMessage = "Please add new or select the existing Option Pool";
                strXML+= "<strErrorMessage>"+strErrorMessage+"</strErrorMessage>";
                
            }
            
            else
            {
                //System.out.println("optionpool is not null");
                Vector vtViewOptionPool = DatabaseSchema.getFormFields("csmartformadmin_view_OptionPool");     

                int intOptionPoolID = Integer.parseInt(runtimeData.getParameter("OPTIONPOOL_intOptionPoolID"));

                strXML = QueryChannel.buildFormLabelXMLFile(vtViewOptionPool) +
                         QueryChannel.buildViewFromKeyXMLFile(vtViewOptionPool,"OPTIONPOOL_intOptionPoolID",intOptionPoolID);

                strStylesheet =VIEW_OPTIONPOOL;
            }
            
           
            /*
            // Start Lock
            
            if (new Integer(intOptionPoolID).toString() != null)
            {               
                clearLockRequest();
                lockRequest = new LockRequest(authToken);
                lockRequest.addLock("OPTIONPOOL", new Integer(intOptionPoolID).toString(), LockRecord.READ_WRITE);
            }
            
            lockRequest.lockDelayWrite();
            // End lock
             */
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }  
   
    
     /* To Update OptionPool, on click of update in Edit Option Pool(in Add/Edit Options)
     *
     *
     */
    private void doUpdateOptionPool()
    {        
        try
        {     
           strStylesheet = VIEW_OPTIONPOOL;
           Vector vtViewOptionPool = DatabaseSchema.getFormFields("csmartformadmin_view_OptionPool"); 
           Vector vtEditOptionPool = DatabaseSchema.getFormFields("csmartformadmin_add_OptionPool");
           
           
           DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_OPTIONPOOL_UPDATE,authToken);     
           
           String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtEditOptionPool, runtimeData);            
           // checks data validation
           String strValidation = QueryChannel.validateData(vtEditOptionPool, runtimeData);
           
            
            if (((strCheckRequiredFields == null) || (strCheckRequiredFields.equals(""))) && ((strValidation == null) || (strValidation.equals(""))))
            {
                
               if (runtimeData.getParameter("OPTIONPOOL_intOptionPoolID") != null)
               {           
                   query.setDomain("OPTIONPOOL", null,null,null);
                   query.setFields(vtEditOptionPool, runtimeData);     
                   query.setWhere(null, 0, "OPTIONPOOL_intOptionPoolID", "=", runtimeData.getParameter("OPTIONPOOL_intOptionPoolID"), 0, DALQuery.WHERE_HAS_VALUE);                               
                   query.setField("OPTIONPOOL_intDeleted","0");
                   
                   
                    // Start Lock                  
                   
                   if (lockRequestOptionPool.isValid() && lockRequestOptionPool.lockWrites())
                   {  
                                       
                   
                        query.executeUpdate();
                   
                   
                      lockRequestOptionPool.unlockWrites();                   
                   
                      // After adding new DataELement Pool, go back to Add/Edit DataElements page
                    buildAddEditOptions();
                    strCurrent = ADDEDIT_OPTIONS;
                    strStylesheet = ADDEDIT_OPTIONS;  

                    // These values are required to access Add/Edit DataElements page, if back button is clicked
                    strXML+= "<OptionPoolSelected>"+runtimeData.getParameter("OptionPoolSelected")+"</OptionPoolSelected>" +
                             "<OPTIONS_intOptionIDSelected>"+runtimeData.getParameter("OPTIONS_intOptionIDSelected")+"</OPTIONS_intOptionIDSelected>" +
                             "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                             "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                             "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";
                             
                        strErrorMessage = "The Option Pool is updated successfully";
                   
                   }
                   else
                   {                       
                       strErrorMessage = "This record is being viewed by other users. You cannot update it now. Please try again later. ";                       
                       //displayViewDataElementPool();
                       strStylesheet = VIEW_OPTIONPOOL;                
                        strXML = QueryChannel.buildFormLabelXMLFile(vtViewOptionPool) +
                         QueryChannel.buildViewXMLFile(vtViewOptionPool, runtimeData);
                  
                   }
                   // End lock
                       
                   
                      /*  
                    // After adding new DataELement Pool, go back to Add/Edit DataElements page
                    buildAddEditOptions();
                    strCurrent = ADDEDIT_OPTIONS;
                    strStylesheet = ADDEDIT_OPTIONS;  

                    // These values are required to access Add/Edit DataElements page, if back button is clicked
                    strXML+= "<OptionPoolSelected>"+runtimeData.getParameter("OptionPoolSelected")+"</OptionPoolSelected>" +
                             "<OPTIONS_intOptionIDSelected>"+runtimeData.getParameter("OPTIONS_intOptionIDSelected")+"</OPTIONS_intOptionIDSelected>" +
                             "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                             "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                             "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";
                             */

               }
               
            }
             else
            {                
                strStylesheet = VIEW_OPTIONPOOL;                
                strXML = QueryChannel.buildFormLabelXMLFile(vtViewOptionPool) +
                         QueryChannel.buildViewXMLFile(vtViewOptionPool, runtimeData);
                  
                strErrorMessage = strCheckRequiredFields;                 
               
            }
            strXML+= "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";                
            
           
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
   
    
     /* To Delete Option Pool
     *
     *
     */
    private void doDeleteOptionPool()
    {
        
        try
        {     
                      
            Vector vtViewOptionPool = DatabaseSchema.getFormFields("csmartformadmin_view_OptionPool");  
            Vector vtDEToOptions = DatabaseSchema.getFormFields("csmartformadmin_addEditOptions_DataElementsToOptions");
            
            // To check if the DataElement already has a optionpool linked
            DALSecurityQuery querySel = new DALSecurityQuery(SMARTFORM_OPTIONPOOL_DELETE, authToken);                        
            querySel.setDomain("DATAELEMENTSTOOPTIONS", null, null, null);         
            querySel.setFields(vtDEToOptions, null);   
            querySel.setWhere(null, 0, "DATAELEMENTSTOOPTIONS_intOptionPoolID", "=",runtimeData.getParameter("OptionPoolSelected") , 0, DALQuery.WHERE_HAS_VALUE);                            
            querySel.setWhere("AND", 0, "DATAELEMENTSTOOPTIONS_intDeleted", "=","0" , 0, DALQuery.WHERE_HAS_VALUE);                            
            ResultSet rsquerySel = querySel.executeSelect();
            
            // In DataElementsto options, if record exist for this option pool, can't delete
            if (rsquerySel.next())
            {                
                strErrorMessage = "This OptionPool is still being used in the DataElements, so unable to delete.";
                displayViewOptionPool();
              
            }
            // if no record exist for this option pool, then delete
            else
            {
            
                if (runtimeData.getParameter("OPTIONPOOL_intOptionPoolID") != null)
                 {
                    DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_OPTIONPOOL_DELETE, authToken);  
                    query.setDomain("OPTIONPOOL", null, null, null);  
                    query.setFields(vtViewOptionPool, runtimeData);                
                    query.setWhere(null, 0, "OPTIONPOOL_intOptionPoolID", "=", runtimeData.getParameter("OPTIONPOOL_intOptionPoolID"), 0, DALQuery.WHERE_HAS_VALUE);                
                    query.setField("OPTIONPOOL_intDeleted", "-1");                

                    
                     //Locking

                       if (lockRequestOptionPool.isValid() && lockRequestOptionPool.lockWrites())
                       {  
                                         
                          query.executeUpdate();

                       
                          lockRequestOptionPool.unlockWrites();  
                        
                          strErrorMessage = "The Option Pool is deleted successfully";

                          //**
                          // After adding new DataELement Pool, go back to Add/Edit Options page by setting Option Pool id as below
                          runtimeData.setParameter("OPTIONPOOL_intOptionPoolID","null");
                          //runtimeData.setParameter("OptionPoolSelected","null");
                          
                          //**
                          // go back to Add/Edit Options page
                          buildAddEditOptions();
                          strCurrent = ADDEDIT_OPTIONS;
                          strStylesheet = ADDEDIT_OPTIONS;  

                       
                          

                       }
                       else
                       {                       
                           strErrorMessage = "This record is being viewed by other users. You cannot delete it now. Please try again later. ";
                           displayViewOptionPool();   
                                             

                       }
                                          

                 }
                
            }
            
            rsquerySel.close();
            
              // These values are required to access Add/Edit DataElements page, if back button is clicked
                strXML+= "<OptionPoolSelected>"+runtimeData.getParameter("OptionPoolSelected")+"</OptionPoolSelected>" +
                         "<OPTIONS_intOptionIDSelected>"+runtimeData.getParameter("OPTIONS_intOptionIDSelected")+"</OPTIONS_intOptionIDSelected>" +
                         "<SMARTFORM_intSmartformID>"+runtimeData.getParameter("SMARTFORM_intSmartformID")+"</SMARTFORM_intSmartformID>" +
                         "<DEPoolSelected>"+runtimeData.getParameter("DEPoolSelected")+"</DEPoolSelected>" +
                         "<DATAELEMENTS_intDataElementIDselected>"+runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected")+"</DATAELEMENTS_intDataElementIDselected>";

                strXML+= "<strErrorMessage>"+ strErrorMessage + "</strErrorMessage>";   
               
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }    
    
     /* To display the same page again, after updating or deleting the option Pool
     *
     *
     */
    private void displayViewOptionPool()
    {
        try
        {
            strCurrent = VIEW_OPTIONPOOL;
            
            // Get the form fields to display all the labels and their corresponding fields
            Vector vtViewOptionPool = DatabaseSchema.getFormFields("csmartformadmin_view_OptionPool");   

            strXML = QueryChannel.buildFormLabelXMLFile(vtViewOptionPool) +
                     QueryChannel.buildViewXMLFile(vtViewOptionPool, runtimeData);      
            
            strStylesheet = VIEW_OPTIONPOOL; 
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
        
    }
    
    
    /* Before deleting Smartforms Dataelement, change(move up) the number and order of the other
     * dataelements by moving the dataelement to be deleted to the last no and order
     *
     */
    private void updateNoAndOrderBeforeDelete()
    {
        try
        {
           
            // Get the form fields to display all the labels and their corresponding fields            
            Vector vtBuildSmartFormsDataElements = DatabaseSchema.getFormFields("csmartformadmin_buildsmartform_SmartformtoDataElementsOnly");
            
            // Get the order of the dataelement id, which is to be deleted
            String strOrder = "";            
            DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
            query.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);                  
            query.setFields(vtBuildSmartFormsDataElements, null);
            query.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);                
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementID", "=",runtimeData.getParameter("SMARTFORMTODATAELEMENTS_DEIdSelected") , 0, DALQuery.WHERE_HAS_VALUE);               
            query.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);               
            
            ResultSet rs = query.executeSelect();      
            
            while(rs.next())
            {
                strOrder = rs.getString("SMARTFORMTODATAELEMENTS_intDataElementOrder");
            }
            
            //Get all the Dataelements for this Smartform whose order is greater than strOrder(order of the DE to be deleted)
            DALSecurityQuery query1 = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
            query1.setDomain("SMARTFORMTODATAELEMENTS", null, null, null);                  
            query1.setFields(vtBuildSmartFormsDataElements, null);
            query1.setWhere(null, 0, "SMARTFORMTODATAELEMENTS_intSmartformID", "=", runtimeData.getParameter("SMARTFORM_intSmartformID"), 0, DALQuery.WHERE_HAS_VALUE);                
            query1.setWhere("AND", 0, "SMARTFORMTODATAELEMENTS_intDataElementOrder", ">", strOrder, 0, DALQuery.WHERE_HAS_VALUE);               
            query1.setOrderBy("SMARTFORMTODATAELEMENTS_intDataElementOrder","ASC");
            ResultSet rs1 = query1.executeSelect();
            //get all the Dataelements, whose order is > than the order of the DE to be deleted
            while(rs1.next())
            {
                //System.out.println("DE id to be moved up: "+rs1.getString("SMARTFORMTODATAELEMENTS_intDataElementID"));
                // Move Up - the dataelement which is > than strOrder , then move the next up
                ChangeDEOrder(runtimeData.getParameter("SMARTFORM_intSmartformID"), rs1.getString("SMARTFORMTODATAELEMENTS_intDataElementID"), "moveUp");
                
            }
            
            rs.close();
            rs1.close();
            
            
            
        }
        catch (Exception e)
        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }      
       
    }
    
    
    // Additional Info Start
     /* To get the values of fields selected to be shown as
     * additional info in Smartform Completion
     *
     */   
    
     private void getLookupsAdditionalInfo(){

        try{
            
            //System.out.println("inside display fields checking");
            
            // Type System Lookup - Get Lookup Object names and its field names
            Hashtable hashLookupObjs = DatabaseSchema.getLookupObjects();
            Enumeration key_enum = hashLookupObjs.keys();            
            while (key_enum.hasMoreElements())
            {   
                // Get Lookup Object Names
                String lookupObjName =  (String)key_enum.nextElement();                
                //System.out.println("lookupObjName:"+lookupObjName);                
                
                 // Get the lookup Objects Field Names
                Vector vtLookupFieldNames = DatabaseSchema.getLookupObjectFields(lookupObjName);

                for(int i=0; i< vtLookupFieldNames.size();i++)
                {            
                    LookupObjectField obj = (LookupObjectField)vtLookupFieldNames.elementAt(i);
                    //System.out.println("internal name:"+obj.getName());              
                    
                    // If this name can be displayed as a label with checkbox
                    if (obj.isInFieldList())
                    {                                               
                        String strInternalName = obj.getName();
                        //System.out.println("internal name in field list:"+strInternalName);
                        
                        // Get the form fields to display all the labels and their corresponding fields
                        Vector vtBuildLookupField = DatabaseSchema.getFormFields("csmartformadmin_addEditDataElements_SystemLookupField");

                        // Check if this internal name is already present for this DataElement key in SystemLookupField table
                        DALSecurityQuery querySel = new DALSecurityQuery(SMARTFORM_BUILD, authToken); 
                        querySel.setDomain("SYSTEMLOOKUPFIELD", null, null, null);     
                        querySel.setFields(vtBuildLookupField,null);
                        querySel.setWhere(null, 0, "SYSTEMLOOKUPFIELD_intDataElementID", "=",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected") , 0, DALQuery.WHERE_HAS_VALUE);                            
                        querySel.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_strInternalName", "=", strInternalName, 0, DALQuery.WHERE_HAS_VALUE);   
                        querySel.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_intDeleted", "=","0" , 0, DALQuery.WHERE_HAS_VALUE);      
                        ResultSet rsResultSel = querySel.executeSelect();

                        
                        String strIntlName = "SYSTEMLOOKUPFIELD_strInDisplayAddnl";
                        String displayName = strIntlName.concat(strInternalName);
                        //System.out.println("displayName:"+displayName);
                        //System.out.println("displayName getparam:"+runtimeData.getParameter(displayName));
                        
                        // Check if the checkbox is selected and then insert
                        if(runtimeData.getParameter(displayName) != null)
                        {
                            //System.out.println(strInternalName+" selected");
                            
                            
                            // If the selected internal name already exist, then do update
                            if (rsResultSel.next())
                            {                                
                                //Set the Domains & runtimeData & other fields to do Insert
                                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                                query.setDomain("SYSTEMLOOKUPFIELD", null, null, null);            
                                query.setWhere(null, 0, "SYSTEMLOOKUPFIELD_intDataElementLookupFieldID", "=",rsResultSel.getString("SYSTEMLOOKUPFIELD_intDataElementLookupFieldID") , 0, DALQuery.WHERE_HAS_VALUE);                                        
                                query.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_intDeleted", "=","0" , 0, DALQuery.WHERE_HAS_VALUE);  
                                //query.setField("SYSTEMLOOKUPFIELD_strFieldOrder", runtimeData.getParameter("SYSTEMLOOKUPFIELD_strFieldOrder"+obj.getName()));
                                query.setField("SYSTEMLOOKUPFIELD_strInDisplayAddnl", "0");
                                query.executeUpdate(); 
                                //System.out.println("updated display addnl to 0");
                                
                                
                            }
                            // if the selected internal name does not exist already, then do insert
                            else
                            {
                                //Set the Domains & runtimeData & other fields to do Insert
                                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                                query.setDomain("SYSTEMLOOKUPFIELD", null, null, null);            
                                query.setField("SYSTEMLOOKUPFIELD_intDataElementID",runtimeData.getParameter("DATAELEMENTS_intDataElementIDselected"));
                                query.setField("SYSTEMLOOKUPFIELD_strInternalName",strInternalName);  
                                query.setField("SYSTEMLOOKUPFIELD_strInDropdown", "-1");
                                query.setField("SYSTEMLOOKUPFIELD_strInDisplayAddnl", "0");
                                //query.setField("SYSTEMLOOKUPFIELD_strFieldOrder", runtimeData.getParameter("SYSTEMLOOKUPFIELD_strFieldOrder"+obj.getName()));
                                query.executeInsert();
                                //System.out.println("inserted display addnl 0");
                                
                            }
                             

                            
                        }
                        // if checkbox is not selected
                        else
                        {
                            // If the deselected internal name exist, then do delete
                            if (rsResultSel.next())
                            {
                                
                                //Set the Domains & runtimeData & other fields to do Insert
                                DALSecurityQuery query = new DALSecurityQuery(SMARTFORM_BUILD, authToken);
                                query.setDomain("SYSTEMLOOKUPFIELD", null, null, null);            
                                query.setWhere(null, 0, "SYSTEMLOOKUPFIELD_intDataElementLookupFieldID", "=",rsResultSel.getString("SYSTEMLOOKUPFIELD_intDataElementLookupFieldID") , 0, DALQuery.WHERE_HAS_VALUE);                                                                        
                                query.setWhere("AND", 0, "SYSTEMLOOKUPFIELD_intDeleted", "=","0" , 0, DALQuery.WHERE_HAS_VALUE);                                  
                                query.setField("SYSTEMLOOKUPFIELD_strInDisplayAddnl", "-1");
                                query.executeUpdate(); 
                                //System.out.println("updated display addnl to -1");
                                
                                
                            }
                            
                        }               
                        
                        rsResultSel.close();
                        
                    } // end of if isinfieldlist
                    
                    
                    
                }  // end of for  
                
            } // end of while   

        }catch (Exception e){

            LogService.instance().log(LogService.ERROR, "Unknown error in Smartform Channel - " + e.toString(), e);

        }
     }  
    // Additional Info End
    
    
}




  
