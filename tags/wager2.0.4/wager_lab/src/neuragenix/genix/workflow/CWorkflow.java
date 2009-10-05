/*
 * CWorkflow.java
 *
 * Created on February 25, 2004
 */

package neuragenix.genix.workflow;

/**
 *
 * @author  Agustian Agustian
 */

// java packages
import java.util.Vector;
import java.util.Hashtable;
import java.util.Enumeration;
import java.io.File;
import java.io.FileInputStream;
import java.io.InputStream;
import java.sql.*;
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
import org.jasig.portal.MultipartDataSource;
import org.jasig.portal.UPFileSpec;

// neuragenix packages
import neuragenix.dao.DatabaseSchema;
import neuragenix.dao.DALQuery;
import neuragenix.dao.DALSecurityQuery;
import neuragenix.dao.SessionManager;
import neuragenix.security.AuthToken;
import neuragenix.common.*;
import neuragenix.genix.workflow.util.WorkflowUtilities;
import neuragenix.genix.workflow.util.XPDLReader;

public class CWorkflow implements IChannel
{

    // At the very least, you'll need to keep track of the static data 
    // and the runtime data that the portal sends you. 
    private ChannelStaticData staticData;
    private ChannelRuntimeData runtimeData;
    private PortalEvent portalEvent;

    // These are the page names that the value current (current page) can contain
    // workflow designer
    private static final String WORKFLOW_DESIGNER = "workflow_designer";

    // workflow template
    private static final String WORKFLOW_SEARCH_WORKFLOW_TEMPLATE = "workflow_search_workflow_template";
    private static final String WORKFLOW_VIEW_WORKFLOW_TEMPLATE = "workflow_view_workflow_template";
    private static final String WORKFLOW_ACTIVATE_WORKFLOW_TEMPLATE = "workflow_activate_workflow_template";
    private static final String WORKFLOW_DEACTIVATE_WORKFLOW_TEMPLATE = "workflow_deactivate_workflow_template";
    private static final String WORKFLOW_DELETE_WORKFLOW_TEMPLATE = "workflow_delete_workflow_template";
    private static final String WORKFLOW_INSTANTIATE_WORKFLOW_TEMPLATE = "workflow_instantiate_workflow_template";

    // workflow instance
    private static final String WORKFLOW_SEARCH_WORKFLOW_INSTANCE = "workflow_search_workflow_instance";
    private static final String WORKFLOW_VIEW_WORKFLOW_INSTANCE = "workflow_view_workflow_instance";
    private static final String WORKFLOW_SUSPEND_WORKFLOW_INSTANCE = "workflow_suspend_workflow_instance";
    private static final String WORKFLOW_RESUME_WORKFLOW_INSTANCE = "workflow_resume_workflow_instance";
    private static final String WORKFLOW_DELETE_WORKFLOW_INSTANCE = "workflow_delete_workflow_instance";

    // workflow system tasks
    private static final String WORKFLOW_MANAGE_SYSTEM_TASKS = "workflow_manage_system_tasks";
    private static final String WORKFLOW_DELETE_SYSTEM_TASKS = "workflow_delete_system_tasks";
    private static final String WORKFLOW_INSERT_SYSTEM_TASKS = "workflow_insert_system_tasks";
    private static final String WORKFLOW_EDIT_SYSTEM_TASKS   = "workflow_edit_system_tasks";
    private static final String WORKFLOW_UPDATE_SYSTEM_TASKS = "workflow_update_system_tasks";

    // workflow files
    private static final String WORKFLOW_MANAGE_FILES  = "workflow_manage_files";
    private static final String WORKFLOW_ADD_FILE      = "workflow_add_file";
    private static final String WORKFLOW_DELETE_FILE   = "workflow_delete_file";
     
    // security
    private static final String SECURITY = "security";

    private AuthToken authToken; // used for security
    private String strActivity;

    private String strCurrent; // This is used to know what the current page the user is on
    private String strXML = ""; // used to create the xml document with the starting XML header
    private String strStylesheet; // Used to specify the stylesheet to use for the portal
    private boolean blFirstTime = false;

    private String strPreviousXML = "";
    private String strPreviousStylesheet;
    private String strErrorMessage = "";

    private ChannelRuntimeData oldRuntimeData;

    /*
     * Lock request object to handle record locking
     */
    private LockRequest lockRequest = null;
	
    /**
     *  Contructs the Workflow Channel
     */
    public CWorkflow() 
    {
            this.strStylesheet = WORKFLOW_SEARCH_WORKFLOW_TEMPLATE;
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
        clearLockRequest();
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
        this.blFirstTime = true;

        // create domains & formfields for this channel
        InputStream file = CWorkflow.class.getResourceAsStream("WorkflowDBSchema.xml");
        DatabaseSchema.loadDomains(file, "WorkflowDBSchema.xml");

        // channel's ids
        Context globalIDContext = null;
        try
        {
            // Get the context that holds the global IDs for this user
            globalIDContext = (Context)staticData.getJNDIContext().lookup("/channel-ids");
        }
        catch(NotContextException nce)
        {
            LogService.log(LogService.ERROR, "Could not find subcontext /channel-ids in JNDI");
        }
        catch(NamingException e)
        {
            LogService.log(LogService.ERROR, e);
        }
        
        try
        {            
            SessionManager.addChannelID(authToken.getSessionUniqueID(), 
                                       "CDownload", 
                                        (String) globalIDContext.lookup("CDownload"));                    
        }
        catch (NotContextException nce) {
            LogService.log(LogService.ERROR, "Could not find channel ID for fname=CDownload and CBiospeciment");
        }
        catch (NamingException e) {
            LogService.log(LogService.ERROR, e);
        }        
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
            this.runtimeData = rd;
            strErrorMessage = "";

            // Get the current page the users is on
            if(runtimeData.getParameter("current") != null)
            {
                blFirstTime = false;
                strCurrent = runtimeData.getParameter("current");
            }
            // first time to channel
            else if(blFirstTime)
            {
                LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
                blFirstTime = false;
                strCurrent = WORKFLOW_SEARCH_WORKFLOW_TEMPLATE;
            }
            // go to the channel from another tab
            else
            {
                LockRequest.emptyAllLocks(authToken.getSessionUniqueID());
                strCurrent = null;
            }

            if (strCurrent != null)
            {
                // user is on the search workflow template page
                if (strCurrent.equals(WORKFLOW_SEARCH_WORKFLOW_TEMPLATE))
                {
                    // if the user doesn't have permission to search workflow template 
                    // display unauthorization message
                    if(!authToken.hasActivity(WORKFLOW_SEARCH_WORKFLOW_TEMPLATE))
                    {
                        strXML  = buildSecurityErrorXMLFile("search workflow template");
                        strStylesheet = SECURITY;
                        return;
                    }

                    // create the query object
                    DALSecurityQuery query = new DALSecurityQuery(WORKFLOW_SEARCH_WORKFLOW_TEMPLATE, authToken);


                    // user click the search button to search tasks
                    if(runtimeData.getParameter("search") != null)
                    {
                        oldRuntimeData = runtimeData;
                        doSearchWorkflowTemplate(oldRuntimeData);
                    }

                    // the user instantiate the workflow template after populating the runtime data
                    else if(runtimeData.getParameter("instantiate") != null)
                    {
                        // if the user doesn't have permission to instantiate a workflow template 
                        // display unauthorization message
                        if(!authToken.hasActivity(WORKFLOW_INSTANTIATE_WORKFLOW_TEMPLATE))
                        {
                            strXML  = buildSecurityErrorXMLFile("instantiate workflow template");
                            strStylesheet = SECURITY;
                            return;
                        }

                        String strWorkflowTemplateKey =
                                runtimeData.getParameter("WORKFLOW_TEMPLATE_intWorkflowTemplateKey");

                        String strDomain = runtimeData.getParameter("strDomain");
                        String strDomainKey = runtimeData.getParameter("intDomainKey");

                        //constructing the runtimedata, this time we only need to construct the date data type
                        boolean blConstructRuntimedata = constructDateRuntimedata(runtimeData, query);

                        //if runtimedata construction was successfully completed
                        if(blConstructRuntimedata)
                        {
                            WorkflowUtilities.prepareRuntimeData(runtimeData, staticData, portalEvent);
                            WorkflowEngine.instantiateWorkflowTemplates(null, runtimeData, 
                                strWorkflowTemplateKey, strDomain, strDomainKey, true, query);
                            doSearchWorkflowTemplate(oldRuntimeData);
                        }
                    }
                    // cancelling the instantiation
                    else if(runtimeData.getParameter("cancelInstantiation") != null)
                    {
                        doSearchWorkflowTemplate(oldRuntimeData);
                    }
                    // back to search workflow template screen
                    else if(runtimeData.getParameter("back") != null)
                    {
                        doSearchWorkflowTemplate(oldRuntimeData);
                    }
                    else if(runtimeData.getParameter("action") != null)
                    {
                        String strWorkflowTemplateKey =
                            runtimeData.getParameter("WORKFLOW_TEMPLATE_intWorkflowTemplateKey");


                        // activate
                        if(runtimeData.getParameter("action").equals("activate"))
                        {
                            // if the user doesn't have permission to activate a workflow template 
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_ACTIVATE_WORKFLOW_TEMPLATE))
                            {
                                strXML  = buildSecurityErrorXMLFile("activate workflow template");
                                strStylesheet = SECURITY;
                                return;
                            }

                            StringBuffer sbfInfo = new StringBuffer();
                                                        
                            if(lockRequest.isValid() && lockRequest.lockWrites())
                            {
                                // activate validation
                                String strErrorMessage = WorkflowManager.validateWorkflowActivation(strWorkflowTemplateKey, query);
                                if (strErrorMessage != null)
                                {
                                    // display error messsage
                                    doSearchWorkflowTemplate(oldRuntimeData);
                                    // compose the message to be shown to the user
                                    sbfInfo.append("<strErrorMessage>");
                                    sbfInfo.append(strErrorMessage);
                                    sbfInfo.append("</strErrorMessage>");
                                }
                                else
                                {
                                    // activate workflow!
                                    Vector vecWorkflowTemplateKey = 
                                        WorkflowManager.activateWorkflowTemplates(strWorkflowTemplateKey, query);
                                    lockRequest.unlockWrites();

                                    doSearchWorkflowTemplate(oldRuntimeData);

                                    // compose the message to be shown to the user
                                    Enumeration enumWorkflowTemplateKey = vecWorkflowTemplateKey.elements();
                                    sbfInfo.append("<strInfo>");
                                    sbfInfo.append("Activated workflow templates: ");
                                    while(enumWorkflowTemplateKey.hasMoreElements())
                                    {
                                        String strTempWorkflowTemplateKey = (String) enumWorkflowTemplateKey.nextElement();
                                        String strWorkflowPackageKey = 
                                            WorkflowManager.getWorkflowPackageKeyGivenWorkflowTemplateKey(strTempWorkflowTemplateKey, query);
                                        String strWorkflowTemplateName = 
                                            WorkflowManager.getWorkflowTemplateName(strTempWorkflowTemplateKey, query);
                                        String strWorkflowPackageName = 
                                            WorkflowManager.getWorkflowPackageName(strWorkflowPackageKey, query);

                                        sbfInfo.append(strWorkflowTemplateName);
                                        sbfInfo.append(" in package ");
                                        sbfInfo.append(strWorkflowPackageName);
                                        if(enumWorkflowTemplateKey.hasMoreElements())
                                        {
                                            sbfInfo.append(",");
                                        }
                                    }
                                    sbfInfo.append("</strInfo>");
                                }
                            }
                            else
                            {
                                doSearchWorkflowTemplate(oldRuntimeData);
                                // compose the message to be shown to the user
                                sbfInfo.append("<strErrorMessage>");
                                sbfInfo.append("Activate Failed - Record is currently being locked by other user. ");
                                sbfInfo.append("Please try again later.");
                                sbfInfo.append("</strErrorMessage>");
                            }

                            strXML += sbfInfo.toString();
                        }
                        // deactivate
                        else if(runtimeData.getParameter("action").equals("deactivate"))
                        {
                            // if the user doesn't have permission to deactivate a workflow template 
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_DEACTIVATE_WORKFLOW_TEMPLATE))
                            {
                                    strXML  = buildSecurityErrorXMLFile("deactivate workflow template");
                                    strStylesheet = SECURITY;
                                    return;
                            }

                            String strWorkflowTemplateName =
                                runtimeData.getParameter("WORKFLOW_TEMPLATE_strName");
                            String strWorkflowPackageName =
                                runtimeData.getParameter("WORKFLOW_PACKAGE_strName");

                            StringBuffer sbfInfo = new StringBuffer();
                            if(lockRequest.isValid() && lockRequest.lockWrites())
                            {
                                // deactivating
                                WorkflowManager.deactivateWorkflowTemplate(strWorkflowTemplateKey, query);
                                lockRequest.unlockWrites();
                                doSearchWorkflowTemplate(oldRuntimeData);

                                // compose the message to be shown to the user
                                //StringBuffer sbfInfo = new StringBuffer();
                                sbfInfo.append("<strInfo>Workflow template ");
                                sbfInfo.append(strWorkflowTemplateName);
                                sbfInfo.append(" in package ");
                                sbfInfo.append(strWorkflowPackageName);
                                sbfInfo.append(" has been deactivated</strInfo>");
                            }
                            else
                            {
                                doSearchWorkflowTemplate(oldRuntimeData);
                                // compose the message to be shown to the user
                                sbfInfo.append("<strErrorMessage>");
                                sbfInfo.append("Deactivate Failed - Record is currently being locked by other user. ");
                                sbfInfo.append("Please try again later.");
                                sbfInfo.append("</strErrorMessage>");
                            }

                            strXML += sbfInfo.toString();	
                        }
                        //delete
                        else if(runtimeData.getParameter("action").equals("delete"))
                        {
                            // if the user doesn't have permission to delete a workflow template 
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_DELETE_WORKFLOW_TEMPLATE))
                            {
                                strXML  = buildSecurityErrorXMLFile("delete workflow template");
                                strStylesheet = SECURITY;
                                return;
                            }

                            String strWorkflowTemplateName =
                                runtimeData.getParameter("WORKFLOW_TEMPLATE_strName");
                            String strWorkflowPackageName =
                                runtimeData.getParameter("WORKFLOW_PACKAGE_strName");

                            String strWorkflowTemplateStatus = 
                                WorkflowManager.getWorkflowTemplateStatus(strWorkflowTemplateKey, query);

                            boolean blHaveActiveInstantiation = 
                                WorkflowManager.haveActiveInstantiation(strWorkflowTemplateKey, query);

                            boolean blDoParentsHaveActiveInstantiations = 
                                WorkflowManager.doParentsHaveActiveInstantiations(strWorkflowTemplateKey, query);

                            boolean blIsSubWorkflowTemplate = 
                                WorkflowManager.isSubWorkflowTemplate(strWorkflowTemplateKey, query);

                            // ensure that there is no active instantiation of the template and
                            // the template status is not active
                            StringBuffer sbfInfo = new StringBuffer();
                            if(!blHaveActiveInstantiation && !blDoParentsHaveActiveInstantiations && 
                               !blIsSubWorkflowTemplate &&
                               strWorkflowTemplateStatus.equals(WorkflowManager.WORKFLOW_TEMPLATE_STATUS_NOT_ACTIVE))
                            {
                                if(lockRequest.isValid() && lockRequest.lockWrites())
                                {
                                    WorkflowEngine.deleteWorkflowTemplateAndChildren(strWorkflowTemplateKey, query);
                                    lockRequest.unlockWrites();
                                }
                                else
                                {
                                    // compose the message to be shown to the user
                                    sbfInfo.append("<strErrorMessage>");
                                    sbfInfo.append("Delete Failed - Record is currently being locked by other user. ");
                                    sbfInfo.append("Please try again later.");
                                    sbfInfo.append("</strErrorMessage>");
                                }
                            }
                            else if(blHaveActiveInstantiation)
                            {
                                sbfInfo.append("<strInfo>Unable to delete workflow template ");
                                sbfInfo.append(strWorkflowTemplateName);
                                sbfInfo.append(" in package ");
                                sbfInfo.append(strWorkflowPackageName);
                                sbfInfo.append(" due to the existance of active instantiation</strInfo>");
                            }
                            else if(strWorkflowTemplateStatus.equals(WorkflowManager.WORKFLOW_TEMPLATE_STATUS_ACTIVE))
                            {
                                sbfInfo.append("<strInfo>Unable to delete workflow template ");
                                sbfInfo.append(strWorkflowTemplateName);
                                sbfInfo.append(" in package ");
                                sbfInfo.append(strWorkflowPackageName);
                                sbfInfo.append(" when its status is ACTIVE</strInfo>");
                            }
                            else if(blDoParentsHaveActiveInstantiations)
                            {
                                sbfInfo.append("<strInfo>Unable to delete workflow template ");
                                sbfInfo.append("due to the existance of active instantiation in parent workflow</strInfo>");
                                buildViewWorkflowTemplateScreen();
                            }
                            else if(blIsSubWorkflowTemplate)
                            {
                                sbfInfo.append("<strInfo>Unable to delete sub workflow template ");
                                sbfInfo.append("</strInfo>");
                                buildViewWorkflowTemplateScreen();
                            }

                            doSearchWorkflowTemplate(oldRuntimeData);
                            strXML += sbfInfo.toString();	
                        }
                        // edit
                        else if(runtimeData.getParameter("action").equals("edit"))
                        {

                        }
                        // prepare runtimedata
                        else if(runtimeData.getParameter("action").equals("prepare_runtimedata"))
                        {
                            // if the user doesn't have permission to instantiate a workflow template 
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_INSTANTIATE_WORKFLOW_TEMPLATE))
                            {
                                strXML  = buildSecurityErrorXMLFile("instantiate workflow template");
                                strStylesheet = SECURITY;
                                return;
                            }

                            String strWorkflowTemplateName =
                                runtimeData.getParameter("WORKFLOW_TEMPLATE_strName");
                            String strWorkflowPackageName =
                                runtimeData.getParameter("WORKFLOW_PACKAGE_strName");

                            String strWorkflowTemplateStatus = 
                                WorkflowManager.getWorkflowTemplateStatus(strWorkflowTemplateKey, query);


                            if(strWorkflowTemplateStatus.equals(WorkflowManager.WORKFLOW_TEMPLATE_STATUS_ACTIVE) &&
                               !WorkflowManager.haveNonActiveTemplates(strWorkflowTemplateKey, query))
                            {
                                //call extra page to fill the runtimedata!!!!!
                                buildWorkflowInstantiationScreen(runtimeData);
                                strXML += "<prepare_runtimedata>true</prepare_runtimedata>";
                                strXML += "<source>workflow_search_workflow_template</source>";
                                strXML += "<WORKFLOW_TEMPLATE_intWorkflowTemplateKey>" + strWorkflowTemplateKey +
                                                  "</WORKFLOW_TEMPLATE_intWorkflowTemplateKey>";	
                            }
                            else //if(WorkflowManager.haveNonActiveTemplates(strWorkflowTemplateKey, query))
                            {
                                doSearchWorkflowTemplate(oldRuntimeData);
                                StringBuffer sbfInfo = new StringBuffer();
                                sbfInfo.append("<strInfo>Please activate all workflow templates before instantiating");
                                sbfInfo.append("</strInfo>");

                                strXML += sbfInfo.toString();
                            }
                            //else
                            //{
                            //	doSearchWorkflowTemplate(oldRuntimeData);
                            //	StringBuffer sbfInfo = new StringBuffer();
                            //	sbfInfo.append("<strInfo>Please activate workflow template ");
                            //	sbfInfo.append(strWorkflowTemplateName);
                            //	sbfInfo.append(" in package ");
                            //	sbfInfo.append(strWorkflowPackageName);
                            //	sbfInfo.append(" before instantiating it</strInfo>");

                            //	strXML += sbfInfo.toString();
                            //}

                            //System.err.println("instantiation - strXML = " + strXML);
                        }
                    }
                    else
                    {
                        buildWorkflowSearchWorkflowTemplateScreen();
                    }
                }
                else if(strCurrent.equals(WORKFLOW_SEARCH_WORKFLOW_INSTANCE))
                {
                    // if the user doesn't have permission to search workflow instance 
                    // display unauthorization message
                    if(!authToken.hasActivity(WORKFLOW_SEARCH_WORKFLOW_INSTANCE))
                    {
                        strXML  = buildSecurityErrorXMLFile("search workflow instance");
                        strStylesheet = SECURITY;
                        return;
                    }

                    // create the query object
                    DALSecurityQuery query = new DALSecurityQuery(WORKFLOW_SEARCH_WORKFLOW_INSTANCE, authToken);
					
                    // user click the search button to search tasks
                    if(runtimeData.getParameter("search") != null)
                    {
                        oldRuntimeData = runtimeData;
                        doSearchWorkflowInstance(oldRuntimeData);
                    }
                    else if(runtimeData.getParameter("back") != null)
                    {
                        doSearchWorkflowInstance(oldRuntimeData);
                    }
                    else if(runtimeData.getParameter("action") != null)
                    {
                        String strWorkflowInstanceKey =
                            runtimeData.getParameter("WORKFLOW_INSTANCE_intWorkflowInstanceKey");

                        // suspend
                        if(runtimeData.getParameter("action").equals("suspend"))
                        {
                            // if the user doesn't have permission to suspend a workflow instance
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_SUSPEND_WORKFLOW_INSTANCE))
                            {
                                strXML  = buildSecurityErrorXMLFile("suspend workflow instance");
                                strStylesheet = SECURITY;
                                return;
                            }

                            String strWorkflowInstanceStatus = 
                                WorkflowManager.getWorkflowInstanceStatus(strWorkflowInstanceKey, query);

							// only allow the user to suspend an in progress, completed and failed workflow instance
                            StringBuffer sbfInfo = new StringBuffer();
							if(strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_IN_PROGRESS)
							   || strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_COMPLETED)
							   || strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_FAILED))
							{
                                if(lockRequest.isValid() && lockRequest.lockWrites())
                                {
									WorkflowManager.updateWorkflowInstancePreviousStatus(strWorkflowInstanceStatus,
																					 	 strWorkflowInstanceKey, query);
                                    WorkflowManager.updateWorkflowInstanceStatus(
                                        WorkflowManager.WORKFLOW_INSTANCE_STATUS_SUSPENDED, strWorkflowInstanceKey, query);
                                    lockRequest.unlockWrites();
                                }
                                else
                                {
                                    // compose the message to be shown to the user
                                    sbfInfo.append("<strErrorMessage>");
                                    sbfInfo.append("Suspend Failed - Record is currently being locked by other user. ");
                                    sbfInfo.append("Please try again later.");
                                    sbfInfo.append("</strErrorMessage>");
                                }
                            }
                            else if(strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_NOT_STARTED) ||
									strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_FAILED))
                            {
                                sbfInfo.append("<strInfo>Unable to suspend the ");
                                sbfInfo.append(strWorkflowInstanceStatus);
                                sbfInfo.append(" workflow instance</strInfo>");
                            }

                            doSearchWorkflowInstance(oldRuntimeData);
                            strXML += sbfInfo.toString();	
                        }
                        // resume
                        else if(runtimeData.getParameter("action").equals("resume"))
                        {
                            // if the user doesn't have permission to resume a workflow instance 
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_RESUME_WORKFLOW_INSTANCE))
                            {
                                strXML  = buildSecurityErrorXMLFile("resume workflow instance");
                                strStylesheet = SECURITY;
                                return;
                            }

                            String strWorkflowInstanceStatus = 
                                WorkflowManager.getWorkflowInstanceStatus(strWorkflowInstanceKey, query);

                            // only allow the user to resume a suspended workflow instance
                            StringBuffer sbfInfo = new StringBuffer();
                            if(strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_SUSPENDED))
                            {
                                if(lockRequest.isValid() && lockRequest.lockWrites())
                                {
									String strPreviousStatus = WorkflowManager.getWorkflowInstancePreviousStatus(
																					strWorkflowInstanceKey, query);
                                    WorkflowManager.updateWorkflowInstanceStatus(strPreviousStatus, 
																				 strWorkflowInstanceKey, query);
                                    lockRequest.unlockWrites();
                                }
                                else
                                {
                                    // compose the message to be shown to the user
                                    sbfInfo.append("<strErrorMessage>");
                                    sbfInfo.append("Resume Failed - Record is currently being locked by other user. ");
                                    sbfInfo.append("Please try again later.");
                                    sbfInfo.append("</strErrorMessage>");
                                }
                            }
                            else if(strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_COMPLETED) ||
                                    strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_FAILED)    ||
                                    strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_NOT_STARTED) ||
									strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_IN_PROGRESS))
                            {
                                sbfInfo.append("<strInfo>Unable to resume the ");
                                sbfInfo.append(strWorkflowInstanceStatus);
                                sbfInfo.append(" workflow instance</strInfo>");
                            }

                            doSearchWorkflowInstance(oldRuntimeData);
                            strXML += sbfInfo.toString();	
                        }
                        // delete
                        else if(runtimeData.getParameter("action").equals("delete"))
                        {
                            // if the user doesn't have permission to delete a workflow instance 
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_DELETE_WORKFLOW_INSTANCE))
                            {
                                strXML  = buildSecurityErrorXMLFile("delete workflow instance");
                                strStylesheet = SECURITY;
                                return;
                            }

                            String strWorkflowTaskKey =
                                WorkflowManager.getSubWorkflowTaskKey(strWorkflowInstanceKey,query);

                            // only allow user to delete the main workflow instance not the sub-workflow instance
                            StringBuffer sbfInfo = new StringBuffer();
                            if(strWorkflowTaskKey == null)
                            {
                                if(lockRequest.isValid() && lockRequest.lockWrites())
                                {
                                    WorkflowEngine.deleteWorkflowInstances(strWorkflowInstanceKey, query);
                                    lockRequest.unlockWrites();
                                }
                                else
                                {
                                    // compose the message to be shown to the user
                                    sbfInfo.append("<strErrorMessage>");
                                    sbfInfo.append("Delete Failed - Record is currently being locked by other user. ");
                                    sbfInfo.append("Please try again later.");
                                    sbfInfo.append("</strErrorMessage>");
                                }
                            }
                            else
                            {
                                sbfInfo.append("<strInfo>Unable to delete a sub-workflow instance</strInfo>");
                            }

                            doSearchWorkflowInstance(oldRuntimeData);
                            strXML += sbfInfo.toString();	
                        }
                    }
                    else
                    {
                        buildWorkflowSearchWorkflowInstanceScreen();
                    }
                }
                // user is on the view workflow template page
                else if(strCurrent.equals(WORKFLOW_VIEW_WORKFLOW_TEMPLATE))
                {
                    // if the user doesn't have permission to search workflow template 
                    // display unauthorization message
                    if(!authToken.hasActivity(WORKFLOW_VIEW_WORKFLOW_TEMPLATE))
                    {
                        strXML  = buildSecurityErrorXMLFile("view workflow template");
                        strStylesheet = SECURITY;
                        return;
                    }
//agus start
//System.err.println("strPackageID = >>>" + runtimeData.getParameter("strPackageID") + "<<<");
//System.err.println("strWorkflowID = >>>" + runtimeData.getParameter("strWorkflowID") + "<<<");
//agus end

                    // create the query object
                    DALSecurityQuery query = new DALSecurityQuery(WORKFLOW_VIEW_WORKFLOW_TEMPLATE, authToken);

                    // the user instantiate the workflow template after populating the runtime data
                    if(runtimeData.getParameter("instantiate") != null)
                    {
                        // if the user doesn't have permission to instantiate a workflow template 
                        // display unauthorization message
                        if(!authToken.hasActivity(WORKFLOW_INSTANTIATE_WORKFLOW_TEMPLATE))
                        {
                            strXML  = buildSecurityErrorXMLFile("instantiate workflow template");
                            strStylesheet = SECURITY;
                            return;
                        }

                        String strWorkflowTemplateKey =
                                runtimeData.getParameter("WORKFLOW_TEMPLATE_intWorkflowTemplateKey");

                        String strDomain = runtimeData.getParameter("strDomain");
                        String strDomainKey = runtimeData.getParameter("intDomainKey");

                        //constructing the runtimedata, this time we only need to construct the date data type
                        boolean blConstructRuntimedata = constructDateRuntimedata(runtimeData, query);

                        //if runtimedata construction was successfully completed
                        if(blConstructRuntimedata)
                        {
                            WorkflowUtilities.prepareRuntimeData(runtimeData, staticData, portalEvent);
                            WorkflowEngine.instantiateWorkflowTemplates(null, runtimeData, 
                                strWorkflowTemplateKey, strDomain, strDomainKey, true, query);
//agus start
//System.err.println("after instantiating --- strPackageID = >>>" + runtimeData.getParameter("strPackageID") + "<<<");
//System.err.println("after instantiating --- strWorkflowID = >>>" + runtimeData.getParameter("strWorkflowID") + "<<<");
//System.err.println("after instantiating --- intWorkflowTemplateKey = >>>" + runtimeData.getParameter("WORKFLOW_TEMPLATE_intWorkflowTemplateKey") + "<<<");
//agus end
                            buildViewWorkflowTemplateScreen();
                        }
                    }
                    // cancelling the instantiation
                    if(runtimeData.getParameter("cancelInstantiation") != null)
                    {
                        buildViewWorkflowTemplateScreen();
                    }
                    else if(runtimeData.getParameter("action") != null)
                    {
                        String strWorkflowTemplateKey =
                            runtimeData.getParameter("WORKFLOW_TEMPLATE_intWorkflowTemplateKey");

                        // activate
                        if(runtimeData.getParameter("action").equals("activate"))
                        {
                            // if the user doesn't have permission to activate a workflow template 
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_ACTIVATE_WORKFLOW_TEMPLATE))
                            {
                                strXML  = buildSecurityErrorXMLFile("activate workflow template");
                                strStylesheet = SECURITY;
                                return;
                            }

                            StringBuffer sbfInfo = new StringBuffer();
                            if(lockRequest.isValid() && lockRequest.lockWrites())
                            {
                                // activating
                                WorkflowManager.activateWorkflowTemplates(strWorkflowTemplateKey, query);
                                lockRequest.unlockWrites();
                            }
                            else
                            {
                                // compose the message to be shown to the user
                                sbfInfo.append("<strErrorMessage>");
                                sbfInfo.append("Activate Failed - Record is currently being locked by other user. ");
                                sbfInfo.append("Please try again later.");
                                sbfInfo.append("</strErrorMessage>");
                            }

                            buildViewWorkflowTemplateScreen();
                            strXML += sbfInfo.toString();
                        }
                        // deactivate
                        else if(runtimeData.getParameter("action").equals("deactivate"))
                        {
                            // if the user doesn't have permission to deactivate a workflow template 
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_DEACTIVATE_WORKFLOW_TEMPLATE))
                            {
                                strXML  = buildSecurityErrorXMLFile("deactivate workflow template");
                                strStylesheet = SECURITY;
                                return;
                            }

                            StringBuffer sbfInfo = new StringBuffer();
                            if(lockRequest.isValid() && lockRequest.lockWrites())
                            {
                                // deactivating
                                WorkflowManager.deactivateWorkflowTemplate(strWorkflowTemplateKey, query);
                                lockRequest.unlockWrites();
                            }
                            else
                            {
                                // compose the message to be shown to the user
                                sbfInfo.append("<strErrorMessage>");
                                sbfInfo.append("Deactivate Failed - Record is currently being locked by other user. ");
                                sbfInfo.append("Please try again later.");
                                sbfInfo.append("</strErrorMessage>");
                            }

                            buildViewWorkflowTemplateScreen();
                            strXML += sbfInfo.toString();
                        }
                        //delete
                        else if(runtimeData.getParameter("action").equals("delete"))
                        {
                            // if the user doesn't have permission to delete a workflow template 
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_DELETE_WORKFLOW_TEMPLATE))
                            {
                                strXML  = buildSecurityErrorXMLFile("delete workflow template");
                                strStylesheet = SECURITY;
                                return;
                            }

                            String strWorkflowTemplateStatus = 
                                WorkflowManager.getWorkflowTemplateStatus(strWorkflowTemplateKey, query);

                            boolean blHaveActiveInstantiation = 
                                WorkflowManager.haveActiveInstantiation(strWorkflowTemplateKey, query);

                            boolean blDoParentsHaveActiveInstantiations = 
                                WorkflowManager.doParentsHaveActiveInstantiations(strWorkflowTemplateKey, query);

                            boolean blIsSubWorkflowTemplate = 
                                WorkflowManager.isSubWorkflowTemplate(strWorkflowTemplateKey, query);

                            // ensure that there is no active instantiation of the template and
                            // the template status is not active
                            StringBuffer sbfInfo = new StringBuffer();
                            if(!blHaveActiveInstantiation && !blDoParentsHaveActiveInstantiations &&
                               !blIsSubWorkflowTemplate &&
                               strWorkflowTemplateStatus.equals(WorkflowManager.WORKFLOW_TEMPLATE_STATUS_NOT_ACTIVE))
                            {
                                if(lockRequest.isValid() && lockRequest.lockWrites())
                                {
                                    WorkflowEngine.deleteWorkflowTemplateAndChildren(strWorkflowTemplateKey, query);
                                    lockRequest.unlockWrites();	
                                    doSearchWorkflowTemplate(oldRuntimeData);
                                }
                                else
                                {
                                    // compose the message to be shown to the user
                                    sbfInfo.append("<strErrorMessage>");
                                    sbfInfo.append("Delete Failed - Record is currently being locked by other user. ");
                                    sbfInfo.append("Please try again later.");
                                    sbfInfo.append("</strErrorMessage>");
                                }
                            }
                            else if(blHaveActiveInstantiation)
                            {
                                sbfInfo.append("<strInfo>Unable to delete workflow template ");
                                sbfInfo.append("due to the existance of active instantiation</strInfo>");
                                buildViewWorkflowTemplateScreen();
                            }
                            else if(strWorkflowTemplateStatus.equals(WorkflowManager.WORKFLOW_TEMPLATE_STATUS_ACTIVE))
                            {
                                sbfInfo.append("<strInfo>Unable to delete workflow template ");
                                sbfInfo.append("when its status is ACTIVE</strInfo>");
                                buildViewWorkflowTemplateScreen();
                            }
                            else if(blDoParentsHaveActiveInstantiations)
                            {
                                sbfInfo.append("<strInfo>Unable to delete workflow template ");
                                sbfInfo.append("due to the existance of active instantiation in parent workflow</strInfo>");
                                buildViewWorkflowTemplateScreen();
                            }
                            else if(blIsSubWorkflowTemplate)
                            {
                                sbfInfo.append("<strInfo>Unable to delete sub workflow template ");
                                sbfInfo.append("</strInfo>");
                                buildViewWorkflowTemplateScreen();
                            }

                            strXML += sbfInfo.toString();	
                        }
                        // edit
                        else if(runtimeData.getParameter("action").equals("edit"))
                        {                            
                            
                        }
                        // prepare runtimedata
                        else if(runtimeData.getParameter("action").equals("prepare_runtimedata"))
                        {
                            // if the user doesn't have permission to instantiate a workflow template 
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_INSTANTIATE_WORKFLOW_TEMPLATE))
                            {
                                strXML  = buildSecurityErrorXMLFile("instantiate workflow template");
                                strStylesheet = SECURITY;
                                return;
                            }

                            String strWorkflowTemplateStatus = 
                                WorkflowManager.getWorkflowTemplateStatus(strWorkflowTemplateKey, query);

                            if(strWorkflowTemplateStatus.equals(WorkflowManager.WORKFLOW_TEMPLATE_STATUS_ACTIVE) &&
                               !WorkflowManager.haveNonActiveTemplates(strWorkflowTemplateKey, query))
                            {
                                //call extra page to fill the runtimedata!!!!!
                                buildWorkflowInstantiationScreen(runtimeData);
                                strXML += "<prepare_runtimedata>true</prepare_runtimedata>";
                                strXML += "<source>workflow_view_workflow_template</source>";
                                strXML += "<WORKFLOW_TEMPLATE_intWorkflowTemplateKey>" + strWorkflowTemplateKey +
                                                  "</WORKFLOW_TEMPLATE_intWorkflowTemplateKey>";
                                strXML += "<strPackageID>" + runtimeData.getParameter("strPackageID") +
                                                  "</strPackageID>";	
                                strXML += "<strWorkflowID>" + runtimeData.getParameter("strWorkflowID") +
                                                  "</strWorkflowID>";	
                            }
                            else
                            {
                                buildViewWorkflowTemplateScreen();
                                StringBuffer sbfInfo = new StringBuffer();
                                sbfInfo.append("<strInfo>Please activate all workflow templates ");
                                sbfInfo.append("before instantiating</strInfo>");

                                strXML += sbfInfo.toString();
                            }
                        }
                    }
                    else
                    {
                        buildViewWorkflowTemplateScreen();
                    }
                }
                
                // user is on the view workflow template page
                else if(strCurrent.equals(WORKFLOW_VIEW_WORKFLOW_INSTANCE))
                {
                    // if the user doesn't have permission to search workflow template 
                    // display unauthorization message
                    if(!authToken.hasActivity(WORKFLOW_VIEW_WORKFLOW_INSTANCE))
                    {
                            strXML  = buildSecurityErrorXMLFile("view workflow instance");
                            strStylesheet = SECURITY;
                            return;
                    }

                    // create the query object
                    DALSecurityQuery query = new DALSecurityQuery(WORKFLOW_VIEW_WORKFLOW_INSTANCE, authToken);

                    // check whether the action is null or not
                    if(runtimeData.getParameter("action") != null)
                    {
                        String strWorkflowInstanceKey =
                            runtimeData.getParameter("WORKFLOW_INSTANCE_intWorkflowInstanceKey");
				
                        // suspend
                        if(runtimeData.getParameter("action").equals("suspend"))
                        {
                            // if the user doesn't have permission to suspend a workflow instance
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_SUSPEND_WORKFLOW_INSTANCE))
                            {
                                strXML  = buildSecurityErrorXMLFile("suspend workflow instance");
                                strStylesheet = SECURITY;
                                return;
                            }

                            String strWorkflowInstanceStatus = 
                                WorkflowManager.getWorkflowInstanceStatus(strWorkflowInstanceKey, query);

                            // only allow the user to suspend an in progress, completed and failed workflow instance
                            StringBuffer sbfInfo = new StringBuffer();
                            if(strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_IN_PROGRESS)
							   || strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_COMPLETED)
							   || strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_FAILED))
                            {
                                if(lockRequest.isValid() && lockRequest.lockWrites())
                                {
									WorkflowManager.updateWorkflowInstancePreviousStatus(strWorkflowInstanceStatus,
																					 	 strWorkflowInstanceKey, query);

                                    WorkflowManager.updateWorkflowInstanceStatus(
                                            WorkflowManager.WORKFLOW_INSTANCE_STATUS_SUSPENDED, strWorkflowInstanceKey, query);
                                    lockRequest.unlockWrites();
                                }
                                else
                                {
                                    // compose the message to be shown to the user
                                    sbfInfo.append("<strErrorMessage>");
                                    sbfInfo.append("Suspend Failed - Record is currently being locked by other user. ");
                                    sbfInfo.append("Please try again later.");
                                    sbfInfo.append("</strErrorMessage>");
                                }
                            }
                            else if(strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_COMPLETED) ||
                                    strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_FAILED) ||
                                    strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_NOT_STARTED))
                            {
                                sbfInfo.append("<strInfo>Unable to suspend a ");
                                sbfInfo.append(strWorkflowInstanceStatus);
                                sbfInfo.append(" workflow instance</strInfo>");
                            }

                            buildViewWorkflowInstanceScreen();
                            strXML += sbfInfo.toString();	
                        }
                        // resume
                        else if(runtimeData.getParameter("action").equals("resume"))
                        {
                            // if the user doesn't have permission to resume a workflow instance 
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_RESUME_WORKFLOW_INSTANCE))
                            {
                                strXML  = buildSecurityErrorXMLFile("resume workflow instance");
                                strStylesheet = SECURITY;
                                return;
                            }

                            String strWorkflowInstanceStatus = 
                                WorkflowManager.getWorkflowInstanceStatus(strWorkflowInstanceKey, query);

                            // only allow the user to resume a suspended workflow instance
                            StringBuffer sbfInfo = new StringBuffer();
                            if(strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_SUSPENDED))
                            {
                                if(lockRequest.isValid() && lockRequest.lockWrites())
                                {
									String strPreviousStatus = WorkflowManager.getWorkflowInstancePreviousStatus(
																					strWorkflowInstanceKey, query);

                                    WorkflowManager.updateWorkflowInstanceStatus(strPreviousStatus,
                                            									 strWorkflowInstanceKey, query);
                                    lockRequest.unlockWrites();
                                }
                                else
                                {
                                    // compose the message to be shown to the user
                                    sbfInfo.append("<strErrorMessage>");
                                    sbfInfo.append("Resume Failed - Record is currently being locked by other user. ");
                                    sbfInfo.append("Please try again later.");
                                    sbfInfo.append("</strErrorMessage>");
                                }
                            }
                            else if(strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_COMPLETED) ||
                                    strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_FAILED)    ||
                                    strWorkflowInstanceStatus.equals(WorkflowManager.WORKFLOW_INSTANCE_STATUS_NOT_STARTED))
                            {
                                sbfInfo.append("<strInfo>Unable to resume a ");
                                sbfInfo.append(strWorkflowInstanceStatus);
                                sbfInfo.append(" workflow instance</strInfo>");
                            }

                            buildViewWorkflowInstanceScreen();
                            strXML += sbfInfo.toString();	
                        }
                        // delete
                        else if(runtimeData.getParameter("action").equals("delete"))
                        {
                            // if the user doesn't have permission to delete a workflow instance 
                            // display unauthorization message
                            if(!authToken.hasActivity(WORKFLOW_DELETE_WORKFLOW_INSTANCE))
                            {
                                strXML  = buildSecurityErrorXMLFile("delete workflow instance");
                                strStylesheet = SECURITY;
                                return;
                            }

                            String strWorkflowTaskKey =
                                WorkflowManager.getSubWorkflowTaskKey(strWorkflowInstanceKey,query);

                            // only allow user to delete the main workflow instance not the sub-workflow instance
                            StringBuffer sbfInfo = new StringBuffer();
                            if(strWorkflowTaskKey == null)
                            {
                                if(lockRequest.isValid() && lockRequest.lockWrites())
                                {
                                    WorkflowEngine.deleteWorkflowInstances(strWorkflowInstanceKey, query);
                                    lockRequest.unlockWrites();
                                }
                                else
                                {
                                    // compose the message to be shown to the user
                                    sbfInfo.append("<strErrorMessage>");
                                    sbfInfo.append("Delete Failed - Record is currently being locked by other user. ");
                                    sbfInfo.append("Please try again later.");
                                    sbfInfo.append("</strErrorMessage>");
                                }
                            }
                            else
                            {
                                sbfInfo.append("<strInfo>Unable to delete a sub-workflow instance</strInfo>");
                            }

                            //go back to search workflow instance page
                            doSearchWorkflowInstance(oldRuntimeData);
                            strXML += sbfInfo.toString();	
                        }
                    }
                    else
                    {
                        buildViewWorkflowInstanceScreen();
                    }
                }
                
                // loading the Workflow designer applet
                else if (strCurrent.equals(WORKFLOW_DESIGNER))
                {
                    strStylesheet = WORKFLOW_DESIGNER;
        
                    strXML = "";
                }
                
                // workflow upload screen
                else if (strCurrent.equals(WORKFLOW_MANAGE_FILES))
                {
                    strStylesheet = WORKFLOW_MANAGE_FILES;
                    strXML = "";
                    
                    /*
                    // if the user doesn't have permission to manage workflow files
                    // display unauthorization message
                    if(!authToken.hasActivity(WORKFLOW_MANAGE_FILES))
                    {
                        strXML  = buildSecurityErrorXMLFile("manage workflow files");
                        strStylesheet = SECURITY;
                        return;
                    }                    
                    */

                    if (runtimeData.getParameter("delete") != null)
                    {
                        /*
                        if(!authToken.hasActivity(WORKFLOW_DELETE_FILE))
                        {
                            strXML  = buildSecurityErrorXMLFile("delete workflow file");
                            strStylesheet = SECURITY;
                            return;
                        }
                        */
                        
                        // the workflow can only be deleted if it's not associated
                        // to an active workflow or associated with any running
                        // instances
                        
                        
                    }
                    else if (runtimeData.getParameter("add") != null)
                    {
                        /*
                        if(!authToken.hasActivity(WORKFLOW_ADD_FILE))
                        {
                            strXML  = buildSecurityErrorXMLFile("add workflow file");
                            strStylesheet = SECURITY;
                            return;
                        }
                        
                        
                        String errMsg = null;
                        if ( ((errMsg = validateWorkflowFileFields(runtimeData)) != null) ||
                             ((errMsg = uploadWorkflowFile(runtimeData)) != null) )
                        {
                            strXML += "<strErrorMessage>" + 
                                      "Unable to add workflow file - " + 
                                       Utilities.cleanForXSL(errMsg) + 
                                       "</strErrorMessage>";         
                            
                            runtimeData.setParameter("WORKFLOW_FILE_strPackageName", 
                                Utilities.cleanForXSL(runtimeData.getParameter("WORKFLOW_FILE_strPackageName")));
                            runtimeData.setParameter("WORKFLOW_FILE_strTemplateName",
                                Utilities.cleanForXSL(runtimeData.getParameter("WORKFLOW_FILE_strTemplateName")));
                        }
                        else
                        {
                            if(lockRequest.isValid() && lockRequest.lockWrites())
                            {
                                doAddWorkflowFile(runtimeData);
                                lockRequest.unlockWrites();

                                // all OK
                                strXML += "<strInfo>" + 
                                          "Successfully added new workflow file." + 
                                          "</strInfo>";
                            }
                            else
                            {
                                // compose the message to be shown to the user
                                // with locking issue
                                strXML += "<strInfo>" +
                                          "Unable to add workflow - Record is currently being locked by other user. " +
                                          "Please try again later." +
                                          "</strInfo>";
                            }
                        }
                        */
                    }
                    
                    buildWorkflowManageFilesScreen();
                }
                
                // manage System task screen
                else if (strCurrent.equals(WORKFLOW_MANAGE_SYSTEM_TASKS))
                {
                    strStylesheet = WORKFLOW_MANAGE_SYSTEM_TASKS;
                    strXML = "";
                    
                    // Function key to filter out when display results
                    String strExcludeKey = null;
                    
                    // if the user doesn't have permission to manage system task
                    // display unauthorization message
                    if(!authToken.hasActivity(WORKFLOW_MANAGE_SYSTEM_TASKS))
                    {
                        strXML  = buildSecurityErrorXMLFile("manage system tasks");
                        strStylesheet = SECURITY;
                        return;
                    }

                    String strFunctionKey = runtimeData.getParameter("WORKFLOW_FUNCTION_intWorkflowFunctionKey");
                    String strName = runtimeData.getParameter("WORKFLOW_FUNCTION_strName");
                    String strType = runtimeData.getParameter("WORKFLOW_FUNCTION_strType");
                    
                    // create the query object
                    DALSecurityQuery query = new DALSecurityQuery(WORKFLOW_SEARCH_WORKFLOW_TEMPLATE, authToken);

                    // add System Task
                    if( runtimeData.getParameter("insert") != null )
                    {
                        
                        // if the user doesn't have permission to add system task
                        // display unauthorization message
                        if(!authToken.hasActivity(WORKFLOW_INSERT_SYSTEM_TASKS))
                        {
                            strXML  = buildSecurityErrorXMLFile("add system tasks");
                            strStylesheet = SECURITY;
                            return;
                        }
                        
                        String errMsg = null;                        
                        if ( (errMsg= validateSystemTaskFields(runtimeData)) != null)
                        {
                            strXML += "<strErrorMessage>" + 
                                      "Unable to add system task - " + 
                                       Utilities.cleanForXSL(errMsg) + 
                                       "</strErrorMessage>";
                            setSystemTaskInfoFromRuntimeData(runtimeData);
                        }                        
                        else if (WorkflowManager.isFunctionNameExist(strName, null, query))
                        {
                            strXML += "<strErrorMessage>" +
                                      "Unable to add system task - Name already exists.  Please enter a unique name." +
                                      "</strErrorMessage>";
                            setSystemTaskInfoFromRuntimeData(runtimeData);
                        }
                        else
                        {
                            // upload the file
                            Object objFilename  = runtimeData.getObjectParameter("WORKFLOW_FUNCTION_strFilename");
                            if ( ((errMsg = uploadClassFile(runtimeData)) != null) )
                            {                         
                                strXML += "<strErrorMessage>" + 
                                          "Unable to add system task - " + 
                                           Utilities.cleanForXSL(errMsg) + 
                                           "</strErrorMessage>";
                                setSystemTaskInfoFromRuntimeData(runtimeData);
                            }
                            else
                            {
                                // insert function
                                if(lockRequest.isValid() && lockRequest.lockWrites())
                                {
                                    Function function = getFunctionFromRuntimeData(runtimeData);
                                    WorkflowManager.save("workflow_function_details", "WORKFLOW_FUNCTION", function, query);
                                    lockRequest.unlockWrites();

                                    // give more detailed success message
                                    String extraInfo = "";
                                    if (objFilename == null)
                                        extraInfo = "  Ensure that the function exists in the existing class file!";
                                    
                                    // all OK
                                    strXML += "<strInfo>" + 
                                              "Successfully added system task." + 
                                              extraInfo +
                                              "</strInfo>";                                                                         
                                }
                                else
                                {
                                    // compose the message to be shown to the user
                                    // with locking issue
                                    strXML += "<strInfo>" +
                                              "Unable to add system task - Record is currently being locked by other user. " +
                                              "Please try again later." +
                                              "</strInfo>";
                                }
                            }
                        }
                    }
                    
                    // user requested Edit of system task
                    else if( runtimeData.getParameter("edit") != null )
                    {                        
                        // if the user doesn't have permission to edit system task
                        // display unauthorization message
                        if(!authToken.hasActivity(WORKFLOW_EDIT_SYSTEM_TASKS))
                        {
                            strXML  = buildSecurityErrorXMLFile("edit system tasks");
                            strStylesheet = SECURITY;
                            return;
                        }
                       
                        // validate paramater
                        String errMsg = WorkflowManager.validateFunction(strFunctionKey, strType, query);                            
                        if ( errMsg != null )
                        {
                            strXML += "<strInfo>" +
                                      "Unable to edit system task - " + 
                                      Utilities.cleanForXSL(errMsg) + 
                                      "</strInfo>";
                        }
                        else
                        {
                            // retrieve record to edit
                            doEditWorkflowSystemTask(strFunctionKey);
                            
                            // remember this key to not display this record
                            // in the results
                            strExcludeKey = strFunctionKey;
                        }                        
                    }
                    
                    // deleting the system task
                    else if( runtimeData.getParameter("delete") != null )
                    {
                        // if the user doesn't have permission to delete system task
                        // display unauthorization message
                        if(!authToken.hasActivity(WORKFLOW_DELETE_SYSTEM_TASKS))
                        {
                            strXML  = buildSecurityErrorXMLFile("delete system tasks");
                            strStylesheet = SECURITY;
                            return;
                        }                        
                        

                        // validate paramater
                        String errMsg = WorkflowManager.validateFunction(strFunctionKey, strType, query);
                        if ( errMsg != null )
                        {
                            strXML += "<strInfo>" +
                                      "Unable to delete system task - " +
                                      Utilities.cleanForXSL(errMsg) + 
                                      "</strInfo>";
                        }
                        else
                        {
                            if (lockRequest.isValid() && lockRequest.lockWrites())
                            {
                                // delete record
                                WorkflowManager.deleteFunction("WORKFLOW_FUNCTION", 
                                                               strFunctionKey, query);
                                
                                lockRequest.unlock();

                                // all OK
                                strXML += "<strInfo>" + 
                                          "Successfully deleted system task." +
                                          "</strInfo>";
                            }
                            else
                            {
                                // compose the message to be shown to the user
                                strXML += "<strInfo>" +
                                          "Unable to delete system task - Record is currently being locked by other user. " +
                                          "Please try again later." +
                                          "</strInfo>";
                            }                                
                        }                        
                    }
                    
                    // changing values of system task
                    else if ( runtimeData.getParameter("update") != null )
                    {
                        // if the user doesn't have permission to update system task
                        // display unauthorization message
                        if(!authToken.hasActivity(WORKFLOW_UPDATE_SYSTEM_TASKS))
                        {
                            strXML  = buildSecurityErrorXMLFile("update system tasks");
                            strStylesheet = SECURITY;
                            return;
                        }
                        
                        
                        String errMsg = null;
                        
                        // validation
                        if ( (errMsg= validateSystemTaskFields(runtimeData)) != null )
                        {
                            strXML += "<strErrorMessage>" + 
                                      "Unable to update system task - " + 
                                      errMsg + "</strErrorMessage>";
                            setSystemTaskInfoFromRuntimeData(runtimeData);           
                            strExcludeKey = strFunctionKey;
                        }
                        else if (WorkflowManager.isFunctionNameExist(strName, strFunctionKey, query))
                        {
                            strXML += "<strErrorMessage>" +
                                      "Unable to update system task - Name already exists.  Please enter a unique name." +
                                      "</strErrorMessage>";
                            setSystemTaskInfoFromRuntimeData(runtimeData);
                            strExcludeKey = strFunctionKey;
                        }
                        else if ( (errMsg = WorkflowManager.validateFunction(strFunctionKey, strType, query)) != null )
                        {
                            strXML += "<strErrorMessage>" + 
                                      "Unable to update system task - " + 
                                      errMsg + "</strErrorMessage>";
                            setSystemTaskInfoFromRuntimeData(runtimeData);           
                            strExcludeKey = strFunctionKey;
                        }
                        else
                        {
                            // upload the file, if required
                            Object objFilename  = runtimeData.getObjectParameter("WORKFLOW_FUNCTION_strFilename");
                            if ( ((errMsg = uploadClassFile(runtimeData)) != null) )
                            {                          
                                strXML += "<strErrorMessage>" + 
                                          "Unable to update system task - " + 
                                          Utilities.cleanForXSL(errMsg) + 
                                          "</strErrorMessage>";
                                setSystemTaskInfoFromRuntimeData(runtimeData);                                
                                strExcludeKey = strFunctionKey;                                
                            }
                            else
                            {
                                if (lockRequest.isValid() && lockRequest.lockWrites())
                                {
                                    // update record
                                    Function function = getFunctionFromRuntimeData(runtimeData);
                                    WorkflowManager.update("workflow_function_details", "WORKFLOW_FUNCTION", function, query);                                                                    
                                    lockRequest.unlock();

                                    // give more detailed success message
                                    String extraInfo = "";
                                    if (objFilename == null)
                                        extraInfo = "  Ensure that the function exists in the existing class file!";
                                    
                                    // all OK
                                    strXML += "<strInfo>" + 
                                              "Successfully updated system task." +
                                              extraInfo +
                                              "</strInfo>";
                                }
                                else
                                {
                                    // compose the message to be shown to the user
                                    strXML += "<strInfo>" +
                                              "Unable to update system task - Record is currently being locked by other user. " +
                                              "Please try again later." +
                                              "</strInfo>";
                                }                                
                            }
                        }
                    }

                    // build the base workflow system task screen
                    buildWorkflowManageSystemTasksScreen(strExcludeKey);                                       
                }
                
                strPreviousXML = strXML;
                strPreviousStylesheet = strStylesheet;
            }
            else
            {
                strXML = strPreviousXML;
                strStylesheet = strPreviousStylesheet;
            }
            
            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><workflow>" + strXML + "</workflow>";
            
    	}
    	catch(Exception e)
    	{
            System.out.println(e.toString());
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - setRuntimeData" + e.toString());
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
        //System.err.println(strXML);

        // specify the stylesheet selector 

        xslt.setXSL("CWorkflow.ssl", strStylesheet, runtimeData.getBrowserInfo());

        // set parameters that the stylesheet needs.
        xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL());

        // set download details param
        UPFileSpec upfTmp = new UPFileSpec(runtimeData.getBaseActionURL( true ));        
        upfTmp.setTargetNodeId(SessionManager.getChannelID(authToken.getSessionUniqueID(), "CDownload"));
        xslt.setStylesheetParameter("downloadURL", upfTmp.getUPFile());
        if( SessionManager.getChannelID(authToken.getSessionUniqueID(), "CDownload") != null )
            xslt.setStylesheetParameter("downloadNodeId", SessionManager.getChannelID(authToken.getSessionUniqueID(), "CDownload"));
        
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
                LogService.ERROR, "Unknown error in Workflow Channel - " + e.toString(), e);
        }
        finally
        {
            lockRequest = null;
        }
    }


    /*
     * Build the view workflow instance screen
     */
    private void buildViewWorkflowInstanceScreen()
    {
        strStylesheet = WORKFLOW_VIEW_WORKFLOW_INSTANCE;
        Vector vecViewWorkflowInstanceFormFields = DatabaseSchema.getFormFields("view_workflow_instance");
        DALQuery query = new DALQuery();

        try
        {
            query.setDomain("WORKFLOW_INSTANCE", null, null, null);
            query.setFields(vecViewWorkflowInstanceFormFields, null);
            query.setWhere(null, 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=", 
                           runtimeData.getParameter("WORKFLOW_INSTANCE_intWorkflowInstanceKey"), 0, 
                           DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();

            strXML = QueryChannel.buildFormLabelXMLFile(vecViewWorkflowInstanceFormFields) + 
                 QueryChannel.buildSearchXMLFile("workflow_view_workflow_instance_result",
                                                 rsResultSet, vecViewWorkflowInstanceFormFields);
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
                "Unknown error in Workflow Channel - buildViewWorkflowInstanceScreen" + e.toString(), e);
        }
    }


    /*
     * Build the view workflow template screen
     */
    private void buildViewWorkflowTemplateScreen()
    {
        strStylesheet = WORKFLOW_VIEW_WORKFLOW_TEMPLATE;
        Vector vecViewWorkflowTemplateFormFields = DatabaseSchema.getFormFields("view_workflow_template");
        DALQuery query = new DALQuery();

        try
        {
            query.setDomain("WORKFLOW_TEMPLATE", null, null, null);
            query.setDomain("WORKFLOW_PACKAGE", "WORKFLOW_PACKAGE_intWorkflowPackageKey",
                                            "WORKFLOW_TEMPLATE_intWorkflowPackageKey", "LEFT JOIN");
            query.setFields(vecViewWorkflowTemplateFormFields, null);
            query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_intWorkflowTemplateKey", "=", 
                                            runtimeData.getParameter("WORKFLOW_TEMPLATE_intWorkflowTemplateKey"), 0, 
                                            DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();

            strXML = QueryChannel.buildFormLabelXMLFile(vecViewWorkflowTemplateFormFields) + 
                 QueryChannel.buildSearchXMLFile("workflow_view_workflow_template_result",
                                                 rsResultSet, vecViewWorkflowTemplateFormFields);
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
                "Unknown error in Workflow Channel - buildViewWorkflowTemplateScreen" + e.toString(), e);
        }
    }

    /**
     * Build the search workflow template screen
     */
    private void buildWorkflowSearchWorkflowTemplateScreen()
    {
        strStylesheet = WORKFLOW_SEARCH_WORKFLOW_TEMPLATE;
        Vector vecSearchWorkflowTemplateFormFields = DatabaseSchema.getFormFields("search_workflow_template");

        try
        {
            // clear the current lock
            clearLockRequest();

            strXML = QueryChannel.buildFormLabelXMLFile(vecSearchWorkflowTemplateFormFields) +
                     QueryChannel.buildViewXMLFile(vecSearchWorkflowTemplateFormFields, runtimeData);
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
                "Unknown error in Workflow Channel - buildWorkflowSearchWorkflowTemplateScreen" + e.toString(), e);
        }
    }

    /**
     * Get the System task Parameter types based on name
     *
     * @param strFunctionParamterNames Parameter Names of a System Task
     *
     * @return String representation of parameter types
     */
    private String getParameterTypesFromNames(String strFunctionParameterNames)
    {            
        String strFunctionParameterTypes = "";

        if (strFunctionParameterNames != null)
        {
            String[] names = strFunctionParameterNames.split(",");

            for (int i=0; (names != null) && (i < names.length); i++)
            {
                /*
                String abbrType = names[i].substring(0, 4);
                String type = "";

                if (abbrType.equals("str"))
                    type = "java.lang.String";
                else if (abbrType.equals("int"))
                    type = "java.lang.Integer";
                else if (abbrType.equals("bl"))
                    type = "java.lang.Boolean";
                */

                // append the type
                if (names[i].length() > 0)
                {
                    strFunctionParameterTypes += "java.lang.String";

                    // add the comma if there's more than one parameters
                    if (i != names.length-1)
                        strFunctionParameterTypes += ",";
                }
            }
        }
        return strFunctionParameterTypes;
    }
     
    /**
     * Translate the runtime data into a function object
     *
     * @param runtimeData Runtime data object
     *
     * @return Function object 
     */
    private Function getFunctionFromRuntimeData(ChannelRuntimeData runtimeData)
    {
        String strFunctionKey            = runtimeData.getParameter("WORKFLOW_FUNCTION_intWorkflowFunctionKey").trim();
        String strName                   = runtimeData.getParameter("WORKFLOW_FUNCTION_strName").trim();
        String strFunctionName           = runtimeData.getParameter("WORKFLOW_FUNCTION_strFunctionName").trim();
        String strType                   = runtimeData.getParameter("WORKFLOW_FUNCTION_strType").trim();            
        String strClassName              = runtimeData.getParameter("WORKFLOW_FUNCTION_strClassName").trim();            

        // if full class path has not been specified, specify it            
        if (strClassName.indexOf("neuragenix.genix.workflow.function.") == -1)
            strClassName = "neuragenix.genix.workflow.function." + strClassName;            

        String strFunctionParameterNames = runtimeData.getParameter("WORKFLOW_FUNCTION_strFunctionParameterNames").trim();            
        // remove any trailing comma
        if (strFunctionParameterNames.endsWith(","))
            strFunctionParameterNames = strFunctionParameterNames.substring(0, strFunctionParameterNames.length()-1);            
        String strFunctionParameterTypes = getParameterTypesFromNames(strFunctionParameterNames);

        Function function = new Function(strFunctionKey, strName, 
                                         strClassName, strFunctionName, 
                                         strFunctionParameterNames, 
                                         strFunctionParameterTypes, strType);
        return function;
    }

    /**
     * Edit the System Task screen
     */    
    private void doEditWorkflowSystemTask(String strFunctionKey)
    {
        Vector vecEditSystemTaskFormFields = DatabaseSchema.getFormFields("workflow_function_details");
        DALQuery query = new DALQuery();

        try
        {
            query.setDomain("WORKFLOW_FUNCTION", null, null, null);
            query.setFields(vecEditSystemTaskFormFields, null);
            query.setWhere(null, 0, "WORKFLOW_FUNCTION_intDeleted", "=", "0", 0, 
                           DALQuery.WHERE_HAS_VALUE);
            
            if ( (strFunctionKey != null) && (strFunctionKey != "") )
                query.setWhere("AND", 0, "WORKFLOW_FUNCTION_intWorkflowFunctionKey", "=", 
                               strFunctionKey, 0, DALQuery.WHERE_HAS_VALUE);

            // checking data validation
            String strDataValidation = QueryChannel.validateData(vecEditSystemTaskFormFields, runtimeData);
            if (strDataValidation == null)
            {
                ResultSet rsResultSet = query.executeSelect();

                // if we can build lock record for the list
                if (buildWorkflowManageSystemTaskLock(rsResultSet))
                {                    
                    strXML += QueryChannel.buildSearchXMLFile("workflow_system_task_info",
                                                              rsResultSet, vecEditSystemTaskFormFields);
                }
                // display locking error message
                else
                {                        
                    strXML += "<strInfo>" + "Record locking produced some error. Please try again later." +
                                      "</strInfo>";                        
                }

                // close the result set
                rsResultSet.close();
            }
            else
            {
                strXML += "<strInfo>" + strDataValidation + "</strInfo>";
            }                
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - doEditWorkflowSystemTask " + e.toString(), e);
        }
    }
       
    /** 
     * Add workflow file
     *
     * @param runtimeData Runtime data object
     *
     * @return Error message status. Returns null if no errors
     */
    private String doAddWorkflowFile(ChannelRuntimeData runtimeData)
    {
        String strStatus = null;
        
        DALQuery query = new DALQuery();
        
        try
        {
            query.setDomain("WORKFLOW_FILE", null, null, null);
            
            
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - doAddWorkflowFile " + e.toString(), e);
        }
        
        return strStatus;
    }
    
    /**
     * Upload Workflow file
     *
     * @param runtimeData run time data object
     *
     * @return Error message status.  Returns null if no errors.
     */
    private String uploadWorkflowFile(ChannelRuntimeData runtimeData)
    {
        String strStatus = null;
        String strFilename = null;
        
        try
        {
            String packageName = runtimeData.getParameter("WORKFLOW_FILE_strPackageName");
            
            if ( (strStatus == null) &&
                ((strStatus = QueryChannel.checkFileSize("WORKFLOW_FILE_strFilename", runtimeData))       == null) && 
                ((strStatus = QueryChannel.checkFileType("WORKFLOW_FILE_strFilename", runtimeData,"xml")) == null) )
            {                           
                // upload file
                QueryChannel.uploadFile("WORKFLOW_FILE_strFilename", runtimeData, 
                    WorkflowManager.XPDL_DIRECTORY, packageName  + ".xml");
                
                // once uploaded, let's verify that the file is in the correct format!
                File file = new File(WorkflowManager.XPDL_DIRECTORY + "/" + packageName + ".xml");

                //construct the FileInputStream
                FileInputStream fileInputStream = new FileInputStream(file);                
                
                if (XPDLReader.parseXML(fileInputStream) != null)
                {                    
                    // if not a valid format file, then delete this sucker! 
                    file.delete();                    
                    strStatus = "Workflow file is not in a valid format or file is corrupted.";
                }
            }
        }
        catch (Exception e)
        {
            strStatus = "Upload workflow file error.";
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - uploadClassFile " + e.toString(), e);
        }
        
        return strStatus;
    }
    
    /**
     * Upload Class file
     * 
     * @param runtimeData Runtime data object
     *
     * @return Error message status.  Returns null if no errors.
     */
    private String uploadClassFile(ChannelRuntimeData runtimeData)        
    {
        String strStatus = null;    
        String strType = null;
        String strName = null;
        String strFunctionKey = null;
        String strClassName = null;
        DALSecurityQuery securityQuery = new DALSecurityQuery();
        
        String  conflictClass = null;
        boolean classExists = false;

        try
        {
            // define class name                
            String className = runtimeData.getParameter("WORKFLOW_FUNCTION_strClassName");
            Object filename  = runtimeData.getObjectParameter("WORKFLOW_FUNCTION_strFilename");
            
            // check if the entered Class file entered is existing
            DALQuery query = new DALQuery();
            query.setDomain("WORKFLOW_FUNCTION", null, null, null);
            query.setFields(DatabaseSchema.getFormFields("workflow_function_details"), null);
            query.setWhere(null, 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
                                   
            ResultSet rsResultSet = query.executeSelect();

            while(rsResultSet.next())
            {
                // check each function key does not break business rules
                // when replacing a file
                strFunctionKey = rsResultSet.getString("WORKFLOW_FUNCTION_intWorkflowFunctionKey");
                strName        = rsResultSet.getString("WORKFLOW_FUNCTION_strName");
                strType        = rsResultSet.getString("WORKFLOW_FUNCTION_strType");
                strClassName   = rsResultSet.getString("WORKFLOW_FUNCTION_strClassName");

                
                // name exists - regardless of case!
                if (strClassName.equalsIgnoreCase("neuragenix.genix.workflow.function."+className))
                {
                    conflictClass = strClassName.replaceFirst("neuragenix.genix.workflow.function.", "");
                }
             
                // class exists - case sensitive!
                if (strClassName.equals("neuragenix.genix.workflow.function."+className))
                    classExists = true;
                
                if ( (filename != null) &&
                     strClassName.equals("neuragenix.genix.workflow.function."+className) &&
                     (strStatus = WorkflowManager.validateFunction(strFunctionKey, strType, securityQuery)) != null )
                {
                    strStatus = "Cannot replace class file as another system task, " + strName + ", is using this file.  " +
                                "The file can only be replaced if " + strName + " is editable.";
                    //strStatus = "Cannot replace class file as another System Task (" + 
                    //            strName + ") using this file did not satisfy the condition: " + 
                    //            strStatus;
                    break;
                }
            }
            
            // close result set
            rsResultSet.close();

            // if class does not exists, make sure user uploads the file!!!!
            if ( (classExists == false) && (filename == null) )
            {
                if (conflictClass != null)
                    strStatus = "Class name conflicts with an existing class name of \"" + conflictClass + "\".  Please enter a different class name";
                else
                    strStatus = "Class does not exists.  You must upload a file called \"" + className + ".class\"";
            }
            // ensure file size is within acceptable limits
            else if ( (filename != null) && (strStatus == null) &&
                ((strStatus = QueryChannel.checkFileSize("WORKFLOW_FUNCTION_strFilename", runtimeData))         == null) && 
                ((strStatus = QueryChannel.checkFileType("WORKFLOW_FUNCTION_strFilename", runtimeData,"class")) == null) )
            {                           
                // upload file
                QueryChannel.uploadFile("WORKFLOW_FUNCTION_strFilename", runtimeData, 
                    WorkflowManager.FUNCTION_DIRECTORY, className + ".class");
            }                
        }
        catch (Exception e)
        {
            strStatus = "Upload class file error.";
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - uploadClassFile " + e.toString(), e);
        }

        return strStatus;
    }

    /**
     * Validate System Task Fields
     *
     * @param runtimeData ChannelRuntimeData object to be validated
     *
     * @return Error message of validation.  Returns null if no errors.
     */
    private String validateSystemTaskFields(ChannelRuntimeData runtimeData)
    {
        String errMsg = null;
        String filterTypes = "A-Z, 0-9, underscores and hyphens";
        String nameFilter  = "([a-zA-Z0-9_\\-]*)";
        String paramFilter = "([a-zA-Z0-9_\\-,]*)";

        // convert runtime data into a function object
        Function function  = getFunctionFromRuntimeData(runtimeData);
        
        // validate name
        if ( (errMsg = validateField(function.getName(), "Name", true, nameFilter, filterTypes)) != null )
            return errMsg;
        
        // validate class name
        // remove the full package name
        String className = function.getClassName().replaceFirst("neuragenix.genix.workflow.function.", "");        
        if ( (errMsg = validateField(className, "Class name", true, nameFilter, filterTypes)) != null )
            return errMsg;
        
        // validate function name
        if ( (errMsg = validateField(function.getFunctionName(), "Function name", true, nameFilter, filterTypes)) != null )
            return errMsg;
        
        // validate parameters
        if ( (errMsg = validateField(function.getParameterNames(), "Parameters", false, paramFilter, filterTypes)) != null )
            return errMsg;       
        if (function.getParameterNames().length() > 0) // validate parameter elements if required
        {
            String[] paramNames = function.getParameterNames().split(",");
            for (int i=0; (errMsg != null) && ( i <paramNames.length); i++)
                if (paramNames[i].length() > 0)
                    return "Parameters must be defined after each \",\"";
        }

        // validate file name, if required
        MultipartDataSource file = (MultipartDataSource) runtimeData.getObjectParameter("WORKFLOW_FUNCTION_strFilename");        
        if ( (file != null) && 
             (file.getName().equals(className + ".class") == false) )
            return "Filename must be the same as the class name.";
        
        return errMsg;
    }

    /**
     * Validate File Upload Fields
     * 
     * @param runtimeData Runtime data object
     *
     * @return Error message status.  Returns null if no errors.
     */
    private String validateWorkflowFileFields(ChannelRuntimeData runtimeData)
    {
        String errMsg = null;
        
        String filterTypes = "A-Z, 0-9, underscores, hyphens and spaces";
        String nameFilter  = "([a-zA-Z0-9_\\- ]*)";
        
        String packageName  = runtimeData.getParameter("WORKFLOW_FILE_strPackageName").trim();
        String templateName = runtimeData.getParameter("WORKFLOW_FILE_strTemplateName").trim();
        
        if ( (errMsg = validateField(packageName, "Package name", true, nameFilter, filterTypes)) != null )
            return errMsg;
        
        if ( (errMsg = validateField(templateName, "Template name", true, nameFilter, filterTypes)) != null )
            return errMsg;
        
        MultipartDataSource file = (MultipartDataSource) runtimeData.getObjectParameter("WORKFLOW_FILE_strFilename");
        if ( file == null )
            return "File has not been specified.  Please specify file to upload.";
        else if ( file.getName().equals(packageName+".xml") == false )
            return "Filename must be the same as the package name.";
        
        try
        {
            // check that the package name and template name is unique
            DALQuery query = new DALQuery();
            query.setDomain("WORKFLOW_FILE", null, null, null);
            query.setFields(DatabaseSchema.getFormFields("workflow_file_details"), null);
            query.setWhere(null, 0, "WORKFLOW_FILE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResultSet = query.executeSelect();

            while (rsResultSet.next())
            {
                String strPackageName  = rsResultSet.getString("WORKFLOW_FILE_strPackageName");
                String strTemplateName = rsResultSet.getString("WORKFLOW_FILE_strTemplateName");

                if (strPackageName.equals(packageName))
                {
                    errMsg = "Package name already exists.  Package name must be unique.";
                    break;
                }
                else if (strTemplateName.equals(templateName))
                {
                    errMsg = "Template name already exists.  Template name must be unique.";
                    break;
                }
            }

            // close result set
            rsResultSet.close();
        }
        catch (Exception e)
        {
            errMsg = "validation failed.";
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - validateWorkflowFileFields " + e.toString(), e);
        }

        return errMsg;
    }
    
    /**
     * Validate field based on paramter types
     * 
     * @param field Field string to be validated
     * @param fieldName Name of field to be represented in error message
     * @param required Validation for required fields
     * @param filter Filter for acceptable field value
     * @param filterTypes Filter to be represented in the error message
     *
     * @return Error message for any validation errors.  Returns null if no errors;
     */
    private String validateField(String field, String fieldName, boolean required, 
                                 String filter, String filterTypes)
    {
        String errMsg = null;
        
        if (required && field.length() == 0)
            errMsg = fieldName + " cannot be blank.";
        else if ( (filter != null) && (field.matches(filter) == false) )
            errMsg = fieldName + " is invalid.  Only " + filterTypes + " are allowed.";
                
        return errMsg;
    }
    
    /**
     * Set the system task info based on the runtime data
     * 
     * @param runtimeData ChannelRuntimeData object
     */
    private void setSystemTaskInfoFromRuntimeData(ChannelRuntimeData runtimeData)
    {
        String strFunctionKey            = runtimeData.getParameter("WORKFLOW_FUNCTION_intWorkflowFunctionKey");
        String strName                   = runtimeData.getParameter("WORKFLOW_FUNCTION_strName");
        String strClassName              = runtimeData.getParameter("WORKFLOW_FUNCTION_strClassName");
        String strFunctionName           = runtimeData.getParameter("WORKFLOW_FUNCTION_strFunctionName");
        String strFunctionParameterNames = runtimeData.getParameter("WORKFLOW_FUNCTION_strFunctionParameterNames");
        String strFunctionParameterTypes = getParameterTypesFromNames(strFunctionParameterNames);
        String strType                   = runtimeData.getParameter("WORKFLOW_FUNCTION_strType");

        if (strFunctionKey == null)
            strFunctionKey = "";
        if (strName == null)
            strName = "";
        if (strClassName == null)
            strClassName = "";
        if (strFunctionName == null)
            strFunctionName = "";
        if (strFunctionParameterNames == null)
            strFunctionParameterNames = "";
        if (strType == null)
            strType = "";

        strXML += "<workflow_system_task_info>" +
        
                  "<WORKFLOW_FUNCTION_intWorkflowFunctionKey>" + 
                  Utilities.cleanForXSL(strFunctionKey) + 
                  "</WORKFLOW_FUNCTION_intWorkflowFunctionKey>" +                      
                  
                  "<WORKFLOW_FUNCTION_strName>" + 
                  Utilities.cleanForXSL(strName) + 
                  "</WORKFLOW_FUNCTION_strName>" +
                  
                  "<WORKFLOW_FUNCTION_strClassName>neuragenix.genix.workflow.function." + 
                  Utilities.cleanForXSL(strClassName) 
                  + "</WORKFLOW_FUNCTION_strClassName>" +
                  
                  "<WORKFLOW_FUNCTION_strFunctionName>" + 
                  Utilities.cleanForXSL(strFunctionName) + 
                  "</WORKFLOW_FUNCTION_strFunctionName>" +
                  
                  "<WORKFLOW_FUNCTION_strFunctionParameterNames>" + 
                  Utilities.cleanForXSL(strFunctionParameterNames) + 
                  "</WORKFLOW_FUNCTION_strFunctionParameterNames>" +
                  
                  "<WORKFLOW_FUNCTION_strType>" + 
                  strType + 
                  "</WORKFLOW_FUNCTION_strType>" +
                  
                  "</workflow_system_task_info>";
    }

    /**
     * Build the base Workflow Manage files screen
     *
     */
    private void buildWorkflowManageFilesScreen()
    {
        Vector vecManageWorkflowFileFormFields = DatabaseSchema.getFormFields("workflow_file_details");
        DALQuery query = new DALQuery();
        
        try
        {
            query.setDomain("WORKFLOW_FILE", null, null, null);
            query.setFields(vecManageWorkflowFileFormFields, null);
            query.setWhere(null, 0, "WORKFLOW_FILE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("WORKFLOW_FILE_intWorkflowFileKey", "ASC");
            
            String strDataValidation = QueryChannel.validateData(vecManageWorkflowFileFormFields, runtimeData);
            if (strDataValidation == null)
            {
                ResultSet rsResultSet = query.executeSelect();
                
                if (buildWorkflowManageFileLock(rsResultSet))
                {
                    strXML += QueryChannel.buildFormLabelXMLFile(vecManageWorkflowFileFormFields) + 
                              QueryChannel.buildSearchXMLFile("workflow_file_result",
                                                              rsResultSet, vecManageWorkflowFileFormFields);
                }
                else
                {
                    strXML += QueryChannel.buildFormLabelXMLFile(vecManageWorkflowFileFormFields) + 
                              "<strInfo>" + "Record locking produced some error. Please try again later." +
                                      "</strInfo>";
                }
                // close the result set
                rsResultSet.close();                
            }
            else
            {
                strXML = QueryChannel.buildFormLabelXMLFile(vecManageWorkflowFileFormFields);
                strXML += "<strInfo>" + strDataValidation + "</strInfo>";                
            }
            
        }        
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - buildWorkflowManageFilesScreen " + e.toString(), e);
        }
    }
    
    /**
     * Build the base Workflow Manage System Task screen
     * 
     * @param strExcludeKey Function key to exclude from the results
     */
    private void buildWorkflowManageSystemTasksScreen(String strExcludeKey)
    {            
        Vector vecViewManageSystemTaskFormFields = DatabaseSchema.getFormFields("workflow_function_details");
        DALQuery query = new DALQuery();

        try
        {
            query.setDomain("WORKFLOW_FUNCTION", null, null, null);
            query.setFields(vecViewManageSystemTaskFormFields, null);
            query.setWhere(null, 0, "WORKFLOW_FUNCTION_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setOrderBy("WORKFLOW_FUNCTION_intWorkflowFunctionKey", "ASC");
            
            if (strExcludeKey != null)
                query.setWhere("AND", 0, "WORKFLOW_FUNCTION_intWorkflowFunctionKey", 
                               "<>", strExcludeKey, 0, DALQuery.WHERE_HAS_VALUE);


            // checking data validation
            String strDataValidation = QueryChannel.validateData(vecViewManageSystemTaskFormFields, runtimeData);
            if (strDataValidation == null)
            {
                ResultSet rsResultSet = query.executeSelect();
                
                // if we can build lock record for the list
                if (buildWorkflowManageSystemTaskLock(rsResultSet))
                {                    
                    strXML += QueryChannel.buildFormLabelXMLFile(vecViewManageSystemTaskFormFields) + 
                              QueryChannel.buildLOVXMLFile("WORKFLOW_FUNCTION_strType", "") +
                              QueryChannel.buildSearchXMLFile("workflow_manage_system_tasks_result",
                                                              rsResultSet, vecViewManageSystemTaskFormFields);
                    // close the result set
                    rsResultSet.close();
                }
                else
                {
                    strXML += QueryChannel.buildFormLabelXMLFile(vecViewManageSystemTaskFormFields) + 
                              QueryChannel.buildLOVXMLFile("WORKFLOW_FUNCTION_strType", "") +
                              "<strInfo>" + "Record locking produced some error. Please try again later." +
                                      "</strInfo>";
                }
            }
            else
            {
                strXML = QueryChannel.buildFormLabelXMLFile(vecViewManageSystemTaskFormFields) + 
                         QueryChannel.buildLOVXMLFile("WORKFLOW_FUNCTION_strType", "");
                strXML += "<strInfo>" + strDataValidation + "</strInfo>";
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - buildWorkflowManageSystemTasksScreen " + e.toString(), e);
        }
    }

    /**
     * build the search workflow template result screen
     *
     * @param runtimeData	the runtimedata
     */
    private void doSearchWorkflowTemplate(ChannelRuntimeData runtimeData)
    {
        strStylesheet = WORKFLOW_SEARCH_WORKFLOW_TEMPLATE;
        Vector vecSearchWorkflowTemplateFormFields = DatabaseSchema.getFormFields("search_workflow_template");
        DALQuery query = new DALQuery();

        try
        {
            query.setDomain("WORKFLOW_TEMPLATE", null, null, null);
            query.setDomain("WORKFLOW_PACKAGE", "WORKFLOW_PACKAGE_intWorkflowPackageKey",
                                            "WORKFLOW_TEMPLATE_intWorkflowPackageKey", "LEFT JOIN");
            query.setFields(vecSearchWorkflowTemplateFormFields, null);
            query.setWhere(null, 0, "WORKFLOW_TEMPLATE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            // set the name
            if(runtimeData.getParameter("WORKFLOW_TEMPLATE_strName") != null &&
               runtimeData.getParameter("WORKFLOW_TEMPLATE_strName").length() != 0)
            {
                query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_strName", "LIKE",
                               runtimeData.getParameter("WORKFLOW_TEMPLATE_strName"), 0, DALQuery.WHERE_HAS_VALUE);
            }

            // set the status
            if(runtimeData.getParameter("WORKFLOW_TEMPLATE_strStatus") != null &&
               runtimeData.getParameter("WORKFLOW_TEMPLATE_strStatus").length() != 0)
            {
                query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_strStatus", "=",
                               runtimeData.getParameter("WORKFLOW_TEMPLATE_strStatus"), 0, DALQuery.WHERE_HAS_VALUE);
            }

            // checking data validation
            String strDataValidation = QueryChannel.validateData(vecSearchWorkflowTemplateFormFields, runtimeData);

            // if all data is OK, do the search
            if (strDataValidation == null)
            {
                ResultSet rsResultSet = query.executeSelect();

                                // if we can build lock record for the list
                if (buildSearchWorkflowTemplateRecordLock(rsResultSet))
                {
                    strXML = QueryChannel.buildFormLabelXMLFile(vecSearchWorkflowTemplateFormFields) + 
                             QueryChannel.buildViewXMLFile(vecSearchWorkflowTemplateFormFields, runtimeData) +
                             QueryChannel.buildSearchXMLFile("workflow_search_workflow_template_result",
                                                             rsResultSet, vecSearchWorkflowTemplateFormFields);
                }
                // display locking error message
                else
                {
                        strXML = QueryChannel.buildFormLabelXMLFile(vecSearchWorkflowTemplateFormFields) + 
                                 QueryChannel.buildViewXMLFile(vecSearchWorkflowTemplateFormFields, runtimeData);
                        strXML += "<strErrorMessage>" + "Record locking produced some error. Please try again later." +
                                          "</strErrorMessage>";
                }
            }
            // there is some invalid data, display error message
            else
            {
                strXML = QueryChannel.buildFormLabelXMLFile(vecSearchWorkflowTemplateFormFields) + 
                         QueryChannel.buildViewXMLFile(vecSearchWorkflowTemplateFormFields, runtimeData);
                strXML += "<strErrorMessage>" + strDataValidation + "</strErrorMessage>";
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - doSearchWorkflowTemplate" + e.toString(), e);
        }
    }

    /**
     * Build the search workflow instance screen
     */
    private void buildWorkflowSearchWorkflowInstanceScreen()
    {
        strStylesheet = WORKFLOW_SEARCH_WORKFLOW_INSTANCE;
        Vector vecSearchWorkflowInstanceFormFields = DatabaseSchema.getFormFields("search_workflow_instance");

        try
        {
            // clear the current lock
            clearLockRequest();

            strXML = QueryChannel.buildFormLabelXMLFile(vecSearchWorkflowInstanceFormFields) +
                     QueryChannel.buildViewXMLFile(vecSearchWorkflowInstanceFormFields, runtimeData);
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR,
                "Unknown error in Workflow Channel - buildWorkflowSearchWorkflowInstanceScreen" + e.toString(), e);
        }
    }

    /**
     * build the search workflow instance result screen
     *
     * @param runtimeData	the runtimedata
     */
    private void doSearchWorkflowInstance(ChannelRuntimeData runtimeData)
    {
        strStylesheet = WORKFLOW_SEARCH_WORKFLOW_INSTANCE;
        Vector vecSearchWorkflowInstanceFormFields = DatabaseSchema.getFormFields("search_workflow_instance");
        DALQuery query = new DALQuery();

        try
        {
            query.setDomain("WORKFLOW_INSTANCE", null, null, null);
            query.setFields(vecSearchWorkflowInstanceFormFields, null);
            query.setWhere(null, 0, "WORKFLOW_INSTANCE_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            // set the instance key 
            if(runtimeData.getParameter("WORKFLOW_INSTANCE_intWorkflowInstanceKey") != null &&
               runtimeData.getParameter("WORKFLOW_INSTANCE_intWorkflowInstanceKey").length() != 0)
            {
                query.setWhere("AND", 0, "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "=",
                               runtimeData.getParameter("WORKFLOW_INSTANCE_intWorkflowInstanceKey"), 0, DALQuery.WHERE_HAS_VALUE);
            }

            // set the name 
            if(runtimeData.getParameter("WORKFLOW_INSTANCE_strName") != null &&
               runtimeData.getParameter("WORKFLOW_INSTANCE_strName").length() != 0)
            {
                query.setWhere("AND", 0, "WORKFLOW_INSTANCE_strName", "LIKE",
                               runtimeData.getParameter("WORKFLOW_INSTANCE_strName"), 0, DALQuery.WHERE_HAS_VALUE);
            }

            // set the status
            if(runtimeData.getParameter("WORKFLOW_INSTANCE_strStatus") != null &&
               runtimeData.getParameter("WORKFLOW_INSTANCE_strStatus").length() != 0)
            {
                query.setWhere("AND", 0, "WORKFLOW_INSTANCE_strStatus", "=",
                               runtimeData.getParameter("WORKFLOW_INSTANCE_strStatus"), 0, DALQuery.WHERE_HAS_VALUE);
            }

                        // if user does not specify created date, pass a null value through runtime data
            if(runtimeData.getParameter("WORKFLOW_INSTANCE_strDateCreatedOperator") == null ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_strDateCreatedOperator").trim().equals("") ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCreated_Day") == null ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCreated_Day").trim().equals("") ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCreated_Month") == null ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCreated_Month").trim().equals("") ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCreated_Year") == null ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCreated_Year").trim().equals(""))
            {
                runtimeData.setParameter("WORKFLOW_INSTANCE_dtDateCreated", (String) null);
            }
            // else set the condition
            else
            {
                runtimeData.setParameter("WORKFLOW_INSTANCE_dtDateCreated",
                                         QueryChannel.makeDateFromForm("WORKFLOW_INSTANCE_dtDateCreated", runtimeData));
                query.setWhere("AND", 0, "WORKFLOW_INSTANCE_dtDateCreated",
                               runtimeData.getParameter("WORKFLOW_INSTANCE_strDateCreatedOperator"),
                               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCreated"), 0, DALQuery.WHERE_HAS_VALUE);
            }

                        // if user does not specify completed date, pass a null value through runtime data
            if(runtimeData.getParameter("WORKFLOW_INSTANCE_strDateCompletedOperator") == null ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_strDateCompletedOperator").trim().equals("") ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCompleted_Day") == null ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCompleted_Day").trim().equals("") ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCompleted_Month") == null ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCompleted_Month").trim().equals("") ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCompleted_Year") == null ||
               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCompleted_Year").trim().equals(""))
            {
                runtimeData.setParameter("WORKFLOW_INSTANCE_dtDateCompleted", (String) null);
            }
            // else set the condition
            else
            {
                runtimeData.setParameter("WORKFLOW_INSTANCE_dtDateCompleted",
                                         QueryChannel.makeDateFromForm("WORKFLOW_INSTANCE_dtDateCompleted", runtimeData));
                query.setWhere("AND", 0, "WORKFLOW_INSTANCE_dtDateCompleted",
                               runtimeData.getParameter("WORKFLOW_INSTANCE_strDateCompletedOperator"),
                               runtimeData.getParameter("WORKFLOW_INSTANCE_dtDateCompleted"), 0, DALQuery.WHERE_HAS_VALUE);
            }

            // checking data validation
            String strDataValidation = QueryChannel.validateData(vecSearchWorkflowInstanceFormFields, runtimeData);

            // if all data is OK, do the search
            if(strDataValidation == null)
            {
                ResultSet rsResultSet = query.executeSelect();

                // if we can build lock record for the list
                if(buildSearchWorkflowInstanceRecordLock(rsResultSet))
                {
                    strXML = QueryChannel.buildFormLabelXMLFile(vecSearchWorkflowInstanceFormFields) + 
                             QueryChannel.buildViewXMLFile(vecSearchWorkflowInstanceFormFields, runtimeData) +
                             QueryChannel.buildSearchXMLFile("workflow_search_workflow_instance_result",
                                                             rsResultSet, vecSearchWorkflowInstanceFormFields);
                }
                // display locking error message
                else
                {
                    strXML = QueryChannel.buildFormLabelXMLFile(vecSearchWorkflowInstanceFormFields) + 
                    QueryChannel.buildViewXMLFile(vecSearchWorkflowInstanceFormFields, runtimeData);
                    strXML += "<strErrorMessage>" + "Record locking produced some error. Please try again later." +
                                          "</strErrorMessage>";
                }

            }
            // there is some invalid data, display error message
            else
            {
                strXML = QueryChannel.buildFormLabelXMLFile(vecSearchWorkflowInstanceFormFields) + 
                         QueryChannel.buildViewXMLFile(vecSearchWorkflowInstanceFormFields, runtimeData);
                strXML += "<strErrorMessage>" + strDataValidation + "</strErrorMessage>";
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - doSearchWorkflowInstance" + e.toString(), e);
        }
    }

    /**
     * build workflow instantiation screen
     *
     * @param runtimeData	the runtimedata
     */
    private void buildWorkflowInstantiationScreen(ChannelRuntimeData runtimeData)
    {
        strStylesheet = WORKFLOW_SEARCH_WORKFLOW_TEMPLATE;
        Vector vecWorkflowTemplateParameterFormFields = DatabaseSchema.getFormFields("workflow_template_parameter_details");
        DALQuery query = new DALQuery();

        try
        {
            query.setDomain("WORKFLOW_TEMPLATE_PARAMETER", null, null, null);
            query.setFields(vecWorkflowTemplateParameterFormFields, null);
            query.setWhere(null, 0, "WORKFLOW_TEMPLATE_PARAMETER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_PARAMETER_intWorkflowTemplateKey", "=",
                           runtimeData.getParameter("WORKFLOW_TEMPLATE_intWorkflowTemplateKey"), 0, DALQuery.WHERE_HAS_VALUE);

            // checking data validation
            String strDataValidation = QueryChannel.validateData(vecWorkflowTemplateParameterFormFields, runtimeData);

            // if all data is OK, do the search
            if (strDataValidation == null)
            {
                ResultSet rsResultSet = query.executeSelect();

                //strXML = QueryChannel.buildFormLabelXMLFile(vecWorkflowTemplateParameterFormFields) + 
                strXML=  QueryChannel.buildViewXMLFile(vecWorkflowTemplateParameterFormFields, runtimeData) +
                         QueryChannel.buildSearchXMLFileWithDateType("workflow_template_parameter_result",
                                                                     rsResultSet, vecWorkflowTemplateParameterFormFields);

            }
            // there is some invalid data, display error message
            else
            {
                //strXML = QueryChannel.buildFormLabelXMLFile(vecWorkflowTemplateParameterFormFields) + 
                strXML = QueryChannel.buildViewXMLFile(vecWorkflowTemplateParameterFormFields, runtimeData);
                strXML += "<strErrorMessage>" + strDataValidation + "</strErrorMessage>";
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - buildWorkflowInstantiationScreen" + e.toString(), e);
        }
    }

    /**
     * construct correct runtimedata for date type field, applicable for workflow_templete_parameter domain ONLY 
     *
     * @param runtimeData	the runtimedata
     * @param query			the query object
     *
     * @return true if the construction is successful, else return false
     */
    public boolean constructDateRuntimedata(ChannelRuntimeData runtimeData, DALSecurityQuery query)
    {
        boolean blReturnValue = true;

        try
        {
            String strWorkflowTemplateKey =
                runtimeData.getParameter("WORKFLOW_TEMPLATE_intWorkflowTemplateKey");

            Vector vecWorkflowTemplateParameterFormFields = 
                DatabaseSchema.getFormFields("workflow_template_parameter_details");

            query.reset();

            // get the workflow template parameter
            query.setDomain("WORKFLOW_TEMPLATE_PARAMETER", null, null, null);
            query.setFields(vecWorkflowTemplateParameterFormFields, null);
            query.setWhere(null, 0, "WORKFLOW_TEMPLATE_PARAMETER_intDeleted", "=", "0",
                           0, DALQuery.WHERE_HAS_VALUE);
            query.setWhere("AND", 0, "WORKFLOW_TEMPLATE_PARAMETER_intWorkflowTemplateKey", "=",
                           strWorkflowTemplateKey, 0, DALQuery.WHERE_HAS_VALUE);

            // checking data validation
            String strDataValidation = QueryChannel.validateData(vecWorkflowTemplateParameterFormFields, runtimeData);

            // if all data is OK, do the search
            if (strDataValidation == null)
            {
                ResultSet rsResultSet = query.executeSelect();

                // get the field name only when the type is a "Date"
                Vector vecDateFieldName = new Vector();
                int intNoOfFields = vecWorkflowTemplateParameterFormFields.size();
                String strDateFieldName = null;
                while (rsResultSet.next())
                {
                    for (int i=0; i < intNoOfFields; i++)
                    {
                        String strFieldName = (String) vecWorkflowTemplateParameterFormFields.get(i);

                        if(strFieldName.endsWith("strName"))
                        {
                            if(rsResultSet.getString(strFieldName) != null)
                            {
                                    strDateFieldName = rsResultSet.getString(strFieldName);
                            }
                        }

                        if(strFieldName.endsWith("strType"))
                        {
                            if(rsResultSet.getString(strFieldName) != null)
                            {
                                if(rsResultSet.getString(strFieldName).equals("Date"))
                                {
                                        vecDateFieldName.add(strDateFieldName);
                                }
                            }
                        }
                    }
                }

                //System.err.println("vecDateFieldName = " + vecDateFieldName);

                Enumeration enumDateFieldName = vecDateFieldName.elements();
                while(enumDateFieldName.hasMoreElements())
                {
                    strDateFieldName = (String) enumDateFieldName.nextElement();

                    // if user does not specify received date, pass a null value through runtime data
                    if (runtimeData.getParameter(strDateFieldName + "_Day") == null ||
                        runtimeData.getParameter(strDateFieldName + "_Day").trim().equals("") ||
                        runtimeData.getParameter(strDateFieldName + "_Month") == null ||
                        runtimeData.getParameter(strDateFieldName + "_Month").trim().equals("") ||
                        runtimeData.getParameter(strDateFieldName + "_Year") == null ||
                        runtimeData.getParameter(strDateFieldName + "_Year").trim().equals(""))
                    {
                        runtimeData.setParameter(strDateFieldName, (String) null);
                    }
                    // else set the condition
                    else
                    {
                        runtimeData.setParameter(strDateFieldName,
                                                QueryChannel.makeDateFromForm(strDateFieldName, runtimeData));
                    }
                }
            }
            // there is some invalid data, display error message
            else
            {
                buildWorkflowInstantiationScreen(runtimeData);
                strXML += "<prepare_runtimedata>true</prepare_runtimedata>";
                strXML += "<source>" + runtimeData.getParameter("source") + "</source>";
                strXML += "<WORKFLOW_TEMPLATE_intWorkflowTemplateKey>" + strWorkflowTemplateKey +
                                  "</WORKFLOW_TEMPLATE_intWorkflowTemplateKey>";

                blReturnValue = false;
            }
        }
        catch (Exception e)
        {
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - constructDateRuntimedata" + e.toString(), e);
        }

        return blReturnValue;
    }


    // Locking Methods
    
    private boolean buildWorkflowManageFileLock(ResultSet rsResultSet)
    {
        try
        {
            // clear the current lock
            clearLockRequest();
            
            // create new LockRequest object
            lockRequest = new LockRequest(authToken);
            
            while (rsResultSet.next())
            {
                lockRequest.addLock(WorkflowManager.WORKFLOW_FILE, 
                    rsResultSet.getString("WORKFLOW_FILE_intWorkflowFileKey"), LockRecord.READ_WRITE);
            }
            rsResultSet.beforeFirst();
            
            return lockRequest.lockDelayWrite();
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - buildWorkflowManageSystemTaskLock" + e.toString(), e);
        }
        
        return false;
    }
    
    /**
     * Build record locking for manage workflow system task list
     *
     * @param rsResultSet	the result set
     *
     * @return an indicator whether the lock is successful or not
     */
    private boolean buildWorkflowManageSystemTaskLock(ResultSet rsResultSet)
    {
        try
        {
            // clear the current lock
            clearLockRequest();
            
            // create new LockRequest object
            lockRequest = new LockRequest(authToken);
            
            while (rsResultSet.next())
            {
                lockRequest.addLock(WorkflowManager.WORKFLOW_FUNCTION, 
                    rsResultSet.getString("WORKFLOW_FUNCTION_intWorkflowFunctionKey"), LockRecord.READ_WRITE);
            }
            rsResultSet.beforeFirst();
            
            return lockRequest.lockDelayWrite();
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - buildWorkflowManageSystemTaskLock" + e.toString(), e);
        }
        
        return false;
    }
    
    /**
     * Build record locking for search workflow template list
     *
     * @param rsResultSet	the result set
     *
     * @return an indicator whether the lock is successful or not
     */
    private boolean buildSearchWorkflowTemplateRecordLock(ResultSet rsResultSet)
    {
        try
        {
            // clear the current lock
            clearLockRequest();
            
            // create new LockRequest object
            lockRequest = new LockRequest(authToken);
            
            while (rsResultSet.next())
            {
                lockRequest.addLock(WorkflowManager.WORKFLOW_PROCESS, 
                    rsResultSet.getString("WORKFLOW_TEMPLATE_intWorkflowTemplateKey"), LockRecord.READ_WRITE);
            }
            rsResultSet.beforeFirst();
            
            return lockRequest.lockDelayWrite();
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - buildSearchWorkflowTemplateRecordLock" + e.toString(), e);
        }
        
        return false;
    }

    /**
     * Build record locking for search workflow instance list
     *
     * @param rsResultSet	the result set
     *
     * @return an indicator whether the lock is successful or not
     */
    private boolean buildSearchWorkflowInstanceRecordLock(ResultSet rsResultSet)
    {
        try
        {
            // clear the current lock
            clearLockRequest();
            
            // create new LockRequest object
            lockRequest = new LockRequest(authToken);
            
            while (rsResultSet.next())
            {
                lockRequest.addLock(WorkflowManager.WORKFLOW_INSTANCE, 
                    rsResultSet.getString("WORKFLOW_INSTANCE_intWorkflowInstanceKey"), LockRecord.READ_WRITE);
            }
            rsResultSet.beforeFirst();
            
            return lockRequest.lockDelayWrite();
        }
        catch(Exception e)
        {
            LogService.instance().log(LogService.ERROR, 
                "Unknown error in Workflow Channel - buildSearchWorkflowInstanceRecordLock" + e.toString(), e);
        }
        
        return false;
    }

    /**
     *  Print out message if user doesn't have the autority to do a specific activity
     *
     *  @param aFunctionalArea	 the functional area
     *
     *  @return the error message
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
}
