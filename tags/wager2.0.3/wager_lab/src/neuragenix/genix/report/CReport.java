/**
 * CReport.java
 * Copyright � 2003 Neuragenix, Inc .  All rights reserved.
 * Date: 14/11/2003
 */

package neuragenix.genix.report;

/**
 * Report channel
 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>
 *
 * Modified on: 05/08/2004
 * @author <a href="mailto:dmurley@neuragenix.com">Daniel Murley</a>
 * Description: Implementation of customization of parameters for reports
 *
 *
 * Modified on: 26/02/2004
 * @author <a href="mailto:abalraj@neuragenix.com">Anita Balraj</a>
 * Description: Secure Download Implementation
 *
 * Modified on: 17/11/2004
 * @author <a href="mailto:sparappat@neuragenix.com">Seena Parappat</a>
 * Description: Implementation of requirement for separate report edit form
 *
 */

// java packages
import java.io.*;
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.sql.ResultSet;
import java.util.StringTokenizer;
import java.util.Calendar;
import java.util.GregorianCalendar;
import javax.swing.tree.DefaultMutableTreeNode;

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
import org.jasig.portal.RDBMServices;

// neuragenix packages
import neuragenix.security.AuthToken;
import neuragenix.dao.*;
import neuragenix.common.*;

//Start Secure Download Implementation

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

//End Secure


public class CReport implements IChannel
{  
    // channel name for tree
    private static final String CHANNEL_NAME = "report";
    
    // query file footer
    private static final String QUERY_FOOTER_1 = "</jp:mondrianQuery>\n" +
                                               "<c:set var=\"title01\" scope=\"session\">\n";
    
    private static final String QUERY_FOOTER_2 = "</c:set>";
    
    // Directory that store OLAP Schema
    private static final String OLAPSCHEMA_DIRECTORY = PropertiesManager.getProperty("neuragenix.genix.report.OLAPSchemaLocation");
    
    // Directory that store report files
    private static final String REPORT_DIRECTORY = PropertiesManager.getProperty("neuragenix.genix.report.ReportLocation");
    
    // Server URL
    private static final String SERVER_URL = PropertiesManager.getProperty("neuragenix.genix.serverURL");
    
    // query HEADER
    private static String QUERY_HEADER;
    
    // pages & activities
    private final String REPORT_LIST = "report_list";
    private final String REPORT_CATEGORY_ADD = "report_category_add";
    private final String REPORT_CATEGORY_VIEW = "report_category_view";
    private final String REPORT_RESULT = "report_result";
    private final String REPORT_ADD = "report_add";
    private final String REPORT_EDIT = "report_edit";
    private final String REPORT_VIEW = "report_view";
    private final String REPORT_UPDATE = "report_update";
    private final String REPORT_DELETE = "report_delete";
    private final String SECURITY = "security";
    private final String NODE_LINK = "node_link";
    
    // channel's runtime & static data
    private ChannelStaticData staticData;
    private ChannelRuntimeData runtimeData;
    private ChannelRuntimeData currentRuntimeData;
    private ChannelRuntimeData backRuntimeData;
   
    private AuthToken authToken ;
    private String strSessionUniqueID;
    private String strActivity;
    private String strTarget;
    private String strCurrent;
    private String strXML = "";
    private String strBackXML = "";
    private String strPreviousXML = "";
    private String strStylesheet;
    private String strBackStylesheet = REPORT_VIEW;
    private String strPreviousStylesheet;
    private String strTreeXML = "";
    private String strBackTreeXML = "";
    private String strFormXML = "";
    private String strBackFormXML = "";
    private String strPreviousFormXML = "";
    private String strErrorMessage;
    private boolean firstTime = false;
    private boolean blFirstTimeEdit = false;
    private int intEditedReportKey;
    private String strCurrentLocalNode = "";

    
    //Start Secure Download Implementation    
    private String nodeId;
    //End Secure
    
    // Agus tree
    private Hashtable hashGlobalExpanded = new Hashtable(50);
    
    /** Lock request object to handle record locking
     */
    private LockRequest lockRequest = null;
    
    /** Contructs the Inventory Channel
     */
    public CReport()
    {
        this.strStylesheet = REPORT_CATEGORY_ADD;
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
     *  Process layout-level events coming from the portal.
     *  Satisfies implementation of IChannel Interface.
     *
     *  @param PortalEvent ev a portal layout event
     */
    public void receiveEvent(PortalEvent ev)
    {
        // If the user logout, destroy lock request object
        if (ev.getEventNumber() == PortalEvent.SESSION_DONE)
        {
            clearLockRequest();           
            ReportChannelsManager.unregisterReportChannelObject(authToken.getSessionUniqueID());
        }
        
        
    }

    /**
     *  Receive static channel data from the portal.
     *  Satisfies implementation of IChannel Interface.
     *
     *  @param ChannelStaticData sd static channel data
     */
    public void setStaticData(ChannelStaticData sd)
    {
   /*     QUERY_HEADER = "<%@ page session=\"true\" contentType=\"text/html; charset=ISO-8859-1\" %>\n" +
                       "<%@ taglib uri=\"http://www.tonbeller.com/jpivot\" prefix=\"jp\" %>\n" +
                       "<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jstl/core\" %>\n" +
                       "<jp:mondrianQuery id=\"query01\" jdbcDriver=\"" + RDBMServices.getJdbcDriver() + 
                       "\" jdbcUrl=\"" + RDBMServices.getJdbcUrl() + "?user=" + RDBMServices.getJdbcUser() + 
                       "&password=postgres\" catalogUri=\"/WEB-INF/queries/SchemaFileName.xml\">\n";
     */   
        
        QUERY_HEADER = "<%@ page session=\"true\" contentType=\"text/html; charset=ISO-8859-1\" %>\n" +
                       "<%@ taglib uri=\"http://www.tonbeller.com/jpivot\" prefix=\"jp\" %>\n" +
                       "<%@ taglib prefix=\"c\" uri=\"http://java.sun.com/jstl/core\" %>\n" +
                       "<jp:mondrianQuery id=\"query01\" jdbcDriver=\"" + RDBMServices.getJdbcDriver() + 
                       "\" jdbcUrl=\"" + RDBMServices.getJdbcUrl() + "\" catalogUri=\"/WEB-INF/queries/SchemaFileName.xml\">\n";
        
        firstTime = true;
        this.staticData = sd;
        this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");
               
        // register session to report manager
        ReportChannelsManager.registerReportChannelObject(authToken.getSessionUniqueID(), this);
        
        // create domains & formfields for this channel
        InputStream file = CReport.class.getResourceAsStream("ReportDBSchema.xml");
        DatabaseSchema.loadDomains(file, "ReportDBSchema.xml");
        
        //Start Secure Download Implementation
    
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
            SessionManager.addChannelID(strSessionUniqueID, "Download",
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

        //End Secure 

        
        // load OLAP schema
        File olapfile = new File(OLAPSCHEMA_DIRECTORY + "/OLAPSchema.xml");
        OLAPSchema.loadOLAPSchema(olapfile);
        
        // load report tree structure
        file = CReport.class.getResourceAsStream("ReportTreeStructure.xml");
        TreeSchema.loadDomains(file, "ReportTreeStructure.xml");
        
        // register this object
        ReportChannelsManager.registerReportChannelObject(authToken.getSessionUniqueID(), this);
    }

    /**
     *  Receive channel runtime data from the portal.
     *  Satisfies implementation of IChannel Interface.
     *
     *  @param ChannelRuntimeData rd handle to channel runtime data
     */
    public void setRuntimeData(ChannelRuntimeData rd)
    {
        
        try
        {
            this.currentRuntimeData = rd;
            this.runtimeData = rd;
            strErrorMessage = "";
            
            // A back button has been pressed             
            if( rd.getParameter("Back" ) != null )
            {
                this.runtimeData = backRuntimeData;  
                strXML = strBackXML;
                strFormXML = strBackFormXML;
                strTreeXML = strBackTreeXML;
                strStylesheet = strBackStylesheet;
            }
            
            // Get the current page the users is on
            if (runtimeData.getParameter("current") != null)
            {
                firstTime = false;
                strCurrent = runtimeData.getParameter("current");
            }
            // if first time go to channel, default page is add report category
            else if (firstTime)
            {
                LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
                firstTime = false;
                strCurrent = REPORT_CATEGORY_ADD;
            }
            else
            {
                LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
                strCurrent = null;
            }

            if (strCurrent != null)
            {
                // user clicked on a tree node
                if (strCurrent.equals(NODE_LINK))
                {
                    // a link to a Report Category
                    if (runtimeData.getParameter("domain").equals("ReportCategory"))
                    {
                        displayReportCategoryViewForm();
                    }
                    // a link to a Report
                    else
                    {
                        //doRunReport();
                        displayReportViewForm();
                    }
                }
                else if (strCurrent.equals(REPORT_CATEGORY_ADD))
                {
                    // user saves new report category
                    if (runtimeData.getParameter("save") != null)
                    {
                        // if the user doesn't have permission to add report
                        // display unauthorization message
                        if (!authToken.hasActivity(REPORT_ADD))
                        {
                            strXML  = buildSecurityErrorXMLFile("add report category");
                            strStylesheet = SECURITY;
                            strPreviousXML = strXML;  
                            strPreviousStylesheet = strStylesheet;
                            return;
                        }
                        doAddReportCategory();
                    }
                    else
                    {
                        displayReportCategoryAddForm();
                    }
                    
                }
                else if (strCurrent.equals(REPORT_CATEGORY_VIEW))
                {
                    // user updates report category
                    if (runtimeData.getParameter("save") != null)
                    {
                        // if the user doesn't have permission to update report category
                        // display unauthorization message
                        if (!authToken.hasActivity(REPORT_UPDATE))
                        {
                            strXML  = buildSecurityErrorXMLFile("update report category");
                            strStylesheet = SECURITY;
                            strPreviousXML = strXML;  
                            strPreviousStylesheet = strStylesheet;
                            return;
                        }
                        doUpdateReportCategory();
                    }
                    // user deletes report category
                    else if (runtimeData.getParameter("delete") != null)
                    {
                        // if the user doesn't have permission to update report category
                        // display unauthorization message
                        if (!authToken.hasActivity(REPORT_DELETE))
                        {
                            strXML  = buildSecurityErrorXMLFile("delete report category");
                            strStylesheet = SECURITY;
                            strPreviousXML = strXML;  
                            strPreviousStylesheet = strStylesheet;
                            return;
                        }
                        doDeleteReportCategory();
                    }
                    else
                    {
                        displayReportCategoryViewForm();
                    }
                }
                else if (strCurrent.equals(REPORT_ADD))
                {   

                    // user saves new report
                    if (runtimeData.getParameter("save") != null)
                    {
                        // if the user doesn't have permission to add report
                        // display unauthorization message
                        if (!authToken.hasActivity(REPORT_ADD))
                        {
                            strXML  = buildSecurityErrorXMLFile("add report");
                            strStylesheet = SECURITY;
                            strPreviousXML = strXML;  
                            strPreviousStylesheet = strStylesheet;
                            return;
                        }

                        doAddReport();
                    }
                    else if (runtimeData.getParameter("delete") != null)
                    {
                        // Cancels the add report function and
                        // reverts to displaying the report catogory add form
                        displayReportCategoryAddForm();
                    }    
                    else
                    {
                        // if the user doesn't have permission to view report
                        // display unauthorization message
                        if (!authToken.hasActivity(REPORT_VIEW))
                        {
                            strXML  = buildSecurityErrorXMLFile("view report");
                            strStylesheet = SECURITY;
                            strPreviousXML = strXML;  
                            strPreviousStylesheet = strStylesheet;
                            return;
                        }

                        displayReportAddForm();
                    }

                }
                else if (strCurrent.equals(REPORT_EDIT))
                {
                    // Set the report key to that of the report being edited
                    runtimeData.setParameter("REPORT_intReportKey", Integer.toString(intEditedReportKey));
                    // user saves new report
                    if (runtimeData.getParameter("save") != null)
                    {
                        // if the user doesn't have permission to update report
                        // display unauthorization message
                        if (!authToken.hasActivity(REPORT_UPDATE))
                        {
                            strXML  = buildSecurityErrorXMLFile("update report");
                            strStylesheet = SECURITY;
                            strPreviousXML = strXML;  
                            strPreviousStylesheet = strStylesheet;
                            return;
                        }

                        doUpdateReport();

                    }
                    // user wants to delete this report
                    else if (runtimeData.getParameter("delete") != null)
                    {
                        // if the user doesn't have permission to view report
                        // display unauthorization message
                        if (!authToken.hasActivity(REPORT_DELETE))
                        {
                            strXML  = buildSecurityErrorXMLFile("delete report");
                            strStylesheet = SECURITY;
                            strPreviousXML = strXML;  
                            strPreviousStylesheet = strStylesheet;
                            return;
                        }

                        doDeleteReport();
                        
                        
                    }
                    // Cancel request, revert to displaying the report view form
                    else if ((runtimeData.getParameter("cancel")) != null)
                    {                   
                        displayReportViewForm();
                    }
                    // display the report details to be edited
                    else 
                    {
                        // if the user doesn't have permission to update report
                        // display unauthorization message
                        if (!authToken.hasActivity(REPORT_UPDATE))
                        {
                            strXML  = buildSecurityErrorXMLFile("update report");
                            strStylesheet = SECURITY;
                            strPreviousXML = strXML;  
                            strPreviousStylesheet = strStylesheet;
                            return;
                        }
                        
                        displayReportEditForm();                                                
                    }
                }    
                else if (strCurrent.equals(REPORT_VIEW))
                {

                    //if the user requests an edit of the report details
                    if (runtimeData.getParameter("edit") != null)
                    {
                        blFirstTimeEdit = true;
                        // Check if we are allowed to update
                        if ((lockRequest.isValid()) && (lockRequest.lockWrites()))
                        {    
                            lockRequest.unlockWrites();
                            // if the user doesn't have permission to update report
                            // display unauthorization message
                            if (!authToken.hasActivity(REPORT_UPDATE))
                            {
                                strXML  = buildSecurityErrorXMLFile("update report");
                                strStylesheet = SECURITY;
                                strPreviousXML = strXML;  
                                strPreviousStylesheet = strStylesheet;
                                return;
                            }
                            else
                            {
                                displayReportEditForm();
                            }
                            
                            
                        }
                        else
                        {
                            strErrorMessage = "Edit not allowed since the record is being viewed by other users.";
                            displayReportViewForm();
                        }
                    }    
                    // user wants to run the report
                    else if (runtimeData.getParameter("run") != null)
                    {
                        // if the user doesn't have permission to view report
                        // display unauthorization message
                        if (!authToken.hasActivity(REPORT_VIEW))
                        {
                            strXML  = buildSecurityErrorXMLFile("view report");
                            strStylesheet = SECURITY;
                            strPreviousXML = strXML;  
                            strPreviousStylesheet = strStylesheet;
                            return;
                        }
                        
                        doRunReport();
                    }
                    // user wants to view the report details
                    else
                    {
                        // if the user doesn't have permission to view report
                        // display unauthorization message
                        if (!authToken.hasActivity(REPORT_VIEW))
                        {
                            strXML  = buildSecurityErrorXMLFile("view report");
                            strStylesheet = SECURITY;
                            strPreviousXML = strXML;  
                            strPreviousStylesheet = strStylesheet;
                            return;
                        }
                        
                        displayReportViewForm();
                    }
                }
                
                strXML += "<strSessionID>" + authToken.getSessionUniqueID() + "</strSessionID>";
                strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root>" + strTreeXML + strFormXML + "</root>";
                //strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><report>" + strXML + "</report>";
                strPreviousXML = strXML;
                strPreviousStylesheet = strStylesheet;
                strPreviousFormXML = strFormXML;
                // Set the data to be loaded on click of the back button.
                backRuntimeData = runtimeData;
                strBackXML = strXML;
                strBackFormXML = strFormXML;
                strBackTreeXML = strTreeXML;
                strBackStylesheet = strStylesheet;                                
            }
            else
            {
                strStylesheet = strPreviousStylesheet;
                
                // user click on the collapse or expand buttons
                if (runtimeData.getParameter("expanded") != null)
                {
                    strFormXML = strPreviousFormXML;
                    strTreeXML = displayReportListForm();
                    strXML += "<strSessionID>" + authToken.getSessionUniqueID() + "</strSessionID>";
                    strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><root>" + strTreeXML + strFormXML + "</root>";
                    strPreviousXML = strXML;
                }
                else
                {
                    displayReportListForm();
                    strXML = strPreviousXML;    
                }
            }
        
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
        }
    }
    
  
    
    /**
     *  Generates the actual reports
     *  Reads in parameters from the view form, modifies appropriately for dates, and then
     *  updates the OLAP schema, modifying hardcoded parameters for those entered by the user
     *
     */
    private void doRunReport()
    {
        strStylesheet = REPORT_RESULT;
        Vector vtViewReportFormFields = DatabaseSchema.getFormFields("view_report");
        Vector vtParameters = DatabaseSchema.getFormFields("view_report_parameter");
        Vector vtYTDDates = new Vector();
        Calendar highestDate = null;


        
        //mondrian.rolap.CachePool.instance().flush();
        try
        {
            DALSecurityQuery query = null;

            // First check that all the required parameter values for this have been entered
            query = new DALSecurityQuery(REPORT_VIEW, authToken);
            query.setDomain("REPORTPARAMETERS", null, null, null);
            query.setFields(vtParameters, null);
            query.setWhere(null, 0, "REPORTPARAMETERS_intReportKey", "=", runtimeData.getParameter("REPORT_intReportKey"), 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "REPORTPARAMETERS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsCheckParameters = query.executeSelect();
            
            while (rsCheckParameters.next())
            {            
               String schemaName = rsCheckParameters.getString("REPORTPARAMETERS_strSchemaName");
               String dataType = rsCheckParameters.getString("REPORTPARAMETERS_strType");
               if (!dataType.equals("Date"))
               {
                   // If no value has been entered by the user for a string or numeric parameter
                   // invoke an error message
                   if (runtimeData.getParameter("REPORTPARAMETER_" + schemaName).equals(""))
                   {
                       strErrorMessage = "Please enter values for the parameter fields.";
                       break;
                   }    
               }
            }
            
            // If there is no error message we can run the report, that
            // is all the required parameter values have been entered
            if (strErrorMessage.equals(""))
            {
                
                query = new DALSecurityQuery(REPORT_VIEW, authToken);
                query.setDomain("REPORT", null, null, null);
                query.setField("REPORT_strOLAPSchemaFile", null);
                query.setWhere(null, 0, "REPORT_intReportKey", "=", runtimeData.getParameter("REPORT_intReportKey"), 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsReportSchema = query.executeSelect();
                rsReportSchema.next();
                String strOLAPSchema = rsReportSchema.getString("REPORT_strOLAPSchemaFile");           

                // get the magical OLAP schema 
                String strOLAPXML = "";

                // The default schema is OLAPSchema.xml 
                // If a different one is specified in edit or add report, the newly specified schema 
                // will be used
                BufferedReader inFile = new BufferedReader(new FileReader(OLAPSCHEMA_DIRECTORY + "/" + strOLAPSchema));
                String strLine = inFile.readLine();
                while (strLine != null)
                {
                    strOLAPXML += strLine; 
                    strLine = inFile.readLine();
                }
                inFile.close();


                // get all the parameters for this particular report
                query = new DALSecurityQuery(REPORT_VIEW, authToken);
                query.setDomain("REPORTPARAMETERS", null, null, null);
                query.setFields(vtParameters, null);
                query.setWhere(null, 0, "REPORTPARAMETERS_intReportKey", "=", runtimeData.getParameter("REPORT_intReportKey"), 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "REPORTPARAMETERS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsParameters = query.executeSelect();

                // get the parameters that are not required by this report
                DALQuery subQuery = new DALQuery();
                subQuery.setDomain("REPORTPARAMETERS", null, null, null);
                subQuery.setField("REPORTPARAMETERS_strSchemaName", null);
                subQuery.setWhere(null, 0, "REPORTPARAMETERS_intReportKey", "=", runtimeData.getParameter("REPORT_intReportKey"), 0, DALQuery.WHERE_HAS_VALUE);
                subQuery.executeSelect();

                // use the above to work out which parameters will need to be changed
                // to prevent an exception being thrown.  Filters out
                // any parameters with the same name as the ones required by this report

                query = new DALSecurityQuery(REPORT_VIEW, authToken);
                query.setDomain("REPORTPARAMETERS", null, null, null);
                query.setFields(vtParameters, null);
                query.setWhere(null, 0, "REPORTPARAMETERS_intReportKey", "<>", runtimeData.getParameter("REPORT_intReportKey"), 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "REPORTPARAMETERS_strSchemaName", "NOT IN", subQuery, 0, DALQuery.WHERE_HAS_SUB_QUERY);

                ResultSet rsParamChecker = query.executeSelect();

                while (rsParameters.next())
                {
                   String schemaName = rsParameters.getString("REPORTPARAMETERS_strSchemaName");
                   String dataType = rsParameters.getString("REPORTPARAMETERS_strType");
                   String dataOperator = runtimeData.getParameter("REPORTPARAMETERS_strOperator");
                   String conditionValue = "";

                   if (rsParameters.getString("REPORTPARAMETERS_intReportKey").equals(runtimeData.getParameter("REPORT_intReportKey")))
                   {

                       /*
                        * NOTE : 
                        * The below has been coded in such a way that :
                        *
                        * Date style parameters will not effect the operators in the olap schema
                        * however numeric and string parameters will. 
                        *
                        */

                       if (dataType.equals("Date"))
                       {
                          // get the date in correct format to be used as condition to query the schema
                          conditionValue = runtimeData.getParameter("REPORTPARAMETERS_dtDate_Year_"+ schemaName) + "-" + runtimeData.getParameter("REPORTPARAMETERS_dtDate_Month_"+ schemaName) + "-" + runtimeData.getParameter("REPORTPARAMETERS_dtDate_Day_"+ schemaName);

                          Calendar newDate = new GregorianCalendar( Integer.parseInt(runtimeData.getParameter("REPORTPARAMETERS_dtDate_Year_"+ schemaName)), Integer.parseInt(runtimeData.getParameter("REPORTPARAMETERS_dtDate_Month_"+ schemaName)), Integer.parseInt(runtimeData.getParameter("REPORTPARAMETERS_dtDate_Day_"+ schemaName)));
                          if (highestDate != null)
                              if (highestDate.before(newDate))
                                  highestDate = newDate;
                       }
                       else
                       {                       
                           conditionValue = Utilities.cleanForSQLStatement(runtimeData.getParameter("REPORTPARAMETER_" + schemaName));
                           
                           if (conditionValue != null) 
                           {
                               conditionValue = conditionValue.toLowerCase();
                           }                          
                           if (dataOperator != null) {
                               conditionValue = dataOperator + " " + conditionValue;
                           }
                       }
                       strOLAPXML = strOLAPXML.replaceAll(schemaName, conditionValue);
                   }


                }

                while(rsParamChecker.next())
                {
                   String schemaName = rsParamChecker.getString("REPORTPARAMETERS_strSchemaName");
                   String dataType = rsParamChecker.getString("REPORTPARAMETERS_strType");
                   String dataOperator = rsParamChecker.getString("REPORTPARAMETERS_strOperator");
                   String conditionValue = "";

                   if (dataType.equals("Date"))
                   {
                      conditionValue = "2000-01-01";
                   }
                   else
                   {
                      conditionValue = "> 0";   // operator needed to prevent exception being thrown 
                   }


                   strOLAPXML = strOLAPXML.replaceAll(schemaName, conditionValue);

                }

                // calculate a YTD value based on the highest value found from the parameters

                if (highestDate != null)
                {
                    int year = highestDate.get(Calendar.YEAR);
                    int month = highestDate.get(Calendar.MONTH);

                    if (month <= 6)
                    {
                        year--;   
                    }

                    String finYear = year + "-" + "07-01";

                    strOLAPXML = strOLAPXML.replaceAll("parYTD", finYear);
                }
                else // no highest date set... most likely because there were no date based parameters
                {
                    strOLAPXML = strOLAPXML.replaceAll("parYTD", "2000-01-01");

                }

                // write out the schema to file
                java.util.Date today = new java.util.Date();
                String strCurrentFileName = today.toString().replaceAll(" ", "");
                strCurrentFileName = today.toString().replaceAll(":", "");
                //System.err.println(strCurrentFileName);
                String strSchemaFileName = OLAPSCHEMA_DIRECTORY + "/" + strCurrentFileName + ".xml";
                BufferedWriter queryFile = new BufferedWriter(new FileWriter(new File(strSchemaFileName)));
                queryFile.write(strOLAPXML);
                queryFile.close();

                query = new DALSecurityQuery(REPORT_VIEW, authToken);
                query.setDomain("REPORT", null, null, null);
                query.setFields(vtViewReportFormFields, null);
                query.setWhere(null, 0, "REPORT_intReportKey", "=", runtimeData.getParameter("REPORT_intReportKey"), 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsReportList = query.executeSelect();
                rsReportList.next();

                String strFileName = authToken.getSessionUniqueID() + "reportpdf.pdf";
                String strJSPFileName = OLAPSCHEMA_DIRECTORY + "/" + rsReportList.getString("REPORT_strMDXQueryFile") + ".jsp";
                //BufferedReader inFile = new BufferedReader(new FileReader(strJSPFileName));
                String strFileContent = new String(QUERY_HEADER);
                //String strLine = inFile.readLine();
                //while (strLine != null)
                //{
                //    strFileContent += strLine; 
                //    strLine = inFile.readLine();
                //}
                strFileContent = strFileContent.replaceAll("SchemaFileName", strCurrentFileName);
                strFileContent = strFileContent + rsReportList.getString("REPORT_strReportQuery") + QUERY_FOOTER_1 + rsReportList.getString("REPORT_strReportName") + QUERY_FOOTER_2;
                //inFile.close();
                BufferedWriter outFile = new BufferedWriter(new FileWriter(new File(strJSPFileName)));
                outFile.write(strFileContent);
                outFile.close();

                strFormXML = "<report><query>" + rsReportList.getString("REPORT_strMDXQueryFile") + "</query>" +
                             "<serverURL>" + SERVER_URL + "</serverURL>" +
                             "<REPORT_intReportKey>" + runtimeData.getParameter("REPORT_intReportKey") + 
                             "</REPORT_intReportKey><strFileName>" + strFileName + "</strFileName></report>";
                //System.err.println(strXML);
            }
            // If there is an error message, do not run the report
            // notify the user by displaying the error string in
            // the view report form
            else
            {
                displayReportViewForm();            
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
        }
    }
    
    /** display add new REPORT CATEGORY form
     */
    private void displayReportCategoryAddForm()
    {
        strStylesheet = REPORT_CATEGORY_ADD;
        Vector vtAddReportCategoryFormFields = DatabaseSchema.getFormFields("add_report_category");
        strFormXML = "<report><strErrorMessage>" + strErrorMessage + "</strErrorMessage>" + QueryChannel.buildFormLabelXMLFile(vtAddReportCategoryFormFields) + "</report>";
        strTreeXML = displayReportListForm();
    }
    
    /** Add new REPORT CATEGORY
     */
    private void doAddReportCategory()
    {
        Vector vtAddReportCategoryFormFields = DatabaseSchema.getFormFields("add_report_category");
        
        // checks data for required fields
        String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddReportCategoryFormFields, runtimeData);
        if (strCheckRequiredFields == null)
        {
        
            try
            {
                
                // add new report category to database
                DALSecurityQuery query = new DALSecurityQuery(REPORT_ADD, authToken);
                query.setDomain("REPORTCATEGORY", null, null, null);
                query.setFields(vtAddReportCategoryFormFields, runtimeData);
                
                query.executeInsert();

                
                
                runtimeData.setParameter("REPORTCATEGORY_intReportCategoryKey", QueryChannel.getNewestKeyAsString(query));

                // display report category details
                displayReportCategoryViewForm();
            }
            catch (Exception e)
            {
                LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
            }
        }
        else
        {
            // display error message
            strErrorMessage = strCheckRequiredFields;
            displayReportCategoryAddForm();
        }
    }
    
    /** Update REPORT CATEGORY
     */
    private void doUpdateReportCategory()
    {
        Vector vtUpdateReportCategoryFormFields = DatabaseSchema.getFormFields("update_report_category");
        
        // checks data for required fields
        String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtUpdateReportCategoryFormFields, runtimeData);
        if (strCheckRequiredFields == null)
        {
        
            try
            {
                
                if (lockRequest.isValid() && lockRequest.lockWrites())
                {
                    // update REPORT CATEGORY
                    DALSecurityQuery query = new DALSecurityQuery(REPORT_UPDATE, authToken);
                    query.setDomain("REPORTCATEGORY", null, null, null);
                    query.setFields(vtUpdateReportCategoryFormFields, runtimeData);
                    query.setWhere(null, 0, "REPORTCATEGORY_intReportCategoryKey", "=", runtimeData.getParameter("REPORTCATEGORY_intReportCategoryKey"), 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();
                    
                    // unlock the record
                    if (lockRequest.isValid())
                        lockRequest.unlockWrites();
                }
                else
                {
                    strErrorMessage = "Update failed since the record is being viewed by other users.";
                }

                // display REPORT CATEGORY details
                displayReportCategoryViewForm();
            }
            catch (Exception e)
            {
                LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
            }
        }
        else
        {
            // display error message
            strErrorMessage = strCheckRequiredFields;
            displayReportCategoryViewForm();
        }
    }
    
    /** Delete REPORT CATEGORY
     */
    private void doDeleteReportCategory()
    {
        try
        {
            // if we can lock the record, delete it
            if (lockRequest.isValid() && lockRequest.lockWrites())
            {
                String strCurrentReportCategoryID = runtimeData.getParameter("REPORTCATEGORY_intReportCategoryKey");
                DALSecurityQuery query = new DALSecurityQuery(REPORT_DELETE, authToken);
                query.setDomain("REPORT", null, null, null);
                query.setField("REPORT_intReportKey", null);                
                query.setWhere(null, 0, "REPORT_intParentKey", "=", strCurrentReportCategoryID, 0, DALQuery.WHERE_HAS_VALUE);
                query.setWhere("AND", 0, "REPORT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsResult = query.executeSelect();
                
                // there are some report in this category, so can not delete it
                if (rsResult.next())
                {
                    strErrorMessage = "Delete failed since the category is not empty.";
                    displayReportCategoryViewForm();
                }
                else
                {
                    query.reset();
                    query.setDomain("REPORTCATEGORY", null, null, null);
                    query.setField("REPORTCATEGORY_intDeleted", "-1");
                    query.setWhere(null, 0, "REPORTCATEGORY_intReportCategoryKey", "=", strCurrentReportCategoryID, 0, DALQuery.WHERE_HAS_VALUE);
                    query.executeUpdate();
                }
                
                // unlock write
                if (lockRequest.isValid())
                    lockRequest.unlockWrites();
                
                displayReportCategoryAddForm();
            }
            else
            {
                strErrorMessage = "Delete failed since the record is being viewed by other users.";
                displayReportCategoryViewForm();
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
        }
    }
    
    /** Display report category details
     */
    private void displayReportCategoryViewForm()
    {
        strStylesheet = REPORT_CATEGORY_VIEW;
        Vector vtViewReportCategoryFormFields = DatabaseSchema.getFormFields("view_report_category");
        int intReportCategoryKey = Integer.parseInt(runtimeData.getParameter("REPORTCATEGORY_intReportCategoryKey"));
        strCurrentLocalNode = "REPORTCATEGORY_" + intReportCategoryKey;
        
        try
        {
            strFormXML = "<report><strErrorMessage>" + strErrorMessage + "</strErrorMessage>" + QueryChannel.buildFormLabelXMLFile(vtViewReportCategoryFormFields) + QueryChannel.buildViewFromKeyXMLFile(vtViewReportCategoryFormFields, "REPORTCATEGORY_intReportCategoryKey", intReportCategoryKey) + "</report>";
            strTreeXML = displayReportListForm();            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
        }
    }
    
    /** display add new REPORT form
     */
    private void displayReportAddForm()
    {
        strStylesheet = REPORT_ADD;
        Vector vtAddReportFormFields = DatabaseSchema.getFormFields("add_report");
        Vector vtListReportCategoryFormFields = DatabaseSchema.getFormFields("view_report_category");
        Vector vtAddReportParameterFormFields = DatabaseSchema.getFormFields("add_report_parameter");
        
        
        try
        {
            // build Category dropdown list
            DALQuery query = new DALQuery();
            query.setDomain("REPORTCATEGORY", null, null, null);
            query.setFields(vtListReportCategoryFormFields, null);
            query.setWhere(null, 0, "REPORTCATEGORY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("REPORTCATEGORY_intReportCategoryKey", "ASC");
            ResultSet rsReportCategoryList = query.executeSelect();
            
            
            strFormXML = "<report><strErrorMessage>" + strErrorMessage + "</strErrorMessage>" + 
                         QueryChannel.buildSearchXMLFile("search_report_category", rsReportCategoryList, vtListReportCategoryFormFields) + 
                         QueryChannel.buildFormLabelXMLFile(vtAddReportFormFields) +
                         QueryChannel.buildFormLabelXMLFile(vtAddReportParameterFormFields) +
                         
                         QueryChannel.buildAddFormXMLFile(vtAddReportFormFields) +
                         QueryChannel.buildAddFormXMLFile(vtAddReportParameterFormFields) + 
                         "</report>";
            
            strTreeXML = displayReportListForm();
            
            LogService.instance().log(LogService.ERROR, strFormXML);
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
        }
    }
    
    /** display view new REPORT form
     */
    private void displayReportEditForm()
    {
        // Set the formfields to be used in querying the database
        Vector vtViewReportFormFields = DatabaseSchema.getFormFields("add_report");
        Vector vtListReportCategoryFormFields = DatabaseSchema.getFormFields("view_report_category");
        Vector vtViewReportParameterFormFields = DatabaseSchema.getFormFields("view_report_parameter");
        
        String fieldValues[] = runtimeData.getParameterValues("REPORT_userParameters");

        // Set the stylesheet to the report edit form
        strStylesheet = REPORT_EDIT;
        StringTokenizer tokenizer = null;       
        String strReportXML = "";
        String strReportParametersXML = "";        
        
        // store the ReportKey as an integer into the variable intReportKey
        int intReportKey = Integer.parseInt(runtimeData.getParameter("REPORT_intReportKey"));
        intEditedReportKey = intReportKey;
        boolean blParamHandled = false;
        String strAllowOperator;
        if ( runtimeData.getParameter("REPORTPARAMETERS_strAllowOperator") == null )
        { 
            strAllowOperator = "N";
        }
        else
        {
            strAllowOperator = "Y";
        }    

        
        
        try
        {
            // build Category dropdown list
            DALQuery categoryQuery = new DALQuery();
            categoryQuery.setDomain("REPORTCATEGORY", null, null, null);
            categoryQuery.setFields(vtListReportCategoryFormFields, null);
            categoryQuery.setWhere(null, 0, "REPORTCATEGORY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            categoryQuery.setOrderBy("REPORTCATEGORY_intReportCategoryKey", "ASC");
            ResultSet rsReportCategoryList = categoryQuery.executeSelect();                        
            
            // Build the report details
            // if coming to this page for the first time, build the fields
            // based on what is in the database
            // otherwise we are coming to the page on a submit from the edit form.
            // This occurs when the user changes focus in the add parameters
            // select box, presses the Add/Edit Parameter button or browses to a different OLAP schema.
            // Display the latest changes to the fields that have not yet been saved.
            if (blFirstTimeEdit == true)
            {    
                blFirstTimeEdit = false;
                DALQuery parametersQuery = new DALQuery();
                parametersQuery.setDomain("REPORTPARAMETERS", null, null, null);
                parametersQuery.setFields(vtViewReportParameterFormFields, null);            
                parametersQuery.setWhere(null, 0, "REPORTPARAMETERS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                parametersQuery.setWhere("AND", 0, "REPORTPARAMETERS_intReportKey", "=", runtimeData.getParameter("REPORT_intReportKey"), 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsReportParametersList = parametersQuery.executeSelect();


                while (rsReportParametersList.next())
                {
                    strReportParametersXML += "<REPORTPARAMETERS_list>";
                    for (int i=0; i < vtViewReportParameterFormFields.size(); i++) 
                    {

                        String strFieldName = (String) vtViewReportParameterFormFields.get(i);
                        String strFieldValue = rsReportParametersList.getString(strFieldName);
                        // clean the parameter operator characters for XSL
                        if (strFieldName.equals("REPORTPARAMETERS_strOperator"))
                        {
                            strFieldValue = Utilities.cleanForXSL (strFieldValue);
                        }    
                        strReportParametersXML += "<View" + strFieldName + ">" + strFieldValue + "</View" + strFieldName + ">";

                    }
                    strReportParametersXML += "</REPORTPARAMETERS_list>";
                }
                
                strReportXML = QueryChannel.buildViewFromKeyXMLFile(vtViewReportFormFields, "REPORT_intReportKey", intReportKey);
                
            }    
            else 
            {
                               
                if (fieldValues != null)
                {    
                    strReportParametersXML = "";

                    for (int i = 0; i < fieldValues.length; i++)
                    {
                       strReportParametersXML += "<REPORTPARAMETERS_list>";
                       tokenizer = new StringTokenizer(fieldValues[i], ":");
                       String strReportParameterName = tokenizer.nextToken().trim();

                       if (runtimeData.getParameter("strParameterEditing").equals("add"))
                       {
                           // Edit of an existing parameter, build the parameters added select box
                           // from the relevant fields
                           if ( (runtimeData.getParameter("REPORTPARAMETERS_strName").equals(strReportParameterName)) )
                           {                          
                              blParamHandled = true;
                              strReportParametersXML += 
                                "<ViewREPORTPARAMETERS_strName>" + strReportParameterName + "</ViewREPORTPARAMETERS_strName>"
                              + "<ViewREPORTPARAMETERS_strSchemaName>" + runtimeData.getParameter("REPORTPARAMETERS_strSchemaName") + "</ViewREPORTPARAMETERS_strSchemaName>"
                              + "<ViewREPORTPARAMETERS_strType>" + runtimeData.getParameter("REPORTPARAMETERS_strType")  + "</ViewREPORTPARAMETERS_strType>"
                              + "<ViewREPORTPARAMETERS_strAllowOperator>" + strAllowOperator + "</ViewREPORTPARAMETERS_strAllowOperator>";
                           }                            
                           // Addition of a new parameter - add the new parameter to the select box 
                           // or just a resubmit on selection of the report parameters select box - leave the select box as is - rebuild the same parameter values
                           else 
                           {                              
                               // rebuild the existing parameters
                               strReportParametersXML += "<ViewREPORTPARAMETERS_strName>" + strReportParameterName + "</ViewREPORTPARAMETERS_strName>"
                               + "<ViewREPORTPARAMETERS_strSchemaName>" + tokenizer.nextToken().trim() + "</ViewREPORTPARAMETERS_strSchemaName>"
                               + "<ViewREPORTPARAMETERS_strType>" + tokenizer.nextToken().trim() + "</ViewREPORTPARAMETERS_strType>"
                               + "<ViewREPORTPARAMETERS_strAllowOperator>" + tokenizer.nextToken().trim() + "</ViewREPORTPARAMETERS_strAllowOperator>";
                           }  
                           
                       }
                       else 
                       {
                           // On submit build all the current parameters
                           strReportParametersXML += "<ViewREPORTPARAMETERS_strName>" + strReportParameterName + "</ViewREPORTPARAMETERS_strName>"
                           + "<ViewREPORTPARAMETERS_strSchemaName>" + tokenizer.nextToken().trim() + "</ViewREPORTPARAMETERS_strSchemaName>"
                           + "<ViewREPORTPARAMETERS_strType>" + tokenizer.nextToken().trim() + "</ViewREPORTPARAMETERS_strType>"
                           + "<ViewREPORTPARAMETERS_strAllowOperator>" + tokenizer.nextToken().trim() + "</ViewREPORTPARAMETERS_strAllowOperator>";                       
                       }    
                       strReportParametersXML += "</REPORTPARAMETERS_list>";               
                    }

                }

                // Add any new parameters not already in the report parameters list
                if ((blParamHandled == false) && (runtimeData.getParameter("strParameterEditing").equals("add")))
                {
                    strReportParametersXML += "<REPORTPARAMETERS_list>" +
                    "<ViewREPORTPARAMETERS_strName>" + runtimeData.getParameter("REPORTPARAMETERS_strName") + "</ViewREPORTPARAMETERS_strName>"
                    + "<ViewREPORTPARAMETERS_strSchemaName>" + runtimeData.getParameter("REPORTPARAMETERS_strSchemaName") + "</ViewREPORTPARAMETERS_strSchemaName>"
                    + "<ViewREPORTPARAMETERS_strType>" + runtimeData.getParameter("REPORTPARAMETERS_strType")  + "</ViewREPORTPARAMETERS_strType>"
                    + "<ViewREPORTPARAMETERS_strAllowOperator>" + strAllowOperator + "</ViewREPORTPARAMETERS_strAllowOperator>" + "</REPORTPARAMETERS_list>";
                }    
                
                // Get the location of the OLAP schema to use.
                // if null the default or previously selected OLAP schema will be used in doRunReport                                          
                if (runtimeData.getObjectParameter("REPORTOLAPSCHEMA_strFileName") != null) 
                {
                    String strOLAPSchemaFile = QueryChannel.uploadFile( "REPORTOLAPSCHEMA_strFileName",
                          runtimeData, OLAPSCHEMA_DIRECTORY, null);
                    runtimeData.setParameter("REPORT_strOLAPSchemaFile", strOLAPSchemaFile);
                }  

                strReportXML = "<REPORT_strReportName>" + runtimeData.getParameter("REPORT_strReportName") + "</REPORT_strReportName>" +
                                "<REPORT_intParentKey>" + runtimeData.getParameter("REPORT_intParentKey") + "</REPORT_intParentKey>" +
                                "<REPORT_strReportQuery>" + runtimeData.getParameter("REPORT_strReportQuery") + "</REPORT_strReportQuery>" +
                                "<REPORT_strReportDesc>" + runtimeData.getParameter("REPORT_strReportDesc") + "</REPORT_strReportDesc>" +
                                "<REPORT_strOLAPSchemaFile>" + runtimeData.getParameter("REPORT_strOLAPSchemaFile") + "</REPORT_strOLAPSchemaFile>" ;

            
            }    
            

                
            // Get the report parameter name, schema and type of the report parameter
            // that currently has focus in the "REPORT_userParameters" select box
            // This is used to populate the report parameter name, schema and type input fields
            // for easier editing
            String strSelParams = runtimeData.getParameter("REPORT_selectedParameter");
            String strSelParamXML = "";
            String strSelParamTypeXML = "";

            if ((strSelParams != null) && !(strSelParams.equals("")))
            {    
                tokenizer = new StringTokenizer(strSelParams, ":"); 
                // Build the XML to display the values of the parameter that has focus
                strSelParamXML += "<select_param_name>" + tokenizer.nextToken().trim() + "</select_param_name>" +
                                  "<select_param_schema>" + tokenizer.nextToken().trim() + "</select_param_schema>" +
                                  "<select_param_type>" + tokenizer.nextToken().trim() + "</select_param_type>" +
                                  "<select_allow_operator>" + tokenizer.nextToken().trim() + "</select_allow_operator>";        
            }
            
            
            // Build the form XML
            strFormXML = "<report><strErrorMessage>" + strErrorMessage + "</strErrorMessage>" + 
                         QueryChannel.buildSearchXMLFile("search_report_category", rsReportCategoryList, vtListReportCategoryFormFields) + 
                         QueryChannel.buildFormLabelXMLFile(vtViewReportFormFields) +
                         QueryChannel.buildFormLabelXMLFile(vtViewReportParameterFormFields) +
                         
                         QueryChannel.buildAddFormXMLFile(vtViewReportParameterFormFields) +
                         strReportXML +
                         strReportParametersXML + strSelParamXML + 
                         "</report>";            
                        
            strTreeXML = displayReportListForm();
            LogService.instance().log(LogService.ERROR, strFormXML);
                   
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
        }
    }
        
    
    
    /** Add new REPORT
     */
    private void doAddReport()
    {
        Vector vtAddReportFormFields = DatabaseSchema.getFormFields("add_report");
        Vector vtAddReportParamsFields = DatabaseSchema.getFormFields("add_report_parameter");
        StringTokenizer tokenizer = null;
        String strOLAPSchemaFile = "";
        
        // checks data for required fields
        String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtAddReportFormFields, runtimeData);
               
        if (strCheckRequiredFields == null)
        {
        
            try
            {                
                // Get the location of the OLAP schema to use.
                // if null the default or previously selected OLAP schema will be used in doRunReport                                          
                if (runtimeData.getObjectParameter("REPORTOLAPSCHEMA_strFileName") != null) 
                {
                    strOLAPSchemaFile = QueryChannel.uploadFile( "REPORTOLAPSCHEMA_strFileName",
                          runtimeData, OLAPSCHEMA_DIRECTORY, null);
                    runtimeData.setParameter("REPORT_strOLAPSchemaFile", strOLAPSchemaFile);
                }  

                
                // create query file
                String strFileName = OLAPSCHEMA_DIRECTORY + "/" + runtimeData.getParameter("REPORT_strReportName") + authToken.getSessionUniqueID() + ".jsp";
                BufferedWriter queryFile = new BufferedWriter(new FileWriter(new File(strFileName)));
                String strQuery = QUERY_HEADER + runtimeData.getParameter("REPORT_strReportQuery") + QUERY_FOOTER_1 + runtimeData.getParameter("REPORT_strReportName") + QUERY_FOOTER_2;
                queryFile.write(strQuery);
                queryFile.close();

                // add new report to database
                runtimeData.setParameter("REPORT_strMDXQueryFile", runtimeData.getParameter("REPORT_strReportName") + authToken.getSessionUniqueID());
                DALSecurityQuery query = new DALSecurityQuery(REPORT_ADD, authToken);
                query.setDomain("REPORT", null, null, null);
                query.setFields(vtAddReportFormFields, runtimeData);
                query.executeInsert();

                String fieldValues[] = runtimeData.getParameterValues("REPORT_userParameters");
                String strReportKey = QueryChannel.getNewestKeyAsString(query);
                
                // Insert each report parameter in the "REPORT_userParameters" select box into the database
                if (fieldValues != null)
                {
                    for (int i = 0; i < fieldValues.length; i++)
                    {
                       tokenizer = new StringTokenizer(fieldValues[i], ":");
                       query = new DALSecurityQuery(REPORT_ADD, authToken);
                       query.setDomain("REPORTPARAMETERS", null, null, null);
                       query.setField("REPORTPARAMETERS_intReportKey", strReportKey);

                       // tokenizer the data from the parameters list on the view
                       query.setField("REPORTPARAMETERS_strName", tokenizer.nextToken().trim());
                       query.setField("REPORTPARAMETERS_strSchemaName", tokenizer.nextToken().trim());
                       query.setField("REPORTPARAMETERS_strType", tokenizer.nextToken().trim());
                       query.setField("REPORTPARAMETERS_strAllowOperator", tokenizer.nextToken().trim());
                       query.executeInsert();

                    }
                }
                    
                runtimeData.setParameter("REPORT_intReportKey", strReportKey);

                // display report details
                displayReportViewForm();
            }
            catch (Exception e)
            {
                LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
            }
        }
        else
        {
            // display error message
            strErrorMessage = strCheckRequiredFields;
            displayReportAddForm();
        }
    }
    
    /** Update REPORT
     */
    private void doUpdateReport()
    {
        Vector vtUpdateReportFormFields = DatabaseSchema.getFormFields("update_report");
        String strOLAPSchemaFile = "";
        
        // checks data for required fields
        String strCheckRequiredFields = QueryChannel.checkRequiredFields(vtUpdateReportFormFields, runtimeData);
        if (strCheckRequiredFields == null)
        {
        
            try
            {
                if (lockRequest.isValid() && lockRequest.lockWrites())
                {                                                           

                    // Get the location of the OLAP schema to use.
                    // if null the default or previously selected OLAP schema will be used in doRunReport                                          
                    if (runtimeData.getObjectParameter("REPORTOLAPSCHEMA_strFileName") != null) 
                    {
                        strOLAPSchemaFile = QueryChannel.uploadFile( "REPORTOLAPSCHEMA_strFileName",
                              runtimeData, OLAPSCHEMA_DIRECTORY, null);
                        runtimeData.setParameter("REPORT_strOLAPSchemaFile", strOLAPSchemaFile);
                    }  
                    
                    
                    DALSecurityQuery query = new DALSecurityQuery(REPORT_UPDATE, authToken);
                    query.setDomain("REPORT", null, null, null);
                    query.setFields(vtUpdateReportFormFields, null);
                    query.setWhere(null, 0, "REPORT_intReportKey", "=", runtimeData.getParameter("REPORT_intReportKey"), 0, DALQuery.WHERE_HAS_VALUE);
                    ResultSet rsResult = query.executeSelect();
                    rsResult.next();
                    String strOldFileName = OLAPSCHEMA_DIRECTORY + "/" + rsResult.getString("REPORT_strMDXQueryFile") + ".jsp";
                    File flOldQueryFile = new File(strOldFileName);
                    flOldQueryFile.delete();


                    // create query file
                    String strFileName = OLAPSCHEMA_DIRECTORY + "/" + runtimeData.getParameter("REPORT_strReportName") + authToken.getSessionUniqueID() + ".jsp";
                    BufferedWriter queryFile = new BufferedWriter(new FileWriter(new File(strFileName)));
                    String strQuery = QUERY_HEADER + runtimeData.getParameter("REPORT_strReportQuery") + QUERY_FOOTER_1 + runtimeData.getParameter("REPORT_strReportName") + QUERY_FOOTER_2;
                    queryFile.write(strQuery);
                    queryFile.close();

                    // add new report to database
                    runtimeData.setParameter("REPORT_strMDXQueryFile", runtimeData.getParameter("REPORT_strReportName") + authToken.getSessionUniqueID());
                    query.setFields(vtUpdateReportFormFields, runtimeData);
                    query.executeUpdate();
                    
                    // Add or edit the newly edited report parameters to the database if necessary
                    doUpdateReportParameters();

                    // Delete the parameters that have been deleted from the database
                    doRemoveReportParameters();
                    
                    if (lockRequest.isValid())
                        lockRequest.unlockWrites();
                }
                else
                {
                    strErrorMessage = "Update failed since the record is being viewed by other users.";
                }

                // display report details
                displayReportViewForm();                                                
                
            }
            catch (Exception e)
            {
                LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
            }
        }
        else
        {
            // display error message
            strErrorMessage = strCheckRequiredFields;
            displayReportViewForm();
        }
    }

    /** Update REPORT Parameters
     */
    private void doUpdateReportParameters()
    {
        Vector vtViewReportParameterFormFields = DatabaseSchema.getFormFields("view_report_parameter"); 
        String fieldValues[] = runtimeData.getParameterValues("REPORT_userParameters");
        
        StringTokenizer tokenizer = null;
        String strReportParameterName;
        int intReportParameterKey = 0;
        boolean record_exists = false;
        
        try
        {
            if (lockRequest.isValid() && lockRequest.lockWrites())
            {                                                           
                //query the report parameters table
                DALSecurityQuery parametersQuery = new DALSecurityQuery(REPORT_ADD, authToken);
                parametersQuery.setDomain("REPORTPARAMETERS", null, null, null);
                parametersQuery.setFields(vtViewReportParameterFormFields, null);
                parametersQuery.setWhere(null, 0, "REPORTPARAMETERS_intReportKey", "=", runtimeData.getParameter("REPORT_intReportKey"), 0, DALQuery.WHERE_HAS_VALUE);
                parametersQuery.setWhere("AND", 0, "REPORTPARAMETERS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsReportParametersList;


                // check if a report parameter with the same name already exists for the current report
                // if it does exist execute an update otherwise an insert of a new report parameter
                if (fieldValues != null)
                {    
                    for (int i = 0; i < fieldValues.length; i++)
                    {
                        tokenizer = new StringTokenizer(fieldValues[i], ":");
                        // initialise the DAL query for updating
                        DALSecurityQuery query = new DALSecurityQuery(REPORT_ADD, authToken);
                        query.setDomain("REPORTPARAMETERS", null, null, null);
                        query.setField("REPORTPARAMETERS_intReportKey", runtimeData.getParameter("REPORT_intReportKey"));

                        strReportParameterName = tokenizer.nextToken().trim();

                        // query the report parameters table
                        rsReportParametersList = parametersQuery.executeSelect();

                        record_exists = false;                         
                        while ((rsReportParametersList.next()) && (record_exists == false))
                        {                
                            if ((rsReportParametersList.getString("REPORTPARAMETERS_strName")).equals(strReportParameterName))
                            {
                              record_exists = true;
                              intReportParameterKey = Integer.parseInt(rsReportParametersList.getString("REPORTPARAMETERS_intReportParameterKey"));
                            }    
                        }    

                        // set the values of the fields to be updated or inserted
                        query.setField("REPORTPARAMETERS_strName", strReportParameterName);
                        query.setField("REPORTPARAMETERS_strSchemaName", tokenizer.nextToken().trim());
                        query.setField("REPORTPARAMETERS_strType", tokenizer.nextToken().trim());
                        query.setField("REPORTPARAMETERS_strAllowOperator", tokenizer.nextToken().trim());


                        // Parameter with the same name exists in the report so execute an update with the edited data
                        if (record_exists == true)
                        {
                            query.setWhere(null, 0, "REPORTPARAMETERS_intReportParameterKey", "=", (Integer.toString(intReportParameterKey)), 0, DALQuery.WHERE_HAS_VALUE);
                            query.executeUpdate();                            
                        }    
                        // Parameter with the same name does not exist for the report so execute an insert of the new parameter
                        else 
                        {                             
                            query.executeInsert(); 
                        }
                    }
                }    
                
                if (lockRequest.isValid())
                    lockRequest.unlockWrites();
            }
            else
            {
                strErrorMessage = "Update failed since the record is being viewed by other users.";
            }
                  
            displayReportEditForm();
        } 
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
        }
    }
    
    /** Remove REPORT Parameters
     */
    private void doRemoveReportParameters()
    {
        Vector vtViewReportParameterFormFields = DatabaseSchema.getFormFields("view_report_parameter"); 
        
        String fieldValues[] = runtimeData.getParameterValues("REPORT_userParameters");
        
        StringTokenizer tokenizer = null;
        String strReportParameterName;
        boolean record_exists = false;
        int intReportParameterKey = 0;
        
        try
        {
            if (lockRequest.isValid() && lockRequest.lockWrites())
            {                                                           

                //check if we have any records to delete from the report parameters table
                // query the report parameters table                
                DALSecurityQuery parametersQuery = new DALSecurityQuery(REPORT_ADD, authToken);
                parametersQuery.setDomain("REPORTPARAMETERS", null, null, null);
                parametersQuery.setFields(vtViewReportParameterFormFields, null);
                parametersQuery.setWhere(null, 0, "REPORTPARAMETERS_intReportKey", "=", runtimeData.getParameter("REPORT_intReportKey"), 0, DALQuery.WHERE_HAS_VALUE);
                parametersQuery.setWhere("AND", 0, "REPORTPARAMETERS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                ResultSet rsReportParametersList = parametersQuery.executeSelect();
                
                while (rsReportParametersList.next())
                {                                                                
                    record_exists = false;
                    if (fieldValues != null)
                    {    
                        for (int i = 0; i < fieldValues.length; i++)
                        {
                           tokenizer = new StringTokenizer(fieldValues[i], ":");
                           strReportParameterName = tokenizer.nextToken().trim();
                           if ((rsReportParametersList.getString("REPORTPARAMETERS_strName")).equals(strReportParameterName))
                           {
                             record_exists = true;
                           }                            
                        }
                    }

                    // if the report parameter that was in the original table has no been found in the edited report form
                    // it must be deleted from the report parameters table
                    if (record_exists == false)
                    {
                        intReportParameterKey = Integer.parseInt(rsReportParametersList.getString("REPORTPARAMETERS_intReportParameterKey"));
                        DALSecurityQuery query = new DALSecurityQuery(REPORT_ADD, authToken);
                        query.setDomain("REPORTPARAMETERS", null, null, null);
                        query.setField("REPORTPARAMETERS_intDeleted", "-1");
                        query.setWhere(null, 0, "REPORTPARAMETERS_intReportParameterKey", "=", (Integer.toString(intReportParameterKey)), 0, DALQuery.WHERE_HAS_VALUE);
                        query.executeUpdate();                         
                    }    
                }                                    
                
                if (lockRequest.isValid())
                    lockRequest.unlockWrites();
            }
            else
            {
                strErrorMessage = "Update failed since the record is being viewed by other users.";
            }
                  
            displayReportEditForm();
        } 
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
        }
    }

    /** Delete REPORT
     */
    private void doDeleteReport()
    {
        try
        {
            // if we can lock the record, delete it
            if (lockRequest.isValid() && lockRequest.lockWrites())
            {
                String strCurrentReportID = runtimeData.getParameter("REPORT_intReportKey");
                DALSecurityQuery query = new DALSecurityQuery(REPORT_DELETE, authToken);
                query.setDomain("REPORT", null, null, null);
                query.setField("REPORT_intDeleted", "-1");
                query.setWhere(null, 0, "REPORT_intReportKey", "=", strCurrentReportID, 0, DALQuery.WHERE_HAS_VALUE);
                query.executeUpdate();
                
                // unlock write
                if (lockRequest.isValid())
                    lockRequest.unlockWrites();
                
                displayReportCategoryAddForm();
            }
            else
            {
                strErrorMessage = "Delete failed since the record is being viewed by other users.";
                displayReportViewForm();
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
        }
    }
    
    /** Display report details
     */
    private void displayReportViewForm()
    {
        strStylesheet = REPORT_VIEW;
        Vector vtViewReportFormFields = DatabaseSchema.getFormFields("view_report");
        Vector vtListReportCategoryFormFields = DatabaseSchema.getFormFields("view_report_category");
        Vector vtParameters = DatabaseSchema.getFormFields("view_report_parameter");
        
        
        
        int intReportKey = Integer.parseInt(runtimeData.getParameter("REPORT_intReportKey"));
        strCurrentLocalNode = "REPORT_" + intReportKey;
        
        try
        {
            DALSecurityQuery query = new DALSecurityQuery(REPORT_VIEW, authToken);
            query.setDomain("REPORTCATEGORY", null, null, null);
            query.setFields(vtListReportCategoryFormFields, null);
            query.setWhere(null, 0, "REPORTCATEGORY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("REPORTCATEGORY_intReportCategoryKey", "ASC");
            ResultSet rsReportCategoryList = query.executeSelect();
            
            query = new DALSecurityQuery(REPORT_VIEW, authToken);
            query.setDomain("REPORTPARAMETERS", null, null, null);
            query.setFields(vtParameters, null);
            query.setWhere(null, 0, "REPORTPARAMETERS_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "REPORTPARAMETERS_intReportKey", "=", intReportKey + "", 0, DALQuery.WHERE_HAS_VALUE);
            ResultSet rsParameterList = query.executeSelect();

            String strDateXML = "";
            strDateXML = QueryChannel.buildDateDropDownXMLFile("REPORTPARAMETERS_dtDate", null);
          
            String strReportParametersXML = "";
            
            while (rsParameterList.next())
            {
                strReportParametersXML += "<search_report_parameters>";
                for (int i=0; i < vtParameters.size(); i++) 
                {
                    String strFieldName = (String) vtParameters.get(i);
                    String strFieldValue = rsParameterList.getString(strFieldName);
                    // clean the parameter operator characters for XSL
                    if (strFieldName.equals("REPORTPARAMETERS_strOperator"))
                    {
                        strFieldValue = Utilities.cleanForXSL (strFieldValue);
                    }    
                    strReportParametersXML += "<" + strFieldName + ">" + strFieldValue + "</" + strFieldName + ">";
                }
                strReportParametersXML += (strDateXML + "</search_report_parameters>");
            }
                      
            // Generate the XML to be processed by the ViewReport.xsl
            strFormXML = "<report><strErrorMessage>" + strErrorMessage + "</strErrorMessage>" + QueryChannel.buildSearchXMLFile("search_report_category", rsReportCategoryList, vtListReportCategoryFormFields) +
            strReportParametersXML +
            QueryChannel.buildFormLabelXMLFile(vtViewReportFormFields) +
            QueryChannel.buildViewFromKeyXMLFile(vtViewReportFormFields, "REPORT_intReportKey", intReportKey) + 
            "</report>";
            
            
            LogService.instance().log(LogService.ERROR, "THE XML! : " + strFormXML);
           
            strTreeXML = displayReportListForm();
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
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
        xslt.setXSL("CReport.ssl", strStylesheet, currentRuntimeData.getBrowserInfo());

        // set parameters that the stylesheet needs.
        xslt.setStylesheetParameter("baseActionURL", currentRuntimeData.getBaseActionURL());
        //xslt.setStylesheetParameter("strCurrent", strStylesheet);
        
        //Start Secure Download Implementation       
        
        org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(currentRuntimeData.getBaseActionURL( true ));

        upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "Download"));
        xslt.setStylesheetParameter("downloadURL", upfTmp.getUPFile());
        xslt.setStylesheetParameter("nodeId", SessionManager.getChannelID(strSessionUniqueID, "Download"));   

        //End Secure


        // set the output Handler for the output.
        xslt.setTarget(out);

        // do the deed
        xslt.transform();
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
                LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
        }
        finally
        {
            lockRequest = null;
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
                + " is not authorised to " + aFunctionalArea + "</errortext>"
                + "<errordata></errordata>"
                + "</securityerror>";
        return strTempErrorXML;      
    }

    private void updateHashExpanded(String key, boolean fromLink)
    {   
        if (!hashGlobalExpanded.containsKey(key))
            hashGlobalExpanded.put(key, "expanded");
        else if (!fromLink)
            hashGlobalExpanded.remove(key);
    }

    private String displayReportListForm()
    {
        String strHashExpandedKey = runtimeData.getParameter("hashExpandedKey");
        String strExpanded = runtimeData.getParameter("expanded");	
        boolean blExpanded = false;
        if(strExpanded != null)
        {
            blExpanded = new Boolean(strExpanded).booleanValue();	
        }

        if( strHashExpandedKey != null && strExpanded != null)
        {
            updateHashExpanded(strHashExpandedKey.toUpperCase(), blExpanded);
        }
        
        try
        {
            // create new lock request
            clearLockRequest();
            lockRequest = new LockRequest(authToken);

            // builds report tree
            GenericTree myGenericTree = new GenericTree();
            DefaultMutableTreeNode manyDomainTree = myGenericTree.buildTree(
                                            hashGlobalExpanded, strCurrentLocalNode, "manydomain", CHANNEL_NAME);
           
            
            // Lock the currently edited report category
            // Get the report category formfields
            Vector vtViewReportCatFormFields = DatabaseSchema.getFormFields("view_report_category");
            // Get a list of all the report categories
            DALQuery cat_query = new DALQuery();
            cat_query.setDomain("REPORTCATEGORY", null, null, null);
            cat_query.setFields(vtViewReportCatFormFields, null);
            cat_query.setWhere(null, 0, "REPORTCATEGORY_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            cat_query.setOrderBy("REPORTCATEGORY_intReportCategoryKey", "ASC");
            ResultSet rsReportCategoryList = cat_query.executeSelect();
            Vector vtCatResult = QueryChannel.lookupRecord(rsReportCategoryList, vtViewReportCatFormFields);

            // Loop through all the report categories, lock the one that is
            // currently being edited, that is if it is the currently selected node
            // in the report categories tree
            for (int i=0; i < vtCatResult.size(); i++)
            {
                String intReportCatKey = ((Hashtable) vtCatResult.get(i)).get("REPORTCATEGORY_intReportCategoryKey").toString();

                if (strCurrentLocalNode.equals("REPORTCATEGORY_" + intReportCatKey))
                {    
                    lockRequest.addLock("REPORTCATEGORY", intReportCatKey, LockRecord.READ_WRITE);
                }    
            }
                        

            // Lock the currently edited report
            // Get the report category formfields
            Vector vtViewReportFormFields = DatabaseSchema.getFormFields("view_report");
            // Get a list of all the reports
            DALSecurityQuery query = new DALSecurityQuery(REPORT_VIEW, authToken);
            query.setDomain("REPORT", null, null, null);
            query.setFields(vtViewReportFormFields, null);
            query.setWhere(null, 0, "REPORT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("REPORT_intReportKey", "ASC");
            ResultSet rsReportList = query.executeSelect();
            Vector vtResult = QueryChannel.lookupRecord(rsReportList, vtViewReportFormFields);

            // Loop through all the reports, lock the one that is
            // currently being edited, that is if it is the currently selected node
            // in the report tree
            for (int i=0; i < vtResult.size(); i++)
            {
                String intReportKey = ((Hashtable) vtResult.get(i)).get("REPORT_intReportKey").toString();

                if (strCurrentLocalNode.equals("REPORT_" + intReportKey))
                {    
                    lockRequest.addLock("REPORT", intReportKey, LockRecord.READ_WRITE);
                }    
            }

            
            // builds XML string
            String strURL = "current=node_link";	
            Vector vtFormFieldMappingName = new Vector();
            vtFormFieldMappingName.add("report category mapping");
            vtFormFieldMappingName.add("report mapping");

            lockRequest.lockDelayWrite();
            
            return myGenericTree.toXML(manyDomainTree, "manydomain", strURL, hashGlobalExpanded, vtFormFieldMappingName);
            //System.err.println("XML : " + strXML);
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, "Unknown error in Report Channel - " + e.toString(), e);
        }

        return "";
    }
    
    public String getSessionID()
    {
        return authToken.getSessionUniqueID();
    }

}

