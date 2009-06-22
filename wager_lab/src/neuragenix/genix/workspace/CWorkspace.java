/**

 * CWorkspace.java

 * Copyright ? 2004 Neuragenix, Inc .  All rights reserved.

 * Date: 25/02/2004

 */



package neuragenix.genix.workspace;



/**

 * Workspace channel

 * @author <a href="mailto:hhoang@neuragenix.com">Huy Hoang</a>

 */



// java packages

import java.util.Vector;

import java.util.Hashtable;

import java.util.Enumeration;

import java.io.*;

import java.sql.*;

import java.util.Calendar;



// javax packages

import javax.naming.Context;

import javax.naming.NamingException;

import javax.naming.NotContextException;

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



// neuragenix packages

import neuragenix.security.AuthToken;

import neuragenix.dao.*;

import neuragenix.utils.Password;



import neuragenix.common.*;

import neuragenix.genix.workflow.*;



import neuragenix.utils.WorkflowTaskCompletionThread;











public class CWorkspace implements IChannel

{

    

    // pages & activities

    private final String SEARCH_TASK = "search_task";

    private final String VIEW_TASK = "view_task";

    private final String VIEW_TASK_STATUS = "view_task_status";

    private final String VIEW_TASK_HISTORY = "view_task_history";

    private final String VIEW_ACTIVE_TASK = "view_active_task";

    private final String REASSIGN_TASKS = "reassign_tasks";

    private final String REASSIGN_TASK = "reassign_task";

    private final String CREATE_ALERT = "create_alert";

    private final String VIEW_ALERT = "view_alert";

    private final String ALERT_LIST = "alert_list";

    private final String ORG_CHART = "org_chart";

    private final String SECURITY = "security";

    private final String WORKSPACE_REASSIGN_TASK = "workspace_reassign_task";

    private final String WORKSPACE_REASSIGN_TASKS = "workspace_reassign_tasks";

    private final String WORKSPACE_CREATE_ALERT = "workspace_create_alert";

    private final String WORKSPACE_VIEW_ALERT = "workspace_view_alert";

    private final String WORKSPACE_UPDATE_ALERT = "workspace_update_alert";

    private final String WORKSPACE_DELETE_ALERT = "workspace_delete_alert";

    private final String WORKSPACE_SEARCH_TASK = "workspace_search_task";

    private final String WORKSPACE_VIEW_TASK = "workspace_view_task";

    private final String WORKSPACE_UPDATE_TASK = "workspace_update_task";

    private final String WORKSPACE_COMPLETE_TASK = "workspace_complete_task";

    

    // constants used in this channel

    private final String ALERT_SENT = "Sent";

    private final String ALERT_NOT_SENT = "Not sent";

    private final String MARK_ALERT_AS_READ = "Read";

    

    // channel's runtime & static data

    private ChannelStaticData staticData;

    private ChannelRuntimeData runtimeData;



    

    private AuthToken authToken ;

    private String strSessionUniqueID;

    private String strActivity;

    private String strCurrent;

    private String strXML = "";

    private String strCurrentXML = "";

    private String strPreviousXML = "";

    private String strStylesheet;

    private String strCurrentStylesheet = "";

    private String strPreviousStylesheet;

    private boolean firstTime = false;

    private String strErrorMessage = "";

    

    /** Lock request object to handle record locking

     */

    private LockRequest lockRequest = null;

    

    private Hashtable hashExpanded = new Hashtable(50);

    private Hashtable hsOrder = new Hashtable(2, 5);
    private Hashtable hsSearchOrder = new Hashtable(2, 5);

    private Vector vtOrder = new Vector( 5, 2 );
    private Vector vtSearchOrder = new Vector( 5, 2 );

    private DALSecurityQuery searchTaskQuery;
    

    private ChannelRuntimeData oldRuntimeData;

    

    //private Vector vtUserNames = new Vector(5, 2);

    

    private Hashtable hashUsers;

    

    private Hashtable hashUserGroups;

    

    // flag to tell if the user has incoming alerts

    private boolean isHasIncomingAlert = true;

    

    /** Channels don't need to clear lock

     */

    private Hashtable hashChannelsNotToClearLock = null;

    

    
    /** Contructs the Workspace Channel

     */

    public CWorkspace()

    {

        this.strStylesheet = VIEW_ACTIVE_TASK;

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

        firstTime = true;

        this.staticData = sd;

        this.authToken = (AuthToken)sd.getPerson().getAttribute("AuthToken");

        

        // create domains & formfields for this channel

        InputStream file = CWorkspace.class.getResourceAsStream("WorkspaceDBSchema.xml");

        DatabaseSchema.loadDomains(file, "WorkspaceDBSchema.xml");

        

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

            SessionManager.addChannelID(strSessionUniqueID, "CCase", (String) globalIDContext.lookup("CCase"));

            

        } 

        catch (NotContextException nce)

        {

            LogService.log(LogService.ERROR, "Could not find channel ID for fname=CCase");

        } 

        catch (NamingException e) 

        {

            LogService.log(LogService.ERROR, e);

        }

        

        try

        {

            //strSessionUniqueID = authToken.getSessionUniqueID();

            //SessionManager.addSession(strSessionUniqueID);

            SessionManager.addChannelID(strSessionUniqueID, "CSmartform", (String) globalIDContext.lookup("CSmartform"));

            

        } 

        catch (NotContextException nce)

        {

            LogService.log(LogService.ERROR, "Could not find channel ID for fname=CSmartform");

        } 

        catch (NamingException e) 

        {

            LogService.log(LogService.ERROR, e);

        }

        

        // instantiate the hashChannelNotToClearLock

        hashChannelsNotToClearLock = SessionManager.getSharedChannels(authToken, "CWorkspace");

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

            isHasIncomingAlert = isHasIncomingAlerts();

            

            

            // Get the current page the users is on

            if(runtimeData.getParameter("current") != null)

            {

                firstTime = false;

                strCurrent = runtimeData.getParameter("current");

            }

            // first time to channel

            else if (firstTime)

            {

                SessionManager.clearLockRequests(strSessionUniqueID, hashChannelsNotToClearLock);

                //LockRequest.emptyAllLocks(authToken.getSessionUniqueID());

                firstTime = false;

                strCurrent = VIEW_ACTIVE_TASK;

                runtimeData.setParameter("current",strCurrent);

            }

            // go to the channel from another tab

            else

            {

                SessionManager.clearLockRequests(strSessionUniqueID, hashChannelsNotToClearLock);

//                LockRequest.emptyAllLocks(authToken.getSessionUniqueID());

                firstTime = false;

                if(this.strPreviousXML != null && this.oldRuntimeData != null && this.oldRuntimeData.getParameter("done") == null)

                {

                    clearLockRequest();

                    

                    for(Enumeration eord = this.oldRuntimeData.getParameterNames();eord.hasMoreElements();)

                    {

                        String key = (String)eord.nextElement();

                        this.runtimeData.setParameter(key,this.oldRuntimeData.getParameter(key));

                    }

                    if(runtimeData.getParameter("current").equals("view_task") && runtimeData.getParameter("save") != null)

                    {

                        this.strCurrent = null;

                        strXML = strPreviousXML;

                        strStylesheet = strPreviousStylesheet;

                        buildTaskRecordLock(runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"));

                    }

                    //rennypv...added to build locks after reassigning tasks

                    if(runtimeData.getParameter("current").equals(REASSIGN_TASK))

                    {

                        String taskkey = runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_intTaskKey");

                        buildTaskRecordLock(taskkey);

                    }

                }

                else

                {

                    strCurrent = VIEW_ACTIVE_TASK;

                }

            }

            

            if (strCurrent != null)

            {

                // user is on the search task page

                if (strCurrent.equals(SEARCH_TASK))

                {

                    // if the user doesn't have permission to search task

                    // display unauthorization message

                    if (!authToken.hasActivity(WORKSPACE_SEARCH_TASK))

                    {

                        strXML  = buildSecurityErrorXMLFile("search task");

                        strStylesheet = SECURITY;

                        return;

                    }

                    // user click the search button to search tasks

                    if ((((runtimeData.getParameter("search") != null) && (runtimeData.getParameter("search").length() > 0)) 
                        || runtimeData.getParameter("sort") != null) 
                        || (runtimeData.getParameter("naviSearch") != null))

                    {

                        // if the user doesn't have permission to search task

                        // display unauthorization message

                        if (!authToken.hasActivity(WORKSPACE_SEARCH_TASK))

                        {

                            strXML  = buildSecurityErrorXMLFile("search task");

                            strStylesheet = SECURITY;

                            return;

                        }

                        doSearchTask();

                        oldRuntimeData = runtimeData;

                    }

                    else

                    {

                        buildSearchTaskForm();

                    }

                }

                // user is on the view task page

                else if (strCurrent.equals(VIEW_TASK))

                {

                    // if the user doesn't have permission to view task

                    // display unauthorization message

                    if (!authToken.hasActivity(WORKSPACE_VIEW_TASK))

                    {

                        strXML  = buildSecurityErrorXMLFile("view task");

                        strStylesheet = SECURITY;

                        return;

                    }

                    

                    // user click the save button to update the task

                    if (runtimeData.getParameter("save") != null)

                    {

                        // if the user doesn't have permission to update task

                        // display unauthorization message

                        if (!authToken.hasActivity(WORKSPACE_UPDATE_TASK))

                        {

                            strXML  = buildSecurityErrorXMLFile("update task");

                            strStylesheet = SECURITY;

                            return;

                        }

                    

                        doUpdateTask();

                    }

                    // user click the done button to complete the task

                    else if (runtimeData.getParameter("done") != null)

                    {

                        // if the user doesn't have permission to complete task

                        // display unauthorization message

                        if (!authToken.hasActivity(WORKSPACE_COMPLETE_TASK))

                        {

                            strXML  = buildSecurityErrorXMLFile("complete task");

                            strStylesheet = SECURITY;

                            return;

                        }

                        

                        doCompleteTask();

                    }

                    // user click the reassign button to reassign the task

                    else if (runtimeData.getParameter("reassign") != null)

                    {

                        // if the user doesn't have permission to reassign task

                        // display unauthorization message

                        if (!authToken.hasActivity(WORKSPACE_REASSIGN_TASK))

                        {

                            strXML  = buildSecurityErrorXMLFile("reassign task");

                            strStylesheet = SECURITY;

                            return;

                        }

                        

                        runtimeData.setParameter("WORKFLOW_TASK_REASSIGN_intTaskKey", runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"));

                        buildReassignTaskForm();

                    }

                    // user click the back button to go to the previous page

                    else if (runtimeData.getParameter("back") != null)

                    {

                        ChannelRuntimeData currentRuntimeData = runtimeData;

                        runtimeData = oldRuntimeData;

                        

                        //System.err.println(runtimeData.getParameter("source"));

                        //rennypv

                        clearLockRequest();

                        if (currentRuntimeData.getParameter("source").equals("search_task"))

                        {

                            // if the user doesn't have permission to search task

                            // display unauthorization message

                            if (!authToken.hasActivity(WORKSPACE_SEARCH_TASK))

                            {

                                strXML  = buildSecurityErrorXMLFile("search task");

                                strStylesheet = SECURITY;

                                return;

                            }

                            

                            doSearchTask();

                        }

                        else

                        {

                            // if the user doesn't have permission to search task

                            // display unauthorization message

                            if (!authToken.hasActivity(WORKSPACE_SEARCH_TASK))

                            {

                                strXML  = buildSecurityErrorXMLFile("search task");

                                strStylesheet = SECURITY;

                                return;

                            }

                            

                            buildActiveTaskListForm();

                        }

                        

                        runtimeData = currentRuntimeData;

                    }

                    // user click the view status button to view the workflow status

                    else if (runtimeData.getParameter("view_status") != null)

                    {

                        

                        // if the user doesn't have permission to view task

                        // display unauthorization message

                        if (!authToken.hasActivity(WORKSPACE_VIEW_TASK))

                        {

                            strXML  = buildSecurityErrorXMLFile("view task");

                            strStylesheet = SECURITY;

                            return;

                        }

                        

                        buildViewTaskStatus();

                    }

                    else

                    {

                        // if the user doesn't have permission to view task

                        // display unauthorization message

                        if (!authToken.hasActivity(WORKSPACE_VIEW_TASK))

                        {

                            strXML  = buildSecurityErrorXMLFile("view task");

                            strStylesheet = SECURITY;

                            return;

                        }

                        

                        buildViewTaskForm();

                    }

                }

                // user is on the view task status page

                else if (strCurrent.equals(VIEW_TASK_STATUS))

                {

                    // user click the Back button to go back to view task

                    if (runtimeData.getParameter("back") != null)

                    {

                        // if the user doesn't have permission to view task

                        // display unauthorization message

                        if (!authToken.hasActivity(WORKSPACE_VIEW_TASK))

                        {

                            strXML  = buildSecurityErrorXMLFile("view task");

                            strStylesheet = SECURITY;

                            return;

                        }

                        

                        buildViewTaskForm();

                    }

                }

                // user is on the view active tasks page

                else if (strCurrent.equals(VIEW_ACTIVE_TASK))

                {

                    

                    // if the user doesn't have permission to view task

                        // display unauthorization message

                    if (!authToken.hasActivity(WORKSPACE_VIEW_TASK))

                    {

                        strXML  = buildSecurityErrorXMLFile("view task");

                        strStylesheet = SECURITY;

                        return;

                    }

                    clearLockRequest();

                    oldRuntimeData = runtimeData;

                    

                    

                    buildActiveTaskListForm();

                }

                // user is on the reassign all tasks page

                else if (strCurrent.equals(REASSIGN_TASKS))

                {

                    // if the user doesn't have permission to reassign task

                    // display unauthorization message

                    if (!authToken.hasActivity(WORKSPACE_REASSIGN_TASKS))

                    {

                        strXML  = buildSecurityErrorXMLFile("reassign tasks");

                        strStylesheet = SECURITY;

                        return;

                    }

                    

                    // user clicked on the Org Chart button to go to Org Chart tree

                    // and select the user whose jobs will be reassigned

                    if (runtimeData.getParameter("org_chart_from") != null)

                    {

                        runtimeData.setParameter("WORKFLOW_TASK_REASSIGN_dtFromDate", QueryChannel.makeDateFromForm("WORKFLOW_TASK_REASSIGN_dtFromDate", runtimeData));

                        runtimeData.setParameter("WORKFLOW_TASK_REASSIGN_dtToDate", QueryChannel.makeDateFromForm("WORKFLOW_TASK_REASSIGN_dtToDate", runtimeData));

                        // keep the runtime data for rebuilding the form later

                        oldRuntimeData = runtimeData;

                        buildOrgChartForm(REASSIGN_TASKS, "WORKFLOW_TASK_REASSIGN_strFromUserName");

                    }

                    // user clicked on the Org Chart button to go to Org Chart tree

                    // and select the user who will be assigned jobs

                    else if (runtimeData.getParameter("org_chart_to") != null)

                    {

                        runtimeData.setParameter("WORKFLOW_TASK_REASSIGN_dtFromDate", QueryChannel.makeDateFromForm("WORKFLOW_TASK_REASSIGN_dtFromDate", runtimeData));

                        runtimeData.setParameter("WORKFLOW_TASK_REASSIGN_dtToDate", QueryChannel.makeDateFromForm("WORKFLOW_TASK_REASSIGN_dtToDate", runtimeData));

                        // keep the runtime data for rebuilding the form later

                        oldRuntimeData = runtimeData;

                        buildOrgChartForm(REASSIGN_TASKS, "WORKFLOW_TASK_REASSIGN_strToUserName");

                    }

                    // user clicked on the Save button to save the reassign task records

                    else if (runtimeData.getParameter("save") != null)

                    {

                        doSaveReassignTasks();

                    }

                    // user clicked on the Delete button to delete the reassign task records

                    else if (runtimeData.getParameter("delete") != null)

                    {

                        doDeleteReassignTask();

                    }

                    else

                        buildReassignTasksForm();

   

                }

                // user is on the reassign task page

                else if (strCurrent.equals(REASSIGN_TASK))

                {

                    // if the user doesn't have permission to reassign task

                    // display unauthorization message

                    if (!authToken.hasActivity(WORKSPACE_REASSIGN_TASK))

                    {

                        strXML  = buildSecurityErrorXMLFile("reassign task");

                        strStylesheet = SECURITY;

                        return;

                    }

                    

                    // user clicked on the Org Chart button to go to Org Chart tree

                    if (runtimeData.getParameter("org_chart") != null)

                    {

                        // keep the runtime data for rebuilding the form later

                        oldRuntimeData = runtimeData;

                        buildOrgChartForm(REASSIGN_TASK, "WORKFLOW_TASK_REASSIGN_strToUserName");

                    }

                    // user clicked on the Save button to save the reassign task record

                    else if (runtimeData.getParameter("save") != null)

                    {

                        doSaveReassignTask();

                    }

                    // user clicked on the Back button to go back to view task

                    else if (runtimeData.getParameter("back") != null)

                    {

                        runtimeData.setParameter("WORKFLOW_TASK_intTaskKey", runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_intTaskKey"));

                        buildViewTaskForm();

                    }

                    else

                    {

                        buildReassignTaskForm();

                    }

                }

                // user is on the create alert page

                else if (strCurrent.equals(CREATE_ALERT))

                {

                    // if the user doesn't have permission to create alert

                    // display unauthorization message

                    if (!authToken.hasActivity(WORKSPACE_CREATE_ALERT))

                    {

                        strXML  = buildSecurityErrorXMLFile("create alert");

                        strStylesheet = SECURITY;

                        return;

                    }

                    

                    // user click on the Save button to save the alert

                    if (runtimeData.getParameter("save") != null)

                        doSaveAlert();

                    // user clicked on the Org Chart button to go to Org Chart tree

                    else if (runtimeData.getParameter("org_chart") != null)

                    {

                        // set date & time

                        runtimeData.setParameter("WORKSPACE_ALERT_dtDate", QueryChannel.makeDateFromForm("WORKSPACE_ALERT_dtDate", runtimeData));

                        runtimeData.setParameter("WORKSPACE_ALERT_tmTime", QueryChannel.makeTimeFromForm("WORKSPACE_ALERT_tmTime", runtimeData));

                        // keep the runtime data for rebuilding the form later

                        oldRuntimeData = runtimeData;

                        buildOrgChartForm(CREATE_ALERT, "WORKSPACE_ALERT_strSendToUserName");

                    }

                    else

                        buildCreateAlertForm();

                }

                // user is on the view alert page

                else if (strCurrent.equals(VIEW_ALERT))

                {

                    // if the user doesn't have permission to view alert

                    // display unauthorization message

                    if (!authToken.hasActivity(WORKSPACE_VIEW_ALERT))

                    {

                        strXML  = buildSecurityErrorXMLFile("view alert");

                        strStylesheet = SECURITY;

                        return;

                    }

                    

                    // user click on the Save button to update the alert

                    if (runtimeData.getParameter("save") != null)

                    {

                        // if the user doesn't have permission to update alert

                        // display unauthorization message

                        if (!authToken.hasActivity(WORKSPACE_UPDATE_ALERT))

                        {

                            strXML  = buildSecurityErrorXMLFile("update alert");

                            strStylesheet = SECURITY;

                            return;

                        }

                        doUpdateAlert();

                    }

                    // user click on the Delete button to delete the alert

                    else if (runtimeData.getParameter("delete") != null)

                    {

                        // if the user doesn't have permission to update alert

                        // display unauthorization message

                        if (!authToken.hasActivity(WORKSPACE_DELETE_ALERT))

                        {

                            strXML  = buildSecurityErrorXMLFile("delete alert");

                            strStylesheet = SECURITY;

                            return;

                        }

                        doDeleteAlert();

                    }

                    // user clicked on the Org Chart button to go to Org Chart tree

                    else if (runtimeData.getParameter("org_chart") != null)

                    {

                        // set date & time

                        runtimeData.setParameter("WORKSPACE_ALERT_dtDate", QueryChannel.makeDateFromForm("WORKSPACE_ALERT_dtDate", runtimeData));

                        runtimeData.setParameter("WORKSPACE_ALERT_tmTime", QueryChannel.makeTimeFromForm("WORKSPACE_ALERT_tmTime", runtimeData));

                        // keep the runtime data for rebuilding the form later

                        oldRuntimeData = runtimeData;

                        buildOrgChartForm(VIEW_ALERT, "WORKSPACE_ALERT_strSendToUserName");

                    }

                    else

                        buildViewAlertForm();

                }

                // user is on the alert list page

                else if (strCurrent.equals(ALERT_LIST))

                {

                    // if the user doesn't have permission to view alert

                    // display unauthorization message

                    if (!authToken.hasActivity(WORKSPACE_VIEW_ALERT))

                    {

                        strXML  = buildSecurityErrorXMLFile("view alert");

                        strStylesheet = SECURITY;

                        return;

                    }

                    // user wants to remove the read alert

                    if (runtimeData.getParameter("remove") != null)

                    {

                        // if the user doesn't have permission to update alert

                        // display unauthorization message

                        if (!authToken.hasActivity(WORKSPACE_DELETE_ALERT))

                        {

                            strXML  = buildSecurityErrorXMLFile("remove alert");

                            strStylesheet = SECURITY;

                            return;

                        }

                        markAlertAsRead();

                    }

                    else if (runtimeData.getParameter("delete") != null)

                    {

                        // if the user doesn't have permission to update alert

                        // display unauthorization message

                        if (!authToken.hasActivity(WORKSPACE_DELETE_ALERT))

                        {

                            strXML  = buildSecurityErrorXMLFile("delete alert");

                            strStylesheet = SECURITY;

                            return;

                        }

                        doDeleteAlert();

                    }

                    

                    buildAlertListForm();

                }

                // user is on the org chart page

                else if (strCurrent.equals(ORG_CHART))

                {

                    if (runtimeData.getParameter("treeAction").equals("collapse"))

                        hashExpanded.remove(runtimeData.getParameter("ORGGROUP_intOrgGroupKey"));

                    else

                        hashExpanded.put(runtimeData.getParameter("ORGGROUP_intOrgGroupKey"), "expanded");

                    

                    buildOrgChartForm(runtimeData.getParameter("source_page"), runtimeData.getParameter("field_name"));

                }

                strPreviousXML = null;

                strPreviousStylesheet = null;

                if(runtimeData.getParameter("treeAction") == null)

                {

                    this.oldRuntimeData = null;

                    this.oldRuntimeData = new ChannelRuntimeData();

                    for(Enumeration erd=this.runtimeData.getParameterNames();erd.hasMoreElements();)

                    {

                        String key = (String)erd.nextElement();

                        String value = this.runtimeData.getParameter(key);

                        this.oldRuntimeData.setParameter(key,value);

                    }

                }

                strPreviousXML = strXML;

                strPreviousStylesheet = strStylesheet;

            }

            else

            {

                strXML = strPreviousXML;

                strStylesheet = strPreviousStylesheet;

            }

            

            

            strXML += "<WORKFLOW_TASK_intWorkflowInstanceKey>"+ this.runtimeData.getParameter("WORKFLOW_TASK_intWorkflowInstanceKey") + "</WORKFLOW_TASK_intWorkflowInstanceKey>";            

            strXML += "<isHasIncomingAlert>" + isHasIncomingAlert + "</isHasIncomingAlert>";

            strXML = "<?xml version=\"1.0\" encoding=\"utf-8\"?><workspace>" + strXML + "</workspace>";

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Build the search task formvtCreateAlertForm

     */

    private void buildSearchTaskForm()

    {

        strStylesheet = SEARCH_TASK;

        Vector vtSearchTaskForm = DatabaseSchema.getFormFields(SEARCH_TASK);

        

        try

        {

            // clear the current lock

            clearLockRequest();

    

            strXML = QueryChannel.buildFormLabelXMLFile(vtSearchTaskForm) + 

                     QueryChannel.buildViewXMLFile(vtSearchTaskForm, runtimeData);

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Perform the search task request

     */

    private void doSearchTask()

    {

        boolean task_sort = false;

        String task_order = "ascending";

        String priority_order = "ASC";

        String DateTime_order = "ASC";

        String DateCompleted_order = "ASC";

        String status_order = "ASC";

        strStylesheet = SEARCH_TASK;

        strXML = "";
        String strDataValidation = null;
        

        Vector vtSearchTaskForm = DatabaseSchema.getFormFields(SEARCH_TASK);

        

        try

        {  
            int noOfPages = 0;
            int currentPage = 1;
            String noOfRecords = PropertiesManager.getProperty("neuragenix.platformgenix.RecordPerPage");


            // get the users and user groups related to the current system user

            getUserAndUserGroupWithReassignTask();

            if( searchTaskQuery == null )
                searchTaskQuery = new DALSecurityQuery(WORKSPACE_SEARCH_TASK, authToken);
            
            if( runtimeData.getParameter( "search") != null ){
                
                searchTaskQuery = null;
                searchTaskQuery = new DALSecurityQuery(WORKSPACE_SEARCH_TASK, authToken);
                
                searchTaskQuery.setDomain("WORKFLOW_TASK", null, null, null);

                searchTaskQuery.setDomain("WORKFLOW_INSTANCE", "WORKFLOW_TASK_intWorkflowInstanceKey", "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "INNER JOIN");

                searchTaskQuery.setFields(vtSearchTaskForm, null);

                searchTaskQuery.setWhere(null, 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

                searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_strType", "=", "Human", 0, DALQuery.WHERE_HAS_VALUE);

                searchTaskQuery.setWhere("AND", 0, "WORKFLOW_INSTANCE_strStatus", "=", "In progress", 0, DALQuery.WHERE_HAS_VALUE);

                //searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_dtDateReceived", "<>", "", 0, DALQuery.WHERE_HAS_VALUE);

                searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_dtDateReceived", "IS NOT NULL", "IS NOT NULL", 0, DALQuery.WHERE_HAS_NULL_VALUE);

                //searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_strType", "<>", "Stop", 0, DALQuery.WHERE_HAS_VALUE);

                /*
                if(runtimeData.getParameter("sort") != null)

                {

                    if(runtimeData.getParameter("type") != null && runtimeData.getParameter("type").equals("Task"))

                    {

                        if(runtimeData.getParameter("order").equals("ascending"))

                        {

                            task_order = "descending";

                        }

                        else

                        {

                            task_order = "ascending";

                        }

                        task_sort = true;

                    }

                    if(runtimeData.getParameter("type") != null && runtimeData.getParameter("type").equals("Priority"))

                    {

                        searchTaskQuery.setOrderBy("WORKFLOW_TASK_strPriority", runtimeData.getParameter("order"));

                        if(runtimeData.getParameter("order").equals("ASC"))

                        {

                            priority_order = "DESC";

                        }

                        else

                        {

                            priority_order = "ASC";

                        }

                    }

                    if(runtimeData.getParameter("type") != null && runtimeData.getParameter("type").equals("DateTime"))

                    {

                        searchTaskQuery.setOrderBy("WORKFLOW_TASK_dtDateReceived", runtimeData.getParameter("order"));

                        if(runtimeData.getParameter("order").equals("ASC"))

                        {

                            DateTime_order = "DESC";

                        }

                        else

                        {

                            DateTime_order = "ASC";

                        }

                    }

                    if(runtimeData.getParameter("type") != null && runtimeData.getParameter("type").equals("DateCompleted"))

                    {

                        searchTaskQuery.setOrderBy("WORKFLOW_TASK_dtDateCompleted", runtimeData.getParameter("order"));

                        if(runtimeData.getParameter("order").equals("ASC"))

                        {

                            DateCompleted_order = "DESC";

                        }

                        else

                        {

                            DateCompleted_order = "ASC";

                        }

                    }

                    if(runtimeData.getParameter("type") != null && runtimeData.getParameter("type").equals("Status"))

                    {

                        searchTaskQuery.setOrderBy("WORKFLOW_TASK_strStatus", runtimeData.getParameter("order"));

                        if(runtimeData.getParameter("order").equals("ASC"))

                        {

                            status_order = "DESC";

                        }

                        else

                        {

                            status_order = "ASC";

                        }

                    }

                    for(Enumeration eord = this.oldRuntimeData.getParameterNames();eord.hasMoreElements();)

                    {

                        String key = (String)eord.nextElement();

                        this.runtimeData.setParameter(key,this.oldRuntimeData.getParameter(key));

                    }

                }

                else

                {

                    searchTaskQuery.setOrderBy("WORKFLOW_TASK_dtDateReceived", "ASC");

                    searchTaskQuery.setOrderBy("WORKFLOW_TASK_strTimeReceived", "ASC");

                }
                */



                Enumeration enumUsers = hashUsers.keys();

                Enumeration enumUserGroups = hashUserGroups.keys();

                boolean isFirstUser = true;

                boolean isFirstGroup = true;

                if (enumUsers.hasMoreElements())

                {

                    while (enumUsers.hasMoreElements())

                    {

                        Object currentUserKey = enumUsers.nextElement();



                        if (isFirstUser && !enumUsers.hasMoreElements())

                        {

                            isFirstUser = false;



                            if (enumUserGroups.hasMoreElements())

                            {

                                searchTaskQuery.setWhere("AND", 2, "WORKFLOW_TASK_strPerformerType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                                searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_intPerformer", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                            }

                            else

                            {

                                searchTaskQuery.setWhere("AND", 2, "WORKFLOW_TASK_strPerformerType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                                searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_intPerformer", "=", currentUserKey, 2, DALQuery.WHERE_HAS_VALUE);

                            }

                        }

                        else if (isFirstUser)

                        {

                            isFirstUser = false;

                            searchTaskQuery.setWhere("AND", 2, "WORKFLOW_TASK_strPerformerType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                            searchTaskQuery.setWhere("AND", 1, "WORKFLOW_TASK_intPerformer", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                        }

                        else if (!enumUsers.hasMoreElements())

                        {

                            if (enumUserGroups.hasMoreElements())

                                searchTaskQuery.setWhere("OR", 0, "WORKFLOW_TASK_intPerformer", "=", currentUserKey, 2, DALQuery.WHERE_HAS_VALUE);

                            else

                                searchTaskQuery.setWhere("OR", 0, "WORKFLOW_TASK_intPerformer", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                        }

                        else

                        {

                            searchTaskQuery.setWhere("OR", 0, "WORKFLOW_TASK_intPerformer", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                        }



                    }



                    while (enumUserGroups.hasMoreElements())

                    {

                        Object currentGroupKey = enumUserGroups.nextElement();



                        if (isFirstGroup && !enumUserGroups.hasMoreElements())

                        {

                            isFirstGroup = false;



                            searchTaskQuery.setWhere("OR", 1, "WORKFLOW_TASK_strPerformerType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);

                            searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_intPerformer", "=", currentGroupKey, 2, DALQuery.WHERE_HAS_VALUE);

                        }

                        else if (isFirstGroup)

                        {

                            isFirstGroup = false;

                            searchTaskQuery.setWhere("OR", 1, "WORKFLOW_TASK_strPerformerType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);

                            searchTaskQuery.setWhere("AND", 1, "WORKFLOW_TASK_intPerformer", "=", currentGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

                        }

                        else if (!enumUserGroups.hasMoreElements())

                        {

                            searchTaskQuery.setWhere("OR", 0, "WORKFLOW_TASK_intPerformer", "=", currentGroupKey, 3, DALQuery.WHERE_HAS_VALUE);

                        }

                        else

                        {

                            searchTaskQuery.setWhere("OR", 0, "WORKFLOW_TASK_intPerformer", "=", currentGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

                        }

                    }

                }





                // if user specifies task name

                if (runtimeData.getParameter("WORKFLOW_TASK_strName") != null &&

                    !runtimeData.getParameter("WORKFLOW_TASK_strName").trim().equals(""))

                    searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_strName", "=", runtimeData.getParameter("WORKFLOW_TASK_strName"), 0, DALQuery.WHERE_HAS_VALUE);



                // if user specifies priority

                if (runtimeData.getParameter("WORKFLOW_TASK_strPriority") != null &&

                    !runtimeData.getParameter("WORKFLOW_TASK_strPriority").trim().equals(""))

                {

                    if (runtimeData.getParameter("WORKFLOW_TASK_strPriority").equals("Low"))

                    {

                        searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_strPriority", "=", "1", 0, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (runtimeData.getParameter("WORKFLOW_TASK_strPriority").equals("Medium"))

                    {

                        searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_strPriority", "=", "2", 0, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (runtimeData.getParameter("WORKFLOW_TASK_strPriority").equals("High"))

                    {

                        searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_strPriority", "=", "3", 0, DALQuery.WHERE_HAS_VALUE);

                    }

                }



                // if user specifies status

                if (runtimeData.getParameter("WORKFLOW_TASK_strStatus") != null &&

                    !runtimeData.getParameter("WORKFLOW_TASK_strStatus").trim().equals(""))

                    searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_strStatus", "=", runtimeData.getParameter("WORKFLOW_TASK_strStatus"), 0, DALQuery.WHERE_HAS_VALUE);



                if (runtimeData.getParameter("WORKFLOW_TASK_dtDateReceived_Operator") != null && !runtimeData.getParameter("WORKFLOW_TASK_dtDateReceived_Operator").equals(""))

                {

                    // if user does not specify received date, pass a null value through runtime data

                    if (runtimeData.getParameter("WORKFLOW_TASK_dtDateReceived_Day") == null ||

                        runtimeData.getParameter("WORKFLOW_TASK_dtDateReceived_Day").trim().equals("") ||

                        runtimeData.getParameter("WORKFLOW_TASK_dtDateReceived_Month") == null ||

                        runtimeData.getParameter("WORKFLOW_TASK_dtDateReceived_Month").trim().equals("") ||

                        runtimeData.getParameter("WORKFLOW_TASK_dtDateReceived_Year") == null ||

                        runtimeData.getParameter("WORKFLOW_TASK_dtDateReceived_Year").trim().equals(""))

                    {

                        runtimeData.setParameter("WORKFLOW_TASK_dtDateReceived", (String) null);

                    }

                    // else set the condition

                    else

                    {

                        runtimeData.setParameter("WORKFLOW_TASK_dtDateReceived", QueryChannel.makeDateFromForm("WORKFLOW_TASK_dtDateReceived", runtimeData));

                        searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_dtDateReceived", runtimeData.getParameter("WORKFLOW_TASK_dtDateReceived_Operator"), runtimeData.getParameter("WORKFLOW_TASK_dtDateReceived"), 0, DALQuery.WHERE_HAS_VALUE);

                    }

                }



                if (runtimeData.getParameter("WORKFLOW_TASK_dtDateCompleted_Operator") != null && !runtimeData.getParameter("WORKFLOW_TASK_dtDateCompleted_Operator").equals(""))

                {

                    if (runtimeData.getParameter("WORKFLOW_TASK_dtDateCompleted_Day") == null ||

                        runtimeData.getParameter("WORKFLOW_TASK_dtDateCompleted_Day").trim().equals("") ||

                        runtimeData.getParameter("WORKFLOW_TASK_dtDateCompleted_Month") == null ||

                        runtimeData.getParameter("WORKFLOW_TASK_dtDateCompleted_Month").trim().equals("") ||

                        runtimeData.getParameter("WORKFLOW_TASK_dtDateCompleted_Year") == null ||

                        runtimeData.getParameter("WORKFLOW_TASK_dtDateCompleted_Year").trim().equals(""))

                    {

                        runtimeData.setParameter("WORKFLOW_TASK_dtDateCompleted", (String) null);

                    }

                    else

                    {

                        runtimeData.setParameter("WORKFLOW_TASK_dtDateCompleted", QueryChannel.makeDateFromForm("WORKFLOW_TASK_dtDateCompleted", runtimeData));

                        searchTaskQuery.setWhere("AND", 0, "WORKFLOW_TASK_dtDateCompleted", runtimeData.getParameter("WORKFLOW_TASK_dtDateCompleted_Operator"), runtimeData.getParameter("WORKFLOW_TASK_dtDateCompleted"), 0, DALQuery.WHERE_HAS_VALUE);

                    }

                }

                
            


                // checking data validation

                strDataValidation = QueryChannel.validateData(vtSearchTaskForm, runtimeData);



                
            }
                //---------- END PAGING ------------//

            
            // --- ORDERING --- //
            searchTaskQuery.clearOrderBy();
            
            if( runtimeData.getParameter( "orderBy") != null ){
                
                if( !hsSearchOrder.containsKey(runtimeData.getParameter( "orderBy"))){
                    hsSearchOrder = null;
                    vtSearchOrder = null;
                    hsSearchOrder = new Hashtable(2,2);
                    vtSearchOrder = new Vector(2,2);
                }


                String order = "ASC";

                int intOrder = 0;

                for( ; intOrder < vtSearchOrder.size(); intOrder++ ){

                    if( ((String)vtSearchOrder.get(intOrder)).equals( runtimeData.getParameter( "orderBy") ))

                        break;

                }



                if( hsSearchOrder.containsKey( runtimeData.getParameter("orderBy") ) ){



                    if( ((String) hsSearchOrder.get( runtimeData.getParameter("orderBy") )).equals( "ASC" ) ){

                        order = "DESC";

                    }

                }else{

                    hsSearchOrder.put( runtimeData.getParameter("orderBy"), order );

                }



                hsSearchOrder.remove( runtimeData.getParameter("orderBy") );

                hsSearchOrder.put( runtimeData.getParameter("orderBy"), order );



                if( intOrder < vtSearchOrder.size() ){

                    vtSearchOrder.remove(intOrder);

                }

                vtSearchOrder.add(runtimeData.getParameter( "orderBy" ));

            }



            for( int i = vtSearchOrder.size()-1; i >= 0 ; i-- ){

                String orderBy = (String) vtSearchOrder.get(i);

                searchTaskQuery.setOrderBy( orderBy , (String) hsSearchOrder.get( orderBy ));

            }
            
            //-- END ORDERING --//
                // if all data is OK, do the search

            if (strDataValidation == null)

            {

                //System.err.println(searchTaskQuery.convertSelectQueryToString());

                //ResultSet rsResult = searchTaskQuery.executeSelect();

                // ------------------------------ START PAGING FROM HERE ------ LONG ----


                if( (runtimeData.getParameter( "noOfRecords") != null ) && 
                    isNumeric( runtimeData.getParameter( "noOfRecords"))){
                    noOfRecords = runtimeData.getParameter( "noOfRecords");
                }

                int intNoOfRecords = Integer.parseInt( noOfRecords );

                if( ( runtimeData.getParameter( "currentPage" ) != null ) &&
                    isNumeric( runtimeData.getParameter( "currentPage" ))){
                    currentPage = Integer.parseInt( runtimeData.getParameter( "currentPage") );
                }

                searchTaskQuery.clearLimitOffset();
                ResultSet tRS = searchTaskQuery.executeSelect();
                //System.err.println( "Sql: --> " + searchTaskQuery.convertSelectQueryToString());

                tRS.last();
                int allRecords = tRS.getRow();
                tRS = null;



                if( intNoOfRecords < 0) intNoOfRecords = 1;
                //if( intNoOfRecords > 20) intNoOfRecords = 20;

                noOfPages = ( (allRecords - 1) / intNoOfRecords ) + 1;

                if( currentPage < 1 ) currentPage =1;
                if( currentPage > noOfPages ) currentPage = noOfPages;

                searchTaskQuery.setLimitOffset( intNoOfRecords, ((currentPage - 1) * intNoOfRecords) );



                strXML = "<currentPage>" + currentPage + "</currentPage>" ;
                strXML += "<noOfPages>" + noOfPages + "</noOfPages>" ;
                strXML += "<noOfRecords>" + noOfRecords + "</noOfRecords>" ;
                strXML += QueryChannel.buildAddFormXMLFile( DatabaseSchema.getFormFields("record_per_page"));

                ResultSet rsResult = searchTaskQuery.executeSelect();



                if (hashUsers.size() > 0)

                {

                    // if we can build lock record for the list

//rennypv                    if (buildSearchTaskRecordLock(rsResult))

                    {

                        strXML += QueryChannel.buildFormLabelXMLFile(vtSearchTaskForm) + 

                                 QueryChannel.buildViewXMLFile(vtSearchTaskForm, runtimeData) +

                                 WorkspaceChannel.buildSearchXMLFileForTask("search_task", rsResult, vtSearchTaskForm);

                    }

                    // display locking error message

/*                    else

                    {

                        strXML = QueryChannel.buildFormLabelXMLFile(vtSearchTaskForm) + 

                                 QueryChannel.buildViewXMLFile(vtSearchTaskForm, runtimeData);

                        strXML += "<strErrorMessage>" + "Record locking produced some error. Please try again later." + "</strErrorMessage>";

                    }*/

                }

                else

                {

                    strXML += QueryChannel.buildFormLabelXMLFile(vtSearchTaskForm) + 

                                 QueryChannel.buildViewXMLFile(vtSearchTaskForm, runtimeData);

                }

            }

            // there is some invalid data, display error message

            else

            {

                strXML += QueryChannel.buildFormLabelXMLFile(vtSearchTaskForm) + 

                         QueryChannel.buildViewXMLFile(vtSearchTaskForm, runtimeData);

                strXML += "<strErrorMessage>" + strDataValidation + "</strErrorMessage>";

            }

            strXML += "<task_order>"+ task_order+ "</task_order>" +

                    "<task_sort>"+ task_sort+ "</task_sort>" +

                    "<priority_order>"+ priority_order+ "</priority_order>" +

                    "<DateTime_order>"+ DateTime_order+ "</DateTime_order>" +

                    "<DateCompleted_order>"+ DateCompleted_order+ "</DateCompleted_order>" +

                    "<status_order>"+ status_order+ "</status_order>";
            
            
            

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Build active task list form

     */

    private void buildActiveTaskListForm()

    {

        boolean task_sort = false;

        

        strStylesheet = VIEW_ACTIVE_TASK;

        Vector vtSearchTaskForm = DatabaseSchema.getFormFields(SEARCH_TASK);
        

                

        try

        {
            
            int noOfPages = 0;
            int currentPage = 1;
            String noOfRecords = PropertiesManager.getProperty("neuragenix.platformgenix.RecordPerPage");


            // get the users and user groups related to the current system user

            getUserAndUserGroupWithReassignTask();

            



            

            DALSecurityQuery query = new DALSecurityQuery(WORKSPACE_SEARCH_TASK, authToken);

            query.setDomain("WORKFLOW_TASK", null, null, null);

            query.setDomain("WORKFLOW_INSTANCE", "WORKFLOW_TASK_intWorkflowInstanceKey", "WORKFLOW_INSTANCE_intWorkflowInstanceKey", "INNER JOIN");

            query.setFields(vtSearchTaskForm, null);

            query.setWhere(null, 0, "WORKFLOW_TASK_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "WORKFLOW_TASK_strType", "=", "Human", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "WORKFLOW_INSTANCE_strStatus", "=", "In progress", 0, DALQuery.WHERE_HAS_VALUE);

            //query.setWhere("AND", 0, "WORKFLOW_TASK_strType", "<>", "Stop", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 1, "WORKFLOW_TASK_strStatus", "=", "Active", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("OR", 0, "WORKFLOW_TASK_strStatus", "=", "Started", 1, DALQuery.WHERE_HAS_VALUE);

                        

            

            Enumeration enumUsers = hashUsers.keys();

            Enumeration enumUserGroups = hashUserGroups.keys();

            boolean isFirstUser = true;

            boolean isFirstGroup = true;

            if (enumUsers.hasMoreElements())

            {

                while (enumUsers.hasMoreElements())

                {

                    Object currentUserKey = enumUsers.nextElement();



                    if (isFirstUser && !enumUsers.hasMoreElements())

                    {

                        isFirstUser = false;



                        if (enumUserGroups.hasMoreElements())

                        {

                            query.setWhere("AND", 2, "WORKFLOW_TASK_strPerformerType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                            query.setWhere("AND", 0, "WORKFLOW_TASK_intPerformer", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                        }

                        else

                        {

                            query.setWhere("AND", 2, "WORKFLOW_TASK_strPerformerType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                            query.setWhere("AND", 0, "WORKFLOW_TASK_intPerformer", "=", currentUserKey, 2, DALQuery.WHERE_HAS_VALUE);

                        }

                    }

                    else if (isFirstUser)

                    {

                        isFirstUser = false;

                        query.setWhere("AND", 2, "WORKFLOW_TASK_strPerformerType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 1, "WORKFLOW_TASK_intPerformer", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (!enumUsers.hasMoreElements())

                    {

                        if (enumUserGroups.hasMoreElements())

                            query.setWhere("OR", 0, "WORKFLOW_TASK_intPerformer", "=", currentUserKey, 2, DALQuery.WHERE_HAS_VALUE);

                        else

                            query.setWhere("OR", 0, "WORKFLOW_TASK_intPerformer", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                    }

                    else

                    {

                        query.setWhere("OR", 0, "WORKFLOW_TASK_intPerformer", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }



                }



                while (enumUserGroups.hasMoreElements())

                {

                    Object currentGroupKey = enumUserGroups.nextElement();



                    if (isFirstGroup && !enumUserGroups.hasMoreElements())

                    {

                        isFirstGroup = false;



                        query.setWhere("OR", 1, "WORKFLOW_TASK_strPerformerType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 0, "WORKFLOW_TASK_intPerformer", "=", currentGroupKey, 2, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (isFirstGroup)

                    {

                        isFirstGroup = false;

                        query.setWhere("OR", 1, "WORKFLOW_TASK_strPerformerType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 1, "WORKFLOW_TASK_intPerformer", "=", currentGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (!enumUserGroups.hasMoreElements())

                    {

                        query.setWhere("OR", 0, "WORKFLOW_TASK_intPerformer", "=", currentGroupKey, 3, DALQuery.WHERE_HAS_VALUE);

                    }

                    else

                    {

                        query.setWhere("OR", 0, "WORKFLOW_TASK_intPerformer", "=", currentGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                }

            }

            

            strXML = "";

            

            // ------------------- START ORDERING FROM HERE ---------------- LONG --------
            
                       
            if( runtimeData.getParameter( "orderBy") != null ){
                
                if( !hsOrder.containsKey(runtimeData.getParameter( "orderBy"))){
                    hsOrder = null;
                    vtOrder = null;
                    hsOrder = new Hashtable(2,2);
                    vtOrder = new Vector(2,2);
                }

                String order = "ASC";

                int intOrder = 0;

                for( ; intOrder < vtOrder.size(); intOrder++ ){

                    if( ((String)vtOrder.get(intOrder)).equals( runtimeData.getParameter( "orderBy") ))

                        break;

                }

                

                if( hsOrder.containsKey( runtimeData.getParameter("orderBy") ) ){

                    

                    if( ((String) hsOrder.get( runtimeData.getParameter("orderBy") )).equals( "ASC" ) ){

                        order = "DESC";

                    }

                }else{

                    hsOrder.put( runtimeData.getParameter("orderBy"), order );

                }

                

                hsOrder.remove( runtimeData.getParameter("orderBy") );

                hsOrder.put( runtimeData.getParameter("orderBy"), order );

                

                if( intOrder < vtOrder.size() ){

                    vtOrder.remove(intOrder);

                    if( runtimeData.getParameter( "orderBy" ).equals( "WORKFLOW_TASK_dtDateReceived")){

                        vtOrder.remove(intOrder-1);    

                    }

                }

                

                if( runtimeData.getParameter( "orderBy" ).equals( "WORKFLOW_TASK_dtDateReceived")){

                    vtOrder.add("WORKFLOW_TASK_strTimeReceived");

                    vtOrder.add("WORKFLOW_TASK_dtDateReceived");

                }else{

                    vtOrder.add(runtimeData.getParameter( "orderBy" ));

                }

                

                if( runtimeData.getParameter( "orderBy" ).equals( "WORKFLOW_TASK_dtDateReceived")){

                    if( hsOrder.containsKey( "WORKFLOW_TASK_strTimeReceived" ) ){

                        hsOrder.remove( "WORKFLOW_TASK_strTimeReceived" );

                    }

                    hsOrder.put( "WORKFLOW_TASK_strTimeReceived", order );   

                }

            }

            

            for( int i = vtOrder.size()-1; i >= 0 ; i-- ){

                String orderBy = (String) vtOrder.get(i);

                query.setOrderBy( orderBy , (String) hsOrder.get( orderBy ));

            }
           
            if( hsOrder.containsKey("WORKFLOW_TASK_strName")){
                strXML+="<sort>" + "WORKFLOW_TASK_strName" +
                "</sort>";
                if(((String) hsOrder.get( "WORKFLOW_TASK_strName" )).equals("ASC"))
                    strXML+="<order>" + "ascending" +
                    "</order>";
                else
                    strXML+="<order>" + "descending" +
                    "</order>";
                    
            }else{
                
                strXML+="<order>" + "descending" +
                    "</order>";
            }

            

            // ------------------- START PAGING FROM HERE ---------------- LONG --------

                        



            if( (runtimeData.getParameter( "noOfRecords") != null ) && 

                isNumeric( runtimeData.getParameter( "noOfRecords"))){

                noOfRecords = runtimeData.getParameter( "noOfRecords");

            }

                        

            int intNoOfRecords = Integer.parseInt( noOfRecords );

            

            

            if( ( runtimeData.getParameter( "currentPage" ) != null ) &&

                isNumeric( runtimeData.getParameter( "currentPage" ))){

                currentPage = Integer.parseInt( runtimeData.getParameter( "currentPage") );

            }



            query.clearLimitOffset();

            ResultSet tRS = query.executeSelect();

            //System.err.println( "Sql: --> " + query.convertSelectQueryToString());

            tRS.last();

            int allRecords = tRS.getRow();

            tRS = null;



            if( intNoOfRecords < 0) intNoOfRecords = 1;

            //if( intNoOfRecords > 20) intNoOfRecords = 20;

            

            noOfPages = ( (allRecords - 1) / intNoOfRecords ) + 1;



            if( currentPage < 1 ) currentPage =1;

            if( currentPage > noOfPages ) currentPage = noOfPages;



            query.setLimitOffset( intNoOfRecords, ((currentPage - 1) * intNoOfRecords) );


            strXML += "<currentPage>" + currentPage + "</currentPage>" ;
            strXML += "<noOfPages>" + noOfPages + "</noOfPages>" ;
            strXML += "<noOfRecords>" + noOfRecords + "</noOfRecords>" ;
            strXML += QueryChannel.buildAddFormXMLFile( DatabaseSchema.getFormFields("record_per_page"));


            ResultSet rsResult = query.executeSelect();

            
            if (hashUsers.size() > 0)

            {

                // if we can build lock record for the list

//rennypv                if (buildSearchTaskRecordLock(rsResult))

                    strXML += QueryChannel.buildFormLabelXMLFile(vtSearchTaskForm) + 
                        WorkspaceChannel.buildSearchXMLFileForTask("search_task", rsResult, vtSearchTaskForm);
                    
                    

/*                else

                {

                    strXML = QueryChannel.buildFormLabelXMLFile(vtSearchTaskForm);

                    strXML += "<strErrorMessage>" + "Record locking produced some error. Please try again later." + "</strErrorMessage>";

                }*/

            }

            else

            {

                strXML += QueryChannel.buildFormLabelXMLFile(vtSearchTaskForm);

            }

            /*strXML += "<task_order>"+ task_order+ "</task_order>" +

                    "<task_sort>"+ task_sort+ "</task_sort>" +

                    "<priority_order>"+ priority_order+ "</priority_order>" +

                    "<DateTime_order>"+ DateTime_order+ "</DateTime_order>" +

                    "<status_order>"+ status_order+ "</status_order>";

             */

            //strXML = QueryChannel.buildFormLabelXMLFile(vtSearchTaskForm) +

            //         QueryChannel.buildSearchXMLFile("search_task", rsResult, vtSearchTaskForm);

            
            
            

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Build reassign tasks form

     */

    private void buildReassignTasksForm()

    {

        strStylesheet = REASSIGN_TASKS;

        Vector vtReassignTasksForm = DatabaseSchema.getFormFields(REASSIGN_TASKS);

        Vector vtSearchReassignTasksForm = DatabaseSchema.getFormFields("search_reassign_tasks");

        

        try

        {

            // get users and groups related to the system users

            getUserAndUserGroup();

            

            // get all reassign tasks done by the user

            // getting users & groups from reassign table

            DALQuery query = new DALQuery();

            query.setDomain("WORKFLOW_TASK_REASSIGN", null, null, null);

            query.setFields(vtSearchReassignTasksForm, null);

            query.setWhere(null, 0, "WORKFLOW_TASK_REASSIGN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "WORKFLOW_TASK_REASSIGN_intTaskKey", "=", "-1", 0, DALQuery.WHERE_HAS_VALUE);

            

            Enumeration enumUsers = hashUsers.keys();

            boolean isFirstUser = true;

            

            while (enumUsers.hasMoreElements())

            {

                Object currentUserKey = enumUsers.nextElement();

                

                if (isFirstUser && !enumUsers.hasMoreElements())

                {

                    isFirstUser = false;

                    query.setWhere("AND", 0, "WORKFLOW_TASK_REASSIGN_intReassignBy", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                }

                else if (isFirstUser)

                {

                    isFirstUser = false;

                    query.setWhere("AND", 1, "WORKFLOW_TASK_REASSIGN_intReassignBy", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                }

                else if (!enumUsers.hasMoreElements())

                {

                    query.setWhere("OR", 0, "WORKFLOW_TASK_REASSIGN_intReassignBy", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                }

                else

                {

                    query.setWhere("OR", 0, "WORKFLOW_TASK_REASSIGN_intReassignBy", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                }

            }

            

            ResultSet rsResult = query.executeSelect();

            

            if (runtimeData.getParameter("source") != null)

            {

                if (runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strFromUserName") != null)

                {

                    oldRuntimeData.setParameter("WORKFLOW_TASK_REASSIGN_strFromUserName", runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strFromUserName"));

                    oldRuntimeData.setParameter("WORKFLOW_TASK_REASSIGN_intFromUserKey", runtimeData.getParameter("key"));

                    oldRuntimeData.setParameter("WORKFLOW_TASK_REASSIGN_strFromUserType", runtimeData.getParameter("type"));

                    strXML = QueryChannel.buildFormLabelXMLFile(vtReassignTasksForm) +

                             QueryChannel.buildViewXMLFile(vtReassignTasksForm, oldRuntimeData) +

                             buildUserDropdown(runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_intReassignBy")) +

                             QueryChannel.buildSearchXMLFile("search_reassign_tasks", rsResult, vtSearchReassignTasksForm);

                    strXML += "<WORKFLOW_TASK_REASSIGN_strFromUserName>" + runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strFromUserName") + "</WORKFLOW_TASK_REASSIGN_strFromUserName>";

                    strXML += "<WORKFLOW_TASK_REASSIGN_strToUserName>" + oldRuntimeData.getParameter("WORKFLOW_TASK_REASSIGN_strToUserName") + "</WORKFLOW_TASK_REASSIGN_strToUserName>";

                }

                else

                {

                    oldRuntimeData.setParameter("WORKFLOW_TASK_REASSIGN_strToUserName", runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strToUserName"));

                    oldRuntimeData.setParameter("WORKFLOW_TASK_REASSIGN_intToUserKey", runtimeData.getParameter("key"));

                    oldRuntimeData.setParameter("WORKFLOW_TASK_REASSIGN_strToUserType", runtimeData.getParameter("type"));

                    strXML = QueryChannel.buildFormLabelXMLFile(vtReassignTasksForm) +

                             QueryChannel.buildViewXMLFile(vtReassignTasksForm, oldRuntimeData) +

                             buildUserDropdown(runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_intReassignBy")) +

                             QueryChannel.buildSearchXMLFile("search_reassign_tasks", rsResult, vtSearchReassignTasksForm);

                    strXML += "<WORKFLOW_TASK_REASSIGN_strToUserName>" + runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strToUserName") + "</WORKFLOW_TASK_REASSIGN_strToUserName>";

                    strXML += "<WORKFLOW_TASK_REASSIGN_strFromUserName>" + oldRuntimeData.getParameter("WORKFLOW_TASK_REASSIGN_strFromUserName") + "</WORKFLOW_TASK_REASSIGN_strFromUserName>";

                }

            }

            else

            {

                strXML = QueryChannel.buildFormLabelXMLFile(vtReassignTasksForm) +

                         QueryChannel.buildViewXMLFile(vtReassignTasksForm, runtimeData) +

                         buildUserDropdown(runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_intReassignBy")) +

                         QueryChannel.buildSearchXMLFile("search_reassign_tasks", rsResult, vtSearchReassignTasksForm);

                if (runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strToUserName") != null)

                    strXML += "<WORKFLOW_TASK_REASSIGN_strToUserName>" + runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strToUserName") + "</WORKFLOW_TASK_REASSIGN_strToUserName>";

                if (runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strFromUserName") != null)

                    strXML += "<WORKFLOW_TASK_REASSIGN_strFromUserName>" + oldRuntimeData.getParameter("WORKFLOW_TASK_REASSIGN_strFromUserName") + "</WORKFLOW_TASK_REASSIGN_strFromUserName>";

            }

                         

            //strXML = QueryChannel.buildFormLabelXMLFile(vtReassignTasksForm) +

            //         QueryChannel.buildAddFormXMLFile(vtReassignTasksForm);

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Build reassign a task form

     */

    private void buildReassignTaskForm()

    {

        strStylesheet = REASSIGN_TASK;

        Vector vtReassignTaskForm = DatabaseSchema.getFormFields(REASSIGN_TASK);

        

        try

        {

            //System.err.println(runtimeData.getParameter("key"));

            //System.err.println(runtimeData.getParameter("type"));

            // if it comes from Org Chart page, rebuild the page with old runtime data

            if (runtimeData.getParameter("source") != null && runtimeData.getParameter("source").equals("orgchart"))

            {

                oldRuntimeData.setParameter("WORKFLOW_TASK_REASSIGN_strToUserName", runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strToUserName"));

                oldRuntimeData.setParameter("WORKFLOW_TASK_REASSIGN_intToUserKey", runtimeData.getParameter("key"));

                oldRuntimeData.setParameter("WORKFLOW_TASK_REASSIGN_strToUserType", runtimeData.getParameter("type"));

                

                strXML = QueryChannel.buildFormLabelXMLFile(vtReassignTaskForm) +

                         QueryChannel.buildViewXMLFile(vtReassignTaskForm, oldRuntimeData) +

                         buildUserDropdown(runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_intReassignBy"));

                strXML += "<WORKFLOW_TASK_REASSIGN_strToUserName>" + runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strToUserName") + "</WORKFLOW_TASK_REASSIGN_strToUserName>";

                            //rennypv added to maintain the runtimeData

                runtimeData.setParameter("WORKFLOW_TASK_REASSIGN_intTaskKey",oldRuntimeData.getParameter("WORKFLOW_TASK_REASSIGN_intTaskKey"));

                runtimeData.setParameter("WORKFLOW_TASK_strName",oldRuntimeData.getParameter("WORKFLOW_TASK_strName"));

            }

            else

            {

                strXML = QueryChannel.buildFormLabelXMLFile(vtReassignTaskForm) +

                         QueryChannel.buildViewXMLFile(vtReassignTaskForm, runtimeData) +

                         buildUserDropdown(runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_intReassignBy"));

                if (runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strToUserName") != null)

                    strXML += "<WORKFLOW_TASK_REASSIGN_strToUserName>" + runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strToUserName") + "</WORKFLOW_TASK_REASSIGN_strToUserName>";

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Build view task form

     */

    private void buildViewTaskForm()

    {

        strStylesheet = VIEW_TASK;

        Vector vtViewTaskForm = DatabaseSchema.getFormFields(VIEW_TASK);

        

        try

        {

            if (runtimeData.getParameter("WORKFLOW_TASK_intTaskKey") != null)

            {

                if (buildTaskRecordLock(runtimeData.getParameter("WORKFLOW_TASK_intTaskKey")))

                {

                    strXML = QueryChannel.buildFormLabelXMLFile(vtViewTaskForm) + 

                             WorkspaceChannel.buildViewTaskFromKey(true, vtViewTaskForm, runtimeData.getParameter("current"), runtimeData.getParameter("source"), 

                                    runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"), authToken.getUserIdentifier());

                    

                    //System.err.println(runtimeData.getParameter("source"));

                    if (runtimeData.getParameter("source") != null)

                        strXML += "<source>" + runtimeData.getParameter("source") + "</source>";

                }

                else

                {

                    buildSearchTaskForm();

                    strXML += "<strErrorMessage>" + "Record locking produced some error. Please try again later." + "</strErrorMessage>";

                }

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }



	/**

     * View task status

     */
/*
    private void buildViewTaskStatus()

    {

        strStylesheet = VIEW_TASK_STATUS;

        Vector vtViewTaskForm = DatabaseSchema.getFormFields(VIEW_TASK_STATUS);

        Vector vtViewTaskHistoryForm = DatabaseSchema.getFormFields(VIEW_TASK_HISTORY);

        

        try

        {

            strXML = "<WORKFLOW_TASK_intTaskKey>" + runtimeData.getParameter("WORKFLOW_TASK_intTaskKey") + "</WORKFLOW_TASK_intTaskKey>";

            DALQuery query1 = new DALQuery();

            query1.setDomain("WORKFLOW_TASK", null, null, null);

            query1.setDomain("ORGUSER", "WORKFLOW_TASK_intPerformer", "ORGUSER_intOrgUserKey", "LEFT JOIN");

            query1.setFields(vtViewTaskForm, null);

            query1.setWhere(null, 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=", runtimeData.getParameter("WORKFLOW_TASK_intWorkflowInstanceKey"), 0, DALQuery.WHERE_HAS_VALUE);

            query1.setWhere("AND", 0, "WORKFLOW_TASK_strType", "<>", "Start", 0, DALQuery.WHERE_HAS_VALUE);

            query1.setWhere("AND", 0, "WORKFLOW_TASK_strType", "<>", "Stop", 0, DALQuery.WHERE_HAS_VALUE);

            query1.setWhere("AND", 0, "WORKFLOW_TASK_strStatus", "<>", "Not active", 0, DALQuery.WHERE_HAS_VALUE);

			query1.setWhere("AND", 0, "WORKFLOW_TASK_strStatus", "<>", "Active", 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResult = query1.executeSelect();



            DALQuery query2 = new DALQuery();

            query2.setDomain("WORKFLOW_TASK_HISTORY", null, null, null);

            query2.setDomain("ORGUSER", "WORKFLOW_TASK_HISTORY_intPerformer", "ORGUSER_intOrgUserKey", "LEFT JOIN");

            query2.setFields(vtViewTaskHistoryForm, null);

            query2.setWhere(null, 0, "WORKFLOW_TASK_HISTORY_intWorkflowInstanceKey", "=", runtimeData.getParameter("WORKFLOW_TASK_intWorkflowInstanceKey"), 0, DALQuery.WHERE_HAS_VALUE);

            query2.setWhere("AND", 0, "WORKFLOW_TASK_HISTORY_strType", "<>", "Start", 0, DALQuery.WHERE_HAS_VALUE);

            query2.setWhere("AND", 0, "WORKFLOW_TASK_HISTORY_strType", "<>", "Stop", 0, DALQuery.WHERE_HAS_VALUE);

			query2.setWhere("AND", 0, "WORKFLOW_TASK_HISTORY_strStatus", "<>", "Not active", 0, DALQuery.WHERE_HAS_VALUE);

            query2.setWhere("AND", 0, "WORKFLOW_TASK_HISTORY_strStatus", "<>", "Active", 0, DALQuery.WHERE_HAS_VALUE);

            //query2.setOrderBy("WORKFLOW_TASK_HISTORY_dtDateCompleted", "DESC");

            //query2.setOrderBy("WORKFLOW_TASK_HISTORY_strTimeCompleted", "DESC");

			ResultSet rsResult2 = query2.executeSelect(); 



			ResultSet rsResultFinal = null;

            if(rsResult2.first())

			{

				query2.setOrderBy("WORKFLOW_TASK_HISTORY_dtDateCompleted", "DESC");

            	query2.setOrderBy("WORKFLOW_TASK_HISTORY_strTimeCompleted", "DESC");

            	query1.setSiblingQuery("UNION", query2);

			}

			else

			{

				query1.setOrderBy("WORKFLOW_TASK_dtDateCompleted", "DESC");

            	query1.setOrderBy("WORKFLOW_TASK_strTimeCompleted", "DESC");

			}



            rsResultFinal = query1.executeSelect();  

			

            strXML += WorkspaceChannel.buildSearchXMLFileForTask("search_task", rsResultFinal, vtViewTaskForm);

            //strXML += WorkspaceChannel.buildSearchXMLFileForTaskHistory("search_task_history", rsResultHistory, vtViewTaskHistoryForm);

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

        

    }
*/
    

    /**

     * View task status

     */

	

    private void buildViewTaskStatus()

    {

        strStylesheet = VIEW_TASK_STATUS;

        Vector vtViewTaskForm = new Vector( 5, 2 );

        Vector vtViewTaskHistoryForm = new Vector( 5, 2 );

        

        try

        {

            runtimeData.setParameter( "WORKFLOW_INSTANCE_intWorkflowInstanceKey", runtimeData.getParameter("WORKFLOW_TASK_intWorkflowInstanceKey"));

            strXML = "<WORKFLOW_TASK_intTaskKey>" + runtimeData.getParameter("WORKFLOW_TASK_intTaskKey") + "</WORKFLOW_TASK_intTaskKey>";

            vtViewTaskForm = DatabaseSchema.getFormFields("view_task_status_group");

            ResultSet rsResultFinal = null;

            

            DALQuery query1 = new DALQuery();

            query1.setDomain("WORKFLOW_TASK", null, null, null);

            query1.setDomain("ORGGROUP", "WORKFLOW_TASK_intPerformer", "ORGGROUP_intOrgGroupKey", "LEFT JOIN");

            query1.setFields(vtViewTaskForm, null);

            query1.setWhere(null, 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=", runtimeData.getParameter("WORKFLOW_INSTANCE_intWorkflowInstanceKey"), 0, DALQuery.WHERE_HAS_VALUE);

            query1.setWhere("AND", 1, "WORKFLOW_TASK_strPerformerType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);
            query1.setWhere("OR", 0, "WORKFLOW_TASK_strType", "=", "System", 1, DALQuery.WHERE_HAS_VALUE);
            query1.setWhere("AND", 0, "WORKFLOW_TASK_strStatus", "<>", "Not active", 0, DALQuery.WHERE_HAS_VALUE);
            rsResultFinal = query1.executeSelect();
            strXML+= WorkspaceChannel.buildSearchXMLFileForTask("search_task", rsResultFinal, vtViewTaskForm);

            
            // ------------------ BUILD CURRENT TASKS FOR USER

            vtViewTaskForm = DatabaseSchema.getFormFields("view_task_status");

            DALQuery query2 = new DALQuery();

            query2.setDomain("WORKFLOW_TASK", null, null, null);

            query2.setDomain("ORGUSER", "WORKFLOW_TASK_intPerformer", "ORGUSER_intOrgUserKey", "LEFT JOIN");

            query2.setFields(vtViewTaskForm, null);

            query2.setWhere(null, 0, "WORKFLOW_TASK_intWorkflowInstanceKey", "=", runtimeData.getParameter("WORKFLOW_INSTANCE_intWorkflowInstanceKey"), 0, DALQuery.WHERE_HAS_VALUE);

            query2.setWhere("AND", 0, "WORKFLOW_TASK_strPerformerType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

            query2.setWhere("AND", 0, "WORKFLOW_TASK_strStatus", "<>", "Not active", 0, DALQuery.WHERE_HAS_VALUE);
            rsResultFinal = query2.executeSelect();
            strXML+= WorkspaceChannel.buildSearchXMLFileForTask("search_task", rsResultFinal, vtViewTaskForm);

                    

            // ------------------ BUILD TASK HISTORY FOR GROUP

            vtViewTaskForm = DatabaseSchema.getFormFields("view_task_history_group");

            DALQuery query3 = new DALQuery();

            query3.setDomain("WORKFLOW_TASK_HISTORY", null, null, null);

            query3.setDomain("ORGGROUP", "WORKFLOW_TASK_HISTORY_intPerformer", "ORGGROUP_intOrgGroupKey", "LEFT JOIN");

            query3.setFields(vtViewTaskForm, null);

            query3.setWhere(null, 0, "WORKFLOW_TASK_HISTORY_intWorkflowInstanceKey", "=", runtimeData.getParameter("WORKFLOW_INSTANCE_intWorkflowInstanceKey"), 0, DALQuery.WHERE_HAS_VALUE);

            query3.setWhere("AND", 1, "WORKFLOW_TASK_HISTORY_strPerformerType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);
            query3.setWhere("OR", 0, "WORKFLOW_TASK_HISTORY_strType", "=", "System", 1, DALQuery.WHERE_HAS_VALUE);
            query3.setWhere("AND", 0, "WORKFLOW_TASK_HISTORY_strStatus", "<>", "Not active", 0, DALQuery.WHERE_HAS_VALUE);
            query3.setWhere("AND", 0, "WORKFLOW_TASK_HISTORY_strStatus", "<>", "Active", 0, DALQuery.WHERE_HAS_VALUE);
            rsResultFinal = query3.executeSelect();
            strXML+= WorkspaceChannel.buildSearchXMLFileForTaskHistory("search_task", rsResultFinal, vtViewTaskForm);

            
            // ------------------ BUILD TASK HISTORY FOR GROUP

            vtViewTaskForm = DatabaseSchema.getFormFields("view_task_history");

            rsResultFinal = null;

            

            DALQuery query4 = new DALQuery();

            query4.setDomain("WORKFLOW_TASK_HISTORY", null, null, null);

            query4.setDomain("ORGUSER", "WORKFLOW_TASK_HISTORY_intPerformer", "ORGUSER_intOrgUserKey", "LEFT JOIN");

            query4.setFields(vtViewTaskForm, null);

            query4.setWhere(null, 0, "WORKFLOW_TASK_HISTORY_intWorkflowInstanceKey", "=", runtimeData.getParameter("WORKFLOW_INSTANCE_intWorkflowInstanceKey"), 0, DALQuery.WHERE_HAS_VALUE);

            query4.setWhere("AND", 0, "WORKFLOW_TASK_HISTORY_strPerformerType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

            query4.setWhere("AND", 0, "WORKFLOW_TASK_HISTORY_strStatus", "<>", "Not active", 0, DALQuery.WHERE_HAS_VALUE);
            query4.setWhere("AND", 0, "WORKFLOW_TASK_HISTORY_strStatus", "<>", "Active", 0, DALQuery.WHERE_HAS_VALUE);

                       
            //System.err.println( "Final query ---->\n" + query1.convertSelectQueryToString() );
            
            rsResultFinal = query4.executeSelect();
            strXML+= WorkspaceChannel.buildSearchXMLFileForTaskHistory("search_task", rsResultFinal, vtViewTaskForm);
            
                        

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

        

    }

	

    

    /**

     * Update the current task

     */

    private void doUpdateTask()

    {

        Vector vtUpdateTaskForm = DatabaseSchema.getFormFields("update_task");

        

        try

        {

            // sign off check

            if (runtimeData.getParameter("WORKFLOW_TASK_strSignoff") != null &&

                runtimeData.getParameter("WORKFLOW_TASK_strSignoff").equals("Yes"))

            {

                

                if (runtimeData.getParameter("signoff_pw") == null)

                {

                    // back to view task form

                    buildViewTaskForm();

                    strXML += "<strErrorMessage>" + "Please enter the sign off password." + "</strErrorMessage>";

                    return;

                }

                else if (!Password.isVerified(authToken.getUserIdentifier(), runtimeData.getParameter("signoff_pw")))

                {

                    // back to view task form

                    buildViewTaskForm();

                    strXML += "<strErrorMessage>" + "Please enter the correct password." + "</strErrorMessage>";

                    return;

                }

            }

            

            String strCurrentTaskStatus = runtimeData.getParameter("WORKFLOW_TASK_strStatus");

            

            // only update the task if its staus is "Started" or "Active"

            if (strCurrentTaskStatus.equals("Active") || strCurrentTaskStatus.equals("Started"))

            {

                // if we can lock the record for updating

                if (lockRequest.isValid() && lockRequest.lockWrites())

                {

                    // will do checking for signoff session

                    runtimeData.setParameter("WORKFLOW_TASK_strStatus", "Started");

                    DALSecurityQuery query = new DALSecurityQuery(WORKSPACE_UPDATE_TASK, authToken);

                    query.setDomain("WORKFLOW_TASK", null, null, null);

                    query.setFields(vtUpdateTaskForm, runtimeData);

                    query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"), 0, DALQuery.WHERE_HAS_VALUE);

                    query.executeUpdate();



                    // unlock the record

                    lockRequest.unlockWrites();



                    // back to view task form

                    buildViewTaskForm();

                    

                }

                else

                {

                    // back to view task form

                    buildViewTaskForm();

                    strXML += "<strErrorMessage>" + "Record locking produced some error. Please try again later." + "</strErrorMessage>";

                }

            }

            else

            {

                // back to view task form

                buildViewTaskForm();

                strXML += "<strErrorMessage>A task which is " + strCurrentTaskStatus + " can not be updated.</strErrorMessage>";

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Complete the current task

     */

    private void doCompleteTask()

    {

        Vector vtCompleteTaskForm = DatabaseSchema.getFormFields("complete_task");

        

        try

        {

            // sign off check

            if (runtimeData.getParameter("WORKFLOW_TASK_strSignoff") != null &&

                runtimeData.getParameter("WORKFLOW_TASK_strSignoff").equals("Yes"))

            {

                

                if (runtimeData.getParameter("signoff_pw") == null)

                {

                    // back to view task form

                    buildViewTaskForm();

                    strXML += "<strErrorMessage>" + "Please enter the sign off password." + "</strErrorMessage>";

                    return;

                }

                else if (!Password.isVerified(authToken.getUserIdentifier(), runtimeData.getParameter("signoff_pw")))

                {

                    // back to view task form

                    buildViewTaskForm();

                    strXML += "<strErrorMessage>" + "Please enter the correct password." + "</strErrorMessage>";

                    return;

                }

            }

            

            

            String strCurrentTaskStatus = runtimeData.getParameter("WORKFLOW_TASK_strStatus");

            

            // only update the task if its staus is "Started" or "Active"

            if (strCurrentTaskStatus.equals("Active") || strCurrentTaskStatus.equals("Started"))

            {

                // if we can lock the record for updating

                if (lockRequest.isValid() && lockRequest.lockWrites())

                {

                    String[] strActions = runtimeData.getParameterValues("WORKFLOW_TASK_strAction");

                    boolean hasAction = Boolean.valueOf(runtimeData.getParameter("hasAction")).booleanValue();

                    boolean validActions = true;

                     

                    // user must select an action if the task has one

                    if (hasAction && strActions == null)

                    {

                        // back to view task form

                        buildViewTaskForm();

                        strXML += "<strErrorMessage>" + "Please select an action." + "</strErrorMessage>";

                    }

                    else

                    {   

                        if (strActions != null)

                        {

                            String strActualAction = "";

                            for (int i=0; i < strActions.length; i++)

                            {

                                if (strActions[i].length() == 0)

                                {

                                    validActions = false;

                                    break;

                                }

                                

                                strActualAction += strActions[i] + ",";

                            }

                            

                            if (validActions)

                            {

                                strActualAction = strActualAction.substring(0, strActualAction.length() - 1);

                                runtimeData.setParameter("WORKFLOW_TASK_strAction", strActualAction);

                            }

                        }

                        

                        if (validActions)

                        {

                            //runtimeData.setParameter("WORKFLOW_TASK_strStatus", "Started");

                            DALSecurityQuery query = new DALSecurityQuery(WORKSPACE_UPDATE_TASK, authToken);

                            query.setDomain("WORKFLOW_TASK", null, null, null);

                            query.setFields(vtCompleteTaskForm, runtimeData);

                            query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"), 0, DALQuery.WHERE_HAS_VALUE);

                            query.executeUpdate();



                            // unlock the record

                            lockRequest.unlockWrites();



							//set the task in handovering mode

							WorkflowManager.updateWorkflowTaskHandoverStatus(WorkflowManager.TASK_HANDOVER_YES,

											runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"), query);

							

							// update ix_task, status, date complete

							WorkflowManager.updateTaskStatus("Completed",

															 runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"), 

															 new DALSecurityQuery("", null));



							Calendar currentTime = Calendar.getInstance();

							String strToday = WorkflowManager.getDate(currentTime) + "/" + 

											  WorkflowManager.getMonth(currentTime) + "/" + 

											  currentTime.get(Calendar.YEAR); 



							String strCurrentTime = currentTime.get(Calendar.HOUR_OF_DAY) + ":" +

													currentTime.get(Calendar.MINUTE) + ":" +

													currentTime.get(Calendar.SECOND);



							WorkflowManager.updateTaskCompletedDate(strToday,

																	runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"),

																	new DALSecurityQuery("", null));

							WorkflowManager.updateTaskCompletedTime(strCurrentTime,

																	runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"), 

																	new DALSecurityQuery("", null));
							
							//agus start
							//get the task thread configuration
							String strTaskThreadStatus = WorkflowManager.getTaskThreadStatus(
															runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"),
															new DALSecurityQuery("", null));
							
							if(strTaskThreadStatus != null && strTaskThreadStatus.equals("No"))
							{
								System.err.println("Thread is NOT activated");
                            	WorkflowEngine.finishTask(runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"), 
														  "Completed", new Boolean(true), runtimeData, 
														  new DALSecurityQuery("", null));
							}
							else if(strTaskThreadStatus != null && strTaskThreadStatus.equals("Yes"))
							{
								System.err.println("Thread is activated");
								WorkflowTaskCompletionThread workflowTaskCompletionThread = 
									new WorkflowTaskCompletionThread(runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"),
																	 "Completed", new Boolean(true), runtimeData,
																	 new DALSecurityQuery("", null));
							
								workflowTaskCompletionThread.start();
							}
							else //to cater for existing tasks that do not have thread configuration
							{
								System.err.println("OLD TASK with NO thread");
								WorkflowEngine.finishTask(runtimeData.getParameter("WORKFLOW_TASK_intTaskKey"), 
							 							  "Completed", new Boolean(true), runtimeData, 
														  new DALSecurityQuery("", null));
							}
							//agus end

                            // back to active task list form

                            buildActiveTaskListForm();

                        }

                        else

                        {

                            // back to view task form

                            buildViewTaskForm();

                            strXML += "<strErrorMessage>" + "There is some invalid action selected. Please select again." + "</strErrorMessage>";

                        }

                    }

                }

                else

                {

                    // back to view task form

                    buildViewTaskForm();

                    strXML += "<strErrorMessage>" + "Record locking produced some error. Please try again later." + "</strErrorMessage>";

                }

            }

            else

            {

                // back to view task form

                buildViewTaskForm();

                strXML += "<strErrorMessage>A task which is " + strCurrentTaskStatus + " can not be done.</strErrorMessage>";

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Build create alert form

     */

    private void buildCreateAlertForm()

    {

        strStylesheet = CREATE_ALERT;

        Vector vtCreateAlertForm = DatabaseSchema.getFormFields(CREATE_ALERT);

        

        try

        {

            // clear the current lock

            clearLockRequest();

            

            // if it comes from Org Chart page, rebuild the page with old runtime data

            if (runtimeData.getParameter("source") != null)

            {

                oldRuntimeData.setParameter("WORKSPACE_ALERT_strSendToUserName", runtimeData.getParameter("WORKSPACE_ALERT_strSendToUserName"));

                oldRuntimeData.setParameter("WORKSPACE_ALERT_intSendToUserKey", runtimeData.getParameter("key"));

                oldRuntimeData.setParameter("WORKSPACE_ALERT_strSendToUserType", runtimeData.getParameter("type"));

                

                oldRuntimeData.setParameter("WORKSPACE_ALERT_strSendTo", runtimeData.getParameter("WORKSPACE_ALERT_strSendTo"));

                strXML = QueryChannel.buildFormLabelXMLFile(vtCreateAlertForm) +

                         QueryChannel.buildViewXMLFile(vtCreateAlertForm, oldRuntimeData) +

                         buildUserDropdown(runtimeData.getParameter("WORKSPACE_ALERT_intFromUserKey"));

                strXML += "<WORKSPACE_ALERT_strSendToUserName>" + runtimeData.getParameter("WORKSPACE_ALERT_strSendToUserName") + "</WORKSPACE_ALERT_strSendToUserName>";

            }

            else

            {

                strXML = QueryChannel.buildFormLabelXMLFile(vtCreateAlertForm) +

                         QueryChannel.buildViewXMLFile(vtCreateAlertForm, runtimeData) +

                         buildUserDropdown(runtimeData.getParameter("WORKSPACE_ALERT_intFromUserKey"));

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Save an alert

     */

    private void doSaveAlert()

    {

        Vector vtCreateAlertForm = DatabaseSchema.getFormFields(CREATE_ALERT);

        

        try

        {

            // set date & time

            runtimeData.setParameter("WORKSPACE_ALERT_dtDate", QueryChannel.makeDateFromForm("WORKSPACE_ALERT_dtDate", runtimeData));

            runtimeData.setParameter("WORKSPACE_ALERT_tmTime", QueryChannel.makeTimeFromForm("WORKSPACE_ALERT_tmTime", runtimeData));

            

            // set Origin User

            runtimeData.setParameter("WORKSPACE_ALERT_strFromUser", authToken.getUserIdentifier());

            

            // checks required fields

            String strRequiredFields = QueryChannel.checkRequiredFields(vtCreateAlertForm, runtimeData);

            // checks data validation

            String strDataValidation = QueryChannel.validateData(vtCreateAlertForm, runtimeData);

            

            // if missing some required fields

            // display error message

            if (strRequiredFields != null)

            {

                strStylesheet = CREATE_ALERT;

                strXML = QueryChannel.buildFormLabelXMLFile(vtCreateAlertForm) +

                         QueryChannel.buildViewXMLFile(vtCreateAlertForm, runtimeData);

                strXML += "<strErrorMessage>" + strRequiredFields + "</strErrorMessage>";

            }

            // there is some invalid data

            else if (strDataValidation != null)

            {

                strStylesheet = CREATE_ALERT;

                strXML = QueryChannel.buildFormLabelXMLFile(vtCreateAlertForm) +

                         QueryChannel.buildViewXMLFile(vtCreateAlertForm, runtimeData);

                strXML += "<strErrorMessage>" + strDataValidation + "</strErrorMessage>";

            }

            // everything is OK, save the alert

            else

            {

                DALSecurityQuery query = new DALSecurityQuery(WORKSPACE_CREATE_ALERT, authToken);

                query.setDomain("WORKSPACE_ALERT", null, null, null);

                query.setFields(vtCreateAlertForm, runtimeData);

                query.executeInsert();

                

                // get the key of the inserted record and send it to runtimeData

                //runtimeData.setParameter("WORKSPACE_ALERT_intAlertKey", QueryChannel.getNewestKeyAsString("WORKSPACE_ALERT_intAlertKey"));

                buildAlertListForm();

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Build view alert form

     */

    private void buildViewAlertForm()

    {

        strStylesheet = VIEW_ALERT;

        Vector vtViewAlertForm = DatabaseSchema.getFormFields(VIEW_ALERT);

        

        try

        {

            // if it comes from Org Chart page, rebuild the page with old runtime data

            if (runtimeData.getParameter("source") != null)

            {

                oldRuntimeData.setParameter("WORKSPACE_ALERT_strSendToUserName", runtimeData.getParameter("WORKSPACE_ALERT_strSendToUserName"));

                oldRuntimeData.setParameter("WORKSPACE_ALERT_intSendToUserKey", runtimeData.getParameter("key"));

                oldRuntimeData.setParameter("WORKSPACE_ALERT_strSendToUserType", runtimeData.getParameter("type"));

                strXML = QueryChannel.buildFormLabelXMLFile(vtViewAlertForm) +

                         QueryChannel.buildViewXMLFile(vtViewAlertForm, oldRuntimeData)+

                         buildUserDropdown(runtimeData.getParameter("WORKSPACE_ALERT_intFromUserKey"));

                strXML += "<WORKSPACE_ALERT_strSendToUserName>" + runtimeData.getParameter("WORKSPACE_ALERT_strSendToUserName") + "</WORKSPACE_ALERT_strSendToUserName>";

            }

            else

            {

                // if we can lock the record

                if (buildAlertRecordLock(runtimeData.getParameter("WORKSPACE_ALERT_intAlertKey")))

                {

                    strXML = QueryChannel.buildFormLabelXMLFile(vtViewAlertForm) +

                             QueryChannel.buildViewFromKeyXMLFile(vtViewAlertForm, "WORKSPACE_ALERT_intAlertKey", runtimeData.getParameter("WORKSPACE_ALERT_intAlertKey")) +

                             buildUserDropdown(runtimeData.getParameter("WORKSPACE_ALERT_intFromUserKey"));

                    strXML += "<WORKSPACE_ALERT_strSendToUserName>" + runtimeData.getParameter("WORKSPACE_ALERT_strSendToUserName") + "</WORKSPACE_ALERT_strSendToUserName>";

                }

                else

                {

                    buildCreateAlertForm();

                    strXML += "<strErrorMessage>" + "Record locking produced some error. Please try again later." + "</strErrorMessage>";

                }

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Build alert list form

     */

    private void buildAlertListForm()

    {

        strStylesheet = ALERT_LIST;

        Vector vtViewAlertForm = DatabaseSchema.getFormFields(VIEW_ALERT);

        

        try

        {

            // get the users and user groups related to the current system user

            getUserAndUserGroupWithReassignTask();

            

            DALQuery query = new DALQuery();

            query.setDomain("WORKSPACE_ALERT", null, null, null);

            query.setFields(vtViewAlertForm, null);

            query.setWhere(null, 0, "WORKSPACE_ALERT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "WORKSPACE_ALERT_strStatus", "=", ALERT_NOT_SENT, 0, DALQuery.WHERE_HAS_VALUE);

            

            Enumeration enumUsers = hashUsers.keys();

            Enumeration enumUserGroups = hashUserGroups.keys();

            boolean isFirstUser = true;

            boolean isFirstGroup = true;

            if (enumUsers.hasMoreElements())

            {

                while (enumUsers.hasMoreElements())

                {

                    Object currentUserKey = enumUsers.nextElement();



                    if (isFirstUser && !enumUsers.hasMoreElements())

                    {

                        isFirstUser = false;



                        if (enumUserGroups.hasMoreElements())

                        {

                            query.setWhere("AND", 2, "WORKSPACE_ALERT_strFromUserType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                            query.setWhere("AND", 0, "WORKSPACE_ALERT_intFromUserKey", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                        }

                        else

                        {

                            query.setWhere("AND", 2, "WORKSPACE_ALERT_strFromUserType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                            query.setWhere("AND", 0, "WORKSPACE_ALERT_intFromUserKey", "=", currentUserKey, 2, DALQuery.WHERE_HAS_VALUE);

                        }

                    }

                    else if (isFirstUser)

                    {

                        isFirstUser = false;

                        query.setWhere("AND", 2, "WORKSPACE_ALERT_strFromUserType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 1, "WORKSPACE_ALERT_intFromUserKey", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (!enumUsers.hasMoreElements())

                    {

                        if (enumUserGroups.hasMoreElements())

                            query.setWhere("OR", 0, "WORKSPACE_ALERT_intFromUserKey", "=", currentUserKey, 2, DALQuery.WHERE_HAS_VALUE);

                        else

                            query.setWhere("OR", 0, "WORKSPACE_ALERT_intFromUserKey", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                    }

                    else

                    {

                        query.setWhere("OR", 0, "WORKSPACE_ALERT_intFromUserKey", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }



                }



                while (enumUserGroups.hasMoreElements())

                {

                    Object currentGroupKey = enumUserGroups.nextElement();



                    if (isFirstGroup && !enumUserGroups.hasMoreElements())

                    {

                        isFirstGroup = false;



                        query.setWhere("OR", 1, "WORKSPACE_ALERT_strFromUserType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 0, "WORKSPACE_ALERT_intFromUserKey", "=", currentGroupKey, 2, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (isFirstGroup)

                    {

                        isFirstGroup = false;

                        query.setWhere("OR", 1, "WORKSPACE_ALERT_strFromUserType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 1, "WORKSPACE_ALERT_intFromUserKey", "=", currentGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (!enumUserGroups.hasMoreElements())

                    {

                        query.setWhere("OR", 0, "WORKSPACE_ALERT_intFromUserKey", "=", currentGroupKey, 3, DALQuery.WHERE_HAS_VALUE);

                    }

                    else

                    {

                        query.setWhere("OR", 0, "WORKSPACE_ALERT_intFromUserKey", "=", currentGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                }

            }
            
            
            // ------------------------------ START PAGING FROM HERE ------ LONG ----

            int noOfPages = 0;
            int currentPage = 1;
            String noOfRecords = PropertiesManager.getProperty("neuragenix.platformgenix.RecordPerPage");

            if( (runtimeData.getParameter( "noOfRecords") != null ) && 
                isNumeric( runtimeData.getParameter( "noOfRecords"))){
                noOfRecords = runtimeData.getParameter( "noOfRecords");
            }

            int intNoOfRecords = Integer.parseInt( noOfRecords );

            if( ( runtimeData.getParameter( "currentPage" ) != null ) &&
                isNumeric( runtimeData.getParameter( "currentPage" ))){
                currentPage = Integer.parseInt( runtimeData.getParameter( "currentPage") );
            }

            query.clearLimitOffset();
            ResultSet tRS = query.executeSelect();
            //System.err.println( "Sql: --> " + searchTaskQuery.convertSelectQueryToString());

            tRS.last();
            int allRecords = tRS.getRow();
            tRS = null;



            if( intNoOfRecords < 0) intNoOfRecords = 1;
            //if( intNoOfRecords > 20) intNoOfRecords = 20;

            noOfPages = ( (allRecords - 1) / intNoOfRecords ) + 1;

            if( currentPage < 1 ) currentPage =1;
            if( currentPage > noOfPages ) currentPage = noOfPages;

            query.setLimitOffset( intNoOfRecords, ((currentPage - 1) * intNoOfRecords) );



            strXML = "<currentPage>" + currentPage + "</currentPage>" ;
            strXML += "<noOfPages>" + noOfPages + "</noOfPages>" ;
            strXML += "<noOfRecords>" + noOfRecords + "</noOfRecords>" ;
            
            strXML += QueryChannel.buildAddFormXMLFile( DatabaseSchema.getFormFields("record_per_page"));

            // ------------------------------ END PAGING HERE ------ LONG ----

            ResultSet rsOutgoingAlert = query.executeSelect();



            // getting incoming alert

            query = new DALQuery();

            query.setDomain("WORKSPACE_ALERT", null, null, null);

            query.setFields(vtViewAlertForm, null);

            query.setWhere(null, 0, "WORKSPACE_ALERT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "WORKSPACE_ALERT_strStatus", "=", ALERT_SENT, 0, DALQuery.WHERE_HAS_VALUE);

            

            enumUsers = hashUsers.keys();

            enumUserGroups = hashUserGroups.keys();

            isFirstUser = true;

            isFirstGroup = true;

            if (enumUsers.hasMoreElements())

            {

                while (enumUsers.hasMoreElements())

                {

                    Object currentUserKey = enumUsers.nextElement();



                    if (isFirstUser && !enumUsers.hasMoreElements())

                    {

                        isFirstUser = false;



                        if (enumUserGroups.hasMoreElements())

                        {

                            query.setWhere("AND", 2, "WORKSPACE_ALERT_strSendToUserType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                            query.setWhere("AND", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                        }

                        else

                        {

                            query.setWhere("AND", 2, "WORKSPACE_ALERT_strSendToUserType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                            query.setWhere("AND", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentUserKey, 2, DALQuery.WHERE_HAS_VALUE);

                        }

                    }

                    else if (isFirstUser)

                    {

                        isFirstUser = false;

                        query.setWhere("AND", 2, "WORKSPACE_ALERT_strSendToUserType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 1, "WORKSPACE_ALERT_intSendToUserKey", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (!enumUsers.hasMoreElements())

                    {

                        if (enumUserGroups.hasMoreElements())

                            query.setWhere("OR", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentUserKey, 2, DALQuery.WHERE_HAS_VALUE);

                        else

                            query.setWhere("OR", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                    }

                    else

                    {

                        query.setWhere("OR", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }



                }



                while (enumUserGroups.hasMoreElements())

                {

                    Object currentGroupKey = enumUserGroups.nextElement();



                    if (isFirstGroup && !enumUserGroups.hasMoreElements())

                    {

                        isFirstGroup = false;



                        query.setWhere("OR", 1, "WORKSPACE_ALERT_strSendToUserType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentGroupKey, 2, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (isFirstGroup)

                    {

                        isFirstGroup = false;

                        query.setWhere("OR", 1, "WORKSPACE_ALERT_strSendToUserType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 1, "WORKSPACE_ALERT_intSendToUserKey", "=", currentGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (!enumUserGroups.hasMoreElements())

                    {

                        query.setWhere("OR", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentGroupKey, 3, DALQuery.WHERE_HAS_VALUE);

                    }

                    else

                    {

                        query.setWhere("OR", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                }

            }
            
            //-- INCOMING ALERTS PAGING --//
            
            noOfPages = 0;
            currentPage = 1;
            noOfRecords = PropertiesManager.getProperty("neuragenix.platformgenix.RecordPerPage");

            if( (runtimeData.getParameter( "noOfRecordsI") != null ) && 
                isNumeric( runtimeData.getParameter( "noOfRecordsI"))){
                noOfRecords = runtimeData.getParameter( "noOfRecordsI");
            }

            intNoOfRecords = Integer.parseInt( noOfRecords );

            if( ( runtimeData.getParameter( "currentPageI" ) != null ) &&
                isNumeric( runtimeData.getParameter( "currentPageI" ))){
                currentPage = Integer.parseInt( runtimeData.getParameter( "currentPageI") );
            }

            query.clearLimitOffset();
            tRS = query.executeSelect();
            //System.err.println( "Sql: --> " + searchTaskQuery.convertSelectQueryToString());

            tRS.last();
            allRecords = tRS.getRow();
            tRS = null;



            if( intNoOfRecords < 0) intNoOfRecords = 1;
            //if( intNoOfRecords > 20) intNoOfRecords = 20;

            noOfPages = ( (allRecords - 1) / intNoOfRecords ) + 1;

            if( currentPage < 1 ) currentPage =1;
            if( currentPage > noOfPages ) currentPage = noOfPages;

            query.setLimitOffset( intNoOfRecords, ((currentPage - 1) * intNoOfRecords) );



            strXML += "<currentPageI>" + currentPage + "</currentPageI>" ;
            strXML += "<noOfPagesI>" + noOfPages + "</noOfPagesI>" ;
            strXML += "<noOfRecordsI>" + noOfRecords + "</noOfRecordsI>" ;

            //System.err.println(query.convertSelectQueryToString());

            ResultSet rsIncomingAlert = query.executeSelect();

            

            if (hashUsers.size() > 0)

            {

                // if we can build lock record for the list

                if (buildAlertListRecordLock(rsOutgoingAlert))

                {

                    strXML += QueryChannel.buildColumnLabelXMLFile(vtViewAlertForm) +

                             QueryChannel.buildSearchXMLFile("search_outgoing_alert", rsOutgoingAlert, vtViewAlertForm) +

                             QueryChannel.buildSearchXMLFile("search_incoming_alert", rsIncomingAlert, vtViewAlertForm);

                }

                else

                {

                    strXML += QueryChannel.buildColumnLabelXMLFile(vtViewAlertForm) +

                             "<strErrorMessage>" + "Record locking produced some error. Please try again later." + "</strErrorMessage>";

                }

            }

            else

            {

                strXML += QueryChannel.buildColumnLabelXMLFile(vtViewAlertForm);

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Update the current alert

     */

    private void doUpdateAlert()

    {

        strStylesheet = VIEW_ALERT;

        Vector vtViewAlertForm = DatabaseSchema.getFormFields(VIEW_ALERT);

        

        try

        {

            // set date & time

            runtimeData.setParameter("WORKSPACE_ALERT_dtDate", QueryChannel.makeDateFromForm("WORKSPACE_ALERT_dtDate", runtimeData));

            runtimeData.setParameter("WORKSPACE_ALERT_tmTime", QueryChannel.makeTimeFromForm("WORKSPACE_ALERT_tmTime", runtimeData));

            

            // set Origin User

            runtimeData.setParameter("WORKSPACE_ALERT_strFromUser", authToken.getUserIdentifier());

            

            // checks required fields

            String strRequiredFields = QueryChannel.checkRequiredFields(vtViewAlertForm, runtimeData);

            // checks data validation

            String strDataValidation = QueryChannel.validateData(vtViewAlertForm, runtimeData);

            

            // if missing some required fields

            // display error message

            if (strRequiredFields != null)

            {

                buildViewAlertForm();

                strXML += "<strErrorMessage>" + strRequiredFields + "</strErrorMessage>";

            }

            // there is some invalid data

            else if (strDataValidation != null)

            {

                buildViewAlertForm();

                strXML += "<strErrorMessage>" + strDataValidation + "</strErrorMessage>";

            }

            // all data is OK

            else

            {

                // update the record only if we can lock it

                if (lockRequest.isValid() && lockRequest.lockWrites())

                {

                    DALSecurityQuery query = new DALSecurityQuery(WORKSPACE_UPDATE_ALERT, authToken);

                    query.setDomain("WORKSPACE_ALERT", null, null, null);

                    query.setFields(vtViewAlertForm, runtimeData);

                    query.setWhere(null, 0, "WORKSPACE_ALERT_intAlertKey", "=", runtimeData.getParameter("WORKSPACE_ALERT_intAlertKey"), 0, DALQuery.WHERE_HAS_VALUE);

                    query.executeUpdate();



                    // unlock the record

                    lockRequest.unlockWrites();

                    

                    // back to alert list

                    buildAlertListForm();

                }

                else

                {

                    buildViewAlertForm();

                    strXML += "<strErrorMessage>" + "Record locking produced some error. Please try again later." + "</strErrorMessage>";

                }

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Delete the current alert

     */

    private void doDeleteAlert()

    {

        try

        {

            // delete the record only if we can lock it

            if (lockRequest.isValid() && lockRequest.lockWrites())

            {

                DALSecurityQuery query = new DALSecurityQuery(WORKSPACE_DELETE_ALERT, authToken);

                query.setDomain("WORKSPACE_ALERT", null, null, null);

                query.setField("WORKSPACE_ALERT_intDeleted", "-1");

                query.setWhere(null, 0, "WORKSPACE_ALERT_intAlertKey", "=", runtimeData.getParameter("WORKSPACE_ALERT_intAlertKey"), 0, DALQuery.WHERE_HAS_VALUE);

                query.executeUpdate();

                lockRequest.unlockWrites();

                

                // back to alert list

                buildAlertListForm();

            }

            else

            {

                buildViewAlertForm();

                strXML += "<strErrorMessage>" + "Record locking produced some error. Please try again later." + "</strErrorMessage>";

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Build Org Chart form

     */

    private void buildOrgChartForm(String strSourcePage, String strFieldName)

    {

        strStylesheet = ORG_CHART;

        DefaultMutableTreeNode orgChartTree = OrgChart.buildOrgTree(hashExpanded, "group", "user" );

        strXML = OrgChart.toXML(orgChartTree, hashExpanded) +

                 "<source_page>" + strSourcePage + "</source_page>" +

                 "<field_name>" + strFieldName + "</field_name>";

    }

    

    /**

     * Save the reassign task

     */

    private void doSaveReassignTask()

    {

        Vector vtSaveReassignTask = DatabaseSchema.getFormFields("add_reassign_task");

        

        try

        {

            DALSecurityQuery query = new DALSecurityQuery(WORKSPACE_REASSIGN_TASK, authToken);

            query.setDomain("WORKFLOW_TASK", null, null, null);

            query.setField("WORKFLOW_TASK_intPerformer", null);

            query.setField("WORKFLOW_TASK_strPerformerType", null);

            query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_intTaskKey"), 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResult = query.executeSelect();



            if (rsResult.next())

            {

                runtimeData.setParameter("WORKFLOW_TASK_REASSIGN_intFromUserKey", rsResult.getString("WORKFLOW_TASK_intPerformer"));

                runtimeData.setParameter("WORKFLOW_TASK_REASSIGN_strFromUserType", rsResult.getString("WORKFLOW_TASK_strPerformerType"));

            }

                

            // checks required fields

            String strRequiredFields = QueryChannel.checkRequiredFields(vtSaveReassignTask, runtimeData);

            // checks data validation

            String strDataValidation = QueryChannel.validateData(vtSaveReassignTask, runtimeData);

            

            // there is no data for required fields

            if (strRequiredFields != null)

            {

                buildReassignTaskForm();

                strXML += "<strErrorMessage>" + strRequiredFields + "</strErrorMessage>";

            }

            // there is some invalid data

            else if (strDataValidation != null)

            {

                buildReassignTaskForm();

                strXML += "<strErrorMessage>" + strDataValidation + "</strErrorMessage>";

            }

            else

            {

                // if we can lock the task, do reassign

                if (lockRequest.isValid() && lockRequest.lockWrites())

                {

                    // update task

                    query.reset();

                    query.setDomain("WORKFLOW_TASK", null, null, null);

                    query.setField("WORKFLOW_TASK_intPerformer", runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_intToUserKey"));

                    query.setField("WORKFLOW_TASK_strPerformerType", runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_strToUserType"));

                    query.setWhere(null, 0, "WORKFLOW_TASK_intTaskKey", "=", runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_intTaskKey"), 0, DALQuery.WHERE_HAS_VALUE);

                    query.executeUpdate();

                    

                    // unlock the record

                    lockRequest.unlockWrites();

                    

                    // add a reassign task record

                    query.reset();

                    query.setDomain("WORKFLOW_TASK_REASSIGN", null, null, null);

                    query.setFields(vtSaveReassignTask, runtimeData);

                    query.executeInsert();



                    buildActiveTaskListForm();

                }

                else

                {

                    buildReassignTaskForm();

                    strXML += "<strErrorMessage>" + strRequiredFields + "</strErrorMessage>";

                }

            }

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

        

    }

    

    /**

     * Save the reassign tasks

     */

    private void doSaveReassignTasks()

    {

        Vector vtUpdateReassignTask = DatabaseSchema.getFormFields("update_reassign_task");

        Vector vtReassignTasks = DatabaseSchema.getFormFields("reassign_tasks");

        Vector vtSaveReassignTask = DatabaseSchema.getFormFields("add_reassign_task");

        

        try

        {

            

            // checks required fields

            String strRequiredFields = QueryChannel.checkRequiredFields(vtReassignTasks, runtimeData);

            // checks data validation

            String strDataValidation = QueryChannel.validateData(vtReassignTasks, runtimeData);

            

            // there is no data for required fields

            if (strRequiredFields != null)

            {

                buildReassignTasksForm();

                strXML += "<strErrorMessage>" + strRequiredFields + "</strErrorMessage>";

            }

            // there is some invalid data

            else if (strDataValidation != null)

            {

                buildReassignTasksForm();

                strXML += "<strErrorMessage>" + strDataValidation + "</strErrorMessage>";

            }

            else

            {

                

                // insert a record to task reassign table

                DALSecurityQuery query = new DALSecurityQuery(WORKSPACE_REASSIGN_TASKS, authToken);

                query.setDomain("WORKFLOW_TASK_REASSIGN", null, null, null);

                query.setFields(vtSaveReassignTask, runtimeData);

                query.executeInsert();

                

                buildReassignTasksForm();

            }

            

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Delete the reassign tasks

     */

    private void doDeleteReassignTask()

    {

        

        try

        {

            DALQuery query = new DALQuery();

            query.setDomain("WORKFLOW_TASK_REASSIGN", null, null, null);

            query.setField("WORKFLOW_TASK_REASSIGN_intDeleted", "-1");

            query.setWhere(null, 0, "WORKFLOW_TASK_REASSIGN_intTaskReassignKey", "=", runtimeData.getParameter("WORKFLOW_TASK_REASSIGN_intTaskReassignKey"), 0, DALQuery.WHERE_HAS_VALUE);

            query.executeUpdate();

            

            buildReassignTasksForm();

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /**

     * Build record locking for search task list

     */

    private boolean buildSearchTaskRecordLock(ResultSet rsResult)

    {

        try

        {

            // clear the current lock

            clearLockRequest();

            

            // create new LockRequest object

            lockRequest = new LockRequest(authToken);

            

            // register the lock to session manager

            SessionManager.addLockRequestObject(strSessionUniqueID, "CWorkspace", lockRequest);

            

            while (rsResult.next())

            {

                lockRequest.addLock("WORKFLOW_TASK", rsResult.getString("WORKFLOW_TASK_intTaskKey"), LockRecord.READ_ONLY);

            }

            rsResult.beforeFirst();

            

            return lockRequest.lockDelayWrite();

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

        

        return false;

    }

    

    /**

     * Build record locking for alert list

     */

    private boolean buildAlertListRecordLock(ResultSet rsResult)

    {

        try

        {

            // clear the current lock

            clearLockRequest();

            

            // create new LockRequest object

            lockRequest = new LockRequest(authToken);

            

            // register the lock to session manager

            SessionManager.addLockRequestObject(strSessionUniqueID, "CWorkspace", lockRequest);

            

            while (rsResult.next())

            {

                lockRequest.addLock("WORKSPACE_ALERT", rsResult.getString("WORKSPACE_ALERT_intAlertKey"), LockRecord.READ_ONLY);

            }

            rsResult.beforeFirst();

            

            return lockRequest.lockDelayWrite();

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

        

        return false;

    }

    

    /**

     * Build record locking for a task

     */

    private boolean buildTaskRecordLock(String intTaskKey)

    {

        try

        {

            // clear the current lock

            clearLockRequest();

            

            // create new LockRequest object

            lockRequest = new LockRequest(authToken);

            

            lockRequest.addLock("WORKFLOW_TASK", intTaskKey, LockRecord.READ_WRITE);

            

            // register the lock to session manager

            SessionManager.addLockRequestObject(strSessionUniqueID, "CWorkspace", lockRequest);

            

            return lockRequest.lockDelayWrite();

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

        

        return false;

    }

    

    /**

     * Build record locking for a alert

     */

    private boolean buildAlertRecordLock(String intAlertKey)

    {

        try

        {

            // clear the current lock

            clearLockRequest();

            

            // create new LockRequest object

            lockRequest = new LockRequest(authToken);

            

            lockRequest.addLock("WORKSPACE_ALERT", intAlertKey, LockRecord.READ_WRITE);

            

            // register the lock to session manager

            SessionManager.addLockRequestObject(strSessionUniqueID, "CWorkspace", lockRequest);

            

            return lockRequest.lockDelayWrite();

        }

        catch(Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

        

        return false;

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

                LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

        finally

        {

            lockRequest = null;

        }

    }

    

    /** Get all users and user groups related to the current system user

     */

    private void getUserAndUserGroup()

    {

        hashUsers = new Hashtable(5);

        hashUserGroups = new Hashtable(5);

        

        try

        {

            // getting all users, user groups related to the current system users

            DALQuery query = new DALQuery();

            query.setDomain("ORGUSER", null, null, null);

            query.setDomain("ORGUSERGROUP", "ORGUSER_intOrgUserKey", "ORGUSERGROUP_intOrgUserKey", "INNER JOIN");

            query.setField("ORGUSER_intOrgUserKey", null);

            query.setField("ORGUSER_strFirstName", null);

            query.setField("ORGUSER_strLastName", null);

            query.setField("ORGUSERGROUP_intOrgGroupKey", null);

            query.setWhere(null, 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "ORGUSERGROUP_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "ORGUSER_strSystemUser", "=", authToken.getUserIdentifier(), 0, DALQuery.WHERE_HAS_VALUE);



            ResultSet rsResult = query.executeSelect();

            while (rsResult.next())

            {

                // save the group key into hashtable

                hashUserGroups.put(rsResult.getString("ORGUSERGROUP_intOrgGroupKey"), "group key");

                

                // save user key and name into hashtable

                hashUsers.put(rsResult.getString("ORGUSER_intOrgUserKey"), rsResult.getString("ORGUSER_strFirstName") + " " + rsResult.getString("ORGUSER_strLastName"));

            }

            

            query = new DALQuery();

            query.setDomain("ORGUSER", null, null, null);

            //query.setDomain("ORGUSERGROUP", "ORGUSER_intOrgUserKey", "ORGUSERGROUP_intOrgUserKey", "INNER JOIN");

            query.setField("ORGUSER_intOrgUserKey", null);

            query.setField("ORGUSER_strFirstName", null);

            query.setField("ORGUSER_strLastName", null);

            //query.setField("ORGUSERGROUP_intOrgGroupKey", null);

            query.setWhere(null, 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "ORGUSER_strSystemUser", "=", authToken.getUserIdentifier(), 0, DALQuery.WHERE_HAS_VALUE);



            rsResult = query.executeSelect();

            while (rsResult.next())

            {

                // save the group key into hashtable

                //hashUserGroups.put(rsResult.getString("ORGUSERGROUP_intOrgGroupKey"), "group key");

                

                // save user key and name into hashtable

                hashUsers.put(rsResult.getString("ORGUSER_intOrgUserKey"), rsResult.getString("ORGUSER_strFirstName") + " " + rsResult.getString("ORGUSER_strLastName"));

            }

            

        }

        catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    /** Get all users and user groups related to the current system user and

     *  all reassign users

     */

    private void getUserAndUserGroupWithReassignTask()

    {

        hashUsers = new Hashtable(5);

        hashUserGroups = new Hashtable(5);

        

        Hashtable hashReassignUsers = new Hashtable(5);

        Hashtable hashReassignGroups = new Hashtable(5);

        

        try

        {

            // getting all users, user groups related to the current system users

            DALQuery query = new DALQuery();

            query.setDomain("ORGUSER", null, null, null);

            query.setDomain("ORGUSERGROUP", "ORGUSER_intOrgUserKey", "ORGUSERGROUP_intOrgUserKey", "INNER JOIN");

            query.setField("ORGUSER_intOrgUserKey", null);

            query.setField("ORGUSER_strFirstName", null);

            query.setField("ORGUSER_strLastName", null);

            query.setField("ORGUSERGROUP_intOrgGroupKey", null);

            query.setWhere(null, 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "ORGUSERGROUP_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "ORGUSER_strSystemUser", "=", authToken.getUserIdentifier(), 0, DALQuery.WHERE_HAS_VALUE);



            ResultSet rsResult = query.executeSelect();

            while (rsResult.next())

            {

                // save the group key into hashtable

                hashUserGroups.put(rsResult.getString("ORGUSERGROUP_intOrgGroupKey"), "group key");

                

                // save user key and name into hashtable

                hashUsers.put(rsResult.getString("ORGUSER_intOrgUserKey"), rsResult.getString("ORGUSER_strFirstName") + " " + rsResult.getString("ORGUSER_strLastName"));

            }

            

            query = new DALQuery();

            query.setDomain("ORGUSER", null, null, null);

            query.setField("ORGUSER_intOrgUserKey", null);

            query.setField("ORGUSER_strFirstName", null);

            query.setField("ORGUSER_strLastName", null);

            query.setWhere(null, 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "ORGUSER_strSystemUser", "=", authToken.getUserIdentifier(), 0, DALQuery.WHERE_HAS_VALUE);



            rsResult = query.executeSelect();

            while (rsResult.next())

            {

                // save the group key into hashtable

                //hashUserGroups.put(rsResult.getString("ORGUSERGROUP_intOrgGroupKey"), "group key");

                

                // save user key and name into hashtable

                hashUsers.put(rsResult.getString("ORGUSER_intOrgUserKey"), rsResult.getString("ORGUSER_strFirstName") + " " + rsResult.getString("ORGUSER_strLastName"));

            }

            

            // getting users & groups from reassign table

            query = new DALQuery();

            query.setDomain("WORKFLOW_TASK_REASSIGN", null, null, null);

            query.setField("WORKFLOW_TASK_REASSIGN_intFromUserKey", null);

            query.setField("WORKFLOW_TASK_REASSIGN_strFromUserType", null);

            query.setWhere(null, 0, "WORKFLOW_TASK_REASSIGN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "WORKFLOW_TASK_REASSIGN_intTaskKey", "=", "-1", 0, DALQuery.WHERE_HAS_VALUE);

            

            Enumeration enumUsers = hashUsers.keys();

            Enumeration enumUserGroups = hashUserGroups.keys();

            boolean isFirstUser = true;

            boolean isFirstGroup = true;

            if (enumUsers.hasMoreElements())

            {

                while (enumUsers.hasMoreElements())

                {

                    Object currentUserKey = enumUsers.nextElement();



                    if (isFirstUser && !enumUsers.hasMoreElements())

                    {

                        isFirstUser = false;



                        if (enumUserGroups.hasMoreElements())

                        {

                            query.setWhere("AND", 2, "WORKFLOW_TASK_REASSIGN_strToUserType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                            query.setWhere("AND", 0, "WORKFLOW_TASK_REASSIGN_intToUserKey", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                        }

                        else

                        {

                            query.setWhere("AND", 2, "WORKFLOW_TASK_REASSIGN_strToUserType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                            query.setWhere("AND", 0, "WORKFLOW_TASK_REASSIGN_intToUserKey", "=", currentUserKey, 2, DALQuery.WHERE_HAS_VALUE);

                        }

                    }

                    else if (isFirstUser)

                    {

                        isFirstUser = false;

                        query.setWhere("AND", 2, "WORKFLOW_TASK_REASSIGN_strToUserType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 1, "WORKFLOW_TASK_REASSIGN_intToUserKey", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (!enumUsers.hasMoreElements())

                    {

                        if (enumUserGroups.hasMoreElements())

                            query.setWhere("OR", 0, "WORKFLOW_TASK_REASSIGN_intToUserKey", "=", currentUserKey, 2, DALQuery.WHERE_HAS_VALUE);

                        else

                            query.setWhere("OR", 0, "WORKFLOW_TASK_REASSIGN_intToUserKey", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                    }

                    else

                    {

                        query.setWhere("OR", 0, "WORKFLOW_TASK_REASSIGN_intToUserKey", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }



                }



                while (enumUserGroups.hasMoreElements())

                {

                    Object currentGroupKey = enumUserGroups.nextElement();



                    if (isFirstGroup && !enumUserGroups.hasMoreElements())

                    {

                        isFirstGroup = false;



                        query.setWhere("OR", 1, "WORKFLOW_TASK_REASSIGN_strToUserType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 0, "WORKFLOW_TASK_REASSIGN_intToUserKey", "=", currentGroupKey, 2, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (isFirstGroup)

                    {

                        isFirstGroup = false;

                        query.setWhere("OR", 1, "WORKFLOW_TASK_REASSIGN_strToUserType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 1, "WORKFLOW_TASK_REASSIGN_intToUserKey", "=", currentGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (!enumUserGroups.hasMoreElements())

                    {

                        query.setWhere("OR", 0, "WORKFLOW_TASK_REASSIGN_intToUserKey", "=", currentGroupKey, 3, DALQuery.WHERE_HAS_VALUE);

                    }

                    else

                    {

                        query.setWhere("OR", 0, "WORKFLOW_TASK_REASSIGN_intToUserKey", "=", currentGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                }

            }

            

            rsResult = query.executeSelect();

            while (rsResult.next())

            {

                String strFromUserType = rsResult.getString("WORKFLOW_TASK_REASSIGN_strFromUserType");

                

                // save user key and name into hashtable

                if (strFromUserType.equals("User"))

                    hashReassignUsers.put(rsResult.getString("WORKFLOW_TASK_REASSIGN_intFromUserKey"), "");

                else

                    hashReassignGroups.put(rsResult.getString("WORKFLOW_TASK_REASSIGN_intFromUserKey"), "");

            }

            

            // getting users & groups from reassign table

            query = new DALQuery();

            query.setDomain("WORKFLOW_TASK_REASSIGN", null, null, null);

            query.setField("WORKFLOW_TASK_REASSIGN_intFromUserKey", null);

            query.setField("WORKFLOW_TASK_REASSIGN_strFromUserType", null);

            query.setWhere(null, 0, "WORKFLOW_TASK_REASSIGN_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "WORKFLOW_TASK_REASSIGN_intTaskKey", "=", "-1", 0, DALQuery.WHERE_HAS_VALUE);

            rsResult = query.executeSelect();

            while (rsResult.next())

            {

                String strFromUserType = rsResult.getString("WORKFLOW_TASK_REASSIGN_strFromUserType");

                

                // save user key and name into hashtable

                if (strFromUserType.equals("User"))

                {

                    hashUsers.remove(rsResult.getString("WORKFLOW_TASK_REASSIGN_intFromUserKey"));

                }

                else

                    hashUserGroups.remove(rsResult.getString("WORKFLOW_TASK_REASSIGN_intFromUserKey"));

            }

            

            hashUsers.putAll(hashReassignUsers);

            hashUserGroups.putAll(hashReassignGroups);

        }

        catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    

    

    /** Output channel content to the portal

     *  @param out a sax document handler

     */

    public void renderXML(ContentHandler out) throws PortalException

    {



        // Create a new XSLT styling engine

        XSLT xslt = new XSLT(this);



        //System.err.println("strXML: -------------------------------------------> " + strXML);

        // pass the result xml to the styling engine.

        xslt.setXML(strXML);



        // specify the stylesheet selector

        xslt.setXSL("CWorkspace.ssl", strStylesheet, runtimeData.getBrowserInfo());



        // set parameters that the stylesheet needs.

        xslt.setStylesheetParameter("baseActionURL", runtimeData.getBaseActionURL());



        org.jasig.portal.UPFileSpec upfTmp = new org.jasig.portal.UPFileSpec(runtimeData.getBaseActionURL());



        // set case channel URL param

        upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CCase"));

        xslt.setStylesheetParameter("caseChannelURL", upfTmp.getUPFile());

        xslt.setStylesheetParameter( "caseChannelTabOrder", SessionManager.getTabOrder( authToken, "CCase") );



        // set smartform channel URL param

        upfTmp.setTargetNodeId(SessionManager.getChannelID(strSessionUniqueID, "CSmartform"));

        xslt.setStylesheetParameter("smartformChannelURL", upfTmp.getUPFile());

        xslt.setStylesheetParameter( "smartformChannelTabOrder", SessionManager.getTabOrder( authToken, "CSmartform") );

        

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

        String strTempErrorXML  = "<?xml version=\"1.0\" encoding=\"utf-8\"?><securityerror>"

                + "<errorstring>No Permission to " + aFunctionalArea + "</errorstring>"

                + "<errortext>The " + staticData.getPerson().getFullName() 

                + " is not authorised to " + aFunctionalArea + "</errortext>"

                + "<errordata></errordata>"

                + "</securityerror>";

        return strTempErrorXML;      

    }

    

    /** Build XML string for user dropdown

     */

    private String buildUserDropdown(String strOrgUserKey)

    {

        StringBuffer strResult = new StringBuffer();

        

        try

        {

            DALQuery query = new DALQuery();

            query.setDomain("ORGUSER", null, null, null);

            query.setField("ORGUSER_intOrgUserKey", null);

            query.setField("ORGUSER_strFirstName", null);

            query.setField("ORGUSER_strLastName", null);

            query.setWhere(null, 0, "ORGUSER_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "ORGUSER_strSystemUser", "=", authToken.getUserIdentifier(), 0, DALQuery.WHERE_HAS_VALUE);

            ResultSet rsResult = query.executeSelect();

            

            while (rsResult.next())

            {

                if (strOrgUserKey == null || !rsResult.getString("ORGUSER_intOrgUserKey").equals(strOrgUserKey))

                    strResult.append("<OrgUser selected=\"0\">");

                else

                    strResult.append("<OrgUser selected=\"1\">");

                strResult.append("<ORGUSER_intOrgUserKey>" + rsResult.getString("ORGUSER_intOrgUserKey") + "</ORGUSER_intOrgUserKey>");

                strResult.append("<ORGUSER_strOrgUserName>" + rsResult.getString("ORGUSER_strFirstName") + " " + rsResult.getString("ORGUSER_strLastName") + "</ORGUSER_strOrgUserName>");

                strResult.append("</OrgUser>");

            }

        }

        catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

        

        return strResult.toString();

    }

	

    /** Check if the user has incoming alerts

     * @return blResult if it is true that means the user has incoming alert

     */

    private boolean isHasIncomingAlerts()

    {

        boolean blResult = false;

        Vector vtViewAlertForm = DatabaseSchema.getFormFields(VIEW_ALERT);

        

        try

        {

            // get the users and user groups related to the current system user

            getUserAndUserGroupWithReassignTask();

            

            if (hashUsers.size() == 0)

                return false;

            

            DALQuery query = new DALQuery();

            query.setDomain("WORKSPACE_ALERT", null, null, null);

            query.setFields(vtViewAlertForm, null);

            query.setWhere(null, 0, "WORKSPACE_ALERT_intDeleted", "=", "0", 0, DALQuery.WHERE_HAS_VALUE);

            query.setWhere("AND", 0, "WORKSPACE_ALERT_strStatus", "=", ALERT_SENT, 0, DALQuery.WHERE_HAS_VALUE);

            

            Enumeration enumUsers = hashUsers.keys();

            Enumeration enumUserGroups = hashUserGroups.keys();

            boolean isFirstUser = true;

            boolean isFirstGroup = true;

            if (enumUsers.hasMoreElements())

            {

                while (enumUsers.hasMoreElements())

                {

                    Object currentUserKey = enumUsers.nextElement();



                    if (isFirstUser && !enumUsers.hasMoreElements())

                    {

                        isFirstUser = false;



                        if (enumUserGroups.hasMoreElements())

                        {

                            query.setWhere("AND", 2, "WORKSPACE_ALERT_strSendToUserType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                            query.setWhere("AND", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                        }

                        else

                        {

                            query.setWhere("AND", 2, "WORKSPACE_ALERT_strSendToUserType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                            query.setWhere("AND", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentUserKey, 2, DALQuery.WHERE_HAS_VALUE);

                        }

                    }

                    else if (isFirstUser)

                    {

                        isFirstUser = false;

                        query.setWhere("AND", 2, "WORKSPACE_ALERT_strSendToUserType", "=", "User", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 1, "WORKSPACE_ALERT_intSendToUserKey", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (!enumUsers.hasMoreElements())

                    {

                        if (enumUserGroups.hasMoreElements())

                            query.setWhere("OR", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentUserKey, 2, DALQuery.WHERE_HAS_VALUE);

                        else

                            query.setWhere("OR", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentUserKey, 1, DALQuery.WHERE_HAS_VALUE);

                    }

                    else

                    {

                        query.setWhere("OR", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentUserKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }



                }



                while (enumUserGroups.hasMoreElements())

                {

                    Object currentGroupKey = enumUserGroups.nextElement();



                    if (isFirstGroup && !enumUserGroups.hasMoreElements())

                    {

                        isFirstGroup = false;



                        query.setWhere("OR", 1, "WORKSPACE_ALERT_strSendToUserType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentGroupKey, 2, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (isFirstGroup)

                    {

                        isFirstGroup = false;

                        query.setWhere("OR", 1, "WORKSPACE_ALERT_strSendToUserType", "=", "Group", 0, DALQuery.WHERE_HAS_VALUE);

                        query.setWhere("AND", 1, "WORKSPACE_ALERT_intSendToUserKey", "=", currentGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                    else if (!enumUserGroups.hasMoreElements())

                    {

                        query.setWhere("OR", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentGroupKey, 3, DALQuery.WHERE_HAS_VALUE);

                    }

                    else

                    {

                        query.setWhere("OR", 0, "WORKSPACE_ALERT_intSendToUserKey", "=", currentGroupKey, 0, DALQuery.WHERE_HAS_VALUE);

                    }

                }

            }

            ResultSet rsIncomingAlert = query.executeSelect();

            

            blResult = rsIncomingAlert.next();

        }

        catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

        

        return blResult;

    }

    

    /** Mark the alert as read one, so it will not be showed in the alert list

     */

    private void markAlertAsRead()

    {

        try

        {

            DALSecurityQuery query = new DALSecurityQuery(WORKSPACE_DELETE_ALERT, authToken);

            query.setDomain("WORKSPACE_ALERT", null, null, null);

            query.setField("WORKSPACE_ALERT_strStatus", MARK_ALERT_AS_READ);

            

            query.setWhere(null, 0, "WORKSPACE_ALERT_intAlertKey", "=", runtimeData.getParameter("WORKSPACE_ALERT_intAlertKey"), 0, DALQuery.WHERE_HAS_VALUE);

            query.executeUpdate();

        }

        catch (Exception e)

        {

            LogService.instance().log(LogService.ERROR, "Unknown error in Workspace Channel - " + e.toString(), e);

        }

    }

    private boolean isNumeric(String s){

        

        String validChars = "0123456789";

        boolean isNumber = true;

        if( s.length() == 0 ) return false;
        for (int i = 0; i < s.length() && isNumber; i++){

            char c = s.charAt(i);

            if (validChars.indexOf(c) == -1){

                return false;

            }

        }

        return true;

    }

}

